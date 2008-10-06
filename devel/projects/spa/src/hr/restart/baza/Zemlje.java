/****license*****************************************************************
**   file: Zemlje.java
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



public class Zemlje extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Zemlje Zemljeclass;

  QueryDataSet zemlje = new raDataSet();

  Column zemljeLOKK = new Column();
  Column zemljeAKTIV = new Column();
  Column zemljeCZEMLJE = new Column();
  Column zemljeNAZIVZEM = new Column();
  Column zemljeOZNVAL = new Column();
  Column zemljeDNEVNICA = new Column();
  Column zemljeNOCENJE = new Column();
  Column zemljeLOCO = new Column();
  Column zemljeLITBENZ = new Column();
  Column zemljeINDPUTA = new Column();

  public static Zemlje getDataModule() {
    if (Zemljeclass == null) {
      Zemljeclass = new Zemlje();
    }
    return Zemljeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return zemlje;
  }

  public Zemlje() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    zemljeLOKK.setCaption("Status zauzetosti");
    zemljeLOKK.setColumnName("LOKK");
    zemljeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    zemljeLOKK.setPrecision(1);
    zemljeLOKK.setTableName("ZEMLJE");
    zemljeLOKK.setServerColumnName("LOKK");
    zemljeLOKK.setSqlType(1);
    zemljeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zemljeLOKK.setDefault("N");
    zemljeAKTIV.setCaption("Aktivan - neaktivan");
    zemljeAKTIV.setColumnName("AKTIV");
    zemljeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    zemljeAKTIV.setPrecision(1);
    zemljeAKTIV.setTableName("ZEMLJE");
    zemljeAKTIV.setServerColumnName("AKTIV");
    zemljeAKTIV.setSqlType(1);
    zemljeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zemljeAKTIV.setDefault("D");
    zemljeCZEMLJE.setCaption("Oznaka");
    zemljeCZEMLJE.setColumnName("CZEMLJE");
    zemljeCZEMLJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    zemljeCZEMLJE.setPrecision(3);
    zemljeCZEMLJE.setRowId(true);
    zemljeCZEMLJE.setTableName("ZEMLJE");
    zemljeCZEMLJE.setServerColumnName("CZEMLJE");
    zemljeCZEMLJE.setSqlType(1);
    zemljeNAZIVZEM.setCaption("Naziv");
    zemljeNAZIVZEM.setColumnName("NAZIVZEM");
    zemljeNAZIVZEM.setDataType(com.borland.dx.dataset.Variant.STRING);
    zemljeNAZIVZEM.setPrecision(50);
    zemljeNAZIVZEM.setTableName("ZEMLJE");
    zemljeNAZIVZEM.setServerColumnName("NAZIVZEM");
    zemljeNAZIVZEM.setSqlType(1);
    zemljeOZNVAL.setCaption("Valuta");
    zemljeOZNVAL.setColumnName("OZNVAL");
    zemljeOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    zemljeOZNVAL.setPrecision(3);
    zemljeOZNVAL.setTableName("ZEMLJE");
    zemljeOZNVAL.setServerColumnName("OZNVAL");
    zemljeOZNVAL.setSqlType(1);
    zemljeDNEVNICA.setCaption("Dnevnica");
    zemljeDNEVNICA.setColumnName("DNEVNICA");
    zemljeDNEVNICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    zemljeDNEVNICA.setPrecision(17);
    zemljeDNEVNICA.setScale(2);
    zemljeDNEVNICA.setDisplayMask("###,###,##0.00");
    zemljeDNEVNICA.setDefault("0");
    zemljeDNEVNICA.setTableName("ZEMLJE");
    zemljeDNEVNICA.setServerColumnName("DNEVNICA");
    zemljeDNEVNICA.setSqlType(2);
    zemljeDNEVNICA.setDefault("0");
    zemljeNOCENJE.setCaption("No\u0107enje");
    zemljeNOCENJE.setColumnName("NOCENJE");
    zemljeNOCENJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    zemljeNOCENJE.setPrecision(17);
    zemljeNOCENJE.setScale(2);
    zemljeNOCENJE.setDisplayMask("###,###,##0.00");
    zemljeNOCENJE.setDefault("0");
    zemljeNOCENJE.setTableName("ZEMLJE");
    zemljeNOCENJE.setServerColumnName("NOCENJE");
    zemljeNOCENJE.setSqlType(2);
    zemljeNOCENJE.setDefault("0");
    zemljeLOCO.setCaption("Prijevoz po km");
    zemljeLOCO.setColumnName("LOCO");
    zemljeLOCO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    zemljeLOCO.setPrecision(17);
    zemljeLOCO.setScale(2);
    zemljeLOCO.setDisplayMask("###,###,##0.00");
    zemljeLOCO.setDefault("0");
    zemljeLOCO.setTableName("ZEMLJE");
    zemljeLOCO.setServerColumnName("LOCO");
    zemljeLOCO.setSqlType(2);
    zemljeLOCO.setDefault("0");
    zemljeLITBENZ.setCaption("Prijevoz po litri goriva");
    zemljeLITBENZ.setColumnName("LITBENZ");
    zemljeLITBENZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    zemljeLITBENZ.setPrecision(17);
    zemljeLITBENZ.setScale(2);
    zemljeLITBENZ.setDisplayMask("###,###,##0.00");
    zemljeLITBENZ.setDefault("0");
    zemljeLITBENZ.setTableName("ZEMLJE");
    zemljeLITBENZ.setServerColumnName("LITBENZ");
    zemljeLITBENZ.setSqlType(2);
    zemljeLITBENZ.setDefault("0");
    zemljeINDPUTA.setCaption("Indikator puta");
    zemljeINDPUTA.setColumnName("INDPUTA");
    zemljeINDPUTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    zemljeINDPUTA.setPrecision(1);
    zemljeINDPUTA.setTableName("ZEMLJE");
    zemljeINDPUTA.setServerColumnName("INDPUTA");
    zemljeINDPUTA.setSqlType(1);
    zemljeINDPUTA.setDefault("Z");
    zemlje.setResolver(dm.getQresolver());
    zemlje.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Zemlje", null, true, Load.ALL));
 setColumns(new Column[] {zemljeLOKK, zemljeAKTIV, zemljeCZEMLJE, zemljeNAZIVZEM, zemljeOZNVAL, zemljeDNEVNICA, zemljeNOCENJE, zemljeLOCO,
        zemljeLITBENZ, zemljeINDPUTA});
  }

  public void setall() {

    ddl.create("Zemlje")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("czemlje", 3, true)
       .addChar("nazivzem", 50)
       .addChar("oznval", 3)
       .addFloat("dnevnica", 17, 2)
       .addFloat("nocenje", 17, 2)
       .addFloat("loco", 17, 2)
       .addFloat("litbenz", 17, 2)
       .addChar("indputa", 1, "Z")
       .addPrimaryKey("czemlje");


    Naziv = "Zemlje";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
