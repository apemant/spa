/****license*****************************************************************
**   file: frmRnus.java
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
import hr.restart.baza.Rnus;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

public class frmRnus extends raMatPodaci {
	
	hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  Valid vl = Valid.getValid();
  Rbr rbr = Rbr.getRbr();
  lookupData ld = lookupData.getlookupData();
  hr.restart.baza.dM dm;
  
  jpRnus jp;
  
  int cartnor;

	public frmRnus() {
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
    
    
    this.setRaQueryDataSet(Rnus.getDataModule().getTempSet("1=0"));
    this.setVisibleCols(new int[] {1,4,6,7});
    
    jp = new jpRnus(this);
    this.setRaDetailPanel(jp);
    jp.initRpcart();
    
    removeRnvCopyCurr();    
	}
	
	public void setOwner(int cartnor) {
		this.cartnor = cartnor;
		Aus.setFilter(getRaQueryDataSet(), "SELECT * FROM Rnus WHERE cartnor="+cartnor);
	}
	
	public void beforeShow() {
		ld.raLocate(dm.getArtikli(), "CART", Integer.toString(cartnor));
    setTitle("Nusproizvodi normativa "+cartnor+" - " + dm.getArtikli().getString("NAZART"));
  }

  public void EntryPoint(char mode) {
    if (mode == 'N')
      jp.EraseFields();
    if (mode == 'I')
      jp.rpc.EnabDisab(false);
  }
	
	public void SetFokus(char mode) {
		if (mode == 'N') {
      getRaQueryDataSet().setInt("CARTNOR", cartnor);
      jp.rpc.setCART();
    } else if (mode == 'I') {
      jp.jraKK.requestFocus();
    }
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
    if (!raVart.isStanje(cart)) {
    //if (!Aut.getAut().artTipa(Aut.getAut().getNumber(jpDetail.rpc.getCART()), "PU")) {
      JOptionPane.showMessageDialog(this.jp, "Artikl nije na stanju!",
                  "Greška", JOptionPane.ERROR_MESSAGE);
      jp.rpc.EnabDisab(true);
      jp.rpc.setCART();
      return false;
    }
    if (vl.isEmpty(jp.jraKK)) return false;
    if (vl.isEmpty(jp.jraKC)) return false;
    if (mode == 'N') {
    	vl.execSQL("select * from rnus WHERE cartnor = " + cartnor + " and cart = " + cart);
      vl.RezSet.open();
      if (vl.RezSet.rowCount() > 0) {
      	JOptionPane.showMessageDialog(this.jp, "Artikl veæ u popisu!",
            "Greška", JOptionPane.ERROR_MESSAGE);
      	jp.rpc.EnabDisab(true);
      	jp.rpc.setCART();
      	return false;
      }
    }
    return true;
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
    if (!raVart.isStanje(cart)) {
    //if (!Aut.getAut().artTipa(Aut.getAut().getNumber(jpDetail.rpc.getCART()), "PU")) {
      jp.EraseFields();
      Aut.getAut().handleRpcErr(jp.rpc, "Artikl se ne vodi na stanju!");
      return false;
    }
    jp.EnableFields();
    jp.jraKK.requestFocus();
    return true;
	}
}
