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
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.XYPanel;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class frmShemeRobno  extends raMasterDetail {
	
	raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  
  String[] mkey = {"RBR"};
  
  static frmShemeRobno frm;
  
  JPanel jpMaster;
  JPanel jpDetail;

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
  
  JraComboBox jcbKar = new JraComboBox(new String[] {"D", "P"});
  
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
		XYPanel basic = new XYPanel(getMasterSet());
		basic.text = 120;
		basic.bottom = 10;
		basic.down(10).label("Shema").text("RBR").text("OPIS", 400).nl();
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
		tabs.addTab("Zaglavlje", szag);
		tabs.addTab("Ažuriranje", supd);
		tabs.addTab("Stavke", sstav);
		
		JPanel ret = new JPanel(new BorderLayout());
		ret.add(basic, BorderLayout.NORTH);
		ret.add(tabs);
		return ret;
	}

	private JPanel createDetailPanel() {
		XYPanel basic = new XYPanel(getDetailSet());
		basic.text = 120;
		basic.label("Polje").text("POLJE").label("ako", 40).text("UVJET", 300).nl();
		basic.label("Konto").nav("BROJKONTA", dm.getKontaAnalitic(), "NAZIVKONTA").combo(jcbKar, 50).nl();
		basic.label("Knjiga").nav("CKNJIGE", dm.getKnjigeUI(), "NAZKNJIGE").nl();
		basic.label("Kolona").nav("CKOLONE", dm.getKoloneknjUI(), "NAZIVKOLONE").expand();
		
		return basic;
	}
	
	@Override
	public void SetFokusMaster(char mode) {
		super.SetFokusMaster(mode);
		Aus.dumpContainer(raMaster.jpDetailView, 2);
	}
}
