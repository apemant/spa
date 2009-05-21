/****license*****************************************************************
**   file: upKPRFake.java
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
/*
 * Created on 2005.07.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.robno;

import hr.restart.baza.KPR;
import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class upKPRFake extends raUpit {
	
	private boolean noData=false;
	JRadioButton jrbsklad = new JRadioButton("Skladište");
	JRadioButton jrboj = new JRadioButton("Organizacijska jedinica");
	JlrNavField jrfCORG = new JlrNavField();
	JlrNavField jrfNAZORG = new JlrNavField();
	JraButton jbCORG = new JraButton();
	JlrNavField jrfCSKL = new JlrNavField();
	JlrNavField jrfNAZSKL = new JlrNavField();
	JraButton jbCSKL = new JraButton();

	
	
	BorderLayout borderLayout1 = new BorderLayout();

	dM dm = dM.getDataModule();

	Timestamp date;

	Date dateP = null;

	Date dateZ = null;

	boolean naslovnica;

	QueryDataSet knjiga;

	hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();

	hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

	JLabel jLabel1 = new JLabel();

	JLabel jlRbr = new JLabel();

	JPanel jPanel3 = new JPanel();

	JPanel jp = new JPanel();

	JraTextField jtfPocDatum = new JraTextField();

	JraTextField jtfZavDatum = new JraTextField();

	JraTextField jtfPocRbr = new JraTextField();


	hr.restart.robno._Main main;

	QueryDataSet qdsKPR;

	QueryDataSet qdsDonos;

	QueryDataSet qdsJPTV;

	raCommonClass rcc = raCommonClass.getraCommonClass();

	java.math.BigDecimal initZAD;

	java.math.BigDecimal initRAZ;

	TableDataSet tds = new TableDataSet();

	java.math.BigDecimal tempRAZ = main.nul;

	java.math.BigDecimal tempZAD = main.nul;

	Valid vl;

	lookupData lD = lookupData.getlookupData();

	XYLayout xYLayout1 = new XYLayout();

	static upKPRFake upk;

	boolean struktura = false;

	public static upKPRFake getInstance() {
		if (upk == null) {
			upk = new upKPRFake();
		}
		return upk;
	}

	public upKPRFake() {
		try {
			jbInit();
			upk = this;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void componentShow() {
		showDefaultValues();
//		jrfCORG.setText(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
//		jrfCORG.forceFocLost();
		jrfCSKL.setText(raUser.getInstance().getDefSklad());
		jrfCSKL.forceFocLost();
	}

	void showDefaultValues() {
		jrbsklad.setSelected(true);
		jrboj.setSelected(false);
		jrbsklad_actionPerformed();
		tds.setTimestamp("pocDatum", rut.findFirstDayOfYear(Integer.parseInt(vl
				.findYear())));
		tds.setTimestamp("zavDatum", Valid.getValid().findDate(false, 0));
		tds.setInt("pocRbr", 1);
		jp.setPreferredSize(jp.getPreferredSize());
		this.jtfPocDatum.requestFocus();
		this.getJPTV().setDataSet(null);
	}

	public void okPress() {
		noData = false;
		naslovnica = false;
		makeSet();
	}

	public void firstESC() {
		this.getJPTV().setDataSet(null);
		rcc.EnabDisabAll(this.jPanel3, true);		
		if (jrbsklad.isSelected()) {
			if (jrfCSKL.getText().equalsIgnoreCase("")) {
				cancelPress();
				return;
			} else {
				jrfCSKL.setText("");
				jrfCSKL.forceFocLost();
				jrbsklad_actionPerformed();	
			}
		}
	    if (jrboj.isSelected()) {
				if (jrfCORG.getText().equalsIgnoreCase("")) {
					cancelPress();
					return;
				} else {
					jrfCORG.setText("");
					jrfCORG.forceFocLost();
					jrboj_actionPerformed();
				}
		}
	    return;
	}

	private void makeSet() {

		String corg;
		knjiga = KPR.getDataModule().getFilteredDataSet("1=0");
		knjiga.getColumn("CSKL").setVisible(TriStateProperty.FALSE);
		knjiga.getColumn("GOD").setVisible(TriStateProperty.FALSE);
		knjiga.getColumn("KLJUC").setVisible(TriStateProperty.FALSE);

		String sqlupit;
		
		if (jrbsklad.isSelected()){
			corg = "doki.cskl='" + jrfCSKL.getText() + "' ";
			sqlupit = "select max(doki.cskl),max(doki.vrdok),max(doki.brdok),"
				+ "datdok,sum(stdoki.iprodsp) as iprodsp,"
				+ "MAX(doki.GOD) as god,max(doki.cskl) as cskl "
				+ "from doki,stdoki WHERE "+corg+" and doki.cskl = stdoki.cskl AND "
				+ "doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god "
				+ "AND doki.brdok = stdoki.brdok "
				+ "AND doki.DATDOK between '"
				+ ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum"))
				+ "' and '"
				+ ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))
				+ "' "
				+ "AND doki.vrdok='GOT' group by datdok order by datdok";
			
		} else {
			corg = "doki.cskl='" + jrfCORG.getText() + "' ";
			sqlupit = "select max(doki.cskl),max(doki.vrdok),max(doki.brdok),"
				+ "datdok,sum(stdoki.iprodsp) as iprodsp,"
				+ "MAX(doki.GOD) as god,max(doki.cskl) as cskl "
				+ "from doki,stdoki WHERE "+corg+" and doki.cskl = stdoki.cskl AND "
				+ "doki.vrdok = stdoki.vrdok AND doki.god = stdoki.god "
				+ "AND doki.brdok = stdoki.brdok "
				+ "AND DATDOK between '"
				+ ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum"))
				+ "' and '"
				+ ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))
				+ "' "
				+ "AND doki.vrdok='GRN' group by datdok order by datdok";
			
		}
		knjiga.open();
System.out.println(sqlupit);
		QueryDataSet racuni = ut
				.getNewQueryDataSet(sqlupit);
//System.out.println(racuni.getQuery().getQueryString());
		int i = tds.getInt("pocRbr");
		if (racuni.getRowCount() == 0) {
			setNoDataAndReturnImmediately();
			return;
		}
		knjiga.insertRow(false);
		knjiga.setInt("RBR", i - 1);
		knjiga.setString("GOD", racuni.getString("GOD"));
		knjiga.setString("KLJUC", "");
		knjiga.setString("OPIS", "Donos iz prethodnog perioda");
		knjiga.setBigDecimal("ZAD", Aus.zero2);
		knjiga.setBigDecimal("RAZ", Aus.zero2);

		for (racuni.first(); racuni.inBounds(); racuni.next()) {
			Timestamp dat = ut.getFirstSecondOfDay(racuni
					.getTimestamp("DATDOK"));
			if (lD.raLocate(knjiga, new String[] { "DATUM", "KLJUC" },
					new String[] { dat.toString(), "U" })) {
				knjiga.setBigDecimal("ZAD", knjiga.getBigDecimal("ZAD").add(
						racuni.getBigDecimal("IPRODSP")).setScale(2,
						BigDecimal.ROUND_HALF_UP));
			} else {
				knjiga.insertRow(false);
				knjiga.setString("CSKL", racuni.getString("CSKL"));
				knjiga.setInt("RBR", i);
				knjiga.setString("GOD", racuni.getString("GOD"));
				knjiga.setString("KLJUC", "U");
				knjiga.setString("OPIS", "Zaduženje maloprodaje ");
				knjiga.setTimestamp("DATUM", dat);
				knjiga.setBigDecimal("ZAD", racuni.getBigDecimal("IPRODSP"));
				i++;
			}
			if (lD.raLocate(knjiga, new String[] { "DATUM", "KLJUC" },
					new String[] { dat.toString(), "I" })) {
				knjiga.setBigDecimal("RAZ", knjiga.getBigDecimal("RAZ").add(
						racuni.getBigDecimal("IPRODSP")).setScale(2,
						BigDecimal.ROUND_HALF_UP));
			} else {
				knjiga.insertRow(false);
				knjiga.setString("CSKL", racuni.getString("CSKL"));
				knjiga.setInt("RBR", i);
				knjiga.setString("GOD", racuni.getString("GOD"));
				knjiga.setString("KLJUC", "I");
				knjiga.setString("OPIS", "Razduženje maloprodaje");
				knjiga.setTimestamp("DATUM", dat);
				knjiga.setBigDecimal("RAZ", racuni.getBigDecimal("IPRODSP"));
				i++;
			}
		}
		
		qdsKPR = new QueryDataSet();
		qdsKPR.setColumns(knjiga.cloneColumns());
		qdsKPR.open();
		
		for (knjiga.goToRow(1);knjiga.inBounds();knjiga.next()){
		  qdsKPR.insertRow(false);
		  knjiga.copyTo(qdsKPR);
		}
		
		this.getJPTV().setDataSet(knjiga);
	}

	private void fillJPTV(QueryDataSet ds) {
		this.getJPTV().setDataSetAndSums(ds, new String[] { "ZAD", "RAZ" });
	}

	public boolean runFirstESC() {
		return true;

	}

	private void jbInit() throws Exception {
		this.addReport("hr.restart.robno.repKPR_fake",
				"hr.restart.robno.repKPR_fake", "KPR",
				"Knjiga popisa robe");
		setJPan(jp);
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
		jPanel3.setMinimumSize(new Dimension(604, 160));
		jPanel3.setPreferredSize(new Dimension(655, 160));
		jPanel3.setLayout(xYLayout1);
		jLabel1.setText("Datum (od-do)");
		jlRbr.setText("Po\u010Detni redni broj");
		vl = Valid.getValid();
		jp.setLayout(borderLayout1);
		jp.setMinimumSize(new Dimension(555, 160));
		jp.setPreferredSize(new Dimension(650, 160));
		xYLayout1.setWidth(655);
		xYLayout1.setHeight(160);
		tds.setColumns(new Column[] {
				dM.createTimestampColumn("pocDatum", "Poèetni datum"),
				dM.createTimestampColumn("zavDatum", "Završni dautm"),
				dM.createIntColumn("pocRbr", "Poèetni redni broj"),
				dM.createIntColumn("zavRbr", "Završni redni broj") });
		tds.open();

		jtfZavDatum.setDataSet(tds);
		jtfZavDatum.setColumnName("zavDatum");
		jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

		jtfPocDatum.setDataSet(tds);
		jtfPocDatum.setColumnName("pocDatum");
		jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

		jtfPocRbr.setDataSet(tds);
		jtfPocRbr.setColumnName("pocRbr");

		jrfCSKL.setColumnName("CSKL");
		jrfCSKL.setColNames(new String[] { "NAZSKL" });
		jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZSKL });
		jrfCSKL.setVisCols(new int[] { 0, 1 });
		jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
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

		new raDateRange(jtfPocDatum, jtfZavDatum);
		jPanel3.add(jrbsklad, new XYConstraints(15, 10, -1, -1));
		jPanel3.add(new JLabel("Skladište"), new XYConstraints(15, 35, 100, -1));
		jPanel3.add(jrfCSKL, new XYConstraints(150, 35, 100, -1));
		jPanel3.add(jrfNAZSKL, new XYConstraints(255, 35, 285, -1));
		jPanel3.add(jbCSKL, new XYConstraints(545, 35, 21, 21));
		jPanel3.add(jrboj, new XYConstraints(15, 60, -1, -1));
		jPanel3.add(new JLabel("Org. jedinica"), new XYConstraints(15, 85, 100,-1));
		jPanel3.add(jrfCORG, new XYConstraints(150, 85, 100, -1));
		jPanel3.add(jrfNAZORG, new XYConstraints(255, 85, 285, -1));
		jPanel3.add(jbCORG, new XYConstraints(545, 85, 21, 21));
		jPanel3.add(new JLabel("Datum (od-do)"), new XYConstraints(15, 110, -1,-1));
		jPanel3.add(jtfPocDatum, new XYConstraints(150, 110, 100, -1));
		jPanel3.add(jtfZavDatum, new XYConstraints(255, 110, 100, -1));
		jPanel3.add(jlRbr, new XYConstraints(15, 135, -1, -1));		
		jPanel3.add(jtfPocRbr, new XYConstraints(150, 135, 50, -1));

		jp.add(jPanel3, BorderLayout.CENTER);
	}

	public void jptv_doubleClick() {
//		System.out.println("Not inplementid jet :)");
	}

	public QueryDataSet getRepSet() {
		return qdsKPR;
	}

	public double getDonosZad() {
		return initZAD.doubleValue();
	}

	public double getDonosRaz() {
		return initRAZ.doubleValue();
	}

	public boolean getNaslovnica() {
		return naslovnica;
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
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	    		jrfCSKL.requestFocus();
	        }
	      });
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
	     SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	    		jrfCORG.requestFocus();
	        }
	      });		

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
}
