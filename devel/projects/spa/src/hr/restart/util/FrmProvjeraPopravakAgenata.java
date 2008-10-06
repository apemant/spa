/****license*****************************************************************
**   file: FrmProvjeraPopravakAgenata.java
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
package hr.restart.util;

import hr.restart.baza.Partneri;
import hr.restart.baza.dM;
import hr.restart.robno.rapancskl1;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class FrmProvjeraPopravakAgenata extends raUpitLite {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  
  TableDataSet fieldSet = new TableDataSet();
  
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfBrDok = new JraTextField();
  JraCheckBox jcbCheckNull = new JraCheckBox();
  protected raComboBox rcmbPeriod = new raComboBox();
  protected raComboBox rcmbVrdoksi = new raComboBox();
  protected rapancskl1 rpcskl = new rapancskl1(false,350);
  

  public FrmProvjeraPopravakAgenata() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void componentShow() {
    fieldSet.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    fieldSet.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    fieldSet.setString("VRDOK","");
    fieldSet.setString("BRDOK","");
    fieldSet.setString("PERIOD","2");
    rcmbPeriod.setSelectedIndex(2);
    rcmbVrdoksi.setSelectedIndex(0);
    jcbCheckNull.setSelected(false);
  }

  public boolean runFirstESC() {
    return false;
  }

  public void firstESC() {
    rcc.EnabDisabAll(jp,true);
    if (rcmbPeriod.getSelectedIndex() == 0){
      rcc.setLabelLaF(jtfPocDatum,false);
      rcc.setLabelLaF(jtfZavDatum,false);
    } else if (rcmbPeriod.getSelectedIndex() == 1){
      rcc.setLabelLaF(jtfPocDatum,true);
      rcc.setLabelLaF(jtfZavDatum,false);
    } else {
      rcc.setLabelLaF(jtfPocDatum,true);
      rcc.setLabelLaF(jtfZavDatum,true);
    }
  }
  
  QueryDataSet dokiz = null; 
  QueryDataSet partneri = null; 
  QueryDataSet agenti = null;
  boolean passed = false;
  boolean changed = false;
  boolean noData = false;

  public void okPress() {
    if (jcbCheckNull.isSelected()){
    partneri = dm.getPartneri();
    } else {
      partneri = Partneri.getDataModule().getFilteredDataSet("cagent > 0");
    }
    partneri.open();
    
    agenti = dm.getAgenti();
    agenti.open();
    
    String fbrdok = "";
    
    if (!fieldSet.getString("BRDOK").equals("")){
      fbrdok = "and brdok = "+fieldSet.getString("BRDOK");  
    }
    
    String fvrdok = "and vrdok in ('ROT','RAC')";
    
    if (!fieldSet.getString("VRDOK").equals("")){
      fbrdok = "and vrdok = '"+fieldSet.getString("VRDOK")+"'";  
    }
    
    String period = "";
    
    if (fieldSet.getString("PERIOD").equals("1")){
      period = "and datdok between "+util.getTimestampValue(fieldSet.getTimestamp("pocDatum"),util.NUM_FIRST)+" and "+util.getTimestampValue(vl.findDate(false,0),util.NUM_LAST);
    } else if (fieldSet.getString("PERIOD").equals("2")){
      period = "and datdok between "+util.getTimestampValue(fieldSet.getTimestamp("pocDatum"),util.NUM_FIRST)+" and "+util.getTimestampValue(fieldSet.getTimestamp("zavDatum"),util.NUM_LAST);
    }
    
    String qs = "Select * from doki where cskl = '"+rpcskl.getCSKL()+"' "+fvrdok+" "+fbrdok+" "+period+" order by datdok";
    
    System.out.println(qs); //XDEBUG delete when no more needed
    
    dokiz = ut.getNewQueryDataSet(qs); 
    
    if (dokiz.rowCount() < 1) {
      noData = true;
      setNoDataAndReturnImmediately();
    }
    
    dokiz.setRowId("CSKL",true);
    dokiz.setRowId("VRDOK",true);
    dokiz.setRowId("GOD",true);
    dokiz.setRowId("BRDOK",true);
    
    dokiz.first();
    
    int cpar = 0;
    int cage = 0;
    
    do {
      cpar = dokiz.getInt("CPAR");
      cage = dokiz.getInt("CAGENT");
      if (ld.raLocate(partneri, "CPAR", cpar + "")) {
        if (partneri.getInt("CAGENT") != cage) {
          dokiz.setInt("CAGENT", partneri.getInt("CAGENT"));
          changed = true;
        }
      } else {
        dokiz.setInt("CAGENT", 0);
      }

    } while (dokiz.next());

    if (changed) {
      passed = saveDataInMyneTransaction();
    }
  }

  protected boolean saveDataInMyneTransaction() {
    raLocalTransaction saveOurData = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          raTransaction.saveChanges(dokiz);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    return saveOurData.execTransaction();
  }
  
  public void afterOKPress() {
    if (noData){
     noData = false;
     return;
    }
    if (!changed){
      JOptionPane.showMessageDialog(this.getJPan(), new String[]{"Svi agenti su ispravno postavljeni"}, "Poruka", javax.swing.JOptionPane.INFORMATION_MESSAGE);
      firstESC();
      return;
    } else {
      if (passed) {
        JOptionPane.showMessageDialog(this.getJPan(), new String[]{"Popravak agenata uspješan"}, "Poruka", javax.swing.JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(this.getJPan(), new String[]{"Popravak agenata nije uspio"}, "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
      }
      firstESC();
    }
  }

  private void init() throws Exception {
    this.setJPan(jp);
    rpcskl.setDisabAfter(false);
    
    fieldSet.setColumns(new Column[] {
        dm.createTimestampColumn("pocDatum"), 
        dm.createTimestampColumn("zavDatum"),
        dm.createStringColumn("VRDOK",3),
        dm.createStringColumn("PERIOD",1),
        dm.createStringColumn("BRDOK",10)}
    );
    fieldSet.open();
    
    rcmbPeriod.setDataSet(fieldSet);
    rcmbPeriod.setColumnName("PERIOD");
    rcmbPeriod.setRaItems(new String[][] {
        {"Od poèetka vremena do danas","0"},
        {"Od datuma do danas","1"},
        {"Od datuma do datuma","2"}
      }
    );
    rcmbPeriod.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        if (rcmbPeriod.getSelectedIndex() == 0){
          rcc.setLabelLaF(jtfPocDatum,false);
          rcc.setLabelLaF(jtfZavDatum,false);
        } else if (rcmbPeriod.getSelectedIndex() == 1){
          rcc.setLabelLaF(jtfPocDatum,true);
          rcc.setLabelLaF(jtfZavDatum,false);
        } else {
          rcc.setLabelLaF(jtfPocDatum,true);
          rcc.setLabelLaF(jtfZavDatum,true);
        }
      }
    });
    
    rcmbVrdoksi.setDataSet(fieldSet);
    rcmbVrdoksi.setColumnName("VRDOK");
    rcmbVrdoksi.setRaItems(new String[][] {
        {"Svi dokumenti",""},
        {"ROT - Raèuni otpremnice","ROT"},
        {"RAC - Raèuni","RAC"}
      });

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(fieldSet);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(fieldSet);
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    
    jtfBrDok.setDataSet(fieldSet);
    jtfBrDok.setColumnName("BRDOK");
    
    jcbCheckNull.setText("Ažurirati i dokumente s partnerima koji nemaju agenta?");

    xYLayout1.setWidth(650);
    xYLayout1.setHeight(175);
    jp.setLayout(xYLayout1);

    jp.add(rpcskl, new XYConstraints(0, 10, -1, -1));
    jp.add(new JLabel("Period"), new XYConstraints(15, 35, -1, -1));
    jp.add(rcmbPeriod, new XYConstraints(150, 35, 455, -1));
    jp.add(new JLabel("Broj dokumenta"), new XYConstraints(15, 60, -1, -1));
    jp.add(jtfBrDok, new XYConstraints(150, 60, 100, -1));
    jp.add(new JLabel("Vrsta dokumenta"), new XYConstraints(15, 85, -1, -1));
    jp.add(rcmbVrdoksi, new XYConstraints(150, 85, 455, -1));
    jp.add(new JLabel("Datum (od - do)"), new XYConstraints(15, 110, -1, -1));
    jp.add(jtfPocDatum, new XYConstraints(150, 110, 100, -1));
    jp.add(jtfZavDatum, new XYConstraints(255, 110, 100, -1));
    jp.add(jcbCheckNull, new XYConstraints(150, 135, -1, -1));
  }
  
  public boolean isIspis() {
    return false;
  }
  
  public boolean ispisNow() {
    return false;
  }
  
  public boolean Validacija() {
    if (rpcskl.getCSKL().equals(""))
      return false;
    if (!fieldSet.getString("BRDOK").equals("")) {
      try {
        Integer.parseInt(fieldSet.getString("BRDOK"));
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this.getJPan(), new String[]{fieldSet.getString("BRDOK") + " nije broj!"}, "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    int upozorenje = JOptionPane.showConfirmDialog(this.getJPan(), new String[]{ "Ova akcija ima za posljedicu usklaðenje agenata na izlaznim dokumentima sa agentima", "koji su navedeni u adressaru partnera. Ukoliko je bilo promjene agenata unutar", "perioda koji se ažurira moglo bi doæi do neželjenih promjena, stoga vam preporuèavamo", "da prije ove akcije napravite kopiju svojih podataka i/ili navedete preciznije datumske", "periode za ispravak podataka. U protivnom ne odgovaramo za netoène podatke.", " ", "Želite li nastaviti?"}, "Upozorenje", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
    if (upozorenje == JOptionPane.NO_OPTION || upozorenje == -1)
      return false;
    return true;
  }

}
