/****license*****************************************************************
**   file: frmSume.java
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



import hr.restart.baza.dM;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import javax.swing.JOptionPane;





public class frmSume extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();



  jpSumeMaster jpMaster;

  jpSumDetail jpDetail;



  String[] key = new String[] {"CSUME"};



  public frmSume() {

    try {

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }



  public void EntryPointMaster(char mode) {

    // Disabla tekst komponente kljuca kod izmjene

    if (mode == 'I') {

      rcc.setLabelLaF(jpMaster.jraCsume, false);

    }

  }



  public void SetFokusMaster(char mode) {

    if (mode == 'N') {

      jpMaster.jraCsume.requestFocus();

    } else if (mode == 'I') {

      jpMaster.jraOpis.requestFocus();

    }

  }



  public boolean ValidacijaMaster(char mode) {

    if (vl.isEmpty(jpMaster.jraCsume))

      return false;

    if(mode=='N' && vl.notUnique(this.jpMaster.jraCsume))

      return false;

    return true;

  }



  public void EntryPointDetail(char mode) {

    // Disabla tekst komponentu kljuca kod izmjene

    if (mode == 'I') {

     // rcc.setLabelLaF(jpDetail.jlrCvrp, false);

    }

  }



  public void SetFokusDetail(char mode) {

    if (mode == 'N') {

      jpDetail.jlrCvrp.forceFocLost();

      jpDetail.jlrCvrp.requestFocus();

    } else if (mode == 'I') {

      jpDetail.jlrCvrp.requestFocus();

    }

  }



  public boolean ValidacijaDetail(char mode) {

    if (vl.isEmpty(jpDetail.jlrCvrp))

      return false;

    if (mode == 'N' && notUnique())

      return false;

    else

      return true;

  }



  public boolean DeleteCheckMaster()

  {

    if((getDetailSet().isEmpty()))

    {

      return true;

    }

    else

    {

      JOptionPane.showConfirmDialog(this,"Nisu obrisane stavke dokumenta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);

      return false;

    }

  }



  public boolean notUnique() {



    if(plUtil.getPlUtil().checkSumeUnique(getDetailSet()))

    {

      JOptionPane.showConfirmDialog(this.jpDetail,"Zapis postoji !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);

      jpDetail.jlrCvrp.requestFocus();

      return true;

    }

    return false;

  }



  private void jbInit() throws Exception {



    this.setMasterSet(dm.getSume());

    this.setNaslovMaster("Sume");

    this.setVisibleColsMaster(new int[] {0, 1, 2});

    this.setMasterKey(key);

    jpMaster = new jpSumeMaster(this);

    this.setJPanelMaster(jpMaster);



    this.setDetailSet(dm.getSumePrim());

    this.setNaslovDetail("Stavke Suma");

    this.setVisibleColsDetail(new int[] {0});

    this.setDetailKey(key);



    this.raDetail.getJpTableView().addTableModifier(

      new raTableColumnModifier("CVRP", new String[]{"CVRP", "NAZIV"}, dm.getVrsteprim())

    );



//    this.raDetail.getJpTableView().addTableModifier(

//      new raTableColumnModifier("CSUME", new String[]{"CSUME", "OPIS"}, dm.getSume())

//    );



    // Ubacivanje stringa u kolonu -> ne postoji vrijednost u bazi, nego je definirana u combo box-u

    this.raMaster.getJpTableView().addTableModifier(

      new raTableColumnModifier("VRSTA", new String[]{"VRSTA"}, dm.getSume()){

        public void replaceValues() {

          tableDs.getVariant("VRSTA", getRow(), shared);

          if (shared.toString().equals("1")) setComponentText("1 Bruto");

          else setComponentText("2 Sati");

        }

      }

    );



    jpDetail = new jpSumDetail(this);

    this.setJPanelDetail(jpDetail);

  }



   public void refilterDetailSet() {

    setNaslovDetail("Stavke sume "+getMasterSet().getString("OPIS").toLowerCase());

    super.refilterDetailSet();

  }



  public void show()

  {

    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-580;

    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-310;



    raMaster.setLocation((int)x/2,(int)y/2);

    super.show();

  }

}

