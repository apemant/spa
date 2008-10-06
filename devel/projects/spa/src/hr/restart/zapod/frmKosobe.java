/****license*****************************************************************
**   file: frmKosobe.java
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
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raProcess;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmKosobe extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  frmPartneri fpar;
  jpKosobe jpDetail;
  int currCpar;


  public frmKosobe(frmPartneri f, QueryDataSet tempParamQDS, int cpar) {
    super(2);
    try {
      fpar = f;
      currCpar = cpar;
      setRaQueryDataSet(tempParamQDS);
      fpar.setEnabled(false);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private int maxCKO;
  public void SetFokus(char mode) {
    if (mode != 'B') {
      jpDetail.jraIme.requestFocus();
    }
  }
  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraIme)) {
      return false;
    }
    if (mode == 'N') {
      getRaQueryDataSet().setInt("CPAR",currCpar);
      getRaQueryDataSet().setInt("CKO",++maxCKO);
    }
    return true;
  }

  public static boolean addKontakt(int cpar, String ime, String napomena) {
    if (ime == null || ime.trim().equals("")) return true;
    QueryDataSet contacts = hr.restart.baza.Kosobe.getDataModule().getTempSet("CPAR = "+cpar);
    contacts.open();
    if (lookupData.getlookupData().raLocate(contacts,"IME",ime)) return true;
    int res = JOptionPane.showConfirmDialog(
        null,"Dodati kontakt osobu "+ime+" za partnera "+cpar+"?","Kontakt osobe",JOptionPane.YES_NO_CANCEL_OPTION);
    //0-Da, 1-Ne, 2-Ponisti
    if (res == 0) {
      contacts = addKontaktGo(cpar,0,ime,"",napomena, contacts);
      contacts.saveChanges();
    }
    return res!=2;
  }
  static QueryDataSet addKontaktGo(int cpar, int cko, String ime, String tel, String napomena, QueryDataSet contacts) {
    if (contacts == null) return null;
    contacts.open();
    if (cko == 0) {
      if (contacts.getRowCount() == 0) {
        cko = 1;
      } else {
        contacts.setSort(new SortDescriptor(new String[] {"CPAR","CKO"}));
        contacts.last();
        cko = contacts.getInt("CKO") + 1;
      }
    }
    contacts.insertRow(false);
    contacts.setInt("CPAR",cpar);
    contacts.setInt("CKO",cko);
    contacts.setString("IME",ime);
    contacts.setString("TEL",tel);
    contacts.setString("NAPOMENA",napomena);
    contacts.post();
    return contacts;
  }
/*  public static void autoAddKontakt() {
    hr.restart.util.raProcess.runChild(new Runnable() {
      public void run() {
        try {
          autoAddKontaktGo();
        }
        catch (Exception ex) {
          ex.printStackTrace();
          hr.restart.util.raProcess.fail();
        }
      }
    });
  }*/
  public static void autoAddKontaktGo() {
    QueryDataSet partneri = hr.restart.baza.dM.getDataModule().getPartneri();
    raProcess.openDataSet(partneri);
    partneri.first();
    do {
      String ime = partneri.getString("KO").trim();
      if (!ime.equals("")) {
        int cpar = partneri.getInt("CPAR");
        QueryDataSet contacts = hr.restart.baza.Kosobe.getDataModule().getTempSet("CPAR = "+cpar);
        raProcess.openDataSet(contacts);
        if (!lookupData.getlookupData().raLocate(contacts,"IME",ime)) {
          contacts = addKontaktGo(cpar,0,ime,"","-",contacts);
          contacts.saveChanges();
        }
      }
    } while (partneri.next());
  }
  private void jbInit() throws Exception {
//    this.setRaQueryDataSet(dm.getKosobe());
    getRaQueryDataSet().setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {"CPAR","CKO"}));
    getRaQueryDataSet().open();
    getRaQueryDataSet().last();
    maxCKO = getRaQueryDataSet().getInt("CKO");
    this.setVisibleCols(new int[] {2, 3});
    jpDetail = new jpKosobe(this);
    this.setRaDetailPanel(jpDetail);
  }
  public void ZatvoriOstalo() {
    fpar.setEnabled(true);
    fpar.toFront();
  }

}
