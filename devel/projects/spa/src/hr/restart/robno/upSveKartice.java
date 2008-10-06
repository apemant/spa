/****license*****************************************************************
**   file: upSveKartice.java
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
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raUpit;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.TableDataSet;
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

public class upSveKartice extends raUpit
{
  hr.restart.robno._Main main;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  String cart;
  String vrzal;
  Valid vl;
  TableDataSet tds = new TableDataSet();
  Column column1 = new Column();
  Column column2 = new Column();
  java.sql.Date dateZ = null;
  java.sql.Date dateP = null;
  Column col1=new Column();
  Column col2=new Column();

  Column colKUl=new Column();
  Column colKIz=new Column();

  Column colZad=new Column();
  Column colRaz=new Column();

  XYLayout xYLayout2 = new XYLayout();
  JraTextField jtfZavDatum = new JraTextField();
  rapancskl rpcskl = new rapancskl()
  {
    public void findFocusAfter()
    {
      jtfPocDatum.requestFocus();
    }
  };
  JraTextField jtfPocDatum = new JraTextField();
  JLabel jLabel1 = new JLabel();
  JPanel jPanel3 = new JPanel();
  JPanel jp = new JPanel();
  dM dm;
  static upSveKartice upk;
  JTextField jtfPocArtikl = new JTextField();
  JTextField jtfZavArtikl = new JTextField();
  JLabel jLabel2 = new JLabel();
  XYLayout xYLayout1 = new XYLayout();

  public upSveKartice() {
    try {
      jbInit();
      upk=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static upSveKartice getupSveKartice(){
    if (upk == null) {
      upk = new upSveKartice();
    }
    return upk;
  }
  public boolean Validacija() {
    if (rpcskl.getCSKL().equals("")) {
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      rpcskl.setCSKL("");
      return false;
    }
    if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;
    return true;
  }
  public void okPress()
  {
    java.math.BigDecimal tempKOL = main.nul;
    java.math.BigDecimal tempVR = main.nul;
    java.math.BigDecimal tempKOL2 = main.nul;
    java.math.BigDecimal tempSUM = main.nul;
    java.math.BigDecimal tempUl = main.nul;
    java.math.BigDecimal tempIz = main.nul;
    java.math.BigDecimal tempUk = main.nul;
    java.math.BigDecimal tempZad = main.nul;
    java.math.BigDecimal tempRaz = main.nul;
    boolean insertNewRow=false;
    dateP = new java.sql.Date(this.tds.getTimestamp("pocDatum").getTime());
    dateZ = new java.sql.Date(this.tds.getTimestamp("zavDatum").getTime());
    String qStr;

    lookupData.getlookupData().raLocate(dm.getSklad(), new String[] {"CSKL"}, new String[] {rpcskl.getCSKL()});
    vrzal = dm.getSklad().getString("VRZAL");

    qStr = rdUtil.getUtil().getSveKartice(jtfPocArtikl.getText(), jtfZavArtikl.getText() , vrzal, rpcskl.getCSKL(),  util.getTimestampValue(tds.getTimestamp("pocDatum"), 0), util.getTimestampValue(tds.getTimestamp("zavDatum"), 1),"" );
    vl.execSQL(qStr);
    vl.RezSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    vl.RezSet.setColumns(new Column[]
    {
      (Column) hr.restart.baza.dM.getDataModule().getDoku().getColumn("BRDOK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getDoku().getColumn("VRDOK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getDoku().getColumn("DATDOK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStdoku().getColumn("CART").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStdoku().getColumn("CART1").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStdoku().getColumn("BC").clone(),
      (Column) colKUl.clone(),
      (Column) colKIz.clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStdoku().getColumn("ZC").clone(),
      (Column) colZad.clone(),
      (Column) colRaz.clone(),
      (Column) col1.clone(),
      (Column) col2.clone()
    });
    openScratchDataSet(vl.RezSet);
    if (vl.RezSet.rowCount() == 0) setNoDataAndReturnImmediately();
//    vl.RezSet.open();
    vl.RezSet.first();
    vl.RezSet.getColumn("VRDOK").setVisible(0);
    setSifraVisible();
      do
      {
        checkClosing();
        tempKOL=tempKOL.add(vl.RezSet.getBigDecimal("KOLUL").add(vl.RezSet.getBigDecimal("KOLIZ").negate()));
        tempVR=tempVR.add(vl.RezSet.getBigDecimal("KOLZAD").add(vl.RezSet.getBigDecimal("KOLRAZ").negate()));
        vl.RezSet.setBigDecimal("SKOL",tempKOL);
        vl.RezSet.setBigDecimal("SIZN",tempVR);
          if (vl.RezSet.getTimestamp("DATDOK").before(tds.getTimestamp("pocDatum")))
          {
            tempUl=tempUl.add(vl.RezSet.getBigDecimal("KOLUL"));
            tempIz=tempIz.add(vl.RezSet.getBigDecimal("KOLIZ"));
            tempZad = tempZad.add(vl.RezSet.getBigDecimal("KOLZAD"));
            tempRaz = tempRaz.add(vl.RezSet.getBigDecimal("KOLRAZ"));
            tempKOL2 = vl.RezSet.getBigDecimal("SKOL");
            tempSUM = vl.RezSet.getBigDecimal("SIZN");
            vl.RezSet.deleteRow();
            insertNewRow=true;
          }
          else
          {
            vl.RezSet.next();
          }
      } while (vl.RezSet.inBounds());
      this.getJPTV().setDataSet(vl.RezSet);
  }

  public void firstESC()
  {
      if(!this.jtfPocArtikl.isEnabled())
      {
        rcc.EnabDisabAll(this.jPanel3, true);
        showDefaultValues();
        this.jtfPocArtikl.requestFocus();
      }
      else
      {
        rcc.EnabDisabAll(this.rpcskl, true);
        rpcskl.setCSKL("");
      }
  }

  public boolean runFirstESC() {
    if (rpcskl.getCSKL().equals("")) {
      return false;
    }
    else {
      return true;
    }
  }
  public void componentShow() {
//    rpcskl.setCSKL(hr.restart.robno.Util.getUtil().findCSKL());
//    rpcskl.setCSKL(raUser.getInstance().getDefSklad());
//    rpcskl.setDisab('S');
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

   if(cskl_corg)
   {
     rpcskl.setCSKL(raUser.getInstance().getDefSklad());
     rpcskl.setDisab('S');
    }
    showDefaultValues();
    if(!cskl_corg)
      rpcskl.jrfCSKL.requestFocusLater();
  }
  private void jbInit() throws Exception {
    this.addReport("hr.restart.robno.repSveKartice", "Kartice Svih Artikala", 5);
    dm = hr.restart.baza.dM.getDataModule();
    vl = hr.restart.util.Valid.getValid();

    col1.setColumnName("SKOL");
    col1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    col1.setDisplayMask("###,###,##0.00");
    col1.setDefault("0");
    col1.setResolvable(false);
    col1.setSqlType(0);
    col1.setCaption("Saldo kol");
    col2.setColumnName("SIZN");
    col2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    col2.setDisplayMask("###,###,##0.00");
    col2.setDefault("0");
    col2.setResolvable(false);
    col2.setSqlType(0);
    col2.setCaption("Saldo izn");

    colKUl.setColumnName("KOLUL");
    colKUl.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    colKUl.setDisplayMask("###,###,##0.00");
    colKUl.setDefault("0");
    colKUl.setResolvable(false);
    colKUl.setSqlType(0);
    colKUl.setCaption("Ulaz");

    colKIz.setColumnName("KOLIZ");
    colKIz.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    colKIz.setDisplayMask("###,###,##0.00");
    colKIz.setDefault("0");
    colKIz.setResolvable(false);
    colKIz.setSqlType(0);
    colKIz.setCaption("Izlaz");


    colZad.setColumnName("KOLZAD");
    colZad.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    colZad.setDisplayMask("###,###,##0.00");
    colZad.setDefault("0");
    colZad.setResolvable(false);
    colZad.setSqlType(0);
    colZad.setCaption("Zaduženje");

    colRaz.setColumnName("KOLRAZ");
    colRaz.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    colRaz.setDisplayMask("###,###,##0.00");
    colRaz.setDefault("0");
    colRaz.setResolvable(false);
    colRaz.setSqlType(0);
    colRaz.setCaption("Razduženje");

    this.setJPan(jp);
    jPanel3.setPreferredSize(new Dimension(655, 65));
    jPanel3.setLayout(xYLayout2);
    jLabel1.setText("Datum (od-do)");

    tds.setColumns(new Column[] {dm.createTimestampColumn("pocDatum"), dm.createTimestampColumn("zavDatum")});

    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setText("jraTextField1");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    rpcskl.setRaMode('S');
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("jraTextField2");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    new raDateRange(jtfPocDatum, jtfZavDatum);



    jp.setLayout(xYLayout1);
    jLabel2.setText("Artikl (od-do)");
    jtfPocArtikl.setHorizontalAlignment(SwingConstants.RIGHT);
    jtfZavArtikl.setHorizontalAlignment(SwingConstants.RIGHT);
    jp.setMinimumSize(new Dimension(555, 120));
    jp.setPreferredSize(new Dimension(645, 120));
    xYLayout1.setWidth(395);
    xYLayout1.setHeight(300);
    jPanel3.add(jLabel1, new XYConstraints(15, 5, -1, -1));
    jPanel3.add(jtfPocDatum, new XYConstraints(150, 5, 100, -1));
    jPanel3.add(jtfZavDatum,  new XYConstraints(255, 5, 100, -1));
    jPanel3.add(jtfPocArtikl,  new XYConstraints(150, 31, 100, -1));
    jPanel3.add(jtfZavArtikl,     new XYConstraints(255, 31, 100, -1));
    jp.add(rpcskl,         new XYConstraints(0, 0, 635, -1));
    jp.add(jPanel3,         new XYConstraints(0, 48, 489, -1));
    jPanel3.add(jLabel2,  new XYConstraints(15, 31, -1, -1));
  }
/**
 * Prikazivanje defaultnih vrijednosti
 */
  void showDefaultValues() {
    tds.setTimestamp("pocDatum",
      hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    rcc.EnabDisabAll(this.rpcskl,false);
    upStanjeNaSkladistu.getInstance().nTransData=0;
    this.jtfPocArtikl.setText("");
    this.jtfZavArtikl.setText("");
    this.getJPTV().setDataSet(null);
  }

  void setSifraVisible()
  {
    vl.RezSet.getColumn("CART").setVisible(0);
    vl.RezSet.getColumn("CART1").setVisible(0);
    vl.RezSet.getColumn("BC").setVisible(0);
    vl.RezSet.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);
  }

  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }
  public String getCskl() {
    return rpcskl.getCSKL();
  }
  public java.sql.Timestamp getPocDatum() {
    return tds.getTimestamp("pocDatum");
  }
  public java.sql.Timestamp getZavDatum() {
    return tds.getTimestamp("zavDatum");
  }
}