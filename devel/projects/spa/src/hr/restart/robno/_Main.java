/****license*****************************************************************
**   file: _Main.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.startFrame;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class _Main extends hr.restart.util.startFrame {
  static java.math.BigDecimal sto = new java.math.BigDecimal(100).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  static java.math.BigDecimal nul = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  static java.math.BigDecimal one = new java.math.BigDecimal(1).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  static java.math.BigDecimal _sto = new java.math.BigDecimal(0.01).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
  public static hr.restart.robno.menuOp opMnu;
  public static hr.restart.robno.menuInv invMnu;
  public static hr.restart.robno.menuUpit upitMnu;
  public static hr.restart.robno.menuSklad skladMnu;
  public static hr.restart.robno.menuNabava nabavaMnu;
  public static hr.restart.robno.menuMalo maloMnu;
  public static hr.restart.robno.menuIzvMalo maloIzvMnu;
  public static hr.restart.robno.menuVele veleMnu;
  public static hr.restart.robno.menuIzvVele veleIzvMnu;
  public static hr.restart.robno.menuMeskla mesklaMnu;
  public static hr.restart.robno.menuNar narMnu;
  public static hr.restart.robno.menuObrade obradeMnu;
  public static hr.restart.robno.menuServis servisMnu;
  public static hr.restart.robno.menuKonsig konsigMnu;
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  /**
   * 'P' - primka-kalkulacija
   * 'D' - povratnica-tereæenje (dobavljaèu)
   * 'S' - poèetno stanje
   * 'R' - primka (bez kalkulacije)
   * 'K' - kalkulacija
   * 
   *  you've gotta be kiddin' me...
   */
  // public static char prSTAT;
  dM dm;
  JPanel contentPane;
  JMenuBar jmMain = new JMenuBar();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JMenuItem jmDelAll = new JMenuItem();
  JMenu jmSys = new JMenu();
  JMenu jmIzvjestaji = new JMenu();
  JMenuItem jmOstaliRep = new JMenuItem();
  JMenuItem jmRecalc = new JMenuItem();
  JMenuItem jmProv = new JMenuItem();
  JMenuItem jmPopravakRezKol = new JMenuItem();
  JMenuItem jmKarticaTest = new JMenuItem();
  JMenuItem jmProvjeraPopravakAgenata = new JMenuItem();
  JMenuItem jmValidRobnoDoc = new JMenuItem();
  JMenuItem jmFormPS = new JMenuItem();
  JMenuItem jmFormIL = new JMenuItem();
  JMenuItem jmPopravakZavTr = new JMenuItem();  
  JMenu jmPrometi = new JMenu();
  JMenu jmProdaja = new JMenu();
  JMenu jmRacuni = new JMenu();
//  JMenu jMenu1 = new JMenu();
  private JMenuItem jMenuItem1 = new JMenuItem();

  public _Main(int i) {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
      this.ShowMe(false, res.getString("title_robno_text"));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
// ovo sam ti ja dodao tako da mogu uloadati _Main a da mi ne prokaze ekran
// treba mi konstruktor bez parametara za tu svrhu zbog komande Class.newinstance

  public _Main() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      this.jbInit();
// u tom je problem, kuish? this.ShowMe(false, res.getString("title_robno_text"));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static javax.swing.JMenu getOpMenu(startFrame startframe) {
    opMnu = new menuOp(startframe);
    return opMnu;
  }
  public static javax.swing.JMenu getInvMenu(startFrame startframe) {
    invMnu = new menuInv(startframe);
    return invMnu;
  }
  public static javax.swing.JMenu getUpitMenu(startFrame startframe) {
    upitMnu = new menuUpit(startframe);
    return upitMnu;
  }
  public static javax.swing.JMenu getSkladMenu(startFrame startframe) {
    skladMnu = new menuSklad(startframe);
    return skladMnu;
  }
  public static javax.swing.JMenu getNabavaMenu(startFrame startframe) {
    nabavaMnu = new menuNabava(startframe);
    return nabavaMnu;
  }
  public static javax.swing.JMenu getMaloMenu(startFrame startframe) {
    maloMnu = new menuMalo(startframe);
    return maloMnu;
  }
  public static javax.swing.JMenu getIzvMaloMenu(startFrame startframe) {
    maloIzvMnu = new menuIzvMalo(startframe);
    return maloIzvMnu;
  }
  public static javax.swing.JMenu getVeleMenu(startFrame startframe) {
    veleMnu = new menuVele(startframe);
    return veleMnu;
  }
  public static javax.swing.JMenu getIzvVeleMenu(startFrame startframe) {
    veleIzvMnu = new menuIzvVele(startframe);
    return veleIzvMnu;
  }
  public static javax.swing.JMenu getMesklaMenu(startFrame startframe) {
    mesklaMnu = new menuMeskla(startframe);
    return mesklaMnu;
  }
  public static javax.swing.JMenu getNarMenu(startFrame startframe) {
    narMnu = new menuNar(startframe);
    return narMnu;
  }
  public static javax.swing.JMenu getObradeMenu(startFrame startframe) {
    obradeMnu = new menuObrade(startframe);
    return obradeMnu;
  }
  public static javax.swing.JMenu getServisMenu(startFrame startframe) {
    servisMnu = new menuServis(startframe);
    return servisMnu;
  }
  
  public static javax.swing.JMenu getKonsigMenu(startFrame startframe) {
	    konsigMnu = new menuKonsig(startframe);
	    return konsigMnu;
  }
  
  
  private void jbInit() throws Exception  {
    //setIconImage(Toolkit.getDefaultToolkit().createImage(_Main.class.getResource("[Your Icon]")));
    dm = dM.getDataModule();
    contentPane = (JPanel) this.getContentPane();
    contentPane.setBackground(SystemColor.desktop);
    jmSys.setText("Pomo\u0107ne funkcije");
    jmDelAll.setText("Brisanje baze");
    jmDelAll.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmDelAll_actionPerformed(e);
      }
    });
    jmIzvjestaji.setText("Izvje\u0161taji");
    jmOstaliRep.setText("Ostali izvješaji");
    jmOstaliRep.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmOstaliRep_actionPerformed(e);
      }
    });
    jmRecalc.setText("Rekalkulacija stanja");
    jmRecalc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRecalc_actionPerformed(e);
      }
    });
    jmProv.setText("Provjera matematike stanja");
    jmProv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmProv_actionPerformed(e);
      }
    });
    jmProvjeraPopravakAgenata.setText("Provjera i popravak agenata na dokumentima");
    jmProvjeraPopravakAgenata.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPrPop_actionPerformed(e);
      }
    });
    jmPopravakRezKol.setText("Popravak rezerviranih kolièina");
    jmPopravakRezKol.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPopravakRezKol_actionPerformed(e);
      }
    });
    jmPopravakZavTr.setText("Baterija testova i ispravaka");
    jmPopravakZavTr.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	jmPopravakZavTr_actionPerformed(e);
        }
      });
    jmKarticaTest.setText("Provjera kartice");
    jmKarticaTest.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKarticaTest_actionPerformed(e);
      }
    });
    jmValidRobnoDoc.setText("Provjera i popravak dokumenata");
    jmValidRobnoDoc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmValidRobnoDoc_actionPerformed(e);
      }
    });
    jmFormPS.setText("Formiranje dokumenta poèetno stanje");
    jmFormPS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFormPS_actionPerformed(e);
      }
    });
    jmFormIL.setText("Formiranje inventurne liste");
    jmFormIL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFormIL_actionPerformed(e);
      }
    });
    jmPrometi.setText("Prometi");
    jmProdaja.setText("Prodaja");
    jmRacuni.setText("Obrada raèuna");
//    jMenu1.setText("Obrade");
    jMenuItem1.setText("Test");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuItem1_actionPerformed(e);
      }
    });
    jmMain.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmMain.add(hr.restart.robno._Main.getOpMenu(this));
    jmMain.add(jmPrometi);
    jmMain.add(hr.restart.robno._Main.getUpitMenu(this));
    jmMain.add(hr.restart.robno._Main.getIzvVeleMenu(this));
    jmMain.add(hr.restart.robno._Main.getInvMenu(this));
    jmMain.add(hr.restart.robno._Main.getObradeMenu(this));
    jmSys.add(jmDelAll);
    jmSys.add(jmRecalc);
    jmSys.add(jmProv);
    jmSys.add(jmPopravakRezKol);
    jmSys.add(jmPopravakZavTr);
    jmSys.add(jmValidRobnoDoc);
    jmSys.add(jmFormPS);
    jmSys.add(jmFormIL);
    jmSys.add(jmKarticaTest);
    jmSys.add(jmProvjeraPopravakAgenata);
    setToolMenu(jmSys);
    jmIzvjestaji.add(jmOstaliRep);
    jmPrometi.add(hr.restart.robno._Main.getSkladMenu(this));
    jmPrometi.add(hr.restart.robno._Main.getNabavaMenu(this));
    jmPrometi.add(hr.restart.robno._Main.getVeleMenu(this));
    jmPrometi.add(hr.restart.robno._Main.getMesklaMenu(this));
    jmPrometi.add(hr.restart.robno._Main.getNarMenu(this));
    jmPrometi.add(hr.restart.robno._Main.getKonsigMenu(this));
    if (frmParam.getParam("robno", "distron","N", "Koristiti modul distribucije D/N").equals("D")) {
      jmMain.add(new hr.restart.distrib.menuDistrib(this)); //ai 2013
    }
    this.setRaJMenuBar(jmMain);
  }
  void jmDelAll_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.robno.raDelAll", jmDelAll.getText());
  }
  void showSheet() {
    JOptionPane.showConfirmDialog(null,"Opcija nije implementirana !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
  }
  void jmOstaliRep_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.robno.frmRobnoReportList", jmOstaliRep.getText());
  }
  void jMenuItem39_actionPerformed(ActionEvent e) {
    this.showSheet();
  }
  void jMenuItem40_actionPerformed(ActionEvent e) {
    this.showSheet();
  }
  void jMenuItem41_actionPerformed(ActionEvent e) {
    this.showSheet();
  }
  void jMenuItem42_actionPerformed(ActionEvent e) {
    this.showSheet();
  }
  void jmRecalc_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.robno.raRekalkulacijaStanja", jmRecalc.getText());
  }
  void jmProv_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.robno.raProvjeraStanja", jmProv.getText());
  }
  void jmPrPop_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.util.FrmProvjeraPopravakAgenata", jmProvjeraPopravakAgenata.getText()); //TODO
  }
  void jmPopravakRezKol_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.robno.raPopravakRezKol", jmPopravakRezKol.getText());
  }
  void jmPopravakZavTr_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.robno.RepareZavTr", jmPopravakZavTr.getText());
  }  
  void jmKarticaTest_actionPerformed(ActionEvent e){
    this.showFrame("hr.restart.robno.frmTestValidKartica", jmKarticaTest.getText());
  }
  void jmValidRobnoDoc_actionPerformed(ActionEvent e){
    this.showFrame("hr.restart.robno.testValidDocs", jmValidRobnoDoc.getText());
  }
  void jmFormPS_actionPerformed(ActionEvent e) {
    this.showFrame("hr.restart.robno.raFormPS", jmFormPS.getText());
  }
  void jmFormIL_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.robno.frmInvLista", jmFormIL.getText());
  }
  public startFrame get_Main() {
    return this;
  }
  void jMenuItem1_actionPerformed(ActionEvent e) {
    showFrame("hr.restart.robno.raTestis", "Test komponenta za testiranje");
  }
}
