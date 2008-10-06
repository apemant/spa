/****license*****************************************************************
**   file: Telehist.java
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



public class Telehist extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Telehist Telehistclass;

  QueryDataSet telh = new QueryDataSet();

  Column telhCHIST = new Column();
  Column telhCTEL = new Column();
  Column telhCPAR = new Column();
  Column telhDATUMOD = new Column();
  Column telhDATUMDO = new Column();

  public static Telehist getDataModule() {
    if (Telehistclass == null) {
      Telehistclass = new Telehist();
    }
    return Telehistclass;
  }

  public QueryDataSet getQueryDataSet() {
    return telh;
  }

  public Telehist() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    telhCHIST.setCaption("Auto");
    telhCHIST.setColumnName("CHIST");
    telhCHIST.setDataType(com.borland.dx.dataset.Variant.INT);
    telhCHIST.setRowId(true);
    telhCHIST.setTableName("TELEHIST");
    telhCHIST.setServerColumnName("CHIST");
    telhCHIST.setSqlType(4);
    telhCHIST.setWidth(6);
    telhCHIST.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    telhCTEL.setCaption("Telemarketer");
    telhCTEL.setColumnName("CTEL");
    telhCTEL.setDataType(com.borland.dx.dataset.Variant.INT);
    telhCTEL.setTableName("TELEHIST");
    telhCTEL.setServerColumnName("CTEL");
    telhCTEL.setSqlType(4);
    telhCTEL.setWidth(6);
    telhCPAR.setCaption("Partner");
    telhCPAR.setColumnName("CPAR");
    telhCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    telhCPAR.setTableName("TELEHIST");
    telhCPAR.setServerColumnName("CPAR");
    telhCPAR.setSqlType(4);
    telhCPAR.setWidth(6);
    telhDATUMOD.setCaption("Od");
    telhDATUMOD.setColumnName("DATUMOD");
    telhDATUMOD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    telhDATUMOD.setDisplayMask("dd-MM-yyyy");
//    telhDATUMOD.setEditMask("dd-MM-yyyy");
    telhDATUMOD.setTableName("TELEHIST");
    telhDATUMOD.setServerColumnName("DATUMOD");
    telhDATUMOD.setSqlType(93);
    telhDATUMOD.setWidth(10);
    telhDATUMDO.setCaption("Do");
    telhDATUMDO.setColumnName("DATUMDO");
    telhDATUMDO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    telhDATUMDO.setDisplayMask("dd-MM-yyyy");
//    telhDATUMDO.setEditMask("dd-MM-yyyy");
    telhDATUMDO.setTableName("TELEHIST");
    telhDATUMDO.setServerColumnName("DATUMDO");
    telhDATUMDO.setSqlType(93);
    telhDATUMDO.setWidth(10);
    telh.setResolver(dm.getQresolver());
    telh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Telehist", null, true, Load.ALL));
    setColumns(new Column[] {telhCHIST, telhCTEL, telhCPAR, telhDATUMOD, telhDATUMDO});
  }

  public void setall() {

    ddl.create("Telehist")
       .addInteger("chist", 6, true)
       .addInteger("ctel", 6)
       .addInteger("cpar", 6)
       .addDate("datumod")
       .addDate("datumdo")
       .addPrimaryKey("chist");


    Naziv = "Telehist";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpar"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
