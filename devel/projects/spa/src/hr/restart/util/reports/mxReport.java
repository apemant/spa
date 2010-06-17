/****license*****************************************************************
**   file: mxReport.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.util.reports;

import bsh.Interpreter;
import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.Util;

/**
 * Utility za kreiranje matricnih reporta
 * <pre>
 * npr.
 * public class tempReport extends mxReport {
 *  String[] detail = new String[1];
 *  public tempReport() {
 *   setDataSet(hr.restart.baza.dM.getDataModule().getStdoku()); // mxRep zna ispisati dataSet i dataObject koji implementira
 *                                                               // sg.com.elixir.reportwriter.datasource.IDataProvider
 *                                                               // ( setDataObject(IDataProvider) )
 *   setPgHeader("<#ARTIKL,20,right#> KOL   <#CIJENA,20,right#><$newline$>"+
 *               "------------------------------------------------"
 *   ); //stringovi 'ARTIKL','IZNOS' nemaju getter u repIzdatnici pa ce se ispisati uppercase u zadanom formatu
 *   detail[0] = "<#CART,20,right#> <#KOL,4,right#> <#VC,20,right#>"; //repIzdanica ima metode getNAZART(), getKOL(), getIRAZ() koje ce report pozvati kada za to bude vrijeme
 *   setDetail(detail);
 *   setRepFooter("------------------------------------------------<$newline$>"+
 *                "<#UKUPNO:,25,right#> <%sum(VC,20,right)%>"
 *   );
 *  }
 * }
 * .
 * .
 * .
 * u raMatPodaci klasi kaze se:
 * getRepRunner().addReport("testframe.tempReport","Temp Matri\u010Dni ispis");
 *
 * I pritiscima na prave gumbe u pravo vrijeme dobije se ovo
 *              ARTIKL KOL                 CIJENA
 *------------------------------------------------
 *                   1 10.0                10.00
 *                   2 10.0                20.00
 *                   3 10.0                30.00
 *                   4 10.0                40.00
 *                   5 10.0                50.00
 *                   6 10.0                60.00
 *                   1 10.0                10.00
 *                   2 10.0                20.00
 *                   3 10.0                30.00
 *                   4 10.0                40.00
 *                   5 10.0                50.00
 *                   6 10.0                60.00
 *------------------------------------------------
 *                  UKUPNO:              1680.00
 *
 * </pre>
 */

public class mxReport {
//sysoutTEST ST = new sysoutTEST(false);

  public mxReport() {
  }

  public mxReport(String repTit) {
    setTitle(repTit);
  }

  public static String TMPPRINTFILE = "ispis.txt";
//  private java.io.FileWriter file;i
  protected hr.restart.util.FileHandler file = new hr.restart.util.FileHandler(TMPPRINTFILE);
  private boolean fileEmpty = true;
  private String repHeader="";
  private String repFooter="";
  private String pgHeader="";
  private String pgFooter="";
  private hr.restart.util.reports.mxPrinter printer;
  private hr.restart.util.reports.mxDocument document;
  private hr.restart.util.reports.mxRM RM;
  private String oldRepHeader="";
  private String oldRepFooter="";
  private String oldPgHeader="";
  private String oldPgFooter="";
  private String oldHeader[]={""};
  private com.borland.dx.dataset.DataSet dataSet = null;
  private Object dataObject = null;
  private String[] header = {""};
  private String[] footer = {""};
  private String[] detail = {""};
  private String title = "Ispis";
  private int repLine = 0;
  private int repPage = 0;
  private java.util.Vector vecTags = new java.util.Vector();

  public void setTitle(String newTitle) {
    title = newTitle;
  }

  public String getTitle() {
    return title;
  }

  public String getRepHeader() {
    return repHeader;
  }
  public void setRepHeader(String newRepHeader) {
    repHeader = newRepHeader;
  }
  public void setRepFooter(String newRepFooter) {
    repFooter = newRepFooter;
  }
  public String getRepFooter() {
    return repFooter;
  }
  public void setPgHeader(String newPgHeader) {
    pgHeader = newPgHeader;
  }
  public String getPgHeader() {
    return pgHeader;
  }
  public void setPgFooter(String newPgFooter) {
    pgFooter = newPgFooter;
  }
  public String getPgFooter() {
    return pgFooter;
  }
  public void setHeader(String[] newHeader) {
    header = newHeader;
    oldHeader = (String[])newHeader.clone();
    for (int i = 0;i<oldHeader.length;i++) oldHeader[i] = "";
  }
  public String[] getHeader() {
    return header;
  }

  public void setFooter(String[] newFooter) {
    footer = newFooter;
  }
  public String[] getFooter() {
    return footer;
  }

  public void setDetail(String[] newDetail) {
    detail = newDetail;
  }
  public String[] getDetail() {
    return detail;
  }
  public void setPrinter(hr.restart.util.reports.mxPrinter newPrinter) {
    printer = newPrinter;
  }
  public hr.restart.util.reports.mxPrinter getPrinter() {
    return printer;
  }
  public void setDocument(hr.restart.util.reports.mxDocument newDocument) {
    document = newDocument;
  }
  public hr.restart.util.reports.mxDocument getDocument() {
    return document;
  }
  public void setRM(hr.restart.util.reports.mxRM newRM) {
    RM = newRM;
  }
  public hr.restart.util.reports.mxRM getRM() {
    return RM;
  }
  public void setDataSet(com.borland.dx.dataset.DataSet newDataSet) {
    dataSet = newDataSet;
  }
  public com.borland.dx.dataset.DataSet getDataSet() {
    return dataSet;
  }
  public void setDataObject(Object newDataObject) {
    dataObject = newDataObject;
  }
  public Object getDataObject() {
    return dataObject;
  }
/**
 * Provjerava i inicijalizira parametre reporta
 */
  public void start() {
    if (chkParams()) {
      if (openFile()) {
        file.write(getPrinter().getReset());
        repLine = 0;
        repPage = 0;
      };
    }
  }
  private boolean chkParams() {
    if (repHeader.concat(repFooter).concat(pgHeader).concat(pgFooter).concat(header[0]).concat(detail[0]).equals("")) {
//      throw new java.lang.Exception("mxReport nije ispravno inicijaliziran");
//      System.out.println("mxReport nije ispravno inicijaliziran");
      return false;
    }
    if (printer == null) setPrinter(mxPrinter.getDefaultMxPrinter());
    if (document == null) setDocument(mxDocument.getDefaultMxDocument());
    if (RM == null) setRM(mxRM.getDefaultMxRM());
    return true;
  }
  protected boolean openFile() {
    try {
/*      java.io.File delfile = new java.io.File(TMPPRINTFILE);
//      file = new java.io.RandomAccessFile(delfile,"rw");
      delfile.delete();

      file = new java.io.FileWriter(delfile);*/
      file.openWrite();
      fileEmpty = true;
      return true;
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }
/**
 * Dodaje novi slog u report
 */
  public void add() {
    writeRepHeader(); // samo na prvoj strani reporta...OK
    oldPgHeader = writeBreak(oldPgHeader,pgHeader);
    for (int i=0;i<header.length;i++) {
      writeFooter(i);
      oldHeader[i] = writeBreak(oldHeader[i],header[i]);
      if (i < detail.length) writeBreak("",detail[i]);
    }
  }
/**
 * Zavrsava izvjestaj
 */
  public void finish() {
    for (int i=0;i<footer.length;i++) {
      writeBreak("",footer[i]);
    }
    writeBreak("",pgFooter);
    writeBreak("",repFooter);
    initOldValues();
    try {
      file.close();
    } catch (Exception e) {
      System.out.println("Unable to close file "+e);
    }
  }

  void initOldValues() {
    oldRepHeader="";
    oldRepFooter="";
    oldPgHeader="";
    oldPgFooter="";
    for (int i=0;i<oldHeader.length;i++) oldHeader[i]="";
  }

  private void writeHMargin(int iteration) {
    for (int i=0;i<iteration;i++) {
      writeToFile(printer.getNewline());
    }
    repLine = repLine + iteration;
  }

  private String getLeftMarginString() {
    int lmargin = document.getLeftMargin();
    if (lmargin == -1) lmargin = printer.getLeftMargin();
    if (lmargin == -1) return "";
    char[] lmarC = new char[lmargin];
    for (int i=0;i<lmarC.length;i++) lmarC[i]=' ';
    String lmarS = new String(lmarC);
    return lmarS;
  }

  private void writeRepHeader() {
    try {
      if (fileEmpty) {
        repLine = 0;
        repPage = 1;
        writeHMargin(printer.getTopMargin());
        writeBreak("",repHeader);
        fileEmpty = false;
      }
    } catch (Exception e) {
      System.out.println("mxPrinter.writeRepHeader: "+e);
    }
  }

  private void writeFooter(int i) {
    if (oldHeader[i].equals("")) return;
    if (i >= footer.length) return;
    if (footer[i].equals("")) return;
    if (!oldHeader[i].equals(makeLine(header[i]))) {
      writeBreak("",footer[i]);
      oldHeader[i] = "";
    }
  }

  private int getBlockHeight(String block) {
    int blockHeight = countOccurance(block,"<$newline$>");
    return blockHeight;
  }

  public int countOccurance(String searchStr,String pattern) {
    String sstr = new String(searchStr);
    int count = 0;
    while (sstr.length()>=0) {
      int bidx = sstr.indexOf(pattern);
      if (bidx == -1) return count;
      count++;
      sstr = sstr.substring(bidx+pattern.length());
    }
    return count;
  }

  private void chkNextPage(String block) {
    if ((repLine + printer.getBotMargin() + getBlockHeight(block) + getBlockHeight(pgFooter) >= printer.getPageSize())&&(printer.getPageSize()>0)) {
      nextPage();
    }
  }

  private void nextPage() {
    writeBreak("",pgFooter);
    writeHMargin(printer.getPageSize() - repLine);
    repPage = repPage + 1;
    repLine = 0;
    writeHMargin(printer.getTopMargin());
    writeBreak("",pgHeader);
  }

  private String writeBreak(String oldValue,String newValue) {
    String bodyVal = makeLine(newValue);
//    System.out.println("BodyVal : " + bodyVal);
    if (bodyVal.equals("")) return bodyVal;

    if (oldValue.equals("")) { // ako je oldValue = "" bezuvjetno ispisuje break
      oldValue = bodyVal;
    } else {
      if (oldValue.equals(bodyVal)) { //ako je isti nista ne ispisujes
        return bodyVal;
      }
      if (newValue.equals(pgHeader)) {
        nextPage();
        return bodyVal;
      }
    }

    if (!newValue.equals(pgFooter)) chkNextPage(newValue);
    writeToFile(bodyVal);
    repLine = repLine + getBlockHeight(newValue);
    return bodyVal;
  }

  private void writeToFile(String str) {
    try {
      String repRow = getLeftMarginString()+str;
      if ((repRow.length() > getPageWidth())&&(getPageWidth()>0)){
        repRow = repRow.substring(0,getPageWidth());
      }
      String wstr = repRow+printer.getNewline();
      file.write(wstr);
//      file.write(new String(wstr.getBytes("UTF8")));
//      file.write(wstr.getBytes());
    } catch (Exception e) {
      System.out.println("Unable to write text "+str+" to file. "+e);
    }
  }
  private int getPageWidth() {
    int pwidth = document.getPageWidth();
    if (pwidth == 0) pwidth = printer.getPageWidth();
    return pwidth;
  }
/*  private boolean chkWriteBreak(String oldValue,String newValue) {
    if (oldValue.equals("")&&newValue.equals("")) return false;
    if (oldValue.equals(newValue)) return false;
    oldValue = newValue;
    return true;
  }*/

  public String makeLine(String lineStr)  {
    String rezLine = handleTags(lineStr,"<#","#>"); //data
    rezLine = handleTags(rezLine,"<$","$>"); //escape seq
    rezLine = handleTags(rezLine,"<%sum(",")%>"); //sume
    rezLine = handleTags(rezLine,"<&calc(",")&>"); //kalkulacije
    return rezLine;
  }

  private String handleTags(String lineStr,String blimit,String elimit) {
    String tempStr = new String(lineStr);
    String rezStr = "";
    while (tempStr.length()>0) {
      lineTag lt = getTag(tempStr,blimit,elimit);
      if (lt==null) {
        rezStr = rezStr.concat(tempStr);
        break;
      }
      rezStr = rezStr.concat(tempStr.substring(0,lt.begin-blimit.length()));
      rezStr = rezStr.concat(lt.getValue());
      tempStr = tempStr.substring(lt.end+elimit.length());
    }
    return rezStr;
  }

  private lineTag getTag(String str,String blimiter,String elimiter) {

    int startTag = str.indexOf(blimiter)+blimiter.length();
    if (startTag == -1) return null;

    int endTag = str.indexOf(elimiter);
    if (endTag == -1) return null;

    String tagName = str.substring(startTag,endTag);
//    System.err.println("TagName - "+tagName);
    int tagLen = -1;
    int tagAligment = -1;
// parametri iz taga
    if (tagName.indexOf("|") > -1) {
      String resto = tagName.substring(tagName.indexOf("|")+1);
      String sTagLen = resto;
      //length
      if (resto.indexOf("|") != -1) {
        sTagLen = sTagLen.substring(0,sTagLen.indexOf("|"));
      }
      tagLen = Integer.parseInt(sTagLen);
      //aligment
      resto = resto.substring(resto.indexOf("|")+1);
      tagAligment = getAligmentFromString(resto);
      tagName = tagName.substring(0,tagName.indexOf("|"));
    }
//    System.out.println("TagName 2 - "+tagName);
    String tagValue=null;
//value iz dataseta
    if (isDataFromDataSet(tagName)) {
      com.borland.dx.dataset.Variant Vv = new com.borland.dx.dataset.Variant();
      if (tagAligment == -1) {
        tagAligment = com.borland.dbswing.DBUtilities.convertJBCLToSwingAlignment(dataSet.getColumn(tagName).getAlignment(),true);
      }
      dataSet.getVariant(tagName,Vv);
      tagValue = hr.restart.robno.sgQuerys.getSgQuerys().format2(dataSet,tagName);
//      tagValue = Vv.toString();
    } else if (getValueFromDataObject(tagName)!=null) {
      tagValue = getValueFromDataObject(tagName);
    } else if (getEscapeSequence(tagName)!=null) {
      tagValue = getEscapeSequence(tagName);
      if (tagValue.equals(printer.getNewline())) tagValue = tagValue + getLeftMarginString();
    } else {
      tagValue = tagName; //return null; toUpperCase
    }
    if (tagName.equals("")) return null;
//    System.out.println("TagValue "+tagValue);
//    System.out.println("Vraccam  "+ getLineTag(tagName,tagValue,startTag,endTag,blimiter,tagLen,tagAligment));
    return getLineTag(tagName,tagValue,startTag,endTag,blimiter,tagLen,tagAligment);
  }

  private lineTag getLineTag(String tagName,String tagValue,int startTag, int endTag, String blimit, int tagLen, int tagAligment) {
    lineTag cvt = null;
    for (int i=0;i<vecTags.size();i++) {
      cvt = (lineTag)vecTags.get(i);
      if (cvt.tag.equals(tagName)) {
        break;
      } else {
        cvt = null;
      }
    }
    if (cvt == null) {
      cvt = new lineTag(tagName,tagValue,startTag,endTag,blimit,tagLen,tagAligment);
      vecTags.add(cvt);
    } else {
      cvt.begin = startTag;
      cvt.end = endTag;
      cvt.type = blimit;
      cvt.length = tagLen;
      cvt.alignment = tagAligment;
      cvt.setValue(tagValue);
    }
    return cvt;
  }
  private String getValueFromDataObject(String fieldName) {
    if (getDataObject() == null) return null;
    try {
//ST.prn("trying to invoke string getter for data object class");
      String retVal = getStringFromGetter(getDataObject(),fieldName);
      if (retVal == null) {
        java.lang.reflect.Field fld = getDataObject().getClass().getField(fieldName);
        retVal = fld.get(getDataObject()).toString();
      }
      return retVal;
    } catch (Exception e) {
//System.out.println(getDataObject().getClass().toString()+"----"+fieldName);
//e.printStackTrace();
      return null;
    }
  }
  private int getAligmentFromString(String str) {
    if (str.equalsIgnoreCase("center")) return javax.swing.SwingConstants.CENTER;
    if (str.equalsIgnoreCase("right")) return javax.swing.SwingConstants.RIGHT;
    if (str.equalsIgnoreCase("left")) return javax.swing.SwingConstants.LEFT;
    return -1;
  }
  /**
   * vraca string parametar za alignment ovisno o ulaznom parametru
   * koji bi morao biti javax.swing.SwingConstants.CENTER,RIGHT ili LEFT,
   * ako on to nije vraca " " (blenk komada jedan).
   */
  public String getStringFromAlignment(int align) {
    if (align == javax.swing.SwingConstants.CENTER) return "center";
    if (align == javax.swing.SwingConstants.RIGHT) return "right";
    if (align == javax.swing.SwingConstants.LEFT) return "left";
    return " ";
  }
  /**
   * vraca string parametar za alignment ovisno o ulaznom parametru
   * koji bi morao biti Column.getAlignment(),
   * ako on to nije vraca " " (blenk komada jedan).
   */
  public String getStringFromDataAlignment(int columnAlign) {
    return getStringFromAlignment(com.borland.dbswing.DBUtilities.convertJBCLToSwingAlignment(columnAlign,true));
  }
  private boolean isDataFromDataSet(String colName) {
    if (dataSet == null) return false;
    if (dataSet.hasColumn(colName) == null) return false;
    return true;
  }

  private String getEscapeSequence(String tagName) {
    String printerSeq = getStringFromGetter(printer,tagName);
    String documentSeq = getStringFromGetter(document,tagName);
    if (documentSeq!=null) return documentSeq;
    if (printerSeq!=null) return printerSeq;
    return null;
  }
  private String getStringFromGetter(Object obj,String name) {
//    System.out.println("NAME : " + name);
    String metName = "get".concat(name.substring(0,1).toUpperCase()).concat(name.substring(1));
    try {
      java.lang.reflect.Method metod = obj.getClass().getMethod(metName,null);
      return metod.invoke(obj,null).toString();
    } catch (Exception e) {
      return null;
    }
  }
  /**
   * <pre>
   * - za dataSet protrci sa next() kroz dataset i za svaki slog kaze add()
   * - za dataObject, ako je instanceof sg.com.elixir.reportwriter.datasource.IDataProvider
   *   protrci kroz enumeraciju getData() i za svaki member kaze add() i na kraju close()
   * - za ostale slucajeve (dataObject koji nije instance IDataProvider) overridati i napisati svoj kod
   */
  public void makeReport() {
    if (getDataSet() != null) {
      getDataSet().open();
      getDataSet().first();
      start();
      do {
        add();
      } while (getDataSet().next());
      finish();
    } else if (getDataObject() != null) {
      if (dataObject instanceof sg.com.elixir.reportwriter.datasource.IDataProvider) {
        Object savedDO = dataObject;
        java.util.Enumeration _enum = ((sg.com.elixir.reportwriter.datasource.IDataProvider)dataObject).getData();
        start();
        while (_enum.hasMoreElements()) {
//          setDataObject(enum.nextElement());
          dataObject = _enum.nextElement();
          add();
        }
        finish();
        dataObject = savedDO;
      } else {
        System.out.println("Nije prepoznat tip objekta. Potrebno overridati mxReport.makeReport()");
      }
    } else {
      System.out.println("Niti dataSet niti dataObject nisu setirani. Nemam od kuda ispisati report");
    }
  }
  /**
   * 
   * @param printCommand
   * @param _f
   * @return
   */
  public static boolean bshEvalPrintCommand(String printCommand, String _f) {
    if (!(printCommand.startsWith("$shc") || printCommand.startsWith("$shf"))) return false;
    if (_f == null) _f = System.getProperty("user.dir")+System.getProperty("file.separator")+mxReport.TMPPRINTFILE;
    try {
      String shellcmd = printCommand.substring(4).trim();
      if (printCommand.startsWith("$shf")) {
        String _c = FileHandler.readFile(Aus.findFileAnywhere(shellcmd).getPath());
        shellcmd = _c;
      }
      Interpreter i = new Interpreter();
      i.set("_f", _f);
      i.eval(shellcmd);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
/**
 * Ispisuje report koristeci mxRM.printCommand kao externu komandu
 */
  public void print() {
    try {
      if (!bshEvalPrintCommand(RM.getPrintCommand(), null)) {
System.out.println("mxReport:: exec proc "+RM.getPrintCommand());
        Process proc = java.lang.Runtime.getRuntime().exec(RM.getPrintCommand());
System.out.println("mxReport:: waitFor proc "+RM.getPrintCommand());

System.out.println("mxReport:: outputting input stream for "+proc);
      Util.bufferedReadOut(proc.getInputStream());
System.out.println("mxReport:: outputting error stream for "+proc);
      Util.bufferedReadOut(proc.getErrorStream());
System.out.println("mxReport:: done with proc "+proc);
//      while ((ch = proc.getErrorStream().read()) > -1) System.out.write(ch);
//      while ((ch = proc.getInputStream().read()) > -1) System.out.write(ch);
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Ispis neuspjesan Exeption: "+e+"\nCommand: "+RM.getPrintCommand());
    }
  }
  public class lineTag {
    public String tag="";
    private String value="";
    public java.math.BigDecimal sumValue = null;
    public String type="";
    public int begin=0;
    public int end=0;
    public int length=-1;
    public int alignment=0;
    private boolean isSumable = true;

    public lineTag() {
    }
    public lineTag(String tag1,String value1,int begin1,int end1,String type1,int length1, int alignment1) {
      tag = tag1;
      setValue(value1);
      begin = begin1;
      end = end1;
      type = type1;
      length = length1;
      alignment = alignment1;
    }
    public void setValue(String newValue) {
      boolean isChanged = ((!value.equals(newValue))&&(!type.equals("<%sum(")));
      value = newValue;
      if (isChanged) addSum();
    }
    public String getValue() {
      String retVal;
      if (type.equals("<%sum(")) {
        if (sumValue != null) retVal = sumValue.toString();
          else retVal = "";
      } else if (type.equals("<&calc(")) {
//kalkulacija
        char[] cvalue = value.toCharArray();
        value = "";
        for (int i=0;i<cvalue.length;i++){
          if (cvalue[i] != '.') value += cvalue[i];
        }
        value = value.replace(',','.');
        java.math.BigDecimal rvbd = Aus.zero2;
        try {
        rvbd = new java.math.BigDecimal(hr.restart.util.Util.getUtil().mathEvalString(value)).setScale(2,java.math.BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
          e.printStackTrace();
        }
        retVal = hr.restart.robno.sgQuerys.getSgQuerys().format(rvbd,2); //rvbd+"";
      } else {
        retVal = value;
      }
      return hr.restart.util.Util.getUtil().alignString(retVal,length,alignment);
    }
    private void addSum() {
      if (!isSumable) return;
      try {
        java.math.BigDecimal addbd = new java.math.BigDecimal(value.trim());
        if (sumValue == null) {
          sumValue = addbd;
        } else {
          sumValue = sumValue.add(addbd);
        }
      } catch (Exception e) {
        isSumable = false;
      }
    }

    public String toString() {
      String strSum = "";
      if (sumValue != null) strSum = ";\n sumValue = "+sumValue.toString();
      return "\nlineTag:\n tag = "+tag
              +";\n value = "+value
              +strSum
              +";\n begin = "+begin
              +";\n end = "+end
              +";\n type = "+type
              +";\n length = "+length
              +";\n align = "+alignment+";\n";
    }
  }
}
