/****license*****************************************************************
**   file: raRobnoTransferAbstract.java
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
package hr.restart.db;

import hr.restart.util.VarStr;
import hr.restart.util.raTransaction;

import com.borland.dx.sql.dataset.QueryDataSet;

abstract public class raRobnoTransferAbstract implements raRobnoTransferInterface {

  private String table_name="";
  private String filename="";
  private QueryDataSet forPrijenos;
  private hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  public int brGreske = 0;
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private java.sql.Timestamp datumdo = null;
  public abstract String sqlUpit(String qkeytab,String table_name);
  public String textGreske() {
    if (brGreske==1) {
      return "Greška pri prebacivanju kljuèeva iz "+table_name+" u replinfo";
    } else if (brGreske==2) {
      return "Nema podataka u "+table_name+" za prijenos";
    } else if (brGreske==3) {
      return "Neuspješan ftp prijenos "+table_name;
    } else if (brGreske==4) {
      return "Neispravan query za prikupljanje podataka "+table_name;
    }
    return "Nema greske";
  }

  public void setTable_name(String table_name){
    this.table_name= table_name;
  }

  public raRobnoTransferAbstract() {
    dm.getDataModule().loadModules();
    dm.getReplinfo().open();
  }

  private boolean addNewRecords(String qkeytab){
    try {
      String sqlUpit = sqlUpit(qkeytab,table_name);
//System.out.println(sqlUpit);
      if ("WRONG".equalsIgnoreCase(sqlUpit)) {
        brGreske = 4;
        return false;
      }
      QueryDataSet nedostajuci = hr.restart.util.Util.getNewQueryDataSet(sqlUpit,true);
      if (nedostajuci.getRowCount()!=0) {
        for (nedostajuci.first();nedostajuci.inBounds();nedostajuci.next()){
          dm.getReplinfo().insertRow(true);
          dm.getReplinfo().setString("IMETAB",table_name);
          dm.getReplinfo().setString("KEYTAB",nedostajuci.getString("slog"));
          if (nedostajuci.hasColumn("DATDOK")!=null) {
            dm.getReplinfo().setTimestamp("DATPROM",nedostajuci.getTimestamp("DATDOK"));
          } else {
            dm.getReplinfo().setTimestamp("DATPROM",hr.restart.util.Valid.getValid().getToday());
          }
          dm.getReplinfo().setShort("RBR_URL",(short)0);
          dm.getReplinfo().setShort("RBR_URL",(short)0);
          dm.getReplinfo().setString("REP_FLAG","N");
        }
        dm.getReplinfo().saveChanges();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean prepareData() {

    String qkeytab = raReplicate.getKeyTab(table_name);
    if (!addNewRecords(qkeytab)){
      brGreske = 1;
      return false;
    }

    String sqlup = "SELECT * FROM "+table_name+" where ("+qkeytab+") in "+
                   "(select replinfo.keytab from replinfo where rep_flag='N' and imetab='"+table_name+"'";
    String dodatak = ")";

    if (datumdo != null) {
      dodatak=" and datprom <="+rut.getTimestampValue(datumdo,rut.NUM_LAST)+")";
      sqlup = sqlup + dodatak;
    }
    forPrijenos = hr.restart.util.Util.getNewQueryDataSet(sqlup,true);
    if (forPrijenos.getRowCount()==0) {
      brGreske = 2;
      return false;
    }
    String upit2 = "update replinfo set rbr_url=1 where rep_flag='N' and imetab='"+
                            table_name+"' "+(new VarStr(dodatak)).chopRight(1);
    try {
      raTransaction.runSQL(upit2);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean dataToFile() {

    if (brGreske !=0 ) {
      System.out.println(textGreske());
    return false;}
    filename=table_name;
    hr.restart.baza.KreirDrop kd = hr.restart.baza.KreirDrop.getModuleByName(table_name);
    if (kd == null) {
      System.out.println("kd za "+table_name +" je null");
      return false;
    }
    hr.restart.baza.KreirDrop.getModuleByName(table_name).dumpTable(forPrijenos,new java.io.File(
        hr.restart.sisfun.frmParam.getParam
                                        ("robno","replDirLocal","c:/restart/trans",
                                         "Folder na klientu za prihvat potvrde transfera")),filename);
    return true;
  }

  public String getFileName(){
    return filename;
  }

  public boolean fileToData() {
    /**@todo Implement this hr.restart.db.raRobnoTransferInterface method*/
    throw new java.lang.UnsupportedOperationException("Method fileToData() not yet implemented.");
  }

  /**
   * oznaèene
  */
  public void updateForPrijenos(){

//    java.awt.Component jc;
//    jc.addComponentListener(null);

  }
  public void setDatumdo(java.sql.Timestamp datumdo) {
    this.datumdo = datumdo;
  }
  public java.sql.Timestamp getDatumdo() {
    return datumdo;
  }

  /**
   *  Ovdje treba pokupiti file sa servera i updatati replinfo ako postoji za kljuceve koji su prosli !!!
   */
  public boolean getFilefromServer(){
    return true;
  }
}