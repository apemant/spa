/****license*****************************************************************
**   file: menuIzvMalo.java
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

public class menuIzvMalo extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmRekapUplata = new JMenuItem();
  JMenuItem jmPregledR1Maloprodaja = new JMenuItem();
  JMenuItem jmSpecCek = new JMenuItem();
  JMenuItem jmInventCek = new JMenuItem();
  JMenuItem jmSpecKart = new JMenuItem();
  JMenuItem jmIzvNacPl = new JMenuItem();
  JMenuItem jmCijenePol = new JMenuItem();
  JMenuItem jmDeklaracije = new JMenuItem();
  JMenuItem jmIzvArt = new JMenuItem();
  JMenuItem jmOstaliRep = new JMenuItem();
  JMenuItem jmKPR = new JMenuItem();
  JMenuItem jmZOP = new JMenuItem();
  JMenuItem jmPregledKPRGRN = new JMenuItem();
  JMenuItem jmPregledZaduzenjeKPR = new JMenuItem();
  public JMenuItem jmFISBIH = new JMenuItem();

  public menuIzvMalo(hr.restart.util.startFrame startframe) {
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
    this.setText("Izvještaji");
    jmFISBIH.setText("Fiskalni izvještaji");
    jmFISBIH.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFISBIH_actionPerformed(e);
      }
    });
    jmRekapUplata.setText("Rekapitulacija uplata");
    jmRekapUplata.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRekapUplata_actionPerformed(e);
      }
    });
    jmPregledR1Maloprodaja.setText("Pregled R1 raèuna");
    jmPregledR1Maloprodaja.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPregledR1Maloprodaja_actionPerformed(e);
      }
    });
    jmSpecCek.setText("Specifikacija èekova");
    jmSpecCek.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmSpecCek_actionPerformed(e);
      }
    });

    jmInventCek.setText("Inventura èekova");
    jmInventCek.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jminventCek_actionPerformed(e);
      }
    });

    jmSpecKart.setText("Specifikacija slipova kreditnih kartica");
    jmSpecKart.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmSpecKart_actionPerformed(e);
      }
    });
    jmIzvNacPl.setText("Pregled prodaje po naèinima plaæanja");
    jmIzvNacPl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIzvNacPl_actionPerformed(e);
      }
    });
    jmCijenePol.setText("Ispis cijena za police");
    jmCijenePol.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmCijenePol_actionPerformed(e);
      }
    });
    jmDeklaracije.setText("Ispis deklaracije");
    jmDeklaracije.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmDeklaracije_actionPerformed(e);
      }
    });
    jmIzvArt.setText("Pregled prodaje po artiklima");
    jmIzvArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIzvArt_actionPerformed(e);
      }
    });
    jmOstaliRep.setText("Ostali izvještaji");
    jmOstaliRep.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmOstaliRep_actionPerformed(e);
      }
    });
    jmKPR.setText("Knjiga popisa-KPR");
    jmKPR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKPR_actionPerformed(e);
      }
    });
    jmZOP.setText("Zapisnik o danim popustima");
    jmZOP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmZOP_actionPerformed(e);
      }
    });
    jmPregledKPRGRN.setText("Pregled KPR za GRN i GOT");
    jmPregledKPRGRN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
      	jmPregledKPRGRN_actionPerformed(e);
      }
    });
    jmPregledZaduzenjeKPR.setText("Pregled dokumenta zaduzenja za KPR iz GRN i GOT -a");
    jmPregledZaduzenjeKPR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
      	jmPregledZaduzenjeKPR_actionPerformed(e);
      }
    });
    this.add(jmRekapUplata);
    this.add(jmPregledR1Maloprodaja);
    this.add(jmIzvNacPl);
    this.add(jmSpecCek);
    this.add(jmInventCek);
    this.add(jmSpecKart);
    this.add(jmCijenePol);
    this.add(jmDeklaracije);
    this.addSeparator();
    this.add(jmKPR);
    this.add(jmZOP);
    this.add(jmIzvArt);
    this.add(jmPregledKPRGRN);
    this.add(jmPregledZaduzenjeKPR);
    this.addSeparator();
    this.add(jmOstaliRep);
    if (repFISBIH.isFISBIH()) {
      addSeparator();
      add(jmFISBIH);
    }
  }
  void jmRekapUplata_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upRekapUplata", jmRekapUplata.getText());
  }
  void jmPregledR1Maloprodaja_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.UpPregledR1Maloprodaja", jmPregledR1Maloprodaja.getText());
  }
  void jmSpecCek_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmSpecCek", jmSpecCek.getText());
  }
  void jminventCek_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmInventuraCekova", jmInventCek.getText());
  }
  void jmSpecKart_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmSpecKart", jmSpecKart.getText());
  }
  void jmIzvNacPl_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvNacPl", jmIzvNacPl.getText());
  }
  void jmCijenePol_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmCijenePolDva", jmCijenePol.getText());
  }
  void jmDeklaracije_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmDeklaracija", jmDeklaracije.getText());
  }
  void jmKPR_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upKPR_NextGeneration", jmKPR.getText());
  }
  void jmZOP_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upZOP", jmZOP.getText());
  }
  void jmIzvArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvArt", jmIzvArt.getText());
  }
  void jmOstaliRep_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.mp.frmMpReportList", jmOstaliRep.getText());
  }
  void jmPregledKPRGRN_actionPerformed(ActionEvent e){
    SF.showFrame("hr.restart.robno.upKPRFake", jmPregledKPRGRN.getText());
  	
  }
  void jmPregledZaduzenjeKPR_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.UpPregledZaduzenjeKPR", jmPregledZaduzenjeKPR.getText());	  	
  }
  public void jmFISBIH_actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    SF.showFrame("hr.restart.robno.FISBIHIzvjestaji", jmFISBIH.getText());
  }

}
