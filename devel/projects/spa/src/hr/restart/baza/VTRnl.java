/****license*****************************************************************
**   file: VTRnl.java
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



public class VTRnl extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static VTRnl VTRnlclass;

  QueryDataSet vtrnl = new QueryDataSet();

  Column vtrnlCRADNAL = new Column();
  Column vtrnlRBSRNL = new Column();
  Column vtrnlBRANCH = new Column();
  Column vtrnlRBSIZD = new Column();
  Column vtrnlVEZA = new Column();

  public static VTRnl getDataModule() {
    if (VTRnlclass == null) {
      VTRnlclass = new VTRnl();
    }
    return VTRnlclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vtrnl;
  }

  public VTRnl() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vtrnlCRADNAL.setCaption("Radni nalog");
    vtrnlCRADNAL.setColumnName("CRADNAL");
    vtrnlCRADNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrnlCRADNAL.setPrecision(30);
    vtrnlCRADNAL.setRowId(true);
    vtrnlCRADNAL.setTableName("VTRNL");
    vtrnlCRADNAL.setServerColumnName("CRADNAL");
    vtrnlCRADNAL.setSqlType(1);
    vtrnlCRADNAL.setWidth(30);
    vtrnlRBSRNL.setCaption("Rbr stavke rnl");
    vtrnlRBSRNL.setColumnName("RBSRNL");
    vtrnlRBSRNL.setDataType(com.borland.dx.dataset.Variant.INT);
    vtrnlRBSRNL.setRowId(true);
    vtrnlRBSRNL.setTableName("VTRNL");
    vtrnlRBSRNL.setServerColumnName("RBSRNL");
    vtrnlRBSRNL.setSqlType(4);
    vtrnlRBSRNL.setWidth(6);
    vtrnlBRANCH.setCaption("Grana normativa");
    vtrnlBRANCH.setColumnName("BRANCH");
    vtrnlBRANCH.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrnlBRANCH.setPrecision(60);
    vtrnlBRANCH.setRowId(true);
    vtrnlBRANCH.setTableName("VTRNL");
    vtrnlBRANCH.setServerColumnName("BRANCH");
    vtrnlBRANCH.setSqlType(1);
    vtrnlBRANCH.setWidth(30);
    vtrnlRBSIZD.setCaption("Rbr stavke izdatnice");
    vtrnlRBSIZD.setColumnName("RBSIZD");
    vtrnlRBSIZD.setDataType(com.borland.dx.dataset.Variant.INT);
    vtrnlRBSIZD.setTableName("VTRNL");
    vtrnlRBSIZD.setServerColumnName("RBSIZD");
    vtrnlRBSIZD.setSqlType(4);
    vtrnlRBSIZD.setWidth(6);
    vtrnlVEZA.setCaption("id_stavka pripadaju\u0107e izdatnice");
    vtrnlVEZA.setColumnName("VEZA");
    vtrnlVEZA.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrnlVEZA.setPrecision(50);
    vtrnlVEZA.setTableName("VTRNL");
    vtrnlVEZA.setServerColumnName("VEZA");
    vtrnlVEZA.setSqlType(1);
    vtrnlVEZA.setWidth(30);
    vtrnl.setResolver(dm.getQresolver());
    vtrnl.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from VTRnl", null, true, Load.ALL));
    setColumns(new Column[] {vtrnlCRADNAL, vtrnlRBSRNL, vtrnlBRANCH, vtrnlRBSIZD, vtrnlVEZA});
  }

  public void setall() {

    ddl.create("VTRnl")
       .addChar("cradnal", 30, true)
       .addInteger("rbsrnl", 6, true)
       .addChar("branch", 60, true)
       .addInteger("rbsizd", 6)
       .addChar("veza", 50)
       .addPrimaryKey("cradnal,rbsrnl,branch");


    Naziv = "VTRnl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cradnal"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
