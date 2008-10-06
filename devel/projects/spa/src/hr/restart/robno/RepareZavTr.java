/****license*****************************************************************
**   file: RepareZavTr.java
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
 * Created on 2004.12.17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.raCommonClass;
import hr.restart.util.raProcess;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RepareZavTr extends raUpitLite {

	JPanel mainPanel = new JPanel();

	JCheckBox zavtroskovi = new JCheckBox(
			"Provjera/popravak zavisnih troškova za PRK i KAL");

	JCheckBox primke = new JCheckBox(
			"Provjera/popravak PRI prema odgovarajuæim KAL");

	JCheckBox popveze = new JCheckBox(
			"Provjera/popravak kljuèeva stavaka stdok(U)");

	JCheckBox popvtprijenos = new JCheckBox(
			"Provjera/popravak veza stdoku prema VTprijenos tabeli");

	JCheckBox popvtprijenos1 = new JCheckBox(
			"Popravak veæ prije pronaðenih grešaka (opcijom 2)");

	JCheckBox popvezestdoki = new JCheckBox(
			"Provjera/popravak kljuèeva stavaka stdok(I)");
	
	JCheckBox popvezeRACOTP = new JCheckBox(
	"Provjera/popravak veza RAC i GRN sa OTP");

	JCheckBox popvezeRACOTPVTZ = new JCheckBox(
	"Provjera/popravak veza RAC i GRN sa OTP na nivou zaglavlja i VTPrijenosa");
	


	ArrayList al = new ArrayList();

	public RepareZavTr() {
		try {
			jbInit();
		} catch (Exception ex) {
		}
	}

	private void jbInit() throws Exception {

		al.clear();
		XYLayout xYLayout1 = new XYLayout();
		mainPanel.setLayout(xYLayout1);
		this.setJPan(mainPanel);
		xYLayout1.setWidth(595);
		
		al.add(zavtroskovi);
		al.add(primke);
		al.add(popveze);
		al.add(popvtprijenos);
		al.add(popvtprijenos1);
		al.add(popvezestdoki);
		al.add(popvezeRACOTP);
		al.add(popvezeRACOTPVTZ);
		int offset = 15;
		int razmak = 30;
		xYLayout1.setHeight(offset+razmak*al.size());		
		for (int i= 0;i<al.size();i++){
			mainPanel.add(((JCheckBox) al.get(i)), new XYConstraints(15, razmak*i+offset, -1, -1));
		}
/*		
		mainPanel.add(zavtroskovi, new XYConstraints(15, 15, -1, -1));
		mainPanel.add(primke, new XYConstraints(15, 45, -1, -1));
		mainPanel.add(popveze, new XYConstraints(15, 75, -1, -1));
		mainPanel.add(popvtprijenos, new XYConstraints(15, 105, -1, -1));
		mainPanel.add(popvtprijenos1, new XYConstraints(15, 135, -1, -1));
		mainPanel.add(popvezestdoki, new XYConstraints(15, 165, -1, -1));
		mainPanel.add(popvezeRACOTP, new XYConstraints(15, 195, -1, -1));
*/		
	}

	private dM dm = dM.getDataModule();

	sysoutTEST ST = new sysoutTEST(false);

	public StorageDataSet initGreskaSet() {
		StorageDataSet greska = new StorageDataSet();
		Column col = dm.getDoki().getColumn("OPIS").cloneColumn();
		col.setWidth(2000);
		greska.setColumns(new Column[] { col });
		return greska;
	}

	public StorageDataSet popravakPrimki(boolean popravak) {
		StorageDataSet greska = initGreskaSet();
		greska.open();
		QueryDataSet qds = Util.getNewQueryDataSet(
				"SELECT * FROM STDOKU WHERE VRDOK IN ('KAL')", true);
		QueryDataSet primka = null;

		for (qds.first(); qds.inBounds(); qds.next()) {
			primka = Util.getNewQueryDataSet(
					"SELECT * FROM STDOKU WHERE VEZA='"
							+ qds.getString("ID_STAVKA") + "'", true);

			if (primka.getRowCount() > 1) {
				greska.insertRow(false);
				greska.setString("OPIS",
						"Ima više stavaka primki za ovu kalkulaciju "
								+ primka.getString("ID_STAVKA"));
				continue;
			}

			if (primka.getRowCount() == 0) {
				greska.insertRow(false);
				greska.setString("OPIS",
						"Postoji veza na primku ali ne postoji ista za ovu kalkulaciju "
								+ qds.getString("ID_STAVKA"));
				qds.setString("VEZA", "ERROR");
				//System.out.println("Eroriram opciju
				// primka.getRowCount()==0");
				qds.saveChanges();
				continue;
			}

			if (primka.getInt("CART") != qds.getInt("CART")) {
				greska.insertRow(false);
				greska.setString("OPIS", "Razlièiti artikli u primci "
						+ primka.getString("ID_STAVKA") + " i kalkulaciji "
						+ qds.getString("ID_STAVKA"));
				qds.setString("VEZA", "ERROR");
				qds.setString("VEZA", "ERROR");
				primka.setString("VEZA", "ERROR");
				qds.saveChanges();
				primka.saveChanges();

				System.out
						.println("Eroriram opciju primka.getInt(CART)!=qds.getInt(CART)");

				continue;
			}
			if (!primka.getString("CSKL").equalsIgnoreCase(
					qds.getString("CSKLART"))) {
				greska.insertRow(false);
				greska.setString("OPIS", "Razlièita skladišta u primci "
						+ primka.getString("ID_STAVKA") + " i kalkulaciji "
						+ qds.getString("ID_STAVKA"));
				qds.setString("VEZA", "ERROR");
				primka.setString("VEZA", "ERROR");
				qds.saveChanges();
				primka.saveChanges();
				continue;
			}

			for (primka.first(); primka.inBounds(); primka.next()) {
				if (qds.getBigDecimal("INAB").compareTo(
						primka.getBigDecimal("INAB")) == 0) {
					continue;
				}
				primka.setBigDecimal("NC", qds.getBigDecimal("NC"));
				primka.setBigDecimal("VC", qds.getBigDecimal("VC"));				
				primka.setBigDecimal("ZC", qds.getBigDecimal("ZC"));
				primka.setBigDecimal("MC", qds.getBigDecimal("MC"));
				primka.setBigDecimal("INAB", qds.getBigDecimal("INAB"));
				primka.setBigDecimal("IZAD", qds.getBigDecimal("IZAD"));
				primka.setBigDecimal("IBP", qds.getBigDecimal("IBP"));
				primka.setBigDecimal("ISP", qds.getBigDecimal("ISP"));				
				greska.insertRow(false);
				greska.setString("OPIS", "Kalkulacija "
						+ primka.getString("ID_STAVKA") + " "
						+ (popravak ? "Popravljeno" : ""));

				// ovo treba malo uljepšati !!!
			}
			primka.saveChanges();
		}

		return greska;
	}

	public StorageDataSet repareZavTr(boolean popravak) {
		StorageDataSet greska = initGreskaSet();
		greska.open();
		QueryDataSet qds = Util.getNewQueryDataSet(
				"SELECT * FROM STDOKU WHERE VRDOK IN ('KAL','PRK')", true);
		QueryDataSet zavtr = null;
		BigDecimal izad = Aus.zero2;
		for (qds.first(); qds.inBounds(); qds.next()) {
			zavtr = Util.getNewQueryDataSet("SELECT * FROM VTZTR WHERE CSKL='"
					+ qds.getString("CSKL") + "' " + "AND GOD = '"
					+ qds.getString("GOD") + "' " + "AND VRDOK='"
					+ qds.getString("VRDOK") + "' " + "AND BRDOK="
					+ qds.getInt("BRDOK") + " " + "AND RBR = "
					+ qds.getShort("RBR"), true);
			if (zavtr.getRowCount()==0){
				System.out.println("Preskacem "+qds.getInt("BRDOK"));
				continue;
			}
			System.out.println("NE preskacem "+qds.getInt("BRDOK"));
			for (zavtr.first(); zavtr.inBounds(); zavtr.next()) {
				izad = izad.add(zavtr.getBigDecimal("IZT"));
			}

			if (izad.compareTo(qds.getBigDecimal("IZT")) != 0) {
				greska.insertRow(false);
				greska.setString("OPIS", "Greška IZT("
						+ qds.getBigDecimal("IZT") + " != IZT(VtZTR)(" + izad
						+ ")" + (popravak ? "Popravljeno" : ""));
				if (popravak) {

					qds.setBigDecimal("IZT", izad);
					qds.setBigDecimal("INAB", qds.getBigDecimal("IZT").add(
							qds.getBigDecimal("IDOB")).subtract(
							qds.getBigDecimal("IRAB")));
					qds.setBigDecimal("IZAD", qds.getBigDecimal("INAB"));

				}
			}
			izad = Aus.zero2;
		}
		if (popravak) {
			qds.saveChanges();
		}
		if (greska.getRowCount() == 0) {
			greska.insertRow(false);
			greska.setString("OPIS", "Nije bilo grešaka");
		}
		return greska;
	}
	
	public StorageDataSet popravakVTprijenosVezaRACGRNOTPZag(boolean popravak) {
		StorageDataSet greska = initGreskaSet();
		greska.open();
		QueryDataSet qds = Util.getNewQueryDataSet(
				"SELECT * FROM VTprijenos WHERE keydest like '%OTP%'", true);
		int count = Util.getNewQueryDataSet(
				"SELECT count(*) as brojac FROM VTprijenos WHERE keydest like '%OTP%'", true).getInt(
				"BROJAC");

        int brojac = 0;					
		
		QueryDataSet otpremnica = null;
		QueryDataSet racun = null;
		for (qds.first(); qds.inBounds(); qds.next()) {
			otpremnica = Util.getNewQueryDataSet(
					"SELECT * FROM doki WHERE "+
					"cskl||'-'||vrdok||'-'||god||'-'||brdok||'-' ='"+qds.getString("KEYDEST")+"'", true);
			racun = Util.getNewQueryDataSet(
					"SELECT * FROM doki WHERE "+
					"cskl||'-'||vrdok||'-'||god||'-'||brdok||'-' ='"+qds.getString("KEYSRC")+"'", true);
			
			if (otpremnica.getRowCount()==0){
				greska.insertRow(false);
				greska.setString("OPIS", "Ne postoji otpremnica koja se nalazi u VTPrijenos"
						+ qds.getString("KEYDEST") + ")" +
						(popravak ? "Popravljeno" : ""));
				if (popravak) {
					qds.deleteRow();
					qds.saveChanges();
				}
				continue;
			}
			if (racun.getRowCount()==0){
				greska.insertRow(false);
				greska.setString("OPIS", "Ne postoji raèun koja se nalazi u VTPrijenos"
						+ qds.getString("KEYSRC") + ")" +
						(popravak ? "Popravljeno" : ""));
				if (popravak) {
					qds.deleteRow();
					qds.saveChanges();
				}
				continue;
			}
	        if (!racun.isAssignedNull("CPAR")){
				if (racun.getInt("CPAR")!=otpremnica.getInt("CPAR")){
					greska.insertRow(false);
					greska.setString("OPIS", "Partner na otpremnici ("
							+ qds.getString("KEYSRC") + ") i raèunu ("+ qds.getString("KEYSRC") + ") je razlièit "+ 
							(popravak ? "Popravljeno" : ""));
					if (popravak) {
						qds.deleteRow();
						qds.saveChanges();
					}
					continue;
				}
	        }

	        if (!racun.isAssignedNull("CKUPAC")){
				if (racun.getInt("CKUPAC")!=otpremnica.getInt("CKUPAC")){
					greska.insertRow(false);
					greska.setString("OPIS", "Kupac na otpremnici ("
							+ qds.getString("KEYSRC") + ") i raèunu ("+ qds.getString("KEYSRC") + ") je razlièit "+ 
							(popravak ? "Popravljeno" : ""));
					if (popravak) {
						qds.deleteRow();
						qds.saveChanges();
					}
					continue;
				}
	          }			
			
			if (racun.getTimestamp("DATDOK").compareTo(otpremnica.getTimestamp("DATDOK"))!=0){
				greska.insertRow(false);
				greska.setString("OPIS", "Datumi otpremnice ("
						+ qds.getString("KEYSRC") + ") i raèuna ("+ qds.getString("KEYSRC") + ") su razlièiti "+ 
						(popravak ? "Popravljeno" : ""));
//				if (popravak) {
//					qds.deleteRow();
//					qds.saveChanges();
//				}
//				continue;
			}

			if (racun.getTimestamp("SYSDAT").compareTo(otpremnica.getTimestamp("SYSDAT"))!=0){
				greska.insertRow(false);
				greska.setString("OPIS", "Systemski datumi otpremnice ("
						+ qds.getString("KEYSRC") + ") i raèuna ("+ qds.getString("KEYSRC") + ") su razlièiti "+ 
						(popravak ? "Popravljeno" : ""));
//				if (popravak) {
//					qds.deleteRow();
//					qds.saveChanges();
//				}
//				continue;
			}
			
			brojac++;
			raProcess.setMessage("Obraðeno " + brojac + "/" + count, true);			
		}
		
		if (greska.getRowCount() == 0) {
			greska.insertRow(false);
			greska.setString("OPIS", "Nije bilo grešaka");
		}
		return greska;

	}
	
	public StorageDataSet popravakVTprijenosVezaRACGRNOTP(boolean popravak) {
		System.gc();		
		StorageDataSet greska = initGreskaSet();
		greska.open();
		QueryDataSet qds = Util.getNewQueryDataSet(
				"SELECT * FROM STDOKI WHERE VRDOK IN ('OTP')", true);
		QueryDataSet veznastavka = null; 
		QueryDataSet vtprijenos = null;
		String sveza = "";
		int count = Util.getNewQueryDataSet(
				"SELECT count(*) as brojac FROM STDOKI WHERE VRDOK IN ('OTP')", true).getInt(
				"BROJAC");

        int brojac = 0;		
		for (qds.first(); qds.inBounds(); qds.next()) {
			if (qds.getString("VEZA")==null){
				qds.setString("VEZA","");
				qds.saveChanges();
			}
			
			if (!qds.getString("VEZA").equalsIgnoreCase("") && 
				!qds.getString("VEZA").equalsIgnoreCase("ERROR")){
				veznastavka = Util.getNewQueryDataSet("SELECT * FROM STDOKI "+
						"WHERE ID_STAVKA IN ('"+qds.getString("VEZA")+"')",true);
				if (veznastavka.getRowCount()==0){
					greska.insertRow(false);
					greska.setString("OPIS", "Nepostojeca veza ("
							+ qds.getString("ID_STAVKA") + ")" +
							(popravak ? "Popravljeno" : ""));
					if (popravak) {
						qds.setString("VEZA","ERROR");
						qds.saveChanges();
					}					
				} else if (veznastavka.getRowCount()>1){
					greska.insertRow(false);
					greska.setString("OPIS", "Za ovaj dokument postoji više vezanih stavaka ("
							+ qds.getString("ID_STAVKA") + ")" +
							(popravak ? "Popravljeno" : ""));
					if (popravak) {
						qds.setString("VEZA","ERROR");
						qds.saveChanges();
					}					
				} else {
					if (qds.getInt("CART") != veznastavka.getInt("CART")){
						greska.insertRow(false);
						greska.setString("OPIS", 
								"Razlièiti artikli na vezanim dokumentima ("
								+ qds.getString("ID_STAVKA") + ") " +
								(popravak ? "Popravljeno" : ""));
						if (popravak) {
							qds.setString("VEZA","ERROR");
							qds.saveChanges();
						}						
					} else if (qds.getBigDecimal("KOL").compareTo(veznastavka.getBigDecimal("KOL"))!=0){
						greska.insertRow(false);
						greska.setString("OPIS", 
								"Razlièite kolièine na vezanim dokumentima ("
								+ qds.getString("ID_STAVKA") + ") " +
								(popravak ? "Popravljeno" : ""));
						if (popravak) {
							qds.setString("VEZA","ERROR");
							qds.saveChanges();
						}						
					}else if (!qds.getString("CSKL").equalsIgnoreCase(veznastavka.getString("CSKLART"))){
					greska.insertRow(false);
					greska.setString("OPIS", 
							"Razlièita skladišta na vezanim dokumentima ("
							+ qds.getString("ID_STAVKA") + ") " +
							(popravak ? "Popravljeno" : ""));
					if (popravak) {
						qds.setString("VEZA","ERROR");
						qds.saveChanges();
					}						
				}					
				} 
			} else {
				vtprijenos = Util.getNewQueryDataSet(
							"SELECT * FROM VTPRIJENOS WHERE KEYDEST in ('"+
							raControlDocs.getKey(qds,new String[] {"cskl","vrdok","god","brdok"},"stdoki")
							+"')",true);
					if (vtprijenos.getRowCount()==0){
						greska.insertRow(false);
						greska.setString("OPIS", 
								"Nisam nasao u vtprijenosu ("
								+ qds.getString("ID_STAVKA") + ") " +
								(popravak ? "Popravljeno" : ""));
						if (popravak) {
							qds.setString("VEZA","");
							qds.saveChanges();
						}						
					} else {
						
						veznastavka = Util.getNewQueryDataSet("select * from stdoki "+
								"WHERE id_stavka like '"+vtprijenos.getString("KEYSRC")+"%'",true);
						
						if (veznastavka.getRowCount()==0){
							greska.insertRow(false);
							greska.setString("OPIS", 
									"Krivi upis u VTPrijenos ("
									+ vtprijenos.getString("KEYSRC") + ") ");
						}
						
						for (veznastavka.first();veznastavka.inBounds();veznastavka.next()){
							if (veznastavka.getInt("CART")==qds.getInt("CART") &&
								veznastavka.getBigDecimal("KOL").compareTo(qds.getBigDecimal("KOL"))==0 &&
								veznastavka.getString("CSKLART").equalsIgnoreCase(qds.getString("CSKL"))){
								greska.insertRow(false);
								greska.setString("OPIS", 
										"Pronaðena veza ("
										+ qds.getString("ID_STAVKA") + ") " +
										(popravak ? "Popravljeno" : ""));
								if (popravak) {
									qds.setString("VEZA",veznastavka.getString("ID_STAVKA"));
									veznastavka.setString("VEZA",qds.getString("ID_STAVKA"));									
									qds.saveChanges();
									veznastavka.saveChanges();
								}						
								break;
							}	
						}
					}
			}
			brojac++;
			raProcess.setMessage("Obraðeno " + brojac + "/" + count, true);			
		}
		if (greska.getRowCount() == 0) {
			greska.insertRow(false);
			greska.setString("OPIS", "Nije bilo grešaka");
		}
		qds = null;
		veznastavka = null;
		vtprijenos = null;

		return greska;
	}
	public StorageDataSet popravakVTprijenosVeza(boolean popravak) {
		StorageDataSet greska = initGreskaSet();
		greska.open();
		QueryDataSet qds = Util.getNewQueryDataSet(
				"SELECT * FROM STDOKU WHERE VRDOK IN ('KAL','PRK')", true);
		QueryDataSet vtprijenos = null;
		String sveza = "";
		for (qds.first(); qds.inBounds(); qds.next()) {
			if (qds.getString("VRDOK").equalsIgnoreCase("KAL")) {
				vtprijenos = Util.getNewQueryDataSet(
						"SELECT * FROM VTPRIJENOS WHERE KEYDEST = '"
								+ qds.getString("ID_STAVKA") + "'", true);
				sveza = vtprijenos.getString("KEYSRC");
			} else if (qds.getString("VRDOK").equalsIgnoreCase("PRI")) {
				vtprijenos = Util.getNewQueryDataSet(
						"SELECT * FROM VTPRIJENOS WHERE KEYSRC = '"
								+ qds.getString("ID_STAVKA") + "'", true);
				sveza = vtprijenos.getString("KEYDEST");
			} else {
				greska.insertRow(false);
				greska.setString("OPIS", "Ne postojeæi dokument "
						+ qds.getString("ID_STAVKA"));
				continue;
			}
			
			if (vtprijenos.getRowCount() > 1) {
				greska.insertRow(false);
				greska.setString("OPIS",
						"Postoji više kljuèeva u VTPrijenos tabeli za ovaj kljuè "
								+ qds.getString("ID_STAVKA"));
				continue;
			}
			if (vtprijenos.getRowCount() == 0) {
				sveza = "";
			}

			if (!sveza.equals(qds.getString("VEZA"))) {
				greska.insertRow(false);
				greska.setString("OPIS", "Kriva veza ("
						+ qds.getString("ID_STAVKA") + ")stara veza = "
						+ qds.getString("ID_STAVKA") + " nova veza " + sveza
						+ " --- " + (popravak ? "Popravljeno" : ""));
				if (popravak) {
					qds.setString("VEZA", sveza);
				}
			}
		}

		if (popravak) {
			qds.saveChanges();
		}
		if (greska.getRowCount() == 0) {
			greska.insertRow(false);
			greska.setString("OPIS", "Nije bilo grešaka");
		}

		return greska;

	}

	public StorageDataSet popravakIDSTAVKE(boolean popravak, boolean ulaz) {
		StorageDataSet greska = initGreskaSet();
		greska.open();
		QueryDataSet qds = null;
		int count = 0;
		if (ulaz) {
			qds = Util.getNewQueryDataSet("SELECT * FROM STDOKU", true);
			count = Util.getNewQueryDataSet(
					"SELECT count(*) as brojac FROM STDOKU", true).getInt(
					"BROJAC");
		} else {
			qds = Util.getNewQueryDataSet("SELECT * FROM STDOKI", true);
			count = Util.getNewQueryDataSet(
					"SELECT count(*) as brojac FROM STDOKI", true).getInt(
					"BROJAC");

		}
		String kljuch = "";
		int brojac = 1;
		for (qds.first(); qds.inBounds(); qds.next()) {
			raProcess.setMessage("Obraðeno " + brojac + "/" + count, true);
			kljuch = raControlDocs.getKey(qds);
			if (!kljuch.equals(qds.getString("ID_STAVKA"))) {
				greska.insertRow(false);
				greska.setString("OPIS", "Greška neispravni kljuchevi tabela "
						+ "Neispravni(" + qds.getString("ID_STAVKA") + ") "
						+ "Ispravni(" + kljuch + ") "
						+ (popravak ? "Popravljeno" : ""));
				if (popravak) {
					qds.setString("ID_STAVKA", kljuch);
				}
			}
			brojac++;
		}

		if (popravak) {
			raProcess.setMessage("Snimanje promjena", true);
			qds.saveChanges();
		}
		if (greska.getRowCount() == 0) {
			greska.insertRow(false);
			greska.setString("OPIS", "Nije bilo grešaka");
		}
		return greska;

	}

	public boolean Validacija() {

		int opt =howMeniIsSelected(); 
		if (opt < 1) {
			javax.swing.JOptionPane.showConfirmDialog(this,
					"Nije odabrana niti jedna opcija !", "Greška",
					javax.swing.JOptionPane.DEFAULT_OPTION,
					javax.swing.JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (opt > 1) {
			javax.swing.JOptionPane.showConfirmDialog(this,
					"Smije se odabrati samo jedna operacija !", "Greška",
					javax.swing.JOptionPane.DEFAULT_OPTION,
					javax.swing.JOptionPane.ERROR_MESSAGE);
			
			return false;
		}

		return true;
	}

	public void okPress() {

		if (zavtroskovi.isSelected()) {
			ST.showInFrame(repareZavTr(true), "Greške i obavijesti");
		} else if (primke.isSelected()) {
			ST.showInFrame(popravakPrimki(true), "Greške i obavijesti");
		} else if (popveze.isSelected()) {
			ST.showInFrame(popravakIDSTAVKE(true, true),
					"Greške u kljuèevima stavaka");
		} else if (popvtprijenos.isSelected()) {
			ST.showInFrame(popravakVTprijenosVeza(true),
					"Greške u vezi VTprijenos - KAL - PRI");
		} else if (popvtprijenos1.isSelected()) {
			ST.showInFrame(popravakERRORVEZE(true),
					"Greške u vezi VTprijenos - KAL - PRI (2)");
		} else if (popvezestdoki.isSelected()) {
			ST.showInFrame(popravakIDSTAVKE(true, false),
					"Greške u kljuèevima stavaka stdoki");
		} else if (popvezeRACOTP.isSelected()){
			ST.showInFrame(popravakVTprijenosVezaRACGRNOTP(true),
			"Greške u vezi VTprijenos - RAC (GRN) - OTP (1)");
		} else if (popvezeRACOTPVTZ.isSelected()){
			ST.showInFrame(popravakVTprijenosVezaRACGRNOTPZag(true),
			"Greške u vezi VTprijenos - RAC (GRN) - OTP (1) nivo zaglavlja");

			
		}
			
		
	}

	private int vratiDokuCPAR(QueryDataSet qds) {

		return Util.getNewQueryDataSet(
				"SELECT * FROM DOKU WHERE CSKL='" + qds.getString("CSKL")
						+ "' AND VRDOK = '" + qds.getString("VRDOK") + "' AND "
						+ "GOD='" + qds.getString("GOD") + "' AND BRDOK="
						+ qds.getInt("BRDOK"), true).getInt("CPAR");

	}

	private void iscalcPrimke() {

		QueryDataSet errorPrimke = Util.getNewQueryDataSet(
				"SELECT * FROM STDOKU WHERE VRDOK='PRI'", true);
		QueryDataSet kalk = null;
		for (errorPrimke.first(); errorPrimke.inBounds(); errorPrimke.next()) {
			kalk = Util.getNewQueryDataSet(
					"SELECT * FROM STDOKU WHERE ID_STAVKA='"
							+ errorPrimke.getString("VEZA") + "'", true);
			if (kalk.getRowCount() != 1) {
				errorPrimke.setString("VEZA", "ERROR");
				continue;
			}

			if (kalk.getInt("CART") != errorPrimke.getInt("CART")) {
				errorPrimke.setString("VEZA", "ERROR");
				continue;
			}
		}
		errorPrimke.saveChanges();

	}

	public StorageDataSet popravakERRORVEZE(boolean popravak) {
		StorageDataSet greska = initGreskaSet();
		greska.open();
		iscalcPrimke();
		QueryDataSet errorKalkulacije = Util
				.getNewQueryDataSet(
						"SELECT * FROM STDOKU WHERE VEZA='ERROR' AND VRDOK='KAL'",
						true);
		QueryDataSet errorPrimke = null;

		for (errorKalkulacije.first(); errorKalkulacije.inBounds(); errorKalkulacije
				.next()) {
			errorPrimke = Util.getNewQueryDataSet(
					"SELECT * FROM STDOKU WHERE VEZA='ERROR' AND VRDOK='PRI' "
							+ "AND CART=" + errorKalkulacije.getInt("CART"),
					true);
			if (errorPrimke.getRowCount() == 0) {
				greska.insertRow(false);
				greska.setString("OPIS",
						"Ne mogu popraviti jer ne mogu naci niti jednu primku za "
								+ errorKalkulacije.getString("ID_STAVKA"));
				continue;
			}
			boolean isOK = false;
			if (errorPrimke.getRowCount() == 1) {
				// provjera kolièine
				isOK = true;
				if (errorPrimke.getBigDecimal("KOL").compareTo(
						errorKalkulacije.getBigDecimal("KOL")) != 0) {
					greska.insertRow(false);
					greska.setString("OPIS",
							"Ne mogu popraviti jer jedini koji je potencijalan ima razlièitu kolièinu  "
									+ errorKalkulacije.getString("ID_STAVKA"));
					isOK = false;
				}
				if (vratiDokuCPAR(errorKalkulacije) != vratiDokuCPAR(errorPrimke)) {
					greska.insertRow(false);
					greska.setString("OPIS",
							"Ne mogu popraviti jer jedini koji je potencijalan ima razlièitog partnera  "
									+ errorKalkulacije.getString("ID_STAVKA"));
					isOK = false;
				}
			}

			if (errorPrimke.getRowCount() > 1) {
				for (errorPrimke.first(); errorPrimke.inBounds(); errorPrimke
						.next()) {
					if (errorPrimke.getBigDecimal("KOL").compareTo(
							errorKalkulacije.getBigDecimal("KOL")) != 0) {
						continue;
					}
					if (vratiDokuCPAR(errorKalkulacije) != vratiDokuCPAR(errorPrimke)) {
						continue;
					}
					isOK = true;
					break;
				}
			}

			if (isOK && popravak) {
				greska.insertRow(false);
				greska.setString("OPIS", "Popravljam  "
						+ errorKalkulacije.getString("ID_STAVKA") + " sa "
						+ errorPrimke.getString("ID_STAVKA"));

				errorPrimke.setString("VEZA", errorKalkulacije
						.getString("ID_STAVKA"));
				errorKalkulacije.setString("VEZA", errorPrimke
						.getString("ID_STAVKA"));
				QueryDataSet vtpd = Util
						.getNewQueryDataSet(
								"SELECT * FROM VTPRIJENOS WHERE KEYDEST='"
										+ errorKalkulacije
												.getString("ID_STAVKA") + "'",
								true);
				vtpd.deleteAllRows();
				vtpd.saveChanges();
				QueryDataSet vtpd1 = Util.getNewQueryDataSet(
						"SELECT * FROM VTPRIJENOS WHERE KEYSRC='"
								+ errorPrimke.getString("ID_STAVKA") + "'",
						true);
				vtpd1.deleteAllRows();
				vtpd1.saveChanges();
				errorKalkulacije.saveChanges();
				errorPrimke.saveChanges();
				vtpd1.insertRow(false);
				vtpd1.setString("KEYDEST", errorKalkulacije
						.getString("ID_STAVKA"));
				vtpd1.setString("KEYSRC", errorPrimke.getString("ID_STAVKA"));
				vtpd1.saveChanges();
			}
		}
		if (greska.getRowCount() == 0) {
			greska.insertRow(false);
			greska.setString("OPIS", "Nije bilo grešaka");
		}
		return greska;
	}

	public boolean runFirstESC() {
		return true;
	}

	public void firstESC() {
		componentShow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.restart.util.raUpitLite#componentShow()
	 */
	public void componentShow() {
		zavtroskovi.setSelected(false);
		primke.setSelected(false);
		popveze.setSelected(false);
		popvtprijenos.setSelected(false);
		raCommonClass.getraCommonClass().EnabDisabAllLater(getJPan(), true);
	}

	public boolean isIspis() {
		return false;
	}

	public boolean ispisNow() {
		return false;
	}

	public void afterOKPress() {
		javax.swing.JOptionPane.showConfirmDialog(this,
				"Sistemska operacija je završena !", "Poruka",
				javax.swing.JOptionPane.DEFAULT_OPTION,
				javax.swing.JOptionPane.DEFAULT_OPTION);

		System.gc();
		raCommonClass.getraCommonClass().EnabDisabAllLater(getJPan(), true);
		globalSelect(false);
	}

	public int howMeniIsSelected() {
		int sel = 0;
		for (int i = 0; i < al.size(); i++) {
			sel = sel + (((JCheckBox) al.get(i)).isSelected() ? 1 : 0);
		}

		return sel;
	}

	public void globalSelect(boolean how) {
		for (int i = 0; i < al.size(); i++) {
			((JCheckBox) al.get(i)).setSelected(how);
		}
	}
}