/****license*****************************************************************
**   file: raPOS.java
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
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.raTransaction;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class raPOS extends raIzlazTemplate  {
//  hr.restart.util.startFrame SF;
//  QueryDataSet stanjeforChange = new QueryDataSet();

  public void initialiser(){
    what_kind_of_dokument = "POS";
  }

  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repPOS","Razduženje maloprodaje",2);
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repPOS","Razduženje maloprodaje",2);
  }
  public raPOS() {
    setPreSel((jpPreselectDoc) presPOS.getPres());
    addButtons(true,false);
    master_titel = "Razduženja maloprodaje";
    detail_titel_mno = "Stavke razduženja maloprodaje";
    detail_titel_jed = "Stavka razduženja maloprodaje";
    setMasterSet(dm.getZagPos());
    setDetailSet(dm.getStDokiPos());
    raDetail.addOption(rnvDellAllStav, 3);
    MP.BindComp();
    DP.BindComp();
  }

  public void revive() {
    super.revive();
    try {
      raTransaction.runSQL("UPDATE pos SET status='N', rdok='' WHERE " +
          Condition.equal("RDOK", key4delZag));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void brisiVezu() {
    // empty
  }
  
  public void cskl2csklart() {
    // empty
  }
  
  public void keyActionMaster() {
   frmPos2POS.razdMP(getPreSelect().getSelRow(), getMasterSet(), getDetailSet());
     if (frmPos2POS.getFrmpos().errors==null){
       if (frmPos2POS.getFrmpos().isCancelPress()) return;
       frmPos2POS.getFrmpos().saveAll();
       //raMaster.getJpTableView().fireTableDataChanged();
       afterOKSC();
     }
     else {
       getDetailSet().refresh();
       int res = JOptionPane.showConfirmDialog(raMaster.getWindow(), 
           new raMultiLineMessage(frmPos2POS.getFrmpos().errors +
                "\n\nDetaljni prikaz grešaka?",
  	            SwingConstants.LEADING), "Greška", 
  	            JOptionPane.ERROR_MESSAGE,
  	            JOptionPane.OK_CANCEL_OPTION);
       if (res == JOptionPane.OK_OPTION)
         SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            frmTableDataView errs = new frmTableDataView();
            errs.setTitle("Popis artikala s nedovoljnom zalihom");
            errs.setSaveName("view-transfer");
            errs.setDataSet(frmPos2POS.getFrmpos().errorSet);
            errs.jp.getMpTable().setAutoResizeMode(
                JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            errs.show();
          }
        });
     }
  }

}