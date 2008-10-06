/****license*****************************************************************
**   file: jpPreselectDoc.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.raMasterDetail;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.Font;
import java.sql.Timestamp;

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

abstract public class jpPreselectDoc extends PreSelect {
  public char par;
  public char skl;
  hr.restart.util.raMasterDetail rmd;
  hr.restart.util.Util uut = hr.restart.util.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  JlrNavField jrfCSKL = new JlrNavField();
  JraTextField jtfDATUMDO = new JraTextField();
  JLabel jlBRDOK = new JLabel();
  JraTextField jtfBRDOK = new JraTextField();
  JlrNavField jrfCPAR = new JlrNavField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JLabel jlDatum = new JLabel();
  JraButton jbCSKL = new JraButton();
  JraTextField jtfDATUMOD = new JraTextField();
  JlrNavField jrfNAZPAR = new JlrNavField();
  JraTextField jtfVRDOK = new JraTextField();
  JLabel jlVRDOK = new JLabel();
  JraButton jbCPAR = new JraButton();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jpSelDoc = new JPanel();
  JLabel jlCSKL = new JLabel();
  JLabel jlCPAR = new JLabel();
  String raDokument;
  /* Dodao tomislav da bi se sprijecilo neke dokumente da rade na skladistu materijala
  	a za njih se pusta obracun !
  */
  boolean isMatDocAllowed = false; // ako je tru dozvoljeni su dokumenti za mat skladiste
  boolean isMatDocifObracAllowed = false; // ako je tru i ako je isMatDocAllowed tada je
  										  // dozvoljeno i za mat skladišta koja se obracunavaju
  raMiscTests rMT = raMiscTests.getRaMiscTest();
  
  boolean firstTime = true;
  
  DataRow preselData;

  abstract public void defaultMatDocAllowed();
  abstract public void defaultMatDocAllowedifObrac();

  public jpPreselectDoc() {
  }
/**
 * Verzija ekrana
 * @param version - 'S' - trazi skladiste
 *                - 'P' - trazi partnera
 */
  public jpPreselectDoc(char cskl, char cpar) {
    par=cpar;
    skl=cskl;
    try {
      jbInit();
      installResetButton();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void resetDefaults() {
    prepareRaDokument();
    if (jrfCPAR.isVisible())
      jrfCPAR.requestFocus();
    else jrfCSKL.requestFocus();
  }
  
  private void jbInit() throws Exception {
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        if (skl=='D') {
          jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
        }
        else if (skl=='M') {
          jrfCSKL.setRaDataSet(hr.restart.robno.Util.getMatSkladFromCorg());
        }
        else {
          jrfCSKL.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        }
        if (par!='D') {
          jrfCPAR.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        }
      }
    });


    defaultMatDocAllowed();
    defaultMatDocAllowedifObrac();
    jpSelDoc.setLayout(xYLayout1);

    jlVRDOK.setText("Vrsta dokumenta");
    jtfVRDOK.setDisabledTextColor(jtfVRDOK.getForeground());
    jtfVRDOK.setEnablePopupMenu(false);
    jtfVRDOK.setEnabled(false);
    jtfVRDOK.setOpaque(false);
    jtfVRDOK.setFont(jtfVRDOK.getFont().deriveFont(Font.BOLD));
    jtfVRDOK.setColumnName("VRDOK");
    jtfDATUMOD.setColumnName("DATDOK");
    jtfDATUMOD.setHorizontalAlignment(SwingConstants.CENTER);
    jlDatum.setText("Datum  (od - do)");
    jlBRDOK.setText("Broj dokumenta");
    jtfBRDOK.setColumnName("BRDOK");
    jtfDATUMDO.setColumnName("DATDOK");
    jtfDATUMDO.setHorizontalAlignment(SwingConstants.CENTER);
    jrfCSKL.setColumnName("CSKL");

    if (skl=='D') {
      jlCSKL.setText("Skladište");
      jrfCSKL.setNavColumnName("CSKL");
      jrfCSKL.setColNames(new String[] {"NAZSKL"});
      jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
      jrfCSKL.setVisCols(new int[]{0,1});
      jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());

      jrfNAZSKL.setColumnName("NAZSKL");
      jrfNAZSKL.setSearchMode(1);
      jrfNAZSKL.setNavProperties(jrfCSKL);
    }
    else if (skl=='M') {
      jlCSKL.setText("Skladište");
      jrfCSKL.setNavColumnName("CSKL");
      jrfCSKL.setColNames(new String[] {"NAZSKL"});
      jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
      jrfCSKL.setVisCols(new int[]{0,1});
      jrfCSKL.setRaDataSet(hr.restart.robno.Util.getMatSkladFromCorg());
      jrfNAZSKL.setColumnName("NAZSKL");
      jrfNAZSKL.setSearchMode(1);
      jrfNAZSKL.setNavProperties(jrfCSKL);
    }
    else {
      jlCSKL.setText("Org. jedinica");
      jrfCSKL.setNavColumnName("CORG");
      jrfCSKL.setColNames(new String[] {"NAZIV"});
      jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
      jrfCSKL.setVisCols(new int[]{0,1});
      jrfCSKL.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      jrfNAZSKL.setColumnName("NAZIV");
      jrfNAZSKL.setSearchMode(1);
      jrfNAZSKL.setNavProperties(jrfCSKL);
    }
    if (par=='D') {
      jlCPAR.setText("Partner");
      jrfCPAR.setColumnName("CPAR");
      jrfCPAR.setColNames(new String[] {"NAZPAR"});
      jrfCPAR.setVisCols(new int[]{0,1,2});
      jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
      jrfCPAR.setRaDataSet(dm.getPartneri());
      jrfNAZPAR.setColumnName("NAZPAR");
      jrfNAZPAR.setSearchMode(1);
      jrfNAZPAR.setNavProperties(jrfCPAR);
    }
    else if (par=='B') {
      jlCPAR.setText("Prodajno mjesto");
      jrfCPAR.setColumnName("CBLAG");
      jrfCPAR.setNavColumnName("CPRODMJ");
      jrfCPAR.setColNames(new String[] {"NAZPRODMJ"});
      jrfCPAR.setVisCols(new int[]{0,1,2});
      jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
      jrfCPAR.setRaDataSet(dm.getProd_mj());
      jrfNAZPAR.setColumnName("NAZPRODMJ");
      jrfNAZPAR.setSearchMode(1);
      jrfNAZPAR.setNavProperties(jrfCPAR);
    }
    else {
      jlCPAR.setText("Mjesto troška");
      jrfCPAR.setColumnName("CORG");
      jrfCPAR.setColNames(new String[] {"NAZIV"});
      jrfCPAR.setVisCols(new int[]{0,1,2});
      jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
      jrfCPAR.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      jrfNAZPAR.setColumnName("NAZIV");
      jrfNAZPAR.setSearchMode(1);
      jrfNAZPAR.setNavProperties(jrfCPAR);
    }
    jrfCSKL.setNavButton(jbCSKL);
    jrfCPAR.setNavButton(jbCPAR);
    jpSelDoc.add(jlVRDOK, new XYConstraints(15, 20, -1, -1));
    jpSelDoc.add(jtfVRDOK, new XYConstraints(150, 20, 50, -1));
    jpSelDoc.add(jlCSKL, new XYConstraints(15, 45, -1, -1));
    jpSelDoc.add(jrfCSKL, new XYConstraints(150, 45, 100, -1));
    jpSelDoc.add(jrfNAZSKL, new XYConstraints(260, 45, 275, -1));
    jpSelDoc.add(jbCSKL, new XYConstraints(539, 45, 21, 21));
    jpSelDoc.add(jlBRDOK, new XYConstraints(15, 95, -1, -1));
    jpSelDoc.add(jtfBRDOK, new XYConstraints(150, 95, 100, -1));
    jpSelDoc.add(jlDatum, new XYConstraints(15, 120, -1, -1));
    jpSelDoc.add(jtfDATUMOD, new XYConstraints(150, 120, 100, -1));
    jpSelDoc.add(jtfDATUMDO, new XYConstraints(260, 120, 100, -1));
    if (par!='F') {
      jpSelDoc.add(jlCPAR, new XYConstraints(15, 70, -1, -1));
      jpSelDoc.add(jrfCPAR, new XYConstraints(150, 70, 100, -1));
      jpSelDoc.add(jrfNAZPAR, new XYConstraints(260, 70, 275, -1));
      jpSelDoc.add(jbCPAR, new XYConstraints(539, 70, 21, 21));
      jpSelDoc.add(jlBRDOK, new XYConstraints(15, 95, -1, -1));
      jpSelDoc.add(jtfBRDOK, new XYConstraints(150, 95, 100, -1));
      jpSelDoc.add(jlDatum, new XYConstraints(15, 120, -1, -1));
      jpSelDoc.add(jtfDATUMOD, new XYConstraints(150, 120, 100, -1));
      jpSelDoc.add(jtfDATUMDO, new XYConstraints(260, 120, 100, -1));
      xYLayout1.setHeight(161);
      xYLayout1.setWidth(575);
    }
    else {
      jpSelDoc.add(jlBRDOK, new XYConstraints(15, 70, -1, -1));
      jpSelDoc.add(jtfBRDOK, new XYConstraints(150, 70, 100, -1));
      jpSelDoc.add(jlDatum, new XYConstraints(15, 95, -1, -1));
      jpSelDoc.add(jtfDATUMOD, new XYConstraints(150, 95, 100, -1));
      jpSelDoc.add(jtfDATUMDO, new XYConstraints(260, 95, 100, -1));
      xYLayout1.setHeight(136);
      xYLayout1.setWidth(575);
    }
  }
  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres) {
    showJpSelectDoc(raDocumentC, owner, showPres, "", true);
  }
  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres, String tit) {
    showJpSelectDoc(raDocumentC, owner, showPres, tit, true);
  }
  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres, String tit, boolean imaPartner) {
    rmd=owner;
    raDokument=raDocumentC;
    setSelDataSet(owner.getMasterSet());
    addSelRange(jtfDATUMOD, jtfDATUMDO);
    setSelPanel(jpSelDoc);
    if (!imaPartner) {
      jrfCPAR.setText("");
      jrfCPAR.forceFocLost();
      jlCPAR.setVisible(false);
      jrfCPAR.setVisible(false);
      jrfNAZPAR.setVisible(false);
      jbCPAR.setVisible(false);
    }
    else {
      jlCPAR.setVisible(true);
      jrfCPAR.setVisible(true);
      jrfNAZPAR.setVisible(true);
      jbCPAR.setVisible(true);
    }
    if (showPres) {
      showPreselect(owner ,"Predselekcija - "+tit);
    // zakomentir'o ab.f
//    } else {
//      doSelect();
//      owner.go();
    } else {
      owner.setPreSelect(this,"Predselekcija - "+tit,false);
    }
  }
  
  public void memorize() {
    preselData = new DataRow(getSelRow());
    dM.copyColumns(getSelRow(), preselData);
    firstTime = false;
  }
  
  public void SetFokus() {
    if (firstTime) prepareRaDokument();
    else {
      dM.copyColumns(preselData, getSelRow());
      jrfCSKL.forceFocLost();
      if (jrfCPAR.isVisible())
        jrfCPAR.forceFocLost();
    }
    if (jrfCPAR.isVisible())
      jrfCPAR.requestFocus();
    else jrfCSKL.requestFocus();
  }
  public boolean Validacija() {
    if (hr.restart.util.Valid.getValid().isEmpty(this.jrfCSKL))
      return false;
    // zakomentir'o sg.!f
//    if (getSelRow().getInt("BRDOK")>0) {
//      getSelRow().setTimestamp("DATDOK-from",hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
//    }
    if (!hr.restart.util.Util.getUtil().isValidRange(getSelRow().getTimestamp("DATDOK-from"), getSelRow().getTimestamp("DATDOK-to"))) {
      jtfDATUMOD.requestFocus();
      javax.swing.JOptionPane.showConfirmDialog(null,
          "Datum od je veæi od datuma do ili godine nisu identiène","Greska",
             javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }

    String godinaRad=null;
    boolean radDvije= false;

//System.out.println(jrfCSKL.getNavColumnName());
    if (jrfCSKL.getNavColumnName().equalsIgnoreCase("CORG")) {
      godinaRad = Aut.getAut().getKnjigodRobno();
//      hr.restart.util.lookupData.getlookupData().raLocate(dm.getKnjigod(),"CORG",
//          hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(jrfCSKL.getText()));
//      godinaRad =dm.getKnjigod().getString("GOD");

      radDvije= dm.getKnjigod().getString("STATRADA").equalsIgnoreCase("D");
    } else {
      godinaRad =rMT.getGodinaRad(jrfCSKL.getText());
      radDvije= rMT.isRadUDvijeGodine(jrfCSKL.getText());
    }

//System.out.println(godinaRad);
//System.out.println(radDvije);

/*    if (!hr.restart.util.Util.getUtil().getYear(getSelRow().getTimestamp("DATDOK-from")).equalsIgnoreCase(godinaRad)){
      if (!( radDvije&&
          hr.restart.util.Util.getUtil().getYear(getSelRow().getTimestamp("DATDOK-from")).
        equalsIgnoreCase(String.valueOf(Integer.parseInt(godinaRad)+1)))){
        jtfDATUMOD.requestFocus();
        javax.swing.JOptionPane.showConfirmDialog(null,
            "Datumi nisu u tekuæoj godini ili nije mod rada \"DVIJE GODINE\" !!!","Greska",
             javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
      }

    }
*/



/* dodao T.V. provjera skladista materijala*/
/* Zakomentirao Siniša zbog problema sa PTE u TP    
/*	if (rMT.isSkladMaterijalno(jrfCSKL.getText())) {
		if (isMatDocAllowed){
		  if (rMT.isSkladMatAndObrac(jrfCSKL.getText()) && !isMatDocifObracAllowed) {
		  	// ne moze se za ona skladista koja su materijalna i obracunavaju se
  			javax.swing.JOptionPane.showConfirmDialog(null,
  					"Ovaj dokument se ne može primjenjivati za skladišta koja se moraju obraèunavati",
      				"Greska",
         			javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
			return false;

		  }
		}
		else {
			// nedozvoljavamo za skladište materijala
  		javax.swing.JOptionPane.showConfirmDialog(null,
  			"Ovaj dokument se ne može primjenjivati za skladišta materijala",
      		"Greška",javax.swing.JOptionPane.DEFAULT_OPTION,
        	javax.swing.JOptionPane.ERROR_MESSAGE);
        return false ;
		}

	}*/
	memorize();
    return true;
  }
  void prepareRaDokument() {
    System.out.println("prepareRaDok: "+getSelRow().getString("CSKL").equals("")+" '"+getSelRow().getString("CSKL")+"'");
    getSelRow().setString("VRDOK",raDokument);
    if (skl=='D') { // dodao tomo
//      jrfCSKL.setText(hr.restart.sisfun.frmParam.getParam("robno","defCskl"));
      jrfCSKL.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
      jrfCSKL.forceFocLost();
    }
    else {
      jrfCSKL.setText(hr.restart.sisfun.raUser.getInstance().getDefCorg());
	  System.out.println("Knjig: "+jrfCSKL.getText());
	  System.out.println("Knjig2: "+dlgGetKnjig.getKNJCORG());
      if (jrfCSKL.getText().equals("") || jrfCSKL.getText()==null) {
    	  System.out.println("Knjig: "+dlgGetKnjig.getKNJCORG());
    	  jrfCSKL.setText(dlgGetKnjig.getKNJCORG());
      }
      jrfCSKL.forceFocLost();
    }
    jrfCPAR.setText("");
    jrfCPAR.forceFocLost();
    jrfCSKL.requestFocus();
    jrfCSKL.selectAll();
//    getSelRow().setTimestamp("DATDOK-from",hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    Timestamp day = vl.getToday();
    if (!Aut.getAut().getKnjigodRobno().equals(vl.findYear()) &&
        !dm.getKnjigod().getString("STATRADA").equalsIgnoreCase("D"))
      day = uut.getYearEnd(Aut.getAut().getKnjigodRobno());

      getSelRow().setTimestamp("DATDOK-from",day);
      getSelRow().setTimestamp("DATDOK-to",day);

      getSelRow().post();
  }
  public static jpPreselectDoc getPres() {
    return null;
  }
  public java.sql.Timestamp getDefDate() {
    return getSelRow().getTimestamp("DATDOK-to");
  }
  public String getRaDokument() {
    return raDokument;
  }
  public boolean isBRDOK() {
  	if (getSelRow().getInt("BRDOK")==0) return false;
  	return true;
  }
}
