/****license*****************************************************************
**   file: dob_art.java
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
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class dob_art extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static dob_art dob_artclass;
  dM dm  = dM.getDataModule();
  QueryDataSet dob_art = new raDataSet();
  Column dob_artLOKK = new Column();
  Column dob_artAKTIV = new Column();
  Column dob_artCPAR = new Column();
  Column dob_artCART = new Column();
  Column dob_artCART1 = new Column();
  Column dob_artBC = new Column();
  Column dob_artNAZART = new Column();
  Column dob_artDC = new Column();
  Column dob_artPRAB = new Column();
  Column dob_artNAZPAK = new Column();
  Column dob_artJMPAK = new Column();
  Column dob_artBRJED = new Column();
  Column dob_artTEZPAK = new Column();
  public static dob_art getDataModule() {
    if (dob_artclass == null) {
      dob_artclass = new dob_art();
    }
    return dob_artclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return dob_art;
  }
  public dob_art() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dob_artTEZPAK.setCaption(dmRes.getString("dob_artTEZPAK_caption"));
    dob_artTEZPAK.setColumnName("TEZPAK");
    dob_artTEZPAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dob_artTEZPAK.setDisplayMask("###,###,##0.000");
    dob_artTEZPAK.setDefault("0");
    dob_artTEZPAK.setPrecision(15);
    dob_artTEZPAK.setScale(3);
    dob_artTEZPAK.setTableName("DOB_ART");
    dob_artTEZPAK.setSqlType(2);
    dob_artTEZPAK.setServerColumnName("TEZPAK");
    dob_artBRJED.setCaption(dmRes.getString("dob_artBRJED_caption"));
    dob_artBRJED.setColumnName("BRJED");
    dob_artBRJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dob_artBRJED.setDisplayMask("###,###,##0.000");
    dob_artBRJED.setDefault("0");
    dob_artBRJED.setPrecision(15);
    dob_artBRJED.setScale(3);
    dob_artBRJED.setTableName("DOB_ART");
    dob_artBRJED.setSqlType(2);
    dob_artBRJED.setServerColumnName("BRJED");
    dob_artJMPAK.setCaption(dmRes.getString("dob_artJMPAK_caption"));
    dob_artJMPAK.setColumnName("JMPAK");
    dob_artJMPAK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dob_artJMPAK.setPrecision(3);
    dob_artJMPAK.setTableName("DOB_ART");
    dob_artJMPAK.setSqlType(1);
    dob_artJMPAK.setServerColumnName("JMPAK");
    dob_artNAZPAK.setCaption(dmRes.getString("dob_artNAZPAK_caption"));
    dob_artNAZPAK.setColumnName("NAZPAK");
    dob_artNAZPAK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dob_artNAZPAK.setPrecision(10);
    dob_artNAZPAK.setTableName("DOB_ART");
    dob_artNAZPAK.setSqlType(1);
    dob_artNAZPAK.setServerColumnName("NAZPAK");
    dob_artPRAB.setCaption(dmRes.getString("dob_artPRAB_caption"));
    dob_artPRAB.setColumnName("PRAB");
    dob_artPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dob_artPRAB.setDisplayMask("##0.00");
    dob_artPRAB.setDefault("0");
    dob_artPRAB.setPrecision(8);
    dob_artPRAB.setScale(2);
    dob_artPRAB.setTableName("DOB_ART");
    dob_artPRAB.setSqlType(2);
    dob_artPRAB.setServerColumnName("PRAB");
    dob_artDC.setCaption(dmRes.getString("dob_artDC_caption"));
    dob_artDC.setColumnName("DC");
    dob_artDC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dob_artDC.setDisplayMask("###,###,##0.00");
    dob_artDC.setDefault("0");
    dob_artDC.setPrecision(12);
    dob_artDC.setScale(2);
    dob_artDC.setTableName("DOB_ART");
    dob_artDC.setSqlType(2);
    dob_artDC.setServerColumnName("DC");
    dob_artNAZART.setCaption("Naziv");
    dob_artNAZART.setColumnName("NAZART");
    dob_artNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    dob_artNAZART.setPrecision(75);
    dob_artNAZART.setTableName("DOB_ART");
    dob_artNAZART.setWidth(30);
    dob_artNAZART.setServerColumnName("NAZART");
    dob_artNAZART.setSqlType(1);
    dob_artBC.setCaption("Barcode");
    dob_artBC.setColumnName("BC");
    dob_artBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    dob_artBC.setPrecision(20);
    dob_artBC.setTableName("DOB_ART");
    dob_artBC.setWidth(10);
    dob_artBC.setServerColumnName("BC");
    dob_artBC.setSqlType(1);
    dob_artCART1.setCaption("Oznaka");
    dob_artCART1.setColumnName("CART1");
    dob_artCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    dob_artCART1.setPrecision(20);
    dob_artCART1.setTableName("DOB_ART");
    dob_artCART1.setWidth(11);
    dob_artCART1.setServerColumnName("CART1");
    dob_artCART1.setSqlType(1);
    dob_artCART.setCaption(dmRes.getString("dob_artCART_caption1"));
    dob_artCART.setColumnName("CART");
    dob_artCART.setDataType(com.borland.dx.dataset.Variant.INT);
    dob_artCART.setRowId(true);
    dob_artCART.setTableName("DOB_ART");
    dob_artCART.setSqlType(4);
    dob_artCART.setServerColumnName("CART");
    dob_artCART.setWidth(6);
    dob_artCPAR.setCaption(dmRes.getString("dob_artCPAR_caption"));
    dob_artCPAR.setColumnName("CPAR");
    dob_artCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    dob_artCPAR.setRowId(true);
    dob_artCPAR.setTableName("DOB_ART");
    dob_artCPAR.setVisible(com.borland.jb.util.TriStateProperty.DEFAULT);
    dob_artCPAR.setWidth(5);
    dob_artCPAR.setSqlType(4);
    dob_artCPAR.setServerColumnName("CPAR");
    dob_artAKTIV.setCaption(dmRes.getString("dob_artAKTIV_caption"));
    dob_artAKTIV.setColumnName("AKTIV");
    dob_artAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    dob_artAKTIV.setDefault("D");
    dob_artAKTIV.setPrecision(1);
    dob_artAKTIV.setTableName("DOB_ART");
    dob_artAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dob_artAKTIV.setSqlType(1);
    dob_artAKTIV.setServerColumnName("AKTIV");
    dob_artLOKK.setCaption(dmRes.getString("dob_artLOKK_caption"));
    dob_artLOKK.setColumnName("LOKK");
    dob_artLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dob_artLOKK.setDefault("N");
    dob_artLOKK.setPrecision(1);
    dob_artLOKK.setTableName("DOB_ART");
    dob_artLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dob_artLOKK.setSqlType(1);
    dob_artLOKK.setServerColumnName("LOKK");
    dob_art.setResolver(dm.getQresolver());
    dob_art.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from dob_art", null, true, Load.ALL));
 setColumns(new Column[] {dob_artLOKK, dob_artAKTIV, dob_artCPAR, dob_artCART, dob_artCART1, dob_artBC, dob_artNAZART, dob_artDC, dob_artPRAB, dob_artNAZPAK, dob_artJMPAK,
        dob_artBRJED, dob_artTEZPAK});

  }

  public void setall(){

    /*SqlDefTabela = "create table dob_art " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cpar  numeric(6,0) not null,"+
      "cart  decimal(6,0) not null,"+
      "dc numeric(12,2)  ,"+
      "prab numeric(6,2) ,"+
      "nazpak char(10) character set win1250,"+
      "jmpak char(3),"+
      "brjed numeric(12,3),"+
      "tezpak numeric(12,3),"+
      "Primary Key (cpar,cart))" ;
*/
    ddl.create("dob_art")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cpar", 6, true)
       .addInteger("cart", 6, true)
       .addChar("cart1", 20)
       .addChar("bc", 20)
       .addChar("nazart", 75)
       .addFloat("dc", 12, 2)
       .addFloat("prab", 6, 2)
       .addChar("nazpak", 10)
       .addChar("jmpak", 3)
       .addFloat("brjed", 12, 3)
       .addFloat("tezpak", 12, 3)
       .addPrimaryKey("cpar,cart");

    Naziv="dob_art";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"idob_artkey on dob_art (cpar,cart)" ,
              CommonTable.SqlDefIndex+"idob_artcpar on dob_art (cpar)",
              CommonTable.SqlDefIndex+"idob_artcart on dob_art (cart)"};

    NaziviIdx=new String[]{"idob_artkey","idob_artcpar","idob_artcart" };
*/
  }
}