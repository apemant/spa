package hr.restart.swing;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;


public class raTableRunningSum extends raTableModifier {

  private String colname;
  Variant shared = new Variant();
  BigDecimal forwardLast, forwardSum;
  BigDecimal backwardLast, backwardSum;
  int forwardLastRow, backwardLastRow;
  SortDescriptor olds;
  int oldrows;
  
  public raTableRunningSum(String col) {
    colname = col;
  }
  
  public void dataChanged() {
    forwardSum = backwardSum = null;
    oldrows = 0;
  }

  public boolean doModify() {
    if (getTable() instanceof JraTable2) {
      JraTable2 tab = (JraTable2) getTable();
      Column col = tab.getDataSetColumn(getColumn());
      return (col != null && col.getColumnName().equalsIgnoreCase(colname));
    }
    return false;
  }

  public void modify() {
    JraTable2 tab = (JraTable2) getTable();
    DataSet ds = tab.getDataSet();
    int row = getRow();
    if (row > ds.rowCount()) return;
    if (row > 0) {
      if (oldrows == 0 || oldrows != ds.rowCount()) {
        forwardSum = backwardSum = null;
        oldrows = ds.rowCount();
      }
      if (ds.getSort() != null || olds != null) {
        SortDescriptor news = ds.getSort();
        if (news == null || olds == null || !olds.equals(news)) {
          forwardSum = backwardSum = null;
          olds = news;
        }
      }
      
      boolean found = false;
      if (forwardSum != null) {
        if (forwardLastRow == row) {
          ds.getVariant(colname, row, shared);
          if (shared.getBigDecimal().compareTo(forwardLast) == 0) {
            found = true;
            shared.setBigDecimal(forwardSum);
          }
        } else if (forwardLastRow == row - 1) {
          ds.getVariant(colname, row - 1, shared);
          if (shared.getBigDecimal().compareTo(forwardLast) == 0) {
            found = true;
            forwardLastRow = row;
            ds.getVariant(colname, row, shared);
            forwardLast = shared.getBigDecimal();
            forwardSum = forwardSum.add(forwardLast);
            shared.setBigDecimal(forwardSum);
          }
        }
      }
      if (!found && backwardSum != null) {
        if (backwardLastRow == row) {
          ds.getVariant(colname, row, shared);
          if (shared.getBigDecimal().compareTo(backwardLast) == 0) {
            found = true;
            shared.setBigDecimal(backwardSum);
          }
        } else if (backwardLastRow == row -1) {
          ds.getVariant(colname, row - 1, shared);
          if (shared.getBigDecimal().compareTo(backwardLast) == 0) {
            found = true;
            forwardLastRow = row;
            ds.getVariant(colname, row, shared);
            forwardLast = shared.getBigDecimal();
            forwardSum = backwardSum.add(forwardLast);
            shared.setBigDecimal(forwardSum);
          }
        } else if (backwardLastRow == row + 1) {
          ds.getVariant(colname, row + 1, shared);
          if (shared.getBigDecimal().compareTo(backwardLast) == 0) {
            found = true;
            backwardLastRow = row;
            ds.getVariant(colname, row, shared);
            backwardSum = backwardSum.subtract(backwardLast);
            backwardLast = shared.getBigDecimal();
            shared.setBigDecimal(backwardSum);
          }
        }
      }
      if (!found) {
        ds.getVariant(colname, 0, shared);
        BigDecimal sum = shared.getBigDecimal();
        for (int i = 1; i <= row; i++) {
          ds.getVariant(colname, i, shared);
          sum = sum.add(shared.getBigDecimal());
        }
        forwardLastRow = backwardLastRow = row;
        forwardLast = backwardLast = shared.getBigDecimal();
        shared.setBigDecimal(sum);
        forwardSum = backwardSum = sum;
      }
      Column bcol = ds.getColumn(colname);
      setComponentText(bcol.format(shared));
    }
  }
}
