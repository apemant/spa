/****license*****************************************************************
**   file: UplIsplAdvancedDetail.java
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
package hr.restart.blpn;

import hr.restart.gk.jpTemSK;
import hr.restart.gk.raKontoCorgGroup;
import hr.restart.util.Valid;
import hr.restart.util.raMatPodaci;
import hr.restart.zapod.raKonta;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class UplIsplAdvancedDetail extends JPanel {
  raKontoCorgGroup kcg;
  jpTemSK tsk;
  raMatPodaci detail;
  public UplIsplAdvancedDetail(raMatPodaci det) {
    try {
System.out.println("creatin' new UplIsplAdvancedDetail");
      detail = det;
      jinit();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void jinit() throws Exception {
    tsk = new jpTemSK();
    kcg = new raKontoCorgGroup(detail, tsk.jraDatdok) {
      public void afterAfterLookupKonto() {
        boolean isSK;
        try {
          isSK = raKonta.isSaldak(kcg.getJlrBROJKONTA().getText());
        } catch (Exception e) {
          isSK = false;
        }
        //tsk.justEnable(raKonta.isSaldak(viewSet.getString("BROJKONTA")));
        tsk.enable(
            /*raKonta.isSaldak(detail.getRaQueryDataSet().getString("BROJKONTA"))*/
            isSK
            , detail);
      }
    };
    //tsk.rebind(detail.getRaQueryDataSet());
    
    arrangeComponents();
  }
  JLabel kontoLabel;
  private void arrangeComponents() {
    setLayout(new XYLayout());
    add(kontoLabel = new JLabel("Konto"),        new XYConstraints(15, 0, -1, -1));
    add(kcg.getJlrBROJKONTA(),         new XYConstraints(150, 0, 100, -1));
    add(kcg.getJlrNAZIVKONTA(),        new XYConstraints(255, 0, 345, -1));
    add(kcg.getJbGetKonto(),   new XYConstraints(605, 0, 21, 21));

    add(new JLabel("Org. jedinica"),        new XYConstraints(15, 25, -1, -1));
    add(kcg.getJlrCORG(),         new XYConstraints(150, 25, 100, -1));
    add(kcg.getJlrNAZIVORG(),        new XYConstraints(255, 25, 345, -1));
    add(kcg.getJbGetCorg(),   new XYConstraints(605, 25, 21, 21));
    add(tsk, new XYConstraints(0,50,-1,-1));
  }
  
  public boolean validateData() {
    Valid vl = Valid.getValid();
    if (raKonta.isSaldak(detail.getRaQueryDataSet().getString("BROJKONTA"))) {
      if (!tsk.validate(' ')) return false;
    }
    if (vl.isEmpty(kcg.getJlrBROJKONTA())) return false;
    if (vl.isEmpty(kcg.getJlrCORG())) return false;
    return true;
  }
}
