/****license*****************************************************************
**   file: frmPromjene.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;
import hr.restart.util.raMatPodaci;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.TableDataSet;
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

public class frmPromjene extends raMatPodaci
{
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();

  JLabel jLabel2 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jlGodina = new JLabel();
  Integer god;


  hr.restart.sisfun.frmPassword pass = new hr.restart.sisfun.frmPassword();
  JraButton jbKnjig = new JraButton();
  JraTextField jrfDatProm = new JraTextField();
  JraTextField jrfGod = new JraTextField();
  JlrNavField jrfKnjig = new JlrNavField(){
    public void keyF9Pressed()
    {
      newKeyF9Pressed();
    }
  };
  JlrNavField jrfNazKnjig = new JlrNavField();

  JLabel jLabel11 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JraCheckBox jcbPromjene = new JraCheckBox();
  JraCheckBox jcbAmortizacije = new JraCheckBox();
  JraCheckBox jcbRevalorizacije = new JraCheckBox();
  JPanel jPanel2 = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  TableDataSet tds = new TableDataSet();
  Column column1 = new Column();
  Column godina = new Column();
  Valid vl = Valid.getValid();

  public frmPromjene()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  public void SetFokus(char mode)
  {
    rcc.setLabelLaF(jcbAmortizacije, false);
    rcc.setLabelLaF(jcbRevalorizacije, false);

    if (mode=='N')
    {
      this.jrfNazKnjig.setText("");
      tds.setTimestamp("datum", firstDayOfCurrYear());
      tds.setString("godina", Valid.getValid().findYear());
      jrfKnjig.requestFocus();
    }
    else if (mode =='I')
    {
      tds.setTimestamp("datum", dm.getOS_Kontrola().getTimestamp("DATUM"));
      tds.setString("godina", dm.getOS_Kontrola().getString("GODINA"));
      rcc.setLabelLaF(jrfKnjig, false);
      rcc.setLabelLaF(jrfNazKnjig, false);
      rcc.setLabelLaF(jbKnjig, false);

      if(dm.getOS_Kontrola().getString("PROM").equals("D"))
      {
        jcbPromjene.setSelected(true);
      }
      else
      {
        jcbPromjene.setSelected(false);
      }
      jrfGod.requestFocus();
    }
    else
    {
      tds.setString("godina", dm.getOS_Kontrola().getString("GODINA"));
    }

  }

  public boolean Validacija(char mode)
  {
    if(mode=='N')
    {
      if(vl.isEmpty(jrfKnjig))
        return false;

      if(vl.isEmpty(this.jrfGod))
        return false;

      try
      {
        provjeraGodine();
      }
      catch (Exception ex)
      {
        jrfGod.setBackground(Color.red);
        jrfGod.selectAll();
        jrfGod.requestFocus();
        return false;
      }


      boolean PKexist = rdOSUtil.getUtil().checkPromPK(dm.getOS_Kontrola().getString("CORG"));
      if(!PKexist)
      {
        JOptionPane.showConfirmDialog(this.jPanel1,"Postoji zapis za tu org. jedinicu !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        jrfKnjig.requestFocus();
        return false;
      }
      dm.getOS_Kontrola().setString("AMOR", "N");
      dm.getOS_Kontrola().setString("REVA", "N");
//      dm.getOS_Kontrola().setString("GODINA", tds.getString("godina"));
//      dm.getOS_Kontrola().setTimestamp("DATUM", tds.getTimestamp("datum"));

      god = new Integer(this.getRaQueryDataSet().getString("GODINA"));
      dm.getOS_Kontrola().setTimestamp("DATUM",
                                       hr.restart.util.Util.getUtil().clearTime(hr.restart.robno.Util.getUtil().findFirstDayOfYear(god.intValue())));

      if(jcbPromjene.isSelected())
        dm.getOS_Kontrola().setString("PROM", "D");
      else
        dm.getOS_Kontrola().setString("PROM", "N");
    }
    else
    {

      god = new Integer(this.getRaQueryDataSet().getString("GODINA"));
//      System.out.println("GODIAN " + this.getRaQueryDataSet().getString("GODINA"));
      dm.getOS_Kontrola().setTimestamp("DATUM",
                                       hr.restart.util.Util.getUtil().clearTime(hr.restart.robno.Util.getUtil().findFirstDayOfYear(god.intValue())));


      if(jcbPromjene.isSelected())
        dm.getOS_Kontrola().setString("PROM", "D");
      else
        dm.getOS_Kontrola().setString("PROM", "N");
    }

    return true;
  }

  private void jbInit() throws Exception
  {
    setTitle("Ugovori") ;
    setRaDetailPanel(this.jPanel1);
    setRaQueryDataSet(dm.getOS_Kontrola());
    setVisibleCols(new int[] {0,1, 6});
    jPanel1.setLayout(xYLayout1);
    xYLayout1.setWidth(585);
    xYLayout1.setHeight(150);

    //**** labele
    jLabel2.setText("Knjigovodstvo");
    jLabel4.setText("Datum");
    jlGodina.setText("Godina");
    jLabel11.setText("Šifra");
    jLabel12.setText("Naziv");

//    jPanel1.setPreferredSize(new Dimension(555, 354));

    //**** dodjeljivanje vrijednosti iz baze
    jrfDatProm.setColumnName("DATUM");
    jrfDatProm.setDataSet(dm.getOS_Kontrola());
    jrfGod.setColumnName("GODINA");
    jrfGod.setDataSet(dm.getOS_Kontrola());

    //trazenje recorda u tablici Partneri prema stranom kljucu u tablici Ugovori
    jrfKnjig.setColumnName("CORG");
    jrfKnjig.setDataSet(dm.getOS_Kontrola());
    jrfKnjig.setColNames(new String[] {"NAZIV"});
    jrfKnjig.setVisCols(new int[]{2,3});
    jrfKnjig.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazKnjig});
    jrfKnjig.setRaDataSet(dm.getOrgstruktura());
    jrfNazKnjig.setColumnName("NAZIV");
    jrfNazKnjig.setSearchMode(1);
    jrfNazKnjig.setNavProperties(jrfKnjig);

    //**** button
    jbKnjig.setText("Ugovori - Partneri");
    jbKnjig.setToolTipText(jbKnjig.getText());
    jbKnjig.setText("...");

    //**** action LIsteners
    jbKnjig.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbKnjig_actionPerformed(e);
      }
    });

    //**** adding components
    jcbPromjene.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbPromjene.setText("Promjene");
    jcbPromjene.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbPromjene_actionPerformed(e);
      }
    });
    jcbAmortizacije.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAmortizacije.setText("Amortizacije");
    jcbRevalorizacije.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbRevalorizacije.setText("Revalorizacije");
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    jPanel2.setLayout(xYLayout2);
    jrfGod.setHorizontalAlignment(SwingConstants.CENTER);
    jPanel1.add(jrfNazKnjig,       new XYConstraints(255, 25, 285, -1));
    jPanel1.add(jLabel12,    new XYConstraints(255, 5, 79, -1));
    jPanel1.add(jLabel11,    new XYConstraints(150, 5, 62, -1));
    jPanel1.add(jLabel2,     new XYConstraints(15, 25, -1, -1));
//    jPanel1.add(jLabel4,      new XYConstraints(15, 50, -1, -1));
    jPanel1.add(jlGodina,      new XYConstraints(15, 50, -1, -1));
    jPanel1.add(jbKnjig,        new XYConstraints(543, 25, 22, 22));
    jPanel1.add(jPanel2,       new XYConstraints(150, 85, 415, 53));
    jPanel2.add(jcbAmortizacije, new XYConstraints(136, 14, -1, -1));
    jPanel2.add(jcbPromjene, new XYConstraints(15, 14, -1, -1));
    jPanel2.add(jcbRevalorizacije, new XYConstraints(269, 12, -1, -1));
//    jPanel1.add(jrfDatProm,  new XYConstraints(150, 50, 100, 22));
    jPanel1.add(jrfGod,  new XYConstraints(150, 50, 100, 22));
    jPanel1.add(jrfKnjig,  new XYConstraints(150, 25, 100, -1));

    //***** binding
    column1.setCaption("datum");
    column1.setColumnName("datum");
    column1.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    column1.setDisplayMask("dd-MM-yyyy");
//    column1.setEditMask("dd-MM-yyyy");
    column1.setResolvable(false);
    column1.setSqlType(0);
    column1.setServerColumnName("NewColumn1");

    godina.setCaption("godina");
    godina.setColumnName("godina");
    godina.setDataType(com.borland.dx.dataset.Variant.STRING);
//    godina.setDisplayMask("dd-MM-yyyy");
//    godina.setEditMask("dd-MM-yyyy");
//    godina.setResolvable(false);
//    godina.setSqlType(1);
//    godina.setServerColumnName("NewColumn1");

    jrfDatProm.setColumnName("datum");
    jrfDatProm.setDataSet(tds);
    jrfDatProm.setHorizontalAlignment(SwingConstants.CENTER);

//    jrfGod.setColumnName("godina");
//    jrfGod.setDataSet(tds);
//    jrfGod.setHorizontalAlignment(SwingConstants.CENTER);

    jrfGod.setColumnName("GODINA");
    jrfGod.setDataSet(dm.getOS_Kontrola());
    getJpTableView().addTableModifier(
      new raTableColumnModifier("CORG", new String[]{"CORG", "NAZIV"}, dm.getOrgstruktura())
    );

    tds.setColumns(new Column[] {column1, godina});
    if(!tds.isOpen())
      tds.open();

  }

  public Timestamp firstDayOfCurrYear()
  {
    String currYear = hr.restart.util.Valid.getValid().findYear().toString();
    int intCurrYear = new Integer(currYear).intValue();
    String newyear = intCurrYear + "-01-01 0:00:00.0";
    Timestamp ts = Timestamp.valueOf(newyear);
    return ts;
  }


  void jbKnjig_actionPerformed(ActionEvent e)
  {
    newKeyF9Pressed();
  }

  void newKeyF9Pressed()
  {
    Valid vl = hr.restart.util.Valid.getValid();
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    Frame frame = null;
    Dialog dialog = null;
    lookupFrame lookUp = null;

    String[] result;

    String qStr = rdOSUtil.getUtil().getOrgStr();
    vl.execSQL(qStr);
    vl.RezSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    vl.RezSet.setColumns(new Column[]
    {
        (Column) dm.getOrgstruktura().getColumn("CORG").clone(),
        (Column) dm.getOrgstruktura().getColumn("NAZIV").clone(),
        (Column) dm.getOrgstruktura().getColumn("MJESTO").clone(),
        (Column) dm.getOrgstruktura().getColumn("ADRESA").clone(),
        (Column) dm.getOrgstruktura().getColumn("PRIPADNOST").clone(),
        (Column) dm.getOrgstruktura().getColumn("ZIRO").clone(),
        (Column) dm.getOrgstruktura().getColumn("HPBROJ").clone(),
        (Column) dm.getOrgstruktura().getColumn("NALOG").clone(),
        (Column) dm.getOrgstruktura().getColumn("AKTIV").clone(),
        (Column) dm.getOrgstruktura().getColumn("LOKK").clone()
    });

    vl.RezSet.open();
    vl.RezSet.setTableName("STANJAZADOHVATSAF9");
    lookupData LD = lookupData.getlookupData();
    result = LD.lookUp((java.awt.Frame)this.getFrameOwner(), vl.RezSet, new int[] {0, 1});

    try
    {
      this.jrfKnjig.setText(result[0]);
      jrfKnjig.forceFocLost();
      jrfGod.requestFocus();
    }
    catch (Exception ex)
    {
      jrfKnjig.requestFocus();
    }
  }

  void jcbPromjene_actionPerformed(ActionEvent e) {
    if(jcbPromjene.isSelected()==false)
    {
      if (pass.askLogin())
      {
        if(hr.restart.sisfun.raUser.getInstance().getUser().equals("root"))
          jcbPromjene.setSelected(false);
        else
          jcbPromjene.setSelected(true);
      }
      else
          jcbPromjene.setSelected(true);
    }
  }

  private void  provjeraGodine() throws Exception
 {
    try
    {
      Integer provjera = new Integer(this.jrfGod.getText());

    }
    catch (Exception ex)
    {
      throw new Exception();
    }
    int intOK = jrfGod.getText().length();

    if (intOK !=4)
    {
      throw new Exception();
    }
  }
}