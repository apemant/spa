/****license*****************************************************************
**   file: RobnoPostreceiver.java
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

import hr.restart.util.Valid;
import hr.restart.util.mail.DataPostreceiver;
import hr.restart.util.mail.DataReceiverLoadedData;
import hr.restart.util.mail.ui.DataReceiverShowData;

import com.borland.dx.sql.dataset.QueryDataSet;

public class RobnoPostreceiver implements DataPostreceiver {
	public boolean error=false;
	boolean deleted=false;

	public boolean run(DataReceiverLoadedData data) {
//		System.out.println("aaaaaaaaaaaaaaaaa: "+data);
		QueryDataSet[] dokis = data.getLoadedSetsByModule("stdoki");
		for (int i = 0; i < dokis.length; i++) {
			dokis[i].first();
			while (dokis[i].inBounds()) {
				findCart(dokis[i]);
				dokis[i].next();
			} 
			dokis[i].post();
		}
		QueryDataSet[] dokus = data.getLoadedSetsByModule("stdoku");
		for (int i = 0; i < dokus.length; i++) {
			dokus[i].first();
			while (dokus[i].inBounds()) {
				findCart(dokus[i]);
				dokus[i].next();
			} 
			dokus[i].post();
		}
		QueryDataSet[] meuus = data.getLoadedSetsByModule("stmeskla");
		for (int i = 0; i < meuus.length; i++) {
			meuus[i].first();
			while (meuus[i].inBounds()) {
				findCart(meuus[i], 2);
				meuus[i].next();
			} 
			meuus[i].post();
		}
		QueryDataSet[] paars = data.getLoadedSetsByModule("partneri");
		for (int i = 0; i < paars.length; i++) {
			paars[i].first();
			while (paars[i].inBounds()) {
				findCpar(paars[i]);
				if (!deleted) {
					paars[i].next();
				}
			} 
			paars[i].post();
		}
		return !error;
	}

	public String getShortInfo() {
		return "- robno: kontrola artikala i partnera";
	}
	void findCart(QueryDataSet qds) {
		findCart(qds,1);
	}
	void findCart(QueryDataSet qds, int mode) {
		String cart=qds.getString("CART1");
		Valid.getValid().execSQL("SELECT CART,BC,NAZART,JM FROM ARTIKLI WHERE CART1='"+cart+"'");
		Valid.getValid().RezSet.open();
		if (Valid.getValid().RezSet.getInt("CART")>0) {
			qds.setInt("CART", Valid.getValid().RezSet.getInt("CART"));
			qds.setString("BC", Valid.getValid().RezSet.getString("BC"));
			qds.setString("NAZART", Valid.getValid().RezSet.getString("NAZART"));
			qds.setString("JM", Valid.getValid().RezSet.getString("JM"));
		}
		else {
			error=true;
			String error;
			if (mode==2) {
				String skl;
				if (qds.getString("VRDOK").equalsIgnoreCase("MEI")) {
					skl=qds.getString("CSKLIZ");
				}
				else {
					skl=qds.getString("CSKLUL");
				}
				error="Ne postoji artikl sa skladišta: "+skl+"\n Artikl: "+qds.getString("CART1")+" "+qds.getString("NAZART");
			}
			else {
				error="Ne postoji artikl sa skladišta: "+qds.getString("CSKL")+"\n Artikl: "+qds.getString("CART1")+" "+qds.getString("NAZART");
			}
            if (DataReceiverShowData.getDRSDInstance()!=null) {
              DataReceiverShowData.getDRSDInstance().addError(error);
            } else {
              javax.swing.JOptionPane.showMessageDialog(null,error);
            }
		}
	}
	void findCpar(QueryDataSet qds) {
		int cpar=qds.getInt("CPAR");
		Valid.getValid().execSQL("SELECT CPAR FROM PARTNERI WHERE CPAR="+cpar);
		Valid.getValid().RezSet.open();
		System.out.println("Ekjuel: SELECT CPAR FROM PARTNERI WHERE CPAR="+cpar);
		if (Valid.getValid().RezSet.getRowCount()>0) {
			System.out.println("Obrisan partner: "+cpar);
			qds.deleteRow();
			deleted=true;
		}
		else {
			deleted=true;
		}
		
	}
    
}
