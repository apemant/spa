/****license*****************************************************************
**   file: KnjigeUI.java
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



public class KnjigeUI extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static KnjigeUI KnjigeUIclass;

  QueryDataSet knjigeui = new raDataSet();
  QueryDataSet knjigeu = new raDataSet();
  QueryDataSet knjigei = new raDataSet();

  Column knjigeuiLOKK = new Column();
  Column knjigeuiAKTIV = new Column();
  Column knjigeuiCKNJIGE = new Column();
  Column knjigeuiURAIRA = new Column();
  Column knjigeuiCSKL = new Column();
  Column knjigeuiVRDOK = new Column();
  Column knjigeuiNAZKNJIGE = new Column();
  Column knjigeuiNACBROJDOK = new Column();
  Column knjigeuiKNJIZENJE = new Column();
  Column knjigeuiVIRTUA = new Column();

  public static KnjigeUI getDataModule() {
    if (KnjigeUIclass == null) {
      KnjigeUIclass = new KnjigeUI();
    }
    return KnjigeUIclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return knjigeui;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKnjigeU() {
    return knjigeu;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKnjigeI() {
    return knjigei;
  }

  public KnjigeUI() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    knjigeuiLOKK.setCaption("Status zauzetosti");
    knjigeuiLOKK.setColumnName("LOKK");
    knjigeuiLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiLOKK.setPrecision(1);
    knjigeuiLOKK.setTableName("KNJIGEUI");
    knjigeuiLOKK.setServerColumnName("LOKK");
    knjigeuiLOKK.setSqlType(1);
    knjigeuiLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    knjigeuiLOKK.setDefault("N");
    knjigeuiAKTIV.setCaption("Aktivan - neaktivan");
    knjigeuiAKTIV.setColumnName("AKTIV");
    knjigeuiAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiAKTIV.setPrecision(1);
    knjigeuiAKTIV.setTableName("KNJIGEUI");
    knjigeuiAKTIV.setServerColumnName("AKTIV");
    knjigeuiAKTIV.setSqlType(1);
    knjigeuiAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    knjigeuiAKTIV.setDefault("D");
    knjigeuiCKNJIGE.setCaption("Oznaka");
    knjigeuiCKNJIGE.setColumnName("CKNJIGE");
    knjigeuiCKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiCKNJIGE.setPrecision(5);
    knjigeuiCKNJIGE.setRowId(true);
    knjigeuiCKNJIGE.setTableName("KNJIGEUI");
    knjigeuiCKNJIGE.setServerColumnName("CKNJIGE");
    knjigeuiCKNJIGE.setSqlType(1);
    knjigeuiURAIRA.setCaption("Indikator URA/IRA");
    knjigeuiURAIRA.setColumnName("URAIRA");
    knjigeuiURAIRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiURAIRA.setPrecision(1);
    knjigeuiURAIRA.setRowId(true);
    knjigeuiURAIRA.setTableName("KNJIGEUI");
    knjigeuiURAIRA.setServerColumnName("URAIRA");
    knjigeuiURAIRA.setSqlType(1);
    knjigeuiCSKL.setCaption("Vrsta sheme knjiženja");
    knjigeuiCSKL.setColumnName("CSKL");
    knjigeuiCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiCSKL.setPrecision(12);
    knjigeuiCSKL.setTableName("KNJIGEUI");
    knjigeuiCSKL.setServerColumnName("CSKL");
    knjigeuiCSKL.setSqlType(1);
    knjigeuiVRDOK.setCaption("Vrsta dokumenta");
    knjigeuiVRDOK.setColumnName("VRDOK");
    knjigeuiVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiVRDOK.setPrecision(3);
    knjigeuiVRDOK.setTableName("KNJIGEUI");
    knjigeuiVRDOK.setServerColumnName("VRDOK");
    knjigeuiVRDOK.setSqlType(1);
    knjigeuiNAZKNJIGE.setCaption("Naziv");
    knjigeuiNAZKNJIGE.setColumnName("NAZKNJIGE");
    knjigeuiNAZKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiNAZKNJIGE.setPrecision(30);
    knjigeuiNAZKNJIGE.setTableName("KNJIGEUI");
    knjigeuiNAZKNJIGE.setServerColumnName("NAZKNJIGE");
    knjigeuiNAZKNJIGE.setSqlType(1);
    knjigeuiNACBROJDOK.setCaption("Na\u010Din nu\u0111enja broja izlaznog dokumenta");
    knjigeuiNACBROJDOK.setColumnName("NACBROJDOK");
    knjigeuiNACBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiNACBROJDOK.setPrecision(1);
    knjigeuiNACBROJDOK.setTableName("KNJIGEUI");
    knjigeuiNACBROJDOK.setServerColumnName("NACBROJDOK");
    knjigeuiNACBROJDOK.setSqlType(1);
    knjigeuiNACBROJDOK.setDefault("0");
    
    knjigeuiKNJIZENJE.setCaption("Knjizenje u GK/SK");
    knjigeuiKNJIZENJE.setColumnName("KNJIZENJE");
    knjigeuiKNJIZENJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiKNJIZENJE.setPrecision(1);
    knjigeuiKNJIZENJE.setTableName("KNJIGEUI");
    knjigeuiKNJIZENJE.setServerColumnName("KNJIZENJE");
    knjigeuiKNJIZENJE.setSqlType(1);
    knjigeuiKNJIZENJE.setDefault("D");
    
    knjigeuiVIRTUA.setCaption("Virtualna knjiga");
    knjigeuiVIRTUA.setColumnName("VIRTUA");
    knjigeuiVIRTUA.setDataType(com.borland.dx.dataset.Variant.STRING);
    knjigeuiVIRTUA.setPrecision(1);
    knjigeuiVIRTUA.setTableName("KNJIGEUI");
    knjigeuiVIRTUA.setServerColumnName("VIRTUA");
    knjigeuiVIRTUA.setSqlType(1);
    knjigeuiVIRTUA.setDefault("N");
    
    knjigeui.setResolver(dm.getQresolver());
    knjigeui.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from KnjigeUI", null, true, Load.ALL));
 setColumns(new Column[] {knjigeuiLOKK, knjigeuiAKTIV, knjigeuiCKNJIGE, knjigeuiURAIRA, knjigeuiCSKL, knjigeuiVRDOK, knjigeuiNAZKNJIGE,
        knjigeuiNACBROJDOK, knjigeuiKNJIZENJE, knjigeuiVIRTUA});

    createFilteredDataSet(knjigeu, "uraira = 'U'");
    createFilteredDataSet(knjigei, "uraira = 'I'");
  }

  public void setall() {

    ddl.create("KnjigeUI")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cknjige", 5, true)
       .addChar("uraira", 1, true)
       .addChar("cskl", 12)
       .addChar("vrdok", 3)
       .addChar("nazknjige", 30)
       .addChar("nacbrojdok", 1, "0")
       .addChar("knjizenje", 1, "D")
       .addChar("virtua", 1, "N")
       .addPrimaryKey("cknjige,uraira");

    Naziv = "KnjigeUI";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
