/****license*****************************************************************
**   file: KamRac.java
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



public class KamRac extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static KamRac KamRacclass;

  QueryDataSet krc = new raDataSet();

  Column krcKNJIG = new Column();
  Column krcCPAR = new Column();
  Column krcBROJDOK = new Column();
  Column krcDUGPOT = new Column();
  Column krcDATDOK = new Column();
  Column krcDATDOSP = new Column();
  Column krcIZNOS = new Column();
  Column krcCSKSTAVKE = new Column();

  public static KamRac getDataModule() {
    if (KamRacclass == null) {
      KamRacclass = new KamRac();
    }
    return KamRacclass;
  }

  public QueryDataSet getQueryDataSet() {
    return krc;
  }

  public KamRac() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    krcKNJIG.setCaption("Knjigovodstvo");
    krcKNJIG.setColumnName("KNJIG");
    krcKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    krcKNJIG.setPrecision(12);
    krcKNJIG.setRowId(true);
    krcKNJIG.setTableName("KAMRAC");
    krcKNJIG.setServerColumnName("KNJIG");
    krcKNJIG.setSqlType(1);
    krcCPAR.setCaption("Partner");
    krcCPAR.setColumnName("CPAR");
    krcCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    krcCPAR.setPrecision(6);
    krcCPAR.setRowId(true);
    krcCPAR.setTableName("KAMRAC");
    krcCPAR.setServerColumnName("CPAR");
    krcCPAR.setSqlType(4);
    krcCPAR.setWidth(6);
    krcBROJDOK.setCaption("Broj ra\u010Duna");
    krcBROJDOK.setColumnName("BROJDOK");
    krcBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    krcBROJDOK.setPrecision(50);
    krcBROJDOK.setRowId(true);
    krcBROJDOK.setTableName("KAMRAC");
    krcBROJDOK.setServerColumnName("BROJDOK");
    krcBROJDOK.setSqlType(1);
    krcBROJDOK.setWidth(30);
    krcDUGPOT.setCaption("Kupac / dobavlja\u010D");
    krcDUGPOT.setColumnName("DUGPOT");
    krcDUGPOT.setDataType(com.borland.dx.dataset.Variant.STRING);
    krcDUGPOT.setPrecision(1);
    krcDUGPOT.setTableName("KAMRAC");
    krcDUGPOT.setServerColumnName("DUGPOT");
    krcDUGPOT.setSqlType(1);
    krcDUGPOT.setDefault("D");
    krcDATDOK.setCaption("Datum");
    krcDATDOK.setColumnName("DATDOK");
    krcDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    krcDATDOK.setPrecision(19);
    krcDATDOK.setDisplayMask("dd-MM-yyyy");
//    krcDATDOK.setEditMask("dd-MM-yyyy");
    krcDATDOK.setTableName("KAMRAC");
    krcDATDOK.setServerColumnName("DATDOK");
    krcDATDOK.setSqlType(93);
    krcDATDOK.setWidth(10);
    krcDATDOSP.setCaption("Dospje\u0107e");
    krcDATDOSP.setColumnName("DATDOSP");
    krcDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    krcDATDOSP.setPrecision(19);
    krcDATDOSP.setDisplayMask("dd-MM-yyyy");
//    krcDATDOSP.setEditMask("dd-MM-yyyy");
    krcDATDOSP.setTableName("KAMRAC");
    krcDATDOSP.setServerColumnName("DATDOSP");
    krcDATDOSP.setSqlType(93);
    krcDATDOSP.setWidth(10);
    krcIZNOS.setCaption("Iznos ra\u010Duna");
    krcIZNOS.setColumnName("IZNOS");
    krcIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    krcIZNOS.setPrecision(17);
    krcIZNOS.setScale(2);
    krcIZNOS.setDisplayMask("###,###,##0.00");
    krcIZNOS.setDefault("0");
    krcIZNOS.setTableName("KAMRAC");
    krcIZNOS.setServerColumnName("IZNOS");
    krcIZNOS.setSqlType(2);
    krcCSKSTAVKE.setCaption("Jedinstveni klju\u010D");
    krcCSKSTAVKE.setColumnName("CRACUNA");
    krcCSKSTAVKE.setDataType(com.borland.dx.dataset.Variant.STRING);
    krcCSKSTAVKE.setPrecision(80);
    krcCSKSTAVKE.setTableName("KAMRAC");
    krcCSKSTAVKE.setServerColumnName("CRACUNA");
    krcCSKSTAVKE.setSqlType(1);
    krcCSKSTAVKE.setWidth(30);
    krc.setResolver(dm.getQresolver());
    krc.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from KamRac", null, true, Load.ALL));
 setColumns(new Column[] {krcKNJIG, krcCPAR, krcBROJDOK, krcDUGPOT, krcDATDOK, krcDATDOSP, krcIZNOS, krcCSKSTAVKE});
  }

  public void setall() {

    ddl.create("KamRac")
       .addChar("knjig", 12, true)
       .addInteger("cpar", 6, true)
       .addChar("brojdok", 50, true)
       .addChar("dugpot", 1, "D")
       .addDate("datdok")
       .addDate("datdosp")
       .addFloat("iznos", 17, 2)
       .addChar("cracuna", 80)
       .addPrimaryKey("knjig,cpar,brojdok");


    Naziv = "KamRac";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brojdok"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
