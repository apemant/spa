/****license*****************************************************************
**   file: raDelAll.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.raUpitDialog;

import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raDelAll extends raUpitDialog { /*JraDialog {*/

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  XYLayout xYLayout1 = new XYLayout();
  JPanel mainPanel = new JPanel();
  JLabel tekst1 = new JLabel("Obrada");

  JLabel tekst2 = new JLabel("Godina");
  JraTextField jtfGodina = new JraTextField();

  JProgressBar jProgressBar1 = new JProgressBar();
  rapancskl1 rpcskl = new rapancskl1(){
    public void MYafter_lookUp() {}
    public void MYpost_after_lookUp(){}
  };

  boolean isSelected = false;
  String godina = "";

  java.util.Vector vec = new java.util.Vector();
//  JCheckBox jc = new JCheckBox("Obrisati zajedni\u010Dke podatke",false);

  JCheckBox jc2 = new JCheckBox("Knjigovodstvo",false);

  public void okPress(){
    godina = jtfGodina.getText();
    isSelected = jc2.isSelected();
System.out.println("godina = "+godina+" isSelected"+isSelected);
    go();
  }

  public raDelAll(){
    try {
      jbInit();
    }
    catch (Exception e) {}
  }

  public raDelAll(String god) {
    try {
      jbInit();
    }
    catch (Exception e) {}
  }

  public void componentShow(){
//    jc.setSelected(false);
SwingUtilities.invokeLater(new Runnable() {
  public void run() {
    jc2.setSelected(false);
    InitState(true);
    jtfGodina.setText(val.findYear(val.getToday()));
  }
});

  }

  public void InitState(boolean how) {

    rpcskl.Clear();
    rpcskl.disabCSKL(how);
    rpcskl.jrfCSKL.requestFocus();

  }

  void jbInit() throws Exception {

    dm.getSklad().open();
    setJPan(mainPanel);
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(140);

    rpcskl.setOverCaption(true);
/*
    jc.setHorizontalTextPosition(SwingConstants.LEFT);
    jc.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jc_itemStateChanged(e);
      }
    });
*/
    jc2.setHorizontalTextPosition(SwingConstants.LEFT);
    jc2.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jc2_itemStateChanged(e);
      }
    });

    jtfGodina.setHorizontalAlignment(SwingConstants.CENTER);

    mainPanel.setLayout(xYLayout1);
    mainPanel.add(rpcskl,new XYConstraints(0, 25, -1, -1) );
    mainPanel.add(tekst2,new XYConstraints(15, 75, -1, -1));
    mainPanel.add(jtfGodina,new XYConstraints(150, 75, 100, -1));
//    mainPanel.add(jc,new XYConstraints(372, 75, 210, -1));  // opasno je zbog zajedni\u010Dkih podataka u drugom knjigovodstvu
    mainPanel.add(jc2,new XYConstraints(372, 75, 210, -1));
    mainPanel.add(tekst1, new XYConstraints(15, 100, -1, -1));
    mainPanel.add(jProgressBar1, new XYConstraints(150, 100, 390, -1));

  }

/*
  void jc_itemStateChanged(ItemEvent e) {
      InitState(!jc.isSelected());
  }
*/

  void jc2_itemStateChanged(ItemEvent e) {
    if (jc2.isSelected()) {
      rpcskl.Clear();
      rpcskl.disabCSKL(false);
    }
    else {
      rpcskl.jrfCSKL.requestFocus();
      rpcskl.disabCSKL(true);
    }
  }

  public void prepare() {

    vec.clear();
    int i = 0;
//    if (rpcskl.jrfCSKL.getText().equals("")) {
    if (jc2.isSelected()) {
      dm.getSklad().first();
      do {
        if (PripadaKnj()) {
          vec.add(i,dm.getSklad().getString("CSKL"));
          i++;
        }
      }
      while (dm.getSklad().next());
    }
    else {

      hr.restart.util.lookupData.getlookupData().raLocate(dm.getSklad(),new String[] {"CSKL"},
          new String[] {rpcskl.jrfCSKL.getText()});
      if (PripadaKnj()) {
          vec.add(i,dm.getSklad().getString("CSKL"));
      }
      else {
      javax.swing.JOptionPane.showConfirmDialog(this,"Skladište ne pripada odabranom knjigovodstvu !","Poruka",
                javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
       rpcskl.Clear();
       rpcskl.disabCSKL(false);
       rpcskl.jrfCSKL.requestFocus();
      }
    }
  }

  public boolean PripadaKnj() {
    if (dm.getSklad().getString("KNJIG").equals(
      hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG(false))) return true;
    return false;
  }

  public void poruka(boolean isOK){
    String poruka="Za zadane uvijete tablice u bazi su obrisane !";
    if (!isOK)
      poruka = "Neuspješno brisanje zbog sistemskih razloga !" ;
          javax.swing.JOptionPane.showConfirmDialog(this,
            poruka,isOK?"Poruka":"Upozorenje",
                javax.swing.JOptionPane.DEFAULT_OPTION ,
                isOK?javax.swing.JOptionPane.DEFAULT_OPTION:
                javax.swing.JOptionPane.WARNING_MESSAGE);
  }
  public boolean validacija(){
    boolean returnValue= !val.isEmpty(jtfGodina);

    if (returnValue) {
      if (jtfGodina.getText().length()!=4 ) {
        jtfGodina.setText("");
        jtfGodina.requestFocus();

        returnValue = false ;
      }
    }

    if (returnValue) {

      for ( int i = 0; i< jtfGodina.getText().length();i++) {
        returnValue = Character.isDigit(jtfGodina.getText().charAt(i));
        if (!returnValue) {
          jtfGodina.setText("");
          jtfGodina.requestFocus();
          break;
        }
      }
    }
    return returnValue;
  }

  public void go(){
    if (validacija()) {
      prepare();
      if (vec.size()!=0) tred();
      else componentShow();
    }
  }

  public void Mytred_A() {
    rpcskl.disabCSKL(false);

    new Thread(new Runnable() {
      public void run() {
        int i =0;
        boolean isOK = true;
        boolean isOKOJ = true;
        jProgressBar1.setValue(0);
        if (jc2.isSelected()) {
          jProgressBar1.setMaximum(24*vec.size());
        } else {
          jProgressBar1.setMaximum(20*vec.size());
        }
        while (i< vec.size()) {
System.out.println("isSelected "+ isSelected);
          if (isSelected) {
//              obrada_zajednicki();
            jProgressBar1.setMaximum(24*vec.size());
            isOK = obrada_promet((String) vec.get(i)) ;
System.out.println("Thread(new Runnable()");
            isOKOJ = obrada_prometOJ();
            isOK = isOKOJ && isOK;
          } else {
            jProgressBar1.setMaximum(20*vec.size());
            isOK = obrada_promet((String) vec.get(i));
          }
          i++;
        }
        poruka(isOK);
        jProgressBar1.setValue(0);
      }
  }).start();
    componentShow();
  }

  public void tred() {

    if (JOptionPane.showConfirmDialog(null,"Da li \u017Eelite obrisati stanje i promete ?",
        "Brisanje ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
      Mytred_A();
    }

  }

  public boolean obrada_prometOJ(){
System.out.println("obrada_prometOJ()");
    raPrepDelStatments rp = new raPrepDelStatments(){
      public void  execute_plus(){
        jProgressBar1.setValue(jProgressBar1.getValue()+1);
      }
    };
    return rp.del_obrada_prometOJ(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG(false),godina);
  }

  public boolean obrada_promet(String cskl) {

    raPrepDelStatments rp = new raPrepDelStatments(){

      public void  execute_plus(){
        jProgressBar1.setValue(jProgressBar1.getValue()+1);
      }

    };

    return rp.del_obrada_promet(cskl,godina);
  }

  public void obrada_zajednicki(){
  }

}