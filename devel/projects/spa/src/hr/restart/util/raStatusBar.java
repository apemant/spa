/****license*****************************************************************
**   file: raStatusBar.java
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
package hr.restart.util;

import hr.restart.start;
import hr.restart.swing.JraProgressBar;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raStatusBar extends JPanel {
//  static raStatusBar status;
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jlMsg = new JLabel();
  JLabel jlDate = new JLabel();
  JraProgressBar progBar = new JraProgressBar();
  hr.restart.sisfun.raDelayWindow delayWin;
  private boolean showDelayWindow = false;
  public raStatusBar() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    jlMsg.setBorder(BorderFactory.createEtchedBorder());
    jlMsg.setToolTipText("");
    jlDate.setBorder(BorderFactory.createEtchedBorder());
    jlDate.setText("RA");
    this.add(jlMsg, BorderLayout.CENTER);
    this.add(jlDate, BorderLayout.EAST);
    this.addPropertyChangeListener("statusMsg",new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if (delayWin != null && isShowDelayWindow()) {
          delayWin.setMessage(evt.getNewValue());
        }
      }
    });
    start.invokeAppleUtilMethod("initStatusBar", new raStatusBar[] {this}, new Class[] {raStatusBar.class});
  }
/**
 * Prikazuje text zadan u String msgText u status baru
 */
TimeTrack TT = new TimeTrack(false);
  public void statusMSG(String msgText){
    String oldVal = jlMsg.getText();
    jlMsg.setText(msgText);
    jlDate.setText(this.getDate());
    firePropertyChange("statusMsg",oldVal,msgText);
  }
/**
 * Metoda statusMSG pozvana bez parametara brise text u status baru
 */
  public void statusMSG(){
    String oldVal = jlMsg.getText();
    jlMsg.setText("");
    jlDate.setText(this.getDate());
    firePropertyChange("statusMsg",oldVal,"");
  }
/**
 * Vraca u string danasnji datum u formatu DD.MM.YYYY
 */
  public String getDate() {
    String dt = (String)start.invokeAppleUtilMethod("getStatusBarDate", null, null);  
    if (dt != null) {
      return dt;
    }
    java.util.Date currDate = new java.util.Date(System.currentTimeMillis());
    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy");
    return dateFormat.format(currDate).toString();
  }
/**
 * dodaje progressbar u statusbar i ceka na next()
 */
  public void startTask(int steps) {
     startTask(steps, "Obrada u tijeku ...");
  }
/**
 * dodaje progressbar u statusbar i ceka na next() i daje poruku u drugom parametru
 */
  public void startTask(int steps, String mess) {
    progBar.setMaximum(steps*10);
    if (isProgBarAdded()) {
      progBar.setValue(0);
      progBar.stopTimer();
    } else {
      add(progBar,BorderLayout.WEST);
      validate();
    }
    showDelayWindow();
    statusMSG(mess);
    progBar.startTimer();
  }
  private void showDelayWindow() {
    if (isShowDelayWindow()) {
      delayWin = hr.restart.sisfun.raDelayWindow.show();
      delayWin.setMessage("Obrada u tijeku ...");
    }
  }
  private void hideDelayWindow() {
    if (delayWin != null) {
      delayWin.close();
      delayWin = null;
    }
  }
  public boolean isProgBarAdded() {
    Component[] comps = getComponents();
    for (int i=0; i<comps.length;i++) {
      if (comps[i].equals(progBar)) return true;
    }
    return false;
  }
  /**
   * povecava progressbar za jednu jedinicu, naravno ako je prije toga pusten starttask
   */
  public void next() {
    next(null);
  }
  /**
   * povecava progressbar za jednu jedinicu, naravno ako je prije toga pusten starttask
   * drugi parametar je poruka
   */
  public void next(String mess) {
    if (!isProgBarAdded()) return;
    int val = progBar.getValue();
    val = val + 10;
    progBar.setValue(val);
    if (mess!=null) statusMSG(mess);
  }
  /**
   * Task je gotov pa micemo progressbar i cistimo poruku
   */
  public void finnishTask() {
    if (!isProgBarAdded()) return;
    progBar.setValue(0);
    progBar.stopTimer();
    hideDelayWindow();
    remove(progBar);
    validate();
    statusMSG();
  }
  public JraProgressBar getProgressBar() {
    return progBar;
  }
  public static raStatusBar getStatusBar() {
    if (hr.restart.start.isMainFrame()) {
      return hr.restart.mainFrame.getMainFrame().getStatusBar();
    } else {
//      return startFrame.getStartFrame().getStatusBar();
      return raLLFrames.getRaLLFrames().getMsgStartFrame().getStatusBar();
    }
  }

  public boolean isShowDelayWindow() {
    return showDelayWindow;
  }
  public void setShowDelayWindow(boolean _showDelayWindow) {
    showDelayWindow = _showDelayWindow;
    if (!showDelayWindow) hideDelayWindow();
  }
}