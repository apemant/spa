/****license*****************************************************************
**   file: raFieldMask.java
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

import hr.restart.util.VarStr;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public abstract class raFieldMask implements FocusListener, KeyListener {
  protected JTextField tf;
  protected boolean insert = true, foc;

  protected static JTextField sharedTx = new JTextField();
  protected static VarStr sharedBuf = new VarStr();
  
  protected int sBeg, sEnd, cPos, tLen, sLen;
  protected boolean sel, prot;

//  public abstract void updateText();
//  public abstract void resolveText();
  public abstract boolean keypressBackspace();
  public abstract boolean keypressCut();
  public abstract boolean keypressCopy();
  public abstract boolean keypressPaste();
  public abstract boolean keypressDelete();
  public abstract boolean keypressCharacter(char ch);

  public boolean keypressCode(int code) {
    return false;
  }
  public boolean keypressControl(char ch) {
    return false;
  }

  public raFieldMask(JTextField tf) {
    this.tf = tf;
    if (!(tf instanceof JraTextField))
      tf.addFocusListener(this);
    tf.addKeyListener(this);
    if (tf instanceof JraTextField)
      ((JraTextField) tf).setFieldMask(this);
  }

  protected void setText(String s) {
    tf.selectAll();
    tf.replaceSelection(s);
  }

  public void uninstall() {
    tf.removeFocusListener(this);
    tf.removeKeyListener(this);
  }
  
  public void setProtected(boolean prot) {
    System.out.println("protected "+prot);
    this.prot = prot;
  }
  
  public boolean isProtected() {
    return prot;
  }

  public void setOverwriteMode(boolean over) {
    insert = !over;
  }

  protected void findPos() {
    cPos = tf.getCaretPosition();
    tLen = tf.getDocument().getLength();
  }

  protected void findSelection() {
    sBeg = tf.getSelectionStart();
    sEnd = tf.getSelectionEnd();
    sLen = sEnd - sBeg;
    sel = sBeg < sEnd;
  }
  
  public void cacheDynamicVariables() {
    findPos();
    findSelection();
  }

  public void keyTyped(KeyEvent e) {
    if (!tf.isEnabled()) return;

    char ch = e.getKeyChar();
    cacheDynamicVariables();
    if (!prot && ch == '\b' && keypressBackspace()) e.consume();
    if (!prot && ch < ' ' && keypressControl(ch)) e.consume();
    if (!prot && !e.isControlDown() && !e.isAltDown() && !e.isAltGraphDown()) {
      if (ch >= ' ' && keypressCharacter(ch)) e.consume();
    }
    if (prot && !e.isConsumed()) e.consume();
    if (e.isConsumed()) foc = false;
  }

  public void keyPressed(KeyEvent e) {
    if (!tf.isEnabled()) return;

    int code = e.getKeyCode();
    boolean ctrl = e.isControlDown();
//    if (code != e.VK_ENTER && code != e.VK_TAB && code != e.VK_F10 &&
//        tf.getDocument().getLength() == 0)
//      focusGained(null);
    cacheDynamicVariables();
    if (!prot && code == e.VK_INSERT) insert = !insert;
    if (!prot && code == e.VK_X && ctrl && keypressCut()) e.consume();
    if (code == e.VK_C && ctrl && keypressCopy()) e.consume();
    if (!prot && code == e.VK_V && ctrl && keypressPaste()) e.consume();
    if (!prot && code == e.VK_DELETE && keypressDelete()) e.consume();
    if (!prot && code == e.VK_BACK_SPACE) e.consume();
    if (!prot && !e.isConsumed() && keypressCode(code)) e.consume();
    if (prot && !e.isConsumed()) {
      if (code != e.VK_LEFT && code != e.VK_RIGHT &&
          code != e.VK_UP && code != e.VK_DOWN &&
          code != e.VK_PAGE_UP && code != e.VK_PAGE_DOWN &&
          code != e.VK_HOME && code != e.VK_END)
        e.consume();
    }
    if (e.isConsumed()) foc = false;
  }

  public void keyReleased(KeyEvent e) {
  }

  public void focusGained(FocusEvent e) {
    foc = true;
  }

  public void focusLost(FocusEvent e) {
  }
}

/*class BlockCaret extends DefaultCaret {
  protected synchronized void damage(Rectangle r) {
    if (r != null) {
      x = r.x - 4;
      y = r.y;
      width = r.height * 2;
      height = r.height;
      repaint();
    }
  }
  public void paint(Graphics g) {
    if (isVisible()) {
      try {
        TextUI mapper = getComponent().getUI();
        Rectangle r = mapper.modelToView(getComponent(), getDot(), Position.Bias.Forward);
        x = r.x - 4;
        y = r.y;
        width = 10;
        height = r.height;
        g.setColor(getComponent().getCaretColor());
        try {
          Rectangle r2 = mapper.modelToView(getComponent(), getDot() + 1, Position.Bias.Forward);
          width = r2.x - r.x + 8;
          g.drawRect(r.x, r.y, r2.x - r.x, r.height - 1);
        } catch (BadLocationException e) {
          g.drawLine(r.x, r.y, r.x, r.y + r.height - 1);
        }
      } catch (BadLocationException e) {
        // can't render I guess
        //System.err.println("Can't render cursor");
      }
    }
  }
}
*/