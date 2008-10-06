/****license*****************************************************************
**   file: frmProdMj.java
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

import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmProdMj extends raSifraNaziv
{

  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  hr.restart.baza.dM dm;
  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfCNAZSKL = new JlrNavField();
  JLabel jLabel1 = new JLabel();
  JraButton jbCSKL = new JraButton();

  public frmProdMj()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    dm = hr.restart.baza.dM.getDataModule();

    //**** setiranje dataseta za frmProdMj
    this.setRaDataSet(dm.getProd_mj());
    this.setRaColumnSifra("CPRODMJ");
    this.setRaColumnNaziv("NAZPRODMJ");

    //**** definicija objekata i dodavanje panela u frmProdMj
    this.setRaText("Blagajna");
    jp.setLayout(xYLayout1);
//    xYLayout1.setWidth(555);
    xYLayout1.setHeight(41);
    jLabel1.setText("Prodajno mjesto");
    jp.setToolTipText("");
    jbCSKL.setText("...");
    this.jpRoot.add(jp,java.awt.BorderLayout.SOUTH);

    //**** punjenje fieldova podacima
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setDataSet(dm.getProd_mj());
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{0, 1});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfCNAZSKL});
    jrfCSKL.setRaDataSet(dm.getSklad());
    jrfCSKL.setNavButton(jbCSKL);
    jrfCNAZSKL.setColumnName("NAZSKL");
    jrfCNAZSKL.setSearchMode(1);
    jrfCNAZSKL.setNavProperties(jrfCSKL);
    //**** dodavanje komponenti na panel
    jp.add(jLabel1,      new XYConstraints(15, 0, -1, -1));
    jp.add(jrfCNAZSKL,      new XYConstraints(255, 0, 285, -1));
    jp.add(jrfCSKL,    new XYConstraints(150, 0, 100, -1));
    jp.add(jbCSKL,        new XYConstraints(545, 0, 21, 21));
  }
  public void SetFokus(char mode) {
    if (mode=='N') {
      System.out.println("set fokus");
      jrfCNAZSKL.setText("");
    }
    super.SetFokus(mode);
  }
}