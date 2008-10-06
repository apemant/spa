/****license*****************************************************************
**   file: Vrart.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;



public class Vrart extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrart Vrartclass;

  QueryDataSet vrart = new raDataSet();

  /*Column vrartLOKK = new Column();
  Column vrartAKTIV = new Column();
  Column vrartCVRART = new Column();
  Column vrartNAZVRART = new Column();
  Column vrartKONTOPOT = new Column();
  Column vrartKONTOPRI = new Column();
  Column vrartKONTOPDV = new Column();
  Column vrartKONTOPNP = new Column();*/

  public static Vrart getDataModule() {
    if (Vrartclass == null) {
      Vrartclass = new Vrart();
    }
    return Vrartclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vrart;
  }

  public Vrart() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    initModule();
    /*
    vrartLOKK.setCaption("Status zauzetosti");
    vrartLOKK.setColumnName("LOKK");
    vrartLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrartLOKK.setPrecision(1);
    vrartLOKK.setTableName("VRART");
    vrartLOKK.setServerColumnName("LOKK");
    vrartLOKK.setSqlType(1);
    vrartLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrartLOKK.setDefault("N");
    vrartAKTIV.setCaption("Aktivan - neaktivan");
    vrartAKTIV.setColumnName("AKTIV");
    vrartAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrartAKTIV.setPrecision(1);
    vrartAKTIV.setTableName("VRART");
    vrartAKTIV.setServerColumnName("AKTIV");
    vrartAKTIV.setSqlType(1);
    vrartAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrartAKTIV.setDefault("D");
    vrartCVRART.setCaption("Šifra");
    vrartCVRART.setColumnName("CVRART");
    vrartCVRART.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrartCVRART.setPrecision(1);
    vrartCVRART.setRowId(true);
    vrartCVRART.setTableName("VRART");
    vrartCVRART.setServerColumnName("CVRART");
    vrartCVRART.setSqlType(1);
    vrartNAZVRART.setCaption("Naziv");
    vrartNAZVRART.setColumnName("NAZVRART");
    vrartNAZVRART.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrartNAZVRART.setPrecision(30);
    vrartNAZVRART.setTableName("VRART");
    vrartNAZVRART.setServerColumnName("NAZVRART");
    vrartNAZVRART.setSqlType(1);
    vrartKONTOPOT.setCaption("Konto za potraživanje");
    vrartKONTOPOT.setColumnName("KONTOPOT");
    vrartKONTOPOT.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrartKONTOPOT.setPrecision(8);
    vrartKONTOPOT.setTableName("VRART");
    vrartKONTOPOT.setServerColumnName("KONTOPOT");
    vrartKONTOPOT.setSqlType(1);
    vrartKONTOPOT.setVisible(com.borland.jb.util.TriStateProperty.FALSE);

    vrartKONTOPRI.setCaption("Konto za prihod");
    vrartKONTOPRI.setColumnName("KONTOPRI");
    vrartKONTOPRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrartKONTOPRI.setPrecision(8);
    vrartKONTOPRI.setTableName("VRART");
    vrartKONTOPRI.setServerColumnName("KONTOPRI");
    vrartKONTOPRI.setSqlType(1);
    vrartKONTOPDV.setCaption("Konto za PDV");
    vrartKONTOPDV.setColumnName("KONTOPDV");
    vrartKONTOPDV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrartKONTOPDV.setPrecision(8);
    vrartKONTOPDV.setTableName("VRART");
    vrartKONTOPDV.setServerColumnName("KONTOPDV");
    vrartKONTOPDV.setSqlType(1);
    vrartKONTOPNP.setCaption("Konto za PNP");
    vrartKONTOPNP.setColumnName("KONTOPNP");
    vrartKONTOPNP.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrartKONTOPNP.setPrecision(8);
    vrartKONTOPNP.setTableName("VRART");
    vrartKONTOPNP.setServerColumnName("KONTOPNP");
    vrartKONTOPNP.setSqlType(1);
    vrartKONTOPNP.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrart.setResolver(dm.getQresolver());
    vrart.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Vrart", null, true, Load.ALL));
 setColumns(new Column[] {vrartLOKK, vrartAKTIV, vrartCVRART, vrartNAZVRART, vrartKONTOPOT, vrartKONTOPRI, vrartKONTOPDV, vrartKONTOPNP});*/
  }

  /*public void setall() {

    ddl.create("Vrart")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvrart", 1, true)
       .addChar("nazvrart", 30)
       .addChar("kontopot", 8)
       .addChar("kontopri", 8)
       .addChar("kontopdv", 8)
       .addChar("kontopnp", 8)
       .addPrimaryKey("cvrart");


    Naziv = "Vrart";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
