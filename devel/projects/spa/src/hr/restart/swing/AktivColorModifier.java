package hr.restart.swing;

import hr.restart.pl.raParam;
import hr.restart.sisfun.frmParam;

import java.awt.Color;

import javax.swing.JComponent;

public class AktivColorModifier extends raStatusColorModifier {

  public AktivColorModifier(String col, String val, Color back, Color fore) {
    super(col, val, back, fore);
    if (backCol == null) {
      backCol = getColor(frmParam.getParam("sisfun", "neaktBC", "", "Background color neaktivnog recorda u tablici"));
    }
    if (foreCol == null) {
      foreCol = getColor(frmParam.getParam("sisfun", "neaktFC", "", "Font color neaktivnog recorda u tablici"));
    }
  }
  public AktivColorModifier() {
    super("AKTIV", "D", null, null);
  }
  
  private Color getColor(String sc) {
    if (!sc.equals("")) {
      try {
        return Color.decode(sc);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }
  public void modify() {
    JComponent jRenderComp = (JComponent) renderComponent;
    if (isSelected()) {
      jRenderComp.setBackground((backCol==null)?Color.WHITE.darker():backCol);
      jRenderComp.setForeground(Color.LIGHT_GRAY.brighter());
    } else {
      //jRenderComp.setBackground(getTable().getBackground());
      jRenderComp.setForeground(foreCol==null?Color.LIGHT_GRAY:foreCol);
    }
  }
}
