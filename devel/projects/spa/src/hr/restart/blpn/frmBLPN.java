/****license*****************************************************************
**   file: frmBLPN.java
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
package hr.restart.blpn;

import hr.restart.util.PreSelect;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class frmBLPN extends startFrame {
  JMenuBar jMenuBar = new JMenuBar();
  JMenu jmBlagajna = new JMenu();
  JMenuItem jmiBlag = new JMenuItem();
  JMenuItem jmiUplIspl = new JMenuItem();
  JMenuItem jmiBlagIzv = new JMenuItem();
  JMenu jmOsnovni = new JMenu();
  JMenuItem jmiShemeKontaBLPN = new JMenuItem();
  JMenuItem jmiVrskBLPN = new JMenuItem();
  JMenuItem jmiZemlje = new JMenuItem();
  JMenuItem jmiPSredstva = new JMenuItem();
  JMenuItem jmiRelacije = new JMenuItem();
  JMenuItem jmiParamBLPN = new JMenuItem();
  JMenu jmPN = new JMenu();
  JMenuItem jmiPrijavaPN = new JMenuItem();
  JMenuItem jmiObracunPN = new JMenuItem();
  JMenuItem jmiArhivaPN = new JMenuItem();
  JMenuItem jmiAkontacijaPN = new JMenuItem();
  JMenuItem jmiRazlikaPN = new JMenuItem();
  JMenu jmIspisi = new JMenu();
  JMenuItem jmiKarticaRadnika = new JMenuItem();
  private JMenuItem jmiTemeljnicaBlagajne = new JMenuItem();
  private JMenuItem jmiTemeljnicaPN = new JMenuItem();

  public frmBLPN() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jmBlagajna.setText("Blagajna");
    jmiBlag.setText("Blagajne");
    jmiBlag.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBlag_actionPerformed(e);
      }
    });
    jmiUplIspl.setText("Uplata / Isplata");
    jmiUplIspl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiUplIspl_actionPerformed(e);
      }
    });
    jmiBlagIzv.setText("Blagajni\u010Dki izvještaji");
    jmiBlagIzv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiBlagIzv_actionPerformed(e);
      }
    });
    jmOsnovni.setText("Osnovni podaci");
    jmiShemeKontaBLPN.setText("Sheme knjiženja");
    jmiShemeKontaBLPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiShemeKontaBLPN_actionPerformed(e);
      }
    });
    jmiVrskBLPN.setText("Vrste shema knjiženja");
    jmiVrskBLPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiVrskBLPN_actionPerformed(e);
      }
    });
    jmiZemlje.setText("Zemlje");
    jmiZemlje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiZemlje_actionPerformed(e);
      }
    });
    jmiPSredstva.setText("Prijevozna sredstva");
    jmiPSredstva.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPSredstva_actionPerformed(e);
      }
    });
    jmiRelacije.setText("Relacije");
    jmiRelacije.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRelacije_actionPerformed(e);
      }
    });
    jmiParamBLPN.setText("Parametri");
    jmiParamBLPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiParamBLPN_actionPerformed(e);
      }
    });
    jmPN.setText("Putni nalozi");
    jmiPrijavaPN.setText("Prijava");
    jmiPrijavaPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiPrijavaPN_actionPerformed(e);
      }
    });
    jmiObracunPN.setText("Obra\u010Dun");
    jmiObracunPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiObracunPN_actionPerformed(e);
      }
    });
    jmiArhivaPN.setText("Arhiva");
    jmiArhivaPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiArhivaPN_actionPerformed(e);
      }
    });
    jmiAkontacijaPN.setText("Isplata akontacije po PN");
    jmiAkontacijaPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiAkontacijaPN_actionPerformed(e);
      }
    });
    jmiRazlikaPN.setText("Uplata / isplata razlike po PN");
    jmiRazlikaPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRazlikaPN_actionPerformed(e);
      }
    });
    jmIspisi.setText("Izvještaji");
    jmiKarticaRadnika.setText("Kartica djelatnika");
    jmiKarticaRadnika.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKarticaRadnika_actionPerformed(e);
      }
    });
    jmiTemeljnicaBlagajne.setText("Temeljnica blagajne");
    jmiTemeljnicaBlagajne.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiTemeljnicaBlagajne_actionPerformed(e);
      }
    });
    jmiTemeljnicaPN.setText("Temeljnica putnog naloga");
    jmiTemeljnicaPN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiTemeljnicaPN_actionPerformed(e);
      }
    });
    jMenuBar.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jMenuBar.add(jmOsnovni);
    jMenuBar.add(jmBlagajna);
    jMenuBar.add(jmPN);
    jMenuBar.add(jmIspisi);
    jmBlagajna.add(jmiBlag);
    jmBlagajna.add(jmiUplIspl);
    jmBlagajna.add(jmiBlagIzv);
    jmBlagajna.add(jmiAkontacijaPN);
    jmBlagajna.add(jmiRazlikaPN);
    jmOsnovni.add(jmiZemlje);
    jmOsnovni.add(jmiPSredstva);
    jmOsnovni.add(jmiRelacije);
    jmOsnovni.add(jmiShemeKontaBLPN);
    jmOsnovni.add(jmiVrskBLPN);
    jmOsnovni.add(jmiParamBLPN);
    jmPN.add(jmiPrijavaPN);
    jmPN.add(jmiObracunPN);
    jmPN.add(jmiArhivaPN);
    jmIspisi.add(jmiKarticaRadnika);
    jmIspisi.add(jmiTemeljnicaBlagajne);
    jmIspisi.add(jmiTemeljnicaPN);
    setRaJMenuBar(jMenuBar);
  }

  void jmiShemeKontaBLPN_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.frmShemeKontaBLPN",jmiShemeKontaBLPN.getText());
  }

  void jmiVrskBLPN_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.frmVrskBLPN",jmiVrskBLPN.getText());
  }

  void jmiBlag_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.frmBlag",jmiBlag.getText());
  }

  void jmiUplIspl_actionPerformed(ActionEvent e) {
    PreSelect.showPreselect("hr.restart.blpn.presUplIspl","hr.restart.blpn.frmUplIspl","Uplata / Isplata");
  }

  void jmiBlagIzv_actionPerformed(ActionEvent e) {
    PreSelect.showPreselect("hr.restart.blpn.presUplIspl","hr.restart.blpn.frmBlagIzv","Blagajni\u010Dki izvještaji");
  }

  void jmiZemlje_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.frmZemlje",jmiZemlje.getText());
  }
  
  void jmiPSredstva_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.frmPSredstva",jmiPSredstva.getText());
  }

  void jmiRelacije_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.frmRelacije",jmiRelacije.getText());
  }

  void jmiParamBLPN_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.frmParamBLPN",jmiParamBLPN.getText());
  }

  void jmiAkontacijaPN_actionPerformed(ActionEvent e) {
    PreSelect.showPreselect("hr.restart.blpn.presAkontacijaPN","hr.restart.blpn.frmAkontacijaPN",jmiAkontacijaPN.getText());
  }

  void jmiRazlikaPN_actionPerformed(ActionEvent e) {
    PreSelect.showPreselect("hr.restart.blpn.presRazlikaPN","hr.restart.blpn.frmRazlikaPN","Uplata / isplata razlike po putnom nalogu");
  }

  void jmiPrijavaPN_actionPerformed(ActionEvent e) {
//    showFrame("hr.restart.blpn.frmPrijavaPN","Prijava putnog naloga");
    PreSelect.showPreselect("hr.restart.blpn.PresPrijavaPn","hr.restart.blpn.frmPrijavaPN","Prijava putnog naloga");
  }

  void jmiObracunPN_actionPerformed(ActionEvent e) {
    PreSelect.showPreselect("hr.restart.blpn.presPN","hr.restart.blpn.frmObracunPN_V1_1","Obra\u010Duni putnih naloga");
  }

  void jmiArhivaPN_actionPerformed(ActionEvent e) {
    PreSelect.showPreselect("hr.restart.blpn.presPNArh","hr.restart.blpn.frmArhivaPN","Arhiva pitnih naloga" );
  }

  void jmiKarticaRadnika_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.ispKarticaRadnikaBLPN","Ispis kartice djelatnika");
  }

  void jmiTemeljnicaBlagajne_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.ispTemBlag",jmiTemeljnicaBlagajne.getText());
  }

  void jmiTemeljnicaPN_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.blpn.ispTemPN",jmiTemeljnicaBlagajne.getText());
  }

}