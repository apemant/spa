/****license*****************************************************************
**   file: VTZtrt.java
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



public class VTZtrt extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static VTZtrt VTZtrtclass;

  QueryDataSet vtztrt = new QueryDataSet();

  Column vtztrtDODKEY = new Column();
  Column vtztrtLRBR = new Column();
  Column vtztrtCZT = new Column();
  Column vtztrtCPAR = new Column();
  Column vtztrtIZT = new Column();
  Column vtztrtPZT = new Column();
  Column vtztrtPRPOR = new Column();
  Column vtztrtBRRAC = new Column();
  Column vtztrtDATRAC = new Column();
  Column vtztrtULDOK = new Column();
  Column vtztrtDATDOSP = new Column();

  public static VTZtrt getDataModule() {
    if (VTZtrtclass == null) {
      VTZtrtclass = new VTZtrt();
    }
    return VTZtrtclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vtztrt;
  }

  public VTZtrt() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vtztrtDODKEY.setCaption("Privremeni klju\u010D");
    vtztrtDODKEY.setColumnName("DODKEY");
    vtztrtDODKEY.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtztrtDODKEY.setRowId(true);
    vtztrtDODKEY.setTableName("VTZTRT");
    vtztrtDODKEY.setServerColumnName("DODKEY");
    vtztrtDODKEY.setSqlType(5);
    vtztrtDODKEY.setWidth(4);
    vtztrtLRBR.setCaption("Rbr stavke troškova");
    vtztrtLRBR.setColumnName("LRBR");
    vtztrtLRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtztrtLRBR.setRowId(true);
    vtztrtLRBR.setTableName("VTZTRT");
    vtztrtLRBR.setServerColumnName("LRBR");
    vtztrtLRBR.setSqlType(5);
    vtztrtLRBR.setWidth(4);
    vtztrtCZT.setCaption("Zavisni trošak");
    vtztrtCZT.setColumnName("CZT");
    vtztrtCZT.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtztrtCZT.setTableName("VTZTRT");
    vtztrtCZT.setServerColumnName("CZT");
    vtztrtCZT.setSqlType(5);
    vtztrtCZT.setWidth(2);
    vtztrtCPAR.setCaption("Partner");
    vtztrtCPAR.setColumnName("CPAR");
    vtztrtCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    vtztrtCPAR.setTableName("VTZTRT");
    vtztrtCPAR.setServerColumnName("CPAR");
    vtztrtCPAR.setSqlType(4);
    vtztrtCPAR.setWidth(6);
    vtztrtIZT.setCaption("Iznos troškova stavke");
    vtztrtIZT.setColumnName("IZT");
    vtztrtIZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtztrtIZT.setPrecision(17);
    vtztrtIZT.setScale(2);
    vtztrtIZT.setDisplayMask("###,###,##0.00");
    vtztrtIZT.setDefault("0");
    vtztrtIZT.setTableName("VTZTRT");
    vtztrtIZT.setServerColumnName("IZT");
    vtztrtIZT.setSqlType(2);
    vtztrtPZT.setCaption("Postotno optere\u0107enje");
    vtztrtPZT.setColumnName("PZT");
    vtztrtPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtztrtPZT.setPrecision(17);
    vtztrtPZT.setScale(7);
    vtztrtPZT.setDisplayMask("###,###,##0.00");
    vtztrtPZT.setDefault("0");
    vtztrtPZT.setTableName("VTZTRT");
    vtztrtPZT.setServerColumnName("PZT");
    vtztrtPZT.setSqlType(2);
    vtztrtPRPOR.setCaption("Iznos pretporeza");
    vtztrtPRPOR.setColumnName("PRPOR");
    vtztrtPRPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtztrtPRPOR.setPrecision(17);
    vtztrtPRPOR.setScale(2);
    vtztrtPRPOR.setDisplayMask("###,###,##0.00");
    vtztrtPRPOR.setDefault("0");
    vtztrtPRPOR.setTableName("VTZTRT");
    vtztrtPRPOR.setServerColumnName("PRPOR");
    vtztrtPRPOR.setSqlType(2);
    vtztrtBRRAC.setCaption("Broj ra\u010Duna");
    vtztrtBRRAC.setColumnName("BRRAC");
    vtztrtBRRAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtztrtBRRAC.setPrecision(20);
    vtztrtBRRAC.setTableName("VTZTRT");
    vtztrtBRRAC.setServerColumnName("BRRAC");
    vtztrtBRRAC.setSqlType(1);
    vtztrtDATRAC.setCaption("Datum ra\u010Duna");
    vtztrtDATRAC.setColumnName("DATRAC");
    vtztrtDATRAC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    vtztrtDATRAC.setDisplayMask("dd-MM-yyyy");
//    vtztrtDATRAC.setEditMask("dd-MM-yyyy");
    vtztrtDATRAC.setTableName("VTZTRT");
    vtztrtDATRAC.setServerColumnName("DATRAC");
    vtztrtDATRAC.setSqlType(93);
    vtztrtDATRAC.setWidth(10);
    vtztrtULDOK.setCaption("Ulazni dokument");
    vtztrtULDOK.setColumnName("ULDOK");
    vtztrtULDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtztrtULDOK.setPrecision(20);
    vtztrtULDOK.setTableName("VTZTRT");
    vtztrtULDOK.setServerColumnName("ULDOK");
    vtztrtULDOK.setSqlType(1);
    vtztrtDATDOSP.setCaption("Datum dospje\u0107a");
    vtztrtDATDOSP.setColumnName("DATDOSP");
    vtztrtDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    vtztrtDATDOSP.setDisplayMask("dd-MM-yyyy");
//    vtztrtDATDOSP.setEditMask("dd-MM-yyyy");
    vtztrtDATDOSP.setTableName("VTZTRT");
    vtztrtDATDOSP.setServerColumnName("DATDOSP");
    vtztrtDATDOSP.setSqlType(93);
    vtztrtDATDOSP.setWidth(10);
    vtztrt.setResolver(dm.getQresolver());
    vtztrt.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from VTZtrt", null, true, Load.ALL));
    setColumns(new Column[] {vtztrtDODKEY, vtztrtLRBR, vtztrtCZT, vtztrtCPAR, vtztrtIZT, vtztrtPZT, vtztrtPRPOR, vtztrtBRRAC, vtztrtDATRAC, vtztrtULDOK, 
        vtztrtDATDOSP});
  }

  public void setall() {

    ddl.create("VTZtrt")
       .addShort("dodkey", 4, true)
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
       .addPrimaryKey("dodkey,lrbr");


    Naziv = "VTZtrt";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
