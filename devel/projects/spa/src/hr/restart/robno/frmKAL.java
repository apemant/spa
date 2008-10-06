/****license*****************************************************************
**   file: frmKAL.java
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
import hr.restart.baza.Stdoku;
import hr.restart.baza.VTprijenos;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraDialog;
import hr.restart.util.Aus;
import hr.restart.util.LinkClass;
import hr.restart.util.OKpanel;
import hr.restart.util.VarStr;
import hr.restart.util.raImages;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;
import hr.restart.util.raTwoTableChooser;
import hr.restart.util.startFrame;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;

public class frmKAL extends frmUlazTemplate implements IZavtrHandler {

	frmVTZtr zt = new frmVTZtr(); // (ab.f)

	frmVTZtrstav zts = new frmVTZtrstav();

	private raKalkulBDUlaz rKBDU = new raKalkulBDUlaz();

	private LinkClass lc = LinkClass.getLinkClass();

	private hr.restart.util.lookupData lD = hr.restart.util.lookupData
			.getlookupData();

	private QueryDataSet vezaNaPrimku = null;

	JraDialogPrimke jradialogprimke = new JraDialogPrimke(this.getJframe(),
			"Odabir stavaka primki", true) {
		public void okPress() {
          raLocalTransaction rLT = new raLocalTransaction() {
            public boolean transaction() {
                return localOKpress();
            }};
            if (rLT.execTransaction()) 
              postAdd();
		}

		public void cancelPress() {
			localCancelPress();
		}
	};

	hr.restart.robno.jpUlazMaster jpMaster = new hr.restart.robno.jpUlazMaster(
			(frmUlazTemplate) this, 1);

	hr.restart.robno.jpUlazDetail jpDetail = new hr.restart.robno.jpUlazDetail(
			this);

	private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(
			false);

	private hr.restart.util.lookupData ld = hr.restart.util.lookupData
			.getlookupData();

	private QueryDataSet stanje = new QueryDataSet();

	private QueryDataSet stavkePRI = new QueryDataSet();

	private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass
			.getraCommonClass();

	//	private raControlDocs rCD = new raControlDocs();

	raNavAction rnvUnosKalkulacije = new raNavAction("Nova stavka kalkulacije",
			raImages.IMGADD, java.awt.event.KeyEvent.VK_F2) {
		public void actionPerformed(ActionEvent e) {
			keyStavkaKalkulacije();
		}
	};

	public jpUlazDetail getDetailPanel() {
		return jpDetail;
	}

	public jpUlazMaster getMasterPanel() {
		return jpMaster;
	}

	public frmVTZtrstav getZavtrDetail() {
		return zts;
	}

	public frmVTZtr getZavtrMaster() {
		return zt;
	}

	public void keyStavkaKalkulacije() {

		String org = OrgStr.getOrgStr().getKNJCORG(true);
		String sqlsklad = "select * from sklad where knjig in ('" + org + "')";
		VarStr skl = new VarStr("'");
		QueryDataSet sklad = hr.restart.util.Util.getNewQueryDataSet(sqlsklad,
				true);
		for (sklad.first(); sklad.inBounds(); sklad.next()) {
			skl.append(sklad.getString("CSKL")).append("','");
		}
		skl.chopRight(2);
		String sqldata = "Select doku.datdok,doku.cskl,doku.vrdok,"
				+ "doku.god,doku.brdok,stdoku.rbr,stdoku.cart,stdoku.cart1,stdoku.bc,nazart,kol "
				+ "from doku,stdoku where "
				+ "doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok "
				+ "AND doku.god = stdoku.god AND doku.brdok = stdoku.brdok "
				+ "AND stdoku.cskl in (" + skl + ") and " + "doku.cpar="
				+ getMasterSet().getInt("CPAR") + " and "
				+ "stdoku.vrdok='PRI' and stdoku.god='"
				+ getMasterSet().getString("GOD") + "' "
				+ " and stdoku.status='N'";
		QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(sqldata,
				true);

		if (qds.getRowCount() == 0) {
			javax.swing.JOptionPane.showMessageDialog(raMaster.getWindow(),
					"Nema primki tog obavljaèa za kalkulaciju !!!", "Sistem",
					JOptionPane.WARNING_MESSAGE, null);
			return;
		}
		prikaz_i_dohvatPrimke(qds);
	}

	public StorageDataSet setupStorageDataSet() {
		StorageDataSet stds = new StorageDataSet();
		Column cskl = dm.getStdoku().getColumn("CSKL").cloneColumn();
		cskl.setVisible(TriStateProperty.FALSE);
		Column god = dm.getStdoku().getColumn("GOD").cloneColumn();
		god.setVisible(TriStateProperty.FALSE);
		Column vrdok = dm.getStdoku().getColumn("VRDOK").cloneColumn();
		vrdok.setVisible(TriStateProperty.FALSE);
		Column brdok = dm.getStdoku().getColumn("BRDOK").cloneColumn();
		brdok.setVisible(TriStateProperty.FALSE);

		Column datdok = dm.getDoku().getColumn("DATDOK").cloneColumn();
		Column key = dm.getDoki().getColumn("OPIS").cloneColumn();
		key.setWidth(dm.getDoku().getColumn("CSKL").getWidth()
				+ dm.getDoku().getColumn("VRDOK").getWidth()
				+ dm.getDoku().getColumn("GOD").getWidth()
				+ dm.getDoku().getColumn("BRDOK").getWidth() + 4);
		Column rbr = dm.getStdoku().getColumn("RBR").cloneColumn();
		Column cart = dm.getDoki().getColumn("OPIS").cloneColumn();
		cart.setColumnName("NAZART");
		//		cart.setWidth(cart.getWidth() + 100);
		Column kol = dm.getStdoku().getColumn("KOL").cloneColumn();
		stds.setColumns(new Column[] { cskl, vrdok, brdok, god, datdok, key,
				rbr, cart, kol });
		stds.open();
		return stds;
	}

	public void prikaz_i_dohvatPrimke(QueryDataSet qds) {

		StorageDataSet stds = setupStorageDataSet();
		StorageDataSet stdsright = setupStorageDataSet();
		String tmp = "";

		for (qds.first(); qds.inBounds(); qds.next()) {
			stds.insertRow(false);
			stds.setTimestamp("DATDOK", qds.getTimestamp("DATDOK"));
			stds.setString("OPIS", qds.getString("CSKL") + "-"
					+ qds.getString("VRDOK") + "-" + qds.getString("GOD") + "-"
					+ qds.getInt("BRDOK"));
			if (frmParam.getParam("robno", "indiCart", "CART")
					.equalsIgnoreCase("CART")) {
				tmp = String.valueOf(qds.getInt("CART")) + "-";
			} else {
				tmp = qds.getString(frmParam.getParam("robno", "indiCart",
						"CART"))
						+ "-";
			}
			stds.setString("NAZART", tmp + qds.getString("NAZART"));
			stds.setShort("RBR", qds.getShort("RBR"));
			stds.setBigDecimal("KOL", qds.getBigDecimal("KOL"));
			stds.setString("CSKL", qds.getString("CSKL"));
			stds.setString("VRDOK", qds.getString("VRDOK"));
			stds.setInt("BRDOK", qds.getInt("BRDOK"));
			stds.setString("GOD", qds.getString("GOD"));
		}

        stds.setTableName("PRK-KAL");
        stdsright.setTableName("PRK-KAL");
		jradialogprimke.setLeftSet(stds);
		jradialogprimke.setRightSet(stdsright);
		jradialogprimke.initialize();
		startFrame.getStartFrame().centerFrame(jradialogprimke, 0,
				"Odabir stavaka primke");
		jradialogprimke.setVisible(true);
	}

	public void AfterCancelMaster() {
		zt.kill();
	}

	public void AfterCancelDetail() {
		zts.kill();
	}

	public boolean DeleteCheckMaster() {
		zt.prepareSave();
		return super.DeleteCheckMaster();
	}

	public boolean DeleteCheckDetail() {
		zts.prepareSave();
		lc.TransferFromDB2Class(getDetailSet(), rKBDU.stavkaold);
		findSTANJE();
		lc.TransferFromDB2Class(stanjeSet, rKBDU.stanje);
		return super.DeleteCheckDetail();
	}

	public void afterSetModeMaster(char oldm, char newm) {
		super.afterSetModeMaster(oldm, newm);
		if (newm == 'B')
			zt.kill();
	}

	public void afterSetModeDetail(char oldm, char newm) {
		super.afterSetModeDetail(oldm, newm);
		if (newm == 'B')
			zts.kill();
	}

	public boolean doWithSaveMaster(char mode) {
		//if (mode == 'N' || mode == 'B' || (mode == 'I' && enableZT))
			try {
				zt.saveChanges(getMasterSet().getString("CSHZT").equals(
								"YES") ? mode : 'B');
			} catch (Exception e) {
				return false;
			}
		return super.doWithSaveMaster(mode);
	}
	
	public void userEditEnable(boolean en) {
	  rnvUnosKalkulacije.setEnabled(en);
	  super.userEditEnable(en);
	}

	public boolean localOKpress() {

		String sqlUpit = "";
		String sqlStanje = "";
		if (jradialogprimke.getRightSet().getRowCount() == 0) {
			JOptionPane.showConfirmDialog(null,
					"Nije odabrana niti jedna stavka za kalkulaciju",
					"Upozorenje", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (jradialogprimke.getRightSet().getRowCount() != 1) {
			JOptionPane.showConfirmDialog(null,
					"Dozvoljeno je odabrati samo jednu stavku za prijenos",
					"Upozorenje", JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		for (jradialogprimke.getRightSet().first(); jradialogprimke
				.getRightSet().inBounds(); jradialogprimke.getRightSet().next()) {
			sqlUpit = "select * from stdoku where ";
			sqlStanje = "select * from stanje where ";
			sqlUpit = sqlUpit + "cskl='"
					+ jradialogprimke.getRightSet().getString("CSKL")
					+ "' and " + "vrdok='"
					+ jradialogprimke.getRightSet().getString("VRDOK")
					+ "' and " + "god='"
					+ jradialogprimke.getRightSet().getString("GOD") + "' and "
					+ "brdok=" + jradialogprimke.getRightSet().getInt("BRDOK")
					+ " and " + "rbr="
					+ jradialogprimke.getRightSet().getShort("RBR");
			QueryDataSet qdsStPrim = hr.restart.util.Util.getNewQueryDataSet(
					sqlUpit, true);

			if (qdsStPrim == null || qdsStPrim.getRowCount() == 0)
				throw new RuntimeException(
						"GREŠKA !!! Ne postoji stavka za ovaj upit ---> "
								+ sqlUpit);

			sqlStanje = sqlStanje + "cskl='"
					+ jradialogprimke.getRightSet().getString("CSKL")
					+ "' and " + "god='"
					+ jradialogprimke.getRightSet().getString("GOD") + "' and "
					+ "cart=" + qdsStPrim.getInt("CART");

			stanjeSet = hr.restart.util.Util
					.getNewQueryDataSet(sqlStanje, true);

			if (stanjeSet == null || stanjeSet.getRowCount() == 0) {
				stanjeSet.insertRow(false);
				stanjeSet.setString("CSKL", qdsStPrim.getString("CSKL"));
				stanjeSet.setString("GOD", getMasterSet().getString("GOD"));
				stanjeSet.setInt("CART", qdsStPrim.getInt("CART"));
				nullStanje(stanjeSet);
			}
			getDetailSet().insertRow(false);
			getDetailSet().setString("CSKL", getMasterSet().getString("CSKL"));
			getDetailSet().setString("CSKLART", qdsStPrim.getString("CSKL"));
			getDetailSet().setString("GOD", getMasterSet().getString("GOD"));
			getDetailSet()
					.setString("VRDOK", getMasterSet().getString("VRDOK"));
			getDetailSet().setInt("BRDOK", getMasterSet().getInt("BRDOK"));
			int rbr = Rbr.getRbr().getRbsID(getDetailSet());
			getDetailSet().setShort("RBR", (short) rbr);
			getDetailSet().setInt("RBSID", rbr);
			getDetailSet().setInt("CART", qdsStPrim.getInt("CART"));
			getDetailSet().setString("CART1", qdsStPrim.getString("CART1"));
			getDetailSet().setString("BC", qdsStPrim.getString("BC"));
			getDetailSet().setString("NAZART", qdsStPrim.getString("NAZART"));
			getDetailSet().setBigDecimal("KOL", qdsStPrim.getBigDecimal("KOL"));
			getDetailSet().setBigDecimal("PZT",
					getMasterSet().getBigDecimal("UPZT"));
			if (getMasterSet().getString("CSHZT").equals("YES")) {
				zts.needsRefresh('N');
			}
			getDetailSet()
					.setBigDecimal("SKOL", stanjeSet.getBigDecimal("KOL"));
			getDetailSet().setBigDecimal("SVC", stanjeSet.getBigDecimal("VC"));
			getDetailSet().setBigDecimal("SMC", stanjeSet.getBigDecimal("MC"));
			getDetailSet().setBigDecimal("VC", stanjeSet.getBigDecimal("VC"));
			getDetailSet().setBigDecimal("MC", stanjeSet.getBigDecimal("MC"));
			rCD.unosKalkulacije(getDetailSet(), stanjeSet);
			stanjeSet.setTimestamp("DATZK", 
	            getMasterSet().getTimestamp("DATDOK"));
			lc.TransferFromDB2Class(getDetailSet(), rKBDU.stavka);
			rKBDU.stavkaold.Init();
			rKBDU.vrzal = hr.restart.util.Util.getNewQueryDataSet(
					"select vrzal from sklad where cskl ='"
							+ qdsStPrim.getString("CSKL") + "'", true)
					.getString("VRZAL");
			stanjeSet.setBigDecimal("KOLUL", stanjeSet.getBigDecimal("KOLUL")
					.add(getDetailSet().getBigDecimal("KOL")));
			stanjeSet.setBigDecimal("KOL", stanjeSet.getBigDecimal("KOLUL")
					.subtract(stanjeSet.getBigDecimal("KOLIZ")));
			getDetailSet().setString(
					"ID_STAVKA",
					raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
							"vrdok", "god", "brdok", "rbsid" }, "stdoku"));
			getDetailSet().setString("VEZA", qdsStPrim.getString("ID_STAVKA"));
			qdsStPrim.setString("STATUS", "P");
			qdsStPrim.setString("VEZA", getDetailSet().getString("ID_STAVKA"));
            vtprij = VTprijenos.getDataModule().getTempSet("1=0");
            vtprij.open();
			vtprij.insertRow(true);
			vtprij.setString("KEYSRC",
					qdsStPrim.getString("ID_STAVKA"));
			vtprij.setString("KEYDEST",
					getDetailSet().getString("ID_STAVKA"));

			if (getMasterSet().getString("CSHZT").equals("YES")) {
				zts.prepareSave();
				try {
					zts.saveChanges('N');
				} catch (Exception e) {

				}
			}
			kalfindDOBART();
			raTransaction.saveChanges(getDetailSet());
			raTransaction.saveChanges(stanjeSet);
			raTransaction.saveChanges(qdsStPrim);
			raTransaction.saveChanges(vtprij);
			raTransaction.saveChanges(dm.getDob_art());
		}
		jradialogprimke.setVisible(false);
        return true;
	}

	boolean findSTANJE() {
		stanjeSet = hr.restart.util.Util.getNewQueryDataSet(rdUtil.getUtil()
				.findStanje(getDetailSet().getString("CSKLART"),
						getDetailSet().getInt("CART"),
						getMasterSet().getString("GOD")), true);
		if (stanjeSet.getRowCount() > 0) {
			return true;
		}
		return false;
	}

	public void nullStanje(QueryDataSet ds) {
		BigDecimal nula = Aus.zero2;
		ds.setBigDecimal("KOLPS", nula);
		ds.setBigDecimal("KOLUL", nula);
		ds.setBigDecimal("KOLIZ", nula);
		ds.setBigDecimal("KOLREZ", nula);
		ds.setBigDecimal("NABPS", nula);
		ds.setBigDecimal("MARPS", nula);
		ds.setBigDecimal("PORPS", nula);
		ds.setBigDecimal("VPS", nula);
		ds.setBigDecimal("NABUL", nula);
		ds.setBigDecimal("MARUL", nula);
		ds.setBigDecimal("PORUL", nula);
		ds.setBigDecimal("VUL", nula);
		ds.setBigDecimal("NABIZ", nula);
		ds.setBigDecimal("MARIZ", nula);
		ds.setBigDecimal("PORIZ", nula);
		ds.setBigDecimal("VIZ", nula);
		ds.setBigDecimal("KOL", nula);
		ds.setBigDecimal("ZC", nula);
		ds.setBigDecimal("VRI", nula);
		ds.setBigDecimal("NC", nula);
		ds.setBigDecimal("VC", nula);
		ds.setBigDecimal("MC", nula);
		ds.setBigDecimal("KOLMAT", nula);
	}

	public void localCancelPress() {
		jradialogprimke.setVisible(false);
	}

	synchronized public void postAdd() {

		raNavAction[] rNA = raDetail.getNavBar().getNavContainer()
				.getNavActions();
		raDetail.getJpTableView().fireTableDataChanged();
		for (int i = 0; i < rNA.length; i++) {
			if (rNA[i].getIdentifier().equalsIgnoreCase("Izmjena")) {
				rNA[i].setEnabled(true);
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				raDetail.setLockedMode('I');
				raDetail.rnvUpdate_action();
			}
		});
	}

	{
		dm.getDokuKAL().open();
		dm.getStdokuKAL().open();
	}

	public frmKAL() {

	  prSTAT='K';
		vrDok = "KAL";
		masterTitle = "Kalkulacije";
		detailTitle = "Stavke kalkulacije";
		jpp = presKAL.getPres();
		setJPanelMaster(jpMaster);
		setJPanelDetail(jpDetail);
		jpDetail.rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());

		setMasterSet(dm.getDokuKAL());
		setDetailSet(dm.getStdokuKAL());
		jpMaster.setDataSet(getMasterSet());

		jpDetail.setDataSet(getDetailSet(), getMasterSet());
		zt.setMasterFrame(this);
		zts.setMasterFrame(this);
		raDetail.getJpTableView().getNavBar().removeStandardOption(0);
		jpDetail.rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());

		raDetail.addOption(rnvUnosKalkulacije, 0, false);
		raDetail.addOption(rnvKartica, 4, false);
		jpMaster.jpBRDOKUL.setIsDodatak(true);
		setVisibleColsMaster(new int[] { 4, 5, 6, 13, 14 });
		raMaster.getRepRunner().addReport("hr.restart.robno.repPrkKal",
				"hr.restart.robno.repPrkProvider", "PriKalkMegablastVersion",
				"Kalkulacija");
		raDetail.getRepRunner().addReport("hr.restart.robno.repPrkKAl",
				"hr.restart.robno.repPrkProvider", "PriKalkMegablastVersion",
				"Kalkulacija");
		jpMaster.jpGetVal.setRaDataSet(getMasterSet());
	}

	public void SetFokusMaster(char mode) {
		boolean detailExist = Stdoku.getDataModule().getRowCount(
				Condition.whereAllEqual(new String[] { "CSKL", "GOD", "VRDOK",
						"BRDOK" }, getMasterSet())) > 0;
		if (mode == 'I' && detailExist)
			jpMaster.jpGetVal.setValutaEditable(false);
		else
			jpMaster.jpGetVal.setValutaEditable(true);

		if (mode == 'N') {
			getMasterSet().setTimestamp("DATDOK",
					jpp.getSelRow().getTimestamp("DATDOK-to"));
			getMasterSet().setTimestamp("DVO",
					jpp.getSelRow().getTimestamp("DATDOK-to"));
			jpp.copySelValues();
		}

		enableZT = (mode == 'N' || (mode == 'I' && !detailExist));
		zt.needsRefresh();
		jpMaster.initPanel(mode);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jpMaster.jrfCPAR.requestFocus();
			}
		});
	}

	public void SetFokusDetail(char mode) {
		super.SetFokusDetail(mode);
		lc.TransferFromDB2Class(getDetailSet(), rKBDU.stavkaold);
		if (mode == 'N') {
			getDetailSet().setBigDecimal("PZT",
					getMasterSet().getBigDecimal("UPZT")); // (ab.f)
			jpDetail.rpcart.setCART();
		}
		jpDetail.findVirtualFields(mode);
		if (getMasterSet().getString("CSHZT").equals("YES"))
			zts.needsRefresh(mode);

		rcc.setLabelLaF(jpDetail.jtfKOL, false);
		jpDetail.rpcart.EnabDisab(false);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jpDetail.jtfDC.requestFocus();
				jpDetail.jtfDC.selectAll();
			}
		});
	}

	public void EntryPointDetail(char mode) {
		super.EntryPointDetail(mode);
		jpDetail.disableDefFields();
        if (mode == 'I') jpDetail.rpcart.EnabDisab(false);
		rcc.setLabelLaF(jpDetail.jtfKOL, false);
	}

	public boolean ValidacijaMaster(char mode) {
		if (!super.ValidacijaMaster(mode))
			return false;
		if (vl.isEmpty(jpMaster.jrfCPAR))
			return false;
		if (zt.isShowing())
			zt.rnvExit_action();
		if (enableZT) getMasterSet().setString("CSHZT",
					jpMaster.jcbZT.isSelected() ? "YES" : "");
		zt.prepareSave();
		return true;
	}

	public boolean ValidacijaDetail(char mode) {
		if (zts.isShowing())
			zts.rnvExit_action();

		if (vl.isEmpty(jpDetail.jtfKOL))
			return false;
		if (vl.isEmpty(jpDetail.jtfDC))
			return false;
		if (vl.isEmpty(jpDetail.jtfVC))
			return false;
		if (vl.isEmpty(jpDetail.jtfMC))
			return false;
		if (!dlgSerBrojevi.getdlgSerBrojevi().findSB(jpDetail.rpcart,
				getDetailSet(), 'U', mode)) {
			return false;
		}
		if (!super.ValidacijaDetail(mode))
			return false;
		zts.prepareSave();
		return true;
	}

	public void AfterAfterSaveMaster(char mode) {
		if (mode == 'N') {
			raMaster.setLockedMode('I');
			jBStavke_actionPerformed(null);
			return;
		}
		super.AfterAfterSaveMaster(mode);
	}

	public void AfterSaveDetail(char mode) {
		super.AfterSaveDetail(mode);
	}

	public boolean ValDPEscapeDetail(char mode) {
		if (mode == 'N') {
			if (jpDetail.rpcart.getCART().trim().equals("")) {
				return true;
			} else {
				getDetailSet().setBigDecimal("DC", main.nul);
				getDetailSet().setBigDecimal("PRAB", main.nul);
				getDetailSet().setBigDecimal("PZT", main.nul);
				getDetailSet().setBigDecimal("PMAR", main.nul);
				getDetailSet().setBigDecimal("VC", main.nul);
				getDetailSet().setBigDecimal("MC", main.nul);
				getDetailSet().setBigDecimal("KOL", main.nul);
				jpDetail.kalkulacija(1);
				jpDetail.disableUnosFields(true, 'P');
				jpDetail.rpcart.setCART();
				jpDetail.findSTANJE(' ');
				return false;
			}
		} else {
			return true;
		}
	}

	public void findVezaNaPrimku() {
		//		String upit ="SELECT * FROM stdoku WHERE "+
		//		"cskl||'-'||vrdok||'-'||god||'-'||brdok||'-'||rbsid||'-'"+
		//		" in ('"+dm.getVTprijenos().getString("KEYSRC")+"')";
//		String upit = "SELECT * FROM stdoku WHERE "
//				+ "cskl||'-'||vrdok||'-'||god||'-'||brdok||'-'||rbsid||'-'"
//				+ " in ('" + getDetailSet().getString("VEZA") + "')";

	  //tu je njesra ako ima crticu u cskl, ali tesko je naci zajednicki jezik medju bazama
	    String[] veza = new VarStr(getDetailSet().getString("VEZA")).split('-');
	    String upit = "SELECT * FROM stdoku WHERE cskl='"+veza[0]+"' AND vrdok = '"+veza[1]
                      +"' AND god ='"+veza[2]
	                  +"' AND brdok ="+veza[3]
	                  +" AND rbsid = "+veza[4];
		vezaNaPrimku = hr.restart.util.Util.getNewQueryDataSet(upit, true);
	}

    
    QueryDataSet vtprij = null;
    
	public boolean localDodatniDetailCheck() {
		String key = rCD.getKey(getDetailSet());
        vtprij = VTprijenos.getDataModule().getTempSet(Condition.equal("KEYDEST", key));
        vtprij.open();
		if (vtprij.rowCount() > 0) {
			findVezaNaPrimku();
			if (vezaNaPrimku.getRowCount() == 1) {
				vezaNaPrimku.setString("STATUS", "N");
				vezaNaPrimku.setString("VEZA", "");
				vezaNaPrimku.setBigDecimal("NC", Aus.zero2);
				vezaNaPrimku.setBigDecimal("VC", Aus.zero2);
				vezaNaPrimku.setBigDecimal("MC", Aus.zero2);
				vezaNaPrimku.setBigDecimal("ZC", Aus.zero2);
				vezaNaPrimku.setBigDecimal("INAB", Aus.zero2);
				vezaNaPrimku.setBigDecimal("IMAR", Aus.zero2);
				vezaNaPrimku.setBigDecimal("IBP", Aus.zero2);
				vezaNaPrimku.setBigDecimal("IPOR", Aus.zero2);
				vezaNaPrimku.setBigDecimal("ISP", Aus.zero2);
				vezaNaPrimku.setBigDecimal("IZAD", Aus.zero2);
				vezaNaPrimku.setBigDecimal("DIOPORMAR", Aus.zero2);
				vezaNaPrimku.setBigDecimal("DIOPORPOR", Aus.zero2);
				vezaNaPrimku.setBigDecimal("PORAV", Aus.zero2);
				vezaNaPrimku.setBigDecimal("SKOL", Aus.zero2);
				vezaNaPrimku.setBigDecimal("SVC", Aus.zero2);
				vezaNaPrimku.setBigDecimal("SMC", Aus.zero2);
			}
			vtprij.deleteRow();
		}
		return true;
	}

	public boolean locateSklad() {
		dm.getSklad().open();
		if (!lD.raLocate(dm.getSklad(), "CSKL", getDetailSet().getString(
				"CSKLART"))) {
		  JOptionPane.showMessageDialog(raDetail.getWindow(),
              "Ne mogu pronaæi slog skladišta! ("+
              getDetailSet().getString("CSKLART")+")", "Greška",
              JOptionPane.ERROR_MESSAGE);
          return false;
		}
		return true;
	}

	QueryDataSet findNewDobArt() {
		QueryDataSet ldobart = hr.restart.util.Util
				.getNewQueryDataSet("SELECT * FROM DOB_ART WHERE CPAR = "
						+ getMasterSet().getInt("CPAR") + " AND CART = "
						+ getDetailSet().getInt("CART"));
		if (ldobart.getRowCount() == 0) {
			ldobart.insertRow(false);
			ldobart.setInt("CPAR", getMasterSet().getInt("CPAR"));
			ldobart.setInt("CART", getDetailSet().getInt("CART"));
		}
		ldobart.setString("CART1", getDetailSet().getString("CART1"));
		ldobart.setString("BC", getDetailSet().getString("BC"));
		ldobart.setString("NAZART", getDetailSet().getString("NAZART"));
		ldobart.setBigDecimal("DC", getDetailSet().getBigDecimal("DC"));
		ldobart.setBigDecimal("PRAB", getDetailSet().getBigDecimal("PRAB"));
		return ldobart;
	}

	void mysaveDobArt() {
		if (kalfindDOBART()) {
			dm.getDob_art().insertRow(false);
		}
		dm.getDob_art().setInt("CPAR", getMasterSet().getInt("CPAR"));
		dm.getDob_art().setInt("CART", getDetailSet().getInt("CART"));
		dm.getDob_art().setString("CART1", getDetailSet().getString("CART1"));
		dm.getDob_art().setString("BC", getDetailSet().getString("BC"));
		dm.getDob_art().setString("NAZART", getDetailSet().getString("NAZART"));
		dm.getDob_art().setBigDecimal("DC", getDetailSet().getBigDecimal("DC"));
		dm.getDob_art().setBigDecimal("PRAB",
				getDetailSet().getBigDecimal("PRAB"));
		//		dm.getDob_art().post();
		//		dm.getDob_art().saveChanges();
	}

	public boolean kalfindDOBART() {

		//if (!dm.getDob_art().isOpen())
			dm.getDob_art().open();
		//dm.getDob_art().refresh();
		return hr.restart.util.lookupData.getlookupData().raLocate(
				dm.getDob_art(),
				new com.borland.dx.dataset.DataSet[] { getMasterSet(),
						getDetailSet() },
				new java.lang.String[] { "CPAR", "CART" },
				new java.lang.String[] { "CPAR", "CART" });
	}

	public boolean doWithSaveDetail(char mode) {
		if (getMasterSet().getString("CSHZT").equals("YES"))
			try {
				zts.saveChanges(mode);
			} catch (Exception e) {
				return false;
			}

		if (mode == 'I') {

			getDetailSet().setString("STATUS", "O");
			raTransaction.saveChanges(getDetailSet());
			findSTANJE();
			lc.TransferFromDB2Class(getDetailSet(), rKBDU.stavka);
			lc.TransferFromDB2Class(stanjeSet, rKBDU.stanje);
			rKBDU.vrzal = hr.restart.util.Util.getNewQueryDataSet(
					"select vrzal from sklad where cskl ='"
							+ getDetailSet().getString("CSKLART") + "'", true)
					.getString("VRZAL");

			rKBDU.updateStanje();
			lc.TransferFromClass2DB(stanjeSet, rKBDU.stanje);
			findVezaNaPrimku();
			if (vezaNaPrimku.getRowCount() == 1) {
				vezaNaPrimku.setBigDecimal("NC", getDetailSet().getBigDecimal(
						"NC"));
				vezaNaPrimku.setBigDecimal("VC", getDetailSet().getBigDecimal(
						"VC"));
				vezaNaPrimku.setBigDecimal("MC", getDetailSet().getBigDecimal(
						"MC"));
				vezaNaPrimku.setBigDecimal("ZC", getDetailSet().getBigDecimal(
						"ZC"));
				vezaNaPrimku.setBigDecimal("INAB", getDetailSet()
						.getBigDecimal("INAB"));
				vezaNaPrimku.setBigDecimal("IMAR", getDetailSet()
						.getBigDecimal("IMAR"));
				vezaNaPrimku.setBigDecimal("IBP", getDetailSet().getBigDecimal(
						"IBP"));
				vezaNaPrimku.setBigDecimal("IPOR", getDetailSet()
						.getBigDecimal("IPOR"));
				vezaNaPrimku.setBigDecimal("ISP", getDetailSet().getBigDecimal(
						"ISP"));
				vezaNaPrimku.setBigDecimal("IZAD", getDetailSet()
						.getBigDecimal("IZAD"));
				vezaNaPrimku.setBigDecimal("DIOPORMAR", getDetailSet()
						.getBigDecimal("DIOPORMAR"));
				vezaNaPrimku.setBigDecimal("DIOPORPOR", getDetailSet()
						.getBigDecimal("DIOPORPOR"));
				vezaNaPrimku.setBigDecimal("PORAV", getDetailSet()
						.getBigDecimal("PORAV"));
				vezaNaPrimku.setBigDecimal("SKOL", getDetailSet()
						.getBigDecimal("SKOL"));
				vezaNaPrimku.setBigDecimal("SVC", getDetailSet().getBigDecimal(
						"SVC"));
				vezaNaPrimku.setBigDecimal("SMC", getDetailSet().getBigDecimal(
						"SMC"));
			} else {
				System.err
						.println("Nisam našao stavku koju moram promjeniti ili ih je previše");
				System.out.println(vezaNaPrimku.getQuery().getQueryString());
				return false;
			}
			raTransaction.saveChanges(stanjeSet);
			raTransaction.saveChanges(vezaNaPrimku);
			raTransaction.saveChanges(getMasterSet());
			raTransaction.saveChanges(findNewDobArt());
		} else if (mode == 'B') {
			raTransaction.saveChanges(vtprij);
			if (vezaNaPrimku != null || vezaNaPrimku.getRowCount() != 0) {
				System.out.println("vezaNaPrimku.getRowCount() "
						+ vezaNaPrimku.getRowCount());
				raTransaction.saveChanges(vezaNaPrimku);
			}
			rKBDU.delStanje();
			lc.TransferFromClass2DB(stanjeSet, rKBDU.stanje);
			raTransaction.saveChanges(stanjeSet);
			raTransaction.saveChanges(getMasterSet());
			hr.restart.util.Valid.getValid().recountDataSet(raDetail, "RBR",
					delStavka, false);
			raTransaction.saveChanges(getDetailSet());
		}
		return true;
	}

	/*
	 * public boolean doWithSaveMaster(char mode) {
	 * 
	 * if (mode=='N') { try {
	 * getDetailSet().setInt("BRDOK",getMasterSet().getInt("BRDOK"));
	 * raTransaction.saveChanges(getDetailSet());
	 * raTransaction.saveChanges(stanje);
	 * jpMaster.jpBRDOKUL.getZaglavPRI().setString("STATUS","P");
	 * raTransaction.saveChanges(jpMaster.jpBRDOKUL.getZaglavPRI()); return
	 * true; } catch (Exception ex) { ex.printStackTrace(); return false; } }
	 * else if (mode=='I') { if (!super.doWithSaveMaster(mode)) return false; }
	 * return true; }
	 */
	public boolean ValidacijaPrijeIzlazaDetail() {

		for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet()
				.next()) {
			if (!getDetailSet().getString("STATUS").equalsIgnoreCase("O")) {
				javax.swing.JOptionPane.showMessageDialog(raMaster.getWindow(),
						"Nije iskalkulirana stavka br."
								+ getDetailSet().getShort("RBR"), "Sistem",
						JOptionPane.WARNING_MESSAGE, null);
				return false;
			}
		}
		if (!super.ValidacijaPrijeIzlazaDetail())
			return false;
		if (!getMasterSet().getString("CSHZT").equals("YES"))
			return true;
		zts.kill();
		return checkZavtr();
	}
}

abstract class JraDialogPrimke extends JraDialog {
	raTwoTableChooser rtc;

	OKpanel okp;

	public void setLeftSet(StorageDataSet ds) {
		rtc.setLeftDataSet(ds);
	}

	public void setRightSet(StorageDataSet ds) {
		rtc.setRightDataSet(ds);
	}

	public StorageDataSet getRightSet() {
		return rtc.getRightDataSet();
	}

	public JraDialogPrimke(java.awt.Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		init();
	}

	abstract public void okPress();

	abstract public void cancelPress();

	public void init() {
		rtc = new raTwoTableChooser();
		okp = new OKpanel() {
			public void jBOK_actionPerformed() {
				okPress();
			}

			public void jPrekid_actionPerformed() {
				cancelPress();
			}
		};
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(rtc, BorderLayout.CENTER);
		this.getContentPane().add(okp, BorderLayout.SOUTH);
	}

	public void initialize() {
		rtc.initialize();
	}
}