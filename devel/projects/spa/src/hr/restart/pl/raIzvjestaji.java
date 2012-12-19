/****license*****************************************************************
**   file: raIzvjestaji.java
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
package hr.restart.pl;

import hr.restart.db.raPreparedStatement;
import hr.restart.util.Util;
import hr.restart.util.Valid;

import java.sql.Timestamp;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raIzvjestaji {

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 1.1. na obrascu ID (mio I stup)
   */
  public static short[] ID_1_1 = new short[] {10001,1};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 1.2. na obrascu ID (mio II stup)
   */

  public static short[] ID_1_2 = new short[] {10001,2};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 1.3. na obrascu ID (MIO prema plaæi do 31.12.2002.)
   */
  public static short[] ID_1_2002 = new short[] {10001,2002};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 1.3. na obrascu ID (zdravstveno osiguranje)
   */
  public static short[] ID_1_3 = new short[] {10001,3};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 1.4. na obrascu ID (zaposljavanje)
   */
  public static short[] ID_1_4 = new short[] {10001,4};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 3.1. na obrascu ID (premije po osnovi zivotnog osiguranja)
   */
  public static short[] ID_3_1 = new short[] {10001,6};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 3.2. na obrascu ID (premije po osnovi dopunskog zdravstvenog osiguranja)
   */
  public static short[] ID_3_2 = new short[] {10001,7};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 3.3. na obrascu ID (premije po osnovi dobrovoljnog mirovinskog osiguranja)
   */
  public static short[] ID_3_3 = new short[] {10001,8};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 3.3. na obrascu ID od 01/2003 (Dodatni - MIO I.stup za staz sa povecanim trajanjem)
   */
  public static short[] ID03_3_3 = new short[] {10001,9};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 3.4. na obrascu ID od 01/2003 (Dodatni - MIO II.stup za staz sa povecanim trajanjem)
   */
  public static short[] ID03_3_4 = new short[] {10001,10};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku 3.6. na obrascu ID od 01/2003 (poseban - ozljede na radu)
   */
  public static short[] ID03_3_6 = new short[] {10001,11};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku III 4.4.2. na obrascu ID od 01/2005 (poseban - za zapošljavanje osoba s invaliditetom)
   */
  public static short[] ID05_4_4_2 = new short[] {10001,12};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * opis primitaka od nesam. rada (donji dio obrasca) sa oznakom 020
   */
  public static short[] ID_020 = new short[] {10001,20};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * opis primitaka od nesam. rada (donji dio obrasca) sa oznakom 030
   */
  public static short[] ID_030 = new short[] {10001,30};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * opis primitaka od nesam. rada (donji dio obrasca) sa oznakom 040
   */
  public static short[] ID_040 = new short[] {10001,40};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * opis primitaka od nesam. rada (donji dio obrasca) sa oznakom 050
   */
  public static short[] ID_050 = new short[] {10001,50};

///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////
  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku IV.8. na obrascu RS strana B (Mirovinsko osiguranje 1. stup)
  */
  public static short[] RS_IV_8 = new short[] {10003,8};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku IV.8. na obrascu RS strana B (Mirovinsko osiguranje 2. stup)
   */
  public static short[] RS_IV_9 = new short[] {10003,9};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku III.5a (osnovica) i III.5b (iznos) na obrascu RS strana A (Osnovno zdravstveno osiguranje)
   */
  public static short[] RS_III_5 = new short[] {10003,10};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku III.6a (osnovica) i III.6b (iznos) na obrascu RS strana A (Doprinos za zapošljavanje)
   */
  public static short[] RS_III_6 = new short[] {10003,11};

  /**
   * Parametar za metodu getOdbiciWhQueryIzv(short[]) koja bi vratila CVRODB in (x,y,z...) a odnosi se na
   * tocku IV.12. na obrascu RS strana B (Premije osiguranja)
   */
  public static short[] RS_IV_12 = new short[] {10003,12};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * zarade ciji neto se odbija od iznosa ZA ISPLATU na RS obrascu
   */
  public static short[] RS_NE_99 = new short[] {10003,99};
///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////
  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * zarade koje ulaze u sate provedene na radu duzem od punog radnog vremena (kolona 6)
   */
  public static short[] ER1_6 = new short[] {10002,6};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * zarade koje ulaze u sate odsutnosti s rada s pravom na naknadu place po ZOR-u (kolona 7)
   */
  public static short[] ER1_7 = new short[] {10002,7};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * zarade koje ulaze u sate bolovanja na teret HZZO (kolona 9)
   */
  public static short[] ER1_9 = new short[] {10002,9};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * zarade koje ulaze u iznos naknate na teret HZZO (kolona 8.1 na M4 obrascu)
   */
  public static short[] M4_1 = new short[] {10004,1};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * zarade koje ulaze u iznos naknate na teret RH (kolona 8.2 na M4 obrascu)
   */
  public static short[] M4_2 = new short[] {10004,2};

  /**
   * Parametar za metodu getPrimanjaWhQueryIzv(short[]) koja bi vratila CVRPRIM in (x,y,z...) a odnosi se na
   * zarade koje ulaze u iznos naknate na teret Centra za socijalnu skrb (kolona 8.3 na M4 obrascu)
   */
  public static short[] M4_3 = new short[] {10004,3};

  protected raIzvjestaji() {
  }
  public static String getOdbiciWhQueryIzv(short cizv, short cgrizv) {
    return getWhQueryIzv(cizv,cgrizv,"grizvodb","CVRODB");
  }

  /**
   * Pozivati sa jednim od short[] statickih parametara (vidi opise parametara)
   * @param param
   * @return dio query stringa 'CVRODB in (cvrodb1,cvrodb2,cvrodb3,...cvrodbn)' koji zadovoljavaju parametar
   */
  public static String getOdbiciWhQueryIzv(short[] param) {
    return getOdbiciWhQueryIzv(param[0],param[1]);
  }

  public static String getPrimanjaWhQueryIzv(short cizv, short cgrizv) {
    return getWhQueryIzv(cizv,cgrizv,"grizvprim","CVRP");
  }

  /**
   * Pozivati sa jednim od short[] statickih parametara (vidi opise parametara)
   * @param param
   * @return dio query stringa 'CVRP in (cvrp1,cvrp2,cvrp3,...cvrpn)' koji zadovoljavaju parametar
   */

  public static String getPrimanjaWhQueryIzv(short[] param) {
    return getPrimanjaWhQueryIzv(param[0],param[1]);
  }

  private static String getWhQueryGrupe(short cizv, short cgrizv, String tabName, String colName) {
    return "SELECT " + colName + " FROM " + tabName + " where CIZV = "+cizv+" AND cgrizv = "+cgrizv;
  }

  public static String getWhQueryIzv(short cizv, short cgrizv, String tabName, String colName) {
    String ret = colName + " in (";
    String qry = getWhQueryGrupe(cizv,cgrizv,tabName,colName);
    QueryDataSet qds = Util.getNewQueryDataSet(qry);
    if (qds.getRowCount() == 0) return "";//colName + " in (99999)";
    qds.first();
    do {
      ret = ret+qds.getShort(colName)+",";
    } while (qds.next());
    ret = ret.substring(0,ret.length()-1)+")";
    return ret;
  }

  public static raFond getFond(short godina, short mjesec) {
    return new raFond(godina,mjesec);
  }
  public static raFond getFond(short godina, short mjesec, boolean ispl) {
    return new raFond(godina,mjesec, ispl);
  }
  public static String convertCopcineToRS(String copcine) {
    String copcine2 = copcine;
    try {
      String mcopc = Valid.getValid().maskZeroInteger(new Integer(copcine2),3);
      char[] aopc = mcopc.toCharArray();
      int l1 = 0;
      int x = 4;
      for (int i = 0; i < aopc.length; i++) {
        l1 = l1 + Integer.parseInt(new String(new char[] {aopc[i]}))*x;
        x = x - 1;
      }
      int l2 = l1%11;
      int l3 = 11 - l2;
      l3 = l3>=10?l3-10:l3;
      copcine2 = mcopc+l3;
    }
    catch (Exception ex) {
    }
    return copcine2;
  }

  //cache getCustomData_radnici
  private static QueryDataSet grizvznac;
  public static String getCustomData_radnici(String cradnik, short cizv, short cgrizv) {
    String ret = "";
    if (grizvznac != null && (grizvznac.getShort("CIZV")!=cizv || grizvznac.getShort("CGRIZV")!=cgrizv)) grizvznac = null;
    if (grizvznac == null) 
      grizvznac = Util.getNewQueryDataSet("SELECT * FROM grizvznac where cizv="+cizv+" and cgrizv="+cgrizv);
    if (grizvznac.getRowCount() == 0) {
      return "";
    } else if (grizvznac.getRowCount() == 1) {
      QueryDataSet znacData = Util.getNewQueryDataSet("SELECT vri from PlZnacRadData where cradnik = '"+cradnik+"' and cznac = "+grizvznac.getShort("CZNAC"));
      ret = znacData.getString("VRI");
    } else {
      String cznac_in = "(";
      for (grizvznac.first(); grizvznac.inBounds(); grizvznac.next()) {
        cznac_in = cznac_in + grizvznac.getShort("CZNAC") + ",";
      }
      cznac_in = cznac_in.substring(0,cznac_in.length()-1)+")";
      QueryDataSet znacData = Util.getNewQueryDataSet("SELECT vri from PlZnacRadData where cradnik = '"+cradnik+"' and cznac in "+cznac_in);
      for (znacData.first(); znacData.inBounds(); znacData.next()) {
        ret = ret + znacData.getString("VRI")+ ", ";
      }
    }
    try {
      Timestamp ts = java.sql.Timestamp.valueOf(ret);
      System.out.println("ts = "+ts);
      ret = formatDate(ts);
    } catch (Exception e) {
      
    }
    return ret;
  }
  public static String formatDate(java.sql.Timestamp ts) {
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
    String date = sdf.format(ts);
    if (date.equals("01.01.1970")) return "";
    return date;
  }
  public static void addDefault() {
    raPreparedStatement plizv = new raPreparedStatement("plizv",raPreparedStatement.INSERT);
    raPreparedStatement grupeizv = new raPreparedStatement("grupeizv",raPreparedStatement.INSERT);

    addPlizv(10001,"ID obrazac","",plizv);
    addGrupeizv(10001,1,"Mirovinsko osiguranje I stup",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,2,"Mirovinsko osiguranje II stup",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,2002,"MIO prema plaæi do 31.12.2002.",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,3,"Zdravstveno osiguranje",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,4,"Zapošljavanje",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,6,"Životno osiguranje",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,7,"Dopunsko zdravstveno osig.",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,8,"Dobrovoljno mirovinsko osig.",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,20,"Mirovina",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,30,"Pla\u0107a u naravi",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,40,"Nakn. iznad propisanih iznosa",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,50,"Ostali primici",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,9,"MIO I. za staž sa pov.traj.",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,10,"MIO II. za staž sa pov.traj.",true,false,false,false,"",grupeizv);
    addGrupeizv(10001,11,"Doprinos za ZO-ozlj. na radu",true,false,false,false,"",grupeizv);

    addPlizv(10002,"Potvrda o pla\u0107i (ER-1)","",plizv);
    addGrupeizv(10002,6,"Sati-duže od punog rad. vr.",false,true,false,false,"",grupeizv);
    addGrupeizv(10002,7,"Odsut.s pravom na nakn.po ZOR",false,true,false,false,"",grupeizv);
    addGrupeizv(10002,9,"Sati bolovanja na teret HZZO",false,true,false,false,"",grupeizv);

    addPlizv(10003,"Obrazac RS","",plizv);
    addGrupeizv(10003,8,"Mirovinsko 1.stup (B-IV-8)",true,false,false,false,"",grupeizv);
    addGrupeizv(10003,9,"Mirovinsko 2.stup (B-IV-9)",true,false,false,false,"",grupeizv);
    addGrupeizv(10003,10,"Zdravstveno (A-III-5)",true,false,false,false,"",grupeizv);
    addGrupeizv(10003,11,"Zapošljavanje (A-III-6)",true,false,false,false,"",grupeizv);
    addGrupeizv(10003,12,"Premije osiguranja (B-IV-12)",true,false,false,false,"",grupeizv);
    addGrupeizv(10003,99,"Primanja NE idu u ZA ISPLATU",false,false,false,true,"",grupeizv);

    addPlizv(10004,"M4 obrazac","",plizv);
    addGrupeizv(10004,1,"Nakn.pl.na teret HZZO",true,false,false,false,"",grupeizv);
    addGrupeizv(10004,2,"Nakn.pl.na teret Rep. Hrvatske",true,false,false,false,"",grupeizv);
    addGrupeizv(10004,3,"Nakn.pl.na ter. centra za s.s.",true,false,false,false,"",grupeizv);

//id i rs imaju neke iste za sada

    replicateOdbici(ID_1_1, RS_IV_8, false);
    replicateOdbici(ID_1_2, RS_IV_9, false);
    replicateOdbici(ID_1_3, RS_III_5, false);
    replicateOdbici(ID_1_4, RS_III_6, false);
    if (replicateOdbici(ID_3_1, RS_IV_12, false)) {
      replicateOdbici(ID_3_2, RS_IV_12, true);
      replicateOdbici(ID_3_3, RS_IV_12, true);
    }
  }

  private static void addPlizv(int cizv, String opis, String parametri, raPreparedStatement p) {
    try {
      p.setString("LOKK","N",false);
      p.setString("AKTIV","D",false);
      p.setShort("CIZV",(short)cizv,false);
      p.setString("OPIS",opis,false);
      p.setString("PARAMETRI",parametri,false);
      p.execute();
    }
    catch (Exception ex) {
      System.out.println("plizv CIZV = "+cizv+"; OPIS = "+opis+" nije dodan! ex = "+ex);
      //ex.printStackTrace();
    }
  }

  private static void addGrupeizv(int cizv, int cgrizv, String naziv,
                                  boolean sumbruto, boolean sumsati, boolean sumneto, boolean sumneto2,
                                  String parametri, raPreparedStatement p) {
    try {
      p.setShort("CIZV",(short)cizv,false);
      p.setShort("CGRIZV",(short)cgrizv,false);
      p.setString("NAZIV",naziv,false);
      p.setString("SUMBRUTO",sumbruto?"D":"N",false);
      p.setString("SUMSATI",sumsati?"D":"N",false);
      p.setString("SUMNETO",sumneto?"D":"N",false);
      p.setString("SUMNETO2",sumneto2?"D":"N",false);
      p.execute();
    }
    catch (Exception ex) {
      System.out.println("grupeizv CIZV = "+cizv+"; CGRIZV= "+cgrizv+"; NAZIV = "+naziv+" nije dodan! ex = "+ex);
    }
  }

  private static boolean replicateOdbici(short[] _orig, short[] _new, boolean force) {
    QueryDataSet orgSet = Util.getNewQueryDataSet(getWhQueryGrupe(_orig[0],_orig[1],"grizvodb","CVRODB"));
    if (orgSet.getRowCount() == 0) return false; // nema nis za koprianje
    QueryDataSet newSet = Util.getNewQueryDataSet(getWhQueryGrupe(_new[0],_new[1],"grizvodb","CVRODB"));
    if (!force && newSet.getRowCount() != 0) return false; // vec je definirano nesto za grupu
    orgSet.first();
    do {
      newSet.insertRow(false);
      orgSet.copyTo(newSet);
      newSet.setShort("CIZV",_new[0]);
      newSet.setShort("CGRIZV",_new[1]);
      newSet.post();
    } while (orgSet.next());
    newSet.saveChanges();
    return true;
  }
}