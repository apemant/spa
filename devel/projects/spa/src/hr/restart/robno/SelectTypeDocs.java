/****license*****************************************************************
**   file: SelectTypeDocs.java
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

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYLayout;

public class SelectTypeDocs extends JraDialog {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
	 public void jBOK_actionPerformed(){
//		 jBOK_action();
	 }
	 public void jPrekid_actionPerformed(){
//		jPrekid_action();
	 }
  };

  public SelectTypeDocs(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public SelectTypeDocs() {
    this(null, "", false);
  }

  public void initVrdok(String[] name, String[] vrdok){
    String[] zaodabrati;
/*
    if (this.vrsta_dok.equals("PON")) {
       zaodabrati = new String[] {"Narudžba"};
       tamodalje("NKU");
    }
    else if (this.vrsta_dok.equals("PRD")) {
       zaodabrati = new String[] {"Narudžba","Ponuda"};
       tamodalje("NKU");
    }
    else if (this.vrsta_dok.equals("RAC")) {
       zaodabrati = new String[] {"Otpremnica"};
       tamodalje("OTP");
    }
    else if (this.vrsta_dok.equals("ROT")) {
       zaodabrati = new String[] {"Narudžba","Ponuda","Predra\u010Dun"};
       tamodalje("NKU");
    }
    else {
       zaodabrati=new String[] {"mal san puka","adasdas","asdasd"};
    }
    */
  }



  XYLayout xyl =new XYLayout();
  JLabel tekst = new JLabel("Vrsta dokumenta");
  String vrsta_dok = "";

  void jbInit() throws Exception {
    addKeyListener(new java.awt.event.KeyAdapter(){
        public void keyPressed(java.awt.event.KeyEvent e){
          if (e.getKeyCode()==java.awt.event.KeyEvent.VK_F10){
//            odabir.hide();
//            go_next();
          }
          else if (e.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE){
//            odabir.hide();0
          }
        }
    });


/*
    JComboBox jco = new JComboBox(zaodabrati);
    jco.addItemListener(new java.awt.event.ItemListener(){
      public void itemStateChanged (java.awt.event.ItemEvent i){
        if (i.getItem().equals("Ponuda")){
          tamodalje("PON");
        }else if (i.getItem().equals("Predra\u010Dun")){
          tamodalje("PRD");
        }else if (i.getItem().equals("Narudžba")){
          tamodalje("NKU");
        }else if (i.getItem().equals("Otpremnica")){
          tamodalje("OTP");
        }else if (i.getItem().equals("Otpremnica")){
          tamodalje("RN");
        }
      }
    });

    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation((screenSize.width  - getWidth()) / 2,
                (screenSize.height - getHeight()) / 2);
    setTitle("Odabir vrste dokumenta za podlogu");
    getContentPane().setLayout(new BorderLayout());
    panel1.add(jco, new XYConstraints(150, 15, 150, -1));
    panel1.add(tekst, new XYConstraints(15, 15, -1, -1));
    getContentPane().add(okp,BorderLayout.SOUTH);
    getContentPane().add(panel1);

    setModal(true);
    panel1.setLayout(borderLayout1); // mjenjaj
    xyl.setHeight(50);
    xyl.setWidth(315);
    panel1.setBorder(new javax.swing.border.EtchedBorder());
    odabir.getContentPane().add(jpo,BorderLayout.CENTER);
    pack();
    */
  }
}