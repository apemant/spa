/****license*****************************************************************
**   file: FrmPartneriArtikli.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.robno.Aut;
import hr.restart.robno.frmArtikli;
import hr.restart.robno.rapancart;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.util.Valid;
import hr.restart.util.raMatPodaci;
import hr.restart.util.sysoutTEST;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author S.G.
 *
 * Started 2005.10.10
 * 
 */

public class FrmPartneriArtikli extends raMatPodaci {
  QueryDataSet paramQDS = null;
  
  frmPartneri fP = null;
  int cpar;
  
  frmArtikli fA = null;  
  int cart;
  String cartOne, bc, nazart;
  
  String parent;

  JPanel jp = new JPanel();
  XYLayout myXyLayout = new XYLayout();

  dM dm;
  Valid vl;

  JraCheckBox jcbAktiv = new JraCheckBox();
  private JraTextField jraSifraStranca = new JraTextField();
  private JraTextField jraEANStranca = new JraTextField();
  private JraTextField jraNazart = new JraTextField();

  private jpCpar partnerPanel = new jpCpar() {
    public void afterLookUp(boolean succ) {
      if (succ) {
        jraSifraStranca.requestFocus();
        partnerPanel.EnabDisabAll(false);
      }
    }
  };
  
  protected rapancart rpcart = new rapancart(){
    public void metToDo_after_lookUp() {
      jraSifraStranca.requestFocus();
    }
    public void findFocusAfter() {
    }
  };
  
  public FrmPartneriArtikli(frmPartneri f, QueryDataSet tempParamQDS, int cpar){
    super(2);
    try {
      dm = dM.getDataModule();
      vl = Valid.getValid();
      
      fP = f;
      this.cpar=cpar;
      fP.setEnabled(false);
      paramQDS = tempParamQDS;
      paramQDS.open();
      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
      st.prn(paramQDS);
      parent = "Part";
      initializer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public FrmPartneriArtikli(frmArtikli a, QueryDataSet tempParamQDS, int cart, String cartOne, String bc, String nazart){
  super(2);
    try {
      dm = dM.getDataModule();
      vl = Valid.getValid();
      
      fA = a;
      this.cart = cart;
      this.cartOne = cartOne;
      this.bc = bc;
      this.nazart = nazart;
      fA.setEnabled(false);
      paramQDS = tempParamQDS;
      paramQDS.open();
      sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
      st.prn(paramQDS);
      parent = "Artik";
      initializer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void initializer() throws Exception {
    this.setTitle("Šifre artikala partnera iz " + parent);

    jp.setLayout(myXyLayout);
    
    this.setRaQueryDataSet(paramQDS);
    this.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5});
    
    myXyLayout.setWidth(645);

    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivna šifra");
    jcbAktiv.setUnselectedDataValue("N");

    jraSifraStranca.setColumnName("CCPAR");
    jraSifraStranca.setDataSet(getRaQueryDataSet());
    jraEANStranca.setColumnName("BCPAR");
    jraEANStranca.setDataSet(getRaQueryDataSet());
    jraNazart.setColumnName("PNAZART");
    jraNazart.setDataSet(getRaQueryDataSet());
    
    if (parent.equalsIgnoreCase("Part")){

      myXyLayout.setHeight(170);

      rpcart.setBorder(null);
      rpcart.setMode("DOH");
      rpcart.setReportMode();

      rpcart.setMyAfterLookupOnNavigate(false);
      rpcart.setFocusCycleRoot(true);
      rpcart.setTabela(getRaQueryDataSet());
      rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno", "indiCart"));
      rpcart.InitRaOst();
      
      jp.add(rpcart, new XYConstraints(0, 25, 630, 77));
      jp.add(jraSifraStranca,    new XYConstraints(150, 105, 150, -1));
      jp.add(new JLabel("Šifra/EAN kupca"),     new XYConstraints(15, 105, -1, -1));
      jp.add(jraEANStranca,    new XYConstraints(305, 105, 150, -1));
      jp.add(new JLabel("Naziv artikla"),     new XYConstraints(15, 130, -1, -1));
      jp.add(jraNazart,    new XYConstraints(150, 130, 455, -1));
      
    } else {

      myXyLayout.setHeight(125);
      
      partnerPanel.bind(getRaQueryDataSet());
      
      jp.add(partnerPanel, new XYConstraints(0, 32, 635, 25));
      jp.add(jraSifraStranca,    new XYConstraints(150, 62, 150, -1));
      jp.add(new JLabel("Šifra/EAN kupca"),     new XYConstraints(15, 62, -1, -1));
      jp.add(jraEANStranca,    new XYConstraints(305, 62, 150, -1));
      jp.add(new JLabel("Naziv artikla"),     new XYConstraints(15, 87, -1, -1));
      jp.add(jraNazart,    new XYConstraints(150, 87, 455, -1));
    }
    jp.add(jcbAktiv,    new XYConstraints(400, 7, 204, -1));
    
    this.setRaDetailPanel(jp);
  }

  public void SetFokus(char mode) {
    System.out.println("set Fikus"); //XDEBUG delete when no more needed
    if (mode == 'N') {
      if (parent.equalsIgnoreCase("Part")) {
        getRaQueryDataSet().setInt("CPAR", cpar);
        rpcart.SetFocus();
      } else {
        getRaQueryDataSet().setInt("CART", cart);
        getRaQueryDataSet().setString("CART1", cartOne);
        getRaQueryDataSet().setString("BC", bc);
        getRaQueryDataSet().setString("NAZART", nazart);
        //getRaQueryDataSet().setString("PNAZART", nazart); //TODO za sada....
        partnerPanel.clear();
        partnerPanel.cpar.requestFocus();
//        partnerPanel.cpar.requestFocus();
      }
    }
    if (mode == 'I') {
      System.out.println("Izmjena"); //XDEBUG delete when no more needed
      if (parent.equalsIgnoreCase("Part")) {
        rpcart.setCART(getRaQueryDataSet().getInt("CART"));
        rpcart.EnabDisab(false);
      } else {
        partnerPanel.setCpar(getRaQueryDataSet().getInt("CPAR"));
        partnerPanel.cpar.forceFocLost();
        partnerPanel.EnabDisabAll(false);
      }
      jraSifraStranca.requestFocus();
    }
    if (mode == 'B') {
      System.out.println("Browse"); //XDEBUG delete when no more needed
      if (parent.equalsIgnoreCase("Part")) {
        rpcart.setCART(getRaQueryDataSet().getInt("CART"));
      } else {
        partnerPanel.setCpar(getRaQueryDataSet().getInt("CPAR"));
        partnerPanel.cpar.forceFocLost();
      }
    }
  }

  public void EntryPoint(char mode) {
    System.out.println("ENTERY POINT"); //XDEBUG delete when no more needed
    if (mode == 'N') {
      if (parent.equalsIgnoreCase("Part")) {
        getRaQueryDataSet().setInt("CPAR", cpar);
        rpcart.SetFocus();
      } else {
        getRaQueryDataSet().setInt("CART", cart);
        getRaQueryDataSet().setString("CART1", cartOne);
        getRaQueryDataSet().setString("BC", bc);
        getRaQueryDataSet().setString("NAZART", nazart);
        //getRaQueryDataSet().setString("PNAZART", nazart); //TODO za sada....
        partnerPanel.clear();
        partnerPanel.cpar.requestFocus();
      }
    }
  }

  public void ExitPoint(char mode) {
    System.out.println("EXIT POINT"); //XDEBUG delete when no more needed
    if (mode == 'N') {
      if (parent.equalsIgnoreCase("Part")) {
        rpcart.setCART();
        rpcart.EnabDisab(true);
      } else {
        partnerPanel.clear();
        partnerPanel.EnabDisabAll(true);
      }
    }
  }

  public boolean Validacija(char mode) {
    System.out.println("\n\nparent - " + parent + "\n\n"); //XDEBUG delete when no more needed
    if (mode == 'N') {
      String pcart, pcpar;
      
      pcart = parent.equalsIgnoreCase("Part") ? rpcart.getCART() : cart+"";
      pcpar = parent.equalsIgnoreCase("Part") ? cpar+"" : partnerPanel.getCpar()+"";
      
      System.out.println("CART = "+pcart+" CPAR = "+pcpar);
      
      /*this.getJpTableView().enableEvents(false);
      boolean postojim = lookupData.getlookupData().raLocate(
          getRaQueryDataSet(), 
          new String[]{"CPAR", "CART"}, 
          new String[]{pcpar, pcart});
      this.getJpTableView().enableEvents(true);
      
      System.out.println("CART = "+pcart+" CPAR = "+pcpar+" : postojim? - " + postojim); //XDEBUG delete when no more needed
      if (postojim) {
        System.out.println("!!! VEC POSTOJI !!!"); //XDEBUG delete when no more needed
        return false;
      }*/
      if (parent.equalsIgnoreCase("Part")) {
        getRaQueryDataSet().setInt("CART", Integer.parseInt(rpcart.getCART()));
        getRaQueryDataSet().setString("CART1", rpcart.getCART1());
        getRaQueryDataSet().setString("BC", rpcart.getBC());
        getRaQueryDataSet().setString("NAZART", rpcart.getNAZART());
        //getRaQueryDataSet().setString("PNAZART", rpcart.getNAZART()); //TODO za sada....
      } else {
        getRaQueryDataSet().setInt("CPAR", partnerPanel.getCpar());
      }
    }
    System.out.println("jraSifraStranca is empty - " + vl.isEmpty(jraSifraStranca)); //XDEBUG delete when no more needed
    return !vl.isEmpty(jraSifraStranca);
  }
  
  public void ZatvoriOstalo() {
    if (parent.equalsIgnoreCase("Part")) {
      fP.setEnabled(true);
      fP.toFront();
    } else {
      fA.setEnabled(true);
      fA.toFront();
    }
  }

}
