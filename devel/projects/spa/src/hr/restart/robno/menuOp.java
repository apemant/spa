/****license*****************************************************************
**   file: menuOp.java
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

import hr.restart.util.raLoader;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class menuOp extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.startFrame SF;
  JMenuItem jmSkladista = new JMenuItem();
  JMenuItem jmPorezi = new JMenuItem();
  JMenuItem jmJedMj = new JMenuItem();
  JMenuItem jmGrupaArt = new JMenuItem();
  JMenuItem jmArtikli = new JMenuItem();
  JMenuItem jmShemeKonta = new JMenuItem();
  JMenuItem jmShemeKontaOJ = new JMenuItem();
  JMenuItem jmRabati = new JMenuItem();
  JMenuItem jmShemeRab = new JMenuItem();
  JMenuItem jmZavTr = new JMenuItem();
  JMenuItem jmShemeZavTr = new JMenuItem();
  JMenuItem jmNormativi = new JMenuItem();
  JMenuItem jmSerBroj = new JMenuItem();
  JMenuItem jmSkupArt = new JMenuItem();
  JMenuItem jmDobArt = new JMenuItem();
  JMenuItem jmKupArt = new JMenuItem();
  JMenuItem jmFrankature = new JMenuItem();
  JMenuItem jmNamjene = new JMenuItem();
  JMenuItem jmNacOtp = new JMenuItem();
  JMenuItem jmVlasnici = new JMenuItem();
  JMenuItem jmKreditori = new JMenuItem();
  JMenuItem jmVrArt = new JMenuItem();

  public menuOp(hr.restart.util.startFrame startframe) {
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
    this.setText(res.getString("jmOsnPodaci_text"));
    jmSkladista.setText(res.getString("jmSkladista_text"));
    jmSkladista.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmSkladista_actionPerformed(e);
      }
    });
    jmPorezi.setText(res.getString("jmPorezi_text"));
    jmPorezi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPorezi_actionPerformed(e);
      }
    });
    jmJedMj.setText("Jednice mjere");
    jmJedMj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmJedMj_actionPerformed(e);
      }
    });
    jmGrupaArt.setText(res.getString("jmGrupaArt_text"));
    jmGrupaArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmGrupaArt_actionPerformed(e);
      }
    });
    jmArtikli.setText(res.getString("jmArtikli_text"));
    jmArtikli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmArtikli_actionPerformed(e);
      }
    });
    jmShemeKonta.setText("Sheme kontiranja");
    jmShemeKonta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmShemeKonta_actionPerformed(e);
      }
    });
    jmShemeKontaOJ.setText("Sheme kontiranja - OJ");
    jmShemeKontaOJ.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmShemeKontaOJ_actionPerformed(e);
      }
    });
    jmRabati.setText("Rabati");
    jmRabati.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRabati_actionPerformed(e);
      }
    });
    jmShemeRab.setText("Sheme rabata");
    jmShemeRab.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmShemeRab_actionPerformed(e);
      }
    });
    jmZavTr.setText("Zavisni tro\u0161kovi");
    jmZavTr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmZavTr_actionPerformed(e);
      }
    });
    jmShemeZavTr.setText("Sheme zavisnih tro\u0161kova");
    jmShemeZavTr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmShemeZavTr_actionPerformed(e);
      }
    });
    jmNormativi.setText("Normativi");
    jmNormativi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNormativi_actionPerformed(e);
      }
    });
    jmSerBroj.setText("Serijski brojevi");
    jmSerBroj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmSerBroj_actionPerformed(e);
      }
    });
    jmSkupArt.setText("Skupine artikala");
    jmSkupArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmSkupArt_actionPerformed(e);
      }
    });
    jmDobArt.setText("Dobavlja\u010Di-artikli");
    jmDobArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmDobArt_actionPerformed(e);
      }
    });
    jmKupArt.setText("Kupci-artikli");
    jmKupArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKupArt_actionPerformed(e);
      }
    });
    jmFrankature.setText("Paritet");
    jmFrankature.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFrankature_actionPerformed(e);
      }
    });
    jmNamjene.setText("Namjene robe");
    jmNamjene.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNamjene_actionPerformed(e);
      }
    });
    jmNacOtp.setText("Na\u010Dini otpreme");
    jmNacOtp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmNacOtp_actionPerformed(e);
      }
    });
    jmVlasnici.setText("Kupci - vlasnici");
    jmVlasnici.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmVlasnici_actionPerformed(e);
      }
    });
    jmKreditori.setText("Kreditori");
    jmKreditori.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKreditori_actionPerformed(e);
      }
    });
    jmVrArt.setText("Vrste artikla");
    jmVrArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmVrArt_actionPerformed(e);
      }
    });

    this.add(jmSkladista);
    this.add(jmPorezi);
    this.add(jmJedMj);
    this.add(jmGrupaArt);
    //this.add(jmVrArt);
    this.add(jmArtikli);
    this.addSeparator();
    this.add(jmShemeKonta);
    this.add(jmShemeKontaOJ);
    this.addSeparator();
    this.add(jmRabati);
    this.add(jmShemeRab);
    this.add(jmZavTr);
    this.add(jmShemeZavTr);
    this.add(jmNormativi);
    this.add(jmSerBroj);
    this.add(jmSkupArt);
    this.addSeparator();
    this.add(jmDobArt);
    this.add(jmKupArt);
    this.addSeparator();
    this.add(jmFrankature);
    this.add(jmNamjene);
    this.add(jmNacOtp);
    this.add(jmVlasnici);
//    this.add(jmKreditori);
  }
  void jmSkladista_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmSkladiste", res.getString("frmSkladiste_title"));
  }
  void jmPorezi_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmPorezi", res.getString("frmPorezi_title"));
  }
  void jmJedMj_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmJedMj", res.getString("frmJedMj_title"));
  }
  void jmGrupaArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmGrupArt",res.getString("frmGrupArt_title"));
  }
  void jmArtikli_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmArtikli", res.getString("frmArtikli_title"));
  }
  void jmShemeKonta_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmShemeKontaRobno fShKonta = (frmShemeKontaRobno) SF.showFrame("hr.restart.robno.frmShemeKontaRobno", 0, res.getString("frmShemeKonta_title"), false);
    fShKonta.go();
  }
  void jmShemeKontaOJ_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmShemeKontaRobnoOJ fShKontaOJ = (frmShemeKontaRobnoOJ) SF.showFrame("hr.restart.robno.frmShemeKontaRobnoOJ", 0, res.getString("frmShemeKonta_title"), false);
    fShKontaOJ.go();
  }
  void jmRabati_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmRabati", res.getString("frmRabati_title"));
  }
  void jmShemeRab_actionPerformed(ActionEvent e) {
   hr.restart.robno.frmShemaRab frmShemaRab = (hr.restart.robno.frmShemaRab)raLoader.load("hr.restart.robno.frmShemaRab");
   frmShemaRab.go();
  }
  void jmZavTr_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmZtr", res.getString("frmZavTr_title"));
  }
  void jmShemeZavTr_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmShemaZT", res.getString("frmShemaZT_title"));
  }
  void jmNormativi_actionPerformed(ActionEvent e) {
    frmNorme fNorme = (frmNorme)raLoader.load("hr.restart.robno.frmNorme");
    fNorme.go();
  }
  void jmSerBroj_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmSerBr", res.getString("dlgSerBrojevi_title"));
  }
  void jmSkupArt_actionPerformed(ActionEvent e) {
    hr.restart.robno.frmSkup_Art fSkupArt = (frmSkup_Art) SF.showFrame("hr.restart.robno.frmSkup_Art", 0, res.getString("frmSkupArt_title"), false);
    fSkupArt.go();
  }
  void jmDobArt_actionPerformed(ActionEvent e) {
    frmDob_Art fDobArt = (frmDob_Art)raLoader.load("hr.restart.robno.frmDob_Art");
    fDobArt.go();
  }
  void jmKupArt_actionPerformed(ActionEvent e) {
    frmKup_Art fKupArt = (frmKup_Art)raLoader.load("hr.restart.robno.frmKup_Art");
    fKupArt.go();
  }
  void jmFrankature_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmFranka", res.getString("frmFranka_title"));
  }
  void jmNamjene_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmNamjena", res.getString("frmNamjena_title"));
  }
  void jmNacOtp_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmNacOtp", res.getString("frmNacOtp_title"));
  }
  void jmVlasnici_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmVlasnik", res.getString("frmVlasnik_title"));
  }
  void jmKreditori_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmKreditori", "Kreditori");
  }
  void jmVrArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmVrArt", jmVrArt.getText());
  }
}
