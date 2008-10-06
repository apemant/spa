/****license*****************************************************************
**   file: Orgrad.java
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



public class Orgrad extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Orgrad Orgradclass;

  QueryDataSet orgrad = new QueryDataSet();

  Column orgradCORG = new Column();
  Column orgradCRADNIK = new Column();
  Column orgradUDIORADA = new Column();
  Column orgradPARAMETRI = new Column();

  public static Orgrad getDataModule() {
    if (Orgradclass == null) {
      Orgradclass = new Orgrad();
    }
    return Orgradclass;
  }

  public QueryDataSet getQueryDataSet() {
    return orgrad;
  }

  public Orgrad() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    orgradCORG.setCaption("Org. Jedinica");
    orgradCORG.setColumnName("CORG");
    orgradCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgradCORG.setPrecision(12);
    orgradCORG.setRowId(true);
    orgradCORG.setTableName("ORGRAD");
    orgradCORG.setServerColumnName("CORG");
    orgradCORG.setSqlType(1);
    orgradCRADNIK.setCaption("Mati\u010Dni broj");
    orgradCRADNIK.setColumnName("CRADNIK");
    orgradCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgradCRADNIK.setPrecision(6);
    orgradCRADNIK.setRowId(true);
    orgradCRADNIK.setTableName("ORGRAD");
    orgradCRADNIK.setServerColumnName("CRADNIK");
    orgradCRADNIK.setSqlType(1);
    orgradUDIORADA.setCaption("Udio rada u drugoj org jedinici %");
    orgradUDIORADA.setColumnName("UDIORADA");
    orgradUDIORADA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    orgradUDIORADA.setPrecision(17);
    orgradUDIORADA.setScale(2);
    orgradUDIORADA.setDisplayMask("###,###,##0.00");
    orgradUDIORADA.setDefault("0");
    orgradUDIORADA.setTableName("ORGRAD");
    orgradUDIORADA.setServerColumnName("UDIORADA");
    orgradUDIORADA.setSqlType(2);
    orgradPARAMETRI.setCaption("Parametri");
    orgradPARAMETRI.setColumnName("PARAMETRI");
    orgradPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgradPARAMETRI.setPrecision(20);
    orgradPARAMETRI.setTableName("ORGRAD");
    orgradPARAMETRI.setServerColumnName("PARAMETRI");
    orgradPARAMETRI.setSqlType(1);
    orgradPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    orgrad.setResolver(dm.getQresolver());
    orgrad.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Orgrad", null, true, Load.ALL));
 setColumns(new Column[] {orgradCORG, orgradCRADNIK, orgradUDIORADA, orgradPARAMETRI});
  }

  public void setall() {

    ddl.create("Orgrad")
       .addChar("corg", 12, true)
       .addChar("cradnik", 6, true)
       .addFloat("udiorada", 17, 2)
       .addChar("parametri", 20)
       .addPrimaryKey("corg,cradnik");


    Naziv = "Orgrad";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cradnik"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
