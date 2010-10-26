/****license*****************************************************************
**   file: menuNar.java
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
import hr.restart.util.raFileFilter;
import hr.restart.util.raLoader;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class menuNar extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmZAH = new JMenuItem();
  JMenuItem jmUZP = new JMenuItem();
  JMenuItem jmNDO = new JMenuItem();
  JMenuItem jmNKU = new JMenuItem();
  JMenuItem jmNKUedi = new JMenuItem();
  JMenuItem jmNKUedi2 = new JMenuItem();
  
  JFileChooser fc = new JFileChooser();

  public menuNar(hr.restart.util.startFrame startframe) {
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
    this.setText("Narudžbe");
    jmZAH.setText("Trebovanje");
    jmZAH.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmZAH_actionPerformed(e);
      }
    });
    jmUZP.setText("Upit za ponudu");
    jmUZP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmUZP_actionPerformed(e);
      }
    });
    jmNDO.setText("Narudžbe dobavljaèu");
    jmNDO.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNDO_actionPerformed(e);
      }
    });
    jmNKU.setText("Narudžbe kupca");
    jmNKU.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNKU_actionPerformed(e);
      }
    });
    jmNKUedi.setText("Import narudžbe kupca");
    jmNKUedi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNKUedi_actionPerformed(e);
      }
    });
    jmNKUedi2.setText("Preuzimanje narudžbi kupca putem EDI");
    jmNKUedi2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNKUedi2_actionPerformed(e);
      }
    });
    this.add(jmZAH);
    this.add(jmUZP);
    this.add(jmNDO);
    this.add(jmNKU);
    frmParam.getParam("robno", "ediCskl", "",
      "Šifra OJ ili skladišta za EDI narudžbe");
    if (frmParam.getParam("robno", "ediOrder", "N",
        "Omoguæiti import narudžbi kupca preko EDI (D,N,I)").equals("D")) {
      this.add(jmNKUedi);
      fc.setCurrentDirectory(new File("."));
      FileFilter ff = new raFileFilter("EDI XML datoteke (*.xml)");
      fc.addChoosableFileFilter(ff);
      fc.setFileFilter(ff);
    } else if (frmParam.getParam("robno", "ediOrder", "N",
    "Omoguæiti import narudžbi kupca preko EDI (D,N,I)").equals("I")) {
      this.add(jmNKUedi2);
    }
  }
  void jmZAH_actionPerformed(ActionEvent e) {
    raZAH fZAH = (raZAH) raLoader.load("hr.restart.robno.raZAH");
    presZAH.getPres().showJpSelectDoc("TRE", fZAH, true, "Trebovanje");
  }
  void jmUZP_actionPerformed(ActionEvent e) {
    raUZP fUZP = (raUZP) raLoader.load("hr.restart.robno.raUZP");
    presUZP.getPres().showJpSelectDoc("UZP", fUZP, true, "Upit za ponudu");
  }
  void jmNDO_actionPerformed(ActionEvent e) {
    frmNarDob frmND = (frmNarDob) raLoader.load("hr.restart.robno.frmNarDob");
    presNDO.getPres().showJpSelectDoc("NDO", frmND, true, "Narudžbe dobavljaèu");
  }
  void jmNKU_actionPerformed(ActionEvent e) {
    raNKU ranku = (raNKU)raLoader.load("hr.restart.robno.raNKU");
    presNKU.getPres().showJpSelectDoc("NKU", ranku, true, "Narudžbe kupca");
  }
  
  void jmNKUedi2_actionPerformed(ActionEvent e) {
    //SF.showFrame("hr.restart.robno.frmEDI", jmNKUedi2.getText());
    String path = frmParam.getParam("robno", "panteonPath", "/home/abf/tmp/hr/test",
        "Putanja mape za EDI preko Panteona");
    raEDI.importPanteon(new File(path), true);
  }
  void jmNKUedi_actionPerformed(ActionEvent e) {
    if (fc.showOpenDialog(null) == fc.APPROVE_OPTION) {
      
      String cskl = frmParam.getParam("robno", "ediCskl", "",
          "Šifra OJ ili skladišta za EDI narudžbe");
      raOrderEDI.createOrder(cskl, fc.getSelectedFile());
    }
  }
}