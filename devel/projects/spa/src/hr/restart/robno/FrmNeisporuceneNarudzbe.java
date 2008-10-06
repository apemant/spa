/****license*****************************************************************
**   file: FrmNeisporuceneNarudzbe.java
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
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raUpitFat;
import hr.restart.util.sysoutTEST;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author S.G.
 *
 * Started 2005.04.29
 * 
 */

public class FrmNeisporuceneNarudzbe extends raUpitFat {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rutil = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();

  JPanel jp = new JPanel();
  TableDataSet fieldSet = new TableDataSet();
  protected XYLayout xYLayout3 = new XYLayout();

  JLabel jlCskl = new JLabel();
  JlrNavField jlrCskl = new JlrNavField();
  JlrNavField jlrNazskl = new JlrNavField();
  JraButton jbSelCskl = new JraButton();

  JLabel jlDatum1 = new JLabel();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocDatum = new JraTextField();
  
  private String skladistaCache = "";
  
  public FrmNeisporuceneNarudzbe(){
    try {
      initializer();
    } catch (Exception e) {
    }
  }

  public void componentShow() {
    setDataSet(null);
    fieldSet.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    fieldSet.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
  }

  public String navDoubleClickActionName() {
    return "Narudžba";
  }

  public int[] navVisibleColumns() {
    return new int[] {0,1,2,3,4};
  }

  int nTransData=0;
  java.sql.Timestamp nTransDate;
  String nTransCskl="";
  
  public void jptv_doubleClick() {
    String cskl = this.getJPTV().getDataSet().getString("CSKL");
    String brdok = this.getJPTV().getDataSet().getInt("BRDOK")+"";
    String vrdok = this.getJPTV().getDataSet().getString("VRDOK");
    String god = this.getJPTV().getDataSet().getString("GOD");

    raMasterDetail.showRecord("hr.restart.robno.raNKU", 
        new String[]{"CSKL", "VRDOK", "GOD", "BRDOK"}, 
        new String[]{cskl, vrdok, god, brdok}
    );
  }
  
  public void okPress() {
    sysoutTEST st = new sysoutTEST(false);
    st.prn(fieldSet);
    
    QueryDataSet finalSet = ut.getNewQueryDataSet(upitString(), false);
    
    finalSet.setColumns(new Column[] {(Column) dm.getDoki().getColumn("CSKL").clone(),
        							  (Column) dm.getDoki().getColumn("VRDOK").clone(),
        							  (Column) dm.getDoki().getColumn("GOD").clone(),
        							  (Column) dm.getDoki().getColumn("BRDOK").clone(),
        							  (Column) dm.getDoki().getColumn("CPAR").clone(),
        							  (Column) dm.getDoki().getColumn("DATDOK").clone()/*,
        							  dm.createIntColumn("BNSP","Broj neiskalkuliranih stavaka")*/});
    finalSet.open();
    finalSet.getColumn("GOD").setVisible(0);
    
    if (finalSet == null || finalSet.rowCount() < 1) setNoDataAndReturnImmediately();
    
    rcc.setLabelLaF(this.getOKPanel().jBOK,false);
    
    setDataSet(finalSet);
  }
  
  private String upitString(){
    String upitnik,cskl,period;
    
    period = "AND doki.datdok BETWEEN "+rutil.getTimestampValue(fieldSet.getTimestamp("pocDatum"), 0)+
    " AND "+rutil.getTimestampValue(fieldSet.getTimestamp("zavDatum"), 1)+" ";
    
    if (fieldSet.getString("CSKL").equals("")){
      
    QueryDataSet tempSkl = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("Knjig='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
    tempSkl.open();
    tempSkl.first();
    String skls = "(";

    do {
      skls += "'"+tempSkl.getString("CSKL")+"'";
      if (tempSkl.next()) skls += ",";
      else break;
    } while (true);

    skls += ")";
    skladistaCache = skls;
      
      cskl = "AND doki.cskl in "+skladistaCache+" ";
    } else {
      cskl = "AND doki.cskl = '"+fieldSet.getString("CSKL")+"' ";
    }
    
    upitnik = "SELECT "+

    "max(doki.cskl) as cskl, "+
    "max(doki.vrdok)as vrdok, "+ 
    "max(doki.god) as god, "+
    "max(doki.brdok) as brdok, "+ 
    "max(doki.cpar) as cpar, "+
    "max(doki.datdok) as datdok "+ 
//    "count(*) as bnsp "+ 

    "FROM doki, stdoki "+
    "WHERE doki.cskl = stdoki.cskl "+
    "AND doki.vrdok = stdoki.vrdok "+
    "AND doki.god = stdoki.god "+
    "AND doki.brdok = stdoki.brdok "+
    "AND stdoki.status = 'N' "+
    "AND doki.vrdok = 'NKU' "+
    
    cskl +
    period +
    
    "group by vrdok,brdok order by datdok";
    
    System.out.println(upitnik);
    
    return upitnik;
  }
  
  public boolean runFirstESC() {
    if (this.getJPTV().getDataSet() != null) return true;
    if (!jlrCskl.getText().equals("")) return true;
    return false;
  }
  
  public void firstESC() {
    rcc.EnabDisabAll(jp, true);
    rcc.setLabelLaF(this.getOKPanel().jBOK, true);
    if (this.getJPTV().getDataSet() != null) {
      this.getJPTV().clearDataSet();
      removeNav();
      jlrCskl.requestFocus();
      
    } else 
    if (!jlrCskl.getText().equals("")) {
      jlrCskl.setText("");
      jlrCskl.emptyTextFields();

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jlrCskl.requestFocus();
        }
      });
    }
  }
  
  private void initializer() throws Exception {
    this.setJPan(jp);

    fieldSet.setColumns(new Column[] {dm.createTimestampColumn("pocDatum","Od datuma"),
        							  dm.createTimestampColumn("zavDatum","Do datuma"),
        							  dm.createStringColumn("CSKL","Skladište",5)}); 

    jlDatum1.setText("Datum (od-do)");

    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(fieldSet);
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(fieldSet);
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jlCskl.setText("Skladište");
    
    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(fieldSet);
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0,1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(rutil.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String newk, String oldk) {
        jlrCskl.setRaDataSet(rutil.getSkladFromCorg());
      }
    });

    xYLayout3.setWidth(650);
    xYLayout3.setHeight(85);
    jp.setLayout(xYLayout3);
    
    jp.add(jlCskl, new XYConstraints(15, 22, -1, -1));
    jp.add(jlrCskl, new XYConstraints(150, 20, 100, -1));
    jp.add(jlrNazskl, new XYConstraints(255, 20, 350, -1));
    jp.add(jbSelCskl, new XYConstraints(610, 20, 21, 21));

    jp.add(jtfPocDatum, new XYConstraints(150, 45, 100, -1));
    jp.add(jtfZavDatum, new XYConstraints(255, 45, 100, -1));
    jp.add(jlDatum1, new XYConstraints(15, 47, -1, -1));
    
    this.getJPTV().addTableModifier(new hr.restart.swing.raTableColumnModifier("CPAR", new String[] {
		"CPAR", "NAZPAR" }, new String[] { "CPAR" }, dm.getPartneri()) {
		public int getMaxModifiedTextLength() {
			return 27;
		}
	});
    
    this.getJPTV().addTableModifier(new hr.restart.swing.raTableColumnModifier("CSKL", new String[] {
		"CSKL", "NAZSKL" }, new String[] { "CSKL" }, dm.getSklad()) {
		public int getMaxModifiedTextLength() {
			return 17;
		}
	});}

}
