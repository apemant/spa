/****license*****************************************************************
**   file: Konta.java
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

public class Konta extends KreirDrop implements DataModule {
  //ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Konta Kontaclass;

  /*dM dm  = dM.getDataModule();
  Column kontaLOKK = new Column();
  Column kontaAKTIV = new Column();
  Column kontaBROJKONTA = new Column();
  Column kontaNAZIVKONTA = new Column();
  Column kontaVRSTAKONTA = new Column();
  Column kontaORGSTR = new Column();
  Column kontaOSTAVKE = new Column();
  Column kontaSALDAK = new Column();
  Column kontaKARAKTERISTIKA = new Column();
  Column kontaISPISBB = new Column();
  Column kontaNACPR = new Column();*/
  QueryDataSet konta = new raDataSet();
  QueryDataSet kontaaktiv = new raDataSet();
  QueryDataSet kontaSin = new raDataSet();
  QueryDataSet kontaAna = new raDataSet();
  QueryDataSet kontaAnaP = new raDataSet();
  QueryDataSet kontaAnaD = new raDataSet();

  public static Konta getDataModule() {
    if (Kontaclass == null) {
      Kontaclass = new Konta();
    }
    return Kontaclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return konta;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return kontaaktiv;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaSin() {
    return kontaSin;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaAna() {
    return kontaAna;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaAnaP() {
    return kontaAnaP;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaAnaD() {
    return kontaAnaD;
  }

  public Konta() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    /*kontaNACPR.setCaption("Na\u010Din knjiženja");
    kontaNACPR.setColumnName("NACPR");
    kontaNACPR.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaNACPR.setDefault("P");
    kontaNACPR.setPrecision(1);
    kontaNACPR.setTableName("KONTA");
    kontaNACPR.setSqlType(1);
    kontaNACPR.setServerColumnName("NACPR");

    kontaISPISBB.setCaption(dmRes.getString("kontaISPISBB_caption"));
    kontaISPISBB.setColumnName("ISPISBB");
    kontaISPISBB.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaISPISBB.setPrecision(1);
    kontaISPISBB.setTableName("KONTA");
    kontaISPISBB.setSqlType(1);
    kontaISPISBB.setServerColumnName("ISPISBB");
    kontaKARAKTERISTIKA.setCaption(dmRes.getString("kontaKARAKTERISTIKA_caption"));
    kontaKARAKTERISTIKA.setColumnName("KARAKTERISTIKA");
    kontaKARAKTERISTIKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaKARAKTERISTIKA.setDefault("O");
    kontaKARAKTERISTIKA.setPrecision(1);
    kontaKARAKTERISTIKA.setTableName("KONTA");
    kontaKARAKTERISTIKA.setSqlType(1);
    kontaKARAKTERISTIKA.setServerColumnName("KARAKTERISTIKA");
    kontaSALDAK.setCaption(dmRes.getString("kontaSALDAK_caption"));
    kontaSALDAK.setColumnName("SALDAK");
    kontaSALDAK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaSALDAK.setDefault("F");
    kontaSALDAK.setPrecision(1);
    kontaSALDAK.setTableName("KONTA");
    kontaSALDAK.setSqlType(1);
    kontaSALDAK.setServerColumnName("SALDAK");
    kontaOSTAVKE.setCaption(dmRes.getString("kontaOSTAVKE_caption"));
    kontaOSTAVKE.setColumnName("OSTAVKE");
    kontaOSTAVKE.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaOSTAVKE.setPrecision(1);
    kontaOSTAVKE.setTableName("KONTA");
    kontaOSTAVKE.setSqlType(1);
    kontaOSTAVKE.setServerColumnName("OSTAVKE");
    kontaORGSTR.setCaption(dmRes.getString("kontaORGSTR_caption"));
    kontaORGSTR.setColumnName("ORGSTR");
    kontaORGSTR.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaORGSTR.setPrecision(1);
    kontaORGSTR.setTableName("KONTA");
    kontaORGSTR.setSqlType(1);
    kontaORGSTR.setServerColumnName("ORGSTR");
    kontaVRSTAKONTA.setCaption("Vrsta");
    kontaVRSTAKONTA.setColumnName("VRSTAKONTA");
    kontaVRSTAKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaVRSTAKONTA.setDefault("A");
    kontaVRSTAKONTA.setPrecision(1);
    kontaVRSTAKONTA.setTableName("KONTA");
    kontaVRSTAKONTA.setSqlType(1);
    kontaVRSTAKONTA.setServerColumnName("VRSTAKONTA");
    kontaNAZIVKONTA.setCaption(dmRes.getString("kontaNAZIVKONTA_caption"));
    kontaNAZIVKONTA.setColumnName("NAZIVKONTA");
    kontaNAZIVKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaNAZIVKONTA.setPrecision(72);
    kontaNAZIVKONTA.setTableName("KONTA");
    kontaNAZIVKONTA.setSqlType(1);
    kontaNAZIVKONTA.setWidth(40);
    kontaNAZIVKONTA.setServerColumnName("NAZIVKONTA");
    kontaBROJKONTA.setCaption(dmRes.getString("kontaBROJKONTA_caption"));
    kontaBROJKONTA.setColumnName("BROJKONTA");
    kontaBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaBROJKONTA.setPrecision(8);
    kontaBROJKONTA.setRowId(true);
    kontaBROJKONTA.setTableName("KONTA");
    kontaBROJKONTA.setSqlType(1);
    kontaBROJKONTA.setWidth(10);
    kontaBROJKONTA.setServerColumnName("BROJKONTA");
    kontaAKTIV.setCaption("Aktiv");
    kontaAKTIV.setColumnName("AKTIV");
    kontaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaAKTIV.setDefault("D");
    kontaAKTIV.setPrecision(1);
    kontaAKTIV.setTableName("KONTA");
    kontaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kontaAKTIV.setSqlType(1);
    kontaAKTIV.setServerColumnName("AKTIV");
    kontaLOKK.setCaption(dmRes.getString("kontaLOKK_caption"));
    kontaLOKK.setColumnName("LOKK");
    kontaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kontaLOKK.setDefault("N");
    kontaLOKK.setPrecision(1);
    kontaLOKK.setTableName("KONTA");
    kontaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kontaLOKK.setSqlType(1);
    kontaLOKK.setServerColumnName("LOKK");
    konta.setResolver(dm.getQresolver());
    konta.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from konta", null, true, Load.ALL));
 setColumns(new Column[] {kontaLOKK, kontaAKTIV, kontaBROJKONTA, kontaNAZIVKONTA, kontaVRSTAKONTA, kontaORGSTR, kontaOSTAVKE, kontaSALDAK,
        kontaKARAKTERISTIKA, kontaISPISBB, kontaNACPR});
*/
    initModule();
    createFilteredDataSet(kontaaktiv, "aktiv = 'D'");
    createFilteredDataSet(kontaSin, "aktiv = 'D' AND vrstakonta != 'A'");
    createFilteredDataSet(kontaAna, "aktiv = 'D' AND vrstakonta = 'A'");
    createFilteredDataSet(kontaAnaP, "aktiv = 'D' AND vrstakonta = 'A' AND (karakteristika = 'O' OR karakteristika = 'P')");
    createFilteredDataSet(kontaAnaD, "aktiv = 'D' AND vrstakonta = 'A' AND (karakteristika = 'O' OR karakteristika = 'D')");
  }

/* public void setall(){

    SqlDefTabela = "create table Konta " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N'," +
      "aktiv char(1) CHARACTER SET WIN1250 default 'D'," +
      "brojkonta CHAR(8) CHARACTER SET WIN1250 NOT NULL, " + //Status zauzetosti
      "nazivkonta CHAR(72) CHARACTER SET WIN1250," +
      "vrstakonta CHAR(1) CHARACTER SET WIN1250,"+
      "orgstr CHAR(1) CHARACTER SET WIN1250,"+
      "ostavke CHAR(1) CHARACTER SET WIN1250,"+
      "saldak CHAR(1) CHARACTER SET WIN1250,"+
      "karakteristika CHAR(1) CHARACTER SET WIN1250,"+
      "ispisbb CHAR(1) CHARACTER SET WIN1250,"+
      "Primary Key (brojkonta))" ;
    ddl.create("konta")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("brojkonta", 8, true)
       .addChar("nazivkonta", 72)
       .addChar("vrstakonta", 1)
       .addChar("orgstr", 1)
       .addChar("ostavke", 1)
       .addChar("saldak", 1)
       .addChar("karakteristika", 1)
       .addChar("ispisbb", 1)
       .addChar("nacpr", 1, "P")
       .addPrimaryKey("brojkonta");

    Naziv="Konta";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


    NaziviIdx=new String[]{"ilokkkonta","iaktivkonta","ibrojkonta","inazivkonta"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Konta (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Konta (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Konta (brojkonta)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" Konta (nazivkonta)"} ;
                          
  }*/
}
