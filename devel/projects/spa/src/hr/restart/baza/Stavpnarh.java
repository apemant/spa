/****license*****************************************************************
**   file: Stavpnarh.java
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



public class Stavpnarh extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Stavpnarh Stavpnarhclass;

  QueryDataSet spnarh = new QueryDataSet();

  Column spnarhLOKK = new Column();
  Column spnarhAKTIV = new Column();
  Column spnarhKNJIG = new Column();
  Column spnarhGODINA = new Column();
  Column spnarhBROJ = new Column();
  Column spnarhRBS = new Column();
  Column spnarhSTAVKA = new Column();
  Column spnarhCSKL = new Column();
  Column spnarhVRDOK = new Column();
  Column spnarhCZEMLJE = new Column();
  Column spnarhIZNOS = new Column();
  Column spnarhPVIZNOS = new Column();
  Column spnarhTECAJ = new Column();
  Column spnarhOZNVAL = new Column();
  Column spnarhODMJ = new Column();
  Column spnarhDOMJ = new Column();
  Column spnarhCPRIJSRED = new Column();
  Column spnarhBROJDNK = new Column();
  Column spnarhDATUMODL = new Column();
  Column spnarhDATUMDOL = new Column();
  Column spnarhVRIJODL = new Column();
  Column spnarhVRIJDOL = new Column();
  Column spnarhBROJSATI = new Column();
  Column spnarhINDPUTA = new Column();
  Column spnarhCPN = new Column();
  Column spnarhISPL = new Column();
  Column spnarhCNALOGA = new Column();

  public static Stavpnarh getDataModule() {
    if (Stavpnarhclass == null) {
      Stavpnarhclass = new Stavpnarh();
    }
    return Stavpnarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return spnarh;
  }

  public Stavpnarh() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    spnarhLOKK.setCaption("Status zauzetosti");
    spnarhLOKK.setColumnName("LOKK");
    spnarhLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhLOKK.setPrecision(1);
    spnarhLOKK.setTableName("STAVPNARH");
    spnarhLOKK.setServerColumnName("LOKK");
    spnarhLOKK.setSqlType(1);
    spnarhLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    spnarhLOKK.setDefault("N");
    spnarhAKTIV.setCaption("Aktivan - neaktivan");
    spnarhAKTIV.setColumnName("AKTIV");
    spnarhAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhAKTIV.setPrecision(1);
    spnarhAKTIV.setTableName("STAVPNARH");
    spnarhAKTIV.setServerColumnName("AKTIV");
    spnarhAKTIV.setSqlType(1);
    spnarhAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    spnarhAKTIV.setDefault("D");
    spnarhKNJIG.setCaption("Knjigovodstvo");
    spnarhKNJIG.setColumnName("KNJIG");
    spnarhKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhKNJIG.setPrecision(12);
    spnarhKNJIG.setRowId(true);
    spnarhKNJIG.setTableName("STAVPNARH");
    spnarhKNJIG.setServerColumnName("KNJIG");
    spnarhKNJIG.setSqlType(1);
    spnarhGODINA.setCaption("Godina");
    spnarhGODINA.setColumnName("GODINA");
    spnarhGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnarhGODINA.setPrecision(4);
    spnarhGODINA.setRowId(true);
    spnarhGODINA.setTableName("STAVPNARH");
    spnarhGODINA.setServerColumnName("GODINA");
    spnarhGODINA.setSqlType(5);
    spnarhGODINA.setWidth(4);
    spnarhBROJ.setCaption("Broj putnog naloga");
    spnarhBROJ.setColumnName("BROJ");
    spnarhBROJ.setDataType(com.borland.dx.dataset.Variant.INT);
    spnarhBROJ.setPrecision(6);
    spnarhBROJ.setRowId(true);
    spnarhBROJ.setTableName("STAVPNARH");
    spnarhBROJ.setServerColumnName("BROJ");
    spnarhBROJ.setSqlType(4);
    spnarhBROJ.setWidth(6);
    spnarhRBS.setCaption("Redni broj stavke");
    spnarhRBS.setColumnName("RBS");
    spnarhRBS.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnarhRBS.setPrecision(3);
    spnarhRBS.setRowId(true);
    spnarhRBS.setTableName("STAVPNARH");
    spnarhRBS.setServerColumnName("RBS");
    spnarhRBS.setSqlType(5);
    spnarhRBS.setWidth(3);
    spnarhSTAVKA.setCaption("Trošak");
    spnarhSTAVKA.setColumnName("STAVKA");
    spnarhSTAVKA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnarhSTAVKA.setPrecision(2);
    spnarhSTAVKA.setTableName("STAVPNARH");
    spnarhSTAVKA.setServerColumnName("STAVKA");
    spnarhSTAVKA.setSqlType(5);
    spnarhSTAVKA.setWidth(2);
    spnarhCSKL.setCaption("Vrsta stavke");
    spnarhCSKL.setColumnName("CSKL");
    spnarhCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhCSKL.setPrecision(12);
    spnarhCSKL.setTableName("STAVPNARH");
    spnarhCSKL.setServerColumnName("CSKL");
    spnarhCSKL.setSqlType(1);
    spnarhVRDOK.setCaption("Vrsta dokumenta");
    spnarhVRDOK.setColumnName("VRDOK");
    spnarhVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhVRDOK.setPrecision(3);
    spnarhVRDOK.setTableName("STAVPNARH");
    spnarhVRDOK.setServerColumnName("VRDOK");
    spnarhVRDOK.setSqlType(1);
    spnarhVRDOK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    spnarhCZEMLJE.setCaption("Zemlja");
    spnarhCZEMLJE.setColumnName("CZEMLJE");
    spnarhCZEMLJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhCZEMLJE.setPrecision(3);
    spnarhCZEMLJE.setTableName("STAVPNARH");
    spnarhCZEMLJE.setServerColumnName("CZEMLJE");
    spnarhCZEMLJE.setSqlType(1);
    spnarhIZNOS.setCaption("Iznos");
    spnarhIZNOS.setColumnName("IZNOS");
    spnarhIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    spnarhIZNOS.setPrecision(17);
    spnarhIZNOS.setScale(2);
    spnarhIZNOS.setDisplayMask("###,###,##0.00");
    spnarhIZNOS.setDefault("0");
    spnarhIZNOS.setTableName("STAVPNARH");
    spnarhIZNOS.setServerColumnName("IZNOS");
    spnarhIZNOS.setSqlType(2);
    spnarhIZNOS.setDefault("0");
    spnarhPVIZNOS.setCaption("Iznos u dom. val.");
    spnarhPVIZNOS.setColumnName("PVIZNOS");
    spnarhPVIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    spnarhPVIZNOS.setPrecision(17);
    spnarhPVIZNOS.setScale(2);
    spnarhPVIZNOS.setDisplayMask("###,###,##0.00");
    spnarhPVIZNOS.setDefault("0");
    spnarhPVIZNOS.setTableName("STAVPNARH");
    spnarhPVIZNOS.setServerColumnName("PVIZNOS");
    spnarhPVIZNOS.setSqlType(2);
    spnarhPVIZNOS.setDefault("0");
    spnarhTECAJ.setCaption("Te\u010Daj");
    spnarhTECAJ.setColumnName("TECAJ");
    spnarhTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    spnarhTECAJ.setPrecision(12);
    spnarhTECAJ.setScale(6);
    spnarhTECAJ.setDisplayMask("###,###,##0.000000");
    spnarhTECAJ.setDefault("0");
    spnarhTECAJ.setTableName("STAVPNARH");
    spnarhTECAJ.setServerColumnName("TECAJ");
    spnarhTECAJ.setSqlType(2);
    spnarhTECAJ.setDefault("1");
    spnarhOZNVAL.setCaption("Oznaka valute");
    spnarhOZNVAL.setColumnName("OZNVAL");
    spnarhOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhOZNVAL.setPrecision(3);
    spnarhOZNVAL.setTableName("STAVPNARH");
    spnarhOZNVAL.setServerColumnName("OZNVAL");
    spnarhOZNVAL.setSqlType(1);
    spnarhODMJ.setCaption("Od mjesta");
    spnarhODMJ.setColumnName("ODMJ");
    spnarhODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhODMJ.setPrecision(30);
    spnarhODMJ.setTableName("STAVPNARH");
    spnarhODMJ.setServerColumnName("ODMJ");
    spnarhODMJ.setSqlType(1);
    spnarhODMJ.setWidth(30);
    spnarhDOMJ.setCaption("Do mjesta");
    spnarhDOMJ.setColumnName("DOMJ");
    spnarhDOMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhDOMJ.setPrecision(30);
    spnarhDOMJ.setTableName("STAVPNARH");
    spnarhDOMJ.setServerColumnName("DOMJ");
    spnarhDOMJ.setSqlType(1);
    spnarhDOMJ.setWidth(30);
    spnarhCPRIJSRED.setCaption("Prijevozno sredstvo");
    spnarhCPRIJSRED.setColumnName("CPRIJSRED");
    spnarhCPRIJSRED.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhCPRIJSRED.setPrecision(5);
    spnarhCPRIJSRED.setTableName("STAVPNARH");
    spnarhCPRIJSRED.setServerColumnName("CPRIJSRED");
    spnarhCPRIJSRED.setSqlType(1);
    spnarhBROJDNK.setCaption("Dnevnica / no\u0107enja / kilometara");
    spnarhBROJDNK.setColumnName("BROJDNK");
    spnarhBROJDNK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    spnarhBROJDNK.setPrecision(17);
    spnarhBROJDNK.setScale(2);
    spnarhBROJDNK.setDisplayMask("###,###,##0.00");
    spnarhBROJDNK.setDefault("0");
    spnarhBROJDNK.setTableName("STAVPNARH");
    spnarhBROJDNK.setServerColumnName("BROJDNK");
    spnarhBROJDNK.setSqlType(2);
    spnarhDATUMODL.setCaption("Datum odlaska");
    spnarhDATUMODL.setColumnName("DATUMODL");
    spnarhDATUMODL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    spnarhDATUMODL.setPrecision(8);
    spnarhDATUMODL.setDisplayMask("dd-MM-yyyy");
//    spnarhDATUMODL.setEditMask("dd-MM-yyyy");
    spnarhDATUMODL.setTableName("STAVPNARH");
    spnarhDATUMODL.setServerColumnName("DATUMODL");
    spnarhDATUMODL.setSqlType(93);
    spnarhDATUMODL.setWidth(10);
    spnarhDATUMDOL.setCaption("Datum dolaska");
    spnarhDATUMDOL.setColumnName("DATUMDOL");
    spnarhDATUMDOL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    spnarhDATUMDOL.setPrecision(8);
    spnarhDATUMDOL.setDisplayMask("dd-MM-yyyy");
//    spnarhDATUMDOL.setEditMask("dd-MM-yyyy");
    spnarhDATUMDOL.setTableName("STAVPNARH");
    spnarhDATUMDOL.setServerColumnName("DATUMDOL");
    spnarhDATUMDOL.setSqlType(93);
    spnarhDATUMDOL.setWidth(10);
    spnarhVRIJODL.setCaption("Vrijeme odlaska");
    spnarhVRIJODL.setColumnName("VRIJODL");
    spnarhVRIJODL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnarhVRIJODL.setPrecision(4);
    spnarhVRIJODL.setTableName("STAVPNARH");
    spnarhVRIJODL.setServerColumnName("VRIJODL");
    spnarhVRIJODL.setSqlType(5);
    spnarhVRIJODL.setWidth(4);
    spnarhVRIJDOL.setCaption("Vrijeme dolaska");
    spnarhVRIJDOL.setColumnName("VRIJDOL");
    spnarhVRIJDOL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    spnarhVRIJDOL.setPrecision(4);
    spnarhVRIJDOL.setTableName("STAVPNARH");
    spnarhVRIJDOL.setServerColumnName("VRIJDOL");
    spnarhVRIJDOL.setSqlType(5);
    spnarhVRIJDOL.setWidth(4);
    spnarhBROJSATI.setCaption("Broj sati");
    spnarhBROJSATI.setColumnName("BROJSATI");
    spnarhBROJSATI.setDataType(com.borland.dx.dataset.Variant.INT);
    spnarhBROJSATI.setPrecision(6);
    spnarhBROJSATI.setTableName("STAVPNARH");
    spnarhBROJSATI.setServerColumnName("BROJSATI");
    spnarhBROJSATI.setSqlType(4);
    spnarhBROJSATI.setWidth(6);
    spnarhINDPUTA.setCaption("Indikator puta");
    spnarhINDPUTA.setColumnName("INDPUTA");
    spnarhINDPUTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhINDPUTA.setPrecision(1);
    spnarhINDPUTA.setRowId(true);
    spnarhINDPUTA.setTableName("STAVPNARH");
    spnarhINDPUTA.setServerColumnName("INDPUTA");
    spnarhINDPUTA.setSqlType(1);
    spnarhINDPUTA.setDefault("Z");
    spnarhCPN.setCaption("Broj naloga");
    spnarhCPN.setColumnName("CPN");
    spnarhCPN.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhCPN.setPrecision(24);
    spnarhCPN.setTableName("STAVPNARH");
    spnarhCPN.setServerColumnName("CPN");
    spnarhCPN.setSqlType(1);
    spnarhISPL.setCaption("Ispla\u0107eno");
    spnarhISPL.setColumnName("ISPL");
    spnarhISPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhISPL.setPrecision(1);
    spnarhISPL.setTableName("STAVPNARH");
    spnarhISPL.setServerColumnName("ISPL");
    spnarhISPL.setSqlType(1);
    spnarhISPL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    spnarhISPL.setDefault("N");
    spnarhCNALOGA.setCaption("Oznaka naloga u GK");
    spnarhCNALOGA.setColumnName("CNALOGA");
    spnarhCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    spnarhCNALOGA.setPrecision(30);
    spnarhCNALOGA.setTableName("STAVPNARH");
    spnarhCNALOGA.setServerColumnName("CNALOGA");
    spnarhCNALOGA.setSqlType(1);
    spnarhCNALOGA.setWidth(30);
    spnarh.setResolver(dm.getQresolver());
    spnarh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Stavpnarh", null, true, Load.ALL));
 setColumns(new Column[] {spnarhLOKK, spnarhAKTIV, spnarhKNJIG, spnarhGODINA, spnarhBROJ, spnarhRBS, spnarhSTAVKA, spnarhCSKL, spnarhVRDOK,
        spnarhCZEMLJE, spnarhIZNOS, spnarhPVIZNOS, spnarhTECAJ, spnarhOZNVAL, spnarhODMJ, spnarhDOMJ, spnarhCPRIJSRED, spnarhBROJDNK, spnarhDATUMODL,
        spnarhDATUMDOL, spnarhVRIJODL, spnarhVRIJDOL, spnarhBROJSATI, spnarhINDPUTA, spnarhCPN, spnarhISPL, spnarhCNALOGA});
  }

  public void setall() {

    ddl.create("Stavpnarh")
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
       .addChar("cnaloga", 30)
       .addPrimaryKey("knjig,godina,broj,rbs,indputa");


    Naziv = "Stavpnarh";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"broj", "rbs"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
