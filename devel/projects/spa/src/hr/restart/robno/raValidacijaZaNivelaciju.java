/****license*****************************************************************
**   file: raValidacijaZaNivelaciju.java
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
package hr.restart.robno;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.sql.dataset.QueryDataSet;


public class raValidacijaZaNivelaciju {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private QueryDataSet allDocsForCheck;
  private QueryDataSet oldSlogZaduzenja;
//  private StorageDataSet hahaSQL ;
  private TypeDoc TD = TypeDoc.getTypeDoc();
  private raValidacijaDocs rcc = null;
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
/*
  private void addhahaSET(String[] keys){
    if (hahaSQL==null) {
      hahaSQL = new StorageDataSet();
      Column c_opis = dm.getSklad().getColumn("CSKL").cloneColumn();

    }
    hahaSQL.insertRow(true);
  }
*/
  public raValidacijaZaNivelaciju() {

    if (allDocsForCheck != null) {
      allDocsForCheck = null;
      oldSlogZaduzenja = null;
    }
    allDocsForCheck = new QueryDataSet();
    oldSlogZaduzenja = new QueryDataSet();
    Column c_cskl = dm.getSklad().getColumn("CSKL").cloneColumn();
    Column c_god = dm.getStdoki().getColumn("GOD").cloneColumn();
    Column c_vrdok = dm.getStdoki().getColumn("VRDOK").cloneColumn();
    Column c_brdok = dm.getStdoki().getColumn("BRDOK").cloneColumn();
    Column c_rbr = dm.getStdoki().getColumn("RBR").cloneColumn();
    Column c_datdok= dm.getDoki().getColumn("DATDOK").cloneColumn();
    Column c_cart = dm.getArtikli().getColumn("CART").cloneColumn();
    Column c_svc = dm.getStdoku().getColumn("SVC").cloneColumn();
    Column c_svcgood = dm.getStdoku().getColumn("SVC").cloneColumn();
           c_svcgood.setColumnName("SVC_good");
    Column c_vc = dm.getStdoku().getColumn("VC").cloneColumn();
    Column c_vcgood = dm.getStdoku().getColumn("VC").cloneColumn();
           c_vcgood.setColumnName("VC_good");
    Column c_smc = dm.getStdoku().getColumn("SMC").cloneColumn();
    Column c_smcgood = dm.getStdoku().getColumn("SMC").cloneColumn();
           c_smcgood.setColumnName("SMC_good");
    Column c_mc = dm.getStdoku().getColumn("MC").cloneColumn();
    Column c_mcgood = dm.getStdoku().getColumn("MC").cloneColumn();
           c_mcgood.setColumnName("MC_good");
    Column c_key = dm.getStdoki().getColumn("SITKAL").cloneColumn();
    Column c_istina = dm.getStdoki().getColumn("LOKK").cloneColumn();

    allDocsForCheck.setColumns(new Column[]{c_cskl,c_cart,c_vrdok,c_god,c_brdok,c_datdok,c_rbr,c_svc,
                               c_svcgood,c_vc,c_vcgood,c_smc,c_smcgood,c_mc,c_mcgood,c_key,c_istina});
    allDocsForCheck.setMetaDataUpdate(MetaDataUpdate.NONE);
    oldSlogZaduzenja.setColumns(allDocsForCheck.cloneColumns());
  }

  public String queryString(String cskl,String god ,String cart) {
    String dokuq = " and ";
    String dokiq = " and ";
    String mesuq = " and ";
    String mesiq = " and ";
    if (cskl != null && !cskl.equalsIgnoreCase("")) {
      dokuq = dokuq + " doku.cskl ='"+cskl+"' ";
      dokiq = dokiq + " doki.cskl ='"+cskl+"' ";
      mesuq = mesuq + " meskla.csklul ='"+cskl+"' ";
      mesiq = mesiq + " meskla.cskliz ='"+cskl+"' ";
    }
    if (god != null && !god.equalsIgnoreCase("")) {
      String veznik = dokuq.equalsIgnoreCase(" and ")?" ":" and ";
      dokuq = dokuq+veznik+" doku.god ='"+god+"' ";
      dokiq = dokiq+veznik+" doki.god ='"+god+"' ";
      mesuq = mesuq+veznik+" meskla.god ='"+god+"' ";
      mesiq = mesiq+veznik+" meskla.god ='"+god+"' ";
    }

    if (cart != null && !cart.equalsIgnoreCase("")) {
      String veznik = dokuq.equalsIgnoreCase(" and ")?" ":" and ";
      dokuq = dokuq+veznik+" cart ="+cart+" ";
      dokiq = dokiq+veznik+" cart ="+cart+" ";
      mesuq = mesuq+veznik+" cart ="+cart+" ";
      mesiq = mesiq+veznik+" cart ="+cart+" ";
    }
String queryy =
    "SELECT doku.cskl as cskl ,doku.god as god ,doku.vrdok as vrdok ,doku.brdok as brdok ,stdoku.rbr as rbr,"+
            "datdok as datdok,stdoku.cart as cart,"+
            "svc,0. as SVC_good,vc,0. as VC_good,"+
            "smc,0. as SMC_good,mc,0. as MC_good, "+
            "(cskl||'-'||vrdok||'-'||god||'-'||brdok||'-'||rbr) as key, 'N' as istina "+
            "FROM doku ,stdoku WHERE doku.cskl = stdoku.cskl "+
            "AND doku.vrdok = stdoku.vrdok AND doku.god = stdoku.god "+
            "AND doku.brdok = stdoku.brdok "+dokuq+
     "union "+
     "SELECT doki.cskl as cskl,doki.god as god,doki.vrdok as vrdok,doki.brdok as brdok,stdoki.rbr as rbr," +
            "datdok,stdoki.cart as cart,"+
            "0. as svc,0. as SVC_good,vc,0. as VC_good,"+
            "0. as smc,0. as SMC_good,mc,0. as MC_good, "+
            "(cskl||'-'||vrdok||'-'||god||'-'||brdok||'-'||rbr) as key, 'N' as istina "+
            "FROM doki ,stdoki WHERE doki.cskl = stdoki.cskl "+
            "AND doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god "+
            "AND doki.brdok = stdoki.brdok "+dokiq+
     "union "+
     "SELECT meskla.csklul as cskl,meskla.god as god,meskla.vrdok as vrdok,meskla.brdok as brdok,stmeskla.rbr as rbr,"+
            "datdok as datdok, stmeskla.cart as cart, "+
            "svc,0. as SVC_good,vc,0. as VC_good,"+
            "smc,0. as SMC_good,mc,0. as MC_good, "+
            "(cskliz||'-'||csklul||'-'vrdok||'-'||god||'-'||brdok||'-'||rbr) as key, "+
            "'N' as istina "+
            "FROM meskla ,stmeskla "+
            "WHERE meskla.csklul = stmeskla.csklul and "+
            "meskla.cskliz = stmeskla.cskliz AND meskla.vrdok = stmeskla.vrdok "+
            "AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok "+mesuq+
     "union "+
     "SELECT meskla.cskliz as cskl,meskla.god as god,meskla.vrdok as vrdok,meskla.brdok as brdok,stmeskla.rbr as rbr,"+
             "datdok as datdok,stmeskla.cart as cart ,"+
             "svc,0. as SVC_good,vc,0. as VC_good,"+
             "smc,0. as SMC_good,mc,0. as MC_good, "+
             "(cskliz||'-'||csklul||'-'vrdok||'-'||god||'-'||brdok||'-'||rbr) as key, "+
             "'N' as istina "+
             "FROM meskla ,stmeskla "+
             "WHERE meskla.csklul = stmeskla.csklul and meskla.cskliz = stmeskla.cskliz "+
             "AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god "+
             "AND meskla.brdok = stmeskla.brdok "+mesiq+
     "order by 7,6,5";
System.out.println(queryy);
     return queryy;
  }

  public void checkPrice(String cskl,String god ,String cart,boolean repopuna) {

    QueryDataSet allDocsForCheck1 = hr.restart.util.Util.getNewQueryDataSet(queryString(cskl,god ,cart));
    allDocsForCheck.open();
    allDocsForCheck.emptyAllRows();
    for (allDocsForCheck1.first();allDocsForCheck1.inBounds();allDocsForCheck1.next()) {
      allDocsForCheck.insertRow(true);
      dm.copyColumns(allDocsForCheck1,allDocsForCheck);
    }
//    ST.prn(allDocsForCheck);

    if (allDocsForCheck.isEmpty()) return;
    oldSlogZaduzenja.open();
    oldSlogZaduzenja.insertRow(true);
    allDocsForCheck.first();
    boolean isFirst= true;
// todo kad se mijenja artikl treba promjeniti i zaduzenje
    for (allDocsForCheck.first();allDocsForCheck.inBounds();allDocsForCheck.next()) {

      if (isFirst) {
        isFirst=false;
      }
      else {
        if (allDocsForCheck.getInt("CART")==oldSlogZaduzenja.getInt("CART")) {
          if (TD.isDocUlaz(allDocsForCheck.getString("VRDOK"))) {
            if (!allDocsForCheck.getString("VRDOK").equalsIgnoreCase("PST")) {
              if (allDocsForCheck.getBigDecimal("SVC").compareTo(oldSlogZaduzenja.getBigDecimal("VC"))!=0) {
                allDocsForCheck.setBigDecimal("SVC_good",oldSlogZaduzenja.getBigDecimal("VC"));
                allDocsForCheck.setString("ISTINA","D");
                if (CommonAdd()) {
                  rcc.errors.setString("OPIS","CART="+allDocsForCheck.getInt("CART")+" SVC je "+allDocsForCheck.getBigDecimal("SVC")+" a treba biti ->" );
                  rcc.errors.setBigDecimal("GRESKA",allDocsForCheck.getBigDecimal("SVC_good"));
                };
              }
              if (allDocsForCheck.getBigDecimal("SMC").compareTo(oldSlogZaduzenja.getBigDecimal("MC"))!=0) {
                allDocsForCheck.setBigDecimal("SMC_good",oldSlogZaduzenja.getBigDecimal("MC"));
                allDocsForCheck.setString("ISTINA","D");
                if (CommonAdd()) {
                  rcc.errors.setString("OPIS","CART="+allDocsForCheck.getInt("CART")+" SMC je "+allDocsForCheck.getBigDecimal("SMC")+" a treba biti ->" );
                  rcc.errors.setBigDecimal("GRESKA",allDocsForCheck.getBigDecimal("SMC_good"));
                };
              }
            }
            dm.copyColumns(allDocsForCheck,oldSlogZaduzenja);
          }
          else {
            if (TD.isDocDiraZalihu(allDocsForCheck.getString("VRDOK"))) {
              if (allDocsForCheck.getBigDecimal("VC").compareTo(oldSlogZaduzenja.getBigDecimal("VC"))!=0) {
                allDocsForCheck.setBigDecimal("VC_good",oldSlogZaduzenja.getBigDecimal("VC"));
                allDocsForCheck.setString("ISTINA","D");
                if (CommonAdd()) {
                  rcc.errors.setString("OPIS","CART="+allDocsForCheck.getInt("CART")+" VC je "+allDocsForCheck.getBigDecimal("VC")+" a treba biti ->" );
                  rcc.errors.setBigDecimal("GRESKA",allDocsForCheck.getBigDecimal("VC_good"));
                };

              }
              if (allDocsForCheck.getBigDecimal("MC").compareTo(oldSlogZaduzenja.getBigDecimal("MC"))!=0) {
                allDocsForCheck.setBigDecimal("MC_good",oldSlogZaduzenja.getBigDecimal("MC"));
                allDocsForCheck.setString("ISTINA","D");
                if (CommonAdd()) {
                  rcc.errors.setString("OPIS","CART="+allDocsForCheck.getInt("CART")+ " MC je "+allDocsForCheck.getBigDecimal("MC")+" a treba biti ->" );
                  rcc.errors.setBigDecimal("GRESKA",allDocsForCheck.getBigDecimal("MC_good"));
                };
              }
            }
          }
        }
        else {
          if (TD.isDocUlaz(allDocsForCheck.getString("VRDOK"))) {
            dm.copyColumns(allDocsForCheck,oldSlogZaduzenja);
          }
        }
      }
    }
// ako je odabran popravak
    if (repopuna) {
      hr.restart.util.VarStr queryDod = new hr.restart.util.VarStr(
          "(cskl'||'-'vrdok||'-'||god||'-'||brdok||'-'||rbr) in (");
      hr.restart.util.VarStr queryDodMes = new hr.restart.util.VarStr(
          "(cskliz||'-'||csklul||'-'vrdok||'-'||god||'-'||brdok||'-'||rbr) in (");
      for (allDocsForCheck.first();allDocsForCheck.inBounds();allDocsForCheck.next()) {
        if(allDocsForCheck.getString("ISTINA").equalsIgnoreCase("D")) {
          queryDod = queryDod.append("'").append(allDocsForCheck.getString("KEY")).append("',");
          queryDodMes = queryDod.append("'").append(allDocsForCheck.getString("KEY")).append("',");
        }
      }
      queryDod.chop();
      queryDodMes.chop();
      queryDod.append(")");
      queryDodMes.append(")");

//      QueryDataSet qds =

    }
  }

  public QueryDataSet getAllDocsForCheck() {
    return allDocsForCheck;
  }

  public boolean CommonAdd() {
    if (rcc== null) return false;
    rcc.errors.insertRow(true);
    rcc.errors.setString("CSKL",allDocsForCheck.getString("CSKL"));
    rcc.errors.setString("CSKLIZ",allDocsForCheck.getString("CSKL"));
    rcc.errors.setString("VRDOK",allDocsForCheck.getString("VRDOK"));
    rcc.errors.setString("GOD",allDocsForCheck.getString("GOD"));
    rcc.errors.setInt("BRDOK",allDocsForCheck.getInt("BRDOK"));
    rcc.errors.setShort("RBR",allDocsForCheck.getShort("RBR"));
    return true;
  }

  public void setraValidacijaDocs(raValidacijaDocs rcc) {
    this.rcc = rcc;
  }

}