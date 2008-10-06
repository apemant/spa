/****license*****************************************************************
**   file: raDateChooser.java
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

import hr.restart.robno.Aut;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

class DayLabel extends JLabel {
  public static final int BEFORE_FIRST = 0;
  public static final int AFTER_LAST = 100;
  private static Color faintText, normText, selText, normBack, selBack;
  static {
    JTextField dummy = new JTextField();
    setColors(dummy);
  }

  private boolean sel, pick, faint;
  private int day;

  public static void setColors(JTextField dummy) {
    normBack = dummy.getBackground();
    normText = dummy.getForeground();
    selBack = dummy.getSelectionColor();
    selText = dummy.getSelectedTextColor();
    faintText = dummy.getDisabledTextColor();
//    findFaint();
  }

//  private static void findFaint() {
//    int r = (normBack.getRed() * 2  + normText.getRed()) / 3;
//    int g = (normBack.getGreen() * 2 + normText.getGreen()) / 3;
//    int b = (normBack.getBlue() * 2 + normText.getBlue()) / 3;
//    faintText = new Color(r, g, b);
//  }

  public DayLabel(int day, boolean fainted) {
    this.day = day;
    if (day > BEFORE_FIRST && day < AFTER_LAST) this.setText(String.valueOf(day));
    this.setHorizontalAlignment(SwingConstants.CENTER);
    this.setOpaque(true);
    sel = pick = false;
    faint = fainted;
//    if (!fainted) textCol = normText;
//    else textCol = selText = faintText;
    setBack();
  }

  public void setSelected(boolean sel) {
    this.sel = sel && isDate();
    setBack();
  }

  public void setHighlight(boolean h) {
    pick = h && isDate();
    setBack();
  }


  public boolean before(DayLabel d) {
    if (getMonth() < d.getMonth() || (getMonth() == d.getMonth() && day < d.day)) return true;
    else return false;
  }

  public boolean after(DayLabel d) {
    if (getMonth() > d.getMonth() || (getMonth() == d.getMonth() && day > d.day)) return true;
    else return false;
  }

  public boolean equals(DayLabel d) {
    if (getMonth() == d.getMonth() && day == d.day) return true;
    else return false;
  }

  public boolean isDate() {
    return (day > BEFORE_FIRST && day < AFTER_LAST);
  }

  public int getDay() {
    return day;
  }

  public Rectangle getViewBounds(Rectangle r) {
    r = getBounds(r);
    r.translate(getPanel().getX() + getParent().getX(), getPanel().getY() + getParent().getY());
    return r;
  }

  public MonthPanel getPanel() {
    return (MonthPanel) getParent().getParent();
  }

  public int getMonth() {
    return getPanel().getMonth();
  }

  public int getLastDay() {
    return getPanel().getDays();
  }

  public int getYear() {
    return getPanel().getYear();
  }

  private void setBack() {
    if (!sel) {
      this.setBackground(normBack);
      this.setForeground(faint ? faintText : normText);
    } else {
      this.setBackground(selBack);
      this.setForeground(faint ? faintText : selText);
    }
    if (pick)
      this.setBorder(BorderFactory.createLineBorder(Color.red));
    else this.setBorder(BorderFactory.createLineBorder(this.getBackground()));
  }

  public void repaint() {}
}

class MonthPanel extends JPanel {
  public static final String[] monthName = new String[] {"Sije\u010Danj", "Velja\u010Da", "Ožujak", "Travanj",
      "Svibanj", "Lipanj", "Srpanj", "Kolovoz", "Rujan", "Listopad", "Studeni", "Prosinac"};
  public static final String[] dayName = new String[] {"Pon", "Uto", "Sri", "\u010Cet", "Pet", "Sub", "Ned"};

  private JPanel jpMain;
  private JLabel jlMonth;
  private JLabel[] jlDay;
  private DayLabel[] day;

  private int month, year, days;
//  private Rectangle m_bounds = null;

  public MonthPanel(int year, int month, int active) {
    this.month = month;
    this.year = year;

    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(year, month, 1);
    days = c.getActualMaximum(c.DAY_OF_MONTH);
    int weekDay = (c.get(c.DAY_OF_WEEK) - c.MONDAY + 7) % 7;

    jlMonth = new JLabel(monthName[month] + " " + year + ".");
    jlMonth.setHorizontalAlignment(SwingConstants.CENTER);

    jlDay = new JLabel[7];
    for (int i = 0; i < 7; i++) {
      jlDay[i] = new JLabel(" " + dayName[i] + " ");
      jlDay[i].setHorizontalAlignment(SwingConstants.CENTER);
    }

    day = new DayLabel[days];
    for (int i = 0; i < days; i++)
      day[i] = new DayLabel(i + 1, (i >= active) && (active >= 0));

    jpMain = new JPanel(new GridLayout(0, 7));
    for (int i = 0; i < 7; i++)
      jpMain.add(jlDay[i]);
    for (int i = 0; i < weekDay; i++)
      jpMain.add(new DayLabel(0, false));
    for (int i = 0; i < days; i++)
      jpMain.add(day[i]);
    for (int i = weekDay + days; i < 42; i++)
      jpMain.add(new DayLabel(100, false));

    jpMain.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createEmptyBorder(2, 0, 0, 0),
      BorderFactory.createLoweredBevelBorder()));

    this.setLayout(new BorderLayout());
    this.add(jlMonth, BorderLayout.NORTH);
    this.add(jpMain, BorderLayout.CENTER);
  }

  public void setSelectedDays(int first, int last, boolean yesno) {
    for (int i = first; i <= Math.min(last, days); i++)
      day[i - 1].setSelected(yesno);
  }

  public int getDays() {
    return days;
  }

  public DayLabel getDay(int d) {
    if (d < 1) d = 1;
    if (d > days) d = days;
    return day[d - 1];
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }
}

public class raDateChooser {

  private static raDateChooser dc = null;

  private JDialog main;
  private JraScrollPane v;
  private JPanel jp;
  private Rectangle br;
  private Timer scroller;
//  private JComboBox yr;
//  private JPanel yrp;

  private Point location;

  private MonthPanel[] month;
  private DayLabel highlight, previous, first, last, ref;
  private Timestamp result, from, to;

  private boolean range, scroll;
  private int columns, speed;


  public static void getRange() {
    getRange(null, null, null);
  }

  public static void getRange(Container owner) {
    getRange(owner, null, null);
  }

  public static void getRange(Timestamp firstDate, Timestamp lastDate) {
    getRange(null, firstDate, lastDate);
  }

  public static void getRange(Container owner, Timestamp firstDate, Timestamp lastDate) {
    if (dc == null) dc = new raDateChooser();

    Calendar c = Calendar.getInstance();
    if (firstDate != null && lastDate != null &&
        !Util.getUtil().getFirstSecondOfDay(firstDate).after(lastDate)) {
      c.setTime(new java.util.Date(firstDate.getTime()));
      Calendar ct = Calendar.getInstance();
      ct.setTime(new java.util.Date(lastDate.getTime()));
      if (c.get(c.YEAR) == ct.get(ct.YEAR)) {
        dc.showChooser(owner, true, c, ct);
        return;
      }
    }
    dc.showChooser(owner, true, c, null);
  }

  public static Timestamp getDate() {
    return getDate(null, null);
  }

  public static Timestamp getDate(Container owner) {
    return getDate(owner, null);
  }

  public static Timestamp getDate(Timestamp date) {
    return getDate(null, date);
  }

  public static Timestamp getDate(Container owner, Timestamp date) {
    if (dc == null) dc = new raDateChooser();

    Calendar c = Calendar.getInstance();
    if (date != null) c.setTime(new java.util.Date(date.getTime()));

    dc.showChooser(owner, false, c, null);
    return dc.result;
  }

  public static Timestamp getFrom() {
    return dc == null ? null : dc.from;
  }

  public static Timestamp getTo() {
    return dc == null ? null : dc.to;
  }


  private void showChooser(Container owner, boolean rng, Calendar high, Calendar end) {
    initDialog(owner);
    range = rng;
    scroll = false;
    previous = ref = null;
    result = from = to = null;

    if (high.get(high.YEAR) != month[0].getYear()) {
      setYear(high.get(high.YEAR));
      setPreferredPanelSize();
      main.pack();
    }
    if (rng && end != null)
      setSelection(month[high.get(high.MONTH)].getDay(high.get(high.DAY_OF_MONTH)),
                   month[end.get(end.MONTH)].getDay(end.get(end.DAY_OF_MONTH)));
    setHighlight(high.get(high.MONTH), high.get(high.DAY_OF_MONTH));
    if (!rng) main.setTitle("Odabir datuma");
    else main.setTitle("Odabir datumskog raspona");
    main.setVisible(true);
  }


  private raDateChooser() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void initDialog(Container owner) {
    Container parent = null;

    if (owner instanceof JComponent)
      parent = ((JComponent) owner).getTopLevelAncestor();
    else if (owner instanceof Window)
      parent = owner;

    if (parent instanceof Dialog)
      main = new JraDialog((Dialog) parent, true);
    else
      main = new JraDialog((Frame) parent, true);

    addDialogListeners();
    main.getContentPane().add(v);
    main.pack();
    if (location == null)
      location = new Point(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - main.getWidth() / 2,
                           Toolkit.getDefaultToolkit().getScreenSize().width / 4);
    main.setLocation(location);
  }

  private void setYear(int year) {
    Calendar c = Calendar.getInstance();

    if (year <= 0)
      year = c.get(c.YEAR);

    jp.removeAll();
    for (int i = 0; i < 12; i++) {
      int active = -1;
      if (i == c.get(c.MONTH) && year == c.get(c.YEAR)) active = c.get(c.DAY_OF_MONTH);
      else if (year > c.get(c.YEAR) || (year == c.get(c.YEAR) && i > c.get(c.MONTH))) active = 0;
      jp.add(month[i] = new MonthPanel(year, i, active));
    }
    highlight = previous = first = last = ref = null;
  }

  private void checkYear(Calendar c) {
    if (c.get(c.YEAR) != month[0].getYear()) {
      setYear(c.get(c.YEAR));
      setPreferredPanelSize();
      main.pack();
    }
  }

  private void jbInit() throws Exception {
    int dw = Aut.getAut().getNumber(frmParam.getParam("sisfun", "datumHoriz"));
    int dh = Aut.getAut().getNumber(frmParam.getParam("sisfun", "datumVert"));
    br = new Rectangle();
    jp = new JPanel();
    month = new MonthPanel[12];
    setYear(-1);
    columns = Math.max(1, Math.min(4, dw));
    dh = Math.max(1, Math.min(4, dh));
    if (columns == 4 && dh == 4) dh = 3;

    jp.setPreferredSize(new Dimension(month[0].getPreferredSize().width + 10,
          (month[0].getPreferredSize().height + 5) * jp.getComponentCount() + 5));

    v = new JraScrollPane(JraScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JraScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    v.getViewport().setMinimumSize(new Dimension(month[0].getPreferredSize().width + 10,
      month[0].getPreferredSize().height + 10));
    v.getViewport().setPreferredSize(v.getViewport().getMinimumSize());
    v.setViewportView(jp);

    Dimension ps = v.getViewport().getMinimumSize();
    Dimension ns = new Dimension(columns * (ps.width - 5) + 5, dh * (ps.height - 5) + 5);

    setPreferredPanelSize();
    v.getViewport().setPreferredSize(ns);

    v.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        ensureProperViewSize();
      }
    });

    jp.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        //System.out.println("mouseclick "+e);
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0)
          mouseClick(e);
      }
      public void mousePressed(MouseEvent e) {
        //System.out.println("mousepress "+e);
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0)
          mousePress(e);
        if (e.getModifiers() == e.BUTTON3_MASK) {
          if (scroll) {
            scroll = false;
            scroller.stop();
          }
          setSelection(null, null);
          jp.repaint();
        }
      }
      public void mouseReleased(MouseEvent e) {
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0) {
          if (scroll) {
            scroll = false;
            scroller.stop();
          }
          if (first != null && last != null)
            setResultRange();
        }
      }
    });
    jp.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0)
          mouseDrag(e);
      }
    });
    scroller = new javax.swing.Timer(30, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (scroll && speed != 0) scrollView();
      }
    });
  }

  private void close() {
    if (main != null) {
      if (scroll) {
        scroll = false;
        scroller.stop();
      }
      location = main.getLocation();
      main.setVisible(false);
      setSelection(null, null);
      setHighlight(null);
      main.dispose();
      main = null;
    }
  }

  private void setResult() {
    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(highlight.getYear(), highlight.getMonth(), highlight.getDay());
    result = new Timestamp(c.getTime().getTime());
    close();
  }

  private void setResultRange() {
    Calendar c = Calendar.getInstance();
    c.clear();
    c.set(first.getYear(), first.getMonth(), first.getDay());
    from = new Timestamp(c.getTime().getTime());
    c.clear();
    c.set(last.getYear(), last.getMonth(), last.getDay());
    to = new Timestamp(c.getTime().getTime());
    close();
  }


  private void selectRange(DayLabel df, DayLabel dt, boolean sel, int addf, int addt) {
    if (df.getMonth() == dt.getMonth())
      df.getPanel().setSelectedDays(df.getDay() + addf, dt.getDay() - addt, sel);
    else {
      df.getPanel().setSelectedDays(df.getDay() + addf, df.getLastDay(), sel);
      for (int i = df.getMonth() + 1; i <= dt.getMonth() - 1; i++)
        month[i].setSelectedDays(1, 31, sel);
      dt.getPanel().setSelectedDays(1, dt.getDay() - addt, sel);
    }
  }

  private void setSelection(DayLabel df, DayLabel dt) {
    if (df == null || dt == null) {
      if (first != null && last != null)
        selectRange(first, last, false, 0, 0);
    } else if (first == null || last == null)
      selectRange(df, dt, true, 0, 0);
    else if (!first.equals(df) || !last.equals(dt)) {
      if (last.before(df) || first.after(dt)) {
        selectRange(first, last, false, 0, 0);
        selectRange(df, dt, true, 0, 0);
      } else {
        if (first.before(df)) selectRange(first, df, false, 0, 1);
        else if (first.after(df)) selectRange(df, first, true, 0, 1);
        if (last.after(dt)) selectRange(dt, last, false, 1, 0);
        else if (last.before(dt)) selectRange(last, dt, true, 1, 0);
      }
    }
    first = df;
    last = dt;
  }

  private void mouseClick(MouseEvent e) {
    Component c = findComponent(e.getPoint());
    if (e.getClickCount() == 2) {
      if (c instanceof DayLabel && highlight != null && highlight.isDate()) {
        if (!range) setResult();
        else {
          setSelection(highlight, highlight);
          setResultRange();
        }
      } else if (range) selectMonth(c);
    }
  }

  private Component findComponent(Point p) {
    Component c = jp.getComponentAt(p);
    if (!(c instanceof MonthPanel)) return c;
    
    Component cc = c.getComponentAt(p.x - c.getX(), p.y - c.getY());
    if (!(cc instanceof JPanel)) return cc;
    return cc.getComponentAt(p.x - c.getX() - cc.getX(), p.y - c.getY() - cc.getY());
  }

  private void mousePress(MouseEvent e) {
    Component c = findComponent(e.getPoint());
    ref = null;
    if (range) {
      if (e.isShiftDown() && highlight != null &&
          c instanceof DayLabel && ((DayLabel) c).isDate()) {
        ref = highlight;
        if (ref.before((DayLabel) c)) setSelection(ref, (DayLabel) c);
        else setSelection((DayLabel) c, ref);
      } else setSelection(null, null);
    }
    if (c instanceof DayLabel)
      setHighlight((DayLabel) c);
  }

  private void scrollView() {
    Point t = v.getViewport().getViewPosition();
    t.setLocation(t.x, Math.max(0, Math.min(jp.getHeight() - v.getViewport().getHeight(), t.y + speed)));
    v.getViewport().setViewPosition(t);
    DayLabel p;
    if (speed < 0) p = getFirstVisibleDay();
    else p = getLastVisibleDay();
    if (range && ref != null) {
      if (p.before(ref)) setSelection(p, ref);
      else setSelection(ref, p);
    }
    setHighlight(p);
  }

  private void mouseDrag(MouseEvent e) {
    Rectangle view = v.getViewport().getViewRect();
    if (!view.contains(e.getPoint())) {
      if (!scroll) {
        scroll = true;
        scroller.restart();
      }
      if (e.getY() < view.y)
        speed = (e.getY() - view.y) / 2 - 1;
      else if (e.getY() > view.y + view.height)
        speed = (e.getY() - view.y - view.height) / 2 + 1;
      else speed = 0;
      return;
    }
    if (scroll) {
      scroll = false;
      scroller.stop();
    }
    if (!range) {
      mousePress(e);
    } else if (ref != null || highlight != null) {
//      System.out.print("drag ");
      DayLabel p = null;
      Component c = findComponent(e.getPoint());
      if (c instanceof DayLabel) {
        p = (DayLabel) c;
        if (!p.isDate()) {
          if (p.getDay() == 0) p = p.getPanel().getDay(1);
          else p = p.getPanel().getDay(p.getLastDay());
        }
      }
      if (p != null) {
        if (ref == null) {
          if (highlight.equals(p)) return;
          ref = highlight;
        }
        if (p.before(ref)) setSelection(p, ref);
        else setSelection(ref, p);
        setHighlight(p);
      }
    }
  }

  private boolean isVisible(DayLabel d) {
    return isVisible(d, v.getViewport().getViewRect());
  }

  private boolean isVisible(DayLabel d, Rectangle view) {
    return view.intersects(d.getViewBounds(br));
  }

  private void checkHighlightVisible() {
    jp.repaint();
    if (highlight == null) return;
    if (!isVisible(highlight))
      v.getViewport().setViewPosition(new Point(0, Math.max(0,
        Math.min(jp.getHeight() - v.getViewport().getHeight(), highlight.getPanel().getY() - 5)
      )));
  }

  private DayLabel getLastVisibleDay() {
    Rectangle view = v.getViewport().getViewRect();
    for (int i = 11; i >= 0; i--)
      for (int d = month[i].getDays(); d > 0; d--)
        if (isVisible(month[i].getDay(d), view))
          return month[i].getDay(d);
    return null;
  }

  private DayLabel getFirstVisibleDay() {
    Rectangle view = v.getViewport().getViewRect();
    for (int i = 0; i < 12; i++)
      for (int d = 1; d <= month[i].getDays(); d++)
        if (isVisible(month[i].getDay(d), view))
          return month[i].getDay(d);
    return null;
  }

  private void setPreferredPanelSize() {
    Dimension ps = v.getViewport().getMinimumSize();

    jp.setPreferredSize(new Dimension(columns * (ps.width - 5) + 5,
       ((jp.getComponentCount() - 1) / columns + 1) * (ps.height - 5) + 5));
  }

  private void ensureProperViewSize() {
    Dimension ps = v.getViewport().getMinimumSize();
    Dimension rs = v.getViewport().getSize();
    int nw = Math.min(4, Math.max(((rs.width - 5 + ps.width / 2) / (ps.width - 5)), 1));
    int nh = Math.min(5, Math.max(((rs.height - 5 + ps.height / 2) / (ps.height - 5)), 1));

    if (nh > (jp.getComponentCount() - 1) / nw + 1)
      nh = (jp.getComponentCount() - 1) / nw + 1;

    Dimension ns = new Dimension(nw * (ps.width - 5) + 5, nh * (ps.height - 5) + 5);
    if (ns.equals(rs)) return;

    columns = nw;
    setPreferredPanelSize();
    v.getViewport().setPreferredSize(ns);
    main.pack();
    v.getViewport().setViewPosition(new Point(0, jp.getPreferredSize().height));
    checkHighlightVisible();
  }

  private void selectMonth(Component m) {
    if (m instanceof JLabel) {
      String name = ((JLabel) m).getText();
      if (name != null && name.length() > 0)
        for (int i = 0; i < 12; i++)
          if (name.startsWith(MonthPanel.monthName[month[i].getMonth()])) {
            setSelection(month[i].getDay(1), month[i].getDay(31));
            setResultRange();
            break;
          }
    }
  }

  private void setHighlight(DayLabel l) {
    if (l == highlight) return;
    if (highlight != null) highlight.setHighlight(false);
    highlight = l;
    if (highlight != null) {
      if (!highlight.isDate()) {
        if (highlight.getDay() == highlight.BEFORE_FIRST)
          highlight = highlight.getPanel().getDay(1);
        else highlight = highlight.getPanel().getDay(highlight.getLastDay());
      }
      highlight.setHighlight(true);
      checkHighlightVisible();
    }
  }

  private void setHighlight(int m, int d) {
    for (int i = 0; i < 12 && month[i] != null; i++)
      if (month[i].getMonth() == m) {
        DayLabel sel = month[i].getDay(d);
        if (range && previous != null) {
          if (previous == last && !sel.before(first))
            setSelection(first, sel);
          else if (previous == first && !sel.after(last))
            setSelection(sel, last);
          else if (previous.before(sel))
            setSelection(previous, sel);
          else setSelection(sel, previous);
        }
        setHighlight(sel);
        return;
      }
    if (m < 0) {
      setYear(month[0].getYear() - 1);
      setPreferredPanelSize();
      main.pack();
      setHighlight(11, d);
    } else if (m > 11) {
      setYear(month[0].getYear() + 1);
      setPreferredPanelSize();
      main.pack();
      setHighlight(0, d);
    }
  }

  private void moveHighlight(int diff) {
    if ((diff < 0 && highlight.getDay() > -diff) ||
        (diff > 0 && highlight.getDay() <= highlight.getLastDay() - diff))
      setHighlight(highlight.getMonth(), highlight.getDay() + diff);
    else
      setHighlight(highlight.getMonth() + (diff > 0 ? 1 : -1), (diff > 0 ? 1 : 31));
  }

  private void addDialogListeners() {
    main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    main.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        close();
      }
    });
    main.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (highlight == null) return;
        if (e.isShiftDown() && range) previous = highlight;

        if (e.getKeyCode() == e.VK_LEFT) moveHighlight(-1);
        else if (e.getKeyCode() == e.VK_RIGHT) moveHighlight(1);
        else if (e.getKeyCode() == e.VK_UP) moveHighlight(-7);
        else if (e.getKeyCode() == e.VK_DOWN) moveHighlight(7);
        else if (e.getKeyCode() == e.VK_PAGE_UP)
          setHighlight(highlight.getMonth() - 1, highlight.getDay());
        else if (e.getKeyCode() == e.VK_PAGE_DOWN)
          setHighlight(highlight.getMonth() + 1, highlight.getDay());
        else if (e.getKeyCode() == e.VK_HOME) {
          if (highlight.getDay() == 1) setHighlight(0, 1);
          else setHighlight(highlight.getMonth(), 1);
        } else if (e.getKeyCode() == e.VK_END) {
          if (highlight.getDay() == highlight.getLastDay()) setHighlight(11, 31);
          else setHighlight(highlight.getMonth(), highlight.getLastDay());
        } else if (e.getKeyCode() == e.VK_ESCAPE) {
          close();
        }
        previous = null;
      }
      public void keyReleased(KeyEvent e) {
        if (highlight == null) return;
        if (e.getKeyCode() == e.VK_ENTER) {
          if (!range) setResult();
          else if (first != null && last != null) setResultRange();
        }
      }
    });
  }
}

