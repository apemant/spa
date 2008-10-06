/****license*****************************************************************
**   file: frmPokrivanjePoluAuto.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.raProcess;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.borland.jbcl.layout.XYConstraints;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmPokrivanjePoluAuto extends frmPokrivanje {
  JLabel jlDepth = new JLabel();
  JraTextField jraDepth = new JraTextField();
  JLabel jlFactor = new JLabel();
  JraTextField jraFactor = new JraTextField();

  public frmPokrivanjePoluAuto() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init() throws Exception {
    jlDepth.setText("Stupanj pokrivanja");
    jraDepth.setColumnName("DEPTH");
    jraDepth.setDataSet(data);
    jlFactor.setText("Prag za automatsko oznaèavanje");
    jraFactor.setColumnName("FACTOR");
    jraFactor.setDataSet(data);

    pan.add(jlDepth, new XYConstraints(15, 72, -1, -1));
    pan.add(jraDepth, new XYConstraints(150, 70, 100, -1));
    pan.add(jlFactor, new XYConstraints(280, 72, -1, -1));
    pan.add(jraFactor, new XYConstraints(480, 70, 100, -1));
    lay.setHeight(100);
    pack();
  }

  public void OKPress() {
    final int depth = data.getShort("DEPTH");
    if (!jpc.Validacija()) return;
    if (depth < 1 || depth > 20) {
      jraDepth.requestFocus();
      JOptionPane.showMessageDialog(getWindow(), "Maksimalna dubina mora biti izmedju 1 i 20!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    double thr = data.getBigDecimal("FACTOR").doubleValue();
    if (thr < 0.1 || thr > 1) {
      jraFactor.requestFocus();
      JOptionPane.showMessageDialog(getWindow(), "Prag za oznaèavanje mora biti izmedju 0.1 i 1!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    raOptimisticMatch.setMarkThreshold(thr);
    raProcess.runChild(getWindow(), new Runnable() {
      public void run() {
        Condition cp = jpp.isEmpty() ? Condition.none : Condition.equal("CPAR", jpp.getCpar());
        raOptimisticMatch.find(depth, jpc.getCondition().and(cp));
      }
    });
    if (raProcess.isCompleted() && raOptimisticMatch.isAnythingFound())
      raOptimisticMatch.showResultDialog(getWindow(), "Prikaz dokumenata za pokrivanje po saldu");
    else if (!raProcess.isInterrupted()) JOptionPane.showMessageDialog(
        getWindow(), "Nema dokumenata za pokrivanje po saldu!",
        "Poruka", JOptionPane.INFORMATION_MESSAGE);
  }
}
