/****license*****************************************************************
**   file: upTotSkladKar.java
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


/**
 * @author S.G.
 *
 * Started 2005.04.20
 * 
 */

public class upTotSkladKar extends upTotKar {
  
  private int[] visCols;

  public upTotSkladKar() {
    super();
    System.out.println("upTotSkaldKar .... I'AM ALIVE!!!"); //XDEBUG delete when no more needed
  }

  protected void handleColsAndRest() {
    vNull.getColumn("ZC").setVisible(0);
    vNull.getColumn("KOLRAZ").setVisible(0);
    vNull.getColumn("KOLZAD").setVisible(0);
    vNull.getColumn("SIZN").setVisible(0);
    vNull.getColumn("KOLDZAD").setVisible(0);
    
    if (ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")).equals(ut.getFirstDayOfYear(tds.getTimestamp("pocDatum")))) {
      this.addReport("hr.restart.robno.repTotKar","hr.restart.robno.repTotKar","TotSkladKar", "Totali prometa kartica");
      vNull.getColumn("KOLDON").setVisible(0);
      visCols = new int[] {0,1,2,3,4,5};
    } else {
//      this.addReport("hr.restart.robno.repTotKar","hr.restart.robno.repTotKar","TotKarDonos", "Totali prometa kartica");
      visCols = new int[] {0,1,2,3,4,5,6};
    }
  }
  
  public int[] navVisibleColumns() {
    return visCols;
  }

  public void jptv_doubleClick() {
    upStanjeNaSkladistu ussns = upStanjeNaSkladistu.getInstance();
    
    System.out.println(ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum"))); //XDEBUG delete when no more needed
    System.out.println(ut.getFirstDayOfYear(tds.getTimestamp("pocDatum"))); //XDEBUG delete when no more needed
    
    ussns.nTransDonos = !ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")).equals(ut.getFirstDayOfYear(tds.getTimestamp("pocDatum")));
    ussns.nTransDate = tds.getTimestamp("zavDatum");
    ussns.nTransDateOd = tds.getTimestamp("pocDatum");
    ussns.nTransData = this.getJPTV().getMpTable().getDataSet().getInt("CART");
    ussns.nTransCskl = rpcskl.getCSKL();
    _Main.getStartFrame().showFrame("hr.restart.robno.upSkladKartica", res.getString("upFrmKartica_title"));
    
  }
  
}
