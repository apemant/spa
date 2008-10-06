/****license*****************************************************************
**   file: upUnFuckDoc.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raUpitFat;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
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
 * <p>Comment: ovo ništa nevalja</p>
 * @author unascribed
 * @version 1.0
 */

public class upUnFuckDoc extends raUpitFat {

  JPanel jPanel3 = new JPanel();
  JPanel jp = new JPanel();

  hr.restart.robno._Main main;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  TableDataSet tds = new TableDataSet();
  java.sql.Date dateP = null;

  static upUnFuckDoc upk;
  XYLayout xYLayout1 = new XYLayout();
  Valid vl;
  dM dm;
  String sSklSelected = "";
  JLabel jLabel2 = new JLabel();
  JraTextField jtfZavDatum = new JraTextField();
  BorderLayout borderLayout1 = new BorderLayout();


  hr.restart.robno.rapancskl rpcskl = new hr.restart.robno.rapancskl() {
      public void findFocusAfter() {
        jtfZavDatum.requestFocus();
        jtfZavDatum.selectAll();
      }
  };

  //*** konstruktor
  public upUnFuckDoc() {
    try {
      jbInit();
      upk=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

   public static upUnFuckDoc getupUnFuckDoc(){
    if (upk == null) {
      upk = new upUnFuckDoc();
    }
    return upk;
  }
   
   private int[] kols;

  public void okPress(){
    String qStr="";
    String sSkl = "";

    if(!rpcskl.jrfCSKL.getText().equals("")) {
      sSkl = " AND DOKI.CSKL ='"+rpcskl.jrfCSKL.getText()+"'";
      sSklSelected = "D";
    }
    rcc.EnabDisabAll(jp, false);
    rcc.EnabDisabAll(rpcskl, false);
//    qStr=rdUtil.getUtil().getUnFuck(sSkl);
    
    qStr = "SELECT max(DOKI.CSKL) as cskl , " +
            "max(DOKI.BRDOK) as BRDOK, " +
            "max(DOKI.god) as god, "+
            "max(DOKI.CPAR) as cpar, " +
            "max(DOKI.DATDOK) as datdok, " +
            "sum(STDOKI.IRAZ) as iraz "+

            "FROM DOKI, STDOKI "+

            "WHERE doki.cskl = stdoki.cskl "+
            "AND doki.vrdok = stdoki.vrdok "+
            "AND doki.god = stdoki.god "+
            "AND doki.brdok = stdoki.brdok "+
            "AND DOKI.AKTIV='D' "+ 
            "and STATIRA='N' "+
            "AND DOKI.VRDOK = 'OTP' "+
            "and doki.datdok <= '"+hr.restart.util.Util.getUtil().getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"' "+
            sSkl+

            "group by doki.cskl, doki.god, doki.brdok";
    
    System.out.println("\n\n"+qStr+"\n\n"); //XDEBUG delete when no more needed

    vl.execSQL(qStr);
    vl.RezSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    vl.RezSet.setColumns(new Column[] {
        dm.createStringColumn("CSKL","Skladište",10),
        dm.createIntColumn("BRDOK","Broj otpremnice"),
        dm.createStringColumn("GOD","Godina",4),
        dm.createIntColumn("CPAR","Partner"),
        dm.createTimestampColumn("DATDOK","Datum otpremnice"),
        dm.createBigDecimalColumn("IRAZ","Razduženje")
    });
    vl.RezSet.open();
    vl.RezSet.first();
    
    if(!rpcskl.jrfCSKL.getText().equals(""))
      vl.RezSet.getColumn("CSKL").setVisible(0);
    
    
    int counter = 0;
    
    for (int i=0; i<vl.RezSet.columnCount(); i++){
      System.out.println("kolona "+i+" oznake "+vl.RezSet.getColumn(i).getColumnName()+" zove se "+vl.RezSet.getColumn(i).getCaption()+" getVisible="+vl.RezSet.getColumn(i).getVisible()); //XDEBUG delete when no more needed
      if (vl.RezSet.getColumn(i).getVisible() != 0){
        counter++;
      }
    }
    
    if(vl.RezSet.getRowCount()>0) {
      setDataSet(vl.RezSet);
      //getJPTV().getMpTable().requestFocus();
    } else {
      setNoDataAndReturnImmediately();
    }
  }

  // handlanje pritiska na tipku ESC
  public void firstESC() {
    if(getJPTV().getDataSet()==null) {
      rcc.EnabDisabAll(jPanel3, true);
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.setCSKL("");
    } else {
      showDefaultValues();
      removeNav();
    }
  }

  public boolean runFirstESC() {
    if (rpcskl.getCSKL().equals("")) {
      return false;
    } else {
      return true;
    }
  }

  //*** init metoda
  private void jbInit() throws Exception {
    jPanel3.setLayout(xYLayout1);
    addReport("hr.restart.robno.repUnFuckDoc", "Ispis nefakturiranih otpremnica", 5);
    dm = hr.restart.baza.dM.getDataModule();
    vl = hr.restart.util.Valid.getValid();
    rpcskl.setRaMode('S');
    rpcskl.setCSKL("");

    setJPan(jp);

    jp.setLayout(borderLayout1);
    jp.setMinimumSize(new Dimension(555, 43));
    jp.setPreferredSize(new Dimension(650, 100));

    jp.add(rpcskl, BorderLayout.NORTH);
    jp.add(jPanel3, BorderLayout.CENTER);

    jPanel3.setMinimumSize(new Dimension(555, 70));
    jPanel3.setPreferredSize(new Dimension(650, 42));

    jLabel2.setText("Datum (od-do)");
    jPanel3.add(jLabel2,      new XYConstraints(15, 5, -1, -1));
    jPanel3.add(jtfZavDatum,       new XYConstraints(150, 5, 100, -1));


    tds.setColumns(new Column[] {
        dm.createTimestampColumn("zavDatum")
        });
    
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    showDefaultValues();
  }

  //*** Defaultne vrijednosti dialoga
  String defSKL = hr.restart.sisfun.raUser.getInstance().getDefSklad();

  void showDefaultValues() {
    rcc.EnabDisabAll(jPanel3, true);
    tds.open();
    rpcskl.setCSKL(defSKL);
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    setDataSet(null);
    jtfZavDatum.requestFocus();
    jtfZavDatum.selectAll();
  }

  //***************************************************************************
  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }


  public void componentShow() {
    showDefaultValues();
  }

  public String navDoubleClickActionName() {
    return "Otpremnica";
  }

  public int[] navVisibleColumns() {
    return kols; //new int[] {0,1,2,3,4};
  }
  
  
  public void jptv_doubleClick() {
    String cskl = this.getJPTV().getDataSet().getString("CSKL");
    String brdok = this.getJPTV().getDataSet().getInt("BRDOK")+"";
    String vrdok = "OTP";
    String god = this.getJPTV().getDataSet().getString("GOD");
    
    System.out.println(cskl+", "+vrdok+", "+god+", "+brdok);
    raMasterDetail.showRecord("hr.restart.robno.raOTP", 
        new String[]{"CSKL", "VRDOK", "GOD", "BRDOK"}, 
        new String[]{cskl, vrdok, god, brdok}
    );
  }
}
