/****license*****************************************************************
**   file: MxDokument.java
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



public class MxDokument extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static MxDokument MxDokumentclass;

  QueryDataSet mxdoc = new raDataSet();

  Column mxdocCDOC = new Column();
  Column mxdocOPIS = new Column();
  Column mxdocPAGESIZE = new Column();
  Column mxdocPAGEWIDTH = new Column();
  Column mxdocTOPMARGIN_MM = new Column();
  Column mxdocLEFTMARGIN = new Column();
  Column mxdocLINESPACING = new Column();
  Column mxdocEJECTPAPER = new Column();
  Column mxdocUSER1 = new Column();
  Column mxdocUSER2 = new Column();
  Column mxdocUSER3 = new Column();
  Column mxdocUSER4 = new Column();
  Column mxdocUSER5 = new Column();
  Column mxdocUSER6 = new Column();
  Column mxdocUSER7 = new Column();
  Column mxdocUSER8 = new Column();
  Column mxdocUSER9 = new Column();

  public static MxDokument getDataModule() {
    if (MxDokumentclass == null) {
      MxDokumentclass = new MxDokument();
    }
    return MxDokumentclass;
  }

  public QueryDataSet getQueryDataSet() {
    return mxdoc;
  }

  public MxDokument() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    mxdocCDOC.setCaption("Šifra");
    mxdocCDOC.setColumnName("CDOC");
    mxdocCDOC.setDataType(com.borland.dx.dataset.Variant.INT);
    mxdocCDOC.setPrecision(6);
    mxdocCDOC.setRowId(true);
    mxdocCDOC.setTableName("MXDOKUMENT");
    mxdocCDOC.setServerColumnName("CDOC");
    mxdocCDOC.setSqlType(4);
    mxdocCDOC.setWidth(6);
    mxdocOPIS.setCaption("Opis");
    mxdocOPIS.setColumnName("OPIS");
    mxdocOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocOPIS.setPrecision(50);
    mxdocOPIS.setTableName("MXDOKUMENT");
    mxdocOPIS.setServerColumnName("OPIS");
    mxdocOPIS.setSqlType(1);
    mxdocOPIS.setWidth(30);
    mxdocPAGESIZE.setCaption("Visina stranice");
    mxdocPAGESIZE.setColumnName("PAGESIZE");
    mxdocPAGESIZE.setDataType(com.borland.dx.dataset.Variant.INT);
    mxdocPAGESIZE.setPrecision(6);
    mxdocPAGESIZE.setTableName("MXDOKUMENT");
    mxdocPAGESIZE.setServerColumnName("PAGESIZE");
    mxdocPAGESIZE.setSqlType(4);
    mxdocPAGESIZE.setWidth(6);
    mxdocPAGEWIDTH.setCaption("Širina stranice");
    mxdocPAGEWIDTH.setColumnName("PAGEWIDTH");
    mxdocPAGEWIDTH.setDataType(com.borland.dx.dataset.Variant.INT);
    mxdocPAGEWIDTH.setPrecision(6);
    mxdocPAGEWIDTH.setTableName("MXDOKUMENT");
    mxdocPAGEWIDTH.setServerColumnName("PAGEWIDTH");
    mxdocPAGEWIDTH.setSqlType(4);
    mxdocPAGEWIDTH.setWidth(6);
    mxdocTOPMARGIN_MM.setCaption("Gornja margina u mm");
    mxdocTOPMARGIN_MM.setColumnName("TOPMARGIN_MM");
    mxdocTOPMARGIN_MM.setDataType(com.borland.dx.dataset.Variant.INT);
    mxdocTOPMARGIN_MM.setPrecision(6);
    mxdocTOPMARGIN_MM.setTableName("MXDOKUMENT");
    mxdocTOPMARGIN_MM.setServerColumnName("TOPMARGIN_MM");
    mxdocTOPMARGIN_MM.setSqlType(4);
    mxdocTOPMARGIN_MM.setWidth(6);
    mxdocLEFTMARGIN.setCaption("Lijeva margina");
    mxdocLEFTMARGIN.setColumnName("LEFTMARGIN");
    mxdocLEFTMARGIN.setDataType(com.borland.dx.dataset.Variant.INT);
    mxdocLEFTMARGIN.setPrecision(6);
    mxdocLEFTMARGIN.setTableName("MXDOKUMENT");
    mxdocLEFTMARGIN.setServerColumnName("LEFTMARGIN");
    mxdocLEFTMARGIN.setSqlType(4);
    mxdocLEFTMARGIN.setWidth(6);
    mxdocLINESPACING.setCaption("Razmak linija");
    mxdocLINESPACING.setColumnName("LINESPACING");
    mxdocLINESPACING.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocLINESPACING.setPrecision(15);
    mxdocLINESPACING.setTableName("MXDOKUMENT");
    mxdocLINESPACING.setServerColumnName("LINESPACING");
    mxdocLINESPACING.setSqlType(1);
    mxdocEJECTPAPER.setCaption("Izbaciti papir");
    mxdocEJECTPAPER.setColumnName("EJECTPAPER");
    mxdocEJECTPAPER.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocEJECTPAPER.setPrecision(1);
    mxdocEJECTPAPER.setTableName("MXDOKUMENT");
    mxdocEJECTPAPER.setServerColumnName("EJECTPAPER");
    mxdocEJECTPAPER.setSqlType(1);
    mxdocUSER1.setCaption("User 1");
    mxdocUSER1.setColumnName("USER1");
    mxdocUSER1.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER1.setPrecision(15);
    mxdocUSER1.setTableName("MXDOKUMENT");
    mxdocUSER1.setServerColumnName("USER1");
    mxdocUSER1.setSqlType(1);
    mxdocUSER2.setCaption("User 2");
    mxdocUSER2.setColumnName("USER2");
    mxdocUSER2.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER2.setPrecision(15);
    mxdocUSER2.setTableName("MXDOKUMENT");
    mxdocUSER2.setServerColumnName("USER2");
    mxdocUSER2.setSqlType(1);
    mxdocUSER3.setCaption("User 3");
    mxdocUSER3.setColumnName("USER3");
    mxdocUSER3.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER3.setPrecision(15);
    mxdocUSER3.setTableName("MXDOKUMENT");
    mxdocUSER3.setServerColumnName("USER3");
    mxdocUSER3.setSqlType(1);
    mxdocUSER4.setCaption("User 4");
    mxdocUSER4.setColumnName("USER4");
    mxdocUSER4.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER4.setPrecision(15);
    mxdocUSER4.setTableName("MXDOKUMENT");
    mxdocUSER4.setServerColumnName("USER4");
    mxdocUSER4.setSqlType(1);
    mxdocUSER5.setCaption("User 5");
    mxdocUSER5.setColumnName("USER5");
    mxdocUSER5.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER5.setPrecision(15);
    mxdocUSER5.setTableName("MXDOKUMENT");
    mxdocUSER5.setServerColumnName("USER5");
    mxdocUSER5.setSqlType(1);
    mxdocUSER6.setCaption("User 6");
    mxdocUSER6.setColumnName("USER6");
    mxdocUSER6.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER6.setPrecision(15);
    mxdocUSER6.setTableName("MXDOKUMENT");
    mxdocUSER6.setServerColumnName("USER6");
    mxdocUSER6.setSqlType(1);
    mxdocUSER7.setCaption("User 7");
    mxdocUSER7.setColumnName("USER7");
    mxdocUSER7.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER7.setPrecision(15);
    mxdocUSER7.setTableName("MXDOKUMENT");
    mxdocUSER7.setServerColumnName("USER7");
    mxdocUSER7.setSqlType(1);
    mxdocUSER8.setCaption("User 8");
    mxdocUSER8.setColumnName("USER8");
    mxdocUSER8.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER8.setPrecision(15);
    mxdocUSER8.setTableName("MXDOKUMENT");
    mxdocUSER8.setServerColumnName("USER8");
    mxdocUSER8.setSqlType(1);
    mxdocUSER9.setCaption("User 9");
    mxdocUSER9.setColumnName("USER9");
    mxdocUSER9.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxdocUSER9.setPrecision(15);
    mxdocUSER9.setTableName("MXDOKUMENT");
    mxdocUSER9.setServerColumnName("USER9");
    mxdocUSER9.setSqlType(1);
    mxdoc.setResolver(dm.getQresolver());
    mxdoc.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from MxDokument", null, true, Load.ALL));
 setColumns(new Column[] {mxdocCDOC, mxdocOPIS, mxdocPAGESIZE, mxdocPAGEWIDTH, mxdocTOPMARGIN_MM, mxdocLEFTMARGIN, mxdocLINESPACING, mxdocEJECTPAPER,
        mxdocUSER1, mxdocUSER2, mxdocUSER3, mxdocUSER4, mxdocUSER5, mxdocUSER6, mxdocUSER7, mxdocUSER8, mxdocUSER9});
  }

  public void setall() {

    ddl.create("MxDokument")
       .addInteger("cdoc", 6, true)
       .addChar("opis", 50)
       .addInteger("pagesize", 6)
       .addInteger("pagewidth", 6)
       .addInteger("topmargin_mm", 6)
       .addInteger("leftmargin", 6)
       .addChar("linespacing", 15)
       .addChar("ejectpaper", 1)
       .addChar("user1", 15)
       .addChar("user2", 15)
       .addChar("user3", 15)
       .addChar("user4", 15)
       .addChar("user5", 15)
       .addChar("user6", 15)
       .addChar("user7", 15)
       .addChar("user8", 15)
       .addChar("user9", 15)
       .addPrimaryKey("cdoc");


    Naziv = "MxDokument";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
