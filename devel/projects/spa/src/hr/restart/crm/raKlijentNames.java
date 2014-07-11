/****license*****************************************************************
**   file: raKlijentNames.java
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
package hr.restart.crm;

import hr.restart.baza.Klijenti;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;


public class raKlijentNames {
  
  private static raKlijentNames inst = new raKlijentNames();

  private Set clients = new HashSet();
  private int serial = -1;
  private double thresh = 0.8;
  private StorageDataSet result;
  
  private raKlijentNames() {
    checkThreshold();
    checkChanges();
    result = new StorageDataSet();
    result.setColumns(new Column[] {
       Klijenti.getDataModule().getColumn("CKLIJENT").cloneColumn(),
       Klijenti.getDataModule().getColumn("NAZIV").cloneColumn(),
       Klijenti.getDataModule().getColumn("MJ").cloneColumn(),
       Klijenti.getDataModule().getColumn("OIB").cloneColumn(),
       dM.createStringColumn("RAZLOG", "Razlog", 50)
    });
    result.open();
  }
  
  public static raKlijentNames getInstance() {
    return inst;
  }
  
  private boolean checkThreshold() {
    thresh = Aus.getDecNumber(frmParam.getParam("crm", "heurGranica", "0.8", 
        "Granica provjere sliènosti postojeæih klijenata")).doubleValue();
    if (thresh > 0.99) thresh = 0.99;
    if (thresh < 0.5 && thresh > 0) thresh = 0.5;
    return thresh > 0;
  }
  
  public void checkChanges() {
    if (!checkThreshold()) return;
    
    int newSerial = dM.getDataModule().getSynchronizer().getSerialNumber("klijenti");
    if (serial == newSerial) return;
    serial = newSerial;
    clients.clear();
    DataSet ds = Klijenti.getDataModule().getQueryDataSet();
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      clients.add(new ClientData(ds));
  }
  
  public void addRow(DataSet ds) {
    if (!checkThreshold()) return;
    
    serial = dM.getDataModule().getSynchronizer().getSerialNumber("klijenti");
    clients.add(new ClientData(ds));
  }
  
  public void removeRow(int cklijent) {
    if (!checkThreshold()) return;
    
    serial = dM.getDataModule().getSynchronizer().getSerialNumber("klijenti");
    clients.remove(new ClientData(cklijent));
  }
  
  
  public DataSet findSimilar(DataSet ds) {
    if (!checkThreshold()) return null;
    
    double sim;
    result.empty();
    for (Iterator i = clients.iterator(); i.hasNext(); ) {
      ClientData cd = (ClientData) i.next();
      if (cd.cklijent == ds.getInt("CKLIJENT")) continue;
      if (ds.getString("OIB").trim().length() > 0 && cd.oib.length() > 0 && 
          cd.oib.equals(ds.getString("OIB").trim())) cd.addTo(result, "Identièan OIB");
      else if (ds.getString("OIB").trim().length() == 0 || cd.oib.length() == 0)
        if ((sim = Aus.heuristicCompare(ds.getString("NAZIV"), cd.naziv)) >= thresh)
          cd.addTo(result, "Slièan naziv (faktor " + Aus.formatFloat((float) sim) + ")");
    }
    return result;
  }
  
  
  public static class ClientData {
    int cklijent;
    String naziv;
    String mj;
    String oib;
    
    public ClientData(int cklijent) {
      this.cklijent = cklijent;
    }
    
    public ClientData(DataSet ds) {
      cklijent = ds.getInt("CKLIJENT");
      naziv = ds.getString("NAZIV");
      mj = ds.getString("MJ");
      oib = ds.getString("OIB").trim();
    }
    
    public void addTo(DataSet ds, String razlog) {
      ds.insertRow(false);
      ds.setInt("CKLIJENT", cklijent);
      ds.setString("NAZIV", naziv);
      ds.setString("MJ", mj);
      ds.setString("OIB", oib);
      ds.setString("RAZLOG", razlog);
    }
    
    public int hashCode() {
      return cklijent;
    }
    
    public boolean equals(Object obj) {
      if (obj instanceof ClientData) 
        return ((ClientData) obj).cklijent == this.cklijent;
      return false;
    }
  }
}
