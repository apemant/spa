package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.SanityException;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;

public class SanityCheck {
	
	private SanityCheck() {
		// static method class
	}
	
	public static void basicDoki(ReadRow ds) {
		basicCSKL(ds);
		basicVRDOK(ds);
		basicGOD(ds);
		basicBRDOK(ds);
		basicDATDOK(ds);
		godDatum(ds);
		vrdokIzlaz(ds);
	}
	
	public static void basicStdoki(ReadRow ds) {
		basicCSKL(ds);
		basicVRDOK(ds);
		basicGOD(ds);
		basicBRDOK(ds);
		basicRBR(ds);
		vrdokIzlaz(ds);
	}
	
	public static void basicDoku(ReadRow ds) {
		basicCSKL(ds);
		basicVRDOK(ds);
		basicGOD(ds);
		basicBRDOK(ds);
		basicDATDOK(ds);
		godDatum(ds);
		vrdokUlaz(ds);
	}
	
	public static void basicStdoku(ReadRow ds) {
		basicCSKL(ds);
		basicVRDOK(ds);
		basicGOD(ds);
		basicBRDOK(ds);
		basicRBR(ds);
		vrdokUlaz(ds);
	}
	
	public static void basicMeskla(ReadRow ds) {
		basicCSKLIZ(ds);
		basicCSKLUL(ds);
		basicVRDOK(ds);
		basicGOD(ds);
		basicBRDOK(ds);
		basicDATDOK(ds);
		godDatum(ds);
		vrdokMeskla(ds);
	}
	
	public static void basicStmeskla(ReadRow ds) {
		basicCSKLIZ(ds);
		basicCSKLUL(ds);
		basicVRDOK(ds);
		basicGOD(ds);
		basicBRDOK(ds);
		basicRBR(ds);
		vrdokMeskla(ds);
	}
	
	public static void consistencyIzlaz(ReadRow master, ReadRow detail) {
	  if (dM.compareColumns(master, detail, hr.restart.robno.Util.mkey) != null)
	    throw new SanityException("Nekonzistentni master i detail!");
	  
	}
	
	public static void basicCSKL(ReadRow ds) {
		if (ds.isNull("CSKL") || ds.getString("CSKL").trim().length() == 0)
			throw new SanityException("Nedefinirano skladište!");
	}
	
	public static void basicCSKLUL(ReadRow ds) {
		if (ds.isNull("CSKLUL") || ds.getString("CSKLUL").trim().length() == 0)
			throw new SanityException("Nedefinirano ulazno skladište!");
	}
	
	public static void basicCSKLIZ(ReadRow ds) {
		if (ds.isNull("CSKLIZ") || ds.getString("CSKLIZ").trim().length() == 0)
			throw new SanityException("Nedefinirano izlazno skladište!");
	}
	
	public static void basicVRDOK(ReadRow ds) {
		if (ds.isNull("VRDOK") || ds.getString("VRDOK").trim().length() == 0)
			throw new SanityException("Nedefinirana vrsta dokumenta!");
		
		if (!lookupData.getlookupData().raLocate(
				dM.getDataModule().getVrdokum(), "VRDOK", ds.getString("VRDOK")))
			throw new SanityException("Nepoznata vrsta dokumenta - " +	ds.getString("VRDOK") + "!");
	}
	
	public static void basicGOD(ReadRow ds) {
		if (ds.isNull("GOD") || ds.getString("GOD").trim().length() == 0)
			throw new SanityException("Nedefinirana godina!");
		int god = Aus.getNumber(ds.getString("GOD"));
		int thisGod = Aus.getNumber(Valid.getValid().findYear());
		if (god <= thisGod - 20 || god > thisGod+1)
			throw new SanityException("Pogrešna godina - " + god + "!");
	}

	public static void basicBRDOK(ReadRow ds) {
		if (ds.isNull("BRDOK") || ds.getInt("BRDOK") < 1)
			throw new SanityException("Nedefiniran broj dokumenta!");
	}
	
	public static void basicDATDOK(ReadRow ds) {
		if (ds.isNull("DATDOK"))
			throw new SanityException("Nedefiniran datum dokumenta!");
		Timestamp datum = ds.getTimestamp("DATDOK");
		Timestamp lastValid = Util.getUtil().getLastDayOfYear(Valid.getValid().getToday());
		lastValid = Util.getUtil().addYears(lastValid, 1);
    Timestamp firstValid = Util.getUtil().addYears(lastValid, -25);
    if (datum.after(Util.getUtil().getLastSecondOfDay(lastValid)) ||
    		datum.before(firstValid))
    	throw new SanityException("Pogrešan datum dokumenta - " + Aus.formatTimestamp(datum) + "!");
	}
	
	public static void basicRBR(ReadRow ds) {
		if (ds.isNull("RBR") || ds.getShort("RBR") < 1)
			throw new SanityException("Nedefiniran redni broj stavke!");
	}
	
	public static void vrdokUlaz(ReadRow ds) {
		if (!TypeDoc.getTypeDoc().isDocStdoku(ds.getString("VRDOK")))
			throw new SanityException("Pogrešna vrsta dokumenta - " +	ds.getString("VRDOK") + "!");
	}
	
	public static void vrdokIzlaz(ReadRow ds) {
		if (!TypeDoc.getTypeDoc().isDocStdoki(ds.getString("VRDOK")) && 
		    !TypeDoc.getTypeDoc().isDocDOS(ds.getString("VRDOK")))
			throw new SanityException("Pogrešna vrsta dokumenta - " +	ds.getString("VRDOK") + "!");
	}
	
	public static void vrdokMeskla(ReadRow ds) {
		if (!TypeDoc.getTypeDoc().isDocStmeskla(ds.getString("VRDOK")))
			throw new SanityException("Pogrešna vrsta dokumenta - " +	ds.getString("VRDOK") + "!");
	}
	
	public static void godDatum(ReadRow ds) {
		Timestamp datum = ds.getTimestamp("DATDOK");
		String god = ds.getString("GOD");
		if (!Util.getUtil().getYear(datum).equals(god))
			throw new SanityException("Datum i godina dokumenta se ne poklapaju!");
	}
	
	public static void stanjeArt(DataSet stanje, ReadRow stavka) {
		if (stanje.rowCount() == 0 ||
				dM.compareColumns(stanje, stavka, new String[] {"CSKL", "GOD", "CART"}) != null)
			throw new SanityException("Nije pronaðeno stanje artikla!");
	}
	
	public static void stanjeArt(DataSet stanje, ReadRow stavka, String cskl) {
		if (stanje.rowCount() == 0 ||
				!stanje.getString("CSKL").equals(stavka.getString(cskl)) ||
				dM.compareColumns(stanje, stavka, new String[] {"GOD", "CART"}) != null)
			throw new SanityException("Nije pronaðeno stanje artikla!");
	}
}