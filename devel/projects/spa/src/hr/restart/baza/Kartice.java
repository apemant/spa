/****license*****************************************************************
**   file: Kartice.java
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

public class Kartice extends KreirDrop implements DataModule {

  private static Kartice karticeclass;
  dM dm  = dM.getDataModule();
  QueryDataSet kartice = new raDataSet();
  QueryDataSet karticeaktiv = new raDataSet();

  Column karticeLOKK = new Column();
  Column karticeAKTIV = new Column();
  Column karticeCBANKA = new Column();
  Column karticeNAZIV = new Column();
  Column karticeMAX_RATA = new Column();
  Column karticeMIN_IZNOS = new Column();
  Column karticeMAX_IZNOS = new Column();
  Column karticePROVIZIJA = new Column();
  Column karticeBROJKONTA = new Column();
  Column karticeVRDOK = new Column();

  public static Kartice getDataModule() {
    if (karticeclass == null) {
      karticeclass = new Kartice();
    }
    return karticeclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return kartice;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return karticeaktiv;
  }

  public Kartice() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){

   /*SqlDefTabela = "create table Kartice " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + // Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
        "cbanka char(4) CHARACTER SET WIN1250 not null, "+    // Šifra
        "naziv  char(30) CHARACTER SET WIN1250, " +           // Naziv korisnika
        "max_rata numeric(4,0), "  +
        "min_iznos numeric(17,2)," +
        "max_iznos numeric(17,2)," +
        "provizija numeric(6,2),"  +
        "Primary Key (cbanka))" ;
*/

    ddl.create("kartice")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cbanka", 4, true)
       .addChar("naziv", 30)
       .addShort("max_rata", 4)
       .addFloat("min_iznos", 17, 2)
       .addFloat("max_iznos", 17, 2)
       .addFloat("provizija", 6, 2)
       .addChar("brojkonta", 8)
       .addChar("vrdok", 3)
       .addPrimaryKey("cbanka");

    Naziv="Kartice";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


    /*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"ikarticelokk on Kartice (lokk)",
              CommonTable.SqlDefIndex+"ikarticeaktiv on Kartice (aktiv)",
              CommonTable.SqlDefUniqueIndex+"ikarticeckart on Kartice (cbanka)"};

    NaziviIdx=new String[]{"ikarticelokk","ikarticeaktiv","ikarticecbanka"};
*/
  }
  private void jbInit() throws Exception {
    karticeBROJKONTA.setCaption("Konto");
    karticeBROJKONTA.setColumnName("BROJKONTA");
    karticeBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    karticeBROJKONTA.setDefault("");
    karticeBROJKONTA.setPrecision(8);
    karticeBROJKONTA.setTableName("KARTICE");
    karticeBROJKONTA.setServerColumnName("BROJKONTA");
    karticeBROJKONTA.setSqlType(1);
    karticeBROJKONTA.setWidth(8);
    karticePROVIZIJA.setCaption("Provizija");
    karticePROVIZIJA.setColumnName("PROVIZIJA");
    karticePROVIZIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    karticePROVIZIJA.setDisplayMask("###,###,##0.00");
    karticePROVIZIJA.setDefault("0");
    karticePROVIZIJA.setPrecision(10);
    karticePROVIZIJA.setScale(2);
    karticePROVIZIJA.setTableName("KARTICE");
    karticePROVIZIJA.setServerColumnName("PROVIZIJA");
    karticePROVIZIJA.setSqlType(2);
    karticeMAX_IZNOS.setCaption("Maksimalni iznos rate");
    karticeMAX_IZNOS.setColumnName("MAX_IZNOS");
    karticeMAX_IZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    karticeMAX_IZNOS.setDisplayMask("###,###,##0.00");
    karticeMAX_IZNOS.setDefault("0");
    karticeMAX_IZNOS.setPrecision(15);
    karticeMAX_IZNOS.setScale(2);
    karticeMAX_IZNOS.setTableName("kartice");
    karticeMAX_IZNOS.setServerColumnName("MAX_IZNOS");
    karticeMAX_IZNOS.setSqlType(2);
    karticeMIN_IZNOS.setCaption("Minimalni iznos rate");
    karticeMIN_IZNOS.setColumnName("MIN_IZNOS");
    karticeMIN_IZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    karticeMIN_IZNOS.setDisplayMask("###,###,##0.00");
    karticeMIN_IZNOS.setDefault("0");
    karticeMIN_IZNOS.setPrecision(15);
    karticeMIN_IZNOS.setScale(2);
    karticeMIN_IZNOS.setTableName("kartice");
    karticeMIN_IZNOS.setServerColumnName("MIN_IZNOS");
    karticeMIN_IZNOS.setSqlType(2);
    karticeMAX_RATA.setCaption("Maximalni broj rata");
    karticeMAX_RATA.setColumnName("MAX_RATA");
    karticeMAX_RATA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    karticeMAX_RATA.setDisplayMask("###0");
    karticeMAX_RATA.setDefault("0");
    karticeMAX_RATA.setTableName("kartice");
    karticeMAX_RATA.setServerColumnName("MAX_RATA");
    karticeMAX_RATA.setSqlType(5);
    karticeNAZIV.setCaption("Naziv");
    karticeNAZIV.setColumnName("NAZIV");
    karticeNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    karticeNAZIV.setPrecision(30);
    karticeNAZIV.setTableName("KARTICE");
    karticeNAZIV.setWidth(30);
    karticeNAZIV.setServerColumnName("NAZIV");
    karticeNAZIV.setSqlType(1);
    karticeCBANKA.setCaption("Šifra");
    karticeCBANKA.setColumnName("CBANKA");
    karticeCBANKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    karticeCBANKA.setPrecision(4);
    karticeCBANKA.setRowId(true);
    karticeCBANKA.setTableName("KARTICE");
    karticeCBANKA.setWidth(5);
    karticeCBANKA.setServerColumnName("CBANKA");
    karticeCBANKA.setSqlType(1);
    karticeAKTIV.setColumnName("AKTIV");
    karticeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    karticeAKTIV.setDefault("D");
    karticeAKTIV.setPrecision(1);
    karticeAKTIV.setTableName("KARTICE");
    karticeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    karticeAKTIV.setServerColumnName("AKTIV");
    karticeAKTIV.setSqlType(1);
    karticeLOKK.setColumnName("LOKK");
    karticeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    karticeLOKK.setDefault("N");
    karticeLOKK.setPrecision(1);
    karticeLOKK.setTableName("KARTICE");
    karticeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    karticeLOKK.setServerColumnName("LOKK");
    karticeLOKK.setSqlType(1);
    karticeVRDOK.setCaption("Vrsta dokumenta");
    karticeVRDOK.setColumnName("VRDOK");
    karticeVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    karticeVRDOK.setPrecision(3);
    karticeVRDOK.setTableName("KARTICE");
    karticeVRDOK.setServerColumnName("VRDOK");
    karticeVRDOK.setSqlType(1);
    kartice.setResolver(dm.getQresolver());
    kartice.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from kartice", null, true, Load.ALL));
 setColumns(new Column[] {karticeLOKK, karticeAKTIV, karticeCBANKA, karticeNAZIV, karticeMAX_RATA, karticeMIN_IZNOS, karticeMAX_IZNOS,
        karticePROVIZIJA, karticeBROJKONTA, karticeVRDOK});

    createFilteredDataSet(karticeaktiv, "aktiv = 'D'");
  }
}