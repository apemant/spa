/****license*****************************************************************
**   file: reportsQuerysCollector.java
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

import hr.restart.util.raMasterDetail;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class reportsQuerysCollector {

  TypeDoc TD = TypeDoc.getTypeDoc();
  String what = "";
  raVectorRekap rVR = new raVectorRekap();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private static reportsQuerysCollector RQCclass;
  private String SQLrealone = "";
  private String SQLracuni = "select * from  doki, stdoki where "+
		"doki.cskl=stdoki.cskl and doki.vrdok=stdoki.vrdok and doki.god=stdoki.god and doki.brdok=stdoki.brdok ";
  private String SQLracdod="";
  private String SQLmeskla = "select * from  meskla, stmeskla where "+
		"meskla.cskliz=stmeskla.cskliz and meskla.csklul=stmeskla.csklul and "+
		"meskla.vrdok=stmeskla.vrdok and meskla.god=stmeskla.god and meskla.brdok=stmeskla.brdok ";
  private String SQLprikalk = "select * from  doku, stdoku where "+
		"doku.cskl=stdoku.cskl and doku.vrdok=stdoku.vrdok and doku.god=stdoku.god "+
                "and doku.brdok=stdoku.brdok";

  private String SQLdnevnik =
  // ulaz primke
  "SELECT doku.cskl,doku.datdok,doku.vrdok,doku.brdok,doku.god,stdoku.cart,stdoku.nazart,"+
		"stdoku.kol as kolul,0. as koliz,stdoku.zc,stdoku.izad,0. as iraz FROM doku,stdoku "+
		"WHERE doku.cskl = stdoku.cskl and doku.vrdok = stdoku.vrdok and doku.brdok = stdoku.brdok "+
		"and doku.god = stdoku.god and doku.vrdok <> 'POR'"+
	 " UNION "+
  // poravnanje kod primke
  "SELECT doku.cskl,doku.datdok,'POR' as vrdok,doku.brdok,doku.god,stdoku.cart,stdoku.nazart,"+
		"0. as kolul,0. as koliz,0. as zc,stdoku.porav as izad,0. as iraz FROM doku,stdoku "+
		"WHERE doku.cskl = stdoku.cskl and doku.vrdok = stdoku.vrdok and doku.brdok = stdoku.brdok "+
		"and doku.god = stdoku.god and doku.vrdok <> 'POR' and stdoku.porav <> 0."+
	 " UNION "+
  // poravnanje
  "SELECT doku.cskl,doku.datdok,doku.vrdok,doku.brdok,doku.god,stdoku.cart,stdoku.nazart,"+
		"0. as kolul,0. as koliz,0. as zc,stdoku.porav as izad,0. as iraz FROM doku,stdoku "+
		"WHERE doku.cskl = stdoku.cskl and doku.vrdok = stdoku.vrdok and doku.brdok = stdoku.brdok "+
		"and doku.god = stdoku.god and doku.vrdok = 'POR'"+
	 " UNION "+
  // ulaz me\u0111uskladišnice
  "SELECT meskla.csklul as cskl,meskla.datdok,meskla.vrdok,meskla.brdok,meskla.god,stmeskla.cart,stmeskla.nazart,"+
		"stmeskla.kol as kolul,0. as koliz,stmeskla.zcul as zc,stmeskla.zadrazul as izad,0. as iraz "+
		"FROM meskla,stmeskla WHERE meskla.csklul = stmeskla.csklul and meskla.cskliz = stmeskla.cskliz "+
		"and meskla.vrdok = stmeskla.vrdok and meskla.brdok = stmeskla.brdok and meskla.god = stmeskla.god "+
		"and stmeskla.porav = 0."+
	 " UNION "+
  // poravnanje ulaza me\u0111uskladišnice
  "SELECT meskla.csklul as cskl,meskla.datdok,'POR' as vrdok,meskla.brdok,meskla.god,stmeskla.cart,stmeskla.nazart,"+
		"0. as kolul,0. as koliz,0. as zc,stmeskla.porav as izad,0. as iraz FROM meskla,stmeskla "+
		"WHERE meskla.csklul = stmeskla.csklul and meskla.cskliz = stmeskla.cskliz "+
		"and meskla.vrdok = stmeskla.vrdok and meskla.brdok = stmeskla.brdok and meskla.god = stmeskla.god "+
		"and stmeskla.porav <> 0."+
	 " UNION "+
  // izlaz me\u0111uskladišnice
  "SELECT meskla.cskliz as cskl,meskla.datdok,meskla.vrdok,meskla.brdok,meskla.god,stmeskla.cart,stmeskla.nazart,"+
		"0. as kolul,stmeskla.kol as koliz,stmeskla.zc,0. as izad,stmeskla.zadraziz as iraz "+
		"FROM meskla,stmeskla WHERE meskla.csklul = stmeskla.csklul and meskla.cskliz = stmeskla.cskliz "+
		"and meskla.vrdok = stmeskla.vrdok and meskla.brdok = stmeskla.brdok and meskla.god = stmeskla.god"+
	 " UNION "+
  // ostali izlazi
  "SELECT doki.cskl,doki.datdok,doki.vrdok,doki.brdok,doki.god,stdoki.cart,stdoki.nazart,"+
		"0. as kolul,stdoki.kol as koliz,stdoki.zc,0. as izad,stdoki.iraz FROM doki,stdoki "+
		"WHERE doki.cskl = stdoki.cskl and doki.vrdok = stdoki.vrdok and doki.brdok = stdoki.brdok "+
		"and doki.god = stdoki.god";

  hr.restart.baza.dM dm  = hr.restart.baza.dM.getDataModule();
  QueryDataSet repQDS = new QueryDataSet();
  public raMasterDetail caller = null;
  

  public static reportsQuerysCollector getRQCModule() {

    if (RQCclass == null) {
      RQCclass = new reportsQuerysCollector();
    } 
    return RQCclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    if (TD.isDocStdoki(what)) rVR.rakapitulacija(repQDS);
    return repQDS;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getValuteQueryDataSet() {
    QueryDataSet qds = ReportValuteTester.cambioValute(repQDS);
    if (TD.isDocStdoki(what)) rVR.rakapitulacija(qds);
    return qds;
  }

  public void ReSql(String Sqlr){
    this.what="ROT";
    ReSql(Sqlr,"ROT");
  }

  public void ReSql(String Sqlr,String what){
    this.what=what;
    caller = null;
    SQLracdod=Sqlr;
//    setQuery(what);
/*    repQDS = hr.restart.util.Util.getNewQueryDataSet(SQLrealone,true); */

    repQDS.close();
    repQDS.closeStatement();
    setQuery(what);
//----------------------------------------------------------------------------------
System.out.println("SQLrealone " +SQLrealone);
//----------------------------------------------------------------------------------
    repQDS.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
        SQLrealone, null, true, Load.ALL));
    repQDS.open();

    if (TD.isDocStdoki(what)) rVR.rakapitulacija(repQDS);
  }

  public reportsQuerysCollector() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void setQuery(String what){
    if (TD.isDocStdoki(what) || TD.isDocDOS(what)) setQueryDokiStdoki(what);
    else if (TD.isDocStdoku(what)) setQueryDokuStdoku(what);
    else if (TD.isDocStmeskla(what)) setQueryMesklaStmeskla(what);
    else if (what.equals("DNE")) setQueryDnevnik();
  }
//// templatei za dokumente

  private void setQueryDokiStdoki(String type_doc) {
    SQLrealone = SQLracuni.concat(" and doki.vrdok = '"+type_doc+"' ").concat(SQLracdod);
  }
  private void setQueryDokuStdoku(String type_doc) {
    SQLrealone = SQLprikalk.concat(" and doku.vrdok = '"+type_doc+"' ").concat(SQLracdod);
  }
  private void setQueryMesklaStmeskla(String type_doc) {
    SQLrealone = SQLmeskla.concat(" and meskla.vrdok = '"+type_doc+"' ").concat(SQLracdod);
  }
  private void setQueryDnevnik() {
      SQLrealone = SQLdnevnik;
  }
  private void jbInit() throws Exception {
    repQDS.setResolver(dm.getQresolver());
  }
  public DataSet getPorezSet(){
    return rVR.qds;
  }
  public DataSet getPoreziSet(String cskl,String vrdok,String god,int brdok ) {
    return rVR.getPoreziSet(cskl,vrdok,god,brdok);
  }
  public DataSet getPoreziSet(String cskl,String vrdok,String god,int brdok,boolean isValuta) {
    if (!isValuta)  return rVR.getPoreziSet(cskl,vrdok,god,brdok);
    return rVR.getPoreziSet(cskl,vrdok,god,brdok);
  }

}