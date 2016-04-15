package hr.restart.robno;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.Sklad;
import hr.restart.baza.Stanje;
import hr.restart.baza.VTCartPart;
import hr.restart.baza.dM;
import hr.restart.sisfun.TextFile;
import hr.restart.util.Aus;
import hr.restart.util.HashDataSet;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


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
      		"TRGOVINA_HORECA;KOLICINA_KG;VALUTA;RF_OR;RF_RAR;RF_DAR;RF_CS;RF_UKUPNO_IZNOS;" +
      		"IZNOS_PRODAJE;NI;CK2;NR_FIX;NR_VAR;NR_AKC;NR_CS;NR_OSTAL;ZT;PP;CAR");
      
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
        line.append("0;0;0;0;");
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("NETO"))).append(';');
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("NI"))).append(';');
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("CK2"))).append(';');
        line.append("0;0;0;0;0;0;");
        line.append(Aus.formatBigDecimal(ds.getBigDecimal("PPK"))).append(";0");
        tf.out(line.toString());
      }
      
      tf.close();
    }
    
    return ds;
  }
  
  public static DataSet fillCK2(int cpar, Timestamp from, Timestamp to, Workbook wb) {
    DataSet ca = VTCartPart.getDataModule().getTempSet(Condition.equal("CPAR", cpar));
    ca.open();
    
    HashMap arts = new HashMap();
    for (ca.first(); ca.inBounds(); ca.next()) 
      if (ca.getString("CCPAR").length() > 0)
        arts.put(ca.getString("CCPAR"), Integer.toString(ca.getInt("CART")));
    
    DataSet ul = Aus.q("SELECT stdoku.cart, sum(stdoku.kol) as kol, sum(stdoku.kol*artikli.tezpak) as kol2, sum(stdoku.inab) as inab " +
        "FROM doku,stdoku,artikli WHERE " + Util.getUtil().getDoc("doku", "stdoku") + " AND stdoku.cart=artikli.cart AND " +
        Condition.between("DATDOK", from, to).qualified("doku") + " AND doku.vrdok in ('PRK','KAL','PTE') GROUP BY stdoku.cart");
    
    HashDataSet ds = new HashDataSet(ul, "CART");
    
    System.out.println(((QueryDataSet) ul).getOriginalQueryString());
    
    Sheet sh = wb.getSheetAt(0);
    for (int i = 6; i < 895; i++) {
      Row row = sh.getRow(i);
      if (row == null) continue;
      
      String rart = getStringValue(row, 1);
      if (rart == null || rart.length() == 0) continue;
      
      int dash = rart.indexOf(" - ");
      if (dash < 0) continue;
      String art = rart.substring(0, dash).trim();
      if (!arts.containsKey(art)) continue;
      
      if (!ds.loc(arts.get(art))) continue;
      
      BigDecimal kol2 = ds.get().getBigDecimal("KOL2");
      if (kol2.signum() != 0) {
        Cell c = row.getCell(4);
        c.setCellValue(ds.get().getBigDecimal("INAB").divide(kol2, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
      }
      BigDecimal kol = ds.get().getBigDecimal("KOL");
      if (kol.signum() != 0) {
        Cell c = row.getCell(8);
        c.setCellValue(ds.get().getBigDecimal("INAB").divide(kol, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
      }
    }
    return ul;
  }
  
  public static DataSet fillSheets(int cpar, Timestamp from, Timestamp to, Workbook wb) {
    DataSet ca = VTCartPart.getDataModule().getTempSet(Condition.equal("CPAR", cpar));
    ca.open();
    
    HashMap arts = new HashMap();
    for (ca.first(); ca.inBounds(); ca.next()) 
      if (ca.getString("CCPAR").length() > 0)
        arts.put(new Integer(ca.getInt("CART")), ca.getString("CCPAR"));
    HashSet artss = new HashSet(arts.values());
    
    DataSet sf = Aus.q("SELECT stdoki.cart, stdoki.kol*artikli.tezpak as kol, stdoki.ineto, stdoki.iprodbp " +
        "FROM doki,stdoki,artikli WHERE " + Util.getUtil().getDoc("doki","stdoki") + " AND stdoki.cart=artikli.cart AND " +
      Condition.between("DATDOK", from, to).qualified("doki") + " AND " +
        "doki.vrdok in ('POD','GOT','ROT', 'GRN') ORDER BY stdoki.cart");
    
    System.out.println(((QueryDataSet) sf).getOriginalQueryString());
    
    String god = hr.restart.util.Util.getUtil().getYear(from);
    
    DataSet ds = Aus.q("SELECT stanje.cart, MAX(stanje.vc*artikli.tezkol) as vc FROM stanje,artikli WHERE stanje.cart=artikli.cart AND " +
        Condition.equal("GOD", god).and(Condition.in("CART", arts.keySet())).qualified("stanje") + " GROUP BY stanje.cart");
    
    HashDataSet st = new HashDataSet(ds, "CART");
    
    int cart = -99;
    StorageDataSet out = Aus.createSet("CART CCPAR:20 KOL.3 INETO.2 IPRODBP.2 VC.2 FVC.2");
    for (sf.first(); sf.inBounds(); sf.next()) {
      if (sf.getInt("CART") != cart) {
        cart = sf.getInt("CART");
        if (arts.containsKey(new Integer(cart))) {
          out.insertRow(false);
          out.setInt("CART", cart);
          out.setString("CCPAR", (String) arts.get(new Integer(cart)));
          if (st.loc(sf)) out.setBigDecimal("VC", st.get().getBigDecimal("VC"));
        }
      }
      if (out.rowCount() > 0 && out.getInt("CART") == cart) {
        Aus.add(out, "KOL", sf);
        Aus.add(out, "INETO", sf);
        Aus.add(out, "IPRODBP", sf);
      }
    }
    //hr.restart.robno.raFranck.fillSheets(308378, hr.restart.util.Util.getUtil().getYearBegin("2015"), hr.restart.util.Util.getUtil().getYearEnd("2015"), null)
    
    for (out.first(); out.inBounds(); out.next()) 
      if (out.getBigDecimal("KOL").signum() != 0) {
        BigDecimal vc = out.getBigDecimal("INETO").divide(out.getBigDecimal("KOL"), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal fvc = out.getBigDecimal("IPRODBP").divide(out.getBigDecimal("KOL"), 2, BigDecimal.ROUND_HALF_UP);
        if (vc.compareTo(out.getBigDecimal("VC")) > 0)
          out.setBigDecimal("VC", vc);
        out.setBigDecimal("FVC", fvc);
      }
    
    if (wb != null) {
      fillSheet(wb.getSheetAt(0), new HashDataSet(out, "CCPAR"), artss);
      fillSheet(wb.getSheetAt(1), new HashDataSet(out, "CCPAR"), artss);
    }
    
    return out;
  }
  
  private static void fillSheet(Sheet sh, HashDataSet ds, HashSet artss) {
    for (int i = 1090; i < 2156; i++) {
      Row row = sh.getRow(i);
      
      String rart = getStringValue(row, 1);
      if (rart == null || rart.length() == 0) continue;
      
      int dash = rart.indexOf(" - ");
      if (dash < 0) continue;
      String art = rart.substring(0, dash).trim();
      if (!artss.contains(art)) continue;
      
      if (!ds.loc(art)) continue;
      
      Cell c = row.getCell(2);
      c.setCellValue(ds.get().getBigDecimal("FVC").doubleValue());
      
      c = row.getCell(10);
      c.setCellValue(ds.get().getBigDecimal("VC").doubleValue());
      
      if (ds.get().getBigDecimal("VC").signum() > 0) {
        BigDecimal perc = ds.get().getBigDecimal("VC").subtract(ds.get().getBigDecimal("FVC")).
          divide(ds.get().getBigDecimal("VC"), 4, BigDecimal.ROUND_HALF_UP).movePointRight(2);
        
        c = row.getCell(8);
        c.setCellValue(perc.doubleValue());
        
        c = row.getCell(12);
        c.setCellValue(perc.doubleValue());
      }
    }
  }
  
  public static void fillSheet(int cpar, Timestamp from, Timestamp to, Workbook wb) {
    
    DataSet sf = Aus.q("SELECT doki.cskl, doki.datdok, doki.vrdok, doki.cpar, stdoki.cart, stdoki.kol*artikli.tezpak as kol, stdoki.iprodbp " +
        "FROM doki,stdoki,artikli WHERE " + Util.getUtil().getDoc("doki","stdoki") + " AND stdoki.cart=artikli.cart AND " +
      Condition.between("DATDOK", from, to).qualified("doki") + " AND " +
        "doki.vrdok in ('POD','GOT','ROT')");
    
    String year = Integer.toString(Aus.getNumber(hr.restart.util.Util.getUtil().getYear(from)) - 1);
    Timestamp yb =  hr.restart.util.Util.getUtil().getYearBegin(year);
    Timestamp ye =  hr.restart.util.Util.getUtil().getYearEnd(year);
    
    DataSet ly = Aus.q("SELECT doki.cskl, doki.datdok, doki.vrdok, doki.cpar, stdoki.cart, stdoki.kol*artikli.tezpak as kol, stdoki.iprodbp " +
        "FROM doki,stdoki,artikli WHERE " + Util.getUtil().getDoc("doki","stdoki") + " AND stdoki.cart=artikli.cart AND " +
      Condition.between("DATDOK", yb, ye).qualified("doki") + " AND " +
        "doki.vrdok in ('POD','GOT','ROT')");
    
    DataSet sklad = Sklad.getDataModule().getAktiv();
    sklad.open();
    
    DataSet org = Orgstruktura.getDataModule().getAktiv();
    org.open();
    
    HashMap dest = new HashMap();
    for (sklad.first(); sklad.inBounds(); sklad.next()) 
      if (lookupData.getlookupData().raLocate(org, "CORG",  sklad)) {
        String pp = org.getString("FPP");
        if (pp.equals("1") || pp.equals("11") || pp.equals("12"))
          dest.put(sklad.getString("CSKL"), "1");
        else if (pp.equals("2")) dest.put(sklad.getString("CSKL"), "3"); 
        else if (pp.equals("3")) dest.put(sklad.getString("CSKL"), "2");
        else if (pp.equals("4")) dest.put(sklad.getString("CSKL"), "4");
        else if (pp.equals("10")) dest.put(sklad.getString("CSKL"), "5");
      }

    DataSet ca = VTCartPart.getDataModule().getTempSet(Condition.equal("CPAR", cpar));
    ca.open();

    HashMap arts = new HashMap();
    for (ca.first(); ca.inBounds(); ca.next()) 
      if (ca.getString("CCPAR").length() > 0)
        arts.put(new Integer(ca.getInt("CART")), ca.getString("CCPAR"));
    HashSet artss = new HashSet(arts.values());
    
    HashMap fresh = new HashMap();
    fillMap(cpar, fresh, sf, dest, arts);
    
    HashMap old = new HashMap();
    fillMap(cpar, old, ly, dest, arts);
    
    System.out.println(old);
    
    for (int sheet = 1; sheet <= 6; sheet++) {
      fillSheet(wb, "" + sheet, "franck", 0, 6, 22, fresh, old, artss);
      fillSheet(wb, "" + sheet, "horeca", 0, 21, 142, fresh, old, artss);
      
      fillSheet(wb, "" + sheet, "franck", 1, 145, 161, fresh, old, artss);
      fillSheet(wb, "" + sheet, "horeca", 1, 161, 281, fresh, old, artss);
    }
    
    Sheet tot = wb.getSheetAt(5);
    
    for (int i = 6; i <= 281; i++) {
      Row row = tot.getRow(i);
      
      String rart = getStringValue(row, 1);
      if (rart == null || rart.length() == 0) continue;
      
      int dash = rart.indexOf(" - ");
      if (dash < 0) continue;
      String art = rart.substring(0, dash).trim();
      if (!artss.contains(art)) continue;
      
      String val = getStringValue(row, 13 * 5 - 2);
      if (val != null && val.startsWith("=")) break;
      
      double maink = getIntValue(row, 13 * 5 - 2) / (double) getIntValue(row, 13 * 5 - 3);
      for (int s = 0; s < 5; s++) {
        Sheet sh = wb.getSheetAt(s);
        Row r = sh.getRow(i);
        int poldv = getIntValue(r, 13 * 5 - 3);
        Cell cell = r.getCell((short) (13 * 5 - 2));
        cell.setCellValue((int) (poldv * maink));
      }
      
      for (int m = 1; m <= 12; m++) {
        
        double mkoef = getIntValue(row, m * 5 - 2) / (double) getIntValue(row, 13 * 5 - 2); 
       
        System.out.println("Artikl " + art + "  mj " + m + " = koef "+ mkoef + "  ratio " + maink);
        
        for (int s = 0; s < 5; s++) {
          Sheet sh = wb.getSheetAt(s);
          
          Row r = sh.getRow(i);
          
          int poldv = getIntValue(r, 13 * 5 - 2);
          Cell cell = r.getCell((short) (m * 5 - 2));
          cell.setCellValue((int) (poldv * mkoef));
        }
      }
    }
    
  }
  
  static void fillSheet(Workbook wb, String sheet, String src, int bd, int rfrom, int rto, HashMap fresh, HashMap old, HashSet arts) {
    Sheet sh = wb.getSheetAt(Aus.getNumber(sheet) - 1);
    for (int i = rfrom; i <= rto; i++) {
      Row row = sh.getRow(i);
      
      String rart = getStringValue(row, 1);
      if (rart == null || rart.length() == 0) continue;
      
      int dash = rart.indexOf(" - ");
      if (dash < 0) continue;
      String art = rart.substring(0, dash).trim();
      if (!arts.contains(art)) continue;
      
      for (int m = 1; m <= 13; m++) {
        String val = getStringValue(row, m * 5 - 2);
        if (val != null && val.startsWith("=")) break;
        
        Cell cell = row.getCell((short) (m * 5 - 3));
        cell.setCellValue(getVal(old, src, sheet, m, art, bd));
        
        cell = row.getCell((short) (m * 5 - 1));
        cell.setCellValue(getVal(fresh, src, sheet, m, art, bd));
      }
    }
  }
  
  static int getVal(HashMap data, String src, String vpart, int m, String art, int bd) {
    HashMap part = (HashMap) data.get(src);
    if (part == null) return 0;
    
    HashMap div = (HashMap) part.get(vpart);
    if (div == null) return 0;
    
    HashMap mon = (HashMap) div.get(new Integer(m));
    if (mon == null) return 0;
    
    BigDecimal[] tot = (BigDecimal[]) mon.get(art);
    if (tot == null) return 0;
    
    return tot[bd].setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
  }

  static void fillMap(int cpar, HashMap data, DataSet ds, HashMap dest, HashMap arts) {
    for (ds.first(); ds.inBounds(); ds.next()) {
      String pp = (String) dest.get(ds.getString("CSKL"));
      String art = (String) arts.get(new Integer(ds.getInt("CART")));
      if (pp != null && art != null) {
        String vpart = "horeca";
        if (ds.getInt("CPAR") == cpar) vpart = "franck";
        
        HashMap part = (HashMap) data.get(vpart);
        if (part == null) data.put(vpart, part = new HashMap());
        
        HashMap div = (HashMap) part.get(pp);
        if (div == null) part.put(pp, div = new HashMap());
        
        Integer month = Integer.valueOf(hr.restart.util.Util.getUtil().getMonth(ds.getTimestamp("DATDOK")));
        fillArt(ds, div, month, art);
        fillArt(ds, div, new Integer(13), art);
        
        HashMap total = (HashMap) part.get("6");
        if (total == null) part.put("6",  total = new HashMap());
        
        fillArt(ds, total, month, art);
        fillArt(ds, total, new Integer(13), art);
      }
    }
  }
  
  static void fillArt(DataSet ds, HashMap div, Integer month, String art) {
    HashMap mon = (HashMap) div.get(month);
    if (mon == null) div.put(month, mon = new HashMap());
    
    BigDecimal[] tot = (BigDecimal[]) mon.get(art);
    if (tot == null) mon.put(art, tot = new BigDecimal[] {Aus.zero0, Aus.zero0});
    tot[0] = tot[0].add(ds.getBigDecimal("KOL"));
    tot[1] = tot[1].add(ds.getBigDecimal("IPRODBP"));
  }
  
  static String getStringValue(Row row, int num) {
    Cell cell = row.getCell((short) num);
    if (cell == null) return "";
    if (cell.getCellType() == Cell.CELL_TYPE_STRING)
      return cell.getStringCellValue();
    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
      return "" + (long) cell.getNumericCellValue();
    return "";
  }
  
  static int getIntValue(Row row, int num) {
    Cell cell = row.getCell((short) num);
    if (cell == null) return 0;
    if (cell.getCellType() == Cell.CELL_TYPE_STRING)
      return 0;
    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
      return (int) cell.getNumericCellValue();
    return 0;
  }
  
  static String getNumValue(Row row, int num) {
    Cell cell = row.getCell((short) num);
    if (cell == null) return "";
    if (cell.getCellType() == Cell.CELL_TYPE_STRING)
      return "";
    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
      return "" + BigDecimal.valueOf(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    return "";
  }
}