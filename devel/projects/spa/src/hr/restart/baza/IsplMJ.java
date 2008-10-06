/****license*****************************************************************
**   file: IsplMJ.java
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



public class IsplMJ extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static IsplMJ IsplMJclass;

  QueryDataSet isplmj = new raDataSet();

  Column isplmjLOKK = new Column();
  Column isplmjAKTIV = new Column();
  Column isplmjCISPLMJ = new Column();
  Column isplmjNAZIV = new Column();
  Column isplmjCBANKE = new Column();
  Column isplmjNOVSPEC = new Column();
  Column isplmjBANSPEC = new Column();
  Column isplmjTIPISPLMJ = new Column();
  Column isplmjTIPFILE = new Column();
  Column isplmjPARAMETRI = new Column();

  public static IsplMJ getDataModule() {
    if (IsplMJclass == null) {
      IsplMJclass = new IsplMJ();
    }
    return IsplMJclass;
  }

  public QueryDataSet getQueryDataSet() {
    return isplmj;
  }

  public IsplMJ() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    isplmjLOKK.setCaption("Status zauzetosti");
    isplmjLOKK.setColumnName("LOKK");
    isplmjLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    isplmjLOKK.setPrecision(1);
    isplmjLOKK.setTableName("ISPLMJ");
    isplmjLOKK.setServerColumnName("LOKK");
    isplmjLOKK.setSqlType(1);
    isplmjLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    isplmjLOKK.setDefault("N");
    isplmjAKTIV.setCaption("Aktivan - neaktivan");
    isplmjAKTIV.setColumnName("AKTIV");
    isplmjAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    isplmjAKTIV.setPrecision(1);
    isplmjAKTIV.setTableName("ISPLMJ");
    isplmjAKTIV.setServerColumnName("AKTIV");
    isplmjAKTIV.setSqlType(1);
    isplmjAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    isplmjAKTIV.setDefault("D");
    isplmjCISPLMJ.setCaption("Oznaka");
    isplmjCISPLMJ.setColumnName("CISPLMJ");
    isplmjCISPLMJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    isplmjCISPLMJ.setPrecision(2);
    isplmjCISPLMJ.setRowId(true);
    isplmjCISPLMJ.setTableName("ISPLMJ");
    isplmjCISPLMJ.setServerColumnName("CISPLMJ");
    isplmjCISPLMJ.setSqlType(5);
    isplmjCISPLMJ.setWidth(2);
    isplmjNAZIV.setCaption("Naziv");
    isplmjNAZIV.setColumnName("NAZIV");
    isplmjNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    isplmjNAZIV.setPrecision(30);
    isplmjNAZIV.setTableName("ISPLMJ");
    isplmjNAZIV.setServerColumnName("NAZIV");
    isplmjNAZIV.setSqlType(1);
    isplmjCBANKE.setCaption("Oznaka banke");
    isplmjCBANKE.setColumnName("CBANKE");
    isplmjCBANKE.setDataType(com.borland.dx.dataset.Variant.INT);
    isplmjCBANKE.setPrecision(6);
    isplmjCBANKE.setTableName("ISPLMJ");
    isplmjCBANKE.setServerColumnName("CBANKE");
    isplmjCBANKE.setSqlType(4);
    isplmjCBANKE.setWidth(6);
    isplmjNOVSPEC.setCaption("Specifikacija nov\u010Danica");
    isplmjNOVSPEC.setColumnName("NOVSPEC");
    isplmjNOVSPEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    isplmjNOVSPEC.setPrecision(1);
    isplmjNOVSPEC.setTableName("ISPLMJ");
    isplmjNOVSPEC.setServerColumnName("NOVSPEC");
    isplmjNOVSPEC.setSqlType(1);
    isplmjNOVSPEC.setDefault("N");
    isplmjBANSPEC.setCaption("Specifikacija za banku");
    isplmjBANSPEC.setColumnName("BANSPEC");
    isplmjBANSPEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    isplmjBANSPEC.setPrecision(1);
    isplmjBANSPEC.setTableName("ISPLMJ");
    isplmjBANSPEC.setServerColumnName("BANSPEC");
    isplmjBANSPEC.setSqlType(1);
    isplmjBANSPEC.setDefault("D");
    isplmjTIPISPLMJ.setCaption("Tip isplatnog mjesta");
    isplmjTIPISPLMJ.setColumnName("TIPISPLMJ");
    isplmjTIPISPLMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    isplmjTIPISPLMJ.setPrecision(1);
    isplmjTIPISPLMJ.setTableName("ISPLMJ");
    isplmjTIPISPLMJ.setServerColumnName("TIPISPLMJ");
    isplmjTIPISPLMJ.setSqlType(1);
    isplmjTIPISPLMJ.setDefault("G");
    isplmjTIPFILE.setCaption("Tip datoteke");
    isplmjTIPFILE.setColumnName("TIPFILE");
    isplmjTIPFILE.setDataType(com.borland.dx.dataset.Variant.STRING);
    isplmjTIPFILE.setPrecision(1);
    isplmjTIPFILE.setTableName("ISPLMJ");
    isplmjTIPFILE.setServerColumnName("TIPFILE");
    isplmjTIPFILE.setSqlType(1);
    isplmjTIPFILE.setDefault("0");
    isplmjPARAMETRI.setCaption("Parametri");
    isplmjPARAMETRI.setColumnName("PARAMETRI");
    isplmjPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    isplmjPARAMETRI.setPrecision(20);
    isplmjPARAMETRI.setTableName("ISPLMJ");
    isplmjPARAMETRI.setServerColumnName("PARAMETRI");
    isplmjPARAMETRI.setSqlType(1);
    isplmjPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    isplmj.setResolver(dm.getQresolver());
    isplmj.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from IsplMJ", null, true, Load.ALL));
 setColumns(new Column[] {isplmjLOKK, isplmjAKTIV, isplmjCISPLMJ, isplmjNAZIV, isplmjCBANKE, isplmjNOVSPEC, isplmjBANSPEC, isplmjTIPISPLMJ,
        isplmjTIPFILE, isplmjPARAMETRI});
  }

  public void setall() {

    ddl.create("IsplMJ")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cisplmj", 2, true)
       .addChar("naziv", 30)
       .addInteger("cbanke", 6)
       .addChar("novspec", 1, "N")
       .addChar("banspec", 1, "D")
       .addChar("tipisplmj", 1, "G")
       .addChar("tipfile", 1, "0")
       .addChar("parametri", 20)
       .addPrimaryKey("cisplmj");


    Naziv = "IsplMJ";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
