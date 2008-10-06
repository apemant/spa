/****license*****************************************************************
**   file: Stavkeblarh.java
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



public class Stavkeblarh extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Stavkeblarh Stavkeblarhclass;

  QueryDataSet sbla = new QueryDataSet();

  Column sblaLOKK = new Column();
  Column sblaAKTIV = new Column();
  Column sblaKNJIG = new Column();
  Column sblaCBLAG = new Column();
  Column sblaOZNVAL = new Column();
  Column sblaGODINA = new Column();
  Column sblaBRIZV = new Column();
  Column sblaRBS = new Column();
  Column sblaDATUM = new Column();
  Column sblaPRIMITAK = new Column();
  Column sblaIZDATAK = new Column();
  Column sblaPVPRIMITAK = new Column();
  Column sblaPVIZDATAK = new Column();
  Column sblaTECAJ = new Column();
  Column sblaCRADNIK = new Column();
  Column sblaCPN = new Column();
  Column sblaOPIS = new Column();
  Column sblaTKO = new Column();
  Column sblaCGRSTAV = new Column();
  Column sblaSTAVKA = new Column();
  Column sblaCSKL = new Column();
  Column sblaVRDOK = new Column();
  Column sblaCORG = new Column();
  Column sblaCNALOGA = new Column();
  Column sblaVRSTA = new Column();

  Column sblaBROJKONTA = new Column();//NEU
  Column sblaDATDOK = new Column();
  Column sblaDATDOSP = new Column();
  Column sblaBROJDOK = new Column();
  Column sblaCPAR = new Column();

  public static Stavkeblarh getDataModule() {
    if (Stavkeblarhclass == null) {
      Stavkeblarhclass = new Stavkeblarh();
    }
    return Stavkeblarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return sbla;
  }

  public Stavkeblarh() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    sblaLOKK.setCaption("Status zauzetosti");
    sblaLOKK.setColumnName("LOKK");
    sblaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaLOKK.setPrecision(1);
    sblaLOKK.setTableName("STAVKEBLARH");
    sblaLOKK.setServerColumnName("LOKK");
    sblaLOKK.setSqlType(1);
    sblaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sblaLOKK.setDefault("N");
    sblaAKTIV.setCaption("Aktivan - neaktivan");
    sblaAKTIV.setColumnName("AKTIV");
    sblaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaAKTIV.setPrecision(1);
    sblaAKTIV.setTableName("STAVKEBLARH");
    sblaAKTIV.setServerColumnName("AKTIV");
    sblaAKTIV.setSqlType(1);
    sblaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sblaAKTIV.setDefault("D");
    sblaKNJIG.setCaption("Knjigovodstvo");
    sblaKNJIG.setColumnName("KNJIG");
    sblaKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaKNJIG.setPrecision(12);
    sblaKNJIG.setRowId(true);
    sblaKNJIG.setTableName("STAVKEBLARH");
    sblaKNJIG.setServerColumnName("KNJIG");
    sblaKNJIG.setSqlType(1);
    sblaCBLAG.setCaption("Broj blagajne");
    sblaCBLAG.setColumnName("CBLAG");
    sblaCBLAG.setDataType(com.borland.dx.dataset.Variant.INT);
    sblaCBLAG.setPrecision(6);
    sblaCBLAG.setRowId(true);
    sblaCBLAG.setTableName("STAVKEBLARH");
    sblaCBLAG.setServerColumnName("CBLAG");
    sblaCBLAG.setSqlType(4);
    sblaCBLAG.setWidth(6);
    sblaOZNVAL.setCaption("Oznaka valute");
    sblaOZNVAL.setColumnName("OZNVAL");
    sblaOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaOZNVAL.setPrecision(3);
    sblaOZNVAL.setRowId(true);
    sblaOZNVAL.setTableName("STAVKEBLARH");
    sblaOZNVAL.setServerColumnName("OZNVAL");
    sblaOZNVAL.setSqlType(1);
    sblaGODINA.setCaption("Godina");
    sblaGODINA.setColumnName("GODINA");
    sblaGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    sblaGODINA.setPrecision(4);
    sblaGODINA.setRowId(true);
    sblaGODINA.setTableName("STAVKEBLARH");
    sblaGODINA.setServerColumnName("GODINA");
    sblaGODINA.setSqlType(5);
    sblaGODINA.setWidth(4);
    sblaBRIZV.setCaption("Broj blagajni\u010Dkog izvještaja");
    sblaBRIZV.setColumnName("BRIZV");
    sblaBRIZV.setDataType(com.borland.dx.dataset.Variant.INT);
    sblaBRIZV.setPrecision(6);
    sblaBRIZV.setRowId(true);
    sblaBRIZV.setTableName("STAVKEBLARH");
    sblaBRIZV.setServerColumnName("BRIZV");
    sblaBRIZV.setSqlType(4);
    sblaBRIZV.setWidth(6);
    sblaRBS.setCaption("RBS");
    sblaRBS.setColumnName("RBS");
    sblaRBS.setDataType(com.borland.dx.dataset.Variant.INT);
    sblaRBS.setPrecision(6);
    sblaRBS.setRowId(true);
    sblaRBS.setTableName("STAVKEBLARH");
    sblaRBS.setServerColumnName("RBS");
    sblaRBS.setSqlType(4);
    sblaRBS.setWidth(6);
    sblaDATUM.setCaption("Datum");
    sblaDATUM.setColumnName("DATUM");
    sblaDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    sblaDATUM.setPrecision(8);
    sblaDATUM.setDisplayMask("dd-MM-yyyy");
//    sblaDATUM.setEditMask("dd-MM-yyyy");
    sblaDATUM.setTableName("STAVKEBLARH");
    sblaDATUM.setWidth(10);
    sblaDATUM.setServerColumnName("DATUM");
    sblaDATUM.setSqlType(93);
    sblaPRIMITAK.setCaption("Primitak");
    sblaPRIMITAK.setColumnName("PRIMITAK");
    sblaPRIMITAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblaPRIMITAK.setPrecision(17);
    sblaPRIMITAK.setScale(2);
    sblaPRIMITAK.setDisplayMask("###,###,##0.00");
    sblaPRIMITAK.setDefault("0");
    sblaPRIMITAK.setTableName("STAVKEBLARH");
    sblaPRIMITAK.setServerColumnName("PRIMITAK");
    sblaPRIMITAK.setSqlType(2);
    sblaPRIMITAK.setDefault("0");
    sblaIZDATAK.setCaption("Izdatak");
    sblaIZDATAK.setColumnName("IZDATAK");
    sblaIZDATAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblaIZDATAK.setPrecision(17);
    sblaIZDATAK.setScale(2);
    sblaIZDATAK.setDisplayMask("###,###,##0.00");
    sblaIZDATAK.setDefault("0");
    sblaIZDATAK.setTableName("STAVKEBLARH");
    sblaIZDATAK.setServerColumnName("IZDATAK");
    sblaIZDATAK.setSqlType(2);
    sblaIZDATAK.setDefault("0");
    sblaPVPRIMITAK.setCaption("Primitak u dom.valuti");
    sblaPVPRIMITAK.setColumnName("PVPRIMITAK");
    sblaPVPRIMITAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblaPVPRIMITAK.setPrecision(17);
    sblaPVPRIMITAK.setScale(2);
    sblaPVPRIMITAK.setDisplayMask("###,###,##0.00");
    sblaPVPRIMITAK.setDefault("0");
    sblaPVPRIMITAK.setTableName("STAVKEBLARH");
    sblaPVPRIMITAK.setServerColumnName("PVPRIMITAK");
    sblaPVPRIMITAK.setSqlType(2);
    sblaPVPRIMITAK.setDefault("0");
    sblaPVIZDATAK.setCaption("Izdatak u dom.valuti");
    sblaPVIZDATAK.setColumnName("PVIZDATAK");
    sblaPVIZDATAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblaPVIZDATAK.setPrecision(17);
    sblaPVIZDATAK.setScale(2);
    sblaPVIZDATAK.setDisplayMask("###,###,##0.00");
    sblaPVIZDATAK.setDefault("0");
    sblaPVIZDATAK.setTableName("STAVKEBLARH");
    sblaPVIZDATAK.setServerColumnName("PVIZDATAK");
    sblaPVIZDATAK.setSqlType(2);
    sblaPVIZDATAK.setDefault("0");
    sblaTECAJ.setCaption("Te\u010Daj");
    sblaTECAJ.setColumnName("TECAJ");
    sblaTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblaTECAJ.setPrecision(12);
    sblaTECAJ.setScale(6);
    sblaTECAJ.setDisplayMask("###,###,##0.000000");
    sblaTECAJ.setDefault("0");
    sblaTECAJ.setTableName("STAVKEBLARH");
    sblaTECAJ.setServerColumnName("TECAJ");
    sblaTECAJ.setSqlType(2);
    sblaTECAJ.setDefault("1");
    sblaCRADNIK.setCaption("Radnik");
    sblaCRADNIK.setColumnName("CRADNIK");
    sblaCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaCRADNIK.setPrecision(6);
    sblaCRADNIK.setTableName("STAVKEBLARH");
    sblaCRADNIK.setServerColumnName("CRADNIK");
    sblaCRADNIK.setSqlType(1);
    sblaCPN.setCaption("Putni nalog");
    sblaCPN.setColumnName("CPN");
    sblaCPN.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaCPN.setPrecision(24);
    sblaCPN.setTableName("STAVKEBLARH");
    sblaCPN.setServerColumnName("CPN");
    sblaCPN.setSqlType(1);
    sblaOPIS.setCaption("Opis");
    sblaOPIS.setColumnName("OPIS");
    sblaOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaOPIS.setPrecision(50);
    sblaOPIS.setTableName("STAVKEBLARH");
    sblaOPIS.setServerColumnName("OPIS");
    sblaOPIS.setSqlType(1);
    sblaTKO.setCaption("Kome / od koga");
    sblaTKO.setColumnName("TKO");
    sblaTKO.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaTKO.setPrecision(50);
    sblaTKO.setTableName("STAVKEBLARH");
    sblaTKO.setServerColumnName("TKO");
    sblaTKO.setSqlType(1);
    sblaCGRSTAV.setCaption("Grupa");
    sblaCGRSTAV.setColumnName("CGRSTAV");
    sblaCGRSTAV.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaCGRSTAV.setPrecision(5);
    sblaCGRSTAV.setTableName("STAVKEBLARH");
    sblaCGRSTAV.setServerColumnName("CGRSTAV");
    sblaCGRSTAV.setSqlType(1);
    sblaSTAVKA.setCaption("Veza sa kontom preko SHKONTA.VRDOK=BL");
    sblaSTAVKA.setColumnName("STAVKA");
    sblaSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaSTAVKA.setPrecision(8);
    sblaSTAVKA.setTableName("STAVKEBLARH");
    sblaSTAVKA.setServerColumnName("STAVKA");
    sblaSTAVKA.setSqlType(1);
    sblaCSKL.setCaption("Veza sa kontom preko SHKONTA");
    sblaCSKL.setColumnName("CSKL");
    sblaCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaCSKL.setPrecision(12);
    sblaCSKL.setTableName("STAVKEBLARH");
    sblaCSKL.setServerColumnName("CSKL");
    sblaCSKL.setSqlType(1);
    sblaVRDOK.setCaption("Vrsta dokumenta");
    sblaVRDOK.setColumnName("VRDOK");
    sblaVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaVRDOK.setPrecision(3);
    sblaVRDOK.setTableName("STAVKEBLARH");
    sblaVRDOK.setServerColumnName("VRDOK");
    sblaVRDOK.setSqlType(1);
    sblaCORG.setCaption("Org. jedinica");
    sblaCORG.setColumnName("CORG");
    sblaCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaCORG.setPrecision(12);
    sblaCORG.setTableName("STAVKEBLARH");
    sblaCORG.setServerColumnName("CORG");
    sblaCORG.setSqlType(1);
    sblaCNALOGA.setCaption("Oznaka naloga u GK");
    sblaCNALOGA.setColumnName("CNALOGA");
    sblaCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaCNALOGA.setPrecision(30);
    sblaCNALOGA.setTableName("STAVKEBLARH");
    sblaCNALOGA.setServerColumnName("CNALOGA");
    sblaCNALOGA.setSqlType(1);
    sbla.setResolver(dm.getQresolver());
    
    sblaVRSTA.setCaption("Uplatnica Isplatnica");
    sblaVRSTA.setColumnName("VRSTA");
    sblaVRSTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaVRSTA.setPrecision(1);
    sblaVRSTA.setRowId(true);
    sblaVRSTA.setTableName("STAVBLAG");
    sblaVRSTA.setServerColumnName("VRSTA");
    sblaVRSTA.setSqlType(1);   
    
    sblaBROJKONTA.setCaption("Konto");//neu
    sblaBROJKONTA.setColumnName("BROJKONTA");
    sblaBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaBROJKONTA.setPrecision(8);
    sblaBROJKONTA.setTableName("STAVKEBLARH");
    sblaBROJKONTA.setServerColumnName("BROJKONTA");
    sblaBROJKONTA.setSqlType(1);

    sblaDATDOK.setCaption("Datum dokumenta");
    sblaDATDOK.setColumnName("DATDOK");
    sblaDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    sblaDATDOK.setPrecision(8);
    sblaDATDOK.setDisplayMask("dd-MM-yyyy");
    sblaDATDOK.setTableName("STAVKEBLARH");
    sblaDATDOK.setServerColumnName("DATDOK");
    sblaDATDOK.setSqlType(93);
    sblaDATDOK.setWidth(10);

    sblaDATDOSP.setCaption("Datum dospje\u0107a");
    sblaDATDOSP.setColumnName("DATDOSP");
    sblaDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    sblaDATDOSP.setPrecision(8);
    sblaDATDOSP.setDisplayMask("dd-MM-yyyy");
    sblaDATDOSP.setTableName("STAVKEBLARH");
    sblaDATDOSP.setServerColumnName("DATDOSP");
    sblaDATDOSP.setSqlType(93);
    sblaDATDOSP.setWidth(10);

    sblaBROJDOK.setCaption("Broj dokumenta");
    sblaBROJDOK.setColumnName("BROJDOK");
    sblaBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblaBROJDOK.setPrecision(50);
    sblaBROJDOK.setTableName("STAVKEBLARH");
    sblaBROJDOK.setServerColumnName("BROJDOK");
    sblaBROJDOK.setSqlType(1);
    sblaBROJDOK.setWidth(30);

    sblaCPAR.setCaption("Partner");
    sblaCPAR.setColumnName("CPAR");
    sblaCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    sblaCPAR.setPrecision(6);
    sblaCPAR.setTableName("STAVKEBLARH");
    sblaCPAR.setServerColumnName("CPAR");
    sblaCPAR.setSqlType(4);
    sblaCPAR.setWidth(6);
    
    sbla.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Stavkeblarh", null, true, Load.ALL));
 setColumns(new Column[] {sblaLOKK, sblaAKTIV, sblaKNJIG, sblaCBLAG, sblaOZNVAL, sblaGODINA, sblaBRIZV, sblaRBS, sblaDATUM, sblaPRIMITAK, sblaIZDATAK,
        sblaPVPRIMITAK, sblaPVIZDATAK, sblaTECAJ, sblaCRADNIK, sblaCPN, sblaOPIS, sblaTKO, sblaCGRSTAV, sblaSTAVKA, sblaCSKL, sblaVRDOK, sblaCORG, sblaCNALOGA, sblaVRSTA,
        sblaBROJKONTA,sblaDATDOK,sblaDATDOSP,sblaBROJDOK,sblaCPAR});
  }

  public void setall() {

    ddl.create("Stavkeblarh")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addInteger("cblag", 6, true)
       .addChar("oznval", 3, true)
       .addShort("godina", 4, true)
       .addInteger("brizv", 6, true)
       .addInteger("rbs", 6, true)
       .addDate("datum")
       .addFloat("primitak", 17, 2)
       .addFloat("izdatak", 17, 2)
       .addFloat("pvprimitak", 17, 2)
       .addFloat("pvizdatak", 17, 2)
       .addFloat("tecaj", 12, 6)
       .addChar("cradnik", 6)
       .addChar("cpn", 24)
       .addChar("opis", 50)
       .addChar("tko", 50)
       .addChar("cgrstav", 5)
       .addChar("stavka", 8)
       .addChar("cskl", 12)
       .addChar("vrdok", 3)
       .addChar("corg", 12)
       .addChar("cnaloga", 30)
       .addChar("vrsta", 1, true)
       .addChar("brojkonta", 8) //neu
       .addDate("datdok")
       .addDate("datdosp")
       .addChar("brojdok", 50)
       .addInteger("cpar", 6)
       .addPrimaryKey("knjig,cblag,oznval,godina,brizv,rbs,vrsta");


    Naziv = "Stavkeblarh";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brizv", "rbs"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
