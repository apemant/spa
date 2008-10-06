/****license*****************************************************************
**   file: raTkalSkalTest.java
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
import hr.restart.baza.dM;

import java.util.HashMap;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raTkalSkalTest {

  private dM dm = dM.getDataModule();
  private raControlDocs rCD = new raControlDocs();
  private String key="";

  public void init(){
    dm.getStdoki().open();
  }
  public static void main(String[] args) {


    raTkalSkalTest rTST = new raTkalSkalTest();
    rTST.init();
    rTST.popraviStdoki();
    String meskla_sql = "SELECT stdoki.cskl as izlcskl,stdoki.vrdok as izlvrdok,stdoki.god as izlgod,stdoki.brdok as izlbrdok,stdoki.rbr as izlrbr,"+
                        "stdoki.nc as izl_nc,stdoki.vc as izl_vc,stdoki.mc as izl_mc,stdoki.zc as izl_zc,"+
                        "stmeskla.nc as ul_nc,stmeskla.vc as ul_vc,stmeskla.mc as ul_mc,stmeskla.zc as ul_zc "+
                        "FROM stdoki,stmeskla WHERE stdoki.itkal = "+
                        "stmeskla.cskliz||'-'||stmeskla.csklul||'-'||stmeskla.vrdok||'-'||stmeskla.god"+
                        "||'-'||stmeskla.brdok||'-'||stmeskla.rbsid||'-' and stmeskla.mc != stdoki.mc";
//    System.out.println(meskla_sql);
    rTST.rekalkuliraj(meskla_sql);
    String prim_sql = "SELECT stdoki.cskl as izlcskl,stdoki.vrdok as izlvrdok,stdoki.god as izlgod,stdoki.brdok as izlbrdok,stdoki.rbr as izlrbr,"+
                        "stdoki.nc as izl_nc,stdoki.vc as izl_vc,stdoki.mc as izl_mc,stdoki.zc as izl_zc,"+
                        "stdoku.nc as ul_nc,stdoku.vc as ul_vc,stdoku.mc as ul_mc,stdoku.zc as ul_zc "+
                        "FROM stdoki,stdoku WHERE stdoki.itkal = "+
                        "stdoku.cskl||'-'||stdoku.vrdok||'-'||stdoku.god"+
                        "||'-'||stdoku.brdok||'-'||stdoku.rbsid||'-' and stdoku.mc != stdoki.mc";
//    System.out.println(prim_sql);
    rTST.rekalkuliraj(prim_sql);

System.out.println("Kraj");
System.exit(0);

  }

  public raTkalSkalTest() {
  }

  public boolean controlAndRepareTkalSkal(String namefield,DataSet qds){

// kontrole
    if (qds.hasColumn(namefield) == null) throw new RuntimeException("Ne valja polje "+namefield + "za zadani DataSet");

    HashMap hm = new HashMap();
    String realSqlOne = "";
    key = qds.getString(namefield);
    if (key.equalsIgnoreCase("") || key == null) return false;
    hm = rCD.getHashMapKey(key);
    if (hm==null) {
      qds.setString(namefield,"");
      return false;
    }
    if (TypeDoc.getTypeDoc().isDocMeskla((String) hm.get("VRDOK"))){
      realSqlOne = rCD.UniversalKeyToSqlKey("stmeskla",
          new String[] {"cskliz","csklul","vrdok","god","brdok","rbsid"},
          new String[] {(String) hm.get("CSKLIZ"),(String) hm.get("CSKLUL"),
                        (String) hm.get("VRDOK"),(String) hm.get("GOD"),
                        (String) hm.get("BRDOK"),(String) hm.get("RBRSID")});
    } else {
      realSqlOne = rCD.UniversalKeyToSqlKey("stdoku",
          new String[] {"cskl","vrdok","god","brdok","rbsid"},
          new String[] {(String) hm.get("CSKL"),(String) hm.get("VRDOK"),
                        (String) hm.get("GOD"),(String) hm.get("BRDOK"),
                        (String) hm.get("RBRSID")});
      }
      qds.setString(namefield,realSqlOne);
      realSqlOne ="";
      return true;
  }


  public void popraviStdoki(){
    dm.getStdoki().open();
    for (dm.getStdoki().first();dm.getStdoki().inBounds();dm.getStdoki().next()){
      controlAndRepareTkalSkal("ITKAL",dm.getStdoki());
      controlAndRepareTkalSkal("SITKAL",dm.getStdoki());
    }
    dm.getStdoki().saveChanges();
  }

  public void rekalkuliraj(String query){
    String newsql="";
    QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(query,true);
    if (qds.getRowCount()==0) {
      System.out.println("nema greske");
      return;
    }
//System.out.println("Ima gresaka "+qds.getRowCount());
    for (qds.first();qds.inBounds();qds.next()){
      newsql = "update stdoki set nc="+qds.getBigDecimal("ul_nc")+", vc ="+qds.getBigDecimal("ul_vc")+
        ", mc="+qds.getBigDecimal("ul_mc")+" where cskl='"+qds.getString("izlcskl")+
        "' and vrdok='"+qds.getString("izlvrdok")+"' and god='"+qds.getString("izlgod")+"' and brdok="+
        qds.getInt("izlbrdok")+" and rbr="+qds.getShort("izlrbr");
      try {
        hr.restart.util.raTransaction.runSQL(newsql,dm.getDatabaseConnection());
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}