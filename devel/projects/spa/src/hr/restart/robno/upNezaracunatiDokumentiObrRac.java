/****license*****************************************************************
**   file: upNezaracunatiDokumentiObrRac.java
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
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raUpitFat;
import hr.restart.util.sysoutTEST;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class upNezaracunatiDokumentiObrRac extends raUpitFat {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rutil = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();

  JPanel jp = new JPanel();
  TableDataSet fieldSet = new TableDataSet();
  protected XYLayout xYLayout3 = new XYLayout();

  JLabel jlCorg = new JLabel();

  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
      if (!jlrCorg.getText().equals("")) {
        jpKup.cpar.requestFocus();
      }
    }
  };

  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
      if (!jlrCorg.getText().equals("")) {
        jpKup.cpar.requestFocus();
      }
    }
  };

  JraButton jbSelCorg = new JraButton();
  JLabel jlDatum1 = new JLabel();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocDatum = new JraTextField();
  
  protected raComboBox rcmbVrstaRacuna = new raComboBox();
  
  jpCpar jpKup = new jpCpar(){
    public void afterLookUp(boolean succ) {
      if (succ) {
        fieldSet.setString("CPAR",jpKup.getCpar()+"");
        jtfPocDatum.requestFocus();
      } else fieldSet.setString("CPAR","");
    }
  };
  
  public upNezaracunatiDokumentiObrRac() {
    try {
      init();
    } catch (Exception noInit) {
      noInit.printStackTrace();
    }
  }

  public void componentShow() {
    setDataSet(null);
    fieldSet.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    fieldSet.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    jpKup.clear();
    rcmbVrstaRacuna.setSelectedIndex(0);
    fieldSet.setString("VRDOK","all");
  }
  
  public boolean runFirstESC() {
    if (this.getJPTV().getDataSet() != null) return true;
    if (!jpKup.cpar.getText().equals("")) return true;
    if (!jlrCorg.getText().equals("")) return true;
    return false;
  }
  
  public void firstESC() {
    rcc.EnabDisabAll(jp, true);
    rcc.setLabelLaF(this.getOKPanel().jBOK, true);
    if (this.getJPTV().getDataSet() != null) {
      setDataSet(null);
      removeNav();
      if (!jlrCorg.getText().equals("")) {
        jlrCorg.requestFocus();
        jlrCorg.selectAll();
        return;
      }
    }
    if (!jpKup.cpar.getText().equals("")) {
      jpKup.clear();
      fieldSet.setString("CPAR","");
      jpKup.cpar.requestFocus();
    } else if (!jlrCorg.getText().equals("")) {
      jlrCorg.setText("");
      jlrCorg.emptyTextFields();
      jlrCorg.requestFocus();
    }
  }
  
  public void okPress() {
    sysoutTEST st = new sysoutTEST(false);
    st.prn(fieldSet);
    
    QueryDataSet finalSet = ut.getNewQueryDataSet(upitString(), false);
    
    finalSet.setColumns(new Column[] {(Column) dm.getDoki().getColumn("VRDOK").clone(),
        							  (Column) dm.getDoki().getColumn("CSKL").clone(),
        							  (Column) dm.getDoki().getColumn("GOD").clone(),
        							  (Column) dm.getDoki().getColumn("BRDOK").clone(),
        							  (Column) dm.getDoki().getColumn("CPAR").clone(),
        							  (Column) dm.getDoki().getColumn("DATDOK").clone(),
        							  dm.createIntColumn("BNZS","Broj nezaraèunatih stavki"),
        							  dm.createBigDecimalColumn("IZNOS","Iznos")
    });
    
    finalSet.open();
    finalSet.getColumn("CSKL").setVisible(0);
    finalSet.getColumn("GOD").setVisible(0);
    
    if (finalSet == null || finalSet.rowCount() < 1) setNoDataAndReturnImmediately();
    
    rcc.setLabelLaF(this.getOKPanel().jBOK,false);
    this.getJPTV().addTableModifier(new hr.restart.swing.raTableColumnModifier("CPAR",
        new String [] {"CPAR","NAZPAR"},
        new String[] {"CPAR"},
        dm.getPartneri()));
    setDataSetAndSums(finalSet, new String[] {"IZNOS"});
  }
  
  private String upitString(){
    String upitnik,cskl,vrdok,period,partner = "";
    
    if (!fieldSet.getString("CPAR").equalsIgnoreCase("")) partner = "AND doki.cpar = '"+fieldSet.getString("CPAR")+"' ";
    
    if (fieldSet.getString("VRDOK").equalsIgnoreCase("all")) vrdok = "AND doki.vrdok in ('RAC','GRN') ";
    else vrdok = "AND doki.vrdok = '"+fieldSet.getString("VRDOK")+"' ";
    
    period = "AND doki.datdok BETWEEN "+rutil.getTimestampValue(fieldSet.getTimestamp("pocDatum"), 0)+" AND "+rutil.getTimestampValue(fieldSet.getTimestamp("zavDatum"), 1)+" ";
    
    cskl = "AND doki.cskl = '"+fieldSet.getString("CORG")+"' ";
    
    upitnik = "SELECT "+

    "max(doki.vrdok)as vrdok, "+ 
    "max(doki.cskl) as cskl, "+
    "max(doki.god) as god, "+
    "max(doki.brdok) as brdok, "+ 
    "max(doki.cpar) as cpar, "+
    "max(doki.datdok) as datdok, "+ 
    "count(*) as bnzs, "+ 
    "max(doki.uirac) as iznos "+ 

    "FROM doki, stdoki "+
    "WHERE doki.cskl = stdoki.cskl "+
    "AND doki.vrdok = stdoki.vrdok "+
    "AND doki.god = stdoki.god "+
    "AND doki.brdok = stdoki.brdok "+
    "AND doki.status = 'N' "+
    "AND (doki.cradnal is null or doki.cradnal='') "+
    
    cskl +
    partner +
    vrdok +
    period +
    
    "group by doki.vrdok,doki.brdok";
    
    System.out.println(upitnik);
    
    return upitnik;
  }
  
  private void init() throws Exception{
    this.setJPan(jp);

    fieldSet.setColumns(new Column[] {dm.createTimestampColumn("pocDatum","Od datuma"),
        							  dm.createTimestampColumn("zavDatum","Do datuma"),
        							  dm.createStringColumn("CORG","Org. jedinica",5),
        							  dm.createStringColumn("CPAR","Partner",5),
        							  dm.createStringColumn("VRDOK",3)}); 

    rcmbVrstaRacuna.setRaDataSet(fieldSet);
    rcmbVrstaRacuna.setRaColumn("VRDOK");
    rcmbVrstaRacuna.setRaItems(new String[][]{{"Svi raèuni", "all"}, {"Gotovinski raèuni", "GRN"}, {"Bezgotovinski raèuni", "RAC"}});
    
    jlDatum1.setText("Datum (od-do)");

    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(fieldSet);
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(fieldSet);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jlCorg.setText("Org. jedinica");
    
    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fieldSet);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0,1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String newk, String oldk) {
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);
    
    jpKup.setPartnerOboje();
    
    xYLayout3.setWidth(650);
    xYLayout3.setHeight(110);
    jp.setLayout(xYLayout3);
    
    jp.add(jlCorg, new XYConstraints(15, 22, -1, -1));
    jp.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jp.add(jlrNaziv, new XYConstraints(255, 20, 350, -1));
    jp.add(jbSelCorg, new XYConstraints(610, 20, 21, 21));

    jp.add(jpKup, new XYConstraints(0, 45, -1, -1));

    jp.add(jtfPocDatum, new XYConstraints(150, 70, 100, -1));
    jp.add(jtfZavDatum, new XYConstraints(255, 70, 100, -1));
    jp.add(jlDatum1, new XYConstraints(15, 72, -1, -1));
    jp.add(rcmbVrstaRacuna,  new XYConstraints(360, 70, 245, 21));
  }
  
  public boolean isIspis() {
    return false;
  }
  
  
  public boolean Validacija() {
    if (fieldSet.getString("CORG").equalsIgnoreCase("")) return false;
    return true;
  }
  
  
  public void jptv_doubleClick() {
    String cskl = this.getJPTV().getDataSet().getString("CSKL");
    String brdok = this.getJPTV().getDataSet().getInt("BRDOK")+"";
    String vrdok = this.getJPTV().getDataSet().getString("VRDOK");
    String god = this.getJPTV().getDataSet().getString("GOD");
    
    System.out.println(cskl+", "+vrdok+", "+god+", "+brdok);
    raMasterDetail.showRecord("hr.restart.robno.ra"+vrdok, 
        new String[]{"CSKL", "VRDOK", "GOD", "BRDOK"}, 
        new String[]{cskl, vrdok, god, brdok}
    );
  }

  public String navDoubleClickActionName() {
    return "Raèun";
  }

  public int[] navVisibleColumns() {
    return new int[] {0,1,2,3,4,5};
  }
}
