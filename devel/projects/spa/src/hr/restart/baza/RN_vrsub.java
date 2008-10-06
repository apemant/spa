/****license*****************************************************************
**   file: RN_vrsub.java
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



public class RN_vrsub extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static RN_vrsub RN_vrsubclass;

  QueryDataSet rnvsub = new raDataSet();
  QueryDataSet rnvsubunos = new raDataSet();

  Column rnvsubLOKK = new Column();
  Column rnvsubAKTIV = new Column();
  Column rnvsubCVRSUBJ = new Column();
  Column rnvsubNAZVRSUBJ = new Column();
  Column rnvsubNAZSERBR = new Column();
  Column rnvsubCPRIP = new Column();
  Column rnvsubNAZSIF = new Column();

  public static RN_vrsub getDataModule() {
    if (RN_vrsubclass == null) {
      RN_vrsubclass = new RN_vrsub();
    }
    return RN_vrsubclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rnvsub;
  }

  public QueryDataSet getUnos() {
    return rnvsubunos;
  }

  public RN_vrsub() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rnvsubLOKK.setCaption("Status zauzetosti");
    rnvsubLOKK.setColumnName("LOKK");
    rnvsubLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rnvsubLOKK.setPrecision(1);
    rnvsubLOKK.setTableName("RN_VRSUB");
    rnvsubLOKK.setServerColumnName("LOKK");
    rnvsubLOKK.setSqlType(1);
    rnvsubLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rnvsubLOKK.setDefault("N");
    rnvsubAKTIV.setCaption("Aktivan - neaktivan");
    rnvsubAKTIV.setColumnName("AKTIV");
    rnvsubAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rnvsubAKTIV.setPrecision(1);
    rnvsubAKTIV.setTableName("RN_VRSUB");
    rnvsubAKTIV.setServerColumnName("AKTIV");
    rnvsubAKTIV.setSqlType(1);
    rnvsubAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rnvsubAKTIV.setDefault("D");
    rnvsubCVRSUBJ.setCaption("Šifra");
    rnvsubCVRSUBJ.setColumnName("CVRSUBJ");
    rnvsubCVRSUBJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rnvsubCVRSUBJ.setPrecision(3);
    rnvsubCVRSUBJ.setRowId(true);
    rnvsubCVRSUBJ.setTableName("RN_VRSUB");
    rnvsubCVRSUBJ.setServerColumnName("CVRSUBJ");
    rnvsubCVRSUBJ.setSqlType(5);
    rnvsubCVRSUBJ.setWidth(3);
    rnvsubNAZVRSUBJ.setCaption("Naziv");
    rnvsubNAZVRSUBJ.setColumnName("NAZVRSUBJ");
    rnvsubNAZVRSUBJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    rnvsubNAZVRSUBJ.setPrecision(50);
    rnvsubNAZVRSUBJ.setTableName("RN_VRSUB");
    rnvsubNAZVRSUBJ.setServerColumnName("NAZVRSUBJ");
    rnvsubNAZVRSUBJ.setSqlType(1);
    rnvsubNAZVRSUBJ.setWidth(30);
    rnvsubNAZSERBR.setCaption("Tekst S/B");
    rnvsubNAZSERBR.setColumnName("NAZSERBR");
    rnvsubNAZSERBR.setDataType(com.borland.dx.dataset.Variant.STRING);
    rnvsubNAZSERBR.setPrecision(40);
    rnvsubNAZSERBR.setTableName("RN_VRSUB");
    rnvsubNAZSERBR.setServerColumnName("NAZSERBR");
    rnvsubNAZSERBR.setSqlType(1);
    rnvsubNAZSERBR.setWidth(30);
    rnvsubCPRIP.setCaption("Pripadnost");
    rnvsubCPRIP.setColumnName("CPRIP");
    rnvsubCPRIP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rnvsubCPRIP.setPrecision(3);
    rnvsubCPRIP.setTableName("RN_VRSUB");
    rnvsubCPRIP.setServerColumnName("CPRIP");
    rnvsubCPRIP.setSqlType(5);
    rnvsubCPRIP.setWidth(3);
    rnvsubNAZSIF.setCaption("Tekst šifre");
    rnvsubNAZSIF.setColumnName("NAZSIF");
    rnvsubNAZSIF.setDataType(com.borland.dx.dataset.Variant.STRING);
    rnvsubNAZSIF.setPrecision(40);
    rnvsubNAZSIF.setTableName("RN_VRSUB");
    rnvsubNAZSIF.setServerColumnName("NAZSIF");
    rnvsubNAZSIF.setSqlType(1);
    rnvsubNAZSIF.setWidth(30);
    rnvsub.setResolver(dm.getQresolver());
    rnvsub.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from RN_vrsub", null, true, Load.ALL));
    setColumns(new Column[] {rnvsubLOKK, rnvsubAKTIV, rnvsubCVRSUBJ, rnvsubNAZVRSUBJ, rnvsubNAZSERBR, rnvsubCPRIP, rnvsubNAZSIF});

    createFilteredDataSet(rnvsubunos, "");
  }

  public void setall() {

    ddl.create("RN_vrsub")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cvrsubj", 3, true)
       .addChar("nazvrsubj", 50)
       .addChar("nazserbr", 40)
       .addShort("cprip", 3)
       .addChar("nazsif", 40)
       .addPrimaryKey("cvrsubj");


    Naziv = "RN_vrsub";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
