/****license*****************************************************************
**   file: OS_Arhiva.java
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



public class OS_Arhiva extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Arhiva OS_Arhivaclass;

  QueryDataSet osarh = new QueryDataSet();

  Column osarhLOKK = new Column();
  Column osarhAKTIV = new Column();
  Column osarhCORG = new Column();
  Column osarhRBR = new Column();
  Column osarhINVBROJ = new Column();
  Column osarhNAZSREDSTVA = new Column();
  Column osarhCLOKACIJE = new Column();
  Column osarhCOBJEKT = new Column();
  Column osarhCARTIKLA = new Column();
  Column osarhCPAR = new Column();
  Column osarhBROJKONTA = new Column();
  Column osarhCGRUPE = new Column();
  Column osarhZAKSTOPA = new Column();
  Column osarhODLSTOPA = new Column();
  Column osarhCSKUPINE = new Column();
  Column osarhPORIJEKLO = new Column();
  Column osarhRADNIK = new Column();
  Column osarhDATNABAVE = new Column();
  Column osarhDATAKTIVIRANJA = new Column();
  Column osarhSTATUSKNJ = new Column();
  Column osarhOSNOVICA = new Column();
  Column osarhISPRAVAK = new Column();
  Column osarhSADVRIJED = new Column();
  Column osarhREVOSN = new Column();
  Column osarhREVISP = new Column();
  Column osarhREVSAD = new Column();
  Column osarhAMORTIZACIJA = new Column();
  Column osarhPAMORTIZACIJA = new Column();
  Column osarhREVAMOR = new Column();
  Column osarhPREBACAM = new Column();

  public static OS_Arhiva getDataModule() {
    if (OS_Arhivaclass == null) {
      OS_Arhivaclass = new OS_Arhiva();
    }
    return OS_Arhivaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osarh;
  }

  public OS_Arhiva() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osarhLOKK.setCaption("Status zauzetosti");
    osarhLOKK.setColumnName("LOKK");
    osarhLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhLOKK.setPrecision(1);
    osarhLOKK.setTableName("OS_ARHIVA");
    osarhLOKK.setServerColumnName("LOKK");
    osarhLOKK.setSqlType(1);
    osarhLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osarhLOKK.setDefault("N");
    osarhAKTIV.setCaption("Aktivan - neaktivan");
    osarhAKTIV.setColumnName("AKTIV");
    osarhAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhAKTIV.setPrecision(1);
    osarhAKTIV.setTableName("OS_ARHIVA");
    osarhAKTIV.setServerColumnName("AKTIV");
    osarhAKTIV.setSqlType(1);
    osarhAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osarhAKTIV.setDefault("D");
    osarhCORG.setCaption("OJ");
    osarhCORG.setColumnName("CORG");
    osarhCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhCORG.setPrecision(12);
    osarhCORG.setRowId(true);
    osarhCORG.setTableName("OS_ARHIVA");
    osarhCORG.setServerColumnName("CORG");
    osarhCORG.setSqlType(1);
    osarhRBR.setCaption("Rbr");
    osarhRBR.setColumnName("RBR");
    osarhRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    osarhRBR.setPrecision(6);
    osarhRBR.setRowId(true);
    osarhRBR.setTableName("OS_ARHIVA");
    osarhRBR.setServerColumnName("RBR");
    osarhRBR.setSqlType(4);
    osarhRBR.setWidth(6);
    osarhINVBROJ.setCaption("IBroj");
    osarhINVBROJ.setColumnName("INVBROJ");
    osarhINVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhINVBROJ.setPrecision(10);
    osarhINVBROJ.setRowId(true);
    osarhINVBROJ.setTableName("OS_ARHIVA");
    osarhINVBROJ.setServerColumnName("INVBROJ");
    osarhINVBROJ.setSqlType(1);
    osarhNAZSREDSTVA.setCaption("Naziv sredstva");
    osarhNAZSREDSTVA.setColumnName("NAZSREDSTVA");
    osarhNAZSREDSTVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhNAZSREDSTVA.setPrecision(50);
    osarhNAZSREDSTVA.setTableName("OS_ARHIVA");
    osarhNAZSREDSTVA.setServerColumnName("NAZSREDSTVA");
    osarhNAZSREDSTVA.setSqlType(1);
    osarhNAZSREDSTVA.setWidth(30);
    osarhCLOKACIJE.setCaption("Lokacija");
    osarhCLOKACIJE.setColumnName("CLOKACIJE");
    osarhCLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhCLOKACIJE.setPrecision(12);
    osarhCLOKACIJE.setTableName("OS_ARHIVA");
    osarhCLOKACIJE.setServerColumnName("CLOKACIJE");
    osarhCLOKACIJE.setSqlType(1);
    osarhCOBJEKT.setCaption("Objekt");
    osarhCOBJEKT.setColumnName("COBJEKT");
    osarhCOBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhCOBJEKT.setPrecision(6);
    osarhCOBJEKT.setTableName("OS_ARHIVA");
    osarhCOBJEKT.setServerColumnName("COBJEKT");
    osarhCOBJEKT.setSqlType(1);
    osarhCARTIKLA.setCaption("Artikl");
    osarhCARTIKLA.setColumnName("CARTIKLA");
    osarhCARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhCARTIKLA.setPrecision(6);
    osarhCARTIKLA.setTableName("OS_ARHIVA");
    osarhCARTIKLA.setServerColumnName("CARTIKLA");
    osarhCARTIKLA.setSqlType(1);
    osarhCPAR.setCaption("Partner");
    osarhCPAR.setColumnName("CPAR");
    osarhCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    osarhCPAR.setPrecision(6);
    osarhCPAR.setTableName("OS_ARHIVA");
    osarhCPAR.setServerColumnName("CPAR");
    osarhCPAR.setSqlType(4);
    osarhCPAR.setWidth(6);
    osarhBROJKONTA.setCaption("Konto");
    osarhBROJKONTA.setColumnName("BROJKONTA");
    osarhBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhBROJKONTA.setPrecision(8);
    osarhBROJKONTA.setTableName("OS_ARHIVA");
    osarhBROJKONTA.setServerColumnName("BROJKONTA");
    osarhBROJKONTA.setSqlType(1);
    osarhCGRUPE.setCaption("Amort. grupa");
    osarhCGRUPE.setColumnName("CGRUPE");
    osarhCGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhCGRUPE.setPrecision(6);
    osarhCGRUPE.setTableName("OS_ARHIVA");
    osarhCGRUPE.setServerColumnName("CGRUPE");
    osarhCGRUPE.setSqlType(1);
    osarhZAKSTOPA.setCaption("Zakonska stopa");
    osarhZAKSTOPA.setColumnName("ZAKSTOPA");
    osarhZAKSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhZAKSTOPA.setPrecision(10);
    osarhZAKSTOPA.setScale(4);
    osarhZAKSTOPA.setDisplayMask("###,###,##0.0000");
    osarhZAKSTOPA.setDefault("0");
    osarhZAKSTOPA.setTableName("OS_ARHIVA");
    osarhZAKSTOPA.setServerColumnName("ZAKSTOPA");
    osarhZAKSTOPA.setSqlType(2);
    osarhODLSTOPA.setCaption("Stopa po odluci");
    osarhODLSTOPA.setColumnName("ODLSTOPA");
    osarhODLSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhODLSTOPA.setPrecision(10);
    osarhODLSTOPA.setScale(4);
    osarhODLSTOPA.setDisplayMask("###,###,##0.0000");
    osarhODLSTOPA.setDefault("0");
    osarhODLSTOPA.setTableName("OS_ARHIVA");
    osarhODLSTOPA.setServerColumnName("ODLSTOPA");
    osarhODLSTOPA.setSqlType(2);
    osarhCSKUPINE.setCaption("Rev. skupina");
    osarhCSKUPINE.setColumnName("CSKUPINE");
    osarhCSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhCSKUPINE.setPrecision(6);
    osarhCSKUPINE.setTableName("OS_ARHIVA");
    osarhCSKUPINE.setServerColumnName("CSKUPINE");
    osarhCSKUPINE.setSqlType(1);
    osarhPORIJEKLO.setCaption("Porijeklo");
    osarhPORIJEKLO.setColumnName("PORIJEKLO");
    osarhPORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhPORIJEKLO.setPrecision(2);
    osarhPORIJEKLO.setTableName("OS_ARHIVA");
    osarhPORIJEKLO.setServerColumnName("PORIJEKLO");
    osarhPORIJEKLO.setSqlType(1);
    osarhRADNIK.setCaption("Radnik");
    osarhRADNIK.setColumnName("RADNIK");
    osarhRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhRADNIK.setPrecision(6);
    osarhRADNIK.setTableName("OS_ARHIVA");
    osarhRADNIK.setServerColumnName("RADNIK");
    osarhRADNIK.setSqlType(1);
    osarhDATNABAVE.setCaption("Datum nabave");
    osarhDATNABAVE.setColumnName("DATNABAVE");
    osarhDATNABAVE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osarhDATNABAVE.setPrecision(8);
    osarhDATNABAVE.setDisplayMask("dd-MM-yyyy");
//    osarhDATNABAVE.setEditMask("dd-MM-yyyy");
    osarhDATNABAVE.setTableName("OS_ARHIVA");
    osarhDATNABAVE.setServerColumnName("DATNABAVE");
    osarhDATNABAVE.setSqlType(93);
    osarhDATNABAVE.setWidth(10);
    osarhDATAKTIVIRANJA.setCaption("Datum aktiviranja");
    osarhDATAKTIVIRANJA.setColumnName("DATAKTIVIRANJA");
    osarhDATAKTIVIRANJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osarhDATAKTIVIRANJA.setPrecision(8);
    osarhDATAKTIVIRANJA.setDisplayMask("dd-MM-yyyy");
//    osarhDATAKTIVIRANJA.setEditMask("dd-MM-yyyy");
    osarhDATAKTIVIRANJA.setTableName("OS_ARHIVA");
    osarhDATAKTIVIRANJA.setServerColumnName("DATAKTIVIRANJA");
    osarhDATAKTIVIRANJA.setSqlType(93);
    osarhDATAKTIVIRANJA.setWidth(10);
    osarhSTATUSKNJ.setCaption("Status knjiženja");
    osarhSTATUSKNJ.setColumnName("STATUSKNJ");
    osarhSTATUSKNJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osarhSTATUSKNJ.setPrecision(1);
    osarhSTATUSKNJ.setTableName("OS_ARHIVA");
    osarhSTATUSKNJ.setServerColumnName("STATUSKNJ");
    osarhSTATUSKNJ.setSqlType(1);
    osarhSTATUSKNJ.setDefault("N");
    osarhOSNOVICA.setCaption("Osnovica");
    osarhOSNOVICA.setColumnName("OSNOVICA");
    osarhOSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhOSNOVICA.setPrecision(17);
    osarhOSNOVICA.setScale(2);
    osarhOSNOVICA.setDisplayMask("###,###,##0.00");
    osarhOSNOVICA.setDefault("0");
    osarhOSNOVICA.setTableName("OS_ARHIVA");
    osarhOSNOVICA.setServerColumnName("OSNOVICA");
    osarhOSNOVICA.setSqlType(2);
    osarhISPRAVAK.setCaption("Ispravak");
    osarhISPRAVAK.setColumnName("ISPRAVAK");
    osarhISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhISPRAVAK.setPrecision(17);
    osarhISPRAVAK.setScale(2);
    osarhISPRAVAK.setDisplayMask("###,###,##0.00");
    osarhISPRAVAK.setDefault("0");
    osarhISPRAVAK.setTableName("OS_ARHIVA");
    osarhISPRAVAK.setServerColumnName("ISPRAVAK");
    osarhISPRAVAK.setSqlType(2);
    osarhSADVRIJED.setCaption("Vrijednost");
    osarhSADVRIJED.setColumnName("SADVRIJED");
    osarhSADVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhSADVRIJED.setPrecision(17);
    osarhSADVRIJED.setScale(2);
    osarhSADVRIJED.setDisplayMask("###,###,##0.00");
    osarhSADVRIJED.setDefault("0");
    osarhSADVRIJED.setTableName("OS_ARHIVA");
    osarhSADVRIJED.setServerColumnName("SADVRIJED");
    osarhSADVRIJED.setSqlType(2);
    osarhREVOSN.setCaption("Rev. isnovice");
    osarhREVOSN.setColumnName("REVOSN");
    osarhREVOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhREVOSN.setPrecision(17);
    osarhREVOSN.setScale(2);
    osarhREVOSN.setDisplayMask("###,###,##0.00");
    osarhREVOSN.setDefault("0");
    osarhREVOSN.setTableName("OS_ARHIVA");
    osarhREVOSN.setServerColumnName("REVOSN");
    osarhREVOSN.setSqlType(2);
    osarhREVISP.setCaption("Rev. ispravka");
    osarhREVISP.setColumnName("REVISP");
    osarhREVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhREVISP.setPrecision(17);
    osarhREVISP.setScale(2);
    osarhREVISP.setDisplayMask("###,###,##0.00");
    osarhREVISP.setDefault("0");
    osarhREVISP.setTableName("OS_ARHIVA");
    osarhREVISP.setServerColumnName("REVISP");
    osarhREVISP.setSqlType(2);
    osarhREVSAD.setCaption("Revalorizacija");
    osarhREVSAD.setColumnName("REVSAD");
    osarhREVSAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhREVSAD.setPrecision(17);
    osarhREVSAD.setScale(2);
    osarhREVSAD.setDisplayMask("###,###,##0.00");
    osarhREVSAD.setDefault("0");
    osarhREVSAD.setTableName("OS_ARHIVA");
    osarhREVSAD.setServerColumnName("REVSAD");
    osarhREVSAD.setSqlType(2);
    osarhAMORTIZACIJA.setCaption("Amortizacija");
    osarhAMORTIZACIJA.setColumnName("AMORTIZACIJA");
    osarhAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhAMORTIZACIJA.setPrecision(17);
    osarhAMORTIZACIJA.setScale(2);
    osarhAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osarhAMORTIZACIJA.setDefault("0");
    osarhAMORTIZACIJA.setTableName("OS_ARHIVA");
    osarhAMORTIZACIJA.setServerColumnName("AMORTIZACIJA");
    osarhAMORTIZACIJA.setSqlType(2);
    osarhPAMORTIZACIJA.setCaption("Pov. amortizacija");
    osarhPAMORTIZACIJA.setColumnName("PAMORTIZACIJA");
    osarhPAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhPAMORTIZACIJA.setPrecision(17);
    osarhPAMORTIZACIJA.setScale(2);
    osarhPAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    osarhPAMORTIZACIJA.setDefault("0");
    osarhPAMORTIZACIJA.setTableName("OS_ARHIVA");
    osarhPAMORTIZACIJA.setServerColumnName("PAMORTIZACIJA");
    osarhPAMORTIZACIJA.setSqlType(2);
    osarhREVAMOR.setCaption("Rev. amortizacije");
    osarhREVAMOR.setColumnName("REVAMOR");
    osarhREVAMOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhREVAMOR.setPrecision(17);
    osarhREVAMOR.setScale(2);
    osarhREVAMOR.setDisplayMask("###,###,##0.00");
    osarhREVAMOR.setDefault("0");
    osarhREVAMOR.setTableName("OS_ARHIVA");
    osarhREVAMOR.setServerColumnName("REVAMOR");
    osarhREVAMOR.setSqlType(2);
    osarhPREBACAM.setCaption("Preb. amortizacije");
    osarhPREBACAM.setColumnName("PREBACAM");
    osarhPREBACAM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osarhPREBACAM.setPrecision(17);
    osarhPREBACAM.setScale(2);
    osarhPREBACAM.setDisplayMask("###,###,##0.00");
    osarhPREBACAM.setDefault("0");
    osarhPREBACAM.setTableName("OS_ARHIVA");
    osarhPREBACAM.setServerColumnName("PREBACAM");
    osarhPREBACAM.setSqlType(2);
    osarh.setResolver(dm.getQresolver());
    osarh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Arhiva", null, true, Load.ALL));
 setColumns(new Column[] {osarhLOKK, osarhAKTIV, osarhCORG, osarhRBR, osarhINVBROJ, osarhNAZSREDSTVA, osarhCLOKACIJE, osarhCOBJEKT, osarhCARTIKLA,
        osarhCPAR, osarhBROJKONTA, osarhCGRUPE, osarhZAKSTOPA, osarhODLSTOPA, osarhCSKUPINE, osarhPORIJEKLO, osarhRADNIK, osarhDATNABAVE, osarhDATAKTIVIRANJA,
        osarhSTATUSKNJ, osarhOSNOVICA, osarhISPRAVAK, osarhSADVRIJED, osarhREVOSN, osarhREVISP, osarhREVSAD, osarhAMORTIZACIJA, osarhPAMORTIZACIJA,
        osarhREVAMOR, osarhPREBACAM});
  }

  public void setall() {

    ddl.create("OS_Arhiva")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addInteger("rbr", 6, true)
       .addChar("invbroj", 10, true)
       .addChar("nazsredstva", 50)
       .addChar("clokacije", 12)
       .addChar("cobjekt", 6)
       .addChar("cartikla", 6)
       .addInteger("cpar", 6)
       .addChar("brojkonta", 8)
       .addChar("cgrupe", 6)
       .addFloat("zakstopa", 10, 4)
       .addFloat("odlstopa", 10, 4)
       .addChar("cskupine", 6)
       .addChar("porijeklo", 2)
       .addChar("radnik", 6)
       .addDate("datnabave")
       .addDate("dataktiviranja")
       .addChar("statusknj", 1, "N")
       .addFloat("osnovica", 17, 2)
       .addFloat("ispravak", 17, 2)
       .addFloat("sadvrijed", 17, 2)
       .addFloat("revosn", 17, 2)
       .addFloat("revisp", 17, 2)
       .addFloat("revsad", 17, 2)
       .addFloat("amortizacija", 17, 2)
       .addFloat("pamortizacija", 17, 2)
       .addFloat("revamor", 17, 2)
       .addFloat("prebacam", 17, 2)
       .addPrimaryKey("corg,rbr,invbroj");


    Naziv = "OS_Arhiva";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"rbr", "invbroj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
