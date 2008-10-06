/****license*****************************************************************
**   file: Paramblpn.java
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



public class Paramblpn extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Paramblpn Paramblpnclass;

  QueryDataSet parbl = new raDataSet();

  Column parblKNJIG = new Column();
  Column parblCSKL = new Column();
  Column parblVRDOK = new Column();
  Column parblSTAVKAPNZ = new Column();
  Column parblSTAVKAPNI = new Column();
  Column parblSTAVKAF = new Column();
  Column parblPNF = new Column();
  Column parblPARAMETRI = new Column();

  public static Paramblpn getDataModule() {
    if (Paramblpnclass == null) {
      Paramblpnclass = new Paramblpn();
    }
    return Paramblpnclass;
  }

  public QueryDataSet getQueryDataSet() {
    return parbl;
  }

  public Paramblpn() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    parblKNJIG.setCaption("Knjigovodstvo");
    parblKNJIG.setColumnName("KNJIG");
    parblKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    parblKNJIG.setPrecision(12);
    parblKNJIG.setRowId(true);
    parblKNJIG.setTableName("PARAMBLPN");
    parblKNJIG.setServerColumnName("KNJIG");
    parblKNJIG.setSqlType(1);
    parblCSKL.setCaption("Vrsta stavke");
    parblCSKL.setColumnName("CSKL");
    parblCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    parblCSKL.setPrecision(12);
    parblCSKL.setTableName("PARAMBLPN");
    parblCSKL.setServerColumnName("CSKL");
    parblCSKL.setSqlType(1);
    parblVRDOK.setCaption("Vrsta dokumenta");
    parblVRDOK.setColumnName("VRDOK");
    parblVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    parblVRDOK.setPrecision(3);
    parblVRDOK.setTableName("PARAMBLPN");
    parblVRDOK.setServerColumnName("VRDOK");
    parblVRDOK.setSqlType(1);
    parblVRDOK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parblSTAVKAPNZ.setCaption("Trošak PN u zemlji");
    parblSTAVKAPNZ.setColumnName("STAVKAPNZ");
    parblSTAVKAPNZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    parblSTAVKAPNZ.setPrecision(8);
    parblSTAVKAPNZ.setTableName("PARAMBLPN");
    parblSTAVKAPNZ.setServerColumnName("STAVKAPNZ");
    parblSTAVKAPNZ.setSqlType(1);
    parblSTAVKAPNI.setCaption("Trošak PN u inozemstvu");
    parblSTAVKAPNI.setColumnName("STAVKAPNI");
    parblSTAVKAPNI.setDataType(com.borland.dx.dataset.Variant.STRING);
    parblSTAVKAPNI.setPrecision(8);
    parblSTAVKAPNI.setTableName("PARAMBLPN");
    parblSTAVKAPNI.setServerColumnName("STAVKAPNI");
    parblSTAVKAPNI.setSqlType(1);
    parblSTAVKAF.setCaption("Footer isplatnice / uplatnice");
    parblSTAVKAF.setColumnName("STAVKAF");
    parblSTAVKAF.setDataType(com.borland.dx.dataset.Variant.STRING);
    parblSTAVKAF.setPrecision(500);
    parblSTAVKAF.setTableName("PARAMBLPN");
    parblSTAVKAF.setServerColumnName("STAVKAF");
    parblSTAVKAF.setSqlType(1);
    parblPNF.setCaption("Footer putnog naloga");
    parblPNF.setColumnName("PNF");
    parblPNF.setDataType(com.borland.dx.dataset.Variant.STRING);
    parblPNF.setPrecision(200);
    parblPNF.setTableName("PARAMBLPN");
    parblPNF.setServerColumnName("PNF");
    parblPNF.setSqlType(1);
    parblPARAMETRI.setCaption("Parametri");
    parblPARAMETRI.setColumnName("PARAMETRI");
    parblPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    parblPARAMETRI.setPrecision(20);
    parblPARAMETRI.setTableName("PARAMBLPN");
    parblPARAMETRI.setServerColumnName("PARAMETRI");
    parblPARAMETRI.setSqlType(1);
    parblPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parbl.setResolver(dm.getQresolver());
    parbl.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Paramblpn", null, true, Load.ALL));
 setColumns(new Column[] {parblKNJIG, parblCSKL, parblVRDOK, parblSTAVKAPNZ, parblSTAVKAPNI, parblSTAVKAF, parblPNF, parblPARAMETRI});
  }

  public void setall() {

    ddl.create("Paramblpn")
       .addChar("knjig", 12, true)
       .addChar("cskl", 12)
       .addChar("vrdok", 3)
       .addChar("stavkapnz", 8)
       .addChar("stavkapni", 8)
       .addChar("stavkaf", 500)
       .addChar("pnf", 200)
       .addChar("parametri", 20)
       .addPrimaryKey("knjig");


    Naziv = "Paramblpn";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
