/****license*****************************************************************
**   file: jpTemSK.java
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
/*
 * jpTemSK.java
 *
 * Created on April 15, 2004, 8:21 AM
 */

package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raSaldaKonti;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 *
 * @author  andrej
 */
public class jpTemSK extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  
  jpDevIznos jpDevI;
  
  String vrdok;
  //JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojdok = new JLabel();
  JLabel jlCpar = new JLabel();
  JraButton jbSelCpar = new JraButton();
  JraTextField jraBrojdok = new JraTextField();
//  JraTextField jraBrojizv = new JraTextField();
//  JraTextField jraVrdok = new JraTextField();
  JraTextField jraDatdosp = new JraTextField();
  public JraTextField jraDatdok = new JraTextField() {
    public void valueChanged() {
      if (jpDevI!=null) jpDevI.setTecDate(jraDatdok.getDataSet().getTimestamp(jraDatdok.getColumnName()));
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (jpDevI!=null) jpDevI.setTecDate(jraDatdok.getDataSet().getTimestamp(jraDatdok.getColumnName()));
    }*/
  };
  JlrNavField jlrNazpar = new JlrNavField();
  JlrNavField jlrCpar = new JlrNavField();  
  /** Creates a new instance of jpTemSK */
  public jpTemSK() {
    jpInit();
  }
  private void jpInit() {
    setLayout(lay);

    jbSelCpar.setText("...");
    jlBrojdok.setText("Datum dokumenta");
    jlCpar.setText("Partner");
    jraBrojdok.setColumnName("BROJDOK");
    //jraVrdok.setColumnName("VRDOK");
    jraDatdosp.setColumnName("DATDOSP");
    jraDatdok.setColumnName("DATDOK");
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

    add(jlBrojdok, new XYConstraints(15, 0, -1, -1));
    add(jraDatdok, new XYConstraints(150, 0, 100, -1));
    add(jraDatdosp, new XYConstraints(255, 0, 100, -1));
    add(jraBrojdok, new XYConstraints(360, 0, 240, -1));
    
    add(jlCpar, new XYConstraints(15, 25, -1, -1));
    add(jlrCpar, new XYConstraints(150, 25, 100, -1));
    add(jlrNazpar, new XYConstraints(255, 25, 345, -1));
    add(jbSelCpar, new XYConstraints(605, 25, 21, 21));

//    jpDetail.add(jraVrdok, new XYConstraints(150, 50, 100, -1));
    
    //rcc.setLabelLaF(jraVrdok, false);
  }
  public boolean validate(char mode) {
    if (!enablejp) return jpDevI!=null?jpDevI.validate(mode):true;//true - ako se dev iznos uvijek unosi mora ovako
    if (vl.isEmpty(jraDatdok)) return false;
    if (vl.isEmpty(jraDatdosp)) return false;
    if (vl.isEmpty(jraBrojdok)) return false;
    if (vl.isEmpty(jlrCpar)) return false;
    if (jpDevI!=null) return jpDevI.validate(mode);
    return true;
  }
  public void rebind(StorageDataSet ds) {
    rebind(ds,true);
  }
  public void rebind(StorageDataSet ds,boolean rebindCPar) {
    jraBrojdok.setDataSet(ds);
    jraDatdosp.setDataSet(ds);
    //jraDatdok.setDataSet(ds);
    if (rebindCPar) jlrCpar.setDataSet(ds);
    //jlrNazpar.setDataSet(ds);
  }
  public void setJpDevIznos(jpDevIznos _jpdi) {
    jpDevI = _jpdi;
  }
  private boolean enablejp = false;
    
  public void enable(boolean _enabled,raMatPodaci rm) {
    QueryDataSet qds = rm.getRaQueryDataSet();
    boolean qdsIsSkstavke = false;
    if (_enabled && rm.getRaQueryDataSet().hasColumn("BROJDOK")==null) {
      qds = raSaldaKonti.getSkstavkaFromGK(qds);
      if (qds != null) qdsIsSkstavke = true;
      else {
        _enabled = false;
        qds = rm.getRaQueryDataSet();
      }
    }
    
    enablejp = _enabled;
    rebind(enablejp||alwaysEnablePar()?qds:null,!qdsIsSkstavke||alwaysEnablePar());
    if (qdsIsSkstavke||alwaysEnablePar()) jlrCpar.setDataSet(rm.getRaQueryDataSet());
    jraDatdok.setDataSet(enablejp ? qds : rm.getRaQueryDataSet());

    if (jpDevI != null) {
      if (qds.hasColumn("DEVID") != null) jpDevI.setDataSet(qds);
      if (rm.getRaQueryDataSet().hasColumn("DEVID") != null) jpDevI.setDataSet(rm.getRaQueryDataSet());
    }
    if (rm.getMode() != 'B') {
      jraBrojdok.setEnabled(enablejp);
      jraDatdosp.setEnabled(enablejp);
      jlrCpar.setEnabled(enablejp||alwaysEnablePar());
      jlrNazpar.setEnabled(enablejp||alwaysEnablePar());
      jbSelCpar.setEnabled(enablejp||alwaysEnablePar());
    }
//System.out.println("checkpoint 1 jpDevI = "+jpDevI);    
    jlBrojdok.setText(enablejp?"Dok: dat/dosp/broj":"Datum dokumenta");
    jraBrojdok.setVisible(enablejp);
    jraDatdosp.setVisible(enablejp);
    jlrCpar.setVisible(enablejp||alwaysEnablePar());
    jlrNazpar.setVisible(enablejp||alwaysEnablePar());
    jlCpar.setVisible(enablejp||alwaysEnablePar());
    jbSelCpar.setVisible(enablejp||alwaysEnablePar());
    if (jpDevI != null) {
      //jpDevI - neka je uvijek enabled?
      boolean jpdevi_enablejp = true; //enablejp
      jpDevI.setEnabled(jpdevi_enablejp);
      jpDevI.setVisible(jpdevi_enablejp);
      if (jpDevI!=null && jpdevi_enablejp) {
        jpDevI.setFokus(rm.getMode());
      }
    }
    
    if (enablejp && !qdsIsSkstavke && rm.getMode()=='N') qds.setTimestamp("DATDOSP",qds.getTimestamp("DATDOK"));
  }
  public void justEnable(boolean enablejp) {
    jraBrojdok.setEnabled(enablejp);
    jraDatdosp.setEnabled(enablejp);
    jlrCpar.setEnabled(enablejp);
    jlrNazpar.setEnabled(enablejp);
    jbSelCpar.setEnabled(enablejp);
  }
  /**
   * @return
   */
  private boolean alwaysEnablePar() {
    return (frmParam.getParam("gk","finkonpar_tem","N","Unosi li se partner kroz temeljnicu i na financijska konta").equals("D"));
  }
  /**
   * Radi setLabelLaf(*Cpar, enabler || finkonpar_tem = D)
   * @param enabled
   */
  public void setLabelLafPar(boolean enabled) {
    rcc.setLabelLaF(jlrCpar,(enabled||alwaysEnablePar()));
    rcc.setLabelLaF(jlrNazpar,(enabled||alwaysEnablePar()));
    rcc.setLabelLaF(jbSelCpar,(enabled||alwaysEnablePar()));
  }
} 
