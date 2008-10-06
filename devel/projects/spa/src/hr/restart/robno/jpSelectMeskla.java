/****license*****************************************************************
**   file: jpSelectMeskla.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.PreSelect;
import hr.restart.util.raMasterDetail;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataRow;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */


public class jpSelectMeskla extends PreSelect {

  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  rapancskl1 rpcskliz = new rapancskl1(false);
  rapancskl1 rpcsklul = new rapancskl1(false);
  JLabel jlVRDOK = new JLabel();
  JLabel jlDatum = new JLabel();
  JraTextField jtfDATUMOD = new JraTextField();
  JraTextField jtfDATUMDO = new JraTextField();
  JraTextField jtfVRDOK = new JraTextField();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jpSelDoc = new JPanel();
  String raDokument;
  raMiscTests rMT = raMiscTests.getRaMiscTest();
  boolean izlaz = true;
  JLabel jlBRDOK = new JLabel();
  JraTextField jtfBRDOK = new JraTextField();
  
  boolean firstTime = true;
  
  DataRow preselData;

  public jpSelectMeskla() {
    try {
      jbInit();
      installResetButton();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        rpcskliz.jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
        rpcsklul.jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
      }
    });

    jpSelDoc.setLayout(xYLayout1);
    xYLayout1.setHeight(155);
    rpcskliz.jlCSKL.setText("Izlazno skladište");
    rpcskliz.jrfCSKL.setColumnName("CSKLIZ");
    rpcsklul.jlCSKL.setText("Ulazno skladište");
    rpcsklul.jrfCSKL.setColumnName("CSKLUL");
    jlDatum.setText("Datum  (od - do)");
    jlVRDOK.setText("Vrsta dokumenta");
    jtfVRDOK.setText(raDokument);
    jtfVRDOK.setColumnName("VRDOK");
    jtfVRDOK.setFont(jtfVRDOK.getFont().deriveFont(Font.BOLD));
    jtfVRDOK.setOpaque(false);
    jtfVRDOK.setEnabled(false);
    jtfVRDOK.setEnablePopupMenu(false);
    jtfVRDOK.setDisabledTextColor(jtfVRDOK.getForeground());
//    jtfVRDOK.setBorder(null);
    jtfDATUMOD.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMOD.setColumnName("DATDOK");
    jtfDATUMDO.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMDO.setColumnName("DATDOK");
    jlBRDOK.setText("Broj dokumenta");
    jtfBRDOK.setColumnName("BRDOK");


  }

  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres) {
    showJpSelectDoc(raDocumentC, owner, showPres, "", true);
  }
  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres, String tit) {
    showJpSelectDoc(raDocumentC, owner, showPres, tit, true);
  }
  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres, String tit, boolean imaPartner) {
    raDokument=raDocumentC;
    setSelDataSet(owner.getMasterSet());
    addSelRange(jtfDATUMOD, jtfDATUMDO);
    setSelPanel(jpSelDoc);
    if (showPres) {
      showPreselect(owner ,"Predselekcija - "+tit);
      this.getSelPanel().repaint();
    } else {
      //doSelect();
      //owner.go();
      owner.setPreSelect(this, "Predselekcija - "+tit, false);
    }
  }

  public void SetFokus() {
    if (firstTime) {
      prepareRaDokument();
      oslobodi(true);
    } else {
      dM.copyColumns(preselData, getSelRow());
    }
    if (rpcskliz.getCSKL().length() == 0)
      rpcskliz.jrfCSKL.requestFocusLater();
    else rpcsklul.jrfCSKL.requestFocusLater();
  }

  public boolean Validacija() {

    if (val.isEmpty(rpcskliz.jrfCSKL)) return false;
    if (val.isEmpty(rpcsklul.jrfCSKL)) return false;
    if (rpcskliz.jrfCSKL.getText().equals(rpcsklul.jrfCSKL.getText())) {
         javax.swing.JOptionPane.showMessageDialog(null,
      "Skladišta moraju biti razli\u010Dita !",
      "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      oslobodi(false);
      return false;
    }
    if (getSelRow().getInt("BRDOK")>0) {
        getSelRow().setTimestamp("DATDOK-from",hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(val.findYear())));
      }

    if (!hr.restart.util.Util.getUtil().isValidRange(getSelRow().getTimestamp("DATDOK-from"), getSelRow().getTimestamp("DATDOK-to"))) {
      jtfDATUMOD.requestFocus();
      javax.swing.JOptionPane.showConfirmDialog(null,
          "Datum od je veæi od datuma do ili godine nisu identiène","Greska",
             javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }


/*    String godinaRad =rMT.getGodinaRad(rpcskliz.jrfCSKL.getText());

    if (!hr.restart.util.Util.getUtil().getYear(getSelRow().getTimestamp("DATDOK-from")).equalsIgnoreCase(godinaRad)){
      if (!(rMT.isRadUDvijeGodine(rpcsklul.jrfCSKL.getText()) &&
          hr.restart.util.Util.getUtil().getYear(getSelRow().getTimestamp("DATDOK-from")).
        equalsIgnoreCase(String.valueOf(Integer.parseInt(godinaRad)+1)))){
        jtfDATUMOD.requestFocus();
        javax.swing.JOptionPane.showConfirmDialog(null,
            "Datumi nisu u tekuæoj godini ili nije mod rada \"DVIJE GODINE\" !!!","Greska",
             javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
      }
    } */

    memorize();
    return true;
  }

  public void memorize() {
    preselData = new DataRow(getSelRow());
    dM.copyColumns(getSelRow(), preselData);
    firstTime = false;
  }
  public void oslobodi(boolean default1) {
      rpcskliz.disabCSKL(true);
      rpcskliz.Clear();
      rpcsklul.disabCSKL(true);
      rpcsklul.Clear();
      if (default1) {
        if (izlaz) {
          rpcskliz.setDefaultCSKL();
          rpcskliz.jrfCSKL.requestFocus();
        } else {
          rpcsklul.setDefaultCSKL();
          rpcsklul.jrfCSKL.requestFocus();
        }
      }
//hr.restart.sisfun.raLock.getRaLock().getDefSklad()

  }

  void prepareRaDokument() {
    getSelRow().setString("VRDOK",raDokument);
//    getSelRow().setTimestamp("DATDOK-from",hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(val.findYear())));
    getSelRow().setTimestamp("DATDOK-from",hr.restart.util.Valid.getValid().getToday());
    getSelRow().setTimestamp("DATDOK-to",hr.restart.util.Valid.getValid().getToday());
    getSelRow().post();
  }
  public void setIzlaz(boolean izlaz) {
    this.izlaz = izlaz;
    if (izlaz) {
      jpSelDoc.add(jlVRDOK, new XYConstraints(15, 20, -1, -1));
      jpSelDoc.add(jtfVRDOK, new XYConstraints(150, 20, 50, -1));

      jpSelDoc.add(jlBRDOK, new XYConstraints(15, 95, -1, -1));
      jpSelDoc.add(jtfBRDOK, new XYConstraints(150, 95, 100, -1));

      jpSelDoc.add(jlDatum, new XYConstraints(15, 120 , -1, -1));
      jpSelDoc.add(jtfDATUMOD, new XYConstraints(150, 120, 100, -1));
      jpSelDoc.add(jtfDATUMDO, new XYConstraints(255, 120, 100, -1));
      jpSelDoc.add(rpcsklul, new XYConstraints(0, 70, -1, -1));
      jpSelDoc.add(rpcskliz, new XYConstraints(0, 45, -1, -1));
    } else {
      jpSelDoc.add(jlVRDOK, new XYConstraints(15, 20, -1, -1));
      jpSelDoc.add(jtfVRDOK, new XYConstraints(150, 20, 50, -1));

      jpSelDoc.add(jlBRDOK, new XYConstraints(15, 95, -1, -1));
      jpSelDoc.add(jtfBRDOK, new XYConstraints(150, 95, 100, -1));

      jpSelDoc.add(jlDatum, new XYConstraints(15, 120 , -1, -1));
      jpSelDoc.add(jtfDATUMOD, new XYConstraints(150, 120, 100, -1));
      jpSelDoc.add(jtfDATUMDO, new XYConstraints(255, 120, 100, -1));
      jpSelDoc.add(rpcsklul, new XYConstraints(0, 45, -1, -1));
      jpSelDoc.add(rpcskliz, new XYConstraints(0, 70, -1, -1));
    }
  }
  public boolean isBRDOK() {
  	if (getSelRow().getInt("BRDOK")==0) return false;
  	return true;
  }
}