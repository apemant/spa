/****license*****************************************************************
**   file: SEQ.java
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
import java.util.ResourceBundle;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class SEQ extends KreirDrop implements DataModule {

  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static SEQ SEQclass;
  dM dm  = dM.getDataModule();
  QueryDataSet seq = new QueryDataSet();

  Column seqLOKK = new Column();
  Column seqOPIS = new Column();
  Column seqKNJIG = new Column();
  Column seqGOD = new Column();
  Column seqAPP = new Column();
  Column seqBROJ = new Column();

  public static SEQ getDataModule() {
    if (SEQclass == null) {
      SEQclass = new SEQ();
    }
    return SEQclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return seq;
  }
  public SEQ() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

    seqLOKK.setColumnName("LOKK");
    seqLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    seqLOKK.setDefault("N");
    seqLOKK.setPrecision(1);
    seqLOKK.setTableName("SEQ");
    seqLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    seqLOKK.setSqlType(1);
    seqLOKK.setServerColumnName("LOKK");

    seqOPIS.setCaption(dmRes.getString("opis"));
    seqOPIS.setColumnName("OPIS");
    seqOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    seqOPIS.setPrecision(30);
    seqOPIS.setRowId(true);
    seqOPIS.setTableName("SEQ");
    seqOPIS.setSqlType(1);
    seqOPIS.setServerColumnName("OPIS");

    seqKNJIG.setCaption("Knjigovodstvo");
    seqKNJIG.setColumnName("KNJIG");
    seqKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    seqKNJIG.setPrecision(12);
    seqKNJIG.setTableName("SEQ");
    seqKNJIG.setSqlType(1);
    seqKNJIG.setServerColumnName("KNJIG");

    seqGOD.setCaption("Godina");
    seqGOD.setColumnName("GOD");
    seqGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    seqGOD.setPrecision(4);
    seqGOD.setTableName("SEQ");
    seqGOD.setSqlType(1);
    seqGOD.setServerColumnName("GOD");

    seqAPP.setCaption("Aplikacija");
    seqAPP.setColumnName("APP");
    seqAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    seqAPP.setPrecision(10);
    seqAPP.setTableName("SEQ");
    seqAPP.setSqlType(1);
    seqAPP.setServerColumnName("APP");

    seqBROJ.setCaption(dmRes.getString("seqBROJ_caption"));
    seqBROJ.setColumnName("BROJ");
    seqBROJ.setDataType(com.borland.dx.dataset.Variant.DOUBLE);
    seqBROJ.setScale(0);
    seqBROJ.setTableName("SEQ");
    seqBROJ.setServerColumnName("BROJ");
    seqBROJ.setSqlType(8);
    seq.setResolver(dm.getQresolver());
    seq.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from seq", null, true, Load.ALL));
 setColumns(new Column[] {seqLOKK, seqOPIS, seqKNJIG, seqGOD, seqAPP, seqBROJ});
  }

  public void setall(){

    /*SqlDefTabela = "create table SEQ " +
      "(opis char(30) CHARACTER SET WIN1250 not null , " + //Status zauzetosti
      "broj numeric(10,0)," +
      "Primary Key (opis))" ;
*/
    ddl.create("SEQ")
       .addChar("lokk", 1, "N")
       .addChar("opis", 30, true)
       .addChar("knjig", 12)
       .addChar("god", 4)
       .addChar("app", 10)
       .addFloat("broj", 10, 0)
       .addPrimaryKey("opis");

    Naziv="SEQ";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    NaziviIdx=new String[]{"iopisSEQ"};

    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+NaziviIdx[0] +" on SEQ (opis)"} ;
*/
   }
}