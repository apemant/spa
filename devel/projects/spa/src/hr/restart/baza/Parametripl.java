/****license*****************************************************************
**   file: Parametripl.java
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



public class Parametripl extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Parametripl Parametriplclass;

  QueryDataSet ppl = new raDataSet();

  Column pplCPARPL = new Column();
  Column pplMINPL = new Column();
  Column pplMINOSDOP = new Column();
  Column pplOSNPOR1 = new Column();
  Column pplOSNPOR2 = new Column();
  Column pplOSNPOR3 = new Column();
  Column pplOSNPOR4 = new Column();
  Column pplOSNPOR5 = new Column();
  Column pplPARAMETRI = new Column();

  public static Parametripl getDataModule() {
    if (Parametriplclass == null) {
      Parametriplclass = new Parametripl();
    }
    return Parametriplclass;
  }

  public QueryDataSet getQueryDataSet() {
    return ppl;
  }

  public Parametripl() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    pplCPARPL.setCaption("Klju\u010D");
    pplCPARPL.setColumnName("CPARPL");
    pplCPARPL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pplCPARPL.setPrecision(1);
    pplCPARPL.setRowId(true);
    pplCPARPL.setTableName("PARAMETRIPL");
    pplCPARPL.setServerColumnName("CPARPL");
    pplCPARPL.setSqlType(5);
    pplCPARPL.setWidth(1);
    pplCPARPL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pplCPARPL.setDefault("1");
    pplMINPL.setCaption("Minimalna pla\u0107a - olakšice");
    pplMINPL.setColumnName("MINPL");
    pplMINPL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pplMINPL.setPrecision(17);
    pplMINPL.setScale(2);
    pplMINPL.setDisplayMask("###,###,##0.00");
    pplMINPL.setDefault("0");
    pplMINPL.setTableName("PARAMETRIPL");
    pplMINPL.setServerColumnName("MINPL");
    pplMINPL.setSqlType(2);
    pplMINPL.setDefault("1250");
    pplMINOSDOP.setCaption("Minimalna osnovica za doprinose");
    pplMINOSDOP.setColumnName("MINOSDOP");
    pplMINOSDOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pplMINOSDOP.setPrecision(17);
    pplMINOSDOP.setScale(2);
    pplMINOSDOP.setDisplayMask("###,###,##0.00");
    pplMINOSDOP.setDefault("0");
    pplMINOSDOP.setTableName("PARAMETRIPL");
    pplMINOSDOP.setServerColumnName("MINOSDOP");
    pplMINOSDOP.setSqlType(2);
    pplMINOSDOP.setDefault("1800");
    pplOSNPOR1.setCaption("Osnovica za prvi porez");
    pplOSNPOR1.setColumnName("OSNPOR1");
    pplOSNPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pplOSNPOR1.setPrecision(17);
    pplOSNPOR1.setScale(2);
    pplOSNPOR1.setDisplayMask("###,###,##0.00");
    pplOSNPOR1.setDefault("0");
    pplOSNPOR1.setTableName("PARAMETRIPL");
    pplOSNPOR1.setServerColumnName("OSNPOR1");
    pplOSNPOR1.setSqlType(2);
    pplOSNPOR1.setDefault("2500");
    pplOSNPOR2.setCaption("Osnovica za drugi porez");
    pplOSNPOR2.setColumnName("OSNPOR2");
    pplOSNPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pplOSNPOR2.setPrecision(17);
    pplOSNPOR2.setScale(2);
    pplOSNPOR2.setDisplayMask("###,###,##0.00");
    pplOSNPOR2.setDefault("0");
    pplOSNPOR2.setTableName("PARAMETRIPL");
    pplOSNPOR2.setServerColumnName("OSNPOR2");
    pplOSNPOR2.setSqlType(2);
    pplOSNPOR2.setDefault("6250");
    pplOSNPOR3.setCaption("Osnovica za tre\u0107i porez");
    pplOSNPOR3.setColumnName("OSNPOR3");
    pplOSNPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pplOSNPOR3.setPrecision(17);
    pplOSNPOR3.setScale(2);
    pplOSNPOR3.setDisplayMask("###,###,##0.00");
    pplOSNPOR3.setDefault("0");
    pplOSNPOR3.setTableName("PARAMETRIPL");
    pplOSNPOR3.setServerColumnName("OSNPOR3");
    pplOSNPOR3.setSqlType(2);
    pplOSNPOR3.setDefault("0");
    pplOSNPOR4.setCaption("Osnovica za \u010Detvrti porez");
    pplOSNPOR4.setColumnName("OSNPOR4");
    pplOSNPOR4.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pplOSNPOR4.setPrecision(17);
    pplOSNPOR4.setScale(2);
    pplOSNPOR4.setDisplayMask("###,###,##0.00");
    pplOSNPOR4.setDefault("0");
    pplOSNPOR4.setTableName("PARAMETRIPL");
    pplOSNPOR4.setServerColumnName("OSNPOR4");
    pplOSNPOR4.setSqlType(2);
    pplOSNPOR4.setDefault("0");
    pplOSNPOR5.setCaption("Osnovica za peti porez");
    pplOSNPOR5.setColumnName("OSNPOR5");
    pplOSNPOR5.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pplOSNPOR5.setPrecision(17);
    pplOSNPOR5.setScale(2);
    pplOSNPOR5.setDisplayMask("###,###,##0.00");
    pplOSNPOR5.setDefault("0");
    pplOSNPOR5.setTableName("PARAMETRIPL");
    pplOSNPOR5.setServerColumnName("OSNPOR5");
    pplOSNPOR5.setSqlType(2);
    pplOSNPOR5.setDefault("0");
    pplPARAMETRI.setCaption("Parametri");
    pplPARAMETRI.setColumnName("PARAMETRI");
    pplPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    pplPARAMETRI.setPrecision(20);
    pplPARAMETRI.setTableName("PARAMETRIPL");
    pplPARAMETRI.setServerColumnName("PARAMETRI");
    pplPARAMETRI.setSqlType(1);
    pplPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ppl.setResolver(dm.getQresolver());
    ppl.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Parametripl", null, true, Load.ALL));
 setColumns(new Column[] {pplCPARPL, pplMINPL, pplMINOSDOP, pplOSNPOR1, pplOSNPOR2, pplOSNPOR3, pplOSNPOR4, pplOSNPOR5, pplPARAMETRI});
  }

  public void setall() {

    ddl.create("Parametripl")
       .addShort("cparpl", 1, true)
       .addFloat("minpl", 17, 2)
       .addFloat("minosdop", 17, 2)
       .addFloat("osnpor1", 17, 2)
       .addFloat("osnpor2", 17, 2)
       .addFloat("osnpor3", 17, 2)
       .addFloat("osnpor4", 17, 2)
       .addFloat("osnpor5", 17, 2)
       .addChar("parametri", 20)
       .addPrimaryKey("cparpl");


    Naziv = "Parametripl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
