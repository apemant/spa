/****license*****************************************************************
**   file: repER1.java
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
package hr.restart.pl;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.text.DateFormat;

import com.borland.dx.dataset.DataSet;

public class repER1novi implements raReportData {

//  hr.restart.robno._Main main;
  frmER1 frer1 = frmER1.getInstance();
  DataSet ds = frer1.getRepSet();
  
  DateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy");

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repER1novi() {
    ru.setDataSet(ds);
  }
  
  public raReportData getRow(int idx) {
    return this;
  }

  public int getRowCount() {
    return 1;
  }

  public void close() {
  }

  public String getNAZIV(){
    return frer1.getKnjNaziv();
  }
  public String getPREFIKSOB(){
    return Aus.ljust(ds.getString("BROBVEZE").substring(0, 3), 3);
  }
  public String getBROBVEZE(){
    return Aus.ljust(ds.getString("BROBVEZE").substring(4), 8);
  }

  public String getIME(){
     return ds.getString("IME");
  }
  public String getPREZIME(){
     return ds.getString("PREZIME");
  }
  public String getJMBG(){
     return Aus.ljust(ds.getString("JMBG"), 13);
  }
  
  public String getOIB(){
    return Aus.ljust(ds.getString("OIB"), 11);
 }
  public String getPREFIKSRAD(){
    return Aus.ljust(ds.getString("BROSIGZO").substring(0, 3), 3);
  }
  public String getBROSIGZO(){
     return Aus.ljust(ds.getString("BROSIGZO").substring(4), 8);
  }
  
  public String getDATROD(){
    return getJMBG().substring(0, 4) +
        (getJMBG().charAt(4) == '9' ? "1" : "2") +
        getJMBG().substring(4, 7);
  }
  
  public String getDRUGISTUP(){
    if (ds.getString("CLANOMF").equals("N")) return "N";
    return "D";
  }
  
  public String getDATOD(){
    return sdf.format(frer1.getDatOd());
  }
  public String getDATDO(){
    return sdf.format(frer1.getDatDo());
  }
  
  private String mjgod(int n) {
    if (ds.rowCount() < n) return "";
    ds.goToRow(n - 1);
    return ds.getShort("MJOBRDOH") + "./" + ds.getShort("GODOBRDOH") + ".";
  }
  
  public String getMJGOD1() { return mjgod(1); }
  public String getMJGOD2() { return mjgod(2); }
  public String getMJGOD3() { return mjgod(3); }
  public String getMJGOD4() { return mjgod(4); }
  public String getMJGOD5() { return mjgod(5); }
  public String getMJGOD6() { return mjgod(6); }

  private BigDecimal brutto(int n) {
    if (ds.rowCount() < n) return null;
    ds.goToRow(n - 1);
    return ds.getBigDecimal("BRUTO").add(ds.getBigDecimal("NAKNADE"));
  }

  public BigDecimal getBRUTTO1() { return brutto(1); }
  public BigDecimal getBRUTTO2() { return brutto(2); }
  public BigDecimal getBRUTTO3() { return brutto(3); }
  public BigDecimal getBRUTTO4() { return brutto(4); }
  public BigDecimal getBRUTTO5() { return brutto(5); }
  public BigDecimal getBRUTTO6() { return brutto(6); }
  
  public BigDecimal getBRUTTO() { 
    return Aus.sum("BRUTO", ds).add(Aus.sum("NAKNADE", ds));
  }
  
  private BigDecimal netto(int n) {
    if (ds.rowCount() < n) return null;
    ds.goToRow(n - 1);
    return ds.getBigDecimal("NETOPK");
  }
  
  public BigDecimal getNETTO1() { return netto(1); }
  public BigDecimal getNETTO2() { return netto(2); }
  public BigDecimal getNETTO3() { return netto(3); }
  public BigDecimal getNETTO4() { return netto(4); }
  public BigDecimal getNETTO5() { return netto(5); }
  public BigDecimal getNETTO6() { return netto(6); }
  
  public BigDecimal getNETTO() { 
    return Aus.sum("NETOPK", ds);
  }
  
  private Integer satin(int n) {
    if (ds.rowCount() < n) return null;
    ds.goToRow(n - 1);
    return new Integer(ds.getBigDecimal("SATIPUNORV").intValue());
  }
  
  public Integer getSATIN1() { return satin(1); }
  public Integer getSATIN2() { return satin(2); }
  public Integer getSATIN3() { return satin(3); }
  public Integer getSATIN4() { return satin(4); }
  public Integer getSATIN5() { return satin(5); }
  public Integer getSATIN6() { return satin(6); }
  
  public Integer getSATIN() {
    return new Integer(Aus.sum("SATIPUNORV", ds).intValue());
  }
  
  private Integer satiprek(int n) {
    if (ds.rowCount() < n) return null;
    ds.goToRow(n - 1);
    return new Integer(ds.getBigDecimal("SATIDUZE").intValue());
  }
  
  public Integer getSATIPREK1() { return satiprek(1); }
  public Integer getSATIPREK2() { return satiprek(2); }
  public Integer getSATIPREK3() { return satiprek(3); }
  public Integer getSATIPREK4() { return satiprek(4); }
  public Integer getSATIPREK5() { return satiprek(5); }
  public Integer getSATIPREK6() { return satiprek(6); }
  
  public Integer getSATIPREK() {
    return new Integer(Aus.sum("SATIDUZE", ds).intValue());
  }
  
  private Integer satiods(int n) {
    if (ds.rowCount() < n) return null;
    ds.goToRow(n - 1);
    return new Integer(ds.getBigDecimal("SATIKRACE").intValue());
  }
  
  public Integer getSATIODS1() { return satiods(1); }
  public Integer getSATIODS2() { return satiods(2); }
  public Integer getSATIODS3() { return satiods(3); }
  public Integer getSATIODS4() { return satiods(4); }
  public Integer getSATIODS5() { return satiods(5); }
  public Integer getSATIODS6() { return satiods(6); }
  
  public Integer getSATIODS() {
    return new Integer(Aus.sum("SATIKRACE", ds).intValue());
  }
  
  private Integer satiuk(int n) {
    if (ds.rowCount() < n) return null;
    ds.goToRow(n - 1);
    return new Integer(ds.getBigDecimal("SATI").intValue());
  }
  
  public Integer getSATIUK1() { return satiuk(1); }
  public Integer getSATIUK2() { return satiuk(2); }
  public Integer getSATIUK3() { return satiuk(3); }
  public Integer getSATIUK4() { return satiuk(4); }
  public Integer getSATIUK5() { return satiuk(5); }
  public Integer getSATIUK6() { return satiuk(6); }
  
  public Integer getSATIUK() {
    return new Integer(Aus.sum("SATI", ds).intValue());
  }
  
  private Integer satines(int n) {
    if (ds.rowCount() < n) return null;
    ds.goToRow(n - 1);
    return new Integer(ds.getBigDecimal("SATIBOL").intValue());
  }
  
  public Integer getSATINES1() { return satines(1); }
  public Integer getSATINES2() { return satines(2); }
  public Integer getSATINES3() { return satines(3); }
  public Integer getSATINES4() { return satines(4); }
  public Integer getSATINES5() { return satines(5); }
  public Integer getSATINES6() { return satines(6); }
  
  public Integer getSATINES() {
    return new Integer(Aus.sum("SATIBOL", ds).intValue());
  }
  
  private Integer satikal(int n) {
    if (ds.rowCount() < n) return null;
    ds.goToRow(n - 1);
    return new Integer(ds.getBigDecimal("FONDSATI").intValue());
  }
  
  public Integer getSATIKAL1() { return satikal(1); }
  public Integer getSATIKAL2() { return satikal(2); }
  public Integer getSATIKAL3() { return satikal(3); }
  public Integer getSATIKAL4() { return satikal(4); }
  public Integer getSATIKAL5() { return satikal(5); }
  public Integer getSATIKAL6() { return satikal(6); }
  
  public Integer getSATIKAL() {
    return new Integer(Aus.sum("FONDSATI", ds).intValue());
  }
  
  public BigDecimal getPBRUTTO() {
    return getBRUTTO().divide(Aus.sum("SATI", ds), 2, BigDecimal.ROUND_HALF_UP);
  }
  
  public BigDecimal getPNETTO() {
    return getNETTO().divide(Aus.sum("SATI", ds), 2, BigDecimal.ROUND_HALF_UP);
  }
  
  public String getUMJESTU(){
    return frer1.getKnjMjesto();
  }
  public String getDANA(){
    return rdu.dataFormatter(vl.getToday());
  }
}