/****license*****************************************************************
**   file: frmRepldef.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;


public class frmRepldef extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpRepldef jpDetail;


  public frmRepldef() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraBatch_index, false);
      rcc.setLabelLaF(jpDetail.jraImetab, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jpDetail.jlrRbr_url.forceFocLost();
      jpDetail.jlrRbr_url2.forceFocLost();
      jpDetail.jraBatch_index.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jlrRbr_url.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraBatch_index))
      return false;
    if (mode == 'N' && vl.notUnique(new JTextComponent[]
      {jpDetail.jraBatch_index, jpDetail.jraImetab, jpDetail.jlrRbr_url}))
      return false;
    if(jpDetail.jlrRbr_url.getText().equals(""))
    {
      jpDetail.jlrRbr_url.requestFocus();
      JOptionPane.showConfirmDialog(this,"Obavezan unos polja \"URL iz\" !",
                                      "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return false;
    }
    if(jpDetail.jlrRbr_url2.getText().equals(""))
    {
      jpDetail.jlrRbr_url2.requestFocus();
      if(jpDetail.jlrRbr_url2.getText().equals(""))
      {
        JOptionPane.showConfirmDialog(this,"Obavezan unos polja \"URL u\" !",
                                      "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return false;
      }
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getRepldef());
    this.setVisibleCols(new int[] {0, 1});
    jpDetail = new jpRepldef(this);
    this.setRaDetailPanel(jpDetail);
  }

//  public boolean runFirstESC()
// {
//   if(!jrfCSKL.getText().equals(""))
//     return true;
//   return false;
// }
//
// public void firstESC()
// {
//   if(!jlfCBanka.getText().equals(""))
//   {
//     EnDisCBAN(1);
//     jlfCBanka.setText("");
//     jlfCBanka.forceFocLost();
//     jlfCBanka.requestFocus();
//   }
//   else if(!jrfCSKL.getText().equals(""))
//   {
//     EnDisCSKL(1);
//     jrfCSKL.setText("");
//     jrfCSKL.forceFocLost();
//     jrfCSKL.requestFocus();
//   }
// }
}
