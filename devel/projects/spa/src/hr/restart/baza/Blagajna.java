/****license*****************************************************************
**   file: Blagajna.java
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



public class Blagajna extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Blagajna Blagajnaclass;

  QueryDataSet blagajna = new raDataSet();

  Column blagajnaLOKK = new Column();
  Column blagajnaAKTIV = new Column();
  Column blagajnaKNJIG = new Column();
  Column blagajnaCBLAG = new Column();
  Column blagajnaOZNVAL = new Column();
  Column blagajnaNAZIV = new Column();
  Column blagajnaSALDO = new Column();
  Column blagajnaDATIZV = new Column();
  Column blagajnaBRIZV = new Column();
  Column blagajnaPVSALDO = new Column();
  Column blagajnaBREZGOT = new Column();
  Column blagajnaSTAVKA = new Column();
  Column blagajnaCSKL = new Column();
  Column blagajnaVRDOK = new Column();

  public static Blagajna getDataModule() {
    if (Blagajnaclass == null) {
      Blagajnaclass = new Blagajna();
    }
    return Blagajnaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return blagajna;
  }

  public Blagajna() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    blagajnaLOKK.setCaption("Status zauzetosti");
    blagajnaLOKK.setColumnName("LOKK");
    blagajnaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaLOKK.setPrecision(1);
    blagajnaLOKK.setTableName("BLAGAJNA");
    blagajnaLOKK.setServerColumnName("LOKK");
    blagajnaLOKK.setSqlType(1);
    blagajnaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    blagajnaLOKK.setDefault("N");
    blagajnaAKTIV.setCaption("Aktivan - neaktivan");
    blagajnaAKTIV.setColumnName("AKTIV");
    blagajnaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaAKTIV.setPrecision(1);
    blagajnaAKTIV.setTableName("BLAGAJNA");
    blagajnaAKTIV.setServerColumnName("AKTIV");
    blagajnaAKTIV.setSqlType(1);
    blagajnaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    blagajnaAKTIV.setDefault("D");
    blagajnaKNJIG.setCaption("Knjigovodstvo");
    blagajnaKNJIG.setColumnName("KNJIG");
    blagajnaKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaKNJIG.setPrecision(12);
    blagajnaKNJIG.setRowId(true);
    blagajnaKNJIG.setTableName("BLAGAJNA");
    blagajnaKNJIG.setServerColumnName("KNJIG");
    blagajnaKNJIG.setSqlType(1);
    blagajnaCBLAG.setCaption("Broj blagajne");
    blagajnaCBLAG.setColumnName("CBLAG");
    blagajnaCBLAG.setDataType(com.borland.dx.dataset.Variant.INT);
    blagajnaCBLAG.setPrecision(6);
    blagajnaCBLAG.setRowId(true);
    blagajnaCBLAG.setTableName("BLAGAJNA");
    blagajnaCBLAG.setServerColumnName("CBLAG");
    blagajnaCBLAG.setSqlType(4);
    blagajnaCBLAG.setWidth(6);
    blagajnaOZNVAL.setCaption("Oznaka valute");
    blagajnaOZNVAL.setColumnName("OZNVAL");
    blagajnaOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaOZNVAL.setPrecision(3);
    blagajnaOZNVAL.setRowId(true);
    blagajnaOZNVAL.setTableName("BLAGAJNA");
    blagajnaOZNVAL.setServerColumnName("OZNVAL");
    blagajnaOZNVAL.setSqlType(1);
    blagajnaNAZIV.setCaption("Naziv blagajne");
    blagajnaNAZIV.setColumnName("NAZIV");
    blagajnaNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaNAZIV.setPrecision(50);
    blagajnaNAZIV.setTableName("BLAGAJNA");
    blagajnaNAZIV.setServerColumnName("NAZIV");
    blagajnaNAZIV.setSqlType(1);
    blagajnaNAZIV.setWidth(30);
    blagajnaSALDO.setCaption("Saldo blagajne");
    blagajnaSALDO.setColumnName("SALDO");
    blagajnaSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagajnaSALDO.setPrecision(17);
    blagajnaSALDO.setScale(2);
    blagajnaSALDO.setDisplayMask("###,###,##0.00");
    blagajnaSALDO.setDefault("0");
    blagajnaSALDO.setTableName("BLAGAJNA");
    blagajnaSALDO.setServerColumnName("SALDO");
    blagajnaSALDO.setSqlType(2);
    blagajnaDATIZV.setCaption("Datum zadnjeg blagajni\u010Dkog izvještaja");
    blagajnaDATIZV.setColumnName("DATIZV");
    blagajnaDATIZV.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    blagajnaDATIZV.setPrecision(8);
    blagajnaDATIZV.setDisplayMask("dd-MM-yyyy");
//    blagajnaDATIZV.setEditMask("dd-MM-yyyy");
    blagajnaDATIZV.setTableName("BLAGAJNA");
    blagajnaDATIZV.setServerColumnName("DATIZV");
    blagajnaDATIZV.setSqlType(93);
    blagajnaDATIZV.setWidth(10);
    blagajnaBRIZV.setCaption("Broj zadnjeg blagajni\u010Dkog izvještaja");
    blagajnaBRIZV.setColumnName("BRIZV");
    blagajnaBRIZV.setDataType(com.borland.dx.dataset.Variant.INT);
    blagajnaBRIZV.setPrecision(6);
    blagajnaBRIZV.setTableName("BLAGAJNA");
    blagajnaBRIZV.setServerColumnName("BRIZV");
    blagajnaBRIZV.setSqlType(4);
    blagajnaBRIZV.setWidth(6);
    blagajnaPVSALDO.setCaption("Saldo u dom. valuti");
    blagajnaPVSALDO.setColumnName("PVSALDO");
    blagajnaPVSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagajnaPVSALDO.setPrecision(17);
    blagajnaPVSALDO.setScale(2);
    blagajnaPVSALDO.setDisplayMask("###,###,##0.00");
    blagajnaPVSALDO.setDefault("0");
    blagajnaPVSALDO.setTableName("BLAGAJNA");
    blagajnaPVSALDO.setServerColumnName("PVSALDO");
    blagajnaPVSALDO.setSqlType(2);
    blagajnaPVSALDO.setDefault("0");
    blagajnaBREZGOT.setCaption("Bezgotovinska");
    blagajnaBREZGOT.setColumnName("BREZGOT");
    blagajnaBREZGOT.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaBREZGOT.setPrecision(1);
    blagajnaBREZGOT.setTableName("BLAGAJNA");
    blagajnaBREZGOT.setServerColumnName("BREZGOT");
    blagajnaBREZGOT.setSqlType(1);
    blagajnaBREZGOT.setDefault("N");
    blagajnaSTAVKA.setCaption("Veza sa kontom preko SHKONTA.VRDOK=BL");
    blagajnaSTAVKA.setColumnName("STAVKA");
    blagajnaSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaSTAVKA.setPrecision(8);
    blagajnaSTAVKA.setTableName("BLAGAJNA");
    blagajnaSTAVKA.setServerColumnName("STAVKA");
    blagajnaSTAVKA.setSqlType(1);
    blagajnaCSKL.setCaption("Veza sa kontom preko SHKONTA");
    blagajnaCSKL.setColumnName("CSKL");
    blagajnaCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaCSKL.setPrecision(12);
    blagajnaCSKL.setTableName("BLAGAJNA");
    blagajnaCSKL.setServerColumnName("CSKL");
    blagajnaCSKL.setSqlType(1);
    blagajnaVRDOK.setCaption("Vrsta dokumenta");
    blagajnaVRDOK.setColumnName("VRDOK");
    blagajnaVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajnaVRDOK.setPrecision(3);
    blagajnaVRDOK.setTableName("BLAGAJNA");
    blagajnaVRDOK.setServerColumnName("VRDOK");
    blagajnaVRDOK.setSqlType(1);
    blagajna.setResolver(dm.getQresolver());
    blagajna.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Blagajna", null, true, Load.ALL));
 setColumns(new Column[] {blagajnaLOKK, blagajnaAKTIV, blagajnaKNJIG, blagajnaCBLAG, blagajnaOZNVAL, blagajnaNAZIV, blagajnaSALDO, blagajnaDATIZV,
        blagajnaBRIZV, blagajnaPVSALDO, blagajnaBREZGOT, blagajnaSTAVKA, blagajnaCSKL, blagajnaVRDOK});
  }

  public void setall() {

    ddl.create("Blagajna")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addInteger("cblag", 6, true)
       .addChar("oznval", 3, true)
       .addChar("naziv", 50)
       .addFloat("saldo", 17, 2)
       .addDate("datizv")
       .addInteger("brizv", 6)
       .addFloat("pvsaldo", 17, 2)
       .addChar("brezgot", 1, "N")
       .addChar("stavka", 8)
       .addChar("cskl", 12)
       .addChar("vrdok", 3)
       .addPrimaryKey("knjig,cblag,oznval");


    Naziv = "Blagajna";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
