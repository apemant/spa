/****license*****************************************************************
**   file: Povjerioci.java
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



public class Povjerioci extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Povjerioci Povjeriociclass;

  QueryDataSet pov = new raDataSet();

  Column povLOKK = new Column();
  Column povAKTIV = new Column();
  Column povCPOV = new Column();
  Column povNAZPOV = new Column();
  Column povPBR = new Column();
  Column povMJESTO = new Column();
  Column povADRESA = new Column();
  Column povNACISP = new Column();
  Column povPNBZ1 = new Column();
  Column povPNBZ2 = new Column();
  Column povPNBO1 = new Column();
  Column povPNBO2 = new Column();
  Column povSIF1 = new Column();
  Column povSIF2 = new Column();
  Column povSIF3 = new Column();
  Column povZIRO = new Column();
  Column povSVRHA = new Column();
  Column povPARAMETRI = new Column();

  public static Povjerioci getDataModule() {
    if (Povjeriociclass == null) {
      Povjeriociclass = new Povjerioci();
    }
    return Povjeriociclass;
  }

  public QueryDataSet getQueryDataSet() {
    return pov;
  }

  public Povjerioci() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    povLOKK.setCaption("Status zauzetosti");
    povLOKK.setColumnName("LOKK");
    povLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    povLOKK.setPrecision(1);
    povLOKK.setTableName("POVJERIOCI");
    povLOKK.setServerColumnName("LOKK");
    povLOKK.setSqlType(1);
    povLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    povLOKK.setDefault("N");
    povAKTIV.setCaption("Aktivan - neaktivan");
    povAKTIV.setColumnName("AKTIV");
    povAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    povAKTIV.setPrecision(1);
    povAKTIV.setTableName("POVJERIOCI");
    povAKTIV.setServerColumnName("AKTIV");
    povAKTIV.setSqlType(1);
    povAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    povAKTIV.setDefault("D");
    povCPOV.setCaption("Oznaka");
    povCPOV.setColumnName("CPOV");
    povCPOV.setDataType(com.borland.dx.dataset.Variant.INT);
    povCPOV.setPrecision(6);
    povCPOV.setRowId(true);
    povCPOV.setTableName("POVJERIOCI");
    povCPOV.setServerColumnName("CPOV");
    povCPOV.setSqlType(4);
    povCPOV.setWidth(6);
    povNAZPOV.setCaption("Naziv");
    povNAZPOV.setColumnName("NAZPOV");
    povNAZPOV.setDataType(com.borland.dx.dataset.Variant.STRING);
    povNAZPOV.setPrecision(100);
    povNAZPOV.setTableName("POVJERIOCI");
    povNAZPOV.setServerColumnName("NAZPOV");
    povNAZPOV.setSqlType(1);
    povPBR.setCaption("Poštanski broj");
    povPBR.setColumnName("PBR");
    povPBR.setDataType(com.borland.dx.dataset.Variant.INT);
    povPBR.setPrecision(5);
    povPBR.setTableName("POVJERIOCI");
    povPBR.setServerColumnName("PBR");
    povPBR.setSqlType(4);
    povPBR.setWidth(5);
    povMJESTO.setCaption("Mjesto");
    povMJESTO.setColumnName("MJESTO");
    povMJESTO.setDataType(com.borland.dx.dataset.Variant.STRING);
    povMJESTO.setPrecision(30);
    povMJESTO.setTableName("POVJERIOCI");
    povMJESTO.setServerColumnName("MJESTO");
    povMJESTO.setSqlType(1);
    povADRESA.setCaption("Adresa");
    povADRESA.setColumnName("ADRESA");
    povADRESA.setDataType(com.borland.dx.dataset.Variant.STRING);
    povADRESA.setPrecision(50);
    povADRESA.setTableName("POVJERIOCI");
    povADRESA.setServerColumnName("ADRESA");
    povADRESA.setSqlType(1);
    povNACISP.setCaption("Na\u010Din ispisa");
    povNACISP.setColumnName("NACISP");
    povNACISP.setDataType(com.borland.dx.dataset.Variant.STRING);
    povNACISP.setPrecision(1);
    povNACISP.setTableName("POVJERIOCI");
    povNACISP.setServerColumnName("NACISP");
    povNACISP.setSqlType(1);
    povNACISP.setDefault("2");
    povPNBZ1.setCaption("Poziv na broj zaduž (1.dio)");
    povPNBZ1.setColumnName("PNBZ1");
    povPNBZ1.setDataType(com.borland.dx.dataset.Variant.STRING);
    povPNBZ1.setPrecision(2);
    povPNBZ1.setTableName("POVJERIOCI");
    povPNBZ1.setServerColumnName("PNBZ1");
    povPNBZ1.setSqlType(1);
    povPNBZ2.setCaption("Poziv na broj zaduž (2.dio)");
    povPNBZ2.setColumnName("PNBZ2");
    povPNBZ2.setDataType(com.borland.dx.dataset.Variant.STRING);
    povPNBZ2.setPrecision(22);
    povPNBZ2.setTableName("POVJERIOCI");
    povPNBZ2.setServerColumnName("PNBZ2");
    povPNBZ2.setSqlType(1);
    povPNBO1.setCaption("Poziv na broj odob (1.dio)");
    povPNBO1.setColumnName("PNBO1");
    povPNBO1.setDataType(com.borland.dx.dataset.Variant.STRING);
    povPNBO1.setPrecision(2);
    povPNBO1.setTableName("POVJERIOCI");
    povPNBO1.setServerColumnName("PNBO1");
    povPNBO1.setSqlType(1);
    povPNBO2.setCaption("Poziv na broj odob (2.dio)");
    povPNBO2.setColumnName("PNBO2");
    povPNBO2.setDataType(com.borland.dx.dataset.Variant.STRING);
    povPNBO2.setPrecision(22);
    povPNBO2.setTableName("POVJERIOCI");
    povPNBO2.setServerColumnName("PNBO2");
    povPNBO2.setSqlType(1);
    povSIF1.setCaption("Šifra 1");
    povSIF1.setColumnName("SIF1");
    povSIF1.setDataType(com.borland.dx.dataset.Variant.STRING);
    povSIF1.setPrecision(3);
    povSIF1.setTableName("POVJERIOCI");
    povSIF1.setServerColumnName("SIF1");
    povSIF1.setSqlType(1);
    povSIF2.setCaption("Šifra 2");
    povSIF2.setColumnName("SIF2");
    povSIF2.setDataType(com.borland.dx.dataset.Variant.STRING);
    povSIF2.setPrecision(3);
    povSIF2.setTableName("POVJERIOCI");
    povSIF2.setServerColumnName("SIF2");
    povSIF2.setSqlType(1);
    povSIF3.setCaption("Šifra 3");
    povSIF3.setColumnName("SIF3");
    povSIF3.setDataType(com.borland.dx.dataset.Variant.STRING);
    povSIF3.setPrecision(3);
    povSIF3.setTableName("POVJERIOCI");
    povSIF3.setServerColumnName("SIF3");
    povSIF3.setSqlType(1);
    povZIRO.setCaption("Žiro ra\u010Dun");
    povZIRO.setColumnName("ZIRO");
    povZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    povZIRO.setPrecision(40);
    povZIRO.setTableName("POVJERIOCI");
    povZIRO.setServerColumnName("ZIRO");
    povZIRO.setSqlType(1);
    povSVRHA.setCaption("Svrha doznake");
    povSVRHA.setColumnName("SVRHA");
    povSVRHA.setDataType(com.borland.dx.dataset.Variant.STRING);
    povSVRHA.setPrecision(50);
    povSVRHA.setTableName("POVJERIOCI");
    povSVRHA.setServerColumnName("SVRHA");
    povSVRHA.setSqlType(1);
    povPARAMETRI.setCaption("Parametri");
    povPARAMETRI.setColumnName("PARAMETRI");
    povPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    povPARAMETRI.setPrecision(20);
    povPARAMETRI.setTableName("POVJERIOCI");
    povPARAMETRI.setServerColumnName("PARAMETRI");
    povPARAMETRI.setSqlType(1);
    povPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pov.setResolver(dm.getQresolver());
    pov.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Povjerioci", null, true, Load.ALL));
 setColumns(new Column[] {povLOKK, povAKTIV, povCPOV, povNAZPOV, povPBR, povMJESTO, povADRESA, povNACISP, povPNBZ1, povPNBZ2, povPNBO1, povPNBO2,
        povSIF1, povSIF2, povSIF3, povZIRO, povSVRHA, povPARAMETRI});
  }

  public void setall() {

    ddl.create("Povjerioci")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cpov", 6, true)
       .addChar("nazpov", 100)
       .addInteger("pbr", 5)
       .addChar("mjesto", 30)
       .addChar("adresa", 50)
       .addChar("nacisp", 1, "2")
       .addChar("pnbz1", 2)
       .addChar("pnbz2", 22)
       .addChar("pnbo1", 2)
       .addChar("pnbo2", 22)
       .addChar("sif1", 3)
       .addChar("sif2", 3)
       .addChar("sif3", 3)
       .addChar("ziro", 40)
       .addChar("svrha", 50)
       .addChar("parametri", 20)
       .addPrimaryKey("cpov");


    Naziv = "Povjerioci";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
