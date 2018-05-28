/****license*****************************************************************
**   file: frmNormArt.java
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
import hr.restart.baza.dM;
import hr.restart.baza.norme;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raMatPodaci;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;

public class frmNormArt extends raMatPodaci {
	
	hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  Valid vl = Valid.getValid();
  Rbr rbr = Rbr.getRbr();
  lookupData ld = lookupData.getlookupData();
  hr.restart.baza.dM dm;
  
  jpNormArt jp;
    
  int cartnor;

	public frmNormArt() {
		super(2, DIALOG, frmArtikli.getInstance().getWindow());
		try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
	}
	
	private void jbInit() throws Exception {

    dm = hr.restart.baza.dM.getDataModule();
    
    this.setRaQueryDataSet(norme.getDataModule().getFilteredDataSet(Condition.nil));
    this.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,6});
    
    jp = new jpNormArt(this);
    this.setRaDetailPanel(jp);
    jp.initRpcart();
    
    removeRnvCopyCurr();    
	}
	
	public void setOwner(int cartnor) {
		this.cartnor = cartnor;
		Aus.setFilter(getRaQueryDataSet(), "SELECT * FROM norme WHERE cartnor="+cartnor);
	}
	
	public void beforeShow() {
		ld.raLocate(dm.getArtikli(), "CART", Integer.toString(cartnor));
    setTitle("Sastav normativa "+cartnor+" - " + dm.getArtikli().getString("NAZART"));
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
      jp.EraseFields();
      jp.rpc.EnabDisab(true);
      jp.rpc.setCART();
    } else if (mode == 'I') {
      jp.jraKOL.requestFocus();
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
    if (vl.isEmpty(jp.jraKOL)) return false;

    if (mode == 'N') {
    	if (norme.getDataModule().getRowCount(Condition.whereAllEqual(new String[] {"CARTNOR", "CART"}, getRaQueryDataSet())) > 0) {
      	JOptionPane.showMessageDialog(getWindow(), "Artikl veæ u popisu!",
            "Greška", JOptionPane.ERROR_MESSAGE);
      	jp.rpc.EnabDisab(true);
      	jp.EraseFields();
      	jp.rpc.setCART();
      	return false;
      }
    	if (isCircular(getRaQueryDataSet().getInt("CART"), cartnor)) {
    		JOptionPane.showMessageDialog(getWindow(), "Beskonaène petlje su nedopuštene!",
            "Greška", JOptionPane.ERROR_MESSAGE);
      	jp.rpc.EnabDisab(true);
      	jp.EraseFields();
      	jp.rpc.setCART();
      	return false;
    	}
    }
    return true;
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
    jp.EnableFields();
    jp.jraKOL.requestFocus();
    return true;
	}
	
	/*public static boolean isCircular(int cart, int destCart) {
		if (lookupData.getlookupData().raLocate(dM.getDataModule().getSortedNorme(), "CARTNOR", Integer.toString(cart))) 
			for (DataSet sn = dM.getDataModule().getSortedNorme(); sn.inBounds() && sn.getInt("CARTNOR") == cart; sn.next()) 
				if (sn.getInt("CART") == destCart) return true;
				else {
					int current = sn.getRow();
					if (isCircular(sn.getInt("CART"), destCart)) return true;
	        sn.goToRow(current);
				}
		return false;
	}*/
	
	public static boolean isCircular(int cart, int destCart) {
      if (norme.check(cart))
        for (int i = 0; i < norme.count(cart); i++)
          if (norme.cart(cart, i) != destCart) {
            if (isCircular(norme.cart(cart, i), destCart)) return true;
          } else return true;
          
      return false;
  }
}
