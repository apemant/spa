/****license*****************************************************************
**   file: raPan4UnosInv.java
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
import hr.restart.util.PreSelect;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raPan4UnosInv extends JPanel {
  dM dm = hr.restart.baza.dM.getDataModule();
  BorderLayout borderLayout = new BorderLayout();
  raCommonClass rcc = new raCommonClass();
  QueryDataSet qdsInventura;
  TableDataSet tds = new TableDataSet();
  rapancart rpcrt = new rapancart(){
    public void nextTofocus(){}
    public void metToDo_after_lookUp(){}
  };

  PreSelect pres = new PreSelect() {
    public void SetFokus() {
//      System.out.println("pres kaze: fokus");
      jrfCSKL.setText("");
      jrfNAZSKL.setText("");
      jrfCSKL.requestFocus();
    }
//    public boolean Validacija() {
//  //    jrfCSKL.setRaDataSet(sgQuerys.getSgQuerys().getSkladistaUInventuri(hr.restart.zapod.OrgStr.getKNJCORG()));
//      hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//      syst.prn(jrfCSKL.getRaDataSet());
//      String validir = jrfCSKL.getRaDataSet().getString("CSKL");
//      if (validir.equals("")){
//        JOptionPane.showConfirmDialog(null,"Skladište nije uneseno","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//        return false
//        ;
//      }
//      return true;
//    }
  };

  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfNAZSKL = new JlrNavField();

  XYLayout xYLayout2 = new XYLayout();
  JraButton jbCSKL = new JraButton();
  JPanel jpSel = new JPanel();
  JLabel jLabel1 = new JLabel();
  XYLayout xYLayout3 = new XYLayout();
  JraTextField jtfINVKOL = new JraTextField();
  JLabel jlKolicina = new JLabel();
  Border border1;
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();

  public raPan4UnosInv() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  Column column0 = new Column();

  void jbInit() throws Exception {
    column0 = dm.createStringColumn("CSKL", 10);
    tds.setColumns(new Column[] {column0});
    tds.open();
    border1 = BorderFactory.createEtchedBorder(new Color(224, 255, 255),new Color(109, 129, 140));
    this.setLayout(xYLayout3);
    rpcrt.setMode("DOH");

    // --------------------preselect----------------------BOB

    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[]{0,1,2});
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setRaDataSet(sgQuerys.getSgQuerys().getSkladistaUInventuri(hr.restart.zapod.OrgStr.getKNJCORG()));
    jrfCSKL.setDataSet(tds);
    jrfCSKL.setNavButton(jbCSKL);

    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);

    // --------------------preselect----------------------EOB

    xYLayout2.setWidth(604);
    xYLayout2.setHeight(63);
    jbCSKL.setText("...");
    jpSel.setLayout(xYLayout2);
    jLabel1.setText("Skladište");
    xYLayout3.setWidth(662);
    xYLayout3.setHeight(112);
    jtfINVKOL.setDataSet(dm.getInventura());
    jtfINVKOL.setColumnName("KOLINV");
    jlKolicina.setText("Koli\u010Dina");
    this.setMinimumSize(new Dimension(660, 112));
    this.setPreferredSize(new Dimension(660, 113));
    jpSel.setBorder(border1);
    jpSel.setPreferredSize(new Dimension(537, 74));
    jLabel2.setText("Šifra");
    jLabel3.setText("Naziv");

    this.add(rpcrt,                new XYConstraints(0, 0, 656, 73));
    this.add(jlKolicina,      new XYConstraints(15, 75, -1, -1));
    this.add(jtfINVKOL,      new XYConstraints(150, 75, 100, -1));

    jpSel.add(jrfCSKL, new XYConstraints(150, 20, 100, -1));
    jpSel.add(jrfNAZSKL,       new XYConstraints(255, 20, 260, -1));
    jpSel.add(jbCSKL,        new XYConstraints(520, 20, 21, 21));
    jpSel.add(jLabel1,    new XYConstraints(15, 20, -1, -1));
    jpSel.add(jLabel2,     new XYConstraints(150, 5, -1, -1));
    jpSel.add(jLabel3,     new XYConstraints(255, 5, -1, -1));
//    jpSel.add(jfrDATINV,     new XYConstraints(150, 55, 100, -1));

    pres.setSelDataSet(dm.getInventura());
    pres.setSelPanel(this.jpSel);
  }
}