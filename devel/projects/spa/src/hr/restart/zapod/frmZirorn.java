/****license*****************************************************************
**   file: frmZirorn.java
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



import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;





public class frmZirorn extends raMatPodaci {

  private static frmZirorn fzirorn;

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();



  jpZirorn jpDetail;





  public frmZirorn() {

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }



  public static frmZirorn getFrmZirorn() {

    if (fzirorn==null) fzirorn = new frmZirorn();



    return fzirorn;

  }

  void setDefaults() {

    jpDetail.jlrCorg.emptyTextFields();

    setDomVal();

    jpDetail.jlrBrojkonta.emptyTextFields();

    jpDetail.jlrCvrnal.emptyTextFields();

    getRaQueryDataSet().setString("DEV","N");

    getRaQueryDataSet().setString("PROMET","N");

    setEnabKonto(false);

    setEnabVal(false);

  }

  void setDomVal() {

    getRaQueryDataSet().setString("OZNVAL",Tecajevi.getDomOZNVAL());

    jpDetail.jlrOznval.forceFocLost();

  }

  void setEnabKonto(boolean enab) {
    rcc.setLabelLaF(jpDetail.jlrBrojkonta,enab);
    rcc.setLabelLaF(jpDetail.jlrNazivkonta,enab);
    rcc.setLabelLaF(jpDetail.jbSelBrojkonta,enab);
    rcc.setLabelLaF(jpDetail.jlrKkonta,enab);
    rcc.setLabelLaF(jpDetail.jlrNazkkonta,enab);
    rcc.setLabelLaF(jpDetail.jbSelKkonta,enab);
  }

  void setEnabKnjig(boolean enab) {

    rcc.setLabelLaF(jpDetail.jlrCorg,enab);

    rcc.setLabelLaF(jpDetail.jlrNaziv,enab);

    rcc.setLabelLaF(jpDetail.jbSelCorg,enab);

  }

  void setEnabVal(boolean enab) {

    rcc.setLabelLaF(jpDetail.jlrOznval,enab);

    rcc.setLabelLaF(jpDetail.jlrCval,enab);

    rcc.setLabelLaF(jpDetail.jlrNazval,enab);

    rcc.setLabelLaF(jpDetail.jbSelOznval,enab);

  }

  void handleKonto() {

    boolean selected = getRaQueryDataSet().getString("PROMET").equals("D");

    setEnabKonto(selected);

    if (!selected) {

      jpDetail.jlrBrojkonta.setText("");

      jpDetail.jlrBrojkonta.emptyTextFields();
      
      jpDetail.jlrKkonta.setText("");

      jpDetail.jlrKkonta.emptyTextFields();

    }

  }

  void handleVal(boolean clear) {

    boolean selected = getRaQueryDataSet().getString("DEV").equals("D");

    setEnabVal(selected);

    if (selected && clear) {

      jpDetail.jlrOznval.setText("");

      jpDetail.jlrOznval.emptyTextFields();

    } else if (!selected) {

      setDomVal();

    }

  }

  public void SetFokus(char mode) {

    if (mode == 'N') {

      setDefaults();

      jpDetail.jlrCorg.requestFocus();

    } else if (mode == 'I') {

      setEnabKnjig(false);

      rcc.setLabelLaF(jpDetail.jraZiro,false);

      handleKonto();

      handleVal(false);

      jpDetail.jlrCvrnal.requestFocus();

    }

  }



  public boolean Validacija(char mode) {

    if (

        vl.isEmpty(jpDetail.jlrCorg)

        || vl.isEmpty(jpDetail.jraZiro)

        || vl.isEmpty(jpDetail.jlrOznval)

        || (jpDetail.jcbPromet.isSelected() && vl.isEmpty(jpDetail.jlrBrojkonta))

       ) return false;



    if (mode == 'N') {

      if (vl.notUnique(new javax.swing.text.JTextComponent[] {

        jpDetail.jlrCorg,jpDetail.jraZiro

      })) return false;

    }

    return true;



  }



  private void jbInit() throws Exception {

    this.setRaQueryDataSet(dm.getAllZirorn());

    this.setVisibleCols(new int[] {0,1,2,3});

    jpDetail = new jpZirorn(this);

    this.setRaDetailPanel(jpDetail);

    getJpTableView().addTableModifier(

      new hr.restart.swing.raTableColumnModifier("CVRNAL",new String[] {"CVRNAL","OPISVRNAL"},dm.getVrstenaloga())

    );

    getJpTableView().addTableModifier(

      new hr.restart.swing.raTableColumnModifier("CORG",new String[] {"NAZIV"},dm.getOrgstruktura())

    );

    hr.restart.sisfun.raDataIntegrity.installFor(this).setKey("ZIRO").addIgnoreTable("orgstruktura");

  }

}

