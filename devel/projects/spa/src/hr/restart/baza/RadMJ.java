/****license*****************************************************************
**   file: RadMJ.java
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



public class RadMJ extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static RadMJ RadMJclass;

  QueryDataSet radmj = new raDataSet();

  Column radmjLOKK = new Column();
  Column radmjAKTIV = new Column();
  Column radmjCRADMJ = new Column();
  Column radmjNAZIVRM = new Column();
  Column radmjKOEF = new Column();
  Column radmjBODOVI = new Column();
  Column radmjDODBOD = new Column();
  Column radmjPARAMETRI = new Column();

  public static RadMJ getDataModule() {
    if (RadMJclass == null) {
      RadMJclass = new RadMJ();
    }
    return RadMJclass;
  }

  public QueryDataSet getQueryDataSet() {
    return radmj;
  }

  public RadMJ() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    radmjLOKK.setCaption("Status zauzetosti");
    radmjLOKK.setColumnName("LOKK");
    radmjLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    radmjLOKK.setPrecision(1);
    radmjLOKK.setTableName("RADMJ");
    radmjLOKK.setServerColumnName("LOKK");
    radmjLOKK.setSqlType(1);
    radmjLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    radmjLOKK.setDefault("N");
    radmjAKTIV.setCaption("Aktivan - neaktivan");
    radmjAKTIV.setColumnName("AKTIV");
    radmjAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    radmjAKTIV.setPrecision(1);
    radmjAKTIV.setTableName("RADMJ");
    radmjAKTIV.setServerColumnName("AKTIV");
    radmjAKTIV.setSqlType(1);
    radmjAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    radmjAKTIV.setDefault("D");
    radmjCRADMJ.setCaption("Oznaka");
    radmjCRADMJ.setColumnName("CRADMJ");
    radmjCRADMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    radmjCRADMJ.setPrecision(6);
    radmjCRADMJ.setRowId(true);
    radmjCRADMJ.setTableName("RADMJ");
    radmjCRADMJ.setServerColumnName("CRADMJ");
    radmjCRADMJ.setSqlType(1);
    radmjNAZIVRM.setCaption("Naziv");
    radmjNAZIVRM.setColumnName("NAZIVRM");
    radmjNAZIVRM.setDataType(com.borland.dx.dataset.Variant.STRING);
    radmjNAZIVRM.setPrecision(50);
    radmjNAZIVRM.setTableName("RADMJ");
    radmjNAZIVRM.setServerColumnName("NAZIVRM");
    radmjNAZIVRM.setSqlType(1);
    radmjKOEF.setCaption("Koeficijent");
    radmjKOEF.setColumnName("KOEF");
    radmjKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    radmjKOEF.setPrecision(17);
    radmjKOEF.setScale(4);
    radmjKOEF.setDisplayMask("###,###,##0.0000");
    radmjKOEF.setDefault("0");
    radmjKOEF.setTableName("RADMJ");
    radmjKOEF.setServerColumnName("KOEF");
    radmjKOEF.setSqlType(2);
    radmjKOEF.setDefault("0");
    radmjBODOVI.setCaption("Bodovi");
    radmjBODOVI.setColumnName("BODOVI");
    radmjBODOVI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    radmjBODOVI.setPrecision(17);
    radmjBODOVI.setScale(2);
    radmjBODOVI.setDisplayMask("###,###,##0.00");
    radmjBODOVI.setDefault("0");
    radmjBODOVI.setTableName("RADMJ");
    radmjBODOVI.setServerColumnName("BODOVI");
    radmjBODOVI.setSqlType(2);
    radmjBODOVI.setDefault("0");
    radmjDODBOD.setCaption("Dodatni bodovi");
    radmjDODBOD.setColumnName("DODBOD");
    radmjDODBOD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    radmjDODBOD.setPrecision(17);
    radmjDODBOD.setScale(2);
    radmjDODBOD.setDisplayMask("###,###,##0.00");
    radmjDODBOD.setDefault("0");
    radmjDODBOD.setTableName("RADMJ");
    radmjDODBOD.setServerColumnName("DODBOD");
    radmjDODBOD.setSqlType(2);
    radmjDODBOD.setDefault("0");
    radmjPARAMETRI.setCaption("Parametri");
    radmjPARAMETRI.setColumnName("PARAMETRI");
    radmjPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    radmjPARAMETRI.setPrecision(20);
    radmjPARAMETRI.setTableName("RADMJ");
    radmjPARAMETRI.setServerColumnName("PARAMETRI");
    radmjPARAMETRI.setSqlType(1);
    radmjPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    radmj.setResolver(dm.getQresolver());
    radmj.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from RadMJ", null, true, Load.ALL));
 setColumns(new Column[] {radmjLOKK, radmjAKTIV, radmjCRADMJ, radmjNAZIVRM, radmjKOEF, radmjBODOVI, radmjDODBOD, radmjPARAMETRI});
  }

  public void setall() {

    ddl.create("RadMJ")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cradmj", 6, true)
       .addChar("nazivrm", 50)
       .addFloat("koef", 17, 4)
       .addFloat("bodovi", 17, 2)
       .addFloat("dodbod", 17, 2)
       .addChar("parametri", 20)
       .addPrimaryKey("cradmj");


    Naziv = "RadMJ";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
