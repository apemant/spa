/****license*****************************************************************
**   file: crmMain.java
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
package hr.restart.crm;

import hr.restart.util.startFrame;

import java.awt.AWTEvent;

import javax.swing.JMenuBar;


/**
 * <p>Title: Crm</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class crmMain extends startFrame {
  public static crmMain _crmMain;
  //public static hr.restart.pos.jposMenu posMnu;
  public static hr.restart.crm.jcrmOpMenu crmOpMnu;
  public static hr.restart.crm.jcrmMenu crmMnu;
  
  JMenuBar jmMain = new JMenuBar();

  public crmMain() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
      _crmMain=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * Statichki getter
   */
  public static crmMain getCrmMain() {
    if (_crmMain == null) {
      _crmMain = new crmMain();
    }
    return _crmMain;
  }
  /*public static javax.swing.JMenu getJposMenu(startFrame startframe) {
    posMnu = new jposMenu(startframe);
    return posMnu;
  }*/
  
  public static javax.swing.JMenu getJcrmOpMenu(startFrame startframe) {
    crmOpMnu = new jcrmOpMenu(startframe);
    return crmOpMnu;
  }
  
  public static javax.swing.JMenu getJcrmMenu(startFrame startframe) {
    crmMnu = new jcrmMenu(startframe);
    return crmMnu;
  }
  
  private void jbInit() throws Exception  {
    jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmMain.add(hr.restart.crm.crmMain.getJcrmOpMenu(this));
    jmMain.add(hr.restart.crm.crmMain.getJcrmMenu(this));
    //jmMain.add(hr.restart.mp.frmMp.getOpMpMenu(this));
    //jmMain.add(hr.restart.pos.posMain.getJposMenu(this));
    this.setRaJMenuBar(jmMain);
  }
}