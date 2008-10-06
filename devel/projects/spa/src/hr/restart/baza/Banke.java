/****license*****************************************************************
**   file: Banke.java
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

public class Banke extends KreirDrop implements DataModule {

  private static Banke bankeclass;
  dM dm  = dM.getDataModule();
  QueryDataSet banke = new raDataSet();
  QueryDataSet bankeaktiv = new raDataSet();
  Column bankeLOKK = new Column();
  Column bankeAKTIV = new Column();
  Column bankeCBANKA = new Column();
  Column bankeNAZIV = new Column();
  Column bankeZIRO = new Column();
  Column bankeBANKA = new Column();
  Column bankeMAX_RATA = new Column();
  Column bankeMIN_IZNOS = new Column();
  Column bankeMAX_IZNOS = new Column();
  Column bankeBROJKONTA = new Column();
  Column bankeVRDOK = new Column();

  public static Banke getDataModule() {
    if (bankeclass == null) {
      bankeclass = new Banke();
    }
    return bankeclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return banke;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return bankeaktiv;
  }

  public Banke() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){

    /*SqlDefTabela = "create table Banke " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + // Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
        "cbanka char(4) CHARACTER SET WIN1250 not null, "+    // Šifra
        "naziv  char(30) CHARACTER SET WIN1250, " +           // Naziv korisnika
        "ziro char(40) CHARACTER SET WIN1250, " +             // Žiro ra\u010Dun banke
        "banka char(1) CHARACTER SET WIN1250, "+              // Flag banka gotovina www.ask
        "max_rata numeric(4,0), " +
        "min_iznos numeric(17,2)," +
        "max_iznos numeric(17,2)," +
        "Primary Key (cbanka))" ;

    Naziv="Banke";

    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"ibankelokk on banke (lokk)",
              CommonTable.SqlDefIndex+"ibankeaktiv on banke (aktiv)",
              CommonTable.SqlDefUniqueIndex+"ibankecbanka on banke (cbanka)"};

    NaziviIdx=new String[]{"ibankelokk","ibankeaktiv","ibankecbanka"};*/

    ddl.create("banke")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cbanka", 4, true)
       .addChar("naziv", 200)
       .addChar("ziro", 40)
       .addChar("banka", 1)
       .addShort("max_rata", 4)
       .addFloat("min_iznos", 17, 2)
       .addFloat("max_iznos", 17, 2)
       .addChar("brojkonta", 8)
       .addChar("vrdok", 3)
       .addPrimaryKey("cbanka");

    Naziv = "Banke";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

  }
  private void jbInit() throws Exception {
    bankeBROJKONTA.setCaption("Konto");
    bankeBROJKONTA.setColumnName("BROJKONTA");
    bankeBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankeBROJKONTA.setDefault("");
    bankeBROJKONTA.setPrecision(8);
    bankeBROJKONTA.setTableName("BANKE");
    bankeBROJKONTA.setServerColumnName("BROJKONTA");
    bankeBROJKONTA.setSqlType(1);
    bankeBROJKONTA.setWidth(8);

    bankeMAX_IZNOS.setCaption("Maximalni iznos rate");
    bankeMAX_IZNOS.setColumnName("MAX_IZNOS");
    bankeMAX_IZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    bankeMAX_IZNOS.setDisplayMask("###,###,##0.00");
    bankeMAX_IZNOS.setDefault("0");
    bankeMAX_IZNOS.setPrecision(15);
    bankeMAX_IZNOS.setScale(2);
    bankeMAX_IZNOS.setTableName("BANKE");
    bankeMAX_IZNOS.setServerColumnName("MAX_IZNOS");
    bankeMAX_IZNOS.setSqlType(2);
    bankeMIN_IZNOS.setCaption("Minimalni iznos rate");
    bankeMIN_IZNOS.setColumnName("MIN_IZNOS");
    bankeMIN_IZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    bankeMIN_IZNOS.setDisplayMask("###,###,##0.00");
    bankeMIN_IZNOS.setDefault("0");
    bankeMIN_IZNOS.setPrecision(15);
    bankeMIN_IZNOS.setScale(2);
    bankeMIN_IZNOS.setTableName("BANKE");
    bankeMIN_IZNOS.setServerColumnName("MIN_IZNOS");
    bankeMIN_IZNOS.setSqlType(2);
    bankeMAX_RATA.setCaption("Maximalni broj rata");
    bankeMAX_RATA.setColumnName("MAX_RATA");
    bankeMAX_RATA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    bankeMAX_RATA.setDisplayMask("####");
    bankeMAX_RATA.setDefault("0");
    bankeMAX_RATA.setTableName("BANKE");
    bankeMAX_RATA.setServerColumnName("MAX_RATA");
    bankeMAX_RATA.setSqlType(5);
    bankeBANKA.setCaption("Banka fl.");
    bankeBANKA.setColumnName("BANKA");
    bankeBANKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankeBANKA.setPrecision(1);
    bankeBANKA.setTableName("BANKE");
    bankeBANKA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    bankeBANKA.setServerColumnName("BANKA");
    bankeBANKA.setSqlType(1);
    bankeZIRO.setCaption("Žiro ra\u010Dun");
    bankeZIRO.setColumnName("ZIRO");
    bankeZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankeZIRO.setPrecision(40);
    bankeZIRO.setTableName("BANKE");
    bankeZIRO.setServerColumnName("ZIRO");
    bankeZIRO.setSqlType(1);
    bankeNAZIV.setCaption("Naziv");
    bankeNAZIV.setColumnName("NAZIV");
    bankeNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankeNAZIV.setPrecision(200);
    bankeNAZIV.setTableName("BANKE");
    bankeNAZIV.setServerColumnName("NAZIV");
    bankeNAZIV.setSqlType(1);
    bankeCBANKA.setCaption("Šifra");
    bankeCBANKA.setColumnName("CBANKA");
    bankeCBANKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankeCBANKA.setPrecision(4);
    bankeCBANKA.setRowId(true);
    bankeCBANKA.setTableName("BANKE");
    bankeCBANKA.setWidth(5);
    bankeCBANKA.setServerColumnName("CBANKA");
    bankeCBANKA.setSqlType(1);
    bankeAKTIV.setColumnName("AKTIV");
    bankeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankeAKTIV.setDefault("D");
    bankeAKTIV.setPrecision(1);
    bankeAKTIV.setTableName("BANKE");
    bankeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    bankeAKTIV.setServerColumnName("AKTIV");
    bankeAKTIV.setSqlType(1);
    bankeLOKK.setColumnName("LOKK");
    bankeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankeLOKK.setDefault("N");
    bankeLOKK.setPrecision(1);
    bankeLOKK.setTableName("BANKE");
    bankeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    bankeLOKK.setServerColumnName("LOKK");
    bankeLOKK.setSqlType(1);
    bankeVRDOK.setCaption("Vrsta dokumenta");
    bankeVRDOK.setColumnName("VRDOK");
    bankeVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankeVRDOK.setPrecision(3);
    bankeVRDOK.setTableName("BANKE");
    bankeVRDOK.setServerColumnName("VRDOK");
    bankeVRDOK.setSqlType(1);
    banke.setResolver(dm.getQresolver());
    banke.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from banke", null, true, Load.ALL));
 setColumns(new Column[] {bankeLOKK, bankeAKTIV, bankeCBANKA, bankeNAZIV, bankeZIRO, bankeBANKA, bankeMAX_RATA, bankeMIN_IZNOS, bankeMAX_IZNOS, bankeBROJKONTA, bankeVRDOK});

    createFilteredDataSet(bankeaktiv, "aktiv = 'D'");
  }

}
