/****license*****************************************************************
**   file: repIspVirNewSmall2.java
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
package hr.restart.zapod;

import com.borland.dx.sql.dataset.QueryDataSet;

public class repIspVirNewSmall2 extends repMatrixVirmans {

  public repIspVirNewSmall2() {
    init();
  }

  public String[] getDetail(){
    String[] detail = new String[] {
      "<$CondensedOFF$>"+
      "<#DUMMY|24|left#><#ROW1|33|left#>"+
      "<$newline$><$newline$>"+
      "<#DUMMY|34|left#><#ROW9|24|right#>"+
      "<$newline$><$newline$>"+
      "<#DUMMY|4|left#><#ROW2|100|left#>"+
      "<$newline$>"+
      "<$CondensedOFF$><#DUMMY|4|left#><$CondensedON$><#ROW3|100|left#>"+
      "<$CondensedOFF$><$newline$>"+
      "<#DUMMY|4|left#><#ROW4|100|left#>"+
      "<$newline$><$newline$>"+
      "<$CondensedOFF$><#DUMMY|4|left#><#ROW5|100|left#>"+
      "<$newline$>"+
      "<$CondensedOFF$><#DUMMY|4|left#><$CondensedON$><#ROW6|80|left#>"+
      "<$newline$><$CondensedOFF$>"+
      "<#DUMMY|4|left#><#ROW7|100|left#>"+
      "<$newline$><$CondensedOFF$><$newline$>"+
      "<#DUMMY|13|left#><#ROW8|88|left#><$CondensedOFF$>"+
      "<$newline$><$newline$>"+
      "<#DUMMY|4|left#><#ROW10|12|left#><$newline$><$newline$>"+
      "<#DUMMY|4|left#><#ROW11|12|left#>"+
      "<$newline$><$newline$><$newline$>"+
      "<$newline$><$newline$><$newline$>"+
      "<$newline$>"
    };
    return detail;
  }

  private QueryDataSet getSelectedDS() {
    return (QueryDataSet)frmVirmani.getInstance().getRaQueryDataSet();
  }

  public void fill() {
    qds = getSelectedDS();
    qds.open();
    qds.first();
    boolean newVir;
    while(qds.inBounds()) {
      newVir = frmVirmani.isNewVir();
      if (newVir){
        int tmpint;
        try {
          tmpint = Integer.parseInt(qds.getString("JEDZAV"));
          newVir = false;
        } catch (Exception ex) {}
      }
      virmanDS.open();
      virmanDS.insertRow(false);
      virmanDS.setString("ROW1", !newVir ? handleHHPUI("NNDNN") : handleHHPUI(qds.getString("JEDZAV")));
      virmanDS.setString("ROW9", handleIznos(qds.getBigDecimal("IZNOS")));
      svrha.clear();
      handleSvrha(replaceReturn(qds.getString("NATERET").trim()));
      checkLL();
      virmanDS.setString("ROW2", handleGlobal(svrha.get(0).toString()," "+qds.getString("PNBZ1"),qds.getString("BRRACNT"),""));
      virmanDS.setString("ROW3", svrha.get(1).toString());
      virmanDS.setString("ROW4", handlePNB(svrha.get(2).toString(),qds.getString("PNBZ2"),qds.getString("PNBZ1")));

      svrha.clear();
      handleSvrha(replaceReturn(qds.getString("UKORIST")));
      checkLL();
      virmanDS.setString("ROW5", handleGlobal(svrha.get(0).toString()," "+qds.getString("PNBO1"),qds.getString("BRRACUK"),replaceReturn(qds.getString("BRRACUK").trim())));
      virmanDS.setString("ROW6", svrha.get(1).toString());
      virmanDS.setString("ROW7", handlePNB(svrha.get(2).toString(),qds.getString("PNBO2"),qds.getString("PNBO1")));
      virmanDS.setString("ROW8", getSOPiOP(qds.getString("SIF1"),qds.getString("SVRHA")));
      virmanDS.setString("ROW10", getDatum());
      virmanDS.setString("ROW11", getDatumPod());

      virmanDS.setString("DUMMY", " ");
      virmanDS.post();
      qds.next();
    }
  }
}