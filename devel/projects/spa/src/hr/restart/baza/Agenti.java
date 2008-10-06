/****license*****************************************************************
**   file: Agenti.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Agenti extends KreirDrop implements DataModule {

//  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Agenti Agenticlass;
//  dM dm  = dM.getDataModule();
  QueryDataSet agenti = new raDataSet();
  QueryDataSet agentiaktiv = new raDataSet();
  
  /*Column agentiLOKK = new Column();
  Column agentiAKTIV = new Column();
  Column agentiCAGENT = new Column();
  Column agentiNAZAGENT = new Column();
  Column agentiZAPORKA = new Column();
  Column agentiPOSTOPROV = new Column();
  Column agentiVRSTAPROV = new Column();*/

  public static Agenti getDataModule() {
    if (Agenticlass == null) {
      Agenticlass = new Agenti();
    }
    return Agenticlass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return agenti;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return agentiaktiv;
  }

  public Agenti() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    initModule();
/*
    agentiVRSTAPROV.setColumnName("VRSTAPROV");
    agentiVRSTAPROV.setDataType(com.borland.dx.dataset.Variant.STRING);
    agentiVRSTAPROV.setDefault("F");
    agentiVRSTAPROV.setPrecision(1);
    agentiVRSTAPROV.setTableName("AGENTI");
    agentiVRSTAPROV.setServerColumnName("VRSTAPROV");
    agentiVRSTAPROV.setSqlType(1);

    agentiPOSTOPROV.setCaption("Provizija");
    agentiPOSTOPROV.setColumnName("POSTOPROV");
    agentiPOSTOPROV.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    agentiPOSTOPROV.setDisplayMask("#,##0.00");
    agentiPOSTOPROV.setDefault("0");
    agentiPOSTOPROV.setPrecision(10);
    agentiPOSTOPROV.setScale(2);
    agentiPOSTOPROV.setTableName("AGENTI");
    agentiPOSTOPROV.setSqlType(2);
    agentiPOSTOPROV.setServerColumnName("POSTOPROV");

      agentiNAZAGENT.setCaption("Ime i prezime");
    agentiNAZAGENT.setColumnName("NAZAGENT");
    agentiNAZAGENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    agentiNAZAGENT.setPrecision(50);
    agentiNAZAGENT.setTableName("AGENTI");
    agentiNAZAGENT.setWidth(30);
    agentiNAZAGENT.setServerColumnName("NAZAGENT");
    agentiNAZAGENT.setSqlType(1);
    agentiCAGENT.setCaption("\u0160ifra");
    agentiCAGENT.setColumnName("CAGENT");
    agentiCAGENT.setDataType(com.borland.dx.dataset.Variant.INT);
    agentiCAGENT.setRowId(true);
    agentiCAGENT.setTableName("AGENTI");
    agentiCAGENT.setWidth(5);
    agentiCAGENT.setServerColumnName("CAGENT");
    agentiCAGENT.setSqlType(4);
    agentiZAPORKA.setColumnName("ZAPORKA");
    agentiZAPORKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    agentiZAPORKA.setPrecision(30);
    agentiZAPORKA.setTableName("AGENTI");
    agentiZAPORKA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    agentiZAPORKA.setServerColumnName("ZAPORKA");
    agentiZAPORKA.setSqlType(1);
    agentiAKTIV.setColumnName("AKTIV");
    agentiAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    agentiAKTIV.setDefault("D");
    agentiAKTIV.setPrecision(1);
    agentiAKTIV.setTableName("AGENTI");
    agentiAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    agentiAKTIV.setServerColumnName("AKTIV");
    agentiAKTIV.setSqlType(1);
    agentiLOKK.setColumnName("LOKK");
    agentiLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    agentiLOKK.setDefault("N");
    agentiLOKK.setPrecision(1);
    agentiLOKK.setTableName("AGENTI");
    agentiLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    agentiLOKK.setServerColumnName("LOKK");
    agentiLOKK.setSqlType(1);

    agenti.setResolver(dm.getQresolver());
    agenti.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM AGENTI", null, true, Load.ALL));
 setColumns(new Column[] {agentiLOKK, agentiAKTIV, agentiCAGENT, agentiNAZAGENT, agentiZAPORKA, agentiPOSTOPROV, agentiVRSTAPROV});
*/
    createFilteredDataSet(agentiaktiv, "aktiv = 'D'");
  }

   /*public void setall(){*/

    /*SqlDefTabela = "create table Agenti " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " +
      "cagent numeric(6,0) not null,"+
      "nazagent char(50) CHARACTER SET WIN1250," +
      "zaporka char(30) CHARACTER SET WIN1250 , " +
      "Primary Key (cagent))" ;

    Naziv="Agenti";

    NaziviIdx=new String[]{"ilokkagent","iaktivagent","icagent"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Agenti (lokk)",
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Agenti (aktiv)",
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Agenti (cagent)" };
      */
    /*ddl.create("agenti")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cagent", 6, true)
       .addChar("nazagent", 50)
       .addChar("zaporka", 30)
       .addFloat("postoprov", 6, 2)
       .addChar("vrstaprov", 1, "F")
       .addPrimaryKey("cagent");

    Naziv = "Agenti";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

   }*/
}

