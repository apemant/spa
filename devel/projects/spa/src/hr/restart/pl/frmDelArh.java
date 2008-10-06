/****license*****************************************************************
**   file: frmDelArh.java
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
package hr.restart.pl;

import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;

import javax.swing.JOptionPane;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmDelArh extends frmObradaPL {
  boolean suc;

  public frmDelArh() {
  }

  public void okPress() {
    suc=makeTransaction();
  }

  private boolean makeTransaction() {
    hr.restart.db.raPreparedStatement p;
    raLocalTransaction trans = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          raTransaction.runSQL(sjQuerys.delKUMULORGARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          raTransaction.runSQL(sjQuerys.delKUMULRADARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          raTransaction.runSQL(sjQuerys.delPRIMANJAARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          raTransaction.runSQL(sjQuerys.delODBICIARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          raTransaction.runSQL(sjQuerys.delPRISUTARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          raTransaction.runSQL(sjQuerys.delRSPERIODARH(tds.getShort("godina"), tds.getShort("mjesec"),tds.getShort("rbr"), tds.getString("CORG")));
          return true;
        }
        catch (Exception ex) {
          ex.printStackTrace();
          throw ex;
        }

      }
    };
    boolean b=trans.execTransaction();
    return b;
  }
  public void showMessage() {
    if (suc) JOptionPane.showConfirmDialog(jp,"Obraèun je obrisan iz arhive !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
    else JOptionPane.showConfirmDialog(jp,"Greška kod brisanja obraèuna !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    this.hide();
  }
}