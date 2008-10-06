/****license*****************************************************************
**   file: raAutoMatPodaci.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public abstract class raAutoMatPodaci extends raMatPodaci {
  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();

//  JScrollBar vs = new JScrollBar(JScrollBar.VERTICAL) {
//    public void setEnabled(boolean dummy) {
//      super.setEnabled(true);
//    }
//  };

  JTextComponent focusNew, focusEdit;
  JTextComponent[] key;

  JraTextField[] field;

  XYLayout lay = new XYLayout();
  JPanel detail = new JPanel(lay);

  FocusListener showFocus = new FocusAdapter() {
    public void focusGained(FocusEvent e) {
      if (e.getComponent().getParent() instanceof JComponent)
        ((JComponent) e.getComponent().getParent()).
          scrollRectToVisible(e.getComponent().getBounds());
    }
  };

  public void EntryPoint(char mode) {
    for (int i = 0; i < key.length; i++)
      rcc.setLabelLaF(key[i], mode == 'N');
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      focusNew.requestFocus();
    } else if (mode == 'I') {
      focusEdit.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    for (int i = 0; i < key.length; i++)
      if (vl.isEmpty(key[i])) return false;
    if (mode == 'N' && vl.notUnique(key)) return false;
    return true;
  }

  public raAutoMatPodaci(QueryDataSet module) {
    try {
      init(module);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init(QueryDataSet module) throws Exception {
    this.setRaQueryDataSet(module);
    module.open();
    ArrayList keys = new ArrayList();
    ArrayList fields = new ArrayList();
    int y = 20, x = 0;
    for (int i = 0; i < module.getColumnCount(); i++) {
      Column c = module.getColumn(i);
      if (c.getVisible() != com.borland.jb.util.TriStateProperty.FALSE) {
        JLabel label = new JLabel();
        label.setText(c.getCaption());
        detail.add(label, new XYConstraints(15, y, -1, -1));

        JraTextField field = new JraTextField();

        field.setColumnName(c.getColumnName());
        field.setDataSet(module);
        fields.add(field);
        int w = 100;
        if (c.getDataType() == Variant.STRING)
          if (c.getPrecision() > 40) w = 350;
          else if (c.getPrecision() > 12) w = 250;
        detail.add(field, new XYConstraints(150, y, w, -1));
        field.addFocusListener(showFocus);

        if (c.isRowId()) {
          keys.add(field);
          if (focusNew == null) focusNew = field;
        } else
          if (focusEdit == null) focusEdit = field;

        if (w > x) x = w;
        y += 25;
      }
    }
    if (focusNew == null)
      throw new IllegalArgumentException("Nema RowId kolone");
    lay.setWidth(x + 170);
    lay.setHeight(y + 15);
    field = new JraTextField[fields.size()];
    fields.toArray(field);
    key = new JraTextField[keys.size()];
    keys.toArray(key);
//    JraScrollPane sc = new JraScrollPane(detail, JraScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                                     JraScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//    sc.setVerticalScrollBar(vs);
//    if (y > 500) sc.setPreferredSize(new Dimension(x + 170, 410));
//    else if (y > 350) sc.setPreferredSize(new Dimension(x + 170, 310));
//    JPanel direct = new JPanel(new BorderLayout());
//    direct.add(sc);
    setRaDetailPanel(detail);
  }

  public void pack() {
    super.pack();
    if (getWindow().getHeight() > 500) getWindow().setSize(getWindow().getWidth(), 400);
    else if (getWindow().getHeight() > 350) getWindow().setSize(getWindow().getWidth(), 320);
  }
}
