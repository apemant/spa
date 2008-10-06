/****license*****************************************************************
**   file: upPregledNorm.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raLLFrames;
import hr.restart.util.raUpit;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYLayout;

public class upPregledNorm extends raUpit {
  hr.restart.robno._Main main;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = hr.restart.baza.dM.getDataModule();
  Valid vl = hr.restart.util.Valid.getValid();

  XYLayout xYLayout2 = new XYLayout();
  rapancskl rpcskl = new rapancskl() {
    public void findFocusAfter() {
      rpcart.setDefParam();
      rpcart.SetDefFocus();
    }
  };
  BorderLayout borderLayout1 = new BorderLayout();
  rapancart rpcart = new rapancart() {
    public void nextTofocus() {
      if (!rpcFocusLost) {
        getOKPanel().jPrekid.requestFocus();
      }
    }
    public void metToDo_after_lookUp() {
      if (!rpcFocusLost) {
        rpcFocusLost = true;
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            rpcFocusLost = false;
            ok_action();
          }
        });
      }
    }
  };

  boolean rpcFocusLost;

  StorageDataSet ds;

  JPanel jpMainPanel = new JPanel();
  static upPregledNorm instanceOfMe;

  public static upPregledNorm getInstance(){
    return instanceOfMe;
  }

  public upPregledNorm() {
    try {
      jbInit();
      instanceOfMe = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void componentShow() {
    rpcskl.setCSKL(hr.restart.robno.Util.getUtil().findCSKL());
    rpcskl.setDisab('S');
    rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
    rpcart.SetDefFocus();
    this.getJPTV().clearDataSet();
    showDefaultValues();
  }

  public boolean Validacija() {
    /*if (rpcFocusLost) return false; */
    rpcFocusLost = true;
    if (rpcskl.getCSKL().equals("")) {
      JOptionPane.showMessageDialog(this.getWindow(),"Obavezan unos skladišta !",
          "Greška",JOptionPane.ERROR_MESSAGE);
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.setCSKL("");
      rpcFocusLost = false;
      return false;
    }
    if (rpcart.getCART().equals("")) {
      JOptionPane.showMessageDialog(this.getWindow(),"Obavezan unos artikla !","Greška",JOptionPane.ERROR_MESSAGE);
      rpcFocusLost = false;
      rpcart.setCART();
      return false;
    }
    return true;
  }

  public void okPress() {

    String cskl = rpcskl.getCSKL();
    String god = vl.getKnjigYear("robno");
    String[] cols = {"CSKL", "GOD", "CART"};
    
    int cart = Aus.getNumber(rpcart.getCART());
    DataSet expand = Aut.getAut().expandArt(cart, new BigDecimal(1), true);
    if (expand == null || expand.rowCount() == 0) setNoDataAndReturnImmediately();
    
    ds.empty();
    for (expand.first(); expand.inBounds(); expand.next()) {
      checkClosing();
      cart = expand.getInt("CART");
      ds.insertRow(false);
      Aut.getAut().copyArtFields(ds, expand);
      ds.setBigDecimal("KOL", expand.getBigDecimal("KOL"));
      DataSet st = Stanje.getDataModule().getTempSet(Condition.whereAllEqual(cols, 
          new Object[] {cskl, god, new Integer(cart)}));
      st.open();
      if (st.rowCount() > 0) 
        ds.setBigDecimal("NC", st.getBigDecimal("NC"));
      else {
        DataSet art = Artikli.getDataModule().getTempSet(Condition.equal("CART", cart));
        art.open();
        ds.setBigDecimal("NC", art.getBigDecimal("NC"));
      }
      ds.setBigDecimal("INAB", util.multiValue(ds.getBigDecimal("KOL"), 
          ds.getBigDecimal("NC")));
    }
    String ic = Aut.getAut().getIndiCART();
    ds.getColumn("CART").setWidth(6);
    ds.getColumn("CART").setVisible(ic.equalsIgnoreCase("CART") ? 1 : 0);
    ds.getColumn("CART1").setVisible(ic.equalsIgnoreCase("CART1") ? 1 : 0);
    ds.getColumn("BC").setVisible(ic.equalsIgnoreCase("BC") ? 1 : 0);
    this.getJPTV().setDataSetAndSums(ds, new String[] {"INAB"});
    this.killAllReports();
  }
 

  public void firstESC() {
    if (this.getJPTV().getStorageDataSet() != null || rpcart.getCART().length() > 0)  {
      rpcFocusLost = false;
      this.getJPTV().clearDataSet();
      rpcart.EnabDisab(true);
      rpcart.setCART();
    } else {
      rcc.EnabDisabAll(rpcskl, true);
      rpcskl.setCSKL("");
      //rpcskl.jrfCSKL.requestFocusLater();
    }
  }
  
  public boolean runFirstESC() {
    return rpcskl.getCSKL().length() != 0;
  }
  
  private void createDataSet() {
    ds = new StorageDataSet();
    ds.setColumns(new Column[] {
        (Column) dm.getArtikli().getColumn("CART").clone(),
        (Column) dm.getArtikli().getColumn("CART1").clone(),
        (Column) dm.getArtikli().getColumn("BC").clone(),
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getArtikli().getColumn("JM").clone(),
        (Column) dm.getStdoki().getColumn("KOL").clone(),
        (Column) dm.getStdoki().getColumn("NC").clone(),
        (Column) dm.getStdoki().getColumn("INAB").clone(),
    });
    ds.open();
  }

  private void jbInit() throws Exception {
    createDataSet();
    this.setJPan(jpMainPanel);
    rpcart.setMode("DOH");
    rpcart.setBorder(null);

    rpcskl.setRaMode('S');

    jpMainPanel.setLayout(borderLayout1);
    jpMainPanel.add(rpcart, BorderLayout.CENTER);
    jpMainPanel.add(rpcskl, BorderLayout.NORTH);
    raLLFrames.getRaLLFrames().getMsgStartFrame().centerFrame(this,0,getTitle());
  }

  void showDefaultValues() {
    this.rpcskl.setCSKL(hr.restart.sisfun.raUser.getInstance().getDefSklad()); //sgQuerys.getSgQuerys().getDefSklad());
//    rcc.EnabDisabAll(this.rpcskl, false); //TODO zasad vako, kasnije vidit èemo kako....
      
      rpcFocusLost = false;
      rpcart.EnabDisab(true);
      rpcart.setCART();
  }

  public boolean isIspis(){
    return false;
  }

  public String getCskl() {
    return rpcskl.getCSKL();
  }
  public int getCart() {
    return Integer.parseInt(rpcart.getCART());
  }
}
// ura 1235, 2931