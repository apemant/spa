/****license*****************************************************************
**   file: dlgPrintCjenik.java
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

import hr.restart.sisfun.raUser;
import hr.restart.swing.JraCheckBox;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpitLite;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class dlgPrintCjenik extends raUpitLite {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JPanel jp = new JPanel();
  static QueryDataSet qds = new QueryDataSet();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  static dlgPrintCjenik dPC;
  boolean OKEsc=false;

  public static dlgPrintCjenik getInstance() {
    if(dPC==null)
      dPC = new dlgPrintCjenik();
    return dPC;
  }

  rapancart rpcart = new rapancart() {
    public void nextTofocus(){
      jcbIspisKol.requestFocus();
    }
    public void metToDo_after_lookUp(){
      jcbIspisKol.requestFocus();
    }
  };

  private rapancskl1 rpcskl = new rapancskl1(349) {
    public void findFocusAfter() {
    }
    public void MYpost_after_lookUp(){
      rpcart.SetDefFocus();
    }
  };

  private XYLayout xYLayout1 = new XYLayout();
  private JPanel jpCB = new JPanel();
  private XYLayout xYLayout2 = new XYLayout();
  private JraCheckBox jcbIspisKol = new JraCheckBox();
  private JraCheckBox jcbKolNull = new JraCheckBox();
  private JraCheckBox jcbDescending = new JraCheckBox();
  private TitledBorder ispisTitledBorder;
  private raComboBox rcbSljed = new raComboBox();
  JLabel jlIspis = new JLabel("Ispis");

  com.borland.dx.dataset.TableDataSet tds = new com.borland.dx.dataset.TableDataSet();


  public dlgPrintCjenik() {
    System.out.println("dlgPrintCjenik");
    try {
      jbInit();
    } catch (Exception ex){
    }
    dPC = this;
  }

  private void jbInit() throws Exception {
    tds.setColumns(new com.borland.dx.dataset.Column[] {dm.createStringColumn("SORT",10)});
    tds.open();

    ispisTitledBorder = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,new Color(224, 255, 255),new Color(76, 90, 98),new Color(109, 129, 140)),"");
    rpcskl.setDisabAfter(true);

    rpcart.jrfNAZART.setNextFocusableComponent(jcbIspisKol);
    rpcart.setSearchable(false);

    xYLayout1.setWidth(655);
    xYLayout1.setHeight(180);

    xYLayout2.setWidth(400);
    xYLayout2.setHeight(30);

    jp.setLayout(xYLayout1);
    jpCB.setLayout(xYLayout2);

    jpCB.setBorder(ispisTitledBorder);
    jcbIspisKol.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbIspisKol.setText("Kolièine");
    jcbIspisKol.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jcbIspisKol_focusLost(e);
      }
    });

    jcbKolNull.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbKolNull.setText(" = 0");
    jcbKolNull.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jcbKolNull_itemStateChanged(e);
      }
    });

    rcbSljed.setDataSet(tds);
    rcbSljed.setColumnName("SORT");
    String nazOznaka = Aut.getAut().getCARTdependable("Šifra artikla","Oznaka artikla","Bar code artikla");
    String oznaka = Aut.getAut().getCARTdependable("CART","CART1","BC");
    rcbSljed.setRaItems(new String[][] {
      {nazOznaka,oznaka},
      {"Naziv artikla","NAZART"},
      {"Grupa artikala","CGRART"}
    });

    jlSljed.setText("Sljed");
    jp.add(rpcskl,   new XYConstraints(0, 0, 655, -1));
    jp.add(rpcart,     new XYConstraints(0, 45, 655, 75));

    jp.add(jlIspis,            new XYConstraints(15, 137, -1, -1));
    jp.add(jpCB,                    new XYConstraints(150, 122, 455, 45));

    jpCB.add(jlSljed,    new XYConstraints(15, 9, -1, -1));
    jpCB.add(rcbSljed,       new XYConstraints(100, 7, 150, -1));
    jpCB.add(jcbIspisKol,   new XYConstraints(315, 5, -1, -1));
    jpCB.add(jcbKolNull,    new XYConstraints(393, 5, -1, -1));
    rpcart.setMode(new String("DOH"));
    rpcart.setBorder(null);
    this.setJPan(jp);
  }

  public void componentShow() {
    tds.setString("SORT",Aut.getAut().getCARTdependable("CART","CART1","BC"));
    rcbSljed.setSelectedIndex(0);
    jcbKolNull.setSelected(false);
    jcbIspisKol.setSelected(false);
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

    if(cskl_corg) {
      rpcskl.jrfCSKL.setText(raUser.getInstance().getDefSklad());
      rpcskl.jrfCSKL.forceFocLost();
    }
    else if(!cskl_corg)
      rpcskl.jrfCSKL.requestFocus();
  }


  public boolean runFirstESC() {
    if(!rpcart.jrfCART.getText().equals(""))
      return true;
    if(!rpcskl.jrfCSKL.getText().equals(""))
      return true;
    return false;
  }

  public void firstESC() {
    if(OKEsc) {
      reset();
    } else if(!rpcart.getCART().equals("") || !rpcart.jrfCART.isEnabled()) {
      rcc.EnabDisabAll(rpcart, true);
      rpcart.setCART();
    }
    else if(!rpcskl.jrfCSKL.getText().equals("")) {
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.Clear();
      rpcskl.jrfCSKL.requestFocus();
    }
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return true;
  }

  public void okPress(){
    OKEsc = true;

    if(preparePrint()==0) {
      setNoDataAndReturnImmediately();
//      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//      reset();
//      firstESC(); /** @todo chack this out */
//      return false;
    } else {
      this.killAllReports();
      if (jcbIspisKol.isSelected()) {
        this.addReport("hr.restart.robno.repCjenikKol", "Ispis cjenika s kolièinama", 5);
      } else {
        this.addReport("hr.restart.robno.repCjenik2", "Ispis cjenika", 5);
      }
    }
  }

  boolean podgrupe = false;
  private JLabel jlSljed = new JLabel();

  public boolean Validacija(){
    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))){
      int grupe = JOptionPane.showConfirmDialog(this.jp,"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.CANCEL_OPTION) return false;
      if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }

    rcc.EnabDisabAll(rpcart,false);
    rcc.EnabDisabAll(jpCB,false);

    if(rpcskl.jrfCSKL.getText().equals("")) {
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      rpcskl.jrfCSKL.requestFocus();
      return false;
    }
//
//    if(preparePrint()==0) {
//      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//      reset();
//      firstESC(); /** @todo chack this out */
//      return false;
//    }
//    this.killAllReports();
//    System.out.println("jcbIspisKol.isSelected() - "+ jcbIspisKol.isSelected()); //XDEBUG delete when no more needed
//    if(jcbIspisKol.isSelected()){
//      System.out.println("Ispis cjenika s kolièinama"); //XDEBUG delete when no more needed
//      this.addReport("hr.restart.robno.repCjenikKol","Ispis cjenika s kolièinama",5);
//    } else {
//      System.out.println("Ispis cjenika"); //XDEBUG delete when no more needed
//      this.addReport("hr.restart.robno.repCjenik2","Ispis cjenika",5);
//    }
    return true;
  }

  void jcbIspisKol_focusLost(FocusEvent e) {
    if(rpcskl.jrfCSKL.isEnabled())
      jcbKolNull.setNextFocusableComponent(rpcskl);
    else
      jcbKolNull.setNextFocusableComponent(rpcart);
  }

  void jcbKolNull_itemStateChanged(ItemEvent e) {
    if(rpcskl.jrfCSKL.isEnabled())
      jcbKolNull.setNextFocusableComponent(rpcskl);
    else
      jcbKolNull.setNextFocusableComponent(rpcart);
  }

  private int preparePrint() {
    String god = ut.getYear(ut.getFirstDayOfYear());
    String qStr = "";
    String uvjet= "";

    if (!rpcart.findCART(podgrupe).equals("")){
      uvjet = "AND "+ rpcart.findCART(podgrupe);
    }

    /*rpcart.findCART(podgrupe);*/
//    System.out.println("rpcart.findCART vraca " + uvjet);

    qds.close();

//    if(jcbKolNull.isSelected())
//      qStr = rdUtil.getUtil().getCjenikIspis(rpcart.getCART(), rpcskl.jrfCSKL.getText(),param, true, god, uvjet);
//    else
      qStr = rdUtil.getUtil().getCjenikIspis(rpcart.getCART(), rpcskl.jrfCSKL.getText(),tds.getString("SORT"), jcbKolNull.isSelected(), god, uvjet);
    qds = ut.getNewQueryDataSet(qStr); //.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.prn(qds);
//    qds.open();
    return qds.getRowCount();
  }

  public static QueryDataSet getQDS() {
    return qds;
  }

  public String getSortColumn() {
    return tds.getString("SORT");
  }

  private void reset(){
    OKEsc = false;
    jcbKolNull.setSelected(false);
    jcbIspisKol.setSelected(false);
    rcc.EnabDisabAll(jpCB, true);
  }
}