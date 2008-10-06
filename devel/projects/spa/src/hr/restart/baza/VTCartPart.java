/****license*****************************************************************
**   file: VTCartPart.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;



public class VTCartPart extends KreirDrop implements DataModule {

  //dM dm  = dM.getDataModule();
  private static VTCartPart VTCartPartclass;

  QueryDataSet vtcp = new QueryDataSet();
  QueryDataSet vtcpaktiv = new QueryDataSet();

/*  Column vtcpLOCK = new Column();
  Column vtcpAKTIV = new Column();
  Column vtcpCPAR = new Column();
  Column vtcpCART = new Column();
  Column vtcpCART1 = new Column();
  Column vtcpBC = new Column();
  Column vtcpNAZART = new Column();
  Column vtcpCCPAR = new Column();
  Column vtcpPNAZART = new Column();*/

  public static VTCartPart getDataModule() {
    if (VTCartPartclass == null) {
      VTCartPartclass = new VTCartPart();
    }
    return VTCartPartclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vtcp;
  }

  public QueryDataSet getAktiv() {
    return vtcpaktiv;
  }

  public VTCartPart() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    initModule();
    /*
    vtcpLOCK.setCaption("Status zauzetosti");
    vtcpLOCK.setColumnName("LOCK");
    vtcpLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtcpLOCK.setPrecision(1);
    vtcpLOCK.setTableName("VTCARTPART");
    vtcpLOCK.setServerColumnName("LOCK");
    vtcpLOCK.setSqlType(1);
    vtcpLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vtcpLOCK.setDefault("N");
    
    vtcpAKTIV.setCaption("Aktivan - neaktivan");
    vtcpAKTIV.setColumnName("AKTIV");
    vtcpAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtcpAKTIV.setPrecision(1);
    vtcpAKTIV.setTableName("VTCARTPART");
    vtcpAKTIV.setServerColumnName("AKTIV");
    vtcpAKTIV.setSqlType(1);
    vtcpAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vtcpAKTIV.setDefault("D");
    
    vtcpCPAR.setCaption("Partner");
    vtcpCPAR.setColumnName("CPAR");
    vtcpCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    vtcpCPAR.setPrecision(5);
    vtcpCPAR.setRowId(true);
    vtcpCPAR.setTableName("VTCARTPART");
    vtcpCPAR.setServerColumnName("CPAR");
    vtcpCPAR.setSqlType(4);
    vtcpCPAR.setWidth(5);
    
    vtcpCART.setCaption("Šifra");
    vtcpCART.setColumnName("CART");
    vtcpCART.setDataType(com.borland.dx.dataset.Variant.INT);
    vtcpCART.setPrecision(5);
    vtcpCART.setRowId(true);
    vtcpCART.setTableName("VTCARTPART");
    vtcpCART.setServerColumnName("CART");
    vtcpCART.setSqlType(4);
    vtcpCART.setWidth(5);
    
    vtcpCART1.setCaption("Oznaka");
    vtcpCART1.setColumnName("CART1");
    vtcpCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtcpCART1.setPrecision(20);
    vtcpCART1.setTableName("VTCARTPART");
    vtcpCART1.setServerColumnName("CART1");
    vtcpCART1.setSqlType(1);
    
    vtcpBC.setCaption("Barcode");
    vtcpBC.setColumnName("BC");
    vtcpBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtcpBC.setPrecision(20);
    vtcpBC.setTableName("VTCARTPART");
    vtcpBC.setServerColumnName("BC");
    vtcpBC.setSqlType(1);
    
    vtcpNAZART.setCaption("Naziv");
    vtcpNAZART.setColumnName("NAZART");
    vtcpNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtcpNAZART.setPrecision(50);
    vtcpNAZART.setTableName("VTCARTPART");
    vtcpNAZART.setServerColumnName("NAZART");
    vtcpNAZART.setSqlType(1);
    vtcpNAZART.setWidth(30);
    
    vtcpCCPAR.setCaption("Šifra artikla kupca");
    vtcpCCPAR.setColumnName("CCPAR");
    vtcpCCPAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtcpCCPAR.setPrecision(20);
    vtcpCCPAR.setTableName("VTCARTPART");
    vtcpCCPAR.setServerColumnName("CCPAR");
    vtcpCCPAR.setSqlType(1);
    
    vtcpPNAZART.setCaption("Naziv artikla kupca");
    vtcpPNAZART.setColumnName("PNAZART");
    vtcpPNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtcpPNAZART.setPrecision(50);
    vtcpPNAZART.setTableName("VTCARTPART");
    vtcpPNAZART.setServerColumnName("PNAZART");
    vtcpPNAZART.setSqlType(1);
    vtcpPNAZART.setWidth(30);
    
    vtcp.setResolver(dm.getQresolver());
    vtcp.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from VTCartPart", null, true, Load.ALL));
    setColumns(new Column[] {vtcpLOCK, vtcpAKTIV, vtcpCPAR, vtcpCART, vtcpCART1, vtcpBC, vtcpNAZART, vtcpCCPAR, vtcpPNAZART});
*/
    createFilteredDataSet(vtcpaktiv, "aktiv='D'");
  }

/*  public void setall() {

    ddl.create("VTCartPart")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cpar", 5, true)
       .addInteger("cart", 5, true)
       .addChar("cart1", 20)
       .addChar("bc", 20)
       .addChar("nazart", 50)
       .addChar("ccpar", 20)
       .addChar("pnazart", 50)
       .addPrimaryKey("cpar,cart");


    Naziv = "VTCartPart";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpar", "cart"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  } */
}
