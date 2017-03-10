/****license*****************************************************************
**   file: raRekalkPred.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Doku;
import hr.restart.baza.Stdoku;
import hr.restart.baza.VTPred;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.dlgErrors;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.raUpitLite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raRekalkPred extends raUpitLite {
  
  dM dm = dM.getDataModule();
  
  lookupData ld = lookupData.getlookupData();
  
  JPanel jPanel1 = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  
  rapancskl1 rpcskl = new rapancskl1() {
    void jbInitRest(boolean how) throws Exception {
        super.jbInitRest(how);
        this.xYLayout1.setWidth(640);
        remove(jbCSKL);
        remove(jrfNAZSKL);
        add(jrfNAZSKL, new XYConstraints(255, 25, 348, -1));
        add(jbCSKL, new XYConstraints(609, 25, 21, 21));
    }

    public void MYpost_after_lookUp() {
    }
  };
 
  JraTextField godina = new JraTextField();
  
  dlgErrors err;
  
  QueryDataSet stpre, vtpre;
  
  String cskl = "2";

  String god = "2002";

  public raRekalkPred() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  void jbInit() throws Exception {        
    
    xYLayout1.setWidth(645);
    xYLayout1.setHeight(100);
    jPanel1.setLayout(xYLayout1);
    jPanel1.setBorder(new javax.swing.border.EtchedBorder());
    rpcskl.setOverCaption(true);
    
    godina.setHorizontalAlignment(SwingConstants.CENTER);
    jPanel1.add(rpcskl, new XYConstraints(0, 0, -1, -1)); //(0, 0, -1, -1)
    
    jPanel1.add(new JLabel("Godina"), new XYConstraints(15, 60, -1, -1)); //15, 50, -1,
    jPanel1.add(godina, new XYConstraints(150, 60, 100, -1));
    this.setJPan(jPanel1);
  }
  
  public void okPress() {
    if (err != null && !err.isDead()) err.hide();
    
    Condition conmain = Condition.equal("CSKL", cskl).and(Condition.equal("GOD", god)).and(Condition.equal("VRDOK", "PRE"));

    setMessage("Dohvat predatnica ...");
    QueryDataSet pre = Doku.getDataModule().getTempSet(conmain.and(Condition.equal("STATKNJ", "N")));
    openScratchDataSet(pre);
    if (pre.rowCount() == 0) setNoDataAndReturnImmediately();
    
    err = new dlgErrors(this.getWindow(), "Razlike izmeðu tablice stanja i rekalkulacije", false);
    JButton popr = new JButton("Popravi!");
    popr.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {            
        saveAll();
      }
    });
    err.setActionButton(popr);
    err.setData(new Column[] {dM.createBigDecimalColumn("TOTAL"), dM.createBigDecimalColumn("ISPRAVNI")});
    err.setColumnWidth(50);
    err.setSize(640, 400);
    
    HashMap tips = new HashMap();
    for (dm.getSklad().first(); dm.getSklad().inBounds(); dm.getSklad().next())
      tips.put(dm.getSklad().getString("CSKL"), dm.getSklad().getString("TIPSKL"));

    setNewMessage("Dohvat stavaka predatnica ...");
    stpre = Stdoku.getDataModule().getTempSet(conmain.and(Condition.in("BRDOK", pre)));
    openScratchDataSet(stpre);
    
    setNewMessage("Dohvat stavaka vezne tablice predatnica ...");
    vtpre = VTPred.getDataModule().getTempSet("id_stavka like '" + 
          raControlDocs.UniversalKeyToSqlKey("doki", new String[] {"CSKL", "VRDOK", "GOD"},
              new String[] {cskl, "PRE", god}) + "%'");
    openScratchDataSet(vtpre);
    
    Condition crads = Condition.in("CRADNAL", pre);

    setMessage("Dohvat stavaka izdatnica...");
    QueryDataSet stizd = stdoki.getDataModule().getTempSet("CSKL IRAZ VEZA ID_STAVKA", Condition.equal("VRDOK", "IZD").and(crads));
    openScratchDataSet(stizd);

    setMessage("Priprema kumulativa izdatnica...");
    HashMap mat = new HashMap();
    HashMap rob = new HashMap();
    HashMap pro = new HashMap();
    HashMap pol = new HashMap();
    for (stizd.first(); stizd.inBounds(); stizd.next()) {
      String rnls = stizd.getString("VEZA");
      String izds = stizd.getString("ID_STAVKA");
      if (rnls.length() == 0) {
        err.addError("Nedostajuæa veza rnl za stavku izdatnice " + izds);
        continue;
      }
      
      HashMap dest = null;
      if (tips.get(stizd.getString("CSKL")).equals("M")) dest = mat;
      else if (tips.get(stizd.getString("CSKL")).equals("R")) dest = rob;
      else if (tips.get(stizd.getString("CSKL")).equals("P")) dest = pro;
      else if (tips.get(stizd.getString("CSKL")).equals("L")) dest = pol;
      else err.addError("Pogrešno skladište za stavku izdatnice " + izds);
      
      if (dest != null) {
        BigDecimal old = (BigDecimal) dest.get(rnls);
        if (old == null) dest.put(rnls, stizd.getBigDecimal("IRAZ"));
        else dest.put(rnls, old.add(stizd.getBigDecimal("IRAZ")));
      }
    }
    
    setNewMessage("Rekalkulacija predatnica (0 od " + stpre.rowCount() + ")...");
    int obr = 0;
    String[] sumc = {"MAT_FI", "ROB_FI", "PRO_FI", "POL_FI", "USL_FI"};
    String[] clearc = {"IMAR", "IPOR", "POR1", "PMAR", "MAR", "SVC", "SMC", "SKOL"};
    for (stpre.first(); stpre.inBounds(); stpre.next()) {
      if (++obr % 17 == 0)
        setMessage("Rekalkulacija predatnica (" + obr + " od " + stpre.rowCount() + ")...");
      
      if (stpre.getString("ID_STAVKA").length() == 0) {
        err.addError("Nedostajuæa id_stavka " + stpre.getInt("BRDOK") + "/" + stpre.getInt("RBR"));
        continue;
      }
      
      if (stpre.getString("VEZA").length() == 0) {
        err.addError("Nedostajuæa veza stavke " + stpre.getString("ID_STAVKA"));
        continue;
      }
      
      if (!ld.raLocate(vtpre, "ID_STAVKA", stpre)) {
        err.addError("Nepostojeæa vezna stavka za stavku " + stpre.getString("ID_STAVKA"));
        continue;
      }
      
      String rnls = stpre.getString("VEZA");
      boolean prom = update(vtpre, "MAT_I", "MAT_F", "MAT_FI", (BigDecimal) mat.get(rnls));
      prom = update(vtpre, "ROB_I", "ROB_F", "ROB_FI", (BigDecimal) rob.get(rnls)) || prom;
      prom = update(vtpre, "PRO_I", "PRO_F", "PRO_FI", (BigDecimal) pro.get(rnls)) || prom;
      prom = update(vtpre, "POL_I", "POL_F", "POL_FI", (BigDecimal) pol.get(rnls)) || prom;
      if (prom) {
        BigDecimal oldtot = vtpre.getBigDecimal("TOTAL");
        Aus.clear(vtpre, "TOTAL");
        Aus.addTo(vtpre, "TOTAL", sumc);
        err.addError("Razlika na stavci " + rnls, new Object[] {oldtot, vtpre.getBigDecimal("TOTAL")});
        
        Aus.set(stpre, "INAB", vtpre, "TOTAL");
        Aus.div(stpre, "NC", "INAB", "KOL");
        Aus.set(stpre, "IZAD", "INAB");
        Aus.set(stpre, "ZC", "NC");
        Aus.clear(stpre, clearc);
      }
    }    
  }
  
  private void saveAll() {
    raProcess.runChild(this.getWindow(), new Runnable() {
      public void run() {
        try {         
          raTransaction.saveChangesInTransaction(new QueryDataSet[] {stpre, vtpre});
        } catch (Exception ex) {
          ex.printStackTrace();
          raProcess.fail();
        }        
      }
    });
    if (raProcess.isFailed())
      JOptionPane.showMessageDialog(this.getWindow(),
          "Snimanje neuspješno !", "Greška", JOptionPane.ERROR_MESSAGE);
    else if (raProcess.isCompleted())
      JOptionPane.showMessageDialog(this.getWindow(),
          "Snimanje uspješno završeno!", "Informacija", JOptionPane.INFORMATION_MESSAGE);
  }

  private boolean update(QueryDataSet vtp, String coli, String colf, String colfi, BigDecimal izn) {
    if (izn == null) return false;
    if (izn.compareTo(vtp.getBigDecimal(coli)) == 0) return false;
    vtp.setBigDecimal(coli, izn);
    Aus.mul(vtp, colfi, coli, colf);
    return true;
  }
  
  public void afterOKPress() {
    if (raProcess.isCompleted()) {
      if (err.countErrors() > 0) err.show();
      else JOptionPane.showMessageDialog(this.getWindow(),
          "Nema grešaka na skladištu.", "Rekalkulacija", JOptionPane.INFORMATION_MESSAGE);
    } else if (raProcess.isFailed()) {
      JOptionPane.showMessageDialog(this.getWindow(),
          "Rekalkulacija predatnica neuspješna !", "Greška", JOptionPane.ERROR_MESSAGE);        
    }
  }
  
  public void componentShow() {
    rpcskl.Clear();
    godina.setText(hr.restart.util.Valid.getValid().findYear());
    rpcskl.jrfCSKL.requestFocus();
  }
  
  public boolean Validacija() {
    
     if (rpcskl.jrfCSKL.getText().equals("")) {
            rpcskl.jrfCSKL.requestFocus();
            return false;       
     }
    if (!ValGod()) {
      godina.setText("");
      godina.requestFocus();
            return false;
    }
        
    cskl = rpcskl.jrfCSKL.getText();
    if (!ld.raLocate(dm.getSklad(), "CSKL", cskl)) {
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Nepostojeæe skladište!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;     
    }
    if (!dm.getSklad().getString("VRZAL").equals("N")) {
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Odabrano skladište se ne vodi po nabavnim cijenama!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    god = godina.getText();
            EnabDisab(false);
            
    return true;
  }

    public boolean ValGod() {
        int pero = 0;
        try {
            pero = Integer.parseInt(godina.getText());
        } catch (Exception e) {
            return false;
        }
        if (pero < 1900 || pero > 3900)
            return false;
        return true;
    }
  
    public void EnabDisab(boolean kako) {
      rpcskl.disabCSKL(kako);
      hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
              godina, kako);
    }

    public boolean runFirstESC() {
      return !rpcskl.jrfCSKL.getText().equals("");
    }
    
    public boolean isIspis() {
      return false;
    }

    public boolean ispisNow() {
      return false;
    }
    
    public void firstESC() {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            EnabDisab(true);
            //rpcskl.Clear();
            //rpcart.Clear();
            rpcskl.Clear();
            rpcskl.jrfCSKL.requestFocus();
        }
      });
    }
}
