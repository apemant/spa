/****license*****************************************************************
**   file: mxPrinter.java
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

public class mxPrinter {

  public mxPrinter() {
  }
  private int topMargin;
  private int leftMargin;
  private int lineFeed_n;
  private String lineFeedSec;
  private int pageSize;
  private int pageWidth;
  private int botMargin;
  private int paperStart_mm;
  private String reset;
  private String newline;
  private String cpi10;
  private String cpi12;
  private String cpi15;
  private String cpi17;
  private String condensedON;
  private String condensedOFF;
  private String lpi1_8;
  private String lpi1_6;
  private String lpiN_180;
  private String lpiN_360;
  private String doubleWidthON;
  private String doubleWidthOFF;
  private String doubleHeightON;
  private String doubleHeightOFF;
  private String propGustON;
  private String propGustOFF;
  private String endOfPage;
  private String userSec1;
  private String userSec2;
  private String userSec3;
  private String userSec4;

  public int getTopMargin() {
    return topMargin;
  }
  public void setTopMargin(int newTopMargin) {
    topMargin = newTopMargin;
  }
  public void setLeftMargin(int newLeftMargin) {
    leftMargin = newLeftMargin;
  }
  public int getLeftMargin() {
    return leftMargin;
  }
  public void setLineFeed_n(int newLineFeed_n) {
    lineFeed_n = newLineFeed_n;
  }
  public int getLineFeed_n() {
    return lineFeed_n;
  }
  public void setLineFeedSec(String newLineFeedSec) {
    lineFeedSec = newLineFeedSec;
  }
  public String getLineFeedSec() {
    return lineFeedSec;
  }
  public void setPageSize(int newPageSize) {
    pageSize = newPageSize;
  }
  public int getPageSize() {
    return pageSize;
  }
  public void setPageWidth(int newPageWidth) {
    pageWidth = newPageWidth;
  }
  public int getPageWidth() {
    return pageWidth;
  }
  public void setBotMargin(int newBotMargin) {
    botMargin = newBotMargin;
  }
  public int getBotMargin() {
    return botMargin;
  }
  public void setPaperStart_mm(int newPaperStart_mm) {
    paperStart_mm = newPaperStart_mm;
  }
  public int getPaperStart_mm() {
    return paperStart_mm;
  }
  public void setReset(String newReset) {
    reset = newReset;
  }
  public String getReset() {
    return reset;
  }
  public void setNewline(String newNewline) {
    newline = newNewline;
  }
  public String getNewline() {
    return newline;
  }
  public void setCpi10(String newCpi10) {
    cpi10 = newCpi10;
  }
  public String getCpi10() {
    return cpi10;
  }
  public void setCpi12(String newCpi12) {
    cpi12 = newCpi12;
  }
  public String getCpi12() {
    return cpi12;
  }
  public void setCpi15(String newCpi15) {
    cpi15 = newCpi15;
  }
  public String getCpi15() {
    return cpi15;
  }
  public void setCpi17(String newCpi17) {
    cpi17 = newCpi17;
  }
  public String getCpi17() {
    return cpi17;
  }
  public void setCondensedON(String newCondensedON) {
    condensedON = newCondensedON;
  }
  public String getCondensedON() {
    return condensedON;
  }
  public void setCondensedOFF(String newCondensedOFF) {
    condensedOFF = newCondensedOFF;
  }
  public String getCondensedOFF() {
    return condensedOFF;
  }
  public void setLpi1_8(String newLpi1_8) {
    lpi1_8 = newLpi1_8;
  }
  public String getLpi1_8() {
    return lpi1_8;
  }
  public void setLpi1_6(String newLpi1_6) {
    lpi1_6 = newLpi1_6;
  }
  public String getLpi1_6() {
    return lpi1_6;
  }
  public void setLpiN_180(String newLpiN_180) {
    lpiN_180 = newLpiN_180;
  }
  public String getLpiN_180() {
    return lpiN_180;
  }
  public void setLpiN_360(String newLpiN_360) {
    lpiN_360 = newLpiN_360;
  }
  public String getLpiN_360() {
    return lpiN_360;
  }
  public void setDoubleWidthON(String newDoubleWidthON) {
    doubleWidthON = newDoubleWidthON;
  }
  public String getDoubleWidthON() {
    return doubleWidthON;
  }
  public void setDoubleWidthOFF(String newDoubleWidthOFF) {
    doubleWidthOFF = newDoubleWidthOFF;
  }
  public String getDoubleWidthOFF() {
    return doubleWidthOFF;
  }
  public void setDoubleHeightON(String newDoubleHeightON) {
    doubleHeightON = newDoubleHeightON;
  }
  public String getDoubleHeightON() {
    return doubleHeightON;
  }
  public void setDoubleHeightOFF(String newDoubleHeightOFF) {
    doubleHeightOFF = newDoubleHeightOFF;
  }
  public String getDoubleHeightOFF() {
    return doubleHeightOFF;
  }
  public void setPropGustON(String newPropGustON) {
    propGustON = newPropGustON;
  }
  public String getPropGustON() {
    return propGustON;
  }
  public void setPropGustOFF(String newPropGustOFF) {
    propGustOFF = newPropGustOFF;
  }
  public String getPropGustOFF() {
    return propGustOFF;
  }
  public void setEndOfPage(String newEndOfPage) {
    endOfPage = newEndOfPage;
  }
  public String getEndOfPage() {
    return endOfPage;
  }
  public void setUserSec1(String newUserSec1) {
    userSec1 = newUserSec1;
  }
  public String getUserSec1() {
    return userSec1;
  }
  public void setUserSec2(String newUserSec2) {
    userSec2 = newUserSec2;
  }
  public String getUserSec2() {
    return userSec2;
  }
  public void setUserSec3(String newUserSec3) {
    userSec3 = newUserSec3;
  }
  public String getUserSec3() {
    return userSec3;
  }
  public void setUserSec4(String newUserSec4) {
    userSec4 = newUserSec4;
  }
  public String getUserSec4() {
    return userSec4;
  }
  /**
   * Inicijalizira mxReport klasu iz dataseta (baze)
   */
  public void init(com.borland.dx.dataset.DataSet ds) throws Exception {
    this.setBotMargin(ds.getInt("BOTMARGIN"));
    this.setCondensedOFF(ds.getString("CONDENSOFF"));
    this.setCondensedON(ds.getString("CONDENSON"));
    this.setCpi10(ds.getString("CPI10"));
    this.setCpi12(ds.getString("CPI12"));
    this.setCpi15(ds.getString("CPI15"));
    this.setCpi17(ds.getString("CPI17"));
    this.setDoubleHeightOFF(ds.getString("DOUBLEHOFF"));
    this.setDoubleHeightON(ds.getString("DOUBLEHON"));
    this.setDoubleWidthOFF(ds.getString("DOUBLEWOFF"));
    this.setDoubleWidthON(ds.getString("DOUBLEWON"));
    this.setEndOfPage(ds.getString("ENDOFPAGE"));
    this.setLeftMargin(ds.getInt("LEFTMARGIN"));
    this.setLineFeed_n(ds.getInt("LINEFEED_N"));
    this.setLineFeedSec(ds.getString("LINEFEEDSEC"));
    this.setLpi1_6(ds.getString("LPI6"));
    this.setLpi1_8(ds.getString("LPI8"));
    this.setLpiN_180(ds.getString("LF_N180"));
    this.setLpiN_360(ds.getString("LF_N360"));
    this.setNewline(ds.getString("NEWLINE"));
    this.setPageSize(ds.getInt("PAGESIZE"));
    this.setPageWidth(ds.getInt("PAGEWIDTH"));
    this.setPaperStart_mm(ds.getInt("PAPERSTART_MM"));
    this.setPropGustOFF(ds.getString("PROPOFF"));
    this.setPropGustON(ds.getString("PROPON"));
    this.setReset(ds.getString("RESET"));
    this.setTopMargin(ds.getInt("TOPMARGIN"));
    this.setUserSec1(ds.getString("USER1"));
    this.setUserSec2(ds.getString("USER2"));
    this.setUserSec3(ds.getString("USER3"));
    this.setUserSec4(ds.getString("USER4"));
  }
/**
 * Inicijalizira mxReport iz ResourceBundle-a
 */
  public void init(java.util.ResourceBundle res) throws Exception {
    this.setBotMargin(Integer.parseInt(res.getString("BOTMARGIN")));
    this.setCondensedOFF(res.getString("CONDENSEDOFF"));
    this.setCondensedON(res.getString("CONDENSEDON"));
    this.setCpi10(res.getString("CPI10"));
    this.setCpi12(res.getString("CPI12"));
    this.setCpi15(res.getString("CPI15"));
    this.setCpi17(res.getString("CPI17"));
    this.setDoubleHeightOFF(res.getString("DOUBLEHEIGHTOFF"));
    this.setDoubleHeightON(res.getString("DOUBLEHEIGHTON"));
    this.setDoubleWidthOFF(res.getString("DOUBLEWIDTHOFF"));
    this.setDoubleWidthON(res.getString("DOUBLEWIDTHON"));
    this.setEndOfPage(res.getString("ENDOFPAGE"));
    this.setLeftMargin(Integer.parseInt(res.getString("LEFTMARGIN")));
    this.setLineFeed_n(Integer.parseInt(res.getString("LINEFEED_N")));
    this.setLineFeedSec(res.getString("LINEFEEDSEC"));
    this.setLpi1_6(res.getString("LPI1_6"));
    this.setLpi1_8(res.getString("LPI1_8"));
    this.setLpiN_180(res.getString("LPIN_180"));
    this.setLpiN_360(res.getString("LPIN_360"));
    this.setNewline(res.getString("NEWLINE"));
    this.setPageSize(Integer.parseInt(res.getString("PAGESIZE")));
    this.setPageWidth(Integer.parseInt(res.getString("PAGEWIDTH")));
    this.setPaperStart_mm(Integer.parseInt(res.getString("PAPERSTART_MM")));
    this.setPropGustOFF(res.getString("PROPGUSTOFF"));
    this.setPropGustON(res.getString("PROPGUSTON"));
    this.setReset(res.getString("RESET"));
    this.setTopMargin(Integer.parseInt(res.getString("TOPMARGIN")));
    this.setUserSec1(res.getString("USERSEC1"));
    this.setUserSec2(res.getString("USERSEC2"));
    this.setUserSec3(res.getString("USERSEC3"));
    this.setUserSec4(res.getString("USERSEC4"));
  }
/**
 * Vraca defaultni inicijalizirani printer
 */
  public static mxPrinter getDefaultMxPrinter() {
    mxPrinter mxp = new mxPrinter();
    mxp.setBotMargin(0);
    mxp.setCondensedOFF("\u0012"); //"\u001B\u0012");
    mxp.setCondensedON("\u001B\u000F");
    mxp.setCpi10("\u001B\u0050");
    mxp.setCpi12("\u001B\u004D");
    mxp.setCpi15("\u001B\u0067");
    mxp.setCpi17("\u001B\u000F");
    mxp.setDoubleHeightOFF("\u001B\u00770");
    mxp.setDoubleHeightON("\u001B\u00771");
    mxp.setDoubleWidthOFF("\u001B\u00570");
    mxp.setDoubleWidthON("\u001B\u00571");
    mxp.setEndOfPage("\u000C");
    mxp.setLeftMargin(0);
    mxp.setLineFeed_n(180);
    mxp.setLineFeedSec("");
    mxp.setLpi1_6("\u001B\u0032");
    mxp.setLpi1_8("\u001B\u0030");
    mxp.setLpiN_180("\u001B\u0033");
    mxp.setLpiN_360("\u001B\u002B");
    mxp.setNewline("\n");
    mxp.setPageSize(0);
    mxp.setPageWidth(0);
    mxp.setPaperStart_mm(0);
    mxp.setPropGustOFF("\u001B\u00702");
    mxp.setPropGustON("\u001B\u00701");
    mxp.setReset("\u001B\u0040");
    mxp.setTopMargin(0);
    mxp.setUserSec1("");
    mxp.setUserSec2("");
    mxp.setUserSec3("");
    mxp.setUserSec4("");

    return mxp;
  }

  //*** dodao Rade
  public int getDefaultMxPrinterA5()
  {
    setPageSize(36);
    return getPageSize();
  }

  public int getDefaultMxPrinterA4()
  {
    setPageSize(72);
    return getPageSize();
  }
}