/****license*****************************************************************
**   file: Vrsteodb.java
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



public class Vrsteodb extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrsteodb Vrsteodbclass;

  QueryDataSet vodb = new raDataSet();

  Column vodbLOKK = new Column();
  Column vodbAKTIV = new Column();
  Column vodbCVRODB = new Column();
  Column vodbOPISVRODB = new Column();
  Column vodbNIVOODB = new Column();
  Column vodbTIPODB = new Column();
  Column vodbVRSTAOSN = new Column();
  Column vodbOSNOVICA = new Column();
  Column vodbCPOV = new Column();
  Column vodbIZNOS = new Column();
  Column vodbSTOPA = new Column();
  Column vodbPARAMETRI = new Column();

  public static Vrsteodb getDataModule() {
    if (Vrsteodbclass == null) {
      Vrsteodbclass = new Vrsteodb();
    }
    return Vrsteodbclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vodb;
  }

  public Vrsteodb() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vodbLOKK.setCaption("Status zauzetosti");
    vodbLOKK.setColumnName("LOKK");
    vodbLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vodbLOKK.setPrecision(1);
    vodbLOKK.setTableName("VRSTEODB");
    vodbLOKK.setServerColumnName("LOKK");
    vodbLOKK.setSqlType(1);
    vodbLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vodbLOKK.setDefault("N");
    vodbAKTIV.setCaption("Aktivan - neaktivan");
    vodbAKTIV.setColumnName("AKTIV");
    vodbAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vodbAKTIV.setPrecision(1);
    vodbAKTIV.setTableName("VRSTEODB");
    vodbAKTIV.setServerColumnName("AKTIV");
    vodbAKTIV.setSqlType(1);
    vodbAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vodbAKTIV.setDefault("D");
    vodbCVRODB.setCaption("Oznaka");
    vodbCVRODB.setColumnName("CVRODB");
    vodbCVRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vodbCVRODB.setPrecision(4);
    vodbCVRODB.setRowId(true);
    vodbCVRODB.setTableName("VRSTEODB");
    vodbCVRODB.setServerColumnName("CVRODB");
    vodbCVRODB.setSqlType(5);
    vodbCVRODB.setWidth(4);
    vodbOPISVRODB.setCaption("Opis");
    vodbOPISVRODB.setColumnName("OPISVRODB");
    vodbOPISVRODB.setDataType(com.borland.dx.dataset.Variant.STRING);
    vodbOPISVRODB.setPrecision(30);
    vodbOPISVRODB.setTableName("VRSTEODB");
    vodbOPISVRODB.setServerColumnName("OPISVRODB");
    vodbOPISVRODB.setSqlType(1);
    vodbNIVOODB.setCaption("Nivo odbitka");
    vodbNIVOODB.setColumnName("NIVOODB");
    vodbNIVOODB.setDataType(com.borland.dx.dataset.Variant.STRING);
    vodbNIVOODB.setPrecision(4);
    vodbNIVOODB.setTableName("VRSTEODB");
    vodbNIVOODB.setServerColumnName("NIVOODB");
    vodbNIVOODB.setSqlType(1);
    vodbNIVOODB.setDefault("RA");
    vodbTIPODB.setCaption("Tip odbitka");
    vodbTIPODB.setColumnName("TIPODB");
    vodbTIPODB.setDataType(com.borland.dx.dataset.Variant.STRING);
    vodbTIPODB.setPrecision(1);
    vodbTIPODB.setTableName("VRSTEODB");
    vodbTIPODB.setServerColumnName("TIPODB");
    vodbTIPODB.setSqlType(1);
    vodbTIPODB.setDefault("S");
    vodbVRSTAOSN.setCaption("Vrsta osnovice");
    vodbVRSTAOSN.setColumnName("VRSTAOSN");
    vodbVRSTAOSN.setDataType(com.borland.dx.dataset.Variant.STRING);
    vodbVRSTAOSN.setPrecision(1);
    vodbVRSTAOSN.setTableName("VRSTEODB");
    vodbVRSTAOSN.setServerColumnName("VRSTAOSN");
    vodbVRSTAOSN.setSqlType(1);
    vodbVRSTAOSN.setDefault("3");
    vodbOSNOVICA.setCaption("Osnovica za obracun");
    vodbOSNOVICA.setColumnName("OSNOVICA");
    vodbOSNOVICA.setDataType(com.borland.dx.dataset.Variant.STRING);
    vodbOSNOVICA.setPrecision(1);
    vodbOSNOVICA.setTableName("VRSTEODB");
    vodbOSNOVICA.setServerColumnName("OSNOVICA");
    vodbOSNOVICA.setSqlType(1);
    vodbOSNOVICA.setDefault("0");
    vodbCPOV.setCaption("Oznaka povjerioca-virmana");
    vodbCPOV.setColumnName("CPOV");
    vodbCPOV.setDataType(com.borland.dx.dataset.Variant.INT);
    vodbCPOV.setPrecision(6);
    vodbCPOV.setTableName("VRSTEODB");
    vodbCPOV.setServerColumnName("CPOV");
    vodbCPOV.setSqlType(4);
    vodbCPOV.setWidth(6);
    vodbIZNOS.setCaption("Iznos");
    vodbIZNOS.setColumnName("IZNOS");
    vodbIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vodbIZNOS.setPrecision(17);
    vodbIZNOS.setScale(2);
    vodbIZNOS.setDisplayMask("###,###,##0.00");
    vodbIZNOS.setDefault("0");
    vodbIZNOS.setTableName("VRSTEODB");
    vodbIZNOS.setServerColumnName("IZNOS");
    vodbIZNOS.setSqlType(2);
    vodbIZNOS.setDefault("0");
    vodbSTOPA.setCaption("Stopa");
    vodbSTOPA.setColumnName("STOPA");
    vodbSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vodbSTOPA.setPrecision(7);
    vodbSTOPA.setScale(5);
    vodbSTOPA.setDisplayMask("###,###,##0.00000");
    vodbSTOPA.setDefault("0");
    vodbSTOPA.setTableName("VRSTEODB");
    vodbSTOPA.setServerColumnName("STOPA");
    vodbSTOPA.setSqlType(2);
    vodbSTOPA.setDefault("0");
    vodbPARAMETRI.setCaption("Parametri");
    vodbPARAMETRI.setColumnName("PARAMETRI");
    vodbPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    vodbPARAMETRI.setPrecision(20);
    vodbPARAMETRI.setTableName("VRSTEODB");
    vodbPARAMETRI.setServerColumnName("PARAMETRI");
    vodbPARAMETRI.setSqlType(1);
    vodbPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vodb.setResolver(dm.getQresolver());
    vodb.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vrsteodb", null, true, Load.ALL));
 setColumns(new Column[] {vodbLOKK, vodbAKTIV, vodbCVRODB, vodbOPISVRODB, vodbNIVOODB, vodbTIPODB, vodbVRSTAOSN, vodbOSNOVICA, vodbCPOV, vodbIZNOS,
        vodbSTOPA, vodbPARAMETRI});
  }

  public void setall() {

    ddl.create("Vrsteodb")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cvrodb", 4, true)
       .addChar("opisvrodb", 30)
       .addChar("nivoodb", 4, "RA")
       .addChar("tipodb", 1, "S")
       .addChar("vrstaosn", 1, "3")
       .addChar("osnovica", 1, "0")
       .addInteger("cpov", 6)
       .addFloat("iznos", 17, 2)
       .addFloat("stopa", 7, 5)
       .addChar("parametri", 20)
       .addPrimaryKey("cvrodb");


    Naziv = "Vrsteodb";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
