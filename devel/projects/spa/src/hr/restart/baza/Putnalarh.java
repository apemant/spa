/****license*****************************************************************
**   file: Putnalarh.java
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



public class Putnalarh extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Putnalarh Putnalarhclass;

  QueryDataSet pnarh = new QueryDataSet();

  Column pnarhLOKK = new Column();
  Column pnarhAKTIV = new Column();
  Column pnarhKNJIG = new Column();
  Column pnarhGODINA = new Column();
  Column pnarhBROJ = new Column();
  Column pnarhCPN = new Column();
  Column pnarhCRADNIK = new Column();
  Column pnarhDATUMODL = new Column();
  Column pnarhDATOBR = new Column();
  Column pnarhTRAJANJE = new Column();
  Column pnarhRAZLOGPUTA = new Column();
  Column pnarhMJESTA = new Column();
  Column pnarhCPRIJSRED = new Column();
  Column pnarhINDPUTA = new Column();
  Column pnarhSTATUS = new Column();
  Column pnarhAKONTACIJA = new Column();
  Column pnarhTROSKOVI = new Column();
  Column pnarhRAZLIKA = new Column();
  Column pnarhUPLRAZLIKA = new Column();
  Column pnarhTECRAZLIKA = new Column();
  Column pnarhCZEMLJE = new Column();
  Column pnarhCORG = new Column();
  Column pnarhCNALOGA = new Column();

  public static Putnalarh getDataModule() {
    if (Putnalarhclass == null) {
      Putnalarhclass = new Putnalarh();
    }
    return Putnalarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return pnarh;
  }

  public Putnalarh() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    pnarhLOKK.setCaption("Status zauzetosti");
    pnarhLOKK.setColumnName("LOKK");
    pnarhLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhLOKK.setPrecision(1);
    pnarhLOKK.setTableName("PUTNALARH");
    pnarhLOKK.setServerColumnName("LOKK");
    pnarhLOKK.setSqlType(1);
    pnarhLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pnarhLOKK.setDefault("N");
    pnarhAKTIV.setCaption("Aktivan - neaktivan");
    pnarhAKTIV.setColumnName("AKTIV");
    pnarhAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhAKTIV.setPrecision(1);
    pnarhAKTIV.setTableName("PUTNALARH");
    pnarhAKTIV.setServerColumnName("AKTIV");
    pnarhAKTIV.setSqlType(1);
    pnarhAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pnarhAKTIV.setDefault("D");
    pnarhKNJIG.setCaption("Knjigovodstvo");
    pnarhKNJIG.setColumnName("KNJIG");
    pnarhKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhKNJIG.setPrecision(12);
    pnarhKNJIG.setRowId(true);
    pnarhKNJIG.setTableName("PUTNALARH");
    pnarhKNJIG.setServerColumnName("KNJIG");
    pnarhKNJIG.setSqlType(1);
    pnarhGODINA.setCaption("Godina");
    pnarhGODINA.setColumnName("GODINA");
    pnarhGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pnarhGODINA.setPrecision(4);
    pnarhGODINA.setRowId(true);
    pnarhGODINA.setTableName("PUTNALARH");
    pnarhGODINA.setServerColumnName("GODINA");
    pnarhGODINA.setSqlType(5);
    pnarhGODINA.setWidth(4);
    pnarhBROJ.setCaption("Broj putnog naloga");
    pnarhBROJ.setColumnName("BROJ");
    pnarhBROJ.setDataType(com.borland.dx.dataset.Variant.INT);
    pnarhBROJ.setPrecision(6);
    pnarhBROJ.setRowId(true);
    pnarhBROJ.setTableName("PUTNALARH");
    pnarhBROJ.setServerColumnName("BROJ");
    pnarhBROJ.setSqlType(4);
    pnarhBROJ.setWidth(6);
    pnarhCPN.setCaption("Broj naloga");
    pnarhCPN.setColumnName("CPN");
    pnarhCPN.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhCPN.setPrecision(24);
    pnarhCPN.setTableName("PUTNALARH");
    pnarhCPN.setServerColumnName("CPN");
    pnarhCPN.setSqlType(1);
    pnarhCRADNIK.setCaption("Radnik");
    pnarhCRADNIK.setColumnName("CRADNIK");
    pnarhCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhCRADNIK.setPrecision(6);
    pnarhCRADNIK.setTableName("PUTNALARH");
    pnarhCRADNIK.setServerColumnName("CRADNIK");
    pnarhCRADNIK.setSqlType(1);
    pnarhDATUMODL.setCaption("Datum odlaska");
    pnarhDATUMODL.setColumnName("DATUMODL");
    pnarhDATUMODL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    pnarhDATUMODL.setPrecision(8);
    pnarhDATUMODL.setDisplayMask("dd-MM-yyyy");
//    pnarhDATUMODL.setEditMask("dd-MM-yyyy");
    pnarhDATUMODL.setTableName("PUTNALARH");
    pnarhDATUMODL.setServerColumnName("DATUMODL");
    pnarhDATUMODL.setSqlType(93);
    pnarhDATUMODL.setWidth(10);
    pnarhDATOBR.setCaption("Datum obra\u010Duna");
    pnarhDATOBR.setColumnName("DATOBR");
    pnarhDATOBR.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    pnarhDATOBR.setPrecision(8);
    pnarhDATOBR.setDisplayMask("dd-MM-yyyy");
//    pnarhDATOBR.setEditMask("dd-MM-yyyy");
    pnarhDATOBR.setTableName("PUTNALARH");
    pnarhDATOBR.setServerColumnName("DATOBR");
    pnarhDATOBR.setSqlType(93);
    pnarhDATOBR.setWidth(10);
    pnarhTRAJANJE.setCaption("Trajanje");
    pnarhTRAJANJE.setColumnName("TRAJANJE");
    pnarhTRAJANJE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pnarhTRAJANJE.setPrecision(4);
    pnarhTRAJANJE.setTableName("PUTNALARH");
    pnarhTRAJANJE.setServerColumnName("TRAJANJE");
    pnarhTRAJANJE.setSqlType(5);
    pnarhTRAJANJE.setWidth(4);
    pnarhTRAJANJE.setDefault("1");
    pnarhRAZLOGPUTA.setCaption("Razlog puta");
    pnarhRAZLOGPUTA.setColumnName("RAZLOGPUTA");
    pnarhRAZLOGPUTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhRAZLOGPUTA.setPrecision(50);
    pnarhRAZLOGPUTA.setTableName("PUTNALARH");
    pnarhRAZLOGPUTA.setServerColumnName("RAZLOGPUTA");
    pnarhRAZLOGPUTA.setSqlType(1);
    pnarhRAZLOGPUTA.setWidth(30);
    pnarhMJESTA.setCaption("Mjesta");
    pnarhMJESTA.setColumnName("MJESTA");
    pnarhMJESTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhMJESTA.setPrecision(50);
    pnarhMJESTA.setTableName("PUTNALARH");
    pnarhMJESTA.setServerColumnName("MJESTA");
    pnarhMJESTA.setSqlType(1);
    pnarhMJESTA.setWidth(30);
    pnarhCPRIJSRED.setCaption("Prijevozno sredstvo");
    pnarhCPRIJSRED.setColumnName("CPRIJSRED");
    pnarhCPRIJSRED.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhCPRIJSRED.setPrecision(5);
    pnarhCPRIJSRED.setTableName("PUTNALARH");
    pnarhCPRIJSRED.setServerColumnName("CPRIJSRED");
    pnarhCPRIJSRED.setSqlType(1);
    pnarhINDPUTA.setCaption("Indikator puta");
    pnarhINDPUTA.setColumnName("INDPUTA");
    pnarhINDPUTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhINDPUTA.setPrecision(1);
    pnarhINDPUTA.setRowId(true);
    pnarhINDPUTA.setTableName("PUTNALARH");
    pnarhINDPUTA.setServerColumnName("INDPUTA");
    pnarhINDPUTA.setSqlType(1);
    pnarhINDPUTA.setDefault("Z");
    pnarhSTATUS.setCaption("Status");
    pnarhSTATUS.setColumnName("STATUS");
    pnarhSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhSTATUS.setPrecision(1);
    pnarhSTATUS.setTableName("PUTNALARH");
    pnarhSTATUS.setServerColumnName("STATUS");
    pnarhSTATUS.setSqlType(1);
    pnarhSTATUS.setDefault("P");
    pnarhAKONTACIJA.setCaption("Ispla\u0107ena akontacija");
    pnarhAKONTACIJA.setColumnName("AKONTACIJA");
    pnarhAKONTACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnarhAKONTACIJA.setPrecision(17);
    pnarhAKONTACIJA.setScale(2);
    pnarhAKONTACIJA.setDisplayMask("###,###,##0.00");
    pnarhAKONTACIJA.setDefault("0");
    pnarhAKONTACIJA.setTableName("PUTNALARH");
    pnarhAKONTACIJA.setServerColumnName("AKONTACIJA");
    pnarhAKONTACIJA.setSqlType(2);
    pnarhAKONTACIJA.setDefault("0");
    pnarhTROSKOVI.setCaption("Obra\u010Dunati troškovi");
    pnarhTROSKOVI.setColumnName("TROSKOVI");
    pnarhTROSKOVI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnarhTROSKOVI.setPrecision(17);
    pnarhTROSKOVI.setScale(2);
    pnarhTROSKOVI.setDisplayMask("###,###,##0.00");
    pnarhTROSKOVI.setDefault("0");
    pnarhTROSKOVI.setTableName("PUTNALARH");
    pnarhTROSKOVI.setServerColumnName("TROSKOVI");
    pnarhTROSKOVI.setSqlType(2);
    pnarhTROSKOVI.setDefault("0");
    pnarhRAZLIKA.setCaption("Razlika po obra\u010Dunu");
    pnarhRAZLIKA.setColumnName("RAZLIKA");
    pnarhRAZLIKA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnarhRAZLIKA.setPrecision(17);
    pnarhRAZLIKA.setScale(2);
    pnarhRAZLIKA.setDisplayMask("###,###,##0.00");
    pnarhRAZLIKA.setDefault("0");
    pnarhRAZLIKA.setTableName("PUTNALARH");
    pnarhRAZLIKA.setServerColumnName("RAZLIKA");
    pnarhRAZLIKA.setSqlType(2);
    pnarhRAZLIKA.setDefault("0");
    pnarhUPLRAZLIKA.setCaption("Upla\u0107ena razlika po obra\u010Dunu");
    pnarhUPLRAZLIKA.setColumnName("UPLRAZLIKA");
    pnarhUPLRAZLIKA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnarhUPLRAZLIKA.setPrecision(17);
    pnarhUPLRAZLIKA.setScale(2);
    pnarhUPLRAZLIKA.setDisplayMask("###,###,##0.00");
    pnarhUPLRAZLIKA.setDefault("0");
    pnarhUPLRAZLIKA.setTableName("PUTNALARH");
    pnarhUPLRAZLIKA.setServerColumnName("UPLRAZLIKA");
    pnarhUPLRAZLIKA.setSqlType(2);
    pnarhUPLRAZLIKA.setDefault("0");
    pnarhTECRAZLIKA.setCaption("Te\u010Dajna razlika");
    pnarhTECRAZLIKA.setColumnName("TECRAZLIKA");
    pnarhTECRAZLIKA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pnarhTECRAZLIKA.setPrecision(17);
    pnarhTECRAZLIKA.setScale(2);
    pnarhTECRAZLIKA.setDisplayMask("###,###,##0.00");
    pnarhTECRAZLIKA.setDefault("0");
    pnarhTECRAZLIKA.setTableName("PUTNALARH");
    pnarhTECRAZLIKA.setServerColumnName("TECRAZLIKA");
    pnarhTECRAZLIKA.setSqlType(2);
    pnarhTECRAZLIKA.setDefault("0");
    pnarhCZEMLJE.setCaption("Zemlja");
    pnarhCZEMLJE.setColumnName("CZEMLJE");
    pnarhCZEMLJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhCZEMLJE.setPrecision(3);
    pnarhCZEMLJE.setTableName("PUTNALARH");
    pnarhCZEMLJE.setServerColumnName("CZEMLJE");
    pnarhCZEMLJE.setSqlType(1);
    pnarhCORG.setCaption("Org. jedinica");
    pnarhCORG.setColumnName("CORG");
    pnarhCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhCORG.setPrecision(12);
    pnarhCORG.setTableName("PUTNALARH");
    pnarhCORG.setServerColumnName("CORG");
    pnarhCORG.setSqlType(1);
    pnarhCNALOGA.setCaption("Oznaka naloga u GK");
    pnarhCNALOGA.setColumnName("CNALOGA");
    pnarhCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pnarhCNALOGA.setPrecision(30);
    pnarhCNALOGA.setTableName("PUTNALARH");
    pnarhCNALOGA.setServerColumnName("CNALOGA");
    pnarhCNALOGA.setSqlType(1);
    pnarhCNALOGA.setWidth(30);
    pnarh.setResolver(dm.getQresolver());
    pnarh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Putnalarh", null, true, Load.ALL));
 setColumns(new Column[] {pnarhLOKK, pnarhAKTIV, pnarhKNJIG, pnarhGODINA, pnarhBROJ, pnarhCPN, pnarhCRADNIK, pnarhDATUMODL, pnarhDATOBR,
        pnarhTRAJANJE, pnarhRAZLOGPUTA, pnarhMJESTA, pnarhCPRIJSRED, pnarhINDPUTA, pnarhSTATUS, pnarhAKONTACIJA, pnarhTROSKOVI, pnarhRAZLIKA, pnarhUPLRAZLIKA,
        pnarhTECRAZLIKA, pnarhCZEMLJE, pnarhCORG, pnarhCNALOGA});
  }

  public void setall() {

    ddl.create("Putnalarh")
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
       .addChar("cnaloga", 30)
       .addPrimaryKey("knjig,godina,broj,indputa");


    Naziv = "Putnalarh";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"broj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
