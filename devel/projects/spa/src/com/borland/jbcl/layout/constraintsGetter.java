package com.borland.jbcl.layout;
import java.awt.*;

public class constraintsGetter {

  public static XYConstraints getXYConstraints(XYLayout lay,Component comp) {
    XYConstraints xyC = (XYConstraints)lay.info.get(comp);
    if (xyC == null) {
      Rectangle r = comp.getBounds();
      xyC = new XYConstraints(r.x,r.y,r.width,r.height);
    }
    return xyC;
  }

}