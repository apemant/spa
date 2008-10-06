/****license*****************************************************************
**   file: frmKontaIsp.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmKontaIsp extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  QueryDataSet filteredDS = new QueryDataSet();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  JPanel jpDetail = new JPanel();
  JLabel jlBrojkonta = new JLabel();
  JLabel jlBrojkonta1 = new JLabel();
  JLabel jlBrojkonta2 = new JLabel();
  JLabel jlBrojkonta3 = new JLabel();
  JLabel jlBrojkonta4 = new JLabel();
  JLabel jlBrojkonta5 = new JLabel();
  JraButton jbSelBrojkonta = new JraButton();
  JraButton jbSelBrojkonta1 = new JraButton();
  JraButton jbSelBrojkonta2 = new JraButton();
  JraButton jbSelBrojkonta3 = new JraButton();
  JraButton jbSelBrojkonta4 = new JraButton();
  JraButton jbSelBrojkonta5 = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JlrNavField jlrBrojkonta = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkonta = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrBrojkonta1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkonta1 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrBrojkonta2 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkonta2 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrBrojkonta3 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkonta3= new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JlrNavField jlrBrojkonta4 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkonta4= new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrBrojkonta5 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazivkonta5= new JlrNavField() {
    public void after_lookUp() {
    }
  };

  private XYLayout xYLayout1 = new XYLayout();

  public frmKontaIsp() {
    super(2);
    try {

      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jlrBrojkonta, false);
      rcc.setLabelLaF(jlrNazivkonta, false);
      rcc.setLabelLaF(jbSelBrojkonta, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jlrBrojkonta.setText("");
      jlrBrojkonta1.setText("");
      jlrBrojkonta2.setText("");
      jlrBrojkonta3.setText("");
      jlrBrojkonta4.setText("");
      jlrBrojkonta5.setText("");
      jlrBrojkonta.forceFocLost();
      jlrBrojkonta1.forceFocLost();
      jlrBrojkonta2.forceFocLost();
      jlrBrojkonta3.forceFocLost();
      jlrBrojkonta4.forceFocLost();
      jlrBrojkonta5.forceFocLost();
      jlrBrojkonta.requestFocus();
    } else if (mode == 'I') {
      jlrBrojkonta1.setText("");
      jlrBrojkonta1.forceFocLost();
      jlrBrojkonta2.setText("");
      jlrBrojkonta2.forceFocLost();
      jlrBrojkonta3.setText("");
      jlrBrojkonta3.forceFocLost();
      jlrBrojkonta4.setText("");
      jlrBrojkonta4.forceFocLost();
      jlrBrojkonta5.setText("");
      jlrBrojkonta5.forceFocLost();
      jlrBrojkonta1.requestFocus();
    }
    else
    {
      jlrBrojkonta2.forceFocLost();
    }
  }

  public boolean Validacija(char mode) {
     getRaQueryDataSet().setString("CORG",hr.restart.zapod.OrgStr.getKNJCORG());
    if (vl.isEmpty(jlrBrojkonta1))
      return false;
    if (mode == 'N' && vl.notUnique(jlrBrojkonta))
    {
      jlrNazivkonta5.setText("");
      jlrNazivkonta4.setText("");
      jlrNazivkonta3.setText("");
      jlrNazivkonta2.setText("");
      jlrNazivkonta1.setText("");
      jlrNazivkonta5.forceFocLost();
      jlrNazivkonta4.forceFocLost();
      jlrNazivkonta3.forceFocLost();
      jlrNazivkonta2.forceFocLost();
      jlrNazivkonta1.forceFocLost();
      jlrBrojkonta.requestFocus();
      return false;
    }
    if(jlrBrojkonta.getText().equals(jlrBrojkonta1.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto ispravka jednak kontu osnovice !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta1.setText("");
      jlrBrojkonta1.forceFocLost();
      jlrBrojkonta1.requestFocus();
      return false;
    }

    if(jlrBrojkonta.getText().equals(jlrBrojkonta2.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto amortizacije jednak kontu osnovice !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta2.setText("");
      jlrBrojkonta2.forceFocLost();
      jlrBrojkonta2.requestFocus();
      return false;
    }

    if(jlrBrojkonta.getText().equals(jlrBrojkonta3.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto dobavljaèa jednak kontu osnovice !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta3.setText("");
      jlrBrojkonta3.forceFocLost();
      jlrBrojkonta3.requestFocus();
      return false;
    }

    if(jlrBrojkonta.getText().equals(jlrBrojkonta4.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto poreza jednak kontu osnovice !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta4.setText("");
      jlrBrojkonta4.forceFocLost();
      jlrBrojkonta4.requestFocus();
      return false;
    }

    if(jlrBrojkonta.getText().equals(jlrBrojkonta5.getText()) )
        {
          JOptionPane.showConfirmDialog(this,"Konto sadašnje vrijednosti jednak kontu osnovice !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
          jlrBrojkonta5.setText("");
          jlrBrojkonta5.forceFocLost();
          jlrBrojkonta5.requestFocus();
          return false;
    }

    if(jlrBrojkonta1.getText().equals(jlrBrojkonta2.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto amortizacije jednak kontu ispravka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta2.setText("");
      jlrBrojkonta2.forceFocLost();
      jlrBrojkonta2.requestFocus();
      return false;
    }

    if(jlrBrojkonta1.getText().equals(jlrBrojkonta3.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto dobavljaèa jednak kontu ispravka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta3.setText("");
      jlrBrojkonta3.forceFocLost();
      jlrBrojkonta3.requestFocus();
      return false;
    }

    if(jlrBrojkonta1.getText().equals(jlrBrojkonta4.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto poreza jednak kontu ispravka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta4.setText("");
      jlrBrojkonta4.forceFocLost();
      jlrBrojkonta4.requestFocus();
      return false;
    }
    if(jlrBrojkonta1.getText().equals(jlrBrojkonta5.getText()) )
        {
          JOptionPane.showConfirmDialog(this,"Konto sadašnje vrijednosti jednak kontu ispravka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
          jlrBrojkonta5.setText("");
          jlrBrojkonta5.forceFocLost();
          jlrBrojkonta5.requestFocus();
          return false;
    }

    if(jlrBrojkonta2.getText().equals(jlrBrojkonta3.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto dobavljaèa jednak kontu amortizacije!","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta3.setText("");
      jlrBrojkonta3.forceFocLost();
      jlrBrojkonta3.requestFocus();
      return false;
    }

    if(jlrBrojkonta2.getText().equals(jlrBrojkonta4.getText()) )
    {
      JOptionPane.showConfirmDialog(this,"Konto poreza jednak kontu amortizacije!","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      jlrBrojkonta4.setText("");
      jlrBrojkonta4.forceFocLost();
      jlrBrojkonta4.requestFocus();
      return false;
    }

    if(jlrBrojkonta2.getText().equals(jlrBrojkonta5.getText()) )
        {
          JOptionPane.showConfirmDialog(this,"Konto sadašnje vrijednosti jednak kontu amortizacije!","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
          jlrBrojkonta5.setText("");
          jlrBrojkonta5.forceFocLost();
          jlrBrojkonta5.requestFocus();
          return false;
    }

    if(jlrBrojkonta3.getText().equals(jlrBrojkonta4.getText()) )
        {
          JOptionPane.showConfirmDialog(this,"Konto poreza jednak kontu dobavljaèa!","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
          jlrBrojkonta4.setText("");
          jlrBrojkonta4.forceFocLost();
          jlrBrojkonta4.requestFocus();
          return false;
    }

    if(jlrBrojkonta3.getText().equals(jlrBrojkonta5.getText()) )
        {
          JOptionPane.showConfirmDialog(this,"Konto sadašnje vrijednosti jednak kontu dobavljaèa!","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
          jlrBrojkonta5.setText("");
          jlrBrojkonta5.forceFocLost();
          jlrBrojkonta5.requestFocus();
          return false;
    }

    if(jlrBrojkonta4.getText().equals(jlrBrojkonta5.getText()) )
        {
          JOptionPane.showConfirmDialog(this,"Konto sadašnje vrijednosti jednak kontu poreza!","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
          jlrBrojkonta5.setText("");
          jlrBrojkonta5.forceFocLost();
          jlrBrojkonta5.requestFocus();
          return false;
    }

    return true;
  }



  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getOS_Kontaisp());
    this.setVisibleCols(new int[] {3, 4, 7});
    jbSelBrojkonta.setText("...");
    jbSelBrojkonta1.setText("...");
    jbSelBrojkonta2.setText("...");
    jbSelBrojkonta3.setText("...");
    jbSelBrojkonta4.setText("...");
    jbSelBrojkonta5.setText("...");
    jlBrojkonta.setText("Konto osnovice");
    jlBrojkonta1.setText("Konto ispravka");
    jlBrojkonta2.setText("Konto amortizacije");
    jlBrojkonta3.setText("Konto dobavljaèa");
    jlBrojkonta4.setText("Konto poreza");
    jlBrojkonta5.setText("Konto sadašnje vr.");


//
//    jpDetail = new jpKontaIsp(this);
    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("BROJKONTA", new String[]{"BROJKONTA","NAZIVKONTA"},dm.getKonta())
    );
    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("KONTOISP",new String[]{"BROJKONTA","NAZIVKONTA"},
                                   new String[]{"KONTOISP"},new String[]{"BROJKONTA"},dm.getKonta())
    );
    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("KONTOAMOR",new String[]{"BROJKONTA","NAZIVKONTA"},
                                   new String[]{"KONTOAMOR"},new String[]{"BROJKONTA"},dm.getKonta())
    );
    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("KONTODOB",new String[]{"BROJKONTA","NAZIVKONTA"},
                                   new String[]{"KONTODOB"},new String[]{"BROJKONTA"},dm.getKonta())
    );

    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("KONTOPOR",new String[]{"BROJKONTA","NAZIVKONTA"},
                                   new String[]{"KONTOPOR"},new String[]{"BROJKONTA"},dm.getKonta())
    );

    this.getJpTableView().addTableModifier(
            new raTableColumnModifier("KONTOSADVR",new String[]{"BROJKONTA","NAZIVKONTA"},
                                       new String[]{"KONTOSADVR"},new String[]{"BROJKONTA"},dm.getKonta())
    );

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
        new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnjig, String newKnjig)
      {
        setNewQuery();
      };
    }
    );
    setNewQuery();
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */

    jlrBrojkonta.setColumnName("BROJKONTA");
    jlrBrojkonta.setDataSet(getRaQueryDataSet());
    jlrBrojkonta.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta.setTextFields(new JTextComponent[] {jlrNazivkonta});
    jlrBrojkonta.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta.setSearchMode(3);
    jlrBrojkonta.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jlrBrojkonta.setNavButton(jbSelBrojkonta);

    jlrNazivkonta.setColumnName("NAZIVKONTA");
    jlrNazivkonta.setNavProperties(jlrBrojkonta);
    jlrNazivkonta.setSearchMode(1);

    jlrBrojkonta1.setColumnName("KONTOISP");
    jlrBrojkonta1.setNavColumnName("BROJKONTA");
    jlrBrojkonta1.setDataSet(getRaQueryDataSet());
    jlrBrojkonta1.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta1.setTextFields(new JTextComponent[] {jlrNazivkonta1});
    jlrBrojkonta1.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta1.setSearchMode(3);
    jlrBrojkonta1.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jlrBrojkonta1.setNavButton(jbSelBrojkonta1);

    jlrNazivkonta1.setColumnName("NAZIVKONTA");
    jlrNazivkonta1.setNavProperties(jlrBrojkonta1);
    jlrNazivkonta1.setSearchMode(1);

    jlrBrojkonta2.setColumnName("KONTOAMOR");
    jlrBrojkonta2.setNavColumnName("BROJKONTA");
    jlrBrojkonta2.setDataSet(getRaQueryDataSet());
    jlrBrojkonta2.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta2.setTextFields(new JTextComponent[] {jlrNazivkonta2});
    jlrBrojkonta2.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta2.setSearchMode(3);
    jlrBrojkonta2.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jlrBrojkonta2.setNavButton(jbSelBrojkonta2);

    jlrNazivkonta2.setColumnName("NAZIVKONTA");
    jlrNazivkonta2.setNavProperties(jlrBrojkonta2);
    jlrNazivkonta2.setSearchMode(1);

    jlrBrojkonta3.setColumnName("KONTODOB");
    jlrBrojkonta3.setNavColumnName("BROJKONTA");
    jlrBrojkonta3.setDataSet(getRaQueryDataSet());
    jlrBrojkonta3.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta3.setTextFields(new JTextComponent[] {jlrNazivkonta3});
    jlrBrojkonta3.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta3.setSearchMode(3);
    jlrBrojkonta3.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jlrBrojkonta3.setNavButton(jbSelBrojkonta3);

    jlrNazivkonta3.setColumnName("NAZIVKONTA");
    jlrNazivkonta3.setNavProperties(jlrBrojkonta3);
    jlrNazivkonta3.setSearchMode(1);


    jlrBrojkonta4.setColumnName("KONTOPOR");
    jlrBrojkonta4.setNavColumnName("BROJKONTA");
    jlrBrojkonta4.setDataSet(getRaQueryDataSet());
    jlrBrojkonta4.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta4.setTextFields(new JTextComponent[] {jlrNazivkonta4});
    jlrBrojkonta4.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta4.setSearchMode(3);
    jlrBrojkonta4.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jlrBrojkonta4.setNavButton(jbSelBrojkonta4);

    jlrNazivkonta4.setColumnName("NAZIVKONTA");
    jlrNazivkonta4.setNavProperties(jlrBrojkonta4);
    jlrNazivkonta4.setSearchMode(1);

    jlrBrojkonta5.setColumnName("KONTOSADVR");
    jlrBrojkonta5.setNavColumnName("BROJKONTA");
    jlrBrojkonta5.setDataSet(getRaQueryDataSet());
    jlrBrojkonta5.setColNames(new String[] {"NAZIVKONTA"});
    jlrBrojkonta5.setTextFields(new JTextComponent[] {jlrNazivkonta5});
    jlrBrojkonta5.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrBrojkonta5.setSearchMode(3);
    jlrBrojkonta5.setRaDataSet(hr.restart.zapod.raKonta.getAnalitickaKonta());
    jlrBrojkonta5.setNavButton(jbSelBrojkonta5);

    jlrNazivkonta5.setColumnName("NAZIVKONTA");
    jlrNazivkonta5.setNavProperties(jlrBrojkonta5);
    jlrNazivkonta5.setSearchMode(1);

    jpDetail.setLayout(xYLayout1);
    xYLayout1.setWidth(560);
    xYLayout1.setHeight(195);
    jpDetail.add(jbSelBrojkonta,    new XYConstraints(520, 30, 21, 21));
    jpDetail.add(jbSelBrojkonta1,    new XYConstraints(520, 55, 21, 21));
    jpDetail.add(jbSelBrojkonta2,    new XYConstraints(520, 80, 21, 21));
    jpDetail.add(jbSelBrojkonta3,    new XYConstraints(520, 105, 21, 21));
    jpDetail.add(jbSelBrojkonta4,    new XYConstraints(520, 130, 21, 21));
    jpDetail.add(jbSelBrojkonta5,    new XYConstraints(520, 155, 21, 21));
    jpDetail.add(jcbAktiv,     new XYConstraints(445, 5, 70, -1));
    jpDetail.add(jlBrojkonta,  new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlBrojkonta1,  new XYConstraints(15, 55, -1, -1));
    jpDetail.add(jlBrojkonta2,  new XYConstraints(15, 80, -1, -1));
    jpDetail.add(jlBrojkonta3,  new XYConstraints(15, 105, -1, -1));
    jpDetail.add(jlBrojkonta4,  new XYConstraints(15, 130, -1, -1));
    jpDetail.add(jlBrojkonta5,  new XYConstraints(15, 155, -1, -1));
    jpDetail.add(jlrBrojkonta,   new XYConstraints(150, 30, 100, -1));
    jpDetail.add(jlrBrojkonta1,   new XYConstraints(150, 55, 100, -1));
    jpDetail.add(jlrBrojkonta2,   new XYConstraints(150, 80, 100, -1));
    jpDetail.add(jlrBrojkonta3,   new XYConstraints(150, 105, 100, -1));
    jpDetail.add(jlrBrojkonta4,   new XYConstraints(150, 130, 100, -1));
    jpDetail.add(jlrBrojkonta5,   new XYConstraints(150, 155, 100, -1));
    jpDetail.add(jlrNazivkonta,       new XYConstraints(255, 30, 260, -1));
    jpDetail.add(jlrNazivkonta1,     new XYConstraints(255, 55, 260, -1));
    jpDetail.add(jlrNazivkonta2,     new XYConstraints(255, 80, 260, -1));
    jpDetail.add(jlrNazivkonta3,     new XYConstraints(255, 105, 260, -1));
    jpDetail.add(jlrNazivkonta4,     new XYConstraints(255, 130, 260, -1));
    jpDetail.add(jlrNazivkonta5,     new XYConstraints(255, 155, 260, -1));
    this.setRaDetailPanel(jpDetail);
  }

  public boolean ValDPEscape(char mode)
  {
    if(mode=='N')
    {
      if(jlrBrojkonta.getText().equals(""))
      {
        return true;
      }
      if(!jlrBrojkonta5.getText().equals(""))
      {
        jlrBrojkonta5.setText("");
        jlrBrojkonta5.forceFocLost();
        jlrBrojkonta5.requestFocus();
        return false;
      }
      if(!jlrBrojkonta4.getText().equals(""))
      {
        jlrBrojkonta4.setText("");
        jlrBrojkonta4.forceFocLost();
        jlrBrojkonta4.requestFocus();
        return false;
      }
      if(!jlrBrojkonta3.getText().equals(""))
      {
        jlrBrojkonta3.setText("");
        jlrBrojkonta3.forceFocLost();
        jlrBrojkonta3.requestFocus();
        return false;
      }
      if(!jlrBrojkonta2.getText().equals(""))
      {
        jlrBrojkonta2.setText("");
        jlrBrojkonta2.forceFocLost();
        jlrBrojkonta2.requestFocus();
        return false;
      }
      if(!jlrBrojkonta1.getText().equals(""))
      {
        jlrBrojkonta1.setText("");
        jlrBrojkonta1.forceFocLost();
        jlrBrojkonta1.requestFocus();
        return false;
      }
      if(!jlrBrojkonta.getText().equals(""))
      {
        jlrBrojkonta.setText("");
        jlrBrojkonta.forceFocLost();
        jlrBrojkonta.requestFocus();
        return false;
      }
    }
    else
    {
      super.ValDPEscape(mode);
    }
    return true;
  }
  public void setNewQuery()
  {
    String qStr = "select * from os_kontaisp where corg ='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'";
    Aus.refilter(getRaQueryDataSet(), qStr);    
    this.getJpTableView().fireTableDataChanged();
    jlrBrojkonta.forceFocLost();
    jlrBrojkonta1.forceFocLost();
    jlrBrojkonta2.forceFocLost();
  }
}
