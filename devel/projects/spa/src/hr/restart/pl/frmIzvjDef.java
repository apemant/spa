/****license*****************************************************************
**   file: frmIzvjDef.java
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
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmIzvjDef extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  jpIzvjDefMaster jpMaster;
  jpIzvjDefDetail jpDetail;

  raNavAction rnvIzvProm = new raNavAction("Primanja",raImages.IMGSTAV,KeyEvent.VK_F6) {
    public void actionPerformed(ActionEvent e) {
      izvPrim_action();
    }
  };

  raNavAction rnvIzvOdb = new raNavAction("Odbici",raImages.IMGHISTORY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      izvOdb_action();
    }
  };
  
  raNavAction rnvIzvZnac = new raNavAction("Podaci radnika",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      izvZnac_action();
    }
  };

  String[] key = new String[] {"CIZV"};

  public frmIzvjDef() {
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
      rcc.setLabelLaF(jpMaster.jraCizv, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jpMaster.jraCizv.requestFocus();
    } else if (mode == 'I') {
      jpMaster.jraOpis.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraCizv))
      return false;
    if (mode=='N' && vl.notUnique(jpMaster.jraCizv))
    {
      return false;
    }
    return true;
  }

  public void EntryPointDetail(char mode) {

    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCgrizv, false);
    }
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      jpDetail.jraCgrizv.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraNaziv.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jraCgrizv))
      return false;
    if (mode == 'N' && notUnique())
      return false;
    return true;
  }

  public boolean notUnique() {
   if( plUtil.getPlUtil().checkGrIzvUnique(this.getDetailSet().getShort("CIZV"),
                                this.getDetailSet().getShort("CGRIZV")))
   {
     JOptionPane.showConfirmDialog(this.jpDetail,"Zapis postoji !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
     jpDetail.jraCgrizv.requestFocus();
     return true;

   }
   return false;
  }

  void izvPrim_action() {
    QueryDataSet grizvPrimDS = new QueryDataSet();
    grizvPrimDS = plUtil.getPlUtil().getgrizvPrimDS(getDetailSet().getShort("CIZV"),
                  getDetailSet().getShort("CGRIZV"));
    frmGrizvPrim fgp = new frmGrizvPrim(this, grizvPrimDS,getDetailSet().getShort("CIZV"),
                 getDetailSet().getShort("CGRIZV"));
    startFrame.getStartFrame().centerFrame(fgp,0,fgp.getTitle());
    fgp.show();
  }

  void izvOdb_action() {
    QueryDataSet grizvOdbDS = new QueryDataSet();
    grizvOdbDS = plUtil.getPlUtil().getgrizvOdbDS(getDetailSet().getShort("CIZV"),
                  getDetailSet().getShort("CGRIZV"));
    frmGrizvOdb fgo = new frmGrizvOdb(this, grizvOdbDS,getDetailSet().getShort("CIZV"),
                  getDetailSet().getShort("CGRIZV"));
    startFrame.getStartFrame().centerFrame(fgo,0,fgo.getTitle());
    fgo.show();
  }
  
  void izvZnac_action() {
    QueryDataSet grizvZnacDS = new QueryDataSet();
    grizvZnacDS = plUtil.getPlUtil().getgrizvZnacDS(getDetailSet().getShort("CIZV"),
                  getDetailSet().getShort("CGRIZV"));
    frmGrizvZnac fgz = new frmGrizvZnac(this, grizvZnacDS,getDetailSet().getShort("CIZV"),
                  getDetailSet().getShort("CGRIZV"));
    startFrame.getStartFrame().centerFrame(fgz,0,fgz.getTitle());
    fgz.show();
  }
  
  private void jbInit() throws Exception {
    this.setMasterSet(dm.getPlizv());
    this.setNaslovMaster("Izvještaji");
    this.setVisibleColsMaster(new int[] {0, 1});
    this.setMasterKey(key);
    jpMaster = new jpIzvjDefMaster(this);
    this.setJPanelMaster(jpMaster);
    raDetail.addOption(rnvIzvProm,3);
    raDetail.addOption(rnvIzvOdb,4);
    raDetail.addOption(rnvIzvZnac,5);
    this.setDetailSet(dm.getGrupeizv());
    this.setVisibleColsDetail(new int[] {0, 1, 2});
    this.setDetailKey(key);
    jpDetail = new jpIzvjDefDetail(this);
    this.setJPanelDetail(jpDetail);
  }

  public boolean DeleteCheckDetail()
  {
    if(plUtil.getPlUtil().checkGrIzvStavke(getDetailSet().getShort("CIZV"), getDetailSet().getShort("CGRIZV")))
    {
      JOptionPane.showConfirmDialog(this, "Nisu pobrisane stavke !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }

    return true;
  }

  public boolean DeleteCheckMaster()
  {
    if(getDetailSet().getRowCount()>0)
    {
      JOptionPane.showConfirmDialog(this, "Nisu pobrisane stavke !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

   public void refilterDetailSet() {
    setNaslovDetail("Grupe za izvještaj  "+getMasterSet().getString("OPIS").toLowerCase());
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
