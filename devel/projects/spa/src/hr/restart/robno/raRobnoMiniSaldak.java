/****license*****************************************************************
**   file: raRobnoMiniSaldak.java
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
import hr.restart.baza.UplRobno;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raRobnoMiniSaldak extends hr.restart.util.raUpitFat {

	private boolean isFirstEscOver = true;
	
	private boolean danasUpl = false;

	private String status = "SVI";

	private String dospjelo = "SVI";

	private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
	private lookupData ld = lookupData.getlookupData();

	//private raSelectTableModifier rSTM;

	private QueryDataSet qdsAllIzlaz = new QueryDataSet();

	private QueryDataSet qdsPojedIzlaz = new QueryDataSet();

	private QueryDataSet qdsAllUlaz = new QueryDataSet();

	private QueryDataSet qdsPojedUlaz = new QueryDataSet();

	private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass
			.getraCommonClass();

	private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

	private JPanel panel_za_upit = new JPanel();

	//private JLabel jlCPAR = new JLabel("Partner");

	private raComboBox rcbPartnerKupac = new raComboBox();

	private JraButton jbCPAR = new JraButton();

	private JlrNavField jrfCPAR = new JlrNavField();

	private JlrNavField jrfNAZPAR = new JlrNavField();

	private boolean isFromAllPartners = false;

	private HashMap hm = new HashMap();

	private JLabel jlSklad = new JLabel("Skladište");

	private JraButton jbsklad = new JraButton();

	private JlrNavField jrfCSKL = new JlrNavField();

	private JlrNavField jrfNAZSKL = new JlrNavField();

	private JLabel jlCorg = new JLabel("Org. jedinica");

	private JraButton jbcorg = new JraButton();

	private JlrNavField jrfCORG = new JlrNavField();

	private JlrNavField jrfNAZORG = new JlrNavField();

	private TableDataSet tds = new TableDataSet();

	private JraTextField jtfPocDatum = new JraTextField();

	private JraTextField jtfZavDatum = new JraTextField();

	private JraComboBox JCstatus = new JraComboBox(new String[] { "Svi",
			"Neplaæeno", "Plaæeno" });

	private JraComboBox JCdosp = new JraComboBox(new String[] { "Svi",
			"Dospjelo", "Nedospjelo" });
	
	JraCheckBox jcbPON = new JraCheckBox();
	JraCheckBox jcbUPL = new JraCheckBox();

	private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(
			false);

/*	raNavAction rnvRAC = new raNavAction("Raèuni", raImages.IMGSTAV,
			java.awt.event.KeyEvent.VK_F6) {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			jptv_doubleClick();
		}
	};*/

	raNavAction rnvUPL = new raNavAction("Uplata", raImages.IMGCHANGE,
			java.awt.event.KeyEvent.VK_F4) {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			azuriranjeNaplate();
		}
	};

	raNavAction rnvPonUPL = new raNavAction("Poništavanje uplate",
			raImages.IMGDELETE, java.awt.event.KeyEvent.VK_F3) {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			ponUPL();
		}
	};

/*	raNavAction rnvPonIsp = new raNavAction("Ispis", raImages.IMGPRINT,
			java.awt.event.KeyEvent.VK_F5) {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			ispis();
		}
	};*/

	/*
	 * raNavAction rnvTogleall = new raNavAction("Okreni
	 * odabir",raImages.IMGALIGNJUSTIFY,java.awt.event.KeyEvent.VK_A,java.awt.event.KeyEvent.CTRL_MASK) {
	 * public void actionPerformed(java.awt.event.ActionEvent e) {
	 * getJPTV().getSelectionTracker() togle_ctrl_A(); } };
	 */

	public boolean isIspis() {
		return false;
	}

	protected boolean createNavBar() {
		return true;
	}
	
	boolean initialized = false;
	
	public void resetDefaults() {
		initialized = true;
		
		tds.open();

		tds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil()
				.findFirstDayOfYear(
						Integer.parseInt(hr.restart.util.Valid.getValid()
								.findYear())));
		tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(
				false, 0));

		jtfPocDatum.setColumnName("pocDatum");
		jtfPocDatum.setDataSet(tds);
		jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

		jtfZavDatum.setDataSet(tds);
		jtfZavDatum.setColumnName("zavDatum");
		jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
		jrfCSKL.setText("");
		jrfCSKL.emptyTextFields();
		jrfCORG.setText("");
		jrfCORG.emptyTextFields();
		JCstatus.setSelectedIndex(1);
		JCdosp.setSelectedIndex(1);
		jcbPON.setSelected(false);
		jcbUPL.setSelected(false);
	}

	public void componentShow() {
		if (!initialized) resetDefaults();

		rcbPartnerKupac.setSelectedIndex(0);
		tds.setString("PK", "K");
		jrfCPAR.setText("");
		jrfCPAR.emptyTextFields();


        this.getJPTV().clearDataSet();
        this.getJPTV().setDataSet(null);
        
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jrfCSKL.requestFocus();
			}
		});
	}

    public boolean runFirstESC() {
      System.out.println("run first escape..."); //XDEBUG delete when no more needed
        return !isFirstEscOver;
    }

	public void firstESC() {
      System.out.println("first escape himself..."); // XDEBUG delete when no
                                                      // more needed

      
		getJPTV().uninstallSelectionTracker();

		mantancereports(isFromAllPartners, tds.getString("PK")); //TODO !!!
		if (isFromAllPartners) {
			isFromAllPartners = false;
			fromHash();
		/*if (tds.getString("PK").equals("K")) {
				this.getJPTV().setDataSetAndSums(qdsAllIzlaz,
						new String[] { "UIRAC", "PLATITI", "SALDO" });
			} else {
				return;
				//        this.getJPTV().setDataSetAndSums(qdsAllUlaz,new String[]
				// {"UIRAC"}); //TODO ovdje hendlati sume...
			}*/
/*			removeNavs();
			addAllPar();
			getJPTV().getColumnsBean().initialize();
			getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);*/
			if (!"".equalsIgnoreCase(selectidColumn)) {
				//System.out.println("selectidColumn "+selectidColumn);
				getJPTV().getColumnsBean().setComboSelectedItem(selectidColumn);
			}
			return;
		}
		/*if (isFirstEscOver) {
			this.cancelPress();
		} else* {*/
//			componentShow();
		rcc.EnabDisabAll(panel_za_upit, true);
		rcc.setLabelLaF(this.okp.jBOK, true);
		//jrfCSKL.requestFocus();
            if (getJPTV().getDataSet() != null){
              
              //this.getJPTV().clearDataSet();
              setDataSet(null);
              removeNav();
              //this.getJPTV().getColumnsBean().initialize();
            } 
            if (!jrfCPAR.getText().equals("")) {
              jrfCPAR.setText("");
              jrfCPAR.emptyTextFields();
              jrfCPAR.requestFocusLater();
            } else if (!jrfCORG.getText().equals("")) {
              jrfCORG.setText("");
              jrfCORG.emptyTextFields();
              jrfCORG.requestFocusLater();
            } else if (!jrfCSKL.getText().equals("")) {
              jrfCSKL.setText("");
              jrfCSKL.emptyTextFields();
              jrfCSKL.requestFocusLater();
            } else jrfCSKL.requestFocusLater();
            if (jrfCSKL.getText().equals("") && jrfCORG.getText().equals("")){
              isFirstEscOver = true;
            } 
	}

	public void cancelPress() {
		//setDataSet(null);
		//removeNav();
		
        /*this.getJPTV().getColumnsBean().initialize();
		getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);*/
		rcc.EnabDisabAll(panel_za_upit, true);
		rcc.setLabelLaF(this.okp.jBOK, true);
		isFirstEscOver = true;
		isFromAllPartners = false;
		super.cancelPress();
	}

	public void setUpQDS() {

		Column cskl = dm.getDoki().getColumn("CSKL").cloneColumn();
		cskl.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
		Column god = dm.getDoki().getColumn("GOD").cloneColumn();
		god.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
		Column brdok = dm.getDoki().getColumn("BRDOK").cloneColumn();
		brdok.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
		Column vrdok = dm.getDoki().getColumn("VRDOK").cloneColumn();
		vrdok.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
        vrdok.setCaption("Dokument");
		Column kljuc = dm.getStanje().getColumn("TKAL").cloneColumn();
		kljuc.setCaption("Dokument");
		kljuc.setColumnName("kljuc");
		kljuc.setWidth(3);
		Column pnbz2 = dm.getDoki().getColumn("PNBZ2").cloneColumn();
		pnbz2.setCaption("Poziv na broj");
		pnbz2.setColumnName("pnbz2");
		pnbz2.setWidth(6);
		Column dvo = dm.getDoki().getColumn("dvo").cloneColumn();
		dvo.setCaption("DVO");
		dvo.setColumnName("dvo");
		dvo.setWidth(4);
		Column datdok = dm.getDoki().getColumn("DATDOK").cloneColumn();
        datdok.setWidth(4);
		Column datdosp = dm.getDoki().getColumn("datdosp").cloneColumn();
		datdosp.setCaption("Dospijeæe");
		datdosp.setColumnName("datdosp");
		datdosp.setWidth(4);
		Column datupl = dm.getDoki().getColumn("datupl").cloneColumn();
		datupl.setCaption("Datum uplate");
		datupl.setColumnName("datupl");
		datupl.setWidth(4);
		Column cpar = dm.getDoki().getColumn("CPAR").cloneColumn();
		cpar.setCaption("Šifra");
		cpar.setColumnName("CPAR");
		cpar.setWidth(1);
		Column nazpar = dm.getPartneri().getColumn("NAZPAR").cloneColumn();
		nazpar.setCaption("Naziv");
		nazpar.setColumnName("NAZPAR");
		nazpar.setWidth(23);
		Column uirac = dm.getDoki().getColumn("UIRAC").cloneColumn();
		uirac.setCaption("Raèun");
		uirac.setColumnName("UIRAC");
		uirac.setWidth(6);
		Column platiti = dm.getDoki().getColumn("PLATITI").cloneColumn();
		platiti.setCaption("Uplata");
		platiti.setColumnName("platiti");
		platiti.setWidth(6);
		Column statpla = dm.getDoki().getColumn("STATPLA").cloneColumn();
		statpla.setCaption("Status");
		statpla.setColumnName("STATPLA");
		statpla.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
		statpla.setWidth(1);
		Column saldo = dm.getStdoki().getColumn("IPRODBP").cloneColumn();
		saldo.setCaption("Saldo");
		saldo.setColumnName("SALDO");
		saldo.setPersist(false);
		saldo.setWidth(6);
		Column danik = dm.getStdoki().getColumn("rbsid").cloneColumn();
		danik.setCaption("Kasni");
		danik.setColumnName("DANIK");
		danik.setPersist(false);
		danik.setWidth(1);
		danik.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
		Column nabrac = dm.createStringColumn("BRRAC", "Broj raèuna", 20);
		nabrac.setWidth(6);

		qdsAllIzlaz.setColumns(new Column[] { cskl, vrdok, god, brdok, kljuc,
				pnbz2, datdok, dvo, datdosp, cpar, nazpar, uirac, platiti, statpla,
				saldo });
		qdsAllUlaz.setColumns(qdsAllIzlaz.cloneColumns());//new
														  // Column[]{cskl,vrdok,god,brdok,kljuc,pnbz2,dvo,datdosp,cpar,nazpar,uirac,platiti,statpla,saldo});
		//    qdsAll.setLocale(Locale.getDefault());
		//    System.out.println("Locale: "+Locale.getDefault());
		qdsAllIzlaz.open();
		qdsAllUlaz.open();
		qdsPojedIzlaz.setColumns(new Column[] { cskl.cloneColumn(),
				vrdok.cloneColumn(), god.cloneColumn(), brdok.cloneColumn(),
				kljuc.cloneColumn(), pnbz2.cloneColumn(), datdok.cloneColumn(), dvo.cloneColumn(),
				datdosp.cloneColumn(), datupl, cpar.cloneColumn(),
				uirac.cloneColumn(), platiti.cloneColumn(),
				statpla.cloneColumn(), saldo.cloneColumn(), danik });

		qdsPojedUlaz
				.setColumns(new Column[] { cskl.cloneColumn(),
                        vrdok.cloneColumn(), nabrac, god.cloneColumn(),
						brdok.cloneColumn(), kljuc.cloneColumn(),
						pnbz2.cloneColumn(), datdok.cloneColumn(), dvo.cloneColumn(),
						datdosp.cloneColumn(), datupl.cloneColumn(),
						cpar.cloneColumn(), uirac.cloneColumn(),
						platiti.cloneColumn(), statpla.cloneColumn(),
						saldo.cloneColumn(), danik.cloneColumn() });
		//qdsPojedUlaz.getColumn("DVO").setCaption("DVO");
		qdsPojedIzlaz.open();
		qdsPojedUlaz.open();
	}

	public void mantancereports(boolean isAll, String kd) {
		this.killAllReports();
		if (tds.getString("PK").equals("K")) {
			if (isAll)
				this.addReport("hr.restart.robno.repRobnoMiniSaldak",
						"hr.restart.robno.repRobnoMiniSaldak",
						"RobnoMiniSaldak", "Ispis saldiranih kartica");
			else {
				this.addReport("hr.restart.robno.repRobnoMiniSaldakCpar",
						"hr.restart.robno.repRobnoMiniSaldak",
						"RobnoMiniSaldakCpar", "Izvadak otvorenih stavaka");
				this.addReport(
						"hr.restart.robno.repRobnoMiniSaldakKarticaCpar",
						"hr.restart.robno.repRobnoMiniSaldak",
						"RobnoMiniSaldakKarticaCpar",
						"Salda konta kupaca - kartica");
			}
		} else {
			if (isAll)
				this
						.addReport("hr.restart.robno.repRobnoMiniSaldak",
								"hr.restart.robno.repRobnoMiniSaldakUlaz",
								"RobnoMiniSaldakUlaz",
								"Ispis saldiranih kartica ulaza");
			else {
				this.addReport(
						"hr.restart.robno.repRobnoMiniSaldakKarticaCparUlaz",
						"hr.restart.robno.repRobnoMiniSaldakUlaz",
						"RobnoMiniSaldakKarticaCparUlaz",
						"Salda konta kupaca - kartica");
			}
		}
	}

	public void okPress() {
		getJPTV().uninstallSelectionTracker();

		mantancereports(jrfCPAR.getText().equalsIgnoreCase(""), tds
				.getString("PK")); //TODO !!!!
		rcc.setLabelLaF(this.okp.jBOK, false);

		isFirstEscOver = false;
		if (tds.getString("PK").equals("K")) {
			//      System.out.println("KUPAC");
			findKarticaIzlaz();
		} else {
			//      System.out.println("DOBAVLJAÈ");
			findKarticaUlaz();
		}
		//    hr.restart.util.sysoutTEST syst = new
		// hr.restart.util.sysoutTEST(false);
		//    syst.prn(tds); // tds ispitivanje

	}

	private static raRobnoMiniSaldak instanceOfMe;

	public raRobnoMiniSaldak() {
      System.out.println("RA ROBNO MINI SALDAK"); //XDEBUG delete when no more needed
		try {
			initializer();
			instanceOfMe = this;
		} catch (Exception sex) {
			sex.printStackTrace();
		}
	}

	public static raRobnoMiniSaldak getInstance() {
		if (instanceOfMe == null)
			instanceOfMe = new raRobnoMiniSaldak();
		return instanceOfMe;
	}

	String vdat = "DVO";
	/**
	 *  
	 */
	private void initializer() throws Exception {
	    vdat = frmParam.getParam("robno", "miniDvo", "N", "Mini saldak dohvat po DVO (D,N)").equalsIgnoreCase("N") ? "DATDOK" : "DVO";
	    
	    danasUpl = frmParam.getParam("robno", "danasUpl", "N", "Staviti današnji dan po defaultu za uplatu (D,N)").equalsIgnoreCase("D");
	  
		keySupport.setNavContainer(getJPTV().getNavBar().getNavContainer());
		setUpQDS();
		tds.setColumns(new Column[] {
				dm.createTimestampColumn("pocDatum", "Po\u010Detni datum"),
				dm.createTimestampColumn("zavDatum", "Završni datum"),
				dm.createBigDecimalColumn("UPLATA", "Uplata", 2),
				dm.createTimestampColumn("DATUPL", "Datum Uplate"),
				dm.createStringColumn("NAP", 50),
				dm.createStringColumn("PK", 1) });

		rcbPartnerKupac.setRaDataSet(tds);
		rcbPartnerKupac.setRaColumn("PK");

		rcbPartnerKupac.setRaItems(new String[][] { { "Kupac", "K" },
				{ "Dobavljaè", "D" }, });

		jrfCPAR.setColumnName("CPAR");
		jrfCPAR.setColNames(new String[] { "NAZPAR" });
		jrfCPAR.setVisCols(new int[] { 0, 1, 2 });
		jrfCPAR
				.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZPAR });
		jrfCPAR.setRaDataSet(dm.getPartneri());
		jrfNAZPAR.setColumnName("NAZPAR");
		jrfNAZPAR.setSearchMode(1);
		jrfNAZPAR.setNavProperties(jrfCPAR);
		jbCPAR.setText("...");
		jrfCPAR.setNavButton(jbCPAR);

		jrfCSKL.setColumnName("CSKL");
		jrfCSKL.setColNames(new String[] { "NAZSKL" });
		jrfCSKL.setVisCols(new int[] { 0, 1, 2 });
		jrfCSKL
				.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZSKL });
		jrfCSKL.setRaDataSet(dm.getSklad());
		jrfNAZSKL.setColumnName("NAZSKL");
		jrfNAZSKL.setSearchMode(1);
		jrfNAZSKL.setNavProperties(jrfCSKL);
		jbsklad.setText("...");
		jrfCSKL.setNavButton(jbsklad);

		jrfCORG.setColumnName("CORG");
		jrfCORG.setColNames(new String[] { "NAZIV" });
		jrfCORG.setVisCols(new int[] { 0, 1, 2 });
		jrfCORG
				.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZORG });
		jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
				.getOrgstrAndCurrKnjig());//dm.getOrgstruktura());
		jrfNAZORG.setColumnName("NAZIV");
		jrfNAZORG.setSearchMode(1);
		jrfNAZORG.setNavProperties(jrfCORG);
		jbcorg.setText("...");
		jrfCORG.setNavButton(jbcorg);
		
		jcbPON.setText("Prikaz ponuda");
		jcbPON.setHorizontalTextPosition(SwingConstants.LEADING);
		jcbPON.setHorizontalAlignment(SwingConstants.TRAILING);
		
		jcbUPL.setText("Prikaz uplata iz izvoda");
		jcbUPL.setHorizontalTextPosition(SwingConstants.LEADING);
		jcbUPL.setHorizontalAlignment(SwingConstants.TRAILING);

		JCstatus.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent i) {
				if (i.getItem().equals("Svi")) {
					status = "SVI";
				} else if (i.getItem().equals("Neplaæeno")) {
					status = "NE";
				} else if (i.getItem().equals("Plaæeno")) {
					status = "DA";
				}
			}
		});

		JCdosp.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent i) {
				if (i.getItem().equals("Svi")) {
					dospjelo = "SVI";
				} else if (i.getItem().equals("Nedospjelo")) {
					dospjelo = "NE";
				} else if (i.getItem().equals("Dospjelo")) {
					dospjelo = "DA";
				}
			}
		});

		XYLayout xyl = new XYLayout();
		xyl.setWidth(700);
		xyl.setHeight(140);
		panel_za_upit.setLayout(xyl);

		panel_za_upit.add(jlSklad, new XYConstraints(15, 17, -1, -1));
		panel_za_upit.add(jrfCSKL, new XYConstraints(150, 15, 100, -1));
		panel_za_upit.add(jrfNAZSKL, new XYConstraints(255, 15, 350, -1));
		panel_za_upit.add(jbsklad, new XYConstraints(610, 15, 21, 21));

		panel_za_upit.add(jlCorg, new XYConstraints(15, 42, -1, -1));
		panel_za_upit.add(jrfCORG, new XYConstraints(150, 40, 100, -1));
		panel_za_upit.add(jrfNAZORG, new XYConstraints(255, 40, 350, -1));
		panel_za_upit.add(jbcorg, new XYConstraints(610, 40, 21, 21));
		
		panel_za_upit.add(jcbPON, new XYConstraints(365, 90, 240, -1));
		//panel_za_upit.add(jcbUPL, new XYConstraints(365, 115, 240, -1));

		panel_za_upit.add(/* jlCPAR */rcbPartnerKupac, new XYConstraints(15, 65,
				130, -1));
		panel_za_upit.add(jrfCPAR, new XYConstraints(150, 65, 100, -1));
		panel_za_upit.add(jrfNAZPAR, new XYConstraints(255, 65, 350, -1));
		panel_za_upit.add(jbCPAR, new XYConstraints(610, 65, 21, 21));

		panel_za_upit.add(new JLabel("Plaæeno / Dospjelost"),
				new XYConstraints(15, 92, -1, -1));
		panel_za_upit.add(JCstatus, new XYConstraints(150, 90, 100, -1));
		panel_za_upit.add(JCdosp, new XYConstraints(255, 90, 100, -1));

		panel_za_upit.add(new JLabel("Datum (od-do)"), new XYConstraints(15,
				117, -1, -1));
		panel_za_upit.add(jtfPocDatum, new XYConstraints(150, 115, 100, -1));
		panel_za_upit.add(jtfZavDatum, new XYConstraints(255, 115, 100, -1));
		this.setJPan(panel_za_upit);
		initMiniPanel();

		hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
				new hr.restart.zapod.raKnjigChangeListener() {
					public void knjigChanged(String oldKnj, String newKnj) {
						jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr
								.getOrgStr().getOrgstrAndCurrKnjig());
					}
				});
		
		installResetButton();
	}

	private void findKarticaIzlaz() { // ovo je bilo i do sada

		String sqlpitanje = "";

		if (jrfCPAR.getText().equalsIgnoreCase("")) {
			sqlpitanje = "select (doki.cskl||'-'||doki.vrdok||'-'||doki.god||'-'||doki.brdok) as kljuc,doki.cskl,doki.vrdok,doki.god,doki.brdok,doki.pnbz2,doki.datdok,doki.dvo,doki.datdosp,"
					+ "doki.cpar,partneri.nazpar,doki.uirac,doki.platiti,doki.datupl from doki, partneri where doki.uirac != 0 and doki.cpar = partneri.cpar and ";
		} else {
			sqlpitanje = "select (doki.cskl||'-'||doki.vrdok||'-'||doki.god||'-'||doki.brdok) as kljuc,doki.cskl,doki.vrdok,doki.god,doki.brdok,doki.pnbz2,doki.datdok,doki.dvo,doki.datdosp,"
					+ "doki.cpar,partneri.nazpar,doki.uirac,doki.platiti,doki.datupl from doki, partneri where doki.uirac != 0 and doki.cpar = "
					+ jrfCPAR.getText() + " and doki.cpar = partneri.cpar and ";
		}
		Condition svd = Condition.in("VRDOK", new String[] {"ROT", "POD"});
        Condition fvd = Condition.in("VRDOK", new String[] {"RAC", "PRD", "TER", "ODB"});
        if (jcbPON.isSelected()) {
          svd = svd.or(Condition.equal("VRDOK", "PON").and(Condition.equal("PARAM", "P")).and(Condition.equal("STATIRA", "N")));
          fvd = fvd.or(Condition.equal("VRDOK", "PON").and(Condition.equal("PARAM", "OJ")).and(Condition.equal("STATIRA", "N")));              
        }

		if (jrfCSKL.getText().equalsIgnoreCase("")
				&& jrfCORG.getText().equalsIgnoreCase("")) {
            sqlpitanje = sqlpitanje + "(" + svd.and(Condition.in("CSKL", Util.getSkladFromCorg())).or(
              fvd.and(Condition.in("CSKL", OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "CORG"))).qualified("doki") + ")";
          /*sqlpitanje = sqlpitanje + "doki.vrdok in ('ROT','POD','RAC','TER','ODB', 'PRD')";*/ 
           
		} else if (!jrfCSKL.getText().equalsIgnoreCase("")
				&& jrfCORG.getText().equalsIgnoreCase("")) {
			sqlpitanje = sqlpitanje + "(" + svd.and(jrfCSKL.getCondition()).qualified("doki") + ")";
/*					+ "doki.vrdok in ('ROT','POD') and doki.cskl='"
					+ jrfCSKL.getText() + "'";*/
		} else if (jrfCSKL.getText().equalsIgnoreCase("")
				&& !jrfCORG.getText().equalsIgnoreCase("")) {
		  sqlpitanje = sqlpitanje + "(" + fvd.and(jrfCORG.getCondition()).qualified("doki") + ")";
/*			sqlpitanje = sqlpitanje
					+ "doki.vrdok in ('RAC','TER','ODB', 'PRD') and doki.cskl='"
					+ jrfCORG.getText() + "'";*/
		} else if (!jrfCSKL.getText().equalsIgnoreCase("")
				&& !jrfCORG.getText().equalsIgnoreCase("")) {
		  sqlpitanje = sqlpitanje + "(" + svd.and(jrfCSKL.getCondition()).
		      or(fvd.and(jrfCORG.getCondition())).qualified("doki") + ")";
/*			sqlpitanje = sqlpitanje
					+ "((doki.vrdok in ('ROT','POD') and doki.cskl='"
					+ jrfCSKL.getText() + "') or ("
					+ "doki.vrdok in ('RAC','TER','ODB','PRD') and doki.cskl='"
					+ jrfCORG.getText() + "'))";*/
		}

		if (status.equalsIgnoreCase("DA")) {
			sqlpitanje = sqlpitanje + " and doki.statpla='D'";
		} else if (status.equalsIgnoreCase("NE")) {
			sqlpitanje = sqlpitanje + " and doki.statpla='N'";
		}

		if (dospjelo.equalsIgnoreCase("DA")) {
			sqlpitanje = sqlpitanje
					+ " and doki.datdosp<='"
					+ hr.restart.util.Util.getUtil().getFirstSecondOfDay(
							Valid.getValid().getToday()) + "'";
		} else if (dospjelo.equalsIgnoreCase("NE")) {
			sqlpitanje = sqlpitanje
					+ " and doki.datdosp>'"
					+ hr.restart.util.Util.getUtil().getFirstSecondOfDay(
							Valid.getValid().getToday()) + "'";
		}

		String collation = "";

		try {
			collation = hr.restart.sisfun.frmParam.getParam("sisfun",
					"CollateSeq");
		} catch (Exception e) {
			e.printStackTrace();
		}

		sqlpitanje = sqlpitanje
				+ " and "
				+ Condition.between("doki."+vdat, tds.getTimestamp("pocDatum"),
						tds.getTimestamp("zavDatum"))
				+ " order by partneri.nazpar " + collation;

		boolean isCPAR4ALL = jrfCPAR.getText().length() == 0;

		if (isCPAR4ALL) {
			qdsAllIzlaz.emptyAllRows();
			qdsAllIzlaz.getColumn("CPAR").setVisible(
					com.borland.jb.util.TriStateProperty.TRUE);
			qdsAllIzlaz.getColumn("PNBZ2").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsAllIzlaz.getColumn("DATDOSP").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsAllIzlaz.getColumn("DVO").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsAllIzlaz.getColumn("DATDOK").setVisible(
                com.borland.jb.util.TriStateProperty.FALSE);
		} else {
			qdsPojedIzlaz.emptyAllRows();
			qdsPojedIzlaz.getColumn("CPAR").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsPojedIzlaz.getColumn("VRDOK").setVisible(
			    com.borland.jb.util.TriStateProperty.TRUE);
			qdsPojedIzlaz.getColumn("PNBZ2").setVisible(
					com.borland.jb.util.TriStateProperty.TRUE);
			qdsPojedIzlaz.getColumn("DATDOSP").setVisible(
					com.borland.jb.util.TriStateProperty.TRUE);
		}

		    System.out.println("sqlPitanje " + sqlpitanje);

		QueryDataSet tmpqds = hr.restart.util.Util.getNewQueryDataSet(
				sqlpitanje, true);

		if (tmpqds == null || tmpqds.rowCount() < 1){
			setNoDataAndReturnImmediately();
        }

		for (tmpqds.first(); tmpqds.inBounds(); tmpqds.next()) {
			if (isCPAR4ALL) {
				if (!hr.restart.util.lookupData.getlookupData().raLocate(
						qdsAllIzlaz, "CPAR",
						String.valueOf(tmpqds.getInt("CPAR")))) {
					qdsAllIzlaz.insertRow(true);
					qdsAllIzlaz.setInt("CPAR", tmpqds.getInt("CPAR"));
					qdsAllIzlaz.setString("NAZPAR", tmpqds.getString("NAZPAR"));
					//          if
					// (hr.restart.util.lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",String.valueOf(tmpqds.getInt("CPAR")))){
					//            qdsAllIzlaz.setString("NAZPAR",dm.getPartneri().getString("NAZPAR"));
					//          }
				}
				qdsAllIzlaz.setBigDecimal("UIRAC", qdsAllIzlaz.getBigDecimal(
						"UIRAC").add(tmpqds.getBigDecimal("UIRAC")));
				qdsAllIzlaz.setBigDecimal("PLATITI", qdsAllIzlaz.getBigDecimal(
						"PLATITI").add(tmpqds.getBigDecimal("PLATITI")));
				qdsAllIzlaz
						.setBigDecimal("SALDO", qdsAllIzlaz.getBigDecimal(
								"UIRAC").subtract(
								qdsAllIzlaz.getBigDecimal("PLATITI")));
			} else {
				qdsPojedIzlaz.insertRow(true);
				dm.copyColumns(tmpqds, qdsPojedIzlaz, new String[] { "cskl",
						"vrdok", "god", "brdok", "pnbz2", "dvo", "datdok", "uirac",
						"platiti", "datdosp", "kljuc", "cpar", "datupl" });
				qdsPojedIzlaz.setBigDecimal("SALDO", qdsPojedIzlaz
						.getBigDecimal("UIRAC").subtract(
								qdsPojedIzlaz.getBigDecimal("PLATITI")));
				if (qdsPojedIzlaz.getBigDecimal("SALDO").doubleValue() != 0) {
					qdsPojedIzlaz
							.setInt(
									"DANIK",
									raDateUtil
											.getraDateUtil()
											.DateDifference(
													ut
															.getFirstSecondOfDay(tmpqds
																	.getTimestamp("datdosp")),
													ut
															.getFirstSecondOfDay(hr.restart.util.Valid
																	.getValid()
																	.findDate(
																			false,
																			0))));
				} else {
					qdsPojedIzlaz.setInt("DANIK", raDateUtil.getraDateUtil()
							.DateDifference(
									ut.getFirstSecondOfDay(tmpqds
											.getTimestamp("datdosp")),
									ut.getFirstSecondOfDay(tmpqds
											.getTimestamp("datupl"))));
				}
			}
		}
		//removeNavs();
		if (isCPAR4ALL) {
			//      qdsAllIzlaz.setSort(new SortDescriptor(new String[] {"NAZPAR"}));
            qdsAllIzlaz.setTableName("izlazall");
			qdsAllIzlaz.first();
			this.setDataSetAndSums(qdsAllIzlaz,
					new String[] { "UIRAC", "PLATITI", "SALDO" });
			//addAllPar();
		} else {
            qdsPojedIzlaz.setTableName("izlazsingle");
			qdsPojedIzlaz.first();
			qdsPojedIzlaz.setSort(new SortDescriptor(new String[] {vdat}));
			this.setDataSetAndSums(qdsPojedIzlaz,
					new String[] { "UIRAC", "PLATITI", "SALDO" });
			//addPojed();
		}

		//getJPTV().getColumnsBean().initialize();
		//getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);
		if (isCPAR4ALL) {
			getJPTV().installSelectionTracker(new String[] { "CPAR" });
		}
	}

	private void findKarticaUlaz() { // ovo je novo.... gledati prk i kal

		boolean isCPAR4ALL = jrfCPAR.getText().equalsIgnoreCase("");
//group by cskl, vrdok, brdok, god order by nazpar
		String sqlpitanje = "select doku.cskl as cskl, doku.vrdok as vrdok, doku.god as god, "
				+ "doku.brdok as brdok, max(doku.brrac) as brrac, max(doku.cpar) as cpar, max(partneri.nazpar) as nazpar, max(doku.cskl||'-'||doku.vrdok||'-'||doku.god||'-'||doku.brdok) as kljuc, "
				+ "max(doku.pnbz2) as pnbz2, max(doku.datdok) as datdok, max(doku.dvo) as dvo, max(doku.datdosp) as datdosp, max(doku.datupl) as datupl, max(doku.cpar) as cpar, max(doku.statpla) as statpla, "
				+ "max(doku.platiti) as platiti, max(doku.uiprpor) as uiprpor, max(doku.uikal) as uikal "
				+ "from doku, partneri where doku.cpar = partneri.cpar ";

		if (jrfCSKL.getText().equalsIgnoreCase("")
				&& jrfCORG.getText().equalsIgnoreCase("")) {
			sqlpitanje += "and doku.vrdok in ('PRK','KAL','PTE') ";
		} else if (!jrfCSKL.getText().equalsIgnoreCase("")
				&& jrfCORG.getText().equalsIgnoreCase("")) {
			sqlpitanje += "and doku.vrdok in ('PRK','PTE') AND doku.cskl = '"
					+ jrfCSKL.getText() + "' ";
		} else if (jrfCSKL.getText().equalsIgnoreCase("")
				&& !jrfCORG.getText().equalsIgnoreCase("")) {
			sqlpitanje += "and doku.vrdok = 'KAL' AND doku.cskl = '"
					+ jrfCORG.getText() + "' ";
		} else if (!jrfCSKL.getText().equalsIgnoreCase("")
				&& !jrfCORG.getText().equalsIgnoreCase("")) {
			sqlpitanje += "and ((doku.vrdok in ('PRK','PTE') and doku.cskl='"
					+ jrfCSKL.getText()
					+ "') OR (doku.vrdok = 'KAL' and doku.cskl='"
					+ jrfCORG.getText() + "')) ";
		}

		if (!isCPAR4ALL)
			sqlpitanje += " and doku.cpar = " + jrfCPAR.getText() + " ";

		if (status.equalsIgnoreCase("DA")) {
			sqlpitanje += " and doku.statpla='D'";
		} else if (status.equalsIgnoreCase("NE")) {
			sqlpitanje += " and doku.statpla='N'";
		}

		if (dospjelo.equalsIgnoreCase("DA")) {
			sqlpitanje += " and datdosp<='"
					+ hr.restart.util.Util.getUtil().getFirstSecondOfDay(
							Valid.getValid().getToday()) + "'";
		} else if (dospjelo.equalsIgnoreCase("NE")) {
			sqlpitanje += " and datdosp>'"
					+ hr.restart.util.Util.getUtil().getFirstSecondOfDay(
							Valid.getValid().getToday()) + "'";
		}

		sqlpitanje += " and "
				+ Condition.between(vdat, tds.getTimestamp("pocDatum"), tds
						.getTimestamp("zavDatum"));

		String collation = "";
		try {
			collation = hr.restart.sisfun.frmParam.getParam("sisfun",
					"CollateSeq");
		} catch (Exception e) {
			e.printStackTrace();
		}

		sqlpitanje += " group by cskl, vrdok, brdok, god order by nazpar "
				+ collation;

		if (isCPAR4ALL) {
			qdsAllUlaz.emptyAllRows();
			qdsAllUlaz.getColumn("CPAR").setVisible(
					com.borland.jb.util.TriStateProperty.TRUE);
			qdsAllUlaz.getColumn("PNBZ2").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsAllUlaz.getColumn("DATDOSP").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsAllUlaz.getColumn("DVO").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsAllUlaz.getColumn("DATDOK").setVisible(
                com.borland.jb.util.TriStateProperty.FALSE);
		} else {
			qdsPojedUlaz.emptyAllRows();
			qdsPojedUlaz.getColumn("CPAR").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsPojedUlaz.getColumn("VRDOK").setVisible(
			    com.borland.jb.util.TriStateProperty.TRUE);
			qdsPojedUlaz.getColumn("PNBZ2").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsPojedUlaz.getColumn("DATDOSP").setVisible(
					com.borland.jb.util.TriStateProperty.FALSE);
			qdsPojedUlaz.getColumn("DATDOSP").setVisible(
					com.borland.jb.util.TriStateProperty.TRUE);
			qdsPojedUlaz.getColumn("DVO").setVisible(
					com.borland.jb.util.TriStateProperty.TRUE);
			qdsPojedUlaz.getColumn("DATDOK").setVisible(
                com.borland.jb.util.TriStateProperty.TRUE);
			qdsPojedUlaz.getColumn("PLATITI").setVisible(
					com.borland.jb.util.TriStateProperty.TRUE);
			qdsPojedUlaz.getColumn("SALDO").setVisible(
					com.borland.jb.util.TriStateProperty.TRUE);
		}

		//debug
		    System.out.println("sqlPitanje " + sqlpitanje);
		//    sysoutTEST s = new sysoutTEST(false);

		QueryDataSet tmpqds = hr.restart.util.Util.getNewQueryDataSet(
				sqlpitanje, true);

		if (tmpqds == null || tmpqds.rowCount() < 1)
			setNoDataAndReturnImmediately();

		//debug
		//    s.prn(tmpqds);

		for (tmpqds.first(); tmpqds.inBounds(); tmpqds.next()) {
			if (isCPAR4ALL) {
				if (!hr.restart.util.lookupData.getlookupData().raLocate(
						qdsAllUlaz, "CPAR",
						String.valueOf(tmpqds.getInt("CPAR")))) {
					qdsAllUlaz.insertRow(true);
					qdsAllUlaz.setInt("CPAR", tmpqds.getInt("CPAR"));
					qdsAllUlaz.setString("NAZPAR", tmpqds.getString("NAZPAR"));
					//          if
					// (hr.restart.util.lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",String.valueOf(tmpqds.getInt("CPAR")))){
					//            qdsAllUlaz.setString("NAZPAR",dm.getPartneri().getString("NAZPAR"));
					//          }
				}
				qdsAllUlaz.setBigDecimal("UIRAC", qdsAllUlaz.getBigDecimal(
						"UIRAC").add(
						tmpqds.getBigDecimal("UIPRPOR").add(
								tmpqds.getBigDecimal("UIKAL"))));
				qdsAllUlaz.setBigDecimal("PLATITI", qdsAllUlaz.getBigDecimal(
						"PLATITI").add(tmpqds.getBigDecimal("PLATITI")));
				qdsAllUlaz.setBigDecimal("SALDO", qdsAllUlaz.getBigDecimal(
						"UIRAC").subtract(qdsAllUlaz.getBigDecimal("PLATITI")));
			} else {
				qdsPojedUlaz.insertRow(true);
                dm.copyColumns(tmpqds, qdsPojedUlaz, new String[] 
                    {"brrac", "cskl", "vrdok", "god", "brdok", "kljuc", "cpar", 
                     "platiti", "statpla", "datdok", "dvo", "datdosp", "datupl"});
				qdsPojedUlaz.setBigDecimal("UIRAC", tmpqds.getBigDecimal(
						"UIPRPOR").add(tmpqds.getBigDecimal("UIKAL")));

				qdsPojedUlaz.setBigDecimal("SALDO", qdsPojedUlaz.getBigDecimal(
						"UIRAC")
						.subtract(qdsPojedUlaz.getBigDecimal("PLATITI")));
				if (qdsPojedUlaz.getBigDecimal("SALDO").doubleValue() != 0) {
					qdsPojedUlaz
							.setInt(
									"DANIK",
									raDateUtil
											.getraDateUtil()
											.DateDifference(
													ut
															.getFirstSecondOfDay(tmpqds
																	.getTimestamp("datdosp")),
													ut
															.getFirstSecondOfDay(hr.restart.util.Valid
																	.getValid()
																	.findDate(
																			false,
																			0))));
				} else {
					qdsPojedUlaz.setInt("DANIK", raDateUtil.getraDateUtil()
							.DateDifference(ut.getFirstSecondOfDay(tmpqds
											.getTimestamp("datdosp")),
									ut.getFirstSecondOfDay(tmpqds
											.getTimestamp("datupl"))));
				}
			}
		}
		//removeNavs();
		//debug
		//    s.prn(qdsPojedUlaz);

		if (isCPAR4ALL) {
			//      qdsAllUlaz.setSort(new SortDescriptor(new String[] {"NAZPAR"}));
            qdsAllUlaz.setTableName("ulazall");
			qdsAllUlaz.first();
			setDataSetAndSums(qdsAllUlaz, 	new String[] { "UIRAC", "PLATITI", "SALDO" });
			//addAllPar();
		} else {
            qdsPojedUlaz.setTableName("ulazsingle");
			qdsPojedUlaz.first();
			qdsPojedUlaz.setSort(new SortDescriptor(new String[] {vdat}));			
			setDataSetAndSums(qdsPojedUlaz, new String[] { "UIRAC", "PLATITI", "SALDO" });
			//addPojed();
		}

		/*getJPTV().getColumnsBean().initialize();
		getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);*/
		if (isCPAR4ALL) {
			getJPTV().installSelectionTracker(new String[] { "CPAR" });
		}
	}

	/*private void removeNavs() {
		getJPTV().getNavBar().removeOption(rnvUPL);
		getJPTV().getNavBar().removeOption(rnvPonUPL);
		getJPTV().getNavBar().removeOption(rnvRAC);
		getJPTV().getNavBar().removeOption(rnvPonIsp);
		//getJPTV().getNavBar().removeOption(rnvTogleall);
		getJPTV().getNavBar().unregisterNavBarKeys(this);
	}*/
    
    protected void addNavBarOptions(){ 
      super.addNavBarOptions();
        if (jrfCPAR.getText().length() == 0) {
          if (getJPTV().getNavBar().contains(rnvUPL))
            getJPTV().getNavBar().removeOption(rnvUPL);
          if (getJPTV().getNavBar().contains(rnvPonUPL))
            getJPTV().getNavBar().removeOption(rnvPonUPL);
        } else {
          if (!getJPTV().getNavBar().contains(rnvUPL))
            getJPTV().getNavBar().addOption(rnvUPL, 0);
          if (!getJPTV().getNavBar().contains(rnvPonUPL))
            getJPTV().getNavBar().addOption(rnvPonUPL, 1);
        }
      }

      public void navbarremoval(){
        super.navbarremoval();
        if (getJPTV().getNavBar().contains(rnvUPL))
          this.getJPTV().getNavBar().removeOption(rnvUPL);
        if (getJPTV().getNavBar().contains(rnvPonUPL))
          this.getJPTV().getNavBar().removeOption(rnvPonUPL);
      }

	/*private void addAllPar() {
		getJPTV().getNavBar().addOption(rnvRAC, 0);
		getJPTV().getNavBar().addOption(rnvPonIsp, 1);
		//getJPTV().getNavBar().addOption(rnvTogleall,2);
		getJPTV().getNavBar().registerNavBarKeys(this);
	}

	private void addPojed() {
		getJPTV().getNavBar().addOption(rnvUPL, 0);
		getJPTV().getNavBar().addOption(rnvPonUPL, 1);
		getJPTV().getNavBar().addOption(rnvRAC, 2);
		getJPTV().getNavBar().addOption(rnvPonIsp, 3);
		getJPTV().getNavBar().registerNavBarKeys(this);
	}*/

	private JPanel miniPanel = new JPanel();

	private JraDialog miniFrame;

	private JraTextField racun = new JraTextField();

	private JraTextField platiti = new JraTextField();

	private JraTextField uplata = new JraTextField();

	private JraTextField datupl = new JraTextField();
	
	private JraTextField opis = new JraTextField();
	
	JraButton delUpl = new JraButton();
	JraButton addUpl = new JraButton();
	
	JraTable2 jup = new JraTable2();
	
	QueryDataSet upls;

	//mini panel za upis uplate!!!
	private OKpanel miniOK = new OKpanel() {
		public void jPrekid_actionPerformed() {
			cancelOption();
		}

		public void jBOK_actionPerformed() {
			saveUplatu();
		}
	};

	private void cancelOption() {
		getJPTV().uninstallSelectionTracker();

		miniFrame.dispose();
	}

	private void saveUplatu() {

		if (tds.getString("PK").equals("K")) {
			if (tds.getBigDecimal("UPLATA").signum() != 0) {
				addUpl(tds.getBigDecimal("UPLATA"), tds.getTimestamp("DATUPL"), tds.getString("NAP"));
			}
			qdsPojedIzlaz.setBigDecimal("PLATITI", Aus.sum("IZNOS", upls));
			/*if (qdsPojedIzlaz.getBigDecimal("UIRAC").compareTo(
					qdsPojedIzlaz.getBigDecimal("PLATITI")) == -1) {
				qdsPojedIzlaz.cancel();
				JOptionPane.showConfirmDialog(this,
						"Uplata ne moze biti veæa od iznosa raèuna !!!!",
						"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						uplata.requestFocus();
					}
				});
				return;
			}*/

			if (qdsPojedIzlaz.getBigDecimal("UIRAC").compareTo(
					qdsPojedIzlaz.getBigDecimal("PLATITI")) == 0) {
				qdsPojedIzlaz.setString("STATPLA", "D");
			} else {
				qdsPojedIzlaz.setString("STATPLA", "N");
			}

			qdsPojedIzlaz.setBigDecimal("SALDO", qdsPojedIzlaz.getBigDecimal(
					"UIRAC").subtract(qdsPojedIzlaz.getBigDecimal("PLATITI")));
			upls.last();
			qdsPojedIzlaz.setTimestamp("datupl", new Timestamp(upls.getTimestamp("DATUM").getTime()));
			if (qdsPojedIzlaz.getBigDecimal("SALDO").doubleValue() == 0) {
				qdsPojedIzlaz.setInt("DANIK", raDateUtil.getraDateUtil()
						.DateDifference(
								ut.getFirstSecondOfDay(qdsPojedIzlaz
										.getTimestamp("datdosp")),
								ut.getFirstSecondOfDay(qdsPojedIzlaz
										.getTimestamp("datupl"))));
			} else {
				qdsPojedIzlaz.setInt("DANIK", raDateUtil.getraDateUtil()
						.DateDifference(
								ut.getFirstSecondOfDay(qdsPojedIzlaz
										.getTimestamp("datdosp")),
								ut.getFirstSecondOfDay(hr.restart.util.Valid
										.getValid().findDate(false, 0))));
			}

			String mysqlupit = "select * from doki where cskl='"
					+ qdsPojedIzlaz.getString("cskl") + "' and vrdok='"
					+ qdsPojedIzlaz.getString("vrdok") + "' and god='"
					+ qdsPojedIzlaz.getString("god") + "' and brdok="
					+ qdsPojedIzlaz.getInt("BRDOK");
			QueryDataSet myqds = hr.restart.util.Util.getNewQueryDataSet(
					mysqlupit, true);
			myqds.setBigDecimal("PLATITI", qdsPojedIzlaz
					.getBigDecimal("PLATITI"));
			myqds.setString("STATPLA", qdsPojedIzlaz.getString("STATPLA"));
			myqds.setTimestamp("datupl", new Timestamp(upls.getTimestamp("DATUM").getTime()));
			if (qdsAllIzlaz != null && qdsAllIzlaz.getRowCount() != 0) {
				qdsAllIzlaz.setBigDecimal("PLATITI", qdsAllIzlaz.getBigDecimal(
						"PLATITI").add(tds.getBigDecimal("UPLATA")));
				qdsAllIzlaz.setBigDecimal("SALDO", qdsAllIzlaz.getBigDecimal(
						"UIRAC").subtract(qdsAllIzlaz.getBigDecimal("PLATITI")));
			}

			raTransaction.saveChanges(myqds);
			raTransaction.saveChanges(upls);

			qdsPojedIzlaz.setInt("DANIK", raDateUtil.getraDateUtil()
					.DateDifference(ut.getFirstSecondOfDay(qdsPojedIzlaz
									.getTimestamp("datdosp")),
							ut.getFirstSecondOfDay(qdsPojedIzlaz
									.getTimestamp("datupl"))));
			getJPTV().fireTableDataChanged();
			miniFrame.dispose();
		} else {
			if (tds.getBigDecimal("UPLATA").signum() != 0) {
				addUpl(tds.getBigDecimal("UPLATA"), tds.getTimestamp("DATUPL"), tds.getString("NAP"));
			}
			qdsPojedUlaz.setBigDecimal("PLATITI", Aus.sum("IZNOS", upls));
			/*if (qdsPojedUlaz.getBigDecimal("UIRAC").compareTo(
					qdsPojedUlaz.getBigDecimal("PLATITI")) == -1) {
				qdsPojedUlaz.cancel();
				JOptionPane.showConfirmDialog(this,
						"Uplata ne moze biti veæa od iznosa raèuna !!!!",
						"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						uplata.requestFocus();
					}
				});
				return;
			}*/

			if (qdsPojedUlaz.getBigDecimal("UIRAC").compareTo(
					qdsPojedUlaz.getBigDecimal("PLATITI")) == 0) {
				qdsPojedUlaz.setString("STATPLA", "D");
			} else {
				qdsPojedUlaz.setString("STATPLA", "N");
			}

			qdsPojedUlaz.setBigDecimal("SALDO", qdsPojedUlaz.getBigDecimal(
					"UIRAC").subtract(qdsPojedUlaz.getBigDecimal("PLATITI")));
			upls.last();
			qdsPojedUlaz.setTimestamp("datupl", new Timestamp(upls.getTimestamp("DATUM").getTime()));
			if (qdsPojedUlaz.getBigDecimal("SALDO").doubleValue() == 0) {
				qdsPojedUlaz.setInt("DANIK", raDateUtil.getraDateUtil()
						.DateDifference(ut.getFirstSecondOfDay(qdsPojedUlaz
										.getTimestamp("datdosp")),
								ut.getFirstSecondOfDay(qdsPojedUlaz
										.getTimestamp("datupl"))));
			} else {
				qdsPojedUlaz.setInt("DANIK", raDateUtil.getraDateUtil()
						.DateDifference(ut.getFirstSecondOfDay(qdsPojedUlaz
										.getTimestamp("datdosp")),
								ut.getFirstSecondOfDay(hr.restart.util.Valid
										.getValid().findDate(false, 0))));
			}

			String mysqlupit = "select * from doku where cskl='"
					+ qdsPojedUlaz.getString("cskl") + "' and vrdok='"
					+ qdsPojedUlaz.getString("vrdok") + "' and god='"
					+ qdsPojedUlaz.getString("god") + "' and brdok="
					+ qdsPojedUlaz.getInt("BRDOK");
			QueryDataSet myqds = hr.restart.util.Util.getNewQueryDataSet(
					mysqlupit, true);
			myqds.setBigDecimal("PLATITI", qdsPojedUlaz
					.getBigDecimal("PLATITI"));
			myqds.setString("STATPLA", qdsPojedUlaz.getString("STATPLA"));
			myqds.setTimestamp("datupl", new Timestamp(upls.getTimestamp("DATUM").getTime()));

			if (qdsAllUlaz != null && qdsAllUlaz.getRowCount() != 0) {
                qdsAllUlaz.setBigDecimal("PLATITI", qdsAllUlaz.getBigDecimal(
						"PLATITI").add(tds.getBigDecimal("UPLATA")));
                qdsAllUlaz.setBigDecimal("SALDO", qdsAllUlaz.getBigDecimal(
						"UIRAC").subtract(qdsAllUlaz.getBigDecimal("PLATITI")));
			}

			raTransaction.saveChanges(myqds);
			raTransaction.saveChanges(upls);

            qdsPojedUlaz.setInt("DANIK", raDateUtil.getraDateUtil()
					.DateDifference(
							ut.getFirstSecondOfDay(qdsPojedUlaz
									.getTimestamp("datdosp")),
							ut.getFirstSecondOfDay(qdsPojedUlaz
									.getTimestamp("datupl"))));
			getJPTV().fireTableDataChanged();
			miniFrame.dispose();
		}
	}

	private void initMiniPanel() {

		/*miniFrame.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == e.VK_ESCAPE) {
					e.consume();
					cancelOption();
				} else if (e.getKeyCode() == e.VK_F10) {
					e.consume();
					saveUplatu();
				}
			}
		});*/
		
		JPanel left = new JPanel(new XYLayout(310,150));
		
		left.add(new JLabel("Iznos raèuna"), new XYConstraints(15, 15, -1, -1));
		left.add(racun, new XYConstraints(200, 15, 100, -1));
		left.add(new JLabel("Prijašnje uplate"), new XYConstraints(15, 40, -1, -1));
		left.add(platiti, new XYConstraints(200, 40, 100, -1));
		left.add(new JLabel("Uplata"), new XYConstraints(15, 65, -1, -1));
		left.add(uplata, new XYConstraints(200, 65, 100, -1));
		left.add(new JLabel("Datum uplate"), new XYConstraints(15, 90, -1, -1));
		left.add(datupl, new XYConstraints(200, 90, 100, -1));
		left.add(opis, new XYConstraints(15, 115, 285, -1));
		left.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel but = new JPanel(new GridLayout(1, 0));
		delUpl.setText("Obriši uplatu");
		addUpl.setText("Dodaj uplatu");
		but.add(addUpl);
		but.add(delUpl);
		addUpl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addUpl(tds.getBigDecimal("UPLATA"), tds.getTimestamp("DATUPL"), tds.getString("NAP"));
				jup.fireTableDataChanged();
				tds.setBigDecimal("UPLATA", Aus.zero2);
				uplata.selectAll();
			}
		});
		delUpl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (upls.rowCount() > 0) {
					upls.deleteRow();
					jup.fireTableDataChanged();
					tds.setBigDecimal("UPLATA", Aus.zero2);
					uplata.selectAll();
				}
			}
		});
		
		JPanel up = new JPanel(new BorderLayout());
		up.add(left, BorderLayout.NORTH);
		up.add(but);
		
		miniPanel.setLayout(new BorderLayout());
		miniPanel.add(up, BorderLayout.NORTH);
		miniPanel.add(new JraScrollPane(jup, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		jup.setPreferredScrollableViewportSize(new Dimension(260, 200));

	}

	//  public void jptv_doubleClickA() {
	//    hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
	//    rut.showDocs(qdsPojedIzlaz.getString("CSKL"),qdsPojedIzlaz.getString("VRDOK"),qdsPojedIzlaz.getInt("BRDOK"),qdsPojedIzlaz.getString("GOD"));
	//  }

	String selectidColumn = "";
	String dockey;

	public void jptv_doubleClick() {
		getJPTV().uninstallSelectionTracker();

		if (tds.getString("PK").equals("K")) {
			if (jrfCPAR.getText().equalsIgnoreCase("")) {
                 getJPTV().getColumnsBean().saveSettings();
				toHash();
				this.isFromAllPartners = true;
				jrfCPAR.setText(String.valueOf(qdsAllIzlaz.getInt("CPAR")));
				jrfCPAR.forceFocLost();

				selectidColumn = getJPTV().getColumnsBean()
						.getComboSelectedItem();
				// ovdje zapamtiti koji je selektiran
				this.getJPTV().setDataSet(null);
				okPress();
			} else {
				hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
				rut.showDocs(qdsPojedIzlaz.getString("CSKL"), "",
						qdsPojedIzlaz.getString("VRDOK"), qdsPojedIzlaz.getInt("BRDOK"),
						qdsPojedIzlaz.getString("GOD"));
				//        jptv_doubleClickA();
			}
		} else {
			//      System.out.println("Primke - kalkulacije");
			if (jrfCPAR.getText().equalsIgnoreCase("")) {
				toHash();
				this.isFromAllPartners = true;
				jrfCPAR.setText(String.valueOf(qdsAllUlaz.getInt("CPAR")));
				jrfCPAR.forceFocLost();

				selectidColumn = getJPTV().getColumnsBean()
						.getComboSelectedItem();
				// ovdje zapamtiti koji je selektiran
				this.getJPTV().setDataSet(null);
				ok_action();
			} else {
				hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
				rut.showDocs(qdsPojedUlaz.getString("CSKL"), "", 
						qdsPojedUlaz.getString("VRDOK"), qdsPojedUlaz.getInt("BRDOK"),
						qdsPojedUlaz.getString("GOD"));
			}
			//TODO ovde hendlat ulaze (primke kalkulacije i kalkulacije)
		}
	}

	public void ponUPL() {
		if (!isFromAllPartners)
			return;

		if (tds.getString("PK").equals("K")) {
		  if ("PON".equals(qdsPojedIzlaz.getString("VRDOK"))) {
            JOptionPane.showMessageDialog(this.getWindow(), "Ponude se ne mogu ažurirati.\n" +
                  "Potrebno ih je prebaciti u raèune ili poništiti.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
          }
			if (qdsPojedIzlaz.getBigDecimal("PLATITI").doubleValue() == 0) {
				JOptionPane.showMessageDialog(this,
						"Za uvaj raèun nije izvršena uplata !!!!", "Poruka",
						JOptionPane.DEFAULT_OPTION);
				return;
			}
			String mysqlupit = "select * from doki where cskl='"
					+ qdsPojedIzlaz.getString("cskl") + "' and vrdok='"
					+ qdsPojedIzlaz.getString("vrdok") + "' and god='"
					+ qdsPojedIzlaz.getString("god") + "' and brdok="
					+ qdsPojedIzlaz.getInt("BRDOK");
			QueryDataSet myqds = hr.restart.util.Util.getNewQueryDataSet(
					mysqlupit, true);
            if (qdsAllIzlaz != null && qdsAllIzlaz.getRowCount() != 0) {
                qdsAllIzlaz.setBigDecimal("PLATITI", qdsAllIzlaz.getBigDecimal(
                        "PLATITI").subtract(qdsPojedIzlaz.getBigDecimal("PLATITI")));
                qdsAllIzlaz.setBigDecimal("SALDO", qdsAllIzlaz.getBigDecimal(
                        "UIRAC").subtract(qdsAllIzlaz.getBigDecimal("PLATITI")));
            }
      upls = UplRobno.getDataModule().getTempSet(Condition.equal("CDOC", dockey = raControlDocs.getKey(qdsPojedIzlaz, "doki")));
      upls.open();
      upls.deleteAllRows();
			qdsPojedIzlaz.setBigDecimal("PLATITI", Aus.zero2);
			qdsPojedIzlaz.setString("STATPLA", "N");
			qdsPojedIzlaz.setBigDecimal("SALDO", qdsPojedIzlaz.getBigDecimal(
					"UIRAC").subtract(qdsPojedIzlaz.getBigDecimal("PLATITI")));
			myqds.setBigDecimal("PLATITI", qdsPojedIzlaz
					.getBigDecimal("PLATITI"));
			myqds.setString("STATPLA", qdsPojedIzlaz.getString("STATPLA"));
			myqds.setAssignedNull("datupl");
			raTransaction.saveChanges(myqds);
			raTransaction.saveChanges(upls);
			qdsPojedIzlaz.setAssignedNull("datupl");
			qdsPojedIzlaz.setInt("DANIK", raDateUtil.getraDateUtil()
					.DateDifference(ut.getFirstSecondOfDay(qdsPojedIzlaz
									.getTimestamp("datdosp")),
							ut.getFirstSecondOfDay(hr.restart.util.Valid
									.getValid().findDate(false, 0))));

			getJPTV().fireTableDataChanged();
			JOptionPane.showMessageDialog(this, "Uplata poništena !!!",
					"Poruka", JOptionPane.DEFAULT_OPTION);
		} else {
			//      System.out.println();
			if (qdsPojedUlaz.getBigDecimal("PLATITI").doubleValue() == 0) {
				JOptionPane.showMessageDialog(this,
						"Za uvaj raèun nije izvršena uplata !!!!", "Poruka",
						JOptionPane.DEFAULT_OPTION);
				return;
			}
			String mysqlupit = "select * from doku where cskl='"
					+ qdsPojedUlaz.getString("cskl") + "' and vrdok='"
					+ qdsPojedUlaz.getString("vrdok") + "' and god='"
					+ qdsPojedUlaz.getString("god") + "' and brdok="
					+ qdsPojedUlaz.getInt("BRDOK");

			QueryDataSet myqds = hr.restart.util.Util.getNewQueryDataSet(
					mysqlupit, true);
            if (qdsAllUlaz != null && qdsAllUlaz.getRowCount() != 0) {
              qdsAllUlaz.setBigDecimal("PLATITI", qdsAllUlaz.getBigDecimal(
                        "PLATITI").subtract(qdsPojedUlaz.getBigDecimal("PLATITI")));
              qdsAllUlaz.setBigDecimal("SALDO", qdsAllUlaz.getBigDecimal(
                         "UIRAC").subtract(qdsAllUlaz.getBigDecimal("PLATITI")));
            }
      upls = UplRobno.getDataModule().getTempSet(Condition.equal("CDOC", dockey = raControlDocs.getKey(qdsPojedUlaz, "doki")));
      upls.open();
      upls.deleteAllRows();
			qdsPojedUlaz.setBigDecimal("PLATITI", Aus.zero2);
            qdsPojedUlaz.setString("STATPLA", "N");
			qdsPojedUlaz.setBigDecimal("SALDO", qdsPojedUlaz.getBigDecimal(
					"UIRAC").subtract(qdsPojedUlaz.getBigDecimal("PLATITI")));
			myqds.setBigDecimal("PLATITI", qdsPojedUlaz
					.getBigDecimal("PLATITI"));
			myqds.setString("STATPLA", qdsPojedUlaz.getString("STATPLA"));
			myqds.setAssignedNull("datupl");
			raTransaction.saveChanges(myqds);
			raTransaction.saveChanges(upls);
			qdsPojedUlaz.setAssignedNull("datupl");
			qdsPojedUlaz.setInt("DANIK", raDateUtil.getraDateUtil()
					.DateDifference(ut.getFirstSecondOfDay(qdsPojedUlaz
									.getTimestamp("datdosp")),
							ut.getFirstSecondOfDay(hr.restart.util.Valid
									.getValid().findDate(false, 0))));

			getJPTV().fireTableDataChanged();
			JOptionPane.showMessageDialog(this, "Uplata poništena !!!",
					"Poruka", JOptionPane.DEFAULT_OPTION);
			//TODO dobavljaci

			//      if (qdsPojedUlaz.getString("STATPLA").equals("N")) return;
			//      
			//      String mysqlupit = "select * from doku where
			// cskl='"+qdsPojedUlaz.getString("cskl")+"' and vrdok='"+
			//      							qdsPojedUlaz.getString("vrdok")+"' and
			// god='"+qdsPojedUlaz.getString("god")+"' and brdok="+
			//      							qdsPojedUlaz.getInt("BRDOK");
			//      QueryDataSet myqds =
			// hr.restart.util.Util.getNewQueryDataSet(mysqlupit,true);
			//      myqds.setString("STATPLA","N");
			//      qdsPojedUlaz.setString("STATPLA","N");
			//      raTransaction.saveChanges(myqds);
			//      getJPTV().fireTableDataChanged();
			//      
			//      JOptionPane.showMessageDialog(this,
			//          "Status raèuna - neplaæen","Poruka",
			//           JOptionPane.DEFAULT_OPTION);
			//// this.getJPTV().getDataSet().refresh();
		}
	}
	
	void addUpl(BigDecimal iznos, Timestamp datum, String nap) {
		Valid.getValid().execSQL("SELECT MAX(rbr) as mrbr FROM UplRobno WHERE cdoc='"+dockey+"'");
		DataSet ds = Valid.getValid().getDataAndClear();
		ds.open();
		short rbr = ds.rowCount() == 0 ? 0 : ds.getShort("MRBR");
		upls.enableDataSetEvents(false);
		for (upls.first(); upls.inBounds(); upls.next())
			if (upls.getShort("RBR") > rbr) rbr = upls.getShort("RBR");
		upls.enableDataSetEvents(true);
		
		upls.insertRow(false);
		upls.setString("CDOC", dockey);
		upls.setShort("RBR", ++rbr);
		upls.setBigDecimal("IZNOS", iznos);
		upls.setTimestamp("DATUM", datum);
		upls.setString("NAP", nap);
		upls.post();
	}

	public void azuriranjeNaplate() {
		if (tds.getString("PK").equals("K")) {
			if (jrfCPAR.getText().equalsIgnoreCase("")) {
				return;
			}
			if ("PON".equals(qdsPojedIzlaz.getString("VRDOK"))) {
			  JOptionPane.showMessageDialog(this.getWindow(), "Ponude se ne mogu ažurirati.\n" +
			  		"Potrebno ih je prebaciti u raèune ili poništiti.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
			  return;
			}
			racun.setDataSet(qdsPojedIzlaz);
			racun.setColumnName("UIRAC");
			platiti.setDataSet(qdsPojedIzlaz);
			platiti.setColumnName("PLATITI");
			uplata.setDataSet(tds);
			uplata.setColumnName("UPLATA");
			datupl.setDataSet(tds);
			datupl.setColumnName("datupl");
			opis.setDataSet(tds);
            opis.setColumnName("NAP");

			uplata.getDataSet().setBigDecimal("UPLATA",
					qdsPojedIzlaz.getBigDecimal("SALDO"));
			datupl.getDataSet().setTimestamp("datupl",
					hr.restart.util.Valid.getValid().findDate(false, danasUpl ? 0 : -1));
			
			upls = UplRobno.getDataModule().getTempSet("CDOC RBR NAP DATUM IZNOS",
			    Condition.equal("CDOC", dockey = raControlDocs.getKey(qdsPojedIzlaz, "doki")));
			upls.open();
			upls.setSort(new SortDescriptor(new String[] {"DATUM"}));
			if (upls.rowCount() == 0 && qdsPojedIzlaz.getBigDecimal("PLATITI").signum() != 0) {
				addUpl(qdsPojedIzlaz.getBigDecimal("PLATITI"), qdsPojedIzlaz.getTimestamp("DATUPL"), "");
				upls.saveChanges();
			}
			jup.setDataSet(upls);

			rcc.setLabelLaF(racun, false);
			rcc.setLabelLaF(platiti, false);
            miniFrame = new JraDialog(getJframe(), "Ažuriranje raèuna", true);
            miniFrame.getContentPane().setLayout(new BorderLayout());
            miniFrame.getContentPane().add(miniPanel, BorderLayout.CENTER);
            miniFrame.getContentPane().add(miniOK, BorderLayout.SOUTH);
            miniOK.registerOKPanelKeys(miniFrame);
			miniFrame.pack();
			miniFrame.setLocation(this.getLocation().x
					+ (int) ((this.getSize().getWidth() - miniFrame.getSize()
							.getWidth()) / 2), this.getLocation().y
					+ (int) ((this.getSize().getHeight() - miniFrame.getSize()
							.getHeight()) / 2));

			miniFrame.setVisible(true);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					uplata.requestFocus();
				}
			});
		} else {
			if (jrfCPAR.getText().equalsIgnoreCase("")) {
				return;
			}
			racun.setDataSet(qdsPojedUlaz);
			racun.setColumnName("UIRAC");
			platiti.setDataSet(qdsPojedUlaz);
			platiti.setColumnName("PLATITI");
			uplata.setDataSet(tds);
			uplata.setColumnName("UPLATA");
			datupl.setDataSet(tds);
			datupl.setColumnName("datupl");

			uplata.getDataSet().setBigDecimal("UPLATA",
					qdsPojedUlaz.getBigDecimal("SALDO"));
			datupl.getDataSet().setTimestamp("datupl",
					hr.restart.util.Valid.getValid().findDate(false, danasUpl ? 0 : -1));
			upls = UplRobno.getDataModule().getTempSet("CDOC RBR NAP DATUM IZNOS",
			    Condition.equal("CDOC", dockey = raControlDocs.getKey(qdsPojedUlaz, "doku")));
			upls.open();
			upls.setSort(new SortDescriptor(new String[] {"DATUM"}));
			if (upls.rowCount() == 0 && qdsPojedUlaz.getBigDecimal("PLATITI").signum() != 0) {
				addUpl(qdsPojedUlaz.getBigDecimal("PLATITI"), qdsPojedUlaz.getTimestamp("DATUPL"), "");
				upls.saveChanges();
			}
			jup.setDataSet(upls);

			rcc.setLabelLaF(racun, false);
			rcc.setLabelLaF(platiti, false);
            
            miniFrame = new JraDialog(getJframe(), "Ažuriranje raèuna", true);
            miniFrame.getContentPane().setLayout(new BorderLayout());
            miniFrame.getContentPane().add(miniPanel, BorderLayout.CENTER);
            miniFrame.getContentPane().add(miniOK, BorderLayout.SOUTH);
            miniOK.registerOKPanelKeys(miniFrame);
			miniFrame.pack();
			miniFrame.setLocation(this.getLocation().x
					+ (int) ((this.getSize().getWidth() - miniFrame.getSize()
							.getWidth()) / 2), this.getLocation().y
					+ (int) ((this.getSize().getHeight() - miniFrame.getSize()
							.getHeight()) / 2));

			miniFrame.setVisible(true);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					uplata.requestFocus();
				}
			});

			//      //TODO dobavljaci
			//      if (qdsPojedUlaz.getString("STATPLA").equals("D")) return;
			//
			//      String mysqlupit = "select * from doku where
			// cskl='"+qdsPojedUlaz.getString("cskl")+"' and vrdok='"+
			//      							qdsPojedUlaz.getString("vrdok")+"' and
			// god='"+qdsPojedUlaz.getString("god")+"' and brdok="+
			//      							qdsPojedUlaz.getInt("BRDOK");
			//      QueryDataSet myqds =
			// hr.restart.util.Util.getNewQueryDataSet(mysqlupit,true);
			//      myqds.setString("STATPLA","D");
			//      qdsPojedUlaz.setString("STATPLA","D");
			//      raTransaction.saveChanges(myqds);
			//      getJPTV().fireTableDataChanged();
			//      
			//      JOptionPane.showMessageDialog(this,
			//          "Status raèuna - plaæen","Poruka",
			//           JOptionPane.DEFAULT_OPTION);
		}
	}

	public QueryDataSet getQDS() {
		return this.getJPTV().getDataSet();
	}

	public String getCSKL() {
		return jrfCSKL.getText();
	}

	public String getCORG() {
		return jrfCORG.getText();
	}

	public String getStatus() {
		return status;
	}

	public String getDospjelo() {
		return dospjelo;
	}

	public void toHash() {
		hm.clear();
		hm.put("CORG", jrfCORG.getText());
		hm.put("CSKL", jrfCSKL.getText());
		hm.put("tds", tds);
		hm.put("JCstatus", new Integer(JCstatus.getSelectedIndex()));
		hm.put("JCdosp", new Integer(JCdosp.getSelectedIndex()));
	}

	public void fromHash() {

		jrfCPAR.setText("");
		jrfCPAR.emptyTextFields();
		if (hm.containsKey("CORG")) {
			jrfCORG.setText((String) hm.get("CORG"));
			jrfCORG.forceFocLost();
		} else {
			jrfCORG.setText("");
			jrfCORG.emptyTextFields();
		}
		if (hm.containsKey("CSKL")) {
			jrfCSKL.setText((String) hm.get("CSKL"));
			jrfCSKL.forceFocLost();
		} else {
			jrfCSKL.setText("");
			jrfCSKL.emptyTextFields();
		}

		if (hm.containsKey("tds")) {
			tds = (TableDataSet) hm.get("tds");
		} else {
			tds = null;
		}

		if (hm.containsKey("JCdosp")) {
			JCdosp.setSelectedIndex(((Integer) hm.get("JCdosp")).intValue());
		}
		if (hm.containsKey("JCstatus")) {
			JCstatus
					.setSelectedIndex(((Integer) hm.get("JCstatus")).intValue());
		}
        if (tds != null) {
    		if (tds.getString("PK").equals("K")) {
    		  if (qdsAllIzlaz.getInt("CPAR") == qdsPojedIzlaz.getInt("CPAR") ||
    		      ld.raLocate(qdsAllIzlaz, "CPAR", 
    		          Integer.toString(qdsPojedIzlaz.getInt("CPAR")))) {
    		  
    		    qdsAllIzlaz.setBigDecimal("UIRAC", Aus.sum("UIRAC", qdsPojedIzlaz));
    		    qdsAllIzlaz.setBigDecimal("PLATITI", Aus.sum("PLATITI", qdsPojedIzlaz));
    		    Aus.sub(qdsAllIzlaz, "SALDO", "UIRAC", "PLATITI");
    		  }
    		    
    			this.setDataSetAndSums(qdsAllIzlaz,
    					new String[] { "UIRAC", "PLATITI", "SALDO" });
    		} else {
    		  if (qdsAllUlaz.getInt("CPAR") == qdsPojedUlaz.getInt("CPAR") ||
                  ld.raLocate(qdsAllUlaz, "CPAR", 
                      Integer.toString(qdsPojedUlaz.getInt("CPAR")))) {
              
                qdsAllUlaz.setBigDecimal("UIRAC", Aus.sum("UIRAC", qdsPojedUlaz));
                qdsAllUlaz.setBigDecimal("PLATITI", Aus.sum("PLATITI", qdsPojedUlaz));
                Aus.sub(qdsAllUlaz, "SALDO", "UIRAC", "PLATITI");
              }
    		  
                this.setDataSetAndSums(qdsAllUlaz,
                    new String[] { "UIRAC", "PLATITI", "SALDO" });
    			/*this.setDataSet(null);
    			ok_action();*/
            
  		     }
        } else ok_action();
		/*getJPTV().getColumnsBean().initialize();
		getJPTV().getColumnsBean().rnvRefresh.setEnabled(false);*/
	}

	/*
	 * public void togle_ctrl_A(){ int row =0; if (getJPTV().getDataSet()!=null) {
	 * row = getJPTV().getDataSet().getRow(); } getJPTV().enableEvents(false);
	 * for
	 * (getJPTV().getDataSet().first();getJPTV().getDataSet().inBounds();getJPTV().getDataSet().next()){
	 * rSTM.toggleSelection(getJPTV().getDataSet()); }
	 * getJPTV().getDataSet().goToRow(row); getJPTV().enableEvents(true); }
	 */

	/*
	 * public void togle(){ int row =0; if (getJPTV().getDataSet()!=null) { row =
	 * getJPTV().getDataSet().getRow(); }
	 * rSTM.toggleSelection(getJPTV().getDataSet());
	 * getJPTV().getDataSet().goToClosestRow(row+1); }
	 * 
	 * protected void this_keyPressed(java.awt.event.KeyEvent e) { if
	 * (e.getKeyCode()==e.VK_ENTER) { e.consume(); togle(); }
	 * super.this_keyPressed(e); }
	 */

	public java.sql.Timestamp pocDat() {
		return tds.getTimestamp("pocDatum");
	}

	public java.sql.Timestamp zavDat() {
		return tds.getTimestamp("zavDatum");
	}

  public String navDoubleClickActionName() {
    return "Raèuni";
  }
  
  private int[] visibleAll = new int[]{0,1,2,3,4};
  private int[] visibleSingle = new int[]{0,1,2,3,4,5,6,7};

  public int[] navVisibleColumns(){
    return jrfCPAR.getText().length() == 0 ? visibleAll : visibleSingle;
  }
}
