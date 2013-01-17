package hr.restart.robno;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmParam;
import hr.restart.util.VarStr;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;

public class repFISBIH extends mxReport {
  int sirina;
  String reptype;
  public QueryDataSet filelines;
  
  public repFISBIH(String _reptype) {
    this(_reptype, 250);
  }
  public repFISBIH(String _reptype, int sirina) {
    this.sirina = sirina;
    reptype = _reptype;
    init();
  }
  public void init() {
    filelines = new QueryDataSet();
    filelines.addColumn(new Column("LINE","Redak",Variant.STRING));
    filelines.open();
    setDataSet(filelines);
//    String[] detail = new String[] {"<#LINE|"+sirina+"|left#>"};
    String[] detail = new String[] {"<#LINE#>"};
    setDetail(detail);
    try {
      setPrinter(mxPrinter.getDefaultMxPrinter());
      getPrinter().setNewline(System.getProperty("line.separator"));
//      fill();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
  public void makeReport() {
    try {
      fill();
    } catch (Exception e) {
      e.printStackTrace();
    }
    super.makeReport();
    
  }
  public void print() {
    super.print();
    try {
      Thread.sleep(Integer.parseInt(frmParam.getParam("robno", "FISBIHwait", "3000", "Koliko milisekundi da èeka prije obrade odgovora FISBIH ispisa")));
    } catch (Exception e) {
      e.printStackTrace();
    }
    handleResponse();
  }
  public void handleResponse() {
    TextFile resp = TextFile.read(responseFile);
    String theJLine = null;
    if (resp != null) {
      while (true) {
        String fline = resp.in();
        System.out.println("Processing response line "+fline);
        if (fline == null) break;
        if (fline.toUpperCase().startsWith("J,")) {
          theJLine = fline;
        }
      }
    }
    if (theJLine == null) {
      if (responseFile == null) {
        System.out.println("Response file is null");
      }
      System.out.println("J line not found in responseFile");
      return;
    }
    String sLRN = theJLine.substring(theJLine.lastIndexOf(';')+1);
    handleResponse(sLRN);
  }
  /**
   * Overrride to set lastReceiptNumber eg. ds.setInt("FBR", Integer.parseInt(sLRN)); ds.saveChanges();
   * @param sLRN
   */
  protected void handleResponse(String sLRN) {
    
  }
  /**
   * fill filelines
   * @throws Exception
   */
  public void fill() throws Exception {  }
  
  public void setPrint(String filename) {
    mxRM diskrm = mxRM.getDefaultMxRM();
    String prncmd = frmParam.getParam("robno","FISBIHcmd"+reptype,"","komanda za kopiranje file-a za FISBIH",true);
    if ("".equals(prncmd)) {
      prncmd = frmParam.getParam("robno","FISBIHcmdALL","cmd /c copy # ","komanda za kopiranje file-a na medij za FISBIH",true);
    }
    diskrm.setPrintCommand(prncmd+filename);
    setRM(diskrm);
  }
  public void setPrinter(hr.restart.util.reports.mxPrinter newPrinter) {
    newPrinter.setReset(""); // uvijek salje reset printera, a ovo nije printer nego file
    super.setPrinter(newPrinter);
  }
  public static boolean isFISBIH() {
    return frmParam.getParam("robno","FISBIH","N","Koristi li se fiskalizacija za BiH? (D/N)").equalsIgnoreCase("D");
  }
  /**
   * 
   * @param fn file name bez ekstenzije
   */
  public void setResponse(String fn) {
     String respPattern = frmParam.getParam("robno","FISBIHresp","c:\\temp\\#.IN$","Gdje se nalazi file s odgovorom za FISBIH",true);
     responseFile = new File(new VarStr(respPattern).replaceAll("#", fn).toString());
  }
  private File responseFile = null;
  public File getResponse() {
    if (responseFile == null) setPrint(reptype);
    return responseFile;
  }
  public static repFISBIH izvjestajX() {
    return new repFISBIH("X") {
      public void init() {
        super.init();
        setPrint("izvjX.INP");
      }
      public void fill() throws Exception {
        filelines.emptyAllRows();
        filelines.insertRow(false);
        filelines.setString("LINE", "X,1,______,_,__;");
        filelines.post();
      }
    };
  }
  
  public static repFISBIH izvjestajZ() {
    return new repFISBIH("Z") {
      public void init() {
        super.init();
        setPrint("izvjZ.INP");
      }
      public void fill() throws Exception {
        filelines.emptyAllRows();
        filelines.insertRow(false);
        filelines.setString("LINE", "Z,1,______,_,__;");
        filelines.post();
      }
    };
  }
  public static repFISBIH izvjestajR(final Timestamp _od, final Timestamp _do) {
    return new repFISBIH("R") {
      public void init() {
        super.init();
        setPrint("izvjR.INP");
      }
      public void fill() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
        String f_od = df.format(_od);
        String f_do = df.format(_do);
        filelines.emptyAllRows();
        filelines.insertRow(false);
        filelines.setString("LINE", "R,1,______,_,__;6;"+f_od+";"+f_do+";");
        filelines.post();
      }
    };
  }
  
}
