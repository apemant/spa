/****license*****************************************************************
**   file: Prava.java
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

public class Prava extends KreirDrop implements DataModule {

  private static Prava Pravaclass;
  dM dm  = dM.getDataModule();
  QueryDataSet prava = new raDataSet();
  Column pravaCPRAVA = new Column();
  Column pravaVRPRAVA = new Column();
  Column pravaPRAVO = new Column();
  Column pravaSIFRA = new Column();
  Column pravaKLJUC = new Column();
  Column pravaOPIS = new Column();

  public static Prava getDataModule() {
    if (Pravaclass == null) {
      Pravaclass = new Prava();
    }
    return Pravaclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return prava;
  }
  public Prava() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    pravaKLJUC.setCaption("Klju\u010D");
    pravaKLJUC.setColumnName("KLJUC");
    pravaKLJUC.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravaKLJUC.setPrecision(50);
    pravaKLJUC.setTableName("PRAVA");
    pravaKLJUC.setServerColumnName("KLJUC");
    pravaKLJUC.setSqlType(1);

    pravaSIFRA.setCaption("Šifra u domeni");
    pravaSIFRA.setColumnName("SIFRA");
    pravaSIFRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravaSIFRA.setPrecision(20);
    pravaSIFRA.setTableName("PRAVA");
    pravaSIFRA.setServerColumnName("SIFRA");
    pravaSIFRA.setSqlType(1);

    pravaOPIS.setCaption("Opis prava");
    pravaOPIS.setColumnName("OPIS");
    pravaOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravaOPIS.setPrecision(50);
    pravaOPIS.setTableName("PRAVA");
    pravaOPIS.setServerColumnName("OPIS");
    pravaOPIS.setWidth(30);
    pravaOPIS.setSqlType(1);

    pravaPRAVO.setAlignment(com.borland.dx.text.Alignment.CENTER);
    pravaPRAVO.setCaption("Pravo");
    pravaPRAVO.setColumnName("PRAVO");
    pravaPRAVO.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravaPRAVO.setPrecision(4);
    pravaPRAVO.setDefault("1000");
    pravaPRAVO.setTableName("PRAVA");
    pravaPRAVO.setWidth(6);
    pravaPRAVO.setServerColumnName("PRAVO");
    pravaPRAVO.setSqlType(1);
    pravaVRPRAVA.setAlignment(com.borland.dx.text.Alignment.CENTER);
    pravaVRPRAVA.setCaption("Domena");
    pravaVRPRAVA.setColumnName("VRPRAVA");
    pravaVRPRAVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravaVRPRAVA.setDefault("A");
    pravaVRPRAVA.setPrecision(1);
    pravaVRPRAVA.setTableName("PRAVA");
    pravaVRPRAVA.setWidth(8);
    pravaVRPRAVA.setServerColumnName("VRPRAVA");
    pravaVRPRAVA.setSqlType(1);
    pravaCPRAVA.setCaption("Šifra");
    pravaCPRAVA.setColumnName("CPRAVA");
    pravaCPRAVA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pravaCPRAVA.setPrecision(1);
    pravaCPRAVA.setTableName("PRAVA");
    pravaCPRAVA.setRowId(true);
    pravaCPRAVA.setWidth(7);
    pravaCPRAVA.setServerColumnName("CPRAVA");
    pravaCPRAVA.setSqlType(5);

    prava.setResolver(dm.getQresolver());
    prava.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM PRAVA", null, true, Load.ALL));
 setColumns(new Column[] {pravaCPRAVA, pravaVRPRAVA, pravaPRAVO, pravaSIFRA, pravaKLJUC, pravaOPIS});
  }

   public void setall(){

    ddl.create("prava")
       .addShort("cprava", 4, true)
       .addChar("vrprava", 1, "A")
       .addChar("pravo", 4)
       .addChar("sifra", 20)
       .addChar("kljuc", 50)
       .addChar("opis", 50)
       .addPrimaryKey("cprava");

    Naziv = "Prava";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

   }
}