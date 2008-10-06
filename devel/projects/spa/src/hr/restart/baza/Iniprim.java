/****license*****************************************************************
**   file: Iniprim.java
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



public class Iniprim extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Iniprim Iniprimclass;

  QueryDataSet iniprim = new raDataSet();
  QueryDataSet iniprimaktiv = new raDataSet();

  Column iniprimLOKK = new Column();
  Column iniprimAKTIV = new Column();
  Column iniprimCVRP = new Column();
  Column iniprimRBR = new Column();
  Column iniprimSATI = new Column();
  Column iniprimKOEF = new Column();
  Column iniprimIZNOS = new Column();
  Column iniprimIRAZOD = new Column();
  Column iniprimIRAZDO = new Column();
  Column iniprimSFOND = new Column();
  Column iniprimCNIVO = new Column();

  public static Iniprim getDataModule() {
    if (Iniprimclass == null) {
      Iniprimclass = new Iniprim();
    }
    return Iniprimclass;
  }

  public QueryDataSet getQueryDataSet() {
    return iniprim;
  }

  public QueryDataSet getAktiv() {
    return iniprimaktiv;
  }

  public Iniprim() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    iniprimLOKK.setCaption("Status zauzetosti");
    iniprimLOKK.setColumnName("LOKK");
    iniprimLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    iniprimLOKK.setPrecision(1);
    iniprimLOKK.setTableName("INIPRIM");
    iniprimLOKK.setServerColumnName("LOKK");
    iniprimLOKK.setSqlType(1);
    iniprimLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    iniprimLOKK.setDefault("N");
    iniprimAKTIV.setCaption("Aktivan - neaktivan");
    iniprimAKTIV.setColumnName("AKTIV");
    iniprimAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    iniprimAKTIV.setPrecision(1);
    iniprimAKTIV.setTableName("INIPRIM");
    iniprimAKTIV.setServerColumnName("AKTIV");
    iniprimAKTIV.setSqlType(1);
    iniprimAKTIV.setDefault("D");
    iniprimCVRP.setCaption("Oznaka");
    iniprimCVRP.setColumnName("CVRP");
    iniprimCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    iniprimCVRP.setPrecision(3);
    iniprimCVRP.setRowId(true);
    iniprimCVRP.setTableName("INIPRIM");
    iniprimCVRP.setServerColumnName("CVRP");
    iniprimCVRP.setSqlType(5);
    iniprimCVRP.setWidth(3);
    iniprimRBR.setCaption("Redni broj");
    iniprimRBR.setColumnName("RBR");
    iniprimRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    iniprimRBR.setPrecision(3);
    iniprimRBR.setRowId(true);
    iniprimRBR.setTableName("INIPRIM");
    iniprimRBR.setServerColumnName("RBR");
    iniprimRBR.setSqlType(5);
    iniprimRBR.setWidth(3);
    iniprimRBR.setDefault("1");
    iniprimSATI.setCaption("Sati");
    iniprimSATI.setColumnName("SATI");
    iniprimSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    iniprimSATI.setPrecision(17);
    iniprimSATI.setScale(2);
    iniprimSATI.setDisplayMask("###,###,##0.00");
    iniprimSATI.setDefault("0");
    iniprimSATI.setTableName("INIPRIM");
    iniprimSATI.setServerColumnName("SATI");
    iniprimSATI.setSqlType(2);
    iniprimSATI.setDefault("0");
    iniprimKOEF.setCaption("Koeficijent");
    iniprimKOEF.setColumnName("KOEF");
    iniprimKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    iniprimKOEF.setPrecision(17);
    iniprimKOEF.setScale(2);
    iniprimKOEF.setDisplayMask("###,###,##0.00");
    iniprimKOEF.setDefault("0");
    iniprimKOEF.setTableName("INIPRIM");
    iniprimKOEF.setServerColumnName("KOEF");
    iniprimKOEF.setSqlType(2);
    iniprimKOEF.setDefault("0");
    iniprimIZNOS.setCaption("Iznos");
    iniprimIZNOS.setColumnName("IZNOS");
    iniprimIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    iniprimIZNOS.setPrecision(17);
    iniprimIZNOS.setScale(2);
    iniprimIZNOS.setDisplayMask("###,###,##0.00");
    iniprimIZNOS.setDefault("0");
    iniprimIZNOS.setTableName("INIPRIM");
    iniprimIZNOS.setServerColumnName("IZNOS");
    iniprimIZNOS.setSqlType(2);
    iniprimIZNOS.setDefault("0");
    iniprimIRAZOD.setCaption("Ispla\u0107eno za razdoblje od");
    iniprimIRAZOD.setColumnName("IRAZOD");
    iniprimIRAZOD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    iniprimIRAZOD.setPrecision(8);
    iniprimIRAZOD.setDisplayMask("dd-MM-yyyy");
//    iniprimIRAZOD.setEditMask("dd-MM-yyyy");
    iniprimIRAZOD.setTableName("INIPRIM");
    iniprimIRAZOD.setServerColumnName("IRAZOD");
    iniprimIRAZOD.setSqlType(93);
    iniprimIRAZOD.setWidth(10);
    iniprimIRAZDO.setCaption("Ispla\u0107eno za razdoblje do");
    iniprimIRAZDO.setColumnName("IRAZDO");
    iniprimIRAZDO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    iniprimIRAZDO.setPrecision(8);
    iniprimIRAZDO.setDisplayMask("dd-MM-yyyy");
//    iniprimIRAZDO.setEditMask("dd-MM-yyyy");
    iniprimIRAZDO.setTableName("INIPRIM");
    iniprimIRAZDO.setServerColumnName("IRAZDO");
    iniprimIRAZDO.setSqlType(93);
    iniprimIRAZDO.setWidth(10);
    iniprimSFOND.setCaption("Fond sati");
    iniprimSFOND.setColumnName("SFOND");
    iniprimSFOND.setDataType(com.borland.dx.dataset.Variant.STRING);
    iniprimSFOND.setPrecision(1);
    iniprimSFOND.setTableName("INIPRIM");
    iniprimSFOND.setServerColumnName("SFOND");
    iniprimSFOND.setSqlType(1);
    iniprimSFOND.setDefault("X");
    iniprimCNIVO.setCaption("Oznaka nivoa");
    iniprimCNIVO.setColumnName("CNIVO");
    iniprimCNIVO.setDataType(com.borland.dx.dataset.Variant.STRING);
    iniprimCNIVO.setPrecision(15);
    iniprimCNIVO.setTableName("INIPRIM");
    iniprimCNIVO.setServerColumnName("CNIVO");
    iniprimCNIVO.setSqlType(1);
    iniprim.setResolver(dm.getQresolver());
    iniprim.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Iniprim", null, true, Load.ALL));
 setColumns(new Column[] {iniprimLOKK, iniprimAKTIV, iniprimCVRP, iniprimRBR, iniprimSATI, iniprimKOEF, iniprimIZNOS, iniprimIRAZOD, iniprimIRAZDO,
        iniprimSFOND, iniprimCNIVO});

    createFilteredDataSet(iniprimaktiv, "aktiv='D'");
  }

  public void setall() {

    ddl.create("Iniprim")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cvrp", 3, true)
       .addShort("rbr", 3, true)
       .addFloat("sati", 17, 2)
       .addFloat("koef", 17, 2)
       .addFloat("iznos", 17, 2)
       .addDate("irazod")
       .addDate("irazdo")
       .addChar("sfond", 1, "X")
       .addChar("cnivo", 15)
       .addPrimaryKey("cvrp,rbr");


    Naziv = "Iniprim";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
