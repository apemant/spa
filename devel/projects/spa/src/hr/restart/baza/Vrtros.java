/****license*****************************************************************
**   file: Vrtros.java
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

public class Vrtros extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Vrtros Vrtrosclass;
  dM dm  = dM.getDataModule();

  QueryDataSet Vrtros = new raDataSet();
  QueryDataSet Vrtrosaktiv = new raDataSet();
  Column vrtrosLOKK = new Column();
  Column vrtrosAKTIV = new Column();
  Column vrtrosCVRTR = new Column();
  Column vrtrosNAZIV = new Column();
  Column vrtrosBROJKONTA = new Column();
  Column vrtrosVRDOK = new Column();
  Column vrtrosPPOR1 = new Column();

  public static Vrtros getDataModule() {
    if (Vrtrosclass == null) {
      Vrtrosclass = new Vrtros();
    }
    return Vrtrosclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return Vrtros;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return Vrtrosaktiv;
  }

  public Vrtros() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    vrtrosBROJKONTA.setCaption("Broj konta");
    vrtrosBROJKONTA.setColumnName("BROJKONTA");
    vrtrosBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrtrosBROJKONTA.setPrecision(8);
    vrtrosBROJKONTA.setTableName("VRTROS");
    vrtrosBROJKONTA.setSqlType(1);
    vrtrosBROJKONTA.setServerColumnName("BROJKONTA");
    vrtrosBROJKONTA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);    
    vrtrosCVRTR.setCaption("Šifra");
    vrtrosCVRTR.setColumnName("CVRTR");
    vrtrosCVRTR.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrtrosCVRTR.setPrecision(12);
    vrtrosCVRTR.setRowId(true);
    vrtrosCVRTR.setTableName("VRTROS");
    vrtrosCVRTR.setSqlType(1);
    vrtrosCVRTR.setServerColumnName("CVRTR");
    vrtrosLOKK.setCaption("Lokk");
    vrtrosLOKK.setColumnName("LOKK");
    vrtrosLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrtrosLOKK.setDefault("N");
    vrtrosLOKK.setPrecision(1);
    vrtrosLOKK.setTableName("VRTROS");
    vrtrosLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrtrosLOKK.setSqlType(1);
    vrtrosLOKK.setServerColumnName("LOKK");
    vrtrosNAZIV.setCaption("Naziv");
    vrtrosNAZIV.setColumnName("NAZIV");
    vrtrosNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrtrosNAZIV.setPrecision(50);
    vrtrosNAZIV.setTableName("VRTROS");
    vrtrosNAZIV.setSqlType(1);
    vrtrosNAZIV.setServerColumnName("NAZIV");
    vrtrosAKTIV.setCaption("Aktiv");
    vrtrosAKTIV.setColumnName("AKTIV");
    vrtrosAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrtrosAKTIV.setDefault("D");
    vrtrosAKTIV.setPrecision(1);
    vrtrosAKTIV.setTableName("VRTROS");
    vrtrosAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrtrosAKTIV.setSqlType(1);
    vrtrosAKTIV.setServerColumnName("AKTIV");
    vrtrosVRDOK.setCaption("Vrsta dokumenta");
    vrtrosVRDOK.setColumnName("VRDOK");
    vrtrosVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrtrosVRDOK.setPrecision(3);
    vrtrosVRDOK.setTableName("VRTROS");
    vrtrosVRDOK.setServerColumnName("VRDOK");
    vrtrosVRDOK.setSqlType(1);
    vrtrosVRDOK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    
    vrtrosPPOR1.setCaption("Posto osnovice poreza");
    vrtrosPPOR1.setColumnName("OSNOVICA");
    vrtrosPPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vrtrosPPOR1.setDisplayMask("###,###,##0.00");
    vrtrosPPOR1.setDefault("0");
    vrtrosPPOR1.setPrecision(15);
    vrtrosPPOR1.setScale(2);
    vrtrosPPOR1.setTableName("VRTROS");
    vrtrosPPOR1.setWidth(14);
    vrtrosPPOR1.setSqlType(2);
    vrtrosPPOR1.setServerColumnName("OSNOVICA");

    
    
    
    Vrtros.setResolver(dm.getQresolver());
    Vrtros.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from vrtros", null, true, Load.ALL));
 setColumns(new Column[] {vrtrosLOKK, vrtrosAKTIV, vrtrosCVRTR, vrtrosNAZIV, vrtrosBROJKONTA, vrtrosVRDOK,vrtrosPPOR1});

    createFilteredDataSet(Vrtrosaktiv, "aktiv = 'D'");
  }

  public void setall(){
    /*SqlDefTabela = "create table Vrtros " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N'," +
      "aktiv char(1) CHARACTER SET WIN1250 default 'D'," +
      "cvrtr CHAR(12) CHARACTER SET WIN1250 NOT NULL," +
      "naziv CHAR(50) CHARACTER SET WIN1250," +
      "brojkonta CHAR(8) CHARACTER SET WIN1250, " +
      "Primary Key (cvrtr))"; */

    ddl.create("vrtros")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvrtr", 12, true)
       .addChar("naziv", 50)
       .addChar("brojkonta", 8)
       .addChar("vrdok", 3)
	   .addFloat("osnovica", 8, 2)
       .addPrimaryKey("cvrtr");

    Naziv="Vrtros";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    NaziviIdx=new String[]{"ilokkmjtros","iaktivmjtros","icmjtr"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Vrtros (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Vrtros (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Vrtros (cvrtr)"} ;
                            */
 }
}