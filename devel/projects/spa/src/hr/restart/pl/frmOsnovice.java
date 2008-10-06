/****license*****************************************************************
**   file: frmOsnovice.java
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


public class frmOsnovice extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpOsnoviceMaster jpMaster;
  jpOsnDetail jpDetail;

  String[] key = new String[] {"COSN"};

  public frmOsnovice() {
//    super(1,2);
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
      rcc.setLabelLaF(jpMaster.jraCosn, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jpMaster.jraCosn.requestFocus();
    } else if (mode == 'I') {
      jpMaster.jraOpis.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraCosn))
      return false;
     if(mode=='N' && vl.notUnique(this.jpMaster.jraCosn))
      return false;
    return true;
  }

  public void EntryPointDetail(char mode) {
    // Disabla tekst komponentu kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCvrp, false);
    }
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

  public void SetFokusDetail(char mode) {
//    getDetailSet().enableDataSetEvents(false);
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
    if (mode == 'N' && notUnique()) /**@todo: Provjeriti jedinstvenost kljuca detaila */
      return false;
    return true;
  }

  public boolean notUnique() {
   if(plUtil.getPlUtil().checkOsnoviceUnique(getDetailSet()))
    {
      JOptionPane.showConfirmDialog(this.jpDetail,"Zapis postoji !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jpDetail.jlrCvrp.requestFocus();
      return true;
    }
    return false;
  }

  private void jbInit() throws Exception {
    this.setMasterSet(dm.getPlosnovice());
    this.setNaslovMaster("Osnovice");
    this.setVisibleColsMaster(new int[] {0, 1});
    this.setMasterKey(key);
    jpMaster = new jpOsnoviceMaster(this);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getPlosnprim());
    this.setNaslovDetail("Stavke osnovica");
    this.setVisibleColsDetail(new int[] {0});
    this.setDetailKey(key);
    this.raDetail.getJpTableView().addTableModifier(
      new raTableColumnModifier("CVRP", new String[]{"CVRP", "NAZIV"}, dm.getVrsteprim())
    );

//     this.raDetail.getJpTableView().addTableModifier(
//      new raTableColumnModifier("COSN", new String[]{"COSN", "OPIS"}, dm.getPlosnovice())
//    );

    jpDetail = new jpOsnDetail(this);
    this.setJPanelDetail(jpDetail);
  }

   public void refilterDetailSet() {
    setNaslovDetail("Vrste primanja koja se zbrajaju u osnovicu  "+getMasterSet().getString("OPIS").toLowerCase());
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
