package hr.restart.sisfun;

import hr.restart.util.Aus;
import hr.restart.util.IntParam;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;


public class EbayUtils {
  
  private static final String GET_ITEM = "/buy/browse/v1/item/";
  private static final String GET_ITEM_GROUP = "/buy/browse/v1/item/get_items_by_item_group";
  private static final String GET_PRODUCT = "/commerce/catalog/v1_beta/product/";
  
  private static final String REQUEST_TOKENS = "/identity/v1/oauth2/token";
  
  private static final EbayUtils inst = new EbayUtils();

  private ApiContext api;
  
  private String token;
  private long expiry;
  
  public static EbayUtils getInstance() {
    return inst;
  }
  
  public EbayUtils() {
    api = new ApiContext();
    api.setApiServerUrl(getSOAPHost());
  }

  public String getToken() {
    if (token == null || token.length() == 0 || System.currentTimeMillis() > expiry)
      requestAccessToken(getRefreshToken());
      
    return token;
  }
  
  public String getHost() {
    return frmParam.getParam("sisfun", "ebay.host", "api.ebay.com", "Host za pristup eBay API");
  }
  
  public String getSOAPHost() {
    return frmParam.getParam("sisfun", "ebay.soap.host", "https://api.ebay.com/wsapi", "Host za pristup eBay SOAP API");
  }
    
  public String getClientId() {
    return frmParam.getParam("sisfun", "ebay.client.id", "RestArt-Maguro-PRD-02ccbdebc-e10c7473", "Client ID za pristup eBay API");
  }
  
  public String getClientSecret() {
    return frmParam.getParam("sisfun", "ebay.client.key", "PRD-2ccbdebca2ed-9717-44fb-ab14-8649", "Client Key za pristup eBay API");
  }
  
  public String getRuName() {
    return frmParam.getParam("sisfun", "ebay.ruName", "Rest_Art-RestArt-Maguro--laqwl", "Redirect URI name za pristup eBay API");
  }
  
  public String getRefreshToken() {
    return frmParam.getParam("sisfun", "ebay.refresh.token", "", "Refresh token za pristup eBay API");
  }

  HttpClient getClient() {
    return HttpClientBuilder.create().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
  }
  
  HttpGet getRequest(String path, boolean auth) throws URISyntaxException {
    HttpGet req = new HttpGet(new URIBuilder().setScheme("https").setHost(getHost()).setPath(path).build());
    if (auth) req.addHeader("Authorization", "Bearer " + getToken()); 
    return req;
  }
    
  String getResponse(HttpClient client, HttpUriRequest req) throws ClientProtocolException, IOException {
    HttpResponse resp = client.execute(req);
    
    if (resp.getEntity() == null) return null;
    
    String str = EntityUtils.toString(resp.getEntity(), "utf-8");
    if (str == null || str.length() == 0) return null;
    
    return str;
  }
  
  public void saveImage(File out, String uri) {
    HttpClient client = getClient();
    try {
      HttpGet get = new HttpGet(uri);
      HttpResponse resp = client.execute(get);
      
      FileUtils.writeByteArrayToFile(out, EntityUtils.toByteArray(resp.getEntity()));
      System.out.println("Saved: " + uri + " => " + out);
      return;
    } catch (EbayException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new EbayException("Greška kod uèitavanja slike!");
  }
  
  public void requestTokens(String code) {
    HttpClient client = getClient();
    try {
      HttpPost auth = new HttpPost(new URIBuilder().setScheme("https").setHost(getHost()).setPath(REQUEST_TOKENS).build());
      auth.addHeader("Content-Type", "application/x-www-form-urlencoded");
      auth.addHeader("Authorization", "Basic " + Base64.encodeBase64String((getClientId() + ":" + getClientSecret()).getBytes("utf-8")));
      auth.setEntity(new StringEntity("grant_type=authorization_code&code=" + code + "&redirect_uri="+getRuName()));
      
      String ret = getResponse(client, auth);
      JSONObject o = JSONObject.fromObject(ret);
      if (o == null || o.get("errors") != null)
        throw new EbayException("Greška kod formiranja refresh tokena!");
      
      getRefreshToken();
      Object rt = o.get("refresh_token");
      if (rt == null || !(rt instanceof String))
        throw new EbayException("Greška kod osvježavanja access tokena!");
      
      String refreshToken = (String) rt;
      if (refreshToken.length() == 0)
        throw new EbayException("Greška kod osvježavanja access tokena!");
      frmParam.setParam("sisfun", "ebay.refresh.token", refreshToken);
      System.out.println("Refresh token: " + refreshToken);
      
      setupAccessToken(o);
      return;
    } catch (EbayException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    throw new EbayException("Greška kod formiranja refresh tokena!");
    
  }
  
  private String scope = 
      "scope=https://api.ebay.com/oauth/api_scope " + 
  		//"https://api.ebay.com/oauth/api_scope/sell.marketing.readonly " +
  		//"https://api.ebay.com/oauth/api_scope/sell.marketing " +
  		"https://api.ebay.com/oauth/api_scope/sell.inventory.readonly " +
  		"https://api.ebay.com/oauth/api_scope/sell.inventory ";
  		//"https://api.ebay.com/oauth/api_scope/sell.account.readonly " +
  		//"https://api.ebay.com/oauth/api_scope/sell.account " +
  		//"https://api.ebay.com/oauth/api_scope/sell.fulfillment.readonly " +
  		//"https://api.ebay.com/oauth/api_scope/sell.fulfillment " +
  		//"https://api.ebay.com/oauth/api_scope/sell.analytics.readonly " +
  		//"https://api.ebay.com/oauth/api_scope/commerce.catalog.readonly";
  
  public void requestAccessToken(String refreshToken) {
    HttpClient client = getClient();
    try {
      HttpPost auth = new HttpPost(new URIBuilder().setScheme("https").setHost(getHost()).setPath(REQUEST_TOKENS).build());
      auth.addHeader("Content-Type", "application/x-www-form-urlencoded");
      auth.addHeader("Authorization", "Basic " + Base64.encodeBase64String((getClientId() + ":" + getClientSecret()).getBytes("utf-8")));
      auth.setEntity(new StringEntity("grant_type=refresh_token&refresh_token=" + refreshToken + "&" + URLEncoder.encode(scope, "utf-8")));
      
      String ret = getResponse(client, auth);
      
      JSONObject o = JSONObject.fromObject(ret);
      if (o == null || o.get("errors") != null)
        throw new EbayException("Greška kod osvježavanja access tokena!");
      
      setupAccessToken(o);
      return;
    } catch (EbayException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    throw new EbayException("Greška kod osvježavanja access tokena!");
  }
  
  private void setupAccessToken(JSONObject o) {
    Object tok = o.get("access_token");
    if (tok == null || !(tok instanceof String) || (token = (String) tok).length() == 0)
      throw new EbayException("Greška kod osvježavanja access tokena!");
    
    Object exp = o.get("expires_in");
    if (exp == null)
      throw new EbayException("Greška kod osvježavanja access tokena!");
    
    expiry = System.currentTimeMillis() + 1000L * 
        (exp instanceof Number ? ((Number) exp).intValue() : Aus.getNumber(exp.toString())) - 30000L;
    
    System.out.println("token: " + token);
    System.out.println("expiration: " + expiry);
  }
  
  public void getGroupArt(String id) {
    HttpClient client = getClient();
    try {
      HttpGet req = getRequest(GET_ITEM_GROUP + "?item_group_id="+ id , true);
      
      String ret = getResponse(client, req);
      if (ret == null) return;
      
      System.out.println(JSONObject.fromObject(ret));
      return;
    } catch (EbayException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    throw new EbayException("Greška kod dohvata skupnog artikla!");
  }
  
  public void getArt(String id) {
    HttpClient client = getClient();
    try {
      HttpGet req = getRequest(GET_ITEM + "v1|" + id + "|0", true);
      
      String ret = getResponse(client, req);
      if (ret == null) return;
      
      System.out.println(JSONObject.fromObject(ret));
      return;
    } catch (EbayException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    throw new EbayException("Greška kod dohvata artikla!");
  }
  
  public void getProduct(String id) {
    HttpClient client = getClient();
    try {
      HttpGet req = getRequest(GET_PRODUCT + id, true);
      
      String ret = getResponse(client, req);
      if (ret == null) return;
      
      System.out.println(JSONObject.fromObject(ret));
      return;
    } catch (EbayException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    throw new EbayException("Greška kod dohvata artikla!");
  }
  
  public void getItemId(String id) {
    HttpClient client = getClient();
    try {
      HttpGet req = getRequest(GET_ITEM + id, true);
      
      String ret = getResponse(client, req);
      if (ret == null) return;
      
      System.out.println(JSONObject.fromObject(ret));
      return;
    } catch (EbayException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    throw new EbayException("Greška kod dohvata artikla!");
  }
  
  public ItemType getTradingItem(String id) {
    ApiCredential creds = api.getApiCredential();
    creds.seteBayToken(IntParam.getTag("ebay.token"));
    GetItemCall gi = new GetItemCall(api);
    gi.setIncludeItemSpecifics(Boolean.TRUE);
    gi.setIncludeItemCompatibilityList(Boolean.TRUE);
    gi.addDetailLevel(DetailLevelCodeType.RETURN_ALL);
    try {
      return gi.getItem(id);
    } catch (EbayException e) {
      throw e;
    } catch (ApiException e) {
      e.printStackTrace();
    } catch (SdkException e) {
      e.printStackTrace();
      e.getInnerThrowable().printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new EbayException("Greška kod dohvata artikla!");
  }
}
