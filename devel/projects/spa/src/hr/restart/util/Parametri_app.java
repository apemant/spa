/****license*****************************************************************
**   file: Parametri_app.java
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

import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class Parametri_app extends JraDialog implements okFrame {
  JPanel jPanelparamap = new JPanel();
  JPanel jMainPane = new JPanel();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JTextField jTextuser = new JTextField();
  private String oldUrl = "";
  private String oldUser = "";
  private String oldPass = "";
  private String oldTip = "";
/*
  jTextuser
  jComboBoxUrl
  jComboBoxTip
  jPasswordPass

  JButton jButtonOK = new JButton();
  JButton jButtonPrekid = new JButton();
*/
  OKpanel okp = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
  };
  JraComboBox jComboBoxUrl = new JraComboBox();
  JraComboBox jComboBoxTip = new JraComboBox();
  JPasswordField jPasswordPass = new JPasswordField();
//  GlavniEkran gle ;
  hr.restart.util.IntParam param = new hr.restart.util.IntParam();
  XYLayout xYLayout1 = new XYLayout();
  FlowLayout flowLayout1 = new FlowLayout();


  public Parametri_app(Frame frame) {
    super(frame, "Baza", true);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
//    this.getContentPane().setLayout(flowLayout1);
    jPanelparamap.setLayout(xYLayout1);
    jLabel1.setText("Url :");
    jLabel2.setText("User :");
    jLabel3.setText("Password :");
    jLabel4.setText("Tip :");
    jComboBoxUrl.insertItemAt(param.URL,0);

    int i=1;
    String Sadrzaj="-1";

  while (param.VratiSadrzajTaga("url"+i) != "") {

      Sadrzaj = param.VratiSadrzajTaga("url"+i);
      if (!Sadrzaj.equals(param.URL))
        jComboBoxUrl.addItem(Sadrzaj);
      i++;
    }
    jComboBoxUrl.setSelectedIndex(0);

    jComboBoxTip.insertItemAt(param.TIP,0);
    /// Tip Drivera
    i=1 ;
    Sadrzaj="-1";

   do {
      Sadrzaj = param.VratiSadrzajTaga("tip"+i);
      if (!Sadrzaj.equals(param.TIP)){
        jComboBoxTip.addItem(Sadrzaj);
        }
      i++;
    }while (param.VratiSadrzajTaga("tip"+i) != "");

    jComboBoxTip.setSelectedIndex(0);

    jTextuser.setMaximumSize(new Dimension(4, 21));
    jTextuser.setText(param.USER);
    jPasswordPass.setNextFocusableComponent(jComboBoxTip);
    jPasswordPass.setText(param.PASSWORD);

    this.setResizable(false);
    jPanelparamap.setMaximumSize(new Dimension(400, 162));
    jComboBoxUrl.setEditable(true);
    jComboBoxTip.setEditable(true);

    jPasswordPass.setMaximumSize(new Dimension(4, 21));
    xYLayout1.setWidth(520);
    xYLayout1.setHeight(140);
    jPanelparamap.add(jLabel1,   new XYConstraints(15, 20, -1, -1));
    jPanelparamap.add(jComboBoxUrl,   new XYConstraints(90, 20, 410, -1));
    jPanelparamap.add(jLabel2,  new XYConstraints(15, 45, -1, -1));
    jPanelparamap.add(jLabel3,   new XYConstraints(15, 70, -1, -1));
    jPanelparamap.add(jLabel4,    new XYConstraints(15, 95, -1, -1));
    jPanelparamap.add(jTextuser,   new XYConstraints(90, 45, 410, -1));
    jPanelparamap.add(jPasswordPass,    new XYConstraints(90, 70, 410, -1));
    jPanelparamap.add(jComboBoxTip,    new XYConstraints(90, 95, 410, -1));
    jPanelparamap.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
//    jMainPane.setLayout(new GridBagLayout());
//    jMainPane.add(jPanelparamap);
    raMatPodaci.addCentered(jPanelparamap,jMainPane);
    this.getContentPane().add(jMainPane, BorderLayout.CENTER);
    this.getContentPane().add(okp,BorderLayout.SOUTH);

    oldPass = jPasswordPass.getText();
    oldTip = jComboBoxTip.getSelectedItem().toString();
    oldUrl = jComboBoxUrl.getSelectedItem().toString();
    oldUser = jTextuser.getText();
    jTextuser.addKeyListener(new hr.restart.swing.JraKeyListener());
    jComboBoxUrl.addKeyListener(new hr.restart.swing.JraKeyListener());
    jComboBoxTip.addKeyListener(new hr.restart.swing.JraKeyListener());
    jPasswordPass.addKeyListener(new hr.restart.swing.JraKeyListener());
    okp.registerOKPanelKeys(this);
  }
  public boolean doSaving() {
    return !(
              oldPass.equals(jPasswordPass.getText()) &&
              oldTip.equals(jComboBoxTip.getSelectedItem().toString()) &&
              oldUrl.equals(jComboBoxUrl.getSelectedItem().toString()) &&
              oldUser.equals(jTextuser.getText())
            );
  }
  public void action_jBOK() {

    hr.restart.baza.BazaOper baza1 = new hr.restart.baza.BazaOper();


    if (!baza1.TestCon(jTextuser.getText(),jPasswordPass.getText().toString(),
        jComboBoxUrl.getSelectedItem().toString(),jComboBoxTip.getSelectedItem().toString()))
        JOptionPane.showMessageDialog(null,"Ne mogu se zaka\u010Diti na bazu !","Greška!",JOptionPane.ERROR_MESSAGE);

    else {
      param.UpisiUIni(jComboBoxUrl.getSelectedItem().toString(),"url");
      param.UpisiUIni(jPasswordPass.getText().toString(),"pass");
      param.UpisiUIni(jComboBoxTip.getSelectedItem().toString(),"tip");
      param.UpisiUIni(jTextuser.getText(),"user");
      this.hide();
    }
  }

  public void action_jPrekid() {
    hide();
  }
  public OKpanel getOKpanel() {
    return okp;
  }
/*
  void jButtonPrekid_actionPerformed(ActionEvent e) {
  }
  void jButtonOK_actionPerformed(ActionEvent e) {
  }

*/
}