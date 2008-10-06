/****license*****************************************************************
**   file: osMain.java
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
package hr.restart.os;

import hr.restart.util.raLoader;
import hr.restart.util.startFrame;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class osMain extends startFrame {
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JMenuBar jmMain = new JMenuBar();
  JMenu jmOsPodaci = new JMenu();
  JMenu jmOsSredstva = new JMenu();
  JMenu jmSitniInventar = new JMenu();
  JMenu jmIzracuni = new JMenu();
  JMenu jmIspis = new JMenu();
  JMenu jmIspisOS = new JMenu();
  JMenu jmIspisSI = new JMenu();
  JMenu jmSys = new JMenu();
  JMenuItem jmLokacije = new JMenuItem();
  JMenuItem jmAmGrupe = new JMenuItem();
  JMenuItem jmRevSkupine = new JMenuItem();
  JMenuItem jmArtikli = new JMenuItem();
  JMenuItem jmPromjene = new JMenuItem();
  JMenuItem jmStaraOS = new JMenuItem();
  JMenuItem jmTekucaOS = new JMenuItem();
  JMenuItem jmLikvidacijaOS = new JMenuItem();
  JMenuItem jmStaraSI = new JMenuItem();
  JMenuItem jmTekucaSI = new JMenuItem();
  JMenuItem jmLikvidacijaSI = new JMenuItem();
  JMenuItem jmAmortizacija = new JMenuItem();
  JMenuItem jmPocStanje = new JMenuItem();
  JMenuItem jmDel = new JMenuItem();
  JMenuItem jmIspOS = new JMenuItem();

//  JMenuItem jmIspOSNG = new JMenuItem();
//  JMenuItem jmIspSING = new JMenuItem();

  JMenuItem jmIspSI = new JMenuItem();
  JMenuItem jmIspAmor = new JMenuItem();
  JMenuItem jmIspRek = new JMenuItem();
  JMenuItem jmIspLikvi = new JMenuItem();
  JMenuItem jmIspKnj = new JMenuItem();
  JMenuItem jmIspInv = new JMenuItem();
  JMenuItem jmKnjizSI = new JMenuItem();
  JMenuItem jmLikviSI = new JMenuItem();
  JMenuItem jmInvSI = new JMenuItem();
  JMenuItem jmBrisanje = new JMenuItem();
  private JMenuItem jmPorijeklo = new JMenuItem();
  JMenuItem jmPromCORG = new JMenuItem();
  private JMenuItem jmPripremaOS = new JMenuItem();
  private JMenuItem jmObjekti = new JMenuItem();
  private JMenuItem jmStatRad = new JMenuItem();
  private JMenuItem jmKontaIsp = new JMenuItem();
  private JMenuItem jmDelAll = new JMenuItem();
  private JMenuItem jmVrifyAmor = new JMenuItem();
  private JMenuItem jmTemeljnica = new JMenuItem();
  private JMenuItem jmShemeKontaOS = new JMenuItem();
  private JMenuItem jmVrskOS = new JMenuItem();

  public osMain() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception  {
    jmSys.setText("Pomo\u0107ne funkcije");
    jmOsPodaci.setText("Osnovni podaci");
    jmOsSredstva.setText("Osnovna sredstva");
    jmSitniInventar.setText("Sitni inventar");
    jmIzracuni.setText("Izraèuni");
    jmIspis.setText("Ispisi");
    jmIspisOS.setText("Osnovna Sredstva");
    jmIspisSI.setText("Sitni inventar");
    jmLokacije.setText("Lokacije");
    jmLokacije.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmLokacije_actionPerformed(e);
      }
    });
    jmAmGrupe.setText("Amortizacijske grupe");
    jmAmGrupe.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmAmGrupe_actionPerformed(e);
      }
    });
    jmRevSkupine.setText("Revalorizacijske skupine");
    jmRevSkupine.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRevSkupine_actionPerformed(e);
      }
    });
    jmArtikli.setText("Artikli");
    jmArtikli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmArtikli_actionPerformed(e);
      }
    });
    jmPromjene.setText("Vrste promjena");
    jmPromjene.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPromjene_actionPerformed(e);
      }
    });
    jmStaraOS.setText("Sredstva iz predhodnih godina");
    jmStaraOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStaraOS_actionPerformed(e);
      }
    });
    jmTekucaOS.setText("Sredstva iz tekuæe godine");
    jmTekucaOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmTekucaOS_actionPerformed(e);
      }
    });
    jmLikvidacijaOS.setText("Likvidacija");
    jmLikvidacijaOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmLikvidacijaOS_actionPerformed(e);
      }
    });
    jmStaraSI.setText("Inventar iz predhodnih godina");
    jmStaraSI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStaraSI_actionPerformed(e);
      }
    });
    jmTekucaSI.setText("Inventar iz tekuæe godine");
    jmTekucaSI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmTekucaSI_actionPerformed(e);
      }
    });
    jmLikvidacijaSI.setText("Likvidacija");
    jmLikvidacijaSI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmLikvidacijaSI_actionPerformed(e);
      }
    });
    jmAmortizacija.setText("Amortizacija");
    jmAmortizacija.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmAmortizacija_actionPerformed(e);
      }
    });
    jmPocStanje.setText("Poèetno stanje");
    jmPocStanje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPocStanje_actionPerformed(e);
      }
    });
    jmDel.setText("Brisanje posljednjeg obraèuna");
    jmDel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmDel_actionPerformed(e);
      }
    });
    jmIspOS.setText("Ispis osnovnih sredstava");
    jmIspOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspOS_actionPerformed(e);
      }
    });


//    jmIspOSNG.setText("Radni ispis osnovnih sredstava");
//    jmIspOSNG.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmIspOSNG_actionPerformed(e);
//      }
//    });
//    jmIspSING.setText("Radni ispis sitnog inventara");
//    jmIspSING.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jmIspSING_actionPerformed(e);
//      }
//    });

    jmIspSI.setText("Ispis sitnog inventara");
    jmIspSI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspSI_actionPerformed(e);
      }
    });
    jmIspAmor.setText("Ispis amortizacije");
    jmIspAmor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspAmor_actionPerformed(e);
      }
    });
    jmIspRek.setText("Rekapitulacijske liste");
    jmIspRek.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspRek_actionPerformed(e);
      }
    });
    jmIspLikvi.setText("Ispis likvidiranih sredstava");
    jmIspLikvi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspLikvi_actionPerformed(e);
      }
    });
    jmIspKnj.setText("Ispis knjiženja");
    jmIspKnj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspKnj_actionPerformed(e);
      }
    });
    jmIspInv.setText("Ispis inventurne liste");
    jmIspInv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspInv_actionPerformed(e);
      }
    });
    jmKnjizSI.setText("Ispis knjiženja");
    jmKnjizSI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKnjizSI_actionPerformed(e);
      }
    });
    jmLikviSI.setText("Ispis likvidiranog");
    jmLikviSI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmLikviSI_actionPerformed(e);
      }
    });
    jmInvSI.setText("Ispis inventurne liste");
    jmInvSI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmInvSI_actionPerformed(e);
      }
    });
    jmBrisanje.setText("Brisanje prometa");
    jmBrisanje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmBrisanje_actionPerformed(e);
      }
    });
    jmPorijeklo.setText("Porijeklo");
    jmPorijeklo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPorijeklo_actionPerformed(e);
      }
    });
    jmPromCORG.setText("Promjena organizacijske jedinice");
    jmPromCORG.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPromCORG_actionPerformed(e);
      }
    });
    jmPripremaOS.setText("Sredstva u pripremi");
    jmPripremaOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPripremaOS_actionPerformed(e);
      }
    });
    jmObjekti.setText("Objekti");
    jmObjekti.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmObjekti_actionPerformed(e);
      }
    });
    jmStatRad.setText("Status rada");
    jmStatRad.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStatRad_actionPerformed(e);
      }
    });
    jmKontaIsp.setText("Konta ispravka");
    jmKontaIsp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKontaIsp_actionPerformed(e);
      }
    });
    jmDelAll.setText("Brisanje svih obraèuna");
    jmDelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmDelAll_actionPerformed(e);
      }
    });
    jmVrifyAmor.setText("Potvrda obraèuna");
    jmVrifyAmor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmVrifyAmor_actionPerformed(e);
      }
    });
    jmTemeljnica.setText("Temeljnica");
    jmTemeljnica.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmTemeljnica_actionPerformed(e);
      }
    });
    jmVrskOS.setActionCommand("Vrste shema knjiženja");
    jmVrskOS.setText("Vrste shema knjiženja");
    jmVrskOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmVrskOS_actionPerformed(e);
      }
    });
    jmShemeKontaOS.setActionCommand("Sheme knjiženja");
    jmShemeKontaOS.setText("Sheme knjiženja");
    jmShemeKontaOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmShemeKontaOS_actionPerformed(e);
      }
    });
    jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmMain.add(jmOsPodaci);
    jmMain.add(jmOsSredstva);
    jmMain.add(jmSitniInventar);
    jmMain.add(jmIzracuni);
    jmMain.add(jmIspis);
    jmOsPodaci.add(jmObjekti);
    jmOsPodaci.add(jmLokacije);
    jmOsPodaci.add(jmAmGrupe);
    jmOsPodaci.add(jmRevSkupine);
    jmOsPodaci.add(jmArtikli);
    jmOsPodaci.add(jmPromjene);
    jmOsPodaci.add(jmPorijeklo);
    jmOsPodaci.addSeparator();
    jmOsPodaci.add(jmVrskOS);
    jmOsPodaci.add(jmShemeKontaOS);
    jmOsPodaci.add(jmKontaIsp);
    jmOsPodaci.addSeparator();
    jmOsPodaci.add(jmStatRad);
    jmOsSredstva.add(jmPripremaOS);
    jmOsSredstva.add(jmStaraOS);
    jmOsSredstva.add(jmTekucaOS);
    jmOsSredstva.add(jmLikvidacijaOS);
    jmOsSredstva.addSeparator();
    jmOsSredstva.add(jmPromCORG);
    jmSitniInventar.add(jmStaraSI);
    jmSitniInventar.add(jmTekucaSI);
    jmSitniInventar.add(jmLikvidacijaSI);
/*    jmSitniInventar.addSeparator();
    jmSitniInventar.add(jmIspSI);
    jmSitniInventar.add(jmKnjizSI);
    jmSitniInventar.add(jmLikviSI);
    jmSitniInventar.add(jmInvSI);*/
    jmIzracuni.add(jmAmortizacija);
    jmIzracuni.add(jmVrifyAmor);
    jmIzracuni.addSeparator();
    jmIzracuni.add(jmPocStanje);
    jmIzracuni.add(jmDel);
    jmIzracuni.add(jmDelAll);
    jmIzracuni.addSeparator();
    jmIzracuni.add(jmTemeljnica);


//    jmIspis.addSeparator();
//    jmIspis.add(jmIspOSNG);
//    jmIspis.add(jmIspSING);
//    jmIspis.addSeparator();
/*
    jmIspis.add(jmIspOS);
    jmIspis.add(jmIspAmor);
    jmIspis.add(jmIspRek);
    jmIspis.add(jmIspLikvi);
    jmIspis.add(jmIspKnj);
    jmIspis.add(jmIspInv);
    jmIspis.addSeparator();
    jmIspis.add(jmTemeljnica);
*/

    jmIspis.add(jmIspisOS);
    jmIspis.add(jmIspisSI);

    jmIspisOS.add(jmIspOS);
    jmIspisOS.add(jmIspAmor);
    jmIspisOS.add(jmIspRek);
    jmIspisOS.add(jmIspLikvi);
    jmIspisOS.add(jmIspKnj);
    jmIspisOS.add(jmIspInv);

    jmIspisSI.add(jmIspSI);
    jmIspisSI.add(jmKnjizSI);
    jmIspisSI.add(jmLikviSI);
    jmIspisSI.add(jmInvSI);



    jmSys.add(jmBrisanje);
    setToolMenu(jmSys);
    this.setRaJMenuBar(jmMain);
  }
  void jmLokacije_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmOsLokacije", "Lokacije");
  }
  void jmAmGrupe_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmOsAmGrupe", "Amortizacijske grupe");
  }
  void jmRevSkupine_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmOsRevSkupine", "Revalorizacijske skupine");
  }
  void jmArtikli_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmOsArtikli", "Artikli");
  }
  void jmPromjene_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmOsVrPromjene", "Vrste promjena");
  }
  void jmStaraOS_actionPerformed(ActionEvent e) {
    if (!isOK()) return;
    frmOSStari fOsStari = (frmOSStari)raLoader.load("hr.restart.os.frmOSStari");
    fOsStari.pres.showPreselect(fOsStari, "Sredstva iz predhodnih godina - predselekcija");
  }
  void jmTekucaOS_actionPerformed(ActionEvent e) {
    if (!isOK()) return;
    frmOSNovi fOsNovi = (frmOSNovi)raLoader.load("hr.restart.os.frmOSNovi");
    fOsNovi.pres.showPreselect(fOsNovi, "Sredstva iz tekuæe godina - predselekcija");
  }
  void jmLikvidacijaOS_actionPerformed(ActionEvent e) {
    if (!isOK()) return;
    frmOSLikvi fOsLikvi = (frmOSLikvi)raLoader.load("hr.restart.os.frmOSLikvi");
    fOsLikvi.pres.showPreselect(fOsLikvi, "Likvidirana sredstva - predselekcija");
  }
  void jmStaraSI_actionPerformed(ActionEvent e) {
    if (!isOK()) return;
    frmSIStari fSiStari = (frmSIStari)raLoader.load("hr.restart.os.frmSIStari");
    fSiStari.pres.showPreselect(fSiStari, "Inventar iz predhodnih godina - predselekcija");
  }
  void jmTekucaSI_actionPerformed(ActionEvent e) {
    if (!isOK()) return;
    frmSINovi fSiNovi = (frmSINovi)raLoader.load("hr.restart.os.frmSINovi");
    fSiNovi.pres.showPreselect(fSiNovi, "Inventar iz tekuæe godine - predselekcija");
  }
  void jmLikvidacijaSI_actionPerformed(ActionEvent e) {
    if (!isOK()) return;
    frmSILikvi fSiLikvi = (frmSILikvi)raLoader.load("hr.restart.os.frmSILikvi");
    fSiLikvi.pres.showPreselect(fSiLikvi, "Likvidirani inventar - predselekcija");
  }
  void jmPripremaOS_actionPerformed(ActionEvent e) {
    if (!isOK()) return;
    frmOSPriprema fOSPriprema = (frmOSPriprema)raLoader.load("hr.restart.os.frmOSPriprema");
    fOSPriprema.pres.showPreselect(fOSPriprema, "Sredstva u pripremi - predselekcija");
  }
  void jmAmortizacija_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.upObrAmortizacija", "Obraèun amortizacije");
  }
  void jmIspOS_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispOS_NextGeneration", "Ispis osnovnih sredstava"); // hr.restart.os.ispOS
  }

//  void jmIspOSNG_actionPerformed(ActionEvent e) {
//    this.showFrame("hr.restart.os.ispOS_NextGeneration", jmIspOSNG.getText());
//  }
//  void jmIspSING_actionPerformed(ActionEvent e) {
//    this.showFrame("hr.restart.os.ispSI_NextGeneration", jmIspSING.getText());
//  }

  void jmIspSI_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispSI_NextGeneration", "Ispis sitnog inventara"); //was hr.restart.os.ispSI
  }
  void jmDel_actionPerformed(ActionEvent e) {
    dm.getOS_Metaobrada().open();
    if (dm.getOS_Metaobrada().rowCount()>0) {
      dm.getOS_Kontrola().open();
      dm.getOS_Kontrola().interactiveLocate(dm.getOS_Metaobrada().getString("CORG"),"CORG",com.borland.dx.dataset.Locate.FIRST, false);
      if (JOptionPane.showConfirmDialog(null,"Da li \u017Eelite poništiti posljednju obradu ?","Poništenje privremenih obrada",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
        if (dm.getOS_Kontrola().getString("AMOR").equals("D")) {
          JOptionPane.showConfirmDialog(null,"Konaèan obraèun amortizacije nije moguæe poništiti !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
          return;
        }
        hr.restart.os.osUtil.getUtil().deleteAmortizacija();
      }
    }
    else {
      JOptionPane.showConfirmDialog(null,"Nema aktivnih obraèuna !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    }
  }
  void jmDelAll_actionPerformed(ActionEvent e) {
    osUtil.getUtil().deleteAllAmortizacija();
  }
  void jmPocStanje_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.upPocStanje", "Poèetno stanje");
  }
  void jmIspAmor_actionPerformed(ActionEvent e) {
//    this.showFrame("hr.restart.os.ispAmor", "Ispis amortizacije");
    this.showFrame("hr.restart.os.ispAmor_NextGeneration", "Ispis amortizacije");
  }
  void jmIspRek_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispRekap_NextGeneration", "Ispis rekapitulacije");
//    this.showFrame("hr.restart.os.ispRekap", "Ispis rekapitulacije");
  }
  void jmIspLikvi_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispLikvi", "Ispis likvidiranih sredstaca");
  }
  void jmIspKnj_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispKnjiz", "Ispis knjiženja");
  }
  void jmIspInv_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispInv", "Ispis inventurne liste");
  }
  void jmKnjizSI_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispKnjizSI", "Ispis knjiženja");
  }
  void jmLikviSI_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispLikviSI", "Ispis likvidiranog sitnog inventara");
  }
  void jmInvSI_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.ispInvSI", "Ispis inventurne liste");
  }
  void jmBrisanje_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmBrisanje", "Brisanje osnovnih sredstava");
  }
  void jmPorijeklo_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmPorijeklo", "Porijeklo osnovnog sredstva");
  }
  void jmPromCORG_actionPerformed(ActionEvent e) {
    if (!isOK()) return;
    frmCorgChanger fCorgChanger = (frmCorgChanger)raLoader.load("hr.restart.os.frmCorgChanger");
    fCorgChanger.pres.showPreselect(fCorgChanger, "Organizacijska jedinica");
  }
  private boolean isOK(){
    if (rdOSUtil.getUtil().getKontrola()) {
      return true;
    }
    JOptionPane.showConfirmDialog(this.getStartFrame(),"Ne postoji kontrolni slog za aktivno knjigovodstvo !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    return false;
  }
  void jmStatRad_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmPromjene", "Formiranje promjena");
  }
  void jmObjekti_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmOsObjekti", "Objekti");
  }
  void jmKontaIsp_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmKontaIsp", "Konta ispravka");
  }
  void jmVrifyAmor_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmVerifyAmor", "Potvrda obraèuna amortizacije");
  }
  void jmTemeljnica_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmOSTemeljnica", "Izrada temeljnice");
  }
  void jmVrskOS_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmVrskOS", "Vrste shema knjiženja");
  }
  void jmShemeKontaOS_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.os.frmShemeKontaOS", jmShemeKontaOS.getText());
  }
}
