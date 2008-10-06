/****license*****************************************************************
**   file: raSectionDesigner.java
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
package hr.restart.util.reports;

import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraFrame;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.VarStr;
import hr.restart.util.raFileFilter;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raSectionDesigner {

  static raSectionDesigner inst = new raSectionDesigner();

  private JDialog dlg;

  private JPanel content = new JPanel(new BorderLayout());

  private StorageDataSet clipboard;

  private JLabel box = new JLabel();
  private JPanel main = new JPanel() {
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      paintLines(g);
    }
  };

  private JPopupMenu prop = new JPopupMenu();

  private boolean drag;
  private Point dp;

  private JraComboBox fonts = new JraComboBox();
  private JraComboBox sizes = new JraComboBox();
  
  private raNavAction edit, delete;
  private Action copy, cut, paste, pastePos, pasteEl;


//  private ArrayList op = new ArrayList();
  private float zoom = 1.0f;

//  private Runnable onExit = null;
  private boolean accepted, toSave, saveIcon;

  AffineTransform ptop;
  int hposLeftMargin, hposRightMargin, hposEdge, oldHeight;
  DataSet ds, head;

  raNavBar nave = new raNavBar(raNavBar.EMPTY);
  raNavBar navf = new raNavBar(raNavBar.EMPTY);
//  raNavBar nava = new raNavBar(raNavBar.EMPTY);
  raNavBar navesc = new raNavBar(raNavBar.EMPTY);
  raNavBar navok = new raNavBar(raNavBar.EMPTY);
  raNavAction nas;

  private raSectionDesigner() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  static int getLeftMargin() {
    return inst.hposLeftMargin;
  }

  static int getRightMargin() {
    return inst.hposRightMargin;
  }

  static int getHeight() {
    return inst.main.getHeight();
  }

  public static boolean isAccepted() {
    return inst.accepted;
  }

  public static boolean isSaveRequested() {
    return inst.toSave;
  }

//  public static boolean show(Window owner) {
//    return show(owner, null, null);
//  }

  public static boolean show(Window owner, String title, DataSet sect, boolean saveIcon) {
    return inst.showInstance(owner, title, null, sect, saveIcon);
  }

  public static boolean show(Window owner, String title, DataSet sect) {
    return inst.showInstance(owner, title, null, sect, false);
  }

  public static boolean show(Window owner, String title, DataSet head, DataSet sect) {
    return inst.showInstance(owner, title, head, sect, false);
  }

  private boolean showInstance(Window owner, String title, DataSet head, DataSet sect, boolean saveIcon) {

    if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, title, true);
    else if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, title, true);
    else dlg = new JraDialog((Frame) null, title, true);

    this.saveIcon = saveIcon;
    toSave = false;
    navok.removeOption(nas);
    if (saveIcon) navok.addOption(nas, 0);

    dlg.setContentPane(content);
//    dlg.getFontMetrics(dlg.getFont())
    if (sect != null) {
      setDataSets(head, sect);
      inputElements();
    }
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        hide(false);
      }
    });
    nave.registerNavBarKeys(dlg);
    navf.registerNavBarKeys(dlg);
    dlg.setLocationRelativeTo(owner);
    Element.selectionChanged();
    dlg.show();
    return accepted;
  }

//  public void runOnExit(Runnable r) {
//    onExit = r;
//  }

  private void jbInit() throws Exception {
    main.setLayout(null);
    main.setBackground(new JTextField().getBackground());
    main.setBorder(BorderFactory.createLoweredBevelBorder());
    initMain();

    clipboard = hr.restart.baza.Logodat.getDataModule().getTempSet("1=0");
    clipboard.open();

//    fonts.addkey

    box.setBorder(BorderFactory.createLineBorder(Color.gray));
//    initChg();
    initNavBars();
    initPopup();
    initFonts();

    Element.setOwner(main);
    Element.setAutoAlign(8);

    JPanel header = new JPanel();
    FlowLayout hf = (FlowLayout) header.getLayout();
    hf.setHgap(0);
    hf.setVgap(0);
    hf.setAlignment(hf.LEFT);
    header.add(nave);
    header.add(navesc);
    header.add(Box.createHorizontalStrut(10));
    header.add(fonts);
    header.add(Box.createHorizontalStrut(5));
    header.add(sizes);
    header.add(Box.createHorizontalStrut(5));
    header.add(navf);
//    header.add(Box.createHorizontalStrut(5));
//    header.add(nava);
    JPanel headerall = new JPanel(new BorderLayout());
    headerall.add(header, BorderLayout.WEST);
    headerall.add(navok, BorderLayout.EAST);

    content.add(main, BorderLayout.CENTER);
    content.add(headerall, BorderLayout.NORTH);
//    this.pack();
//    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

//    startFrame.getStartFrame().getGraphics().getfon
    ptop = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
        getDefaultConfiguration().getDefaultTransform();

//    ptop = ((Graphics2D) this.getWindow().getGraphics())
//           .getFontRenderContext().getTransform();
    Point p = new Point();
    ptop.transform(new Point((int) (21 / 2.54 * 72), 0), p);
    hposEdge = p.x;
    ptop.transform(new Point((int) (1 / 2.54 * 72), 0), p);
    hposLeftMargin = p.x;
    ptop.transform(new Point((int) (20 / 2.54 * 72), 0), p);
    hposRightMargin = p.x;
    main.setPreferredSize(new Dimension(hposEdge, 200));

//    JraKeyListener kl = new JraKeyListener();
//    kl.setTransferAllKeysToParent(true);
//
//    fonts.addKeyListener(kl);
//    sizes.addKeyListener(kl);

//    System.err.println(fonts.getInputMap());


//    System.err.println(VarStr.join(fonts.getInputMap().keys(), ';'));

/*    fonts.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        System.err.println(e);
      }

      public void keyPressed(KeyEvent e) {
        System.err.println(e);
      }

      public void keyReleased(KeyEvent e) {
        System.err.println(e);
      }
    }); */

//    this.addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent e) {
//        if (checkBounds()) raSectionDesigner.this.hide(false);
//      }
//    });
  }

  private int lpx, lpy;

  private void initMain() {
    main.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.isPopupTrigger())
          prop.show(main, lpx = e.getX(), lpy = e.getY());
        Element.clearSelection();
        main.repaint();
      }

      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
          prop.show(main, lpx = e.getX(), lpy = e.getY());
        if ((e.getModifiers() & e.BUTTON1_MASK) == 0) return;
        dp = e.getPoint();
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
          prop.show(main, lpx = e.getX(), lpy = e.getY());
        if ((e.getModifiers() & e.BUTTON1_MASK) == 0) return;
        if (dp != null && !main.isAncestorOf(box)) {
          Element.clearSelection();
          main.repaint();
        }
        if (main.isAncestorOf(box)) {
          Rectangle b = box.getBounds();
          Rectangle r = new Rectangle();
          if (e.isShiftDown())
            for (int i = 0; i < Element.count(); i++) {
              Element l = Element.get(i);
              l.getBounds(r);
              if (b.intersects(r)) l.toggle(true);
            }
          else
            for (int i = 0; i < Element.count(); i++) {
              Element l = Element.get(i);
              l.getBounds(r);
              if (b.intersects(r) ^ l.isSelected()) l.toggle(true);
            }
          main.remove(box);
          main.repaint();
        }
        dp = null;
        drag = false;
      }

      public void mouseEntered(MouseEvent e) {
      }

      public void mouseExited(MouseEvent e) {
      }
    });
    main.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        if (!drag && dp != null && dp.distance(e.getPoint()) > 4) {
          drag = true;
          main.add(box, 0);
        }
        if (drag)
          box.setBounds(Math.min(e.getX(), dp.x), Math.min(e.getY(), dp.y),
                        Math.abs(e.getX() - dp.x), Math.abs(e.getY() - dp.y));
      }

      public void mouseMoved(MouseEvent e) {
      }
    });
  }

  private void initNavBars() {
    nave.addOption(new raNavAction("Novi element", raImages.IMGADD, KeyEvent.VK_F2) {
      public void actionPerformed(ActionEvent e) {
        Element el = new Element();
        el.setTextual("Element " + Element.count());
        addElement(el, true);
      }
    });
    nave.addOption(edit = new raNavAction("Izmjena elementa", raImages.IMGCHANGE, KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        if (Element.countSelected() > 0) {
          if (ElementDialog.show(dlg, Element.getSelected(0)) != null) {
            if (Element.getSelected(0).isText())
              setTextFont(Element.getSelected(0));
          };
        }
      }
    });
    nave.addOption(delete = new raNavAction("Brisanje elementa", raImages.IMGDELETE, KeyEvent.VK_F3){
      public void actionPerformed(ActionEvent e) {
        if (Element.countSelected() > 0) {
          Element.deleteSelection();
          main.repaint();
        }
      }
    });
    copy = new AbstractAction("Kopiraj") {
      public void actionPerformed(ActionEvent e) {
        copyAll();
      }
    };
    cut = new AbstractAction("Izreži") {
      public void actionPerformed(ActionEvent e) {
        cutAll();
      }
    };
    paste = new AbstractAction("Umetni") {
      public void actionPerformed(ActionEvent e) {
        pasteAll();
      }
    };
    pastePos = new AbstractAction("Umetni") {
      public void actionPerformed(ActionEvent e) {
        pasteAll(lpx, lpy);
      }
    };
    pasteEl = new AbstractAction("Umetni") {
      public void actionPerformed(ActionEvent e) {
        pasteAll(Element.lpx, Element.lpy);
      }
    };
    InputMap anc = content.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    anc.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "ra-delete-elem");
    anc.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), "ra-copy");
    anc.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK), "ra-cut");
    anc.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK), "ra-paste");
    content.getActionMap().put("ra-delete-elem", delete);
    content.getActionMap().put("ra-copy", copy);
    content.getActionMap().put("ra-cut", cut);
    content.getActionMap().put("ra-paste", paste);
    copy.setEnabled(false);
    cut.setEnabled(false);
    paste.setEnabled(false);
    pastePos.setEnabled(false);
    pasteEl.setEnabled(false);
    Element.setCutAndPasteActions(cut, copy, pasteEl);

    navesc.addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        raSectionDesigner.this.hide(false);
      }
    });
    navf.addOption(new raNavAction("Manji font", raImages.IMGZOOMOUT, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Element.countSelected(); i++) {
          Element l = Element.getSelected(i);
          if (l.getFont().getSize2D() >= 9f)
            l.changeFontSize(l.getFont().getSize2D() - 1);
        }
        Element.selectionChanged();
      }
    });
    navf.addOption(new raNavAction("Ve\u0107i font", raImages.IMGZOOMIN, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Element.countSelected(); i++) {
          Element l = Element.getSelected(i);
          if (l.getFont().getSize2D() <= 31f)
            l.changeFontSize(l.getFont().getSize2D() + 1);
        }
        Element.selectionChanged();
      }
    });
    navf.addOption(new raNavAction("Normal", raImages.IMGNORMAL, KeyEvent.VK_N) {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Element.countSelected(); i++) {
          Element.getSelected(i).changeFontStyle(Font.PLAIN);
        }
      }
    });
    navf.addOption(new raNavAction("Bold", raImages.IMGBOLD, KeyEvent.VK_B) {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Element.countSelected(); i++) {
          Element l = Element.getSelected(i);
          l.changeFontStyle(l.getFont().getStyle() ^ Font.BOLD);
        }
      }
    });
    navf.addOption(new raNavAction("Kurziv", raImages.IMGITALIC, KeyEvent.VK_I) {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Element.countSelected(); i++) {
          Element l = Element.getSelected(i);
          l.changeFontStyle(l.getFont().getStyle() ^ Font.ITALIC);
        }
      }
    });
    navok.addOption(new raNavAction("Prihvati", raImages.IMGOK, KeyEvent.VK_F10) {
      public void actionPerformed(ActionEvent e) {
        raSectionDesigner.this.hide(true);
      }
    });
    nas = new raNavAction("Prihvati za stalno", raImages.IMGSAVE, KeyEvent.VK_S, KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        checkForSave();
      }
    };
/*    nava.addOption(new raNavAction("Poravnaj", raImages.IMGITALIC, KeyEvent.VK_I) {
      public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < Element.countSelected(); i++) {
          Element l = Element.getSelected(i);
          l.changeFontStyle(l.getFont().getStyle() ^ Font.ITALIC);
        }
      }
    });*/

//    nave.registerNavBarKeys(this);
//    navf.registerNavBarKeys(this);
  }

  private void initPopup() {
    JMenuItem jmiText = new JMenuItem("Dodaj element...");

    prop.add(jmiText);
    jmiText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Element el = ElementDialog.show(dlg, null);
        if (el != null) addElement(el, false);
      }
    });
    prop.addSeparator();
    prop.add(cut);
    prop.add(copy);
    prop.add(pastePos);
    prop.pack();
  }

  private class FontListCellRenderer extends DefaultListCellRenderer {
//    private boolean disableCC = true;
    Border emptyBorder = BorderFactory.createEmptyBorder(1, 6, 1, 1);
    public Component getListCellRendererComponent(JList l, Object v, int idx, boolean sel, boolean foc) {
      super.getListCellRendererComponent(l, v, idx, sel && (idx >= 0), /*disableCC = */false);
//      disableCC = true;
      if (idx >= 0)
        this.setFont(Font.decode(v.toString()));
      this.setBorder(emptyBorder);
      return this;
    }
/*      public void setBackground(Color c) {
      if (!disableCC) super.setBackground(c);
    }
    public void setForeground(Color c) {
      if (!disableCC) super.setForeground(c);
    } */

  }

  private void initFonts() {
    String[] families =
        GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    fonts.setModel(new DefaultComboBoxModel(families));
    fonts.setSelectedItem("Lucida Bright");
//    fonts.setEditable(true);
    fonts.setPreferredSize(new Dimension(150, new JTextField().getPreferredSize().height));
//    fonts.setEditable(true);

    fonts.setRenderer(new FontListCellRenderer());
    sizes.setModel(new DefaultComboBoxModel(new Object[] {
      "6", "7", "8", "9", "10", "11", "12", "14", "16", "18", "20", "24", "32"
    }));
    sizes.setSelectedItem("9");
    sizes.setEditable(true);
    sizes.setPreferredSize(new Dimension(50, new JTextField().getPreferredSize().height));
    sizes.setRenderer(new DefaultListCellRenderer() {
      Border emptyBorder = BorderFactory.createEmptyBorder(1, 6, 1, 1);
      public Component getListCellRendererComponent(JList l, Object v, int idx, boolean sel, boolean foc) {
        super.getListCellRendererComponent(l, v, idx, sel && (idx >= 0), false);
        this.setBorder(emptyBorder);
        return this;
      }
    });
    fonts.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED)
          fontSelected(e.getItem());
//        System.out.println("ITEM STATE  " + e.getItem() + "  " + e.getStateChange());
      }
    });
    sizes.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED)
          sizeSelected(e.getItem());
//        System.out.println(e);
//        System.out.println(e.getItem() + "  " + e.getStateChange());
      }
    });
/*    fonts.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("ACTION " + e.getActionCommand() + "  " + e.getModifiers());

      }
    });*/

    InputMap imf = fonts.getInputMap(fonts.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    InputMap ims = sizes.getInputMap(sizes.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    ActionMap amf = fonts.getActionMap();
    ActionMap ams = sizes.getActionMap();
    KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    KeyStroke f10 = KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0);

//    final Action fontsEscOld, sizesEscOld;
//    fontsEscOld = amf.get(imf.get(esc));
//    sizesEscOld = ams.get(ims.get(esc));
//    Object[] ik = imf.allKeys();
//    Object[] ak = amf.allKeys();
//    System.err.println(ik.length);
//    System.err.println(ak.length);
//    System.err.println(VarStr.join(ik,','));
//    System.err.println(VarStr.join(ak,','));
//    System.err.println(fontsEscOld);
//    System.err.println(sizesEscOld);
//    System.err.println(imf);
//    System.err.println(ims);

    Action fontsEsc = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (fonts.isPopupVisible()) fonts.hidePopup();
        else raSectionDesigner.this.hide(false);
      }
    };
    Action fontsF10 = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (fonts.isPopupVisible()) fonts.hidePopup();
        raSectionDesigner.this.hide(true);
      }
    };
    Action sizesEsc = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (sizes.isPopupVisible()) sizes.hidePopup();
        else raSectionDesigner.this.hide(true);
      }
    };
    Action sizesF10 = new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (sizes.isPopupVisible()) sizes.hidePopup();
        raSectionDesigner.this.hide(true);
      }
    };
    if (imf.get(esc) == null) imf.put(esc, "Esc-press-RA");
    if (ims.get(esc) == null) ims.put(esc, "Esc-press-RA");
    amf.put(imf.get(esc), fontsEsc);
    ams.put(ims.get(esc), sizesEsc);
    imf.put(f10, "F10-press-RA");
    ims.put(f10, "F10-press-RA");
    amf.put(imf.get(f10), fontsF10);
    ams.put(ims.get(f10), sizesF10);

    Element.setCallback(new Runnable() {
      public void run() {
        Element e = null;
        int num = 0;
        for (int i = 0; i < Element.countSelected(); i++)
          if (Element.getSelected(i).isText()) {
            ++num;
            e = Element.getSelected(i);
          }
        if (num == 1)
          fixFontCombos(e);
        edit.setEnabled(Element.countSelected() == 1);
        delete.setEnabled(Element.countSelected() > 0);
        cut.setEnabled(num > 0);
        copy.setEnabled(num > 0);
        paste.setEnabled(clipboard.rowCount() > 0);
        pastePos.setEnabled(clipboard.rowCount() > 0);
        pasteEl.setEnabled(clipboard.rowCount() > 0);
      }
    });
  }

  private void copyAll() {
    if (Element.countSelected() > 0) {
      clipboard.empty();
      for (int i = 0; i < Element.countSelected(); i++) {
        clipboard.insertRow(false);
        outputElement(Element.getSelected(i), clipboard);
      }
      paste.setEnabled(true);
      pastePos.setEnabled(true);
      pasteEl.setEnabled(true);
    }
  }

  private void cutAll() {
    copyAll();
    Element.deleteSelection();
    main.repaint();
  }

  private void pasteAll() {
    Element.deleteSelection();
    for (clipboard.first(); clipboard.inBounds(); clipboard.next())
      inputElement(clipboard);
    main.repaint();
  }

  private void pasteAll(int dx, int dy) {
    Element.deleteSelection();
    int minx = -1, miny = -1;
    for (clipboard.first(); clipboard.inBounds(); clipboard.next()) {
      if (minx == -1 || clipboard.getInt("HPOS") < minx)
        minx = clipboard.getInt("HPOS");
      if (miny == -1 || clipboard.getInt("VPOS") < miny)
        miny = clipboard.getInt("VPOS");
    }
    for (clipboard.first(); clipboard.inBounds(); clipboard.next()) {
      Element el = inputElement(clipboard);
      el.setLocation(el.getX() - minx - hposLeftMargin + dx, el.getY() - miny + dy);
    }
    main.repaint();
  }

  private void fontSelected(Object font) {
    int idx = ((DefaultComboBoxModel) fonts.getModel()).getIndexOf(font);
    if (idx >= 0) {
      if (fonts.getSelectedIndex() != idx) fonts.setSelectedIndex(idx);
      else Element.changeSelectionFont(Font.decode((String) font));
    } //else if (sel.size() == 0) fonts.setSelectedIndex(0);
//    else fonts.setSelectedIndex(((DefaultComboBoxModel) fonts.getModel()).getIndexOf((((Element) sel.getLast()).item.getFont().getFamily())));
  }

  private void sizeSelected(Object size) {
    int idx = ((DefaultComboBoxModel) sizes.getModel()).getIndexOf(size);
    float sz = 0f;
    try {
      sz = (float) Math.round(Float.parseFloat(size.toString()));
    } catch (Exception e) {}
    if (idx >= 0 || sz >= 6f && sz <= 32f) {
      Element.changeSelectionSize(sz);

    } //else if (sel.size() == 0) sizes.setSelectedIndex(0);
//    else sizes.setSelectedItem(String.valueOf(Math.round(((Element) sel.getLast()).item.getFont().getSize2D())));
  }

  public void fixFontCombos(Element e) {
    fonts.setSelectedIndex(((DefaultComboBoxModel) fonts.getModel()).getIndexOf((e.getFont().getFamily())));
    sizes.setSelectedItem(String.valueOf(Math.round(e.getFont().getSize2D())));
  }

  private void setPosition(Element e) {
    Rectangle r = new Rectangle();
    Rectangle rc = new Rectangle();
    boolean found = false;

    r.setSize(e.getSize());
    for (int y = 10; !found && y < main.getHeight() - r.height - 10; y += 10)
      for (int x = 10 + hposLeftMargin; !found && x < hposRightMargin - r.width - 10; x += 10) {
        r.x = x;
        r.y = y;
        found = true;
        for (int i = 0; found && i < Element.count(); i++) {
          Element.get(i).getBounds(rc);
          found = !r.intersects(rc);
        }
      }
    if (found) e.setLocation(r.getLocation());
    else e.setLocation((main.getWidth() - e.getWidth()) / 2,
                       (main.getHeight() - e.getHeight()) / 3);
  }

  private void setTextFont(Element elem) {
    float size = 9.0f;
    try {
      size = Float.parseFloat(sizes.getSelectedItem().toString());
//      System.err.println(size);
      elem.changeFont(Font.decode(fonts.getSelectedItem().toString()));
      elem.changeFontSize(size);
    } catch (Exception e) {}
  }

  private void addElement(Element elem, boolean setpos) {
    if (elem.isText()) setTextFont(elem);
    main.add(elem, 0);
    if (setpos) {
      elem.setSize(elem.getPreferredSize());
      setPosition(elem);
    } else elem.setLocation(lpx, lpy);
    elem.toggle(false);
    main.repaint();
  }

  private void paintLines(Graphics g) {
    if (main.getWidth() > hposEdge) {
//      g.setColor(Color.black);
//      g.drawLine(hposEdge, 0, hposEdge, main.getHeight());
      g.setColor(Color.gray);
      g.fillRect(hposEdge, 0, main.getWidth() - 1, main.getHeight());
    }
    if (main.getWidth() > hposRightMargin) {
      g.setColor(Color.gray);
      g.drawLine(hposRightMargin, 0, hposRightMargin, main.getHeight());
    }
    if (main.getWidth() > hposLeftMargin) {
      g.setColor(Color.gray);
      g.drawLine(hposLeftMargin, 0, hposLeftMargin, main.getHeight());
    }
  }

  public boolean checkBounds() {
    Element.clearSelection();
    for (int i = 0; i < Element.count(); i++) {
      Element e = Element.get(i);
      if (e.getX() < hposLeftMargin || e.getX() + e.getWidth() - 1 > hposRightMargin ||
          e.getY() < 0 || e.getY() + e.getHeight() > main.getHeight())
        e.toggle(true);
    }
    if (Element.countSelected() > 0) {
      main.repaint();
      if (JOptionPane.showConfirmDialog(dlg, "Neki elementi nisu unutar"+
        " granica podru\u010Dja ispisivanja!\nObrisati?", "Greška", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
        Element.deleteSelection();
        main.repaint();
        return true;
      } else return false;
    } else return true;
  }

/*  public void setSection(String vrdok, String vrsta) {
    this.vrdok = vrdok;
    this.vrsta = vrsta;
  }*/

  private String corg, vrsta, vrsec, vrdok;

  public static void setKey(String corg, String vrsta, String vrsec, String vrdok) {
    inst.corg = corg;
    inst.vrsta = vrsta;
    inst.vrsec = vrsec;
    inst.vrdok = vrdok;
  }

  public void outputElement(Element e, DataSet ds) {
    if (e.isText()) {
      ds.setString("TEKST", e.getText());
      ds.setString("TIP", "T");
      Font f = e.getFont();
      StringBuffer fontdef = new StringBuffer(f.getFamily());
      fontdef.append(';').append(f.getSize2D());
      fontdef.append(f.isBold() ? ";B" : ";N");
      fontdef.append(f.isItalic() ? ";I" : ";N");
      ds.setString("FONT", fontdef.toString());
      ds.setInt("SIRINA", e.getWidth());
      ds.setInt("VISINA", e.getHeight());
      if (e.getHorizontalAlignment() == SwingConstants.LEADING)
        ds.setString("ALIGN", "L");
      else if (e.getHorizontalAlignment() == SwingConstants.TRAILING)
        ds.setString("ALIGN", "R");
      else ds.setString("ALIGN", "C");
    } else if (e.isPic()) {
      String fn = "";
      try {
        fn = new File(e.getURL().getFile()).getName();
        } catch (Exception ex) {}
        ds.setString("TEKST", fn);
        ds.setString("TIP", "P");
        ds.setInt("SIRINA", e.getWidth());
        ds.setInt("VISINA", e.getHeight());
        ds.setString("ALIGN", e.isFollow() ? "Z" : "C");
    } else if (e.isHline()) {
      ds.setString("TIP", "H");
      ds.setInt("SIRINA", e.getWidth());
      ds.setInt("VISINA", e.getThick());
    } else if (e.isVline()) {
      ds.setString("TIP", "V");
      ds.setInt("SIRINA", e.getThick());
      ds.setInt("VISINA", e.getHeight());
    }
    ds.setInt("HPOS", e.getX() - hposLeftMargin);
    ds.setInt("VPOS", e.getY());
  }

  public void outputElements() {
    short rbr = 0;
    ds.deleteAllRows();
    if (Element.count() == 0 && !saveIcon) {
      ds.post();
      return;
    }
    DataSet oldHead = head;
    if (head == null) head = ds;
    head.setShort("RBR", (short) 0);
    head.setInt("SIRINA", main.getWidth());
    head.setInt("VISINA", main.getHeight());
    head.setInt("VPOS", oldHeight);
    head.setString("CORG", corg);
    head.setString("VRSTA", vrsta);
    head.setString("VRSEC", vrsec);
    head.setString("VRDOK", vrdok);
//    head.post();
    head = oldHead;
    for (int i = 0; i < Element.count(); i++) {
      Element e = Element.get(i);
      ds.insertRow(false);
      ds.setShort("RBR", ++rbr);
      ds.setString("CORG", corg);
      ds.setString("VRSTA", vrsta);
      ds.setString("VRSEC", vrsec);
      ds.setString("VRDOK", vrdok);
      outputElement(e, ds);
    }
    ds.post();
  }

  public void setDataSets(DataSet head, DataSet ds) {
    this.head = head;
    this.ds = ds;
  }

  public Element inputElement(DataSet ds) {
    Element el;
    if (ds.getString("TIP").equalsIgnoreCase("T")) {
      String[] fontdata = new VarStr(ds.getString("FONT")).split(";");
      Font f = Font.decode(fontdata[0]).deriveFont(Float.parseFloat(fontdata[1]));
      if (fontdata.length > 2 && fontdata[2].equalsIgnoreCase("B"))
        f = f.deriveFont(Font.BOLD);
      if (fontdata.length > 3 && fontdata[3].equalsIgnoreCase("I"))
        f = f.deriveFont(Font.ITALIC);

      el = new Element(Element.TEXT);
      el.setTextual(ds.getString("TEKST"));
      el.setFont(f);
      el.setSize(ds.getInt("SIRINA"), ds.getInt("VISINA"));
      el.setLocation(ds.getInt("HPOS") + hposLeftMargin, ds.getInt("VPOS"));
      if (ds.getString("ALIGN").equalsIgnoreCase("L"))
        el.setHorizontalAlignment(SwingConstants.LEADING);
      else if (ds.getString("ALIGN").equalsIgnoreCase("R"))
        el.setHorizontalAlignment(SwingConstants.TRAILING);
      else el.setHorizontalAlignment(SwingConstants.CENTER);
      main.add(el, 0);
    } else if (ds.getString("TIP").equalsIgnoreCase("P")) {
      el = new Element(Element.PIC);
      java.net.URL url = null;
      try {
        url = Aus.findFileAnywhere(ds.getString("TEKST")).toURL();
        } catch (Exception e) {}
        el.setImage(url);
//        el.setSize(el.getPreferredSize());
        el.setLocation(ds.getInt("HPOS") + hposLeftMargin, ds.getInt("VPOS"));
        el.setFollow(ds.getString("ALIGN").equalsIgnoreCase("Z"));
        el.setSize(ds.getInt("SIRINA"), ds.getInt("VISINA"));
        main.add(el, 0);
    } else if (ds.getString("TIP").equalsIgnoreCase("H")) {
      el = new Element(Element.HLINE);
      el.setHline(ds.getInt("VISINA"));
      el.setSize(ds.getInt("SIRINA"), el.getSize().height);
      el.setLocation(ds.getInt("HPOS") + hposLeftMargin, ds.getInt("VPOS"));
      main.add(el, 0);
    } else {
      el = new Element(Element.VLINE);
      el.setVline(ds.getInt("SIRINA"));
      el.setSize(el.getSize().width, ds.getInt("VISINA"));
      el.setLocation(ds.getInt("HPOS") + hposLeftMargin, ds.getInt("VPOS"));
      main.add(el, 0);
    }
    return el;
  }

  public void inputElements() {
    Element.clearElements();
    main.removeAll();
    main.setSize(hposEdge, 100);
    main.setPreferredSize(main.getSize());
    if (head != null) {
      oldHeight = head.getInt("VPOS");
      main.setSize(head.getInt("SIRINA") == 0 ? hposEdge :
                   head.getInt("SIRINA"), head.getInt("VISINA"));
      main.setPreferredSize(main.getSize());
    }

    for (ds.first(); ds.inBounds(); ds.next()) {
      if (ds.getShort("RBR") == 0) {
        oldHeight = ds.getInt("VPOS");
        main.setSize(ds.getInt("SIRINA") == 0 ? hposEdge :
                   ds.getInt("SIRINA"), ds.getInt("VISINA"));
        main.setPreferredSize(main.getSize());
      } else inputElement(ds);
    }
    dlg.pack();
  }

  private void checkForSave() {
    hide(toSave = true);
    toSave = accepted;
  }

  public void hide(boolean accept) {
    if (accept && !checkBounds()) return;
    accepted = accept;
    if (dlg != null) {
      dlg.hide();
      dlg.dispose();
      dlg = null;
    }
    if (accepted) outputElements();

//    super.hide();
//    if (onExit != null) onExit.run();

  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {}

    JFrame f = new JraFrame();
    show(f, "Pokus", null, null);
//    hr.restart.util.startFrame.getStartFrame().showFrame("hr.restart.util.reports.raSectionDesigner", "Designer");
  }
}

class Element extends JLabel {
  public static final int UNDEFINED = -1;
  public static final int TEXT = 0;
  public static final int PIC = 1;
  public static final int HLINE = 2;
  public static final int VLINE = 3;

  public static final String SELECTION = "selection";

  private static final int HAND = 5;

  private static final int BODY = 0;
  private static final int N_HANDLE = 1;
  private static final int W_HANDLE = 2;
  private static final int S_HANDLE = 3;
  private static final int E_HANDLE = 4;
  private static final int NW_HANDLE = 5;
  private static final int NE_HANDLE = 6;
  private static final int SW_HANDLE = 7;
  private static final int SE_HANDLE = 8;

  private static final Cursor[] resizeCursors = {
    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),
    Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR),
    Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR),
    Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR),
    Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR),
    Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR),
    Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR),
    Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR),
    Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)
  };

  private static Color sel = new Color(10, 10, 150, 30);
  private static Color hand = new Color(50, 200, 0, 200);

  private static JPanel owner;
  private static int alignDist, handle;
  static int lpx, lpy;

  private static boolean drag, resize;
  private static ArrayList selection = new ArrayList();
  private static ArrayList elements = new ArrayList();

  private static Action cut, copy, paste;

  private static Runnable callback;

  static {
    initStaticComponents();
  }

  private Point dp, rp;

  private int type;
  private int thick;
  private boolean selected, focus, follow;
  private Cursor cursor;
  private Image im;

  private java.net.URL url;

  private JPopupMenu prop = new JPopupMenu();

  private static void initStaticComponents() {
//    ta.setPreferredSize(new Dimension(400, 120));
//    tf.setPreferredSize(new Dimension(400, tf.getPreferredSize().height));
  }

  public Element() {
    this(UNDEFINED);
  }

  public Element(int type) {
    this.type = type;
    this.setBorder(null);
    elements.add(this);
    if (type == HLINE) setHline(1);
    if (type == VLINE) setVline(1);
    addMouseListeners();
    initPopup();
  }

  public void delete() {
    selection.remove(this);
    owner.remove(this);
    elements.remove(this);
    selectionChanged();
  }

  public static void setCutAndPasteActions(Action acut, Action acopy, Action apaste) {
    cut = acut;
    copy = acopy;
    paste = apaste;
  }

  public static void setCallback(Runnable r) {
    callback = r;
  }

  public static void selectionChanged() {
    if (callback != null)
      callback.run();
  }

  public static void setOwner(JPanel pan) {
    owner = pan;
  }

  public static void setAutoAlign(int distance) {
    alignDist = distance;
  }

  public static void setSelectedColor(Color c) {
    sel = c;
  }

  public static Color getSelectedColor() {
    return sel;
  }

  public static int count() {
    return elements.size();
  }

  public static Element get(int idx) {
    return (Element) elements.get(idx);
  }

  public static int countSelected() {
    return selection.size();
  }

  public static Element getSelected(int idx) {
    return (Element) selection.get(idx);
  }

  public static void clearSelection() {
    for (Iterator i = selection.iterator(); i.hasNext(); ((Element) i.next()).selected = false);
    selection.clear();
    selectionChanged();
  }

  public static void deleteSelection() {
    while (selection.size() > 0)
      ((Element) selection.get(selection.size() - 1)).delete();
    selectionChanged();
  }

  public static void clearElements() {
    elements.clear();
    selection.clear();
    selectionChanged();
  }

  public static void changeSelectionFont(Font f) {
    for (int i = 0; i < selection.size(); i++)
      ((Element) selection.get(i)).changeFont(f);
  }

  public static void changeSelectionSize(float sz) {
    for (int i = 0; i < selection.size(); i++)
      ((Element) selection.get(i)).changeFontSize(sz);
  }

  public boolean isSelected() {
    return selected;
  }

  public void toggle(boolean add) {
    int old = selection.size();
    if (!add) {
      if (selected) clearSelection();
      else {
        clearSelection();
        selection.add(this);
        selected = true;
      }
    } else {
      if (selected) selection.remove(this);
      else selection.add(this);
      selected = !selected;
    }
    selectionChanged();
  }

  public void changeFont(Font f) {
    if (isText()) {
      boolean resize = getSize().equals(getPreferredSize());
      setFont(f.deriveFont(getFont().getSize2D()).deriveFont(getFont().getStyle()));
      if (resize) setSize(getPreferredSize());
    }
  }

  public void changeFontSize(float sz) {
    if (isText()) {
      boolean resize = getSize().equals(getPreferredSize());
      setFont(getFont().deriveFont(sz));
      if (resize) setSize(getPreferredSize());
    }
  }

  public void changeFontStyle(int style) {
    if (isText()) {
      boolean resize = getSize().equals(getPreferredSize());
      setFont(getFont().deriveFont(style));
      if (resize) setSize(getPreferredSize());
    }
  }

  public void setTextual(String text) {
    boolean resize = (type != TEXT || getSize().equals(getPreferredSize()));
    type = TEXT;
    setText(text);
    setIcon(null);
    setVerticalAlignment(SwingConstants.TOP);
    if (resize) setSize(getPreferredSize());
    if (selected) selectionChanged();
  }

  public void setFollow(boolean yes) {
    follow = yes;
  }

  public void setImage(java.net.URL url) {
    type = PIC;
    if (url != null) {
      ImageIcon icon = new ImageIcon(url);
      im = icon.getImage();
      setIcon(icon);
    } else im = null;

    this.url = url;
    setText(null);
    setSize(getPreferredSize());
    if (selected) selectionChanged();
  }

  public void setHline(int thick) {
    boolean resize = !isHline();
    setText(null);
    setIcon(null);
    type = HLINE;
    this.thick = thick;
    setSize(resize ? 50 : getWidth(), HAND - 1 + thick);
    if (selected) selectionChanged();
  }

  public void setVline(int thick) {
    boolean resize = !isVline();
    setText(null);
    setIcon(null);
    type = VLINE;
    this.thick = thick;
    setSize(HAND - 1 + thick, resize ? 50 : getHeight());
    if (selected) selectionChanged();
  }

  private void addMouseListeners() {
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.isPopupTrigger())
          prop.show(Element.this, lpx = e.getX(), lpy = e.getY());
        if ((e.getModifiers() & e.BUTTON1_MASK) == 0) return;
        if (!selected || getHandle(e.getX(), e.getY()) == BODY) {
          if (e.getClickCount() == 1) {
            toggle(e.isShiftDown());
            owner.repaint();
          } else if (e.getClickCount() == 2) {
            ElementDialog.show((Window) owner.getTopLevelAncestor(), Element.this);
          }
        }
      }

      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
          prop.show(Element.this, lpx = e.getX(), lpy = e.getY());
        if ((e.getModifiers() & e.BUTTON1_MASK) == 0) return;
        handle = getHandle(e.getX(), e.getY());
        if (!selected || handle == BODY)
          dp = e.getPoint();
        else rp = e.getPoint();
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
          prop.show(Element.this, lpx = e.getX(), lpy = e.getY());
        if ((e.getModifiers() & e.BUTTON1_MASK) == 0) return;
        if (dp != null && !drag && !e.getPoint().equals(dp)) {
          toggle(e.isShiftDown());
          owner.repaint();
        } else repaint();
        dp = rp = null;
        drag = resize = false;
        setCursorShape(e.getX(), e.getY());
      }

      public void mouseExited(MouseEvent e) {
        if (dp == null && rp == null) {
          setCursorShape(e.getX(), e.getY());
          if (focus) {
            focus = false;
            repaint();
          }
        }
      }
      public void mouseEntered(MouseEvent e) {
        if (!focus) {
          focus = true;
          repaint();
        }
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        boolean adjust = false;
        if (!drag && dp != null && dp.distance(e.getPoint()) > 4) {
          drag = adjust = true;
//          setCursor(cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
        if (drag) {
          if (selection.size() > 1 && selected) {
            for (Iterator i = selection.iterator(); i.hasNext();) {
              Element l = (Element) i.next();
              l.setLocation(l.getX() + e.getX() - dp.x, l.getY() + e.getY() - dp.y);
            }
//            lp.setLocation(e.getX(), e.getY());
          } else alignLocation(e.getX(), e.getY());
//          owner.repaint();
        }
        if (adjust)
          setCursor(cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        if (!resize && rp != null/* && rp.distance(e.getPoint()) > 4*/) resize = true;
        if (resize) resizeTo(e.getX(), e.getY());
      }
      public void mouseMoved(MouseEvent e) {
        if (selected && !drag)
          setCursorShape(e.getX(), e.getY());
      }
    });
  }

  public boolean isText() {
    return type == TEXT;
  }

  public boolean isTextReal() {
    return isText() && getText() != null && getText().length() > 0;
  }

  public boolean isPic() {
    return type == PIC;
  }

  public boolean isFollow() {
    return follow;
  }

  public boolean isHline() {
    return type == HLINE;
  }

  public boolean isVline() {
    return type == VLINE;
  }

  public boolean isLine() {
    return type == HLINE || type == VLINE;
  }

  public int getThick() {
    return thick;
  }

  public java.net.URL getURL() {
    return url;
  }

  public Dimension getPreferredSize() {
    if (!isTextReal()) return super.getPreferredSize();
    String[] parts = new VarStr(getText()).split('\n');
//    Graphics g = getGraphics();
//    FontMetrics fm = (g != null ? g : owner.getGraphics()).getFontMetrics(getFont());
    FontMetrics fm = getFontMetrics(getFont());
    double x = 0, y = 0;
    for (int i = 0; i < parts.length; i++) {
      Rectangle2D rect = fm.getStringBounds(parts[i], getGraphics());
      y += rect.getHeight();
      x = Math.max(x, rect.getWidth());
    }
    return new Dimension((int) x, (int) y);
  }

  public void paintComponent(Graphics g) {
    if (isPic()) {
//      super.paintComponent(g);
//      g.drawImage(getIcon().geti)
      if (im == null) super.paintComponent(g);
      else if (follow)
        g.drawImage(im, 0, 0, getWidth(), getHeight(), this);
      else {
//        g.drawImage(im, 0, 0, this);
        g.drawImage(im, (getWidth() - im.getWidth(this)) / 2,
                    (getHeight() - im.getHeight(this)) / 2, this);
      }
    } else if (!isLine()) {
      if (getText() == null || getText().length() == 0) return;
      FontMetrics fm = g.getFontMetrics(getFont());
      int x, y = 0, from = 0, beg;
      g.setColor(getForeground());
      g.setFont(getFont());
      while ((from = getText().indexOf('\n', beg = from)) >= 0) {
        if (getHorizontalAlignment() == SwingConstants.LEADING) x = 0;
        else {
          Rectangle2D rect = fm.getStringBounds(getText(), beg, from, g);
          if (getHorizontalAlignment() == SwingConstants.CENTER)
            x = getWidth() / 2 - (int) (rect.getWidth() / 2);
          else x = getWidth() - (int) rect.getWidth();
        }
        g.drawString(getText().substring(beg, from++), x, y + fm.getAscent());
        y += fm.getHeight();
      }
      if (getHorizontalAlignment() == SwingConstants.LEADING) x = 0;
      else {
        Rectangle2D rect = fm.getStringBounds(getText().substring(beg), g);
        if (getHorizontalAlignment() == SwingConstants.CENTER)
          x = getWidth() / 2 - (int) (rect.getWidth() / 2);
        else x = getWidth() - (int) rect.getWidth();
      }
      g.drawString(getText().substring(beg), x, y + fm.getAscent());
    } else {
      g.setColor(getForeground());
      if (isHline())
        g.fillRect(0, HAND / 2, getWidth(), thick);
      else
        g.fillRect(HAND / 2, 0, thick, getHeight());
    }
    if (focus) {
      g.setColor(sel);
      g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
    if (selected) {
      g.setColor(sel);
      g.fillRect(0, 0, getWidth(), getHeight());
      if (focus && !drag) {
        g.setColor(hand);
        if (isHline() || (!isLine() && getHeight() >= HAND * 3 / 2)) {
          g.fillRect(0, (getHeight() - HAND) / 2, HAND, HAND);
          g.fillRect(getWidth() - HAND, (getHeight() - HAND) / 2, HAND, HAND);
        }
        if (isVline() || (!isLine() && getWidth() >= HAND * 3 / 2)) {
          g.fillRect((getWidth() - HAND) / 2, 0, HAND, HAND);
          g.fillRect((getWidth() - HAND) / 2, getHeight() - HAND, HAND, HAND);
        }
        if (!isLine()) {
          g.fillRect(0, 0, HAND, HAND);
          g.fillRect(getWidth() - HAND, 0, HAND, HAND);
          g.fillRect(0, getHeight() - HAND, HAND, HAND);
          g.fillRect(getWidth() - HAND, getHeight() - HAND, HAND, HAND);
        }
      }
    }
  }

  private int getHandle(int x, int y) {
    int w = getWidth(), h = getHeight();

    if (isHline() || (!isLine() && h >= HAND * 3 / 2))
      if (y >= (h - HAND) / 2 && y < (h - HAND) / 2 + HAND)
        if (x < w && x >= w - HAND) return E_HANDLE;
        else if (x >= 0 && x < HAND) return W_HANDLE;

    if (isVline() || (!isLine() && w >= HAND * 3 / 2))
      if (x >= (w - HAND) / 2 && x < (w - HAND) / 2 + HAND)
        if (y < h && y > h - HAND) return S_HANDLE;
        else if (y >= 0 && y < HAND) return N_HANDLE;

    if (!isLine())
      if (x < w && x >= w - HAND && y < h && y >= h - HAND) return SE_HANDLE;
      else if (x < w && x >= w - HAND && y >= 0 && y < HAND) return NE_HANDLE;
      else if (x >= 0 && x < HAND && y < h && y >= h - HAND) return SW_HANDLE;
      else if (x >= 0 && x < HAND && y >= 0 && y < HAND) return NW_HANDLE;

    return BODY;
  }

  private void setCursorShape(int x, int y) {
    Cursor cur = resizeCursors[selected ? getHandle(x, y) : BODY];
    if (cursor != cur) setCursor(cursor = cur);
  }

  private int xdist(Rectangle r1, Rectangle r2) {
    return Math.min(Math.abs(r1.x + r1.width - r2.x), Math.abs(r1.x - r2.x - r2.width));
  }

  private int ydist(Rectangle r1, Rectangle r2) {
    return Math.min(Math.abs(r1.y + r1.height - r2.y), Math.abs(r1.y - r2.y - r2.height));
  }

  private int xalign(Rectangle r1, Rectangle r2) {
    return Math.min(Math.abs(r1.x - r2.x), Math.abs(r1.x + r1.width - r2.x - r2.width));
  }

  private int getAscent() {
    return (int) getFont().getLineMetrics(getText(),
        ((Graphics2D) getGraphics()).getFontRenderContext()).getAscent();
  }

  private int yalign(Rectangle rl, Element m) {
    return Math.abs(rl.y + getAscent() - m.getY() - m.getAscent());
  }

  private void alignLocation(int x, int y) {
    Rectangle rl = getBounds(), rm = new Rectangle(), minx = null, miny = null;
    rl.setLocation(getX() + x - dp.x, getY() + y - dp.y);
    if (alignDist == 0 || (!isTextReal() && !isLine())) {
//      lp.setLocation(x, y);
      setLocation(rl.x, rl.y);
      return;
    }
    int rx = rl.x, ry = rl.y;

    Element m, my = null;
    for (int i = 0; i < elements.size(); i++) {
      m = (Element) elements.get(i);
      m.getBounds(rm);
      if (m != this && m.isTextReal() && isTextReal()) {
        if (ydist(rl, rm) <= Math.max(rl.getHeight(), rm.getHeight()) / 2 &&
            xalign(rl, rm) < alignDist)
          if (minx == null || ydist(rl, rm) < ydist(rl, minx))
            minx = m.getBounds(minx);
        if (xdist(rl, rm) <= Math.max(rl.getHeight(), rm.getHeight()) &&
            yalign(rl, m) < alignDist)
          if (miny == null || xdist(rl, rm) < xdist(rl, miny))
            miny = (my = m).getBounds(miny);
      }
//      if (m != this && m.isHline() && isVline()) {
//        if (getX() + getWidth() / 2
//      }
    }

//    System.out.println(minx + "  " + miny);
    if (minx != null) {
      if (Math.abs(minx.x - rl.x) <= Math.abs(minx.x + minx.width - rl.x - rl.width))
        rl.setLocation(minx.x, rl.y);
      else rl.setLocation(minx.x + minx.width - rl.width, rl.y);
    } else if (Math.abs(rl.x - raSectionDesigner.getLeftMargin()) < alignDist)
      rl.setLocation(raSectionDesigner.getLeftMargin(), rl.y);
    else if (Math.abs(rl.x + rl.width - 1 - raSectionDesigner.getRightMargin()) < alignDist)
      rl.setLocation(raSectionDesigner.getRightMargin() - rl.width + 1, rl.y);

    if (miny != null)
      rl.setLocation(rl.x, miny.y + my.getAscent() - getAscent());
    else if (Math.abs(rl.y) < alignDist)
      rl.setLocation(rl.x, 0);
    else if (Math.abs(rl.y + rl.height - 1 - raSectionDesigner.getHeight()) < alignDist)
      rl.setLocation(rl.x, raSectionDesigner.getHeight() - rl.height + 1);

//    lp.setLocation(x + rl.x - rx, y + rl.y - ry);
    setLocation(rl.x, rl.y);
  }

  private static Rectangle rr = new Rectangle();

  private void resizeTo(int x, int y) {
    getBounds(rr);
    if (handle == NW_HANDLE || handle == W_HANDLE || handle == SW_HANDLE) {
      rr.width = Math.max(HAND * 3 / 2, rr.width - x + rp.x);
//      rp.x += getWidth() - rr.width;
      rr.x += getWidth() - rr.width;
    }
    if (handle == NW_HANDLE || handle == N_HANDLE || handle == NE_HANDLE) {
      rr.height = Math.max(HAND * 3 / 2, rr.height - y + rp.y);
//      rp.y += getHeight() - rr.height;
      rr.y += getHeight() - rr.height;
    }
    if (handle == NE_HANDLE || handle == E_HANDLE || handle == SE_HANDLE) {
      rr.width = Math.max(HAND * 3 / 2, rr.width + x - rp.x);
      rp.x += rr.width - getWidth();
//      rr.x += rr.width - getWidth();
    }
    if (handle == SW_HANDLE || handle == S_HANDLE || handle == SE_HANDLE) {
      rr.height = Math.max(HAND * 3 / 2, rr.height + y - rp.y);
      rp.y += rr.height - getHeight();
//      rr.y += rr.height - getHeight();
    }
    setBounds(rr);
  }

  private void initPopup() {
    JMenuItem jmiRestore = new JMenuItem("Prirodna veli\u010Dina");
    jmiRestore.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isPic() || isTextReal())
          setSize(getPreferredSize());
      }
    });
    prop.add(jmiRestore);
    prop.addSeparator();

    JMenuItem jmiFront = new JMenuItem("Primakni na vrh");
    jmiFront.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Element.elements.remove(Element.this);
        Element.elements.add(Element.this);
        owner.remove(Element.this);
        owner.add(Element.this, 0);
        owner.repaint();
      }
    });
    JMenuItem jmiBack = new JMenuItem("Odmakni na dno");
    jmiBack.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Element.elements.remove(Element.this);
        Element.elements.add(0, Element.this);
        owner.remove(Element.this);
        owner.add(Element.this);
        owner.repaint();
      }
    });
    JMenuItem jmiDel = new JMenuItem("Obriši element");
    jmiDel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Element.this.delete();
        owner.repaint();
      }
    });
    prop.add(jmiFront);
    prop.add(jmiBack);
    prop.addSeparator();
    prop.add(jmiDel);
    prop.addSeparator();
    if (cut != null) {
      prop.add(cut);
      prop.add(copy);
      prop.add(paste);
      prop.addSeparator();
    }
    JMenuItem jmiProp = new JMenuItem("Parametri...");
    jmiProp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ElementDialog.show((Window) owner.getTopLevelAncestor(), Element.this);
      }
    });
    prop.add(jmiProp);

    prop.pack();
  }

/*  public void changeText() {
    int ret;
    if (!isTextReal()) tf.setText("");
    else tf.setText(getText());
    ret = JOptionPane.showConfirmDialog(this, tf, "Unos teksta",
                      JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (ret == JOptionPane.OK_OPTION)
      setText(tf.getText().trim().substring(0, Math.min(80, tf.getText().trim().length())));
  }*/

  /*public void changeText() {
    ElementDialog.show((Window) owner.getTopLevelAncestor(), this);
    int ret;
//    JDialog d = new JDialog(

    if (!isTextReal()) ta.setText("");
    else ta.setText(getText());
    JraScrollPane sc = new JraScrollPane(ta);
    sc.setPreferredSize(new Dimension(400, 120));
//    sc.setBorder(BorderFactory.createLoweredBevelBorder());

    ret = JOptionPane.showConfirmDialog(this, sc, "Unos teksta",
                      JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (ret == JOptionPane.OK_OPTION)
      setText(ta.getText().trim().substring(0, Math.min(80, ta.getText().trim().length())));
  }*/

/*  public void changePicture() {
    if (fc.showOpenDialog(owner) == fc.APPROVE_OPTION) {
      try {
        if (fc.getSelectedFile().exists())
          setImage(fc.getSelectedFile().toURL());
      } catch (Exception e) {}
    }
  }*/
}

class ElementDialog {
  private JDialog dlg;

  private OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };
  private JPanel main, chg, textPan, picPan, linePan;

  private raButtonGroup bgType = new raButtonGroup();
  private JraRadioButton jrbText = new JraRadioButton();
  private JraRadioButton jrbPic = new JraRadioButton();
  private JraRadioButton jrbLine = new JraRadioButton();

  private raButtonGroup bgAlign = new raButtonGroup();
  private JraRadioButton jrbLeft = new JraRadioButton();
  private JraRadioButton jrbCenter = new JraRadioButton();
  private JraRadioButton jrbRight = new JraRadioButton();
  private JTextArea ta = new JTextArea();

  private raButtonGroup bgOrient = new raButtonGroup();
  private JraRadioButton jrbHoriz = new JraRadioButton();
  private JraRadioButton jrbVert = new JraRadioButton();
  private JraTextField jtfThick = new JraTextField(7);

  private JraCheckBox jcbResize = new JraCheckBox();
  private JLabel jlPic = new JLabel();

  private int etype;
  private java.net.URL eurl;
  private String etext;

  private boolean accept, focus;

  private JFileChooser fc = new JFileChooser();

  private Border bord = BorderFactory.createCompoundBorder(
      BorderFactory.createEmptyBorder(10, 10, 10, 10),
      BorderFactory.createEtchedBorder());

  private static ElementDialog inst = new ElementDialog();

  public static Element show(Window owner, Element el) {
    return inst.showInstance(owner, el);
  }

  private Element showInstance(Window owner, Element el) {
    if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, "Parametri elementa", true);
    else if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, "Parametri elementa", true);
    else dlg = new JraDialog((Frame) null, "Parametri elementa", true);
    setElement(el);
    dlg.getContentPane().add(main);
    dlg.pack();
    Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
    if (dlg.getX() == 0 && dlg.getX() == 0)
      dlg.setLocation(scr.width / 2 - dlg.getWidth() / 2, scr.height / 3 - dlg.getHeight() / 3);

    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        accept = false;
        hide();
      }
    });
    dlg.addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        if (focus) {
          focus = false;
          ta.selectAll();
          ta.requestFocus();
        }
      }
    });

    okp.registerOKPanelKeys(dlg);
    dlg.show();
    if (!accept) return null;
    if (el == null)
      el = new Element();
    if (jrbText.isSelected()) {
      el.setTextual(ta.getText().trim().substring(0, Math.min(200, ta.getText().trim().length())));
      if (jrbLeft.isSelected())
        el.setHorizontalAlignment(SwingConstants.LEADING);
      else if (jrbCenter.isSelected())
        el.setHorizontalAlignment(SwingConstants.CENTER);
      else el.setHorizontalAlignment(SwingConstants.TRAILING);
    } else if (jrbPic.isSelected()) {
      if (!el.isPic() || !eurl.equals(el.getURL()))
        el.setImage(eurl);
      el.setFollow(!jcbResize.isSelected());
    } else {
      int thick = 1;
      try {
        thick = Integer.parseInt(jtfThick.getText());
      } catch (Exception e) {}
      thick = Math.min(12, Math.max(1, thick));
      if (jrbHoriz.isSelected()) el.setHline(thick);
      else el.setVline(thick);
    }
    return el;
  }

  private void setElement(Element el) {
    bgType.setSelected(jrbText);
    ta.setText("");
    bgAlign.setSelected(jrbLeft);
    jcbResize.setSelected(false);
    bgOrient.setSelected(jrbHoriz);
    jtfThick.setText("1");
    focus = false;
    if (el == null) return;
    if (el.isText()) {
      bgType.setSelected(jrbText);
      ta.setText(el.getText());
      if (el.getHorizontalAlignment() == SwingConstants.LEADING)
        bgAlign.setSelected(jrbLeft);
      else if (el.getHorizontalAlignment() == SwingConstants.CENTER)
        bgAlign.setSelected(jrbCenter);
      else bgAlign.setSelected(jrbRight);
      jlPic.setIcon(null);
      focus = true;
    } else if (el.isPic()) {
      bgType.setSelected(jrbPic);
      eurl = el.getURL();
      jlPic.setIcon(el.getIcon());
      jcbResize.setSelected(!el.isFollow());
      ta.setText("");
    } else {
      bgType.setSelected(jrbLine);
      if (el.isHline())
        bgOrient.setSelected(jrbHoriz);
      else bgOrient.setSelected(jrbVert);
      jtfThick.setText(String.valueOf(el.getThick()));
      jlPic.setIcon(null);
      ta.setText("");
    }
  }

  private ElementDialog() {
    JPanel type = new JPanel(new GridLayout(1, 0));
    type.add(jrbText);
    type.add(jrbPic);
    type.add(jrbLine);
    bgType.add(jrbText, " Tekst ");
    bgType.add(jrbPic, " Slika ");
    bgType.add(jrbLine, " Linija ");
    bgType.setHorizontalAlignment(SwingConstants.LEADING);
    bgType.setHorizontalTextPosition(SwingConstants.TRAILING);
    bgType.setCursorModel(bgType.NORMAL);
    type.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));

    chg = new JPanel(new BorderLayout());
    chg.setBorder(BorderFactory.createEtchedBorder());
    chg.setBorder(bord);

    main = new JPanel(new BorderLayout());
    main.add(type, BorderLayout.NORTH);
    main.add(chg, BorderLayout.CENTER);
    main.add(okp, BorderLayout.SOUTH);

    textPan = new JPanel(new BorderLayout());
    JraScrollPane sc = new JraScrollPane(ta);
    sc.setPreferredSize(new Dimension(400, 120));
    textPan.add(sc, BorderLayout.CENTER);
    JPanel align = new JPanel(new GridLayout(0, 3));
    align.add(jrbLeft);
    align.add(jrbCenter);
    align.add(jrbRight);
    bgAlign.add(jrbLeft, " Lijevo ");
    bgAlign.add(jrbCenter, " Centar ");
    bgAlign.add(jrbRight, " Desno ");
    bgAlign.setHorizontalAlignment(SwingConstants.LEADING);
    bgAlign.setHorizontalTextPosition(SwingConstants.TRAILING);
    bgAlign.setCursorModel(bgAlign.NORMAL);
    textPan.add(align, BorderLayout.SOUTH);
    align.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Poravnanje"),
        BorderFactory.createEmptyBorder(0, 10, 0, 5)));

    picPan = new JPanel(new BorderLayout());
    jcbResize.setText(" Konstantna veli\u010Dina ");
    picPan.add(jcbResize, BorderLayout.NORTH);
    picPan.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
    picPan.add(jlPic);
    jlPic.setText(null);
    jlPic.setHorizontalAlignment(SwingConstants.CENTER);
    jlPic.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    JraButton odabir = new JraButton();
    odabir.setText("Odabir");
    picPan.add(odabir, BorderLayout.SOUTH);

    linePan = new JPanel(new BorderLayout());
    JPanel orient = new JPanel(new GridLayout(0, 2));
    orient.add(jrbHoriz);
    orient.add(jrbVert);
    bgOrient.add(jrbHoriz, " Vodoravna ");
    bgOrient.add(jrbVert, " Okomita ");
    bgOrient.setCursorModel(bgOrient.NORMAL);
    bgOrient.setHorizontalAlignment(SwingConstants.LEADING);
    bgOrient.setHorizontalTextPosition(SwingConstants.TRAILING);
    orient.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
    linePan.add(orient, BorderLayout.NORTH);
    JLabel lab = new JLabel("Širina");
    lab.setPreferredSize(new Dimension(lab.getPreferredSize().width * 3,
                                       lab.getPreferredSize().height));
    JPanel pan = new JPanel();
    pan.add(lab);
    pan.add(jtfThick);
    pan.setBorder(orient.getBorder());
    linePan.add(pan);

    chg.add(textPan);
    textPan.setPreferredSize(textPan.getPreferredSize());
    picPan.setPreferredSize(textPan.getPreferredSize());
    linePan.setPreferredSize(textPan.getPreferredSize());

    jrbText.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbText.isSelected() && !chg.isAncestorOf(textPan)) {
          if (chg.isShowing())
            textPan.setPreferredSize(chg.getComponent(0).getSize());
          chg.remove(0);
          chg.add(textPan);
          chg.revalidate();
          chg.repaint();
//          dlg.repaint();
        }
      }
    });
    jrbPic.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbPic.isSelected() && !chg.isAncestorOf(picPan)) {
          if (chg.isShowing())
            picPan.setPreferredSize(chg.getComponent(0).getSize());
          chg.remove(0);
          chg.add(picPan);
          chg.revalidate();
          chg.repaint();
//          dlg.repaint();
        }
      }
    });
    jrbLine.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbLine.isSelected() && !chg.isAncestorOf(linePan)) {
          if (chg.isShowing())
            linePan.setPreferredSize(chg.getComponent(0).getSize());
          chg.remove(0);
          chg.add(linePan);
          chg.revalidate();
          chg.repaint();
//          dlg.repaint();
        }
      }
    });
    odabir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        changePicture();
      }
    });
    fc.setFileFilter(new raFileFilter("JPEG format (*.jpg, *.jpeg)"));
    fc.addChoosableFileFilter(new raFileFilter("GIF format (*.gif)"));
    fc.addChoosableFileFilter(new raFileFilter("PNG format (*.png)"));
  }

  private void OKPress() {
    accept = true;
    this.hide();
  }

  private void hide() {
    if (dlg != null) {
      dlg.removeAll();
      dlg.dispose();
      dlg = null;
    }
  }

  private void CancelPress() {
    accept = false;
    this.hide();
  }

  private void changePicture() {
    if (fc.showOpenDialog(jlPic) == fc.APPROVE_OPTION) {
      try {
        if (fc.getSelectedFile().exists()) {
//          jlPic.setIcon(new ImageIcon(eurl = fc.getSelectedFile().toURL()).getImage().getScaledInstance());
          ImageIcon icon = new ImageIcon(eurl = fc.getSelectedFile().toURL());
          jlPic.setIcon(icon);
        }
      } catch (Exception e) {}
    }
  }
}
