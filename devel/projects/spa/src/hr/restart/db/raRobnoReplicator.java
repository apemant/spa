/****license*****************************************************************
**   file: raRobnoReplicator.java
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
package hr.restart.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;


public class raRobnoReplicator implements raReplicator{


  private hr.restart.robno.raKalkulBDStanje rKBDStanje = new hr.restart.robno.raKalkulBDStanje();
  private hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
  private hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  private hr.restart.robno.TypeDoc TD = hr.restart.robno.TypeDoc.getTypeDoc();
  private hr.restart.robno.raFakeBDStanje raFakeBDStanje = new hr.restart.robno.raFakeBDStanje();
  private hr.restart.robno.raFakeBDStanje raFakeBDStanjeOLD = new hr.restart.robno.raFakeBDStanje();
  private hr.restart.robno.raFakeBDStdoku raFakeBDStdoku = new hr.restart.robno.raFakeBDStdoku();
  private hr.restart.robno.raFakeBDStdoku raFakeBDStdokuOLD = new hr.restart.robno.raFakeBDStdoku();
  private hr.restart.robno.raFakeBDStdoki raFakeBDStdoki = new hr.restart.robno.raFakeBDStdoki();
  private hr.restart.robno.raFakeBDStdoki raFakeBDStdokiOLD = new hr.restart.robno.raFakeBDStdoki();
  private hr.restart.robno.raFakeBDStmeskla raFakeBDStmeskla = new hr.restart.robno.raFakeBDStmeskla();
  private hr.restart.robno.raFakeBDStmeskla raFakeBDStmesklaOLD = new hr.restart.robno.raFakeBDStmeskla();
  private HashMap hm = new HashMap();
  private HashMap hmpartneri = new HashMap();
  private QueryDataSet stanje = null;
  private QueryDataSet skladista = null;
  private QueryDataSet replinfo = null;
  private QueryDataSet artikli = null;
  private QueryDataSet partneri = null;
  private QueryDataSet kupci = null;
  private static boolean isSkladLoad = false;
  private static boolean isStanjeLoad = false;
  private static boolean isReplInfoLoad = false;
  private static boolean isArtikliLoaded = false;
  private static boolean isPartneriLoaded = false;
  private static boolean isKupciLoaded = false;
  private static int offset=1;
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private HashMap hmdatabase = new HashMap();


  public static boolean bFastTransfer=true;

  public boolean repl_1(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
    throw new java.lang.UnsupportedOperationException("Method repl_1() not yet implemented."+ds_from.imetab);
  }

  public boolean repl_2(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
    throw new java.lang.UnsupportedOperationException("Method repl_1() not yet implemented.");
  }

    /**
     *  Samo za prometne podatke koji ažuriraju stanje
     * @param ds_from BazaServer.gdb
     * @param ds_to
     * @return
     */


  public boolean repl_3(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){

    hmdatabase.put("ds_from",ds_from.ds.getDatabase());
    hmdatabase.put("ds_to",ds_to.ds.getDatabase());

    try {
      ds_from.ds.open();
      if (ds_from.ds.getRowCount() == 0) {
        return true;
      }
      ds_to.ds.open();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    int rows = ds_from.ds.getRowCount();
    if (raReplicateRobno.isProcess()) {
      hr.restart.util.raProcess.setMessage("Replikacija "+ds_to.imetab+"  ...", true);
    } else {
      System.out.println("Za prenijeti "+ds_to.imetab+"= "+rows);
    }
    findAllReplInfo(ds_from);

    if ("stdoku".equalsIgnoreCase(ds_from.imetab) ||
        "stdoki".equalsIgnoreCase(ds_from.imetab) ||
        "stmeskla".equalsIgnoreCase(ds_from.imetab)) {
//      findAllStanje(ds_to);
      findAllSklad(ds_to);
    }
    boolean isStatus = false;
    int mycart = 0;
    for (ds_from.ds.first();ds_from.ds.inBounds();ds_from.ds.next()) {
      if (raReplicateRobno.isProcess()) {
        hr.restart.util.raProcess.setMessage("Preneseno "+ds_to.imetab+" "+
            ds_from.ds.getRow()+"/"+rows, true);
      }
      ds_to.ds.insertRow(true);
      hr.restart.baza.dM.copyColumns(ds_from.ds,ds_to.ds);
      int cpar=0;
      if ("doku".equalsIgnoreCase(ds_from.imetab) || "doki".equalsIgnoreCase(ds_from.imetab)) {
        cpar = mantencePartner(ds_from,ds_to);
        if (cpar !=0) {
          ds_to.ds.setInt("CPAR",cpar);
        }
      }
      if ("stdoku".equalsIgnoreCase(ds_from.imetab)) {
//        mycart = mantenceArtikli(ds_from,ds_to);
//        ds_to.ds.setInt("CART",mycart);
// privremeno zakomentirano zbog sranja u prijenosima
/*
        stanje = getStanjeSet(ds_to,ds_from.ds.getString("CSKL"),ds_from.ds.getString("GOD"),
                        mycart);

        if (bFastTransfer && stanje.getRowCount()==0) {
//System.out.println("bFastTransfer = "+bFastTransfer);
          stanje.insertRow(true);
          stanje.setString("CSKL",ds_from.ds.getString("CSKL"));
          stanje.setString("GOD",ds_from.ds.getString("GOD"));
          stanje.setInt("CART",mycart);
          initRestQDS(stanje);
        }
        raFakeBDStanje.Init();
        raFakeBDStanjeOLD.Init();
        raFakeBDStdoku.Init();
        raFakeBDStdokuOLD.Init();
        lc.TransferFromDB2Class(stanje,raFakeBDStanje);
        lc.TransferFromDB2Class(ds_from.ds,raFakeBDStdoku);
        raFakeBDStanje.sVrSklad = vrstaZalihe(skladista,ds_from.ds.getString("CSKL"));
        rKBDStanje.kalkStanjefromStdoku(raFakeBDStanje,raFakeBDStdoku,raFakeBDStdokuOLD,ds_from.ds.getString("VRDOK"));
        lc.TransferFromClass2DB(stanje,raFakeBDStanje);
*/
      }
      else if ("stdoki".equalsIgnoreCase(ds_from.imetab)) {
//        mycart = mantenceArtikli(ds_from,ds_to);
//        ds_to.ds.setInt("CART",mycart);
        // privremeno zakomentirano zbog sranja u prijenosima
/*
        stanje = getStanjeSet(ds_to,ds_from.ds.getString("CSKL"),ds_from.ds.getString("GOD"),
                        mycart);

        if (bFastTransfer && stanje.getRowCount()==0) {
//System.out.println("bFastTransfer = "+bFastTransfer);
          stanje.insertRow(true);
          stanje.setString("CSKL",ds_from.ds.getString("CSKL"));
          stanje.setString("GOD",ds_from.ds.getString("GOD"));
          stanje.setInt("CART",mycart);
          initRestQDS(stanje);
        }
        raFakeBDStanje.Init();
        raFakeBDStanjeOLD.Init();
        raFakeBDStdoki.Init();
        raFakeBDStdokiOLD.Init();
        lc.TransferFromDB2Class(stanje,raFakeBDStanje);
        lc.TransferFromDB2Class(ds_from.ds,raFakeBDStdoki);
        raFakeBDStanje.sVrSklad = vrstaZalihe(skladista,ds_from.ds.getString("CSKL"));
        rKBDStanje.kalkStanjefromStdoki(raFakeBDStanje,raFakeBDStdoki,raFakeBDStdokiOLD,ds_from.ds.getString("VRDOK"));
        lc.TransferFromClass2DB(stanje,raFakeBDStanje);
*/
      }
      else if ("stmeskla".equalsIgnoreCase(ds_from.imetab)) {
//        mycart = mantenceArtikli(ds_from,ds_to);
//        ds_to.ds.setInt("CART",mycart);
        // privremeno zakomentirano zbog sranja u prijenosima
/*
        raFakeBDStanje.Init();
        raFakeBDStanjeOLD.Init();
        raFakeBDStmeskla.Init();
        raFakeBDStmesklaOLD.Init();
        lc.TransferFromDB2Class(ds_from.ds,raFakeBDStmeskla);
        if (ds_from.ds.getString("VRDOK").equalsIgnoreCase("MES") ||
            ds_from.ds.getString("VRDOK").equalsIgnoreCase("MEU")) {
          stanje = getStanjeSet(ds_to,ds_from.ds.getString("CSKLUL"),ds_from.ds.getString("GOD"),
                                mycart);

        if (bFastTransfer && stanje.getRowCount()==0) {
//System.out.println("bFastTransfer = "+bFastTransfer);
            stanje.insertRow(true);
            stanje.setString("CSKL",ds_from.ds.getString("CSKLUL"));
            stanje.setString("GOD",ds_from.ds.getString("GOD"));
            stanje.setInt("CART",mycart);
            initRestQDS(stanje);
          }

          lc.TransferFromDB2Class(stanje,raFakeBDStanje);
          raFakeBDStanje.sVrSklad = vrstaZalihe(skladista,ds_from.ds.getString("CSKLUL"));
          rKBDStanje.kalkStanjefromStmesklaUlaz(raFakeBDStanje,raFakeBDStmeskla,raFakeBDStmesklaOLD,false);
          lc.TransferFromClass2DB(stanje,raFakeBDStanje);
*/
        }
        if (ds_from.ds.getString("VRDOK").equalsIgnoreCase("MES") ||
            ds_from.ds.getString("VRDOK").equalsIgnoreCase("MEI")) {

/*
          stanje = getStanjeSet(ds_to,ds_from.ds.getString("CSKLIZ"),ds_from.ds.getString("GOD"),
                                mycart);
        if (bFastTransfer && stanje.getRowCount()==0) {
//System.out.println("bFastTransfer = "+bFastTransfer);
            stanje.insertRow(true);
            stanje.setString("CSKL",ds_from.ds.getString("CSKLIZ"));
            stanje.setString("GOD",ds_from.ds.getString("GOD"));
            stanje.setInt("CART",mycart);
            initRestQDS(stanje);
          }

          lc.TransferFromDB2Class(stanje,raFakeBDStanje);
          raFakeBDStanje.sVrSklad = vrstaZalihe(skladista,ds_from.ds.getString("CSKLIZ"));
          rKBDStanje.kalkStanjefromStmesklaIzlaz(raFakeBDStanje,raFakeBDStmeskla,raFakeBDStmesklaOLD);
          lc.TransferFromClass2DB(stanje,raFakeBDStanje);
        }
*/
      }
      findAndUpdateReplInfo(ds_from);

      if ("doku".equalsIgnoreCase(ds_from.imetab) || "doki".equalsIgnoreCase(ds_from.imetab) ||
          "meskla".equalsIgnoreCase(ds_from.imetab)) {
            ds_from.ds.setString("STATUS","P");
            isStatus = true;
          } else {
            isStatus = false;
          }
      }
      hm.put(ds_to.imetab,ds_to.ds);
      if (isStatus) {
        System.out.println(ds_from.imetab);
        hm.put(ds_from.imetab+"from",ds_from.ds);
      }
      return true;
    }



    public boolean repl_4(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
      throw new java.lang.UnsupportedOperationException("Method repl_4() not yet implemented.");
    }
    public boolean repl_5(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
      throw new java.lang.UnsupportedOperationException("Method repl_5() not yet implemented.");
    }
    public boolean repl_6(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
      throw new java.lang.UnsupportedOperationException("Method repl_6() not yet implemented.");
    }
    public boolean repl_7(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
      throw new java.lang.UnsupportedOperationException("Method repl_7() not yet implemented.");
    }
    public boolean repl_8(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
      throw new java.lang.UnsupportedOperationException("Method repl_8() not yet implemented.");
    }
    public boolean repl_9(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
      throw new java.lang.UnsupportedOperationException("Method repl_9() not yet implemented.");
    }

    /**
     * Metoda potrebne za replikaciju van interface-a
     *
     */

    private void initRestQDS(QueryDataSet qds) {
      Column[] cols = qds.getColumns();
      for (int i = 0;i<cols.length;i++) {
        if (!("CSKL".equalsIgnoreCase(cols[i].getColumnName()) ||
            "GOD".equalsIgnoreCase(cols[i].getColumnName()) ||
            "CART".equalsIgnoreCase(cols[i].getColumnName()))) {
           if (cols[i].getSqlType()== java.sql.Types.DECIMAL ||
               cols[i].getSqlType()== java.sql.Types.NUMERIC) {
             qds.setBigDecimal(cols[i].getColumnName(),new java.math.BigDecimal("0.00"));
           }
           else if (cols[i].getSqlType()== java.sql.Types.VARCHAR) {
             qds.setString(cols[i].getColumnName(),"");
           }
         }
      }
    }

    private void findAndUpdateReplInfo(raReplicate.ReplDataSet ds_from){
      if (ld.raLocate(replinfo,new String[] {"IMETAB","KEYTAB"},new String[] {ds_from.imetab,raReplicate.getKeyTab(ds_from.ds)})){
        replinfo.setString("REP_FLAG","D");
      }
    }

    private QueryDataSet getStanjeSet(raReplicate.ReplDataSet rds,String cskl,String god,int cart) {
      if (bFastTransfer) {
        if (hm.containsKey("stanje"+cskl+"-"+god+"-"+cart)) {
          return (QueryDataSet) hm.get("stanje"+cskl+"-"+god+"-"+cart);
        }
        QueryDataSet mystanje = findQDS(rds,
                                        "select * from stanje where cskl='"+cskl+"' and god='"+god+"' and cart="+cart,true);
        hm.put("stanje"+cskl+"-"+god+"-"+cart,mystanje);
        return mystanje;
      } else {
        QueryDataSet mystanje;
        if (hm.containsKey("stanje")) {
          mystanje = (QueryDataSet) hm.get("stanje");
        } else {
          mystanje = findQDS(rds,"select * from stanje",true);
          hm.put("stanje",mystanje);
        }
        if (!ld.raLocate(mystanje,new String[] {"CSKL","GOD","CART"},
                                  new String[] {cskl,god,String.valueOf(cart)})){
          mystanje.insertRow(true);
          mystanje.setString("CSKL",cskl);
          mystanje.setString("GOD",god);
          mystanje.setInt("CART",cart);
          initRestQDS(mystanje);
        }
        return mystanje;
      }
    }
/*
    private boolean findStanje(DataSet stanje,String cskl,String god,int cart) {
      return ld.raLocate(stanje,new String[] {"CSKL","GOD","CART"},new String[] {cskl,god,String.valueOf(cart)});
    }
*/
    private String vrstaZalihe(DataSet skladista,String cskl) {
      if (ld.raLocate(skladista,new String[] {"CSKL"},new String[] {cskl})){
        return skladista.getString("VRZAL");
      }
      return "";
    }
/*
    private void findAllStanje(raReplicate.ReplDataSet ds_to) {
      if (!isStanjeLoad) {
        stanje = new QueryDataSet();
        String qry = "select * from stanje";
        stanje.setResolver(ds_to.ds.getResolver());
        stanje.setQuery(new QueryDescriptor(ds_to.ds.getDatabase(),qry,null, true, Load.ALL));
        stanje.open();
        isStanjeLoad = true;
        hm.put("stanje",stanje);
      }
    }
*/
    private QueryDataSet findAllArtikli(raReplicate.ReplDataSet rds) {
      if (!isArtikliLoaded)  {
        artikli = findQDS(rds,"select * from artikli ",false);
        artikli.setColumns(hr.restart.baza.dM.getDataModule().getArtikli().cloneColumns());
        artikli.open();
        isArtikliLoaded = true;
      }
      return artikli;
    }
    private QueryDataSet findQDS(raReplicate.ReplDataSet rds,String query,boolean needopen) {
      QueryDataSet tmpQDS = new QueryDataSet();
      tmpQDS.setResolver(rds.ds.getResolver());
      tmpQDS.setQuery(new QueryDescriptor(rds.ds.getDatabase(),query,null, true, Load.ALL));
      if (needopen) tmpQDS.open();
      return tmpQDS;

    }
    private QueryDataSet findQDS(raReplicate.ReplDataSet rds,String query) {
      return findQDS(rds,query,true);
    }

    private int mantenceArtikli(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
      artikli = findQDS(ds_to,"select * from artikli where cart1 = '"+ds_from.ds.getString("CART1")+"'",true);
      if (artikli.getRowCount()>0){
        return artikli.getInt("CART");
      }
      else if (hm.containsKey("artikli"+ds_from.ds.getString("CART1"))){
        QueryDataSet art = (QueryDataSet) hm.get("artikli"+ds_from.ds.getString("CART1"));
        return art.getInt("CART");
      }
      else {
        int novicart =ds_from.ds.getInt("CART");
        if (findQDS(ds_to,"select * from artikli where cart = "+novicart,true).getRowCount()>0){
          novicart = findQDS(ds_to,"SELECT MAX(cart) as MMAX from ARTIKLI").getInt("MMAX")+offset;
          offset=offset+1;
        }

        QueryDataSet forPrenos = findQDS(ds_from,"SELECT * FROM ARTIKLI WHERE CART = "+
            ds_from.ds.getInt("CART"));


        if (forPrenos.getRowCount()==0) {
          return novicart;
        }

        artikli.insertRow(true);
        hr.restart.baza.dM.copyColumns(forPrenos,artikli);
        artikli.setInt("CART",novicart);
        if (findQDS(ds_to,"select * from artikli where bc = '"+forPrenos.getString("BC")+"'",true).getRowCount()>0){
          System.out.println("greska isti BC "+forPrenos.getString("BC"));
          artikli.setString("BC","GR"+novicart);
//          return ds_from.ds.getInt("CART");
        }

        if (raReplicateRobno.isProcess()) {
          hr.restart.util.raProcess.setMessage("Art. cart_old ="+ds_from.ds.getInt("CART")+
              "cart_new = "+novicart, true);
        }
//        hm.put("artikli"+novicart,artikli);
        hm.put("artikli"+ds_from.ds.getString("CART1"),artikli);
        return novicart;
      }
    }
    private int lmantenceArtikli(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){
      if (ld.raLocate(findAllArtikli(ds_to),new String[] {"CART1"},new String[] {ds_from.ds.getString("CART1")})){
        return artikli.getInt("CART");
      } else {
        int novicart =ds_from.ds.getInt("CART");
        if (ld.raLocate(findAllArtikli(ds_to),new String[] {"CART"},
                        new String[] {String.valueOf(novicart)})){
//System.out.println(offset);
          novicart = findQDS(ds_to,"SELECT MAX(cart) as MMAX from ARTIKLI").getInt("MMAX")+offset;
          offset=offset+1;
//System.out.println(offset);
        }

        QueryDataSet forPrenos = findQDS(ds_from,"SELECT * FROM ARTIKLI WHERE CART = "+
            ds_from.ds.getInt("CART"));
        if (forPrenos.getRowCount()==0) {
          return novicart;
        }
        artikli.insertRow(true);
        hr.restart.baza.dM.copyColumns(forPrenos,artikli);
        artikli.setInt("CART",novicart);
        if (raReplicateRobno.isProcess()) {
          hr.restart.util.raProcess.setMessage("Art. cart_old ="+ds_from.ds.getInt("CART")+
              "cart_new = "+novicart, true);
          ST.prn(forPrenos);
        }
ST.prn(forPrenos);
        hm.put("ser-artikli",artikli);
        return novicart;
      }
    }

    private int mantencePartner(raReplicate.ReplDataSet ds_from,raReplicate.ReplDataSet ds_to){

      int partner = ds_from.ds.getInt("CPAR");
      if (partner==0) return partner;
      if (hmpartneri.containsKey("partneri"+partner)) {
//System.out.println("Imam ga "+ partner);
        return partner;
      }
      QueryDataSet partnerFrom = findQDS(ds_from,"select * from partneri where cpar="+partner,true);
      if (partnerFrom.getRowCount()==0) {
//System.out.println("Nema partnera :((( "+ partner);
        return partner;
      }
      QueryDataSet partnerTo = findQDS(ds_to,"select * from partneri where cpar="+partner,true);

      if (partnerTo.getRowCount()==0) {
        QueryDataSet malaP =
            findQDS(ds_to,"select * from partneri where mb='"+partnerFrom.getString("MB")+"'",true);
//System.out.println(malaP.getQuery().getQueryString());
        if (malaP.getRowCount()>0) {
//System.out.println("malaP.getRowCount()");
          partner = malaP.getInt("CPAR");
        } else {
          QueryDataSet malaP2 =
              findQDS(ds_to,"select * from partneri where zr='"+partnerFrom.getString("ZR")+"'",true);
          if (malaP2.getRowCount()>0) {
//System.out.println("malaP2.getRowCount()>0");
            partner = malaP2.getInt("CPAR");
          } else {
            hr.restart.db.raPreparedStatement rPS =
                new hr.restart.db.raPreparedStatement("partneri",
                                                      hr.restart.db.raPreparedStatement.INSERT,
                                                      ds_to.ds.getDatabase().getJdbcConnection());

            String[] columns = raConnectionFactory.getColumns(
                                     ds_to.ds.getDatabase().getJdbcConnection(),
                                     "partneri");

//Variant myvariant = new Variant();
            for (int i=0 ;i< columns.length;i++) {
              rPS.setValue(columns[i],hr.restart.db.raVariant.getDataSetValue(partnerFrom,columns[i]),false);
//partnerFrom.getVariant(columns[i],myvariant);
//System.out.println(columns[i]+"-"+myvariant);
            }
//System.out.println(rPS.getInsertSql());
//System.out.println(partner);
//ST.prn(partnerFrom);
            hmpartneri.put("partneri"+partner,rPS);
          }
        }
      }
      return partner;
    }

    private void findAllSklad(raReplicate.ReplDataSet ds_to) {
      if (!isSkladLoad) {
        skladista = new QueryDataSet();
        skladista.setResolver(ds_to.ds.getResolver());
        skladista.setQuery(new QueryDescriptor(ds_to.ds.getDatabase(),"SELECT * FROM SKLAD",null, true, Load.ALL));
        skladista.open();
        isSkladLoad= true;
      }
    }

    private void findAllReplInfo(raReplicate.ReplDataSet ds_from) {
      if (!isReplInfoLoad) {
        replinfo = new QueryDataSet();
        replinfo.setResolver(ds_from.ds.getResolver());
        replinfo.setQuery(new QueryDescriptor(ds_from.ds.getDatabase(),"SELECT * FROM REPLINFO",null, true, Load.ALL));
        replinfo.open();
        hm.put("replinfo",replinfo);
        isReplInfoLoad= true;
      }
    }

    public boolean saveAll() {
      if (!hm.isEmpty()) {
//        hr.restart.util.raLocalTransaction trans =
//            new hr.restart.util.raLocalTransaction(new Database [] {
//                                                   (Database) hmdatabase.get("ds_from"),
//                                                   (Database) hmdatabase.get("ds_to")}){
//          public boolean transaction() throws Exception {

            for (Iterator iter = hm.keySet().iterator();iter.hasNext();) {
              Object slijedeci = iter.next();
              String title = String.valueOf(slijedeci);
              if (raReplicateRobno.isProcess()) {
                hr.restart.util.raProcess.setMessage("Spremanje prijenosa "+title,true);
              } else {
                System.out.println("Spremanje prijenosa "+title);
              }
              try {
                hr.restart.util.raTransaction.saveChanges(
                    (QueryDataSet) hm.get(slijedeci));
              }
              catch (Exception ex) {
                ex.printStackTrace();
                return false;
              }
            }

            for (Iterator iter = hmpartneri.keySet().iterator();iter.hasNext();) {
              Object slijedeci = iter.next();
              String title = String.valueOf(slijedeci);
              if (raReplicateRobno.isProcess()) {
                hr.restart.util.raProcess.setMessage("Spremanje prijenosa "+title,true);
              } else {
                System.out.println("Spremanje prijenosa "+title);
              }
              try {
                ((hr.restart.db.raPreparedStatement) hmpartneri.get(slijedeci)).execute();
              }
              catch (SQLException ex) {
                ex.printStackTrace();
                return false;
              }
            }

            if (raReplicateRobno.isProcess()) {
              hr.restart.util.raProcess.setMessage("Èišæenje ",true);
            }
            hm.clear();
            hmpartneri.clear();
            hmdatabase.clear();
            stanje = null;
            skladista = null;
            replinfo = null;
            artikli = null;
            partneri = null;
            kupci = null;
            isSkladLoad = false;
            isStanjeLoad = false;
            isReplInfoLoad = false;
            isArtikliLoaded = false;
            isPartneriLoaded = false;
            isKupciLoaded = false;
            offset=1;
            return true;
//            return false;

          }

//        };
//        return trans.execTransaction();
//      }
      return true;
    }
}