/****license*****************************************************************
**   file: jpChooseOdb2.java
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

public class jpChooseOdb2 extends JPanel {
  JlrNavField jrfSifra = new JlrNavField(){
    public void after_lookUp()
     {
//      field =2;
      if(jrfSifra2.isEnabled())
        disabFields(0);
      else
        disabFields(3);
//      if( esc==false)
        setRbrOdb();

     }
  };
  JlrNavField jrfNaziv = new JlrNavField(){
    public void after_lookUp()
     {
      if(jrfSifra2.isEnabled())
        disabFields(0);
      else
        disabFields(3);
//      if( esc==false)
        setRbrOdb();
     }
  };
  JraButton jbChoose = new JraButton(){
    public void after_lookUp()
     {
      if(jrfSifra2.isEnabled())
        disabFields(0);
      else
        disabFields(3);
//      if( esc==false)
        setRbrOdb();
     }
  };
  JlrNavField jrfSifra2 = new JlrNavField(){
    public void after_lookUp()
     {
//      field = 1;
      disabFields(1);
//      if( esc==false)
        setRbrOdb();
     }

  };
  JlrNavField jrfIme = new JlrNavField(){
    public void after_lookUp()
     {
      if(nivoOdb.substring(0,2).equals("RA"))
      {
        if(jrfSifra2.isEnabled())
          disabFields(0);
        else
          disabFields(3);
      }
      else if(nivoOdb.substring(2,4).equals("RA"))
      {
        disabFields(1);
      }
//      if( esc==false)
        setRbrOdb();
     }
  };
  JlrNavField jrfNaziv2 = new JlrNavField(){
    public void after_lookUp()
     {
      disabFields(1);
//      if( esc==false)
        setRbrOdb();
     }
  };
  JraButton jbChoose2 = new JraButton(){
    public void after_lookUp()
     {
      disabFields(1);
//      if( esc==false)
        setRbrOdb();
     }
  };
  raCommonClass rcc = raCommonClass.getraCommonClass();

  raMatPodaci fOdbici;
  jpOdbici jpSouthDetail;
  XYLayout xYLayout1 = new XYLayout();

  Hashtable HTsifra = new Hashtable();
  Hashtable HTnaziv = new Hashtable();
  QueryDataSet qds = new QueryDataSet();
  QueryDataSet qds2 = new QueryDataSet();
  String sifra = "";
  String naziv = "";
  String sifra2 = "";
  String naziv2 = "";
  String nivoOdb = "";
  dM dm = dM.getDataModule();
  JraTextField jtfRBR = new JraTextField();
  JLabel jlRbr = new JLabel();
  JLabel jlLabel = new JLabel();
  JLabel jlLabel2 = new JLabel();
  JLabel jlSifra = new JLabel();
  JLabel jlNaziv = new JLabel();
  JLabel jlSifra2 = new JLabel();
  JLabel jlNaziv2 = new JLabel();
  JLabel jlIme = new JLabel();
//  int field = 1;
  boolean global;

  public jpChooseOdb2(raMatPodaci f, String nivoOdbitka, jpOdbici jp, boolean global)
  {
    this.global = global;
    fOdbici = f;
    this.jpSouthDetail = jp;
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
    jbChoose.setText("...");

    jlRbr.setText("Redni broj odbitka");
    jlSifra.setText("Šifra");
    jlSifra2.setText("Šifra");
    jlNaziv.setText("Naziv");
    jlNaziv2.setText("Naziv");

    jlIme.setText("Ime");
    jbChoose2.setText("...");

    if(nivoOdb.substring(0,2).equals("RA"))
    {
      xYLayout1.setHeight(105);
      jlNaziv.setText("Prezime");
      this.add(jlLabel2,   new XYConstraints(15, 80, -1, -1));
      this.add(jlSifra,     new XYConstraints(150, 17, -1, -1));
      this.add(jlSifra2,     new XYConstraints(150, 63, -1, -1));
      this.add(jlNaziv,   new XYConstraints(385, 17, -1, -1));
      this.add(jlNaziv2,   new XYConstraints(255, 63, -1, -1));
      this.add(jlIme,   new XYConstraints(255, 17, -1, -1));

      this.add(jrfNaziv,        new XYConstraints(385, 35, 170, -1));
      this.add(jrfIme,        new XYConstraints(255, 35, 125, -1));
      this.add(jrfSifra,      new XYConstraints(150, 35, 100, -1));
      this.add(jbChoose,     new XYConstraints(559, 35, 21, 21));

      this.add(jrfNaziv2,       new XYConstraints(255, 80, 300, -1));
      this.add(jrfSifra2,      new XYConstraints(150, 80, 100, -1));
      this.add(jbChoose2,     new XYConstraints(559, 80, 21, 21));
    }
    else if (nivoOdb.substring(2, 4).equals("RA"))
    {
      xYLayout1.setHeight(105);
      jlNaziv2.setText("Prezime");
      this.add(jlLabel2,   new XYConstraints(15, 80, -1, -1));
      this.add(jlSifra2,     new XYConstraints(150, 17, -1, -1));
      this.add(jlNaziv2,   new XYConstraints(385, 63, -1, -1));
      this.add(jlSifra,     new XYConstraints(150, 63, -1, -1));
      this.add(jlNaziv,   new XYConstraints(255, 17, -1, -1));
      this.add(jlIme,   new XYConstraints(255, 63, -1, -1));

      this.add(jrfNaziv,        new XYConstraints(255, 35, 300, -1));
      this.add(jrfSifra,      new XYConstraints(150, 35, 100, -1));
      this.add(jbChoose,     new XYConstraints(559, 35, 21, 21));

      this.add(jrfNaziv2,       new XYConstraints(385, 80, 170, -1));
      this.add(jrfIme,        new XYConstraints(255, 80, 125, -1));
      this.add(jrfSifra2,      new XYConstraints(150, 80, 100, -1));
      this.add(jbChoose2,     new XYConstraints(559, 80, 21, 21));
    }
    else
    {
      xYLayout1.setHeight(85);
      this.add(jlSifra,     new XYConstraints(150, 17, -1, -1));
      this.add(jlNaziv,   new XYConstraints(255, 17, -1, -1));

      this.add(jlLabel2,   new XYConstraints(15, 60, -1, -1));
      this.add(jrfNaziv,        new XYConstraints(255, 35, 300, -1));
      this.add(jrfSifra,      new XYConstraints(150, 35, 100, -1));
      this.add(jbChoose,     new XYConstraints(559, 35, 21, 21));

      this.add(jrfNaziv2,       new XYConstraints(255, 60, 300, -1));
      this.add(jrfSifra2,      new XYConstraints(150, 60, 100, -1));
      this.add(jbChoose2,     new XYConstraints(559, 60, 21, 21));
    }



    this.add(jlLabel,   new XYConstraints(15, 35, -1, -1));



    jtfRBR.setHorizontalAlignment(SwingConstants.RIGHT);


    BindComp();
  }

  private void BindComp()
  {
    this.setHash();
    String temp1 = nivoOdb.substring(0,2);
    String temp2 = nivoOdb.substring(2,4);

    sifra = HTsifra.get(temp1).toString();
    naziv = HTnaziv.get(temp1).toString();

    sifra2 = HTsifra.get(temp2).toString();
    naziv2 = HTnaziv.get(temp2).toString();

    setDS();

    jrfSifra.setColumnName("CKEY");
    jrfSifra.setNavColumnName(sifra);

    jrfSifra.setVisCols(new int[] {0, 1});
    jrfSifra.setSearchMode(0);

    if(temp1.equals("RA"))
    {
      jrfSifra.setColNames(new String[] {"IME", naziv});
      jrfSifra.setTextFields(new JTextComponent[] {jrfIme, jrfNaziv});
      jrfIme.setColumnName("IME");
      jrfIme.setNavProperties(jrfSifra);
      jrfIme.setSearchMode(1);

    }
    else
    {
      jrfSifra.setColNames(new String[] {naziv});
      jrfSifra.setTextFields(new JTextComponent[] {jrfNaziv});
    }


    if(temp1.equals("PO"))
      jrfSifra.setRaDataSet(dm.getOrgstruktura());
    else
      jrfSifra.setRaDataSet(qds);

    jrfSifra.setNavButton(jbChoose);

    jrfNaziv.setColumnName(naziv);
    jrfNaziv.setNavProperties(jrfSifra);
    jrfNaziv.setSearchMode(1);

    jrfSifra2.setColumnName("CKEY2");
    jrfSifra2.setNavColumnName(sifra2);

    jrfSifra2.setSearchMode(0);

    if(temp2.equals("RA"))
    {
      jrfSifra2.setColNames(new String[] {"IME", naziv2});
      jrfSifra2.setTextFields(new JTextComponent[] {jrfIme, jrfNaziv2});
      jrfSifra2.setVisCols(new int[] {0, 1, 2});
      jrfIme.setColumnName("IME");
      jrfIme.setNavProperties(jrfSifra2);
      jrfIme.setSearchMode(1);
    }
    else
    {
      jrfSifra2.setColNames(new String[] {naziv2});
      jrfSifra2.setTextFields(new JTextComponent[] {jrfNaziv2});
      jrfSifra2.setVisCols(new int[] {0, 1});
    }

    if(temp2.equals("PO"))
      jrfSifra2.setRaDataSet(dm.getOrgstruktura());
    else
      jrfSifra2.setRaDataSet(qds2);

//    jrfSifra2.setRaDataSet(qds2);
    jrfSifra2.setNavButton(jbChoose2);

    jrfNaziv2.setColumnName(naziv2);
    jrfNaziv2.setNavProperties(jrfSifra2);
    jrfNaziv2.setSearchMode(1);



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
//    HTnaziv.put("PO","NAZIVRM");
    HTnaziv.put("PO","NAZIV");
  }

  public void setDS()
  {
    String nivo = nivoOdb.substring(0,2);
    String nivo2 = nivoOdb.substring(2,4);
    if(nivo.equals("ZA"))
    {
      jlLabel.setText("Primanja");
      qds = dm.getVrsteprim();
    }
    if (nivo.equals("RA"))
    {
      jlLabel.setText("Radnik");
      //qds = hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig();
      qds = dm.getAllRadnici();
    }
    if (nivo.equals("OJ"))
    {
      jlLabel.setText("Org. jedinica");
      qds = dm.getOrgstruktura();
    }
    if (nivo.equals("OP"))
    {
      jlLabel.setText("Op\u0107ina");
      qds = dm.getOpcine();
    }
    if (nivo.equals("VR"))
    {
      jlLabel.setText("Vr. radnog odnosa");
      qds = dm.getVrodn();
    }
    if (nivo.equals("PO"))
    {
      jlLabel.setText("Poduze\u0107e");
//      qds = dm.getRadMJ();
      qds = dm.getOrgpl();
    }

    if(nivo2.equals("ZA"))
    {
      jlLabel2.setText("Primanja");
      qds2 = dm.getVrsteprim();
    }
    if (nivo2.equals("RA"))
    {
      jlLabel2.setText("Radnik");
      //qds = hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig();
      qds2 = dm.getAllRadnici();
    }
    if (nivo2.equals("OJ"))
    {
      jlLabel2.setText("Org. jedinica");
      qds2 = dm.getOrgstruktura();
    }
    if (nivo2.equals("OP"))
    {
      jlLabel2.setText("Op\u0107ina");
      qds2 = dm.getOpcine();
    }
    if (nivo2.equals("VR"))
    {
      jlLabel2.setText("Vr. radnog odnosa");
      qds2 = dm.getVrodn();
    }

    if (nivo2.equals("PO"))
    {
      jlLabel2.setText("Poduze\u0107e");
      qds2 = dm.getOrgpl();
    }
  }

   public void ESCPressed(char mod)
  {
//     esc = true;
    if(!jrfSifra2.isEnabled() && mod=='N')
    {
      if(nivoOdb.substring(2,4).equals("RA"))
          rcc.setLabelLaF(jrfIme, true);
      rcc.setLabelLaF(jrfSifra2, true);
      rcc.setLabelLaF(jrfNaziv2, true);
      rcc.setLabelLaF(jbChoose2, true);
      jrfSifra2.setText("");
      jrfSifra2.forceFocLost();
      jrfSifra2.requestFocus();

    }
    else if(!jrfSifra.isEnabled() && mod == 'N')
    {

      if(nivoOdb.substring(0,2).equals("RA"))
          rcc.setLabelLaF(jrfIme, true);
      rcc.setLabelLaF(jrfSifra, true);
      rcc.setLabelLaF(jrfNaziv, true);
      rcc.setLabelLaF(jbChoose, true);
      this.jrfSifra.setText("");
      this.jrfSifra.forceFocLost();
      jrfSifra.requestFocus();

    }
    else
    {
      fOdbici.getOKpanel().jPrekid_actionPerformed();
    }
//    esc = false;
  }

  private void disabFields(int param)
  {
    if(param == 0)
    {
      if(!jrfSifra.getText().equals(""))
      {
        rcc.setLabelLaF(jrfSifra, false);
        rcc.setLabelLaF(jrfNaziv, false);
        rcc.setLabelLaF(jbChoose, false);
        if(nivoOdb.substring(0,2).equals("RA"))
          rcc.setLabelLaF(jrfIme, false);
        if(global == false)
          jrfSifra2.requestFocus();
      }
    }
    else if (param == 1)
    {
      if(!jrfSifra2.getText().equals(""))
      {
        rcc.setLabelLaF(jrfSifra2, false);
        rcc.setLabelLaF(jrfNaziv2, false);
        rcc.setLabelLaF(jbChoose2, false);
        if(nivoOdb.substring(2,4).equals("RA"))
          rcc.setLabelLaF(jrfIme, false);
        jpSouthDetail.jp2.jraRBROdb.requestFocus();
      }
    }
    else
    {
      if(!jrfSifra.getText().equals(""))
      {
        rcc.setLabelLaF(jrfSifra, false);
        rcc.setLabelLaF(jrfNaziv, false);
        rcc.setLabelLaF(jbChoose, false);
        if(nivoOdb.substring(0,2).equals("RA"))
          rcc.setLabelLaF(jrfIme, false);
        jpSouthDetail.jp2.jraRBROdb.requestFocus();
      }
    }
  }

  private void setRbrOdb()
  {
    if(fOdbici.getMode()!='N') return;
    if(!jrfSifra.getText().equals("") &&  !jrfSifra2.getText().equals(""))
    {
      short rbr = plUtil.getPlUtil().getMaxOdbiciRBR(fOdbici.getRaQueryDataSet().getShort("CVRODB"),
          jrfSifra.getText(), jrfSifra2.getText());
      fOdbici.getRaQueryDataSet().setShort("RBRODB", rbr);
      jpSouthDetail.jp2.jraRBROdb.selectAll();
    }
    else
    {
//      fOdbici.getRaQueryDataSet().setShort("RBRODB", (short)1);
//      if(field ==1)
//      {
//        SwingUtilities.invokeLater(new Runnable() {
//          public void run() {
//            jrfSifra.requestFocus();
//          }
//        });
//      }
//      else
//      {
//        SwingUtilities.invokeLater(new Runnable() {
//          public void run() {
//            jrfSifra2.requestFocus();
//          }
//        });
//      }
    }
  }
}