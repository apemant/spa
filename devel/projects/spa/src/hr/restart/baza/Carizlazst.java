/****license*****************************************************************
**   file: Carizlazst.java
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



public class Carizlazst extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carizlazst Carizlazstclass;

  QueryDataSet carizlazst = new QueryDataSet();

  Column carizlazstLOCK = new Column();
  Column carizlazstAKTIV = new Column();
  Column carizlazstID_IZLAZ_ZAG = new Column();
  Column carizlazstID_IZLAZ_STAV = new Column();
  Column carizlazstRBR = new Column();
  Column carizlazstCTG = new Column();
  Column carizlazstSTOPA1 = new Column();
  Column carizlazstCART = new Column();
  Column carizlazstCART1 = new Column();
  Column carizlazstBC = new Column();
  Column carizlazstNAZART = new Column();
  Column carizlazstJM = new Column();
  Column carizlazstCSIFDRV = new Column();
  Column carizlazstPREF = new Column();
  Column carizlazstCPOTPOR = new Column();
  Column carizlazstKOL = new Column();
  Column carizlazstNETTO_KG = new Column();
  Column carizlazstKOL_KOM = new Column();
  Column carizlazstCIJENA_VAL = new Column();
  Column carizlazstVRIJEDNOST_VAL = new Column();
  Column carizlazstOSNOVICA = new Column();
  Column carizlazstIZNOSCAR = new Column();
  Column carizlazstPPOREZ = new Column();
  Column carizlazstPOREZ = new Column();
  Column carizlazstTROS = new Column();
  Column carizlazstJCD_CCAR = new Column();
  Column carizlazstJCD_BROJ = new Column();
  Column carizlazstJCD_DATUM = new Column();
  Column carizlazstJCD_CPP37 = new Column();
  Column carizlazstJCD_CCAR_KONAC = new Column();
  Column carizlazstJCD_BROJ_KONAC = new Column();
  Column carizlazstJCD_DATUM_KONAC = new Column();
  Column carizlazstJCD_CPP37_KONAC = new Column();

  public static Carizlazst getDataModule() {
    if (Carizlazstclass == null) {
      Carizlazstclass = new Carizlazst();
    }
    return Carizlazstclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carizlazst;
  }

  public Carizlazst() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carizlazstLOCK.setCaption("Status zauzetosti");
    carizlazstLOCK.setColumnName("LOCK");
    carizlazstLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstLOCK.setPrecision(1);
    carizlazstLOCK.setTableName("CARIZLAZST");
    carizlazstLOCK.setServerColumnName("LOCK");
    carizlazstLOCK.setSqlType(1);
    carizlazstLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carizlazstLOCK.setDefault("N");
    carizlazstAKTIV.setCaption("Aktivan - neaktivan");
    carizlazstAKTIV.setColumnName("AKTIV");
    carizlazstAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstAKTIV.setPrecision(1);
    carizlazstAKTIV.setTableName("CARIZLAZST");
    carizlazstAKTIV.setServerColumnName("AKTIV");
    carizlazstAKTIV.setSqlType(1);
    carizlazstAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carizlazstAKTIV.setDefault("D");
    carizlazstID_IZLAZ_ZAG.setCaption("Broja\u010D autonumber");
    carizlazstID_IZLAZ_ZAG.setColumnName("ID_IZLAZ_ZAG");
    carizlazstID_IZLAZ_ZAG.setDataType(com.borland.dx.dataset.Variant.INT);
    carizlazstID_IZLAZ_ZAG.setRowId(true);
    carizlazstID_IZLAZ_ZAG.setTableName("CARIZLAZST");
    carizlazstID_IZLAZ_ZAG.setServerColumnName("ID_IZLAZ_ZAG");
    carizlazstID_IZLAZ_ZAG.setSqlType(4);
    carizlazstID_IZLAZ_ZAG.setWidth(8);
    carizlazstID_IZLAZ_ZAG.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carizlazstID_IZLAZ_STAV.setCaption("Broja\u010D autonumber stavaka");
    carizlazstID_IZLAZ_STAV.setColumnName("ID_IZLAZ_STAV");
    carizlazstID_IZLAZ_STAV.setDataType(com.borland.dx.dataset.Variant.INT);
    carizlazstID_IZLAZ_STAV.setRowId(true);
    carizlazstID_IZLAZ_STAV.setTableName("CARIZLAZST");
    carizlazstID_IZLAZ_STAV.setServerColumnName("ID_IZLAZ_STAV");
    carizlazstID_IZLAZ_STAV.setSqlType(4);
    carizlazstID_IZLAZ_STAV.setWidth(8);
    carizlazstID_IZLAZ_STAV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carizlazstRBR.setCaption("Rbr");
    carizlazstRBR.setColumnName("RBR");
    carizlazstRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    carizlazstRBR.setTableName("CARIZLAZST");
    carizlazstRBR.setServerColumnName("RBR");
    carizlazstRBR.setSqlType(5);
    carizlazstRBR.setWidth(4);
    carizlazstCTG.setCaption("Tarifni broj");
    carizlazstCTG.setColumnName("CTG");
    carizlazstCTG.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstCTG.setPrecision(10);
    carizlazstCTG.setTableName("CARIZLAZST");
    carizlazstCTG.setServerColumnName("CTG");
    carizlazstCTG.setSqlType(1);
    carizlazstSTOPA1.setCaption("Stopa");
    carizlazstSTOPA1.setColumnName("STOPA1");
    carizlazstSTOPA1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstSTOPA1.setPrecision(17);
    carizlazstSTOPA1.setScale(3);
    carizlazstSTOPA1.setDisplayMask("###,###,##0.000");
    carizlazstSTOPA1.setDefault("0");
    carizlazstSTOPA1.setTableName("CARIZLAZST");
    carizlazstSTOPA1.setServerColumnName("STOPA1");
    carizlazstSTOPA1.setSqlType(2);
    carizlazstCART.setCaption("Šifra");
    carizlazstCART.setColumnName("CART");
    carizlazstCART.setDataType(com.borland.dx.dataset.Variant.INT);
    carizlazstCART.setTableName("CARIZLAZST");
    carizlazstCART.setServerColumnName("CART");
    carizlazstCART.setSqlType(4);
    carizlazstCART.setWidth(8);
    carizlazstCART1.setCaption("Kataloški broj");
    carizlazstCART1.setColumnName("CART1");
    carizlazstCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstCART1.setPrecision(20);
    carizlazstCART1.setTableName("CARIZLAZST");
    carizlazstCART1.setServerColumnName("CART1");
    carizlazstCART1.setSqlType(1);
    carizlazstBC.setCaption("Barcode");
    carizlazstBC.setColumnName("BC");
    carizlazstBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstBC.setPrecision(20);
    carizlazstBC.setTableName("CARIZLAZST");
    carizlazstBC.setServerColumnName("BC");
    carizlazstBC.setSqlType(1);
    carizlazstNAZART.setCaption("Naziv");
    carizlazstNAZART.setColumnName("NAZART");
    carizlazstNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstNAZART.setPrecision(50);
    carizlazstNAZART.setTableName("CARIZLAZST");
    carizlazstNAZART.setServerColumnName("NAZART");
    carizlazstNAZART.setSqlType(1);
    carizlazstNAZART.setWidth(30);
    carizlazstJM.setCaption("Jm");
    carizlazstJM.setColumnName("JM");
    carizlazstJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstJM.setPrecision(3);
    carizlazstJM.setTableName("CARIZLAZST");
    carizlazstJM.setServerColumnName("JM");
    carizlazstJM.setSqlType(1);
    carizlazstCSIFDRV.setCaption("Porijeklo");
    carizlazstCSIFDRV.setColumnName("CSIFDRV");
    carizlazstCSIFDRV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstCSIFDRV.setPrecision(3);
    carizlazstCSIFDRV.setTableName("CARIZLAZST");
    carizlazstCSIFDRV.setServerColumnName("CSIFDRV");
    carizlazstCSIFDRV.setSqlType(1);
    carizlazstPREF.setCaption("Preferencijal");
    carizlazstPREF.setColumnName("PREF");
    carizlazstPREF.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstPREF.setPrecision(10);
    carizlazstPREF.setTableName("CARIZLAZST");
    carizlazstPREF.setServerColumnName("PREF");
    carizlazstPREF.setSqlType(1);
    carizlazstCPOTPOR.setCaption("Potvrda o porijeklu");
    carizlazstCPOTPOR.setColumnName("CPOTPOR");
    carizlazstCPOTPOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstCPOTPOR.setPrecision(10);
    carizlazstCPOTPOR.setTableName("CARIZLAZST");
    carizlazstCPOTPOR.setServerColumnName("CPOTPOR");
    carizlazstCPOTPOR.setSqlType(1);
    carizlazstKOL.setCaption("Koli\u010Dina");
    carizlazstKOL.setColumnName("KOL");
    carizlazstKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstKOL.setPrecision(17);
    carizlazstKOL.setScale(3);
    carizlazstKOL.setDisplayMask("###,###,##0.000");
    carizlazstKOL.setDefault("0");
    carizlazstKOL.setTableName("CARIZLAZST");
    carizlazstKOL.setServerColumnName("KOL");
    carizlazstKOL.setSqlType(2);
    carizlazstNETTO_KG.setCaption("Netto kg");
    carizlazstNETTO_KG.setColumnName("NETTO_KG");
    carizlazstNETTO_KG.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstNETTO_KG.setPrecision(17);
    carizlazstNETTO_KG.setScale(3);
    carizlazstNETTO_KG.setDisplayMask("###,###,##0.000");
    carizlazstNETTO_KG.setDefault("0");
    carizlazstNETTO_KG.setTableName("CARIZLAZST");
    carizlazstNETTO_KG.setServerColumnName("NETTO_KG");
    carizlazstNETTO_KG.setSqlType(2);
    carizlazstKOL_KOM.setCaption("Koli\u010Dina (kom)");
    carizlazstKOL_KOM.setColumnName("KOL_KOM");
    carizlazstKOL_KOM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstKOL_KOM.setPrecision(17);
    carizlazstKOL_KOM.setScale(3);
    carizlazstKOL_KOM.setDisplayMask("###,###,##0.000");
    carizlazstKOL_KOM.setDefault("0");
    carizlazstKOL_KOM.setTableName("CARIZLAZST");
    carizlazstKOL_KOM.setServerColumnName("KOL_KOM");
    carizlazstKOL_KOM.setSqlType(2);
    carizlazstCIJENA_VAL.setCaption("Cijena valutna");
    carizlazstCIJENA_VAL.setColumnName("CIJENA_VAL");
    carizlazstCIJENA_VAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstCIJENA_VAL.setPrecision(17);
    carizlazstCIJENA_VAL.setScale(2);
    carizlazstCIJENA_VAL.setDisplayMask("###,###,##0.00");
    carizlazstCIJENA_VAL.setDefault("0");
    carizlazstCIJENA_VAL.setTableName("CARIZLAZST");
    carizlazstCIJENA_VAL.setServerColumnName("CIJENA_VAL");
    carizlazstCIJENA_VAL.setSqlType(2);
    carizlazstVRIJEDNOST_VAL.setCaption("Vrijednost valutna");
    carizlazstVRIJEDNOST_VAL.setColumnName("VRIJEDNOST_VAL");
    carizlazstVRIJEDNOST_VAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstVRIJEDNOST_VAL.setPrecision(17);
    carizlazstVRIJEDNOST_VAL.setScale(2);
    carizlazstVRIJEDNOST_VAL.setDisplayMask("###,###,##0.00");
    carizlazstVRIJEDNOST_VAL.setDefault("0");
    carizlazstVRIJEDNOST_VAL.setTableName("CARIZLAZST");
    carizlazstVRIJEDNOST_VAL.setServerColumnName("VRIJEDNOST_VAL");
    carizlazstVRIJEDNOST_VAL.setSqlType(2);
    carizlazstOSNOVICA.setCaption("Vrijednost valutna");
    carizlazstOSNOVICA.setColumnName("OSNOVICA");
    carizlazstOSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstOSNOVICA.setPrecision(17);
    carizlazstOSNOVICA.setScale(2);
    carizlazstOSNOVICA.setDisplayMask("###,###,##0.00");
    carizlazstOSNOVICA.setDefault("0");
    carizlazstOSNOVICA.setTableName("CARIZLAZST");
    carizlazstOSNOVICA.setServerColumnName("OSNOVICA");
    carizlazstOSNOVICA.setSqlType(2);
    carizlazstIZNOSCAR.setCaption("Carinska osnovica");
    carizlazstIZNOSCAR.setColumnName("IZNOSCAR");
    carizlazstIZNOSCAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstIZNOSCAR.setPrecision(17);
    carizlazstIZNOSCAR.setScale(2);
    carizlazstIZNOSCAR.setDisplayMask("###,###,##0.00");
    carizlazstIZNOSCAR.setDefault("0");
    carizlazstIZNOSCAR.setTableName("CARIZLAZST");
    carizlazstIZNOSCAR.setServerColumnName("IZNOSCAR");
    carizlazstIZNOSCAR.setSqlType(2);
    carizlazstPPOREZ.setCaption("Posto poreza");
    carizlazstPPOREZ.setColumnName("PPOREZ");
    carizlazstPPOREZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstPPOREZ.setPrecision(17);
    carizlazstPPOREZ.setScale(2);
    carizlazstPPOREZ.setDisplayMask("###,###,##0.00");
    carizlazstPPOREZ.setDefault("0");
    carizlazstPPOREZ.setTableName("CARIZLAZST");
    carizlazstPPOREZ.setServerColumnName("PPOREZ");
    carizlazstPPOREZ.setSqlType(2);
    carizlazstPOREZ.setCaption("Vrijednost valutna");
    carizlazstPOREZ.setColumnName("POREZ");
    carizlazstPOREZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstPOREZ.setPrecision(17);
    carizlazstPOREZ.setScale(2);
    carizlazstPOREZ.setDisplayMask("###,###,##0.00");
    carizlazstPOREZ.setDefault("0");
    carizlazstPOREZ.setTableName("CARIZLAZST");
    carizlazstPOREZ.setServerColumnName("POREZ");
    carizlazstPOREZ.setSqlType(2);
    carizlazstTROS.setCaption("Posebni porez");
    carizlazstTROS.setColumnName("TROS");
    carizlazstTROS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazstTROS.setPrecision(17);
    carizlazstTROS.setScale(2);
    carizlazstTROS.setDisplayMask("###,###,##0.00");
    carizlazstTROS.setDefault("0");
    carizlazstTROS.setTableName("CARIZLAZST");
    carizlazstTROS.setServerColumnName("TROS");
    carizlazstTROS.setSqlType(2);
    carizlazstJCD_CCAR.setCaption("Šifra carinarnice");
    carizlazstJCD_CCAR.setColumnName("JCD_CCAR");
    carizlazstJCD_CCAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstJCD_CCAR.setPrecision(20);
    carizlazstJCD_CCAR.setTableName("CARIZLAZST");
    carizlazstJCD_CCAR.setServerColumnName("JCD_CCAR");
    carizlazstJCD_CCAR.setSqlType(1);
    carizlazstJCD_BROJ.setCaption("Broj JCD");
    carizlazstJCD_BROJ.setColumnName("JCD_BROJ");
    carizlazstJCD_BROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstJCD_BROJ.setPrecision(30);
    carizlazstJCD_BROJ.setTableName("CARIZLAZST");
    carizlazstJCD_BROJ.setServerColumnName("JCD_BROJ");
    carizlazstJCD_BROJ.setSqlType(1);
    carizlazstJCD_BROJ.setWidth(30);
    carizlazstJCD_DATUM.setCaption("Datum JCD");
    carizlazstJCD_DATUM.setColumnName("JCD_DATUM");
    carizlazstJCD_DATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    carizlazstJCD_DATUM.setDisplayMask("dd-MM-yyyy");
//    carizlazstJCD_DATUM.setEditMask("dd-MM-yyyy");
    carizlazstJCD_DATUM.setTableName("CARIZLAZST");
    carizlazstJCD_DATUM.setServerColumnName("JCD_DATUM");
    carizlazstJCD_DATUM.setSqlType(93);
    carizlazstJCD_DATUM.setWidth(10);
    carizlazstJCD_CPP37.setCaption("Šifra carinskog postupka");
    carizlazstJCD_CPP37.setColumnName("JCD_CPP37");
    carizlazstJCD_CPP37.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstJCD_CPP37.setPrecision(10);
    carizlazstJCD_CPP37.setTableName("CARIZLAZST");
    carizlazstJCD_CPP37.setServerColumnName("JCD_CPP37");
    carizlazstJCD_CPP37.setSqlType(1);
    carizlazstJCD_CCAR_KONAC.setCaption("Šifra carinarnice (kona\u010Dni)");
    carizlazstJCD_CCAR_KONAC.setColumnName("JCD_CCAR_KONAC");
    carizlazstJCD_CCAR_KONAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstJCD_CCAR_KONAC.setPrecision(20);
    carizlazstJCD_CCAR_KONAC.setTableName("CARIZLAZST");
    carizlazstJCD_CCAR_KONAC.setServerColumnName("JCD_CCAR_KONAC");
    carizlazstJCD_CCAR_KONAC.setSqlType(1);
    carizlazstJCD_BROJ_KONAC.setCaption("Broj JCD (kona\u010Dni)");
    carizlazstJCD_BROJ_KONAC.setColumnName("JCD_BROJ_KONAC");
    carizlazstJCD_BROJ_KONAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstJCD_BROJ_KONAC.setPrecision(30);
    carizlazstJCD_BROJ_KONAC.setTableName("CARIZLAZST");
    carizlazstJCD_BROJ_KONAC.setServerColumnName("JCD_BROJ_KONAC");
    carizlazstJCD_BROJ_KONAC.setSqlType(1);
    carizlazstJCD_BROJ_KONAC.setWidth(30);
    carizlazstJCD_DATUM_KONAC.setCaption("Datum JCD (kona\u010Dni)");
    carizlazstJCD_DATUM_KONAC.setColumnName("JCD_DATUM_KONAC");
    carizlazstJCD_DATUM_KONAC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    carizlazstJCD_DATUM_KONAC.setDisplayMask("dd-MM-yyyy");
//    carizlazstJCD_DATUM_KONAC.setEditMask("dd-MM-yyyy");
    carizlazstJCD_DATUM_KONAC.setTableName("CARIZLAZST");
    carizlazstJCD_DATUM_KONAC.setServerColumnName("JCD_DATUM_KONAC");
    carizlazstJCD_DATUM_KONAC.setSqlType(93);
    carizlazstJCD_DATUM_KONAC.setWidth(10);
    carizlazstJCD_CPP37_KONAC.setCaption("Šifra carinskog postupka");
    carizlazstJCD_CPP37_KONAC.setColumnName("JCD_CPP37_KONAC");
    carizlazstJCD_CPP37_KONAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazstJCD_CPP37_KONAC.setPrecision(10);
    carizlazstJCD_CPP37_KONAC.setTableName("CARIZLAZST");
    carizlazstJCD_CPP37_KONAC.setServerColumnName("JCD_CPP37_KONAC");
    carizlazstJCD_CPP37_KONAC.setSqlType(1);
    carizlazst.setResolver(dm.getQresolver());
    carizlazst.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carizlazst", null, true, Load.ALL));
    setColumns(new Column[] {carizlazstLOCK, carizlazstAKTIV, carizlazstID_IZLAZ_ZAG, carizlazstID_IZLAZ_STAV, carizlazstRBR, carizlazstCTG, 
        carizlazstSTOPA1, carizlazstCART, carizlazstCART1, carizlazstBC, carizlazstNAZART, carizlazstJM, carizlazstCSIFDRV, carizlazstPREF, carizlazstCPOTPOR, 
        carizlazstKOL, carizlazstNETTO_KG, carizlazstKOL_KOM, carizlazstCIJENA_VAL, carizlazstVRIJEDNOST_VAL, carizlazstOSNOVICA, carizlazstIZNOSCAR, 
        carizlazstPPOREZ, carizlazstPOREZ, carizlazstTROS, carizlazstJCD_CCAR, carizlazstJCD_BROJ, carizlazstJCD_DATUM, carizlazstJCD_CPP37, 
        carizlazstJCD_CCAR_KONAC, carizlazstJCD_BROJ_KONAC, carizlazstJCD_DATUM_KONAC, carizlazstJCD_CPP37_KONAC});
  }

  public void setall() {

    ddl.create("Carizlazst")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("id_izlaz_zag", 8, true)
       .addInteger("id_izlaz_stav", 8, true)
       .addShort("rbr", 4)
       .addChar("ctg", 10)
       .addFloat("stopa1", 17, 3)
       .addInteger("cart", 8)
       .addChar("cart1", 20)
       .addChar("bc", 20)
       .addChar("nazart", 50)
       .addChar("jm", 3)
       .addChar("csifdrv", 3)
       .addChar("pref", 10)
       .addChar("cpotpor", 10)
       .addFloat("kol", 17, 3)
       .addFloat("netto_kg", 17, 3)
       .addFloat("kol_kom", 17, 3)
       .addFloat("cijena_val", 17, 2)
       .addFloat("vrijednost_val", 17, 2)
       .addFloat("osnovica", 17, 2)
       .addFloat("iznoscar", 17, 2)
       .addFloat("pporez", 17, 2)
       .addFloat("porez", 17, 2)
       .addFloat("tros", 17, 2)
       .addChar("jcd_ccar", 20)
       .addChar("jcd_broj", 30)
       .addDate("jcd_datum")
       .addChar("jcd_cpp37", 10)
       .addChar("jcd_ccar_konac", 20)
       .addChar("jcd_broj_konac", 30)
       .addDate("jcd_datum_konac")
       .addChar("jcd_cpp37_konac", 10)
       .addPrimaryKey("id_izlaz_zag,id_izlaz_stav");


    Naziv = "Carizlazst";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"id_izlaz_zag", "id_izlaz_stav"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
