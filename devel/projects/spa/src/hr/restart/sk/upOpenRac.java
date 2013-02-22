/****license*****************************************************************
**   file: upOpenRac.java
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
package hr.restart.sk;

import hr.restart.baza.Agenti;
import hr.restart.baza.Condition;
import hr.restart.baza.Partneri;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.jpCpar;
import hr.restart.swing.jpZupGrad;
import hr.restart.swing.raButtonGroup;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raProcess;
import hr.restart.util.raUpitLite;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class upOpenRac extends raUpitLite {
  private dM dm = dM.getDataModule();
  private lookupData ld = lookupData.getlookupData();
  private raCommonClass rcc = raCommonClass.getraCommonClass();
  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  public JPanel pan = new JPanel() {
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      raMatPodaci.drawEtchedLine(pan, g, 15, 130 + dkAdd, 545, 130 + dkAdd);
    }
  };
  XYLayout lay = new XYLayout();

  public jpCorg jpc = new jpCorg(100, 290, true);
  public jpCpar jpp = new jpCpar(100, 290, true) {
    public void afterLookUp(boolean succ) {
      if (succ) jpzg.init();
    }
    protected void kupSelected() {
      super.kupSelected();
      if (raSaldaKonti.isDirect()) checkKupDob(true);
    }
    protected void dobSelected() {
      super.dobSelected();
      if (raSaldaKonti.isDirect()) checkKupDob(false);
    }
  };
  public jpSelKonto jpk = new jpSelKonto(100, 290, true) {
    public void afterLookUp(boolean succ) {
      afterKonto(succ);
    }
  };
  public jpZupGrad jpzg = new jpZupGrad(100, 290) {
    public void afterLookUp(boolean succ) {
      if (succ) jpp.init();
    }
  };

//  JLabel jlMjesto = new JLabel();
//  JLabel jlZup = new JLabel();
//  JraTextField jraMjesto = new JraTextField() {
//    public boolean isFocusTraversable() {
//      return jpp.isEmpty();
//    }
//  };
  
  raComboBox rcbDat = new raComboBox() {
    public void this_itemStateChanged() {
    }
  };
//  JLabel jlDan = new JLabel();
  JraTextField jraDan = new JraTextField();
  
  JraCheckBox jcbUpl = new JraCheckBox();
  JraCheckBox jcbKum = new JraCheckBox();
  JraCheckBox jcbNon = new JraCheckBox();

//  JraButton jbSelZup = new JraButton();
//  JlrNavField jlrZup = new JlrNavField() {
//    public boolean isFocusTraversable() {
//      return jraMjesto.getText().length() == 0 && jpp.isEmpty();
//    }
//  };

//  JlrNavField jlrNazivZup = new JlrNavField() {
//    public boolean isFocusTraversable() {
//      return jraMjesto.getText().length() == 0 && jpp.isEmpty();
//    }
//  };
  
  int dkAdd = 0;
  boolean dospPer;
  
  public static DataSet cs; 
  
  raButtonGroup bg = new raButtonGroup(SwingConstants.TRAILING, SwingConstants.LEFT);
  JraRadioButton jrbDosp = new JraRadioButton();
  JraRadioButton jrbNedosp = new JraRadioButton();
  JLabel jlPeriod = new JLabel();

  JLabel jlIznos = new JLabel();
  JraTextField jraIznos = new JraTextField();

  StorageDataSet fields = new StorageDataSet();

  JLabel jlKas = new JLabel();
  JraTextField jraP1 = new JraTextField();
  JraTextField jraP2 = new JraTextField();
  JraTextField jraP3 = new JraTextField();
  JraTextField jraP4 = new JraTextField();
  JraTextField[] all = {jraP1, jraP2, jraP3, jraP4};

  QueryDataSet repQDS = new QueryDataSet();
  static upOpenRac upThis;
  boolean firstReset = false;

  public upOpenRac() {
    try {
      upThis = this;
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  public static upOpenRac getInstance() {
    return upThis;
  }

  public QueryDataSet getRepQDS() {
    return repQDS;
  }
  
  public void resetDefaults() {
    jpp.init();
    jpc.init();
    jpzg.init();
    jpk.setKontaAllow(false);
    rcbDat.setSelectedIndex(0);
    fields.setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG(false));
    fields.setTimestamp("DATUM", Valid.getValid().getToday());
  }

  public void componentShow() {
    changeIcon(1);
    jpp.setKupci(jpp.isKupci());
    jpk.setKontaAllow(jpk.isKontaAllow());
    if (raSaldaKonti.isDirect() && !jpk.isKontaAllow()) jpk.enabDohvat(false);
    if (!firstReset) {
      firstReset = true;
      resetDefaults();
    }
    rcc.setLabelLaF(jraP4, !jcbUpl.isSelected());
    jpp.focusCombo();
  }

  public String getNaslov() {
    return (jpp.isKupci() ? "\nPOTRAŽIVANJA" : "\nDUGOVANJA")+
        " na dan "+rdu.dataFormatter(fields.getTimestamp("DATUM"));
  }
  
  public String getPodnaslov() {
    if (getPeriods() == 0) return  "";
    return dospPer ? "\nuz raspodjelu dospjelih raèuna po danima kašnjenja" :
      "\nuz raspodjelu nedospjelih raèuna po danima do dospjeæa";
  }

  public String getMjesto() {
    if (jpzg.getAgent() != null) return jpzg.getAgent();
    else if (jpzg.getGrad() != null) return jpzg.getGrad();
    else if (jpzg.getZupanija() != null) return jpzg.getZupanija();
    else return "";
  }

  public String getLabelMjesto() {
    if (jpzg.getAgent() != null) return "Agent";
    else if (jpzg.getGrad() != null) return "Grad";
    else if (jpzg.getZupanija() != null) return "Županija";
    else return "";
  }

  public boolean isIspis() {
    return false;
  }

  public boolean Validacija() {
    return jpc.Validacija();
  }

  public int getPeriods() {
    int p1 = Aus.getNumber(jraP1.getText());
    int p2 = Aus.getNumber(jraP2.getText());
    int p3 = Aus.getNumber(jraP3.getText());
    int p4 = Aus.getNumber(jraP4.getText());
    if (p4 > 0 && !isUpl()) return 5;
    else if (p3 > 0) return 4;
    else if (p2 > 0) return 3;
    else if (p1 > 0) return 2;
    else return 0;
  }

  public int getPeriod1() {
    return Aus.getNumber(jraP1.getText());
  }

  public int getPeriod2() {
    return Aus.getNumber(jraP2.getText());
  }

  public int getPeriod3() {
    return Aus.getNumber(jraP3.getText());
  }

  public int getPeriod4() {
    return Aus.getNumber(jraP4.getText());
  }
  
  public boolean isUpl() {
    return jcbUpl.isSelected();
  }
  
  public boolean isKum() {
    return jcbKum.isSelected() && !dospPer;
  }
  
  public boolean isNon() {
    return jcbNon.isSelected();
  }

  public void okPress() {
    dospPer = jrbDosp.isSelected();

    String datumCol = rcbDat.getSelectedIndex() == 0 ? "DATDOK" : "DATUMKNJ";
    Timestamp dan = ut.getLastSecondOfDay(fields.getTimestamp("DATUM"));
    Condition dat = Aus.getCurrGKDatumCond(datumCol, dan);
    Timestamp d2 = Aus.getGkYear(dan);
    d2 = Util.getUtil().addDays(d2, 1);
    Condition kon = raSaldaKonti.isDirect() ? jpk.getCondition() : Condition.none;
    //Condition dat = Condition.where("DATDOK", Condition.BEFORE, dan);
    String sort, select;
    if ("$NAZPAR".equals(getSortColumn())) {
      sort = getOrderBy();
      select = "skstavke.cpar,partneri.nazpar,skstavke.saldo,skstavke.datdosp," +
      		"skstavke.vrdok,skstavke.brojdok,skstavke.id,skstavke.ip,skstavke.cskstavke," +
      		"skstavke.tecaj,skstavke.oznval,skstavke.datumknj FROM skstavke,partneri " +
            "WHERE partneri.cpar=skstavke.cpar AND ";
    } else {
      sort = " ORDER BY cpar";
      select = "cpar,saldo,datdosp,vrdok,brojdok,id,ip,cskstavke,tecaj,oznval,datumknj FROM skstavke WHERE ";
    }

    String vrd = jpp.isKupci() ? "('IRN','UPL','OKK')" : "('URN','IPL','OKD')";
    String que = "SELECT "+select+Aus.getKnjigCond().and(jpc.getOptCondition()).and(dat).and(kon)
                + " AND pokriveno!='X' AND vrdok IN "+vrd+sort;
    System.out.println(que);
    QueryDataSet ds = Util.getNewQueryDataSet(que, false);
    ds.setMetaDataUpdate(ds.getMetaDataUpdate() & ~MetaDataUpdate.ROWID);
    raProcess.setMessage("Dohvat otvorenih raèuna...", false);
    openScratchDataSet(ds);
    if (ds.rowCount() == 0) setNoDataAndReturnImmediately();
    ds.getColumn("CSKSTAVKE").setRowId(true);
    
    if (!isNon()) {
      raProcess.setMessage("Popravak salda raèuna...", false);
      
      Condition remDoc = Aus.getKnjigCond().and(Aus.getVrdokCond(jpp.isKupci())).
          and(Condition.where(datumCol, Condition.AFTER, dan));
      raSaldaKonti.updateOutOfRangeSaldo(ds, remDoc);
    }
    
    cs = Skstavke.getDataModule().getScopedSet("CPAR BROJDOK DATDOSP SALDO STAVKA");
    cs.open();
    String[] cc = {"CPAR", "BROJDOK", "DATDOSP", "SALDO"};
    
    repQDS.empty();
    repQDS.setSort(null);
    raProcess.setMessage("Obrada i zbrajanje raèuna...", false);
    int cpar = 0;
    boolean first = true, okPartner = true;
    for (ds.first(); ds.inBounds(); ds.next()) {
      checkClosing();
      BigDecimal sal = ds.getBigDecimal("SALDO");
      if (sal.signum() == 0 && raVrdokMatcher.isUplataTip(ds)) continue;
      if (first || cpar != ds.getInt("CPAR")) {
        first = false;
        cpar = ds.getInt("CPAR");
        boolean f = ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(cpar));
        okPartner = f && jpzg.checkPartner(dm.getPartneri()) &&
                     (jpp.getCpar() == 0 || jpp.getCpar() == cpar);
        if (okPartner) {
          repQDS.insertRow(false);
          repQDS.setInt("CPAR", cpar);
          repQDS.setString("NAZPAR", dm.getPartneri().getString("NAZPAR"));
          if (!dm.getPartneri().isNull("CAGENT")) {
            repQDS.setInt("CAGENT", dm.getPartneri().getInt("CAGENT"));
            ld.raLocate(dm.getAgenti(), "CAGENT", 
                String.valueOf(repQDS.getInt("CAGENT")));
            repQDS.setString("NAZAGENT", dm.getAgenti().getString("NAZAGENT"));
          }
        }
      }
      if (!okPartner) continue;
      
      Timestamp dosp = ut.getLastSecondOfDay(ds.getTimestamp("DATDOSP"));
      if (raVrdokMatcher.isUplataTip(ds))
        repQDS.setBigDecimal("TOTALU", repQDS.getBigDecimal("TOTALU").add(sal));
      else {
        if (!ds.getTimestamp("DATUMKNJ").before(d2))
          Aus.add(repQDS, "TOTALR", ds, jpp.isKupci() ? "ID" : "IP");
        repQDS.setBigDecimal("TOTAL", repQDS.getBigDecimal("TOTAL").add(sal));
        if (!dosp.after(dan) ^ dospPer) {
          repQDS.setBigDecimal("NDOSP", repQDS.getBigDecimal("NDOSP").add(sal));
        } else {
          int kas = raDateUtil.getraDateUtil().DateDifference(dosp, dan);
          cs.insertRow(false);
          dM.copyColumns(ds, cs, cc);
          cs.setShort("STAVKA", (short) kas);
          dispatchPeriods(sal, kas);
        }
      }
    }
    BigDecimal miniz = fields.getBigDecimal("MINIZNOS");
    //if (miniz.signum() == 0) miniz = new BigDecimal("0.01");
    repQDS.first();
    while (repQDS.inBounds()) {
      checkClosing();
      repQDS.setBigDecimal("TSAL", repQDS.getBigDecimal("TOTAL").subtract(repQDS.getBigDecimal("TOTALU")));
      if (repQDS.getBigDecimal("TOTAL").abs().compareTo(miniz) >= 0  ||
          repQDS.getBigDecimal("TOTALU").abs().compareTo(miniz) >= 0
          || jpp.getCpar() > 0) {
    	if (isKum()) sumPeriods();
        repQDS.next();
      } else repQDS.emptyRow();
    }
    if (repQDS.rowCount() == 0) setNoDataAndReturnImmediately();
    this.killAllReports();
    if (getPeriods() > (isUpl() ? 1 : 2))
      this.addReport("hr.restart.sk.repDugPotPeriod", "hr.restart.sk.repDugPotPer", "DugPotPeriodLand", "Ispis");
    else
      this.addReport("hr.restart.sk.repDugPotPeriod", "hr.restart.sk.repDugPotPer", "DugPotPeriod", "Ispis");
    System.out.println(getSelectedSort());
    if (!"$NAZPAR".equals(getSortColumn()))
      repQDS.setSort(getSelectedSort());
  }


  /**
   * @param sal je valjda saldo racuna
   * @param kas su dani kasnjenja racuna u odnosu na zadani datum
   */
  public void dispatchPeriods(BigDecimal sal, int kas) {
    int p1 = Aus.getNumber(jraP1.getText());
    int p2 = Aus.getNumber(jraP2.getText());
    int p3 = Aus.getNumber(jraP3.getText());
    int p4 = Aus.getNumber(jraP4.getText());
    if (!dospPer) kas=-kas;
    if (kas <= p1 || p1 == 0)
      repQDS.setBigDecimal("PER1", repQDS.getBigDecimal("PER1").add(sal));
    else if (kas <= p2 || p2 == 0)
      repQDS.setBigDecimal("PER2", repQDS.getBigDecimal("PER2").add(sal));
    else if (kas <= p3 || p3 == 0)
      repQDS.setBigDecimal("PER3", repQDS.getBigDecimal("PER3").add(sal));
    else if (kas <= p4 || p4 == 0 || isUpl())
      repQDS.setBigDecimal("PER4", repQDS.getBigDecimal("PER4").add(sal));
    else repQDS.setBigDecimal("PER5", repQDS.getBigDecimal("PER5").add(sal));
  }
  
  void sumPeriods() {
	int p1 = Aus.getNumber(jraP1.getText());
	int p2 = Aus.getNumber(jraP2.getText());
	int p3 = Aus.getNumber(jraP3.getText());
	int p4 = Aus.getNumber(jraP4.getText());
	if (p1 != 0) Aus.add(repQDS, "PER1", "NDOSP");
	if (p1 != 0) Aus.add(repQDS, "PER2", "PER1");
	if (p2 != 0) Aus.add(repQDS, "PER3", "PER2");
	if (p3 != 0) Aus.add(repQDS, "PER4", "PER3");
	if (p4 != 0 && !isUpl()) Aus.add(repQDS, "PER5", "PER4");
  }

  public void firstESC() {
    rcc.EnabDisabAll(pan, true);
    if (raSaldaKonti.isDirect() && !jpk.isKontaAllow()) jpk.enabDohvat(false);
    jpp.focusCombo();
  }
  public boolean runFirstESC() {
    return false;
  }
  public boolean ispisNow() {
    if (!tab) return true;
    if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(this,
        "Prikazati rezultat u tablici?", "Tablièni prikaz",
        JOptionPane.OK_CANCEL_OPTION)) return true;
    
    showTable();
    firstESC();
    return false;
  }
  
  void showTable() {
    frmTableDataView view = new frmTableDataView();
    view.setTitle("Otvoreni promet "+
        (jpp.isKupci() ? "kupaca" : "dobavljaèa") + " po periodima"+
        "  na dan " + Aus.formatTimestamp(fields.getTimestamp("DATUM")));
    
    repQDS.getColumn("NDOSP").setCaption(dospPer ? "Nedospjelo" : "Dospjelo");
    String dz = dospPer ? "Do " : "Za ";
    
    int p1 = Aus.getNumber(jraP1.getText());
    int p2 = Aus.getNumber(jraP2.getText());
    int p3 = Aus.getNumber(jraP3.getText());
    int p4 = Aus.getNumber(jraP4.getText());
    if (isUpl()) p4 = 0;
    
    if (p1 != 0) repQDS.getColumn("PER1").setCaption(dz + p1 +" dana");
    else repQDS.getColumn("PER1").setCaption(dospPer ? "Dospjelo" : "Nedospjelo");
    if (p2 != 0) repQDS.getColumn("PER2").setCaption(dz + p2 +" dana");
    else repQDS.getColumn("PER2").setCaption("Preko " + p1 +" dana");
    if (p3 != 0) repQDS.getColumn("PER3").setCaption(dz + p3 +" dana");
    else repQDS.getColumn("PER3").setCaption("Preko " + p2 +" dana");
    if (p4 != 0) repQDS.getColumn("PER4").setCaption(dz + p4 +" dana");
    else repQDS.getColumn("PER4").setCaption("Preko " + p3 +" dana");
    if (p4 != 0) repQDS.getColumn("PER5").setCaption("Preko " + p4 +" dana");
    
    repQDS.getColumn("PER5").setVisible(p4 == 0 ? 0 : 1);
    repQDS.getColumn("PER4").setVisible(p3 == 0 ? 0 : 1);
    repQDS.getColumn("PER3").setVisible(p2 == 0 ? 0 : 1);
    repQDS.getColumn("PER2").setVisible(p1 == 0 ? 0 : 1);
    view.setDataSet(repQDS);
    view.setSums(new String[] {"TOTALR", "TSAL", "TOTALU", 
        "TOTAL", "NDOSP", "PER1", "PER2", "PER3", "PER4", "PER5"});
    view.setSaveName("sk.openrac");
    view.show();
  }

  boolean tab;
  private void init() throws Exception {
    tab = "D".equalsIgnoreCase(frmParam.getParam("sk", "openSKtab", "D",
        "Opcija prikaza otvorenog prometa u tablici (D,N)"));

    fields.setColumns(new Column[] {
      dM.createStringColumn("CORG", "Org. jedinica", 12),
      dM.createIntColumn("CPAR", "Partner"),
      dM.createIntColumn("NAZPAR", "Partner"),
      dM.createStringColumn("BROJKONTA", "Konto", 8),
      dM.createStringColumn("CGRPAR", "Grupa", 10),
      dM.createShortColumn("CZUP", "Županija"),
      dM.createIntColumn("PBR", "Poštanski broj"),
      dM.createTimestampColumn("DATUM", "Na dan"),
      dM.createBigDecimalColumn("MINIZNOS", "Minimalni iznos"),
      dM.createIntColumn("CAGENT", "Agent")
    });
    fields.open();

    initRepQDSColumns();
    repQDS.open();
    
    dkAdd = raSaldaKonti.isDirect() ? 25 : 0;

    pan.setLayout(lay);
    lay.setWidth(585);
    lay.setHeight(260 + dkAdd);

//    jlMjesto.setText("Mjesto");
//    jlZup.setText("Županija");
    rcbDat.setRaItems(new String[][] {
        {"Na dan", "D"},
        {"Knjiženo do", "K"}
      });
    rcbDat.setSelectedIndex(0);
    //jlDan.setText("Na dan");
    jlIznos.setText("Minimalni iznos");

    jpc.bind(fields);
    jpp.bind(fields);
    jpzg.bind(fields);
    jpk.bind(fields);
//    jraMjesto.setColumnName("MJ");
//    jraMjesto.setDataSet(fields);
    jraDan.setColumnName("DATUM");
    jraDan.setDataSet(fields);
    jraIznos.setColumnName("MINIZNOS");
    jraIznos.setDataSet(fields);
    
    jcbUpl.setText(" Prikaz otvorenih uplata ");
    jcbUpl.setHorizontalAlignment(JLabel.LEADING);
    jcbUpl.setHorizontalTextPosition(JLabel.TRAILING);
    
    jcbKum.setText(" Kumulativni prikaz perioda ");
    jcbKum.setHorizontalAlignment(JLabel.LEADING);
    jcbKum.setHorizontalTextPosition(JLabel.TRAILING);
    
    jcbNon.setText(" Prikaz trenutaènog salda raèuna i uplata bez obzira na period ");
    jcbNon.setHorizontalAlignment(JLabel.LEADING);
    jcbNon.setHorizontalTextPosition(JLabel.TRAILING);

/*    jlrZup.setColumnName("CZUP");
    jlrZup.setDataSet(fields);
    jlrZup.setColNames(new String[] {"NAZIVZUP"});
    jlrZup.setTextFields(new JTextComponent[] {jlrNazivZup});
    jlrZup.setVisCols(new int[] {0, 1});
    jlrZup.setSearchMode(0);
    jlrZup.setRaDataSet(dm.getZupanije());
    jlrZup.setNavButton(jbSelZup);

    jlrNazivZup.setColumnName("NAZIVZUP");
    jlrNazivZup.setNavProperties(jlrZup);
    jlrNazivZup.setSearchMode(1); */
    
    jlPeriod.setText("Vrsta raèuna za raspodjelu po periodima");
    bg.add(jrbDosp, " Dospjeli ");
    bg.add(jrbNedosp, " Nedospjeli ");
    bg.setSelected(jrbDosp);

    jlKas.setText("Granice perioda u danima");
    new raTextMask(jraP1, 4, false, raTextMask.DIGITS);
    new raTextMask(jraP2, 4, false, raTextMask.DIGITS);
    new raTextMask(jraP3, 4, false, raTextMask.DIGITS);
    new raTextMask(jraP4, 4, false, raTextMask.DIGITS);
    jraP1.setHorizontalAlignment(JLabel.CENTER);
    jraP2.setHorizontalAlignment(JLabel.CENTER);
    jraP3.setHorizontalAlignment(JLabel.CENTER);
    jraP4.setHorizontalAlignment(JLabel.CENTER);
    jraP1.setText("15");
    jraP2.setText("30");
    jraP3.setText("45");
    jraP4.setText("60");

    pan.add(jpc, new XYConstraints(0, 20, -1, -1));
    if (raSaldaKonti.isDirect())
      pan.add(jpk, new XYConstraints(0, 45, -1, -1));
    pan.add(jpp, new XYConstraints(0, 45 + dkAdd, -1, -1));
    pan.add(jpzg, new XYConstraints(0, 70 + dkAdd, -1, -1));
//    pan.add(jlMjesto, new XYConstraints(15, 72, -1, -1));
//    pan.add(jraMjesto, new XYConstraints(150, 70, 100, -1));
//    pan.add(jlZup, new XYConstraints(275, 72, -1, -1));
//    pan.add(jlrZup, new XYConstraints(340, 70, 50, -1));
//    pan.add(jlrNazivZup, new XYConstraints(395, 70, 150, -1));
//    pan.add(jbSelZup, new XYConstraints(550, 70, 21, 21));
    pan.add(rcbDat, new XYConstraints(15, 95 + dkAdd, 130, -1));
    pan.add(jraDan, new XYConstraints(150, 95 + dkAdd, 100, -1));
    pan.add(jlIznos, new XYConstraints(320, 97 + dkAdd, -1, -1));
    pan.add(jraIznos, new XYConstraints(445, 95 + dkAdd, 100, -1));
    
    pan.add(jlPeriod, new XYConstraints(15, 147 + dkAdd, -1, -1));
    pan.add(jrbDosp, new XYConstraints(290, 145 + dkAdd, 125, -1));
    pan.add(jrbNedosp, new XYConstraints(420, 145 + dkAdd, 125, -1));
    
    pan.add(jlKas, new XYConstraints(15, 172 + dkAdd, -1, -1));
    pan.add(jraP1, new XYConstraints(290, 170 + dkAdd, 60, -1));
    pan.add(jraP2, new XYConstraints(355, 170 + dkAdd, 60, -1));
    pan.add(jraP3, new XYConstraints(420, 170 + dkAdd, 60, -1));
    pan.add(jraP4, new XYConstraints(485, 170 + dkAdd, 60, -1));
    pan.add(jcbUpl, new XYConstraints(15, 195 + dkAdd, -1, -1));
    pan.add(jcbKum, new XYConstraints(290, 195 + dkAdd, -1, -1));    
    pan.add(jcbNon, new XYConstraints(15, 220 + dkAdd, -1, -1));
    
    jcbUpl.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        rcc.setLabelLaF(jraP4, e.getStateChange() == e.DESELECTED);
      }
    });
    jcbUpl.setSelected();
    


    FocusListener checker = new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JraTextField)
          checkPeriod();
      }
    };
    jraP1.addFocusListener(checker);
    jraP2.addFocusListener(checker);
    jraP3.addFocusListener(checker);
    jraP4.addFocusListener(checker);
//    setSortCummulative(true);
    addSortOption("CPAR", "Šifra partnera");
    addSortOption("$NAZPAR", "Naziv partnera");
    addSortOption("-TOTAL", "Saldo otvorenih raèuna");
    addSortOption("-TSAL", "Ukupni saldo");
//    addSortOption("TOTAL", "Saldo otvorenih raèuna - najmanji");
    installResetButton();
    this.setJPan(pan);
  }

  public void initRepQDSColumns() {
    initRepQDSColumnsBasic();
    initRepQDSCoumnsPeriod();
  }
  
  public void initRepQDSColumnsBasic() {
    repQDS.setColumns(new Column[] {
      dM.createIntColumn("CPAR", "Šifra"),
      Partneri.getDataModule().getColumn("NAZPAR").cloneColumn(),
      dM.createIntColumn("CAGENT", "Agent"),
      Agenti.getDataModule().getColumn("NAZAGENT").cloneColumn(),
      dM.createBigDecimalColumn("TOTALR", "Ukupni promet"),
      dM.createBigDecimalColumn("TSAL", "Ukupni saldo"),
      dM.createBigDecimalColumn("TOTALU", "Otvorene uplate"),
      dM.createBigDecimalColumn("TOTAL", "Otvoreni raèuni"),
      dM.createBigDecimalColumn("NDOSP", "Nedospjelo")
    });
  }
  
  public void initRepQDSCoumnsPeriod() {
    repQDS.addColumn(dM.createBigDecimalColumn("PER1"));
    repQDS.addColumn(dM.createBigDecimalColumn("PER2"));
    repQDS.addColumn(dM.createBigDecimalColumn("PER3"));
    repQDS.addColumn(dM.createBigDecimalColumn("PER4"));
    repQDS.addColumn(dM.createBigDecimalColumn("PER5"));
  }

  private void checkPeriod() {
    int previous = 0;
    for (int i = 0; i < 4; i++) {
      int num = Aus.getNumber(all[i].getText());
      if (num == 0 || previous < 0) num = -1;
      else if (num <= previous) num = previous + 1;
      previous = num;
      all[i].setText(num == -1 ? "" : String.valueOf(num));
    }
  }
  
  protected void afterKonto(boolean succ) {
    DataRow konto = !succ ? null : jpk.getKontoRow();
    if (konto != null) {
      if (konto.getString("SALDAK").equalsIgnoreCase("D") && jpp.isKupci()) 
        jpp.setKupci(false);
      else if (konto.getString("SALDAK").equalsIgnoreCase("K") && !jpp.isKupci())
        jpp.setKupci(true);
    }
    if (succ) jpp.focusCparLater();
  }
    
  void checkKupDob(boolean kupac) {
    DataRow konto = jpk.getKontoRow();
    if (konto != null) {
      if (konto.getString("SALDAK").equalsIgnoreCase("D") && jpp.isKupci())
        jpk.clear();
      else if (konto.getString("SALDAK").equalsIgnoreCase("K") && !jpp.isKupci())
        jpk.clear();
    }
  }
}
