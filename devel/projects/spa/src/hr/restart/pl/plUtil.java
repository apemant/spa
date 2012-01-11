/****license*****************************************************************
**   file: plUtil.java
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

import hr.restart.baza.GrIzvZnac;
import hr.restart.baza.Grizvprim;
import hr.restart.baza.Odbici;
import hr.restart.baza.Plosnprim;
import hr.restart.baza.SumePrim;
import hr.restart.baza.Vrsteodb;
import hr.restart.baza.Vrsteprim;
import hr.restart.baza.dM;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class plUtil {

  private static plUtil myUtil;
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();

  public static plUtil getPlUtil()
  {
    if(myUtil==null)
      myUtil = new plUtil();
    return myUtil;
  }

  public plUtil() {
  }

  public short getMaxOdbiciRBR(short vrOdb, String kljuc)
  {
    QueryDataSet qds = new QueryDataSet();

    String qStr = "select max(rbrodb) as rbr from odbici where cvrodb="+vrOdb+
                  " and ckey='"+kljuc+"'";
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    return (short)(qds.getShort("RBR") + 1);
  }

 public BigDecimal getIznos(short vrOdb)
 {
   QueryDataSet qds = new QueryDataSet();

   String qStr = "select iznos as iznos from vrsteodb where cvrodb="+vrOdb;
   qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
   qds.open();

   return (qds.getBigDecimal("IZNOS"));
  }

  public BigDecimal getStopa(short vrOdb)
  {
    QueryDataSet qds = new QueryDataSet();

    String qStr = "select stopa as stopa from vrsteodb where cvrodb="+vrOdb;
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();

    return (qds.getBigDecimal("STOPA"));
  }

  public short getMaxOdbiciRBR(short vrOdb, String kljuc, String kljuc2)
  {
    QueryDataSet qds = new QueryDataSet();

    String qStr = "select max(rbrodb) as rbr from odbici where cvrodb="+vrOdb+
                  " and ckey='"+kljuc+"' and ckey2='"+kljuc2+"'";
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();

    return (short)(qds.getShort("RBR") + 1);
  }

   public short getMaxIniPrimRBR(short cVrp)
  {
    QueryDataSet qds = new QueryDataSet();
    String qStr = "select max(rbr) as rbr from iniprim where cvrp="+cVrp;
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    return (short)(qds.getShort("RBR") + 1);
  }

  public String getStrNivo(int vrodb)
  {
    QueryDataSet qds = new QueryDataSet();

    String qStr = "select NIVOODB as NIVOODB from vrsteodb where cvrodb="+ vrodb;
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));

    qds.open();
    return qds.getString("NIVOODB");
  }

  public String getOsnovica(int vrodb)
  {
    QueryDataSet qds = new QueryDataSet();

    String qStr = "select OSNOVICA as OSNOVICA from vrsteodb where cvrodb="+ vrodb;
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    return qds.getString("OSNOVICA");
  }

/*
  public QueryDataSet getDefDS(int vrodb)
  {
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(new Column[] {
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("LOKK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("AKTIV").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("CVRODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("CKEY").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("RBRODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PNB1").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PNB2").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("IZNOS").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("STOPA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("DATPOC").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("DATZAV").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("GLAVNICA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("RATA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("SALDO").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("STAVKA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PARAMETRI").clone()
    });
    String qStr = "select * from odbici where cvrodb="+ vrodb;
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    return qds;
  }


  public QueryDataSet getDefDS(int vrodb, String nivoOdb)
  {
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(new Column[] {
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("LOKK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("AKTIV").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("CVRODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("CKEY").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("RBRODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PNB1").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PNB2").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("IZNOS").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("STOPA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("DATPOC").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("DATZAV").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("GLAVNICA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("RATA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("SALDO").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("STAVKA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PARAMETRI").clone()
    });

    String qStr="";
    if(nivoOdb.length()==2)
      qStr = "select * from odbici where cvrodb="+ vrodb+" and ckey2 =''";
    else
      qStr = "select * from odbici where cvrodb="+ vrodb+" and ckey2 != ''";
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    return qds;
  }


  public QueryDataSet getDefDS(String inStr)
  {
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(new Column[] {
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("LOKK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("AKTIV").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("CVRODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("CKEY").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("RBRODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PNB1").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PNB2").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("IZNOS").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("STOPA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("DATPOC").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("DATZAV").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("GLAVNICA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("RATA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("SALDO").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("STAVKA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getOdbici().getColumn("PARAMETRI").clone()
    });

    String qStr = "select * from odbici where cvrodb in ("+inStr+")";

    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    return qds;
  }

  public QueryDataSet getvrOdbQDS(String no, String to, String vos, String os)
  {
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(new Column[] {
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("LOKK").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("AKTIV").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("CVRODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("OPISVRODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("NIVOODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("TIPODB").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("VRSTAOSN").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("OSNOVICA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("CPOV").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("IZNOS").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("STOPA").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getVrsteodb().getColumn("PARAMETRI").clone()
    });
    String qStr = "select * from vrsteodb where nivoodb='"+no+"' and tipodb='"+
                  to+"' and vrstaosn='"+vos+"' and osnovica ='"+os+"'";

    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    return qds;
  }
  */

  //**************************************
  public QueryDataSet getDefDS(int vrodb)
  {
    QueryDataSet qds = Odbici.getDataModule().getFilteredDataSet("cvrodb="+ vrodb);
    qds.open();
    return qds;
  }

  public QueryDataSet getDefDS(int vrodb, String nivoOdb)
  {
    QueryDataSet qds;
    if(nivoOdb.length()==2)
      qds = Odbici.getDataModule().getFilteredDataSet("cvrodb="+ vrodb+" and ckey2 =''");
    else
      qds = Odbici.getDataModule().getFilteredDataSet("cvrodb="+ vrodb+" and ckey2 != ''");
    qds.open();
    return qds;
  }

  public QueryDataSet getDefDS(String inStr)
  {
    QueryDataSet qds = Odbici.getDataModule().getFilteredDataSet("cvrodb in ("+inStr+")");
    qds.open();
    return qds;
  }

  public QueryDataSet getvrOdbQDS(String no, String to, String vos, String os)
  {
    QueryDataSet qds = Vrsteodb.getDataModule().getFilteredDataSet("nivoodb='"+no+"' and tipodb='"+
                  to+"' and vrstaosn='"+vos+"' and osnovica ='"+os+"'");
    qds.open();
    return qds;
  }
//******************************
  public void recalcOdbiciRBR(short cvrodb, short rbr)
  {
    Valid vl = Valid.getValid();
    String qStr = "update odbici set rbrodb = rbrodb-1 where cvrodb="+cvrodb+
                  " and rbrodb >" + rbr;
    vl.runSQL(qStr);
  }

  public void recalcIniPrimRBR(short cvrp, short rbr)
  {
    Valid vl = Valid.getValid();
    String qStr = "update iniprim set rbr = rbr-1 where cvrp="+cvrp+
                  " and rbr >" + rbr;
    vl.runSQL(qStr);
  }

  public boolean checkSumeUnique(QueryDataSet qds)
  {
    QueryDataSet temp = new QueryDataSet();
    qds.open();
    String qStr = "select * from sumeprim where csume="+qds.getInt("CSUME")+" and CVRP="+qds.getShort("CVRP");
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkOsnoviceUnique(QueryDataSet qds)
  {
    QueryDataSet temp = new QueryDataSet();
    qds.open();
    String qStr = "select * from PLOSNPRIM where cosn="+qds.getShort("COSN")+" and CVRP="+qds.getShort("CVRP");
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkGrIzvUnique(short cizv, short cgrizv)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from grupeizv where cizv ="+cizv+" and CGRIZV="+cgrizv;
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkGlobalOdbiciUnique(QueryDataSet ds)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from odbici where cvrodb ="+ds.getShort("CVRODB")+
                  " and ckey='"+ds.getString("CKEY")+"'"+
                  " and ckey2='"+ds.getString("CKEY2")+"' and rbrodb="+ds.getShort("RBRODB");
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }


  public boolean checkGrizvPrimUnique(short cizv, short cgrizv, short cvrp)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from grizvprim where cizv ="+cizv+" and CGRIZV="+cgrizv +" and cvrp="+cvrp;
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkGrizvOdbUnique(short cizv, short cgrizv, short cvrodb)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from grizvodb where cizv ="+cizv+" and CGRIZV="+cgrizv +" and cvrodb="+cvrodb;
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }
  public boolean checkGrizvZnacUnique(short cizv, short cgrizv, short cznac)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from grizvznac where cizv ="+cizv+" and CGRIZV="+cgrizv +" and cznac="+cznac;
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }


  public boolean checkOrgRadUnique(String corg, String cradnik)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from orgrad where corg ='"+corg+"' and CRADNIK='"+cradnik +"'";
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkVriBodaUnique(short god, short mjesec)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from vriboda where godina ="+god+" and mjobr="+mjesec;
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkSumePrimUnique(short cvrp, int sume)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from sumeprim where cvrp ="+cvrp+" and csume="+sume;
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkOsnPrimUnique(short cvrp, short sume)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from plosnprim where cvrp ="+cvrp+" and cosn="+sume;
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkFondSatiUnique(short god, short mjesec)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from fondsati where godina ="+god+" and mjesec="+mjesec+" and knjig='"+OrgStr.getKNJCORG()+"'";
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

//  public QueryDataSet getgrizvPrimDS(short cizv, short cgrizv)
//  {
//    QueryDataSet qds = new QueryDataSet();
//    qds.setColumns(new Column[]{
//    (Column) dm.getGrizvprim().getColumn("CIZV").clone(),
//    (Column) dm.getGrizvprim().getColumn("CGRIZV").clone(),
//    (Column) dm.getGrizvprim().getColumn("CVRP").clone()
//    });
//    String qStr = "select * from grizvprim where cizv ="+cizv+" and cgrizv="+ cgrizv;
//    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    return qds;
//  }

  public QueryDataSet getgrizvPrimDS(short cizv, short cgrizv)
  {
    QueryDataSet qds = Grizvprim.getDataModule().getFilteredDataSet("cizv ="+cizv+" and cgrizv="+ cgrizv);
    return qds;
  }

  public QueryDataSet getgrizvZnacDS(short cizv, short cgrizv)
  {
    QueryDataSet qds = GrIzvZnac.getDataModule().getFilteredDataSet("cizv ="+cizv+" and cgrizv="+ cgrizv);
    return qds;
  }

  public QueryDataSet getgrizvOdbDS(short cizv, short cgrizv)
  {
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(new Column[]{
    (Column) dm.getGrizvodb().getColumn("CIZV").clone(),
    (Column) dm.getGrizvodb().getColumn("CGRIZV").clone(),
    (Column) dm.getGrizvodb().getColumn("CVRODB").clone()
    });
    String qStr = "select * from grizvodb where cizv ="+cizv+" and cgrizv="+ cgrizv;
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return qds;
  }

//  public QueryDataSet getgrizvOdbDS(short cizv, short cgrizv)
//  {
//    QueryDataSet qds = Grizvprim.getDataModule().getFilteredDataSet("cizv ="+cizv+" and cgrizv="+ cgrizv);
//    return qds;
//  }


//  public QueryDataSet getsumePrimDS(short cvrp)
//  {
//    QueryDataSet qds = new QueryDataSet();
//    qds.setColumns(new Column[]{
//    (Column) dm.getSumePrim().getColumn("CVRP").clone(),
//    (Column) dm.getSumePrim().getColumn("CSUME").clone()
//    });
//    String qStr = "select * from sumeprim where cvrp ="+cvrp;
//    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    return qds;
//  }
//
  public QueryDataSet getsumePrimDS(short cvrp)
  {
    QueryDataSet qds = SumePrim.getDataModule().getFilteredDataSet("cvrp ="+cvrp);
    return qds;
  }

//  public QueryDataSet getosnPrimDS(short cvrp)
//  {
//    QueryDataSet qds = new QueryDataSet();
//    qds.setColumns(new Column[]{
//    (Column) dm.getPlosnprim().getColumn("CVRP").clone(),
//    (Column) dm.getPlosnprim().getColumn("COSN").clone()
//    });
//    String qStr = "select * from plosnprim where cvrp ="+cvrp;
//    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    return qds;
//  }


  public QueryDataSet getosnPrimDS(short cvrp)
  {
    QueryDataSet qds = Plosnprim.getDataModule().getFilteredDataSet("cvrp ="+cvrp);
    return qds;
  }


  public QueryDataSet getgrizvPrimDS2(short cvrp)
  {
    QueryDataSet qds = Grizvprim.getDataModule().getFilteredDataSet("cvrp ="+cvrp);
    return qds;
  }



  public QueryDataSet getOrgRadDS(String cradnika)
  {
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(new Column[]{
    (Column) dm.getOrgrad().getColumn("CORG").clone(),
    (Column) dm.getOrgrad().getColumn("CRADNIK").clone(),
    (Column) dm.getOrgrad().getColumn("UDIORADA").clone()
    });
    String qStr = "select * from orgrad where cradnik ='"+cradnika+"'";
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return qds;
  }

//  public QueryDataSet getArhivaDS()
//  {
//    QueryDataSet qds = new QueryDataSet();
//    String qStr = "select * from vrsteprim";
//    qds.setColumns(new Column[]{
//    (Column) dm.getVrsteprim().getColumn("CVRP").clone(),
//    (Column) dm.getVrsteprim().getColumn("NAZIV").clone(),
//    (Column) dm.getVrsteprim().getColumn("COBR").clone(),
//    (Column) dm.getVrsteprim().getColumn("COSN").clone(),
//    (Column) dm.getVrsteprim().getColumn("RSOO").clone(),
//    (Column) dm.getVrsteprim().getColumn("RNALOG").clone(),
//    (Column) dm.getVrsteprim().getColumn("REGRES").clone(),
//    (Column) dm.getVrsteprim().getColumn("CGRPRIM").clone(),
//    (Column) dm.getVrsteprim().getColumn("CVRPARH").clone(),
//    (Column) dm.getVrsteprim().getColumn("STAVKA").clone()
//    });
//    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    return qds;
//  }

  public QueryDataSet getArhivaDS()
  {
    QueryDataSet qds = Vrsteprim.getDataModule().copyDataSet();
//    QueryDataSet qds = new QueryDataSet();
//    String qStr = "select * from vrsteprim";
//    qds.setColumns(new Column[]{
//    (Column) dm.getVrsteprim().getColumn("CVRP").clone(),
//    (Column) dm.getVrsteprim().getColumn("NAZIV").clone(),
//    (Column) dm.getVrsteprim().getColumn("COBR").clone(),
//    (Column) dm.getVrsteprim().getColumn("COSN").clone(),
//    (Column) dm.getVrsteprim().getColumn("RSOO").clone(),
//    (Column) dm.getVrsteprim().getColumn("RNALOG").clone(),
//    (Column) dm.getVrsteprim().getColumn("REGRES").clone(),
//    (Column) dm.getVrsteprim().getColumn("CGRPRIM").clone(),
//    (Column) dm.getVrsteprim().getColumn("CVRPARH").clone(),
//    (Column) dm.getVrsteprim().getColumn("STAVKA").clone()
//    });
//    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return qds;
  }

  public void kontolaUnosaTexta(hr.restart.swing.JraTextField tf, KeyEvent e)
  {
     try {
       if(tf.getSelectedText().length()!=0)
       {
        if(e.getKeyCode()!=KeyEvent.VK_LEFT && e.getKeyCode()!=KeyEvent.VK_RIGHT)
        {
          String insert2 = tf.getText().substring(tf.getSelectionEnd(), tf.getSelectionEnd()+1);
        }
       }
     }
     catch (Exception ex) {

     }


    if(tf.getText().length()>3 && e.getKeyCode()!=KeyEvent.VK_BACK_SPACE
        && tf.getCaretPosition()==4)
      tf.setEditable(false);
    else if(tf.getText().length()>3 && tf.getCaretPosition()<4)
    {
      if(e.getKeyCode()!=KeyEvent.VK_LEFT && e.getKeyCode()!=KeyEvent.VK_RIGHT /*&& e.getID() != KeyEvent.VK_SHIFT*/)
      {
        StringBuffer test = new StringBuffer(tf.getText());
        char[] typed = new char[]{e.getKeyChar()};
        test.replace(tf.getCaretPosition(),tf.getCaretPosition()+1, new String(typed));
        int caret = tf.getCaretPosition();
        tf.setText(test.toString());
        tf.setCaretPosition(caret+1);
      }
    }
    else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE)
      tf.setEditable(true);
  }

  public String getRadnikCorg(String cRadnik)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select corg from radnici where cradnik ='"+cRadnik+"'";
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    return temp.getString("corg");
  }

//  public boolean checkVRPStavke(short cvrp)
//  {
//    QueryDataSet temp1 = new QueryDataSet();
//    QueryDataSet temp2 = new QueryDataSet();
//    QueryDataSet temp3 = new QueryDataSet();
//    QueryDataSet temp4 = new QueryDataSet();
//    String qStr1 = "select * from grizvprim where cvrp ="+cvrp;
//    String qStr2 = "select * from plosnprim where cvrp ="+cvrp;
//    String qStr3 = "select * from sumeprim where cvrp ="+cvrp;
//    String qStr4 = "select * from odbici where (cvrodb in "+
//                   "(select cvrodb from vrsteodb where nivoodb like 'ZA%') and ckey='"+cvrp+"')"+
//                   "or cvrodb in (select cvrodb from vrsteodb where nivoodb like '%ZA') and "+
//                   "ckey2='"+cvrp+"'";
//    temp1.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr1));
//    temp2.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr2));
//    temp3.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr3));
//    temp4.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr4));
//    temp1.open();
//    temp2.open();
//    temp3.open();
//    temp4.open();
//
//    if((temp1.getRowCount()+temp2.getRowCount()+temp3.getRowCount()+temp4.getRowCount())>0)
//      return true;
//    return false;
//  }

  public boolean checkRadniciStavke(String cradnik)
  {
    QueryDataSet temp = new QueryDataSet();
    QueryDataSet temp2 = new QueryDataSet();
    String qStr = "select * from odbici where cvrodb in "+
                   "(select cvrodb from vrsteodb where nivoodb like 'RA%' or nivoodb like '%RA') and "+
                   "ckey='"+cradnik+"' or ckey2='"+cradnik+"'";
    String qStr2 = "select * from orgrad where cradnik ='"+cradnik+"'";

    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp2.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr2));
    temp.open();
    temp2.open();
    if((temp.getRowCount()+temp2.getRowCount())>0)
      return true;
    return false;
  }

  public boolean checkZupStavke(short czup)
  {
    QueryDataSet temp = new QueryDataSet();

    String qStr = "select * from opcine where czup = "+czup;

    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkBankeStavke(int cbanke)
  {
    QueryDataSet temp = new QueryDataSet();

    String qStr = "select * from isplmj where cbanke = "+cbanke;

    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

//  public boolean checkOpcineStavke(String copcine)
//  {
//    QueryDataSet temp = new QueryDataSet();
//
//    String qStr = "select * from odbici where cvrodb in "+
//                   "(select cvrodb from vrsteodb where nivoodb like 'OP%' or nivoodb like '%OP') and "+
//                   "ckey='"+copcine+"' or ckey2='"+copcine+"'";
//
//    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    System.out.println("qstr: " + qStr);
//    temp.open();
//    if(temp.getRowCount()>0)
//      return true;
//    return false;
//  }

  public boolean checkOpcineStavke(String copcine)
  {
    String inVrOdb = getNoVrOdb("OP");
//    System.out.println("inVroOdb: " + inVrOdb);
    String q = "select * from odbici where cvrodb in ("+inVrOdb+")";
//    System.out.println("Q: " + q);
    QueryDataSet qds = Util.getNewQueryDataSet(q);
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(qds);
    qds.first();
    while(qds.inBounds())
    {
      ld.raLocate(dm.getVrsteodb(), new String[]{"CVRODB"},new String[] {qds.getShort("CVRODB")+""});
      String nivo = dm.getVrsteodb().getString("NIVOODB");
      int idx = nivo.indexOf("OP");
//      System.out.println("COPCINE: " + copcine);
      if(idx >2)
      {
        if(qds.getString("CKEY2").equals(copcine))
          return true;
      }
      else if(qds.getString("CKEY").equals(copcine))
        return true;


      qds.next();
    }
    return false;
  }

  public boolean checkOrgJedStavke(String corg)
  {
    QueryDataSet temp = new QueryDataSet();

    String qStr = "select * from odbici where cvrodb in "+
                   "(select cvrodb from vrsteodb where nivoodb like 'OJ%' or nivoodb like '%OJ') and "+
                   "ckey='"+corg+"' or ckey2='"+corg+"'";

    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkVrOdnStavke(String cvrodn)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from odbici where cvrodb in "+
                   "(select cvrodb from vrsteodb where nivoodb like 'VR%' or nivoodb like '%VR') and "+
                   "ckey='"+cvrodn+"' or ckey2='"+cvrodn+"'";
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));

    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public boolean checkVrOdbStavke(short cvrodb)
 {
   QueryDataSet temp = new QueryDataSet();
   String qStr = "select * from odbici where cvrodb="+cvrodb;
   temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
   temp.open();
   if(temp.getRowCount()>0)
     return true;
   return false;
 }


  public boolean checkGrIzvStavke(short cizv, short cgrizv)
  {
    QueryDataSet temp = new QueryDataSet();
    String qStr = "select * from grizvprim where cizv ="+cizv+" and cgrizv="+ cgrizv;
    temp.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    temp.open();
    if(temp.getRowCount()>0)
      return true;
    return false;
  }

  public QueryDataSet getSifrarniciDS()
  {
    QueryDataSet qds = new QueryDataSet();
    String qStr = "select * from vrstesif where vrstasif like 'PL%'";
    qds.setColumns(new Column[]{
    (Column) dm.getVrstesif().getColumn("LOKK").clone(),
    (Column) dm.getVrstesif().getColumn("AKTIV").clone(),
    (Column) dm.getVrstesif().getColumn("VRSTASIF").clone(),
    (Column) dm.getVrstesif().getColumn("OPISVRSIF").clone()
    });
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return qds;
  }

  public QueryDataSet getGlobalVrOdbDS(String uvjet)
  {
    QueryDataSet qds = new QueryDataSet();
    String qStr = "select * from vrsteodb where nivoodb like '%"+uvjet+"%'";
    qds.setColumns(new Column[]{
      (Column)dm.getVrsteodb().getColumn("LOKK").clone(),
      (Column)dm.getVrsteodb().getColumn("AKTIV").clone(),
      (Column)dm.getVrsteodb().getColumn("CVRODB").clone(),
      (Column)dm.getVrsteodb().getColumn("OPISVRODB").clone(),
      (Column)dm.getVrsteodb().getColumn("NIVOODB").clone(),
      (Column)dm.getVrsteodb().getColumn("TIPODB").clone(),
      (Column)dm.getVrsteodb().getColumn("VRSTAOSN").clone(),
      (Column)dm.getVrsteodb().getColumn("OSNOVICA").clone(),
      (Column)dm.getVrsteodb().getColumn("CPOV").clone(),
      (Column)dm.getVrsteodb().getColumn("IZNOS").clone(),
      (Column)dm.getVrsteodb().getColumn("STOPA").clone()
    });
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return qds;
  }

  public String refilterOdbici(short cvrodb, String kljuc, String opis)
  {
    ld.raLocate(dm.getVrsteodb(), new String[]{"CVRODB"}, new String[]{cvrodb+""});
    String nivo = dm.getVrsteodb().getString("NIVOODB");
    String qStr="";
    if(nivo.length()>2)
    {
      int idx = nivo.indexOf(opis);
      if(idx>0)
        qStr = "select * from odbici where cvrodb ="+cvrodb+" and  ckey2='"+kljuc+"' order by rbrodb";
      else
        qStr = "select * from odbici where cvrodb ="+cvrodb+" and ckey='"+kljuc+"'  order by rbrodb";
    }
    else if(nivo.length()==2)
      qStr = "select * from odbici where cvrodb ="+cvrodb+" and ckey='"+kljuc+"' order by rbrodb";
//    System.out.println("qStr: " + qStr);
    return qStr;
  }

  public String[] getKeysVrOdb(int rbr, short cvrodb){
    String[] test = new String[2];
    test[0]="001";
    test[1]=1+"";
    return test;
  }

  public String getMasterTitle(String uvjet, String kljuc)
  {
    Hashtable tablice = new Hashtable();
    Hashtable kljucevi = new Hashtable();
    Hashtable naziv = new Hashtable();
    Hashtable dodatak = new Hashtable();
    QueryDataSet qds = new QueryDataSet();


    kljucevi.put("OP", "COPCINE");
    kljucevi.put("RA", "CRADNIK");
    kljucevi.put("OJ", "CORG");
    kljucevi.put("VR", "CVRO");
    kljucevi.put("ZA", "CVRP");
    kljucevi.put("PO", "CORG");
    

    naziv.put("OP","NAZIVOP");
    naziv.put("RA","PREZIME");
    naziv.put("OJ","NAZIV");
    naziv.put("VR","NAZIVRO");
    naziv.put("ZA","NAZIV");

    tablice.put("OP", "OPCINE");
    tablice.put("RA", "RADNICI");
    tablice.put("OJ", "ORGSTRUKTURA");
    tablice.put("VR", "VRODN");
    tablice.put("ZA", "VRSTEPRIM");
    tablice.put("PO", "ORGSTRUKTURA");

    dodatak.put("OP", "za op\u0107inu ");
    dodatak.put("RA", "za radnika ");
    dodatak.put("OJ", "za org. jedinicu ");
    dodatak.put("VR", "za vr. radnog odnosa ");
    dodatak.put("ZA", "za vr. primanja ");
    dodatak.put("PO", "za poduze\u0107e ");

    String qStr = "";
    String quote = uvjet.equals("ZA")?"":"'";
    if(uvjet.equals("RA"))
      qStr = "select ime, prezime from "+tablice.get(uvjet)+
                  " where " + kljucevi.get(uvjet)+" = "+quote+kljuc+quote;
    else
      qStr = "select "+naziv.get(uvjet)+" from "+tablice.get(uvjet)+
                  " where " + kljucevi.get(uvjet)+" = "+quote+kljuc+quote;
    
    System.out.println("plUtil.getMasterTitle :: "+qStr);
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));

    qds.open();
    if(uvjet.equals("RA"))
      return (dodatak.get(uvjet)+ qds.getString("IME")+" "+ qds.getString("PREZIME"));
    return (dodatak.get(uvjet) + qds.getString((String)naziv.get(uvjet)));
  }

  public boolean getOznValEnable(short vrodb)
  {
    String qStr = "select nivoodb as nivoodb, tipodb as tipodb, vrstaosn as vrstaosn, osnovica as osnovica "+
                  "from vrsteodb where cvrodb ="+vrodb;
    QueryDataSet qds = new QueryDataSet();
    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qds.open();
    if(qds.getString("nivoodb").equals("RA") && qds.getString("tipodb").equals("S")
       && qds.getString("vrstaosn").equals("3") && qds.getString("osnovica").equals("0"))
      return true;
    return false;
  }



  // R E P O R T S
  boolean isGrouped(int i, String fld) {
    if ("cvro".equalsIgnoreCase(fld)) {
      if (i == 1 || i == 3) return true;
    }
    if ("corg".equalsIgnoreCase(fld)) {
      if (i == 2 || i == 3) return true;
    }
    return false;
  }
  public String getRekObrStr(int i, String table)
  {
    String qStr = "select "+(isGrouped(i, "corg")?"":"max(")+"corg"+(isGrouped(i, "corg")?"":")")+" as corg, " +
    		          ""+(isGrouped(i, "cvro")?"":"max(")+"cvro"+(isGrouped(i, "cvro")?"":")")+" as cvro, " +
    		          "max(knjig) as knjig, sum(sati) as sati, "+
                  "sum(bruto) as bruto, sum(doprinosi) as doprinosi, sum(neto)as neto, sum(neop) as neop, "+
                  "sum(iskneop) as iskneop, sum(porosn) as porosn, sum(por1) as por1, sum(por2)  as por2, "+
                  "sum(por3) as por3, sum(por4) as por4, sum(por5) as por5, sum(poruk) as poruk, sum(prir) as prir, "+
                  "sum(poriprir) as poriprir, sum(neto2) as neto2, sum(naknade) as naknade, sum(netopk) as netopk, "+
                  "sum(krediti) as krediti, sum(naruke) as naruke, sum(doprpod) as doprpod, sum(0.00) as ol31,  sum(0.00) as ol32, sum(0.00) as ol33 "+
                  "from "+table+" where ";

    return qStr;
  }
  public String getRekObrGroupBy(int i, String cvro )
  {
    String gb = "";
    if(!cvro.equals(""))
      gb=" and cvro='"+cvro+"'";
    switch (i) {
      case 1:
        gb+=" group by cvro ";
        break;
      case 2:
        gb+=" group by corg ";
        break;
      case 3:
        gb+=" group by corg, cvro ";
        break;
    }
    return gb;
  }
  public String getSpecKredStr(String table, String where)
  {
    String sIsp =  "select max("+table+".cradnik) as cradnik,max(radnici.corg) as corg, " +
                " max("+table+".cvrodb) as cvrodb, sum("+table+".obriznos) as obriznos, "+
                "sum("+table+".saldo) as saldo, max("+table+".rbrodb) as rbrodb, " +
                		"max(radnici.prezime) as prezime, max(radnici.ime) as ime"+
                " from "+table+", radnici where "+table+"."+where+
                  " and radnici.cradnik= "+table+".cradnik and ";//radnici.";

    return sIsp;
  }

  public int getBrDjelatnikaPL(String isplmj)
  {
    String isplAdd = "";
    QueryDataSet tempQds=new QueryDataSet();

    if(!isplmj.equals(""))
      isplAdd = "where isplmj = "+isplmj;
    String qStr = "select * from radnicipl "+isplAdd;
    tempQds.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));
    tempQds.open();
    return tempQds.getRowCount();
  }

  public short getStandOdbRBR(short vrOd, String nO)
  {
    String qStr;
    if(nO.length()>2)
      qStr = "select max(rbrodb) as rbrodb from odbici where cvrodb="+vrOd + " and ckey='$DEF' and ckey2='$DEF'";
    else
      qStr = "select max(rbrodb) as rbrodb from odbici where cvrodb="+vrOd+ " and ckey='$DEF'";
    QueryDataSet qds = Util.getNewQueryDataSet(qStr, true);
    qds.first();
    if(qds.getRowCount()>0)
      return (short)(qds.getShort("rbrodb")+1);
    return 1;
  }


//*****************************
//***** Standardni odbici *****
//*****************************
  public void deleteStandOdb(String no, String key)
  {
    String inVrOdb = getNoVrOdb(no);
    String qStr="";
    VarStr v = new VarStr(inVrOdb);
    String[] cVrodb = v.split(',');
    for(int i = 0; i<cVrodb.length;i++)
    {
      ld.raLocate(dm.getVrsteodb(), new String[]{"CVRODB"}, new String[]{cVrodb[i]});
      int idx = dm.getVrsteodb().getString("NIVOODB").indexOf(no);
      if(idx==0)
        qStr= "delete from odbici where cvrodb="+cVrodb[i]+" and ckey='"+key+"'";
      else
        qStr= "delete from odbici where cvrodb="+cVrodb[i]+" and ckey2='"+key+"'";
      try {
        vl.runSQL(qStr);
        dm.getOdbici().refresh();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void addStandOdbici(String no, String key)
  {
    String inVrOdb = getNoVrOdb(no);
    if(inVrOdb.equals("")) return;
    HashMap nivOd = getMapVrOdb(no);
    QueryDataSet qdsInit = Odbici.getDataModule().getFilteredDataSet("CVRODB IN ("+inVrOdb + ") and (ckey='$DEF' or ckey2='$DEF')");
    QueryDataSet testDS =  Odbici.getDataModule().getFilteredDataSet("1=2");
    qdsInit.open();
    qdsInit.first();
    if(!dm.getOdbici().isOpen())
      dm.getOdbici().open();
    while(qdsInit.inBounds())
    {
      if(qdsInit.getString("CKEY2").equals(""))
      {
        dm.getOdbici().insertRow(false);
        qdsInit.copyTo(dm.getOdbici());
        dm.getOdbici().setString("CKEY", key);
      }
      else
      {
        String cVrOd= qdsInit.getShort("CVRODB")+"";
        String drugiDioNivoa = nivOd.get(cVrOd).toString();
        ld.raLocate(dm.getVrsteodb(), new String[]{"CVRODB"}, new String[]{cVrOd});
        int idx  = dm.getVrsteodb().getString("NIVOODB").indexOf(drugiDioNivoa);
        ArrayList al = getNOArrList(drugiDioNivoa);
        for(int i = 0; i<al.size();i++)
        {
          dm.getOdbici().insertRow(false);
          qdsInit.copyTo(dm.getOdbici());
          if(idx >0)
          {
            dm.getOdbici().setString("CKEY", key);
            dm.getOdbici().setString("CKEY2", al.get(i)+"");
          }
          else
          {
            dm.getOdbici().setString("CKEY2", key);
            dm.getOdbici().setString("CKEY", al.get(i)+"");
          }
        }
      }
      qdsInit.next();
    }
    dm.getOdbici().saveChanges();
  }

  public String getNoVrOdb(String no)
  {
    QueryDataSet qdsAL = Util.getNewQueryDataSet("select * from vrsteodb", true);
    qdsAL.first();
    String nivoOdb="";
    String vrOdb = "";

    while(qdsAL.inBounds())
    {
      nivoOdb = qdsAL.getString("NIVOODB");
      if (nivoOdb.length()>2)
      {
        if(nivoOdb.substring(0,2).equals(no))
        {
          vrOdb = vrOdb + qdsAL.getShort("CVRODB")+",";
        }
        else if(nivoOdb.substring(2,4).equals(no))
        {
          vrOdb = vrOdb +  qdsAL.getShort("CVRODB")+",";
        }
      }
      else
      {
         if(nivoOdb.equals(no))
         {
           vrOdb = vrOdb + qdsAL.getShort("CVRODB")+",";
         }
      }
      qdsAL.next();
    }
    if(vrOdb.lastIndexOf(",")!=-1)
      return vrOdb.substring(0, vrOdb.lastIndexOf(","));
    else
      return vrOdb;
  }


  public HashMap getMapVrOdb(String no)
  {
    QueryDataSet qdsAL = Util.getNewQueryDataSet("select * from vrsteodb", true);
    qdsAL.first();
    String nivoOdb="";
    HashMap keyMap = new HashMap();
    while(qdsAL.inBounds())
    {
      nivoOdb = qdsAL.getString("NIVOODB");
      if (nivoOdb.length()>2)
      {
        if(nivoOdb.substring(0,2).equals(no))
        {
          keyMap.put(qdsAL.getShort("CVRODB")+"",qdsAL.getString("NIVOODB").substring(2,4));
        }
        else if(nivoOdb.substring(2,4).equals(no))
        {
          keyMap.put(qdsAL.getShort("CVRODB")+"",qdsAL.getString("NIVOODB").substring(0,2));
        }
      }
      qdsAL.next();
    }
    return keyMap;
  }

// selektiranje vrijednosti za drugi dio nivoa odbitka
  public ArrayList getNOArrList(String pk)
  {
    ArrayList vrati = new ArrayList();
    String kljuc="";
    String tablica="";
    String test = "";

    if(pk.equals("RA"))
    {
      kljuc = "CRADNIK";
      tablica = "RADNICIPL";
    }

    if(pk.equals("OP"))
    {
      kljuc = "COPCINE";
      tablica = "OPCINE";
    }

    if(pk.equals("OJ"))
    {
      kljuc = "CORG";
      tablica = "ORGPL";
    }

    if(pk.equals("VR"))
    {
      kljuc = "CVRO";
      tablica = "VRODN";
    }

    if(pk.equals("ZA"))
    {
      kljuc = "CVRP";
      tablica = "VRSTEPRIM";
    }

    if(pk.equals("PO"))
    {
      kljuc = "CORG";
      tablica = "ORGPL where corg='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'";
    }

    QueryDataSet temp = Util.getNewQueryDataSet("select " + kljuc + " from " + tablica+ " order by "+kljuc, true);
    while(temp.inBounds())
    {
      if(pk.equals("ZA"))
      {
        vrati.add(temp.getShort(kljuc)+"");
      }
      else
      {
        vrati.add(temp.getString(kljuc));
      }
      temp.next();
    }
    return vrati;
  }

  public String getRadCurKnjig()
  {
    String in = "CORG IN('";
    StorageDataSet sds = OrgStr.getOrgStr().getOrgstrAndKnjig(OrgStr.getOrgStr().getKNJCORG());
    sds.open();
    sds.first();

    int i = 0;
    while(sds.inBounds())
    {
      if(i<sds.getRowCount()-1)
        in = in+sds.getString("CORG")+"','";
      else
        in = in+sds.getString("CORG")+"')";
      i++;
      sds.next();
    }
//    System.out.println("in: " + in);
    return in;


  }
}