/****license*****************************************************************
**   file: frmNapuniInv.java
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
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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

public class frmNapuniInv extends raUpitLite {
  hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  TableDataSet tds = new TableDataSet();
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCSKL = new JLabel("Skladište");
  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraButton jbCSKL = new JraButton();
  JLabel jlGOD = new JLabel("Godina");
  JraTextField jtfGOD = new JraTextField();

  public frmNapuniInv() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void componentShow() {
    jrfCSKL.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
    jrfCSKL.forceFocLost();
  }
  public void okPress() {
    System.out.println("OkPress");
    String strSql="select * from stdoki where vrdok='INM' and god='"+tds.getString("GOD")+"' and cskl='"+tds.getString("CSKL")+"'";
    vl.execSQL(strSql);
    com.borland.dx.sql.dataset.QueryDataSet qds = vl.RezSet;
    do {
      ld.raLocate(dm.getInventura(),"CART",qds.getString("CART"));
      System.out.println("Art: "+dm.getInventura().getString("NAZIV"));
    } while (qds.next());

  }
  public void firstESC() {
  }
  public boolean runFirstESC() {
    return false;
  }
  private void jbInit() throws Exception {
    jp.setLayout(xYLayout1);
    tds.setColumns(new Column[] {
        dm.createStringColumn("CSKL", "Skladište", 12),
        dm.createStringColumn("GOD", "Godina", 4)
    });
    tds.open();
    this.setJPan(jp);

    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setDataSet(this.tds);
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[] {0, 1});
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setRaDataSet(rut.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);

    jtfGOD.setHorizontalAlignment(SwingConstants.CENTER);
    jtfGOD.setColumnName("GOD");
    jtfGOD.setDataSet(tds);

    xYLayout1.setWidth(645);
    xYLayout1.setHeight(85);
    jp.add(jlCSKL, new XYConstraints(15, 20, -1, -1));
    jp.add(jrfCSKL, new XYConstraints(150, 20, 100, -1));
    jp.add(jrfNAZSKL, new XYConstraints(255, 20, 350, -1));
    jp.add(jbCSKL, new XYConstraints(610, 20, 21, 21));
    jp.add(jlGOD, new XYConstraints(15, 45, -1, -1));
    jp.add(jtfGOD, new XYConstraints(150, 45, 100, -1));
  }
  public boolean isIspis() {
    return false;
  }

}