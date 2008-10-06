/****license*****************************************************************
**   file: menuOpMp.java
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
package hr.restart.mp;

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

public class menuOpMp extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmSkladista = new JMenuItem();
  JMenuItem jmPorezi = new JMenuItem();
  JMenuItem jmJedMj = new JMenuItem();
  JMenuItem jmGrupaArt = new JMenuItem();
  JMenuItem jmArtikli = new JMenuItem();
  JMenuItem jmShemeKonta = new JMenuItem();
  JMenuItem jmShemeKontaOJ = new JMenuItem();
  JMenuItem jmProdMjesta = new JMenuItem();
  JMenuItem jmVrArt = new JMenuItem();
  JMenuItem jmBlagajnici = new JMenuItem();

  public menuOpMp(hr.restart.util.startFrame startframe) {
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
    this.setText("Osnovni podaci");
    jmSkladista.setText(res.getString("jmSkladista_text"));
    jmSkladista.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmSkladista_actionPerformed(e);
      }
    });
    jmPorezi.setText(res.getString("jmPorezi_text"));
    jmPorezi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPorezi_actionPerformed(e);
      }
    });
    jmJedMj.setText("Jednice mjere");
    jmJedMj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmJedMj_actionPerformed(e);
      }
    });
    jmGrupaArt.setText(res.getString("jmGrupaArt_text"));
    jmGrupaArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmGrupaArt_actionPerformed(e);
      }
    });
    jmArtikli.setText(res.getString("jmArtikli_text"));
    jmArtikli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmArtikli_actionPerformed(e);
      }
    });
    jmShemeKonta.setText("Sheme kontiranja");
    jmShemeKonta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmShemeKonta_actionPerformed(e);
      }
    });
    jmShemeKontaOJ.setText("Sheme kontiranja - OJ");
    jmShemeKontaOJ.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmShemeKontaOJ_actionPerformed(e);
      }
    });
    jmProdMjesta.setText("Blagajne");
    jmProdMjesta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmProdMjesta_actionPerformed(e);
      }
    });
    jmBlagajnici.setText("Blagajnici");
    jmBlagajnici.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
      	jmBlagajnici_actionPerformed(e);
      }
    });
    jmVrArt.setText("Vrste artikla");
    jmVrArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmVrArt_actionPerformed(e);
      }
    });
    this.add(jmSkladista);
    this.add(jmPorezi);
    this.add(jmJedMj);
    this.add(jmGrupaArt);
    //this.add(jmVrArt);
    this.add(jmArtikli);
    this.addSeparator();
    this.add(jmShemeKonta);
    this.add(jmShemeKontaOJ);
    this.addSeparator();
    this.add(jmProdMjesta);
    this.add(jmBlagajnici);
  }

  void jmSkladista_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmSkladiste", jmSkladista.getText());
  }
  void jmPorezi_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmPorezi", jmPorezi.getText());
  }
  void jmJedMj_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmJedMj", jmJedMj.getText());
  }
  void jmGrupaArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmGrupArt", jmGrupaArt.getText());
  }
  void jmArtikli_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmArtikli", jmArtikli.getText());
  }
  void jmShemeKonta_actionPerformed(ActionEvent e) {
    frmShemeKontaMp fShKonta = (frmShemeKontaMp) SF.showFrame("hr.restart.mp.frmShemeKontaMp", 0, jmShemeKonta.getText(), false);
    fShKonta.go();
  }
  void jmShemeKontaOJ_actionPerformed(ActionEvent e) {
    frmShemeKontaMpOJ fShKonta = (frmShemeKontaMpOJ) SF.showFrame("hr.restart.mp.frmShemeKontaMpOJ", 0, jmShemeKonta.getText(), false);
    fShKonta.go();
  }
  void jmProdMjesta_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmProdMj", jmProdMjesta.getText());
  }
  void jmBlagajnici_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmBlagajnici", jmBlagajnici.getText());
  }
  void jmVrArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmVrArt", jmVrArt.getText());
  }

}