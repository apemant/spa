/****license*****************************************************************
**   file: Kupci.java
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

public class Kupci extends KreirDrop implements DataModule {

  //ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Kupci Kupciclass;
  //dM dm  = dM.getDataModule();
  QueryDataSet kupci = new raDataSet();
  QueryDataSet kupciaktiv = new raDataSet();
  /*Column kupciLOKK = new Column();
  Column kupciAKTIV = new Column();
  Column kupciCKUPAC = new Column();
  Column kupciIME = new Column();
  Column kupciPREZIME = new Column();
  Column kupciJMBG = new Column();
  Column kupciADR = new Column();
  Column kupciPBR = new Column();
  Column kupciMJ = new Column();
  Column kupciTEL = new Column();
  Column kupciEMADR = new Column();*/

  public static Kupci getDataModule() {
    if (Kupciclass == null) {
      Kupciclass = new Kupci();
    }
    return Kupciclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return kupci;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return kupciaktiv;
  }

  public Kupci() {
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
    
    /*kupciEMADR.setCaption("E-mail");
    kupciEMADR.setColumnName("EMADR");
    kupciEMADR.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciEMADR.setPrecision(30);
    kupciEMADR.setTableName("KUPCI");
    kupciEMADR.setServerColumnName("EMADR");
    kupciEMADR.setSqlType(1);
    kupciTEL.setCaption("Telefon");
    kupciTEL.setColumnName("TEL");
    kupciTEL.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciTEL.setPrecision(20);
    kupciTEL.setTableName("KUPCI");
    kupciTEL.setWidth(12);
    kupciTEL.setServerColumnName("TEL");
    kupciTEL.setSqlType(1);
    kupciMJ.setCaption("Mjesto");
    kupciMJ.setColumnName("MJ");
    kupciMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciMJ.setPrecision(30);
    kupciMJ.setTableName("KUPCI");
    kupciMJ.setServerColumnName("MJ");
    kupciMJ.setSqlType(1);
    kupciPBR.setCaption("Poštanski broj");
    kupciPBR.setColumnName("PBR");
    kupciPBR.setDataType(com.borland.dx.dataset.Variant.INT);
    kupciPBR.setPrecision(5);
    kupciPBR.setTableName("KUPCI");
    kupciPBR.setWidth(5);
    kupciPBR.setServerColumnName("PBR");
    kupciPBR.setSqlType(4);
    kupciADR.setCaption("Adresa");
    kupciADR.setColumnName("ADR");
    kupciADR.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciADR.setPrecision(40);
    kupciADR.setTableName("KUPCI");
    kupciADR.setWidth(25);
    kupciADR.setServerColumnName("ADR");
    kupciADR.setSqlType(1);
    kupciIME.setCaption("Ime");
    kupciIME.setColumnName("IME");
    kupciIME.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciIME.setPrecision(50);
    kupciIME.setTableName("KUPCI");
    kupciIME.setWidth(10);
    kupciIME.setServerColumnName("IME");
    kupciIME.setSqlType(1);
    kupciPREZIME.setCaption("Prezime");
    kupciPREZIME.setColumnName("PREZIME");
    kupciPREZIME.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciPREZIME.setPrecision(50);
    kupciPREZIME.setTableName("KUPCI");
    kupciPREZIME.setWidth(20);
    kupciPREZIME.setServerColumnName("PREZIME");
    kupciPREZIME.setSqlType(1);
    kupciCKUPAC.setCaption("\u0160ifra");
    kupciCKUPAC.setColumnName("CKUPAC");
    kupciCKUPAC.setDataType(com.borland.dx.dataset.Variant.INT);
    kupciCKUPAC.setPrecision(6);
    kupciCKUPAC.setRowId(true);
    kupciCKUPAC.setTableName("KUPCI");
    kupciCKUPAC.setWidth(5);
    kupciCKUPAC.setServerColumnName("CKUPAC");
    kupciCKUPAC.setSqlType(4);
    kupciJMBG.setCaption("JMBG");
    kupciJMBG.setColumnName("JMBG");
    kupciJMBG.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciJMBG.setPrecision(13);
    kupciJMBG.setTableName("KUPCI");
    kupciJMBG.setServerColumnName("JMBG");
    kupciJMBG.setSqlType(1);
    kupciAKTIV.setColumnName("AKTIV");
    kupciAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciAKTIV.setDefault("D");
    kupciAKTIV.setPrecision(1);
    kupciAKTIV.setTableName("KUPCI");
    kupciAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kupciAKTIV.setServerColumnName("AKTIV");
    kupciAKTIV.setSqlType(1);
    kupciLOKK.setColumnName("LOKK");
    kupciLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kupciLOKK.setDefault("N");
    kupciLOKK.setPrecision(1);
    kupciLOKK.setTableName("KUPCI");
    kupciLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kupciLOKK.setServerColumnName("LOKK");
    kupciLOKK.setSqlType(1);

    kupci.setResolver(dm.getQresolver());
    kupci.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM KUPCI", null, true, Load.ALL));
 setColumns(new Column[] {kupciLOKK, kupciAKTIV, kupciCKUPAC, kupciIME, kupciPREZIME, kupciJMBG, kupciADR, kupciPBR, kupciMJ, kupciTEL, kupciEMADR});
 */
    createFilteredDataSet(kupciaktiv, "aktiv='D'");
  }

   //public void setall(){

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
    /*ddl.create("kupci")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("ckupac", 6, true)
       .addChar("ime", 50)
       .addChar("prezime", 50)
       .addChar("jmbg", 13)
       .addChar("adr", 40)
       .addInteger("pbr", 5)
       .addChar("mj", 30)
       .addChar("tel", 20)
       .addChar("emadr", 30)
       .addPrimaryKey("ckupac");

    Naziv = "Kupci";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

   }*/
}