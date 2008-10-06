/****license*****************************************************************
**   file: raUnosInventure.java
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
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.Dimension;
import java.math.BigDecimal;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raUnosInventure extends raMatPodaci {
  raCommonClass rcc = new raCommonClass();
  dM dm = hr.restart.baza.dM.getDataModule();
  raPan4UnosInv rpui = new raPan4UnosInv();
  QueryDataSet inventuraZaSkladiste;

  public raUnosInventure() {
    super(2);
    initInv();
  }

  void initInv() {
    setVisibleCols(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,7});

    setRaQueryDataSet (dm.getInventura());
    setRaDetailPanel(rpui);
    InitRPCRT();
  }

  public void show() {
    super.show();
    this.disableAdd();
  }

  public boolean Validacija(char mode) {
    if (mode=='I'){
      kalkulirKalkulir();
    }
    return true;
  }

  public void EntryPoint(char mode) {
    if (mode=='I'){
      rpui.rpcrt.EnabDisab(false);
    }
  }

  public void SetFokus(char mode) {
    if (mode=='I'){
      rpui.jtfINVKOL.requestFocus();
    }
  }

  public void kalkulirKalkulir(){
//    System.out.println("uslo u kalkulaciju");
    BigDecimal kolINV = dm.getInventura().getBigDecimal("KOLINV");
    BigDecimal kolKNJ = dm.getInventura().getBigDecimal("KOLKNJ");

    BigDecimal invVRI = kolINV.multiply(dm.getInventura().getBigDecimal("ZC"));
    dm.getInventura().setBigDecimal("VRIINV", invVRI.setScale(2, BigDecimal.ROUND_HALF_UP));

    if (kolINV.compareTo(kolKNJ) > 0){
      BigDecimal kolVIS = kolINV.subtract(kolKNJ);
      BigDecimal vriVIS = kolVIS.multiply(dm.getInventura().getBigDecimal("ZC"));
      dm.getInventura().setBigDecimal("KOLVIS", kolVIS);
      dm.getInventura().setBigDecimal("VRIVIS", vriVIS.setScale(2, BigDecimal.ROUND_HALF_UP));
      dm.getInventura().setBigDecimal("KOLMANJ", _Main.nul);
      dm.getInventura().setBigDecimal("VRIMANJ", _Main.nul);
    }

    else if (kolINV.compareTo(kolKNJ) < 0){
      BigDecimal kolMANJ = kolKNJ.subtract(kolINV);
      BigDecimal vriMANJ = kolMANJ.multiply(dm.getInventura().getBigDecimal("ZC"));
      dm.getInventura().setBigDecimal("KOLMANJ", kolMANJ);
      dm.getInventura().setBigDecimal("VRIMANJ", vriMANJ.setScale(2, BigDecimal.ROUND_HALF_UP));
      dm.getInventura().setBigDecimal("KOLVIS", _Main.nul);
      dm.getInventura().setBigDecimal("VRIVIS", _Main.nul);
    }

    else if (kolINV.compareTo(kolKNJ) == 0){
      dm.getInventura().setBigDecimal("KOLMANJ", _Main.nul);
      dm.getInventura().setBigDecimal("KOLVIS", _Main.nul);
      dm.getInventura().setBigDecimal("VRIMANJ", _Main.nul);
      dm.getInventura().setBigDecimal("VRIVIS", _Main.nul);
    }
  }

  public void InitRPCRT(){
    if (!dm.getInventura().isOpen()) dm.getInventura().open();
    rpui.rpcrt.setPreferredSize(new Dimension(800, 100));
    rpui.rpcrt.setCskl(dm.getInventura().getString("CSKL"));
    rpui.rpcrt.setTabela(dm.getInventura());
    rpui.rpcrt.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
    rpui.rpcrt.InitRaPanCart();
    rpui.rpcrt.setMode("DOH");
    rpui.rpcrt.setnextFocusabile(rpui.jtfINVKOL);
    rpui.rpcrt.setBorder(null);
    rcc.setLabelLaF(rpui.jtfINVKOL, false);
  }
}