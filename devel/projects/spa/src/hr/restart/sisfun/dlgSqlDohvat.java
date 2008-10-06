/****license*****************************************************************
**   file: dlgSqlDohvat.java
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
/*
 * Created on 2005.02.08
 *
 */
package hr.restart.sisfun;

import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;
import hr.restart.util.OKpanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


/**
 * @author abf
 */
public class dlgSqlDohvat {  
  static dlgSqlDohvat inst = new dlgSqlDohvat();
  
  
  JEditorPane query = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  JPanel main = new JPanel();
  JraScrollPane vp = new JraScrollPane();
  JraDialog win;
  
  
  boolean ok;
  
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };
  
  private dlgSqlDohvat() {
    try {
      jbInit();
    } catch(Exception e) {
      e.printStackTrace();
    }    
  }
  
  public static String showDlg(Container owner, String initialText) {
    return inst._showDlg(owner, initialText);
  }
  
  private String _showDlg(Container owner, String initialText) {
    Container realparent = null;
    String title = "SQL Dohvat";

    if (owner instanceof JComponent)
      realparent = ((JComponent) owner).getTopLevelAncestor();
    else if (owner instanceof Window)
      realparent = owner;

    if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, true);
    else win = new JraDialog((Frame) null, title, true);

    win.setContentPane(main);
    win.pack();
    if (realparent != null) win.setLocationRelativeTo(realparent);
    query.setText(initialText);
    win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    win.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        CancelPress();
      }
    });
    okp.registerOKPanelKeys(win);
    ok = false;
    win.show();
    return ok ? query.getText() : null;
  }
  
  void OKPress() {
    ok = true;
    CancelPress();
  }
  
  void CancelPress() {
    if (win != null) {
      win.dispose();
      win = null;
    }
  }
  
  private void jbInit() throws Exception {
    query.setFont(new JTextArea().getFont());

    Dimension d = new Dimension(54 * query.getFontMetrics(query.getFont()).charWidth('m'),
                                         14 * query.getFontMetrics(query.getFont()).getHeight());

    vp.setViewportView(query);
    vp.getViewport().setPreferredSize(d);
    main.setLayout(new BorderLayout());
    main.add(vp);
    main.add(okp, BorderLayout.SOUTH);
  }
}
