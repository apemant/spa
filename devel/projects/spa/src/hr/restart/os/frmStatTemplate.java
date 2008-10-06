/****license*****************************************************************
**   file: frmStatTemplate.java
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
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raTwoTableChooser;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
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

public class frmStatTemplate extends JraDialog {
 TableDataSet tDS = new TableDataSet();
 TableDataSet tDS2 = new TableDataSet();
 TableDataSet tdsDate = new TableDataSet();
 QueryDataSet qds = new QueryDataSet();
 dM dm = dM.getDataModule();
 Valid vl;
 boolean predselekcija = false;
 String stariStatus = "";
// raCommonClass rcc = raCommonClass.getraCommonClass();

 Column column1 = new Column();
 Column column2 = new Column();
 Column column3 = new Column();
 Column column4 = new Column();
 Column column5 = new Column();
 Column column6 = new Column();
 Column column7 = new Column();
 Column column8 = new Column();
 Column column9 = new Column();
 Column column10 = new Column();

 Column date = new Column();

 XYLayout xYLayout1 = new XYLayout();
 JraTextField jtfDatum = new JraTextField();
 JLabel jlDatum = new JLabel();

 JPanel jPanel1 = new JPanel();
 JPanel jPanelDate = new JPanel();

 PreSelect pres = new PreSelect(){
   public void SetFokus()
   {
     jrfCOrg.setText(hr.restart.zapod.OrgStr.getKNJCORG());
     jrfCOrg.forceFocLost();
     jrfCOrg.requestFocus();
   }

   public boolean Validacija()
   {
     if(jrfCOrg.getText().equals(""))
     {
       jrfCOrg.requestFocus();
       return false;
     }
     predselekcija = true;
     return true;
   }
 };
 JPanel jpSel = new JPanel();

 private BorderLayout borderLayout1 = new BorderLayout();

  public frmStatTemplate() {
    try {
      jbInit();
    }
    catch (Exception ex) {

    }
  }

  raTwoTableChooser ttC = new raTwoTableChooser(this){
    public void saveChoose()
    {
      savePress();
    }

    public void actionLtoR()
    {
      super.actionLtoR();
      moveRight();
    }

    public void actionRtoL()
    {
      super.actionRtoL();
      moveLeft();
    }

    public void actionLtoR_all()
    {
      super.actionLtoR_all();
      moveAllRight();
    }

    public void actionRtoL_all()
    {
      super.actionRtoL_all();
      moveAllLeft();
    }
  };
  XYLayout xYLayout2 = new XYLayout();
  JlrNavField jrfCOrg = new JlrNavField();
  JlrNavField jrfNazCorg = new JlrNavField();
  JraButton jbCOrg = new JraButton();

  JLabel jlCorg = new JLabel();
  JLabel jlSifra = new JLabel();
  JLabel jlNaziv = new JLabel();


  void jbInit() throws Exception
  {
    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-635;
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-265;

    this.setLocation((int)x/2,(int)y/2);

    // binding
    jrfCOrg.setVisCols(new int[]{0,1});
    jrfCOrg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCOrg.setColNames(new String[] {"NAZIV"});
    jrfCOrg.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazCorg});
    jrfCOrg.setColumnName("CORG");
    jrfCOrg.setSearchMode(0);
    jrfCOrg.setNavButton(jbCOrg);
    jrfNazCorg.setNavProperties(jrfCOrg);
    jrfNazCorg.setColumnName("NAZIV");




    // Lijeva tablica
    column1.setCaption("Org. Jedinica");
    column1.setColumnName("CORG");
    column1.setDataType(com.borland.dx.dataset.Variant.STRING);
    column1.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column1.setServerColumnName("NewColumn1");
    column1.setSqlType(0);
    column2.setCaption("Inv. broj");
    column2.setColumnName("INVBROJ");
    column2.setDataType(com.borland.dx.dataset.Variant.STRING);
    column2.setServerColumnName("NewColumn1");
    column2.setSqlType(0);
    column3.setCaption("Aktivna OS");
    column3.setColumnName("NAZSREDSTVA");
    column3.setDataType(com.borland.dx.dataset.Variant.STRING);
    column3.setServerColumnName("NewColumn2");
    column3.setSqlType(0);
    column4.setCaption("Status");
    column4.setColumnName("STATUS");
    column4.setDataType(com.borland.dx.dataset.Variant.STRING);
    column4.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column4.setServerColumnName("NewColumn1");
    column4.setSqlType(0);

    // Desna tablica
    column6.setCaption("Org. Jedinica");
    column6.setColumnName("CORG");
    column6.setDataType(com.borland.dx.dataset.Variant.STRING);
    column6.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column6.setServerColumnName("NewColumn1");
    column6.setSqlType(0);
    column7.setCaption("Inv. broj");
    column7.setColumnName("INVBROJ");
    column7.setDataType(com.borland.dx.dataset.Variant.STRING);
    column7.setServerColumnName("NewColumn1");
    column7.setSqlType(0);
    column8.setCaption("Aktivna OS");
    column8.setColumnName("NAZSREDSTVA");
    column8.setDataType(com.borland.dx.dataset.Variant.STRING);
    column8.setServerColumnName("NewColumn2");
    column8.setSqlType(0);
    column9.setCaption("Status");
    column9.setColumnName("STATUS");
    column9.setDataType(com.borland.dx.dataset.Variant.STRING);
    column9.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column9.setServerColumnName("NewColumn1");
    column9.setSqlType(0);

    // datum
    jtfDatum.setDataSet(tdsDate);
    jtfDatum.setColumnName("DATAKTIVIRANJA");
    jtfDatum.setHorizontalAlignment(SwingConstants.CENTER);

    date.setCaption("Datum aktiviranja");
    date.setColumnName("DATAKTIVIRANJA");
    date.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    date.setDisplayMask("dd-MM-yyyy");
//    date.setEditMask("dd-MM-yyyy");
    date.setResolvable(false);
    date.setSqlType(0);
    date.setServerColumnName("NewColumn2");

    tdsDate.setColumns(new Column[] {date});

    jPanel1.setLayout(borderLayout1);
    jPanelDate.setLayout(xYLayout1);
    xYLayout1.setWidth(625);
    xYLayout1.setHeight(55);
    jlDatum.setText("Datum aktivacije");
    jpSel.setLayout(xYLayout2);
    xYLayout2.setWidth(525);
    xYLayout2.setHeight(70);
    jlCorg.setText("Org. jedinica");
    jbCOrg.setText("...");
    jlSifra.setText("Šifra");
    jlNaziv.setText("Naziv");
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(ttC, BorderLayout.CENTER);
    jPanel1.add(jPanelDate, BorderLayout.NORTH);
    jPanelDate.add(jtfDatum,     new XYConstraints(150, 15, 100, -1));
    jPanelDate.add(jlDatum,   new XYConstraints(15, 15, -1, -1));
    jpSel.add(jrfCOrg,    new XYConstraints(150, 25, 80, -1));
    jpSel.add(jrfNazCorg,      new XYConstraints(235, 25, 255, -1));
    jpSel.add(jbCOrg,           new XYConstraints(493, 25, 21, 21));
    jpSel.add(jlCorg,  new XYConstraints(15, 25, -1, -1));
    jpSel.add(jlSifra,    new XYConstraints(150, 6, -1, -1));
    jpSel.add(jlNaziv,    new XYConstraints(235, 6, -1, -1));


    pres.setSelDataSet(dm.getOS_Sredstvo());
    pres.setSelPanel(jpSel);
  }

  public void setTDS(QueryDataSet qds)
  {
    qds.open();
    for (int i = 0; i< qds.getRowCount();i++)
    {
      tDS.insertRow(false);
      tDS.setString("CORG", qds.getString("CORG"));
      tDS.setString("INVBROJ", qds.getString("INVBROJ"));
      tDS.setString("NAZSREDSTVA", qds.getString("NAZSRED"));
      tDS.setString("STATUS", qds.getString("STATUS"));
      qds.next();
    }
    if(tDS.getRowCount()==0)
    {
      ttC.rnvLtoR.setEnabled(false);
      ttC.rnvRtoL.setEnabled(false);
      ttC.rnvLtoR_all.setEnabled(false);
      ttC.rnvRtoL_all.setEnabled(false);
    }
    else
    {
      ttC.rnvLtoR.setEnabled(true);
      ttC.rnvRtoL.setEnabled(true);
      ttC.rnvLtoR_all.setEnabled(true);
      ttC.rnvRtoL_all.setEnabled(true);
    }
    ttC.rnvSave.setEnabled(false);
  }

  private void moveLeft()
  {
    if(tDS2.getRowCount()==0)
    {
      ttC.rnvSave.setEnabled(false);
    }
  }

  private void moveRight()
  {
    if(tDS2.getRowCount()>0) ttC.rnvSave.setEnabled(true);
    ttC.requestFocus();
  }

  private void moveAllRight()
  {
    if(tDS2.getRowCount()>0) ttC.rnvSave.setEnabled(true);
  }

  private void moveAllLeft()
  {
    ttC.rnvSave.setEnabled(false);
    ttC.requestFocus();
  }

  void savePress()
  {
    ttC.requestFocus();

    if(tDS2.getRowCount()==0) return;
    tDS2.first();

    while (tDS2.inBounds())
    {
      String statUpdate =  rdOSUtil.getUtil().statusUpdate(tDS2.getString("CORG"),
          tDS2.getString("INVBROJ"), tdsDate.getTimestamp("DATAKTIVIRANJA").toString(), stariStatus);
      dm.getOS_Sredstvo().getDatabase().executeStatement(statUpdate);
      tDS2.next();
    }
    tDS2.emptyAllRows();
    ttC.fireTableDataChanged();
    ttC.rnvSave.setEnabled(false);
  }

  public void prepareTDS(String status, String corg)
  {
    stariStatus = status;
    qds = rdOSUtil.getUtil().getStatusDS(status, corg, tdsDate.getTimestamp("DATAKTIVIRANJA").toString());
    if (!tDS2.isOpen())
      tDS2.setColumns(new Column[] {column6, column7, column8, column9});
    if (!tDS.isOpen())
      tDS.setColumns(new Column[] {column1, column2, column3, column4});

    tDS.open();
    tDS2.open();
    if(tDS.getRowCount()>0) tDS.emptyAllRows();
    if(tDS2.getRowCount()>0) tDS2.emptyAllRows();
    this.setTDS(qds);

    tDS.setTableName("os-TDS");
    tDS2.setTableName("os-TDS");
    ttC.setLeftDataSet(tDS);
    ttC.setRightDataSet(tDS2);
    ttC.initialize();

  }

  void setRaTextFieldFocus(JraTextField rtf)
  {
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        jtfDatum.requestFocus();
      }
    });
  }

  public void show()
  {
    if(!predselekcija)
      pres.showPreselect(this, "Org.jedinice");
    else
      super.show();
  }

  public void hide()
  {
    predselekcija = false;
    super.hide();
  }


}