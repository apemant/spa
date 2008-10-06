/****license*****************************************************************
**   file: Izvodi.java
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



public class Izvodi extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Izvodi Izvodiclass;

  QueryDataSet izvodi = new raDataSet();

  Column izvodiLOKK = new Column();
  Column izvodiAKTIV = new Column();
  Column izvodiKNJIG = new Column();  
  Column izvodiZIRO = new Column();
  Column izvodiBROJIZV = new Column();
  Column izvodiBROJSTAVKI = new Column();
  Column izvodiPRETHSTANJE = new Column();
  Column izvodiID = new Column();
  Column izvodiIP = new Column();
  Column izvodiNOVOSTANJE = new Column();
  Column izvodiDATUM = new Column();
  Column izvodiCNALOGA = new Column();
  Column izvodiSTATUS = new Column();
  Column izvodiGOD = new Column();

  public static Izvodi getDataModule() {
    if (Izvodiclass == null) {
      Izvodiclass = new Izvodi();
    }
    return Izvodiclass;
  }

  public QueryDataSet getQueryDataSet() {
    return izvodi;
  }

  public Izvodi() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    izvodiLOKK.setCaption("Status zauzetosti");
    izvodiLOKK.setColumnName("LOKK");
    izvodiLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    izvodiLOKK.setPrecision(1);
    izvodiLOKK.setTableName("IZVODI");
    izvodiLOKK.setServerColumnName("LOKK");
    izvodiLOKK.setSqlType(1);
    izvodiLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    izvodiLOKK.setDefault("N");
    izvodiAKTIV.setCaption("Aktivan - neaktivan");
    izvodiAKTIV.setColumnName("AKTIV");
    izvodiAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    izvodiAKTIV.setPrecision(1);
    izvodiAKTIV.setTableName("IZVODI");
    izvodiAKTIV.setServerColumnName("AKTIV");
    izvodiAKTIV.setSqlType(1);
    izvodiAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    izvodiAKTIV.setDefault("D");
    izvodiKNJIG.setCaption("Knjigovodstvo");
    izvodiKNJIG.setColumnName("KNJIG");
    izvodiKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    izvodiKNJIG.setPrecision(12);
    izvodiKNJIG.setRowId(true);
    izvodiKNJIG.setTableName("IZVODI");
    izvodiKNJIG.setServerColumnName("KNJIG");
    izvodiKNJIG.setSqlType(1);    
    izvodiZIRO.setCaption("Žiro ra\u010Dun");
    izvodiZIRO.setColumnName("ZIRO");
    izvodiZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    izvodiZIRO.setPrecision(40);
    izvodiZIRO.setRowId(true);
    izvodiZIRO.setTableName("IZVODI");
    izvodiZIRO.setServerColumnName("ZIRO");
    izvodiZIRO.setSqlType(1);
    izvodiBROJIZV.setCaption("Broj izvoda");
    izvodiBROJIZV.setColumnName("BROJIZV");
    izvodiBROJIZV.setDataType(com.borland.dx.dataset.Variant.INT);
    izvodiBROJIZV.setPrecision(6);
    izvodiBROJIZV.setRowId(true);
    izvodiBROJIZV.setTableName("IZVODI");
    izvodiBROJIZV.setServerColumnName("BROJIZV");
    izvodiBROJIZV.setSqlType(4);
    izvodiBROJIZV.setWidth(6);
    izvodiBROJSTAVKI.setCaption("Broj stavki");
    izvodiBROJSTAVKI.setColumnName("BROJSTAVKI");
    izvodiBROJSTAVKI.setDataType(com.borland.dx.dataset.Variant.INT);
    izvodiBROJSTAVKI.setPrecision(6);
    izvodiBROJSTAVKI.setTableName("IZVODI");
    izvodiBROJSTAVKI.setServerColumnName("BROJSTAVKI");
    izvodiBROJSTAVKI.setSqlType(4);
    izvodiBROJSTAVKI.setWidth(6);
    izvodiBROJSTAVKI.setDefault("0");
    izvodiPRETHSTANJE.setCaption("Prethodno stanje");
    izvodiPRETHSTANJE.setColumnName("PRETHSTANJE");
    izvodiPRETHSTANJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    izvodiPRETHSTANJE.setPrecision(17);
    izvodiPRETHSTANJE.setScale(2);
    izvodiPRETHSTANJE.setDisplayMask("###,###,##0.00");
    izvodiPRETHSTANJE.setDefault("0");
    izvodiPRETHSTANJE.setTableName("IZVODI");
    izvodiPRETHSTANJE.setServerColumnName("PRETHSTANJE");
    izvodiPRETHSTANJE.setSqlType(2);
    izvodiPRETHSTANJE.setDefault("0");
    izvodiID.setCaption("Ukupni dnevni dugovni promet");
    izvodiID.setColumnName("ID");
    izvodiID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    izvodiID.setPrecision(17);
    izvodiID.setScale(2);
    izvodiID.setDisplayMask("###,###,##0.00");
    izvodiID.setDefault("0");
    izvodiID.setTableName("IZVODI");
    izvodiID.setServerColumnName("ID");
    izvodiID.setSqlType(2);
    izvodiID.setDefault("0");
    izvodiIP.setCaption("Ukupni dnevni potražni promet");
    izvodiIP.setColumnName("IP");
    izvodiIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    izvodiIP.setPrecision(17);
    izvodiIP.setScale(2);
    izvodiIP.setDisplayMask("###,###,##0.00");
    izvodiIP.setDefault("0");
    izvodiIP.setTableName("IZVODI");
    izvodiIP.setServerColumnName("IP");
    izvodiIP.setSqlType(2);
    izvodiIP.setDefault("0");
    izvodiNOVOSTANJE.setCaption("Novo stanje");
    izvodiNOVOSTANJE.setColumnName("NOVOSTANJE");
    izvodiNOVOSTANJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    izvodiNOVOSTANJE.setPrecision(17);
    izvodiNOVOSTANJE.setScale(2);
    izvodiNOVOSTANJE.setDisplayMask("###,###,##0.00");
    izvodiNOVOSTANJE.setDefault("0");
    izvodiNOVOSTANJE.setTableName("IZVODI");
    izvodiNOVOSTANJE.setServerColumnName("NOVOSTANJE");
    izvodiNOVOSTANJE.setSqlType(2);
    izvodiNOVOSTANJE.setDefault("0");
    izvodiDATUM.setCaption("Datum");
    izvodiDATUM.setColumnName("DATUM");
    izvodiDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    izvodiDATUM.setPrecision(8);
    izvodiDATUM.setDisplayMask("dd-MM-yyyy");
//    izvodiDATUM.setEditMask("dd-MM-yyyy");
    izvodiDATUM.setTableName("IZVODI");
    izvodiDATUM.setWidth(10);
    izvodiDATUM.setServerColumnName("DATUM");
    izvodiDATUM.setSqlType(93);
    izvodiCNALOGA.setCaption("Oznaka naloga");
    izvodiCNALOGA.setColumnName("CNALOGA");
    izvodiCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    izvodiCNALOGA.setPrecision(30);
    izvodiCNALOGA.setTableName("IZVODI");
    izvodiCNALOGA.setServerColumnName("CNALOGA");
    izvodiCNALOGA.setSqlType(1);
    izvodiSTATUS.setCaption("Status");
    izvodiSTATUS.setColumnName("STATUS");
    izvodiSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    izvodiSTATUS.setPrecision(1);
    izvodiSTATUS.setTableName("IZVODI");
    izvodiSTATUS.setServerColumnName("STATUS");
    izvodiSTATUS.setSqlType(1);
    izvodiGOD.setCaption("Godina");
    izvodiGOD.setColumnName("GOD");
    izvodiGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    izvodiGOD.setPrecision(4);
    izvodiGOD.setRowId(true);
    izvodiGOD.setTableName("IZVODI");
    izvodiGOD.setServerColumnName("GOD");
    izvodiGOD.setSqlType(1);
    izvodi.setResolver(dm.getQresolver());
    izvodi.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Izvodi", null, true, Load.ALL));
 setColumns(new Column[] {izvodiLOKK, izvodiAKTIV, izvodiKNJIG, izvodiZIRO, izvodiBROJIZV, izvodiBROJSTAVKI, izvodiPRETHSTANJE, izvodiID, izvodiIP,
        izvodiNOVOSTANJE, izvodiDATUM, izvodiCNALOGA, izvodiSTATUS, izvodiGOD});
  }

  public void setall() {

    ddl.create("Izvodi")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addChar("ziro", 40, true)
       .addInteger("brojizv", 6, true)
       .addInteger("brojstavki", 6)
       .addFloat("prethstanje", 17, 2)
       .addFloat("id", 17, 2)
       .addFloat("ip", 17, 2)
       .addFloat("novostanje", 17, 2)
       .addDate("datum")
       .addChar("cnaloga", 30)
       .addChar("status", 1)
       .addChar("god", 4, true)
       .addPrimaryKey("knjig,god,ziro,brojizv");


    Naziv = "Izvodi";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
