/****license*****************************************************************
**   file: raTypeaheadWindow.java
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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.JWindow;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raTypeaheadWindow {
  private JWindow tip = null;
  private JToolTip jtt = null;
  private ExpireTimer exp = null;
  JComponent invoker = null;
  static raTypeaheadWindow inst = new raTypeaheadWindow();
  static {
    AWTKeyboard.registerKeyStroke(AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        if (inst.invoker == null) return false;
        hideTip();
        return true;        
      }
    });
  }
  private raTypeaheadWindow() {
  }

  public static void setTip(JComponent comp, String text, Point origin) {
    inst.update(comp, text, origin);
  }

  public static void hideTip() {
    inst.hide();
  }

  public static boolean isShowing(JComponent comp) {
    return inst.invoker == comp && inst.exp != null && inst.exp.isRunning();
  }

  private void hide() {
    if (exp != null) {
      exp.destroy();
      exp = null;
    }
    if (tip != null) {
      tip.dispose();
      tip = null;
    }
    invoker = null;
    jtt = null;
  }

  private void update(JComponent comp, String text, Point origin) {
    if (invoker != null && comp != invoker) hide();
    if (invoker == null || !exp.isRunning()) {
      invoker = comp;
      tip = new JWindow();
      jtt = comp.createToolTip();
      jtt.setOpaque(true);
      exp = new ExpireTimer(tip);
      jtt.setTipText(text);
      tip.setContentPane(jtt);
      tip.setSize(200, jtt.getPreferredSize().height);
      tip.setLocation(origin);
      tip.setBackground(jtt.getBackground());
      tip.show();
      exp.start();
    } else {
      exp.refresh();
      jtt.setTipText(text);
      jtt.repaint();
      if (!tip.getLocation().equals(origin))
        tip.setLocation(origin);

    }
  }
}

class ExpireTimer extends javax.swing.Timer {
  JWindow targ;
  public ExpireTimer(JWindow target) {
    super(4000, null);
    setRepeats(false);
    targ = target;
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        destroy();
      }
    });
  }
  public void destroy() {
    super.stop();
    if (targ != null) {
      targ.dispose();
      targ = null;
    }
  }
  public void refresh() {
    if (super.isRunning())
      super.restart();
  }
}
