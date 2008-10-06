/****license*****************************************************************
**   file: raMiniEditor.java
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

import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextPane;


public class raMiniEditor extends JPanel {

  JTextPane jEditorPane1 = new JTextPane();
  raNavBar jtb = new raNavBar(raNavBar.EMPTY);
  BorderLayout borderLayout1 = new BorderLayout();

  raNavAction raNavBold = new raNavAction("Bold",hr.restart.util.raImages.IMGBOLD,-1){
    public void actionPerformed(ActionEvent e) {
      hekBA.actionPerformed(e);
   }
  };
  raNavAction raNavItalic = new raNavAction("Italic",hr.restart.util.raImages.IMGITALIC,-1){
    public void actionPerformed(ActionEvent e) {
      hekIA.actionPerformed(e);
   }
  };

  raNavAction raNavUnderline = new raNavAction("Underline",hr.restart.util.raImages.IMGUNDERLINE,-1){
    public void actionPerformed(ActionEvent e) {
      hekUIA.actionPerformed(e);
   }
  };

  raNavAction raNavIspis = new raNavAction("SRANJE"){
    public void actionPerformed(ActionEvent e) {
      System.out.println(jEditorPane1.getText());
   }
  };

  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  JComboBox jComboBoxFonts = new JComboBox();
  JComboBox jComboBoxFontsSize = new JComboBox(new Integer [] {new Integer(2),
        new Integer(4),new Integer(8),new Integer(16),new Integer(32),new Integer(64)});

  javax.swing.text.html.HTMLEditorKit.BoldAction hekBA =
      new javax.swing.text.html.HTMLEditorKit.BoldAction();
  javax.swing.text.html.HTMLEditorKit.ItalicAction hekIA =
      new javax.swing.text.html.HTMLEditorKit.ItalicAction();
  javax.swing.text.html.HTMLEditorKit.UnderlineAction hekUIA =
      new javax.swing.text.html.HTMLEditorKit.UnderlineAction();
/*
  javax.swing.text.html.HTMLEditorKit.FontFamilyAction hekFFA =
      new javax.swing.text.html.HTMLEditorKit.FontFamilyAction(nesto,nesto);
*/


  public raMiniEditor() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

    jEditorPane1.setContentType("text/html");
    String [] font_names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (int i = 0;i< font_names.length;i++) {
      jComboBoxFonts.addItem(font_names[i]);
    }
    jComboBoxFonts.setSelectedIndex(0);
    jComboBoxFontsSize.setSelectedIndex(0);
    jComboBoxFontsSize.setPreferredSize(new Dimension (40,21));

    setLayout(borderLayout1);
    jtb.addOption(raNavBold);
    jtb.addOption(raNavItalic);
    jtb.addOption(raNavUnderline);
    jtb.addOption(raNavIspis);

    add(jEditorPane1,  BorderLayout.CENTER);
    jPanel4.add(jComboBoxFonts);
    jPanel4.add(jComboBoxFontsSize);
    jPanel3.setLayout(new BorderLayout());
    jPanel3.add(jtb,BorderLayout.WEST);
    jPanel3.add(jPanel4,BorderLayout.CENTER);
    add(jPanel3,BorderLayout.NORTH);
  }
}