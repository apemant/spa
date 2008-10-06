/****license*****************************************************************
**   file: raCenteredXYLayout.java
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
import java.awt.Rectangle;
public class raCenteredXYLayout extends raXYLayout {
  public static int LEFT = 2;
  public static int RIGHT = 4;
  public static int TOP = 8;
  public static int BOTTOM = 16;
  public static int CENTER = 32;
  private int alignments;
  public raCenteredXYLayout() {
    this(CENTER);
  }
  public raCenteredXYLayout(int _alignments) {
    super(false);
    alignments=_alignments;
    setMargins(0,0);
  }
  public void layoutContainer(Container target) {
    super.layoutContainer(target);
    Dimension layoutSize = preferredLayoutSize(target);
    Dimension targetSize = target.getSize();
    Rectangle compRelPos = new Rectangle();
    if (!(layoutSize.height > targetSize.height || layoutSize.width > targetSize.width)) {
      if (alignments == CENTER) {
        compRelPos.x = (targetSize.width - layoutSize.width) / 2;
        compRelPos.y = (targetSize.height - layoutSize.height) / 2;
      } else if (alignments == LEFT) {
        compRelPos.x = 0;
        compRelPos.y = (targetSize.height - layoutSize.height) / 2;
      } else if (alignments == RIGHT) {
        compRelPos.x = targetSize.width;
        compRelPos.y = (targetSize.height - layoutSize.height) / 2;
      } else if (alignments == TOP) {
        compRelPos.x = (targetSize.width - layoutSize.width) / 2;
        compRelPos.y = 0;
      } else if (alignments == BOTTOM) {
        compRelPos.x = (targetSize.width - layoutSize.width) / 2;
        compRelPos.y = targetSize.height;
      }
    }
//    v_needed = false;
    for (int i=0;i<target.getComponentCount();i++) {
      Component c = target.getComponent(i);
      int _W = c.getBounds().width;
/*      v_needed = target.getParent() != null
               //&& target.getParent().getWidth() != old_W
               //&& c instanceof javax.swing.JPanel
               && c instanceof javax.swing.JTabbedPane
              ;
      if (v_needed) {
 //       System.out.println("adjusting target ("+target.getClass()+")on "+target.getParent().getBounds().width);
        _W = target.getParent().getWidth();
      }*/
      c.setBounds(
        c.getBounds().x + compRelPos.x, c.getBounds().y + compRelPos.y,
        _W, c.getBounds().height
      );
    }
    if (target.getParent() != null) {
      Rectangle oldbounds = target.getBounds();
      target.setBounds(oldbounds.x,oldbounds.y,target.getParent().getWidth(),oldbounds.height);
    }
  }
//  private boolean v_needed;
/*
  public Dimension preferredLayoutSize(Container target) {
    Dimension dim = super.preferredLayoutSize(target);
    //vrati marginicu ...
      dim.width = dim.width - 15;
      dim.height = dim.height - 15;
    //
    return dim;
  }
*/
  public Dimension minimumLayoutSize(Container parent) {
    return new Dimension(0,0);
  }
}