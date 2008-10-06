/****license*****************************************************************
**   file: jpDetailDoc.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Urdok;
import hr.restart.baza.Urstat;
import hr.restart.baza.Urvrdok;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.jpCpar;
import hr.restart.swing.raDateMask;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.zapod.jpGetValute;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpDetailDoc extends JPanel {

  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();
  
  frmDoc frm;
  
  JPanel jpDetail = new JPanel();
  XYLayout lay = new XYLayout();
  
  JPanel jpCustom = new JPanel();
  XYLayout clay = new XYLayout();
  
  
  jpCorg jpo = new jpCorg(350) {
    public void afterLookUp(boolean succ) {
      if (succ) jpp.focusCpar();
      checkCorg();
    }
  };
  jpCpar jpp = new jpCpar(350) {
    public void afterLookUp(boolean succ) {
      if (inedit && succ) frm.partnerChanged();
      if (inedit && succ) jraDatpri.requestFocus();
    }
  };
  
  JLabel jlSig = new JLabel();
  JlrNavField jlrSig = new JlrNavField() {
    public void after_lookUp() {
      if (inedit && isLastLookSuccessfull())
        checkSig();
    }
  };
  JlrNavField jlrIme = new JlrNavField() {
    public void after_lookUp() {
      if (inedit && isLastLookSuccessfull())
        checkSig();
    }
  };
  JlrNavField jlrPrezime = new JlrNavField() {
    public void after_lookUp() {
      if (inedit && isLastLookSuccessfull())
        checkSig();
    }
  };
  JraButton jbSelSig = new JraButton();
  
  
  JLabel jlBroj = new JLabel();
  JLabel jlBrojdok = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlaDatumDok = new JLabel();
  JLabel jlaDatumPri = new JLabel();
  JLabel jlaDatumDosp = new JLabel();
  JLabel jlOpis = new JLabel();
  JLabel jlStatus = new JLabel();
  
  
  JraTextField jraDatdok = new JraTextField();
  JraTextField jraDatpri = new JraTextField();
  JraTextField jraDatdosp = new JraTextField();
  JraTextField jraBroj = new JraTextField();
  JraTextField jraBrojdok = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  
  raComboBox rcbStatus = new raComboBox();
  
  jpGetValute jpgetval = new jpGetValute();
  JraTextField jraIznos = new JraTextField();
  JLabel jlIznos = new JLabel();
  JraButton jbDoh, jbDoh2, jbDoh3;
  
  boolean inedit;
  String vrdok;
  
  public jpDetailDoc(frmDoc fd) {
    try {
      frm = fd;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void BindComponents(DataSet ds) {
    jraBroj.setDataSet(ds);
    jraBrojdok.setDataSet(ds);
    jraDatdok.setDataSet(ds);
    jraDatpri.setDataSet(ds);
    jraOpis.setDataSet(ds);
    jlrSig.setDataSet(ds);
    jpp.bind(ds);
    jpo.bind(ds);
    rcbStatus.setRaDataSet(ds);
    jpgetval.setRaDataSet(ds);
    jraIznos.setDataSet(ds);
    jraDatdosp.setDataSet(ds);
  }
  
  int defStatus;
  String defStat;
  public void setVrdok(String vrdok) {
    this.vrdok = vrdok;
    
    DataSet stats = Urstat.getDataModule().getTempSet(
        Condition.in("VRDOK", new String[] {"*", vrdok}));
    stats.open();
    rcbStatus.setRaItems(stats, "STATUS", "OPIS");
    ld.raLocate(stats, "DEFVAL", "D");
    rcbStatus.setSelectedIndex(defStatus = stats.getRow());
    defStat = stats.getString("STATUS");
    if ("URA".equals(vrdok)) {
      jpp.setPartnerDob();
      setRacVisible(true);
    } else if ("IRA".equals(vrdok)) {
      jpp.setPartnerKup();
      setRacVisible(true);
    } else {
      jpp.setPartnerAny();
      setRacVisible(false);
    }
  }
  
  void setRacVisible(boolean vis) {
    jpDetail.remove(jlaDatumDosp);
    jpDetail.remove(jraDatdosp);
    jpDetail.remove(jpgetval);
    jpDetail.remove(jlIznos);
    jpDetail.remove(jraIznos);
    if (vis) {
      lay.setHeight(305);
      jpDetail.add(jlaDatumDosp, new XYConstraints(361, 123, 98, -1));      
      jpDetail.add(jraDatdosp, new XYConstraints(360, 140, 100, -1));
      jpDetail.add(jpgetval, new XYConstraints(0, 225, -1, -1));
      jpDetail.add(jlIznos, new XYConstraints(15, 275, -1, -1));
      jpDetail.add(jraIznos, new XYConstraints(150, 275, 100, -1));
    }
    createCustomPanel(vis);
  }
  
  public void initStatus() {
    rcbStatus.setSelectedIndex(defStatus);
    rcbStatus.getDataSet().setString(rcbStatus.getColumnName(), defStat);
  }
  
  public void setEdit(boolean ed) {
    inedit = ed;
  }
  
  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(646);
    lay.setHeight(285);
    
    jpCustom.setLayout(clay);
    clay.setWidth(590);
    
    jlBroj.setText("Interni broj dokumenta");
    jlBroj.setHorizontalAlignment(SwingConstants.TRAILING);
    jlBrojdok.setText("Broj dokumenta");
//    jlCorg.setText("Org. jedinica");
//    jlCpar.setText("Partner");
    jlDatum.setText("Datum");
    jlOpis.setText("Opis");
    jlIznos.setText("Iznos raèuna");
    jlStatus.setText("Status");
    jlSig.setText("Signacija");
    jlaDatumDok.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatumDok.setText("Dokumenta");
    jlaDatumPri.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatumPri.setText("Primitka");
    jlaDatumDosp.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatumDosp.setText("Dospjeæa");

    jraBroj.setColumnName("RBR");
    jraBroj.setHorizontalAlignment(SwingConstants.TRAILING);
    jraBrojdok.setColumnName("BROJDOK");
    jraDatdok.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatdok.setColumnName("DATDOK");
    jraDatpri.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatpri.setColumnName("DATPRI");
    jraDatdosp.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatdosp.setColumnName("DATDOSP");
    jraOpis.setColumnName("OPIS");
    
    jlrSig.setColumnName("SIG");
    jlrSig.setNavColumnName("CRADNIK");
    jlrSig.setRaDataSet(dm.getRadnici());
    jlrSig.setColNames(new String[] {"IME", "PREZIME"});
    jlrSig.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrSig.setVisCols(new int[] {0, 1, 2});
    jlrSig.setSearchMode(0);
    jlrSig.setNavButton(jbSelSig);

    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrSig);
    jlrIme.setSearchMode(1);
    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrSig);
    jlrPrezime.setSearchMode(1);
    
    rcbStatus.setRaColumn("STATUS");
    
    jpgetval.setTecajVisible(true);
    jpgetval.setTecajEditable(true);
    jraIznos.setColumnName("IZNOS");
    
    jpDetail.add(jlBroj, new XYConstraints(15, 20, 450, -1));
    jpDetail.add(jraBroj, new XYConstraints(505, 20, 100, -1));
    jpDetail.add(jpo, new XYConstraints(0, 70, -1, -1));
    jpDetail.add(jlSig, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrSig, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrIme, new XYConstraints(255, 45, 145, -1));
    jpDetail.add(jlrPrezime, new XYConstraints(405, 45, 200, -1));
    jpDetail.add(jbSelSig, new XYConstraints(610, 45, 21, 21));
    jpDetail.add(jpp, new XYConstraints(0, 95, -1, -1));
    jpDetail.add(jlaDatumPri, new XYConstraints(151, 123, 98, -1));
    jpDetail.add(jlaDatumDok, new XYConstraints(256, 123, 98, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 140, -1, -1));
    jpDetail.add(jraDatpri, new XYConstraints(150, 140, 100, -1));
    jpDetail.add(jraDatdok, new XYConstraints(255, 140, 100, -1));
    jpDetail.add(jlBrojdok, new XYConstraints(15, 165, -1, -1));
    jpDetail.add(jraBrojdok, new XYConstraints(150, 165, 455, -1));
    jpDetail.add(jlStatus, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(rcbStatus, new XYConstraints(150, 18, 100, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 195, -1, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 195, 455, -1));
    
    jraDatdok.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        frm.datdokChanged();
      }
    });
    
    this.add(jpDetail, BorderLayout.CENTER);
  }
  
  void checkSig() {
    String rad = jlrSig.getText().trim();
    ld.raLocate(dm.getRadnici(), "CRADNIK", rad);
    String corg = dm.getRadnici().getString("CORG");
    if (corg.trim().length() > 0 && !corg.trim().equals(jpo.getCorg()))
      jpo.setCorg(corg);
    jpp.focusCparLater();
  }
  
  void checkCorg() {
    String rad = jlrSig.getText().trim();
    if (ld.raLocate(dm.getRadnici(), "CRADNIK", rad)) {
      if (!dm.getRadnici().getString("CORG").equals(jpo.getCorg())) {
        jlrSig.setText("");
        jlrSig.forceFocLost();
        jlrSig.requestFocusLater();
      }
    }
  }
  
  private void createCustomPanel(boolean racs) {
    jpDetail.remove(jpCustom);
    if (racs) return;
    jpCustom.removeAll();
    jbDoh = jbDoh2 = jbDoh3 = null;
    DataSet vrd = Urvrdok.getDataModule().getTempSet(Condition.equal("VRDOK", vrdok));
    vrd.open();
    String[] cols = new VarStr(vrd.getString("KOLONE")).splitTrimmed(',');
    
    int h = 0;
    Column c;
    for (int i = 0; i < cols.length; i++)
      if (null != (c = Urdok.getDataModule().getColumn(cols[i])))
        addColumn(c, h += 25);
    
    if (h > 0) {
      clay.setHeight(h + 25);
      lay.setHeight(275 + h);
      jpCustom.setBorder(BorderFactory.createEtchedBorder());
      jpDetail.add(jpCustom, new XYConstraints(14, 235, -1, -1));
    } else lay.setHeight(230);
  }
  
  private void addColumn(Column c, int nexth) {
    DataSet ds = jraBroj.getDataSet();
    if (c.getDataType() == Variant.STRING && c.getPrecision() == 1) {
      JraCheckBox cb = new JraCheckBox();
      cb.setHorizontalTextPosition(SwingConstants.LEADING);
      cb.setHorizontalAlignment(SwingConstants.TRAILING);
      cb.setText(" " + c.getCaption() + " ");
      cb.setSelectedDataValue("D");
      cb.setUnselectedDataValue("N");
      cb.setColumnName(c.getColumnName());
      cb.setDataSet(ds);
      jpCustom.add(cb, new XYConstraints(15, nexth - 10, 565, -1));
    } else {
      JLabel lab = new JLabel(c.getCaption());
      JraTextField tf = new JraTextField();
      tf.setColumnName(c.getColumnName());
      tf.setDataSet(ds);
      tf.setHorizontalAlignment(SwingConstants.TRAILING);
      if (c.getDataType() == Variant.INT)
        new raTextMask(tf, 10, false, raTextMask.NUMERIC);
      else if (c.getDataType() == Variant.BIGDECIMAL)
        Aus.installNumberMask(tf, c.getScale());
      else if (c.getDataType() == Variant.TIMESTAMP) {
        new hr.restart.swing.raDatePopup(tf);
        new raDateMask(tf);
        tf.setHorizontalAlignment(SwingConstants.CENTER);
      }
      boolean link = c.getColumnName().startsWith("HLINK");
      int w = c.getDataType() == Variant.STRING && c.getPrecision() > 12 ? 250 : 100;
      if (link) w = 350;
      jpCustom.add(lab, new XYConstraints(15, nexth - 10, -1, -1));
      jpCustom.add(tf, new XYConstraints(570 - w, nexth - 10, w, -1));
      if (link) {
        JraButton jb = new JraButton();
        jb.setName(c.getColumnName());
        jb.setText(null);
        jb.setIcon(raImages.getImageIcon(raImages.IMGOPEN));
        jpCustom.add(jb, new XYConstraints(544 - w, nexth - 10, 21, 21));
        jb.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            frm.invokeLink(((JraButton) e.getSource()).getName());
          }
        });
        if (c.getColumnName().equals("HLINK")) jbDoh = jb;
        else if (c.getColumnName().equals("HLINK2")) jbDoh2 = jb;
        else if (c.getColumnName().equals("HLINK3")) jbDoh3 = jb;
      }
    }
  }
}
