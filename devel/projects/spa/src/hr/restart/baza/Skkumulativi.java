/****license*****************************************************************
**   file: Skkumulativi.java
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



public class Skkumulativi extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Skkumulativi Skkumulativiclass;

  QueryDataSet skkum = new raDataSet();

  Column skkumLOKK = new Column();
  Column skkumAKTIV = new Column();
  Column skkumKNJIG = new Column();
  Column skkumGODINA = new Column();
  Column skkumCORG = new Column();
  Column skkumCPAR = new Column();
  Column skkumULOGA = new Column();
  Column skkumBROJKONTA = new Column();
  Column skkumPOCSTRAC = new Column();
  Column skkumPOCSTUPL = new Column();
  Column skkumPROMETR = new Column();
  Column skkumPROMETU = new Column();
  Column skkumDATZADRAC = new Column();

  public static Skkumulativi getDataModule() {
    if (Skkumulativiclass == null) {
      Skkumulativiclass = new Skkumulativi();
    }
    return Skkumulativiclass;
  }

  public QueryDataSet getQueryDataSet() {
    return skkum;
  }

  public Skkumulativi() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    skkumLOKK.setCaption("Status zauzetosti");
    skkumLOKK.setColumnName("LOKK");
    skkumLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skkumLOKK.setPrecision(1);
    skkumLOKK.setTableName("SKKUMULATIVI");
    skkumLOKK.setServerColumnName("LOKK");
    skkumLOKK.setSqlType(1);
    skkumLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skkumLOKK.setDefault("N");
    skkumAKTIV.setCaption("Aktivan - neaktivan");
    skkumAKTIV.setColumnName("AKTIV");
    skkumAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    skkumAKTIV.setPrecision(1);
    skkumAKTIV.setTableName("SKKUMULATIVI");
    skkumAKTIV.setServerColumnName("AKTIV");
    skkumAKTIV.setSqlType(1);
    skkumAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skkumAKTIV.setDefault("D");
    skkumKNJIG.setCaption("Knjigovodstvo");
    skkumKNJIG.setColumnName("KNJIG");
    skkumKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    skkumKNJIG.setPrecision(12);
    skkumKNJIG.setRowId(true);
    skkumKNJIG.setTableName("SKKUMULATIVI");
    skkumKNJIG.setServerColumnName("KNJIG");
    skkumKNJIG.setSqlType(1);
    skkumGODINA.setCaption("Godina");
    skkumGODINA.setColumnName("GODINA");
    skkumGODINA.setDataType(com.borland.dx.dataset.Variant.STRING);
    skkumGODINA.setPrecision(4);
    skkumGODINA.setRowId(true);
    skkumGODINA.setTableName("SKKUMULATIVI");
    skkumGODINA.setServerColumnName("GODINA");
    skkumGODINA.setSqlType(1);
    skkumCORG.setCaption("Org. jedinica");
    skkumCORG.setColumnName("CORG");
    skkumCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    skkumCORG.setPrecision(12);
    skkumCORG.setRowId(true);
    skkumCORG.setTableName("SKKUMULATIVI");
    skkumCORG.setServerColumnName("CORG");
    skkumCORG.setSqlType(1);
    skkumCPAR.setCaption("Partner");
    skkumCPAR.setColumnName("CPAR");
    skkumCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    skkumCPAR.setPrecision(6);
    skkumCPAR.setRowId(true);
    skkumCPAR.setTableName("SKKUMULATIVI");
    skkumCPAR.setServerColumnName("CPAR");
    skkumCPAR.setSqlType(4);
    skkumCPAR.setWidth(6);
    skkumULOGA.setCaption("Uloga");
    skkumULOGA.setColumnName("ULOGA");
    skkumULOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    skkumULOGA.setPrecision(1);
    skkumULOGA.setRowId(true);
    skkumULOGA.setTableName("SKKUMULATIVI");
    skkumULOGA.setServerColumnName("ULOGA");
    skkumULOGA.setSqlType(1);
    skkumBROJKONTA.setCaption("Konto");
    skkumBROJKONTA.setColumnName("BROJKONTA");
    skkumBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    skkumBROJKONTA.setPrecision(8);
    skkumBROJKONTA.setRowId(true);
    skkumBROJKONTA.setTableName("SKKUMULATIVI");
    skkumBROJKONTA.setServerColumnName("BROJKONTA");
    skkumBROJKONTA.setSqlType(1);
    skkumPOCSTRAC.setCaption("Po\u010Detno stanje ra\u010Duna");
    skkumPOCSTRAC.setColumnName("POCSTRAC");
    skkumPOCSTRAC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skkumPOCSTRAC.setPrecision(17);
    skkumPOCSTRAC.setScale(2);
    skkumPOCSTRAC.setDisplayMask("###,###,##0.00");
    skkumPOCSTRAC.setDefault("0");
    skkumPOCSTRAC.setTableName("SKKUMULATIVI");
    skkumPOCSTRAC.setServerColumnName("POCSTRAC");
    skkumPOCSTRAC.setSqlType(2);
    skkumPOCSTUPL.setCaption("Po\u010Detno stanje uplata");
    skkumPOCSTUPL.setColumnName("POCSTUPL");
    skkumPOCSTUPL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skkumPOCSTUPL.setPrecision(17);
    skkumPOCSTUPL.setScale(2);
    skkumPOCSTUPL.setDisplayMask("###,###,##0.00");
    skkumPOCSTUPL.setDefault("0");
    skkumPOCSTUPL.setTableName("SKKUMULATIVI");
    skkumPOCSTUPL.setServerColumnName("POCSTUPL");
    skkumPOCSTUPL.setSqlType(2);
    skkumPROMETR.setCaption("Promet ra\u010Duna");
    skkumPROMETR.setColumnName("PROMETR");
    skkumPROMETR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skkumPROMETR.setPrecision(17);
    skkumPROMETR.setScale(2);
    skkumPROMETR.setWidth(12);
    skkumPROMETR.setDisplayMask("###,###,##0.00");
    skkumPROMETR.setDefault("0");
    skkumPROMETR.setTableName("SKKUMULATIVI");
    skkumPROMETR.setServerColumnName("PROMETR");
    skkumPROMETR.setSqlType(2);
    skkumPROMETU.setCaption("Promet uplata");
    skkumPROMETU.setColumnName("PROMETU");
    skkumPROMETU.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skkumPROMETU.setPrecision(17);
    skkumPROMETU.setScale(2);
    skkumPROMETU.setWidth(12);
    skkumPROMETU.setDisplayMask("###,###,##0.00");
    skkumPROMETU.setDefault("0");
    skkumPROMETU.setTableName("SKKUMULATIVI");
    skkumPROMETU.setServerColumnName("PROMETU");
    skkumPROMETU.setSqlType(2);
    skkumDATZADRAC.setCaption("Datum zadnjeg ra\u010Duna u kumulativu");
    skkumDATZADRAC.setColumnName("DATZADRAC");
    skkumDATZADRAC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skkumDATZADRAC.setPrecision(8);
    skkumDATZADRAC.setDisplayMask("dd-MM-yyyy");
//    skkumDATZADRAC.setEditMask("dd-MM-yyyy");
    skkumDATZADRAC.setTableName("SKKUMULATIVI");
    skkumDATZADRAC.setServerColumnName("DATZADRAC");
    skkumDATZADRAC.setSqlType(93);
    skkumDATZADRAC.setWidth(10);
    skkum.setResolver(dm.getQresolver());
    skkum.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Skkumulativi", null, true, Load.ALL));
 setColumns(new Column[] {skkumLOKK, skkumAKTIV, skkumKNJIG, skkumGODINA, skkumCORG, skkumCPAR, skkumULOGA, skkumBROJKONTA, skkumPOCSTRAC,
        skkumPOCSTUPL, skkumPROMETR, skkumPROMETU, skkumDATZADRAC});
  }

  public void setall() {

    ddl.create("Skkumulativi")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addChar("godina", 4, true)
       .addChar("corg", 12, true)
       .addInteger("cpar", 6, true)
       .addChar("uloga", 1, true)
       .addChar("brojkonta", 8, true)
       .addFloat("pocstrac", 17, 2)
       .addFloat("pocstupl", 17, 2)
       .addFloat("prometr", 17, 2)
       .addFloat("prometu", 17, 2)
       .addDate("datzadrac")
       .addPrimaryKey("knjig,godina,corg,cpar,uloga,brojkonta");


    Naziv = "Skkumulativi";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpar", "brojkonta"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
