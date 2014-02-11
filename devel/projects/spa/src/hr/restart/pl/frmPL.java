/****license*****************************************************************
**   file: frmPL.java
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
package hr.restart.pl;

import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class frmPL extends startFrame {
  JMenuBar jMenuBar = new JMenuBar();
  JMenu jmOsnovni = new JMenu();
  JMenuItem jmiRadMj = new JMenuItem();
  JMenuItem jmiVrodn = new JMenuItem();
  JMenuItem jmiZupanije = new JMenuItem();
  JMenuItem jmiOpcine = new JMenuItem();
  JMenuItem jmiPovjerioci = new JMenuItem();
  JMenuItem jmiBanke = new JMenuItem();
  JMenuItem jmiIsplMj = new JMenuItem();
  JMenuItem jmiOdbici = new JMenuItem();
  JMenuItem jmiVrsteOdb = new JMenuItem();
  JMenu jmPodaciObr = new JMenu();
  JMenuItem jmiOrgpl = new JMenuItem();
  JMenuItem jmiRadnicipl = new JMenuItem();
  JMenuItem jmiFondSati = new JMenuItem();
  JMenuItem jmiVriBoda = new JMenuItem();
  JMenu jmDefinicije = new JMenu();
  JMenuItem jmiNacobr = new JMenuItem();
  JMenuItem jmiSume = new JMenuItem();
  JMenuItem jmiOsnovice = new JMenuItem();
  JMenuItem jmiIzvjDef = new JMenuItem();
  JMenuItem jmiZnacRad = new JMenuItem();
  JMenuItem jmiPlSifre = new JMenuItem();
  JMenuItem jmiIniPrim = new JMenuItem();
  JMenu jmObrada = new JMenu();
  JMenuItem jmiIniciranje = new JMenuItem();
  JMenuItem jmiPrimanja = new JMenuItem();
  JMenu jmIzvObr = new JMenu();
  JMenuItem jmiRekPrim = new JMenuItem();
  JMenuItem jmiRekObr = new JMenuItem();
  JMenuItem jmiSpecKred = new JMenuItem();
  JMenuItem jmiRekDopPor = new JMenuItem();
  JMenu jmObrasciObr = new JMenu();
  JMenuItem jmiRS = new JMenuItem();
  JMenuItem jmiJOPPD = new JMenuItem();
  JMenuItem jmiVirmani = new JMenuItem();
  JMenuItem jmiBankSpec = new JMenuItem();
  JMenuItem jmiBankSpecA = new JMenuItem();
  JMenu jmArhiva = new JMenu();
  JMenuItem jmiArhiviranje = new JMenuItem();
  JMenuItem jmiDohArh = new JMenuItem();
  JMenuItem jmiDelArh = new JMenuItem();
  JMenuItem jmiDatIspl = new JMenuItem();
  JMenu jmIzvArh = new JMenu();
  JMenuItem jmiIspList = new JMenuItem();
  JMenuItem jmiNeobKred = new JMenuItem();
  JMenuItem jmiPrisRad = new JMenuItem();
  JMenu jmObracun = new JMenu();
  JMenuItem jmiObrPrimanja = new JMenuItem();
  JMenuItem jmiObrPor = new JMenuItem();
//  JMenuItem jmiObrKred = new JMenuItem();
  JMenuItem jmiObrOs = new JMenuItem();
  JMenuItem jmiVrstePrim = new JMenuItem();
  JMenuItem jmiParametriPl = new JMenuItem();
  JMenuItem jmiIspListA   = new JMenuItem();
  JMenuItem jmiRekPrimA   = new JMenuItem();
  JMenuItem jmiRekDopPorA = new JMenuItem();
  JMenuItem jmiRekObrA    = new JMenuItem();
  JMenuItem jmiSpecKredA  = new JMenuItem();
  private JMenu jmObrasciArh = new JMenu();
  private JMenuItem jmiID = new JMenuItem();
  private JMenuItem jmiER1 = new JMenuItem();
  private JMenuItem jmiRSArh = new JMenuItem();
  private JMenuItem jmiDNR = new JMenuItem();
  private JMenuItem jmiGeneric = new JMenuItem();
  private JMenuItem jmiGenericArh = new JMenuItem();
  private JMenuItem jmiM4 = new JMenuItem();
  private JMenuItem jmiSPL = new JMenuItem();
  private JMenuItem jmiIPP = new JMenuItem();
  private JMenuItem jmiIDObr = new JMenuItem();
  private JMenu jmSys = new JMenu("Sistemski alati");
  private JMenu jmAddDefault = new JMenu("Dodavanje obaveznih podataka");
  private JMenuItem jmiAddDefIzvjestaji = new JMenuItem("Izvještaji i grupe izvještaja");
  private JMenuItem jmiPK = new JMenuItem();
  private JMenuItem jmiMjIzv = new JMenuItem();
  private JMenuItem jmiMjIzvArh = new JMenuItem();
  private JMenuItem jmiVirmaniArh = new JMenuItem();
//  JMenuItem jmiNeobKredA  = new JMenuItem();
  private JMenuItem jmiKalkulator = new JMenuItem();
  private JMenuItem jmiEvidencija = new JMenuItem();
  public frmPL() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jmOsnovni.setText("Osnovni podaci");
    jmiRadMj.setText("Radna mjesta");
    jmiRadMj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRadMj_actionPerformed(e);
      }
    });
    jmiVrodn.setText("Vrste radnog odnosa");
    jmiVrodn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVrodn_actionPerformed(e);
      }
    });
    jmiZupanije.setText("Županije");
    jmiZupanije.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZupanije_actionPerformed(e);
      }
    });
    jmiOpcine.setText("Op\u0107ine");
    jmiOpcine.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiOpcine_actionPerformed(e);
      }
    });
    jmiPovjerioci.setText("Povjerioci - virmani");
    jmiPovjerioci.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPovjerioci_actionPerformed(e);
      }
    });
    jmiBanke.setText("Banke");
    jmiBanke.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBanke_actionPerformed(e);
      }
    });
    jmiIsplMj.setText("Isplatna mjesta");
    jmiIsplMj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIsplMj_actionPerformed(e);
      }
    });
    jmiOdbici.setText("Odbici");
    jmiOdbici.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiOdbici_actionPerformed(e);
      }
    });
    jmiVrsteOdb.setText("Vrste odbitaka");
    jmiVrsteOdb.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVrsteOdb_actionPerformed(e);
      }
    });
    jmPodaciObr.setText("Podaci za obra\u010Dun");
    jmiOrgpl.setText("Organizacijske jedinice");
    jmiOrgpl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiOrgpl_actionPerformed(e);
      }
    });
    jmiRadnicipl.setText("Radnici");
    jmiRadnicipl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRadnicipl_actionPerformed(e);
      }
    });
    jmiFondSati.setText("Fond sati");
    jmiFondSati.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiFondSati_actionPerformed(e);
      }
    });
    jmiVriBoda.setText("Vrijednost boda");
    jmiVriBoda.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVriBoda_actionPerformed(e);
      }
    });
    jmDefinicije.setText("Definicije");
    jmiNacobr.setText("Na\u010Dini obra\u010Duna");
    jmiNacobr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiNacobr_actionPerformed(e);
      }
    });
    jmiSume.setText("Sume");
    jmiSume.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSume_actionPerformed(e);
      }
    });
    jmiOsnovice.setText("Osnovice");
    jmiOsnovice.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiOsnovice_actionPerformed(e);
      }
    });
    jmiIzvjDef.setText("Izvještaji");
    jmiIzvjDef.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIzvjDef_actionPerformed(e);
      }
    });
    jmiZnacRad.setText("Dodatni podaci o djelatnicima");
    jmiZnacRad.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZnacRad_actionPerformed(e);
      }
    });
    jmiPlSifre.setText("Šifrarnici");
    jmiPlSifre.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPlSifre_actionPerformed(e);
      }
    });
    jmiIniPrim.setText("Inicijalna primanja");
    jmiIniPrim.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIniPrim_actionPerformed(e);
      }
    });
    jmObrada.setText("Obra\u010Dun");
    jmiIniciranje.setText("Iniciranje");
    jmiIniciranje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIniciranje_actionPerformed(e);
      }
    });
    jmiPrimanja.setText("Unos primanja");
    jmiPrimanja.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPrimanja_actionPerformed(e);
      }
    });
    jmIzvObr.setText("Izvještaji");
    jmiRekPrim.setText("Rekapitulacija primanja");
    jmiRekPrim.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRekPrim_actionPerformed(e);
      }
    });
    jmiRekPrimA.setText("Rekapitulacija primanja");
    jmiRekPrimA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRekPrimA_actionPerformed(e);
      }
    });

    jmiRekObr.setText("Rekapitulacija obra\u010Duna");
    jmiRekObr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRekObr_actionPerformed(e);
      }
    });
    jmiRekObrA.setText("Rekapitulacija obra\u010Duna");
    jmiRekObrA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRekObrA_actionPerformed(e);
      }
    });
    jmiSpecKred.setText("Specifikacije kredita");
    jmiSpecKred.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSpecKred_actionPerformed(e);
      }
    });
    jmiSpecKredA.setText("Specifikacije kredita");
    jmiSpecKredA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSpecKredA_actionPerformed(e);
      }
    });
    jmiRekDopPor.setText("Rekapitulacija doprinosa i poreza");
    jmiRekDopPor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRekDopPor_actionPerformed(e);
      }
    });
    jmiRekDopPorA.setText("Rekapitulacija doprinosa i poreza");
    jmiRekDopPorA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRekDopPorA_actionPerformed(e);
      }
    });
    jmObrasciObr.setText("Obrasci");
    jmiRS.setText("R-S");
    jmiRS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRS_actionPerformed(e);
      }
    });
    jmiJOPPD.setText("Obrasci za e-poreznu");
    jmiJOPPD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiJOPPD_actionPerformed(e);
      }
    });
    jmiVirmani.setText("Virmani / diskete");
    jmiVirmani.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVirmani_actionPerformed(e);
      }
    });
    jmiBankSpec.setText("Diskete i specifikacije za banku");
    jmiBankSpec.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBankSpec_actionPerformed(e);
      }
    });
    jmiBankSpecA.setText("Diskete i specifikacije za banku");
    jmiBankSpecA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBankSpecA_actionPerformed(e);
      }
    });
    jmArhiva.setText("Arhiva");
    jmiArhiviranje.setText("Arhiviranje");
    jmiArhiviranje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiArhiviranje_actionPerformed(e);
      }
    });
    jmiDohArh.setText("Dohvat arhive");
    jmiDohArh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiDohArh_actionPerformed(e);
      }
    });
    jmiDelArh.setText("Brisanje arhive");
    jmiDelArh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiDelArh_actionPerformed(e);
      }
    });
    jmiDatIspl.setText("Promjena datuma isplate");
    jmiDatIspl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiDatIspl_actionPerformed(e);
      }
    });
    jmIzvArh.setText("Izvještaji");
    jmiIspList.setText("Isplatne liste");
    jmiIspList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIspList_actionPerformed(e);
      }
    });
    jmiIspListA.setText("Isplatne liste");
    jmiIspListA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIspListA_actionPerformed(e);
      }
    });
    jmiNeobKred.setText("Neobustavljeni krediti");
    jmiNeobKred.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiNeobKred_actionPerformed(e);
      }
    });
    jmiPrisRad.setText("Unos prisutnosti na radu");
    jmiPrisRad.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPrisRad_actionPerformed(e);
      }
    });
    jmObracun.setText("Obra\u010Dun");
    jmiObrPrimanja.setText("Primanja");
    jmiObrPrimanja.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiObrPrimanja_actionPerformed(e);
      }
    });
    jmiObrPor.setText("Poreza, doprinosa i kredita");
    jmiObrPor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiObrPor_actionPerformed(e);
      }
    });
//    jmiObrKred.setText("Kredita");
//    jmiObrKred.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmiObrKred_actionPerformed(e);
//      }
//    });
    jmiObrOs.setText("Osnovica");
    jmiObrOs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiObrOs_actionPerformed(e);
      }
    });
    jmiVrstePrim.setText("Vrste primanja");
    jmiVrstePrim.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVrstePrim_actionPerformed(e);
      }
    });
    jmiParametriPl.setText("Parametri");
    jmiParametriPl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiParametriPl_actionPerformed(e);
      }
    });
    jmObrasciArh.setText("Obrasci");
    jmiID.setText("ID");
    jmiID.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiID_actionPerformed(e);
      }
    });
    jmiER1.setText("Potvrda o pla\u0107i (ER-1)");
    jmiER1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiER1_actionPerformed(e);
      }
    });
    jmiRSArh.setText("R-S");
    jmiRSArh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRSArh_actionPerformed(e);
      }
    });
    jmiDNR.setText("DNR");
    jmiDNR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiDNR_actionPerformed(e);
      }
    });
    jmiGeneric.setText("Ostali izvještaji");
    jmiGeneric.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiGeneric_actionPerformed(e);
      }
    });
    jmiGenericArh.setText("Ostali izvještaji");
    jmiGenericArh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiGenericArh_actionPerformed(e);
      }
    });
    jmiM4.setText("M-4");
    jmiM4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiM4_actionPerformed(e);
      }
    });
    jmiSPL.setText("Statisti\u010Dki izvještaj (SPL)");
    jmiSPL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiSPL_actionPerformed(e);
      }
    });
    jmiIPP.setText("Izvješæe o posebnom porezu - IPP");
    jmiIPP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIPP_actionPerformed(e);
      }
    });
    jmiIDObr.setText("ID");
    jmiIDObr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIDObr_actionPerformed(e);
      }
    });
    jmiAddDefIzvjestaji.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        raIzvjestaji.addDefault();
        JOptionPane.showMessageDialog(frmPL.this,"Obavezni izvještaji i grupe izvještaja dodani!","Dodavanje obaveznih podataka",JOptionPane.INFORMATION_MESSAGE);
      }
    });

    jmiPK.setText("IP obrazac (Porezna kartica)");
    jmiPK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPK_actionPerformed(e);
      }
    });
    jmiMjIzv.setText("Mjeseèni izvještaj");
    jmiMjIzv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiMjIzv_actionPerformed(e);
      }
    });
    jmiMjIzvArh.setText("Mjeseèni izvještaj");
    jmiMjIzvArh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiMjIzvArh_actionPerformed(e);
      }
    });
    jmiVirmaniArh.setText("Virmani / diskete");
    jmiVirmaniArh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVirmaniArh_actionPerformed(e);
      }
    });
    jmiKalkulator.setText("Kalkulator bruto <-> neto");
    jmiKalkulator.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKalkulator_actionPerformed(e);
      }
    });
    jmiEvidencija.setText("Evidencija djelatnika");
    jmiEvidencija.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiEvidencija_actionPerformed(e);
      }
    });
    jMenuBar.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jMenuBar.add(jmOsnovni);
    jMenuBar.add(jmPodaciObr);
    jMenuBar.add(jmObrada);
    jMenuBar.add(jmArhiva);
    jmOsnovni.add(jmiPlSifre);
    jmOsnovni.add(jmiRadMj);
    jmOsnovni.add(jmiVrodn);
    jmOsnovni.add(jmiZupanije);
    jmOsnovni.add(jmiOpcine);
    jmOsnovni.add(jmiPovjerioci);
    jmOsnovni.add(jmiBanke);
    jmOsnovni.add(jmiIsplMj);
    jmOsnovni.add(jmiVrstePrim);
    jmOsnovni.add(jmiVrsteOdb);
    jmOsnovni.add(jmiOdbici);
    jmOsnovni.add(jmDefinicije);
    jmPodaciObr.add(jmiOrgpl);
    jmPodaciObr.add(jmiRadnicipl);
    jmPodaciObr.add(jmiFondSati);
    jmPodaciObr.add(jmiVriBoda);
    jmPodaciObr.add(jmiIniPrim);
    jmPodaciObr.addSeparator();
    jmPodaciObr.add(jmiParametriPl);
    jmDefinicije.add(jmiNacobr);
    jmDefinicije.add(jmiSume);
    jmDefinicije.add(jmiOsnovice);
    jmDefinicije.add(jmiIzvjDef);
    jmDefinicije.add(jmiZnacRad);
    jmObrada.add(jmiIniciranje);
    jmObrada.add(jmiPrimanja);
    jmObrada.add(jmiPrisRad);
    jmObrada.add(jmObracun);
    jmObrada.addSeparator();
    jmObrada.add(jmIzvObr);
    jmObrada.add(jmObrasciObr);
    jmObrada.addSeparator();
    jmObrada.add(jmiEvidencija);
    jmObrada.addSeparator();
    jmObrada.add(jmiKalkulator);
    jmIzvObr.add(jmiIspList);
    jmIzvObr.add(jmiRekPrim);
    jmIzvObr.add(jmiRekDopPor);
    jmIzvObr.add(jmiRekObr);
    jmIzvObr.add(jmiSpecKred);
    jmIzvObr.add(jmiNeobKred);
    jmIzvObr.add(jmiMjIzv);
    jmIzvObr.addSeparator();
    jmIzvObr.add(jmiGeneric);
    jmObrasciObr.add(jmiRS);
    jmObrasciObr.add(jmiSPL);
    jmObrasciObr.add(jmiIPP);
    jmObrasciObr.add(jmiVirmani);
    jmObrasciObr.add(jmiBankSpec);
    jmObrasciObr.add(jmiIDObr);
    jmObrasciObr.add(jmiJOPPD);
    jmArhiva.add(jmiArhiviranje);
    jmArhiva.add(jmiDohArh);
    jmArhiva.add(jmiDelArh);
    jmArhiva.add(jmiDatIspl);
    jmArhiva.addSeparator();
    jmArhiva.add(jmIzvArh);
    jmArhiva.add(jmObrasciArh);

    jmIzvArh.add(jmiIspListA);
    jmIzvArh.add(jmiRekPrimA);
    jmIzvArh.add(jmiRekDopPorA);
    jmIzvArh.add(jmiRekObrA);
    jmIzvArh.add(jmiSpecKredA);
    jmIzvArh.add(jmiMjIzvArh);
    jmIzvArh.addSeparator();
    jmIzvArh.add(jmiGenericArh);

//    jmIzvArh.add(jmiNeobKredA);

    jmObracun.add(jmiObrPrimanja);
    jmObracun.add(jmiObrPor);
//    jmObracun.add(jmiObrKred);
    jmObracun.add(jmiObrOs);
    jmObrasciArh.add(jmiID);
    jmObrasciArh.add(jmiRSArh);
    jmObrasciArh.add(jmiM4);
    jmObrasciArh.add(jmiER1);
    jmObrasciArh.add(jmiDNR);
    jmObrasciArh.add(jmiPK);
    jmObrasciArh.add(jmiVirmaniArh);
    jmObrasciArh.add(jmiBankSpecA);
    setRaJMenuBar(jMenuBar);
    jmSys.add(jmAddDefault);
    jmAddDefault.add(jmiAddDefIzvjestaji);
    setToolMenu(jmSys);
  }

  void jmiRadMj_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRadMj",jmiRadMj.getText());
  }

  void jmiVrodn_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmVrodn",jmiVrodn.getText());
  }

  void jmiZupanije_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmZupanije",jmiZupanije.getText());
  }

  void jmiOpcine_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmOpcine",jmiOpcine.getText());
  }

  void jmiPovjerioci_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmPovjerioci",jmiPovjerioci.getText());
  }

  void jmiBanke_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmBankePL",jmiBanke.getText());
  }

  void jmiIsplMj_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmIsplMj",jmiIsplMj.getText());
  }

  void jmiVrstePrim_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmVrstePrim",jmiVrstePrim.getText());
  }

  void jmiOdbici_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmOdbici",jmiOdbici.getText());
  }

  void jmiVrsteOdb_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmVrsteOdb",jmiVrsteOdb.getText());
  }

  void jmiPlSifre_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmPlSifre",jmiPlSifre.getText());
  }

  void jmiNacobr_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmNacobr",jmiNacobr.getText());
  }

  void jmiSume_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmSume",jmiSume.getText());
  }

  void jmiOsnovice_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmOsnovice",jmiOsnovice.getText());
  }

  void jmiIzvjDef_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmIzvjDef",jmiIzvjDef.getText());
  }
  
  void jmiZnacRad_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmZnacRad",jmiZnacRad.getText());
  }

  void jmiOrgpl_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmOrgpl",jmiOrgpl.getText());
  }

  void jmiRadnicipl_actionPerformed(ActionEvent e) {
    frmRadnicipl frpl = hr.restart.pl.frmRadnicipl.getInstance();
    centerFrame(frpl, 0, jmiRadnicipl.getText());
    showFrame(frpl);
    //showFrame("hr.restart.pl.frmRadnicipl",jmiRadnicipl.getText());
  }

  void jmiFondSati_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmFondSati",jmiFondSati.getText());
  }

  void jmiVriBoda_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmVriBoda",jmiVriBoda.getText());
  }

  void jmiIniPrim_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmIniPrim",jmiIniPrim.getText());
  }

  void jmiIniciranje_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmIniciranje",jmiIniciranje.getText());
  }

  void jmiPrimanja_actionPerformed(ActionEvent e) {
    hr.restart.util.PreSelect.showPreselect("hr.restart.pl.presPrimanja","hr.restart.pl.frmPrimanja",jmiPrimanja.getText());
//    showFrame("hr.restart.pl.frmPrimanja",jmiPrimanja.getText());
  }

  void jmiPrisRad_actionPerformed(ActionEvent e) {
    hr.restart.util.PreSelect.showPreselect("hr.restart.pl.presPrisRad","hr.restart.pl.frmPrisRad",jmiPrisRad.getText());
//    showFrame("hr.restart.pl.frmPrisRad",jmiPrisRad.getText());
  }

  void jmiObrPrimanja_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmObrPrimanja",jmiObrPrimanja.getText());
  }

  void jmiObrPor_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmObrPor",jmiObrPor.getText());
  }

//  void jmiObrKred_actionPerformed(ActionEvent e) {
//    showFrame("hr.restart.pl.frmObrKred",jmiObrKred.getText());
//  }

  void jmiObrOs_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmObrOs",jmiObrOs.getText());
  }

  void jmiRS_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRS",jmiRS.getText());
  }
  
  void jmiJOPPD_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.sk.frmPDV2", "Obrasci za poreznu upravu");
  }

  void jmiVirmani_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmVirmaniPl",jmiVirmani.getText());
  }

  void jmiBankSpec_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmBankSpec",jmiBankSpec.getText());
  }
  void jmiBankSpecA_actionPerformed(ActionEvent e) {
//    showFrame("hr.restart.pl.frmBankSpec",jmiBankSpec.getText());
    frmBankSpec spec = hr.restart.pl.frmBankSpec.getInstanceA();
    centerFrame(spec, 0, jmiBankSpec.getText()+" iz arhive");
    showFrame(spec);
  }

  void jmiIspList_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmIspList",jmiIspList.getText());
  }

  void jmiIspListA_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmIspListA",jmiIspList.getText());
  }

  void jmiRekPrim_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRekPrim",jmiRekPrim.getText());
  }
  void jmiRekPrimA_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRekPrimA",jmiRekPrimA.getText());
  }

  void jmiRekDopPor_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRekDopPor",jmiRekDopPor.getText());
  }

  void jmiRekDopPorA_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRekDopPorA",jmiRekDopPor.getText());
  }

  void jmiRekObr_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRekObr",jmiRekObr.getText());
  }

  void jmiRekObrA_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRekObrA",jmiRekObrA.getText());
  }

  void jmiSpecKred_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmSpecKred",jmiSpecKred.getText());
  }

  void jmiSpecKredA_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmSpecKredA",jmiSpecKredA.getText());
  }

  void jmiNeobKred_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmNeobKred",jmiNeobKred.getText());
  }

  void jmiArhiviranje_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmArhiviranje",jmiArhiviranje.getText());
  }

  void jmiParametriPl_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmParametriPl",jmiParametriPl.getText());
  }

  void jmiDohArh_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmDohArh",jmiDohArh.getText());
  }

  void jmiDelArh_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmDelArh",jmiDelArh.getText());
  }

  void jmiDatIspl_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmDatIspl",jmiDatIspl.getText());
  }

  void jmiID_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmID",jmiID.getText());
  }

  void jmiRSArh_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmRSArh",jmiRSArh.getText());
  }

  void jmiM4_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmM4",jmiM4.getText());
  }

  void jmiER1_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmER1",jmiER1.getText());
  }

  void jmiDNR_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmDNR",jmiDNR.getText());
  }

  void jmiSPL_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmSPL",jmiSPL.getText());
  }
  
  void jmiIPP_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmIPP",jmiIPP.getText());
  }

  void jmiIDObr_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmIDObr",jmiIDObr.getText());
  }

  void jmiGeneric_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmGeneric",jmiGeneric.getText());
  }

  void jmiPK_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmPK",jmiPK.getText());
  }

  void jmiMjIzv_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmMjIzv",jmiMjIzv.getText());
  }

  void jmiMjIzvArh_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmMjIzvArh",jmiMjIzvArh.getText());
  }

  void jmiGenericArh_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmGeneric",jmiGenericArh.getText());
  }

  void jmiVirmaniArh_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmVirmaniPlArh",jmiVirmaniArh.getText());
  }
  void jmiKalkulator_actionPerformed(ActionEvent e) {
    frmCalcPorez fcp = new frmCalcPorez(new raCalcPorezDataGetterBazara());
    centerFrame(fcp);
    showFrame(fcp);
  }
  void jmiEvidencija_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.pl.frmEvidencijaDjel",jmiEvidencija.getText());
  }
}