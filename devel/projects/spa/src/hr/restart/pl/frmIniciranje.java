/****license*****************************************************************
**   file: frmIniciranje.java
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
package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.baza.Orgpl;
import hr.restart.baza.Orgstruktura;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.zapod.OrgStr;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;

public class frmIniciranje extends frmObradaPL {
  raIniciranje rin = raIniciranje.getInstance();
  //JraCheckBox jcbIniPrim = new JraCheckBox("Dodati inicijalna primanja");
  String[] INIPRIMOPTS = new String[] {
    "Dodati inicijalna primanja",
    "Ne dodati inicijalna primanja",
    "Inicijalna primanja 1-5",
  };
  JraComboBox jcbIniPrim = new JraComboBox(INIPRIMOPTS);
  JraButton jbNivoi = new JraButton();
  boolean succ;
  boolean[] nivoi = new boolean[] {true,true,true,true,true}; //svi,oj,vro,radnici, ini 5
  public frmIniciranje() {
    addCheckBox();
    addOrgFor();
  }

  private void addOrgFor() {
    String oj4 = getOJFor();
    if ("".equals(oj4)) return;
    StorageDataSet orgs = Orgstruktura.getDataModule().getTempSet(Condition.equal("CORG", oj4));
    orgs.open();
    JLabel jl = new JLabel("Obrada za O.J. "+getOJFor()+" "+orgs.getString("NAZIV"));
    jl.setForeground(Color.RED);
    jl.setFont(jl.getFont().deriveFont(Font.BOLD));
    jp.add(jl, new XYConstraints(15, 115, -1, -1));
//    xYLayout1.setHeight(155);//130
  }
  static StorageDataSet orgpl4;
  public static String getOJFor() {
    String oj4 = frmParam.getParam("pl", "plOOJFor"+OrgStr.getKNJCORG(), "", "Za koju org jedinicu se radi obracun place na knjigovodstvu "+OrgStr.getKNJCORG()+" (prazno za norm.op.)").trim();
    if ("".equals(oj4)) return "";
    if (orgpl4 == null || !oj4.equals(orgpl4.getString("CORG")) ) {
      orgpl4 = Orgpl.getDataModule().getFilteredDataSet(Condition.equal("CORG", oj4));
      orgpl4.open();
      if (orgpl4.getRowCount() == 0) {
        System.err.println("Neispravan parametar plOOJFor - ne postoji oj "+oj4+" u orgpl!");
        return "";
      }
    }
    return oj4;
    
  }
  private void addCheckBox() {
/*    jcbIniPrim.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbIniPrim.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbIniPrim.setSelected(true);*/
    jcbIniPrim.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        for (int i=0; i<nivoi.length; i++) nivoi[i] = jcbIniPrim.getSelectedIndex() == 0;
        if (jcbIniPrim.getSelectedIndex() == 2) nivoi[0] = true;
        jbNivoi.setEnabled(jcbIniPrim.getSelectedIndex() != 1);
      }
    });
    jbNivoi.setText("...");
    jbNivoi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        pickNivoi();
      }
    });
    jp.add(jcbIniPrim, new XYConstraints(380, 90, 180, -1));
    jp.add(jbNivoi, new XYConstraints(565, 90, 21, 21));
  }

  public boolean Validacija() {
    if (!super.Validacija()) return false;
    if (rin.isInitObr(this)) {
      if (rin.obrCount > 0) {
        JOptionPane.showConfirmDialog(jp,"Neke org.jedinice su obra\u0111ene, potrebno je poništiti obra\u010Dun !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      } else {
        if (JOptionPane.showConfirmDialog(jp,"Pla\u0107a je ve\u0107 inicirane. Želite li poništiti inicijalizaciju ?","Upozorenje",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
          if (rin.ponistavanje(tds.getString("CORG"))) {
            dm.getOrgpl().refresh();
            JOptionPane.showConfirmDialog(jp,"Inicijalizacija je uspješno poništena !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
      return false;
    }
    if (rin.obrCount > 0) {
      JOptionPane.showConfirmDialog(jp,"Neke org.jedinice su obra\u0111ene, potrebno je poništiti obra\u010Dun !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    } else return JOptionPane.showConfirmDialog(jp,getIniPitanje(),"Iniciranje",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == 0;
  }

  public void okPress() {
    succ = rin.iniciranje(tds, getPickedNivoi());
    //jcbIniPrim.isSelected());
  }
  public void showMessage() {
    if (!succ) JOptionPane.showConfirmDialog(jp,"Greška kod iniciranja obra\u010Duna !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    else JOptionPane.showConfirmDialog(jp,"Obra\u010Dun je uspješno iniciran !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
  }
  public void setEnableObr(boolean enable) {
    rcc.setLabelLaF(jcbIniPrim,enable);
    super.setEnableObr(enable);
  }

  private void pickNivoi() {
    JPanel jpPickNivoi = new JPanel(new GridLayout(5,1));
    boolean b = jcbIniPrim.getSelectedIndex() == 0;
    JraCheckBox jcbCORG = new JraCheckBox(b?"Org. jedinica":"Primanja 1");
    JraCheckBox jcbCRADNIK = new JraCheckBox(b?"Radnik":"Primanja 2");
    JraCheckBox jcbCVRO = new JraCheckBox(b?"Vrsta radnog odnosa":"Primanja 3");
    JraCheckBox jcbSVI = new JraCheckBox(b?"Svima":"Primanja 4");
    JraCheckBox jcbIni5 = new JraCheckBox("Primanja 5");
    jpPickNivoi.add(jcbCORG);
    jpPickNivoi.add(jcbCRADNIK);
    jpPickNivoi.add(jcbCVRO);
    jpPickNivoi.add(jcbSVI);
    if (!b) jpPickNivoi.add(jcbIni5);
    jcbCORG.setSelected(nivoi[0]);
    jcbCRADNIK.setSelected(nivoi[1]);
    jcbCVRO.setSelected(nivoi[2]);
    jcbSVI.setSelected(nivoi[3]);
    jcbIni5.setSelected(nivoi[4]);
    JOptionPane.showMessageDialog(jbNivoi, jpPickNivoi, "Odaberite nivoe za dodavanje", JOptionPane.PLAIN_MESSAGE);
    nivoi[0] = jcbCORG.isSelected();
    nivoi[1] = jcbCRADNIK.isSelected();
    nivoi[2] = jcbCVRO.isSelected();
    nivoi[3] = jcbSVI.isSelected();
    nivoi[4] = jcbIni5.isSelected();
  }
  
  private String[] getPickedNivoi() {
    if (jcbIniPrim.getSelectedIndex() == 0) {
      return new String[] {
        nivoi[0]?"CORG":"-", 
        nivoi[1]?"CRADNIK":"-",
        nivoi[2]?"CVRO":"-",
        nivoi[3]?"SVI":"-",
        "-"
      };
    } else if (jcbIniPrim.getSelectedIndex() == 2) {
      return new String[] {
        nivoi[0]?"INI1":"-",
        nivoi[1]?"INI2":"-",
        nivoi[2]?"INI3":"-",
        nivoi[3]?"INI4":"-",
        nivoi[4]?"INI5":"-"
      };
    }
    return null;
  }
  private String getIniPitanje() {
    String common = "Inicirati obra\u010Dun?";
    if (jcbIniPrim.getSelectedIndex() == 0) {
      return "Dodajem inicijalna primanja na nivou: "+
        (nivoi[0]?"org. jedinica, ":"")+
        (nivoi[1]?"radnika, ":"")+
        (nivoi[2]?"vrste radnog odnosa, ":"")+
        (nivoi[3]?"svi, ":"")+"\n"+
        common;
    } else if (jcbIniPrim.getSelectedIndex() == 2) {
      return "Dodajem inicijalna primanja na nivou: "+
      (nivoi[0]?"primanja 1, ":"")+
      (nivoi[1]?"primanja 2, ":"")+
      (nivoi[2]?"primanja 3, ":"")+
      (nivoi[3]?"primanja 4, ":"")+
      (nivoi[4]?"primanja 5, ":"")+"\n"+
      common;
    }
    return common;
  }
}