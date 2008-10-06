/****license*****************************************************************
**   file: upArtSklad.java
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
import hr.restart.sisfun.Asql;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.sql.dataset.QueryDataSet;

public class upArtSklad extends raUpitFat {
  _Main ma;
  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  //hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  Valid vl = Valid.getValid();

  rapancart rpc = new rapancart() {
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

  private static upArtSklad upas;
  private com.borland.dx.sql.dataset.QueryDataSet qdsFakeArtikl;

  public upArtSklad() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    upas = this;
  }
  public static upArtSklad getInstance() {
    return upas;
  }
  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }

  public void componentShow() {

    showDefaults();
    rpc.setCART();

    //rpc.requestFocus();
  }

  public boolean Validacija() {
    if (rpcFocusLost) return false;
    rpcFocusLost = true;
    if (rpc.getCART().equals("")) {
      JOptionPane.showMessageDialog(this.getWindow(),"Obavezan unos artikla !","Greška",JOptionPane.ERROR_MESSAGE);
      rpcFocusLost = false;
      rpc.setCART();
      return false;
    }
    return true;
  }

  protected QueryDataSet list = null;
  public void okPress() {
    String god = hr.restart.util.Util.getUtil().getYear(hr.restart.util.Util.getUtil().getFirstDayOfYear());
    list = Asql.getArtiklSklad(rpc.getCART(), false);
    openScratchDataSet(list);
    if (list.rowCount() == 0) setNoDataAndReturnImmediately();
    disableVriCols();
    setDataSet(list);
  }
  
  protected void disableVriCols(){
  }

  public void firstESC() {
    removeNav();
    rpc.EnabDisab(true);
    showDefaults();
    rpc.setCART();
  }

  public boolean runFirstESC() {
    return rpcFocusLost;
  }
  private void showDefaults() {
    rpcFocusLost = false;
    rpc.EnabDisab(true);
    this.getJPTV().clearDataSet();
  }

  private void jbInit() throws Exception {
    this.setJPan(rpc);
    initRpcart();
  }
   private void initRpcart() {
    //rpc.setGodina(vl.findYear(vl.getToday()));
    //rpc.setCskl(dm.getCjenik().getString("CSKL"));
    rpc.setTabela(Aut.getAut().getFakeArtikl());
    rpc.setMode("DOH");
    //rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setDefParam();
    rpc.InitRaPanCart();
    //rpc.setnextFocusabile(this.jraPromjena);
  }
  public String navDoubleClickActionName() {
    // TODO Auto-generated method stub
    return "";
  }
  public int[] navVisibleColumns() {
    // TODO Auto-generated method stub
    return new int[] {0,1,2,3};
  }
}
