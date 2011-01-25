/****license*****************************************************************
**   file: rdOSUtil.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.OS_Kontaisp;
import hr.restart.baza.OS_Kontrola;
import hr.restart.baza.OS_Lokacije;
import hr.restart.baza.OS_SI;
import hr.restart.baza.OS_Sredstvo;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.lookupData;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

/**
 * <p>Title: rdOSUtil</p>
 * <p>Description: Pomocne metode za osnovna sredstva</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class rdOSUtil {

  private static rdOSUtil myUtil;
  dM dm = dM.getDataModule();
  hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();


  public static rdOSUtil getUtil()
  {
    if(myUtil==null)
      myUtil = new rdOSUtil();

    return myUtil;
  }

  public rdOSUtil() {
  }
    public int getOSMaxRBR(String invBr, String knjig, String status)
    {
      QueryDataSet ds = new QueryDataSet();
      String qStr = "select max(rbr) as MAXRBR from os_promjene where invbroj='"+invBr+"'"+
                    " and corg='"+knjig+"' and status = '"+status+"'";
      ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      ds.open();
      return ds.getInt("MAXRBR");

    }

    public StorageDataSet getOrgStruktura(String corg)
    {
      StorageDataSet qds = new StorageDataSet();
      hr.restart.zapod.OrgStr knjOrgStr = hr.restart.zapod.OrgStr.getOrgStr();
      qds = knjOrgStr.getOrgstrAndKnjig(corg);
      qds.setSort(new SortDescriptor(new String[]{"CORG"}));
      return qds;
    }

  public boolean checkObjektStavke(String corg, String cobjekt)
  {
    String qStr = "corg = '"+corg+"' and cobjekt = '"+cobjekt+"'";
    return OS_Lokacije.getDataModule().getRowCount(qStr) > 0;
  }


    public QueryDataSet getLokacijeDS(String corg, boolean inCorg)
    {
      QueryDataSet qds = new QueryDataSet();

      qds.setColumns(new Column[]{
        (Column)dm.getOS_Lokacije().getColumn("LOKK").clone(),
        (Column)dm.getOS_Lokacije().getColumn("AKTIV").clone(),
        (Column)dm.getOS_Lokacije().getColumn("CLOKACIJE").clone(),
        (Column)dm.getOS_Lokacije().getColumn("CORG").clone(),
        (Column)dm.getOS_Lokacije().getColumn("COBJEKT").clone(),
        (Column)dm.getOS_Lokacije().getColumn("NAZLOKACIJE").clone()
      });

      String corging = "corg ";

//      if (inCorg){
//        corging += "in("+")";
//      } else {
        corging += "='"+corg+"' ";
//      }

      System.out.println("corging - " + corging);

      String qStr = "select * from os_lokacije where "+corging;
      Aus.setFilter(qds, qStr);
      qds.setSort(new SortDescriptor(new String[]{"CLOKACIJE"}));
      return qds;
    }

    public QueryDataSet getLokacijeRaDataSet()
    {
      QueryDataSet qds = new QueryDataSet();
      StorageDataSet sDS = new StorageDataSet();
      sDS = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
      String corg = "('";

      sDS.first();
      while (sDS.inBounds())
      {
        if(sDS.getRow()< sDS.getRowCount()-1)
          corg += sDS.getString("CORG") + "', '";
        else
          corg += sDS.getString("CORG") + "')";
        sDS.next();
      }

     qds.setColumns(new Column[] {
       (Column) hr.restart.baza.dM.getDataModule().getOS_Lokacije().getColumn("LOKK").clone(),
       (Column) hr.restart.baza.dM.getDataModule().getOS_Lokacije().getColumn("AKTIV").clone(),
       (Column) hr.restart.baza.dM.getDataModule().getOS_Lokacije().getColumn("CORG").clone(),
       (Column) hr.restart.baza.dM.getDataModule().getOS_Lokacije().getColumn("COBJEKT").clone(),
       (Column) hr.restart.baza.dM.getDataModule().getOS_Lokacije().getColumn("CLOKACIJE").clone(),
       (Column) hr.restart.baza.dM.getDataModule().getOS_Lokacije().getColumn("NAZLOKACIJE").clone()
     });
     String qStr = "select * from os_lokacije where corg in "+corg;

      qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      qds.open();
      return qds;
    }

    public QueryDataSet getObjektRaDataSet(String str)
    {
      QueryDataSet qds = new QueryDataSet();
      qds.setColumns(new Column[] {
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("LOKK").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("AKTIV").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("CORG").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("COBJEKT").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("NAZOBJEKT").clone()
      });
      String qStr = "select * from os_objekt where corg = '"+str+"'";

      qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      qds.open();
      return qds;
    }

    public QueryDataSet getKontoispRaDataSet(String str)
    {
      String qStr = "select * from os_kontaisp where corg = '"+str+"'";
      QueryDataSet qds = Util.getNewQueryDataSet(qStr, false);
      qds.setColumns(new Column[] {
        (Column) hr.restart.baza.dM.getDataModule().getOS_Kontaisp().getColumn("LOKK").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Kontaisp().getColumn("AKTIV").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Kontaisp().getColumn("CORG").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Kontaisp().getColumn("BROJKONTA").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Kontaisp().getColumn("KONTOISP").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Kontaisp().getColumn("KONTOAMOR").clone()
      });
      qds.open();
      return qds;
    }

    public QueryDataSet getObjektRaDataSet()
    {
      QueryDataSet qds = new QueryDataSet();
      StorageDataSet sDS = new StorageDataSet();
      sDS = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
      String corg = "('";

      sDS.first();
      while (sDS.inBounds())
      {
        if(sDS.getRow()< sDS.getRowCount()-1)
          corg += sDS.getString("CORG") + "', '";
        else
          corg += sDS.getString("CORG") + "')";
        sDS.next();
      }
      qds.setColumns(new Column[] {
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("LOKK").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("AKTIV").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("CORG").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("COBJEKT").clone(),
        (Column) hr.restart.baza.dM.getDataModule().getOS_Objekt().getColumn("NAZOBJEKT").clone()
      });
      String qStr = "select * from os_objekt where corg in "+corg;
      qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      qds.open();
      return qds;
    }

    public int getSIMaxRBR(String invBr, String knjig, String status)
    {
      QueryDataSet ds = new QueryDataSet();
      String qStr = "select max(rbr) as MAXRBR from os_stsi where invbroj='"+invBr+"'"+
                    " and corg='"+ knjig+"' and status='"+status+"'";
      ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      ds.open();
      return ds.getInt("MAXRBR");
    }

    public QueryDataSet getLikviDS()
    {
      String qStr="select * from os_vrpromjene where tippromjene='L'";
      return Util.getNewQueryDataSet(qStr, false);      
    }

    public QueryDataSet getCorgDS()
    {      
      String qStr="select * from os_vrpromjene where tippromjene='O'";
      return Util.getNewQueryDataSet(qStr, false);
    }

    public QueryDataSet getNonLikviDS()
    {
      String qStr="select * from os_vrpromjene where tippromjene='N'";
      return Util.getNewQueryDataSet(qStr, false);
    }

    public QueryDataSet getMyF9DS(String cOrg)
    {
//      System.out.println("CORG " + cOrg);
      String qStr = "select OS_LOKACIJE.CORG AS CORG, OS_LOKACIJE.CLOKACIJE AS CLOKACIJE, OS_LOKACIJE.NAZLOKACIJE AS NAZLOKACIJE "+
                    " from OS_LOKACIJE where OS_LOKACIJE.CORG ='"+cOrg+"'";
      return Util.getNewQueryDataSet(qStr, false);      
    }

    public boolean getInvBr(String invBr, char mod, String cOrg)
    {      
      if (mod=='S' || mod=='N' || mod=='L' || mod=='P')
      	return OS_Sredstvo.getDataModule().getRowCount("invbroj ='" + invBr +"' and corg2 ='"+ cOrg + "'") > 0;        
      else if (mod=='X' || mod=='Y' || mod=='Z')
      	return OS_SI.getDataModule().getRowCount("invbroj ='" + invBr +"' and corg2 ='"+ cOrg + "'") > 0;
      return false;
    }

    public String getDSRow(int rbr, String cPromjene)
    {
    	String qStr="select TIPPROMJENE as TIPPROMJENE from  OS_VRPROMJENE"+
	      " where OS_VRPROMJENE.CPROMJENE='" +cPromjene +"'";
      QueryDataSet tempDS = Util.getNewQueryDataSet(qStr);      
      return tempDS.getString("TIPPROMJENE");
    }

    public String getDetailStrIspis(String invBr, String corg, String status, String aktiv, char mod)
    {
//     System.out.println("STATUS: " + status);
      String qStr = "select os_sredstvo.corg2 as corg2, os_sredstvo.invbroj as invbroj, os_sredstvo.nazsredstva as nazsredstva,"+
                    "os_sredstvo.clokacije as clokacije, os_sredstvo.cgrupe as cgrupe, os_sredstvo.cskupine as cskupine,"+
                    "os_sredstvo.brojkonta as brojkonta, os_sredstvo.cpar as cpar, os_sredstvo.cartikla as cartikla, "+
                    "os_sredstvo.cobjekt as cobjekt, os_sredstvo.cradnik as cradnik, os_sredstvo.datnabave as datnab, "+
                    "os_sredstvo.dataktiviranja as datakt, os_sredstvo.datlikvidacije as datlikvidacije, os_sredstvo.status as status, "+
//                    "os_promjene.aktiv as aktiv, os_promjene.dokument as dokument, os_sredstvo.datpromjene as datumpromjene, os_sredstvo.porijeklo as porijeklo,"+
                    "'"+aktiv+"' as aktiv, os_promjene.dokument as dokument, os_sredstvo.datpromjene as datumpromjene, os_sredstvo.porijeklo as porijeklo,"+
                    "os_sredstvo.nabvrijed as nabvrijed, os_promjene.rbr as rbr, os_promjene.datpromjene as datpromjene, os_promjene.cpromjene as cpromjene,"+
                    "os_promjene.osnduguje as osnduguje, os_promjene.osnpotrazuje as osnpotrazuje, os_promjene.ispduguje as ispduguje,"+
                    "os_promjene.isppotrazuje as isppotrazuje, os_promjene.saldo as saldo from os_sredstvo, os_promjene"+
                    " where os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.corg2=os_promjene.corg2 and os_sredstvo.status=os_promjene.status "+
                    "and os_promjene.invbroj='"+invBr+"' "+" and os_promjene.status='"+status+"' and "+getPripOrg(corg, "os_promjene");//"' and os_promjene.corg2='"+corg+"'" ;
//     System.out.println("KARTICA OS: " + qStr);
      return qStr;
    }

    public String getMasterStrIspis(String cOrg, String status, char mod)
    {

      System.out.println("corg " + cOrg + " status " + status + " mod " + mod);
      String qStr = "select corg2 as corg2, invbroj as invbr, nazsredstva as nazsredstva, datpromjene as datum, "+
                    "osnduguje as osnduguje, osnpotrazuje as osnpotrazuje, ispduguje as ispduguje, isppotrazuje as isppotrazuje, status as status "+
                    " from os_sredstvo"+
//                    " where corg='"+cOrg+"' and status='A'"+
//                    " where status='"+status+"' and " + getPripOrg(cOrg,"OS_SREDSTVO");
                    " where status='"+status+"' and " + getPripOrg(cOrg,"OS_SREDSTVO");
      System.out.println("QSTR: " + qStr);
      return qStr;
    }

    public String getOSIspis(String cOrg, String kumulativi, int inv)
    {
      String qStr = "";
      if(kumulativi.equals("N"))
      {
        qStr = "select max(os_sredstvo.dokument) as dokument, max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
               "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_promjene.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_promjene.datpromjene) as datum, "+
               "sum(os_promjene.osnduguje) as osnduguje, sum(os_promjene.osnpotrazuje) as osnpotrazuje, sum(os_promjene.ispduguje) as ispduguje, sum(os_promjene.isppotrazuje) as isppotrazuje, "+
               "sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.isppocetak) as isppocetak from os_sredstvo, os_promjene where os_promjene.corg2 = os_sredstvo.corg2 and os_promjene.invbroj = os_sredstvo.invbroj and os_promjene.corg2='"+cOrg+"'";
        qStr += getOrderBy(inv, ispOS.getSelectedRB(),"os_sredstvo");
      }
      else
      {
        qStr= "select max(os_sredstvo.nazsredstva)as nazsredstva, max(os_sredstvo.corg2)as corg, max(os_sredstvo.invbroj) as invbroj, sum(os_promjene.osnduguje) as osnduguje,"+
              "sum(os_promjene.osnpotrazuje)as osnpotrazuje,sum(os_sredstvo.osnpocetak)as osnpocetak, sum(os_promjene.ispduguje)as ispduguje, sum(os_promjene.isppotrazuje)as isppotrazuje,"+
              "sum(os_sredstvo.isppocetak)as isppocetak from os_sredstvo, os_promjene where os_sredstvo.corg2 = os_promjene.corg2 and os_promjene.corg2='"+cOrg+"' ";
        if (inv==4) qStr  += " group by os_sredstvo.invbroj";
        else  qStr += " group by os_sredstvo.corg2";

      }
      return qStr;
    }

    public String getOSIspis(String kumulativi, int inv)
    {
      String qStr="";
      if(kumulativi.equals("N"))
      {
        qStr = "select max(os_sredstvo.dokument) as dokument, max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
               "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_promjene.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_promjene.datpromjene) as datum, "+
               "sum(os_promjene.osnduguje) as osnduguje, sum(os_promjene.osnpotrazuje) as osnpotrazuje, sum(os_promjene.ispduguje) as ispduguje, sum(os_promjene.isppotrazuje) as isppotrazuje, "+
               "sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.isppocetak) as isppocetak from os_sredstvo, os_promjene where os_promjene.corg2 = os_sredstvo.corg2 and os_promjene.invbroj = os_sredstvo.invbroj";

        qStr += getOrderBy(inv, ispOS.getSelectedRB(),"os_sredstvo");
      }
      else
      {
        qStr= "select max(os_sredstvo.nazsredstva)as nazsredstva, max(os_sredstvo.corg2)as corg, max(os_sredstvo.invbroj) as invbroj, sum(os_promjene.osnduguje) as osnduguje,"+
              "sum(os_promjene.osnpotrazuje)as osnpotrazuje,sum(os_sredstvo.osnpocetak)as osnpocetak, sum(os_promjene.ispduguje)as ispduguje, sum(os_promjene.isppotrazuje)as isppotrazuje,"+
              "sum(os_sredstvo.isppocetak)as isppocetak from os_sredstvo, os_promjene where os_sredstvo.corg2 = os_promjene.corg2"+
              " group by os_sredstvo.corg2";
      }
      return qStr;
    }

    public String getOSIspTrSt(String pocDat, String zavDat, String kumulativi, int inv)
    {
      String qStr="";
      if(kumulativi.equals("N"))
      {
        qStr=  "select max(os_promjene.dokument) as dokument, max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
               "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_promjene.datpromjene) as datum, "+
               "sum(os_promjene.osnduguje) as osnduguje, sum(os_promjene.osnpotrazuje) as osnpotrazuje, sum(os_promjene.ispduguje) as ispduguje, sum(os_promjene.isppotrazuje) as isppotrazuje "+
               "from os_sredstvo, os_promjene where os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.corg2= os_promjene.corg2 "+
               " and os_promjene.datpromjene >= "+pocDat+" and os_promjene.datpromjene <= "+zavDat;
        qStr += getOrderBy(inv, ispOS.getSelectedRB(),"os_sredstvo");
      }
      else
      {
        qStr= "select max(os_sredstvo.nazsredstva)as nazsredstva, max(os_sredstvo.corg2)as corg, max(os_sredstvo.invbroj) as invbroj, sum(os_promjene.osnduguje) as osnduguje,"+
              "sum(os_promjene.osnpotrazuje)as osnpotrazuje,sum(os_sredstvo.osnpocetak)as osnpocetak, sum(os_promjene.ispduguje)as ispduguje, sum(os_promjene.isppotrazuje)as isppotrazuje,"+
              "sum(os_sredstvo.isppocetak)as isppocetak from os_sredstvo, os_promjene where os_sredstvo.corg2 = os_promjene.corg2 "+
              " and os_promjene.datpromjene >= "+pocDat+" and os_promjene.datpromjene <= "+zavDat+" "+
              " group by os_sredstvo.corg2";
      }
      return qStr;
    }

    public String getOSIspTrSt(String cOrg, String pocDat, String zavDat, String kumulativi, int inv)
    {
      String qStr="";
      if(kumulativi.equals("N"))
      {
        qStr=  "select max(os_promjene.dokument) as dokument, max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
               "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_promjene.datpromjene) as datum, "+
               "sum(os_promjene.osnduguje) as osnduguje, sum(os_promjene.osnpotrazuje) as osnpotrazuje, sum(os_promjene.ispduguje) as ispduguje, sum(os_promjene.isppotrazuje) as isppotrazuje "+
               "from os_sredstvo, os_promjene where os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.corg2= os_promjene.corg2 "+
               " and os_promjene.datpromjene >= "+pocDat+" and os_promjene.datpromjene <= "+zavDat+" and os_promjene.corg2= '"+cOrg+"'";
        qStr += getOrderBy(inv, ispOS.getSelectedRB(),"os_sredstvo");
      }
      else
      {
        qStr= "select max(os_sredstvo.nazsredstva)as nazsredstva, max(os_sredstvo.corg2)as corg, max(os_sredstvo.invbroj) as invbroj, sum(os_promjene.osnduguje) as osnduguje,"+
              "sum(os_promjene.osnpotrazuje)as osnpotrazuje,sum(os_sredstvo.osnpocetak)as osnpocetak, sum(os_promjene.ispduguje)as ispduguje, sum(os_promjene.isppotrazuje)as isppotrazuje,"+
              "sum(os_sredstvo.isppocetak)as isppocetak from os_sredstvo, os_promjene where os_sredstvo.corg2 = os_promjene.corg2 "+
              " and os_promjene.datpromjene >= "+pocDat+" and os_promjene.datpromjene <= "+zavDat+" and os_promjene.corg2= '"+cOrg+"'"+
              " group by os_sredstvo.corg2";
      }
      return qStr;
    }

    public String getTipPromjene(String cProm)
    {
      QueryDataSet qds = new QueryDataSet();
      String qStr ="select tippromjene from os_vrpromjene where cpromjene = '"+cProm+"'";
      qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      qds.open();
      String ret = qds.getString("TIPPROMJENE");
      qds.close();
      return ret;
    }

    public String deleteLikvidacija(String tablica, String cOrg, String invBroj)
    {
      String qStr ="update "+ tablica +" set datlikvidacije = null where corg='"+cOrg+"' "+
                      "and invbroj='"+invBroj+"'";
      return qStr;
    }

    public int getOldRows(String cOrg, String invBroj)
    {
      QueryDataSet qds = new QueryDataSet();
      String qStr ="select * from os_promjene where corg = '"+ cOrg +"' and invbroj='"+invBroj+"'";
      qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
      qds.open();
      int ret = qds.getRowCount();
      qds.close();
      return ret;
    }

    //************************** S I T N I  I N V E N T A R

     public String getSIIspis(String cOrg, String kumulativi, int inv)
    {
      String qStr = "";
      if(kumulativi.equals("N"))
      {
        qStr = "select max(os_si.dokument) as dokument, max(os_si.corg) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
               "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_stsi.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_stsi.datpromjene) as datum, "+
               "sum(os_stsi.osnduguje) as osnduguje, sum(os_stsi.osnpotrazuje) as osnpotrazuje, sum(os_stsi.ispduguje) as ispduguje, sum(os_stsi.isppotrazuje) as isppotrazuje, "+
               "sum(os_si.osnpocetak) as osnpocetak, sum(os_si.isppocetak) as isppocetak from os_si, os_stsi where os_stsi.corg = os_si.corg and os_stsi.invbroj = os_si.invbroj and os_stsi.corg='"+cOrg+"'";
        qStr += getOrderBy(inv, ispSI.getSelectedRB(),"os_si");
      }
      else
      {
        qStr= "select max(os_si.nazsredstva)as nazsredstva, max(os_si.corg)as corg, max(os_si.invbroj) as invbroj, sum(os_stsi.osnduguje) as osnduguje,"+
              "sum(os_stsi.osnpotrazuje)as osnpotrazuje,sum(os_si.osnpocetak)as osnpocetak, sum(os_stsi.ispduguje)as ispduguje, sum(os_stsi.isppotrazuje)as isppotrazuje,"+
              "sum(os_si.isppocetak)as isppocetak from os_si, os_stsi where os_si.corg = os_stsi.corg and os_si.invbroj = os_stsi.invbroj and os_stsi.corg='"+cOrg+"' "+
              " group by os_si.corg";
      }
      return qStr;
    }

    public String getSIIspis(String kumulativi, int inv)
    {
      String qStr = "";
      if(kumulativi.equals("N"))
      {
          qStr = "select max(os_si.dokument) as dokument, max(os_si.corg) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
                 "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_stsi.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_stsi.datpromjene) as datum, "+
                 "sum(os_stsi.osnduguje) as osnduguje, sum(os_stsi.osnpotrazuje) as osnpotrazuje, sum(os_stsi.ispduguje) as ispduguje, sum(os_stsi.isppotrazuje) as isppotrazuje, "+
                 "sum(os_si.osnpocetak) as osnpocetak, sum(os_si.isppocetak) as isppocetak from os_si, os_stsi where os_stsi.corg = os_si.corg and os_stsi.invbroj = os_si.invbroj";

          qStr += getOrderBy(inv, ispSI.getSelectedRB(),"os_si");
      }
      else
      {
        qStr= "select max(os_si.nazsredstva)as nazsredstva, max(os_si.corg)as corg, max(os_si.invbroj) as invbroj, sum(os_stsi.osnduguje) as osnduguje,"+
              "sum(os_stsi.osnpotrazuje)as osnpotrazuje,sum(os_si.osnpocetak)as osnpocetak, sum(os_stsi.ispduguje)as ispduguje, sum(os_stsi.isppotrazuje)as isppotrazuje,"+
              "sum(os_si.isppocetak)as isppocetak from os_si, os_stsi where os_si.corg = os_stsi.corg and os_si.invbroj = os_stsi.invbroj"+
              " group by os_si.corg";

      }
      return qStr;
    }

    public String getSIIspTrSt(String pocDat, String zavDat, String kumulativi, int inv)
    {
      String qStr = "";
      if(kumulativi.equals("N"))
      {
          qStr=  "select max(os_stsi.dokument) as dokument, max(os_si.corg) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
                 "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_stsi.datpromjene) as datum, "+
                 "sum(os_stsi.osnduguje) as osnduguje, sum(os_stsi.osnpotrazuje) as osnpotrazuje, sum(os_stsi.ispduguje) as ispduguje, sum(os_stsi.isppotrazuje) as isppotrazuje "+
                 "from os_si, os_stsi where os_si.invbroj=os_stsi.invbroj and os_si.corg= os_stsi.corg "+
                 " and os_stsi.datpromjene >= "+pocDat+" and os_stsi.datpromjene <= "+zavDat;
          qStr += getOrderBy(inv, ispSI.getSelectedRB(),"os_si");
      }
      else
      {
        qStr= "select max(os_si.nazsredstva)as nazsredstva, max(os_si.corg)as corg, max(os_si.invbroj) as invbroj, sum(os_stsi.osnduguje) as osnduguje,"+
              "sum(os_stsi.osnpotrazuje)as osnpotrazuje,sum(os_si.osnpocetak)as osnpocetak, sum(os_stsi.ispduguje)as ispduguje, sum(os_stsi.isppotrazuje)as isppotrazuje,"+
              "sum(os_si.isppocetak)as isppocetak from os_si, os_stsi where os_si.corg = os_stsi.corg "+
              " and os_stsi.datpromjene >= "+pocDat+" and os_stsi.datpromjene <= "+zavDat+" "+
              " group by os_si.corg";
      }
      return qStr;
    }

    public String getSIIspTrSt(String cOrg, String pocDat, String zavDat, String kumulativi, int inv)
    {
      String qStr = "";
      if(kumulativi.equals("N"))
      {
        qStr= "select max(os_stsi.dokument) as dokument, max(os_si.corg) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
              "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_stsi.datpromjene) as datum, "+
              "sum(os_stsi.osnduguje) as osnduguje, sum(os_stsi.osnpotrazuje) as osnpotrazuje, sum(os_stsi.ispduguje) as ispduguje, sum(os_stsi.isppotrazuje) as isppotrazuje "+
              "from os_si, os_stsi where os_si.invbroj=os_stsi.invbroj and os_si.corg= os_stsi.corg "+
              " and os_stsi.datpromjene >= "+pocDat+" and os_stsi.datpromjene <= "+zavDat+" and os_stsi.corg = '"+cOrg+"'";
        qStr += getOrderBy(inv, ispSI.getSelectedRB(),"os_si");
      }
      else
      {
        qStr= "select max(os_si.nazsredstva)as nazsredstva, max(os_si.corg)as corg, max(os_si.invbroj) as invbroj, sum(os_stsi.osnduguje) as osnduguje,"+
              "sum(os_stsi.osnpotrazuje)as osnpotrazuje,sum(os_si.osnpocetak)as osnpocetak, sum(os_stsi.ispduguje)as ispduguje, sum(os_stsi.isppotrazuje)as isppotrazuje,"+
              "sum(os_si.isppocetak)as isppocetak from os_si, os_stsi where os_si.corg = os_stsi.corg and os_si.invbroj = os_stsi.invbroj"+
              " and os_stsi.datpromjene >= "+pocDat+" and os_stsi.datpromjene <= "+zavDat+" "+" and os_stsi.corg = '"+cOrg+"'"+
              " group by os_si.corg";
      }
      return qStr;
    }


    //******************************* A M O R T I Z A C I J A

    public String getAmorIspis(String cOrg, String kumulativi, int inv)
    {
      String qStr = "";
      if(kumulativi.equals("N"))
      {
        qStr ="select max(os_metaobrada.tipamor) as tipamor, max(os_obrada2.corg) as corg, max(os_obrada2.clokacije) as clokacije, max(os_obrada2.cartikla) as cartikla, max(os_obrada2.brojkonta) as brojkonta, max(os_obrada2.invbroj) as invbroj, "+
              "max(os_obrada2.cgrupe) as cgrupe, max(os_obrada2.cskupine) as cskupine, max(os_obrada2.nazsredstva) as nazsredstva, sum(os_obrada2.zakstopa) as zakstopa, sum(os_obrada2.odlstopa) as odlstopa, "+
              "max(os_obrada2.datpromjene) as datpromjene, sum(os_obrada2.osnovica) as osnovica, sum(os_obrada2.ispravak) as ispravak, sum(os_obrada2.sadvrijed) as sadvrijed, "+
              "sum(os_obrada2.amortizacija) as amortizacija, sum(os_obrada2.pamortizacija) as pamortizacija, max(os_metaobrada.datumod) as datumod, max(os_metaobrada.datumdo) as datumdo from os_obrada2, os_metaobrada"+
              " where os_metaobrada.corg = os_obrada2.corg and os_obrada2.corg='"+cOrg+"'";
        qStr += getOrderBy(inv, ispAmor.getSelectedRB(),"os_obrada2");
      }
      else
      {
        qStr= "select max(os_metaobrada.tipamor) as tipamor, max(nazsredstva)as nazsredstva, max(corg)as corg, max(invbroj) as invbroj, sum(osnovica) as osnovica, sum(ispravak) as ispravak,"+
              "sum(amortizacija)as amortizacija, sum(pamortizacija)as pamortizacija, sum(sadvrijed)as sadvrijed, max(os_metaobrada.datumod) as datumod, max(os_metaobrada.datumdo) as datumdo "+
              "from os_obrada2, os_metaobrada where os_metaobrada.corg = os_obrada2.corg and corg='"+cOrg+"' group by os_obrada2.corg";
      }
      return qStr;

    }

    public String getAmorIspis(String kumulativi, int inv)
    {
      String qStr="";
      if(kumulativi.equals("N"))
      {
       qStr ="select max(os_obrada2.corg) as corg, max(os_obrada2.clokacije) as clokacije, max(os_obrada2.cartikla) as cartikla, max(os_obrada2.brojkonta) as brojkonta, max(os_obrada2.invbroj) as invbroj, "+
              "max(os_obrada2.cgrupe) as cgrupe, max(os_obrada2.cskupine) as cskupine, max(os_obrada2.nazsredstva) as nazsredstva, sum(os_obrada2.zakstopa) as zakstopa, sum(os_obrada2.odlstopa) as odlstopa, "+
              "max(os_obrada2.datpromjene) as datpromjene, sum(os_obrada2.osnovica) as osnovica, sum(os_obrada2.ispravak) as ispravak, sum(os_obrada2.sadvrijed) as sadvrijed, "+
              "sum(os_obrada2.amortizacija) as amortizacija, sum(os_obrada2.pamortizacija) as pamortizacija, max(os_metaobrada.datumod) as datumod, max(os_metaobrada.datumdo) as datumdo from os_obrada2, os_metaobrada"+
              " where os_metaobrada.corg = os_obrada2.corg";


        qStr += getOrderBy(inv, ispAmor.getSelectedRB(),"os_obrada2");
      }
      else
      {
        qStr= "select max(nazsredstva)as nazsredstva, max(corg)as corg, max(invbroj) as invbroj, sum(osnovica) as osnovica, sum(ispravak) as ispravak,"+
              "sum(amortizacija)as amortizacija, sum(pamortizacija)as pamortizacija, sum(sadvrijed)as sadvrijed, max(os_metaobrada.datumod) as datumod, max(os_metaobrada.datumdo) as datumdo "+
              "from os_obrada2, os_metaobrada where os_metaobrada.corg = os_obrada2.corg  group by os_obrada2.corg";
      }
      return qStr;
    }

    public String getOrderBy (int inv, int selRB, String tabl)
    {
      String qStr = "";
      String tab = tabl+".";
      if (inv == 1) qStr = " group by "+tab+"corg";
      else if(inv == 2) qStr = " group by "+oblikIspisa(selRB, tabl);
      else if(inv == 3) qStr = " group by "+tab+"corg, "+oblikIspisa(selRB, tabl);
      else if(inv == 4) qStr = " group by "+tab+"invbroj";
      else if(inv == 5) qStr = " group by "+tab+"corg, "+tab+"invbroj";
      else if(inv == 6) qStr = " group by "+oblikIspisa(selRB,tabl)+", "+tab+"invbroj";
      else if(inv == 7) qStr = " group by "+tab+"corg, "+oblikIspisa(selRB,tabl)+", "+tab+"invbroj";
      return qStr;
    }

    public String oblikIspisa(int caseRB, String tabl)
    {
      String caseRb="";
      switch (caseRB) {
        case 0:
          caseRb=" brojkonta";
          break;
        case 1:
          caseRb=" cgrupe";
          break;
        case 2:
          caseRb=" clokacije";
          break;
        case 3:
          caseRb=" cskupine";
          break;
        case 4:
          caseRb=" cartikla";
          break;
      }
      return tabl+"."+caseRb;
    }

    //****************** K U M U L A T I V I

    public String getAmorOrgKum(StorageDataSet sds)
    {
      String qStr= "";
      sds.open();
      sds.first();
      for(int i=0; i<sds.getRowCount();i++)
      {
        qStr =qStr + "select max(os_metaobrada.tipamor) as tipamor, max(os_obrada2.corg)as corg, max(os_obrada2.invbroj) as invbroj, sum(os_obrada2.osnovica) as osnovica, sum(os_obrada2.ispravak) as ispravak,"+
              "sum(os_obrada2.amortizacija)as amortizacija, sum(os_obrada2.pamortizacija)as pamortizacija, sum(os_obrada2.sadvrijed)as sadvrijed, "+
              "max(os_metaobrada.datumod) as datumod, max(os_metaobrada.datumdo) as datumdo "+
              "from os_obrada2, os_metaobrada  where os_obrada2.corg=os_metaobrada.corg and os_obrada2.corg='"+sds.getString("CORG")+"' group by corg";
        if(i != sds.getRowCount()-1)
          qStr = qStr + " union ";
        sds.next();
      }
      return qStr;
    }

    public String getSIOrgKum(StorageDataSet sds, String pocDat, String zavDat)
    {
      String qStr= "";
      sds.open();
      sds.first();
      for(int i=0; i<sds.getRowCount();i++)
      {
        qStr =qStr + "select max(os_si.corg)as corg, max(os_si.invbroj) as invbroj, sum(os_stsi.osnduguje) as osnduguje,"+
              "sum(os_stsi.osnpotrazuje)as osnpotrazuje,sum(os_si.osnpocetak)as osnpocetak, sum(os_stsi.ispduguje)as ispduguje, sum(os_stsi.isppotrazuje)as isppotrazuje,"+
              "sum(os_si.isppocetak)as isppocetak "+
              "from os_si, os_stsi  where os_si.corg=os_stsi.corg and os_si.invbroj=os_stsi.invbroj and os_si.corg='"+sds.getString("CORG")+"'";
         if (!pocDat.equals(""))
          qStr = qStr +" and os_stsi.datpromjene >= "+pocDat+" and os_stsi.datpromjene <= "+zavDat;
        if(i != sds.getRowCount()-1)
          qStr = qStr + " union ";
        sds.next();
      }
      return qStr;
    }

    public String getOSOrgKum(StorageDataSet sds, String pocDat, String zavDat)
    {
      String qStr= "";
      sds.open();
      sds.first();
      for(int i=0; i<sds.getRowCount();i++)
      {
        qStr =qStr + "select max(os_sredstvo.corg2)as corg, max(os_sredstvo.invbroj) as invbroj, sum(os_promjene.osnduguje) as osnduguje,"+
              "sum(os_promjene.osnpotrazuje)as osnpotrazuje,sum(os_sredstvo.osnpocetak)as osnpocetak, sum(os_promjene.ispduguje)as ispduguje, sum(os_promjene.isppotrazuje)as isppotrazuje,"+
              "sum(os_sredstvo.isppocetak)as isppocetak "+
              "from os_sredstvo, os_promjene  where os_sredstvo.corg2=os_promjene.corg2 and os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.corg2='"+sds.getString("CORG")+"'";
        if (!pocDat.equals(""))
          qStr = qStr +" and os_promjene.datpromjene >= "+pocDat+" and os_promjene.datpromjene <= "+zavDat;
        if(i != sds.getRowCount()-1)
          qStr = qStr + " union ";
        sds.next();
      }
      return qStr;
    }

    //************rekapitulacja
    public String getRekapitulacija(String inCorg2, String pocDat, String zavDat, String sKonto, int gb)
    {
      String qStr= "";
//      sds.open();
//      Condition inCorgs = Condition.in("",sds);
//      System.out.println("IN = " + inCorgs);
//      sds.first();
//      for(int i=0; i<sds.getRowCount();i++)
//      {
//        qStr = qStr + "select max(os_promjene.cpromjene) as cpromjene, max(brojkonta)as brojkonta, max(os_sredstvo.invbroj) as invbroj, max(os_sredstvo.corg2) as corg, "+
//               "0.00 as tri, 0.00 as cetiri, "+
//               "0.00+0.00 as pet, "+
//               "sum(os_promjene.osnduguje) as sest, sum(os_promjene.osnpotrazuje) as sedam, "+
//               "sum(os_promjene.ispduguje) as osam, sum(os_promjene.isppotrazuje) as devet, "+
//               "sum(os_promjene.isppotrazuje) as amor, "+
//               "sum(os_sredstvo.osnpocetak+os_promjene.osnduguje-os_promjene.osnpotrazuje) as deset, "+
//               "sum(os_sredstvo.isppocetak+os_promjene.isppotrazuje-os_promjene.ispduguje) as jedanaest, "+
//               "sum(os_sredstvo.osnpocetak+os_promjene.osnduguje-os_promjene.osnpotrazuje-os_sredstvo.isppocetak-os_promjene.isppotrazuje+os_promjene.ispduguje) as dvanaest "+
//               "from os_sredstvo, os_promjene where os_sredstvo.invbroj= os_promjene.invbroj "+
//               "and os_promjene.datpromjene >="+pocDat+" and os_promjene.datpromjene <="+zavDat+
//               " and os_sredstvo.corg2= os_promjene.corg2 and os_sredstvo.status=os_promjene.status and os_promjene.status='A' and  os_sredstvo.corg2 in ("+inCorg2+") ";

        qStr = qStr + "select os_promjene.cpromjene as cpromjene, brojkonta brojkonta, os_sredstvo.invbroj as invbroj, os_sredstvo.corg2 as corg, "+
               "CAST (0.00 AS numeric(15,2)) as tri, CAST(0.00 AS numeric(15,2)) as cetiri, "+
               "CAST(0.00 AS numeric(15,2)) as pet, "+
               "os_promjene.osnduguje as sest, os_promjene.osnpotrazuje as sedam, "+
               "os_promjene.ispduguje as osam, os_promjene.isppotrazuje as devet, "+
               "os_promjene.isppotrazuje as amor, "+
               "CAST((os_sredstvo.osnpocetak+os_promjene.osnduguje-os_promjene.osnpotrazuje) AS numeric(15,2)) as deset, "+
               "CAST((os_sredstvo.isppocetak+os_promjene.isppotrazuje-os_promjene.ispduguje) AS numeric(15,2)) as jedanaest, "+
               "CAST((os_sredstvo.osnpocetak+os_promjene.osnduguje-os_promjene.osnpotrazuje-os_sredstvo.isppocetak-os_promjene.isppotrazuje+os_promjene.ispduguje) AS numeric(15,2)) as dvanaest "+
               "from os_sredstvo, os_promjene where os_sredstvo.invbroj= os_promjene.invbroj "+
               "and os_promjene.datpromjene >="+pocDat+" and os_promjene.datpromjene <="+zavDat+
               " and os_sredstvo.corg2= os_promjene.corg2 and os_sredstvo.status=os_promjene.status and os_promjene.status='A' and  "+inCorg2+" ";
        if(!sKonto.equals(""))
          qStr=qStr +  " and os_sredstvo.brojkonta ='" + sKonto+"'";
//        qStr+= getRekapGB(gb); ///-> odkomentirati za staru verziju
//        qStr+=" group by cpromjene, invbroj";
//        if(i != sds.getRowCount()-1)
//          qStr = qStr + " union ";
//        sds.next();
//      }
//      System.out.println("REKAPITULACIJA: " + qStr);
      return qStr;
    }

    public QueryDataSet recountRekap(QueryDataSet qds, String uvjet, String paramStr)
    {
      QueryDataSet sds = new QueryDataSet();
      sds.setQuery(new QueryDescriptor(dm.getDatabase1(), paramStr));
      QueryDataSet tempQDS = new QueryDataSet();
      String corg="";
      String bk="";
      String ib="";
      boolean insert;
      double sum5 = 0;
      double sum10 = 0;
      double sum11 = 0;
      double sum12 = 0;
      BigDecimal sum3 = new BigDecimal(0);
      BigDecimal sum4 = new BigDecimal(0);
      BigDecimal sum6 = new BigDecimal(0);
      BigDecimal sum7 = new BigDecimal(0);
      BigDecimal sum8 = new BigDecimal(0);
      BigDecimal amor = new BigDecimal(0);

      BigDecimal sum9 = new BigDecimal(0);

      qds.open();
      tempQDS.setColumns(new Column[]{
        (Column)qds.getColumn("CPROMJENE").clone(),
        (Column)qds.getColumn("BROJKONTA").clone(),
        (Column)qds.getColumn("INVBROJ").clone(),
        (Column)qds.getColumn("CORG").clone(),
        (Column)qds.getColumn("TRI").clone(),
        (Column)qds.getColumn("CETIRI").clone(),
        (Column)qds.getColumn("PET").clone(),
        (Column)qds.getColumn("SEST").clone(),
        (Column)qds.getColumn("SEDAM").clone(),
        (Column)qds.getColumn("OSAM").clone(),
        (Column)qds.getColumn("DEVET").clone(),
        (Column)qds.getColumn("AMOR").clone(),
        (Column)qds.getColumn("DESET").clone(),
        (Column)qds.getColumn("JEDANAEST").clone(),
        (Column)qds.getColumn("DVANAEST").clone()
      });

      sds.open();
      sds.first();
      qds.open();
      for(int i=0; i<sds.getRowCount();i++)
      {
        qds.first();
        insert = false;
        sum5 = 0;
        sum10 = 0;
        sum11 = 0;
        sum12 = 0;
        sum3 = new BigDecimal(0);
        sum4 = new BigDecimal(0);
        sum6 = new BigDecimal(0);
        sum7 = new BigDecimal(0);
        sum8 = new BigDecimal(0);
        amor = new BigDecimal(0);
        sum9 = new BigDecimal(0);
        while(qds.inBounds())
        {
          if(sds.getString(uvjet).equals(qds.getString(uvjet)))
          {
            corg = qds.getString("CORG");
            bk = qds.getString("BROJKONTA");
            ib = qds.getString("INVBROJ");
            sum3=sum3.add(qds.getBigDecimal("TRI"));
            sum4=sum4.add(qds.getBigDecimal("CETIRI"));
            sum6=sum6.add(qds.getBigDecimal("SEST"));
            sum7=sum7.add(qds.getBigDecimal("SEDAM"));
            sum8=sum8.add(qds.getBigDecimal("OSAM"));
            sum5=sum5+qds.getBigDecimal("PET").doubleValue();
            amor = amor.add(qds.getBigDecimal("AMOR"));
            sum9=sum9.add(qds.getBigDecimal("DEVET"));
            sum10=sum10+qds.getBigDecimal("DESET").doubleValue();
            sum11=sum11+qds.getBigDecimal("JEDANAEST").doubleValue();
            sum12=sum12+qds.getBigDecimal("DVANAEST").doubleValue();
            insert = true;
          }
          qds.next();
        }

        if(insert)
        {
          tempQDS.open();
          tempQDS.insertRow(false);
          tempQDS.setString("CORG",corg);
          tempQDS.setString("BROJKONTA",bk);
          tempQDS.setString("INVBROJ",ib);
          tempQDS.setBigDecimal("TRI", sum3);
          tempQDS.setBigDecimal("CETIRI", sum4);
          tempQDS.setBigDecimal("PET", new BigDecimal(sum5));
          tempQDS.setBigDecimal("SEST", sum6);
          tempQDS.setBigDecimal("SEDAM", sum7);
          tempQDS.setBigDecimal("OSAM", sum8);
          tempQDS.setBigDecimal("DEVET", sum9);
          tempQDS.setBigDecimal("AMOR", amor);
          tempQDS.setBigDecimal("DESET",  new BigDecimal(sum10));
          tempQDS.setBigDecimal("JEDANAEST",  new BigDecimal(sum11));
          tempQDS.setBigDecimal("DVANAEST",  new BigDecimal(sum12));
        }
        sds.next();
      }
      return tempQDS;
    }

    public String getRekapGB(int gb)
    {
      String qStr = "";
      switch (gb) {
        case 1:
          qStr=" group by corg ";
          break;
        case 2:
          qStr=" group by brojkonta ";
          break;
        case 3:
          qStr=" group by corg, brojkonta ";
          break;
        case 4:
          qStr=" group by invbroj ";
          break;
        case 5:
          qStr=" group by corg, invbroj ";
          break;
        case 6:
          qStr=" group by brojkonta, invbroj ";
          break;
        case 7:
          qStr=" group by corg, brojkonta, invbroj ";
          break;
      }
      return qStr;
    }

    public String getDefaultRekap(String cOrg)
    {
      String qStr = "select datum as datum from os_kontrola where corg='"+cOrg+"'";
      return qStr;
    }

    public String getDefaultRekap()
    {
      String qStr = "select corg as corg, datumod as datumod, datumdo as datumdo from os_metaobrada";
      return qStr;
    }

    //*************pregled knjizenja

    public String getIzKnjiz(String corg, String konto, String vrPr, String pocDat, String zavDat, boolean selected)
    {
     String qStr = "select os_promjene.corg2 as corg, os_promjene.invbroj as invbroj, os_promjene.datpromjene as datpromjene, os_promjene.cpromjene as cpromjene, "+
                   "os_promjene.osnduguje as osnduguje, os_promjene.osnpotrazuje as osnpotrazuje, os_promjene.ispduguje as ispduguje, os_promjene.isppotrazuje as isppotrazuje, "+
                   "os_promjene.saldo as saldo, os_sredstvo.brojkonta as brojkonta from os_promjene, os_sredstvo "+
                   "where os_promjene.corg2 = os_sredstvo.corg2 and os_promjene.invbroj = os_sredstvo.invbroj "+
                   "and os_promjene.datpromjene >="+pocDat+" and os_promjene.datpromjene <="+zavDat;
     if(!konto.equals(""))
        qStr = qStr + " and os_sredstvo.brojkonta='"+konto+"' ";
      if(!vrPr.equals(""))
        qStr = qStr + " and os_promjene.cpromjene='"+vrPr+"' ";

      if(!selected)
        qStr = qStr + " and os_promjene.corg2='"+corg+"' ";
      else
//       qStr += " and " + getKnjizPripCorg(corg)+ "order by corg";
       qStr += " and " + getPripOrg(corg, "OS_PROMJENE")+ "order by corg";
      return qStr;
    }

    public String getIzKnjizSI(String corg, String konto, String vrPr, String pocDat, String zavDat, boolean selected)
    {
     String qStr = "select os_stsi.corg2 as corg, os_stsi.invbroj as invbroj, os_stsi.datpromjene as datpromjene, os_stsi.cpromjene as cpromjene, "+
                   "os_stsi.osnduguje as osnduguje, os_stsi.osnpotrazuje as osnpotrazuje, os_stsi.ispduguje as ispduguje, os_stsi.isppotrazuje as isppotrazuje, "+
                   "os_stsi.saldo as saldo, os_si.brojkonta as brojkonta from os_stsi, os_si "+
                   "where os_stsi.corg2 = os_si.corg2 and os_stsi.invbroj = os_si.invbroj "+
                   "and os_stsi.datpromjene >="+pocDat+" and os_stsi.datpromjene <="+zavDat;

      if(!konto.equals(""))
        qStr = qStr + " and os_si.brojkonta='"+konto+"' ";
      if(!vrPr.equals(""))
        qStr = qStr + " and os_stsi.cpromjene='"+vrPr+"' ";
      if(!selected)
        qStr = qStr + " and os_stsi.corg2='"+corg+"' ";
      else
//        qStr += " and " + getKnjizSIPripCorg(corg)+ "order by corg";
        qStr += " and " + getPripOrg(corg, "OS_STSI")+ "order by corg";

        System.out.println("qStr - " + qStr);
      return qStr;
    }



    public String getAmRev(String cOrg, String pocDat, String zavDat, int po, boolean poOJ)
    {
     String qStr = "select corg as corg, invbroj as invbroj, nazsredstva as nazsredstva, datpromjene as datpr, datlikvidacije as datlik, "+
                   "osnovica as nabvrijed, ispravak as ispravak, amortizacija as amortizacija, "+
                   "(ispravak + amortizacija) as stvarniisp, mjesec as mjesec from os_obrada4 "+
                   " where os_obrada4.datlikvidacije between "+pocDat+" AND "+zavDat;
//                   "where os_obrada4.datpromjene <="+zavDat+
//                   " and os_obrada4.datlikvidacije >'1970-01-01 01:00:00' and "+getLIKPripOrg(cOrg, po);
//                   " and os_obrada4.datlikvidacije >'1970-01-01 01:00:00'";// and "+getPripOrg(cOrg, po, "OS_OBRADA4");

      if (poOJ)
        qStr = qStr + " and "+getPripOrgPrim(cOrg,"OS_OBRADA4")+" order by corg";
      else
        qStr = qStr + " and corg ='"+cOrg+"' order by corg";
      
      System.out.println(qStr);
      return qStr;
    }

//    public String getLikviSI(String corg, String pocDat, String zavDat, boolean selected)
//    {
//      String qStr = "select corg2 as corg, invbroj as invbroj, nazsredstva as nazsredstva, datpromjene as datpr, datlikvidacije as datlik, "+
//                    "saldo as saldo, (isppocetak + isppotrazuje-ispduguje) as ispravak, (osnpocetak + osnduguje-osnpotrazuje) as osnovica, brojkonta as brojkonta "+
//                    " from os_si "+
//                    "where os_si.datpromjene >="+pocDat+" and os_si.datpromjene <="+zavDat+
//                    " and os_si.datlikvidacije >'1970-01-01 01:00:00' ";
//      if (selected == false)
//        qStr = qStr + " and corg2 ='"+corg+"' order by corg";
//      else
//        qStr += " and " + getPripOrg(corg, "OS_SI")+ "order by corg";
//      return qStr;
//    }

//    public String getLikvi(String corg, String pocDat, String zavDat, boolean selected)
//    {
//      String qStr = "select corg2 as corg, invbroj as invbroj, nazsredstva as nazsredstva, datpromjene as datpr, datlikvidacije as datlik, "+
//                    "saldo as saldo, (isppocetak + isppotrazuje-ispduguje) as ispravak, (osnPocetak + osnduguje-osnpotrazuje) as osnovica, brojkonta as brojkonta "+
//                    " from os_sredstvo "+
//                    "where os_sredstvo.datpromjene >="+pocDat+" and os_sredstvo.datpromjene <="+zavDat+
//                    " and os_sredstvo.datlikvidacije >'1970-01-01 01:00:00' ";
//
//      if (selected == false)
//        qStr = qStr + " and corg2 ='"+corg+"' order by corg";
//      else
//        qStr += " and " + getPripOrg(corg, "OS_SREDSTVO")+ "order by corg";
//      System.out.println("LIK STR: " + qStr);
//      return qStr;
//    }

    public String getLikvi(String corg, String pocDat, String zavDat, boolean selected)
    {
      String qStr = "select corg2 as corg, invbroj as invbroj, nazsredstva as nazsredstva, datpromjene as datpr, datlikvidacije as datlik, "+
                    "CAST((osnPocetak + osnduguje+revosn-isppocetak-isppotrazuje-amortizacija-pamortizacija-revisp) AS numeric(15,2)) as saldo, " +
                    "CAST((isppocetak + isppotrazuje+amortizacija+pamortizacija+revisp) AS numeric(15,2)) as ispravak, " +
                    "CAST((osnPocetak + osnduguje+revosn) AS numeric(15,2)) as osnovica, brojkonta as brojkonta "+
                    " from os_sredstvo "+
                    "where os_sredstvo.datlikvidacije >="+pocDat+" and os_sredstvo.datlikvidacije <="+zavDat+
                    " and os_sredstvo.datlikvidacije >'1970-01-01 01:00:00' ";

      if (selected == false)
        qStr = qStr + " and corg2 ='"+corg+"' order by corg";
      else
        qStr += " and " + getPripOrg(corg, "OS_SREDSTVO")+ "order by corg";
//      System.out.println("likvi STR: " + qStr);
      return qStr;
    }

    public String getLikviSI(String corg, String pocDat, String zavDat, boolean selected)
   {
     String qStr = "select corg2 as corg, invbroj as invbroj, nazsredstva as nazsredstva, datpromjene as datpr, datlikvidacije as datlik, "+
//                   "saldo as saldo, (isppocetak + isppotrazuje-ispduguje) as ispravak, (osnpocetak + osnduguje-osnpotrazuje) as osnovica, brojkonta as brojkonta "+
                   "CAST((osnPocetak + osnduguje+revosn-isppocetak-isppotrazuje-amortizacija-pamortizacija-revisp) AS numeric(15,2)) as saldo, " +
                   "CAST((isppocetak + isppotrazuje+amortizacija+pamortizacija+revisp) AS numeric(15,2)) as ispravak, " +
                   "CAST((osnPocetak + osnduguje+revosn) AS numeric(15,2)) as osnovica, brojkonta as brojkonta "+
                   " from os_si "+
                   "where os_si.datpromjene >="+pocDat+" and os_si.datpromjene <="+zavDat+
                   " and os_si.datlikvidacije >'1970-01-01 01:00:00' ";
     if (selected == false)
       qStr = qStr + " and corg2 ='"+corg+"' order by corg";
     else
       qStr += " and " + getPripOrg(corg, "OS_SI")+ "order by corg";
     return qStr;
    }

    public String getAmRevTot(String cOrg, String pocDat, String zavDat, int po, boolean poOJ)
    {
      String qStr = "select max(corg) as corg, sum(osnovica) as nabvrijed,"+
                    "sum(ispravak) as ispravak, sum(amortizacija) as amortizacija, "+
                    "sum(ispravak + amortizacija) as stvarniisp from os_obrada4 "+
                    " where os_obrada4.datlikvidacije between "+pocDat+" AND "+zavDat;
//                    "where os_obrada4.datpromjene <="+zavDat+
//                    " and os_obrada4.datlikvidacije >'1970-01-01 01:00:00' and "+getLIKPripOrg(cOrg, po);
//                    " and os_obrada4.datlikvidacije >'1970-01-01 01:00:00' ";//and "+getPripOrg(cOrg, po, "OS_OBRADA4");
//      if (!cOrg.equals(""))
//        qStr = qStr + " and corg ='"+cOrg+"' ";
//      qStr = qStr + " group by os_obrada4.corg";
      if (poOJ)
        qStr = qStr + " and "+getPripOrgPrim(cOrg,"OS_OBRADA4")+" order by corg";
      else
        qStr = qStr + " and corg ='"+cOrg+"' order by corg";
      return qStr;
    }

    public String getAmRevTotSI(String corg, String pocDat, String zavDat)
    {
      String qStr = "select max(corg) as corg, sum(nabvrijed) as nabvrijed, "+
                    "sum(isppotrazuje-ispduguje) as ispravak, sum(amortizacija) as amortizacija, "+
                    "sum(isppotrazuje-ispduguje + amortizacija) as stvarniisp from os_si "+
                    "where os_si.datpromjene <="+zavDat+
                    " and os_si.datlikvidacije >'1970-01-01 01:00:00' ";
      if (!corg.equals(""))
        qStr = qStr + " and corg ='"+corg+"' ";
      qStr = qStr + " group by os_si.corg";

      return qStr;
    }

    //************ inventurna lista

    public String getInv(String corg, String clokacije, boolean selected)
    {
      String qStr = "select os_sredstvo.corg2 as corg, os_sredstvo.invbroj as invbroj, os_sredstvo.datlikvidacije as datlikvidacije, "+
                    "os_sredstvo.nazsredstva as nazsredstva, os_lokacije.clokacije as clokacije, "+
                    "os_lokacije.nazlokacije as nazlokacije from os_sredstvo, os_lokacije "+
                    "where os_sredstvo.corg2=os_lokacije.corg and os_sredstvo.clokacije=os_lokacije.clokacije "+
                    " and os_sredstvo.datlikvidacije is null and os_sredstvo.cobjekt = os_lokacije.cobjekt ";

      if(!clokacije.equals(""))
        qStr += " and os_sredstvo.clokacije='"+clokacije+"' ";
      if (selected == false)
        qStr = qStr + " and corg2 ='"+corg+"' order by corg, invbroj";
      else
//        qStr += " and " + getLikviPripCorg(corg)+ "order by corg";
        qStr += " and " + getPripOrg(corg, "OS_SREDSTVO")+ "order by corg, invbroj";

      System.out.println("qStr\n"+qStr);

      return qStr;
    }

     public String getInvSI(String corg, String clokacije, boolean selected)
    {

      String qStr = "select os_si.corg as corg, os_si.invbroj as invbroj, os_si.datlikvidacije as datlikvidacije, "+
                    "os_si.nazsredstva as nazsredstva, os_lokacije.clokacije as clokacije, "+
                    "os_lokacije.nazlokacije as nazlokacije from os_si, os_lokacije "+
                    "where os_si.corg=os_lokacije.corg and os_si.clokacije=os_lokacije.clokacije "+
                    " and os_si.datlikvidacije is null and os_si.cobjekt = os_lokacije.cobjekt ";
      if(!clokacije.equals(""))
        qStr += " and os_si.clokacije='"+clokacije+"' ";
      if (selected == false)
        qStr = qStr + " and corg2 ='"+corg+"' order by corg";
      else
//        qStr += " and " + getLikviSIPripCorg(corg)+ "order by corg";
        qStr += " and " + getPripOrg(corg, "OS_SI")+ "order by corg";
      return qStr;
    }

    //****************promjene

    public String getOrgStr()
    {
      String qStr = "select ORGSTRUKTURA.CORG AS CORG, ORGSTRUKTURA.NAZIV AS NAZIV, ORGSTRUKTURA.MJESTO AS MJESTO, "+
                    "ORGSTRUKTURA.ADRESA AS ADRESA, ORGSTRUKTURA.PRIPADNOST AS PRIPADNOST, ORGSTRUKTURA.ZIRO AS ZIRO, "+
                    "ORGSTRUKTURA.HPBROJ AS HPBROJ, ORGSTRUKTURA.NALOG AS NALOG, ORGSTRUKTURA.AKTIV as AKTIV, ORGSTRUKTURA.LOKK AS LOKK "+
                    " from ORGSTRUKTURA where ORGSTRUKTURA.NALOG ='1'";
      return qStr;
    }
    public boolean checkPromPK(String corg)
    {      
      String qStr = "corg ='"+corg+"'";
      return OS_Kontrola.getDataModule().getRowCount(qStr) == 0;
    }

    public int StrToInt(String convertibleInt)
    {
      Integer newInt = new Integer(convertibleInt);
      int myInt = newInt.intValue();
      return myInt;
    }

    public boolean getKontrola()
    {
      String cOrg1 = hr.restart.zapod.dlgGetKnjig.getKNJCORG();
      if(ld.raLocate(dm.getOS_Kontrola(),
       new java.lang.String[] {"CORG"},
       new java.lang.String[] {cOrg1},
       com.borland.dx.dataset.Locate.CASE_INSENSITIVE))
       {
          return true;
       }
       else
       {
          return false;
       }
    }

    public String getLokacijaStr(String cOrg)
    {
      String qStr = "select OS_LOKACIJE.CORG AS CORG, OS_LOKACIJE.CLOKACIJE AS CLOKACIJE, OS_LOKACIJE.NAZLOKACIJE AS NAZLOKACIJE "+
                    " from OS_LOKACIJE where OS_LOKACIJE.CORG ='"+cOrg+"'";
      return qStr;
    }

    public boolean isPromjene(String cOrg) {
       return ld.raLocate(dm.getOS_Kontrola(),new String[] {"CORG"},new String[] {
       hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(cOrg)
      });
    }

    public boolean checkPrethodneStavke(QueryDataSet qds)
    {
      java.sql.Timestamp prvidan = hr.restart.util.Util.getUtil().getFirstDayOfYear(dm.getOS_Kontrola().getTimestamp("DATUM"));

      com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
      for (int i=0; i<qds.rowCount();i++) {
        qds.getVariant("DATPROMJENE",i,v);
        if(v.getTimestamp().before(prvidan) && v.getTimestamp().after(java.sql.Timestamp.valueOf("1970-01-01 01:00:00.0")))
         return true;
      }
      return false;
    }

    public boolean checkLikvidacija(QueryDataSet qds)
    {
      if (qds.getTimestamp("DATLIKVIDACIJE").after(java.sql.Timestamp.valueOf("1970-01-01 01:00:00.0")))
        return true;
      return false;
    }

    public boolean checkLokacijePK(String cOrg, String cLokacije, String cObjekt)
    {      
      String qStr = "select corg, clokacije, cobjekt from os_lokacije";
      DataSet qds = Util.getNewQueryDataSet(qStr);      
      qds.first();
      do
      {
        if(qds.getString("CORG").equals(cOrg) && qds.getString("CLOKACIJE").equals(cLokacije)
           && qds.getString("COBJEKT").equals(cObjekt))
          return true;
        qds.next();
      }
      while(qds.inBounds());
      return false;
    }

    public boolean checkObjektPK(String cOrg, String cObjekt)
    {      
      String qStr = "select corg, cobjekt from os_objekt";
      DataSet qds = Util.getNewQueryDataSet(qStr);
      qds.first();
      do
      {
        if(qds.getString("CORG").equals(cOrg) && qds.getString("COBJEKT").equals(cObjekt))
          return true;
        qds.next();
      }
      while(qds.inBounds());
      return false;
    }

    public void  provjeraGodine(String year) throws Exception
    {
      Integer provjera = new Integer(year);
      int intOK = provjera.intValue();
      if (year.length() < 4)
      {
        throw new Exception();
      }
    }

    public void  provjeraGodine(JraTextField testField) throws Exception
    {
      Integer provjera = new Integer(testField.getText());
      int intOK = provjera.intValue();
      if (testField.getText().length() < 4)
      {
        throw new Exception();
      }
    }

    public String recalcRBR(String invBr, int rbr)
  {
    String qStr = "update os_promjene set rbr = rbr-1 where invbroj='"+invBr+"'"+
                  " and rbr >" + rbr;
    return qStr;
  }


  String getPripOrg(String str, int mode, String corgTable) {
   int i=0;
   String cVrati;
   if (mode==8) {
     com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
     cVrati = " "+Condition.in("CORG2", tds, "CORG").qualified(corgTable);
//     cVrati=" "+corgTable.trim()+".CORG2 in (";
//     com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
//     tds.first();
//     do {
//       if (i>0) {
//         cVrati=cVrati+',';
//       }
//       i++;
//       cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//       tds.next();
//     } while (tds.inBounds());
//     cVrati=cVrati+")";
   }
   else {
     cVrati=" "+corgTable.trim()+".CORG2='"+str+"'";
   }
   return cVrati;
  }

  String getPripOrg(String str, String corgTable) {
    return getPripOrg(str, 8, corgTable);
    //    int i=0;
//    String cVrati;
//      cVrati=" "+corgTable.trim()+".CORG2 in (";
//      com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
//      tds.first();
//      do {
//        if (i>0) {
//          cVrati=cVrati+',';
//        }
//        i++;
//        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//        tds.next();
//      } while (tds.inBounds());
//      cVrati=cVrati+")";
//    return cVrati;
  }

  /////
  String getPripOrgAm(String str, int mode, String corgTable) {
 int i=0;
 String cVrati;
 if (mode==8) {
   cVrati = " "+Aus.getCorgInCond(str).qualified(corgTable);
//   cVrati=" "+corgTable.trim()+".CORG in (";
//   com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
//   tds.first();
//   do {
//     if (i>0) {
//       cVrati=cVrati+',';
//     }
//     i++;
//     cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//     tds.next();
//   } while (tds.inBounds());
//   cVrati=cVrati+")";
 }
 else {
   cVrati=" "+corgTable.trim()+".CORG='"+str+"'";
 }
 return cVrati;
}

String getPripOrgAm(String str, String corgTable) {
  return getPripOrgAm(str, 8, corgTable);
//  int i=0;
//  String cVrati;
//    cVrati=" "+corgTable.trim()+".CORG in (";
//    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
//    tds.first();
//    do {
//      if (i>0) {
//        cVrati=cVrati+',';
//      }
//      i++;
//      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//      tds.next();
//    } while (tds.inBounds());
//    cVrati=cVrati+")";
//  return cVrati;
  }

  String getPripOrgPrim(String str, String corgTable) {
    return getPripOrgAm(str, 8, corgTable);
//    int i=0;  //Djizus fakin kopipejstn krajst !!!
//    String cVrati;
//      cVrati=" "+corgTable.trim()+".CORG in (";
//      com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
//      tds.first();
//      do {
//        if (i>0) {
//          cVrati=cVrati+',';
//        }
//        i++;
//        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//        tds.next();
//      } while (tds.inBounds());
//      cVrati=cVrati+")";
//    return cVrati;
  }

   // Osnovna sredstva
    public String getPST_OSIspis(String cOrg, int oj, int ol, int ib, int po, String status, String gpp, String gpz, String porijeklo, String aktiv)
    {
      String qStr = "";
      qStr = "select max(os_sredstvo.dokument) as dokument, max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
              "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_sredstvo.datpromjene) as datum, "+
              "sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.isppocetak) as isppocetak "+
//              "from os_sredstvo where os_sredstvo.osnpocetak>0 "+getOSStatus(status)+getAktivnost(aktiv)+getOSPorijeklo(porijeklo)+" and os_sredstvo.godproiz>="+gpp+" and os_sredstvo.godproiz<="+gpz+" and "+getOSPripOrg(cOrg, po)+getGroupBy(oj+ol+ib, ispOS.getSelectedRB());
              "from os_sredstvo where os_sredstvo.osnpocetak>0 "+getOSStatus(status)+getAktivnost(aktiv)+getOSPorijeklo(porijeklo)+" and os_sredstvo.godproiz>='"+gpp+"' and os_sredstvo.godproiz<='"+gpz+"' and "+getPripOrg(cOrg, po, "OS_SREDSTVO")+getGroupBy(oj+ol+ib, ispOS.getSelectedRB(),"os_sredstvo");
//      System.out.println("PST_OSI "+qStr);
      return qStr;
    }

    public String getPST_OSIspisV2(String cOrg, boolean oj, boolean ol, boolean ib, boolean po, String status, String gpp, String gpz, String porijeklo, String aktiv, int selRB){
      String nadoprc = "";
      int pripO = 0;
      if (po) pripO = 8;
      if(oj && !ol && !ib) nadoprc = " group by os_sredstvo.corg2";
      else if(!oj && ol && !ib) nadoprc = " group by "+oblikIspisa(selRB,"os_sredstvo");
      else if(oj && ol && !ib) nadoprc = " group by os_sredstvo.corg2, "+oblikIspisa(selRB,"os_sredstvo");
      else if(!oj && !ol && ib) nadoprc = " group by os_sredstvo.invbroj";
      else if(oj && !ol && ib) nadoprc = " group by os_sredstvo.corg2, os_sredstvo.invbroj";
      else if(!oj && ol && ib) nadoprc = " group by "+oblikIspisa(selRB,"os_sredstvo")+", os_sredstvo.invbroj";
      else if(oj && ol && ib) nadoprc = " group by corg2, "+oblikIspisa(selRB,"os_sredstvo")+", os_sredstvo.invbroj";
      String qStr = "select max(os_sredstvo.dokument) as dokument, max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
              "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_sredstvo.datpromjene) as datum, "+
              "sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.isppocetak) as isppocetak "+
              "from os_sredstvo where os_sredstvo.osnpocetak>0 "+getOSStatus(status)+getAktivnost(aktiv)+getOSPorijeklo(porijeklo)+" and os_sredstvo.godproiz>='"+gpp+"' and os_sredstvo.godproiz<='"+gpz+"' and "+getPripOrg(cOrg, pripO, "OS_SREDSTVO")+nadoprc;
//      System.out.println("PST_OSI "+qStr);
      return qStr;
    }

    public String getTST_OSIspis(String cOrg, int oj, int ol, int ib, int po, String status, String gpp, String gpz, String porijeklo, String aktiv)
    {
      //+getAktivnost(aktiv)

      String datLik = "";
      if(aktiv.equals("D"))
        datLik = " and os_sredstvo.datlikvidacije is null " +getAktivnost(aktiv);
      else if (aktiv.equals("N"))
        datLik = " and os_sredstvo.datlikvidacije is not null " +getAktivnost(aktiv);

      String t = hr.restart.util.Util.getUtil().clearTime(hr.restart.robno.Util.getUtil().findFirstDayOfYear()).toString();
      String qStr = "select max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
                    "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_sredstvo.datpromjene) as datum, "+
                    "sum(os_sredstvo.osnduguje) as osnduguje, sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.osnpotrazuje) as osnpotrazuje, sum(os_sredstvo.ispduguje) as ispduguje, sum(os_sredstvo.isppotrazuje) as isppotrazuje, sum(os_sredstvo.isppocetak) as isppocetak, "+
                    "sum(os_sredstvo.amortizacija) as amortizacija, sum(os_sredstvo.pamortizacija) as pamortizacija "+
                    "from os_sredstvo where "+getPripOrg(cOrg, po, "OS_SREDSTVO")+datLik+getOSStatus(status)+getOSPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispOS.getSelectedRB(),"os_sredstvo")+
                    " union select max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
                    "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, "+
                    "max(os_sredstvo.datpromjene) as datum, sum(os_sredstvo.osnduguje) as osnduguje, sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.osnpotrazuje) as osnpotrazuje, "+
                    "sum(os_sredstvo.ispduguje) as ispduguje, sum(os_sredstvo.isppotrazuje) as isppotrazuje, sum(os_sredstvo.isppocetak) as isppocetak, "+
                    "sum(os_sredstvo.amortizacija) as amortizacija, sum(os_sredstvo.pamortizacija) as pamortizacija "+
                    "from os_sredstvo where "+getPripOrg(cOrg, po, "OS_SREDSTVO")+datLik+ " and os_sredstvo.godproiz>="+gpp+
                    "' and os_sredstvo.godproiz<='"+gpz+"' "+getOSStatus(status)+getOSPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispOS.getSelectedRB(),"os_sredstvo");

//      String qStr="select max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
//                  "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, "+
//                  "max(os_sredstvo.datpromjene) as datum, sum(os_sredstvo.osnduguje) as osnduguje, sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.osnpotrazuje) as osnpotrazuje, "+
//                  "sum(os_sredstvo.ispduguje) as ispduguje, sum(os_sredstvo.isppotrazuje) as isppotrazuje, sum(os_sredstvo.isppocetak) as isppocetak "+
//                  "from os_sredstvo where "+getPripOrg(cOrg, po, "OS_SREDSTVO")+datLik+ " and os_sredstvo.godproiz>="+gpp+
//                  " and os_sredstvo.godproiz<="+gpz+" "+getOSStatus(status)+getOSPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispOS.getSelectedRB());

//      System.out.println("TST_OS: " + qStr);
      return qStr;
    }

    public String getTST_OSIspisV2(String cOrg, boolean oj, boolean ol, boolean ib, boolean po, String status, String gpp, String gpz, String porijeklo, String aktiv, int selRB) {
      String nadoprc = "";
     int pripO = 0;
     if (po) pripO = 8;
     if(oj && !ol && !ib) nadoprc = " group by os_sredstvo.corg2";
     else if(!oj && ol && !ib) nadoprc = " group by "+oblikIspisa(selRB,"os_sredstvo");
     else if(oj && ol && !ib) nadoprc = " group by os_sredstvo.corg2, "+oblikIspisa(selRB,"os_sredstvo");
     else if(!oj && !ol && ib) nadoprc = " group by os_sredstvo.invbroj";
     else if(oj && !ol && ib) nadoprc = " group by os_sredstvo.corg2, os_sredstvo.invbroj";
     else if(!oj && ol && ib) nadoprc = " group by "+oblikIspisa(selRB,"os_sredstvo")+", os_sredstvo.invbroj";
      else if(oj && ol && ib) nadoprc = " group by os_sredstvo.corg2, "+oblikIspisa(selRB,"os_sredstvo")+", os_sredstvo.invbroj";
      String datLik = "";
      if(aktiv.equals("D"))
        datLik = " and os_sredstvo.datlikvidacije is null " +getAktivnost(aktiv);
      else if (aktiv.equals("N"))
        datLik = " and os_sredstvo.datlikvidacije is not null " +getAktivnost(aktiv);

      String t = hr.restart.util.Util.getUtil().clearTime(hr.restart.robno.Util.getUtil().findFirstDayOfYear()).toString();
/*      String qStr = "select max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
      "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_sredstvo.datpromjene) as datum, "+
      "sum(os_sredstvo.osnduguje) as osnduguje, sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.osnpotrazuje) as osnpotrazuje, sum(os_sredstvo.ispduguje) as ispduguje, sum(os_sredstvo.isppotrazuje) as isppotrazuje, sum(os_sredstvo.isppocetak) as isppocetak, "+
      "sum(os_sredstvo.amortizacija) as amortizacija, sum(os_sredstvo.pamortizacija) as pamortizacija "+
      "from os_sredstvo where "+getPripOrg(cOrg, pripO, "OS_SREDSTVO")+datLik+" "+getOSStatus(status)+" "+getOSPorijeklo(porijeklo)+" "+nadoprc+" "+
      " union select max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+*/
      String qStr = "select max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
                    "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, "+
                    "max(os_sredstvo.datpromjene) as datum, sum(os_sredstvo.osnduguje) as osnduguje, sum(os_sredstvo.osnpocetak) as osnpocetak, sum(os_sredstvo.osnpotrazuje) as osnpotrazuje, "+
                    "sum(os_sredstvo.ispduguje) as ispduguje, sum(os_sredstvo.isppotrazuje) as isppotrazuje, sum(os_sredstvo.isppocetak) as isppocetak, "+
                    "sum(os_sredstvo.amortizacija) as amortizacija, sum(os_sredstvo.pamortizacija) as pamortizacija "+
                    "from os_sredstvo where "+getPripOrg(cOrg, pripO, "OS_SREDSTVO")+" "+datLik+ " and os_sredstvo.godproiz>='"+gpp+
                    "' and os_sredstvo.godproiz<='"+gpz+"' "+getOSStatus(status)+" "+getOSPorijeklo(porijeklo)+" "+nadoprc;
//      System.out.println("TST_OS: " + qStr);
      return qStr;
    }

    public String getSND_OSIspis(String cOrg,String pocDat, String zavDat, int oj, int ol, int ib, int po, String status, String gpp, String gpz, String porijeklo, String aktiv)
    {
      String datLik="";
      if(aktiv.equals("D"))
        datLik = " and (os_sredstvo.datlikvidacije is null or os_sredstvo.datlikvidacije > "+zavDat+") ";
      else if(aktiv.equals("N"))
        datLik = " and os_sredstvo.datlikvidacije <= "+zavDat+" and os_sredstvo.datlikvidacije is not null ";
      String qStr="";
        qStr= "select max(os_promjene.dokument) as dokument, max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
              "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_promjene.datpromjene) as datum, "+
              "sum(os_promjene.osnpocetak) as osnpocetak, sum(os_promjene.isppocetak) as isppocetak, "+
              "sum(os_promjene.osnduguje) as osnduguje, sum(os_promjene.osnpotrazuje) as osnpotrazuje, sum(os_promjene.ispduguje) as ispduguje, sum(os_promjene.isppotrazuje) as isppotrazuje "+
              "from os_sredstvo "+
              "JOIN os_promjene on os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.corg2= os_promjene.corg2 "+
              " and os_promjene.datpromjene <= "+zavDat+getOSStatus_SND(status, zavDat)+getOSPorijeklo(porijeklo)+" and os_sredstvo.godproiz>='"+gpp+
              "' and os_sredstvo.godproiz<='"+gpz+"' and "+getPripOrg(cOrg, po, "OS_SREDSTVO")+datLik+
              getGroupBy(oj+ol+ib, ispOS.getSelectedRB(),"os_sredstvo");
//        System.out.println("SND_OS: " + qStr);
        return qStr;
    }

    public String getSND_OSIspisV2(String cOrg,String pocDat, String zavDat, boolean oj, boolean ol, boolean ib, boolean po, String status, String gpp, String gpz, String porijeklo, String aktiv, int selRB) {
      String dodatak = "";
      int pripO = 0;
      if (po) pripO = 8;
      if(oj && !ol && !ib) dodatak = " group by os_sredstvo.corg2";
      else if(!oj && ol && !ib) dodatak = " group by "+oblikIspisa(selRB,"os_sredstvo");
      else if(oj && ol && !ib) dodatak = " group by os_sredstvo.corg2, "+oblikIspisa(selRB,"os_sredstvo");
      else if(!oj && !ol && ib) dodatak = " group by os_sredstvo.invbroj";
      else if(oj && !ol && ib) dodatak = " group by os_sredstvo.corg2, os_sredstvo.invbroj";
      else if(!oj && ol && ib) dodatak = " group by "+oblikIspisa(selRB,"os_sredstvo")+", os_sredstvo.invbroj";
      else if(oj && ol && ib) dodatak = " group by os_sredstvo.corg2, "+oblikIspisa(selRB,"os_sredstvo")+", os_sredstvo.invbroj";
      String datLik="";
      if(aktiv.equals("D"))
        datLik = " and (os_sredstvo.datlikvidacije is null or os_sredstvo.datlikvidacije > "+zavDat+") ";
      else if(aktiv.equals("N"))
        datLik = " and os_sredstvo.datlikvidacije <= "+zavDat+" and os_sredstvo.datlikvidacije is not null ";
      String qStr="";
      qStr= "select max(os_promjene.dokument) as dokument, max(os_sredstvo.corg2) as corg, max(os_sredstvo.brojkonta) as brojkonta, max(os_sredstvo.cgrupe) as cgrupe, max(os_sredstvo.clokacije) as clokacije, "+
            "max(os_sredstvo.cskupine) as cskupine, max(os_sredstvo.cartikla) as cartikla, max(os_sredstvo.invbroj) as invbr, max(os_sredstvo.nazsredstva) as nazsredstva, max(os_sredstvo.datpromjene) as datum, "+
            "sum(os_promjene.osnpocetak) as osnpocetak, sum(os_promjene.isppocetak) as isppocetak, "+
            "sum(os_promjene.osnduguje) as osnduguje, sum(os_promjene.osnpotrazuje) as osnpotrazuje, sum(os_promjene.ispduguje) as ispduguje, sum(os_promjene.isppotrazuje) as isppotrazuje "+
            "from os_sredstvo "+
            "JOIN "+
            "os_promjene on os_sredstvo.invbroj=os_promjene.invbroj and os_sredstvo.corg2= os_promjene.corg2 "+
            "and os_promjene.datpromjene <= "+zavDat+getOSStatus_SND(status, zavDat)+getOSPorijeklo(porijeklo)+" and os_sredstvo.godproiz>='"+gpp+"' "+
            "and os_sredstvo.godproiz<='"+gpz+"' and "+getPripOrg(cOrg, pripO, "OS_SREDSTVO")+datLik+
            dodatak;
//      System.out.println("SND_OS: " + qStr);
      return qStr;
    }

    public String getAktivnost(String aktiv)
    {
      String aktivnost="";
      if(!aktiv.equals("")) aktivnost= " and os_sredstvo.aktiv='"+aktiv+"' ";
      return aktivnost;
    }

    public String getAktivnostSI(String aktiv)
    {
      String aktivnost="";
      if(!aktiv.equals("")) aktivnost= " and os_si.aktiv='"+aktiv+"' ";
      return aktivnost;
    }

    public String getPST_SIIspis(String cOrg, int oj, int ol, int ib, int po, String status, String gpp, String gpz, String porijeklo,String aktiv)
    {
      String qStr = "";
      qStr = "select max(os_si.dokument) as dokument, max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
             "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_si.datpromjene) as datum, "+
             "sum(os_si.osnpocetak) as osnpocetak, sum(os_si.isppocetak) as isppocetak "+
             "from os_si where os_si.osnpocetak>0 and os_si.godproiz>='"+gpp+"' and os_si.godproiz<='"+gpz+"' "+
//             getSIStatus(status)+getAktivnostSI(aktiv)+" and "+getSIPripOrg(cOrg, po)+getSIPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispSI.getSelectedRB());
             getSIStatus(status)+getAktivnostSI(aktiv)+" and "+getPripOrg(cOrg, po, "OS_SI")+getSIPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispSI.getSelectedRB(),"os_si");
      return qStr;
    }

    public String getPST_SIIspisV2(String cOrg, boolean oj, boolean ol, boolean ib, boolean po, String status, String gpp, String gpz, String porijeklo, String aktiv, int selRB) {
      String nadoprc = "";
      int pripO = 0;
      if (po) pripO = 8;
      if(oj && !ol && !ib) nadoprc = " group by os_si.corg2";
      else if(!oj && ol && !ib) nadoprc = " group by "+oblikIspisa(selRB,"os_si");
      else if(oj && ol && !ib) nadoprc = " group by os_si.corg2, "+oblikIspisa(selRB,"os_si");
      else if(!oj && !ol && ib) nadoprc = " group by os_si.invbroj";
      else if(oj && !ol && ib) nadoprc = " group by os_si.corg2, os_si.invbroj";
      else if(!oj && ol && ib) nadoprc = " group by "+oblikIspisa(selRB,"os_si")+", os_si.invbroj";
      else if(oj && ol && ib) nadoprc = " group by os_si.corg2, "+oblikIspisa(selRB,"os_si")+", os_si.invbroj";
      String qStr = "select max(os_si.dokument) as dokument, max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
             "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_si.datpromjene) as datum, "+
             "sum(os_si.osnpocetak) as osnpocetak, sum(os_si.isppocetak) as isppocetak "+
             "from os_si where os_si.osnpocetak>0 and os_si.godproiz>='"+gpp+"' and os_si.godproiz<='"+gpz+"' "+
//             getSIStatus(status)+getAktivnostSI(aktiv)+" and "+getSIPripOrg(cOrg, po)+getSIPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispSI.getSelectedRB());
             getSIStatus(status)+getAktivnostSI(aktiv)+" and "+getPripOrg(cOrg, pripO, "OS_SI")+getSIPorijeklo(porijeklo)+nadoprc;
//      System.out.println("qStr\n"+qStr);
      return qStr;
    }

    public String getTST_SIIspis(String cOrg, int oj, int ol, int ib, int po, String status, String gpp, String gpz, String porijeklo, String aktiv)
    {
      String datLik = "";
//      System.out.println("AKTIV: " + aktiv);
      if(aktiv.equals("D"))
        datLik = " and datlikvidacije is null " +getAktivnostSI(aktiv);
      else if (aktiv.equals("N"))
        datLik = " and datlikvidacije is not null " +getAktivnostSI(aktiv);
      String t = hr.restart.util.Util.getUtil().clearTime(hr.restart.robno.Util.getUtil().findFirstDayOfYear()).toString();
      String qStr="";
        qStr= "select max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
              "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_si.datpromjene) as datum, "+
              "sum(os_si.osnduguje) as osnduguje, sum(os_si.osnpocetak) as osnpocetak, sum(os_si.osnpotrazuje) as osnpotrazuje, sum(os_si.ispduguje) as ispduguje, sum(os_si.isppotrazuje) as isppotrazuje, sum(os_si.isppocetak) as isppocetak "+
//              "from os_si where "+getSIPripOrg(cOrg, po)+getAktivnostSI(aktiv)+" and datlikvidacije>'"+t+"'"+getSIStatus(status)+getSIPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispOS.getSelectedRB())+
              "from os_si where "+getPripOrg(cOrg, po, "OS_SI")+getAktivnostSI(aktiv)+" "+getSIStatus(status)+getSIPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispOS.getSelectedRB(),"os_si")+
              " union select max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
              "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_si.datpromjene) as datum, "+
              "sum(os_si.osnduguje) as osnduguje, sum(os_si.osnpocetak) as osnpocetak, sum(os_si.osnpotrazuje) as osnpotrazuje, sum(os_si.ispduguje) as ispduguje, sum(os_si.isppotrazuje) as isppotrazuje, sum(os_si.isppocetak) as isppocetak "+
//              "from os_si where "+getSIPripOrg(cOrg, po)+" and datlikvidacije is null and os_si.godproiz>="+gpp+" and os_si.godproiz<="+gpz+" "+
              "from os_si where "+getPripOrg(cOrg, po, "OS_SI")+" and datlikvidacije is null and os_si.godproiz>='"+gpp+"' and os_si.godproiz<='"+gpz+"' "+
              getSIStatus(status)+datLik+getSIPorijeklo(porijeklo)+getGroupBy(oj+ol+ib, ispSI.getSelectedRB(),"os_si");
      return qStr;
    }

    public String getTST_SIIspisV2(String cOrg, boolean oj, boolean ol, boolean ib, boolean po, String status, String gpp, String gpz, String porijeklo, String aktiv, int selRB){
      String nadoprc = "";
      int pripO = 0;
      if (po) pripO = 8;
      if(oj && !ol && !ib) nadoprc = " group by os_si.corg2";
      else if(!oj && ol && !ib) nadoprc = " group by "+oblikIspisa(selRB,"os_si");
      else if(oj && ol && !ib) nadoprc = " group by os_si.corg2, "+oblikIspisa(selRB,"os_si");
      else if(!oj && !ol && ib) nadoprc = " group by os_si.invbroj";
      else if(oj && !ol && ib) nadoprc = " group by os_si.corg2, os_si.invbroj";
      else if(!oj && ol && ib) nadoprc = " group by "+oblikIspisa(selRB,"os_si")+", os_si.invbroj";
      else if(oj && ol && ib) nadoprc = " group by os_si.corg2, "+oblikIspisa(selRB,"os_si")+", os_si.invbroj";
      String datLik = "";
//      System.out.println("AKTIV: " + aktiv);
      if(aktiv.equals("D"))
        datLik = " and datlikvidacije is null " +getAktivnostSI(aktiv);
      else if (aktiv.equals("N"))
        datLik = " and datlikvidacije is not null " +getAktivnostSI(aktiv);
      String t = hr.restart.util.Util.getUtil().clearTime(hr.restart.robno.Util.getUtil().findFirstDayOfYear()).toString();
      String qStr="";
      qStr= "select max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
            "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_si.datpromjene) as datum, "+
            "sum(os_si.osnduguje) as osnduguje, sum(os_si.osnpocetak) as osnpocetak, sum(os_si.osnpotrazuje) as osnpotrazuje, sum(os_si.ispduguje) as ispduguje, sum(os_si.isppotrazuje) as isppotrazuje, sum(os_si.isppocetak) as isppocetak "+
            "from os_si where "+getPripOrg(cOrg, pripO, "OS_SI")+getAktivnostSI(aktiv)+" "+getSIStatus(status)+getSIPorijeklo(porijeklo)+nadoprc+
            " union select max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
            "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_si.datpromjene) as datum, "+
            "sum(os_si.osnduguje) as osnduguje, sum(os_si.osnpocetak) as osnpocetak, sum(os_si.osnpotrazuje) as osnpotrazuje, sum(os_si.ispduguje) as ispduguje, sum(os_si.isppotrazuje) as isppotrazuje, sum(os_si.isppocetak) as isppocetak "+
            "from os_si where "+getPripOrg(cOrg, pripO, "OS_SI")+" and datlikvidacije is null and os_si.godproiz>='"+gpp+"' and os_si.godproiz<='"+gpz+"' "+
            getSIStatus(status)+datLik+getSIPorijeklo(porijeklo)+nadoprc;
//      System.out.println("qStrTST_SI\n"+qStr);
      return qStr;
    }

/* ----> ova je bila prije nego sam extendao ispSI iz ispOS
    public String getSND_SIIspis(String cOrg,String pocDat, String zavDat, int oj, int ol, int ib, int po, String status, String gpp, String gpz, String porijeklo, String aktiv)
    {
      String qStr="";
        qStr= "select max(os_stsi.dokument) as dokument, max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
              "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_stsi.datpromjene) as datum, "+
              "sum(os_stsi.osnpocetak) as osnpocetak, sum(os_stsi.isppocetak) as isppocetak, "+
              "sum(os_stsi.osnduguje) as osnduguje, sum(os_stsi.osnpotrazuje) as osnpotrazuje, sum(os_stsi.ispduguje) as ispduguje, sum(os_stsi.isppotrazuje) as isppotrazuje "+
              "from os_si "+
              "JOIN os_stsi on os_si.invbroj=os_stsi.invbroj and os_si.corg2= os_stsi.corg2 "+
              " and os_stsi.datpromjene >= "+pocDat+" and os_stsi.datpromjene <= "+zavDat+getSIStatus_SND(status, zavDat)+getSIPorijeklo(porijeklo)+" and os_si.godproiz>="+gpp+
//              " and os_si.godproiz<="+gpz+" and "+getSIPripOrg(cOrg, po)+getAktivnostSI(aktiv)+
              " and os_si.godproiz<="+gpz+" and "+getPripOrg(cOrg, po, "OS_SI")+getAktivnostSI(aktiv)+
              getGroupBy(oj+ol+ib, ispSI.getSelectedRB());
      return qStr;
    }
*/

    public String getSND_SIIspis(String cOrg,String pocDat, String zavDat, int oj, int ol, int ib, int po, String status, String gpp, String gpz, String porijeklo, String aktiv)
    {
      String datLik="";
     if(aktiv.equals("D"))
       datLik = " and (os_sredstvo.datlikvidacije is null or os_sredstvo.datlikvidacije > "+zavDat+") ";
     else if(aktiv.equals("N"))
        datLik = " and os_sredstvo.datlikvidacije <= "+zavDat+" and os_sredstvo.datlikvidacije is not null ";
      String qStr="";
        qStr= "select max(os_stsi.dokument) as dokument, max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
              "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_stsi.datpromjene) as datum, "+
              "sum(os_stsi.osnpocetak) as osnpocetak, sum(os_stsi.isppocetak) as isppocetak, "+
              "sum(os_stsi.osnduguje) as osnduguje, sum(os_stsi.osnpotrazuje) as osnpotrazuje, sum(os_stsi.ispduguje) as ispduguje, sum(os_stsi.isppotrazuje) as isppotrazuje "+
              "from os_si "+
              "JOIN os_stsi on os_si.invbroj=os_stsi.invbroj and os_si.corg2= os_stsi.corg2 "+
//              " and os_stsi.datpromjene >= '"+pocDat+"' and os_stsi.datpromjene <= "+zavDat+getSIStatus_SND(status, zavDat)+getSIPorijeklo(porijeklo)+" and os_si.godproiz>="+gpp+
              " and os_stsi.datpromjene <= "+zavDat+getSIStatus_SND(status, zavDat)+getSIPorijeklo(porijeklo)+" and os_si.godproiz>='"+gpp+
//              " and os_si.godproiz<="+gpz+" and "+getSIPripOrg(cOrg, po)+getAktivnostSI(aktiv)+
              "' and os_si.godproiz<='"+gpz+"' and "+getPripOrg(cOrg, po, "OS_SI")+datLik+
              getGroupBy(oj+ol+ib, ispSI.getSelectedRB(),"os_si");
      return qStr;
    }

    public String getSND_SIIspisV2(String cOrg,String pocDat, String zavDat, boolean oj, boolean ol, boolean ib, boolean po, String status, String gpp, String gpz, String porijeklo, String aktiv, int selRB) {
      String nadoprc = "";
      int pripO = 0;
      if (po) pripO = 8;
      if(oj && !ol && !ib) nadoprc = " group by os_si.corg2";
      else if(!oj && ol && !ib) nadoprc = " group by "+oblikIspisa(selRB,"os_si");
      else if(oj && ol && !ib) nadoprc = " group by os_si.corg2, "+oblikIspisa(selRB,"os_si");
      else if(!oj && !ol && ib) nadoprc = " group by os_si.invbroj";
      else if(oj && !ol && ib) nadoprc = " group by os_si.corg2, os_si.invbroj";
      else if(!oj && ol && ib) nadoprc = " group by "+oblikIspisa(selRB,"os_si")+", os_si.invbroj";
      else if(oj && ol && ib) nadoprc = " group by os_si.corg2, "+oblikIspisa(selRB,"os_si")+", os_si.invbroj";
      String datLik="";
     if(aktiv.equals("D"))
       datLik = " and (os_si.datlikvidacije is null or os_si.datlikvidacije > "+zavDat+") ";
     else if(aktiv.equals("N"))
        datLik = " and os_si.datlikvidacije <= "+zavDat+" and os_si.datlikvidacije is not null ";
      String qStr="";
        qStr= "select max(os_stsi.dokument) as dokument, max(os_si.corg2) as corg, max(os_si.brojkonta) as brojkonta, max(os_si.cgrupe) as cgrupe, max(os_si.clokacije) as clokacije, "+
              "max(os_si.cskupine) as cskupine, max(os_si.cartikla) as cartikla, max(os_si.invbroj) as invbr, max(os_si.nazsredstva) as nazsredstva, max(os_stsi.datpromjene) as datum, "+
              "sum(os_stsi.osnpocetak) as osnpocetak, sum(os_stsi.isppocetak) as isppocetak, "+
              "sum(os_stsi.osnduguje) as osnduguje, sum(os_stsi.osnpotrazuje) as osnpotrazuje, sum(os_stsi.ispduguje) as ispduguje, sum(os_stsi.isppotrazuje) as isppotrazuje "+
              "from os_si "+
              "JOIN os_stsi on os_si.invbroj=os_stsi.invbroj and os_si.corg2= os_stsi.corg2 "+
              " and os_stsi.datpromjene <= "+zavDat+getSIStatus_SND(status, zavDat)+getSIPorijeklo(porijeklo)+" and os_si.godproiz>='"+gpp+
              "' and os_si.godproiz<='"+gpz+"' and "+getPripOrg(cOrg, pripO, "OS_SI")+datLik+
              nadoprc;
//        System.out.println("qStr\n"+qStr);
      return qStr;
    }

    //Amortizacija
    public String getTST_AMIspis(String cOrg, int oj, int ol, int ib, int po)
    {
      String qStr="";
        qStr= "select max(os_metaobrada.tipamor) as tipamor, max(os_obrada2.corg) as corg, max(os_obrada2.clokacije) as clokacije, max(os_obrada2.cartikla) as cartikla, max(os_obrada2.brojkonta) as brojkonta, max(os_obrada2.invbroj) as invbroj, "+
              "max(os_obrada2.cgrupe) as cgrupe, max(os_obrada2.cskupine) as cskupine, max(os_obrada2.nazsredstva) as nazsredstva, sum(os_obrada2.zakstopa) as zakstopa, sum(os_obrada2.odlstopa) as odlstopa, "+
              "max(os_obrada2.datpromjene) as datpromjene, sum(os_obrada2.osnovica) as osnovica, sum(os_obrada2.ispravak) as ispravak, sum(os_obrada2.sadvrijed) as sadvrijed, "+
              "sum(os_obrada2.amortizacija) as amortizacija, sum(os_obrada2.pamortizacija) as pamortizacija, max(os_metaobrada.datumod) as datumod, max(os_metaobrada.datumdo) as datumdo from os_obrada2, os_metaobrada"+
//              " where "+getAMPripOrg(cOrg, po)+getAmorGroupBy(oj+ol+ib, ispAmor.getSelectedRB());
              " where "+getPripOrgAm(cOrg, po, "OS_OBRADA2")+getAmorGroupBy(oj+ol+ib, ispAmor.getSelectedRB());
      return qStr;
    }
    public String getGroupBy (int inv, int selRB, String tabl)
    {
      String tab = tabl+".";
      String qStr = "";
      if (inv == 0) qStr = "";
      else if(inv == 1) qStr = " group by "+tab+"corg2";
      else if(inv == 2) qStr = " group by "+oblikIspisa(selRB,tabl);
      else if(inv == 3) qStr = " group by "+tab+"corg2, "+oblikIspisa(selRB,tabl);
      else if(inv == 4) qStr = " group by "+tab+"invbroj";
      else if(inv == 5) qStr = " group by "+tab+"corg2, "+tab+"invbroj";
      else if(inv == 6) qStr = " group by "+oblikIspisa(selRB,tabl)+", "+tab+"invbroj";
      else if(inv == 7) qStr = " group by "+tab+"corg2, "+oblikIspisa(selRB,tabl)+", "+tab+"invbroj";
      return qStr;
    }

    public String getAmorGroupBy (int inv, int selRB)
    {
      String qStr = "";
      if (inv == 0) qStr = "";
      else if(inv == 1) qStr = " group by os_obrada2.corg";
      else if(inv == 2) qStr = " group by "+oblikIspisa(selRB,"os_obrada2");
      else if(inv == 3) qStr = " group by os_obrada2.corg, "+oblikIspisa(selRB,"os_obrada2");
      else if(inv == 4) qStr = " group by os_obrada2.invbroj";
      else if(inv == 5) qStr = " group by os_obrada2.corg, os_obrada2.invbroj";
      else if(inv == 6) qStr = " group by "+oblikIspisa(selRB,"os_obrada2")+", os_obrada2.invbroj";
      else if(inv == 7) qStr = " group by os_obrada2.corg, "+oblikIspisa(selRB,"os_obrada2")+", os_obrada2.invbroj";
      return qStr;
    }

    public String getOSStatus(String st)
    {
      String strStatus="";
      if(st.equals("I"))
        strStatus = " and os_sredstvo.status = 'I' ";
      else if(st.equals("P"))
        strStatus = " and os_sredstvo.status = 'P' ";
      else if(st.equals("A"))
        strStatus = " and os_sredstvo.status = 'A' ";
      return strStatus;
    }

    public String getOSStatus_SND(String st, String datak)
   {
     String strStatus="";
     String dat = datak.substring(0,11)+" 00:00:00'";
     if(st.equals("I"))
     {
       strStatus = " and (os_sredstvo.status = 'I' ";
       strStatus += " or (os_sredstvo.staristatus = '"+st+"' and os_sredstvo.dataktiviranja>="+dat+"))";
     }
     else if(st.equals("P"))
     {
       strStatus = " and (os_sredstvo.status = 'P' ";
       strStatus += " or (os_sredstvo.staristatus = '"+st+"' and os_sredstvo.dataktiviranja>="+dat+"))";
     }
     else if(st.equals("A"))
       strStatus = " and os_sredstvo.status = 'A' ";
     return strStatus;
    }

    public String getOSPorijeklo(String po)
   {
     String strPorijeklo="";
     if(po.equals("1"))
       strPorijeklo = " and os_sredstvo.porijeklo = '1' ";
     else if(po.equals("2"))
       strPorijeklo = " and os_sredstvo.porijeklo = '2' ";
     else if(po.equals("3"))
       strPorijeklo = " and os_sredstvo.porijeklo = '3' ";
     return strPorijeklo;
    }

    public String getSIPorijeklo(String po)
    {
      String strPorijeklo="";
      if(po.equals("1"))
        strPorijeklo = " and os_si.porijeklo = '1' ";
      else if(po.equals("2"))
        strPorijeklo = " and os_si.porijeklo = '2' ";
      else if(po.equals("3"))
       strPorijeklo = " and os_si.porijeklo = '3' ";
      return strPorijeklo;
    }

    public String getSIStatus(String st)
    {
      String strStatus="";
      if(st.equals("I"))
        strStatus = " and os_si.status = 'I' ";
      else if(st.equals("P"))
        strStatus = " and os_si.status = 'P' ";
      else if(st.equals("A"))
        strStatus = " and os_si.status = 'A' ";
      return strStatus;
    }

    public String getSIStatus_SND(String st, String datak)
  {
    String strStatus="";
    String dat = datak.substring(0,11)+" 00:00:00'";
    if(st.equals("I"))
    {
      strStatus = " and (os_si.status = 'I' ";
      strStatus += " or (os_si.staristatus = '"+st+"' and os_si.dataktiviranja>="+dat+"))";
    }
    else if(st.equals("P"))
    {
      strStatus = " and (os_si.status = 'P' ";
      strStatus += " or (os_si.staristatus = '"+st+"' and os_si.dataktiviranja>="+dat+"))";
    }
    else if(st.equals("A"))
      strStatus = " and os_si.status = 'A' ";
    return strStatus;
    }

    public void deleteDetailSet(QueryDataSet qds, int i)
    {
      String qStr = "";
      if(i ==0)
        qStr = "delete from os_promjene where invbroj='"+qds.getString("INVBROJ")+
        "' and corg='"+qds.getString("CORG")+"' and corg2='"+qds.getString("CORG2")+"' and status='"+qds.getString("STATUS")+"'";
      else
        qStr = "delete from os_stsi where invbroj='"+qds.getString("INVBROJ")+"'"+
        "' and corg='"+qds.getString("CORG")+"' and corg2='"+qds.getString("CORG2")+"' and status='"+qds.getString("STATUS")+"'";
//     System.out.println("DELETE DETAIL SET: " + qStr);
      dm.getOS_Promjene().getDatabase().executeStatement(qStr);
    }

    public void deleteOS(String sCOrg, boolean OS, boolean SI, boolean prom, boolean matPod)
    {
      String SIknjig = "";
      String SIPromknjig = "";
      String OSknjig = "";
      String OSPromknjig = "";
      String deleteP ="";
      String deleteM ="";
      String updateM ="";

      if(SI && !sCOrg.equals(""))
      {
//        SIknjig = " where "+ this.getSIPripOrg(sCOrg, 8);
        SIknjig = " where "+ this.getPripOrg(sCOrg, 8, "OS_SI");
        SIPromknjig = " where "+ this.getPripOrg(sCOrg, "OS_STSI");
//        SIPromknjig = " where "+ this.getSIPromPripOrg(sCOrg);
      }
      if(OS && !sCOrg.equals(""))
      {
        OSPromknjig = " where "+ this.getPripOrg(sCOrg, "OS_PROMJENE");
//        OSPromknjig = " where "+ this.getOSPromPripOrg(sCOrg);
//        OSknjig = " where "+ this.getOSPripOrg(sCOrg, 8);
        OSknjig = " where "+ this.getPripOrg(sCOrg, 8, "OS_SREDSTVO");
      }

      if(OS)
      {
        deleteP = "delete from os_promjene " + OSPromknjig;
        updateM = "update os_sredstvo set datlikvidacije = null, nabvrijed=0, osnpocetak=0, osnduguje=0,"+
                  "osnpotrazuje=0, revosn=0, isppocetak=0, ispduguje=0, isppotrazuje=0, revisp=0,"+
                  "amortizacija=0, revamor=0, pamortizacija=0, saldo=0 " + OSknjig;
        deleteM = "delete from os_sredstvo " + OSknjig;

        if(prom)
        {
          vl.runSQL(deleteP);
          vl.runSQL(updateM);
        }
        if(matPod)
        {
          vl.runSQL(deleteP);
          vl.runSQL(deleteM);
        }
      }
      if(SI)
      {
        deleteP = "delete from os_stsi " + SIPromknjig;
        deleteM = "delete from os_si " + SIknjig;
        updateM = "update os_si set datlikvidacije = null, nabvrijed=0, osnpocetak=0, osnduguje=0,"+
                  "osnpotrazuje=0, revosn=0, isppocetak=0, ispduguje=0, isppotrazuje=0, revisp=0,"+
                  "amortizacija=0, revamor=0, pamortizacija=0, saldo=0 " + SIknjig;
        if(prom)
        {
          vl.runSQL(deleteP);
          vl.runSQL(updateM);
        }
        if(matPod)
        {
          vl.runSQL(deleteP);
          vl.runSQL(deleteM);
        }
        if(SI && OS && matPod)
        {
          vl.runSQL("delete from os_lokacije");
          vl.runSQL("delete from os_amgrupe");
          vl.runSQL("delete from os_revskupine");
          vl.runSQL("delete from os_artikli");
        }
      }
  }

  //************** OS status (investicija i priprema)

  public QueryDataSet getStatusDS(String status, String corg, String datAk)
  {
    QueryDataSet qds = new QueryDataSet();
    String qStr = "";
    if(status.equals("I"))
    {
      qStr = "select corg as corg, invbroj as invbroj, nazsredstva as nazsred, status as status, "+
             "dataktiviranja as datum from os_sredstvo where status='I' and corg ='"+corg+"'";
    }
    else
    {
      qStr = "select corg as corg, invbroj as invbroj, nazsredstva as nazsred, status as status, "+
             "dataktiviranja as datum from os_sredstvo where status='P' and corg ='"+corg+"'";
    }
    qStr += " and datnabave <='"+datAk+"'";

    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return qds;
  }
  public String statusUpdate(String cOrg, String invBr, String datum, String stariStatus)
  {
    String qStr = "update os_sredstvo set dataktiviranja = '"+datum+"', status ='A', staristatus='"+stariStatus+"'"+
                  " where corg='"+cOrg+"' and invbroj='"+invBr+"'";
    return qStr;
  }
  public QueryDataSet refilterMemTable(QueryDataSet qds,String groupNcond,String cond){
    QueryDataSet tempQds = new QueryDataSet();

    qds.open();
    tempQds.setColumns(new Column[]{
      (Column)qds.getColumn("CORG").clone(),
      (Column)qds.getColumn("BROJKONTA").clone(),
      (Column)qds.getColumn("CPROMJENE").clone(),
      (Column)qds.getColumn("INVBROJ").clone(),
      (Column)qds.getColumn("TRI").clone(),
      (Column)qds.getColumn("CETIRI").clone(),
      (Column)qds.getColumn("PET").clone(),
      (Column)qds.getColumn("SEST").clone(),
      (Column)qds.getColumn("SEDAM").clone(),
      (Column)qds.getColumn("OSAM").clone(),
      (Column)qds.getColumn("DEVET").clone(),
      (Column)qds.getColumn("AMOR").clone(),
      (Column)qds.getColumn("DESET").clone(),
      (Column)qds.getColumn("JEDANAEST").clone(),
      (Column)qds.getColumn("DVANAEST").clone()
    });
    tempQds.open();
    
//    qds.setSort(new SortDescriptor(new String[]{groupNcond}));
    qds.first();
    
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    
    do {
      if (!ld.raLocate(tempQds,
          new String[] {groupNcond,cond},
          new String[] {qds.getString("CORG"), qds.getString("BROJKONTA")})){
        tempQds.insertRow(false);
        tempQds.setString("CORG",qds.getString("CORG"));
        tempQds.setString("BROJKONTA",qds.getString("BROJKONTA"));
        tempQds.setString("CPROMJENE",qds.getString("CPROMJENE"));
        tempQds.setString("INVBROJ",qds.getString("INVBROJ"));
        tempQds.setBigDecimal("TRI",qds.getBigDecimal("TRI"));
        tempQds.setBigDecimal("CETIRI",qds.getBigDecimal("CETIRI"));
        tempQds.setBigDecimal("PET",qds.getBigDecimal("PET"));
        tempQds.setBigDecimal("SEST",qds.getBigDecimal("SEST"));
        tempQds.setBigDecimal("SEDAM",qds.getBigDecimal("SEDAM"));
        tempQds.setBigDecimal("OSAM",qds.getBigDecimal("OSAM"));
        tempQds.setBigDecimal("DEVET",qds.getBigDecimal("DEVET"));
        tempQds.setBigDecimal("DESET",qds.getBigDecimal("DESET"));
        tempQds.setBigDecimal("AMOR",qds.getBigDecimal("AMOR"));
        tempQds.setBigDecimal("JEDANAEST",qds.getBigDecimal("JEDANAEST"));
        tempQds.setBigDecimal("DVANAEST",qds.getBigDecimal("DVANAEST"));
      } else {
        tempQds.setBigDecimal("TRI",tempQds.getBigDecimal("TRI").add(qds.getBigDecimal("TRI")));
        tempQds.setBigDecimal("CETIRI",tempQds.getBigDecimal("CETIRI").add(qds.getBigDecimal("CETIRI")));
        tempQds.setBigDecimal("PET",tempQds.getBigDecimal("PET").add(qds.getBigDecimal("PET")));
        tempQds.setBigDecimal("SEST",tempQds.getBigDecimal("SEST").add(qds.getBigDecimal("SEST")));
        tempQds.setBigDecimal("SEDAM",tempQds.getBigDecimal("SEDAM").add(qds.getBigDecimal("SEDAM")));
        tempQds.setBigDecimal("OSAM",tempQds.getBigDecimal("OSAM").add(qds.getBigDecimal("OSAM")));
        tempQds.setBigDecimal("DEVET",tempQds.getBigDecimal("DEVET").add(qds.getBigDecimal("DEVET")));
        tempQds.setBigDecimal("DESET",tempQds.getBigDecimal("DESET").add(qds.getBigDecimal("DESET")));
        tempQds.setBigDecimal("AMOR",tempQds.getBigDecimal("AMOR").add(qds.getBigDecimal("AMOR")));
        tempQds.setBigDecimal("JEDANAEST",tempQds.getBigDecimal("JEDANAEST").add(qds.getBigDecimal("JEDANAEST")));
        tempQds.setBigDecimal("DVANAEST",tempQds.getBigDecimal("DVANAEST").add(qds.getBigDecimal("DVANAEST")));
      }
    } while (qds.next());
    return tempQds;
  }

  public QueryDataSet refilterMemTable(QueryDataSet qds,String groupBy)
  {
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.showInFrame(qds,"");
    QueryDataSet tempQds = new QueryDataSet();
    BigDecimal tri = new BigDecimal(0);
    BigDecimal cetiri = new BigDecimal(0);
    double pet = 0;
    BigDecimal sest = new BigDecimal(0);
    BigDecimal sedam = new BigDecimal(0);
    BigDecimal osam = new BigDecimal(0);
    BigDecimal devet = new BigDecimal(0);
    BigDecimal amor = new BigDecimal(0);
    double deset = 0;
    double jedanaest = 0;
    double dvanaest = 0;
    qds.open();
    tempQds.setColumns(new Column[]{
      (Column)qds.getColumn("CORG").clone(),
      (Column)qds.getColumn("BROJKONTA").clone(),
      (Column)qds.getColumn("CPROMJENE").clone(),
      (Column)qds.getColumn("INVBROJ").clone(),
      (Column)qds.getColumn("TRI").clone(),
      (Column)qds.getColumn("CETIRI").clone(),
      (Column)qds.getColumn("PET").clone(),
      (Column)qds.getColumn("SEST").clone(),
      (Column)qds.getColumn("SEDAM").clone(),
      (Column)qds.getColumn("OSAM").clone(),
      (Column)qds.getColumn("DEVET").clone(),
      (Column)qds.getColumn("AMOR").clone(),
      (Column)qds.getColumn("DESET").clone(),
      (Column)qds.getColumn("JEDANAEST").clone(),
      (Column)qds.getColumn("DVANAEST").clone()
    });
    tempQds.open();

    qds.setSort(new SortDescriptor(new String[]{groupBy}));
    qds.first();
    LinkedList kljuc = new LinkedList();
    kljuc.add(qds.getString(groupBy));

    for(int i = 0; i<qds.getRowCount(); i++)
    {
      if((qds.getString(groupBy).equals(kljuc.getLast().toString()))) qds.next();
      else
      {
        kljuc.add(qds.getString(groupBy));
        qds.next();
      }
    }
//    qds = replaceAmor_Isp(qds);
    qds.first();

    String corg="";
    String bk="";
    String trenutniKljuc="";
    String cPromjene = "";
    String invBroj = "";
    for(int i = 0; i<kljuc.size();i++)
    {
      qds.first();
      tri=new BigDecimal(0);
      cetiri=new BigDecimal(0);
      pet=0;
      sest=new BigDecimal(0);
      sedam=new BigDecimal(0);
      osam=new BigDecimal(0);
      deset=0;
      jedanaest=0;
      dvanaest=0;
      amor=new BigDecimal(0);
      devet=new BigDecimal(0);
      while(qds.inBounds())
      {
        if(qds.getString(groupBy).equals(kljuc.get(i).toString()))
        {
          tri=tri.add(qds.getBigDecimal("TRI"));
          cetiri=cetiri.add(qds.getBigDecimal("CETIRI"));
          pet+=qds.getBigDecimal("PET").doubleValue();
          sest=sest.add(qds.getBigDecimal("SEST"));
          sedam=sedam.add(qds.getBigDecimal("SEDAM"));
          osam=osam.add(qds.getBigDecimal("OSAM"));
          devet=devet.add(qds.getBigDecimal("DEVET"));
          amor=amor.add(qds.getBigDecimal("AMOR"));
//          System.out.println("amor - " + amor +" , "+qds.getBigDecimal("AMOR"));
          deset+=qds.getBigDecimal("DESET").doubleValue();
          jedanaest+=qds.getBigDecimal("JEDANAEST").doubleValue();
          dvanaest+=qds.getBigDecimal("DVANAEST").doubleValue();
          trenutniKljuc = qds.getString(groupBy);
          corg=qds.getString("CORG");
          bk=qds.getString("BROJKONTA");
          cPromjene=qds.getString("CPROMJENE");
          invBroj = qds.getString("INVBROJ");
        }
        qds.next();
      }
      if(trenutniKljuc.equals(kljuc.get(i).toString()))
      {
        tempQds.insertRow(false);
        tempQds.setString("CORG",corg);
        tempQds.setString("BROJKONTA",bk);
        tempQds.setString("CPROMJENE",cPromjene);
        tempQds.setBigDecimal("TRI",tri);
        tempQds.setBigDecimal("CETIRI",cetiri);
        tempQds.setBigDecimal("PET",new BigDecimal(pet));
        tempQds.setBigDecimal("SEST",sest);
        tempQds.setBigDecimal("SEDAM",sedam);
        tempQds.setBigDecimal("OSAM",osam);
        tempQds.setBigDecimal("AMOR",amor);
        tempQds.setBigDecimal("DEVET",devet);
        tempQds.setBigDecimal("DESET",new BigDecimal(deset));
        tempQds.setBigDecimal("JEDANAEST",new BigDecimal(jedanaest));
        tempQds.setBigDecimal("DVANAEST",new BigDecimal(dvanaest));
        tempQds.setString("INVBROJ",invBroj);
      }
    }
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(tempQds);
    return tempQds;
  }

  public QueryDataSet refilterMemTable2(QueryDataSet qds,String groupBy)
    {
      QueryDataSet tempQds = new QueryDataSet();
      BigDecimal tri = new BigDecimal(0);
      BigDecimal cetiri = new BigDecimal(0);
      double pet = 0;
      BigDecimal sest = new BigDecimal(0);
      BigDecimal sedam = new BigDecimal(0);
      BigDecimal osam = new BigDecimal(0);
      BigDecimal devet = new BigDecimal(0);
      BigDecimal amor = new BigDecimal(0);
      double deset = 0;
      double jedanaest = 0;
      double dvanaest = 0;

      qds.open();
      tempQds.setColumns(new Column[]{
        (Column)qds.getColumn("CORG").clone(),
        (Column)qds.getColumn("BROJKONTA").clone(),
        (Column)qds.getColumn("CPROMJENE").clone(),
        (Column)qds.getColumn("INVBROJ").clone(),
        (Column)qds.getColumn("TRI").clone(),
        (Column)qds.getColumn("CETIRI").clone(),
        (Column)qds.getColumn("PET").clone(),
        (Column)qds.getColumn("SEST").clone(),
        (Column)qds.getColumn("SEDAM").clone(),
        (Column)qds.getColumn("OSAM").clone(),
        (Column)qds.getColumn("DEVET").clone(),
        (Column)qds.getColumn("AMOR").clone(),
        (Column)qds.getColumn("DESET").clone(),
        (Column)qds.getColumn("JEDANAEST").clone(),
        (Column)qds.getColumn("DVANAEST").clone()
      });
      tempQds.open();

      qds.setSort(new SortDescriptor(new String[]{groupBy}));
      qds.first();
      LinkedList kljuc = new LinkedList();
      kljuc.add(qds.getString(groupBy));

      for(int i = 0; i<qds.getRowCount(); i++)
      {
        if((qds.getString(groupBy).equals(kljuc.getLast().toString()))) qds.next();
        else
        {
          kljuc.add(qds.getString(groupBy));
          qds.next();
        }
      }
//      qds = replaceAmor_Isp(qds);

      qds.first();
      String corg="";
      String bk="";
      String trenutniKljuc="";
      String cPromjene = "";
      String invBr ="";
      String tempInv="";
      String insertInv="";
      for(int i = 0; i<kljuc.size();i++)
      {
        qds.first();
        tempInv="";
        tri=new BigDecimal(0);
        cetiri=new BigDecimal(0);
        pet=0;
        sest=new BigDecimal(0);
        sedam=new BigDecimal(0);
        osam=new BigDecimal(0);
        deset=0;
        jedanaest=0;
        dvanaest=0;
        amor=new BigDecimal(0);
        devet=new BigDecimal(0);
        while(qds.inBounds())
        {
          invBr = qds.getString("INVBROJ");
          if(qds.getString(groupBy).equals(kljuc.get(i).toString()))
          {
            if(tempInv.equals(""))
              tempInv = qds.getString("INVBROJ");
//            System.out.println("QDS: " + qds.getString("INVBROJ"));
//            System.out.println("temp; " + tempInv);
            if(qds.getString("INVBROJ").equals(tempInv))
            {
              tri=tri.add(qds.getBigDecimal("TRI"));
              cetiri=cetiri.add(qds.getBigDecimal("CETIRI"));
              pet+=qds.getBigDecimal("PET").doubleValue();
              sest=sest.add(qds.getBigDecimal("SEST"));
              sedam=sedam.add(qds.getBigDecimal("SEDAM"));
              osam=osam.add(qds.getBigDecimal("OSAM"));
              devet=devet.add(qds.getBigDecimal("DEVET"));
              amor=amor.add(qds.getBigDecimal("AMOR"));
              deset+=qds.getBigDecimal("DESET").doubleValue();
              jedanaest+=qds.getBigDecimal("JEDANAEST").doubleValue();
              dvanaest+=qds.getBigDecimal("DVANAEST").doubleValue();
              trenutniKljuc = qds.getString(groupBy);
              corg=qds.getString("CORG");
              bk=qds.getString("BROJKONTA");
              cPromjene=qds.getString("CPROMJENE");
              tempInv = qds.getString("INVBROJ");
              insertInv =qds.getString("INVBROJ");
            }
            else
            {
              tempQds.insertRow(false);
              tempQds.setString("CORG",corg);
              tempQds.setString("INVBROJ",tempInv);
              tempQds.setString("BROJKONTA",bk);
              tempQds.setString("CPROMJENE",cPromjene);
              tempQds.setBigDecimal("TRI",tri);
              tempQds.setBigDecimal("CETIRI",cetiri);
              tempQds.setBigDecimal("PET",new BigDecimal(pet));
              tempQds.setBigDecimal("SEST",sest);
              tempQds.setBigDecimal("SEDAM",sedam);
              tempQds.setBigDecimal("OSAM",osam);
              tempQds.setBigDecimal("AMOR",amor);
              tempQds.setBigDecimal("DEVET",devet);
              tempQds.setBigDecimal("DESET",new BigDecimal(deset));
              tempQds.setBigDecimal("JEDANAEST",new BigDecimal(jedanaest));
              tempQds.setBigDecimal("DVANAEST",new BigDecimal(dvanaest));

              tri=qds.getBigDecimal("TRI");
              cetiri=qds.getBigDecimal("CETIRI");
              pet=qds.getBigDecimal("PET").doubleValue();
              sest=qds.getBigDecimal("SEST");
              sedam=qds.getBigDecimal("SEDAM");
              osam=qds.getBigDecimal("OSAM");
              devet=qds.getBigDecimal("DEVET");
              amor=qds.getBigDecimal("AMOR");
              deset=qds.getBigDecimal("DESET").doubleValue();
              jedanaest=qds.getBigDecimal("JEDANAEST").doubleValue();
              dvanaest=qds.getBigDecimal("DVANAEST").doubleValue();
              insertInv =qds.getString("INVBROJ");
            }
          }

          qds.next();
        }
        if(trenutniKljuc.equals(kljuc.get(i).toString()))
        {
          tempQds.insertRow(false);
          tempQds.setString("CORG",corg);
          tempQds.setString("INVBROJ",insertInv);
          tempQds.setString("BROJKONTA",bk);
          tempQds.setString("CPROMJENE",cPromjene);
          tempQds.setBigDecimal("TRI",tri);
          tempQds.setBigDecimal("CETIRI",cetiri);
          tempQds.setBigDecimal("PET",new BigDecimal(pet));
          tempQds.setBigDecimal("SEST",sest);
          tempQds.setBigDecimal("SEDAM",sedam);
          tempQds.setBigDecimal("OSAM",osam);
          tempQds.setBigDecimal("AMOR",amor);
          tempQds.setBigDecimal("DEVET",devet);
          tempQds.setBigDecimal("DESET",new BigDecimal(deset));
          tempQds.setBigDecimal("JEDANAEST",new BigDecimal(jedanaest));
          tempQds.setBigDecimal("DVANAEST",new BigDecimal(dvanaest));
        }
      }
      return tempQds;
  }


  public QueryDataSet replaceAmor_Isp(QueryDataSet qds)
  {
    String cProm ="";
    lookupData.getlookupData().raLocate(dm.getOS_Vrpromjene(), new String[]{"NAZPROMJENE"}, new String[]{"Amortizacija"});
    cProm = dm.getOS_Vrpromjene().getString("CPROMJENE");

    if(!qds.isOpen())
      qds.open();

    qds.first();
    qds.getColumn("CORG").setRowId(true);
    while (qds.inBounds())
    {
      if(qds.getString("CPROMJENE").equals(cProm))
        qds.setBigDecimal("DEVET", new BigDecimal(0));
      else
        qds.setBigDecimal("AMOR", new BigDecimal(0));
      qds.next();
    }
    return qds;
  }
//  public boolean checkKontoIspPK(String bk)
//  {
//    String qStr = "select kontoisp from os_kontaisp where brojkonta ='"+bk+"'";
//    QueryDataSet qds = new QueryDataSet();
//    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//
//    qds.open();
//    if(qds.getRowCount()>0)
//      return true;
//    return false;
//  }
//
//  public boolean checkKontoAmorPK(String bk)
//  {
//    String qStr = "select kontoamor from os_kontaisp where brojkonta ='"+bk+"'";
//    QueryDataSet qds = new QueryDataSet();
//    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//
//    qds.open();
//    if(qds.getRowCount()>0)
//      return true;
//    return false;
//  }

  public boolean checkKontoIspPK(String bk, String isp, String amor)
  {
  	String qStr = "brojkonta ='"+bk+"' and kontoisp='"+isp+"' and kontoamor='"+amor+"'";
  	return OS_Kontaisp.getDataModule().getRowCount(qStr) > 0;
  }

  public static double getBDouble(ReadRow ds, String colnm) {
    if (ds.getColumn(colnm).getSqlType() == java.sql.Types.DOUBLE) {
      return ds.getDouble(colnm);
    } else {
      return ds.getBigDecimal(colnm).doubleValue();
    }
  }

  ///****** STARE METODE ZA PRIPADAJUCE CORGOVE ZAMJENJENE SA getPripOrg(parametri)

  /*
  //Osnovna sredstva
  String getOSPripOrg(String str, int mode) {
    int i=0;
    String cVrati;
    if (mode==8) {
      cVrati=" OS_SREDSTVO.CORG2 in (";
      com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
      tds.first();
      do {
        if (i>0) {
          cVrati=cVrati+',';
        }
        i++;
        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
        tds.next();
      } while (tds.inBounds());
      cVrati=cVrati+")";
    }
    else {
      cVrati=" OS_SREDSTVO.CORG2='"+str+"'";
    }
    return cVrati;
  }

  String getOSPromPripOrg(String str) {
    int i=0;
    String cVrati;

      cVrati=" OS_PROMJENE.CORG2 in (";
      com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
      tds.first();
      do {
        if (i>0) {
          cVrati=cVrati+',';
        }
        i++;
        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
        tds.next();
      } while (tds.inBounds());
      cVrati=cVrati+")";

    return cVrati;
  }

//Sitni inventar
  String getSIPripOrg(String str, int mode) {
    int i=0;
    String cVrati;
    if (mode==8) {
      cVrati=" OS_SI.CORG2 in (";
      com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
      tds.first();
      do {
        if (i>0) {
          cVrati=cVrati+',';
        }
        i++;
        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
        tds.next();
      } while (tds.inBounds());
      cVrati=cVrati+")";
    }
    else {
      cVrati=" OS_SI.CORG2='"+str+"'";
    }
    return cVrati;
  }

  String getSIPromPripOrg(String str) {
    int i=0;
    String cVrati;
      cVrati=" OS_STSI.CORG2 in (";
      com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
      tds.first();
      do {
        if (i>0) {
          cVrati=cVrati+',';
        }
        i++;
        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
        tds.next();
      } while (tds.inBounds());
      cVrati=cVrati+")";

    return cVrati;
  }

//Amortizacija
  String getAMPripOrg(String str, int mode) {
    int i=0;
    String cVrati;
    if (mode==8) {
      cVrati=" OS_OBRADA2.CORG in (";
      com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
      tds.first();
      do {
        if (i>0) {
          cVrati=cVrati+',';
        }
        i++;
        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
        tds.next();
      } while (tds.inBounds());
      cVrati=cVrati+")";
    }
    else {
      cVrati=" OS_OBRADA2.CORG='"+str+"'";
    }
    return cVrati;
  }

//Likvidacija
  String getLIKPripOrg(String str, int mode) {
    int i=0;
    String cVrati;
    if (mode==8) {
      cVrati=" OS_OBRADA4.CORG in (";
      com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
      tds.first();
      do {
        if (i>0) {
          cVrati=cVrati+',';
        }
        i++;
        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
        tds.next();
      } while (tds.inBounds());
      cVrati=cVrati+")";
    }
    else {
      cVrati=" OS_OBRADA4.CORG='"+str+"'";
    }
    return cVrati;
  }

   String getLikviPripCorg(String str ){
    int i=0;
    String cVrati;
    cVrati=" OS_SREDSTVO.CORG2 in (";
    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
    tds.first();
    do {
      if (i>0) {
        cVrati=cVrati+',';
      }
      i++;
      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
      tds.next();
    } while (tds.inBounds());
    cVrati=cVrati+")";

    return cVrati;
  }

  String getLikviSIPripCorg(String str ){
    int i=0;
    String cVrati;
    cVrati=" OS_SI.CORG2 in (";
    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
    tds.first();
    do {
      if (i>0) {
        cVrati=cVrati+',';
      }
      i++;
      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
      tds.next();
    } while (tds.inBounds());
    cVrati=cVrati+")";

    return cVrati;
  }

  String getKnjizPripCorg(String str ){
    int i=0;
    String cVrati;
    cVrati=" OS_PROMJENE.CORG2 in (";
    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
    tds.first();
    do {
      if (i>0) {
        cVrati=cVrati+',';
      }
      i++;
      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
      tds.next();
    } while (tds.inBounds());
    cVrati=cVrati+")";
    return cVrati;
  }

  String getKnjizSIPripCorg(String str ){
    int i=0;
    String cVrati;
    cVrati=" OS_STSI.CORG2 in (";
    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(str);
    tds.first();
    do {
      if (i>0) {
        cVrati=cVrati+',';
      }
      i++;
      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
      tds.next();
    } while (tds.inBounds());
    cVrati=cVrati+")";
    return cVrati;
  }

  */
  //
}
