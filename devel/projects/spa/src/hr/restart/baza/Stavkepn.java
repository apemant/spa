/****license*****************************************************************
**   file: Stavkepn.java
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



public class Stavkepn extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Stavkepn Stavkepnclass;

  QueryDataSet spn = new QueryDataSet();

  Column spnLOKK = new Column();
  Column spnAKTIV = new Column();
  Column spnKNJIG = new Column();
  Column spnGODINA = new Column();
  Column spnBROJ = new Column();
  Column spnRBS = new Column();
  Column spnSTAVKA = new Column();
  Column spnCSKL = new Column();
  Column spnVRDOK = new Column();
  Column spnCZEMLJE = new Column();
  Column spnIZNOS = new Column();
  Column spnPVIZNOS = new Column();
  Column spnTECAJ = new Column();
  Column spnOZNVAL = new Column();
  Column spnODMJ = new Column();
  Column spnDOMJ = new Column();
  Column spnCPRIJSRED = new Column();
  Column spnBROJDNK = new Column();
  Column spnDATUMODL = new Column();
  Column spnDATUMDOL = new Column();
  Column spnVRIJODL = new Column();
  Column spnVRIJDOL = new Column();
  Column spnBROJSATI = new Column();
  Column spnINDPUTA = new Column();
  Column spnCPN = new Column();
  Column spnISPL = new Column();

  public static Stavkepn getDataModule() {
    if (Stavkepnclass == null) {
      Stavkepnclass = new Stavkepn();
    }
    return Stavkepnclass;
  }

  public QueryDataSet getQueryDataSet() {
    return spn;
  }

  public Stavkepn() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    spnLOKK.setCaption("Status zauzetosti");
    spnLOKK.setColumnName("LOKK");
    spnLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnLOKK.setPrecision(1);
    spnLOKK.setTableName("STAVKEPN");
    spnLOKK.setServerColumnName("LOKK");
    spnLOKK.setSqlType(1);
    spnLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    spnLOKK.setDefault("N");
    spnAKTIV.setCaption("Aktivan - neaktivan");
    spnAKTIV.setColumnName("AKTIV");
    spnAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnAKTIV.setPrecision(1);
    spnAKTIV.setTableName("STAVKEPN");
    spnAKTIV.setServerColumnName("AKTIV");
    spnAKTIV.setSqlType(1);
    spnAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    spnAKTIV.setDefault("D");
    spnKNJIG.setCaption("Knjigovodstvo");
    spnKNJIG.setColumnName("KNJIG");
    spnKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnKNJIG.setPrecision(12);
    spnKNJIG.setRowId(true);
    spnKNJIG.setTableName("STAVKEPN");
    spnKNJIG.setServerColumnName("KNJIG");
    spnKNJIG.setSqlType(1);
    spnGODINA.setCaption("Godina");
    spnGODINA.setColumnName("GODINA");
    spnGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnGODINA.setPrecision(4);
    spnGODINA.setRowId(true);
    spnGODINA.setTableName("STAVKEPN");
    spnGODINA.setServerColumnName("GODINA");
    spnGODINA.setSqlType(5);
    spnGODINA.setWidth(4);
    spnBROJ.setCaption("Broj putnog naloga");
    spnBROJ.setColumnName("BROJ");
    spnBROJ.setDataType(com.borland.dx.dataset.Variant.INT);
    spnBROJ.setPrecision(6);
    spnBROJ.setRowId(true);
    spnBROJ.setTableName("STAVKEPN");
    spnBROJ.setServerColumnName("BROJ");
    spnBROJ.setSqlType(4);
    spnBROJ.setWidth(6);
    spnRBS.setCaption("RBR");
    spnRBS.setColumnName("RBS");
    spnRBS.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnRBS.setPrecision(3);
    spnRBS.setRowId(true);
    spnRBS.setTableName("STAVKEPN");
    spnRBS.setServerColumnName("RBS");
    spnRBS.setSqlType(5);
    spnRBS.setWidth(3);
    spnSTAVKA.setCaption("Trošak");
    spnSTAVKA.setColumnName("STAVKA");
    spnSTAVKA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnSTAVKA.setPrecision(2);
    spnSTAVKA.setTableName("STAVKEPN");
    spnSTAVKA.setServerColumnName("STAVKA");
    spnSTAVKA.setSqlType(5);
    spnSTAVKA.setWidth(2);
    spnCSKL.setCaption("Vrsta stavke");
    spnCSKL.setColumnName("CSKL");
    spnCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnCSKL.setPrecision(12);
    spnCSKL.setTableName("STAVKEPN");
    spnCSKL.setServerColumnName("CSKL");
    spnCSKL.setSqlType(1);
    spnVRDOK.setCaption("Vrsta dokumenta");
    spnVRDOK.setColumnName("VRDOK");
    spnVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnVRDOK.setPrecision(3);
    spnVRDOK.setTableName("STAVKEPN");
    spnVRDOK.setServerColumnName("VRDOK");
    spnVRDOK.setSqlType(1);
    spnVRDOK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    spnCZEMLJE.setCaption("Zemlja");
    spnCZEMLJE.setColumnName("CZEMLJE");
    spnCZEMLJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnCZEMLJE.setPrecision(3);
    spnCZEMLJE.setTableName("STAVKEPN");
    spnCZEMLJE.setServerColumnName("CZEMLJE");
    spnCZEMLJE.setSqlType(1);
    spnIZNOS.setCaption("Iznos");
    spnIZNOS.setColumnName("IZNOS");
    spnIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    spnIZNOS.setPrecision(17);
    spnIZNOS.setScale(2);
    spnIZNOS.setDisplayMask("###,###,##0.00");
    spnIZNOS.setDefault("0");
    spnIZNOS.setTableName("STAVKEPN");
    spnIZNOS.setServerColumnName("IZNOS");
    spnIZNOS.setSqlType(2);
    spnIZNOS.setDefault("0");
    spnPVIZNOS.setCaption("Iznos u dom. val.");
    spnPVIZNOS.setColumnName("PVIZNOS");
    spnPVIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    spnPVIZNOS.setPrecision(17);
    spnPVIZNOS.setScale(2);
    spnPVIZNOS.setDisplayMask("###,###,##0.00");
    spnPVIZNOS.setDefault("0");
    spnPVIZNOS.setTableName("STAVKEPN");
    spnPVIZNOS.setServerColumnName("PVIZNOS");
    spnPVIZNOS.setSqlType(2);
    spnPVIZNOS.setDefault("0");
    spnTECAJ.setCaption("Te\u010Daj");
    spnTECAJ.setColumnName("TECAJ");
    spnTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    spnTECAJ.setPrecision(12);
    spnTECAJ.setScale(6);
    spnTECAJ.setDisplayMask("###,###,##0.000000");
    spnTECAJ.setDefault("0");
    spnTECAJ.setTableName("STAVKEPN");
    spnTECAJ.setServerColumnName("TECAJ");
    spnTECAJ.setSqlType(2);
    spnTECAJ.setDefault("1");
    spnOZNVAL.setCaption("Oznaka valute");
    spnOZNVAL.setColumnName("OZNVAL");
    spnOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnOZNVAL.setPrecision(3);
    spnOZNVAL.setTableName("STAVKEPN");
    spnOZNVAL.setServerColumnName("OZNVAL");
    spnOZNVAL.setSqlType(1);
    spnODMJ.setCaption("Od mjesta");
    spnODMJ.setColumnName("ODMJ");
    spnODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnODMJ.setPrecision(30);
    spnODMJ.setTableName("STAVKEPN");
    spnODMJ.setServerColumnName("ODMJ");
    spnODMJ.setSqlType(1);
    spnODMJ.setWidth(30);
    spnDOMJ.setCaption("Do mjesta");
    spnDOMJ.setColumnName("DOMJ");
    spnDOMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnDOMJ.setPrecision(30);
    spnDOMJ.setTableName("STAVKEPN");
    spnDOMJ.setServerColumnName("DOMJ");
    spnDOMJ.setSqlType(1);
    spnDOMJ.setWidth(30);
    spnCPRIJSRED.setCaption("Prijevozno sredstvo");
    spnCPRIJSRED.setColumnName("CPRIJSRED");
    spnCPRIJSRED.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnCPRIJSRED.setPrecision(5);
    spnCPRIJSRED.setTableName("STAVKEPN");
    spnCPRIJSRED.setServerColumnName("CPRIJSRED");
    spnCPRIJSRED.setSqlType(1);
    spnBROJDNK.setCaption("Dnevnica / no\u0107enja / kilometara");
    spnBROJDNK.setColumnName("BROJDNK");
    spnBROJDNK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    spnBROJDNK.setPrecision(17);
    spnBROJDNK.setScale(2);
    spnBROJDNK.setDisplayMask("###,###,##0.00");
    spnBROJDNK.setDefault("0");
    spnBROJDNK.setTableName("STAVKEPN");
    spnBROJDNK.setServerColumnName("BROJDNK");
    spnBROJDNK.setSqlType(2);
    spnDATUMODL.setCaption("Datum odlaska");
    spnDATUMODL.setColumnName("DATUMODL");
    spnDATUMODL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    spnDATUMODL.setPrecision(8);
    spnDATUMODL.setDisplayMask("dd-MM-yyyy");
//    spnDATUMODL.setEditMask("dd-MM-yyyy");
    spnDATUMODL.setTableName("STAVKEPN");
    spnDATUMODL.setServerColumnName("DATUMODL");
    spnDATUMODL.setSqlType(93);
    spnDATUMODL.setWidth(10);
    spnDATUMDOL.setCaption("Datum dolaska");
    spnDATUMDOL.setColumnName("DATUMDOL");
    spnDATUMDOL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    spnDATUMDOL.setPrecision(8);
    spnDATUMDOL.setDisplayMask("dd-MM-yyyy");
//    spnDATUMDOL.setEditMask("dd-MM-yyyy");
    spnDATUMDOL.setTableName("STAVKEPN");
    spnDATUMDOL.setServerColumnName("DATUMDOL");
    spnDATUMDOL.setSqlType(93);
    spnDATUMDOL.setWidth(10);
    spnVRIJODL.setCaption("Odlazak");
    spnVRIJODL.setColumnName("VRIJODL");
    spnVRIJODL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnVRIJODL.setPrecision(4);
    spnVRIJODL.setTableName("STAVKEPN");
    spnVRIJODL.setServerColumnName("VRIJODL");
    spnVRIJODL.setSqlType(5);
    spnVRIJODL.setWidth(4);
    spnVRIJDOL.setCaption("Dolazak");
    spnVRIJDOL.setColumnName("VRIJDOL");
    spnVRIJDOL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnVRIJDOL.setPrecision(4);
    spnVRIJDOL.setTableName("STAVKEPN");
    spnVRIJDOL.setServerColumnName("VRIJDOL");
    spnVRIJDOL.setSqlType(5);
    spnVRIJDOL.setWidth(4);
    spnBROJSATI.setCaption("Broj sati");
    spnBROJSATI.setColumnName("BROJSATI");
    spnBROJSATI.setDataType(com.borland.dx.dataset.Variant.INT);
    spnBROJSATI.setPrecision(6);
    spnBROJSATI.setTableName("STAVKEPN");
    spnBROJSATI.setServerColumnName("BROJSATI");
    spnBROJSATI.setSqlType(4);
    spnBROJSATI.setWidth(6);
    spnINDPUTA.setCaption("Indikator puta");
    spnINDPUTA.setColumnName("INDPUTA");
    spnINDPUTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnINDPUTA.setPrecision(1);
    spnINDPUTA.setRowId(true);
    spnINDPUTA.setTableName("STAVKEPN");
    spnINDPUTA.setServerColumnName("INDPUTA");
    spnINDPUTA.setSqlType(1);
    spnINDPUTA.setDefault("Z");
    spnCPN.setCaption("Broj naloga");
    spnCPN.setColumnName("CPN");
    spnCPN.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnCPN.setPrecision(24);
    spnCPN.setTableName("STAVKEPN");
    spnCPN.setServerColumnName("CPN");
    spnCPN.setSqlType(1);
    spnISPL.setCaption("Ispla\u0107eno");
    spnISPL.setColumnName("ISPL");
    spnISPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnISPL.setPrecision(1);
    spnISPL.setTableName("STAVKEPN");
    spnISPL.setServerColumnName("ISPL");
    spnISPL.setSqlType(1);
    spnISPL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    spnISPL.setDefault("N");
    spn.setResolver(dm.getQresolver());
    spn.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Stavkepn", null, true, Load.ALL));
 setColumns(new Column[] {spnLOKK, spnAKTIV, spnKNJIG, spnGODINA, spnBROJ, spnRBS, spnSTAVKA, spnCSKL, spnVRDOK, spnCZEMLJE, spnIZNOS, spnPVIZNOS,
        spnTECAJ, spnOZNVAL, spnODMJ, spnDOMJ, spnCPRIJSRED, spnBROJDNK, spnDATUMODL, spnDATUMDOL, spnVRIJODL, spnVRIJDOL, spnBROJSATI, spnINDPUTA, spnCPN,
        spnISPL});
  }

  public void setall() {

    ddl.create("Stavkepn")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addShort("godina", 4, true)
       .addInteger("broj", 6, true)
       .addShort("rbs", 3, true)
       .addShort("stavka", 2)
       .addChar("cskl", 12)
       .addChar("vrdok", 3)
       .addChar("czemlje", 3)
       .addFloat("iznos", 17, 2)
       .addFloat("pviznos", 17, 2)
       .addFloat("tecaj", 12, 6)
       .addChar("oznval", 3)
       .addChar("odmj", 30)
       .addChar("domj", 30)
       .addChar("cprijsred", 5)
       .addFloat("brojdnk", 17, 2)
       .addDate("datumodl")
       .addDate("datumdol")
       .addShort("vrijodl", 4)
       .addShort("vrijdol", 4)
       .addInteger("brojsati", 6)
       .addChar("indputa", 1, "Z", true)
       .addChar("cpn", 24)
       .addChar("ispl", 1, "N")
       .addPrimaryKey("knjig,godina,broj,rbs,indputa");


    Naziv = "Stavkepn";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"broj", "rbs"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
