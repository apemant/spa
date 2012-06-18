/****license*****************************************************************
**   file: frmRnser.java
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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.RN;
import hr.restart.baza.Rnser;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

public class frmRnser extends raMatPodaci {
	
	hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  Valid vl = Valid.getValid();
  Rbr rbr = Rbr.getRbr();
  lookupData ld = lookupData.getlookupData();
  hr.restart.baza.dM dm;
  
  jpRnser jp;
  
  String cradnal;
  int rbsid;

	public frmRnser() {
		super(2);
		try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
	}
	
	private void jbInit() throws Exception {

    dm = hr.restart.baza.dM.getDataModule();
    
    
    this.setRaQueryDataSet(Rnser.getDataModule().getTempSet("1=0"));
    this.setVisibleCols(new int[] {2,3,6,8,10});
    
    jp = new jpRnser(this);
    this.setRaDetailPanel(jp);
    jp.initRpcart();
    
    removeRnvCopyCurr();
    
    addOption(new raNavAction("Rastavi proces", raImages.IMGMOVIE, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        splitProc();
      }
    }, 4, true);
    
	}
	
	public void setStav(String cradnal, int rbsid) {
		this.cradnal = cradnal;
		this.rbsid = rbsid;
		Aus.setFilter(getRaQueryDataSet(), "SELECT * FROM Rnser WHERE cradnal='"+cradnal+"' AND rbsid="+rbsid);
	}
	
	public void beforeShow() {
    setTitle("Radne operacije naloga "+cradnal+" (stavka " + rbsid + ")");
    DataSet cr = RN.getDataModule().getTempSet(Condition.equal("CRADNAL", cradnal));
    cr.open();
    setEditEnabled(cr.rowCount()>0 && !cr.getString("STATUS").equals("Z"));
  }
	
	void splitProc() {
		if (getRaQueryDataSet().rowCount() == 0) return;
		
		QueryDataSet ds = Aut.getAut().expandArt(getRaQueryDataSet(), true);
		if (ds.rowCount() == 0 || ds.getInt("CART") == getRaQueryDataSet().getInt("CART")) {
			JOptionPane.showMessageDialog(this.jp, "Proces nije normiran!", "Greška",
          JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String[] ccs = {"CART", "CART1", "BC", "NAZART", "JM", "KOL"};
		
		this.getJpTableView().enableEvents(false);
		short rbs = rbr.vrati_rbr("RNSER","WHERE CRADNAL='"+cradnal+"' and rbsid="+rbsid);
		boolean first = true;
		for (ds.first(); ds.inBounds(); ds.next()) {
			if (!first) {
				getRaQueryDataSet().insertRow(false);
				getRaQueryDataSet().setString("CRADNAL", cradnal);
        getRaQueryDataSet().setInt("RBSID", rbsid);
        getRaQueryDataSet().setShort("RBR", rbs++);
			}
			first = false;
      dM.copyColumns(ds, getRaQueryDataSet(), ccs);
      if (ld.raLocate(dm.getArtikli(), "CART1", ds.getString("CART1"))) {
      	getRaQueryDataSet().setBigDecimal("ZC", dm.getArtikli().getBigDecimal("NC"));
      	Aus.mul(getRaQueryDataSet(), "VRI", "ZC", "KOL");
      }
		}
		getRaQueryDataSet().saveChanges();
		this.getJpTableView().enableEvents(true);
	}

  public void EntryPoint(char mode) {
    if (mode == 'N')
      jp.EraseFields();
    if (mode == 'I')
      jp.rpc.EnabDisab(false);
  }
	
	public void SetFokus(char mode) {
		if (mode == 'N') {
      getRaQueryDataSet().setString("CRADNAL", cradnal);
      getRaQueryDataSet().setInt("RBSID", rbsid);
      jp.rpc.setCART();
    } else if (mode == 'I') {
      jp.jraKOL.requestFocus();
    }
	}

	protected void ValidacijaNoviDetail() {

    this.getRaQueryDataSet().setShort("RBR", rbr.vrati_rbr("RNSER","WHERE CRADNAL='"+cradnal+"' and rbsid="+rbsid));
  }
	
	public boolean Validacija(char mode) {
		if (jp.rpc.getCART().trim().length() == 0) {
      JOptionPane.showMessageDialog(this.jp,"Obavezan unos Artikla!","Greška",
                                    JOptionPane.ERROR_MESSAGE);
      jp.rpc.EnabDisab(true);
      jp.rpc.setCART();
      return false;
    }
    int cart = Aus.getAnyNumber(jp.rpc.getCART());
    if (raVart.isStanje(cart)) {
    //if (!Aut.getAut().artTipa(Aut.getAut().getNumber(jpDetail.rpc.getCART()), "PU")) {
      JOptionPane.showMessageDialog(this.jp,"Artikl je materijal!",
                  "Greška", JOptionPane.ERROR_MESSAGE);
      jp.rpc.EnabDisab(true);
      jp.rpc.setCART();
      return false;
    }
    if (vl.isEmpty(jp.jraKOL)) return false;
    if (mode == 'N')
      ValidacijaNoviDetail();
    return true;
	}
	
	public void calc() {
		Aus.mul(getRaQueryDataSet(), "VRI", "ZC", "KOL");
	}
	
	public void AfterSave(char mode) {
    if (mode == 'N') {
      jp.EraseFields();
      jp.rpc.EnabDisab(true);
    }
  }

  public boolean ValDPEscape(char mode) {
    if (mode == 'N' && jp.rpcLostFocus) {
      jp.rpc.EnabDisab(true);
      jp.EraseFields();
      jp.rpc.setCART();
      return false;
    }
    return true;
  }

	public boolean rpcOut() {
		int cart = Aus.getAnyNumber(jp.rpc.getCART());
    if (raVart.isStanje(cart)) {
    //if (!Aut.getAut().artTipa(Aut.getAut().getNumber(jpDetail.rpc.getCART()), "PU")) {
      jp.EraseFields();
      Aut.getAut().handleRpcErr(jp.rpc, "Artikl se vodi na stanju!");
      return false;
    }
    ld.raLocate(dm.getArtikli(), "CART", jp.rpc.getCART());
    getRaQueryDataSet().setBigDecimal("ZC", dm.getArtikli().getBigDecimal("NC"));
    calc();
    jp.EnableFields();
    jp.jraKOL.requestFocus();
    return true;
	}
}
