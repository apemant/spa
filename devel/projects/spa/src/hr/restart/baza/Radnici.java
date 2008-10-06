/****license*****************************************************************
**   file: Radnici.java
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



public class Radnici extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Radnici Radniciclass;

  QueryDataSet radnici = new raDataSet();
  QueryDataSet radniciaktiv = new raDataSet();

  Column radniciLOKK = new Column();
  Column radniciAKTIV = new Column();
  Column radniciCRADNIK = new Column();
  Column radniciIME = new Column();
  Column radniciPREZIME = new Column();
  Column radniciIMEOCA = new Column();
  Column radniciTITULA = new Column();
  Column radniciCORG = new Column();

  public static Radnici getDataModule() {
    if (Radniciclass == null) {
      Radniciclass = new Radnici();
    }
    return Radniciclass;
  }

  public QueryDataSet getQueryDataSet() {
    return radnici;
  }

  public QueryDataSet getAktiv() {
    return radniciaktiv;
  }

  public Radnici() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    radniciLOKK.setCaption("Status zauzetosti");
    radniciLOKK.setColumnName("LOKK");
    radniciLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    radniciLOKK.setPrecision(1);
    radniciLOKK.setTableName("RADNICI");
    radniciLOKK.setServerColumnName("LOKK");
    radniciLOKK.setSqlType(1);
    radniciLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    radniciLOKK.setDefault("N");
    radniciAKTIV.setCaption("Aktivan - neaktivan");
    radniciAKTIV.setColumnName("AKTIV");
    radniciAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    radniciAKTIV.setPrecision(1);
    radniciAKTIV.setTableName("RADNICI");
    radniciAKTIV.setServerColumnName("AKTIV");
    radniciAKTIV.setSqlType(1);
    radniciAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    radniciAKTIV.setDefault("D");
    radniciCRADNIK.setCaption("Mati\u010Dni broj");
    radniciCRADNIK.setColumnName("CRADNIK");
    radniciCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    radniciCRADNIK.setPrecision(6);
    radniciCRADNIK.setRowId(true);
    radniciCRADNIK.setTableName("RADNICI");
    radniciCRADNIK.setServerColumnName("CRADNIK");
    radniciCRADNIK.setSqlType(1);
    radniciIME.setCaption("Ime");
    radniciIME.setColumnName("IME");
    radniciIME.setDataType(com.borland.dx.dataset.Variant.STRING);
    radniciIME.setPrecision(50);
    radniciIME.setTableName("RADNICI");
    radniciIME.setServerColumnName("IME");
    radniciIME.setSqlType(1);
    radniciPREZIME.setCaption("Prezime");
    radniciPREZIME.setColumnName("PREZIME");
    radniciPREZIME.setDataType(com.borland.dx.dataset.Variant.STRING);
    radniciPREZIME.setPrecision(50);
    radniciPREZIME.setTableName("RADNICI");
    radniciPREZIME.setServerColumnName("PREZIME");
    radniciPREZIME.setSqlType(1);
    radniciIMEOCA.setCaption("Ime oca");
    radniciIMEOCA.setColumnName("IMEOCA");
    radniciIMEOCA.setDataType(com.borland.dx.dataset.Variant.STRING);
    radniciIMEOCA.setPrecision(50);
    radniciIMEOCA.setTableName("RADNICI");
    radniciIMEOCA.setServerColumnName("IMEOCA");
    radniciIMEOCA.setSqlType(1);
    radniciTITULA.setCaption("Titula");
    radniciTITULA.setColumnName("TITULA");
    radniciTITULA.setDataType(com.borland.dx.dataset.Variant.STRING);
    radniciTITULA.setPrecision(15);
    radniciTITULA.setTableName("RADNICI");
    radniciTITULA.setServerColumnName("TITULA");
    radniciTITULA.setSqlType(1);
    radniciCORG.setCaption("Org. jedinica");
    radniciCORG.setColumnName("CORG");
    radniciCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    radniciCORG.setPrecision(12);
    radniciCORG.setTableName("RADNICI");
    radniciCORG.setServerColumnName("CORG");
    radniciCORG.setSqlType(1);
    radnici.setResolver(dm.getQresolver());
    radnici.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Radnici", null, true, Load.ALL));
 setColumns(new Column[] {radniciLOKK, radniciAKTIV, radniciCRADNIK, radniciIME, radniciPREZIME, radniciIMEOCA, radniciTITULA, radniciCORG});

    createFilteredDataSet(radniciaktiv, "aktiv='D'");
  }

  public void setall() {

    ddl.create("Radnici")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cradnik", 6, true)
       .addChar("ime", 50)
       .addChar("prezime", 50)
       .addChar("imeoca", 50)
       .addChar("titula", 15)
       .addChar("corg", 12)
       .addPrimaryKey("cradnik");


    Naziv = "Radnici";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
