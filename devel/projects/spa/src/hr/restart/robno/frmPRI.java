/****license*****************************************************************
**   file: frmPRI.java
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

import hr.restart.util.Aus;
import hr.restart.util.raTransaction;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;

/**
 * <p>
 * Title: Robno poslovanje
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2000
 * </p>
 * <p>
 * Company: REST-ART
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class frmPRI extends hr.restart.util.raMasterDetail {

	private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass
			.getraCommonClass();

	private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	private hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
	private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

	private jpMasterPanelPRI jpMaster = new jpMasterPanelPRI();

	private jpDetailPanelPRI jpDetail;

	private hr.restart.robno.jpPreselectDoc jpp;

	private String masterTitle = "Primke";

	private String detailTitle = "Stavke primke";

	private String vrDok = "PRI";

	private String srcString;

	private Integer Broj;

	private String oldGOD;

	private int oldCART;

	private java.math.BigDecimal oldKOLBRIS;

	private short delStavka;

	hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

	private QueryDataSet setstanje = null;

	//	final private java.sql.PreparedStatement insertStanje = raTransaction
	//			.getPreparedStatement("INSERT INTO Stanje(cskl,cart,god,kolul,kolmat,kol)
	// Values (?,?,?,?,?,?)");

	public void zamraci(DataSet ds) {
		ds.getColumn("DC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("DC_VAL").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IDOB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IDOB_VAL").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PRAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IRAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PZT").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IZT").setVisible(TriStateProperty.FALSE);
		ds.getColumn("NC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PMAR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("MAR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("VC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR1").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR2").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR3").setVisible(TriStateProperty.FALSE);
		ds.getColumn("MC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("INAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IMAR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IBP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IPOR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("ISP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("ZC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IZAD").setVisible(TriStateProperty.FALSE);
		ds.getColumn("KOLFLH").setVisible(TriStateProperty.FALSE);
		ds.getColumn("SKOL").setVisible(TriStateProperty.FALSE);
		ds.getColumn("SVC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("SMC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("DIOPORMAR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("DIOPORPOR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PORAV").setVisible(TriStateProperty.FALSE);
		ds.getColumn("STATUS").setVisible(TriStateProperty.FALSE);
		ds.getColumn("SKAL").setVisible(TriStateProperty.FALSE);
		ds.getColumn("RBSID").setVisible(TriStateProperty.FALSE);
		ds.getColumn("VEZA").setVisible(TriStateProperty.FALSE);
		ds.getColumn("id_stavka").setVisible(TriStateProperty.FALSE);

	}

	public frmPRI() {
		super(1, 3); //ne radi iz nekog \u010Dudnog razloga
		jpDetail = new jpDetailPanelPRI(this);
		jpp = presPRI.getPres();
		this.setUserCheck(true);
		this.raMaster.getJpTableView().addTableModifier(
				new hr.restart.swing.raTableColumnModifier("CORG",
						new String[] { "CORG", "NAZIV" }, dm
								.getAllOrgstruktura()));
		this.raMaster.getJpTableView().addTableModifier(
				new hr.restart.swing.raTableColumnModifier("CPAR",
						new String[] { "CPAR", "NAZPAR" }, dm.getPartneri()));
		zamraci(dm.getStdokuPRI());
		setMasterSet(dm.getDokuPRI());
		setDetailSet(dm.getStdokuPRI());
		setJPanelMaster(jpMaster);
		setJPanelDetail(jpDetail);

		setMasterKey(Util.mkey);
		setDetailKey(Util.dkey);
		setVisibleColsMaster(new int[] { 4, 5, 6, 13, 14 });
		//    setVisibleColsMaster(new int[] {4,5,6,8,9});
		setVisibleColsDetail(new int[] { 4,
				Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 10 }); //,32,33
		set_kum_detail(false);
		jpMaster.setDataSet(getMasterSet());
		setNaslovMaster(masterTitle);
		setNaslovDetail(detailTitle);
		raDetail.pack(); // repPrkProvider

	    raMaster.getRepRunner().addReport("hr.restart.robno.repPrkKol","hr.restart.robno.repPrkProvider","PriKalk", "Primka");
	    raDetail.getRepRunner().addReport("hr.restart.robno.repPrkKol","hr.restart.robno.repPrkProvider","PriKalk", "Primka");
	    
//		raMaster.getRepRunner().addReport("hr.restart.robno.repPriKalk",
//				"Primka", 2);
//		raDetail.getRepRunner().addReport("hr.restart.robno.repPriKalk",
//				"Primka", 2);

	}

	public void beforeShowMaster() {
		setNaslovMaster(masterTitle);
	}

	public void beforeShowDetail() {
		jpDetail.setDataSet(getDetailSet(), getMasterSet());
		setNaslovDetail((detailTitle.concat(" br. ")
				+ getMasterSet().getString("VRDOK") + "-"
				+ getMasterSet().getString("CSKL").trim() + "/"
				+ getMasterSet().getString("GOD") + "-" + val.maskZeroInteger(
				new Integer(getMasterSet().getInt("BRDOK")), 6)));
	}

	public void SetFokusMaster(char mode) {

		if (mode == 'N') {
			jpp.copySelValues();
			getMasterSet().setTimestamp("DATDOK", val.getPresToday(jpp.getSelRow()));
			getMasterSet().setString("GOD",
					val.findYear(getMasterSet().getTimestamp("DATDOK")));
		}
		jpMaster.initPanel(mode);
	}

	public void SetFokusDetail(char mode) {

		if (mode == 'N') {
			jpDetail.prepareNew();
			getDetailSet().setShort(
					"RBR",
					Rbr.getRbr().vrati_rbr("STDOKU",
							getMasterSet().getString("CSKL"),
							getMasterSet().getString("VRDOK"),
							getMasterSet().getString("GOD"),
							getMasterSet().getInt("BRDOK")));
		} else if (mode == 'I') {
			jpDetail.prepareChange();
		}
	}

	public boolean isKalkulStavke() {
		return hr.restart.util.Util.getNewQueryDataSet(
				"SELECT count(*) AS BROJAC FROM STDOKU WHERE " + "CSKL='"
						+ getMasterSet().getString("CSKL") + "' AND "
						+ "VRDOK='" + getMasterSet().getString("VRDOK")
						+ "' AND " + "GOD='" + getMasterSet().getString("GOD")
						+ "' AND " + "BRDOK=" + getMasterSet().getInt("BRDOK"),
				true).getInt("BROJAC") != 0;
	}

	public boolean ValidacijaMaster(char mode) {
		/*if (mode == 'N') {
			setNewNoDocumentinMaster();
		} else*/
    if (val.isEmpty(jpMaster.jrfCPAR)) return false;		
/*    if (mode == 'I') {

    	
			if (isKalkulStavke()) {
				JOptionPane
						.showConfirmDialog(
								null,
								"Postoje iskalkulirane primke za ovaj dokument ! Nedozvoljena izmjena.",
								"Greška", JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE);
				return false;
			}

		}*/
		/*
		 * else if (mode=='B'){ if (isKalkulStavke()){
		 * 
		 * return false; } }
		 */
		return (super.ValidacijaMaster(mode));
	}
	
	public boolean isPriKalk(){
		return !ut.getNewQueryDataSet(	
		"SELECT status FROM STDOKU WHERE CSKL='"
		+ getMasterSet().getString("CSKL") + "' AND "
		+ "VRDOK='" + getMasterSet().getString("VRDOK")
		+ "' AND " + "GOD='" + getMasterSet().getString("GOD")
		+ "' AND " + "BRDOK=" + getMasterSet().getInt("BRDOK")
		+ " AND RBSID =" + getDetailSet().getInt("RBSID")).getString("STATUS").equalsIgnoreCase("N");
	}
	

	public boolean ValidacijaDetail(char mode) {

		if (mode == 'I' && isPriKalk()) {
//				&& !getDetailSet().getString("STATUS").equalsIgnoreCase("N")) {
			JOptionPane
					.showConfirmDialog(
							null,
							"Stavka primke je veæ iskalkulirana ! Nedozvoljena izmjena.",
							"Greška", JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
			return false;
		}
/*		
		if (mode == 'B'
				&& !getDetailSet().getString("STATUS").equalsIgnoreCase("N")) {
			JOptionPane
					.showConfirmDialog(
							null,
							"Stavka primke je veæ iskalkulirana ! Nedozvoljeno brisanje.",
							"Greška", JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
			return false;
		}
*/
		if (val.isEmpty(jpDetail.jtfKOL))
			return false;
		//    if (!dlgSerBrojevi.getdlgSerBrojevi().findSB(jpDetail.rpcart,
		// getDetailSet(), 'U', mode)) {
		//      return false;
		//    }
		return super.ValidacijaDetail(mode);
	}

	public boolean ValDPEscapeDetail(char mode) {
		if (mode == 'N') {
			if (jpDetail.rpcart.getCART().trim().equals("")) {
				return true;
			} else {
				jpDetail.rpcart.EnabDisab(true);
		        jpDetail.rpcart.setCART();
		        jpDetail.rpcart.SetDefFocus();
				rcc.setLabelLaF(jpDetail.jtfKOL, false);
				return false;
			}
		} else {
//			jpDetail.rpcart.EnabDisab(true);
//	        jpDetail.rpcart.setCART();
//	        jpDetail.rpcart.SetDefFocus();
			rcc.setLabelLaF(jpDetail.jtfKOL, false);
			return true;
		}
	}

	public void forIspis() {
		//    raDetail.getRepRunner().addReport("hr.restart.robno.repPocetnoStanje","Poèetno
		// stanje - kolièine",2);
		//    raDetail.getRepRunner().addReport("hr.restart.robno.repPocetnoStanjeExtendedVersion","Poèetno
		// stanje - vrijednosti",2);
		//    raDetail.getRepRunner().addReport("hr.restart.robno.repPocetnoStanjeMegablastVersion","Poèetno
		// stanje - kalkulacije",2);
		//    raMaster.getRepRunner().addReport("hr.restart.robno.repPocetnoStanje","Poèetno
		// stanje - kolièine",2);
		//    raMaster.getRepRunner().addReport("hr.restart.robno.repPocetnoStanjeExtendedVersion","Poèetno
		// stanje - vrijednosti",2);
		//    raMaster.getRepRunner().addReport("hr.restart.robno.repPocetnoStanjeMegablastVersion","Poèetno
		// stanje - kalkulacije",2);

	}

	public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent ne) {
		jpDetail.oldKOL = getDetailSet().getBigDecimal("KOL");
	}

/*	public void setNewNoDocumentinMaster() {

		Broj = val.findSeqInteger(getMasterSet().getString("CSKL")
				+ getMasterSet().getString("VRDOK")
				+ getMasterSet().getString("GOD"));
		getMasterSet().setInt("BRDOK", Broj.intValue());
	} */

	public boolean DeleteCheckDetail() {
		delStavka = getDetailSet().getShort("RBR");
		oldCART = getDetailSet().getInt("CART");
		oldKOLBRIS = getDetailSet().getBigDecimal("KOL");
//		if (!getDetailSet().getString("STATUS").equalsIgnoreCase("N")) {
		if (isPriKalk()) {
			JOptionPane
					.showConfirmDialog(
							null,
							"Primka je ve\u0107 iskalkulirana ! Nedozvoljeno brisanje.",
							"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public void addKolSkladUL(BigDecimal oldvalue, BigDecimal newvalue,
			QueryDataSet st) {
		st.setBigDecimal("KOLSKLADUL", st.getBigDecimal("KOLSKLADUL").add(
				newvalue).subtract(oldvalue).setScale(3,
				BigDecimal.ROUND_HALF_UP));
		st.setBigDecimal("KOLSKLAD", st.getBigDecimal("KOLSKLADUL").subtract(
				st.getBigDecimal("KOLSKLADIZ")).setScale(3,
				BigDecimal.ROUND_HALF_UP));
	}

	public void stanjeChange(char mode) {
		if (mode == 'B') {
			setstanje = hr.restart.util.Util
					.getNewQueryDataSet("SELECT * FROM STANJE WHERE CSKL='"
							+ getMasterSet().getString("CSKL") + "' AND GOD='"
							+ getMasterSet().getString("GOD") + "' AND CART="
							+ oldCART);
			System.out.println("jpDetail.oldKOL " + oldKOLBRIS);
			addKolSkladUL(oldKOLBRIS, Aus.zero2, setstanje);
			//System.out.println("stanjeChange mode="+mode);
			return;
		}

		//System.out.println("produzi stanjeChange mode="+mode);

		setstanje = hr.restart.util.Util
				.getNewQueryDataSet("SELECT * FROM STANJE WHERE CSKL='"
						+ getDetailSet().getString("CSKL") + "' AND GOD='"
						+ getDetailSet().getString("GOD") + "' AND CART="
						+ getDetailSet().getInt("CART"));
		//System.out.println(setstanje.getQuery().getQueryString());

		if (mode == 'N') {
			if (setstanje.getRowCount() == 0) {
				setstanje.insertRow(false);
				setstanje.setString("CSKL", getMasterSet().getString("CSKL"));
				setstanje.setString("GOD", getMasterSet().getString("GOD"));
				setstanje.setInt("CART", getDetailSet().getInt("CART"));
				setstanje.setBigDecimal("KOLSKLADPS", Aus.zero2);
				setstanje.setBigDecimal("KOLSKLADUL", Aus.zero2);
				setstanje.setBigDecimal("KOLSKLADIZ", Aus.zero2);
				setstanje.setBigDecimal("KOLSKLAD", Aus.zero2);
			}
			addKolSkladUL(Aus.zero2, getDetailSet().getBigDecimal(
					"KOL"), setstanje);
		} else if (mode == 'I') {
			addKolSkladUL(jpDetail.oldKOL, getDetailSet().getBigDecimal("KOL"),
					setstanje);
		}

	}
  
  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') {
      Util.getUtil().getBrojDokumenta(getMasterSet());
    }
    return true;
  }

	public boolean doBeforeSaveDetail(char mode) {
		stanjeChange(mode);
		return true;
	}

	public boolean doWithSaveDetail(char mode) {
		if (mode == 'N') {
			getDetailSet().setInt("RBSID",
					Rbr.getRbr().getRbsID(getDetailSet()));
			getDetailSet().setString(
					"ID_STAVKA",
					raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
							"vrdok", "god", "brdok", "rbsid" }, "stdoku"));
			raTransaction.saveChanges(getDetailSet());
		} else if (mode == 'B') {
			hr.restart.util.Valid.getValid().recountDataSet(raDetail, "RBR",
					delStavka, false);
			raTransaction.saveChanges(getDetailSet());
		}

		if (setstanje.getRowCount() != 0) {
			System.out.println(" u duwidsave "
					+ setstanje.getQuery().getQueryString());
			ST.prn(setstanje);

			raTransaction.saveChanges(setstanje);
		}

		return true;
	}

	public boolean DeleteCheckMaster() {
		if (!getMasterSet().getString("STATUS").equalsIgnoreCase("N")) {
			JOptionPane.showConfirmDialog(null,
					"Dokument je ve\u0107 obra\u0111en ! Nemogu\u0107a akcija",
					"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		srcString =  Util.getUtil().getSeqString(getMasterSet());
		if (getDetailSet().rowCount() > 0) {
			JOptionPane.showConfirmDialog(null,
					"Nisu pobrisane stavke dokumenta !", "Gre\u0161ka",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!Util.getUtil().checkSeq(srcString,
				Integer.toString(getMasterSet().getInt("BRDOK")))) {
			return false;
		}
		return true;
	}

	public boolean doWithSaveMaster(char mode) {
		boolean retValue = true;
		if (mode == 'N') {
		} else if (mode == 'I') {
		} else if (mode == 'B') { // Brisanje mastera
			try {
				Util.getUtil().delSeq(srcString, true);
			} catch (Exception ex) {
				ex.printStackTrace();
				retValue = false;
			}
		}
		return retValue;
	}

	public boolean checkAddEnabled() {
		return !Aut.getAut().isWrongKnjigYear(this, true);
	}

	public boolean checkAccess() {

		if (getMasterSet().getString("STATUS").equalsIgnoreCase("K")) {
			setUserCheckMsg(
					"Korisnik ne može promijeniti dokument jer je proknjižen !",
					false);
			return false;
		}
		if (!checkValid())
			return false;
		if (Aut.getAut().isWrongKnjigYear(this))
			return false;
		if (isKPR()) {
			setUserCheckMsg(
					"Dokument je ušao u knjigu popisa i ne smije se mijenjati !!!",
					false);
			return false;
		}
		restoreUserCheckMessage();
		return true;
	}

	public boolean checkValid() {
		if (getMasterSet().getString("STATUS").equalsIgnoreCase("P")) {
			setUserCheckMsg(
					"Korisnik ne može promijeniti dokument jer je za njega ve\u0107 napravljena kalkulacija !",
					false);
			return false;
		}
		return true;
	}

	public String PrepSql(boolean detail) {
		raDateUtil rdu = raDateUtil.getraDateUtil();
		String sqldodat = "";
		String tabname = "doku.";
		try {

			if (detail) {
				sqldodat = "and "+tabname+"cskl='" + jpp.getSelRow().getString("CSKL")
						+ "' and "+tabname+"vrdok='" + jpp.getSelRow().getString("VRDOK")
						+ "' " + "and "+tabname+"brdok = '"
						+ getMasterSet().getInt("BRDOK") + "' and "+tabname+"god = '"
						+ getMasterSet().getString("GOD") + "'";
			} else {
				if (!jpp.getSelRow().getString("CSKL").equals(""))
					sqldodat = "and "+tabname+"cskl='" + jpp.getSelRow().getString("CSKL")
							+ "' ";
				if (!jpp.getSelRow().getString("VRDOK").equals(""))
					sqldodat = sqldodat + "and "+tabname+"vrdok='"
							+ jpp.getSelRow().getString("VRDOK") + "' ";
				if (jpp.getSelRow().getInt("CPAR") != 0)
					sqldodat = sqldodat + "and "+tabname+"cpar="
							+ jpp.getSelRow().getInt("CPAR") + " ";
				if (!jpp.getSelRow().getTimestamp("DATDOK-from").equals("")) {
					sqldodat = sqldodat
							+ "and "+tabname+"datdok >= '"
							+ rdu.PrepDate(jpp.getSelRow().getTimestamp(
									"DATDOK-from"), true) + "' ";
				}
				if (!jpp.getSelRow().getTimestamp("DATDOK-to").equals("")) {
					sqldodat = sqldodat
							+ "and "+tabname+"datdok <= '"
							+ rdu.PrepDate(jpp.getSelRow().getTimestamp(
									"DATDOK-to"), false) + "' ";
				}
			}
		} catch (Exception exc) {
			sqldodat = "and "+tabname+"cskl='" + getMasterSet().getString("CSKL")
					+ "' and "+tabname+"vrdok='" + getMasterSet().getString("VRDOK")
					+ "' and "+tabname+"god='" + getMasterSet().getString("GOD")
					+ "' and "+tabname+"brdok = '" + getMasterSet().getInt("BRDOK")
					+ "' and "+tabname+"god = '" + getMasterSet().getString("GOD") + "'";
		}
		return sqldodat;
	}

	public void Funkcija_ispisa_master() {
		reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true), vrDok);
		super.Funkcija_ispisa_master();
	}

	public void Funkcija_ispisa_detail() {
		reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true), vrDok);
		super.Funkcija_ispisa_detail();
	}

	public boolean isKPR() {
		return getMasterSet().getString("STAT_KPR").equalsIgnoreCase("D");
	}
	/*
	 * public void EntryDetail(char mode) { System.out.println("Unisho"); if
	 * (mode == 'N') { oldKOLBRIS = Aus.zero2; } else if (mode ==
	 * 'I') { oldKOLBRIS = getDetailSet().getBigDecimal("KOL"); } }
	 */
}