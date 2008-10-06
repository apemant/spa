/****license*****************************************************************
**   file: frmBlagajnaPassword.java
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
 * Created on 2005.07.14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.pos;

import hr.restart.swing.JraDialog;
import hr.restart.util.OKpanel;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.borland.dx.dataset.DataRow;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class frmBlagajnaPassword extends JraDialog {
	  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    JPanel jpcp = new JPanel();
    XYLayout xYLay = new XYLayout();
    JLabel jlNewPass = new JLabel();
    JLabel jlNewPass2 = new JLabel();
    JPasswordField jtNewPass = new JPasswordField();
    JPasswordField jtNewPass2 = new JPasswordField();
    boolean success = false;
    private String strNewPass = null;

    
    boolean firstTime = true;
    
    DataRow preselData;
    
    OKpanel cpokp = new OKpanel() {
      public void jPrekid_actionPerformed() {
        cp_prekid();
      }
      public void jBOK_actionPerformed() {
        cp_ok();
      }
    };

    public frmBlagajnaPassword() {
        try {
            jInit();
          }
          catch(Exception e) {
            e.printStackTrace();
          }
    }
    public frmBlagajnaPassword(JDialog own) {
      super(own,"Promjena zaporke",true);
      try {
        jInit();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
      private void jInit() throws Exception {
        jlNewPass.setText("Nova zaporka");
        jpcp.setLayout(xYLay);
        jlNewPass2.setText("Još jednom nova zaporka");
        xYLay.setWidth(410);
        xYLay.setHeight(85);
        jpcp.add(jlNewPass, new XYConstraints(15, 20, -1, -1));
        jpcp.add(jlNewPass2, new XYConstraints(15, 45, -1, -1));
        jpcp.add(jtNewPass, new XYConstraints(200, 20, 200, -1));
        jpcp.add(jtNewPass2, new XYConstraints(200, 45, 200, -1));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jpcp,BorderLayout.CENTER);
        getContentPane().add(cpokp,BorderLayout.SOUTH);
        this.pack();
        addComponentListener(new ComponentAdapter() {
          public void componentShown(ComponentEvent e) {
            jtNewPass.requestFocus();
          }
        });
      }
      void cp_ok() {
        String s1 = new String(jtNewPass.getPassword());
        String s2 = new String(jtNewPass2.getPassword());
        if (!s1.equals(s2)) {
          JOptionPane.showMessageDialog(this,"Unešene zaporke se razlikuju! Zaporka nije promijenjena!","Poruka",JOptionPane.WARNING_MESSAGE);
          jtNewPass.setText("");
          jtNewPass2.setText("");
          jtNewPass.requestFocus();
          return;
        }
        strNewPass = new String(jtNewPass.getPassword());
        dm.getBlagajnici().setString("LOZINKA",s1);
        success = true;
        hide();
      }

      void cp_prekid() {
        success = false;
        hide();
      }

      public void show() {
        this.setTitle("Promjena zaporke");
      	jtNewPass.setText("");
        jtNewPass2.setText("");
        success = false;
        super.show();
      }

 }
