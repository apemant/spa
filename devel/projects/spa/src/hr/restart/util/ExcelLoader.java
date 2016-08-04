package hr.restart.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;


public class ExcelLoader {

  StorageDataSet dest;
  private List rules;
  String encoding;
  
  public ExcelLoader(StorageDataSet dest) {
    this.dest = dest;
    rules = new ArrayList();
  }
  
  public void addRule(int col, String colname) {
    rules.add(new Rule(col, colname));
  }
  
  public void setEncoding(String enc) {
    encoding = enc;
  }
  
  public void setRules(String rule) {
    rules.clear();
    String[] parts = rule.indexOf(',') > 0 ? new VarStr(rule).splitTrimmed(',') : new VarStr(rule).split();
    if (rule.indexOf('=') > 0)
      for (int i = 0; i < parts.length; i++) {
        int eq = parts[i].indexOf('=');
        String col = parts[i].substring(0, eq);
        if (Aus.isDigit(col)) rules.add(new Rule(Aus.getNumber(col) - 1, parts[i].substring(eq + 1)));
        else rules.add(new Rule(col.charAt(0) - 'A', parts[i].substring(eq + 1)));
      }
    else
      for (int i = 0; i < parts.length; i++)
        if (!parts[i].equals("*"))
          rules.add(new Rule(i, parts[i]));
  }
  
  public StorageDataSet load(String pathname, int sheet, int first, int last) {
    return load(new File(pathname), sheet, first, last);
  }
  
  public StorageDataSet load(File file, int sheet, int first, int last) {
    if (!file.exists()) return getError("Ne postoji datoteka " + file);
    if (!file.canRead()) return getError("Datoteka " + file + " se ne može otvoriti");
    
    try {
      Workbook wb = WorkbookFactory.create(file);
      return fillData(wb, sheet, first, last);
    } catch (IOException e) {
      return getError("Greška kod otvaranja datoteke " + file);
    } catch (Exception e) {
      return getError("Greška: " + e.getMessage());
    }
  }
  
  public StorageDataSet fillData(Workbook wb, int sheet, int first, int last) {
    if (sheet > 0) --sheet;
    Sheet sh = wb.getSheetAt(sheet);
    if (sh == null) return getError("Greška u plahti " + sheet);
    
    for (int l = first; l <= last; l++) {
      Row row = sh.getRow(l - 1);
      if (row == null) continue;
      
      dest.insertRow(false);
      for (int i = 0; i < rules.size(); i++) {
        Rule r = (Rule) rules.get(i);
        r.perform(row);
      }
    }
    return dest;
  }
  
  public StorageDataSet getError(String err) {
    StorageDataSet ret = Aus.createSet("{Opis greške}MSG:250");
    ret.insertRow(false);
    ret.setString("MSG", err);
    return ret;
  }
  
  private class Rule {
    int col;
    String colName;
    int type;
    int len;
    
    public Rule(int col, String colname) {
      this.col = col;
      this.colName = colname;
      type = dest.getColumn(colname).getDataType();
      if (type == Variant.STRING) 
        len = dest.getColumn(colname).getPrecision();
    }
    
    public void perform(Row row) {
      Cell cell = row.getCell(col);
      if (cell == null) return;
      
      
      switch (type) {
        case Variant.STRING:
          fillString(cell);
          break;
        case Variant.INT:
          fillInt(cell);
          break;
        case Variant.BIGDECIMAL:
          fillBigDecimal(cell);
          break;
        case Variant.TIMESTAMP:
          fillTimestamp(cell);
          break;
        case Variant.SHORT:
          fillShort(cell);
          break;
      }
    }
    
    void fillString(Cell cell) {
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        convert(cell.getNumericCellValue());
      else trim(cell.getStringCellValue());
    }
    
    void convert(double num) {
      if (num != (long) num) dest.setString(colName, String.valueOf(num));
      else dest.setString(colName, String.valueOf((long) num));
    }
    
    void trim(String str) {
      if (str.length() > len) str = str.substring(0, len);
      
      if (encoding != null)
        try {
          str = new String(str.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
        }
      
      dest.setString(colName, str);
    }
    
    void fillInt(Cell cell) {
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        dest.setInt(colName, (int) cell.getNumericCellValue());
      else dest.setInt(colName, Aus.getNumber(cell.getStringCellValue()));
    }
    
    void fillBigDecimal(Cell cell) {
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        dest.setBigDecimal(colName, BigDecimal.valueOf(cell.getNumericCellValue()));
      else dest.setBigDecimal(colName, Aus.getDecNumber(cell.getStringCellValue()));
    }
    
    void fillTimestamp(Cell cell) {
      Date date = cell.getDateCellValue();
      if (date != null) dest.setTimestamp(colName, new Timestamp(date.getTime()));
    }
    
    void fillShort(Cell cell) {
      if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        dest.setShort(colName, (short) cell.getNumericCellValue());
      else dest.setShort(colName, (short) Aus.getNumber(cell.getStringCellValue()));
    }
  }
}
