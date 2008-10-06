package hr.restart.swing;

import java.awt.Color;

import javax.swing.JComponent;

import com.borland.dx.dataset.Variant;


public class raStatusColorModifier extends raTableModifier {
  Variant shared = new Variant();

  String colName, normValue;
  Color backCol, foreCol;
  
  public raStatusColorModifier(String col, String val, Color back, Color fore) {
    colName = col;
    normValue = val;
    backCol = back;
    foreCol = fore;
  }
  
  public boolean doModify() {
    if (getTable() instanceof JraTable2) {
      JraTable2 tab = (JraTable2) getTable();
      if (tab.getDataSet().getRowCount() > 0 && 
          tab.getDataSet().hasColumn(colName) != null) {
        tab.getDataSet().getVariant(colName, this.getRow(), shared);
        return !shared.getString().equals(normValue);
      }
    }
    return false;
  }
  
  public void modify() {
    JComponent jRenderComp = (JComponent) renderComponent;
    if (isSelected()) {
      jRenderComp.setBackground(backCol);
      jRenderComp.setForeground(Color.black);
    } else {
      //jRenderComp.setBackground(getTable().getBackground());
      jRenderComp.setForeground(foreCol);
    }
  }
}
