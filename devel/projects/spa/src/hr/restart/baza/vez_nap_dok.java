/****license*****************************************************************
**   file: vez_nap_dok.java
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


public class vez_nap_dok extends KreirDrop implements DataModule {

  private static vez_nap_dok vez_nap_dokclass;
  dM dm  = dM.getDataModule();
  QueryDataSet vez_nap_dok = new QueryDataSet();


   public static vez_nap_dok getDataModule() {
    if (vez_nap_dokclass == null) {
      vez_nap_dokclass = new vez_nap_dok();
    }
    return vez_nap_dokclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vez_nap_dok;
  }
  public vez_nap_dok() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
  }

  public void setall(){

    /*SqlDefTabela = "create table vez_nap_dok " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cskl char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta
      "vrdok char(3) CHARACTER SET WIN1250 not null," +   //Vrsta dokumenta (OTP,PRI,..)
      "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
      "brdok numeric(6,0) not null , " + // Broj dokumenta
      "lrbr char(3) character set win1250 not null, "+
      "naznap char(200) character set win1250,"+
      "Primary Key (cskl,vrdok,god,brdok,lrbr))" ; */

    ddl.create("vez_nap_dok")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 6, true)
       .addChar("vrdok", 3, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addChar("lrbr", 3, true)
       .addChar("naznap", 200)
       .addPrimaryKey("cskl,vrdok,god,brdok,lrbr");

    Naziv="vez_nap_dok";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


    /*
    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"ivez_nap_dokkey on vez_nap_dok (cskl,vrdok,god,brdok,lrbr)" };

    NaziviIdx=new String[]{"ivez_nap_dokkey" }; */
  }
}