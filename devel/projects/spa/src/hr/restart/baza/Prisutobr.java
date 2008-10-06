/****license*****************************************************************
**   file: Prisutobr.java
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



public class Prisutobr extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Prisutobr Prisutobrclass;

  QueryDataSet prob = new QueryDataSet();

  Column probLOKK = new Column();
  Column probAKTIV = new Column();
  Column probCRADNIK = new Column();
  Column probCVRP = new Column();
  Column probDAN = new Column();
  Column probGRPRIS = new Column();
  Column probSATI = new Column();
  Column probIZNOS = new Column();
  Column probSATIRADA = new Column();
  Column probSATIPRAZ = new Column();
  Column probIZNOSOBU = new Column();
  Column probIZNOSP = new Column();
  Column probPARAMETRI = new Column();

  public static Prisutobr getDataModule() {
    if (Prisutobrclass == null) {
      Prisutobrclass = new Prisutobr();
    }
    return Prisutobrclass;
  }

  public QueryDataSet getQueryDataSet() {
    return prob;
  }

  public Prisutobr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    probLOKK.setCaption("Status zauzetosti");
    probLOKK.setColumnName("LOKK");
    probLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    probLOKK.setPrecision(1);
    probLOKK.setTableName("PRISUTOBR");
    probLOKK.setServerColumnName("LOKK");
    probLOKK.setSqlType(1);
    probLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    probLOKK.setDefault("N");
    probAKTIV.setCaption("Aktivan - neaktivan");
    probAKTIV.setColumnName("AKTIV");
    probAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    probAKTIV.setPrecision(1);
    probAKTIV.setTableName("PRISUTOBR");
    probAKTIV.setServerColumnName("AKTIV");
    probAKTIV.setSqlType(1);
    probAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    probAKTIV.setDefault("D");
    probCRADNIK.setCaption("Radnik");
    probCRADNIK.setColumnName("CRADNIK");
    probCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    probCRADNIK.setPrecision(6);
    probCRADNIK.setRowId(true);
    probCRADNIK.setTableName("PRISUTOBR");
    probCRADNIK.setServerColumnName("CRADNIK");
    probCRADNIK.setSqlType(1);
    probCVRP.setCaption("Vrsta primanja");
    probCVRP.setColumnName("CVRP");
    probCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    probCVRP.setPrecision(3);
    probCVRP.setRowId(true);
    probCVRP.setTableName("PRISUTOBR");
    probCVRP.setServerColumnName("CVRP");
    probCVRP.setSqlType(5);
    probCVRP.setWidth(3);
    probDAN.setCaption("Dan");
    probDAN.setColumnName("DAN");
    probDAN.setDataType(com.borland.dx.dataset.Variant.SHORT);
    probDAN.setPrecision(2);
    probDAN.setRowId(true);
    probDAN.setTableName("PRISUTOBR");
    probDAN.setServerColumnName("DAN");
    probDAN.setSqlType(5);
    probDAN.setWidth(2);
    probDAN.setDefault("1");
    probGRPRIS.setCaption("Grupa");
    probGRPRIS.setColumnName("GRPRIS");
    probGRPRIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    probGRPRIS.setPrecision(5);
    probGRPRIS.setRowId(true);
    probGRPRIS.setTableName("PRISUTOBR");
    probGRPRIS.setServerColumnName("GRPRIS");
    probGRPRIS.setSqlType(1);
    probSATI.setCaption("Sati");
    probSATI.setColumnName("SATI");
    probSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    probSATI.setPrecision(17);
    probSATI.setScale(2);
    probSATI.setDisplayMask("###,###,##0.00");
    probSATI.setDefault("0");
    probSATI.setTableName("PRISUTOBR");
    probSATI.setServerColumnName("SATI");
    probSATI.setSqlType(2);
    probSATI.setDefault("0");
    probIZNOS.setCaption("Iznos");
    probIZNOS.setColumnName("IZNOS");
    probIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    probIZNOS.setPrecision(17);
    probIZNOS.setScale(2);
    probIZNOS.setDisplayMask("###,###,##0.00");
    probIZNOS.setDefault("0");
    probIZNOS.setTableName("PRISUTOBR");
    probIZNOS.setServerColumnName("IZNOS");
    probIZNOS.setSqlType(2);
    probIZNOS.setDefault("0");
    probSATIRADA.setCaption("Fond sati rada");
    probSATIRADA.setColumnName("SATIRADA");
    probSATIRADA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    probSATIRADA.setPrecision(17);
    probSATIRADA.setScale(2);
    probSATIRADA.setDisplayMask("###,###,##0.00");
    probSATIRADA.setDefault("0");
    probSATIRADA.setTableName("PRISUTOBR");
    probSATIRADA.setServerColumnName("SATIRADA");
    probSATIRADA.setSqlType(2);
    probSATIRADA.setDefault("0");
    probSATIPRAZ.setCaption("Fond sati praznika");
    probSATIPRAZ.setColumnName("SATIPRAZ");
    probSATIPRAZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    probSATIPRAZ.setPrecision(17);
    probSATIPRAZ.setScale(2);
    probSATIPRAZ.setDisplayMask("###,###,##0.00");
    probSATIPRAZ.setDefault("0");
    probSATIPRAZ.setTableName("PRISUTOBR");
    probSATIPRAZ.setServerColumnName("SATIPRAZ");
    probSATIPRAZ.setSqlType(2);
    probSATIPRAZ.setDefault("0");
    probIZNOSOBU.setCaption("Iznos obustava");
    probIZNOSOBU.setColumnName("IZNOSOBU");
    probIZNOSOBU.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    probIZNOSOBU.setPrecision(17);
    probIZNOSOBU.setScale(2);
    probIZNOSOBU.setDisplayMask("###,###,##0.00");
    probIZNOSOBU.setDefault("0");
    probIZNOSOBU.setTableName("PRISUTOBR");
    probIZNOSOBU.setServerColumnName("IZNOSOBU");
    probIZNOSOBU.setSqlType(2);
    probIZNOSOBU.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    probIZNOSOBU.setDefault("0");
    probIZNOSP.setCaption("Iznos pla\u0107e");
    probIZNOSP.setColumnName("IZNOSP");
    probIZNOSP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    probIZNOSP.setPrecision(17);
    probIZNOSP.setScale(2);
    probIZNOSP.setDisplayMask("###,###,##0.00");
    probIZNOSP.setDefault("0");
    probIZNOSP.setTableName("PRISUTOBR");
    probIZNOSP.setServerColumnName("IZNOSP");
    probIZNOSP.setSqlType(2);
    probIZNOSP.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    probIZNOSP.setDefault("0");
    probPARAMETRI.setCaption("Parametri");
    probPARAMETRI.setColumnName("PARAMETRI");
    probPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    probPARAMETRI.setPrecision(20);
    probPARAMETRI.setTableName("PRISUTOBR");
    probPARAMETRI.setServerColumnName("PARAMETRI");
    probPARAMETRI.setSqlType(1);
    probPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    prob.setResolver(dm.getQresolver());
    prob.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Prisutobr", null, true, Load.ALL));
 setColumns(new Column[] {probLOKK, probAKTIV, probCRADNIK, probCVRP, probDAN, probGRPRIS, probSATI, probIZNOS, probSATIRADA, probSATIPRAZ,
        probIZNOSOBU, probIZNOSP, probPARAMETRI});
  }

  public void setall() {

    ddl.create("Prisutobr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cradnik", 6, true)
       .addShort("cvrp", 3, true)
       .addShort("dan", 2, true)
       .addChar("grpris", 5, true)
       .addFloat("sati", 17, 2)
       .addFloat("iznos", 17, 2)
       .addFloat("satirada", 17, 2)
       .addFloat("satipraz", 17, 2)
       .addFloat("iznosobu", 17, 2)
       .addFloat("iznosp", 17, 2)
       .addChar("parametri", 20)
       .addPrimaryKey("cradnik,cvrp,dan,grpris");


    Naziv = "Prisutobr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cradnik", "dan"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
