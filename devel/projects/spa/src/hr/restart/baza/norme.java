/****license*****************************************************************
**   file: norme.java
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
package hr.restart.baza;
import java.util.ResourceBundle;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class norme extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static norme normeclass;
  dM dm  = dM.getDataModule();
  QueryDataSet norme = new raDataSet();
  QueryDataSet normesorted = new raDataSet();
  Column normeLOKK = new Column();
  Column normeAKTIV = new Column();
  Column normeCARTNOR = new Column();
  Column normeCART = new Column();
  Column normeCART1 = new Column();
  Column normeBC = new Column();
  Column normeNAZART = new Column();
  Column normeJM = new Column();
  Column normeKOL = new Column();
  public static norme getDataModule() {
    if (normeclass == null) {
      normeclass = new norme();
    }
    return normeclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return norme;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getSorted() {
    return normesorted;
  }

  public norme() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    normeKOL.setCaption(dmRes.getString("normeKOL_caption"));
    normeKOL.setColumnName("KOL");
    normeKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    normeKOL.setDisplayMask("###,###,##0.000000");
    normeKOL.setDefault("0");
    normeKOL.setPrecision(15);
    normeKOL.setScale(6);
    normeKOL.setTableName("NORME");
    normeKOL.setWidth(10);
    normeKOL.setSqlType(2);
    normeKOL.setServerColumnName("KOL");
    normeJM.setCaption("JM");
    normeJM.setColumnName("JM");
    normeJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    normeJM.setPrecision(3);
    normeJM.setTableName("NORME");
    normeJM.setSqlType(1);
    normeJM.setServerColumnName("JM");
    normeNAZART.setCaption(dmRes.getString("normeNAZART_caption"));
    normeNAZART.setColumnName("NAZART");
    normeNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    normeNAZART.setPrecision(50);
    normeNAZART.setTableName("NORME");
    normeNAZART.setWidth(30);
    normeNAZART.setSqlType(1);
    normeNAZART.setServerColumnName("NAZART");
    normeBC.setCaption("Barcode");
    normeBC.setColumnName("BC");
    normeBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    normeBC.setPrecision(20);
    normeBC.setTableName("NORME");
    normeBC.setWidth(12);
    normeBC.setServerColumnName("BC");
    normeBC.setSqlType(1);
    normeCART1.setCaption("Oznaka");
    normeCART1.setColumnName("CART1");
    normeCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    normeCART1.setPrecision(20);
    normeCART1.setTableName("NORME");
    normeCART1.setWidth(11);
    normeCART1.setServerColumnName("CART1");
    normeCART1.setSqlType(1);
    normeCART.setCaption("Šifra");
    normeCART.setColumnName("CART");
    normeCART.setDataType(com.borland.dx.dataset.Variant.INT);
    normeCART.setRowId(true);
    normeCART.setTableName("NORME");
    normeCART.setWidth(5);
    normeCART.setSqlType(4);
    normeCART.setServerColumnName("CART");
    normeCARTNOR.setCaption("Normativ");
    normeCARTNOR.setColumnName("CARTNOR");
    normeCARTNOR.setDataType(com.borland.dx.dataset.Variant.INT);
    normeCARTNOR.setRowId(true);
    normeCARTNOR.setTableName("NORME");
    normeCARTNOR.setWidth(5);
    normeCARTNOR.setSqlType(4);
    normeCARTNOR.setServerColumnName("CARTNOR");
    normeAKTIV.setCaption(dmRes.getString("normeAKTIV_caption"));
    normeAKTIV.setColumnName("AKTIV");
    normeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    normeAKTIV.setDefault("D");
    normeAKTIV.setPrecision(1);
    normeAKTIV.setTableName("NORME");
    normeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    normeAKTIV.setSqlType(1);
    normeAKTIV.setServerColumnName("AKTIV");
    normeLOKK.setCaption(dmRes.getString("normeLOKK_caption"));
    normeLOKK.setColumnName("LOKK");
    normeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    normeLOKK.setDefault("N");
    normeLOKK.setPrecision(1);
    normeLOKK.setTableName("NORME");
    normeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    normeLOKK.setSqlType(1);
    normeLOKK.setServerColumnName("LOKK");
    norme.setResolver(dm.getQresolver());
    norme.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from norme", null, true, Load.ALL));
    setColumns(new Column[] {normeLOKK, normeAKTIV, normeCARTNOR, normeCART, normeCART1, normeBC, normeNAZART, normeJM, normeKOL});
    createFilteredDataSet(normesorted, "");
    normesorted.setSort(new SortDescriptor(new String[] {"CARTNOR"}));
  }

   public void setall(){

   /* SqlDefTabela = "create table norme " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cartnor numeric(6,0) not null, "+
      "cart numeric (6,0) not null,"+
      "nazart char(50) character set win1250, " +
      "jm char(3),"+
      "kol numeric (17,3)," +
      "Primary Key (cartnor,cart))" ; */

    ddl.create("norme")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cartnor", 6, true)
       .addInteger("cart", 6, true)
       .addChar("cart1", 20, true)
       .addChar("bc", 20, true)
       .addChar("nazart", 50)
       .addChar("jm", 3)
       .addFloat("kol", 17, 6)
       .addPrimaryKey("cartnor,cart");

    Naziv="norme";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cart"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

/*
    NaziviIdx=new String[]{"inormekey" };

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on norme (cartnor,cart)"};
*/
  }
}