package hr.restart.util;

import hr.restart.start;
import hr.restart.help.raShortcutItem;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.KeyAction;
import hr.restart.swing.SharedFlag;
import hr.restart.swing.raMultiLineMessage;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raDbaseChooser extends JDialog {
  
  Vector vb = new Vector();
  JList bases = new JList(vb) {
    protected void paintComponent(Graphics g) {
      super.paintComponent(Aus.forceAntiAlias(g));
    }
  };
  
  JPopupMenu pop = new JPopupMenu();
  
  JMenuItem pope = new JMenuItem("Izmijeni parametre baze");
  JMenuItem popmu = new JMenuItem("Premjesti bazu gore");
  JMenuItem popmd = new JMenuItem("Premjesti bazu dolje");
  JMenuItem popa = new JMenuItem("Dodaj novu bazu");
  JMenuItem popd = new JMenuItem("Obri�i odabranu bazu");
  JMenuItem popi = new JMenuItem("Inicijaliziraj odabranu bazu");
  
  ConnectionPanel cpan = new ConnectionPanel();
  
  JFileChooser fc = new JFileChooser(new File("."));
  File initFile;
  
  boolean accept;
  boolean changed;
  
  String dtip, duser, dpass, ddbdialect;
  
  int index;
  
  private raDbaseChooser(final boolean edit) {
    super((JFrame) null, "Odabir baze podataka", true);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    Properties props = new Properties();
    FileHandler.loadProperties("base.properties", props, false);
    
    initFile = null;

    index = Aus.getNumber(props.getProperty("index"));
    dtip = props.getProperty("tip");
    duser = props.getProperty("user");
    dpass = props.getProperty("pass");
    ddbdialect = props.getProperty("dbdialect","");
    boolean any = false, all = true;
    for (int bi = 1; any || all; bi++) {
      String name = props.getProperty("name" + bi);
      String url = props.getProperty("url" + bi);
      String tip = props.getProperty("tip" + bi, dtip);
      String user = props.getProperty("user" + bi, duser);
      String pass = props.getProperty("pass" + bi, dpass);
      String params = props.getProperty("params" + bi, "");
      String dbdialect = props.getProperty("dbdialect" + bi,ddbdialect);
      all = name != null && url != null && tip != null && user != null && pass != null;
      any = name != null || url != null;
      if (all) vb.add(new BaseDef(name, url, tip, user, pass, params, dbdialect));
    }
    if (!edit && vb.size() == 1)
      setBaseParams((BaseDef) vb.get(0));
    
    if (edit && vb.size() == 0) {
      props.clear();
      FileHandler.loadProperties("restart.properties", props, false);
      String url = props.getProperty("url");
      String tip = props.getProperty("tip");
      String user = props.getProperty("user");
      String pass = props.getProperty("pass");
      String dbdialect = props.getProperty("dbdialect","");
      if (url != null && tip != null && user != null && pass != null)
        vb.add(new BaseDef("Inicijalna", url, tip, user, pass, "", dbdialect));
    }
    
    if (vb.size() > 0) bases.setSelectedIndex(index);
 //   bases.setFont(bases.getFont().deriveFont(Font.ITALIC).deriveFont(18f));
    raShortcutItem.setFancyFont(bases);
    bases.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    bases.setFixedCellHeight(36);
    bases.revalidate();
    AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        hideLater();
        return true;
      }
    });
    AWTKeyboard.registerKeyStroke(this, AWTKeyboard.ENTER, new KeyAction() {
      public boolean actionPerformed() {
        if (bases.getSelectedIndex() >= 0) {
          accept = true;
          hideLater();
          return true;
        }
        return false;
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        hideLater();
      }
    });
    bases.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          accept = true;
          hideLater();
        }
      }
      public void mousePressed(MouseEvent e) {
        if (edit && e.isPopupTrigger())
          showPopup(e.getX(), e.getY());
      }
      public void mouseReleased(MouseEvent e) {
        if (edit && e.isPopupTrigger())
          showPopup(e.getX(), e.getY());
      }
    });
    bases.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList l, Object val,
          int row, boolean sel, boolean focus) {
        return super.getListCellRendererComponent(l, val, row, sel, sel);
      }
    });
    if (edit) {
      pop.add(pope);
      pope.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          editRow();
        }
      });
      pop.addSeparator();
      pop.add(popmu);
      popmu.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          moveRowUp();
        }
      });
      pop.add(popmd);
      popmd.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          moveRowDown();
        }
      });
      pop.addSeparator();
      pop.add(popa);
      popa.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          addRow();
        }
      });    
      pop.add(popd);
      popd.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          deleteRow();
        }
      });
      pop.addSeparator();
      pop.add(popi);
      popi.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          selectInitFile();
        }
      });
    }
    this.getContentPane().setLayout(new BorderLayout());
    JScrollPane jsp = new JScrollPane(bases,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    bases.setVisibleRowCount(Math.min(vb.size(), 10));
    bases.scrollRectToVisible(bases.getCellBounds(index, index));
    this.getContentPane().add(jsp);
    this.pack();
    this.setLocationRelativeTo(null);
  }
  
  public static boolean showInstance(boolean edit) {
    raDbaseChooser dbc = new raDbaseChooser(edit);
    if (dbc.vb.size() == 0 || (dbc.vb.size() == 1 && !edit)) return true;
    
    dbc.setVisible(true);
    dbc.saveChanges();
    if (dbc.accept && dbc.initFile != null)
      raDbaseCreator.initFrom(dbc.initFile);
    return dbc.accept;
  }
  public static boolean initFrom() {
    raDbaseChooser dbc = new raDbaseChooser(true);
    dbc.selectInitFile();
    if (dbc.accept && dbc.initFile != null)
      raDbaseCreator.initFrom(dbc.initFile);
    return dbc.accept;
  }
  
  private void setBaseParams(BaseDef bd) {
    IntParam.setBaseParams(bd.url, bd.pass, bd.tip, bd.user, bd.dbdialect);
    if (bd.params != null && bd.params.length() > 0) {
      String[] params = new VarStr(bd.params).split();
      String[] args = start.runtimeArgs;
      start.runtimeArgs = new String[params.length + args.length];
      System.arraycopy(params, 0, start.runtimeArgs, 0, params.length);
      System.arraycopy(args, 0, start.runtimeArgs, params.length, args.length);
    }
  }
  
  private void showPopup(int x, int y) {
    bases.setSelectedIndex(bases.locationToIndex(new Point(x, y)));
    popmu.setEnabled(bases.getSelectedIndex() > 0 && vb.size() > 1);
    popmd.setEnabled(bases.getSelectedIndex() >= 0 &&
        bases.getSelectedIndex() < vb.size() - 1 && vb.size() > 1);
    popa.setEnabled(vb.size() < 32);
    pope.setEnabled(bases.getSelectedIndex() >= 0 && vb.size() > 0);
    popd.setEnabled(bases.getSelectedIndex() >= 0 && vb.size() > 1);
    pop.show(bases, x, y);
  }
  
  BaseDef getParams(String title) {
    return cpan.getBaseDef(this, title, (BaseDef) bases.getSelectedValue());
  }
  
  void selectInitFile() {
    if (JOptionPane.showConfirmDialog(this,
        new raMultiLineMessage("Ova naredba �e izbrisati " +
          "odabranu bazu i napuniti je svje�im podacima.\n" +
          "Operacija je nepovratna. Jeste li sigurni?"), 
          "Inicijalizacija baze", JOptionPane.OK_CANCEL_OPTION) 
              != JOptionPane.OK_OPTION)
      return;
    
    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    if (fc.showOpenDialog(this) != fc.APPROVE_OPTION) return;
    
    File sel = fc.getSelectedFile();
    if (!sel.exists()) {
      JOptionPane.showMessageDialog(this, "Neispravna datoteka " +
      		"za inicijalizaciju!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
      return;
    } else if (sel.isDirectory()) {
      String[] files = sel.list();
      boolean dat = false, def = false;
      for (int i = 0; i < files.length && !(dat && def); i++) {
        if (!dat && files[i].endsWith(".dat")) dat = true;
        if (!def && files[i].endsWith(".def")) def = true;
      }
      if (!dat || !def) {
        JOptionPane.showMessageDialog(this, "U odabranom direktoriju " +
        	"nema .dat i .def datoteka!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
        return;
      }
    } else {
      try {
        ZipInputStream zin = new ZipInputStream(
          new BufferedInputStream(new FileInputStream(sel)));
        try {
          ZipEntry entry;
          boolean dat = false, def = false;
          while ((entry = zin.getNextEntry()) != null && !(dat && def)) {
            if (!dat && entry.getName().endsWith(".dat")) dat = true;
            if (!def && entry.getName().endsWith(".def")) def = true;
          }
          if (!dat || !def) {
            JOptionPane.showMessageDialog(this, "U odabranom ZIP datoteci " +
                "nema .dat i .def datoteka!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
            return;
          }
        } catch (IOException e) {
          JOptionPane.showMessageDialog(this, "Neispravna ZIP datoteka " +
              "za inicijalizaciju!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
          return;
        } finally {
          try {
            zin.close();
          } catch (IOException e) {
            // empty
          }
        }
      } catch (Exception ze) {
        JOptionPane.showMessageDialog(this, "Neispravna ZIP datoteka " +
            "za inicijalizaciju!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
        return;
      }
    }
    
    initFile = sel;
    accept = true;
    hideLater();
  }
  
  void redraw(int pos) {
    bases.updateUI();
    bases.revalidate();
    bases.repaint();
    bases.setSelectedIndex(pos);
    changed = true;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        pack();
      }
    });
  }
  
  void addRow() {
    BaseDef bd = getParams("Dodavanje nove baze");
    if (bd != null) {
      int pos = bases.getSelectedIndex();
      vb.add(pos + 1, bd);
      bases.setVisibleRowCount(Math.min(vb.size(), 10));
      redraw(pos + 1);
    }
  }
  
  void editRow() {
    BaseDef bd = getParams("Izmjena parametara baze");
    if (bd != null) {
      int pos = bases.getSelectedIndex();
      vb.set(pos, bd);
      redraw(pos);
    }
  }
  
  void moveRowUp() {
    int pos = bases.getSelectedIndex();
    Object tmp = vb.remove(pos);
    vb.add(pos - 1, tmp);
    redraw(pos);
  }
  
  void moveRowDown() {
    int pos = bases.getSelectedIndex();
    Object tmp = vb.remove(pos);
    vb.add(pos + 1, tmp);
    redraw(pos);
  }
  
  void deleteRow() {
    if (JOptionPane.showConfirmDialog(this, "Obrisati bazu '" +
        ((BaseDef) bases.getSelectedValue()).name + "' ?", "Brisanje",
        JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
      return;
    
    int pos = bases.getSelectedIndex();
    vb.remove(bases.getSelectedIndex());
    if (pos >= vb.size()) --pos;
    bases.setVisibleRowCount(Math.min(vb.size(), 10));
    redraw(pos);
  }
  
  void hideLater() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        hideInstance();
      }
    });
  }
  
  void saveChanges() {
    if (!changed && index == bases.getSelectedIndex()) return;
    
    Properties props = new Properties();
    if (dtip != null) props.setProperty("tip", dtip);
    if (duser != null) props.setProperty("user", duser);
    if (dpass != null) props.setProperty("pass", dpass);
    
    for (int i = 0; i < vb.size(); i++) {
      BaseDef bd = (BaseDef) vb.get(i);
      props.setProperty("name" + (i + 1), bd.name);
      props.setProperty("url" + (i + 1), bd.url);
      if (!bd.tip.equals(dtip))
        props.setProperty("tip" + (i + 1), bd.tip);
      if (!bd.user.equals(duser))
        props.setProperty("user" + (i + 1), bd.user);
      if (!bd.pass.equals(dpass))
        props.setProperty("pass" + (i + 1), bd.pass);
      if (bd.params != null && bd.params.length() > 0)
        props.setProperty("params" + (i + 1), bd.params);
      if (bd.dbdialect != null && bd.dbdialect.length() > 0)
        props.setProperty("dbdialect" + (i + 1), bd.dbdialect);
    }
    
    props.setProperty("index", Integer.toString(bases.getSelectedIndex()));
    
    FileHandler.storeProperties("base.properties", props, false);
  }
  
  void hideInstance() {
    if (accept) setBaseParams((BaseDef) bases.getSelectedValue());
    AWTKeyboard.unregisterComponent(this);
    dispose();
  }
  
  static class BaseDef {
    String name, url, tip, user, pass, params, dbdialect;
    
    public BaseDef(String n, String u, String t, String s, String p, String a, String d) {
      name = n.trim();
      url = u.trim();
      tip = t.trim();
      user = s.trim();
      pass = p.trim();
      params = a.trim();
      dbdialect = d.trim();
    }
    
    public boolean test() {
      try {
         Class.forName(tip);
      } catch (ClassNotFoundException e) {
          e.printStackTrace();
          return false;
      }
      try {
        DriverManager.getConnection(url, user, pass);
      } catch(SQLException e) {
        e.printStackTrace();
        return false ;
      }
      return true;
    }
    
    public String toString() {
      return "    " + name + "             ";
    }
  }
  
  static class ConnectionPanel extends JPanel {
    
    private JDialog dlg;
    
    private JTextField name = new JTextField();
    private JTextField url = new JTextField();
    private JTextField tip = new JTextField();
    private JTextField user = new JTextField();
    private JTextField pass = new JTextField();
    private JTextField params = new JTextField();
    private JTextField dbdialect = new JTextField();
    
    BaseDef ret;
    
    private AcceptPanel ap = new AcceptPanel() {
      public void okPress() {
        getValues();
      }
    
      public void cancelPress() {
        hideDialog();
      }
    };
    
    public ConnectionPanel() {
      XYLayout lay = new XYLayout();
      lay.setHeight(180);
      lay.setWidth(555);
      setLayout(lay);
      add(new JLabel("Ime"), new XYConstraints(5, 10, -1, -1));
      add(name, new XYConstraints(50, 10, 500, -1));
      add(new JLabel("Args"), new XYConstraints(5, 35, -1, -1));
      add(params, new XYConstraints(50, 35, 500, -1));
      add(new JLabel("Url"), new XYConstraints(5, 60, -1, -1));
      add(url, new XYConstraints(50, 60, 500, -1));
      add(new JLabel("Tip"), new XYConstraints(5, 85, -1, -1));
      add(tip, new XYConstraints(50, 85, 500, -1));
      add(new JLabel("Dialect"), new XYConstraints(5, 110, -1, -1));
      add(dbdialect, new XYConstraints(50, 110, 500, -1));
      add(new JLabel("User"), new XYConstraints(5, 135, -1, -1));
      add(user, new XYConstraints(50, 135, 150, -1));
      add(new JLabel("Pass"), new XYConstraints(355, 135, -1, -1));
      add(pass, new XYConstraints(400, 135, 150, -1));
      

      FocusListener foc = new FocusAdapter() {
        public void focusLost(FocusEvent e) {
          if (e.getSource() instanceof JTextField) {
            JTextField tf = (JTextField) e.getSource();
            if (tf.getSelectionEnd() > tf.getSelectionStart())
              tf.select(0, 0);
          }
        }
      };
      name.addFocusListener(foc);
      url.addFocusListener(foc);
      tip.addFocusListener(foc);
      user.addFocusListener(foc);
      pass.addFocusListener(foc);
      params.addFocusListener(foc);
      dbdialect.addFocusListener(foc);
    }
    
    public BaseDef getBaseDef(JDialog parent, String title, BaseDef vals) {      
      setValues(vals);
      ret = null;
      dlg = new JDialog(parent, title, true);
      dlg.getContentPane().setLayout(new BorderLayout());
      dlg.getContentPane().add(this);
      dlg.getContentPane().add(ap, BorderLayout.SOUTH);
      dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      dlg.pack();
      dlg.setLocationRelativeTo(parent);
      dlg.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          hideDialog();
        }
      });
      ap.registerKeys(dlg);
      dlg.setVisible(true);
      return ret;
    }
    
    void hideDialog() {
      if (dlg != null) {
        AWTKeyboard.unregisterComponent(dlg);
        dlg.dispose();
      }
    }
    
    void setValues(BaseDef vals) {
      name.setText(vals.name);
      url.setText(vals.url);
      tip.setText(vals.tip);
      user.setText(vals.user);
      pass.setText(vals.pass);
      params.setText(vals.params);
      dbdialect.setText(vals.dbdialect);
    }

    void getValues() {
      ret = new BaseDef(name.getText(), url.getText(),
          tip.getText(), user.getText(), pass.getText(), params.getText(), dbdialect.getText());
      
      if (!ret.test()) {
        JOptionPane.showMessageDialog(this, "Gre�ka u postavkama baze!",
            "Gre�ka", JOptionPane.ERROR_MESSAGE);
        ret = null;
      } else hideDialog();
    }
  }
  
  static abstract class AcceptPanel extends JPanel {
    
    private JPanel left = new JPanel();
    
    private JButton prekid = new JButton();
    private JButton ok = new JButton();
    
    SharedFlag ticket = new SharedFlag();
    
    ActionExecutor execOK = new ActionExecutor(ticket) {
        public void run() {
            okPress();
        }
    };
    
    ActionExecutor execPrekid = new ActionExecutor(ticket) {
        public void run() {
            cancelPress();
        }
    };
    
    public AcceptPanel() {
      left.setMinimumSize(new Dimension(70, 27));
      left.setPreferredSize(new Dimension(200, 27));
      left.setLayout(new GridLayout());
      prekid.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            execPrekid.invoke();
        }
      });
      prekid.setText("Prekid");
      prekid.setMaximumSize(new Dimension(51, 27));
      prekid.setMinimumSize(new Dimension(51, 27));
      prekid.setPreferredSize(new Dimension(51, 27));
      prekid.setIcon(raImages.getImageIcon(raImages.IMGCANCEL));
      ok.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
            execOK.invoke();
        }
      });
//      jBOK.setSelected(true);
      ok.setText("OK");
      ok.setMaximumSize(new Dimension(69, 27));
      ok.setMinimumSize(new Dimension(69, 27));
      ok.setPreferredSize(new Dimension(69, 27));
      ok.setIcon(raImages.getImageIcon(raImages.IMGOK));

      this.setLayout(new BorderLayout());
      this.add(left, BorderLayout.EAST);
      left.add(ok);
      left.add(prekid);
    }
    
    public void registerKeys(Component comp) {
//    comp.addKeyListener(OKPanelKeyListener);
      AWTKeyboard.registerKeyStroke(comp, AWTKeyboard.F10, new KeyAction() {
        public boolean actionPerformed() {
          if (!ok.isShowing() || !ok.isEnabled()) return false;
          execOK.invokeLater();
          return true;
        }
      });
      AWTKeyboard.registerKeyStroke(comp, AWTKeyboard.ENTER, new KeyAction() {
        public boolean actionPerformed() {
          if (!ok.isShowing() || !ok.isEnabled()) return false;
          AWTKeyboard.ignoreKeyRelease(AWTKeyboard.ENTER);
          execOK.invokeLater();
          return true;
        }
      });
      AWTKeyboard.registerKeyStroke(comp, AWTKeyboard.ESC, new KeyAction() {
        public boolean actionPerformed() {
          if (!prekid.isShowing() || !prekid.isEnabled()) return false;
          execPrekid.invokeLater();   
          return true;
        }
      });
    }
    
    public abstract void okPress();
      
    public abstract void cancelPress();
  }
}
