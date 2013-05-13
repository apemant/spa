package hr.restart.distrib;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import com.borland.jbcl.layout.*;
import com.borland.dx.dataset.*;
import com.borland.dx.sql.dataset.*;
import hr.restart.swing.*;
import hr.restart.baza.*;
import hr.restart.util.*;


public class frmDistart extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpDistart jpDetail;


  public frmDistart() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char mode) {
    jpDetail.getSaldoSet().deleteAllRows();
    jpDetail.getSaldoSet().insertRow(false);
    if (mode == 'N') {
      jpDetail.jlrCpar.forceFocLost();
      jpDetail.jlrCgrart.forceFocLost();
      jpDetail.jlrPj.forceFocLost();
      jpDetail.jlrSifdist.forceFocLost();
      jpDetail.jlrCpar.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jlrCpar.requestFocus();
      jpDetail.jlrCpar.forceFocLost();
      jpDetail.calcSaldo();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrCpar) || vl.isEmpty(jpDetail.jraNaziv) || vl.isEmpty(jpDetail.jraAdr) || vl.isEmpty(jpDetail.jraMj) || vl.isEmpty(jpDetail.jlrSifdist) || vl.isEmpty(jpDetail.jlrCgrart))
      return false;
//    if (mode == 'N' && vl.notUnique()) /**@todo: Provjeriti jedinstvenost kljuca */
//      return false;
    if (mode == 'N') {
      getRaQueryDataSet().setInt("CDISTART", Valid.getValid().findSeqInt("DISTART", true, false));
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(Distart.getDataModule().getQueryDataSet());
    this.setVisibleCols(new int[] {0, 2, 3, 4});
    jpDetail = new jpDistart(this);
    jpDetail.BindComponents(getRaQueryDataSet());
    this.setRaDetailPanel(jpDetail);
  }
}
