/****license*****************************************************************
**   file: jpDetailPanelPRI.java
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

import hr.restart.swing.JraTextMultyKolField;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class jpDetailPanelPRI extends JPanel {

	//  java.util.ResourceBundle res =
	// java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
	hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass
			.getraCommonClass();

	hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();

	hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(
			false);

	JPanel jpDetailCenter = new JPanel();

	XYLayout xYLayout2 = new XYLayout();

//	JraTextField jtfKOL = new JraTextField();
	JraTextMultyKolField jtfKOL = new JraTextMultyKolField(){

		public void propertyChange(PropertyChangeEvent evt) {
//			System.out.println("evt.getPropertyName() "+evt.getPropertyName());
//			if (evt.getPropertyName().equalsIgnoreCase("KOL")) 
//			myKalkul(new FocusEvent(jtfKOL,FocusEvent.FOCUS_FIRST));
			
		}};	
	

	JTextField dummyTF = new JTextField();

	JLabel jlKOL = new JLabel();

	/*
	 * raArtiklUnos rpcart = new raArtiklUnos() { // rapancart rpcart = new
	 * rapancart(){ public void nextTofocus(){ jtfKOL.requestFocus(); } public
	 * void metToDo_after_lookUp(){ HmetToDo_after_lookUp(); }};
	 */
	rapancart rpcart = new rapancart() {
		public void nextTofocus() {
			jtfKOL.requestFocus();
		}

		public void metToDo_after_lookUp() {
			HmetToDo_after_lookUp();
		}
	};

	QueryDataSet StanjeSet = null;

	java.math.BigDecimal oldKOL;

	private frmPRI frmpri;

	public jpDetailPanelPRI(frmPRI frmpri) {
		try {
			this.frmpri = frmpri;
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jbInit() throws Exception {
		rpcart.setMyAfterLookupOnNavigate(false);
		rpcart.setFocusCycleRoot(true);

		jlKOL.setText("Koli\u010Dina");
		jlKOL.setHorizontalAlignment(SwingConstants.CENTER);
		jtfKOL.setColumnName("KOL");
		dummyTF.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(FocusEvent e) {
				jtfKOL.requestFocus();
			}
		});
		jpDetailCenter.setLayout(xYLayout2);
		jpDetailCenter.setBorder(BorderFactory.createEtchedBorder());
		rpcart.setBorder(BorderFactory.createEtchedBorder());
		xYLayout2.setHeight(55);
		jpDetailCenter.add(jlKOL, new XYConstraints(360, 20, 130, -1));
		jpDetailCenter.add(jtfKOL, new XYConstraints(500, 20, 130, -1));
		jpDetailCenter.add(dummyTF, new XYConstraints(550, 20, 1, 1));
		this.setLayout(new BorderLayout());
		this.add(jpDetailCenter, BorderLayout.CENTER);
		this.add(rpcart, BorderLayout.NORTH);

	}

	/**
	 * Iniciranje RAPANCART-a
	 */
	void initRpcart(QueryDataSet master, QueryDataSet detail) {
		rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(
				master.getTimestamp("DATDOK")));
		rpcart.setCskl(master.getString("CSKL"));
		//    rpcart.setTabela(detail);
		//  	rpcart.setDefParam();
		//    rpcart.setnextFocusabile(this.jtfKOL);

		rpcart.setTabela(detail);
		rpcart.setParam(hr.restart.sisfun.frmParam
				.getParam("robno", "indiCart"));
		rpcart.InitRaPanCart();
		rpcart.setMode("N");
	}

	void setDataSet(com.borland.dx.sql.dataset.QueryDataSet qds,
			com.borland.dx.sql.dataset.QueryDataSet qds2) {
		jtfKOL.setDataSet(qds);
		initRpcart(qds2, qds);
	}

	public void prepareNew() {
		rpcart.EnabDisab(true);
		rcc.setLabelLaF(jtfKOL, false);
		rpcart.SetDefFocus();
		//    rpcart.setDefFocus();
	}

	public void prepareChange() {
		rpcart.EnabDisab(false);
		rcc.setLabelLaF(jtfKOL, true);
		findStanje();
		jtfKOL.requestFocus();
		jtfKOL.selectAll();
	}

	public void findStanje() {
		if (StanjeSet != null)
			StanjeSet.close();
		StanjeSet = null;
		StanjeSet = hr.restart.util.Util.getNewQueryDataSet(
				"SELECT * FROM Artikli,Porezi,Stanje WHERE artikli.cpor = porezi.cpor "
						+ "AND artikli.cart = stanje.cart and stanje.cart ="
						+ frmpri.getDetailSet().getInt("CART")
						+ " and stanje.god = '"
						+ frmpri.getDetailSet().getString("GOD")
						+ "' and stanje.cskl = '"
						+ frmpri.getDetailSet().getString("CSKL") + "'", true);
	}

	public void HmetToDo_after_lookUp() {
		if (frmpri.raDetail.getMode() != 'B') {
			rpcart.EnabDisab(false);
			rcc.setLabelLaF(jtfKOL, true);
			findStanje();
			jtfKOL.selectAll();
		}
	}
}