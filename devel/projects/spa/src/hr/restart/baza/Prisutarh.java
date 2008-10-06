/****license*****************************************************************
**   file: Prisutarh.java
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



public class Prisutarh extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Prisutarh Prisutarhclass;

  QueryDataSet parh = new QueryDataSet();

  Column parhLOKK = new Column();
  Column parhAKTIV = new Column();
  Column parhGODOBR = new Column();
  Column parhMJOBR = new Column();
  Column parhRBROBR = new Column();
  Column parhCRADNIK = new Column();
  Column parhCVRP = new Column();
  Column parhDAN = new Column();
  Column parhGRPRIS = new Column();
  Column parhSATI = new Column();
  Column parhIZNOS = new Column();
  Column parhSATIRADA = new Column();
  Column parhSATIPRAZ = new Column();
  Column parhIZNOSOBU = new Column();
  Column parhIZNOSP = new Column();
  Column parhPARAMETRI = new Column();

  public static Prisutarh getDataModule() {
    if (Prisutarhclass == null) {
      Prisutarhclass = new Prisutarh();
    }
    return Prisutarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return parh;
  }

  public Prisutarh() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    parhLOKK.setCaption("Status zauzetosti");
    parhLOKK.setColumnName("LOKK");
    parhLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    parhLOKK.setPrecision(1);
    parhLOKK.setTableName("PRISUTARH");
    parhLOKK.setServerColumnName("LOKK");
    parhLOKK.setSqlType(1);
    parhLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parhLOKK.setDefault("N");
    parhAKTIV.setCaption("Aktivan - neaktivan");
    parhAKTIV.setColumnName("AKTIV");
    parhAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    parhAKTIV.setPrecision(1);
    parhAKTIV.setTableName("PRISUTARH");
    parhAKTIV.setServerColumnName("AKTIV");
    parhAKTIV.setSqlType(1);
    parhAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parhAKTIV.setDefault("D");
    parhGODOBR.setCaption("Godina obra\u010Duna");
    parhGODOBR.setColumnName("GODOBR");
    parhGODOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    parhGODOBR.setPrecision(4);
    parhGODOBR.setRowId(true);
    parhGODOBR.setTableName("PRISUTARH");
    parhGODOBR.setServerColumnName("GODOBR");
    parhGODOBR.setSqlType(5);
    parhGODOBR.setWidth(4);
    parhMJOBR.setCaption("Mjesec obra\u010Duna");
    parhMJOBR.setColumnName("MJOBR");
    parhMJOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    parhMJOBR.setPrecision(2);
    parhMJOBR.setRowId(true);
    parhMJOBR.setTableName("PRISUTARH");
    parhMJOBR.setServerColumnName("MJOBR");
    parhMJOBR.setSqlType(5);
    parhMJOBR.setWidth(2);
    parhRBROBR.setCaption("Redni broj obra\u010Duna");
    parhRBROBR.setColumnName("RBROBR");
    parhRBROBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    parhRBROBR.setPrecision(3);
    parhRBROBR.setRowId(true);
    parhRBROBR.setTableName("PRISUTARH");
    parhRBROBR.setServerColumnName("RBROBR");
    parhRBROBR.setSqlType(5);
    parhRBROBR.setWidth(3);
    parhCRADNIK.setCaption("Radnik");
    parhCRADNIK.setColumnName("CRADNIK");
    parhCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    parhCRADNIK.setPrecision(6);
    parhCRADNIK.setRowId(true);
    parhCRADNIK.setTableName("PRISUTARH");
    parhCRADNIK.setServerColumnName("CRADNIK");
    parhCRADNIK.setSqlType(1);
    parhCVRP.setCaption("Vrsta primanja");
    parhCVRP.setColumnName("CVRP");
    parhCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    parhCVRP.setPrecision(3);
    parhCVRP.setRowId(true);
    parhCVRP.setTableName("PRISUTARH");
    parhCVRP.setServerColumnName("CVRP");
    parhCVRP.setSqlType(5);
    parhCVRP.setWidth(3);
    parhDAN.setCaption("Dan");
    parhDAN.setColumnName("DAN");
    parhDAN.setDataType(com.borland.dx.dataset.Variant.SHORT);
    parhDAN.setPrecision(2);
    parhDAN.setRowId(true);
    parhDAN.setTableName("PRISUTARH");
    parhDAN.setServerColumnName("DAN");
    parhDAN.setSqlType(5);
    parhDAN.setWidth(2);
    parhDAN.setDefault("1");
    parhGRPRIS.setCaption("Grupa");
    parhGRPRIS.setColumnName("GRPRIS");
    parhGRPRIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    parhGRPRIS.setPrecision(5);
    parhGRPRIS.setRowId(true);
    parhGRPRIS.setTableName("PRISUTARH");
    parhGRPRIS.setServerColumnName("GRPRIS");
    parhGRPRIS.setSqlType(1);
    parhSATI.setCaption("Sati");
    parhSATI.setColumnName("SATI");
    parhSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    parhSATI.setPrecision(17);
    parhSATI.setScale(2);
    parhSATI.setDisplayMask("###,###,##0.00");
    parhSATI.setDefault("0");
    parhSATI.setTableName("PRISUTARH");
    parhSATI.setServerColumnName("SATI");
    parhSATI.setSqlType(2);
    parhSATI.setDefault("0");
    parhIZNOS.setCaption("Iznos");
    parhIZNOS.setColumnName("IZNOS");
    parhIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    parhIZNOS.setPrecision(17);
    parhIZNOS.setScale(2);
    parhIZNOS.setDisplayMask("###,###,##0.00");
    parhIZNOS.setDefault("0");
    parhIZNOS.setTableName("PRISUTARH");
    parhIZNOS.setServerColumnName("IZNOS");
    parhIZNOS.setSqlType(2);
    parhIZNOS.setDefault("0");
    parhSATIRADA.setCaption("Fond sati rada");
    parhSATIRADA.setColumnName("SATIRADA");
    parhSATIRADA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    parhSATIRADA.setPrecision(17);
    parhSATIRADA.setScale(2);
    parhSATIRADA.setDisplayMask("###,###,##0.00");
    parhSATIRADA.setDefault("0");
    parhSATIRADA.setTableName("PRISUTARH");
    parhSATIRADA.setServerColumnName("SATIRADA");
    parhSATIRADA.setSqlType(2);
    parhSATIRADA.setDefault("0");
    parhSATIPRAZ.setCaption("Fond sati praznika");
    parhSATIPRAZ.setColumnName("SATIPRAZ");
    parhSATIPRAZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    parhSATIPRAZ.setPrecision(17);
    parhSATIPRAZ.setScale(2);
    parhSATIPRAZ.setDisplayMask("###,###,##0.00");
    parhSATIPRAZ.setDefault("0");
    parhSATIPRAZ.setTableName("PRISUTARH");
    parhSATIPRAZ.setServerColumnName("SATIPRAZ");
    parhSATIPRAZ.setSqlType(2);
    parhSATIPRAZ.setDefault("0");
    parhIZNOSOBU.setCaption("Iznos obustava");
    parhIZNOSOBU.setColumnName("IZNOSOBU");
    parhIZNOSOBU.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    parhIZNOSOBU.setPrecision(17);
    parhIZNOSOBU.setScale(2);
    parhIZNOSOBU.setDisplayMask("###,###,##0.00");
    parhIZNOSOBU.setDefault("0");
    parhIZNOSOBU.setTableName("PRISUTARH");
    parhIZNOSOBU.setServerColumnName("IZNOSOBU");
    parhIZNOSOBU.setSqlType(2);
    parhIZNOSOBU.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parhIZNOSOBU.setDefault("0");
    parhIZNOSP.setCaption("Iznos pla\u0107e");
    parhIZNOSP.setColumnName("IZNOSP");
    parhIZNOSP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    parhIZNOSP.setPrecision(17);
    parhIZNOSP.setScale(2);
    parhIZNOSP.setDisplayMask("###,###,##0.00");
    parhIZNOSP.setDefault("0");
    parhIZNOSP.setTableName("PRISUTARH");
    parhIZNOSP.setServerColumnName("IZNOSP");
    parhIZNOSP.setSqlType(2);
    parhIZNOSP.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parhIZNOSP.setDefault("0");
    parhPARAMETRI.setCaption("Parametri");
    parhPARAMETRI.setColumnName("PARAMETRI");
    parhPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    parhPARAMETRI.setPrecision(20);
    parhPARAMETRI.setTableName("PRISUTARH");
    parhPARAMETRI.setServerColumnName("PARAMETRI");
    parhPARAMETRI.setSqlType(1);
    parhPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parh.setResolver(dm.getQresolver());
    parh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Prisutarh", null, true, Load.ALL));
 setColumns(new Column[] {parhLOKK, parhAKTIV, parhGODOBR, parhMJOBR, parhRBROBR, parhCRADNIK, parhCVRP, parhDAN, parhGRPRIS, parhSATI, parhIZNOS,
        parhSATIRADA, parhSATIPRAZ, parhIZNOSOBU, parhIZNOSP, parhPARAMETRI});
  }

  public void setall() {

    ddl.create("Prisutarh")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("godobr", 4, true)
       .addShort("mjobr", 2, true)
       .addShort("rbrobr", 3, true)
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
       .addPrimaryKey("godobr,mjobr,rbrobr,cradnik,cvrp,dan,grpris");


    Naziv = "Prisutarh";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"mjobr", "rbrobr", "cradnik", "dan"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
