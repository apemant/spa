package hr.restart.robno;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.dM;
import hr.restart.pos.frmMasterBlagajna;
import hr.restart.pos.presBlag;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.reports.raReportData;
import hr.restart.zapod.OrgStr;


public class repPosJas implements raReportData {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  
  DataSet master;
  DataSet ds;
  
  BigDecimal pov;
  
  String oib, ph, kh, god, boper, hvala, title, kuplab, kupac, specForm;
  
  public repPosJas() {
    frmMasterBlagajna fmb = frmMasterBlagajna.getInstance();
    master = fmb == null ? frmMasterBlagajna.getAlterMaster() : fmb.getMasterSet();
    god =master.getString("GOD");
    ds = hr.restart.baza.Stpos.getDataModule().getTempSet("cskl='"+master.getString("CSKL")+"' and vrdok = 'GRC' and god =  '"+master.getString("GOD")+"' and brdok =  "+master.getInt("BRDOK")); 
    ds.open();
    
    dm.getLogotipovi().open();
    lD.raLocate(dm.getLogotipovi(), "CORG", OrgStr.getKNJCORG(false));
    
    kh = dm.getLogotipovi().getString("NAZIVLOG")+"\n"+
         dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+" "+dm.getLogotipovi().getString("MJESTO") +"\n"+
         "OIB "+dm.getLogotipovi().getString("OIB")+"\n" + getPhones();
    

    QueryDataSet sks = hr.restart.baza.Sklad.getDataModule().getTempSet("cskl = '"+master.getString("CSKL")+"'");
    QueryDataSet prm = hr.restart.baza.Prod_mj.getDataModule().getTempSet("cprodmj = '"+master.getString("CPRODMJ")+"'");
    sks.open();
    prm.open();
    
    ph = kh;
    if (!sks.getString("CORG").equals(OrgStr.getKNJCORG(false)) &&
        lD.raLocate(dm.getLogotipovi(), "CORG", sks.getString("CORG"))) {
      ph = dm.getLogotipovi().getString("NAZIVLOG")+"\n"+
           dm.getLogotipovi().getString("ADRESA")+ ", " +String.valueOf(dm.getLogotipovi().getInt("PBR"))+" "+dm.getLogotipovi().getString("MJESTO") +"\n" + getPhones();
    }
    
    String th = frmParam.getParam("pos", "posHeader", "",
    "POS header (1 - poslovnica, knjigovodstvo  2 - obrnuto, ostalo - samo knjigovodstvo)");
    if (th.equals("2") && !kh.equals(ph)) {
      String sw = kh;
      kh = ph;
      ph = sw;
    } else if (!th.equals("1") || kh.equals(ph))
      ph = "";
    
    String post = frmParam.getParam("pos", "addHeaderAfter", "", 
        "Dodatni header iza zaglavlja", true);
    if (post.trim().length() > 0) kh = kh + "\n" + post;
    
    pov = Aus.getDecNumber(frmParam.getParam("robno", "iznosPov", "0.5",
      "Iznos povratne naknade"));
    
    hvala = frmParam.getParam("pos", "hvalaText", "HVALA NA POVJERENJU",
        "Tekst na dnu POS raèuna");
    
    oib = frmParam.getParam("robno", "oibMode", "MB", 
          "Staviti matièni broj (MB) ili OIB?");
    
    specForm = frmParam.getParam("pos", "formatBroj", "",
    "Format broja raèuna na POS-u");
    
    getNacinPlacanja();
    calculatePorez(getRekapitulacija(ds));
    
    String prodMjesto = prm.getString("NAZPRODMJ");
    String user = master.getString("CUSER");
    boper = getBlagajnaOperater(prodMjesto, user);
    kuplab = kupac = "";
    title = "RAÈUN br. " + master.getInt("BRDOK");
    
    if(master.getInt("CKUPAC") != 0){
        DataRow dr = lD.raLookup(hr.restart.baza.dM.getDataModule().getKupci(),"CKUPAC", master.getInt("CKUPAC")+"");
        if (dr != null) {
          title = "RAÈUN R-1 br. " + master.getInt("BRDOK");
          kuplab = "Kupac:";
          kupac =  dr.getString("IME")+" "+dr.getString("PREZIME")+"\n"+
              ((!dr.getString("ADR").equals(""))?dr.getString("ADR")+"\n":"")+
              ((dr.getInt("PBR")!=0)?dr.getInt("PBR")+" ":"")+
              ((!dr.getString("MJ").equals(""))?((dr.getInt("PBR")==0)? dr.getString("MJ"):dr.getString("MJ")):"")+
              getJMBG(dr);
        } System.out.println("Kupac je (ako ga ima) null!!!");
      }    
  }
  
  private String getBRDOK() {
    if (presBlag.isFiskal(master) && master.getString("FOK").equals("D")) {
      return master.getInt("FBR") + "-" + master.getString("FPP") + "-" + master.getInt("FNU");
    }
    if (specForm == null || specForm.length() == 0)
      return Integer.toString(master.getInt("BRDOK"));

    return Aus.formatBroj(master, specForm);
  }

  private String getJMBG(DataRow dr) {
    String result = "";
    if (!oib.equalsIgnoreCase("MB")) {
      String br = dr.getString("OIB");
      if (br.length() == 0) result = "";
      else result = "\nOIB: " + br;
    } 
    if (oib.equalsIgnoreCase("MB") || result.length() == 0) {
      String mb = dr.getString("JMBG");
      if (mb.length() == 0) result = "";
      else result = "\nMB: " + mb; 
    }   
    return result;
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }
  
  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds = null;
  }
  
  public String getHeadFirst() {
    return ph;
  }
  
  public String getHeadSecond() {
    return kh;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getKUPLAB() {
    return kuplab;
  }
  
  public String getKUPAC() {
    return kupac;
  }
  
  public String getNAZIV() {
    return ds.getString("NAZART");
  }
  
  public int getKOL() {
    return ds.getBigDecimal("KOL").intValue();
  }
  
  public BigDecimal getCIJENA() {
    return ds.getBigDecimal("MC");
  }
  
  public BigDecimal getIZNOS() {
    return ds.getBigDecimal("IZNOS");
  }
  
  public BigDecimal getTOTAL() {
    return master.getBigDecimal("NETO");
  }
  
  public BigDecimal getUIPOP() {
    return master.getBigDecimal("UIPOPUST2").signum() == 0 ? null : master.getBigDecimal("UIPOPUST2");
  }
  
  public String getPOP() {
    return master.getBigDecimal("UPPOPUST2").signum() == 0 ? "" :
      "POPUST " + master.getBigDecimal("UPPOPUST2").doubleValue() + "%";
  }
  
  public BigDecimal getUKUPNO() {
    return master.getBigDecimal("UIPOPUST2").signum() == 0 ? null : master.getBigDecimal("UKUPNO");
  }
  
  public String getUKUPLAB() {
    return master.getBigDecimal("UPPOPUST2").signum() == 0 ? "" :
      "UKUPNO";
  }
  
  public String getPLACNAME() {
    return placnaziv;
  }
  
  public String getPLACIZNOS() {
    return placiznos;
  }
  
  public String getPORNAME() {
    return porname;
  }
  
  public String getPORSTOPA() {
    return porstopa;
  }
  
  public String getPOROSN() {
    return porosn;
  }
  
  public String getPORIZNOS() {
    return poriznos;
  }
  
  public String getPOSLUGA() {
    return boper;
  }
  
  public String getHVALA() {
    return hvala;
  }
  
  public String getNADNEVAK() {
    return "Nadnevak: " + Aus.formatTimestamp(master.getTimestamp("DATDOK"));
  }
  
  public String getVRIJEME() {
    return "Vrijeme: " + master.getTimestamp("DATDOK").toString().substring(11,19);
  }
  
  private String getPhones(){
    String phoneString = "Tel. ";
    if (!dm.getLogotipovi().getString("TEL1").equals(""))
    phoneString += dm.getLogotipovi().getString("TEL1");
    if (!dm.getLogotipovi().getString("TEL2").equals(""))
      if (dm.getLogotipovi().getString("TEL1").equals(""))
        phoneString += dm.getLogotipovi().getString("TEL2");
      else
        phoneString += ", "+dm.getLogotipovi().getString("TEL2");
   return phoneString; 
  }
  
  private String getBlagajnaOperater(String blag, String user){
    String blop = hr.restart.sisfun.frmParam.getParam("pos","BlOp","0","Ispis i pozicija blagajne i operatora na malim raèunima (0,1,2,3)");
    if (!blop.equalsIgnoreCase("0")){
      DataRow usr = lD.raLookup(hr.restart.baza.dM.getDataModule().getUseri(),"CUSER", user);
      String operater = usr.getString("NAZIV");
      if (blop.equalsIgnoreCase("1")){
        return "BLAGAJNA: "+blag+"\n"+
               "OPERATER: "+operater;
      } else if (blop.equalsIgnoreCase("2")) {
        return blag+", "+operater;
      } else if (blop.equalsIgnoreCase("3")) {
        //return "Poslužio: "+operater+"<$newline$>"+
        //"Broj stola: " + getStol() + "<$newline$>";
        return "Stol: " + getStol() + "   Poslužio: " + operater;
      } else if (blop.equalsIgnoreCase("4")) {
        return "OPERATER: "+operater+"<$newline$>";
      }
    }
    return "";
  }
  
  protected String getStol() {
    return frmMasterBlagajna.getStol();
  }
  
  String placnaziv, placiznos;
  private void getNacinPlacanja(){
    placnaziv = placiznos = "";
    QueryDataSet npos = ut.getNewQueryDataSet("SELECT nacpl.naznacpl as naznacpl, sum(rate.irata) as irata FROM rate,nacpl "+
                                            "WHERE rate.cnacpl = nacpl.cnacpl "+
                                            "and rate.brdok = " + master.getInt("BRDOK") + " and rate.vrdok = 'GRC' and rate.god='"+ god + //Aut.getAut().getKnjigodRobno() +
                                            "' and rate.cskl= '" + master.getString("CSKL") + "' group by naznacpl");
  
    npos.first();
    if (npos.rowCount() == 0) return ;
  
  //TODO obrati paznju na sgQuerys.getSgQuerys().format(BD,int) ;)

    do {
      if (placnaziv.length() > 0) {
        placnaziv += "\n";
        placiznos += "\n";
      }
      placnaziv += npos.getString("NAZNACPL").toUpperCase();
      placiznos += Aus.formatBigDecimal(npos.getBigDecimal("IRATA"));
    } while (npos.next());
  }

  String porname, porstopa, porosn, poriznos;
  
  private void calculatePorez(DataSet dset){
    porname = porstopa = porosn = poriznos = "";
    if (dset == null || dset.rowCount() < 1 ) return;
    dset.first();

    do {
      if (porname.length() > 0) {
        porname += "\n";
        porstopa += "\n";
        porosn += "\n";
        poriznos += "\n";
      }
      porname += dset.getString("NAZPOR");
      porstopa += Aus.formatBigDecimal(dset.getBigDecimal("UKUPOR")) + "%";
      porosn += Aus.formatBigDecimal(dset.getBigDecimal("NETO").subtract(dset.getBigDecimal("POV").add(dset.getBigDecimal("POR1")).add(dset.getBigDecimal("POR2").add(dset.getBigDecimal("POR3")))));
      poriznos += Aus.formatBigDecimal(dset.getBigDecimal("POREZ"));
    } while (dset.next());
  }
  
  
  BigDecimal izpov = Aus.zero2;
  
  private DataSet getRekapitulacija(DataSet ds) {
    dm.getArtikli().open();
    dm.getPorezi().open();
    QueryDataSet qds = new QueryDataSet();
    Column bjesansam1 = (Column) dm.getPorezi().getColumn("NAZPOR").clone();
    bjesansam1.setColumnName("CPOR");
    Column bjesansam2 = (Column) dm.getPorezi().getColumn("POR1").clone();
    bjesansam2.setColumnName("POREZ");

    qds.setColumns(new Column [] {
        (Column) dm.getStdoki().getColumn("CSKL").clone(),
        (Column) dm.getStdoki().getColumn("VRDOK").clone(),
        (Column) dm.getStdoki().getColumn("GOD").clone(),
        (Column) dm.getStdoki().getColumn("BRDOK").clone(),
        bjesansam1 ,
        (Column) dm.getPorezi().getColumn("NAZPOR").clone(),
        (Column) dm.getPorezi().getColumn("UKUPOR").clone(),
        (Column) dm.getStpos().getColumn("NETO").clone(),
        bjesansam2 ,
        (Column) dm.getStdoki().getColumn("POR1").clone(),
        (Column) dm.getStdoki().getColumn("POR2").clone(),
        (Column) dm.getStdoki().getColumn("POR3").clone(),
        dM.createBigDecimalColumn("POV"),
        new com.borland.dx.dataset.Column("KEY","KEY",com.borland.dx.dataset.Variant.STRING)});

    qds.open();
    ds.open();
    ds.first();
    izpov = Aus.zero2;
    do {
      lD.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[]{String.valueOf(ds.getInt("CART"))});
      lD.raLocate(dm.getPorezi(), new String[]{"CPOR"}, new String[]{dm.getArtikli().getString("CPOR")});
      
//      System.out.println("netto - " + ds.getBigDecimal("NETO")); //XDEBUG delete when no more needed
//      
//      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//      st.prn(ds);
      if ("D".equals(dm.getArtikli().getString("POV")))
        izpov = izpov.add(pov.multiply(ds.getBigDecimal("KOL")).
                  setScale(2, BigDecimal.ROUND_HALF_UP));

      for (int i = 1 ; i<4;i++) {
        if(!dm.getPorezi().getString("NAZPOR"+String.valueOf(i)).equals("")){
          String key = ds.getString("CSKL").concat("-").concat(
              ds.getString("GOD")).concat("-").concat(
              ds.getString("VRDOK")).concat("-").concat(
              String.valueOf(ds.getInt("BRDOK"))).concat("-").concat(
              dm.getPorezi().getString("NAZPOR"+String.valueOf(i)));

          if(!lD.raLocate(qds, new String[]{"KEY"}, new String[]{key})){
            qds.insertRow(true);
            qds.setString("CSKL", ds.getString("CSKL"));
            qds.setString("VRDOK", ds.getString("VRDOK"));
            qds.setString("GOD", ds.getString("GOD"));
            qds.setInt("BRDOK", ds.getInt("BRDOK"));
            qds.setString("CPOR", dm.getPorezi().getString("NAZPOR"+String.valueOf(i)));
            qds.setString("NAZPOR", dm.getPorezi().getString("NAZPOR"+String.valueOf(i)));

            qds.setBigDecimal("UKUPOR", dm.getPorezi().getBigDecimal("POR"+String.valueOf(i)));
            qds.setBigDecimal("NETO", ds.getBigDecimal("NETO"));
            qds.setBigDecimal("POREZ", ds.getBigDecimal("POR"+String.valueOf(i)));
            qds.setBigDecimal("POR1", ds.getBigDecimal("POR1"));
            qds.setBigDecimal("POR2", ds.getBigDecimal("POR2"));
            qds.setBigDecimal("POR3", ds.getBigDecimal("POR3"));
            if ("D".equals(dm.getArtikli().getString("POV")))
              qds.setBigDecimal("POV", pov.multiply(ds.getBigDecimal("KOL")).
                  setScale(2, BigDecimal.ROUND_HALF_UP));

            qds.setString("KEY", key);
          } else {
            qds.setBigDecimal("NETO", qds.getBigDecimal("NETO").
                              add(ds.getBigDecimal("NETO")));
            qds.setBigDecimal("POREZ", qds.getBigDecimal("POREZ").add(ds.getBigDecimal("POR"+String.valueOf(i))));
            qds.setBigDecimal("POR1", qds.getBigDecimal("POR1").add(ds.getBigDecimal("POR1")));
            qds.setBigDecimal("POR2", qds.getBigDecimal("POR2").add(ds.getBigDecimal("POR2")));
            qds.setBigDecimal("POR3", qds.getBigDecimal("POR3").add(ds.getBigDecimal("POR3")));
            if ("D".equals(dm.getArtikli().getString("POV")))
              Aus.add(qds, "POV", pov.multiply(ds.getBigDecimal("KOL")).
                  setScale(2, BigDecimal.ROUND_HALF_UP));
          }
        }
      }
    } while (ds.next());
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(qds);
    return qds;
  }
  
}

