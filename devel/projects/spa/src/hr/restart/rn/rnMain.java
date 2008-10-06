/****license*****************************************************************
**   file: rnMain.java
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
package hr.restart.rn;

import hr.restart.util.startFrame;

import java.awt.AWTEvent;

import javax.swing.JMenuBar;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>ne
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class rnMain extends startFrame {
  public static hr.restart.rn.jrnMenu rnMnu;
  public static hr.restart.rn.menuOpRn oprnMnu;
  public static hr.restart.rn.menuObradeRn obrrnMnu;
  public static hr.restart.rn.menuPreglediRn prernMnu;
  public static hr.restart.rn.menuDocsRn docrnMnu;
  JMenuBar jmMain = new JMenuBar();

  public rnMain() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static javax.swing.JMenu getJrnMenu(startFrame startframe) {
    rnMnu = new jrnMenu(startframe);
    return rnMnu;
  }
  public static javax.swing.JMenu getOpRnMenu(startFrame startframe) {
    oprnMnu = new menuOpRn(startframe);
    return oprnMnu;
  }
  public static javax.swing.JMenu getDocsRnMenu(startFrame startframe) {
    docrnMnu = new menuDocsRn(startframe);
    return docrnMnu;
  }
  public static javax.swing.JMenu getPreglediRnMenu(startFrame startframe) {
    prernMnu = new menuPreglediRn(startframe);
    return prernMnu;
  }
  public static javax.swing.JMenu getObradeRnMenu(startFrame startframe) {
    obrrnMnu = new menuObradeRn(startframe);
    return obrrnMnu;
  }
  private void jbInit() throws Exception  {
    jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmMain.add(hr.restart.rn.rnMain.getOpRnMenu(this));
    jmMain.add(hr.restart.rn.rnMain.getJrnMenu(this));
    jmMain.add(hr.restart.rn.rnMain.getDocsRnMenu(this));
    jmMain.add(hr.restart.rn.rnMain.getPreglediRnMenu(this));
    jmMain.add(hr.restart.rn.rnMain.getObradeRnMenu(this));
    this.setRaJMenuBar(jmMain);
  }
}