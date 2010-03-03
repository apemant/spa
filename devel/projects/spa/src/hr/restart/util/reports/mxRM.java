/****license*****************************************************************
**   file: mxRM.java
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class mxRM {

  public mxRM() {
  }
  private String port;
  private String printCommand;
  private String viewer;

  public String getPort() {
    return port;
  }
  public void setPort(String newPort) {
    port = newPort;
  }
  public void setPrintCommand(String newPrintCommand) {
    printCommand = newPrintCommand;
  }
  public String getPrintCommand() {
    int hPos = printCommand.indexOf("#");
    if (hPos<0) return printCommand;
    String print_file = System.getProperty("user.dir")+System.getProperty("file.separator")+mxReport.TMPPRINTFILE;
    String retVal = printCommand.substring(0,hPos).concat(print_file).concat(printCommand.substring(hPos+1));
    return retVal;
  }
  public void setViewer(String newViewer) {
    viewer = newViewer;
  }
  public String getViewer() {
    return viewer;
  }
/**
 * Inicijalizira mxRM iz dataseta
 */
  public void init(com.borland.dx.dataset.DataSet ds) {
    this.setPort(ds.getString("PORT"));
    this.setPrintCommand(ds.getString("LOCALCOM"));
//    this.setViewer(ds.getString("VIEWER"));
  }
/**
 * Inicijalizira mxRM iz ResourceBundlea
 */
  public void init(java.util.ResourceBundle res) {
    this.setPort(res.getString("PORT"));
    this.setPrintCommand(res.getString("PRINTCOMMAND"));
    this.setViewer(res.getString("VIEWER"));
  }
/**
 * Vraca defaultno mxRM
 */
  public static mxRM getDefaultMxRM() {
    mxRM mxrm = new mxRM();
    com.borland.dx.sql.dataset.QueryDataSet printerRM = hr.restart.baza.dM.getDataModule().getMxPrinterRM();
    printerRM.open();
    if(printerRM.getRowCount()>0){
      try{
        String rMjest = hr.restart.sisfun.frmParam.getParam("sisfun", "printerRMcmnd", "1", "Radno mjesto", true);
        if(hr.restart.util.lookupData.getlookupData().raLocate(printerRM, "CRM", rMjest)){
          System.out.println("radno mjesto "+rMjest+" pronadjeno :)");
        } else{
          printerRM.first();
          mxrm.init(printerRM);
        }
        mxrm.init(printerRM);

        System.out.println("print command "+mxrm.getPrintCommand());

      } catch(Exception ex){
        printerRM.first();
        mxrm.init(printerRM);

      }

    } else{
      mxrm.setPort("LPT1");
      String defaultPrintCommand = hr.restart.util.IntParam.getTag("defprintcommand");
      if (defaultPrintCommand.equals("")) defaultPrintCommand = "type # > lpt1";
      mxrm.setPrintCommand(defaultPrintCommand);
      mxrm.setViewer("");
    }
    return mxrm;
  }
}
