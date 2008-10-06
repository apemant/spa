/****license*****************************************************************
**   file: Grupeizv.java
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



public class Grupeizv extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Grupeizv Grupeizvclass;

  QueryDataSet grupeizv = new raDataSet();

  Column grupeizvCIZV = new Column();
  Column grupeizvCGRIZV = new Column();
  Column grupeizvNAZIV = new Column();
  Column grupeizvSUMBRUTO = new Column();
  Column grupeizvSUMSATI = new Column();
  Column grupeizvSUMNETO = new Column();
  Column grupeizvSUMNETO2 = new Column();
  Column grupeizvPARAMETRI = new Column();

  public static Grupeizv getDataModule() {
    if (Grupeizvclass == null) {
      Grupeizvclass = new Grupeizv();
    }
    return Grupeizvclass;
  }

  public QueryDataSet getQueryDataSet() {
    return grupeizv;
  }

  public Grupeizv() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    grupeizvCIZV.setCaption("Oznaka izvještaja");
    grupeizvCIZV.setColumnName("CIZV");
    grupeizvCIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    grupeizvCIZV.setPrecision(4);
    grupeizvCIZV.setRowId(true);
    grupeizvCIZV.setTableName("GRUPEIZV");
    grupeizvCIZV.setServerColumnName("CIZV");
    grupeizvCIZV.setSqlType(5);
    grupeizvCIZV.setWidth(4);
    grupeizvCGRIZV.setCaption("Oznaka grupe izvještaja");
    grupeizvCGRIZV.setColumnName("CGRIZV");
    grupeizvCGRIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    grupeizvCGRIZV.setPrecision(4);
    grupeizvCGRIZV.setRowId(true);
    grupeizvCGRIZV.setTableName("GRUPEIZV");
    grupeizvCGRIZV.setServerColumnName("CGRIZV");
    grupeizvCGRIZV.setSqlType(5);
    grupeizvCGRIZV.setWidth(4);
    grupeizvNAZIV.setCaption("Naziv grupe izvještaja");
    grupeizvNAZIV.setColumnName("NAZIV");
    grupeizvNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupeizvNAZIV.setPrecision(30);
    grupeizvNAZIV.setTableName("GRUPEIZV");
    grupeizvNAZIV.setServerColumnName("NAZIV");
    grupeizvNAZIV.setSqlType(1);
    grupeizvSUMBRUTO.setCaption("Suma bruto (D/N)");
    grupeizvSUMBRUTO.setColumnName("SUMBRUTO");
    grupeizvSUMBRUTO.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupeizvSUMBRUTO.setPrecision(1);
    grupeizvSUMBRUTO.setTableName("GRUPEIZV");
    grupeizvSUMBRUTO.setServerColumnName("SUMBRUTO");
    grupeizvSUMBRUTO.setSqlType(1);
    grupeizvSUMBRUTO.setDefault("D");
    grupeizvSUMSATI.setCaption("Suma sati (D/N)");
    grupeizvSUMSATI.setColumnName("SUMSATI");
    grupeizvSUMSATI.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupeizvSUMSATI.setPrecision(1);
    grupeizvSUMSATI.setTableName("GRUPEIZV");
    grupeizvSUMSATI.setServerColumnName("SUMSATI");
    grupeizvSUMSATI.setSqlType(1);
    grupeizvSUMSATI.setDefault("D");
    grupeizvSUMNETO.setCaption("Suma neto (D/N)");
    grupeizvSUMNETO.setColumnName("SUMNETO");
    grupeizvSUMNETO.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupeizvSUMNETO.setPrecision(1);
    grupeizvSUMNETO.setTableName("GRUPEIZV");
    grupeizvSUMNETO.setServerColumnName("SUMNETO");
    grupeizvSUMNETO.setSqlType(1);
    grupeizvSUMNETO.setDefault("D");
    grupeizvSUMNETO2.setCaption("Suma iznos na ruke (D/N)");
    grupeizvSUMNETO2.setColumnName("SUMNETO2");
    grupeizvSUMNETO2.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupeizvSUMNETO2.setPrecision(1);
    grupeizvSUMNETO2.setTableName("GRUPEIZV");
    grupeizvSUMNETO2.setServerColumnName("SUMNETO2");
    grupeizvSUMNETO2.setSqlType(1);
    grupeizvSUMNETO2.setDefault("D");
    grupeizvPARAMETRI.setCaption("Parametri");
    grupeizvPARAMETRI.setColumnName("PARAMETRI");
    grupeizvPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupeizvPARAMETRI.setPrecision(20);
    grupeizvPARAMETRI.setTableName("GRUPEIZV");
    grupeizvPARAMETRI.setServerColumnName("PARAMETRI");
    grupeizvPARAMETRI.setSqlType(1);
    grupeizvPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    grupeizv.setResolver(dm.getQresolver());
    grupeizv.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Grupeizv", null, true, Load.ALL));
 setColumns(new Column[] {grupeizvCIZV, grupeizvCGRIZV, grupeizvNAZIV, grupeizvSUMBRUTO, grupeizvSUMSATI, grupeizvSUMNETO, grupeizvSUMNETO2,
        grupeizvPARAMETRI});
  }

  public void setall() {

    ddl.create("Grupeizv")
       .addShort("cizv", 4, true)
       .addShort("cgrizv", 4, true)
       .addChar("naziv", 30)
       .addChar("sumbruto", 1, "D")
       .addChar("sumsati", 1, "D")
       .addChar("sumneto", 1, "D")
       .addChar("sumneto2", 1, "D")
       .addChar("parametri", 20)
       .addPrimaryKey("cizv,cgrizv");


    Naziv = "Grupeizv";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
