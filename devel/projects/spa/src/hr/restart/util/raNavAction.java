/****license*****************************************************************
**   file: raNavAction.java
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
package hr.restart.util;

import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.KeyAction;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public abstract class raNavAction extends JLabel implements Action {
//sysoutTEST ST = new sysoutTEST(false);
  private Color orgBC;
  private Color hiBC;
  public static final int ACTSIZE = 27;
  private String raIconDesc;
  private String identifier;
  private int keyNum;
  private int raNavActionModifiers=0;
  private boolean textual = false;
  private boolean lockEnabled = false;
  private Dimension pSize = new Dimension(ACTSIZE,ACTSIZE);
  private javax.swing.border.Border raisedBorder = BorderFactory.createBevelBorder(
      javax.swing.border.BevelBorder.RAISED,
      this.getBackground().brighter(),
      this.getBackground().darker()
  );
//  private javax.swing.border.Border raisedBorder = BorderFactory.createLineBorder(this.getBackground().brighter().brighter().brighter());
  private javax.swing.border.Border loweredBorder = BorderFactory.createLoweredBevelBorder();
    
/*  private KeyAction AWTact = null;
  private Component AWTcomp = null;*/
  
  private KeyListener raNavActionKeyListener = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
//      if (e.isConsumed()) return;
//      if (!isShowing()) return;
//      if (!isEnabled()) return;
//      if (e.getKeyCode() == keyNum&&e.getModifiers()==raNavActionModifiers) {
//        if (!isLockedAction()) {
//          actionPerformed(null);
//          e.consume();
//        }
//      }
      if (isActionPerformed(e)) {
        e.consume();
        executor.invoke();
      }
    }
  };
  private boolean lockedRaisedBorder=false;
  private boolean lockedLoweredBorder=false;
  private boolean lockedNoBorder=false;
  private boolean lockedAction=false;

  public raNavAction(String identifierC,String raicdesc,int keyNumC, int modifiersC, boolean textualC) {
    textual = textualC;
    identifier = identifierC;
    raIconDesc = raicdesc;
    keyNum = keyNumC;
    raNavActionModifiers = modifiersC;
    initAction();
  }

  public raNavAction(String identifierC,String raicdesc,int keyNumC, boolean textualC) {
    this(identifierC,raicdesc,keyNumC,0,textualC);
  }
  public raNavAction(String identifierC,String raicdesc,int keyNumC, int modifiersC) {
    this(identifierC,raicdesc,keyNumC,modifiersC,false);
  }
  public raNavAction(String identifierC,String raicdesc,int keyNumC) {
    this(identifierC,raicdesc,keyNumC,0,false);
  }
  public raNavAction(String identifierC) {
    this(identifierC,raImages.IMGSTAV,KeyEvent.VK_UNDEFINED,0,false);
  }

  private void initAction() {
    setIcon(raImages.getImageIcon(raIconDesc));
    setTexts();
    orgBC = getBackground();
    hiBC = Color.cyan;
    this.setHorizontalAlignment(CENTER);
    setSize(pSize);
    setPreferredSize(pSize);
    setMinimumSize(pSize);
    addMouseListener(new raAction_MouseListener());
  }

  public void registerNavKey(Component comp) {
    //comp.addKeyListener(raNavActionKeyListener);
    registerKeyStroke(comp);
  }

  public void unregisterNavKey(Component comp) {
    //comp.removeKeyListener(raNavActionKeyListener);
    unregisterKeyStroke(comp);
  }

  public void setTexts() {
    if (textual) setText(identifier);
    if (keyNum!=KeyEvent.VK_UNDEFINED)
      setToolTipText(identifier+" "+getModifierText()+KeyEvent.getKeyText(keyNum));
    else
      setToolTipText(identifier);
  }
  private String getModifierText() {
    if (raNavActionModifiers==0) return "";
    return KeyEvent.getKeyModifiersText(raNavActionModifiers)+"+";
  }
  public int getKeyNum() {
    return keyNum;
  }
  public KeyStroke getKeyStroke() {
    if (keyNum == KeyEvent.VK_UNDEFINED) return null;
    return KeyStroke.getKeyStroke(getKeyNum(), raNavActionModifiers);    
  }
  
  public void registerKeyStroke(Component parent) {
    if (getKeyStroke() != null) {
      unregisterKeyStroke(parent);
      AWTKeyboard.registerKeyStroke(parent, getKeyStroke(), new KeyAction() {
        public boolean actionPerformed() {
          if (!isShowing() || !isEnabled() || isLockedAction()) return false;
          executor.invokeLater();
          return true;
        }
      });
    }
  }
  
  ActionExecutor executor = new ActionExecutor() {
	  public void run() {
		  actionPerformed(null);  
	  }
  };
  
  public void unregisterKeyStroke(Component parent) {
    AWTKeyboard.unregisterKeyStroke(parent, getKeyStroke());
  }
  
  public void unregisterKeyStroke() {
    Component c = this;
    while (c != null) {
      unregisterKeyStroke(c);
      if (c instanceof Window) break;
      c = c.getParent();
    }
  }
  
  public boolean equals(Object obj) {
if (obj == null) {
//  System.err.println("raNavAction.equals::: obj is null");
  return false;
}
    if (obj instanceof hr.restart.util.raNavAction) {
if (((raNavAction)obj).getIdentifier() == null) {
//  System.err.println("raNavAction.equals::: identifier is null");
  return false;
}
      return (((raNavAction)obj).getIdentifier().equals(identifier));
    }
      
    return super.equals(obj);
  }
  public String getIdentifier() {
    return identifier;
  }
  void this_mouseClicked(MouseEvent e) {
// Prebacio u mousePressed zbog neuroznih 'korisnika' kojima drhte ruke
//    if (isEnabled()) actionPerformed(null);
  }

  void this_mouseEntered(MouseEvent e) {
    if (isEnabled()) setNavBorder(raisedBorder);
  }

  void this_mouseExited(MouseEvent e) {
    if (isEnabled()) setNavBorder(null);
  }

  void this_mousePressed(MouseEvent e) {
    if (isEnabled()) {
//      setNavBorder(loweredBorder);
      setNavBorder(null);
      if (isActionPerformed(e)) executor.invoke();
    }
  }

  void this_mouseReleased(MouseEvent e) {
    if (isEnabled()) if (getBorder() == loweredBorder)  setNavBorder(raisedBorder);
  }

  public void setNavBorder(javax.swing.border.Border bord) {
    if (!isBoderLocked()) {
      setVisible(false);
/*
      if (bord != null) {
        setBackground(hiBC);
      } else {
        setBackground(orgBC);
      }
*/
      setBorder(bord);
      setVisible(true);
      repaint();
    }
  }

  public Object getValue(String prp) {
    if (prp.equals("identifier")) return identifier;
    if (prp.equals("raIconDesc")) return raIconDesc;
    if (prp.equals("keyNum")) return new Integer(keyNum);
    if (prp.equals("pSize")) return pSize;
    return null;
  }
  public String getRaIconDesc() {
    return raIconDesc;
  }
  public void putValue(String prp,Object putprp) {
  }
  /**
   * ako je true onda bez obzira na sve border je uvijek raised
   */
  public void setLockedRaisedBorder(boolean newLockedRaisedBorder) {
    lockedRaisedBorder = newLockedRaisedBorder;
    if (lockedRaisedBorder)
      setBorder(raisedBorder);
    else
      setBorder(null);
  }
  public boolean isLockedRaisedBorder() {
    return lockedRaisedBorder;
  }
  /**
   * ako je true onda bez obzira na sve border je uvijek lowered
   */
  public void setLockedLoweredBorder(boolean newLockedLoweredBorder) {
    lockedLoweredBorder = newLockedLoweredBorder;
    if (lockedLoweredBorder)
      setBorder(loweredBorder);
    else
      setBorder(null);
  }
  public boolean isLockedLoweredBorder() {
    return lockedLoweredBorder;
  }
  /**
   * ako je true onda bez obzira na sve border je uvilek null
   */
  public void setLockedNoBorder(boolean newLockedNoBorder) {
    lockedNoBorder = newLockedNoBorder;
    setBorder(null);
  }
  public boolean isLockedNoBorder() {
    return lockedNoBorder;
  }
  public boolean isBoderLocked() {
    return (lockedRaisedBorder||lockedLoweredBorder||lockedNoBorder);
  }
  /**
   * ako je true ta akcija se nikad nece izvoditi
   */
  public void setLockedAction(boolean newLockedAction) {
    lockedAction = newLockedAction;
  }
  public boolean isLockedAction() {
    return lockedAction;
  }
  public void setLockEnabled(boolean enab) {
    lockEnabled = enab;
  }
//  public void paint(Graphics g) {
//    super.paint(g);
//    hr.restart.plaf.raPainter.paintGradient(g,this);
//  }
  public void setEnabled(boolean isEnabled) {
    if (!lockEnabled) {
      setBorder(null);
      super.setEnabled(isEnabled);
    }
  }
  
  public boolean isEnabled() {
    return super.isEnabled() && !isLockedAction();
  }
  
  public boolean isActionPerformed(InputEvent e) {
    if (!isShowing()) return false;
    if (!isEnabled()) return false;
    if (isLockedAction()) return false;
    if (e == null) return true;
    if (e.isConsumed()) return false;
    if (e instanceof KeyEvent) {
      KeyEvent ke = (KeyEvent)e;
      if (!(ke.getKeyCode() == keyNum && ke.getModifiers()==raNavActionModifiers)) return false;
    }
    return true;
  }
  public String toString() {
    return
      "hr.restart.util.raNavAction[identifier = "+identifier+
      "; raIconDesc = "+raIconDesc+
      "; key = "+KeyEvent.getKeyModifiersText(raNavActionModifiers)+"+"+KeyEvent.getKeyText(keyNum)
      +"]";
  }
  class raAction_MouseListener extends java.awt.event.MouseAdapter {
      public void mouseClicked(MouseEvent e) {
        this_mouseClicked(e);
      }
      public void mouseEntered(MouseEvent e) {
        this_mouseEntered(e);
      }
      public void mouseExited(MouseEvent e) {
        this_mouseExited(e);
      }
      public void mousePressed(MouseEvent e) {
        this_mousePressed(e);
      }
      public void mouseReleased(MouseEvent e) {
        this_mouseReleased(e);
      }
  }
}