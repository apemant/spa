/****license*****************************************************************
**   file: DefaultColumnFormater.java
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
/*
 * Created on Dec 27, 2004
 */
package hr.restart.util.textconv;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * Simple, simple, simple, simple column formater
 * @author andrej
 */
public class DefaultColumnFormater implements IColumnFormater {
  private static Logger log = Logger.getLogger(DefaultColumnFormater.class);
  //date format
  private String year = "YYYY";
  private String date = "DD";
  private String month = "MM";
  //integer & bigdecimal format
  private String digit = "N";
  private String signum = "-";
  private String decimaldot = ".";//only for bigdecimal
  
  //string format
  private String stringvar = "?";
  private static DefaultColumnFormater defaultFormater;
  
  public static void setDefaultFormater(DefaultColumnFormater defaultFormater) {
    DefaultColumnFormater.defaultFormater = defaultFormater;
  }
  public static DefaultColumnFormater getDefaultFormater() {
    if (defaultFormater == null) {
      defaultFormater = new DefaultColumnFormater();
    }
    return defaultFormater;
  }
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public String getDecimaldot() {
    return decimaldot;
  }
  public void setDecimaldot(String decimaldot) {
    this.decimaldot = decimaldot;
  }
  public String getDigit() {
    return digit;
  }
  public void setDigit(String digit) {
    this.digit = digit;
  }
  public String getYear() {
    return year;
  }
  public void setYear(String fourdigityear) {
    this.year = fourdigityear;
  }
  public String getMonth() {
    return month;
  }
  public void setMonth(String month) {
    this.month = month;
  }
  public String getSignum() {
    return signum;
  }
  public void setSignum(String signum) {
    this.signum = signum;
  }
  public String getStringvar() {
    return stringvar;
  }
  public void setStringvar(String stringvar) {
    this.stringvar = stringvar;
  }  
  public Object applyFormat(String content, String format, int type) {
    if (type == ColumnTypes.INTEGER) {
      return parseInteger(content, format);
    } else if (type == ColumnTypes.BIGDECIMAL) {
      return parseBigDecimal(content, format);
    } else if (type == ColumnTypes.TIMESTAMP) {
      return parseTimestamp(content, format);
    } else {
      return parseString(content, format);
    }
  }
  /**
   * @param content
   * @param format
   * @return
   */
  private String parseString(String content, String format) {
    if (format == null || format.equals("")) {
      return content;
    } else {
      //TODO implement this!!
      return content;
    }
  }
  /**
   * @param content
   * @param format
   * @return
   */
  private Timestamp parseTimestamp(String content, String format) {
    if (format == null || format.equals("")) {
      format = "YYYYMMDD";
    }
    Calendar cal = Calendar.getInstance();
    //year
    int yindex = format.indexOf(getYear());
    int ylen = getYear().length();
    if (yindex == -1) throw new IllegalArgumentException("Illegal date format "+format);
    String sy = content.substring(yindex, yindex+ylen);
    if (log.isDebugEnabled()) {
      log.debug("year = " + sy);
    }
    cal.set(Calendar.YEAR, Integer.parseInt(sy));
    //month
    int mindex = format.indexOf(getMonth());
    int mlen = getMonth().length();
    if (mindex == -1) throw new IllegalArgumentException("Illegal date format "+format);
    String sm = content.substring(mindex, mindex+mlen);
    if (log.isDebugEnabled()) {
      log.debug("month = " + sm);
    }
    cal.set(Calendar.MONTH, (Integer.parseInt(sm)-1));
    //date
    int dindex = format.indexOf(getDate());
    int dlen = getDate().length();
    if (dindex == -1) throw new IllegalArgumentException("Illegal date format "+format);
    String sd = content.substring(dindex, dindex+dlen);
    if (log.isDebugEnabled()) {
      log.debug(" date = " + sd);
    }
    cal.set(Calendar.DATE, Integer.parseInt(sd));
    return new Timestamp(cal.getTime().getTime());
  }
  /**
   * @param content
   * @param format
   * @return
   */
  private BigDecimal parseBigDecimal(String content, String format) {
    Integer pint = parseInteger(content, format);
    int dotpos = format.indexOf(getDecimaldot());
    int scale = format.length()-dotpos-1;
    BigDecimal divider = new BigDecimal(Math.pow(10,scale));
    if (log.isDebugEnabled()) {
      log.debug("divider = "+divider);
    }
    BigDecimal ret = new BigDecimal(pint.doubleValue());
    ret = ret.divide(divider, scale, BigDecimal.ROUND_HALF_UP);
    return ret;
  }
  /**
   * @param content
   * @param format
   * @return
   */
  private Integer parseInteger(String content, String format) {
    if (format == null || format.equals("")) {
      return new Integer(content);
    } else {
      String ret = "";
      char[] cc = content.toCharArray();
      char cdigit = getDigit().charAt(0);
      for (int i = 0; i < cc.length; i++) {
        if (format.charAt(i) == cdigit && cc[i] != getDecimaldot().charAt(0)) {
          ret = ret + cc[i];
        }
      }
      if (log.isDebugEnabled()) {
        log.debug(ret);
      }
      
      
      Integer rint = new Integer(ret.trim());
      int sindex = format.indexOf(getSignum());
      if (sindex > -1) {
        String ss = content.substring(sindex,sindex+getSignum().length());
        if (ss.equals(getSignum())) {
          rint = new Integer(rint.intValue()*-1);
        }
      }
      return rint;
    }
  }

  public static void main(String[] arg) {
    DefaultColumnFormater cf = DefaultColumnFormater.getDefaultFormater();
    System.out.println(cf.applyFormat(arg[0],arg[1],ColumnTypes.getType(arg[2])));
  }
}
