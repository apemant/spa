package hr.restart.util;

import hr.restart.baza.Condition;
import hr.restart.baza.Rate;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.robno.Aut;
import hr.restart.robno.Util;
import hr.restart.robno.raControlDocs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class Hacks {
  static DataSet arts = dM.getDataModule().getArtikli();
  static QueryDataSet rate;
  static lookupData ld = lookupData.getlookupData();
  static String[] ikey = {"cskl", "vrdok", "god", "brdok", "rbsid"};

  static class Entry {
    static BigDecimal two = new BigDecimal(2);
    static BigDecimal ten = new BigDecimal(10);
    BigDecimal kol;
    BigDecimal vc;
    BigDecimal min;
    
    BigDecimal remain;
    int cart;
    public Entry(int _cart, String _kol, String _vc, String _min) {
      kol = remain = new BigDecimal(_kol);
      vc = new BigDecimal(_vc);
      min = new BigDecimal(_min);
      cart = _cart;
    }
    
    public BigDecimal getMin() {
      if (min.compareTo(remain) >= 0)
        return remain.multiply(vc);
      return min.multiply(vc);
    }
    
    public boolean isNeeded(BigDecimal gfact) {
      if (min.multiply(vc).compareTo(ten) >= 0)
        gfact = gfact.subtract(gfact.movePointLeft(1));
      return remain.signum() > 0 && getFactor().compareTo(gfact) >= 0;
    }
    
    public BigDecimal getMax(BigDecimal remainder) {
      return remain.min(min.multiply(remainder.divide(getMin(), 0, BigDecimal.ROUND_DOWN)));
    }
    
    public BigDecimal getHalf(BigDecimal remainder) {
      return min.multiply(remainder.divide(getMin().multiply(two), 0, BigDecimal.ROUND_DOWN));
    }
    
    public void sub(BigDecimal k) {
      remain = remain.subtract(k);
    }
    
    public BigDecimal getFactor() {
      return remain.divide(kol, 7, BigDecimal.ROUND_HALF_UP);
    }
  }
  
  static BigDecimal getTotal(Entry[] list) {
    BigDecimal total = Aus.zero2;
    for (int i = 0; i < list.length; i++)
      total = total.add(list[i].kol.multiply(list[i].vc));
    return total;
  }
  
  static BigDecimal getRemain(Entry[] list) {
    BigDecimal total = Aus.zero2;
    for (int i = 0; i < list.length; i++)
      total = total.add(list[i].remain.multiply(list[i].vc));
    return total;
  }
  
  static BigDecimal getFactor(Entry[] list) {
    return getRemain(list).divide(getTotal(list), 7, BigDecimal.ROUND_HALF_UP);
  }  
  
  public static void adjustAgro() {
    Entry[] list = new Entry[7];
    list[0] = new Entry(15006, "420.000", "520.00", "1.00");
    list[1] = new Entry(15004, "16800.000", "5.00", "70.00");
    list[2] = new Entry(15007, "30000.000", "0.65", "50.00");
    list[3] = new Entry(15003, "1345.000", "8.00", "1.00");
    list[4] = new Entry(15005, "10000.000", "4.80", "1.00");
    list[5] = new Entry(15002, "15000.000", "4.00", "1.00");
    list[6] = new Entry(15001, "30750.000", "3.20", "1.00");
    
    QueryDataSet zag, st;
    
    st = stdoki.getDataModule().getTempSet("1=0");
    st.open();
    
    rate = Rate.getDataModule().getTempSet();
    
    zag = Aus.q("SELECT * FROM doki WHERE doki.god='2009' and " +
        "doki.vrdok='GOT' and doki.cskl='1' and doki.cnacpl='G' and " +
        "doki.ckupac is null and doki.uirac<=5000 and doki.uirac>0");
        
    adjust(list, zag, st);
    
    zag = Aus.q("SELECT * FROM doki WHERE doki.god='2010' and " +
        "doki.vrdok='GOT' and doki.cskl='1' and doki.cnacpl='G' and " +
        "doki.ckupac is null and doki.uirac<=5000 and doki.uirac>0");
    
    adjust(list, zag, st);
    
    zag = Aus.q("SELECT * FROM doki WHERE doki.god='2010' and " +
        "doki.vrdok='GOT' and doki.cskl='4' and doki.cnacpl='G' and " +
        "doki.ckupac is null and doki.uirac<=5000 and doki.uirac>0");
    
    adjust(list, zag, st);
    
    st.saveChanges();
    rate.saveChanges();
  }
  
  public static void adjust(Entry[] list, QueryDataSet zag, QueryDataSet st) {
    /*BigDecimal total = Aus.zero2;
    BigDecimal iraz = Aus.zero2;
    for (st.first(); st.inBounds(); st.next()) {
      total = total.add(st.getBigDecimal("IPRODBP"));
      iraz = iraz.add(st.getBigDecimal("IRAZ"));
    }*/
    BigDecimal diff = Aus.zero2;
    System.out.println("TOTAL " + getTotal(list));
    System.out.println("REMAINING " + getRemain(list));
    
    
    for (zag.first(); zag.inBounds() && getRemain(list).signum() > 0; zag.next()) {
        QueryDataSet tst = stdoki.getDataModule().getTempSet(
              Condition.whereAllEqual(Util.mkey, zag));
        tst.open();
          
        System.out.println("RAC " + zag.getInt("BRDOK") + 
            ": " + zag.getBigDecimal("UIRAC"));
        
        BigDecimal total = Aus.zero0;
        BigDecimal uirac = diff;
        BigDecimal iraz = Aus.zero2;
        for (tst.first(); tst.inBounds(); tst.next()) {
          uirac = uirac.add(tst.getBigDecimal("IPRODBP"));
          iraz = iraz.add(tst.getBigDecimal("IRAZ"));
        }
        System.out.println("   -> STAV iprodbp = " + uirac +
            "  iraz = " + iraz);
        
       if (getRemain(list).compareTo(uirac) > 0) {
         BigDecimal gfact = getFactor(list);
         BigDecimal kum = Aus.zero2;
         
         int need = 0;
         for (int i = 0; i < 3; i++)
           if (list[i].isNeeded(gfact)) ++need;
         System.out.println("   -> GFACT " + gfact + "  needed " + need);
         HashMap picks = new HashMap();
         
         for (int i = 0; i < 7; i++) {
           if (list[i].isNeeded(getFactor(list))) {
             BigDecimal kol = --need > 0 ? 
                   list[i].getHalf(uirac.subtract(kum)) :
                     list[i].getMax(uirac.subtract(kum));
             
             if (kol.signum() > 0) {
               picks.put(list[i], kol);
               kum = kum.add(kol.multiply(list[i].vc));
               list[i].sub(kol);
               System.out.println("   -> ART " + list[i].cart +
                   ":  KOL " + kol + "  IBP " + kol.multiply(list[i].vc));
             }
           }
         }
         diff = uirac.subtract(kum);
         System.out.println("... kum " + kum + "  diff " + diff);
         
         BigDecimal kumi = Aus.zero2;
         
         short rbs = 0;
         for (Iterator i = picks.entrySet().iterator(); i.hasNext(); ) {
           Map.Entry me = (Map.Entry) i.next();
           Entry e = (Entry) me.getKey();
           st.insertRow(false);
           tst.copyTo(st);
           st.setShort("RBR", ++rbs);
           st.setInt("RBSID", rbs);
           st.setInt("CART", e.cart);
           if (ld.raLocate(arts, "CART", Integer.toString(e.cart)))
             Aut.getAut().copyArtFields(st, arts);
           st.setBigDecimal("KOL", (BigDecimal) me.getValue());
           st.setBigDecimal("UPRAB", Aus.zero2);
           st.setBigDecimal("UIRAB", Aus.zero2);
           st.setBigDecimal("FC", e.vc);
           Aus.mul(st, "INETO", "FC", "KOL");
           st.setBigDecimal("FVC", e.vc);
           Aus.mul(st, "IPRODBP", "FVC", "KOL");
           Aus.mul(st, "POR1", "IPRODBP", "PPOR1");
           st.setBigDecimal("POR1", st.getBigDecimal("POR1").
               movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));
           Aus.add(st, "IPRODSP", "IPRODBP", "POR1");
           Aus.mul(st, "FMC", "FVC", "PPOR1");
           Aus.add(st, "FMC", "FVC");
           Aus.set(st, "FMCPRP", "FMC");
           total = total.add(st.getBigDecimal("IPRODSP"));

           st.setBigDecimal("INAB", iraz.multiply(
               st.getBigDecimal("IPRODBP")).divide(
                   kum, 2, BigDecimal.ROUND_HALF_UP));
           Aus.set(st, "IRAZ", "INAB");
           Aus.div(st, "NC", "INAB", "KOL");
           Aus.set(st, "ZC", "NC");
           Aus.set(st, "VC", "FVC");
           Aus.set(st, "MC", "FMC");
           Aus.set(st, "IBP", "IPRODBP");
           Aus.set(st, "ISP", "IPRODSP");
           Aus.set(st, "UIPOR", "POR1");
           kumi = kumi.add(st.getBigDecimal("IRAZ"));
           
           st.setString("ID_STAVKA", raControlDocs.getKey(st, ikey, "stdoki"));
         }
         BigDecimal idiff = iraz.subtract(kumi);
         System.out.println("... kumi " + kumi + "  idiff " + idiff);
         Aus.add(st, "IRAZ", idiff);
         Aus.add(st, "INAB", idiff);
         zag.setBigDecimal("UIRAC", total);
         zag.setInt("CKUPAC", 999999);
         if (ld.raLocate(rate, Util.mkey, new String[] {
             zag.getString("CSKL"), zag.getString("VRDOK"),
             zag.getString("GOD"), "" + zag.getInt("BRDOK")}))
           rate.setBigDecimal("IRATA", total);
         
         tst.deleteAllRows();
         tst.saveChanges();
       }
    }
    zag.saveChanges();
  }
}
