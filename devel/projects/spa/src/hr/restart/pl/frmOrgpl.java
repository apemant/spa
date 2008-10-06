/****license*****************************************************************
**   file: frmOrgpl.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmOrgpl extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  jpOrgpl jpDetail;

// definiranje rnv buttona
  raNavAction rnvVrOdb = new raNavAction("Odbici",raImages.IMGHISTORY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      vrOdb_action();
    }
  };
  raNavAction rnvDatIspl = new raNavAction("Datum isplate",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F6) {
    public void actionPerformed(ActionEvent e) {
      datumispl_action();
    }
  };
// konstruktor
  public frmOrgpl() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


// provjera stavki prije brisanja
  public boolean BeforeDelete()
  {
    plUtil.getPlUtil().deleteStandOdb("OJ", this.getRaQueryDataSet().getString("CORG"));
    return true;
  }

// Disabla tekst komponente kljuca kod izmjene
  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCorg, false);
      rcc.setLabelLaF(jpDetail.jlrCorgNaz, false);
      rcc.setLabelLaF(jpDetail.jbSelCorg, false);
    }
  }

// setiranje fokusa ovisno o modu rada
  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrCsif1.forceFocLost();
      jpDetail.jlrCopcine.forceFocLost();
      jpDetail.jlrCsif.forceFocLost();
      jpDetail.jlrCorg.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jlrCopcine.requestFocus();
    }
  }

// validacija
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrCorg) || vl.isEmpty(jpDetail.jlrCopcine))
      return false;
    if (mode == 'N' && vl.notUnique(jpDetail.jlrCorg))
      return false;
    return true;
  }

// init
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getOrgpl());
    getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("COPCINE",new String[] {"COPCINE","NAZIVOP"},dm.getOpcine())
    );
    getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CORG",new String[] {"CORG","NAZIV"},dm.getOrgstruktura())
    );

    this.setVisibleCols(new int[] {0, 1});
    jpDetail = new jpOrgpl(this);
    this.addOption(rnvVrOdb, 3);
    this.addOption(rnvDatIspl,4);
    this.setRaDetailPanel(jpDetail);
  }

// overridana show metoda
   public void show()
  {
    this.setTitle("Organizacijske jedinice za obra\u010Dun");
    super.show();
  }

// pozivanje globalne klase Odbitaka ovisno o parametru (vrsti odbitaka) preko rnv buttona s predefiniranim dataset-om
  void vrOdb_action() {
    frmGlobalMaster fGM = new frmGlobalMaster(this, "OJ", getRaQueryDataSet().getString("CORG"),"OJ");
    fGM.show();
  }

  void datumispl_action() {
    final JraDialog di = new JraDialog((Frame)getWindow(), "Org.jedinica "+getRaQueryDataSet().getString("CORG"));
    JraTextField jrdi = new JraTextField();
    jrdi.setDataSet(getRaQueryDataSet());
    jrdi.setColumnName("DATUMISPL");
    JLabel jldi = new JLabel("Datum isplate za tekuæi obraèun");
    JPanel content = new JPanel(new BorderLayout());
    JPanel jpdi = new JPanel();
    OKpanel okdi = new OKpanel() {
      public void jBOK_actionPerformed() {
        getRaQueryDataSet().saveChanges();
        di.hide();
      }
      public void jPrekid_actionPerformed() {
        di.hide();
      }
    };
    XYLayout xyl = new XYLayout();
    jpdi.setLayout(xyl);
    jpdi.add(jldi, new XYConstraints(15, 10, -1, -1));
    jpdi.add(jrdi, new XYConstraints(210, 10, 100, -1));
    di.setModal(true);
    content.add(jpdi,BorderLayout.CENTER);
    content.add(okdi,BorderLayout.SOUTH);
    di.setContentPane(content);
    startFrame.getStartFrame().centerFrame(di,10,di.getTitle());
    di.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        System.out.println(di.getSize());
      }
    });
    di.setSize(350,100);
    di.show();
    di.dispose();
    getJpTableView().fireTableDataChanged();
  }

  public void AfterSave(char mode)
{
  if(mode=='N')
  {
    plUtil.getPlUtil().addStandOdbici("OJ", getRaQueryDataSet().getString("CORG"));
  }
  }

}
