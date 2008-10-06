/****license*****************************************************************
**   file: jpTotalAgent.java
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
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.swing.raTextMask;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



/**
 * @author abf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class jpTotalAgent extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  
  private JPanel jpGod = new JPanel();
  private XYLayout godLay = new XYLayout();
  
  private JPanel jpProm = new JPanel();
  private XYLayout promLay = new XYLayout();
  
  private JPanel jpPlac = new JPanel();
  private XYLayout placLay = new XYLayout();
  
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

  JLabel jlRac = new JLabel();
  JLabel jlPost = new JLabel();
  JLabel jlProv = new JLabel();
  JLabel jlTot = new JLabel();
  JLabel jlNeknj = new JLabel();
  JLabel jlKnj = new JLabel();
  JLabel jlNap = new JLabel();
  JLabel jlNenap = new JLabel();
  
  JraTextField jtRac = new JraTextField();
  JraTextField jtRacKnj = new JraTextField();
  JraTextField jtRacNap = new JraTextField();
  JraTextField jtRacNeknj = new JraTextField();
  JraTextField jtRacNenap = new JraTextField();
  
  JraTextField jtPost = new JraTextField();
  JraTextField jtPostKnj = new JraTextField();
  JraTextField jtPostNeknj = new JraTextField();
  JraTextField jtPostNap = new JraTextField();
  JraTextField jtPostNenap = new JraTextField();
  
  JraTextField jtProv = new JraTextField();
  JraTextField jtProvKnj = new JraTextField();
  JraTextField jtProvNeknj = new JraTextField();
  JraTextField jtProvNap = new JraTextField();
  JraTextField jtProvNenap = new JraTextField();
  
  JLabel jlProv2 = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlPlac = new JLabel();
  JLabel jlRaz = new JLabel();
  
  JraTextField jtIznos = new JraTextField();
  JraTextField jtPlac = new JraTextField();
  JraTextField jtRaz = new JraTextField();
  private JraTable2 jpt1 = new JraTable2();
  private JraTable2 jpt7 = new JraTable2();
  
  private StorageDataSet totals = new StorageDataSet();
  private StorageDataSet m1 = new StorageDataSet();
  private StorageDataSet m7 = new StorageDataSet();
  
  
      
  private String[] bgc = {"RAC", "RACKNJ", "RACNEKNJ", "RACNAP", "RACNENAP", "POSTPROV",
      "PROV", "PROVKNJ", "PROVNEKNJ", "PROVNAP", "PROVNENAP", "PROVUKUP", "PROVPLAC", "PROVOST"};
  
  public jpTotalAgent() {
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
      dM.createBigDecimalColumn("RAC", "Raèuni", 2),
      dM.createBigDecimalColumn("PROV", "Provizija", 2),
      dM.createBigDecimalColumn("PLAT", "Isplatiti", 2),
    };
    mj.setWidth(3);    
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
  
  private void createDataSet() {
    Column[] cols = new Column[bgc.length + 1];
    for (int i = 0; i < bgc.length; i++)
      cols[i] = dM.createBigDecimalColumn(bgc[i]);
    cols[bgc.length] = dM.createStringColumn("GOD", "Godina", 4);

    totals.setColumns(cols);
    
    totals.open();
    totals.insertRow(false);
    BigDecimal z = new BigDecimal(0);
    for (int i = 0; i < bgc.length; i++)
      totals.setBigDecimal(bgc[i], z);    
    totals.post();
    
    m1.setColumns(createColumns());
    fillDefaultData(m1, 1);
    m7.setColumns(createColumns());
    fillDefaultData(m7, 7);
  }
  
  private void bindTextFields() {
    jtRac.setDataSet(totals);
    jtRacKnj.setDataSet(totals);
    jtRacNeknj.setDataSet(totals);
    jtRacNap.setDataSet(totals);
    jtRacNenap.setDataSet(totals);    
    jtPost.setDataSet(totals);
    jtPostKnj.setDataSet(totals);
    jtPostNeknj.setDataSet(totals);
    jtPostNap.setDataSet(totals);
    jtPostNenap.setDataSet(totals);
    jtProv.setDataSet(totals);
    jtProvKnj.setDataSet(totals);
    jtProvNeknj.setDataSet(totals);
    jtProvNap.setDataSet(totals);
    jtProvNenap.setDataSet(totals);
    jtIznos.setDataSet(totals);
    jtPlac.setDataSet(totals);
    jtRaz.setDataSet(totals);
    jtGod.setDataSet(totals);
    
    jtRac.setColumnName("RAC");
    jtRacKnj.setColumnName("RACKNJ");
    jtRacNeknj.setColumnName("RACNEKNJ");
    jtRacNap.setColumnName("RACNAP");
    jtRacNenap.setColumnName("RACNENAP");
    jtPost.setColumnName("POSTPROV");
    jtPostKnj.setColumnName("POSTPROV");
    jtPostNeknj.setColumnName("POSTPROV");
    jtPostNap.setColumnName("POSTPROV");
    jtPostNenap.setColumnName("POSTPROV");
    jtProv.setColumnName("PROV");
    jtProvKnj.setColumnName("PROVKNJ");
    jtProvNeknj.setColumnName("PROVNEKNJ");
    jtProvNap.setColumnName("PROVNAP");
    jtProvNenap.setColumnName("PROVNENAP");
    jtIznos.setColumnName("PROVUKUP");
    jtPlac.setColumnName("PROVPLAC");
    jtRaz.setColumnName("PROVOST");
    jtGod.setColumnName("GOD");    
  }
  
  public DataSet getData() {
    return totals;
  }
    
  public String getGodina() {
    return jtGod.getText();
  }
  
  public void setGodina(String godina) {
    totals.setString("GOD", godina);
  }
  
  public void setMonthVals(int month, BigDecimal rac, BigDecimal prov, BigDecimal plat) {
    DataSet mon = month < 6 ? m1 : m7;

    mon.goToRow(month % 6);
    mon.setBigDecimal("RAC", rac);
    mon.setBigDecimal("PROV", prov);
    mon.setBigDecimal("PLAT", plat);
  }
  
  private void jbInit() throws Exception {
    createDataSet();
    bindTextFields();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    jpGod.setLayout(godLay);
    jtGod.setHorizontalAlignment(JLabel.CENTER);
    jbStart.setText(" Prikaži ");
    new raTextMask(jtGod, 4, false, raTextMask.DIGITS);
    godLay.setWidth(540);
    godLay.setHeight(50);
    jpGod.setBorder(BorderFactory.createEtchedBorder());
    jlGod.setText("Godina");
    jpGod.add(jlGod,  new XYConstraints(15, 15, -1, -1));
    jpGod.add(jtGod, new XYConstraints(175, 15, 50, -1));
    jpGod.add(jbStart, new XYConstraints(230, 15, -1, 21));
    this.add(jpGod);
    
    jpProm.setLayout(promLay);
    promLay.setWidth(540);
    promLay.setHeight(155);
    jpProm.setBorder(BorderFactory.createEtchedBorder());
    jlRac.setText("Neto iznos raèuna");
    jlRac.setHorizontalAlignment(JLabel.CENTER);
    jlPost.setText("Provizija (%)");
    jlPost.setHorizontalAlignment(JLabel.CENTER);
    jlProv.setText("Iznos provizije");
    jlProv.setHorizontalAlignment(JLabel.CENTER);
    jlTot.setText("Ukupno raèuni");
    jlNeknj.setText("Neproknjiženi raèuni");
    jlKnj.setText("Proknjiženi raèuni");
    jlNap.setText("Naplaæeni raèuni");
    jlNenap.setText("Nenaplaæeni raèuni");
    jpProm.add(jlRac, new XYConstraints(175, 2, 125, -1));
    jpProm.add(jlPost, new XYConstraints(305, 2, 80, -1));
    jpProm.add(jlProv, new XYConstraints(390, 2, 125, -1));
    jpProm.add(jlTot, new XYConstraints(15, 20, -1, -1));
    jpProm.add(jlNeknj, new XYConstraints(15, 45, -1, -1));
    jpProm.add(jlKnj, new XYConstraints(15, 70, -1, -1));
    jpProm.add(jlNap, new XYConstraints(15, 95, -1, -1));
    jpProm.add(jlNenap, new XYConstraints(15, 120, -1, -1));
    jpProm.add(jtRac, new XYConstraints(175, 20, 125, -1));
    jpProm.add(jtRacNeknj, new XYConstraints(175, 45, 125, -1));
    jpProm.add(jtRacKnj, new XYConstraints(175, 70, 125, -1));
    jpProm.add(jtRacNap, new XYConstraints(175, 95, 125, -1));
    jpProm.add(jtRacNenap, new XYConstraints(175, 120, 125, -1));
    jpProm.add(jtPost, new XYConstraints(305, 20, 80, -1));
    jpProm.add(jtPostNeknj, new XYConstraints(305, 45, 80, -1));
    jpProm.add(jtPostKnj, new XYConstraints(305, 70, 80, -1));
    jpProm.add(jtPostNap, new XYConstraints(305, 95, 80, -1));
    jpProm.add(jtPostNenap, new XYConstraints(305, 120, 80, -1));
    jpProm.add(jtProv, new XYConstraints(390, 20, 125, -1));
    jpProm.add(jtProvNeknj, new XYConstraints(390, 45, 125, -1));
    jpProm.add(jtProvKnj, new XYConstraints(390, 70, 125, -1));
    jpProm.add(jtProvNap, new XYConstraints(390, 95, 125, -1));
    jpProm.add(jtProvNenap, new XYConstraints(390, 120, 125, -1));
    this.add(jpProm);
    
    jpPlac.setLayout(placLay);
    placLay.setWidth(540);
    placLay.setHeight(55);
    jpPlac.setBorder(BorderFactory.createEtchedBorder());
    jlProv2.setText("Provizija");
    jlIznos.setText("Ukupno");
    jlIznos.setHorizontalAlignment(JLabel.CENTER);
    jlPlac.setText("Plaæeno");
    jlPlac.setHorizontalAlignment(JLabel.CENTER);
    jlRaz.setText("Ostatak");
    jlRaz.setHorizontalAlignment(JLabel.CENTER);
    jpPlac.add(jlIznos, new XYConstraints(175, 2, 110, -1));
    jpPlac.add(jlPlac, new XYConstraints(290, 2, 110, -1));
    jpPlac.add(jlRaz, new XYConstraints(405, 2, 110, -1));
    jpPlac.add(jlProv2, new XYConstraints(15, 20, -1, -1));
    jpPlac.add(jtIznos, new XYConstraints(175, 20, 110, -1));
    jpPlac.add(jtPlac, new XYConstraints(290, 20, 110, -1));
    jpPlac.add(jtRaz, new XYConstraints(405, 20, 110, -1));
    this.add(jpPlac);
    
    rcc.EnabDisabAll(jpProm, false);
    rcc.EnabDisabAll(jpPlac, false);
    
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
  }
  
  public void begin() {}
}
