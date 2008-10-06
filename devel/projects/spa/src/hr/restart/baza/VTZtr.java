/****license*****************************************************************
**   file: VTZtr.java
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



public class VTZtr extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static VTZtr VTZtrclass;

  QueryDataSet vtztr = new QueryDataSet();

  Column vtztrCSKL = new Column();
  Column vtztrVRDOK = new Column();
  Column vtztrGOD = new Column();
  Column vtztrBRDOK = new Column();
  Column vtztrRBR = new Column();
  Column vtztrLRBR = new Column();
  Column vtztrCZT = new Column();
  Column vtztrCPAR = new Column();
  Column vtztrIZT = new Column();
  Column vtztrPZT = new Column();
  Column vtztrPRPOR = new Column();
  Column vtztrBRRAC = new Column();
  Column vtztrDATRAC = new Column();
  Column vtztrULDOK = new Column();
  Column vtztrDATDOSP = new Column();

  public static VTZtr getDataModule() {
    if (VTZtrclass == null) {
      VTZtrclass = new VTZtr();
    }
    return VTZtrclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vtztr;
  }

  public VTZtr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vtztrCSKL.setCaption("Skladište");
    vtztrCSKL.setColumnName("CSKL");
    vtztrCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtztrCSKL.setPrecision(12);
    vtztrCSKL.setRowId(true);
    vtztrCSKL.setTableName("VTZTR");
    vtztrCSKL.setServerColumnName("CSKL");
    vtztrCSKL.setSqlType(1);
    vtztrVRDOK.setCaption("Vrsta dokumenta");
    vtztrVRDOK.setColumnName("VRDOK");
    vtztrVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtztrVRDOK.setPrecision(3);
    vtztrVRDOK.setRowId(true);
    vtztrVRDOK.setTableName("VTZTR");
    vtztrVRDOK.setServerColumnName("VRDOK");
    vtztrVRDOK.setSqlType(1);
    vtztrGOD.setCaption("Godina");
    vtztrGOD.setColumnName("GOD");
    vtztrGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtztrGOD.setPrecision(4);
    vtztrGOD.setRowId(true);
    vtztrGOD.setTableName("VTZTR");
    vtztrGOD.setServerColumnName("GOD");
    vtztrGOD.setSqlType(1);
    vtztrBRDOK.setCaption("Broj dokumenta");
    vtztrBRDOK.setColumnName("BRDOK");
    vtztrBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    vtztrBRDOK.setRowId(true);
    vtztrBRDOK.setTableName("VTZTR");
    vtztrBRDOK.setServerColumnName("BRDOK");
    vtztrBRDOK.setSqlType(4);
    vtztrBRDOK.setWidth(6);
    vtztrRBR.setCaption("Rbr stavke dokumenta (0 - zaglavlje)");
    vtztrRBR.setColumnName("RBR");
    vtztrRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtztrRBR.setRowId(true);
    vtztrRBR.setTableName("VTZTR");
    vtztrRBR.setServerColumnName("RBR");
    vtztrRBR.setSqlType(5);
    vtztrRBR.setWidth(4);
    vtztrLRBR.setCaption("Rbr stavke troškova");
    vtztrLRBR.setColumnName("LRBR");
    vtztrLRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtztrLRBR.setRowId(true);
    vtztrLRBR.setTableName("VTZTR");
    vtztrLRBR.setServerColumnName("LRBR");
    vtztrLRBR.setSqlType(5);
    vtztrLRBR.setWidth(4);
    vtztrCZT.setCaption("Zavisni trošak");
    vtztrCZT.setColumnName("CZT");
    vtztrCZT.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtztrCZT.setTableName("VTZTR");
    vtztrCZT.setServerColumnName("CZT");
    vtztrCZT.setSqlType(5);
    vtztrCZT.setWidth(2);
    vtztrCPAR.setCaption("Partner");
    vtztrCPAR.setColumnName("CPAR");
    vtztrCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    vtztrCPAR.setTableName("VTZTR");
    vtztrCPAR.setServerColumnName("CPAR");
    vtztrCPAR.setSqlType(4);
    vtztrCPAR.setWidth(6);
    vtztrIZT.setCaption("Iznos troškova stavke");
    vtztrIZT.setColumnName("IZT");
    vtztrIZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtztrIZT.setPrecision(17);
    vtztrIZT.setScale(2);
    vtztrIZT.setDisplayMask("###,###,##0.00");
    vtztrIZT.setDefault("0");
    vtztrIZT.setTableName("VTZTR");
    vtztrIZT.setServerColumnName("IZT");
    vtztrIZT.setSqlType(2);
    vtztrPZT.setCaption("Postotno optere\u0107enje");
    vtztrPZT.setColumnName("PZT");
    vtztrPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtztrPZT.setPrecision(17);
    vtztrPZT.setScale(7);
    vtztrPZT.setDisplayMask("###,###,##0.00");
    vtztrPZT.setDefault("0");
    vtztrPZT.setTableName("VTZTR");
    vtztrPZT.setServerColumnName("PZT");
    vtztrPZT.setSqlType(2);
    vtztrPRPOR.setCaption("Iznos pretporeza");
    vtztrPRPOR.setColumnName("PRPOR");
    vtztrPRPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtztrPRPOR.setPrecision(17);
    vtztrPRPOR.setScale(2);
    vtztrPRPOR.setDisplayMask("###,###,##0.00");
    vtztrPRPOR.setDefault("0");
    vtztrPRPOR.setTableName("VTZTR");
    vtztrPRPOR.setServerColumnName("PRPOR");
    vtztrPRPOR.setSqlType(2);
    vtztrBRRAC.setCaption("Broj ra\u010Duna");
    vtztrBRRAC.setColumnName("BRRAC");
    vtztrBRRAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtztrBRRAC.setPrecision(20);
    vtztrBRRAC.setTableName("VTZTR");
    vtztrBRRAC.setServerColumnName("BRRAC");
    vtztrBRRAC.setSqlType(1);
    vtztrDATRAC.setCaption("Datum ra\u010Duna");
    vtztrDATRAC.setColumnName("DATRAC");
    vtztrDATRAC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    vtztrDATRAC.setDisplayMask("dd-MM-yyyy");
//    vtztrDATRAC.setEditMask("dd-MM-yyyy");
    vtztrDATRAC.setTableName("VTZTR");
    vtztrDATRAC.setServerColumnName("DATRAC");
    vtztrDATRAC.setSqlType(93);
    vtztrDATRAC.setWidth(10);
    vtztrULDOK.setCaption("Ulazni dokument");
    vtztrULDOK.setColumnName("ULDOK");
    vtztrULDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtztrULDOK.setPrecision(20);
    vtztrULDOK.setTableName("VTZTR");
    vtztrULDOK.setServerColumnName("ULDOK");
    vtztrULDOK.setSqlType(1);
    vtztrDATDOSP.setCaption("Datum dospje\u0107a");
    vtztrDATDOSP.setColumnName("DATDOSP");
    vtztrDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    vtztrDATDOSP.setDisplayMask("dd-MM-yyyy");
//    vtztrDATDOSP.setEditMask("dd-MM-yyyy");
    vtztrDATDOSP.setTableName("VTZTR");
    vtztrDATDOSP.setServerColumnName("DATDOSP");
    vtztrDATDOSP.setSqlType(93);
    vtztrDATDOSP.setWidth(10);
    vtztr.setResolver(dm.getQresolver());
    vtztr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from VTZtr", null, true, Load.ALL));
    setColumns(new Column[] {vtztrCSKL, vtztrVRDOK, vtztrGOD, vtztrBRDOK, vtztrRBR, vtztrLRBR, vtztrCZT, vtztrCPAR, vtztrIZT, vtztrPZT, vtztrPRPOR, 
        vtztrBRRAC, vtztrDATRAC, vtztrULDOK, vtztrDATDOSP});
  }

  public void setall() {

    ddl.create("VTZtr")
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addShort("rbr", 4, true)
       .addShort("lrbr", 4, true)
       .addShort("czt", 2)
       .addInteger("cpar", 6)
       .addFloat("izt", 17, 2)
       .addFloat("pzt", 17, 7)
       .addFloat("prpor", 17, 2)
       .addChar("brrac", 20)
       .addDate("datrac")
       .addChar("uldok", 20)
       .addDate("datdosp")
       .addPrimaryKey("cskl,vrdok,god,brdok,rbr,lrbr");


    Naziv = "VTZtr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brdok"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
