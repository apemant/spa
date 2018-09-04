package hr.restart.util;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Stanje;
import hr.restart.robno.Aut;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;

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


public class raComms {
  
  private static final String UPLOAD_PRODUCTS = "/api/uploadproductsfile";
  private static final String DOWNLOAD_ORDERS = "/api/getordersfromid/";
  
  private String apikey = "Zq9hAgjwlEZdeJ8hxaJ5jX5BgA9MiSFumiyHnMb6vSWqNjQ4ZBU0htzEJFpT";
  private String host = "www.hipokras.extremeit.hr";

  public raComms() {
    
  }
  
  public void downloadOrders() {
    
    int from = 0;
    
    HttpClient client = getClient();
    
    try {
      HttpGet req = getRequestGet(DOWNLOAD_ORDERS + from);
      
      HttpResponse resp = client.execute(req);
      
      JSONArray arr = JSONArray.fromObject(IOUtils.toString(resp.getEntity().getContent(), "utf-8"));
      
      for (Iterator i = arr.iterator(); i.hasNext(); ) {
        JSONObject o = (JSONObject) i.next();

      }

      /*
      JSONArray ords = new JSONArray();
      IOUtils.toString(resp.getEntity().getContent(), resp.getEntity().getContentType()*/
      
      
      
            
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
  
}
