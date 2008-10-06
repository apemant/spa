/****license*****************************************************************
**   file: raToolMenuPanel.java
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
 * raToolMenuPanel.java
 *
 * Created on 2003. rujan 11, 17:45
 */

package hr.restart.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
/**
 *
 * @author  andrej
 */
public class raToolMenuPanel implements okFrame {
  
  private JButton jbActivation;
  private JPanel jp = new JPanel(new FlowLayout(FlowLayout.CENTER));
  String txtActivate = "Aktiviraj sistemski izbornik";
  String txtDeActivate = "Deaktiviraj sistemski izbornik";
  JFrame owner;
  private JMenuBar jMBar;
  private JPasswordField jpassw = new JPasswordField();
  
  /** Creates a new instance of raToolMenuPanel */
  public raToolMenuPanel(JFrame _owner) {
    owner = _owner;
    initPanel();
  }
  private void initPanel() {
    jbActivation = new JButton(txtActivate, raImages.getImageIcon(raImages.IMGALLFORWARD));
    
    jbActivation.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        activationPerformed();
      }
    });
    
    
    jp.add(new JLabel("Root lozinka"));
    jpassw.setPreferredSize(new Dimension(100,21));
    jp.add(jpassw);
    jp.add(jbActivation);
    clearPassword();
  }
  private boolean checkToolAccess() {
    String ptxt = new String(jpassw.getPassword());
    if (ptxt.equals(hr.restart.sisfun.frmPassword.getRootPassword())) {
      return true;
    } else {
      JOptionPane.showMessageDialog(owner,"Neispravna lozinka");
      return false;
    }
  }
  private void activationPerformed() {
    if (jbActivation.getText().equals(txtActivate)) {
      //dodaj meni
      if (checkToolAccess()) {
        if (jMBar == null) createMenuBar();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            System.out.println("owner.setJMenuBar(jMBar)");
            owner.setJMenuBar(jMBar);
            
            jbActivation.setText(txtDeActivate);
            jbActivation.setIcon(raImages.getImageIcon(raImages.IMGALLBACK));
            jMBar.invalidate();
          }
        });
      }
    } else {
      //makni meni
      owner.setJMenuBar(null);
      jbActivation.setText(txtActivate);
      jbActivation.setIcon(raImages.getImageIcon(raImages.IMGALLFORWARD));
    }
    clearPassword();
    owner.getLayeredPane().revalidate();
    owner.repaint();
  }
  private void clearPassword() {
    String usro = hr.restart.sisfun.raUser.getInstance().getUser();
    if (usro.equals("root") || usro.equals("test") || usro.equals("restart")) {
      jpassw.setText(hr.restart.sisfun.frmPassword.getRootPassword());
    } else {
      jpassw.setText("");
    }
  }
  private void createMenuBar() {
    jMBar = new JMenuBar();
    java.util.LinkedList ll = raLLFrames.getRaLLFrames().getStartFrames();
    JMenu jmAppMenus = new JMenu("Alati");
    startFrame oneGoodStartFrame = startFrame.getStartFrame();
    for (int i=0;i<ll.size();i++) {
      startFrame SF = (startFrame)ll.get(i);
      if (SF.getToolMenu() != null) {
        String bundleName = hr.restart.mainFrame.findAPPBundleSec(SF.getClass().getName());
        String title;
        try {
          title = java.util.ResourceBundle.getBundle(hr.restart.start.RESBUNDLENAME).getString("jB"+bundleName+"_text");          
          System.out.println(" * "+title);
          JMenu mnu = new JMenu(title);
          mnu.add(SF.getToolMenu());
          jmAppMenus.add(mnu);  
          if (!(SF instanceof hr.restart.sisfun.frmSistem)) oneGoodStartFrame = SF;
        } catch (Exception ex) {
          //smece
        }
      }
    }
    jmAppMenus.addSeparator();
    jmAppMenus.add(oneGoodStartFrame.jmiKreator);
    jMBar.add(jmAppMenus);
    JMenu jmSisfun = new JMenu("Sistemske funkcije");
    oneGoodStartFrame.addSystemJMenuBar(jmSisfun);
    jMBar.add(jmSisfun);
  }
  
  public void action_jBOK() {
  }
  
  public void action_jPrekid() {
  }
  
  public boolean doSaving() {
    return false;
  }
  
  public java.awt.Container getContentPane() {
    return jp;
  }
  
  public OKpanel getOKpanel() {
    return null;
  }
  
  public String getTitle() {
    return "Sistemski izbornik";
  }
  
}
