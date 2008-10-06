/****license*****************************************************************
**   file: MenuUpdaterUI.java
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
 * Created on Dec 9, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.menus;

import hr.restart.swing.JraButton;
import hr.restart.util.OKpanel;
import hr.restart.util.okFrame;
import hr.restart.util.raProcess;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MenuUpdaterUI implements okFrame {
  //check, exec, del-exec, update
  JPanel content = new JPanel();
  JraButton jbCheck = new JraButton();
  JraButton jbUpdate = new JraButton();
  JraButton jbDelExec = new JraButton();
  JraButton jbDump = new JraButton();
  JLabel results = new JLabel();
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
  };
  public MenuUpdaterUI() {
    initContent();
  }

  /**
   * 
   */
  private void initContent() {
    jbCheck.setText("Provjeri");
    jbDelExec.setText("Obriši i dodaj");
    jbUpdate.setText("Ažuriraj");
    jbDump.setText("Napravi skriptu");
    jbCheck.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        action(1);
      }
    });
    jbDelExec.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (pass()) action(10);
      }
    });
    jbUpdate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (pass()) action(2);
      }
    });
    jbDump.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        action(107);
      }
    });
    content.setLayout(new BorderLayout());
    JPanel toolbar = new JPanel(new GridLayout(4,1));
    toolbar.add(jbCheck);
    toolbar.add(jbUpdate);
    toolbar.add(jbDelExec);
    toolbar.add(jbDump);
    content.add(results,BorderLayout.CENTER);
    content.add(toolbar,BorderLayout.EAST);
  }

  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#action_jPrekid()
   */
  public void action_jPrekid() {
  }

  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#action_jBOK()
   */
  public void action_jBOK() {
  }

  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#getContentPane()
   */
  public Container getContentPane() {
    return content;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#getTitle()
   */
  public String getTitle() {
    return "Kontrola menija";
  }

  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#getOKpanel()
   */
  public OKpanel getOKpanel() {
    return okp;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#doSaving()
   */
  public boolean doSaving() {
    return false;
  }

  private void action(final int act) {
    raProcess.runChild(content.getTopLevelAncestor(), new Runnable() {
      public void run() {
        int _act;
        String info = "";
        if (act == 10) {
          info = info + "\n"+MenuUpdater.deleteAllMenus();
          _act = 0;
        } else if (act == 107) {
          info = info + "\n"+MenuUpdater.dumpToFileSQL();
          _act = act;
        } else {
          _act = act;
        }
        if (act < 100) info = info + "\n" + MenuUpdater.processFileSQL(_act);
        results.setText("<html><pre>"+info+"</pre></html>");
      }
    });
  }
  private boolean pass() {
    String ptxt = JOptionPane.showInputDialog(content.getTopLevelAncestor(),"Unesite root lozinku");
    if (ptxt!=null && ptxt.equals(hr.restart.sisfun.frmPassword.getRootPassword())) {
      return true;
    } else {
      JOptionPane.showMessageDialog(content.getTopLevelAncestor(),"Neispravna lozinka");
      return false;
    } 
  }
}
