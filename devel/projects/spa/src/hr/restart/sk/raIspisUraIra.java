/****license*****************************************************************
**   file: raIspisUraIra.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.IzvjPDV;
import hr.restart.baza.KoloneknjUI;
import hr.restart.baza.Shkonta;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.StIzvjPDV;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.robno.TypeDoc;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raDelayWindow;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.JasperHook;
import hr.restart.util.reports.TemplateModifier;
import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raReportDescriptor;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raRunReport;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import sg.com.elixir.reportwriter.xml.IModel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raIspisUraIra extends raFrame {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  sysoutTEST sys = new sysoutTEST(false);

  raRunReport RepRun;
  static raIspisUraIra rui;

  JPanel jpDetail = new JPanel();

  JPanel jpK = new JPanel();
  XYLayout xyK = new XYLayout();

  XYLayout lay = new XYLayout();
  JLabel jlK = new JLabel();
  JLabel jlCknjige = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlGod = new JLabel();
  JraButton jbSelCknjige = new JraButton();
  JraRadioButton jrbUraira1 = new JraRadioButton();
  JraRadioButton jrbUraira2 = new JraRadioButton();
  JraTextField jraMfrom = new JraTextField();
  JraTextField jraMto = new JraTextField();
  JraTextField jraGod = new JraTextField();
  raButtonGroup bg1 = new raButtonGroup();
  JlrNavField jlrNazknjige = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCknjige = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      if (!busy)
        OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  StorageDataSet fset = new StorageDataSet();
  QueryDataSet uraira;
  boolean busy;
  boolean oib;

  int lastui = 0;
  Timestamp dmfrom, dmto;


  public raIspisUraIra() {
    try {
      rui = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static raIspisUraIra getInstance() {
    return rui;
  }

  public QueryDataSet getDataSet() {
    return uraira;
  }

  public raRunReport getRepRunner() {
    if (RepRun == null) {
      RepRun = raRunReport.getRaRunReport();
      RepRun.setOwner(this.getWindow(), getClass().getName());
    }
    return RepRun;
  }

  public void SetFokus() {
    oib = !"MB".equals(frmParam.getParam("robno", "oibMode", "MB", 
      "Staviti mati�ni broj (MB) ili OIB?"));
    if (jrbUraira1.isSelected()) uraSelected();
    else iraSelected();
  }

  boolean PDVk, PDVk_ex;
  public boolean Validacija() {
    if (vl.isEmpty(jlrCknjige)) return false;
    int mf = 0, mt = 0, god = 0;
    try {
      mf = Integer.parseInt(jraMfrom.getText().trim());
      mt = Integer.parseInt(jraMto.getText().trim());
      god = Integer.parseInt(jraGod.getText().trim());
    } catch (Exception e) {}   
    if (mf == 0 || mf > 13) {
      jraMfrom.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Pogre�an po�etni mjesec!", "Gre�ka",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mt == 0 || mt > 13) {
      jraMto.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Pogre�an krajnji mjesec!", "Gre�ka",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mf > mt) {
      jraMto.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Po�etni mjesec ve�i od krajnjeg!", "Gre�ka",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    int todayYear = Integer.parseInt(ut.getYear(vl.getToday()));
    if (god == 0 || god > todayYear) {
      jraGod.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Pogre�na godina!", "Gre�ka",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    PDVk = mt == 13;
    PDVk_ex = mf == 13;
    if (mt == 13) mt = 12;
    if (mf == 13) mf = 12;
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, god);
    cal.set(Calendar.DAY_OF_MONTH, 2);
    cal.set(Calendar.MONTH, mf - 1);
    dmfrom = new Timestamp(cal.getTime().getTime());
    cal.set(Calendar.MONTH, mt - 1);
    dmto = new Timestamp(cal.getTime().getTime());
    return true;
  }

  public void show() {
    Aus.recursiveUpdateSizes(jpDetail);
    SetFokus();
    super.show();
  }

  public String getCKNJIGE() {
    return fset.getString("CKNJIGE");
  }

  public String getNAZKNJIGE() {
    return fset.getString("NAZKNJIGE");
  }

  private void OKPress() {
    if (!Validacija()) return;
    busy = true;
    new Thread() {
      public void run() {
        try {
          process();
        } finally {
          busy = false;
        }
      }
    }.start();
  }

  private void cancelPress() {
    this.hide();
  }

  private void process() {
    raDelayWindow proc = raDelayWindow.show(this.getWindow(), 500).setInterruptable(true);
    findUraIra(jrbUraira1.isSelected() ? "U" : "I", true);

    System.out.println("ROWS: "+uraira.getRowCount());
    getRepRunner().clearAllReports();
    if (jrbUraira1.isSelected()) {
      getRepRunner().addJasper("hr.restart.sk.repURAEU13", "hr.restart.sk.repURAEU",
          "uraeu2.jrxml", "Ispis knjige URA EU 13%");
      getRepRunner().addJasper("hr.restart.sk.repURAEvident", "hr.restart.sk.repURAEU",
          "evident.jrxml", "Ispis evidencije porezne obveze");
      getRepRunner().addJasper("hr.restart.sk.repURAEU10", "hr.restart.sk.repURAEU",
          "uraeu10.jrxml", "Ispis knjige URA EU 10%");
      getRepRunner().addJasper("hr.restart.sk.repURAEU", "hr.restart.sk.repURAEU",
          "uraeu.jrxml", "Ispis knjige URA EU");
      getRepRunner().addJasper("hr.restart.sk.repURA25", "hr.restart.sk.repURANew",
          "ura25.jrxml", "Ispis knjige URA 25%");
      getRepRunner().addJasper("hr.restart.sk.repURA09", "hr.restart.sk.repURANew",
          "ura09.jrxml", "Ispis knjige URA 23%");
      getRepRunner().addReport("hr.restart.sk.repURA06", "hr.restart.sk.repURANew",
          "URA06", "Ispis knjige URA 22%");
      getRepRunner().addReport("hr.restart.sk.repURA", "hr.restart.sk.repURA",
                               "URA_ORGINAL", "Ispis knjige URA 2005");
      getRepRunner().addReport("hr.restart.sk.repURA11", "hr.restart.sk.repURA",
                               "URA11", "Ispis knjige URA 2005 s 11 kolona");
      getRepRunner().addReport("hr.restart.sk.repURA12", "hr.restart.sk.repURA",
                               "URA12", "Ispis knjige URA 2005 s 12 kolona");      
      getRepRunner().installTemplateModifier(new TemplateModifier() {
        public void modify(String id, IModel template) {
          int ext = Aus.getNumber(frmParam.getParam("sk", "ispURAchars", "0",
              "�irina broja URA na ispisu knjige, u znakovima"));
          if (ext > 0) addNumUra(new raReportSection(
              template.getModel(raElixirProperties.DETAIL), true), ext);          
        }
      });
//      getRepRunner().addReport("hr.restart.sk.repURADod", "Ispis knjige URA sa dodatnim kolonama", 5);
    } else {
      getRepRunner().addJasper("hr.restart.sk.repIRAEUN2", "hr.restart.sk.repIRA",
          "iraeun2.jrxml", "Ispis knjige IRA EU novi - oporezive kolone");
      getRepRunner().addJasper("hr.restart.sk.repIRAEUN1", "hr.restart.sk.repIRA",
          "iraeun1.jrxml", "Ispis knjige IRA EU novi - neoporezive kolone");
      getRepRunner().addJasper("hr.restart.sk.repIRAEU2", "hr.restart.sk.repIRA",
          "iraeu2.jrxml", "Ispis knjige IRA EU - oporezive kolone");
      getRepRunner().addJasper("hr.restart.sk.repIRAEU1", "hr.restart.sk.repIRA",
          "iraeu1.jrxml", "Ispis knjige IRA EU - neoporezive kolone");
      getRepRunner().addJasper("hr.restart.sk.repIRA25", "hr.restart.sk.repIRA",
          "ira25.jrxml", "Ispis knjige IRA 25%");
      getRepRunner().addJasper("hr.restart.sk.repIRA10", "hr.restart.sk.repIRA",
          "ira10.jrxml", "Ispis knjige IRA 23%");
      getRepRunner().addJasper("hr.restart.sk.repIRA09", "hr.restart.sk.repIRA",
          "ira09.jrxml", "Ispis knjige IRA 2009");
      getRepRunner().addReport("hr.restart.sk.repIRA06", "hr.restart.sk.repIRA",
          "IRA06", "Ispis knjige IRA");
      getRepRunner().addReport("hr.restart.sk.repIRA", "hr.restart.sk.repIRA",
                               "IRA", "Ispis knjige IRA 2005");
      getRepRunner().installTemplateModifier(new TemplateModifier() {
        public void modify(String id, IModel template) {
          int ext = Aus.getNumber(frmParam.getParam("sk", "ispIRAchars", "0",
              "�irina broja IRA na ispisu knjige, u znakovima"));
          if (ext > 0) addNumUra(new raReportSection(
              template.getModel(raElixirProperties.DETAIL), true), ext);          
        }
      });
      JasperHook jh = new JasperHook() {
        public void adjustDesign(String reportName, JasperDesign design) {
          addJaspNum(design);
        }
      };
      int ext = Aus.getNumber(frmParam.getParam("sk", "ispIRAchars", "0",
          "�irina broja IRA na ispisu knjige, u znakovima"));
      if (ext > 0) {
        getRepRunner().addJasperHook("hr.restart.sk.repIRAEUN2", jh);
        getRepRunner().addJasperHook("hr.restart.sk.repIRAEUN1", jh);
        getRepRunner().addJasperHook("hr.restart.sk.repIRAEU2", jh);
        getRepRunner().addJasperHook("hr.restart.sk.repIRAEU1", jh);
        getRepRunner().addJasperHook("hr.restart.sk.repIRA25", jh);
        getRepRunner().addJasperHook("hr.restart.sk.repIRA10", jh);
        getRepRunner().addJasperHook("hr.restart.sk.repIRA09", jh);
      }
    }
    proc.close();
    if (!proc.isInterrupted())
      getRepRunner().go();
    busy = false;
  }
  
  void addJaspNum(JasperDesign jas) {
    int ext = Aus.getNumber(frmParam.getParam("sk", "ispIRAchars", "0",
        "�irina broja IRA na ispisu knjige, u znakovima"));
    if (ext > 0) {
      boolean noext = true;
      JRField[] fld = jas.getFields();
      for (int i = 0; i < fld.length; i++)
        if (fld[i].getName().equalsIgnoreCase("EXTBRDOK")) noext = false;
      if (noext) {
        JRDesignField field = new JRDesignField();
        field.setName("EXTBRDOK");
        field.setDescription("EXTBRDOK");
        field.setValueClassName("java.lang.String");
        
        try {
          jas.addField(field);
        } catch (JRException e) {
          e.printStackTrace();
        }
      }

      JRElement[] els = jas.getDetail().getElements();
      for (int i = 0; i < els.length; i++) 
        if (els[i] instanceof JRDesignTextField)
          checkElement(jas, (JRDesignTextField) els[i], ext);
    }
  }
  
  void checkElement(JasperDesign jas, JRDesignTextField fld, int ext) {
    String txt = fld.getExpression().getText();
    if (txt.toUpperCase().indexOf("BROJDOK") >= 0) {
      JRDesignTextField tf = new JRDesignTextField();
      JRDesignExpression je = new JRDesignExpression();
      je.setText("$F{EXTBRDOK}");
      je.setValueClassName("java.lang.String");
      tf.setExpression(je);
      tf.setBlankWhenNull(true);
      tf.setWrapAllowed(false);
      tf.setStretchWithOverflow(false);
      tf.setFontName(fld.getFontName());
      tf.setFontSize(fld.getFontSize());
      tf.setForecolor(fld.getForecolor());
      tf.setBold(fld.isBold());
      tf.setPdfEncoding(fld.getPdfEncoding());
      tf.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_RIGHT);
      tf.setRightPadding(fld.getLeftPadding());
      tf.setTopPadding(fld.getTopPadding());
      tf.setVerticalAlignment(fld.getVerticalAlignment());
      tf.setY(fld.getY());
      tf.setHeight(fld.getHeight());
      int total = fld.getWidth();
      fld.setWidth(total * 12 / (12 + ext));
      tf.setWidth(total * ext / (12 + ext));
      tf.setX(fld.getX() + total - tf.getWidth());
      ((JRDesignBand) jas.getDetail()).addElement(tf);
    }
  }
  
  void addNumUra(raReportSection detail, int ch) {
    raReportElement brdok = null;
    for (int i = 0; i < detail.getModelCount(); i++)
      if (detail.getModel(i).isText() && 
          detail.getModel(i).getControlSource().equalsIgnoreCase("BROJDOK")) 
        brdok = detail.getModel(i);
    if (brdok != null) {
      raReportElement ura = detail.addModel(raElixirProperties.TEXT, 
          (String[]) brdok.getDefaults().clone());
      ura.restoreDefaults();
      ura.setControlSource("EXTBRDOK");
      ura.setTextAlign(raElixirProperties.RIGHT);
      brdok.setTextAlign(raElixirProperties.LEFT);
      double wid = brdok.getWidthCm();
      brdok.setWidthCm(wid * (17 - ch) / 18);
      ura.setWidthCm(wid * ch / 18);
      ura.setLeftCm(ura.getLeftCm() + brdok.getWidthCm() + wid / 36);
      ura.setProperty(raElixirProperties.WRAP, raElixirPropertyValues.NO);
    }
  }

  private void setUraIraDataSet(String ui) {
    uraira = new QueryDataSet() {
      public boolean refreshSupported() {
        return false;
      }
      public boolean saveChangesSupported() {
        return false;
      }
      public void saveChanges() {
        this.post();
      }
      public void refresh() {
      }
    };
    QueryDataSet tkol = KoloneknjUI.getDataModule().getTempSet(
        Condition.equal("URAIRA", ui) + " ORDER BY ckolone");
    LinkedList cols = new LinkedList();
//    cols.add(dm.getSkstavke().getColumn("CKNJIGE").clone());
    cols.add(dM.createIntColumn("BROJ"));
    cols.add(dm.getUIstavke().getColumn("RBS").clone());
    cols.add(dm.getSkstavke().getColumn("BROJDOK").clone());
    cols.add(dm.getSkstavke().getColumn("EXTBRDOK").clone());
    cols.add(dm.getSkstavke().getColumn("DATPRI").clone());
    cols.add(dm.getSkstavke().getColumn("DATDOK").clone());
    cols.add(dm.getPartneri().getColumn("CPAR").clone());
    cols.add(dM.createStringColumn("OPISPAR", "Naziv partnera", 180));
    //cols.add(dm.getPartneri().getColumn("MB").clone());
    cols.add(dM.createStringColumn("MB", oib ? "OIB": "MB", 30));
    cols.add(dM.createStringColumn("SORTER", 16));
    tkol.open();
    for (int i = 6; i <= (ui.equals("U") ? 19 : 23); i++) {
      cols.add(dM.createBigDecimalColumn("KOLONA" + i, Integer.toString(i)));
    }
    /*for (tkol.first(); tkol.inBounds(); tkol.next()) {
      if (tkol.getString("URAIRA").equals(ui) && tkol.getShort("CKOLONE") != 0
          && tkol.getShort("CKOLONE") < 25) {
        cols.add(dM.createBigDecimalColumn("KOLONA" + tkol.getShort("CKOLONE"), 
            Integer.toString(tkol.getShort("CKOLONE"))));
//        cols.add(c = (Column) tkol.getColumn("NAZIVKOLONE").clone());
//        c.setColumnName("NAZIVKOLONE" + tkol.getShort("CKOLONE"));
      }
    }*/
    cols.add(dM.createBigDecimalColumn("OSTALO", "Ostalo"));
    if (ui.equals("U")) {
      cols.add(dM.createBigDecimalColumn("OCHECK5", "Gre�ka 5% poreza"));
      cols.add(dM.createBigDecimalColumn("OCHECK10", "Gre�ka 13% poreza"));
      cols.add(dM.createBigDecimalColumn("OCHECK25", "Gre�ka 25% poreza"));
    } else {
      cols.add(dM.createBigDecimalColumn("OCHECK5", "Gre�ka 5% poreza"));
      cols.add(dM.createBigDecimalColumn("OCHECK10", "Gre�ka 13% poreza"));
      cols.add(dM.createBigDecimalColumn("OCHECK25", "Gre�ka 25% poreza"));
    }
    cols.add(dM.createBigDecimalColumn("SALDO", "Gre�ka zbroja"));
    uraira.setColumns((Column[]) cols.toArray(new Column[] {}));
    uraira.open();
    uraira.getColumn("BROJ").setVisible(TriStateProperty.FALSE);
    uraira.getColumn("SORTER").setVisible(TriStateProperty.FALSE);
    //uraira.getColumn("MB").setVisible(TriStateProperty.FALSE);
  }
  
  public static BigDecimal getKolona(ReadRow _set, String nc) {
    String cn = "KOLONA"+nc;
    if (_set.hasColumn(cn)==null) {
      return Aus.zero2;
    } else {
      return _set.getBigDecimal(cn).setScale(2,BigDecimal.ROUND_HALF_UP);
    }
  }
  
  private void insertSums(Timestamp df, Timestamp dt, HashMap sums, int broj) {
    uraira.insertRow(false);
    uraira.setInt("RBS", 0);
    uraira.setInt("BROJ", broj);
//    uraira.setString("CKNJIGE", fset.getString("CKNJIGE"));
    uraira.setString("OPISPAR", "UKUPNO od "+
                     rdu.dataFormatter(ut.getFirstDayOfMonth(df))+
                     " do "+rdu.dataFormatter(ut.getLastDayOfMonth(dt)));
    for (Iterator i = sums.keySet().iterator(); i.hasNext();) {
      String col = (String) i.next();
      uraira.setBigDecimal(col, (BigDecimal) sums.get(col));
    }
  }
  
  // WARNING:  HARDCODED!!
  private BigDecimal getColValue(DataSet ds, boolean ulaz) {
    if ((ulaz && ds.getShort("CKOLONE") == utot) ||
        (!ulaz && ds.getShort("CKOLONE") != 6))
      return ds.getBigDecimal("IP").subtract(ds.getBigDecimal("ID"));
    return ds.getBigDecimal("ID").subtract(ds.getBigDecimal("IP"));
  }
  
  private void findErrors(boolean ulaz) {
    BigDecimal x5 = new BigDecimal("0.05");
    BigDecimal x10 = new BigDecimal("0.13");
    BigDecimal x25 = new BigDecimal("0.25");
    boolean nem = "D".equalsIgnoreCase(frmParam.getParam("sk", 
        "checkNemoze", "N", "Ura�unati kolonu ne mo�e se odbiti u provjeru (D,N)"));
    for (uraira.first(); uraira.inBounds(); uraira.next()) {
      if (ulaz) {
        uraira.setBigDecimal("OCHECK5", uraira.getBigDecimal("KOLONA6").
              multiply(x5).setScale(2, BigDecimal.ROUND_HALF_UP).
              subtract(uraira.getBigDecimal("KOLONA11")).
              subtract(nem ? uraira.getBigDecimal("KOLONA12") : Aus.zero2));
        uraira.setBigDecimal("OCHECK10", uraira.getBigDecimal("KOLONA7").
            multiply(x10).setScale(2, BigDecimal.ROUND_HALF_UP).
            subtract(uraira.getBigDecimal("KOLONA13")).
            subtract(nem ? uraira.getBigDecimal("KOLONA14") : Aus.zero2));
        uraira.setBigDecimal("OCHECK25", uraira.getBigDecimal("KOLONA8").
            multiply(x25).setScale(2, BigDecimal.ROUND_HALF_UP).
            subtract(uraira.getBigDecimal("KOLONA15")).
            subtract(nem ? uraira.getBigDecimal("KOLONA16") : Aus.zero2));
        uraira.setBigDecimal("SALDO", uraira.getBigDecimal("KOLONA9").
            subtract(uraira.getBigDecimal("KOLONA6")).
            subtract(uraira.getBigDecimal("KOLONA7")).
            subtract(uraira.getBigDecimal("KOLONA8")).
            subtract(uraira.getBigDecimal("KOLONA11")).
            subtract(uraira.getBigDecimal("KOLONA12")).
            subtract(uraira.getBigDecimal("KOLONA13")).
            subtract(uraira.getBigDecimal("KOLONA14")).
            subtract(uraira.getBigDecimal("KOLONA15")).
            subtract(uraira.getBigDecimal("KOLONA16")).
            subtract(uraira.getBigDecimal("OSTALO")));
      } else {
        uraira.setBigDecimal("OCHECK5", uraira.getBigDecimal("KOLONA17").
            multiply(x5).setScale(2, BigDecimal.ROUND_HALF_UP).
            subtract(uraira.getBigDecimal("KOLONA18")));
        uraira.setBigDecimal("OCHECK10", uraira.getBigDecimal("KOLONA19").
            multiply(x10).setScale(2, BigDecimal.ROUND_HALF_UP).
            subtract(uraira.getBigDecimal("KOLONA20")));
        uraira.setBigDecimal("OCHECK25", uraira.getBigDecimal("KOLONA21").
            multiply(x25).setScale(2, BigDecimal.ROUND_HALF_UP).
            subtract(uraira.getBigDecimal("KOLONA22")));
        uraira.setBigDecimal("SALDO", uraira.getBigDecimal("KOLONA6").
            subtract(uraira.getBigDecimal("KOLONA7")).
            subtract(uraira.getBigDecimal("KOLONA8")).
            subtract(uraira.getBigDecimal("KOLONA9")).
            subtract(uraira.getBigDecimal("KOLONA10")).
            subtract(uraira.getBigDecimal("KOLONA11")).
            subtract(uraira.getBigDecimal("KOLONA12")).
            subtract(uraira.getBigDecimal("KOLONA13")).
            subtract(uraira.getBigDecimal("KOLONA14")).
            subtract(uraira.getBigDecimal("KOLONA15")).
            subtract(uraira.getBigDecimal("KOLONA16")).
            subtract(uraira.getBigDecimal("KOLONA17")).
            subtract(uraira.getBigDecimal("KOLONA18")).
            subtract(uraira.getBigDecimal("KOLONA19")).
            subtract(uraira.getBigDecimal("KOLONA20")).
            subtract(uraira.getBigDecimal("KOLONA21")).
            subtract(uraira.getBigDecimal("KOLONA22")).
            subtract(uraira.getBigDecimal("OSTALO")));
      }
    }
  }
  
  void showDataSet() {
    final boolean showSum = "D".equalsIgnoreCase(frmParam.getParam("sk", "showSum", "N", 
      "Prikazivati sume na prikazu URA/IRA? (D/N)"));
    raProcess.runChild(new Runnable() {
      public void run() {
        
        findUraIra(jrbUraira1.isSelected() ? "U" : "I", showSum);
      }
    });
    if (raProcess.isCompleted()) {
      frmTableDataView view = new frmTableDataView(true, false, false);
      view.setDataSet(uraira);
      if (jrbUraira1.isSelected()) {
        view.setCustomReport(raReportDescriptor.create("hr.restart.sk.repURAEU13", "hr.restart.sk.repURAEU",
            "uraeu2.jrxml", "Ispis knjige URA EU 13%", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repURAEU10", "hr.restart.sk.repURAEU",
            "uraeu10.jrxml", "Ispis knjige URA EU 10%", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repURAEU", "hr.restart.sk.repURAEU",
            "uraeu.jrxml", "Ispis knjige URA EU", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repURA25", "hr.restart.sk.repURANew",
            "ura25.jrxml", "Ispis knjige URA 25%", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repURA09", "hr.restart.sk.repURANew",
            "ura09.jrxml", "Ispis knjige URA 23%", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repURA06", "hr.restart.sk.repURANew",
            "URA06", "Ispis knjige URA 22%"));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repURA", "hr.restart.sk.repURA",
                                 "URA_ORGINAL", "Ispis knjige URA 2005"));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repURA11", "hr.restart.sk.repURA",
                                 "URA11", "Ispis knjige URA 2005 s 11 kolona"));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repURA12", "hr.restart.sk.repURA",
                                 "URA12", "Ispis knjige URA 2005 s 12 kolona"));
      } else {
        view.setCustomReport(raReportDescriptor.create("hr.restart.sk.repIRAEUN2", "hr.restart.sk.repIRA",
            "iraeun2.jrxml", "Ispis knjige IRA EU novi - oporezive kolone", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repIRAEUN1", "hr.restart.sk.repIRA",
            "iraeun1.jrxml", "Ispis knjige IRA EU novi - neoporezive kolone", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repIRAEU2", "hr.restart.sk.repIRA",
            "iraeu2.jrxml", "Ispis knjige IRA EU - oporezive kolone", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repIRAEU1", "hr.restart.sk.repIRA",
            "iraeu1.jrxml", "Ispis knjige IRA EU - neoporezive kolone", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repIRA25", "hr.restart.sk.repIRA",
            "ira25.jrxml", "Ispis knjige IRA 25%", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repIRA10", "hr.restart.sk.repIRA",
            "ira10.jrxml", "Ispis knjige IRA 23%", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repIRA09", "hr.restart.sk.repIRA",
            "ira09.jrxml", "Ispis knjige IRA 2009", true));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repIRA06", "hr.restart.sk.repIRA",
            "IRA06", "Ispis knjige IRA"));
        view.addCustomReport(raReportDescriptor.create("hr.restart.sk.repIRA", "hr.restart.sk.repIRA",
                                 "IRA", "Ispis knjige IRA 2005"));
      }
      List sumc = new ArrayList();
      for (int i = 10; i < uraira.getColumnCount(); i++)
        sumc.add(uraira.getColumn(i).getColumnName());
      if (!showSum)
        view.setSums((String[]) sumc.toArray(new String[sumc.size()]));
      
      String odm = jraMfrom.getText().trim();
      String dom = jraMto.getText().trim();
      String god = jraGod.getText().trim();
      view.setTitle("Knjiga " + jlrCknjige.getText() +
          (jrbUraira1.isSelected() ? " ulaznih" : " izlaznih") + " ra�una  " + 
          (odm.equals(dom) ? "za " + odm + ". mjesec " : 
            "od " + odm + ". do " + dom + ". mjeseca ") + "godine " + god);
      view.show();
    }
  }

  int utot = 9;
  void findUraIra(String ui, boolean addSums) {
    int skiprbr = 0, rbr = 0, broj = 1;
    boolean extbr = false;
    boolean newCalc = "D".equalsIgnoreCase(frmParam.getParam("sk", "urairaSaldo", "N", 
        "Novi na�in ra�unanja vrijednosti u kolonama URA/IRA (D,N)"));
    boolean ulaz = ui.equals("U");
    if (ulaz) {
    	extbr = "D".equalsIgnoreCase(frmParam.getParam("sk", "extBrSortURA", "D", 
        		"Sortirati ra�une knjiga URA po dodatnom broju? (D/N)"));
    } else {
    	extbr = "D".equalsIgnoreCase(frmParam.getParam("sk", "extBrSortIRA", "D", 
        "Sortirati ra�une knjiga IRA po dodatnom broju? (D/N)"));
    }
    boolean skipSum = "D".equalsIgnoreCase(frmParam.getParam("sk", "skipUIsums", "N", 
        "Ne prikazivati sume na ispisu URA/IRA? (D/N)"));
    Timestamp firstm = ut.getFirstDayOfMonth(dmfrom);
    Timestamp lastm = ut.getLastDayOfMonth(dmto);
    Calendar c7 = Calendar.getInstance();
    c7.set(2013, 5, 30);
    Timestamp firsty = ut.getFirstDayOfYear(dmfrom);
    if (dmfrom.after(new Timestamp(c7.getTimeInMillis())) && ut.getYear(dmfrom).equals("2013")) {
      c7.set(2013,6,1,0,0,0);
      firsty = new Timestamp(c7.getTimeInMillis());
    }
    if (dmfrom.before(new Timestamp(c7.getTimeInMillis()))) utot = 10;
    Timestamp lasty = ut.addDays(firstm, -1);
    setUraIraDataSet(ui);
    DataSet ds;
    HashMap sumy = new HashMap();
    HashMap summ = new HashMap();
    boolean mergeKnj = frmParam.getParam("sk", "mergeKnj", "D",  
        "Omogu�iti spajanje knjiga po prvom slovu (D,N)?").equalsIgnoreCase("D");

    String join = "skstavke.knjig=uistavke.knjig AND skstavke.cpar=uistavke.cpar "+
        "AND skstavke.vrdok=uistavke.vrdok AND skstavke.brojdok=uistavke.brojdok "+
        "AND skstavke.cknjige=uistavke.cknjige";

    String cknj_cond = (mergeKnj && fset.getString("CKNJIGE").length()==1)?
                                       " LIKE '"+fset.getString("CKNJIGE")+"%'":
                                       " = '"+fset.getString("CKNJIGE")+"'";

/*    String cond = "skstavke.knjig='"+OrgStr.getKNJCORG(false)+
        "' AND " + Aus.getVrdokCond(ui.equalsIgnoreCase("I"), true).qualified("skstavke") +
            " AND skstavke.cknjige "+cknj_cond;*/
                                            
    String cond = "skstavke.knjig='"+OrgStr.getKNJCORG(false)+
                  "' AND skstavke.vrdok IN ("+
                  (ui.equalsIgnoreCase("I") ? "'IRN', 'OKK'" : "'URN', 'OKD'")+
                  ") AND skstavke.cknjige "+cknj_cond; //"LIKE '"+fset.getString("CKNJIGE")+"%'";
                                            

    if (ut.getYear(lasty).equals(ut.getYear(firstm))) {
      String pdvk = PDVk_ex ? " AND (skstavke.cnacpl is null or skstavke.cnacpl != 'PK')" : "";
      if (PDVk_ex) lasty = lastm;
      String range = vl.getBetweenQuerySintax(dm.getSkstavke().getColumn("DATPRI"),
        firsty.toString(), lasty.toString(), true);
//      uraira.insertRow(false);
//      uraira.setInt("RBS", 0);
//      uraira.setInt("BROJ", broj++);
//      uraira.setString("CKNJIGE", fset.getString("CKNJIGE"));
//      uraira.setString("OPISPAR", "UKUPNO od "+rdu.dataFormatter(firsty)+
//                       " do "+rdu.dataFormatter(lasty));
      String mightySQL = "SELECT uistavke.id, uistavke.ip, "+
        "skstavke.id as sid, skstavke.ip as sip, ckolone FROM skstavke,uistavke "+
        "WHERE "+join+" AND "+cond+" AND "+range + pdvk;

      vl.execSQL(mightySQL);
      (ds = vl.getDataAndClear()).open();
      for (ds.first(); ds.inBounds(); ds.next()) {
        boolean storno = ds.getBigDecimal("SID").add(ds.getBigDecimal("SIP")).signum() < 0;
        short ckol = ds.getShort("CKOLONE");
        if (ckol != 0) {
          String col = ckol < 25 ? "KOLONA" + ckol : "OSTALO";
          BigDecimal val = raSaldaKonti.n0;
          if (newCalc) {
            val = getColValue(ds, ulaz);
          } else {
            val = ds.getBigDecimal("ID").add(ds.getBigDecimal("IP"));
            if (storno && val.signum() > 0) val = val.negate();
            else if (!storno && val.signum() < 0) val = val.abs();
          }
          if (sumy.containsKey(col)) 
            val = val.add((BigDecimal) sumy.get(col));
          sumy.put(col, val);
//          uraira.setBigDecimal(col, (BigDecimal) sumy.get(col));
        }
      }

      String lessMightySQL = "SELECT COUNT(*) AS numrac FROM skstavke "+
        "WHERE "+cond+" AND "+range+pdvk+" GROUP BY knjig";
      vl.execSQL(lessMightySQL);
      vl.RezSet.open();
      skiprbr = rbr =  Valid.getValid().getSetCount(vl.RezSet , 0);
    }
    String range = vl.getBetweenQuerySintax(dm.getSkstavke().getColumn("DATPRI"),
        firstm.toString(), lastm.toString(), true);
    String pdvk = "";
    if (PDVk_ex) pdvk = " AND (cnacpl is not null and cnacpl = 'PK')";
    if (!PDVk) pdvk = " AND (cnacpl is null or cnacpl != 'PK')";
    String mostExcellentSQL =
      "SELECT skstavke.cpar, skstavke.datpri, skstavke.brojdok, skstavke.datdok,"+
      " skstavke.vrdok, skstavke.id as sid, skstavke.ip as sip,"+
      " skstavke.extbrdok, uistavke.id, uistavke.ip, uistavke.dugpot, uistavke.ckolone,"+
      " uistavke.rbs FROM skstavke,uistavke WHERE "+join+" AND "+cond+" AND "+range+pdvk+
      " ORDER BY skstavke.datpri, skstavke.datunos, skstavke.vrdok, skstavke.brojdok";

    System.out.println(mostExcellentSQL);
    String lastdok = "", lastvrdok = "";
    int lastcpar = -2510;
    /*String reqSide = ui.equalsIgnoreCase("U") ? "D" : "P";*/
    Timestamp lastpri = null;
    vl.execSQL(mostExcellentSQL);
    (ds = vl.getDataAndClear()).open();
//    sys.prn(ds);
    VarStr sorter = new VarStr();
    for (ds.first(); ds.inBounds(); ds.next()) {
      boolean storno = ds.getBigDecimal("SID").add(ds.getBigDecimal("SIP")).signum() < 0;
//      System.out.print(ds.getTimestamp("DATPRI") + "  " + lastpri + "  ");
      if (lastpri == null) lastpri = new Timestamp(ds.getTimestamp("DATPRI").getTime());
      if (!ut.getMonth(ds.getTimestamp("DATPRI")).equals(ut.getMonth(lastpri))) {
        if (addSums && !skipSum) {
          insertSums(lastpri, lastpri, summ, ++broj);
          if (!ut.getMonth(lastpri).equals(ut.getMonth(firsty)))
            insertSums(firsty, lastpri, sumy, broj);
        }
        ++broj;
        lastpri = new Timestamp(ds.getTimestamp("DATPRI").getTime());
        summ.clear();
      }
//      System.out.print(lastpri + "  ");
      if (!ds.getString("BROJDOK").equals(lastdok) ||
          !ds.getString("VRDOK").equals(lastvrdok) || lastcpar != ds.getInt("CPAR")) {
        lastdok = ds.getString("BROJDOK");
        lastvrdok = ds.getString("VRDOK");
        lastcpar = ds.getInt("CPAR");
        uraira.insertRow(false);
//        uraira.setString("CKNJIGE", fset.getString("CKNJIGE"));
        uraira.setInt("RBS", ++rbr);
        uraira.setInt("BROJ", broj);
        uraira.setInt("CPAR", ds.getInt("CPAR"));
        uraira.setString("BROJDOK", lastdok);
        uraira.setString("EXTBRDOK", ds.getString("EXTBRDOK"));
        // kreiranje sortera: mjesec + extbrdok ili neki brojac
	    if (extbr) {
	        sorter.clear().append(ds.getString("EXTBRDOK"));
	        int fn = 0;
	        for (int fi = 0; fi < sorter.length(); fi++)
	          if (!Character.isDigit(sorter.charAt(fi))) fn = fi + 1;
	        sorter.chopLeft(fn);
	        sorter.justify(10).leftTruncate(10).replaceAll(' ','0');
	        sorter.insert(0, ut.getMonth(lastpri));
	    } else {
	    	sorter.clear();
	    	int lh = ds.getString("BROJDOK").lastIndexOf('-');
	    	if (lh > 0) {
	    		sorter.append(ds.getString("BROJDOK").substring(lh + 1));
	    		sorter.justify(8).replaceAll(' ', '0');
	    	}
	    	sorter.insert(0, ut.getDay(ds.getTimestamp("DATPRI")));
	    	sorter.insert(0, ut.getMonth(ds.getTimestamp("DATPRI")));
	    }
        uraira.setString("SORTER", sorter.toString());
        uraira.setTimestamp("DATDOK", ds.getTimestamp("DATDOK"));
        uraira.setTimestamp("DATPRI", ds.getTimestamp("DATPRI"));
        ld.raLocate(dm.getPartneri(), "CPAR", Integer.toString(ds.getInt("CPAR")));
        String mj = dm.getPartneri().getString("MJ");
        uraira.setString("OPISPAR", dm.getPartneri().getString("NAZPAR") +
          (mj.trim().length() == 0 ? "" : ", " + mj));
        uraira.setString("MB", dm.getPartneri().getString(oib ? "OIB" : "MB"));
      }
//      System.out.println(lastpri);
      short ckol = ds.getShort("CKOLONE");      
      if (ckol != 0) {
        String col = ckol < 30 ? "KOLONA" + ckol : "OSTALO";
        BigDecimal yval = sumy.containsKey(col) ? (BigDecimal) sumy.get(col) : raSaldaKonti.n0;
        BigDecimal mval = summ.containsKey(col) ? (BigDecimal) summ.get(col) : raSaldaKonti.n0;        
        BigDecimal val = raSaldaKonti.n0;
        
        if (newCalc) {
          val = getColValue(ds, ulaz);
        } else {
          val = ds.getBigDecimal("ID").add(ds.getBigDecimal("IP"));
          if (storno && val.signum() > 0) val = val.negate();
          else if (!storno && val.signum() < 0) val = val.abs();
        }
        /*
        BigDecimal val = ds.getBigDecimal(ds.getString("DUGPOT").equalsIgnoreCase("D") ? "ID" : "IP");
        // bugfix: ako je stavka na krivoj strani, promijeni joj predznak
        // za ulazne racune sve stavke osim ukupnog iznosa moraju biti na dugovnoj
        // strani, za izlazne obratno
        if (ds.getInt("RBS") != 1 && !ds.getString("DUGPOT").equalsIgnoreCase(reqSide))
          val = val.negate();*/
        
        uraira.setBigDecimal(col, uraira.getBigDecimal(col).add(val));
        sumy.put(col, yval.add(val));
        summ.put(col, mval.add(val));
      }
    }
    if (lastpri == null) return;
    if (addSums && !skipSum) {
      insertSums(lastpri, lastpri, summ, ++broj);
      if (!ut.getMonth(lastpri).equals(ut.getMonth(firsty)))
        insertSums(firsty, lastpri, sumy, broj);
    }
    uraira.post();
    if (addSums)
      uraira.setSort(new SortDescriptor(new String[] {"BROJ", "SORTER"}));
    else uraira.setSort(new SortDescriptor(new String[] {"RBS"}));
    rbr = skiprbr;
    for (uraira.first(); uraira.inBounds(); uraira.next())
      if (uraira.getInt("RBS") > 0)
        uraira.setInt("RBS", ++rbr);
    
    if (!addSums) findErrors(ulaz);
  }

  private void jbInit() throws Exception {
//    setSelDataSet(dm.get);
    jpDetail.setLayout(lay);
    lay.setWidth(545);
    lay.setHeight(115);

    fset.setColumns(new Column[] {
      dM.createStringColumn("MFROM", "Po\u010Detni mjesec", 2),
      dM.createStringColumn("MTO", "Krajnji mjesec", 2),
      dM.createStringColumn("GOD", "Godina", 4),
      (Column) dm.getSkstavke().getColumn("CKNJIGE").clone(),
      (Column) dm.getKnjigeUI().getColumn("NAZKNJIGE").clone()
    });
    fset.open();
    fset.setString("MFROM", "1");
    fset.setString("MTO", String.valueOf(Aus.getNumber(ut.getMonth(vl.getToday()))));
    fset.setString("GOD", ut.getYear(vl.getToday()));
    
    JPanel pd = new JPanel(null);
    pd.setLayout(new BoxLayout(pd, BoxLayout.X_AXIS));
    JraButton checkUI = new JraButton();
    checkUI.setText("Prika�i");
    checkUI.setIcon(raImages.getImageIcon(raImages.IMGALLBACK));
    checkUI.setMinimumSize(new Dimension(Aus.big(80), Aus.big(27)));
    checkUI.setPreferredSize(new Dimension(Aus.big(80), Aus.big(27)));
    //checkUI.setPreferredSize(new Dimension(100,25));
    pd.add(checkUI);
    okp.add(pd, BorderLayout.CENTER);
    okp.revalidate();
    okp.repaint();
    checkUI.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (busy || !Validacija()) return;
        try {
          busy = true;
          showDataSet();
        } finally {
          busy = false;
        }
      }
    });
    
    bg1.setHorizontalAlignment(SwingConstants.LEADING);
    bg1.setHorizontalTextPosition(SwingConstants.TRAILING);
//    bg1.setColumnName("URAIRA");
//    bg1.setDataSet(fSalKon.getMasterSet());
    bg1.add(jrbUraira1, " Ulaznih ra\u010Duna ", "U");
    bg1.add(jrbUraira2, " Izlaznih ra\u010Duna ", "I");
    bg1.setSelected(jrbUraira1);
    jbSelCknjige.setText("...");
    jlK.setText("Knjiga");
    jlCknjige.setText("Vrsta knjige");
    jlDatum.setText("Za mjesec (od - do)");
    jlGod.setText("Godina");
    jraMfrom.setColumnName("MFROM");
    jraMfrom.setDataSet(fset);
    jraMfrom.setHorizontalAlignment(SwingConstants.CENTER);
    jraMto.setColumnName("MTO");
    jraMto.setDataSet(fset);
    jraMto.setHorizontalAlignment(SwingConstants.CENTER);
    jraGod.setColumnName("GOD");
    jraGod.setDataSet(fset);
    jraGod.setHorizontalAlignment(SwingConstants.CENTER);

    jlrCknjige.setColumnName("CKNJIGE");
    jlrCknjige.setDataSet(fset);
    jlrCknjige.setColNames(new String[] {"NAZKNJIGE"});
    jlrCknjige.setTextFields(new JTextComponent[] {jlrNazknjige});
    jlrCknjige.setVisCols(new int[] {0, 4});
    jlrCknjige.setSearchMode(0);
    jlrCknjige.setRaDataSet(dm.getKnjigeUI());
    jlrCknjige.setNavButton(jbSelCknjige);

    jlrNazknjige.setColumnName("NAZKNJIGE");
    jlrNazknjige.setDataSet(fset);
    jlrNazknjige.setNavProperties(jlrCknjige);
    jlrNazknjige.setSearchMode(1);

    jpDetail.add(jbSelCknjige, new XYConstraints(510, 50, 21, 21));
    jpDetail.add(jlK, new XYConstraints(15, 24, -1, -1));
    jpDetail.add(jlCknjige, new XYConstraints(15, 50, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 75, -1, -1));
    jpDetail.add(jlrCknjige, new XYConstraints(150, 50, 105, -1));
    jpDetail.add(jlrNazknjige, new XYConstraints(260, 50, 245, -1));
    jpDetail.add(jraMfrom, new XYConstraints(150, 75, 50, -1));
    jpDetail.add(jraMto, new XYConstraints(205, 75, 50, -1));
    jpDetail.add(jlGod, new XYConstraints(330, 75, -1, -1));
    jpDetail.add(jraGod, new XYConstraints(430, 75, 75, -1));

    jpK.setLayout(xyK);
    xyK.setWidth(351);
    xyK.setHeight(25);
    jpK.setBorder(BorderFactory.createEtchedBorder());
    jpK.add(jrbUraira1, new XYConstraints(25, 0, 150, -1));
    jpK.add(jrbUraira2, new XYConstraints(200, 0, 150, -1));

    jpDetail.add(jpK, new XYConstraints(150, 17, -1, -1));

    jrbUraira1.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbUraira1.isSelected())
          uraSelected();
      }
    });

    jrbUraira2.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbUraira2.isSelected())
          iraSelected();
      }
    });

    this.getContentPane().add(jpDetail, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    Aus.recursiveUpdateSizes(jpDetail);
    okp.registerOKPanelKeys(this);
  }

  private void uraSelected() {
    if (lastui != 1) {
      lastui = 1;
      jlrCknjige.setRaDataSet(dm.getKnjigeU());
      jlrCknjige.setText("");
//      getSelRow().setString("CKNJIGE", "");
//      jlrNazknjige.setText("");
      jlrCknjige.forceFocLost();
      bg1.setSelected(jrbUraira1);
    }
  }

  private void iraSelected() {
    if (lastui != 2) {
      lastui = 2;
      jlrCknjige.setRaDataSet(dm.getKnjigeI());
      jlrCknjige.setText("");
//      getSelRow().setString("CKNJIGE", "");
//      jlrNazknjige.setText("");
      jlrCknjige.forceFocLost();
      bg1.setSelected(jrbUraira2);
    }
  }
  /**
   * u Pilotu
   * hr.restart.sk.raIspisUraIra.convertUICols(String oldid,null,null)
   * @param oldid
   * @param rangefrom
   * @param rangeto
   */
  public static void convertUICols(String oldid, 
      Timestamp rangefrom, Timestamp rangeto) {
    if (rangefrom == null) rangefrom = Util.getUtil().getYearBegin("2006");
    if (rangeto == null) rangeto = Util.getUtil().getYearEnd("3006");
    //backup
    File ddir = new File("ui06bak");
    ddir.mkdir();
    int dcnt;
    dcnt = StIzvjPDV.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" StIzvjPDV");
    
    dcnt = IzvjPDV.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" IzvjPDV");

    dcnt = KoloneknjUI.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" KoloneknjUI");
    
    dcnt = Shkonta.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" Shkonta");
    
    //OSNOVNI PODACI
    //izvjpdv.ciz, stizvjpdv.ciz +oldid
    String cizfilter = "CIZ like 'I%'"; 
    QueryDataSet stizvjpdv = StIzvjPDV.getDataModule().getTempSet("CIZ like 'I%' or CIZ like 'V%'");
    QueryDataSet izvjpdv = IzvjPDV.getDataModule().getTempSet("CIZ like 'I%' or CIZ like 'V%'");
    addIdPrefix(izvjpdv, oldid);
    addIdPrefix(stizvjpdv, oldid);
    
    //koloneknjui
    QueryDataSet kolone = KoloneknjUI.getDataModule().getTempSet("ckolone < 1000");
    kolone.open();
    for (kolone.first(); kolone.inBounds(); kolone.next()) {
      kolone.setShort("CKOLONE", (short)(kolone.getShort("CKOLONE")+5000));
      kolone.post();
    }
    
    //shkonta where app='sk'.polje
    QueryDataSet shkontask = Shkonta.getDataModule().getTempSet("app = 'sk'");
    shkontask.open();
    for (shkontask.first(); shkontask.inBounds(); shkontask.next()) {
      try {
        int ckol = new Integer(shkontask.getString("POLJE").trim()).intValue();
        String vrdok = shkontask.getString("VRDOK");
        char uraira = ' ';
        if (vrdok.equals("URN") || vrdok.equals("OKD")) {
          uraira = 'U';
        } else if (vrdok.equals("IRN") || vrdok.equals("OKK")) {
          uraira = 'I';
        }
        shkontask.setString("POLJE", convertCkol06(ckol, uraira)+"");
        shkontask.post();
      } catch (Exception e) {
        System.out.println(e);
      }
      shkontask.post();
    }
    
    //shkonta.ckolone za ostale
    QueryDataSet shkontaresto = Shkonta.getDataModule().getTempSet("ckolone!=0 and ckolone is not null and app!='sk'");
    shkontaresto.open();
    for (shkontaresto.first(); shkontaresto.inBounds(); shkontaresto.next()) {
      try {
        int ckol = (int)shkontaresto.getShort("CKOLONE");
        char uraira=TypeDoc.getTypeDoc().isDocUlaz(shkontaresto.getString("VRDOK"))?'U':'I';
        shkontaresto.setShort("CKOLONE", (short)convertCkol06(ckol, uraira));
        shkontaresto.post();
      } catch (Exception e) {
        System.out.println(e);
      }
    }
    
    //PROMETI
    //uistavke.ckolone
    QueryDataSet uistavke = UIstavke.getDataModule().getTempSet(
        "EXISTS (SELECT * FROM skstavke where uistavke.knjig = skstavke.knjig"
        +" AND uistavke.cpar = skstavke.cpar AND uistavke.vrdok = skstavke.vrdok"
        +" AND uistavke.brojdok = skstavke.brojdok and "
        +Condition.between("DATPRI",rangefrom,rangeto)+")");
 
    processPromet(uistavke);
    
    //skstavkerad.ckolone
    QueryDataSet skstavkerad = Skstavkerad.getDataModule().getTempSet(Condition.between("DATPRI",rangefrom,rangeto));
    
    processPromet(skstavkerad);
    
//    //konverzija izvjpdv i stizvjpdv vec sa prefixom dodati bez prefixa po novom
//    QueryDataSet izvjpdvnew = IzvjPDV.getDataModule().getTempSet("0=1");
//    for (izvjpdv.first(); izvjpdv.inBounds(); izvjpdv.next()) {
//      String ciz = new VarStr(izvjpdv.getString("CIZ")).leftChop(oldid.length()).toString();
//      if (ciz.startsWith("II.5.")) continue; //nema vise
//      izvjpdvnew.insertRow(false);
//      izvjpdv.copyTo(izvjpdvnew);
//      izvjpdvnew.setString("CIZ", ciz);
//      if (ciz.equals("II.3.v")) izvjpdvnew.setString("OPIS", "NENAPLA�ENI IZVOZ - vrijednost");
//      if (ciz.equals("II.3.p")) izvjpdvnew.setString("OPIS", "NENAPLA�ENI IZVOZ - porez");
//      if (ciz.equals("II.4.v")) izvjpdvnew.setString("OPIS", "NAKNADNO OSLOBO�ENJE IZVOZA U OKVIRU OSOBNOG PUTNI�KOG PROMETA - vrijednost");
//      if (ciz.equals("II.4.p")) izvjpdvnew.setString("OPIS", "NAKNADNO OSLOBO�ENJE IZVOZA U OKVIRU OSOBNOG PUTNI�KOG PROMETA - porez");
//      
//      izvjpdvnew.post();
//    }
    
    
    //kraj
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {
        stizvjpdv,
        izvjpdv,
        kolone,
        shkontask,
        shkontaresto,
        uistavke,
        skstavkerad
    });
    
    //uloadaj izvjpdv, stizvjpdv, koloneknjui iz uraira06
    File ldir = new File("uraira06");
    try {
      IzvjPDV.getDataModule().insertData(ldir);
      StIzvjPDV.getDataModule().insertData(ldir);
      KoloneknjUI.getDataModule().insertData(ldir);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static void convert23(String oldid, 
      Timestamp rangefrom, Timestamp rangeto) {
    if (rangefrom == null) rangefrom = Util.getUtil().getYearBegin("2006");
    if (rangeto == null) rangeto = Util.getUtil().getYearEnd("3006");
    //backup
    File ddir = new File("ui09bak");
    ddir.mkdir();
    int dcnt;
    dcnt = StIzvjPDV.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" StIzvjPDV");
    
    dcnt = IzvjPDV.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" IzvjPDV");

    dcnt = KoloneknjUI.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" KoloneknjUI");
    
    dcnt = Shkonta.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" Shkonta");
    
    //OSNOVNI PODACI
    //izvjpdv.ciz, stizvjpdv.ciz +oldid
    String cizfilter = "CIZ like 'I%'"; 
    QueryDataSet stizvjpdv = StIzvjPDV.getDataModule().getTempSet("CIZ like 'I%' or CIZ like 'V%'");
    /*QueryDataSet izvjpdv = IzvjPDV.getDataModule().getTempSet("CIZ like 'I%' or CIZ like 'V%'");
    addIdPrefix(izvjpdv, oldid);*/
//    addIdPrefix(stizvjpdv, oldid);
    processPromet09(stizvjpdv);
    
    //koloneknjui
    QueryDataSet kolone = KoloneknjUI.getDataModule().getTempSet("ckolone < 1000");
    kolone.open();
    for (kolone.first(); kolone.inBounds(); kolone.next()) {
      kolone.setShort("CKOLONE", (short)(kolone.getShort("CKOLONE")+9000));
      kolone.post();
    }
    
    //shkonta where app='sk'.polje
    QueryDataSet shkontask = Shkonta.getDataModule().getTempSet("app = 'sk'");
    shkontask.open();
    for (shkontask.first(); shkontask.inBounds(); shkontask.next()) {
      String vrdok = shkontask.getString("VRDOK");
      char uraira = ' ';
      if (vrdok.equals("URN") || vrdok.equals("OKD")) {
        uraira = 'U';
      } else if (vrdok.equals("IRN") || vrdok.equals("OKK")) {
        uraira = 'I';
      }
      try {
        int ckol = new Integer(shkontask.getString("POLJE").trim()).intValue();
        shkontask.setString("POLJE", convertCkol09(ckol, uraira)+"");
        shkontask.post();
      } catch (Exception e) {
        System.out.println(e);
      }
      shkontask.setString("POLJE", promoteCkol09(shkontask.getString("POLJE"), uraira));
      shkontask.post();
    }
    
    //shkonta.ckolone za ostale
    QueryDataSet shkontaresto = Shkonta.getDataModule().getTempSet("ckolone!=0 and ckolone is not null and app!='sk'");
    shkontaresto.open();
    for (shkontaresto.first(); shkontaresto.inBounds(); shkontaresto.next()) {
      try {
        int ckol = (int)shkontaresto.getShort("CKOLONE");
        char uraira=TypeDoc.getTypeDoc().isDocUlaz(shkontaresto.getString("VRDOK"))?'U':'I';
        shkontaresto.setShort("CKOLONE", (short)convertCkol09(ckol, uraira));
        shkontaresto.setShort("CKOLONE", Short.parseShort(promoteCkol09(shkontaresto.getShort("CKOLONE")+"", uraira)));
        shkontaresto.post();
      } catch (Exception e) {
        System.out.println(e);
      }
    }
    
    //PROMETI
    //uistavke.ckolone
    QueryDataSet uistavke = UIstavke.getDataModule().getTempSet(
        "EXISTS (SELECT * FROM skstavke where uistavke.knjig = skstavke.knjig"
        +" AND uistavke.cpar = skstavke.cpar AND uistavke.vrdok = skstavke.vrdok"
        +" AND uistavke.brojdok = skstavke.brojdok and "
        +Condition.between("DATPRI",rangefrom,rangeto)+")");
 
    processPromet09(uistavke);
    
    //skstavkerad.ckolone
    QueryDataSet skstavkerad = Skstavkerad.getDataModule().getTempSet(Condition.between("DATPRI",rangefrom,rangeto));
    
    processPromet09(skstavkerad);
    
//    //konverzija izvjpdv i stizvjpdv vec sa prefixom dodati bez prefixa po novom
//    QueryDataSet izvjpdvnew = IzvjPDV.getDataModule().getTempSet("0=1");
//    for (izvjpdv.first(); izvjpdv.inBounds(); izvjpdv.next()) {
//      String ciz = new VarStr(izvjpdv.getString("CIZ")).leftChop(oldid.length()).toString();
//      if (ciz.startsWith("II.5.")) continue; //nema vise
//      izvjpdvnew.insertRow(false);
//      izvjpdv.copyTo(izvjpdvnew);
//      izvjpdvnew.setString("CIZ", ciz);
//      if (ciz.equals("II.3.v")) izvjpdvnew.setString("OPIS", "NENAPLA�ENI IZVOZ - vrijednost");
//      if (ciz.equals("II.3.p")) izvjpdvnew.setString("OPIS", "NENAPLA�ENI IZVOZ - porez");
//      if (ciz.equals("II.4.v")) izvjpdvnew.setString("OPIS", "NAKNADNO OSLOBO�ENJE IZVOZA U OKVIRU OSOBNOG PUTNI�KOG PROMETA - vrijednost");
//      if (ciz.equals("II.4.p")) izvjpdvnew.setString("OPIS", "NAKNADNO OSLOBO�ENJE IZVOZA U OKVIRU OSOBNOG PUTNI�KOG PROMETA - porez");
//      
//      izvjpdvnew.post();
//    }
    
    
    //kraj
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {
        stizvjpdv,
        //izvjpdv,
        kolone,
        shkontask,
        shkontaresto,
        uistavke,
        skstavkerad
    });
    
    //uloadaj izvjpdv, stizvjpdv, koloneknjui iz uraira06
    File ldir = new File("uraira09");
    try {
      /*IzvjPDV.getDataModule().insertData(ldir);
      StIzvjPDV.getDataModule().insertData(ldir);*/
      KoloneknjUI.getDataModule().insertData(ldir);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static void convertpdv() {    
    if (IzvjPDV.getDataModule().getRowCount(
        Condition.equal("CIZ", "II.5.v")) > 0) return;
    
    convertCIZ("II.4.v", "II.5.v");
    convertCIZ("II.4.p", "II.5.p");
    convertCIZ("II.3.v", "II.4.v");
    convertCIZ("II.3.p", "II.4.p");
    
    copyCIZ("II.2.v", "II.3.v");
    copyCIZ("II.2.p", "II.3.p");
    
    convertCIZ("III.6.", "III.8.");
    convertCIZ("III.5.v", "III.6.v");
    convertCIZ("III.5.p", "III.6.p");
    convertCIZ("III.4.v", "III.5.v");
    convertCIZ("III.4.p", "III.5.p");
    convertCIZ("III.3.v", "III.4.v");
    convertCIZ("III.3.p", "III.4.p");
    
    copyCIZ("III.2.v", "III.3.v");
    copyCIZ("III.2.p", "III.3.p");
    copyCIZ("III.6.v", "III.7.v");
    copyCIZ("III.6.p", "III.7.p");
  }
  
  static void copyCIZ(String from, String to) {
    QueryDataSet zag = IzvjPDV.getDataModule().getTempSet(
        Condition.equal("CIZ", from));
    zag.open();
    
    QueryDataSet nz = IzvjPDV.getDataModule().getTempSet(Condition.nil);
    nz.open();
    nz.insertRow(false);
    nz.setString("CIZ", to);
    nz.setString("OPIS", new VarStr(zag.getString("OPIS")).
        replace("22", "23").toString());
    
    
    QueryDataSet st = StIzvjPDV.getDataModule().getTempSet(
        Condition.equal("CIZ", from));
    st.open();
    
    QueryDataSet nt = StIzvjPDV.getDataModule().getTempSet(Condition.nil);
    nt.open();
    
    for (st.first(); st.inBounds(); st.next()) {
      nt.insertRow(false);
      st.copyTo(nt);
      nt.setString("CIZ", to);
      if ("U".equals(nt.getString("URAIRA"))) {
        if (nt.getShort("CKOLONE") == 8)
          nt.setShort("CKOLONE", (short) 9);
        if (nt.getShort("CKOLONE") == 14)
          nt.setShort("CKOLONE", (short) 16);
      } else {
        if (nt.getShort("CKOLONE") == 14)
          nt.setShort("CKOLONE", (short) 16);
        if (nt.getShort("CKOLONE") == 15)
          nt.setShort("CKOLONE", (short) 17);
      }
    }
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {nz, nt});
  }
  
  static void convertCIZ(String from, String to) {
    QueryDataSet zag = IzvjPDV.getDataModule().getTempSet(
        Condition.equal("CIZ", from));
    QueryDataSet st = StIzvjPDV.getDataModule().getTempSet(
        Condition.equal("CIZ", from));
    zag.open();
    zag.setString("CIZ", to);
    
    st.open();
    for (st.first(); st.inBounds(); st.next()) {
      st.setString("CIZ", to);
    }
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {zag,st});
  }

  private static String promoteCkol09(String polje, char uraira2) {
    if (uraira2=='I') {
      if (polje.trim().equals("14")) return "16";
      if (polje.trim().equals("15")) return "17";
    } 
    if (uraira2=='U') {
      if (polje.trim().equals("8")) return "9";
      if (polje.trim().equals("14")) return "16";
      //R-2 - popusio je u convertCkol09
      if (polje.trim().equals("R13")) return "R16";
    } 
    
    return polje;
  }

  private static void processPromet(QueryDataSet set) {
    set.open();
    for (set.first(); set.inBounds(); set.next()) {
      int ckol = (int)set.getShort("CKOLONE");
      char uraira = set.getString("URAIRA").charAt(0);
      set.setShort("CKOLONE", (short)convertCkol06(ckol, uraira));
      set.post();
    }
  }
  
  private static void processPromet09(QueryDataSet set) {
    set.open();
    for (set.first(); set.inBounds(); set.next()) {
      int ckol = (int)set.getShort("CKOLONE");
      char uraira = set.getString("URAIRA").charAt(0);
      set.setShort("CKOLONE", (short)convertCkol09(ckol, uraira));
      set.post();
    }
  }

  private static int convertCkol06(int ckol, char uraira) {
    if (uraira == 'U') {
      switch (ckol) {
        case 6: return 8;
        case 7: return 9;
        case 8: return 508;
        case 9: return 13;
        case 10: return 14;
        case 11: return 511;
        case 12: return 512;
      }
    } else if (uraira == 'I') {
      switch (ckol) {
        case 9: return 10;
        case 10: return 11;
        case 11: return 14;
        case 12: return 15;
        case 13: return 14;
        case 14: return 15;
      }      
    }
    return ckol;
  }
  
  private static int convertCkol09(int ckol, char uraira) {
    if (uraira == 'U') {
      if (ckol >= 9 && ckol <= 14)
        return ckol + 1;
    } 
    return ckol;
  }

  private static void addIdPrefix(QueryDataSet set, String id) {
    set.open();
    int cnt = 0;
    for (set.first(); set.inBounds(); set.next()) {
      set.setString("CIZ", id.trim().concat(set.getString("CIZ").trim()));
      set.post();
      cnt++;
    }
    //set.saveChanges();
    System.out.println(set.getTableName()+" :: dodan prefix "+id+" na "+cnt+" slogova");
  }
  
  
  public static void convertUIcolsEU() {
    Timestamp rangefrom, rangeto;
    //01.07.13 na dalje
    Calendar c = Calendar.getInstance();
    c.set(2013, 6, 1);
    rangefrom = new Timestamp(c.getTime().getTime());
    c.set(9999, 11,31);
    rangeto = new Timestamp(c.getTime().getTime());
    //backup
    File ddir = new File("ui13bak");
    ddir.mkdir();
    int dcnt;
    dcnt = StIzvjPDV.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" StIzvjPDV");
    
    dcnt = IzvjPDV.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" IzvjPDV");

    dcnt = KoloneknjUI.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" KoloneknjUI");
    
    dcnt = Shkonta.getDataModule().dumpTable(ddir);
    System.out.println("Backupirano "+dcnt+" Shkonta");
    
    //OSNOVNI PODACI
    //izvjpdv.ciz, stizvjpdv.ciz +oldid
    // ne treba - radi se nova definicija vezana za XML shemu, dok stara ostaje
//    String cizfilter = "CIZ like 'I%'"; 
//    QueryDataSet stizvjpdv = StIzvjPDV.getDataModule().getTempSet("CIZ like 'I%' or CIZ like 'V%'");
//    QueryDataSet izvjpdv = IzvjPDV.getDataModule().getTempSet("CIZ like 'I%' or CIZ like 'V%'");
//    addIdPrefix(izvjpdv, oldid);
//    addIdPrefix(stizvjpdv, oldid);
    
    //koloneknjui
    QueryDataSet kolone = KoloneknjUI.getDataModule().getTempSet(Condition.between("CKOLONE", 1, 1000));
        //"ckolone < 1000");
    kolone.open();
    for (kolone.first(); kolone.inBounds(); kolone.next()) {
      kolone.setShort("CKOLONE", (short)(kolone.getShort("CKOLONE")+13000));
      kolone.post();
    }
    
    //shkonta where app='sk'.polje
    QueryDataSet shkontask = Shkonta.getDataModule().getTempSet("app = 'sk'");
    shkontask.open();
    for (shkontask.first(); shkontask.inBounds(); shkontask.next()) {
      try {
        int ckol = new Integer(shkontask.getString("POLJE").trim()).intValue();
        String vrdok = shkontask.getString("VRDOK");
        char uraira = ' ';
        if (vrdok.equals("URN") || vrdok.equals("OKD")) {
          uraira = 'U';
        } else if (vrdok.equals("IRN") || vrdok.equals("OKK")) {
          uraira = 'I';
        }
        shkontask.setString("POLJE", convertCkol13(ckol, uraira)+"");
        shkontask.post();
      } catch (Exception e) {
        System.out.println(e);
      }
      shkontask.post();
    }
    
    //shkonta.ckolone za ostale
    QueryDataSet shkontaresto = Shkonta.getDataModule().getTempSet("ckolone!=0 and ckolone is not null and app!='sk'");
    shkontaresto.open();
    for (shkontaresto.first(); shkontaresto.inBounds(); shkontaresto.next()) {
      try {
        int ckol = (int)shkontaresto.getShort("CKOLONE");
        char uraira=TypeDoc.getTypeDoc().isDocUlaz(shkontaresto.getString("VRDOK"))?'U':'I';
        shkontaresto.setShort("CKOLONE", (short)convertCkol13(ckol, uraira));
        shkontaresto.post();
      } catch (Exception e) {
        System.out.println(e);
      }
    }
    
    //PROMETI
    //uistavke.ckolone
    QueryDataSet uistavke = UIstavke.getDataModule().getTempSet(
        "EXISTS (SELECT * FROM skstavke where uistavke.knjig = skstavke.knjig"
        +" AND uistavke.cpar = skstavke.cpar AND uistavke.vrdok = skstavke.vrdok"
        +" AND uistavke.brojdok = skstavke.brojdok and "
        +Condition.between("DATPRI",rangefrom,rangeto)+")");
 
    processPromet13(uistavke);
    
    //skstavkerad.ckolone
    QueryDataSet skstavkerad = Skstavkerad.getDataModule().getTempSet(Condition.between("DATPRI",rangefrom,rangeto));
    
    processPromet13(skstavkerad);
    
//    //konverzija izvjpdv i stizvjpdv vec sa prefixom dodati bez prefixa po novom
//    QueryDataSet izvjpdvnew = IzvjPDV.getDataModule().getTempSet("0=1");
//    for (izvjpdv.first(); izvjpdv.inBounds(); izvjpdv.next()) {
//      String ciz = new VarStr(izvjpdv.getString("CIZ")).leftChop(oldid.length()).toString();
//      if (ciz.startsWith("II.5.")) continue; //nema vise
//      izvjpdvnew.insertRow(false);
//      izvjpdv.copyTo(izvjpdvnew);
//      izvjpdvnew.setString("CIZ", ciz);
//      if (ciz.equals("II.3.v")) izvjpdvnew.setString("OPIS", "NENAPLA�ENI IZVOZ - vrijednost");
//      if (ciz.equals("II.3.p")) izvjpdvnew.setString("OPIS", "NENAPLA�ENI IZVOZ - porez");
//      if (ciz.equals("II.4.v")) izvjpdvnew.setString("OPIS", "NAKNADNO OSLOBO�ENJE IZVOZA U OKVIRU OSOBNOG PUTNI�KOG PROMETA - vrijednost");
//      if (ciz.equals("II.4.p")) izvjpdvnew.setString("OPIS", "NAKNADNO OSLOBO�ENJE IZVOZA U OKVIRU OSOBNOG PUTNI�KOG PROMETA - porez");
//      
//      izvjpdvnew.post();
//    }
    
    
    //kraj
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {
//        stizvjpdv,
//        izvjpdv,
        kolone,
        shkontask,
        shkontaresto,
        uistavke,
        skstavkerad
    });
    
    //uloadaj izvjpdv, stizvjpdv, koloneknjui iz uraira06
    File ldir = new File("uraira13");
    try {
      IzvjPDV.getDataModule().insertData(ldir);
      StIzvjPDV.getDataModule().insertData(ldir);
      KoloneknjUI.getDataModule().insertData(ldir);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  //prebac iz starih u nove kolone za promete od 01.07.2013 i sheme knjizenja
  //trauma: izdvojiti 5 i 10%  
  private static int convertCkol13(int ckol, char uraira) {
    if (uraira == 'U') {
      switch (ckol) {
//        case 6: return 6; // 5 i 10% razdvojiti poslije
//        case 7: return 7;
      case 7: return 7; // // 5 i 10% -> 10% razdvojiti poslije
        case 8: return 8;
        case 9: return 8;
        case 10: return 9;
        case 12: return 13; // 5 i 10% -> 10% razdvojiti poslije
        case 13: return 14; // 5 i 10% -> 10% razdvojiti poslije
        case 14: return 15;
        case 15: return 16;
        case 16: return 15;
        case 17: return 16;
      }
    } else if (uraira == 'I') {
      switch (ckol) {
        case 7: return 14;
        case 8: return 15;
        case 9: return 14;
        case 10: return 16;
        case 11: return 16;
        case 12: return 19; // 5 i 10% -> 10% razdvojiti poslije
        case 13: return 20; // 5 i 10% -> 5% razdvojiti poslije
        case 14: return 21;
        case 15: return 22;
        case 16: return 21;
        case 17: return 22;
      }      
    }
    return ckol;
  }
  private static void processPromet13(QueryDataSet set) {
    set.open();
    for (set.first(); set.inBounds(); set.next()) {
      int ckol = (int)set.getShort("CKOLONE");
      char uraira = set.getString("URAIRA").charAt(0);
      set.setShort("CKOLONE", (short)convertCkol13(ckol, uraira));
      set.post();
    }
  }
  
  public static QueryDataSet cnvpdv2014() {
    QueryDataSet iset = Aus.q("SELECT * FROM StIzvjPDV WHERE ciz like 'Pod%'");
    QueryDataSet nset = Aus.q("SELECT * FROM StIzvjPDV WHERE ciz like 'Pdv%'");
    for (iset.first();iset.inBounds();iset.next()) {
      nset.insertRow(false);
      iset.copyTo(nset);
      String ciz = iset.getString("CIZ");
      int piz = Integer.parseInt(ciz.substring(3,6));
      String siz = "";
      if (ciz.length() > 6) siz=ciz.substring(6);
      
      if (piz==302) 
        piz = 304;
      else if (piz==306) 
        piz=314;
      else if (piz==307) 
        piz=315;
      else if (piz==301 || piz==303 || piz==304 || piz==305) {
         int a = 0;
         if (piz==301) {
           a=0;
         } else if (piz==303) {
           a=2;
         } else if (piz==304) {
           a=4;
         } else if (piz==305) {
           a=6;
         }
         short ckol = iset.getShort("CKOLONE");
        switch (ckol) { 
          case 6:
            piz = piz+a;
            break;
    
          case 7:
            piz = piz+a+1;
            break;
            
          case 8:
            piz = piz+a+2;
            break;
           
          case 13:
            piz = piz+a+1;
            break;
    
          case 14:
            piz = piz+a+1;
            break;
    
          case 15:
            piz = piz+a+2;
            break;
    
          case 16:
            piz = piz+a+2;
            break;
    
          default:
            break;
        }//switch
         
      }//if
  
      nset.setString("CIZ","Pdv"+piz+siz);
      nset.post();
    }//for
    return nset;
  }
}