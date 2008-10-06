/****license*****************************************************************
**   file: Sklad.java
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

public class Sklad extends KreirDrop implements DataModule {
  //ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Sklad Skladclass;
  dM dm  = dM.getDataModule();
  QueryDataSet sklad = new raDataSet();
  QueryDataSet skladaktiv = new raDataSet();

  /*Column skladLOKK = new Column();
  Column skladAKTIV = new Column();
  Column skladCSKL = new Column();
  Column skladNAZSKL = new Column();
  Column skladCORG = new Column();
  Column skladKNJIG = new Column();
  Column skladGODINA = new Column();
  Column skladVRSKL = new Column();
  Column skladVRZAL = new Column();
  Column skladTIPSKL = new Column();
  Column skladSTARIDATINV = new Column();
  Column skladDATINV = new Column();
  Column skladDATULDOK = new Column();
  Column skladDATKNJIZ = new Column();
  Column skladSTATINV = new Column();
  Column skladSTATRAD = new Column();
  Column skladDATOBRAC = new Column();
  Column skladNACOBR = new Column();*/
  
   public static Sklad getDataModule() {
    if (Skladclass == null) {
      Skladclass = new Sklad();
    }
    return Skladclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return sklad;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return skladaktiv;
  }

  public Sklad() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    /*skladGODINA.setCaption("Godina");
    skladGODINA.setColumnName("GODINA");
    skladGODINA.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladGODINA.setPrecision(4);
    skladGODINA.setTableName("SKLAD");
    skladGODINA.setServerColumnName("GODINA");
    skladGODINA.setSqlType(1);
    skladGODINA.setWidth(3);
    skladSTATRAD.setCaption("Stat. rada");
    skladSTATRAD.setColumnName("STATRAD");
    skladSTATRAD.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladSTATRAD.setDefault("N");
    skladSTATRAD.setPrecision(1);
    skladSTATRAD.setTableName("SKLAD");
    skladSTATRAD.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skladSTATRAD.setServerColumnName("STATRAD");
    skladSTATRAD.setSqlType(1);
    skladSTATINV.setCaption("Inventura");
    skladSTATINV.setColumnName("STATINV");
    skladSTATINV.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladSTATINV.setDefault("N");
    skladSTATINV.setPrecision(1);
    skladSTATINV.setTableName("SKLAD");
    skladSTATINV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skladSTATINV.setServerColumnName("STATINV");
    skladSTATINV.setSqlType(1);
    skladDATKNJIZ.setCaption("Datum knjiženja");
    skladDATKNJIZ.setColumnName("DATKNJIZ");
    skladDATKNJIZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skladDATKNJIZ.setDisplayMask("dd-MM-yyyy");
    skladDATKNJIZ.setEditMask("dd.MM.yyyy");
    skladDATKNJIZ.setTableName("SKLAD");
    skladDATKNJIZ.setServerColumnName("DATKNJIZ");
    skladDATKNJIZ.setSqlType(93);
    skladDATKNJIZ.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skladDATULDOK.setCaption("Datum ulaznog dokumenta");
    skladDATULDOK.setColumnName("DATULDOK");
    skladDATULDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skladDATULDOK.setDisplayMask("dd.MM.yyyy");
    skladDATULDOK.setEditMask("dd.MM.yyyy");
    skladDATULDOK.setExportDisplayMask("");
    skladDATULDOK.setTableName("SKLAD");
    skladDATULDOK.setServerColumnName("DATULDOK");
    skladDATULDOK.setSqlType(93);
    skladDATULDOK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skladDATINV.setCaption("Datum inventure");
    skladDATINV.setColumnName("DATINV");
    skladDATINV.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skladDATINV.setDisplayMask("dd.MM.yyyy");
//    skladDATINV.setEditMask("dd.MM.yyyy");
    skladDATINV.setTableName("SKLAD");
    skladDATINV.setWidth(10);
    skladDATINV.setServerColumnName("DATINV");
    skladDATINV.setSqlType(93);
    skladSTARIDATINV.setCaption("Stari datum inventure");
    skladSTARIDATINV.setColumnName("STARIDATINV");
    skladSTARIDATINV.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skladSTARIDATINV.setDisplayMask("dd.MM.yyyy");
//    skladSTARIDATINV.setEditMask("dd.MM.yyyy");
    skladSTARIDATINV.setExportDisplayMask("");
    skladSTARIDATINV.setTableName("SKLAD");
    skladSTARIDATINV.setWidth(10);
    skladSTARIDATINV.setServerColumnName("STARIDATINV");
    skladSTARIDATINV.setSqlType(93);
    skladTIPSKL.setCaption("Tip");
    skladTIPSKL.setColumnName("TIPSKL");
    skladTIPSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladTIPSKL.setDefault("R");
    skladTIPSKL.setPrecision(1);
    skladTIPSKL.setTableName("SKLAD");
    skladTIPSKL.setServerColumnName("TIPSKL");
    skladTIPSKL.setSqlType(1);
    skladTIPSKL.setWidth(2);
    skladVRZAL.setCaption("Zaliha");
    skladVRZAL.setColumnName("VRZAL");
    skladVRZAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladVRZAL.setDefault("N");
    skladVRZAL.setPrecision(1);
    skladVRZAL.setTableName("SKLAD");
    skladVRZAL.setServerColumnName("VRZAL");
    skladVRZAL.setSqlType(1);
    skladVRSKL.setCaption("Vrsta");
    skladVRSKL.setColumnName("VRSKL");
    skladVRSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladVRSKL.setDefault("S");
    skladVRSKL.setPrecision(1);
    skladVRSKL.setTableName("SKLAD");
    skladVRSKL.setServerColumnName("VRSKL");
    skladVRSKL.setSqlType(1);
    skladKNJIG.setCaption("Knjigovodstvo");
    skladKNJIG.setColumnName("KNJIG");
    skladKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladKNJIG.setPrecision(12);
    skladKNJIG.setTableName("SKLAD");
    skladKNJIG.setServerColumnName("KNJIG");
    skladKNJIG.setSqlType(1);

    skladCORG.setCaption("Organizacijska jedinica");
    skladCORG.setColumnName("CORG");
    skladCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladCORG.setPrecision(12);
    skladCORG.setTableName("SKLAD");
    skladCORG.setServerColumnName("CORG");
    skladCORG.setSqlType(1);
    skladNAZSKL.setCaption("Naziv");
    skladNAZSKL.setColumnName("NAZSKL");
    skladNAZSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladNAZSKL.setPrecision(50);
    skladNAZSKL.setTableName("SKLAD");
    skladNAZSKL.setWidth(20);
    skladNAZSKL.setServerColumnName("NAZSKL");
    skladNAZSKL.setSqlType(1);
    skladCSKL.setCaption("\u0160ifra");
    skladCSKL.setColumnName("CSKL");
    skladCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladCSKL.setPrecision(12);
    skladCSKL.setRowId(true);
    skladCSKL.setTableName("SKLAD");
    skladCSKL.setServerColumnName("CSKL");
    skladCSKL.setWidth(8);
    skladCSKL.setSqlType(1);

    skladAKTIV.setColumnName("AKTIV");
    skladAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladAKTIV.setDefault("D");
    skladAKTIV.setPrecision(1);
    skladAKTIV.setTableName("SKLAD");
    skladAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skladAKTIV.setServerColumnName("AKTIV");
    skladAKTIV.setSqlType(1);
    skladLOKK.setColumnName("LOKK");
    skladLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladLOKK.setDefault("N");
    skladLOKK.setPrecision(1);
    skladLOKK.setTableName("SKLAD");
    skladLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skladLOKK.setServerColumnName("LOKK");
    skladLOKK.setSqlType(1);

    skladDATOBRAC.setCaption("Datum obra\u010Duna");
    skladDATOBRAC.setColumnName("DATOBRAC");
    skladDATOBRAC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skladDATOBRAC.setDisplayMask("dd.MM.yyyy");
    skladDATOBRAC.setEditMask("dd.MM.yyyy");
    skladDATOBRAC.setTableName("SKLAD");
    skladDATOBRAC.setServerColumnName("DATOBRAC");
    skladDATOBRAC.setSqlType(93);
    skladDATOBRAC.setVisible(com.borland.jb.util.TriStateProperty.FALSE);

    skladNACOBR.setCaption("Na\u010Din obrade");
    skladNACOBR.setColumnName("NACOBR");
    skladNACOBR.setDataType(com.borland.dx.dataset.Variant.STRING);
    skladNACOBR.setDefault("A");
    skladNACOBR.setPrecision(1);
    skladNACOBR.setTableName("SKLAD");
    skladNACOBR.setServerColumnName("NACOBR");
    skladNACOBR.setSqlType(1);
    skladNACOBR.setWidth(2);

    sklad.setResolver(dm.getQresolver());
    sklad.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM SKLAD", null, true, Load.ALL));
 setColumns(new Column[] {skladLOKK, skladAKTIV, skladCSKL, skladNAZSKL, skladCORG, skladKNJIG, skladGODINA, skladVRSKL, skladVRZAL, skladTIPSKL,
        skladSTARIDATINV, skladDATINV, skladDATULDOK, skladDATKNJIZ, skladSTATINV, skladSTATRAD, skladDATOBRAC, skladNACOBR});
*/
    initModule();
    createFilteredDataSet(skladaktiv, "aktiv = 'D'");
  }


  //public void setall(){

    /*SqlDefTabela = "create table Sklad " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
      "cskl char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladista
      "nazskl char(50) CHARACTER SET WIN1250 , " + //Naziv skladišta
      "corg char(10) CHARACTER SET WIN1250 , " + //Šifra org. jedinice
      "vrskl char(1) CHARACTER SET WIN1250 , " +//Vrsta skl.(skl ili trgovina)
      "vrzal char(1) CHARACTER SET WIN1250, "+ //Vrsta zal. (NC,VC,MC)
      "tipskl char(1) CHARACTER SET WIN1250 , "+ //Trg.roba ili proizvod (R/P)
      "staridatinv date , "+ // Zadnji datum inventure
      "datinv date , "+ //Datum inventure
      "statinv char(1) CHARACTER SET WIN1250 default 'N' , "+ // Status inventure
      "statrad char(1) , "+ //Status rada u 2. godine
      "Primary Key (cskl))" ;*/

    /*ddl.create("sklad")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("nazskl", 50)
       .addChar("corg", 12)
       .addChar("knjig", 12)
       .addChar("godina", 4)
       .addChar("vrskl", 1)
       .addChar("vrzal", 1)
       .addChar("tipskl", 1)
       .addDate("staridatinv")
       .addDate("datinv")
       .addDate("datuldok")
       .addDate("datknjiz")
       .addChar("statinv", 1, "N")
       .addChar("statrad", 1)
       .addDate("datobrac")
       .addChar("nacobr", 1)
       .addPrimaryKey("cskl");

    Naziv="Sklad";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"nazskl"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);*/

    /*
    NaziviIdx=new String[]{"ilokksklad","iaktivsklad","icskl","inazskl"};


    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Sklad (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Sklad (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Sklad (cskl)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on Sklad (nazskl)"};
*/
  //}
}