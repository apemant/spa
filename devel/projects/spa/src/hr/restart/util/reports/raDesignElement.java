/****license*****************************************************************
**   file: raDesignElement.java
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
package hr.restart.util.reports;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raDesignElement extends JPanel {
  public static final int TEXT = 0;
  public static final int PIC = 1;
  public static final int HLINE = 2;
  public static final int VLINE = 3;

  private int type;
  private JPanel up, down, left, right, leftUp, leftDown, rightUp, rightDown;
  private JLabel lab;

  public raDesignElement() {
    this(TEXT);
  }

  public raDesignElement(int type) {
    super(new BorderLayout());
    this.type = type;
    if (type == TEXT || type == PIC) initLabel();
    else initLine();
  }

  private void initLabel() {
    add(lab = new JLabel(), BorderLayout.CENTER);
    add(up = new JPanel(new BorderLayout()), BorderLayout.NORTH);
    add(down = new JPanel(new BorderLayout()), BorderLayout.SOUTH);
    add(left = new JPanel(), BorderLayout.WEST);
    add(right = new JPanel(), BorderLayout.EAST);
    up.add(leftUp = new JPanel(), BorderLayout.WEST);
    up.add(rightUp = new JPanel(), BorderLayout.EAST);
    down.add(leftDown = new JPanel(), BorderLayout.WEST);
    down.add(rightDown = new JPanel(), BorderLayout.EAST);

    left.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
    right.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
    up.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
    down.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
    leftUp.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
    leftDown.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
    rightUp.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
    rightDown.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
  }

  private void initLine() {
  }

  public class Label extends JLabel {

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.setColor(Color.lightGray);

    }
  }
}

