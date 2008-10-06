/****license*****************************************************************
**   file: raDisabledPopup.java
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

import hr.restart.help.raLiteBrowser;
import hr.restart.util.Aus;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raDisabledPopup extends JPopupMenu {
//  JraTextField jra = null;

  JraTextField jra = null;
  JTextField tx = new JTextField();
  private static raDisabledPopup inst = new raDisabledPopup();
  
  static {
    AWTKeyboard.registerKeyStroke(AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        return hideInstance();
      }
    });
  }

  JMenuItem jmiCopy = new JMenuItem();
  JMenuItem jmiCut = new JMenuItem();
  JMenuItem jmiPaste = new JMenuItem();
  JMenuItem jmiSelectAll = new JMenuItem();
  JMenuItem jmiClear = new JMenuItem();
  JMenuItem jmiSearch = new JMenuItem();
  JMenuItem jmiSearchAll = new JMenuItem();
//  JCheckBoxMenuItem jmiCalc = new JCheckBoxMenuItem();

  public static void installFor(JraTextField jra) {
    jra.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        inst.checkPopup(e);
      }
      public void mousePressed(MouseEvent e) {
        inst.checkPopup(e);
      }
      public void mouseReleased(MouseEvent e) {
        inst.checkPopup(e);
      }
    });
  }

  public static boolean hideInstance() {
    boolean vis = inst.isVisible();
    inst.setVisible(false);
    return vis;
  }

  private raDisabledPopup() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    add(jmiCopy);
    add(jmiCut);
    add(jmiPaste);
    addSeparator();
    add(jmiClear);
    add(jmiSelectAll);
    addSeparator();
    add(jmiSearch);
    add(jmiSearchAll);
//    addSeparator();
//    add(jmiCalc);
    jmiCopy.setAction(copyAction);
    jmiCut.setAction(cutAction);
    jmiPaste.setAction(pasteAction);
    jmiClear.setAction(clearAction);
    jmiSelectAll.setAction(selectAllAction);
    jmiSearch.setAction(searchAction);
    jmiSearchAll.setAction(searchAllAction);
//    jmiCalc.setAction(calcAction);
/*    jra.addMouseListener(popup = new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        checkPopup(e);
      }
      public void mouseReleased(MouseEvent e) {
        checkPopup(e);
      }
    }); */
  }

/*  public void removeAll() {
    removeTextFieldListeners();
    super.removeAll();
  }

  public void removeTextFieldListeners() {
    if (jra != null)
      jra.removeMouseListener(popup);
  } */

  private void checkPopup(MouseEvent e) {
    if (e.isPopupTrigger() && !e.isConsumed() && e.getSource() instanceof JraTextField) {
      jra = (JraTextField) e.getSource();
      int l = jra.getDocument().getLength();
      int s = jra.getSelectionEnd() - jra.getSelectionStart();
      if (jra.isEnabled() || l > 0) {
        copyAction.setEnabled(l > 0 && !jra.isEnabled() || s > 0);
        cutAction.setEnabled(s > 0);
        pasteAction.setEnabled(jra.isEnabled());
        selectAllAction.setEnabled(jra.isEnabled() && l > 0);
        clearAction.setEnabled(jra.isEnabled() && l > 0);
        searchAction.setEnabled(l > 0);
        searchAllAction.setEnabled(l > 0);
//        calcAction.setEnabled(jra.getFieldMask() instanceof raCalculatorMask);
//        jmiCalc.setSelected(raCalculatorMask.isActive());
        this.show(e.getComponent(), e.getX(), e.getY());
      }
      e.consume();
    }
  }

  private Action copyAction = new AbstractAction("Kopiraj") {
    public void actionPerformed(ActionEvent e) {
      String text = null;
      if (jra.isEnabled()) {
        text = jra.getSelectedText();
        jra.copy();
      } else {
        tx.setText(text = jra.getText());
        tx.selectAll();
        tx.copy();
      }
      if (jra.getFieldMask() instanceof raNumberMask ||
         (jra.getFieldMask() == null && raNumberMask.isFormattedDecNumber(text)))
        raNumberMask.normalizeClipboardNumber();
    }
  };

  private Action cutAction = new AbstractAction("Izreži") {
    public void actionPerformed(ActionEvent e) {
      if (jra.getFieldMask() != null) {
        jra.getFieldMask().cacheDynamicVariables();
        jra.getFieldMask().keypressCut();
      } else jra.cut();
    }
  };

  private Action pasteAction = new AbstractAction("Umetni") {
    public void actionPerformed(ActionEvent e) {
      if (jra.getFieldMask() != null) {
        jra.getFieldMask().cacheDynamicVariables();
        jra.getFieldMask().keypressPaste();
      } else jra.paste();
    }
  };

  private Action selectAllAction = new AbstractAction("Oznaèi sve") {
    public void actionPerformed(ActionEvent e) {
      jra.selectAll();
    }
  };

  private Action clearAction = new AbstractAction("Obriši sve") {
    public void actionPerformed(ActionEvent e) {
      jra.selectAll();
      jra.replaceSelection("");
    }
  };
  
  private Action searchAction = new AbstractAction("Traži na Internetu") {
    public void actionPerformed(ActionEvent e) {
      searchInt(false);
    }
  };
  
  private Action searchAllAction = new AbstractAction("Traži tekst u cjelini") {
    public void actionPerformed(ActionEvent e) {
      searchInt(true);
    }
  };
  
  void searchInt(boolean phrase) {
    try {
      String s = jra.getText();
      if (jra.getSelectionEnd() > jra.getSelectionStart())
        s = s.substring(jra.getSelectionStart(), jra.getSelectionEnd());      
      if (phrase) s = "\"" + s + "\"";
      raLiteBrowser.openSystemBrowser(
          new URL("http://www.pogodak.hr/search.jsp?q="+
              Aus.convertToURLFriendly(s)));
    } catch (MalformedURLException e1) {
      e1.printStackTrace();
    }
  }

//  private Action calcAction = new AbstractAction("Kalkulator") {
//    public void actionPerformed(ActionEvent e) {
//      raCalculatorMask.setActive(jmiCalc.isSelected());
//    }
//  };
}
