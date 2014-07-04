/****license*****************************************************************
**   file: OKpanel.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextArea;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.swing.SharedFlag;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Title:        Utilitys
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */

public abstract class OKpanel extends javax.swing.JPanel {

//  ResourceBundle res = ResourceBundle.getBundle("hr.restart.util.Res_");
//  hr.restart.util.raCommonClass myCC=hr.restart.util.raCommonClass.getraCommonClass();
  /**
   * Optimizacija za taouchscreen: za toliko uvecava visinu gumba u JFrmTPV je stavljeno 27
   */
  public static int TOUCHHEIGHT = 0;
  BorderLayout borderLayout1 = new BorderLayout();
  GridLayout gridLayout2 = new GridLayout();
  static boolean okTrav = false;
  public JraButton jBOK = new JraButton() {
    public boolean isFocusTraversable() {
      return okTrav;
    }
  };
  public JraButton jPrekid = new JraButton();
  public JraButton jReset = new JraButton();
  
  JPanel jPanel7 = new JPanel();
  boolean enterEnabled = false;
  KeyEvent lastKeyEvent = null;
  /*private KeyListener OKPanelKeyListener = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      lastKeyEvent = e;
      if (e.isConsumed()) return;
      if (e.getKeyCode() == e.VK_F10||(e.getKeyCode() == e.VK_ENTER && enterEnabled)) {
        if (!jBOK.isShowing() || !jBOK.isEnabled()) return;
//        jBOK.requestFocus();
        e.consume();
        jBOK_actionPerformed();
      } else if (e.getKeyChar() == e.VK_ESCAPE) {
        if (!jPrekid.isShowing() || !jPrekid.isEnabled()) return;
        e.consume();
        jPrekid_actionPerformed();
      }
    }
  };*/
  
  SharedFlag ticket = new SharedFlag();
  
  ActionExecutor execOK = new ActionExecutor(ticket) {
	  public void run() {
	    if (JraTextArea.currentFocus != null)
          JraTextArea.currentFocus.posText();
		jBOK_actionPerformed();
	  }
  };
  
  ActionExecutor execPrekid = new ActionExecutor(ticket) {
	  public void run() {
		  jPrekid_actionPerformed();
	  }
  };
  
  static {
    okTrav = frmParam.getParam("sisfun", "OKfocus", "N",
        "Dopustiti fokusiranje OK dugmeta na OK-panelu (D,N)", true).equalsIgnoreCase("D");
  }

  public OKpanel() {

    try {

      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setEnterEnabled(boolean enabled) {
    enterEnabled = enabled;
  }
  public boolean isEnterEnabled() {
    return enterEnabled;
  }
  public KeyEvent getLastKeyEvent() {
    return lastKeyEvent;
  }
  
  public static boolean isOkTraversable() {
    return okTrav;
  }
  
  public SharedFlag getTicket() {
    return ticket;
  }
  
  void jbInit() throws Exception {
//System.err.println("OKpanel.TOUCHHEIGHT = "+OKpanel.TOUCHHEIGHT);
    jPanel7.setMinimumSize(new Dimension(70, 27+TOUCHHEIGHT));
    jPanel7.setPreferredSize(new Dimension(200 + startFrame.getFontDelta()*4, 10+TOUCHHEIGHT));
    jPanel7.setLayout(gridLayout2);
    jPrekid.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    	  execPrekid.invoke();
      }
    });
    jPrekid.setText("Prekid");
    jPrekid.setMaximumSize(new Dimension(100, 27+TOUCHHEIGHT));
    jPrekid.setMinimumSize(new Dimension(90, 27+TOUCHHEIGHT));
    jPrekid.setPreferredSize(new Dimension(90, 27+TOUCHHEIGHT));
    jPrekid.setIcon(raImages.getImageIcon(raImages.IMGCANCEL));
    jBOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (JraTextField.currentFocus != null)
          JraTextField.currentFocus.focusLost(null);
    	execOK.invoke();
      }
    });
    
//    jBOK.setSelected(true);
    jBOK.setText("OK");
    jBOK.setMaximumSize(new Dimension(100, 27+TOUCHHEIGHT));
    jBOK.setMinimumSize(new Dimension(90, 27+TOUCHHEIGHT));
    jBOK.setPreferredSize(new Dimension(90, 27+TOUCHHEIGHT));
    jBOK.setIcon(raImages.getImageIcon(raImages.IMGOK));

    // ab.f pogledaj objasnjenje u JraButton-u
    jBOK.setAutomaticFocusLost(false);
    jPrekid.setAutomaticFocusLost(false);

    this.setLayout(borderLayout1);
    this.setPreferredSize(new Dimension(200 + startFrame.getFontDelta()*4, 25+TOUCHHEIGHT));
    this.add(jPanel7, BorderLayout.EAST);
    if (TOUCHHEIGHT >0) add(Box.createVerticalStrut(27 + TOUCHHEIGHT));
    jPanel7.add(jBOK, null);
    jPanel7.add(jPrekid, null);
    
    /*InputMap keys = getInputMap(WHEN_IN_FOCUSED_WINDOW);
    keys.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "OKpanel-OK");
    keys.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "OKpanel-ENTER");
    keys.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "OKpanel-Prekid");
    ActionMap acts = getActionMap();
    acts.put("OKpanel-OK", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (jBOK.isShowing() && jBOK.isEnabled())
          jBOK_actionPerformed();
      }
    });
    acts.put("OKpanel-ENTER", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (enterEnabled && jBOK.isShowing() && jBOK.isEnabled()) {
          jBOK_actionPerformed();
        }
      }
    });
    acts.put("OKpanel-Prekid", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (jPrekid.isShowing() && jPrekid.isEnabled())
          jPrekid_actionPerformed();
      }
    });*/
  }
  
  public void addResetButton(final ResetEnabled screen) {
    jReset.setToolTipText("Pretpostavljene vrijednosti");
    jReset.setIcon(raImages.getImageIcon(raImages.IMGREFRESH));
    jReset.setMaximumSize(new Dimension(27+TOUCHHEIGHT, 27+TOUCHHEIGHT));
    jReset.setMinimumSize(new Dimension(27+TOUCHHEIGHT, 27+TOUCHHEIGHT));
    jReset.setPreferredSize(new Dimension(27+TOUCHHEIGHT, 27+TOUCHHEIGHT));
    JPanel left = new JPanel();
    left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
    remove(jPanel7);
    left.add(jReset);
    left.add(Box.createHorizontalStrut(5));
    left.add(jPanel7);
    add(left, BorderLayout.EAST);
    jReset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        screen.resetDefaults();
      }
    });
  }
 /**
 * Vraca izgled OK gumba na inicijalni (slika i text)
 */
  public void restore_jBOK() {
    jBOK.setText("OK");
    jBOK.setIcon(raImages.getImageIcon(raImages.IMGOK));
  }
/**
 * Mijenja text i sliku na jBOK gumbu, slika se zadaje string parametrom iz hr.restart.util.raImages
 * npr: okpanel.change_jBOK("Ispis",raImages.IMGPRINT)
 */
  public void change_jBOK(String text, String imgdesc) {
    jBOK.setText(text);
    jBOK.setIcon(raImages.getImageIcon(imgdesc));
  }
/**
 * registrira tipke F10 i Escape na zadanoj komponenti, tako da je F10 kao da si stisnuo OK, a Escape kao da si stisnuo prekid
 */
  KeyAction actF10 = null;
  KeyAction actESC = null;
  KeyAction actENTER = null;
  
  public void registerOKPanelKeys(raFrame rfr) {
    registerOKPanelKeys(rfr.getWindow());
  }
  
  public void registerOKEnter(raFrame rfr) {
    registerOKEnter(rfr.getWindow());
  }
  
  public void unregisterOKPanelKeys(raFrame rfr) {
    unregisterOKPanelKeys(rfr.getWindow());
  }
  
  public void unregisterOKEnter(raFrame rfr) {
    unregisterOKEnter(rfr.getWindow());
  }
  
  public void registerOKPanelKeys(Component comp) {
//    comp.addKeyListener(OKPanelKeyListener);
    AWTKeyboard.registerKeyStroke(comp, AWTKeyboard.F10, actF10 = new KeyAction() {
      public boolean actionPerformed() {
        if (!jBOK.isShowing() || !jBOK.isEnabled()) return false;
        execOK.invokeLater();
        return true;
      }
    });
    AWTKeyboard.registerKeyStroke(comp, AWTKeyboard.ENTER, actENTER = new KeyAction() {
      public boolean actionPerformed() {
        if (!jBOK.isShowing() || !jBOK.isEnabled() || !enterEnabled) return false;
        AWTKeyboard.ignoreKeyRelease(AWTKeyboard.ENTER);
        execOK.invokeLater();
        return true;
      }
    });
    AWTKeyboard.registerKeyStroke(comp, AWTKeyboard.ESC, actESC = new KeyAction() {
      public boolean actionPerformed() {
        if (!jPrekid.isShowing() || !jPrekid.isEnabled()) return false;
        execPrekid.invokeLater();	
		return true;
      }
    });
    registerOKEnter(comp);
  }
  
  public void registerOKEnter(Component comp) {
    if (okTrav)
      AWTKeyboard.registerKeyStroke(jBOK, AWTKeyboard.ENTER, new KeyAction() {
        public boolean actionPerformed() {
          if (!jBOK.isShowing() || !jBOK.isEnabled() || enterEnabled) return false;
          AWTKeyboard.ignoreKeyRelease(AWTKeyboard.ENTER);
          execOK.invokeLater();
          return true;
        }
      });
  }
/**
 * makne keylistener (onaj sa F10 i escape)
 */
   public void unregisterOKPanelKeys(Component comp) {
     AWTKeyboard.unregisterKeyStroke(comp, AWTKeyboard.F10, actF10);
     AWTKeyboard.unregisterKeyStroke(comp, AWTKeyboard.ENTER, actENTER); 
     AWTKeyboard.unregisterKeyStroke(comp, AWTKeyboard.ESC, actESC);
//    comp.removeKeyListener(OKPanelKeyListener);
     unregisterOKEnter(comp);
  }
   
   public void unregisterOKEnter(Component comp) {
     AWTKeyboard.unregisterKeyStroke(jBOK, AWTKeyboard.ENTER);     
   }
      
  public abstract void jBOK_actionPerformed();

  public abstract void jPrekid_actionPerformed();
}
