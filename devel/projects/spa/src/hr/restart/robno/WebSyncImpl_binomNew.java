package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.help.MsgDispatcher;
import hr.restart.pos.presBlag;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Timer;
import javax.xml.rpc.ServiceException;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;


public class WebSyncImpl_binomNew implements WebSyncInterface {
  
  public Object lokk = new Object();
  
  public String apiKey;
  String oj, mskl;
  
  
  
  Set carts = new HashSet();
  Set sklads = new HashSet();
  
 
  public WebSyncImpl_binomNew() {
    try {
      
      apiKey = frmParam.getParam("sisfun", "webApikey", "", "ApiKey za web sync");
      
      oj = frmParam.getParam("sisfun", "webOJ", "39", "Org.jed web sync");
      
      mskl = frmParam.getParam("sisfun", "webMainSkl", "218", "Skladište za web artikle");
      DataSet ds = Aus.q("SELECT cart FROM stanje where cskl='" + mskl + "' and god='" + Valid.getValid().findYear() + "'");
//      DataSet ds = Aus.q("SELECT cart FROM stakcije WHERE cak='#web'");
      for (ds.first(); ds.inBounds(); ds.next())
        carts.add(new Integer(ds.getInt("CART")));
      
      String skl = frmParam.getParam("sisfun", "webSklads", "", "Skladišta za web sync");
      if (skl.indexOf(',') < 0) sklads.addAll(Arrays.asList(new VarStr(skl).split()));
      else sklads.addAll(Arrays.asList(new VarStr(skl).splitTrimmed(',')));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public boolean isWeb(int cart) {
    return carts.contains(new Integer(cart));
  }
  
  public boolean isWeb(String cskl) {
    return sklads.contains(cskl);
  }
  
  public Condition getSkladCond() {
    return Condition.in("CSKL", sklads.toArray()); 
  }

  public void deletePartner(int part) {
    try {
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();
      
      int resp = es.deletePartner(apiKey, part + "");
            
      if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public void updatePartner(DataSet ds) {
    try {
      System.out.println("update partner " + ds);
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();
      String ko = ds.getString("KO");
      String ime = ko.indexOf(' ') > 0 ? ko.substring(0, ko.indexOf(' ')).trim() : ko;
      String pre = ko.indexOf(' ') > 0 ? ko.substring(ko.indexOf(' ')).trim() : "";
      int resp = es.savePartner(apiKey, ds.getInt("CPAR") + "", ime, pre, ds.getString("ADR"), ds.getInt("PBR")+"", ds.getString("MJ"), 
          ds.getString("TEL"), ds.getString("EMADR"), ds.getString("NAZPAR"), ds.getString("OIB"), ds.getString("ADR"), ds.getString("AKTIV").equals("D"));
      if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public void updateStanje(int cart, DataSet ms) {
    Calendar c = Calendar.getInstance();
    c.setTime(new Timestamp(System.currentTimeMillis()));
    int hour = c.get(c.HOUR_OF_DAY);
    if (hour < 2 || hour > 4) return;
    
    DataSet ds = Stanje.getDataModule().getTempSet(getSkladCond().
        and(Condition.equal("CART", cart)).and(Condition.equal("GOD", ms)));
    ds.open();
    BigDecimal tot = Aus.zero0;
    for (ds.first(); ds.inBounds(); ds.next()) {
      tot = tot.add(ds.getBigDecimal("KOL"));
    }
    updateStanje(Integer.toString(cart), tot.intValue());
  }
  
  public void updateAllStanje() {
    String god = Valid.getValid().getKnjigYear("robno");
    if (dM.getDataModule().getKnjigod().getString("STATRADA").equals("D"))
      god = Valid.getValid().findYear();
    
    DataSet ds = Stanje.getDataModule().getTempSet(getSkladCond().and(Condition.equal("GOD", god)));
    ds.open();
    ds.setSort(new SortDescriptor(new String[] {"CART"}));
    int cart = -99;
    BigDecimal kol = Aus.zero3;
    System.out.println("updating all "+ ds.rowCount());
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (cart != ds.getInt("CART")) {
        if (cart != -99) updateStanje(Integer.toString(cart), kol.intValue());
        cart = ds.getInt("CART");
        kol = Aus.zero3;
      }
      kol = kol.add(ds.getBigDecimal("KOL"));
      //updateStanje(Integer.toString(ds.getInt("CART")), ds.getBigDecimal("KOL").intValue());
    }
    if (cart != -99) updateStanje(Integer.toString(cart), kol.intValue());
  }
  
  public void updateStanje(String cart, int count) {
    try {
      System.out.println("sending " + cart + " = " + count);
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();
      
      int resp = es.saveInventoryCount(apiKey, cart, count);
                  
      if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public void updatePopust(String cpar, String cart, BigDecimal discount) {
    try {
      System.out.println("update popust " + cart + " za " + cpar);
      
      DataSet ds = Stanje.getDataModule().getTempSet(getSkladCond().and(Condition.equal("CART", cart)).
          and(Condition.equal("GOD", Valid.getValid().getKnjigYear("robno"))));
      ds.open();
      System.out.println("stanje " + ds);
      System.out.println("cijena " + ds.getBigDecimal("VC"));
      BigDecimal disc = ds.getBigDecimal("VC").multiply(discount).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP);
      System.out.println("popust " + discount + " = " + disc);
      
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();

      int resp = es.saveDiscount(apiKey, cpar, cart, disc);

      if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public void importDocs() {
    synchronized (lokk) {
      importImpl();
    }
  }
  
  boolean importDoc(hr.binom.PurchaseBase pb) {
    
    //String cskl = (String) sklads.iterator().next();
    
    Aus.dumpVars(pb);
    
    hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
    lookupData ld = lookupData.getlookupData();
    raKalkulBDDoc rKD = new raKalkulBDDoc();
    //lookupData.getlookupData().raLocate(dM.getDataModule().getSklad(), "CSKL", cskl);
    //String corg = dM.getDataModule().getSklad().getString("CORG");
    
    String cpars = pb.getCliErpID();
    boolean nar = cpars.length() > 0;
    int cpar = 0;
    if (nar) {
      if (ld.raLocate(dM.getDataModule().getPartneri(), "CPAR", cpars))
        cpar = dM.getDataModule().getPartneri().getInt("CPAR");
      else if (ld.raLocate(dM.getDataModule().getPartneri(), "OIB", pb.getR1CompanyOIB()))
        cpar = dM.getDataModule().getPartneri().getInt("CPAR");
    }
    
    QueryDataSet dzg = doki.getDataModule().getTempSet(Condition.nil);
    QueryDataSet dst = stdoki.getDataModule().getTempSet(Condition.nil);
    
    dzg.open();
    dst.open();
    
    String god = Valid.getValid().getKnjigYear("robno");
    if (dM.getDataModule().getKnjigod().getString("STATRADA").equals("D"))
      god = Valid.getValid().findYear();
    
    
    QueryDataSet sta = Stanje.getDataModule().openTempSet(getSkladCond().
        and(Condition.equal("GOD", god)).and(Condition.where("KOL", Condition.GREATER_THAN, Aus.zero0)));
        
    
    dzg.insertRow(false);
    dzg.setString("CUSER", "sistem");
    dzg.setString("CSKL", nar ? mskl : oj);
    dzg.setString("VRDOK", nar ? "NKU" : "GRN");
    if (nar && cpar > 0)
      dzg.setInt("CPAR", cpar);
    
    dzg.setTimestamp("DATDOK", new Timestamp(pb.getBillTimestamp().getTime().getTime()));
    dzg.setTimestamp("DVO", dzg.getTimestamp("DATDOK"));
    dzg.setTimestamp("DATDOSP", dzg.getTimestamp("DATDOK"));
    dzg.setString("GOD", hr.restart.util.Util.getUtil().getYear(dzg.getTimestamp("DATDOK")));
    if (nar && cpar == 0)
      dzg.setString("OPIS", "Narucio " + pb.getR1CompanyTitle()); 
    
    if (!nar) {
      dzg.setInt("FBR", pb.getBillNr());
      dzg.setString("JIR", pb.getJIR());
      dzg.setString("FPP", presBlag.getFiskPP(dzg));
      dzg.setInt("FNU", presBlag.getFiskNapG(dzg));
      dzg.setString("FOK", pb.getJIR() != null && pb.getJIR().length() > 0 ? "D" : "N");
      
      if (pb.getOperatorOIB() != null && pb.getOperatorOIB().length() > 0) {
        if (ld.raLocate(dM.getDataModule().getUseri(), "OIB", pb.getOperatorOIB())) {
          dzg.setString("CUSER", dM.getDataModule().getUseri().getString("CUSER"));
        }
      }
      
      if (pb.getR1CompanyOIB() != null && pb.getR1CompanyOIB().length() > 0) {
        if (ld.raLocate(dM.getDataModule().getKupci(), "OIB", pb.getR1CompanyOIB())) {
          dzg.setInt("CKUPAC", dM.getDataModule().getKupci().getInt("CKUPAC"));
        } else if (ld.raLocate(dM.getDataModule().getPartneri(), "OIB", pb.getR1CompanyOIB())) {
          dzg.setInt("CPAR", dM.getDataModule().getPartneri().getInt("CPAR"));
        }
      }
    }
    
    String[] acc = {"CART", "CART1", "BC", "NAZART", "JM"};
    short rbr = 0;
    
    for (int i = 0; i < pb.getItems().length; i++) {
      hr.binom.PurchaseItemBase pib = pb.getItems()[i];
      
      int cart = Integer.parseInt(pib.getCode());
      int count = pib.getCount();
      BigDecimal mc = pib.getPrice();
      
      ld.raLocate(dM.getDataModule().getArtikli(), "CART", pib.getCode());
      
      dst.insertRow(false);
      dM.copyColumns(dzg, dst, Util.mkey);
      dM.copyColumns(dM.getDataModule().getArtikli(), dst, acc);
      dst.setShort("RBR", ++rbr);
      dst.setInt("RBSID", rbr);
      dst.setInt("CART", cart);
      dst.setBigDecimal("KOL", new BigDecimal(count));
      dst.setBigDecimal("FC", mc);
      //Aus.set(dst, "FMCPRP", "FMC");
      
      if (ld.raLocate(dM.getDataModule().getPorezi(), new String[] { "CPOR" },
          new String[] { dM.getDataModule().getArtikli().getString("CPOR") })) {
      
      dst.setBigDecimal("PPOR1",
          dM.getDataModule().getPorezi().getBigDecimal("POR1"));
      dst.setBigDecimal("PPOR2",
          dM.getDataModule().getPorezi().getBigDecimal("POR2"));
      dst.setBigDecimal("PPOR3",
          dM.getDataModule().getPorezi().getBigDecimal("POR3"));
      dst.setBigDecimal("UPPOR",
          dM.getDataModule().getPorezi().getBigDecimal("UKUPOR"));

 
      }
      
      //Aus.mul(dst, "FVC", new BigDecimal("0.8"));
      //Aus.set(dst, "FC", "FVC");
      if (!nar) {
        ld.raLocate(sta, "CART", pib.getCode());
        dst.setString("CSKLART", sta.getString("CSKL"));
        rKD.stanje.Init();
        lc.TransferFromDB2Class(sta,rKD.stanje);
        rKD.stanje.sVrSklad="N";
      }
        rKD.stavkaold.Init();
        rKD.stavka.Init();
        lc.TransferFromDB2Class(dst,rKD.stavka);
        //rKD.stavka.kol = dst.getBigDecimal("KOL");
        //rKD.stavka.fmc = dst.getBigDecimal("FMC");
          
        rKD.KalkulacijaStavke(nar ? "NKU" : "GRN","KOL",'N',mskl,false);
        //if (!nar) rKD.KalkulacijaStanje("GOT");
        lc.TransferFromClass2DB(dst,rKD.stavka);
        //if (!nar) lc.TransferFromClass2DB(sta,rKD.stanje);
     
           
      dst.setString("ID_STAVKA",
          raControlDocs.getKey(dst, new String[] { "cskl",
              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
      dst.post();
      //if (dM.getDataModule().getArtikli().getString("NAZPAK").equals("salespod")) {
      //  Aus.mul(dst, "KOL", dM.getDataModule().getArtikli(), "BRJED");
      //  Aus.div(dst, "FC", dM.getDataModule().getArtikli().getBigDecimal("BRJED"));
      //}
      
    }
    
    saveDoc(dzg, dst, sta);
    
    Valid.getValid().setSeqFilter("WEBSYNC");
    if (dM.getDataModule().getSeq().getDouble("BROJ") < pb.getID()) {
      dM.getDataModule().getSeq().setDouble("BROJ" , pb.getID());
      dM.getDataModule().getSeq().saveChanges();
    }
    
    return nar;
  }
    
  private void saveDoc(final QueryDataSet zag, final QueryDataSet st, final QueryDataSet sta) {
    new raLocalTransaction() {
      public boolean transaction() throws Exception {
        Util.getUtil().getBrojDokumenta(zag);
        for (st.first(); st.inBounds(); st.next())
          st.setInt("BRDOK", zag.getInt("BRDOK"));
        
        raTransaction.saveChanges(zag);
        raTransaction.saveChanges(st);
        //raTransaction.saveChanges(sta);
        return true;
      }
    }.execTransaction();
  }
  
  void importImpl() {
    try {
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();
      
      Valid.getValid().setSeqFilter("WEBSYNC");
          
      hr.binom.OrderBase ob = es.getOrders(apiKey, (int) dM.getDataModule().getSeq().getDouble("BROJ"));
      
      System.out.println(ob);
      
      hr.binom.PurchaseBase[] pb = ob.getData();
      if (pb != null) {
        int nars = 0, gots = 0;
        for (int i = 0; i < pb.length; i++) {
          if (importDoc(pb[i])) nars++;
          else gots++;
        }
        if (nars+gots > 0) {
          String users = frmParam.getParam("robno", "salepodNotify", "", "Popis korisnika za notifikaciju salepod");
          System.out.println("Users: " + users);
          String[] us = new VarStr(users).split();
          for (int i = 0; i < us.length; i++) {
            System.out.println("Sending to " + us[i]);
            MsgDispatcher.send("sistem", us[i], "Dohvaæeno " + nars + " narudžbi i " +gots+" racuna s webshop servera.");
          }
        }
      }

      
      
      //if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public void install(int delay) {
    Timer t = new Timer(delay*1000, new ActionListener() {
      int lastday = -1;
      public void actionPerformed(ActionEvent e) {
          importDocs();
          
          Calendar c = Calendar.getInstance();
          c.setTime(new Timestamp(System.currentTimeMillis()));
          int hour = c.get(c.HOUR_OF_DAY);
          if (hour < 2 || hour > 4) return;
          if (c.get(c.DAY_OF_YEAR) == lastday) return;
          lastday = c.get(c.DAY_OF_YEAR);
          updateAllStanje();
      }
    });
    t.start();
  }
}
