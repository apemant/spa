/****license*****************************************************************
**   file: frmSK.java
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
package hr.restart.sk;


import hr.restart.gk.frmNalozi;
import hr.restart.util.IntParam;
import hr.restart.util.PreSelect;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class frmSK extends startFrame {
  JMenuBar jmb = new JMenuBar();
  JMenu jmOsnovni = new JMenu();
  JMenu jmObrade = new JMenu();
  JMenuItem jmiRacuni = new JMenuItem();
  JMenuItem jmiUplate = new JMenuItem();
//  JMenuItem jmiPokriv = new JMenuItem();
  JMenuItem jmiKO = new JMenuItem();
  JMenuItem jmiKD = new JMenuItem();
  JMenuItem jmiPregled = new JMenuItem();
//  JMenuItem jmiKumulCheck = new JMenuItem();
  JMenuItem jmiSkCheck = new JMenuItem();
  JMenuItem jmiPokCheck = new JMenuItem();
  JMenuItem jmiAutoCover = new JMenuItem();
  JMenuItem jmiCSK = new JMenuItem();
  JMenuItem jmiGKSK = new JMenuItem();
  JMenu jmUpiti = new JMenu();
  JMenu jmIzvj = new JMenu();
  JMenu jmKnj = new JMenu();
  JMenu jSisAla = new JMenu();
  JMenuItem jmiDefPDV = new JMenuItem();
  JMenuItem jmiPDV = new JMenuItem();
  JMenuItem jmiPDV2 = new JMenuItem();
  JMenuItem jmiKnjigeUI = new JMenuItem();
  JMenuItem jmiKamate = new JMenuItem();
  JMenuItem jmiZbirPreg = new JMenuItem();
  JMenuItem jmiPojPreg = new JMenuItem();
  JMenuItem jmiUspPreg = new JMenuItem();
  JMenuItem jmiAutoPok = new JMenuItem();
  JMenuItem jmiKartice = new JMenuItem();
  JMenuItem jmiDugPotr = new JMenuItem();
  JMenuItem jmiZbirDugPotr = new JMenuItem();
  JMenuItem jmiZbirPregPer = new JMenuItem();
  JMenuItem jmiKupDobPer = new JMenuItem();
  JMenuItem jmiZbirPregPerVal = new JMenuItem();
  JMenuItem jmTemeljGK = new JMenuItem();
  JMenuItem jmiShemek = new JMenuItem();
  JMenuItem jmiVrshemek = new JMenuItem();
  JMenuItem jmiKolone = new JMenuItem();
  JMenuItem jmiShemeveze = new JMenuItem();
  JMenuItem jmiUraIra = new JMenuItem();
  JMenuItem jmiKam = new JMenuItem();
  JMenuItem jmiSKReportList = new JMenuItem();
  JMenuItem jmiVirmaniSK = new JMenuItem();
  JMenuItem jmiBG = new JMenuItem();
  JMenuItem jmiValute = new JMenuItem();
  JMenuItem jmiNadzornaKnjiga = new JMenuItem();
  JMenuItem jmiImport = new JMenuItem();

  public frmSK() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jmOsnovni.setText("Osnovni podaci");
    jmObrade.setText("Obrade");
    jmiRacuni.setText("Ra\u010Duni");
    jmiUplate.setText("Uplate");
//    jmiPokriv.setText("Pokrivanje");
    jmiKO.setText("Knjižne obavijesti");
    jmiKD.setText("Potvr\u0111ivanje dokumenata");
    jmiPregled.setText("Pregled arhive");
    jmiNadzornaKnjiga.setText("Nadzorna knjiga uvoza");
    jmiImport.setText("Import SK raèuna");
//    jmiKumulCheck.setText("Provjera kumulativa");
    jmiSkCheck.setText("Provjera VD stavaka");
    jmiPokCheck.setText("Provjera konzistentnosti pokrivanja");
    jmiAutoCover.setText("Automatsko pokrivanje po broju i saldu");
    jmiDefPDV.setText("Definicija obrasca PDV");
    jmiPDV.setText("Obrazac PDV do 30.06.2013.");
    jmiPDV2.setText("Obrasci za poreznu upravu");
    jSisAla.setText("Sistemski alati");
    jmUpiti.setText("Upiti");
    jmIzvj.setText("Izvještaji");
    jmKnj.setText("Knjiženje");
    jmiKnjigeUI.setText("Knjige URA / IRA");
    jmiKamate.setText("Tablica kamata");
    jmiZbirPreg.setText("Zbirni pregledi za godinu");
    jmiZbirDugPotr.setText("Zbirni pregled otvorenog prometa");
    jmiPojPreg.setText("Kartice kupaca / dobavljaèa");
    jmiUspPreg.setText("Usporedni pregledi kupaca / dobavlja\u010Da");
    jmiAutoPok.setText("Poluautomatsko pokrivanje po saldu");
    jmiKartice.setText("Kartice kupaca / dobavlja\u010Da u valuti");
    jmiDugPotr.setText("Dugovanja / potraživanja");
    jmiUraIra.setText("Ispis knjiga URA/IRA");
    jmiKam.setText("Obra\u010Dun kamata");
    jmiSKReportList.setText("Ostali izvještaji");
    jmiVirmaniSK.setText("Virmani");
    jmiZbirPregPer.setText("Zbirni pregled za period");
    jmiKupDobPer.setText("Kupci / dobavlja\u010Di u valuti");
    jmiZbirPregPerVal.setText("Zbirni pregled za period u valuti");
    jmTemeljGK.setText("Ispis temeljnice za GK");
    jmiShemek.setText("Sheme knjiženja");
    jmiVrshemek.setText("Vrste shema knjiženja");
    jmiKolone.setText("Kolone knjige");
    jmiShemeveze.setText("Sheme veza");
    jmiBG.setText("Brisanje godine");
    jmiValute.setText("Popravak domicilne valute i teèaja");
    jmiCSK.setText("Napuni CSKSTAVKE");
    jmiGKSK.setText("Provjeri GK i SK veze");
    jmb.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmb.add(jmOsnovni);
    jmb.add(jmObrade);
    jmb.add(jmUpiti);
    jmb.add(jmIzvj);
    jmb.add(jmKnj);
    jmObrade.add(jmiRacuni);
    jmObrade.add(jmiUplate);
    jmObrade.add(jmiKO);
    jmObrade.add(jmiKD);
    jmObrade.add(jmiPregled);
    jmObrade.addSeparator();
    jmObrade.add(jmiAutoCover);
    jmObrade.add(jmiAutoPok);
    jmObrade.addSeparator();
    jmObrade.add(jmiNadzornaKnjiga);
    String ix = IntParam.getTag("importxml.dir");
    if (ix !=null && ix.length() > 0) {
      jmObrade.addSeparator();
      jmObrade.add(jmiImport);
    }
//    jSisAla.add(jmiKumulCheck);
    jSisAla.add(jmiSkCheck);
    jSisAla.add(jmiPokCheck);
    jSisAla.add(jmiBG);
    jSisAla.add(jmiValute);
    jSisAla.add(jmiCSK);
    jSisAla.add(jmiGKSK);
    
    jmOsnovni.add(jmiKnjigeUI);
    jmOsnovni.add(jmiKamate);
    jmOsnovni.add(jmiVrshemek);
    jmOsnovni.add(jmiShemek);
    jmOsnovni.add(jmiKolone);
    jmOsnovni.add(jmiShemeveze);
    jmOsnovni.add(jmiDefPDV);
    jmUpiti.add(jmiZbirPreg);
    jmUpiti.add(jmiZbirDugPotr);
    jmUpiti.add(jmiPojPreg);
    jmUpiti.add(jmiUspPreg);
    jmUpiti.addSeparator();
    jmUpiti.add(jmiKartice);
    jmIzvj.add(jmiZbirPregPer);
    jmIzvj.add(jmiDugPotr);
    jmIzvj.add(jmiUraIra);
    jmIzvj.add(jmiPDV2);
    jmIzvj.add(jmiPDV);
    jmIzvj.add(jmiKam);
    jmIzvj.add(jmiVirmaniSK);
    jmIzvj.add(jmiZbirPregPerVal);
    jmIzvj.addSeparator();
    jmIzvj.add(jmiSKReportList);
    
//    jmIzvj.add(jmiKupDobPer);
    jmKnj.add(jmTemeljGK);
    setToolMenu(jSisAla);
    setRaJMenuBar(jmb);

    jmiKnjigeUI.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKnjigeUI_actionPerformed();
      }
    });
    jmiShemek.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiShemek_actionPerformed();
      }
    });
    jmiVrshemek.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVrshemek_actionPerformed();
      }
    });
    jmiKamate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKamate_actionPerformed();
      }
    });
    jmiKolone.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKolone_actionPerformed();
      }
    });
    jmiShemeveze.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiShemeveze_actionPerformed();
      }
    });
    jmiRacuni.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRacuni_actionPerformed();
      }
    });
    jmiUplate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiUplate_actionPerformed();
      }
    });
    jmiKO.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKO_actionPerformed();
      }
    });
//    jmiPokriv.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        frmPokrivanje fp = (frmPokrivanje) showFrame("hr.restart.sk.frmPokrivanje", 15, "Pokrivanje", false);
//        fp.pres.showPreselect(fp, "Pokrivanje");
//      }
//    });
    jmiKD.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKD_actionPerformed();
      }
    });
    jmiPojPreg.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPojPreg_actionPerformed();
      }
    });
    jmiZbirPreg.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZbirPreg_actionPerformed();
      }
    });
    jmiZbirDugPotr.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZbirDugPotr_actionPerformed();
      }
    });
    /*jmiKumulCheck.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKumulCheck_actionPerformed();
      }
    });*/
    jmiSkCheck.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSkCheck_actionPerformed();
      }
    });
    jmiPokCheck.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPokCheck_actionPerformed();
      }
    });
    jmiAutoCover.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiAutoCover_actionPerformed();
      }
    });
    jmiAutoPok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiAutoPok_actionPerformed();
      }
    });
    jmiPregled.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPregled_actionPerformed();
      }
    });
    jmiNadzornaKnjiga.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiNadzornaKnjiga_actionPerformed();
      }
    });
    jmiImport.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiImport_actionPerformed();
      }
    });
    jmiUraIra.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiUraIra_actionPerformed();
      }
    });
    jmiKam.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKam_actionPerformed();
      }
    });
    jmiSKReportList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSKReportList_actionPerformed();
      }
    });
    jmiVirmaniSK.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	jmiVirmaniSK_actionPerformed();
          }
        });
    jmiUspPreg.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiUspPreg_actionPerformed();
      }
    });
    jmiBG.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBG_actionPerformed();
      }
    });
    jmiDefPDV.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiDefPDV_actionPerformed();
      }
    });
    jmiPDV.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPDV_actionPerformed();
      }
    });
    jmiPDV2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPDV2_actionPerformed();
      }
    });
    jmTemeljGK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiTemeljGK_actionPerformed();
      }
    });
    jmiDugPotr.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiDugPotr_actionPerformed();
      }
    });

    jmiZbirPregPer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZbirPregPer_actionPerformed();
      }
    });
    
    jmiZbirPregPerVal.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZbirPregPerVal_actionPerformed();
      }
    });

    jmiValute.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiValute_actionPerformed();
      }
    });
    jmiCSK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiCSK_actionPerformed();        
      }
    });
    jmiGKSK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiGKSK_actionPerformed();        
      }
    });
    jmiKartice.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKartice_actionPerformed();        
      }
    });
  }
  
  public void jmiVirmaniSK_actionPerformed() {
	 showFrame("hr.restart.sk.VirmaniSK",jmiVirmaniSK.getText());
  }
  public void jmiKnjigeUI_actionPerformed() {
    showFrame("hr.restart.sk.frmKnjigeUI",jmiKnjigeUI.getText());
  }
  public void jmiShemek_actionPerformed() {
    showFrame("hr.restart.sk.frmShemeKontaSk",15,jmiShemek.getText());
  }
  public void jmiVrshemek_actionPerformed() {
    showFrame("hr.restart.sk.frmVrskSk",15,jmiVrshemek.getText());
  }
  public void jmiKamate_actionPerformed() {
    showFrame("hr.restart.sk.frmKamate",15,jmiKamate.getText());
  }
  public void jmiKolone_actionPerformed() {
    showFrame("hr.restart.sk.frmKoloneknjUI",15,jmiKolone.getText());
  }
  public void jmiShemeveze_actionPerformed() {
    showFrame("hr.restart.sk.frmShemevezeUI",15,jmiShemeveze.getText());
  }
  public void jmiRacuni_actionPerformed() {
    hr.restart.sk.frmSalKon fsk = (frmSalKon) showFrame("hr.restart.sk.frmSalKon", 15, "Ra\u010Dun salda konti", false);
    fsk.pres.showPreselect(fsk, "Ra\u010Dun salda konti");
  }
  public void jmiUplate_actionPerformed() {
    frmNalozi.getFrmNalozi();
    PreSelect.showPreselect("hr.restart.gk.presIzvodi","hr.restart.gk.frmIzvodi","Izvodi");
  }
  public void jmiKO_actionPerformed() {
    hr.restart.sk.frmSalKonOK fsko = (frmSalKonOK) showFrame("hr.restart.sk.frmSalKonOK", 15, "Obavijesti knjiženja", false);
    fsko.pres.showPreselect(fsko, "Obavijesti knjiženja");
  }
  public void jmiKD_actionPerformed() {
    frmIzborStavki fis = (frmIzborStavki) showFrame("hr.restart.sk.frmIzborStavki", 15, "Potvr\u0111ivanje dokumenata", false);
    fis.pres.showPreselect(fis, "Potvr\u0111ivanje dokumenata");
  }
  public void jmiPojPreg_actionPerformed() {
    frmKartica fk = (frmKartica) showFrame("hr.restart.sk.frmKartica", 15, "Kartica", false);
    fk.pres.showPreselect(fk, "Kartica kupca / dobavljaèa");
  }
  public void jmiZbirPreg_actionPerformed() {
    frmZbirno fz = (frmZbirno) showFrame("hr.restart.sk.frmZbirno", 15, "Zbirni pregled", false);
    fz.pres.showPreselect(fz, "Zbirni pregled za godinu");
  }
  public void jmiZbirDugPotr_actionPerformed() {
    frmDugPot fdp = (frmDugPot) showFrame("hr.restart.sk.frmDugPot", 15, "Zbirni pregled", false);
    fdp.pres.showPreselect(fdp, "Zbirni pregled dugovanja / potraživanja");
  }
  
  public void jmiKartice_actionPerformed() {
    frmKarticaDev fkd = (frmKarticaDev) showFrame("hr.restart.sk.frmKarticaDev", 15, "Devizna kartica", false);
    fkd.pres.showPreselect(fkd, "Devizna kartica"); 
  }
  /*public void jmiKumulCheck_actionPerformed() {
    raSaldaKonti.checkKumulativ();
  }*/
  public void jmiSkCheck_actionPerformed() {
    raSaldaKonti.checkSkstavkeVrdokonto();
  }
  public void jmiPokCheck_actionPerformed() {
    raSaldaKonti.checkPokriveno();
  }
  public void jmiAutoCover_actionPerformed() {
    showFrame("hr.restart.sk.frmPokrivanjeAuto", "Automatsko pokrivanje po broju i saldu");
  }
  public void jmiAutoPok_actionPerformed() {
    showFrame("hr.restart.sk.frmPokrivanjePoluAuto", "Poluautomatsko pokrivanje po saldu");
  }
  public void jmiPregled_actionPerformed() {
    frmPregledArhive fpa = (frmPregledArhive) showFrame("hr.restart.sk.frmPregledArhive", 15, "Pregled arhive", false);
    fpa.pres.showPreselect(fpa, "Pregled arhive");
  }
  public void jmiNadzornaKnjiga_actionPerformed() {
    PreSelect.showPreselect("hr.restart.sk.presNadzornaKnjiga","hr.restart.sk.frmNadzornaKnjiga","Nadzorna knjiga uvoza");
  }
  
  public void jmiImport_actionPerformed() {
    raImportRac.show();
  }
  
  public void jmiUraIra_actionPerformed() {
    showFrame("hr.restart.sk.raIspisUraIra", "Ispis knjiga URA/IRA");
  }
  public void jmiKam_actionPerformed() {
    if (hr.restart.util.raLoader.isLoaderLoaded("hr.restart.sk.raObrKamata")) {
      if (raObrKamata.getInstance().isBusy()) return;
      if (raObrKamata.getInstance().isStandAlone()) {
        if (raObrKamata.getInstance().isShowing())
          raObrKamata.getInstance().hide();
        raObrKamata.getInstance().setStandAlone(false);
      }
    }
    showFrame("hr.restart.sk.raObrKamata", "Obra\u010Dun kamata");
  }
  public void jmiSKReportList_actionPerformed() {
    showFrame("hr.restart.sk.frmSKReportList",jmiSKReportList.getText());
  }
  public void jmiUspPreg_actionPerformed() {
    frmKupDob fkd = (frmKupDob) showFrame("hr.restart.sk.frmKupDob", 15, "Usporedni pregled kupaca-dobavlja\u010Da", false);
    fkd.pres.showPreselect(fkd, "Usporedni pregled kupaca-dobavlja\u010Da");
  }
  public void jmiBG_actionPerformed() {
    showFrame("hr.restart.sk.raBrisanjeGodine", "Brisanje godine");
  }
  public void jmiDefPDV_actionPerformed() {
    showFrame("hr.restart.sk.frmDefPDV", "Definicija obrasca PDV");
  }
  public void jmiPDV_actionPerformed() {
//    showFrame("hr.restart.sk.frmPDV2", "Obrazac PDV");
    showFrame("hr.restart.sk.frmPDV", "Obrazac PDV");
  }
  public void jmiPDV2_actionPerformed() {
    showFrame("hr.restart.sk.frmPDV2", "Obrasci za poreznu upravu");
  }
  public void jmiTemeljGK_actionPerformed() {
    showFrame("hr.restart.sk.frmTemeljGK", "Temeljnica za GK");
  }
  public void jmiDugPotr_actionPerformed() {
    showFrame("hr.restart.sk.upOpenRac", "Dugovanja / potraživanja");
  }
  public void jmiZbirPregPer_actionPerformed() {
    showFrame("hr.restart.sk.upZbirnoPeriod", "Zbirni pregled za period");
  }
  
  public void jmiZbirPregPerVal_actionPerformed() {
    showFrame("hr.restart.sk.upZbirnoPeriodVal", "Zbirni pregled za period u valuti");
  }
  
  public void jmiValute_actionPerformed() {
    raSaldaKonti.fixValute();
  }
  public void jmiCSK_actionPerformed() {
    raSaldaKonti.fixMisingCSK();
    JOptionPane.showMessageDialog(null, "Popravak završen!",
      "Poruka", JOptionPane.INFORMATION_MESSAGE);
  }
  
  public void jmiGKSK_actionPerformed() {
    raSaldaKonti.checkSkConsistency();
  }

//  private void matchThemAll() {
//    QueryDataSet allOfThem = hr.restart.baza.dM.getDataModule().getSkstavkeCover();
//
//  }
}

