/****license*****************************************************************
**   file: Temelj.java
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

public class Temelj extends KreirDrop implements DataModule {

  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Temelj Temeljclass;
  dM dm  = dM.getDataModule();
  QueryDataSet temelj = new QueryDataSet();
  Column temeljLOKK = new Column();
  Column temeljAKTIV = new Column();
  Column temeljCSKL = new Column();
  Column temeljUI = new Column();
  Column temeljBROJKONTA = new Column();
  Column temeljIZNOSDUG = new Column();
  Column temeljIZNOSPOT = new Column();

  public static Temelj getDataModule() {
    if (Temeljclass == null) {
      Temeljclass = new Temelj();
    }
    return Temeljclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return temelj;
  }
  public Temelj() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    temeljIZNOSPOT.setCaption("Iznos potraživanja");
    temeljIZNOSPOT.setColumnName("IZNOSPOT");
    temeljIZNOSPOT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    temeljIZNOSPOT.setDisplayMask("###,###,##0.00");
    temeljIZNOSPOT.setDefault("0");
    temeljIZNOSPOT.setPrecision(15);
    temeljIZNOSPOT.setScale(2);
    temeljIZNOSPOT.setTableName("TEMELJ");
    temeljIZNOSPOT.setWidth(14);
    temeljIZNOSPOT.setSqlType(2);
    temeljIZNOSPOT.setServerColumnName("IZNOSPOT");

    temeljIZNOSDUG.setCaption("Iznos dugovanja");
    temeljIZNOSDUG.setColumnName("IZNOSDUG");
    temeljIZNOSDUG.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    temeljIZNOSDUG.setDisplayMask("###,###,##0.00");
    temeljIZNOSDUG.setDefault("0");
    temeljIZNOSDUG.setPrecision(15);
    temeljIZNOSDUG.setScale(2);
    temeljIZNOSDUG.setTableName("TEMELJ");
    temeljIZNOSDUG.setWidth(14);
    temeljIZNOSDUG.setSqlType(2);
    temeljIZNOSDUG.setServerColumnName("IZNOSDUG");
    temeljBROJKONTA.setCaption("Konto");
    temeljBROJKONTA.setColumnName("BROJKONTA");
    temeljBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    temeljBROJKONTA.setPrecision(8);
    temeljBROJKONTA.setRowId(true);
    temeljBROJKONTA.setTableName("TEMELJ");
    temeljBROJKONTA.setServerColumnName("BROJKONTA");
    temeljBROJKONTA.setSqlType(1);
    temeljBROJKONTA.setWidth(8);
    temeljUI.setCaption("Ulaz/izlaz");
    temeljUI.setColumnName("UI");
    temeljUI.setDataType(com.borland.dx.dataset.Variant.STRING);
    temeljUI.setPrecision(1);
    temeljUI.setTableName("TEMELJ");
    temeljUI.setServerColumnName("UI");
    temeljUI.setSqlType(1);
    temeljCSKL.setCaption("Skladi\u0161te");
    temeljCSKL.setColumnName("CSKL");
    temeljCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    temeljCSKL.setPrecision(12);
    temeljCSKL.setRowId(true);
    temeljCSKL.setTableName("TEMELJ");
    temeljCSKL.setServerColumnName("CSKL");
    temeljCSKL.setSqlType(1);
    temeljAKTIV.setColumnName("AKTIV");
    temeljAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    temeljAKTIV.setDefault("D");
    temeljAKTIV.setPrecision(1);
    temeljAKTIV.setTableName("TEMELJ");
    temeljAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    temeljAKTIV.setServerColumnName("AKTIV");
    temeljAKTIV.setSqlType(1);
    temeljLOKK.setColumnName("LOKK");
    temeljLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    temeljLOKK.setDefault("N");
    temeljLOKK.setPrecision(1);
    temeljLOKK.setTableName("TEMELJ");
    temeljLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    temeljLOKK.setServerColumnName("LOKK");
    temeljLOKK.setSqlType(1);

    temelj.setResolver(dm.getQresolver());
    temelj.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM TEMELJ", null, true, Load.ALL));
 setColumns(new Column[] {temeljLOKK, temeljAKTIV, temeljCSKL, temeljUI, temeljBROJKONTA, temeljIZNOSDUG, temeljIZNOSPOT});
  }

   public void setall(){

    /*SqlDefTabela = "create table Temelj " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " +
      "cagent numeric(6,0) not null,"+
      "nazagent char(50) CHARACTER SET WIN1250," +
      "zaporka char(30) CHARACTER SET WIN1250 , " +
      "Primary Key (cagent))" ;

    Naziv="Temelj";

    NaziviIdx=new String[]{"ilokkagent","iaktivagent","icagent"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Temelj (lokk)",
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Temelj (aktiv)",
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Temelj (cagent)" };
      */
    ddl.create("temelj")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("ui", 1)
       .addChar("brojkonta", 8, true)
       .addFloat("iznosdug", 17, 2)
       .addFloat("iznospot", 17, 2)
       .addPrimaryKey("cskl,brojkonta");

    Naziv = "Temelj";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

   }
}