/****license*****************************************************************
**   file: dlgSerBrojevi.java
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
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raImages;
import hr.restart.util.raTwoTableChooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class dlgSerBrojevi extends JraDialog {

  private boolean TransactionActive = false;
  private boolean orgTransactionActive = false;
  public void setTransactionActive(boolean TransactionActive){
    this.TransactionActive = TransactionActive;
  }

  public boolean isTransactionActive(){
    return TransactionActive;
  }

  public void returnOrgTransactionActive(){
    TransactionActive = orgTransactionActive ;
  }

  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  static dlgSerBrojevi dSerBrojevi;
  int znamenki=0;
  char cMode;
  char cModeIsp;
  DataRow old;

  int delBrdok = 0;
  String delVrdok = "";
  String delCskl = "";
  String delGod = "";
  String delMCsklul = "";
  String delMCskliz = "";
  int MCart=0;
  int rb=0;
  int ser1;
  int ser2;
  boolean okUnos;
  boolean isAdd = false;
  boolean bAutGen = false;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.PreSelect pres = new hr.restart.util.PreSelect();
  hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  private com.borland.dx.sql.dataset.QueryDataSet qds;


  QueryDataSet myDS = new QueryDataSet();
  dM dm;

  JraTextField jtffCART = new JraTextField();
  JraTextField jtffCSKLUL = new JraTextField();
  JraTextField jtffVRDOKUL = new JraTextField();
  JraTextField jtffGODUL = new JraTextField();
  JraTextField jtffBRDOKUL = new JraTextField();

  JPanel jPanel1 = new JPanel();
  JPanel jpFilter = new JPanel();
  JPanel jPanel2 = new JPanel();
  JPanel jPanel3 = new JPanel();

  raTwoTableChooser ttC = new raTwoTableChooser(this){

    public void saveChoose()
    {
      if (tdsb2.getRowCount() ==qds.getBigDecimal("KOL").intValue()) {
        savePress();
        if(!okUnos)
        {
          rcc.EnabDisabAll(jPanel2, true);
          tds.setInt("broj", 0);
          jtfPocBroj.requestFocus();
        }
      }
      else {
        javax.swing.JOptionPane.showMessageDialog(this,
         "Koli\u010Dina odabranih serijskih brojeva nije identicna traženoj kolicini !",
                "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      }
    }

    public void actionRtoL()
    {
//      moveLeft();
//      super.actionRtoL();
      super.actionRtoL();
//      tomoMove();
    }

    public void actionLtoR()
    {
//      moveRight();
      super.actionLtoR();
//      tomoMove();
    }

    public void actionRtoL_all()
    {
//      moveAll();
      super.actionRtoL_all();
//      tomoMove();
    }
  };

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
  Column column11 = new Column();
  Column column12 = new Column();
  Column column13 = new Column();
  Column column14 = new Column();
  Column column15 = new Column();
  Column column16 = new Column();
  Column column17 = new Column();

  XYLayout xYLayout2 = new XYLayout();
  XYLayout xYLayout3 = new XYLayout();
  XYLayout xYLayout1 = new XYLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();

  JLabel jLabel5 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();

  JraTextField jtfBroj = new JraTextField();
  JraTextField jtfNAZART = new JraTextField();
  JraTextField jtfKOL = new JraTextField();
  JraTextField jtfCART = new JraTextField();
  JraTextField jtfZavBroj = new JraTextField();
  JraTextField jtfPocBroj = new JraTextField();
  JraTextField jtfPrefix = new JraTextField();
  JraTextField jtfSufix = new JraTextField();
  JraTextField jtfKolicina = new JraTextField();

  JraButton jbCopy = new JraButton();
  JraButton jbDelete = new JraButton();
  JraButton jbMoveAll = new JraButton();
  JraButton jbMoveRight = new JraButton();
  JraButton jbMoveLeft = new JraButton();

  TableDataSet tds = new TableDataSet();
  TableDataSet tdsb = new TableDataSet();
  TableDataSet tdsb2 = new TableDataSet();

  JraCheckBox jdbCheckBox1 = new JraCheckBox();
  OKpanel okp = new OKpanel()
  {
    public void jBOK_actionPerformed()
    {
      pressOK();
    }
    public void jPrekid_actionPerformed()
    {
      pressCancel();
    }
  };
  JPanel jpAutGen = new JPanel();
  XYLayout xYLayout4 = new XYLayout();
  JTextField jtfSerBrPoj = new JTextField();
  JLabel jlSerBr = new JLabel();
  JraCheckBox jcbAutGen = new JraCheckBox();

  public dlgSerBrojevi(Frame frame, String title, boolean modal)
  {
    super(frame, title, modal);
    try
    {
      jbInit();
      pack();
      dSerBrojevi=this;
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public dlgSerBrojevi()
  {
    this(null, "", false);
  }
  /**
   * Statichki getter
   */
  public static dlgSerBrojevi getdlgSerBrojevi()
  {
    if (dSerBrojevi == null)
    {
      dSerBrojevi = new dlgSerBrojevi();
    }
    return dSerBrojevi;
  }

  void jbInit() throws Exception
  {
    ttC.rnvSave.setVisible(true);
    dm = hr.restart.baza.dM.getDataModule();
    dm.getSerbr().open();
    setModal(true);
    setTitle("Serijski brojevi");

    jpFilter.setLayout(xYLayout2);
    jPanel1.setLayout(borderLayout1);
    jPanel3.setLayout(borderLayout2);
    jPanel2.setLayout(xYLayout3);

    xYLayout2.setWidth(130);
    xYLayout2.setHeight(160);
    xYLayout3.setWidth(555);
    xYLayout3.setHeight(300);

    column1.setColumnName("pocBroj");
    column1.setDataType(com.borland.dx.dataset.Variant.STRING);
    column1.setServerColumnName("NewColumn1");
    column1.setSqlType(0);
    column2.setColumnName("zavBroj");
    column2.setDataType(com.borland.dx.dataset.Variant.STRING);
    column2.setDefault("");
    column2.setServerColumnName("NewColumn2");
    column2.setSqlType(0);
    column3.setColumnName("broj");
    column3.setDataType(com.borland.dx.dataset.Variant.INT);
    column3.setDisplayMask("###,##0.00");
    column3.setDefault("0");
    column3.setServerColumnName("NewColumn1");
    column3.setSqlType(0);
    column4.setColumnName("prefix");
    column4.setDataType(com.borland.dx.dataset.Variant.STRING);
    column4.setServerColumnName("NewColumn1");
    column4.setSqlType(0);
    column5.setColumnName("sufix");
    column5.setDataType(com.borland.dx.dataset.Variant.STRING);
    column5.setServerColumnName("NewColumn2");
    column5.setSqlType(0);
    column6.setColumnName("kontrolni");
    column6.setDataType(com.borland.dx.dataset.Variant.STRING);
    column6.setDefault("N");
    column6.setServerColumnName("NewColumn1");
    column6.setSqlType(0);
    column7.setColumnName("kol");
    column7.setDataType(com.borland.dx.dataset.Variant.INT);
    column7.setDisplayMask("###,##0");
    column7.setServerColumnName("NewColumn1");
    column7.setSqlType(0);

    // Desna tablica
    column8.setCaption("Serijski broj - dokument");
    column8.setColumnName("SB");
    column8.setDataType(com.borland.dx.dataset.Variant.STRING);
    column8.setServerColumnName("NewColumn1");
    column8.setSqlType(0);
    column9.setColumnName("CSKL");
    column9.setDataType(com.borland.dx.dataset.Variant.STRING);
    column9.setPrecision(6);
    column9.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column9.setServerColumnName("NewColumn1");
    column9.setSqlType(0);
    column10.setColumnName("VRDOK");
    column10.setDataType(com.borland.dx.dataset.Variant.STRING);
    column10.setPrecision(3);
    column10.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column10.setServerColumnName("NewColumn2");
    column10.setSqlType(0);
    column11.setColumnName("BRDOK");
    column11.setDataType(com.borland.dx.dataset.Variant.INT);
    column11.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column11.setServerColumnName("NewColumn1");
    column11.setSqlType(0);
    column12.setColumnName("GOD");
    column12.setDataType(com.borland.dx.dataset.Variant.STRING);
    column12.setPrecision(4);
    column12.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column12.setServerColumnName("NewColumn2");
    column12.setSqlType(0);

    // Lijeva tablica
    column13.setCaption("Serijski broj");
    column13.setColumnName("SB");
    column13.setDataType(com.borland.dx.dataset.Variant.STRING);
    column13.setServerColumnName("NewColumn1");
    column13.setSqlType(0);
    column14.setColumnName("CSKL");
    column14.setDataType(com.borland.dx.dataset.Variant.STRING);
    column14.setPrecision(6);
    column14.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column14.setServerColumnName("NewColumn1");
    column14.setSqlType(0);
    column15.setColumnName("VRDOK");
    column15.setDataType(com.borland.dx.dataset.Variant.STRING);
    column15.setPrecision(3);
    column15.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column15.setServerColumnName("NewColumn2");
    column15.setSqlType(0);
    column16.setColumnName("BRDOK");
    column16.setDataType(com.borland.dx.dataset.Variant.INT);
    column16.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column16.setServerColumnName("NewColumn1");
    column16.setSqlType(0);
    column17.setColumnName("GOD");
    column17.setDataType(com.borland.dx.dataset.Variant.STRING);
    column17.setPrecision(4);
    column17.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    column17.setServerColumnName("NewColumn2");
    column17.setSqlType(0);

    jtffCART.setColumnName("CART");
    jtffCART.setDataSet(dm.getSerbr());
    jtffCSKLUL.setColumnName("CSKL");
    jtffCSKLUL.setDataSet(dm.getSerbr());
    jtffVRDOKUL.setColumnName("VRDOK");
    jtffVRDOKUL.setDataSet(dm.getSerbr());
    jtffGODUL.setColumnName("GOD");
    jtffGODUL.setDataSet(dm.getSerbr());
    jtffBRDOKUL.setColumnName("BRDOK");
    jtffBRDOKUL.setDataSet(dm.getSerbr());

    jLabel5.setText("Zaduženo ser. brojeva");
    jLabel4.setText("Koli\u010Dina na primci");
    jtfNAZART.setColumnName("NAZART");
    jLabel2.setText("Serijski broj od - do");
    jtfKOL.setColumnName("KOL");
    jtfCART.setColumnName("CART");
    jLabel1.setText("Artikl");
    jtfZavBroj.setColumnName("zavBroj");
    jtfZavBroj.setDataSet(tds);
    jtfZavBroj.addKeyListener(new java.awt.event.KeyAdapter()
    {
      public void keyReleased(KeyEvent e)
      {
        jtfZavBroj_keyReleased(e);
      }
    });
    jtfPocBroj.setColumnName("pocBroj");
    jtfPocBroj.setDataSet(tds);
    jtfPocBroj.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfPocBroj_focusLost(e);
      }
    });
    jtfPocBroj.addKeyListener(new java.awt.event.KeyAdapter()
    {
      public void keyReleased(KeyEvent e)
      {
        jtfPocBroj_keyReleased(e);
      }
    });
    this.addKeyListener(new java.awt.event.KeyAdapter()
    {
      public void keyPressed(KeyEvent e)
      {
        this_keyPressed(e);
      }
    });
    jtfBroj.setColumnName("broj");
    jtfBroj.setDataSet(tds);
    jLabel3.setText("Prefix");
    jLabel6.setText("Sufix");
    jdbCheckBox1.setHorizontalAlignment(SwingConstants.RIGHT);
    jdbCheckBox1.setHorizontalTextPosition(SwingConstants.LEFT);
    jdbCheckBox1.setText("Kontrolni broj");
    jdbCheckBox1.setColumnName("kontrolni");
    jdbCheckBox1.setDataSet(tds);
    jdbCheckBox1.setSelectedDataValue("D");
    jdbCheckBox1.setUnselectedDataValue("N");
    jtfPrefix.setColumnName("prefix");
    jtfPrefix.setDataSet(tds);
    jtfSufix.setColumnName("sufix");
    jtfSufix.setDataSet(tds);
    jtfKolicina.setColumnName("kol");
    jtfKolicina.setDataSet(tds);
    jtfKolicina.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jtfKolicina_keyPressed(e);
      }
    });
    jLabel7.setText("Koli\u010Dina");
    tds.setColumns(new Column[] {column1, column2, column3, column4, column5, column6, column7});
    jPanel3.setMaximumSize(new Dimension(640, 230));
    jPanel3.setPreferredSize(new Dimension(555, 230));

    jbDelete.setToolTipText("Prebaci selektirano");
    jbDelete.setIcon(raImages.getImageIcon(raImages.IMGDELETE));
    jbDelete.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbDelete_actionPerformed(e);
      }
    });

    jPanel2.setMinimumSize(new Dimension(640, 195));
    jPanel2.setPreferredSize(new Dimension(640, 195));
    jpAutGen.setLayout(xYLayout4);
    jlSerBr.setText("Serijski broj");
    jcbAutGen.setText("Automatsko kreiranje");
    jcbAutGen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbAutGen_actionPerformed(e);
      }
    });
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel2.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jPanel2.add(jtfCART, new XYConstraints(150, 20, 100, -1));
    jPanel2.add(jtfNAZART,   new XYConstraints(255, 20, 370, -1));
    jPanel2.add(jLabel4,  new XYConstraints(16, 45, -1, -1));
    jPanel2.add(jtfKOL,   new XYConstraints(150, 45, 100, -1));
    jPanel2.add(jLabel5,       new XYConstraints(273, 47, -1, -1));
    jPanel2.add(jtfBroj,  new XYConstraints(525, 47, 100, -1));
    jpAutGen.add(jLabel3,     new XYConstraints(2, 0, -1, -1));
    jpAutGen.add(jtfPrefix,      new XYConstraints(137, 0, 100, -1));
    jpAutGen.add(jLabel6,   new XYConstraints(2, 25, -1, -1));
    jpAutGen.add(jtfSufix,   new XYConstraints(137, 25, 100, -1));
    jpAutGen.add(jLabel2,   new XYConstraints(2, 51, -1, -1));
    jpAutGen.add(jtfPocBroj,   new XYConstraints(137, 51, 100, -1));
    jpAutGen.add(jtfZavBroj,   new XYConstraints(242, 51, 100, -1));
    jpAutGen.add(jLabel7,  new XYConstraints(452, 59, -1, -1));
    jpAutGen.add(jdbCheckBox1,  new XYConstraints(472, 34, 140, -1));
    jpAutGen.add(jtfKolicina,  new XYConstraints(512, 59, 100, -1));
    jPanel2.add(jcbAutGen,     new XYConstraints(255, 72, -1, -1));
    jPanel2.add(jtfSerBrPoj,    new XYConstraints(150, 70, 100, -1));
    jPanel2.add(jlSerBr,   new XYConstraints(15, 72, -1, -1));
    jPanel2.add(jpAutGen,    new XYConstraints(13, 105, 646, 112));
    jPanel1.add(okp, BorderLayout.SOUTH);
    jPanel1.add(jPanel2, BorderLayout.WEST);
    this.getContentPane().add(jPanel3, BorderLayout.NORTH);
    jPanel3.add(this.ttC, BorderLayout.NORTH);

    jpFilter.add(jtffCART);
    jpFilter.add(jtffCSKLUL);
    this.jcbAutGen.setRequestFocusEnabled(false);

  }

  void this_keyPressed(KeyEvent e)
  {
    if (e.getKeyCode()==e.VK_F10)
    {
      pressOK();
    }
    else if (e.getKeyCode()==e.VK_ESCAPE)
    {
      pressCancel();
    }
  }

  void pressOK()  {
//    ttC.rnvSave.setEnabled(true);
    if (tds.getString("pocBroj").length()!=tds.getString("zavBroj").length())
    {
      JOptionPane.showConfirmDialog(this.jPanel1,"Dužina po\u010Detnog i završnog serijskog broja nije jednaka !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (tds.getInt("kol")+tds.getInt("broj")>qds.getBigDecimal("KOL").intValue())
    {
      JOptionPane.showConfirmDialog(this.jPanel1,"Broj upisanih serijskih brojeva ve\u0107i od broja na dokumentu !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }

    if(!bAutGen)
    {
      if(this.jtfSerBrPoj.getText().trim().equals(""))
        return;
      if(!checkPojSerBr(jtfSerBrPoj.getText().trim()))  {
        JOptionPane.showConfirmDialog(this.jPanel1,"Serijski broj postoji !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        jtfSerBrPoj.setText("");
        return;
      }
      tdsb2.last();
      tdsb2.insertRow(false);
      tdsb2.setString("SB", jtfSerBrPoj.getText().trim());
      tds.setInt("broj", tdsb2.getRowCount());
      jtfSerBrPoj.setText("");
    }
    else
    {
      try
      {
        ser1=Integer.parseInt(tds.getString("pocBroj"));
      }
      catch(Exception ex)
      {
        return;
      }
      try
      {
        ser2=Integer.parseInt(tds.getString("zavBroj"));
      }
      catch(Exception ex)
      {
        return;
      }
      if (!checkSerBr(ser1, ser2))
      {
        JOptionPane.showConfirmDialog(this.jPanel1,"Serijski broj postoji !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        this.jtfPocBroj.setText("");
        this.jtfZavBroj.setText("");
        this.jtfPocBroj.requestFocus();
        return;
      }
      for (int i=0; i<ser2+1-ser1; i++)
      {
        tdsb2.last();
        tdsb2.insertRow(false);
        tdsb2.setString("SB", tds.getString("prefix")+vl.maskZeroInteger(new Integer(ser1+i), znamenki)+tds.getString("sufix"));
        tds.setInt("broj", tdsb2.getRowCount());
      }
      if(tdsb2.getRowCount()>0)
      {
        if(tds.getInt("broj")==qds.getBigDecimal("KOL").intValue())
          rcc.setLabelLaF(this.jbCopy, true);
        else
          jtfPocBroj.requestFocus();
      }
    }

    clearGets();
    if (tds.getInt("broj")==qds.getBigDecimal("KOL").intValue())
    {
      rcc.EnabDisabAll(this.jPanel2, false);
      ttC.rnvSave.setEnabled(true);
    }
    ttC.setRightDataSet(tdsb2);

    ttC.initialize();
  }

  boolean checkSerBr(int ser1, int ser2)
  {
    tdsb2.first();
    for(int j=0; j<tdsb2.getRowCount();j++)
    {
      for (int i=0; i<ser2+1-ser1; i++)
      {
        String newSB = tds.getString("prefix")+vl.maskZeroInteger(new Integer(ser1+i), znamenki)+tds.getString("sufix");
        if(tdsb2.getString("SB").equals(newSB))
        {
          return false;
        }
      }
      tdsb2.next();
    }
    return true;
  }

  boolean checkPojSerBr(String serBr)
  {
    tdsb2.first();
    for(int j=0; j<tdsb2.getRowCount();j++)
    {
      if(serBr.equals(tdsb2.getString("SB")))
        return false;
      tdsb2.next();
    }
    return true;
  }

  void pressCancel()
  {
    okUnos=false;
    this.hide();
  }
/**
 * Gledaj 'vamo
 * - tQds - dataSet u kojem se nalaze podaci o artiklu
 * - mod - mod rada (U-ulaz, I-Izlaz)
 * - mode - mod rada sa Validacije ('N' - novi, 'I' - ispravak, 'B' - browse)
 */

  boolean findSB(hr.restart.robno.rapancart rpc, com.borland.dx.sql.dataset.QueryDataSet tQds, char mod, char mode)
  {
    if (!tdsb2.isOpen())
      tdsb2.setColumns(new Column[] {column8, column9, column10, column11, column12});
    if (!tdsb.isOpen())
      tdsb.setColumns(new Column[] {column13, column14, column15, column16, column17});

    if (rpc.getISB().trim().equals("D"))
    {
      tdsb.open();
      tdsb2.open();
      tds.open();
      cMode=mod;
      cModeIsp = mode;

      tdsb.deleteAllRows();
      tdsb.post();
      tdsb2.deleteAllRows();
      tdsb2.post();
      if (tds.rowCount()==0)
      {
        tds.insertRow(true);
      }

      clearGets();
      if (mod=='U')
      {
        ttC.rnvRtoL.setEnabled(false);
        ttC.rnvLtoR.setEnabled(false);
        ttC.rnvRtoL_all.setEnabled(false);
        ttC.rnvSave.setEnabled(false);

        rcc.EnabDisabAll(this.jPanel1, true);
        if (mode == 'N')
        {
          myDS = rdUtil.getUtil().getSbUlDataSet();
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          ttC.setRightDataSet(tdsb2);

           if(tdsb2.getRowCount()>0)
           {
            ttC.rnvSave.setEnabled(true);
           }
           tds.setInt("broj", tdsb2.getRowCount());
        }
        else if (mode == 'I')
        {
          delBrdok=tQds.getInt("BRDOK");
          delVrdok=tQds.getString("VRDOK");
          delGod=tQds.getString("GOD");
          delCskl=tQds.getString("CSKL");

          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(true);
          ttC.rnvSave.setEnabled(true);

          rcc.EnabDisabAll(this.jPanel2, false);
          myDS = rdUtil.getUtil().getSbUlR(tQds.getInt("BRDOK"));
          setTdsb2(myDS);
          ttC.setRightDataSet(tdsb2);

          myDS.emptyAllRows();

          myDS = rdUtil.getUtil().getSbUlL(tQds.getInt("BRDOK"));
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);

          if(tdsb2.getRowCount()>0)
           {
              ttC.rnvSave.setEnabled(true);
           }
           tds.setInt("broj", tdsb2.getRowCount());
        }
      }
      else if (mod=='I')
      {
        delBrdok=tQds.getInt("BRDOK");
        delVrdok=tQds.getString("VRDOK");
        delGod=tQds.getString("GOD");
        delCskl=tQds.getString("CSKL");

        if (mode == 'N')
        {
/*
          ttC.rnvLtoR.setEnabled(true);
          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(false);
          ttC.rnvSave.setEnabled(false);
*/
          rcc.EnabDisabAll(this.jPanel1, false);
          myDS = rdUtil.getUtil().getSbIzDataSet(delCskl, tQds.getInt("CART"));
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          ttC.setRightDataSet(tdsb2);
          tds.setInt("broj", tdsb2.getRowCount());
        }
        else if (mode == 'I' )
        {
/*
          ttC.rnvLtoR.setEnabled(false);
          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(true);
          ttC.rnvSave.setEnabled(false);
*/
          rcc.EnabDisabAll(this.jPanel1, false);

          myDS = rdUtil.getUtil().getSbIzR(tQds.getInt("BRDOK"), delCskl, tQds.getShort("RBR"));
          setTdsb2(myDS);
          ttC.setRightDataSet(tdsb2);

//          myDS = rdUtil.getUtil().getSbIzL(tQds.getInt("BRDOK"), delCskl, tQds.getShort("RBR"));
          myDS = rdUtil.getUtil().getSbIzDataSet(delCskl, tQds.getInt("CART"));
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          tds.setInt("broj", tdsb2.getRowCount());
        }
      }
      else if (mod == 'M')
      {
        delBrdok=tQds.getInt("BRDOK");
        delVrdok=tQds.getString("VRDOK");
        delGod=tQds.getString("GOD");
        delMCsklul=tQds.getString("CSKLUL");
        delMCskliz = tQds.getString("CSKLIZ");
        MCart = tQds.getInt("CART");
        rb=tQds.getShort("RBR");

        if(mode=='N')
        {
          ttC.rnvLtoR.setEnabled(true);
          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(false);
          ttC.rnvSave.setEnabled(false);
          rcc.EnabDisabAll(this.jPanel1, false);

          myDS = rdUtil.getUtil().getSbIzDataSet(delMCskliz, rb);
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          ttC.setRightDataSet(tdsb2);
          tds.setInt("broj", tdsb2.getRowCount());
        }
        else if(mode=='I')
        {
          ttC.rnvLtoR.setEnabled(false);
          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(true);
          ttC.rnvSave.setEnabled(false);

          rcc.EnabDisabAll(this.jPanel1, false);

          myDS = rdUtil.getUtil().getSbIzR(tQds.getInt("BRDOK"), delMCskliz, rb);
          setTdsb2(myDS);
          ttC.setRightDataSet(tdsb2);

          myDS = rdUtil.getUtil().getSbIzL(tQds.getInt("BRDOK"), delMCskliz, rb);
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          tds.setInt("broj", tdsb2.getRowCount());
        }
      }
      this.setQds(tQds);
      if (mode=='I')
      {
        makePRES('D');
      }
        makePRES('S');

/*
      if(tds.getInt("broj")==0)
        ttC.rnvRtoL.setEnabled(false);
      ttC.rnvLtoR_all.setEnabled(false);
*/

        tdsb.setTableName("serbr");
        tdsb2.setTableName("serbr");

      ttC.initialize();
//      tomoMove();

      this.setLocation( (int)rpc.getTopLevelAncestor().getBounds().getX() +
                        ((int)rpc.getTopLevelAncestor().getWidth()-(int)this.getWidth())/2,
                        (int)rpc.getTopLevelAncestor().getBounds().getY()+
                        (int)rpc.getTopLevelAncestor().getHeight()-(int)this.getHeight());
      this.pack();
      this.show();
      return okUnos;
    }
    return true;
  }


//// kraj moje

  public void show()
  {
    rcc.setLabelLaF(jtfCART, false);
    rcc.setLabelLaF(jtfNAZART, false);
    rcc.setLabelLaF(jtfKOL, false);
    rcc.setLabelLaF(jtfBroj, false);
    if(!bAutGen)
    {
      rcc.EnabDisabAll(jpAutGen, false);

    }
    else
    {
      rcc.EnabDisabAll(jpAutGen, true);
      rcc.setLabelLaF(this.jtfSerBrPoj, false);
      jtfPrefix.requestFocus();
    }
   super.show();
  }

  void jtfPocBroj_keyReleased(KeyEvent e)
  {
    if (e.getKeyCode()==e.VK_ENTER)
    {
      ser1=findSerBroj(jtfPocBroj.getText(),1);
      if (ser1==0)
      {
        jtfPocBroj.requestFocus();
      }
    }
  }

  void jtfZavBroj_keyReleased(KeyEvent e)
  {
    if (e.getKeyCode()==e.VK_TAB )
    {

      ser2=findSerBroj(jtfZavBroj.getText(),2);
      if (ser2==0)
      {
        jtfZavBroj.requestFocus();
      }
    }
  }

  int findSerBroj(String st, int mod)
  {
    String stCheck=st;
    try
    {
      if (mod==2)
      {
        ser2=Integer.parseInt(st);
        if (znamenki==0)
        {
          znamenki=st.trim().length();
        }
      }
      else
      {
        ser1=Integer.parseInt(st);
        znamenki=st.trim().length();
      }
    }
    catch(Exception ex)
    {
      if (mod==2)
      {
        tds.setInt("kol", 1);
        tds.setString("zavBroj", tds.getString("pocBroj"));
        return ser1;
      }
      tds.setInt("kol", 0);
      tds.setString("zavBroj", "");
      return 0;
    }
    if (mod==2)
    {
      if (ser2<ser1)
      {
        tds.setInt("kol", 1);
        tds.setString("zavBroj", tds.getString("pocBroj"));
        return ser1;
      }
      else
      {
        tds.setInt("kol", ser2-ser1+1);
        jtfZavBroj.setText(vl.maskZeroInteger(new Integer(ser2), znamenki));
        return ser2;
      }
    }
    String checkZnam = qds.getBigDecimal("KOL").intValue()+ser1+"";

    if(checkZnam.length()>znamenki)
    {
      tds.setInt("kol", qds.getBigDecimal("KOL").intValue()-tdsb2.getRowCount());
      double test = java.lang.Math.pow((double)10, (double)znamenki);
      int ZavBr = (int)test-1;
      tds.setInt("broj", (tds.getInt("kol")-ZavBr)*(-1));
      tds.setString("zavBroj", vl.maskZeroInteger(new Integer(ZavBr), znamenki));
    }
    else
    {
      tds.setInt("kol", qds.getBigDecimal("KOL").intValue()-tdsb2.getRowCount());
      tds.setString("zavBroj", vl.maskZeroInteger(new Integer(ser1+tds.getInt("kol")-1), znamenki));
    }
    return ser1;
  }

  void jtfKolicina_keyPressed(KeyEvent e)
  {
    if (e.getKeyCode()==e.VK_ENTER)
    {
      e.consume();
      tds.setInt("kol", Integer.parseInt(jtfKolicina.getText()));
      tds.setString("zavBroj", vl.maskZeroInteger(new Integer(ser1+tds.getInt("kol")-1), znamenki));
    }
  }

  public void setQds(com.borland.dx.sql.dataset.QueryDataSet newQds)
  {
    qds = newQds;
    jtfNAZART.setDataSet(newQds);
    jtfKOL.setDataSet(newQds);
    jtfCART.setDataSet(newQds);
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQds()
  {
    return qds;
  }

  void clearGets()
  {
    tds.setString("pocBroj", "");
    tds.setString("zavBroj", "");
    tds.setString("sufix", "");
    tds.setString("prefix", "");
    tds.setInt("kol", 0);

    if(!bAutGen)
    {
      jtfSerBrPoj.requestFocus();
    }
    else
    {
      jtfPrefix.requestFocus();
      rcc.setLabelLaF(jtfSerBrPoj, false);
    }
  }

  void savePress()
  {
    if (cModeIsp=='I')
    {
      if (cMode=='U')
      {
System.out.println("o\u0111e sam unisao");
        String ulazIspravak = rdUtil.getUtil().ulazIspravak(delBrdok, delVrdok, delGod, delCskl);
        dm.getSerbr().getDatabase().executeStatement(ulazIspravak);
      }
    }

//    if (qds.getBigDecimal("KOL").intValue()==tds.getInt("broj"))
    if (qds.getBigDecimal("KOL").intValue()==tdsb2.getRowCount())
    {
      if (cMode=='I')
      {
        if (JOptionPane.showConfirmDialog(this.jPanel1,"Želite prijenos serijskih brojeva ?","Upit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

          if(cModeIsp=='N')
          {
            dm.getSerbr().enableDataSetEvents(false);
            tdsb2.first();
            do
            {
               if(ld.raLocate(dm.getSerbr(), new com.borland.dx.dataset.DataSet[] {tdsb2, tdsb2},
               new java.lang.String[] {"CSKL","SB"},
               new java.lang.String[] {"CSKL","CSERBR"}))
               {
                  dm.getSerbr().setString("VRDOKIZ", qds.getString("VRDOK"));
                  dm.getSerbr().setString("GODIZ", qds.getString("GOD"));
                  dm.getSerbr().setString("CSKLIZ", qds.getString("CSKL"));
                  dm.getSerbr().setInt("BRDOKIZ", qds.getInt("BRDOK"));
                  dm.getSerbr().setShort("RBRIZ", qds.getShort("RBR"));
                  if (!isTransactionActive()) dm.getSerbr().saveChanges();
               }

              tdsb2.next();
            }while(tdsb2.inBounds());
            dm.getSerbr().enableDataSetEvents(true);
          }
          else if (cModeIsp=='I')
          {
            String izlazIspravak = "";
            izlazIspravak = rdUtil.getUtil().izlazIspravak(delBrdok, delVrdok, delGod, delCskl);
            dm.getSerbr().getDatabase().executeStatement(izlazIspravak);
            tdsb2.first();
            do
            {
              izlazIspravak = rdUtil.getUtil().izlazIspravak(delBrdok, delVrdok, delGod, delCskl, tdsb2.getString("SB"));
              dm.getSerbr().getDatabase().executeStatement(izlazIspravak);
              tdsb2.next();
            }while(tdsb2.inBounds());
            this.hide();
            }
            okUnos=true;
          }
      }
      else if (cMode=='M')
      {
        String mesInsert="";
        String mesUpdate ="";
        tdsb2.first();
        if(cModeIsp=='N')
        {
          do
          {
            mesUpdate = rdUtil.getUtil().mesUpdate(delBrdok, delGod, delMCskliz, delMCsklul, tdsb2.getString("SB"));
            dm.getSerbr().getDatabase().executeStatement(mesUpdate);

            mesInsert = rdUtil.getUtil().mesInsert(MCart,tdsb2.getString("SB"),rb, delMCsklul, delMCskliz, delGod, delBrdok);
            dm.getSerbr().getDatabase().executeStatement(mesInsert);

            tdsb2.next();
          }while(tdsb2.inBounds());
        }
        else if (cModeIsp=='I')
        {
          String mesDel = rdUtil.getUtil().mesDelete(delMCsklul, delMCskliz);
          dm.getSerbr().getDatabase().executeStatement(mesDel);

          mesUpdate = rdUtil.getUtil().mesUpdate2(delBrdok, delGod, delMCskliz, delMCsklul, delVrdok);
          dm.getSerbr().getDatabase().executeStatement(mesUpdate);

          tdsb2.first();
          do
          {
            mesUpdate = rdUtil.getUtil().mesUpdate(delBrdok, delGod, delMCskliz, delMCsklul, tdsb2.getString("SB"));
            dm.getSerbr().getDatabase().executeStatement(mesUpdate);

            mesInsert = rdUtil.getUtil().mesInsert(MCart,tdsb2.getString("SB"),rb, delMCsklul, delMCskliz, delGod, delBrdok);
            dm.getSerbr().getDatabase().executeStatement(mesInsert);

            tdsb2.next();
          }while(tdsb2.inBounds());
        }
      }
      else
      {
        saveULAZ();
      }
    }
    this.hide();
  }

  public void TransactionSave(){


    try {
      hr.restart.util.raTransaction.saveChanges(dm.getSerbr());
      System.out.println("ovo je right");
    }
    catch (Exception ex) {
System.out.println("ovo je kao right");
ex.printStackTrace();
    }
  }

  void jbDelete_actionPerformed(ActionEvent e)
  {
    if(cMode=='U')
    {
      if (tdsb2.rowCount()>0)
      {
        tdsb2.deleteRow();
        tds.setInt("broj", tds.getInt("broj")-1);
        tds.setInt("kol", tds.getInt("broj")*(-1));
        rcc.EnabDisabAll(this.jPanel2, true);
        rcc.setLabelLaF(this.jbCopy, false);
      }
    }
    else if (cMode=='I') {
      if (tdsb2.rowCount()>0)
      {
        tdsb.insertRow(false);
        tdsb.setString("SB", tdsb2.getString("SB"));
        tdsb.setString("CSKL", tdsb2.getString("CSKL"));
        tdsb.setString("VRDOK", tdsb2.getString("VRDOK"));
        tdsb.setString("GOD", tdsb2.getString("GOD"));
        tdsb.setInt("BRDOK", tdsb2.getInt("BRDOK"));
        tdsb.post();

        tdsb2.deleteRow();
        tds.setInt("broj", tds.getInt("broj")-1);
        tds.setInt("kol", tds.getInt("broj")*(-1));

        rcc.setLabelLaF(this.jbCopy, false);
        rcc.setLabelLaF(this.jbMoveRight, true);
        if(tdsb2.getRowCount()==0)
        {
          rcc.setLabelLaF(this.jbMoveLeft, false);
          rcc.setLabelLaF(this.jbMoveAll, false);
        }
      }
    }
  }

/**
 * Mode mozhe biti
 * - 'D' - predselekcija po dokumentu
 * - 'S' - predselekcija po artiklu na stanju
 */
  void makePRES(char mode)  {
   if (mode=='S')
   {
      if (isAdd)
      {
        jpFilter.remove(jtffVRDOKUL);
        jpFilter.remove(jtffGODUL);
        jpFilter.remove(jtffBRDOKUL);
        isAdd=false;
      }
      pres.setSQLFilter(false);
    }
    else
    {
      if (!isAdd)
      {
        jpFilter.add(jtffVRDOKUL);
        jpFilter.add(jtffGODUL);
        jpFilter.add(jtffBRDOKUL);
        isAdd=true;
      }
      pres.setSQLFilter(true);
    }
    dm.getSerbr().removeRowFilterListener(dm.getSerbr().getRowFilterListener());
    pres.setSelDataSet(dm.getSerbr());
    pres.setSelPanel(jpFilter);
    pres.getSelRow().open();
    pres.getSelRow().setInt("CART", qds.getInt("CART"));
    if(cMode=='M')
      pres.getSelRow().setString("CSKL", qds.getString("CSKLUL"));
    else
      pres.getSelRow().setString("CSKL", qds.getString("CSKL"));
    if (mode=='D') {

      pres.getSelRow().setString("VRDOK", qds.getString("VRDOK"));
      pres.getSelRow().setString("GOD", qds.getString("GOD"));
      pres.getSelRow().setInt("BRDOK", qds.getInt("BRDOK"));

    }
    else {
    }
    pres.getSelRow().post();
    pres.doSelect();
  }

  void moveRight()
  {


//    tds.setInt("broj", tds.getInt("broj")+1);
//    if (tds.getInt("broj")==qds.getBigDecimal("KOL").intValue())
    if (tds.getRowCount()   ==qds.getBigDecimal("KOL").intValue())
    {
      ttC.rnvSave.setEnabled(true);
      ttC.rnvLtoR.setEnabled(false);
    }
//    if(tds.getInt("broj")>0)
    if (tds.getRowCount()>0)
    {
      ttC.rnvRtoL.setEnabled(true);
      ttC.rnvRtoL_all.setEnabled(true);
    }
//    ttC.repaint();
  }

  void moveLeft()
  {
    tds.setInt("broj", tds.getInt("broj")-1);

    if(tds.getInt("broj")==0)
    {
      ttC.rnvRtoL.setEnabled(false);
      ttC.rnvRtoL_all.setEnabled(false);
    }

    if (tds.getInt("broj")!=qds.getBigDecimal("KOL").intValue())
    {
      ttC.rnvSave.setEnabled(false);
    }
    ttC.rnvLtoR.setEnabled(true);
//    ttC.repaint();
  }

  void moveAll()
  {
     ttC.rnvRtoL_all.setEnabled(false);
     ttC.rnvRtoL.setEnabled(false);
     ttC.rnvSave.setEnabled(false);
     ttC.rnvLtoR.setEnabled(true);
     tds.setInt("broj", 0);
  }

  void setTdsb2(QueryDataSet qDs)
  {
    qDs.open();
    qDs.enableDataSetEvents(false);
    for (int i = 0; i< qDs.getRowCount();i++)
    {
      tdsb2.insertRow(false);
      tdsb2.setString("SB", qDs.getString("SB"));
      tdsb2.setString("CSKL", qDs.getString("CSKL"));
      tdsb2.setString("VRDOK", qDs.getString("VRDOK"));
      tdsb2.setInt("BRDOK", qDs.getInt("BRDOK"));
      tdsb2.setString("GOD", qDs.getString("GOD"));
      qDs.next();
    }
    qDs.enableDataSetEvents(true);
  }

  void setTdsb(QueryDataSet qDs)
  {
    qDs.open();
    qDs.enableDataSetEvents(false);
    for (int i = 0; i< qDs.getRowCount();i++)
    {
      tdsb.insertRow(false);
      tdsb.setString("SB", qDs.getString("SB"));
      tdsb.setString("CSKL", qDs.getString("CSKL"));
      tdsb.setString("VRDOK", qDs.getString("VRDOK"));
      tdsb.setInt("BRDOK", qDs.getInt("BRDOK"));
      tdsb.setString("GOD", qDs.getString("GOD"));
      qDs.next();
    }
    qDs.enableDataSetEvents(true);
  }

  public void deleteSerBr( char mode )
  {

    String delete="";
    if (mode=='M')
    {
       if (TypeDoc.getTypeDoc().isDoubleDoc(old.getString("VRDOK"))) {
       String update = rdUtil.getUtil().updateBeforeDelete(old);
       /**@todo ovaj dio u transakciju treba staviti*/
       dm.getSerbr().getDatabase().executeStatement(update);
       }
    }
    /**@todo ovaj dio u transakciju treba staviti*/
    delete=rdUtil.getUtil().deleteSerBr(mode, old);
    dm.getSerbr().getDatabase().executeStatement(delete);
  }
  public Object[] getDeleteStringsforTransaction(char mode){
    java.util.ArrayList list = new java.util.ArrayList();
    if (mode=='M' && (TypeDoc.getTypeDoc().isDoubleDoc(old.getString("VRDOK")))) {
        list.add(rdUtil.getUtil().updateBeforeDelete(old));
      }
    list.add(rdUtil.getUtil().deleteSerBr(mode, old));
    return list.toArray();
  }

  public boolean beforeDeleteSerBr(com.borland.dx.sql.dataset.QueryDataSet qds, char mode )
  {
    old = new DataRow(qds);
    qds.getDataRow(old);
    if (mode=='U')
    {
      hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
      String qStr = rdUtil.getUtil().checkBeforeDelete(qds);
      vl.execSQL(qStr);
      vl.RezSet.open();
      if(vl.RezSet.getRowCount()==0)
        return true;
    }
    else if (mode=='I')
      return true;
    else if (mode=='M')
      return true;
    JOptionPane.showConfirmDialog(this.jPanel1,"Brisanje nije moguæe !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    return false;
  }

  private boolean checkPKs()
  {
      dm.getSerbr().removeRowFilterListener(dm.getSerbr().getRowFilterListener());
      hr.restart.util.lookupData ld2 = hr.restart.util.lookupData.getlookupData();
      String sCART = qds.getInt("CART")+"";
      tdsb2.first();
      dm.getSerbr().refresh();

      do
      {
        if( ld2.raLocate(dm.getSerbr(),new String[] {"CART","CSERBR"},
        new String[] {sCART,tdsb2.getString("SB")},
        com.borland.dx.dataset.Locate.CASE_INSENSITIVE))
        {
          return false;
        }
        tdsb2.next();
      }while(tdsb2.inBounds());
      return true;
  }

  void jtfPocBroj_focusLost(FocusEvent e) {
     if(!e.isTemporary())
     {
       ser1=findSerBroj(jtfPocBroj.getText(),1);
        if (ser1==0)
        {
          jtfPocBroj.requestFocus();
        }
      }
  }

  void jcbAutGen_actionPerformed(ActionEvent e) {
    if(bAutGen)
    {
      rcc.EnabDisabAll(this.jpAutGen, false);
      bAutGen = false;
      rcc.setLabelLaF(jtfSerBrPoj, true);
      jtfSerBrPoj.requestFocus();
    }
    else
    {
      rcc.EnabDisabAll(this.jpAutGen, true);
      bAutGen = true;
      jtfPrefix.requestFocus();
      rcc.setLabelLaF(jtfSerBrPoj, false);
    }
  }
/**
 * Gledaj 'vamo
 * - tQds - dataSet u kojem se nalaze podaci o artiklu
 * - mod - mod rada (U-ulaz, I-Izlaz)
 * - mode - mod rada sa Validacije ('N' - novi, 'I' - ispravak, 'B' - browse)
 */

  boolean findSB(hr.restart.robno.raArtiklUnos rpc, com.borland.dx.sql.dataset.QueryDataSet tQds, char mod, char mode)
  {

/*
  boolean findSB(Object obj, com.borland.dx.sql.dataset.QueryDataSet tQds, char mod, char mode){
//    hr.restart.robno.raArtiklUnos rpc;
    if (obj instanceof hr.restart.robno.raArtiklUnos) {
    hr.restart.robno.raArtiklUnos rpc = (hr.restart.robno.raArtiklUnos) obj ;
    }
    else if (obj instanceof hr.restart.robno.raArtiklUnos) {
      hr.restart.robno.rapancart rpc = (hr.restart.robno.rapancart) obj;
    }
    else {
      System.out.println("KRIVI PARAMETRICI");

    }
  }
boolean nesto(){
*/
    if (!tdsb2.isOpen())
      tdsb2.setColumns(new Column[] {column8, column9, column10, column11, column12});
    if (!tdsb.isOpen())
      tdsb.setColumns(new Column[] {column13, column14, column15, column16, column17});

    if (rpc.getISB().trim().equals("D"))
    {
      tdsb.open();
      tdsb2.open();
      tds.open();
      cMode=mod;
      cModeIsp = mode;

      tdsb.deleteAllRows();
      tdsb.post();
      tdsb2.deleteAllRows();
      tdsb2.post();
      if (tds.rowCount()==0)
      {
        tds.insertRow(true);
      }

      clearGets();
      if (mod=='U')
      {

        ttC.rnvRtoL.setEnabled(false);
        ttC.rnvLtoR.setEnabled(false);
        ttC.rnvRtoL_all.setEnabled(false);
        ttC.rnvSave.setEnabled(false);

        rcc.EnabDisabAll(this.jPanel1, true);
        if (mode == 'N')
        {
          myDS = rdUtil.getUtil().getSbUlDataSet();
          setTdsb(myDS);
          
          ttC.setLeftDataSet(tdsb);
          ttC.setRightDataSet(tdsb2);

           if(tdsb2.getRowCount()>0)
           {
            ttC.rnvSave.setEnabled(true);
           }
           tds.setInt("broj", tdsb2.getRowCount());
        }
        else if (mode == 'I')
        {
          delBrdok=tQds.getInt("BRDOK");
          delVrdok=tQds.getString("VRDOK");
          delGod=tQds.getString("GOD");
          delCskl=tQds.getString("CSKL");

          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(true);
          ttC.rnvSave.setEnabled(true);

          rcc.EnabDisabAll(this.jPanel2, false);
          myDS = rdUtil.getUtil().getSbUlR(tQds.getInt("BRDOK"));
          setTdsb2(myDS);
          ttC.setRightDataSet(tdsb2);

          myDS.emptyAllRows();

          myDS = rdUtil.getUtil().getSbUlL(tQds.getInt("BRDOK"));
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);

          if(tdsb2.getRowCount()>0)
           {
              ttC.rnvSave.setEnabled(true);
           }
           tds.setInt("broj", tdsb2.getRowCount());
        }
      }
      else if (mod=='I')
      {
        delBrdok=tQds.getInt("BRDOK");
        delVrdok=tQds.getString("VRDOK");
        delGod=tQds.getString("GOD");
        delCskl=tQds.getString("CSKL");

        if (mode == 'N')
        {
          ttC.rnvLtoR_all.setEnabled(true);
          ttC.rnvLtoR.setEnabled(true);
          ttC.rnvRtoL.setEnabled(false);
          ttC.rnvRtoL_all.setEnabled(false);
          ttC.rnvSave.setEnabled(false);

          rcc.EnabDisabAll(this.jPanel1, false);

          myDS = rdUtil.getUtil().getSbIzDataSet(delCskl, tQds.getShort("RBR"));
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          ttC.setRightDataSet(tdsb2);
          tds.setInt("broj", tdsb2.getRowCount());
        }
        else if (mode == 'I' )
        {
          ttC.rnvLtoR.setEnabled(false);
          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(true);
          ttC.rnvSave.setEnabled(false);

          rcc.EnabDisabAll(this.jPanel1, false);

          myDS = rdUtil.getUtil().getSbIzR(tQds.getInt("BRDOK"), delCskl, tQds.getShort("RBR"));
          setTdsb2(myDS);
          ttC.setRightDataSet(tdsb2);

          myDS = rdUtil.getUtil().getSbIzL(tQds.getInt("BRDOK"), delCskl, tQds.getShort("RBR"));
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          tds.setInt("broj", tdsb2.getRowCount());
        }
      }
      else if (mod == 'M')
      {
        delBrdok=tQds.getInt("BRDOK");
        delVrdok=tQds.getString("VRDOK");
        delGod=tQds.getString("GOD");
        delMCsklul=tQds.getString("CSKLUL");
        delMCskliz = tQds.getString("CSKLIZ");
        MCart = tQds.getInt("CART");
        rb=tQds.getShort("RBR");

        if(mode=='N')
        {
          ttC.rnvLtoR.setEnabled(true);
          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(false);
          ttC.rnvSave.setEnabled(false);
          rcc.EnabDisabAll(this.jPanel1, false);

          myDS = rdUtil.getUtil().getSbIzDataSet(delMCskliz, rb);
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          ttC.setRightDataSet(tdsb2);
          tds.setInt("broj", tdsb2.getRowCount());
        }
        else if(mode=='I')
        {
          ttC.rnvLtoR.setEnabled(false);
          ttC.rnvRtoL.setEnabled(true);
          ttC.rnvRtoL_all.setEnabled(true);
          ttC.rnvSave.setEnabled(false);

          rcc.EnabDisabAll(this.jPanel1, false);

          myDS = rdUtil.getUtil().getSbIzR(tQds.getInt("BRDOK"), delMCskliz, rb);
          setTdsb2(myDS);
          ttC.setRightDataSet(tdsb2);

          myDS = rdUtil.getUtil().getSbIzL(tQds.getInt("BRDOK"), delMCskliz, rb);
          setTdsb(myDS);
          ttC.setLeftDataSet(tdsb);
          tds.setInt("broj", tdsb2.getRowCount());
        }
      }
      this.setQds(tQds);
      if (mode=='I')
      {
        makePRES('D');
      }
        makePRES('S');
        
      tdsb.setTableName("serbr");
      tdsb2.setTableName("serbr");

      if(tds.getInt("broj")==0)
        ttC.rnvRtoL.setEnabled(false);
      ttC.rnvLtoR_all.setEnabled(false);
      ttC.initialize();

      this.setLocation( (int)rpc.getTopLevelAncestor().getBounds().getX() +
                        ((int)rpc.getTopLevelAncestor().getWidth()-(int)this.getWidth())/2,
                        (int)rpc.getTopLevelAncestor().getBounds().getY()+
                        (int)rpc.getTopLevelAncestor().getHeight()-(int)this.getHeight());
      this.pack();
      this.show();
      return okUnos;
    }
    return true;
  }


  public void tomoMove(){

    System.out.println("tdsb.getRowCount() " + tdsb.getRowCount());
    System.out.println("tdsb2.getRowCount() " + tdsb2.getRowCount());

    if (tdsb2.getRowCount() ==qds.getBigDecimal("KOL").intValue()) {
      ttC.rnvSave.setEnabled(true);
    }
    else {
      ttC.rnvSave.setEnabled(false);
    }
    if (tdsb2.getRowCount() ==0){
      ttC.rnvRtoL.setEnabled(false);
      ttC.rnvRtoL_all.setEnabled(false);
    }
    else {
      ttC.rnvRtoL.setEnabled(true);
      ttC.rnvRtoL_all.setEnabled(true);
    }
    if (tdsb.getRowCount() ==0){
      ttC.rnvLtoR.setEnabled(false);
      ttC.rnvLtoR_all.setEnabled(false);
    }
    else {
      ttC.rnvLtoR.setEnabled(true);
      ttC.rnvLtoR_all.setEnabled(true);
    }
//    ttC.repaint();

  }

  public void saveULAZ(){

    if (JOptionPane.showConfirmDialog(this.jPanel1,"Prijenos podataka na stanje ?","Upit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
      if (!checkPKs())
      {
        JOptionPane.showConfirmDialog(this.jPanel1,"Serijski broj postoji !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        tdsb2.deleteAllRows();
        ttC.setRightDataSet(tdsb2);
        ttC.initialize();

        return;
      }
      dm.getSerbr().enableDataSetEvents(false);
      tdsb2.first();
/*
      do
      {
        dm.getSerbr().last();
        dm.getSerbr().insertRow(false);
        dm.getSerbr().setInt("CART", qds.getInt("CART"));
        dm.getSerbr().setString("CSERBR", tdsb2.getString("SB"));
        dm.getSerbr().setString("CSKL", qds.getString("CSKL"));
        dm.getSerbr().setString("VRDOK", qds.getString("VRDOK"));
        dm.getSerbr().setString("GOD", qds.getString("GOD"));
        dm.getSerbr().setInt("BRDOK", qds.getInt("BRDOK"));
        dm.getSerbr().setShort("RBR", qds.getShort("RBR"));
        dm.getSerbr().post();
ST.prn("qds.getShort(RBR) -> "+qds.getShort("RBR"));
        tdsb2.deleteRow();
        tdsb2.setTableName("");
      } while (tdsb2.rowCount()!=0);
*/
ST.prn(qds);
      dm.getSerbr().enableDataSetEvents(true);
      if (!isTransactionActive()) dm.getSerbr().saveChanges();
      okUnos=true;
      this.hide();
     }




  }

  public void savaIZLAZ(char mode){}
  public void savaMESKLA(char mode){}
}