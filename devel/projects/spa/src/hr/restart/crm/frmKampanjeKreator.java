/****license*****************************************************************
**   file: frmKampanjeKreator.java
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
package hr.restart.crm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.Kampanje;
import hr.restart.baza.Klijenti;
import hr.restart.baza.Useri;
import hr.restart.sisfun.raUser;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.SharedFlag;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.raComboBox;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;


public class frmKampanjeKreator extends raFrame {
  
  public OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {      
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };
  
  JraButton jbFilter = new JraButton();
  JraButton jbDod = new JraButton();
  JraButton jbLeft = new JraButton();
  JraButton jbRight = new JraButton();
  JraButton jbAllLeft = new JraButton();
  JraButton jbAllRight = new JraButton();
  
  JLabel ukupno = new JLabel();
  
  JList all = new JList();
  JList sel = new JList();
  SortedListModel allMod = new SortedListModel();
  SortedListModel selMod = new SortedListModel();
  
  DataSet ds;
  AgentPanel agents = new AgentPanel();

  public frmKampanjeKreator() {
    JPanel main = new JPanel(new XYLayout(700, 320));
    all.setModel(allMod);
    all.setCellRenderer(getClientListRenderer());

    sel.setModel(selMod);
    sel.setCellRenderer(getClientListRenderer());
    
    JraScrollPane asc = new JraScrollPane();
    asc.setViewportView(all);
    asc.setPreferredSize(new Dimension(300, 250));
    
    JraScrollPane ssc = new JraScrollPane();
    ssc.setViewportView(sel);
    ssc.setPreferredSize(new Dimension(300, 250));
    
    jbFilter.setText("Postavi filter");
    jbDod.setText("Dodatni odabir");
    
    jbLeft.setIcon(raImages.getImageIcon(raImages.IMGBACK));
    jbRight.setIcon(raImages.getImageIcon(raImages.IMGFORWARD));
    jbAllLeft.setIcon(raImages.getImageIcon(raImages.IMGALLBACK));
    jbAllRight.setIcon(raImages.getImageIcon(raImages.IMGALLFORWARD));
    jbLeft.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        left();
      }
    });
    jbRight.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        right();
      }
    });
    jbAllLeft.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        allLeft();
      }
    });
    jbAllRight.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        allRight();
      }
    });
    
    main.add(new JLabel("Popis klijenata"), new XYConstraints(20, 22, -1, -1));
    main.add(jbFilter, new XYConstraints(170, 17, 150, 25));
    
    main.add(new JLabel("Odabrani klijenti"), new XYConstraints(380, 22, -1, -1));
    main.add(jbDod, new XYConstraints(530, 17, 150, 25));
    
    main.add(asc, new XYConstraints(20, 50, 300, 250));
    main.add(ssc, new XYConstraints(380, 50, 300, 250));
    
    main.add(jbRight, new XYConstraints(337, 100, 25, 25));
    main.add(jbLeft, new XYConstraints(337, 130, 25, 25));
    main.add(jbAllRight, new XYConstraints(337, 165, 25, 25));
    main.add(jbAllLeft, new XYConstraints(337, 195, 25, 25));
    main.add(ukupno, new XYConstraints(380, 305, -1, -1));
    
    
    jpKampanjeMaster podaci = new jpKampanjeMaster(false);
    ds = Kampanje.getDataModule().getTempSet(Condition.nil);
    ds.open();
    ds.insertRow(false);
    podaci.BindComponents(ds);
    JTabbedPane tabs = new JTabbedPane();
    tabs.add("Raspodjela klijenata", agents);
    tabs.add("Podaci o kampanji", podaci);
    tabs.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));
    
    okp.registerOKPanelKeys(this);
    
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(main, BorderLayout.NORTH);
    this.getContentPane().add(tabs);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    this.pack();
  }
  
  void setNumberKlijents() {
    ukupno.setText("Ukupno " + Aus.getNumDep(selMod.getSize(), "odabran ", "odabrano ", "odabrano ")
        + Aus.getNum(selMod.getSize(), "klijent.", "klijenta.", "klijenata."));
    agents.recalc();
  }
  
  public void show() {
    allMod.clear();
    selMod.clear();
    DataSet kl = Klijenti.getDataModule().getQueryDataSet();
    kl.open();
    for (kl.first(); kl.inBounds(); kl.next())
      allMod.add(new Klijent(kl));
    
    all.revalidate();
    all.repaint();
    sel.revalidate();
    sel.repaint();
    
    setNumberKlijents();
    super.show();
  }
  
  public void OKPress() {
    this.hide();
  }
  
  public void cancelPress() {
    this.hide();
  }
  
  void right() {
    int[] indices = all.getSelectedIndices();
    if (indices != null && indices.length > 0)
      selMod.addAll(allMod.removeAll(indices));
    updateLists();
  }
  
  void left() {
    int[] indices = sel.getSelectedIndices();
    if (indices != null && indices.length > 0)
      allMod.addAll(selMod.removeAll(indices));
    updateLists();
  }
  
  void allRight() {
    if (allMod.getSize() > 0) 
      selMod.addAll(allMod.clear());
    updateLists();
  }
  
  void allLeft() {
    if (selMod.getSize() > 0) 
      allMod.addAll(selMod.clear());
    updateLists();
  }
  
  void updateLists() {
    all.clearSelection();
    sel.clearSelection();
    setNumberKlijents();
  }
  
  public ListCellRenderer getClientListRenderer() {
    return new DefaultListCellRenderer() {
        Color tc = null;
        public Component getListCellRendererComponent(JList list,
                Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
          if (value instanceof Klijent)
            tc = raStatusColors.getInstance().getColor(((Klijent) value).status);
          return super.getListCellRendererComponent(list, " " + value, index, 
                    isSelected, false);
        }
        
        public Dimension getPreferredSize() {
            Dimension dim = super.getPreferredSize();
            return new Dimension(dim.width + 25, dim.height);
        }

        protected void paintComponent(Graphics g) {
            g.translate(20, 0);
            super.paintComponent(g);
            g.translate(-20, 0);
            if (tc != null) {
                Color old = g.getColor();
                g.setColor(tc);
                int h = getHeight();
                g.fillRect(4, 4, 12, h - 8);
                g.setColor(old);
            }
        }
    };
  }
  
  class AgentPanel extends JPanel {

    raComboBox rcbMeth = new raComboBox();
    
    JraButton jbAdd = new JraButton();
    
    JraButton jbRecalc = new JraButton();
    
    XYLayout lay = new XYLayout();
    List combo = new ArrayList();
    List numk = new ArrayList();
    List act = new ArrayList();
    List real = new ArrayList();

    String[][] itm;
    
    SharedFlag csf = new SharedFlag();
    ActionExecutor exec = new ActionExecutor(csf) {
      public void run() {
        press((JraButton) obj);
      }
    };

    ActionListener comm = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exec.invoke(e.getSource());
      }
    };

    public AgentPanel() {
      lay.setWidth(650);
      lay.setHeight(75 + 30);
      setLayout(lay);
      
      rcbMeth.setRaItems(new String[][] {
          {"Raspodijeliti po redu (abecedi)", "1"},
          {"Raspodijeliti sluèajnim odabirom", "2"},
          {"Raspodijeliti po zadnjem kontaktu", "3"}
      });
      rcbMeth.setSelectedIndex(0);
      
      jbAdd.setText("Dodaj agenta");
      jbAdd.setIcon(raImages.getImageIcon(raImages.IMGADD));
      jbAdd.addActionListener(comm);
      
      jbRecalc.setText("Razdijeli");
      jbRecalc.addActionListener(comm);
      
      add(rcbMeth, new XYConstraints(15, 20, 295, -1));
      add(jbAdd, new XYConstraints(345, 20, 185, 21));
      add(jbRecalc, new XYConstraints(540, 20, 100, 21));
      
      DataSet us = Useri.getDataModule().getTempSet();
      us.open();
      itm = new String[us.rowCount()][2];
      int i = 0, s = 0;
      for (us.first(); us.inBounds(); us.next(), i++) {
        itm[i][0] = us.getString("NAZIV");
        itm[i][1] = us.getString("CUSER");
        if (raUser.getInstance().getUser().equals(itm[i][1]))
          s = i;
      }

      createLine(0);
      ((raComboBox) combo.get(0)).setSelectedIndex(s);
      addLine(0, 55);
      real.add(new Double(1));
    }

    void removeLine(int i) {
      remove((Component) combo.get(i));
      remove((Component) numk.get(i));
      remove((Component) act.get(i));
    }

    void addLine(int i, int y) {
      int add = i % 2 == 0 ? 0 : 330;
      add((Component) combo.get(i), 
          new XYConstraints(15 + add, y + (i >> 1) * 30, 185, -1));
      add((Component) numk.get(i), 
          new XYConstraints(210 + add, y + (i >> 1) * 30, 70, -1));
      add((Component) act.get(i), 
          new XYConstraints(290 + add, y + (i >> 1) * 30, 21, 21));
    }

    void createLine(int i) {
      raComboBox cb = new raComboBox();
      cb.setRaItems(itm);
      combo.add(i, cb);
      cb.setMaximumRowCount(16);
      
      JraTextField tf = new JraTextField() {
        public void valueChanged() {
          if (isValueChanged()) changed(this);
        }
      };
      new raTextMask(tf, 4, false, raTextMask.DIGITS);
      numk.add(i, tf);
      
      JraButton but = new JraButton();
      but.setIcon(raImages.getImageIcon(raImages.IMGDELETE));
      but.setToolTipText("Obriši");
      act.add(i, but);
      but.addActionListener(comm);
    }

    int getVal(JraTextField tf) {
      return Aus.getAnyNumber(tf.getText());
    }

    int getVal(int i) {
      return getVal((JraTextField) numk.get(i));
    }

    void setVal(JraTextField tf, int num) {
      tf.setTxtBefore(Integer.toString(num));
      tf.setText(Integer.toString(num));
    }

    void setVal(int i, int num) {
      setVal((JraTextField) numk.get(i), num);
    }
    
    void changed(JraTextField tf) {
      int idx = -1;
      for (int i = 0; i < numk.size(); i++)
        if (numk.get(i) == tf) idx = i;
      
      if (getVal(idx) > selMod.getSize())
        setVal(idx, selMod.getSize());
      
      if (selMod.getSize() > 0) {
        real.set(idx, new Double(getVal(idx) / (double) selMod.getSize()));
        recalcReal(idx);
      }
      recalc(idx);
    }
    
    void recalcReal(int idx) {
      double di = ((Double) real.get(idx)).doubleValue();
      di = Math.min(1, Math.max(0, di));

      for (int i = 0; i < numk.size(); i++)
        if (i != idx) real.set(i, new Double((1 - di) * ((Double) real.get(i)).doubleValue()));
      System.out.println(real);
    }
    
    void recalcReal() {
      if (numk.size() == 0) return;
      
      Double avg = new Double(1d / numk.size());
      for (int i = 0; i < numk.size(); i++)
        real.set(i, avg);
      
      recalc(-1);
    }
    
    public void recalc() {
      recalc(-1);
    }
    
    void recalc(int idx) {
      int total = selMod.getSize();
      int d = idx >= 0 ? getVal(idx) : 0;
      for (int i = 0; i < numk.size(); i++) 
        if (i != idx) {        
          setVal(i, (int) (total * ((Double) real.get(i)).doubleValue()));
          d += getVal(i);
        }
      while (d < total)
        for (int i = 0; i < numk.size() && d < total; i++, d++)
          if (i != idx) setVal(i, getVal(i) + 1);
          else --d;
    }

    /*public void recalc() {
      recalc(null);
    }

    void recalc(int i) {
      recalc((JraTextField) numk.get(i));
    }*/

    /*void recalc(JraTextField ignore) {
      int total = selMod.getSize();
      int oldtotal = 0;
      int count = numk.size();
      if (ignore != null) {
        setVal(ignore, getVal(ignore));
        if (numk.size() == 1)
          setVal(ignore, total);
        if (getVal(ignore) > total)
          setVal(ignore, total);    
        total -= getVal(ignore);
        --count;
      }
      
      JraTextField high = null;
      for (int i = 0; i < numk.size(); i++)
        if (numk.get(i) != ignore) {
          oldtotal += getVal(i);
          if (high == null || getVal(i) > getVal(high)) 
            high = (JraTextField) numk.get(i);
        }
           
      for (int i = 0; i < numk.size(); i++)
        if (numk.get(i) != ignore) {
          int old = getVal(i);
          if (total == 0) setVal(i, 0);
          else if (old > 0 && oldtotal > 0) {
            int n = total * old / oldtotal;
            setVal(i, n);
            oldtotal -= old;
            total -= n;
          } else if (oldtotal == 0) {
            int n = total / count;
            setVal(i, n);
            total -= n;
            --count;
          }
        }
//      if (d != total) 
//        setVal(high, getVal(high) + total - d);
    }*/

    void press(JraButton but) {
      if (but == jbAdd) {
        lay.setHeight(95 + 30 * (combo.size() >> 1));
        createLine(combo.size());

        real.add(new Double(1d / combo.size()));
        setVal(combo.size() - 1, selMod.getSize() / combo.size());
        recalcReal(combo.size() - 1);
        recalc(combo.size() - 1);

        addLine(combo.size() - 1, 55);
        pack();
        repaint();
        ((JraTextField) numk.get(combo.size() - 1)).requestFocusLater();
        return;
      }
      if (act.size() == 1) return;
      
      if (but == jbRecalc) {
        recalcReal();
        return;
      }

      int idx = act.indexOf(but);
      
      remove((Component) combo.remove(idx));
      remove((Component) numk.remove(idx));
      remove((Component) act.remove(idx));
      for (int i = idx; i < combo.size(); i++) {
        removeLine(i);
        addLine(i, 55);
      }
      double total = 0;
      for (int i = 0; i < numk.size(); i++)
        total += ((Double) real.get(i)).doubleValue();
      total = 1 / total;
      for (int i = 0; i < numk.size(); i++)
        if (i != idx) real.set(i, new Double(total * ((Double) real.get(i)).doubleValue()));
      recalc();
      lay.setHeight(65 + 30 * ((combo.size() + 1) >> 1));
      pack();
      repaint();
    }
  }

  public static class SortedListModel extends AbstractListModel {

    private static final int INITIAL_CAPACITY = 10;
    private int size;

    private Object[] elems;
    
    public SortedListModel() {
      size = 0;
      elems = new Object[INITIAL_CAPACITY];
    }

    public int getSize() {
      return size;
    }

    public Object getElementAt(int index) {
      if (index >= size)
        throw new IndexOutOfBoundsException();
      return elems[index];
    }

    public void add(Object element) {
      int point = addImpl(element);

      fireIntervalAdded(this, point, point);
    }
    
    private int addImpl(Object element) {
      if (size >= elems.length)
        elems = copyOf(elems, size + (size >> 1));
      
      int point = binarySearch(elems, 0, size, element);
      if (point < 0) point = -point - 1;
      
      if (point < size) 
        System.arraycopy(elems, point, elems, point + 1, size - point);
      
      elems[point] = element;
      ++size;
      
      return point;
    }
    
    public void addAll(Collection c) {
      if (c.size() <= 10)
        for (Iterator i = c.iterator(); i.hasNext(); addImpl(i.next()));
      else {
        if (size + c.size() > elems.length)
          elems = copyOf(elems, Math.max(size + c.size(), elems.length + (elems.length >> 1)));
        for (Iterator i = c.iterator(); i.hasNext(); )
          elems[size++] = i.next();
        Arrays.sort(elems, 0, size);
      }
      fireContentsChanged();
    }

    public Object remove(int index) {
      Object rv = getElementAt(index);
      
      int numMoved = size - index - 1;
      if (numMoved > 0)
          System.arraycopy(elems, index + 1, elems, index, numMoved);
      elems[--size] = null; 

      fireIntervalRemoved(this, index, index);
      return rv;
    }
    
    public Collection removeAll(int[] indices) {    
      ArrayList ret = new ArrayList();
      if (indices.length == 0) return ret;
      
      Arrays.sort(indices);
      
      int r = 0, w = 0, i = 0;
      while (r < size)
        if (r < indices[i])
          elems[w++] = elems[r++];
        else if (++i >= indices.length)
          break;
        else if (r < indices[i]) 
          ret.add(getElementAt(r++));
      
      if (r < size) {
        ret.add(getElementAt(r++));
        System.arraycopy(elems, r, elems, w, size - r);
        w += size - r;
      }
      
      size = w;
      fireContentsChanged();
      
      return ret;
    }
    
    public Collection clear() {
      ArrayList ret = new ArrayList();
      if (size == 0) return ret;
      
      for (int i = 0; i < size; i++) {
        ret.add(elems[i]);
        elems[i] = null;
      }
      size = 0;
      
      fireContentsChanged();
      
      return ret;
    }

    public void fireContentsChanged() {
      fireContentsChanged(this, 0, getSize() - 1);
    }
    
    public String toString() {
      return Arrays.toString(copyOf(elems, size));
    }
  }
  
  static Object[] copyOf(Object[] array, int size) {
    Object[] na = new Object[size];
    System.arraycopy(array, 0, na, 0, Math.min(array.length, size));
    return na;
  }
  
  static int binarySearch(Object[] a, int fromIndex, int toIndex,
      Object key) {
    int low = fromIndex;
    int high = toIndex - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      Comparable midVal = (Comparable) a[mid];
      int cmp = midVal.compareTo(key);

      if (cmp < 0)
        low = mid + 1;
      else if (cmp > 0)
        high = mid - 1;
      else
        return mid; // key found
    }
    return -(low + 1); // key not found.
  }

  public static class Klijent implements Comparable {
    int id;
    String naziv;
    String lowercase;
    String grad;
    String seg;
    String status;
    
    public Klijent(DataSet ds) {
      id = ds.getInt("CKLIJENT");
      naziv = ds.getString("NAZIV");
      lowercase = naziv.toLowerCase();
      grad = ds.getString("MJ");
      seg = ds.getString("CSEG");
      status = ds.getString("SID");
    }
    
    public boolean equals(Object obj) {
      if (obj instanceof Klijent)
        return ((Klijent) obj).id == this.id;
      return false;
    }
    
    public int hashCode() {
      return id;
    }
    
    public String toString() {
      return naziv;
    }
    
    public int compareTo(Object o) {
      return this.lowercase.compareTo(((Klijent) o).lowercase);
    }
  }
}
