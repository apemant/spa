/****license*****************************************************************
**   file: dlgProgressBar.java
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
package hr.restart.robno;

import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class dlgProgressBar extends JraDialog {
  static dlgProgressBar dProgressBar;
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JProgressBar jProgressBar1 = new JProgressBar();

  public dlgProgressBar(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
      dProgressBar=this;
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public dlgProgressBar() {
    this(null, "", false);
  }
  public static dlgProgressBar getdlgProgressBar()
  {
    if (dProgressBar == null)
    {
      dProgressBar = new dlgProgressBar();
    }
    return dProgressBar;
  }
  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    getContentPane().add(panel1);
    panel1.add(jProgressBar1,  BorderLayout.SOUTH);
  }
}