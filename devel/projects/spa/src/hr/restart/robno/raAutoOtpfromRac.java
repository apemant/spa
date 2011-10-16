/****license*****************************************************************
**   file: raAutoOtpfromRac.java
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

import hr.restart.baza.SEQ;
import hr.restart.baza.VTprijenos;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;

import java.util.HashMap;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raAutoOtpfromRac {

	private QueryDataSet zaglavlje_rac;

	private QueryDataSet stavke_rac;

	private QueryDataSet stanje;

	private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(
			false);

	private QueryDataSet zaglavlja_otp;

	private QueryDataSet stavke_otp;

	private QueryDataSet stanja;

	private QueryDataSet mySEQ;
	private QueryDataSet vtPrijenos;

	private raPrenosVT rPVT = new raPrenosVT();

	private hr.restart.util.lookupData lD = hr.restart.util.lookupData
			.getlookupData();

	private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	private HashMap hmrbr = new HashMap();

	private hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

	private raControlDocs rCD = new raControlDocs();

	private raKalkulBDDoc rKD = new raKalkulBDDoc();

	private TypeDoc TD = TypeDoc.getTypeDoc();

	private hr.restart.util.LinkClass lc = hr.restart.util.LinkClass
			.getLinkClass();

	private StorageDataSet greske = new StorageDataSet();
	
	boolean isMinusAllowed = false;
	
	String ncskl = "CSKLART";

	public raAutoOtpfromRac() {
	}

	public void init() {
		zaglavlja_otp = hr.restart.util.Util.getNewQueryDataSet(
				"select * from doki where 1=0", true);
		stavke_otp = hr.restart.util.Util.getNewQueryDataSet(
				"select * from stdoki where 1=0", true);
		mySEQ = SEQ.getDataModule().copyDataSet(); 
          /*hr.restart.util.Util.getNewQueryDataSet("select * from seq",
				true);*/
		vtPrijenos = VTprijenos.getDataModule().getTempSet("1=0");
        vtPrijenos.open();
/*          hr.restart.util.Util.getNewQueryDataSet("select * from VTprijenos",
				true);*/
        
      isMinusAllowed = frmParam.getParam("robno", "allowMinus", "N",
        "Dopustiti odlazak u minus na izlazima (D,N)?").equals("D");
	}

	protected boolean smijem_li_dalje() {

		if (!stavke_rac.getString("STATUS").equalsIgnoreCase("N")) {
			return false;
		}
		if (stavke_rac.getString(ncskl).equalsIgnoreCase("")) {
			return false;
		}
		return raVart.isStanje(stavke_rac.getInt("CART"));
		/*QueryDataSet arts = hr.restart.util.Util
				.getNewQueryDataSet("select * from artikli where cart="
						+ stavke_rac.getInt("CART"), true);
		if (arts.getRowCount() == 1) {
			if (arts.getString("VRART").equalsIgnoreCase("T")
					|| arts.getString("VRART").equalsIgnoreCase("U")) {
				return false;
			}
		}*/
		//return true;
	}

	int fillStavka(DataSet stav) {
	  if (!lD.raLocate(stanje, new String[] { "CSKL", "GOD", "CART" },
          new String[] { stav.getString(ncskl),
//                stavke_rac.getString("GOD"),
          zaglavlja_otp.getString("GOD"),
                  String.valueOf(stav.getInt("CART")) })) {
      return -3;
      }
      stavke_otp.insertRow(false);
      stavke_otp.setString("CSKL", zaglavlja_otp.getString("CSKL"));
      stavke_otp.setString("CSKLART", zaglavlja_otp.getString("CSKL"));
      stavke_otp.setString("GOD", zaglavlja_otp.getString("GOD"));
      stavke_otp.setString("VRDOK", zaglavlja_otp.getString("VRDOK"));
      stavke_otp.setInt("BRDOK", zaglavlja_otp.getInt("BRDOK"));
      if (!hmrbr.containsKey(stavke_otp.getString("CSKL"))) {
          return 1;
      }
      stavke_otp.setShort("RBR", ((Integer) hmrbr.get(stavke_otp
              .getString("CSKL"))).shortValue());
      stavke_otp.setInt("RBSID", ((Integer) hmrbr.get(stavke_otp
              .getString("CSKL"))).intValue());
      hmrbr.put(stavke_otp.getString("CSKL"), new Integer(stavke_otp
              .getInt("RBSID") + 1));
      stavke_otp.setInt("CART", stav.getInt("CART"));
      stavke_otp.setString("CART1", stav.getString("CART1"));
      stavke_otp.setString("BC", stav.getString("BC"));
      stavke_otp.setString("NAZART", stav.getString("NAZART"));
      stavke_otp.setString("JM", stav.getString("JM"));
      stavke_otp.setBigDecimal("KOL", stav.getBigDecimal("KOL"));
      stavke_otp.setString("id_stavka", raControlDocs.getKey(stavke_otp,
              new String[] { "cskl", "vrdok", "god", "brdok", "rbsid" },
              "stdoki"));
      stavke_otp.setString("VEZA", stav.getString("ID_STAVKA"));
      stavke_otp.setString("REZKOL", stav.getString("REZKOL"));
    
      rKD.stanje.Init();
      rKD.stavka.Init();
      rKD.stavkaold.Init();
    //QueryDataSet skltmp = hr.restart.util.Util.getNewQueryDataSet(
    //        "select vrzal from sklad where cskl='"
    //                + zaglavlja_otp.getString("CSKL") + "'", true);
      rKD.setVrzal(zaglavlja_otp.getString("CSKL"));
      lc.TransferFromDB2Class(stavke_otp, rKD.stavka);
      lc.TransferFromDB2Class(stanje, rKD.stanje);
      rKD.SetupPriceForSkladSide();
      rKD.setWhat_kind_of_document("OTP");
      rKD.kalkSkladPart();
      /*BigDecimal bd = rKD.stanje.kolrez;
      rKD.stanje.kolrez = Aus.zero2;*/
      if (rKD.TestStanje() == rKD.rKS.NEG_KOL && !isMinusAllowed) {
          return -5;
      }
      rKD.KalkulacijaStanje("OTP");
      rKD.VratiRezervu("OTP");
      lc.TransferFromClass2DB(stavke_otp, rKD.stavka);
      //rKD.stanje.kolrez = bd;
      lc.TransferFromClass2DB(stanje, rKD.stanje);
      rCD.unosIzlaz(stavke_otp, stanje);
      
      // ako se otpremnica radi iz racuna koji je nastao iz DOS-a!!
      if (stav.getString("VEZA").indexOf("-DOS-") >= 0) {
        stanje.setBigDecimal("KOLSKLADIZ", stanje.getBigDecimal("KOLSKLADIZ")
                      .subtract(stavke_otp.getBigDecimal("KOL")));
        stanje.setBigDecimal("KOLSKLAD", stanje.getBigDecimal("KOLSKLADUL")
                      .subtract(stanje.getBigDecimal("KOLSKLADIZ")));
      }
      stav.setString("STATUS", "P");
      stav.setString("VEZA", stavke_otp.getString("ID_STAVKA"));
      if (raIzlazTemplate.isNabDirect()) {
      	Aus.set(stav, "RNC", stavke_otp, "NC");
      	Aus.set(stav, "RINAB", stavke_otp, "INAB");
      }
      return 0;
	}
	
	// sdfsdfs
	protected int makeStavke() {

		for (stavke_rac.first(); stavke_rac.inBounds(); stavke_rac.next()) {

			if (!smijem_li_dalje()) {
				stavke_rac.setString("STATUS", "P");
				continue;
			}

			if (!lD.raLocate(zaglavlja_otp, "CSKL", stavke_rac
					.getString(ncskl))) {
				return -2;
			}
			int ret = fillStavka(stavke_rac);
			if (ret != 0) return ret;
			
		}
		return 0;
	}
	
	protected int breakStavke() {

      for (stavke_rac.first(); stavke_rac.inBounds(); stavke_rac.next()) {

          if (!smijem_li_dalje()) {
              stavke_rac.setString("STATUS", "P");
              continue;
          }

          if (!lD.raLocate(zaglavlja_otp, "CSKL", stavke_rac
                  .getString(ncskl))) {
              return -2;
          }
          int ret = fillStavka(stavke_rac);
          if (ret != 0) return ret;
          
      }
      return 0;
    }

	protected void makeZaglavlja() {
      boolean datToday = frmParam.getParam("robno", "otpToday", "D",
          "Postaviti današnji dan kod automatskog prijenosa RAC->OTP (D,N)?").
            equalsIgnoreCase("D");
		for (stavke_rac.first(); stavke_rac.inBounds(); stavke_rac.next()) {
			/*
			 * preskaèu se oni kojima ne postoji oznaka skladišta u rapancartu
			 */
			if (stavke_rac.getString(ncskl).equalsIgnoreCase("")) {
				continue;
			}
			/*
			 * tražim artikle u bazi artikala i preskaèem ih ako su usluga ili
			 * tranzit ili iz nekog razloga nisu uopæe u bazi što je merde
			 */
			if ((!stavke_rac.getString("STATUS").equals("X") ||
			    !stavke_rac.getString("VRDOK").equals("ROT")) &&
			    !raVart.isStanje(stavke_rac.getInt("CART"))) continue;
			
/*			QueryDataSet arts = hr.restart.util.Util.getNewQueryDataSet(
					"select * from artikli where cart="
							+ stavke_rac.getInt("CART"), true);
			if (arts.getRowCount() == 1) {
				if (arts.getString("VRART").equalsIgnoreCase("T")
						|| arts.getString("VRART").equalsIgnoreCase("U")) {
					continue;
				}
			} else {
				System.out
						.println("Greska nisam nasao artikl (ili ih ima više od 1) za "
								+ stavke_rac.getInt("CART"));
				continue;
			}*/
			
			/*
			 * Ako dosad nije dodano zaglavlje e sad ce biti
			 */
			if (!lD.raLocate(zaglavlja_otp, "CSKL", stavke_rac
					.getString(ncskl))) {
				zaglavlja_otp.insertRow(false);
				zaglavlja_otp.setString("CUSER", zaglavlje_rac
						.getString("CUSER"));
				zaglavlja_otp.setString("CSKL", stavke_rac.getString(ncskl));
				zaglavlja_otp.setString("VRDOK", "OTP");
				zaglavlja_otp.setTimestamp("SYSDAT",
				hr.restart.util.Util.getUtil().getCurrentDatabaseTime());
//						zaglavlje_rac
//						.getTimestamp("SYSDAT"));
				//				zaglavlja_otp.setTimestamp("DATDOK", zaglavlje_rac
				//						.getTimestamp("DATDOK"));

				zaglavlja_otp.setTimestamp("DATDOK", datToday ? 
                    Valid.getValid().getToday() : zaglavlje_rac.getTimestamp("DATDOK"));
				zaglavlja_otp.setString("GOD", 
						val.findYear(zaglavlja_otp.getTimestamp("DATDOK")));
//						1zaglavlje_rac.getString("GOD"));
				
				zaglavlja_otp.setString("STATIRA", "P");
				if (!zaglavlje_rac.isAssignedNull("CPAR")) {
					zaglavlja_otp.setInt("CPAR", zaglavlje_rac.getInt("CPAR"));
				}
				if (!zaglavlje_rac.isAssignedNull("CKUPAC")) {
					zaglavlja_otp.setInt("CKUPAC", zaglavlje_rac
							.getInt("CKUPAC"));
				}
                if (!(zaglavlje_rac.isAssignedNull("PJ") || zaglavlje_rac.getInt("PJ") == 0)) {
                  zaglavlja_otp.setInt("PJ", zaglavlje_rac.getInt("PJ"));
                }
				zaglavlja_otp
						.setString("CFRA", zaglavlje_rac.getString("CFRA"));
				zaglavlja_otp.setString("CNACPL", zaglavlje_rac
						.getString("CNACPL"));
				zaglavlja_otp.setString("CNAMJ", zaglavlje_rac
						.getString("CNAMJ"));
				zaglavlja_otp
						.setString("CNAC", zaglavlje_rac.getString("CNAC"));
				//brdok

				int brdok = 0;
				String opis = zaglavlja_otp.getString("CSKL")
						+ zaglavlja_otp.getString("VRDOK")
						+ zaglavlja_otp.getString("GOD");
				if (lD.raLocate(mySEQ, "OPIS", opis)) {
					mySEQ.setDouble("BROJ", mySEQ.getDouble("BROJ") + 1);
				} else {
					mySEQ.insertRow(true);
					mySEQ.setString("OPIS", opis);
					mySEQ.setDouble("BROJ", brdok + 1);
				}
				brdok = (int) mySEQ.getDouble("BROJ");

				zaglavlja_otp.setInt("BRDOK", brdok);
				//        zaglavlja_otp.setInt("BRDOK",val.findSeqInt(zaglavlja_otp.getString("CSKL")+
				//                           zaglavlja_otp.getString("VRDOK")+
				//                           zaglavlja_otp.getString("GOD"),false));

				hmrbr.put(zaglavlja_otp.getString("CSKL"), new Integer(1));
				vtPrijenos.open();
				vtPrijenos.insertRow(false);
				vtPrijenos.setString(
						"KEYSRC",
						rPVT.makeKey("doki", zaglavlje_rac.getString("CSKL"),
								zaglavlje_rac.getString("VRDOK"), zaglavlje_rac
										.getString("GOD"), zaglavlje_rac
										.getInt("BRDOK")));
				vtPrijenos.setString(
						"KEYDEST",
						rPVT.makeKey("doki", zaglavlja_otp.getString("CSKL"),
								zaglavlja_otp.getString("VRDOK"), zaglavlja_otp
										.getString("GOD"), zaglavlja_otp
										.getInt("BRDOK")));
			}
		}
        zaglavlje_rac.setString("STATIRA", "P");
	}

	public int makeOtp(String cskl, String vrdok, int brdok, String god) {
		init();
		ncskl = vrdok.equals("ROT") ? "CSKL" : "CSKLART";
		zaglavlje_rac = hr.restart.util.Util.getNewQueryDataSet(
				"select * from doki where cskl='" + cskl + "' and vrdok='"
						+ vrdok + "' and god='" + god + "' and brdok=" + brdok,
				true);

		stavke_rac = hr.restart.util.Util.getNewQueryDataSet(
				"select * from stdoki where cskl='" + cskl + "' and vrdok='"
						+ vrdok + "' and god='" + god + "' and brdok=" + brdok,
				true);

		stanje = hr.restart.util.Util.getNewQueryDataSet(
				"select * from stanje", true);

		if (zaglavlje_rac.getRowCount() != 1) {
			System.out.println("---------");
			System.out.println("Neispravno nalaženje raèuna ili ga nema ili ih je previše ");
			System.out.println("cskl=" + cskl + " vrdok=" + vrdok + " god="
					+ god + " brdok=" + brdok);
			System.out.println("---------");
			return -1; // nema ili previše zaglavlja
		}

		if (stavke_rac.getRowCount() < 1) {
			System.out.println("---------");
			System.out.println("Nema stavki raèuna !!! ");
			System.out.println("cskl=" + cskl + " vrdok=" + vrdok + " god="
					+ god + " brdok=" + brdok);
			System.out.println("---------");
			return -2; // nema ili previše zaglavlja
		}
		makeZaglavlja();
		if (zaglavlja_otp.getRowCount() != 0) {
			int ret = vrdok.equals("ROT") ? breakStavke() : makeStavke();
			if (ret != 0)
				return ret;
		}
		return snimi(); // sve je OK
	}

	public int snimi() {
		ST.prn(zaglavlja_otp);

		raLocalTransaction rLT = new raLocalTransaction() {
			public boolean transaction() {
				try {
					raTransaction.saveChanges(zaglavlja_otp);
                    raTransaction.saveChanges(zaglavlje_rac);
					raTransaction.saveChanges(stanje);
					raTransaction.saveChanges(stavke_otp);
					raTransaction.saveChanges(stavke_rac);
					raTransaction.saveChanges(mySEQ);
					raTransaction.saveChanges(vtPrijenos);
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
				return true;
			}
		};

		if (!rLT.execTransaction()) {
			return -9;
		}
		return 0;
	}
}