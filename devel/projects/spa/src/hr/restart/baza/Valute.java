/****license*****************************************************************
**   file: Valute.java
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

public class Valute extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Valute Valuteclass;
  dM dm  = dM.getDataModule();
  QueryDataSet valute = new raDataSet();
  QueryDataSet valuteaktiv = new raDataSet();
  QueryDataSet valutestrane = new raDataSet();
  Column valuteLOKK = new Column();
  Column valuteAKTIV = new Column();
  Column valuteOZNVAL = new Column();
  Column valuteCVAL = new Column();
  Column valuteJEDVAL = new Column();
  Column valuteNAZVAL = new Column();
  Column valuteSTRVAL = new Column();
  Column valuteNIZJED = new Column();
  Column valuteREFVAL = new Column();


  public static Valute getDataModule() {
    if (Valuteclass == null) {
      Valuteclass = new Valute();
    }
    return Valuteclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return valute;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return valuteaktiv;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStrane() {
    return valutestrane;
  }

  public Valute() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    valuteNIZJED.setCaption("Niža jedinica");
    valuteNIZJED.setColumnName("NIZJED");
    valuteNIZJED.setDataType(com.borland.dx.dataset.Variant.STRING);
    valuteNIZJED.setPrecision(30);
    valuteNIZJED.setTableName("VALUTE");
    valuteNIZJED.setSqlType(1);
    valuteNIZJED.setServerColumnName("NIZJED");
    valuteJEDVAL.setCaption(dmRes.getString("valuteJEDVAL_caption"));
    valuteJEDVAL.setColumnName("JEDVAL");
    valuteJEDVAL.setDataType(com.borland.dx.dataset.Variant.INT);
    valuteJEDVAL.setTableName("VALUTE");
    valuteJEDVAL.setSqlType(4);
    valuteJEDVAL.setServerColumnName("JEDVAL");
    valuteNAZVAL.setCaption(dmRes.getString("valuteNAZVAL_caption"));
    valuteNAZVAL.setColumnName("NAZVAL");
    valuteNAZVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    valuteNAZVAL.setPrecision(30);
    valuteNAZVAL.setTableName("VALUTE");
    valuteNAZVAL.setSqlType(1);
    valuteNAZVAL.setServerColumnName("NAZVAL");
    valuteCVAL.setCaption("Šifra");
    valuteCVAL.setColumnName("CVAL");
    valuteCVAL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    valuteCVAL.setPrecision(3);
    valuteCVAL.setRowId(true);
    valuteCVAL.setTableName("VALUTE");
    valuteCVAL.setSqlType(5);
    valuteCVAL.setServerColumnName("CVAL");
    valuteOZNVAL.setCaption(dmRes.getString("valuteOZNVAL_caption"));
    valuteOZNVAL.setColumnName("OZNVAL");
    valuteOZNVAL.setPrecision(3);
    valuteOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    valuteOZNVAL.setTableName("VALUTE");
    valuteOZNVAL.setSqlType(1);
    valuteOZNVAL.setServerColumnName("OZNVAL");
    valuteAKTIV.setCaption(dmRes.getString("valuteAKTIV_caption"));
    valuteAKTIV.setColumnName("AKTIV");
    valuteAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    valuteAKTIV.setPrecision(1);
    valuteAKTIV.setDefault("D");
    valuteAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    valuteAKTIV.setSqlType(1);
    valuteAKTIV.setServerColumnName("AKTIV");
    valuteAKTIV.setTableName("VALUTE");
    valuteLOKK.setCaption("Lokk");
    valuteLOKK.setColumnName("LOKK");
    valuteLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    valuteLOKK.setPrecision(1);
    valuteLOKK.setDefault("N");
    valuteLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    valuteLOKK.setSqlType(1);
    valuteLOKK.setServerColumnName("LOKK");
    valuteLOKK.setTableName("VALUTE");
    valuteSTRVAL.setCaption(dmRes.getString("valuteSTRVAL_caption"));
    valuteSTRVAL.setColumnName("STRVAL");
    valuteSTRVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    valuteSTRVAL.setDefault("D");
    valuteSTRVAL.setPrecision(1);
    valuteSTRVAL.setTableName("VALUTE");
    valuteSTRVAL.setSqlType(1);
    valuteSTRVAL.setServerColumnName("STRVAL");
    valuteREFVAL.setCaption(dmRes.getString("valuteREFVAL_caption"));
    valuteREFVAL.setColumnName("REFVAL");
    valuteREFVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    valuteREFVAL.setDefault("N");
    valuteREFVAL.setPrecision(1);
    valuteREFVAL.setTableName("VALUTE");
    valuteREFVAL.setSqlType(1);
    valuteREFVAL.setServerColumnName("REFVAL");

    valute.setResolver(dm.getQresolver());
    valute.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from valute", null, true, Load.ALL));
 setColumns(new Column[] {valuteLOKK, valuteAKTIV, valuteOZNVAL, valuteCVAL, valuteJEDVAL, valuteNAZVAL, valuteSTRVAL, valuteNIZJED, valuteREFVAL});

    createFilteredDataSet(valuteaktiv, "aktiv = 'D'");
    createFilteredDataSet(valutestrane, "strval = 'D' AND aktiv = 'D'");
  }

 public void setall(){

    /*SqlDefTabela = "create table Valute " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "oznval char(3) CHARACTER SET WIN1250 not null,"+
      "cval numeric(3,0) not null,"+
      "jedval numeric(5,0), " +
      "nazval char(30)," +
      "nizjed char(30)," +
      "strval char(1)," + /// D/N
      "refval char(1)," + /// D/N
      "Primary Key (cval))" ;*/

    ddl.create("valute")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("oznval", 3, true)
       .addShort("cval", 3, true)
       .addInteger("jedval", 5)
       .addChar("nazval", 30)
       .addChar("strval", 1)
       .addChar("nizjed", 30)
       .addChar("refval", 1)
       .addPrimaryKey("cval");

    Naziv="Valute";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"oznval"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


/*    NaziviIdx=new String[]{"icvaluteValute","ioznakaValute","iaktivValute","ilokkValute"};

    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+NaziviIdx[0] +" on Valute (cval)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Valute (oznval)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[2] +" on Valute (aktiv)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +"  on Valute (lokk)" };
  */
  }
}