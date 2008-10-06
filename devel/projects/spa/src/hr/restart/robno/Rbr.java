/****license*****************************************************************
**   file: Rbr.java
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
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class Rbr {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private QueryDataSet QDSSTAVKE = new QueryDataSet();
  String queryString;
  private static Rbr rbr;

  public static Rbr getRbr(){
    if (rbr == null) {
      rbr = new Rbr();
    }
    return rbr;
  }
  allSelect aSS = new allSelect();

  public short vrati_rbr(String tabela,String uvjet) {
    queryString = aSS.getQuery4RbrCD4vrati_rbr(tabela,uvjet);
    return Prepare();
  }
  public int vrati_rbr(String tabela,String field,String uvjet) {
    queryString = aSS.getQuery4RbrCD4vrati_rbr(tabela,field,uvjet);
    return PrepareI();
  }

  public short vrati_rbr(String tabela,String skladiste,String vrdok,String god,int brdok) {
    queryString = aSS.getQuery4RbrCD4vrati_rbr(tabela,skladiste,vrdok,god,brdok);
    return Prepare();
  }

  private int PrepareI(){
/*
    prepprep();
    if (QDSSTAVKE.isEmpty())
      return (int) 1;
    else {
      return (int)(QDSSTAVKE.getInt("mrbr")+1);
    }
*/
    return PrepareI("mrbr");
  }
  private int PrepareI(String fild){
    prepprep();
    if (QDSSTAVKE.isEmpty())
      return (int) 1;
    else {
      return (int)(QDSSTAVKE.getInt(fild)+1);
    }
  }

  private void prepprep() {
    if (QDSSTAVKE.isOpen())
      QDSSTAVKE.close();
    QDSSTAVKE.setQuery(new QueryDescriptor(dm.getDatabase1(),queryString, null, true, Load.ALL));
    QDSSTAVKE.executeQuery();
  }
  private short Prepare(){
    prepprep();
    if (QDSSTAVKE.isEmpty())
      return (short) 1;
    else {
      return (short)(QDSSTAVKE.getShort("mrbr")+1);
    }
  }

  public short vrati_rbr_mes(String tabela,String skladiz,String skladul,String vrdok,String god,int brdok) {
    queryString = aSS.getQuery4RbrCD4vrati_rbr_mes(tabela,skladiz,skladul,vrdok,god,brdok);
    return Prepare();
  }

  public int getRbsID(DataSet qds) {

    String queryString_part1 = "";
    String queryString_part2 = "and vrdok='"+qds.getString("VRDOK")+
                               "' and god='"+qds.getString("GOD")+
                               "' and brdok="+qds.getInt("BRDOK");
    queryString = "select max(rbsid) as smaxx from "+qds.getTableName()+" where ";
    if (qds.getTableName().equalsIgnoreCase("stdoku") || qds.getTableName().equalsIgnoreCase("stdoki")){
      queryString_part1 = "cskl='"+qds.getString("CSKL")+"' ";
    }
    else if (qds.getTableName().equalsIgnoreCase("stmeskla")){
      queryString_part1 = "csklul='"+qds.getString("CSKLUL")+
                          "' and cskliz = '"+qds.getString("CSKLIZ")+"' ";
    }
    else {
      return -1;
    }
    queryString = queryString+ queryString_part1+queryString_part2;
//    System.out.println(queryString);
//    System.out.println(PrepareI("smaxx"));
    return PrepareI("smaxx");
  }

}

