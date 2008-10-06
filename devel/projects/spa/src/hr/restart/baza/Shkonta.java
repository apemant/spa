/****license*****************************************************************
**   file: Shkonta.java
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



public class Shkonta extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Shkonta Shkontaclass;

  QueryDataSet shkonta = new raDataSet();
  QueryDataSet shkontaall = new raDataSet();

  Column shkontaLOKK = new Column();
  Column shkontaAKTIV = new Column();
  Column shkontaVRDOK = new Column();
  Column shkontaCSKL = new Column();
  Column shkontaSTAVKA = new Column();
  Column shkontaOPIS = new Column();
  Column shkontaPOLJE = new Column();
  Column shkontaBROJKONTA = new Column();
  Column shkontaKARAKTERISTIKA = new Column();
  Column shkontaAPP = new Column();
  Column shkontaCSKLUL = new Column();
  Column shkontaCKNJIGE = new Column();
  Column shkontaCKOLONE = new Column();
  Column shkontaSQLCONDITION = new Column();

  public static Shkonta getDataModule() {
    if (Shkontaclass == null) {
      Shkontaclass = new Shkonta();
    }
    return Shkontaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return shkonta;
  }

  public QueryDataSet getAll() {
    return shkontaall;
  }

  public Shkonta() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    shkontaLOKK.setCaption("Status zauzetosti");
    shkontaLOKK.setColumnName("LOKK");
    shkontaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaLOKK.setPrecision(1);
    shkontaLOKK.setTableName("SHKONTA");
    shkontaLOKK.setServerColumnName("LOKK");
    shkontaLOKK.setSqlType(1);
    shkontaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shkontaLOKK.setDefault("N");
    shkontaAKTIV.setCaption("Aktivan - neaktivan");
    shkontaAKTIV.setColumnName("AKTIV");
    shkontaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaAKTIV.setPrecision(1);
    shkontaAKTIV.setTableName("SHKONTA");
    shkontaAKTIV.setServerColumnName("AKTIV");
    shkontaAKTIV.setSqlType(1);
    shkontaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shkontaAKTIV.setDefault("D");
    shkontaVRDOK.setCaption("Vrsta dokumenta");
    shkontaVRDOK.setColumnName("VRDOK");
    shkontaVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaVRDOK.setPrecision(3);
    shkontaVRDOK.setRowId(true);
    shkontaVRDOK.setTableName("SHKONTA");
    shkontaVRDOK.setServerColumnName("VRDOK");
    shkontaVRDOK.setSqlType(1);
    shkontaCSKL.setCaption("Skladište");
    shkontaCSKL.setColumnName("CSKL");
    shkontaCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaCSKL.setPrecision(12);
    shkontaCSKL.setRowId(true);
    shkontaCSKL.setTableName("SHKONTA");
    shkontaCSKL.setServerColumnName("CSKL");
    shkontaCSKL.setSqlType(1);
    shkontaSTAVKA.setCaption("RBS");
    shkontaSTAVKA.setColumnName("STAVKA");
    shkontaSTAVKA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    shkontaSTAVKA.setPrecision(2);
    shkontaSTAVKA.setRowId(true);
    shkontaSTAVKA.setTableName("SHKONTA");
    shkontaSTAVKA.setServerColumnName("STAVKA");
    shkontaSTAVKA.setSqlType(5);
    shkontaSTAVKA.setWidth(2);
    shkontaOPIS.setCaption("Opis");
    shkontaOPIS.setColumnName("OPIS");
    shkontaOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaOPIS.setPrecision(40);
    shkontaOPIS.setTableName("SHKONTA");
    shkontaOPIS.setServerColumnName("OPIS");
    shkontaOPIS.setSqlType(1);
    shkontaOPIS.setWidth(30);
    shkontaPOLJE.setCaption("Polje");
    shkontaPOLJE.setColumnName("POLJE");
    shkontaPOLJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaPOLJE.setPrecision(50);
    shkontaPOLJE.setTableName("SHKONTA");
    shkontaPOLJE.setServerColumnName("POLJE");
    shkontaPOLJE.setSqlType(1);
    shkontaBROJKONTA.setCaption("Konto");
    shkontaBROJKONTA.setColumnName("BROJKONTA");
    shkontaBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaBROJKONTA.setPrecision(8);
    shkontaBROJKONTA.setTableName("SHKONTA");
    shkontaBROJKONTA.setServerColumnName("BROJKONTA");
    shkontaBROJKONTA.setSqlType(1);
    shkontaKARAKTERISTIKA.setCaption("Karakteristika");
    shkontaKARAKTERISTIKA.setColumnName("KARAKTERISTIKA");
    shkontaKARAKTERISTIKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaKARAKTERISTIKA.setPrecision(1);
    shkontaKARAKTERISTIKA.setTableName("SHKONTA");
    shkontaKARAKTERISTIKA.setServerColumnName("KARAKTERISTIKA");
    shkontaKARAKTERISTIKA.setSqlType(1);
    shkontaAPP.setCaption("Aplikacija");
    shkontaAPP.setColumnName("APP");
    shkontaAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaAPP.setPrecision(10);
    shkontaAPP.setTableName("SHKONTA");
    shkontaAPP.setServerColumnName("APP");
    shkontaAPP.setSqlType(1);
    shkontaCSKLUL.setCaption("Ulazno skladište");
    shkontaCSKLUL.setColumnName("CSKLUL");
    shkontaCSKLUL.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaCSKLUL.setPrecision(12);
    shkontaCSKLUL.setRowId(true);
    shkontaCSKLUL.setTableName("SHKONTA");
    shkontaCSKLUL.setServerColumnName("CSKLUL");
    shkontaCSKLUL.setSqlType(1);
    shkontaCKNJIGE.setCaption("Knjiga");
    shkontaCKNJIGE.setColumnName("CKNJIGE");
    shkontaCKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaCKNJIGE.setPrecision(5);
    shkontaCKNJIGE.setTableName("SHKONTA");
    shkontaCKNJIGE.setServerColumnName("CKNJIGE");
    shkontaCKNJIGE.setSqlType(1);
    shkontaCKOLONE.setCaption("Kolona");
    shkontaCKOLONE.setColumnName("CKOLONE");
    shkontaCKOLONE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    shkontaCKOLONE.setPrecision(2);
    shkontaCKOLONE.setTableName("SHKONTA");
    shkontaCKOLONE.setServerColumnName("CKOLONE");
    shkontaCKOLONE.setSqlType(5);
    shkontaCKOLONE.setWidth(2);
    shkontaSQLCONDITION.setCaption("SQL upit na bazu");
    shkontaSQLCONDITION.setColumnName("SQLCONDITION");
    shkontaSQLCONDITION.setDataType(com.borland.dx.dataset.Variant.STRING);
    shkontaSQLCONDITION.setPrecision(3072);
    shkontaSQLCONDITION.setTableName("SHKONTA");
    shkontaSQLCONDITION.setServerColumnName("SQLCONDITION");
    shkontaSQLCONDITION.setSqlType(1);
    shkontaSQLCONDITION.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shkontaSQLCONDITION.setWidth(300);
    
    
    
    shkonta.setResolver(dm.getQresolver());
    shkonta.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Shkonta", null, true, Load.ALL));
 setColumns(new Column[] {shkontaLOKK, shkontaAKTIV, shkontaVRDOK, shkontaCSKL, shkontaSTAVKA, shkontaOPIS, shkontaPOLJE, shkontaBROJKONTA,
        shkontaKARAKTERISTIKA, shkontaAPP, shkontaCSKLUL, shkontaCKNJIGE, shkontaCKOLONE,shkontaSQLCONDITION});

    createFilteredDataSet(shkontaall, "");
  }

  public void setall() {

    ddl.create("Shkonta")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("vrdok", 3, true)
       .addChar("cskl", 12, true)
       .addShort("stavka", 2, true)
       .addChar("opis", 40)
       .addChar("polje", 50)
       .addChar("brojkonta", 8)
       .addChar("karakteristika", 1)
       .addChar("app", 10)
       .addChar("csklul", 12, true)
       .addChar("cknjige", 5)
       .addShort("ckolone", 2)
	   .addChar("sqlcondition", 3072)
       .addPrimaryKey("vrdok,cskl,stavka,csklul");


    Naziv = "Shkonta";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"stavka"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
