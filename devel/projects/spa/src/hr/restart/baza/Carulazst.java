/****license*****************************************************************
**   file: Carulazst.java
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



public class Carulazst extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carulazst Carulazstclass;

  QueryDataSet carulazst = new QueryDataSet();

  Column carulazstLOCK = new Column();
  Column carulazstAKTIV = new Column();
  Column carulazstID_ULAZ_ZAG = new Column();
  Column carulazstID_ULAZ_STAV = new Column();
  Column carulazstRBR = new Column();
  Column carulazstCTG = new Column();
  Column carulazstCART = new Column();
  Column carulazstCART1 = new Column();
  Column carulazstBC = new Column();
  Column carulazstNAZART = new Column();
  Column carulazstJM = new Column();
  Column carulazstCSIFDRV = new Column();
  Column carulazstPREF = new Column();
  Column carulazstCPOTPOR = new Column();
  Column carulazstKOL = new Column();
  Column carulazstNETTO_KG = new Column();
  Column carulazstKOL_KOM = new Column();
  Column carulazstCIJENA_VAL = new Column();
  Column carulazstVRIJEDNOST_VAL = new Column();
  Column carulazstTROSKOVI_VAL = new Column();
  Column carulazstTROS1_KN = new Column();
  Column carulazstTROS2_KN = new Column();

  public static Carulazst getDataModule() {
    if (Carulazstclass == null) {
      Carulazstclass = new Carulazst();
    }
    return Carulazstclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carulazst;
  }

  public Carulazst() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carulazstLOCK.setCaption("Status zauzetosti");
    carulazstLOCK.setColumnName("LOCK");
    carulazstLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstLOCK.setPrecision(1);
    carulazstLOCK.setTableName("CARULAZST");
    carulazstLOCK.setServerColumnName("LOCK");
    carulazstLOCK.setSqlType(1);
    carulazstLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carulazstLOCK.setDefault("N");
    carulazstAKTIV.setCaption("Aktivan - neaktivan");
    carulazstAKTIV.setColumnName("AKTIV");
    carulazstAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstAKTIV.setPrecision(1);
    carulazstAKTIV.setTableName("CARULAZST");
    carulazstAKTIV.setServerColumnName("AKTIV");
    carulazstAKTIV.setSqlType(1);
    carulazstAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carulazstAKTIV.setDefault("D");
    carulazstID_ULAZ_ZAG.setCaption("Broja\u010D autonumber zaglavlja");
    carulazstID_ULAZ_ZAG.setColumnName("ID_ULAZ_ZAG");
    carulazstID_ULAZ_ZAG.setDataType(com.borland.dx.dataset.Variant.INT);
    carulazstID_ULAZ_ZAG.setTableName("CARULAZST");
    carulazstID_ULAZ_ZAG.setServerColumnName("ID_ULAZ_ZAG");
    carulazstID_ULAZ_ZAG.setSqlType(4);
    carulazstID_ULAZ_ZAG.setWidth(8);
    carulazstID_ULAZ_ZAG.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carulazstID_ULAZ_STAV.setCaption("Broja\u010D autonumber stavaka");
    carulazstID_ULAZ_STAV.setColumnName("ID_ULAZ_STAV");
    carulazstID_ULAZ_STAV.setDataType(com.borland.dx.dataset.Variant.INT);
    carulazstID_ULAZ_STAV.setRowId(true);
    carulazstID_ULAZ_STAV.setTableName("CARULAZST");
    carulazstID_ULAZ_STAV.setServerColumnName("ID_ULAZ_STAV");
    carulazstID_ULAZ_STAV.setSqlType(4);
    carulazstID_ULAZ_STAV.setWidth(8);
    carulazstID_ULAZ_STAV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carulazstRBR.setCaption("Rbr");
    carulazstRBR.setColumnName("RBR");
    carulazstRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    carulazstRBR.setTableName("CARULAZST");
    carulazstRBR.setServerColumnName("RBR");
    carulazstRBR.setSqlType(5);
    carulazstRBR.setWidth(4);
    carulazstCTG.setCaption("Tarifni broj");
    carulazstCTG.setColumnName("CTG");
    carulazstCTG.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstCTG.setPrecision(10);
    carulazstCTG.setTableName("CARULAZST");
    carulazstCTG.setServerColumnName("CTG");
    carulazstCTG.setSqlType(1);
    carulazstCART.setCaption("Šifra");
    carulazstCART.setColumnName("CART");
    carulazstCART.setDataType(com.borland.dx.dataset.Variant.INT);
    carulazstCART.setTableName("CARULAZST");
    carulazstCART.setServerColumnName("CART");
    carulazstCART.setSqlType(4);
    carulazstCART.setWidth(8);
    carulazstCART1.setCaption("Kataloški broj");
    carulazstCART1.setColumnName("CART1");
    carulazstCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstCART1.setPrecision(20);
    carulazstCART1.setTableName("CARULAZST");
    carulazstCART1.setServerColumnName("CART1");
    carulazstCART1.setSqlType(1);
    carulazstBC.setCaption("Barcode");
    carulazstBC.setColumnName("BC");
    carulazstBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstBC.setPrecision(20);
    carulazstBC.setTableName("CARULAZST");
    carulazstBC.setServerColumnName("BC");
    carulazstBC.setSqlType(1);
    carulazstNAZART.setCaption("Naziv");
    carulazstNAZART.setColumnName("NAZART");
    carulazstNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstNAZART.setPrecision(50);
    carulazstNAZART.setTableName("CARULAZST");
    carulazstNAZART.setServerColumnName("NAZART");
    carulazstNAZART.setSqlType(1);
    carulazstNAZART.setWidth(30);
    carulazstJM.setCaption("Jm");
    carulazstJM.setColumnName("JM");
    carulazstJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstJM.setPrecision(3);
    carulazstJM.setTableName("CARULAZST");
    carulazstJM.setServerColumnName("JM");
    carulazstJM.setSqlType(1);
    carulazstCSIFDRV.setCaption("Porijeklo");
    carulazstCSIFDRV.setColumnName("CSIFDRV");
    carulazstCSIFDRV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstCSIFDRV.setPrecision(3);
    carulazstCSIFDRV.setTableName("CARULAZST");
    carulazstCSIFDRV.setServerColumnName("CSIFDRV");
    carulazstCSIFDRV.setSqlType(1);
    carulazstPREF.setCaption("Preferencijal");
    carulazstPREF.setColumnName("PREF");
    carulazstPREF.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstPREF.setPrecision(10);
    carulazstPREF.setTableName("CARULAZST");
    carulazstPREF.setServerColumnName("PREF");
    carulazstPREF.setSqlType(1);
    carulazstCPOTPOR.setCaption("Potvrda o porijeklu");
    carulazstCPOTPOR.setColumnName("CPOTPOR");
    carulazstCPOTPOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazstCPOTPOR.setPrecision(10);
    carulazstCPOTPOR.setTableName("CARULAZST");
    carulazstCPOTPOR.setServerColumnName("CPOTPOR");
    carulazstCPOTPOR.setSqlType(1);
    carulazstKOL.setCaption("Koli\u010Dina");
    carulazstKOL.setColumnName("KOL");
    carulazstKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazstKOL.setPrecision(17);
    carulazstKOL.setScale(3);
    carulazstKOL.setDisplayMask("###,###,##0.000");
    carulazstKOL.setDefault("0");
    carulazstKOL.setTableName("CARULAZST");
    carulazstKOL.setServerColumnName("KOL");
    carulazstKOL.setSqlType(2);
    carulazstNETTO_KG.setCaption("Netto kg");
    carulazstNETTO_KG.setColumnName("NETTO_KG");
    carulazstNETTO_KG.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazstNETTO_KG.setPrecision(17);
    carulazstNETTO_KG.setScale(3);
    carulazstNETTO_KG.setDisplayMask("###,###,##0.000");
    carulazstNETTO_KG.setDefault("0");
    carulazstNETTO_KG.setTableName("CARULAZST");
    carulazstNETTO_KG.setServerColumnName("NETTO_KG");
    carulazstNETTO_KG.setSqlType(2);
    carulazstKOL_KOM.setCaption("Koli\u010Dina (kom)");
    carulazstKOL_KOM.setColumnName("KOL_KOM");
    carulazstKOL_KOM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazstKOL_KOM.setPrecision(17);
    carulazstKOL_KOM.setScale(3);
    carulazstKOL_KOM.setDisplayMask("###,###,##0.000");
    carulazstKOL_KOM.setDefault("0");
    carulazstKOL_KOM.setTableName("CARULAZST");
    carulazstKOL_KOM.setServerColumnName("KOL_KOM");
    carulazstKOL_KOM.setSqlType(2);
    carulazstCIJENA_VAL.setCaption("Cijena valutna");
    carulazstCIJENA_VAL.setColumnName("CIJENA_VAL");
    carulazstCIJENA_VAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazstCIJENA_VAL.setPrecision(17);
    carulazstCIJENA_VAL.setScale(2);
    carulazstCIJENA_VAL.setDisplayMask("###,###,##0.00");
    carulazstCIJENA_VAL.setDefault("0");
    carulazstCIJENA_VAL.setTableName("CARULAZST");
    carulazstCIJENA_VAL.setServerColumnName("CIJENA_VAL");
    carulazstCIJENA_VAL.setSqlType(2);
    carulazstVRIJEDNOST_VAL.setCaption("Vrijednost valutna");
    carulazstVRIJEDNOST_VAL.setColumnName("VRIJEDNOST_VAL");
    carulazstVRIJEDNOST_VAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazstVRIJEDNOST_VAL.setPrecision(17);
    carulazstVRIJEDNOST_VAL.setScale(2);
    carulazstVRIJEDNOST_VAL.setDisplayMask("###,###,##0.00");
    carulazstVRIJEDNOST_VAL.setDefault("0");
    carulazstVRIJEDNOST_VAL.setTableName("CARULAZST");
    carulazstVRIJEDNOST_VAL.setServerColumnName("VRIJEDNOST_VAL");
    carulazstVRIJEDNOST_VAL.setSqlType(2);
    carulazstTROSKOVI_VAL.setCaption("Troškovi valutni");
    carulazstTROSKOVI_VAL.setColumnName("TROSKOVI_VAL");
    carulazstTROSKOVI_VAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazstTROSKOVI_VAL.setPrecision(17);
    carulazstTROSKOVI_VAL.setScale(2);
    carulazstTROSKOVI_VAL.setDisplayMask("###,###,##0.00");
    carulazstTROSKOVI_VAL.setDefault("0");
    carulazstTROSKOVI_VAL.setTableName("CARULAZST");
    carulazstTROSKOVI_VAL.setServerColumnName("TROSKOVI_VAL");
    carulazstTROSKOVI_VAL.setSqlType(2);
    carulazstTROS1_KN.setCaption("Troškovi kunski1");
    carulazstTROS1_KN.setColumnName("TROS1_KN");
    carulazstTROS1_KN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazstTROS1_KN.setPrecision(17);
    carulazstTROS1_KN.setScale(2);
    carulazstTROS1_KN.setDisplayMask("###,###,##0.00");
    carulazstTROS1_KN.setDefault("0");
    carulazstTROS1_KN.setTableName("CARULAZST");
    carulazstTROS1_KN.setServerColumnName("TROS1_KN");
    carulazstTROS1_KN.setSqlType(2);
    carulazstTROS2_KN.setCaption("Troškovi kunski2");
    carulazstTROS2_KN.setColumnName("TROS2_KN");
    carulazstTROS2_KN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazstTROS2_KN.setPrecision(17);
    carulazstTROS2_KN.setScale(2);
    carulazstTROS2_KN.setDisplayMask("###,###,##0.00");
    carulazstTROS2_KN.setDefault("0");
    carulazstTROS2_KN.setTableName("CARULAZST");
    carulazstTROS2_KN.setServerColumnName("TROS2_KN");
    carulazstTROS2_KN.setSqlType(2);
    carulazst.setResolver(dm.getQresolver());
    carulazst.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carulazst", null, true, Load.ALL));
    setColumns(new Column[] {carulazstLOCK, carulazstAKTIV, carulazstID_ULAZ_ZAG, carulazstID_ULAZ_STAV, carulazstRBR, carulazstCTG, carulazstCART, 
        carulazstCART1, carulazstBC, carulazstNAZART, carulazstJM, carulazstCSIFDRV, carulazstPREF, carulazstCPOTPOR, carulazstKOL, carulazstNETTO_KG, 
        carulazstKOL_KOM, carulazstCIJENA_VAL, carulazstVRIJEDNOST_VAL, carulazstTROSKOVI_VAL, carulazstTROS1_KN, carulazstTROS2_KN});
  }

  public void setall() {

    ddl.create("Carulazst")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("id_ulaz_zag", 8)
       .addInteger("id_ulaz_stav", 8, true)
       .addShort("rbr", 4)
       .addChar("ctg", 10)
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
       .addFloat("troskovi_val", 17, 2)
       .addFloat("tros1_kn", 17, 2)
       .addFloat("tros2_kn", 17, 2)
       .addPrimaryKey("id_ulaz_stav");


    Naziv = "Carulazst";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"id_ulaz_stav"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
