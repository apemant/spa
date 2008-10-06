/****license*****************************************************************
**   file: jpNaloziMaster.java
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
package hr.restart.gk;

import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpNaloziMaster extends JPanel {
  //master
  private frmNalozi fNalozi;
  XYLayout xYLayMaster = new XYLayout();
  JLabel jlDATUMKNJ = new JLabel();
  JraTextField jtDATUMKNJ = new JraTextField();
  JLabel jlKONTRIZNOS = new JLabel();
  JraTextField jtKONTRIZNOS = new JraTextField();
  jpBrojNaloga jpBrNal = new jpBrojNaloga();
  jpGetVrsteNaloga jpGetVrnal = new jpGetVrsteNaloga() {
    public void after_lookup_vrn() {
       aftlook_vrn();
    }
  };
  //
  public jpNaloziMaster(frmNalozi fnal) {
    try {
      fNalozi = fnal;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() {
    setLayout(xYLayMaster);
    jlDATUMKNJ.setText("Datum knjiženja");
    jlKONTRIZNOS.setText("Kontrolni iznos");
    jtDATUMKNJ.setDataSet(fNalozi.getMasterSet());
    jtDATUMKNJ.setColumnName("DATUMKNJ");
    jtKONTRIZNOS.setDataSet(fNalozi.getMasterSet());
    jtKONTRIZNOS.setColumnName("KONTRIZNOS");
    jpGetVrnal.setDataSet(fNalozi.getMasterSet());
    jpBrNal.jlBrojNaloga.setText("Broj naloga");
    
    xYLayMaster.setWidth(545);
    xYLayMaster.setHeight(130);
    
    add(jpGetVrnal,    new XYConstraints(0, 0, -1, -1));
    add(jpBrNal,   new XYConstraints(0, 27, -1, -1));
    add(jlDATUMKNJ,  new XYConstraints(15, 70, -1, -1));
    add(jtDATUMKNJ,    new XYConstraints(150, 70, 100, -1));
    add(jlKONTRIZNOS,   new XYConstraints(15, 95, -1, -1));
    add(jtKONTRIZNOS,    new XYConstraints(150, 95, 100, -1));
  }
  public void setCVRNALFromPreSelect(boolean afterLookup) {
    if (fNalozi.getMasterSet().isEditingNewRow())
      fNalozi.getMasterSet().setString("CVRNAL", fNalozi.getPreSelect().getSelRow().getString("CVRNAL"));
    doVrNalAfterLookup = afterLookup;
    jpGetVrnal.jlrCVRNAL.forceFocLost();
    doVrNalAfterLookup = true;
  }
  boolean doVrNalAfterLookup = true;
  public void aftlook_vrn() {
    if (!doVrNalAfterLookup) return;
    if (Valid.getValid().chkIsEmpty(jpGetVrnal.jlrCVRNAL)) return;
    fNalozi.getPreSelect().getSelRow().setString("CVRNAL", fNalozi.getMasterSet().getString("CVRNAL"));
    fNalozi.handleVRNAL_UI(false,true);
/*    jpBrNal.initJP(
      hr.restart.zapod.dlgGetKnjig.getKNJCORG(false),
      Valid.getValid().findYear(fNalozi.getPreSelect().getSelRow().getTimestamp("DATUMKNJ-to")),
      fNalozi.getMasterSet().getString("CVRNAL"), 
      0
      );*/
    jpBrNal.noviBrojNaloga();
  }
}