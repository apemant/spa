package hr.restart.robno;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.*;
import hr.restart.help.MsgDispatcher;
import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;

public class raImportLeoss {

	lookupData ld = lookupData.getlookupData();
	allStanje AST = allStanje.getallStanje();
	boolean delfiles = false;
	
	public void process() {
		String path = frmParam.getParam("robno", "leossPath", "",
  		"Putanja mape za import Leoss dokumenata");
		delfiles = "D".equals(frmParam.getParam("robno", "leossDel", "N",
        "Pobrisati leoss datoteke nakon importa (D,N)"));
		
		File dir = new File(".");
		if (path.length() > 0) dir = new File(path);
		
		File[] dirs = dir.listFiles();
		for (int i = 0; i < dirs.length; i++)
			if (dirs[i].isDirectory())
				processDir(dirs[i]);
		
	}
	
	void processDir(File dir) {
		File dz = new File(dir, "VSE_ODPR.DAT");
		File dst = new File(dir, "ODPREMN.DAT");
		if (dz.exists() && dz.canWrite() && dst.exists() && dst.canWrite())
			processDos(dz, dst, dir.getName());
		
		File pz = new File(dir, "VSE_PREJ.DAT");
		File pst = new File(dir, "PREJEM.DAT");
		if (pz.exists() && pz.canWrite() && pst.exists() && pst.canWrite())
			processPri(pz, pst, dir.getName());
		
		File mz = new File(dir, "VSE_SKL.DAT");
		File mst = new File(dir, "SKLADISC.DAT");
		if (mz.exists() && mz.canWrite() && mst.exists() && mst.canWrite())
			processMes(mz, mst, dir.getName());
	}
		
	void processDos(File zag, File st, String cskl) {
		System.out.println("process DOS " + cskl);
		System.out.println(zag);
		String line;
		ArrayList lzag = new ArrayList();
		ArrayList lst = new ArrayList();
		
		TextFile tzag = TextFile.read(zag);
  	while (null != (line = tzag.in())) lzag.add(line);
  	tzag.close();
  	TextFile tst = TextFile.read(st);
  	while (null != (line = tst.in())) lst.add(line);
  	tst.close();
  	
  	DataSet cj = Cjenik.getDataModule().getTempSet();
  	cj.open();
  	
  	for (int i = 0; i < lzag.size(); i++) {
  		QueryDataSet dz = doki.getDataModule().getTempSet("1=0");
  		dz.open();
  		dz.insertRow(false);
  		
  		line = (String) lzag.get(i);
  		String[] pl = new VarStr(line).split(';');
  		String brdok = pl[0];
  		String spar = pl[1];
  		if (spar.indexOf('-') > 0) {
  			dz.setInt("CPAR", Aus.getAnyNumber(spar));
  			dz.setInt("PJ", Aus.getAnyNumber(spar.substring(spar.indexOf('-') + 1)));
  		} else {
  			dz.setInt("CPAR", Aus.getAnyNumber(spar));
  		}
  		
  		dz.setString("CSKL", cskl);
  		dz.setString("VRDOK", "DOS");
  		dz.setTimestamp("DATDOK", getDate(pl[2], pl[3]));
  		dz.setString("BRDOKIZ", brdok);
  		dz.setString("BRNARIZ", pl[6]);
  		if (pl.length > 7)
  		  dz.setString("CRADNAL", pl[7]);
  		Util.getUtil().getBrojDokumenta(dz);
  		dz.saveChanges();
  		
  		short rbr = 0;
  		for (int j = 0; j < lst.size(); j++) {
  			line = (String) lst.get(j);
    		String[] pls = new VarStr(line).split(';');
    		if (!pls[0].equals(brdok)) continue;
    		
    		QueryDataSet dst = stdoki.getDataModule().getTempSet("1=0");
    		dst.open();
    		dst.insertRow(false);
    		dM.copyColumns(dz, dst, Util.mkey);
    		dst.setShort("RBR", ++rbr);
    		dst.setInt("RBSID", rbr);
    		
    		ld.raLocate(dM.getDataModule().getArtikli(), "CART1", pls[1]);
    		Aut.getAut().copyArtFields(dst, dM.getDataModule().getArtikli());
    		dst.setBigDecimal("KOL", Aus.getDecNumber(pls[2].trim()));
    		Aus.set(dst, "KOL2", "KOL");
    		
    		dst.setString("LOT", pls[7]);
    		dst.setTimestamp("ROKTRAJ", getDate(pls[6], null));
    		
    		AST.findStanjeUnconditional(dst.getString("GOD"), dst.getString("CSKL"), dst.getInt("CART"));			
  			boolean nemaGa = AST.gettrenSTANJE() == null
  			|| AST.gettrenSTANJE().getRowCount() == 0; 
  			if (nemaGa) {
  				AST.gettrenSTANJE().insertRow(false);
  				AST.gettrenSTANJE().setString("GOD",
  						dst.getString("GOD"));
  				AST.gettrenSTANJE().setString("CSKL",
  						dst.getString("CSKL"));
  				AST.gettrenSTANJE().setInt("CART",
  						dst.getInt("CART"));
  				nulaStanje(AST.gettrenSTANJE());
  			}
  			Aus.add(AST.gettrenSTANJE(), "KOLSKLADIZ", dst, "KOL");
  			Aus.sub(AST.gettrenSTANJE(), "KOLSKLAD", "KOLSKLADUL", "KOLSKLADIZ");
  			
  			dst.setString("ID_STAVKA",
                raControlDocs.getKey(dst, new String[] { "cskl",
                        "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
    		
  			raTransaction.saveChangesInTransaction(new QueryDataSet[] {dst, AST.gettrenSTANJE()});
  		}
  	}
  	if (delfiles) {
  	  zag.delete();
  	  st.delete();
  	}
  	System.out.println("over dos");
	}
	
	void processPri(File zag, File st, String cskl) {
		System.out.println("process PRI " + cskl);
		System.out.println(zag);
		String line;
		ArrayList lzag = new ArrayList();
		ArrayList lst = new ArrayList();
		
		TextFile tzag = TextFile.read(zag);
  	while (null != (line = tzag.in())) lzag.add(line);
  	tzag.close();
  	TextFile tst = TextFile.read(st);
  	while (null != (line = tst.in())) lst.add(line);
  	tst.close();
  	
  	for (int i = 0; i < lzag.size(); i++) {
  		QueryDataSet dz = Doku.getDataModule().getTempSet("1=0");
  		dz.open();
  		dz.insertRow(false);
  		
  		line = (String) lzag.get(i);
  		String[] pl = new VarStr(line).split(';');
  		String brdok = pl[0];
  		String spar = pl[1];
  		if (spar.indexOf('-') > 0) {
  			dz.setInt("CPAR", Aus.getAnyNumber(spar));
  			dz.setInt("PJ", Aus.getAnyNumber(spar.substring(spar.indexOf('-') + 1)));
  		} else {
  			dz.setInt("CPAR", Aus.getAnyNumber(spar));
  		}
  		
  		dz.setString("CSKL", cskl);
  		dz.setString("VRDOK", "PRI");
  		dz.setTimestamp("DATDOK", getDate(pl[2], null));
  		Util.getUtil().getBrojDokumenta(dz);
  		dz.saveChanges();
  		
  		short rbr = 0;
  		for (int j = 0; j < lst.size(); j++) {
  			line = (String) lst.get(j);
    		String[] pls = new VarStr(line).split(';');
    		if (!pls[0].equals(brdok)) continue;
    		
    		QueryDataSet dst = Stdoku.getDataModule().getTempSet("1=0");
    		dst.open();
    		dst.insertRow(false);
    		dM.copyColumns(dz, dst, Util.mkey);
    		dst.setShort("RBR", ++rbr);
    		dst.setInt("RBSID", rbr);
    		
    		ld.raLocate(dM.getDataModule().getArtikli(), "CART1", pls[1]);
    		Aut.getAut().copyArtFields(dst, dM.getDataModule().getArtikli());
    		dst.setBigDecimal("KOL", Aus.getDecNumber(pls[2]));
    		
    		dst.setString("LOT", pls[4]);
    		dst.setTimestamp("DATROK", getDate(pls[3], null));
    		
    		AST.findStanjeUnconditional(dst.getString("GOD"), dst.getString("CSKL"), dst.getInt("CART"));			
  			boolean nemaGa = AST.gettrenSTANJE() == null
  			|| AST.gettrenSTANJE().getRowCount() == 0; 
  			if (nemaGa) {
  				AST.gettrenSTANJE().insertRow(false);
  				AST.gettrenSTANJE().setString("GOD",
  						dst.getString("GOD"));
  				AST.gettrenSTANJE().setString("CSKL",
  						dst.getString("CSKL"));
  				AST.gettrenSTANJE().setInt("CART",
  						dst.getInt("CART"));
  				nulaStanje(AST.gettrenSTANJE());
  			}
  			Aus.add(AST.gettrenSTANJE(), "KOLSKLADUL", dst, "KOL");
  			Aus.sub(AST.gettrenSTANJE(), "KOLSKLAD", "KOLSKLADUL", "KOLSKLADIZ");
  			
  			dst.setString("ID_STAVKA",
                raControlDocs.getKey(dst, new String[] { "cskl",
                        "vrdok", "god", "brdok", "rbsid" }, "stdoku"));
    		
  			raTransaction.saveChangesInTransaction(new QueryDataSet[] {dst, AST.gettrenSTANJE()});
  		}
  	}
  	if (delfiles) {
      zag.delete();
      st.delete();
    }
  	System.out.println("over pri");
	}
	
	void processMes(File zag, File st, String cskl) {
		System.out.println("process MES " + cskl);
		System.out.println(zag);
		String line;
		ArrayList lzag = new ArrayList();
		ArrayList lst = new ArrayList();
		
		TextFile tzag = TextFile.read(zag);
  	while (null != (line = tzag.in())) lzag.add(line);
  	tzag.close();
  	TextFile tst = TextFile.read(st);
  	while (null != (line = tst.in())) lst.add(line);
  	tst.close();
  	
  	raKalkulBDMeskla rKM = new raKalkulBDMeskla();
  	raControlDocs rCD = new raControlDocs();
  	hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
  	for (int i = 0; i < lzag.size(); i++) {
  		QueryDataSet dz = Meskla.getDataModule().getTempSet("1=0");
  		dz.open();
  		dz.insertRow(false);
  		
  		line = (String) lzag.get(i);
  		String[] pl = new VarStr(line).split(';');
  		String brdok = pl[0];	
  		dz.setString("CSKLIZ", pl[1]);
  		dz.setString("CSKLUL", pl[2]);
  		dz.setString("VRDOK", "MES");
  		dz.setTimestamp("DATDOK", getDate(pl[3], null));
  		Util.getUtil().getBrojDokumenta(dz);
  		dz.saveChanges();
  		
  		String izv = allStanje.VrstaZalihaA(dz.getString("CSKLIZ"));
    	String ulv = allStanje.VrstaZalihaA(dz.getString("CSKLUL"));
  		
  		String[] mkey = {"CSKLIZ", "CSKLUL", "VRDOK", "GOD", "BRDOK"};
  		short rbr = 0;
  		for (int j = 0; j < lst.size(); j++) {
  			line = (String) lst.get(j);
    		String[] pls = new VarStr(line).split(';');
    		if (!pls[0].equals(brdok)) continue;
    		
    		QueryDataSet dst = Stmeskla.getDataModule().getTempSet("1=0");
    		dst.open();
    		dst.insertRow(false);
    		dM.copyColumns(dz, dst, mkey);
    		dst.setShort("RBR", ++rbr);
    		dst.setInt("RBSID", rbr);
    		
    		ld.raLocate(dM.getDataModule().getArtikli(), "CART1", pls[1]);
    		Aut.getAut().copyArtFields(dst, dM.getDataModule().getArtikli());
    		dst.setBigDecimal("KOL", Aus.getDecNumber(pls[2]));
    		
    		ld.raLocate(dM.getDataModule().getPorezi(), "CPOR", dM.getDataModule().getArtikli().getString("CPOR"));
        
        QueryDataSet izlaz = Stanje.getDataModule().getTempSet(
      			Condition.equal("CSKL", dst.getString("CSKLIZ")).
      			and(Condition.equal("GOD", dst)).and(
      					Condition.equal("CART", dst)));
        izlaz.open();
        
        if (izlaz.rowCount() == 0) {
        	izlaz.insertRow(false);
        	izlaz.setString("GOD", dst.getString("GOD"));
        	izlaz.setString("CSKL", dst.getString("CSKLIZ"));
        	izlaz.setInt("CART", dst.getInt("CART"));
  				nulaStanje(izlaz);
        }
        
        QueryDataSet ulaz = Stanje.getDataModule().getTempSet(
      			Condition.equal("CSKL", dst.getString("CSKLUL")).
      			and(Condition.equal("GOD", dst)).and(
      					Condition.equal("CART", dst)));
        ulaz.open();
                
        rKM.stavka.Init();
        rKM.stavkaold.Init();
        rKM.stanjeiz.Init();
        rKM.stanjeul.Init();
        lc.TransferFromDB2Class(izlaz,rKM.stanjeiz);
        lc.TransferFromDB2Class(ulaz,rKM.stanjeul);
        rKM.stanjeiz.sVrSklad=izv;
        rKM.stanjeul.sVrSklad=ulv;
        
        rKM.stavka.reverzpostopor = dM.getDataModule().getPorezi().getBigDecimal("UKUNPOR");
        rKM.stavka.postopor = dM.getDataModule().getPorezi().getBigDecimal("UKUPOR");

        rKM.setupOldPrice();
        rKM.setupPrice();
        
        rKM.stavka.kol = Aus.getDecNumber(pls[2]);

        rKM.Kalkulacija();
        lc.TransferFromClass2DB(dst, rKM.stavka);
        
        rKM.kalkStanja();
        
        lc.TransferFromClass2DB(izlaz, rKM.stanjeiz);
        rCD.unosIzlaz(dst, izlaz); //???????

        if (ulaz.getRowCount() == 0) {
        	ulaz.insertRow(true);
        	ulaz.setString("CSKL", dst.getString("CSKLUL"));
        	ulaz.setString("GOD", dst.getString("GOD"));
        	ulaz.setInt("CART", dst.getInt("CART"));
        }
        lc.TransferFromClass2DB(ulaz, rKM.stanjeul);
        rCD.unosKalkulacije(dst, ulaz); 
         
        ulaz.setTimestamp("DATZK", dz.getTimestamp("DATDOK"));
        raTransaction.saveChangesInTransaction(new QueryDataSet[] {ulaz, izlaz, dst});
  		}
  	}
  	if (delfiles) {
      zag.delete();
      st.delete();
    }
	}
	
	void nulaStanje(QueryDataSet qdsstanje) {
		BigDecimal nula = Aus.zero2;
		qdsstanje.setBigDecimal("KOLPS", nula);
		qdsstanje.setBigDecimal("KOLUL", nula);
		qdsstanje.setBigDecimal("KOLIZ", nula);
		qdsstanje.setBigDecimal("KOLREZ", nula);
		qdsstanje.setBigDecimal("NABPS", nula);
		qdsstanje.setBigDecimal("MARPS", nula);
		qdsstanje.setBigDecimal("PORPS", nula);
		qdsstanje.setBigDecimal("VPS", nula);
		qdsstanje.setBigDecimal("NABUL", nula);
		qdsstanje.setBigDecimal("MARUL", nula);
		qdsstanje.setBigDecimal("PORUL", nula);
		qdsstanje.setBigDecimal("VUL", nula);
		qdsstanje.setBigDecimal("NABIZ", nula);
		qdsstanje.setBigDecimal("MARIZ", nula);
		qdsstanje.setBigDecimal("PORIZ", nula);
		qdsstanje.setBigDecimal("VIZ", nula);
		qdsstanje.setBigDecimal("KOL", nula);
		qdsstanje.setBigDecimal("ZC", nula);
		qdsstanje.setBigDecimal("VRI", nula);
		qdsstanje.setBigDecimal("NC", nula);
		qdsstanje.setBigDecimal("VC", nula);
		qdsstanje.setBigDecimal("MC", nula);
	}
	
	private Timestamp getDate(String sd, String st) {
  	Calendar cal = Calendar.getInstance();
  	cal.set(cal.YEAR, Integer.parseInt(sd.substring(4, 8)));
  	cal.set(cal.MONTH, Integer.parseInt(sd.substring(2, 4)) - 1);
  	cal.set(cal.DAY_OF_MONTH, Integer.parseInt(sd.substring(0, 2)));
  	if (st != null) {
  		cal.set(cal.HOUR_OF_DAY, Integer.parseInt(st.substring(0, 2)));
      cal.set(cal.MINUTE, Integer.parseInt(st.substring(3, 5)));
      cal.set(cal.SECOND, Integer.parseInt(st.substring(6, 8)));
  	} else {
  		cal.set(cal.HOUR_OF_DAY, 0);
      cal.set(cal.MINUTE, 0);
      cal.set(cal.SECOND, 0);
  	}
  	cal.set(cal.MILLISECOND, 0);
  	return new Timestamp(cal.getTime().getTime());
  }  
	
	public static void main(String[] args) {
  	
    MsgDispatcher.install(false);
    
    new raImportLeoss().process();
    
    System.out.println("over main");
    System.exit(0);
  }
}
