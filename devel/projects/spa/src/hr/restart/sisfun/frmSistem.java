/****license*****************************************************************
**   file: frmSistem.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.baza.kreator;
import hr.restart.util.raFileFilter;
import hr.restart.util.raLLFrames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class frmSistem extends hr.restart.util.startFrame {
  static frmSistem frms;
  JMenuBar jSisMnuBar = new JMenuBar();
  JMenu jSisMnu = new JMenu();
  JMenu jAdmin = new JMenu();
  JMenu jSpecMnu = new JMenu();
  JMenu jSpecRobno = new JMenu();
  JMenu jSisAla = new JMenu();
  JMenu jReplika = new JMenu();
  JMenu jSisPod = new JMenu();
  JMenu jUserPod = new JMenu();
  JMenu jMatPrn = new JMenu();
  JMenuItem jCreateTemp = new JMenuItem();
  JMenuItem jCreateTemp2 = new JMenuItem();
  JMenuItem jClassState = new JMenuItem();
  JMenuItem jScript = new JMenuItem();

  JMenuItem jRepInfo = new JMenuItem();
  JMenuItem jRepUrl = new JMenuItem();
  JMenuItem jRepDef = new JMenuItem();
//  JMenuItem jPilot = new JMenuItem();
  JMenuItem jSpecUsrSkl = new JMenuItem();
  JMenuItem jSpecKnjiGod = new JMenuItem();
  JMenuItem jSpecDefval = new JMenuItem();
  JMenuItem jSisMnuApps = new JMenuItem();
  JMenuItem jSisMnuMenus = new JMenuItem();
  JMenuItem jSisMnuProg = new JMenuItem();  
  JMenuItem jSisMnuFunc = new JMenuItem();
  JMenuItem jSisMnuUsrgrp = new JMenuItem();
  JMenuItem jSisMnuPrava = new JMenuItem();
  JMenuItem jSisMnuUsr = new JMenuItem();
//  JMenuItem jSisTest = new JMenuItem();
  JMenuItem jSisMnuTab = new JMenuItem();
  JMenuItem jSisMnuVrdok = new JMenuItem();
  JMenuItem jSisMnuRep = new JMenuItem();
  JMenuItem jSisMnuRepx = new JMenuItem();
  JMenuItem jSisMnuRepList = new JMenuItem();
  JMenuItem jSisMnuVrshemek = new JMenuItem();
  JMenuItem jSisMnuDefsh = new JMenuItem();
  JMenuItem jSisMnuKlj = new JMenuItem();
  JMenuItem jSisMnuPar = new JMenuItem();
  JMenuItem jSisMnuVrart = new JMenuItem();
  JMenuItem jSisMnuSect = new JMenuItem();
  JMenuItem jSisMnuAssign = new JMenuItem();
  JMenuItem jSisMnuPrn = new JMenuItem();
  JMenuItem jSisMnuRM = new JMenuItem();
  JMenuItem jSisMnuDoc = new JMenuItem();
  JMenuItem jSisMnuShd = new JMenuItem();
  JMenuItem jAdminInit = new JMenuItem();
  JMenuItem jAdminKreator = new JMenuItem();
  JMenuItem jAdminParam = new JMenuItem();
  boolean creating = false;
  Runnable init = new Runnable() {
      public void run() {
        creating = true;
        try {
          Thread.sleep(100);
        }
        catch (Exception ex) {}
        kreator.getKreator().initialize(frms);
        creating = false;
      }
    };

  JFileChooser sf = new JFileChooser(".");

//  frmApl frmapl = new frmApl();
  /**Construct the frame*/
  public frmSistem() {
    try {
      frms = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static frmSistem getFrmSistem() {
    return frms;
  }
  /*Component initialization*/
  private void jbInit() throws Exception  {
    jSisMnu.setText("Sistemske funkcije");
    jSpecMnu.setText("Posebni parametri");
    jSisMnuApps.setText("Aplikacije");
    jSisMnuApps.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuApps_actionPerformed(e);
      }
    });
    jSisMnuMenus.setText("Izbornici");
    jSisMnuMenus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuMenus_actionPerformed(e);
      }
    });
    jSisMnuProg.setText("Programi");
    jSisMnuProg.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuProg_actionPerformed(e);
      }
    });
    jSisMnuFunc.setText("Funkcije");
    jSisMnuFunc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuFunc_actionPerformed(e);
      }
    });
    jSisMnuUsrgrp.setText("Grupe korisnika");
    jSisMnuUsrgrp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuUsrgrp_actionPerformed(e);
      }
    });
    jSisMnuPrava.setText("Prava");
    jSisMnuPrava.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuPrava_actionPerformed(e);
      }
    });
    jSisMnuUsr.setText("Korisnici");
    jSisMnuUsr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuUsr_actionPerformed(e);
      }
    });
//    jSisTest.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jSisTest_actionPerformed(e);
//      }
//    });
    jSisMnuTab.setText("Tablice");
    jSisMnuTab.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuTab_actionPerformed(e);
      }
    });
    jSisMnuPrn.setText("Definicija štampa\u010Da");
    jSisMnuPrn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuPrn_actionPerformed(e);
      }
    });
    jSisMnuRM.setText("Radna mjesta");
    jSisMnuRM.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuRM_actionPerformed(e);
      }
    });
    jSisMnuDoc.setText("Dokumenti");
    jSisMnuDoc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuDoc_actionPerformed(e);
      }
    });
    jSisMnuVrdok.setText("Vrste dokumenta");
    jSisMnuVrdok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuVrdok_actionPerformed(e);
      }
    });
    jSisMnuVrshemek.setText("Vrste shema kontiranja");
    jSisMnuVrshemek.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuVrshemek_actionPerformed(e);
      }
    });
    jSisMnuDefsh.setText("Standardne sheme kontiranja");
    jSisMnuDefsh.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuDefsh_actionPerformed(e);
      }
    });
    jSisMnuKlj.setText("Pregled zaklju\u010Davanja");
    jSisMnuKlj.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuKlj_actionPerformed(e);
      }
    });
    jSisMnuPar.setText("Defaultni parametri");
    jSisMnuPar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuPar_actionPerformed(e);
      }
    });
    jSisMnuVrart.setText("Vrste artikala");
    jSisMnuVrart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuVrart_actionPerformed(e);
      }
    });
//    jSisTest.setText("Incijalizacija baze");
//    jSisTest.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jSisTest_actionPerformed(e);
//      }
//    });
    jSisMnuRep.setText("Dodatni izvještaji");
    jSisMnuRep.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuRep_actionPerformed(e);
      }
    });
    jSisMnuRepx.setText("Excel izvještaji");
    jSisMnuRepx.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuRepx_actionPerformed(e);
      }
    });
    jSisMnuRepList.setText("Izvještaji");
    jSisMnuRepList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuRepList_actionPerformed(e);
      }
    });
    jSisMnuSect.setText("Dijelovi izvještaja");
    jSisMnuSect.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuSect_actionPerformed(e);
      }
    });
    jSisMnuAssign.setText("Povezivanje dijelova izvještaja");
    jSisMnuAssign.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSisMnuAssign_actionPerformed(e);
      }
    });

    jAdminInit.setText("Inicijalizacija baze");
    jAdminInit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jAdminInit_actionPerformed(e);
      }
    });
    jAdminKreator.setText("Kreator");
    jAdminKreator.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jAdminKreator_actionPerformed(e);
      }
    });
    jAdminParam.setText("Parametri");
    jAdminParam.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jAdminParam_actionPerformed(e);
      }
    });
    jSpecUsrSkl.setText("Korisnici - skladišta");
    jSpecUsrSkl.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSpecUsrSkl_actionPerformed(e);
      }
    });
    jSpecKnjiGod.setText("Knjigovodstva - godine");
    jSpecKnjiGod.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSpecKnjiGod_actionPerformed(e);
      }
    });
    jSpecDefval.setText("Default vrijednosti dokumenata");
    jSpecDefval.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jSpecDefval_actionPerformed(e);
      }
    });
    jClassState.setText("Popis klasa");
    jClassState.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jClassState_actionPerformed(e);
      }
    });
    jScript.setText("Pokreni skriptu");
    jScript.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jScript_actionPerformed(e);
      }
    });

    jCreateTemp.setText("Kreiranje java report klasa");
    jCreateTemp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jCreateTemp_actionPerformed(e);
      }
    });
    jCreateTemp2.setText("Inverzna operacija");
    jCreateTemp2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jCreateTemp2_actionPerformed(e);
      }
    });

    jSisMnuShd.setText("Ugasi raèunalo");
    jSisMnuShd.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!raUser.getInstance().isSuper() &&
            !"test".equalsIgnoreCase(raUser.getInstance().getUser())) return;
        raUser.getInstance().unlockUser();
        if (!hr.restart.util.Sys.shutdown(false)) {
          raUser.getInstance().setUser(raUser.getInstance().getUser());
          raUser.getInstance().lockRow(dM.getDataModule().getUseri(), new String[] {"CUSER"});
          JOptionPane.showMessageDialog(null, "Gašenje nije podržano!", "Greška",
                                        JOptionPane.ERROR_MESSAGE);
        }

      }
    });

//    jPilot.setText("Pregled baze");
//    jPilot.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jPilot_actionPerformed(e);
//      }
//    });

    jSisPod.setText("Sistemske tablice");
    jSisPod.add(jSisMnuApps);
    jSisPod.add(jSisMnuMenus);
    jSisPod.add(jSisMnuProg);
    jSisPod.add(jSisMnuFunc);
    jSisPod.add(jSisMnuTab);
    jSisPod.add(jSisMnuVrdok);
    jSisPod.add(jSisMnuVrart);

    jUserPod.setText("Podaci za korisnike");
    jUserPod.add(jSisMnuUsrgrp);
    jUserPod.add(jSisMnuUsr);
    jUserPod.add(jSisMnuPrava);
    jUserPod.add(jSisMnuKlj);

    jMatPrn.setText("Matri\u010Dni ispis");
    jMatPrn.add(jSisMnuPrn);
    jMatPrn.add(jSisMnuRM);
    jMatPrn.add(jSisMnuDoc);

    jRepDef.setText("Definiranje replikacije");
    jRepDef.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jRepDef_actionPerformed();
      }
    });
    jRepInfo.setText("Informacije o replikaciji");
    jRepInfo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jRepInfo_actionPerformed();
      }
    });
    jRepUrl.setText("URL-ovi replikacije");
    jRepUrl.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jRepUrl_actionPerformed();
      }
    });
    jReplika.setText("Replikacija");
    jReplika.add(jRepDef);
    jReplika.add(jRepInfo);
    jReplika.add(jRepUrl);

    jSisAla.setText("Sistemski alati");
    jSisAla.add(jCreateTemp);
    jSisAla.add(jCreateTemp2);
    jSisAla.add(jClassState);

//    jSpecMnu.add(jPilot);
    jSpecRobno.setText("Robno knjigovodstvo");
    jSpecRobno.add(jSpecUsrSkl);
    jSpecRobno.add(jSpecKnjiGod);
    jSpecRobno.add(jSpecDefval);
    jAdmin.setText("Administriranje baze");
    jAdmin.add(jAdminInit);
    jAdmin.add(jAdminKreator);
    jAdmin.add(jAdminParam);
    jSisMnuBar.add(jSisMnu);
    jSisMnu.add(jSisPod);
    jSisMnu.add(jUserPod);
    jSisMnu.add(jMatPrn);
    jSisMnu.add(jSisMnuVrshemek);
    jSisMnu.add(jSisMnuDefsh);
    jSisMnu.add(jSisMnuPar);
    jSisMnu.add(jSisMnuRep);
    jSisMnu.add(jSisMnuRepList);
    jSisMnu.add(jSisMnuRepx);
    jSisMnu.add(jSisMnuSect);
    if (raUser.getInstance().getUser().equals("test"))
      jSisMnu.add(jScript);
//    jSisMnu.add(jSisMnuAssign);
    jSisMnu.add(jSisMnuShd);
    jSisMnu.addSeparator();
    jSisMnu.add(jReplika);

    jSisMnuBar.add(jSpecMnu);
//    jSisMnuBar.add(jSisAla);
    setToolMenu(jSisAla);
    jSpecMnu.add(jSpecRobno);

    String usr = raUser.getInstance().getUser();
    if (usr == null || usr.length() == 0 || usr.equals("root") ||
        usr.equals("test") || usr.equals("restart")) {
      jSisMnu.addSeparator();
      jSisMnu.add(jAdmin);
    }
//    jSisMnuBar.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    this.setRaJMenuBar(jSisMnuBar);
//    centerFrame(frmapl,15,jSisMnuApps.getText());
//    GlobalEventListener.install();
    sf.setFileFilter(new raFileFilter("Script datoteke (*.rsp)"));
  }

  void removeAdmin() {
    jSisMnu.remove(jAdmin);
  }

  void jSisMnuApps_actionPerformed(ActionEvent e) {
    if (busy()) return;
//    showFrame(frmapl);
    showFrame("hr.restart.sisfun.frmApl",15,jSisMnuApps.getText());
  }
  
  void jSisMnuMenus_actionPerformed(ActionEvent e) {
    if (busy()) return;
//    showFrame(frmapl);
    showFrame("hr.restart.util.menus.frmMenus",15,jSisMnuMenus.getText());
  }

  void jSisMnuProg_actionPerformed(ActionEvent e) {
    if (busy()) return;
//    showFrame(frmapl);
    showFrame("hr.restart.sisfun.frmProg",15,jSisMnuProg.getText());
  }

  void jSisMnuFunc_actionPerformed(ActionEvent e) {
    if (busy()) return;
//    showFrame(frmapl);
    showFrame("hr.restart.sisfun.frmFunc",15,jSisMnuFunc.getText());
  }

  /*void jSisTest_actionPerformed(ActionEvent e) {
    System.out.println(frmParam.getParam("sisfun","testni2"));
  }*/

  void jSisMnuPrava_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmPrava",jSisMnuPrava.getText());
  }

  void jSisMnuUsr_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmUseri",jSisMnuUsr.getText());
  }

  void jSisMnuTab_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmTablice",jSisMnuTab.getText());
  }

  void jSisMnuVrdok_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmVrdok",jSisMnuVrdok.getText());
  }

  void jSisMnuVrshemek_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmVrsteShemaKonti",jSisMnuVrshemek.getText());
  }

  void jSisMnuDefsh_actionPerformed(ActionEvent e) {
    if (busy()) return;
    hr.restart.sisfun.frmShkontaUnos frmShU = (hr.restart.sisfun.frmShkontaUnos) this.showFrame("hr.restart.sisfun.frmShkontaUnos", 0, "Standardne sheme kontiranja", false);
    frmShU.go();
  }

  void jSisMnuKlj_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmKljucevi",jSisMnuKlj.getText());
  }

  void jSisMnuPar_actionPerformed(ActionEvent e) {
    if (busy()) return;
    hr.restart.sisfun.frmParamGlob frmg = (hr.restart.sisfun.frmParamGlob) this.showFrame("hr.restart.sisfun.frmParamGlob", 15, "Defaultni parametri", false);
    frmg.psFrmParam.showPreselect(frmg, "Defaultni parametri");
  }

  void jSisMnuVrart_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmVrart",jSisMnuVrart.getText());
  }

  void jSisMnuUsrgrp_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmGrupeUsera",jSisMnuUsrgrp.getText());
  }

  void jSpecUsrSkl_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmUsersklad",jSpecUsrSkl.getText());
  }

  void jSpecDefval_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmDefvaluedok",jSpecDefval.getText());
  }

  void jSpecKnjiGod_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmKnjigod",jSpecKnjiGod.getText());
  }

  void jAdminInit_actionPerformed(ActionEvent e) {
    if (busy()) return;
    if (JOptionPane.showConfirmDialog(null, "Ova naredba \u0107e izbrisati \u010Ditavu bazu sa svim podacima.\n"+
      "Jeste li sigurni da to želite?", "Inicijalizacija baze", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
      == JOptionPane.YES_OPTION) {
      String resp = JOptionPane.showInputDialog(null, "Inicijalizacija baze? Upišite DA (velikim slovima):",
         "Potvrda inicijalizacije", JOptionPane.PLAIN_MESSAGE);
      if (resp != null && resp.equals("DA")) {
        Thread thr = new Thread(init);
        thr.start();
      }
    }
  }
  void jAdminKreator_actionPerformed(ActionEvent e) {
    if (busy()) return;
    kreator.getKreator().pack();
    kreator.getKreator().show();
  }

  void jCreateTemp_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmReportCreator",jCreateTemp.getText());
  }

  void jCreateTemp2_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmTemplateGenerator",jCreateTemp2.getText());
  }

  void jClassState_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmClassState",jClassState.getText());
  }

//  private void automate() {
 /*   try {
      Borg.setMouseOrigin(GlobalEventListener.getMouseX(), GlobalEventListener.getMouseY());
      Borg.flush();
      AutomatedMenu am = new AutomatedMenu(frmSistem.this);
      am.clickMenuItem("Programi");
      AutomatedMatPodaci amp = new AutomatedMatPodaci(
          am.findMatPodaci("hr.restart.sisfun.frmProg"));
      amp.delay(500);
      amp.start();
      amp.setField("CPROG", "prg23");
      amp.setField("OPISPROG", "YEAH! Automated entry");
      amp.setField("APP", "robno");
      amp.accept();
      amp.cancel();
    } catch (Exception e) {
      e.printStackTrace();
    } */
//  }


  void jScript_actionPerformed(ActionEvent e) {
    if (busy() || GlobalEventListener.isBorgThreadRunning()) return;
    if (sf.showOpenDialog(raLLFrames.getRaLLFrames().getMsgStartFrame()) == sf.APPROVE_OPTION)
      new AutomationScript(sf.getSelectedFile().getAbsolutePath()).execute();
  }


//  void jPilot_actionPerformed(ActionEvent e) {
//    if (busy()) return;
//    showFrame("hr.restart.sisfun.raPilot",jPilot.getText());
//  }

  void jAdminParam_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmAllParams",jAdminParam.getText());
  }
  void jSisMnuRep_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmExternalReports",jSisMnuRep.getText());
  }
  void jSisMnuRepx_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmReportx",jSisMnuRepx.getText());
  }
  void jSisMnuRepList_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmReportList",jSisMnuRepList.getText());
//    showFrame("hr.restart.util.reports.frmReportSections",jSisMnuRepList.getText());
  }

  void jSisMnuSect_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.util.reports.frmCustomSections",jSisMnuSect.getText());
  }

  void jSisMnuAssign_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmSectionAssign",jSisMnuAssign.getText());
  }

  void jSisMnuPrn_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmMxPrinter",jSisMnuPrn.getText());
  }

  void jSisMnuRM_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmMxPrinterRM",jSisMnuRM.getText());
  }

  void jSisMnuDoc_actionPerformed(ActionEvent e) {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmMxDocument",jSisMnuDoc.getText());
  }

  void jRepDef_actionPerformed() {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmRepldef",jRepDef.getText());
  }

  void jRepInfo_actionPerformed() {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmReplinfo",jRepInfo.getText());
  }

  void jRepUrl_actionPerformed() {
    if (busy()) return;
    showFrame("hr.restart.sisfun.frmReplurl",jRepUrl.getText());
  }

  public boolean busy() {
    if (creating) {
      JOptionPane.showMessageDialog(null, "Inicijalizacija u tijeku!", "Sistem zauzet", JOptionPane.ERROR_MESSAGE);
      return true;
    } else return false;
  }
}
