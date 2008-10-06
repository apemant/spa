/****license*****************************************************************
**   file: raZamjenaArtikla.java
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

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.NavigationAdapter;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

abstract public class raZamjenaArtikla extends JraDialog {
//    extends raMatPodaci {

  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private DataSet goodset;
  private JPanel panel = new JPanel();
  private JraCheckBox zamjena = new JraCheckBox("Nepotreban ");
  private raJPTableView jptv = new raJPTableView(){
    public void mpTable_doubleClicked() {
      doubleClicked();
    }
  };
  private JraTextField kol = new JraTextField();
  private raDohvatF8Stanje f8 = new raDohvatF8Stanje();
//  private JButton zamjena = new JButton();
  String cskl = null;
  private SimpleRaPanCart rapancart1 = new SimpleRaPanCart();
  private SimpleRaPanCart rapancart2 = new SimpleRaPanCart(){
    public String keyF8Pressed(Container c,String polje,String value) {
      return f8.keyF8Pressed(cskl,c,polje,value,
                             hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
    }
  };


//  private SimpleRaPanCart rapancart2 = new SimpleRaPanCart();
  private XYLayout XYLayout1 = new XYLayout();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.swing.raTableColumnModifier TCMartikl;
  private hr.restart.swing.raTableColumnModifier TCMnormat;
  private hr.restart.swing.raTableColumnModifier TCMzam;
  private hr.restart.util.Valid val= hr.restart.util.Valid.getValid();
  private boolean inReplace = false;

  private hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void jPrekid_actionPerformed(){
      CancelPres();
    }
    public void jBOK_actionPerformed(){
      OKPres();
    }
  };

  public void init()  {
    panel.setLayout(XYLayout1);
    XYLayout1.setHeight(200);
    XYLayout1.setWidth(650);
    zamjena.setSelectedDataValue("A");
    zamjena.setUnselectedDataValue("N");
    zamjena.setColumnName("STATUS");
    zamjena.setHorizontalTextPosition(SwingUtilities.LEFT);

//    zamjena.setSelectedDataValue("P");
    panel.setBorder(BorderFactory.createEtchedBorder());
    panel.add(rapancart1,new XYConstraints(0,0,-1,80));
    panel.add(rapancart2,new XYConstraints(0,80,-1,80));
    panel.add(new JLabel("Koli\u010Dina"),new XYConstraints(15,160,-1,-1));
    panel.add(kol,new XYConstraints(150,160,100,-1));
    panel.add(zamjena,new XYConstraints(450,160,100,-1));
    setTitle("Zamjenski artikli");
    getContentPane().setLayout(new BorderLayout());
    JPanel pan = new JPanel(new BorderLayout());
    pan.add(jptv,BorderLayout.CENTER);
    pan.add(panel,BorderLayout.SOUTH);
    getContentPane().add(pan,BorderLayout.CENTER);
    getContentPane().add(okp,BorderLayout.SOUTH);
    jptv.addTableColorModifier();
  }

  private NavigationAdapter myNavListener = new NavigationAdapter() {
      public void navigated(DataSet ds) {
        rapancart1.forceLookup();
        rapancart2.forceLookup();
      }
  };

  raNavAction rnvUpdate = new raNavAction("Izmjena",raImages.IMGCHANGE,KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        doubleClicked();
      }
  };
/*
  raNavAction rnvZamjena = new raNavAction("Zamjena",raImages.IMGDELETE,KeyEvent.VK_F3) {
      public void actionPerformed(ActionEvent e) {
        zamjena();
      }
  };
*/
  public void doubleClicked(){
    inReplace = true;
    rcc.EnabDisabAll(rapancart2,true);
    rcc.setLabelLaF(kol,true);
    rcc.setLabelLaF(zamjena,true);
    rcc.EnabDisabAll(jptv,false);
    rapancart2.setFocus();
  }

  public raZamjenaArtikla(Frame frame,QueryDataSet ds,DataSet goodds,String cskl) {

    super (frame,true);
    this.cskl = cskl;
    goodset = goodds;

System.out.println("INICJALIZIRAM !!!");

    this.addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          pressESC();
          jptv.requestFocus();
        }
        else if (e.getKeyCode() == KeyEvent.VK_F10) {
          OKPres();
          jptv.requestFocus();
        }
      }
    });

    TCMartikl =  new hr.restart.swing.raTableColumnModifier("CART",
                               new String [] {"CART","NAZART"},
                               new String[] {"CART"},
                               dm.getArtikli().cloneDataSetView());
    TCMnormat =  new hr.restart.swing.raTableColumnModifier("CARTNOR",
                               new String [] {"CART","NAZART"},
                               new String[] {"CARTNOR"},
                               new String[] {"CART"},
                               dm.getArtikli().cloneDataSetView());
    TCMzam =  new hr.restart.swing.raTableColumnModifier("CARTZAM",
                               new String [] {"CART","NAZART"},
                               new String[] {"CARTZAM"},
                               new String[] {"CART"},
                               dm.getArtikli().cloneDataSetView());

    init();
    jptv.setDataSet(ds);
    jptv.setVisibleCols(new int[] {0,1,2,3});
    jptv.getNavBar().addOption(rnvUpdate);
//    jptv.getNavBar().addOption(rnvZamjena);
    jptv.initKeyListener(this);
    jptv.getNavBar().registerNavBarKeys(this);
    jptv.addTableModifier(TCMartikl);
    jptv.addTableModifier(TCMnormat);
    jptv.addTableModifier(TCMzam);
    jptv.getColumnsBean().setSaveName(getClass().getSuperclass().getName());
    jptv.getColumnsBean().setSaveSettings(true);
    jptv.getColumnsBean().eventInit();
    kol.setColumnName("KOLZAM");
    kol.setDataSet(ds);
    rcc.setLabelLaF(kol,false);
    rapancart1.setDataSet(ds);
    rcc.EnabDisabAll(rapancart1,false);
    rapancart2.setDataSet(ds);
    rapancart2.setParalelColumnCart("CARTZAM");
    rcc.EnabDisabAll(rapancart2,false);
    zamjena.getBinder().setDataSet(ds);
    rcc.setLabelLaF(zamjena,false);

    try {
      myNavListener.install(ds);
       //ds.addNavigationListener(myNavListener);
    } catch (Exception e){e.printStackTrace();}
    pack();
  }

  abstract public boolean testStanja(int cart,java.math.BigDecimal kolicina);

  public boolean Validacija() {

   if (val.isEmpty(rapancart2.jrfCART)) return false;
   if (jptv.getDataSet().getBigDecimal("KOLZAM").doubleValue()==0) {
     javax.swing.JOptionPane.showMessageDialog(null,
         "Koli\u010Dina ne smije biti nula !",
          "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
     return false;
   }
   if (!testStanja(jptv.getDataSet().getInt("CARTZAM"),jptv.getDataSet().getBigDecimal("KOLZAM"))) {
     javax.swing.JOptionPane.showMessageDialog(null,
         "Koli\u010Dina je manja nego na zalihi !",
          "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
     return false;
   }
   return true;
  }

  abstract public void setIndikator(boolean indikator);

  public void OKPres(){

    if (inReplace) {
    	System.out.println("Binderstatus "+zamjena.getBinder().getDataSet().getString("STATUS"));
    	
    	if (zamjena.isSelected()) {
    	
//      if (zamjena.getBinder().getDataSet().getString("STATUS").equalsIgnoreCase("A")){
        if (ld.raLocate(goodset,
                        new String[] {"RBSID", "CART", "CARTZAM"},
                        new String[] {String.valueOf(jptv.getDataSet().getInt("RBSID")),
                        String.valueOf(jptv.getDataSet().getInt("CART")),
                        String.valueOf(jptv.getDataSet().getInt("CARTZAM"))})) {
          inReplace = false;
          jptv.getDataSet().post();
          rcc.EnabDisabAll(rapancart2, false);
          rcc.setLabelLaF(zamjena, false);
          rcc.setLabelLaF(kol, false);
          rcc.EnabDisabAll(jptv, true);
          goodset.setString("STATUS", "Z");
          System.out.println("Oðe !!");
        }
      } else if (Validacija()) {
      	
        jptv.getDataSet().post();
        inReplace = false;
        rcc.EnabDisabAll(rapancart2,false);
        rcc.setLabelLaF(kol,false);
        rcc.setLabelLaF(zamjena, false);
        rcc.EnabDisabAll(jptv,true);
        if (ld.raLocate(goodset,new String[]
             {"RBSID","CART"},new String[] {
        		  String.valueOf(jptv.getDataSet().getInt("RBSID")),
                  String.valueOf(jptv.getDataSet().getInt("CART"))})){
           goodset.setBigDecimal("KOLZAM",jptv.getDataSet().getBigDecimal("KOLZAM"));
           goodset.setInt("CARTZAM",jptv.getDataSet().getInt("CARTZAM"));
           goodset.setString("STATUS","N");
           System.out.println("Debug 1");
        }
        
        System.out.println("Debug 2");
      }
    } else {
      if (testExit()) this.dispose();
    }
  }

  public void pressESC(){
    jptv.getDataSet().cancel();
    if (inReplace) {
      inReplace = false;
      rcc.EnabDisabAll(rapancart2,false);
      rcc.setLabelLaF(kol,false);
      rcc.setLabelLaF(zamjena,false);
      rcc.EnabDisabAll(jptv,true);
    } else {
      if (testExit()) this.dispose();
    }
  }

  public void CancelPres(){
    jptv.getDataSet().cancel();
    if (testExit()) this.dispose();
  }

  public void dispose() {
    if (jptv != null && jptv.getDataSet() != null)
      myNavListener.uninstall(jptv.getDataSet());
    super.dispose();
  }
  
  public boolean testExit() {

    boolean returnvalue = true;
//    DataSetView dv = jptv.getDataSet().cloneDataSetView();
    for(goodset.first();goodset.inBounds();goodset.next()) {
      System.out.println(goodset.getInt("CART") +" - testExit() STATUS-"+goodset.getString("STATUS")+"-");

//Z nepotrebno mijenjati
      if (!(goodset.getString("STATUS").equalsIgnoreCase("N") ||
            goodset.getString("STATUS").equalsIgnoreCase("Z"))) returnvalue = false;
    }
    if (!returnvalue) {
      if ((javax.swing.JOptionPane.showConfirmDialog(null,
          "Nisu zamijenjeni svi artikli zbilja želite iza\u0107i ? ","Upozorenje",
          javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.WARNING_MESSAGE)==0)){
        setIndikator(false);
        return true;
      }
      else return false;
    }
    setIndikator(true);
    return true;
  }

/*
  public void zamjena(){

//    ST.prn(goodset);

    String naziv = hr.restart.util.Util.getNewQueryDataSet("select nazart from artikli where cart="+
        jptv.getDataSet().getInt("CART"),true).getString("NAZART");


    if ((javax.swing.JOptionPane.showConfirmDialog(null,
        "Ovom akcijom proglasavate artikl "+naziv +" nepotrebnim za ovaj radni nalog ? ","Upozorenje",
        javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.WARNING_MESSAGE)==0)){
      if (ld.raLocate(goodset,new String[]
           {"RBSID","CART","CARTZAM"},new String[] {String.valueOf(jptv.getDataSet().getInt("RBSID")),
                                            String.valueOf(jptv.getDataSet().getInt("CART")),
                                            String.valueOf(jptv.getDataSet().getInt("CARTZAM"))})){
        goodset.setString("STATUS","Z");
      }
     }
  }
      }
*/
}
