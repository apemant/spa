/****license*****************************************************************
**   file: upSkladKartica.java
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
package hr.restart.robno;

import hr.restart.util.Aus;
import hr.restart.util.lookupData;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author S.G.
 *
 * Started 2005.04.19
 * 
 */

public class upSkladKartica extends upKartica {

  static upSkladKartica upsk;
  
  public upSkladKartica() {
    super();
    upsk = this;
    System.out.println("Eeeeee..... ovo je skladisna kartica :)"); //XDEBUG delete when no more needed
  }
  
  public static upSkladKartica getupsKartica(){
    if (upsk == null) {
      upsk = new upSkladKartica();
    }
    return upsk;
  }


  public void okPress() {
    java.math.BigDecimal tempKOL = main.nul;
    java.math.BigDecimal tempVR = main.nul;
    //java.math.BigDecimal tempKOL2 = main.nul;
    //java.math.BigDecimal tempSUM = main.nul;
    java.math.BigDecimal tempUl = main.nul;
    java.math.BigDecimal tempIz = main.nul;
//    java.math.BigDecimal tempUk = main.nul;
    java.math.BigDecimal tempZad = main.nul;
    java.math.BigDecimal tempRaz = main.nul;
    boolean insertNewRow=false;
    dateP = new java.sql.Date(this.tds.getTimestamp("pocDatum").getTime());
    dateZ = new java.sql.Date(this.tds.getTimestamp("zavDatum").getTime());
    String qStr;
    newDateP = rut.getTimestampValue(tds.getTimestamp("pocDatum"), 0);
    newDateZ = rut.getTimestampValue(tds.getTimestamp("zavDatum"), 1);

    lookupData.getlookupData().raLocate(dm.getSklad(), new String[] {"CSKL"}, new String[] {rpcskl.getCSKL()});
    vrzal = dm.getSklad().getString("VRZAL");

    qStr = rdUtil.getUtil().getSklKarticaArtikla(cart, vrzal, rpcskl.getCSKL(), rut.getDoc("DOKU", "STDOKU"), rut.getDoc("DOKI", "STDOKI"), newDateZ);

//    System.out.println("qdst KARTICA : " + qStr);

    vl.execSQL(qStr);
    QueryDataSet card = vl.getDataAndClear();
//    card.close();
//    card.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    card.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    card.setColumns(new Column[] {
      (Column) colUI.clone(),
      (Column) colSRT.clone(),
      (Column) hr.restart.baza.dM.getDataModule().getDoku().getColumn("VRDOK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getDoku().getColumn("BRDOK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStdoku().getColumn("RBR").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getDoku().getColumn("DATDOK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getDoki().getColumn("CORG").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getDoki().getColumn("CVRTR").clone(),
      (Column) colKUl.clone(),
      (Column) colKIz.clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStdoku().getColumn("ZC").clone(),
      (Column) colZad.clone(),
      (Column) colRaz.clone(),
      (Column) col1.clone(),
      (Column) col2.clone(),
      (Column) colUlSk.clone(),
      (Column) colIzSk.clone()
    });

    card.getColumn("VRDOK").setWidth(7);
    card.getColumn("DATDOK").setWidth(9);
    card.getColumn("KOLUL").setWidth(14);
    card.getColumn("KOLIZ").setWidth(14);
    card.getColumn("SKOL").setWidth(14);
    card.getColumn("CORG").setCaption("Mjesto troška");


    card.getColumn("UI").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("SRT").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("RBR").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("SKLUL").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("SKLIZ").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    
    //additionalDisable();
    card.getColumn("ZC").setVisible(com.borland.jb.util.TriStateProperty.FALSE); 
    card.getColumn("SIZN").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("KOLZAD").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("KOLRAZ").setVisible(com.borland.jb.util.TriStateProperty.FALSE);

    openScratchDataSet(card);
    card = timeReset(card);
    card.setSort(new SortDescriptor(new String[]{"DATDOK","SRT","UI","BRDOK","RBR"}));

    card.open();

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(card);

//      OLD: card.setSort(new SortDescriptor(new String[]{"BRDOK", "RBR", "SRT", "UI"}));

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(card);

    if (card.rowCount() == 0) setNoDataAndReturnImmediately();

    card.first();
      do {
        Aus.sub(card, "SKOL", "KOLUL", "KOLIZ");
        Aus.sub(card, "SIZN", "KOLZAD", "KOLRAZ");
        tempKOL=tempKOL.add(card.getBigDecimal("SKOL"));
        tempVR=tempVR.add(card.getBigDecimal("SIZN"));
        if (card.getTimestamp("DATDOK").before(java.sql.Timestamp.valueOf(newDateP.substring(1,newDateP.length()-2)))) {
          tempUl=tempUl.add(card.getBigDecimal("KOLUL"));
          tempIz=tempIz.add(card.getBigDecimal("KOLIZ"));
          tempZad = tempZad.add(card.getBigDecimal("KOLZAD"));
          tempRaz = tempRaz.add(card.getBigDecimal("KOLRAZ"));
          card.deleteRow();
          if (!insertNewRow) insertNewRow=true;
        }
        else {
          card.next();
        }
      } while (card.inBounds());

      card.first(); // mozda je to mislio (?)

      DataRow temprow = new DataRow(card);
      String[] cols = temprow.getColumnNames(temprow.getColumnCount());

      if(jcbDonos.isSelected()) {
        if (insertNewRow) {
//          System.out.println("row 0 : " + card.getRow());
          DataSet.copyTo(cols, card, cols, temprow);
//          System.out.println("row A : " + card.getRow());
          card.insertRow(false);
//          System.out.println("row B : " + card.getRow());
          DataSet.copyTo(cols, temprow, cols, card);
//          System.out.println("row C : " + card.getRow());

          card.first();
          card.clearValues();
//          System.out.println("row D : " + card.getRow());
//
          card.setString(0, "A");
          card.setString(1, "A");
          card.setString(2, "DON");
          card.setInt(3,0);
          card.setShort(4,(short)0);
          card.setTimestamp(5, tds.getTimestamp("pocDatum"));
          card.setBigDecimal(6, tempUl);
          card.setBigDecimal(7, tempIz);
          //card.setBigDecimal(8, rut.divideValue(tempSUM, tempKOL2));
          card.setBigDecimal(9, tempZad);
          card.setBigDecimal(10, tempRaz);
          try {
            card.setBigDecimal("SKOL", tempUl.subtract(tempIz));
            card.setBigDecimal("SIZN", tempZad.subtract(tempRaz));
            card.setBigDecimal(8, rut.divideValue(
                card.getBigDecimal("SIZN"), card.getBigDecimal("SKOL")));
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
//          System.out.println("row E : " + card.getRow());
//          sysoutTEST syst = new sysoutTEST(false);
//          syst.prn(card);
        }
      }


      if (card.rowCount() == 0) setNoDataAndReturnImmediately();

//      this.getJPTV().setDataSet(card);
      getJPTV().setDataSetAndSums(card, new String[] {"KOLUL","KOLIZ","KOLZAD","KOLRAZ"});
      System.out.println("CART poslije tablice " + rpcart.getCART());
  }
  
  /*protected void additionalDisable(){
    card.getColumn("ZC").setVisible(com.borland.jb.util.TriStateProperty.FALSE); 
    card.getColumn("SIZN").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("KOLZAD").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("KOLRAZ").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
  }*/

  public int[] navVisibleColumns(){
    return new int[] {0,1,2,3,4,5};
  }
  
  protected void handleReports() {
  this.addReport("hr.restart.robno.repKartica","hr.restart.robno.repKartica","SkladKartica", "Ispis kartice artikla");
  this.addReport("hr.restart.robno.repKartica2Reda","hr.restart.robno.repKartica2Reda","SkladKartica2Reda", "Ispis kartice artikla - 2 reda");
  }
}

