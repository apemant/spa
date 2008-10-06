/****license*****************************************************************
**   file: Gruppart.java
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

public class Gruppart extends KreirDrop implements DataModule {

  private static Gruppart gruppartclass;
  dM dm  = dM.getDataModule();
  QueryDataSet gruppart = new raDataSet();
  QueryDataSet gruppartaktiv = new raDataSet();
  Column gpartLOKK = new Column();
  Column gpartAKTIV = new Column();
  Column gpartCGPART = new Column();
  Column gpartNAZIV = new Column();
//  Column gpartNAZGPART = new Column();
  Column gpartCAGENT = new Column();

  public static Gruppart getDataModule() {
    if (gruppartclass == null) {
      gruppartclass = new Gruppart();
//      modules.put(gruppartclass.getClass().getName(), gruppartclass);
    }
    return gruppartclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return gruppart;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return gruppartaktiv;
  }

  public Gruppart() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setall(){

   /*SqlDefTabela = "create table Grupeusera " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', "   + // Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', "   + // Aktivan-neaktivan
        "cgrupeusera char(5) CHARACTER SET WIN1250 not null, "+ // Šifra
        "naziv  char(30) CHARACTER SET WIN1250, " +             // Naziv korisnika
        "Primary Key (cgrupeusera))" ;
*/
    ddl.create("gruppart")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cgpart", 3, true)
       .addChar("naziv", 50)
       .addInteger("cagent", 6)
       .addPrimaryKey("cgpart");

    Naziv="Gruppart";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cagent"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"igrupeuseralokk on grupeusera (lokk)",
              CommonTable.SqlDefIndex+"igrupeuseraaktiv on grupeusera (aktiv)",
              CommonTable.SqlDefUniqueIndex+"igrupeuseracusera on grupeusera (cgrupeusera)"};

    NaziviIdx=new String[]{"iuserilokk","iuseriaktiv","iusericusera","iusericgrpu"};
*/
  }
  private void jbInit() throws Exception {
    gpartNAZIV.setCaption("Naziv");
    gpartNAZIV.setColumnName("NAZIV");
    gpartNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    gpartNAZIV.setPrecision(50);
    gpartNAZIV.setTableName("GRUPPART");
    gpartNAZIV.setSqlType(1);
    gpartNAZIV.setServerColumnName("NAZIV");
    gpartCAGENT.setCaption("Šifra agenta");
    gpartCAGENT.setColumnName("CAGENT");
    gpartCAGENT.setDataType(com.borland.dx.dataset.Variant.INT);
    gpartCAGENT.setPrecision(5);
    gpartCAGENT.setTableName("GRUPPART");
    gpartCAGENT.setWidth(5);
    gpartCAGENT.setSqlType(4);
/*    gpartNAZGPART.setCaption("Naziv");
    gpartNAZGPART.setColumnName("NAZGPART");
    gpartNAZGPART.setDataType(com.borland.dx.dataset.Variant.STRING);
    gpartNAZGPART.setPrecision(50);
    gpartNAZGPART.setTableName("GRUPPART");
    gpartNAZGPART.setWidth(20);
    gpartNAZGPART.setServerColumnName("NAZGPART");
    gpartNAZGPART.setSqlType(1);*/
    gpartCGPART.setCaption("Šifra");
    gpartCGPART.setColumnName("CGPART");
    gpartCGPART.setDataType(com.borland.dx.dataset.Variant.STRING);
    gpartCGPART.setPrecision(3);
    gpartCGPART.setRowId(true);
    gpartCGPART.setTableName("GRUPPART");
    gpartCGPART.setServerColumnName("CGPART");
    gpartCGPART.setSqlType(1);
    gpartAKTIV.setColumnName("AKTIV");
    gpartAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    gpartAKTIV.setDefault("D");
    gpartAKTIV.setPrecision(1);
    gpartAKTIV.setTableName("GRUPPART");
    gpartAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    gpartAKTIV.setServerColumnName("AKTIV");
    gpartAKTIV.setSqlType(1);
    gpartLOKK.setColumnName("LOKK");
    gpartLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    gpartLOKK.setDefault("N");
    gpartLOKK.setPrecision(1);
    gpartLOKK.setTableName("GRUPPART");
    gpartLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    gpartLOKK.setServerColumnName("LOKK");
    gpartLOKK.setSqlType(1);
      gruppart.setResolver(dm.getQresolver());
    gruppart.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from gruppart", null, true, Load.ALL));
 setColumns(new Column[] {gpartLOKK, gpartAKTIV, gpartCGPART, gpartNAZIV, gpartCAGENT/*, gpartNAZGPART*/});

    createFilteredDataSet(gruppartaktiv, "aktiv = 'D'");
  }

}