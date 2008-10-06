/****license*****************************************************************
**   file: AutomatedTask.java
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
package hr.restart.sisfun;

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.borland.dx.dataset.ColumnAware;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */


public class AutomatedTask {
  private static int defaultTypingSpeed = 20;
  private static int defaultMouseSpeed = 4;
  private static int defaultActionDelay = 500;
  private int typingSpeed = defaultTypingSpeed;
  private int mouseSpeed = defaultMouseSpeed;
  private int actionDelay = defaultActionDelay;
  private static ExposedFocusManager efm = ExposedFocusManager.getInstance();

  protected Random rand = new Random(System.currentTimeMillis());

  static {
    GlobalEventListener.install();
  }

  public AutomatedTask() {
    if (!Borg.installed())
      throw new UnsupportedOperationException("Robot not installed");
  }

  public int getTypingSpeed() {
    return typingSpeed;
  }

  public int getMouseSpeed() {
    return mouseSpeed;
  }

  public int getActionDelay() {
    return actionDelay;
  }

  public void setTypingSpeed(int typingSpeed) {
    this.typingSpeed = typingSpeed;
  }

  public void setMouseSpeed(int mouseSpeed) {
    this.mouseSpeed = mouseSpeed;
  }

  public void setActionDelay(int actionDelay) {
    this.actionDelay = actionDelay;
  }

  public void type(String text) throws InterruptedException {
    Borg.setAutoDelay(typingSpeed);
    for (int i = 0; i < text.length(); i++)
      Borg.type(text.charAt(i));
    Borg.flush();
    Borg.setAutoDelay(0);
  }

  public void enter(String text) throws InterruptedException {
    type(text);
    Borg.delay(actionDelay / 4);
    Borg.flush();
    Borg.type(KeyEvent.VK_ENTER);
    Borg.delay(actionDelay);
    Borg.flush();
    Borg.setAutoDelay(0);
  }

  public void type(int keyCode) throws InterruptedException {
    this.type(keyCode, 0);
  }

  public void type(int keyCode, int keyMod) throws InterruptedException {
    Borg.setAutoDelay(typingSpeed);
    Borg.type(keyCode, keyMod);
    Borg.delay(typingSpeed * 2);
    Borg.flush();
    Borg.setAutoDelay(0);
  }

  public void delay(int delay) throws InterruptedException {
    Borg.delay(delay);
    Borg.flush();
  }

  public void tabToComponent(Component target) throws InterruptedException {
    HashSet visited = new HashSet();
    Component focused;
    Borg.setAutoDelay(0);
    Borg.type(KeyEvent.VK_SHIFT);
    Borg.flush();
    while ((focused = efm.getLastFocusedComponent()) != target && !visited.contains(focused)) {
      visited.add(focused);
      Borg.type(KeyEvent.VK_TAB, KeyEvent.CTRL_MASK);
      Borg.delay(actionDelay);
      Borg.type(KeyEvent.VK_SHIFT);
      Borg.flush();
    }
    if (focused != target)
      throw new AutomationException(this, "Can't focus target component");
    Borg.delay(actionDelay / 2);
  }

  public void clickComponent(Component target, int dx) throws InterruptedException {
    if (!target.isShowing())
      throw new AutomationException(this, "Can't click invisible component");
    click(target, dx);
  }

  public void clickComponent(Component target) throws InterruptedException {
    if (!target.isShowing())
      throw new AutomationException(this, "Can't click invisible component");
    click(target);
  }

  public void checkFocus(Component target) throws InterruptedException {
    Borg.type(KeyEvent.VK_SHIFT);
    Borg.flush();
    if (efm.getLastFocusedComponent() != target)
      throw new AutomationException(this, "Illegal focus");
  }

  public void clickMenu(JMenuBar root, JMenuItem target) throws InterruptedException {
    openMenu(root, target);
    click(target);
  }

  public void doubleClickComponent(Component target) throws InterruptedException {
    if (!target.isShowing())
      throw new AutomationException(this, "Can't click invisible component");
    point(target);
    Borg.delay(actionDelay / 5);
    Borg.click();
    Borg.delay(100);
    Borg.click();
    Borg.delay(actionDelay);
    Borg.flush();
    Borg.setAutoDelay(0);
  }

  protected Component findFocusedComponent() throws InterruptedException {
    Borg.type(KeyEvent.VK_SHIFT);
    Borg.flush();
    return efm.getLastFocusedComponent();
  }

  protected Window findFocusedWindow() throws InterruptedException {
    for (Component p = findFocusedComponent(); p != null; p = p.getParent())
      if (p instanceof Window) return (Window) p;
    return null;
  }


  private ColumnAware findColumnAware(Container c, String column, Class tc) {
    for (int i = 0; i < c.getComponentCount(); i++) {
      Component j = c.getComponent(i);
      if (j.getClass() == tc || tc == ColumnAware.class && tc.isAssignableFrom(j.getClass())) {
        ColumnAware ca = (ColumnAware) j;
        if (j.isVisible() && j.isEnabled() && column.equalsIgnoreCase(ca.getColumnName()))
          return ca;
      }
      if (j instanceof Container) {
        ColumnAware ca = findColumnAware((Container) j, column, tc);
        if (ca != null) return ca;
      }
    }
    return null;
  }

  protected ColumnAware findColumnAware(Container c, String column) {
    return findColumnAware(c, column, ColumnAware.class);
  }

  protected JlrNavField findNavField(Container c, String column) {
    return (JlrNavField) findColumnAware(c, column, JlrNavField.class);
  }

  protected JraTextField findTextField(Container c, String column) {
    return (JraTextField) findColumnAware(c, column, JraTextField.class);
  }

  protected raComboBox findComboBox(Container c, String column) {
    return (raComboBox) findColumnAware(c, column, raComboBox.class);
  }

  protected JraCheckBox findCheckBox(Container c, String column) {
    return (JraCheckBox) findColumnAware(c, column, JraCheckBox.class);
  }

  protected JraRadioButton findRadioButton(Container c, String column) {
    return (JraRadioButton) findColumnAware(c, column, JraRadioButton.class);
  }

  protected JMenuItem findMenuItem(JComponent j, String text) {
    int num = j instanceof JMenuBar ?
              ((JMenuBar) j).getMenuCount() :
              ((JMenu) j).getItemCount();
    for (int i = 0; i < num; i++) {
      Component c = j instanceof JMenuBar ? ((JMenuBar) j).getMenu(i) : ((JMenu) j).getItem(i);
      if (c instanceof JMenu) {
        JMenuItem mi = findMenuItem((JMenu) c, text);
        if (mi != null) return mi;
      } else if (c instanceof JMenuItem) {
        JMenuItem mi = (JMenuItem) c;
        if (mi.isEnabled() && mi.getText().trim().equalsIgnoreCase(text.trim()))
          return mi;
      }
    }
    return null;
  }

  protected void point(Component comp, int dx) throws InterruptedException {
    Borg.setAutoDelay(mouseSpeed);
    Point ls = comp.getLocationOnScreen();
    Borg.move(ls.x + dx, ls.y + comp.getHeight() / 2);
  }

  protected void point(Component comp) throws InterruptedException {
    point(comp, comp.getWidth() / 2);
  }

  protected void click(Component comp, int dx) throws InterruptedException {
    point(comp, dx);
    Borg.delay(actionDelay / 5);
    Borg.click();
    Borg.delay(actionDelay / 2);
    Borg.flush();
    Borg.setAutoDelay(0);
  }

  protected void click(Component comp) throws InterruptedException {
    click(comp, comp.getWidth() / 2);
  }

  private void openMenu(Container menu, JMenuItem target) throws InterruptedException {
    if (menu instanceof JMenuBar) {
      for (int i = 0; i < ((JMenuBar) menu).getMenuCount(); i++)
        if (findMenuItem(((JMenuBar) menu).getMenu(i), target.getText()) != null) {
          click(((JMenuBar) menu).getMenu(i));
          Borg.delay(actionDelay + ((JMenu) ((JMenuBar) menu).getMenu(i)).getDelay());
          Borg.flush();
          openMenu(((JMenuBar) menu).getMenu(i), target);
        }
    } else if (menu instanceof JMenu) {
      for (int j = 0; j < ((JMenu) menu).getItemCount(); j++)
        if (((JMenu) menu).getItem(j) instanceof JMenu &&
            findMenuItem(((JMenu) menu).getItem(j), target.getText()) != null) {
          point(((JMenu) menu).getItem(j));
          Borg.delay(actionDelay + ((JMenu) menu).getDelay());
          Borg.flush();
          point(((JMenu) ((JMenu) menu).getItem(j)).getItem(0));
          openMenu(((JMenu) menu).getItem(j), target);
        }
    }
  }

/*  private void pointMenu(Container menu) throws InterruptedException {
    if (menu instanceof JMenu) {
      if (!menu.isShowing()) {
        pointMenu((JMenu) menu.getAccessibleContext().getAccessibleParent());
        point(menu);
      } else click(menu);
      Borg.delay(actionDelay + ((JMenu) menu).getDelay());
      Borg.flush();
      point(((JMenu) menu).getItem(0));
    } else
      throw new AutomationException(this, "Can't click invisible menu");
  } */
}

/*class StringAction implements AutomatedAction {
  private String text;
  private int globalDelay, actionDelay;
  public StringAction(String text, AutomatedTask owner) {
    this(text, owner.getTypingSpeed(), owner.getActionDelay());
  }
  public StringAction(String text, int globalDelay, int actionDelay) {
    this.text = text;
    this.globalDelay = globalDelay;
    this.actionDelay = actionDelay;
  }
  public void perform() throws InterruptedException {
    Borg.setAutoDelay(globalDelay);
    for (int i = 0; i < text.length(); i++)
      Borg.type(text.charAt(i));
    Borg.delay(actionDelay);
    Borg.type(KeyEvent.VK_ENTER);
    Borg.delay(actionDelay);
    Borg.flush();
    Borg.setAutoDelay(0);
  }
}

class KeypressAction implements AutomatedAction {
  private int keyCode, keyMod, actionDelay;
  public KeypressAction(int keyCode, AutomatedTask owner) {
    this(keyCode, 0, owner.getActionDelay());
  }
  public KeypressAction(int keyCode, int actionDelay) {
    this(keyCode, 0, actionDelay);
  }
  public KeypressAction(int keyCode, int keyMod, AutomatedTask owner) {
    this(keyCode, keyMod, owner.getActionDelay());
  }
  public KeypressAction(int keyCode, int keyMod, int actionDelay) {
    this.keyCode = keyCode;
    this.keyMod = keyMod;
    this.actionDelay = actionDelay;
  }
  public void perform() throws InterruptedException {
    Borg.setAutoDelay(20);
    if ((keyMod & KeyEvent.ALT_GRAPH_MASK) > 0)
      Borg.press(KeyEvent.VK_ALT_GRAPH);
    if ((keyMod & KeyEvent.ALT_MASK) > 0)
      Borg.press(KeyEvent.VK_ALT);
    if ((keyMod & KeyEvent.SHIFT_MASK) > 0)
      Borg.press(KeyEvent.VK_SHIFT);
    if ((keyMod & KeyEvent.CTRL_MASK) > 0)
      Borg.press(KeyEvent.VK_CONTROL);
    Borg.type(keyCode);
    if ((keyMod & KeyEvent.ALT_GRAPH_MASK) > 0)
      Borg.release(KeyEvent.VK_ALT_GRAPH);
    if ((keyMod & KeyEvent.ALT_MASK) > 0)
      Borg.release(KeyEvent.VK_ALT);
    if ((keyMod & KeyEvent.SHIFT_MASK) > 0)
      Borg.release(KeyEvent.VK_SHIFT);
    if ((keyMod & KeyEvent.CTRL_MASK) > 0)
      Borg.release(KeyEvent.VK_CONTROL);
    Borg.delay(actionDelay);
    Borg.flush();
    Borg.setAutoDelay(0);
  }
}

class DelayAction implements AutomatedAction {
  private int delay;
  public DelayAction(int delay) {
    this.delay = delay;
  }
  public void perform() throws InterruptedException {
    Borg.delay(delay);
  }
}

class TabTargetAction implements AutomatedAction {
  private static ExposedFocusManager efm = ExposedFocusManager.getInstance();
  private Component target;
  private int delay;
  private AutomatedTask owner;
  public TabTargetAction(Component target, AutomatedTask owner) {
    this(target, owner.getActionDelay());
    this.owner = owner;
  }
  public TabTargetAction(Component target, int delay) {
    this.target = target;
    this.delay = delay;
  }
  public void perform() throws InterruptedException {
    HashSet visited = new HashSet();
    efm.setPriorityKeyListener(target, ignoreTab);
    Component focused;
    try {
      while ((focused = efm.getLastFocusedComponent()) != target && !visited.contains(focused)) {
        visited.add(focused);
        Borg.type(KeyEvent.VK_TAB);
        Borg.delay(delay);
        Borg.flush();
      }
    } finally {
      efm.removePriorityKeyListener(target);
    }
    if (focused != target)
      throw new AutomationException(owner, "Can't focus target component");
    Borg.delay(delay * 2);
  }
  private KeyAdapter ignoreTab = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == e.VK_TAB)
        e.consume();
    }
  };
}

class ClickComponentAction implements AutomatedAction {
  protected AutomatedTask owner;
  protected Component target;
  protected int mouseSpeed, actionDelay;
  public ClickComponentAction(Component target, AutomatedTask owner) {
    this(target, owner.getMouseSpeed(), owner.getActionDelay());
    this.owner = owner;
  }
  public ClickComponentAction(Component target, int mouseSpeed, int actionDelay) {
    this.target = target;
    this.mouseSpeed = mouseSpeed;
    this.actionDelay = actionDelay;
  }
  protected void point(Component comp) throws InterruptedException {
    Borg.setAutoDelay(mouseSpeed);
    Point ls = comp.getLocationOnScreen();
    Borg.move(ls.x + comp.getWidth() / 2, ls.y + comp.getHeight() / 2);
  }
  protected void click() throws InterruptedException {
    Borg.delay(actionDelay);
    Borg.click();
    Borg.delay(actionDelay);
    Borg.flush();
    Borg.setAutoDelay(0);
  }
  public void perform() throws InterruptedException {
    if (!target.isShowing())
      throw new AutomationException(owner, "Can't click invisible component");
    point(target);
    click();
  }
}

class ClickMenuAction extends ClickComponentAction {
  public ClickMenuAction(JMenuItem target, AutomatedTask owner) {
    super(target, owner);
  }
  public ClickMenuAction(JMenuItem target, int mouseSpeed, int actionDelay) {
    super(target, mouseSpeed, actionDelay);
  }
  public void perform() throws InterruptedException {
    if (!target.isShowing()) pointMenu(target.getParent());
    point(target);
    click();
  }
  private void pointMenu(Container menu) throws InterruptedException {
    if (menu instanceof JMenu) {
      if (!menu.isShowing()) pointMenu(menu.getParent());
      point(menu);
      Borg.delay(actionDelay);
      Borg.flush();
    } else
      throw new AutomationException(owner, "Can't click invisible menu");
  }
}

/*class CloseDialogAction extends ClickComponentAction {
  public CloseDialogAction(Dialog target, AutomatedTask owner) {
    super(target, owner);
  }
  public CloseDialogAction(Dialog target, int mouseSpeed, int actionDelay) {
    super(target, mouseSpeed, actionDelay);
  }
  public void perform() throws InterruptedException {
    if (!target.isShowing())
      throw new AutomationException(owner, "Can't close invisible dialog");
    ((Dialog) target).toFront();
    Borg.flush();

  }
}
*/