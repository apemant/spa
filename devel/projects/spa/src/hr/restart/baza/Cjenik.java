/****license*****************************************************************
**   file: Cjenik.java
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



public class Cjenik extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Cjenik Cjenikclass;

  QueryDataSet cjenik = new raDataSet();

  /*Column cjenikLOKK = new Column();
  Column cjenikAKTIV = new Column();
  Column cjenikCORG = new Column();
  Column cjenikCSKL = new Column();
  Column cjenikCPAR = new Column();
  Column cjenikCART = new Column();
  Column cjenikCART1 = new Column();
  Column cjenikBC = new Column();
  Column cjenikNAZART = new Column();
  Column cjenikJM = new Column();
  Column cjenikPOSTO = new Column();
  Column cjenikVCKALDOM = new Column();
  Column cjenikVCKALVAL = new Column();
  Column cjenikVC = new Column();
  Column cjenikMC = new Column();
  Column cjenikCVAL = new Column();
  Column cjenikTECAJ = new Column();
*/
  public static Cjenik getDataModule() {
    if (Cjenikclass == null) {
      Cjenikclass = new Cjenik();
    }
    return Cjenikclass;
  }

  public QueryDataSet getQueryDataSet() {
    return cjenik;
  }

  public Cjenik() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    /*cjenikLOKK.setCaption("Status zauzetosti");
    cjenikLOKK.setColumnName("LOKK");
    cjenikLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikLOKK.setPrecision(1);
    cjenikLOKK.setTableName("CJENIK");
    cjenikLOKK.setServerColumnName("LOKK");
    cjenikLOKK.setSqlType(1);
    cjenikLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cjenikLOKK.setDefault("N");
    cjenikAKTIV.setCaption("Aktivan - neaktivan");
    cjenikAKTIV.setColumnName("AKTIV");
    cjenikAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikAKTIV.setPrecision(1);
    cjenikAKTIV.setTableName("CJENIK");
    cjenikAKTIV.setServerColumnName("AKTIV");
    cjenikAKTIV.setSqlType(1);
    cjenikAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cjenikAKTIV.setDefault("D");
    cjenikCORG.setCaption("OJ");
    cjenikCORG.setColumnName("CORG");
    cjenikCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikCORG.setPrecision(12);
    cjenikCORG.setTableName("CJENIK");
    cjenikCORG.setServerColumnName("CORG");
    cjenikCORG.setRowId(true);
    cjenikCORG.setSqlType(1);
    cjenikCSKL.setCaption("Skladište");
    cjenikCSKL.setColumnName("CSKL");
    cjenikCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikCSKL.setPrecision(12);
    cjenikCSKL.setRowId(true);
    cjenikCSKL.setTableName("CJENIK");
    cjenikCSKL.setServerColumnName("CSKL");
    cjenikCSKL.setSqlType(1);
    cjenikCPAR.setCaption("Partner");
    cjenikCPAR.setColumnName("CPAR");
    cjenikCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    cjenikCPAR.setPrecision(6);
    cjenikCPAR.setRowId(true);
    cjenikCPAR.setTableName("CJENIK");
    cjenikCPAR.setServerColumnName("CPAR");
    cjenikCPAR.setSqlType(4);
    cjenikCPAR.setWidth(6);
    cjenikCART.setCaption("Šifra");
    cjenikCART.setColumnName("CART");
    cjenikCART.setDataType(com.borland.dx.dataset.Variant.INT);
    cjenikCART.setPrecision(6);
    cjenikCART.setRowId(true);
    cjenikCART.setTableName("CJENIK");
    cjenikCART.setServerColumnName("CART");
    cjenikCART.setSqlType(4);
    cjenikCART.setWidth(6);
    cjenikCART1.setCaption("Oznaka");
    cjenikCART1.setColumnName("CART1");
    cjenikCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikCART1.setPrecision(20);
    cjenikCART1.setTableName("CJENIK");
    cjenikCART1.setServerColumnName("CART1");
    cjenikCART1.setSqlType(1);
    cjenikBC.setCaption("Barcode");
    cjenikBC.setColumnName("BC");
    cjenikBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikBC.setPrecision(20);
    cjenikBC.setTableName("CJENIK");
    cjenikBC.setServerColumnName("BC");
    cjenikBC.setSqlType(1);
    cjenikNAZART.setCaption("Naziv");
    cjenikNAZART.setColumnName("NAZART");
    cjenikNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikNAZART.setPrecision(50);
    cjenikNAZART.setTableName("CJENIK");
    cjenikNAZART.setServerColumnName("NAZART");
    cjenikNAZART.setSqlType(1);
    cjenikJM.setCaption("Jm");
    cjenikJM.setColumnName("JM");
    cjenikJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikJM.setPrecision(3);
    cjenikJM.setTableName("CJENIK");
    cjenikJM.setServerColumnName("JM");
    cjenikJM.setSqlType(1);
    cjenikPOSTO.setCaption("Posto popusta");
    cjenikPOSTO.setColumnName("POSTO");
    cjenikPOSTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cjenikPOSTO.setPrecision(6);
    cjenikPOSTO.setScale(2);
    cjenikPOSTO.setDisplayMask("###,###,##0.00");
    cjenikPOSTO.setDefault("0");
    cjenikPOSTO.setTableName("CJENIK");
    cjenikPOSTO.setServerColumnName("POSTO");
    cjenikPOSTO.setSqlType(2);
    cjenikPOSTO.setDefault("0");

    cjenikVCKALDOM.setCaption("Cijena domicijalna");
    cjenikVCKALDOM.setColumnName("VCKALDOM");
    cjenikVCKALDOM.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cjenikVCKALDOM.setPrecision(17);
    cjenikVCKALDOM.setScale(2);
    cjenikVCKALDOM.setDisplayMask("###,###,##0.00");
    cjenikVCKALDOM.setDefault("0");
    cjenikVCKALDOM.setTableName("CJENIK");
    cjenikVCKALDOM.setServerColumnName("VCKALDOM");
    cjenikVCKALDOM.setSqlType(2);
    cjenikVCKALDOM.setDefault("0");

    cjenikVCKALVAL.setCaption("Cijena valutna");
    cjenikVCKALVAL.setColumnName("VCKALVAL");
    cjenikVCKALVAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cjenikVCKALVAL.setPrecision(17);
    cjenikVCKALVAL.setScale(2);
    cjenikVCKALVAL.setDisplayMask("###,###,##0.00");
    cjenikVCKALVAL.setDefault("0");
    cjenikVCKALVAL.setTableName("CJENIK");
    cjenikVCKALVAL.setServerColumnName("VCKALVAL");
    cjenikVCKALVAL.setSqlType(2);
    cjenikVCKALVAL.setDefault("0");
    
    cjenikVC.setCaption("Cijena bez poreza");
    cjenikVC.setColumnName("VC");
    cjenikVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cjenikVC.setPrecision(17);
    cjenikVC.setScale(2);
    cjenikVC.setDisplayMask("###,###,##0.00");
    cjenikVC.setDefault("0");
    cjenikVC.setTableName("CJENIK");
    cjenikVC.setServerColumnName("VC");
    cjenikVC.setSqlType(2);
    cjenikVC.setDefault("0");
    cjenikMC.setCaption("Cijena s porezom");
    cjenikMC.setColumnName("MC");
    cjenikMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cjenikMC.setPrecision(17);
    cjenikMC.setScale(2);
    cjenikMC.setDisplayMask("###,###,##0.00");
    cjenikMC.setDefault("0");
    cjenikMC.setTableName("CJENIK");
    cjenikMC.setServerColumnName("MC");
    cjenikMC.setSqlType(2);
    cjenikMC.setDefault("0");
    
    cjenikCVAL.setCaption("Valuta");
    cjenikCVAL.setColumnName("OZNVAL");
    cjenikCVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    cjenikCVAL.setPrecision(3);
    cjenikCVAL.setTableName("CJENIK");
    cjenikCVAL.setSqlType(1);
    cjenikCVAL.setServerColumnName("OZNVAL");
    
    cjenikTECAJ.setCaption("Teèaj");
    cjenikTECAJ.setColumnName("TECAJ");
    cjenikTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cjenikTECAJ.setDisplayMask("###,###,##0.000000");
    cjenikTECAJ.setDefault("0");
    cjenikTECAJ.setPrecision(15);
    cjenikTECAJ.setScale(6);
    cjenikTECAJ.setTableName("CJENIK");
    cjenikTECAJ.setSqlType(2);
    cjenikTECAJ.setServerColumnName("TECAJ");

    
    cjenik.setResolver(dm.getQresolver());
    cjenik.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Cjenik", null, true, Load.ALL));
    setColumns(new Column[] {cjenikLOKK, cjenikAKTIV, cjenikCORG, cjenikCSKL, cjenikCPAR, 
    		cjenikCART, cjenikCART1, cjenikBC, cjenikNAZART, cjenikJM, cjenikPOSTO, 
			cjenikVCKALDOM, cjenikVCKALVAL, cjenikVC, cjenikMC, cjenikCVAL, cjenikTECAJ});
*/  
    initModule();
  }

  /*public void setall() {

    ddl.create("Cjenik")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("cskl", 12, true)
       .addInteger("cpar", 6, true)
       .addInteger("cart", 6, true)
       .addChar("cart1", 20)
       .addChar("bc", 20)
       .addChar("nazart", 50)
       .addChar("jm", 3)
       .addFloat("posto", 6, 2)
       .addFloat("vckaldom", 17, 2)
       .addFloat("vckalval", 17, 2)
       .addFloat("vc", 17, 2)
       .addFloat("mc", 17, 2)
       .addChar("oznval", 3)
       .addFloat("tecaj", 17, 6)
       .addPrimaryKey("corg,cskl,cpar,cart");


    Naziv = "Cjenik";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
