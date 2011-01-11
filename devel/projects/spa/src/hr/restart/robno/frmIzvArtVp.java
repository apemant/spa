/****license*****************************************************************
**   file: frmIzvArtVp.java
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

import com.borland.dx.sql.dataset.QueryDescriptor;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class frmIzvArtVp extends frmIzvArt {

//  rapancart rpcart = new rapancart(){
//    public void metToDo_after_lookUp() {
//    }
//    public void findFocusAfter() {
//      jraDatumOd.requestFocus();
//    }
//  };

  public static frmIzvArt getInstanceVp() {
    return newInstance;
  }
  
  static frmIzvArtVp newInstance = null;

  public frmIzvArtVp() {
    super('V');
    newInstance = this;
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  private void jbInit() throws Exception {

    rcbVrDok.setRaItems(new String[][] {
      {"Svi veleprodajni dokumenti",""},
      {"Veleprodaja i maloprodaja","ALL"},
      {"ROT - Raèuni otpremnice","ROT"},
      {"POD - Povratnica odobrenje","POD"}
    });

  }
  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }
  public void okPress(){
    System.out.println("vrstadok " + vrstaDok);
    rcc.EnabDisabAll(rpcart, false);
    try {
      if(preparePrint()==0) {
        setNoDataAndReturnImmediately();
        rpcart.setCART();
        rcc.EnabDisabAll(rpcart, true);
      }
      
      reporting();
      
    } catch (Exception ex) {
      setNoDataAndReturnImmediately();
      ex.printStackTrace();
    }

  }


  protected int preparePrint() {
    System.out.println("VELEPRODAJA"); //XDEBUG delete when no more needed
    vrdoks = "veleprodajni";
    String typ = "VELE";
    if (vrstaDok.equals("ALL")) {
      vrstaDok = "";
      vrdoks = "Veleprodaja i maloprodaja";
      typ = "ALL";
    }
    qds.close();
//    qds2.close();
    qds3.close();

    datOd = tds.getTimestamp("pocDatum");
    datDo = tds.getTimestamp("zavDatum");
    String uvjet=  "";

    if (!rpcart.findCART(podgrupe).equals("")){
      uvjet = "AND "+ rpcart.findCART(podgrupe);
    }

    /*"AND "+rpcart.findCART(podgrupe);*/

    String qStr = rdUtil.getUtil().getIzArt(rpcskl.jrfCSKL.getText(), rpcart.jrfCART.getText(), utRobno.getTimestampValue(datOd, utRobno.NUM_FIRST),
        utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok, vrstaArt, uvjet,typ,"");
    //---------------------------------------------------------------------------------
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));

//    String qStr2 = rdUtil.getUtil().getIzArt2(rpcskl.jrfCSKL.getText(), rpcart.jrfCART.getText(), utRobno.getTimestampValue(datOd, utRobno.NUM_FIRST),
//        utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok, vrstaArt, uvjet,"VELE");
    //---------------------------------------------------------------------------------
//    qds2.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr2));

    String qStr3 = rdUtil.getUtil().getIzArt3(rpcskl.jrfCSKL.getText(), rpcart.jrfCART.getText(), utRobno.getTimestampValue(datOd, utRobno.NUM_FIRST),
        utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok, vrstaArt, uvjet,"VELE","");
    //---------------------------------------------------------------------------------
    qds3.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr3));

    
    qds.open();
//    qds2.open();
    qds3.open();

    return qds.getRowCount();
  }
  
  public String getVrsta(){
    return vrdoks;
  }


}
