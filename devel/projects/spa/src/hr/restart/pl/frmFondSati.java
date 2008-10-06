/****license*****************************************************************
**   file: frmFondSati.java
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
import hr.restart.db.raPreparedStatement;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmFondSati extends raMatPodaci {
  raNavAction rnvAutoAdd = new raNavAction("Automatika",raImages.IMGSAVEALL,KeyEvent.VK_F9) {
    public void actionPerformed(ActionEvent e) {
      autoAdd();
    }
  };
  hr.restart.util.PreSelect pres = new hr.restart.util.PreSelect(){
    public void SetFokus()
    {
      jrfGodina.requestFocus();
    }
    public boolean Validacija()
    {

      try {
        provjeraGodine();
      }
      catch (Exception ex) {

      }

      if(vl.isEmpty(jrfGodina))
      {
        jrfGodina.requestFocus();
        return false;
      }
      predselekcija = true;
      return true;
    }
  };
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  JPanel jpSel = new JPanel();
  boolean predselekcija = false;

  jpFondSati jpDetail;
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jrfGodina = new JraTextField();
  JraTextField jraKnjig = new JraTextField();
  JLabel jlGodina = new JLabel();


  public frmFondSati() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   */
  protected void autoAdd() {
    int odg = JOptionPane.showConfirmDialog(frmFondSati.this,"Dodati/izmjeniti fond sati na ekranu svim poduzeæima ?");
    if (odg == JOptionPane.YES_OPTION) {
      System.out.println("DODAJEM FOND");
      autoAddFond();
    }
  }

  /**
   * 
   */
  private void autoAddFond() {
    raProcess.runChild(this,new Runnable() {
      public void run() {
        autoAddRun();
	    }
    });
  }
  private void autoAddRun() {
    raPreparedStatement existFond = new raPreparedStatement(getRaQueryDataSet(),raPreparedStatement.COUNT);
    raPreparedStatement updFond = new raPreparedStatement(getRaQueryDataSet(),raPreparedStatement.UPDATE);
    raPreparedStatement addFond = new raPreparedStatement(getRaQueryDataSet(),raPreparedStatement.INSERT);
    QueryDataSet knjigovodstva = OrgStr.getOrgStr().getKnjigovodstva();
    for (knjigovodstva.first(); knjigovodstva.inBounds(); knjigovodstva.next()) {
      String knjig = knjigovodstva.getString("CORG");
      if (!knjig.equals(OrgStr.getKNJCORG())) {
        for (getRaQueryDataSet().first(); getRaQueryDataSet().inBounds(); getRaQueryDataSet().next()) {
          existFond.setKeys(getRaQueryDataSet());
          existFond.setString("KNJIG",knjig,true);
          if (existFond.isExist()) {
            updFond.setValues(getRaQueryDataSet());
            updFond.setKeys(getRaQueryDataSet());
            updFond.setString("KNJIG",knjig,false);
            updFond.setString("KNJIG",knjig,true);
            try {
              updFond.execute();
            } catch (SQLException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          } else {
            addFond.setValues(getRaQueryDataSet());
            addFond.setString("KNJIG",knjig,false);
            try {
              addFond.execute();
            } catch (SQLException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        }
      }
    }    
  }
  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraMjesec, false);
    }
  }

  public void SetFokus(char mode) {
    rcc.setLabelLaF(this.jpDetail.jraSatiuk, false);
    pres.copySelValues();

    if (mode == 'N') {
      jpDetail.jraMjesec.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraSatirad.requestFocus();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraMjesec) || vl.isEmpty(jpDetail.jraSatirad))
      return false;
    if (mode == 'N' && notUnique())
    {
      jpDetail.jraMjesec.requestFocus();
      JOptionPane.showConfirmDialog(jpDetail,"Zapis postoji !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    try {
      provjeraMjeseca();
      return true;
    }
    catch (Exception ex) {
      jpDetail.jraMjesec.setBackground(Color.red);
      jpDetail.jraMjesec.requestFocus();
      return false;
    }
  }

   private boolean notUnique()
  {
    return plUtil.getPlUtil().checkFondSatiUnique(getRaQueryDataSet().getShort("GODINA"),
                                                 getRaQueryDataSet().getShort("MJESEC"));
  }

  private void jbInit() throws Exception {
    addOption(rnvAutoAdd,3);
    xYLayout1.setWidth(265);
    xYLayout1.setHeight(60);
    jpSel.setLayout(xYLayout1);
    this.setRaQueryDataSet(dm.getFondSati());
    this.setVisibleCols(new int[] {0, 1, 2, 3, 4, 5});

    jpDetail = new jpFondSati(this);

    this.setRaDetailPanel(jpDetail);
    jraKnjig.setVisible(false);
    jlGodina.setText("Godina");

//    jrfGodina.addKeyListener(new java.awt.event.KeyAdapter() {
//      public void keyPressed(KeyEvent e) {
//        jrfGodina_keyPressed(e);
//      }
//    });
    jpSel.add(jraKnjig,  new XYConstraints(67, 41, 77, 20));
    jpSel.add(jlGodina,  new XYConstraints(27, 23, -1, -1));
    jpSel.add(jrfGodina, new XYConstraints(151, 26, 100, -1));

    jrfGodina.setColumnName("GODINA");
//    jrfGodina.setDataSet(dm.getFondSati());
//    new raTextMask(jrfGodina, 4, false, raTextMask.DIGITS); -> ogranicenje unosa broja znakova za int u txt field

    jraKnjig.setColumnName("KNJIG");
    //jraKnjig.setDataSet(dm.getFondSati());
//
//    godina.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    godina.setDisplayMask("yyyy");
//    godina.setEditMask("yyyy");
//    godina.setResolvable(false);
//    godina.setSqlType(0);

    pres.setSelDataSet(dm.getFondSati());
    pres.setSelPanel(jpSel);
    setPresKnjig();
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        setPresKnjig();
      }
    });
  }

  private void setPresKnjig() {
    pres.getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG(false));
  }

  public void show()
  {
    if(!predselekcija) {
      pres.showPreselect(this, "Fond sati");
System.out.println(pres.getSelDataSet());
    } else {
      super.show();
    }
  }

  public void this_hide()
  {
    predselekcija = false;
    super.this_hide();
  }

   private void  provjeraMjeseca() throws Exception
  {
     int intOK = 0;
     try {
       Integer provjera = new Integer(jpDetail.jraMjesec.getText());
       intOK = provjera.intValue();
     }
     catch (Exception ex) {
       throw new Exception();
     }

    if (intOK > 12 || intOK < 1)
    {
      throw new Exception();
    }

  }
  private void provjeraGodine() throws Exception
  {
    try {
      Integer i = new Integer(jrfGodina.getText());
    }
    catch (Exception ex) {
      throw new Exception();
    }

    if(jrfGodina.getText().length()!= 4 || jrfGodina.getText().equals(""))
    {
      throw new Exception();
    }
  }

  void jrfGodina_keyPressed(KeyEvent e) {
//    plUtil.getPlUtil().kontolaUnosaTexta(jrfGodina, e);
  }

}
