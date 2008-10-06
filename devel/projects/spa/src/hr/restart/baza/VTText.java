/****license*****************************************************************
**   file: VTText.java
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



public class VTText extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static VTText VTTextclass;

  QueryDataSet vtt = new raDataSet();

  Column vttCKEY = new Column();
  Column vttTEXTFAK = new Column();

  public static VTText getDataModule() {
    if (VTTextclass == null) {
      VTTextclass = new VTText();
    }
    return VTTextclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vtt;
  }

  public VTText() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vttCKEY.setCaption("Klju\u010D izvornog dokumenta");
    vttCKEY.setColumnName("CKEY");
    vttCKEY.setDataType(com.borland.dx.dataset.Variant.STRING);
    vttCKEY.setPrecision(60);
    vttCKEY.setRowId(true);
    vttCKEY.setTableName("VTTEXT");
    vttCKEY.setServerColumnName("CKEY");
    vttCKEY.setSqlType(1);
    vttCKEY.setWidth(30);
    vttTEXTFAK.setCaption("Tekst stavke");
    vttTEXTFAK.setColumnName("TEXTFAK");
    vttTEXTFAK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vttTEXTFAK.setPrecision(2000);
    vttTEXTFAK.setTableName("VTTEXT");
    vttTEXTFAK.setServerColumnName("TEXTFAK");
    vttTEXTFAK.setSqlType(1);
    vttTEXTFAK.setWidth(30);
    vtt.setResolver(dm.getQresolver());
    vtt.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from VTText", null, true, Load.ALL));
 setColumns(new Column[] {vttCKEY, vttTEXTFAK});
  }

  public void setall() {

    ddl.create("VTText")
       .addChar("ckey", 60, true)
       .addChar("textfak", 2000)
       .addPrimaryKey("ckey");


    Naziv = "VTText";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
