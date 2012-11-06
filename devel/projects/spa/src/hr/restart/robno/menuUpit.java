/****license*****************************************************************
**   file: menuUpit.java
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

public class menuUpit extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  public JMenuItem jmStanje = new JMenuItem();
  public JMenuItem jmStanjeOld = new JMenuItem();
  public JMenuItem jmKartica = new JMenuItem();
  public JMenuItem jmKartKupArt = new JMenuItem();
  public JMenuItem jmKartKupArt2 = new JMenuItem();
  public JMenuItem jmDnevnik = new JMenuItem();
  public JMenuItem jmCjenik = new JMenuItem();
  public JMenuItem jmCjenikCORG = new JMenuItem();
  public JMenuItem jmRab = new JMenuItem();
  public JMenuItem jmArtSklad = new JMenuItem();
  public JMenuItem jmUIDoc = new JMenuItem();
  public JMenuItem jmTotaliKartica = new JMenuItem();
  public JMenuItem jmViewCalc = new JMenuItem();
  public JMenuItem jmRuc = new JMenuItem();
//  JMenu jmOP = new JMenu();

  public menuUpit(hr.restart.util.startFrame startframe) {
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
    this.setText("Upiti");
    jmStanje.setText("Stanje artikala");
    jmStanje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStanje_actionPerformed(e);
      }
    });
    jmStanjeOld.setText("Stanje artikala");
    jmStanjeOld.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStanjeOld_actionPerformed(e);
      }
    });
    jmKartica.setToolTipText("");
    jmKartica.setText("Kartica artikla");
    jmKartica.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKartica_actionPerformed(e);
      }
    });
    jmKartKupArt.setText("Kartica kupci-artikli");
    jmKartKupArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKartKupArt_actionPerformed(e);
      }
    });
    jmKartKupArt2.setText("Kartica kupci-artikli 2");
    jmKartKupArt2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKartKupArt2_actionPerformed(e);
      }
    });
    jmDnevnik.setText("Dnevnik knji\u017Eenja");
    jmDnevnik.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmDnevnik_actionPerformed(e);
      }
    });
    
    jmCjenik.setText("Cjenici za partnere iz skladišta");
    jmCjenik.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmCjenik_actionPerformed(e);
      }
    });
    jmCjenikCORG.setText("Cjenici za partnere iz org. jed.");
    jmCjenikCORG.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmCjenikCORG_actionPerformed(e);
      }
    });
    
    jmRab.setText("Rabati za partnere");
    jmRab.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRab_actionPerformed(e);
      }
    });
    
    jmArtSklad.setText("Artikli - skladišta");
    jmArtSklad.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmArtSklad_actionPerformed(e);
      }
    });
    jmUIDoc.setText("Ulazni/izlazni dokumenti");
    jmUIDoc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmUIDoc_actionPerformed(e);
      }
    });
    jmTotaliKartica.setText("Kartice više artikala");
    jmTotaliKartica.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmTotaliKartica_actionPerformed(e);
      }
    });
    jmViewCalc.setText("Pregled cijena");
    jmViewCalc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmViewCalc_actionPerformed(e);
      }
    });
    jmRuc.setText("Pregled razlika u cijeni");
    jmRuc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRuc_actionPerformed(e);
      }
    });
    this.add(jmStanje);
//    this.add(jmStanjeOld);
    this.add(jmKartica);
    this.add(jmTotaliKartica);
    this.add(jmKartKupArt);
//    this.add(jmKartKupArt2); //TODO
    this.add(jmDnevnik);
    this.add(jmArtSklad);
    this.add(jmUIDoc);
    this.addSeparator();
    this.add(jmRuc);
    this.add(jmViewCalc);
    this.add(jmCjenik);
    this.add(jmCjenikCORG);
    this.add(jmRab);
  }
  public void jmStanje_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.UpStanjeRobno", jmStanje.getText()); //was upStanjeNaSkladistu
    
  }
  public void jmStanjeOld_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upStanjeNaSkladistu", jmStanje.getText()+" - STARO"); //was upStanjeNaSkladistu
    
  }
  public void jmKartica_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upKartica", 15, jmKartica.getText(), false);
    upKartica.getupKartica().clearOutsideData();
    SF.showFrame("hr.restart.robno.upKartica", jmKartica.getText());
    
  }
  public void jmKartKupArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upKarticaKupca", jmKartKupArt.getText());
  }
  public void jmKartKupArt2_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upKartKupArt2", jmKartKupArt2.getText());
  }
  public void jmDnevnik_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upDnevnik", jmDnevnik.getText());
  }
  public void jmCjenik_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmCjenik fCjenik = (frmCjenik) SF.showFrame("hr.restart.robno.frmCjenik", 0, jmCjenik.getText(), false);
    fCjenik.go();
  }
  public void jmRab_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmRabatShema fRab = (frmRabatShema) SF.showFrame("hr.restart.robno.frmRabatShema", 0, jmRab.getText(), false);
    fRab.go();
  }
  public void jmCjenikCORG_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmCjenik fCjenik = (frmCjenik) SF.showFrame("hr.restart.robno.frmCjenikCORG", 0, jmCjenikCORG.getText(), false);
    fCjenik.go();
  }
  public void jmArtSklad_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upArtSklad", jmArtSklad.getText());
  }
  public void jmUIDoc_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upUlazIzlaz", jmUIDoc.getText());
  }
  public void jmTotaliKartica_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upTotKar", jmTotaliKartica.getText());
  }
  public void jmRuc_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.dlgPregledRUC", jmRuc.getText());/*"hr.restart.robno.dlgPregledMarze", jmRuc.getText());*/
  }
  public void jmViewCalc_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.prewievCalc", jmViewCalc.getText());
  }
}