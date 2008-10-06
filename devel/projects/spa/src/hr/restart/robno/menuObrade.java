/****license*****************************************************************
**   file: menuObrade.java
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

import hr.restart.sisfun.frmParam;
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

public class menuObrade extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmNovaGodina = new JMenuItem();
  JMenuItem jmDvijeGodine = new JMenuItem();
  JMenuItem jmPST = new JMenuItem();
  JMenuItem jmIspisDoc = new JMenuItem();
  JMenuItem jmRmRezKol = new JMenuItem();
  JMenuItem jmTemeljnica = new JMenuItem();
  JMenuItem jmTemeljnica2 = new JMenuItem();
  JMenuItem jmRemoteStart = new JMenuItem();
  JMenuItem jmImportLot = new JMenuItem();
  JMenuItem jmCreateKPR = new JMenuItem();
  JMenuItem jmObrIzd = new JMenuItem();
  
  public menuObrade(hr.restart.util.startFrame startframe) {
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
    this.setText("Obrade");
    jmDvijeGodine.setText("Rad u dvije godine");
    jmDvijeGodine.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmDvijeGodine_actionPerformed(e);
      }
    });
    jmNovaGodina.setText("Prijelaz u novu godinu");
    jmNovaGodina.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNovaGodina_actionPerformed(e);
      }
    });
    jmPST.setText("Poèetno stanje");
    jmPST.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPST_actionPerformed(e);
      }
    });
    jmIspisDoc.setText("Ispis dokumenata");
    jmIspisDoc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspisDoc_actionPerformed(e);
      }
    });
    jmRmRezKol.setText("Poništavanje nevažeæih ponuda");
    jmRmRezKol.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRmRezKol_actionPerformed(e);
      }
    });
    jmTemeljnica.setText("Ispis temeljnice");
    jmTemeljnica.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmTemeljnica_actionPerformed(e);
      }
    });
    jmTemeljnica2.setText("Ispis temeljnice za period");
    jmTemeljnica2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          jmTemeljnica2_actionPerformed(e);
        }
      });
    
    jmRemoteStart.setText("Prijenos podataka");
    jmRemoteStart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRemoteStart_actionPerformed(e);
      }
    });
    jmImportLot.setText("Prijenos s vanjskog ureðaja");
    jmImportLot.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmImportLot_actionPerformed(e);
      }
    });
    jmCreateKPR.setText("Formiranje KPR");
    jmCreateKPR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmCreateKPR_actionPerformed(e);
      }
    });
    jmObrIzd.setText("Obraèun troškova materijala");
    jmObrIzd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmObrIzd_actionPerformed(e);
      }
    });
    this.add(jmDvijeGodine);
    this.add(jmNovaGodina);
    this.add(jmPST);
    this.addSeparator();
    this.add(jmIspisDoc);
    this.add(jmRmRezKol);
    this.add(jmTemeljnica);
    this.add(jmTemeljnica2);
    this.addSeparator();
    this.add(jmObrIzd);
    this.addSeparator();
    this.add(jmRemoteStart);
    if ("D".equalsIgnoreCase(frmParam.getParam("robno", "lotOpcija", "N",
        "Omoguæiti unos lota na dokumente s vanjskog ureðaja (D,N)")))
      this.add(jmImportLot);
    this.add(jmCreateKPR);
  }
  void jmDvijeGodine_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmDvijeGodine", jmDvijeGodine.getText());
  }
  void jmNovaGodina_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmPrijenosGodine", jmNovaGodina.getText());
  }
  void jmPST_actionPerformed(ActionEvent e) {
    frmPST fPST = (frmPST)raLoader.load("hr.restart.robno.frmPST");
    presPST.getPres().showJpSelectDoc("PST", fPST, true, jmPST.getText());
  }
  void jmIspisDoc_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raPrintAllDocs", jmIspisDoc.getText());
  }
  void jmRmRezKol_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upUnrealPonude", jmRmRezKol.getText()); // waas frmDelPon...
  }
  void jmTemeljnica_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmRobnoTemeljnica", jmTemeljnica.getText());
  }
  void jmTemeljnica2_actionPerformed(ActionEvent e) {
      SF.showFrame("hr.restart.robno.frmRobnoPeriodTemelj", jmTemeljnica2.getText());
  }
  
  void jmRemoteStart_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raLiteFrameRobnoReplicator", jmRemoteStart.getText());
  }
  
  
  void jmImportLot_actionPerformed(ActionEvent e) {
    frmImportLot.show(SF);
  }
  
  void jmCreateKPR_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmFormKPR", jmCreateKPR.getText());
  }
  
  void jmObrIzd_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raDostoIZD", jmObrIzd.getText());
  }
}