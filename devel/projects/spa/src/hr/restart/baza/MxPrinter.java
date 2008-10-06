/****license*****************************************************************
**   file: MxPrinter.java
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



public class MxPrinter extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static MxPrinter MxPrinterclass;

  QueryDataSet mxpr = new raDataSet();

  Column mxprCPRINTER = new Column();
  Column mxprOPIS = new Column();
  Column mxprPAGESIZE = new Column();
  Column mxprPAGEWIDTH = new Column();
  Column mxprTOPMARGIN = new Column();
  Column mxprLEFTMARGIN = new Column();
  Column mxprBOTMARGIN = new Column();
  Column mxprLINEFEEDSEC = new Column();
  Column mxprLINEFEED = new Column();
  Column mxprPOMAKMM = new Column();
  Column mxprRESET = new Column();
  Column mxprNEWLINE = new Column();
  Column mxprCPI10 = new Column();
  Column mxprCPI12 = new Column();
  Column mxprCPI15 = new Column();
  Column mxprCPI17 = new Column();
  Column mxprCONDENSON = new Column();
  Column mxprCONDENSOFF = new Column();
  Column mxprDOUBLEHON = new Column();
  Column mxprDOUBLEHOFF = new Column();
  Column mxprDOUBLEWON = new Column();
  Column mxprDOUBLEWOFF = new Column();
  Column mxprLPI6 = new Column();
  Column mxprLPI8 = new Column();
  Column mxprLF_N180 = new Column();
  Column mxprLF_N360 = new Column();
  Column mxprPROPON = new Column();
  Column mxprPROPOFF = new Column();
  Column mxprENDOFPAGE = new Column();
  Column mxprUSER1 = new Column();
  Column mxprUSER2 = new Column();
  Column mxprUSER3 = new Column();
  Column mxprUSER4 = new Column();

  public static MxPrinter getDataModule() {
    if (MxPrinterclass == null) {
      MxPrinterclass = new MxPrinter();
    }
    return MxPrinterclass;
  }

  public QueryDataSet getQueryDataSet() {
    return mxpr;
  }

  public MxPrinter() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    mxprCPRINTER.setCaption("Šifra");
    mxprCPRINTER.setColumnName("CPRINTER");
    mxprCPRINTER.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprCPRINTER.setPrecision(6);
    mxprCPRINTER.setRowId(true);
    mxprCPRINTER.setTableName("MXPRINTER");
    mxprCPRINTER.setServerColumnName("CPRINTER");
    mxprCPRINTER.setSqlType(4);
    mxprCPRINTER.setWidth(6);
    mxprOPIS.setCaption("Opis");
    mxprOPIS.setColumnName("OPIS");
    mxprOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprOPIS.setPrecision(50);
    mxprOPIS.setTableName("MXPRINTER");
    mxprOPIS.setServerColumnName("OPIS");
    mxprOPIS.setSqlType(1);
    mxprOPIS.setWidth(30);
    mxprPAGESIZE.setCaption("Visina stranice");
    mxprPAGESIZE.setColumnName("PAGESIZE");
    mxprPAGESIZE.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprPAGESIZE.setPrecision(6);
    mxprPAGESIZE.setTableName("MXPRINTER");
    mxprPAGESIZE.setServerColumnName("PAGESIZE");
    mxprPAGESIZE.setSqlType(4);
    mxprPAGESIZE.setWidth(6);
    mxprPAGEWIDTH.setCaption("Širina stranice");
    mxprPAGEWIDTH.setColumnName("PAGEWIDTH");
    mxprPAGEWIDTH.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprPAGEWIDTH.setPrecision(6);
    mxprPAGEWIDTH.setTableName("MXPRINTER");
    mxprPAGEWIDTH.setServerColumnName("PAGEWIDTH");
    mxprPAGEWIDTH.setSqlType(4);
    mxprPAGEWIDTH.setWidth(6);
    mxprTOPMARGIN.setCaption("Gornja margina");
    mxprTOPMARGIN.setColumnName("TOPMARGIN");
    mxprTOPMARGIN.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprTOPMARGIN.setPrecision(6);
    mxprTOPMARGIN.setTableName("MXPRINTER");
    mxprTOPMARGIN.setServerColumnName("TOPMARGIN");
    mxprTOPMARGIN.setSqlType(4);
    mxprTOPMARGIN.setWidth(6);
    mxprLEFTMARGIN.setCaption("Lijeva margina");
    mxprLEFTMARGIN.setColumnName("LEFTMARGIN");
    mxprLEFTMARGIN.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprLEFTMARGIN.setPrecision(6);
    mxprLEFTMARGIN.setTableName("MXPRINTER");
    mxprLEFTMARGIN.setServerColumnName("LEFTMARGIN");
    mxprLEFTMARGIN.setSqlType(4);
    mxprLEFTMARGIN.setWidth(6);
    mxprBOTMARGIN.setCaption("Donja margina");
    mxprBOTMARGIN.setColumnName("BOTMARGIN");
    mxprBOTMARGIN.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprBOTMARGIN.setPrecision(6);
    mxprBOTMARGIN.setTableName("MXPRINTER");
    mxprBOTMARGIN.setServerColumnName("BOTMARGIN");
    mxprBOTMARGIN.setSqlType(4);
    mxprBOTMARGIN.setWidth(6);
    mxprLINEFEEDSEC.setCaption("Razmak linija");
    mxprLINEFEEDSEC.setColumnName("LINEFEEDSEC");
    mxprLINEFEEDSEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprLINEFEEDSEC.setPrecision(15);
    mxprLINEFEEDSEC.setTableName("MXPRINTER");
    mxprLINEFEEDSEC.setServerColumnName("LINEFEEDSEC");
    mxprLINEFEEDSEC.setSqlType(1);
    mxprLINEFEED.setCaption("Preciznost razmaka");
    mxprLINEFEED.setColumnName("LINEFEED");
    mxprLINEFEED.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprLINEFEED.setPrecision(6);
    mxprLINEFEED.setTableName("MXPRINTER");
    mxprLINEFEED.setServerColumnName("LINEFEED");
    mxprLINEFEED.setSqlType(4);
    mxprLINEFEED.setWidth(6);
    mxprPOMAKMM.setCaption("Pomak papira u mm");
    mxprPOMAKMM.setColumnName("POMAKMM");
    mxprPOMAKMM.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprPOMAKMM.setPrecision(6);
    mxprPOMAKMM.setTableName("MXPRINTER");
    mxprPOMAKMM.setServerColumnName("POMAKMM");
    mxprPOMAKMM.setSqlType(4);
    mxprPOMAKMM.setWidth(6);
    mxprRESET.setCaption("Inicijalizacija");
    mxprRESET.setColumnName("RESET");
    mxprRESET.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprRESET.setPrecision(15);
    mxprRESET.setTableName("MXPRINTER");
    mxprRESET.setServerColumnName("RESET");
    mxprRESET.setSqlType(1);
    mxprNEWLINE.setCaption("Novi red");
    mxprNEWLINE.setColumnName("NEWLINE");
    mxprNEWLINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprNEWLINE.setPrecision(15);
    mxprNEWLINE.setTableName("MXPRINTER");
    mxprNEWLINE.setServerColumnName("NEWLINE");
    mxprNEWLINE.setSqlType(1);
    mxprCPI10.setCaption("10 CPI Ispis");
    mxprCPI10.setColumnName("CPI10");
    mxprCPI10.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprCPI10.setPrecision(15);
    mxprCPI10.setTableName("MXPRINTER");
    mxprCPI10.setServerColumnName("CPI10");
    mxprCPI10.setSqlType(1);
    mxprCPI12.setCaption("12 CPI Ispis");
    mxprCPI12.setColumnName("CPI12");
    mxprCPI12.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprCPI12.setPrecision(15);
    mxprCPI12.setTableName("MXPRINTER");
    mxprCPI12.setServerColumnName("CPI12");
    mxprCPI12.setSqlType(1);
    mxprCPI15.setCaption("15 CPI Ispis");
    mxprCPI15.setColumnName("CPI15");
    mxprCPI15.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprCPI15.setPrecision(15);
    mxprCPI15.setTableName("MXPRINTER");
    mxprCPI15.setServerColumnName("CPI15");
    mxprCPI15.setSqlType(1);
    mxprCPI17.setCaption("17 CPI Ispis");
    mxprCPI17.setColumnName("CPI17");
    mxprCPI17.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprCPI17.setPrecision(1);
    mxprCPI17.setTableName("MXPRINTER");
    mxprCPI17.setServerColumnName("CPI17");
    mxprCPI17.setSqlType(1);
    mxprCONDENSON.setCaption("Kondenzirani ispis");
    mxprCONDENSON.setColumnName("CONDENSON");
    mxprCONDENSON.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprCONDENSON.setPrecision(15);
    mxprCONDENSON.setTableName("MXPRINTER");
    mxprCONDENSON.setServerColumnName("CONDENSON");
    mxprCONDENSON.setSqlType(1);
    mxprCONDENSOFF.setCaption("Nekondenzirani ispis");
    mxprCONDENSOFF.setColumnName("CONDENSOFF");
    mxprCONDENSOFF.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprCONDENSOFF.setPrecision(15);
    mxprCONDENSOFF.setTableName("MXPRINTER");
    mxprCONDENSOFF.setServerColumnName("CONDENSOFF");
    mxprCONDENSOFF.setSqlType(1);
    mxprDOUBLEHON.setCaption("Dvostruka visina");
    mxprDOUBLEHON.setColumnName("DOUBLEHON");
    mxprDOUBLEHON.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprDOUBLEHON.setPrecision(15);
    mxprDOUBLEHON.setTableName("MXPRINTER");
    mxprDOUBLEHON.setServerColumnName("DOUBLEHON");
    mxprDOUBLEHON.setSqlType(1);
    mxprDOUBLEHOFF.setCaption("Normalna visina");
    mxprDOUBLEHOFF.setColumnName("DOUBLEHOFF");
    mxprDOUBLEHOFF.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprDOUBLEHOFF.setPrecision(15);
    mxprDOUBLEHOFF.setTableName("MXPRINTER");
    mxprDOUBLEHOFF.setServerColumnName("DOUBLEHOFF");
    mxprDOUBLEHOFF.setSqlType(1);
    mxprDOUBLEWON.setCaption("Dvostruka širina");
    mxprDOUBLEWON.setColumnName("DOUBLEWON");
    mxprDOUBLEWON.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprDOUBLEWON.setPrecision(15);
    mxprDOUBLEWON.setTableName("MXPRINTER");
    mxprDOUBLEWON.setServerColumnName("DOUBLEWON");
    mxprDOUBLEWON.setSqlType(1);
    mxprDOUBLEWOFF.setCaption("Normalna visina");
    mxprDOUBLEWOFF.setColumnName("DOUBLEWOFF");
    mxprDOUBLEWOFF.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprDOUBLEWOFF.setPrecision(15);
    mxprDOUBLEWOFF.setTableName("MXPRINTER");
    mxprDOUBLEWOFF.setServerColumnName("DOUBLEWOFF");
    mxprDOUBLEWOFF.setSqlType(1);
    mxprLPI6.setCaption("6 redova po in\u010Du");
    mxprLPI6.setColumnName("LPI6");
    mxprLPI6.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprLPI6.setPrecision(15);
    mxprLPI6.setTableName("MXPRINTER");
    mxprLPI6.setServerColumnName("LPI6");
    mxprLPI6.setSqlType(1);
    mxprLPI8.setCaption("8 redova po in\u010Du");
    mxprLPI8.setColumnName("LPI8");
    mxprLPI8.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprLPI8.setPrecision(15);
    mxprLPI8.setTableName("MXPRINTER");
    mxprLPI8.setServerColumnName("LPI8");
    mxprLPI8.setSqlType(1);
    mxprLF_N180.setCaption("Razmak redova N/180");
    mxprLF_N180.setColumnName("LF_N180");
    mxprLF_N180.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprLF_N180.setPrecision(15);
    mxprLF_N180.setTableName("MXPRINTER");
    mxprLF_N180.setServerColumnName("LF_N180");
    mxprLF_N180.setSqlType(1);
    mxprLF_N360.setCaption("Razmak redova N/360");
    mxprLF_N360.setColumnName("LF_N360");
    mxprLF_N360.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprLF_N360.setPrecision(15);
    mxprLF_N360.setTableName("MXPRINTER");
    mxprLF_N360.setServerColumnName("LF_N360");
    mxprLF_N360.setSqlType(1);
    mxprPROPON.setCaption("Proporcionalni ispis");
    mxprPROPON.setColumnName("PROPON");
    mxprPROPON.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprPROPON.setPrecision(15);
    mxprPROPON.setTableName("MXPRINTER");
    mxprPROPON.setServerColumnName("PROPON");
    mxprPROPON.setSqlType(1);
    mxprPROPOFF.setCaption("Fiksna širina");
    mxprPROPOFF.setColumnName("PROPOFF");
    mxprPROPOFF.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprPROPOFF.setPrecision(15);
    mxprPROPOFF.setTableName("MXPRINTER");
    mxprPROPOFF.setServerColumnName("PROPOFF");
    mxprPROPOFF.setSqlType(1);
    mxprENDOFPAGE.setCaption("Kraj stranice");
    mxprENDOFPAGE.setColumnName("ENDOFPAGE");
    mxprENDOFPAGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprENDOFPAGE.setPrecision(15);
    mxprENDOFPAGE.setTableName("MXPRINTER");
    mxprENDOFPAGE.setServerColumnName("ENDOFPAGE");
    mxprENDOFPAGE.setSqlType(1);
    mxprUSER1.setCaption("User 1");
    mxprUSER1.setColumnName("USER1");
    mxprUSER1.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprUSER1.setPrecision(15);
    mxprUSER1.setTableName("MXPRINTER");
    mxprUSER1.setServerColumnName("USER1");
    mxprUSER1.setSqlType(1);
    mxprUSER2.setCaption("User 2");
    mxprUSER2.setColumnName("USER2");
    mxprUSER2.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprUSER2.setPrecision(15);
    mxprUSER2.setTableName("MXPRINTER");
    mxprUSER2.setServerColumnName("USER2");
    mxprUSER2.setSqlType(1);
    mxprUSER3.setCaption("User 3");
    mxprUSER3.setColumnName("USER3");
    mxprUSER3.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprUSER3.setPrecision(15);
    mxprUSER3.setTableName("MXPRINTER");
    mxprUSER3.setServerColumnName("USER3");
    mxprUSER3.setSqlType(1);
    mxprUSER4.setCaption("User 4");
    mxprUSER4.setColumnName("USER4");
    mxprUSER4.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprUSER4.setPrecision(15);
    mxprUSER4.setTableName("MXPRINTER");
    mxprUSER4.setServerColumnName("USER4");
    mxprUSER4.setSqlType(1);
    mxpr.setResolver(dm.getQresolver());
    mxpr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from MxPrinter", null, true, Load.ALL));
 setColumns(new Column[] {mxprCPRINTER, mxprOPIS, mxprPAGESIZE, mxprPAGEWIDTH, mxprTOPMARGIN, mxprLEFTMARGIN, mxprBOTMARGIN, mxprLINEFEEDSEC,
        mxprLINEFEED, mxprPOMAKMM, mxprRESET, mxprNEWLINE, mxprCPI10, mxprCPI12, mxprCPI15, mxprCPI17, mxprCONDENSON, mxprCONDENSOFF, mxprDOUBLEHON,
        mxprDOUBLEHOFF, mxprDOUBLEWON, mxprDOUBLEWOFF, mxprLPI6, mxprLPI8, mxprLF_N180, mxprLF_N360, mxprPROPON, mxprPROPOFF, mxprENDOFPAGE, mxprUSER1,
        mxprUSER2, mxprUSER3, mxprUSER4});
  }

  public void setall() {

    ddl.create("MxPrinter")
       .addInteger("cprinter", 6, true)
       .addChar("opis", 50)
       .addInteger("pagesize", 6)
       .addInteger("pagewidth", 6)
       .addInteger("topmargin", 6)
       .addInteger("leftmargin", 6)
       .addInteger("botmargin", 6)
       .addChar("linefeedsec", 15)
       .addInteger("linefeed", 6)
       .addInteger("pomakmm", 6)
       .addChar("reset", 15)
       .addChar("newline", 15)
       .addChar("cpi10", 15)
       .addChar("cpi12", 15)
       .addChar("cpi15", 15)
       .addChar("cpi17", 1)
       .addChar("condenson", 15)
       .addChar("condensoff", 15)
       .addChar("doublehon", 15)
       .addChar("doublehoff", 15)
       .addChar("doublewon", 15)
       .addChar("doublewoff", 15)
       .addChar("lpi6", 15)
       .addChar("lpi8", 15)
       .addChar("lf_n180", 15)
       .addChar("lf_n360", 15)
       .addChar("propon", 15)
       .addChar("propoff", 15)
       .addChar("endofpage", 15)
       .addChar("user1", 15)
       .addChar("user2", 15)
       .addChar("user3", 15)
       .addChar("user4", 15)
       .addPrimaryKey("cprinter");


    Naziv = "MxPrinter";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
