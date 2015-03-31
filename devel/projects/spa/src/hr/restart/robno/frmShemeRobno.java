/****license*****************************************************************
**   file: frmShemeRobno.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.RobSheme;
import hr.restart.baza.StRobSheme;
import hr.restart.baza.dM;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.XYPanel;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;

public class frmShemeRobno  extends raMasterDetail {
	
	raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  
  String[] mkey = {"RBR"};
  
  static frmShemeRobno frm;
  
  JPanel jpMaster;
  JPanel jpDetail;
  
  XYPanel xymaster, xydetail;

  JEditorPane zag = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  
  JEditorPane upd = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  
  JEditorPane stav = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  
  raComboBox jcbKar = new raComboBox();
  
  public frmShemeRobno() {
    super(2,3);
    frm = this;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {

    this.setMasterSet(RobSheme.getDataModule().copyDataSet());
    this.setNaslovMaster("Sheme knjiženja");
    this.setVisibleColsMaster(new int[] {0, 1, 2, 3});
    this.setMasterKey(mkey);
    
    jpMaster = createMasterPanel();
    this.setJPanelMaster(jpMaster);
    this.setMasterDeleteMode(DELDETAIL);
    
    raMaster.jpDetailView.remove(raMaster.jScrollPaneDetail);
    raMaster.jpDetailView.add(jpMaster);
    
    this.setDetailSet(StRobSheme.getDataModule().getFilteredDataSet(Condition.nil));
    this.setNaslovDetail("Stavke sheme");
    this.setVisibleColsDetail(new int[] {1, 2, 3, 4});
    this.setDetailKey(new String[] {"RBR", "RBS"});
    
    jpDetail = createDetailPanel();
    this.setJPanelDetail(jpDetail);
  }
  
	private JPanel createMasterPanel() {
		XYPanel basic = xymaster = new XYPanel(getMasterSet());
		basic.text = 120;
		basic.bottom = 10;
		basic.down(10).label("Shema").text("RBR").text("OPIS", 450).nl();
		basic.down(5).label("Dokumenti").text("CVRDOK", -1).nl();
		basic.down(5).label("Sklad/OJ").text("CCSKL", -1).nl();
		basic.down(5).label("Kljuè").text("ZAGKEY", -1).expand();
		
		JraScrollPane szag = new JraScrollPane();
		szag.setPreferredSize(new Dimension(500, 200));
		szag.setViewportView(zag);
		
		JraScrollPane supd = new JraScrollPane();
		supd.setPreferredSize(new Dimension(500, 200));
		supd.setViewportView(upd);
		
		JraScrollPane sstav = new JraScrollPane();
		sstav.setPreferredSize(new Dimension(500, 200));
		sstav.setViewportView(stav);
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Zaglavlja", szag);
		tabs.addTab("Ažuriranja", supd);
		tabs.addTab("Stavke", sstav);
		
		JPanel ret = new JPanel(new BorderLayout());
		ret.add(basic, BorderLayout.NORTH);
		ret.add(tabs);
		return ret;
	}

	private JPanel createDetailPanel() {
		XYPanel basic = xydetail = new XYPanel(getDetailSet());
		basic.text = 100;
		basic.label("Polje").text("RBS", 80).text("POLJE", 240).skip(15).check(" Zaglavlje salda konti ", "ZAG").nl();
		basic.label("Uvjet").text("UVJET", 520).nl();
		basic.label("Konto").nav(new String[] {"BROJKONTA", "NAZIVKONTA"}, new int[] {80, 300}, dm.getKontaAnalitic());
		basic.skip(30).combo(jcbKar, 100).nl();
		basic.label("Knjiga").nav(new String[] {"CKNJIGE", "NAZKNJIGE"}, new int[] {80, 300}, dm.getKnjigeUI()).nl();
		basic.label("Kolona").nav(new String[] {"CKOLONE", "NAZIVKOLONE"}, new int[] {80, 300}, dm.getKoloneknjUI());
		
		jcbKar.setRaDataSet(getDetailSet());
		jcbKar.setRaColumn("KARAKTERISTIKA");
	    jcbKar.setRaItems(new String[][] {
	        { "Dugovni", "D" },
	        { "Potražni", "P" }
	    });
		
		return basic.expand();
	}
	
	public void SetFokusMaster(char mode) {
	  if (mode=='N') {
	      rcc.setLabelLaF(xymaster.getText("RBR"), true);
	      DataSet mrbr = Aus.q("SELECT MAX(rbr) AS maxrbr FROM robsheme");
	      getMasterSet().setInt("RBR", mrbr.getInt("MAXRBR") + 1);
	      xymaster.getText("RBR").requestFocus();
	  } else if (mode=='I') {
	      rcc.setLabelLaF(xymaster.getText("RBR"),false);
	      xymaster.getText("OPIS").requestFocus();
	  }
	}
	
	public boolean ValidacijaMaster(char mode) {
	  if (mode == 'N') {
	    if (vl.isEmpty(xymaster.getText("RBR"))) return false;
	    if (vl.notUnique(xymaster.getText("RBR"))) return false;
	  }
	  
	  return true;
	}
	
	public void SetFokusDetail(char mode) {
	  if (mode=='N') {
        rcc.setLabelLaF(xydetail.getText("RBS"), true);
        DataSet mrbr = Aus.q("SELECT MAX(rbs) AS maxrbr FROM strobsheme WHERE " + Condition.equal("RBR", getMasterSet()));
        getDetailSet().setInt("RBS", mrbr.getInt("MAXRBR") + 1);
        xydetail.getText("RBS").requestFocus();
      } else if (mode=='I') {
        rcc.setLabelLaF(xydetail.getText("RBS"),false);
        xymaster.getText("POLJE").requestFocus();
      }
	}
	
	public boolean ValidacijaDetail(char mode) {
      if (mode == 'N') {
        if (vl.isEmpty(xymaster.getText("RBS"))) return false;
        if (StRobSheme.getDataModule().getRowCount(Condition.whereAllEqual(new String[] {"RBR", "RBS"}, getDetailSet())) > 0) {
          xymaster.getText("RBS").requestFocus();
          vl.showValidErrMsg(xymaster.getText("RBS"), 'U');
        }
      }
      
      return true;
    }
	
	public void masterSet_navigated(NavigationEvent e) {
	  if (getMasterSet().rowCount() == 0) {
	    zag.setText("");
	    upd.setText("");
	    stav.setText("");
	  } else {
	    zag.setText(getMasterSet().getString("QZAG"));
	    upd.setText(getMasterSet().getString("QUPD"));
	    stav.setText(getMasterSet().getString("QSTAV"));
	  }
	}

}
