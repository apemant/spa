/****license*****************************************************************
**   file: frmDetailRS.java
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
package hr.restart.pl;

import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;

import java.awt.BorderLayout;

public class frmDetailRS extends raFrame {
  raJPTableView jptv = new raJPTableView();
  OKpanel okp = new OKpanel() {
    public void jPrekid_actionPerformed() {
      frmDetailRS.this.hide();
    }
    public void jBOK_actionPerformed() {
      fRS.getOKPanel().jBOK_actionPerformed();
    }
  };
  private frmRS fRS;
  public frmDetailRS(frmRS _fRS) {
    fRS = _fRS;
    jptv.setDataSet(fRS.detailRS);
    init();
  }
  private void init() {
//    jptv.setVisibleCols(new int[] {0,1,2,21});
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jptv,BorderLayout.CENTER);
    this.getContentPane().add(okp,BorderLayout.SOUTH);
    jptv.initKeyListener(this);
    okp.registerOKPanelKeys(this);
    okp.change_jBOK("Ispis",raImages.IMGPRINT);
    jptv.setKumTak(true);
    jptv.setStoZbrojiti(frmRS.sumcols);
    jptv.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jptv.addTableModifier(new raTableColumnModifier("CRADNIK",
        new String[] {"CRADNIK","PREZIME","IME"},
        hr.restart.baza.dM.getDataModule().getAllRadnici()));
  }
  public void pack() {
    super.pack();
    setSize(700,400);
  }
  public void show() {
    jptv.getColumnsBean().initialize();
    super.show();
  }
}