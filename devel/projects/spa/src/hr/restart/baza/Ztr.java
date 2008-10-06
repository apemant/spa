/****license*****************************************************************
**   file: Ztr.java
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



public class Ztr extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Ztr Ztrclass;

  QueryDataSet ztr = new raDataSet();

  Column ztrLOKK = new Column();
  Column ztrAKTIV = new Column();
  Column ztrCZT = new Column();
  Column ztrNZT = new Column();
  Column ztrCPAR = new Column();
  Column ztrPZT = new Column();
  Column ztrFIKS = new Column();
  Column ztrVRDOK = new Column();

  public static Ztr getDataModule() {
    if (Ztrclass == null) {
      Ztrclass = new Ztr();
    }
    return Ztrclass;
  }

  public QueryDataSet getQueryDataSet() {
    return ztr;
  }

  public Ztr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    ztrLOKK.setCaption("Status zauzetosti");
    ztrLOKK.setColumnName("LOKK");
    ztrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ztrLOKK.setPrecision(1);
    ztrLOKK.setTableName("ZTR");
    ztrLOKK.setServerColumnName("LOKK");
    ztrLOKK.setSqlType(1);
    ztrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ztrLOKK.setDefault("N");
    ztrAKTIV.setCaption("Aktivan - neaktivan");
    ztrAKTIV.setColumnName("AKTIV");
    ztrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    ztrAKTIV.setPrecision(1);
    ztrAKTIV.setTableName("ZTR");
    ztrAKTIV.setServerColumnName("AKTIV");
    ztrAKTIV.setSqlType(1);
    ztrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ztrAKTIV.setDefault("D");
    ztrCZT.setCaption("Šifra");
    ztrCZT.setColumnName("CZT");
    ztrCZT.setDataType(com.borland.dx.dataset.Variant.SHORT);
    ztrCZT.setPrecision(2);
    ztrCZT.setRowId(true);
    ztrCZT.setTableName("ZTR");
    ztrCZT.setServerColumnName("CZT");
    ztrCZT.setSqlType(5);
    ztrCZT.setWidth(2);
    ztrNZT.setCaption("Naziv");
    ztrNZT.setColumnName("NZT");
    ztrNZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ztrNZT.setPrecision(50);
    ztrNZT.setTableName("ZTR");
    ztrNZT.setServerColumnName("NZT");
    ztrNZT.setSqlType(1);
    ztrNZT.setWidth(20);
    ztrCPAR.setCaption("Partner");
    ztrCPAR.setColumnName("CPAR");
    ztrCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    ztrCPAR.setPrecision(6);
    ztrCPAR.setTableName("ZTR");
    ztrCPAR.setServerColumnName("CPAR");
    ztrCPAR.setSqlType(4);
    ztrCPAR.setWidth(6);
    ztrPZT.setCaption("Postotak");
    ztrPZT.setColumnName("PZT");
    ztrPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ztrPZT.setPrecision(6);
    ztrPZT.setScale(2);
    ztrPZT.setDisplayMask("###,###,##0.00");
    ztrPZT.setDefault("0");
    ztrPZT.setTableName("ZTR");
    ztrPZT.setServerColumnName("PZT");
    ztrPZT.setSqlType(2);
    ztrFIKS.setCaption("Fiksni postotak");
    ztrFIKS.setColumnName("FIKS");
    ztrFIKS.setDataType(com.borland.dx.dataset.Variant.STRING);
    ztrFIKS.setPrecision(1);
    ztrFIKS.setTableName("ZTR");
    ztrFIKS.setServerColumnName("FIKS");
    ztrFIKS.setSqlType(1);
    ztrFIKS.setDefault("N");
    ztrVRDOK.setCaption("Vrsta dokumenta");
    ztrVRDOK.setColumnName("VRDOK");
    ztrVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ztrVRDOK.setPrecision(3);
    ztrVRDOK.setTableName("ZTR");
    ztrVRDOK.setServerColumnName("VRDOK");
    ztrVRDOK.setSqlType(1);

    ztr.setResolver(dm.getQresolver());
    ztr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Ztr", null, true, Load.ALL));
 setColumns(new Column[] {ztrLOKK, ztrAKTIV, ztrCZT, ztrNZT, ztrCPAR, ztrPZT, ztrFIKS,ztrVRDOK});
  }

  public void setall() {

    ddl.create("Ztr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("czt", 2, true)
       .addChar("nzt", 50)
       .addInteger("cpar", 6)
       .addFloat("pzt", 6, 2)
       .addChar("fiks", 1, "N")
       .addChar("vrdok", 3)
       .addPrimaryKey("czt");


    Naziv = "Ztr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
