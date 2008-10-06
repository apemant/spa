/****license*****************************************************************
**   file: Porezi.java
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
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Porezi extends KreirDrop  implements DataModule {

  private static Porezi poreziclass;
  dM dm  = dM.getDataModule();
  QueryDataSet porezi = new raDataSet();
  QueryDataSet poreziaktiv = new raDataSet();
  Column poreziLOKK = new Column();
  Column poreziAKTIV = new Column();
  Column poreziCPOR = new Column();
  Column poreziNAZPOR = new Column();
  Column poreziPOR1 = new Column();
  Column poreziNAZPOR1 = new Column();
  Column poreziPOR2 = new Column();
  Column poreziNAZPOR2 = new Column();
  Column poreziPOR3 = new Column();
  Column poreziNAZPOR3 = new Column();
  Column poreziUKUPOR = new Column();
  Column poreziUNPOR1 = new Column();
  Column poreziUNPOR2 = new Column();
  Column poreziUNPOR3 = new Column();
  Column poreziUKUNPOR = new Column();
  Column poreziPORNAPOR1 = new Column();
  Column poreziPORNAPOR2 = new Column();
  Column poreziPORNAPOR3 = new Column();

  public static Porezi getDataModule() {
    if (poreziclass == null) {
      poreziclass = new Porezi();
    }
    return poreziclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return porezi;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return poreziaktiv;
  }

  public Porezi(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    poreziNAZPOR3.setCaption("Naziv tre\u0107eg poreza");
    poreziNAZPOR3.setColumnName("NAZPOR3");
    poreziNAZPOR3.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziNAZPOR3.setPrecision(50);
    poreziNAZPOR3.setTableName("POREZI");
    poreziNAZPOR3.setSqlType(1);
    poreziNAZPOR3.setServerColumnName("NAZPOR3");
    poreziNAZPOR2.setCaption("Naziv drugog poreza");
    poreziNAZPOR2.setColumnName("NAZPOR2");
    poreziNAZPOR2.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziNAZPOR2.setPrecision(50);
    poreziNAZPOR2.setTableName("POREZI");
    poreziNAZPOR2.setSqlType(1);
    poreziNAZPOR2.setServerColumnName("NAZPOR2");
    poreziNAZPOR1.setCaption("Naziv prvog poreza");
    poreziNAZPOR1.setColumnName("NAZPOR1");
    poreziNAZPOR1.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziNAZPOR1.setPrecision(50);
    poreziNAZPOR1.setTableName("POREZI");
    poreziNAZPOR1.setSqlType(1);
    poreziNAZPOR1.setServerColumnName("NAZPOR1");

    poreziAKTIV.setColumnName("AKTIV");
    poreziAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziAKTIV.setDefault("D");
    poreziAKTIV.setPrecision(1);
    poreziAKTIV.setTableName("POREZI");
    poreziAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    poreziAKTIV.setServerColumnName("AKTIV");
    poreziAKTIV.setSqlType(1);
    poreziLOKK.setColumnName("LOKK");
    poreziLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziLOKK.setDefault("N");
    poreziLOKK.setPrecision(1);
    poreziLOKK.setTableName("POREZI");
    poreziLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    poreziLOKK.setServerColumnName("LOKK");
    poreziLOKK.setSqlType(1);
    poreziUKUNPOR.setCaption("Invertna stopa");
    poreziUKUNPOR.setColumnName("UKUNPOR");
    poreziUKUNPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    poreziUKUNPOR.setDisplayMask("#0.000000");
    poreziUKUNPOR.setDefault("0");
    poreziUKUNPOR.setPrecision(10);
    poreziUKUNPOR.setScale(6);
    poreziUKUNPOR.setTableName("POREZI");
    poreziUKUNPOR.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    poreziUKUNPOR.setServerColumnName("UKUNPOR");
    poreziUKUNPOR.setSqlType(2);
    poreziUNPOR3.setCaption("Invertna stopa 3");
    poreziUNPOR3.setColumnName("UNPOR3");
    poreziUNPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    poreziUNPOR3.setDisplayMask("#0.000000");
    poreziUNPOR3.setDefault("0");
    poreziUNPOR3.setPrecision(10);
    poreziUNPOR3.setScale(6);
    poreziUNPOR3.setTableName("POREZI");
    poreziUNPOR3.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    poreziUNPOR3.setServerColumnName("UNPOR3");
    poreziUNPOR3.setSqlType(2);
    poreziUNPOR2.setCaption("Invertna stopa 2");
    poreziUNPOR2.setColumnName("UNPOR2");
    poreziUNPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    poreziUNPOR2.setDisplayMask("#0.000000");
    poreziUNPOR2.setDefault("0");
    poreziUNPOR2.setPrecision(10);
    poreziUNPOR2.setScale(6);
    poreziUNPOR2.setTableName("POREZI");
    poreziUNPOR2.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    poreziUNPOR2.setServerColumnName("UNPOR2");
    poreziUNPOR2.setSqlType(2);
    poreziUNPOR1.setCaption("Invertna stopa 1");
    poreziUNPOR1.setColumnName("UNPOR1");
    poreziUNPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    poreziUNPOR1.setDisplayMask("#0.000000");
    poreziUNPOR1.setDefault("0");
    poreziUNPOR1.setPrecision(10);
    poreziUNPOR1.setScale(6);
    poreziUNPOR1.setTableName("POREZI");
    poreziUNPOR1.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    poreziUNPOR1.setServerColumnName("UNPOR1");
    poreziUNPOR1.setSqlType(2);
    poreziUKUPOR.setCaption("Ukupno");
    poreziUKUPOR.setColumnName("UKUPOR");
    poreziUKUPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    poreziUKUPOR.setDisplayMask("#0.00");
    poreziUKUPOR.setDefault("0");
    poreziUKUPOR.setPrecision(10);
    poreziUKUPOR.setScale(2);
    poreziUKUPOR.setTableName("POREZI");
    poreziUKUPOR.setWidth(5);
    poreziUKUPOR.setServerColumnName("UKUPOR");
    poreziUKUPOR.setSqlType(2);
    poreziPOR3.setCaption("Postotak 3");
    poreziPOR3.setColumnName("POR3");
    poreziPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    poreziPOR3.setDisplayMask("#0.00");
    poreziPOR3.setDefault("0");
    poreziPOR3.setPrecision(10);
    poreziPOR3.setScale(2);
    poreziPOR3.setTableName("POREZI");
    poreziPOR3.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    poreziPOR3.setServerColumnName("POR3");
    poreziPOR3.setSqlType(2);
    poreziPOR2.setCaption("Postotak 2");
    poreziPOR2.setColumnName("POR2");
    poreziPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    poreziPOR2.setDisplayMask("#0.00");
    poreziPOR2.setDefault("0");
    poreziPOR2.setPrecision(10);
    poreziPOR2.setScale(2);
    poreziPOR2.setTableName("POREZI");
    poreziPOR2.setServerColumnName("POR2");
    poreziPOR2.setSqlType(2);
    poreziPOR1.setCaption("Postotak 1");
    poreziPOR1.setColumnName("POR1");
    poreziPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    poreziPOR1.setDisplayMask("#0.00");
    poreziPOR1.setDefault("0");
    poreziPOR1.setPrecision(10);
    poreziPOR1.setScale(2);
    poreziPOR1.setTableName("POREZI");
    poreziPOR1.setServerColumnName("POR1");
    poreziPOR1.setSqlType(2);
    poreziNAZPOR.setCaption("Naziv");
    poreziNAZPOR.setColumnName("NAZPOR");
    poreziNAZPOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziNAZPOR.setPrecision(50);
    poreziNAZPOR.setTableName("POREZI");
    poreziNAZPOR.setWidth(30);
    poreziNAZPOR.setServerColumnName("NAZPOR");
    poreziNAZPOR.setSqlType(1);
    poreziCPOR.setCaption("\u0160ifra");
    poreziCPOR.setColumnName("CPOR");
    poreziCPOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziCPOR.setPrecision(6);
    poreziCPOR.setRowId(true);
    poreziCPOR.setTableName("POREZI");
    poreziCPOR.setWidth(5);
    poreziCPOR.setServerColumnName("CPOR");
    poreziCPOR.setSqlType(1);
    poreziPORNAPOR3.setCaption("PnP 3");
    poreziPORNAPOR3.setColumnName("PORNAPOR3");
    poreziPORNAPOR3.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziPORNAPOR3.setDefault("N");
    poreziPORNAPOR3.setPrecision(1);
    poreziPORNAPOR3.setTableName("POREZI");
    poreziPORNAPOR3.setSqlType(1);
    poreziPORNAPOR3.setServerColumnName("PORNAPOR3");
    poreziPORNAPOR2.setCaption("PnP 2");
    poreziPORNAPOR2.setColumnName("PORNAPOR2");
    poreziPORNAPOR2.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziPORNAPOR2.setDefault("N");
    poreziPORNAPOR2.setPrecision(1);
    poreziPORNAPOR2.setTableName("POREZI");
    poreziPORNAPOR2.setSqlType(1);
    poreziPORNAPOR2.setServerColumnName("PORNAPOR2");
    poreziPORNAPOR1.setCaption("PnP 1");
    poreziPORNAPOR1.setColumnName("PORNAPOR1");
    poreziPORNAPOR1.setDataType(com.borland.dx.dataset.Variant.STRING);
    poreziPORNAPOR1.setDefault("N");
    poreziPORNAPOR1.setPrecision(1);
    poreziPORNAPOR1.setTableName("POREZI");
    poreziPORNAPOR1.setSqlType(1);
    poreziPORNAPOR1.setServerColumnName("PORNAPOR1");


    porezi.setResolver(dm.getQresolver());
    porezi.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from porezi", null, true, Load.ALL));
 setColumns(new Column[] {poreziLOKK, poreziAKTIV, poreziCPOR, poreziNAZPOR, poreziPOR1, poreziNAZPOR1, poreziPOR2, poreziNAZPOR2, poreziPOR3,
        poreziNAZPOR3, poreziUKUPOR, poreziUNPOR1, poreziUNPOR2, poreziUNPOR3, poreziUKUNPOR, poreziPORNAPOR1, poreziPORNAPOR2, poreziPORNAPOR3});

    createFilteredDataSet(poreziaktiv, "aktiv = 'D'");
  }

  public void setall(){

/*    SqlDefTabela =  "create table Porezi " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
      "cpor char(6) not null,"+ //Šifra porez
      "nazpor char(50) CHARACTER SET WIN1250," +   //
      "por1 numeric(5,2) , " + // Porez 1 - npr. PDV 22 %
      "nazpor1 char(50) CHARACTER SET WIN1250," +
      "por2 numeric(5,2), "+   // Porez 2 npr. za potrosnju 3 %
      "nazpor2 char(50) CHARACTER SET WIN1250," +
      "por3 numeric(5,2) , "+  // Porez 3
      "nazpor3 char(50) CHARACTER SET WIN1250," +
      "ukupor numeric (5,2)," + // Ukupni porez
      "unpor1 numeric (9,6)," + // Porez 1 - unazad
      "unpor2 numeric (9,6)," + // Porez 2 - unazad
      "unpor3 numeric (9,6)," + // Porez 4 - unazad
      "ukunpor numeric (9,6)," + // Ukupni porez - unazad
      "pornapor1 char(1) CHARACTER SET WIN1250," +
      "pornapor2 char(1) CHARACTER SET WIN1250," +
      "pornapor3 char(1) CHARACTER SET WIN1250," +
      "Primary Key (cpor))" ; */
  ddl.create("porezi")
     .addChar("lokk", 1, "N")
     .addChar("aktiv", 1, "D")
     .addChar("cpor", 6, true)
     .addChar("nazpor", 50)
     .addFloat("por1", 5, 2)
     .addChar("nazpor1", 50)
     .addFloat("por2", 5, 2)
     .addChar("nazpor2", 50)
     .addFloat("por3", 5, 2)
     .addChar("nazpor3", 50)
     .addFloat("ukupor", 5, 2)
     .addFloat("unpor1", 9, 6)
     .addFloat("unpor2", 9, 6)
     .addFloat("unpor3", 9, 6)
     .addFloat("ukunpor", 9, 6)
     .addChar("pornapor1", 1)
     .addChar("pornapor2", 1)
     .addChar("pornapor3", 1)
     .addPrimaryKey("cpor");

  Naziv="Porezi" ;

  SqlDefTabela = ddl.getCreateTableString();

  String[] idx = new String[] {};
  String[] uidx = new String[] {};
  DefIndex = ddl.getIndices(idx, uidx);
  NaziviIdx = ddl.getIndexNames(idx, uidx);


  /*
  NaziviIdx=new String[]{"ilokkporezi","iaktivporezi","icporporezi"};

  DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Porezi (lokk)" ,
                          CommonTable.SqlDefIndex+NaziviIdx[1] +" on Porezi (aktiv)" ,
                          CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Porezi (cpor)" }; */
  }
}