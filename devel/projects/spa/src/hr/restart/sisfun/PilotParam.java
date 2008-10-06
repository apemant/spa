/****license*****************************************************************
**   file: PilotParam.java
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
package hr.restart.sisfun;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;



public class PilotParam {

  private static PilotParam PilotParamclass;

  StorageDataSet pp = new StorageDataSet();

  Column ppIME = new Column();
  Column ppOPIS = new Column();
  Column ppVRSTA = new Column();
  Column ppTIP = new Column();
  Column ppGET = new Column();
  Column ppKOLONE = new Column();
  Column ppVISKOL = new Column();
  Column ppDEFAULT = new Column();
  Column ppWIDTH = new Column();

  public static PilotParam getDataModule() {
    if (PilotParamclass == null) {
      PilotParamclass = new PilotParam();
    }
    return PilotParamclass;
  }

  public StorageDataSet getDataSet() {
    return pp;
  }

  public PilotParam() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    ppIME.setCaption("Ime");
    ppIME.setColumnName("IME");
    ppIME.setDataType(com.borland.dx.dataset.Variant.STRING);
    ppIME.setPrecision(16);
    ppIME.setTableName("PILOTPARAM");
    ppIME.setServerColumnName("IME");
    ppIME.setSqlType(1);
    ppOPIS.setCaption("Opis");
    ppOPIS.setColumnName("OPIS");
    ppOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    ppOPIS.setPrecision(80);
    ppOPIS.setTableName("PILOTPARAM");
    ppOPIS.setServerColumnName("OPIS");
    ppOPIS.setSqlType(1);
    ppOPIS.setWidth(30);
    ppVRSTA.setCaption("Vrsta");
    ppVRSTA.setColumnName("VRSTA");
    ppVRSTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ppVRSTA.setPrecision(1);
    ppVRSTA.setTableName("PILOTPARAM");
    ppVRSTA.setServerColumnName("VRSTA");
    ppVRSTA.setSqlType(1);
    ppVRSTA.setDefault("P");
//    ppVRSTA.setWidth(8);
    ppTIP.setCaption("Tip");
    ppTIP.setColumnName("TIP");
    ppTIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    ppTIP.setPrecision(1);
    ppTIP.setTableName("PILOTPARAM");
    ppTIP.setServerColumnName("TIP");
    ppTIP.setDefault("S");
    ppTIP.setSqlType(1);
//    ppTIP.setWidth(10);
    ppGET.setCaption("Funkcija dohvata");
    ppGET.setColumnName("GET");
    ppGET.setDataType(com.borland.dx.dataset.Variant.STRING);
    ppGET.setPrecision(200);
    ppGET.setTableName("PILOTPARAM");
    ppGET.setServerColumnName("GET");
    ppGET.setSqlType(1);
    ppGET.setWidth(30);
    ppKOLONE.setCaption("Polja dohvata");
    ppKOLONE.setColumnName("KOLONE");
    ppKOLONE.setDataType(com.borland.dx.dataset.Variant.STRING);
    ppKOLONE.setPrecision(40);
    ppKOLONE.setTableName("PILOTPARAM");
    ppKOLONE.setServerColumnName("KOLONE");
    ppKOLONE.setSqlType(1);
    ppKOLONE.setWidth(30);

    ppVISKOL.setCaption("Kolone dohvata");
    ppVISKOL.setColumnName("VISKOL");
    ppVISKOL.setDataType(com.borland.dx.dataset.Variant.STRING);
    ppVISKOL.setPrecision(30);
    ppVISKOL.setTableName("PILOTPARAM");
    ppVISKOL.setServerColumnName("VISKOL");
    ppVISKOL.setSqlType(1);
    ppDEFAULT.setCaption("Default");
    ppDEFAULT.setColumnName("DEFAULT");
    ppDEFAULT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ppDEFAULT.setPrecision(80);
    ppDEFAULT.setTableName("PILOTPARAM");
    ppDEFAULT.setServerColumnName("DEFAULT");
    ppDEFAULT.setSqlType(1);
    ppDEFAULT.setWidth(30);

    ppWIDTH.setCaption("Širina");
    ppWIDTH.setColumnName("WIDTH");
    ppWIDTH.setDataType(com.borland.dx.dataset.Variant.INT);
    ppWIDTH.setTableName("PILOTPARAM");
    ppWIDTH.setSqlType(4);
    ppWIDTH.setServerColumnName("WIDTH");

    pp.setColumns(new Column[] {ppIME, ppOPIS, ppVRSTA, ppTIP, ppGET, ppKOLONE, ppVISKOL,
        ppDEFAULT, ppWIDTH});
  }
}
