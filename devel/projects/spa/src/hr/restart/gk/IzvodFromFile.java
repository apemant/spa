/*
 * Created on Apr 20, 2005
 */
package hr.restart.gk;

import hr.restart.baza.Virmani;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.util.Valid;
import hr.restart.util.textconv.FileParser;
import hr.restart.util.textconv.ILine;
import hr.restart.util.textconv.ParserManager;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.frmVirmani;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.TreeMap;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * parsira datoteku izvoda dobivenu sa e-bankarstva, ubacuje u tablicu virmani
 * i prikazuje u frmTableDataView 
 */
public class IzvodFromFile {
  QueryDataSet showSet;
  frmVirmani fvirmani;
  public IzvodFromFile() {
    String context = frmParam.getParam("zapod","izvod_ctx_xml","izvod.xml","Konfiguracijski XML za prihvat izvoda iz datoteke");
    String classID = frmParam.getParam("zapod","izvod_fp_id","FP","ID klase fileparsera u izvod_context_xml"); 
    FileParser parser = ParserManager.getFileParser(context,classID);
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(System.getProperty("user.dir"));
    if (chooser.showOpenDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION) {
	    File file = chooser.getSelectedFile();
	    parser.setFile(file);
	    TreeMap parsed = ParserManager.getParsedLines(parser,file);
	    
	    //konverzija u virmane
	    convertToVirmani(parsed);
	    frmTableDataView view = new frmTableDataView(true,false);
	    view.setDataSet(fvirmani.getRaQueryDataSet());
	    view.setVisibleCols(new int[] {2,4,5,6,7,10,16,17,19});
	    view.show();
	    //fvirmani.show();
	    
    } else {
      //nisi odabrao pa nemogu dalje bla bla bla
    }
  }
  /**
   * @param parsed
   */
  private void convertToVirmani(TreeMap parsed) {
    fvirmani = new frmVirmani("zapod", true);
    fvirmani.ckey = generateKey(parsed);
    for (Iterator iter = parsed.keySet().iterator(); iter.hasNext();) {
      Object key = iter.next();
      System.out.println("key = "+key);
      System.out.println("value = "+parsed.get(key));
      ILine line = (ILine)parsed.get(key);
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
    fvirmani.setRaQueryDataSet(Virmani.getDataModule()
        .getFilteredDataSet("app='zapod' and ckey='"+fvirmani.ckey+"'"));
    fvirmani.getRaQueryDataSet().open();
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
    return OrgStr.getKNJCORG()+"-"+
    hr.restart.sisfun.raUser.getInstance().getUser()+"-"+
    Valid.getValid().getToday();
  }
  
}
