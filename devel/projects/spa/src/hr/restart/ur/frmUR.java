/****license*****************************************************************
**   file: frmUR.java
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
package hr.restart.ur;

import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
/**
 * Urudzbeno
 * @author andrej
 *
 */
public class frmUR extends startFrame {
  JMenuBar jmb = new JMenuBar();
  JMenu jmObrade = new JMenu();
  JMenuItem jmiDokumenti = new JMenuItem();
  JMenuItem jmiDokumentiOK = new JMenuItem();
  JMenuItem jmiIzvjestaji = new JMenuItem();
  public frmUR() {
    try {
      jInit();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  private void jInit() {
    jmb.add(hr.restart.zapod.frmZapod.getJzpMenu(this));
    jmObrade.setText("Obrade");
    jmiDokumenti.setText("Dokumenti");
    jmiIzvjestaji.setText("Izvještaji");
    jmiDokumentiOK.setText("Storno dokumenata");
    jmb.add(jmObrade);
    jmObrade.add(jmiDokumenti);
    //jmObrade.add(jmiDokumentiOK);
    jmObrade.addSeparator();
    jmObrade.add(jmiIzvjestaji);
    jmiDokumenti.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiDokumenti_actionPerformed();
      }
    });
    jmiDokumentiOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiDokumentiOK_actionPerformed();
      }
    });
    jmiIzvjestaji.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmiIzvjestaji_actionPerformed();
      }
    });

    setRaJMenuBar(jmb);
  }

  public void jmiIzvjestaji_actionPerformed() {
    showFrame("hr.restart.ur.frmURReportList",jmiIzvjestaji.getText());
    
  }

  public void jmiDokumentiOK_actionPerformed() {
    hr.restart.ur.frmSalKonOKUR fsk = (frmSalKonOKUR) showFrame("hr.restart.ur.frmSalKonOKUR", 15, "Dokumenti", false);
    fsk.pres.showPreselect(fsk, "Storno dokumenata");
    
  }

  public void jmiDokumenti_actionPerformed() {
    frmDoc fd = (frmDoc) showFrame("hr.restart.ur.frmDoc", 15, "Dokumenti urudžbenog zapisnika", false);
    fd.pres.showPreselect(fd, "Dokumenti urudžbenog zapisnika");
    //hr.restart.ur.frmSalKonUR fsk = (frmSalKonUR) showFrame("hr.restart.ur.frmSalKonUR", 15, "Dokumenti", false);
    //fsk.pres.showPreselect(fsk, "Dokumenti");
  }
}
