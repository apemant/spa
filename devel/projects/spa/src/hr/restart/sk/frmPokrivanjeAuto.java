/****license*****************************************************************
**   file: frmPokrivanjeAuto.java
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
import hr.restart.baza.Skstavke;
import hr.restart.util.Aus;
import hr.restart.util.Int2;
import hr.restart.util.raProcess;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmPokrivanjeAuto extends frmPokrivanje {
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  public frmPokrivanjeAuto() {
  }

  public void OKPress() {
    if (!jpc.Validacija()) return;
    raProcess.runChild(getWindow(), new Runnable() {
      public void run() {
        performAutomatch();
      }
    });
    if (total.one == 0 && raProcess.isCompleted())
      JOptionPane.showMessageDialog(getWindow(), "Nema stavaka za pokrivanje!",
                                    "Poruka", JOptionPane.INFORMATION_MESSAGE);
    else if (total.one > 0) JOptionPane.showMessageDialog(getWindow(), Aus.getNumDep(total.one,
        "Pokriven "+total.one+(total.one == 1 ? " broj" : " razlièit broj"),
        "Pokrivena "+total.one+" razlièita broja",
        "Pokriveno "+total.one+" razlièitih brojeva") +
        " na ukupno "+Aus.getNum(total.two, "stavci.", "stavke.", "stavaka."),
        "Poruka", JOptionPane.INFORMATION_MESSAGE);
  }

  private Int2 total = new Int2();

  private void processPartner(Condition cond, int cpar) {
    raProcess.setMessage("Provjera partnera "+cpar+" ...", false);
    QueryDataSet sks = Skstavke.getDataModule().getTempSet(
          cond.and(Condition.equal("CPAR", cpar)));
    raProcess.openScratchDataSet(sks);
    total = total.add(raSaldaKonti.matchBrojdok(sks));
  }

  private void performAutomatch() {
    Condition common = Aus.getKnjigCond().and(jpc.getCondition()).and(Aus.getFreeYearCond()).
                       and(Condition.equal(raSaldaKonti.colPok(), "N"));
    total.clear();
    if (!jpp.isEmpty()) processPartner(common, jpp.getCpar());
    else {
      raProcess.setMessage("Dohvat popisa partnera ...", false);
      QueryDataSet ds = ut.getNewQueryDataSet(
          "SELECT distinct cpar FROM skstavke WHERE "+common, false);
      raProcess.openScratchDataSet(ds);
      for (ds.first(); ds.inBounds(); ds.next())
        processPartner(common, ds.getInt("CPAR"));
    }
  }
}
