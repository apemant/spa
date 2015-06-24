package hr.restart.robno;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.VTCartPart;
import hr.restart.baza.dM;
import hr.restart.sisfun.TextFile;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;


public class raFranck {

  private raFranck() {
    // nothing
  }
  
  
  public static DataSet generate(int cpar, Timestamp from, Timestamp to, File out) {
    hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
    lookupData ld = lookupData.getlookupData();
    
    from = ut.getFirstSecondOfDay(from);
    to = ut.getLastSecondOfDay(to);
    
    DataSet art = Artikli.getDataModule().getQueryDataSet();
    art.open();
      
    DataSet ca = VTCartPart.getDataModule().getTempSet(Condition.equal("CPAR", cpar));
    ca.open();
    
    DataSet sf = Aus.q("SELECT doki.datdok, doki.vrdok, doki.cpar, doki.ckupac, stdoki.cart, stdoki.jm, stdoki.kol, stdoki.inab, stdoki.iprodbp, stdoki.uirab " +
    		"FROM doki,stdoki WHERE " + Util.getUtil().getDoc("doki","stdoki") + " AND " +
          Condition.between("DATDOK", from, to).qualified("doki") + " AND " +
    		"doki.vrdok in ('POD','GOT','ROT','GRN')");
    
    DataSet grn = Aus.q("SELECT doki.datdok, doki.cpar, stdoki.cart, stdoki.kol, stdoki.inab, stdoki.iprodbp, stdoki.uirab, stdoki.id_stavka, stdoki.veza " +
        "FROM doki,stdoki WHERE " + Util.getUtil().getDoc("doki","stdoki") + " AND " +
      Condition.between("DATDOK", from, to).qualified("doki") + " AND " +
        "doki.vrdok='GRN'");      
    
    DataSet otp = Aus.q("SELECT doki.datdok, doki.cpar, stdoki.cart, stdoki.kol, stdoki.inab, stdoki.iprodbp, stdoki.uirab, stdoki.id_stavka " +
        "FROM doki,stdoki WHERE " + Util.getUtil().getDoc("doki","stdoki") + " AND " +
      Condition.between("DATDOK", from, to).qualified("doki") + " AND " +
        "doki.vrdok='OTP'");
    
    StorageDataSet ds = new StorageDataSet();
    ds.setColumns(new Column[] {
        dM.createStringColumn("DATUM", 20),
        dM.createStringColumn("IZLAZ", 3),
        dM.createStringColumn("ART", 20),
        dM.createStringColumn("HORECA", 1),
        dM.createBigDecimalColumn("KG", 2),
        dM.createBigDecimalColumn("NI", 2),
        dM.createBigDecimalColumn("CK2", 2),
        dM.createBigDecimalColumn("NETO", 2),
        dM.createBigDecimalColumn("RAB", 2),
        dM.createBigDecimalColumn("PPK", 2)
    });
    ds.open();
    String[] cc = {"DATUM", "IZLAZ", "ART", "HORECA"}; 
    
    sf.setSort(new SortDescriptor(new String[] {"CART", "CPAR", "DATDOK"}));
    
    int cart = -1;
    String oldc = "";
    String oldd = "";
    String oldp = "";
    String oldh = "";
    
    
    for (sf.first(); sf.inBounds(); sf.next()) {
      String dat = Aus.formatTimestamp(sf.getTimestamp("DATDOK"));
      dat = dat.substring(0, dat.length() - 1);
      String izlaz = "VAN";
      String nc = "";
      String horeca = "H";
      
      if (cart != sf.getInt("CART")) {
        cart = sf.getInt("CART");
        ld.raLocate(art, "CART", Integer.toString(cart));
        if (ld.raLocate(ca, "CART", Integer.toString(cart)))
          nc = ca.getString("CCPAR");
        if (nc.length() == 0) nc = "X" + cart;
      } else nc = oldc;
      
      if (sf.getInt("CPAR") == cpar) {
        izlaz = "HRV";
        horeca = "T";
      }
      
      //if (sf.getString("VRDOK").equals("GOT") && sf.isNull("CKUPAC")) horeca = "T";
      
      if ((out != null && !dat.equals(oldd)) || !oldc.equals(nc) || !izlaz.equals(oldp) || !horeca.equals(oldh)) {
        oldd = dat;
        oldc = nc;
        oldp = izlaz;
        oldh = horeca;
        if (!ld.raLocate(ds, cc, new String[] {oldd, oldp, oldc, oldh})) {
          ds.insertRow(false);
          if (out != null) ds.setString("DATUM",  oldd);
          ds.setString("IZLAZ",  oldp);
          ds.setString("ART",  oldc);
          ds.setString("HORECA",  oldh);
        }
      }
      
      if (art.getInt("CPAR") == cpar) 
        Aus.add(ds, "NI", sf, "INAB");
      else Aus.add(ds, "CK2", sf, "INAB");
      
      BigDecimal kol = sf.getBigDecimal("KOL");
      if (!sf.getString("JM").equals("kg") && !sf.getString("JM").equals("KG") && art.getBigDecimal("TEZPAK").signum() > 0)
        kol = kol.multiply(art.getBigDecimal("TEZPAK")).setScale(2, BigDecimal.ROUND_HALF_UP);
      
      BigDecimal pp = Aus.zero0;
      if (art.getBigDecimal("PPK").signum() > 0)
        pp = kol.multiply(art.getBigDecimal("PPK")).setScale(2, BigDecimal.ROUND_HALF_UP);
      
      Aus.add(ds, "KG", kol);
      Aus.add(ds, "NETO", sf, "IPRODBP");
      Aus.add(ds, "RAB", sf, "UIRAB");
      Aus.add(ds, "PPK", pp);
    }
    
    ds.setSort(new SortDescriptor(new String[] {"DATUM", "ART", "IZLAZ"}));
    
    if (out != null) {
      TextFile tf = TextFile.write(out);
      tf.out("DATUM;PPD_IZVOR;PPD_IZLAZ;ARTIKL;ART_PORIJEKLO_PPD;TRZISTE_DRZAVA;" +
      		"TRGOVINA_HORECA;KOLICINA_KG;VALUTA;RF_UKUPNO_IZNOS;IZNOS_PRODAJE;NI;CK2;NR;ZT;PP");
      
      VarStr line = new VarStr();
      for (ds.first(); ds.inBounds(); ds.next()) {
        line.clear().append(ds.getString("DATUM")).append(";GAL;");
        line.append(ds.getString("IZLAZ")).append(';');
        line.append(ds.getString("ART")).append(';');
        line.append("T;D01;");
        line.append(ds.getString("HORECA")).append(';');
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("KG"))).append(';');
        line.append("kn;");
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("RAB"))).append(';');
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("NETO"))).append(';');
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("NI"))).append(';');
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("CK2"))).append(';');
        line.append("0;0;");
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("PPK")));
        tf.out(line.toString());
      }
      
      tf.close();
    }
    
    return ds;
  }

}
