/*
 * Created on Apr 20, 2005
 */
package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.baza.Gkstavkerad;
import hr.restart.baza.PNBKonto;
import hr.restart.baza.Virmani;
import hr.restart.baza.Ziropar;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.textconv.FileParser;
import hr.restart.util.textconv.ILine;
import hr.restart.util.textconv.ParserManager;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgGetKnjig;
import hr.restart.zapod.frmVirmani;
import hr.restart.zapod.raKonta;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.TreeMap;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * parsira datoteku izvoda dobivenu sa e-bankarstva, ubacuje u tablicu virmani
 * i prikazuje u frmTableDataView 
 */
public class IzvodFromFile {
  private char cmode;
  StorageDataSet showSet;
  frmVirmani fvirmani;
  private TreeMap parsed;
  static String kontoKup = frmParam.getParam("gk", "defKontoKup", "1200", "Defaultni konto kupca pri auto izvodima");
  static String kontoDob = frmParam.getParam("gk", "defKontoDob", "2200", "Defaultni konto dobavljaca pri auto izvodima");
  public IzvodFromFile(char conversionMode) {
    cmode = conversionMode;
    String context = frmParam.getParam("zapod","izvod_ctx_xml","izvod.xml","Konfiguracijski XML za prihvat izvoda iz datoteke");
    String classID = frmParam.getParam("zapod","izvod_fp_id","FP","ID klase fileparsera u izvod_context_xml"); 
    FileParser parser = ParserManager.getFileParser(context,classID);
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(System.getProperty("user.dir"));
    if (chooser.showOpenDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION) {
	    File file = chooser.getSelectedFile();
	    parser.setFile(file);
	    parsed = ParserManager.getParsedLines(parser,file);
	    if (conversionMode == 'V') {
  	    //konverzija u virmane
  	    convertToVirmani();
	    } else if (conversionMode == 'I') {//konverzija u gkstavkerad
	      convertToGkStavke();
	    }
    } else {
      //nisi odabrao pa nemogu dalje bla bla bla
    }
  }
  
  public void showInSet() {
    frmTableDataView view = new frmTableDataView() {//true,false) {
      protected void OKPress() {
        hide();
        commitTransfer();
      }
    };
    view.jp.getNavBar().addOption(new raNavAction("Izmjena polja", raImages.IMGCHANGE, KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        //ekran za dodavanje ziropar i pnbkonto
        new PNBKontoZiroParDlg(getShowSet());          
        
      }
    });
    view.setDataSet(getShowSet());
//    view.setVisibleCols(new int[] {2,4,5,6,7,10,16,17,19});
    view.show();
  }
  protected void commitTransfer() {
    
  }

  public StorageDataSet getShowSet() {
    return showSet;
  }

  public void setShowSet(StorageDataSet showSet) {
    this.showSet = showSet;
  }

  public QueryDataSet convertToGkStavke() {
    StorageDataSet helperset = new StorageDataSet();
    helperset.addColumn(dM.createIntColumn("RBS"));
    helperset.addColumn(dM.createStringColumn("NAZPAR","Naziv partnera", 200));
    helperset.addColumn(dM.createStringColumn("ZIRO","Žiro", 200));
    helperset.addColumn(dM.createStringColumn("PNBZ","Poziv na broj", 200));
    helperset.addColumn(dM.createIntColumn("CPAR"));
    helperset.addColumn(dM.createStringColumn("BROJKONTA","Konto",12));
    helperset.addColumn(dM.createStringColumn("SALDAK","Kup/Dob",1));
    helperset.open();
    
    QueryDataSet gksr = Gkstavkerad.getDataModule().getFilteredDataSet(Condition.nil);
    gksr.open();
    int rbs = 0;
    for (Iterator iter = parsed.keySet().iterator(); iter.hasNext();) {
      Object key = iter.next();
      System.out.println("key = "+key);
      System.out.println("value = "+parsed.get(key));
      ILine line = (ILine)parsed.get(key);
      if (line.getColumn("NAZPAR")==null) continue; //nije line 5
      rbs++;
      //10 1200-ip, 20 2200-id
      String ozntra = (String)line.getColumnValue("OZNTRA");
      boolean isKupac = ozntra.trim().equals("20");
      gksr.insertRow(false);
      helperset.insertRow(false);
      gksr.setInt("RBS", rbs);
      String vbdi = (String)line.getColumnValue("VBDIPAR");
      String zr = (String)line.getColumnValue("ZIROPAR");
      if (zr.startsWith(vbdi)) zr = zr.substring(vbdi.length());
      String ziro = vbdi.trim()+"-"+zr.trim();
      String broj = (String)line.getColumnValue("PNBO");
      String broj1 = broj.substring(0,2);
      String broj2 = broj.substring(2).trim();
      if (broj2.equals("")) broj2 = ((String)line.getColumnValue("SVRHA")).trim();
      if (broj2.equals("")) broj2 = "N/A";
      gksr.setString("BROJKONTA", getKonto(isKupac, ziro, broj2));
      gksr.setString("CORG", dlgGetKnjig.getKNJCORG());
      gksr.setTimestamp("DATDOK", (Timestamp)line.getColumnValue("DATUM"));
      gksr.setString("BROJDOK", broj2);
      if (raKonta.isSaldak(gksr.getString("BROJKONTA"))) {
        gksr.setString("VRDOK", isKupac?"UPL":"IPL");
        gksr.setInt("CPAR", getCPar(ziro));
      }
      gksr.setString("OPIS", ((String)line.getColumnValue("SVRHA")).trim());
      gksr.setBigDecimal("ID", isKupac?Aus.zero2:getIznos((Integer)line.getColumnValue("IZNOS")));
      gksr.setBigDecimal("IP", isKupac?getIznos((Integer)line.getColumnValue("IZNOS")):Aus.zero2);
      gksr.post();
      
      helperset.setInt("RBS", rbs);
      helperset.setString("NAZPAR", (String)line.getColumnValue("NAZPAR"));
      helperset.setString("ZIRO", ziro);
      helperset.setString("PNBZ", broj2);
      helperset.setInt("CPAR", gksr.getInt("CPAR"));
      helperset.setString("BROJKONTA", gksr.getString("BROJKONTA"));
      helperset.setString("SALDAK", isKupac?"K":"D");
      helperset.post();
    }
    setShowSet(helperset);
    return gksr;
  }
  public static int getCPar(String ziro) {
    //locate in ziropar po zircu
    if (lookupData.getlookupData().raLocate(getZiropar(), "ZIRO", ziro)) {
      return getZiropar().getInt("CPAR");
    }
    return -1;
  }
  static QueryDataSet pnbkonto = null;
  static QueryDataSet ziropar = null;
  private static QueryDataSet getZiropar() {
    if (ziropar == null) ziropar = Ziropar.getDataModule().copyDataSet();
    ziropar.open();
    return ziropar;
  }
  private static QueryDataSet getPNBKonto() {
    if (pnbkonto == null) pnbkonto = PNBKonto.getDataModule().copyDataSet();
    pnbkonto.open();
    return pnbkonto;
  }
  public static String getKonto(boolean kup, String ziro, String broj) {
    //locate in pnbkonto po broju
    if (lookupData.getlookupData().raLocate(getPNBKonto(), "PNB", broj)) {
      return getPNBKonto().getString("BROJKONTA");
    }
    //locate in pnbkonto po zircu
    if (lookupData.getlookupData().raLocate(getPNBKonto(), "PNB", ziro)) {
      return getPNBKonto().getString("BROJKONTA");
    }
    return kup?kontoKup:kontoDob;
  }

  /**
   */
  private void convertToVirmani() {
    fvirmani = new frmVirmani("zapod", true);
    fvirmani.ckey = generateKey(parsed);
    for (Iterator iter = parsed.keySet().iterator(); iter.hasNext();) {
      Object key = iter.next();
      System.out.println("key = "+key);
      System.out.println("value = "+parsed.get(key));
      ILine line = (ILine)parsed.get(key);
      if (line.getColumn("NAZPAR")==null) continue; //nije line 5
      fvirmani.add(
          "",//  Jedinica zavoda    
          (String)line.getColumnValue("NAZPAR"),//  Na teret racuna  
          (String)line.getColumnValue("SVRHA"),//  Svrha doznake  
          (String)line.getColumnValue("OZNTRA"),//  U korist racuna  
          (String)line.getColumnValue("ZIROPAR"),//  Broj racuna na teret  
          "",//  Nacin izvrs  
          "",//  Poziv na broj (zaduz.) 1 
          ((String)line.getColumnValue("PNBZ")).trim(),//  Poziv na broj (zaduz.) 2 
          "",//  Sifra 1  
          "",//  Sifra 2  
          "",//  Sifra 3  
          getRacunUkorist(),//  Broj racuna u korist  
          "",//  Poziv na broj (odobr.) 1 
          ((String)line.getColumnValue("PNBO")).trim(),//  Poziv na broj (odobr.) 2 
          getIznos((Integer)line.getColumnValue("IZNOS")),//  Iznos  
          (String)line.getColumnValue("MJESTOPAR"),//  Mjesto  
          (Timestamp)line.getColumnValue("DATUM"),//  Datum izvrsenja  
          getDatumPredaje()//  Datum predaje  
          );
    }
    fvirmani.save();
    setShowSet(Virmani.getDataModule().getFilteredDataSet("app='zapod' and ckey='"+fvirmani.ckey+"'"));
//    fvirmani.setRaQueryDataSet(Virmani.getDataModule()
//        .getFilteredDataSet("app='zapod' and ckey='"+fvirmani.ckey+"'"));
//    fvirmani.getRaQueryDataSet().open();
  }
  /**
   * @return
   */
  private Timestamp getDatumPredaje() {
    // TODO Auto-generated method stub
    return null;
  }
  /**
   * @param integer
   * @return
   */
  private BigDecimal getIznos(Integer integer) {
    return new BigDecimal(integer.doubleValue()).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
  }
  /**
   * @return
   */
  private String getRacunUkorist() {
    // TODO Auto-generated method stub
    return "RACUNUKORIST";
  }
  /**
   * @param parsed
   * @return
   */
  private String generateKey(TreeMap parsed) {
    // TODO Auto-generated method stub
    Timestamp t = hr.restart.util.Valid.getValid().getToday();
    return OrgStr.getKNJCORG()+"-"+
//    new java.util.Formatter().format("%tF %tT",  new Object[] {t,t}); //ne shljaka na 1.4
    System.currentTimeMillis();
  }
  
}
