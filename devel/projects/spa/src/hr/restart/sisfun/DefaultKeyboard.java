/****license*****************************************************************
**   file: DefaultKeyboard.java
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

import hr.restart.util.Aus;
import hr.restart.util.raProcess;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class DefaultKeyboard implements Keyboard{
  private static String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@"+
                                "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`"+
                                "abcdefghijklmnopqrstuvwxyz{|}~¦"+
                                "\u010C\u0106\u0110ŠŽ\u010D\u0107\u0111šž";
  private static int[] codes = new int[chars.length()];
  private static int[] mods = new int[chars.length()];

  private static DefaultKeyboard inst = new DefaultKeyboard();

  private DefaultKeyboard() {
    for (int i = 0; i < codes.length; i++)
      codes[i] = KeyEvent.VK_UNDEFINED;
  }

  public static DefaultKeyboard getInstance() {
    return inst;
  }

  public void autoAdapt() {
    GlobalKeyListener.install();
    raProcess.runChild(null, "Keyboard test", "Operation in progress...", new Runnable() {
      public void run() {
        System.err.println("auto-adapt ...");
        autoAdapt_impl();
        System.err.println("auto-adapt finished.");
      }
    });
    GlobalKeyListener.deinstall();
  }

  private void autoAdapt_impl() {
    try {
      Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      Borg.flush();
    } catch (Exception e) {}
    Borg.setAutoDelay(0);
    testKeys(0);
    testKeys(KeyEvent.SHIFT_MASK);
    try {
      Borg.press(KeyEvent.VK_ALT_GRAPH);
      Borg.release(KeyEvent.VK_ALT_GRAPH);
      testKeys(KeyEvent.ALT_GRAPH_MASK);
    } catch (Exception e) {
      Aus.addTrace("no ALT_GRAPH key");
      testKeys(KeyEvent.CTRL_MASK | KeyEvent.ALT_MASK);
    }
  }

  private void testKeys(int keyMod) {
    int[] otherTestCodes = {KeyEvent.VK_SPACE, KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD,
      KeyEvent.VK_SLASH, KeyEvent.VK_SEMICOLON, KeyEvent.VK_EQUALS, KeyEvent.VK_OPEN_BRACKET,
      KeyEvent.VK_BACK_SLASH, KeyEvent.VK_CLOSE_BRACKET, KeyEvent.VK_BACK_QUOTE,
      KeyEvent.VK_QUOTE, KeyEvent.VK_AMPERSAND, KeyEvent.VK_ASTERISK, KeyEvent.VK_QUOTEDBL,
      KeyEvent.VK_LESS, KeyEvent.VK_GREATER, KeyEvent.VK_BRACELEFT, KeyEvent.VK_BRACERIGHT,
      KeyEvent.VK_AT, KeyEvent.VK_COLON, KeyEvent.VK_CIRCUMFLEX, KeyEvent.VK_DOLLAR,
      KeyEvent.VK_EXCLAMATION_MARK, KeyEvent.VK_LEFT_PARENTHESIS, KeyEvent.VK_NUMBER_SIGN,
      KeyEvent.VK_MINUS, KeyEvent.VK_PLUS, KeyEvent.VK_RIGHT_PARENTHESIS, KeyEvent.VK_UNDERSCORE};

    for (int i = KeyEvent.VK_0; i <= KeyEvent.VK_9; i++) testKey(i, keyMod);
    for (int i = KeyEvent.VK_A; i <= KeyEvent.VK_Z; i++) testKey(i, keyMod);
    for (int i = 0; i < otherTestCodes.length; i++) testKey(otherTestCodes[i], keyMod);
  }

  private void testKey(int keyCode, int keyMod) {
    boolean ctrl, alt, shift, altgr;
    ctrl = alt = shift = altgr = false;
    try {
      if (ctrl = ((keyMod & KeyEvent.CTRL_MASK) > 0)) Borg.press(KeyEvent.VK_CONTROL);
      if (alt = ((keyMod & KeyEvent.ALT_MASK) > 0)) Borg.press(KeyEvent.VK_ALT);
      if (shift = ((keyMod & KeyEvent.SHIFT_MASK) > 0)) Borg.press(KeyEvent.VK_SHIFT);
      if (altgr = ((keyMod & KeyEvent.ALT_GRAPH_MASK) > 0)) Borg.press(KeyEvent.VK_ALT_GRAPH);
      Borg.press(keyCode);
      Borg.release(keyCode);
      Borg.flush();
      char ch = GlobalKeyListener.lastKeyTyped();
      if (ch != KeyEvent.CHAR_UNDEFINED) {
//        System.err.println("got char: "+ch);
        int pos = chars.indexOf(ch);
        if (pos >= 0 && codes[pos] == KeyEvent.VK_UNDEFINED) {
          codes[pos] = keyCode;
          mods[pos] = keyMod;
//          System.err.println("inserted.");
        }
      }
    } catch (Exception e) {
//      System.err.println("error: " + (keyMod == 0 ? "" :
//                         KeyEvent.getKeyModifiersText(keyMod) + " + ") +
//                         KeyEvent.getKeyText(keyCode));
    } finally {
      try {
        if (altgr) Borg.release(KeyEvent.VK_ALT_GRAPH);
        if (shift) Borg.release(KeyEvent.VK_SHIFT);
        if (alt) Borg.release(KeyEvent.VK_ALT);
        if (ctrl) Borg.release(KeyEvent.VK_CONTROL);
      } catch (Exception e) {}
    }
  }

  public int getKeyCode(char ch) {
    int pos = chars.indexOf(ch);
    if (pos >= 0) return codes[pos];
    else return KeyEvent.VK_UNDEFINED;
  }

  public int getKeyModifiers(char ch) {
    int pos = chars.indexOf(ch);
    if (pos >= 0) return mods[pos];
    else return 0;
  }

  public void dump() {
    char ch;
    for (int i = 0; i < chars.length(); i++) {
      ch = chars.charAt(i);
      System.out.println(ch + " = " + (getKeyModifiers(ch) == 0 ? "" :
        KeyEvent.getKeyModifiersText(getKeyModifiers(ch)) + " + ") + KeyEvent.getKeyText(getKeyCode(ch)));
    }
  }

  private static void main(String[] args) {
    Borg.installed();
    Borg.install();
    DefaultKeyboard.getInstance().dump();
  }
}

class GlobalKeyListener {
  private static GlobalKeyListener inst = null;

  private char lastKey = KeyEvent.CHAR_UNDEFINED;
  private AWTEventListener kl;

  public static void install() {
    if (inst == null) inst = new GlobalKeyListener();
  }

  public static void deinstall() {
    if (inst != null) {
      Toolkit.getDefaultToolkit().removeAWTEventListener(inst.kl);
      inst = null;
    }
  }

  private synchronized char getLastKey() {
    char ret = lastKey;
    lastKey = KeyEvent.CHAR_UNDEFINED;
    return ret;
  }

  private synchronized void setLastKey(char ch) {
    lastKey = ch;
  }

  private GlobalKeyListener() {
    Toolkit.getDefaultToolkit().addAWTEventListener(kl = new AWTEventListener() {
      public void eventDispatched(AWTEvent e) {
        if (e instanceof KeyEvent) {
          if (((KeyEvent) e).getID() == KeyEvent.KEY_TYPED)
            setLastKey(((KeyEvent) e).getKeyChar());
        }
      }
    }, AWTEvent.KEY_EVENT_MASK);
  }

  public static char lastKeyTyped() {
    return inst.getLastKey();
  }
}
