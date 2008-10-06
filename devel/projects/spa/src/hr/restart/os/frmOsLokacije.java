/****license*****************************************************************
**   file: frmOsLokacije.java
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
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

public class frmOsLokacije extends raMatPodaci {
  dM dm;
  Valid vl = Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel jpSel = new JPanel();
  JraButton jbCOrg = new JraButton();
  JraButton jbCObjekt = new JraButton();
  JLabel jlOrgjJed = new JLabel();
  JLabel jlLokacije = new JLabel();
  JLabel jlObjekt = new JLabel();
  JraTextField jrfCLokacije = new JraTextField();
  JraTextField jrfNazLokacije = new JraTextField();
  XYLayout xYLayout4 = new XYLayout();
  boolean pUnos = true;
  String cObjekt="";
  char mod='0';

  JlrNavField jrfCOrg = new JlrNavField(){
    public void after_lookUp()
    {
      if(jrfCOrg.getText().equals("")) return;
      jrfCObjekt.setRaDataSet(rdOSUtil.getUtil().getObjektRaDataSet(jrfCOrg.getText()));
      disableCORG();
      if(mod=='N')
      jrfCObjekt.requestFocus();
    }
  };
  JlrNavField jrfCOrgNaz = new JlrNavField(){
    public void after_lookUp()
    {
      if(jrfCOrg.getText().equals("")) return;
      jrfCObjekt.setRaDataSet(rdOSUtil.getUtil().getObjektRaDataSet(jrfCOrg.getText()));
      disableCORG();
      if(mod=='N')
      jrfCObjekt.requestFocus();
    }
  };
  JlrNavField jrfCObjekt = new JlrNavField(){
    public void after_lookUp()
    {
      if(jrfCObjekt.getText().equals("")) return;
      cObjekt=jrfCObjekt.getText();
      disableCOBJ();
      if(mod=='N')
        jrfCLokacije.requestFocus();
    }
  };
  JlrNavField jrfNazObjekt = new JlrNavField(){
    public void after_lookUp()
   {
     if(jrfCObjekt.getText().equals("")) return;
     cObjekt=jrfCObjekt.getText();
     disableCOBJ();
     if(mod=='N')
       jrfCLokacije.requestFocus();
    }
  };

  public frmOsLokacije()
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
    dm = dM.getDataModule();
    xYLayout4.setHeight(100);
    xYLayout4.setWidth(585);
    jpSel.setLayout(xYLayout4);
    this.setVisibleCols(new int[] {0,1,2,3});

    this.getJpTableView().addTableModifier(
      new raTableColumnModifier("COBJEKT", new String[]{"COBJEKT", "NAZOBJEKT"}, dm.getOS_Objekt())
    );

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
    jlLokacije.setText("Lokacija");
    jbCObjekt.setText("...");
    jlObjekt.setText("Objekt");
    jpSel.add(jrfCOrg,   new XYConstraints(150, 15, 100, -1));
    jpSel.add(jlOrgjJed,  new XYConstraints(15, 15, -1, -1));
    jpSel.add(jrfCOrgNaz,         new XYConstraints(255, 15, 285, -1));
    jpSel.add(jbCOrg,            new XYConstraints(544, 15, 21, 21));
    jpSel.add(jrfCLokacije,     new XYConstraints(150, 65, 100, -1));
    jpSel.add(jrfNazLokacije,     new XYConstraints(255, 65, 285, -1));
    jpSel.add(jlLokacije,     new XYConstraints(15, 65, -1, -1));
    jpSel.add(jrfCObjekt,    new XYConstraints(150, 40, 100, -1));
    jpSel.add(jrfNazObjekt,     new XYConstraints(255, 40, 285, -1));
    jpSel.add(jbCObjekt,    new XYConstraints(544, 40, 21, 21));
    jpSel.add(jlObjekt,   new XYConstraints(15, 40, -1, -1));
    this.setRaDetailPanel(jpSel);
  }

  public void SetFokus(char mode)
  {
    mod = mode;
    if(mode=='N')
    {
      if(pUnos)
      {
        rcc.setLabelLaF(jrfCOrg,false);
        rcc.setLabelLaF(jrfCOrgNaz,false);
        rcc.setLabelLaF(jbCOrg,false);
        getRaQueryDataSet().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
        jrfCObjekt.setText("");
        jrfCObjekt.forceFocLost();
        jrfCObjekt.requestFocus();
        jrfNazLokacije.setText("");
        jrfCLokacije.setText("");
        pUnos=false;
      }
      else
      {
        jrfCOrg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
        jrfCOrg.forceFocLost();
        jrfCObjekt.setText(cObjekt);
        jrfCObjekt.forceFocLost();
        jrfNazLokacije.setText("");
        jrfCLokacije.setText("");
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            jrfCLokacije.requestFocus();
          }
        });
      }
    }
    else if(mode=='I')
    {
      rcc.setLabelLaF(jrfCOrg, false);
      rcc.setLabelLaF(jrfCOrgNaz, false);
      rcc.setLabelLaF(jbCOrg, false);
      rcc.setLabelLaF(jrfCObjekt, false);
      rcc.setLabelLaF(jrfNazObjekt, false);
      rcc.setLabelLaF(jbCObjekt, false);
      rcc.setLabelLaF(jrfCLokacije, false);
      jrfNazLokacije.requestFocus();
    }
  }

  public boolean Validacija(char mode)
  {
    if (vl.isEmpty(jrfCOrg) || vl.isEmpty(jrfCObjekt) ||vl.isEmpty(jrfCLokacije) ||vl.isEmpty(jrfNazLokacije))
        return false;
     this.getRaQueryDataSet().setString("CORG", jrfCOrg.getText());
    if (rdOSUtil.getUtil().checkLokacijePK(jrfCOrg.getText(), this.getRaQueryDataSet().getString("CLOKACIJE"), this.getRaQueryDataSet().getString("COBJEKT")) && mode=='N')
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
      if(jrfCOrg.getText().equals(""))
      {
        pUnos=true;
        mod='0';
        return true;
      }
      if(!jrfCObjekt.isEnabled())
      {
        rcc.setLabelLaF(jrfCObjekt, true);
        rcc.setLabelLaF(jrfNazObjekt, true);
        rcc.setLabelLaF(jbCObjekt, true);
        jrfCObjekt.setText("");
        jrfCLokacije.setText("");
        jrfNazLokacije.setText("");
        this.getRaQueryDataSet().setString("CLOKACIJE","");
        this.getRaQueryDataSet().setString("NAZLOKACIJE","");
        jrfCObjekt.forceFocLost();
        jrfCObjekt.requestFocus();
        return false;
      }
      if(!jrfCOrg.isEnabled())
      {
        rcc.setLabelLaF(jrfCOrg, true);
        rcc.setLabelLaF(jrfCOrgNaz, true);
        rcc.setLabelLaF(jbCOrg, true);
        jrfCObjekt.setText("");
        jrfCObjekt.forceFocLost();
        this.getRaQueryDataSet().setString("CLOKACIJE","");
        this.getRaQueryDataSet().setString("NAZLOKACIJE","");
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
    rcc.setLabelLaF(jrfCObjekt, true);
    rcc.setLabelLaF(jrfNazObjekt, true);
    rcc.setLabelLaF(jbCObjekt, true);
  }

  public void disableCOBJ()
  {
    rcc.setLabelLaF(jrfCObjekt, false);
    rcc.setLabelLaF(jrfNazObjekt, false);
    rcc.setLabelLaF(jbCObjekt, false);
  }
  public void enableCOBJ()
 {
   rcc.setLabelLaF(jrfCObjekt, true);
   rcc.setLabelLaF(jrfNazObjekt, true);
   rcc.setLabelLaF(jbCObjekt, true);
  }

  private void bind()
  {
    this.setRaQueryDataSet(rdOSUtil.getUtil().getLokacijeRaDataSet());
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

    jrfCObjekt.setSearchMode(0);
    jrfCObjekt.setColumnName("COBJEKT");
    jrfCObjekt.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazObjekt});
    jrfCObjekt.setColNames(new String[] {"NAZOBJEKT"});
    jrfCObjekt.setVisCols(new int[]{1,2});

    jrfCObjekt.setDataSet(getRaQueryDataSet());
    jrfNazObjekt.setColumnName("NAZOBJEKT");
    jrfNazObjekt.setSearchMode(1);
    jrfNazObjekt.setNavProperties(jrfCObjekt);
    jrfCObjekt.setNavButton(this.jbCObjekt);

    jrfCLokacije.setDataSet(getRaQueryDataSet());
    jrfCLokacije.setColumnName("CLOKACIJE");
    jrfNazLokacije.setDataSet(getRaQueryDataSet());
    jrfNazLokacije.setColumnName("NAZLOKACIJE");
  }
}