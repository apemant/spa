/****license*****************************************************************
**   file: frmSerBr.java
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

import hr.restart.util.raUpit;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class frmSerBr extends raUpit {
  hr.restart.robno._Main main;
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();



  com.borland.dx.sql.dataset.QueryDataSet vArtikli;

  JPanel jp = new JPanel();
  rapancskl rpcskl = new rapancskl()
  {
    public void findFocusAfter() {
      if(!rpcskl.jrfCSKL.getText().equals(""))
      {
        rpcart.setDefParam();
        rpcart.setCART();
      }
    }
  };
  BorderLayout borderLayout1 = new BorderLayout();

  private static frmSerBr upk;



  rapancart rpcart = new rapancart() {
	 public void metToDo_after_lookUp() {
		if (!rpcFocusLost) {
		  rpcFocusLost = true;
		  SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
				SwingUtilities.invokeLater(new Runnable() {
				  public void run() {
					 if (!actionHandled) {
						actionHandled = true;
						okPress();
					 }
				  }
				});
			 }
		  });
		}
	 }
  };

  boolean rpcFocusLost, actionHandled;




  public frmSerBr() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    upk=this;
  }
  public static frmSerBr getupSerBr(){
    if (upk == null) {
      upk = new frmSerBr();
    }
    return upk;
  }
  public void componentShow() {
    rpcskl.setCSKL(hr.restart.robno.Util.getUtil().findCSKL());
    rpcskl.setDisab('S');
    rpcart.SetDefFocus();
    showDefaultValues();
  }

//  public void okPress()
//  {
//    if (rpcskl.getCSKL().equals(""))
//    {
//      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//      rpcskl.setCSKL("");
//      return;
//    }
//
//    if (rpcart.getCART().trim().equals(""))
//    {
//      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos artikla !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
//      rpcart.setCART();
//      return;
//    }
//
//    QueryDataSet  ds = new QueryDataSet();
//    String qStr = rdUtil.getUtil().pregledSB(rpcart.getCART(), rpcskl.getCSKL());
//    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    ds.setColumns(new Column[]
//    {
//      (Column) dm.getSerbr().getColumn("CART").clone(),
//      (Column) dm.getSerbr().getColumn("CSERBR").clone(),
//      (Column) dm.getSerbr().getColumn("RBR").clone(),
//      (Column) dm.getSerbr().getColumn("CSKLUL").clone(),
//      (Column) dm.getSerbr().getColumn("VRDOKUL").clone(),
//      (Column) dm.getSerbr().getColumn("BRDOKUL").clone(),
//      (Column) dm.getSklad().getColumn("NAZSKL").clone(),
//      (Column) dm.getSklad().getColumn("CSKL").clone(),
//      (Column) dm.getArtikli().getColumn("NAZART").clone(),
//      (Column) dm.getArtikli().getColumn("CART").clone(),
//      (Column) dm.getSerbr().getColumn("GODUL").clone()
//    });
//
//    ds.open();
//    if(ds.getRowCount()==0)
//    {
//      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//      rpcFocusLost = false;
//	  actionHandled = false;
//      firstESC();
//    }
//    else
//    {
//      ds.getColumn("CSKLUL").setVisible(0);
//      ds.getColumn("CART").setVisible(0);
//      ds.getColumn("NAZSKL").setVisible(0);
//      ds.getColumn(0).setVisible(0);
//      ds.getColumn(1).setVisible(0);
//      ds.getColumn(2).setVisible(0);
//      ds.getColumn(3).setVisible(0);
//
//      this.getJPTV().setDataSet(ds);
//      rpcFocusLost = false;
//	  actionHandled = false;
//      rcc.EnabDisabAll(jp,false);
//    }
//
//
//
//
//  }
  public void firstESC() {
      if ((rpcart.getCART().trim().equals("")) && this.getJPTV().getDataSet()==null) {
      rcc.EnabDisabAll(this.rpcart, true);
      rcc.EnabDisabAll(this.rpcskl, true);

      showDefaultValues();
      rpcskl.setCSKL("");
    }
    else {
      rpcart.setCART();
      rcc.EnabDisabAll(this.rpcart, true);
      this.getJPTV().setDataSet(null);
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
//  public void keyF6Press() {
//    this.jptv_doubleClick();
//  }
  private void jbInit() throws Exception {
    this.addReport("hr.restart.robno.repSerBr", "Pregled serijskih brojeva", 5);
    this.setJPan(jp);
    jp.setLayout(borderLayout1);
    rpcskl.setRaMode('S');
    rpcart.setBorder(null);
    rpcart.setMode(new String("DOH"));
    rpcart.setSearchable(false);
    jp.add(rpcart, BorderLayout.CENTER);
    jp.add(rpcskl, BorderLayout.NORTH);
  }
/**
 * Prikazivanje defaultnih vrijednosti
 */
  void showDefaultValues() {
    rpcart.setCART();
     rpcFocusLost = false;
	 actionHandled = false;
    this.getJPTV().setDataSet(null);
  }
/**
 * Event za checkBox
 */
  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }
  public String getCskl() {
    return rpcskl.getCSKL();
  }

  public void jptv_doubleClick() {
    _Main.getStartFrame().showFrame("hr.restart.robno.upKartica", res.getString("upFrmKartica_title"));
  }

  public void okPress(){};
  public boolean Validacija()
 {
   if (rpcskl.getCSKL().equals(""))
   {
     rpcskl.jrfCSKL.requestFocus();
     JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
     return false;
   }

   if (rpcart.getCART().trim().equals(""))
   {
     rpcart.jrfCART.requestFocus();
     JOptionPane.showConfirmDialog(this.jp,"Obavezan unos artikla !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);

     return false;
   }

   QueryDataSet  ds = new QueryDataSet();
   String qStr = rdUtil.getUtil().pregledSB(rpcart.getCART(), rpcskl.getCSKL());
   System.out.println("STRING: " + qStr);

   ds.setColumns(new Column[]
   {
     (Column) dm.getSerbr().getColumn("CART").clone(),
     (Column) dm.getSerbr().getColumn("CSERBR").clone(),
     (Column) dm.getSerbr().getColumn("RBR").clone(),
     (Column) dm.getSerbr().getColumn("CSKL").clone(),
     (Column) dm.getSerbr().getColumn("VRDOK").clone(),
     (Column) dm.getSerbr().getColumn("BRDOK").clone(),
     (Column) dm.getSklad().getColumn("NAZSKL").clone(),
     (Column) dm.getArtikli().getColumn("NAZART").clone(),
     (Column) dm.getArtikli().getColumn("CART").clone(),
     (Column) dm.getSerbr().getColumn("GOD").clone()
   });

   ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
   ds.open();
   if(ds.getRowCount()==0)
   {
     JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
     rpcFocusLost = false;
         actionHandled = false;
     firstESC();
     return false;
   }
   else
   {
     ds.getColumn("CSKL").setVisible(0);
     ds.getColumn("CART").setVisible(0);
     ds.getColumn("NAZSKL").setVisible(0);
     ds.getColumn(0).setVisible(0);
     ds.getColumn(1).setVisible(0);
     ds.getColumn(2).setVisible(0);
     ds.getColumn(3).setVisible(0);

     this.getJPTV().setDataSet(ds);
     rpcFocusLost = false;
         actionHandled = false;
     rcc.EnabDisabAll(jp,false);
   }
   return true;
 }
}