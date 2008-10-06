/****license*****************************************************************
**   file: mxDocument.java
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

public class mxDocument {

  public mxDocument() {
  }
  private int pageSize;
  private int pageWidth;
  private String lineSpacing;
  private int topMargin_mm;
  private int leftMargin;
  private boolean ejectPaper;
  private String user1;
  private String user2;
  private String user3;
  private String user4;
  private String user5;
  private String user6;
  private String user7;
  private String user8;
  private String user9;
  private String user0;

  public int getPageSize() {
    return pageSize;
  }
  public void setPageSize(int newPageSize) {
    pageSize = newPageSize;
  }
  public void setPageWidth(int newPageWidth) {
    pageWidth = newPageWidth;
  }
  public int getPageWidth() {
    return pageWidth;
  }
  public void setLineSpacing(String newLineSpacing) {
    lineSpacing = newLineSpacing;
  }
  public String getLineSpacing() {
    return lineSpacing;
  }
  public void setTopMargin_mm(int newTopMargin_mm) {
    topMargin_mm = newTopMargin_mm;
  }
  public int getTopMargin_mm() {
    return topMargin_mm;
  }
  public void setLeftMargin(int newLeftMargin) {
    leftMargin = newLeftMargin;
  }
  public int getLeftMargin() {
    return leftMargin;
  }
  public void setEjectPaper(boolean newEjectPaper) {
    ejectPaper = newEjectPaper;
  }
  public boolean isEjectPaper() {
    return ejectPaper;
  }
  public void setUser1(String newUser1) {
    user1 = newUser1;
  }
  public String getUser1() {
    return user1;
  }
  public void setUser2(String newUser2) {
    user2 = newUser2;
  }
  public String getUser2() {
    return user2;
  }
  public void setUser3(String newUser3) {
    user3 = newUser3;
  }
  public String getUser3() {
    return user3;
  }
  public void setUser4(String newUser4) {
    user4 = newUser4;
  }
  public String getUser4() {
    return user4;
  }
  public void setUser5(String newUser5) {
    user5 = newUser5;
  }
  public String getUser5() {
    return user5;
  }
  public void setUser6(String newUser6) {
    user6 = newUser6;
  }
  public String getUser6() {
    return user6;
  }
  public void setUser7(String newUser7) {
    user7 = newUser7;
  }
  public String getUser7() {
    return user7;
  }
  public void setUser8(String newUser8) {
    user8 = newUser8;
  }
  public String getUser8() {
    return user8;
  }
  public void setUser9(String newUser9) {
    user9 = newUser9;
  }
  public String getUser9() {
    return user9;
  }
  public void setUser0(String newUser0) {
    user0 = newUser0;
  }
  public String getUser0() {
    return user0;
  }
/**
 * Inicijalizira mxDocument iz dataseta (baze)
 */
  public void init(com.borland.dx.dataset.DataSet ds) {
    this.setEjectPaper((ds.getString("EJECTPAPER")=="D"));
    this.setLeftMargin(ds.getInt("LEFTMARGIN"));
    this.setLineSpacing(ds.getString("LINESPACING"));
    this.setPageSize(ds.getInt("PAGESIZE"));
    this.setPageWidth(ds.getInt("PAGEWIDTH"));
    this.setTopMargin_mm(ds.getInt("TOPMARGIN_MM"));
    this.setUser0(ds.getString("USER0"));
    this.setUser1(ds.getString("USER1"));
    this.setUser2(ds.getString("USER2"));
    this.setUser3(ds.getString("USER3"));
    this.setUser4(ds.getString("USER4"));
    this.setUser5(ds.getString("USER5"));
    this.setUser6(ds.getString("USER6"));
    this.setUser7(ds.getString("USER7"));
    this.setUser8(ds.getString("USER8"));
    this.setUser9(ds.getString("USER9"));
  }
/**
 * Inicijalizira mxDocument iz ResourceBundlea
 */
  public void init(java.util.ResourceBundle res) {
    this.setEjectPaper((res.getString("EJECTPAPER")=="D"));
    this.setLeftMargin(Integer.parseInt(res.getString("LEFTMARGIN")));
    this.setLineSpacing(res.getString("LINESPACING"));
    this.setPageSize(Integer.parseInt(res.getString("PAGESIZE")));
    this.setPageWidth(Integer.parseInt(res.getString("PAGEWIDTH")));
    this.setTopMargin_mm(Integer.parseInt(res.getString("TOPMARGIN_MM")));
    this.setUser0(res.getString("USER0"));
    this.setUser1(res.getString("USER1"));
    this.setUser2(res.getString("USER2"));
    this.setUser3(res.getString("USER3"));
    this.setUser4(res.getString("USER4"));
    this.setUser5(res.getString("USER5"));
    this.setUser6(res.getString("USER6"));
    this.setUser7(res.getString("USER7"));
    this.setUser8(res.getString("USER8"));
    this.setUser9(res.getString("USER9"));
  }
/**
 * Vraca defaultni inicijalizirani mxDocument
 * <pre>
 * setEjectPaper(true);
 * setLeftMargin(0);
 * setLineSpacing("1/6");
 * setPageSize(0);
 * setTopMargin_mm(0);
 * </pre>
 */
  public static mxDocument getDefaultMxDocument() {
    mxDocument mxD = new mxDocument();
    mxD.setEjectPaper(true);
    mxD.setLeftMargin(-1);
    mxD.setLineSpacing("");
    mxD.setPageSize(0);
    mxD.setPageWidth(0);
    mxD.setTopMargin_mm(-1);
    mxD.setUser0("");
    mxD.setUser1("");
    mxD.setUser2("");
    mxD.setUser3("");
    mxD.setUser4("");
    mxD.setUser5("");
    mxD.setUser6("");
    mxD.setUser7("");
    mxD.setUser8("");
    mxD.setUser9("");

    return mxD;
  }
}