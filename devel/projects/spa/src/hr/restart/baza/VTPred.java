/****license*****************************************************************
**   file: VTPred.java
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



public class VTPred extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static VTPred VTPredclass;

  QueryDataSet vtpred = new QueryDataSet();

  Column vtpredID_STAVKA = new Column();
  Column vtpredMAT_I = new Column();
  Column vtpredMAT_F = new Column();
  Column vtpredMAT_FI = new Column();
  Column vtpredPRO_I = new Column();
  Column vtpredPRO_F = new Column();
  Column vtpredPRO_FI = new Column();
  Column vtpredROB_I = new Column();
  Column vtpredROB_F = new Column();
  Column vtpredROB_FI = new Column();
  Column vtpredUSL_I = new Column();
  Column vtpredUSL_F = new Column();
  Column vtpredUSL_FI = new Column();
  Column vtpredPOL_I = new Column();
  Column vtpredPOL_F = new Column();
  Column vtpredPOL_FI = new Column();
  Column vtpredTOTAL = new Column();

  public static VTPred getDataModule() {
    if (VTPredclass == null) {
      VTPredclass = new VTPred();
    }
    return VTPredclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vtpred;
  }

  public VTPred() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vtpredID_STAVKA.setCaption("id_stavka predatnice");
    vtpredID_STAVKA.setColumnName("ID_STAVKA");
    vtpredID_STAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtpredID_STAVKA.setPrecision(50);
    vtpredID_STAVKA.setRowId(true);
    vtpredID_STAVKA.setTableName("VTPRED");
    vtpredID_STAVKA.setServerColumnName("ID_STAVKA");
    vtpredID_STAVKA.setSqlType(1);
    vtpredID_STAVKA.setWidth(30);
    vtpredMAT_I.setCaption("Iznos materijala");
    vtpredMAT_I.setColumnName("MAT_I");
    vtpredMAT_I.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredMAT_I.setPrecision(17);
    vtpredMAT_I.setScale(2);
    vtpredMAT_I.setDisplayMask("###,###,##0.00");
    vtpredMAT_I.setDefault("0");
    vtpredMAT_I.setTableName("VTPRED");
    vtpredMAT_I.setServerColumnName("MAT_I");
    vtpredMAT_I.setSqlType(2);
    vtpredMAT_F.setCaption("Faktor mnozenja MAT");
    vtpredMAT_F.setColumnName("MAT_F");
    vtpredMAT_F.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredMAT_F.setPrecision(12);
    vtpredMAT_F.setScale(6);
    vtpredMAT_F.setDisplayMask("###,###,##0.000000");
    vtpredMAT_F.setDefault("0");
    vtpredMAT_F.setTableName("VTPRED");
    vtpredMAT_F.setServerColumnName("MAT_F");
    vtpredMAT_F.setSqlType(2);
    vtpredMAT_FI.setCaption("Rezultirajuci iznos MAT");
    vtpredMAT_FI.setColumnName("MAT_FI");
    vtpredMAT_FI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredMAT_FI.setPrecision(17);
    vtpredMAT_FI.setScale(2);
    vtpredMAT_FI.setDisplayMask("###,###,##0.00");
    vtpredMAT_FI.setDefault("0");
    vtpredMAT_FI.setTableName("VTPRED");
    vtpredMAT_FI.setServerColumnName("MAT_FI");
    vtpredMAT_FI.setSqlType(2);
    vtpredPRO_I.setCaption("Iznos proizvoda");
    vtpredPRO_I.setColumnName("PRO_I");
    vtpredPRO_I.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredPRO_I.setPrecision(17);
    vtpredPRO_I.setScale(2);
    vtpredPRO_I.setDisplayMask("###,###,##0.00");
    vtpredPRO_I.setDefault("0");
    vtpredPRO_I.setTableName("VTPRED");
    vtpredPRO_I.setServerColumnName("PRO_I");
    vtpredPRO_I.setSqlType(2);
    vtpredPRO_F.setCaption("Faktor mnozenja PRO");
    vtpredPRO_F.setColumnName("PRO_F");
    vtpredPRO_F.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredPRO_F.setPrecision(12);
    vtpredPRO_F.setScale(6);
    vtpredPRO_F.setDisplayMask("###,###,##0.000000");
    vtpredPRO_F.setDefault("0");
    vtpredPRO_F.setTableName("VTPRED");
    vtpredPRO_F.setServerColumnName("PRO_F");
    vtpredPRO_F.setSqlType(2);
    vtpredPRO_FI.setCaption("Rezultirajuci iznos PRO");
    vtpredPRO_FI.setColumnName("PRO_FI");
    vtpredPRO_FI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredPRO_FI.setPrecision(17);
    vtpredPRO_FI.setScale(2);
    vtpredPRO_FI.setDisplayMask("###,###,##0.00");
    vtpredPRO_FI.setDefault("0");
    vtpredPRO_FI.setTableName("VTPRED");
    vtpredPRO_FI.setServerColumnName("PRO_FI");
    vtpredPRO_FI.setSqlType(2);
    vtpredROB_I.setCaption("Iznos robe");
    vtpredROB_I.setColumnName("ROB_I");
    vtpredROB_I.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredROB_I.setPrecision(17);
    vtpredROB_I.setScale(2);
    vtpredROB_I.setDisplayMask("###,###,##0.00");
    vtpredROB_I.setDefault("0");
    vtpredROB_I.setTableName("VTPRED");
    vtpredROB_I.setServerColumnName("ROB_I");
    vtpredROB_I.setSqlType(2);
    vtpredROB_F.setCaption("Faktor mnozenja ROB");
    vtpredROB_F.setColumnName("ROB_F");
    vtpredROB_F.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredROB_F.setPrecision(12);
    vtpredROB_F.setScale(6);
    vtpredROB_F.setDisplayMask("###,###,##0.000000");
    vtpredROB_F.setDefault("0");
    vtpredROB_F.setTableName("VTPRED");
    vtpredROB_F.setServerColumnName("ROB_F");
    vtpredROB_F.setSqlType(2);
    vtpredROB_FI.setCaption("Rezultirajuci iznos ROB");
    vtpredROB_FI.setColumnName("ROB_FI");
    vtpredROB_FI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredROB_FI.setPrecision(17);
    vtpredROB_FI.setScale(2);
    vtpredROB_FI.setDisplayMask("###,###,##0.00");
    vtpredROB_FI.setDefault("0");
    vtpredROB_FI.setTableName("VTPRED");
    vtpredROB_FI.setServerColumnName("ROB_FI");
    vtpredROB_FI.setSqlType(2);
    vtpredUSL_I.setCaption("Iznos usluga");
    vtpredUSL_I.setColumnName("USL_I");
    vtpredUSL_I.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredUSL_I.setPrecision(17);
    vtpredUSL_I.setScale(2);
    vtpredUSL_I.setDisplayMask("###,###,##0.00");
    vtpredUSL_I.setDefault("0");
    vtpredUSL_I.setTableName("VTPRED");
    vtpredUSL_I.setServerColumnName("USL_I");
    vtpredUSL_I.setSqlType(2);
    vtpredUSL_F.setCaption("Faktor mnozenja USL");
    vtpredUSL_F.setColumnName("USL_F");
    vtpredUSL_F.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredUSL_F.setPrecision(12);
    vtpredUSL_F.setScale(6);
    vtpredUSL_F.setDisplayMask("###,###,##0.000000");
    vtpredUSL_F.setDefault("0");
    vtpredUSL_F.setTableName("VTPRED");
    vtpredUSL_F.setServerColumnName("USL_F");
    vtpredUSL_F.setSqlType(2);
    vtpredUSL_FI.setCaption("Rezultirajuci iznos USL");
    vtpredUSL_FI.setColumnName("USL_FI");
    vtpredUSL_FI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredUSL_FI.setPrecision(17);
    vtpredUSL_FI.setScale(2);
    vtpredUSL_FI.setDisplayMask("###,###,##0.00");
    vtpredUSL_FI.setDefault("0");
    vtpredUSL_FI.setTableName("VTPRED");
    vtpredUSL_FI.setServerColumnName("USL_FI");
    vtpredUSL_FI.setSqlType(2);
    vtpredPOL_I.setCaption("Iznos POL");
    vtpredPOL_I.setColumnName("POL_I");
    vtpredPOL_I.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredPOL_I.setPrecision(17);
    vtpredPOL_I.setScale(2);
    vtpredPOL_I.setDisplayMask("###,###,##0.00");
    vtpredPOL_I.setDefault("0");
    vtpredPOL_I.setTableName("VTPRED");
    vtpredPOL_I.setServerColumnName("POL_I");
    vtpredPOL_I.setSqlType(2);
    vtpredPOL_F.setCaption("Faktor mnozenja POL");
    vtpredPOL_F.setColumnName("POL_F");
    vtpredPOL_F.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredPOL_F.setPrecision(12);
    vtpredPOL_F.setScale(6);
    vtpredPOL_F.setDisplayMask("###,###,##0.000000");
    vtpredPOL_F.setDefault("0");
    vtpredPOL_F.setTableName("VTPRED");
    vtpredPOL_F.setServerColumnName("POL_F");
    vtpredPOL_F.setSqlType(2);
    vtpredPOL_FI.setCaption("Rezultirajuci iznos POL");
    vtpredPOL_FI.setColumnName("POL_FI");
    vtpredPOL_FI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredPOL_FI.setPrecision(17);
    vtpredPOL_FI.setScale(2);
    vtpredPOL_FI.setDisplayMask("###,###,##0.00");
    vtpredPOL_FI.setDefault("0");
    vtpredPOL_FI.setTableName("VTPRED");
    vtpredPOL_FI.setServerColumnName("POL_FI");
    vtpredPOL_FI.setSqlType(2);
    vtpredTOTAL.setCaption("Suma svih _FI");
    vtpredTOTAL.setColumnName("TOTAL");
    vtpredTOTAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtpredTOTAL.setPrecision(17);
    vtpredTOTAL.setScale(2);
    vtpredTOTAL.setDisplayMask("###,###,##0.00");
    vtpredTOTAL.setDefault("0");
    vtpredTOTAL.setTableName("VTPRED");
    vtpredTOTAL.setServerColumnName("TOTAL");
    vtpredTOTAL.setSqlType(2);
    vtpred.setResolver(dm.getQresolver());
    vtpred.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from VTPred", null, true, Load.ALL));
    setColumns(new Column[] {vtpredID_STAVKA, vtpredMAT_I, vtpredMAT_F, vtpredMAT_FI, vtpredPRO_I, vtpredPRO_F, vtpredPRO_FI, vtpredROB_I, vtpredROB_F, 
        vtpredROB_FI, vtpredUSL_I, vtpredUSL_F, vtpredUSL_FI, vtpredPOL_I, vtpredPOL_F, vtpredPOL_FI, vtpredTOTAL});
  }

  public void setall() {

    ddl.create("VTPred")
       .addChar("id_stavka", 50, true)
       .addFloat("mat_i", 17, 2)
       .addFloat("mat_f", 12, 6)
       .addFloat("mat_fi", 17, 2)
       .addFloat("pro_i", 17, 2)
       .addFloat("pro_f", 12, 6)
       .addFloat("pro_fi", 17, 2)
       .addFloat("rob_i", 17, 2)
       .addFloat("rob_f", 12, 6)
       .addFloat("rob_fi", 17, 2)
       .addFloat("usl_i", 17, 2)
       .addFloat("usl_f", 12, 6)
       .addFloat("usl_fi", 17, 2)
       .addFloat("pol_i", 17, 2)
       .addFloat("pol_f", 12, 6)
       .addFloat("pol_fi", 17, 2)
       .addFloat("total", 17, 2)
       .addPrimaryKey("id_stavka");


    Naziv = "VTPred";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
