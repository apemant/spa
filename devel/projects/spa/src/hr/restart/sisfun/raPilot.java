/****license*****************************************************************
**   file: raPilot.java
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

import hr.restart.baza.KreirDrop;
import hr.restart.baza.Tablice;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraKeyListener;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Assert;
import hr.restart.util.AssertionException;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raFileFilter;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import bsh.EvalError;
import bsh.Interpreter;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/* GRUPIRANJE. Vise grupa medjusobno se odvajaju sa zarezom. Ako se neka grupa
 * sastoji od dva odvojena podatka koja zajedno cine jedno grupiranje, takvi
 * podaci se odvajaju sa znakom |.
 * Grupa se definira tako da se navede ime kolone po kojoj se grupira. Dodatno
 * se mogu navesti i ime kolone iz tablice gdje je ta kolona kljuc (kad se grupira
 * po podatku koji je strani kljuc). Tada se mora navesti i ime gettera iz dM-a
 * koji vraca tablicu stranog kljuca.
 * Primjeri:
 * CORG+NAZIV+getOrgstruktura    - definira grupiranje po CORG-u uz dohvaanje
 * kolone NAZIV iz tablice Orgstruktura (koja se u dM-u dobije preko getOrgstruktura)
 * CORG+NAZIV+getOrgstruktura | CPAR+NAZPAR+getPartneri   - definira *jednu* grupu,
 * ali se ta grupa trigerira za svaku razlicitu kombinaciju CORG i CPAR.
 * CORG+NAZIV+getOrgstruktura, CPAR+NAZPAR+getPartneri   - definira grupu u grupi.
 * Prvo se svi podaci razbiju po org- jedinicama, a onda se svaka od natalih
 * grupa dodatno razbije po partnerima.
 * 
 * MODIFIERI. Vise modifiera odvajaju se zarezom (ovo je univerzalno za sve tagove).
 * Svaki modifier definira se po formatu 
 * <kolona kljuca> = <zamjenska kolona 1> + <zamjenska kolona 2>, .. | <getter iz DM>
 * Za sada je podrzan samo najosnovniji columnsmodifier, gdje je kolona koja se
 * zamjenjuje ujedno i kljuc. Primjer:
 * CPAR = CPAR+NAZPAR | getPartneri  
 */

class AutoComplete {
  public static final int BEGINNING = 1;
  public static final int AFTER_BRACKET = 2;
  public static final int AFTER_DOT = 4;
  public static final int AFTER_COLUMNS = 8;
  public static final int AFTER_TABLES = 16;
  public static final int AFTER_CONDITION = 32;
  public static final int AFTER_FROM = 64;
  public static final int AFTER_SELECT = 128;
  public static final int AFTER_COMMAND = 256;
  public static final int AFTER_COMMAND_COL = 512;
  public static final int AFTER_BRACKET_FUNC = 1024;

  private HashMap table = new HashMap();

  private void fillTable() {
    add("SELECT ", BEGINNING | AFTER_BRACKET);
    add("DELETE FROM ", BEGINNING);
    add("INSERT INTO ", BEGINNING);
    add("UPDATE ", BEGINNING);
    add("* FROM ", AFTER_SELECT);
    add("COUNT(*) FROM ", AFTER_SELECT);
    add("MAX(", AFTER_SELECT | AFTER_COLUMNS);
    add("MIN(", AFTER_SELECT | AFTER_COLUMNS);
    add("SUM(", AFTER_SELECT | AFTER_COLUMNS);
    add("AVG(", AFTER_SELECT | AFTER_COLUMNS);
    add("EXTRACT(", AFTER_SELECT | AFTER_COLUMNS | AFTER_COMMAND);
    add("YEAR FROM ", AFTER_BRACKET_FUNC);
    add("MONTH FROM ", AFTER_BRACKET_FUNC);
    add("DAY FROM ", AFTER_BRACKET_FUNC);
    add("DISTINCT ", AFTER_SELECT);
    add("FROM ", AFTER_COLUMNS);
    add("WHERE ", AFTER_TABLES);
    add("AND ", AFTER_CONDITION);
    add("OR ", AFTER_CONDITION);
    add("UNION ", AFTER_CONDITION);
    add("HAVING ", AFTER_CONDITION);
    add("GROUP BY ", AFTER_CONDITION | AFTER_TABLES);
    add("ORDER BY ", AFTER_CONDITION | AFTER_TABLES);
  }

  public void remove(String word) {
    if (table.containsKey(word))
      table.remove(word);
  }

  public void removeAll(String prefix) {
    String pref = prefix.toLowerCase();
    Iterator i = table.keySet().iterator();
    while (i.hasNext()) {
      String key = (String) i.next();
      if (key.toLowerCase().startsWith(pref)/* && key.length() > prefix.length()*/)
        i.remove();
    }
  }

  public void removeAll(int condition) {
    Iterator i = table.keySet().iterator();
    while (i.hasNext()) {
      String key = (String) i.next();
      if ((((Token) table.get(key)).context & condition) != 0)
        i.remove();
    }
  }

  public void add(String word, int context) {
    if (table.containsKey(word))
      throw new RuntimeException("Duplicate key: " + word);
    table.put(word, new Token(word, context));
  }

  public void addAll(String table, String[] cols) {
    for (int i = 0; i < cols.length; i++)
      add(table + "." + cols[i].toLowerCase(), AFTER_DOT);
  }

  public void addAll(String[] cols) {
    for (int i = 0; i < cols.length; i++)
      add(cols[i].toLowerCase(), AFTER_COMMAND | AFTER_COMMAND_COL |
          AFTER_SELECT | AFTER_COLUMNS | AFTER_BRACKET_FUNC);
  }

  public String get(String prefix, int context) {
    String[] comps = getAll(prefix, context);
    if (comps.length != 1) return "";
    else return comps[0];
  }

  public String[] getAll(String prefix, int context) {
    Token comp;
    ArrayList comps = new ArrayList();
    String pref = prefix.toLowerCase();
    Iterator i = table.values().iterator();
    while (i.hasNext()) {
      comp = (Token) i.next();
      if (comp.word.toLowerCase().startsWith(pref) && (comp.context & context) != 0)
        comps.add(comp.word);
    }
    return (String[]) comps.toArray(new String[comps.size()]);
  }

  public void printAll() {
    Iterator i = table.keySet().iterator();
    while (i.hasNext())
      System.out.print(i.next() + "|");
    System.out.println();
  }

  public void printAllSorted() {
    for (Iterator i = table.values().iterator(); i.hasNext(); ) {
      Token item = (Token) i.next();
      if ((item.context & AFTER_COMMAND_COL) != 0)
        System.out.print(item.word + "|");
    }
    System.out.println();
    System.out.println("----------------");
    for (Iterator i = table.values().iterator(); i.hasNext(); ) {
      Token item = (Token) i.next();
      if ((item.context & AFTER_DOT) != 0)
        System.out.print(item.word + "|");
    }
    System.out.println();
  }

  public AutoComplete() {
    fillTable();
  }
  
  private static class Token {  
    String word;
    int context;
    
    public Token(String word, int context) {
      this.word = word;
      this.context = context;
    }
  }
}

class Tag {
  public static final String CHAR = "S";
  public static final String DATE_FROM = "F";
  public static final String DATE_TO = "T";
  public static final String INT = "I";
  public static final String FLOAT2 = "2";
  public static final String FLOAT3 = "3";

  boolean arg, bsh;

  Variant value;

  String kol;
  String type;
  String getter;
  DataSet getDs;
  String desc;
  String vis;
  String defv;
  int wid;
  public Tag(DataSet ds) {
    desc = ds.getString("OPIS");
    arg = ds.getString("VRSTA").equalsIgnoreCase("A");
    if (arg) {
      getter = ds.getString("GET");
      type = ds.getString("TIP");
      kol = ds.getString("KOLONE");
      vis = ds.getString("VISKOL");
      defv = ds.getString("DEFAULT");
      wid = ds.getInt("WIDTH");
    }
    value = new Variant();
  }

  public boolean isArg() {
    return arg;
  }

  public Variant getValue() {
    return value;
  }

  public Tag(String source) {
    String[] elems = new VarStr(source).split(';');
    arg = elems.length > 1;
    String def = arg ? "" : null;
    desc = elems.length > 0 ? elems[0] : "";
    type = elems.length > 1 ? elems[1] : def;
    getter = elems.length > 2 ? elems[2] : def;
    kol = elems.length > 3 ? elems[3] : def;
    vis = elems.length > 4 ? elems[4] : def;
    defv = elems.length > 5 ? elems[5] : def;
    wid = elems.length > 6 && Aus.isDigit(elems[6]) ? Aus.getNumber(elems[6]) : 0;
    value = new Variant();
  }

  public void toData(DataSet ds) {
    ds.setString("OPIS", desc);
    if (arg) {
      ds.setString("VRSTA", "A");
      ds.setString("GET", getter);
      ds.setString("TIP", type);
      ds.setString("KOLONE", kol);
      ds.setString("VISKOL", vis);
      ds.setString("DEFAULT", defv);
      ds.setInt("WIDTH", wid);
    } else ds.setString("VRSTA", "P");
  }
  
  public boolean isDefault() {
    return !arg && (desc.length() == 0 || desc.equals("$default"));
  }
  
  public String toString() {
    if (!arg) return desc;
    return desc + ';' + type + ';' + getter + ';' + kol + ';' + vis + ';' + defv + ';' + wid;    
  }

  public String queryString() {
    if (type.equals(CHAR))
      return value.getString();
    else if (type.equals(INT))
      return Integer.toString(value.getInt());
    else if (type.equals(FLOAT2) || type.equals(FLOAT3))
      return value.getBigDecimal().toString();
    else if (type.equals(DATE_FROM))
      return "'" + Util.getUtil().getFirstSecondOfDay(value.getTimestamp()) + "'";
    else if (type.equals(DATE_TO))
      return "'" + Util.getUtil().getLastSecondOfDay(value.getTimestamp()) + "'";
    throw new RuntimeException("Nepoznat tip vrijednosti!");
  }

  public String argumentString() {
    if (!arg)
      return value.getString();
    else if (type.equals(CHAR))
      return value.getString();
    else if (type.equals(INT))
      return Integer.toString(value.getInt());
    else if (type.equals(FLOAT2) || type.equals(FLOAT3))
      return value.getBigDecimal().toString();
    else if (type.equals(DATE_FROM) || type.equals(DATE_TO))
      return hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(value.getTimestamp());
    throw new RuntimeException("Nepoznat tip vrijednosti!");
  }
}

class Query {  
  public String command;
  public HashMap tags;

  public Query(String q) {
    this(q, null);
  }

  public Query(String q, HashMap t) {
    command = q;
    tags = t;    
  }  

  public HashMap addDefaults(HashMap others) {
    if (tags == null)
      tags = new HashMap();
    if (tags.get("title") == null)
      tags.put("title", new Tag("$default"));
    if (tags.get("visible") == null)
      tags.put("visible", new Tag("$default"));
    if (tags.get("sums") == null)
      tags.put("sums", new Tag(""));
    if (tags.get("orders") == null)
      tags.put("orders", new Tag(""));
    if (tags.get("groups") == null)
      tags.put("groups", new Tag(""));
    if (tags.get("modifiers") == null)
      tags.put("modifiers", new Tag(""));
    if (others != null)
      tags.putAll(others);
    return tags;
  }  

  public void resolve() {
    ArrayList params = new ArrayList();
    for (Iterator it = tags.entrySet().iterator(); it.hasNext(); ) {
      Tag p = (Tag) ((Map.Entry) it.next()).getValue();
      if (!p.isArg()) params.add(p);
    }
    for (int i = 0; i < params.size(); i++) {
      Tag p = (Tag) params.get(i);
      VarStr desc = new VarStr(p.desc);
      for (Iterator it = tags.entrySet().iterator(); it.hasNext(); ) {
        Map.Entry me = (Map.Entry) it.next();
        Tag a = (Tag) me.getValue();
        if (a.isArg())
          desc.replaceAll("$" + me.getKey(), a.argumentString());
      }
      p.getValue().setString(desc.toString());
    }
  }

  public String out() {
    VarStr ret = new VarStr();
    if (tags != null)
      for (Iterator i = tags.entrySet().iterator(); i.hasNext(); ) {
        Map.Entry e = (Map.Entry) i.next();
        if (!((Tag) e.getValue()).isDefault()) {
          ret.append("<#").append(e.getKey()).append(">");
          ret.append(e.getValue()).append(System.getProperty("line.separator"));
        }
      }
    ret.append(command);
    return ret.toString();
  }
}

public class raPilot extends raFrame {
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  JraScrollPane vp = new JraScrollPane();/*JraScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                     JraScrollPane.HORIZONTAL_SCROLLBAR_NEVER);*/

  LinkedList history = new LinkedList();
  ListIterator hist = history.listIterator();
  JLabel histInfo = new JLabel();
//  int hist;

  int scrollPos;
  static raPilot inst;

  jpScroller jps;
  JPanel jpe = new JPanel();
  JPanel jpd = new JPanel();
  JPanel down = new JPanel(new BorderLayout());
  JPanel header = new JPanel(new BorderLayout());
  JEditorPane query = new JEditorPane() {
     public boolean getScrollableTracksViewportWidth() {
       return true;
     }
  };
  JButton jbTab = new JButton();
  JraCheckBox jcbKey = new JraCheckBox();
  JraCheckBox jcbAuto = new JraCheckBox();
  raNavBar nav = new raNavBar(raNavBar.EMPTY);
  raNavBar info = new raNavBar(raNavBar.EMPTY);

  raDelayWindow process;

  frmTableDataView view = getNewView(true);

  QueryDataSet tab = Tablice.getDataModule().getFilteredDataSet("");

  JFileChooser jf = new JFileChooser();
  
  Interpreter bshInterpreter = new Interpreter();
  
  AutoComplete ac = new AutoComplete();

  VarStr buf = new VarStr();
  HashSet fromTables = new HashSet();
  HashMap tags = null;
  Query currq = null;
  PropsDialog props;
  String dialogTitle;
  Window dialogOwner;
  
  raNavAction copyCurr = null;

  int lastTab;
  boolean skipEvents, busy, chg, offline;

  javax.swing.Timer auto = new javax.swing.Timer(300, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      doCheck();
    }
  });

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      begin(false);
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  FileFilter filterHIS = new raFileFilter("History datoteke (*.his)");
  FileFilter filterSQL = new raFileFilter("Datoteke SQL naredbi (*.sql)");

  public raPilot() {
    try {
      inst = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private static raDelayWindow load;

  public static void executeReport(java.net.URL rep, String title, Window owner) {
    load = raDelayWindow.show(owner, 50).setModal(true).setInterruptable(true);
    if (inst == null)
      startFrame.getStartFrame().showFrame("hr.restart.sisfun.raPilot", 0, "Pilot", false);
    inst.executeOfflineReport(rep, title, owner);
  }
  
  public static void open(boolean paste) {
    if (inst == null)
      startFrame.getStartFrame().showFrame("hr.restart.sisfun.raPilot", 0, "Pilot", false);
    
    raPilot real = inst;
    raPilot np = new raPilot();
    inst = real;
    startFrame.getStartFrame().centerFrame(np, 0, "");
    np.show();
    if (paste) np.query.paste();
    np.updateTitle();
  }
  
  private void executeOfflineReport(java.net.URL rep, String title, Window owner) {
    if (isShowing()) {
      view.hide();
      hide();
    }
    offline = true;    
    dialogTitle = title;
    dialogOwner = owner;
    frmTableDataView old = view;
    Interpreter oldInterpreter = bshInterpreter;
    String oldq = query.getText();
    HashMap oldt = tags;
    bshInterpreter = new Interpreter();
    addDefaultBeanShellCommands();
    view = new frmTableDataView();
    view.setSize(640, 400);
    view.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    if (!load.isInterrupted() && loadCommand(rep) && !load.isInterrupted()) {
      try {
        begin(false).join();
      } catch (InterruptedException e) {
        // nije greska
      }
    }
    bshInterpreter = oldInterpreter;
    view = old;
    query.setText(oldq);
    tags = oldt;
    offline = false;
    if (load != null) {
      load.close();
      load = null;
    }
  }

  private void jbInit() throws Exception {
    addDefaultBeanShellCommands();    

    query.setFont(new JTextArea().getFont());

    Dimension d = new Dimension(72 * query.getFontMetrics(query.getFont()).charWidth(' '),
                                         14 * query.getFontMetrics(query.getFont()).getHeight());

    vp.setViewportView(query);
    vp.getViewport().setPreferredSize(d);
    jbTab.setText("Tablice");
    jcbKey.setText("Imena kolona ");
    jcbKey.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbKey.setSelected(true);
    jcbAuto.setText("Nadopunjavanje ");
    jcbAuto.setHorizontalTextPosition(SwingConstants.LEADING);
    jpd.setLayout(new BoxLayout(jpd, BoxLayout.X_AXIS));
    jpd.setBorder(BorderFactory.createEtchedBorder());
    jpd.add(jbTab);
    jpd.add(new JLabel("       "));
    jpd.add(jcbKey);
    jpd.add(new JLabel("    "));
    jpd.add(jcbAuto);
    jpd.add(new JLabel("     "));
    jpd.add(Box.createHorizontalGlue());
    jpd.add(okp);
    okp.registerOKPanelKeys(this);
    down.add(jpd, BorderLayout.NORTH);
    jps = new jpScroller(d.width, 120) {
      public void needData() {
        scroll();
      }
    };
    jps.setFont("Times New Roman");

    view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    view.jp.installNaturalSelectionTracker();

    props = new PropsDialog(this.getWindow());
    setupNavBars();
    header.add(nav, BorderLayout.WEST);
    header.add(histInfo);
    header.add(info, BorderLayout.EAST);
    
    histInfo.setHorizontalAlignment(JLabel.CENTER);

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(header, BorderLayout.NORTH);
    this.getContentPane().add(vp, BorderLayout.CENTER);
    this.getContentPane().add(down, BorderLayout.SOUTH);
    this.pack();

    fillTableNames();
    auto.setRepeats(false);
    auto.setInitialDelay(300);

    jf.addChoosableFileFilter(filterSQL);
    jf.addChoosableFileFilter(filterHIS);
    jf.setFileFilter(filterSQL);
    jf.setCurrentDirectory(new File("."));

    jbTab.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getTablice();
      }
    });

    query.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_TAB) {
          e.consume();
          try {
            checkAutoComplete(true);
          } catch (Exception ex) {
            // silent
          }
        } else lastTab = -1;
        if (e.getKeyCode() == e.VK_X && e.getModifiers() == e.ALT_MASK)
          okp.jBOK_actionPerformed();
        if (e.getKeyCode() == e.VK_ENTER && e.getModifiers() == e.CTRL_MASK)
          okp.jBOK_actionPerformed();
      }
    });
/*    query.addInputMethodListener(new InputMethodListener() {
      public void caretPositionChanged(InputMethodEvent e) {
        if (jcbAuto.isSelected()) auto.stop();
        lastTab = -1;
      }
      public void inputMethodTextChanged(InputMethodEvent e) {
        lastTab = -1;
        System.out.println("changed");
      }
    }); */
    query.addCaretListener(new CaretListener() {
      public void caretUpdate(CaretEvent e) {
        if (!skipEvents) {
          if (jcbAuto.isSelected() && !chg) auto.stop();
          chg = false;
          lastTab = -1;
        }
      }
    });
    query.getDocument().addDocumentListener(new DocumentListener() {
      public void insertUpdate(DocumentEvent e) {
        if (!skipEvents && jcbAuto.isSelected() && (chg = true)) auto.restart();
      }
      public void removeUpdate(DocumentEvent e) {
        if (!skipEvents && jcbAuto.isSelected() && (chg = true)) auto.restart();
      }
      public void changedUpdate(DocumentEvent e) {
        if (!skipEvents && jcbAuto.isSelected() && (chg = true)) auto.restart();
      }
    });
    jcbKey.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        query.requestFocus();
      }
    });
    jcbAuto.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        query.requestFocus();
      }
    });
    this.addKeyListener(new JraKeyListener());
  }
  
  private void addDefaultBeanShellCommands() {    
    try {
      bshInterpreter.eval(
          "import hr.restart.baza.*;" +
          "import hr.restart.util.*;" +
          "import hr.restart.sisfun.*;" +
          "import hr.restart.util.reports.*;" +
          "import hr.restart.zapod.*;" +
          "import hr.restart.swing.*;" +          
          "import hr.restart.*;" +
          "import com.borland.dx.dataset.*;" +
          "import com.borland.dx.sql.dataset.*;" +
          "import com.borland.jb.util.*;" +
          "import java.math.BigDecimal;"
      );
    } catch (EvalError e) { 
      e.printStackTrace();
    }
  }

  void scroll() {
    ++scrollPos;
    switch (scrollPos) {
      case 1:
        jps.speed(40, 1).align(0).spacing(5).size(16);
        jps.skip(160).bold().append("P I L O T").plain();
        jps.size(12).italic().append(20, "by ab.f").plain();
        break;
      case 2:
        jps.append(60, "Copyright (c) 2002 REST-ART d.o.o.");
        jps.append(25, raImages.getImageIcon(raImages.IMGRAICON).getImage());
        jps.skip(5).append("All rights reserved.").skip(25).pause(2000);
        break;
      case 3:
        jps.align(20).skip(60).append("LIMITTED WARRANTY:").skip(20);
        jps.append("The author hereby disclaims all warranties, express or implied,");
        jps.append("including, but not limitted to, implied warranties of merchantability");
        jps.append("or fitness for a particular purpose. The author shall not be liable");
        jps.append("for any direct, special, incidental or consequential damages arising");
        jps.append("from the use of this product, including, but not limitted to, loss of");
        jps.append("data, profit, sanity, wife, relatives or pets. Use this product at");
        jps.append("your own risk.").skip(20).pause(5000);
        scrollPos = 0;
        break;
    }
    this.repaint();
  }

  void doCheck() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          checkAutoComplete(false);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private int getFromIndex(String queryStr) {
    VarStr lq = buf.clear().append(queryStr).replaceAll('\n', ' ');
    int fr = 0, beg = 0, origLength = lq.length();
    while (fr == 0) {
      fr = lq.indexOfIgnoreCase("from ", beg);
      if (fr < 0) fr = lq.indexOfIgnoreCase(" from", beg);
      if (fr >= 0) {
        lq.setLength(fr);
        if (lq.countOccurences('(') > lq.countOccurences(')')) {
          lq.setLength(origLength);
          beg = fr + 4;
          fr = 0;
        }
      }
    }
    return fr;
  }

  /*private void printAll() {
    AutoComplete.printAll();
  }*/

//  private int getWordBeginning(String text, int from) {
//    while (--from >= 0 && Character.isWhitespace(text.charAt(from)));
//    while (--from >= 0 && (Character.isLetterOrDigit(text.charAt(from)) || text.charAt(from) == '.'));
//    return from + 1;
//  }

  private int getWordBeginning(int from) throws BadLocationException {
    while (--from >= 0 && isWhite(from));
    while (--from >= 0 && isName(from));
    return from + 1;
  }

  private boolean isName(int pos) throws BadLocationException {
    return Character.isLetterOrDigit(query.getText(pos, 1).charAt(0)) ||
           query.getText(pos, 1).equals(".") ||
           query.getText(pos, 1).equals("_");
  }

  private boolean isWhite(int pos) throws BadLocationException {
    return Character.isWhitespace(query.getText(pos, 1).charAt(0));
  }

  void checkAutoComplete(boolean any) throws BadLocationException {
    int context;
    int pos = query.getCaretPosition(), p, realp = pos;
    if (any && lastTab > 0) pos = lastTab;
    if (any) lastTab = pos;
//    String text = query.getText().replace('\n', ' ').toLowerCase();
//    System.out.println("|"+text+"|" + pos + "=|" + text.charAt(pos - 1) + "|" + text.length());
//    if (pos == 0 || Character.isWhitespace(text.charAt(pos - 1))) return;
    if (pos == 0 || isWhite(pos - 1)) return;
    int beg = getWordBeginning(pos);
    int last = getWordBeginning(beg);
//    System.out.println(beg + "  " + last);
    if (query.getText(beg, pos - beg).indexOf(".") > 0) context = AutoComplete.AFTER_DOT;
    else if (last < 0) context = AutoComplete.BEGINNING;
    else {
      String lastw = query.getText(last, beg - last).trim().toLowerCase();

      if (lastw.equals("select"))
        context = AutoComplete.AFTER_SELECT;
      else if (lastw.equals("from") || lastw.equals("into") || lastw.equals("update"))
        context = AutoComplete.AFTER_FROM;
      else if (lastw.equals("where") || lastw.equals("from") ||
          lastw.equals("and") || lastw.equals("or") ||
          lastw.endsWith("=") || lastw.endsWith(">") || lastw.endsWith("<"))
        context = AutoComplete.AFTER_COMMAND;
      else if (lastw.equals("(") || lastw.equals("union"))
        context = AutoComplete.AFTER_BRACKET;
      else if (lastw.endsWith("("))
        context = AutoComplete.AFTER_BRACKET_FUNC;
      else if ((p = query.getText().toLowerCase().indexOf("where")) > 0 && p < pos)
        context = AutoComplete.AFTER_CONDITION;
      else if ((p = getFromIndex(query.getText())) > 0 && p < pos)
        context = AutoComplete.AFTER_TABLES;
      else context = AutoComplete.AFTER_COLUMNS;
    }
//    System.out.println(context);
    String[] tnames = findTables(query.getText(), false);
    if (tnames == null)
      tnames = new String[0];
/*    System.out.println(tnames);
    if (tnames != null) {
      System.out.print(tnames.length + " = ");
      for (int i = 0; i < tnames.length; i++)
        System.out.print(tnames[i] + "|");
      System.out.println();
    }
    AutoComplete.printAll(); */
//    AutoComplete.printAll();

    boolean diff = (tnames.length != fromTables.size());
    if (!diff && tnames.length > 0)
      for (int i = 0; i < tnames.length; i++)
        if (!fromTables.contains(tnames[i].substring(tnames[i].lastIndexOf(".") + 1).toLowerCase()))
          diff = true;
    if (diff) {
//      System.out.println("different");

      if (fromTables.size() == 1)
        ac.removeAll(AutoComplete.AFTER_COMMAND_COL);
      if (fromTables.size() > 0 ) {
        Iterator i = fromTables.iterator();
        while (i.hasNext())
          ac.removeAll((String) i.next() + ".");
      }
      fromTables.clear();
      if (tnames.length == 1)
        ac.addAll(getTable(tnames[0]).getColumnNames(getTable(tnames[0]).getColumnCount()));
      for (int i = 0; i < tnames.length; i++) {
        String tname = tnames[i].substring(tnames[i].lastIndexOf(".") + 1).toLowerCase();
        if (!fromTables.contains(tname)) {
          fromTables.add(tname);
          ac.addAll(tname, getTable(tnames[i]).getColumnNames(getTable(tnames[i]).getColumnCount()));
          if (tnames.length != 1) {
//            System.out.println("added " + tname + ".");
            ac.add(tname + ".", AutoComplete.AFTER_COMMAND |
                             AutoComplete.AFTER_SELECT | AutoComplete.AFTER_COLUMNS);
          }
        }
      }
//      AutoComplete.printAllSorted();
    }
    skipEvents = true;
    if (beg < pos) {
//      System.out.println(text.substring(beg, pos));
      if (!any) {
        String comp = ac.get(query.getText(beg, pos - beg), context);
        if (!comp.equals("")) {
          int end = pos - 1;
          while (++end < query.getDocument().getLength() && isName(end));
          query.setCaretPosition(beg);
          query.moveCaretPosition(end);
          query.replaceSelection(comp);
        }
      } else {
        String[] possible = ac.getAll(query.getText(beg, pos - beg), context);
        if (possible != null && possible.length > 0) {
          int end = realp;
//          while (++end < query.getDocument().getLength() && isName(end));
          String olds = query.getText(beg, end - beg).trim().toLowerCase();
          String news = possible[0];
          for (int i = 0; i < possible.length; i++)
            if (possible[i].trim().equalsIgnoreCase(olds))
              news = possible[(i + 1) % possible.length];
          query.setCaretPosition(beg);
          query.moveCaretPosition(end);
          query.replaceSelection(news);
        }
      }
    }
    skipEvents = false;
  }

  void inflateAsterisk() throws BadLocationException {
    VarStr cols = new VarStr(60);
    String sql = query.getText().replace('\n', ' ').trim();
    if (sql.toLowerCase().indexOf("select") == -1) {
      JOptionPane.showMessageDialog(query, "Neispravan query!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    String[] tnames = findTables(sql, true);
    if (tnames == null) return;
//    System.out.println(tnames);
//    System.out.println(tnames[tnames.length - 1]);
    if (tnames.length < 1) {
      JOptionPane.showMessageDialog(query, "Tablica nije definirana!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    int pos = 0;
    while (++pos < query.getDocument().getLength()) {
      if (query.getText(pos, 1).equals("*")) {
        if (isWhite(pos - 1)) {
          cols.clear();
          for (int i = 0; i < tnames.length; i++) {
            String tname = tnames[i].substring(tnames[i].lastIndexOf(".") + 1);
            String pref = tnames.length == 1 ? "" : tname + ".";
            String[] cnames = getTable(tnames[i]).getColumnNames(getTable(tnames[i]).getColumnCount());
            for (int j = 0; j < cnames.length; j++)
              cols.append(pref).append(cnames[j].toLowerCase()).append(", ");
          }
          cols.chop(2);
          query.setCaretPosition(pos);
          query.moveCaretPosition(pos + 1);
          query.replaceSelection(cols.toString());
          pos += cols.length() - 1;
        } else if (query.getText(pos - 1, 1).equals(".")) {
          int beg = getWordBeginning(pos);
          String tname = query.getText(beg, pos - beg - 1).trim();
          if (tname.equals("")) {
            query.setCaretPosition(beg);
            JOptionPane.showMessageDialog(query, "Neispravno definirana tablica!", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
          }
          for (int i = 0; i <= tnames.length; i++) {
            if (i == tnames.length) {
              query.setCaretPosition(beg);
              JOptionPane.showMessageDialog(query, "Tablica "+tname+" nije u popisu!", "Greška", JOptionPane.ERROR_MESSAGE);
              return;
            }
            if (tnames[i].substring(tnames[i].lastIndexOf(".") + 1).equalsIgnoreCase(tname)) {
              cols.clear();
              String[] cnames = getTable(tnames[i]).getColumnNames(getTable(tnames[i]).getColumnCount());
              for (int j = 0; j < cnames.length; j++)
                cols.append(tname).append(".").append(cnames[j].toLowerCase()).append(", ");
              cols.chop(2);
              query.setCaretPosition(beg);
              query.moveCaretPosition(pos + 1);
              query.replaceSelection(cols.toString());
              pos += cols.length() - (pos - beg);
              break;
            }
          }
        } else {
          query.setCaretPosition(pos);
          JOptionPane.showMessageDialog(query, "Neispravan položaj asteriska!", "Greška", JOptionPane.ERROR_MESSAGE);
          return;
        }
      }
    }
  }

  void fillTableNames() {
    tab.open();
    for (tab.first(); tab.inBounds(); tab.next())
      ac.add(tab.getString("IMETAB"), AutoComplete.AFTER_FROM | AutoComplete.AFTER_TABLES);
  }

  private KreirDrop getModule(String className) {
    KreirDrop t = KreirDrop.getModule(className);
    if (t == null)
      try {
        t = (KreirDrop) Class.forName(className).newInstance();
      } catch (Exception e) {
        // silent
      }
    return t;
  }

  private QueryDataSet getTable(String className) {
    KreirDrop k = getModule(className);
    if (k == null) return null;
    return k.getQueryDataSet();
  }

  void naturalJoin() {
    VarStr where = new VarStr(60);
    String sql = query.getText().replace('\n', ' ').trim();
    String[] tables = findTables(sql, true);
    QueryDataSet t1, t2;
    Column other;
    if (tables == null) return;
    if (tables.length < 2) {
      JOptionPane.showMessageDialog(query, "Nedovoljno tablica za natural-join!", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    where.append("\nWHERE ");
    for (int first = 0; first < tables.length; first++) {
      if (null == (t1 = getTable(tables[first]))) continue;
      for (int second = 0; second < tables.length; second++) if (second != first) {
        if (null == (t2 = getTable(tables[second]))) continue;
        for (int col = 0; col < t1.getColumnCount(); col++)
          if (null != (other = t2.hasColumn(t1.getColumn(col).getColumnName()))
             && other.isRowId() && !(t1.getColumn(col).isRowId() && first > second)) {
//            System.out.println(t1.getColumn(col));
//            System.out.println(t1.getColumn(col).getTableName());
            where.append(t1.getColumn(col).getTableName().toLowerCase());
            where.append(".").append(other.getColumnName().toLowerCase());
            where.append(" = ").append(other.getTableName().toLowerCase());
            where.append(".").append(other.getColumnName().toLowerCase()).append("\nAND ");
          }
      }
    }
    where.chop(4);
    String text;
    int pos;
    sql = query.getText().trim();
    if ((pos = sql.toLowerCase().indexOf("where")) >= 0)
      text = sql.substring(0, pos).trim().concat(where.toString());
    else text = sql.concat(where.toString());
    query.setText(text);
    query.setCaretPosition(query.getDocument().getLength());
    query.requestFocus();
  }

  private String[] findColsListed(String sql) {
    int last = getFromIndex(sql), i = 0;
    String list = sql.substring(sql.toLowerCase().indexOf("select") + 6,
                                last > 0 ? last : sql.length());
//    System.out.println(list);
    StringTokenizer t = new StringTokenizer(list, ",");
    String[] result = new String[t.countTokens()];
//    System.out.println(t.countTokens());
    while (t.hasMoreTokens()) {
      result[i++] = t.nextToken().toLowerCase().trim();
//      String tname = t.nextToken().toLowerCase().trim();
//      if ((p = tname.indexOf(" as")) > 0 || (p = tname.indexOf("as ")) > 0)
//        tname = tname.substring(p + 3).trim();
//      result[i++] = tname;
    }
//    System.out.println(Util.getUtil().arrayString(result));
    return result;
  }

  private void findColumnTable(String[] colsListed, Column col, KreirDrop[] tables) {
    int p;
    for (int i = 0; i < colsListed.length; i++)
      if ((p = colsListed[i].indexOf(" as")) > 0 ||
          (p = colsListed[i].indexOf("as ")) > 0) {
        String cname = colsListed[i].substring(p + 3).trim();
        if (col.getColumnName().equalsIgnoreCase(cname))
          for (int t = 0; t < tables.length; t++)
            if (tables[t].getQueryDataSet().hasColumn(cname) != null) {
              col.setTableName(tables[t].Naziv);
              System.out.println(tables[t].Naziv);
              return;
            }
      }
/*      if ((opened = colsListed[i].indexOf('(')) >= 0 &&
          (closed = colsListed[i].indexOf(')')) > 0) {
        StringTokenizer st = new StringTokenizer(colsListed[i].substring(opened + 1, closed), ".");
        String tname = st.countTokens() > 1 ? st.nextToken() : null;
        String cname = st.nextToken();
        if (cname != null && cname.equalsIgnoreCase(col.getColumnName()))
          if (tname == null) {
            for (int t = 0; t < tables.length; t++)
              if (tables[t].getQueryDataSet().hasColumn(col.getColumnName()) != null)
                col.setTableName(tables[t].Naziv);
          } else
            for (int t = 0; t < tables.length; t++)
              if (tables[t].Naziv.equalsIgnoreCase(tname))
                col.setTableName(tables[t].Naziv);
        if (col.getTableName() != null) return;
      }*/
  }

  private boolean colNotListed(String[] colsListed, Column col) {
//    if (!jcbKey.isSelected()) return false;
    int p;
    if (col.getTableName() == null) return false;
    String tname = col.getTableName().toLowerCase();
    String cname = col.getColumnName().toLowerCase();
//    System.out.println(tname + " " + cname);
    for (int i = 0; i < colsListed.length; i++) {
      if (colsListed[i].indexOf("distinct") >= 0 && colsListed[i].indexOf(cname) >= 0) return false;
      if (colsListed[i].equals("*")) return false;
      if (colsListed[i].equals(tname + ".*")) return false;
      if (colsListed[i].equals(tname + "." + cname)) return false;
      if (colsListed[i].equals(cname)) return false;
      if ((p = colsListed[i].indexOf(" as")) > 0 || (p = colsListed[i].indexOf("as ")) > 0)
        if (colsListed[i].substring(p + 3).trim().equals(cname)) return false;
    }
    return true;
  }

  private String[] findPlainTables(String sql) {
    int last = sql.toLowerCase().indexOf("where"), i = 0;
    int fi = getFromIndex(sql);
    if (fi <= 0) return null;
    String list = sql.substring(getFromIndex(sql) + 5,
                      last > 0 ? last : sql.length());

    StringTokenizer t = new StringTokenizer(list, ",");
    String[] result = new String[t.countTokens()];
    while (t.hasMoreTokens()) {
      StringTokenizer token = new StringTokenizer(t.nextToken().trim());
      result[i++] = token.nextToken().trim();
      if (token.countTokens() > 1) break;
    }
    if (i == result.length) return result;
    
    String[] res = new String[i];
    System.arraycopy(result, 0, res, 0, i);
    return res;    
  }

  private String[] findTables(String sql, boolean show) {
    int last = sql.toLowerCase().indexOf("where"), i = 0;

    if (getFromIndex(sql) <= 0) {
      if (show) {
        if (process != null) process.close();
        JOptionPane.showMessageDialog(query, "Neispravan query!", "Greška", JOptionPane.ERROR_MESSAGE);
      }
      return null;
    }
    String list = sql.substring(getFromIndex(sql) + 5,
                                last > 0 ? last : sql.length());
    StringTokenizer t = new StringTokenizer(list, ",");
    String[] result = new String[t.countTokens()];
    while (t.hasMoreTokens()) {
      StringTokenizer token = new StringTokenizer(t.nextToken().trim());
      String tname = token.nextToken().trim();
      if (tname.length() > 0) {
        if (!ld.raLocate(tab, "IMETAB", tname, Locate.CASE_INSENSITIVE)) {
          if (show) {
            if (process != null) process.close();
            JOptionPane.showMessageDialog(query, "Nepoznata tablica: " + tname, "Greška", JOptionPane.ERROR_MESSAGE);
            return null;
          }
        } else result[i++] = tab.getString("KLASATAB");
      }
      if (token.countTokens() > 1) break;
    }
//    System.out.println(Util.getUtil().arrayString(result));
    if (i == result.length) return result;
    
    String[] res = new String[i];
    System.arraycopy(result, 0, res, 0, i);
    return res;
  }

  void asynchronousOpen(DataSet ds) {
    try {
      long timer = System.currentTimeMillis();
      ds.open();
      timer = System.currentTimeMillis() - timer;
      System.out.println("Opened dataset in "+timer+" ms");
    } catch (Exception e) {
      process.close();
      JOptionPane.showMessageDialog(query, new raMultiLineMessage(
          "Query nije uspio! Greška:\n\n" + e.getLocalizedMessage(),
          SwingConstants.LEADING), "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  int[] getVisibleCols() {
    int[] cols = new int[0];
    if (tags != null && tags.get("visible") != null) {
      Tag t = (Tag) tags.get("visible");
      if (!t.desc.equals("$default")) {
//        System.out.println();
        String[] vis = new VarStr(t.desc).splitTrimmed(",");
        cols = new int[vis.length];
//        System.out.println(VarStr.join(vis,'|'));
        for (int i = 0; i < vis.length; i++) {
          if (!Aus.isNumber(vis[i]) || (cols[i] = Integer.parseInt(vis[i])) < 0) {
            process.close();
            JOptionPane.showMessageDialog(this.getWindow(), "Greška u popisu vidljivih kolona!",
              "Greška", JOptionPane.ERROR_MESSAGE);
            return null;
          }
        }
      }
    }
    return cols;
  }

  int CHAR_WIDTH = 9;
  
  private void parseQuery(String sql, boolean detach) {
//    sysoutTEST sys = new sysoutTEST(false);
    
    int totalWidth = 0;
    QueryDataSet ds;
/*    if (!detach && view.isShowing()) view.hide();
    try {
      if (!checkLimits(sql)) return;
      vl.execSQL(sql);
      ds = vl.RezSet;
      System.out.println("Opening dataset...");
      long tim = System.currentTimeMillis();
      ds.open();
      tim = System.currentTimeMillis() - tim;
      System.out.println("Opened " + ds.getTableName() + " in " + tim + " miliseconds.");
    } catch (Exception e) {
      process.close();
      JOptionPane.showMessageDialog(query, new raMultiLineMessage(
          "Query nije uspio! Greška:\n\n" + e.getLocalizedMessage(),
          SwingConstants.LEADING), "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    */
//    sys.prn(ds);
    /*if (!checkLimits(sql)) return;*/
    vl.execSQL(sql);
    System.out.println(sql);
    ds = vl.RezSet;
    Thread open = new Thread() {
      public void run() {
        asynchronousOpen(vl.getDataAndClear());
      }
    };
    open.start();
    process.setMessage("Dohvat podataka...", false, 10);
    process.setProcessingThreadCurrent();
    try {
      open.join();
    } catch (InterruptedException e) {
      ds.cancelLoading();
      return;
    } finally {
      vl.getDataAndClear();
    }
    if (!ds.isOpen()) return;
    process.setProcessingThread(null);
    process.setInterruptable(true);
    process.setMessage("Punjenje tabli\u010Dnog prikaza...", false, 50);

    String[] colsListed = findColsListed(sql);
    String[] tnames = findTables(sql, false);
    if (tnames == null) return;
    KreirDrop[] tables = new KreirDrop[tnames.length];
    for (int i = 0; i < tnames.length; i++) {
      tables[i] = getModule(tnames[i]);
      if (tables[i] == null) {
        process.close();
        JOptionPane.showMessageDialog(query, "Greška kod tablice: " + tnames[i], "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
    }

    if (process.isInterrupted()) return;

    if (detach)
      detach(true);

    view.clearColumns();

//    System.out.println(hr.restart.util.Util.getUtil().arrayString(colsListed));
    int[] cols = getVisibleCols();
    if (cols == null) return;

    if (process.isInterrupted()) return;

    int numvis = 0;
    HashMap captions = new HashMap();
    for (int i = 0; i < ds.getColumnCount(); i++) {
      QueryDataSet t = null;
      Column col = ds.getColumn(i);
      Column tcol = null;
      if (col.getTableName() == null)
        findColumnTable(colsListed, col, tables);
      for (int j = 0; j < tables.length; j++)
        if (col.getTableName() != null &&
            tables[j].Naziv.equalsIgnoreCase(col.getTableName()))
          t = tables[j].getQueryDataSet();
      if (t != null) {
        String tname = col.getColumnName();
        tcol = t.hasColumn(tname);
        if (tcol == null && Character.isDigit(tname.charAt(tname.length() - 1))) {
          int numDuplicates = Integer.parseInt(tname.substring(tname.length() - 1, tname.length()));
          tname = tname.substring(0, tname.length() - 1);
          for (int j = 0; j < tables.length; j++)
            if (tables[j].getQueryDataSet() != t &&
                tables[j].getQueryDataSet().hasColumn(tname) != null)
              --numDuplicates;
          if (numDuplicates == 0)
            tcol = t.hasColumn(tname);
        }
        if (tcol == null && !colNotListed(colsListed, col)) tcol = col;
      } else /*if (col.getTableName() == null ||
                 col.getTableName().toUpperCase().startsWith("RDB$"))*/
        tcol = col;
      if (tcol == null || colNotListed(colsListed, tcol)) {
        col.setVisible(TriStateProperty.FALSE);
      } else {
        ++numvis;

        view.addColumn(tcol);

        col.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
        String caption = jcbKey.isSelected() && !offline ? tcol.getColumnName() : tcol.getCaption();
        if (captions.containsKey(caption)) {
          int appendNum = ((Integer) captions.get(caption)).intValue() + 1;
          captions.put(caption, new Integer(appendNum));
          caption = caption + appendNum;
        } else captions.put(caption, new Integer(0));
        col.setCaption(caption);
//        System.out.print(caption + ": " + tcol.getWidth()+ " | ");
        col.setDefault(tcol.getDefault());
        col.setDisplayMask(tcol.getDisplayMask());
//        col.setEditMask(tcol.getEditMask());
        col.setWidth(tcol.getWidth());
        col.setRowId(tcol.isRowId());
        if (cols.length == 0)
          totalWidth += col.getWidth();
        else {
          for (int v = 0; v < cols.length; v++)
            if (cols[v] == numvis - 1) {
              totalWidth += col.getWidth();
              break;
            }
        }
      }
    }
//    ds.close();
//    vl.execSQL(sql);
//    ds = vl.RezSet;
//    ds.setColumns((Column[]) columns.toArray(new Column[columns.size()]));
//    ds.open();

    if (cols.length == 0) {
      cols = new int[numvis];
      for (int i = 0; i < numvis; i++) cols[i] = i;
    }
    ds.setResolver(dm.getQresolver());
    if (process.isInterrupted()) return;
  
    view.setDataSet(ds);
    if (process.isInterrupted()) return;
    view.setVisibleCols(cols);

    if (!addSums()) return;
    if (!addGroups()) return;
    if (!addOrders()) return;
    ((raExtendedTable) view.jp.getMpTable()).dumpAll();
    ((raExtendedTable) view.jp.getMpTable()).createSortDescriptor();

    String title;
    if (tnames.length == 0) {
      String[] ptn = findPlainTables(sql);
      title = "Tablic" + (ptn.length > 1 ? "e " : "a ");
      for (int i = 0; i < ptn.length; i++)
        title = title + (i == 0 ? " " : ", ") + ptn[i];
      if (ptn.length == 0) title = "Tablica nepoznata";
    } else {
      title = "Tablic" + (tnames.length > 1 ? "e " : "a ");
      for (int i = 0; i < tnames.length; i++)
        title = title + (i == 0 ? " " : ", ") + tnames[i].substring(tnames[i].lastIndexOf(".") + 1);
    }
    if (tags != null && tags.get("title") != null) {
      Tag t = (Tag) tags.get("title");
      if (!t.desc.equals("$default"))
        title = t.getValue().getString();
    }
    view.setTitle(title);

    if (view.getSize().width > totalWidth * CHAR_WIDTH)
      view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    if (process.isInterrupted()) return;
    process.setInterruptable(false);
//    view.jp.fireTableDataChanged();

    process.close();
    view.show();
//    if (sums != null) view.setSums(sums);
//    view.jp.fireTableDataChanged();

    if (!offline)
      insertHistory();
//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//
//
//        System.out.println("Resizing");
//        System.out.println(view.jp.getSize());
//        if (view.jp.getMpTable().getSize().width < view.jp.getSize().width)
//          view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
//      }
//    });
  }
  
  void analyzeAndShowResults(StorageDataSet ds) {
    if (process.isInterrupted()) {
      process.close();
      return;
    }
    if (!ds.isOpen()) {
      process.setMessage("Dohvat podataka...", false, 50);
      ds.open();
      if (process.isInterrupted()) {
        process.close();
        return;
      }
    }
    process.setMessage("Punjenje tabliènog prikaza...", false, 50);
    
    detach((ds instanceof QueryDataSet) && !offline);

    view.clearColumns();
    int[] cols = getVisibleCols();
    if (cols == null) return;
   
    int numVis = 0;
    int totalWidth = 0;
    HashMap captions = new HashMap();
    for (int i = 0; i < ds.getColumnCount(); i++) {    
      Column col = ds.getColumn(i);
      if (col.getVisible() != TriStateProperty.FALSE) {
        ++numVis;
        view.addColumn(col);
        String caption = jcbKey.isSelected() && !offline ? col.getColumnName() : col.getCaption();
        if (captions.containsKey(caption)) {
          int appendNum = ((Integer) captions.get(caption)).intValue() + 1;
          captions.put(caption, new Integer(appendNum));
          caption = caption + appendNum;
        } else captions.put(caption, new Integer(0));
        col.setCaption(caption);
        if (cols.length == 0) totalWidth += col.getWidth();
        else {
          for (int v = 0; v < cols.length; v++)
            if (cols[v] == numVis - 1) {
              totalWidth += col.getWidth();
              break;
            }
        }
      }      
    }
    
    if (cols.length == 0) {
      cols = new int[numVis];
      for (int i = 0; i < numVis; i++) cols[i] = i;
    }
    if ((ds instanceof QueryDataSet) && !offline)
      ds.setResolver(dm.getQresolver());    
    if (process.isInterrupted()) return;
  
    view.setDataSet(ds);
    if (process.isInterrupted()) return;
    view.setVisibleCols(cols);

    if (!addSums()) return;
    if (!addGroups()) return;
    if (!addOrders()) return;
    ((raExtendedTable) view.jp.getMpTable()).dumpAll();
    ((raExtendedTable) view.jp.getMpTable()).createSortDescriptor();

    String title = "BeanShell DataSet result";    
    if (tags != null && tags.get("title") != null) {
      Tag t = (Tag) tags.get("title");
      if (!t.desc.equals("$default"))
        title = t.getValue().getString();
    }
    view.setTitle(title);

    if (view.getSize().width > totalWidth * CHAR_WIDTH)
      view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    if (process.isInterrupted()) return;
    process.setInterruptable(false);
//    view.jp.fireTableDataChanged();

    view.show();
//    if (sums != null) view.setSums(sums);
//    view.jp.fireTableDataChanged();
    process.close();
    if (!offline)
      insertHistory();
  }

  private String[] getTagParts(String name) {
    if (tags != null && tags.get(name) != null) {
      Tag t = (Tag) tags.get(name);
      String[] parts = new VarStr(t.getValue().getString()).splitTrimmed(',');
      if (parts.length > 1 || parts.length == 1 && parts[0].trim().length() > 0)
        return parts;
    }
    return null;
  }

  private boolean addSums() {
    String[] sums = getTagParts("sums");
    if (sums == null) return true;
    for (int i = 0; i < sums.length; i++) {
      if (!view.checkColumn(sums[i])) {
        process.close();
        JOptionPane.showMessageDialog(this.getWindow(), "Kolona "+sums[i]+" ne postoji "+
                                      "ili se ne može zbrajati!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    view.setSums(sums);
    return true;
  }

  private String checkColumn(String column) {
    checkColumn(view.jp.getStorageDataSet(), column);
    return column;
  }

  private void checkColumn(DataSet ds, String column) {
    Column col = ds.hasColumn(column);
    Assert.notNull(col, column);
    Assert.is(col.getVisible() != com.borland.jb.util.TriStateProperty.FALSE, column);
  }

  private boolean addMultiColumn(String all, VarStr gr, boolean asc, boolean newGroup) {
    int f = gr.indexOf('+'), l = gr.lastIndexOf('+');
    String getter = gr.from(l + 1);
    if (f == l) {
      process.close();
      JOptionPane.showMessageDialog(this.getWindow(), "Nepotreban getter "+getter+
              "() u grupi:\n"+all, "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    DataSet ds = null;
    if (tags.containsKey(getter) && ((Tag) tags.get(getter)).getDs != null)
      ds = ((Tag) tags.get(getter)).getDs;
    if (ds == null)
      try {
        java.lang.reflect.Method m = dM.class.getMethod(getter, null);
        ds = (DataSet) m.invoke(dM.getDataModule(), null);
      } catch (Exception e) {
        process.close();
        JOptionPane.showMessageDialog(this.getWindow(), "Pogrešan getter "+getter+
          "() u grupi:\n"+all, "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    String key = gr.left(f);
    String[] descs = gr.truncate(l).leftChop(f + 1).splitTrimmed('+');
    try {
      checkColumn(ds, key);
      for (int i = 0; i < descs.length; i++)
        checkColumn(ds, descs[i]);
    } catch (AssertionException e) {
      process.close();
      JOptionPane.showMessageDialog(this.getWindow(), "Nepostoje\u0107a kolona '"+e.getMessage()+
         "' u DataSet-u "+getter+"(), u grupi:\n"+all, "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    raExtendedTable t = (raExtendedTable) view.jp.getMpTable();
    t.addToGroup(checkColumn(key), asc, descs, ds, newGroup);
    return true;
  }

  private boolean addGroups() {
    String[] groups = getTagParts("groups");
    if (groups == null) return true;
    raExtendedTable t = (raExtendedTable) view.jp.getMpTable();
    for (int i = 0; i < groups.length; i++) {
      try {
        boolean desc = false;
        String[] cols = new VarStr(groups[i]).splitTrimmed('|');
        System.out.println(VarStr.join(cols, '|'));
        for (int g = 0; g < cols.length; g++) {
          VarStr gr = new VarStr(cols[g]);
          if (g == 0 && (desc = (gr.charAt(0) == '-'))) gr.leftChop(1);
          if (gr.indexOf('+') < 0)
            t.addToGroup(checkColumn(gr.toString()), !desc, g == 0);
          else if (!addMultiColumn(groups[i], gr, !desc, g == 0)) return false;
        }
      } catch (AssertionException e) {
        process.close();
        JOptionPane.showMessageDialog(this.getWindow(), "Nepostoje\u0107a kolona '"+e.getMessage()+
              "' u grupi:\n"+groups[i], "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      } catch (Exception e) {
        e.printStackTrace();
        process.close();
        JOptionPane.showMessageDialog(this.getWindow(), "Pogrešno zadana grupa:\n"
                    +groups[i], "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  }

  private boolean addOrders() {
    String[] orders = getTagParts("orders");
    if (orders == null) return true;
    raExtendedTable t = (raExtendedTable) view.jp.getMpTable();
    for (int i = 0; i < orders.length; i++) {
      boolean desc = orders[i].startsWith("-");
      String col = orders[i].substring(desc ? 1 : 0);
      if (t.inGroup(col)) {
        process.close();
        JOptionPane.showMessageDialog(this.getWindow(), "Kolona "+col+" je ve\u0107 poredana (u grupi)!",
           "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      try {
        t.addSort(checkColumn(col), !desc);
      } catch (AssertionException e) {
        process.close();
        JOptionPane.showMessageDialog(this.getWindow(), "Nepostoje\u0107a kolona '"+e.getMessage()+
           "' za poredak!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  }

  void getTablice() {
    String[] result = lookupData.getlookupData().lookUp(this.getWindow(), tab,
        new String[] {"CTAB"}, new String[] {""},
        new int[] {1,2,3});
    if (result != null && result[0] != null && !result[0].equals("") &&
        ld.raLocate(tab, new String[] {"CTAB"}, result)) {
      KreirDrop t = KreirDrop.getModule(tab.getString("KLASATAB"));
      if (t == null)
        try {
          t = (KreirDrop) Class.forName(tab.getString("KLASATAB")).newInstance();
        } catch (Exception e) {
          // silent
        }
      if (t != null) {
        VarStr s = new VarStr(160);
        s.append("SELECT ");
        for (int i = 0; i < t.getQueryDataSet().getColumnCount(); i++)
          s.append(t.getColumns()[i].getColumnName().toLowerCase()).append(i % 5 == 4 ? ",\n" : ", ");
        s.chop(2);
        s.append("\nFROM ").append(t.Naziv);
        query.setText(s.toString());
        query.setCaretPosition(s.length());
        query.requestFocus();
        t.define();
      }
    }
  }
  
  boolean isSqlCommand(String text) {
    int len = text.length();
    String q = text.substring(0, len < 20 ? len : 20).trim().toUpperCase();
    return q.startsWith("SELECT") || q.startsWith("UPDATE") || 
           q.startsWith("INSERT") || q.startsWith("DELETE") ||
           q.startsWith("CREATE") || q.startsWith("ALTER") ||
           q.startsWith("DROP");    
  }

  private int getAffectRowCount(String sql) {
    vl.execSQL("SELECT COUNT(*) AS NUM "+sql.substring(6));
    vl.RezSet.open();
    return vl.getSetCount(vl.getDataAndClear(), 0);
  }

  private boolean confirmDelete(String sql) {
    raDelayWindow temp = raDelayWindow.show(null, "Provjera",
        "Provjera naredbe...", 250).setModal(true);
    try {
      int rows = getAffectRowCount(sql);
      temp.close();
      int response = JOptionPane.showConfirmDialog(query,
          "Ova naredba æe izbrisati "+Aus.getSlogova(rows)+". Nastaviti?", "Potvrda",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
      return response == JOptionPane.OK_OPTION;
    } catch (Exception e) {
      temp.close();
      int response = JOptionPane.showConfirmDialog(query,
          "Ne mogu parsirati naredbu za brisanje! Nastaviti?", "Potvrda",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
      return response == JOptionPane.OK_OPTION;
    }
  }

  private boolean confirmBatch(String[] comms) {
    raDelayWindow temp = raDelayWindow.show(null, "Provjera",
        "Provjera niza naredbi...", 250).setModal(true);
    try {
      int inserts = 0, deletes = 0, updates = 0, selects = 0;
      for (int i = 0; i < comms.length; i++)
        if (comms[i].length() > 6) {
          String com = comms[i].substring(0, 6).toLowerCase();
          if (com.equals("delete"))
            deletes += getAffectRowCount(comms[i]);
          else if (com.equals("update")) ++updates;
          else if (com.equals("insert")) ++inserts;
          else if (com.equals("select")) ++selects;
        }
      temp.close();
      if (selects > 0) {
        JOptionPane.showMessageDialog(query, "Niz naredbi ne smije sadržavati SELECT!",
            "Poruka", JOptionPane.WARNING_MESSAGE);
        return false;
      }
      if (inserts + updates + deletes > 0) {
        VarStr mess = new VarStr("Ovaj niz naredbi æe");
        if (deletes > 0) {
          mess.append(" izbrisati ").append(Aus.getSlogova(deletes));
          if (inserts + updates > 0) mess.append(" i");
        }
        if (inserts + updates > 0) {
          mess.append(" izvršiti ");
          if (updates > 0) {
            mess.append(Aus.getNum(updates, "naredbu", "naredbe", "naredbi")).append(" izmjene");
            if (inserts > 0) mess.append(" i");
          }
          if (inserts > 0)
            mess.append(Aus.getNum(inserts, "naredbu", "naredbe", "naredbi")).append(" dodavanja");
          mess.append(" slogova");
        }
        mess.append(". Nastaviti?");
        int response = JOptionPane.showConfirmDialog(query, new raMultiLineMessage(mess.toString()),
            "Potvrda", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        return response == JOptionPane.OK_OPTION;
      }
      JOptionPane.showMessageDialog(query, "Niz naredbi ne radi ništa!",
          "Poruka", JOptionPane.WARNING_MESSAGE);
      return false;
    } catch (Exception e) {
      temp.close();
      int response = JOptionPane.showConfirmDialog(query,
          "Ne mogu analizirati niz naredbi! Nastaviti?", "Potvrda",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
      return response == JOptionPane.OK_OPTION;
    }
  }


  /*private boolean confirmLargeQuery(String sql) {
    raDelayWindow temp = null;
    try {
      String cnt = sql.toLowerCase();
      if (cnt.indexOf("union") > 0) return true;
      if (cnt.indexOf("from") < 0) return true;
      temp = raDelayWindow.show(null, "Provjera",
        "Provjera naredbe...", 250).setModal(true);
      cnt = "SELECT COUNT(*) as num " + cnt.substring(cnt.indexOf("from"));
      vl.execSQL(cnt);
      vl.RezSet.open();
      int rows = vl.getSetCount(vl.RezSet, 0);
      temp.close();
      if (rows < 5000) return true;
      int response = JOptionPane.showConfirmDialog(query,
                "Ova naredba æe (pokušati) prikazati "+Aus.getSlogova(rows)+". Nastaviti?",
                "Potvrda", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
      return response == JOptionPane.OK_OPTION;
    } catch (Exception e) {
      if (temp != null) temp.close();
      int response = JOptionPane.showConfirmDialog(query,
          "Ne mogu parsirati naredbu za dohvat! Nastaviti?", "Potvrda",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
      return response == JOptionPane.OK_OPTION;
    }
  }*/

  private boolean checkBatchCommands(String sql) {
    if (sql.indexOf(';') < 0) return false;
    int pos;
    VarStr vs = new VarStr(sql);
    char q = '\'';
    if (vs.countOccurences(q) % 2 == 1 &&
        vs.countOccurences('"') % 2 == 0) q = '"';
    if (vs.countOccurences(q) % 2 == 1) return true;
    while ((pos = vs.indexOf(q)) >= 0)
      vs.delete(pos, vs.indexOf(q, pos + 1) + 1);
    return vs.indexOf(';') >= 0;
  }

  private boolean executeBatch(String sql) {
    if (!checkBatchCommands(sql)) return false;
    String[] comms;
    VarStr vs = new VarStr(sql);
    char q = '\'';
    if (vs.countOccurences(q) % 2 == 1 &&
        vs.countOccurences('"') % 2 == 0) q = '"';
    if (vs.countOccurences(q) % 2 == 0) {
      List c = new ArrayList();
      boolean insideQuote = false;
      int beg = 0;
      for (int i = 0; i < vs.length(); i++)
        if (vs.charAt(i) == q) insideQuote = !insideQuote;
        else if (!insideQuote && vs.charAt(i) == ';') {
          String s = vs.mid(beg, i).trim();
          if (s.length() > 0) c.add(s);
          beg = i + 1;
        }
      comms = (String[]) c.toArray(new String[c.size()]);
    } else
      comms = vs.splitTrimmed(';');

    if (!confirmBatch(comms)) return true;
    process = raDelayWindow.show(250).setModal(true);
    try {
      for (int i = 0; i < comms.length; i++)
        if (comms[i].trim().length() > 0) {
          long timer = System.currentTimeMillis();
          dm.getDatabase1().executeStatement(comms[i]);
          System.out.println("Statement executed in "+(System.currentTimeMillis() - timer)+" ms");
        }
      process.close();
      if (!offline) insertHistory();
      JOptionPane.showMessageDialog(query, "Naredbe uspješno izvedene!", "Poruka", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
      process.close();
      JOptionPane.showMessageDialog(query, new raMultiLineMessage(
        "Niz naredbi nije uspio! Greška:\n\n" + e.getLocalizedMessage(),
        SwingConstants.LEADING), "Greška", JOptionPane.ERROR_MESSAGE);
//      e.printStackTrace();
    }
    return true;
  }
  
  void executeBshCommand(boolean detach) {    
    tags = currq.addDefaults(tags);    
    boolean args = false;
    for (Iterator i = tags.keySet().iterator(); !args && i.hasNext(); )
      args = ((Tag) tags.get(i.next())).isArg();
    if (args) {
      if (offline) {
        if (load.isInterrupted()) return;
        load.close();
      }

      ArgumentDialog.show(offline ? dialogOwner : this.getWindow(), tags,
                          offline ? dialogTitle : "Vrijednosti parametara", true);
      if (!ArgumentDialog.isOK()) return;
//      if (!popArgumentPanel(actual)) return null;
      try {        
        for (Iterator i = tags.keySet().iterator(); i.hasNext(); ) {
          String name = (String) i.next();          
          Tag arg = (Tag) tags.get(name);          
          if (arg.isArg()) {            
            if (arg.type.equals(arg.CHAR))
              bshInterpreter.set(name, arg.getValue().getString());              
            else if (arg.type.equals(arg.INT))
              bshInterpreter.set(name, arg.getValue().getInt());
            else if (arg.type.equals(arg.FLOAT2) || arg.type.equals(arg.FLOAT3))
              bshInterpreter.set(name, arg.getValue().getBigDecimal());
            else if (arg.type.equals(arg.DATE_FROM) || arg.type.equals(arg.DATE_TO))
              bshInterpreter.set(name, arg.getValue().getTimestamp());                        
          }
        }
      } catch (EvalError e) {
        e.printStackTrace();        
        JOptionPane.showMessageDialog(offline ? dialogOwner : this.getWindow(), 
            "Greška u parametrima!", "Greška", JOptionPane.ERROR_MESSAGE);        
        return;
      }
    }
    currq.resolve();
    try {
      process = raDelayWindow.show(250).setModal(true);
      if (offline) process.setMessage("Dohvat podataka...", false, 10);
      Object ret = bshInterpreter.eval(currq.command);
      if (ret instanceof StorageDataSet) 
        analyzeAndShowResults((StorageDataSet) ret);
      else {
        process.close();
        if (!offline) insertHistory();
        if (!offline && (!detach || ret != null)) showReturnValue(ret);        
      }
    } catch (EvalError e) {
      e.printStackTrace();
      process.close();
      JOptionPane.showMessageDialog(offline ? dialogOwner : this.getWindow(),
          new raMultiLineMessage("Greška u izvoðenju bsh skripte:\n\n"+e.getMessage(),
              JLabel.LEADING), "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
  }
  
  private JFrame packer = null;
  private void showReturnValue(Object ret) {
    if (ret == null) {
      JOptionPane.showMessageDialog(this.getWindow(), "Bsh skripta uspješno izvedena!", 
         "Poruka", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    String text = ret.toString();
    JTextArea resultView = new JTextArea();
    resultView.setLineWrap(true);
    resultView.setColumns(text.length() > 64 ? 64 : text.length());
    resultView.setText(text);
    if (packer == null) packer = new JFrame();    
    packer.getContentPane().add(resultView);
    packer.pack();    
    packer.getContentPane().removeAll();    
    resultView.setBackground(getContentPane().getBackground());        
    resultView.setEditable(false);
    JOptionPane.showMessageDialog(this.getWindow(), resultView, 
        "Rezultat", JOptionPane.PLAIN_MESSAGE);    
  }

  void OKPress(boolean detach) {
    updateTitle();
    currq = new Query(query.getText(), tags);
    if (!isSqlCommand(currq.command)) {
      executeBshCommand(detach);
      return;
    }
    String sql = currq.command.replace('\n', ' ').trim();
    if (sql.equals("")) {
      if (!offline) return;
      load.close();
      JOptionPane.showMessageDialog(this.getWindow(), "Greška u izvještaju!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (!offline && executeBatch(sql)) return;
    sql = checkArguments(sql);
    if (sql == null) return;
    if (!detach && sql.length() >= 6 && sql.substring(0, 6).equalsIgnoreCase("delete"))
      if (!confirmDelete(sql)) return;
//    if (sql.length() >= 6 && sql.substring(0, 6).equalsIgnoreCase("select"))
//      if (!confirmLargeQuery(sql)) return;
    process = raDelayWindow.show(250).setModal(true);
    if (sql.length() >= 6 && sql.substring(0, 6).equalsIgnoreCase("select"))
      parseQuery(sql, detach);
    else {
      if (detach) {
        process.close();
        JOptionPane.showMessageDialog(query, "Naredba ne proizvodi prikaz!", "Poruka", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      try {
        long timer = System.currentTimeMillis();
        dm.getDatabase1().executeStatement(sql);
        System.out.println("Statement executed in "+(System.currentTimeMillis() - timer)+" ms");
        process.close();
        if (!offline) insertHistory();
        JOptionPane.showMessageDialog(query, "Naredba uspješno izvedena!", "Poruka", JOptionPane.INFORMATION_MESSAGE);
      } catch (Exception e) {
        process.close();
        JOptionPane.showMessageDialog(query, new raMultiLineMessage(
          "Naredba nije uspjela! Greška:\n\n" + e.getLocalizedMessage(),
          SwingConstants.LEADING), "Greška", JOptionPane.ERROR_MESSAGE);
//        e.printStackTrace();
      }
    }
    process.close();
  }
  
  boolean isInsertHistory() {
    return frmParam.getParam("sisfun", "histInsert", "N", "Automatsko dodavanje naredbi " +
        "u SQL Pilot history (D/N)", true).equalsIgnoreCase("D");
  }
  
  boolean isStickyNavigate() {
    return frmParam.getParam("sisfun", "histAuto", "D", 
        "Automatski update history buffera", true).equalsIgnoreCase("D"); 
  }

  public void show() {
    if (offline && busy) return;

    if (history.size() == 0) {
      loadHistory(new File("default.his"), false);
      if (history.size() == 0)
        loadHistory(new File(Aus.getHomeDirectory(), "history.his"), false);
    }
//    jps.start();
    if (isInsertHistory()) {
      if (copyCurr != null)
        nav.removeOption(copyCurr);
      copyCurr = null;
    } else if (copyCurr == null) {
      nav.addOption(copyCurr = new raNavAction("Kopiraj", 
          raImages.IMGCOPYCURR, KeyEvent.VK_F2, KeyEvent.SHIFT_MASK) {
        public void actionPerformed(ActionEvent e) {
          if (hist.hasNext()) {
            Query q = (Query) hist.next();
            q = new Query(q.command, new HashMap(q.tags));
            hist.add(q);
            hist.previous();
            saveDefaultHistory();
          }
        }
      }, 1);
    }
    offline = busy = false;
    updateTitle();
    super.show();
  }
  
  void updateTitle() {
    String rest = "";
    String q = query.getText();
    if (q.length() > 36) q = q.substring(0, 32) + "...";
    if (q.length() > 0) rest = " - " + q;
    setTitle("SQL Pilot" + rest);
  }

  void cancelPress() {
    this.hide();
  }

  void insertHistory() {
    boolean any = hist.hasNext();    
    String text = currq.command;
    if (!any || !text.equalsIgnoreCase(((Query) hist.next()).command)) {    
      if (!isInsertHistory() && any) hist.remove();      
      hist.add(currq);
      hist.previous();
    } else ((Query) hist.previous()).tags = tags;
    saveDefaultHistory();
  }

  void lastHistory() {
    updateHistory();
    while (hist.hasNext()) hist.next();
    query.setText("");
    tags = null;
    currq = null;
    updateInfo();
  }

  void prevHistory() {
    updateHistory();
    if (hist.hasPrevious()) {
      currq = (Query) hist.previous();
      query.setText(currq.command);
      tags = currq.tags;
      updateInfo();
    }
  }

  void nextHistory() {
    updateHistory();
    if (hist.hasNext()) hist.next();
    showNext();
    updateInfo();
  }
  
  void updateHistory() {
    if (!isInsertHistory() && isStickyNavigate() && hist.hasNext()) {
      Query q = (Query) hist.next();
      String tx = query.getText();
      if (!q.command.equals(tx)) {
        q.command = tx;
        saveDefaultHistory();
      }
      hist.previous();
    }
  }

  private void showNext() {
    if (hist.hasNext()) {
      currq = (Query) hist.next();
      query.setText(currq.command);
      tags = currq.tags;
      hist.previous();
    } else {
      query.setText("");
      currq = null;
      tags = null;
    }
  }
  
  void updateInfo() {
    histInfo.setText((hist.nextIndex() + 1) + "/" + history.size());
    updateTitle();
  }

  void deleteAllHistory() {
    history.clear();
    hist = history.listIterator();
    query.setText("");
    tags = null;
    saveDefaultHistory();
  }

  void deleteHistory() {
    if (hist.hasNext()) {
      hist.next();
      hist.remove();
      showNext();
      saveDefaultHistory();
    }
  }

  private void loadCommand(File fname) {
    loadCommand(TextFile.read(fname));
  }

  private boolean loadCommand(java.net.URL url) {
    try {
      return loadCommand(TextFile.read(url.openStream()));
    } catch (Exception e) {
      return false;
    }
  }

  private boolean loadCommand(TextFile r) {
    if (r != null) {
      int endTag;
      VarStr com = new VarStr();
      tags = new HashMap();
      String line = r.in();
      String sep = System.getProperty("line.separator");
      if (line.equals("<#>")) {
        while (null != (line = r.in()))
          if (line.startsWith("<#") && (endTag = line.indexOf(">")) > 0) {
            tags.put(line.substring(2, endTag), new Tag(line.substring(endTag + 1)));
          } else
            com.append(com.length() > 0 ? sep : "").append(line);
        query.setText(com.toString());
        return true;
      }

      String delim = line.trim().endsWith(";") ? "" : ";";
      do {
        com.append(line).append(delim).append(sep);
      } while (null != (line = r.in()));
      query.setText(com.toString());
      return true;
      //JOptionPane.showMessageDialog(this.getWindow(), "Neispravna datoteka!",
      //      "Greška", JOptionPane.ERROR_MESSAGE);
    }
    JOptionPane.showMessageDialog(this.getWindow(), "Greška prilikom otvaranja datoteke!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
    return false;
  }

  private void loadHistory(File fname, boolean warn) {
    TextFile r = TextFile.read(fname);
    if (r != null) {
      VarStr com = new VarStr();
      String line = r.in();
      if (line == null) line = ""; //ai: ako file nema niti jednu liniju reader.readline() vraca null i tu puca pa je nemoguce otvoriti Pilot 
      if (line.equals("<#>")) {
        history.clear();
        hist = history.listIterator();
        tags = new HashMap();
        int endTag;
        while (null != (line = r.in())) {
          if (line.equals("<#>")) {
            if (com.length() > 0) hist.add(new Query(com.toString(), tags));
            com.clear();
            tags = new HashMap();
          } else if (line.startsWith("<#") && (endTag = line.indexOf(">")) > 0) {
            tags.put(line.substring(2, endTag), new Tag(line.substring(endTag + 1)));
          } else
            com.append(com.length() > 0 ? System.getProperty("line.separator") : "").append(line);
        }
        if (com.length() > 0) hist.add(new Query(com.toString(), tags));
        lastHistory();
      } else if (warn)
        JOptionPane.showMessageDialog(this.getWindow(), "Neispravna datoteka!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
    } else if (warn)
      JOptionPane.showMessageDialog(this.getWindow(), "Greška prilikom otvaranja datoteke!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
  }

  void loadFiles() {
    if (jf.showOpenDialog(this.getWindow()) == jf.APPROVE_OPTION) {
      if (jf.getSelectedFile().getAbsolutePath().endsWith(".sql")) {
        loadCommand(jf.getSelectedFile());
      } else if (jf.getSelectedFile().getAbsolutePath().endsWith(".his")) {
        loadHistory(jf.getSelectedFile(), true);
      } else
        JOptionPane.showMessageDialog(this.getWindow(), "Pogrešna ekstenzija!", "Greška",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private ArrayList tagList(HashMap _tags) {
    if (_tags == null || _tags.size() == 0) return null;
    ArrayList ret = new ArrayList();
    for (Iterator i = _tags.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry e = (Map.Entry) i.next();
      if (!((Tag) e.getValue()).isDefault())
        ret.add("<#" + e.getKey() + ">" + e.getValue());
    }
    return ret;
  }

  private void saveCommand(File fname) {
    TextFile f = TextFile.write(fname);
    if (f != null) {
      f.out("<#>").out(tagList(tags)).out(query.getText()).check();
      f.close();
      JOptionPane.showMessageDialog(this.getWindow(), new raMultiLineMessage("Datoteka pohranjena:\n" + fname,
            SwingConstants.LEADING), "Poruka", JOptionPane.INFORMATION_MESSAGE);
    } else
      JOptionPane.showMessageDialog(this.getWindow(), "Greška prilikom otvaranja datoteke!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
  }
  
  void saveDefaultHistory() {
    saveHistory(new File(Aus.getHomeDirectory(), "history.his"), false);
  }

  private void saveHistory(File fname, boolean warn) {
    TextFile f = TextFile.write(fname);
    if (f != null) {
      for (Iterator i = history.iterator(); i.hasNext();)
        f.out("<#>").out(((Query) i.next()).out()).check();
      f.close();
      if (warn)
        JOptionPane.showMessageDialog(this.getWindow(), new raMultiLineMessage("Datoteka pohranjena:\n" + fname,
            SwingConstants.LEADING), "Poruka", JOptionPane.INFORMATION_MESSAGE);
    } else if (warn)
      JOptionPane.showMessageDialog(this, "Greška prilikom otvaranja datoteke!", "Greška", JOptionPane.ERROR_MESSAGE);
    updateInfo();
  }

  void saveFiles() {
    if (jf.showSaveDialog(this.getWindow()) == jf.APPROVE_OPTION) {
      String fname = jf.getSelectedFile().getName().toLowerCase();
      if (jf.getFileFilter() == filterSQL && !fname.endsWith(".sql") && fname.indexOf(".") == -1)
        fname = fname.concat(".sql");
      else if (jf.getFileFilter() == filterHIS && !fname.endsWith(".his") && fname.indexOf(".") == -1)
        fname = fname.concat(".his");
      if (fname.endsWith(".sql"))
        saveCommand(new File(jf.getSelectedFile().getParent(), fname));
      else if (fname.endsWith(".his"))
        saveHistory(new File(jf.getSelectedFile().getParent(), fname), true);
      else JOptionPane.showMessageDialog(this.getWindow(), "Pogrešna ekstenzija!", "Greška",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private HashMap getActualParams(String sql) {
    int beg, end;
    HashMap actual = new HashMap();
//    String sql = query.getText().toLowerCase();
//    String[] tnames = findTables(sql, false);
//    if (tnames == null) return null;
    StringTokenizer st = new StringTokenizer(sql);

    while (st.hasMoreTokens()) {
      String tok = st.nextToken();
      if ((beg = end = tok.indexOf("$")) >= 0 &&
          (beg < 3 || !tok.substring(beg - 3, beg).equalsIgnoreCase("RDB"))) {
        while (++end < tok.length() && Character.isLetterOrDigit(tok.charAt(end)));
        if (end > beg + 1) actual.put(tok.substring(beg + 1, end),
           new Tag(tok.substring(beg + 1, end).concat(";S")));
      }
    }
    return actual;
  }

  private String checkArguments(String sql) {
    HashMap actual = getActualParams(sql);    
    tags = currq.addDefaults(tags);    
    if (actual.size() == 0) {
      currq.resolve();
      return sql;
    }

    if (new HashMap(actual).keySet().retainAll(tags.keySet())) {
      if (offline) {
        load.close();
        JOptionPane.showMessageDialog(this.getWindow(),
          "Greška u SQL naredbi: nedefinirani argument!", "Greška", JOptionPane.ERROR_MESSAGE);
        return null;
      }
      actual.keySet().removeAll(tags.keySet());
      tags.putAll(actual);
      props.show(tags);
      return null;
    } 
    if (offline) {
      if (load.isInterrupted()) return null;
      load.close();
    }
    ArgumentDialog.show(offline ? dialogOwner : this.getWindow(), tags,
                        offline ? dialogTitle : "Vrijednosti parametara", false);
    if (!ArgumentDialog.isOK()) return null;
//      if (!popArgumentPanel(actual)) return null;
    VarStr result = new VarStr(sql);
    for (Iterator i = tags.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry me = (Map.Entry) i.next();
     if (((Tag) me.getValue()).isArg())
        result.replaceAll("$" + me.getKey(), ((Tag) me.getValue()).queryString());
    }
    currq.resolve();
    return result.toString();
  }

  void showParams() {
    Query q = new Query(null, null);
    tags = q.addDefaults(tags);          

    HashMap actual = getActualParams(query.getText().toLowerCase());
//    System.out.println(actual);
//    System.out.println(actual.entrySet().toArray());
    if (actual != null) {
      actual.keySet().removeAll(tags.keySet());
      tags.putAll(actual);
    }
    props.show(tags);
  }

  private frmTableDataView getNewView(boolean edit) {
    return new frmTableDataView(edit, true, true) {
      public void afterSaveChanges(String tname) {
        if (tname.equalsIgnoreCase("tablice")) {
          tab.refresh();
//          tab = Tablice.getDataModule().getFilteredDataSet("", true);
          ac.removeAll(AutoComplete.AFTER_FROM);
          fillTableNames();
        }
      }
    };
  }

  Thread begin(final boolean detach) {
    Thread t = null;
    if (!busy) {
      busy = true;
      t = new Thread() {
        public void run() {
          try {
            OKPress(detach);
          } finally {
            busy = false;
          }
        }
      };
      t.start();
    }
    return t;
  }

  private void detach(boolean edit) {
    frmTableDataView old = view;
    old.detach();
    if (old != null && old.isShowing())
      old.setTitle(old.getTitle() + " (zombie)");

    view = getNewView(edit);
    view.jp.installNaturalSelectionTracker();
    view.setSize(640, 400);
    view.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    view.jf.setCurrentDirectory(old.jf.getCurrentDirectory());
  }

  private void setupNavBars() {
    nav.addOption(new raNavAction("Nova", raImages.IMGADD, KeyEvent.VK_F2) {
      public void actionPerformed(ActionEvent e) {
        lastHistory();
      }
    });    
    nav.addOption(new raNavAction("Prethodna", raImages.IMGBACK, KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        prevHistory();
      }
    });
    nav.addOption(new raNavAction("Sljede\u0107a", raImages.IMGFORWARD, KeyEvent.VK_F5) {
      public void actionPerformed(ActionEvent e) {
        nextHistory();
      }
    });
    nav.addOption(new raNavAction("Obriši", raImages.IMGDELETE, KeyEvent.VK_F3) {
      public void actionPerformed(ActionEvent e) {
        deleteHistory();
      }
    });
    nav.addOption(new raNavAction("Obriši sve", raImages.IMGDELALL, KeyEvent.VK_F3, KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        deleteAllHistory();
      }
    });

    nav.addOption(new raNavAction("Otvori", raImages.IMGOPEN, KeyEvent.VK_O, KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        loadFiles();
      }
    });
    nav.addOption(new raNavAction("Spremi", raImages.IMGSAVE, KeyEvent.VK_S, KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        saveFiles();
      }
    });
    nav.addOption(new raNavAction("Spoji tablice", raImages.IMGSTAV, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        naturalJoin();
      }
    });
    nav.addOption(new raNavAction("Proširi kolone", raImages.IMGZOOM, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        try {
          inflateAsterisk();
        } catch (Exception ex) {
          // silent
        }
      }
    });
    nav.addOption(new raNavAction("Konekcija", raImages.IMGHOME, KeyEvent.VK_F12) {
      public void actionPerformed(ActionEvent e) {
        dm.showParams();
        dm.reconnectIfNeeded();
        updateTitle();
      }
    });

    nav.addOption(new raNavAction("Novi prikaz", raImages.IMGTABLE, KeyEvent.VK_F9) {
      public void actionPerformed(ActionEvent e) {
        begin(true);
      }
    });
    nav.addOption(new raNavAction("Novi editor", raImages.IMGALIGNJUSTIFY, KeyEvent.VK_N, KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        open(false);
      }
    });
    nav.registerNavBarKeys(this);

    info.addOption(new raNavAction("Parametri", raImages.IMGHISTORY, KeyEvent.VK_F11) {
      public void actionPerformed(ActionEvent e) {
        showParams();
      }
    });
    info.addOption(new raNavAction("Info panel", raImages.IMGINFORMATION, KeyEvent.VK_I, KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        if (down.isAncestorOf(jps)) {
          jps.stop();
          down.remove(jps);
        } else {
          down.add(jps, BorderLayout.SOUTH);
          jps.start();
        }
      }
    });
    info.registerNavBarKeys(this);
  }
}

class PropsDialog {
  JDialog dlg, edit;
  Window owner;
  raJPTableView jp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      if (data.rowCount() > 0) 
        openEdit(false, data.getString("VRSTA").equalsIgnoreCase("A"));      
    }
  };
  raNavBar rnb = jp.getNavBar();

  jpPilotParam ppan = new jpPilotParam();
  jpPilotArg apan = new jpPilotArg();
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      if (Validacija()) {
        data.post();
        jp.fireTableDataChanged();
        closeEdit();
      }
    }
    public void jPrekid_actionPerformed() {
      data.cancel();
      closeEdit();
    }
  };
  StorageDataSet data = PilotParam.getDataModule().getDataSet();

  public PropsDialog(Window owner) {
    this.owner = owner;
    data.open();
    ppan.BindComponents(data);
    apan.BindComponents(data);
    jp.setBorder(BorderFactory.createEtchedBorder());
    jp.getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    jp.setPreferredSize(new Dimension(520, 200));
    jp.getColumnsBean().setSaveSettings(false);
//    data.setTableName("PilotParam");
    jp.setDataSet(data);
    jp.setVisibleCols(new int[] {0,1,2});
    jp.getColumnsBean().initialize();
    rnb.addOption(new raNavAction("Novi parametar", raImages.IMGADD, KeyEvent.VK_F2) {
      public void actionPerformed(ActionEvent e) {
        openEdit(true, false);
      }
    });
    rnb.addOption(new raNavAction("Novi argument", raImages.IMGCOPYCURR, KeyEvent.VK_F2) {
      public void actionPerformed(ActionEvent e) {
        openEdit(true, true);
      }
    });
    rnb.addOption(new raNavAction("Promijeni", raImages.IMGCHANGE, KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        if (data.rowCount() > 0) 
          openEdit(false, data.getString("VRSTA").equalsIgnoreCase("A"));        
      }
    });
    rnb.addOption(new raNavAction("Obriši", raImages.IMGDELETE, KeyEvent.VK_F3) {
      public void actionPerformed(ActionEvent e) {
        if (data.rowCount() > 0) {
          data.emptyRow();
          jp.fireTableDataChanged();
        }
      }
    });
    rnb.addOption(new raNavAction("Obriši sve", raImages.IMGDELALL, KeyEvent.VK_F3, KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        data.empty();
        jp.fireTableDataChanged();
      }
    });
    rnb.addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    jp.addTableModifier(new raTableValueModifier("VRSTA",
        new String[] {"A", "P"}, new String[] {"Argument", "Parametar"}));

/*    jp.addTableModifier(new raTableModifier() {
      public boolean doModify() {
        try {
          return ((JraTable2) getTable()).getDataSetColumn(getColumn()).getColumnName().
              equalsIgnoreCase("VRSTA");
        } catch (Exception e) {
          return false;
        }
      }
      public void modify() {
        if (renderComponent instanceof JLabel) {
          String orig = ((JLabel) renderComponent).getText();
          if (orig.equals("A")) ((JLabel) renderComponent).setText("Argument");
          if (orig.equals("P")) ((JLabel) renderComponent).setText("Parametar");
        }
      }
    }); */

    jp.addTableModifier(new raTableValueModifier("TIP",
        new String[] {"S", "D", "I", "2", "3"},
        new String[] {"Tekst", "Datum", "Cijeli", "2 decimale", "3 decimale"}));

  /*  jp.addTableModifier(new raTableModifier() {
      String[] abbr = new String[] {"S", "D", "I", "2", "3"};
      String[] full = new String[] {"Tekst", "Datum", "Cijeli", "2 decimale", "3 decimale"};
      public boolean doModify() {
        try {
          return ((JraTable2) getTable()).getDataSetColumn(getColumn()).getColumnName().
              equalsIgnoreCase("TIP");
        } catch (Exception e) {
          return false;
        }
      }
      public void modify() {
        if (renderComponent instanceof JLabel) {
          String orig = ((JLabel) renderComponent).getText();
          for (int i = 0; i < abbr.length; i++)
            if (orig.equalsIgnoreCase(abbr[i]))
              ((JLabel) renderComponent).setText(full[i]);
        }
      }
    });*/

//    pan = new JPanel(new BorderLayout());
//    pan.add(jp, BorderLayout.CENTER);
//    pan.add(okp, BorderLayout.SOUTH);
  }

  public void show(HashMap tags) {
    if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, "Parametri", true);
    else if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, "Parametri", true);
    else
      dlg = new JraDialog((Frame) null, "Parametri", true);
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        dispose();
      }
    });

    dlg.getContentPane().add(jp, BorderLayout.CENTER);
    jp.initKeyListener(dlg);
    dlg.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F10) {
          e.consume();
          dispose();
        }
      }
    });

    data.empty();
    for (Iterator i = tags.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry me = (Map.Entry) i.next();
      Tag t = (Tag) me.getValue();
      data.insertRow(false);
      data.setString("IME", me.getKey().toString());
      t.toData(data);
    }
    dlg.pack();
    dlg.setLocationRelativeTo(owner);
    dlg.show();
    tags.clear();
    for (data.first(); data.inBounds(); data.next())
      tags.put(data.getString("IME"), new Tag(data));
//    if (ok)
//      ((PropsModel) tab.getModel()).getData(tags);
  }

  void openEdit(final boolean isNew, final boolean isArg) {
    if (!isNew && data.rowCount() == 0) return;
    if (isNew) {
      data.insertRow(false);
      if (isArg) data.setString("VRSTA", "A");
    }
    edit = new JraDialog(dlg, (isNew ? "Unos" : "Izmjena" ) + 
        (isArg ? " argumenta" : " parametra"), true);
    
    if (isArg) edit.getContentPane().add(apan, BorderLayout.CENTER);
    else edit.getContentPane().add(ppan, BorderLayout.CENTER);
    edit.getContentPane().add(okp, BorderLayout.SOUTH);
    edit.pack();
    edit.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    if (isArg) apan.prepareEdit(isNew);
    else ppan.prepareEdit(isNew);
    
    edit.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        data.cancel();
        closeEdit();
      }
    });
    edit.addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        if (isArg) apan.setFocus(isNew);
        else ppan.setFocus(isNew);        
      }
    });
    okp.registerOKPanelKeys(edit);
    edit.setLocationRelativeTo(dlg);
    edit.show();
  }

  void closeEdit() {
    if (edit != null) {
      okp.unregisterOKPanelKeys(edit);
      edit.dispose();
      edit = null;
    }
  }

  boolean Validacija() {
    if (data.getString("VRSTA").equals("P")) return true;
    if (data.getString("GET").length() > 0) {
      DataSet ds = null;
      String s;
      if (data.getString("GET").startsWith("get"))
        try {
  //        System.out.println(data.getString("GET"));
          java.lang.reflect.Method m = dM.class.getMethod(data.getString("GET"), null);
          ds = (DataSet) m.invoke(dM.getDataModule(), null);
        } catch (Exception e) {
          e.printStackTrace();
          apan.jraGet.requestFocus();
          JOptionPane.showMessageDialog(edit, "Pogrešan getter!",
            "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      if (Valid.getValid().isEmpty(apan.jraKol) || Valid.getValid().isEmpty(apan.jraVis))
        return false;
      if (ds != null) {
        StringTokenizer kol = new StringTokenizer(data.getString("KOLONE"), ",");
        while (kol.hasMoreTokens())
          if (ds.hasColumn(s = kol.nextToken().trim()) == null) {
            apan.jraKol.requestFocus();
            JOptionPane.showMessageDialog(edit, "Nepostoje\u0107a kolona: "+s+"!",
                                          "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
          }
      }
      StringTokenizer vis = new StringTokenizer(data.getString("VISKOL"), ",");
      while (vis.hasMoreTokens()) {
        if (!Aus.isNumber(vis.nextToken())) {
          apan.jraVis.requestFocus();
          JOptionPane.showMessageDialog(edit, "Greška u popisu vidljivih kolona!",
                                        "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    } else {
      data.setString("KOLONE", "");
      data.setString("VISKOL", "");
      data.setInt("WIDTH", 0);
    }
    return true;
  }

  void dispose() {
    if (dlg != null) {
      jp.getNavBar().unregisterNavBarKeys(dlg);
      dlg.dispose();
      dlg = null;
    }
  }
}

class ArgumentDialog {
  private static ArgumentDialog inst = new ArgumentDialog();
  JDialog dlg;
  JPanel pan;
  HashMap tags;
  ArrayList tagnames = new ArrayList();
  StorageDataSet data;
  JTextComponent[] td;
  Interpreter bshInterpreter;
  boolean bshell;

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      ok = true;
      if (Validacija())
        dispose();
    }
    public void jPrekid_actionPerformed() {
      ok = false;
      dispose();
    }
  };

  boolean ok;

  private ArgumentDialog() {
    // no public constr.
  }

  public static void show(Window owner, HashMap tags, String title, boolean bsh) {
    inst.showDlg(owner, tags, title, bsh);
  }

  public static boolean isOK() {
    return inst.ok;
  }

  private void showDlg(Window owner, HashMap tags, String title, boolean bsh) {
    this.tags = tags;
    bshell = bsh;
    createPanel();
    if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, title, true);
    else if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, title, true);
    else
      dlg = new JraDialog((Frame) null, title, true);
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        ok = false;
        dispose();
      }
    });
    dlg.addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        if (td[0] != null)
          td[0].requestFocus();
      }
    });
    dlg.getContentPane().add(pan, BorderLayout.CENTER);
    dlg.getContentPane().add(okp, BorderLayout.SOUTH);
    okp.registerOKPanelKeys(dlg);
    dlg.pack();
    dlg.setLocationRelativeTo(owner);
    initDefaults();
    dlg.show();
  }

  void dispose() {
    if (dlg != null) {
      dlg.dispose();
      dlg = null;
    }
  }

  private void initDefaults() {
    for (int i = 0; i < tagnames.size(); i++) {
      String key = (String) tagnames.get(i);
      Tag t = (Tag) tags.get(key);
      if (t.defv != null && t.defv.length() > 0) {
        if (t.type.equals(t.CHAR)) data.setString(key, t.defv);
        else if (t.type.equals(t.INT)) data.setInt(key, Aus.getNumber(t.defv));
        else if (t.type.equals(t.FLOAT2))
          data.setBigDecimal(key, Aus.getDecNumber(t.defv).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        else if (t.type.equals(t.FLOAT3))
          data.setBigDecimal(key, Aus.getDecNumber(t.defv).setScale(3, java.math.BigDecimal.ROUND_HALF_UP));
        else {
          if (t.defv.equalsIgnoreCase("$today"))
            data.setTimestamp(key, Valid.getValid().getToday());
          else if (t.defv.equalsIgnoreCase("$month"))
            data.setTimestamp(key, Util.getUtil().getFirstDayOfMonth(Valid.getValid().getToday()));
          else if (t.defv.equalsIgnoreCase("$year"))
            data.setTimestamp(key, Util.getUtil().getFirstDayOfYear(Valid.getValid().getToday()));
        }
      }
      for (int c = 0; c < pan.getComponentCount(); c++)
        if (pan.getComponent(c) instanceof JlrNavField) {
          JlrNavField tf = (JlrNavField) pan.getComponent(c);
          if (tf.getColumnName().equalsIgnoreCase(key))
            tf.forceFocLost();
        }
    }
    try {
      bshInterpreter = new Interpreter();
      bshInterpreter.eval(
          "import hr.restart.baza.*;" +
          "import hr.restart.util.*;" +
          "import hr.restart.swing.*;" +          
          "import com.borland.dx.dataset.*;" +
          "import com.borland.dx.sql.dataset.*;" +
          "import com.borland.jb.util.*;" +
          "import java.math.BigDecimal;"
      );
    } catch (EvalError e) { 
      e.printStackTrace();
    }
  }

  private void createPanel() {
    int y = 20, maxw = 540;
    XYLayout xy = new XYLayout();
    pan = new JPanel(xy);
    data = new StorageDataSet();
    tagnames.clear();
    for (Iterator i = tags.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry me = (Map.Entry) i.next();
      if (((Tag) me.getValue()).isArg()) tagnames.add(me.getKey());
    }
    Collections.sort(tagnames);
    Column[] cols = new Column[tagnames.size()];
    td = new JTextComponent[cols.length];
    for (int i = 0; i < tagnames.size(); i++) {
      String key = (String) tagnames.get(i);
      Tag t = (Tag) tags.get(key);
      if (t.type.equals(t.CHAR)) cols[i] = dM.createStringColumn(key, t.desc, t.wid == 0 ? 100 : t.wid);
      else if (t.type.equals(t.INT)) cols[i] = dM.createIntColumn(key, t.desc);
      else if (t.type.equals(t.FLOAT2)) cols[i] = dM.createBigDecimalColumn(key, t.desc, 2);
      else if (t.type.equals(t.FLOAT3)) cols[i] = dM.createBigDecimalColumn(key, t.desc, 3);
      else cols[i] = dM.createTimestampColumn(key, t.desc);
    }
    data.setColumns(cols);
    data.open();
    data.insertRow(false);
    for (int i = 0; i < tagnames.size(); i++) {
      String key = (String) tagnames.get(i);
      Tag t = (Tag) tags.get(key);
      boolean vis = t.defv == null || !t.defv.equals("*");
      int haspair = -1;
      int wid = t.wid;
      if (wid == 0) wid = 100;
      else if (wid < 10) wid = 75;
      else if (wid < 20) wid = 100;
      else if (wid < 30) wid = 200;
      else wid = 350;
      if (key.toLowerCase().endsWith("from")) {
        for (int j = 0; j < tagnames.size(); j++) {
          String key2 = ((String) tagnames.get(j)).toLowerCase();
          if (key2.endsWith("to") && key2.substring(0, key2.length() - 2).
              equalsIgnoreCase(key.substring(0, key.length() - 4))) haspair = j;
        }
      } else if (key.toLowerCase().endsWith("to")) {
        for (int j = 0; j < tagnames.size(); j++) {
          String key2 = ((String) tagnames.get(j)).toLowerCase();
          if (key2.endsWith("from") && key2.substring(0, key2.length() - 4).
              equalsIgnoreCase(key.substring(0, key.length() - 2))) haspair = j;
        }
        if (haspair >= 0 && !t.type.equals(t.CHAR)) continue;
      }
      JLabel l = new JLabel(t.desc);
      if (vis) pan.add(l, new XYConstraints(15, y, -1, -1));
      if (t.getter.length() == 0) {
        JraTextField tf = new JraTextField();
        tf.setDataSet(data);
        tf.setColumnName(key);
        td[i] = tf;
        if (cols[i].getDataType() == Variant.TIMESTAMP)
          tf.setHorizontalAlignment(SwingConstants.CENTER);
        if (vis) pan.add(tf, new XYConstraints(150, y, wid, -1));
        if (haspair > 0 && !t.type.equals(t.CHAR)) {
          JraTextField tf2 = new JraTextField();
          tf2.setDataSet(data);
          tf2.setColumnName((String) tagnames.get(haspair));
          td[haspair] = tf2;
          if (vis) pan.add(tf2, new XYConstraints(155+wid, y, wid, -1));
          if (cols[i].getDataType() == Variant.TIMESTAMP) {
            tf2.setHorizontalAlignment(SwingConstants.CENTER);
            new raDateRange(tf, tf2);
          }
        }
      } else {
        String[] kol = new VarStr(t.kol).splitTrimmed(',');
        JlrNavField nfm = new JlrNavField() {
          public void after_lookUp() {
            checkDynamicBind(getColumnName());
          }
        };
        JraButton b = new JraButton();
        nfm.setDataSet(data);
        nfm.setColumnName(key);
        nfm.setNavColumnName(kol[0]);
        nfm.setNavButton(b);
        if (kol.length > 1) {
          JlrNavField nfo = new JlrNavField();
          nfm.setColNames(new String[] {kol[1]});
          nfm.setTextFields(new JTextComponent[] {nfo});
          nfo.setNavProperties(nfm);
          nfo.setColumnName(kol[1]);
          nfo.setSearchMode(1);
          int twid = wid > 100 ? 350 : (wid < 100 ? 200 : 275);
          if (wid > 100) wid = 100;
          if (200 + twid + wid > maxw) maxw = 200 + twid + wid;
          if (vis) pan.add(nfm, new XYConstraints(150, y, wid, -1));
          if (vis) pan.add(nfo, new XYConstraints(155+wid, y, twid, -1));
          if (vis) pan.add(b, new XYConstraints(160+wid+twid, y, 21, 21));
        } else {
          if (vis) pan.add(nfm, new XYConstraints(150, y, wid, -1));
          if (vis) pan.add(b, new XYConstraints(155+wid, y, 21, 21));
        }
        nfm.setSearchMode(0);
        addNastyProps(nfm, t);
        td[i] = nfm;
      }
      if (vis) y += 25;
    }
    for (int i = 0; i < tagnames.size(); i++) {
      Tag t = (Tag) tags.get(tagnames.get(i));
      if (!t.getValue().isUnassignedNull())
        data.setVariant((String) tagnames.get(i), t.getValue());
      else if (t.type.equals(t.DATE_FROM))
        data.setTimestamp((String) tagnames.get(i), Util.getUtil().getFirstDayOfMonth());
      else if (t.type.equals(t.DATE_TO))
        data.setTimestamp((String) tagnames.get(i), Valid.getValid().getToday());
    }
    xy.setWidth(maxw);
    xy.setHeight(y + 15);
  }
  
  void checkDynamicBind(String key) {
    try {
      for (int i = 0; i < tagnames.size(); i++) {
        String tname = (String) tagnames.get(i);
        if (!tname.equals(key)) {
          Tag t = (Tag) tags.get(tname);
          if (!t.getter.startsWith("get") && td[i] instanceof JlrNavField) {
            for (int j = 0; j < tagnames.size(); j++) {
              String tkey = (String) tagnames.get(j);
              Tag tt = (Tag) tags.get(tkey);
              if (tt.type.equals(tt.CHAR))
                bshInterpreter.set(tkey, data.getString(tkey));
              else if (tt.type.equals(tt.INT))
                bshInterpreter.set(tkey, data.getInt(tkey));
              else if (tt.type.equals(tt.FLOAT2) || tt.type.equals(tt.FLOAT3))
                bshInterpreter.set(tkey, data.getBigDecimal(tkey));
              else bshInterpreter.set(tkey, data.getTimestamp(tkey));
            }
            t.getDs = (DataSet) bshInterpreter.eval(t.getter);
            ((JlrNavField) td[i]).setRaDataSet(t.getDs);
          }
        }
      }
    } catch (EvalError e) {
      System.err.println("error "+e);
    }
  }

  boolean Validacija() {
    if (!bshell) {
      for (int i = 0; i < td.length; i++)
        if (pan.isAncestorOf(td[i])) {
          if (td[i] instanceof JlrNavField) ((JlrNavField) td[i]).forceFocLost();
          if (Valid.getValid().isEmpty(td[i]))
            return false;
        }
    }

    for (int i = 0; i < tagnames.size(); i++) {
      Tag t = (Tag) tags.get(tagnames.get(i));
      data.getVariant((String) tagnames.get(i), t.getValue());
    }
    checkDynamicBind("");
    return true;
  }

  private void addNastyProps(JlrNavField nf, Tag t) {
    try {
      String[] vis = new VarStr(t.vis).splitTrimmed(',');
      int[] visc = new int[vis.length];
      for (int i = 0; i < vis.length; i++)
        visc[i] = Integer.parseInt(vis[i]);
      nf.setVisCols(visc);

      if (t.getter.startsWith("get")) {
        java.lang.reflect.Method m = dM.class.getMethod(t.getter, null);
        nf.setRaDataSet((DataSet) m.invoke(dM.getDataModule(), null));
        t.getDs = nf.getRaDataSet();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
