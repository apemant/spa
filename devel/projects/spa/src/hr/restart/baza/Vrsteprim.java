/****license*****************************************************************
**   file: Vrsteprim.java
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



public class Vrsteprim extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrsteprim Vrsteprimclass;

  QueryDataSet vrsteprim = new raDataSet();

  Column vrsteprimLOKK = new Column();
  Column vrsteprimAKTIV = new Column();
  Column vrsteprimCVRP = new Column();
  Column vrsteprimNAZIV = new Column();
  Column vrsteprimCOBR = new Column();
  Column vrsteprimCOSN = new Column();
  Column vrsteprimRSOO = new Column();
  Column vrsteprimRNALOG = new Column();
  Column vrsteprimREGRES = new Column();
  Column vrsteprimCGRPRIM = new Column();
  Column vrsteprimCVRPARH = new Column();
  Column vrsteprimSTAVKA = new Column();
  Column vrsteprimCPOV = new Column();
  Column vrsteprimKOEF = new Column();
  Column vrsteprimPARAMETRI = new Column();

  public static Vrsteprim getDataModule() {
    if (Vrsteprimclass == null) {
      Vrsteprimclass = new Vrsteprim();
    }
    return Vrsteprimclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vrsteprim;
  }

  public Vrsteprim() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vrsteprimLOKK.setCaption("Status zauzetosti");
    vrsteprimLOKK.setColumnName("LOKK");
    vrsteprimLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimLOKK.setPrecision(1);
    vrsteprimLOKK.setTableName("VRSTEPRIM");
    vrsteprimLOKK.setServerColumnName("LOKK");
    vrsteprimLOKK.setSqlType(1);
    vrsteprimLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrsteprimLOKK.setDefault("N");
    vrsteprimAKTIV.setCaption("Aktivan - neaktivan");
    vrsteprimAKTIV.setColumnName("AKTIV");
    vrsteprimAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimAKTIV.setPrecision(1);
    vrsteprimAKTIV.setTableName("VRSTEPRIM");
    vrsteprimAKTIV.setServerColumnName("AKTIV");
    vrsteprimAKTIV.setSqlType(1);
    vrsteprimAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrsteprimAKTIV.setDefault("D");
    vrsteprimCVRP.setCaption("Oznaka");
    vrsteprimCVRP.setColumnName("CVRP");
    vrsteprimCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vrsteprimCVRP.setPrecision(3);
    vrsteprimCVRP.setRowId(true);
    vrsteprimCVRP.setTableName("VRSTEPRIM");
    vrsteprimCVRP.setServerColumnName("CVRP");
    vrsteprimCVRP.setSqlType(5);
    vrsteprimCVRP.setWidth(3);
    vrsteprimNAZIV.setCaption("Naziv");
    vrsteprimNAZIV.setColumnName("NAZIV");
    vrsteprimNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimNAZIV.setPrecision(30);
    vrsteprimNAZIV.setTableName("VRSTEPRIM");
    vrsteprimNAZIV.setServerColumnName("NAZIV");
    vrsteprimNAZIV.setSqlType(1);
    vrsteprimCOBR.setCaption("Na\u010Din obra\u010Duna");
    vrsteprimCOBR.setColumnName("COBR");
    vrsteprimCOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vrsteprimCOBR.setPrecision(2);
    vrsteprimCOBR.setTableName("VRSTEPRIM");
    vrsteprimCOBR.setServerColumnName("COBR");
    vrsteprimCOBR.setSqlType(5);
    vrsteprimCOBR.setWidth(2);
    vrsteprimCOSN.setCaption("Oznaka osnovice iz koje se ra\u010Duna zarada");
    vrsteprimCOSN.setColumnName("COSN");
    vrsteprimCOSN.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vrsteprimCOSN.setPrecision(2);
    vrsteprimCOSN.setTableName("VRSTEPRIM");
    vrsteprimCOSN.setServerColumnName("COSN");
    vrsteprimCOSN.setSqlType(5);
    vrsteprimCOSN.setWidth(2);
    vrsteprimRSOO.setCaption("Osnova osiguranja");
    vrsteprimRSOO.setColumnName("RSOO");
    vrsteprimRSOO.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimRSOO.setPrecision(5);
    vrsteprimRSOO.setTableName("VRSTEPRIM");
    vrsteprimRSOO.setServerColumnName("RSOO");
    vrsteprimRSOO.setSqlType(1);
    vrsteprimRNALOG.setCaption("Unos radnog naloga");
    vrsteprimRNALOG.setColumnName("RNALOG");
    vrsteprimRNALOG.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimRNALOG.setPrecision(1);
    vrsteprimRNALOG.setTableName("VRSTEPRIM");
    vrsteprimRNALOG.setServerColumnName("RNALOG");
    vrsteprimRNALOG.setSqlType(1);
    vrsteprimRNALOG.setDefault("N");
    vrsteprimREGRES.setCaption("Primanje na godišnjoj osnovi (regres)");
    vrsteprimREGRES.setColumnName("REGRES");
    vrsteprimREGRES.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimREGRES.setPrecision(1);
    vrsteprimREGRES.setTableName("VRSTEPRIM");
    vrsteprimREGRES.setServerColumnName("REGRES");
    vrsteprimREGRES.setSqlType(1);
    vrsteprimREGRES.setDefault("N");
    vrsteprimCGRPRIM.setCaption("Grupa primanja");
    vrsteprimCGRPRIM.setColumnName("CGRPRIM");
    vrsteprimCGRPRIM.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimCGRPRIM.setPrecision(5);
    vrsteprimCGRPRIM.setTableName("VRSTEPRIM");
    vrsteprimCGRPRIM.setServerColumnName("CGRPRIM");
    vrsteprimCGRPRIM.setSqlType(1);
    vrsteprimCVRPARH.setCaption("Vrsta primanja u koju se dohva\u0107a iz arhive");
    vrsteprimCVRPARH.setColumnName("CVRPARH");
    vrsteprimCVRPARH.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vrsteprimCVRPARH.setPrecision(3);
    vrsteprimCVRPARH.setTableName("VRSTEPRIM");
    vrsteprimCVRPARH.setServerColumnName("CVRPARH");
    vrsteprimCVRPARH.setSqlType(5);
    vrsteprimCVRPARH.setWidth(3);
    vrsteprimSTAVKA.setCaption("Veza sa kontom CSKL=1 APP=pl VRDOK=PL");
    vrsteprimSTAVKA.setColumnName("STAVKA");
    vrsteprimSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimSTAVKA.setPrecision(8);
    vrsteprimSTAVKA.setTableName("VRSTEPRIM");
    vrsteprimSTAVKA.setServerColumnName("STAVKA");
    vrsteprimSTAVKA.setSqlType(1);
    vrsteprimCPOV.setCaption("Povjerioc");
    vrsteprimCPOV.setColumnName("CPOV");
    vrsteprimCPOV.setDataType(com.borland.dx.dataset.Variant.INT);
    vrsteprimCPOV.setPrecision(6);
    vrsteprimCPOV.setTableName("VRSTEPRIM");
    vrsteprimCPOV.setServerColumnName("CPOV");
    vrsteprimCPOV.setSqlType(4);
    vrsteprimCPOV.setWidth(6);
    vrsteprimKOEF.setCaption("Koeficijent");
    vrsteprimKOEF.setColumnName("KOEF");
    vrsteprimKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vrsteprimKOEF.setPrecision(9);
    vrsteprimKOEF.setScale(5);
    vrsteprimKOEF.setDisplayMask("###,###,##0.00000");
    vrsteprimKOEF.setDefault("0");
    vrsteprimKOEF.setTableName("VRSTEPRIM");
    vrsteprimKOEF.setServerColumnName("KOEF");
    vrsteprimKOEF.setSqlType(2);
    vrsteprimKOEF.setDefault("100");
    vrsteprimPARAMETRI.setCaption("Parametri");
    vrsteprimPARAMETRI.setColumnName("PARAMETRI");
    vrsteprimPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsteprimPARAMETRI.setPrecision(20);
    vrsteprimPARAMETRI.setTableName("VRSTEPRIM");
    vrsteprimPARAMETRI.setServerColumnName("PARAMETRI");
    vrsteprimPARAMETRI.setSqlType(1);
    vrsteprimPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrsteprim.setResolver(dm.getQresolver());
    vrsteprim.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vrsteprim", null, true, Load.ALL));
 setColumns(new Column[] {vrsteprimLOKK, vrsteprimAKTIV, vrsteprimCVRP, vrsteprimNAZIV, vrsteprimCOBR, vrsteprimCOSN, vrsteprimRSOO,
        vrsteprimRNALOG, vrsteprimREGRES, vrsteprimCGRPRIM, vrsteprimCVRPARH, vrsteprimSTAVKA, vrsteprimCPOV, vrsteprimKOEF, vrsteprimPARAMETRI});
  }

  public void setall() {

    ddl.create("Vrsteprim")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cvrp", 3, true)
       .addChar("naziv", 30)
       .addShort("cobr", 2)
       .addShort("cosn", 2)
       .addChar("rsoo", 5)
       .addChar("rnalog", 1, "N")
       .addChar("regres", 1, "N")
       .addChar("cgrprim", 5)
       .addShort("cvrparh", 3)
       .addChar("stavka", 8)
       .addInteger("cpov", 6)
       .addFloat("koef", 9, 5)
       .addChar("parametri", 20)
       .addPrimaryKey("cvrp");


    Naziv = "Vrsteprim";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
