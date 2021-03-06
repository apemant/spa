/****license*****************************************************************
**   file: ispInvSI.java
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
package hr.restart.os;

import hr.restart.util.Aus;

import javax.swing.JOptionPane;

public class ispInvSI extends ispInv {
  public ispInvSI() {
  }

  public boolean Validacija() {
    if(jpC.getCorg().equals("")) {
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos org. jedinice !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jpC.corg.requestFocus();
      return false;
    }
    return true;
  }

  public void okPress(){
    okpr = true;
    int rows = 0;
    rows = prepareIspis();
    if (rows == 0) setNoDataAndReturnImmediately();
    killAllReports();
    addReport("hr.restart.os.repIspisInvSI","Sitni inventar", 5);
  }

  public int prepareIspis() {
    String qStr="";    
    boolean selected = jpC.isRecursive(); //jcbPripOJ.isSelected();
    qStr = rdOSUtil.getUtil().getInvSI(jpC.getCorg().trim(), jrfLokacija.getText().trim(), selected);
    Aus.refilter(qds, qStr);
    return qds.getRowCount();
  }
}
