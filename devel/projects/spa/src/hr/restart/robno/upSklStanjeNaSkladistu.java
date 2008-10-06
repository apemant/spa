/****license*****************************************************************
**   file: upSklStanjeNaSkladistu.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;


/**
 * @author S.G.
 *
 * Started 2005.04.18
 * 
 */

public class upSklStanjeNaSkladistu extends upStanjeNaSkladistu {
  
  public upSklStanjeNaSkladistu() {
    super();
/*

    jPanel3.add(jrbSaStanja, new XYConstraints(150, 72, -1, -1));
    jPanel3.add(jrbNaDan,  new XYConstraints(400, 72, -1, -1));
//
    jPanel3.add(jtfStanjeZaGod,    new XYConstraints(305, 75, 50, -1));
    jPanel3.add(jtfStanjeNaDan,     new XYConstraints(500, 75, 104, -1));
//
    jPanel3.add(jlStanje, new XYConstraints(15, 75, -1, -1));*/
    
    /*jrbSaStanja.setVisible(false);
    jrbNaDan.setVisible(false);
    jtfStanjeNaDan.setVisible(false);
    jtfStanjeZaGod.setVisible(false);
    jlStanje.setVisible(false);*/
    
    System.out.println("upSklStanjeNaSkladistu... OK"); //XDEBUG delete when no more needed
  }
  
  private int[] vcolsice;
//  private int[] viskol;

  protected void handleReports() {
    if (jrbSaStanja.isSelected()){
      if (!rpcskl.getCSKL().equals("")) {
        this.addReport("hr.restart.robno.repStanje", "hr.restart.robno.repSklStanje", "SkladStanje", "Ispis stanja");
      } else {
        this.addReport("hr.restart.robno.repStanjeSkl", "hr.restart.robno.repSklStanje", "SkladStanjeSkl", "Ispis stanja po skladištima");
        this.addReport("hr.restart.robno.repStanjeArt", "hr.restart.robno.repSklStanje", "SkladStanjeArt", "Ispis usporednog stanja artikala");
      }
    } else {
      this.addReport("hr.restart.robno.repStanjeSkl", "hr.restart.robno.repSklStanje", "SkladStanjeSkl", "Ispis stanja");
    }
  }

  public void jptv_doubleClick() {
    /*if (isNaDan()){
      nTransDate = tds.getTimestamp("NADAN");
      nTransDateOd = ut.getYearBegin(tds.getString("GODINA"));
//      System.out.println("ntrans date 1 " + nTransDate);
    } else {
      nTransDate = ut.getYearEnd(tds.getString("GODINA"));
      nTransDateOd = ut.getYearBegin(tds.getString("GODINA"));
//      System.out.println("ntrans date 2 " + nTransDate);
    }
    if (rpcskl.getCSKL().equals(""))
      nTransCskl = this.getJPTV().getMpTable().getDataSet().getString("CSKL");
    else
      nTransCskl = rpcskl.getCSKL();
    nTransData=this.getJPTV().getMpTable().getDataSet().getInt("CART");
    _Main.getStartFrame().showFrame("hr.restart.robno.upSkladKartica", res.getString("upFrmKartica_title"));
    */
    _Main.getStartFrame().showFrame("hr.restart.robno.upSkladKartica", 
        15, res.getString("upFrmKartica_title"), false);
    if (jrbNaDan.isSelected()){
      upSkladKartica.getupsKartica().setOutsideData(
          rpcskl.getCSKL(), getJPTV().getDataSet().getInt("CART"),
          ut.getFirstDayOfYear(tds.getTimestamp("NADAN")),
          tds.getTimestamp("NADAN"));
    } else {
      upSkladKartica.getupsKartica().setOutsideData(
          rpcskl.getCSKL(), getJPTV().getDataSet().getInt("CART"),
          ut.getYearBegin(tds.getString("GODINA")),
          ut.getToday(ut.getYearBegin(tds.getString("GODINA")), 
              ut.getYearEnd(tds.getString("GODINA"))));
    }
    _Main.getStartFrame().showFrame("hr.restart.robno.upSkladKartica", 
        res.getString("upFrmKartica_title"));
  }
  public void okPress() {
    lookupData.getlookupData().raLocate(dm.getSklad(), new String[] {"CSKL"}, new String[] {rpcskl.getCSKL()});
    vrzal = dm.getSklad().getString("VRZAL");
    mainDataSet.setSort(null);
    try {
      killAllReports();
    } catch (Exception e) {
      System.out.println("ne postoje custom reporti");
    }

    if (jrbSaStanja.isSelected()){ //isTrenutno()){
      handleReports();
      qStr = rde.defaultSkladStanje(rpcskl.getCSKL(), rpcart.findCART(podgrupe), tds.getString("GODINA"), tds.getString("SLJED"));
      mainDataSet.close();
      mainDataSet.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      mainDataSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
      mainDataSet.setColumns(new Column[] {
        (Column) dm.getStanje().getColumn("CSKL").clone(),
        (Column) dm.getArtikli().getColumn("CART").clone(),
        (Column) dm.getArtikli().getColumn("CART1").clone(),
        (Column) dm.getArtikli().getColumn("BC").clone(),
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getArtikli().getColumn("JM").clone(),
        (Column) dm.getStanje().getColumn("KOL").clone()
      });
      mainDataSet.getColumn("CART").setVisible(0);
      mainDataSet.getColumn("CART1").setVisible(0);
      mainDataSet.getColumn("BC").setVisible(0);
      mainDataSet.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);

      disableAdditionalColumns();

      openDataSet(mainDataSet);
      mainDataSet.first();

      if (mainDataSet.rowCount()==0) setNoDataAndReturnImmediately();
      if(/*jcbKolNull.isSelected()*/!jtbKolicinaNula.isSelected()){  
        mainDataSet = filterDataSet(mainDataSet);
        if (mainDataSet.rowCount()==0) setNoDataAndReturnImmediately();
      }
      
//    sysoutTEST sot = new sysoutTEST(false);
//    sot.showInFrame(mainDataSet,"");
      /*if (rpcskl.getCSKL().equals(""))
        viskol = new int[] {0,1,2,3,4,8};
      else
        viskol = new int[] {1,2,3,4,8};*/
    
      mainDataSet.last();
      this.getJPTV().setDataSetAndSums(mainDataSet, new String[] {"KOL"});
    } else {
      if (!rpcart.findCART(podgrupe).equals("")){
        qStr = rde.getSklSkladSqlPoj(rpcskl.getCSKL(), rpcart.findCART(podgrupe) , util.getTimestampValue(tds.getTimestamp("NADAN"), 1), tds.getString("SLJED"));
      } else {
        qStr = rde.getSklSkladSqlPoj(rpcskl.getCSKL(), "" , util.getTimestampValue(tds.getTimestamp("NADAN"), 1), tds.getString("SLJED"));
      }
//      this.addReport("hr.restart.robno.repStanjeSkl", "hr.restart.robno.repStanje", "StanjeSkl", "Ispis stanja");
      
      handleReports();
      
      System.out.println("UPIT "+qStr);

      mainDataSet.close();
      mainDataSet.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      mainDataSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
      mainDataSet.setColumns(new Column[] {
        (Column) dm.getStdoki().getColumn("BRDOK").clone(),
        (Column) dm.getStdoki().getColumn("VRDOK").clone(),
        (Column) dm.getStanje().getColumn("CART").clone(),
        (Column) dm.getArtikli().getColumn("CART1").clone(),
        (Column) dm.getArtikli().getColumn("BC").clone(),
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getArtikli().getColumn("JM").clone(),
        (Column) dm.getStanje().getColumn("KOL").clone()
      });
      mainDataSet.getColumn("BRDOK").setVisible(0);
      mainDataSet.getColumn("VRDOK").setVisible(0);
      String par = frmParam.getParam("robno","indiCart");
      if(par.equals("BC")) {
        mainDataSet.getColumn("CART").setVisible(0);
        mainDataSet.getColumn("CART1").setVisible(0);
      }
      else if (par.equals("CART1")) {
        mainDataSet.getColumn("CART").setVisible(0);
        mainDataSet.getColumn("BC").setVisible(0);
      } else {
        mainDataSet.getColumn("CART1").setVisible(0);
        mainDataSet.getColumn("BC").setVisible(0);
      }

      openDataSet(mainDataSet);

      mainDataSet.first();
      if (mainDataSet.rowCount()==0) setNoDataAndReturnImmediately();
      
      //mainDataSet= sumDSPoj(mainDataSet,null);
      QueryDataSet summ = new QueryDataSet();
      summ.setColumns(mainDataSet.cloneColumns());
      summ.open();
      mainDataSet.setSort(new SortDescriptor(new String[] {"CART"}));
      int oldc = 0;
      for (mainDataSet.first(), oldc = mainDataSet.getInt("CART") - 1; 
            mainDataSet.inBounds(); mainDataSet.next()) {
        if (oldc != mainDataSet.getInt("CART")) {
          summ.insertRow(false);
          mainDataSet.copyTo(summ);
          oldc = mainDataSet.getInt("CART");
        } else
          Aus.add(summ, "KOL", mainDataSet);
      }
      mainDataSet = summ;

      if(/*jcbKolNull.isSelected()*/!jtbKolicinaNula.isSelected()) mainDataSet = filterDataSet(mainDataSet);
      if (mainDataSet.rowCount()==0) setNoDataAndReturnImmediately();
      if (tds.getString("SLJED").equals("KOL")){
        mainDataSet.setSort(new SortDescriptor(new String[]{tds.getString("SLJED")},true,true));
      }
      
      System.out.println("--------------------------------------"); //XDEBUG delete when no more needed
      disableAdditionalColumns();
      System.out.println("--------------------------------------"); //XDEBUG delete when no more needed

//      viskol = new int[] {0,1,2,3,7};
      mainDataSet.last();
      this.getJPTV().setDataSetAndSums(mainDataSet, new String[] {"KOL"});
    
    }
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        rcc.setLabelLaF(jtfStanjeNaDan,false);
        rcc.setLabelLaF(jtfStanjeZaGod,false);
      }
    });
  }

  protected void disableAdditionalColumns() {
    if (jrbSaStanja.isSelected()){
      /*mainDataSet.getColumn("ZC").setVisible(0);
      mainDataSet.getColumn("NC").setVisible(0);  
      mainDataSet.getColumn("VC").setVisible(0);
      mainDataSet.getColumn("MC").setVisible(0);
      mainDataSet.getColumn("VRI").setVisible(0);
      mainDataSet.getColumn("KOLREZ").setVisible(0);*/
      if (rpcskl.getCSKL().equals("")) vcolsice = new int[]{0,1,2,3};
      else vcolsice = new int[]{1,2,3};
    } else {
      /*mainDataSet.getColumn("ZC").setVisible(0);
      mainDataSet.getColumn("NC").setVisible(0);  
      mainDataSet.getColumn("VC").setVisible(0);
      mainDataSet.getColumn("MC").setVisible(0);
      mainDataSet.getColumn("VRI").setVisible(0);*/
      vcolsice = new int[]{0,1,2};
    }
  }

  public int[] navVisibleColumns(){
    return vcolsice;
  }

  private QueryDataSet filterDataSet(QueryDataSet qds){
    qds.open();
    qds.first();
    do{
      if (qds.getBigDecimal("KOL").compareTo(new BigDecimal(0))==0
          /*|| qds.getBigDecimal("KOL").compareTo(new BigDecimal(0))==-1*/)
        qds.deleteRow();
      else
        qds.next();
    }while(qds.inBounds());
    return qds;
  }


}
