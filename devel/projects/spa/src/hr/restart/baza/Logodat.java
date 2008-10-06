/****license*****************************************************************
**   file: Logodat.java
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



public class Logodat extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Logodat Logodatclass;

  QueryDataSet ld = new raDataSet();
  QueryDataSet ldcustom = new raDataSet();

  Column ldCORG = new Column();
  Column ldVRSTA = new Column();
  Column ldVRSEC = new Column();
  Column ldVRDOK = new Column();
  Column ldRBR = new Column();
  Column ldORIENT = new Column();
  Column ldTIP = new Column();
  Column ldHPOS = new Column();
  Column ldVPOS = new Column();
  Column ldSIRINA = new Column();
  Column ldVISINA = new Column();
  Column ldTEKST = new Column();
  Column ldALIGN = new Column();
  Column ldFONT = new Column();

  public static Logodat getDataModule() {
    if (Logodatclass == null) {
      Logodatclass = new Logodat();
    }
    return Logodatclass;
  }

  public QueryDataSet getQueryDataSet() {
    return ld;
  }

  public QueryDataSet getCustom() {
    return ldcustom;
  }

  public Logodat() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    ldCORG.setCaption("Org. jedinica");
    ldCORG.setColumnName("CORG");
    ldCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldCORG.setPrecision(12);
    ldCORG.setRowId(true);
    ldCORG.setTableName("LOGODAT");
    ldCORG.setServerColumnName("CORG");
    ldCORG.setSqlType(1);
    ldVRSTA.setCaption("Podvrsta");
    ldVRSTA.setColumnName("VRSTA");
    ldVRSTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldVRSTA.setPrecision(1);
    ldVRSTA.setRowId(true);
    ldVRSTA.setTableName("LOGODAT");
    ldVRSTA.setServerColumnName("VRSTA");
    ldVRSTA.setSqlType(1);
    ldVRSTA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ldVRSEC.setCaption("Dio izvještaja");
    ldVRSEC.setColumnName("VRSEC");
    ldVRSEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldVRSEC.setPrecision(3);
    ldVRSEC.setRowId(true);
    ldVRSEC.setTableName("LOGODAT");
    ldVRSEC.setServerColumnName("VRSEC");
    ldVRSEC.setSqlType(1);
    ldVRDOK.setCaption("Vrsta dokumenta");
    ldVRDOK.setColumnName("VRDOK");
    ldVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldVRDOK.setPrecision(3);
    ldVRDOK.setRowId(true);
    ldVRDOK.setTableName("LOGODAT");
    ldVRDOK.setServerColumnName("VRDOK");
    ldVRDOK.setSqlType(1);
    ldRBR.setCaption("Rbr");
    ldRBR.setColumnName("RBR");
    ldRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    ldRBR.setPrecision(4);
    ldRBR.setRowId(true);
    ldRBR.setTableName("LOGODAT");
    ldRBR.setServerColumnName("RBR");
    ldRBR.setSqlType(5);
    ldRBR.setWidth(4);
    ldORIENT.setCaption("Orijentacija");
    ldORIENT.setColumnName("ORIENT");
    ldORIENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldORIENT.setPrecision(1);
    ldORIENT.setTableName("LOGODAT");
    ldORIENT.setServerColumnName("ORIENT");
    ldORIENT.setSqlType(1);
    ldORIENT.setDefault("P");
    ldTIP.setCaption("Tip");
    ldTIP.setColumnName("TIP");
    ldTIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldTIP.setPrecision(1);
    ldTIP.setTableName("LOGODAT");
    ldTIP.setServerColumnName("TIP");
    ldTIP.setSqlType(1);
    ldTIP.setDefault("T");
    ldHPOS.setCaption("Horizontalno");
    ldHPOS.setColumnName("HPOS");
    ldHPOS.setDataType(com.borland.dx.dataset.Variant.INT);
    ldHPOS.setPrecision(8);
    ldHPOS.setTableName("LOGODAT");
    ldHPOS.setServerColumnName("HPOS");
    ldHPOS.setSqlType(4);
    ldHPOS.setWidth(8);
    ldVPOS.setCaption("Vertikalno");
    ldVPOS.setColumnName("VPOS");
    ldVPOS.setDataType(com.borland.dx.dataset.Variant.INT);
    ldVPOS.setPrecision(8);
    ldVPOS.setTableName("LOGODAT");
    ldVPOS.setServerColumnName("VPOS");
    ldVPOS.setSqlType(4);
    ldVPOS.setWidth(8);
    ldSIRINA.setCaption("Širina");
    ldSIRINA.setColumnName("SIRINA");
    ldSIRINA.setDataType(com.borland.dx.dataset.Variant.INT);
    ldSIRINA.setPrecision(8);
    ldSIRINA.setTableName("LOGODAT");
    ldSIRINA.setServerColumnName("SIRINA");
    ldSIRINA.setSqlType(4);
    ldSIRINA.setWidth(8);
    ldVISINA.setCaption("Visina");
    ldVISINA.setColumnName("VISINA");
    ldVISINA.setDataType(com.borland.dx.dataset.Variant.INT);
    ldVISINA.setPrecision(8);
    ldVISINA.setTableName("LOGODAT");
    ldVISINA.setServerColumnName("VISINA");
    ldVISINA.setSqlType(4);
    ldVISINA.setWidth(8);
    ldTEKST.setCaption("Tekst");
    ldTEKST.setColumnName("TEKST");
    ldTEKST.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldTEKST.setPrecision(200);
    ldTEKST.setTableName("LOGODAT");
    ldTEKST.setServerColumnName("TEKST");
    ldTEKST.setSqlType(1);
    ldTEKST.setWidth(30);
    ldALIGN.setCaption("Poravnanje");
    ldALIGN.setColumnName("ALIGN");
    ldALIGN.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldALIGN.setPrecision(1);
    ldALIGN.setTableName("LOGODAT");
    ldALIGN.setServerColumnName("ALIGN");
    ldALIGN.setSqlType(1);
    ldALIGN.setDefault("L");
    ldFONT.setCaption("Font");
    ldFONT.setColumnName("FONT");
    ldFONT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ldFONT.setPrecision(40);
    ldFONT.setTableName("LOGODAT");
    ldFONT.setServerColumnName("FONT");
    ldFONT.setSqlType(1);
    ldFONT.setWidth(30);
    ld.setResolver(dm.getQresolver());
    ld.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Logodat", null, true, Load.ALL));
 setColumns(new Column[] {ldCORG, ldVRSTA, ldVRSEC, ldVRDOK, ldRBR, ldORIENT, ldTIP, ldHPOS, ldVPOS, ldSIRINA, ldVISINA, ldTEKST, ldALIGN, ldFONT});

    createFilteredDataSet(ldcustom, "vrsta='C' AND rbr=0");
  }

  public void setall() {

    ddl.create("Logodat")
       .addChar("corg", 12, true)
       .addChar("vrsta", 1, true)
       .addChar("vrsec", 3, true)
       .addChar("vrdok", 3, true)
       .addShort("rbr", 4, true)
       .addChar("orient", 1, "P")
       .addChar("tip", 1, "T")
       .addInteger("hpos", 8)
       .addInteger("vpos", 8)
       .addInteger("sirina", 8)
       .addInteger("visina", 8)
       .addChar("tekst", 200)
       .addChar("align", 1, "L")
       .addChar("font", 40)
       .addPrimaryKey("corg,vrsta,vrsec,vrdok,rbr");


    Naziv = "Logodat";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
