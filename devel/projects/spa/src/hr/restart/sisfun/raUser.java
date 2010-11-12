/****license*****************************************************************
**   file: raUser.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.sisfun;
import hr.restart.baza.Condition;
import hr.restart.baza.Kljucevi;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.Pravagrus;
import hr.restart.baza.Pravauser;
import hr.restart.baza.dM;
import hr.restart.help.MsgDispatcher;
import hr.restart.util.IntParam;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 *
 * <p>Title: raUser</p>
 * <p>Description: Klasa sa metodama za lockanje i provjeru prava.</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: REST-ART</p>
 * @author ab.f
 * @version 1.0
 */

public class raUser {

  sysoutTEST sys = new sysoutTEST(false);
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  private String m_user = "";
  private String m_grus = "";
  private String m_ime_usera = "";
  private boolean superuser = false, ogranicen = false;
  private static String softLockEnabledInProperties = IntParam.getTag("softlock");
  protected static raUser usr;

  StorageDataSet prava = new StorageDataSet();
//  StorageDataSet appr = new StorageDataSet();

  protected raUser() {
    prava.setColumns(new Column[] {
//      (Column) dm.getPrava().getColumn("CPRAVA"),
      (Column) dm.getPrava().getColumn("VRPRAVA").clone(),
      (Column) dm.getPrava().getColumn("PRAVO").clone(),
      (Column) dm.getPrava().getColumn("SIFRA").clone(),
      (Column) dm.getPrava().getColumn("KLJUC").clone(),
      (Column) dm.getPravauser().getColumn("POZ").clone(),
//      (Column) dm.getUseri().getColumn("LOKK").clone()
    });


//    appr.setColumns(prava.cloneColumns());
  }


  /**
   * Stati\u010Dki getter.<p>
   * @return instancu klase.
   */

  public static raUser getInstance() {
    if (usr == null) {
      usr = new raUser();
    }
    return usr;
  }

  /**
   * Provjerava je li aktivni korisnik super user.
   * @return true ako je aktivni korisnik super user (ili root), ina\u010De false.
   */
  public boolean isSuper() {
    return superuser || isRoot() || isTest() || m_user.equals("restart");
  }

  /**
   * @return true ako je aktivni korisnik "root", ina\u010De false.
   */
  public boolean isRoot() {
    return m_user.equals("root");
  }

  /**
   * @return true ako je aktivni korisnik "test", ina\u010De false.
   */
  public boolean isTest() {
    return m_user.equals("test");
  }

  /**
   * Vra\u0107a ime korisnika prijavljenog za rad s programom.<p>
   * @return ime korisnika.
   */
  public String getUser() {
    return m_user;
  }

  /**
   * Vra\u0107a puno ime prijavljenog korisnika.<p>
   * @return ime i prezime korisnika.
   */
  public String getImeUsera() {
    return m_ime_usera;
  }

  /**
   * Otklju\u010Dava aktivnog korisnika. Zvati prije izlaza iz programa.
   */
  public void unlockUser() {
    unlockUser(m_user);
  }

  /**
   * Vra\u0107a default skladište za aktivnog korisnika, ili iz tablice Usersklad,
   * ili iz paramtra defCskl.
   * @return default skladište.
   */

  public String getDefSklad() {
    if (m_user.equals("") || isRoot() ||
        !ld.raLocate(dm.getUsersklad(), "CUSER", m_user))
      return frmParam.getFrmParam().getParam("robno", "defCskl");
    return dm.getUsersklad().getString("CSKL");
  }

  /**
   * Vra\u0107a CORG od default skladišta za aktivnog korisnika, ili iz tablice Usersklad,
   * ili iz paramtra defCskl.
   * @return default skladište.
   */

  public String getDefCorg() {
    String cskl = getDefSklad();
    if (ld.raLocate(dm.getSklad(), "CSKL", cskl))
      return dm.getSklad().getString("CORG");
    else return hr.restart.zapod.OrgStr.getKNJCORG(false);
  }

  public void unlockUser(String user) {
//    System.out.println("unlocking "+ user);
    if (!user.equals("root"))
      unlockRow("Useri", new String[] {"CUSER"}, new String[] {user});
  }

  public void setUser(String user) {
    m_user = user;
    if (user.equals("")) return;
    if (user.equals("root")) {
      superuser = true;
      m_ime_usera = "Administrator";
      return;
    }
    ld.raLocate(dm.getUseri(), "CUSER", user);
//    System.out.println(dm.getUseri());
    m_grus = dm.getUseri().getString("CGRUPEUSERA");
    if (dm.getUseri().getString("OGRANICEN").equals("G"))
      ogranicen = ld.raLocate(dm.getGrupeusera(), "CGRUPEUSERA", m_grus) &&
        dm.getGrupeusera().getString("OGRANICEN").equals("D");
    else ogranicen = dm.getUseri().getString("OGRANICEN").equals("D");
    superuser = dm.getUseri().getString("SUPER").equals("D");
    m_ime_usera = dm.getUseri().getString("NAZIV");

    dm.getKljucevi().refresh();
    preparePrava();
    MsgDispatcher.install(true);
  }

  private String add(String p1, String p2) {
    return ((p1.charAt(0) == '1' || p2.charAt(0) == '1') ? "1" : "0")+
           ((p1.charAt(1) == '1' || p2.charAt(1) == '1') ? "1" : "0")+
           ((p1.charAt(2) == '1' || p2.charAt(2) == '1') ? "1" : "0")+
           ((p1.charAt(3) == '1' || p2.charAt(3) == '1') ? "1" : "0");
  }

  private String sub(String p1, String p2) {
    return ((p1.charAt(0) == '1' && p2.charAt(0) != '1') ? "1" : "0")+
           ((p1.charAt(1) == '1' && p2.charAt(1) != '1') ? "1" : "0")+
           ((p1.charAt(2) == '1' && p2.charAt(2) != '1') ? "1" : "0")+
           ((p1.charAt(3) == '1' && p2.charAt(3) != '1') ? "1" : "0");
  }

  private String[] pn = new String[] {"VRPRAVA", "SIFRA", "KLJUC"};
  private String[] pv = new String[3];

  private void grantOrRevoke(DataSet ds, boolean grant) {
    String[] pcol = new String[] {"CPRAVA"};
    String[] pval = new String[1];
    boolean insert;
    for (ds.first(); ds.inBounds(); ds.next()) {
//      System.out.println(ds);
      pval[0] = String.valueOf(ds.getShort("CPRAVA"));
      if (ds.getString("POZ").equals(grant ? "D" : "N")
          && ld.raLocate(dm.getPrava(), pcol, pval)) {
        insert = false;
//        System.out.println(dm.getPrava());
        pv[0] = dm.getPrava().getString("VRPRAVA");
        pv[1] = dm.getPrava().getString("SIFRA");
        pv[2] = dm.getPrava().getString("KLJUC");

        if (ld.raLocate(prava, pn, pv)) {
//          System.out.println(prava);
          if ((grant && prava.getString("POZ").equals("D")) || (!grant && prava.getString("POZ").equals("N")))
            prava.setString("PRAVO", add(prava.getString("PRAVO"), dm.getPrava().getString("PRAVO")));
          else {
            insert = true;
            prava.setString("PRAVO", sub(prava.getString("PRAVO"), dm.getPrava().getString("PRAVO")));
            if (prava.getString("PRAVO").equals("0000"))
              prava.deleteRow();
          }
        } else insert = true;
        if (insert) {
//          System.out.println("insert");
          prava.insertRow(true);
          prava.setString("VRPRAVA", pv[0]);
          prava.setString("PRAVO", dm.getPrava().getString("PRAVO"));
          prava.setString("SIFRA", pv[1]);
          prava.setString("KLJUC", pv[2]);
          prava.setString("POZ", ds.getString("POZ"));
        }
      }
    }
    prava.post();
  }

  private void preparePrava() {
    prava.close();
    prava.empty();
    prava.open();

//    System.out.println(m_grus);
//    System.out.println(m_user);
    QueryDataSet pg = Pravagrus.getDataModule().getTempSet(Condition.equal("CGRUPEUSERA", m_grus));
    QueryDataSet pu = Pravauser.getDataModule().getTempSet(Condition.equal("CUSER", m_user));
    pg.open();
    pu.open();
    dm.getPrava().open();

    grantOrRevoke(pg, true);
    grantOrRevoke(pg, false);
    grantOrRevoke(pu, true);
    grantOrRevoke(pu, false);
//for (prava.first(); prava.inBounds(); prava.next()) {
//  System.out.println(prava);
//}
/*    if (raLock.getRaLock().canAccessTable("doki", "p"))
         System.out.println("možeš gledat doki");
         else System.out.println("ne možeš gledat doki");
    if (canAccessTable("doki", "d"))
         System.out.println("možeš dodavat doki");
         else System.out.println("ne možeš dodavat doki");
    if (canAccessTable("doki", "i"))
         System.out.println("možeš mijenjat doki");
         else System.out.println("ne možeš mijenjat doki");
    if (canAccessTable("doki", "b"))
         System.out.println("možeš brisat doki");
         else System.out.println("ne možeš brisat doki");

    if (canAccessTable("doki", "2", "p"))
         System.out.println("možeš gledat doki key 2");
         else System.out.println("ne možeš gledat doki key 2");
    if (canAccessTable("doki", "2", "d"))
         System.out.println("možeš dodavat doki key 2");
         else System.out.println("ne možeš dodavat doki key 2");
    if (canAccessTable("doki", "2", "i"))
         System.out.println("možeš mijenjat doki key 2");
         else System.out.println("ne možeš mijenjat doki key 2");
    if (canAccessTable("doki", "2", "b"))
         System.out.println("možeš brisat doki key 2");
         else System.out.println("ne možeš brisat doki key 2");

    if (canAccessTable("Doku", "p"))
         System.out.println("možeš gledat doku");
         else System.out.println("ne možeš gledat doku");
    if (canAccessTable("Doku", "d"))
         System.out.println("možeš dodavat doku");
         else System.out.println("ne možeš dodavat doku");
    if (canAccessTable("Doku", "i"))
         System.out.println("možeš mijenjat doku");
         else System.out.println("ne možeš mijenjat doku");
    if (canAccessTable("Doku", "b"))
         System.out.println("možeš brisat doku");
         else System.out.println("ne možeš brisat doku"); */

//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(prava);
//    System.out.println(canAccessApp("robno", "P"));
  }

  public boolean findLock(String table, String[] keys, String[] values) {
    dm.getKljucevi().refresh();
    String ks = "";
    String vs = "";
    if (keys != null)
      ks = VarStr.join(keys, ';').toString();

    if (values != null)
      vs = VarStr.join(values, ';').toString();
/*      StringBuffer v = new StringBuffer(50);
      for (int i = 0; i < values.length; i++)
        v.append(values[i]).append(";");
      vs = v.toString();
    } */
//    sys.prn(dm.getKljucevi());
//    System.out.println(getUser() + ", " + table.toUpperCase()+ ",  "  +ks+ ",  " + vs);
     return ld.raLocate(dm.getKljucevi(), new String[] {"IMETAB", "KL_KEYS", "VALS"},
        new String[] {table.toUpperCase(), ks, vs});
  }
  
  private Condition findKeyCondition(DataSet ds, String[] keys) {
    String ks = "", vs = "";
    if (keys != null)  {
      ks = VarStr.join(keys, ';').toString();
      vs = VarStr.join(getValues(ds, keys), ';').toString();
    }
    return Condition.whereAllEqual(new String[] {"IMETAB", "KL_KEYS", "VALS"},
        new String[] {ds.getTableName(), ks, vs});
  }
  
  private void removeLock(String table, String[] keys, String[] values) {
    if (findLock(table, keys, values)) {
      dm.getKljucevi().deleteRow();
      dm.getKljucevi().saveChanges();
    }
  }
  
  void removeLock(DataSet ds, String[] keys) {
    QueryDataSet k = Kljucevi.getDataModule().getTempSet(findKeyCondition(ds, keys));
    k.open();
    k.deleteAllRows();
    raTransaction.saveChanges(k);
  }

  private void insertLock(String table, String[] keys, String[] values) {
    //dm.getKljucevi().refresh();
    dm.getKljucevi().insertRow(false);
    dm.getKljucevi().setInt("CKEY", Asql.getMaxKey(m_user) + 1);
    dm.getKljucevi().setString("CUSER", m_user);
    dm.getKljucevi().setTimestamp("DATUM", vl.getToday());
    dm.getKljucevi().setString("IMETAB", table.toUpperCase());
    if (keys != null)
      dm.getKljucevi().setString("KL_KEYS", VarStr.join(keys, ';').toString());
    else dm.getKljucevi().setString("KL_KEYS", "");

    if (values != null)
      dm.getKljucevi().setString("VALS", VarStr.join(values, ';').toString());
    else dm.getKljucevi().setString("VALS", "");

    dm.getKljucevi().saveChanges();
  }
  
  void insertLock(DataSet ds, String[] keys) {
    QueryDataSet k = Kljucevi.getDataModule().getTempSet("1=0");
    k.open();
    k.insertRow(false);
    k.setInt("CKEY", Asql.getMaxKey(m_user) + 1);
    k.setString("CUSER", m_user);
    k.setTimestamp("DATUM", vl.getToday());
    k.setString("IMETAB", ds.getTableName().toUpperCase());
    if (keys != null) {
      k.setString("KL_KEYS", VarStr.join(keys, ';').toString());
      k.setString("VALS", VarStr.join(getValues(ds, keys), ';').toString());
    } else {
      k.setString("KL_KEYS", "");
      k.setString("VALS", "");
    }
    raTransaction.saveChanges(k);
  }

  private String[] getValues(DataSet ds, String[] keys) {
    if (!isLockEnabled()) return new String[0];
    String[] values = new String[keys.length];
    for (int i = 0; i < keys.length; i++) {
      int type = ds.getColumn(keys[i]).getDataType();
      if (type == Variant.SHORT) values[i] = String.valueOf(ds.getShort(keys[i]));
      else if (type == Variant.INT) values[i] = String.valueOf(ds.getInt(keys[i]));
      else if (type == Variant.STRING) values[i] = ds.getString(keys[i]);
      else if (type == Variant.TIMESTAMP) values[i] = ds.getTimestamp(keys[i]).toString();
      else throw new IllegalArgumentException("Klju\u010D mora biti String, Int ili Short!");
    }
    return values;
  }
  
  private String[] kcols = {"IMETAB", "KL_KEYS", "VALS"};
  public boolean checkSameUser(DataSet ds, String[] keys) {
    DataSet klj = Kljucevi.getDataModule().getTempSet(Condition.whereAllEqual(kcols,
        new String[] {ds.getTableName().toUpperCase(), VarStr.join(keys, ';').toString(),
        VarStr.join(getValues(ds, keys), ';').toString()}));
    klj.open();
    return klj.rowCount() > 0 && klj.getString("CUSER").equalsIgnoreCase(m_user);
  }

  /**
   * Zaklju\u010Dava trenuta\u010Dno aktivni slog DataSeta. Izvodi se na SQL tablici,
   * iz DataSeta se samo uzimaju ime tablice i vrijednosti klju\u010Da.<p>
   * @param ds dataset koji se zaklju\u010Dava.
   * @param keys popis kolona dataseta koji predstavlja klju\u010D u tablici.
   * @return true ako je zaklju\u010Davanje uspjelo, false ako nije.
   */

  public boolean lockRow(final DataSet ds, final String[] keys) {
    if (!isLockEnabled()) return true;
    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        //if (isRowLocked(ds, keys)) return false;
        String q="UPDATE " + ds.getTableName() + " SET LOKK='D' WHERE "+
        Condition.whereAllEqual(keys, ds).and(Condition.equal("LOKK", "N"));
 //       System.out.println("lockRow :: "+q);
        if (raTransaction.runSQL(q) < 1) return false;
        insertLock(ds, keys);
        return true;
      }
    };
    return trans.execTransaction();
  }

  /**
   * Samo provjerava da li je tekuci zapis (aktivni slog, red) (current record) :))
   * dataseta zakljucan, a ne zakljucava ga.
   * Izvodi se na SQL tablici,
   * iz DataSeta se samo uzimaju ime tablice i vrijednosti klju\u010Da.<p>
   * @param ds dataset koji se provjerava.
   * @param keys popis kolona dataseta koji predstavlja klju\u010D u tablici.
   * @return true ako je zaklju\u010Davanje uspjelo, false ako nije.
   */
  public boolean isRowLocked(DataSet ds, String[] keys) {
     if (!isLockEnabled()) return false;
     String q;
     vl.execSQL(q="SELECT lokk FROM "+ds.getTableName()+
        " WHERE "+Condition.whereAllEqual(keys, ds)+" AND LOKK='D'");
//     System.out.println("isRowLocked :: "+q);
     vl.RezSet.open();
     return (vl.RezSet.getRowCount() > 0);
  }

  private String getWhere(String[] keys, String[] values) {
    return Condition.whereAllEqual(keys, values).toString();
/*    VarStr where = new VarStr(" WHERE");
    for (int i = 0; i < keys.length; i++)
      where.append(" ").append(keys[i]).append("=").append(values[i]).append(" AND ");
    where.chop(5);//zadnji " AND "
    return where.toString(); */
  }


  /**
   * Otklju\u010Dava trenuta\u010Dno aktivni slog DataSeta.<p>
   * @param ds dataset koji se otklju\u010Dava.
   * @param keys popis kolona dataseta koji predstavlja klju\u010D u tablici.
   */

  public void unlockRow(final DataSet ds, final String[] keys) {
    if (!isLockEnabled()) return;
    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        raTransaction.runSQL("UPDATE " + ds.getTableName() + " SET LOKK='N' WHERE "+
            Condition.whereAllEqual(keys, ds));
        removeLock(ds, keys);
        return true;
      }
    };
    trans.execTransaction();
  }

  /**
   * Otklju\u010Dava jedan slog tablice.<p>
   * @param table ime tablice.
   * @param keys popis kolona koji predstavlja klju\u010D tablice.
   * @param values popis vrijednosti klju\u010Da za slog koji se otklju\u010Dava.
   */

  public void unlockRow(String table, String[] keys, String[] values) {
    if (!isLockEnabled()) return;
    vl.runSQL("UPDATE "+table+" SET LOKK='N' WHERE "+getWhere(keys,values));
    removeLock(table, keys, values);
  }

  /**
   * Otklju\u010Dava više slogova odjednom, na temelju vrijednosti trenuta\u010Dno aktivnog
   * sloga dataseta.<p>
   * @param ds dataset koji se otklju\u010Dava.
   * @param cols popis kolona dataseta \u010Dije vrijednosti odre\u0111uju koji se slogovi
   * otklju\u010Davaju.
   */

  public void unlockRows(DataSet ds, String[] cols) {
    unlockRow(ds, cols);
  }

  /**
   * Otklju\u010Dava više slogova tablice.<p>
   * @param table ime tablice.
   * @param cols popis kolona \u010Dije vrijednosti odre\u0111uju koji se slogovi otklju\u010Davaju.
   * @param values popis vrijednosti tih kolona.
   */

  public void unlockRows(String table, String[] cols, String[] values) {
    unlockRow(table, cols, values);
  }

  /**
   * Otkljucava cijelu tablicu.<p>
   * @param table ime tablice.
   */

  public void unlockTable(String table) {
    vl.runSQL("UPDATE "+table+" SET LOKK='N'");
    removeLock(table, null, null);
  }
  
  private boolean domainDefaultOgranicen(String domain) {
    String defaultAnswer = (domain.equals("A"))?"D":"N"; //samo za aplikacije koristi flag iz baze
    String domainDesc = 
      domain.equals("A")?"aplikacije":
        (domain.equals("T")?"tablice":
          (domain.equals("F")?"funkcije":
            (domain.equals("P")?"programe":"neznam")));
    return frmParam.getParam("sisfun","userlimit"+domain,defaultAnswer,"Ograniciti prava na "+domainDesc+" kao sto je zapisano na useru/grupi")
      .equals("N");
  }
  
  private boolean notOgranicen(String domain) {
    return !ogranicen || domainDefaultOgranicen(domain);
  }

  public boolean canAccessApp(String app, String action) {
    if (!ld.raLocate(dm.getAplikacija(), "APP", app))
      return notOgranicen("A");
//      throw new IllegalArgumentException("Ne postoji aplikacija "+app+"!");
//    }
    return canAccessAnything("A", dm.getAplikacija().getString("APP"),
                             "", action.toUpperCase());
  }

  /**
   * Provjerava pravo korisnika na rad bilo kojim slogom tablice.
   * Zvati ako korisnik nema pravo na \u010Ditavu tablicu, da bi se provjerilo
   * ima li možda pravo na jedan ili više slogova tablice. Provjera prava na
   * to\u010Dno odre\u0111eni slog može se kasnije provjeriti metodom
   * <code>canAccessTable(name, key, action)</code>.<p>
   * @param table ime tablice.
   * @return true ako ima pravo na bar jedan slog, false ako nema.
   */

  public boolean canAccessAnyTableRow(String table) {
    if (isSuper()) return true;
    if (!ld.raLocate(dm.getTablice(), "IMETAB", table, Locate.CASE_INSENSITIVE))
      return notOgranicen("T");
//      throw new IllegalArgumentException("Ne postoji tablica "+table+"!");
//    }
    return ld.raLocate(prava, new String[] {"VRPRAVA", "SIFRA"},
        new String[] {"T",table}, Locate.CASE_INSENSITIVE);
  }

  /**
   * Provjerava pravo korisnika za rad na tablici.<p>
   * @param table ime tablice.
   * @param action Operacija ("pregled", "izmjena", "dodavanje", "brisanje"),
   * dovoljno je samo prvo slovo.
   * @return true ako ima pravo, false ako nema.
   */
  public boolean canAccessTable(String table, String action) {
    return canAccessTable(table, "", action);
  }

  /**
   * Provjerava pravo korisnika za rad na tablici uz zadani klju\u010D.<p>
   * @param table ime tablice.
   * @param key klju\u010D tablice.
   * @param action Operacija ("pregled", "izmjena", "dodavanje", "brisanje"),
   * dovoljno je samo prvo slovo.
   * @return true ako ima pravo, false ako nema.
   */
  public boolean canAccessTable(String table, String key, String action) {
    if (!ld.raLocate(dm.getTablice(), "IMETAB", table, Locate.CASE_INSENSITIVE)) {
      return notOgranicen("T");
//      throw new IllegalArgumentException("Ne postoji tablica "+table+"!");
    }
    return canAccessAnything("T", dm.getTablice().getString("IMETAB"),
                             key, action.toUpperCase());
  }

  /**
   * Provjerava pravo korisnika za rad sa programom.<p>
   * @param prog ime programa.
   * @param action Operacija ("pregled", "izmjena", "dodavanje", "brisanje"),
   * dovoljno je samo prvo slovo.
   * @return true ako ima pravo, false ako nema.
   */
  public boolean canAccessProgram(String prog, String action) {
    if (!ld.raLocate(dm.getProgrami(), "CPROG", prog)) //{
      return notOgranicen("P");
//      throw new IllegalArgumentException("Ne postoji program "+prog+"!");
//    }
    return canAccessAnything("P", dm.getProgrami().getString("CPROG"), "", action.toUpperCase());
  }

  /**
   * Provjerava pravo korisnika za rad sa funkcijom.<p>
   * @param func ime funkcije.
   * @param action Operacija ("pregled", "izmjena", "dodavanje", "brisanje"),
   * dovoljno je samo prvo slovo.
   * @return true ako ima pravo, false ako nema.
   */
  public boolean canAccessFunction(String func, String action) {
    if (!ld.raLocate(dm.getFunkcije(), "CFUNC", func)) //{
      return notOgranicen("F");
//      throw new IllegalArgumentException("Ne postoji funkcija "+func+"!");
//    }
    return canAccessAnything("F", dm.getFunkcije().getString("CFUNC"), "", action.toUpperCase());
  }

  private boolean inPravo(String pravo, String action) {
    if ((action.startsWith("P") && pravo.charAt(0) == '1') ||
        (action.startsWith("D") && pravo.charAt(1) == '1') ||
        (action.startsWith("I") && pravo.charAt(2) == '1') ||
        (action.startsWith("B") && pravo.charAt(3) == '1')) return true;
    else return false;
  }

  private boolean canAccessAnything(String domain, String cdom, String key, String action) {
    pv[0] = domain;
    pv[1] = cdom;
    pv[2] = key;
    if (key != null && key.length() > 0) {
//String pvstring = "{"+pv[0]+","+pv[1]+","+pv[2]+"}";
//System.out.println("ld.raLocate(prava, \"VRPRAVA\", \"SIFRA\", \"KLJUC\", "+pvstring+") && inPravo(prava.getString(\"PRAVO\"), action) = "+
//(ld.raLocate(prava, pn, pv) && inPravo(prava.getString("PRAVO"), action)));
      if (ld.raLocate(prava, pn, pv) && inPravo(prava.getString("PRAVO"), action))
        return (prava.getString("POZ").equals("D"));
      /*return !ogranicen;*/
    }
//String pvstring2 = "{"+pv[0]+","+pv[1]+","+pv[2]+"}";
//System.out.println("ld.raLocate(prava, \"VRPRAVA\", \"SIFRA\", \"KLJUC\", "+pvstring2+") && inPravo(prava.getString(\"PRAVO\"), action) = "+
//(ld.raLocate(prava, pn, pv) && inPravo(prava.getString("PRAVO"), action)));

 /*
 * //TODO: ai: ja mislim da bi tu trebalo biti
 * pv[2]="#$%$#!!" 
 * da ne nadje nista u locateu jer ako je u pitanju zabrana prava na jedan slog (key != null || "")
 * tu se zabranjuje cijela tablica. Trebalo bi to istestirati, a ja sad nemam ni volje ni vremena
 */
    if (ld.raLocate(prava, pn, pv) && inPravo(prava.getString("PRAVO"), action))
      return (prava.getString("POZ").equals("D"));
//System.out.println("return notOgranicen("+domain+") = "+notOgranicen(domain));
    return notOgranicen(domain);
  }


  /**
   * Prikazuje poruku da je zapis zakljucan, uz gumbic za detaljnije informacije
   * @param parent u odnosu na koga se prikazuje poruka (JOptionPane.showMessageDialog(_parent_))
   * @param ds Dataset koji je kontroliran postavljen na tekuci record
   * @param keys kljucevi dataseta za kontrolu
   */
  public void showSoftLockWarning(java.awt.Component parent, DataSet ds, String[] keys) {
    int odg = javax.swing.JOptionPane.showOptionDialog(parent,
        "Zapis se azurira na drugom radnom mjestu. Izmjenu probajte poslije, a sada je moguc samo pregled ",
        "Pozor!", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE, null,
        new String[] {"OK", "Detalji"},"OK");
    if (odg == 1) {
      //nadji slog u kljucevima
      String iimetab = ds.getTableName();
      String ikeys = VarStr.join(keys,';').toString();
      String imessage = "Nema detaljnijih informacija";
      String ivals = VarStr.join(getValues(ds, keys),';').toString();
      com.borland.dx.sql.dataset.QueryDataSet kljuci = hr.restart.baza.Kljucevi.getDataModule().getTempSet();
      kljuci.open();

      if (lookupData.getlookupData().raLocate(kljuci,new String[] {"IMETAB","KL_KEYS","VALS"}, new String[] {iimetab,ikeys,ivals})) {
        //nadji ime usera
        String imeusera = "";
        hr.restart.baza.dM.getDataModule().getUseri().open();
        if (lookupData.getlookupData()
              .raLocate(hr.restart.baza.dM.getDataModule().getUseri(),"CUSER", kljuci.getString("CUSER"))) {
            imeusera = hr.restart.baza.dM.getDataModule().getUseri().getString("NAZIV");
        }
      imessage = "Zapis je u tablici " + iimetab +" zakljucao korisnik: "+
        kljuci.getString("CUSER")+" "+imeusera+", dana "+kljuci.getTimestamp("DATUM")+".";
      } else {
        System.out.print("failed !");
        System.out.print(" iimetab = "+iimetab);
        System.out.print(" ikeys = "+ikeys);
        System.out.println(" ivals = "+ivals);
      }
      javax.swing.JOptionPane.showMessageDialog(parent, imessage, "Info o zakljucavanju",
                                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
  }

  public boolean isLockEnabled() {
    /**@todo: da to ipak bude parametar (globalni lokalni ili sta vec */
    return !softLockEnabledInProperties.equals("N");
  }

  /**@todo: metoda koja sve otkljucava sve recorde:
   * 1. Provjerava da li su svi korisnici odjavljeni (osim tekuceg)
   * 1. Brise sve u tab. kljucevi
   * 2. Prolazi sve tablice koje imaju LOKK i mijenja sa 'D' u 'N'
   * Zasto? Zato sto se svasta moze desiti
   */

  public void maintenanceUnlockEverything() {
    dM.getDataModule().loadModules();
    KreirDrop[] lmod = KreirDrop.getModulesWithColumn("LOKK");
    for (int i = 0; i < lmod.length; i++) {
      if (!lmod[i].Naziv.equalsIgnoreCase("Useri"))
        vl.runSQL("UPDATE "+lmod[i].Naziv+" SET lokk='N'");
    }
    vl.runSQL("UPDATE useri SET lokk='N'");
    vl.runSQL("DELETE FROM kljucevi");
  }
}
