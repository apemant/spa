/****license*****************************************************************
**   file: Gkkumulativi.java
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



public class Gkkumulativi extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Gkkumulativi Gkkumulativiclass;

  QueryDataSet gkkum = new raDataSet();

  Column gkkumLOKK = new Column();
  Column gkkumAKTIV = new Column();
  Column gkkumKNJIG = new Column();
  Column gkkumCORG = new Column();
  Column gkkumBROJKONTA = new Column();
  Column gkkumGODMJ = new Column();
  Column gkkumID = new Column();
  Column gkkumIP = new Column();

  public static Gkkumulativi getDataModule() {
    if (Gkkumulativiclass == null) {
      Gkkumulativiclass = new Gkkumulativi();
    }
    return Gkkumulativiclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return gkkum;
  }

  public Gkkumulativi() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    gkkumLOKK.setCaption("Status zauzetosti");
    gkkumLOKK.setColumnName("LOKK");
    gkkumLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkkumLOKK.setPrecision(1);
    gkkumLOKK.setTableName("GKKUMULATIVI");
    gkkumLOKK.setServerColumnName("LOKK");
    gkkumLOKK.setSqlType(1);
    gkkumLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    gkkumLOKK.setDefault("N");
    gkkumAKTIV.setCaption("Aktivan - neaktivan");
    gkkumAKTIV.setColumnName("AKTIV");
    gkkumAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkkumAKTIV.setPrecision(1);
    gkkumAKTIV.setTableName("GKKUMULATIVI");
    gkkumAKTIV.setServerColumnName("AKTIV");
    gkkumAKTIV.setSqlType(1);
    gkkumAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    gkkumAKTIV.setDefault("D");
    gkkumKNJIG.setCaption("Knjigovodstvo");
    gkkumKNJIG.setColumnName("KNJIG");
    gkkumKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkkumKNJIG.setPrecision(12);
    gkkumKNJIG.setTableName("GKKUMULATIVI");
    gkkumKNJIG.setServerColumnName("KNJIG");
    gkkumKNJIG.setSqlType(1);
    gkkumCORG.setCaption("Org. jedinica");
    gkkumCORG.setColumnName("CORG");
    gkkumCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkkumCORG.setPrecision(12);
    gkkumCORG.setRowId(true);
    gkkumCORG.setTableName("GKKUMULATIVI");
    gkkumCORG.setServerColumnName("CORG");
    gkkumCORG.setSqlType(1);
    gkkumBROJKONTA.setCaption("Konto");
    gkkumBROJKONTA.setColumnName("BROJKONTA");
    gkkumBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkkumBROJKONTA.setPrecision(8);
    gkkumBROJKONTA.setRowId(true);
    gkkumBROJKONTA.setTableName("GKKUMULATIVI");
    gkkumBROJKONTA.setServerColumnName("BROJKONTA");
    gkkumBROJKONTA.setSqlType(1);
    gkkumGODMJ.setCaption("Godina + mjesec");
    gkkumGODMJ.setColumnName("GODMJ");
    gkkumGODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkkumGODMJ.setPrecision(6);
    gkkumGODMJ.setRowId(true);
    gkkumGODMJ.setTableName("GKKUMULATIVI");
    gkkumGODMJ.setServerColumnName("GODMJ");
    gkkumGODMJ.setSqlType(1);
    gkkumID.setCaption("Iznos duguje");
    gkkumID.setColumnName("ID");
    gkkumID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkkumID.setPrecision(17);
    gkkumID.setScale(2);
    gkkumID.setDisplayMask("###,###,##0.00");
    gkkumID.setDefault("0");
    gkkumID.setTableName("GKKUMULATIVI");
    gkkumID.setServerColumnName("ID");
    gkkumID.setSqlType(2);
    gkkumID.setDefault("0");
    gkkumIP.setCaption("Iznos potražuje");
    gkkumIP.setColumnName("IP");
    gkkumIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkkumIP.setPrecision(17);
    gkkumIP.setScale(2);
    gkkumIP.setDisplayMask("###,###,##0.00");
    gkkumIP.setDefault("0");
    gkkumIP.setTableName("GKKUMULATIVI");
    gkkumIP.setServerColumnName("IP");
    gkkumIP.setSqlType(2);
    gkkumIP.setDefault("0");
    gkkum.setResolver(dm.getQresolver());
    gkkum.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Gkkumulativi", null, true, Load.ALL));
 setColumns(new Column[] {gkkumLOKK, gkkumAKTIV, gkkumKNJIG, gkkumCORG, gkkumBROJKONTA, gkkumGODMJ, gkkumID, gkkumIP});
  }

  public void setall() {

    ddl.create("Gkkumulativi")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12)
       .addChar("corg", 12, true)
       .addChar("brojkonta", 8, true)
       .addChar("godmj", 6, true)
       .addFloat("id", 17, 2)
       .addFloat("ip", 17, 2)
       .addPrimaryKey("corg,brojkonta,godmj");


    Naziv = "Gkkumulativi";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brojkonta"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
