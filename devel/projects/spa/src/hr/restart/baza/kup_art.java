/****license*****************************************************************
**   file: kup_art.java
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

public class kup_art extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static kup_art kup_artclass;
  dM dm  = dM.getDataModule();
  QueryDataSet kup_art = new raDataSet();
  Column kup_artLOKK = new Column();
  Column kup_artAKTIV = new Column();
  Column kup_artCPAR = new Column();
  Column kup_artCART = new Column();
  Column kup_artCART1 = new Column();
  Column kup_artBC = new Column();
  Column kup_artNAZART = new Column();
  Column kup_artCDOB = new Column();
  Column kup_artCGRART = new Column();
  Column kup_artCSHRAB = new Column();
  Column kup_artVC = new Column();
  Column kup_artPRAB = new Column();
  Column kup_artNAZPAK = new Column();
  Column kup_artJMPAK = new Column();
  Column kup_artBRJAD = new Column();
  Column kup_artTEZPAK = new Column();

  public static kup_art getDataModule() {
    if (kup_artclass == null) {
      kup_artclass = new kup_art();
    }
    return kup_artclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return kup_art;
  }
  public kup_art() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    kup_artCDOB.setCaption("Dobavlja\u010D");
    kup_artCDOB.setColumnName("CDOB");
    kup_artCDOB.setDataType(com.borland.dx.dataset.Variant.INT);
    kup_artCDOB.setTableName("KUP_ART");
    kup_artCDOB.setSqlType(4);
    kup_artCDOB.setServerColumnName("CDOB");
    kup_artCSHRAB.setCaption("Shema rabata");
    kup_artCSHRAB.setColumnName("CSHRAB");
    kup_artCSHRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artCSHRAB.setPrecision(3);
    kup_artCSHRAB.setTableName("KUP_ART");
    kup_artCSHRAB.setSqlType(1);
    kup_artCSHRAB.setServerColumnName("CSHRAB");
    kup_artCGRART.setCaption("Grupa artikla");
    kup_artCGRART.setColumnName("CGRART");
    kup_artCGRART.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artCGRART.setPrecision(10);
    kup_artCGRART.setTableName("KUP_ART");
    kup_artCGRART.setSqlType(1);
    kup_artCGRART.setServerColumnName("CGRART");
    kup_artNAZART.setCaption("Naziv");
    kup_artNAZART.setColumnName("NAZART");
    kup_artNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artNAZART.setPrecision(50);
    kup_artNAZART.setTableName("KUP_ART");
    kup_artNAZART.setWidth(20);
    kup_artNAZART.setServerColumnName("NAZART");
    kup_artNAZART.setSqlType(1);
    kup_artBC.setCaption("Barcode");
    kup_artBC.setColumnName("BC");
    kup_artBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artBC.setPrecision(20);
    kup_artBC.setTableName("KUP_ART");
    kup_artBC.setWidth(10);
    kup_artBC.setServerColumnName("BC");
    kup_artBC.setSqlType(1);
    kup_artCART1.setCaption("Oznaka");
    kup_artCART1.setColumnName("CART1");
    kup_artCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artCART1.setPrecision(20);
    kup_artCART1.setTableName("KUP_ART");
    kup_artCART1.setWidth(11);
    kup_artCART1.setServerColumnName("CART1");
    kup_artCART1.setSqlType(1);
    kup_artCART.setCaption("Šifra");
    kup_artCART.setColumnName("CART");
    kup_artCART.setDataType(com.borland.dx.dataset.Variant.INT);
    kup_artCART.setRowId(true);
    kup_artCART.setTableName("KUP_ART");
    kup_artCART.setSqlType(4);
    kup_artCART.setServerColumnName("CART");
    kup_artCPAR.setCaption("Partner");
    kup_artCPAR.setColumnName("CPAR");
    kup_artCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    kup_artCPAR.setRowId(true);
    kup_artCPAR.setTableName("KUP_ART");
    kup_artCPAR.setSqlType(4);
    kup_artCPAR.setServerColumnName("CPAR");
    kup_artTEZPAK.setCaption("Težina pakiranja");
    kup_artTEZPAK.setColumnName("TEZPAK");
    kup_artTEZPAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kup_artTEZPAK.setDisplayMask("###,###,##0.00");
    kup_artTEZPAK.setDefault("0");
    kup_artTEZPAK.setPrecision(15);
    kup_artTEZPAK.setScale(3);
    kup_artTEZPAK.setTableName("KUP_ART");
    kup_artTEZPAK.setSqlType(2);
    kup_artTEZPAK.setServerColumnName("TEZPAK");
    kup_artBRJAD.setCaption("Broj jedinica");
    kup_artBRJAD.setColumnName("BRJED");
    kup_artBRJAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kup_artBRJAD.setDisplayMask("###,###,##0.000");
    kup_artBRJAD.setDefault("0");
    kup_artBRJAD.setPrecision(15);
    kup_artBRJAD.setScale(3);
    kup_artBRJAD.setTableName("KUP_ART");
    kup_artBRJAD.setSqlType(2);
    kup_artBRJAD.setServerColumnName("BRJED");
    kup_artJMPAK.setCaption("JM pakiranja");
    kup_artJMPAK.setColumnName("JMPAK");
    kup_artJMPAK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artJMPAK.setPrecision(3);
    kup_artJMPAK.setTableName("KUP_ART");
    kup_artJMPAK.setSqlType(1);
    kup_artJMPAK.setServerColumnName("JMPAK");
    kup_artNAZPAK.setCaption("Naziv pakiranja");
    kup_artNAZPAK.setColumnName("NAZPAK");
    kup_artNAZPAK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artNAZPAK.setPrecision(10);
    kup_artNAZPAK.setTableName("KUP_ART");
    kup_artNAZPAK.setSqlType(1);
    kup_artNAZPAK.setServerColumnName("NAZPAK");
    kup_artPRAB.setCaption("Posto rabata");
    kup_artPRAB.setColumnName("PRAB");
    kup_artPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kup_artPRAB.setDisplayMask("###,###,##0.00");
    kup_artPRAB.setDefault("0");
    kup_artPRAB.setPrecision(10);
    kup_artPRAB.setScale(2);
    kup_artPRAB.setTableName("KUP_ART");
    kup_artPRAB.setSqlType(2);
    kup_artPRAB.setServerColumnName("PRAB");
    kup_artVC.setCaption("Cijena");
    kup_artVC.setColumnName("VC");
    kup_artVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kup_artVC.setDisplayMask("###,###,##0.00");
    kup_artVC.setDefault("0");
    kup_artVC.setPrecision(15);
    kup_artVC.setScale(2);
    kup_artVC.setTableName("KUP_ART");
    kup_artVC.setSqlType(2);
    kup_artVC.setServerColumnName("VC");
    kup_artAKTIV.setColumnName("AKTIV");
    kup_artAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artAKTIV.setDefault("D");
    kup_artAKTIV.setPrecision(1);
    kup_artAKTIV.setTableName("KUP_ART");
    kup_artAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kup_artAKTIV.setSqlType(1);
    kup_artAKTIV.setServerColumnName("AKTIV");
    kup_artLOKK.setColumnName("LOKK");
    kup_artLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kup_artLOKK.setDefault("N");
    kup_artLOKK.setPrecision(1);
    kup_artLOKK.setTableName("KUP_ART");
    kup_artLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kup_artLOKK.setSqlType(1);
    kup_artLOKK.setServerColumnName("LOKK");
    kup_art.setResolver(dm.getQresolver());
    kup_art.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from kup_art", null, true, Load.ALL));
 setColumns(new Column[] {kup_artLOKK, kup_artAKTIV, kup_artCPAR, kup_artCART, kup_artCART1, kup_artBC, kup_artNAZART, kup_artCDOB, kup_artCGRART, kup_artCSHRAB, kup_artVC, kup_artPRAB,
        kup_artNAZPAK, kup_artJMPAK, kup_artBRJAD, kup_artTEZPAK});
  }
 public void setall(){

    /*SqlDefTabela = "create table kup_art " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cpar  numeric(6,0) not null,"+
      "cart  decimal(6,0) not null,"+
      "cgrart char(10) CHARACTER SET WIN1250,"+
      "cshrab char(3) , "+
      "vc numeric (17,2) ,"+
      "prab numeric(6,2),"+
      "nazpak char(10) character set win1250,"+
      "jmpak char(3),"+
      "brjed numeric(12,3),"+
      "tezpak numeric(12,3),"+
      "Primary Key (cpar,cart))" ;*/

    ddl.create("kup_art")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cpar", 6, true)
       .addInteger("cart", 6, true)
       .addChar("cart1", 20)
       .addChar("bc", 20)
       .addChar("nazart", 50)
       .addInteger("cdob", 6)
       .addChar("cgrart", 10)
       .addChar("cshrab", 3)
       .addFloat("vc", 17, 2)
       .addFloat("prab", 6, 2)
       .addChar("nazpak", 10)
       .addChar("jmpak", 3)
       .addFloat("brjed", 12, 3)
       .addFloat("tezpak", 12, 3)
       .addPrimaryKey("cpar,cart");

    Naziv="kup_art";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cart"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"ikup_artkey on kup_art (cpar,cart)" ,
              CommonTable.SqlDefIndex+"ikup_artcpar on kup_art (cpar)",
              CommonTable.SqlDefIndex+"ikup_artcart on kup_art (cart)"};

    NaziviIdx=new String[]{"ikup_artkey","ikup_artcpar","ikup_artcart" };
*/
  }


}