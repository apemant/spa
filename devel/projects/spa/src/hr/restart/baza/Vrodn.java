/****license*****************************************************************
**   file: Vrodn.java
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



public class Vrodn extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrodn Vrodnclass;

  QueryDataSet vrodn = new raDataSet();

  Column vrodnLOKK = new Column();
  Column vrodnAKTIV = new Column();
  Column vrodnCVRO = new Column();
  Column vrodnNAZIVRO = new Column();
  Column vrodnKOEF = new Column();
  Column vrodnPARAMETRI = new Column();

  public static Vrodn getDataModule() {
    if (Vrodnclass == null) {
      Vrodnclass = new Vrodn();
    }
    return Vrodnclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vrodn;
  }

  public Vrodn() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vrodnLOKK.setCaption("Status zauzetosti");
    vrodnLOKK.setColumnName("LOKK");
    vrodnLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrodnLOKK.setPrecision(1);
    vrodnLOKK.setTableName("VRODN");
    vrodnLOKK.setServerColumnName("LOKK");
    vrodnLOKK.setSqlType(1);
    vrodnLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrodnLOKK.setDefault("N");
    vrodnAKTIV.setCaption("Aktivan - neaktivan");
    vrodnAKTIV.setColumnName("AKTIV");
    vrodnAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrodnAKTIV.setPrecision(1);
    vrodnAKTIV.setTableName("VRODN");
    vrodnAKTIV.setServerColumnName("AKTIV");
    vrodnAKTIV.setSqlType(1);
    vrodnAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrodnAKTIV.setDefault("D");
    vrodnCVRO.setCaption("Oznaka");
    vrodnCVRO.setColumnName("CVRO");
    vrodnCVRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrodnCVRO.setPrecision(6);
    vrodnCVRO.setRowId(true);
    vrodnCVRO.setTableName("VRODN");
    vrodnCVRO.setServerColumnName("CVRO");
    vrodnCVRO.setSqlType(1);
    vrodnNAZIVRO.setCaption("Naziv");
    vrodnNAZIVRO.setColumnName("NAZIVRO");
    vrodnNAZIVRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrodnNAZIVRO.setPrecision(50);
    vrodnNAZIVRO.setTableName("VRODN");
    vrodnNAZIVRO.setServerColumnName("NAZIVRO");
    vrodnNAZIVRO.setSqlType(1);
    vrodnKOEF.setCaption("Koeficijent");
    vrodnKOEF.setColumnName("KOEF");
    vrodnKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vrodnKOEF.setPrecision(17);
    vrodnKOEF.setScale(4);
    vrodnKOEF.setDisplayMask("###,###,##0.0000");
    vrodnKOEF.setDefault("0");
    vrodnKOEF.setTableName("VRODN");
    vrodnKOEF.setServerColumnName("KOEF");
    vrodnKOEF.setSqlType(2);
    vrodnKOEF.setDefault("0");
    vrodnPARAMETRI.setCaption("Parametri");
    vrodnPARAMETRI.setColumnName("PARAMETRI");
    vrodnPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrodnPARAMETRI.setPrecision(20);
    vrodnPARAMETRI.setTableName("VRODN");
    vrodnPARAMETRI.setServerColumnName("PARAMETRI");
    vrodnPARAMETRI.setSqlType(1);
    vrodnPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrodn.setResolver(dm.getQresolver());
    vrodn.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vrodn", null, true, Load.ALL));
 setColumns(new Column[] {vrodnLOKK, vrodnAKTIV, vrodnCVRO, vrodnNAZIVRO, vrodnKOEF, vrodnPARAMETRI});
  }

  public void setall() {

    ddl.create("Vrodn")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvro", 6, true)
       .addChar("nazivro", 50)
       .addFloat("koef", 17, 4)
       .addChar("parametri", 20)
       .addPrimaryKey("cvro");


    Naziv = "Vrodn";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
