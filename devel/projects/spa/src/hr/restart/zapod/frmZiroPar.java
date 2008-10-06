/****license*****************************************************************
**   file: frmZiroPar.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.lookupData;
import hr.restart.util.raMatPodaci;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.QueryDataSet;
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

public class frmZiroPar extends raMatPodaci {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Valid vl = new hr.restart.util.Valid();
  frmPartneri fP;
  int cpar;
  QueryDataSet paramQDS = new QueryDataSet();

  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  hr.restart.zapod.jpGetValute jpVal = new hr.restart.zapod.jpGetValute();
  JraTextField jrfZiroRac = new JraTextField();
  JLabel jlZiroRac = new JLabel();
  JraCheckBox jcbDev = new JraCheckBox();

  public frmZiroPar(frmPartneri f, QueryDataSet tempParamQDS, int cpar) {
    super(2);
    try {
      fP = f;
      this.paramQDS = tempParamQDS;
      this.cpar = cpar;
      fP.setEnabled(false);
      paramQDS.open();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char parm1)
  {
    jpVal.initJP(parm1);
//    jpVal.jtOZNVAL.forceFocLost();
    if(parm1=='N')
    {
      jcbDev.setSelected(false);
      jpVal.jtOZNVAL.setText("KN");
      jpVal.jtOZNVAL.forceFocLost();
      rcc.EnabDisabAll(jpVal, false);
      jrfZiroRac.requestFocus();
    }
    else if (parm1=='I')
    {
      jrfZiroRac.requestFocus();
    }
  }

  public boolean Validacija(char parm1)
  {

    if(vl.isEmpty(jrfZiroRac))
      return false;
    getRaQueryDataSet().setInt("CPAR", cpar);
    getRaQueryDataSet().setString("OZNVAL", jpVal.jtOZNVAL.getText());

    return true;

  }
  private void jbInit() throws Exception
  {
/*
    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-555;
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-360;
    this.setLocation((int)x/2,(int)y/2);
*/
    this.setRaQueryDataSet(paramQDS);


    jpVal.setRaDataSet(getRaQueryDataSet());

    jpVal.setBorderVisible(false);

    this.setTitle("Žiro partneri - " + fP.getRaQueryDataSet().getString("NAZPAR"));
//    this.setSize(555, 360);

    jPanel1.setLayout(xYLayout1);

//    this.getJpTableView().setMinimumSize(new Dimension(555, 150));
    jlZiroRac.setText("Žiro ra\u010Dun");

    jcbDev.setColumnName("DEV");
    jcbDev.setDataSet(getRaQueryDataSet());
    jcbDev.setSelectedDataValue("D");
    jcbDev.setUnselectedDataValue("N");
    jcbDev.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbDev_actionPerformed(e);
      }
    });
    jcbDev.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbDev.setText("Devizni ra\u010Dun");

    jrfZiroRac.setColumnName("ZIRO");
    jrfZiroRac.setDataSet(getRaQueryDataSet());

//    xYLayout1.setWidth(530);
//    xYLayout1.setHeight(110);
    jPanel1.add(jpVal, new XYConstraints(0, 45, -1, -1));
    jPanel1.add(jrfZiroRac,       new XYConstraints(150, 20, 200, -1));
    jPanel1.add(jlZiroRac,    new XYConstraints(15, 20, -1, -1));
    jPanel1.add(jcbDev,      new XYConstraints(383, 22, -1, -1));
    setRaDetailPanel(jPanel1);
  }

//  public void this_hide()
//  {
//    hr.restart.zapod.frmPartneri.getFrmPartneri().setEnabled(true);
//    hr.restart.zapod.frmPartneri.getFrmPartneri().show();
//    super.this_hide();
//  }
  public void ZatvoriOstalo() {
    fP.setEnabled(true);
    fP.toFront();
  }

  public void EntryPoint(char mode)
  {
    if(mode=='N')
    {
      rcc.EnabDisabAll(jpVal, false);
    }
  }

  void jcbDev_actionPerformed(ActionEvent e) {
    if(jcbDev.isSelected()== false)
    {
      jpVal.jtOZNVAL.setText("KN");
      jpVal.jtOZNVAL.forceFocLost();
      rcc.EnabDisabAll(jpVal, false);
      jcbDev.requestFocus();
    }
    else
    {
      jpVal.jtOZNVAL.setText("");
      jpVal.jtOZNVAL.forceFocLost();
      rcc.EnabDisabAll(jpVal, true);
      jpVal.jtOZNVAL.requestFocus();
    }
  }
  public static boolean addZiroPar(int _cpar, String _zr, String _oznval) {
    if (_zr == null || _zr.trim().equals("")) return true;
    String ozv,dev;
    if (_oznval == null || _oznval.trim().equals("")) {
      ozv = Tecajevi.getDomOZNVAL();
      dev = "N";
    } else {
      if (lookupData.getlookupData().raLocate(hr.restart.baza.dM.getDataModule().getValute(),"OZNVAL",_oznval)) {
        ozv = _oznval;
        dev = hr.restart.baza.dM.getDataModule().getValute().getString("STRVAL");
      } else {
        ozv = Tecajevi.getDomOZNVAL();
        dev = "N";
      }
    }
    QueryDataSet zirci = hr.restart.baza.Ziropar.getDataModule()
      .getTempSet(hr.restart.baza.Condition.equal("ZIRO", _zr));
    zirci.open();
    if (zirci.getRowCount() > 0) {
      if (zirci.getInt("CPAR") == _cpar) return true;
      return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, 
          new raMultiLineMessage("Isti žiro raèun veæ je unesen za partnera " + zirci.getInt("CPAR") +
              "!\nNastaviti ipak (žiro raèun neæe biti zapisan)?"), "Žiro raèun veæ postoji",
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }
    int res = JOptionPane.showConfirmDialog(
        null,"Dodati Žiro ra\u010Dun "+_zr+"-"+ozv+" za partnera "+_cpar+" ?","Žiro ra\u010Duni",JOptionPane.YES_NO_CANCEL_OPTION);
    //0-Da, 1-Ne, 2-Ponisti
    if (res == 0) {
      zirci.insertRow(false);
      zirci.setInt("CPAR", _cpar);
      zirci.setString("ZIRO", _zr);
      zirci.setString("OZNVAL", ozv);
      zirci.setString("DEV", dev);
      zirci.post();
      zirci.saveChanges();
      dM.getSynchronizer().markAsDirty("ziropar");
    }
    return res!=2;
  }
    
  public void AfterDelete()  {
    jpVal.jtOZNVAL.forceFocLost();
  }
}
