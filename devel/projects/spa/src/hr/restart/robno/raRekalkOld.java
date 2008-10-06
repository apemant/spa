/****license*****************************************************************
**   file: raRekalkOld.java
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

import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.raUpitLite;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raRekalkOld extends raUpitLite {

	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	QueryDataSet tmpStanje = new QueryDataSet();

	private allSelect aSS = new allSelect();

	private raControlDocs rCD = new raControlDocs();

	private int cart = 0;

	hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

	java.math.BigDecimal Nula = new java.math.BigDecimal("0.00");

	String cskl = "2";

	String god = "2002";

	java.util.Date datDokumenta;

	JPanel jPanel1 = new JPanel();

	XYLayout xYLayout1 = new XYLayout();

	boolean isArtiklInUse = false;

	rapancskl1 rpcskl = new rapancskl1() {
		void jbInitRest(boolean how) throws Exception {
			super.jbInitRest(how);
			this.xYLayout1.setWidth(640);
			remove(jbCSKL);
			remove(jrfNAZSKL);
			add(jrfNAZSKL, new XYConstraints(255, 25, 348, -1));
			add(jbCSKL, new XYConstraints(609, 25, 21, 21));
		}

		public void MYpost_after_lookUp() {
		}
	};

	JLabel tekst = new JLabel("Godina");

	JraTextField poljesnova = new JraTextField();

	class myrapancart extends rapancart {
		public myrapancart(int i) {
			super(i);
		}

		public void Clear() {
			jrfCART.emptyTextFields();
			jrfCART1.emptyTextFields();
		}

		public void EnabDisabS(boolean how) {
			super.EnabDisab(how);
		}

		public void EnabDisab(boolean how) {
		}

		public void MYpost_after_lookUp() {
		}
	}

	myrapancart rpcart = new myrapancart(1);

	public raRekalkOld() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void okPress() {
		if (ValidateUnos()) {
			//			raProcess.runChild(this, new Runnable() {
			//				public void run() {
			startUpdateStanje();
			//				}
			//			});
		}
	}

	public boolean ValidateUnos() {
		if (rpcskl.jrfCSKL.getText().equals("")) {
			rpcskl.jrfCSKL.requestFocus();
			return false;
		} else if (!ValGod()) {
			poljesnova.setText("");
			poljesnova.requestFocus();
			return false;
		} else {
			cskl = rpcskl.jrfCSKL.getText();
			god = poljesnova.getText();
			EnabDisab(false);
			return true;
		}
	}

	public boolean ValGod() {

		int pero = 0;
		try {
			pero = Integer.parseInt(poljesnova.getText());
		} catch (Exception e) {
			return false;
		}
		if (pero < 1900 || pero > 3900)
			return false;
		return true;
	}

	public void EnabDisab(boolean kako) {
		rpcskl.disabCSKL(kako);
		rpcart.EnabDisabS(kako);
		hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
				poljesnova, kako);
		//    hr.restart.util.raCommonClass.getraCommonClass().EnabDisabAll(okp,kako);
	}

	void jbInit() throws Exception {

		rpcart.setMyAfterLookupOnNavigate(false);
		rpcart.setMode("DOH");
		rpcart.setnextFocusabile(poljesnova);
		rpcart.setBorder(null);
		xYLayout1.setWidth(645);
		xYLayout1.setHeight(145);
		jPanel1.setLayout(xYLayout1);
		jPanel1.setBorder(new javax.swing.border.EtchedBorder());
		rpcskl.setOverCaption(true);
		poljesnova.setHorizontalAlignment(SwingConstants.CENTER);
		jPanel1.add(rpcskl, new XYConstraints(0, 0, -1, -1)); //(0, 0, -1, -1)
		jPanel1.add(rpcart, new XYConstraints(0, 40, -1, 75));
		jPanel1.add(tekst, new XYConstraints(15, 115, -1, -1)); //15, 50, -1,
		jPanel1.add(poljesnova, new XYConstraints(150, 115, 100, -1));
		this.setJPan(jPanel1);

	}

	public void cancel() {
		oslobodi();
		setVisible(false);
	}

	public void ESC_izlaz(KeyEvent e) {
		if (rpcskl.jrfCSKL.getText().equals(""))
			cancel();
		else {
			oslobodi();
		}
		e.consume();
	}

	public void oslobodi() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EnabDisab(true);
				rpcskl.Clear();
				rpcart.Clear();
				rpcskl.jrfCSKL.requestFocus();
			}
		});
	}

	public void enableAllDataSetEvents(boolean kako) {
		tmpStanje.enableDataSetEvents(kako);
		//    tmpSkladStanjeArt.enableDataSetEvents(kako);
		//    tmpStdoki.enableDataSetEvents(kako);
		//    tmpStdoku.enableDataSetEvents(kako);
		//    tmpStmesklaUl.enableDataSetEvents(kako);
		//    tmpStmesklaIz.enableDataSetEvents(kako);

	}

	/*
	 * public void go_drugi() {
	 * 
	 * jProgressBar1.setValue(0); selectAllStanje();
	 * 
	 * jProgressBar2.setMaximum(1000); //
	 * jProgressBar2.setMaximum(tmpStdokuStanje.getRowCount()+ //
	 * tmpStdokiStanje.getRowCount()+ // tmpStanje.getRowCount());
	 * 
	 * InitDate(); jProgressBar1.setValue(0); clearAllStanje();
	 * jProgressBar1.setValue(0); kalkulStdoku(); jProgressBar1.setValue(0); //
	 * selectStdokiStanje(); // recalcStdokiStanje();
	 * 
	 * javax.swing.JOptionPane.showConfirmDialog(this,"Rekalkulacija stanja
	 * Završena !","Poruka",
	 * javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
	 * jProgressBar1.setValue(0); jProgressBar2.setValue(0); oslobodi(); } int
	 * cart = 0;
	 */
	public void startUpdateStanje() {
		try {
			cart = Integer.parseInt(rpcart.jrfCART.getText());
		} catch (NumberFormatException ex) {
			cart = 0;
		}
		raProcess.setMessage("Prikupljanje podataka ...", false);
		selectAllStanje();
		int j = tmpStanje.getRowCount();
		int i = 0;
		boolean isOK = true;
		for (tmpStanje.first(); tmpStanje.inBounds(); tmpStanje.next()) {
			nullAllStanje(tmpStanje);
			try {
				updateStdoku(tmpStanje.getString("CSKL"), tmpStanje
						.getString("GOD"), tmpStanje.getInt("CART"), tmpStanje);
				updateStmeskla(tmpStanje.getString("CSKL"), tmpStanje
						.getString("GOD"), tmpStanje.getInt("CART"), tmpStanje,
						true);

				findLastPrice(tmpStanje);
				kalkulateSum(tmpStanje);
				kalkulateNC(tmpStanje);
				kalkulateZC(tmpStanje);

				updateStdoki(tmpStanje.getString("CSKL"), tmpStanje
						.getString("GOD"), tmpStanje.getInt("CART"), tmpStanje);
				updateStmeskla(tmpStanje.getString("CSKL"), tmpStanje
						.getString("GOD"), tmpStanje.getInt("CART"), tmpStanje,
						false);
				kalkulateSum(tmpStanje);

				raProcess.setMessage("Obraðeno " + ++i + "/" + j, false);

			} catch (Exception ex) {
				ex.printStackTrace();
				isOK = false;
				break;
			}
		}

		raProcess.setMessage("Snimanje ......", false);
		if (isOK) {
			try {
				raTransaction.saveChanges(tmpStanje);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
		}

	}

	public void afterOKPress() {

		javax.swing.JOptionPane.showConfirmDialog(this,
				"Rekalkulacija stanja Završena !", "Poruka",
				javax.swing.JOptionPane.DEFAULT_OPTION,
				javax.swing.JOptionPane.DEFAULT_OPTION);
		oslobodi();
	}

	public void selectAllStanje() {
		if (cart != 0) {

			tmpStanje = hr.restart.util.Util.getNewQueryDataSet(
					"SELECT * FROM STANJE WHERE " + "CSKL='" + cskl
							+ "' AND GOD='" + god + "' AND CART=" + cart, true);
		} else {
			tmpStanje = hr.restart.util.Util.getNewQueryDataSet(
					"SELECT * FROM STANJE WHERE " + "CSKL='" + cskl
							+ "' AND GOD='" + god + "'", true);
		}

	}

	public String getSkladDocs(boolean ulaz) {
		VarStr var = new VarStr();
		for (int i = 0; i < TypeDoc.araj_docs.length; i++) {
			if (TypeDoc.getTypeDoc().isDocSklad(TypeDoc.araj_docs[i])) {
				if ((ulaz && TypeDoc.getTypeDoc().isDocStdoku(
						TypeDoc.araj_docs[i]))
						|| (!ulaz && TypeDoc.getTypeDoc().isDocStdoki(
								TypeDoc.araj_docs[i]))) {
					var = var.append("'").append(TypeDoc.araj_docs[i]).append(
							"',");
				}
			}
		}
		var = var.chopRight(1);
		return var.toString();
	}

	public void updateStdoki(String cskl, String god, int cart, DataSet stanje) {

		QueryDataSet qds = hr.restart.util.Util
				.getNewQueryDataSet(
						"SELECT sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
								+ "sum(iraz) as siraz from stdoki where cskl='"
								+ cskl
								+ "' and god='"
								+ god
								+ "' and cart="
								+ cart
								+ " and vrdok in ("
								+ getSkladDocs(false) + ")", true);
		System.out.println(qds.getQuery().getQueryString());

		if (qds.getRowCount() > 0) {
			stanje.setBigDecimal("KOLIZ", stanje.getBigDecimal("KOLIZ").add(
					qds.getBigDecimal("SKOL")));
			stanje.setBigDecimal("NABIZ", stanje.getBigDecimal("NABIZ").add(
					qds.getBigDecimal("SINAB")));
			stanje.setBigDecimal("MARIZ", stanje.getBigDecimal("MARIZ").add(
					qds.getBigDecimal("SIMAR")));
			stanje.setBigDecimal("PORIZ", stanje.getBigDecimal("PORIZ").add(
					qds.getBigDecimal("SIPOR")));
			stanje.setBigDecimal("VIZ", stanje.getBigDecimal("VIZ").add(
					qds.getBigDecimal("SIRAZ")));
		}
	}

	public void updateStdoku(String cskl, String god, int cart, DataSet stanje) {
		QueryDataSet qds = hr.restart.util.Util
				.getNewQueryDataSet(
						"SELECT max(vrdok) as svrdok,sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
								+ "sum(diopormar) as sdiopormar,"
								+ "sum(dioporpor) as sdioporpor,"
								+ "sum(porav) as sporav,"
								+ "sum(izad) as sizad from stdoku where cskl='"
								+ cskl
								+ "' and god='"
								+ god
								+ "' and cart="
								+ cart
								+ " and vrdok in ("
								+ "'KAL','PRE','PRK','INV','POR','PST'"
								+ ") group by vrdok", true);

		for (qds.first(); qds.inBounds(); qds.next()) {
			stanje.setBigDecimal("KOLUL", stanje.getBigDecimal("KOLUL").add(
					qds.getBigDecimal("SKOL")));
			stanje.setBigDecimal("NABUL", stanje.getBigDecimal("NABUL").add(
					qds.getBigDecimal("SINAB")));
			stanje.setBigDecimal("MARUL", stanje.getBigDecimal("MARUL").add(
					qds.getBigDecimal("SIMAR").add(
							qds.getBigDecimal("SDIOPORMAR"))));
			stanje.setBigDecimal("PORUL", stanje.getBigDecimal("PORUL").add(
					qds.getBigDecimal("SIPOR").add(
							qds.getBigDecimal("SDIOPORPOR"))));
			stanje.setBigDecimal("VUL", stanje.getBigDecimal("VUL")
					.add(
							qds.getBigDecimal("SIZAD").add(
									qds.getBigDecimal("SPORAV"))));
			if (qds.getString("SVRDOK").equalsIgnoreCase("PST")) {
				stanje.setBigDecimal("KOLPS", qds.getBigDecimal("SKOL"));
				stanje.setBigDecimal("NABPS", qds.getBigDecimal("SINAB"));
				stanje.setBigDecimal("MARPS", qds.getBigDecimal("SIMAR"));
				stanje.setBigDecimal("PORPS", qds.getBigDecimal("SIPOR"));
				stanje.setBigDecimal("VPS", qds.getBigDecimal("SIZAD"));
			}
		}
		qds = hr.restart.util.Util
				.getNewQueryDataSet(
						"SELECT max(vrdok) as svrdok,sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
								+ "sum(diopormar) as sdiopormar,"
								+ "sum(dioporpor) as sdioporpor,"
								+ "sum(porav) as sporav,"
								+ "sum(izad) as sizad from stdoku where csklart='"
								+ cskl
								+ "' and god='"
								+ god
								+ "' and cart="
								+ cart + " and vrdok in ('KAL')", true);
		if (qds.getRowCount() > 0) {
			stanje.setBigDecimal("KOLUL", stanje.getBigDecimal("KOLUL").add(
					qds.getBigDecimal("SKOL")));
			stanje.setBigDecimal("NABUL", stanje.getBigDecimal("NABUL").add(
					qds.getBigDecimal("SINAB")));
			stanje.setBigDecimal("MARUL", stanje.getBigDecimal("MARUL").add(
					qds.getBigDecimal("SIMAR").add(
							qds.getBigDecimal("SDIOPORMAR"))));
			stanje.setBigDecimal("PORUL", stanje.getBigDecimal("PORUL").add(
					qds.getBigDecimal("SIPOR").add(
							qds.getBigDecimal("SDIOPORPOR"))));
			stanje.setBigDecimal("VUL", stanje.getBigDecimal("VUL")
					.add(
							qds.getBigDecimal("SIZAD").add(
									qds.getBigDecimal("SPORAV"))));
		}
	}

	public void updateStmeskla(String cskl, String god, int cart,
			DataSet stanje, boolean ulaz) {
		QueryDataSet qds = new QueryDataSet();

		if (ulaz) {
			qds = hr.restart.util.Util
					.getNewQueryDataSet(
							"SELECT sum(kol) as skol,sum(inabul) as sinab,"
									+ "sum(imarul) as simar,"
									+ "sum(iporul) as sipor,"
									+ "sum(diopormar) as sdiopormar,"
									+ "sum(dioporpor) as sdioporpor,"
									+ "sum(porav) as sporav,"
									+ "sum(zadrazul) as sizad from stmeskla where csklul='"
									+ cskl + "' and god='" + god
									+ "' and cart=" + cart
									+ " and vrdok in ('MES','MEU')", true);

			if (qds.getRowCount() > 0) {
				stanje.setBigDecimal("KOLUL", stanje.getBigDecimal("KOLUL")
						.add(qds.getBigDecimal("SKOL")));
				stanje.setBigDecimal("NABUL", stanje.getBigDecimal("NABUL")
						.add(qds.getBigDecimal("SINAB")));
				stanje.setBigDecimal("MARUL", stanje.getBigDecimal("MARUL")
						.add(
								qds.getBigDecimal("SIMAR").add(
										qds.getBigDecimal("SDIOPORMAR"))));
				stanje.setBigDecimal("PORUL", stanje.getBigDecimal("PORUL")
						.add(
								qds.getBigDecimal("SIPOR").add(
										qds.getBigDecimal("SDIOPORPOR"))));
				stanje.setBigDecimal("VUL", stanje.getBigDecimal("VUL").add(
						qds.getBigDecimal("SIZAD").add(
								qds.getBigDecimal("SPORAV"))));
			}
		} else {
			qds = hr.restart.util.Util
					.getNewQueryDataSet(
							"SELECT sum(kol) as skol,sum(inabiz) as sinab,"
									+ "sum(imariz) as simar,"
									+ "sum(iporiz) as sipor,"
									+ "sum(zadraziz) as sizad from stmeskla where cskliz='"
									+ cskl + "' and god='" + god
									+ "' and cart=" + cart
									+ " and vrdok in ('MES','MEI')", true);

			if (qds.getRowCount() > 0) {

				stanje.setBigDecimal("KOLIZ", stanje.getBigDecimal("KOLIZ")
						.add(qds.getBigDecimal("SKOL")));
				stanje.setBigDecimal("NABIZ", stanje.getBigDecimal("NABIZ")
						.add(qds.getBigDecimal("SINAB")));
				stanje.setBigDecimal("MARIZ", stanje.getBigDecimal("MARIZ")
						.add(qds.getBigDecimal("SIMAR")));
				stanje.setBigDecimal("PORIZ", stanje.getBigDecimal("PORIZ")
						.add(qds.getBigDecimal("SIPOR")));
				stanje.setBigDecimal("VIZ", stanje.getBigDecimal("VIZ").add(
						qds.getBigDecimal("SIZAD")));
			}
		}
	}

	public void nullAllStanje(DataSet stanje) {
		stanje.setBigDecimal("KOLPS", Nula);
		stanje.setBigDecimal("NABPS", Nula);
		stanje.setBigDecimal("MARPS", Nula);
		stanje.setBigDecimal("PORPS", Nula);
		stanje.setBigDecimal("VPS", Nula);
		stanje.setBigDecimal("KOLUL", Nula);
		stanje.setBigDecimal("NABUL", Nula);
		stanje.setBigDecimal("MARUL", Nula);
		stanje.setBigDecimal("PORUL", Nula);
		stanje.setBigDecimal("VUL", Nula);
		stanje.setBigDecimal("KOLIZ", Nula);
		stanje.setBigDecimal("NABIZ", Nula);
		stanje.setBigDecimal("MARIZ", Nula);
		stanje.setBigDecimal("PORIZ", Nula);
		stanje.setBigDecimal("VIZ", Nula);
		stanje.setBigDecimal("NC", Nula);
		stanje.setBigDecimal("VC", Nula);
		stanje.setBigDecimal("MC", Nula);
		stanje.setBigDecimal("ZC", Nula);

	}

	private boolean isGoodQds(DataSet stanje, QueryDataSet qds) {
		if (qds.getRowCount() == 0)
			return false;
		if (!stanje.getString("CSKL").equalsIgnoreCase(qds.getString("CSKL")))
			return false;
		if (!stanje.getString("GOD").equalsIgnoreCase(qds.getString("GOD")))
			return false;
		if (stanje.getInt("CART") != qds.getInt("CART"))
			return false;
		return true;
	}

	public void LastofTheLast(DataSet stanje, java.sql.Timestamp timeusporedi) {

		QueryDataSet primke = hr.restart.util.Util
				.getNewQueryDataSet(
						"select doku.datdok,nc,vc,mc,stdoku.cskl,stdoku.vrdok,stdoku.god,stdoku.brdok,stdoku.rbsid from doku,stdoku "
								+ "WHERE doku.cskl = stdoku.cskl "
								+ "AND doku.vrdok = stdoku.vrdok "
								+ "AND doku.god = stdoku.god "
								+ "AND doku.brdok = stdoku.brdok "
								+ "AND stdoku.cskl='"
								+ stanje.getString("CSKL")
								+ "' "
								+ "AND stdoku.god='"
								+ stanje.getString("GOD")
								+ "' "
								+ "AND stdoku.cart="
								+ stanje.getInt("CART")
								+ " "
								+ "order by datdok", true);

		QueryDataSet meskla = hr.restart.util.Util
				.getNewQueryDataSet(
						"select meskla.datdok,nc,vc,mc,"
								+ "stmeskla.csklul,stmeskla.cskliz,stmeskla.vrdok,stmeskla.god,stmeskla.brdok,stmeskla.rbsid "
								+ "from meskla,stmeskla "
								+ "WHERE meskla.csklul = stmeskla.csklul "
								+ "AND meskla.cskliz = stmeskla.cskliz "
								+ "AND meskla.vrdok = stmeskla.vrdok "
								+ "AND meskla.god = stmeskla.god "
								+ "AND meskla.brdok = stmeskla.brdok "
								+ "AND stmeskla.csklul='"
								+ stanje.getString("CSKL") + "' "
								+ "AND stmeskla.god='"
								+ stanje.getString("GOD") + "' "
								+ "AND stmeskla.cart=" + stanje.getInt("CART")
								+ " " + "order by datdok", true);

		primke.last();
		meskla.last();

		//ST.prn(primke);
		//ST.prn(meskla);
		String who = "";

		if (meskla.getRowCount() == 0) {
			who = "P";
		}
		if (primke.getRowCount() == 0) {
			who = "M";
		}
		if (meskla.getRowCount() == 0 && primke.getRowCount() == 0) {
			who = "G";
		}

		//System.out.println("who = "+who);
		if (who.equalsIgnoreCase("")) {
			if (usporedbaDatuma(primke.getTimestamp("DATDOK"), meskla
					.getTimestamp("DATDOK")) < 0) {
				who = "M";
			} else if (usporedbaDatuma(primke.getTimestamp("DATDOK"), meskla
					.getTimestamp("DATDOK")) > 0) {
				who = "P";
			} else {
				// po slobodnoj procjeni uzeti cu da je primka zapravo zadnja
				// kalkulacija
				who = "P";
				/*
				 * System.out.println("Zbunjen sam za ovaj slucaj ->
				 * (cskl-god-cart) -> " + stanje.getString("CSKL")+"-"+
				 * stanje.getString("GOD")+"-"+ stanje.getInt("CART")); if
				 * (meskla.getBigDecimal("MC").compareTo(primke.getBigDecimal("MC"))
				 * !=0 ||
				 * meskla.getBigDecimal("VC").compareTo(primke.getBigDecimal("VC"))
				 * !=0){
				 * 
				 * QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(
				 * "select
				 * doki.datdok,nc,vc,mc,stdoki.cskl,stdoki.vrdok,stdoki.god,stdoki.brdok,stdoki.rbsid
				 * from doki,stdoki "+ "WHERE doki.cskl = stdoki.cskl "+ "AND
				 * doki.vrdok = stdoki.vrdok "+ "AND doki.god = stdoki.god "+
				 * "AND doki.brdok = stdoki.brdok "+ "AND
				 * stdoki.cskl='"+stanje.getString("CSKL")+"' "+ "AND
				 * stdoki.god='"+stanje.getString("GOD")+"' "+ "AND
				 * stdoki.cart="+stanje.getInt("CART")+" "+ "and datdok >'"+"'
				 * order by datdok",true); } else { who="P"; }
				 */
			}
		}
		//System.out.println("who 2 = "+who);

		if (timeusporedi != null) {
			if (who.equalsIgnoreCase("M")
					&& usporedbaDatuma(meskla.getTimestamp("DATDOK"),
							timeusporedi) < 0) {
				who = "D";
			}
			if (who.equalsIgnoreCase("P")
					&& usporedbaDatuma(primke.getTimestamp("DATDOK"),
							timeusporedi) < 0) {
				who = "D";
			}
		}

		//System.out.println("who 3 = "+who);

		if (who.equalsIgnoreCase("M")) {
			stanje.setBigDecimal("VC", meskla.getBigDecimal("VC"));
			stanje.setBigDecimal("MC", meskla.getBigDecimal("MC"));
			stanje.setString("TKAL", rCD.getKey(meskla, new String[] {
					"cskliz", "csklul", "vrdok", "god", "brdok", "rbsid" },
					"stmeskla"));
			stanje.setTimestamp("DATZK", meskla.getTimestamp("DATDOK"));
		} else if (who.equalsIgnoreCase("P")) {
			stanje.setBigDecimal("VC", primke.getBigDecimal("VC"));
			stanje.setBigDecimal("MC", primke.getBigDecimal("MC"));
			stanje.setString("TKAL", rCD.getKey(primke, new String[] { "cskl",
					"vrdok", "god", "brdok", "rbsid" }, "stdoku"));
			stanje.setTimestamp("DATZK", primke.getTimestamp("DATDOK"));
		}
	}

	private int usporedbaDatuma(java.sql.Timestamp prvi,
			java.sql.Timestamp drugi) {
		Calendar cal_prvi = Calendar.getInstance();
		Calendar cal_drugi = Calendar.getInstance();
		cal_prvi.setTime(prvi);
		cal_drugi.setTime(drugi);
		if (cal_prvi.get(Calendar.YEAR) != cal_drugi.get(Calendar.YEAR)) {
			if (cal_prvi.get(Calendar.YEAR) < cal_drugi.get(Calendar.YEAR)) {
				return -1; //
			} else {
				return 1;
			}
		}
		if (cal_prvi.get(Calendar.MONTH) != cal_drugi.get(Calendar.MONTH)) {
			if (cal_prvi.get(Calendar.MONTH) < cal_drugi.get(Calendar.MONTH)) {
				return -1; //
			} else {
				return 1;
			}
		}
		if (cal_prvi.get(Calendar.DATE) != cal_drugi.get(Calendar.DATE)) {
			if (cal_prvi.get(Calendar.DATE) < cal_drugi.get(Calendar.DATE)) {
				return -1; //
			} else {
				return 1;
			}
		}
		return 0;
	}

	public void findLastPrice(DataSet stanje) {
		String tkal = stanje.getString("TKAL");

		if (tkal == null || tkal.equalsIgnoreCase("")) {
			LastofTheLast(stanje, null);
			return;
		}

		HashMap hm = rCD.getHashMapKey(tkal);
		if (hm == null) {
			LastofTheLast(stanje, null);
			return;
		}
		String upit = "";
		QueryDataSet qds = new QueryDataSet();
		if (hm.containsKey("CSKL")) {
			upit = "select cskl,god,cart,nc,vc,mc from stdoku where cskl='"
					+ (String) hm.get("CSKL") + "' and " + "vrdok='"
					+ (String) hm.get("VRDOK") + "' and god='"
					+ (String) hm.get("GOD") + "' and " + "brdok="
					+ (String) hm.get("BRDOK") + " and rbsid="
					+ (String) hm.get("RBRSID");
			System.out.println(upit);
			qds = hr.restart.util.Util.getNewQueryDataSet(upit, true);
		} else {
			upit = "select csklul as cskl,god,cart,nc,vc,mc from stmeskla where csklul='"
					+ (String) hm.get("CSKLUL")
					+ "' and "
					+ "cskliz='"
					+ (String) hm.get("CSKLIZ")
					+ "' and vrdok='"
					+ (String) hm.get("VRDOK")
					+ "' and god='"
					+ (String) hm.get("GOD")
					+ "' and brdok="
					+ (String) hm.get("BRDOK")
					+ " and rbsid="
					+ (String) hm.get("RBRSID");
			qds = hr.restart.util.Util.getNewQueryDataSet(upit, true);
			System.out.println(upit);
		}
		if (isGoodQds(stanje, qds)) {
			//      stanje.setBigDecimal("NC",qds.getBigDecimal("NC"));
			stanje.setBigDecimal("VC", qds.getBigDecimal("VC"));
			stanje.setBigDecimal("MC", qds.getBigDecimal("MC"));
			if (hm.containsKey("CSKL")) {
				qds = hr.restart.util.Util.getNewQueryDataSet(
						"select datdok from doku where cskl='"
								+ (String) hm.get("CSKL") + "' and "
								+ "vrdok='" + (String) hm.get("VRDOK")
								+ "' and god='" + (String) hm.get("GOD")
								+ "' and " + "brdok="
								+ (String) hm.get("BRDOK"), true);
			} else {
				qds = hr.restart.util.Util.getNewQueryDataSet(
						"select datdok from meskla where " + "csklul='"
								+ (String) hm.get("CSKLUL") + "' and "
								+ "cskliz='" + (String) hm.get("CSKLIZ")
								+ "' and " + "vrdok='"
								+ (String) hm.get("VRDOK") + "' and " + "god='"
								+ (String) hm.get("GOD") + "' and " + "brdok="
								+ (String) hm.get("BRDOK"), true);
			}
			if (qds.getRowCount() > 0) {
				stanje.setTimestamp("DATZK", qds.getTimestamp("DATDOK"));
			}
			LastofTheLast(stanje, stanje.getTimestamp("DATZK"));
		} else {
			LastofTheLast(stanje, null);
		}
	}

	public void kalkulateZC(DataSet stanje) {
		String vrzal = "";
		QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(
				"select vrzal from sklad where cskl='"
						+ stanje.getString("CSKL") + "'", true);
		if (qds.getRowCount() == 1) {
			vrzal = qds.getString("VRZAL");
		} else if (qds.getRowCount() > 1) {
			throw new RuntimeException(
					"Postoji više slogova u tabeli skladište za cskl="
							+ stanje.getString("CSKL"));
		} else if (qds.getRowCount() == 0) {
			throw new RuntimeException(
					"Ne postoji slog u tabeli skladište za cskl="
							+ stanje.getString("CSKL"));
		}
		System.out.println("vrzal=" + vrzal);
		System.out.println("KOL =" + stanje.getBigDecimal("KOL"));
		if (vrzal.equalsIgnoreCase("N")) {
			if (stanje.getBigDecimal("KOL").compareTo(Nula) == 0) {
				stanje.setBigDecimal("ZC", Nula);
			} else {
				stanje.setBigDecimal("ZC", stanje.getBigDecimal("VRI").divide(
						stanje.getBigDecimal("KOL"), 4,
						BigDecimal.ROUND_HALF_UP));
			}
		} else if (vrzal.equalsIgnoreCase("V")) {
			stanje.setBigDecimal("ZC", stanje.getBigDecimal("VC"));
		} else if (vrzal.equalsIgnoreCase("M")) {
			stanje.setBigDecimal("ZC", stanje.getBigDecimal("MC"));
		} else {
			throw new RuntimeException("Neispravna vrsta zaliha " + vrzal
					+ " za skladište =" + stanje.getString("CSKL"));
		}

	}

	public void kalkulateNC(DataSet stanje) {
		BigDecimal bd = Aus.zero2;
		if (stanje.getBigDecimal("KOL").doubleValue() != 0) {
			bd = stanje.getBigDecimal("NABUL").subtract(
					stanje.getBigDecimal("NABIZ"));
			bd = bd.divide(stanje.getBigDecimal("KOL"), 2,
					BigDecimal.ROUND_HALF_UP);
			stanje.setBigDecimal("NC", bd);
		}
	}

	public void kalkulateSum(DataSet stanje) {

		stanje.setBigDecimal("KOL", stanje.getBigDecimal("KOLUL").subtract(
				stanje.getBigDecimal("KOLIZ")));
		stanje.setBigDecimal("VRI", stanje.getBigDecimal("VUL").subtract(
				stanje.getBigDecimal("VIZ")));

	}

	public void InitDate() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.clear();
		cal.set(Integer.parseInt(god), cal.JANUARY, 1, 0, 0, 0);
		datDokumenta = cal.getTime();
	}

	public boolean runFirstESC() {
		return true;
	}

	public void firstESC() {
		cancel();
	}

	public void componentShow() {
		rpcskl.Clear();
		poljesnova.setText(hr.restart.util.Valid.getValid().findYear());
		rpcskl.jrfCSKL.requestFocus();
	}

	public boolean isIspis() {
		return false;
	}

	public boolean ispisNow() {
		return false;
	}
}