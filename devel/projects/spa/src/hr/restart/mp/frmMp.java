/****license*****************************************************************
**   file: frmMp.java
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
package hr.restart.mp;

import hr.restart.util.startFrame;

import java.awt.AWTEvent;

import javax.swing.JMenuBar;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class frmMp extends startFrame {
  public static frmMp _mpMain;
  JMenuBar jmMain = new JMenuBar();
//  JMenu jmPrometi = new JMenu();
  public static hr.restart.mp.menuOpMp opmpMnu;

  public frmMp() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
      _mpMain=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Statichki getter
   */
  public static frmMp getMpMain() {
    if (_mpMain == null) {
      _mpMain = new frmMp();
    }
    return _mpMain;
  }
  private void jbInit() throws Exception  {
    jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmMain.add(hr.restart.mp.frmMp.getOpMpMenu(this));
//    jmMain.add(jmPrometi);
//    jmMain.add(hr.restart.robno._Main.getUpitMenu(this));
    jmMain.add(hr.restart.robno._Main.getMaloMenu(this));
//    jmMain.add(hr.restart.pos.posMain.getJposMenu(this));
    jmMain.add(hr.restart.robno._Main.getIzvMaloMenu(this));

//    jmMain.add(hr.restart.robno._Main.getInvMenu(this));
//    jmMain.add(hr.restart.robno._Main.getObradeMenu(this));
//    jmPrometi.setText("Prometi");
//    jmPrometi.add(hr.restart.robno._Main.getSkladMenu(this));
//    jmPrometi.add(hr.restart.robno._Main.getNabavaMenu(this));
//    jmPrometi.add(hr.restart.robno._Main.getVeleMenu(this));
//    jmPrometi.add(hr.restart.robno._Main.getMesklaMenu(this));
//    jmPrometi.add(hr.restart.robno._Main.getNarMenu(this));

    this.setRaJMenuBar(jmMain);
  }
  public static javax.swing.JMenu getOpMpMenu(startFrame startframe) {
    opmpMnu = new menuOpMp(startframe);
    return opmpMnu;
  }
}
