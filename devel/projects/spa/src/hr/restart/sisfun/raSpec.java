package hr.restart.sisfun;

import hr.restart.util.Aus;
import hr.restart.util.raTransaction;

import java.math.BigDecimal;
import java.util.HashMap;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raSpec {
	
	public static void recalcHTS() {
	  BigDecimal mtot = Aus.zero0;
		QueryDataSet ul = Aus.q("SELECT cskl,vrdok,god,brdok,rbr,cart,kol,inab,imar,ipor,izad FROM stdoku WHERE god='2008'");

		QueryDataSet iz = Aus.q("SELECT cskl,vrdok,god,brdok,rbr,cart,kol,nc,inab,imar,ipor,zc,iraz FROM stdoki WHERE god='2008' and vrdok in ('ROT','OTP','IZD')");

		QueryDataSet mes = Aus.q("SELECT * FROM stmeskla WHERE god='2008'");
		
		HashMap artu = new HashMap();

		for (ul.first(); ul.inBounds(); ul.next()) {
		  String ci = getKey(ul, "CSKL");
			ArtData a = (ArtData) artu.get(ci);
			if (a == null)
				artu.put(ci, a = new ArtData());
			a.addUlaz(ul);
		}
		
		for (mes.first(); mes.inBounds(); mes.next()) {
          String ci = getKey(mes, "CSKLUL");
            ArtData a = (ArtData) artu.get(ci);
            if (a == null)
                artu.put(ci, a = new ArtData());
            a.addUlazMes(mes);
        }
		
		for (iz.first(); iz.inBounds(); iz.next()) {
		  String ci = getKey(iz, "CSKL");
			ArtData a = (ArtData) artu.get(ci);
			if (a != null) a.calcIzlaz(iz);
		}
		for (mes.first(); mes.inBounds(); mes.next()) {
		  String ci = getKey(mes, "CSKLIZ");
			ArtData a = (ArtData) artu.get(ci);
			if (a != null) 
			  mtot = mtot.add(a.calcMes(mes));
		}
		/*frmTableDataView vul = new frmTableDataView();
		vul.setDataSet(ul);
		vul.show();
		frmTableDataView viz = new frmTableDataView();
		viz.setDataSet(iz);
		viz.show();
		frmTableDataView vm = new frmTableDataView();
		vm.setDataSet(mes);
		vm.show();*/
		raTransaction.saveChangesInTransaction(new QueryDataSet[] {ul,iz,mes});
		
		System.out.println("TOTAL mes diff: " + mtot);
	}

	private static String getKey(DataSet ds, String cskl) {
	  return ds.getString(cskl) + "-" + ds.getInt("CART");
	}
	
	static class ArtData {
		BigDecimal koliz, kolul;
		BigDecimal nabiz, nabul;
		BigDecimal nc;
		
		public ArtData() {
			kolul = koliz = Aus.zero3;
			nabul = nabiz = nc = Aus.zero2;
		}
		
		public void addUlaz(DataSet ul) {
			kolul = kolul.add(ul.getBigDecimal("KOL"));
			nabul = nabul.add(ul.getBigDecimal("INAB"));
			Aus.set(ul, "IZAD", "INAB");
			Aus.clear(ul, "IMAR");
			Aus.clear(ul, "IPOR");
			if (kolul.signum() != 0)
				nc = nabul.divide(kolul, 4, BigDecimal.ROUND_HALF_UP);
		}
		
		public void addUlazMes(DataSet mes) {
		  kolul = kolul.add(mes.getBigDecimal("KOL"));
          nabul = nabul.add(mes.getBigDecimal("INABUL"));
          if (kolul.signum() != 0)
              nc = nabul.divide(kolul, 4, BigDecimal.ROUND_HALF_UP);
		}
		
		public void calcIzlaz(DataSet iz) {
			BigDecimal inab = nc.multiply(iz.getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP);
			iz.setBigDecimal("INAB", inab);
			iz.setBigDecimal("NC", nc);
			Aus.clear(iz, "IMAR");
			Aus.clear(iz, "IPOR");
			iz.setBigDecimal("ZC", nc);
			iz.setBigDecimal("IRAZ", inab);
		}
		
		public BigDecimal calcMes(DataSet mes) {
			BigDecimal inab = nc.multiply(mes.getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal ret = inab.subtract(mes.getBigDecimal("INABUL")).abs();
			mes.setBigDecimal("NC", nc);
			mes.setBigDecimal("ZC", nc);
			mes.setBigDecimal("ZCUL", nc);
			mes.setBigDecimal("INABUL", inab);
			mes.setBigDecimal("INABIZ", inab);
			Aus.clear(mes, "IMARIZ");
			Aus.clear(mes, "IMARUL");
			Aus.clear(mes, "IPORIZ");
			Aus.clear(mes, "IPORUL");
			mes.setBigDecimal("ZADRAZUL", inab);
			mes.setBigDecimal("ZADRAZIZ", inab);
			return ret;
		}
	}
}
