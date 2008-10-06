/****license*****************************************************************
**   file: Kumulrad.java
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



public class Kumulrad extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Kumulrad Kumulradclass;

  QueryDataSet kurad = new raDataSet();

  Column kuradLOKK = new Column();
  Column kuradAKTIV = new Column();
  Column kuradCRADNIK = new Column();
  Column kuradSATI = new Column();
  Column kuradBRUTO = new Column();
  Column kuradDOPRINOSI = new Column();
  Column kuradNETO = new Column();
  Column kuradNEOP = new Column();
  Column kuradISKNEOP = new Column();
  Column kuradPOROSN = new Column();
  Column kuradPOR1 = new Column();
  Column kuradPOR2 = new Column();
  Column kuradPOR3 = new Column();
  Column kuradPOR4 = new Column();
  Column kuradPOR5 = new Column();
  Column kuradPORUK = new Column();
  Column kuradPRIR = new Column();
  Column kuradPORIPRIR = new Column();
  Column kuradNETO2 = new Column();
  Column kuradNAKNADE = new Column();
  Column kuradNETOPK = new Column();
  Column kuradKREDITI = new Column();
  Column kuradNARUKE = new Column();

  public static Kumulrad getDataModule() {
    if (Kumulradclass == null) {
      Kumulradclass = new Kumulrad();
    }
    return Kumulradclass;
  }

  public QueryDataSet getQueryDataSet() {
    return kurad;
  }

  public Kumulrad() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    kuradLOKK.setCaption("Status zauzetosti");
    kuradLOKK.setColumnName("LOKK");
    kuradLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kuradLOKK.setPrecision(1);
    kuradLOKK.setTableName("KUMULRAD");
    kuradLOKK.setServerColumnName("LOKK");
    kuradLOKK.setSqlType(1);
    kuradLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kuradLOKK.setDefault("N");
    kuradAKTIV.setCaption("Aktivan - neaktivan");
    kuradAKTIV.setColumnName("AKTIV");
    kuradAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    kuradAKTIV.setPrecision(1);
    kuradAKTIV.setTableName("KUMULRAD");
    kuradAKTIV.setServerColumnName("AKTIV");
    kuradAKTIV.setSqlType(1);
    kuradAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kuradAKTIV.setDefault("D");
    kuradCRADNIK.setCaption("Radnik");
    kuradCRADNIK.setColumnName("CRADNIK");
    kuradCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kuradCRADNIK.setPrecision(6);
    kuradCRADNIK.setRowId(true);
    kuradCRADNIK.setTableName("KUMULRAD");
    kuradCRADNIK.setServerColumnName("CRADNIK");
    kuradCRADNIK.setSqlType(1);
    kuradSATI.setCaption("Sati");
    kuradSATI.setColumnName("SATI");
    kuradSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradSATI.setPrecision(17);
    kuradSATI.setScale(2);
    kuradSATI.setDisplayMask("###,###,##0.00");
    kuradSATI.setDefault("0");
    kuradSATI.setTableName("KUMULRAD");
    kuradSATI.setServerColumnName("SATI");
    kuradSATI.setSqlType(2);
    kuradSATI.setDefault("0");
    kuradBRUTO.setCaption("Bruto");
    kuradBRUTO.setColumnName("BRUTO");
    kuradBRUTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradBRUTO.setPrecision(17);
    kuradBRUTO.setScale(2);
    kuradBRUTO.setDisplayMask("###,###,##0.00");
    kuradBRUTO.setDefault("0");
    kuradBRUTO.setTableName("KUMULRAD");
    kuradBRUTO.setServerColumnName("BRUTO");
    kuradBRUTO.setSqlType(2);
    kuradBRUTO.setDefault("0");
    kuradDOPRINOSI.setCaption("Doprinosi");
    kuradDOPRINOSI.setColumnName("DOPRINOSI");
    kuradDOPRINOSI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradDOPRINOSI.setPrecision(17);
    kuradDOPRINOSI.setScale(2);
    kuradDOPRINOSI.setDisplayMask("###,###,##0.00");
    kuradDOPRINOSI.setDefault("0");
    kuradDOPRINOSI.setTableName("KUMULRAD");
    kuradDOPRINOSI.setServerColumnName("DOPRINOSI");
    kuradDOPRINOSI.setSqlType(2);
    kuradDOPRINOSI.setDefault("0");
    kuradNETO.setCaption("Bruto po odbitku doprinosa");
    kuradNETO.setColumnName("NETO");
    kuradNETO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradNETO.setPrecision(17);
    kuradNETO.setScale(2);
    kuradNETO.setDisplayMask("###,###,##0.00");
    kuradNETO.setDefault("0");
    kuradNETO.setTableName("KUMULRAD");
    kuradNETO.setServerColumnName("NETO");
    kuradNETO.setSqlType(2);
    kuradNETO.setDefault("0");
    kuradNEOP.setCaption("Neoporezivi dio - olakšica");
    kuradNEOP.setColumnName("NEOP");
    kuradNEOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradNEOP.setPrecision(17);
    kuradNEOP.setScale(2);
    kuradNEOP.setDisplayMask("###,###,##0.00");
    kuradNEOP.setDefault("0");
    kuradNEOP.setTableName("KUMULRAD");
    kuradNEOP.setServerColumnName("NEOP");
    kuradNEOP.setSqlType(2);
    kuradNEOP.setDefault("0");
    kuradISKNEOP.setCaption("Iskorišteni neoporezivi dio");
    kuradISKNEOP.setColumnName("ISKNEOP");
    kuradISKNEOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradISKNEOP.setPrecision(17);
    kuradISKNEOP.setScale(2);
    kuradISKNEOP.setDisplayMask("###,###,##0.00");
    kuradISKNEOP.setDefault("0");
    kuradISKNEOP.setTableName("KUMULRAD");
    kuradISKNEOP.setServerColumnName("ISKNEOP");
    kuradISKNEOP.setSqlType(2);
    kuradISKNEOP.setDefault("0");
    kuradPOROSN.setCaption("Porezna osnovica");
    kuradPOROSN.setColumnName("POROSN");
    kuradPOROSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPOROSN.setPrecision(17);
    kuradPOROSN.setScale(2);
    kuradPOROSN.setDisplayMask("###,###,##0.00");
    kuradPOROSN.setDefault("0");
    kuradPOROSN.setTableName("KUMULRAD");
    kuradPOROSN.setServerColumnName("POROSN");
    kuradPOROSN.setSqlType(2);
    kuradPOROSN.setDefault("0");
    kuradPOR1.setCaption("Porez 1");
    kuradPOR1.setColumnName("POR1");
    kuradPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPOR1.setPrecision(17);
    kuradPOR1.setScale(2);
    kuradPOR1.setDisplayMask("###,###,##0.00");
    kuradPOR1.setDefault("0");
    kuradPOR1.setTableName("KUMULRAD");
    kuradPOR1.setServerColumnName("POR1");
    kuradPOR1.setSqlType(2);
    kuradPOR1.setDefault("0");
    kuradPOR2.setCaption("Porez 2");
    kuradPOR2.setColumnName("POR2");
    kuradPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPOR2.setPrecision(17);
    kuradPOR2.setScale(2);
    kuradPOR2.setDisplayMask("###,###,##0.00");
    kuradPOR2.setDefault("0");
    kuradPOR2.setTableName("KUMULRAD");
    kuradPOR2.setServerColumnName("POR2");
    kuradPOR2.setSqlType(2);
    kuradPOR2.setDefault("0");
    kuradPOR3.setCaption("Porez 3");
    kuradPOR3.setColumnName("POR3");
    kuradPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPOR3.setPrecision(17);
    kuradPOR3.setScale(2);
    kuradPOR3.setDisplayMask("###,###,##0.00");
    kuradPOR3.setDefault("0");
    kuradPOR3.setTableName("KUMULRAD");
    kuradPOR3.setServerColumnName("POR3");
    kuradPOR3.setSqlType(2);
    kuradPOR3.setDefault("0");
    kuradPOR4.setCaption("Porez 4");
    kuradPOR4.setColumnName("POR4");
    kuradPOR4.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPOR4.setPrecision(17);
    kuradPOR4.setScale(2);
    kuradPOR4.setDisplayMask("###,###,##0.00");
    kuradPOR4.setDefault("0");
    kuradPOR4.setTableName("KUMULRAD");
    kuradPOR4.setServerColumnName("POR4");
    kuradPOR4.setSqlType(2);
    kuradPOR4.setDefault("0");
    kuradPOR5.setCaption("Porez 5 (invisible)");
    kuradPOR5.setColumnName("POR5");
    kuradPOR5.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPOR5.setPrecision(17);
    kuradPOR5.setScale(2);
    kuradPOR5.setDisplayMask("###,###,##0.00");
    kuradPOR5.setDefault("0");
    kuradPOR5.setTableName("KUMULRAD");
    kuradPOR5.setServerColumnName("POR5");
    kuradPOR5.setSqlType(2);
    kuradPOR5.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kuradPOR5.setDefault("0");
    kuradPORUK.setCaption("Porez ukupno");
    kuradPORUK.setColumnName("PORUK");
    kuradPORUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPORUK.setPrecision(17);
    kuradPORUK.setScale(2);
    kuradPORUK.setDisplayMask("###,###,##0.00");
    kuradPORUK.setDefault("0");
    kuradPORUK.setTableName("KUMULRAD");
    kuradPORUK.setServerColumnName("PORUK");
    kuradPORUK.setSqlType(2);
    kuradPORUK.setDefault("0");
    kuradPRIR.setCaption("Prirez");
    kuradPRIR.setColumnName("PRIR");
    kuradPRIR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPRIR.setPrecision(17);
    kuradPRIR.setScale(2);
    kuradPRIR.setDisplayMask("###,###,##0.00");
    kuradPRIR.setDefault("0");
    kuradPRIR.setTableName("KUMULRAD");
    kuradPRIR.setServerColumnName("PRIR");
    kuradPRIR.setSqlType(2);
    kuradPRIR.setDefault("0");
    kuradPORIPRIR.setCaption("Porez i prirez");
    kuradPORIPRIR.setColumnName("PORIPRIR");
    kuradPORIPRIR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradPORIPRIR.setPrecision(17);
    kuradPORIPRIR.setScale(2);
    kuradPORIPRIR.setDisplayMask("###,###,##0.00");
    kuradPORIPRIR.setDefault("0");
    kuradPORIPRIR.setTableName("KUMULRAD");
    kuradPORIPRIR.setServerColumnName("PORIPRIR");
    kuradPORIPRIR.setSqlType(2);
    kuradPORIPRIR.setDefault("0");
    kuradNETO2.setCaption("Iznos nakon oporezivanja");
    kuradNETO2.setColumnName("NETO2");
    kuradNETO2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradNETO2.setPrecision(17);
    kuradNETO2.setScale(2);
    kuradNETO2.setDisplayMask("###,###,##0.00");
    kuradNETO2.setDefault("0");
    kuradNETO2.setTableName("KUMULRAD");
    kuradNETO2.setServerColumnName("NETO2");
    kuradNETO2.setSqlType(2);
    kuradNETO2.setDefault("0");
    kuradNAKNADE.setCaption("Naknade");
    kuradNAKNADE.setColumnName("NAKNADE");
    kuradNAKNADE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradNAKNADE.setPrecision(17);
    kuradNAKNADE.setScale(2);
    kuradNAKNADE.setDisplayMask("###,###,##0.00");
    kuradNAKNADE.setDefault("0");
    kuradNAKNADE.setTableName("KUMULRAD");
    kuradNAKNADE.setServerColumnName("NAKNADE");
    kuradNAKNADE.setSqlType(2);
    kuradNAKNADE.setDefault("0");
    kuradNETOPK.setCaption("Iznos prije kredita");
    kuradNETOPK.setColumnName("NETOPK");
    kuradNETOPK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradNETOPK.setPrecision(17);
    kuradNETOPK.setScale(2);
    kuradNETOPK.setDisplayMask("###,###,##0.00");
    kuradNETOPK.setDefault("0");
    kuradNETOPK.setTableName("KUMULRAD");
    kuradNETOPK.setServerColumnName("NETOPK");
    kuradNETOPK.setSqlType(2);
    kuradNETOPK.setDefault("0");
    kuradKREDITI.setCaption("Iznos kredita");
    kuradKREDITI.setColumnName("KREDITI");
    kuradKREDITI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradKREDITI.setPrecision(17);
    kuradKREDITI.setScale(2);
    kuradKREDITI.setDisplayMask("###,###,##0.00");
    kuradKREDITI.setDefault("0");
    kuradKREDITI.setTableName("KUMULRAD");
    kuradKREDITI.setServerColumnName("KREDITI");
    kuradKREDITI.setSqlType(2);
    kuradKREDITI.setDefault("0");
    kuradNARUKE.setCaption("Za isplatu");
    kuradNARUKE.setColumnName("NARUKE");
    kuradNARUKE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kuradNARUKE.setPrecision(17);
    kuradNARUKE.setScale(2);
    kuradNARUKE.setDisplayMask("###,###,##0.00");
    kuradNARUKE.setDefault("0");
    kuradNARUKE.setTableName("KUMULRAD");
    kuradNARUKE.setServerColumnName("NARUKE");
    kuradNARUKE.setSqlType(2);
    kuradNARUKE.setDefault("0");
    kurad.setResolver(dm.getQresolver());
    kurad.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Kumulrad", null, true, Load.ALL));
 setColumns(new Column[] {kuradLOKK, kuradAKTIV, kuradCRADNIK, kuradSATI, kuradBRUTO, kuradDOPRINOSI, kuradNETO, kuradNEOP, kuradISKNEOP, kuradPOROSN,
        kuradPOR1, kuradPOR2, kuradPOR3, kuradPOR4, kuradPOR5, kuradPORUK, kuradPRIR, kuradPORIPRIR, kuradNETO2, kuradNAKNADE, kuradNETOPK, kuradKREDITI,
        kuradNARUKE});
  }

  public void setall() {

    ddl.create("Kumulrad")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cradnik", 6, true)
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
       .addPrimaryKey("cradnik");


    Naziv = "Kumulrad";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
