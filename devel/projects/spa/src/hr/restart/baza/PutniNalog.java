/****license*****************************************************************
**   file: PutniNalog.java
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



public class PutniNalog extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static PutniNalog PutniNalogclass;

  QueryDataSet pnal = new raDataSet();

  Column pnalLOKK = new Column();
  Column pnalAKTIV = new Column();
  Column pnalKNJIG = new Column();
  Column pnalGODINA = new Column();
  Column pnalBROJ = new Column();
  Column pnalCPN = new Column();
  Column pnalCRADNIK = new Column();
  Column pnalDATUMODL = new Column();
  Column pnalDATOBR = new Column();
  Column pnalTRAJANJE = new Column();
  Column pnalRAZLOGPUTA = new Column();
  Column pnalMJESTA = new Column();
  Column pnalCPRIJSRED = new Column();
  Column pnalINDPUTA = new Column();
  Column pnalSTATUS = new Column();
  Column pnalAKONTACIJA = new Column();
  Column pnalTROSKOVI = new Column();
  Column pnalRAZLIKA = new Column();
  Column pnalUPLRAZLIKA = new Column();
  Column pnalTECRAZLIKA = new Column();
  Column pnalCZEMLJE = new Column();
  Column pnalCORG = new Column();

  public static PutniNalog getDataModule() {
    if (PutniNalogclass == null) {
      PutniNalogclass = new PutniNalog();
    }
    return PutniNalogclass;
  }

  public QueryDataSet getQueryDataSet() {
    return pnal;
  }

  public PutniNalog() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    pnalLOKK.setCaption("Status zauzetosti");
    pnalLOKK.setColumnName("LOKK");
    pnalLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalLOKK.setPrecision(1);
    pnalLOKK.setTableName("PUTNINALOG");
    pnalLOKK.setServerColumnName("LOKK");
    pnalLOKK.setSqlType(1);
    pnalLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pnalLOKK.setDefault("N");
    pnalAKTIV.setCaption("Aktivan - neaktivan");
    pnalAKTIV.setColumnName("AKTIV");
    pnalAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalAKTIV.setPrecision(1);
    pnalAKTIV.setTableName("PUTNINALOG");
    pnalAKTIV.setServerColumnName("AKTIV");
    pnalAKTIV.setSqlType(1);
    pnalAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pnalAKTIV.setDefault("D");
    pnalKNJIG.setCaption("Knjigovodstvo");
    pnalKNJIG.setColumnName("KNJIG");
    pnalKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalKNJIG.setPrecision(12);
    pnalKNJIG.setRowId(true);
    pnalKNJIG.setTableName("PUTNINALOG");
    pnalKNJIG.setServerColumnName("KNJIG");
    pnalKNJIG.setSqlType(1);
    pnalGODINA.setCaption("Godina");
    pnalGODINA.setColumnName("GODINA");
    pnalGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pnalGODINA.setPrecision(4);
    pnalGODINA.setRowId(true);
    pnalGODINA.setTableName("PUTNINALOG");
    pnalGODINA.setServerColumnName("GODINA");
    pnalGODINA.setSqlType(5);
    pnalGODINA.setWidth(4);
    pnalBROJ.setCaption("Broj putnog naloga");
    pnalBROJ.setColumnName("BROJ");
    pnalBROJ.setDataType(com.borland.dx.dataset.Variant.INT);
    pnalBROJ.setPrecision(6);
    pnalBROJ.setRowId(true);
    pnalBROJ.setTableName("PUTNINALOG");
    pnalBROJ.setServerColumnName("BROJ");
    pnalBROJ.setSqlType(4);
    pnalBROJ.setWidth(6);
    pnalCPN.setCaption("Broj naloga");
    pnalCPN.setColumnName("CPN");
    pnalCPN.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalCPN.setPrecision(24);
    pnalCPN.setTableName("PUTNINALOG");
    pnalCPN.setServerColumnName("CPN");
    pnalCPN.setSqlType(1);
    pnalCRADNIK.setCaption("Radnik");
    pnalCRADNIK.setColumnName("CRADNIK");
    pnalCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalCRADNIK.setPrecision(6);
    pnalCRADNIK.setTableName("PUTNINALOG");
    pnalCRADNIK.setServerColumnName("CRADNIK");
    pnalCRADNIK.setSqlType(1);
    pnalDATUMODL.setCaption("Odlazak");
    pnalDATUMODL.setColumnName("DATUMODL");
    pnalDATUMODL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    pnalDATUMODL.setPrecision(8);
    pnalDATUMODL.setDisplayMask("dd-MM-yyyy");
//    pnalDATUMODL.setEditMask("dd-MM-yyyy");
    pnalDATUMODL.setTableName("PUTNINALOG");
    pnalDATUMODL.setServerColumnName("DATUMODL");
    pnalDATUMODL.setSqlType(93);
    pnalDATUMODL.setWidth(10);
    pnalDATOBR.setCaption("Datum obra\u010Duna");
    pnalDATOBR.setColumnName("DATOBR");
    pnalDATOBR.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    pnalDATOBR.setPrecision(8);
    pnalDATOBR.setDisplayMask("dd-MM-yyyy");
//    pnalDATOBR.setEditMask("dd-MM-yyyy");
    pnalDATOBR.setTableName("PUTNINALOG");
    pnalDATOBR.setServerColumnName("DATOBR");
    pnalDATOBR.setSqlType(93);
    pnalDATOBR.setWidth(10);
    pnalTRAJANJE.setCaption("Trajanje");
    pnalTRAJANJE.setColumnName("TRAJANJE");
    pnalTRAJANJE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pnalTRAJANJE.setPrecision(4);
    pnalTRAJANJE.setTableName("PUTNINALOG");
    pnalTRAJANJE.setServerColumnName("TRAJANJE");
    pnalTRAJANJE.setSqlType(5);
    pnalTRAJANJE.setWidth(4);
    pnalTRAJANJE.setDefault("1");
    pnalRAZLOGPUTA.setCaption("Razlog puta");
    pnalRAZLOGPUTA.setColumnName("RAZLOGPUTA");
    pnalRAZLOGPUTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalRAZLOGPUTA.setPrecision(50);
    pnalRAZLOGPUTA.setTableName("PUTNINALOG");
    pnalRAZLOGPUTA.setServerColumnName("RAZLOGPUTA");
    pnalRAZLOGPUTA.setSqlType(1);
    pnalRAZLOGPUTA.setWidth(30);
    pnalMJESTA.setCaption("Mjesta");
    pnalMJESTA.setColumnName("MJESTA");
    pnalMJESTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalMJESTA.setPrecision(50);
    pnalMJESTA.setTableName("PUTNINALOG");
    pnalMJESTA.setServerColumnName("MJESTA");
    pnalMJESTA.setSqlType(1);
    pnalMJESTA.setWidth(30);
    pnalCPRIJSRED.setCaption("Prijevozno sredstvo");
    pnalCPRIJSRED.setColumnName("CPRIJSRED");
    pnalCPRIJSRED.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalCPRIJSRED.setPrecision(5);
    pnalCPRIJSRED.setTableName("PUTNINALOG");
    pnalCPRIJSRED.setServerColumnName("CPRIJSRED");
    pnalCPRIJSRED.setSqlType(1);
    pnalINDPUTA.setCaption("Indikator puta");
    pnalINDPUTA.setColumnName("INDPUTA");
    pnalINDPUTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalINDPUTA.setPrecision(1);
    pnalINDPUTA.setRowId(true);
    pnalINDPUTA.setTableName("PUTNINALOG");
    pnalINDPUTA.setServerColumnName("INDPUTA");
    pnalINDPUTA.setSqlType(1);
    pnalINDPUTA.setDefault("Z");
    pnalSTATUS.setCaption("Status");
    pnalSTATUS.setColumnName("STATUS");
    pnalSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalSTATUS.setPrecision(1);
    pnalSTATUS.setTableName("PUTNINALOG");
    pnalSTATUS.setServerColumnName("STATUS");
    pnalSTATUS.setSqlType(1);
    pnalSTATUS.setDefault("P");
    pnalAKONTACIJA.setCaption("Ispla\u0107ena akontacija");
    pnalAKONTACIJA.setColumnName("AKONTACIJA");
    pnalAKONTACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnalAKONTACIJA.setPrecision(17);
    pnalAKONTACIJA.setScale(2);
    pnalAKONTACIJA.setDisplayMask("###,###,##0.00");
    pnalAKONTACIJA.setDefault("0");
    pnalAKONTACIJA.setTableName("PUTNINALOG");
    pnalAKONTACIJA.setServerColumnName("AKONTACIJA");
    pnalAKONTACIJA.setSqlType(2);
    pnalAKONTACIJA.setDefault("0");
    pnalTROSKOVI.setCaption("Obra\u010Dunati troškovi");
    pnalTROSKOVI.setColumnName("TROSKOVI");
    pnalTROSKOVI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnalTROSKOVI.setPrecision(17);
    pnalTROSKOVI.setScale(2);
    pnalTROSKOVI.setDisplayMask("###,###,##0.00");
    pnalTROSKOVI.setDefault("0");
    pnalTROSKOVI.setTableName("PUTNINALOG");
    pnalTROSKOVI.setServerColumnName("TROSKOVI");
    pnalTROSKOVI.setSqlType(2);
    pnalTROSKOVI.setDefault("0");
    pnalRAZLIKA.setCaption("Razlika po obra\u010Dunu");
    pnalRAZLIKA.setColumnName("RAZLIKA");
    pnalRAZLIKA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnalRAZLIKA.setPrecision(17);
    pnalRAZLIKA.setScale(2);
    pnalRAZLIKA.setDisplayMask("###,###,##0.00");
    pnalRAZLIKA.setDefault("0");
    pnalRAZLIKA.setTableName("PUTNINALOG");
    pnalRAZLIKA.setServerColumnName("RAZLIKA");
    pnalRAZLIKA.setSqlType(2);
    pnalRAZLIKA.setDefault("0");
    pnalUPLRAZLIKA.setCaption("Upla\u0107ena razlika po obra\u010Dunu");
    pnalUPLRAZLIKA.setColumnName("UPLRAZLIKA");
    pnalUPLRAZLIKA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnalUPLRAZLIKA.setPrecision(17);
    pnalUPLRAZLIKA.setScale(2);
    pnalUPLRAZLIKA.setDisplayMask("###,###,##0.00");
    pnalUPLRAZLIKA.setDefault("0");
    pnalUPLRAZLIKA.setTableName("PUTNINALOG");
    pnalUPLRAZLIKA.setServerColumnName("UPLRAZLIKA");
    pnalUPLRAZLIKA.setSqlType(2);
    pnalUPLRAZLIKA.setDefault("0");
    pnalTECRAZLIKA.setCaption("Te\u010Dajna razlika");
    pnalTECRAZLIKA.setColumnName("TECRAZLIKA");
    pnalTECRAZLIKA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnalTECRAZLIKA.setPrecision(17);
    pnalTECRAZLIKA.setScale(2);
    pnalTECRAZLIKA.setDisplayMask("###,###,##0.00");
    pnalTECRAZLIKA.setDefault("0");
    pnalTECRAZLIKA.setTableName("PUTNINALOG");
    pnalTECRAZLIKA.setServerColumnName("TECRAZLIKA");
    pnalTECRAZLIKA.setSqlType(2);
    pnalTECRAZLIKA.setDefault("0");
    pnalCZEMLJE.setCaption("Zemlja");
    pnalCZEMLJE.setColumnName("CZEMLJE");
    pnalCZEMLJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalCZEMLJE.setPrecision(3);
    pnalCZEMLJE.setTableName("PUTNINALOG");
    pnalCZEMLJE.setServerColumnName("CZEMLJE");
    pnalCZEMLJE.setSqlType(1);
    pnalCORG.setCaption("Org. jedinica");
    pnalCORG.setColumnName("CORG");
    pnalCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnalCORG.setPrecision(12);
    pnalCORG.setTableName("PUTNINALOG");
    pnalCORG.setServerColumnName("CORG");
    pnalCORG.setSqlType(1);
    pnal.setResolver(dm.getQresolver());
    pnal.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from PutniNalog", null, true, Load.ALL));
 setColumns(new Column[] {pnalLOKK, pnalAKTIV, pnalKNJIG, pnalGODINA, pnalBROJ, pnalCPN, pnalCRADNIK, pnalDATUMODL, pnalDATOBR, pnalTRAJANJE,
        pnalRAZLOGPUTA, pnalMJESTA, pnalCPRIJSRED, pnalINDPUTA, pnalSTATUS, pnalAKONTACIJA, pnalTROSKOVI, pnalRAZLIKA, pnalUPLRAZLIKA, pnalTECRAZLIKA,
        pnalCZEMLJE, pnalCORG});
  }

  public void setall() {

    ddl.create("PutniNalog")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addShort("godina", 4, true)
       .addInteger("broj", 6, true)
       .addChar("cpn", 24)
       .addChar("cradnik", 6)
       .addDate("datumodl")
       .addDate("datobr")
       .addShort("trajanje", 4)
       .addChar("razlogputa", 50)
       .addChar("mjesta", 50)
       .addChar("cprijsred", 5)
       .addChar("indputa", 1, "Z", true)
       .addChar("status", 1, "P")
       .addFloat("akontacija", 17, 2)
       .addFloat("troskovi", 17, 2)
       .addFloat("razlika", 17, 2)
       .addFloat("uplrazlika", 17, 2)
       .addFloat("tecrazlika", 17, 2)
       .addChar("czemlje", 3)
       .addChar("corg", 12)
       .addPrimaryKey("knjig,godina,broj,indputa");


    Naziv = "PutniNalog";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"broj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
