/****license*****************************************************************
**   file: Inventura.java
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

public class Inventura extends KreirDrop implements DataModule {
  private static Inventura inventclass;
  dM dm  = dM.getDataModule();
  QueryDataSet invent = new raDataSet();
  /*Column inventCSKL = new Column();
  Column inventCART = new Column();
  Column inventCART1 = new Column();
  Column inventBC = new Column();
  Column inventNAZART = new Column();
  Column inventJM = new Column();
  Column inventKOLKNJ = new Column();
  Column inventKOLINV = new Column();
  Column inventZC = new Column();
  Column inventVRIKNJ = new Column();
  Column inventVRIINV = new Column();
  Column inventKOLMANJ = new Column();
  Column inventKOLVIS = new Column();
  Column inventVRIMANJ = new Column();
  Column inventVRIVIS = new Column();*/

  public static Inventura getDataModule() {
    if (inventclass == null) {
      inventclass = new Inventura();
    }
    return inventclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return invent;
  }

  public Inventura(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    initModule();
/*


    inventCSKL.setCaption("Skladište");
    inventCSKL.setColumnName("CSKL");
    inventCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    inventCSKL.setPrecision(12);
    inventCSKL.setRowId(true);
    inventCSKL.setTableName("INVENTURA");
    inventCSKL.setServerColumnName("CSKL");
    inventCSKL.setSqlType(1);





    inventCART.setCaption("Šifra");
    inventCART.setColumnName("CART");
    inventCART.setDataType(com.borland.dx.dataset.Variant.INT);
    inventCART.setTableName("INVENTURA");
    inventCART.setRowId(true);
    inventCART.setWidth(7);
    inventCART.setServerColumnName("CART");
    inventCART.setSqlType(4);

    inventCART1.setCaption("Oznaka");
    inventCART1.setColumnName("CART1");
    inventCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    inventCART1.setPrecision(20);
    inventCART1.setTableName("INVENTURA");
    inventCART1.setSqlType(1);
    inventCART1.setServerColumnName("CART1");

    inventBC.setCaption("Barcode");
    inventBC.setColumnName("BC");
    inventBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    inventBC.setPrecision(20);
    inventBC.setTableName("INVENTURA");
    inventBC.setSqlType(1);
    inventBC.setServerColumnName("BC");

    inventNAZART.setCaption("Naziv");
    inventNAZART.setColumnName("NAZART");
    inventNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    inventNAZART.setPrecision(50);
    inventNAZART.setTableName("INVENTURA");
    inventNAZART.setWidth(25);
    inventNAZART.setSqlType(1);
    inventNAZART.setServerColumnName("NAZART");

    inventJM.setCaption("Jm");
    inventJM.setColumnName("JM");
    inventJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    inventJM.setPrecision(3);
    inventJM.setTableName("INVENTURA");
    inventJM.setServerColumnName("JM");
    inventJM.setSqlType(1);


    inventKOLKNJ.setCaption("Koli\u010Dina");
    inventKOLKNJ.setColumnName("KOLKNJ");
    inventKOLKNJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventKOLKNJ.setDisplayMask("###,###,##0.000");
    inventKOLKNJ.setDefault("0");
    inventKOLKNJ.setPrecision(15);
    inventKOLKNJ.setScale(3);
    inventKOLKNJ.setTableName("INVENTURA");
    inventKOLKNJ.setWidth(10);
    inventKOLKNJ.setServerColumnName("KOLKNJ");
    inventKOLKNJ.setSqlType(2);

    inventKOLINV.setCaption("Inventurna koli\u010Dina");
    inventKOLINV.setColumnName("KOLINV");
    inventKOLINV.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventKOLINV.setDisplayMask("###,###,##0.000");
    inventKOLINV.setDefault("0");
    inventKOLINV.setPrecision(15);
    inventKOLINV.setScale(3);
    inventKOLINV.setTableName("INVENTURA");
    inventKOLINV.setWidth(10);
    inventKOLINV.setServerColumnName("KOLINV");
    inventKOLINV.setSqlType(2);






    inventZC.setCaption("Cijena");
    inventZC.setColumnName("ZC");
    inventZC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventZC.setDisplayMask("###,###,##0.00");
    inventZC.setDefault("0");
    inventZC.setPrecision(12);
    inventZC.setScale(2);
    inventZC.setTableName("INVENTURA");
    inventZC.setWidth(10);
    inventZC.setServerColumnName("ZC");
    inventZC.setSqlType(2);








    inventVRIKNJ.setCaption("Vrijednost");
    inventVRIKNJ.setColumnName("VRIKNJ");
    inventVRIKNJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventVRIKNJ.setDisplayMask("###,###,##0.00");
    inventVRIKNJ.setDefault("0");
    inventVRIKNJ.setPrecision(15);
    inventVRIKNJ.setScale(2);
    inventVRIKNJ.setTableName("INVENTURA");
    inventVRIKNJ.setWidth(10);
    inventVRIKNJ.setServerColumnName("VRIKNJ");
    inventVRIKNJ.setSqlType(2);






    inventVRIINV.setCaption("Inventurna vrijednost");
    inventVRIINV.setColumnName("VRIINV");
    inventVRIINV.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventVRIINV.setDisplayMask("###,###,##0.00");
    inventVRIINV.setDefault("0");
    inventVRIINV.setPrecision(15);
    inventVRIINV.setScale(2);
    inventVRIINV.setTableName("INVENTURA");
    inventVRIINV.setWidth(10);
    inventVRIINV.setServerColumnName("VRIINV");
    inventVRIINV.setSqlType(2);

    inventKOLMANJ.setCaption("Manjak koli\u010Dine");
    inventKOLMANJ.setColumnName("KOLMANJ");
    inventKOLMANJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventKOLMANJ.setDisplayMask("###,###,##0.000");
    inventKOLMANJ.setDefault("0");
    inventKOLMANJ.setPrecision(15);
    inventKOLMANJ.setScale(3);
    inventKOLMANJ.setTableName("INVENTURA");
    inventKOLMANJ.setWidth(10);
    inventKOLMANJ.setServerColumnName("KOLMANJ");
    inventKOLMANJ.setSqlType(2);

    inventKOLVIS.setCaption("Višak koli\u010Dine");
    inventKOLVIS.setColumnName("KOLVIS");
    inventKOLVIS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventKOLVIS.setDisplayMask("###,###,##0.000");
    inventKOLVIS.setDefault("0");
    inventKOLVIS.setPrecision(15);
    inventKOLVIS.setScale(3);
    inventKOLVIS.setTableName("INVENTURA");
    inventKOLVIS.setWidth(10);
    inventKOLVIS.setSqlType(2);
    inventKOLVIS.setServerColumnName("KOLVIS");

    inventVRIMANJ.setCaption("Manjak vrijednosti");
    inventVRIMANJ.setColumnName("VRIMANJ");
    inventVRIMANJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventVRIMANJ.setDisplayMask("###,###,##0.00");
    inventVRIMANJ.setDefault("0");
    inventVRIMANJ.setPrecision(15);
    inventVRIMANJ.setScale(2);
    inventVRIMANJ.setTableName("INVENTURA");
    inventVRIMANJ.setWidth(10);
    inventVRIMANJ.setSqlType(2);
    inventVRIMANJ.setServerColumnName("VRIMANJ");

    inventVRIVIS.setCaption("Višak vrijednosti");
    inventVRIVIS.setColumnName("VRIVIS");
    inventVRIVIS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    inventVRIVIS.setDisplayMask("###,###,##0.00");
    inventVRIVIS.setDefault("0");
    inventVRIVIS.setPrecision(15);
    inventVRIVIS.setScale(2);
    inventVRIVIS.setTableName("INVENTURA");
    inventVRIVIS.setWidth(10);
    inventVRIVIS.setSqlType(2);
    inventVRIVIS.setServerColumnName("VRIVIS");

    invent.setResolver(dm.getQresolver());
    invent.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
        "SELECT * from inventura", null, true, Load.ALL));
 setColumns(new Column[] {inventCSKL, inventCART, inventCART1, inventBC, inventNAZART, inventJM, inventKOLKNJ, inventKOLINV, inventZC,
        inventVRIKNJ, inventVRIINV, inventKOLMANJ, inventKOLVIS, inventVRIMANJ, inventVRIVIS});
*/  }

  //public void setall(){

/*      SqlDefTabela = "create table Stdoku " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
      "cskl char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta
      "vrdok char(3) CHARACTER SET WIN1250 not null," +   //Vrsta dokumenta (OTP,PRI,..)
      "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
      "brdok numeric(6,0) not null , " + // Broj dokumenta
      "rbr numeric(4,0) not null, " + // Redni broj stavke
      "cart numeric(6,0) , "+   // Šifra artikl broj\u010Danik
      "cart1 char(20) character set win1250 ," +  // Šifra artikl - alpha dodatna
      "bc char(20) character set win1250 ,"+ // Barcode
      "nazart char(50) CHARACTER SET WIN1250 , " + // Naziv artikla
      "jm char(3) CHARACTER SET WIN1250 , " + // Jedinica mjere
      "kol  numeric(17,3) ," + // Koli\u010Dina
      "dc   numeric(12,2) ," + // Dobavlja\u010Deva cijena
      "dc_val numeric(12,2) ," + // Dobavlja\u010Deva cijena u valuti
      "idob numeric(17,2),"  + // Iznos dobavlja\u010Da
      "idob_val numeric(17,2),"  + // Iznos dobavlja\u010Da u valuti
      "prab numeric(6,2),"   + // Posto rabata
      "irab numeric(17,2)," +  // Iznos rabata
      "pzt numeric(6,2)," + // Posto zavisni troškovi (kumulativ)
      "izt numeric(17,2) ," + // Iznos zavisni troškovi (kumulativ)
      "nc   numeric(12,4) ," + // Nabavna cijena
      "pmar numeric(6,2) , " + // Posto marže
      "mar  numeric(17,2), " + // Marža po jedinici VC-NC
      "vc   numeric(12,2) ," + // Prodajna
      "por1  numeric(17,2) ," + // Porez 1 (npr. PDV)
      "por2  numeric(17,2) ," + // Porez 2 (npr. na potrošnju 3%
      "por3  numeric(17,2) ," + // Porez 3
      "mc   numeric(12,2) , " + // Prodajna cijena s porezom
      "inab numeric(17,2), " +  // Iznos nabavni
      "imar numeric(17,2), " + // Iznos marže
      "ibp  numeric(17,2), " + // Iznos bez poreza
      "ipor numeric(17,2),"  + // Iznos poreza
      "isp  numeric(17,2),"  + // Iznos s porezom
      "zc   numeric(12,4),"  + // Cijena zalihe
      "izad numeric(17,2),"  + // Iznos zalihe
      "kolflh numeric(17,3)," + //Koli\u010Dina za FIFO LIFO HIFO
      "skol numeric(17,3) ," + // Koli\u010Dina stara prije nivelacije
      "svc numeric (12,2) ," + // Stara prod. cijena bez nivelacije
      "smc numeric (12,2) , " + // Stara prodajna cijena s porezom
      "diopormar numeric(17,2)," + // Dio poravnanja marže
      "dioporpor numeric(17,2)," + // Dio poravnanja poreza
      "porav numeric(17,2),"+ // Poravnanje
      "Primary Key (cskl,vrdok,god,brdok,rbr))" ; */

    /*ddl.create("inventura")
       .addChar("cskl", 12, true)
       .addInteger("cart", 6, true)
       .addChar("cart1", 20)
       .addChar("bc", 20)
       .addChar("nazart", 50)
       .addChar("jm", 3)
       .addFloat("kolknj", 17, 3)
       .addFloat("kolinv", 17, 3)
       .addFloat("zc", 12, 2)
       .addFloat("vriknj", 17, 2)
       .addFloat("vriinv", 17, 2)
       .addFloat("kolmanj", 17, 3)
       .addFloat("kolvis", 17, 3)
       .addFloat("vrimanj", 17, 2)
       .addFloat("vrivis", 17, 2)
       .addPrimaryKey("cskl,cart");

    Naziv="Inventura";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);*/

    /*
    NaziviIdx=new String[]{"ilokkstdoku","iaktivstdoku","icsklstdoku","ivrdokstdoku","ibrdokstdoku",
                            "irbrstdoku","ipkstdoku ","icartstdoku"} ;


    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Stdoku (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] + " on Stdoku (aktiv)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[2] + " on Stdoku (cskl)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] + " on Stdoku (vrdok)",
                            CommonTable.SqlDefIndex+NaziviIdx[4] + " on Stdoku (brdok)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[5] + " on Stdoku (rbr)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[6] +" on Stdoku (cskl,vrdok,god,brdok,rbr)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[7] + " on Stdoku (cart)" };
  */
  //}
}