package hr.restart.robno;

import java.math.BigDecimal;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import com.borland.dx.sql.dataset.QueryDataSet;

public class repFISBIHRN extends repFISBIH {
  
  public QueryDataSet ds;
  raDateUtil rdu = raDateUtil.getraDateUtil();
  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  lookupData ld = lookupData.getlookupData();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  QueryDataSet partneri = dM.getDataModule().getPartneri();
  boolean parFound = false;
  
  public repFISBIHRN(String tag) {
    super(tag);
  }
  public repFISBIHRN() {
    super("RN");
    setPrint("racun.INP");
    setResponse("racun");
  }
  protected void handleResponse(String sLRN) {
    if (repQC != null && repQC.caller != null && repQC.caller.getMasterSet() != null) {
      repQC.caller.getMasterSet().setInt("FBR", Integer.parseInt(sLRN));
      repQC.caller.getMasterSet().saveChanges();
    } else {
      System.out.println("repQC = "+repQC);
      if (repQC != null) {
        System.out.println("repQC.caller = "+repQC.caller);
        if (repQC.caller != null) {
          System.out.println("repQC.caller.getMasterSet() = "+repQC.caller.getMasterSet());
        }
      }
    }
  }
  public void fill() throws Exception {
    partneri.open();
    ds = repQC.getQueryDataSet();
    ds.open();
    filelines.emptyAllRows();
    parFound = ld.raLocate(partneri, "CPAR", ds.getInt("CPAR")+"");
    //footer
    String row = "Q,"+getLogickiBroj()+",______,_,__;1;Br.RN-fakture "+getBrojRac(ds);
    addLine(row);
    //depozit
    row = get_I_line();
    addLine(row);
    //pocetak
    row = "K,"+getLogickiBroj()+",______,_,__;"+getFBR()+";;;"+getIBK(ds)+";"+getNazPar(ds)+";;"+getAdrPar(ds)+";"+getGradPar(ds)+";";
    addLine(row);
    //stavka
    row = "S,"+getLogickiBroj()+",______,_,__;Stavke po fakturi-racunu;"+getIznos(ds)+";"+getKolicina(ds)+";1;1;"+getPoreskaGrupa(ds)+";0;10001;0;;;";
    addLine(row);
    //kraj racuna @todo razraditi vise nacina placanja
    row = "T,"+getLogickiBroj()+",______,_,__;"+getNacinPlacanja(ds);
    addLine(row);
    row = get_G_line();
    addLine(row);
    parFound = false;
  }
  public String get_G_line() {
    return "G,"+getLogickiBroj()+",______,_,__;LastReceiptNumber";
  }
  
  public String get_I_line() {
    return "";
  }
  public String getFBR() {
    return "";
  }
  public String getIznos(QueryDataSet ds2) {
    BigDecimal iznos = Aus.zero2;
    for (ds2.first(); ds2.inBounds(); ds2.next()) {
      iznos = iznos.add(ds2.getBigDecimal("IPRODSP"));
    }
    return iznos+"";
  }
  public void addLine(String row) {
    if (row == null || "".equals(row.trim())) return;
    filelines.insertRow(false);
    filelines.setString("LINE", row);    
    filelines.post();
  }
  
  private String getGradPar(QueryDataSet ds2) {
    if (parFound) return partneri.getInt("PBR")+" "+getString(partneri,"MJ",14);
    return "";
  }
  private String getAdrPar(QueryDataSet ds2) {
    if (parFound) return getString(partneri,"ADR",21); 
    return "";
  }
  private String getNazPar(QueryDataSet ds2) {
    if (parFound) return getString(partneri,"NAZPAR",21); 
    return "";
  }
  private String getIBK(QueryDataSet ds2) {
    char[] oib = partneri.getString("OIB").toCharArray();
    StringBuffer ret = new StringBuffer();
    for (int i = 0; i < oib.length; i++) {
      if (Character.isDigit(oib[i])) {
        ret.append(oib[i]);
      }
    }
    return ret.toString().trim();
  }
  public String getBrojRac(QueryDataSet ds2) {
    return ds2.getString("VRDOK")+"-"+ds2.getString("CSKL")+"/"+ds2.getString("GOD")+"-"+ds2.getInt("BRDOK");
  }
  public String getNacinPlacanja(QueryDataSet ds2) {
    String np = ds2.getString("CNACPL");
    String def = 
        np.equals("V")?"3"
        :(np.equals("K")?"1"
            :(np.equals("È")?"2"
                :"0"));
    return frmParam.getParam("robno","FISBIHnp"+np,def,"Oznaka naèina plaæanja "+np+" u Fiskalnoj blagajni BIH");
  }

  public String getMjera(QueryDataSet ds2) {
    return "0";
  }

  public String getRabat(QueryDataSet ds2) {
    return ds2.getBigDecimal("UPRAB1")+"";
  }

  public String getKodArtikla(QueryDataSet ds2) {
    String cn = frmParam.getParam("robno","FISBIHkodArt","CART","Koje polje se koristi za kod artikla kod FISBIH [CART|CART1|BC]");
    String kart = "N/A";
    if (cn.equalsIgnoreCase("CART")) {
      kart = ds2.getInt(cn)+"";
    } else {
      kart = ds2.getString(cn);
    }
    return kart;
  }

  public String getPoreskaGrupa(QueryDataSet ds2) {
    dM.getDataModule().getArtikli().open();
    lookupData.getlookupData().raLocate(dM.getDataModule().getArtikli(),
        new String[]{"CART"},
        new String[]{String.valueOf(ds2.getInt("CART"))});
    String cpor = dM.getDataModule().getArtikli().getString("CPOR");
    String pg = frmParam.getParam("robno","FISBIHporgrup"+cpor,"","Porezna grupa za stopu poreza "+cpor+" za fiskalni raèun");
    int ic = 1;
    try {
      ic = Integer.parseInt(pg);
    } catch (Exception e) {
      // TODO: handle exception
    }
    return ic+"";
  }

  public String getGrupaArtikla(QueryDataSet ds2) {
    return "1";
  }

  public String getOdjeljenje(QueryDataSet ds2) {
    String oj = frmParam.getParam("robno","FISBIHOdjel","1","Odjeljenje za fiskalni raèun",true);
    int ioj = 0;
    try {
      ioj = Integer.parseInt(oj);
    } catch (Exception e) {
      ioj = Integer.parseInt(ds2.getString("CORG"));
    } finally {
      ioj = 0;
    }
    return ioj+"";
  }

  public String getKolicina(QueryDataSet ds2) {
    return "1.000";//ds2.getBigDecimal("KOL")+"";
  }

  public String getCijena(QueryDataSet ds2) {
    return ds2.getBigDecimal("FC")+"";
  }

  public String getString(QueryDataSet ds2, String cn, int ln) {
    String ret = ds2.getString(cn).trim();
    ret = normalize(ret);
    if (ret.length()>ln) return ret.substring(0,ln);
    else return ret.trim();
  }

  private static String normalize(String ret) {
    VarStr v = new VarStr(ret);
    v.removeChars(",;");
    String[] t = v.split();
    return VarStr.join(t,' ').toString();
  }

  public String getLogickiBroj() {
    return frmParam.getParam("robno","FISBIHLogBroj","1","Logièki broj 'broj snimljen u tabeli 2, polje 1 u ERK-u'",true);
  }
}
