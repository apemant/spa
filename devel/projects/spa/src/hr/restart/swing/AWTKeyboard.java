/****license*****************************************************************
**   file: AWTKeyboard.java
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
/*
 * Created on Sep 2, 2004
 *
 */
package hr.restart.swing;

import hr.restart.util.IntParam;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.KeyStroke;


/**
 * @author abf
 *
 */
public class AWTKeyboard implements AWTEventListener {
  static AWTKeyboard inst = new AWTKeyboard();
  static {
    Toolkit.getDefaultToolkit().addAWTEventListener(inst, AWTEvent.KEY_EVENT_MASK);
    initTracking();
  }
  
  public static KeyStroke F1 = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
  public static KeyStroke F2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
  public static KeyStroke F3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
  public static KeyStroke F4 = KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0);
  public static KeyStroke F5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
  public static KeyStroke F6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
  public static KeyStroke F7 = KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0);
  public static KeyStroke F8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
  public static KeyStroke F9 = KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0);
  public static KeyStroke F10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);
  
  public static KeyStroke ESC = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
  public static KeyStroke ENTER = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
  public static KeyStroke ENTER_RELEASED = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
  
  public static KeyStroke TAB = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
  
  public static KeyStroke BACKSPACE = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);
  public static KeyStroke SPACE = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
  public static KeyStroke TYPE_SPACE = KeyStroke.getKeyStroke(' ');
  
  public static KeyStroke UP = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
  public static KeyStroke DOWN = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
  public static KeyStroke LEFT = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
  public static KeyStroke RIGHT = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
  
  public static KeyStroke PAGE_UP = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0);
  public static KeyStroke PAGE_DOWN = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0);
  public static KeyStroke HOME = KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0);
  public static KeyStroke END = KeyStroke.getKeyStroke(KeyEvent.VK_END, 0);
  
  public static KeyStroke INSERT = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0);
  public static KeyStroke DELETE = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
  
  private Component foc = null;
  
  HashMap actionMap = new HashMap();
  ContainerKeyData global = new ContainerKeyData();
  boolean track = false;
  boolean fallthrough = false;
  
  private KeyAction keyConsumer = new KeyAction() {
    public boolean actionPerformed() {
      return true;
    }
  };
  
  private KeyAction keyIgnorer = new KeyAction() {
    public boolean actionPerformed() {
    	fallthrough = true;
      return false;
    }
  };
  
  private AWTKeyboard() {
    // singleton
  }
  
  static void initTracking() {
    String _tr = IntParam.getTag("AWTKeyboardTracking");
    if (_tr == null || _tr.length() == 0)
      IntParam.setTag("AWTKeyboardTracking", "false");
    else if (_tr.toLowerCase().equals("true"))
      setTracking(true);
  }
  
  public static void setTracking(boolean track) {
    inst.track = track;
  }
    
  public void eventDispatched(AWTEvent e) {
    KeyEvent ke = (KeyEvent) e;
    
    if (ke.getKeyCode() == ke.VK_F12 && ke.getID() == ke.KEY_PRESSED)
      track = (ke.getModifiers() & ke.SHIFT_MASK) > 0;

    if (track) System.err.println("*AWT-KEY*: "+ke);    
    global.dispatchEvent(ke);
    fallthrough = false;
    Component c = foc = ke.getComponent();
    while (!ke.isConsumed() && c != null && !fallthrough) {
      if (track) System.err.println("AWTCHECK: "+c.getClass().getName());
      ContainerKeyData ckd = (ContainerKeyData) actionMap.get(c);
      if (ckd != null) ckd.dispatchEvent(ke);
      if (c instanceof Window) break;
      c = c.getParent();
    }
  }
  
  public static Component getFocusedComponent() {
    return inst.foc;
  }
  
  public static void registerKeyListener(KeyListener listener) {
    inst.global.addKeyListener(listener);    
  }
  
  public static void unregisterKeyListener(KeyListener listener) {
    inst.global.removeKeyListener(listener);
  }
  
  public static void registerKeyStroke(KeyStroke key, KeyAction action) {
    inst.global.addKeyStroke(key, action);
  }
  
  public static void unregisterKeyStroke(KeyStroke key) {
    inst.global.removeKeyStroke(key);
  }
  
  public static void unregisterKeyStroke(KeyStroke key, KeyAction action) {
    inst.global.removeKeyStrokeAction(key, action);
  }
  
  public static void ignoreKeyStroke(KeyStroke key) {
    registerKeyStroke(key, inst.keyConsumer);
  }
  
  public static void unignoreKeyStroke(KeyStroke key) {
    unregisterKeyStroke(key);
  }
  
  public static void registerKeyListener(Component comp, KeyListener listener) {
    if (inst.track) System.err.println("registering listener on "+comp);
    ContainerKeyData ckd = (ContainerKeyData) inst.actionMap.get(comp);
    if (ckd == null) inst.actionMap.put(comp, ckd = new ContainerKeyData());
    ckd.addKeyListener(listener);
  }
  
  public static void unregisterKeyListener(Component comp, KeyListener listener) {
    if (inst.track) System.err.println("unregistering listener on "+comp);
    ContainerKeyData ckd = (ContainerKeyData) inst.actionMap.get(comp);
    if (ckd != null) ckd.removeKeyListener(listener);
  }
  
  public static void registerKeyStroke(Component comp, KeyStroke key, KeyAction action) {
    if (inst.track) System.err.println("registering keystroke "+key+" on "+comp);
    ContainerKeyData ckd = (ContainerKeyData) inst.actionMap.get(comp);
    if (ckd == null) inst.actionMap.put(comp, ckd = new ContainerKeyData());
    ckd.addKeyStroke(key, action);    
  }
  
  public static void unregisterKeyStroke(Component comp, KeyStroke key) {
    if (inst.track) System.err.println("unregistering keystroke "+key+" on "+comp);
    ContainerKeyData ckd = (ContainerKeyData) inst.actionMap.get(comp);
    if (ckd != null) ckd.removeKeyStroke(key);
  }
  
  public static void unregisterKeyStroke(Component comp, KeyStroke key, KeyAction action) {
    if (inst.track) System.err.println("unregistering keystroke "+key+" action on "+comp);
    ContainerKeyData ckd = (ContainerKeyData) inst.actionMap.get(comp);
    if (ckd != null) ckd.removeKeyStrokeAction(key, action);
  }
  
  public static void ignoreKeyStroke(Component comp, KeyStroke key) {
    registerKeyStroke(comp, key, inst.keyConsumer);
  }
  
  public static void unignoreKeyStroke(Component comp, KeyStroke key) {
    unregisterKeyStroke(comp, key);
  }
  
  public static void bindKeyStroke(Component comp, KeyStroke key) {
    registerKeyStroke(comp, key, inst.keyIgnorer);
  }
  
  public static void unbindKeyStroke(Component comp, KeyStroke key) {
    unregisterKeyStroke(comp, key);
  }
  
  public static void ignoreKeyRelease(KeyStroke key) {
    KeyReleaseConsumer.ignoreReleased(key);
  }
  
  public static void unregisterComponent(Component comp) {
    if (inst.track) System.err.println("unregistering component "+comp);
    inst.actionMap.remove(comp);
  }
  
  public static void unregisterGlobals() {
    inst.global = new ContainerKeyData();
  }
  
  public static void unregisterAll() {
    inst.actionMap.clear();
    inst.global = new ContainerKeyData();
  }
  
  public static int mapSize() {
    return inst.actionMap.size();
  }
  
  public static void dumpStatistics() {
    System.out.println("Components: " + mapSize());
    int keys = 0, acts = 0, lists = 0;
    for (Iterator i = inst.actionMap.values().iterator(); i.hasNext(); ) {
      ContainerKeyData ckd = (ContainerKeyData) i.next();
      keys += ckd.keystrokes.size();
      lists += ckd.keyListeners.size();
      for (Iterator k = ckd.keystrokes.values().iterator(); k.hasNext(); ) {
        ActionList al = (ActionList) k.next();
        acts += al.acts.size();
      }
    }
    System.out.println("Listeners: " + lists);
    System.out.println("Keys: " + keys);
    System.out.println("Actions: " + acts);
  }
}

class ContainerKeyData {
  ArrayList keyListeners = new ArrayList();
  HashMap keystrokes = new HashMap();
  
  public void addKeyListener(KeyListener l) {
    keyListeners.add(l);
  }
  
  public void removeKeyListener(KeyListener l) {
    keyListeners.remove(l);
  }
  
  public void addKeyStroke(KeyStroke k, KeyAction ka) {
    keystrokes.put(k, ActionList.addKeyAction((ActionList) keystrokes.get(k), ka));
  }
  
  public void removeKeyStroke(KeyStroke k) {
    keystrokes.remove(k);
  }
  
  public void removeKeyStrokeAction(KeyStroke k, KeyAction ka) {
    ActionList al = (ActionList) keystrokes.get(k);
    if (al != null && al.removeKeyAction(ka)) keystrokes.remove(k);
  }
  
  public void dispatchEvent(KeyEvent e) {
    KeyStroke ks;
    switch (e.getID()) {
      case KeyEvent.KEY_PRESSED:
        ks = KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers());
        if (keystrokes.containsKey(ks))
          ((ActionList) keystrokes.get(ks)).dispatchEvent(e);
        for (int i = 0; i < keyListeners.size() && !e.isConsumed(); i++)
          ((KeyListener) keyListeners.get(i)).keyPressed(e);
        break;
      case KeyEvent.KEY_RELEASED:
        ks = KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers(), true);
        if (keystrokes.containsKey(ks))
          ((ActionList) keystrokes.get(ks)).dispatchEvent(e);
        for (int i = 0; i < keyListeners.size() && !e.isConsumed(); i++)
          ((KeyListener) keyListeners.get(i)).keyReleased(e);
        break;
      case KeyEvent.KEY_TYPED:
        ks = KeyStroke.getKeyStroke(e.getKeyChar());
        if (keystrokes.containsKey(ks))
          ((ActionList) keystrokes.get(ks)).dispatchEvent(e);
        for (int i = 0; i < keyListeners.size() && !e.isConsumed(); i++)
          ((KeyListener) keyListeners.get(i)).keyTyped(e);
        break;
    }
  }
}

class ActionList {
  ArrayList acts = new ArrayList();
  
  public static ActionList addKeyAction(ActionList al, KeyAction ka) {
    if (al == null) al = new ActionList();
    if (ka instanceof KeyReleaseConsumerBlocker)
      for (Iterator i = al.acts.iterator(); i.hasNext(); )
        if (i.next() instanceof KeyReleaseConsumerBlocker)
          i.remove();
    al.acts.add(ka);
    return al;
  }
  
  public boolean removeKeyAction(KeyAction ka) {
    acts.remove(ka);
    return (acts.size() == 0);
  }
  
  public void dispatchEvent(KeyEvent e) {
    for (int i = 0; i < acts.size() && !e.isConsumed(); i++) {
      KeyAction ka = (KeyAction) acts.get(i);
      if (ka.actionPerformed()) e.consume();
    }
  }
}

class KeyReleaseConsumer implements KeyAction {
  KeyStroke release;
  KeyStroke orig;
  KeyReleaseConsumerBlocker blocker;
  
  public static void ignoreReleased(KeyStroke orig) {
    KeyReleaseConsumer krc = new KeyReleaseConsumer(orig);
    krc.blocker = new KeyReleaseConsumerBlocker();
    AWTKeyboard.unregisterKeyStroke(krc.release);
    AWTKeyboard.registerKeyStroke(krc.release, krc);
    AWTKeyboard.registerKeyStroke(orig, krc.blocker);
  }
  public KeyReleaseConsumer(KeyStroke orig) {
    this.orig = orig;
    release = KeyStroke.getKeyStroke(orig.getKeyCode(), orig.getModifiers(), true);
  }
  public boolean actionPerformed() {
    AWTKeyboard.unregisterKeyStroke(release);
    if (blocker != null) 
      AWTKeyboard.unregisterKeyStroke(orig, blocker);
    return blocker == null || !blocker.pressedAgain();
  }
}

class KeyReleaseConsumerBlocker implements KeyAction {
  boolean anotherKeypress;
  public boolean pressedAgain() {
    return anotherKeypress;
  }
  public void clear() {
    anotherKeypress = false;
  }
  public boolean actionPerformed() {
    anotherKeypress = true;
    return false;
  }
}
