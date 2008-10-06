/****license*****************************************************************
**   file: Stavblag.java
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



public class Stavblag extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Stavblag Stavblagclass;

  //QueryDataSet sbl = new QueryDataSet();
  raDataSet sbl = new raDataSet();

  Column sblLOKK = new Column();
  Column sblAKTIV = new Column();
  Column sblKNJIG = new Column();
  Column sblCBLAG = new Column();
  Column sblOZNVAL = new Column();
  Column sblGODINA = new Column();
  Column sblBRIZV = new Column();
  Column sblRBS = new Column();
  Column sblDATUM = new Column();
  Column sblPRIMITAK = new Column();
  Column sblIZDATAK = new Column();
  Column sblPVPRIMITAK = new Column();
  Column sblPVIZDATAK = new Column();
  Column sblTECAJ = new Column();
  Column sblCRADNIK = new Column();
  Column sblCPN = new Column();
  Column sblOPIS = new Column();
  Column sblTKO = new Column();
  Column sblCGRSTAV = new Column();
  Column sblSTAVKA = new Column();
  Column sblCSKL = new Column();
  Column sblVRDOK = new Column();
  Column sblCORG = new Column();
  Column sblVRSTA = new Column();

  Column sblBROJKONTA = new Column();
  Column sblDATDOK = new Column();
  Column sblDATDOSP = new Column();
  Column sblBROJDOK = new Column();
  Column sblCPAR = new Column();

  public static Stavblag getDataModule() {
    if (Stavblagclass == null) {
      Stavblagclass = new Stavblag();
    }
    return Stavblagclass;
  }

  public QueryDataSet getQueryDataSet() {
    return sbl;
  }

  public Stavblag() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    sblLOKK.setCaption("Status zauzetosti");
    sblLOKK.setColumnName("LOKK");
    sblLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblLOKK.setPrecision(1);
    sblLOKK.setTableName("STAVBLAG");
    sblLOKK.setServerColumnName("LOKK");
    sblLOKK.setSqlType(1);
    sblLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sblLOKK.setDefault("N");
    sblAKTIV.setCaption("Aktivan - neaktivan");
    sblAKTIV.setColumnName("AKTIV");
    sblAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblAKTIV.setPrecision(1);
    sblAKTIV.setTableName("STAVBLAG");
    sblAKTIV.setServerColumnName("AKTIV");
    sblAKTIV.setSqlType(1);
    sblAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sblAKTIV.setDefault("D");
    sblKNJIG.setCaption("Knjigovodstvo");
    sblKNJIG.setColumnName("KNJIG");
    sblKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblKNJIG.setPrecision(12);
    sblKNJIG.setRowId(true);
    sblKNJIG.setTableName("STAVBLAG");
    sblKNJIG.setServerColumnName("KNJIG");
    sblKNJIG.setSqlType(1);
    sblCBLAG.setCaption("Broj blagajne");
    sblCBLAG.setColumnName("CBLAG");
    sblCBLAG.setDataType(com.borland.dx.dataset.Variant.INT);
    sblCBLAG.setPrecision(6);
    sblCBLAG.setRowId(true);
    sblCBLAG.setTableName("STAVBLAG");
    sblCBLAG.setServerColumnName("CBLAG");
    sblCBLAG.setSqlType(4);
    sblCBLAG.setWidth(6);
    sblOZNVAL.setCaption("Oznaka valute");
    sblOZNVAL.setColumnName("OZNVAL");
    sblOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblOZNVAL.setPrecision(3);
    sblOZNVAL.setRowId(true);
    sblOZNVAL.setTableName("STAVBLAG");
    sblOZNVAL.setServerColumnName("OZNVAL");
    sblOZNVAL.setSqlType(1);
    sblGODINA.setCaption("Godina");
    sblGODINA.setColumnName("GODINA");
    sblGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    sblGODINA.setPrecision(4);
    sblGODINA.setRowId(true);
    sblGODINA.setTableName("STAVBLAG");
    sblGODINA.setServerColumnName("GODINA");
    sblGODINA.setSqlType(5);
    sblGODINA.setWidth(4);
    sblBRIZV.setCaption("Broj blagajni\u010Dkog izvještaja");
    sblBRIZV.setColumnName("BRIZV");
    sblBRIZV.setDataType(com.borland.dx.dataset.Variant.INT);
    sblBRIZV.setPrecision(6);
    sblBRIZV.setRowId(true);
    sblBRIZV.setTableName("STAVBLAG");
    sblBRIZV.setServerColumnName("BRIZV");
    sblBRIZV.setSqlType(4);
    sblBRIZV.setWidth(6);
    sblRBS.setCaption("RBS");
    sblRBS.setColumnName("RBS");
    sblRBS.setDataType(com.borland.dx.dataset.Variant.INT);
    sblRBS.setPrecision(6);
    sblRBS.setRowId(true);
    sblRBS.setTableName("STAVBLAG");
    sblRBS.setServerColumnName("RBS");
    sblRBS.setSqlType(4);
    sblRBS.setWidth(6);
    sblDATUM.setCaption("Datum");
    sblDATUM.setColumnName("DATUM");
    sblDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    sblDATUM.setPrecision(8);
    sblDATUM.setDisplayMask("dd-MM-yyyy");
//    sblDATUM.setEditMask("dd-MM-yyyy");
    sblDATUM.setTableName("STAVBLAG");
    sblDATUM.setWidth(10);
    sblDATUM.setServerColumnName("DATUM");
    sblDATUM.setSqlType(93);
    sblPRIMITAK.setCaption("Primitak");
    sblPRIMITAK.setColumnName("PRIMITAK");
    sblPRIMITAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblPRIMITAK.setPrecision(17);
    sblPRIMITAK.setScale(2);
    sblPRIMITAK.setDisplayMask("###,###,##0.00");
    sblPRIMITAK.setDefault("0");
    sblPRIMITAK.setTableName("STAVBLAG");
    sblPRIMITAK.setServerColumnName("PRIMITAK");
    sblPRIMITAK.setSqlType(2);
    sblPRIMITAK.setDefault("0");
    sblIZDATAK.setCaption("Izdatak");
    sblIZDATAK.setColumnName("IZDATAK");
    sblIZDATAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblIZDATAK.setPrecision(17);
    sblIZDATAK.setScale(2);
    sblIZDATAK.setDisplayMask("###,###,##0.00");
    sblIZDATAK.setDefault("0");
    sblIZDATAK.setTableName("STAVBLAG");
    sblIZDATAK.setServerColumnName("IZDATAK");
    sblIZDATAK.setSqlType(2);
    sblIZDATAK.setDefault("0");
    sblPVPRIMITAK.setCaption("Primitak u dom.valuti");
    sblPVPRIMITAK.setColumnName("PVPRIMITAK");
    sblPVPRIMITAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblPVPRIMITAK.setPrecision(17);
    sblPVPRIMITAK.setScale(2);
    sblPVPRIMITAK.setDisplayMask("###,###,##0.00");
    sblPVPRIMITAK.setDefault("0");
    sblPVPRIMITAK.setTableName("STAVBLAG");
    sblPVPRIMITAK.setServerColumnName("PVPRIMITAK");
    sblPVPRIMITAK.setSqlType(2);
    sblPVPRIMITAK.setDefault("0");
    sblPVIZDATAK.setCaption("Izdatak u dom.valuti");
    sblPVIZDATAK.setColumnName("PVIZDATAK");
    sblPVIZDATAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblPVIZDATAK.setPrecision(17);
    sblPVIZDATAK.setScale(2);
    sblPVIZDATAK.setDisplayMask("###,###,##0.00");
    sblPVIZDATAK.setDefault("0");
    sblPVIZDATAK.setTableName("STAVBLAG");
    sblPVIZDATAK.setServerColumnName("PVIZDATAK");
    sblPVIZDATAK.setSqlType(2);
    sblPVIZDATAK.setDefault("0");
    sblTECAJ.setCaption("Te\u010Daj");
    sblTECAJ.setColumnName("TECAJ");
    sblTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    sblTECAJ.setPrecision(12);
    sblTECAJ.setScale(6);
    sblTECAJ.setDisplayMask("###,###,##0.000000");
    sblTECAJ.setDefault("0");
    sblTECAJ.setTableName("STAVBLAG");
    sblTECAJ.setServerColumnName("TECAJ");
    sblTECAJ.setSqlType(2);
    sblTECAJ.setDefault("1");
    sblCRADNIK.setCaption("Radnik");
    sblCRADNIK.setColumnName("CRADNIK");
    sblCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblCRADNIK.setPrecision(6);
    sblCRADNIK.setTableName("STAVBLAG");
    sblCRADNIK.setServerColumnName("CRADNIK");
    sblCRADNIK.setSqlType(1);
    sblCPN.setCaption("Putni nalog");
    sblCPN.setColumnName("CPN");
    sblCPN.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblCPN.setPrecision(24);
    sblCPN.setTableName("STAVBLAG");
    sblCPN.setServerColumnName("CPN");
    sblCPN.setSqlType(1);
    sblOPIS.setCaption("Opis");
    sblOPIS.setColumnName("OPIS");
    sblOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblOPIS.setPrecision(50);
    sblOPIS.setTableName("STAVBLAG");
    sblOPIS.setServerColumnName("OPIS");
    sblOPIS.setSqlType(1);
    sblTKO.setCaption("Kome / od koga");
    sblTKO.setColumnName("TKO");
    sblTKO.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblTKO.setPrecision(50);
    sblTKO.setTableName("STAVBLAG");
    sblTKO.setServerColumnName("TKO");
    sblTKO.setSqlType(1);
    sblCGRSTAV.setCaption("Grupa");
    sblCGRSTAV.setColumnName("CGRSTAV");
    sblCGRSTAV.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblCGRSTAV.setPrecision(5);
    sblCGRSTAV.setTableName("STAVBLAG");
    sblCGRSTAV.setServerColumnName("CGRSTAV");
    sblCGRSTAV.setSqlType(1);
    sblSTAVKA.setCaption("Veza sa kontom");
    sblSTAVKA.setColumnName("STAVKA");
    sblSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblSTAVKA.setPrecision(8);
    sblSTAVKA.setTableName("STAVBLAG");
    sblSTAVKA.setServerColumnName("STAVKA");
    sblSTAVKA.setSqlType(1);
    sblCSKL.setCaption("Vrsta sheme");
    sblCSKL.setColumnName("CSKL");
    sblCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblCSKL.setPrecision(12);
    sblCSKL.setTableName("STAVBLAG");
    sblCSKL.setServerColumnName("CSKL");
    sblCSKL.setSqlType(1);
    sblVRDOK.setCaption("Vrsta dokumenta");
    sblVRDOK.setColumnName("VRDOK");
    sblVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblVRDOK.setPrecision(3);
    sblVRDOK.setTableName("STAVBLAG");
    sblVRDOK.setServerColumnName("VRDOK");
    sblVRDOK.setSqlType(1);
    sblCORG.setCaption("Org. jedinica");
    sblCORG.setColumnName("CORG");
    sblCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblCORG.setPrecision(12);
    sblCORG.setTableName("STAVBLAG");
    sblCORG.setServerColumnName("CORG");
    sblCORG.setSqlType(1);
    
    sblVRSTA.setCaption("Uplatnica Isplatnica");
    sblVRSTA.setColumnName("VRSTA");
    sblVRSTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblVRSTA.setPrecision(1);
    sblVRSTA.setRowId(true);
    sblVRSTA.setTableName("STAVBLAG");
    sblVRSTA.setServerColumnName("VRSTA");
    sblVRSTA.setSqlType(1);    
    
    
    sblBROJKONTA.setCaption("Konto");
    sblBROJKONTA.setColumnName("BROJKONTA");
    sblBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblBROJKONTA.setPrecision(8);
    sblBROJKONTA.setTableName("STAVBLAG");
    sblBROJKONTA.setServerColumnName("BROJKONTA");
    sblBROJKONTA.setSqlType(1);

    sblDATDOK.setCaption("Datum dokumenta");
    sblDATDOK.setColumnName("DATDOK");
    sblDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    sblDATDOK.setPrecision(8);
    sblDATDOK.setDisplayMask("dd-MM-yyyy");
    sblDATDOK.setTableName("STAVBLAG");
    sblDATDOK.setServerColumnName("DATDOK");
    sblDATDOK.setSqlType(93);
    sblDATDOK.setWidth(10);

    sblDATDOSP.setCaption("Datum dospje\u0107a");
    sblDATDOSP.setColumnName("DATDOSP");
    sblDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    sblDATDOSP.setPrecision(8);
    sblDATDOSP.setDisplayMask("dd-MM-yyyy");
    sblDATDOSP.setTableName("STAVBLAG");
    sblDATDOSP.setServerColumnName("DATDOSP");
    sblDATDOSP.setSqlType(93);
    sblDATDOSP.setWidth(10);

    sblBROJDOK.setCaption("Broj dokumenta");
    sblBROJDOK.setColumnName("BROJDOK");
    sblBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sblBROJDOK.setPrecision(50);
    sblBROJDOK.setTableName("STAVBLAG");
    sblBROJDOK.setServerColumnName("BROJDOK");
    sblBROJDOK.setSqlType(1);
    sblBROJDOK.setWidth(30);

    sblCPAR.setCaption("Partner");
    sblCPAR.setColumnName("CPAR");
    sblCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    sblCPAR.setPrecision(6);
    sblCPAR.setTableName("STAVBLAG");
    sblCPAR.setServerColumnName("CPAR");
    sblCPAR.setSqlType(4);
    sblCPAR.setWidth(6);

    
    sbl.setResolver(dm.getQresolver());
    sbl.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Stavblag", null, true, Load.ALL));
 setColumns(new Column[] {sblLOKK, sblAKTIV, sblKNJIG, sblCBLAG, sblOZNVAL, sblGODINA, sblBRIZV, sblRBS, sblDATUM, sblPRIMITAK, sblIZDATAK,
        sblPVPRIMITAK, sblPVIZDATAK, sblTECAJ, sblCRADNIK, sblCPN, sblOPIS, sblTKO, sblCGRSTAV, sblSTAVKA, sblCSKL, sblVRDOK, sblCORG, sblVRSTA,
        sblBROJKONTA,sblDATDOK,sblDATDOSP,sblBROJDOK,sblCPAR});
  }

  public void setall() {

    ddl.create("Stavblag")
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
       .addChar("vrsta", 1, true) 
       .addChar("brojkonta", 8) //neu
       .addDate("datdok")
       .addDate("datdosp")
       .addChar("brojdok", 50)
       .addInteger("cpar", 6)
       .addPrimaryKey("knjig,cblag,oznval,godina,brizv,rbs,vrsta");


    Naziv = "Stavblag";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brizv", "rbs"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
