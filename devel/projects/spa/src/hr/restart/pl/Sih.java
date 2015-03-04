package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.baza.Kumulrad;
import hr.restart.baza.Kumulradarh;
import hr.restart.baza.Odbiciobr;
import hr.restart.baza.Prisutobr;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.Vrsteprim;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.HashDataSet;
import hr.restart.util.HashSum;
import hr.restart.util.Key;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.raProcess;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jb.util.TriStateProperty;


public class Sih {

  static long mili = 0;
  
  public static String[] dtj = {"", "Ned", "Pon", "Uto", "Sri", "Èet", "Pet", "Sub"};
  
  private Sih() {
    // static class
  }
  
  public static void start() {
    System.out.println("Poèetak...");
    mili = System.currentTimeMillis();
  }
  
  public static void report(String what) {
    System.out.println(what + "  " + (System.currentTimeMillis() - mili));
  }
  
  public static StorageDataSet[] findKumRad(List data, int god, int mje, HashDataSet vrprims, HashSum neto, HashSum dopp) {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, god);
    cal.set(cal.MONTH, mje - 1);    
    int maxDana = cal.getActualMaximum(cal.DATE);
    
    StorageDataSet sums = new StorageDataSet();   
    sums.setColumns(new Column[] {
        dM.createStringColumn("PRIM", "Primanje", 50),
        dM.createColumn("SATI", "Sati", null, Variant.BIGDECIMAL, 2, 10, 2),
        dM.createColumn("IZNOS", "Iznos", null, Variant.BIGDECIMAL, 2, 10, 2)
    });
    sums.open();
    HashSum primsati = new HashSum();
    HashSum primiznos = new HashSum();
    
    HashSet grs = new HashSet();
    HashSum[] dani = new HashSum[maxDana];
    VarStr[] mods = new VarStr[maxDana];
    for (int i = 0; i < maxDana; i++) {
      dani[i] = new HashSum();
      mods[i] = new VarStr();
    }
    for (Iterator i = data.iterator(); i.hasNext(); ) {
      Data pd = (Data) i.next();
      grs.add(pd.grpris);
      if (pd.dan > maxDana) {
        System.out.println("Weird bug! dan = " + pd.dan);
        pd.dan = maxDana;
      }
      if (pd.dan > 0 && pd.sati != null && pd.sati.signum() > 0) {
        dani[pd.dan - 1].add(pd.grpris, pd.sati);
        String old = mods[pd.dan - 1].extract(pd.grpris+":", "|");
        if (old == null) mods[pd.dan - 1].append(pd.grpris).append(':').
          append(vrprims.get(Integer.toString(pd.cvrp)).getString("CGRPRIM")).append('|');
        else {
          int beg = mods[pd.dan - 1].indexOf(pd.grpris+":");
          int end = mods[pd.dan - 1].indexOf('|', beg);
          mods[pd.dan - 1].replace(beg, end, pd.grpris+":"+old+","+
              vrprims.get(Integer.toString(pd.cvrp)).getString("CGRPRIM"));
        }
      }
      primsati.add(Integer.toString(pd.cvrp), pd.sati);
      primiznos.add(Integer.toString(pd.cvrp), pd.iznos);
    }
    
    Column[] cols = new Column[grs.size() + 4];
    cols[0] = dM.createStringColumn("MOD", "Modif", 500);
    cols[0].setVisible(TriStateProperty.FALSE);
    cols[1] = dM.createIntColumn("DAN", "Dan");
    cols[2] = dM.createStringColumn("DTJE", "Tje", 3);
    cols[3] = dM.createColumn("UK", "Ukupno", null, Variant.BIGDECIMAL, 2, 6, 2);
    ArrayList lgr = new ArrayList(grs);
    Collections.sort(lgr);
    for (int i = 0; i < lgr.size(); i++)
        cols[i+4] = dM.createColumn((String) lgr.get(i), (String) lgr.get(i), null, Variant.BIGDECIMAL, 2, 6, 2);
    
    lgr.add("UK");
    
    StorageDataSet out = new StorageDataSet();
    out.setLocale(Aus.hr);
    out.setColumns(cols);
    out.open();
    
    for (int i = 0; i < maxDana; i++) {
        out.insertRow(false);
        out.setString("MOD", mods[i].toString());
        out.setInt("DAN", i + 1);
        cal.set(cal.DATE, i + 1);
        out.setString("DTJE", dtj[cal.get(cal.DAY_OF_WEEK)]);
        
        for (Iterator g = dani[i].iterator(); g.hasNext(); ) {
            String gr = (String) g.next();
            out.setBigDecimal(gr, (BigDecimal) dani[i].get(gr));
            Aus.add(out, "UK", gr);
        }
    }
    
    // primanja
    BigDecimal totalPrim = Aus.zero0;
    BigDecimal totalSati = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if (primsati.get(key) != null && primsati.get(key).signum() > 0) {
        insertSum(sums, vrprims, key, primsati.get(key), primiznos.get(key));
        totalPrim = totalPrim.add(primiznos.get(key));
        totalSati = totalSati.add(primsati.get(key));
      }
    }
    insertSum(sums, vrprims, "UKUPNO", totalSati, totalPrim);
    
    // naknade
    BigDecimal totalNak = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if ((primsati.get(key) == null || primsati.get(key).signum() == 0) 
          && primiznos.get(key).signum() > 0) {
        insertSum(sums, vrprims, key, null, primiznos.get(key));
        totalNak = totalNak.add(primiznos.get(key));
      }
    }
    if (totalNak.signum() > 0)
      insertSum(sums, vrprims, "NAKNADE", null, totalNak);
    
    // obustave
    BigDecimal totalOb = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if ((primsati.get(key) == null || primsati.get(key).signum() == 0) 
          && primiznos.get(key).signum() < 0) {
        insertSum(sums, vrprims, key, null, primiznos.get(key));
        totalOb = totalOb.add(primiznos.get(key));
      }
    }
    if (totalOb.signum() > 0)
      insertSum(sums, vrprims, "OBUSTAVE", null, totalOb);
    
    insertSum(sums, vrprims, "", null, null);
    insertSum(sums, vrprims, "SVEUKUPNO", totalSati, totalPrim.add(totalNak).add(totalOb));
    
    String key = ((Data) data.get(0)).cradnik;
    if (neto.get(key) != null) {
      insertSum(sums, vrprims, "", null, null);
      insertSum(sums, vrprims, "PLAÆA S LISTE", null, neto.get(key));
      insertSum(sums, vrprims, "RAZLIKA PLAÆE", null, totalPrim.add(totalNak).add(totalOb).subtract(neto.get(key)));
      insertSum(sums, vrprims, "", null, null);
      insertSum(sums, vrprims, "", null, null);
      insertSum(sums, vrprims, "DOPRINOSI I POREZI", null, dopp.get(key));
      insertSum(sums, vrprims, "BRUTO PLAÆA", null, totalPrim.add(totalNak).add(totalOb).add(dopp.get(key)));
    }
    
    return new StorageDataSet[] {out, sums};
  }
  
  
  public static StorageDataSet generateArhCorgGrupeIzvj(String nazGrupe, int god, int mj, Condition cond) {
    start();
    HashSum[] netodopp = loadArhNetoDopp(god, mj, cond);
    return generateGrupeIzvj(nazGrupe, loadPrisutarh(god, mj, cond), createOjMap(), netodopp[0], netodopp[1]);
  }
  
  public static StorageDataSet generateCorgGrupeIzvj(String nazGrupe, Condition cond) {
    start();
    HashSum[] netodopp = loadNetoDopp(cond);
    return generateGrupeIzvj(nazGrupe, loadPrisutobr(cond), createOjMap(), netodopp[0], netodopp[1]);
  }
  
  public static StorageDataSet generateArhGrupeIzvj(String nazGrupe, int god, int mj, Condition cond) {
    start();
    HashSum[] netodopp = loadArhNetoDopp(god, mj, cond);
    return generateGrupeIzvj(nazGrupe, loadPrisutarh(god, mj, cond), null, netodopp[0], netodopp[1]);
  }
  
  public static StorageDataSet generateGrupeIzvj(String nazGrupe, Condition cond) {
    start();
    HashSum[] netodopp = loadNetoDopp(cond);
    return generateGrupeIzvj(nazGrupe, loadPrisutobr(cond), null, netodopp[0], netodopp[1]);
  }
  
  static HashMap createOjMap() {
    DataSet ds = Radnicipl.getDataModule().getTempSet("CRADNIK CORG");
    raProcess.openScratchDataSet(ds);
    HashMap ret = new HashMap();
    for (ds.first(); ds.inBounds(); ds.next())
      ret.put(ds.getString("CRADNIK"), ds.getString("CORG"));
    
    return ret;
  }
  
  public static StorageDataSet generateGrupeIzvj(String nazGrupe, List data, HashMap radoj, HashSum neto, HashSum dopp) {
    StorageDataSet out = new StorageDataSet();
    if (radoj != null)
      out.addColumn(Radnicipl.getDataModule().getColumn("CORG").cloneColumn());
    out.addColumn(dM.createStringColumn("CSIF", nazGrupe, 5));
    out.addColumn(Prisutobr.getDataModule().getColumn("CVRP").cloneColumn());
    out.addColumn(Vrsteprim.getDataModule().getColumn("NAZIV").cloneColumn());
    out.addColumn(Prisutobr.getDataModule().getColumn("SATI").cloneColumn());
    out.addColumn(Prisutobr.getDataModule().getColumn("IZNOS").cloneColumn());
    out.addColumn(dM.createBigDecimalColumn("UNC", 2));
    out.setLocale(Aus.hr);
    out.getColumn("CVRP").setCaption("Vrsta");
    out.getColumn("NAZIV").setCaption("Naziv primanja");
    out.getColumn("UNC").setVisible(TriStateProperty.FALSE);
    out.open();
    HashDataSet vrprims = new HashDataSet(dM.getDataModule().getVrsteprim(), "CVRP");

    HashMap sums = new HashMap();
    HashSum rads = new HashSum();
    HashSum grads = new HashSum();
    HashMap grupe = new HashMap();
    report("Zbrajanje...");
    int m = 0;
    for (Iterator i = data.iterator(); i.hasNext(); ) {
      if (m++ % 200 == 0) raProcess.checkClosing();
      Data pd = (Data) i.next();
      String oj = radoj == null ? null : (String) radoj.get(pd.cradnik);
      Key k = radoj == null ? new Key(pd.grpris, pd.cvrp) : new Key(oj, pd.grpris, pd.cvrp); 
      Data sum = (Data) sums.get(k);
      if (sum == null) sums.put(k, pd);
      else sum.add(pd);
      Object ok = radoj == null ? pd.grpris : (Object) new Key(oj, pd.grpris);
      HashSet rg = (HashSet) grupe.get(ok);
      if (rg == null) grupe.put(ok, rg = new HashSet());
      rg.add(pd.cradnik);
      rads.add(pd.cradnik, pd.iznos);
      grads.add(new Key(pd.grpris, pd.cradnik), pd.iznos);
    }
    report("Punjenje...");
    for (Iterator i = sums.keySet().iterator(); i.hasNext(); ) {
      if (m++ % 100 == 0) raProcess.checkClosing();
      Key k = (Key) i.next();
      Data sum = (Data) sums.get(k);
      out.insertRow(false);
      if (radoj != null) out.setString("CORG", k.s1);
      out.setString("CSIF", sum.grpris);
      out.setShort("CVRP", (short) sum.cvrp);
      if (vrprims.has(String.valueOf(sum.cvrp)))
        out.setString("NAZIV", vrprims.get(String.valueOf(sum.cvrp)).getString("NAZIV"));
      if (sum.sati != null) out.setBigDecimal("SATI", sum.sati);
      out.setBigDecimal("IZNOS", sum.iznos == null ? Aus.zero0 : sum.iznos);
      Aus.set(out, "UNC", "IZNOS");
    }
    if (neto.total().signum() > 0) {
      HashSet zeros = new HashSet();
      for (Iterator i = grupe.keySet().iterator(); i.hasNext(); ) {
        raProcess.checkClosing();
        Object k = i.next();
        HashSet rg = (HashSet) grupe.get(k);
        String gr = radoj == null ? (String) k : ((Key) k).s2;
        BigDecimal doppor = Aus.zero0, plista = Aus.zero0;
        for (Iterator r = rg.iterator(); r.hasNext(); ) {
          String rad = (String) r.next();
          if (neto.get(rad) == null) continue;
          if (rads.get(rad).signum() == 0 && zeros.add(rad)) {
            doppor = doppor.add(dopp.get(rad));
            plista = plista.add(neto.get(rad));
          } else {
            doppor = doppor.add(dopp.get(rad).multiply(grads.get(new Key(gr, rad))).divide(rads.get(rad), 2, BigDecimal.ROUND_HALF_UP));
            plista = plista.add(neto.get(rad).multiply(grads.get(new Key(gr, rad))).divide(rads.get(rad), 2, BigDecimal.ROUND_HALF_UP));
          }
        }
        
        out.insertRow(false);
        if (radoj != null) out.setString("CORG", ((Key) k).s1);
        out.setString("CSIF", gr);
        out.setShort("CVRP", (short) 1000);
        out.setString("NAZIV", "Doprinosi i porezi");
        out.setBigDecimal("IZNOS", doppor);
        Aus.set(out, "UNC", "IZNOS");
        
        out.insertRow(false);
        if (radoj != null) out.setString("CORG", ((Key) k).s1);
        out.setString("CSIF", gr);
        out.setShort("CVRP", (short) 1001);
        out.setString("NAZIV", "Plaæa s liste (nije u zbroju)");
        out.setBigDecimal("UNC", plista);
      }
    }
    report("Gotovo.");
    return out;
  }
  
  public static StorageDataSet[] generateArhPrimanjaIzvj(int god, int mj, Condition cond) {
    start();
    HashSum[] netodopp = loadArhNetoDopp(god, mj, cond);
    return generatePrimanjaIzvj(loadPrisutarh(god, mj, cond), netodopp[0].total(), netodopp[1].total());
  }
  
  public static StorageDataSet[] generatePrimanjaIzvj(Condition cond) {
    start();
    HashSum[] netodopp = loadNetoDopp(cond);
    return generatePrimanjaIzvj(loadPrisutobr(cond), netodopp[0].total(), netodopp[1].total());
  }
  
  public static StorageDataSet[] generatePrimanjaIzvj(List data, BigDecimal neto, BigDecimal dopp) {
    Data zarade = new Data();
    Data naknade = new Data();
    Data obustave = new Data();
    
    StorageDataSet out = new StorageDataSet();
    out.setColumns(new Column[] {
      Prisutobr.getDataModule().getColumn("CVRP").cloneColumn(),
      Vrsteprim.getDataModule().getColumn("NAZIV").cloneColumn(),
      Prisutobr.getDataModule().getColumn("SATI").cloneColumn(),
      Prisutobr.getDataModule().getColumn("IZNOS").cloneColumn()
    });
    out.setLocale(Aus.hr);
    out.getColumn("CVRP").setCaption("Vrsta");
    out.getColumn("NAZIV").setCaption("Naziv primanja");
    out.open();
    
    HashDataSet vrprims = new HashDataSet(dM.getDataModule().getVrsteprim(), "CVRP");
    
    HashMap vsums = new HashMap();
    report("Zbrajanje...");
    int m = 0;
    for (Iterator i = data.iterator(); i.hasNext(); ) {
      if (m++ % 200 == 0) raProcess.checkClosing();
      Data pd = (Data) i.next();
      String key = String.valueOf(pd.cvrp);
      Data sum = (Data) vsums.get(key);
      if (sum == null) vsums.put(key, pd);
      else sum.add(pd);
      if (pd.sati != null && pd.sati.signum() > 0) zarade.add(pd);
      else if (pd.iznos != null && pd.iznos.signum() > 0) naknade.add(pd);
      else obustave.add(pd);
    }
    report("Punjenje...");
    for (Iterator i = vsums.values().iterator(); i.hasNext(); ) {
      if (m++ % 100 == 0) raProcess.checkClosing();
      Data sum = (Data) i.next();
      out.insertRow(false);
      out.setShort("CVRP", (short) sum.cvrp);
      out.setString("NAZIV", vrprims.get(String.valueOf(sum.cvrp)).getString("NAZIV"));
      if (sum.sati != null) out.setBigDecimal("SATI", sum.sati);
      out.setBigDecimal("IZNOS", sum.iznos == null ? Aus.zero0 : sum.iznos);
    }
    out.setSort(new SortDescriptor(new String[] {"CVRP"}));
    
    report("Totali...");
    StorageDataSet sums = new StorageDataSet();
    sums.setColumns(new Column[] {
        dM.createStringColumn("PRIM", "Primanje", 50),
        dM.createColumn("SATI", "Sati", null, Variant.BIGDECIMAL, 2, 10, 2),
        dM.createColumn("IZNOS", "Iznos", null, Variant.BIGDECIMAL, 2, 10, 2)
    });
    sums.open();
    
    insertSum(sums, vrprims, "UKUPNO ZARADE", zarade.sati, zarade.iznos);
    insertSum(sums, vrprims, "NAKNADE", null, naknade.iznos);
    insertSum(sums, vrprims, "OBUSTAVE", null, obustave.iznos);
    insertSum(sums, vrprims, "ZA ISPLATU", null, zarade.iznos.add(naknade.iznos).add(obustave.iznos));
    insertSum(sums, vrprims, "PLAÆA S LISTE", null, neto);
    insertSum(sums, vrprims, "DOPRINOSI I POREZI", null, dopp);
    insertSum(sums, vrprims, "SVEUKUPNO", zarade.sati, zarade.iznos.add(naknade.iznos).add(obustave.iznos).add(dopp));
    
    report("Gotovo.");
    return new StorageDataSet[] {out, sums};
  }
  
  static void insertSum(StorageDataSet sums, HashDataSet vrprims, String key, BigDecimal sati, BigDecimal iznos) {
    sums.insertRow(false);
    if (vrprims.has(key))
      sums.setString("PRIM", vrprims.get(key).getString("NAZIV"));
    else sums.setString("PRIM", key);
    if (sati != null) sums.setBigDecimal("SATI", sati);
    if (iznos != null) sums.setBigDecimal("IZNOS", iznos);
  }
  
  public static StorageDataSet generateArhPotpisList(int god, int mj, Condition cond) {
    start();
    return generatePotpisList(loadPrisutarh(god, mj, cond), loadArhNeto(god, mj, cond));
  }
  
  public static StorageDataSet generatePotpisList(Condition cond) {
    start();
    return generatePotpisList(loadPrisutobr(cond), loadNeto(cond));
  }
  
  public static StorageDataSet generatePotpisList(List data, HashSum neto) {
    StorageDataSet out = new StorageDataSet();
    out.setColumns(new Column[] {
        Prisutobr.getDataModule().getColumn("CRADNIK").cloneColumn(),
        dM.createStringColumn("PRIME", "Prezime i ime", 100).cloneColumn(),
        dM.createBigDecimalColumn("TOTAL", "Ukupno plaæa", 2),
        dM.createBigDecimalColumn("PLISTA", "Plaæa s liste", 2),
        dM.createBigDecimalColumn("RAZLIKA", "Razlika plaæe", 2),
        dM.createStringColumn("POTPIS", "Potpis primaoca", 20)
    });
    out.setLocale(Aus.hr);
    out.open();
    HashDataSet rads = new HashDataSet(dM.getDataModule().getRadnici(), "CRADNIK");
    
    report("Zbrajanje...");
    HashSum sum = new HashSum();
    int m = 0;
    for (Iterator i = data.iterator(); i.hasNext(); ) {
      if (m++ % 200 == 0) raProcess.checkClosing();
      Data pd = (Data) i.next();
      sum.add(pd.cradnik, pd.iznos);
    }
    report("Punjenje...");
    for (Iterator i = sum.iterator(); i.hasNext(); ) {
      if (m++ % 100 == 0) raProcess.checkClosing();
      String key = (String) i.next();
      out.insertRow(false);
      out.setString("CRADNIK", key);
      out.setString("PRIME", rads.get(key).getString("PREZIME") + " " + rads.get(key).getString("IME"));
      out.setBigDecimal("TOTAL", sum.get(key));
      out.setBigDecimal("PLISTA", neto.get(key));
      Aus.sub(out, "RAZLIKA", "TOTAL", "PLISTA");
    }
    report("Gotovo.");
    return out;
  }
  
  public static HashSum[] loadArhNetoDopp(int god, int mj, Condition cond) {
    return loadNetoDopp(Kumulrad.getDataModule().getTempSet("CRADNIK NETOPK DOPRINOSI PORIPRIR", cond.and(getArhCond(god, mj))),
        Odbiciobr.getDataModule().getTempSet("CRADNIK OBRIZNOS", getDoprNaCond().and(cond).and(getArhCond(god, mj))));
  }
  
  public static HashSum[] loadNetoDopp(Condition cond) {
    return loadNetoDopp(Kumulrad.getDataModule().getTempSet("CRADNIK NETOPK DOPRINOSI PORIPRIR", cond),
        Odbiciobr.getDataModule().getTempSet("CRADNIK OBRIZNOS", getDoprNaCond().and(cond)));
  }
  
  public static Condition getArhCond(int god, int mj) {
    return Condition.equal("GODOBR", god).and(Condition.equal("MJOBR", mj));
  }
  
  public static Condition getDoprNaCond() {
    String[] odbns = raOdbici.getInstance().getVrsteOdbKeysQuery("POVR", "S", "1", "1", true);
    short[] odbn = new short[odbns.length];
    for (int i = 0; i < odbns.length; i++) odbn[i] = (short) Aus.getNumber(odbns[i]);
    
    return Condition.in("CVRODB", odbn);
  }
  
  // netods: netopk, doprinosi,poriprir;  dopna: obriznos
  public static HashSum[] loadNetoDopp(DataSet netods, DataSet dopna) {
    report("Dohvat kumulativa...");
    if (!raProcess.isRunning()) netods.open();
    else raProcess.openScratchDataSet(netods);
    
    HashSum retn = new HashSum(netods, "CRADNIK", "NETOPK");
    HashSum retd = new HashSum(netods, "CRADNIK", "DOPRINOSI");
    for (netods.first(); netods.inBounds(); netods.next()) {
      retn.add();
      retd.add();
      retd.addVal("PORIPRIR");
    }

    report("Dohvat odbitaka...");
    if (!raProcess.isRunning()) dopna.open(); 
    else raProcess.openScratchDataSet(dopna);
    
    retd.set(dopna, "OBRIZNOS");
    for (dopna.first(); dopna.inBounds(); dopna.next()) 
      retd.add();
    
    return new HashSum[] {retn, retd};
  }
  
  public static HashSum loadArhNeto(int god, int mj, Condition cond) {
    return loadNeto(Kumulradarh.getDataModule().getTempSet("CRADNIK NETOPK", cond.and(getArhCond(god, mj))));
  }
  
  public static HashSum loadNeto(Condition cond) {
    return loadNeto(Kumulrad.getDataModule().getTempSet("CRADNIK NETOPK", cond));        
  }
  
  public static HashSum loadNeto(DataSet netods) {
    report("Dohvat kumulativa...");
    if (!raProcess.isRunning()) netods.open();
    else raProcess.openScratchDataSet(netods);
    
    HashSum ret = new HashSum(netods, "CRADNIK", "NETOPK");
    for (netods.first(); netods.inBounds(); netods.next())
      ret.add();
    return ret;
  }

  public static List loadPrisutarh(int god, int mj, Condition cond) {
    return loadPrisutnost("SELECT cradnik,cvrp,dan,grpris,sati,iznos FROM prisutarh WHERE " + cond.and(getArhCond(god, mj)));
  }
  
  public static List loadPrisutobr(Condition cond) {
    return loadPrisutnost("SELECT cradnik,cvrp,dan,grpris,sati,iznos FROM prisutobr"
        + (cond == Condition.none || cond == Condition.ident ? "" : " WHERE " + cond));
  }
  
  // cradnik,cvrp,dan,grpris,sati,iznos
  public static List loadPrisutnost(String query) {
    report("Uèitavanje prisutnosti...");
    List ret = new ArrayList();
    ResultSet rs = Util.openQuickSet(query);
    int i = 0;
    try {
      while (rs.next()) {
        if (i++ % 200 == 0) raProcess.checkClosing();
        ret.add(new Data(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Util.closeQuickSet(rs);
    return ret;
  }
  
  public static class Data {
    String cradnik;
    int dan;
    int cvrp;
    String grpris;
    BigDecimal sati;
    BigDecimal iznos;
    public Data(ResultSet row) {
      try {
        cradnik = row.getString(1).trim();
        cvrp = row.getShort(2);
        dan = row.getShort(3);
        grpris = row.getString(4).trim();
        sati = new BigDecimal(row.getDouble(5)).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (row.wasNull()) sati = null;
        iznos = new BigDecimal(row.getDouble(6)).setScale(2, BigDecimal.ROUND_HALF_UP);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error loading main query data");
      }
    }
    public Data(DataSet ds) {
      cradnik = ds.getString("CRADNIK");
      cvrp = ds.getShort("CVRP");
      dan = ds.getShort("DAN");
      grpris = ds.getString("GRPRIS");
      sati = ds.getBigDecimal("SATI");
      if (ds.isNull("SATI")) sati = null;
      iznos = ds.getBigDecimal("IZNOS");
    }
    
    public Data() {
      sati = Aus.zero0;
      iznos = Aus.zero0;
    }
    
    public void add(Data other) {
      if (other.sati != null)
        sati = sati == null ? other.sati : sati.add(other.sati);
      if (other.iznos != null)
        iznos = iznos == null ? other.iznos : iznos.add(other.iznos);
    }
  }
}
