/****license*****************************************************************
**   file: Serbr.java
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



public class Serbr extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Serbr Serbrclass;

  QueryDataSet serbr = new raDataSet();

  Column serbrLOKK = new Column();
  Column serbrAKTIV = new Column();
  Column serbrCSERBR = new Column();
  Column serbrCART = new Column();
  Column serbrCSKL = new Column();
  Column serbrVRDOK = new Column();
  Column serbrGOD = new Column();
  Column serbrBRDOK = new Column();
  Column serbrRBR = new Column();
  Column serbrCSKLIZ = new Column();
  Column serbrVRDOKIZ = new Column();
  Column serbrGODIZ = new Column();
  Column serbrBRDOKIZ = new Column();
  Column serbrRBRIZ = new Column();

  public static Serbr getDataModule() {
    if (Serbrclass == null) {
      Serbrclass = new Serbr();
    }
    return Serbrclass;
  }

  public QueryDataSet getQueryDataSet() {
    return serbr;
  }

  public Serbr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    serbrLOKK.setCaption("Status zauzetosti");
    serbrLOKK.setColumnName("LOKK");
    serbrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrLOKK.setPrecision(1);
    serbrLOKK.setTableName("SERBR");
    serbrLOKK.setServerColumnName("LOKK");
    serbrLOKK.setSqlType(1);
    serbrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    serbrLOKK.setDefault("N");
    serbrAKTIV.setCaption("Aktivan - neaktivan");
    serbrAKTIV.setColumnName("AKTIV");
    serbrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrAKTIV.setPrecision(1);
    serbrAKTIV.setTableName("SERBR");
    serbrAKTIV.setServerColumnName("AKTIV");
    serbrAKTIV.setSqlType(1);
    serbrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    serbrAKTIV.setDefault("D");
    serbrCSERBR.setCaption("Serijski broj");
    serbrCSERBR.setColumnName("CSERBR");
    serbrCSERBR.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrCSERBR.setPrecision(30);
    serbrCSERBR.setRowId(true);
    serbrCSERBR.setTableName("SERBR");
    serbrCSERBR.setServerColumnName("CSERBR");
    serbrCSERBR.setSqlType(1);
    serbrCSERBR.setWidth(30);
    serbrCART.setCaption("Artikl");
    serbrCART.setColumnName("CART");
    serbrCART.setDataType(com.borland.dx.dataset.Variant.INT);
    serbrCART.setPrecision(6);
    serbrCART.setRowId(true);
    serbrCART.setTableName("SERBR");
    serbrCART.setServerColumnName("CART");
    serbrCART.setSqlType(4);
    serbrCART.setWidth(6);
    serbrCSKL.setCaption("Skladište");
    serbrCSKL.setColumnName("CSKL");
    serbrCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrCSKL.setPrecision(12);
    serbrCSKL.setTableName("SERBR");
    serbrCSKL.setServerColumnName("CSKL");
    serbrCSKL.setSqlType(1);
    serbrVRDOK.setCaption("VD");
    serbrVRDOK.setColumnName("VRDOK");
    serbrVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrVRDOK.setPrecision(3);
    serbrVRDOK.setTableName("SERBR");
    serbrVRDOK.setServerColumnName("VRDOK");
    serbrVRDOK.setSqlType(1);
    serbrGOD.setCaption("Godina");
    serbrGOD.setColumnName("GOD");
    serbrGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrGOD.setPrecision(4);
    serbrGOD.setTableName("SERBR");
    serbrGOD.setServerColumnName("GOD");
    serbrGOD.setSqlType(1);
    serbrBRDOK.setCaption("Broj");
    serbrBRDOK.setColumnName("BRDOK");
    serbrBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    serbrBRDOK.setPrecision(6);
    serbrBRDOK.setTableName("SERBR");
    serbrBRDOK.setServerColumnName("BRDOK");
    serbrBRDOK.setSqlType(4);
    serbrBRDOK.setWidth(6);
    serbrRBR.setCaption("Rbr");
    serbrRBR.setColumnName("RBR");
    serbrRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    serbrRBR.setPrecision(4);
    serbrRBR.setTableName("SERBR");
    serbrRBR.setServerColumnName("RBR");
    serbrRBR.setSqlType(5);
    serbrRBR.setWidth(4);
    serbrCSKLIZ.setCaption("Izlazno skladište");
    serbrCSKLIZ.setColumnName("CSKLIZ");
    serbrCSKLIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrCSKLIZ.setPrecision(12);
    serbrCSKLIZ.setTableName("SERBR");
    serbrCSKLIZ.setServerColumnName("CSKLIZ");
    serbrCSKLIZ.setSqlType(1);
    serbrVRDOKIZ.setCaption("VD iz.");
    serbrVRDOKIZ.setColumnName("VRDOKIZ");
    serbrVRDOKIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrVRDOKIZ.setPrecision(3);
    serbrVRDOKIZ.setTableName("SERBR");
    serbrVRDOKIZ.setServerColumnName("VRDOKIZ");
    serbrVRDOKIZ.setSqlType(1);
    serbrGODIZ.setCaption("Godina iz.");
    serbrGODIZ.setColumnName("GODIZ");
    serbrGODIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    serbrGODIZ.setPrecision(4);
    serbrGODIZ.setTableName("SERBR");
    serbrGODIZ.setServerColumnName("GODIZ");
    serbrGODIZ.setSqlType(1);
    serbrBRDOKIZ.setCaption("Broj iz.");
    serbrBRDOKIZ.setColumnName("BRDOKIZ");
    serbrBRDOKIZ.setDataType(com.borland.dx.dataset.Variant.INT);
    serbrBRDOKIZ.setPrecision(6);
    serbrBRDOKIZ.setTableName("SERBR");
    serbrBRDOKIZ.setServerColumnName("BRDOKIZ");
    serbrBRDOKIZ.setSqlType(4);
    serbrBRDOKIZ.setWidth(6);
    serbrRBRIZ.setCaption("Rbr iz.");
    serbrRBRIZ.setColumnName("RBRIZ");
    serbrRBRIZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    serbrRBRIZ.setPrecision(4);
    serbrRBRIZ.setTableName("SERBR");
    serbrRBRIZ.setServerColumnName("RBRIZ");
    serbrRBRIZ.setSqlType(5);
    serbrRBRIZ.setWidth(4);
    serbr.setResolver(dm.getQresolver());
    serbr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Serbr", null, true, Load.ALL));
 setColumns(new Column[] {serbrLOKK, serbrAKTIV, serbrCSERBR, serbrCART, serbrCSKL, serbrVRDOK, serbrGOD, serbrBRDOK, serbrRBR, serbrCSKLIZ,
        serbrVRDOKIZ, serbrGODIZ, serbrBRDOKIZ, serbrRBRIZ});
  }

  public void setall() {

    ddl.create("Serbr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cserbr", 30, true)
       .addInteger("cart", 6, true)
       .addChar("cskl", 12)
       .addChar("vrdok", 3)
       .addChar("god", 4)
       .addInteger("brdok", 6)
       .addShort("rbr", 4)
       .addChar("cskliz", 12)
       .addChar("vrdokiz", 3)
       .addChar("godiz", 4)
       .addInteger("brdokiz", 6)
       .addShort("rbriz", 4)
       .addPrimaryKey("cserbr,cart");


    Naziv = "Serbr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cserbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
