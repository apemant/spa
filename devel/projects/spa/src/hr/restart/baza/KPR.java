/****license*****************************************************************
**   file: KPR.java
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

public class KPR extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static KPR KPRclass;

  QueryDataSet kpr = new raDataSet();

  Column kprLOKK = new Column();
  Column kprAKTIV = new Column();
  Column kprCSKL = new Column();
  Column kprRBR = new Column();
  Column kprGOD = new Column();
  Column kprKLJUC = new Column();
  Column kprOPIS = new Column();
  Column kprDATUM = new Column();
  Column kprZAD = new Column();
  Column kprRAZ = new Column();


  public static KPR getDataModule() {
    if (KPRclass == null) {
      KPRclass = new KPR();
    }
    return KPRclass;
  }

  public QueryDataSet getQueryDataSet() {
    return kpr;
  }

  public KPR() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    kprGOD.setCaption("Godina");
    kprGOD.setColumnName("GOD");
    kprGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    kprGOD.setPrecision(4);
    kprGOD.setTableName("KPR");
    kprGOD.setServerColumnName("GOD");
    kprGOD.setSqlType(1);
    kprGOD.setRowId(true);

    kprLOKK.setCaption("Status zauzetosti");
    kprLOKK.setColumnName("LOKK");
    kprLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kprLOKK.setPrecision(1);
    kprLOKK.setTableName("KPR");
    kprLOKK.setServerColumnName("LOKK");
    kprLOKK.setSqlType(1);
    kprLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kprLOKK.setDefault("N");
    kprAKTIV.setCaption("Aktivan - neaktivan");
    kprAKTIV.setColumnName("AKTIV");
    kprAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    kprAKTIV.setPrecision(1);
    kprAKTIV.setTableName("KPR");
    kprAKTIV.setServerColumnName("AKTIV");
    kprAKTIV.setSqlType(1);
    kprAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kprAKTIV.setDefault("N");
    kprRBR.setCaption("Rbr");
    kprRBR.setColumnName("RBR");
    kprRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    kprRBR.setPrecision(6);
    kprRBR.setRowId(true);
    kprRBR.setTableName("KPR");
    kprRBR.setServerColumnName("RBR");
    kprRBR.setSqlType(4);
    kprRBR.setWidth(6);
    kprKLJUC.setCaption("Kljuc");
    kprKLJUC.setColumnName("KLJUC");
    kprKLJUC.setDataType(com.borland.dx.dataset.Variant.STRING);
    kprKLJUC.setPrecision(20);
    kprKLJUC.setTableName("KPR");
    kprKLJUC.setServerColumnName("KLJUC");
    kprKLJUC.setSqlType(1);
    kprOPIS.setCaption("Opis");
    kprOPIS.setColumnName("OPIS");
    kprOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    kprOPIS.setPrecision(80);
    kprOPIS.setTableName("KPR");
    kprOPIS.setServerColumnName("OPIS");
    kprOPIS.setSqlType(1);
    kprOPIS.setWidth(30);
    kprDATUM.setCaption("Datum");
    kprDATUM.setColumnName("DATUM");
    kprDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    kprDATUM.setDisplayMask("dd-MM-yyyy");
//    kprDATUM.setEditMask("dd-MM-yyyy");
    kprDATUM.setTableName("KPR");
    kprDATUM.setServerColumnName("DATUM");
    kprDATUM.setSqlType(93);
    kprDATUM.setWidth(10);
    kprZAD.setCaption("Zaduzenje");
    kprZAD.setColumnName("ZAD");
    kprZAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kprZAD.setPrecision(17);
    kprZAD.setScale(2);
    kprZAD.setDisplayMask("###,###,##0.00");
    kprZAD.setDefault("0");
    kprZAD.setTableName("KPR");
    kprZAD.setServerColumnName("ZAD");
    kprZAD.setSqlType(2);
    kprZAD.setDefault("0");
    kprRAZ.setCaption("Razduzenje");
    kprRAZ.setColumnName("RAZ");
    kprRAZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kprRAZ.setPrecision(17);
    kprRAZ.setScale(2);
    kprRAZ.setDisplayMask("###,###,##0.00");
    kprRAZ.setDefault("0");
    kprRAZ.setTableName("KPR");
    kprRAZ.setServerColumnName("RAZ");
    kprRAZ.setSqlType(2);
    kprRAZ.setDefault("0");
    kprCSKL.setCaption("Skladiste");
    kprCSKL.setColumnName("CSKL");
    kprCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    kprCSKL.setPrecision(12);
    kprCSKL.setTableName("KPR");
    kprCSKL.setServerColumnName("CSKL");
    kprCSKL.setSqlType(1);
    kprCSKL.setRowId(true);
    kpr.setResolver(dm.getQresolver());
    kpr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from KPR", null, true, Load.ALL));
 setColumns(new Column[] {kprLOKK, kprAKTIV, kprCSKL, kprRBR, kprGOD, kprKLJUC, kprOPIS, kprDATUM, kprZAD, kprRAZ});
  }

  public void setall() {

    ddl.create("KPR")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "N")
       .addChar("cskl", 12, true)
       .addInteger("rbr", 6, true)
       .addChar("god", 4, true)
       .addChar("kljuc", 20)
       .addChar("opis", 80)
       .addDate("datum")
       .addFloat("zad", 17, 2)
       .addFloat("raz", 17, 2)
       .addPrimaryKey("cskl,rbr,god");


    Naziv = "KPR";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"rbr", "kljuc", "datum"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
