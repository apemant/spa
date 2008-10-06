/****license*****************************************************************
**   file: frmRac.java
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
package hr.restart.rac;

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

public class frmRac extends startFrame {
  public static frmRac _racMain;
  public static hr.restart.rac.racMenu racMnu;
  public static menuIzvRac racIzv;
  public static hr.restart.rac.menuOpRac opracMnu;
  JMenuBar jmMain = new JMenuBar();

  public frmRac() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
      _racMain=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Statichki getter
   */
  public static frmRac getRacMain() {
    if (_racMain == null) {
      _racMain = new frmRac();
    }
    return _racMain;
  }
  public static javax.swing.JMenu getRacMenu(startFrame startframe) {
    racMnu = new racMenu(startframe);
    return racMnu;
  }
  public static javax.swing.JMenu getRacIzv(startFrame startframe) {
    racIzv = new menuIzvRac(startframe);
    return racIzv;
  }
  private void jbInit() throws Exception  {
    jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmMain.add(hr.restart.rac.frmRac.getOpRacMenu(this));
    jmMain.add(hr.restart.rac.frmRac.getRacMenu(this));
    jmMain.add(frmRac.getRacIzv(this));
    this.setRaJMenuBar(jmMain);
  }
  public static javax.swing.JMenu getOpRacMenu(startFrame startframe) {
    opracMnu = new menuOpRac(startframe);
    return opracMnu;
  }
}
