/****license*****************************************************************
**   file: UpPregledZaduzenjeKPR.java
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
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.sysoutTEST;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class UpPregledZaduzenjeKPR extends hr.restart.util.raUpitLite {

	
	boolean isNoData = false;
	
	sysoutTEST ST = new sysoutTEST(false);
	hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
	lookupData lD = lookupData.getlookupData();
	JRadioButton jrbsklad = new JRadioButton("Skladište");
	JRadioButton jrboj = new JRadioButton("Organizacijska jedinica");
	JlrNavField jrfCORG = new JlrNavField();
	JlrNavField jrfNAZORG = new JlrNavField();
	JraButton jbCORG = new JraButton();
	JlrNavField jrfCSKL = new JlrNavField();
	JlrNavField jrfNAZSKL = new JlrNavField();
	JraButton jbCSKL = new JraButton();
	hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
	hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();

	JPanel jPanel1 = new JPanel();

	XYLayout xYLayout1 = new XYLayout();

	StorageDataSet tds = new StorageDataSet();

	QueryDataSet zaduzbina = new QueryDataSet(); // Tomo, a jel' ti znaš ko je napisa Zadužbinu???
	                                             //	Isaac Asimov (by t.v.)
	JraTextField datumOd = new JraTextField();
	JraTextField datumDo = new JraTextField();

	private void initTDS() {
		tds.setColumns(new Column[] {
				dM.createTimestampColumn("pocDatum", "Poèetni datum"),
				dM.createTimestampColumn("zavDatum", "Završni dautm") });
		tds.open();
	}

	private void initZaduzbina() {
		zaduzbina = new QueryDataSet();
		Column datdok = dm.getDoki().getColumn("DATDOK").cloneColumn();
		Column cart = dm.getStdoki().getColumn("CART").cloneColumn();
		Column cart1 = dm.getStdoki().getColumn("CART1").cloneColumn();
		Column bc = dm.getStdoki().getColumn("BC").cloneColumn();
		Column nazart = dm.getStdoki().getColumn("NAZART").cloneColumn();
		Column jm = dm.getStdoki().getColumn("JM").cloneColumn();
		Column kol = dm.getStdoki().getColumn("KOL").cloneColumn();
		Column iprodsp = dm.getStdoki().getColumn("IPRODSP").cloneColumn();
		Column rbr = dm.getStdoki().getColumn("RBR").cloneColumn();
		zaduzbina.setColumns(new Column[] { datdok, rbr,cart, cart1, bc, jm,nazart, kol,
				iprodsp });
		zaduzbina.open();
	}
	
	static UpPregledZaduzenjeKPR upPrZaKPR = null;
	
	public static UpPregledZaduzenjeKPR getInstance() {
	  if (upPrZaKPR == null) upPrZaKPR = new UpPregledZaduzenjeKPR();
	  return upPrZaKPR;
	}

	public UpPregledZaduzenjeKPR() {
		try {
			initTDS();
			initZaduzbina();
			jbInit();
			upPrZaKPR = this;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean runFirstESC() {
		return true;
	}
	  public void afterReport(){
	    super.afterReport();
	    isNoData = true;
	  }
	
	public void firstESC() {
		
		if (jrbsklad.isSelected()) {
			if (jrfCSKL.getText().equalsIgnoreCase("")) {
				if (!isNoData) {
					cancelPress();	
				}
				isNoData = false;
				return;
			} else {
			     SwingUtilities.invokeLater(new Runnable() {
			        public void run() {
						jrfCSKL.setText("");
						jrfCSKL.forceFocLost();
						jrfCSKL.requestFocus();
			        }
			      });
			}
		}
		if (jrboj.isSelected()) {
			if (jrfCORG.getText().equalsIgnoreCase("")) {
				if (!isNoData) {
					cancelPress();	
				}
				isNoData = false;
				return;
			} else {
			     SwingUtilities.invokeLater(new Runnable() {
			        public void run() {
						jrfCORG.setText("");
						jrfCORG.forceFocLost();
						jrfCORG.requestFocus();
			        }
			      });				
			}
		}
	}
	  public void afterOKPress() {
	  	jrfCSKL.setText("");
	  	jrfCSKL.forceFocLost();
	  	
//		rcc.EnabDisabAllLater(jPanel1,true);
		rcc.EnabDisabAll2(jPanel1,true,rcc.DEFENABMODE);
//	  	rcc.EnabDisabAllLater(okp,true);
		rcc.EnabDisabAll2(okp,true,rcc.DEFENABMODE);
	     SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	jrbsklad.setSelected(true);
	        	jrbsklad_actionPerformed();
	        	jrfCSKL.requestFocus();
	        }
	      });
	     
	  }


	public void okPress() {
		isNoData = false;
	  	makeZaduzbina();
//			ST.prn(zaduzbina);
	}

	public void makeZaduzbina() {

		zaduzbina.emptyAllRows();
		String sql = "";
		if (jrbsklad.isSelected()) {
			sql = "select datdok,cart,kol,iprodsp from doki,stdoki "
					+ "WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok "
					+ "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok "
					+ "and stdoki.vrdok='GOT' and doki.cskl='" + jrfCSKL.getText()
					+ "' AND DATDOK between '"
					+ ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum"))
					+ "' and '"
					+ ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))
					+ "' order by datdok, cart";
		} else {
			sql = "select datdok,cart,kol,iprodsp from doki,stdoki "
					+ "WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok "
					+ "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok "
					+ "and stdoki.vrdok='GRN' and doki.cskl='" + jrfCORG.getText()
					+ "' AND DATDOK between '"
					+ ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum"))
					+ "' and '"
					+ ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))
					+ "' order by datdok, cart";
		}
//		System.out.println(sql);
		QueryDataSet qds = ut.getNewQueryDataSet(sql);
		if (qds.getRowCount() == 0) {
			isNoData = true;
			setNoDataAndReturnImmediately();
			return;
		}
		dm.getAllArtikli().open();
		Timestamp ts = ut.getFirstSecondOfDay(qds.getTimestamp("DATDOK"));

		for (qds.first(); qds.inBounds(); qds.next()) {
			ts = ut.getFirstSecondOfDay(qds.getTimestamp("DATDOK"));
			if (!lD.raLocate(zaduzbina, new String[] { "DATDOK", "CART" },
					new String[] { ts.toString(),
							String.valueOf(qds.getInt("CART")) })) {
				zaduzbina.insertRow(false);
				zaduzbina.setTimestamp("DATDOK", ut.getFirstSecondOfDay(qds
						.getTimestamp("DATDOK")));
				zaduzbina.setInt("CART", qds.getInt("CART"));
				lD.raLocate(dm.getAllArtikli(), "CART", String.valueOf(qds
						.getInt("CART")));
				zaduzbina.setString("CART1", dm.getAllArtikli().getString(
						"CART1"));
				zaduzbina.setString("BC", dm.getAllArtikli().getString("BC"));
				zaduzbina.setString("NAZART", dm.getAllArtikli().getString(
						"NAZART"));
				zaduzbina.setString("JM", dm.getAllArtikli().getString("JM"));
				zaduzbina.setBigDecimal("KOL", Aus.zero2);
				zaduzbina.setBigDecimal("IPRODSP", Aus.zero2);
			}
			zaduzbina.setBigDecimal("KOL", zaduzbina.getBigDecimal("KOL").add(
					qds.getBigDecimal("KOL")).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			zaduzbina.setBigDecimal("IPRODSP", zaduzbina.getBigDecimal(
					"IPRODSP").add(qds.getBigDecimal("IPRODSP")).setScale(2,
					BigDecimal.ROUND_HALF_UP));

		}

		// brisanje onih koji imaju kol i iprodsp==0
		for (zaduzbina.first(); zaduzbina.inBounds(); zaduzbina.next()) {
			if (zaduzbina.getBigDecimal("KOL")
					.compareTo(Aus.zero2) == 0
					&& zaduzbina.getBigDecimal("IPRODSP").compareTo(
							Aus.zero2) == 0) {
				zaduzbina.deleteRow();
			}
		}
		// rekalkulacija rbr
		short rbr = 0;
		zaduzbina.first();
		ts = ut.getFirstSecondOfDay(zaduzbina.getTimestamp("DATDOK"));
		for (zaduzbina.first(); zaduzbina.inBounds(); zaduzbina.next()) {
			if (ts.equals(ut.getFirstSecondOfDay(zaduzbina.getTimestamp("DATDOK")))) {
				rbr++;
			} else {
				rbr = 1;
				ts = ut.getFirstSecondOfDay(zaduzbina.getTimestamp("DATDOK"));
			}
			zaduzbina.setShort("RBR", rbr);
		}

	}

//	public void cancel_press() {
//		this.setVisible(false);
//	}

	private void jbInit() throws Exception {
		
		this.addReport("hr.restart.robno.repZaduzenjeKPR",
			"hr.restart.robno.repZaduzenjeKPR", "ZaduzenjeMaloprodajeKPR",
			"Zaduženje maloprodaje");
		jrbsklad.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jrbsklad_actionPerformed();
			}
		});
		jrboj.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jrboj_actionPerformed();
			}
		});

		xYLayout1.setHeight(140);
		xYLayout1.setWidth(570);
		jPanel1.setLayout(xYLayout1);
		jPanel1.setBorder(BorderFactory.createEtchedBorder());

		datumDo.setColumnName("zavDatum");
		datumDo.setText("");
		datumDo.setDataSet(tds);
		datumDo.setHorizontalAlignment(SwingConstants.CENTER);

		datumOd.setColumnName("pocDatum");
		datumOd.setText("");
		datumOd.setDataSet(tds);
		datumOd.setHorizontalAlignment(SwingConstants.CENTER);

		jrfCSKL.setColumnName("CSKL");
		jrfCSKL.setColNames(new String[] { "NAZSKL" });
		jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZSKL });
		jrfCSKL.setVisCols(new int[] { 0, 1 });
		jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
		//		jrfCSKL.setDataSet(hr.restart.robno.Util.getSkladFromCorg());
		jrfCSKL.setNavButton(jbCSKL);

		jrfNAZSKL.setNavProperties(jrfCSKL);
		jrfNAZSKL.setColumnName("NAZSKL");
		jrfNAZSKL.setSearchMode(1);

		jrfCORG.setColumnName("CORG");
		jrfCORG.setColNames(new String[] { "NAZIV" });
		jrfCORG.setVisCols(new int[] { 0, 1, 2 });
		jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZORG });
		jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
		jrfNAZORG.setColumnName("NAZIV");
		jrfNAZORG.setSearchMode(1);
		jrfNAZORG.setNavProperties(jrfCORG);
		jrfCORG.setNavButton(jbCORG);

		jPanel1.add(jrbsklad, new XYConstraints(15, 10, -1, -1));
		jPanel1.add(new JLabel("Skladište"), new XYConstraints(15, 35, 100, -1));
		jPanel1.add(jrfCSKL, new XYConstraints(150, 35, 100, -1));
		jPanel1.add(jrfNAZSKL, new XYConstraints(255, 35, 285, -1));
		jPanel1.add(jbCSKL, new XYConstraints(545, 35, 21, 21));
		jPanel1.add(jrboj, new XYConstraints(15, 60, -1, -1));
		jPanel1.add(new JLabel("Org. jedinica"), new XYConstraints(15, 85, 100,-1));
		jPanel1.add(jrfCORG, new XYConstraints(150, 85, 100, -1));
		jPanel1.add(jrfNAZORG, new XYConstraints(255, 85, 285, -1));
		jPanel1.add(jbCORG, new XYConstraints(545, 85, 21, 21));
		jPanel1.add(new JLabel("Datum (od-do)"), new XYConstraints(15, 110, -1,-1));
		jPanel1.add(datumOd, new XYConstraints(150, 110, 100, -1));
		jPanel1.add(datumDo, new XYConstraints(255, 110, 100, -1));
		
		setJPan(jPanel1);

//		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
//		this.getContentPane().add(okp, BorderLayout.SOUTH);
	}

	void showDefaultValues() {
		jrbsklad.setSelected(true);
		jrboj.setSelected(false);
		jrbsklad_actionPerformed();
		tds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil()
				.findFirstDayOfYear(Integer.parseInt(vl.findYear())));
		tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(
				false, 0));
		jrfCSKL.setText(raUser.getInstance().getDefSklad());
		jrfCSKL.forceFocLost();
	}

	public void componentShow() {
		showDefaultValues();
	}

	private void jrbsklad_actionPerformed() {
		jrfCORG.setText("");
		jrfCORG.forceFocLost();
		jrboj.setSelected(false);

		rcc.setLabelLaF(jrfCSKL, true);
		rcc.setLabelLaF(jrfNAZSKL, true);
		rcc.setLabelLaF(jbCSKL, true);
		rcc.setLabelLaF(jrfCORG, false);
		rcc.setLabelLaF(jrfNAZORG, false);
		rcc.setLabelLaF(jbCORG, false);
		jrfCSKL.requestFocus();
	}

	private void jrboj_actionPerformed() {
		jrfCSKL.setText("");
		jrfCSKL.forceFocLost();
		jrbsklad.setSelected(false);
		rcc.setLabelLaF(jrfCSKL, false);
		rcc.setLabelLaF(jrfNAZSKL, false);
		rcc.setLabelLaF(jbCSKL, false);
		rcc.setLabelLaF(jrfCORG, true);
		rcc.setLabelLaF(jrfNAZORG, true);
		rcc.setLabelLaF(jbCORG, true);
		jrfCORG.requestFocus();
	}

	public boolean Validacija() {
		if (jrbsklad.isSelected()) {
			if (vl.isEmpty(jrfCSKL)) {
				return false;
			}
		}
		if (jrboj.isSelected()) {
			if (vl.isEmpty(jrfCORG)) {
				return false;
			}
		}
		return true;
	}
	
	public QueryDataSet getReportSet(){
	  return zaduzbina;
	}
	
	
  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }
}
