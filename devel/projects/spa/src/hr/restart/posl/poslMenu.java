/****license*****************************************************************
**   file: poslMenu.java
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
package hr.restart.posl;

import hr.restart.sisfun.frmParam;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class poslMenu extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmRuc = new JMenuItem();
  JMenuItem jmArtStat = new JMenuItem();
  JMenuItem jmArtStatTEST = new JMenuItem();
  JMenuItem jmArtParTEST = new JMenuItem();
  JMenuItem jmFinProdaja = new JMenuItem();
  JMenuItem jmFinIzvjAgent = new JMenuItem();
  JMenuItem jmFinIzvjTelemark = new JMenuItem();
  JMenuItem jmPrviKupci = new JMenuItem();
  JMenuItem jmPartneriGrupe = new JMenuItem();
  JMenuItem jmKupStat = new JMenuItem();
  JMenuItem jmObrtajArt = new JMenuItem();
  JMenuItem jmIzvjMalop = new JMenuItem();
  JMenuItem jmRekapProd = new JMenuItem();
  JMenuItem jmIsplataAgenta = new JMenuItem();
  JMenuItem jmOstaliRep = new JMenuItem();
  JMenuItem jmUnRealDoc = new JMenuItem();
  JMenuItem jmUpKPR = new JMenuItem();
  JMenuItem jmProdPoDuc = new JMenuItem();

  JMenuItem jmStatMonths = new JMenuItem();
  JMenuItem jmStatMonthsArtikls = new JMenuItem();

  public poslMenu(hr.restart.util.startFrame startframe) {
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
    jmRuc.setText("Pregled razlika u cijeni");
    jmRuc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRuc_actionPerformed(e);
      }
    });
    jmArtStat.setText("Top lista artikala");
    jmArtStat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmArtStat_actionPerformed(e);
      }
    });
    jmArtStatTEST.setText("Top lista artikala - TEST");
    jmArtStatTEST.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmArtStatTEST_actionPerformed(e);
      }
    });
    jmArtParTEST.setText("Top lista partnera - TEST");
    jmArtParTEST.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmParStatTEST_actionPerformed(e);
      }
    });
    jmFinProdaja.setText("Financijski izvještaj ukupnog prometa");
    jmFinProdaja.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFinProdaja_actionPerformed(e);
      }
    });
    jmFinIzvjAgent.setText("Financijski izvještaj po agentima");
    jmFinIzvjAgent.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFinIzvPoAgentima_actionPerformed(e);
      }
    });
    jmFinIzvjTelemark.setText("Financijski izvještaj po telemarketerima");
    jmFinIzvjTelemark.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFinIzvPoTelemarksima_actionPerformed(e);
      }
    });
    
    jmPrviKupci.setText("Pregled prvih kupaca");
    jmPrviKupci.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPrviKupci_actionPerformed(e);
      }
    });
    jmPartneriGrupe.setText("Pregled partnera po grupama");
    jmPartneriGrupe.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPartneriGrupe_actionPerformed(e);
      }
    });
    
    jmKupStat.setText("Top lista kupaca");
    jmKupStat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKupStat_actionPerformed(e);
      }
    });
    jmObrtajArt.setText("Koeficijent obrtaja po artiklima");
    jmObrtajArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmObrtajArt_actionPerformed(e);
      }
    });
    jmIzvjMalop.setText("Pregled prodaje po skladištima");
    jmIzvjMalop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIzvjMalop_actionPerformed(e);
      }
    });

    jmRekapProd.setText("Pregled prodaje po skladištima i naèinima plaæanja");
    jmRekapProd.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRekapProd_actionPerformed(e);
      }
    });
    jmIsplataAgenta.setText("Pregled i ažuriranje isplate agenata");
    jmIsplataAgenta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIsplataAgenta_actionPerformed(e);
      }
    });
    jmOstaliRep.setText("Ostali izvještaji");
    jmOstaliRep.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmOstaliRep_actionPerformed(e);
      }
    });
    jmUnRealDoc.setText("Pregled nerealiziranih dokumenata");
    jmUnRealDoc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmUnRealDoc_actionPerformed(e);
      }
    });
    jmUpKPR.setText("Pregled knjige popisa - KPR");
    jmUpKPR.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmUpKPR_actionPerformed(e);
      }
    });
    jmStatMonths.setText("Top lista kupaca - mjeseèno");
    jmStatMonths.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStatsMonths_actionPerformed(e);
      }
    });
    jmStatMonthsArtikls.setText("Top lista artikala - mjeseèno");
    jmStatMonthsArtikls.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStatsMonthsArt_actionPerformed(e);
      }
    });
    jmProdPoDuc.setText("Pregled prodaje po prodajnim mjestima");
    jmProdPoDuc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmProdPoDuc_actionPerformed(e);
      }
    }
);


    this.add(jmRuc);
    this.add(jmKupStat);
//    this.add(jmStatMonths);
    // TODO test kod gomexa
    this.add(jmArtStat);
    this.addSeparator();
    /*this.add(jmArtStatTEST);
    this.add(jmArtParTEST);*/
    
    if (frmParam.getFrmParam().getParam("robno", "isLikeFr", "N", "Agenti i telemarketeri postoje u firmi").equals("D")) {
      this.add(jmFinProdaja);
      this.add(jmFinIzvjAgent);
      this.add(jmFinIzvjTelemark);
      this.add(jmPrviKupci);
      this.add(jmPartneriGrupe);
      this.addSeparator();
    }
    
//    this.add(jmStatMonthsArtikls);
    this.add(jmObrtajArt);
    // TODO test kod gomexa
    this.add(jmProdPoDuc);
    this.add(jmIsplataAgenta);
//    this.add(jmIzvjMalop);
//    this.add(jmRekapProd);
    this.addSeparator();
    this.add(jmUnRealDoc);
    this.addSeparator();
    this.add(jmOstaliRep);
    this.addSeparator();
    this.add(jmUpKPR);
  }
  void jmRuc_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.dlgPregledRUC", jmRuc.getText());/*"hr.restart.robno.dlgPregledMarze", jmRuc.getText());*/
  }
  void jmArtStat_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.ispStatArt", jmArtStat.getText()); // was "hr.restart.robno.ispStatArtDva"
  }
  void jmArtStatTEST_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.ispStatArtFast", jmArtStat.getText()+" - TEST"); // was "hr.restart.robno.ispStatArtDva"
  }
  void jmParStatTEST_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.ispStatParFast", jmKupStat.getText()+" - TEST"); // was "hr.restart.robno.ispStatArtDva"
  }
  void jmFinProdaja_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvjestajProdaja", jmFinProdaja.getText()); //TODO !!
  }
  void jmFinIzvPoAgentima_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvProdajaAgentiFaktureAparati", jmFinIzvjAgent.getText()); //TODO !!
  }
  void jmFinIzvPoTelemarksima_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvProdajaTelemarkFaktureAparati", jmFinIzvjTelemark.getText()); //TODO !!
  }
  void jmPrviKupci_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raPrviKupci", jmPrviKupci.getText()); //TODO !!
  }
  void jmPartneriGrupe_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.RaPregledKupacaPoGrupama", jmPartneriGrupe.getText()); //TODO !!
  }
  void jmKupStat_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.ispStatPar", jmKupStat.getText()); // was hr.restart.robno.ispStatParDva
  }
  void jmObrtajArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raKoefObrt", jmObrtajArt.getText());
  }
  void jmIzvjMalop_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raUpitMjeIzvjMal", jmIzvjMalop.getText());
  }
  void jmIsplataAgenta_actionPerformed(ActionEvent e){
    SF.showFrame("hr.restart.robno.frmIsplataAgenta", jmIsplataAgenta.getText());
  }
  void jmRekapProd_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.raUpitRekapProd", jmRekapProd.getText());
  }
  void jmOstaliRep_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmPoslReportList", jmOstaliRep.getText());
  }
  void jmUnRealDoc_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.frmUnRealDoc", jmUnRealDoc.getText()); // FrmNerealiziraniDok , frmUnRealDoc
    SF.showFrame("hr.restart.robno.FrmNerealiziraniDok", jmUnRealDoc.getText());
  }
  void jmUpKPR_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upKPR", jmUpKPR.getText());
  }
  void jmStatsMonths_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upStatsMonths", jmStatMonths.getText());
  }
  void jmStatsMonthsArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upStatsMonthsArtikls", jmStatMonthsArtikls.getText());
  }
  void jmProdPoDuc_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upProdajaPoDucanima",jmProdPoDuc.getText());
  }
}


//package hr.restart.posl;
//
//import java.awt.event.*;
//import java.util.*;
//import javax.swing.*;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: </p>
// * @author not attributable
// * @version 1.0
// */
//
//public class poslMenu extends JMenu {
//  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
//
//  hr.restart.util.startFrame SF;
//  JMenuItem jmRuc = new JMenuItem();
//  JMenuItem jmArtStat = new JMenuItem();
//  JMenuItem jmKupStat = new JMenuItem();
//  JMenuItem jmObrtajArt = new JMenuItem();
//  JMenuItem jmIzvjMalop = new JMenuItem();
//  JMenuItem jmRekapProd = new JMenuItem();
//  JMenuItem jmOstaliRep = new JMenuItem();
//  JMenuItem jmUnRealDoc = new JMenuItem();
//  JMenuItem jmUpKPR = new JMenuItem();
//
//  JMenuItem jmStatMonths = new JMenuItem();
//  JMenuItem jmStatMonthsArtikls = new JMenuItem();
//
//  public poslMenu(hr.restart.util.startFrame startframe) {
//    SF = startframe;
//    jbInit();
//    this.addAncestorListener(new javax.swing.event.AncestorListener() {
//      public void ancestorAdded(javax.swing.event.AncestorEvent e) {
//      }
//      public void ancestorMoved(javax.swing.event.AncestorEvent e) {
//      }
//      public void ancestorRemoved(javax.swing.event.AncestorEvent e) {
//      }
//    });
//  }
//  private void jbInit() {
//    this.setText("Poslovodstvo");
//    jmRuc.setText("Pregled razlika u cijeni");
//    jmRuc.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmRuc_actionPerformed(e);
//      }
//    });
//    jmArtStat.setText("Top lista artikala");
//    jmArtStat.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmArtStat_actionPerformed(e);
//      }
//    });
//    jmKupStat.setText("Top lista kupaca");
//    jmKupStat.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmKupStat_actionPerformed(e);
//      }
//    });
//    jmObrtajArt.setText("Koeficijent obrtaja po artiklima");
//    jmObrtajArt.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmObrtajArt_actionPerformed(e);
//      }
//    });
//    jmIzvjMalop.setText("Pregled prodaje po skladištima");
//    jmIzvjMalop.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmIzvjMalop_actionPerformed(e);
//      }
//    });
//
//    jmRekapProd.setText("Pregled prodaje po skladištima i naèinima plaæanja");
//    jmRekapProd.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmRekapProd_actionPerformed(e);
//      }
//    });
//    jmOstaliRep.setText("Ostali izvještaji");
//    jmOstaliRep.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmOstaliRep_actionPerformed(e);
//      }
//    });
//    jmUnRealDoc.setText("Pregled nerealiziranih dokumenata");
//    jmUnRealDoc.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmUnRealDoc_actionPerformed(e);
//      }
//    });
//    jmUpKPR.setText("Knjiga popisa - KPR");
//    jmUpKPR.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmUpKPR_actionPerformed(e);
//      }
//    });
//    jmStatMonths.setText("Top lista kupaca - mjeseèno");
//    jmStatMonths.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmStatsMonths_actionPerformed(e);
//      }
//    });
//    jmStatMonthsArtikls.setText("Top lista artikala - mjeseèno");
//    jmStatMonthsArtikls.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmStatsMonthsArt_actionPerformed(e);
//      }
//    });
//
//
//    this.add(jmRuc);
//    this.add(jmKupStat);
////    this.add(jmStatMonths);
//    this.add(jmArtStat);
//    this.add(jmStatMonthsArtikls);
//    this.add(jmObrtajArt);
//    this.add(jmIzvjMalop);
//    this.add(jmRekapProd);
//    this.addSeparator();
//    this.add(jmUnRealDoc);
//    this.addSeparator();
//    this.add(jmOstaliRep);
//    this.addSeparator();
//    this.add(jmUpKPR);
//  }
//  void jmRuc_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.dlgPregledRUC", jmRuc.getText());/*"hr.restart.robno.dlgPregledMarze", jmRuc.getText());*/
//  }
//  void jmArtStat_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.ispStatArtDva", jmArtStat.getText());
//  }
//  void jmKupStat_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.ispStatPar", jmKupStat.getText()); // washr.restart.robno.ispStatParDva 
//  }
//  void jmObrtajArt_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.raKoefObrt", jmObrtajArt.getText());
//  }
//  void jmIzvjMalop_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.raUpitMjeIzvjMal", jmIzvjMalop.getText());
//  }
//  void jmRekapProd_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.raUpitRekapProd", jmRekapProd.getText());
//  }
//  void jmOstaliRep_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.frmPoslReportList", jmOstaliRep.getText());
//  }
//  void jmUnRealDoc_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.frmUnRealDoc", jmUnRealDoc.getText());
//  }
//  void jmUpKPR_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.upKPR", jmUpKPR.getText());
//  }
//  void jmStatsMonths_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.upStatsMonths", jmStatMonths.getText());
//  }
//  void jmStatsMonthsArt_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.upStatsMonthsArtikls", jmStatMonthsArtikls.getText());
//  }
//
//
//
//}
