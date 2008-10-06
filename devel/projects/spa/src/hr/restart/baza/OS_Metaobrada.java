/****license*****************************************************************
**   file: OS_Metaobrada.java
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



public class OS_Metaobrada extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Metaobrada OS_Metaobradaclass;

  QueryDataSet osmo = new QueryDataSet();

  Column osmoCORG = new Column();
  Column osmoCORG2 = new Column();
  Column osmoDATUMDO = new Column();
  Column osmoDATUMOD = new Column();
  Column osmoDATUMDO2 = new Column();
  Column osmoDATUMOD2 = new Column();
  Column osmoTIPAMOR = new Column();
  Column osmoVRSTAAMOR = new Column();
  Column osmoVRSTAREVA = new Column();
  Column osmoREVAAMOR = new Column();
  Column osmoISPISINVBROJ = new Column();
  Column osmoNAZIVIZV = new Column();

  public static OS_Metaobrada getDataModule() {
    if (OS_Metaobradaclass == null) {
      OS_Metaobradaclass = new OS_Metaobrada();
    }
    return OS_Metaobradaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osmo;
  }

  public OS_Metaobrada() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osmoCORG.setCaption("OJ");
    osmoCORG.setColumnName("CORG");
    osmoCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osmoCORG.setPrecision(12);
    osmoCORG.setRowId(true);
    osmoCORG.setTableName("OS_METAOBRADA");
    osmoCORG.setServerColumnName("CORG");
    osmoCORG.setSqlType(1);
    osmoCORG.setWidth(6);
    osmoCORG2.setCaption("Šifra org. Jedinice - deprecated");
    osmoCORG2.setColumnName("CORG2");
    osmoCORG2.setDataType(com.borland.dx.dataset.Variant.STRING);
    osmoCORG2.setPrecision(12);
    osmoCORG2.setTableName("OS_METAOBRADA");
    osmoCORG2.setServerColumnName("CORG2");
    osmoCORG2.setSqlType(1);
    osmoCORG2.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osmoDATUMDO.setCaption("Datum do");
    osmoDATUMDO.setColumnName("DATUMDO");
    osmoDATUMDO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osmoDATUMDO.setPrecision(8);
    osmoDATUMDO.setDisplayMask("dd-MM-yyyy");
//    osmoDATUMDO.setEditMask("dd-MM-yyyy");
    osmoDATUMDO.setTableName("OS_METAOBRADA");
    osmoDATUMDO.setServerColumnName("DATUMDO");
    osmoDATUMDO.setSqlType(93);
    osmoDATUMDO.setWidth(10);
    osmoDATUMOD.setCaption("Datum od");
    osmoDATUMOD.setColumnName("DATUMOD");
    osmoDATUMOD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osmoDATUMOD.setPrecision(8);
    osmoDATUMOD.setDisplayMask("dd-MM-yyyy");
//    osmoDATUMOD.setEditMask("dd-MM-yyyy");
    osmoDATUMOD.setTableName("OS_METAOBRADA");
    osmoDATUMOD.setServerColumnName("DATUMOD");
    osmoDATUMOD.setSqlType(93);
    osmoDATUMOD.setWidth(10);
    osmoDATUMDO2.setCaption("Datum obra\u010Duna - deprecated");
    osmoDATUMDO2.setColumnName("DATUMDO2");
    osmoDATUMDO2.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osmoDATUMDO2.setPrecision(8);
    osmoDATUMDO2.setDisplayMask("dd-MM-yyyy");
    osmoDATUMDO2.setEditMask("dd-MM-yyyy");
    osmoDATUMDO2.setTableName("OS_METAOBRADA");
    osmoDATUMDO2.setServerColumnName("DATUMDO2");
    osmoDATUMDO2.setSqlType(93);
    osmoDATUMDO2.setWidth(10);
    osmoDATUMDO2.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osmoDATUMOD2.setCaption("Datum obra\u010Duna - deprecated");
    osmoDATUMOD2.setColumnName("DATUMOD2");
    osmoDATUMOD2.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osmoDATUMOD2.setPrecision(8);
    osmoDATUMOD2.setDisplayMask("dd-MM-yyyy");
    osmoDATUMOD2.setEditMask("dd-MM-yyyy");
    osmoDATUMOD2.setTableName("OS_METAOBRADA");
    osmoDATUMOD2.setServerColumnName("DATUMOD2");
    osmoDATUMOD2.setSqlType(93);
    osmoDATUMOD2.setWidth(10);
    osmoDATUMOD2.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osmoTIPAMOR.setCaption("Tip amortizacije");
    osmoTIPAMOR.setColumnName("TIPAMOR");
    osmoTIPAMOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    osmoTIPAMOR.setPrecision(1);
    osmoTIPAMOR.setTableName("OS_METAOBRADA");
    osmoTIPAMOR.setServerColumnName("TIPAMOR");
    osmoTIPAMOR.setSqlType(1);
    osmoVRSTAAMOR.setCaption("Vrsta amortizacije");
    osmoVRSTAAMOR.setColumnName("VRSTAAMOR");
    osmoVRSTAAMOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    osmoVRSTAAMOR.setPrecision(1);
    osmoVRSTAAMOR.setTableName("OS_METAOBRADA");
    osmoVRSTAAMOR.setServerColumnName("VRSTAAMOR");
    osmoVRSTAAMOR.setSqlType(1);
    osmoVRSTAREVA.setCaption("Vrsta revalorizacije");
    osmoVRSTAREVA.setColumnName("VRSTAREVA");
    osmoVRSTAREVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osmoVRSTAREVA.setPrecision(1);
    osmoVRSTAREVA.setTableName("OS_METAOBRADA");
    osmoVRSTAREVA.setServerColumnName("VRSTAREVA");
    osmoVRSTAREVA.setSqlType(1);
    osmoREVAAMOR.setCaption("Revalorizacija amortizacije");
    osmoREVAAMOR.setColumnName("REVAAMOR");
    osmoREVAAMOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    osmoREVAAMOR.setPrecision(1);
    osmoREVAAMOR.setTableName("OS_METAOBRADA");
    osmoREVAAMOR.setServerColumnName("REVAAMOR");
    osmoREVAAMOR.setSqlType(1);
    osmoISPISINVBROJ.setCaption("Ispis inventarnog broja");
    osmoISPISINVBROJ.setColumnName("ISPISINVBROJ");
    osmoISPISINVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osmoISPISINVBROJ.setPrecision(1);
    osmoISPISINVBROJ.setTableName("OS_METAOBRADA");
    osmoISPISINVBROJ.setServerColumnName("ISPISINVBROJ");
    osmoISPISINVBROJ.setSqlType(1);
    osmoNAZIVIZV.setCaption("Naziv izvještaja");
    osmoNAZIVIZV.setColumnName("NAZIVIZV");
    osmoNAZIVIZV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osmoNAZIVIZV.setPrecision(50);
    osmoNAZIVIZV.setTableName("OS_METAOBRADA");
    osmoNAZIVIZV.setServerColumnName("NAZIVIZV");
    osmoNAZIVIZV.setSqlType(1);
    osmoNAZIVIZV.setWidth(30);
    osmo.setResolver(dm.getQresolver());
    osmo.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Metaobrada", null, true, Load.ALL));
 setColumns(new Column[] {osmoCORG, osmoCORG2, osmoDATUMDO, osmoDATUMOD, osmoDATUMDO2, osmoDATUMOD2, osmoTIPAMOR, osmoVRSTAAMOR, osmoVRSTAREVA,
        osmoREVAAMOR, osmoISPISINVBROJ, osmoNAZIVIZV});
  }

  public void setall() {

    ddl.create("OS_Metaobrada")
       .addChar("corg", 12, true)
       .addChar("corg2", 12)
       .addDate("datumdo")
       .addDate("datumod")
       .addDate("datumdo2")
       .addDate("datumod2")
       .addChar("tipamor", 1)
       .addChar("vrstaamor", 1)
       .addChar("vrstareva", 1)
       .addChar("revaamor", 1)
       .addChar("ispisinvbroj", 1)
       .addChar("nazivizv", 50)
       .addPrimaryKey("corg");


    Naziv = "OS_Metaobrada";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
