/****license*****************************************************************
**   file: Aus.java
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
package hr.restart.util;

import hr.restart.start;
import hr.restart.baza.Condition;
import hr.restart.baza.ConsoleCreator;
import hr.restart.baza.Knjigod;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.Refresher;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.swing.raCalculatorMask;
import hr.restart.swing.raFieldMask;
import hr.restart.swing.raInhumanNumberMask;
import hr.restart.zapod.OrgStr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;

import javax.swing.FocusManager;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import sg.com.elixir.reportwriter.xml.IModel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
import com.borland.jbcl.layout.constraintsGetter;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class Aus {

  public static final BigDecimal zero0 = new BigDecimal(0);
  public static final BigDecimal zero2 = zero0.setScale(2);
  public static final BigDecimal zero3 = zero0.setScale(3);
  public static final BigDecimal one0 = new BigDecimal(1);
  
  
  private static char[] hexChars = 
    {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
  
  private static char[] sixBitChars = {
    '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k',
    'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F',
    'G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','_','.'
  };
  
  private static int[] varTypes = {Variant.STRING, Variant.INT,
    Variant.SHORT, Variant.LONG, Variant.BIGDECIMAL, Variant.FLOAT,
    Variant.DOUBLE, Variant.TIMESTAMP, Variant.DATE};
  private static Class[] classTypes = {String.class, int.class,
      short.class, long.class, BigDecimal.class, float.class,
      double.class, Timestamp.class, Date.class};
  
  public static Locale hr = Locale.getDefault();//new Locale("hr", "HR", "");
  
  private static NumberFormat nf = NumberFormat.getInstance(hr);
  
  private static ArrayList trace;
  
  private static String bigs = null;
  private static boolean big = false;
  private static int bigf = 10;
    
  private Aus() {
  }
  
  public static Graphics2D forceAntiAlias(Graphics g) {
  	((Graphics2D) g).setRenderingHint(
  			RenderingHints.KEY_TEXT_ANTIALIASING, 
  			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
  	return (Graphics2D) g;
  }
  
  public static void recursiveUpdateSizes(JPanel pan) {
    if (!isBig() || pan instanceof OKpanel) return;
    boolean xy = pan.getLayout() instanceof XYLayout;
    if (xy && !updateLayout(pan)) return;
    for (int i = 0; i < pan.getComponentCount(); i++) {
      if (pan.getComponent(i) instanceof JPanel)
        recursiveUpdateSizes((JPanel) pan.getComponent(i));
      if (pan.getComponent(i) instanceof JTabbedPane)
        checkTabs((JTabbedPane) pan.getComponent(i));
          
      if (xy) updateSizes(pan, pan.getComponent(i));
    }
  }
  
  private static void checkTabs(JTabbedPane tabs) {
    for (int i = 0; i < tabs.getComponentCount(); i++) {
      if (tabs.getComponent(i) instanceof JPanel)
        recursiveUpdateSizes((JPanel) tabs.getComponent(i));
      if (tabs.getComponent(i) instanceof JTabbedPane)
        checkTabs((JTabbedPane) tabs.getComponent(i));
    }
  }
  
  private static boolean updateLayout(JPanel pan) {
    XYLayout xy = (XYLayout) pan.getLayout();
    if (constraintsGetter.isMarked(xy)) return false;
    xy.setWidth(big(xy.getWidth()));
    xy.setHeight(big(xy.getHeight()));
    constraintsGetter.mark(xy);
    return true;
  }
  
  private static void updateSizes(JPanel pan, Component comp) {
    XYLayout xy = (XYLayout) pan.getLayout();
    XYConstraints cons = constraintsGetter.get(xy, comp);
    if (cons != null) {
      cons.setY(big(cons.getY()));
      if (cons.getHeight() > 0) cons.setHeight(big(cons.getHeight()));
      if (cons.getWidth() > 0 && cons.getX() + cons.getWidth() > xy.getWidth() / 2) {
//        cons.setX(big(cons.getX() + cons.getWidth()) - big(cons.getWidth()));
//        cons.setWidth(big(cons.getWidth()));
        cons.setWidth(big(cons.getX() + cons.getWidth()) - big(cons.getX()));
        cons.setX(big(cons.getX()));
      } else {
        cons.setX(big(cons.getX()));
        if (cons.getWidth() > 0) cons.setWidth(big(cons.getWidth()));
      }
      constraintsGetter.set(xy, comp, cons);
    }
  }
  
  public static boolean isBig() {
    if (bigs == null) {
      bigs = IntParam.getTag("screen.big");
      if (bigs == null || bigs.length() == 0) IntParam.setTag("screen.big", bigs = "false");
      big = "true".equals(bigs);
      System.out.println("big check");
      if (big) {
        String f = IntParam.getTag("screen.big.size");
        if (f == null || f.length() == 0) IntParam.setTag("screen.big.size", "10");
        else bigf = Aus.getNumber(f);
        if (bigf < 8) bigf = 8;
        if (bigf > 20) bigf = 20;
      }
    }
    return big;
  }
  
  public static int big(int orig) {
    return isBig() ? orig * bigf / 8 : orig;
  }
  
  public static void clearTrace() {
    if (trace != null) {
      trace.clear();
      trace = null;
    }
  }

  public static synchronized void addTrace(String line) {
    if (trace == null) trace = new ArrayList();
    trace.add(line);
  }

  public static synchronized void dumpTrace() {
    System.out.println(VarStr.join(trace,'\n'));
  }

  public static StackFrame getStackFrame() {
    return StackFrame.getStackFrame();
  }

  /**
   * vraæa true ako se string sastoji iskljuèivo od znamenki.
   */
  public static boolean isDigit(String snum) {
    for (int i = 0; i < snum.length(); i++)
      if (!Character.isDigit(snum.charAt(i))) return false;
    return true;
  }
    
  public static void clear(ReadWriteRow ds, String dest) {
    ds.setBigDecimal(dest, zero0);
  }
  
  public static void clear(ReadWriteRow ds, String[] dest) {
    for (int i = 0; i < dest.length; i++) clear(ds, dest[i]);
  }
  
  public static void set(ReadWriteRow ds, String dest, BigDecimal num) {
    ds.setBigDecimal(dest, num);
  }
  
  public static void set(ReadWriteRow ds, String dest, String src) {
    ds.setBigDecimal(dest, ds.getBigDecimal(src));
  }
  
  public static void set(ReadWriteRow ds, String dest, ReadRow row) {
    ds.setBigDecimal(dest, row.getBigDecimal(dest));
  }
  
  public static void set(ReadWriteRow ds, String dest, ReadRow row, String src) {
    ds.setBigDecimal(dest, row.getBigDecimal(src));
  }
  
  public static void add(ReadWriteRow ds, String dest, String src) {
    add(ds, dest, ds.getBigDecimal(src));
  }
  
  public static void add(ReadWriteRow ds, String dest, BigDecimal num) {
    ds.setBigDecimal(dest, ds.getBigDecimal(dest).add(num));
  }
  
  public static void add(ReadWriteRow ds, String dest, ReadRow row) {
    add(ds, dest, row.getBigDecimal(dest));
  }
  
  public static void add(ReadWriteRow ds, String dest, ReadRow row, String src) {
    add(ds, dest, row.getBigDecimal(src));
  }
  
  public static void sub(ReadWriteRow ds, String dest, String src) {
    sub(ds, dest, ds.getBigDecimal(src));
  }
  
  public static void sub(ReadWriteRow ds, String dest, BigDecimal num) {
    ds.setBigDecimal(dest, ds.getBigDecimal(dest).subtract(num));
  }
  
  public static void sub(ReadWriteRow ds, String dest, ReadRow row) {
    sub(ds, dest, row.getBigDecimal(dest));
  }
  
  public static void sub(ReadWriteRow ds, String dest, ReadRow row, String src) {
    sub(ds, dest, row.getBigDecimal(src));
  }
  
  public static int comp(ReadRow srow, String src, ReadRow drow, String dest) {
    return srow.getBigDecimal(src).compareTo(drow.getBigDecimal(dest));
  }
  
  public static int comp(ReadRow srow, String src, ReadRow drow) {
    return srow.getBigDecimal(src).compareTo(drow.getBigDecimal(src));
  }
  
  public static int comp(ReadRow row, String src, String dest) {
    return row.getBigDecimal(src).compareTo(row.getBigDecimal(dest));
  }
  
  public static void addTo(ReadWriteRow ds, String dest, String[] src) {
    for (int i = 0; i < src.length; i++) add(ds, dest, src[i]);
  }
  
  public static void add(ReadWriteRow ds, String dest, String c1, String c2) {
    ds.setBigDecimal(dest, ds.getBigDecimal(c1).add(ds.getBigDecimal(c2)));
  }
  
  public static void sub(ReadWriteRow ds, String dest, String c1, String c2) {
    ds.setBigDecimal(dest, ds.getBigDecimal(c1).subtract(ds.getBigDecimal(c2)));
  }
  
  public static void mul(ReadWriteRow ds, String dest, String src) {
    mul(ds, dest, ds.getBigDecimal(src));
  }
  
  public static void mul(ReadWriteRow ds, String dest, String c1, String c2) {
    ds.setBigDecimal(dest, ds.getBigDecimal(c1).multiply(ds.getBigDecimal(c2)).
        setScale(ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void div(ReadWriteRow ds, String dest, String c1, String c2) {
    if (ds.getBigDecimal(c2).signum() == 0) ds.setBigDecimal(dest, Aus.zero0);
    else ds.setBigDecimal(dest, ds.getBigDecimal(c1).divide(ds.getBigDecimal(c2),
        ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void mul(ReadWriteRow ds, String dest, BigDecimal num) {
    ds.setBigDecimal(dest, ds.getBigDecimal(dest).multiply(num).
        setScale(ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void div(ReadWriteRow ds, String dest, BigDecimal num) {
    if (num.signum() == 0) ds.setBigDecimal(dest, Aus.zero0);
    else ds.setBigDecimal(dest, ds.getBigDecimal(dest).divide(num, 
        ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void mul(ReadWriteRow ds, String dest, ReadRow row, String src) {
    mul(ds, dest, row.getBigDecimal(src));
  }
  
  public static void neg(ReadWriteRow ds, String dest) {
    ds.setBigDecimal(dest, ds.getBigDecimal(dest).negate());
  }
  
  public static void neg(ReadWriteRow ds, String[] dest) {
    for (int i = 0; i < dest.length; i++) neg(ds, dest[i]);
  }
  
  public static void addSub(ReadWriteRow ds, String dest, ReadRow row, String src, BigDecimal num) {
    add(ds, dest, row.getBigDecimal(src).subtract(num));
  }
  
  public static void base(ReadWriteRow ds, String dest, String src, BigDecimal percent) {
    ds.setBigDecimal(dest, ds.getBigDecimal(src).divide(percent.movePointLeft(2).add(one0), 
        ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void percent(ReadWriteRow ds, String dest, String src, BigDecimal num) {
    if (num.signum() == 0) ds.setBigDecimal(dest, Aus.zero0);
    else ds.setBigDecimal(dest, ds.getBigDecimal(src).movePointRight(2).divide(num,
        ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void percent(ReadWriteRow ds, String dest, String src, String total) {
    if (ds.getBigDecimal(total).signum() == 0) ds.setBigDecimal(dest, Aus.zero0);
    else ds.setBigDecimal(dest, ds.getBigDecimal(src).movePointRight(2).divide(ds.getBigDecimal(total),
        ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void percentage(ReadWriteRow ds, String dest, BigDecimal num, String percent) {
    ds.setBigDecimal(dest, ds.getBigDecimal(percent).movePointLeft(2).multiply(num).
        setScale(ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void percentage(ReadWriteRow ds, String dest, String osn, BigDecimal percent) {
    ds.setBigDecimal(dest, percent.movePointLeft(2).multiply(ds.getBigDecimal(osn)).
        setScale(ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static void percentage(ReadWriteRow ds, String dest, String total, String percent) {
    ds.setBigDecimal(dest, ds.getBigDecimal(percent).movePointLeft(2).multiply(ds.getBigDecimal(total)).
        setScale(ds.getColumn(dest).getScale(), BigDecimal.ROUND_HALF_UP));
  }
  
  public static BigDecimal minus(ReadWriteRow ds, String c1, String c2) {
    return ds.getBigDecimal(c1).subtract(ds.getBigDecimal(c2));
  }
  
  public static BigDecimal plus(ReadWriteRow ds, String c1, String c2) {
    return ds.getBigDecimal(c1).add(ds.getBigDecimal(c2));
  }
  
  public static BigDecimal to0(BigDecimal num) {
    return num.setScale(0, BigDecimal.ROUND_HALF_UP);
  }
  
  public static BigDecimal to2(BigDecimal num) {
    return num.setScale(2, BigDecimal.ROUND_HALF_UP);
  }
  
  public static BigDecimal to3(BigDecimal num) {
    return num.setScale(3, BigDecimal.ROUND_HALF_UP);
  }
  
  public static BigDecimal to6(BigDecimal num) {
    return num.setScale(6, BigDecimal.ROUND_HALF_UP);
  }
    
  /**
   * vraæa true ako je string cijeli broji.
   */
  public static boolean isNumber(String snum) {
    try {
      return Integer.parseInt(snum) != 0.1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Pretvara String u int. Korisno ako se o\u010Dekuje pozitivan broj,
   * jer u slu\u010Daju bilo kakve greške, metoda vra\u0107a 0.
   * @param snum String reprezentacija broja.
   * @return 0 u slu\u010Daju greške, ina\u010De doti\u010Dni broj
   */
  public static int getNumber(String snum) {
    try {
      return Integer.parseInt(snum);
    } catch (Exception e) {
      return 0;
    }
  }
  
  /**
   * pretvara timestamp u lijepo formatirani string.
   */
  public static String formatTimestamp(Timestamp t) {
    return raDateUtil.getraDateUtil().dataFormatter(t);
  }

  /**
   * pretvara bigdecimal u lijepo formatirani string. Pazi na broj decimala.
   */
  public static String formatBigDecimal(BigDecimal num) {
    nf.setMinimumFractionDigits(num.scale());
    nf.setMaximumFractionDigits(num.scale());
    return nf.format(num.doubleValue());
  }
  
  public static String formatBigDecimal2(BigDecimal num) {
    try {
      nf.setMinimumFractionDigits(num.scale());
      nf.setMaximumFractionDigits(num.scale());
      nf.setGroupingUsed(false);
      return nf.format(num.doubleValue());
    } finally {
      nf.setGroupingUsed(true);
    }
  }
  
  /**
   * pretvara float u lijepo formatirani string sa dvije decimale.
   */  
  public static String formatFloat(float num) {
    nf.setMinimumFractionDigits(2);
    nf.setMaximumFractionDigits(2);
    return nf.format(num);
  }
  
  public static String formatDouble(double num) {
    nf.setMinimumFractionDigits(2);
    nf.setMaximumFractionDigits(2);
    return nf.format(num);
  }
  
  public static String formatBroj(ReadRow ds, String format) {
    Variant v = new Variant();
    VarStr br = new VarStr(format);
    int b, e;
    while ((b = br.indexOf('[')) >= 0 && (e = br.indexOf(']')) > b+1) {
      String rep = "";
      String part = br.mid(b + 1, e);
      String mod = null;
      int split = part.indexOf(':');
      if (split > 0) {
        mod = part.substring(split + 1);
        part = part.substring(0, split);
      }
      if (ds.hasColumn(part) != null) {
        ds.getVariant(part, v);
        rep = v.toString();
        if (mod != null && mod.startsWith("0")) 
          rep = new VarStr(rep).paddLeft(Aus.getNumber(mod) - rep.length(), '0').toString();
        else if (mod != null && Aus.getNumber(mod) > 0)
          rep = new VarStr(rep).leftJustify(Aus.getNumber(mod)).toString();
        else if (mod != null && Aus.getNumber(mod) < 0)
          rep = new VarStr(rep).rightJustify(-Aus.getNumber(mod)).toString();
      }
      br.replace(b, e + 1, rep);
    }
    return br.toString(); 
  }

  /**
   * pretvara string u bigdecimal. Pokusava i varijantu s decimalnom tockom i sa decimalnim zarezom.
   */
  public static BigDecimal getDecNumber(String snum) {
    try {
      VarStr v = new VarStr(snum);
      int comma = v.lastIndexOf(',');
      int dot = v.lastIndexOf('.');
      int cnum = v.countOccurences(',');
      int dnum = v.countOccurences('.');
      if (comma > dot && cnum <= 1 || dnum > 1) {
        v.remove('.');
        v.replace(',', '.');
      } else v.remove(',');
      return new BigDecimal(v.toString());
    } catch (Exception e) {
      return Aus.zero0;
    }
  }
  
  /**
   * Vraæa true ako string predstavlja decimalni broj formatiran po važeæem Localeu.
   */
  public static boolean isFormattedDecNumber(String snum) {
    try {
      nf.parse(snum);
      return true;
    } catch (ParseException ex) {
      return false;
    }
  }
  
  /**
   * Vraæa true ako string predstavlja decimalni broj formatiran po java standardu (dec. toèka)
   */
  public static boolean isDecNumber(String snum) {
    try {
      Double.parseDouble(snum);
      return true;
    } catch (NumberFormatException ex) {
      return false;
    }
  }

  /**
   * Pretvara string u integer. Nikad ne puca, i vraca sto god uspije.
   * Npr. getAnyNumber("45$dgdgf") vraca 45.
   * @param snum String koji se pretvara u broj.
   * @return sto uspije pretvoriti u broj.
   */
  public static int getAnyNumber(String snum) {
    int i = 0, n = 0;
    boolean signed = false;
    while (i < snum.length() && Character.isWhitespace(snum.charAt(i))) ++i;
    if (i == snum.length()) return 0;
    if (signed = (snum.charAt(i) == '-')) ++i;
    else if (snum.charAt(i) == '+') ++i;
    while (i < snum.length() && Character.isDigit(snum.charAt(i)))
      n = 10 * n + (snum.charAt(i++) - '0');
    return signed ? -n : n;
  }

  public static boolean checkDateRange(JraTextField begField, JraTextField endField) {
    return checkDateRange(begField, endField, false);
  }
  /**
   * Provjerava ispravnost datumskog raspona. U slu\u010Daju greške baca dialog i
   * postavlja fokus na textfield s neispravnim unosom. Ova metoda ne dopušta raspone
   * iza današnjeg datuma.<p>
   * @param begField Textfield po\u010Detnog datuma raspona.
   * @param endField Textfield krajnjeg datuma raspona.
   * @return true ako je raspon u redu, false ako nije.
   */
  public static boolean checkDateRange(JraTextField begField, JraTextField endField, boolean checkToday) {
    Timestamp beginDate = begField.getDataSet().getTimestamp(begField.getColumnName());
    Timestamp endDate = endField.getDataSet().getTimestamp(endField.getColumnName());
    Timestamp lastValid = checkToday ? Valid.getValid().getToday() : 
                          Util.getUtil().getLastDayOfYear(); 
    if (beginDate.after(Util.getUtil().getLastSecondOfDay(lastValid))) {
      begField.requestFocus();
      JOptionPane.showMessageDialog(begField.getTopLevelAncestor(),
        checkToday ? "Poèetni datum je iza današnjeg!" :
          "Poèetni datum je iza kraja godine!",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (endDate.after(Util.getUtil().getLastSecondOfDay(lastValid))) {
      endField.requestFocus();
      JOptionPane.showMessageDialog(endField.getTopLevelAncestor(),
        checkToday ? "Završni datum je iza današnjeg!" 
            : "Završni datum iza kraja godine!",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (endDate.before(Util.getUtil().getFirstSecondOfDay(beginDate))) {
      begField.requestFocus();
      JOptionPane.showMessageDialog(begField.getTopLevelAncestor(),
        "Poèetni datum je iza završnog!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!checkSanityRange(begField)) return false;
    
    return true;
  }
  
  /** 
   * Provjerava ispravnos odnosa datuma dokumenta i datuma dospjeæa. U sluèaju greške
   * fokusira textfield s neispravnim unosom. Maksimalni dani dospjeæa su parametrizirani:
   * zapod.maxDosp. 
   */
  public static boolean checkDatAndDosp(JraTextField datdokField, JraTextField datdospField) {
    if (!checkSanityRange(datdokField)) return false;
    Timestamp datdok = datdokField.getDataSet().getTimestamp(datdokField.getColumnName());
    Timestamp datdosp = datdospField.getDataSet().getTimestamp(datdospField.getColumnName());
    
    if (datdosp.before(Util.getUtil().getFirstSecondOfDay(datdok))) {
      datdospField.requestFocus();
      JOptionPane.showMessageDialog(datdospField.getTopLevelAncestor(),
        "Datum dospjeæa je ispred datuma dokumenta!",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    int ddosp = getNumber(frmParam.getParam("zapod", "maxDosp", "120",
        "Maksimalni dopušteni dani dospjeæa"));
    if (ddosp <= 0) ddosp = 120;
    if (Util.getUtil().getHourDifference(datdok, datdosp) > ddosp * 24) {
      datdospField.requestFocus();
      JOptionPane.showMessageDialog(datdospField.getTopLevelAncestor(),
        "Prekoraèeni maksimalni dani dospjeæa!",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  /**
   * Opæa metoda za provjeru smisla odreðenog datuma. Ne dopušta datume iza kraja tekuæe
   * godine, niti one predaleko u prošlosti.
   */
  public static boolean checkSanityRange(JraTextField datField) {
    Timestamp date = datField.getDataSet().getTimestamp(datField.getColumnName());
    Timestamp lastValid = Util.getUtil().getLastDayOfYear(Valid.getValid().getToday());
    Timestamp firstValid = Util.getUtil().addYears(lastValid, -25);
    
    if (date.after(Util.getUtil().getLastSecondOfDay(lastValid))) {
      datField.requestFocus();
      JOptionPane.showMessageDialog(datField.getTopLevelAncestor(),
        "Pogrešan datum! (Iza kraja tekuæe godine)",
        "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (date.before(firstValid)) {
      datField.requestFocus();
      JOptionPane.showMessageDialog(datField.getTopLevelAncestor(),
          "Pogrešan datum! (Predaleko u prošlosti)",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  /** 
   * Metoda za provjeru datumskih raspona u GK. Ne dopušta raspone koji se protežu
   * preko datuma poèetnog stanja (godišnje obrade), radi onemoguæavanja duplog
   * prometa.
   */
  public static boolean checkGKDateRange(JraTextField begField, JraTextField endField) {
    System.err.println("chk GK range");
    if (!checkSanityRange(begField)) return false;
    if (!checkSanityRange(endField)) return false;
    Timestamp begDate = begField.getDataSet().getTimestamp(begField.getColumnName());
    Timestamp endDate = endField.getDataSet().getTimestamp(endField.getColumnName());
    if (endDate.before(Util.getUtil().getFirstSecondOfDay(begDate))) {
      begField.requestFocus();
      JOptionPane.showMessageDialog(begField.getTopLevelAncestor(),
        "Poèetni datum je iza završnog!",
        "Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    DataSet ds = Knjigod.getDataModule().getTempSet(
        getCorgCond().and(Condition.equal("APP", "gk")));
    ds.open();
    if (ds.rowCount() > 0) {
      for (ds.first(); ds.inBounds(); ds.next()) {
        int god = getNumber(ds.getString("GOD"));
        if (getNumber(Valid.getValid().findYear(begDate)) < god &&
            getNumber(Valid.getValid().findYear(endDate)) >= god) {
          begField.requestFocus();
          JOptionPane.showMessageDialog(begField.getTopLevelAncestor(),
            "Poèetni i završni datum nisu u istoj knjigovodstvenoj godini!",
            "Greška",JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    }
    return true;
  }
  
  public static boolean checkGKDate(JraTextField knjField) {
    System.err.println("chk GK date");
    if (!checkSanityRange(knjField)) return false;
    Timestamp knjDate = knjField.getDataSet().getTimestamp(knjField.getColumnName());
    DataSet ds = Knjigod.getDataModule().getTempSet(
        getCorgCond().and(Condition.equal("APP", "gk")));
    ds.open();
    if (ds.rowCount() > 0) {
      for (ds.first(); ds.inBounds(); ds.next()) {
        int god = getNumber(ds.getString("GOD"));
        if (getNumber(Valid.getValid().findYear(knjDate)) < god) {
          knjField.requestFocus();
          if (JOptionPane.showConfirmDialog(knjField.getTopLevelAncestor(),
              "Datum knjiženja nije u tekuæoj knjigovodstvenoj godini! Nastaviti?", "Pogrešna godina",
              JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return false;
          return true;
        }
      }
    }
    return true;
  }
  
  /**
   * <p>Metoda koja daje Condition koji zahtjeva da datum dokumenta
   * bude manji ili jednak zadanom, a datum knjizenja unutar knjigovodstvene
   * godine u kojoj se zadani datum nalazi.</p>
   * <p>Imena polja se podrazumijevaju kao DATUMKNJ i DATDOK.</p>
   * @param upto datum do kojeg se podaci iz odgovarajuce knjigovodstvene godine uzima.
   * @return trazeni Condition.
   */
  public static Condition getCurrGKDatumCond(Timestamp upto) {
    return getCurrGKDatumCond("DATUMKNJ", "DATDOK", upto);
  }
  
  public static Condition getCurrGKDatumCond(String datfield, Timestamp upto) {
    return getCurrGKDatumCond("DATUMKNJ", datfield, upto);
  }
  
  /**
   * <p>Metoda koja daje Condition koji zahtjeva da datum dokumenta
   * bude manji ili jednak zadanom, a datum knjizenja unutar knjigovodstvene
   * godine u kojoj se zadani datum nalazi.</p>
   * @param knjfield Ime polja datuma knjizenja.
   * @param datfield Ime polja datuma dokumenta.
   * @param upto datum do kojeg se podaci iz odgovarajuce knjigovodstvene godine uzima.
   * @return trazeni Condition.
   */
  public static Condition getCurrGKDatumCond(String knjfield, String datfield, Timestamp upto) {
    // Nadji condition za datum dokumenta
    Condition dat = Condition.where(datfield, Condition.TILL, 
        Util.getUtil().getLastSecondOfDay(upto));

    // nadji sve godisnje obrade napravljene s kljucem gk.
    DataSet ds = Knjigod.getDataModule().getTempSet(Condition.equal("CORG",
        OrgStr.getKNJCORG(false)).and(Condition.equal("APP", "gk")));
    ds.open();
    
    // ako nema nijednog sloga, onda vrati condition samo sa datumom dokumenta.
    if (ds.rowCount() == 0) return dat;

    // pronadji u popisu prvu prethodnu i prvu sljedecu godinu za koju je
    // napravljena godisnja obrada, u odnosu na trazeni datum.
    String thisYear = Valid.getValid().findYear(upto);
    String firstYear = null, lastYear = null;
    for (ds.first(); ds.inBounds(); ds.next()) {
      String knjYear = ds.getString("GOD");
      if (knjYear.compareTo(thisYear) <= 0 &&
         (firstYear == null || firstYear.compareTo(knjYear) < 0))
            firstYear = knjYear;
      if (knjYear.compareTo(thisYear) > 0 &&
         (lastYear == null || lastYear.compareTo(knjYear) > 0))
            lastYear = knjYear;
    }
    // ako nema godisnje obrade iza trazenog datuma, onda treba
    // iskljuciti dokumente knjizene prije zadnje obrade.
    if (lastYear == null)
      return dat.and(Condition.where(knjfield, Condition.FROM,
          Util.getUtil().getYearBegin(firstYear)));
    
    // ako nema godisnje obrade prije trazenog datuma, onda treba 
    // iskljuciti dokumente knjizene nakon prve sljedece obrade.
    lastYear = Integer.toString(getNumber(lastYear) - 1);
    if (firstYear == null)
      return dat.and(Condition.where(knjfield, Condition.TILL,
          Util.getUtil().getYearEnd(lastYear)));
    
    // postoje godisnje obrade i prije i poslije trazenog datuma.
    return dat.and(Condition.between(knjfield, 
        Util.getUtil().getYearBegin(firstYear), Util.getUtil().getYearEnd(lastYear)));
  }

  public static Timestamp getGkYear(Timestamp upto) {
    DataSet ds = Knjigod.getDataModule().getTempSet(Condition.equal("CORG",
        OrgStr.getKNJCORG(false)).and(Condition.equal("APP", "gk")));
    ds.open();

    if (ds.rowCount() == 0) return Util.getUtil().getFirstDayOfYear(upto);
    
    String thisYear = Valid.getValid().findYear(upto);
    String firstYear = null;
    System.out.println(thisYear);
    for (ds.first(); ds.inBounds(); ds.next()) {
      String knjYear = ds.getString("GOD");
      if (knjYear.compareTo(thisYear) <= 0 &&
         (firstYear == null || firstYear.compareTo(knjYear) < 0))
            firstYear = knjYear;
    }
    
    if (firstYear != null) 
      return Util.getUtil().getYearBegin(firstYear); 
      
    return Util.getUtil().getFirstDayOfYear(upto); 
  }
  /** 
   * Metoda daje Condition za vrstu dokumenata u SK, ovisno o tome radi li se
   * o kupcima ili dobavljaèima.
   */
  public static Condition getVrdokCond(boolean kup) {
    return kup ? Condition.in("VRDOK", new String[] {"IRN", "UPL", "OKK"}) :
        Condition.in("VRDOK", new String[] {"URN", "IPL", "OKD"});
  }
  
  public static Condition getVrdokCond(boolean kup, boolean rac) {
    if (kup && rac) return Condition.equal("VRDOK", "IRN").
        or(Condition.equal("VRDOK", "OKK").and(Condition.where("ID", Condition.NOT_EQUAL, 0)));
    if (kup && !rac) return Condition.equal("VRDOK", "UPL").
        or(Condition.equal("VRDOK", "OKK").and(Condition.where("IP", Condition.NOT_EQUAL, 0)));
    if (!kup && rac) return Condition.equal("VRDOK", "URN").
      or(Condition.equal("VRDOK", "OKD").and(Condition.where("IP", Condition.NOT_EQUAL, 0)));
    if (!kup && !rac) return Condition.equal("VRDOK", "IPL").
      or(Condition.equal("VRDOK", "OKD").and(Condition.where("ID", Condition.NOT_EQUAL, 0)));
    return Condition.none;
  }

  /** 
   * Metoda daje Condition za KNJIG=trenutaèno odabrano knjigovodstvo.
   */
  public static Condition getKnjigCond() {
    return Condition.equal("KNJIG", OrgStr.getKNJCORG(false));
  }

  /** 
   * Metoda daje Condition za CORG=trenutaèno odabrano knjigovodstvo.
   */
  public static Condition getCorgCond() {
    return Condition.equal("CORG", OrgStr.getKNJCORG(false));
  }

  public static Condition getCorgInCond(String corg) {
  	Condition c = OrgStr.getCorgsCond(corg);
  	if (c != Condition.nil) return c;
  	return Condition.ident;
  }
  
  public static Condition getCorgInKnjigCorg() {
    return getCorgInCond(OrgStr.getKNJCORG(false));
  }
    
  /**
   * Metoda daje condition za datumsko polje 'col' unutar perioda godine 'year'.
   */
  public static Condition getYearCond(String col, String year) {
    return Condition.between(col, Util.getUtil().getYearBegin(year),
                             Util.getUtil().getYearEnd(year));
  }
  
  /**
   * Join condition dvije tablice po kolonama 'cols' (split)
   */
  public static String join(String tab1, String tab2, String cols) {
  	return join(tab1, tab2, new VarStr(cols).split());
  }
  
  /**
   * Join condition dvije tablice po kolonama cols
   */
  public static String join(String tab1, String tab2, String[] cols) {
  	return join(tab1, cols, tab2, cols);
  }
  
  /**
   * Join condition dvije tablice po kolonama cols1 i cols2, respektivno.
   */
  public static String join(String tab1, String[] cols1, String tab2, String[] cols2) {
  	VarStr v = new VarStr().append(' ');
  	for (int i = 0; i < cols1.length; i++) 
  		v.append(tab1).append('.').append(cols1[i]).append(" = ").append(tab2).append('.').append(cols2[i]).append(" AND ");
  	
  	return v.chop(4).toString();
  }
  
  /**
   * Metoda daje najraniju knjigovodstvenu godinu u kojoj je dopuštena izmjena podataka.
   */
  public static String getFreeYear() {
    String god = Valid.getValid().getLastKnjigYear("gk");
    if (god == null) god = "1970";
    return god;
  }
  
  /**
   * Metoda daje Condition za DATUMKNJ od kojeg (na dalje) je dopušteno knjiženje.
   */
  public static Condition getFreeYearCond() {
    return Condition.from("DATUMKNJ", Util.getUtil().getYearBegin(getFreeYear()));
  }

  /**
   * Provjerava je li godina prestupna.
   */
  public static boolean isYearExt(Timestamp date) {
    int year = getNumber(Util.getUtil().getYear(date));
    return ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)));
  }

  /**
   * Vraca string od 'n' praznih znakova.
   */
  public static String spc(int n) {
    char[] spaces = new char[n];
    for (int i = 0; i < n; i++) spaces[i] = ' ';
    return new String(spaces);
  }
  
  public static String rjust(String str, int len) {
    if (str.length() == len) return str;
    if (str.length() > len) return str.substring(0, len);
    return spc(len - str.length()) + str;
  }
  
  public static String ljust(String str, int len) {
    if (str.length() == len) return str;
    if (str.length() > len) return str.substring(0, len);
    return str + spc(len - str.length());
  }

  /**
   * Vraæa string of 'n' znakova 'ch'.
   */
  public static String string(int n, char ch) {
    char[] chars = new char[n];
    for (int i = 0; i < n; i++) chars[i] = ch;
    return new String(chars);
  }
  
  public static String leadzero(int num, int len) {
    String ns = Integer.toString(num);
    if (ns.length() >= len) return ns;
    return string(len - ns.length(), '0') + ns;
  }

  /**
   * Vraæa gramatièki ispravnu varijantu množine nekog izraza u ovisnosti od broja.
   * Npr. getNumDep(num, "slog", "sloga", "slogova") vraæa ispravan gramatièki oblik
   * rijeèi slog u ovisnosti od broja 'num'. Oblika ima najviše tri, a najlakše ih
   * je pamtiti po brojevima 1, 2, 5. Jedan 'slog', dva 'sloga', pet 'slogova'.
   * Jedna lopta, dvije lopte, pet lopti.
   */
  public static String getNumDep(int num, String t1, String t2, String t5) {
    int j = num % 10, d = (num / 10) % 10;
    if (j == 1 && d != 1) return t1;
    if (j > 1 && j < 5 && d != 1) return t2;
    return t5;
  }

  /**
   *  Kao i getNumDep ali odmah dodaje broj 'num' ispred odgovarajuæeg stringa t1, t2, t5. 
   */
  public static String getNum(int num, String t1, String t2, String t5) {
    return num + " " + getNumDep(num, t1, t2, t5);
  }

  /**
   * Kratica za getNum(num, "slog", "sloga", "slogova").
   */
  public static String getSlogova(int num) {
    return getNum(num, "slog", "sloga", "slogova");
  }

  /**
   * Kratica za getNum(num, "red", "reda", "redova").
   */
  public static String getRedova(int num) {
    return getNum(num, "red", "reda", "redova");
  }

  /**
   * Ispisuje ime nekog gettera i vrijednost koju taj getter vraæa na objektu 'o'.
   * Za debugging. 
   */
  public static void dumpGetter(Object o, String name) {
    try {
      Method getter = o.getClass().getDeclaredMethod(name, null);
      getter.setAccessible(true);
      System.out.print(o.getClass().getName());
      System.out.print(".");
      System.out.print(name);
      System.out.print("() = ");
      System.out.println(getter.invoke(o, null));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Ispisuje imena varijabli i njenih trenutaènih vrijednosti u objektu o.
   * Za debugging.
   */
  public static void dumpVars(Object o) {
    try {
      Field[] fields = o.getClass().getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        fields[i].setAccessible(true);
        System.out.println(fields[i].getName() + "=" + fields[i].get(o));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Ispisuje ime varijable i njenu trenutaènu vrijednost u objektu o.
   * Za debugging.
   */
  public static void dumpVar(Object o, String name) {
    try {
      Field var = o.getClass().getDeclaredField(name);
      var.setAccessible(true);
      System.out.print(o.getClass().getName());
      System.out.print(".");
      System.out.print(name);
      System.out.print(" = ");
      System.out.println(var.get(o));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Ispisuje toènu klasu nekog objekta i imena klasa koje nasljeðuje, sve do prve
   * klase iz java paketa. Za debugging.
   */
  public static void dumpClassName(Object o) {
    Class c = o.getClass();
    System.out.print(c.getName());
    while (!c.getName().startsWith("java") && c.getSuperclass() != null) {
      c = c.getSuperclass();
      System.out.print(": " + c.getName());
    }
    if (o instanceof JComponent)
      System.out.println(" = " + o + "  prefSize = " + ((JComponent) o).getPreferredSize());
    else System.out.println(" = " + o);
  }
  
  public static void dumpColumns(ReadRow ds) {
    dumpClassName(ds);
    if (ds instanceof QueryDataSet) {
      QueryDataSet qds = (QueryDataSet) ds;
      System.out.println("Dataset: " + qds.getQuery().getQueryString());
    }
    for (int i = 0; i < ds.getColumnCount(); i++)
      System.out.println(ConsoleCreator.getExtraData(ds.getColumn(i)));
  }

  /**
   * Ispisuje cijelo stablo sadržaja nekog Containera.
   * Za debugging.
   */
  public static void dumpContainer(Container c, int tabs) {
    System.out.print(spc(tabs));
    dumpClassName(c);
    for (int i = 0; i < c.getComponentCount(); i++) {
      if (c.getComponent(i) instanceof Container)
        dumpContainer((Container) c.getComponent(i), tabs + 3);
      else {
        System.out.print(spc(tabs + 3));
        dumpClassName(c.getComponent(i));
      }
    }
  }

  /**
   * Ispisuje stablo Elixir modela. Za debugging.
   */
  public static void dumpModel(IModel m, int tabs) {
    Enumeration prop = m.getPropertyNames();
    Enumeration hprop = m.getHiddenPropertyNames();
    Enumeration models = m.getModels();
    System.out.println(spc(tabs)+"MODEL: "+m.getPropertyValue("Name"));
    while (prop.hasMoreElements()) {
      String name = (String) prop.nextElement();
      System.out.println(spc(tabs + 3) + name + ": " + m.getPropertyValue(name));
    }
    while (hprop.hasMoreElements()) {
      String name = (String) hprop.nextElement();
      System.out.println(spc(tabs + 3) + "*" + name + ": " + m.getHiddenPropertyValue(name));
    }
    while (models.hasMoreElements())
      dumpModel((IModel) models.nextElement(), tabs + 3);
  }

  public static double getFontHeightRatio(String font1, String font2) {
    try {
      Graphics scr = GraphicsEnvironment.getLocalGraphicsEnvironment().
        createGraphics(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
//      System.out.println(scr);
      return (double) scr.getFontMetrics(Font.decode(font1)).getHeight() /
         scr.getFontMetrics(Font.decode(font2)).getHeight();
    } catch (Exception e) {
      e.printStackTrace();
      return 1.0;
    }
  }

  private static void removeSwingKey(InputMap im, KeyStroke k) {
    while (im != null && im.get(k) != null) {
      im.remove(k);
      im = im.getParent();
    }
  }

  /**
   * Mièe bindinge za neku tipku iz Swing komponente. (Ako smeta, poput F6 na JSplitPane)
   */
  public static void removeSwingKey(JComponent c, int key) {
    KeyStroke k = KeyStroke.getKeyStroke(key, 0);
    removeSwingKey(c.getInputMap(c.WHEN_FOCUSED), k);
    removeSwingKey(c.getInputMap(c.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT), k);
    removeSwingKey(c.getInputMap(c.WHEN_IN_FOCUSED_WINDOW), k);
  }

  /**
   * Mièe bindinge za neku tipku rekurzivno iz cijelog Containera.
   */
  public static void removeSwingKeyRecursive(Container c, int key) {
    try {
      for (int i = 0; i < c.getComponentCount(); i++) {
        if (c.getComponent(i) instanceof JComponent)
          removeSwingKey((JComponent) c.getComponent(i), key);
        if (c.getComponent(i) instanceof Container)
          removeSwingKeyRecursive((Container) c.getComponent(i), key);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Expanda dataset s internom hijerarhijom (tablice gdje jedno polje oznaèava 
   * hijerarhijsku pripadnost nekom drugom slogu u istoj tablici, tipa OrgStr).
   * @param val Vrijednost kljuèa èija se hijerarhija razvija.
   * @param data Dataset s kompletnom hijerarhijom.
   * @param keyCol Ime kolone kljuèa
   * @param linkCol Ime kolone hijerarhijske pripadnosti kljuèu.
   * @return razvijeni dataset, depth-first oblik.
   */
  public static DataSet getDataBranch(String val, DataSet data, String keyCol, String linkCol) {
  	return DataTree.getSubSet(val, data, keyCol, linkCol);
  }
  
  /**
   * Vraæa SQL izraz s popisom svih kljuèeva tablice koji hijerarhijski pripadaju slogu
   * s kljuèem 'val'. Vidi getDataBranch(). 
   */
  public static Condition getDataTreeList(String val, DataSet data, String keyCol, String linkCol) {
  	return DataTree.getSubCond(val, data, keyCol, linkCol);
  }

  public static File[] getClassPath() {
    StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"),
        System.getProperty("path.separator"));
    HashSet cps = new HashSet();
    int i = 0;
    while (st.hasMoreTokens()) {
      String tok = st.nextToken();
      File f = new File(tok);
      cps.add(f.isDirectory() ? f : f.getParentFile());
    }
    return (File[]) cps.toArray(new File[cps.size()]);
  }

  public static File getCurrentDirectory() {
    return new File(System.getProperty("user.dir"));
  }

  public static File getHomeDirectory() {
    return new File(System.getProperty("user.home"));
  }

  public static File findFileAnywhere(String name) {
    File f = new File(getCurrentDirectory(), name);
    if (f.exists()) return f;
    f = new File(getHomeDirectory(), name);
    if (f.exists()) return f;
    File[] cp = getClassPath();
    for (int i = 0; i < cp.length; i++)
      if (cp[i] != null && cp[i].exists()) {
        f = new File(cp[i], name);
        if (f.exists()) return f;
      }
    return null;
  }

  public static double heuristicCompare(String s1, String s2) {
    return heuristicCompare(s1, s2, null);
  }

  /**
   * Metoda koja heuristicki usporedjuje slicnost dva stringa (case-insensitive).
   * Stringovi su to slicniji sto vise ima znakova koji postoje u oba stringa,
   * te sto vise ima sto duzih podnizova u oba stringa. Vraca vrijednost izmedju
   * 0 i 1, 0 znaci da stringovi uopce nemaju istih znakova, a 1 znaci da su
   * identicni (case-insensitive).<p>
   * @param s1 prvi string.
   * @param s2 drugi string.
   * @param ignore popis znakova koji se kompletno ignoriraju u usporedbi
   * @return heuristicku vrijednost slicnosti, izmedju 0 i 1.
   */
  public static double heuristicCompare(String s1, String s2, String ignore) {
    // pretvori u lowercase. Ako vec trazimo heuristiku, velicina
    // slova vjerojatno nije bitna.
    VarStr v1 = new VarStr(s1.toLowerCase()).removeChars(ignore);
    VarStr v2 = new VarStr(s2.toLowerCase()).removeChars(ignore);

    // sortiraj nizove po duljini. Prvi dulji, drugi kraci.
    if (v1.length() < v2.length()) {
      VarStr t = v1;
      v1 = v2;
      v2 = t;
    }
    // duljine stringova
    int l1 = v1.length(), l2 = v2.length();

    if (l1 == 0) return 1;
    if (l2 == 0) return 0;

    // normirana srednja duzina: potrebno zbog toga da usporedba kratkih
    // nizova koji se dosta ralikuju u duzini (recimo, niz od 4 i niz od 8 znakova)
    // ne budu previse razliciti samo zbog duzine.
    double nl = (l2 * Math.sqrt((double) l1 / l2) + l1) / (1 + Math.sqrt((double) l1 / l2));
//    double nl = (l1 + l2) / 2.;

    // broj istih znakova u oba stringa
    int neq = 0;
    VarStr v1c = new VarStr(v1), v2c = new VarStr(v2);
    while (v2c.length() > 0)
      neq += Math.min(v1c.countRemove(v2c.charAt(0)), v2c.countRemove(v2c.charAt(0)));
    double simChars = neq / nl;
    if (l2 == 1 || neq <= 1) return simChars;

    // pokrice duzeg niza kracim, pocevsi od podniza najvece duzine, do podniza od 2 znaka.
    // najprije generiraj tablicu koeficijenata za pokrivanje podnizom neke duljine.
    double[] koef = new double[l2];
    double factor = 0.8 / (Math.sqrt(l2) - 1);
    for (int i = 0; i < l2; i++)
      koef[i] = 1 - factor * (Math.sqrt((double) l2 / (i + 1)) - 1);

    v1c.clear().append(v1);
    v2c.clear().append(v2);
    double simSubstr = 0;
    int sublength = l2, p, totalsub = 0;
    while (sublength >= 2 && v2c.length() >= 2) {
      for (int i = 0; i <= v2c.length() - sublength; i++)
        if ((p = v1c.indexOf(v2c, i, sublength)) >= 0) {
          totalsub += sublength;
          simSubstr += koef[sublength - 1] * sublength / nl;
          v2c.delete(i, i + sublength);
          v1c.delete(p, p + sublength);
        }
      while (--sublength > v2c.length());
    }
    return simChars * 0.2 + simSubstr * 0.8;
  }
  
  public static void installComboPopupHideKey(final JComboBox combo) {
    AWTKeyboard.registerKeyStroke(combo, AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        if (!combo.isPopupVisible()) return false;
        combo.hidePopup();
        return true;        
      }
    });
  }
  
  public static void installEnterRelease(final Component comp) {
    AWTKeyboard.registerKeyStroke(comp, AWTKeyboard.ENTER_RELEASED, new KeyAction() {
      public boolean actionPerformed() {
        if (!comp.isShowing()) return false;
        FocusManager.getCurrentManager().focusNextComponent(comp);
        return true;        
      }
    });
  }
  
  
  /** 
   * Postavlja sql filter na dataset. Usput mièe memorijski filter, ako ga ima, te postavlja locale.
   */
  public static void setFilter(QueryDataSet ds, String filter) {
  	ds.close();
  	ds.closeStatement();
    RowFilterListener filt = ds.getRowFilterListener();
    if (filt != null) ds.removeRowFilterListener(filt);
    if (ds.getLocale() == null) ds.setLocale(hr);
  	ds.setQuery(new QueryDescriptor(ds.getDatabase() == null ? dM.getDataModule().getDatabase1() :
  		ds.getDatabase(), filter, null, true, Load.ALL));
  	removeRowID(ds);
  }
  
  public static void setupColumn(Column col, Column src) {
    col.setCaption(src.getCaption());
    col.setWidth(src.getWidth());
    col.setDisplayMask(src.getDisplayMask());
  }
  
  public static void setupColumn(Column col, KreirDrop src) {
    setupColumn(col, src.getColumn(col.getColumnName()));
  }
  
  
  /**
   * Postavlja filter na dataset i otvara ga.
   */
  public static void refilter(QueryDataSet ds, String filter) {
  	setFilter(ds, filter);
  	ds.open();
  }
  
  public static void removeRowID(StorageDataSet ds) {
  	//ds.setMetaDataUpdate(MetaDataUpdate.NONE);
  }
  
  /**
   * Kreira timestamp iz godine, mjeseca i dana.
   */
  public static Timestamp createTimestamp(int year, int month, int day) {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, year);
    cal.set(cal.MONTH, month - 1);
    cal.set(cal.DAY_OF_MONTH, day);
    return Util.getUtil().getFirstSecondOfDay(new Timestamp(cal.getTime().getTime()));
  }
  
  public static StorageDataSet createSet(String def) {
  	StorageDataSet ds = new StorageDataSet();
  	VarStr buf = new VarStr(def).trim();
  	ArrayList labs = new ArrayList();
  	int b = 0, e;
  	while ((b = buf.indexOf('{', b)) >= 0) {
  		e = buf.indexOf('}', b);
  		labs.add(buf.mid(b + 1, e));
  		buf.replace(b + 1, e, "");
  		b = b + 2;
  	}
  	
  	String[] cold = buf.indexOf(',') > 0 ? buf.splitTrimmed(',') : buf.split();
  	Column[] cols = new Column[cold.length];
  	String module = null;
  	int l = 0, p;
  	for (int i = 0; i < cold.length; i++) {
  		String lab = null;
  		if (cold[i].startsWith("{}")) {
  			lab = (String) labs.get(l++);
  			cold[i] = cold[i].substring(2);
  		}
  		if ((p = cold[i].indexOf('.')) >= 0) {
  			String left = cold[i].substring(0, p);
  			String rest = cold[i].substring(p + 1);
  			if (!isDigit(rest)) {
  				if (p > 0) module = left;
  				cols[i] = KreirDrop.getModuleByName(module).getColumn(rest).cloneColumn();
  				cols[i].setRowId(false);
  			} else 
  				cols[i] = dM.createBigDecimalColumn(left, lab == null ? left : lab, getNumber(rest));
  		} else if ((p = cold[i].indexOf(':')) > 0) {
  			String name = cold[i].substring(0, p);
  			if (lab == null) lab = name;
  			int d = cold[i].indexOf('=');
  			if (d < 0) cols[i] = dM.createStringColumn(name, lab, getNumber(cold[i].substring(p + 1)));
  			else cols[i] = dM.createStringColumn(name, lab, cold[i].substring(d + 1), getNumber(cold[i].substring(p + 1, d)));
  		} else if (cold[i].startsWith("@"))
  			cols[i] = dM.createTimestampColumn(cold[i].substring(1), lab != null ? lab : cold[i].substring(1));
  		else cols[i] = dM.createIntColumn(cold[i]);
  	}
  	
  	ds.setColumns(cols);
  	ds.open();
  	return ds;
  }
  
  public static raFieldMask installNumberMask(JTextField tf, int decs) {
    if ("calc".equalsIgnoreCase(frmParam.getParam("sisfun", "numberMask", 
        "calc", "Vrsta numerièke maske (calc/old)", true)))
      return new raCalculatorMask(tf, decs);
    return new raInhumanNumberMask(tf, decs);
  }

  /**
   * Centrira window.
   */
  public static void centerWindow(Window w) {
    Dimension screenSize = start.getSCREENSIZE();
    Dimension windowSize = w.getSize();
    if (windowSize.height > screenSize.height)
      windowSize.height = screenSize.height;
    if (windowSize.width > screenSize.width)
      windowSize.width = screenSize.width;
    
    w.setLocation((screenSize.width - windowSize.width) / 2, 
        (screenSize.height - windowSize.height) / 2);
  }

  /**
   * Zbraja sve BigDecimal vrijednosti kolone 'col' u datasetu 'ds'.
   */
  public static BigDecimal sum(String col, DataSet ds) {
    BigDecimal s = new BigDecimal(0);
    Variant v = new Variant();
    for (int i = 0; i < ds.rowCount(); i++) {
      ds.getVariant(col, i, v);
      s = s.add(v.getAsBigDecimal());
    }
    return s;
  }

  /**
   * Zbraja BigDecimal vrijednosti kolone 'col' u datasetu 'ds', za one redove
   * gdje je vrijednost kolone 'key' jednaka 'val'.
   */
  public static BigDecimal sum(String col, DataSet ds, String key, Object val) {
    BigDecimal s = new BigDecimal(0);
    Variant v = new Variant();
    for (int i = 0; i < ds.rowCount(); i++) {
      ds.getVariant(key, i, v);
      if (v.getAsObject().equals(val)) {
        ds.getVariant(col, i, v);
        s = s.add(v.getAsBigDecimal());
      }
    }
    return s;
  }
  
  public static Variant max(DataSet ds, String col) {
  	Variant v = new Variant();
  	Variant max = new Variant();
  	
  	ds.getVariant(col, max);
  	
  	for (int i = 0; i < ds.rowCount(); i++) {
  		ds.getVariant(col, i, v);
  		if (v.compareTo(max) > 0)
  			ds.getVariant(col, i, max);
  	}
  	return max;
  }
  
  public static Variant min(DataSet ds, String col) {
  	Variant v = new Variant();
  	Variant min = new Variant();
  	
  	ds.getVariant(col, min);
  	
  	for (int i = 0; i < ds.rowCount(); i++) {
  		ds.getVariant(col, i, v);
  		if (v.compareTo(min) < 0)
  			ds.getVariant(col, i, min);
  	}
  	return min;
  }
  
  public static StorageDataSet q(KreirDrop kd, String cols) {
  	return q(kd.getScopedSet(cols), "SELECT " + cols + " FROM " + kd.Naziv);
  }
  
  public static StorageDataSet qq(KreirDrop kd, String cols) {
  	return qq(kd.getScopedSet(cols), "SELECT " + cols + " FROM " + kd.Naziv);
  }
  
  public static StorageDataSet q(StorageDataSet ds, String query) {
  	Util.fillReadonlyData(ds, query);
  	return ds;
  }
  
  public static StorageDataSet qq(StorageDataSet ds, String query) {
  	Util.fillAsyncData(ds, query);
  	return ds;
  }
  
  public static QueryDataSet q(String query) {
    System.out.println("query: " + query);
    return Util.getNewQueryDataSet(query, true);
  }
  
  public static QueryDataSet qq(String query) {
    QueryDataSet retSet = new com.borland.dx.sql.dataset.QueryDataSet();

    retSet.setLocale(Aus.hr);
    
    retSet.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(),query));
    
    Refresher.postpone();
    
    retSet.setMetaDataUpdate(retSet.getMetaDataUpdate() & ~MetaDataUpdate.ROWID);

    retSet.open();

    return retSet;
  }
  
  /**
   * Prebacuje cijelu kolonu nekog dataseta u odgovarajuæi array.
   * @param ds DataSet èija kolona se traži.
   * @param col Ime kolone u datasetu.
   * @return dinamièki kreiran array, vraæen kao objekt.
   */
  public static Object toArray(DataSet ds, String col) {
    int dt = ds.getColumn(col).getDataType();
    Class c = null;
    for (int i = 0; i < varTypes.length; i++)
      if (varTypes[i] == dt) c = classTypes[i];
    if (c == null)
      throw new RuntimeException("Invalid datatype: " +
          Variant.typeName(dt));
    
    Variant v = new Variant();
    Object arr = Array.newInstance(c, ds.getRowCount());
    for (int i = 0; i < ds.rowCount(); i++) {
      ds.getVariant(col, i, v);
      Array.set(arr, i, v.getAsObject());
    }
    return arr;
  }
  
  /**
   * Usporeðuje kolonu 'col' dva dataseta, i vraæa dataset s popisom elemenata koji
   * nedostaju u jednom od ta dva dataseta. Za traženje raznih grešaka.
   */
  public static DataSet compareValues(DataSet one, DataSet two, String col) {
    Column first = (Column) one.getColumn(col).clone();
    Column second = (Column) two.getColumn(col).clone();
    if (first.getDataType() != second.getDataType())
      throw new IllegalArgumentException("Kolone koje se usporeðuju su nejednakog tipa");
    if (first.getDataType() != Variant.STRING &&
        first.getDataType() != Variant.BIGDECIMAL)
      throw new IllegalArgumentException("Kolone koje se usporeðuju moraju biti " +
            " tipa String ili BigDecimal");
    String name1 = col + "_1";
    String name2 = col + "_2";
    first.setColumnName(name1);
    first.setDefault(null);
    second.setColumnName(name2);
    second.setDefault(null);
    boolean bd = first.getDataType() == Variant.BIGDECIMAL;
    
    StorageDataSet result = new StorageDataSet();
    result.setColumns(new Column[] {first, second});
    result.open();
    
    List a1 = getSortedColumn(one, col, bd); 
    List a2 = getSortedColumn(two, col, bd);
    
    for (int i1 = 0, i2 = 0, as1 = a1.size(), as2 = a2.size(); i1 < as1 || i2 < as2; ++i1, ++i2) {
      if (i2 >= as2) fillValue(result, name1, a1.get(i1), bd);
      else if (i1 >= as1) fillValue(result, name2, a2.get(i2), bd);
      else {
        Comparable c1 = (Comparable) a1.get(i1);
        Comparable c2 = (Comparable) a2.get(i2);
        if (c1.compareTo(c2) < 0) {
          fillValue(result, name1, c1, bd);
          --i2;
        } else if (c2.compareTo(c1) < 0) {
          fillValue(result, name2, c2, bd);
          --i1;
        }
      }
    }
    result.post();
    return result;
  }
  
  private static void fillValue(DataSet ds, String col, Object val, boolean bd) {
    ds.insertRow(false);
    if (!bd) ds.setString(col, (String) val);
    else ds.setBigDecimal(col, (BigDecimal) val);
  }
  
  private static List getSortedColumn(DataSet ds, String col, boolean bd) {
    Variant temp = new Variant();
    
    List l = new ArrayList();
    for (int i = 0; i < ds.rowCount(); i++) {
      ds.getVariant(col, i, temp);
      if (bd) l.add(temp.getBigDecimal());
      else l.add(temp.getString());
    }
    Collections.sort(l);
    return l;
  }
  
  public static void fontSize(int size, JComponent[] comps) {
    for (int i = 0; i < comps.length; i++)
      comps[i].setFont(comps[i].getFont().deriveFont((float) size));
  }
  
  public static void fontSize(int size, JPanel cont) {
    for (int i = 0; i < cont.getComponentCount(); i++)
      if (cont.getComponent(i) instanceof JPanel)
        fontSize(size, (JPanel) cont.getComponent(i));
      else cont.getComponent(i).setFont(
          cont.getComponent(i).getFont().deriveFont((float) size));
  }
  
  public static Color halfTone(Color cFrom, Color cTo, float factor) {
    return new Color((int) (cFrom.getRed() * (1 - factor) + cTo.getRed() * factor),
                     (int) (cFrom.getGreen() * (1 - factor) + cTo.getGreen() * factor),
                     (int) (cFrom.getBlue() * (1 - factor) + cTo.getBlue() * factor));
  }
    
  public static String leg(int cond, String less, String eq, String greater) {
    if (cond == 0) return eq;
    return cond < 0 ? less : greater;
  }
  
  static int off = 0;
  public static String timeToHex() {
    long tim = System.currentTimeMillis() + off++;
    StringBuffer hex = new StringBuffer(16);
    for (int i = 0; i < 16; i++, tim >>>= 4)
      hex.insert(0, hexChars[(int) tim & 15]);
    return hex.toString();
  }
  
  public static String timeToString() {
    StringBuffer ret = new StringBuffer(11);
    for (long tim = System.currentTimeMillis() + off++; tim > 0; tim >>>= 6)
      ret.insert(0, sixBitChars[(int) tim & 63]);
    return ret.toString();
  }

  public static String getDumpSeparator() {
    String sep = null;
    try {
      if (!dM.isMinimal())
        sep = frmParam.getParam("sisfun", "dumpSeparator");
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
    if (sep == null || sep.length() != 1 || sep.equals(",")) sep = "#";
    return sep;
  }
  
  public static String createLink(String text, String action) {
    VarStr v = new VarStr();
    v.append("<a href=\"");
    v.append(toHtml(new VarStr(action)));
    v.append("\">");
    v.append(toHtml(new VarStr(text)));
    v.append("</a>");
    return v.toString();
  }
  
  public static VarStr toHtml(VarStr v) {
    v.replaceAll("&", "&amp;");
    v.replaceAll("\"", "&quot;");
    v.replaceAll("<", "&lt;");
    v.replaceAll(">", "&gt;");
    return v;
  }
  
  public static String toHtml(String orig) {
    return toHtml(new VarStr(orig)).toString();
  }
  
  public static String convertToURLFriendly(String orig) {
    VarStr v = new VarStr(orig);
    Set non = new HashSet();
    for (int i = 0; i < v.length(); i++)
      if (!Character.isLetterOrDigit(v.charAt(i)) && v.charAt(i) != ' ')
        non.add(new Character(v.charAt(i)));
    
    for (Iterator i = non.iterator(); i.hasNext(); ) {
      Character ch = (Character) i.next();
      v.replaceAll(ch.toString(), "%".concat(Integer.toHexString(ch.charValue())));
    }
    return v.replaceAll(' ','+').toString();
  }
  
  public static String convertToAscii(String orig) {
    String[] our = {"È", "Æ", "Ð", "Š", "Ž", "è", "æ", "ð", "š", "ž"};
    String[] asc = {"C", "C", "DJ", "S", "Z", "c", "c", "dj", "s", "z"};
    
    VarStr buf = new VarStr(orig);
    for (int i = 0; i < our.length; i++)
      buf.replaceAll(our[i], asc[i]);
    
    return buf.toString();
  }
  
  public static int simpleCode(int code) {
    DesEncrypter des = new DesEncrypter("1restart2");
    Timestamp now = getNow();
    String out = des.encrypt(code + now.toString());
    int ret = 0;
    for (int ch = 0; ch < 5 && ch < out.length(); ch++)
      ret = ret * 17 + out.charAt(ch);
    
    return ret % 90000 + 10000;
  }
  
  public static Timestamp getNow() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    String server = IntParam.getTag("time.server");
    if (server == null || server.length() == 0) server = "time.apple.com";
    if (server == null || server.length() == 0) return now;
    
    try {
      org.apache.commons.net.ntp.NTPUDPClient client = new org.apache.commons.net.ntp.NTPUDPClient();
      // We want to timeout if a response takes longer than 10 seconds
      client.setDefaultTimeout(10000);
      try {
        client.open();
        InetAddress host = InetAddress.getByName(server);
        System.out.println("> " + host.getHostName() + "/" + host.getHostAddress());
        org.apache.commons.net.ntp.TimeInfo info = client.getTime(host);
        
        now = new Timestamp(info.getMessage().getTransmitTimeStamp().getTime());
        /*
        org.apache.commons.net.ntp.NtpV3Packet message = info.getMessage();
        int stratum = message.getStratum();
        String refType;
        if (stratum <= 0) {
            refType = "(Unspecified or Unavailable)";
        } else if (stratum == 1) {
            refType = "(Primary Reference; e.g., GPS)"; // GPS, radio clock, etc.
        } else {
            refType = "(Secondary Reference; e.g. via NTP or SNTP)";
        }
        // stratum should be 0..15...
        System.out.println(" Stratum: " + stratum + " " + refType);
        int version = message.getVersion();
        int li = message.getLeapIndicator();
        System.out.println(" leap=" + li + ", version="
                + version + ", precision=" + message.getPrecision());

        System.out.println(" mode: " + message.getModeName() + " (" + message.getMode() + ")");
        int poll = message.getPoll();
        // poll value typically btwn MINPOLL (4) and MAXPOLL (14)
        System.out.println(" poll: " + (poll <= 0 ? 1 : (int) Math.pow(2, poll))
                + " seconds" + " (2 ** " + poll + ")");
        double disp = message.getRootDispersionInMillisDouble();
        System.out.println(" rootdelay=" + numberFormat.format(message.getRootDelayInMillisDouble())
                + ", rootdispersion(ms): " + numberFormat.format(disp));

        int refId = message.getReferenceId();
        String refAddr = org.apache.commons.net.ntp.NtpUtils.getHostAddress(refId);
        String refName = null;
        if (refId != 0) {
            if (refAddr.equals("127.127.1.0")) {
                refName = "LOCAL"; // This is the ref address for the Local Clock
            } else if (stratum >= 2) {
                // If reference id has 127.127 prefix then it uses its own reference clock
                // defined in the form 127.127.clock-type.unit-num (e.g. 127.127.8.0 mode 5
                // for GENERIC DCF77 AM; see refclock.htm from the NTP software distribution.
                if (!refAddr.startsWith("127.127")) {
                    try {
                        InetAddress addr = InetAddress.getByName(refAddr);
                        String name = addr.getHostName();
                        if (name != null && !name.equals(refAddr)) {
                            refName = name;
                        }
                    } catch (UnknownHostException e) {
                        // some stratum-2 servers sync to ref clock device but fudge stratum level higher... (e.g. 2)
                        // ref not valid host maybe it's a reference clock name?
                        // otherwise just show the ref IP address.
                        refName = org.apache.commons.net.ntp.NtpUtils.getReferenceClock(message);
                    }
                }
            } else if (version >= 3 && (stratum == 0 || stratum == 1)) {
                refName = org.apache.commons.net.ntp.NtpUtils.getReferenceClock(message);
                // refname usually have at least 3 characters (e.g. GPS, WWV, LCL, etc.)
            }
            // otherwise give up on naming the beast...
        }
        if (refName != null && refName.length() > 1) {
            refAddr += " (" + refName + ")";
        }
        System.out.println(" Reference Identifier:\t" + refAddr);

        org.apache.commons.net.ntp.TimeStamp refNtpTime = message.getReferenceTimeStamp();
        System.out.println(" Reference Timestamp:\t" + refNtpTime + "  " + refNtpTime.toDateString());

        // Originate Time is time request sent by client (t1)
        org.apache.commons.net.ntp.TimeStamp origNtpTime = message.getOriginateTimeStamp();
        System.out.println(" Originate Timestamp:\t" + origNtpTime + "  " + origNtpTime.toDateString());

        long destTime = info.getReturnTime();
        // Receive Time is time request received by server (t2)
        org.apache.commons.net.ntp.TimeStamp rcvNtpTime = message.getReceiveTimeStamp();
        System.out.println(" Receive Timestamp:\t" + rcvNtpTime + "  " + rcvNtpTime.toDateString());

        // Transmit time is time reply sent by server (t3)
        org.apache.commons.net.ntp.TimeStamp xmitNtpTime = message.getTransmitTimeStamp();
        System.out.println(" Transmit Timestamp:\t" + xmitNtpTime + "  " + xmitNtpTime.toDateString());

        // Destination time is time reply received by client (t4)
        org.apache.commons.net.ntp. TimeStamp destNtpTime = org.apache.commons.net.ntp.TimeStamp.getNtpTime(destTime);
        System.out.println(" Destination Timestamp:\t" + destNtpTime + "  " + destNtpTime.toDateString());

        info.computeDetails(); // compute offset/delay if not already done
        Long offsetValue = info.getOffset();
        Long delayValue = info.getDelay();
        String delay = (delayValue == null) ? "N/A" : delayValue.toString();
        String offset = (offsetValue == null) ? "N/A" : offsetValue.toString();

        System.out.println(" Roundtrip delay(ms)=" + delay
                + ", clock offset(ms)=" + offset); // offset in ms
        */
        
      } catch (Exception e) {
        e.printStackTrace();
      }
      client.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("server time " + now);
    return now;
  }
  
  public static Connection getMainConnection() {
    String url = null, tip = null, user = null, pass = null;
    
    Properties props = new Properties();
    FileHandler.loadProperties("base.properties", props, false);
    
    url = props.getProperty("url1");
    if (url != null) {
      tip = props.getProperty("tip1", props.getProperty("tip"));
      user = props.getProperty("user1", props.getProperty("user"));
      pass = props.getProperty("pass1", props.getProperty("pass"));
    }
    
    if (url == null || tip == null || user == null || pass == null) {
      props.clear();
      FileHandler.loadProperties("restart.properties", props, false);
      
      url = props.getProperty("url");
      tip = props.getProperty("tip");
      user = props.getProperty("user");
      pass = props.getProperty("pass");
    }
    return getConnection(url, tip, user, pass);
  }
  
  public static Connection getConnection(String url, String tip, String user, String pass) {
    if (url == null || tip == null || user == null || pass == null) return null;
    
    try {
      Class.forName(tip);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    try {
      return DriverManager.getConnection(url, user, pass);
    } catch(SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static class Data {
    public BigDecimal bruto, mio1, mio2, pmio1, pmio2, dohodak, osnodb, koef, odbitak, osnovica, 
            stopa1, stopa2, stopa3, osn1, osn2, osn3, por1, por2, por3, poruk, stopaprir, prir, neto, zos, zap, oz, bruto2;
    
    public Data() {
        bruto = mio1 = mio2 = pmio1 = pmio2 = dohodak = osnodb = koef = odbitak = osnovica = stopa1 = stopa2 = stopa3 = 
                osn1 = osn2 = osn3 = por1 = por2 = por3 = poruk = stopaprir = prir = neto = zos = zap = oz = bruto2 = BigDecimal.ZERO;
    }
  }
  
  private static class Params {
    public static BigDecimal mio1 = new BigDecimal("15.00");
    public static BigDecimal mio2 = new BigDecimal("5.00");
    public static BigDecimal mio = new BigDecimal("20.00");
    
    public static BigDecimal odb = new BigDecimal("2600.00");
    public static BigDecimal koef = new BigDecimal("1");
    public static BigDecimal dk = new BigDecimal("0.5");
    public static BigDecimal dn = new BigDecimal("0.2");
    public static BigDecimal dinc = new BigDecimal("0.1");
    
    public static BigDecimal por1 = new BigDecimal("12.00");
    public static BigDecimal por2 = new BigDecimal("25.00");
    public static BigDecimal por3 = new BigDecimal("40.00");
    
    public static BigDecimal razred1 = new BigDecimal("2200.00");
    public static BigDecimal razred2 = new BigDecimal("11000.00");
  }

  public static Data findPlaca(BigDecimal iznos, boolean bruto, boolean stup2, int djece, int uzdr, BigDecimal prirez) {
    if (bruto) return findByBruto(iznos, stup2, getKoef(djece, uzdr), prirez);
    return findByNeto(iznos, stup2, getKoef(djece, uzdr), prirez);
  }

  private static BigDecimal limit(BigDecimal num, BigDecimal max) {
    if (num.compareTo(max) > 0) return max;
    return num;
  }

  private static BigDecimal getKoef(int djece, int uzdr) {
    BigDecimal koef = Params.koef;
    BigDecimal dk = Params.dk;
    BigDecimal dn = Params.dn;
    for (int d = 1; d <= djece; d++) {
        koef = koef.add(dk);
        dk = dk.add(dn);
        dn = dn.add(Params.dinc);
    }
    for (int u = 1; u <= uzdr; u++)
        koef = koef.add(Params.dk);
    
    return koef;
  }

  private static Data findByBruto(BigDecimal bruto, boolean stup2, BigDecimal koef, BigDecimal prirez) {
    Data ret = new Data();
    
    ret.bruto = bruto.setScale(2, BigDecimal.ROUND_HALF_UP);
    if (stup2) {
        ret.pmio1 = Params.mio1;
        ret.pmio2 = Params.mio2;
    } else 
        ret.pmio1 = Params.mio;

    ret.mio1 = bruto.multiply(ret.pmio1).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP);
    ret.mio2 = bruto.multiply(ret.pmio2).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP);
    ret.dohodak = ret.bruto.subtract(ret.mio1).subtract(ret.mio2);
    
    ret.osnodb =  Params.odb;
    ret.koef = koef;
    ret.odbitak = ret.osnodb.multiply(ret.koef).setScale(2, BigDecimal.ROUND_HALF_UP);
    if (ret.odbitak.compareTo(ret.dohodak) > 0) ret.odbitak = ret.dohodak;
    ret.osnovica = ret.dohodak.subtract(ret.odbitak);
    ret.stopa1 = Params.por1;
    ret.stopa2 = Params.por2;
    ret.stopa3 = Params.por3;
    BigDecimal osn = ret.osnovica;
    ret.osn1 = limit(osn, Params.razred1);
    osn = osn.subtract(ret.osn1);
    ret.osn2 = limit(osn, Params.razred2);
    ret.osn3 = osn.subtract(ret.osn2);
    ret.por1 = ret.osn1.multiply(Params.por1).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP);
    ret.por2 = ret.osn2.multiply(Params.por2).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP);
    ret.por3 = ret.osn3.multiply(Params.por3).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP);
    ret.poruk = ret.por1.add(ret.por2).add(ret.por3);
    ret.stopaprir = prirez;
    ret.prir = ret.poruk.multiply(prirez).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP);
    ret.neto = ret.dohodak.subtract(ret.poruk).subtract(ret.prir);
    
    return ret;
  }
  
  private static Data findByNeto(BigDecimal bruto, BigDecimal neto, boolean stup2, BigDecimal koef, BigDecimal prirez) {
    for (int i = 0; i < 5; i++) {
      Data ret = findByBruto(bruto, stup2, koef, prirez);
      if (ret.neto.compareTo(neto) == 0) return ret;
      bruto = bruto.subtract(ret.neto).add(neto);
      System.out.println("adjustring bruto " + bruto);
    }
    return null;
  }

  private static Data findByNeto(BigDecimal neto, boolean stup2, BigDecimal koef, BigDecimal prirez) {
    BigDecimal mio = BigDecimal.ONE.subtract((stup2 ? Params.mio1.add(Params.mio2) : Params.mio).movePointLeft(2));
    BigDecimal odb = Params.odb.multiply(koef);
    
    //D = N - O
    if (neto.compareTo(odb) <= 0) {
        BigDecimal bruto = neto.divide(mio, 2, BigDecimal.ROUND_HALF_UP);
        return findByNeto(bruto, neto, stup2, koef, prirez);
    }
    
    BigDecimal prn = BigDecimal.ONE.add(prirez.movePointLeft(2));
    BigDecimal pp1 = Params.por1.multiply(prn).movePointLeft(2);
    
    //D=(N-O*s1)/(1-S1) - O
    BigDecimal osn = neto.subtract(odb.multiply(pp1)).
            divide(BigDecimal.ONE.subtract(pp1), 6, BigDecimal.ROUND_HALF_UP).subtract(odb);
    if (osn.compareTo(Params.razred1) <= 0) {
        BigDecimal bruto = osn.add(odb).divide(mio, 2, BigDecimal.ROUND_HALF_UP);
        return findByNeto(bruto, neto, stup2, koef, prirez);
    }
    
    BigDecimal pp2 = Params.por2.multiply(prn).movePointLeft(2);
    
    //D = (N -(O+2200)*S2 + 2200*s1) / (1-s2) - O
    osn = neto.subtract(odb.add(Params.razred1).multiply(pp2)).add(Params.razred1.multiply(pp1)).
            divide(BigDecimal.ONE.subtract(pp2), 6, BigDecimal.ROUND_HALF_UP).subtract(odb);
    if (osn.compareTo(Params.razred2) <= 0) {
        BigDecimal bruto = osn.add(odb).divide(mio, 2, BigDecimal.ROUND_HALF_UP);
        return findByNeto(bruto, neto, stup2, koef, prirez);
    }
    
    BigDecimal pp3 = Params.por3.multiply(prn).movePointLeft(2);
    
    // X = (N-(O+13200)*s3 +2200*s1 + 11000*s2)/ 0.8*(1-s3)
    BigDecimal bruto = neto.subtract(odb.add(Params.razred1).add(Params.razred2).multiply(pp3)).
            add(Params.razred1.multiply(pp1)).add(Params.razred2.multiply(pp2)).
            divide(BigDecimal.ONE.subtract(pp3).multiply(mio), 2, BigDecimal.ROUND_HALF_UP);

    return findByNeto(bruto, neto, stup2, koef, prirez);
  }
}
