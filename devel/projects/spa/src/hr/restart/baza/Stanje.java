/****license*****************************************************************
**   file: Stanje.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import java.util.ResourceBundle;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Stanje extends KreirDrop implements DataModule {

  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Stanje Stanjeclass;
  QueryDataSet stanje = new QueryDataSet();
  /*Column stanjeLOKK = new Column();
  Column stanjeAKTIV = new Column();
  Column stanjeGOD = new Column();
  Column stanjeCSKL = new Column();
  Column stanjeCART = new Column();
  Column stanjeKOLPS = new Column();
  Column stanjeKOLUL = new Column();
  Column stanjeKOLIZ = new Column();
  Column stanjeKOLREZ = new Column();
  Column stanjeNABPS = new Column();
  Column stanjeMARPS = new Column();
  Column stanjePORPS = new Column();
  Column stanjeVPS = new Column();
  Column stanjeNABUL = new Column();
  Column stanjeMARUL = new Column();
  Column stanjePORUL = new Column();
  Column stanjeVUL = new Column();
  Column stanjeNABIZ = new Column();
  Column stanjeMARIZ = new Column();
  Column stanjePORIZ = new Column();
  Column stanjeVIZ = new Column();
  Column stanjeKOL = new Column();
  Column stanjeZC = new Column();
  Column stanjeVRI = new Column();
  Column stanjeNC = new Column();
  Column stanjeVC = new Column();
  Column stanjeMC = new Column();
  Column stanjeDATZK = new Column();
  Column stanjeSKAL = new Column();
  Column stanjeTKAL = new Column();
  Column stanjeITKAL = new Column();
  Column stanjeSITKAL = new Column();
  Column stanjeBSIZ = new Column();
  Column stanjeSBSIZ = new Column();
  Column stanjeKOLMAT = new Column();
  Column stanjeKOLSKLADPS = new Column();
  Column stanjeKOLSKLADUL = new Column();
  Column stanjeKOLSKLADIZ = new Column();
  Column stanjeKOLSKLAD = new Column();*/

  public static Stanje getDataModule() {
    if (Stanjeclass == null) {
      Stanjeclass = new Stanje();
    }
    return Stanjeclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return stanje;
  }
  public Stanje() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    /*stanjeDATZK.setCaption("Datum zadnje kalkulacije");
    stanjeDATZK.setColumnName("DATZK");
    stanjeDATZK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    stanjeDATZK.setDisplayMask("dd-MM-yyyy");
    stanjeDATZK.setEditMask("dd-MM-yyyy");
    stanjeDATZK.setTableName("STANJE");
    stanjeDATZK.setWidth(5);
    stanjeDATZK.setSqlType(93);
    stanjeDATZK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeDATZK.setServerColumnName("DATZK");

    stanjeMC.setCaption("Cijena s porezom");
    stanjeMC.setColumnName("MC");
    stanjeMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeMC.setDisplayMask("###,###,##0.00");
    stanjeMC.setDefault("0");
    stanjeMC.setPrecision(15);
    stanjeMC.setScale(2);
    stanjeMC.setTableName("STANJE");
    stanjeMC.setServerColumnName("MC");
    stanjeMC.setSqlType(2);
    stanjeVC.setCaption("Cijena bez poreza");
    stanjeVC.setColumnName("VC");
    stanjeVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeVC.setDisplayMask("###,###,##0.00");
    stanjeVC.setDefault("0");
    stanjeVC.setPrecision(15);
    stanjeVC.setScale(2);
    stanjeVC.setTableName("STANJE");
    stanjeVC.setServerColumnName("VC");
    stanjeVC.setSqlType(2);
    stanjeNC.setCaption("Nabavna cijena");
    stanjeNC.setColumnName("NC");
    stanjeNC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeNC.setDisplayMask("###,###,##0.0000");
    stanjeNC.setDefault("0");
    stanjeNC.setPrecision(15);
    stanjeNC.setScale(2);
    stanjeNC.setTableName("STANJE");
    stanjeNC.setServerColumnName("NC");
    stanjeNC.setSqlType(2);
    stanjeVRI.setCaption("Vrijednost");
    stanjeVRI.setColumnName("VRI");
    stanjeVRI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeVRI.setDisplayMask("###,###,##0.00");
    stanjeVRI.setDefault("0");
    stanjeVRI.setPrecision(15);
    stanjeVRI.setScale(2);
    stanjeVRI.setTableName("STANJE");
    stanjeVRI.setServerColumnName("VRI");
    stanjeVRI.setSqlType(2);
    stanjeZC.setCaption("Cijena");
    stanjeZC.setColumnName("ZC");
    stanjeZC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeZC.setDisplayMask("###,###,##0.0000");
    stanjeZC.setDefault("0");
    stanjeZC.setPrecision(15);
    stanjeZC.setScale(2);
    stanjeZC.setTableName("STANJE");
    stanjeZC.setServerColumnName("ZC");
    stanjeZC.setSqlType(2);
    stanjeKOL.setCaption("Koli\u010Dina");
    stanjeKOL.setColumnName("KOL");
    stanjeKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOL.setDisplayMask("###,###,##0.000");
    stanjeKOL.setDefault("0");
    stanjeKOL.setPrecision(15);
    stanjeKOL.setScale(3);
    stanjeKOL.setTableName("STANJE");
    stanjeKOL.setServerColumnName("KOL");
    stanjeKOL.setSqlType(2);
    stanjeVIZ.setCaption("Vrijednost izlaza");
    stanjeVIZ.setColumnName("VIZ");
    stanjeVIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeVIZ.setDisplayMask("###,###,##0.00");
    stanjeVIZ.setDefault("0");
    stanjeVIZ.setPrecision(15);
    stanjeVIZ.setScale(2);
    stanjeVIZ.setTableName("STANJE");
    stanjeVIZ.setServerColumnName("VIZ");
    stanjeVIZ.setSqlType(2);
    stanjePORIZ.setCaption("Porez vrijednosti izlaza");
    stanjePORIZ.setColumnName("PORIZ");
    stanjePORIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjePORIZ.setDisplayMask("###,###,##0.00");
    stanjePORIZ.setDefault("0");
    stanjePORIZ.setPrecision(15);
    stanjePORIZ.setScale(2);
    stanjePORIZ.setTableName("STANJE");
    stanjePORIZ.setServerColumnName("PORIZ");
    stanjePORIZ.setSqlType(2);
    stanjeMARIZ.setCaption("Marža vrijednosti izlaza");
    stanjeMARIZ.setColumnName("MARIZ");
    stanjeMARIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeMARIZ.setDisplayMask("###,###,##0.00");
    stanjeMARIZ.setDefault("0");
    stanjeMARIZ.setPrecision(15);
    stanjeMARIZ.setScale(2);
    stanjeMARIZ.setTableName("STANJE");
    stanjeMARIZ.setServerColumnName("MARIZ");
    stanjeMARIZ.setSqlType(2);
    stanjeNABIZ.setCaption("Nabavna vrijednost izlaza");
    stanjeNABIZ.setColumnName("NABIZ");
    stanjeNABIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeNABIZ.setDisplayMask("###,###,##0.00");
    stanjeNABIZ.setDefault("0");
    stanjeNABIZ.setPrecision(15);
    stanjeNABIZ.setScale(2);
    stanjeNABIZ.setTableName("STANJE");
    stanjeNABIZ.setServerColumnName("NABIZ");
    stanjeNABIZ.setSqlType(2);
    stanjeVUL.setCaption("Vrijednost ulaza");
    stanjeVUL.setColumnName("VUL");
    stanjeVUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeVUL.setDisplayMask("###,###,##0.00");
    stanjeVUL.setDefault("0");
    stanjeVUL.setPrecision(15);
    stanjeVUL.setScale(2);
    stanjeVUL.setTableName("STANJE");
    stanjeVUL.setServerColumnName("VUL");
    stanjeVUL.setSqlType(2);
    stanjePORUL.setCaption("Porez vrijednosti ulaza");
    stanjePORUL.setColumnName("PORUL");
    stanjePORUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjePORUL.setDisplayMask("###,###,##0.00");
    stanjePORUL.setDefault("0");
    stanjePORUL.setPrecision(15);
    stanjePORUL.setScale(2);
    stanjePORUL.setTableName("STANJE");
    stanjePORUL.setServerColumnName("PORUL");
    stanjePORUL.setSqlType(2);
    stanjeMARUL.setCaption("Marža vrijednosti ulaza");
    stanjeMARUL.setColumnName("MARUL");
    stanjeMARUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeMARUL.setDisplayMask("###,###,##0.00");
    stanjeMARUL.setDefault("0");
    stanjeMARUL.setPrecision(15);
    stanjeMARUL.setScale(2);
    stanjeMARUL.setTableName("STANJE");
    stanjeMARUL.setServerColumnName("MARUL");
    stanjeMARUL.setSqlType(2);
    stanjeNABUL.setCaption("Nabavna vrijednost ulaza");
    stanjeNABUL.setColumnName("NABUL");
    stanjeNABUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeNABUL.setDisplayMask("###,###,##0.00");
    stanjeNABUL.setDefault("0");
    stanjeNABUL.setPrecision(15);
    stanjeNABUL.setScale(2);
    stanjeNABUL.setTableName("STANJE");
    stanjeNABUL.setServerColumnName("NABUL");
    stanjeNABUL.setSqlType(2);
    stanjeVPS.setCaption("Vrijednost po\u010Detnog stanja");
    stanjeVPS.setColumnName("VPS");
    stanjeVPS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeVPS.setDisplayMask("###,###,##0.00");
    stanjeVPS.setDefault("0");
    stanjeVPS.setPrecision(15);
    stanjeVPS.setScale(2);
    stanjeVPS.setTableName("STANJE");
    stanjeVPS.setServerColumnName("VPS");
    stanjeVPS.setSqlType(2);
    stanjePORPS.setCaption("Porez po\u010Detnog stanja");
    stanjePORPS.setColumnName("PORPS");
    stanjePORPS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjePORPS.setDisplayMask("###,###,##0.00");
    stanjePORPS.setDefault("0");
    stanjePORPS.setPrecision(15);
    stanjePORPS.setScale(2);
    stanjePORPS.setTableName("STANJE");
    stanjePORPS.setServerColumnName("PORPS");
    stanjePORPS.setSqlType(2);
    stanjeMARPS.setCaption("Marža po\\u10detnog stanja");
    stanjeMARPS.setColumnName("MARPS");
    stanjeMARPS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeMARPS.setDisplayMask("###,###,##0.00");
    stanjeMARPS.setDefault("0");
    stanjeMARPS.setPrecision(15);
    stanjeMARPS.setScale(2);
    stanjeMARPS.setTableName("STANJE");
    stanjeMARPS.setServerColumnName("MARPS");
    stanjeMARPS.setSqlType(2);
    stanjeNABPS.setCaption("Nabavna vrijednost po\\u10detnog stanja");
    stanjeNABPS.setColumnName("NABPS");
    stanjeNABPS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeNABPS.setDisplayMask("###,###,##0.00");
    stanjeNABPS.setDefault("0");
    stanjeNABPS.setPrecision(15);
    stanjeNABPS.setScale(2);
    stanjeNABPS.setTableName("STANJE");
    stanjeNABPS.setServerColumnName("NABPS");
    stanjeNABPS.setSqlType(2);
    stanjeKOLREZ.setCaption("Rezervirano");
    stanjeKOLREZ.setColumnName("KOLREZ");
    stanjeKOLREZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLREZ.setDisplayMask("###,###,##0.000");
    stanjeKOLREZ.setDefault("0");
    stanjeKOLREZ.setPrecision(15);
    stanjeKOLREZ.setScale(3);
    stanjeKOLREZ.setTableName("STANJE");
    stanjeKOLREZ.setServerColumnName("KOLREZ");
    stanjeKOLREZ.setSqlType(2);
    stanjeKOLIZ.setCaption("Izlaz");
    stanjeKOLIZ.setColumnName("KOLIZ");
    stanjeKOLIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLIZ.setDisplayMask("###,###,##0.000");
    stanjeKOLIZ.setDefault("0");
    stanjeKOLIZ.setPrecision(15);
    stanjeKOLIZ.setScale(3);
    stanjeKOLIZ.setTableName("STANJE");
    stanjeKOLIZ.setServerColumnName("KOLIZ");
    stanjeKOLIZ.setSqlType(2);
    stanjeKOLUL.setCaption("Ulaz");
    stanjeKOLUL.setColumnName("KOLUL");
    stanjeKOLUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLUL.setDisplayMask("###,###,##0.000");
    stanjeKOLUL.setDefault("0");
    stanjeKOLUL.setPrecision(15);
    stanjeKOLUL.setScale(3);
    stanjeKOLUL.setTableName("STANJE");
    stanjeKOLUL.setServerColumnName("KOLUL");
    stanjeKOLUL.setSqlType(2);
    stanjeKOLPS.setCaption("Po\u010Detno stanje");
    stanjeKOLPS.setColumnName("KOLPS");
    stanjeKOLPS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLPS.setDisplayMask("###,###,##0.000");
    stanjeKOLPS.setDefault("0");
    stanjeKOLPS.setPrecision(15);
    stanjeKOLPS.setScale(3);
    stanjeKOLPS.setTableName("STANJE");
    stanjeKOLPS.setServerColumnName("KOLPS");
    stanjeKOLPS.setSqlType(2);
    stanjeCART.setCaption("Artikl");
    stanjeCART.setColumnName("CART");
    stanjeCART.setDataType(com.borland.dx.dataset.Variant.INT);
    stanjeCART.setRowId(true);
    stanjeCART.setTableName("STANJE");
    stanjeCART.setServerColumnName("CART");
    stanjeCART.setSqlType(4);
    stanjeCSKL.setCaption("Skladi\u0161te");
    stanjeCSKL.setColumnName("CSKL");
    stanjeCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stanjeCSKL.setPrecision(12);
    stanjeCSKL.setRowId(true);
    stanjeCSKL.setTableName("STANJE");
    stanjeCSKL.setServerColumnName("CSKL");
    stanjeCSKL.setSqlType(1);
    stanjeGOD.setCaption("Godina");
    stanjeGOD.setColumnName("GOD");
    stanjeGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    stanjeGOD.setPrecision(4);
    stanjeGOD.setRowId(true);
    stanjeGOD.setTableName("STANJE");
    stanjeGOD.setServerColumnName("GOD");
    stanjeGOD.setSqlType(1);
    stanjeAKTIV.setColumnName("AKTIV");
    stanjeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    stanjeAKTIV.setDefault("D");
    stanjeAKTIV.setPrecision(1);
    stanjeAKTIV.setTableName("STANJE");
    stanjeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeAKTIV.setServerColumnName("AKTIV");
    stanjeAKTIV.setSqlType(1);
    stanjeLOKK.setColumnName("LOKK");
    stanjeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    stanjeLOKK.setDefault("N");
    stanjeLOKK.setPrecision(1);
    stanjeLOKK.setTableName("STANJE");
    stanjeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeLOKK.setServerColumnName("LOKK");
    stanjeLOKK.setSqlType(1);

    stanjeSKAL.setCaption("Prethodni broj kalkulacije");
    stanjeSKAL.setColumnName("SKAL");
    stanjeSKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stanjeSKAL.setPrecision(52);
    stanjeSKAL.setTableName("STANJE");
    stanjeSKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeSKAL.setServerColumnName("SKAL");
    stanjeSKAL.setSqlType(1);

    stanjeTKAL.setCaption("Tekuæi broj kalkulacije");
    stanjeTKAL.setColumnName("TKAL");
    stanjeTKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stanjeTKAL.setPrecision(52);
    stanjeTKAL.setTableName("STANJE");
    stanjeTKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeTKAL.setServerColumnName("TKAL");
    stanjeTKAL.setSqlType(1);

    stanjeITKAL.setCaption("Broj kalkulacije zadnjeg izlaza");
    stanjeITKAL.setColumnName("ITKAL");
    stanjeITKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stanjeITKAL.setPrecision(52);
    stanjeITKAL.setTableName("STANJE");
    stanjeITKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeITKAL.setServerColumnName("ITKAL");
    stanjeITKAL.setSqlType(1);

    stanjeSITKAL.setCaption("Broj prethodne kalkulacije izlaza");
    stanjeSITKAL.setColumnName("SITKAL");
    stanjeSITKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stanjeSITKAL.setPrecision(52);
    stanjeSITKAL.setTableName("STANJE");
    stanjeSITKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeSITKAL.setServerColumnName("SITKAL");
    stanjeSITKAL.setSqlType(1);

    stanjeBSIZ.setCaption("Broj stavki izlaza napravljenih po zadnjem izlazu");
    stanjeBSIZ.setColumnName("BSIZ");
    stanjeBSIZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stanjeBSIZ.setPrecision(4);
    stanjeBSIZ.setTableName("STANJE");
    stanjeBSIZ.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeBSIZ.setServerColumnName("BSIZ");
    stanjeBSIZ.setSqlType(5);

    stanjeSBSIZ.setCaption("Broj stavki izlaza napravljenih po prethodnom izlazu");
    stanjeSBSIZ.setColumnName("SBSIZ");
    stanjeSBSIZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stanjeSBSIZ.setPrecision(4);
    stanjeSBSIZ.setTableName("STANJE");
    stanjeSBSIZ.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeSBSIZ.setServerColumnName("SBSIZ");
    stanjeSBSIZ.setSqlType(5);

    stanjeKOLMAT.setCaption("Kolmat");
    stanjeKOLMAT.setColumnName("KOLMAT");
    stanjeKOLMAT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLMAT.setDisplayMask("###,###,##0.000");
    stanjeKOLMAT.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stanjeKOLMAT.setDefault("0");
    stanjeKOLMAT.setPrecision(15);
    stanjeKOLMAT.setScale(3);
    stanjeKOLMAT.setTableName("STANJE");
    stanjeKOLMAT.setServerColumnName("KOLMAT");
    stanjeKOLMAT.setSqlType(2);

    stanjeKOLSKLAD.setCaption("Kol. sklad.");
    stanjeKOLSKLAD.setColumnName("KOLSKLAD");
    stanjeKOLSKLAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLSKLAD.setDisplayMask("###,###,##0.000");
    stanjeKOLSKLAD.setDefault("0");
    stanjeKOLSKLAD.setPrecision(15);
    stanjeKOLSKLAD.setScale(3);
    stanjeKOLSKLAD.setTableName("STANJE");
    stanjeKOLSKLAD.setServerColumnName("KOLSKLAD");
    stanjeKOLSKLAD.setSqlType(2);
    stanjeKOLSKLADIZ.setCaption("Izlaz");
    stanjeKOLSKLADIZ.setColumnName("KOLSKLADIZ");
    stanjeKOLSKLADIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLSKLADIZ.setDisplayMask("###,###,##0.000");
    stanjeKOLSKLADIZ.setDefault("0");
    stanjeKOLSKLADIZ.setPrecision(15);
    stanjeKOLSKLADIZ.setScale(3);
    stanjeKOLSKLADIZ.setTableName("STANJE");
    stanjeKOLSKLADIZ.setServerColumnName("KOLSKLADIZ");
    stanjeKOLSKLADIZ.setSqlType(2);
    stanjeKOLSKLADUL.setCaption("Ulaz");
    stanjeKOLSKLADUL.setColumnName("KOLSKLADUL");
    stanjeKOLSKLADUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLSKLADUL.setDisplayMask("###,###,##0.000");
    stanjeKOLSKLADUL.setDefault("0");
    stanjeKOLSKLADUL.setPrecision(15);
    stanjeKOLSKLADUL.setScale(3);
    stanjeKOLSKLADUL.setTableName("STANJE");
    stanjeKOLSKLADUL.setServerColumnName("KOLSKLADUL");
    stanjeKOLSKLADUL.setSqlType(2);
    stanjeKOLSKLADPS.setCaption("Po\u010Detno stanje");
    stanjeKOLSKLADPS.setColumnName("KOLSKLADPS");
    stanjeKOLSKLADPS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stanjeKOLSKLADPS.setDisplayMask("###,###,##0.000");
    stanjeKOLSKLADPS.setDefault("0");
    stanjeKOLSKLADPS.setPrecision(15);
    stanjeKOLSKLADPS.setScale(3);
    stanjeKOLSKLADPS.setTableName("STANJE");
    stanjeKOLSKLADPS.setServerColumnName("KOLSKLADPS");
    stanjeKOLSKLADPS.setSqlType(2);
    
    
    stanje.setResolver(dm.getQresolver());
    stanje.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM STANJE", null, true, Load.ALL));
 setColumns(new Column[] {stanjeLOKK, stanjeAKTIV, stanjeGOD, stanjeCSKL, stanjeCART, stanjeKOLPS, stanjeKOLUL, stanjeKOLIZ, stanjeKOLREZ, stanjeNABPS, stanjeMARPS, stanjePORPS,
        stanjeVPS, stanjeNABUL, stanjeMARUL, stanjePORUL, stanjeVUL, stanjeNABIZ, stanjeMARIZ, stanjePORIZ, stanjeVIZ, stanjeKOL, stanjeZC, stanjeVRI, stanjeNC, stanjeVC,
        stanjeMC, stanjeDATZK, stanjeSKAL, stanjeTKAL, stanjeITKAL, stanjeSITKAL, stanjeBSIZ, stanjeSBSIZ, stanjeKOLMAT, stanjeKOLSKLAD, stanjeKOLSKLADUL, stanjeKOLSKLADIZ, stanjeKOLSKLADPS});
*/  
    initModule();
  }
  
  protected void modifyColumn(Column c) {
    if (c.getColumnName().equals("NC") || c.getColumnName().equals("ZC")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "skladDec", 
          "2", "Broj decimala za skladišne cijene (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
  }

  //public void setall(){

/*    SqlDefTabela = "create table Stanje " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
      "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
      "cskl char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta
      "cart numeric(6,0) not null, "+   // Šifra artikl broj\u010Danik
      "kolps  numeric(17,3) ," + // Koli\u010Dina po\u010Detnog stanja
      "kolul  numeric(17,3) ," + // Koli\u010Dina ulazna
      "koliz  numeric(17,3) ," + // Koli\u010Dina izlazna
      "kolrez numeric(17,3) , "+ // Rezervirana koli\u010Dina
      "nabps  numeric(17,2) ," + // Nabavna vrijednost po\u010D.stanja
      "marps  numeric(17,2),"  + // Marža vrijed. po\u010Detnog stanja
      "porps  numeric(17,2),"  + // Porez vrijed. po\u010Detnog stanja
      "vps    numeric(17,2),"  + // Vrijednost po\u010Detnog stanja
      "nabul  numeric(17,2),"  + // Nabavna vrijed. ulaza
      "marul  numeric(17,2) ," + // Marža vrijed. ulaza
      "porul  numeric(17,2),"  + // Porez vrijed. ulaza
      "vul    numeric(17,2) ," + // Vrijednost ulaza
      "nabiz  numeric(17,2),"  + // Nabavna vrijed. izlaza
      "mariz  numeric(17,2) ," + // Marža vrijed. izlaza
      "poriz  numeric(17,2),"  + // Porez vrijed. izlaza
      "viz    numeric(17,2) ," + // Vrijednost izlaza
      "kol    numeric(17,3),"  + // Koli\u010Dina na zalihi
      "zc     numeric(12,4),"  + // Cijena zalihe
      "vri    numeric(17,2),"  + // Vrijednost zalihe
      "nc     numeric(12,4) ," + // Prosje\u010Dna nabavna cijena
      "vc     numeric(12,2)," + // Prodajna cijena bez poreza
      "mc     numeric(12,2) ," + // Prodajna cijena s porezom
      "Primary Key (god,cskl,cart))" ; */

 /*   ddl.create("stanje")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("god", 4, true)
       .addChar("cskl", 12, true)
       .addInteger("cart", 6, true)
       .addFloat("kolps", 17, 3)
       .addFloat("kolul", 17, 3)
       .addFloat("koliz", 17, 3)
       .addFloat("kolrez", 17, 3)
       .addFloat("nabps", 17, 2)
       .addFloat("marps", 17, 2)
       .addFloat("porps", 17, 2)
       .addFloat("vps", 17, 2)
       .addFloat("nabul", 17, 2)
       .addFloat("marul", 17, 2)
       .addFloat("porul", 17, 2)
       .addFloat("vul", 17, 2)
       .addFloat("nabiz", 17, 2)
       .addFloat("mariz", 17, 2)
       .addFloat("poriz", 17, 2)
       .addFloat("viz", 17, 2)
       .addFloat("kol", 17, 3)
       .addFloat("zc", 12, 2)
       .addFloat("vri", 17, 2)
       .addFloat("nc", 12, 2)
       .addFloat("vc", 12, 2)
       .addFloat("mc", 12, 2)
       .addDate("datzk")
       .addChar("skal", 52)
       .addChar("tkal", 52)
       .addChar("itkal", 52)
       .addChar("sitkal", 52)
       .addShort("bsiz", 4)
       .addShort("sbsiz", 4)
       .addFloat("kolmat", 17, 3)
       .addFloat("kolsklad", 17, 3)
       .addFloat("kolskladul", 17, 3)
       .addFloat("kolskladiz", 17, 3)
       .addFloat("kolskladps", 17, 3)
       .addPrimaryKey("god,cskl,cart");

    Naziv="Stanje";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cart"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);*/

/*
    NaziviIdx=new String[]{"ilokkstanje", "iaktivstanje", "igodstanje", "icsklstanje",
                            "icartstanje", "ipkstanje", "ikolstanje", "izcstanje", "ivristanje"} ;

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Stanje (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Stanje (aktiv)",
                            CommonTable.SqlDefIndex+NaziviIdx[2] +" on Stanje (god)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on Stanje (cskl)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[4] +" on Stanje (cart)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[5] +" on Stanje (god,cskl,cart)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[6] +" on Stanje (kol)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[7] +" on Stanje (zc)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[8] +" on Stanje (vri)" };
    */
//    }
}
