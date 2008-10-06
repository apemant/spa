/****license*****************************************************************
**   file: frmOsObjekti.java
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
package hr.restart.os;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmOsObjekti extends raMatPodaci {
  dM dm;
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel jpSel = new JPanel();
  JLabel jlOrgjJed = new JLabel();
  JLabel jlLokacije = new JLabel();
  JraTextField jrfCObjekta = new JraTextField();
  JraTextField jrfNazObjekta = new JraTextField();
  JraButton jbCOrg = new JraButton();
  XYLayout xYLayout4 = new XYLayout();

  boolean pUnos = true;
  char mod='0';

  JlrNavField jrfCOrg = new JlrNavField(){
    public void after_lookUp()
    {
      if(jrfCOrg.getText().equals("")) return;
      disableCORG();
    }
  };
  JlrNavField jrfCOrgNaz = new JlrNavField(){
    public void after_lookUp()
    {
      if(jrfCOrg.getText().equals("")) return;
      disableCORG();
    }
  };

  public frmOsObjekti()
  {
    super(2);
    try
    {
      jbInit();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

  }

  private void jbInit() throws Exception
  {
    this.setVisibleCols(new int[] {0,1,2});
    dm = dM.getDataModule();
    xYLayout4.setHeight(80);
    xYLayout4.setWidth(585);
    jpSel.setLayout(xYLayout4);

    this.getJpTableView().addTableModifier(
      new raTableColumnModifier("CORG", new String[]{"CORG", "NAZIV"}, dm.getOrgstruktura())
    );

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
        new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnjig, String newKnjig)
      {
        bind();
      };
    }
    );

    bind();
    jlLokacije.setText("Objekt");
    jpSel.add(jrfCOrg,   new XYConstraints(150, 15, 100, -1));
    jpSel.add(jlOrgjJed,  new XYConstraints(15, 15, -1, -1));
    jpSel.add(jrfCOrgNaz,         new XYConstraints(255, 15, 285, -1));
    jpSel.add(jbCOrg,            new XYConstraints(544, 15, 21, 21));
    jpSel.add(jrfCObjekta,   new XYConstraints(150, 40, 100, -1));
    jpSel.add(jrfNazObjekta,   new XYConstraints(255, 40, 285, -1));
    jpSel.add(jlLokacije,   new XYConstraints(15, 40, -1, -1));
    this.setRaDetailPanel(jpSel);
  }

  public void SetFokus(char mode)
  {
    mod = mode;
    if(mode=='N')
    {
      if(!pUnos)
      {
        jrfCOrg.setText("");
        jrfCOrg.forceFocLost();
        jrfCOrg.requestFocus();
        pUnos=false;
      }
      else
      {
        jrfCOrg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
          jrfCOrg.forceFocLost();
        jrfCObjekta.requestFocus();
      }
    }
    else if(mode=='I')
    {
      rcc.setLabelLaF(jrfCOrg, false);
      rcc.setLabelLaF(jrfCOrgNaz, false);
      rcc.setLabelLaF(jbCOrg, false);
      rcc.setLabelLaF(jrfCObjekta, false);
      jrfNazObjekta.requestFocus();
    }
  }

  public boolean Validacija(char mode)
  {
     this.getRaQueryDataSet().setString("CORG", jrfCOrg.getText());
    if (rdOSUtil.getUtil().checkObjektPK(jrfCOrg.getText(), this.getRaQueryDataSet().getString("COBJEKT")) && mode=='N')
    {
      JOptionPane.showConfirmDialog(this.jrfCOrg,"Zapis postoji !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.SetFokus(mode);
      return false;
    }
    else
    {
      return true;
    }
  }

  public boolean ValDPEscape(char mode)
  {
    if(mode=='N')
    {
      if(!jrfCOrg.isEnabled())
      {
        rcc.setLabelLaF(jrfCOrg, true);
        rcc.setLabelLaF(jrfCOrgNaz, true);
        rcc.setLabelLaF(jbCOrg, true);
        jrfCOrg.setText("");
        jrfCOrg.forceFocLost();
        jrfCOrg.requestFocus();
        return false;
      }
    }
    else
    {
      super.ValDPEscape(mode);
    }
    pUnos=true;
    mod='0';
    return true;
  }
  public void disableCORG()
  {
    rcc.setLabelLaF(jrfCOrg, false);
    rcc.setLabelLaF(jrfCOrgNaz, false);
    rcc.setLabelLaF(jbCOrg, false);
    if(mod=='N')
      jrfCObjekta.requestFocus();
  }

  public void enableCORG()
  {
    rcc.setLabelLaF(jrfCOrg, true);
    rcc.setLabelLaF(jrfCOrgNaz, true);
    rcc.setLabelLaF(jbCOrg, true);
    if(mod=='N')
      jrfCObjekta.requestFocus();
  }

  private void bind()
  {
    this.setRaQueryDataSet(rdOSUtil.getUtil().getObjektRaDataSet());
    jrfCOrgNaz.setNavProperties(jrfCOrg);
    jrfCOrgNaz.setColumnName("NAZIV");
    jlOrgjJed.setText("Org jedinica");
    jbCOrg.setText("...");
    jrfCOrg.setSearchMode(0);
    jrfCOrg.setColumnName("CORG");
    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfCOrgNaz});
    jrfCOrg.setColNames(new String[] {"NAZIV"});
    jrfCOrg.setVisCols(new int[]{0,1});
    jrfCOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCOrg.setDataSet(getRaQueryDataSet());
    jrfCOrgNaz.setColumnName("NAZIV");
    jrfCOrgNaz.setSearchMode(1);
    jrfCOrgNaz.setNavProperties(jrfCOrg);
    jrfCOrg.setNavButton(this.jbCOrg);
    jrfCObjekta.setDataSet(getRaQueryDataSet());
    jrfCObjekta.setColumnName("COBJEKT");
    jrfNazObjekta.setDataSet(getRaQueryDataSet());
    jrfNazObjekta.setColumnName("NAZOBJEKT");
  }

  public boolean BeforeDelete()
  {
    if(rdOSUtil.getUtil().checkObjektStavke(getRaQueryDataSet().getString("CORG"),getRaQueryDataSet().getString("COBJEKT")))
    {
      JOptionPane.showConfirmDialog(this, "Nisu pobrisane lokacije !", "Greška!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }


}