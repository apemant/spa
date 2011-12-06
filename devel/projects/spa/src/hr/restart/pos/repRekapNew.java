package hr.restart.pos;

import hr.restart.robno.Aut;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class repRekapNew extends mxReport {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  hr.restart.pos.ispRekapNew irn = hr.restart.pos.ispRekapNew.getInstance();
  String[] detail = new String[1];
  //hr.restart.robno.sgQuerys sgq = hr.restart.robno.sgQuerys.getSgQuerys();

  int width = 40;
  String doubleLineSep, pcorg;
  
  
  public repRekapNew() {
    // 
  }
  
  public void makeReport(){
    String wdt = hr.restart.sisfun.frmParam.getParam("pos", "sirPOSpr", "41", "Sirina pos ispisa. Preporuka 39 - 46",true);
    width = Integer.parseInt(wdt);
    pcorg = frmParam.getParam("pos", "posCorg", "",
    "OJ za logotip na POS-u");
    if (pcorg == null || pcorg.length() == 0)
      pcorg = OrgStr.getKNJCORG(false);
    
    doubleLineSep = getDoubleLineLength();
    

      this.setDataSet(new StorageDataSet());
      this.getDataSet().open();
      
      String vc = hr.restart.sisfun.frmParam.getParam(
          "sisfun", "printerRMcmnd", "1", "Radno mjesto", true);
      lD.raLocate(dm.getMxPrinterRM(), "CRM", vc);
      mxRM rm = new mxRM();
      rm.init(dm.getMxPrinterRM());
      setRM(rm);

      //    System.out.println("WIDTH - "+ width);
    dm.getLogotipovi().open();
    
    lD.raLocate(dm.getLogotipovi(), "CORG", pcorg);
    
    String kh = "<#"+dm.getLogotipovi().getString("NAZIVLOG")+"|"+width+"|center#><$newline$>"+
    "<#"+dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+" "+dm.getLogotipovi().getString("MJESTO") +"|"+width+"|center#><$newline$>"+
    "<#OIB "+dm.getLogotipovi().getString("OIB")+"|"+width+"|center#><$newline$>"+ getPhones();
    

    QueryDataSet sks = hr.restart.baza.Sklad.getDataModule().getTempSet("cskl = '"+irn.getCSKL()+"'");
    sks.open();
    
    String ph = kh;
    if (!sks.getString("CORG").equals(OrgStr.getKNJCORG(false)) &&
        lD.raLocate(dm.getLogotipovi(), "CORG", sks.getString("CORG"))) {
      ph = "<#"+dm.getLogotipovi().getString("NAZIVLOG")+"|"+width+"|center#><$newline$>"+
      "<#"+dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+" "+dm.getLogotipovi().getString("MJESTO") +"|"+width+"|center#><$newline$>"+ 
      "<#OIB "+dm.getLogotipovi().getString("OIB")+"|"+width+"|center#><$newline$>"+ getPhones();
    }

    String prep = frmParam.getParam("pos", "addHeader", "",
        "Dodatni header ispred POS raèuna", true);
    
    if (prep.length() > 0) {
      String[] parts = new VarStr(prep).split('|');
      VarStr buf = new VarStr();
      for (int i = 0; i < parts.length; i++)
        buf.append("<#").append(parts[i]).append('|').
          append(width).append("|center#><$newline$>");
      prep = buf.toString();
    }
    
    String th = frmParam.getParam("pos", "posHeader", "",
        "POS header (1 - poslovnica, knjigovodstvo  2 - obrnuto, ostalo - samo knjigovodstvo)");
    String header = prep + kh;
    if (th.equals("1") && !kh.equals(ph))
      header = ph + kh;
    if (th.equals("2") && !kh.equals(ph))
      header = kh + ph;
    
    this.setPgHeader(header+getZag()+getPlac()+getPop()+getPor());
    
    this.setRepFooter(
        "<$newline$><$newline$><$newline$>"+
        "<$newline$><$newline$><$newline$>"+getLastEscapeString());

    super.makeReport();
  }
  
  String getZag() {
    String z = "<$newline$><#Obrazac R-1 |"+width+"|right#><$newline$><$newline$>"+
    			"<#OBRAÈUN|"+width+"|center#><$newline$>"+
    			"<#" + ("od " + Aus.formatTimestamp(irn.getPocDatum()) + " do " + Aus.formatTimestamp(irn.getZavDatum())) + 
    			"|"+width+"|center#><$newline$><$newline$>";
    return z;
  }
  
  String getPlac() {
  	String z = doubleLineSep + "<$newline$><#Naèin plaæanja|20|left#> <#IZNOS|"+(width-21)+"|right#><$newline$>"+ 
  						doubleLineSep + "<$newline$>";
  	
  	DataSet pl = irn.getFinalSet();
  	BigDecimal tot = Aus.zero2;
  	
  	for (pl.first(); pl.inBounds(); pl.next()) {
  		if (pl.getBigDecimal("IZNOS").signum() == 0)
  			z += "<#"+pl.getString("NACPL")+"|25|left#><$newline$>";
  		else z +="<#"+pl.getString("NACPL")+"|25|left#> <#"+Aus.formatBigDecimal(pl.getBigDecimal("IZNOS"))+"|"+(width-26)+"|right#><$newline$>";
  		tot = tot.add(pl.getBigDecimal("IZNOS"));
  	}
  	z += "<$newline$><#UKUPNO|20|left#> <#"+Aus.formatBigDecimal(tot)+"|"+(width-21)+"|right#><$newline$>";
  	
    return z;
  }
  
  String getPop() {
    return "<$newline$><$newline$>ODOBRENI POPUST: <#"+Aus.formatBigDecimal(irn.getPopust())+"|"+(width-17)+"|right#><$newline$><$newline$>";
  }
  
  String getPor() {
  	
    
  	String z =  "<#P R E G L E D  P O R E Z A|"+width+"|center#><$newline$>"+
                 "<#NAZIV|6|left#> <#STOPA|8|right#> <#OSNOVICA|12|right#> <#POREZ|"+(width-29)+"|right#><$newline$>"+
                  doubleLineSep+"<$newline$>";
  	
  	DataSet ds = irn.getPorezSet();
  	for (ds.first(); ds.inBounds(); ds.next()) {    
      z += "<#"+ds.getString("NAZIV")+"|6|left#> <#"+Aus.formatBigDecimal(ds.getBigDecimal("STOPA"))+"%|8|right#> <#"+
      	Aus.formatBigDecimal(ds.getBigDecimal("OSNOVICA"))+"|12|right#> <#"+
      	Aus.formatBigDecimal(ds.getBigDecimal("IZNOS"))+"|"+(width-29)+"|right#>"+ "<$newline$>";
    }
    return z;
  }
  
  private String getLastEscapeString() {
    try {
      int crm = dm.getMxPrinterRM().getInt("CRM");//jebiga
      String str = frmParam.getParam("sisfun", "endPOSRM"+crm, "\\u001B\\u0064\\u0000", "Sekvenca koja dolazi na kraju ispisa POS racuna za rm "+crm);
//String str = "\\u0041\\u004e\\u0044\\u0052\\u0045\\u004a";
      StringTokenizer tok = new StringTokenizer(str,"\\u");
      char[] ret = new char[tok.countTokens()];
      int i=0;
      while (tok.hasMoreTokens()) {
        ret[i] = (char)Integer.parseInt(tok.nextToken(), 16);
        i++;
      }
      return new String(ret);
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "";
    }
  }
  
  private String getPhones(){
    String phoneString = "<#Tel. ";
    if (!dm.getLogotipovi().getString("TEL1").equals(""))
    phoneString += dm.getLogotipovi().getString("TEL1");
    if (!dm.getLogotipovi().getString("TEL2").equals(""))
      if (dm.getLogotipovi().getString("TEL1").equals(""))
        phoneString += dm.getLogotipovi().getString("TEL2");
      else
        phoneString += ", "+dm.getLogotipovi().getString("TEL2");
   return phoneString+"|"+width+"|center#><$newline$>"; 
  }
  
  private String getDoubleLineLength(){
    String dl = "";
    for (int i=1; i <= width; i++){
      dl += "-";
    }
    return dl;
   }

}
