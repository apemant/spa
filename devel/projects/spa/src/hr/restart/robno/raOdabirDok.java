/****license*****************************************************************
**   file: raOdabirDok.java
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

import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.util.Valid;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raOdabirDok extends JraDialog {

  static String lastv = null;
  static String lasto = null;
  String odabrano = "";
  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xyl = new XYLayout();
  JLabel tekst = new JLabel("Vrsta dokumenta");
  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void jBOK_actionPerformed(){
     jBOK_action();
    }
    public void jPrekid_actionPerformed(){
      jPrekid_action();
    }
  };
  JraComboBox jco ;
  public void jPrekid_action() {
  	Valid.getValid().getDataAndClear();
  	dispose();
  }

  public void jBOK_action(){
    dispose();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        lasto = odabrano;
		afterOKPress(odabrano);
	  }
	});
  }

  public void afterOKPress(String odabrano){}

  public raOdabirDok(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
  }

  public raOdabirDok() {
    this(null, "", false);
  }

  public void show() {

    if (jco == null) {
      System.out.println("Prvo potegni metodu DocumentiZaPrijenos(String vrsta_dok)");
    }
    else {
      try {
        jbInit();
        pack();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width -
                 getWidth()) / 2,
                 (Toolkit.getDefaultToolkit().getScreenSize().height -
                 getHeight()) / 2);
        okp.registerOKPanelKeys(this);
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
      super.show();
    }
  }
  
  String findVrdok(Object opis) {
    if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("PON"))){
      return "PON";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("PRD"))){
      return "PRD";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("NKU"))){
      return "NKU";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("OTP"))){
      return "OTP";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("RAC"))){
      return "RAC";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("RN"))){
      return "RN";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("IZD"))){
      return "IZD";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("POS"))){
      return "POS";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("ROT"))){
      return "ROT";
    }else if (opis.equals("Razduženje blagajne")){
      return "POS";
    }else if (opis.equals("Storno ROT")){
      return "SRT";
    }else if (opis.equals("Storno PRD")){
      return "SPD";
    }else if (opis.equals("Storno RAC")){
      return "SRC";
    }else if (opis.equals("Storno GOT")){
      return "SGT";
    }else if (opis.equals("Storno GRN")){
      return "SGR";
    }else if (opis.equals("Raèun")){
      return "RAC";
    }else if (opis.equals("Gotovinski raèun")){
      return "GRN";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("REV"))){
      return "REV";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("DOS"))){
      return "DOS";
    }else if (opis.equals(TypeDoc.getTypeDoc().nazivDokumenta("POD"))){
      return "POD";
    }
    return null;
  }

  void jbInit() throws Exception {

    jco.addItemListener(new java.awt.event.ItemListener(){
      public void itemStateChanged (java.awt.event.ItemEvent i){
        odabrano = findVrdok(i.getItem());
      }
    });
    panel2.setLayout(xyl);
    xyl.setHeight(50);
    xyl.setWidth(315);
    panel2.setBorder(new javax.swing.border.EtchedBorder());
    panel2.add(jco, new XYConstraints(150, 15, 150, -1));
    panel2.add(tekst, new XYConstraints(15, 15, -1, -1));
    panel1.setLayout(borderLayout1);
    panel1.add(okp,BorderLayout.SOUTH);
    panel1.add(panel2,BorderLayout.CENTER);
    getContentPane().add(panel1);

  }

  public void DocumentiZaPrijenos(String vrsta_dok) {

    String[] zaodabrati;
     if (vrsta_dok.equals("PON")) {
       zaodabrati = new String[] {"Narudžba", "Ponuda"};
       odabrano = "NKU";
    }
    else if (vrsta_dok.equals("OTP")) {
       zaodabrati = new String[] {"Ra\u010Dun", "Ponuda", "Narudžba kupca","Pos", "Raèun - otpremnica"};
       odabrano = "RAC";
    }
    else if (vrsta_dok.equals("IZD")) {
        zaodabrati = new String[] {"Razduženje blagajne","Raèun", "Gotovinski raèun"};
        odabrano = "POS";
     }
    else if (vrsta_dok.equals("PRD")) {
       zaodabrati = new String[] {"Narudžba","Ponuda", "Storno PRD"};
       odabrano = "NKU";
    }
    else if (vrsta_dok.equals("RAC")) {
    	odabrano=hr.restart.sisfun.frmParam.getParam("robno","selDokOnRAC", "OTP", "Dokument za dohvat na RACu");
    	if (odabrano.equalsIgnoreCase("RN")) {
      	  zaodabrati = new String[] {"Radni nalog","Otpremnica","Radni nalog po izdatnici","Ponuda","Raèun","Revers","Dostavnica", "Storno RAC"};
    	}
    	else {
    	  zaodabrati = new String[] {"Otpremnica","Radni nalog","Radni nalog po izdatnici","Ponuda","Raèun","Revers","Dostavnica", "Storno RAC"};
    	}
    }
    else if (vrsta_dok.equals("ROT")) {
      zaodabrati = new String[] {"Narudžba","Ponuda","Raèun za predujam","Dostavnica","Raèun - otpremnica", "Storno ROT"};
      odabrano = "NKU";
    }
    else if (vrsta_dok.equals("POD")) {
      zaodabrati = new String[] {"Dostavnica"};
      odabrano = "DOS";
    }
    else if (vrsta_dok.equals("GRN")) {
    	odabrano=hr.restart.sisfun.frmParam.getParam("robno","selDokOnRAC", "OTP", "Dokument za dohvat na RACu");
    	if (odabrano.equalsIgnoreCase("RN")) {
    		zaodabrati = new String[] {"Radni nalog","Otpremnica", "Ponuda","Radni nalog po izdatnici", "Raèun za predujam", "Storno GRN"};
    	}
    	else {
    		zaodabrati = new String[] {"Ponuda","Otpremnica", "Radni nalog","Radni nalog po izdatnici", "Raèun za predujam", "Storno GRN"};
    		odabrano = "PON";
    	}
    }
    else if (vrsta_dok.equals("GOT")) {
      zaodabrati = new String[] {"Ponuda", "Raèun za predujam", "Storno GOT"};
      odabrano = "PON";
    }

    else if (vrsta_dok.equals("KAL")) {
      zaodabrati = new String[] {"Primke"};
      odabrano = "PRI";
    } else if (vrsta_dok.equals("DOS")) {
        zaodabrati = new String[] {"Narudžba"};
        odabrano = "NKU";
    } else {
       zaodabrati=new String[] {"ne postoji odabir"};
    }
     if (vrsta_dok.equals(lastv) && lasto != null)
       odabrano = lasto;
     lasto = odabrano;
     lastv = vrsta_dok;
    jco = new JraComboBox(zaodabrati);
    for (int i = 0; i < zaodabrati.length; i++)
      if (odabrano.equals(findVrdok(zaodabrati[i])))
        jco.setSelectedIndex(i);
  }
}