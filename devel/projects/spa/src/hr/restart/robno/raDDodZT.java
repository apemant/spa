/****license*****************************************************************
**   file: raDDodZT.java
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

import hr.restart.util.Aus;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class raDDodZT extends raDDodRab {

  String greska = "Prvi u nizu zavisnih troškova ne smije imati oznaku ztnazt = D "+
          "Pobriši stavke zav. troškova i unesi ispravno ";
  allSelect aSS = new allSelect();

  /*
  public raDDodZT(Frame frame, raIzlazTemplate fDI, String title, boolean modal) {
      dm.getVtzavtr().open();
  }
*/
  public raDDodZT(raIzlazTemplate fDI, String title, boolean modal) {
    super(null, fDI, title, modal);
      dm.getVtzavtr().open();
  }

  public boolean isRnR() {
    return rabatTMP4Browse.getString("ZTNAZT").equals("D");
  }

  public void afterJob() {

//      fDI.jpZatr_afterJob();
  }
  public void jbInit4overload(){
    jlrCRAB1.setColumnName("CZT");
    jlrCRAB1.setRaDataSet(dm.getZavtr());
    jlrCRAB1.setColNames(new String[] {"NZT","PZT","IZT"});
    jlrPRAB1.setColumnName("PZT");
    jlrNAZRAB1.setColumnName("NZT");
    jlrIRAB1.setColumnName("IZT");
    jcrabnarab.setText("Zav. tr. na zav. tr.");
    jLabel1.setText("Zavisni trošak");
  }
  public void transferFill(){
    insertTempRow(rabatTMP.getShort("LRBR"),
                  rabatTMP.getString("CZT"),
                  rabatTMP.getString("NZT"),
                  rabatTMP.getBigDecimal("PZT"),
                  rabatTMP.getString("ZTNAZT"));
     brojac_sloga=rabatTMP.getShort("LRBR");
  }

  public void findRabat(int rbr) {
    rabatTMP.close();
    rabatTMP.closeStatement();
    rabatTMP.setQuery(new QueryDescriptor(dm.getDatabase1(),
                      aSS.getQuery4rDDZT4findRabat(fDI.getDetailSet().getString("CSKL"),
                                          fDI.getDetailSet().getString("VRDOK"),
                                          fDI.getDetailSet().getString("GOD"),
                                          fDI.getDetailSet().getInt("BRDOK"),
                                          rbr), null, true, Load.ALL));
    rabatTMP.open();
  }

  public void setupRabatTmp(){
    rabatTMP4Browse.close();
    Column lrbr_C = (Column) hr.restart.baza.dM.getDataModule().getVtzavtr().getColumn("LRBR").clone();
    lrbr_C.setCaption("Redni broj");
    rabatTMP4Browse.setColumns(new Column[]
     {lrbr_C ,
      (Column) hr.restart.baza.dM.getDataModule().getVtzavtr().getColumn("CZT").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getZavtr().getColumn("NZT").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVtzavtr().getColumn("PZT").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVtzavtr().getColumn("ZTNAZT").clone()
    });
    }
  public void insertTempRow(short lrbr, String crab,String nrab,
                             java.math.BigDecimal prab,String rabnarab )
  {
    rabatTMP4Browse.insertRow(true);
    rabatTMP4Browse.setShort("LRBR",lrbr);
    rabatTMP4Browse.setString("CZT", crab);
    rabatTMP4Browse.setString("NZT", nrab);
    rabatTMP4Browse.setBigDecimal("PZT", prab);
    rabatTMP4Browse.setString("ZTNAZT", rabnarab);
    addPopust();
  }
  public void addPopust(){
    BigDecimal tmpBig = Aus.zero2;
    if (rabatTMP4Browse.getString("ZTNAZT").equals("N"))
    {
      sp = sp.add(rabatTMP4Browse.getBigDecimal("PZT"));
    }
    else
    {
      tmpBig = sp;
      tmpBig = tmpBig.multiply(rabatTMP4Browse.getBigDecimal("PZT"));
      tmpBig = tmpBig.divide(BigDecimal.valueOf(100),2,2);
      sp = sp.add(tmpBig);
    }
    jlUkupno.setText("U K U P N O        "+sp);
  }
}