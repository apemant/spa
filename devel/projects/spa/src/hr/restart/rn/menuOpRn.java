/****license*****************************************************************
**   file: menuOpRn.java
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
package hr.restart.rn;

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

public class menuOpRn extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmSkladista = new JMenuItem();
  JMenuItem jmPorezi = new JMenuItem();
  JMenuItem jmJedMj = new JMenuItem();
  JMenuItem jmNormativi = new JMenuItem();
  JMenuItem jmGrupaArt = new JMenuItem();
  JMenuItem jmSkupArt = new JMenuItem();
  JMenuItem jmArtikli = new JMenuItem();
  JMenuItem jmRN_Tekst = new JMenuItem();
  JMenuItem jmRN_ShemaZnac = new JMenuItem();
  JMenuItem jmRN_Sifarnici = new JMenuItem();
  JMenuItem jmVlasnici = new JMenuItem();


  public menuOpRn(hr.restart.util.startFrame startframe) {
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
    jmNormativi.setText("Normativi");
    jmNormativi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNormativi_actionPerformed(e);
      }
    });
    jmGrupaArt.setText(res.getString("jmGrupaArt_text"));
    jmGrupaArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmGrupaArt_actionPerformed(e);
      }
    });
    jmSkupArt.setText("Skupine artikala");
    jmSkupArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmSkupArt_actionPerformed(e);
      }
    });
    jmArtikli.setText(res.getString("jmArtikli_text"));
    jmArtikli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmArtikli_actionPerformed(e);
      }
    });
    jmRN_Tekst.setText("Naruèeni radovi");
    jmRN_Tekst.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRN_Tekst_actionPerformed(e);
      }
    });
    jmRN_ShemaZnac.setText("Vrste subjekata");
    jmRN_ShemaZnac.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRN_ShemaZnac_actionPerformed(e);
      }
    });
    jmRN_Sifarnici.setText("Šifrarnici");
    jmRN_Sifarnici.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRN_Sifarnici_actionPerformed(e);
      }
    });
    jmVlasnici.setText("Kupci - vlasnici");
    jmVlasnici.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmVlasnici_actionPerformed(e);
      }
    });


    this.add(jmSkladista);
    this.add(jmPorezi);
    this.add(jmJedMj);
    this.add(jmNormativi);
    this.add(jmGrupaArt);
    this.add(jmArtikli);
    this.add(jmSkupArt);
    this.add(jmVlasnici);
    this.addSeparator();
    this.add(jmRN_Tekst);
    this.add(jmRN_ShemaZnac);
    this.add(jmRN_Sifarnici);
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
  void jmNormativi_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmNorme", jmNormativi.getText());
  }
  void jmGrupaArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmGrupArt", jmGrupaArt.getText());
  }
  void jmSkupArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmSkup_Art", jmSkupArt.getText());
  }
  void jmArtikli_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmArtikli", jmArtikli.getText());
  }
  void jmRN_Tekst_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmNarRadovi", res.getString("frmNarRadovi_title"));
  }
  void jmRN_ShemaZnac_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmZnacajke fZnacajke = (hr.restart.robno.frmZnacajke)raLoader.load("hr.restart.robno.frmZnacajke");
    fZnacajke.go();
  }
  void jmRN_Sifarnici_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmSifrarnik fSifrarnik = (hr.restart.robno.frmSifrarnik)raLoader.load("hr.restart.robno.frmSifrarnik");
    fSifrarnik.go();
  }
  void jmVlasnici_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmVlasnik", res.getString("frmVlasnik_title"));
  }
}

