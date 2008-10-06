/****license*****************************************************************
**   file: raPropertiesBindedPanel.java
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
/*
 * raPropertiesBindedPanel.java
 *
 * Created on 2003. prosinac 12, 15:16
 */

package hr.restart.swing;

import hr.restart.util.Aus;
import hr.restart.util.OKpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;


/**
 *
 * @author  andrej
 */
public class raPropertiesBindedPanel extends JraPanel implements raPropertiesBinder.PropertiesBindedComponent {
  public DefaultFormBuilder builder;
  public FormLayout layout;
  public raPropertiesBinder binder;
  /** Creates a new instance of raPropertiesBindedPanel */
  protected raPropertiesBindedPanel() {
  }
  public raPropertiesBindedPanel(raPropertiesBinder _binder) {
    //binder = _binder;
    _binder.bindOther(this);
    jInitCommon();
  }
  
  private void jInitCommon() {
    layout = new FormLayout("left:min(150px;pref), 5px, left:min(120px;pref), 5px, p:f");
    builder = new DefaultFormBuilder(this,layout);
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setFocusCycleRoot(true);
    addAncestorListener(new AncestorListener() {
      public void ancestorAdded(AncestorEvent event) {
        reloadScreen();
      }
      public void ancestorRemoved(AncestorEvent event) {
      }
      public void ancestorMoved(AncestorEvent event) {
      }
    });
  }
  
  public void reloadScreen() {
    for (Iterator i = binder.getProperties().keySet().iterator(); i.hasNext(); ) {
      String ident = i.next().toString();
      binder.load(ident);
//      System.out.println(ident+" loaded");
    }
  }
  
  public void addTextFieldRow(String identifier, boolean enabled) {
    addTextFieldRow(identifier,enabled, null);
  }
  
  public void addTextFieldRow(String identifier, boolean enabled, Component additionalComponent) {
    JTextField jt = getNumericTextField(identifier);
    jt.setDisabledTextColor(jt.getForeground());
    jt.setEnabled(enabled);
    if (additionalComponent == null) additionalComponent = new Container();
    builder.append(identifier,additionalComponent,jt,1);
  }
  
  public void addListColsRow(String identifier, String[] cols, int rowCount) {
    //builder.append(identifier, getListCols(cols, rowCount),3);
    builder.appendSeparator(identifier);
    builder.append(getListCols(cols, rowCount),5);
  }
  public JTextField getNumericTextField(String identifier) {
    return getNumericTextField(identifier,2);
  }
  public JTextField getNumericTextField(String identifier, int decplaces) {
    JTextField jt = getTextField(identifier);
    Aus.installNumberMask(jt, decplaces);
    return jt;
  }
  public JTextField getTextField(String identifier) {
    final JTextField jt = new JTextField() {
      private boolean focable = true;
      public boolean isFocusTraversable() {
        return focable;
      }
      public void setEnabled(boolean en) {
        super.setEnabled(en);
        focable = en;
      }
    };
    jt.addKeyListener(new JraKeyListener());
    jt.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent ev) {
        scrollToVisible(jt);
      }
    });
    binder.bind(jt, identifier);
    return jt;
  }
  private void editList(JList list, String[] cols) {
    if (list.getSelectedIndex() > 0 && list.getSelectedIndex() < list.getModel().getSize()-1) {
      if (list.getTopLevelAncestor() instanceof Dialog)
        new ListEditDialog((Dialog)list.getTopLevelAncestor(), cols, list.getSelectedIndex());
      else if (list.getTopLevelAncestor() instanceof Frame)
        new ListEditDialog((Frame)list.getTopLevelAncestor(), cols, list.getSelectedIndex());
      list.repaint();
    }
  }
  public JList getListCols(final String[] cols,int rowCount) {
    ColsListModel model = new ColsListModel(cols, rowCount);
    binder.bindOther(model);
    final JList list = new JList(model);
    list.setCellRenderer(new ColListCellRenderer());
    list.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.gray));
    list.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 2) editList(list,cols);
      }
    });
    list.addKeyListener(new JraKeyListener());
    list.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F4 || e.getKeyCode() == e.VK_F6) {
          editList(list,cols);
        }
      }
    });
    list.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent ev) {
        scrollToVisible(list);
        list.setSelectedIndex(1);
      }
      public void focusLost(FocusEvent ev) {
        list.setSelectedIndex(0);
      }
    });
    return list;
  }
  
  public static void scrollToVisible(final Container c) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        //find viewport
        Container vc = c.getParent();
        while (true) {
          if (vc == null) break;
          if (vc instanceof JViewport) break;
          vc = vc.getParent();
        }
        if (vc instanceof JViewport) {
          JViewport jvw = (JViewport)vc;
          if (jvw.getViewRect().y > c.getBounds().y) {
            jvw.setViewPosition(new Point(0, 0));
          }
          jvw.scrollRectToVisible(c.getBounds());
        }
      }
    });
  }
  
  public void setBinder(raPropertiesBinder _binder) {
    binder = _binder;
  }
  
  public class ColsListModel extends AbstractListModel implements raPropertiesBinder.PropertiesBindedComponent {
    int rowCount;
    String[] cols;
    raPropertiesBinder binder;
    public ColsListModel(String[] _cols, int _rowCount) {
      cols = _cols;
      rowCount = _rowCount+2;
//      setBinder(_binder);
    }
    public int getSize() {
      return rowCount;
    }
    public void setBinder(raPropertiesBinder _binder) {
      binder = _binder;
      for (int i=1; i<rowCount; i++) {
        for (int j=0; j<cols.length; j++) {
          if (!binder.getProperties().containsKey(cols[j]+i)) binder.setText(cols[j]+i, "");
        }
      }
    }
    public Object getElementAt(int index) {
      if (index == 0) return cols;
      boolean total = index == getSize()-1;
      BigDecimal ss = Aus.zero2;
      if (total) {
        for (int i=1; i<rowCount-1; i++) {
          ss = ss.add(binder.getBigDecimal(cols[cols.length-1]+i));
        }
      }
      String[] elementsat = new String[cols.length];
      for (int i=0; i<cols.length; i++) {
        elementsat[i] = total?"":binder.getText(cols[i]+index);
      }
      if (total) {
        elementsat[cols.length-1] = ss.toString();
        elementsat[0] = "Ukupno";
      }
      return elementsat;
    }
  }
  
  public class ColListCellRenderer extends JPanel implements ListCellRenderer {
    public ColListCellRenderer() {
      setOpaque(true);
    }
    public Component getListCellRendererComponent(
    JList list,
    Object value,
    int index,
    boolean isSelected,
    boolean cellHasFocus) {
      //setText(value.toString());
      //ColsListModel model = (ColsListModel)list.getModel();
      String[] data = (String[])value;
      removeAll();
      setLayout(new GridLayout(1,data.length));
      for (int i=0; i<data.length; i++) {
        JLabel jl = new JLabel();
        if (index != 0) {
          jl.setOpaque(true);
          jl.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
          jl.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
          if (index == list.getModel().getSize()-1) {
            jl.setFont(jl.getFont().deriveFont(Font.BOLD));
          }
        } else {
          jl.setOpaque(false);
        }
        if (i!=0) jl.setHorizontalAlignment(SwingConstants.RIGHT);
        jl.setText(data[i]);
        jl.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 1, Color.gray),
        BorderFactory.createEmptyBorder(0, 2, 0, 2)));
        add(jl);
      }
      //setPreferredSize(new Dimension(data.length*100, 22));
      return this;
    }
  }
  
  public class ListEditDialog extends JDialog {
    String[] identifiers;
    int index;
    public ListEditDialog(java.awt.Dialog owner, String[] _identifiers, int _index) {
      super(owner);
      construct(_identifiers, _index);
    }
    public ListEditDialog(java.awt.Frame owner, String[] _identifiers, int _index) {
      super(owner);
      construct(_identifiers, _index);
    }
    private void construct(String[] _identifiers, int _index) {
      identifiers = _identifiers;
      index = _index;
      setModal(true);
      jInit();
      pack();
      show();
    }
    private void jInit() {
      OKpanel _okp = new OKpanel() {
        public void jBOK_actionPerformed() {
          for (int i=0; i<identifiers.length; i++) {
            binder.post(identifiers[i]+index);
          }
          ListEditDialog.this.hide();
        }
        public void jPrekid_actionPerformed() {
          ListEditDialog.this.hide();
        }
      };
      JPanel _jp = new JPanel();
      FormLayout _layout = new FormLayout("left:min(150px;pref), 5px, left:min(120px;pref), 5px, p:f");
      DefaultFormBuilder _builder = new DefaultFormBuilder(_jp,_layout);
      _jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      for (int i=0; i<identifiers.length; i++) {
        JTextField jt;
        if (i==0) jt = getTextField(identifiers[i]+index);
        else jt = getNumericTextField(identifiers[i]+index);
        _builder.append(identifiers[i],jt,3);
      }
      ListEditDialog.this.getContentPane().add(_jp,BorderLayout.CENTER);
      ListEditDialog.this.getContentPane().add(_okp,BorderLayout.SOUTH);
      _okp.registerOKPanelKeys(ListEditDialog.this);
    }
  }  
}
