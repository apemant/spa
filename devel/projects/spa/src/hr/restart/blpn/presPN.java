/****license*****************************************************************
**   file: presPN.java
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

import hr.restart.baza.Radnici;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.Color;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class presPN extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlDatod = new JLabel();
  JLabel jlGodina = new JLabel();
  JLabel jlStatus = new JLabel();
  JLabel jlStatusputa = new JLabel();
  JLabel jlRadnik = new JLabel();
  JraTextField jraGodina = new JraTextField() {
    public void valueChanged() {
      godinaChanged();
    }
  };
  JraTextField jraDatod = new JraTextField();
  JraTextField jraDatdo = new JraTextField();
  JraTextField jraKnjig = new JraTextField();
  raComboBox rcbStatus = new raComboBox();
  raComboBox rcbIndPut = new raComboBox();
  JlrNavField jlrCradnik = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrPrezime = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrIme = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraButton jbSelCradnik = new JraButton();
  private Border border1;
  private boolean firstin = true;

  public presPN() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  protected void jbInit() throws Exception {
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        radnici = null;
        jlrCradnik.setRaDataSet(getRadnici());
        jlrIme.setRaDataSet(getRadnici());
        jlrPrezime.setRaDataSet(getRadnici());
      }
    });
    border1 = BorderFactory.createEtchedBorder(new Color(224, 255, 255),new Color(109, 129, 140));
    this.setSelDataSet(dm.getPutniNalog());
    jpDetail.setLayout(lay);
    lay.setWidth(599);
    lay.setHeight(161);

    jlDatod.setText("Period dat. odlaska");
    jlGodina.setText("Godina");
    jlStatus.setText("Status naloga");
    jlStatusputa.setText("Indikator puta");
    jlRadnik.setText("Radnik");

    jraDatod.setColumnName("DATUMODL");
    jraDatod.setDataSet(getSelDataSet());
    jraDatod.setHorizontalAlignment(SwingConstants.CENTER);

    jraDatdo.setColumnName("DATUMODL");
    jraDatdo.setDataSet(getSelDataSet());
    jraDatdo.setHorizontalAlignment(SwingConstants.CENTER);

    jraKnjig.setColumnName("KNJIG");
    jraKnjig.setDataSet(getSelDataSet());
    jraKnjig.setHorizontalAlignment(SwingConstants.CENTER);
    jraKnjig.setVisible(false);
    jraKnjig.setEditable(false);

    jraGodina.setColumnName("GODINA");
    jraGodina.setDataSet(getSelDataSet());
    jraGodina.setHorizontalAlignment(SwingConstants.CENTER);

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(getSelDataSet());
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1, 2});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(getRadnici());
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
//    jlrIme.setDataSet(stds);
    jlrIme.setSearchMode(1);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
//    jlrPrezime.setDataSet(stds);
    jlrPrezime.setSearchMode(1);

    rcbIndPut.setRaColumn("INDPUTA");
    rcbIndPut.setRaDataSet(getSelDataSet());
    rcbIndPut.setRaItems(new String[][] {
      {"Svi indikatori",""},
      {"Tuzemstvo","Z"},
      {"Inozemstvo","I"}});

    rcbStatus.setRaColumn("STATUS");
    rcbStatus.setRaDataSet(getSelDataSet());
    rcbStatus.setRaItems(new String[][] {
        {"",""}});


    jpDetail.setBorder(border1);
    //    jpDetail.setMinimumSize(new Dimension(370, 125));
    
    jpDetail.add(jlGodina, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraGodina, new XYConstraints(150, 20, 100, -1));
    
    jpDetail.add(jlStatus, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(rcbStatus, new XYConstraints(150, 45, 100, -1));

    jpDetail.add(jlStatusputa, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(rcbIndPut, new XYConstraints(150, 70, 100, -1));

    jpDetail.add(jlDatod, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraDatdo, new XYConstraints(255, 95, 100, -1));
    jpDetail.add(jraDatod,    new XYConstraints(150, 95, 100, -1));
    
    jpDetail.add(jlrCradnik,  new XYConstraints(150, 120, -1, -1));
    jpDetail.add(jlrIme,       new XYConstraints(255, 120, 120, -1));
    jpDetail.add(jlrPrezime, new XYConstraints(380,120,170,-1));
    jpDetail.add(jlRadnik, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jbSelCradnik, new XYConstraints(555, 120, 21, 21));
    jpDetail.add(jraKnjig,    new XYConstraints(0,0,0,0));
    addSelRange(jraDatod,jraDatdo);
    this.setSelPanel(jpDetail);
  }
  short inigodina = 0;
  public void SetFokus(){
    if (firstin) {
      getSelRow().setString("CRADNIK", "");
      jlrCradnik.emptyTextFields();
      rcbStatus.setRaItems(new String[][] {
        {"Svi statusi",""},
        {"Prijavljen","P"},
        {"Akontiran","A"},
        {"Obra\u0111en","O"},
        {"Ispla\u0107en","I"}
      });
      rcbStatus.setSelectedIndex(0);
      rcbIndPut.setSelectedIndex(0);
      short god = Short.parseShort(vl.findYear(vl.getToday()));
      vl.getCommonRange(jraDatod, jraDatdo);
      getSelRow().setShort("GODINA", god);
      getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
      firstin = false;
    } 
    jraGodina.requestFocus();
    jraGodina.selectAll();
    inigodina = getSelRow().getShort("GODINA");
//System.out.println("inigodina = "+inigodina);
  }
  private static StorageDataSet radnici;
  public static StorageDataSet getRadnici() {
    if (radnici == null) {
      radnici = Radnici.getDataModule().getFilteredDataSet(" radnici.aktiv='D' AND (radnici.corg in "+
          OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "radnici.corg")+")");
    }
    return radnici;
  }
  public boolean Validacija() {
    short god = getSelRow().getShort("GODINA");
    Calendar c = Calendar.getInstance();
    c.setTime(getSelRow().getTimestamp("DATUMODL-from"));
    short god_from = (short)c.get(Calendar.YEAR);
    c.setTime(getSelRow().getTimestamp("DATUMODL-to"));
    short god_to = (short)c.get(Calendar.YEAR);
    if (!(god_from == god_to && god_from == god)) {
      JOptionPane.showMessageDialog(super.getPreSelDialog(),"Nesipravan datumski period ili godina", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
  protected void godinaChanged() {
    short god = getSelRow().getShort("GODINA");
    if (god == inigodina) return;//ai: maskcheck bas i ne zove samo kad je promijenjeno pa ja eto ovako.
    inigodina = god;
    Calendar c = Calendar.getInstance();
    short tgod = (short)c.get(Calendar.YEAR);
    if (tgod>god) {//01.12-31.12
      c.set(god,11,1);
      getSelRow().setTimestamp("DATUMODL-from",c.getTime().getTime());
      c.set(god,11,31);
      getSelRow().setTimestamp("DATUMODL-to",c.getTime().getTime());
    } else if (tgod<god) {//01.01.-31.01.
      c.set(god,0,1);
      getSelRow().setTimestamp("DATUMODL-from",c.getTime().getTime());
      c.set(god,0,31);
      getSelRow().setTimestamp("DATUMODL-to",c.getTime().getTime());      
    } else {
      vl.getCommonRange(jraDatod, jraDatdo);
    }
  }

}