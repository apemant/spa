package hr.restart.util;

import hr.restart.baza.*;
import hr.restart.help.MsgDispatcher;
import hr.restart.robno.Aut;
import hr.restart.robno.raControlDocs;
import hr.restart.sisfun.frmParam;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class raComms {
  
  private static final String UPLOAD_PRODUCTS = "/api/uploadproductsfile";
  private static final String DOWNLOAD_ORDERS = "/api/getordersfromid/";
  
  private String apikey = "Zq9hAgjwlEZdeJ8hxaJ5jX5BgA9MiSFumiyHnMb6vSWqNjQ4ZBU0htzEJFpT";
  private String host = "www.hipokras.com";
  private String corg, cskl, user, users;
  private int dost;
  private boolean auto;

  public raComms() {
    auto = frmParam.getParam("robno", "webDaemon", "N", "Automatska komunikacija s webshopom (D,N").equalsIgnoreCase("D");
    if (auto) {
      apikey = frmParam.getParam("robno", "webApiKey", apikey, "Api key za webshop");
      host = frmParam.getParam("robno", "webHost", host, "Adresa webshopa");
      corg = frmParam.getParam("robno", "webCorg", "01", "Corg za webshop");
      cskl = frmParam.getParam("robno", "webCskl", "1", "Skladište za webshop");
      user = frmParam.getParam("robno", "webUser", "restart", "Username za webshop");
      users = frmParam.getParam("robno", "webNotify", "", "Popis korisnika za notifikaciju webshop");
      dost = Aus.getAnyNumber(frmParam.getParam("robno", "webDostava", "0", "Šifra artikla dostave na webshopu"));
      raControlDocs.UniversalKeyToSqlKey("stdoki", hr.restart.robno.Util.dikey, hr.restart.robno.Util.dikey);
    }
  }
  
  public void dump(int broj, int indent) {
    try {
      HttpClient client = getClient();
      
      HttpGet req = getRequestGet(DOWNLOAD_ORDERS + broj);
      
      HttpResponse resp = client.execute(req);
      
      JSONArray arr = JSONArray.fromObject(IOUtils.toString(resp.getEntity().getContent(), "utf-8"));
      
      for (Iterator z = arr.iterator(); z.hasNext(); ) {
        JSONObject oz = (JSONObject) z.next();
        
        System.out.println(oz.toString(4, indent));
      }
      
      } catch (URISyntaxException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      //} catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        //e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }
  
  public void downloadOrders() {
    
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    HttpClient client = getClient();
    
    QueryDataSet npl = nacpl.getDataModule().openShadowSet("");
    
    StorageDataSet vals = Valute.getDataModule().openShadowSet("");
    
    QueryDataSet seq = SEQ.getDataModule().openShadowSet(Condition.equal("OPIS", "webshop-"+corg));
    if (seq.rowCount() == 0) {
      seq.insertRow(false);
      seq.setString("OPIS", "webshop-"+corg);
      seq.setInt("BROJ", 0);
    }
    int rbr = seq.getInt("BROJ");
    
    QueryDataSet narsds = doki.getDataModule().openShadowSet("BRNARIZ", Condition.equal("GOD", Valid.getValid().findYear()).
        and(Condition.equal("CSKL", corg)).and(Condition.equal("VRDOK", "GRN")));
    HashSet nars = new HashSet();
    for (narsds.first(); narsds.inBounds(); narsds.next()) nars.add(narsds.getString("BRNARIZ"));
    
    QueryDataSet zag = doki.getDataModule().openShadowSet();
    QueryDataSet st = stdoki.getDataModule().openShadowSet();
    
    QueryDataSet rate = Rate.getDataModule().openEmptySet();
    
    QueryDataSet arts = Artikli.getDataModule().openShadowSet("");
    
    String seqs = corg + "GRN" + Valid.getValid().findYear();
    QueryDataSet dseq = SEQ.getDataModule().openShadowSet(Condition.equal("OPIS", seqs));
    
    QueryDataSet kups = Kupci.getDataModule().openShadowSet("");
    
    Timestamp first = null;
    Timestamp last = null;
    int errors = 0;
    boolean dirty = false;
    VarStr errs = new VarStr("Neuspješno prebaèene narudžbe: ");
    int downs = 0;
    
    try {
      HttpGet req = getRequestGet(DOWNLOAD_ORDERS + rbr);
      
      HttpResponse resp = client.execute(req);
      
      JSONArray arr = JSONArray.fromObject(IOUtils.toString(resp.getEntity().getContent(), "utf-8"));
      boolean nonpay = false;
      
      for (Iterator z = arr.iterator(); z.hasNext(); ) {
        JSONObject oz = (JSONObject) z.next();
        
        String broj = oz.getInt("id") + "";
        if (nars.contains(broj)) continue;
        
        if (!oz.getString("order_status_id").equals("3")) {
          if (!oz.getString("order_status_id").equals("4"))
            nonpay = true;
          continue;
        }
        
        if (!nonpay) seq.setInt("BROJ", oz.getInt("id") + 1);
        
        dseq.refresh();
        int brdok = dseq.getInt("BROJ") + 1;
        dseq.setInt("BROJ", brdok);
        
        Timestamp dat = Valid.getValid().getToday();
        try {
          dat = new Timestamp(df.parse(oz.getString("created_at")).getTime());
        } catch (ParseException e) {
          e.printStackTrace();
        }
        
        String ime = oz.getString("invoice_name");
        String prez = oz.getString("invoice_surname");
        String oib = oz.getString("invoice_oib");
        String emadr = oz.getString("invoice_email");
        String adr = oz.getString("invoice_email");
        String mj = oz.getString("invoice_city");
        String pbr = oz.getString("invoice_zip");
        String tel = oz.getString("invoice_phone");
        
        boolean found = false;
        if (oib.trim().length() > 0) {
          found = lookupData.getlookupData().raLocate(kups, "OIB", oib);
        }
        if (!found && ime.trim().length() > 0 && prez.trim().length() > 0 &&
              adr.trim().length() > 0 && mj.trim().length() > 0) {
          found = lookupData.getlookupData().raLocate(kups, new String[] {"IME", "PREZIME", "ADR", "MJ"}, 
                      new String[] {ime, prez, adr, mj});
        }
        
        if (!found) {
          kups.insertRow(false);
          kups.setInt("CKUPAC", Kupci.getDataModule().getShadowMax("CKUPAC") + 1);
          kups.setString("IME", ime);
          kups.setString("PREZIME", prez);
          kups.setString("OIB", oib);
          kups.setString("EMADR", emadr);
          kups.setString("ADR", adr);
          kups.setString("MJ", mj);
          kups.setInt("PBR", Aus.getAnyNumber(pbr));
          kups.setString("TEL", tel);
          kups.post();
          dirty = true;
        }

        
        zag.insertRow(false);
        zag.setString("CSKL", corg);
        zag.setString("VRDOK", "GRN");
        zag.setString("GOD", Valid.getValid().findYear());
        zag.setInt("BRDOK", brdok);
        zag.setTimestamp("SYSDAT", Valid.getValid().getToday());
        zag.setString("CUSER", user);
        zag.setTimestamp("DATDOK", dat);
        zag.setTimestamp("DATDOSP", dat);
        zag.setTimestamp("DVO", dat);
        zag.setString("BRNARIZ", broj);
        zag.setInt("CKUPAC", kups.getInt("CKUPAC"));
        
        BigDecimal div = Aus.one0;
        if (lookupData.getlookupData().raLocate(vals, "BKEY", oz.getString("user_currency_id"))) {
          div = Aus.getDecNumber(oz.getString("conversion_rate"));
          zag.setString("OZNVAL", vals.getString("OZNVAL"));
          zag.setBigDecimal("TECAJ", Aus.one0.divide(div, 7, BigDecimal.ROUND_HALF_UP));
        }
        
        if (lookupData.getlookupData().raLocate(npl, "BKEY", oz.getString("payment_method_id"))) {
          zag.setString("CNACPL", npl.getString("CNACPL"));
        }
        
        zag.setBigDecimal("UIRAC", Aus.getDecNumber(oz.getString("total_with_tax")));
        
        int rbs = 0;
        JSONArray items = oz.getJSONArray("items");
        for (Iterator i = items.iterator(); i.hasNext(); ) {
          JSONObject oi = (JSONObject) i.next();
          
          if (lookupData.getlookupData().raLocate(arts, "CART1", oi.getString("item_reference"))) {
            st.insertRow(false);
            dM.copyColumns(zag, st, hr.restart.robno.Util.mkey);
            st.setInt("RBSID", ++rbs);
            st.setShort("RBR", (short) rbs);
            Aut.getAut().copyArtFields(st, arts);
            st.setBigDecimal("KOL", Aus.getDecNumber(oi.getString("item_quantity")));
            st.setBigDecimal("FC", Aus.getDecNumber(oi.getString("item_price")));
            st.setBigDecimal("FVC", st.getBigDecimal("FC"));
            st.setBigDecimal("FMC", Aus.getDecNumber(oi.getString("item_price_with_tax")));
            st.setBigDecimal("FMCPRP", Aus.getDecNumber(oi.getString("item_price_with_tax")));
            st.setBigDecimal("INETO", Aus.getDecNumber(oi.getString("total_price")));
            st.setBigDecimal("IPRODBP", st.getBigDecimal("INETO"));
            st.setBigDecimal("POR1", Aus.getDecNumber(oi.getString("total_price_tax")));
            st.setBigDecimal("IPRODSP", Aus.getDecNumber(oi.getString("total_price_with_tax")));
            st.setBigDecimal("UIPOR", st.getBigDecimal("POR1"));
            st.setBigDecimal("PPOR1", Aus.getDecNumber(oi.getString("tax_rate")));
            st.setBigDecimal("UPPOR", st.getBigDecimal("PPOR1"));
            st.setString("CSKLART", cskl);
            st.setString("ID_STAVKA", raControlDocs.getKey(st, hr.restart.robno.Util.dikey, "stdoki"));
          }
        }
        
        BigDecimal ship = Aus.getDecNumber(oz.getString("total_shipping"));
        if (ship.signum() > 0 && dost > 0 && lookupData.getlookupData().raLocate(arts, "CART", dost + ""))  {
          //ship = ship.multiply(div).setScale(2, BigDecimal.ROUND_HALF_UP);
          st.insertRow(false);
          dM.copyColumns(zag, st, hr.restart.robno.Util.mkey);
          st.setInt("RBSID", ++rbs);
          st.setShort("RBR", (short) rbs);
          Aut.getAut().copyArtFields(st, arts);
          st.setBigDecimal("KOL", Aus.one0);
          st.setBigDecimal("FC", ship);
          st.setBigDecimal("FVC", ship);
          st.setBigDecimal("FMC", Aus.getDecNumber(oz.getString("total_shipping_with_tax")));
          st.setBigDecimal("FMCPRP", Aus.getDecNumber(oz.getString("total_shipping_with_tax")));
          st.setBigDecimal("INETO", ship);
          st.setBigDecimal("IPRODBP", ship);
          st.setBigDecimal("POR1", Aus.getDecNumber(oz.getString("total_shipping_tax")));
          st.setBigDecimal("IPRODSP", st.getBigDecimal("FMC"));
          st.setBigDecimal("UIPOR", st.getBigDecimal("POR1"));
          st.setBigDecimal("PPOR1", st.getBigDecimal("UIPOR").movePointRight(2).divide(ship, 1, BigDecimal.ROUND_HALF_UP));
          st.setBigDecimal("UPPOR", st.getBigDecimal("PPOR1"));
          st.setString("ID_STAVKA", raControlDocs.getKey(st, hr.restart.robno.Util.dikey, "stdoki"));
        }
                
        if (raTransaction.saveChangesInTransaction(new QueryDataSet[] {zag, st, seq, dseq, kups})) {
          if (first == null) first = dat;
          last = dat;
          ++downs;
        } else {
          ++errors;
          errs.append(broj).append(", ");
          System.out.println("FAILED!");
        }
      }
      
      if (dirty) {
        new DBDataSetSynchronizer().markAsDirty("kupci");
      }
      
      if (errors > 0 || first != null) {
        System.out.println("Users: " + users);
        VarStr buf = new VarStr();
        buf.append("<html><p>WEBSHOP:</p>");
        buf.append("<p>Dohvaæeno " + downs + " narudžbi ");
        buf.append("(").append(Aus.createLink("Pregled", 
              "hr.restart.robno.Util.getUtil().showDocs(\""+corg+
              "\", \"GRN\", java.sql.Timestamp.valueOf(\"" + first + "\"), " + 
              "java.sql.Timestamp.valueOf(\"" + last + "\"))"));
        buf.append(") s webshop servera.</p>");
        if (errors > 0) {
          errs.chop(2).append('.');
          buf.append("<p>").append(errs).append("</p>");
        }
        String msg = buf.toString();
        
        String[] us = new VarStr(users).split();
        for (int i = 0; i < us.length; i++) {
          System.out.println("Sending to " + us[i]);
          MsgDispatcher.sendOut("sistem", us[i], msg);
        }
        
        //sendArts();
      }
      
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    //} catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      //e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  public void sendArts() {
    
    
    StorageDataSet art = Artikli.getDataModule().getScopedSet("CART CART1 NAZART JM CPOR MC NAZLANG");
    Util.getUtil().fillAsyncData(art, "SELECT cart, cart1, nazart, jm, cpor, mc, nazlang FROM artikli");
    
    StorageDataSet sta = Stanje.getDataModule().getScopedSet("CART KOL");
    Util.getUtil().fillAsyncData(sta, "SELECT cart, kol FROM stanje WHERE " + Condition.equal("GOD", Aut.getAut().getKnjigodRobno()));
    
    lookupData ld = lookupData.getlookupData();
    
    JSONArray arr = new JSONArray();
    for (art.first(); art.inBounds(); art.next()) {
      JSONObject o = new JSONObject();
      o.put("id", Integer.valueOf(art.getInt("CART")));
      o.put("name", art.getString("NAZART"));
      o.put("name_en", art.getString("NAZLANG"));
      o.put("unity", art.getString("JM"));
      o.put("unit", art.getString("JM"));
      o.put("price", art.getBigDecimal("MC"));
      o.put("tax_id", art.getString("CPOR"));
      o.put("reference", art.getString("CART1"));
      if (ld.raLocate(sta, "CART", art))
        o.put("quantity", sta.getBigDecimal("KOL"));
      else o.put("quantity", Aus.zero0);
      o.put("description", "");
      
      arr.add(o);
    }

    HttpClient client = getClient();
    try {
      HttpPost req = getRequest(UPLOAD_PRODUCTS);
      
      HttpEntity entity = MultipartEntityBuilder.create().addTextBody("api_token", apikey)
        .addBinaryBody("products_file", arr.toString().getBytes("UTF-8"), ContentType.create("application/octet-stream"), "artikli.dat").build();      
      req.setEntity(entity);
      
      System.out.println(req);
      System.out.println(Arrays.toString(req.getAllHeaders()));
      System.out.println(entity);
      System.out.println(arr);
      
      HttpResponse resp = client.execute(req);
      
      System.out.println(resp.toString());
      System.out.println(resp.getEntity().toString());
      System.out.println(IOUtils.toString(resp.getEntity().getContent()));
            
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    //} catch (ClientProtocolException e) {
      //e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  HttpClient getClient() {
    return HttpClientBuilder.create().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
  }
  
  HttpGet getRequestGet(String path) throws URISyntaxException {
    HttpGet req = new HttpGet(new URIBuilder().setScheme("https").setHost(host).setPath(path).setParameter("api_token", apikey).build()); 
    req.addHeader("charset", "utf-8");
    
    return req;
  }

  HttpPost getRequest(String path) throws URISyntaxException {
    HttpPost req = new HttpPost(new URIBuilder().setScheme("https").setHost(host).setPath(path).build()); 
    req.addHeader("charset", "utf-8");
    
    return req;
  }
  
  public void install(int delay) {
    if (!auto) return;
    Timer t = new Timer("webshop", true);
    t.schedule(new TimerTask() {
      public void run() {
        System.out.println("Starting download...");
        downloadOrders();
      }
    }, delay * 1000, delay * 1000);
    System.out.println("Pokrenuto osvježavanje svakih " + delay + " sekundi.");
  }
}
