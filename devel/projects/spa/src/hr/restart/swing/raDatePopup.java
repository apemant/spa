/****license*****************************************************************
**   file: raDatePopup.java
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

import hr.restart.util.Util;
import hr.restart.util.Valid;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

public class raDatePopup extends JPopupMenu {
  JraTextField jra = null;
  JraTextField jra2 = null;
  boolean first;
  MouseListener popup;

  JMenuItem jmiSelectRange = new JMenuItem();
  JMenuItem jmiSelectDate = new JMenuItem();
  JMenuItem jmiToday = new JMenuItem();
  JMenuItem jmiFirstInMonth = new JMenuItem();
  JMenuItem jmiLastInMonth = new JMenuItem();
  JMenuItem jmiFirstInYear = new JMenuItem();
  JMenuItem jmiLastInYear = new JMenuItem();

  static raDatePopup currInst = null;
  
  static {
    AWTKeyboard.registerKeyStroke(AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        if (currInst == null || !currInst.isVisible()) return false;
        currInst.setVisible(false);
        return true;
      }
    });
  }

  private raDatePopup() {
    // onemogucen konstruktor bez parametara
  }

  public static void hideInstance() {
    if (currInst != null && currInst.isVisible())
      currInst.setVisible(false);
  }

  public raDatePopup(JraTextField owner) {
    try {
//      System.out.println("adding "+owner.getColumnName());
      jra = owner;
      jra2 = null;
//      System.out.println("adding solo "+jra + "  "+jra.getColumnName());
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public raDatePopup(JraTextField owner, JraTextField other, boolean first) {
    try {
      jra = owner;
      jra2 = other;
//      System.out.println("adding "+owner.getColumnName()+" with "+other.getColumnName());
      this.first = first;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public JraTextField getOwner() {
    return jra;
  }

  public void addToRange(JraTextField other, boolean first) {
    if (jra2 == null) {
      add(jmiSelectRange, 0);
      jmiSelectRange.setAction(selectRange);
      jra.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, MouseEvent.CTRL_MASK), "RANGE");
      jra.getActionMap().put("RANGE", selectRange);
//      System.out.println("adding "+other.getColumnName()+" to "+jra.getColumnName());
    }// else System.out.println(other.getColumnName() + "  already added");
    jra2 = other;
    this.first = first;
  }

  private void jbInit() throws Exception {
/*    ActionListener a;
    dc.setText("Odabir iz popisa    ");
    jmiFirstInMonth.setText("Prvi u mjesecu");
    jmiLastInMonth.setText("Zadnji u mjesecu");
    jmiFirstInYear.setText("Prvi u godini");
    jmiLastInYear.setText("Zadnji u godini"); */
    if (jra2 != null) add(jmiSelectRange);
    add(jmiSelectDate);
    add(jmiToday);
    add(jmiFirstInMonth);
    add(jmiLastInMonth);
    add(jmiFirstInYear);
    add(jmiLastInYear);
/*    dc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, ActionEvent.ALT_MASK));
    jmiFirstInMonth.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.SHIFT_MASK));
    jmiLastInMonth.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
    jmiFirstInYear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.SHIFT_MASK));
    jmiLastInYear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK)); */
/*    dc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chooseDate();
      }
    });
    jmiFirstInMonth.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        firstInMonth();
      }
    });
    jmiLastInMonth.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lastInMonth();
      }
    });
    jmiFirstInYear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        firstInYear();
      }
    });
    jmiLastInYear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lastInYear();
      }
    }); */
    if (jra2 != null) jmiSelectRange.setAction(selectRange);
    jmiSelectDate.setAction(chooseDate);
    jmiToday.setAction(today);
    jmiFirstInMonth.setAction(firstInMonth);
    jmiLastInMonth.setAction(lastInMonth);
    jmiFirstInYear.setAction(firstInYear);
    jmiLastInYear.setAction(lastInYear);
    if (jra != null) setOwnerListener();
  }

  public void removeAll() {
    removeTextFieldListeners();
    super.removeAll();
  }

  public void removeTextFieldListeners() {
    if (jra2 != null) {
      jra.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_A, MouseEvent.CTRL_MASK));
      jra.getActionMap().remove("RANGE");
    }
    if (jra != null) {
      jra.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_S, MouseEvent.CTRL_MASK));
      jra.getActionMap().remove("SEL");
      jra.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_Y, MouseEvent.CTRL_MASK));
      jra.getActionMap().remove("TD");
      jra.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_D, MouseEvent.CTRL_MASK));
      jra.getActionMap().remove("FM");
      jra.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_F, MouseEvent.CTRL_MASK));
      jra.getActionMap().remove("LM");
      jra.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_G, MouseEvent.CTRL_MASK));
      jra.getActionMap().remove("FY");
      jra.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_H, MouseEvent.CTRL_MASK));
      jra.getActionMap().remove("LY");
      jra.removeMouseListener(popup);
    }
  }

  private void setOwnerListener() {
    jra.setEnablePopupMenu(false);
    if (jra2 != null) {
      jra.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, MouseEvent.CTRL_MASK), "RANGE");
      jra.getActionMap().put("RANGE", selectRange);
    }
    jra.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, MouseEvent.CTRL_MASK), "SEL");
    jra.getActionMap().put("SEL", chooseDate);
    jra.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, MouseEvent.CTRL_MASK), "TD");
    jra.getActionMap().put("TD", today);
    jra.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, MouseEvent.CTRL_MASK), "FM");
    jra.getActionMap().put("FM", firstInMonth);
    jra.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, MouseEvent.CTRL_MASK), "LM");
    jra.getActionMap().put("LM", lastInMonth);
    jra.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_G, MouseEvent.CTRL_MASK), "FY");
    jra.getActionMap().put("FY", firstInYear);
    jra.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_H, MouseEvent.CTRL_MASK), "LY");
    jra.getActionMap().put("LY", lastInYear);
    jra.addMouseListener(popup = new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        checkPopup(e);
      }
      public void mouseReleased(MouseEvent e) {
        checkPopup(e);
      }
    });
  }

  private void checkPopup(MouseEvent e) {
//    System.out.println(jra);
//    System.out.println(jra.getText());
    if (e.isPopupTrigger() && jra.isEnabled())
      (currInst = this).show(e.getComponent(), e.getX(), e.getY());
  }

  private void setDate(JraTextField j, Timestamp t) {
    try {
      j.getDataSet().setTimestamp(j.getColumnName(), t);
    } catch (Exception e) {}
  }

  private Timestamp getDate(JraTextField j) {
    Timestamp t = null;
    if (j.Validacija() && !j.getText().equals("__-__-____") && !j.getText().equals("")) {
      try {
        t = j.getDataSet().getTimestamp(j.getColumnName());
//        System.out.println(t);
      } catch (Exception e) {}
    }
    return t;
  }

  private Action selectRange = new AbstractAction("Odabir perioda") {
    public void actionPerformed(ActionEvent e) {
      Timestamp t = getDate(jra);
      Timestamp t2 = getDate(jra2);
      if (first) {
        raDateChooser.getRange(jra, t, t2);
        if (raDateChooser.getFrom() != null && raDateChooser.getTo() != null) {
          setDate(jra, raDateChooser.getFrom());
          setDate(jra2, raDateChooser.getTo());
        }
      } else {
        raDateChooser.getRange(jra, t2, t);
        if (raDateChooser.getFrom() != null && raDateChooser.getTo() != null) {
          setDate(jra2, raDateChooser.getFrom());
          setDate(jra, raDateChooser.getTo());
        }
      }
    }
  };

  private Action chooseDate = new AbstractAction("Odabir iz popisa") {
    public void actionPerformed(ActionEvent e) {
      Timestamp t = getDate(jra);
      t = raDateChooser.getDate(jra, t);
      if (t != null) setDate(jra, t);
    }
  };

  private Action today = new AbstractAction("Današnji datum") {
    public void actionPerformed(ActionEvent e) {
      setDate(jra, Valid.getValid().getToday());
      jra.selectAll();
    }
  };

  private Action firstInMonth = new AbstractAction("Prvi u mjesecu") {
    public void actionPerformed(ActionEvent e) {
      setDate(jra, Util.getUtil().getFirstDayOfMonth());
      jra.selectAll();
    }
  };

  private Action lastInMonth = new AbstractAction("Zadnji u mjesecu") {
    public void actionPerformed(ActionEvent e) {
      setDate(jra, Util.getUtil().getLastDayOfMonth());
      jra.selectAll();
    }
  };

  private Action firstInYear = new AbstractAction("Prvi u godini") {
    public void actionPerformed(ActionEvent e) {
      setDate(jra, Util.getUtil().getFirstDayOfYear());
      jra.selectAll();
    }
  };

  private Action lastInYear = new AbstractAction("Zadnji u godini") {
    public void actionPerformed(ActionEvent e) {
      setDate(jra, Util.getUtil().getLastDayOfYear());
      jra.selectAll();
    }
  };
}
