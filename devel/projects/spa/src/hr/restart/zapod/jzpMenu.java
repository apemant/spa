/****license*****************************************************************
**   file: jzpMenu.java
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
package hr.restart.zapod;



import hr.restart.sisfun.frmParam;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;





/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class jzpMenu extends javax.swing.JMenu {

  hr.restart.util.startFrame SF;

  java.util.ResourceBundle Res = java.util.ResourceBundle.getBundle(hr.restart.zapod.frmZapod.RESBUNDLENAME);

  public JMenuItem jzpMenuOrgJed = new JMenuItem();

  JMenuItem jzpMenuKonta = new JMenuItem();

  public JMenuItem jzpMenuPpar = new JMenuItem();

  JMenuItem jzpMenuPparHistory = new JMenuItem();
  
  JMenuItem jzpMenuZirorn = new JMenuItem();

  JMenuItem jzpMenuAgenti = new JMenuItem();
  
  JMenuItem jzpMenuTele = new JMenuItem();

  JMenuItem jzpMenuValute = new JMenuItem();

  JMenuItem jzpMenuTec = new JMenuItem();

  JMenuItem jzpMenuKor = new JMenuItem();
  
  JMenuItem jzpPP = new JMenuItem();

//novi

  JMenuItem jmKontaPar = new JMenuItem();

  public JMenuItem jmBanke = new JMenuItem();

  public JMenuItem jmKartice = new JMenuItem();

  JMenuItem jmUgovori = new JMenuItem();

  JMenuItem jmVrstaUgovora = new JMenuItem();

  JMenuItem jmGrupPart = new JMenuItem();

  public JMenuItem jmNacPlac = new JMenuItem();

  JMenuItem jmNapomene = new JMenuItem();

  JMenuItem jmVrTros = new JMenuItem();

//

  JMenuItem jmRadnici = new JMenuItem();

  JMenuItem jmSifarnici = new JMenuItem();

  JMenuItem jmZpZemlje = new JMenuItem();
  JMenuItem jmZupanije = new JMenuItem();
  JMenuItem jmGradovi = new JMenuItem();


//LOADER

/*

  frmPartneri fpartneri = frmPartneri.getFrmPartneri();

  frmOrgStr forgstr = frmOrgStr.getFrmOrgStr();

  frmKonta fkonta = frmKonta.getFrmKonta();

  frmZirorn fzirorn = frmZirorn.getFrmZirorn();

  frmAgenti fagenti = frmAgenti.getFrmAgenti();

  frmValute fvalute = frmValute.getFrmValute();

  frmTecajevi ftecajevi = frmTecajevi.getFrmTecajevi();

  frmLogos flogos = frmLogos.getFrmLogos();

*/

  public jzpMenu(hr.restart.util.startFrame startframe) {

    SF = startframe;

    jInit();

    this.addAncestorListener(new javax.swing.event.AncestorListener() {

      public void ancestorAdded(javax.swing.event.AncestorEvent e) {

        findSF();

      }

      public void ancestorMoved(javax.swing.event.AncestorEvent e) {

      }

      public void ancestorRemoved(javax.swing.event.AncestorEvent e) {

      }

    });

  }

  private void findSF() {

/*    if (this.getTopLevelAncestor() instanceof hr.restart.util.startFrame) {

      SF = (hr.restart.util.startFrame)this.getTopLevelAncestor();

    } else {

      SF = hr.restart.util.startFrame.getStartFrame();

    }*/



//LOADER

/*

    SF.centerFrame(fpartneri,15,Res.getString("jzpMenuPpar_text"));

    SF.centerFrame(fkonta,15,Res.getString("jzpMenuKonta_text"));

    SF.centerFrame(forgstr,15,Res.getString("jzpMenuOrgJed_text"));

    SF.centerFrame(fzirorn,15,Res.getString("jzpMenuZirorn_text"));

    SF.centerFrame(fagenti,15,Res.getString("jzpMenuAgenti_text"));

    SF.centerFrame(fvalute,15,Res.getString("jzpMenuValute_text"));

    SF.centerFrame(ftecajevi,15,Res.getString("jzpMenuTec_text"));

    SF.centerFrame(flogos,15,Res.getString("jzpMenuLog_text"));

*/

  }

  private void jInit() {

    this.setText(Res.getString("jzpMenu_text"));

    jzpMenuOrgJed.setText(Res.getString("jzpMenuOrgJed_text"));

//    jzpMenuOrgJed.setAccelerator(javax.swing.KeyStroke.getKeyStroke(113,0, false));

    jzpMenuOrgJed.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuOrgJed_actionPerformed(e);

      }

    });

    jzpMenuKonta.setText(Res.getString("jzpMenuKonta_text"));

    jzpMenuKonta.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuKonta_actionPerformed(e);

      }

    });

    jzpMenuPpar.setText(Res.getString("jzpMenuPpar_text"));

    jzpMenuPpar.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuPpar_actionPerformed(e);

      }

    });

    jzpMenuPparHistory.setText("Povjest partnera");

    jzpMenuPparHistory.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuPparHist_actionPerformed(e);

      }

    });

    jzpMenuZirorn.setText(Res.getString("jzpMenuZirorn_text"));

    jzpMenuZirorn.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuZirorn_actionPerformed(e);

      }

    });

    jzpMenuAgenti.setText(Res.getString("jzpMenuAgenti_text"));

    jzpMenuAgenti.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuAgenti_actionPerformed(e);

      }

    });
    
    jzpMenuTele.setText("Telemarketeri / serviseri");

    jzpMenuTele.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuTele_actionPerformed(e);

      }

    });

    jzpMenuValute.setText(Res.getString("jzpMenuValute_text"));

    jzpMenuValute.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuValute_actionPerformed(e);

      }

    });

    jzpMenuTec.setText(Res.getString("jzpMenuTec_text"));

    jzpMenuTec.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuTec_actionPerformed(e);

      }

    });

    jzpMenuKor.setText(Res.getString("jzpMenuLog_text"));

    jzpMenuKor.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jzpMenuKor_actionPerformed(e);

      }

    });
    
    jzpPP.setText("Prijava posl. prostora");
    jzpPP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SF.showFrame("hr.restart.util.raFiskPoslovni", jzpPP.getText());
      }
    });
    

//

    jmKontaPar.setText("Konta za partnere");

    jmKontaPar.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmKontaPar_actionPerformed(e);

      }

    });

    jmBanke.setText("Banke");

    jmBanke.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmBanke_actionPerformed(e);

      }

    });

    jmKartice.setText("Kartice");

    jmKartice.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmKartice_actionPerformed(e);

      }

    });

    jmUgovori.setText("Ugovori");

    jmUgovori.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmUgovori_actionPerformed(e);

      }

    });

    jmVrstaUgovora.setText("Vrste Ugovora");

    jmVrstaUgovora.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmVrstaUgovora_actionPerformed(e);

      }

    });

    jmGrupPart.setText("Grupe partnera");

    jmGrupPart.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmGrupPart_actionPerformed(e);

      }

    });

    jmNacPlac.setText("Na\u010Dini pla\u0107anja");

    jmNacPlac.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmNacPlac_actionPerformed(e);

      }

    });

    jmNapomene.setText("Napomene");

    jmNapomene.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmNapomene_actionPerformed(e);

      }

    });

    jmVrTros.setText("Vrste tro\u0161ka");

    jmVrTros.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmVrTros_actionPerformed(e);

      }

    });

    jmRadnici.setText("Radnici");

    jmRadnici.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmRadnici_actionPerformed(e);

      }

    });

    jmZpZemlje.setText("Države");

    jmZpZemlje.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmZpZemlje_actionPerformed(e);

      }

    });

    jmZupanije.setText("Županije");

    jmZupanije.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmZupanije_actionPerformed(e);

      }

    });

    jmGradovi.setText("Gradovi");

    jmGradovi.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmGradovi_actionPerformed(e);

      }

    });

    jmSifarnici.setText("Šifarnici");

    jmSifarnici.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jmSifarnici_actionPerformed(e);

      }

    });

//

    this.add(jzpMenuOrgJed);

    this.add(jmZpZemlje);

    this.add(jmZupanije);

    this.add(jmGradovi);

    this.add(jzpMenuKonta);

    this.add(jzpMenuPpar);
    
    if (frmParam.getFrmParam().getParam("zapod","prHisPAr","N","Prikaz menu opcije \"Povijest Partnera\"").equals("D"))
      this.add(jzpMenuPparHistory);

    this.add(jzpMenuZirorn);

    this.addSeparator();

    this.add(jzpMenuValute);

    this.add(jzpMenuTec);

    this.add(jzpMenuAgenti);
    
    this.add(jzpMenuTele);

    this.addSeparator();

    //

    //this.add(jmKontaPar);

    this.add(jmUgovori);

    this.add(jmVrstaUgovora);

    this.add(jmBanke);

    this.add(jmKartice);

    this.add(jmGrupPart);

    this.add(jmNacPlac);

    this.add(jmNapomene);

    this.add(jmVrTros);

    this.add(jmRadnici);

    //

    this.addSeparator();

    this.add(jzpMenuKor);
    this.add(jzpPP);
    this.add(jmSifarnici);

  }



  public void jzpMenuOrgJed_actionPerformed(ActionEvent e) {

//    SF.showFrame(forgstr);

    SF.showFrame("hr.restart.zapod.frmOrgStr",15,Res.getString("jzpMenuOrgJed_text"));

  }



  void jzpMenuKonta_actionPerformed(ActionEvent e) {

//    SF.showFrame(fkonta);

    SF.showFrame("hr.restart.zapod.frmKonta",15,Res.getString("jzpMenuKonta_text"));

  }



  public void jzpMenuPpar_actionPerformed(ActionEvent e) {

//    SF.showFrame(fpartneri);

    SF.showFrame("hr.restart.zapod.frmPartneri",15,Res.getString("jzpMenuPpar_text"));

  }

  void jzpMenuPparHist_actionPerformed(ActionEvent e) { //TODO ovo
    SF.showFrame("hr.restart.zapod.frmPartHistory",15,"Povijest partnera");
  }


  void jzpMenuZirorn_actionPerformed(ActionEvent e) {

//    SF.showFrame(fzirorn);

    SF.showFrame("hr.restart.zapod.frmZirorn",15,Res.getString("jzpMenuZirorn_text"));

  }

  void jzpMenuValute_actionPerformed(ActionEvent e) {

//    SF.showFrame(fvalute);

    SF.showFrame("hr.restart.zapod.frmValute",15,Res.getString("jzpMenuValute_text"));

  }



  void jzpMenuTec_actionPerformed(ActionEvent e) {

    hr.restart.zapod.frmTecajevi ftecajevi =

      (frmTecajevi)SF.showFrame("hr.restart.zapod.frmTecajevi",15,Res.getString("jzpMenuTec_text"),false);

    ftecajevi.psFrm.showPreselect(ftecajevi,"Datum te\u010Daja");

  }



  void jzpMenuAgenti_actionPerformed(ActionEvent e) {

//    SF.showFrame(fagenti);

    SF.showFrame("hr.restart.zapod.frmAgenti",15,Res.getString("jzpMenuAgenti_text"));

  }
  
  void jzpMenuTele_actionPerformed(ActionEvent e) {

//  SF.showFrame(fagenti);

  SF.showFrame("hr.restart.zapod.frmTelemark",15,"Telemarketeri / serviseri");

  }



  void jzpMenuKor_actionPerformed(ActionEvent e) {

//    SF.showFrame(flogos);

    SF.showFrame("hr.restart.zapod.frmLogos",15,Res.getString("jzpMenuLog_text"));

  }

//

  void jmKontaPar_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmKontaPar", Res.getString("frmKontaPar_title"));

  }

  public void jmBanke_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmBanke", Res.getString("frmBanke_title"));

  }

  public void jmKartice_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmKartice", Res.getString("frmKartice_title"));

  }

  void jmUgovori_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmUgovori", Res.getString("frmUgovori_title"));

  }

  void jmVrstaUgovora_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmVrstaUgovora", jmVrstaUgovora.getText());

  }

  void jmGrupPart_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmGruppart", Res.getString("frmGrupPart_title"));

  }

  public void jmNacPlac_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmNacPl", Res.getString("frmNacPl_title"));

  }

  void jmNapomene_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmNapomene", Res.getString("frmNapomene_title"));

  }

  void jmVrTros_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmVrtros", Res.getString("frmVrtros_title"));

  }

  void jmRadnici_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmRadnici", "Radnici");

  }

  void jmSifarnici_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmSifrarnici", jmSifarnici.getText());

  }

  void jmZpZemlje_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmZapZemlje", jmZpZemlje.getText());

  }

  void jmZupanije_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.pl.frmZupanije", jmZupanije.getText());

  }

  void jmGradovi_actionPerformed(ActionEvent e) {

    SF.showFrame("hr.restart.zapod.frmMjesta", jmGradovi.getText());

  }

//

}