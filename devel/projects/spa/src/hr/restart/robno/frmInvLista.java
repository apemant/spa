/****license*****************************************************************
**   file: frmInvLista.java
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
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raUpitLite;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class frmInvLista extends raUpitLite {
  dM dm = dM.getDataModule();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();

  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCSKL = new JLabel();
  JLabel jlGOD = new JLabel();
  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraTextField jtfGOD = new JraTextField();
  JraButton jbCSKL = new JraButton();
  TableDataSet tds = new TableDataSet();
  public frmInvLista() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public boolean runFirstESC() {
    /**@todo Implement this hr.restart.util.raUpitLite abstract method*/
    throw new java.lang.UnsupportedOperationException("Method runFirstESC() not yet implemented.");
  }
  public void firstESC() {
    /**@todo Implement this hr.restart.util.raUpitLite abstract method*/
  }
  public void componentShow() {
    jrfCSKL.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
    jrfCSKL.forceFocLost();
    jtfGOD.requestFocus();
  }
  public boolean isIspis() {
    return false;
  }
  public void okPress() {
    vl.execSQL("select cart,kol,iraz from stdoki where vrdok='INM' and god='"+tds.getString("GOD")+"' and cskl='"+jrfCSKL.getText()+"'");
    vl.RezSet.open();
    vl.RezSet.first();
    do {
      if (hr.restart.util.lookupData.getlookupData().raLocate(dm.getInventura(),"CART", String.valueOf(vl.RezSet.getInt("CART")))) {
        dm.getInventura().setBigDecimal("KOLKNJ", rut.sumValue(dm.getInventura().getBigDecimal("KOLKNJ"),vl.RezSet.getBigDecimal("KOL")));
        dm.getInventura().setBigDecimal("VRIKNJ", rut.sumValue(dm.getInventura().getBigDecimal("VRIKNJ"),vl.RezSet.getBigDecimal("IRAZ")));
        dm.getInventura().setBigDecimal("KOLMANJ", rut.sumValue(dm.getInventura().getBigDecimal("KOLMANJ"),vl.RezSet.getBigDecimal("KOL")));
        dm.getInventura().setBigDecimal("VRIMANJ", rut.sumValue(dm.getInventura().getBigDecimal("VRIMANJ"),vl.RezSet.getBigDecimal("IRAZ")));
        dm.getInventura().post();
      }
      else {
        System.out.println("error: ");
      }

    } while (vl.RezSet.next());

    vl.execSQL("select cart,kol,izad from stdoku where vrdok='INV' and god='"+tds.getString("GOD")+"' and cskl='"+jrfCSKL.getText()+"'");
    vl.RezSet.open();
    vl.RezSet.first();
    do {
      if (hr.restart.util.lookupData.getlookupData().raLocate(dm.getInventura(),"CART", String.valueOf(vl.RezSet.getInt("CART")))) {
        dm.getInventura().setBigDecimal("KOLKNJ", rut.negateValue(dm.getInventura().getBigDecimal("KOLKNJ"),vl.RezSet.getBigDecimal("KOL")));
        dm.getInventura().setBigDecimal("VRIKNJ", rut.negateValue(dm.getInventura().getBigDecimal("VRIKNJ"),vl.RezSet.getBigDecimal("IZAD")));
        dm.getInventura().setBigDecimal("KOLVIS", rut.sumValue(dm.getInventura().getBigDecimal("KOLVIS"),vl.RezSet.getBigDecimal("KOL")));
        dm.getInventura().setBigDecimal("VRIVIS", rut.sumValue(dm.getInventura().getBigDecimal("VRIVIS"),vl.RezSet.getBigDecimal("IZAD")));
        dm.getInventura().post();
      }
      else {
        System.out.println("error: ");
      }

    } while (vl.RezSet.next());
    try {
      dm.getInventura().saveChanges();
    }
    catch (Exception ex) {
      JOptionPane.showConfirmDialog(this.jp, "Greška kod formiranja liste !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      this.hide();
    }
    JOptionPane.showConfirmDialog(this.jp, "Formiranje uspješno završeno !", "Informacija", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    this.hide();
  }
  private void jbInit() throws Exception {
    this.setJPan(jp);
    jlCSKL.setText("Skladište");
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(80);
    jp.setLayout(xYLayout1);
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setDataSet(this.tds);
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[] {0,1});
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setRaDataSet(rut.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);

    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);

    jtfGOD.setColumnName("GOD");
    jtfGOD.setDataSet(tds);

    jlGOD.setText("Godina");
    jbCSKL.setText("...");
    jp.add(jlCSKL,   new XYConstraints(15, 20, -1, -1));
    jp.add(jlGOD,    new XYConstraints(15, 45, -1, -1));
    jp.add(jrfCSKL,   new XYConstraints(150, 20, 100, -1));
    jp.add(jrfNAZSKL,     new XYConstraints(260, 20, 255, -1));
    jp.add(jtfGOD,   new XYConstraints(150, 45, 100, -1));
    jp.add(jbCSKL,   new XYConstraints(519, 20, 21, 21));
    tds.setColumns(new Column[] {
                   dM.createStringColumn("god", "Godina", 4),
                   dM.createStringColumn("cskl", "Skladište", 12)
    });


  }

}
