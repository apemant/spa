/****license*****************************************************************
**   file: raOptimisticMatch.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Pokriveni;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;
import hr.restart.util.Aus;
import hr.restart.util.Int2;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raNavBar;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableCellRenderer;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raOptimisticMatch {
  private static raOptimisticMatch inst = new raOptimisticMatch();
  private Valid vl = Valid.getValid();

  // mapa svih mapa:
  // kljucevi su (partner$konto$valuta$godina$uloga),
  // a vrijednosti mape salda na popis dokumenata.
  private HashMap master = new HashMap();
  private JTable table;
  private JPanel contents;
  private raNavBar nav;
  private OKpanel okp;
  private MatchTableModel model;
  private JraComboBox cbox;
  private JraDialog win;
  private double threshold = 0.60;
  private String ignore = " -./,";


  private raOptimisticMatch() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean find(int depth) {
    return find(depth, Condition.none);
  }

  // extra je dodatni condition koji se moze poslati, npr. samo kupci, samo jedna godina itd.
  public static boolean find(int depth, Condition extra) {
    return inst.findAll(depth, extra);
  }

  public static void setMarkThreshold(double factor) {
    inst.threshold = factor;
  }

  public static void setIgnoreChars(String chars) {
    inst.ignore = chars;
  }

  static double getFactor() {
    return inst.threshold;
  }

  static String getIgnore() {
    return inst.ignore;
  }

  private boolean findAll(int maxDepth, Condition cond) {
    // nadji sve stavke koje zadovoljavaju uvjet, a nisu pokrivene.
    DataSet ds = Skstavke.getDataModule().getTempSet(Aus.getKnjigCond().and(cond).
        and(Aus.getFreeYearCond()).and(Condition.equal(raSaldaKonti.colPok(), "N")));

    // ako je metoda pozvana iz raProcess sustava, iskoristi ga
    if (!raProcess.isRunning()) ds.open();
    else {
      raProcess.setMessage("Dohvat nepokrivenih stavaka salda konti ...", true);
      raProcess.openScratchDataSet(ds);
    }

    // najprije prevrti cijeli dataset i prebaci sve stavke u MatchRow,
    // te ih napuni u LinkedList iz kojih cemo ih izbacivati kad i ako
    // pronadjemo kombinaciju u kojoj se moze pokriti.
    LinkedList stavke = new LinkedList();
    for (ds.first(); ds.inBounds(); ds.next())
      stavke.addLast(new MatchRow(ds));

    // pocni raditi kombinacije. Najprije svaka stavka pojedinacno,
    // onda sve do 'maxDepth' stavki po kombinaciji. Prati i potrosnju
    // objekata da ne predje neku razumnu granicu.
    int slots = 0, collections = 0;
    master.clear();
    for (int depth = 1; depth <= maxDepth; depth++) {
      if (raProcess.isRunning())
        raProcess.setMessage("Provjera moguæeg pokrivanja stupnja "+depth+" ...", false);

      // prodji kroz sve preostale stavke i puni glavnu mapu,
      // koja se sastoji od po jedne mape salda na popis dokumenata za
      // svaku razlicitu kombinaciju cpar$konto$valuta$godina$uloga
      // koja postoji u nadjenim stavkama. Jebeno.
      for (Iterator st = stavke.iterator(); st.hasNext(); ) {
        MatchRow mr = (MatchRow) st.next();
        if (raProcess.isRunning()) raProcess.checkClosing();

        // probaj naci vec postojecu grupu salda za ovaj kljuc ili napravi novu
        SaldoGroup salda = (SaldoGroup) master.get(mr.getMasterKey());
        if (salda == null) master.put(mr.getMasterKey(), salda = new SaldoGroup());

        // Inicijaliziraj listu kombinacija koja ce se kasnije ubaciti u mapu salda.
        // Kombinacije se ne mogu odmah ubacivati u mapu jer se po njoj iterira, pa
        // bi to rezultiralo gadnim kolizijama. Ako je depth 1, onda se napravi
        // kombinacija od samo teukce stavke, inace se prolazi kroz cijelu mapu
        // salda i priprema dodavanje novih kombinacija duljine depth.
        ArrayList toAdd = new ArrayList();
        if (depth == 1) toAdd.add(new MatchableCollection(mr));
        else for (Iterator it = salda.slotIterator(); it.hasNext(); ) {
          SaldoSlot ss = (SaldoSlot) it.next();
          if (raProcess.isRunning()) raProcess.checkClosing();

          // prodji koz sve kombinacije u ovom slotu (koje imaju taj saldo)
          // i pripremi dodavanje po jedne nove kombinacije za svaku koja
          // ima depth-1 clanova, i koja je istog tipa kao nova stavka
          // (tip racuni ili uplate), te koja vec nije u toj kombinaciji.
          for (int i = 0; i < ss.getCollectionCount(); i++) {
            MatchableCollection mc = ss.getCollection(i);
            if (mc.checkAdd(mr, depth))
              toAdd.add(mc.copyAndExtend(mr));
          }
        }

        // i na kraju ubaci sve pripremljene kombinacije iz liste toAdd
        // u odgovarajuce slotove mape salda.
        slots -= salda.countSlots();
        for (int i = 0; i < toAdd.size(); i++) {
          MatchableCollection mc = (MatchableCollection) toAdd.get(i);
          if (raProcess.isRunning()) raProcess.checkClosing();

          // probaj naci postojeci saldo slot ili napravi novog ako ga nema,
          // te dodaj ovu kombinaciju u njega.
          SaldoSlot ss = (SaldoSlot) salda.getSlot(mc.getMatchSaldo());
          if (ss == null) salda.addSlot(mc.getMatchSaldo(), ss = new SaldoSlot());
          ss.addCollection(mc);
          mc.setOwner(ss);
        }
        slots += salda.countSlots();
        collections += toAdd.size();
        if (collections > 100000) break;
      }
      if (collections > 100000) break;

      // ako se stavka vec nalazi u nekoj kombinaciji koja se moze pokriti,
      // nema potrebe traziti daljnje kombinacije s njom, tj kombinacije s
      // vise clanova. Cilj je da se minimiziraju visestruka pokrivanja.
      for (Iterator st = stavke.iterator(); st.hasNext(); ) {
        MatchRow mr = (MatchRow) st.next();
        if (mr.isMatchable()) st.remove();
      }
    }

    // na kraju balade, u glavnoj mapi imamo mnostvo mapa (za svaki kljuc
    // cpar$konto$valuta$godina$uloga po jedna) u cijim slotovima se
    // potencijalno nalaze kombinacije stavaka koje se mogu medjusobno
    // pokriti. Sve te kombinacije bit ce ponudjene za pokrivanje, grupirane
    // po saldu. No najprije treba izbaciti iz mape sve one slotove kod kojih
    // nema bar po jedna kombinacija tipa uplate i jedna tipa racun.
    for (Iterator it = master.values().iterator(); it.hasNext(); ) {
      SaldoGroup salda = (SaldoGroup) it.next();
      for (Iterator s = salda.slotIterator(); s.hasNext(); ) {
        SaldoSlot ss = (SaldoSlot) s.next();
        if (!ss.isMatchable()) s.remove();
      }
      if (salda.countSlots() == 0) it.remove();
    }

    // medju preostalim mapama probaj naci najbolje matcheve.
    for (Iterator it = master.values().iterator(); it.hasNext(); ) {
      SaldoGroup salda = (SaldoGroup) it.next();
      for (Iterator s = salda.slotIterator(); s.hasNext(); ) {
        SaldoSlot ss = (SaldoSlot) s.next();
        for (int i = 0; i < ss.getCollectionCount(); i++) {
          MatchableCollection mi = ss.getCollection(i);
          mi.findRepresentations();
          double now;
          for (int j = 0; j < ss.getCollectionCount(); j++) {
            MatchableCollection mj = ss.getCollection(j);
            if (mi.isRacunTip() != mj.isRacunTip() &&
                (now = mi.getMatchFactor(mj)) > mi.getMatchedFactor() &&
                now > mj.getMatchedFactor() && now > mi.getMaxMatchStrength() &&
                now > mj.getMaxMatchStrength())
              mi.setMatch(mj);
          }
        }
      }
      salda.findMarks();
    }
    return master.size() > 0;
  }

  public static boolean isAnythingFound() {
    return inst.master.size() > 0;
  }

  public static void showResultDialog(Container owner, String title) {
    inst.showResults(owner, title);
  }

  public void showResults(Container parent, String title) {
    Container realparent = null;

    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, true);
    else win = new JraDialog((Frame) null, title, true);
    win.setContentPane(contents);
    ((MatchTableModel) table.getModel()).fillData(master);
    win.pack();
    win.setLocationRelativeTo(parent);
    win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    win.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        CancelPress();
      }
    });
    win.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == e.VK_ESCAPE) {
          CancelPress();
          e.consume();
        }
      }
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_A && e.getModifiers() == e.CTRL_MASK) {
          ((Action) table.getActionMap().get("selectAll")).actionPerformed(null);
          e.consume();
        }
      }
    });
    int dd = ToolTipManager.sharedInstance().getDismissDelay();
    ToolTipManager.sharedInstance().setDismissDelay(10000);
    okp.registerOKPanelKeys(win);
    table.scrollRectToVisible(new Rectangle(0,0,10,10));
    if (table.isEditing())
      table.removeEditor();
    win.show();
    ToolTipManager.sharedInstance().setDismissDelay(dd);
  }

  private Rectangle srect = new Rectangle();
  private Point spoint = new Point();

  private void paintSections(Graphics g) {
    int rmin, rmax, rh = table.getRowHeight();
    MatchTableModel model = (MatchTableModel) table.getModel();
    g.getClipBounds(srect);
    spoint.setLocation(0, srect.y);
    rmin = table.rowAtPoint(spoint);
    if (rmin < 0) rmin = 0;
    spoint.setLocation(0, srect.y + srect.height);
    rmax = table.rowAtPoint(spoint);
    if (rmax < 0) rmax = table.getRowCount() - 1;
    for (int i = rmin; i <= rmax; i++) {
      if (model.isRowHeader(i)) {
        if (model.isRowHeader2(i)) {
          g.clearRect(0, rh * i, table.getWidth(), rh - 1);
          g.drawString(model.getDescription(i), 1, rh * i +
                       table.getFontMetrics(table.getFont()).getAscent());
        } else g.clearRect(0, rh * i, table.getWidth(), rh * 2 - 1);
      }
    }
  }

  private void init() throws Exception {
    model = new MatchTableModel();
    cbox = new JraComboBox();
    cbox.setRenderer(new BasicComboBoxRenderer() {
      public Component getListCellRendererComponent(JList l, Object val,
             int idx, boolean sel, boolean foc) {
        if (sel && idx >= 0 && val instanceof MatchableCollection)
          l.setToolTipText(((MatchableCollection) val).getToolTipText());
        return super.getListCellRendererComponent(l, val, idx, sel, foc);
      }
    });
    table = new JTable(model) {
      public void paintChildren(Graphics g) {
        super.paintChildren(g);
        paintSections(g);
      }
      public void changeSelection(int row, int col, boolean toggle, boolean extend) {
        int scrow = row;
        int oldrow = getSelectedRow();
        int oldcol = getSelectedColumn();
        int maxrow = model.getRowCount() - 2;
        if (model.isRowHeader(row) && (toggle || extend)) return;
        if (oldrow != -1 && model.isRowHeader(row)) {
          if (oldrow < row) while (row < maxrow && model.isRowHeader(++row));
          else while (row > 0 && model.isRowHeader(--row));
          if (model.isRowHeader(row))
            if (oldrow < row) while (row > 0 && model.isRowHeader(--row));
            else while (row < maxrow && model.isRowHeader(++row));
          if (model.isRowHeader(row)) row = oldrow;
        }
        if (scrow < 2) scrow = 0;
        else if (scrow <= maxrow) scrow = row;
        getSelectionModel().setSelectionInterval(row, row);
//        super.changeSelection(row, col, false, false);
        if (oldrow < 0) oldrow = row;
        if ((toggle || extend) && (col == oldcol || row != oldrow)) {
          if (!extend) selectOrToggle(row, true);
          else {
            int beg = (oldrow > row ? row : oldrow), end = oldrow + row - beg;
            if (beg == end && extend) return;
            for (int i = beg; i <= end; i++)
              selectOrToggle(i, toggle);
          }
          repaint();
        }
        if (getAutoscrolls()) {
          Rectangle cellRect = getCellRect(scrow, 0, true);
          if (cellRect != null) scrollRectToVisible(cellRect);
        }
      }
      private void selectOrToggle(int row, boolean toggle) {
        if (toggle) model.toggleRowSelected(row);
        else model.setRowSelected(row, true);
      }
    };
    table.setColumnSelectionAllowed(false);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    for (int i = 0; i < model.m_columns.length; i++)
      table.getColumnModel().getColumn(i).setPreferredWidth(model.m_columns[i].m_width);
    table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cbox) {
      public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) {
        cbox.setModel(new DefaultComboBoxModel(model.getMatchOptions(row)));
        cbox.setToolTipText(v instanceof MatchableCollection ?
                            ((MatchableCollection) v).getToolTipText() : null);
        return super.getTableCellEditorComponent(t, v, sel, row, col);
      }
    });
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      private javax.swing.border.Border emptyBorder =
          BorderFactory.createEmptyBorder(0, 2, 0, 2);
      private Color m = null;
      private Color g = Color.green.darker().darker().darker();
      private Color halfTone(Color cFrom, Color cTo, float factor) {
        return new Color((int) (cFrom.getRed() * (1 - factor) + cTo.getRed() * factor),
                         (int) (cFrom.getGreen() * (1 - factor) + cTo.getGreen() * factor),
                         (int) (cFrom.getBlue() * (1 - factor) + cTo.getBlue() * factor));
      }
      public Component getTableCellRendererComponent(JTable t, Object v,
          boolean sel, boolean foc, int row, int col) {
        super.getTableCellRendererComponent(t, v, sel, false, row, col);
        setOpaque(true);
        setBorder(emptyBorder);
        if (model.isRowSelected(row)) {
          if (m == null) m = halfTone(Color.yellow, t.getBackground(), 0.75f);
          super.setBackground(sel ? g : m);
        } else super.setBackground(sel ? t.getSelectionBackground() : t.getBackground());
        int rc = t.convertColumnIndexToModel(col);
        if (rc == 2) setHorizontalAlignment(JLabel.TRAILING);
        else {
          setHorizontalAlignment(JLabel.LEADING);
          if (rc == 0 || rc == 1)
            setToolTipText(model.getToolTip(row, col));
        }
        return this;
      }
    });

    table.setRowHeight(21);
    table.setPreferredScrollableViewportSize(new Dimension(740,
        table.getPreferredScrollableViewportSize().height));
/*    table.getActionMap().put("select-all", new AbstractAction() {
      if (e.getKeyCode() == e.VK_A && e.getModifiers() == e.CTRL_MASK) {
        for (int i = 0; i < model.getRowCount(); i++)
          model.toggleRowSelected(i);
        table.repaint();
        e.consume();
      }
    });*/

    table.getActionMap().put("selectAll", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (table.isEditing())
          table.removeEditor();
        for (int i = 0; i < model.getRowCount(); i++)
          model.toggleRowSelected(i);
        table.repaint();
      }
    });
    table.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER && table.getSelectedRow() >= 0) {
          model.toggleRowSelected(table.getSelectedRow());
          table.repaint();
        }
      }
    });

    contents = new JPanel(new BorderLayout());
    okp = new OKpanel() {
      public void jBOK_actionPerformed() {
        OKPress();
      }
      public void jPrekid_actionPerformed() {
        CancelPress();
      }
    };
    JPanel pd = new JPanel(null);
    pd.setLayout(new BoxLayout(pd, BoxLayout.X_AXIS));

    JButton all = new JraButton();
    all.setText("Oznaèi sve");
    all.setIcon(raImages.getImageIcon(raImages.IMGALIGNJUSTIFY));
    all.setPreferredSize(new Dimension(120, 25));
    all.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < model.getRowCount(); i++)
          model.setRowSelected(i, true);
        table.repaint();
      }
    });
    okp.add(all, BorderLayout.WEST);

    contents.add(new JraScrollPane(table));
    contents.add(okp, BorderLayout.SOUTH);
    okp.registerOKPanelKeys(table);
  }

  private Int2 total = new Int2();
  private void performMatching() {
    QueryDataSet sks = null, pok = null;
    Int2 run = new Int2();
    String lastcpar = "";
    total.clear();

    for (int row = 0; row < model.getRowCount(); row++)
      if (model.isRowSelected(row)) {
        MatchableCollection mc = (MatchableCollection) model.getValueAt(row, 0);
        String mkey = mc.getRow(0).getMasterKey();
        String cpar = mkey.substring(0, mkey.indexOf('$'));
        if (!cpar.equals(lastcpar)) {
          if (sks != null && raTransaction.saveChangesInTransaction(new QueryDataSet[] {sks, pok}))
            total = total.add(run);
          run.clear();
          sks = dM.getDataModule().getSkstavke();
          Skstavke.getDataModule().setFilter(Aus.getKnjigCond().and(Aus.getFreeYearCond()).
              and(Condition.equal("CPAR", Aus.getNumber(lastcpar = cpar))));
          sks.open();
          pok = Pokriveni.getDataModule().getFilteredDataSet("1=0");
          pok.open();
        }
        PotentialMatch pm = new PotentialMatch();
        for (int i = 0; i < mc.getRowCount(); i++)
          pm.addStavka(mc.getRow(i));
        for (int i = 0; i < mc.getMatch().getRowCount(); i++)
          pm.addStavka(mc.getMatch().getRow(i));
        run = run.add(pm.realize(sks, pok));
      }
    if (sks != null && R2Handler.saveChangesInTransaction(new QueryDataSet[] {sks, pok}))
      total = total.add(run);
    dM.getDataModule().getSynchronizer().markAsDirty("pokriveni");
  }

  private void OKPress() {
    Window owner = win.getOwner();
    if (table.isEditing())
      table.removeEditor();
    if (win != null) {
      win.dispose();
      win = null;
    }
    raProcess.runChild(owner, new Runnable() {
      public void run() {
        performMatching();
      }
    });
    if (total.one == 0 && raProcess.isCompleted())
      JOptionPane.showMessageDialog(owner, "Nema stavaka za pokrivanje!",
                                    "Poruka", JOptionPane.INFORMATION_MESSAGE);
    else if (total.one > 0) JOptionPane.showMessageDialog(owner, Aus.getNumDep(total.one,
        "Pokriven "+total.one+" saldo",
        "Pokrivena "+total.one+" salda",
        "Pokriveno "+total.one+" salda") +
        " na ukupno "+Aus.getNum(total.two, "stavci.", "stavke.", "stavaka."),
        "Poruka", JOptionPane.INFORMATION_MESSAGE);
  }

  private void CancelPress() {
    if (JOptionPane.showConfirmDialog(win, "Izlazak iz pokrivanja? " +
        "(Odabir se gubi)", "Potvrda izlaza", JOptionPane.OK_CANCEL_OPTION) == 
          JOptionPane.CANCEL_OPTION) return;

    if (table.isEditing())
      table.removeEditor();
    if (win != null) {
      win.dispose();
      win = null;
    }
  }
}

class ColumnData  {
  public String m_title;
  public int m_width;
  public int m_alignment;
  public ColumnData(String title, int width, int alignment) {
    m_title = title;
    m_width = width;
    m_alignment = alignment;
  }
}

class RowData {
  public String key, saldo;
  public boolean act;
  public MatchableCollection mc;

  public RowData(String key, MatchableCollection mc) {
    this.key = key;
    this.mc = mc;
    act = false;
    updateValues();
  }

  public void updateValues() {
    if (mc != null) {
      saldo = mc.getSaldoFormatted();
      act = act || (mc.getMatch() != null && mc.getMatchedFactor() > raOptimisticMatch.getFactor());
      act = act && mc.getMatch() != null;
    } else {
      saldo = "";
      act = false;
    }
  }

/*  public int compareToInverse(RowData other, int col) {
    if (!key.equals(other.key)) return other.key.compareTo(key);
    if (mc == null || other.mc == null)
      return (other.mc == null ? 1 : 0) - (mc == null ? 1 : 0);
    return other.compare(this, col);
  }

  public int compareTo(RowData other, int col) {
    if (!key.equals(other.key)) return key.compareTo(other.key);
    if (mc == null || other.mc == null)
      return (other.mc == null ? 1 : 0) - (mc == null ? 1 : 0);
    return compare(other, col);
  }

  private int compare(RowData other, int col) {
    switch (col) {
      case 0:
        return saldo.compareTo(other.saldo);
      case 1:
        return dok1.compareToIgnoreCase(other.dok1);
      case 2:
        return dok2.compareToIgnoreCase(other.dok2);
      case 3:
        return (act ? 1 : 0) - (other.act ? 1 : 0);
      default:
        return 0;
    }
  }*/

  public Object getElement(int col) {
    if (mc == null) return "";
    switch (col) {
      case 2:
        return saldo;
      case 0:
        return mc;
      case 1:
        return mc.getMatch() != null ? mc.getMatch() : MatchableCollection.none;
      default:
        return "";
    }
  }

  public String getToolTipText(int col) {
    if (col == 0) return mc.getToolTipText();
    else if (col == 1) return ((MatchableCollection) getElement(1)).getToolTipText();
    else return null;
  }

  public String getDescription() {
    return key;
  }

  public boolean isSelected() {
    return act;
  }

  public void setSelected(boolean on) {
    act = on;
  }

  public boolean canSelect() {
    return mc != null && mc.getMatch() != null;
  }
}

class MatchTableModel extends javax.swing.table.AbstractTableModel {
  static final public ColumnData m_columns[] = {
    new ColumnData( "Dokumenti koji se mogu pokriti", 320, JLabel.LEFT ),
    new ColumnData( "Moguæi izbori za pokrivanje", 320, JLabel.LEFT ),
    new ColumnData( "Saldo", 100, JLabel.RIGHT )};

  Vector rows = new Vector();
  int sortColumn = -1;
  boolean ascending;

  public MatchTableModel() {
  }

  private String analyzeKey(String key) {
    VarStr desc = new VarStr();
    String[] parts = new VarStr(key).split('$');

    lookupData.getlookupData().raLocate(dM.getDataModule().getPartneri(), "CPAR", parts[0]);
    desc.append("K".equals(parts[1]) ? "Kupac " : "Dobavljaè ").append(parts[0]);
    desc.append(" - ").append(dM.getDataModule().getPartneri().getString("NAZPAR"));
    desc.append(", konto ").append(parts[2]);
    return desc.toString();
  }

  public void fillData(final HashMap master) {
//    javax.swing.plaf.basic.BasicTableUI tui;
    rows.clear();
    ArrayList keys = new ArrayList(master.keySet());
    Collections.sort(keys, new Comparator() {
      public int compare(Object o1, Object o2) {
        SaldoGroup s1 = (SaldoGroup) master.get(o1);
        SaldoGroup s2 = (SaldoGroup) master.get(o2);
        if (s1.countMarkedCollections() != s2.countMarkedCollections())
          return s2.countMarkedCollections() - s1.countMarkedCollections();
        else if (s1.countTotalCollections() != s2.countTotalCollections())
          return s1.countTotalCollections() - s2.countTotalCollections();
        return Aus.getAnyNumber((String) o1) - Aus.getAnyNumber((String) o2);
      }
    });
    for (Iterator it = keys.iterator(); it.hasNext(); ) {
      String key = (String) it.next();
      String desc = analyzeKey(key);
      SaldoGroup salda = (SaldoGroup) master.get(key);
      rows.add(new RowData(desc, null));
      rows.add(new RowData(desc, null));
      for (Iterator s = salda.slotIterator(); s.hasNext(); ) {
        SaldoSlot ss = (SaldoSlot) s.next();
        for (int c = 0; c < ss.getCollectionCount(); c++) {
          MatchableCollection mc = ss.getCollection(c);
          if ((mc.getMatch() != null || mc.getMaxMatchStrength() <= raOptimisticMatch.getFactor()) &&
            mc.isRacunTip() == (ss.getNumRac() <= ss.getNumUpl())) rows.add(new RowData(desc, mc));
        }
      }
    }
    rows.add(new RowData("", null));
    this.fireTableDataChanged();
  }

  public boolean isRowHeader(int row) {
    if (row < 0 || row >= rows.size()) return false;
    return ((RowData) rows.get(row)).mc == null;
  }

  public boolean isRowHeader2(int row) {
    return row > 0 && isRowHeader(row) && isRowHeader(row - 1);
  }

  public int getRowCount(){
    return rows == null ? 0: rows.size();
  }

  public int getColumnCount(){
    return 3;
  }

  public boolean isRowSelected(int row) {
    if (row >= getRowCount() || row < 0) return false;
    return ((RowData) rows.get(row)).isSelected();
  }

  public void setRowSelected(int row, boolean on) {
    RowData rd = (RowData) rows.get(row);
    rd.setSelected(on && rd.canSelect());
  }

  public void toggleRowSelected(int row) {
    setRowSelected(row, !isRowSelected(row));
  }

  public boolean isCellEditable(int row, int col) {
    return (col == 1 && !isRowHeader(row));
  }

  public Object[] getMatchOptions(int row) {
    RowData rd = (RowData) rows.get(row);
    SaldoSlot ss = rd.mc.getOwner();
    ArrayList ret = new ArrayList();
    ret.add(MatchableCollection.none);
    for (int i = 0; i < ss.getCollectionCount(); i++) {
      MatchableCollection mc = ss.getCollection(i);
      if (rd.mc.isRacunTip() != mc.isRacunTip() &&
         (mc.getMaxMatchStrength() <= 0 || mc.getMatch() == rd.mc))
        ret.add(mc);
    }
    return ret.toArray();
  }

  public String getDescription(int row) {
    if (row >= getRowCount() || row < 0) return "";
    return ((RowData) rows.get(row)).getDescription();
  }

  public Object getValueAt(int row, int col) {
    if (row >= getRowCount() || row < 0) return "";
    return ((RowData) rows.get(row)).getElement(col);
  }

  public void setValueAt(Object obj, int row, int col) {
    if (col == 1 && !isRowHeader(row)) {
      RowData rd = (RowData) rows.get(row);
      if (!(obj instanceof MatchableCollection) || obj == MatchableCollection.none)
        rd.mc.setMatch(null);
      else rd.mc.setMatch((MatchableCollection) obj);
      rd.updateValues();
      fireTableRowsUpdated(row, row);
    }
  }

  public String getColumnName(int column) {
    return m_columns[column].m_title;
  }

  public String getToolTip(int row, int col) {
    if (row >= getRowCount() || row < 0 || isRowHeader(row)) return null;
    return ((RowData) rows.get(row)).getToolTipText(col);
  }
}

class SaldoGroup {
  private HashMap map;
  private int marked, total;

  public SaldoGroup() {
    map = new HashMap();
  }

  public SaldoSlot getSlot(BigDecimal saldo) {
    return (SaldoSlot) map.get(saldo);
  }

  public void addSlot(BigDecimal saldo, SaldoSlot slot) {
    map.put(saldo, slot);
  }

  public Iterator slotIterator() {
    return map.values().iterator();
  }

  public int countSlots() {
    return map.size();
  }

  public void findMarks() {
    marked = total = 0;
    for (Iterator it = map.values().iterator(); it.hasNext(); ) {
      SaldoSlot ss = (SaldoSlot) it.next();
      total += ss.getNumMatch();
      int matched = 0;
      for (int i = 0; i < ss.getCollectionCount(); i++)
        if (ss.getCollection(i).getMatchedFactor() > raOptimisticMatch.getFactor()) ++matched;
      marked += matched / 2;
    }
  }

  public int countMarkedCollections() {
    return marked;
  }

  public int countTotalCollections() {
    return total;
  }
}

// klasa za pamcenje svih kombinacija dokumenata ciji efektivni zbroj salda
// ima odredjeni iznos. Svaka kombinacija (MatchableCollection) sastoji se
// od odredjenog broja sk stavaka (ista stavka moze biti u vise kombinacija)
// i tip, tj. vrsta stavaka kombinacije, 'racun' ili 'uplata'. Kasnije ce
// se te kombinacije iz pojedinog slota ponuditi za pokrivanje ako u slotu
// postoji bar jedna kombinacija tipa 'racun' i jedna tipa 'uplaza'
// Klasa je zapravo tek wrapper za ArrayList pun MatchableCollectiona...

class SaldoSlot {
  private ArrayList collections;                // popis kombinacija
  private int numRac, numUpl;

  public SaldoSlot() {
    collections = new ArrayList();
    numRac = numUpl = 0;
  }

  public void addCollection(MatchableCollection mc) {
    boolean notm = false;
    collections.add(mc);
    if (numRac > 0 && numUpl > 0) mc.setMatchable();
    else notm = true;
    if (mc.isRacunTip()) ++numRac;
    else ++numUpl;
    if (notm && numRac > 0 && numUpl > 0) setMatchable();
  }

  public int getCollectionCount() {
    return collections.size();
  }

  public MatchableCollection getCollection(int i) {
    return (MatchableCollection) collections.get(i);
  }

  public boolean isMatchable() {
    return (numRac > 0 && numUpl > 0);
  }

  public int getNumRac() {
    return numRac;
  }

  public int getNumUpl() {
    return numUpl;
  }

  public int getNumMatch() {
    return numRac < numUpl ? numRac : numUpl;
  }

  public void setMatchable() {
    for (int i = 0; i < collections.size(); i++)
      getCollection(i).setMatchable();
  }
}


// klasa za pamcenje odredjene kombinacije sk stavki. Pamti popis sk stavki
// od kojih se sastoji, tip (racuni ili uplate) i efektivni saldo kombinacije.
// Pod 'efektivni saldo' mislim na to da negativni iznos obavijesti knjizenja
// tretiram kao pozitivni na suprotnoj strani, tako da ih fino mogu zbrojiti.
class MatchableCollection {
  private boolean racTip;                       // tip kombinacije
  private ArrayList rows = new ArrayList();     // popis sk stavki
  private BigDecimal saldo;                     // efektivni saldo kombinacije
  private SaldoSlot owner;
  private String repr, tool;

  private MatchableCollection match;
  private double matchFactor = 0;
  private static VarStr shared = new VarStr();
  public static final MatchableCollection none = new MatchableCollection();
  private static NumberFormat nf;
  static {
    nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);
  }

  public double getMatchedFactor() {
    return matchFactor;
  }

  public double getMatchFactor(MatchableCollection other) {
    if (rows.size() > 1 && other.rows.size() > 1) return getMultiMatch(other);
    if (rows.size() > 1) return other.getMatchFactor(this);
    String brdok = getRow(0).getBrojDok();
    double sim = 0;
    for (int i = 0; i < other.getRowCount(); i++)
      sim += Aus.heuristicCompare(brdok, other.getRow(i).getBrojDok(),
                                  raOptimisticMatch.getIgnore());
    if (sim == 0) sim = 0.01;
    return sim / other.getRowCount();
  }

  private double getMultiMatch(MatchableCollection other) {
    if (rows.size() > other.rows.size()) return other.getMultiMatch(this);
    double sim = 0;
    for (int j = 0; j < other.getRowCount(); j++) {
      String brdok = other.getRow(j).getBrojDok();
      double best = 0, now;
      for (int i = 0; i < getRowCount(); i++)
        if ((now = Aus.heuristicCompare(brdok, getRow(i).getBrojDok())) > best)
          best = now;
      sim += best;
    }
    if (sim == 0) sim = 0.01;
    return sim / other.getRowCount();
  }

  private MatchableCollection() {
    repr = "ne pokriva se";
    tool = null;
  }

  public MatchableCollection(MatchRow mr) {
    rows.add(mr);
    racTip = mr.isRacunSide();
    saldo = mr.isKob() ? mr.getPVSaldo().abs() : mr.getPVSaldo();
    matchFactor = 0;
    repr = tool = null;
//    repr = getStringRepresentation();
//    tool = getToolTipRepresentation();
  }

  public MatchableCollection copyAndExtend(MatchRow mr) {
    MatchableCollection copy = new MatchableCollection(mr);
    copy.saldo = copy.saldo.add(saldo);
    copy.rows.addAll(rows);
//    copy.repr = copy.getStringRepresentation();
//    copy.tool = copy.getToolTipRepresentation();
    return copy;
  }

  public void setOwner(SaldoSlot ss) {
    owner = ss;
  }

  public SaldoSlot getOwner() {
    return owner;
  }

  public boolean checkAdd(MatchRow mr, int size) {
    return racTip == mr.isRacunSide() && rows.size() == size - 1 &&
                   mr.compareByKey(getRow(0)) > 0;
  }
//  mc.getRowCount() == depth - 1 && mc.isRacunTip() == mr.isRacunSide())

  public boolean isRacunTip() {
    return racTip;
  }

  public BigDecimal getMatchSaldo() {
    return saldo;
  }

  public int getRowCount() {
    return rows.size();
  }

  public MatchRow getRow(int row) {
    return (MatchRow) rows.get(row);
  }

  public MatchableCollection getMatch() {
    return match;
  }

  public double getMaxMatchStrength() {
    double str = 0;
    for (int i = 0; i < rows.size(); i++)
      str = Math.max(str, getRow(i).getMatchStrength());
    return str;
  }

  public void setMatch(MatchableCollection other) {
    if (match != null) match.setMatchDirect(null);
    if (other != null && other.match != null) other.match.setMatchDirect(null);
    if (other != null) other.setMatchDirect(this);
    setMatchDirect(other);
  }

  private void setMatchDirect(MatchableCollection other) {
    match = other;
    matchFactor = (match == null ? 0 : getMatchFactor(match));
    for (int i = 0; i < rows.size(); i++)
      getRow(i).setMatchStrength(matchFactor);
  }

  public String getSaldoFormatted() {
    if (raSaldaKonti.isSimple() || raSaldaKonti.isDomVal(getRow(0).getOznval())) 
      return nf.format(saldo.doubleValue());
    return "(" + getRow(0).getOznval() + ") " + nf.format(saldo.doubleValue());
  }

  public String getSF(BigDecimal sal) {
    return nf.format(sal.doubleValue());
  }

  private String getToolTipRepresentation() {
    shared.clear();
    shared.append("<html><body><table border>");
    shared.append("<tr><th>OJ</th><th>Dokument</th><th>Datum</th><th>Saldo</th></tr>");

    for (int i = 0; i < rows.size(); i++) {
      MatchRow row = getRow(i);
      shared.append("<tr><td>").append(row.getCorg());
      shared.append("</td><td>").append(row.getVrdok());
      shared.append(' ').append(row.getBrojDok());
      if (row.isRacunTip() && row.getExtBroj().length() > 0)
        shared.append(" (").append(row.getExtBroj()).append(")");
      shared.append("</td><td>");
      shared.append(raDateUtil.getraDateUtil().dataFormatter(getRow(i).getDatum()));
      shared.append("</td><td>").append(getSF(getRow(i).getPVSaldo())).append("</td></tr>");
    }
    return shared.append("</table></body></html>").toString();
  }

  private String getStringRepresentation() {
    shared.clear();
    if (rows.size() == 1) {
      shared.append(getRow(0).getVrdok()).append(' ');
      shared.append(getRow(0).getBrojDok());
      return shared.toString();
    }
    for (int i = 0; i < rows.size(); i++) {
      shared.append(getRow(i).getVrdok()).append(' ');
      shared.append(getRow(i).getBrojDok()).append(" (");
      shared.append(getSF(getRow(i).getPVSaldo())).append(") + ");
    }
    return shared.chop(3).toString();
  }

  public void findRepresentations() {
    tool = getToolTipRepresentation();
    repr = getStringRepresentation();
  }

  public String getToolTipText() {
    return tool;
  }

  public String toString() {
    return repr != null ? repr : getStringRepresentation();
  }

  public void setMatchable() {
    for (int i = 0; i < rows.size(); i++)
      getRow(i).setMatchable();
  }
}
