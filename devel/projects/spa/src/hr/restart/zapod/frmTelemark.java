/****license*****************************************************************
**   file: frmTelemark.java
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

import hr.restart.baza.Telehist;
import hr.restart.baza.Telemark;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraTextField;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raSifraNaziv;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class frmTelemark extends raSifraNaziv {
  hr.restart.baza.dM dm;
  JraTextField jrEMADR;
  raNavAction rnvSifKup = new raNavAction("Kupci",raImages.IMGOPEN,KeyEvent.VK_F12){
    public void actionPerformed(ActionEvent e) {
      handleTelemarketer();
    }
  };

  public frmTelemark() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setRaDataSet(Telemark.getDataModule().getQueryDataSet());
    this.setRaColumnSifra("CTEL");
    this.setRaColumnNaziv("IME");
    this.setRaText("Telemarketer");
    this.jlNaziv.setText("Ime i prezime");
    raDataIntegrity.installFor(this, true);
    jrEMADR.setColumnName("EMADR");
    jrEMADR.setDataSet(getRaDataSet());
    
    this.addOption(rnvSifKup,5);
  }
  
  private void handleTelemarketer(){
    QueryDataSet zaPoslat = Telehist.getDataModule().getFilteredDataSet("ctel = " + this.getRaDataSet().getInt("CTEL"));
    zaPoslat.open();
    FrmPartneriTelemarketeri fpt = new FrmPartneriTelemarketeri(zaPoslat,this.getRaDataSet().getInt("CTEL"));
    hr.restart.util.startFrame.getStartFrame().centerFrame(fpt,0,"Kupci telemarketera " + getRaQueryDataSet().getString("IME"));
    fpt.show();
  }
  public void defaultAdd2Panel(int width, int heigh) {
    jrEMADR = new JraTextField();
    super.defaultAdd2Panel(555, 100);
    jp.add(new JLabel("E-Mail"), new XYConstraints(15, 63, -1, -1));
    jp.add(jrEMADR, new XYConstraints(150, 63, 390, -1));
  }
}
