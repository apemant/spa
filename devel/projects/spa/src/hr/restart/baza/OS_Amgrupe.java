/****license*****************************************************************
**   file: OS_Amgrupe.java
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



public class OS_Amgrupe extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Amgrupe OS_Amgrupeclass;

  QueryDataSet osamg = new raDataSet();

  Column osamgLOKK = new Column();
  Column osamgAKTIV = new Column();
  Column osamgCGRUPE = new Column();
  Column osamgNAZGRUPE = new Column();
  Column osamgZAKSTOPA = new Column();
  Column osamgODLSTOPA = new Column();

  public static OS_Amgrupe getDataModule() {
    if (OS_Amgrupeclass == null) {
      OS_Amgrupeclass = new OS_Amgrupe();
    }
    return OS_Amgrupeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osamg;
  }

  public OS_Amgrupe() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osamgLOKK.setCaption("Status zauzetosti");
    osamgLOKK.setColumnName("LOKK");
    osamgLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osamgLOKK.setPrecision(1);
    osamgLOKK.setTableName("OS_AMGRUPE");
    osamgLOKK.setServerColumnName("LOKK");
    osamgLOKK.setSqlType(1);
    osamgLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osamgLOKK.setDefault("N");
    osamgAKTIV.setCaption("Aktivan - neaktivan");
    osamgAKTIV.setColumnName("AKTIV");
    osamgAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osamgAKTIV.setPrecision(1);
    osamgAKTIV.setTableName("OS_AMGRUPE");
    osamgAKTIV.setServerColumnName("AKTIV");
    osamgAKTIV.setSqlType(1);
    osamgAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osamgAKTIV.setDefault("D");
    osamgCGRUPE.setCaption("Šifra");
    osamgCGRUPE.setColumnName("CGRUPE");
    osamgCGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osamgCGRUPE.setPrecision(6);
    osamgCGRUPE.setRowId(true);
    osamgCGRUPE.setTableName("OS_AMGRUPE");
    osamgCGRUPE.setServerColumnName("CGRUPE");
    osamgCGRUPE.setSqlType(1);
    osamgNAZGRUPE.setCaption("Naziv");
    osamgNAZGRUPE.setColumnName("NAZGRUPE");
    osamgNAZGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osamgNAZGRUPE.setPrecision(50);
    osamgNAZGRUPE.setTableName("OS_AMGRUPE");
    osamgNAZGRUPE.setServerColumnName("NAZGRUPE");
    osamgNAZGRUPE.setSqlType(1);
    osamgNAZGRUPE.setWidth(30);
    osamgZAKSTOPA.setCaption("Zakonska stopa");
    osamgZAKSTOPA.setColumnName("ZAKSTOPA");
    osamgZAKSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osamgZAKSTOPA.setPrecision(10);
    osamgZAKSTOPA.setScale(4);
    osamgZAKSTOPA.setDisplayMask("###,###,##0.0000");
    osamgZAKSTOPA.setDefault("0");
    osamgZAKSTOPA.setTableName("OS_AMGRUPE");
    osamgZAKSTOPA.setServerColumnName("ZAKSTOPA");
    osamgZAKSTOPA.setSqlType(2);
    osamgZAKSTOPA.setDefault("0");
    osamgZAKSTOPA.setWidth(8);
    osamgODLSTOPA.setCaption("Stopa po odluci");
    osamgODLSTOPA.setColumnName("ODLSTOPA");
    osamgODLSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osamgODLSTOPA.setPrecision(10);
    osamgODLSTOPA.setScale(4);
    osamgODLSTOPA.setDisplayMask("###,###,##0.0000");
    osamgODLSTOPA.setDefault("0");
    osamgODLSTOPA.setTableName("OS_AMGRUPE");
    osamgODLSTOPA.setServerColumnName("ODLSTOPA");
    osamgODLSTOPA.setSqlType(2);
    osamgODLSTOPA.setDefault("0");
    osamgODLSTOPA.setWidth(8);
    osamg.setResolver(dm.getQresolver());
    osamg.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Amgrupe", null, true, Load.ALL));
 setColumns(new Column[] {osamgLOKK, osamgAKTIV, osamgCGRUPE, osamgNAZGRUPE, osamgZAKSTOPA, osamgODLSTOPA});
  }

  public void setall() {

    ddl.create("OS_Amgrupe")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cgrupe", 6, true)
       .addChar("nazgrupe", 50)
       .addFloat("zakstopa", 10, 4)
       .addFloat("odlstopa", 10, 4)
       .addPrimaryKey("cgrupe");


    Naziv = "OS_Amgrupe";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
