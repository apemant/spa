/****license*****************************************************************
**   file: JraKeyListener.java
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

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JraKeyListener implements KeyListener {
/**
 * Ako je pritisnuta tipka ENTER prebacuje focus na slijedecu komponentu
 * metodom {@link #focusNext(java.util.EventObject) focusNext(EventObject)}.
 */
  private int[] keysForParent;
  private static int[] defaultKeyForParent = new int[] {KeyEvent.VK_F10,KeyEvent.VK_ESCAPE};
  public static int[] allFuncKeys = new int[] {KeyEvent.VK_F10, KeyEvent.VK_F1, KeyEvent.VK_F2,
      KeyEvent.VK_F3, KeyEvent.VK_F4, KeyEvent.VK_F5, KeyEvent.VK_F6, KeyEvent.VK_F7, 
      KeyEvent.VK_F8, KeyEvent.VK_F9, KeyEvent.VK_DELETE, KeyEvent.VK_INSERT,
      KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_UP, KeyEvent.VK_DOWN,
      KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ESCAPE};
  boolean transferAllKeysToParent = false;
/**
 * Konstruktor sa defaultnim keyForParent = = new int[] {KeyEvent.VK_F10,KeyEvent.VK_ESCAPE}.
 */
  public JraKeyListener() {
    super();
    keysForParent = defaultKeyForParent;
  }
/**
 * Konstruktor sa zadanim keyForParent.
 * keyForParent je niz keyCodova (e.getKeyCode) za koje se event okida na topLevelAncestoru
 */
  public JraKeyListener(int[] keysForParentC) {
    super();
    if (keysForParentC!=null) {
      keysForParent = keysForParentC;
    } else {
      keysForParent = new int[] {};
    }
  }

  public JraKeyListener(int[] keysForParentC, boolean transferAllKeysToParentC) {
    this(keysForParentC);
    setTransferAllKeysToParent(transferAllKeysToParentC);
  }

  public void keyReleased(KeyEvent e) {
    enterPressed(e);
  }
  public void keyPressed(KeyEvent e) {
    if (isExtraKey(e)) return;
    if (isKeyForParent(e)) {
      if (e.isConsumed()) return;
      if (!((java.awt.Component)e.getSource()).isShowing()) return;
      transferKeyToParent(e);
    }
//    enterPressed(e);
  }
  public void keyTyped(KeyEvent e) {

  }
  public static boolean enterNow = true;
  private void enterPressed(KeyEvent e) {
      if (e.getKeyCode() == e.VK_ENTER) {
        if (e.isConsumed()) return;
        if (enterNow) {
          focusNext(e);
        } else {
          enterNow = true;
        }
        e.consume();
    }
  }
  /**
   * Zove FocusManager i kaze mu da prebaci fokus na slijedecu komponentu
   */
  public static void focusNext(java.util.EventObject e) {
    javax.swing.FocusManager.getCurrentManager().focusNextComponent((java.awt.Component)e.getSource());
/*    javax.swing.JComponent nextOwner = getFocusOwner(e);
    if (nextOwner != null) {
      System.out.println("nextOwner = "+nextOwner);
      nextOwner.scrollRectToVisible(nextOwner.getBounds());
    } else System.out.println("nextOwner je null");*/
  }
/*  private static javax.swing.JComponent getFocusOwner(java.util.EventObject e) {
    Object source = e.getSource();
    if (source instanceof javax.swing.JComponent) {
      javax.swing.JComponent srccomp = (javax.swing.JComponent)source;
      Container toplevelanc = srccomp.getTopLevelAncestor();
      if (toplevelanc == null) return null;
      if (!(toplevelanc instanceof java.awt.Window)) return null;
      Window topwin = (Window)toplevelanc;
      Component focOwner = topwin.getFocusOwner();
      if (!(focOwner instanceof javax.swing.JComponent)) return null;
      return (javax.swing.JComponent)focOwner;
    } else return null;
  }*/
  /**
   * poziva keypressed od toplevelancestora
   */
  public void transferKeyToParent(KeyEvent e) {
    KeyListener[] kyl;
    java.awt.Component son = (java.awt.Component)e.getSource();
    kyl = (KeyListener[])son.getListeners(KeyListener.class);
    processKyl(kyl,e);
//    if (!e.isConsumed()) {
      //parent
//      java.awt.Container papa = ((javax.swing.JComponent)e.getSource()).getTopLevelAncestor();
      java.awt.Container papa = getKeyAncestor((java.awt.Component)e.getSource());
      kyl = (KeyListener[])papa.getListeners(KeyListener.class);
      processKyl(kyl,e);
      e.consume();
//    }
  }
  public Container getKeyAncestor(java.awt.Component cmp) {
    Container retParent = cmp.getParent();
    Container currParent = cmp.getParent();
    while (currParent != null){
      currParent = currParent.getParent();
      if (currParent == null) break;
      retParent = currParent;
      if (retParent instanceof javax.swing.JInternalFrame) break;
      if (retParent instanceof java.awt.Window) break;
//      if (currParent instanceof javax.swing.JTabbedPane) break;
    }
    return retParent;
  }

  private void processKyl(KeyListener[] kyl,KeyEvent e) {
    for (int i=0;i<kyl.length;i++) {
      if (!kyl[i].equals(this)) kyl[i].keyPressed(e);
    }
  }
  private boolean isKeyForParent(KeyEvent e) {
    if (transferAllKeysToParent) return true;
    int[] keysForParentIsKeyForParent = getKeysForParentIsKeyForParent(e);
    for (int i=0;i<keysForParentIsKeyForParent.length;i++) {
      if (keysForParentIsKeyForParent[i] == e.getKeyCode()) return true;
    }
    return false;
  }
  private int[] getKeysForParentIsKeyForParent(KeyEvent e) {
    if (e.getSource() instanceof javax.swing.JComboBox) {
      if (((javax.swing.JComboBox)e.getSource()).isPopupVisible()) return keysForParent;
      int[] keysForParent2 = new int[keysForParent.length+1];
      for (int i=0;i<keysForParent.length;i++) keysForParent2[i] = keysForParent[i];
      keysForParent2[keysForParent.length] = e.VK_ENTER;
      return keysForParent2;
    } else {
      return keysForParent;
    }
  }
  private boolean isExtraKey(KeyEvent e) {
    if (e.getSource() instanceof javax.swing.JComboBox) {
      javax.swing.JComboBox jcb = (javax.swing.JComboBox)e.getSource();
      if (jcb.isPopupVisible()) return true;
    }
    return false;
  }

  public void setTransferAllKeysToParent(boolean p) {
    transferAllKeysToParent = p;
  }

  public boolean isTransferAllKeysToParent() {
    return transferAllKeysToParent;
  }
}