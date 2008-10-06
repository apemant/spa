/****license*****************************************************************
**   file: frmGrizvPrim.java
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
import hr.restart.util.raMatPodaci;

import java.awt.Rectangle;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmGrizvPrim extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  QueryDataSet paramQDS ;
  jpGrizvPrim jpDetail;
  jpGrizvPrim2 jpDetail2;
  Rectangle rmd;
  short cizv=0;
  short cgrizv=0;
  short cvrp = 0;
  boolean param2 = false;
  frmIzvjDef fIzvjDef;
  frmVrstePrim fVP;

  public frmGrizvPrim(frmIzvjDef f, QueryDataSet tempParamQDS, short cizv, short cgrizv) {
    super(2);
    try {
      param2 = true;
      fIzvjDef = f;
      fIzvjDef.raDetail.setEnabled(false);
      fIzvjDef.raMaster.setEnabled(false);
      this.paramQDS = tempParamQDS;
      this.cizv = cizv;
      this.cgrizv = cgrizv;

      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public frmGrizvPrim(frmVrstePrim f, QueryDataSet tempParamQDS, short cvrp) {
    super(2);
    try {
      fVP = f;
      this.paramQDS = tempParamQDS;
      this.cvrp = cvrp;
      fVP.setEnabled(false);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
    }
  }

  public void SetFokus(char mode) {
    if(param2)
    {
      if (mode == 'N') {
        jpDetail.jlrCvrp.forceFocLost();
        jpDetail.jlrCvrp.requestFocus();
      } else if (mode == 'I') {
        rcc.setLabelLaF(jpDetail.jlrCvrp, true);
        rcc.setLabelLaF(jpDetail.jlrNaziv, true);
        rcc.setLabelLaF(jpDetail.jbSelCvrp, true);
        jpDetail.jlrCvrp.requestFocus();
      }
    }
    else
    {
      if (mode == 'N') {
        jpDetail2.jlrCizv.forceFocLost();
        jpDetail2.jlrCgrizv.forceFocLost();
        jpDetail2.jlrCizv.requestFocus();
      } else if (mode == 'I') {
        rcc.setLabelLaF(jpDetail2.jlrCizv, true);
        rcc.setLabelLaF(jpDetail2.jlrOpis, true);
        rcc.setLabelLaF(jpDetail2.jbSelCizv, true);
        rcc.setLabelLaF(jpDetail2.jlrCgrizv, true);
        rcc.setLabelLaF(jpDetail2.jlrNaziv, true);
        rcc.setLabelLaF(jpDetail2.jbSelCgrizv, true);
        jpDetail2.jlrCizv.requestFocus();
      }
    }
  }

  public boolean Validacija(char mode) {
    if(param2)
    {
      getRaQueryDataSet().setShort("CIZV", cizv);
      getRaQueryDataSet().setShort("CGRIZV", cgrizv);
      if (vl.isEmpty(jpDetail.jlrCvrp))
       return false;
      if (notUnique())
      {
        this.jpDetail.jlrCvrp.requestFocus();
        JOptionPane.showConfirmDialog(this.jpDetail,"Zapis postoji !", "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return false;
      }
    }
    else
    {
      getRaQueryDataSet().setShort("CVRP", cvrp);
      if (vl.isEmpty(jpDetail2.jlrCizv) || vl.isEmpty(jpDetail2.jlrCgrizv))
        return false;
      if (notUnique())
      {
        this.jpDetail2.jlrCizv.requestFocus();
        JOptionPane.showConfirmDialog(this.jpDetail2,"Zapis postoji !", "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return false;
      }
    }


    return true;
  }

  private boolean notUnique()
  {
    if(param2)
      return plUtil.getPlUtil().checkGrizvPrimUnique(cizv, cgrizv, getRaQueryDataSet().getShort("CVRP"));
    else
      return plUtil.getPlUtil().checkGrizvPrimUnique(getRaQueryDataSet().getShort("CIZV"),
                                                      getRaQueryDataSet().getShort("CGRIZV"), cvrp);
  }

  private void jbInit() throws Exception {
/*    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-630;
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-260;
    this.setLocation((int)x/2,(int)y/2);
    this.getJpTableView().setMinimumSize(new Dimension(630, 150));*/
    this.setRaQueryDataSet(paramQDS);
    addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_ESCAPE) {
        public void keyAction() {
          ESCPressed();
        }
      });

    if(param2)
    {
      this.setVisibleCols(new int[] {2});
      getJpTableView().addTableModifier(
        new hr.restart.swing.raTableColumnModifier("CVRP",new String[] {"CVRP","NAZIV"},dm.getVrsteprim())
      );
    }
    else
    {
      this.setVisibleCols(new int[] {0, 1});
      getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CGRIZV",new String[] {"CGRIZV","NAZIV"},dm.getGrupeizv())
      );
      getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CIZV",new String[] {"CIZV","OPIS"},dm.getPlizv())
      );
    }

    if(param2)
    {
      this.setTitle("Vrste primanja za grupu "+ fIzvjDef.getDetailSet().getString("NAZIV").toLowerCase());
      this.setSize(630, 325);
      jpDetail = new jpGrizvPrim(this);
      this.setRaDetailPanel(jpDetail);
    }
    else
    {
      //this.setTitle("Vrste primanja za grupu izvještaja");
      this.setTitle("Izvještaj za vrstu primanja " + fVP.getRaQueryDataSet().getString("NAZIV").toLowerCase());
      this.setSize(625, 360);
      jpDetail2 = new jpGrizvPrim2(this);
      this.setRaDetailPanel(jpDetail2);
    }
  }

   public boolean ValidacijaPrijeIzlaza(){
    if(param2)
    {
      fIzvjDef.raDetail.setEnabled(true);
      fIzvjDef.raMaster.setEnabled(true);
    }
    else
    {
      fVP.setEnabled(true);
    }
    return true;
   }

   public void ESCPressed()
   {
     this.hide();
   }

}
