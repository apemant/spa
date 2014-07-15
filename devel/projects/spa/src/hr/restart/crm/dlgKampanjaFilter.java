/****license*****************************************************************
**   file: dlgKampanjaFilter.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Klijenti;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.raInputDialog;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;

import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class dlgKampanjaFilter extends raInputDialog {
    
    raComboBox jcbUser = new raComboBox();
    raComboBox jcbStatus = new raComboBox();
    raComboBox jcbSeg = new raComboBox();
    JlrNavField jlrGrad = new JlrNavField();
    JraButton jbGrad = new JraButton();
    JLabel jlStatus = new JLabel();
    JLabel jlSeg = new JLabel();
    JLabel jlUser = new JLabel();
    JLabel jlCity = new JLabel();
    
    JPanel main = new JPanel();
    
    String currUser, currStatus, currSeg, currGrad;

    public dlgKampanjaFilter() {
        main.setLayout(new XYLayout(370, 150));
              
        jlrGrad.setNavColumnName("NAZMJESTA");
        jlrGrad.setVisCols(new int[] {0, 1, 2});
        jlrGrad.setRaDataSet(dM.getDataModule().getMjesta());
        jlrGrad.setNavButton(jbGrad);
        jlrGrad.setFocusLostOnShow(false);
        jlrGrad.setAfterLookUpOnClear(false);
        jlrGrad.setSearchMode(1);
        
        reset();
        
        main.add(new JLabel("Status"), new XYConstraints(20, 20, -1, -1));
        main.add(jcbStatus, new XYConstraints(150, 20, 200, 21));        
        main.add(new JLabel("Kontaktirao"), new XYConstraints(20, 50, -1, -1));
        main.add(jcbUser, new XYConstraints(150, 50, 200, 21));
        main.add(new JLabel("Segmentacija"), new XYConstraints(20, 80, -1, -1));
        main.add(jcbSeg, new XYConstraints(150, 80, 200, 21));
        main.add(new JLabel("Grad"), new XYConstraints(20, 110, -1, -1));
        main.add(jlrGrad, new XYConstraints(150, 110, 170, -1));
        main.add(jbGrad, new XYConstraints(325, 110, 21, 21));
    }
    
    public void reset() {
      jcbStatus.setRaItems(dM.getDataModule().getKlijentStat(), "SID", "NAZIV", "*", "");
      jcbStatus.setSelectedIndex(0);
      jcbUser.setRaItems(dM.getDataModule().getUseri(), "CUSER", "NAZIV", "*", "");
      jcbUser.setSelectedIndex(0);
      jcbSeg.setRaItems(dM.getDataModule().getSegmentacija(), "CSEG", "NAZIV", "*", "");
      jcbSeg.setSelectedIndex(0);
      jlrGrad.setText("");
      jlrGrad.forceFocLost();
      currStatus = currUser = currSeg = "*";
      currGrad = "";
      
    }
    
    public boolean show(Container parent) {
      if (!super.show(parent, main, "Filtriranje klijenata")) {
        jcbStatus.setDataValue(currStatus);
        jcbUser.setDataValue(currUser);
        jcbSeg.setDataValue(currSeg);
        jlrGrad.setText(currGrad);
        jlrGrad.forceFocLost();
        return false;
      }
      boolean chg = 
          !currStatus.equals(jcbStatus.getDataValue()) ||
          !currUser.equals(jcbUser.getDataValue()) ||
          !currSeg.equals(jcbSeg.getDataValue()) ||
          !currGrad.equals(jlrGrad.getText());
      
      currStatus = jcbStatus.getDataValue();
      currUser = jcbUser.getDataValue();
      currSeg = jcbSeg.getDataValue();
      currGrad = jlrGrad.getText();
      return chg;
    }
  
    public DataSet getFilteredSet() {    
      Condition condStatus = jcbStatus.getSelectedIndex() == 0 ? Condition.none : Condition.equal("SID", currStatus);
      Condition condSeg = jcbSeg.getSelectedIndex() == 0 ? Condition.none : Condition.equal("CSEG", currSeg);
      Condition condGrad = currGrad.length() == 0 ? Condition.none : 
         Condition.in("MJ", new String[] { 
            currGrad, currGrad.toLowerCase(), currGrad.toUpperCase(),
            currGrad.substring(0, 1).toUpperCase() + currGrad.substring(1),
            currGrad.substring(0, 1).toUpperCase() + currGrad.substring(1).toLowerCase()});
      Condition condUser = jcbUser.getSelectedIndex() == 0 ? Condition.none : 
          Condition.raw("EXISTS (SELECT * FROM kontakti WHERE kontakti.cklijent = klijenti.cklijent" +
          		" AND kontakti.cuser='" + currUser + "' AND kontakti.status='" + frmKampanje.CLOSED + "')");
      
      Condition all = condStatus.and(condSeg).and(condUser).and(condGrad);
      if (all == Condition.none) return Klijenti.getDataModule().getQueryDataSet();
      
      System.out.println(all);
      return Klijenti.getDataModule().getTempSet(all);
    }
}
