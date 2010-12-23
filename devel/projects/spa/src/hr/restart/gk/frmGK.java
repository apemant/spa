/****license*****************************************************************
**   file: frmGK.java
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
package hr.restart.gk;

import hr.restart.sisfun.frmParam;
import hr.restart.util.PreSelect;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class frmGK extends startFrame {
  static frmGK _frmGK = null;
  preSelNalozi fPN = new preSelNalozi();
  JMenuBar jmb = new JMenuBar();
  JMenu jmOsnovni = new JMenu();
  JMenu jmKnjizenja = new JMenu();
  JMenu jmUpiti = new JMenu();
  JMenu jmIzvj = new JMenu();
  JMenu jmGodObrade = new JMenu();
  JMenu jmKontrole = new JMenu();
  JMenuItem jmiVrNaloga = new JMenuItem();
  JMenuItem jmiRepGK = new JMenuItem();
  JMenuItem jmiNalozi = new JMenuItem();
  JMenuItem jmiStanjeKonto = new JMenuItem();
  JMenuItem jmiBrutoBilanca = new JMenuItem();
  JMenuItem jmiKartGK = new JMenuItem();
  JMenuItem jmiKartGKVal = new JMenuItem();
  JMenuItem jmiBrBilancaPer = new JMenuItem();
  JMenuItem jmiZakList = new JMenuItem();
  JMenuItem jmiFinDnev = new JMenuItem();
  JMenuItem jmiBrBilancaPerVal = new JMenuItem();
  JMenuItem jmiGenericGK = new JMenuItem();
  JMenuItem jmiExcelGK = new JMenuItem();
  JMenuItem jmiRepGKUpit = new JMenuItem();
  JMenuItem jmiIspKartica = new JMenuItem();
  JMenuItem jmiZakKlasa = new JMenuItem();
  JMenuItem jmiGodObr = new JMenuItem();
  JMenuItem jmiIzvodi = new JMenuItem();
  JMenuItem jmiSKRac = new JMenuItem();
  JMenuItem jmiSKRacRaz = new JMenuItem();
  JMenuItem jmiRobno = new JMenuItem();
  JMenuItem jmiBlag = new JMenuItem();
  JMenuItem jmiPN = new JMenuItem();
  JMenuItem jmiPlace = new JMenuItem();
  JMenuItem jmiOs = new JMenuItem();
  JMenuItem jmiCheckKumul = new JMenuItem();
  JMenuItem jmiCheckNalog = new JMenuItem();
  JMenuItem jmiCheckIzvod = new JMenuItem();
  JMenuItem jmiCheckZiroPar = new JMenuItem();
  JMenuItem jmiChangeURIRVRDOK = new JMenuItem();
  JMenuItem jmiChangeURIRVRDOK_proknjizeno = new JMenuItem();
  JMenuItem jmiFixCGKSTAVKE = new JMenuItem();
  JMenuItem jmiFixCNALOGA = new JMenuItem();
  JMenuItem jmiFixGODMJ = new JMenuItem();
  public frmGK() {
    try {
      jbInit();
      _frmGK=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.setTitle("Glavna knjiga");
    jmb.setToolTipText("Glavna knjiga");
    jmOsnovni.setText("Osnovni podaci");
    jmKnjizenja.setText("Knji\u017Eenja");
    jmUpiti.setText("Upiti");
    jmIzvj.setText("Izvje\u0161taji");
    jmGodObrade.setText("Godi\u0161nja obrada");
    jmiRepGK.setText("Definicije izvještaja");
    jmiRepGK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRepGK_actionPerformed(e);
      }
    });
    jmiVrNaloga.setText("Vrste naloga-temeljnica");
    jmiVrNaloga.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVrNaloga_actionPerformed(e);
      }
    });
    jmiNalozi.setText("Nalozi - Temeljnice");
    jmiNalozi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiNalozi_actionPerformed(e);
      }
    });
    jmiStanjeKonto.setText("Stanje na kontu");
    jmiStanjeKonto.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiStanjeKonto_actionPerformed(e);
      }
    });
    jmiBrutoBilanca.setText("Bruto bilanca");
    jmiBrutoBilanca.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBrutoBilanca_actionPerformed(e);
      }
    });
    jmiKartGK.setText("Kartice glavne knjige");
    jmiKartGK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKartGK_actionPerformed(e);
      }
    });
    jmiKartGKVal.setText("Kartice glavne knjige u valuti");
    jmiKartGKVal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKartGKVal_actionPerformed(e);
      }
    });
    jmiBrBilancaPer.setText("Bruto bilanca za period");
    jmiBrBilancaPer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBrBilancaPer_actionPerformed(e);
      }
    });
    jmiZakList.setText("Zaklju\u010Dni list");
    jmiZakList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZakList_actionPerformed(e);
      }
    });
    jmiFinDnev.setText("Financijski dnevnik");
    jmiFinDnev.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiFinDnev_actionPerformed(e);
      }
    });
    jmiBrBilancaPerVal.setText("Bruto bilanca za period u valuti");
    jmiBrBilancaPerVal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBrBilancaPerVal_actionPerformed(e);
      }
    });
    jmiRepGKUpit.setText("Definirani izvjestaji");
    jmiRepGKUpit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRepGKUpit_actionPerformed(e);
      }
    });    
    jmiGenericGK.setText("Ostali izvjestaji");
    jmiGenericGK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiGenericGK_actionPerformed(e);
      }
    });
    jmiExcelGK.setText("Excel izvjestaji");
    jmiExcelGK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiExcelGK_actionPerformed(e);
      }
    });
    jmiIspKartica.setText("Ispis svih kartica");
    jmiIspKartica.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIspKartica_actionPerformed(e);
      }
    });
    jmiZakKlasa.setText("Zaklju\u010Dak klasa");
    jmiZakKlasa.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZakKlasa_actionPerformed(e);
      }
    });
    jmiGodObr.setText("Prijelaz u novu godinu");
    jmiGodObr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiGodObr_actionPerformed(e);
      }
    });
    jmiIzvodi.setText("Izvodi");
    jmiIzvodi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIzvodi_actionPerformed(e);
      }
    });
    jmiSKRac.setText("SK Raèuni");
    jmiSKRac.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSKRac_actionPerformed(e);
      }
    });
    jmiSKRacRaz.setText("SK razlike u saldu");
    jmiSKRacRaz.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSKRacRaz_actionPerformed(e);
      }
    });
    jmiRobno.setText("Robno - materijalno");
    jmiRobno.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRobno_actionPerformed(e);
      }
    });
    jmiBlag.setText("Blagajna");
    jmiBlag.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBlag_actionPerformed(e);
      }
    });
    jmiPN.setText("Putni nalozi");
    jmiPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPN_actionPerformed(e);
      }
    });
    jmiPlace.setText("Pla\u0107e");
    jmiPlace.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPlace_actionPerformed(e);
      }
    });
    jmiOs.setText("Osnovna sredstva");
    jmiOs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiOs_actionPerformed(e);
      }
    });
    jmKontrole.setText("Kontrola i ispravak podataka");
    jmiCheckKumul.setText("Provjera kumulativa");
    jmiCheckKumul.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiCheckKumul_actionPerformed(e);
      }
    });
    jmiCheckNalog.setText("Provjera naloga");
    jmiCheckNalog.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiCheckNalog_actionPerformed(e);
      }
    });
    jmiCheckIzvod.setText("Provjera izvoda");
    jmiCheckIzvod.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiCheckIzvod_actionPerformed(e);
      }
    });


    jmiCheckZiroPar.setText("Kontrola i ispravak žiro ra\u010Duna partnera");
    jmiCheckZiroPar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiCheckZiroPar_actionPerformed(e);
      }
    });
    jmiChangeURIRVRDOK.setText("Ispravak vrste dokumenta po parametru temURNIRN (neproknjiženo)");
    jmiChangeURIRVRDOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiChangeURIRVRDOK_actionPerformed(e);
      }
    });
    jmiChangeURIRVRDOK_proknjizeno.setText("Ispravak vrste dokumenta po parametru temURNIRN (proknjiženo)");
    jmiChangeURIRVRDOK_proknjizeno.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiChangeURIRVRDOK_proknjizeno_actionPerformed(e);
      }
    });
    jmiFixCGKSTAVKE.setText("Popravak veze izmedju stavke GK i stavke SK");
    jmiFixCGKSTAVKE.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiFixCGKSTAVKE_actionPerformed(e);
      }
    });
    jmiFixCNALOGA.setText("Popravak CNALOGA na gkstavkama");
    jmiFixCNALOGA.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiFixCNALOGA_actionPerformed(e);
      }
    });
    jmiFixGODMJ.setText("Popravak GODine i MJeseca kod neproknjiženih stavaka");
    jmiFixGODMJ.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiFixGODMJ_actionPerformed(e);
      }
    });
    jmShemeK.setText("Sheme knjiženja");
    jmiSh_sk.setText("Salda konti");
    jmiSh_sk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSh_sk_actionPerformed(e);
      }
    });
    jmiSh_robno.setText("Robno materijalno poslovanje");
    jmiSh_robno.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSh_robno_actionPerformed(e);
      }
    });
    jmiSh_blpn.setText("Blagajna i putni nalozi");
    jmiSh_blpn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSh_blpn_actionPerformed(e);
      }
    });
    jmiSh_pl.setText("Pla\u0107e");
    jmiSh_pl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSh_pl_actionPerformed(e);
      }
    });
    jmiSh_vrste.setText("Vrste shema");
    jmiSh_vrste.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSh_vrste_actionPerformed(e);
      }
    });
    jmiSh_rac.setText("Obrada raèuna");
    jmiSh_rac.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSh_robnoOJ_actionPerformed(e);
      }
    });
    jmiSh_mp.setText("Maloprodaja - skladišni dokumenti");
    jmiSh_mp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSh_mp_actionPerformed(e);
      }
    });

    jmiSh_mpOJ.setText("Maloprodaja - financijski dokumenti");
    jmiSh_mpOJ.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSh_mpOJ_actionPerformed(e);
      }
    });


    jmb.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmb.add(jmOsnovni);
    jmb.add(jmKnjizenja);
    jmb.add(jmUpiti);
    jmb.add(jmIzvj);
    jmb.add(jmGodObrade);
    jmOsnovni.add(jmiVrNaloga);
    jmOsnovni.add(jmShemeK);
    jmOsnovni.addSeparator();
    jmOsnovni.add(jmiRepGK);
    jmKnjizenja.add(jmiNalozi);
    jmKnjizenja.add(jmiIzvodi);
    jmKnjizenja.add(jmiSKRac);
    if ("D".equalsIgnoreCase(frmParam.getParam("gk", "skRaz", "N", 
    		"Opcija za knjiženje razlika u saldu SK dokumenata (D,N)")))
    				jmKnjizenja.add(jmiSKRacRaz);
    jmKnjizenja.add(jmiRobno);
    jmKnjizenja.add(jmiBlag);
    jmKnjizenja.add(jmiPN);
    jmKnjizenja.add(jmiPlace);
    jmKnjizenja.add(jmiOs);
    jmUpiti.add(jmiStanjeKonto);
    jmUpiti.add(jmiBrutoBilanca);
    jmUpiti.add(jmiKartGK);
    jmUpiti.add(jmiKartGKVal);
//    jmIzvj.add(jmiBrBilancaPer);
    jmIzvj.add(jmiZakList);
    jmIzvj.add(jmiFinDnev);
//    jmIzvj.add(jmiBrBilancaPerVal);
    jmIzvj.addSeparator();
    jmIzvj.add(jmiRepGKUpit);
    jmIzvj.add(jmiGenericGK);
    jmIzvj.add(jmiExcelGK);
    jmGodObrade.add(jmiIspKartica);
    jmGodObrade.add(jmiZakKlasa);
    jmGodObrade.add(jmiGodObr);
    setRaJMenuBar(jmb);
    jmKontrole.add(jmiCheckKumul);
    jmKontrole.add(jmiCheckNalog);
    jmKontrole.add(jmiCheckIzvod);
    jmKontrole.add(jmiCheckZiroPar);
    jmKontrole.add(jmiChangeURIRVRDOK);
    jmKontrole.add(jmiChangeURIRVRDOK_proknjizeno);
    jmKontrole.add(jmiFixCGKSTAVKE);
    jmKontrole.add(jmiFixCNALOGA);
    jmKontrole.add(jmiFixGODMJ);
    jmShemeK.add(jmiSh_sk);
    jmShemeK.add(jmiSh_robno);
    jmShemeK.add(jmiSh_rac);
    jmShemeK.add(jmiSh_mp);
    jmShemeK.add(jmiSh_mpOJ);
    jmShemeK.add(jmiSh_blpn);
    jmShemeK.add(jmiSh_pl);
    jmShemeK.addSeparator();
    jmShemeK.add(jmiSh_vrste);
    setToolMenu(jmKontrole);
  }

  void jmiNalozi_actionPerformed(ActionEvent e) {
/*
    preSelNalozi prsNal = (preSelNalozi)raLoader.load("hr.restart.gk.preSelNalozi");
    frmNalozi fNal = (frmNalozi)raLoader.load("hr.restart.gk.frmNalozi");
    fNal.psNal=prsNal;
    prsNal.showPreselect(fNal.raMaster,"Nalozi - predselekcija");
*/
    frmNalozi fN = frmNalozi.getFrmNalozi();
    openNalog();
    fPN.showPreselect(fN,"Nalozi - predselekcija");
//    PreSelect.showPreselect("hr.restart.gk.preSelNalozi","hr.restart.gk.frmNalozi","Nalozi - predselekcija");
  }
  void jmiRepGK_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmRepgk",jmiRepGK.getText());
  }
  void jmiVrNaloga_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmVrNaloga","Vrste naloga");
  }

  void jmiStanjeKonto_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmStanjeKonto","Stanje na kontu");
  }

  void jmiKartGK_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKarticeGK","Kartice glavne knjige");
  }

  void jmiBrutoBilanca_actionPerformed(ActionEvent e) {
//    showFrame("hr.restart.gk.frmBrutoBilanca","Bruto bilanca");
    showFrame("hr.restart.gk.frmBrBilAll","Bruto bilanca");
  }

  void jmiKartGKVal_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKarticeGKVal","Kartice glavne knjige u valuti");
  }

  void jmiBrBilancaPer_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmBrBilancaPer","Bruto bilanca za period");
  }

  void jmiZakList_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmZakList","Zaklju\u010Dni list");
  }

  void jmiFinDnev_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmFinDnev","Financijski dnevnik");
  }

  void jmiBrBilancaPerVal_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmBrBilancaPerVal","Bruto bilanca za period u valuti");
  }
  
  void jmiGenericGK_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmGenericGK",jmiGenericGK.getText());
  }
  
  void jmiExcelGK_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmExcelGK",jmiExcelGK.getText());
  }
  
  void jmiRepGKUpit_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmRepgkUpit",jmiRepGKUpit.getText());
  }

  void jmiIzvodi_actionPerformed(ActionEvent e) {
    frmNalozi.getFrmNalozi();
    openIzvod();
    PreSelect.showPreselect("hr.restart.gk.presIzvodi","hr.restart.gk.frmIzvodi","Izvodi");
  }

  void jmiSKRac_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKnjSKRac","Knjiženje ra\u010Duna iz salda konti");
  }
  
  void jmiSKRacRaz_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKnjSkDiff","Knjiženje razlika raèuna i uplata SK");
  }

  void jmiRobno_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKnjRobno","Knjiženje iz robnog knjigovodstva");
  }

  void jmiBlag_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKnjBlag","Knjiženje blagajne");
  }

  void jmiPN_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKnjPN","Knjiženje putnih naloga");
  }

  void jmiPlace_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKnjPlace","Knjiženje pla\u0107a");
  }

  void jmiOs_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmKnjOS","Knjiženje osnovnih sredstava");
  }


  void jmiCheckKumul_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmGKKCheckKumul","Provjera kumulativa");
  }
  void jmiCheckNalog_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmGKKCheckNalog","Provjera naloga");
  }
  void jmiCheckIzvod_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmGKKCheckIzvod","Provjera izvoda");
  }

  void jmiCheckZiroPar_actionPerformed(ActionEvent e) {
    new Thread() {
      public void run() {
        raGKKontrole.checkZiroPar();
      }
    }.start();
  }
  
  void jmiChangeURIRVRDOK_actionPerformed(ActionEvent e) {
    raGKKontrole.changeVRDOKfrmParam(false);
  }
  
  void jmiChangeURIRVRDOK_proknjizeno_actionPerformed(ActionEvent e) {
    raGKKontrole.changeVRDOKfrmParam(true);
  }
  void jmiFixCGKSTAVKE_actionPerformed(ActionEvent e) {
    raGKKontrole.dodajRBSnaCGKSTAVKE();
  }
  
  void jmiFixCNALOGA_actionPerformed(ActionEvent e) {
    raKnjizenje.fixMisingCNALOGA();
    JOptionPane.showMessageDialog(null, "Popravak završen!",
      "Poruka", JOptionPane.INFORMATION_MESSAGE);    
  }
  
  void jmiFixGODMJ_actionPerformed(ActionEvent e) {
    raGKKontrole.fillGODMJinGKStavkeRad();
  }
  void jmiIspKartica_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmIspKartica","Ispis svih kartica");
  }

// nema drugih knjizenja ako je otvoren ekran za temeljnicu ili izvode
  private boolean nalogShow = false;
  private boolean izvodShow = false;
  private JMenu jmShemeK = new JMenu();
  private JMenuItem jmiSh_sk = new JMenuItem();
  private JMenuItem jmiSh_robno = new JMenuItem();
  private JMenuItem jmiSh_blpn = new JMenuItem();
  private JMenuItem jmiSh_pl = new JMenuItem();
  private JMenuItem jmiSh_vrste = new JMenuItem();
  private JMenuItem jmiSh_rac = new JMenuItem();
  private JMenuItem jmiSh_mp = new JMenuItem();
  private JMenuItem jmiSh_mpOJ = new JMenuItem();
  void openNalog() {
    nalogShow = true;
    handleMenus();
  }
  void closeNalog() {
    nalogShow = false;
    handleMenus();
  }
  public static frmGK getFrmGK() {
    if (_frmGK==null) new frmGK();
    return _frmGK;
  }
  void openIzvod() {
    izvodShow = true;
    handleMenus();
  }
  void closeIzvod() {
    izvodShow = false;
    handleMenus();
  }

  void handleMenus() {
    boolean bothShow = !(izvodShow||nalogShow);
    jmiNalozi.setEnabled(!izvodShow);
    jmiIzvodi.setEnabled(!nalogShow);
    jmiSKRac.setEnabled(bothShow);
    jmiRobno.setEnabled(bothShow);
    jmiBlag.setEnabled(bothShow);
    jmiPN.setEnabled(bothShow);
    jmiPlace.setEnabled(bothShow);
    jmiOs.setEnabled(bothShow);
  }

  void jmiSh_sk_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.sk.frmShemeKontaSk",jmiSh_sk.getText());
  }

  void jmiSh_robno_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.robno.frmShemeKontaRobno",jmiSh_robno.getText());
  }

  void jmiSh_pl_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmShemeKontaPL",jmiSh_blpn.getText());
  }

  void jmiSh_blpn_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.frmShemeKontaBLPN",jmiSh_blpn.getText());
  }

  void jmiSh_vrste_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.sisfun.frmVrsteShemaKonti",jmiSh_vrste.getText());
  }

  void jmiSh_robnoOJ_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.rac.frmShemeKontaRac",jmiSh_rac.getText());
  }

  void jmiSh_mp_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.mp.frmShemeKontaMp",jmiSh_mp.getText());
  }

  void jmiSh_mpOJ_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.mp.frmShemeKontaMpOJ",jmiSh_mpOJ.getText());
  }

  void jmiZakKlasa_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmZaKlasa",jmiZakKlasa.getText());
  }

  void jmiGodObr_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.gk.frmGodObr",jmiGodObr.getText());
  }

}