/****license*****************************************************************
**   file: repDynamicProvider.java
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

import sg.com.elixir.reportwriter.datasource.*;

import hr.restart.baza.dM;
import hr.restart.swing.dataSetTableModel;
import hr.restart.swing.JraTableInterface;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.columnsbean.ColumnsBean;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raJPTableView;
import hr.restart.robno.repMemo;
import hr.restart.robno.raDateUtil;

import java.util.Enumeration;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import java.math.BigDecimal;

import com.borland.dx.dataset.Variant;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Column;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repDynamicProvider implements IDataProvider {
  static repDynamicProvider inst;
  static repMemo rpm = repMemo.getrepMemo();
  static raDateUtil rdu = raDateUtil.getraDateUtil();
  static Valid vl = Valid.getValid();
  static raElixirProperties ep = raElixirPropertiesInstance.get();
  static raElixirPropertyValues ev = raElixirPropertyValuesInstance.get();

  ColumnsBean bean;
  JraTableInterface table;
  raExtendedTable xt;
  JTable jt;
  TableColumnModel model;
  String title;
  String subtitle;
  Variant v;
  
  JTable summ;

  boolean sums;


  public repDynamicProvider() {
    inst = this;
  }

  public static repDynamicProvider getInstance() {
    if (inst == null) new repDynamicProvider();
    return inst;
  }

  public String getGroupCaption(int n) {
    if (xt != null && n < xt.getTotalGroupCount() && n >= 0) {
      return xt.getDataSet().hasColumn(xt.getGroup(n)).getCaption().concat(":");
    } else return "";
  }

  public String getGroup(int row, int n) {
    if (xt != null && n < xt.getTotalGroupCount() && n >= 0) {
      xt.getDataSet().getVariant(xt.getGroup(n), row, v);
      return v.toString().toUpperCase();
    } else return "";
  }

  public String getGroupValue(int row, int n) {
    if (xt != null && n < xt.getTotalGroupCount() && n >= 0) {
      if (xt.getGroupDesc(n) == null || 
          (xt.getGroupGet(n) == null && xt.getGroupGetDs(n) == null) ||
          (xt.getGroupGet(n) != null && xt.getGroupGet(n).length() > 0 && !dM.isDataSetGetter(xt.getGroupGet(n)))) 
        return getGroup(row, n);
      xt.getDataSet().getVariant(xt.getGroup(n), row, v);
      DataSet ds = null;
      if (xt.getGroupGet(n) == null || xt.getGroupGet(n).length() > 0)
        ds = xt.getGroupGetDs(n) != null ? xt.getGroupGetDs(n) :
          dM.getDataByName(xt.getGroupGet(n));
      if (ds != null) {
        ds.open();
        lookupData.getlookupData().raLocate(ds, xt.getGroup(n), v.toString());
      }
      VarStr full = new VarStr(xt.getDataSet().getColumn(xt.getGroup(n)).format(v));
      for (int i = 0; i < xt.getGroupDesc(n).length; i++) {
      	if (xt.getGroupDesc(n)[i].equals("#")) full.clear();
      	else if (xt.getGroupDesc(n)[i].startsWith("#"))
      		full.append(xt.getGroupDesc(n)[i].substring(1));
      	else if (ds != null){
      		ds.getVariant(xt.getGroupDesc(n)[i], v);
      		full.append(' ').append(
      		  ds.getColumn(xt.getGroupDesc(n)[i]).format(v));
      	}
      }
      return full.toString();
    } else return "";
  }

  public String getValueAt(int row, int col) {
    return ((JLabel) getRenderComp(row, col)).getText();
  }

  public double getDoubleAt(int row, int col) {
    Object o = ((JraTable2) jt).getValueAt(row, col);
//    System.out.println("double: "+row+"  "+col);
//    hr.restart.util.Aus.dumpClassName(o);
    if (o instanceof Number)
      return ((Number) o).doubleValue();
    else return 0;
  }

  private boolean isRowNumberSum(int col) {
    dataSetTableModel m = (dataSetTableModel) table.getModel();
//    System.out.println(col);
//    System.out.println(((dataSetTableModel) table.getModel()).getColumnName(jt.convertColumnIndexToModel(col)));
//    System.out.println(((dataSetTableModel) table.getModel()).getTableSumRow().getColsForSum());
    return m.getTableSumRow().getColsForSum().contains(
        m.getCols()[jt.convertColumnIndexToModel(col)].getColumnName());
  }

  private boolean isRowFloatSum(int col) {
    int ty = table.getDataSet().getColumn(((dataSetTableModel) table.getModel()).
             getCols()[jt.convertColumnIndexToModel(col)].getColumnName()).getDataType();
    return (ty == Variant.DOUBLE || ty == Variant.FLOAT || ty == Variant.BIGDECIMAL);
  }

  private Component getRenderComp(int row, int col) {
    return jt.getDefaultRenderer(Object.class).getTableCellRendererComponent(jt,
           jt.getValueAt(row, col), false, false, row, col);
  }

  public String getTitle() {
    return "\n".concat(title);
  }

  public String getSubtitle() {
    if (subtitle.length() == 0) return "";
    return "\n".concat(subtitle);
  }
  
  public void prepareReport(JraTableInterface tab, String xtitle) {
    table = tab;
    jt = (JTable) table;
    xt = (jt instanceof raExtendedTable ? (raExtendedTable) jt : null);
    table = (JraTableInterface) jt;
    model = table.getColumnModel();
    sums = ((dataSetTableModel) table.getModel()).getTableSumRow() != null &&
           ((dataSetTableModel) table.getModel()).getTableSumRow().isSumingEnabled();
    v = new Variant();
    createTitles(xtitle);
  }

  public raReportTemplate createDynamicReport(ColumnsBean colb, String xtitle) {
    bean = colb;
    prepareReport(bean.getRaJdbTable(), xtitle);
    return createTemplate();
  }
  
  public boolean hasSumRow() {
    return sums;
  }

  public void activate() {
    inst = this;
  }

  boolean test() {
    return hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)").equals("D");
  }

  public Enumeration getData() {
    return new Enumeration() {
      private int idx = 0;
      public boolean hasMoreElements()  {
//      if (arrIdx > 100) return false;
        return idx < (table.getRowCount() - (inst.sums ? 1 : 0));
      }
      public Object nextElement() {
        if (!hasMoreElements()) return null;
        return new raDynamicTableData(idx++);
      }
    };
  }

  public void close() {
  }

  private void createTitles(String tt) {
    String[] breaks = new String[] {"  "," za period", " u periodu", " od "};
    String ttl = tt.toLowerCase();
    int breakPoint = -1, idx = 0;
    while (idx < breaks.length && breakPoint == -1)
      breakPoint = ttl.indexOf(breaks[idx++]);

    if (breakPoint == -1) {
      title = tt;
      subtitle = "";
    } else {
      title = tt.substring(0, breakPoint).trim();
      subtitle = tt.substring(breakPoint).trim();
    }
  }

  private raReportTemplate createTemplate() {
    raReportTemplate temp;
    int width = model.getTotalColumnWidth();
    int cols = model.getColumnCount();

    double ratio;
    if ((cols > 7) || (width > 800)) {
      repLandscapeTableTemplate lt = new repLandscapeTableTemplate();
      temp = lt;
      ratio = ((double) lt.PageSetup.getHeight() - lt.PageSetup.getLeft() * 2
                                                 - 20 * (cols - 1)) / width;
      int w = createHD(ratio, lt.SectionHeader1, lt.TextHeaderValue0) - 20;
      createHD(ratio, lt.Detail, lt.TextDataValue0);
      createFooter(ratio, lt.SectionFooter1, lt.TextSumValue0, "U K U P N O");

      if (subtitle.length() == 0)
        lt.SectionHeader0.setHeight(760);

      if (xt != null && xt.getTotalGroupCount() > 0) {
        createGroups(temp, lt.Sections, lt.SectionHeader1, lt.SectionFooter1, lt.TextDataValue0);
        if (xt.isForcePage()) {
        	lt.SectionHeader0.setProperty(ep.REPEAT, ev.YES);
        	lt.SectionHeader0.setProperty(ep.FORCE_NEW, ev.BEFORE);
        	lt.SectionFooter1.setProperty(ep.FORCE_NEW, ev.AFTER);
        }
        if (sums && !(xt != null && xt.isForcePage())) {
          lt.Section0.setProperty(ep.GROUP_FOOTER, ev.YES);
          createFooter(ratio, lt.SectionFooter0, lt.TextTSumValue0, "S V E U K U P N O");
          lt.SectionFooter0.setHeight(400);
          lt.Line5.setWidth(w);
          lt.Line6.setWidth(w);
        } else if (xt.getOwner() != null &&
            xt.getOwner().getSummary() instanceof JraTable2) {
          defineSummary(15, 0, lt.Section0, lt.SectionFooter0,
              lt.TextHeaderValue0, lt.TextDataValue0);
        } else lt.SectionFooter0.setTransparent(true);
        
      } else if (xt.getOwner() != null &&
          xt.getOwner().getSummary() instanceof JraTable2) {
        defineSummary(15, 300, lt.Section0, lt.SectionFooter0,
            lt.TextHeaderValue0, lt.TextDataValue0);
      } else lt.SectionFooter0.setTransparent(true);

      lt.Line1.setWidth(w);
      lt.Line2.setWidth(w);
      if (sums) {
        lt.Line3.setWidth(w);
        lt.Line4.setWidth(w);
      }
    } else {
      repPortraitTableTemplate lt = new repPortraitTableTemplate();
      temp = lt;
      ratio = ((double) lt.PageSetup.getWidth() - lt.PageSetup.getLeft() * 2
                                                - 20 * (cols - 1)) / width;
      int w = createHD(ratio, lt.SectionHeader1, lt.TextHeaderValue0) - 20;
      createHD(ratio, lt.Detail, lt.TextDataValue0);
      createFooter(ratio, lt.SectionFooter1, lt.TextSumValue0, "U K U P N O");

      if (subtitle.length() == 0)
        lt.SectionHeader0.setHeight(760);
      if (xt != null && xt.getTotalGroupCount() > 0) {
        createGroups(temp, lt.Sections, lt.SectionHeader1, lt.SectionFooter1, lt.TextDataValue0);
        if (xt.isForcePage()) {
        	lt.SectionHeader0.setProperty(ep.REPEAT, ev.YES);
        	lt.SectionHeader0.setProperty(ep.FORCE_NEW, ev.BEFORE);
        	lt.SectionFooter1.setProperty(ep.FORCE_NEW, ev.AFTER);
        }
        if (sums && !(xt != null && xt.isForcePage())) {
          lt.Section0.setProperty(ep.GROUP_FOOTER, ev.YES);
          createFooter(ratio, lt.SectionFooter0, lt.TextTSumValue0, "S V E U K U P N O");
          lt.SectionFooter0.setHeight(400);
          lt.Line5.setWidth(w);
          lt.Line6.setWidth(w);
        } else if (xt.getOwner() != null &&
            xt.getOwner().getSummary() instanceof JraTable2) {
          defineSummary(15, 0, lt.Section0, lt.SectionFooter0,
              lt.TextHeaderValue0, lt.TextDataValue0);
        } else lt.SectionFooter0.setTransparent(true);
      } else if (xt.getOwner() != null &&
          xt.getOwner().getSummary() instanceof JraTable2) {
        defineSummary(15, 300, lt.Section0, lt.SectionFooter0,
            lt.TextHeaderValue0, lt.TextDataValue0);
      } else lt.SectionFooter0.setTransparent(true);

      lt.Line1.setWidth(w);
      lt.Line2.setWidth(w);
      if (sums) {
        lt.Line3.setWidth(w);
        lt.Line4.setWidth(w);
      }
    }
//    hr.restart.util.Aus.dumpModel(temp.getReportTemplate(), 0);
    return temp;
  }
  
  private void defineSummary(double ratio, int y, raReportElement sect, 
      raReportSection foot, raReportElement head, raReportElement data) {
    summ = (JraTable2) xt.getOwner().getSummary();
    sect.setProperty(ep.GROUP_FOOTER, ev.YES);
    while (foot.getModelCount() > 0) foot.removeModel(0);
    boolean heads = summ.getTableHeader().getParent() == summ.getParent();
    if (heads) {
      raReportElement shd = foot.addModel(ep.TEXT, 
          (String[]) head.getDefaults().clone());
      shd.defaultAlterer().setTop(y);
      shd.defaultAlterer().setControlSource("SummaryHead0");
      shd.restoreDefaults();
      createSHD(ratio, foot, shd);
    }
    raReportElement sd = foot.addModel(ep.TEXT, 
        (String[]) data.getDefaults().clone());
    sd.defaultAlterer().setTop(y + (heads ? 300 : 0));
    sd.defaultAlterer().setControlSource("SummaryData0");
    sd.restoreDefaults();
    createSHD(ratio, foot, sd);
    
    foot.setHeight(y + (heads ? 600 : 300));
  }

  private long defineElem(long x, double ratio, int num, raReportElement e) {
    int width = model.getColumn(num).getWidth();
    e.setLeft(x);
    e.setWidth((long) (width * ratio));
    String cs = e.getControlSource();
    e.setControlSource(cs.substring(0, cs.length() - 1).concat(Integer.toString(num)));
    if (xt != null && cs.startsWith("SumValue") && xt.getGroupCount() > 0
        && isRowNumberSum(num)) {
      e.setControlSource("=(dsum \"DataNum"+num+"\")");
      if (isRowFloatSum(num))
        e.setProperty(ep.FORMAT, "Number|false|1|309|2|2|true|3|false");
      else
        e.setProperty(ep.FORMAT, "Number|false|1|309|0|0|true|0|false");
    }
    if (cs.startsWith("DataValue") || cs.startsWith("SumValue")) {
      try {
        int align = ((JLabel) getRenderComp(0, num)).getHorizontalAlignment();
        if (align == JLabel.CENTER) e.setProperty(ep.ALIGN, ev.CENTER);
        else if (align == JLabel.LEFT || align == JLabel.LEADING)
          e.setProperty(ep.ALIGN, ev.LEFT);
      } catch (Exception ex) {}
//      ((JraTable2) table).getDefaultRenderer(Object.class).getTableCellRendererComponent()
    }
//    ((JraTable2) table).getDataSetColumn(num).getDataType()
    return x + e.getWidth() + 20;
  }
  
  private long defineElemS(long x, double ratio, int num, raReportElement e) {
    int width = summ.getColumnModel().getColumn(num).getWidth();
    e.setLeft(x);
    e.setWidth((long) (width * ratio));
    String cs = e.getControlSource();
    e.setControlSource(cs.substring(0, cs.length() - 1).concat(Integer.toString(num)));
    /*if (xt != null && cs.startsWith("SumValue") && xt.getGroupCount() > 0
        && isRowNumberSum(num)) {
      e.setControlSource("=(dsum \"DataNum"+num+"\")");
      if (isRowFloatSum(num))
        e.setProperty(ep.FORMAT, "Number|false|1|309|2|2|true|3|false");
      else
        e.setProperty(ep.FORMAT, "Number|false|1|309|0|0|true|0|false");
    }*/
    if (cs.startsWith("SummaryData")) {
      try {
        int align = ((JLabel) summ.getDefaultRenderer(Object.class).getTableCellRendererComponent(summ,
            summ.getValueAt(0, num), false, false, 0, num)).getHorizontalAlignment();
        if (align == JLabel.CENTER) e.setProperty(ep.ALIGN, ev.CENTER);
        else if (align == JLabel.LEFT || align == JLabel.LEADING)
          e.setProperty(ep.ALIGN, ev.LEFT);
      } catch (Exception ex) {}
//      ((JraTable2) table).getDefaultRenderer(Object.class).getTableCellRendererComponent()
    }
//    ((JraTable2) table).getDataSetColumn(num).getDataType()
    return x + e.getWidth() + 20;
  }

  private int createHD(double ratio, raReportSection sect, raReportElement def) {
    long x = defineElem(0, ratio, 0, def);
    for (int i = 1; i < model.getColumnCount(); i++) {
      raReportElement e = sect.addModel(ep.TEXT, (String[]) def.getDefaults().clone());
      e.restoreDefaults();
      x = defineElem(x, ratio, i, e);
    }
    return (int) x;
  }
  
  private int createSHD(double ratio, raReportSection sect, raReportElement def) {
    long x = defineElemS(0, ratio, 0, def);
    for (int i = 1; i < summ.getColumnModel().getColumnCount(); i++) {
      raReportElement e = sect.addModel(ep.TEXT, (String[]) def.getDefaults().clone());
      e.restoreDefaults();
      x = defineElemS(x, ratio, i, e);
    }
    return (int) x;
  }

  private void createFooter(double ratio, raReportSection sect, raReportElement def, String uk) {
    if (!sums)
      sect.setTransparent(true);
    else {
//      raRowSume srow = bean.getSumRow();
      int non = 0;
      while (non < model.getColumnCount() &&
             getValueAt(table.getRowCount() - 1, non).trim().length() == 0) ++non;

      if (non == model.getColumnCount()) {
        ratio = ratio * model.getTotalColumnWidth() / model.getColumn(0).getWidth();
        defineElem(0, ratio, 0, def);
      } else {
        createHD(ratio, sect, def);
        if (non > 0) {
          for (int i = 1; i < non; i++) sect.removeModel(3);
          def.setWidth(sect.getModel(3).getLeft() - 20);
//          def.setControlSource("=(if (= 1 1) \"U K U P N O\" \"\")");
          def.setControlSource("=(if (= 1 1) \""+uk+"\")");
          def.setProperty(ep.ALIGN, ep.LEFT);
        }
      }
    }
  }

  private int getMaxGroupSectionChars() {
    int max = 0;
    for (int i = 0; i < xt.getTotalGroupCount(); i++) {
      Column col = xt.getDataSet().hasColumn(xt.getGroup(i));
      if (col != null && col.getCaption().length() > max)
        max = col.getCaption().length();
    }
    return max;
  }

  private void createGroups(raReportTemplate tem, raReportSection secs,
          raReportSection head1, raReportSection foot1, raReportElement def) {
    int l = xt.getGroupCount() + 1;
    int max = getMaxGroupSectionChars();
    int ng = 0, last = xt.getTotalGroupCount() - 1;
    raReportSection head =
        new raReportSection(tem.template.getModel(ep.SECTION_HEADER + l), head1.getDefaults());
    head.restoreDefaults();
    head1.moveAll(head);
    head1 = head;
    raReportSection foot =
        new raReportSection(tem.template.getModel(ep.SECTION_FOOTER + l), foot1.getDefaults());
    foot.restoreDefaults();
    foot1.moveAll(foot);
    foot1 = foot;
    raReportElement sec;
    for (int i = 0; i < xt.getTotalGroupCount(); i++) {
      if (xt.isRealGroup(i)) {
        sec = secs.getModel(ep.SECTION + xt.getGroupNum(i), null);
        sec.setProperty(ep.FIELD, "Group" + i);
        sec.setProperty(ep.GROUP_ON, ev.EACH_VALUE);
        sec.setProperty(ep.GROUP_HEADER, ev.YES);
        if (xt.isGroupDescending(i))
          sec.setProperty(ep.SORT_ORDER, ev.DESCENDING);
//        if (xt.getTotalGroupCount() > 1) {
          sec.setProperty(ep.GROUP_FOOTER, ev.YES);
          foot = new raReportSection(tem.template.getModel(ep.SECTION_FOOTER + xt.getGroupNum(i)));
//          private String[] SectionHeader1Props = new String[] {"Logo", "", "", "", "Yes", "No", "Yes",
//     "Yes", "660"};
          foot.setCaption(xt.getGroup(i));
          foot.setVisible(true);
          foot.setProperty(ep.GROW, ev.YES);
          foot.setProperty(ep.SHRINK, ev.NO);
          foot.setHeight(0);
//          if (xt.getGroupNum(i) == 1)
//            foot.setProperty(ep.FORCE_NEW, ev.AFTER);
//          else
            foot.setHeight(500);
//        } else foot = null;
        head = new raReportSection(tem.template.getModel(ep.SECTION_HEADER + xt.getGroupNum(i)));
        head.setCaption(xt.getGroup(i));
        head.setVisible(true);
        head.setProperty(ep.GROW, ev.YES);
        head.setProperty(ep.SHRINK, ev.NO);
        head.setProperty(ep.REPEAT, ev.NO);
        head.setHeight(0);
        ng = 0;
        last = i;
      }
      raReportElement name = head.addModel(ep.TEXT, def.getDefaults());
      name.setFontSize(9);
      name.setFontBold(true);
      name.setTop(ng * 300);
      name.setLeft(20);
      name.setHeight(300);
      name.setWidth(200 + max * 120);
      name.setControlSource("GroupCaption" + i);
      raReportElement value = head.addModel(ep.TEXT, def.getDefaults());
      value.setFontSize(9);
      value.setTop(ng * 300);
      value.setLeft(max * 120 + 300);
      value.setHeight(300);
      value.setWidth(10000 - max * 120 - 400);
      value.setControlSource("GroupValue" + i);
      ++ng;
    }
    sec = secs.getModel(ep.SECTION + l, null);
    sec.setProperty(ep.FIELD, "Group" + last);
    sec.setProperty(ep.GROUP_ON, ev.EACH_VALUE);
    sec.setProperty(ep.GROUP_HEADER, ev.YES);
    sec.setProperty(ep.GROUP_FOOTER, ev.YES);
  }
}
// GROUP_HEADER, GROUP_FOOTER, GROUP_ON

