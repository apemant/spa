/****license*****************************************************************
**   file: jpChooseOdb.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

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

public class jpChooseOdb extends JPanel {
  JlrNavField jrfSifra = new JlrNavField(){
     public void after_lookUp()
     {
      if(!jrfSifra.getText().equals(""))
      {
        rcc.setLabelLaF(jrfSifra, false);
        rcc.setLabelLaF(jrfNaziv, false);
        rcc.setLabelLaF(jbChoose, false);
        if(nivoOdb.equals("RA"))
          rcc.setLabelLaF(jrfIme, false);
//
//        char[] test = new char[]{fOdbici.getMode()};
//        System.out.println("mod: " + new String(test));
        if(fOdbici.getMode()=='N')
        {
          short rbr = plUtil.getPlUtil().getMaxOdbiciRBR(fOdbici.getRaQueryDataSet().getShort("CVRODB"), jrfSifra.getText());
          fOdbici.getRaQueryDataSet().setShort("RBRODB",rbr);
        }
        jpSouthDetail.jp2.jraRBROdb.requestFocus();
      }
     }
  };
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jrfNaziv = new JlrNavField(){
    public void after_lookUp()
     {
      if(!jrfNaziv.getText().equals(""))
      {
        rcc.setLabelLaF(jrfSifra, false);
        rcc.setLabelLaF(jrfNaziv, false);
        rcc.setLabelLaF(jbChoose, false);
        if(nivoOdb.equals("RA"))
          rcc.setLabelLaF(jrfIme, false);
        jpSouthDetail.jp2.jraRBROdb.requestFocus();
      }
     }
  };

  JlrNavField jrfIme = new JlrNavField(){
    public void after_lookUp()
     {
      if(!jrfIme.getText().equals(""))
      {
        rcc.setLabelLaF(jrfSifra, false);
        rcc.setLabelLaF(jrfNaziv, false);
        rcc.setLabelLaF(jbChoose, false);
        rcc.setLabelLaF(jrfIme, false);
//        jpSouthDetail.jp2.jraPnb1.requestFocus();
        jpSouthDetail.jp2.jraRBROdb.requestFocus();
      }
     }
  };
//  frmOdbici fOdbici;
  raMatPodaci fOdbici;
  JraButton jbChoose = new JraButton();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  jpOdbici jpSouthDetail;

  Hashtable HTsifra = new Hashtable();
  Hashtable HTnaziv = new Hashtable();
  QueryDataSet qds = new QueryDataSet();
  String sifra = "";
  String naziv = "";
  String nivoOdb = "";
  dM dm = dM.getDataModule();
  JraTextField jtfRBR = new JraTextField();
  JLabel jlRbr = new JLabel();
  JLabel jlLabel = new JLabel();
  JLabel jlSifra = new JLabel();
  JLabel jlNaziv = new JLabel();
  JLabel jlIme = new JLabel();

//  public jpChooseOdb(frmOdbici f)
//  {
//    fOdbici = f;
//
//    try {
//
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//
//  }

  public jpChooseOdb(raMatPodaci f, String nivoOdbitka, jpOdbici jp)
  {
    fOdbici = f;
    jpSouthDetail = jp;
    nivoOdb = nivoOdbitka;
    try {

      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

  }

  private void jbInit() throws Exception {

    this.setLayout(xYLayout1);
    xYLayout1.setWidth(605);
    xYLayout1.setHeight(60);
    jbChoose.setText("...");

    jlRbr.setText("Redni broj odbitka");
    jlSifra.setText("Šifra");
    if(nivoOdb.equals("RA"))
      jlNaziv.setText("Prezime");
    else
      jlNaziv.setText("Naziv");
    jlIme.setText("Ime");


    if(nivoOdb.equals("RA"))
    {
      this.add(jrfSifra,      new XYConstraints(150, 35, 100, -1));
      this.add(jrfNaziv,        new XYConstraints(385, 35, 170, -1));
      this.add(jrfIme,        new XYConstraints(255, 35, 125, -1));
      this.add(jbChoose,      new XYConstraints(559, 35, 21, 21));
      this.add(jlLabel,   new XYConstraints(15, 35, -1, -1));
      this.add(jlSifra,    new XYConstraints(150, 17, -1, -1));
      this.add(jlNaziv,      new XYConstraints(385, 17, -1, -1));
      this.add(jlIme,      new XYConstraints(255, 17, -1, -1));
    }
    else
    {
      this.add(jrfSifra,      new XYConstraints(150, 35, 100, -1));
      this.add(jrfNaziv,        new XYConstraints(255, 35, 300, -1));
      this.add(jbChoose,      new XYConstraints(559, 35, 21, 21));
      this.add(jlLabel,   new XYConstraints(15, 35, -1, -1));
      this.add(jlSifra,    new XYConstraints(150, 17, -1, -1));
      this.add(jlNaziv,      new XYConstraints(255, 17, -1, -1));
    }


    jtfRBR.setHorizontalAlignment(SwingConstants.RIGHT);
//    this.jrfNaziv.setNextFocusableComponent(this.jpSouthDetail.jp2.jraPnb1);
//    this.jrfNaziv.setNextFocusableComponent(this.jpSouthDetail.jp2.jraRBROdb);

    BindComp();
  }

  private void BindComp()
  {
    this.setHash();

    sifra = HTsifra.get(nivoOdb).toString();
    naziv = HTnaziv.get(nivoOdb).toString();
    setDS();

    jrfSifra.setColumnName("CKEY");
    jrfSifra.setNavColumnName(sifra);
    jrfSifra.setDataSet(fOdbici.getRaQueryDataSet());

    if(nivoOdb.equals("RA"))
    {
      jrfSifra.setColNames(new String[] {"IME", naziv});
      jrfSifra.setTextFields(new JTextComponent[] {jrfIme, jrfNaziv});
      jrfSifra.setVisCols(new int[] {0, 1, 2});

      jrfIme.setColumnName("IME");
      jrfIme.setNavProperties(jrfSifra);
      jrfIme.setSearchMode(1);
    }
    else
    {
      jrfSifra.setColNames(new String[] {naziv});
      jrfSifra.setTextFields(new JTextComponent[] {jrfNaziv});
      jrfSifra.setVisCols(new int[] {0, 1});
    }

    if(nivoOdb.equals("PO"))
      jrfSifra.setRaDataSet(dm.getOrgstruktura());
    else
      jrfSifra.setRaDataSet(qds);

    jrfSifra.setSearchMode(0);
    jrfSifra.setNavButton(jbChoose);

    jrfNaziv.setColumnName(naziv);
    jrfNaziv.setNavProperties(jrfSifra);
    jrfNaziv.setSearchMode(1);

    jtfRBR.setColumnName("RBRODB");
    jtfRBR.setDataSet(fOdbici.getRaQueryDataSet());


    this.jrfSifra.requestFocus();

  }

  public void setHash()
  {
    HTsifra.put("OP", "COPCINE");
    HTsifra.put("RA", "CRADNIK");
    HTsifra.put("OJ", "CORG");
    HTsifra.put("VR", "CVRO");
    HTsifra.put("ZA", "CVRP");
    HTsifra.put("PO", "CORG");

    HTnaziv.put("OP","NAZIVOP");
    HTnaziv.put("RA","PREZIME");
    HTnaziv.put("OJ","NAZIV");
    HTnaziv.put("VR","NAZIVRO");
    HTnaziv.put("ZA","NAZIV");
    HTnaziv.put("PO","NAZIV");
  }

  public void setDS()
  {
    if(nivoOdb.equals("ZA"))
    {
      jlLabel.setText("Primanja");
      qds = dm.getVrsteprim();
    }
    if (nivoOdb.equals("RA"))
    {
      jlLabel.setText("Radnik");
      qds = dm.getAllRadnici();
//      qds = hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig();
    }
    if (nivoOdb.equals("OJ"))
    {
      jlLabel.setText("Org. jedinica");
      qds = dm.getOrgstruktura();
    }
    if (nivoOdb.equals("OP"))
    {
      jlLabel.setText("Op\u0107ina");
      qds = dm.getOpcine();
    }
    if (nivoOdb.equals("VR"))
    {
      jlLabel.setText("Vr. radnog odnosa");
      qds = dm.getVrodn();
    }
    if (nivoOdb.equals("PO"))
    {
      jlLabel.setText("Poduze\u0107e");
      qds = dm.getOrgpl();
    }

  }

  public void ESCPressed(char mod)
  {
    if(!jrfSifra.isEnabled() && mod=='N')
    {
      rcc.setLabelLaF(jrfSifra, true);
      rcc.setLabelLaF(jrfNaziv, true);
      rcc.setLabelLaF(jbChoose, true);
      fOdbici.getRaQueryDataSet().setShort("RBRODB", (short)1);
      jrfSifra.setText("");
      jrfSifra.forceFocLost();
      try {
        rcc.setLabelLaF(jrfIme, true);
      }
      catch (Exception ex) {

      }

      jrfSifra.requestFocus();
    }
    else
    {
      fOdbici.getOKpanel().jPrekid_actionPerformed();
    }
  }

}