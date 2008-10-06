/****license*****************************************************************
**   file: frmGlobalMaster.java
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
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmGlobalMaster extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
  QueryDataSet mojDS = new QueryDataSet();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  String uvjet;
  String nivoOdb="";
  String osnovica = "";
  int RowPointer = 0;
  Hashtable ht = new Hashtable();
  String kljuc = "";
  raMatPodaci parent;

  String parOpis="";

  jpGlobalMasterMaster jpMaster;
  jpOdbici jpDetail;
  jpChooseOdb jpDetailOdb;
  jpChooseOdb2 jpDetailOdb2;
  char mod;


  String[] key = new String[] {"CVRODB"};

  raNavAction rnvAuto = new raNavAction("Automatika",raImages.IMGSAVEALL,KeyEvent.VK_F9) {
    public void actionPerformed(ActionEvent e) {
      automatika_action();
    }
  };

  raNavAction rnvAutoM = new raNavAction("Automatika",raImages.IMGSAVEALL,KeyEvent.VK_F9) {
    public void actionPerformed(ActionEvent e) {
      automatikaM_action();
    }
  };

  public frmGlobalMaster(raMatPodaci rMP, String uvjet, String klj, String parOpis) {
    this.parOpis= parOpis;
    parent = rMP;
    parent.setEnabled(false);
    this.uvjet = uvjet;
    this.kljuc = klj;
    try {
      setHash();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public frmGlobalMaster(String uvjet, String klj, String parOpis) {
    this.parOpis = parOpis;
    this.uvjet = uvjet;
    this.kljuc = klj;
    try {
      setHash();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPointMaster(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jlrCvrodb, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jpMaster.jlrCvrodb.requestFocus();
    } else if (mode == 'I') {
      jpMaster.jlrCvrodb.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jlrCvrodb))
      return false;
    return true;
  }

  public void EntryPointDetail(char mode) {
    // Disabla tekst komponentu kljuca kod izmjene
    if (mode == 'I') {
    }
  }

  public void SetFokusDetail(char mode) {
    this.getDetailSet().setShort("RBRODB", (short)1);

    mod = mode;
    if (mode == 'B' || mode == 'I')
      setKeys(mode);
    if (mode == 'N') {
      this.getDetailSet().setBigDecimal("IZNOS", getMasterSet().getBigDecimal("IZNOS"));
      this.getDetailSet().setBigDecimal("STOPA", getMasterSet().getBigDecimal("STOPA"));
      if(nivoOdb.length()>2)
      {
        jpDetailOdb2.jrfSifra.setText("");
        jpDetailOdb2.jrfSifra2.setText("");
        jpDetailOdb2.jrfSifra.forceFocLost();
        jpDetailOdb2.jrfSifra2.forceFocLost();

        if(nivoOdb.substring(0,2).equals(uvjet))
        {
          rcc.setLabelLaF(jpDetailOdb2.jrfNaziv, false);
          rcc.setLabelLaF(jpDetailOdb2.jrfSifra, false);
          if(uvjet.equals("RA"))
            rcc.setLabelLaF(jpDetailOdb2.jrfIme, false);
          else
            rcc.setLabelLaF(jpDetailOdb2.jrfIme, true);

          jpDetailOdb2.jrfSifra.setText(kljuc);
          jpDetailOdb2.jrfSifra.forceFocLost();
          if(nivoOdb.substring(2,4).equals("PO"))
          {
            jpDetailOdb2.jrfSifra2.setText(knjOrgStr.getKNJCORG());
            jpDetailOdb2.jrfSifra2.forceFocLost();
            rcc.setLabelLaF(jpDetailOdb2.jrfSifra2, false);
            rcc.setLabelLaF(jpDetailOdb2.jrfNaziv2, false);
            rcc.setLabelLaF(jpDetailOdb2.jbChoose2, false);
          }
          else
          {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                jpDetailOdb2.jrfSifra2.requestFocus();
              }
            });

            rcc.setLabelLaF(jpDetailOdb2.jrfNaziv2, true);
            rcc.setLabelLaF(jpDetailOdb2.jrfSifra2, true);
          }
        }
        else
        {
          jpDetailOdb2.jrfSifra2.setText(kljuc);
          jpDetailOdb2.jrfSifra2.forceFocLost();

          rcc.setLabelLaF(jpDetailOdb2.jrfSifra2, false);
          rcc.setLabelLaF(jpDetailOdb2.jrfNaziv2, false);

          if(uvjet.equals("RA"))
            rcc.setLabelLaF(jpDetailOdb2.jrfIme, false);
          else
            rcc.setLabelLaF(jpDetailOdb2.jrfIme, true);

          if(nivoOdb.substring(0,2).equals("PO"))
          {
            jpDetailOdb2.jrfSifra.setText(knjOrgStr.getKNJCORG());
            jpDetailOdb2.jrfSifra.forceFocLost();
            rcc.setLabelLaF(jpDetailOdb2.jrfSifra, false);
            rcc.setLabelLaF(jpDetailOdb2.jrfNaziv, false);
            rcc.setLabelLaF(jpDetailOdb2.jbChoose, false);
          }
          else
          {
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                jpDetailOdb2.jrfSifra.requestFocus();
              }
            });

            rcc.setLabelLaF(jpDetailOdb2.jrfSifra, true);
            rcc.setLabelLaF(jpDetailOdb2.jrfNaziv, true);
            rcc.setLabelLaF(jpDetailOdb2.jbChoose, true);
          }
        }
      }
      else
      {
        jpDetailOdb.jrfSifra.setText(kljuc);
        jpDetailOdb.jrfSifra.forceFocLost();
        rcc.setLabelLaF(jpDetailOdb.jrfSifra, false);
        rcc.setLabelLaF(jpDetailOdb.jrfNaziv, false);
        if(uvjet.equals("RA"))
        {
          rcc.setLabelLaF(jpDetailOdb.jrfIme, false);
        }
        rcc.setLabelLaF(jpDetailOdb.jtfRBR, false);
        jpDetail.jp2.jraRBROdb.requestFocus();
      }
    } else if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jp2.jraRBROdb, false);
      jpDetail.jp2.jraPnb1.requestFocus();
    }
  }

  public boolean notUnique() {

   if( plUtil.getPlUtil().checkGlobalOdbiciUnique(getDetailSet()))
   {
     JOptionPane.showConfirmDialog(this.jpDetail,"Zapis postoji !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
     jpDetail.jp2.jraRBROdb.requestFocus();
     return true;

   }
   return false;
  }

  public boolean ValidacijaDetail(char mode) {
    if(nivoOdb.length()>2)
    {
      getDetailSet().setString("CKEY", jpDetailOdb2.jrfSifra.getText());
      getDetailSet().setString("CKEY2", jpDetailOdb2.jrfSifra2.getText());
    }
    else
    {
      getDetailSet().setString("CKEY", jpDetailOdb.jrfSifra.getText());
      getDetailSet().setString("CKEY2", "");
    }
    if (mode == 'N' && notUnique())
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    mojDS = plUtil.getPlUtil().getGlobalVrOdbDS(uvjet);
    setMasterSet(mojDS);
    setNaslovMaster("Vrste odbitaka");
    setVisibleColsMaster(new int[] {0, 1, 2});
    setMasterKey(key);
    jpMaster = new jpGlobalMasterMaster(this);
    setJPanelMaster(jpMaster);

    setDetailSet(dm.getOdbici());
    setNaslovDetail("Stavke Odbici");
    setVisibleColsDetail(new int[] {3, 0, 7, 6});
    setDetailKey(key);

    if(getDetailSet().isOpen())
      getDetailSet().close();
    getDetailSet().setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    raDetail.getJpTableView().addTableModifier(
      new raTableColumnModifier("CVRODB", new String[]{"CVRODB", "OPISVRODB"}, dm.getVrsteodb())
    );

    raDetail.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_ESCAPE) {
        public void keyAction() {
          ESCPressed();
        }
    });

    raDetail.addOption(rnvAuto,3);
    raMaster.addOption(rnvAutoM,4);

    jpDetail = new jpOdbici(raDetail);
    this.setJPanelDetail(jpDetail);
  }

  public String getSQLFilterQuery() {
    return plUtil.getPlUtil().refilterOdbici(getMasterSet().getShort("CVRODB"), kljuc, parOpis);
  }

  public void beforeShowDetail()
  {
    this.constructChild();
  }

  void setKeys(char mod)
  {
    if(nivoOdb.length()>2)
    {
      jpDetailOdb2.jrfSifra.setText(getDetailSet().getString("CKEY"));
      jpDetailOdb2.jrfSifra.forceFocLost();
      jpDetailOdb2.jrfSifra2.setText(getDetailSet().getString("CKEY2"));
      jpDetailOdb2.jrfSifra2.forceFocLost();
      rcc.setLabelLaF(jpDetailOdb2.jrfNaziv, false);
      rcc.setLabelLaF(jpDetailOdb2.jrfNaziv2, false);
      rcc.setLabelLaF(jpDetailOdb2.jrfSifra, false);
      rcc.setLabelLaF(jpDetailOdb2.jrfSifra2, false);
      rcc.setLabelLaF(jpDetailOdb2.jtfRBR, false);
      if(!jpDetail.jp2.jraPnb1.isEnabled())
        raDetail.requestFocus();
    }
    else
    {
      jpDetailOdb.jrfSifra.setText(getDetailSet().getString("CKEY"));
      jpDetailOdb.jrfSifra.forceFocLost();
      rcc.setLabelLaF(jpDetailOdb.jrfSifra, false);
      rcc.setLabelLaF(jpDetailOdb.jrfNaziv, false);
      rcc.setLabelLaF(jpDetailOdb.jtfRBR, false);
      if(!jpDetail.jp2.jraPnb1.isEnabled())
        raDetail.requestFocus();

    }
  }

  public boolean DeleteCheckDetail()
  {
    if(this.getDetailSet().getString("CKEY").equals("$DEF") ||
       this.getDetailSet().getString("CKEY2").equals("$DEF"))
    {
      JOptionPane.showConfirmDialog(this,"Standardni odbitak ! Brisanje nije mogu\u0107e !",
                                      "Greška !",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    RowPointer = getDetailSet().getRow();
    return true;
  }

  void setHash()
  {
    ht.put("RA", "Radnici");
    ht.put("OP", "Op\u0107ine");
    ht.put("OJ", "Org. jedinice");
    ht.put("PO", "Poduze\u0107e");
    ht.put("VR", "Vrsta radnog odnosa");
    ht.put("ZA", "Primanja");
  }

  void ESCPressed()
  {
    if(nivoOdb.length()>2  && mod=='N')
    {
      if(nivoOdb.substring(2,4).equals(uvjet) && !jpDetailOdb2.jrfSifra.isEnabled())
      {
        if(nivoOdb.substring(0,2).equals("PO"))
          raDetail.getOKpanel().jPrekid_actionPerformed();
        if(nivoOdb.substring(0,2).equals("RA"))
          rcc.setLabelLaF(jpDetailOdb2.jrfIme, true);
        rcc.setLabelLaF(jpDetailOdb2.jrfSifra, true);
        rcc.setLabelLaF(jpDetailOdb2.jrfNaziv, true);
        rcc.setLabelLaF(jpDetailOdb2.jbChoose, true);
        jpDetailOdb2.jrfSifra.setText("");
        jpDetailOdb2.jrfSifra.forceFocLost();
        jpDetailOdb2.jrfSifra.requestFocus();
      }
      else if(nivoOdb.substring(2,4).equals(uvjet) && jpDetailOdb2.jrfSifra.isEnabled())
        raDetail.getOKpanel().jPrekid_actionPerformed();

      if(nivoOdb.substring(0,2).equals(uvjet) && !jpDetailOdb2.jrfSifra2.isEnabled())
      {
        if(nivoOdb.substring(2,4).equals("PO"))
          raDetail.getOKpanel().jPrekid_actionPerformed();
        if(nivoOdb.substring(2,4).equals("RA"))
          rcc.setLabelLaF(jpDetailOdb2.jrfIme, true);
        rcc.setLabelLaF(jpDetailOdb2.jrfSifra2, true);
        rcc.setLabelLaF(jpDetailOdb2.jrfNaziv2, true);
        rcc.setLabelLaF(jpDetailOdb2.jbChoose2, true);
        jpDetailOdb2.jrfSifra2.setText("");
        jpDetailOdb2.jrfSifra2.forceFocLost();
        jpDetailOdb2.jrfSifra2.requestFocus();
      }
      else if(nivoOdb.substring(0,2).equals(uvjet) && jpDetailOdb2.jrfSifra2.isEnabled())
        raDetail.getOKpanel().jPrekid_actionPerformed();
    }
    else
      raDetail.getOKpanel().jPrekid_actionPerformed();
  }

  public void beforeShowMaster()
  {
    int x= (raMaster.getToolkit().getDefaultToolkit().getScreenSize().width)-605;
    int y= (raMaster.getToolkit().getDefaultToolkit().getScreenSize().height)-295;
    raMaster.setLocation((int)x/2,(int)y/2);
    raMaster.setTitle("Vrste odbitaka " + plUtil.getPlUtil().getMasterTitle(uvjet, kljuc));
    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[1],false);
    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[2],false);
    raMaster.disableAdd();
  }

   public boolean ValidacijaPrijeIzlazaMaster(){
      parent.setEnabled(true);
      return true;
   }

   void automatika_action()
   {
     raAutomatikaPL.autoAddOdbici(getDetailSet(), uvjet);
   }

   void automatikaM_action()
   {
     raAutomatikaPL.autoAddOdbici(this, uvjet);
   }

   public void refilterDetailSet()
   {
     super.refilterDetailSet();
     this.constructChild();
   }

   private void constructChild()
   {
     raDetail.setTitle("Odbici za vr. odbitka " + getMasterSet().getString("OPISVRODB").toLowerCase());
    if(jpDetailOdb != null )
      jpDetail.remove(jpDetailOdb);
    if(jpDetailOdb2 != null)
      jpDetail.remove(jpDetailOdb2);
    if(plUtil.getPlUtil().getOznValEnable(getMasterSet().getShort("CVRODB")))
    {
      jpDetail.jp2.jraOznVal.setVisible(true);
      jpDetail.jp2.jlOznVal.setVisible(true);
      jpDetail.jp2.jbOznVal.setVisible(true);
    }
    else
    {
      jpDetail.jp2.jraOznVal.setVisible(false);
      jpDetail.jp2.jlOznVal.setVisible(false);
      jpDetail.jp2.jbOznVal.setVisible(false);
    }
    osnovica = plUtil.getPlUtil().getOsnovica(getMasterSet().getShort("CVRODB"));
    enabDisabFields();
    nivoOdb = getMasterSet().getString("NIVOODB");

    if(nivoOdb.length()>2)
    {
      if(nivoOdb.substring(0,2).equals("RA") || nivoOdb.substring(2,4).equals("RA"))
          raDetail.setSize(625,375);
        else
          raDetail.setSize(625,355);
      jpDetailOdb2 = new jpChooseOdb2(raDetail, nivoOdb, jpDetail, true);
      jpDetail.add(jpDetailOdb2, BorderLayout.NORTH);
    }
    else
    {
      raDetail.setSize(625,335);
      jpDetailOdb = new jpChooseOdb(raDetail, nivoOdb, jpDetail);
      jpDetail.add(jpDetailOdb, BorderLayout.CENTER);
    }
   }

   public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent e)
   {
     enabDisabFields();
   }

   private void enabDisabFields()
   {
     if( osnovica.equals("0"))
     {
       jpDetail.jp2.jlIznos.setVisible(true);
       jpDetail.jp2.jraIznos.setVisible(true);
       jpDetail.jp2.jlStopa.setVisible(false);
       jpDetail.jp2.jraStopa.setVisible(false);
       jpDetail.jp2.jraDatpoc.setVisible(true);
       jpDetail.jp2.jraDatzav.setVisible(true);
       jpDetail.jp2.jraGlavnica.setVisible(true);
       jpDetail.jp2.jraRata.setVisible(true);
       jpDetail.jp2.jraSaldo.setVisible(true);
       jpDetail.jp2.jlDatpoc.setVisible(true);
       jpDetail.jp2.jlDatzav.setVisible(true);
       jpDetail.jp2.jlGlavnica.setVisible(true);
       jpDetail.jp2.jlRata.setVisible(true);
       jpDetail.jp2.jlSaldo.setVisible(true);
       jpDetail.jp2.setSize(605, 140);
     }
     else
     {
       jpDetail.jp2.jlIznos.setVisible(false);
       jpDetail.jp2.jraIznos.setVisible(false);
       jpDetail.jp2.jlStopa.setVisible(true);
       jpDetail.jp2.jraStopa.setVisible(true);
       jpDetail.jp2.jraDatpoc.setVisible(false);
       jpDetail.jp2.jraDatzav.setVisible(false);
       jpDetail.jp2.jraGlavnica.setVisible(false);
       jpDetail.jp2.jraRata.setVisible(false);
       jpDetail.jp2.jraSaldo.setVisible(false);
       jpDetail.jp2.jlDatpoc.setVisible(false);
       jpDetail.jp2.jlDatzav.setVisible(false);
       jpDetail.jp2.jlGlavnica.setVisible(false);
       jpDetail.jp2.jlRata.setVisible(false);
       jpDetail.jp2.jlSaldo.setVisible(false);
       jpDetail.jp2.setSize(605, 60);
     }
   }
}
