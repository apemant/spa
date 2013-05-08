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
import hr.restart.baza.Blagajna;
import hr.restart.baza.Blagizv;
import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.blpn.frmBlag;
import hr.restart.blpn.frmUplIspl;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;
import hr.restart.zapod.raKonta;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
public class frmKnjBlag extends frmKnjizenje {
  String domVal = hr.restart.zapod.Tecajevi.getDomOZNVAL();
  QueryDataSet izvjestaji;
  
  //*************
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCblag = new JLabel();
  JLabel jlaCblag = new JLabel();
  JLabel jlaNaziv = new JLabel();
  JLabel jlaOznval = new JLabel();
  JraButton jbSelCblag = new JraButton();
  JlrNavField jlrCblag = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOznval = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  StorageDataSet viewSet;
  private StorageDataSet getSelDataSet() {
    if (viewSet == null) {
      viewSet = new StorageDataSet();
      viewSet.addColumn(Blagajna.getDataModule().getColumn("CBLAG").cloneColumn());
      viewSet.open();
    }
    return viewSet;
  }
  private void initblp() {
    jpDetail.setLayout(lay);
//    lay.setWidth(591);
    lay.setHeight(50);

    jlCblag.setText("Blagajna");
    jlaCblag.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCblag.setText("Šifra");
    jlaNaziv.setHorizontalAlignment(SwingConstants.CENTER);
    jlaNaziv.setText("Naziv");
    jlaOznval.setHorizontalAlignment(SwingConstants.CENTER);
    jlaOznval.setText("Valuta");

    jlrCblag.setColumnName("CBLAG");
    jlrCblag.setDataSet(getSelDataSet());
    jlrCblag.setColNames(new String[] {"OZNVAL", "NAZIV"});
    jlrCblag.setTextFields(new JTextComponent[] {jlrOznval, jlrNaziv});
    jlrCblag.setVisCols(new int[] {1, 2, 3}); /**@todo: Dodati visible cols za lookup frame */
    jlrCblag.setSearchMode(0);
    jlrCblag.setRaDataSet(frmBlag.getBlagajneKnjig());
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jlrCblag.setRaDataSet(frmBlag.getBlagajneKnjig());
        jlrOznval.setRaDataSet(frmBlag.getBlagajneKnjig());
        jlrNaziv.setRaDataSet(frmBlag.getBlagajneKnjig());
      }
    });

    jlrCblag.setNavButton(jbSelCblag);

    jlrOznval.setColumnName("OZNVAL");
    jlrOznval.setNavProperties(jlrCblag);
    jlrOznval.setSearchMode(1);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCblag);
    jlrNaziv.setSearchMode(1);
    
    jpDetail.add(jbSelCblag, new XYConstraints(525, 20, 21, 21));
    jpDetail.add(jlCblag, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlaCblag, new XYConstraints(151, 3, 48, -1));
    jpDetail.add(jlaNaziv, new XYConstraints(256, 3, 265, -1));
    jpDetail.add(jlaOznval, new XYConstraints(206, 3, 43, -1));
    jpDetail.add(jlrCblag,  new XYConstraints(150, 20, 50, -1));
    jpDetail.add(jlrNaziv,   new XYConstraints(255, 20, 265, -1));
    jpDetail.add(jlrOznval,   new XYConstraints(205, 20, 45, -1));
    jp.add(jpDetail,BorderLayout.CENTER);
  }
  //*************
  
  public frmKnjBlag() {
    initblp();
  }
  public boolean okPress() {
    if (!getKnjizenje().startKnjizenje(this)) return false;
    Condition cond = Condition.whereAllEqual(new String[]{"STATUS","KNJIG"}, new String[] {"Z",OrgStr.getKNJCORG(false)})
        .and(Condition.till("DATDO", dataSet.getTimestamp("DATUMDO")));
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getBlagajna(), "CBLAG", viewSet.getInt("CBLAG")+"")) {
      System.out.println("BLAGAJNA " + viewSet.getInt("CBLAG") + " found!");
      cond = cond.and(Condition.equal("CBLAG", viewSet.getInt("CBLAG")));
    }
    System.out.println(cond);
    izvjestaji = Blagizv.getDataModule().getFilteredDataSet(cond);
//      Util.getNewQueryDataSet("SELECT * FROM Blagizv WHERE status = 'Z' and knjig = '"+hr.restart.zapod.OrgStr.getKNJCORG(false)+
//        "' and datdo < '"+new java.sql.Date(Util.getUtil().addDays(dataSet.getTimestamp("DATUMDO"),1).getTime()).toString()+"'");
    izvjestaji.open();
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
                stavka.setInt("CPAR", uis.getInt("CPAR"));
                stavka.setString("OPIS",stavbl.getString("OPIS").concat(opisIzvj).concat(empty50).substring(0,50));
                stavka.setTimestamp("DATDOK",stavbl.getTimestamp("DATDOK"));
              }
              if (isSk(uis)) {
                stavka.setTimestamp("DATDOSP", stavbl.getTimestamp("DATUM"));
                stavka.setString("BROJDOK", uis.getString("BROJDOK"));
                stavka.setString("VRDOK","NK!"/*frmNalozi.determineVrdok(uis, false)*/);
                stavka.setInt("CPAR", uis.getInt("CPAR"));
              } 
              stavka.setInt("BROJIZV",izvjestaji.getShort("GODINA")*1000+izvjestaji.getInt("BRIZV"));
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
              stavka.setInt("CPAR", stavbl.getInt("CPAR"));
            }
            boolean isSK = isSk(stavbl);
            if (isSK) {
              stavka.setTimestamp("DATDOSP", stavbl.getTimestamp("DATDOSP"));
              stavka.setString("BROJDOK", stavbl.getString("BROJDOK"));
              stavka.setString("BROJKONTA",stavbl.getString("BROJKONTA"));
              stavka.setString("VRDOK",frmNalozi.determineVrdok(stavka, false));
              stavka.setInt("CPAR", stavbl.getInt("CPAR"));
            } //else System.out.println(stavbl.getString("BROJKONTA")+" NIPOŠTO NIJE sk stavka");
            stavka.setInt("BROJIZV",izvjestaji.getShort("GODINA")*1000+izvjestaji.getInt("BRIZV"));
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
        String cbl = izvjestaji.getInt("CBLAG")+"";
        String dritokonto = frmParam.getParam("blpn", "kontoPromBL"+cbl,"","Konto za promet blagajne "+cbl).trim();
        if (dritokonto.equals("")) {
          String cstav = dev?"2":"1";
          String cskl = frmParam.getParam("pl", "csklPromBL","1","Koja vrsta sheme sadrži konta za promet blagajne");
          dritokonto = getKnjizenje().getBrojKonta("BL",cskl,cstav);
        } 
        StorageDataSet promet = getKnjizenje().getNewStavka(dritokonto,izvjestaji.getString("KNJIG"));
        getKnjizenje().setID(ukid);
        getKnjizenje().setIP(ukip);
        promet.setString("OPIS","Promet ".concat(opisIzvj).concat(empty50).substring(0,50));
        promet.setTimestamp("DATDOK",izvjestaji.getTimestamp("DATDO"));
        promet.setInt("BROJIZV",izvjestaji.getShort("GODINA")*1000+izvjestaji.getInt("BRIZV"));
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
  private boolean isSk(QueryDataSet stavbl) {
    boolean ret;
    try {
      ret = raKonta.isSaldak(stavbl.getString("BROJKONTA"));
    } catch (Exception e) {
      System.out.println("raKonta.isSaldak je puko ko kokica "+e);
      ret = false;
    }
    return ret;
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