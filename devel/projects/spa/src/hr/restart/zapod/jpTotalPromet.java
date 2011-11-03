/****license*****************************************************************
**   file: jpTotalPromet.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.swing.raTextMask;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
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

public class jpTotalPromet extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  private JPanel jpGod = new JPanel();
  private XYLayout godLay = new XYLayout();
  private JLabel jlGod = new JLabel();
  private JraTextField jtGod = new JraTextField() {
    public void addNotify() {
      super.addNotify();
      AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ENTER_RELEASED, new KeyAction() {
        public boolean actionPerformed() {
          begin();
          return true;
        }
      });
    }
  };
  private JraButton jbStart = new JraButton();

  private JPanel jpSaldak = new JPanel();
  private XYLayout skLay = new XYLayout();
  private JLabel jlProk = new JLabel();
  private JLabel jlDug = new JLabel();
  private JraTextField jtDug = new JraTextField();
  private JraTextField jtPot = new JraTextField();
  private JraTextField jtSaldo = new JraTextField();
  private JraTextField jtDugO = new JraTextField();
  private JraTextField jtDugPost = new JraTextField();
  private JLabel jlDugPost = new JLabel();
  private JraTextField jtDugOPost = new JraTextField();
  private JLabel jlPot = new JLabel();
  private JLabel jlSaldo = new JLabel();
  private JraTextField jtPotO = new JraTextField();
  private JraTextField jtSaldoO = new JraTextField();
  private JLabel jlProkO = new JLabel();
  private JLabel jlProkNN = new JLabel();
  private JLabel jlProkND = new JLabel();
  private JraTextField jtDugNN = new JraTextField();
  private JraTextField jtDugND = new JraTextField();
  private JraTextField jtDugNNPost = new JraTextField();
  private JraTextField jtDugNDPost = new JraTextField();
  private JraTextField jtPotNN = new JraTextField();
  private JraTextField jtPotND = new JraTextField();
  private JraTextField jtSaldoNN = new JraTextField();
  private JraTextField jtSaldoND = new JraTextField();
  private JPanel jpNeprok = new JPanel();
  private XYLayout npLay = new XYLayout();
  private JLabel jlNeprok = new JLabel();
  private JLabel jlIznos = new JLabel();
  private JLabel jlBroj = new JLabel();
  private JLabel jlProsjek = new JLabel();
  private JLabel jlProsjekRUC = new JLabel();
  private JLabel jlUkupno = new JLabel();
  
  private JPanel jpTotal = new JPanel();
  private XYLayout tLay = new XYLayout();
  private JLabel jlTotalSaldo = new JLabel();
  private JLabel jlTotal = new JLabel();
  private JLabel jlLimit = new JLabel();
  private JLabel jlRazlika = new JLabel();
  private JraTextField jtTotal = new JraTextField();
  private JraTextField jtLimit = new JraTextField();
  private JraTextField jtRazlika = new JraTextField();

  private JraTextField jtNepok = new JraTextField();
  private JraTextField jtNepokBroj = new JraTextField();
  private JraTextField jtNepokRucPros = new JraTextField();
  private JraTextField jtNepokPros = new JraTextField();

  private JraTextField jtUkup = new JraTextField();
  private JraTextField jtUkupBroj = new JraTextField();
  private JraTextField jtUkupRucPros = new JraTextField();
  private JraTextField jtUkupPros = new JraTextField();

  private JraTable2 jpt1 = new JraTable2();
  private JraTable2 jpt7 = new JraTable2();
  private StorageDataSet m1 = new StorageDataSet();
  private StorageDataSet m7 = new StorageDataSet();
  private StorageDataSet totals = new StorageDataSet();
  
  private JraCheckBox jcbPoc = new JraCheckBox();
  private boolean lastPoc;
  

  public jpTotalPromet() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private Column[] createColumns() {
    Column mj, p;
    Column[] cols = new Column[] {
      mj = dM.createIntColumn("MJ", "Mj"),
      dM.createBigDecimalColumn("VRI", "Prodaja", 2),
      dM.createBigDecimalColumn("RUC", "RuC", 2),
      p = dM.createBigDecimalColumn("RUCP", "(%)", 2),
    };
    mj.setWidth(3);
    p.setWidth(6);
    return cols;
  }

  private void fillDefaultData(StorageDataSet ds, int fm) {
    ds.open();
    ds.empty();
    for (int i = 1; i <= 6; ++i, ++fm) {
      ds.insertRow(false);
      ds.setInt("MJ", fm);
    }
    ds.post();
  }

  private String[] bgc = {"PROKDUG", "PROKP", "PROKPOT", "PROKSALDO", "OTDUG", "OTP",
    "OTPOT", "OTSALDO", "DOSPDUG", "DOSPP", "DOSPPOT", "DOSPSALDO", "NDODUG",
    "NDOP", "NDOPOT", "NDOSALDO", "NEPROK", "NEPROKPROS", "NEPROKRUCPROS",
    "UKUP", "UKUPPROS", "UKUPRUCPROS", "TOTAL", "LIMIT", "RAZLIKA"};


  private void createDataSets() {
    m1.setColumns(createColumns());
    fillDefaultData(m1, 1);
    m7.setColumns(createColumns());
    fillDefaultData(m7, 7);

    Column[] cols = new Column[bgc.length + 3];
    for (int i = 0; i < bgc.length; i++)
      cols[i] = dM.createBigDecimalColumn(bgc[i]);
    cols[bgc.length] = dM.createIntColumn("NEPROKBROJ");
    cols[bgc.length + 1] = dM.createIntColumn("UKUPBROJ");
    cols[bgc.length + 2] = dM.createStringColumn("GOD", "Godina", 4);

    totals.setColumns(cols);
    totals.open();
    totals.insertRow(false);
    BigDecimal z = new BigDecimal(0);
    for (int i = 0; i < bgc.length; i++)
      totals.setBigDecimal(bgc[i], z);
    totals.setInt("NEPROKBROJ", 0);
    totals.setInt("UKUPBROJ", 0);
    totals.post();
  }

  public void setMonthVals(int month, BigDecimal prod, BigDecimal ruc) {
    DataSet mon = month < 6 ? m1 : m7;

    mon.goToRow(month % 6);
    mon.setBigDecimal("VRI", prod);
    mon.setBigDecimal("RUC", ruc);
    mon.setBigDecimal("RUCP", rut.findPostotak(prod, ruc));
  }

  private void bindTextFields() {
    jtGod.setColumnName("GOD");
    jtGod.setDataSet(totals);
    jtNepokBroj.setColumnName("NEPROKBROJ");
    jtNepokBroj.setDataSet(totals);
    jtDug.setColumnName("PROKDUG");
    jtDug.setDataSet(totals);
    jtDugND.setColumnName("NDODUG");
    jtDugND.setDataSet(totals);
    jtDugNDPost.setColumnName("NDOP");
    jtDugNN.setColumnName("DOSPDUG");
    jtDugNNPost.setColumnName("DOSPP");
    jtDugO.setColumnName("OTDUG");
    jtDugOPost.setColumnName("OTP");
    jtDugPost.setColumnName("PROKP");
    jtPot.setColumnName("PROKPOT");
    jtPotND.setColumnName("NDOPOT");
    jtPotNN.setColumnName("DOSPPOT");
    jtPotO.setColumnName("OTPOT");
    jtSaldo.setColumnName("PROKSALDO");
    jtSaldoND.setColumnName("NDOSALDO");
    jtSaldoNN.setColumnName("DOSPSALDO");
    jtSaldoO.setColumnName("OTSALDO");
    jtNepok.setColumnName("NEPROK");
    jtNepokPros.setColumnName("NEPROKPROS");
    jtNepokRucPros.setColumnName("NEPROKRUCPROS");
    jtUkup.setColumnName("UKUP");
    jtUkupPros.setColumnName("UKUPPROS");
    jtUkupRucPros.setColumnName("UKUPRUCPROS");
    jtUkupBroj.setColumnName("UKUPBROJ");
    jtTotal.setColumnName("TOTAL");
    jtLimit.setColumnName("LIMIT");
    jtRazlika.setColumnName("RAZLIKA");
    jtDugNDPost.setDataSet(totals);
    jtDugNN.setDataSet(totals);
    jtDugNNPost.setDataSet(totals);
    jtDugO.setDataSet(totals);
    jtDugOPost.setDataSet(totals);
    jtDugPost.setDataSet(totals);
    jtPot.setDataSet(totals);
    jtPotND.setDataSet(totals);
    jtPotNN.setDataSet(totals);
    jtPotO.setDataSet(totals);
    jtSaldo.setDataSet(totals);
    jtSaldoND.setDataSet(totals);
    jtSaldoNN.setDataSet(totals);
    jtSaldoO.setDataSet(totals);
    jtNepok.setDataSet(totals);
    jtNepokPros.setDataSet(totals);
    jtNepokRucPros.setDataSet(totals);
    jtUkup.setDataSet(totals);
    jtUkupPros.setDataSet(totals);
    jtUkupRucPros.setDataSet(totals);
    jtUkupBroj.setDataSet(totals);
    jtTotal.setDataSet(totals);
    jtLimit.setDataSet(totals);
    jtRazlika.setDataSet(totals);
  }

  public DataSet getData() {
    return totals;
  }
  
  public String getGodina() {
    return jtGod.getText();
  }
  
  public boolean isPoc() {
    return jcbPoc.isSelected();
  }
  
  public void setGodina(String godina) {
    totals.setString("GOD", godina);
  }

  private void jbInit() throws Exception {
    createDataSets();
    bindTextFields();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    jpGod.setLayout(godLay);
    jtGod.setHorizontalAlignment(JLabel.CENTER);
    jbStart.setText(" Prikaži ");
    new raTextMask(jtGod, 4, false, raTextMask.DIGITS);
    godLay.setWidth(565);
    godLay.setHeight(50);
    jpGod.setBorder(BorderFactory.createEtchedBorder());
    jlGod.setText("Godina");
    jcbPoc.setText(" Ukljuèi poèetno stanje ");
    jcbPoc.setHorizontalTextPosition(JLabel.LEADING);
    jcbPoc.setHorizontalAlignment(JLabel.TRAILING);
    jcbPoc.setSelected(lastPoc = true);
    jpTotal.setLayout(tLay);
    tLay.setWidth(565);
    tLay.setHeight(55);
    jpTotal.setBorder(BorderFactory.createEtchedBorder());
    jlTotalSaldo.setText("Ukupno za kupca");
    jlTotal.setText("Saldo");
    jlTotal.setHorizontalAlignment(SwingConstants.CENTER);
    jlLimit.setText("Limit");
    jlLimit.setHorizontalAlignment(SwingConstants.CENTER);
    jlRazlika.setText("Razlika");
    jlRazlika.setHorizontalAlignment(SwingConstants.CENTER);
    
    jpSaldak.setLayout(skLay);
    skLay.setWidth(565);
    skLay.setHeight(130);
    jpSaldak.setBorder(BorderFactory.createEtchedBorder());
    jlProk.setText("Proknjiženi promet");
    jlDug.setHorizontalAlignment(SwingConstants.CENTER);
    jlDug.setText("Duguje");
    jlDugPost.setText("(%)");
    jlDugPost.setHorizontalAlignment(SwingConstants.CENTER);
    jlPot.setText("Potražuje");
    jlPot.setHorizontalAlignment(SwingConstants.CENTER);
    jlSaldo.setHorizontalAlignment(SwingConstants.CENTER);
    jlSaldo.setText("Saldo");
    jlProkO.setText("Otvoreni promet");
    jlProkNN.setText("Nenaplaæena realizacija");
    jlProkND.setText("Nedospjela potraživanja");
    jpNeprok.setLayout(npLay);
    npLay.setWidth(565);
    npLay.setHeight(80);
    jpNeprok.setBorder(BorderFactory.createEtchedBorder());
    jlNeprok.setText("Neproknjiženi raèuni");
    jlIznos.setText("Iznos");
    jlBroj.setText("Broj");
    jlProsjek.setText("Prosjeèni iznos");
    jlProsjekRUC.setText("Prosjeèna RuC");
    jlUkupno.setText("Ukupno raèuni");
    jlIznos.setHorizontalAlignment(SwingConstants.CENTER);
    jlBroj.setHorizontalAlignment(SwingConstants.CENTER);
    jlProsjek.setHorizontalAlignment(SwingConstants.CENTER);
    jlProsjekRUC.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(jpGod);
    jpGod.add(jlGod,  new XYConstraints(15, 15, -1, -1));
    jpGod.add(jtGod, new XYConstraints(175, 15, 50, -1));
    jpGod.add(jbStart, new XYConstraints(230, 15, -1, 21));
    jpGod.add(jcbPoc, new XYConstraints(350, 14, 200, -1));
    this.add(jpTotal);
    jpTotal.add(jlTotalSaldo, new XYConstraints(15, 20, -1, -1));
    jpTotal.add(jlTotal, new XYConstraints(230, 2, 100, -1));
    jpTotal.add(jtTotal, new XYConstraints(230, 20, 100, -1));
    jpTotal.add(jlLimit, new XYConstraints(340, 2, 100, -1));
    jpTotal.add(jtLimit, new XYConstraints(340, 20, 100, -1));
    jpTotal.add(jlRazlika, new XYConstraints(450, 2, 100, -1));
    jpTotal.add(jtRazlika, new XYConstraints(450, 20, 100, -1));
    this.add(jpSaldak);
    jpSaldak.add(jlProk, new XYConstraints(15, 20, -1, -1));
    jpSaldak.add(jlDug,  new XYConstraints(175, 2, 100, -1));
    jpSaldak.add(jtDug, new XYConstraints(175, 20, 100, -1));
    jpSaldak.add(jtPot,    new XYConstraints(340, 20, 100, -1));
    jpSaldak.add(jtSaldo,    new XYConstraints(450, 20, 100, -1));
    jpSaldak.add(jtDugO, new XYConstraints(175, 45, 100, -1));
    jpSaldak.add(jtDugPost, new XYConstraints(280, 20, 50, -1));
    jpSaldak.add(jlDugPost,  new XYConstraints(280, 2, 50, -1));
    jpSaldak.add(jtDugOPost, new XYConstraints(280, 45, 50, -1));
    jpSaldak.add(jlPot,   new XYConstraints(340, 2, 100, -1));
    jpSaldak.add(jlSaldo,    new XYConstraints(450, 2, 100, -1));
    jpSaldak.add(jtPotO,   new XYConstraints(340, 45, 100, -1));
    jpSaldak.add(jtSaldoO,    new XYConstraints(450, 45, 100, -1));
    jpSaldak.add(jlProkO, new XYConstraints(15, 45, -1, -1));
    jpSaldak.add(jlProkNN, new XYConstraints(15, 70, -1, -1));
    jpSaldak.add(jlProkND, new XYConstraints(15, 95, -1, -1));
    jpSaldak.add(jtDugNN, new XYConstraints(175, 70, 100, -1));
    jpSaldak.add(jtDugND, new XYConstraints(175, 95, 100, -1));
    jpSaldak.add(jtDugNNPost, new XYConstraints(280, 70, 50, -1));
    jpSaldak.add(jtDugNDPost, new XYConstraints(280, 95, 50, -1));
    jpSaldak.add(jtPotNN,   new XYConstraints(340, 70, 100, -1));
    jpSaldak.add(jtPotND,   new XYConstraints(340, 95, 100, -1));
    jpSaldak.add(jtSaldoNN,    new XYConstraints(450, 70, 100, -1));
    jpSaldak.add(jtSaldoND,    new XYConstraints(450, 95, 100, -1));

    this.add(jpNeprok);
    jpNeprok.add(jlNeprok, new XYConstraints(15, 20, -1, -1));
    jpNeprok.add(jlIznos, new XYConstraints(175, 2, 100, -1));
    jpNeprok.add(jlBroj, new XYConstraints(280, 2, 50, -1));
    jpNeprok.add(jlProsjek, new XYConstraints(340, 2, 100, -1));
    jpNeprok.add(jlProsjekRUC, new XYConstraints(450, 2, 100, -1));
    jpNeprok.add(jtNepok, new XYConstraints(175, 20, 100, -1));
    jpNeprok.add(jtNepokBroj, new XYConstraints(280, 20, 50, -1));
    jpNeprok.add(jtNepokRucPros, new XYConstraints(450, 20, 100, -1));
    jpNeprok.add(jtNepokPros, new XYConstraints(340, 20, 100, -1));
    jpNeprok.add(jlUkupno, new XYConstraints(15, 45, -1, -1));
    jpNeprok.add(jtUkup, new XYConstraints(175, 45, 100, -1));
    jpNeprok.add(jtUkupBroj, new XYConstraints(280, 45, 50, -1));
    jpNeprok.add(jtUkupRucPros, new XYConstraints(450, 45, 100, -1));
    jpNeprok.add(jtUkupPros, new XYConstraints(340, 45, 100, -1));

    rcc.EnabDisabAll(jpSaldak, false);
    rcc.EnabDisabAll(jpNeprok, false);
    rcc.EnabDisabAll(jpTotal, false);

    jpt1.setAutoResizeMode(jpt1.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    jpt7.setAutoResizeMode(jpt7.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    jpt1.setDataSet(m1);
    jpt7.setDataSet(m7);
    jpt1.stopFire();
    jpt7.stopFire();
    jpt1.setEnabled(false);
    jpt7.setEnabled(false);


    JPanel j1 = new JPanel(new BorderLayout());
    j1.add(jpt1);
    j1.add(jpt1.getTableHeader(), BorderLayout.NORTH);
    JPanel j7 = new JPanel(new BorderLayout());
    j7.add(jpt7);
    j7.add(jpt7.getTableHeader(), BorderLayout.NORTH);

    JPanel jsp = new JPanel(new BorderLayout());
    JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, j1, j7);
    sp.setResizeWeight(0.5);
    sp.setOneTouchExpandable(false);

    jsp.add(sp);
    this.add(jsp);

    jbStart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        begin();
      }
    });
    
    jcbPoc.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        checkChangePoc();
      }
    });
    jcbPoc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        checkChangePoc();
      }
    });
  }
  
  void checkChangePoc() {
    if (jcbPoc.isSelected() != lastPoc) {
      lastPoc = jcbPoc.isSelected();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          begin();
        }
      });
    }
  }

  public void begin() {}
}
