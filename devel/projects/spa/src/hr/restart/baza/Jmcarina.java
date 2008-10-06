/****license*****************************************************************
**   file: Jmcarina.java
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



public class Jmcarina extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Jmcarina Jmcarinaclass;

  QueryDataSet jmcarina = new QueryDataSet();

  Column jmcarinaLOCK = new Column();
  Column jmcarinaAKTIV = new Column();
  Column jmcarinaCJMCAR = new Column();
  Column jmcarinaCJMCAROZN = new Column();
  Column jmcarinaNAZIV = new Column();

  public static Jmcarina getDataModule() {
    if (Jmcarinaclass == null) {
      Jmcarinaclass = new Jmcarina();
    }
    return Jmcarinaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return jmcarina;
  }

  public Jmcarina() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jmcarinaLOCK.setCaption("Status zauzetosti");
    jmcarinaLOCK.setColumnName("LOCK");
    jmcarinaLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    jmcarinaLOCK.setPrecision(1);
    jmcarinaLOCK.setTableName("JMCARINA");
    jmcarinaLOCK.setServerColumnName("LOCK");
    jmcarinaLOCK.setSqlType(1);
    jmcarinaLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    jmcarinaLOCK.setDefault("N");
    jmcarinaAKTIV.setCaption("Aktivan - neaktivan");
    jmcarinaAKTIV.setColumnName("AKTIV");
    jmcarinaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    jmcarinaAKTIV.setPrecision(1);
    jmcarinaAKTIV.setTableName("JMCARINA");
    jmcarinaAKTIV.setServerColumnName("AKTIV");
    jmcarinaAKTIV.setSqlType(1);
    jmcarinaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    jmcarinaAKTIV.setDefault("D");
    jmcarinaCJMCAR.setCaption("Šifra mjerne jedinice");
    jmcarinaCJMCAR.setColumnName("CJMCAR");
    jmcarinaCJMCAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    jmcarinaCJMCAR.setPrecision(10);
    jmcarinaCJMCAR.setRowId(true);
    jmcarinaCJMCAR.setTableName("JMCARINA");
    jmcarinaCJMCAR.setServerColumnName("CJMCAR");
    jmcarinaCJMCAR.setSqlType(1);
    jmcarinaCJMCAROZN.setCaption("Oznaka mjerne jedinice");
    jmcarinaCJMCAROZN.setColumnName("CJMCAROZN");
    jmcarinaCJMCAROZN.setDataType(com.borland.dx.dataset.Variant.STRING);
    jmcarinaCJMCAROZN.setPrecision(10);
    jmcarinaCJMCAROZN.setTableName("JMCARINA");
    jmcarinaCJMCAROZN.setServerColumnName("CJMCAROZN");
    jmcarinaCJMCAROZN.setSqlType(1);
    jmcarinaNAZIV.setCaption("Naziv");
    jmcarinaNAZIV.setColumnName("NAZIV");
    jmcarinaNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    jmcarinaNAZIV.setPrecision(80);
    jmcarinaNAZIV.setTableName("JMCARINA");
    jmcarinaNAZIV.setServerColumnName("NAZIV");
    jmcarinaNAZIV.setSqlType(1);
    jmcarinaNAZIV.setWidth(30);
    jmcarina.setResolver(dm.getQresolver());
    jmcarina.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Jmcarina", null, true, Load.ALL));
    setColumns(new Column[] {jmcarinaLOCK, jmcarinaAKTIV, jmcarinaCJMCAR, jmcarinaCJMCAROZN, jmcarinaNAZIV});
  }

  public void setall() {

    ddl.create("Jmcarina")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cjmcar", 10, true)
       .addChar("cjmcarozn", 10)
       .addChar("naziv", 80)
       .addPrimaryKey("cjmcar");


    Naziv = "Jmcarina";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cjmcar"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
