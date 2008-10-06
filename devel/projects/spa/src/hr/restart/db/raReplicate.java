/****license*****************************************************************
**   file: raReplicate.java
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

import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.ConnectionDescriptor;
import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public abstract class raReplicate {
  private String batch_index = "";
  private static String keyDelimiter = "-";
  private TreeMap replMap = new TreeMap();
  private raReplicator replicator;
  private replThread replDretva;
  private boolean useflags = true;
  protected raReplicate() {
  }

  public raReplicate(String batch_idx) {
    this(batch_idx,true);
  }

  public raReplicate(String batch_idx, boolean _useflags) {
    hr.restart.baza.dM.getDataModule();
    batch_index = batch_idx;
    useflags = _useflags;
    try {
      before_init();
      init();
      after_init();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * tu se definiraju filteri i drugo prije pocetka replikacije
   * npr:
   * <pre>
   *
   * </pre>
   */
  public void init() throws Exception {

  }

  /**
   * punjenje
   */
  private void before_init() throws Exception {
    QueryDataSet repldef = Util.getNewQueryDataSet(
        "SELECT * FROM repldef where repldef.BATCH_INDEX = '"
        +getBatch_index()+"' ORDER BY SLIJED, NACINREP");
    int i = 0;
    repldef.first();
    do {
      //iz
      i = i + 1;
      replMap.put(new Integer(i),createReplDataSet(repldef,true));
      //u
      i = i + 1;
      replMap.put(new Integer(i),createReplDataSet(repldef,false));
    } while (repldef.next());
  }

  private void after_init() throws Exception {
    try {
      for (Iterator i = replMap.keySet().iterator(); i.hasNext(); ) {
        Object key = i.next();
        ReplDataSet rplds = (ReplDataSet)replMap.get(key);
        rplds.ds.open();
        //test
//        System.out.println("replquery = "+rplds.ds.getQuery().getQueryString());
//        System.out.println("replurl = "+rplds.ds.getDatabase().getConnection().getConnectionURL());
      }
    }
    catch (Exception ex) {

    }
  }
  private ReplDataSet createReplDataSet(QueryDataSet repldef, boolean iz) {
    QueryDataSet replQds = new QueryDataSet();
    String qry = getRQuery(repldef,iz,"N",null);
    Database db = getRDatabase(repldef,iz);
//    System.out.println("qry = "+qry);
//    System.out.println("db = "+db.getConnection().getConnectionURL());
    replQds.setQuery(new QueryDescriptor(db,qry));
    ReplDataSet rplds = new ReplDataSet(replQds,repldef.getString("IMETAB"),repldef.getString("NACINREP"),iz);
    return rplds;
  }

  private String getRQuery(DataSet repldef,boolean iz,String repflag, String addqry) {
    String imetab = repldef.getString("IMETAB");
    String qkeytab = getKeyTab(imetab);
    String qry = "";
    if (useflags) {
      qry = "SELECT * FROM "+imetab+" where ("+qkeytab+") in (select replinfo.keytab from replinfo where replinfo.rep_flag='"+repflag+"')";
    } else {
      qry = "SELECT "+imetab+".* FROM "+imetab;
    }
System.out.println(qry);
    return qry;
  }

  private static Database getRDatabase(DataSet repldef,boolean iz) {
    String rbr_url_column = iz?"RBR_URL":"RBR_URL_U";
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    int rbr_url = repldef.getShort(rbr_url_column);
    if (rbr_url == 0) {
      return dm.getDatabase1();
    }
    StorageDataSet urls=dm.getReplurl();
    boolean b = lookupData.getlookupData().raLocate(urls,"RBR_URL",Integer.toString(rbr_url));
    Database db = new Database();
    String conURL = urls.getString("URL");
    String conUSER = urls.getString("USR");
    String conPASS = urls.getString("PASS");
    String conTIP = urls.getString("DRIVER");

    db.setConnection(
        new ConnectionDescriptor(conURL,conUSER,conPASS,false,conTIP,
        dm.getDatabase1().getConnection().getProperties()));
    return db;
  }

  /**
   * vraca datasetove koji imaju nacin replikacije nacinrep
   */
/*  public QueryDataSet[] getRepDatasets(String nacinrep) {
    throw new UnsupportedOperationException("Not jet implemented");
  }*/
  private Database[] getDatabases() {
    HashMap dbMap = new HashMap();
    for (Iterator i = replMap.keySet().iterator(); i.hasNext(); ) {
      Object item = i.next();
      ReplDataSet rds = (ReplDataSet)replMap.get(item);
      String dbMapKey = rds.ds.getDatabase().getConnection().getConnectionURL();
      if (dbMap.get(dbMapKey) == null) {
        dbMap.put(dbMapKey,rds.ds.getDatabase());
      }
    }
    Database[] dbs = new Database[dbMap.size()];
    int arrIndex = 0;
    for (Iterator i = dbMap.keySet().iterator(); i.hasNext(); ) {
      Object key = i.next();
      dbs[arrIndex] = (Database)dbMap.get(key);
      arrIndex++;
    }
/*    for (int i = 0; i < dbs.length; i++) {
      System.out.println("con"+i+" = "+dbs[i].getConnection().getConnectionURL());
    }*/
    return dbs;
  }
  /**
   * vraca odedjeni dataset po imenu tablice, nacinu replikacije i iz
   */
  public ReplDataSet getRepDataset(String imetab,String nacinrepl,boolean iz) {
//    throw new UnsupportedOperationException("Not jet implemented");
    for (Iterator i = replMap.keySet().iterator(); i.hasNext(); ) {
      Object key = i.next();
      ReplDataSet rplds = (ReplDataSet)replMap.get(key);
      if (rplds.imetab.equals(imetab) && rplds.nacinrepl.equals(nacinrepl) && rplds.iz == iz) {
        return rplds;
      }
    }
    return null;
  }

  /**
   * postavlja dodatni filter na dataset koji reprezentira tablicu imetab.
   * Filter je u formatu npr. 'CSKLUL = '8''
   */
  public void setFilter(String imetab, String nacinrepl, String filter, boolean iz) throws Exception {
    //u replMapu nadji ds i azuriraj query
    QueryDataSet fqds = getRepDataset(imetab,nacinrepl,iz).ds;
    if (fqds == null) throw new NullPointerException(
        "Nije zadan "+(iz?"lokalni":"centralni")+
        " dataset sa imenom tablice "+imetab+" i nacinom "+nacinrepl+"!!");
    String qry = fqds.getQuery().getQueryString();
    Database qdb = fqds.getDatabase();
    qry = qry.concat(" ").concat(filter);
    fqds.setQuery(new QueryDescriptor(qdb,qry));
  }

  /**
   * starta replikaciju: dize transakciju na svim bazama i radi copyto te zove user implementaciju
   */
  public boolean start() {
//    replDretva = new replThread();
//    replDretva.start();
    getNewReplThread().start();
    try {
      replDretva.join();
    }
    catch (InterruptedException ex) {
    }
    return success;
  }

  /**
   * vraca current thread
   * @return
   */
  public replThread getReplThread() {
    return replDretva;
  }


  /**
   *  za upacivanje u raProcces treba nam novi replThread
   * @return uvijek novi replThread i puni replDretvu
   */

  public replThread getNewReplThread() {
    replDretva = new replThread();
    return replDretva;
  }


  /**
   * tu se validira replikacija, ako nesto ne stima returnaj false i sve se ponistava
   */
  public abstract boolean validacija();

  private boolean success = true;

  public boolean isSuccess() {
    return success;
  }


  /**
   * Identifikator replikacijskog batch-a; na njega se veze tablica REPLDEF
   * u kojoj su definirane tabele za replikaciju
   */
  public String getBatch_index() {
    return batch_index;
  }
//thread koji sve starta
  class replThread extends Thread {
    public void run() {
      success = new raLocalTransaction(getDatabases()) {

        //set tablica i nacina koji su vec odradjeni
        HashSet doneRPSet = new HashSet();
        //kontrolira da li je napravljena ta metoda, ako nije dodaje u set i smatra napravljenom
        private boolean isDone(String imetab,String nacin) {
          for (Iterator i = doneRPSet.iterator(); i.hasNext(); ) {
            String[] item = (String[])i.next();
            if (item[0].equals(imetab) && item[1].equals(nacin)) return true;
          }
          doneRPSet.add(new String[] {imetab,nacin});
          return false;
        }

        public boolean transaction() throws Exception {
System.out.println("transaction");
          //ako nije setiran custom replicator, setiram defaultni (replLogic)
          if (getReplicator() == null) setReplicator(new raReplLogic());
          doneRPSet.clear();
          //kroz replMap
          for (Iterator i = replMap.keySet().iterator(); i.hasNext(); ) {
            Object key = i.next();
            //tekuci ReplDataSet
            ReplDataSet replds = (ReplDataSet)replMap.get(key);
            if (!isDone(replds.imetab,replds.nacinrepl)) {
              //nadji lokalni dataset (iz_ds) i centralni dataset (u_ds)
              ReplDataSet iz_ds;
              ReplDataSet u_ds;
              if (replds.iz) {
                iz_ds = replds;
                u_ds = getRepDataset(replds.imetab,replds.nacinrepl,false);
              } else {
                iz_ds = getRepDataset(replds.imetab,replds.nacinrepl,true);
                u_ds = replds;
              }
              //pozovi metodu raReplicator-a reflectionom u odnosu na nacinrep
              String nacrep = replds.nacinrepl;
              java.lang.reflect.Method repMethod =
                  getReplicator().getClass().getMethod("repl_"+nacrep,new Class[] {ReplDataSet.class,ReplDataSet.class});
              repMethod.invoke(getReplicator(),new Object[] {iz_ds,u_ds});
            }
          }
          //implementirana validacija
System.out.println("kraj transaction");
          return validacija();
        }
      }.execTransaction();
    }
  }

  public void setReplicator(raReplicator _replicator) {
    replicator = _replicator;
  }

  public raReplicator getReplicator() {
    return replicator;
  }

//staticke metode

  /**
   * vraca rep_flag iz repl_info po pripadajucem kljucu current sloga u zadanom datesetu
   * <pre>
   * za sada moze biti:
   *  'N' ako nije nasao slog u repl_info, ili ako je u pronadjenom slogu upisan 'N'
   *  'P' zadani slog je prenesen na centralni server
   * </pre>
   */
  public static String getReplStatus(QueryDataSet ds) {
    String imetab = Valid.getTableName(ds.getQuery().getQueryString());
    String keytab = getKeyTab(ds);
    // select rep_flag from repl_info where imetab = 'imetab' and keytab = 'keytab'
    // if getrowcount = 0 return 'N'
    throw new UnsupportedOperationException("Not jet implemented");
  }

  public static String getKeyTab(String imetab,HashMap hm) {
    String[] keys = raConnectionFactory.getKeyColumns(
        hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection(),imetab);
    String keytab = "";
    for (int i = 0; i < keys.length; i++) {
      keytab = keytab.concat(imetab).concat(".").concat(keys[i]).concat("||'").concat(keyDelimiter).concat("'||");
    }
    keytab = keytab.concat("''");
    return keytab;
  }
  /**
   * nalazi konkatinaciju naziva kljuceva za replinfo.keytab pogodnih za sql query
   */
  public static String getKeyTab(String imetab) {
    String[] keys = raConnectionFactory.getKeyColumns(
        hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection(),imetab);
    String keytab = "";
    for (int i = 0; i < keys.length; i++) {
      keytab = keytab.concat(imetab).concat(".").concat(keys[i]).concat("||'").concat(keyDelimiter).concat("'||");
    }
    keytab = keytab.concat("''");
    return keytab;
  }

  /**
   * nalazi konkatinaciju vrijednosti kljuceva za replinfo.keytab
   */
  public static String getKeyTab(QueryDataSet ds) {
    String imetab = Valid.getTableName(ds.getQuery().getQueryString());
    String[] keys = raConnectionFactory.getKeyColumns(ds.getDatabase().getJdbcConnection(),imetab);
    String keytab = "";
    for (int i = 0; i < keys.length; i++) {
      Variant v = new Variant();
      ds.getVariant(keys[i],v);
      String keyVal;// = v.toString();
      Column c = ds.getColumn(keys[i]);
      if (c.getDataType() == Variant.STRING) {
        String _s = v.toString();
        int cl = c.getPrecision() - _s.length();
        String _sa = "";
        if (cl>0) {
          char[] chr = new char[cl];
          Arrays.fill(chr,' ');
          _sa = new String(chr);
        }
        keyVal = _s.concat(_sa);
      } else if (c.getDataType() == Variant.TIMESTAMP) {
        //to ne bude islo
        keyVal = new java.sql.Date(v.getTimestamp().getTime()).toString();
      } else {
        keyVal = v.toString();
      }
      keytab = keytab.concat(keyVal).concat(keyDelimiter);
    }
    return keytab;
  }

  /**
   * vraca (najveci) nacin replikacije iz tabele repldef ili '0' ako replikacija
   * za tu tabelu nije definirana. O njemu ovisi ponasanje unosa i lookupa za zadanu tabelu
   * 0 - sve lokalno
   * 1 - trazi i lokalno i centralno, pa prebacuje iz centralnog u lokalno
   * 2 - lookup lokalno, a unos centralno
   * 3 - sve lokalno sa restrikcijama
   */
  public static String getNacinRep(String imetab) {
    //select nacinrep from repldef where imetab = 'imetab'
    // if rowcount = 0 return '0'
    //ako ima vise slogova vrati najveci
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    dm.getRepldef().open();
    dm.getRepldef().setSort(new SortDescriptor(new String[] {"IMETAB","NACINREP"},false,true));
    if (lookupData.getlookupData().raLocate(dm.getRepldef(),"IMETAB",imetab)) {
      return dm.getRepldef().getString("NACINREP");
    } else {
      return "0";
    }
  }
//
/**
 * handlanje koje se poziva u
 * 1. raMatPodaci.setRaQueryDataSet() prije stvarnog setanja
 * 2. lookupData.lookUp koji koristi JlrNavField
 * ako je nacinrep zadane tablice 1 ili 2 prebacuje dataset na URL_U
 * @return true ako je promijenio url na zadanom datasetu, false ako nije
 */
  public static boolean replicateQueryDataSet(QueryDataSet ds) {
    String imetab = Valid.getTableName(ds.getQuery().getQueryString());
    if (imetab == null) return false;
    String nacinrep = getNacinRep(imetab);//pozicionira na replDef
    if (nacinrep.equals("1") || nacinrep.equals("2")) { // centralan unos
      Database db = getRDatabase(hr.restart.baza.dM.getDataModule().getRepldef(),false);
      if (db.getConnection().getConnectionURL()
          .equals(ds.getDatabase().getConnection().getConnectionURL())) {
        return false;
      }
      String qry = ds.getQuery().getQueryString();
      ds.setQuery(new QueryDescriptor(db,qry));
      return true;
    }
    return false;
  }
//klasa za replMap - okosnica definicije replikacije :)))
  public class ReplDataSet {
    QueryDataSet ds;
    String imetab;
    String nacinrepl;
    boolean iz;

    public ReplDataSet(QueryDataSet _ds, String _imetab, String _nacinrepl, boolean _iz) {
      ds = _ds;
      imetab = _imetab;
      nacinrepl = _nacinrepl;
      iz = _iz;
    }

    public boolean equals(Object obj) {
      if (obj instanceof ReplDataSet) {
        ReplDataSet rds = (ReplDataSet)obj;
        return imetab.equals(rds.imetab) && nacinrepl.equals(rds.nacinrepl) && iz == rds.iz;
      } else return super.equals(obj);
    }
  }

  //test
  public static void main(String[] args) {
    new raReplicate("test",false) {
      public void init() throws Exception {
        setFilter("partneri","1"," AND CPAR BETWEEN 1000 AND 2000",false);
        setFilter("partneri","1"," AND CPAR BETWEEN 5000 AND 8000",true);
      }
      public boolean validacija() {
        return true;
      }
    }.start();
  }
}