/****license*****************************************************************
**   file: dlgPregledRUC.java
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
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraCheckBox;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpit;

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

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class dlgPregledRUC extends raUpit {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JPanel jp = new JPanel();

  private XYLayout xYLayout1 = new XYLayout();
  private JPanel jpCB = new JPanel();
  private XYLayout xYLayout2 = new XYLayout();
  private JraCheckBox jcbIspisKol = new JraCheckBox();
  private JraCheckBox jcbKolNull = new JraCheckBox();
  private TitledBorder ispisTitledBorder;

  private raComboBox rcbSljed = new raComboBox();
  JLabel jlSljed = new JLabel("Slijed");
  JLabel jlIspis = new JLabel("Ispis");
  com.borland.dx.dataset.TableDataSet tds = new com.borland.dx.dataset.TableDataSet();

  QueryDataSet result = new QueryDataSet();
  static QueryDataSet qds = new QueryDataSet();
  private QueryDataSet fakeArtikls = Aut.getAut().getFakeArtikl();

  protected rapancskl1 rpcskl = new rapancskl1(348) {
    public void MYpost_after_lookUp(){
      rpcart.setGodina(Aut.getAut().getKnjigodRobno());
      rpcart.setCskl(rpcskl.getCSKL());
      rpcart.setDefParam();
      rpcart.setCART();
    }
  };

  protected rapancart rpcart = new rapancart(){
    public void metToDo_after_lookUp() {
    }
    /*public void nextTofocus() {
      rcbSljed.requestFocus();
    }*/
  };

  public dlgPregledRUC() {
    try{
      jbInit();
    } catch (Exception ex){
      ex.printStackTrace();
    }
    dpr = this;
  }

  private static dlgPregledRUC dpr;

  public static dlgPregledRUC getInstance(){
    if(dpr==null)
      dpr = new dlgPregledRUC();
    return dpr;
  }

  public void componentShow() {
    tds.setString("SORT",Aut.getAut().getCARTdependable("CART","CART1","BC"));
    rcbSljed.setSelectedIndex(0);
    getJPTV().clearDataSet();
    jcbKolNull.setSelected(false);
    jcbIspisKol.setSelected(false);
    rpcart.EnabDisab(true);
    rcc.EnabDisabAll(jpCB, true);
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

    if(cskl_corg) {
      rpcskl.jrfCSKL.setText(raUser.getInstance().getDefSklad());
      rpcskl.jrfCSKL.forceFocLost();
      rpcart.setCARTLater();
    }
    else if(!cskl_corg)
      rpcskl.jrfCSKL.requestFocus();
  }

  boolean podgrupe = false;

  public boolean Validacija() {
    if(rpcskl.jrfCSKL.getText().equals("")) {
      rpcart.EnabDisab(true);
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,
                                    "Obavezan unos skladišta !",
                                    "Greška",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.WARNING_MESSAGE);

      return false;
    }

    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))){
      int grupe = JOptionPane.showConfirmDialog(this.jp,"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }

    return true;
  }

  public void okPress() {

    // BOB - ex prepare print (mm - problematicno) (ndm - ???)

    String god = hr.restart.util.Util.getUtil().getYear(hr.restart.util.Util.getUtil().getFirstDayOfYear());
    String qStr = "";
    String uvjet= "";

    if (!rpcart.findCART(podgrupe).equals("")){
      uvjet = " AND "+ rpcart.findCART(podgrupe) + 
              " AND " + raVart.getStanjeCond() + " ";
    } else {
      uvjet = " AND " + raVart.getStanjeCond() + " ";
    }

    qds.close();
    qStr = rdUtil.getUtil().getMarzaIspis(rpcart.getCART(), rpcskl.jrfCSKL.getText(),tds.getString("SORT"), jcbKolNull.isSelected(), god, uvjet);
    qds = hr.restart.util.Util.getUtil().getNewQueryDataSet(qStr, false);
    openDataSet(qds);
    if (qds.rowCount() == 0) setNoDataAndReturnImmediately();

    // EOB - ex prepare print

    this.killAllReports();
    if(jcbIspisKol.isSelected())
      this.addReport("hr.restart.robno.repMarzaKol", "Ispis marže s kolièinama", 5);
    else
      this.addReport("hr.restart.robno.repMarza", "Ispis marže", 5);

    result.getColumn("CSKL").setVisible(0);
    result.getColumn("CART").setVisible(0);
    result.getColumn("CART1").setVisible(0);
    result.getColumn("BC").setVisible(0);
    result.getColumn("CGRART").setVisible(0);

    if(!result.isOpen()) result.open();

    if(!qds.isOpen()) qds.open();

    result.deleteAllRows();
    qds.first();
    while(qds.inBounds()) {
      result.insertRow(false);
      dM.copyColumns(qds,result);
      qds.next();
    }
    result.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);

    if (rcbSljed.getSelectedIndex() == 3 || rcbSljed.getSelectedIndex() == 4){
      result.setSort(new SortDescriptor(new String[]{tds.getString("SORT")},true,true));
    } else {
      result.setSort(new SortDescriptor(new String[]{tds.getString("SORT")}));
    }
    result.first();
//    this.getJPTV().setKumTak(true);
//    this.getJPTV().setStoZbrojiti(new String[] {"NC","VC","RUC"});
    this.getJPTV().setDataSet(result);
//    this.getJPTV().init_kum();
  }

  public boolean runFirstESC() {
    return !rpcskl.jrfCSKL.getText().equals("");
  }

  public void firstESC() {
    if(getJPTV().getDataSet()!=null || isInterrupted()) {
      getJPTV().clearDataSet();
      rcc.EnabDisabAll(jpCB, true);
      rpcart.EnabDisab(true);
      rpcart.setCART();
    }
    else if(!rpcart.getCART().equals("") || !rpcart.jrfCART.isEnabled()) {
      //jcbKolNull.setSelected(false);
      //jcbIspisKol.setSelected(false);
      rpcart.EnabDisab(true);
      rpcart.setCART();
    }
    else if(!rpcskl.jrfCSKL.getText().equals("")) {
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.Clear();
      rpcskl.jrfCSKL.requestFocus();
    }
  }

  private void jbInit() throws Exception {
    result.setColumns( new Column[] {
      (Column) dM.getDataModule().getStanje().getColumn("CSKL").clone(),
      (Column) dM.getDataModule().getArtikli().getColumn("CART").clone(),
      (Column) dM.getDataModule().getArtikli().getColumn("CART1").clone(),
      (Column) dM.getDataModule().getArtikli().getColumn("BC").clone(),
      (Column) dM.getDataModule().getArtikli().getColumn("NAZART").clone(),
      (Column) dM.getDataModule().getArtikli().getColumn("CGRART").clone(),
      (Column) dM.getDataModule().getStanje().getColumn("KOL").clone(),
      dM.createBigDecimalColumn("NC","Nabavna cijena",2),
      (Column) dM.getDataModule().getStanje().getColumn("VC").clone(),
      dM.createBigDecimalColumn("MARZA","Ruc %",2),
      dM.createBigDecimalColumn("RUC","Ruc",2)
    });

    rpcskl.setDisabAfter(true);

    tds.setColumns(new com.borland.dx.dataset.Column[] {dm.createStringColumn("SORT",10)});
    tds.open();
    ispisTitledBorder = new TitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,new Color(224, 255, 255),new Color(76, 90, 98),new Color(109, 129, 140)),"");

    xYLayout1.setWidth(655);
    xYLayout1.setHeight(180);
    jp.setLayout(xYLayout1);
    jpCB.setLayout(xYLayout2);
    xYLayout2.setWidth(400);
    xYLayout2.setHeight(30);
    jpCB.setBorder(ispisTitledBorder);

    jcbIspisKol.setText("Kolièine");
    jcbIspisKol.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jcbIspisKol_focusLost(e);
      }
    });

    jcbKolNull.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbKolNull.setText("= 0");
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
      {"Grupa artikala","CGRART"},
      {"Razlika u cijeni %","MARZA"},
      {"Razlika u cijeni","RUC"}
    });

    jpCB.add(jlSljed,    new XYConstraints(15, 9, -1, -1));
    jpCB.add(rcbSljed,       new XYConstraints(100, 7, 150, -1));
    jpCB.add(jcbIspisKol,   new XYConstraints(315, 5, -1, -1));
    jpCB.add(jcbKolNull,    new XYConstraints(393, 5, -1, -1));

    jp.add(rpcskl,   new XYConstraints(0, 0, 655, -1));
    jp.add(rpcart,     new XYConstraints(0, 45, 655, 75));
    jp.add(jlIspis,          new XYConstraints(15, 137, -1, -1));
    jp.add(jpCB,          new XYConstraints(150, 122, 455, 45));

    this.setJPan(jp);

    rpcart.setDefParam();
    rpcart.setTabela(fakeArtikls);
    rpcart.setBorder(null);
    rpcart.setMode("DOH");
    rpcart.setSearchable(false);
    rpcart.InitRaPanCart();
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

  public static QueryDataSet getQDS() {
    return qds;
  }

  public String getParam(){
    return hr.restart.sisfun.frmParam.getParam("robno","indiCart");
  }
}

