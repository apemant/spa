/****license*****************************************************************
**   file: jpTemIzvod.java
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
package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpTemIzvod extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  String vrdok;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojdok = new JLabel();
  JLabel jlBrojizv = new JLabel();
  JLabel jlCpar = new JLabel();
  JLabel jlVrdok = new JLabel();
  JraButton jbSelCpar = new JraButton();
  JraTextField jraBrojdok = new JraTextField();
  JraTextField jraBrojizv = new JraTextField();
  JraTextField jraVrdok = new JraTextField();
  JraTextField jraDatdosp = new JraTextField();
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  raMatPodaci rmParent;
  public jpTemIzvod(raMatPodaci rm) {
    try {
      rmParent = rm;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(491);
    lay.setHeight(135);

    jbSelCpar.setText("...");
    jlBrojdok.setText("Broj dokumenta");
    jlBrojizv.setText("Izvod / datum dosp.");
    jlCpar.setText("Partner");
    jlVrdok.setText("Vrsta dokumenta");
    jraBrojdok.setColumnName("BROJDOK");
    jraBrojizv.setColumnName("BROJIZV");
    jraVrdok.setColumnName("VRDOK");
    jraDatdosp.setColumnName("DATDOSP");

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1});
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jpDetail.add(jbSelCpar, new XYConstraints(455, 45, 21, 21));
    jpDetail.add(jlBrojdok, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlBrojizv, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlCpar, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlVrdok, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(255, 45, 195, -1));
    jpDetail.add(jraBrojdok, new XYConstraints(150, 70, 300, -1));
    jpDetail.add(jraBrojizv, new XYConstraints(150, 95, 100, -1));
    jpDetail.add(jraDatdosp, new XYConstraints(255, 95, 100, -1));
    jpDetail.add(jraVrdok, new XYConstraints(150, 20, 100, -1));
    rcc.setLabelLaF(jraVrdok, false);
    this.add(jpDetail, BorderLayout.CENTER);
    
  }
  public static boolean sucfull;
  
  public void constructDialog() {
    if (rmParent.getFrameMode() == rmParent.DIALOG)
     dlgTemIzvod = new JraDialog((JDialog)rmParent.getWindow(),true);
    else
      dlgTemIzvod = new JraDialog((JFrame)rmParent.getWindow(),true);
     
    OKpanel okp = new OKpanel() {
      public void jBOK_actionPerformed() {
        if (validateDlg()) {
          sucfull=true;
          dlgTemIzvod.hide();
        }
      }
      public void jPrekid_actionPerformed() {
        sucfull=false;
        dlgTemIzvod.hide();
      }
    };
    dlgTemIzvod.getContentPane().setLayout(new BorderLayout());
    dlgTemIzvod.getContentPane().add(this,BorderLayout.CENTER);
    dlgTemIzvod.getContentPane().add(okp,BorderLayout.SOUTH);
    dlgTemIzvod.addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        focusPoint();
      }
    });
    okp.registerOKPanelKeys(dlgTemIzvod);
  }
  private void focusPoint() {
    jraVrdok.getDataSet().setString(jraVrdok.getColumnName(), vrdok);
    if (vl.chkIsEmpty(jraDatdosp)) {
      jraDatdosp.getDataSet().setTimestamp("DATDOSP", jraDatdosp.getDataSet().getTimestamp("DATDOK"));
    }
    jlrCpar.requestFocus();
  }
  private boolean validateDlg() {
    if (vl.isEmpty(jlrCpar)) return false;
    if (vl.isEmpty(jraBrojdok)) return false;
    if (vl.isEmpty(jraDatdosp)) return false;
    if (vl.isEmpty(jraBrojizv)) jraBrojizv.getDataSet().setInt("BROJIZV", 0);
    return true;
  }
  
  public void rebindDataSet(StorageDataSet ds) {
    jraVrdok.setDataSet(ds);
    jraBrojdok.setDataSet(ds);
    jraBrojizv.setDataSet(ds);
    jraDatdosp.setDataSet(ds);
    jlrCpar.setDataSet(ds);
  }
  
  private JraDialog dlgTemIzvod;
  private static jpTemIzvod jptemiz;
  
  public static void showTemIzvod(raMatPodaci rm,String _vrdok) {
    if (jptemiz == null) {
      jptemiz = new jpTemIzvod(rm);
    } else jptemiz.rmParent = rm;
    jptemiz.rebindDataSet(rm.getRaQueryDataSet());
    jptemiz.vrdok = _vrdok;
    jptemiz.constructDialog();
    startFrame.getStartFrame().centerFrame(jptemiz.dlgTemIzvod, 0, "Podaci za SK");
    startFrame.getStartFrame().showFrame(jptemiz.dlgTemIzvod);
  }
  public static boolean isSuccFull() {
    if (jptemiz == null) return false;
    return jptemiz.sucfull;
  }
}

