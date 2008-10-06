/****license*****************************************************************
**   file: frmOK.java
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
package hr.restart.ok;

import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class frmOK extends startFrame {
  JMenuBar jmb = new JMenuBar();
  JMenu jmOsnovni = new JMenu();
  JMenu jmObrade = new JMenu();

  JMenuItem jmiKamate = new JMenuItem();
  JMenuItem jmiRacuni = new JMenuItem();
  JMenuItem jmiUplate = new JMenuItem();
  JMenuItem jmiObrKam = new JMenuItem();

  public frmOK() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jmiKamate.setText("Tablica kamata");
    jmiKamate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiKamate_actionPerformed();
      }
    });
    jmiRacuni.setText("Ra\u010Duni");
    jmiRacuni.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiRacuni_actionPerformed();
      }
    });
    jmiUplate.setText("Uplate");
    jmiUplate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiUplate_actionPerformed();
      }
    });
    jmOsnovni.setText("Osnovni podaci");
    jmObrade.setText("Obrada");
    jmiObrKam.setText("Obra\u010Dun kamata");
    jmiObrKam.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiObrKam_actionPerformed();
      }
    });

    jmb.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmb.add(jmOsnovni);
    jmb.add(jmObrade);
    jmOsnovni.add(jmiKamate);
    jmOsnovni.add(jmiRacuni);
    jmOsnovni.add(jmiUplate);
    jmObrade.add(jmiObrKam);
    setRaJMenuBar(jmb);
  }
  public void jmiKamate_actionPerformed() {
    showFrame("hr.restart.sk.frmKamate",15,jmiKamate.getText());
  }
  public void jmiRacuni_actionPerformed() {
//        frmKamateRac fkr = (frmKamateRac) showFrame("hr.restart.ok.frmKamateRac", 15, "", false);
//        fkr.pres.showPreselect(fkr, "Ra\u010Duni kupaca ili dobavlja\u010Da");
//        if (hr.restart.util.raLoader.isLoaderLoaded("hr.restart.ok.frmKamateRU") &&
	  if (hr.restart.util.raLoader.isLoaderLoaded("hr.restart.ok.frmKamateUpl") &&
	      frmKamateUpl.getInstance().isShowing())
	    frmKamateUpl.getInstance().rnvExit_action();
	
	  frmKamateRU fru = (frmKamateRU) showFrame("hr.restart.ok.frmKamateRU", 15, "", false);
	  fru.pres.showPreselect(fru, "Ra\u010Duni kupaca ili dobavlja\u010Da");
  }
  public void jmiUplate_actionPerformed() {
    if (hr.restart.util.raLoader.isLoaderLoaded("hr.restart.ok.frmKamateRU") &&
        frmKamateRU.getInstance().raMaster.isShowing()) {
      if (frmKamateRU.getInstance().getMasterSet().rowCount() > 0 ||
          frmKamateRU.getInstance().raDetail.isShowing()) {
        frmKamateRU.getInstance().jBStavke_actionPerformed(null);
        return;
      }
      frmKamateRU.getInstance().raMaster.rnvExit_action();
    }
    frmKamateUpl fku = (frmKamateUpl) showFrame("hr.restart.ok.frmKamateUpl", 15, "", false);
    fku.pres.showPreselect(fku, "Uplate kupaca ili dobavlja\u010Dima");
  }
  public void jmiObrKam_actionPerformed() {
    if (!hr.restart.util.raLoader.isLoaderLoaded("hr.restart.sk.raObrKamata"))
      hr.restart.util.raLoader.load("hr.restart.sk.raObrKamata");
    if (hr.restart.sk.raObrKamata.getInstance().isBusy()) return;
    if (!hr.restart.sk.raObrKamata.getInstance().isStandAlone()) {
      if (hr.restart.sk.raObrKamata.getInstance().isShowing())
        hr.restart.sk.raObrKamata.getInstance().hide();
      hr.restart.sk.raObrKamata.getInstance().setStandAlone(true);
    }
    showFrame("hr.restart.sk.raObrKamata", "Obra\u010Dun kamata");
  }
}

