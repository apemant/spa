/****license*****************************************************************
**   file: raExtendedTable.java
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
package hr.restart.swing;

import hr.restart.robno.repMemo;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raJPTableView;
import hr.restart.util.reports.repDynamicProvider;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raExtendedTable extends JraTable2 {
  JPopupMenu pop = new JPopupMenu();
  JMenuItem jmiNewGroup = new JMenuItem("Dodaj grupiranje po koloni");
  JMenuItem jmiAddGroup = new JMenuItem("Ubaci kolonu u grupu");
  JMenuItem jmiRemGroup = new JMenuItem("Izbaci kolonu iz grupe");
  JMenuItem jmiAddSort = new JMenuItem("Ubaci kolonu u poredak");
  JMenuItem jmiRemove = new JMenuItem("Izbaci kolonu iz poretka");
  JMenuItem jmiAddSum = new JMenuItem("Zbroji kolonu");
  JMenuItem jmiRemSum = new JMenuItem("Poništi zbrajanje kolone");
  JMenuItem jmiRemoveAll = new JMenuItem("Poništi grupiranje i poredak");

  String clickCol;
  ArrayList group, sort;
  String lastAddedSort;
  boolean forcePage;
  
  raJPTableView owner;

  public raExtendedTable() {
    super();
    createHeaderMouseListener();
    createPopup();
    group = new ArrayList();
    sort = new ArrayList();
    getTableHeader().setDefaultRenderer(new HeaderRenderer());
  }
  
  public void setOwner(raJPTableView owner) {
    this.owner = owner;
  }
  
  public raJPTableView getOwner() {
    return owner;
  }

  String oldcolname = "";
  private void createHeaderMouseListener() {
    getTableHeader().removeMouseListener(tableHeaderMouseListener);
    tableHeaderMouseListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (!isEnabled() || getDataSet() == null) return;
        if (e.isPopupTrigger()) showPopup(e);
        else if ((e.getModifiers() & e.BUTTON1_MASK) == e.BUTTON1_MASK)
          leftClick(e);
      }
      public void mousePressed(MouseEvent e) {
        if (!isEnabled() || getDataSet() == null) return;
        if (e.isPopupTrigger()) showPopup(e);
      }
      public void mouseReleased(MouseEvent e) {
        if (!isEnabled() || getDataSet() == null) return;
        if (e.isPopupTrigger()) showPopup(e);
      }
    };
  }

  private void createPopup() {
    pop = new JPopupMenu();
    pop.add(jmiAddSort);
    pop.add(jmiRemove);
    pop.addSeparator();
    pop.add(jmiNewGroup);
    pop.add(jmiAddGroup);
    pop.add(jmiRemGroup);
    pop.addSeparator();
    pop.add(jmiAddSum);
    pop.add(jmiRemSum);
    pop.addSeparator();
    pop.add(jmiRemoveAll);
    jmiNewGroup.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (clickCol.equalsIgnoreCase(lastAddedSort)) lastAddedSort = null;
        sort.remove(ColumnInfo.get(clickCol));
        addToGroup(clickCol, true, true);
        createSortDescriptor();
      }
    });
    jmiAddGroup.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (clickCol.equalsIgnoreCase(lastAddedSort)) lastAddedSort = null;
        sort.remove(ColumnInfo.get(clickCol));
        addToGroup(clickCol, true, false);
        createSortDescriptor();
      }
    });
    jmiAddSort.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (lastAddedSort != null && sort.contains(ColumnInfo.get(lastAddedSort)))
          lastAddedSort = clickCol;
        sort.add(new ColumnInfo(clickCol));
        createSortDescriptor();
      }
    });
    jmiRemGroup.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        group.remove(ColumnInfo.get(clickCol));
        renumberGroups();
        createSortDescriptor();
      }
    });
    jmiRemove.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sort.remove(ColumnInfo.get(clickCol));
        createSortDescriptor();
      }
    });
    jmiRemoveAll.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        group.clear();
        sort.clear();
        createSortDescriptor();
      }
    });
    jmiAddSum.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        owner.getStorageDataSet().last();
        owner.toggleKumul(clickCol);
        owner.destroy_kum();
        owner.setKumTak(owner.getStoZbrojiti() != null);
        owner.init_kum();
      }
    });
    jmiRemSum.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        owner.toggleKumul(clickCol);
        owner.destroy_kum();
        owner.setKumTak(owner.getStoZbrojiti() != null);
        owner.init_kum();
      }
    });
  }

  private void showPopup(MouseEvent e) {
    if (getDataSet() == null) return;
    int idx = getTableHeader().columnAtPoint(e.getPoint());
    clickCol = getRealColumnName(idx);
    ColumnInfo ci = ColumnInfo.get(clickCol);
    jmiNewGroup.setEnabled(!group.contains(ci));
    jmiAddGroup.setEnabled(!group.contains(ci) && group.size() > 0);
    jmiRemGroup.setEnabled(group.contains(ci));
    jmiAddSort.setEnabled(!group.contains(ci) && !sort.contains(ci));
    jmiRemove.setEnabled(sort.contains(ci));
    jmiRemoveAll.setEnabled(group.size() + sort.size() > 0);
    if (Valid.getValid().isNumeric(getDataSet().getColumn(clickCol))) {
      boolean sum = (owner.getStoZbrojiti() != null && 
          Util.getUtil().containsArr(owner.getStoZbrojiti(), clickCol));
      jmiAddSum.setEnabled(!sum);
      jmiRemSum.setEnabled(sum);
    } else {
      jmiAddSum.setEnabled(false);
      jmiRemSum.setEnabled(false);
    }
    getColumnModel().getSelectionModel().setSelectionInterval(idx, idx);
    pop.show((Component) e.getSource(), e.getX(), e.getY());
  }

  private void leftClick(MouseEvent e) {
    if (getDataSet() == null) return;
    // Hrvatski Locale
    /*if (getDataSet() instanceof StorageDataSet) {
      StorageDataSet sds = (StorageDataSet) getDataSet();
      if (sds.getLocale() == null) sds.setLocale(Locale.getDefault());
    }*/
    int idx = getTableHeader().columnAtPoint(e.getPoint());
    clickCol = getRealColumnName(getTableHeader().columnAtPoint(e.getPoint()));
    ColumnInfo ci = ColumnInfo.get(clickCol);
    if (group.contains(ci))
      ((ColumnInfo) group.get(group.indexOf(ci))).toggle();
    else if (sort.contains(ci))
      ((ColumnInfo) sort.get(sort.indexOf(ci))).toggle();
    else {
      if (lastAddedSort != null && sort.contains(ColumnInfo.get(lastAddedSort)))
        sort.remove(ColumnInfo.get(lastAddedSort));
      if (lastAddedSort == null) sort.clear();
      sort.add(new ColumnInfo(lastAddedSort = clickCol));
    }
    getColumnModel().getSelectionModel().setSelectionInterval(idx, idx);
    createSortDescriptor();
    String newcolname = getColumnModel().getColumn(getTableHeader().columnAtPoint(e.getPoint())).getHeaderValue().toString();

    //raExtendedTable.this.firePropertyChange(COLNAMEPROPERTY,oldcolname,newcolname);
    fireColumnChanged(oldcolname, newcolname);
    oldcolname = newcolname;
    setSpeedColumn(getTableHeader().columnAtPoint(e.getPoint()));
  }

  private void generateSort(DataSet ds) {
    group.clear();
    sort.clear();
    if (ds != null && ds.getSort() != null) {
      SortDescriptor sd = ds.getSort();
      for (int i = 0; i < sd.getKeys().length; i++)
        addSort(sd.getKeys()[i], sd.getDescending() == null ? 
            true : !sd.getDescending()[i]);
    }
  }
  
  public void setDataSet(DataSet ds) {
    generateSort(ds);
    super.setDataSet(ds);
  }

  public void createSortDescriptor() {
    if (group.size() + sort.size() == 0) {
      raSelectTableModifier stm = hasSelectionTrackerInstalled();
      if (stm != null && stm.isNatural()) stm.clearSelection();
      getDataSet().setSort(original);
      if (isShowing()) getTableHeader().repaint();
      return;
    }
    String[] names = new String[countGroups() + sort.size()];
    boolean[] asc = new boolean[countGroups() + sort.size()];
    int ng = 0;
    for (int i = 0; i < group.size(); i++)
      if (i == 0 || ((ColumnInfo) group.get(i - 1)).numgroup != ((ColumnInfo) group.get(i)).numgroup) {
        names[ng] = ((ColumnInfo) group.get(i)).colname;
        asc[ng++] = !((ColumnInfo) group.get(i)).ascending;
      }
    for (int i = 0; i < sort.size(); i++) {
      names[i + ng] = ((ColumnInfo) sort.get(i)).colname;
      asc[i + ng] = !((ColumnInfo) sort.get(i)).ascending;
    }
    SortDescriptor sd = new SortDescriptor("", names, asc, true, false, null);
    if (isShowing()) getTableHeader().repaint();
    raSelectTableModifier stm = hasSelectionTrackerInstalled();
    if (stm != null && stm.isNatural()) stm.clearSelection();
    getDataSet().setSort(sd);
  }

  public void addToGroup(String name, boolean asc, String[] descs, String get, boolean newGroup) {
    ColumnInfo ci = new ColumnInfo(name, countGroups() + (newGroup ? 1 : 0));
    ci.ascending = asc;
    ci.descs = descs;
    ci.get = get;
    group.add(ci);
  }
  
  public void addToGroup(String name, boolean asc, String[] descs, DataSet getDs, boolean newGroup) {
    ColumnInfo ci = new ColumnInfo(name, countGroups() + (newGroup ? 1 : 0));
    ci.ascending = asc;
    ci.descs = descs;
    ci.getDs = getDs;
    group.add(ci);
  }

  public void addToGroup(String name, boolean asc, boolean newGroup) {
    addToGroup(name, asc, null, (String) null, newGroup);
  }

  public void addSort(String name, boolean asc) {
    ColumnInfo ci = new ColumnInfo(name);
    ci.ascending = asc;
    sort.add(ci);
    lastAddedSort = name;
  }
  
  SortDescriptor original = null;
  
  public void resetSortColumns() {
	group.clear();
	sort.clear();
	if (getDataSet() != null) {
	  original = getDataSet().getSort();
	  generateSort(getDataSet());
	} else original = null;
	lastAddedSort = null;
	if (isShowing()) getTableHeader().repaint();
  }
  
  public boolean isCustomSort() {
	return (group.size() + sort.size() > 0);
  }

  public boolean inGroup(String name) {
    return group.contains(ColumnInfo.get(name));
  }

  private void renumberGroups() {
    for (int i = 0, gr = 0, old = 0; i < group.size(); i++) {
      ColumnInfo ci = (ColumnInfo) group.get(i);
      if (i == 0 || ci.numgroup != old) {
        old = ci.numgroup;
        ++gr;
      }
      ci.numgroup = gr;
    }
  }

  public void dumpAll() {
    for (int i = 0; i < group.size(); i++)
      ((ColumnInfo) group.get(i)).dump();
    for (int i = 0; i < sort.size(); i++)
      ((ColumnInfo) sort.get(i)).dump();
  }

  private int countGroups() {
    return group.size() == 0 ? 0 : ((ColumnInfo) group.get(group.size() - 1)).numgroup;
  }

  public int getTotalGroupCount() {
    return group.size();
  }

  public int getGroupCount() {
    return countGroups();
  }

  public String getGroup(int n) {
    return ((ColumnInfo) group.get(n)).colname;
  }

  public String getRealGroup(int n) {
    for (int i = 0; i < group.size(); i++)
      if (((ColumnInfo) group.get(i)).numgroup == n)
        return ((ColumnInfo) group.get(i)).colname;
    return "";
  }

  public boolean isRealGroup(int n) {
    return (n == 0 || ((ColumnInfo) group.get(n)).numgroup != ((ColumnInfo) group.get(n - 1)).numgroup);
  }

  public boolean isGroupDescending(int n) {
    return !((ColumnInfo) group.get(n)).ascending;
  }

  public int getGroupNum(int n) {
    return ((ColumnInfo) group.get(n)).numgroup;
  }

  public String[] getGroupDesc(int n) {
    return ((ColumnInfo) group.get(n)).descs;
  }

  public String getGroupGet(int n) {
    return ((ColumnInfo) group.get(n)).get;
  }
  
  public DataSet getGroupGetDs(int n) {
    return ((ColumnInfo) group.get(n)).getDs;
  }
  
  public boolean isForcePage() {
		return forcePage;
	}

	public void setForcePage(boolean forcePage) {
		this.forcePage = forcePage;
	}

  private String xlsRange(int firstRow, int lastRow, short column) {
    char cc = (char) ('A' + column);
    return "" + cc + firstRow + ":" + cc + lastRow;
  }
  
  public void exportToXLS(File output) {
    String fname = output.getName();
    if (!fname.endsWith("xls") && fname.indexOf('.') < 0)
      output = new File(output.getParentFile(), fname + ".xls");
    System.out.println("exporting to XLS");
    HSSFWorkbook wb = new HSSFWorkbook();
    
    HSSFDataFormat df = wb.createDataFormat();
    
    String fontFamily = frmParam.getParam("sisfun", "excelFont", "Arial", 
        "Font za export u Excel", true);
    if (fontFamily == null || fontFamily.length() == 0)
      fontFamily = "Arial";
    
    int fontSize = 10;
    String fontSizeTx = frmParam.getParam("sisfun", "excelFontSize", "10", 
        "Velièina fonta za export u Excel, u toèkama", true);
    if (fontSizeTx != null && Aus.getNumber(fontSizeTx) >= 6 && Aus.getNumber(fontSizeTx) <= 72)
      fontSize = Aus.getNumber(fontSizeTx);
    
    HSSFFont font = wb.createFont();
    font.setFontName(fontFamily);
    font.setFontHeightInPoints((short) fontSize);
    
    HSSFFont fontTitle = wb.createFont();
    fontTitle.setFontName(fontFamily);
    fontTitle.setFontHeightInPoints((short) (fontSize * 1.8));
    
    HSSFFont fontSubtitle = wb.createFont();
    fontSubtitle.setFontName(fontFamily);
    fontSubtitle.setFontHeightInPoints((short) (fontSize * 1.5));

    HSSFCellStyle csHeader = wb.createCellStyle();
    csHeader.setFont(font);
    csHeader.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
    csHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    csHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    csHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    csHeader.setDataFormat(df.getFormat("text"));
    
    HSSFCellStyle csFooter = wb.createCellStyle();
    csFooter.setFont(font);
    csFooter.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
    csFooter.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    csFooter.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    csFooter.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    csFooter.setDataFormat(df.getFormat("text"));
    
    HSSFCellStyle csFooterNum2 = wb.createCellStyle();
    csFooterNum2.setFont(font);
    csFooterNum2.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
    csFooterNum2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    csFooterNum2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    csFooterNum2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    csFooterNum2.setDataFormat(df.getFormat("#,##0.00"));
    
    HSSFCellStyle csFooterNum = wb.createCellStyle();
    csFooterNum.setFont(font);
    csFooterNum.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
    csFooterNum.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    csFooterNum.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    csFooterNum.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    csFooterNum.setDataFormat(df.getFormat("#"));
    
    HSSFCellStyle csDate = wb.createCellStyle();
    csDate.setFont(font);
    csDate.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    csDate.setDataFormat(df.getFormat("dd.mm.yyyy"));
    
    HSSFCellStyle csTitle = wb.createCellStyle();
    csTitle.setFont(fontTitle);
    csTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    csTitle.setDataFormat(df.getFormat("text"));
    
    HSSFCellStyle csSubtitle = wb.createCellStyle();
    csSubtitle.setFont(fontSubtitle);
    csSubtitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    csSubtitle.setDataFormat(df.getFormat("text"));
    
    HSSFCellStyle csNum2 = wb.createCellStyle();
    csNum2.setFont(font);
    csNum2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    csNum2.setDataFormat(df.getFormat("#,##0.00"));
    
    HSSFCellStyle csNum3 = wb.createCellStyle();
    csNum3.setFont(font);
    csNum3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    csNum3.setDataFormat(df.getFormat("#,##0.000"));
    
    HSSFCellStyle csNum = wb.createCellStyle();
    csNum.setFont(font);
    csNum.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    csNum.setDataFormat(df.getFormat("#.#"));
    
    HSSFCellStyle csInt = wb.createCellStyle();
    csInt.setFont(font);
    csInt.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    csInt.setDataFormat(df.getFormat("#"));
    
    HSSFCellStyle csText = wb.createCellStyle();
    csText.setFont(font);
    csText.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    csText.setDataFormat(df.getFormat("text"));
    
    repDynamicProvider dp = repDynamicProvider.getInstance();
    boolean sums = dp.hasSumRow();
    int cols = getColumnModel().getColumnCount();
    int rows = getRowCount() - (sums ? 1 : 0);
    
    HSSFSheet sheet = wb.createSheet();
    HSSFRow row;
    HSSFCell cell;
    
    short cRow = 0;
    
    // header and title
    row = sheet.createRow(cRow = 0);
    cell = row.createCell((short) 0);
    cell.setCellStyle(csText);
    if (cols > 1) sheet.addMergedRegion(new Region(cRow, (short) 0, cRow, (short) (cols - 1)));
    cell.setEncoding(wb.ENCODING_UTF_16);
    cell.setCellValue(repMemo.getrepMemo().getOneLine());
    
    row = sheet.createRow(++cRow);
    cell = row.createCell((short) 0);
    cell.setCellStyle(csTitle);
    if (cols > 1) sheet.addMergedRegion(new Region(cRow, (short) 0, cRow, (short) (cols - 1)));
    cell.setEncoding(wb.ENCODING_UTF_16);
    if (dp.getTitle().length() > 0)
      cell.setCellValue(dp.getTitle().substring(1));
    else cell.setCellValue("");
    
    if (dp.getSubtitle().length() > 0) {
      row = sheet.createRow(++cRow);
      cell = row.createCell((short) 0);
      cell.setCellStyle(csSubtitle);
      if (cols > 1) sheet.addMergedRegion(new Region(cRow, (short) 0, cRow, (short) (cols - 1)));
      cell.setEncoding(wb.ENCODING_UTF_16);
      cell.setCellValue(dp.getSubtitle().substring(1));
    }
    
    for (short c = 0; c < cols; c++) 
      sheet.setColumnWidth(c, (short) (getColumnModel().getColumn(c).getWidth() * 40));

    // sections
    row = sheet.createRow(++cRow);
    int secRow = 0, firstRow = 0;
    
    for (int r = 0; r < rows; r++) {
      if (r == 0) {
        row = sheet.createRow(++cRow);
        for (short c = 0; c < cols; c++) {
          cell = row.createCell(c);
          cell.setCellStyle(csHeader);
          cell.setEncoding(wb.ENCODING_UTF_16);
          cell.setCellValue(getColumnModel().getColumn(c).getHeaderValue().toString());
        }
        if (firstRow == 0) firstRow = cRow;
        secRow = cRow;
      }
      row = sheet.createRow(++cRow);
      for (short c = 0; c < cols; c++) {
        cell = row.createCell(c);
        Object o = getValueAt(r, c);
        if (o instanceof Number) {
          if (o instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) o;
            if (bd.scale() == 2) cell.setCellStyle(csNum2);
            else if (bd.scale() == 3) cell.setCellStyle(csNum3);
            else cell.setCellStyle(csNum);
            cell.setCellValue(bd.doubleValue());
          } else {
            String t = dp.getValueAt(r, c);
            if (Aus.isDigit(t)) {
              cell.setCellStyle(csInt);
              cell.setCellValue(((Number) o).doubleValue());
            } else {
              cell.setCellStyle(csText);
              cell.setEncoding(wb.ENCODING_UTF_16);
              cell.setCellValue(t);
            }
          }
        } else if (o instanceof Date) {
          cell.setCellStyle(csDate);
          cell.setCellValue((Date) o);
        } else {
          cell.setCellStyle(csText);
          cell.setEncoding(wb.ENCODING_UTF_16);
          cell.setCellValue(dp.getValueAt(r, c));
        }
      }
    }
    System.out.println("sums " + sums);
    if (sums) {
      int non = 0;
      while (non < cols && dp.getValueAt(getRowCount() - 1, non).trim().length() == 0) ++non;
      if (non < cols) {
        System.out.println("creating row "+non);
        row = sheet.createRow(++cRow);
        
        if (non > 0) {
          cell = row.createCell((short) 0);
          cell.setCellStyle(csFooter);
          cell.setEncoding(wb.ENCODING_UTF_16);
          cell.setCellValue("U K U P N O");
          if (non > 1)
            sheet.addMergedRegion(new Region(cRow, (short) 0, cRow, (short) (non - 1)));
        }
        for (short c = (short) non; c < cols; c++) {
          cell = row.createCell(c);
          Object o = getValueAt(rows - 1, c);
          if ((o instanceof BigDecimal) &&
               ((BigDecimal) o).scale() == 2)
            cell.setCellStyle(csFooterNum2);
          else cell.setCellStyle(csFooterNum);
          if (dp.getValueAt(getRowCount() - 1, c).trim().length() != 0)
            cell.setCellFormula("SUBTOTAL(9;"+xlsRange(firstRow + 1, cRow, c)+")");
          else cell.setCellValue("");
        }
      }
    } 
    FileOutputStream out = null;
    
    try {
      out = new FileOutputStream(output);
      wb.write(out);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) 
        try {
          out.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }
  }

  class HeaderRenderer extends DefaultTableCellRenderer {
    int num;
    Font ital, norm;
//    Font tiny;
    public HeaderRenderer() {
      super();
      setHorizontalAlignment(JLabel.CENTER);
    }
    public Component getTableCellRendererComponent(JTable t, Object v,
        boolean sel, boolean foc, int row, int col) {
      num = -1;
      setFont(norm != null ? norm : (norm = getFont()));
      if (t != null) {
        JTableHeader header = t.getTableHeader();
        if (header != null) {
          setForeground(header.getForeground());
          setBackground(header.getBackground());
          setFont(header.getFont());
          if (getDataSet() != null) {
            String cn = getRealColumnName(col);
            if (group.contains(ColumnInfo.get(cn)))
              setFont(ital != null ? ital : (ital = norm.deriveFont(Font.ITALIC | Font.BOLD)));
            else num = sort.indexOf(ColumnInfo.get(cn));
          }
        }
      }
      setText((v == null) ? "" : v.toString());
      setBorder(UIManager.getBorder("TableHeader.cellBorder"));
      try {
        if (getPreferredSize().width > t.getCellRect(row, col, false).width)
          setToolTipText(getText());
        else setToolTipText(null);
      } catch (Exception e) {
        setToolTipText(null);
      }
      return this;
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (num >= 0) {
        Color sh1 = getBackground().darker();
        Color sh2 = sh1.darker();
        Color hi1 = getBackground().brighter();
        Color hi2 = hi1.brighter();
        ColumnInfo ci = (ColumnInfo) sort.get(num);
        int h = getHeight() / 4;
        int size = h * 2;
        int spc = (getHeight() - size) / 2;
        int bg = getWidth() - size - spc - getInsets().right;
//        if (tiny == null) tiny = getFont().deriveFont(h * 1.2f);
        g.setColor(getBackground());
        g.fillRect(bg - 2, getInsets().top, getWidth() - getInsets().right - bg + 1,
                   getHeight() - getInsets().top - getInsets().bottom);
        if (ci.ascending) {
          g.setColor(hi1);
          g.drawLine(bg + h + 1, spc + size - 1, bg + size, spc);
          g.setColor(hi2);
          g.drawLine(bg + h + 2, spc + size - 1, bg + size + 1, spc);
          g.setColor(sh1);
          g.drawLine(bg, spc, bg + size, spc);
          g.drawLine(bg + 1, spc, bg + h, spc + size);
          g.setColor(sh2);
          g.drawLine(bg, spc, bg + h - 1, spc + size - 1);
//          g.setFont(tiny);
//          g.setColor(getForeground());
//          if (sort.size() > 1) {
//            Rectangle2D r = g.getFontMetrics().getStringBounds(String.valueOf(num + 1), g);
//
//            g.drawString(String.valueOf(num + 1), bg + h - (int) (r.getWidth() / 2),
//                         spc + 2 + (int) g.getFontMetrics().getDescent());
//          }
        } else {
          g.setColor(sh1);
          g.drawLine(bg + h, spc, bg + 1, spc + size - 1);
          g.setColor(sh2);
          g.drawLine(bg + h - 1, spc, bg, spc + size - 1);
          g.setColor(hi1);
          g.drawLine(bg + 1, spc + size - 1, bg + size, spc + size - 1);
          g.drawLine(bg + size, spc + size - 1, bg + h + 1, spc);
          g.setColor(hi2);
          g.drawLine(bg + size + 1, spc + size - 1, bg + h + 2, spc);
//          g.setFont(tiny);
//          g.setColor(getForeground());
//          if (sort.size() > 1) {
//            Rectangle2D r = g.getFontMetrics().getStringBounds(String.valueOf(num + 1), g);
//            g.drawString(String.valueOf(num + 1), bg + h - (int) (r.getWidth() / 2), spc + size - 1);
//          }
        }
      }
    }
  }

  static class ColumnInfo {
    String colname;
    String[] descs;
    String get;
    DataSet getDs;
    boolean ascending;
    int numgroup;
    static ColumnInfo dummy = new ColumnInfo("");

    static ColumnInfo get(String name) {
      dummy.colname = name;
      return dummy;
    }
    ColumnInfo(String name) {
      colname = name;
      ascending = true;
    }
    ColumnInfo(String name, int group) {
      this(name);
      numgroup = group;
    }
    void toggle() {
      ascending = !ascending;
    }
    public boolean equals(Object o) {
      if (o != null && o instanceof ColumnInfo)
        return colname.equals(((ColumnInfo) o).colname);
      else return false;
    }
    public void dump() {
      if (numgroup == 0)
        System.out.println((ascending ? "SORT: " : "SORT(descending): ") + colname);
      else {
        System.out.println(numgroup+". "+(ascending ? "GROUP: " : "GROUP(descending): ")+colname);
        if (get != null)
          System.out.println("  print "+VarStr.join(descs,',')+" from "+get+"()");
      }
    }
  }
}

