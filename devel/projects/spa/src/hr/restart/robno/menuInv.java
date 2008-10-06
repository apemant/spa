/****license*****************************************************************
**   file: menuInv.java
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

import hr.restart.util.raLoader;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class menuInv extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
//  JMenu jmInventura = new JMenu();
  JMenuItem jmPopisnaLista = new JMenuItem();
  JMenuItem jpPripInv = new JMenuItem();
  JMenuItem jmUnosInventure = new JMenuItem();
  JMenuItem jmKnjizenjeInv = new JMenuItem();
  JMenuItem jmVisak = new JMenuItem();
  JMenuItem jmManjak = new JMenuItem();
  JMenuItem jmOtpRobe = new JMenuItem();
  JMenuItem jmInventurnaLista = new JMenuItem();
  JMenuItem jmOtpisIzvjestaj = new JMenuItem();

  public menuInv(hr.restart.util.startFrame startframe) {
    SF = startframe;
    jbInit();
    this.addAncestorListener(new javax.swing.event.AncestorListener() {
      public void ancestorAdded(javax.swing.event.AncestorEvent e) {
      }
      public void ancestorMoved(javax.swing.event.AncestorEvent e) {
      }
      public void ancestorRemoved(javax.swing.event.AncestorEvent e) {
      }
    });
  }

  private void jbInit() {
    this.setText("Inventura");
    jmPopisnaLista.setText("Popisna lista");
    jmPopisnaLista.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPopisnaLista_actionPerformed(e);
      }
    });
    jpPripInv.setText("Priprema inventure");
    jpPripInv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jpPripInv_actionPerformed(e);
      }
    });
    jmUnosInventure.setText("Unos inventurnih kolièina");
    jmUnosInventure.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmUnosInventure_actionPerformed(e);
      }
    });
    jmKnjizenjeInv.setText("Knjiženje inventure");
    jmKnjizenjeInv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKnjizenjeInv_actionPerformed(e);
      }
    });
    jmVisak.setText("Vi\u0161ak");
    jmVisak.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmVisak_actionPerformed(e);
      }
    });
    jmManjak.setText("Manjak");
    jmManjak.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmManjak_actionPerformed(e);
      }
    });
    jmOtpRobe.setText("Otpis robe");
    jmOtpRobe.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmOtpRobe_actionPerformed(e);
      }
    });
    jmInventurnaLista.setText("Inventurna lista");
    jmInventurnaLista.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmInventurnaLista_actionPerformed(e);
      }
    });
    
    jmOtpisIzvjestaj.setText("Lista raspodjele otpisa");
    jmOtpisIzvjestaj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
          jmOtpisIzvjestaj_actionPerformed(e);
      }
    });

    
    
    this.add(jmPopisnaLista);
    this.add(jpPripInv);
    this.add(jmUnosInventure);
    this.add(jmInventurnaLista);
    this.add(jmKnjizenjeInv);
    this.addSeparator();
    this.add(jmVisak);
    this.add(jmManjak);
    this.add(jmOtpRobe);
    this.add(jmOtpisIzvjestaj);
  }
  void jmPopisnaLista_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raPopListaInv", res.getString("raPopisnaLista_title"));
  }
  void jpPripInv_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raPripInv", res.getString("raPripInv_title"));
  }
  void jmUnosInventure_actionPerformed(ActionEvent e) {
    hr.restart.robno.raUnosInventure raInventura = (raUnosInventure) SF.showFrame("hr.restart.robno.raUnosInventure", 0, res.getString("raUnosInventure_title"), false);
    hr.restart.robno.raPan4UnosInv raPan4 = new hr.restart.robno.raPan4UnosInv();
    raPan4.pres.showPreselect(raInventura, "Unos inventure");
  }
  void jmKnjizenjeInv_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raKnjizenjeInventure", "Knjiženje inventure");
  }
  void jmInventurnaLista_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raInventurnaLista", res.getString("raInventurnaLista_title"));
  }

  void jmVisak_actionPerformed(ActionEvent e) {
    frmPregledVisak fVishak = (frmPregledVisak)raLoader.load("hr.restart.robno.frmPregledVisak");
    presINV.getPres().showJpSelectDoc("INV", fVishak, true, "Inventurni višak");
//    jpSelectDoc.showJpSelectDoc("INV", fVishak, true, ((javax.swing.JMenuItem)e.getSource()).getText());
  }
  void jmManjak_actionPerformed(ActionEvent e) {
    frmPregledManjak fManjak = (frmPregledManjak)raLoader.load("hr.restart.robno.frmPregledManjak");
    presINM.getPres().showJpSelectDoc("INM", fManjak, true, "Inventurni manjak");

//    jpSelectDoc.showJpSelectDoc("INM", fManjak, true, ((javax.swing.JMenuItem)e.getSource()).getText());
  }
  void jmOtpRobe_actionPerformed(ActionEvent e) {
    frmOtpisRobe fOtpis = (frmOtpisRobe)raLoader.load("hr.restart.robno.frmOtpisRobe");
    presOTR.getPres().showJpSelectDoc("OTR", fOtpis, true, "Otpis robe");
//    jpSelectDoc.showJpSelectDoc("OTR", frmotpis, true, ((javax.swing.JMenuItem)e.getSource()).getText());
  }
  
  void jmOtpisIzvjestaj_actionPerformed(ActionEvent e) {
      SF.showFrame("hr.restart.robno.raOtpisIzvjestaj", "Lista raspodjele otpisa");
    }

}