/****license*****************************************************************
**   file: dlgSplitAmount.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raCommonClass;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class dlgSplitAmount extends JraDialog {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel jp = new JPanel();
  XYLayout lay = new XYLayout();

  JLabel jlFirst = new JLabel();
  JLabel jlSecond = new JLabel();

  JraTextField jraFirst = new JraTextField() {
    public void valueChanged() {
      ensureSum(false);
    }
  };
  JraTextField jraSecond = new JraTextField() {
    public void valueChanged() {
      ensureSum(true);
    }
  };
  
  StorageDataSet fields = new StorageDataSet();
  
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };
  
  private void init() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  boolean ok;
  protected void CancelPress() {
    this.dispose();
  }

  protected void OKPress() {
    ok = true;
    this.dispose();
  }
  
  BigDecimal original;
  
  public BigDecimal performSplit(String item, BigDecimal value) {
    fields.setBigDecimal("FIRST", value);
    fields.setBigDecimal("SECOND", raSaldaKonti.n0);
    jlFirst.setText(item);
    original = value;
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        jraSecond.requestFocusLater();
      }
    });
    ok = false;
    super.show();
    ensureSum(true);
    if (!ok) return null;
    return fields.getBigDecimal("SECOND");
  }

  public dlgSplitAmount(Frame frame) {
    super(frame, "", true);
    init();
  }

  public dlgSplitAmount(Dialog dlg) {
    super(dlg, "", true);
    init();
  }

  private void setFields() {
    fields.setColumns(new Column[] {
      dM.createBigDecimalColumn("FIRST"),
      dM.createBigDecimalColumn("SECOND"),
    });
    fields.open();
  }

  private void jbInit() throws Exception {
    setFields();
    jp.setLayout(lay);
    lay.setWidth(300);
    lay.setHeight(80);

    jlFirst.setText("Stavka");
    jlSecond.setText("Nova stavka");
    jlFirst.setHorizontalAlignment(SwingConstants.CENTER);
    jlSecond.setHorizontalAlignment(SwingConstants.CENTER);
    
    jraFirst.setDataSet(fields);
    jraFirst.setColumnName("FIRST");
    jraSecond.setDataSet(fields);
    jraSecond.setColumnName("SECOND");

    jp.add(jlFirst, new XYConstraints(15, 20, 100, -1));
    jp.add(jlSecond, new XYConstraints(125, 20, 100, -1));
    jp.add(jraFirst, new XYConstraints(15, 45, 100, -1));
    jp.add(jraSecond, new XYConstraints(125, 45, 100, -1));

    addListeners();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    getContentPane().add(jp, BorderLayout.CENTER);
    getContentPane().add(okp, BorderLayout.SOUTH);
    pack();

    okp.registerOKPanelKeys(this);
    startFrame.getStartFrame().centerFrame(this, 15, "Odvajanje iznosa u novu stavku");
  }
  
  private void addListeners() {
    /*jraFirst.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        ensureSum(false);
      }
    });
    jraSecond.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        ensureSum(true);
      }
    });*/
  }

  protected void ensureSum(boolean sec) {
    if (sec) fields.setBigDecimal("FIRST", original.subtract(fields.getBigDecimal("SECOND")));
    else fields.setBigDecimal("SECOND", original.subtract(fields.getBigDecimal("FIRST")));
  }
}
