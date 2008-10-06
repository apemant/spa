/****license*****************************************************************
**   file: UIstavke.java
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



public class UIstavke extends KreirDrop implements DataModule {

//  dM dm  = dM.getDataModule();
  private static UIstavke UIstavkeclass;

  QueryDataSet uistavke = new QueryDataSet();

/* Column uistavkeLOKK = new Column();
  Column uistavkeAKTIV = new Column();
  Column uistavkeKNJIG = new Column();
  Column uistavkeCPAR = new Column();
  Column uistavkeVRDOK = new Column();
  Column uistavkeBROJKONTA = new Column();
  Column uistavkeBROJDOK = new Column();
  Column uistavkeRBS = new Column();
  Column uistavkeCORG = new Column();
  Column uistavkeID = new Column();
  Column uistavkeIP = new Column();
  Column uistavkeCKOLONE = new Column();
  Column uistavkeCKNJIGE = new Column();
  Column uistavkeURAIRA = new Column();
  Column uistavkeDUGPOT = new Column();
  Column uistavkeCGKSTAVKE = new Column();
  Column uistavkeSTAVKA = new Column();
  Column uistavkeCSKL = new Column();
*/
  
  public static UIstavke getDataModule() {
    if (UIstavkeclass == null) {
      UIstavkeclass = new UIstavke();
    }
    return UIstavkeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return uistavke;
  }

  public UIstavke() {
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
    uistavkeLOKK.setCaption("Status zauzetosti");
    uistavkeLOKK.setColumnName("LOKK");
    uistavkeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeLOKK.setPrecision(1);
    uistavkeLOKK.setTableName("UISTAVKE");
    uistavkeLOKK.setServerColumnName("LOKK");
    uistavkeLOKK.setSqlType(1);
    uistavkeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    uistavkeLOKK.setDefault("N");
    uistavkeAKTIV.setCaption("Aktivan - neaktivan");
    uistavkeAKTIV.setColumnName("AKTIV");
    uistavkeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeAKTIV.setPrecision(1);
    uistavkeAKTIV.setTableName("UISTAVKE");
    uistavkeAKTIV.setServerColumnName("AKTIV");
    uistavkeAKTIV.setSqlType(1);
    uistavkeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    uistavkeAKTIV.setDefault("D");
    uistavkeKNJIG.setCaption("Knjigovodstvo");
    uistavkeKNJIG.setColumnName("KNJIG");
    uistavkeKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeKNJIG.setPrecision(12);
    uistavkeKNJIG.setRowId(true);
    uistavkeKNJIG.setTableName("UISTAVKE");
    uistavkeKNJIG.setServerColumnName("KNJIG");
    uistavkeKNJIG.setSqlType(1);
    uistavkeCPAR.setCaption("Partner");
    uistavkeCPAR.setColumnName("CPAR");
    uistavkeCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    uistavkeCPAR.setPrecision(6);
    uistavkeCPAR.setRowId(true);
    uistavkeCPAR.setTableName("UISTAVKE");
    uistavkeCPAR.setServerColumnName("CPAR");
    uistavkeCPAR.setSqlType(4);
    uistavkeCPAR.setWidth(6);
    uistavkeVRDOK.setCaption("Vrsta dokumenta");
    uistavkeVRDOK.setColumnName("VRDOK");
    uistavkeVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeVRDOK.setPrecision(3);
    uistavkeVRDOK.setRowId(true);
    uistavkeVRDOK.setTableName("UISTAVKE");
    uistavkeVRDOK.setServerColumnName("VRDOK");
    uistavkeVRDOK.setSqlType(1);
    uistavkeBROJKONTA.setCaption("Konto");
    uistavkeBROJKONTA.setColumnName("BROJKONTA");
    uistavkeBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeBROJKONTA.setPrecision(8);
    uistavkeBROJKONTA.setRowId(true);
    uistavkeBROJKONTA.setTableName("UISTAVKE");
    uistavkeBROJKONTA.setServerColumnName("BROJKONTA");
    uistavkeBROJKONTA.setSqlType(1);
    uistavkeBROJDOK.setCaption("Broj dokumenta");
    uistavkeBROJDOK.setColumnName("BROJDOK");
    uistavkeBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeBROJDOK.setPrecision(50);
    uistavkeBROJDOK.setRowId(true);
    uistavkeBROJDOK.setTableName("UISTAVKE");
    uistavkeBROJDOK.setServerColumnName("BROJDOK");
    uistavkeBROJDOK.setSqlType(1);
    uistavkeBROJDOK.setWidth(30);
    uistavkeRBS.setCaption("RBS");
    uistavkeRBS.setColumnName("RBS");
    uistavkeRBS.setDataType(com.borland.dx.dataset.Variant.INT);
    uistavkeRBS.setPrecision(6);
    uistavkeRBS.setRowId(true);
    uistavkeRBS.setTableName("UISTAVKE");
    uistavkeRBS.setServerColumnName("RBS");
    uistavkeRBS.setSqlType(4);
    uistavkeRBS.setWidth(6);
    uistavkeCORG.setCaption("OJ");
    uistavkeCORG.setColumnName("CORG");
    uistavkeCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeCORG.setPrecision(12);
    uistavkeCORG.setTableName("UISTAVKE");
    uistavkeCORG.setServerColumnName("CORG");
    uistavkeCORG.setSqlType(1);
    uistavkeID.setCaption("Dugovni iznos");
    uistavkeID.setColumnName("ID");
    uistavkeID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    uistavkeID.setPrecision(17);
    uistavkeID.setScale(2);
    uistavkeID.setDisplayMask("###,###,##0.00");
    uistavkeID.setDefault("0");
    uistavkeID.setTableName("UISTAVKE");
    uistavkeID.setServerColumnName("ID");
    uistavkeID.setSqlType(2);
    uistavkeIP.setCaption("Potražni iznos");
    uistavkeIP.setColumnName("IP");
    uistavkeIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    uistavkeIP.setPrecision(17);
    uistavkeIP.setScale(2);
    uistavkeIP.setDisplayMask("###,###,##0.00");
    uistavkeIP.setDefault("0");
    uistavkeIP.setTableName("UISTAVKE");
    uistavkeIP.setServerColumnName("IP");
    uistavkeIP.setSqlType(2);
    uistavkeCKOLONE.setCaption("Broj kolone u knjizi");
    uistavkeCKOLONE.setColumnName("CKOLONE");
    uistavkeCKOLONE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    uistavkeCKOLONE.setPrecision(2);
    uistavkeCKOLONE.setTableName("UISTAVKE");
    uistavkeCKOLONE.setServerColumnName("CKOLONE");
    uistavkeCKOLONE.setSqlType(5);
    uistavkeCKOLONE.setWidth(2);
    uistavkeCKNJIGE.setCaption("Oznaka knjige");
    uistavkeCKNJIGE.setColumnName("CKNJIGE");
    uistavkeCKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeCKNJIGE.setPrecision(5);
    uistavkeCKNJIGE.setTableName("UISTAVKE");
    uistavkeCKNJIGE.setServerColumnName("CKNJIGE");
    uistavkeCKNJIGE.setSqlType(1);
    uistavkeURAIRA.setCaption("Indikator URA/IRA");
    uistavkeURAIRA.setColumnName("URAIRA");
    uistavkeURAIRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeURAIRA.setPrecision(1);
    uistavkeURAIRA.setTableName("UISTAVKE");
    uistavkeURAIRA.setServerColumnName("URAIRA");
    uistavkeURAIRA.setSqlType(1);
    uistavkeDUGPOT.setCaption("Dugovna/potražna");
    uistavkeDUGPOT.setColumnName("DUGPOT");
    uistavkeDUGPOT.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeDUGPOT.setPrecision(1);
    uistavkeDUGPOT.setTableName("UISTAVKE");
    uistavkeDUGPOT.setServerColumnName("DUGPOT");
    uistavkeDUGPOT.setSqlType(1);
    uistavkeCGKSTAVKE.setCaption("CNALOGA+RBS gkstavke");
    uistavkeCGKSTAVKE.setColumnName("CGKSTAVKE");
    uistavkeCGKSTAVKE.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeCGKSTAVKE.setPrecision(38);
    uistavkeCGKSTAVKE.setTableName("UISTAVKE");
    uistavkeCGKSTAVKE.setServerColumnName("CGKSTAVKE");
    uistavkeCGKSTAVKE.setSqlType(1);
    uistavkeCGKSTAVKE.setWidth(30);
    uistavkeSTAVKA.setCaption("Stavka sheme");
    uistavkeSTAVKA.setColumnName("STAVKA");
    uistavkeSTAVKA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    uistavkeSTAVKA.setPrecision(2);
    uistavkeSTAVKA.setTableName("UISTAVKE");
    uistavkeSTAVKA.setServerColumnName("STAVKA");
    uistavkeSTAVKA.setSqlType(5);
    uistavkeSTAVKA.setWidth(2);
    uistavkeCSKL.setCaption("Vrsta sheme");
    uistavkeCSKL.setColumnName("CSKL");
    uistavkeCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    uistavkeCSKL.setPrecision(12);
    uistavkeCSKL.setTableName("UISTAVKE");
    uistavkeCSKL.setServerColumnName("CSKL");
    uistavkeCSKL.setSqlType(1);
    uistavke.setResolver(dm.getQresolver());
    uistavke.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from UIstavke", null, true, Load.ALL));
 setColumns(new Column[] {uistavkeLOKK, uistavkeAKTIV, uistavkeKNJIG, uistavkeCPAR, uistavkeVRDOK, uistavkeBROJKONTA, uistavkeBROJDOK, uistavkeRBS,
        uistavkeCORG, uistavkeID, uistavkeIP, uistavkeCKOLONE, uistavkeCKNJIGE, uistavkeURAIRA, uistavkeDUGPOT, uistavkeCGKSTAVKE, uistavkeSTAVKA, uistavkeCSKL}); */
  }

  /*public void setall() {

    ddl.create("UIstavke")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addInteger("cpar", 6, true)
       .addChar("vrdok", 3, true)
       .addChar("brojkonta", 8, true)
       .addChar("brojdok", 50, true)
       .addInteger("rbs", 6, true)
       .addChar("corg", 12)
       .addFloat("id", 17, 2)
       .addFloat("ip", 17, 2)
       .addShort("ckolone", 2)
       .addChar("cknjige", 5)
       .addChar("uraira", 1)
       .addChar("dugpot", 1)
       .addChar("cgkstavke", 38)
       .addShort("stavka", 2)
       .addChar("cskl", 12)
       .addPrimaryKey("knjig,cpar,vrdok,brojkonta,brojdok,rbs");


    Naziv = "UIstavke";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpar", "brojdok", "rbs"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
