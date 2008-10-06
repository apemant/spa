/****license*****************************************************************
**   file: Blagajnici.java
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



public class Blagajnici extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Blagajnici Blagajniciclass;

  QueryDataSet blagajnici = new raDataSet();

  Column blagajniciLOKK = new Column();
  Column blagajniciAKTIV = new Column();
  Column blagajniciCBLAGAJNIK = new Column();
  Column blagajniciNAZBLAG = new Column();
  Column blagajniciTIP = new Column();
  Column blagajniciSUPER = new Column();
  Column blagajniciBRISANJE = new Column();
  Column blagajniciLOZINKA = new Column();

  public static Blagajnici getDataModule() {
    if (Blagajniciclass == null) {
      Blagajniciclass = new Blagajnici();
    }
    return Blagajniciclass;
  }

  public QueryDataSet getQueryDataSet() {
    return blagajnici;
  }

  public Blagajnici() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    blagajniciLOKK.setCaption("Status zauzetosti");
    blagajniciLOKK.setColumnName("LOKK");
    blagajniciLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajniciLOKK.setPrecision(1);
    blagajniciLOKK.setTableName("BLAGAJNICI");
    blagajniciLOKK.setServerColumnName("LOKK");
    blagajniciLOKK.setSqlType(1);
    blagajniciLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    blagajniciLOKK.setDefault("N");
    blagajniciAKTIV.setCaption("Aktivan - neaktivan");
    blagajniciAKTIV.setColumnName("AKTIV");
    blagajniciAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajniciAKTIV.setPrecision(1);
    blagajniciAKTIV.setTableName("BLAGAJNICI");
    blagajniciAKTIV.setServerColumnName("AKTIV");
    blagajniciAKTIV.setSqlType(1);
    blagajniciAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    blagajniciAKTIV.setDefault("D");
    blagajniciCBLAGAJNIK.setCaption("Blagajnik");
    blagajniciCBLAGAJNIK.setColumnName("CBLAGAJNIK");
    blagajniciCBLAGAJNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajniciCBLAGAJNIK.setPrecision(3);
    blagajniciCBLAGAJNIK.setRowId(true);
    blagajniciCBLAGAJNIK.setTableName("BLAGAJNICI");
    blagajniciCBLAGAJNIK.setServerColumnName("CBLAGAJNIK");
    blagajniciCBLAGAJNIK.setSqlType(1);
    blagajniciNAZBLAG.setCaption("Naziv");
    blagajniciNAZBLAG.setColumnName("NAZBLAG");
    blagajniciNAZBLAG.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajniciNAZBLAG.setPrecision(40);
    blagajniciNAZBLAG.setTableName("BLAGAJNICI");
    blagajniciNAZBLAG.setServerColumnName("NAZBLAG");
    blagajniciNAZBLAG.setSqlType(1);
    blagajniciNAZBLAG.setWidth(30);
    blagajniciTIP.setCaption("Tip");
    blagajniciTIP.setColumnName("TIP");
    blagajniciTIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajniciTIP.setPrecision(1);
    blagajniciTIP.setTableName("BLAGAJNICI");
    blagajniciTIP.setServerColumnName("TIP");
    blagajniciTIP.setSqlType(1);
    blagajniciSUPER.setCaption("Super");
    blagajniciSUPER.setColumnName("SUPER");
    blagajniciSUPER.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajniciSUPER.setPrecision(1);
    blagajniciSUPER.setTableName("BLAGAJNICI");
    blagajniciSUPER.setServerColumnName("SUPER");
    blagajniciSUPER.setSqlType(1);
    blagajniciBRISANJE.setCaption("Brisanje");
    blagajniciBRISANJE.setColumnName("BRISANJE");
    blagajniciBRISANJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajniciBRISANJE.setPrecision(1);
    blagajniciBRISANJE.setTableName("BLAGAJNICI");
    blagajniciBRISANJE.setServerColumnName("BRISANJE");
    blagajniciBRISANJE.setSqlType(1);
    blagajniciLOZINKA.setCaption("Lozinka");
    blagajniciLOZINKA.setColumnName("LOZINKA");
    blagajniciLOZINKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagajniciLOZINKA.setPrecision(16);
    blagajniciLOZINKA.setTableName("BLAGAJNICI");
    blagajniciLOZINKA.setServerColumnName("LOZINKA");
    blagajniciLOZINKA.setSqlType(1);
    blagajniciLOZINKA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    blagajnici.setResolver(dm.getQresolver());
    blagajnici.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Blagajnici", null, true, Load.ALL));
    setColumns(new Column[] {blagajniciLOKK, blagajniciAKTIV, blagajniciCBLAGAJNIK, blagajniciNAZBLAG, blagajniciTIP, blagajniciSUPER, 
        blagajniciBRISANJE, blagajniciLOZINKA});
  }

  public void setall() {

    ddl.create("Blagajnici")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cblagajnik", 3, true)
       .addChar("nazblag", 40)
       .addChar("tip", 1)
       .addChar("super", 1)
       .addChar("brisanje", 1)
       .addChar("lozinka", 16)
       .addPrimaryKey("cblagajnik");


    Naziv = "Blagajnici";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
