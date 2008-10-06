/****license*****************************************************************
**   file: frmVir.java
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

import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class frmVir extends startFrame {
  private JMenuBar jmbVir = new JMenuBar();
  private JMenu jmVirmani = new JMenu();
  private JMenuItem jmiVirmani = new JMenuItem();

  public frmVir() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jmVirmani.setText("Virmani");
    jmiVirmani.setText("Pisanje virmana");
    jmiVirmani.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVirmani_actionPerformed(e);
      }
    });
    jmbVir.add(frmZapod.getJzpMenu(this));
    jmbVir.add(jmVirmani);
    jmVirmani.add(jmiVirmani);
    setRaJMenuBar(jmbVir);
  }
  public void ShowMe(boolean b, String s) {
    super.ShowMe(b,s);
    jmiVirmani_actionPerformed(null);
  }
  public void showMeP(String s) {
    super.ShowMeP(s);
    jmiVirmani_actionPerformed(null);
  }
  void jmiVirmani_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.zapod.frmVirmani",jmiVirmani.getText());
  }
}