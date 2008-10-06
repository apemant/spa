/****license*****************************************************************
**   file: raPrebacivanjePodataka.java
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
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;

import java.io.File;
import java.io.FileNotFoundException;

import com.borland.dx.sql.dataset.QueryDataSet;


public class raPrebacivanjePodataka {

  public raPrebacivanjePodataka() {
  }
  public static void main(String[] args) {
    raPrebacivanjePodataka raPrebacivanjePodataka1 = new raPrebacivanjePodataka();
    raPrebacivanjePodataka1.prebaci();

  }
  public void prebaci(){
    dM dm = dM.getDataModule();
     dm.getAllArtikli().open();
     dm.getStanje().open();
     dm.getOrgstruktura().open();
     dm.getSklad().open();
     dm.getPartneri().open();
     QueryDataSet tmpArtikli = new QueryDataSet();

    try {
      QueryDataSet qdsStanje =
          Stanje.getDataModule().loadData(new File("."));
      QueryDataSet tmpStanje =new QueryDataSet();
      for (qdsStanje.first();qdsStanje.inBounds();qdsStanje.next()){
        tmpArtikli=hr.restart.util.Util.getNewQueryDataSet("select * from artikli where cart1='"+
        qdsStanje.getString("TKAL")+"'",true);
        if (tmpArtikli.getRowCount()!=0){
          tmpStanje=hr.restart.util.Util.getNewQueryDataSet("select * from stanje where cart="+tmpArtikli.getInt("CART")+
              " and cskl='"+qdsStanje.getString("CSKL")+"'",true);
          if (tmpStanje.getRowCount()!=0) {
            tmpStanje.setBigDecimal("KOL",qdsStanje.getBigDecimal("KOL"));
            tmpStanje.setBigDecimal("VC",qdsStanje.getBigDecimal("VC"));
            tmpStanje.setBigDecimal("VRI",tmpStanje.getBigDecimal("VRI").add(
                qdsStanje.getBigDecimal("VRI")));
            tmpStanje.saveChanges();
            tmpStanje.close();
          }else {
            dm.getStanje().insertRow(false);
            dm.getStanje().setString("CSKL",qdsStanje.getString("CSKL"));
            dm.getStanje().setInt("CART",tmpArtikli.getInt("CART"));
            dm.getStanje().setString("GOD","2004");
            dm.getStanje().setBigDecimal("KOL",qdsStanje.getBigDecimal("KOL"));
            dm.getStanje().setBigDecimal("VRI",qdsStanje.getBigDecimal("VRI"));
            tmpStanje.setBigDecimal("VC",qdsStanje.getBigDecimal("VC"));
            dm.getStanje().saveChanges();
          }
//          System.out.println(qdsStanje.getString("CSKL")+"-"+tmpArtikli.getInt("CART")+"    P R O S A O");
        } else {
          System.out.println("Nisam nasao "+qdsStanje.getString("TKAL"));
        }
      }
      System.exit(0);
    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
  }


}