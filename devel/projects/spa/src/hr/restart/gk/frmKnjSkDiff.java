/****license*****************************************************************
**   file: frmKnjSkDiff.java
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
package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.baza.Pokriveni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.sk.jpSelKonto;
import hr.restart.sk.raSaldaKonti;
import hr.restart.sk.raVrdokMatcher;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.util.Aus;
import hr.restart.util.raCommonClass;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.Tecajevi;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKnjSkDiff extends frmKnjizenje {

	raCommonClass rcc = raCommonClass.getraCommonClass();
	
	jpCpar jpp = new jpCpar(100, 265, true) {
    public void afterLookUp(boolean succ) {
    }
    protected void kupSelected() {
      super.kupSelected();
      if (raSaldaKonti.isDirect()) checkKupDob(true);
    }
    protected void dobSelected() {
      super.dobSelected();
      if (raSaldaKonti.isDirect()) checkKupDob(false);
    }
  };
  
  jpSelKonto jpk = new jpSelKonto(100, 265, true) {
    public void afterLookUp(boolean succ) {
      afterKonto(succ);
    }
  };
  
  jpSelKonto jpid = new jpSelKonto(100, 265, false);
  jpSelKonto jpip = new jpSelKonto(100, 265, false);
  
  JraTextField jraMaxSal = new JraTextField();
	
	public frmKnjSkDiff() {
		dataSet.addColumn(dM.createStringColumn("BROJKONTA", "Konto", 8));
		dataSet.addColumn(dM.createIntColumn("CPAR", "Partner"));
		dataSet.addColumn(dM.createBigDecimalColumn("MAXSAL", "Maksimalni saldo", 2));
				
		jpk.bind(dataSet);
		jpp.bind(dataSet);
		
		jpid.bindOwn("Protukonto duguje");
		jpip.bindOwn("Protukonto potražuje");
		
		jraMaxSal.setDataSet(dataSet);
		jraMaxSal.setColumnName("MAXSAL");
		
		JPanel ap = new JPanel(new XYLayout(540, 140));
		ap.add(jpk, new XYConstraints(0, 5, -1, -1));
		ap.add(jpp, new XYConstraints(0, 30, -1, -1));
		ap.add(jpid, new XYConstraints(0, 60, -1, -1));
		ap.add(jpip, new XYConstraints(0, 85, -1, -1));
		ap.add(new JLabel("Maksimalni saldo"), new XYConstraints(15, 110, -1, -1));
		ap.add(jraMaxSal, new XYConstraints(150, 110, 100, -1));
		
		this.jp.add(ap,BorderLayout.CENTER);
	}
	
	public boolean Validacija() {
    if (vl.isEmpty(jraMaxSal)) return false;
    if (dataSet.getBigDecimal("MAXSAL").signum() <= 0) {
    	jraMaxSal.requestFocus();
    	JOptionPane.showMessageDialog(this, "Pogrešan maksimalni iznos salda stavke!",
    			"Greška", JOptionPane.ERROR_MESSAGE);
    	return false;
    }
    if (jpid.getKonto().length() == 0 && jpip.getKonto().length() == 0) {
    	jpid.focusKonto();
    	JOptionPane.showMessageDialog(this, "Potrebno je unijeti konto bar jedne od strana!",
    			"Greška", JOptionPane.ERROR_MESSAGE);
    	return false;
    }
    return true;
  }
	
	protected void afterKonto(boolean succ) {
    DataRow konto = !succ ? null : jpk.getKontoRow();
    if (konto != null) {
      if (konto.getString("SALDAK").equalsIgnoreCase("D") && jpp.isKupci()) 
        jpp.setKupci(false);
      else if (konto.getString("SALDAK").equalsIgnoreCase("K") && !jpp.isKupci())
        jpp.setKupci(true);
    }
    if (succ) jpp.focusCparLater();
  }
    
  void checkKupDob(boolean kupac) {
    DataRow konto = jpk.getKontoRow();
    if (konto != null) {
      if (konto.getString("SALDAK").equalsIgnoreCase("D") && jpp.isKupci())
        jpk.clear();
      else if (konto.getString("SALDAK").equalsIgnoreCase("K") && !jpp.isKupci())
        jpk.clear();
    }
  }
  
  Collection skstav = null;
  public boolean okPress() throws Exception {
  	BigDecimal ms = dataSet.getBigDecimal("MAXSAL");
  	String dkon = jpid.getKonto();
  	String pkon = jpip.getKonto();
  	
  	Condition cond = jpk.getCondition().and(jpp.getCondition()).
  			and(Aus.getVrdokCond(jpp.isKupci())).and(Aus.getKnjigCond()).
  			and(Aus.getCurrGKDatumCond("DATUMKNJ", dataSet.getTimestamp("DATUMDO"))).
  			and(Condition.between("SALDO", ms.negate(), ms)).and(Condition.diff("SALDO", 0));
  	
  	String query = 
		  	"SELECT cpar, brojkonta, corg, id, ip, saldo, " +
		  	"oznval, cskstavke FROM skstavke WHERE " + cond;
  	System.out.println("query: "+query);
  	StorageDataSet data = Skstavke.getDataModule().getScopedSet(
  			"CPAR BROJKONTA CORG ID IP SALDO OZNVAL CSKSTAVKE");
  	ut.fillReadonlyData(data, query);
  	
  	List st = new ArrayList();
  	
  	for (data.first(); data.inBounds(); data.next())
  		if (raSaldaKonti.isDomVal(data))
  			st.add(new LinkData(data));

  	if (st.isEmpty()) {
      getKnjizenje().setErrorMessage("Nema podataka za knjiženje");
      throw new Exception("Nema podataka za knjiženje");
    }
  	
  	Map sks = new HashMap();
  	Map fins = new HashMap();
  	for (Iterator i = st.iterator(); i.hasNext(); ) {
  		LinkData ld = (LinkData) i.next();
  		String skey = skKey(ld);
  		String fkey = finKey(ld);
  		
  		Total tsk = (Total) sks.get(skey);
  		if (tsk != null) tsk.add(ld);
  		else sks.put(skey, new Total(ld));
  		
  		Total tfin = (Total) fins.get(fkey);
  		if (tfin != null) tfin.add(ld);
  		else fins.put(fkey, new Total(ld));
  	}
  	
  	if (!getKnjizenje().startKnjizenje(this)) return false;
    getKnjizenje().setSKRacKnj(false);
    getKnjizenje().setInfoKeys(null);
    
    skstav = sks.values();
    StorageDataSet sds = null;
    for (Iterator i = skstav.iterator(); i.hasNext(); ) {
    	Total tsk = (Total) i.next();
    	if (tsk.saldo.signum() == 0) continue;
    	
    	sds = getKnjizenje().getNewStavka(tsk.konto, tsk.corg);
    	if (sds != null) {
    		if (tsk.saldo.signum() > 0) {
    			getKnjizenje().setID(Aus.zero2);
    			getKnjizenje().setIP(tsk.saldo);
    		} else {
    			getKnjizenje().setID(tsk.saldo.negate());
    			getKnjizenje().setIP(Aus.zero2);
    		}
    		
    		sds.setTimestamp("DATDOK", dataSet.getTimestamp("DATUMKNJ"));
    		String cgk = getKnjizenje().getFNalozi().jpMaster.jpBrNal.getCNaloga() + 
    					"-"	+ getKnjizenje().getFNalozi().jpDetail.jpBrNal.rbs;
    		sds.setString("BROJDOK", tsk.cgk = cgk);
    		sds.setInt("CPAR", tsk.cpar);
    		sds.setString("VRDOK", jpp.isKupci() ? "OKK" : "OKD");
    		sds.setString("OPIS", "Razlike u plaæanju");
    		sds.setString("OZNVAL", Tecajevi.getDomOZNVAL());
    		sds.setBigDecimal("TECAJ", Aus.one0);
    		if (!getKnjizenje().saveStavka()) return false;
    	}
    }
    
    for (Iterator i = fins.values().iterator(); i.hasNext(); ) {
    	Total tfin = (Total) i.next();
    	if (tfin.saldo.signum() == 0) continue;
    	
    	String konto = (tfin.saldo.signum() > 0 && dkon.length() > 0) || pkon.length() == 0 ? dkon : pkon;
    	
    	sds = getKnjizenje().getNewStavka(konto, tfin.corg);
    	if (sds != null) {
    		BigDecimal id = Aus.zero0;
    		BigDecimal ip = Aus.zero0;
    		if (tfin.saldo.signum() > 0) {
    			if (dkon.length() > 0) id = tfin.saldo;
    			else ip = tfin.saldo.negate();
    		} else {
    			if (pkon.length() > 0) ip = tfin.saldo.negate();
    			else id = tfin.saldo;
    		}
    		getKnjizenje().setID(id);
    		getKnjizenje().setIP(ip);
    		sds.setTimestamp("DATDOK", dataSet.getTimestamp("DATUMKNJ"));
    		sds.setString("OPIS", "Razlike u plaæanju");
    		if (!getKnjizenje().saveStavka()) return false;
    	}
    }
  	return getKnjizenje().saveAll();
  }
  
  /*
   * Proknjizavanje temeljnice. SK dio se hendla posve ruèno, zbog specifiènog posla.
   */
  public boolean commitTransfer() {
  	raVrdokMatcher vm = new raVrdokMatcher();
  	QueryDataSet sk = Skstavke.getDataModule().getTempSet(Condition.nil);
  	sk.open();
  	QueryDataSet pok = Pokriveni.getDataModule().getTempSet(Condition.nil);
  	pok.open();
  	for (Iterator i = skstav.iterator(); i.hasNext(); ) {
  		Total tsk = (Total) i.next();
    	if (tsk.saldo.signum() == 0) continue;
    	
    	sk.insertRow(false);
    	sk.setString("KNJIG", OrgStr.getKNJCORG(false));
    	sk.setString("VRDOK", jpp.isKupci() ? "OKK" : "OKD");
    	sk.setInt("CPAR", tsk.cpar);
    	sk.setString("BROJKONTA", tsk.konto);
    	sk.setString("BROJDOK", tsk.cgk);
    	sk.setTimestamp("DATDOK", dataSet.getTimestamp("DATUMKNJ"));
    	sk.setTimestamp("DATUMKNJ", dataSet.getTimestamp("DATUMKNJ"));
    	sk.setInt("BROJIZV", 0);
    	sk.setString("CORG", tsk.corg);
    	sk.setString("OZNVAL", Tecajevi.getDomOZNVAL());
    	sk.setBigDecimal("TECAJ", Aus.one0);
    	if (tsk.saldo.signum() > 0) {
    		sk.setBigDecimal("ID", Aus.zero2);
    		sk.setBigDecimal("IP", tsk.saldo);
    	} else {
    		sk.setBigDecimal("ID", tsk.saldo.negate());
    		sk.setBigDecimal("IP", Aus.zero2);
    	}
    	sk.setBigDecimal("SALDO", tsk.saldo.abs());
    	sk.setBigDecimal("SSALDO", sk.getBigDecimal("SALDO"));
    	sk.setBigDecimal("PVID", sk.getBigDecimal("ID"));
    	sk.setBigDecimal("PVIP", sk.getBigDecimal("IP"));
    	sk.setBigDecimal("PVSALDO", sk.getBigDecimal("SALDO"));
    	sk.setBigDecimal("PVSSALDO", sk.getBigDecimal("SSALDO"));
    	sk.setString("CSKSTAVKE", raSaldaKonti.findCSK(sk));
    	sk.setString("CGKSTAVKE", tsk.cgk);
    	
    	QueryDataSet psk = Skstavke.getDataModule().getTempSet(
      		Condition.in("CSKSTAVKE", tsk.links.toArray()));
    	psk.open();
    	if (psk.rowCount() != tsk.links.size()) {
    		System.out.println("Dokument broj "+tsk.cgk+" od partnera "+
            String.valueOf(tsk.cpar) + " je pokriven s nepostojeæim dokumentom");
    		return false;
    	}
    	for (psk.first(); psk.inBounds(); psk.next()) {
    		BigDecimal sal = psk.getBigDecimal("SALDO");
    		
    		vm.setStavka(psk);
    		if (vm.getMatchSide().equals("cracuna") != vm.isRacunTip())
    			sal = sal.negate();
    		
    		raSaldaKonti.matchIznos(psk, sk, pok, sal);
    	}
    	if (sk.getBigDecimal("SALDO").signum() != 0) {
    		System.out.println("Dokument broj "+tsk.cgk+" od partnera "+
            String.valueOf(tsk.cpar) + " ne saldira");
    		return false;
    	}
    	raTransaction.saveChanges(psk);
  	}
  	
  	raTransaction.saveChanges(sk);
  	raTransaction.saveChanges(pok);
  	return true;
  }
  
  private String skKey(LinkData ld) {
  	return ld.konto + "-" + ld.cpar;
  }
  
  private String finKey(LinkData ld) {
  	return ld.corg;
  }
  
  static class LinkData {
  	String corg, konto, cskstavke;
  	int cpar;
  	BigDecimal id, ip;
  	
  	public LinkData(DataSet row) {
  		cskstavke = row.getString("CSKSTAVKE");
  		konto = row.getString("BROJKONTA");
  		corg = row.getString("CORG");
  		cpar = row.getInt("CPAR");
  		id = ip = Aus.zero2;
  		if (row.getBigDecimal("ID").signum() != 0)
  			id = row.getBigDecimal("SALDO");
  		else ip = row.getBigDecimal("SALDO");
  	}
  }
  
  static class Total {
  	List links = new ArrayList();
  	int cpar;
  	String corg, konto, cgk;
  	BigDecimal saldo = Aus.zero2;
  	
  	public Total(LinkData ld) {
  		cpar = ld.cpar;
  		corg = ld.corg;
  		konto = ld.konto;
  		add(ld);
  	}
  	
  	public void add(LinkData ld) {
  		links.add(ld.cskstavke);
  		saldo = saldo.add(ld.id).subtract(ld.ip);
  	}
  }
}
