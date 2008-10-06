/****license*****************************************************************
**   file: Sifrarnici.java
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
import com.borland.dx.sql.dataset.QueryDescriptor;



public class Sifrarnici extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Sifrarnici Sifrarniciclass;

  QueryDataSet sifr = new raDataSet();

  Column sifrLOKK = new Column();
  Column sifrAKTIV = new Column();
  Column sifrCSIF = new Column();
  Column sifrVRSTASIF = new Column();
  Column sifrNAZIV = new Column();
  Column sifrOPIS = new Column();
  Column sifrCSIFPRIP = new Column();
  Column sifrPARAMETRI = new Column();

  public static Sifrarnici getDataModule() {
    if (Sifrarniciclass == null) {
      Sifrarniciclass = new Sifrarnici();
    }
    return Sifrarniciclass;
  }

  public QueryDataSet getQueryDataSet() {
    return sifr;
  }

  public Sifrarnici() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    sifrLOKK.setCaption("Status zauzetosti");
    sifrLOKK.setColumnName("LOKK");
    sifrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sifrLOKK.setPrecision(1);
    sifrLOKK.setTableName("SIFRARNICI");
    sifrLOKK.setServerColumnName("LOKK");
    sifrLOKK.setSqlType(1);
    sifrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sifrLOKK.setDefault("N");
    sifrAKTIV.setCaption("Aktivan - neaktivan");
    sifrAKTIV.setColumnName("AKTIV");
    sifrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    sifrAKTIV.setPrecision(1);
    sifrAKTIV.setTableName("SIFRARNICI");
    sifrAKTIV.setServerColumnName("AKTIV");
    sifrAKTIV.setSqlType(1);
    sifrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sifrAKTIV.setDefault("D");
    sifrCSIF.setCaption("Oznaka");
    sifrCSIF.setColumnName("CSIF");
    sifrCSIF.setDataType(com.borland.dx.dataset.Variant.STRING);
    sifrCSIF.setPrecision(5);
    sifrCSIF.setRowId(true);
    sifrCSIF.setTableName("SIFRARNICI");
    sifrCSIF.setServerColumnName("CSIF");
    sifrCSIF.setSqlType(1);
    sifrVRSTASIF.setCaption("Vrsta");
    sifrVRSTASIF.setColumnName("VRSTASIF");
    sifrVRSTASIF.setDataType(com.borland.dx.dataset.Variant.STRING);
    sifrVRSTASIF.setPrecision(4);
    sifrVRSTASIF.setRowId(true);
    sifrVRSTASIF.setTableName("SIFRARNICI");
    sifrVRSTASIF.setServerColumnName("VRSTASIF");
    sifrVRSTASIF.setSqlType(1);
    sifrNAZIV.setCaption("Naziv");
    sifrNAZIV.setColumnName("NAZIV");
    sifrNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    sifrNAZIV.setPrecision(30);
    sifrNAZIV.setTableName("SIFRARNICI");
    sifrNAZIV.setServerColumnName("NAZIV");
    sifrNAZIV.setSqlType(1);
    sifrOPIS.setCaption("Opis");
    sifrOPIS.setColumnName("OPIS");
    sifrOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    sifrOPIS.setPrecision(150);
    sifrOPIS.setTableName("SIFRARNICI");
    sifrOPIS.setServerColumnName("OPIS");
    sifrOPIS.setSqlType(1);
    sifrCSIFPRIP.setCaption("Oznaka pripadnosti");
    sifrCSIFPRIP.setColumnName("CSIFPRIP");
    sifrCSIFPRIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    sifrCSIFPRIP.setPrecision(5);
    sifrCSIFPRIP.setTableName("SIFRARNICI");
    sifrCSIFPRIP.setServerColumnName("CSIFPRIP");
    sifrCSIFPRIP.setSqlType(1);
    sifrPARAMETRI.setCaption("Parametri");
    sifrPARAMETRI.setColumnName("PARAMETRI");
    sifrPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    sifrPARAMETRI.setPrecision(20);
    sifrPARAMETRI.setTableName("SIFRARNICI");
    sifrPARAMETRI.setServerColumnName("PARAMETRI");
    sifrPARAMETRI.setSqlType(1);
    sifrPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sifr.setResolver(dm.getQresolver());
    sifr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Sifrarnici", null, true, Load.ALL));
 setColumns(new Column[] {sifrLOKK, sifrAKTIV, sifrCSIF, sifrVRSTASIF, sifrNAZIV, sifrOPIS, sifrCSIFPRIP, sifrPARAMETRI});
  }

  public void setall() {

    ddl.create("Sifrarnici")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("csif", 5, true)
       .addChar("vrstasif", 4, true)
       .addChar("naziv", 30)
       .addChar("opis", 150)
       .addChar("csifprip", 5)
       .addChar("parametri", 20)
       .addPrimaryKey("csif,vrstasif");


    Naziv = "Sifrarnici";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"csif", "vrstasif"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
