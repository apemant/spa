/****license*****************************************************************
**   file: dlgTotalAgent.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraDialog;
import hr.restart.swing.KeyAction;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;



/**
 * @author abf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class dlgTotalAgent {
  
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();

  JraDialog dlg;
  
  int key;
  
  boolean busy;
  
  jpTotalAgent pan = new jpTotalAgent() {
    public void begin() {
      if (busy) return;
      busy = true;
      new Thread() {
        public void run() {
          try {
            findPromet(dlg, pan.getGodina());
            pan.repaint();
          } finally {
            busy = false;
          }
        }
      }.start();      
    }
  };
  
  public void show(Container owner, int key, String title) {
    this.key = key;
    if (!findPromet(owner, Valid.getValid().getKnjigYear("robno"))) return;
    
    if (owner instanceof JComponent)
      owner = ((JComponent) owner).getTopLevelAncestor();
    if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, true);
    else if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, true);
    else dlg = new JraDialog((Frame) null, true);
        
    dlg.setTitle(title);
    dlg.setContentPane(pan);
    dlg.pack();
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    AWTKeyboard.registerKeyStroke(dlg, AWTKeyboard.F10, new KeyAction() {
      public boolean actionPerformed() {
        pan.begin();
        return true;
      }
    });
    AWTKeyboard.registerKeyStroke(dlg, AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        hide();
        return true;
      }
    });
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        hide();
      }
    });
    if (owner != null) dlg.setLocationRelativeTo(owner);
    dlg.show();
  }
  
  private String procGod;
  
  boolean findPromet(Container owner, String god) {
    int g = Aus.getNumber(god);
    if (g < 20) g = 2000 + g;
    else if (g < 100) g = 1900 + g;

    if (g < 1950 || g > 2010) {
      JOptionPane.showMessageDialog(owner, "Pogrešna godina!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    procGod = String.valueOf(g);
    if (dlg == null) pan.setGodina(procGod);
    raProcess.runChild(owner, "Promet agenta", "Operacija u tijeku ...", new Runnable() {
      public void run() {
        findPromet();
      }
    });
    return raProcess.isCompleted();
  }
  
  BigDecimal s = new BigDecimal(100);
  
  private BigDecimal getPost(BigDecimal osn, BigDecimal post) {
    return osn.multiply(post).divide(s, 2, BigDecimal.ROUND_HALF_UP);
  }
  
  private void findPromet() {    
    BigDecimal z = new BigDecimal(0);
    
    raProcess.setMessage("Provjera salda konti raèuna ...", true);
    DataSet sk = Skstavke.getDataModule().getTempSet("BROJDOK", 
        Aus.getKnjigCond().and(Condition.equal("CAGENT", key)).and(
            Condition.equal("VRDOK", "IRN")).and(
            Condition.equal("POKRIVENO", "D")));
    raProcess.openScratchDataSet(sk);
    HashSet pokriveni = new HashSet();
    for (sk.first(); sk.inBounds(); sk.next())
      pokriveni.add(sk.getString("BROJDOK"));
    
    raProcess.setMessage("Dohvat raèuna ...", false);
    DataSet skl = rut.getSkladFromCorg();
    raProcess.checkClosing();
    DataSet knj = OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
    
    Condition sklDok = Condition.in("VRDOK", new String[] {"ROT", "GOT"});
    Condition orgDok = Condition.in("VRDOK", new String[] {"RAC", "GRN"});
    Condition period = Condition.between("DATDOK",
        ut.getYearBegin(procGod), ut.getYearEnd(procGod));
    
    String query =
      "SELECT max(doki.datdok) as datdok, max(doki.uirac) as uirac, "+
      "sum(stdoki.iprodbp) as uibp, max(doki.statknj) as statknj, "+
      "max(doki.statpla) as statpla, max(doki.pnbz2) as brojdok "+
      "FROM doki,stdoki WHERE "+rut.getDoc("doki","stdoki")+" AND "+
      Condition.equal("CAGENT", key).and(period).
      and((sklDok.and(Condition.in("CSKL", skl))).
      or(orgDok.and(Condition.in("CSKL", knj, "CORG")))).qualified("doki") +
      " GROUP BY doki.cskl, doki.god, doki.vrdok, doki.brdok";
    
    DataSet ds = ut.getNewQueryDataSet(query, false);
    raProcess.openScratchDataSet(ds);    
    
    raProcess.setMessage("Priprema kumulativa ...", false);
    BigDecimal ukup = z, knjiz = z, neknjiz = z, nap = z, nenap = z;
    BigDecimal[] mjF = new BigDecimal[12];
    BigDecimal[] mjN = new BigDecimal[12];
    BigDecimal[] mjP = new BigDecimal[12];
    for (int i = 0; i < 12; i++)
      mjF[i] = mjN[i] = mjP[i] = z;
    Calendar c = Calendar.getInstance();
    
    for (ds.first(); ds.inBounds(); ds.next()) {
      raProcess.checkClosing();
      c.setTime(ds.getTimestamp("DATDOK"));
      int mj = c.get(c.MONTH);
      BigDecimal rac = ds.getBigDecimal("UIBP");
      ukup = ukup.add(rac);
      mjF[mj] = mjF[mj].add(rac);
      if (ds.getString("STATKNJ").equalsIgnoreCase("N")) 
        neknjiz = neknjiz.add(rac);
      else {
        knjiz = knjiz.add(rac);
        if (ds.getString("STATPLA").equalsIgnoreCase("D") ||
            pokriveni.contains(ds.getString("BROJDOK"))) {
          nap = nap.add(rac);
          mjN[mj] = mjN[mj].add(rac);
        } else nenap = nenap.add(rac);            
      }
    }
    raProcess.setMessage("Priprema prikaza rezultata ...", false);
    raProcess.checkClosing();
    
    if (dm.getAllAgenti().getInt("CAGENT") != key)
      lookupData.getlookupData().raLocate(dm.getAllAgenti(), "CAGENT", Integer.toString(key));
    
    BigDecimal provp = dm.getAllAgenti().getBigDecimal("POSTOPROV");
    boolean fakt = dm.getAllAgenti().getString("VRSTAPROV").equalsIgnoreCase("F");
    
    ds = pan.getData();
    ds.enableDataSetEvents(false);
    ds.setBigDecimal("RAC", ukup);
    ds.setBigDecimal("RACKNJ", knjiz);
    ds.setBigDecimal("RACNEKNJ", neknjiz);
    ds.setBigDecimal("RACNAP", nap);
    ds.setBigDecimal("RACNENAP", nenap);
    ds.setBigDecimal("POSTPROV", provp);
    ds.setBigDecimal("PROV", getPost(ukup, provp));
    ds.setBigDecimal("PROVKNJ", getPost(knjiz, provp));
    ds.setBigDecimal("PROVNEKNJ", getPost(neknjiz, provp));
    ds.setBigDecimal("PROVNAP", getPost(nap, provp));
    ds.setBigDecimal("PROVNENAP", getPost(nenap, provp));
    ds.setBigDecimal("PROVUKUP", ds.getBigDecimal(fakt ? "PROV" : "PROVNAP"));
    ds.setBigDecimal("PROVPLAC", z);
    ds.setBigDecimal("PROVOST", ds.getBigDecimal("PROVUKUP").subtract(ds.getBigDecimal("PROVPLAC")));
    for (int i = 0; i < 12; i++) {      
      pan.setMonthVals(i, fakt ? mjF[i] : mjN[i], getPost(fakt ? mjF[i] : mjN[i], provp), z);
    }
    ds.enableDataSetEvents(true);
  }
  
  public void hide() {
    if (dlg != null) {
      dlg.dispose();
      dlg = null;
    }
  }
}
