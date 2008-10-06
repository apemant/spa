/****license*****************************************************************
**   file: frmKnjBlag.java
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
package hr.restart.gk;
import hr.restart.blpn.frmUplIspl;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.raTransaction;
import hr.restart.zapod.raKonta;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
public class frmKnjBlag extends frmKnjizenje {
  String domVal = hr.restart.zapod.Tecajevi.getDomOZNVAL();
  QueryDataSet izvjestaji;
  public frmKnjBlag() {
  }
  public boolean okPress() {
    if (!getKnjizenje().startKnjizenje(this)) return false;
    izvjestaji = Util.getNewQueryDataSet("SELECT * FROM Blagizv WHERE status = 'Z' and knjig = '"+hr.restart.zapod.OrgStr.getKNJCORG(false)+
        "' and datdo < '"+new java.sql.Date(Util.getUtil().addDays(dataSet.getTimestamp("DATUMDO"),1).getTime()).toString()+"'");
    if (izvjestaji.getRowCount() == 0) {
      getKnjizenje().setErrorMessage("Nema podataka za knjiženje!");
      return false;
    }
    izvjestaji.first();
    do {
      BigDecimal ukid = Aus.zero2;
      BigDecimal ukip = Aus.zero2;
      boolean dev = !izvjestaji.getString("OZNVAL").equals(domVal);
      QueryDataSet stavbl = getStavBl(izvjestaji);
      String opisIzvj = " blag. izvj. "
                      +izvjestaji.getInt("CBLAG")
                      +"-"+izvjestaji.getString("OZNVAL")
                      +"-"+izvjestaji.getShort("GODINA")
                      +"-"+izvjestaji.getInt("BRIZV");
      String empty50 = "                                                 ";
      if (stavbl.getRowCount() > 0) {
        stavbl.first();
        do {
          StorageDataSet stavka;
          if (stavbl.getString("BROJKONTA").equals(frmUplIspl.getFakeURABrojkonta())) {
System.out.println("Usao u nenormalno");            
            //nenormalno
            QueryDataSet uis = frmUplIspl.getSkUIStavke(frmUplIspl.getBLUID(stavbl));
            for (uis.first(); uis.inBounds(); uis.next()) {
              stavka = getKnjizenje().getNewStavka(uis.getString("BROJKONTA"),uis.getString("CORG"));
              getKnjizenje().setID(uis.getBigDecimal("ID"));
              getKnjizenje().setIP(uis.getBigDecimal("IP"));
              ukid = ukid.add(uis.getBigDecimal("IP"));
              ukip = ukip.add(uis.getBigDecimal("ID"));
              if (getKnjizenje().isLastKontoZbirni()) {
                stavka.setString("OPIS","Stavke -".concat(opisIzvj).concat(empty50).substring(0,50));
                stavka.setTimestamp("DATDOK",izvjestaji.getTimestamp("DATDO"));
              } else {
                stavka.setString("OPIS",stavbl.getString("OPIS").concat(opisIzvj).concat(empty50).substring(0,50));
                stavka.setTimestamp("DATDOK",stavbl.getTimestamp("DATDOK"));
              }
              if (!getKnjizenje().saveStavka()) return false;
            }
          } else {
            //normalno knjizenje
            if (stavbl.getString("BROJKONTA").equals("")) {
              stavka = getKnjizenje().getNewStavka(stavbl);
            } else {
              stavka = getKnjizenje().getNewStavka(stavbl.getString("BROJKONTA"),stavbl.getString("CORG"));
            }
  //System.out.println("stavka blagajne je \n"+stavbl);
  //System.out.println("stavka za knjizenje je \n"+stavka);
  //System.out.println("Konto nove stavke je "+stavka.getString("BROJKONTA"));
            if (dev) {
              getKnjizenje().setID(stavbl.getBigDecimal("PVIZDATAK"));
              getKnjizenje().setIP(stavbl.getBigDecimal("PVPRIMITAK"));
              ukid = ukid.add(stavbl.getBigDecimal("PVPRIMITAK"));
              ukip = ukip.add(stavbl.getBigDecimal("PVIZDATAK"));
            } else {
              getKnjizenje().setID(stavbl.getBigDecimal("IZDATAK"));
              getKnjizenje().setIP(stavbl.getBigDecimal("PRIMITAK"));
              ukid = ukid.add(stavbl.getBigDecimal("PRIMITAK"));
              ukip = ukip.add(stavbl.getBigDecimal("IZDATAK"));
            }
            if (getKnjizenje().isLastKontoZbirni()) {
              stavka.setString("OPIS","Stavke -".concat(opisIzvj).concat(empty50).substring(0,50));
              stavka.setTimestamp("DATDOK",izvjestaji.getTimestamp("DATDO"));
            } else {
              stavka.setString("OPIS",stavbl.getString("OPIS").concat(opisIzvj).concat(empty50).substring(0,50));
              stavka.setTimestamp("DATDOK",stavbl.getTimestamp("DATDOK"));
            }
            boolean isSK;
            try {
              isSK = raKonta.isSaldak(stavbl.getString("BROJKONTA"));
            } catch (Exception e) {
              System.out.println("raKonta.isSaldak je puko ko kokica "+e);
              isSK = false;
            }
            if (isSK) {
              stavka.setTimestamp("DATDOSP", stavbl.getTimestamp("DATDOSP"));
              stavka.setString("BROJDOK", stavbl.getString("BROJDOK"));
              stavka.setInt("CPAR", stavbl.getInt("CPAR"));
              stavka.setString("BROJKONTA",stavbl.getString("BROJKONTA"));
              stavka.setString("VRDOK",frmNalozi.determineVrdok(stavka, false));
            } //else System.out.println(stavbl.getString("BROJKONTA")+" NIPOŠTO NIJE sk stavka");
            if (!getKnjizenje().saveStavka()) return false;
          } //if SKstavke
        } while (stavbl.next());
        //promet
        /*
        if (!ld.raLocate(dm.getBlagajna(),new String[] {"KNJIG","CBLAG","OZNVAL"},new String[] {
          izvjestaji.getString("KNJIG"),
          Integer.toString(izvjestaji.getInt("CBLAG")),
          izvjestaji.getString("OZNVAL")})) {
              getKnjizenje().setErrorMessage("Blagajni\u010Dki izvještaj s neispravnom oznakom blagajne!!");
              return false;
        }
        */
        String cstav = dev?"2":"1";
        StorageDataSet promet = getKnjizenje().getNewStavka(getKnjizenje().getBrojKonta("BL","1",cstav),izvjestaji.getString("KNJIG"));
        getKnjizenje().setID(ukid);
        getKnjizenje().setIP(ukip);
        promet.setString("OPIS","Promet ".concat(opisIzvj).concat(empty50).substring(0,50));
        promet.setTimestamp("DATDOK",izvjestaji.getTimestamp("DATDO"));
        if (!getKnjizenje().saveStavka()) return false;
        //
      }
    } while (izvjestaji.next());
    boolean succ;
    try {
      getKnjizenje().fixVrdok = false;
      succ = getKnjizenje().saveAll();
    } finally {
      getKnjizenje().fixVrdok = true;
    }
    return succ; 
  }

  private QueryDataSet getStavBl(DataSet izvj) {
    return Util.getNewQueryDataSet("SELECT * FROM stavkeblarh "+
                                   "WHERE stavkeblarh.knjig = '"+izvj.getString("KNJIG") +
                                   "' AND stavkeblarh.cblag = "+izvj.getInt("CBLAG") +
                                   " AND stavkeblarh.oznval = '"+izvj.getString("OZNVAL") +
                                   "' AND stavkeblarh.godina = "+izvj.getShort("GODINA") +
                                   " AND stavkeblarh.brizv = "+izvj.getInt("BRIZV")
                                   );
  }
  
  public boolean commitTransfer() {
    if (!getKnjizenje().commitTransferSK()) return false;
    try {
      for (izvjestaji.first(); izvjestaji.inBounds(); izvjestaji.next()) {
        izvjestaji.setString("STATUS","K");
      }
      // u transakciji sa obradom naloga
      //izvjestaji.saveChanges();
      raTransaction.saveChanges(izvjestaji);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}