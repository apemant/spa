/****license*****************************************************************
**   file: raTopLineBorder.java
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;

public class raTopLineBorder extends AbstractBorder {

  public void paintBorder(Component c,
                        Graphics g,
                        int x,
                        int y,
                        int width,
                        int height) {

    int parw = width;
    if (c instanceof JComponent) {
      Container adam = ((JComponent)c).getTopLevelAncestor();
      if (adam!=null) parw = adam.getWidth();
    }
    g.drawLine(0,0,parw,0);
//    g.drawLine(0,height-1,parw,height-1);
//    g.drawRoundRect(x, y, width-1, height-1, 1, 1);
    javax.swing.border.EtchedBorder e;
  }
}