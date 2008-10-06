/****license*****************************************************************
**   file: Kumulorg.java
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



public class Kumulorg extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Kumulorg Kumulorgclass;

  QueryDataSet kuorg = new raDataSet();

  Column kuorgLOKK = new Column();
  Column kuorgAKTIV = new Column();
  Column kuorgCORG = new Column();
  Column kuorgCVRO = new Column();
  Column kuorgKNJIG = new Column();
  Column kuorgSATI = new Column();
  Column kuorgBRUTO = new Column();
  Column kuorgDOPRINOSI = new Column();
  Column kuorgNETO = new Column();
  Column kuorgNEOP = new Column();
  Column kuorgISKNEOP = new Column();
  Column kuorgPOROSN = new Column();
  Column kuorgPOR1 = new Column();
  Column kuorgPOR2 = new Column();
  Column kuorgPOR3 = new Column();
  Column kuorgPOR4 = new Column();
  Column kuorgPOR5 = new Column();
  Column kuorgPORUK = new Column();
  Column kuorgPRIR = new Column();
  Column kuorgPORIPRIR = new Column();
  Column kuorgNETO2 = new Column();
  Column kuorgNAKNADE = new Column();
  Column kuorgNETOPK = new Column();
  Column kuorgKREDITI = new Column();
  Column kuorgNARUKE = new Column();
  Column kuorgDOPRPOD = new Column();

  public static Kumulorg getDataModule() {
    if (Kumulorgclass == null) {
      Kumulorgclass = new Kumulorg();
    }
    return Kumulorgclass;
  }

  public QueryDataSet getQueryDataSet() {
    return kuorg;
  }

  public Kumulorg() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    kuorgLOKK.setCaption("Status zauzetosti");
    kuorgLOKK.setColumnName("LOKK");
    kuorgLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kuorgLOKK.setPrecision(1);
    kuorgLOKK.setTableName("KUMULORG");
    kuorgLOKK.setServerColumnName("LOKK");
    kuorgLOKK.setSqlType(1);
    kuorgLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kuorgLOKK.setDefault("N");
    kuorgAKTIV.setCaption("Aktivan - neaktivan");
    kuorgAKTIV.setColumnName("AKTIV");
    kuorgAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    kuorgAKTIV.setPrecision(1);
    kuorgAKTIV.setTableName("KUMULORG");
    kuorgAKTIV.setServerColumnName("AKTIV");
    kuorgAKTIV.setSqlType(1);
    kuorgAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kuorgAKTIV.setDefault("D");
    kuorgCORG.setCaption("Org. Jedinica");
    kuorgCORG.setColumnName("CORG");
    kuorgCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    kuorgCORG.setPrecision(12);
    kuorgCORG.setRowId(true);
    kuorgCORG.setTableName("KUMULORG");
    kuorgCORG.setServerColumnName("CORG");
    kuorgCORG.setSqlType(1);
    kuorgCVRO.setCaption("Vrsta rada");
    kuorgCVRO.setColumnName("CVRO");
    kuorgCVRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    kuorgCVRO.setPrecision(6);
    kuorgCVRO.setRowId(true);
    kuorgCVRO.setTableName("KUMULORG");
    kuorgCVRO.setServerColumnName("CVRO");
    kuorgCVRO.setSqlType(1);
    kuorgKNJIG.setCaption("Knjigovodstvo");
    kuorgKNJIG.setColumnName("KNJIG");
    kuorgKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    kuorgKNJIG.setPrecision(12);
    kuorgKNJIG.setTableName("KUMULORG");
    kuorgKNJIG.setServerColumnName("KNJIG");
    kuorgKNJIG.setSqlType(1);
    kuorgSATI.setCaption("Sati");
    kuorgSATI.setColumnName("SATI");
    kuorgSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgSATI.setPrecision(17);
    kuorgSATI.setScale(2);
    kuorgSATI.setDisplayMask("###,###,##0.00");
    kuorgSATI.setDefault("0");
    kuorgSATI.setTableName("KUMULORG");
    kuorgSATI.setServerColumnName("SATI");
    kuorgSATI.setSqlType(2);
    kuorgSATI.setDefault("0");
    kuorgBRUTO.setCaption("Bruto");
    kuorgBRUTO.setColumnName("BRUTO");
    kuorgBRUTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgBRUTO.setPrecision(17);
    kuorgBRUTO.setScale(2);
    kuorgBRUTO.setDisplayMask("###,###,##0.00");
    kuorgBRUTO.setDefault("0");
    kuorgBRUTO.setTableName("KUMULORG");
    kuorgBRUTO.setServerColumnName("BRUTO");
    kuorgBRUTO.setSqlType(2);
    kuorgBRUTO.setDefault("0");
    kuorgDOPRINOSI.setCaption("Doprinosi");
    kuorgDOPRINOSI.setColumnName("DOPRINOSI");
    kuorgDOPRINOSI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgDOPRINOSI.setPrecision(17);
    kuorgDOPRINOSI.setScale(2);
    kuorgDOPRINOSI.setDisplayMask("###,###,##0.00");
    kuorgDOPRINOSI.setDefault("0");
    kuorgDOPRINOSI.setTableName("KUMULORG");
    kuorgDOPRINOSI.setServerColumnName("DOPRINOSI");
    kuorgDOPRINOSI.setSqlType(2);
    kuorgDOPRINOSI.setDefault("0");
    kuorgNETO.setCaption("Bruto po odbitku doprinosa");
    kuorgNETO.setColumnName("NETO");
    kuorgNETO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgNETO.setPrecision(17);
    kuorgNETO.setScale(2);
    kuorgNETO.setDisplayMask("###,###,##0.00");
    kuorgNETO.setDefault("0");
    kuorgNETO.setTableName("KUMULORG");
    kuorgNETO.setServerColumnName("NETO");
    kuorgNETO.setSqlType(2);
    kuorgNETO.setDefault("0");
    kuorgNEOP.setCaption("Neoporezivi dio - olakšica");
    kuorgNEOP.setColumnName("NEOP");
    kuorgNEOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgNEOP.setPrecision(17);
    kuorgNEOP.setScale(2);
    kuorgNEOP.setDisplayMask("###,###,##0.00");
    kuorgNEOP.setDefault("0");
    kuorgNEOP.setTableName("KUMULORG");
    kuorgNEOP.setServerColumnName("NEOP");
    kuorgNEOP.setSqlType(2);
    kuorgNEOP.setDefault("0");
    kuorgISKNEOP.setCaption("Iskorišteni neoporezivi dio");
    kuorgISKNEOP.setColumnName("ISKNEOP");
    kuorgISKNEOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgISKNEOP.setPrecision(17);
    kuorgISKNEOP.setScale(2);
    kuorgISKNEOP.setDisplayMask("###,###,##0.00");
    kuorgISKNEOP.setDefault("0");
    kuorgISKNEOP.setTableName("KUMULORG");
    kuorgISKNEOP.setServerColumnName("ISKNEOP");
    kuorgISKNEOP.setSqlType(2);
    kuorgISKNEOP.setDefault("0");
    kuorgPOROSN.setCaption("Porezna osnovica");
    kuorgPOROSN.setColumnName("POROSN");
    kuorgPOROSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPOROSN.setPrecision(17);
    kuorgPOROSN.setScale(2);
    kuorgPOROSN.setDisplayMask("###,###,##0.00");
    kuorgPOROSN.setDefault("0");
    kuorgPOROSN.setTableName("KUMULORG");
    kuorgPOROSN.setServerColumnName("POROSN");
    kuorgPOROSN.setSqlType(2);
    kuorgPOROSN.setDefault("0");
    kuorgPOR1.setCaption("Porez 1");
    kuorgPOR1.setColumnName("POR1");
    kuorgPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPOR1.setPrecision(17);
    kuorgPOR1.setScale(2);
    kuorgPOR1.setDisplayMask("###,###,##0.00");
    kuorgPOR1.setDefault("0");
    kuorgPOR1.setTableName("KUMULORG");
    kuorgPOR1.setServerColumnName("POR1");
    kuorgPOR1.setSqlType(2);
    kuorgPOR1.setDefault("0");
    kuorgPOR2.setCaption("Porez 2");
    kuorgPOR2.setColumnName("POR2");
    kuorgPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPOR2.setPrecision(17);
    kuorgPOR2.setScale(2);
    kuorgPOR2.setDisplayMask("###,###,##0.00");
    kuorgPOR2.setDefault("0");
    kuorgPOR2.setTableName("KUMULORG");
    kuorgPOR2.setServerColumnName("POR2");
    kuorgPOR2.setSqlType(2);
    kuorgPOR2.setDefault("0");
    kuorgPOR3.setCaption("Porez 3");
    kuorgPOR3.setColumnName("POR3");
    kuorgPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPOR3.setPrecision(17);
    kuorgPOR3.setScale(2);
    kuorgPOR3.setDisplayMask("###,###,##0.00");
    kuorgPOR3.setDefault("0");
    kuorgPOR3.setTableName("KUMULORG");
    kuorgPOR3.setServerColumnName("POR3");
    kuorgPOR3.setSqlType(2);
    kuorgPOR3.setDefault("0");
    kuorgPOR4.setCaption("Porez 4");
    kuorgPOR4.setColumnName("POR4");
    kuorgPOR4.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPOR4.setPrecision(17);
    kuorgPOR4.setScale(2);
    kuorgPOR4.setDisplayMask("###,###,##0.00");
    kuorgPOR4.setDefault("0");
    kuorgPOR4.setTableName("KUMULORG");
    kuorgPOR4.setServerColumnName("POR4");
    kuorgPOR4.setSqlType(2);
    kuorgPOR4.setDefault("0");
    kuorgPOR5.setCaption("Porez 5 (invisible)");
    kuorgPOR5.setColumnName("POR5");
    kuorgPOR5.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPOR5.setPrecision(17);
    kuorgPOR5.setScale(2);
    kuorgPOR5.setDisplayMask("###,###,##0.00");
    kuorgPOR5.setDefault("0");
    kuorgPOR5.setTableName("KUMULORG");
    kuorgPOR5.setServerColumnName("POR5");
    kuorgPOR5.setSqlType(2);
    kuorgPOR5.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kuorgPOR5.setDefault("0");
    kuorgPORUK.setCaption("Porez ukupno");
    kuorgPORUK.setColumnName("PORUK");
    kuorgPORUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPORUK.setPrecision(17);
    kuorgPORUK.setScale(2);
    kuorgPORUK.setDisplayMask("###,###,##0.00");
    kuorgPORUK.setDefault("0");
    kuorgPORUK.setTableName("KUMULORG");
    kuorgPORUK.setServerColumnName("PORUK");
    kuorgPORUK.setSqlType(2);
    kuorgPORUK.setDefault("0");
    kuorgPRIR.setCaption("Prirez");
    kuorgPRIR.setColumnName("PRIR");
    kuorgPRIR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPRIR.setPrecision(17);
    kuorgPRIR.setScale(2);
    kuorgPRIR.setDisplayMask("###,###,##0.00");
    kuorgPRIR.setDefault("0");
    kuorgPRIR.setTableName("KUMULORG");
    kuorgPRIR.setServerColumnName("PRIR");
    kuorgPRIR.setSqlType(2);
    kuorgPRIR.setDefault("0");
    kuorgPORIPRIR.setCaption("Porez i prirez");
    kuorgPORIPRIR.setColumnName("PORIPRIR");
    kuorgPORIPRIR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgPORIPRIR.setPrecision(17);
    kuorgPORIPRIR.setScale(2);
    kuorgPORIPRIR.setDisplayMask("###,###,##0.00");
    kuorgPORIPRIR.setDefault("0");
    kuorgPORIPRIR.setTableName("KUMULORG");
    kuorgPORIPRIR.setServerColumnName("PORIPRIR");
    kuorgPORIPRIR.setSqlType(2);
    kuorgPORIPRIR.setDefault("0");
    kuorgNETO2.setCaption("Iznos nakon oporezivanja");
    kuorgNETO2.setColumnName("NETO2");
    kuorgNETO2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgNETO2.setPrecision(17);
    kuorgNETO2.setScale(2);
    kuorgNETO2.setDisplayMask("###,###,##0.00");
    kuorgNETO2.setDefault("0");
    kuorgNETO2.setTableName("KUMULORG");
    kuorgNETO2.setServerColumnName("NETO2");
    kuorgNETO2.setSqlType(2);
    kuorgNETO2.setDefault("0");
    kuorgNAKNADE.setCaption("Naknade");
    kuorgNAKNADE.setColumnName("NAKNADE");
    kuorgNAKNADE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgNAKNADE.setPrecision(17);
    kuorgNAKNADE.setScale(2);
    kuorgNAKNADE.setDisplayMask("###,###,##0.00");
    kuorgNAKNADE.setDefault("0");
    kuorgNAKNADE.setTableName("KUMULORG");
    kuorgNAKNADE.setServerColumnName("NAKNADE");
    kuorgNAKNADE.setSqlType(2);
    kuorgNAKNADE.setDefault("0");
    kuorgNETOPK.setCaption("Iznos prije kredita");
    kuorgNETOPK.setColumnName("NETOPK");
    kuorgNETOPK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgNETOPK.setPrecision(17);
    kuorgNETOPK.setScale(2);
    kuorgNETOPK.setDisplayMask("###,###,##0.00");
    kuorgNETOPK.setDefault("0");
    kuorgNETOPK.setTableName("KUMULORG");
    kuorgNETOPK.setServerColumnName("NETOPK");
    kuorgNETOPK.setSqlType(2);
    kuorgNETOPK.setDefault("0");
    kuorgKREDITI.setCaption("Iznos kredita");
    kuorgKREDITI.setColumnName("KREDITI");
    kuorgKREDITI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgKREDITI.setPrecision(17);
    kuorgKREDITI.setScale(2);
    kuorgKREDITI.setDisplayMask("###,###,##0.00");
    kuorgKREDITI.setDefault("0");
    kuorgKREDITI.setTableName("KUMULORG");
    kuorgKREDITI.setServerColumnName("KREDITI");
    kuorgKREDITI.setSqlType(2);
    kuorgKREDITI.setDefault("0");
    kuorgNARUKE.setCaption("Za isplatu");
    kuorgNARUKE.setColumnName("NARUKE");
    kuorgNARUKE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgNARUKE.setPrecision(17);
    kuorgNARUKE.setScale(2);
    kuorgNARUKE.setDisplayMask("###,###,##0.00");
    kuorgNARUKE.setDefault("0");
    kuorgNARUKE.setTableName("KUMULORG");
    kuorgNARUKE.setServerColumnName("NARUKE");
    kuorgNARUKE.setSqlType(2);
    kuorgNARUKE.setDefault("0");
    kuorgDOPRPOD.setCaption("Doprinosi poduze\u0107a");
    kuorgDOPRPOD.setColumnName("DOPRPOD");
    kuorgDOPRPOD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuorgDOPRPOD.setPrecision(17);
    kuorgDOPRPOD.setScale(2);
    kuorgDOPRPOD.setDisplayMask("###,###,##0.00");
    kuorgDOPRPOD.setDefault("0");
    kuorgDOPRPOD.setTableName("KUMULORG");
    kuorgDOPRPOD.setServerColumnName("DOPRPOD");
    kuorgDOPRPOD.setSqlType(2);
    kuorgDOPRPOD.setDefault("0");
    kuorg.setResolver(dm.getQresolver());
    kuorg.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Kumulorg", null, true, Load.ALL));
 setColumns(new Column[] {kuorgLOKK, kuorgAKTIV, kuorgCORG, kuorgCVRO, kuorgKNJIG, kuorgSATI, kuorgBRUTO, kuorgDOPRINOSI, kuorgNETO, kuorgNEOP,
        kuorgISKNEOP, kuorgPOROSN, kuorgPOR1, kuorgPOR2, kuorgPOR3, kuorgPOR4, kuorgPOR5, kuorgPORUK, kuorgPRIR, kuorgPORIPRIR, kuorgNETO2, kuorgNAKNADE,
        kuorgNETOPK, kuorgKREDITI, kuorgNARUKE, kuorgDOPRPOD});
  }

  public void setall() {

    ddl.create("Kumulorg")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("cvro", 6, true)
       .addChar("knjig", 12)
       .addFloat("sati", 17, 2)
       .addFloat("bruto", 17, 2)
       .addFloat("doprinosi", 17, 2)
       .addFloat("neto", 17, 2)
       .addFloat("neop", 17, 2)
       .addFloat("iskneop", 17, 2)
       .addFloat("porosn", 17, 2)
       .addFloat("por1", 17, 2)
       .addFloat("por2", 17, 2)
       .addFloat("por3", 17, 2)
       .addFloat("por4", 17, 2)
       .addFloat("por5", 17, 2)
       .addFloat("poruk", 17, 2)
       .addFloat("prir", 17, 2)
       .addFloat("poriprir", 17, 2)
       .addFloat("neto2", 17, 2)
       .addFloat("naknade", 17, 2)
       .addFloat("netopk", 17, 2)
       .addFloat("krediti", 17, 2)
       .addFloat("naruke", 17, 2)
       .addFloat("doprpod", 17, 2)
       .addPrimaryKey("corg,cvro");


    Naziv = "Kumulorg";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
