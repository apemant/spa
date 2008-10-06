/****license*****************************************************************
**   file: raFlatIspis.java
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

import hr.restart.swing.JraScrollPane;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raFlatIspis extends raFrame {
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      okPress();

    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };
  private javax.swing.JPanel JPan;
  JraScrollPane jScroll = new JraScrollPane();

  public raFlatIspis() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public raFlatIspis(hr.restart.util.startFrame owner, boolean modal) {
//    super (modal, owner);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    this.getContentPane().add(jScroll, BorderLayout.CENTER);
  }
  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_F10) {
      okPress();
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      cancelPress();
    }
  }
  public void okPress() {
  }
  public void cancelPress() {
    this.setVisible(false);
  }
  public void setJPan(javax.swing.JPanel newJPan) {
    JPan = newJPan;
    jScroll.getViewport().setLayout(new GridBagLayout());
    jScroll.getViewport().add(JPan);
  }
  public javax.swing.JPanel getJPan() {
    return JPan;
  }
}