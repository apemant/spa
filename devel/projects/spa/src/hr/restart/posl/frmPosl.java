/****license*****************************************************************
**   file: frmPosl.java
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
package hr.restart.posl;


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

public class frmPosl extends startFrame {
  public static frmPosl _mpMain;
  public static hr.restart.posl.poslMenu mpMnu;
  public static hr.restart.posl.prometMenu promMnu;
  JMenuBar jmMain = new JMenuBar();

  public frmPosl() {
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
  public static frmPosl getMpMain() {
    if (_mpMain == null) {
      _mpMain = new frmPosl();
    }
    return _mpMain;
  }
  public static javax.swing.JMenu getMpMenu(startFrame startframe) {
    mpMnu = new poslMenu(startframe);
    return mpMnu;
  }
  public static javax.swing.JMenu getPrometMenu(startFrame startframe) {
    promMnu = new prometMenu(startframe);
    return promMnu;
  }
  private void jbInit() throws Exception  {
    jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmMain.add(hr.restart.robno._Main.getOpMenu(this));
    jmMain.add(hr.restart.posl.frmPosl.getPrometMenu(this));
    jmMain.add(hr.restart.posl.frmPosl.getMpMenu(this));
    this.setRaJMenuBar(jmMain);
  }
}
