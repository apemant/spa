/****license*****************************************************************
**   file: raXYLayout.java
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
package hr.restart.swing.layout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Hashtable;

public class raXYLayout implements LayoutManager2 {

  Hashtable compBounds;
  private int width;
  private int height;
  private boolean resizeable;
  public raXYLayout() {
    this(hr.restart.start.isRESIZABLELAYOUT());
  }
  public raXYLayout(boolean resizeableC) {
    compBounds = new Hashtable();
    resizeable = resizeableC;
  }
  public int getWidth() {
    return width;
  }

  public void setWidth(int widthC)  {
//    width = widthC;
    width = -1;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height)
  {
//      this.height = height;
    height = -1;
  }
  public void parseXYLayout(LayoutManager Lay, Container target) {
    com.borland.jbcl.layout.XYLayout jbXYLay = null;
    if (Lay instanceof com.borland.jbcl.layout.XYLayout) {
      jbXYLay = (com.borland.jbcl.layout.XYLayout)Lay;
      setHeight(jbXYLay.getHeight());
      setWidth(jbXYLay.getWidth());
    }
    Lay.layoutContainer(target);
    compBounds.clear();
    for (int i=0; i<target.getComponentCount(); i++) {
      Component cmp = target.getComponent(i);
//      compBounds.put(cmp,cmp.getBounds());
      if (jbXYLay == null) {
        addLayoutComponent(cmp,com.borland.jbcl.layout.constraintsGetter.getXYConstraints(jbXYLay,cmp));
      } else {
        addLayoutComponent(cmp,cmp.getBounds());
      }
    }
  }

  public void addLayoutComponent(Component comp,Object constr) {
    //Rectangle bounds;
    raXYConstraints bounds;
    if (constr instanceof raXYConstraints) {
//      raXYConstraints xyconstr = (raXYConstraints)constr;
//      bounds = new Rectangle(xyconstr.x,xyconstr.y,xyconstr.width,xyconstr.height);
      bounds = (raXYConstraints)constr;
    } else if (constr instanceof com.borland.jbcl.layout.XYConstraints) {
      com.borland.jbcl.layout.XYConstraints jbXYC = (com.borland.jbcl.layout.XYConstraints)constr;
//      bounds = new Rectangle(jbXYC.getX(),jbXYC.getY(),jbXYC.getWidth(),jbXYC.getHeight());
      bounds = new raXYConstraints(jbXYC.getX(),jbXYC.getY(),jbXYC.getWidth(),jbXYC.getHeight());
    } else {
      bounds = new raXYConstraints(comp.getBounds());
    }
    compBounds.put(comp,bounds);
  }

  public Dimension maximumLayotSize(Container target) {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

  public float getLayoutAlignmentY(Container target) {
    return 0.5F;
  }

  public float getLayoutAlignmentX(Container target) {
    return 0.5F;
  }

  public void invalidateLayout(Container target) {
  }

  public void addLayoutComponent(String name, Component comp) {
  }

  public void removeLayoutComponent(Component comp) {
  }

  public Dimension preferredLayoutSize(Container parent) {
    return getLayoutSize(parent, true);
  }

  public Dimension minimumLayoutSize(Container parent) {
    return getLayoutSize(parent, false);
  }

  public Dimension maximumLayoutSize(Container parent) {
    return new Dimension(0x7fffffff, 0x7fffffff);
  }

  public void layoutContainer(Container target) {
    Dimension parSize=null;
    Dimension defParSize=null;
    if (resizeable) {
      parSize = getLayoutSize(target,true);
      defParSize = target.getSize();
    }
    Insets insets = target.getInsets();
    int count = target.getComponentCount();
    for(int i = 0; i < count; i++)
    {
        Component component = target.getComponent(i);
        if(component.isVisible())   {
            Rectangle r;
            if (resizeable) {
              r = getSizedComponentBounds(component,parSize,defParSize, true);
            } else {
              r = getComponentBounds(component, true);
            }
//            component.setBounds(insets.left + r.x, insets.top + r.y, r.width, r.height);
            component.setBounds(insets.left + r.x, r.y, r.width, r.height);
//            component.setBounds(r);
        }
    }
  }
  // iz xylayouta
  Dimension getLayoutSize(Container target, boolean doPreferred) {
      Dimension dim = new Dimension(0, 0);
      if(width <= 0 || height <= 0)
      {
          int count = target.getComponentCount();
          for(int i = 0; i < count; i++)
          {
              Component component = target.getComponent(i);
              if(component.isVisible())
              {
                  Rectangle r = getComponentBounds(component, doPreferred);
                  dim.width = Math.max(dim.width, r.x + r.width);
                  dim.height = Math.max(dim.height, r.y + r.height);
              }
          }

      }
      if(width > 0) //nije nikad u duhu metoda setWidth i setHeight koji setiraju na -1
          dim.width = width;
      if(height > 0)
          dim.height = height;

      Insets insets = target.getInsets();
      dim.width += insets.left + insets.right;
//      dim.height += insets.top + insets.bottom;
      //malo marginicu ...
      dim.width = dim.width + wm;
      dim.height = dim.height + hm;
      //
      return dim;
  }

  boolean isPanel(Component component) {
    return (component instanceof javax.swing.JPanel)||(component instanceof javax.swing.JTabbedPane);
  }

  boolean isButton(Component component) {
//  javax.swing.AbstractButton bbb;
    return (component instanceof javax.swing.AbstractButton);
  }

  Rectangle getSizedComponentBounds(Component component, Dimension parentSize, Dimension parentDefaultSize ,boolean doPreferred) {
      double heightkoef = parentDefaultSize.getHeight()/parentSize.getHeight();
      double widthkoef = parentDefaultSize.getWidth()/parentSize.getWidth();
      Rectangle r = getComponentBounds(component,doPreferred);
      int newX;
      int newY;
      int newWidth;
      int newHeight;
      newX = getIntFromDouble(r.x*widthkoef);
      newY = getIntFromDouble(r.y*heightkoef);
      if (isPanel(component)) {
        newWidth = getIntFromDouble(r.width*widthkoef);
        newHeight = getIntFromDouble(r.height+heightkoef);
      } else if (isButton(component)) {
        newWidth = r.width;
        newHeight = r.height;
      } else {
        newWidth = getIntFromDouble(r.width*widthkoef);
        newHeight = r.height;
      }
      Rectangle newr = new Rectangle(newX,newY,newWidth,newHeight);
      return newr;
  }

  int getIntFromDouble(double d) {
    return new Double(d).intValue();
  }

  static final raXYConstraints defaultConstraints = new raXYConstraints();

  Rectangle getComponentBounds(Component component, boolean doPreferred) {
//      Rectangle constraints = (Rectangle)compBounds.get(component);
      raXYConstraints constraints = (raXYConstraints)compBounds.get(component);
      if(constraints == null) constraints = defaultConstraints;
      Rectangle r = new Rectangle(constraints.x, constraints.y, constraints.width, constraints.height);
      if(r.width <= 0 || r.height <= 0)
      {
          Dimension d = doPreferred ? component.getPreferredSize() : component.getMinimumSize();
          if(r.width <= 0)
              r.width = d.width;
          if(r.height <= 0)
              r.height = d.height;
      }
      return r;
  }

//marginalizacija
  private int wm = 15, hm = 15;
  public void setMargins(int _wm,int _hm) {
    wm = _wm;
    hm = _hm;
  }
}