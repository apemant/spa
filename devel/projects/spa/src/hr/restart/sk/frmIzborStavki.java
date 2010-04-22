/****license*****************************************************************
**   file: frmIzborStavki.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.KnjigeUI;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.dM;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raFrame;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.raTwoTableChooser;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmIzborStavki extends raFrame {
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();

  JPanel jpDetail = new JPanel();
  raTwoTableChooser jpttc = new raTwoTableChooser();

  StorageDataSet left = new StorageDataSet();
  StorageDataSet right = new StorageDataSet();

  presSalKon pres;


  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };


  public frmIzborStavki() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void show() {
    initLeft();
    if (left.isEmpty()) {
      JOptionPane.showMessageDialog(null, "Nema nijedne radne stavke za potvrdu!", "",
        JOptionPane.INFORMATION_MESSAGE);
    } else {
      jpttc.initialize();
      super.show();
    }
  }

  private void initLeft() {
    left.open();
    right.open();
    left.empty();
    right.empty();
    String[] copycols = new String[] {"CPAR", "VRDOK", "BROJDOK", "KNJIG", "CSKL", "DATDOK", "CKNJIGE"};
/*    vl.execSQL("SELECT * FROM skstavkerad WHERE rbs = 1");
    vl.RezSet.open();
    vl.RezSet.first();
    while (vl.RezSet.inBounds()) {
      left.insertRow(false);
      DataSet.copyTo(copycols, vl.RezSet, copycols, left);
      vl.RezSet.next();
    } */
    DataSet ds = KnjigeUI.getDataModule().getTempSet("CKNJIGE",
        Condition.equal("VIRTUA", "D").and(Condition.equal("URAIRA", all)));
    ds.open();
    Set virt = new HashSet();
    for (ds.first(); ds.inBounds(); ds.next()) 
      virt.add(ds.getString("CKNJIGE"));
    
    for (all.first(); all.inBounds(); all.next())
      if (!virt.contains(all.getString("CKNJIGE"))) {
        left.insertRow(false);
        dM.copyColumns(all, left, copycols);
      }
    left.post();
  }

  private int countknj, countpok, retval;
  private String[] key = new String[] {"KNJIG", "CPAR", "VRDOK", "BROJDOK"};
  private QueryDataSet all, stav;

  private boolean knjiziJednu() {
    raLocalTransaction knj = new raLocalTransaction() {
      public boolean transaction() throws Exception {
//        System.out.println(all);
        //raSaldaKonti.setKumInvalid();
        Skstavkerad.getDataModule().setFilter(stav, Condition.whereAllEqual(key, all)).open();
        retval = raSaldaKonti.knjiziStavku(stav);
        if (retval != raSaldaKonti.OK) return false;
        raTransaction.saveChanges(dm.getSkstavke());
        raTransaction.saveChanges(dm.getUIstavke());
        //raTransaction.saveChanges(dm.getSkkumulativi());
        raTransaction.runSQL("DELETE FROM skstavkerad WHERE "+Condition.whereAllEqual(key, all));
        return true;
      }
    };
    return knj.execTransaction();
  }

  private void Proknjizi() {
    raProcess.installErrorTrace(all, new String[] {"BROJDOK", "CPAR", "DATDOK"},
              new int[] {36, 20, 8, 16}, "Greške prilikom potvr\u0111ivanja radnih dokumenata");
//    all = Skstavkerad.getDataModule().getTempSet(pres.getLastFilterQuery());
//    all.open();
    stav = Skstavkerad.getDataModule().getTempSet("1=0");
    DataRow loc = new DataRow(all, key);
    countknj = countpok = 0;
    int total = right.rowCount(), now = 0;
    boolean found;

    for (right.first(); right.inBounds(); right.next()) {
      DataSet.copyTo(key, right, key, loc);
      raProcess.setMessage("Dokument " + (++now) + " od " + total + " ...", false);
      if ((found = all.locate(loc, Locate.FIRST)) &&
          all.getString("POKRIVENO").equalsIgnoreCase("D")) {
        if (!isBlagajna() && all.getString("ZIRO").startsWith("BL#:")) {
          raProcess.addError("Raèun je unesen kroz blagajnu i potvrðuje se zajedno sa blagajnièkim izvještajem", all);
        } else if (knjiziJednu()) {
          ++countknj;
          if (raSaldaKonti.matchLast())
            ++countpok;
        } else {
          if (retval == raSaldaKonti.NO_KONTO || retval == raSaldaKonti.NO_SHEMA)
            raProcess.addError("Pogrešna shema knjiženja ili kontni plan", all);
          else if (retval == raSaldaKonti.NO_VALUTA)
            raProcess.addError("Nepostojeæa valuta", all);
          else if (retval == raSaldaKonti.DUPLICATE_KEY)
            raProcess.addError("Raèun/OK istog broja i partnera veæ potvrðen", all);
          else if (retval == raSaldaKonti.UNBALANCED)
            raProcess.addError("Dokument nije u balansu", all);
          else raProcess.addError("Greška prilikom potvrðivanja", all);
        }
      } else if (!found) {
        raProcess.addError("Greška pri prijenosu - nazvati REST-ART", all);
        System.out.println("(SK) Greška pri prijenosu:");
        System.out.println(all);
      } else raProcess.addError("Dokument nije u balansu", all);
    }
  }

  public boolean isBlagajna() {
    return false;
  }

  private void OKPress() {
    if (right.rowCount() > 0) {
      this.hide();
      raProcess.runChild(null, "Arhiviranje u tijeku", "Priprema arhiviranja ...",
                         right.rowCount()+3, new Runnable() {
        public void run() {
          Proknjizi();
        }
      });
      String dod = (countpok == 0 ? "" : (" (automatski pokriveno " + countpok + ")"));
      raProcess.report("Potvr\u0111eno " + countknj + " radnih dokumenata" +
                       dod + " od "+right.rowCount()+" odabranih.");
    } else this.hide();
  }

  private void cancelPress() {
    this.hide();
  }

  private void jbInit() throws Exception {
    left.setColumns(new Column[] {
      (Column) dm.getSkstavkerad().getColumn("cpar").clone(),
      (Column) dm.getSkstavkerad().getColumn("vrdok").clone(),
      (Column) dm.getSkstavkerad().getColumn("brojdok").clone(),
      (Column) dm.getSkstavkerad().getColumn("knjig").clone(),
      (Column) dm.getSkstavkerad().getColumn("cskl").clone(),
      (Column) dm.getSkstavkerad().getColumn("datdok").clone(),
      (Column) dm.getSkstavkerad().getColumn("cknjige").clone()
    });
    right.setColumns(new Column[] {
      (Column) dm.getSkstavkerad().getColumn("cpar").clone(),
      (Column) dm.getSkstavkerad().getColumn("vrdok").clone(),
      (Column) dm.getSkstavkerad().getColumn("brojdok").clone(),
      (Column) dm.getSkstavkerad().getColumn("knjig").clone(),
      (Column) dm.getSkstavkerad().getColumn("cskl").clone(),
      (Column) dm.getSkstavkerad().getColumn("datdok").clone(),
      (Column) dm.getSkstavkerad().getColumn("cknjige").clone()
    });
    left.getColumn("knjig").setVisible(0);
    left.getColumn("cskl").setVisible(0);
    left.getColumn("brojdok").setWidth(16);
    left.getColumn("vrdok").setWidth(4);
    left.getColumn("cknjige").setWidth(4);
    right.getColumn("knjig").setVisible(0);
    right.getColumn("cskl").setVisible(0);
    right.getColumn("brojdok").setWidth(16);
    right.getColumn("vrdok").setWidth(4);
    right.getColumn("cknjige").setWidth(4);

    left.setTableName("sk-arhiva");
    right.setTableName("sk-arhiva");
    jpttc.rnvSave.setVisible(false);
    jpttc.setLeftDataSet(left);
    jpttc.setRightDataSet(right);

    jpDetail.setLayout(new BorderLayout());
    jpDetail.add(jpttc);
    jpDetail.setBorder(BorderFactory.createEtchedBorder());

    all = Skstavkerad.getDataModule().getTempSet("1=0");
    all.open();
    pres = new presSalKon(all, "URAIRA", "U", "I");
    pres.setSelDataSet(all);
    pres.setSelPanel(pres.jpDetail);

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jpDetail, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    this.pack();
    this.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        cancelPress();
      }
    });
    okp.registerOKPanelKeys(this);
  }
  /**
   * :)
   * @param qds
   */
  public static void proknjizi(QueryDataSet qds) {
    proknjizi(qds, false);
  }
  public static void proknjizi(QueryDataSet qds, final boolean blagajna) {
    qds.open();
    frmIzborStavki fiz = new frmIzborStavki() {
      public boolean isBlagajna() {
        return blagajna;
      }
    };
    fiz.all = qds;
    fiz.initLeft();
    fiz.jpttc.initialize();
    fiz.jpttc.rnvLtoR_all.actionPerformed(null);
    fiz.OKPress();
  }

}