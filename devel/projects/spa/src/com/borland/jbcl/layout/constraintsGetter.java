package com.borland.jbcl.layout;
import java.awt.*;

public class constraintsGetter {

  public static void mark(XYLayout lay) {
    lay.info.put("resized", new Object());
  }
  
  public static void unmark(XYLayout lay) {
    lay.info.remove("resized");
  }
  
  public static boolean isMarked(XYLayout lay) {
    return lay.info.containsKey("resized");
  }
  
  public static XYConstraints getXYConstraints(XYLayout lay,Component comp) {
    XYConstraints xyC = (XYConstraints)lay.info.get(comp);
    if (xyC == null) {
      Rectangle r = comp.getBounds();
      xyC = new XYConstraints(r.x,r.y,r.width,r.height);
    }
    return xyC;
  }
  
  public static XYConstraints get(XYLayout lay, Component comp) {
    return (XYConstraints)lay.info.get(comp);
  }
  
  public static void set(XYLayout lay, Component comp, XYConstraints cons) {
    lay.info.put(comp, cons);
  }


}