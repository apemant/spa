/****license*****************************************************************
**   file: frmPregledArhive.java
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

import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.startFrame;
import hr.restart.util.reports.JTablePrintRun;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class frmPregledArhive extends raFrame {
  raOdbici rod = raOdbici.getInstance();
  int mode;
  String oldString1;
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private BorderLayout borderLayout1 = new BorderLayout();
  JTablePrintRun printer = new JTablePrintRun();
  hr.restart.util.raJPTableView jptv = new hr.restart.util.raJPTableView() {
         public void mpTable_doubleClicked() {
                jptv_doubleClick(mode);
         }
  };
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
//      pressOK();
    }
    public void jPrekid_actionPerformed() {
//      pressCancel();
    }
  };

  public frmPregledArhive(com.borland.dx.sql.dataset.QueryDataSet qds, int mod, String title, String oldStr) {
    mode=mod;
    oldString1=oldStr;
    try {
      this.setSize(600,400);
      this.setTitle(title);
      jptv.setDataSet(qds);
      jptv.enableEvents(true);
      if (mode==1) { // Pregled obraèuna
        jptv.setVisibleCols(new int[] {0,1,2});
        jptv.getNavBar().addOption(new raNavAction("Detaljno", raImages.IMGSTAV, KeyEvent.VK_F6) {
          public void actionPerformed(ActionEvent e) {
            frmPregledArhive.this.jptv_doubleClick(1);
          }
        });
      }
      else if (mode==2) { // Pregled po org. jedinicama
        jptv.setVisibleCols(new int[] {3,7});
        jptv.getNavBar().addOption(new raNavAction("Detaljno", raImages.IMGSTAV, KeyEvent.VK_F6) {
          public void actionPerformed(ActionEvent e) {
            frmPregledArhive.this.jptv_doubleClick(2);
          }
        });
        jptv.addTableModifier(new hr.restart.swing.raTableColumnModifier("CORG", new String[] {"CORG", "NAZIV"}, dm.getAllOrgstruktura()));
        jptv.setKumTak(true);
        jptv.setStoZbrojiti(new String[] {"SATI", "BRUTO", "DOPRINOSI", "NETO", "NEOP", "ISKNEOP", "POROSN", "POR1", "POR2", "POR3", "PRIR", "NETO2", "NAKNADE", "NETOPK", "KREDITI", "NARUKE"});
        jptv.setNaslovi(new String[] {"Sati", "Bruto", "Doprinosi", "Dohodak", "Neoporezivo", "Iskorišteni neoporezivi", "Por. osnovica", "Porez 1", "Porez 2", "Porez 3", "Prirez", "Iznos nakon oporezivanja", "Naknade", "Iznos prije kredita", "Krediti", "Za isplatu"});
      }
      else if (mode==3) { // Pregled po radnicima
        jptv.setVisibleCols(new int[] {3,4,5});
        jptv.getNavBar().addOption(new raNavAction("Doprinosi", raImages.IMGZOOM, KeyEvent.VK_F2) {
          public void actionPerformed(ActionEvent e) {
            frmPregledArhive.this.detailView('D');
          }
        });
        jptv.getNavBar().addOption(new raNavAction("Olakšice", raImages.IMGTIP, KeyEvent.VK_F3) {
          public void actionPerformed(ActionEvent e) {
            frmPregledArhive.this.detailView('O');
          }
        });
        jptv.getNavBar().addOption(new raNavAction("Krediti", raImages.IMGHISTORY, KeyEvent.VK_F4) {
          public void actionPerformed(ActionEvent e) {
            frmPregledArhive.this.detailView('K');
          }
        });
        jptv.getNavBar().addOption(new raNavAction("Doprinosi poduzeæa", raImages.IMGCOMPOSEMAIL, KeyEvent.VK_F6) {
          public void actionPerformed(ActionEvent e) {
            frmPregledArhive.this.detailView('P');
          }
        });
        jptv.getNavBar().addOption(new raNavAction("Primanja", raImages.IMGSTAV, KeyEvent.VK_F7) {
          public void actionPerformed(ActionEvent e) {
            frmPregledArhive.this.jptv_doubleClick(3);
          }
        });
        jptv.addTableModifier(new hr.restart.swing.raTableColumnModifier("CRADNIK", new String[] {"CRADNIK", "IME", "PREZIME"}, dm.getAllRadnici()));
        jptv.setKumTak(true);
        jptv.setStoZbrojiti(new String[] {"SATI", "BRUTO", "DOPRINOSI", "NETO", "NEOP", "ISKNEOP", "POROSN", "POR1", "POR2", "POR3", "PRIR", "NETO2", "NAKNADE", "NETOPK", "KREDITI", "NARUKE"});
        jptv.setNaslovi(new String[] {"Sati", "Bruto", "Doprinosi", "Dohodak", "Neoporezivo", "Iskorišteni neoporezivi", "Por. osnovica", "Porez 1", "Porez 2", "Porez 3", "Prirez", "Iznos nakon oporezivanja", "Naknade", "Iznos prije kredita", "Krediti", "Za isplatu"});
      }
      else if (mode==4) { // Pregled zarada
        jptv.setVisibleCols(new int[] {4,7,9});
        jptv.addTableModifier(new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim()));
        jptv.setKumTak(true);
        jptv.setStoZbrojiti(new String[] {"SATI", "KOEF", "BRUTO", "DOPRINOSI", "NETO"});
        jptv.setNaslovi(new String[] {"Sati", "Koeficijent", "Bruto", "Doprinosi", "Iznos za oporezivanje"});
      }
      else if (mode==5) { // Pregled doprinosa radnika
        jptv.setVisibleCols(new int[] {8,12,13,14});
        jptv.addTableModifier(new hr.restart.swing.raTableColumnModifier("CVRODB", new String[] {"CVRODB", "OPISVRODB"}, dm.getVrsteodb()));
        jptv.setKumTak(true);
        jptv.setStoZbrojiti(new String[] {"OBRSTOPA", "OBRIZNOS"});
        jptv.setNaslovi(new String[] {"Stopa", "Iznos"});
      }
      else if (mode==6) { // Pregled olakšica
        jptv.setVisibleCols(new int[] {8,12,13,14});
        jptv.addTableModifier(new hr.restart.swing.raTableColumnModifier("CVRODB", new String[] {"CVRODB", "OPISVRODB"}, dm.getVrsteodb()));
        jptv.setKumTak(true);
        jptv.setStoZbrojiti(new String[] {"OBRSTOPA", "OBRIZNOS"});
        jptv.setNaslovi(new String[] {"Stopa", "Iznos"});
      }
      else if (mode==7) { // Pregled kredita
        jptv.setVisibleCols(new int[] {8,12,13,14});
        jptv.addTableModifier(new hr.restart.swing.raTableColumnModifier("CVRODB", new String[] {"CVRODB", "OPISVRODB"}, dm.getVrsteodb()));
        jptv.setKumTak(true);
        jptv.setStoZbrojiti(new String[] {"OBRSTOPA", "OBRIZNOS"});
        jptv.setNaslovi(new String[] {"Stopa", "Iznos"});
      }
      else if (mode==8) { // Pregled doprinosa poduzeæa
        jptv.setVisibleCols(new int[] {8,12,13,14});
        jptv.addTableModifier(new hr.restart.swing.raTableColumnModifier("CVRODB", new String[] {"CVRODB", "OPISVRODB"}, dm.getVrsteodb()));
        jptv.setKumTak(true);
        jptv.setStoZbrojiti(new String[] {"OBRSTOPA", "OBRIZNOS"});
        jptv.setNaslovi(new String[] {"Stopa", "Iznos"});
      }
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    System.out.println("jbinit");
    jptv.getColumnsBean().setSaveSettings(false);
    this.getContentPane().setLayout(borderLayout1);
    jptv.getNavBar().removeStandardOptions(new int[] {raNavBar.ACTION_ADD, raNavBar.ACTION_UPDATE, raNavBar.ACTION_DELETE});
    this.getContentPane().add(jptv, BorderLayout.CENTER);
/*    jptv.getNavBar().addOption(new raNavAction("Detaljno", raImages.IMGSTAV, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        frmPregledArhive.this.jptv_doubleClick();
      }
    });*/
    jptv.getNavBar().addOption(new raNavAction("Ispis", raImages.IMGPRINT, KeyEvent.VK_F5) {
      public void actionPerformed(ActionEvent e) {
        frmPregledArhive.this.print();
      }
    });
    jptv.getNavBar().addOption(new raNavAction("Izlaz", raImages.IMGEXIT, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        frmPregledArhive.this.hide();
      }
    });

    this.jptv.initKeyListener(this);
  }
  private void print() {
    jptv.getNavBar().getColBean().setRaJdbTable(jptv.getMpTable());
    printer.setColB(jptv.getNavBar().getColBean());
    printer.setRTitle(this.getTitle());
    printer.runIt();
  }

  public void show() {
    System.out.println("show");
    try {
      jptv.getColumnsBean().initialize();

    }
    catch (Exception ex) {

    }
    startFrame.getStartFrame().centerFrame(this,0,getTitle());
    super.show();
    System.out.println("Aftershow");
  }
  private void jptv_doubleClick(int mod) {
    com.borland.dx.sql.dataset.QueryDataSet qds;
    if (mod==1) { // pregled po organizacijskim jedinicama i vrsti primanja
/*      com.borland.dx.sql.dataset.QueryDataSet qds= Util.getNewQueryDataSet(sjQuerys.getOrgsplFromArhiv(oldString1));
      qds.close();
      qds.setColumns(dm.getKumulorgarh().cloneColumns());
      */
      String filter="godobr="+jptv.getDataSet().getShort("GODOBR")+" and mjobr="+jptv.getDataSet().getShort("MJOBR")+" and rbrobr="+jptv.getDataSet().getShort("RBROBR")+" and knjig= '"+dlgGetKnjig.getKNJCORG()+"'";
      System.out.println("Filter: "+filter);
      qds = hr.restart.baza.Kumulorgarh.getDataModule().getFilteredDataSet(filter);
      qds.open();
      hr.restart.pl.frmPregledArhive fpa = new hr.restart.pl.frmPregledArhive(qds, 2, "Pregled kumulativa po org. jedinicama i vrstama rada", "");
      startFrame.getStartFrame().showFrame(fpa);
    }
    else if (mod==2) { // Pregled po radnicima
      String filter="godobr="+jptv.getDataSet().getShort("GODOBR")+" and mjobr="+jptv.getDataSet().getShort("MJOBR")+" and rbrobr="+jptv.getDataSet().getShort("RBROBR")+" and corg='"+jptv.getDataSet().getString("CORG")+"'";
      System.out.println("Filter 2: "+filter);
      qds = hr.restart.baza.Kumulradarh.getDataModule().getFilteredDataSet(filter);
      qds.open();
      hr.restart.pl.frmPregledArhive fpa = new hr.restart.pl.frmPregledArhive(qds, 3, "Pregled radnika", "");
      startFrame.getStartFrame().showFrame(fpa);
    }
    else if (mod==3) { // Pregled zarada
      String filter="godobr="+jptv.getDataSet().getShort("GODOBR")+" and mjobr="+jptv.getDataSet().getShort("MJOBR")+" and rbrobr="+jptv.getDataSet().getShort("RBROBR")+" and cradnik='"+jptv.getDataSet().getString("CRADNIK")+"'";
      System.out.println("Filter 3: "+filter);
      qds = hr.restart.baza.Primanjaarh.getDataModule().getFilteredDataSet(filter);
      qds.open();
      hr.restart.pl.frmPregledArhive fpa = new hr.restart.pl.frmPregledArhive(qds, 4, "Pregled primanja", "");
      startFrame.getStartFrame().showFrame(fpa);
    }
  }
  private void detailView(char mod) {
    rod.setObrRange(jptv.getDataSet().getShort("GODOBR"), jptv.getDataSet().getShort("MJOBR"), jptv.getDataSet().getShort("RBROBR"));
    if (mod=='D') { // Doprinosi radnika
      hr.restart.pl.frmPregledArhive fpa = new hr.restart.pl.frmPregledArhive(rod.getDoprinosiRadnik(jptv.getDataSet().getString("CRADNIK"), rod.ARH), 5, "Pregled doprinosa radnika", "");
      startFrame.getStartFrame().showFrame(fpa);
    }
    else if (mod=='O') { // Olakšice
      hr.restart.pl.frmPregledArhive fpa = new hr.restart.pl.frmPregledArhive(rod.getOlaksice(jptv.getDataSet().getString("CRADNIK"), rod.ARH), 6, "Pregled olakšica", "");
      startFrame.getStartFrame().showFrame(fpa);
    }
    else if (mod=='K') { // Krediti
      hr.restart.pl.frmPregledArhive fpa = new hr.restart.pl.frmPregledArhive(rod.getKrediti(jptv.getDataSet().getString("CRADNIK"), rod.ARH), 7, "Pregled kredita", "");
      startFrame.getStartFrame().showFrame(fpa);
    }
    else if (mod=='P') { // Doprinosi poduzeæa
      hr.restart.pl.frmPregledArhive fpa = new hr.restart.pl.frmPregledArhive(rod.getDoprinosiNa(jptv.getDataSet().getString("CRADNIK"), rod.ARH), 8, "Pregled doprinosa poduzeæa", "");
      startFrame.getStartFrame().showFrame(fpa);
    }
  }
}