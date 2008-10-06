/****license*****************************************************************
**   file: Skupart.java
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
import java.util.ResourceBundle;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Skupart extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Skupart Skupartclass;
  dM dm  = dM.getDataModule();
  QueryDataSet skupart = new raDataSet();
  Column skupartCSKUPART = new Column();
  Column skupartNAZSKUPART = new Column();
  Column skupartCART = new Column();
  Column skupartCART1 = new Column();
  Column skupartBC = new Column();
  Column skupartNAZART = new Column();
  Column skupartJM = new Column();
  Column skupartKOL = new Column();
  public static Skupart getDataModule() {
    if (Skupartclass == null) {
      Skupartclass = new Skupart();
    }
    return Skupartclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return skupart;
  }
  public Skupart() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    skupartKOL.setCaption("Koli\u010Dina");
    skupartKOL.setColumnName("KOL");
    skupartKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skupartKOL.setDisplayMask("###,###,##0.000");
    skupartKOL.setDefault("0");
    skupartKOL.setPrecision(15);
    skupartKOL.setScale(3);
    skupartKOL.setTableName("SKUPART");
    skupartKOL.setServerColumnName("KOL");
    skupartKOL.setSqlType(2);
    skupartJM.setCaption("Jedinica mjere");
    skupartJM.setColumnName("JM");
    skupartJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    skupartJM.setPrecision(3);
    skupartJM.setTableName("SKUPART");
    skupartJM.setSqlType(1);
    skupartJM.setServerColumnName("JM");
    skupartNAZART.setCaption("Naziv artikla");
    skupartNAZART.setColumnName("NAZART");
    skupartNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    skupartNAZART.setPrecision(50);
    skupartNAZART.setTableName("SKUPART");
    skupartNAZART.setWidth(20);
    skupartNAZART.setServerColumnName("NAZART");
    skupartNAZART.setSqlType(1);
    skupartBC.setCaption("Barcode");
    skupartBC.setColumnName("BC");
    skupartBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    skupartBC.setPrecision(20);
    skupartBC.setTableName("SKUPART");
    skupartBC.setWidth(10);
    skupartBC.setServerColumnName("BC");
    skupartBC.setSqlType(1);
    skupartCART1.setCaption("Oznaka");
    skupartCART1.setColumnName("CART1");
    skupartCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    skupartCART1.setPrecision(20);
    skupartCART1.setTableName("SKUPART");
    skupartCART1.setWidth(11);
    skupartCART1.setServerColumnName("CART1");
    skupartCART1.setSqlType(1);
    skupartCART.setCaption("Artikl");
    skupartCART.setColumnName("CART");
    skupartCART.setDataType(com.borland.dx.dataset.Variant.INT);
    skupartCART.setPrecision(6);
    skupartCART.setRowId(true);
    skupartCART.setTableName("SKUPART");
    skupartCART.setServerColumnName("CART");
    skupartCART.setSqlType(4);
    skupartNAZSKUPART.setCaption("Naziv");
    skupartNAZSKUPART.setColumnName("NAZSKUPART");
    skupartNAZSKUPART.setDataType(com.borland.dx.dataset.Variant.STRING);
    skupartNAZSKUPART.setPrecision(50);
    skupartNAZSKUPART.setTableName("SKUPART");
    skupartNAZSKUPART.setWidth(30);
    skupartNAZSKUPART.setServerColumnName("NAZSKUPART");
    skupartNAZSKUPART.setSqlType(1);
    skupartCSKUPART.setCaption("\u0160ifra");
    skupartCSKUPART.setColumnName("CSKUPART");
    skupartCSKUPART.setDataType(com.borland.dx.dataset.Variant.STRING);
    skupartCSKUPART.setPrecision(6);
    skupartCSKUPART.setRowId(true);
    skupartCSKUPART.setTableName("SKUPART");
    skupartCSKUPART.setWidth(5);
    skupartCSKUPART.setServerColumnName("CSKUPART");
    skupartCSKUPART.setSqlType(1);
    skupart.setResolver(dm.getQresolver());
    skupart.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * FROM SKUPART", null, true, Load.ALL));
 setColumns(new Column[] {skupartCSKUPART, skupartNAZSKUPART, skupartCART, skupartCART1, skupartBC, skupartNAZART, skupartJM, skupartKOL});
  }

 public void setall(){

   /* SqlDefTabela =  "create table Grupart " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
      "cgrart char(10) CHARACTER SET WIN1250 not null,"+ //Grupa artikala podgrupa
      "cgrartprip char(10) CHARACTER SET WIN1250 , " + //Sifra grupe - pripadnosti
      "nazgrart char(50) CHARACTER SET WIN1250 , " + // Naziv grupe artikala
      "ppop numeric(5,2)," +         // Posto popusta
      "Primary Key (cgrart))" ;
*/
    ddl.create("skupart")
       .addChar("cskupart", 6, true)
       .addChar("nazskupart", 50 )
       .addInteger("cart", 6, true)
       .addChar("cart1", 20)
       .addChar("bc", 20)
       .addChar("nazart", 50)
       .addChar("jm", 3)
       .addFloat("kol", 17, 3)
       .addPrimaryKey("cskupart,cart");

    Naziv="Skupart";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cskupart"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

  /*
    NaziviIdx=new String[]{"ilokkgrupart","iaktivgrupart","icgrartgrupart","icgrartprip"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Grupart (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Grupart (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Grupart (cgrart)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on Grupart (cgrartprip)"};
  */
  }
}
