package hr.restart.pos;

import hr.restart.robno.Aut;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
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
      "<#"+dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+" "+dm.getLogotipovi().getString("MJESTO") +"|"+width+"|center#><$newline$>"+ getPhones();
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
    
    this.setPgFooter(
        "<$newline$><$newline$><$newline$>"+
        "<$newline$><$newline$><$newline$>"+
        "\u001B\u0064\u0000");

    super.makeReport();
  }
  
  String getZag() {
    String z = "<$newline$><#Obrazac R-1 |"+width+"|right><$newline$>";
    
    return z;
  }
  
  String getPlac() {
    return "";
  }
  
  String getPop() {
    return "";
  }
  
  String getPor() {
    return "";
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
