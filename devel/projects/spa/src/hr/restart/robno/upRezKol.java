/****license*****************************************************************
**   file: upRezKol.java
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
import hr.restart.sisfun.raUser;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class upRezKol extends raUpitFat {
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  TableDataSet tds = new TableDataSet();
  Valid vl = hr.restart.util.Valid.getValid();
  BorderLayout borderLayout1 = new BorderLayout();
  boolean podgrupe=false;
  private boolean doubleClicked = false; 
  private int position = 0;
  String defSKL = raUser.getInstance().getDefSklad();
  QueryDataSet upMas = new QueryDataSet() {
    public boolean refreshSupported() {
      return false;
    }
    public boolean saveChangesSupported() {
      return false;
    }
  };
  QueryDataSet upDet = new QueryDataSet();

  String oldCskl;
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  rapancskl rpcskl = new rapancskl(){
    public void findFocusAfter() {
      rpcart.setDefParam();
      rpcart.setCART();
    }
  };
  rapancart rpcart = new rapancart(){
    public void metToDo_after_lookUp() {
    }
    public void findFocusAfter() {
      getOKPanel().jPrekid.requestFocus();
    }
  };
  public upRezKol() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public boolean runFirstESC() {
    if (this.getJPTV().getDataSet() != null) return this.getJPTV().getDataSet() != null;
    else if (!rpcskl.getCSKL().equalsIgnoreCase("")) return true;
    return false;
    
  }
  public void firstESC() {
    if (doubleClicked) {
      doubleClicked = false;
      rpcart.setCART();
      System.out.println("tmpCskl " + tmpCskl);
      rpcskl.setCSKL(tmpCskl);
      upMas.empty();
      upDet.empty();
      this.getJPTV().clearDataSet();
      ok_action();
      this.getJPTV().enableEvents(false);
      this.getJPTV().getDataSet().goToRow(position);
      this.getJPTV().enableEvents(true);
     return; 
    } else {
      setDataSet(null);
      removeNav();
      if (!rpcart.getCART().equals("")) {
        rcc.EnabDisabAll(rpcart,true);
        rpcart.setCART();
      } else {
        rcc.EnabDisabAll(jp,true);
        rpcskl.setCSKL("");
        rpcskl.jrfCSKL.requestFocus();
      }
    }
  }
  
  public void componentShow() {
    oldCskl = null;
    rcc.EnabDisabAll(jp, true);
    showDefaultValues();
  }
  
  int upMasBrojColumn = 0;
  int upDetBrojColumn = 0;
  
  public void okPress() {
    String qStr="";
    String qStrSkladsStanje = "";
    String qStrSkladsPromet = "";
    String csklStanje = "";
    String csklPromet = "";
    
    if(!rpcskl.getCSKL().equals("")){
      qStrSkladsStanje = "stanje.cskl='" + rpcskl.getCSKL() + "' ";
      qStrSkladsPromet = "and stdoki.csklart='" + rpcskl.getCSKL() + "' ";
      upMasBrojColumn = 5;
      upDetBrojColumn = 5;
    } else {
      String sklads = "";
      QueryDataSet sks = hr.restart.robno.Util.getUtil().getSkladFromCorg();
      sks.first();
      
      for (;;) {
        sklads += sks.getString("CSKL");
        if (sks.next())
          sklads += ",";
        else {
          break;
        }
      }
      
      qStrSkladsStanje = "stanje.cskl in (" + sklads + ") ";
      qStrSkladsPromet = "and stdoki.csklart in (" + sklads + ") ";
      csklStanje = "stanje.cskl as cskl,";
      csklPromet = "(stdoki.csklart) as cskl,";
      upMasBrojColumn = 6;
      upDetBrojColumn = 6;
    }
    
    if (rpcart.getCART().equals("")) {
      qStr = "SELECT "+csklStanje+"stanje.cart as cart,artikli.cart1 as cart1,artikli.bc as bc,"+
	  	"artikli.nazart as nazart, stanje.kolrez as kolrez "+
		"FROM stanje,artikli where " +
        qStrSkladsStanje + " and " +
	    "stanje.god = '"+Aut.getAut().getKnjigodRobno()+"' and "+
        "artikli.cart=stanje.cart and stanje.kolrez>0 order by "+(rpcskl.getCSKL().equals("")? "stanje.cskl,":"")+" artikli.nazart COLLATE PXW_SLOV"; //Aut.getAut().getCARTdependable("cart","cart1","bc");
      
      System.out.println(qStr);
      
      upMas.close();
      upMas.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      upMas.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
      
      Column[] upMasCols = new Column[upMasBrojColumn];
      if (upMasBrojColumn == 6){
        upMasCols[0] = (Column) dm.getStanje().getColumn("CSKL").clone();
      }
      upMasCols[upMasBrojColumn-5] = (Column) dm.getArtikli().getColumn("CART").clone();
      upMasCols[upMasBrojColumn-4] = (Column) dm.getArtikli().getColumn("CART1").clone();
      upMasCols[upMasBrojColumn-3] = (Column) dm.getArtikli().getColumn("BC").clone();
      upMasCols[upMasBrojColumn-2] = (Column) dm.getArtikli().getColumn("NAZART").clone();
      upMasCols[upMasBrojColumn-1] = (Column) dm.getStanje().getColumn("KOLREZ").clone();
      
      upMas.setColumns( upMasCols);
      openDataSet(upMas);
      if (upMas.rowCount()==0) setNoDataAndReturnImmediately();
      upMas.first();
      setDataSet(upMas);
    } else {
      String cartStr;
      cartStr = rpcart.findCART("stdoki", podgrupe);
      qStr = "SELECT" +
      csklPromet + 
      "(stdoki.vrdok) as vrdok,"+
      "(stdoki.brdok) as brdok,"+
	  "(stdoki.rbr) as rbr,"+
      "(doki.cpar) as cpar,"+
      "(doki.ckupac) as ckupac,"+
      "(doki.datdok) as datdok,"+
      "(stdoki.kol) as kol "+
      
		"FROM doki,stdoki " +
        "WHERE " + rut.getDoc("DOKI", "STDOKI") + " " +
        qStrSkladsPromet + " and " +
	    "doki.god = '"+Aut.getAut().getKnjigodRobno()+"' and "+
		"stdoki.cart="+rpcart.getCART()+" and stdoki.rezkol='D' and doki.statira='N' and doki.vrdok='PON' order by "+(rpcskl.getCSKL().equals("")? "doki.cskl,":"")+"doki.brdok,stdoki.rbr";
     
      QueryDataSet tset = ut.getNewQueryDataSet(qStr);  //new QueryDataSet();

      if (tset.rowCount()==0) {
        setNoDataAndReturnImmediately();
      }
      tset.first();
      
      upDet.empty();
      upDet.close();
      
      Column[] upMasCols = new Column[upDetBrojColumn];
      if (upDetBrojColumn == 6){
        upMasCols[0] = (Column) dm.getStanje().getColumn("CSKL").clone();
      }
      upMasCols[upDetBrojColumn-5] = (Column) dm.getDoki().getColumn("VRDOK").clone();
      upMasCols[upDetBrojColumn-4] = (Column) dm.getDoki().getColumn("BRDOK").clone();
      upMasCols[upDetBrojColumn-3] = dm.createStringColumn("PK","Partner/kupac",0);
      upMasCols[upDetBrojColumn-2] = dm.createTimestampColumn("DATDOK","Datum");
      upMasCols[upDetBrojColumn-1] = dm.createBigDecimalColumn("KOL","Kolièina",2);
      
      upDet.setColumns(upMasCols);
      
      upDet.open();
      dm.getPartneri().open();
      dm.getKupci().open();
      
      try {
      
      do {
        upDet.insertRow(false);
        if (upDetBrojColumn == 6) upDet.setString("CSKL", tset.getString("CSKL"));
        upDet.setString("VRDOK", tset.getString("VRDOK"));
        upDet.setInt("BRDOK", tset.getInt("BRDOK"));
        upDet.setTimestamp("DATDOK", tset.getTimestamp("DATDOK"));
        upDet.setBigDecimal("KOL", tset.getBigDecimal("KOL"));
        if (tset.getInt("CPAR") != 0) {
          DataRow dr = hr.restart.util.lookupData.getlookupData().raLookup(hr.restart.baza.dM.getDataModule().getPartneri(),"CPAR", tset.getInt("CPAR")+"");
          upDet.setString("PK", dr.getString("NAZPAR"));
        } else if (tset.getInt("CKUPAC") != 0) {
          DataRow dr = hr.restart.util.lookupData.getlookupData().raLookup(hr.restart.baza.dM.getDataModule().getKupci(),"CKUPAC", tset.getInt("CKUPAC")+"");
          upDet.setString("PK", dr.getString("IME") + " " + dr.getString("PREZIME"));
        } else {
          upDet.setString("PK", "Kupac graðani");
        }
      } while (tset.next());
      setDataSetAndSums(upDet,new String[] {"KOL"});
      } catch (Exception sex){
       sex.printStackTrace(); 
      }
    }



  }
  private void jbInit() throws Exception {
    tds.setColumns(new Column[] {
        dM.createStringColumn("cskl", "Skladište", 12),
        dM.createIntColumn("cpar", "Partner"),
        dM.createIntColumn("cart", "Artikl"),
        dM.createStringColumn("cart1", "Oznaka", 20),
        dM.createStringColumn("bc", "Barcode", 20),
        dM.createStringColumn("cgrart", "Grupa artikla", 10),
        dM.createStringColumn("vrart", "Vrsta artikla", 1),
        dM.createStringColumn("nazart", "Naziv", 50)
    });
    tds.open();


    rpcskl.setRaMode('S');

    rpcart.setMode(new String("DOH"));
    rpcart.setBorder(null);
    jp.setLayout(borderLayout1);
    jp.add(rpcskl, BorderLayout.NORTH);
    jp.add(rpcart,  BorderLayout.CENTER);
    this.setJPan(jp);
  }
  
  public void showDefaultValues() {
    rpcskl.jrfCSKL.setRaDataSet(dm.getSklad());
    rpcart.setCART();
    upMas.empty();
    upDet.empty();
    this.getJPTV().clearDataSet();
    rpcskl.jrfCSKL.requestFocus();
  }
  public boolean Validacija() {
    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))){
      int grupe = JOptionPane.showConfirmDialog(this.jp,"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.CANCEL_OPTION) return false;
      if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
      else {podgrupe = true;}
    }
    return true;
  }
  public boolean isIspis() {
    return false;
  }
  
  public String navDoubleClickActionName(){
    return "Detaljni pregled rezervacija";
  }
  
  public int[] navVisibleColumns(){
    System.out.println(upMasBrojColumn + " " + upDetBrojColumn);
    if (rpcart.getCART().equals("")){
      if (upMasBrojColumn == 6)
        return new int[]{0,Aut.getAut().getCARTdependable(1,2,3),4,5};
      return new int[]{Aut.getAut().getCARTdependable(0,1,2),3,4};
    }
    if (upDetBrojColumn == 6)
      return new int[]{0,1,2,3,4,5};
    return new int[]{0,1,2,3,4};
  }
  
  private String tmpCskl = "";
  
  public void jptv_doubleClick() {
    if (rpcart.getCART().equals("")) {
      tmpCskl = rpcskl.getCSKL();
      doubleClicked = true;
      position = this.getJPTV().getDataSet().getRow();
      if (rpcskl.getCSKL().equals(""))rpcskl.setCSKL(this.getJPTV().getDataSet().getString("CSKL"));
      rpcart.setCART(this.getJPTV().getDataSet().getInt("CART"));
      upMas.empty();
      upDet.empty();
      this.getJPTV().clearDataSet();
      
      ok_action_thread();
    }
    else {
      //TODO buducnost
    }
  }

}
