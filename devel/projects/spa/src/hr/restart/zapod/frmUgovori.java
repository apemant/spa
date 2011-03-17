/****license*****************************************************************
**   file: frmUgovori.java
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
package hr.restart.zapod;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Partneri;
import hr.restart.baza.Porezi;
import hr.restart.baza.Ugovori;
import hr.restart.baza.stugovor;
import hr.restart.baza.zirorn;
import hr.restart.robno.Aut;
import hr.restart.robno.frmDodatniTxt;
import hr.restart.robno.raControlDocs;
import hr.restart.robno.raVart;
import hr.restart.robno.rapancart;
import hr.restart.swing.AktivColorModifier;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.JraTextMultyKolField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.MathEvaluator;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
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

public class frmUgovori extends raMasterDetail {

	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	hr.restart.util.raCommonClass rCC = hr.restart.util.raCommonClass
			.getraCommonClass();

	hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();

	hr.restart.util.lookupData lD = hr.restart.util.lookupData.getlookupData();

	private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

	private QueryDataSet pjed = new QueryDataSet();
	private QueryDataSet vtt = null;
	

	String defNacpl = hr.restart.sisfun.frmParam.getParam("robno", "defNacpl",
			"", "Predefinirani naèin plaæanja");

	String defNacotp = hr.restart.sisfun.frmParam.getParam("robno",
			"defNacotp", "", "Predefinirani naèin otpreme robe");

	String defNamjena = hr.restart.sisfun.frmParam.getParam("robno",
			"defNamjena", "", "Predefinirana namjena robe");

	String franka = hr.restart.sisfun.frmParam.getParam("robno", "defFranka",
			"", "Predefinirana šifra frankature");

	boolean inSetFocus = true;

	UgovoriMasterPanel ump;

	UgovoriDetailPanel udp;

	short delRbr = -1;
	
	raNavAction rnvRbr = new raNavAction("Resortiranje rbr", raImages.IMGPREFERENCES,
			java.awt.event.KeyEvent.VK_F12) {
		public void actionPerformed(ActionEvent e) {
			
			if (getDetailSet().getRowCount()<2){
				JOptionPane
				.showConfirmDialog(
						raDetail,"Moraju postojati barem dvije stavke \nza sortiranje !",
						"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
						JOptionPane.ERROR_MESSAGE);
		return;
				
			}
			
			Object[] options = { "Da", "Ne" };

			int res = JOptionPane.showOptionDialog(raDetail,
					"Želite li presortirati ugovor prema \nredoslijedu u "+
					"grupama artikla ?",
					"Upit", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
					null, options, options[0]);

			if (res == 0) {
				retumbanje();
			}

			
		}
	};
	
	private void retumbanje(){
		String sql ="select stugovor.cart,stugovor.rbr,stugovor.rbsid,grupart.ssort from "+
		"stugovor,Artikli,Grupart WHERE stugovor.cart = artikli.cart "+
        "AND artikli.cgrart = grupart.cgrart "+
        "AND stugovor.cugovor='"+getMasterSet().getString("CUGOVOR")+
        "' AND stugovor.knjig='"+getMasterSet().getString("KNJIG")+
        "' order by grupart.ssort,stugovor.cart"; 
System.out.println(sql);		
		QueryDataSet qds = Util.getNewQueryDataSet(sql);
		HashMap hm = new HashMap();
		short rbr = 1;
		for (qds.first();qds.inBounds();qds.next()) {
			hm.put(String.valueOf(qds.getShort("RBR")),new short[]{rbr});
			rbr++;
//System.out.println("ima li ga "+qds.getShort("RBR")+"  ?" +hm.containsKey(String.valueOf(qds.getShort("RBR"))));			
		}
		
		System.out.println("RBR u qds = "+qds.getRowCount());
		System.out.println("RBR u getDetailSet() = "+getDetailSet().getRowCount());
		
		if (qds.getRowCount() != getDetailSet().getRowCount()) {
			JOptionPane
			.showConfirmDialog(
					raDetail,
					"Postoje artikli koji nemaju grupu artikla.  \n"+
					"Presortiravanje nije moguæe !",
					"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int row = getDetailSet().row();
		for (qds.first();qds.inBounds();qds.next()) {
			if (lD.raLocate(getDetailSet(),new String[]{"CUGOVOR","RBSID","KNJIG"},
					new String[]{
                      getMasterSet().getString("CUGOVOR"),
                      String.valueOf(qds.getInt("RBSID")),
                      getMasterSet().getString("KNJIG")
                      })){

				rbr = ((short[])hm.get(String.valueOf(qds.getShort("RBR"))))[0];
				
				
				if (getDetailSet().getString("MANIPULATIVNI").trim().length()!=0){
					VarStr vs = new VarStr(getDetailSet().getString("MANIPULATIVNI"));
System.out.println("Start vs "+vs);					
					Iterator it = hm.keySet().iterator();
					
					Object obj = null;
					while (it.hasNext()){
						obj = it.next();
System.out.println(obj+"->"+((short[])hm.get(obj))[0]);						
						vs.replaceAll("["+Short.parseShort((String) obj)+"]",
							"[tv"+((short[])hm.get(obj))[0]+"]");
System.out.println(vs);						
						
					}
					vs.replaceAll("[tv","[");
System.out.println("Konaèmi "+vs);
					
					getDetailSet().setString("MANIPULATIVNI",vs.toString());
				}
				getDetailSet().setShort("RBR",rbr);

			} else {
				System.out.println("Greška ne mogu naæi rbsid="+qds.getInt("RBSID"));
			}
			
			
		}


		raLocalTransaction saveTransaction = new raLocalTransaction() {

			public boolean transaction() throws Exception {
				saveChanges(getDetailSet());
				return true;
			}

		};
		if (saveTransaction.execTransaction()){
			getDetailSet().goToClosestRow(row);
			raDetail.setSort(new String[]{"RBR"});
			
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
//					getDetailSet().refresh();
					raDetail.getJpTableView().fireTableDataChanged();
				}});
			
			JOptionPane
			.showConfirmDialog(
					raDetail,
					"Presortiravanje uspješno završeno !",
					"Informacija", JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane
			.showConfirmDialog(
					raDetail,"Presortiravanje nije uspjelo !",
					"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	raNavAction rnvRekalkul = new raNavAction("Rekalkulacija", raImages.IMGEXPORT2,
			java.awt.event.KeyEvent.VK_F12) {
		public void actionPerformed(ActionEvent e) {
			Object[] options = { "Da", "Ne" };

			int res = JOptionPane.showOptionDialog(raDetail,
					"Želite li rekalkulirati sve aktivne \n" +
					" ugovore prema zadnjem teèaju ?",
					"Upit", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
					null, options, options[0]);

			if (res == 0) {
			     raProcess.runChild(raMaster, new Runnable() {
			          public void run() {
							rekalkulAll();			            
			          }
			        });
			     getDetailSet().refresh();
			     if (raProcess.isCompleted()) {
					JOptionPane
					.showConfirmDialog(
							raDetail,
							"Rekalkulacija ugovora završena !",
							"Informacija", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
			     }
			}
		}
	};
	private String domOZNVAL;
	private String getDomOZNVAL() {
	  if (domOZNVAL == null || domOZNVAL.trim().equals("")) {
	    domOZNVAL = Tecajevi.getDomOZNVAL();
	  }
	  return domOZNVAL;
	}
	QueryDataSet porezi;
	QueryDataSet artikli;
	public void rekalkulAll(){
		
		raProcess.setMessage("Rekalkulacija u tijeku ..",true);
		QueryDataSet zaglavlja = Util.getNewQueryDataSet(
				"select * from Ugovori WHERE aktiv='D' and "+getCorgCondition("ugovori"));
		QueryDataSet zadnjatecajnalista = Util.getNewQueryDataSet(
				"select * from Tecajevi a WHERE datval in (select max(datval) from tecajevi b where a.oznval=b.oznval)");
		porezi = Porezi.getDataModule().copyDataSet();
		artikli = Artikli.getDataModule().copyDataSet();
		porezi.open();
		artikli.open();
		for (zaglavlja.first();zaglavlja.inBounds();zaglavlja.next()) {
			raProcess.setMessage("Obradjujem ugovor "+zaglavlja.getString("CUGOVOR"),true);
			maintancestavke(zaglavlja.getString("CUGOVOR") ,zadnjatecajnalista);
		}
	}
    /**
     * @return " corg in ('popis','corgova','koji','pripadaju','tekucem', knjigovodstvu')"
     */
  public static String getCorgCondition(String tname) {
    String tabname = (tname == null || tname.trim().length()==0)?"":tname.concat(".");
    return " "+tabname+"knjig = '"+OrgStr.getKNJCORG()+"'";
  }

  public void maintancestavke(String ugovor ,QueryDataSet teclista) {
		QueryDataSet stavke = 
			Util.getNewQueryDataSet("SELECT * FROM STUGOVOR where CUGOVOR='"+ugovor+"' and "+getCorgCondition("STUGOVOR"));
		for (stavke.first();stavke.inBounds();stavke.next()){
		  handlePOR(stavke); //vuce ppor1 ppor2 ppor3 iz tablice porezi
			if (stavke.getString("OZNVAL") == null  || stavke.getString("OZNVAL").trim().length()==0 || stavke.getString("OZNVAL").trim().equalsIgnoreCase(getDomOZNVAL())) {
				stavke.setString("OZNVAL", getDomOZNVAL());
			  //continue;
			}
			if (lD.raLocate(teclista,"OZNVAL",stavke.getString("OZNVAL"))){
				stavke.setBigDecimal("TECAJ", teclista.getBigDecimal("TECSRED"));
				
				kalkulacija("VAL_VC", stavke);
				setupIznos4Racun(stavke);
			}
		}
		HashMap hm = getCalcHashMap(stavke);
		for (stavke.first(); stavke.inBounds(); stavke.next()) {
			if (!(stavke.getString("MANIPULATIVNI") == null  || stavke.getString("MANIPULATIVNI").trim().length()==0 )) {
				kalkManipStavka(stavke, hm);
			}
		}
		majnetrans(stavke);
		
	}
	
	
//vuce ppor1 ppor2 ppor3 iz tablice porezi
	private void handlePOR(QueryDataSet stavke) {
	  if (!lookupData.getlookupData().raLocate(artikli, "CART", stavke.getInt("CART")+"")) {
	    System.err.println("Err!!@##@@! artikal "+stavke.getInt("CART")+" nije pronadjen");
	    return;
	  }
	  if (!lookupData.getlookupData().raLocate(porezi, "CPOR", artikli.getString("CPOR"))) {
	    System.err.println("Err!!! porez za artikl CART::"+stavke.getInt("CART")+" CPOR::"+artikli.getString("CPOR")+" nije pronadjen");
	    return;
	  }
	  stavke.setBigDecimal("PPOR1", porezi.getBigDecimal("POR1"));
	  stavke.setBigDecimal("PPOR2", porezi.getBigDecimal("POR2"));
	  stavke.setBigDecimal("PPOR3", porezi.getBigDecimal("POR3"));
	  stavke.setBigDecimal("UPPOR", porezi.getBigDecimal("POR1").add(porezi.getBigDecimal("POR2").add(porezi.getBigDecimal("POR3"))));
	  stavke.post();
  }
  public void jBStavke_actionPerformed(java.awt.event.ActionEvent e) {

		if (!hr.restart.sisfun.frmParam.getParam("robno", "ugoRekal",
				"N", "Rekalk. ugovora kod ulaza u stavke").equalsIgnoreCase("D")) {
			super.jBStavke_actionPerformed(e);
			return;
		}
		
		
		QueryDataSet qds = Util
				.getNewQueryDataSet("SELECT * FROM STUGOVOR "
						+ "where cugovor='"
						+ getMasterSet().getString("CUGOVOR") + "'" 
                        + " AND knjig = '"+getMasterSet().getString("KNJIG")+"'");
		if (qds.getRowCount() == 0) {
			super.jBStavke_actionPerformed(e);
			return;
		}

		Object[] options = { "Da", "Ne" };

		int res = JOptionPane.showOptionDialog(this,
				"Želite li rekalkulirati ugovor prema \n zadnjem teèaju ?",
				"Upit", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
				null, options, options[0]);

		if (res == 0) {
			rekalkulacijaStavki();
		}
		super.jBStavke_actionPerformed(e);

	}

	public void rekalkulacijaStavki() {
		QueryDataSet qds = Util
				.getNewQueryDataSet("SELECT * FROM STUGOVOR "
						+ "where cugovor='"
						+ getMasterSet().getString("CUGOVOR") + "'"
                        + " AND knjig = '"+getMasterSet().getString("KNJIG")+"'");
		QueryDataSet valut;
		for (qds.first(); qds.inBounds(); qds.next()) {
			valut = Util
					.getNewQueryDataSet("select * from Tecajevi where oznval='"
							+ qds.getString("OZNVAL") + "' order by datval");
			if (valut.getRowCount() == 0)
				continue;
			valut.last(); // zadnji teèaj
			qds.setBigDecimal("TECAJ", valut.getBigDecimal("TECSRED"));
			kalkulacija("VAL_VC", qds);
		}
		majnetrans(qds);
		getDetailSet().refresh();
		HashMap hm = getCalcHashMap(qds);
		for (qds.first(); qds.inBounds(); qds.next()) {
			if (!qds.getString("MANIPULATIVNI").equalsIgnoreCase("")) {
				// System.out.println("Manip =
				// "+qds.getString("MANIPULATIVNI"));
				kalkManipStavka(qds, hm);
			}
		}
		majnetrans(qds);

	}

	public boolean majnetrans(final QueryDataSet qds) {
		raLocalTransaction saveTransaction = new raLocalTransaction() {

			public boolean transaction() throws Exception {
				saveChanges(qds);
				return true;
			}

		};
		return saveTransaction.execTransaction();

	}

	public void EntryPointMaster(char mode) {
	}

	public boolean ValDPEscapeMaster(char mode) {

		return true;
	}

	public void defaultMasterValues() {
		if (lD.raLocate(dm.getNacotp(), new String[] { "CNAC" },
				new String[] { defNacotp })) {
			getMasterSet().setString("CNAC", defNacotp);
			ump.jlrCNAC.forceFocLost();
		}

		if (lD.raLocate(dm.getNamjena(), new String[] { "CNAMJ" },
				new String[] { defNamjena })) {
			getMasterSet().setString("CNAMJ", defNamjena);
			ump.jlrCNAMJ.forceFocLost();
		}

		if (lD.raLocate(dm.getFranka(), new String[] { "CFRA" },
				new String[] { franka })) {
			getMasterSet().setString("CFRA", franka);
			ump.jlrFRANKA.forceFocLost();
		}
		if (lD.raLocate(dm.getNacpl(), new String[] { "CNACPL" },
				new String[] { defNacpl })) {
			getMasterSet().setString("CNACPL", defNacpl);
			ump.jlrCNACPL.forceFocLost();
		}
	}

	public void SetFokusMaster(char mode) {
		if (mode == 'N') {
			defaultMasterValues();
			rCC.setLabelLaF(ump.jtCUGOVOR, true);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ump.jtCUGOVOR.requestFocus();
				}
			});
		} else if (mode == 'I') {
			rCC.setLabelLaF(ump.jtCUGOVOR, false);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ump.jtOPIS.requestFocus();
				}
			});
		}

	}

	public boolean DeleteCheckMaster() {
		return true;
	}
	private QueryDataSet ppar;
	private QueryDataSet getPp() {
	  if (ppar == null) ppar = Partneri.getDataModule().getTempSet();
	  ppar.open();
	  return ppar;
	}
	
	public boolean ValidacijaMaster(char mode) {
		if (mode == 'N') {
			if (vl.notUnique(ump.jtCUGOVOR))
				return false;
			getMasterSet().setString("KNJIG", OrgStr.getKNJCORG());
		}
		if (vl.isEmpty(ump.jtCUGOVOR))
			return false;
		if (vl.isEmpty(ump.jtOPIS))
			return false;
		if (vl.isEmpty(ump.jlrCPAR))
			return false;
        
		QueryDataSet pp = getPp();
		if (lookupData.getlookupData().raLocate(pp, "CPAR", getMasterSet().getInt("CPAR")+"")) {
		  getMasterSet().setString("NAZPAR", pp.getString("NAZPAR"));
		}
		return true;
	}

	public boolean ValidacijaDetail(char mode) {
		if (vl.isEmpty(udp.rpcart.jrfCART))
			return false;
		if (vl.isEmpty(udp.jtfKOL))
			return false;

        if (mode == 'N') {
          getDetailSet().setString("KNJIG", getMasterSet().getString("KNJIG"));
        }
		// if (vl.isEmpty(udp.jraFC))
		// return false;

		return true;
	}

	public void EntryPointDetail(char mode) {
	}

	public void SetFokusDetail(char mode) {

		if (mode == 'N') {
			// rCC.setLabelLaF(udp.rpcart,true);
			udp.rpcart.EnabDisab(true);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					udp.rpcart.SetFocus();
				}
			});
			rCC.EnabDisabAll(udp.jpDetailCenter, false);
			udp.disableS();

		} else if (mode == 'I') {
			// rCC.setLabelLaF(udp.rpcart,false);
			udp.rpcart.EnabDisab(false);
			rCC.EnabDisabAll(udp.jpDetailCenter, true);
			udp.disableS();
			udp.jpgetval.initJP(mode);
			if (!getDetailSet().getString("MANIPULATIVNI").equalsIgnoreCase("")) {
				rCC.setLabelLaF(udp.jpgetval.jcbValuta, false);
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					udp.jtfKOL.requestFocus();
				}
			});
		}

		udp.myHandler();

	}

	public boolean DeleteCheckDetail() {

		delRbr = getDetailSet().getShort("RBR");
		return true;
	}

	public void AfterDeleteDetail() {
	}

	public boolean ValDPEscapeDetail(char mode) {
		return true;
	}

	BigDecimal suma = Aus.zero2;

	public boolean doWithSaveDetail(char mode) {

		QueryDataSet manip = Util
				.getNewQueryDataSet("SELECT * FROM STUGOVOR WHERE CUGOVOR='"
						+ getMasterSet().getString("CUGOVOR") + "'"
                        + " AND knjig = '"+getMasterSet().getString("KNJIG")+"'");
		BigDecimal sumaa = kalkulManipulacija(manip);
		raTransaction.saveChanges(manip);
		getMasterSet().setBigDecimal("IZNOS", sumaa);
		raTransaction.saveChanges(getMasterSet());
		if (this.vtt != null) {
			 String ckey = raControlDocs.getKey(getDetailSet());
			 this.vtt.setString("CKEY",ckey);
			 raTransaction.saveChanges(this.vtt);
		}

		return true;
	}

	public void setupIznos4Racun(QueryDataSet qds){
		if (qds.getBigDecimal("IPRODBP").compareTo(
				qds.getBigDecimal("IMINIZNOSBP")) > 0) {
			qds.setBigDecimal("IZNOSRAC",
					qds.getBigDecimal("IPRODBP"));
			qds.setBigDecimal("IZNOSRACSP",
					qds.getBigDecimal("IPRODSP"));
		} else {
			qds.setBigDecimal("IZNOSRAC",
					qds.getBigDecimal("IMINIZNOSBP"));
			BigDecimal porez = qds.getBigDecimal("IMINIZNOSBP")
					.multiply(qds.getBigDecimal("UPPOR"))
					.divide(new BigDecimal("100"), 2,BigDecimal.ROUND_HALF_UP);
			qds.setBigDecimal("IZNOSRACSP",
					qds.getBigDecimal("IMINIZNOSBP").add(porez));
		}
		if (!qds.getString("STATUSRAC").equalsIgnoreCase("D")) {
			qds.setBigDecimal("IZNOSRAC", Aus.zero2);
			qds.setBigDecimal("IZNOSRACSP",Aus.zero2);
		}
System.out.println("Radim kalkulaciju iznosa za faktrur");

		
		
	}
	
	public boolean doBeforeSaveDetail(char mode) {

		if (mode == 'B') {
			vl.recountDataSet(getDetailSet(), "rbr", delRbr, false);
		} else {
			setupIznos4Racun(getDetailSet());
		}
		suma = Util.getNewQueryDataSet(
				"SELECT MAX(IPRODSP) AS IPRODSP FROM STUGOVOR WHERE CUGOVOR='"
						+ getMasterSet().getString("CUGOVOR") + "'"
                        + " AND knjig = '"+getMasterSet().getString("KNJIG")+"'")
				.getBigDecimal("IPRODSP");
		if (mode == 'N') {
			try {
				short rbr = Util.getNewQueryDataSet(
						"SELECT MAX(RBR) AS RBR FROM STUGOVOR WHERE CUGOVOR='"
								+ getMasterSet().getString("CUGOVOR") + "'"
                                + " AND knjig = '"+getMasterSet().getString("KNJIG")+"'")
						.getShort("RBR");
				int rbsid = Util.getNewQueryDataSet(
						"SELECT MAX(RBSID) AS RBSID FROM STUGOVOR WHERE CUGOVOR='"
								+ getMasterSet().getString("CUGOVOR") + "'"
                                + " AND knjig = '"+getMasterSet().getString("KNJIG")+"'")
						.getInt("RBSID");
				getDetailSet().setShort("RBR", ++rbr);
				getDetailSet().setInt("RBSID", ++rbsid);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return true;
	}

	public void metToDo_after_lookUp() {
		rCC.EnabDisabAll(udp.jpDetailCenter, true);
		udp.disableS();
		udp.jpgetval.jcbValuta.setSelected(false);
		udp.jpgetval.actionPerformedjcbValuta();

		QueryDataSet artikli = Util
				.getNewQueryDataSet("SELECT * from artikli where cart = "
						+ getDetailSet().getInt("CART"));
		if (artikli.getRowCount() != 0) {
			QueryDataSet porezi = Util
					.getNewQueryDataSet("SELECT * from porezi where cpor = '"
							+ artikli.getString("CPOR") + "'");
			if (porezi.getRowCount() != 0) {
				
				getDetailSet().setBigDecimal("PPOR1",
						porezi.getBigDecimal("POR1"));
				getDetailSet().setBigDecimal("PPOR2",
						porezi.getBigDecimal("POR2"));
				getDetailSet().setBigDecimal("PPOR3",
						porezi.getBigDecimal("POR3"));
				getDetailSet().setBigDecimal("UPPOR",
						porezi.getBigDecimal("UKUPOR"));
			}
		}

		QueryDataSet cjenikSKL = Util
				.getNewQueryDataSet("select * from cjenik where cpar="
						+ getMasterSet().getInt("CPAR") + " and cart = "
						+ getDetailSet().getInt("CART") + " and cskl='"
						+ getDetailSet().getString("CSKLART")
						+ "' and corg = '" + OrgStr.getKNJCORG() + "' ");
		QueryDataSet cjenikALL = Util
				.getNewQueryDataSet("select * from cjenik where cpar="
						+ getMasterSet().getInt("CPAR") + " and cart = "
						+ getDetailSet().getInt("CART")
						+ " and cskl='_!X!_' and corg = '"
						+ OrgStr.getKNJCORG() + "' ");

		if (!raVart.isStanje(artikli)) {
		    /*artikli.getString("VRART").equalsIgnoreCase("U")
				|| artikli.getString("VRART").equalsIgnoreCase("T")) {*/
			if (cjenikALL.getRowCount() != 0) {
				getDetailSet().setBigDecimal("FC",
						cjenikALL.getBigDecimal("VC"));
			} else {
				getDetailSet().setBigDecimal("FC", artikli.getBigDecimal("VC"));
			}
		} else {
			QueryDataSet stanje = Util
					.getNewQueryDataSet("select * from stanje where "
							+ "CSKL='" + getDetailSet().getString("CSKLART")
							+ "' and god='" + udp.getGod() + "' and cart="
							+ getDetailSet().getInt("CART"));

			if (cjenikSKL.getRowCount() != 0) {
				getDetailSet().setBigDecimal("FC",
						cjenikSKL.getBigDecimal("VC"));
			} else if (cjenikALL.getRowCount() != 0) {
				getDetailSet().setBigDecimal("FC",
						cjenikALL.getBigDecimal("VC"));
			} else if (stanje.getRowCount() != 0) {
				getDetailSet().setBigDecimal("FC", stanje.getBigDecimal("VC"));
			} else {
				getDetailSet().setBigDecimal("FC", artikli.getBigDecimal("VC"));
			}
		}
	}

	public void AfterSaveDetail(char mode) {
		int row = getDetailSet().getRow();
		getDetailSet().refresh();
		getDetailSet().goToClosestRow(row);
		if (mode == 'N') {
			rCC.setLabelLaF(udp.rpcart, true);
			rCC.EnabDisabAll(udp.jpDetailCenter, false);
			udp.disableS();
		}
	}

	public void kalkulacija(String name, DataSet ds) {
		BigDecimal tmp1 = Aus.zero2;
		BigDecimal tmp2 = Aus.zero2;

		if (name.equalsIgnoreCase("KOLIFCIUIRAB")) {

			tmp1 = (ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("FC")))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			tmp2 = (ds.getBigDecimal("UPRAB").multiply(tmp1)).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp2 = tmp2.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			ds.setBigDecimal("UIRAB", tmp2);
			ds.setBigDecimal("IPRODBP", (tmp1.subtract(tmp2)).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			try {
				ds.setBigDecimal("FVC", ds.getBigDecimal("IPRODBP").divide(
						ds.getBigDecimal("KOL"), 2, BigDecimal.ROUND_HALF_UP));
			} catch (Exception e) {
				System.out.println("KOL = 0..."); // XDEBUG delete when no
				// more needed
				ds.setBigDecimal("FVC", Aus.zero2);
				// TODO: hendlati upis cijene prije kolicine!!
			}

			tmp1 = (ds.getBigDecimal("IPRODBP").multiply(ds
					.getBigDecimal("PPOR1"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			ds.setBigDecimal("POR1", tmp1);
			
			tmp1 = (ds.getBigDecimal("IPRODBP").multiply(ds
					.getBigDecimal("PPOR2"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			ds.setBigDecimal("POR2", tmp1);
			
			tmp1 = (ds.getBigDecimal("IPRODBP").multiply(ds
					.getBigDecimal("PPOR3"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			ds.setBigDecimal("POR3", tmp1);
/*			
			tmp1 = (ds.getBigDecimal("IPRODBP").multiply(ds
					.getBigDecimal("UPPOR"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.add(ds.getBigDecimal("IPRODBP")).setScale(2,
					BigDecimal.ROUND_HALF_UP);
*/
			tmp1 = ds.getBigDecimal("IPRODBP").add(ds.getBigDecimal("POR1")).
			          add(ds.getBigDecimal("POR2")).add(ds.getBigDecimal("POR3")).setScale(2,
					  BigDecimal.ROUND_HALF_UP);
			
			try {
				tmp2 = tmp1.divide(ds.getBigDecimal("KOL"), 2,
						BigDecimal.ROUND_HALF_UP);
			} catch (Exception ex) {
				tmp2 = Aus.zero2;
			}

			ds.setBigDecimal("IPRODSP", tmp1);
			ds.setBigDecimal("FMC", tmp2);
		} else if (name.equalsIgnoreCase("IPRODBP")) {

/*			
			tmp1 = (ds.getBigDecimal("IPRODBP").multiply(ds
					.getBigDecimal("UPPOR"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.add(ds.getBigDecimal("IPRODBP")).setScale(2,
					BigDecimal.ROUND_HALF_UP);
*/					
			tmp1 = (ds.getBigDecimal("IPRODBP").multiply(ds
					.getBigDecimal("PPOR1"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			ds.setBigDecimal("POR1", tmp1);
			
			tmp1 = (ds.getBigDecimal("IPRODBP").multiply(ds
					.getBigDecimal("PPOR2"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			ds.setBigDecimal("POR2", tmp1);
			
			tmp1 = (ds.getBigDecimal("IPRODBP").multiply(ds
					.getBigDecimal("PPOR3"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.divide(new BigDecimal("100"), 2,
					BigDecimal.ROUND_HALF_UP);
			ds.setBigDecimal("POR3", tmp1);

			tmp1 = ds.getBigDecimal("IPRODBP").add(ds.getBigDecimal("POR1")).
			          add(ds.getBigDecimal("POR2")).add(ds.getBigDecimal("POR3")).setScale(2,
					  BigDecimal.ROUND_HALF_UP);					
					
			try {
				tmp2 = tmp1.divide(ds.getBigDecimal("KOL"), 2,
						BigDecimal.ROUND_HALF_UP);
			} catch (Exception ex) {
				tmp2 = Aus.zero2;
			}
			ds.setBigDecimal("IPRODSP", tmp1);
			ds.setBigDecimal("FMC", tmp2);
			try {
				ds.setBigDecimal("FVC", ds.getBigDecimal("IPRODBP").divide(
						ds.getBigDecimal("KOL"), 2, BigDecimal.ROUND_HALF_UP));
			} catch (Exception ex) {
				ds.setBigDecimal("FVC", Aus.zero2);
			}

			tmp1 = (ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("FC")))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			tmp2 = (tmp1.subtract(ds.getBigDecimal("IPRODBP"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);

			ds.setBigDecimal("UIRAB", tmp2);

			if (tmp1.doubleValue() != 0) {
				tmp2 = tmp2.divide(tmp1, 4, BigDecimal.ROUND_HALF_UP);
			} else {
				tmp2 = Aus.zero3;
			}

			tmp2 = (tmp2.multiply(new BigDecimal("100"))).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			ds.setBigDecimal("UPRAB", tmp2);
		} else if (name.equalsIgnoreCase("VAL_VC")) {
		  if (!ds.getString("OZNVAL").equals(getDomOZNVAL())) {
		    tmp1 = (ds.getBigDecimal("VAL_VC").multiply(ds
		        .getBigDecimal("UPPOR"))).setScale(4,
		            BigDecimal.ROUND_HALF_UP);
		    tmp1 = tmp1.divide(new BigDecimal("100"), 4,
		        BigDecimal.ROUND_HALF_UP);
		    
		    ds.setBigDecimal("VAL_MC", tmp1.add(ds.getBigDecimal("VAL_VC"))
		        .setScale(4, BigDecimal.ROUND_HALF_UP));
		    
		    ds.setBigDecimal("FC", ds.getBigDecimal("TECAJ").multiply(
		        ds.getBigDecimal("VAL_VC")).setScale(2,
		            BigDecimal.ROUND_HALF_UP));
		  }
			kalkulacija("KOLIFCIUIRAB", ds);

		} else if (name.equalsIgnoreCase("VAL_MC")) {
			tmp1 = ds.getBigDecimal("UPPOR").divide(new BigDecimal("100.00"),
					6, BigDecimal.ROUND_HALF_UP);
			tmp1 = tmp1.add(new BigDecimal("1.00")).setScale(6,
					BigDecimal.ROUND_HALF_UP);

			if (tmp1.compareTo(Aus.zero2) != 0) {
				tmp2 = (ds.getBigDecimal("VAL_MC").divide(tmp1, 4,
						BigDecimal.ROUND_HALF_UP));
				ds.setBigDecimal("VAL_VC", tmp2);
			} else {
				ds.setBigDecimal("VAL_VC", new BigDecimal("0.0000"));
			}
			ds.setBigDecimal("FC", ds.getBigDecimal("TECAJ").multiply(
					ds.getBigDecimal("VAL_VC")).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			kalkulacija("KOLIFCIUIRAB", ds);
		}
	}

	public boolean validManipString(String manip) {

		char[] validc = new char[] { ' ', '+', '.', ',', '*', '/', '-', '(',
				')', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '[', ']' };

		char[] stc = manip.toCharArray();
		for (int i = 0; i < stc.length; i++) {
			boolean ret = false;
			for (int j = 0; j < validc.length; j++) {
				if (stc[i] == validc[j]) {

					ret = true;
					break;
				}
			}
			if (!ret)
				return false;
		}
		return true;
	}

	public void mymanipkalk() {

		QueryDataSet manip = Util
				.getNewQueryDataSet("SELECT * FROM STUGOVOR WHERE CUGOVOR='"
						+ getMasterSet().getString("CUGOVOR") + "'"
                        + " AND knjig = '"+getMasterSet().getString("KNJIG")+"'");
		HashMap al = getCalcHashMap(manip);
		kalkManipStavka(getDetailSet(), al);

	}

	public void kalkManipStavka(QueryDataSet qds, HashMap al) {

		MathEvaluator me = new MathEvaluator();

		String m = qds.getString("MANIPULATIVNI");
System.out.println(m);
		VarStr tmpvs = new VarStr(m);
		
		
//		if (m.length() > "[ALL]".length()
//				&& m.substring(0, "[ALL]".length()).equalsIgnoreCase("[ALL]")) {
    	if (tmpvs.indexOf("[ALL]")>-1) {			
			
			
			System.out.println("jeee");
			QueryDataSet rbsovi = Util
					.getNewQueryDataSet("SELECT RBR from stugovor where cugovor='"
							+ qds.getString("CUGOVOR")+ "'"
                            + " AND knjig = '"+qds.getString("KNJIG")+"'"
                            + " and statusman='D'");
			if(rbsovi.getRowCount()<1) {
				qds.setBigDecimal("KOL", new BigDecimal("1.00"));
				qds.setBigDecimal("FC", Aus.zero2);
				kalkulacija("IPRODBP", qds);
				setupIznos4Racun(qds);

				return;
			}
			
System.out.println(rbsovi.getQuery().getQueryString());			
			String replStr = "";
			for (rbsovi.first(); rbsovi.inBounds(); rbsovi.next()) {
				replStr = replStr + "[" + rbsovi.getShort("RBR") + "]+";
			}
			replStr = replStr.substring(0, replStr.length() - 1);
			if (replStr.equalsIgnoreCase(""))
				replStr = "0";
			else {
				replStr = "(" + replStr + ")";
			}
			tmpvs.replaceAll("[ALL]", replStr);
			m = tmpvs.toString();
		}
		System.out.println("Manip string =" + m);

		if (validManipString(m)) {

			VarStr vs = new VarStr(m);
			for (short k = 0; k < 101; k++) {
				String broj = "0.00";
				if (al.containsKey(new Short(k))) {
					broj = ((BigDecimal) al.get(new Short(k))).toString();
				} else {
					broj = "0.00";
				}
				vs.replaceAll("[" + k + "]", broj);
			}
			try {
				me.reset();
				me.setExpression(vs.toString());
				Double evaluated = me.getValue();
				if (evaluated != null) {
					qds.setBigDecimal("IPRODBP", new BigDecimal(evaluated
							.doubleValue()).setScale(2,
							BigDecimal.ROUND_HALF_UP));
					if (qds.getBigDecimal("KOL").doubleValue() != 0) {
						qds.setBigDecimal("FC", qds.getBigDecimal("IPRODBP")
								.divide(qds.getBigDecimal("KOL"), 2,
										BigDecimal.ROUND_HALF_UP));
					} else {
						qds.setBigDecimal("FC", qds.getBigDecimal("IPRODBP"));
						qds.setBigDecimal("KOL", new BigDecimal("1.00"));
					}
					System.out.println("USPJESNO " + vs.toString() + "="
							+ qds.getBigDecimal("IPRODBP"));
				}
			} catch (Exception e) {
				// System.out.println("evaluateAndPost: exception "+e);
				System.out.println("evaluateAndPost: exception "
						+ vs.toString());
			}
			kalkulacija("IPRODBP", qds);
			setupIznos4Racun(qds);
		}
	}

	public HashMap getCalcHashMap(QueryDataSet qds) {

		HashMap al = new HashMap();
		for (qds.first(); qds.inBounds(); qds.next()) {
			al.put(new Short(qds.getShort("RBR")), qds
					.getBigDecimal("IZNOSRAC"));
		}
		return al;

	}

	public BigDecimal kalkulManipulacija(QueryDataSet qds) {
		BigDecimal suma = Aus.zero2;

		HashMap al = getCalcHashMap(qds);

		for (qds.first(); qds.inBounds(); qds.next()) {
			kalkManipStavka(qds, al);
		}

		for (qds.first(); qds.inBounds(); qds.next()) {
			suma = suma.add(qds.getBigDecimal("IPRODSP"));
		}

		return suma;
	}

	public void kalkulacija(String name) {
		kalkulacija(name, getDetailSet());
	}

	public void jlrCPAR_after_lookUp() {

		pjed = hr.restart.util.Util.getNewQueryDataSet(
				"select * from pjpar where cpar="
						+ ump.jlrCPAR.getDataSet().getInt("CPAR"), true);

		ump.jlrCPJ.setRaDataSet(pjed);
		// ST.prn(pjed);
/*		
		if (inSetFocus) {
			// inSetFocus=!inSetFocus;
			return;
		}
*/		
System.out.println("Tu sam");		
		if ((raMaster.getMode() == 'N'
				|| raMaster.getMode() == 'I') && ump.jlrCPAR.isValueChanged()) {
System.out.println("Oðe bi trebao biti");			
			getMasterSet().setShort("DANIDOSP",
					ump.jlrCPAR.getRaDataSet().getShort("DOSP"));
		}
	}

	public void beforeShow() {
		inSetFocus = true;
	}

	public void rabatmaintance() {
		// raDDodRab rDR = new raDDodRab((Frame) raDetail.getFrameOwner(), this,
		// "Dodatni popust", true){
		// public void afterJob() {
		// }
		// };
	}

	private static frmUgovori instanceOfMe = null;

	public static frmUgovori getInstance() {
		if (instanceOfMe == null)
			instanceOfMe = new frmUgovori();
		return instanceOfMe;
	}

	public frmUgovori() {
		super(1, 3);
		try {
          setMasterDeleteMode(raMasterDetail.DELDETAIL);
			ump = new UgovoriMasterPanel();
			udp = new UgovoriDetailPanel() {
				public void mymetToDo_after_lookUp() {
					metToDo_after_lookUp();
				}

				public void kkalkulacija(String name) {
					kalkulacija(name);
				}

				public void jbRabat_actionPerformed(ActionEvent e) {
					rabatmaintance();
				}

				public void manipkalk() {
					mymanipkalk();
				}

			};
			udp.jpgetval.setTecajDate(vl.getToday());
			setMasterSet(dm.getAllUgovori());
			Ugovori.getDataModule().setFilter(getMasterSet(),Condition.equal("KNJIG",OrgStr.getKNJCORG()));
			setDetailSet(dm.getstugovor());
			java.lang.String[] key = { "CUGOVOR","KNJIG" };
			setMasterKey(key);
			setDetailKey(key);
			raMaster.getNavBar().getColBean().setSaveSettings(true);
			setNaslovMaster("Ugovori");
			setNaslovDetail("Stavke ugovora");
			setVisibleColsMaster(new int[] { 1, 2, 5, 10 });
			setVisibleColsDetail(new int[] { 1,
					Aut.getAut().getCARTdependable(2, 3, 4), 5, 8, 9, 21, 22,
					30 });
			setJPanelMaster(ump);
			setJPanelDetail(udp);
			raMaster.getJpTableView().addTableModifier(new AktivColorModifier());
			raMaster.addOption(new raNavAction("Kopiraj tekuæi zapis",raImages.IMGCOPYCURR,KeyEvent.VK_F2,KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        rnvCopyCurr_action();
      }
			}, 4);
			raDetail.removeRnvCopyCurr();
			raDetail.addOption(rnvRbr,4);
			raMaster.addOption(rnvRekalkul,5);
			raDetail.setSort(new String[]{"RBR"});
//			raMaster.getJpTableView().addTableModifier(
//					new hr.restart.swing.raTableColumnModifier("CPAR",
//							new String[] { "CPAR", "NAZPAR" },
//							new String[] { "CPAR" }, dm.getPartneri()));
			ump.bindComponents(getMasterSet());
			udp.BindComponents(getDetailSet());
			setUserCheck(false);
			set_kum_detail(true);
			stozbrojiti_detail(new String[] { "IPRODSP", "IZNOSRACSP" });

			this.raMaster.getRepRunner().addReport(
					"hr.restart.zapod.repUgovori",
					"hr.restart.zapod.repUgovori", "Ugovori", "Ispis Ugovora");
			this.raDetail.getRepRunner().addReport(
					"hr.restart.zapod.repUgovori",
					"hr.restart.zapod.repUgovori", "Ugovori", "Ispis Ugovora");

			instanceOfMe = this;

			
            hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
                new raKnjigChangeListener() {
                    public void knjigChanged(String oldKnjig,
                            String newKnjig) {
                        if (raDetail.isShowing()) raDetail.hide();
                        Ugovori.getDataModule().setFilter(getMasterSet(),Condition.equal("KNJIG", newKnjig));
                        getMasterSet().open();
                        //setMasterSet(dm.getAllUgovori());
                        ump.bindComponents(getMasterSet());
                        raMaster.getJpTableView().fireTableDataChanged();
                    }
                });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	private QueryDataSet getUgovoriK() {
//      return Ugovori.getDataModule().getFilteredDataSet(getCorgCondition("Ugovori"));
//    }
  /*public void masterSet_navigated(NavigationEvent e) {
		raDetail.setSort(new String[]{"RBR"});
		
	}*/

	/*
	 * public void SetFokus(char mode) {
	 * 
	 * if (mode == 'N') {
	 * ump.jtDANIDOSP.getDataSet().setShort(ump.jtDANIDOSP.getColumnName(),(short)
	 * 0); rCC.setLabelLaF(ump.jtCUGOVOR,true); ump.jlrCORG.emptyTextFields();
	 * ump.jlrCPAR.emptyTextFields(); ump.jlrCPJ.emptyTextFields();
	 * ump.jlrCVRUGO.emptyTextFields(); ump.jtCUGOVOR.requestFocus(); } else if
	 * (mode == 'I') { rCC.setLabelLaF(ump.jtCUGOVOR,false);
	 * ump.jtOPIS.requestFocus(); } inSetFocus = false; }
	 */

	boolean copyCurrEngaged = false;
	StorageDataSet detailTMP = null;
	protected void rnvCopyCurr_action() {
	  refilterDetailSet();
	  detailTMP = getDetailSet().cloneDataSetStructure();
	  detailTMP.open();
	  for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
      detailTMP.insertRow(false);
      getDetailSet().copyTo(detailTMP);
      detailTMP.post();
    }
    raMaster.setMode('N');
    raMaster.copyToNew = true;
    copyCurrEngaged = true;
    raMaster.prepareDetails();
  }
	public void AfterAfterSaveMaster(char mode) {
	    if (copyCurrEngaged && mode == 'N') {
	      QueryDataSet detaljy = stugovor.getDataModule().getTempSet(Condition.nil);
	      detaljy.open();
	      for (detailTMP.first(); detailTMP.inBounds(); detailTMP.next()) {
//	        System.out.println(detailTMP);
//	        System.out.println("->");
	        detaljy.insertRow(false);
          detailTMP.copyTo(detaljy);
          detaljy.setString("CUGOVOR", getMasterSet().getString("CUGOVOR"));
          detaljy.post();
//          System.err.println(detaljy);
        }
	      detaljy.saveChanges();
	      refilterDetailSet();
	      copyCurrEngaged = false;
	      detailTMP = null;
	      raMaster.setMode('I');
	      raMaster.AfterAfterSave('I');
//	      raMaster.af
	    } else {
	      super.AfterAfterSaveMaster(mode);
	    }
	}

  class UgovoriMasterPanel extends JPanel {

		JLabel jlFRANK = new JLabel();

		JLabel jlNAMJ = new JLabel();

		JLabel jlNACPL = new JLabel();

		JLabel jlNACOTP = new JLabel();

		JraButton jbCFRA = new JraButton();

		JraButton jbCNAMJ = new JraButton();

		JraButton jbCNACPL = new JraButton();

		JraButton jbCNAC = new JraButton();

		JlrNavField jlrFRANKA = new JlrNavField() {
			public void after_lookUp() {
				jlrNAZFRA.setCaretPosition(0);
			}
		};

		JlrNavField jlrNAZFRA = new JlrNavField();

		JlrNavField jlrCNAMJ = new JlrNavField() {
			public void after_lookUp() {
				jlrNAZNAMJ.setCaretPosition(0);
			}
		};

		JlrNavField jlrNAZNAMJ = new JlrNavField();

		JlrNavField jlrCNACPL = new JlrNavField() {
			public void after_lookUp() {
				jlrNAZNACPL.setCaretPosition(0);
			}
		};

		JlrNavField jlrNAZNACPL = new JlrNavField();

		JlrNavField jlrCNAC = new JlrNavField() {
			public void after_lookUp() {
				jlrNAZNAC.setCaretPosition(0);
			}
		};

		JlrNavField jlrNAZNAC = new JlrNavField();

		//

		XYLayout xYLayout1 = new XYLayout();

		public JLabel jlUGOVOR = new JLabel();

		public JLabel jlPARTNER = new JLabel();

		public JLabel jlPJ = new JLabel("Poslovna jedinica");

		public JLabel jlDATUGOVOR = new JLabel();

		public JLabel jlOPIS = new JLabel();

		public JraTextField jtOPIS = new JraTextField();

		public JraTextField jtCUGOVOR = new JraTextField();

		public JraCheckBox jcbAKTIV = new JraCheckBox();

		public JraTextField jtDATUGOVOR = new JraTextField();

		public JraTextField jtDANIDOSP = new JraTextField();

		public JraTextField jtIZNOS = new JraTextField();

		public JLabel jlTEXTFAK = new JLabel("Opis stavke na ra\u010Dunu");

		public hr.restart.swing.JraTextArea jtTEXTFAK = new hr.restart.swing.JraTextArea();

		public JLabel jlCVRUGO = new JLabel("Vrsta ugovora");

		public JlrNavField jlrCVRUGO = new JlrNavField();

		public JlrNavField jlrOPIS = new JlrNavField();

		public JraButton jbGetVrugo = new JraButton();

		public JLabel jlCORG = new JLabel("Org. jedinica");

		public JlrNavField jlrCORG = new JlrNavField();

		public JlrNavField jlrNAZORG = new JlrNavField();

		public JraButton jbGetCorg = new JraButton();

		JlrNavField jlrNAZPJ = new JlrNavField();

		JraButton jbGetPJ = new JraButton();
		
	  JlrNavField jdbZIRO = new JlrNavField();
	  JLabel jLZiroOrg = new JLabel("Žiro raèun");
	  JraButton jBgetZiro = new JraButton();

		JlrNavField jlrCPAR = new JlrNavField() {
			public void after_lookUp() {
System.out.println("jlrCPAR_after_lookUp()");				
				jlrCPAR_after_lookUp();
			}
		};

		JlrNavField jlrNAZPAR = new JlrNavField();

		JraButton jbGetPar = new JraButton();

		JlrNavField jlrCPJ = new JlrNavField() {
			public void after_lookUp() {
				// jlrCPJ_after_lookUp();
			}
		};

		public void dodatakJelima() {
	   
		  jdbZIRO.setColumnName("ZIRO");
	    jdbZIRO.setRaDataSet(zirorn.getDataModule().getFilteredDataSet(Condition.equal("CORG", dlgGetKnjig.getKNJCORG())));
	    jdbZIRO.setFocusLostOnShow(false);
	    jdbZIRO.setColNames(new String[] {"ZIRO"});
	    jdbZIRO.setTextFields(new javax.swing.text.JTextComponent[] {jdbZIRO});
	    jdbZIRO.setVisCols(new int[] {1});
	    jdbZIRO.setHandleError(false);
	    jdbZIRO.setSearchMode(1);
	    jdbZIRO.setNavButton(jBgetZiro);


			jlrFRANKA.setColumnName("CFRA");
			jlrFRANKA.setVisCols(new int[] { 0, 1 });
			jlrFRANKA.setColNames(new String[] { "NAZFRA" });
			jlrFRANKA
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZFRA });
			jlrFRANKA.setRaDataSet(dm.getFranka());
			jlrFRANKA.setNavButton(jbCFRA);
			jlrNAZFRA.setColumnName("NAZFRA");
			jlrNAZFRA.setSearchMode(1);
			jlrNAZFRA.setNavProperties(jlrFRANKA);
			jlrNAZFRA.setCaretPosition(0);
			jbCFRA.setText("...");

			jlrCNAC.setColumnName("CNAC");
			jlrCNAC.setVisCols(new int[] { 0, 1 });
			jlrCNAC.setColNames(new String[] { "NAZNAC" });
			jlrCNAC
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNAC });
			jlrCNAC.setRaDataSet(dm.getNacotp());
			jlrCNAC.setNavButton(jbCNAC);
			jlrNAZNAC.setColumnName("NAZNAC");
			jlrNAZNAC.setSearchMode(1);
			jlrNAZNAC.setNavProperties(jlrCNAC);
			jlrNAZNAC.setCaretPosition(0);
			jbCNAC.setText("...");

			jlrCNACPL.setColumnName("CNACPL");
			jlrCNACPL.setVisCols(new int[] { 0, 1 });
			jlrCNACPL.setColNames(new String[] { "NAZNACPL" });
			jlrCNACPL
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNACPL });
			jlrCNACPL.setRaDataSet(dm.getNacpl());
			jlrCNACPL.setNavButton(jbCNACPL);
			jlrNAZNACPL.setColumnName("NAZNACPL");
			jlrNAZNACPL.setSearchMode(1);
			jlrNAZNACPL.setNavProperties(jlrCNACPL);
			jlrNAZNACPL.setCaretPosition(0);
			jbCNACPL.setText("...");

			jlrCNAMJ.setColumnName("CNAMJ");
			jlrCNAMJ.setVisCols(new int[] { 0, 1 });
			jlrCNAMJ.setColNames(new String[] { "NAZNAMJ" });
			jlrCNAMJ
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZNAMJ });
			jlrCNAMJ.setRaDataSet(dm.getNamjena());
			jlrNAZNAMJ.setColumnName("NAZNAMJ");
			jlrNAZNAMJ.setSearchMode(1);
			jlrNAZNAMJ.setNavProperties(jlrCNAMJ);
			jlrNAZNAMJ.setCaretPosition(0);
			jbCNAMJ.setText("...");
			jlrCNAMJ.setNavButton(jbCNAMJ);

			add(new JLabel("Paritet / Naèin otpreme "), new XYConstraints(15,
					195, -1, -1));
			// add(jlFRANK, new XYConstraints(15, 195, -1, -1));
			add(jlrFRANKA, new XYConstraints(150, 195, 30, -1));
			add(jlrNAZFRA, new XYConstraints(185, 195, 130, -1));
			add(jbCFRA, new XYConstraints(321, 195, 21, 21));
			// add(jlNACOTP, new XYConstraints(353, 195, -1, -1));
			add(jlrCNAC, new XYConstraints(346, 195, 30, -1)); // 442
			add(jlrNAZNAC, new XYConstraints(380, 195, 130, -1)); // 476
			add(jbCNAC, new XYConstraints(515, 195, 21, 21)); // 612

			// add(jlNACPL, new XYConstraints(15, 225, -1, -1));
			add(new JLabel("Naè. pl. / Namj. robe"), new XYConstraints(15, 220,
					-1, -1));
			add(jlrNAZNACPL, new XYConstraints(185, 220, 130, -1));

			add(jlrCNACPL, new XYConstraints(150, 220, 30, -1));
			add(jbCNACPL, new XYConstraints(321, 220, 21, 21));
			// add(jlNAMJ, new XYConstraints(353, 225, -1, -1));
			add(jlrCNAMJ, new XYConstraints(346, 220, 30, -1));
			add(jlrNAZNAMJ, new XYConstraints(380, 220, 130, -1));
			add(jbCNAMJ, new XYConstraints(515, 220, 21, 21));
			
			add(jLZiroOrg, new XYConstraints(15, 245, -1, -1));
	    add(jdbZIRO, new XYConstraints(150, 245, 360, -1));
	    add(jBgetZiro, new XYConstraints(515, 245, 21, 21));
		}

		public UgovoriMasterPanel() {
			setLayout(xYLayout1);
			xYLayout1.setWidth(540);
			xYLayout1.setHeight(285);

			// **** labele
			jlUGOVOR.setText("Broj");
			jlPARTNER.setText("Partner");
			// jlDATUGOVOR.setText("Datum / Iznos / D.dosp.");
			jlDATUGOVOR.setText("Datum / Dani dosp.");
			jlOPIS.setText("Opis");
			jtOPIS.setColumnName("OPIS");
			jtIZNOS.setColumnName("IZNOS");
			jtDATUGOVOR.setHorizontalAlignment(SwingConstants.CENTER);
			jtDATUGOVOR.setColumnName("DATUGOVOR");
			jtDANIDOSP.setColumnName("DANIDOSP");
			jtCUGOVOR.setHorizontalAlignment(SwingConstants.RIGHT);
			jtCUGOVOR.setColumnName("CUGOVOR");
			jlrCPAR.setColumnName("CPAR"); // pretraga prema kljucu
			jlrCPAR.setColNames(new String[] { "NAZPAR" }); // kolone koje se
			// pretrazuju u
			// raDataSetu
			jlrCPAR.setVisCols(new int[] { 0, 1 });// definiranje kolona za
			// prikaz u tablici
			jlrCPAR
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZPAR }); // ubacivanje
			// trazene
			// vrijednosti
			// u
			// text
			// field
			jlrCPAR.setRaDataSet(dm.getPartneri()); // trazenje po datasetu
			jlrCPAR.setNavButton(jbGetPar);
			jlrNAZPAR.setColumnName("NAZPAR");
			jlrNAZPAR.setSearchMode(1);
			jlrNAZPAR.setNavProperties(jlrCPAR);

			jlrCPJ.setColumnName("PJ"); // pretraga prema kljucu
			jlrCPJ.setColNames(new String[] { "NAZPJ" }); // kolone koje se
			// pretrazuju u
			// raDataSetu
			jlrCPJ.setVisCols(new int[] { 1, 2, 3, 4 });// definiranje kolona za
			// prikaz u tablici
			jlrCPJ
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZPJ }); // ubacivanje
			// trazene
			// vrijednosti
			// u
			// text
			// field
			jlrCPJ.setRaDataSet(pjed); // trazenje po datasetu
			jlrCPJ.setNavButton(jbGetPJ);
			jlrNAZPJ.setColumnName("NAZPJ");
			jlrNAZPJ.setSearchMode(1);
			jlrNAZPJ.setNavProperties(jlrCPJ);

			jbGetCorg.setText("...");
			jlrCORG.setColumnName("CORG"); // pretraga prema kljucu
			jlrCORG.setColNames(new String[] { "NAZIV" }); // kolone koje se
			// pretrazuju u
			// raDataSetu
			jlrCORG.setVisCols(new int[] { 0, 1 });// definiranje kolona za
			// prikaz u tablici
			jlrCORG
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrNAZORG }); // ubacivanje
			// trazene
			// vrijednosti
			// u
			// text
			// field
			jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
					.getOrgstrAndCurrKnjig()); // trazenje po datasetu
			jlrNAZORG.setColumnName("NAZIV");
			jlrNAZORG.setSearchMode(1);
			jlrNAZORG.setNavProperties(jlrCORG);
			jlrCORG.setNavButton(jbGetCorg);

			// **** check box
			jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
			jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
			jcbAKTIV.setText("Aktivan");
			jcbAKTIV.setColumnName("AKTIV");
			jcbAKTIV.setSelectedDataValue("D");
			jcbAKTIV.setUnselectedDataValue("N");

			// **** combo box
			// raCBoxUloga.setRaColumn("ULOGA");
			// raCBoxUloga.setRaDataSet(getRaQueryDataSet());
			// raCBoxUloga.setRaItems(new String[][] {
			// { "Oboje", "O"},
			// { "Kupac","K" },
			// { "Dobavlja\u010D", "D" }
			// });

			// **** button
			jbGetPar.setText("Ugovori - Partneri");
			jbGetPar.setToolTipText(jbGetPar.getText());
			jbGetPar.setText("...");

			// **** action LIsteners
			// jlrNAZPAR.addActionListener(new java.awt.event.ActionListener() {
			// public void actionPerformed(ActionEvent e) {
			// jrtNazPar_actionPerformed(e);
			// }
			// });

			// **** tomoadder

			jtTEXTFAK.setColumnName("TEXTFAK");

			jlrCVRUGO.setColumnName("CVRUGO"); // pretraga prema kljucu

			jlrCVRUGO.setColNames(new String[] { "OPIS" }); // kolone koje se
			// pretrazuju u
			// raDataSetu
			jlrCVRUGO.setVisCols(new int[] { 0, 1 });// definiranje kolona za
			// prikaz u tablici
			jlrCVRUGO
					.setTextFields(new javax.swing.text.JTextComponent[] { jlrOPIS }); // ubacivanje
			// trazene
			// vrijednosti
			// u
			// text
			// field
			jlrCVRUGO.setRaDataSet(dm.getVrsteugo()); // trazenje po datasetu
			jlrCVRUGO.setNavButton(jbGetVrugo);
			jlrOPIS.setColumnName("OPIS");
			jlrOPIS.setSearchMode(1);
			jlrOPIS.setNavProperties(jlrCVRUGO);
			jbGetVrugo.setText("...");
			jlCVRUGO.setText("Vrsta");

			add(jcbAKTIV, new XYConstraints(471, 15, -1, -1));
			add(jlUGOVOR, new XYConstraints(15, 20, -1, -1));
			add(jtCUGOVOR, new XYConstraints(150, 20, 100, -1));
			add(jlOPIS, new XYConstraints(15, 45, -1, -1));
			add(jtOPIS, new XYConstraints(150, 45, 361, -1));

			add(jlCORG, new XYConstraints(15, 70, -1, -1));
			add(jlrCORG, new XYConstraints(150, 70, 100, -1));
			add(jlrNAZORG, new XYConstraints(255, 70, 255, -1));
			add(jbGetCorg, new XYConstraints(515, 70, 21, 21));

			add(jlPARTNER, new XYConstraints(15, 95, -1, -1));
			add(jlrCPAR, new XYConstraints(150, 95, 100, -1));
			add(jlrNAZPAR, new XYConstraints(255, 95, 255, -1));
			add(jbGetPar, new XYConstraints(515, 95, 21, 21));

			add(jlPJ, new XYConstraints(15, 120, -1, -1));
			add(jlrCPJ, new XYConstraints(150, 120, 100, -1));
			add(jlrNAZPJ, new XYConstraints(255, 120, 255, -1));
			add(jbGetPJ, new XYConstraints(515, 120, 21, 21));

			add(jlDATUGOVOR, new XYConstraints(15, 145, -1, -1));
			add(jtDATUGOVOR, new XYConstraints(150, 145, 100, -1));
			// add(jtIZNOS, new XYConstraints(255, 145, 130, -1));
			add(jtDANIDOSP, new XYConstraints(390, 145, 120, -1));

			add(jlCVRUGO, new XYConstraints(15, 170, -1, -1));
			add(jlrCVRUGO, new XYConstraints(150, 170, 100, -1));
			add(jlrOPIS, new XYConstraints(255, 170, 255, -1));
			add(jbGetVrugo, new XYConstraints(515, 170, 21, 21));
			dodatakJelima();
			// add(jlTEXTFAK, new XYConstraints(15, 195, -1, -1));
			// add(jtTEXTFAK, new XYConstraints(150, 195, 361, 63));
		}

		public void bindComponents(DataSet ds) {
			jtTEXTFAK.setDataSet(ds);
			jlrCVRUGO.setDataSet(ds);
			jtOPIS.setDataSet(ds);
			jtIZNOS.setDataSet(ds);
			jtDATUGOVOR.setDataSet(ds);
			jtDANIDOSP.setDataSet(ds);
			jtCUGOVOR.setDataSet(ds);
			jlrCPAR.setDataSet(ds);
			jlrCPJ.setDataSet(ds);
			jlrCORG.setDataSet(ds);
			jcbAKTIV.setDataSet(ds);
			jlrFRANKA.setDataSet(ds);
			jlrCNAC.setDataSet(ds);
			jlrCNACPL.setDataSet(ds);
			jlrCNAMJ.setDataSet(ds);
			jdbZIRO.setDataSet(ds);

            jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig()); 
            jlrNAZORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
		}
	}

	class UgovoriDetailPanel extends JPanel {

		JraTextMultyKolField jtfKOL = new JraTextMultyKolField() {

			public void propertyChange(PropertyChangeEvent evt) {
			}
            public void valueChanged() {
              kkalkulacija("KOLIFCIUIRAB");
            }
		};

		JraTextField jraUPRAB = new JraTextField() {
          public void valueChanged() {
            kkalkulacija("KOLIFCIUIRAB");
          }
        };

		JraTextField jraVAL_VC = new JraTextField() {
          public void valueChanged() {
            kkalkulacija("VAL_VC");
          }
        };

		JraTextField jraVAL_MC = new JraTextField() {
          public void valueChanged() {
            kkalkulacija("VAL_MC");
          }
        };

		hr.restart.zapod.jpGetValute jpgetval = new hr.restart.zapod.jpGetValute() {
			public void actionPerformedjcbValuta() {
				super.actionPerformedjcbValuta();
				myHandler();
			}

		};

		JraCheckBox jrcbStatusManip = new JraCheckBox(
				"Obraèun manipulativnih troškova");

		JraCheckBox jrcbStatusPriv = new JraCheckBox(
				"Poništenje iznosa nakon obrade");

		JraCheckBox jrcbStatusRac = new JraCheckBox("Prikaz na fakturi");

		public void myHandler() {

			rCC.setLabelLaF(jraVAL_VC, jpgetval.jcbValuta.isSelected());
			rCC.setLabelLaF(jraVAL_MC, jpgetval.jcbValuta.isSelected());

			if (!jpgetval.jcbValuta.isSelected()) {
				jraVAL_VC.getDataSet().setBigDecimal("VAL_VC",
						Aus.zero2);
				jraVAL_VC.getDataSet().setBigDecimal("VAL_MC",
						Aus.zero2);
			}

		}

		public rapancart rpcart;

		XYLayout xYLayoutDC = new XYLayout();

		XYLayout xYLayoutDC2 = new XYLayout();

		JLabel jlKOL = new JLabel();

		JLabel jlFC = new JLabel();

		JraTextField jraFC = new JraTextField() {
          public void valueChanged() {
            kkalkulacija("KOLIFCIUIRAB");
          }
        };

		JLabel jlPostotak = new JLabel();

		JraButton jbRabat = new JraButton();

		JLabel jlRABATI = new JLabel();

		JraTextField jraIPROBDP = new JraTextField() {
			public boolean isFocusTraversable() {
				return false;
			}
            public void valueChanged() {
              kkalkulacija("IPRODBP");
            }
		};

		JraTextField jraIPRODSP = new JraTextField() {
			public boolean isFocusTraversable() {
				return false;
			}
		};

		private JLabel jlPorez = new JLabel();

		JraTextField jraMANIP = new JraTextField();

		JraTextField jraIMINIZNOSBP = new JraTextField();

		private JraTextField jraPORER = new JraTextField() {
			public boolean isFocusTraversable() {
				return false;
			}
		};

		JLabel jlZAKOL = new JLabel();

		JPanel jpDetailCenter = new JPanel();

		public void setupRapancart() {
			rpcart = new rapancart(1) {
				public void nextTofocus() {
					System.out.println("nextTofocus()");
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							jtfKOL.requestFocus();
						}
					});
				}

				public void metToDo_after_lookUp() {
					System.out.println("metToDo_after_lookUp() sdfkksdfsdf");
					mymetToDo_after_lookUp();
				}

				public void jbDODTXT_actionPerformed(ActionEvent e) {
					jbDODTXT_action();
				}
			};
			rpcart.setUlazIzlaz('I');

			rpcart.setMyAfterLookupOnNavigate(false);
			rpcart.setFocusCycleRoot(true);
			rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
			// rpcart.setGodina(hr.restart.util.Valid.getValid().findYear(
			// fDI.getMasterSet().getTimestamp("DATDOK")));
			// rpcart.setCskl(fDI.getDetailSet().getString("CSKL"));
			rpcart.setDefParam();
			rpcart.setMode("N");
			rpcart.dodText();
			rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno",
					"indiCart"));

		}

		public void init() {

			jpgetval.setTecajVisible(true);
			setLayout(xYLayoutDC2);
			setBorder(BorderFactory.createEtchedBorder());
			setPreferredSize(new Dimension(660, 295));
			jpDetailCenter.setLayout(xYLayoutDC);
			xYLayoutDC.setHeight(160);
			xYLayoutDC.setWidth(660);
			jpDetailCenter.setBorder(BorderFactory.createEtchedBorder());

			jlKOL.setHorizontalAlignment(SwingConstants.RIGHT);
			jlKOL.setText("Koli\u010Dina ");
			setupRapancart();

			jtfKOL.setColumnName("KOL");
			jtfKOL.addFocusListener(new java.awt.event.FocusAdapter() {
				/*public void focusLost(FocusEvent e) {
					if (jtfKOL.isValueChanged()) {
						kkalkulacija("KOLIFCIUIRAB");
					}
				}*/

				public void focusGained(FocusEvent e) {
					// fDI.MfocusGained(e);
				}
			});

			jlFC.setText("Prodajna cijena");
			jlFC.setHorizontalAlignment(SwingConstants.RIGHT);
			jraFC.setColumnName("FC");

			jraMANIP.setColumnName("MANIPULATIVNI");
			jraIMINIZNOSBP.setColumnName("IMINIZNOSBP");
			jrcbStatusManip.setColumnName("STATUSMAN");
			jrcbStatusManip.setSelectedDataValue("D");
			jrcbStatusManip.setUnselectedDataValue("N");
			jrcbStatusManip.setHorizontalAlignment(SwingConstants.LEFT);
			jrcbStatusPriv.setColumnName("STATUSPON");
			jrcbStatusPriv.setSelectedDataValue("D");
			jrcbStatusPriv.setUnselectedDataValue("N");
			jrcbStatusPriv.setHorizontalAlignment(SwingConstants.LEFT);

			jrcbStatusRac.setColumnName("STATUSRAC");
			jrcbStatusRac.setSelectedDataValue("D");
			jrcbStatusRac.setUnselectedDataValue("N");
			jrcbStatusRac.setHorizontalAlignment(SwingConstants.LEFT);

			jraFC.addFocusListener(new java.awt.event.FocusAdapter() {
				/*public void focusLost(FocusEvent e) {
					if (jraFC.isValueChanged()) {
						kkalkulacija("KOLIFCIUIRAB");
					}
				}*/

				public void focusGained(FocusEvent e) {
					// fDI.MfocusGained(e);
				}
			});
			jlPostotak.setText("Popust (%)");
			jlPostotak.setHorizontalAlignment(SwingConstants.RIGHT);

			jraUPRAB.setColumnName("UPRAB");
			jraUPRAB.addFocusListener(new java.awt.event.FocusAdapter() {
				/*public void focusLost(FocusEvent e) {
					if (jraUPRAB.isValueChanged()) {
						kkalkulacija("KOLIFCIUIRAB");
					}
				}*/

				public void focusGained(FocusEvent e) {
					// fDI.MfocusGained(e);
				}
			});

			jbRabat.setText("...");
			jbRabat.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jbRabat_actionPerformed(e);

				}
			});
			jlRABATI.setText("Bez poreza");
			jlRABATI.setHorizontalAlignment(SwingConstants.RIGHT);
			// jraIPROBDP.setEditable(false);
			jraIPROBDP.setColumnName("IPRODBP");
			/*jraIPROBDP.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					if (jraIPROBDP.isValueChanged()) {
						kkalkulacija("IPRODBP");
					}
				}
			});*/

			jlPorez.setHorizontalAlignment(SwingConstants.RIGHT);
			jlPorez.setText("Porez (%)");

			jraPORER.setEditable(false);

			jraPORER.setColumnName("UPPOR");
			jlZAKOL.setText("S porezom");
			jlZAKOL.setHorizontalAlignment(SwingConstants.RIGHT);
			jraIPRODSP.setColumnName("IPRODSP");

			jraVAL_VC.setColumnName("VAL_VC");
			/*jraVAL_VC.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					if (jraVAL_VC.isValueChanged()) {
						kkalkulacija("VAL_VC");
					}
				}
			});*/

			jraMANIP.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					if (jraMANIP.getText().equalsIgnoreCase("")) {
						rCC.setLabelLaF(jraFC, true);
						rCC.setLabelLaF(jraIPROBDP, true);
						rCC.setLabelLaF(jtfKOL, true);
						rCC.setLabelLaF(jraUPRAB, true);
						rCC.setLabelLaF(jpgetval.jcbValuta, true);
						// jpgetval.jcbValuta.setSelected(false);
						jpgetval.actionPerformedjcbValuta();
					} else {
						rCC.setLabelLaF(jraFC, false);
						rCC.setLabelLaF(jraIPROBDP, false);
						rCC.setLabelLaF(jtfKOL, false);
						rCC.setLabelLaF(jraUPRAB, false);
						rCC.setLabelLaF(jpgetval.jcbValuta, false);
						jpgetval.jcbValuta.setSelected(false);
						jpgetval.actionPerformedjcbValuta();
						manipkalk();
					}
				}
			});

			jraVAL_MC.setColumnName("VAL_MC");
			/*jraVAL_MC.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					if (jraVAL_MC.isValueChanged()) {
						kkalkulacija("VAL_MC");
					}
				}
			});*/

			jpDetailCenter.add(jpgetval, new XYConstraints(0, 0, -1, -1));
			jpDetailCenter.add(new JLabel("Valutna cijena"), new XYConstraints(
					15, 62, 110, -1));
			jpDetailCenter.add(new JLabel("Bez poreza"), new XYConstraints(150,
					45, 110, -1));
			jpDetailCenter.add(jraVAL_VC, new XYConstraints(150, 62, 110, -1));
			jpDetailCenter.add(new JLabel("S porezom"), new XYConstraints(265,
					45, 110, -1));
			jpDetailCenter.add(jraVAL_MC, new XYConstraints(265, 62, 110, -1));
			jpDetailCenter.add(new JLabel("Min. iznos "), new XYConstraints(
					380, 45, 110, -1));
			jpDetailCenter.add(jraIMINIZNOSBP, new XYConstraints(380, 62, 110,
					-1));
			jpDetailCenter.add(new JLabel("Formula "), new XYConstraints(495,
					45, 110, -1));
			jpDetailCenter.add(jraMANIP, new XYConstraints(495, 62, 113, -1));

			jpDetailCenter.add(jlKOL, new XYConstraints(15, 85, 100, -1)); // 15,0
			jpDetailCenter.add(jtfKOL, new XYConstraints(15, 102, 100, -1));// 15,15
			jpDetailCenter.add(jlFC, new XYConstraints(120, 85, 110, -1)); // 120
			jpDetailCenter.add(jraFC, new XYConstraints(120, 102, 110, -1));// 120
			jpDetailCenter.add(jlPostotak, new XYConstraints(235, 85, 65, -1));
			jpDetailCenter.add(jraUPRAB, new XYConstraints(235, 102, 65, -1));
			jpDetailCenter.add(jlRABATI, new XYConstraints(305, 85, 110, -1));
			jpDetailCenter
					.add(jraIPROBDP, new XYConstraints(305, 102, 110, -1));
			jpDetailCenter.add(jlPorez, new XYConstraints(420, 85, 75, -1));
			jpDetailCenter.add(jraPORER, new XYConstraints(420, 102, 75, -1));
			jpDetailCenter.add(jlZAKOL, new XYConstraints(498, 85, 110, -1));
			jpDetailCenter
					.add(jraIPRODSP, new XYConstraints(500, 102, 108, -1));
			jpDetailCenter.add(jbRabat, new XYConstraints(613, 102, 21, 21));
			jpDetailCenter.add(jrcbStatusManip, new XYConstraints(15, 128, 250,
					-1));
			jpDetailCenter.add(jrcbStatusPriv, new XYConstraints(265, 128, 250,
					-1));
			jpDetailCenter.add(jrcbStatusRac, new XYConstraints(515, 128, 250,
					-1));

			add(rpcart, new XYConstraints(0, 0, 660, 135));
			add(jpDetailCenter, new XYConstraints(0, 135, 660, 160));

		}

		UgovoriDetailPanel() {
			init();

		}

		public String getGod() {
			QueryDataSet qdsgod = Util
					.getNewQueryDataSet("SELECT * FROM KNJIGOD where corg='"
							+ OrgStr.getKNJCORG() + "' and app='robno'");
			if (qdsgod.getRowCount() == 0) {
				return Util.getUtil().getYear(
						new Timestamp(System.currentTimeMillis()));
			} else {
				return qdsgod.getString("GOD");
			}
		}

		public void BindComponents(DataSet ds) {
			rpcart.setTabela((QueryDataSet) ds);
			rpcart.setGodina(getGod());
			rpcart.InitRaPanCart();
			rpcart.setAllowUsluga(true);
			jtfKOL.setDataSet(ds);
			jraFC.setDataSet(ds);
			jraUPRAB.setDataSet(ds);
			jraIPROBDP.setDataSet(ds);
			jraIPRODSP.setDataSet(ds);
			jraPORER.setDataSet(ds);
			jpgetval.setRaDataSet(ds);
			jraVAL_VC.setDataSet(ds);
			jraVAL_MC.setDataSet(ds);
			jraMANIP.setDataSet(ds);
			jraIMINIZNOSBP.setDataSet(ds);

			jrcbStatusManip.setDataSet(ds);
			jrcbStatusPriv.setDataSet(ds);
			jrcbStatusRac.setDataSet(ds);

		}

		public void disableS() {
			rCC.setLabelLaF(jraPORER, false);
			rCC.setLabelLaF(jraIPRODSP, false);
			// rCC.setLabelLaF(jraUPRAB, false);
		}

		public void mymetToDo_after_lookUp() {
		}

		public void kkalkulacija(String name) {
		}

		public void jbRabat_actionPerformed(ActionEvent e) {
		}

		public void jbDODTXT_action() {
System.out.println("unišo ...");
			if (rpcart.getCART().equalsIgnoreCase("")) {
				JOptionPane
						.showConfirmDialog(
								this,
								"Artikl mora biti upisan da bi se mogao unijeti dodatni tekst",
								"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE);
				return;
			} else if (!rpcart.isUsluga()) {
				JOptionPane
						.showConfirmDialog(
								this,
								"Artikl mora biti tipa usluge da bi se mogao unijeti dodatni tekst",
								"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Aus.dumpClassName(this);
			frmDodatniTxt dtx = new frmDodatniTxt() {
				public void stoakojesnimio(QueryDataSet vtt) {
					vtsnimio(vtt);
				}

				public void stoakonijesnimio(QueryDataSet vtt) {
					 vtsnimio(vtt);
				}
			};
			dtx.setUP(this.getTopLevelAncestor(), getDetailSet(),
					this.getLocation());
		}

		public void manipkalk() {
		}

	}
	
	public void vtsnimio(QueryDataSet vtt){
		this.vtt = vtt;
		ST.prn(this.vtt);
	}

	QueryDataSet repQDS = new QueryDataSet();

	public QueryDataSet getRepSet() {
		return repQDS;
	}

	private String getQueryString() {
		String qs = "SELECT * FROM Ugovori, stugovor WHERE ugovori.cugovor = stugovor.cugovor "
				+ " AND Ugovori.cugovor = '"
				+ this.getMasterSet().getString("CUGOVOR") + "'"
                + " AND Ugovori.knjig = '"
                + this.getMasterSet().getString("KNJIG") + "'"
;
		// System.out.println(qs);
		return qs;
	}

	public void Funkcija_ispisa_detail() {
		System.out.println("--------------------detail-----------------------"); // XDEBUG
		// delete
		// when
		// no
		// more
		// needed
		repQDS.close();
		repQDS.closeStatement();
		repQDS
				.setQuery(new QueryDescriptor(dm.getDatabase1(),
						getQueryString()));
		repQDS.open();
		super.Funkcija_ispisa_detail();
	}

	public void Funkcija_ispisa_master() {
		System.out.println("--------------------master-----------------------"); // XDEBUG
		// delete
		// when
		// no
		// more
		// needed
		repQDS.close();
		repQDS.closeStatement();
		repQDS
				.setQuery(new QueryDescriptor(dm.getDatabase1(),
						getQueryString()));
		repQDS.open();
		super.Funkcija_ispisa_master();
	}
	public static void fillNazPar() {
	  QueryDataSet ug = Ugovori.getDataModule().getTempSet();
	  QueryDataSet pp = Partneri.getDataModule().getTempSet();
	  ug.open();
	  pp.open();
	  for (ug.first(); ug.inBounds(); ug.next()) {
      if (lookupData.getlookupData().raLocate(pp, "CPAR", ug.getInt("CPAR")+"")) {
        ug.setString("NAZPAR", pp.getString("NAZPAR"));
        ug.post();
      }
    }
	  ug.saveChanges();
	}
}
