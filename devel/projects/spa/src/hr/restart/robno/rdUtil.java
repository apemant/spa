/****license*****************************************************************
**   file: rdUtil.java
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

import hr.restart.baza.Condition;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class rdUtil {
  static rdUtil myUtil;

  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

  public rdUtil() {}

  public static rdUtil getUtil() {
    if (myUtil == null)
      myUtil = new rdUtil();
    return myUtil;
  }

  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //------- M E T O D E Z A P R I K A Z D A T A S E T K O M P O N E N T I
  // ------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  //  public void printDSColumnNames(com.borland.dx.sql.dataset.QueryDataSet qds)
  // {
  //    qds.open();
  //    String[] kolone = qds.getColumnNames(qds.columnCount());
  //
  //    String type;
  //    qds.first();
  //  // System.out.println("Ukupno redova: " + qds.getRowCount());
  //    if(qds.getRowCount()!=0) {
  //      for(int j=0; j<qds.getRowCount();j++) {
  //  // System.out.println("Red br.: " + (j+1));
  //  // System.out.println("--------------------------------------------");
  //        for(int i=0; i<kolone.length;i++) {
  //          if(qds.getColumn(i).getDataType()==3)
  //            type="Short";
  //          else if(qds.getColumn(i).getDataType()==4)
  //            type="Int";
  //          else if(qds.getColumn(i).getDataType()==7)
  //            type="Double";
  //          else if(qds.getColumn(i).getDataType()==10)
  //            type="BigDecimal";
  //          else if(qds.getColumn(i).getDataType()==16)
  //            type="String";
  //          else
  //            type="TimeStamp";
  //
  //          if(qds.getColumn(i).getDataType()==3)
  //            System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getShort(i));
  //          else if(qds.getColumn(i).getDataType()==4)
  //            System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getInt(i));
  //          else if(qds.getColumn(i).getDataType()==7)
  //            System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getDouble(i));
  //          else if(qds.getColumn(i).getDataType()==10)
  //            System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getBigDecimal(i).toString());
  //          else if(qds.getColumn(i).getDataType()==16)
  //            System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getString(i));
  //          else
  //            System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getTimestamp(i).toString());
  //        }
  //        qds.next();
  //      }
  //    } else {
  //      for(int i=0; i<kolone.length;i++) {
  //
  //        if(qds.getColumn(i).getDataType()==3)
  //          type="Short";
  //        else if(qds.getColumn(i).getDataType()==4)
  //          type="Int";
  //        else if(qds.getColumn(i).getDataType()==7)
  //          type="Double";
  //        else if(qds.getColumn(i).getDataType()==10)
  //          type="BigDecimal";
  //        else if(qds.getColumn(i).getDataType()==16)
  //          type="String";
  //        else
  //          type="TimeStamp";
  //
  //        if(qds.getColumn(i).getDataType()==3)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type );
  //        else if(qds.getColumn(i).getDataType()==4)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type );
  //        else if(qds.getColumn(i).getDataType()==7)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type );
  //        else if(qds.getColumn(i).getDataType()==10)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type);
  //        else if(qds.getColumn(i).getDataType()==16)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type);
  //        else
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type);
  //      }
  //    }
  //  }

  //  public void printDSColumnNames(com.borland.dx.dataset.DataSet qds) {
  //    qds.open();
  //    String[] kolone = qds.getColumnNames(qds.columnCount());
  //    String type;
  //    qds.first();
  //  // System.out.println("Ukupno redova: " + qds.getRowCount());
  //    for(int j=0; j<qds.getRowCount();j++) {
  //
  //  // System.out.println("Red br.: " + (j+1));
  //  // System.out.println("--------------------------------------------");
  //      for(int i=0; i<kolone.length;i++) {
  //        if(qds.getColumn(i).getDataType()==3)
  //          type="Short";
  //        else if(qds.getColumn(i).getDataType()==4)
  //          type="Int";
  //        else if(qds.getColumn(i).getDataType()==7)
  //          type="Double";
  //        else if(qds.getColumn(i).getDataType()==10)
  //          type="BigDecimal";
  //        else if(qds.getColumn(i).getDataType()==16)
  //          type="String";
  //        else
  //          type="TimeStamp";
  //
  //        if(qds.getColumn(i).getDataType()==3)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getShort(i));
  //        else if(qds.getColumn(i).getDataType()==4)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getInt(i));
  //        else if(qds.getColumn(i).getDataType()==7)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getDouble(i));
  //        else if(qds.getColumn(i).getDataType()==10)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getBigDecimal(i).toString());
  //        else if(qds.getColumn(i).getDataType()==16)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getString(i));
  //        else
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getTimestamp(i).toString());
  //      }
  //      qds.next();
  //    }
  //  }

  //  public void printDSColumnNames(com.borland.dx.dataset.StorageDataSet qds) {
  //    qds.open();
  //    String[] kolone = qds.getColumnNames(qds.columnCount());
  //    String type;
  //    qds.first();
  //  // System.out.println("Ukupno redova: " + qds.getRowCount());
  //    for(int j=0; j<qds.getRowCount();j++) {
  //  // System.out.println("Red br.: " + (j+1));
  //  // System.out.println("--------------------------------------------");
  //      for(int i=0; i<kolone.length;i++) {
  //        if(qds.getColumn(i).getDataType()==3)
  //          type="Short";
  //        else if(qds.getColumn(i).getDataType()==4)
  //          type="Int";
  //        else if(qds.getColumn(i).getDataType()==7)
  //          type="Double";
  //        else if(qds.getColumn(i).getDataType()==10)
  //          type="BigDecimal";
  //        else if(qds.getColumn(i).getDataType()==16)
  //          type="String";
  //        else
  //          type="TimeStamp";
  //  // System.out.println("DATATYPE " + qds.getColumn(i).getDataType());
  //        if(qds.getColumn(i).getDataType()==3)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getShort(i));
  //        else if(qds.getColumn(i).getDataType()==4)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getInt(i));
  //        else if(qds.getColumn(i).getDataType()==7)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getDouble(i));
  //        else if(qds.getColumn(i).getDataType()==10)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getBigDecimal(i).toString());
  //        else if(qds.getColumn(i).getDataType()==16)
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getString(i));
  //        else
  //          System.out.println("Kolona["+i+"]: " + kolone[i] + " Type: "+ type + " ->
  // value: " + qds.getTimestamp(i).toString());
  //      }
  //      qds.next();
  //    }
  //  }

  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //---------- D O D A T N I P O P U S T i Z A V I S N I T R O Š A K
  // -----------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  //-> izlazni dokumenti
  //  public String getDodPopust(char mode) {
  //    String dodatak="";
  //    if (mode=='N')
  //      dodatak = " and rbr=0";
  //
  //    String qStr="select VTRABAT.CRAB as CRAB, VTRABAT.RBR as RBR, VTRABAT.CSKL
  // as CSKL, VTRABAT.PRAB as PRAB, VTRABAT.IRAB as IRAB, "+
  //                "VTRABAT.AKTIV as AKTIV, VTRABAT.LRBR as LRBR, VTRABAT.GOD as GOD,
  // VTRABAT.LOKK as LOKK,VTRABAT.VRDOK as VRDOK, VTRABAT.BRDOK as BRDOK , "+
  //                "VTRABAT.RABNARAB as RABNARAB from vtrabat, doki"+
  //                " where vtrabat.god=doki.god and vtrabat.brdok=doki.brdok and
  // vtrabat.vrdok=doki.vrdok and vtrabat.cskl=doki.cskl"+ dodatak;
  //    return qStr;
  //  }

  //  public String getDodZT(char mode) {
  //    String dodatak="";
  //    if (mode=='N')
  //      dodatak = " and rbr=0";
  //
  //    String qStr="select VTZAVTR.CZT as CZT, VTZAVTR.RBR as RBR, VTZAVTR.CSKL as
  // CSKL, VTZAVTR.PZT as PZT, VTZAVTR.IZT as IZT, "+
  //                " VTZAVTR.GOD as GOD, VTZAVTR.VRDOK as VRDOK, VTZAVTR.BRDOK as BRDOK , "+
  //                "VTZAVTR.ZTNAZT as ZTNAZT, VTZAVTR.BROJKONTA AS BK, VTZAVTR.LOKK AS LOKK,
  // VTZAVTR.AKTIV AS AKTIV, VTZAVTR.LRBR AS LRBR "+
  //                "from vtzavtr, doki where vtzavtr.god=doki.god and vtzavtr.brdok=doki.brdok
  // and vtzavtr.vrdok=doki.vrdok and vtzavtr.cskl=doki.cskl"
  //               +dodatak;
  //    return qStr;
  //  }

  // ulazni dokimenti
  //  public String updateZavtrU(DataRow qdr) {
  //    String qStr = "insert into vtzavtr
  // values('"+qdr.getString("LOKK")+"','"+qdr.getString("AKTIV")+"','"+qdr.getString("CSKL")+"','"+
  //                  qdr.getString("VRDOK")+"','"+qdr.getString("GOD")+"',"+qdr.getInt("BRDOK")+","+qdr.getShort("RBR")+","+
  //                  qdr.getShort("LRBR")+",'"+qdr.getString("CZT")+"',"+qdr.getBigDecimal("PZT")+","+qdr.getBigDecimal("IZT")+","+
  //                  qdr.getBigDecimal("UIPRPOR")+",'"+qdr.getString("ZTNAZT")+"','"+qdr.getString("BROJKONTA")+"')";
  //
  //    return qStr;
  //  }

  //  public String getDodZTU(short rbr, int brdok, QueryDataSet ds) {
  //    String dodatak="";
  //    dodatak = " and rbr=" + rbr;
  //
  //    String qStr="select VTZAVTR.LOKK AS LOKK, VTZAVTR.AKTIV AS AKTIV,
  // VTZAVTR.CSKL as CSKL, VTZAVTR.VRDOK as VRDOK, "+
  //                " VTZAVTR.GOD as GOD, VTZAVTR.BRDOK as BRDOK , VTZAVTR.RBR as RBR, "+
  //                "VTZAVTR.LRBR AS LRBR, VTZAVTR.CZT as CZT, VTZAVTR.PZT as PZT, VTZAVTR.IZT
  // as IZT,VTZAVTR.ZTNAZT as ZTNAZT, VTZAVTR.BROJKONTA AS BROJKONTA, "+
  //                "VTZAVTR.UIPRPOR AS UIPRPOR from vtzavtr where
  // vtzavtr.god='"+ds.getString("GOD")+"' and vtzavtr.brdok="+brdok+" and
  // vtzavtr.vrdok='"+ds.getString("VRDOK")+
  //                "' and vtzavtr.cskl='"+ds.getString("CSKL")+"'"+dodatak;
  //    return qStr;
  //  }

  //  public String deleteExistingZavtrU(QueryDataSet qds) {
  //    String qStr = "delete from vtzavtr where cskl='"+qds.getString("CSKL")+"'
  // and vrdok='"+qds.getString("VRDOK")+
  //                  "' and god='"+qds.getString("GOD")+"' and brdok="+qds.getInt("BRDOK")+" and
  // rbr ="+qds.getShort("RBR");
  //
  //    return qStr;
  //  }

  //  public String deleteExistingZavtrU(QueryDataSet qds, short rbr) {
  //    String qStr = "delete from vtzavtr where cskl='"+qds.getString("CSKL")+"'
  // and vrdok='"+qds.getString("VRDOK")+
  //                  "' and god='"+qds.getString("GOD")+"' and brdok="+qds.getInt("BRDOK")+" and
  // rbr ="+rbr;
  //    return qStr;
  //  }

  //  public String maxRBR(QueryDataSet qds) {
  //    String maxRBR = "select max(rbr) as MAXRBR from vtzavtr where
  // cskl='"+qds.getString("CSKL")+"' and vrdok='"+qds.getString("VRDOK")+
  //                    "' and god='"+qds.getString("GOD")+"' and brdok="+qds.getInt("BRDOK");
  //    return maxRBR;
  //  }

  //  public String recalcRBR(QueryDataSet ds, short rbr) {
  //    String qStr = "update vtzavtr set rbr = rbr-1 where
  // cskl='"+ds.getString("CSKL")+"' and vrdok ='"+ds.getString("VRDOK")+
  //                  "' and god ='"+ds.getString("GOD")+"' and brdok ="+ds.getInt("BRDOK")+
  //                  " and rbr >" + rbr;
  //    return qStr;
  //  }

  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //--------------------- M A X I M A L N I A R T I K L
  // ---------------------------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  public int getMaxArtikl() {
    hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
    int maxCart = 1;
    String qStr = "select MAX(CART) AS CART from ARTIKLI";

    vl.execSQL(qStr);
    vl.RezSet.open();
    maxCart = vl.RezSet.getInt("CART") + 1;
    vl.RezSet.close();
    return maxCart;
  }

  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //--------------------- S E R I J S K I B R O J E V I
  // ---------------------------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  //-> Ulazni dokumenti
  public QueryDataSet getSbUlDataSet() {
    QueryDataSet ds = new QueryDataSet();
    String qStr = "select SERBR.RBR AS RBR, SERBR.CSERBR AS SB, SERBR.CSKL AS CSKL, SERBR.VRDOK AS VRDOK, SERBR.BRDOK AS BRDOK" + ", SERBR.GOD AS GOD from SERBR where SERBR.VRDOKIZ IS NULL";
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    ds.open();
    return ds;
  }

  public QueryDataSet getSbUlR(int brdok) {
    QueryDataSet ds = new QueryDataSet();

    String qStr = "select SERBR.RBR AS RBR, SERBR.CSERBR AS SB, SERBR.CSKL AS CSKL, SERBR.VRDOK AS VRDOK, SERBR.BRDOK AS BRDOK" + ", SERBR.GOD AS GOD from SERBR where SERBR.VRDOKIZ IS NULL" + " and SERBR.BRDOK = " + brdok;
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    ds.open();

    return ds;
  }

  public QueryDataSet getSbUlL(int brdok) {
    QueryDataSet ds = new QueryDataSet();
    String qStr = "select SERBR.RBR AS RBR, SERBR.CSERBR AS SB, SERBR.CSKL AS CSKL, SERBR.VRDOK AS VRDOK, SERBR.BRDOK AS BRDOK" + ", SERBR.GOD AS GOD from SERBR where SERBR.VRDOKIZ IS NULL" + " and SERBR.BRDOK NOT IN (" + brdok + ")";
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    ds.open();
    return ds;
  }

  //-> Izlazni dokumenti
  public QueryDataSet getSbIzDataSet(String tempCskl, int cart) {
    QueryDataSet ds = new QueryDataSet();

    String qStr = "select SERBR.RBR AS RBR, SERBR.CSERBR AS SB, SERBR.CSKL AS CSKL, SERBR.VRDOK AS VRDOK, SERBR.BRDOK AS BRDOK" + ", SERBR.GOD AS GOD from SERBR where SERBR.VRDOKIZ IS NULL and SERBR.CSKL='" + tempCskl + "' and SERBR.CART=" + cart;
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    ds.open();
    return ds;
  }

  public QueryDataSet getSbIzR(int brdok, String tempCskl, int rbr) {
    QueryDataSet ds = new QueryDataSet();

    String qStr = "select SERBR.RBR AS RBR, SERBR.CSERBR AS SB, SERBR.CSKLIZ AS CSKL, " + "SERBR.VRDOKIZ AS VRDOK, SERBR.BRDOKIZ AS BRDOK" + ", SERBR.GODIZ AS GOD from SERBR where SERBR.VRDOKIZ IS NOT NULL" + " and SERBR.BRDOKIZ = " + brdok + " and SERBR.CSKL='" + tempCskl + "' and SERBR.RBR=" + rbr;
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    ds.open();
    return ds;
  }

  public QueryDataSet getSbIzL(int brdok, String tempCskl, int rbr) {
    QueryDataSet ds = new QueryDataSet();
    String qStr = "select SERBR.RBR AS RBR, SERBR.CSERBR AS SB, SERBR.CSKL AS CSKL, " + "SERBR.VRDOK AS VRDOK, SERBR.BRDOK AS BRDOK" + ", SERBR.GOD AS GOD from SERBR where SERBR.VRDOKIZ IS NULL and SERBR.CSKL='" + tempCskl + "' and SERBR.RBR=" + rbr;
    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    ds.open();

    return ds;
  }

  //  public QueryDataSet getMESR(int brdok, String tempCskl) {
  //    QueryDataSet ds = new QueryDataSet();
  //    String qStr = "select SERBR.RBR AS RBR, SERBR.CSERBR AS SB, SERBR.CSKLIZ AS
  // CSKL, "+
  //                  "SERBR.VRDOKIZ AS VRDOK, SERBR.BRDOKIZ AS BRDOK"+
  //                  ", SERBR.GODIZ AS GOD from SERBR where SERBR.VRDOKIZ IS NOT NULL"+
  //                  " and SERBR.BRDOKIZ = " + brdok;
  //    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
  //    ds.open();
  //    return ds;
  //  }

  //  public QueryDataSet getMESL(int brdok, String tempCskl) {
  //    QueryDataSet ds = new QueryDataSet();
  //
  //    String qStr = "select SERBR.RBR AS RBR, SERBR.CSERBR AS SB, SERBR.CSKL AS
  // CSKL, "+
  //                  "SERBR.VRDOK AS VRDOK, SERBR.BRDOK AS BRDOK"+
  //                  ", SERBR.GOD AS GOD from SERBR where SERBR.VRDOKIZ IS NULL";
  //    ds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
  //    ds.open();
  //
  //    return ds;
  //  }

  //-> snimanje
  public String ulazIspravak(int delBrdok, String delVrdok, String delGod, String delCskl) {
    String ulazIspravak = "delete from SERBR where brdok =" + delBrdok + " and vrdok ='" + delVrdok + "' and god ='" + delGod + "' and cskl='" + delCskl + "'";
    return ulazIspravak;
  }

  public String izlazIspravak(int delBrdok, String delVrdok, String delGod, String delCskl) {
    String izlazIspravak = "update SERBR set brdokiz = NULL, vrdokiz=NULL, godiz=NULL, cskliz=NULL" + " where brdokiz =" + delBrdok + " and vrdokiz ='" + delVrdok + "' and godiz ='" + delGod + "' and cskliz='" + delCskl + "'";
    return izlazIspravak;
  }

  public String izlazIspravak(int delBrdok, String delVrdok, String delGod, String delCskl, String sb) {
    String izlazIspravak = "update SERBR set brdokiz = " + delBrdok + ", vrdokiz='" + delVrdok + "', godiz='" + delGod + "', cskliz='" + delCskl + "' where csklul='" + delCskl + "' and cserbr='" + sb + "'";
    return izlazIspravak;
  }

  public String mesInsert(int MCart, String sb, int rb, String delMCsklul, String delMCskliz, String delGod, int delBrdok) {
    String mesInsert = "insert into SERBR values('N', 'D'," + MCart + ", '" + sb + "'," + (rb + 1) + ", '" + delMCsklul + "', '" + delMCskliz + "', 'MES', '" + delGod + "', " + delBrdok + ", '', '', '', '', NULL)";
    return mesInsert;

  }

  public String mesDelete(String delMCsklul, String delMCskliz) {
    String mesDel = "delete from SERBR where cskl='" + delMCsklul + "' and vrdokul='MES'";
    return mesDel;
  }

  public String mesUpdate(int delBrdok, String delGod, String delMCskliz, String delMCsklul, String sb) {
    String mesUpdate = "update SERBR set brdokiz = " + delBrdok + ", vrdokiz='MES', godiz='" + delGod + "', cskliz='" + delMCskliz + "', cskliz2='" + delMCsklul + "' where csklul='" + delMCskliz + "' and cserbr='" + sb + "' and rbr=1";
    return mesUpdate;
  }

  public String mesUpdate2(int delBrdok, String delGod, String delMCskliz, String delMCsklul, String delVrdok) {
    String mesUpdate = "update SERBR set brdokiz = NULL, vrdokiz=NULL, godiz=NULL, cskliz=NULL, cskliz2=NULL" + " where brdokiz =" + delBrdok + " and vrdokiz ='" + delVrdok + "' and godiz ='" + delGod + "' and cskliz='" + delMCskliz + "' and cskliz2='" + delMCsklul + "'";
    return mesUpdate;
  }

  //               <=--------------------------------eeee----------------------------------<<<<<

  //-> brisanje
  public String deleteSerBr(char mode, DataRow old) {
    String delete = "";

    if (mode == 'U') {
      delete = "delete from SERBR where brdok =" + old.getInt("BRDOK") + " and vrdok ='" + old.getString("VRDOK") + "' and god ='" + old.getString("GOD") + "' and cskl='" + old.getString("CSKL") + "' and rbr=" + old.getShort("RBR") + " and cart =" + old.getInt("CART");
    } else if (mode == 'I') {
      delete = "update SERBR set brdokiz = NULL, vrdokiz=NULL, godiz=NULL, cskliz=NULL, rbriz=NULL" + " where brdokiz =" + old.getInt("BRDOK") + " and vrdokiz ='" + old.getString("VRDOK") + "' and godiz ='" + old.getString("GOD") + "' and cskliz='" + old.getString("CSKL") + "' and rbr=" + old.getShort("RBR") + " and cart=" + old.getInt("CART");
    } else if (mode == 'M') {
      delete = "delete from SERBR where brdokul =" + old.getInt("BRDOK") + " and vrdokul ='" + old.getString("VRDOK") + "' and godul ='" + old.getString("GOD") + "' and csklul='" + old.getString("CSKLUL") + "' and cart =" + old.getInt("CART") + " and rbr=" + (old.getShort("RBR") + 1);
    }
    return delete;
  }

  public String updateBeforeDelete(DataRow old) {
    String update = "update SERBR set brdokiz = NULL, vrdokiz=NULL, godiz=NULL, cskliz=NULL," + "rbriz=NULL " + " where vrdokiz ='" + old.getString("VRDOK") + "' and godiz ='" + old.getString("GOD") + "' and cskliz='" + old.getString("CSKLIZ") + "' and cart=" + old.getInt("CART") + " and rbriz=" + old.getShort("RBR");
    return update;
  }

  public String checkBeforeDelete(QueryDataSet qds) {
    String qStr = "select * from serbr where cskl='" + qds.getString("CSKL") + "' and cart=" + qds.getInt("CART") + " and god='" + qds.getString("GOD") + "' and brdok=" + qds.getInt("BRDOK") + " and rbr=" + qds.getShort("RBR") + " and vrdokiz is not null";
    return qStr;
  }

  public String pregledSB(String cart, String cskl) {
    String qStr = "select ARTIKLI.NAZART, SERBR.CART, SERBR.CSERBR, SERBR.RBR, SERBR.CSKLIZ, SERBR.VRDOK, SERBR.BRDOKIZ, SERBR.GOD" + ", SKLAD.NAZSKL from SERBR, SKLAD, ARTIKLI WHERE SERBR.VRDOKIZ IS null and SERBR.CART = " + cart + " and SERBR.CSKLIZ = '" + cskl + "' and SKLAD.CSKL = SERBR.CSKLIZ and ARTIKLI.CART =" + cart;
    return qStr;
  }

  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //--------------------- S T A N J E S K L A D I S T A
  // ---------------------------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//
  /** @todo jebo ovo :((( */
  /*
   * public String defaultStanjeNaDan(String cSkl, String cArt, Timestamp ts) {
   * String year = ts.toString().substring(0, 4); String carting = ""; if
   * (!cArt.equals("")){ carting = "AND "+cArt; } String qStr = "SELECT
   * ARTIKLI.CART, ARTIKLI.CART1, ARTIKLI.BC, ARTIKLI.NAZART, 0.00 AS KOL, 0.00
   * AS ZC, 0.00 AS POR , 0.00 AS NAB, 0.00 AS MAR, 0.00 AS VRI from ARTIKLI,
   * STANJE where ARTIKLI.CART=STANJE.CART AND STANJE.GOD ='"+ year+"' AND
   * STANJE.CSKL = '"+ cSkl+"' "+carting+ " ORDER BY 1"; //
   * System.out.println(">> stanje : TRENUTNO STANJE 1 - " + qStr); return qStr; }
   */

  public String defaultStanje(String cSkl, String cArt, String god, String sljed) {
    String qStr = "";
    String carting = "";
    String descending = "";

    if (sljed.equals("KOL"))
      descending = " descending";
    else if (sljed.equals("NAZART"))
      descending = " " + vl.getCollateSQL();

    if (!cArt.equals("")) {
      carting = "AND " + cArt;
    }
    
    String cskling = "";
    
    if (cSkl.equals("")){
      DataSet sfc = hr.restart.robno.Util.getUtil().getSkladFromCorg();
      sfc.open();
      sfc.first();
      String eskling = "";
      do {
        eskling += "'"+sfc.getString("CSKL")+"'";
        if (sfc.next()) eskling += ", ";
        else {
         break; 
        }
      } while (true);
      
      cskling = "' AND CSKL in (" + eskling + ") ";
    } else cskling = "' AND CSKL = '" + cSkl + "' ";
    
    System.out.println("CSKLING - "+cskling);
    qStr = "SELECT stanje.cskl, ARTIKLI.CART, ARTIKLI.CART1, ARTIKLI.BC, ARTIKLI.NAZART, ARTIKLI.JM, STANJE.KOL, STANJE.ZC, STANJE.NC, STANJE.VC, STANJE.MC, (STANJE.NABUL-STANJE.NABIZ+STANJE.NABPS) AS NAB," + "(STANJE.MARUL-STANJE.MARIZ+STANJE.MARPS) AS MAR, (STANJE.PORUL-STANJE.PORIZ+STANJE.PORPS) AS POR, STANJE.VRI, STANJE.KOLREZ " + "from STANJE,ARTIKLI where STANJE.CART=ARTIKLI.CART AND STANJE.GOD = '" + god + cskling + carting + " ORDER BY cskl, " + sljed + descending;
        System.out.println(">> stanje : TRENUTNO STANJE - " + qStr);
    return qStr;
  }
  public String defaultSkladStanje(String cSkl, String cArt, String god, String sljed) {
    String qStr = "";
    String carting = "";
    String descending = "";

    if (sljed.equals("KOL"))
      descending = " descending";
    else if (sljed.equals("NAZART"))
      descending = " " + vl.getCollateSQL();

    if (!cArt.equals("")) {
      carting = "AND " + cArt;
    }
    
    String cskling = "";
    
    if (cSkl.equals("")){
      DataSet sfc = hr.restart.robno.Util.getUtil().getSkladFromCorg();
      sfc.open();
      sfc.first();
      String eskling = "";
      do {
        eskling += "'"+sfc.getString("CSKL")+"'";
        if (sfc.next()) eskling += ", ";
        else {
         break; 
        }
      } while (true);
      
      cskling = "' AND CSKL in (" + eskling + ") ";
    } else cskling = "' AND CSKL = '" + cSkl + "' ";
    
    System.out.println("CSKLING - "+cskling);
    qStr = "SELECT stanje.cskl, ARTIKLI.CART, ARTIKLI.CART1, ARTIKLI.BC, ARTIKLI.NAZART, ARTIKLI.JM, STANJE.KOLSKLAD AS KOL from STANJE,ARTIKLI where STANJE.CART=ARTIKLI.CART AND STANJE.GOD = '" + god + cskling + carting + " ORDER BY cskl, " + sljed + descending;
    //    System.out.println(">> stanje : TRENUTNO STANJE - " + qStr);
    return qStr;
  }

  //*** Query string za stanje skladista
  public String getSkladSqlPoj(String vrzal, String cSkl, String cArt, String dateP, String sljed) {
    String year = dateP.substring(1, 5);
    String qStr;
    String carting = "";
    String descending = "";
    String ordering = "";
    if (sljed.equals("KOL")) {
      ordering = "7";
      descending = " descending";
    } else if (sljed.equals("NAZART")) {
      ordering = "6";
      descending = " " + vl.getCollateSQL();
    } else {
      ordering = Aut.getAut().getCARTdependable("3", "4", "5");
    }

    if (!cArt.equals("")) {
      carting = "AND " + cArt;
    }
    
    String cskl;
    
    if (!cSkl.equals("")) cskl = " = '"+cSkl+"' ";
    else {
      QueryDataSet skls = util.getSkladFromCorg();
      cskl = " in (";
      skls.first();
      for (;;) {
        cskl += "'"+skls.getString("CSKL")+"'";
        if (skls.next()) cskl += ",";
        else {
          cskl += ") ";
          break;
        }
      }
    }

    // Ulazi
    qStr = "SELECT  STDOKU.BRDOK, STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NC, STDOKU.VC, STDOKU.MC, STDOKU.NAZART, STDOKU.JM, STDOKU.KOL, STDOKU.ZC, STDOKU.IZAD AS VRI, " + "STDOKU.IPOR AS POR, STDOKU.IMAR AS MAR, STDOKU.INAB AS NAB " + "from STDOKU, DOKU, ARTIKLI " + "where DOKU.GOD='" + year + "' AND STDOKU.CSKL" + cskl + " " + carting + " AND " + util.getDoc("DOKU", "STDOKU") + " AND stdoku.cart = artikli.cart " + " AND DOKU.DATDOK <= " + dateP + " " + "UNION ALL " +
    // Izlazi
        "SELECT  STDOKI.BRDOK, STDOKI.VRDOK, STDOKI.CART, STDOKI.CART1, STDOKI.BC, STDOKI.NC, STDOKI.VC, STDOKI.MC, STDOKI.NAZART, STDOKI.JM, -STDOKI.KOL, STDOKI.ZC, -STDOKI.IRAZ AS VRI, " + "STDOKI.IPOR AS POR, STDOKI.IMAR AS MAR, STDOKI.INAB AS NAB " + "from STDOKI, DOKI, ARTIKLI " + "where DOKI.GOD='" + year + "' AND STDOKI.CSKL" + cskl + " " + carting + " AND " + util.getDoc("DOKI", "STDOKI") + " AND stdoki.cart = artikli.cart " + " AND DOKI.DATDOK <= " + dateP + " " + dokumentiN("STDOKI") + "UNION ALL " +
        // Samo poravnanja na ulazu
        "SELECT STDOKU.BRDOK, STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NC, STDOKU.VC, STDOKU.MC, STDOKU.NAZART, STDOKU.JM, STDOKU.KOL ," + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS VRI, " + "STDOKU.IPOR AS POR, STDOKU.IMAR AS MAR, STDOKU.INAB AS NAB " + "from ARTIKLI,STDOKU,DOKU " + "where DOKU.GOD='" + year + "' AND STDOKU.CSKL" + cskl + " " + carting + " AND " + util.getDoc("DOKU", "STDOKU") + "  AND DOKU.VRDOK='POR' AND STDOKU.SKOL>0 " + "AND DOKU.DATDOK <= " + dateP + " AND STDOKU.CART=ARTIKLI.CART " + "UNION ALL " +
        // Poravnanje
        "SELECT STDOKU.BRDOK, 'POR', STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NC, STDOKU.VC, STDOKU.MC, STDOKU.NAZART, STDOKU.JM, STDOKU.KOL-STDOKU.KOL, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS VRI, " + "STDOKU.IPOR AS POR, STDOKU.IMAR AS MAR, STDOKU.INAB AS NAB " + "from STDOKU,DOKU,ARTIKLI " + "where DOKU.GOD='" + year + "' AND STDOKU.CSKL" + cskl + " " + carting + " AND " + util.getDoc("DOKU", "STDOKU") + "  AND STDOKU.PORAV<>0 AND STDOKU.VRDOK NOT IN ('POR') " + "AND DOKU.DATDOK <= " + dateP + " AND stdoku.cart = artikli.cart " + "UNION ALL " +
        // Medjuskladisnice - ulaz
        "SELECT MESKLA.BRDOK, MESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1, STMESKLA.BC, STMESKLA.NC, STMESKLA.VC, STMESKLA.MC, STMESKLA.NAZART, STMESKLA.JM, STMESKLA.KOL, STMESKLA.ZCUL, STMESKLA.ZADRAZUL AS VRI, " + "STMESKLA.IPORUL AS POR, STMESKLA.IMARUL AS MAR, STMESKLA.INABUL AS NAB " + "from STMESKLA, MESKLA,ARTIKLI " + "where MESKLA.GOD='" + year + "' AND (MESKLA.VRDOK='MEU' OR MESKLA.VRDOK='MES') AND STMESKLA.CSKLUL" + cskl + " " + carting + " AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ " + "AND MESKLA.DATDOK <= " + dateP + " AND stmeskla.cart = artikli.cart AND meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok " + "UNION ALL " +
        // Medjuskladisnice - izlaz
        "SELECT MESKLA.BRDOK, MESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1, STMESKLA.BC, STMESKLA.NC, STMESKLA.VC, STMESKLA.MC, STMESKLA.NAZART, STMESKLA.JM, -STMESKLA.KOL, STMESKLA.ZC, -STMESKLA.ZADRAZIZ AS VRI, " + "STMESKLA.IPORIZ AS POR, STMESKLA.IMARIZ AS MAR, STMESKLA.INABIZ AS NAB " + "from STMESKLA,MESKLA,ARTIKLI " + "where MESKLA.GOD='" + year + "' AND (MESKLA.VRDOK='MEI' OR MESKLA.VRDOK='MES') AND STMESKLA.CSKLIZ" + cskl + " " + carting + " AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ " + "AND MESKLA.DATDOK <= " + dateP + " AND stmeskla.cart = artikli.cart AND meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok " + "UNION ALL " +
        // Medjuskladisnice - Poravnanje
        "SELECT MESKLA.BRDOK, 'POR', STMESKLA.CART, STMESKLA.CART1, STMESKLA.BC, STMESKLA.NC, STMESKLA.VC, STMESKLA.MC, STMESKLA.NAZART, STMESKLA.JM, STMESKLA.KOL-STMESKLA.KOL, " + this.getZCPor("STMESKLA", vrzal) + " STMESKLA.PORAV AS VRI, " + "STMESKLA.DIOPORPOR AS POR, STMESKLA.DIOPORMAR AS MAR, (STMESKLA.DIOPORMAR-STMESKLA.DIOPORMAR)  AS NAB " + "from STMESKLA,MESKLA,ARTIKLI " + "where MESKLA.GOD='" + year + "' AND STMESKLA.CSKLUL" + cskl + " " + carting + " AND stmeskla.cart = artikli.cart AND meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok AND STMESKLA.PORAV<>0 " + "AND MESKLA.DATDOK <= " + dateP + "ORDER BY " + ordering + descending;

    //    System.out.println("STANJE KOMPLICIRANO : " + qStr);

    return qStr;
  }
  
  public String getSklSkladSqlPoj(String cSkl, String cArt, String dateP, String sljed) {
    String year = dateP.substring(1, 5);
    String qStr;
    String carting = "";
    String descending = "";
    String ordering = "";
    if (sljed.equals("KOL")) {
      ordering = "7";
      descending = " descending";
    } else if (sljed.equals("NAZART")) {
      ordering = "6";
      descending = " " + vl.getCollateSQL();
    } else {
      ordering = Aut.getAut().getCARTdependable("3", "4", "5");
    }

    if (!cArt.equals("")) {
      carting = "AND " + cArt;
    }
    
    String cskl;
    
    if (!cSkl.equals("")) cskl = " = '"+cSkl+"' ";
    else {
      QueryDataSet skls = util.getSkladFromCorg();
      cskl = " in (";
      skls.first();
      for (;;) {
        cskl += "'"+skls.getString("CSKL")+"'";
        if (skls.next()) cskl += ",";
        else {
          cskl += ") ";
          break;
        }
      }
    }

    // Ulazi
    qStr = "SELECT  STDOKU.BRDOK, STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NAZART, STDOKU.JM, STDOKU.KOL from STDOKU, DOKU where DOKU.GOD='" + year + "' AND STDOKU.CSKL" + cskl + " " + carting + " AND " + util.getDoc("DOKU", "STDOKU") + " AND DOKU.DATDOK <= " + dateP + " AND DOKU.VRDOK not in ('KAL','POR') UNION ALL " +
    // Izlazi
        "SELECT  STDOKI.BRDOK, STDOKI.VRDOK, STDOKI.CART, STDOKI.CART1, STDOKI.BC, STDOKI.NAZART, STDOKI.JM, -STDOKI.KOL from STDOKI, DOKI where DOKI.GOD='" + year + "' AND STDOKI.CSKL" + cskl + " " + carting + " AND " + util.getDoc("DOKI", "STDOKI") + " AND DOKI.DATDOK <= " + dateP + " AND DOKI.VRDOK not in ('RAC','GRN','PRD','PON','NKU','NDO','ZAH','TRE','TER','ODB','POS') and (stdoki.veza is null or STDOKI.VEZA not LIKE '%DOS%') UNION ALL " +
        // Medjuskladisnice - ulaz
        "SELECT MESKLA.BRDOK, MESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1, STMESKLA.BC, STMESKLA.NAZART, STMESKLA.JM, STMESKLA.KOL from STMESKLA, MESKLA,ARTIKLI " + "where MESKLA.GOD='" + year + "' AND (MESKLA.VRDOK='MEU' OR MESKLA.VRDOK='MES') AND STMESKLA.CSKLUL" + cskl + " " + carting + " AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ " + "AND MESKLA.DATDOK <= " + dateP + " AND stmeskla.cart = artikli.cart AND meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok " + "UNION ALL " +
        // Medjuskladisnice - izlaz
        "SELECT MESKLA.BRDOK, MESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1, STMESKLA.BC, STMESKLA.NAZART, STMESKLA.JM, -STMESKLA.KOL from STMESKLA,MESKLA,ARTIKLI " + "where MESKLA.GOD='" + year + "' AND (MESKLA.VRDOK='MEI' OR MESKLA.VRDOK='MES') AND STMESKLA.CSKLIZ" + cskl + " " + carting + " AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ " + "AND MESKLA.DATDOK <= " + dateP + " AND stmeskla.cart = artikli.cart AND meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok ORDER BY " + ordering + descending;

    //    System.out.println("STANJE KOMPLICIRANO : " + qStr);

    return qStr;
  }

  //  public String getSkladSql(String vrzal, String cSkl, String cArt, String
  // dateP) {
  //    String qStr;
  //
  //  // Ulazi
  //    qStr="SELECT STDOKU.BRDOK, STDOKU.VRDOK, STDOKU.CART, STDOKU.NAZART,
  // STDOKU.KOL, STDOKU.ZC, STDOKU.IZAD AS VRI, "+
  //         "STDOKU.IPOR AS POR, STDOKU.IMAR AS MAR, STDOKU.INAB AS NAB "+
  //         "from STDOKU, DOKU "+
  //  // "where STDOKU."+cArt+" AND STDOKU.CSKL='"+cSkl+"' AND
  // "+util.getDoc("DOKU", "STDOKU")+" "+
  //         "where STDOKU.CSKL='"+cSkl+"' AND "+util.getDoc("DOKU", "STDOKU")+" "+
  //         "AND DOKU.DATDOK <= "+dateP+ " AND STDOKU.BRDOK=DOKU.BRDOK AND
  // STDOKU.VRDOK=DOKU.VRDOK "+
  //         "UNION ALL "+
  //  // Izlazi
  //    "SELECT STDOKI.BRDOK, STDOKI.VRDOK, STDOKI.CART, STDOKI.NAZART,
  // -STDOKI.KOL, STDOKI.ZC, -STDOKI.IRAZ AS VRI, "+
  //         "STDOKI.IPOR AS POR, STDOKI.IMAR AS MAR, STDOKI.INAB AS NAB "+
  //         "from STDOKI, DOKI "+
  //  // "where STDOKI."+cArt+" AND STDOKI.CSKL='"+cSkl+"' AND
  // "+util.getDoc("DOKI", "STDOKI")+" "+
  //         "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+" "+
  //         "AND DOKI.DATDOK <= "+dateP+" AND STDOKI.BRDOK=DOKI.BRDOK AND
  // STDOKI.VRDOK=DOKI.VRDOK " +dokumentiN("STDOKI")+
  //         "UNION ALL "+
  //  // Samo poravnanja na ulazu
  //  // "SELECT STDOKU.BRDOK, STDOKU.VRDOK, STDOKU.CART, STDOKU.NAZART,
  // STDOKU.KOL , STDOKU.PORAV/STDOKU.SKOL AS ZC, STDOKU.PORAV AS VRI, "+
  //    "SELECT STDOKU.BRDOK, STDOKU.VRDOK, STDOKU.CART, STDOKU.NAZART, STDOKU.KOL
  // , "+this.getZCPor("STDOKU", vrzal)+" STDOKU.PORAV AS VRI, "+
  //         "STDOKU.IPOR AS POR, STDOKU.IMAR AS MAR, STDOKU.INAB AS NAB "+
  //         "from ARTIKLI,STDOKU,DOKU "+
  //  // "where STDOKU."+cArt+" AND STDOKU.CSKL='"+cSkl+"' AND
  // "+util.getDoc("DOKU", "STDOKU") +
  //         "where STDOKU.CSKL='"+cSkl+"' AND "+util.getDoc("DOKU", "STDOKU") +
  //         " AND DOKU.VRDOK='POR' AND STDOKU.SKOL>0 "+
  //         "AND DOKU.DATDOK <= "+dateP+" AND STDOKU.CART=ARTIKLI.CART AND
  // STDOKU.VRDOK=DOKU.VRDOK AND STDOKU.BRDOK=DOKU.BRDOK "+
  //         "UNION ALL "+
  //  // Poravnanje
  //  // "SELECT STDOKU.BRDOK, 'POR', STDOKU.CART, STDOKU.NAZART,
  // STDOKU.KOL-STDOKU.KOL, STDOKU.PORAV/STDOKU.SKOL, STDOKU.PORAV AS VRI, "+
  //    "SELECT STDOKU.BRDOK, 'POR', STDOKU.CART, STDOKU.NAZART,
  // STDOKU.KOL-STDOKU.KOL, "+this.getZCPor("STDOKU", vrzal)+" STDOKU.PORAV AS
  // VRI, "+
  //         "STDOKU.IPOR AS POR, STDOKU.IMAR AS MAR, STDOKU.INAB AS NAB "+
  //         "from STDOKU,DOKU "+
  //  // "where STDOKU."+cArt+" AND STDOKU.CSKL='"+cSkl+"' AND
  // "+util.getDoc("DOKU", "STDOKU")+" AND STDOKU.PORAV<>0 AND STDOKU.VRDOK NOT
  // IN ('POR') "+
  //         "where STDOKU.CSKL='"+cSkl+"' AND "+util.getDoc("DOKU", "STDOKU")+" AND
  // STDOKU.PORAV<>0 AND STDOKU.VRDOK NOT IN ('POR') "+
  //         "AND DOKU.DATDOK <= "+dateP+" AND STDOKU.BRDOK=DOKU.BRDOK AND
  // STDOKU.VRDOK=DOKU.VRDOK "+
  //         "UNION ALL "+
  //  // Medjuskladisnice - ulaz
  //    "SELECT MESKLA.BRDOK, MESKLA.VRDOK, STMESKLA.CART, STMESKLA.NAZART,
  // STMESKLA.KOL, STMESKLA.ZCUL, STMESKLA.ZADRAZUL AS VRI, "+
  //         "STMESKLA.IPORUL AS POR, STMESKLA.IMARUL AS MAR, STMESKLA.INABUL AS NAB "+
  //         "from STMESKLA, MESKLA "+
  //  // "where STMESKLA."+cArt+" AND STMESKLA.CSKLUL='"+cSkl+"' AND
  // MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ " +
  //         "where STMESKLA.CSKLUL='"+cSkl+"' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND
  // MESKLA.CSKLIZ=STMESKLA.CSKLIZ " +
  //         "AND MESKLA.DATDOK <= "+dateP+" AND MESKLA.VRDOK=STMESKLA.VRDOK AND
  // MESKLA.BRDOK=STMESKLA.BRDOK " +
  //         "UNION ALL "+
  //  // Medjuskladisnice - izlaz
  //    "SELECT MESKLA.BRDOK, MESKLA.VRDOK, STMESKLA.CART, STMESKLA.NAZART,
  // -STMESKLA.KOL, STMESKLA.ZC, -STMESKLA.ZADRAZIZ AS VRI, "+
  //         "STMESKLA.IPORIZ AS POR, STMESKLA.IMARIZ AS MAR, STMESKLA.INABIZ AS NAB "+
  //         "from STMESKLA,MESKLA "+
  //  // "where STMESKLA."+cArt+" AND STMESKLA.CSKLIZ='"+cSkl+"' AND
  // MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ "+
  //         "where STMESKLA.CSKLIZ='"+cSkl+"' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND
  // MESKLA.CSKLIZ=STMESKLA.CSKLIZ "+
  //         "AND MESKLA.DATDOK <= "+dateP+" AND MESKLA.VRDOK=STMESKLA.VRDOK AND
  // MESKLA.BRDOK = STMESKLA.BRDOK " +
  //         "UNION ALL "+
  //  // Medjuskladisnice - Poravnanje
  //  // "SELECT MESKLA.BRDOK, 'POR', STMESKLA.CART, STMESKLA.NAZART,
  // STMESKLA.KOL-STMESKLA.KOL, STMESKLA.PORAV/STMESKLA.SKOL, STMESKLA.PORAV AS
  // VRI, "+
  //    "SELECT MESKLA.BRDOK, 'POR', STMESKLA.CART, STMESKLA.NAZART,
  // STMESKLA.KOL-STMESKLA.KOL, "+this.getZCPor("STMESKLA", vrzal)+"
  // STMESKLA.PORAV AS VRI, "+
  //         "STMESKLA.DIOPORPOR AS POR, STMESKLA.DIOPORMAR AS MAR, 0.00 AS NAB "+
  //         "from STMESKLA,MESKLA "+
  //  // "where STMESKLA."+cArt+" AND STMESKLA.CSKLUL='"+cSkl+"' AND
  // MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND
  // MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND
  // MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 "+
  //         "where STMESKLA.CSKLUL='"+cSkl+"' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND
  // MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND
  // MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK AND
  // STMESKLA.PORAV<>0 "+
  //         "AND MESKLA.DATDOK <= "+dateP +" AND MESKLA.BRDOK=STMESKLA.BRDOK ";
  //  // System.out.println("Arghhhh SND: " + qStr);
  //    return qStr;
  //  }

  public String findStanje(String cskl, int cart, String godina) {
    String qStr = "select * from stanje where cskl ='" + cskl + "' and cart = " + cart + " and god='" + godina + "'";
    return qStr;
  }

  //********************************************************************************//
  //                                                                                //
  //                           T O T A L I K A R T I C A //
  //                                                                                //
  //********************************************************************************//

  //*** Query string za sve kartice
  public String getSveKartice(String cKartArtPoc, String cKartArtZav, String vrzal, String cSkl, String dateP, String dateZ, String addCart) {
    //Pocetni datum hardcodiran na pocetak 2002 godine//vise nije
    String qStr;
    if (cKartArtPoc.trim().equals(""))
      cKartArtPoc = "0";
    if (!cKartArtZav.trim().equals("")) {
      // Ulazi
      qStr = "SELECT STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NAZART, STDOKU.JM, DOKU.BRDOK, DOKU.DATDOK, STDOKU.KOL AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, STDOKU.ZC, STDOKU.IZAD AS KOLZAD, (STDOKU.IZAD-STDOKU.IZAD) AS KOLRAZ " + "from ARTIKLI, STDOKU, DOKU " + "where STDOKU.CART>=" + cKartArtPoc + " AND STDOKU.CART<=" + cKartArtZav + " AND STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND DOKU.VRDOK NOT IN ('POR') " + "AND DOKU.DATDOK <= " + dateZ + " AND DOKU.DATDOK >= " + dateP + " AND STDOKU.CART=ARTIKLI.CART " + addCart + "UNION ALL " +
      // Samo poravnanja na ulazu
          "SELECT STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NAZART, STDOKU.JM, DOKU.BRDOK, DOKU.DATDOK, (STDOKU.KOL-STDOKU.KOL) AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS KOLZAD, (STDOKU.PORAV-STDOKU.PORAV) AS KOLRAZ " + "from ARTIKLI,STDOKU,DOKU " + "where STDOKU.CART>=" + cKartArtPoc + " AND STDOKU.CART<=" + cKartArtZav + " AND STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + "  AND DOKU.VRDOK='POR' AND STDOKU.SKOL>0 " + "AND DOKU.DATDOK <= " + dateP + " AND STDOKU.CART=ARTIKLI.CART " + addCart + "UNION ALL " +
          // Izlazi
          "SELECT STDOKI.VRDOK, STDOKI.CART, STDOKI.CART1, STDOKI.BC, STDOKI.NAZART, STDOKI.JM, DOKI.BRDOK, DOKI.DATDOK, (STDOKI.KOL-STDOKI.KOL) AS KOLUL, STDOKI.KOL AS KOLIZ, STDOKI.ZC, (STDOKI.IRAZ-STDOKI.IRAZ) AS KOLZAD, STDOKI.IRAZ AS KOLRAZ " + "from ARTIKLI, STDOKI,DOKI " + "where STDOKI.CART>=" + cKartArtPoc + " and STDOKI.CART<=" + cKartArtZav + " AND STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " " + "AND DOKI.DATDOK <= " + dateZ + " AND STDOKI.CART=ARTIKLI.CART " + addCart + "AND DOKI.DATDOK >= " + dateP + " " + dokumentiN("STDOKI") + "UNION ALL " +
          // Poravnanje
          "SELECT STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NAZART, STDOKU.JM, DOKU.BRDOK, DOKU.DATDOK, (STDOKU.KOL-STDOKU.KOL) AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS KOLZAD, (STDOKU.PORAV-STDOKU.PORAV) AS KOLRAZ  " + "from ARTIKLI, STDOKU,DOKU " + "where STDOKU.CART>=" + cKartArtPoc + " AND STDOKU.CART<=" + cKartArtZav + " AND STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND STDOKU.PORAV<>0 " + "AND DOKU.DATDOK <= " + dateZ + " AND DOKU.DATDOK >= " + dateP + " AND STDOKU.CART=ARTIKLI.CART  AND STDOKU.VRDOK NOT IN ('POR') " + addCart + "UNION ALL " +
          // Medjuskladisnice - ulaz
          "SELECT STMESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1 ,STMESKLA.BC, STMESKLA.NAZART, STMESKLA.JM, MESKLA.BRDOK, MESKLA.DATDOK, STMESKLA.KOL AS KOLUL, (STMESKLA.KOL-STMESKLA.KOL) AS KOLIZ, STMESKLA.ZC, STMESKLA.ZADRAZUL AS KOLZAD, (STMESKLA.ZADRAZUL-STMESKLA.ZADRAZUL) AS KOLRAZ " + "from ARTIKLI, STMESKLA,MESKLA " + "where (MESKLA.VRDOK = 'MEU' OR MESKLA.VRDOK = 'MES') AND STMESKLA.CART>=" + cKartArtPoc + " AND STMESKLA.CART<=" + cKartArtZav + " AND STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK " + "AND MESKLA.DATDOK <= " + dateZ + " AND MESKLA.DATDOK >= " + dateP + " AND STMESKLA.CART=ARTIKLI.CART " + addCart + "UNION ALL " +
          // Medjuskladisnice - izlaz
          "SELECT STMESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1 ,STMESKLA.BC, STMESKLA.NAZART, STMESKLA.JM, MESKLA.BRDOK, MESKLA.DATDOK, (STMESKLA.KOL-STMESKLA.KOL) AS KOLUL, STMESKLA.KOL AS KOLIZ, STMESKLA.ZC, (STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS KOLZAD, STMESKLA.ZADRAZIZ AS KOLRAZ " + "from ARTIKLI, STMESKLA,MESKLA " + "where (MESKLA.VRDOK = 'MEI' OR MESKLA.VRDOK = 'MES') AND STMESKLA.CART>=" + cKartArtPoc + " AND STMESKLA.CART<=" + cKartArtZav + " AND STMESKLA.CSKLIZ='" + cSkl + "' AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK " + "AND MESKLA.DATDOK <= " + dateZ + " AND MESKLA.DATDOK >= " + dateP + " AND STMESKLA.CART=ARTIKLI.CART " + addCart + "UNION ALL " +
          // Medjuskladisnice - Poravnanje
          "SELECT STMESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1 ,STMESKLA.BC, STMESKLA.NAZART, STMESKLA.JM, MESKLA.BRDOK, MESKLA.DATDOK, (STMESKLA.KOL-STMESKLA.KOL) as KOLUL, (STMESKLA.KOL-STMESKLA.KOL) as KOLIZ, " + this.getZCPor("STMESKLA", vrzal) + " STMESKLA.PORAV AS KOLZAD, (STMESKLA.PORAV-STMESKLA.PORAV) AS KOLRAZ " + "from ARTIKLI, STMESKLA,MESKLA " + "where STMESKLA.CART>=" + cKartArtPoc + " AND STMESKLA.CART<=" + cKartArtZav + " AND STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 " + "AND MESKLA.DATDOK <= " + dateZ + " AND STMESKLA.CART=ARTIKLI.CART " + addCart + "AND MESKLA.DATDOK >= " + dateP;
    } else {
      // Ulazi
      qStr = "SELECT STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NAZART, STDOKU.JM, DOKU.BRDOK, DOKU.DATDOK, STDOKU.KOL AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, STDOKU.ZC, STDOKU.IZAD AS KOLZAD, (STDOKU.IZAD-STDOKU.IZAD) AS KOLRAZ " + "from ARTIKLI,STDOKU,DOKU " + "where STDOKU.CART>=" + cKartArtPoc + " AND STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND DOKU.VRDOK NOT IN ('POR') " + "AND DOKU.DATDOK <= " + dateZ + " AND STDOKU.CART=ARTIKLI.CART " + addCart + "AND DOKU.DATDOK >= " + dateP + " UNION ALL " +
      // Samo poravnanja na ulazu
          "SELECT STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NAZART, STDOKU.JM, DOKU.BRDOK, DOKU.DATDOK, (STDOKU.KOL-STDOKU.KOL) AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS KOLZAD, (STDOKU.PORAV-STDOKU.PORAV) AS KOLRAZ " + "from ARTIKLI,STDOKU,DOKU " + "where STDOKU.CART>=" + cKartArtPoc + " AND STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + "  AND DOKU.VRDOK='POR' AND STDOKU.SKOL>0 " + "AND DOKU.DATDOK <= " + dateP + " AND STDOKU.CART=ARTIKLI.CART " + addCart + "UNION ALL " +
          // Izlazi
          "SELECT STDOKI.VRDOK, STDOKI.CART, STDOKI.CART1, STDOKI.BC, STDOKI.NAZART, STDOKI.JM, DOKI.BRDOK, DOKI.DATDOK, (STDOKI.KOL-STDOKI.KOL) AS KOLUL, STDOKI.KOL AS KOLIZ, STDOKI.ZC, (STDOKI.IRAZ-STDOKI.IRAZ) AS KOLZAD, STDOKI.IRAZ AS KOLRAZ " + "from ARTIKLI,STDOKI,DOKI " + "where STDOKI.CART>=" + cKartArtPoc + " AND STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " " + "AND DOKI.DATDOK <= " + dateZ + " AND STDOKI.CART=ARTIKLI.CART " + addCart + "AND DOKI.DATDOK >= " + dateP + " " + dokumentiN("STDOKI") + " UNION ALL " +
          // Poravnanje
          "SELECT STDOKU.VRDOK, STDOKU.CART, STDOKU.CART1, STDOKU.BC, STDOKU.NAZART, STDOKU.JM, DOKU.BRDOK, DOKU.DATDOK, (STDOKU.KOL-STDOKU.KOL) AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS KOLZAD, (STDOKU.PORAV-STDOKU.PORAV) AS KOLRAZ  " + "from ARTIKLI, STDOKU,DOKU " + "where STDOKU.CART>=" + cKartArtPoc + " AND STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND STDOKU.PORAV<>0 " + "AND DOKU.DATDOK <= " + dateZ + " AND STDOKU.CART=ARTIKLI.CART " + addCart + "AND DOKU.DATDOK >= " + dateP + " UNION ALL " +
          // Medjuskladisnice - ulaz
          "SELECT STMESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1 ,STMESKLA.BC, STMESKLA.NAZART, STMESKLA.JM, MESKLA.BRDOK, MESKLA.DATDOK, STMESKLA.KOL AS KOLUL, (STMESKLA.KOL-STMESKLA.KOL) AS KOLIZ, STMESKLA.ZC, STMESKLA.ZADRAZUL AS KOLZAD, (STMESKLA.ZADRAZUL-STMESKLA.ZADRAZUL) AS KOLRAZ " + "from ARTIKLI, STMESKLA,MESKLA " + "where (MESKLA.VRDOK = 'MEU' OR MESKLA.VRDOK = 'MES') AND STMESKLA.CART>=" + cKartArtPoc + " AND STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK " + "AND MESKLA.DATDOK <= " + dateZ + " AND STMESKLA.CART=ARTIKLI.CART " + addCart + "AND MESKLA.DATDOK >= " + dateP + " UNION ALL " +
          // Medjuskladisnice - izlaz
          "SELECT STMESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1 ,STMESKLA.BC, STMESKLA.NAZART, STMESKLA.JM, MESKLA.BRDOK, MESKLA.DATDOK, (STMESKLA.KOL-STMESKLA.KOL) AS KOLUL, STMESKLA.KOL AS KOLIZ, STMESKLA.ZC, (STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS KOLZAD, STMESKLA.ZADRAZIZ AS KOLRAZ " + "from ARTIKLI,STMESKLA,MESKLA " + "where (MESKLA.VRDOK = 'MEI' OR MESKLA.VRDOK = 'MES') AND STMESKLA.CART>=" + cKartArtPoc + " AND STMESKLA.CSKLIZ='" + cSkl + "' AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK " + "AND MESKLA.DATDOK <= " + dateZ + " AND STMESKLA.CART=ARTIKLI.CART " + addCart + "AND MESKLA.DATDOK >= " + dateP + " UNION ALL " +
          // Medjuskladisnice - Poravnanje
          "SELECT STMESKLA.VRDOK, STMESKLA.CART, STMESKLA.CART1 ,STMESKLA.BC, STMESKLA.NAZART, STMESKLA.JM, MESKLA.BRDOK, MESKLA.DATDOK, (STMESKLA.KOL-STMESKLA.KOL) as KOLUL, (STMESKLA.KOL-STMESKLA.KOL) as KOLIZ, " + this.getZCPor("STMESKLA", vrzal) + " STMESKLA.PORAV AS KOLZAD, (STMESKLA.PORAV-STMESKLA.PORAV) AS KOLRAZ " + "from ARTIKLI, STMESKLA,MESKLA " + "where STMESKLA.CART>=" + cKartArtPoc + " AND STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 " + "AND MESKLA.DATDOK <= " + dateZ + " AND STMESKLA.CART=ARTIKLI.CART " + addCart + "AND MESKLA.DATDOK >= " + dateP;
    }
    //    System.out.println("QSTR\n"+qStr);
    return qStr;
  }

  //*** Punjenje postojecih artikala za totale kartica
  public String getSveKarticeNull(String cArtPoc, String cArtZav, String cSkl, Timestamp ts, String addCart) {
    String qStr;
    if (cArtPoc.trim().equals(""))
      cArtPoc = "0";
    if (cArtZav.trim().equals("")) {
      qStr = "SELECT 'test' AS VRDOK, STANJE.CART,ARTIKLI.CART1,ARTIKLI.BC, ARTIKLI.NAZART, ARTIKLI.JM, 0 AS BRDOK, '" + ts + "' AS DATDOK, 0.000  AS KOLUL, 0.000 AS KOLIZ, 0.00  AS ZC, 0.00  AS KOLZAD, 0.00 AS KOLRAZ " + "from  ARTIKLI, STANJE where STANJE.CART>=" + cArtPoc + " AND ARTIKLI.CART=STANJE.CART  AND STANJE.CSKL='" + cSkl + "' AND STANJE.GOD=" + hr.restart.util.Valid.getValid().findYear(ts) + addCart + "ORDER BY STANJE.CART";
    } else {
      qStr = "SELECT 'test' AS VRDOK, STANJE.CART, ARTIKLI.CART1, ARTIKLI.BC, ARTIKLI.NAZART, ARTIKLI.JM, 0 AS BRDOK, '" + ts + "' AS DATDOK,  0.000  AS KOLUL, 0.000 AS KOLIZ, 0.00  AS ZC, 0.00  AS KOLZAD, 0.00 AS KOLRAZ " + "from  ARTIKLI, STANJE where STANJE.CART>=" + cArtPoc + " AND STANJE.CART<=" + cArtZav + " AND ARTIKLI.CART=STANJE.CART  AND STANJE.CSKL='" + cSkl + "' AND STANJE.GOD=" + hr.restart.util.Valid.getValid().findYear(ts) + addCart + "ORDER BY STANJE.CART";
    }
    return qStr;
  }

  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //--------------------- K N J I G A P O P I S A R O B E
  // ------------------------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  //*** Ulazi Knjige Popisa Robe
  public String getKPRUlSql(String cSkl, String dateP, String dateZ, String parametar) {
    //    System.out.println("datumi: " + dateP+" "+dateZ);
    String qStr, izlazi;
    izlazi = this.izlazi(cSkl, dateP, dateZ, parametar);
    //    qStr="SELECT ' ' as CSKLUL,' ' as CSKLIZ, MAX(0) as RBR, "+
    qStr = "SELECT MAX(DOKU.CSKL) as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, " + "MAX(DOKU.DATDOK) AS DATDOK, STDOKU.VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, " + "SUM(STDOKU.IZAD" + /* +STDOKU.PORAV */") AS SUMZAD, SUM(STDOKU.IZAD-STDOKU.IZAD) AS SUMRAZ " + "from STDOKU, DOKU " + "where STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND DOKU.DATDOK >= " + dateP + " AND DOKU.DATDOK <= " + dateZ + " GROUP BY STDOKU.VRDOK, STDOKU.BRDOK " + "UNION " +
    // Poravnanje
        "select MAX(DOKU.CSKL) as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKU.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, SUM(STDOKU.PORAV) AS SUMZAD, SUM(STDOKU.PORAV-STDOKU.PORAV) AS SUMRAZ " + "from STDOKU,DOKU " + "where STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND (STDOKU.PORAV<>0 OR STDOKU.VRDOK IN ('POR')) " + //AND
        // STDOKU.VRDOK
        // NOT
        // IN
        // ('POR')
        // "+
        "AND DOKU.DATDOK >= " + dateP + " AND DOKU.DATDOK <= " + dateZ + " GROUP BY  STDOKU.VRDOK, STDOKU.BRDOK  " + "UNION " +
        // Medjuskladisnice - ulaz
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND,  MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.ZADRAZUL) AS SUMZAD, SUM(STMESKLA.ZADRAZUL-STMESKLA.ZADRAZUL) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK in  ('MEU','MES') and STMESKLA.CSKLUL='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " + "AND MESKLA.DATDOK >= " + dateP + " AND MESKLA.DATDOK <= " + dateZ + " GROUP BY MESKLA.VRDOK, MESKLA.BRDOK, MESKLA.CSKLIZ " + "UNION " +
        // Medjuskladisnice - Poravnanje
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.PORAV) AS SUMZAD, SUM(STMESKLA.PORAV-STMESKLA.PORAV) AS SUMRAZ " + "from STMESKLA,MESKLA " + "where STMESKLA.VRDOK in  ('MEU','MES') and STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 " + "AND MESKLA.DATDOK >= " + dateP + " AND MESKLA.DATDOK <= " + dateZ + " GROUP BY MESKLA.VRDOK,  MESKLA.BRDOK, MESKLA.CSKLIZ " + izlazi;

    return qStr;
  }

  public String getKPRUlSql2(String cSkl, Timestamp dateZ, String parametar) {
    //    System.out.println("datum: "+dateZ);
    String qStr, izlazi;
    String bitvin = hr.restart.baza.Condition.between("DATDOK", ut.getFirstDayOfYear(dateZ), dateZ).toString();
    izlazi = this.izlazi2(cSkl, dateZ, parametar);
    //    qStr="SELECT ' ' as CSKLUL,' ' as CSKLIZ, MAX(0) as RBR, "+
    qStr = "SELECT MAX(DOKU.CSKL) as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, " + "MAX(DOKU.DATDOK) AS DATDOK, STDOKU.VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, " + "SUM(STDOKU.IZAD" + /* +STDOKU.PORAV */") AS SUMZAD, SUM(STDOKU.IZAD-STDOKU.IZAD) AS SUMRAZ " + "from STDOKU, DOKU " + "where STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND DOKU.STAT_KPR='N' AND DOKU." + bitvin/*
                                                                                                                                                                                                                                                                                                                                                                                                 * DATDOK <=
                                                                                                                                                                                                                                                                                                                                                                                                 * "+dateZ
                                                                                                                                                                                                                                                                                                                                                                                                 */+ " GROUP BY STDOKU.VRDOK, STDOKU.BRDOK " + "UNION " +
    // Poravnanje
        "select MAX(DOKU.CSKL) as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKU.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, SUM(STDOKU.PORAV) AS SUMZAD, SUM(STDOKU.PORAV-STDOKU.PORAV) AS SUMRAZ " + "from STDOKU,DOKU " + "where STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND (STDOKU.PORAV<>0 OR STDOKU.VRDOK IN ('POR')) " + //"
        // AND
        // STDOKU.PORAV<>0
        // AND
        // STDOKU.VRDOK
        // NOT
        // IN
        // ('POR')
        // "+
        "AND DOKU.STAT_KPR='N' AND DOKU." + bitvin/* DATDOK <= "+dateZ */+ " GROUP BY  STDOKU.VRDOK, STDOKU.BRDOK  " + "UNION " +
        // Medjuskladisnice - ulaz
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND,  MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.ZADRAZUL) AS SUMZAD, SUM(STMESKLA.ZADRAZUL-STMESKLA.ZADRAZUL) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK in ('MEU','MES') and STMESKLA.CSKLUL='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " + "AND MESKLA.STAT_KPR='N' AND MESKLA." + bitvin/*
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * DATDOK <=
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  * "+dateZ
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  */+ " GROUP BY MESKLA.VRDOK, MESKLA.BRDOK, MESKLA.CSKLIZ " + "UNION " +
        // Medjuskladisnice - ulaz MES
        //   "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ,
        // MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK,
        // '' AS KOLND, MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.ZADRAZUL) AS SUMZAD,
        // 0.00 AS SUMRAZ "+
        //        "from STMESKLA, MESKLA "+
        //        "where STMESKLA.VRDOK = 'MES' and STMESKLA.CSKLUL='"+cSkl+"' AND
        // STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND
        // STMESKLA.GOD=MESKLA.GOD " +
        //        " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK "
        // +
        //        "AND MESKLA.STAT_KPR='N' AND MESKLA.DATDOK <= "+dateZ+" GROUP BY
        // MESKLA.VRDOK, MESKLA.BRDOK, MESKLA.CSKLIZ " +
        //        "UNION "+
        // Medjuskladisnice - Poravnanje
        //   "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ,
        // MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS
        // KOLND, MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.PORAV) AS SUMZAD, 0.00 AS
        // SUMRAZ "+
        //        "from STMESKLA,MESKLA "+
        //        "where STMESKLA.VRDOK = 'MES' and STMESKLA.CSKLUL='"+cSkl+"' AND
        // MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND
        // MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND
        // MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 "+
        //        "AND MESKLA.STAT_KPR='N' AND MESKLA.DATDOK <= "+dateZ+" GROUP BY
        // MESKLA.VRDOK, MESKLA.BRDOK "+
        //        "UNION "+
        // Medjuskladisnice - Poravnanje
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.PORAV) AS SUMZAD, SUM(STMESKLA.PORAV-STMESKLA.PORAV) AS SUMRAZ " + "from STMESKLA,MESKLA " + "where STMESKLA.VRDOK in ('MEU','MES') and STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 " + "AND MESKLA.STAT_KPR='N' AND MESKLA." + bitvin/*
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   * DATDOK <=
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   * "+dateZ
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   */+ " GROUP BY MESKLA.VRDOK,  MESKLA.BRDOK, MESKLA.CSKLIZ " + izlazi;

    return qStr;
  }

  //*** Racunanje Donosa Za Knjigu Popisa Robe
  public String getKPRDonosSql(String cSkl, String dateP, String parametar) {
    String qStr, izlazi;
    int godina = hr.restart.util.Aus.getNumber(dateP.substring(1, dateP.indexOf("-")));
    String pocGod = godina + "-01-01  00:00:00.0";

    izlazi = this.donosIzlazi(cSkl, dateP, parametar);

    // Ulazi
    qStr = "select '            ' as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKU.DATDOK) AS DATDOK, " + "STDOKU.VRDOK AS VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, " + "SUM(STDOKU.IZAD" + /* +STDOKU.PORAV */") AS SUMZAD, " + "SUM(STDOKU.IZAD-STDOKU.IZAD) AS SUMRAZ " + "from STDOKU, DOKU " + "where STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") +
    //         " AND DOKU.DATDOK < "+dateP+" AND DOKU.DATDOK >= '"+pocGod+"' GROUP BY
        // STDOKU.BRDOK, STDOKU.VRDOK "+
        " AND DOKU.DATDOK < " + dateP + " AND DOKU.DATDOK >= '" + pocGod + "' GROUP BY STDOKU.VRDOK, STDOKU.BRDOK " + "UNION " +
        // Poravnanje
        "select '            ' as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKU.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS KOLND,   STDOKU.BRDOK AS BRDOK, SUM(STDOKU.PORAV) AS SUMZAD, SUM(STDOKU.PORAV-STDOKU.PORAV) AS SUMRAZ " + "from STDOKU,DOKU " + "where STDOKU.CSKL='" + cSkl + "' AND " + util.getDoc("DOKU", "STDOKU") + " AND (STDOKU.PORAV<>0 OR STDOKU.VRDOK IN ('POR')) " + //STDOKU.PORAV<>0
        // AND
        // STDOKU.VRDOK
        // NOT
        // IN
        // ('POR')
        // "+
        //         "AND DOKU.DATDOK < "+dateP+" AND DOKU.DATDOK >= '"+pocGod+"' GROUP BY
        // STDOKU.BRDOK, STDOKU.VRDOK "+
        "AND DOKU.DATDOK < " + dateP + " AND DOKU.DATDOK >= '" + pocGod + "' GROUP BY STDOKU.VRDOK, STDOKU.BRDOK " + "UNION " +
        // Medjuskladisnice - ulaz
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND,  MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.ZADRAZUL) AS SUMZAD, SUM(STMESKLA.ZADRAZUL-STMESKLA.ZADRAZUL) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK in ('MEU','MES') and STMESKLA.CSKLUL='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ and STMESKLA.BRDOK=MESKLA.BRDOK and STMESKLA.VRDOK=MESKLA.VRDOK and MESKLA.GOD=STMESKLA.GOD " + "AND MESKLA.DATDOK < " + dateP + " AND MESKLA.DATDOK >= '" + pocGod + "' GROUP BY MESKLA.VRDOK, MESKLA.BRDOK, MESKLA.CSKLIZ " + "UNION " +
        // Medjuskladisnice - Poravnanje
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, 'POR' AS VRDOK, '' AS KOLND,  MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.PORAV) AS SUMZAD, SUM(STMESKLA.PORAV-STMESKLA.PORAV) AS SUMRAZ " + "from STMESKLA,MESKLA " + "where STMESKLA.VRDOK in ('MEU','MES') and STMESKLA.CSKLUL='" + cSkl + "' AND STMESKLA.PORAV<>0 " + " AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ and STMESKLA.BRDOK=MESKLA.BRDOK and STMESKLA.VRDOK=MESKLA.VRDOK and MESKLA.GOD=STMESKLA.GOD " +
        //         " AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ
        // and STMESKLA.BRDOK=MESKLA.BRDOK and STMESKLA.SKOL>0 and
        // STMESKLA.VRDOK=MESKLA.VRDOK and MESKLA.GOD=STMESKLA.GOD "+
        "AND MESKLA.DATDOK < " + dateP + " AND MESKLA.DATDOK >= '" + pocGod + "' GROUP BY MESKLA.VRDOK, MESKLA.BRDOK, MESKLA.CSKLIZ " + izlazi;

    //System.out.println(qStr);
    return qStr;
  }

  //*** Izlazi Knjige Popisa Robe ovisno o parametru
  private String izlazi(String cSkl, String dateP, String dateZ, String parametar) {
    String izl = "";
    if (parametar.equals("N")) {
      // Izlazi
      //      MAX(DOKU.CSKL)
      //      izl="UNION select ' ' as CSKLUL,' ' as CSKLIZ, MAX(0) as RBR,
      // MAX(DOKI.DATDOK) AS DATDOK, "+
      izl = "UNION select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  SUM(STDOKI.ISP-STDOKI.ISP) AS SUMZAD, SUM(STDOKI.ISP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + dokumentiN("STDOKI") + " AND DOKI.DATDOK >= " + dateP + " AND DOKI.DATDOK <=" + dateZ + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
      //GOT-ovi
          //          "select ' ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR,
          // MAX(DOKI.DATDOK) AS DATDOK, "+
          //          "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, 0.00 AS
          // SUMZAD, SUM(STDOKI.ISP) AS SUMRAZ "+
          //          "from STDOKI, DOKI "+
          //          "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+
          // " AND doki.vrdok = 'GOT' "+
          //          " AND DOKI.DATDOK >= "+dateP+" AND DOKI.DATDOK <="+dateZ+" GROUP BY
          // DOKI.DATDOK, STDOKI.BRDOK, STDOKI.VRDOK "+
          //          "UNION "+
          // Medjuskladisnice izlaz
          "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK IN ('MEI','MES') and STMESKLA.CSKLIZ='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " + dokumentiN("STMESKLA") + " AND MESKLA.DATDOK >= " + dateP + " AND MESKLA.DATDOK <=" + dateZ + " GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLIZ, MESKLA.CSKLUL, MESKLA.GOD ";
    } else {
      // Izlazi -> tu je promjena
      //      izl="UNION select MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK,
      // STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,
      // SUM(-STDOKI.UIRAB) AS SUMZAD, SUM(STDOKI.IPRODSP) AS SUMRAZ "+
      izl = "UNION select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMZAD, " + "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + dokumentiE("STDOKI") + " AND DOKI.DATDOK >= " + dateP + " AND DOKI.DATDOK <=" + dateZ + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +

      "select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IRAZ) AS SUMZAD, " + "SUM(STDOKI.IRAZ-STDOKI.IRAZ) AS SUMRAZ " + "from STDOKI, DOKI " + "where (STDOKI.IPRODSP-STDOKI.IRAZ)<>0 and STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + dokumentiE("STDOKI") + " AND DOKI.DATDOK >= " + dateP + " AND DOKI.DATDOK <=" + dateZ + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +

      // Medjuskladisnice izlaz
          "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK,  '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK IN ('MEI','MES') and STMESKLA.CSKLIZ='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " + dokumentiE("STMESKLA") + " AND MESKLA.DATDOK >= " + dateP + " AND MESKLA.DATDOK <=" + dateZ + " GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL " + "UNION " + dodaciIzlaza(cSkl, dateP, dateZ);
    }
    return izl;
  }

  private String izlazi2(String cSkl, Timestamp dateZ, String parametar) {
    System.out.println("izlazi2");
    String izl = "";
    String bitvin = Condition.between("DATDOK", ut.getFirstDayOfYear(dateZ), dateZ).toString();
    if (parametar.equals("N")) {
      izl = "UNION select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  SUM(STDOKI.IRAZ-STDOKI.IRAZ) AS SUMZAD, SUM(STDOKI.IRAZ) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + dokumentiN("STDOKI") + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
          	"select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK IN ('MEI','MES') and STMESKLA.CSKLIZ='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " + dokumentiN("STMESKLA") + " AND MESKLA.STAT_KPR='N' AND MESKLA." + bitvin + " GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLIZ, MESKLA.CSKLUL, MESKLA.GOD ";
    } else  if (parametar.equals("B")) {
        izl = "UNION select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMZAD, " + "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK NOT IN ('RAC','PRD','PON','NKU','ROT','POD','IZD','OTP','TRE','ZAH')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
              " select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IRAZ) AS SUMZAD, " + "SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.IRAZ-STDOKI.IPRODSP<>0 AND STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK NOT IN ('RAC','PRD','PON','NKU','ROT','POD','IZD','OTP','TRE','ZAH')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
  		" select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.UIRAB-STDOKI.UIRAB) AS SUMZAD, " + "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKLART='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK IN ('POS') " + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
  		" select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, -SUM(STDOKI.UIRAB) AS SUMZAD, " + "SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.UIRAB<>0 AND STDOKI.CSKLART='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK IN ('POS') " + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
      "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK,  '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK IN ('MEI','MES') and STMESKLA.CSKLIZ='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " + dokumentiE("STMESKLA") + " AND MESKLA.STAT_KPR='N' AND MESKLA." + bitvin + " GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL " + "UNION " + dodaciIzlaza2(cSkl, dateZ);
    } else  if (parametar.equals("C")) {
      DataSet uc = Aus.q("SELECT cart FROM artikli WHERE "+ hr.restart.robno.raVart.getStanjeCond().not());
      Condition ucc = Condition.in("CART", uc).qualified("stdoki");
      
      izl = "UNION select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMZAD, " + "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK NOT IN ('POS','RAC','PRD','PON','NKU','ROT','POD','IZD','OTP','TRE','ZAH')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
            " select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "'POP' AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMZAD, " + "SUM(STDOKI.IRAZ-STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.IRAZ-STDOKI.IPRODSP<>0 AND STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK NOT IN ('POS', 'RAC','PRD','PON','NKU','ROT','POD','IZD','OTP','TRE','ZAH')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
            
            " select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "'USL' AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.FMCPRP*STDOKI.KOL) AS SUMZAD, " + "SUM(STDOKI.IRAZ-STDOKI.IRAZ) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK NOT IN ('POS', 'RAC','PRD','PON','NKU','ROT','POD','IZD','OTP','TRE','ZAH')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " AND " + ucc + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
            
            "select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMZAD, " + "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKLART='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK IN ('GRN')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
            " select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "'POP' AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMZAD, " + "SUM(STDOKI.FMCPRP*STDOKI.KOL-STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.FMCPRP*STDOKI.KOL-STDOKI.IPRODSP<>0 AND STDOKI.CSKLART='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK IN ('GRN')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +

            " select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "'USL' AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.FMCPRP*STDOKI.KOL) AS SUMZAD, " + "SUM(STDOKI.IRAZ-STDOKI.IRAZ) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKLART='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK IN ('GRN')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " AND " + ucc + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
            
            " select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.UIRAB-STDOKI.UIRAB) AS SUMZAD, " + "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKLART='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK IN ('POS') " + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
      " select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IPRODSP) AS SUMZAD, " + "SUM(STDOKI.UIRAB) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.UIRAB<>0 AND STDOKI.CSKLART='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK IN ('POS') " + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
      "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK,  '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK IN ('MEI','MES') and STMESKLA.CSKLIZ='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " + dokumentiE("STMESKLA") + " AND MESKLA.STAT_KPR='N' AND MESKLA." + bitvin + " GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL " + "UNION " + dodaciIzlaza2(cSkl, dateZ);
    } else {
      izl = "UNION select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(STDOKI.IPRODSP-STDOKI.IRAZ) AS SUMZAD, " + "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK NOT IN ('POS','RAC','PRD','PON','NKU','ROT','POD','IZD','OTP','TRE','ZAH')" + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
      		" select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, -SUM(STDOKI.UIRAB) AS SUMZAD, " + "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKLART='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK IN ('POS') " + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
          "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK,  '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.VRDOK IN ('MEI','MES') and STMESKLA.CSKLIZ='" + cSkl + "' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " + " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " + dokumentiE("STMESKLA") + " AND MESKLA.STAT_KPR='N' AND MESKLA." + bitvin + " GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL " + "UNION " + dodaciIzlaza2(cSkl, dateZ);
    }
    return izl;
  }

  private String donosIzlazi(String cSkl, String dateP, String parametar) {
    String izl = "";
    int godina = hr.restart.util.Aus.getNumber(dateP.substring(1, dateP.indexOf("-")));
    String pocGod = godina + "-01-01  00:00:00.0";

    if (parametar.equals("N")) {
      // Izlazi
      izl = "UNION select '            ' as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  SUM(STDOKI.ISP-STDOKI.ISP) AS SUMZAD, SUM(STDOKI.ISP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + dokumentiN("STDOKI") + " AND DOKI.DATDOK < " + dateP + " AND DOKI.DATDOK >= '" + pocGod + "' " + dokumentiN("STDOKI") + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
      // Medjuskladisnice izlaz
          "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.CSKLIZ='" + cSkl + "' " + dokumentiN("STMESKLA") + " AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ and STMESKLA.BRDOK=MESKLA.BRDOK and STMESKLA.VRDOK=MESKLA.VRDOK and MESKLA.GOD=STMESKLA.GOD " + " AND MESKLA.DATDOK < " + dateP + " AND MESKLA.DATDOK >= '" + pocGod + "' GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL ";
    } else {
      // Izlazi
      izl = "UNION select '            ' as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, " + "STDOKI.VRDOK AS VRDOK,  '' AS KOLND, STDOKI.BRDOK AS BRDOK,  SUM(STDOKI.IPRODSP - STDOKI.ISP) AS SUMZAD, " +
      //          "SUM(STDOKI.ISP) AS SUMRAZ "+
          "SUM(STDOKI.IPRODSP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + dokumentiE("STDOKI") + " AND DOKI.DATDOK < " + dateP + " AND DOKI.DATDOK >= '" + pocGod + "' " + dokumentiN("STDOKI") + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION " +
          // Medjuskladisnice izlaz
          "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.CSKLIZ='" + cSkl + "' " + dokumentiE("STMESKLA") + " AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ and STMESKLA.BRDOK=MESKLA.BRDOK and STMESKLA.VRDOK=MESKLA.VRDOK and MESKLA.GOD=STMESKLA.GOD " + " AND MESKLA.DATDOK < " + dateP + " AND MESKLA.DATDOK >= '" + pocGod + "' GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL  UNION " + dodaciDonIzlaza(cSkl, dateP);
    }

    return izl;
  }

  //*************************************************************************************************//
  //-------------------------------------------------------------------------------------------------//
  //---------------------- D O D A C I Z A K N J I G U P O P I S A R O B E
  // ----------------------//
  //-------------------------------------------------------------------------------------------------//
  //*************************************************************************************************//

  //*** Dodavanje dokumenta 'ROT'
  private String dodaciIzlaza(String cSkl, String dateP, String dateZ) {
    String izl;
    // Izlazi
    izl = "select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  -SUM(STDOKI.ISP) AS SUMZAD, SUM(STDOKI.ISP-STDOKI.ISP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK = 'ROT'" + " AND DOKI.DATDOK >= " + dateP + " AND DOKI.DATDOK <=" + dateZ + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION ALL " +
    // Medjuskladisnice izlaz
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ)  AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.CSKLIZ='" + cSkl + "' " + " AND STMESKLA.VRDOK = 'ROT' " + " AND MESKLA.DATDOK >= " + dateP + " AND MESKLA.DATDOK <=" + dateZ + " GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL ";

    return izl;
  }

  private String dodaciIzlaza2(String cSkl, Timestamp dateZ) {
    String izl;
    String bitvin = hr.restart.baza.Condition.between("DATDOK", ut.getFirstDayOfYear(dateZ), dateZ).toString();
    // Izlazi
    izl = "select '            ' as CSKLUL,MAX(DOKI.CSKL) as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  -SUM(STDOKI.IRAZ) AS SUMZAD, SUM(STDOKI.IRAZ-STDOKI.IRAZ) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK in ('ROT','POD','IZD','OTP') AND (STDOKI.veza is null or (STDOKI.veza not like '%POS%' and STDOKI.veza not like '%GRN%')) " + " AND DOKI.STAT_KPR='N' AND DOKI." + bitvin/*
                                                                                                                                                                                                                                                                                                                                                                                                      * DATDOK
                                                                                                                                                                                                                                                                                                                                                                                                      * <="+dateZ
                                                                                                                                                                                                                                                                                                                                                                                                      */+ " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK ";
    /*+ "UNION ALL " +
    // Medjuskladisnice izlaz
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ)  AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.CSKLIZ='" + cSkl + "' " + " AND STMESKLA.VRDOK = 'ROT' " + " AND MESKLA.STAT_KPR='N' AND MESKLA." + bitvin
                                                                                                                                                                                                                                                                                                                                                                                                  * DATDOK
                                                                                                                                                                                                                                                                                                                                                                                                  * <="+dateZ
                                                                                                                                                                                                                                                                                                                                                                                                  + " GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL "*/
    
    return izl;
  }

  //*** Dodavanje dokumenta 'ROT' za donos
  private String dodaciDonIzlaza(String cSkl, String dateP) {
    int godina = hr.restart.util.Aus.getNumber(dateP.substring(1, dateP.indexOf("-")));
    String pocGod = godina + "-01-01  00:00:00.0";
    String izl;
    // Izlazi
    izl = "select '            ' as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  -SUM(STDOKI.ISP) AS SUMZAD, SUM(STDOKI.ISP-STDOKI.ISP) AS SUMRAZ " + "from STDOKI, DOKI " + "where STDOKI.CSKL='" + cSkl + "' AND " + util.getDoc("DOKI", "STDOKI") + " AND STDOKI.VRDOK = 'ROT'" + " AND DOKI.DATDOK < " + dateP + " AND DOKI.DATDOK >= '" + pocGod + "' " + dokumentiN("STDOKI") + " GROUP BY STDOKI.BRDOK, STDOKI.VRDOK " + "UNION ALL " +
    // Medjuskladisnice izlaz
        "select MAX(MESKLA.CSKLUL) as CSKLUL, MAX(MESKLA.CSKLIZ) as CSKLIZ, MAX(0) as RBR, MAX(MESKLA.DATDOK) AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, -SUM(STMESKLA.ZADRAZIZ) AS SUMZAD, SUM(STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ)  AS SUMRAZ " + "from STMESKLA, MESKLA " + "where  STMESKLA.CSKLIZ='" + cSkl + "' " + " AND STMESKLA.VRDOK = 'ROT' " + " AND MESKLA.DATDOK < " + dateP + " AND MESKLA.DATDOK >= '" + pocGod + "' GROUP BY MESKLA.BRDOK, MESKLA.VRDOK, MESKLA.CSKLUL ";

    return izl;
  }

  //*** Def dokumenata
  private String dokumentiN(String table) {
    String dok = "";
    if (table.equals("STDOKI"))
      dok = " AND STDOKI.VRDOK NOT IN ('RAC','GRN','PRD','PON','NKU','DOS','TRE','ZAH')";
    else
      dok = " AND STMESKLA.VRDOK NOT IN ('RAC','PRD','PON','NKU','GOT','TRE','ZAH')";
    return dok;
  }

  private String dokumentiE(String table) {
    String dok = "";
    if (table.equals("STDOKI")) {
      dok = " AND STDOKI.VRDOK NOT IN ('RAC','PRD','PON','NKU','ROT','TRE','ZAH')";
    } else {
      dok = " AND STMESKLA.VRDOK NOT IN ('RAC','PRD','PON','NKU','ROT','TRE','ZAH')";
    }
    return dok;
  }

  //********************************************************************************//
  //-- -- - --- - - - -- - --- - --- - --- ---- --- - - -- ---- ---//
  //- - - -- - - - K A R T I C A A R T I K L A - -- --- ---- -- -- - -//
  //-- -- - --- - --- --- - -- -- - ------ - - - - - - - -- - - - - - - -----//
  //********************************************************************************//
    
  private String dokiF(boolean naru, boolean nari){
    String dod = naru ? nari ? " and vrdok not in ('NKU','NDO')" : 
      "and vrdok!='NDO'" : nari ? "and vrdok!='NKU'" : ""; 
    String upit = "SELECT vrdok FROM Vrdokum where app in ('rac','mp','robno','sklad') and tipdok in ('F','O','SK') and vrsdok = 'I'" + dod; 
    QueryDataSet vrdoksi = ut.getNewQueryDataSet(upit);
    vrdoksi.first();
    String dokiF = "(";
    for (;;) {
      dokiF += "'" + vrdoksi.getString("VRDOK") + "'";
      if (vrdoksi.next())
        dokiF += ",";
      else {
        dokiF += ")";
        break;
      }
    }
    
   return dokiF; 
  }
  private String dokiFSK(){
    String upit = "SELECT vrdok FROM Vrdokum where app in ('rac','mp','robno','sklad') and tipdok in ('F','O') and vrsdok = 'I'";
    QueryDataSet vrdoksi = ut.getNewQueryDataSet(upit);
    vrdoksi.first();
    String dokiF = "(";
    for (;;) {
      dokiF += "'" + vrdoksi.getString("VRDOK") + "'";
      if (vrdoksi.next())
        dokiF += ",";
      else {
        dokiF += ")";
        break;
      }
    }
    
   return dokiF; 
  }

  public String getKarticaArtikla(String cart, String vrzal, String cSkl, String docu, String doci, String newDateZ) {
    return getKarticaArtikla(cart, vrzal, cSkl, docu, doci, newDateZ, false, false);
  }
  
  public String getKarticaArtikla(String cart, String vrzal, String cSkl, String docu, String doci, String newDateZ, boolean naru, boolean nari) {
    String god = newDateZ.substring(1, 5);
    String ugod = " AND DOKU.GOD='" + god + "' ";
    String igod = " AND DOKI.GOD='" + god + "' ";
    String mgod = " AND MESKLA.GOD='" + god + "' ";
    if ("N".equalsIgnoreCase(frmParam.getParam("robno", "godKartica", 
        "D", "Ogranièiti karticu artikla na jednu godinu (D,N)")))
      ugod = igod = mgod = " ";

    String qStr =
    // Ulazi
    "SELECT 'A' as ui, 'B' as SRT, DOKU.VRDOK, DOKU.BRDOK, STDOKU.RBR, DOKU.DATDOK, STDOKU.KOL AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, " + "STDOKU.ZC, STDOKU.IZAD AS KOLZAD, (STDOKU.IZAD-STDOKU.IZAD) AS KOLRAZ, doku.cskl as sklul, '            ' as skliz " + "from STDOKU,DOKU " + "where STDOKU.CART=" + cart + " AND STDOKU.CSKL='" + cSkl + "' AND " + docu + "  AND DOKU.VRDOK NOT IN ('POR','KAL') " + "AND DOKU.DATDOK <= " + newDateZ + ugod + "UNION ALL " +
    // Samo poravnanja na ulazu
        "SELECT 'A' as ui, 'B' as SRT,  DOKU.VRDOK, DOKU.BRDOK, STDOKU.RBR, DOKU.DATDOK, (STDOKU.KOL-STDOKU.KOL) AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS KOLZAD, (STDOKU.PORAV-STDOKU.PORAV) AS KOLRAZ, '            ' as sklul, '            ' as skliz " + "from STDOKU,DOKU " + "where STDOKU.CART=" + cart + " AND STDOKU.CSKL='" + cSkl + "' AND " + docu + "  AND DOKU.VRDOK='POR' AND STDOKU.SKOL>0 " + "AND DOKU.DATDOK <= " + newDateZ + ugod + "UNION ALL " +
        // Izlazi
        "SELECT 'B' as ui, 'B' as SRT, DOKI.VRDOK, DOKI.BRDOK, STDOKI.RBR, DOKI.DATDOK, (STDOKI.KOL-STDOKI.KOL) AS KOLUL, STDOKI.KOL AS KOLIZ, " + "STDOKI.ZC, (STDOKI.IRAZ-STDOKI.IRAZ) AS KOLZAD, STDOKI.IRAZ AS KOLRAZ, '            ' as sklul, doki.cskl as skliz " + "from STDOKI,DOKI " + "where STDOKI.CART=" + cart + " AND STDOKI.CSKL='" + cSkl + "' AND " + doci + igod + "AND DOKI.DATDOK <= " + newDateZ + " AND DOKI.VRDOK NOT IN " +dokiF(naru, nari)+ " UNION ALL " +
        // Poravnanje
        "SELECT 'A' as ui, 'A' as SRT, 'POR', DOKU.BRDOK, STDOKU.RBR, DOKU.DATDOK, (STDOKU.KOL-STDOKU.KOL) AS KOLUL, (STDOKU.KOL-STDOKU.KOL) AS KOLIZ, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS KOLZAD, (STDOKU.PORAV-STDOKU.PORAV) AS KOLRAZ, '            ' as sklul, '            ' as skliz " + "from STDOKU,DOKU " + "where STDOKU.CART=" + cart + " AND STDOKU.CSKL='" + cSkl + "' AND " + docu + " AND STDOKU.PORAV<>0 " + ugod + "AND DOKU.DATDOK <= " + newDateZ + " AND STDOKU.VRDOK NOT IN ('POR') " + "UNION ALL " +
        // Medjuskladisnice - ulaz // srt je bilo 'B'
        "SELECT 'A' as ui, 'A' as SRT, MESKLA.VRDOK, MESKLA.BRDOK, STMESKLA.RBR, MESKLA.DATDOK, STMESKLA.KOL AS KOLUL, " + "(STMESKLA.KOL-STMESKLA.KOL) AS KOLIZ, STMESKLA.ZCUL, STMESKLA.ZADRAZUL AS KOLZAD, (STMESKLA.ZADRAZUL-STMESKLA.ZADRAZUL) AS KOLRAZ, meskla.csklul as sklul, meskla.cskliz as skliz " + "from STMESKLA,MESKLA " + "where (MESKLA.VRDOK='MEU' OR MESKLA.VRDOK='MES') AND STMESKLA.CART=" + cart + " AND STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL  " + "AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ  AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK " + " AND MESKLA.VRDOK = STMESKLA.VRDOK AND MESKLA.DATDOK <= " + newDateZ + mgod + "UNION ALL " +
        // Medjuskladisnice - izlaz
        "SELECT 'B' as ui, 'B' as SRT, MESKLA.VRDOK, MESKLA.BRDOK, STMESKLA.RBR, MESKLA.DATDOK, (STMESKLA.KOL-STMESKLA.KOL) AS KOLUL, " + "STMESKLA.KOL AS KOLIZ, STMESKLA.ZC, (STMESKLA.ZADRAZIZ-STMESKLA.ZADRAZIZ) AS KOLZAD, STMESKLA.ZADRAZIZ AS KOLRAZ, meskla.csklul as sklul, meskla.cskliz as skliz " + "from STMESKLA,MESKLA " + "where (MESKLA.VRDOK='MEI' OR MESKLA.VRDOK='MES') AND STMESKLA.CART=" + cart + " AND STMESKLA.CSKLIZ='" + cSkl + "' AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ " + "AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK " + " AND MESKLA.VRDOK = STMESKLA.VRDOK AND MESKLA.DATDOK <= " + newDateZ + mgod + "UNION ALL " +
        // Medjuskladisnice - Poravnanje
        "SELECT 'A' as ui, 'A' as SRT, 'POR', MESKLA.BRDOK, STMESKLA.RBR, MESKLA.DATDOK, (STMESKLA.KOL-STMESKLA.KOL) as KOLUL, (STMESKLA.KOL-STMESKLA.KOL) as KOLIZ," + this.getZCPor("STMESKLA", vrzal) + " STMESKLA.PORAV AS KOLZAD, (STMESKLA.PORAV-STMESKLA.PORAV) AS KOLRAZ, '            ' as sklul, '            ' as skliz " + "from STMESKLA,MESKLA " + "where STMESKLA.CART=" + cart + " AND STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL " + "AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ  AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD " + "AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 " + "AND MESKLA.DATDOK <= " + newDateZ + mgod + " ORDER BY 6,2,1,4,5"; // bilo
    // 4,5,2,1
        System.out.println("KARTICA qStr: " + qStr);
    return qStr;
  }

  public String getSklKarticaArtikla(String cart, String vrzal, String cSkl, String docu, String doci, String newDateZ) {
    String god = newDateZ.substring(1, 5);
    String ugod = " AND DOKU.GOD='" + god + "' ";
    String igod = " AND DOKI.GOD='" + god + "' ";
    String mgod = " AND MESKLA.GOD='" + god + "' ";
    if ("N".equalsIgnoreCase(frmParam.getParam("robno", "godKartica", 
        "D", "Ogranièiti karticu artikla na jednu godinu (D,N)")))
      ugod = igod = mgod = " ";
    
    String qStr =
    // Ulazi
    "SELECT 'A' as ui, 'B' as SRT, DOKU.VRDOK, DOKU.BRDOK, STDOKU.RBR, DOKU.DATDOK, " +
    "DOKU.CORG, '            ' AS CVRTR, "+
    "STDOKU.KOL AS KOLUL, 0.000 AS KOLIZ, " + "STDOKU.ZC, STDOKU.IZAD AS KOLZAD, 0.00 AS KOLRAZ, doku.cskl as sklul, '            ' as skliz " + "from STDOKU,DOKU " + "where STDOKU.CART=" + cart + " AND STDOKU.CSKL='" + cSkl + "' AND " + docu + "  AND DOKU.VRDOK NOT IN ('POR','KAL') " + "AND DOKU.DATDOK <= " + newDateZ + ugod + "UNION ALL " +
    // Samo poravnanja na ulazu
        //"SELECT 'A' as ui, 'B' as SRT,  DOKU.VRDOK, DOKU.BRDOK, STDOKU.RBR, DOKU.DATDOK, 0.000 AS KOLUL, 0.000 AS KOLIZ, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS KOLZAD, 0.00 AS KOLRAZ, '            ' as sklul, '            ' as skliz " + "from STDOKU,DOKU " + "where STDOKU.CART=" + cart + " AND STDOKU.CSKL='" + cSkl + "' AND " + docu + "  AND DOKU.VRDOK='POR' AND STDOKU.SKOL>0 " + "AND DOKU.DATDOK <= " + newDateZ + " " + " AND DOKU.GOD='" + god + "' " + "UNION ALL " +
        // Izlazi
        "SELECT 'B' as ui, 'B' as SRT, DOKI.VRDOK, DOKI.BRDOK, STDOKI.RBR, DOKI.DATDOK, " +
        "DOKI.CORG, DOKI.CVRTR, "+
        "0.000 AS KOLUL, STDOKI.KOL AS KOLIZ, " + "STDOKI.ZC, 0.00 AS KOLZAD, STDOKI.IRAZ AS KOLRAZ, '            ' as sklul, doki.cskl as skliz " + "from STDOKI,DOKI " + "where STDOKI.CART=" + cart + " AND STDOKI.CSKL='" + cSkl + "' AND " + doci + igod + "AND DOKI.DATDOK <= " + newDateZ + " AND DOKI.VRDOK NOT IN ('RAC','GRN','PRD','PON','UZP','NKU','NDO','ZAH','TRE','TER','ODB','POS') and (stdoki.veza is null or (STDOKI.VEZA not LIKE '%DOS%')) UNION ALL " +

        //"SELECT 'B' as ui, 'B' as SRT, DOKI.VRDOK, DOKI.BRDOK, STDOKI.RBR, DOKI.DATDOK, 0.000 AS KOLUL, STDOKI.KOL AS KOLIZ, " + "STDOKI.ZC, 0.00 AS KOLZAD, STDOKI.IRAZ AS KOLRAZ, '            ' as sklul, doki.cskl as skliz " + "from STDOKI,DOKI " + "where STDOKI.CART=" + cart + " AND STDOKI.CSKL='" + cSkl + "' AND " + doci + " " + " AND DOKI.GOD='" + god + "' " + "AND DOKI.DATDOK <= " + newDateZ + " AND DOKI.VRDOK = 'DOS' and (STDOKI.VEZA not LIKE '%ROT%' or stdoki.veza is null) UNION ALL " +
        // Poravnanje
        "SELECT 'A' as ui, 'A' as SRT, 'POR', DOKU.BRDOK, STDOKU.RBR, DOKU.DATDOK, " +
        "DOKU.CORG, '            ' AS CVRTR, "+
        "0.000 AS KOLUL, 0.000 AS KOLIZ, " + this.getZCPor("STDOKU", vrzal) + " STDOKU.PORAV AS KOLZAD, 0.00 AS KOLRAZ, '            ' as sklul, '            ' as skliz " + "from STDOKU,DOKU " + "where STDOKU.CART=" + cart + " AND STDOKU.CSKL='" + cSkl + "' AND " + docu + " AND STDOKU.PORAV<>0 " + ugod + "AND DOKU.DATDOK <= " + newDateZ + " AND STDOKU.VRDOK NOT IN ('POR') " + "UNION ALL " +
        // Medjuskladisnice - ulaz // srt je bilo 'B'
        "SELECT 'A' as ui, 'A' as SRT, MESKLA.VRDOK, MESKLA.BRDOK, STMESKLA.RBR, MESKLA.DATDOK, " +
        "'            ' AS CORG, '            ' AS CVRTR, "+
        "STMESKLA.KOL AS KOLUL, " + "0.000 AS KOLIZ, STMESKLA.ZCUL, STMESKLA.ZADRAZUL AS KOLZAD, 0.00 AS KOLRAZ, meskla.csklul as sklul, meskla.cskliz as skliz " + "from STMESKLA,MESKLA " + "where (MESKLA.VRDOK='MEU' OR MESKLA.VRDOK='MES') AND STMESKLA.CART=" + cart + " AND STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL  " + "AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ  AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK " + " AND MESKLA.VRDOK = STMESKLA.VRDOK AND MESKLA.DATDOK <= " + newDateZ + mgod + "UNION ALL " +
        // Medjuskladisnice - izlaz
        "SELECT 'B' as ui, 'B' as SRT, MESKLA.VRDOK, MESKLA.BRDOK, STMESKLA.RBR, MESKLA.DATDOK, " +
        "'            ' AS CORG, '            ' AS CVRTR, "+
        "0.000 AS KOLUL, " + "STMESKLA.KOL AS KOLIZ, STMESKLA.ZC, 0.00 AS KOLZAD, STMESKLA.ZADRAZIZ AS KOLRAZ, meskla.csklul as sklul, meskla.cskliz as skliz " + "from STMESKLA,MESKLA " + "where (MESKLA.VRDOK='MEI' OR MESKLA.VRDOK='MES') AND STMESKLA.CART=" + cart + " AND STMESKLA.CSKLIZ='" + cSkl + "' AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ " + "AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK " + " AND MESKLA.VRDOK = STMESKLA.VRDOK AND MESKLA.DATDOK <= " + newDateZ + mgod + "UNION ALL " +
        // Medjuskladisnice - Poravnanje
        "SELECT 'A' as ui, 'A' as SRT, 'POR', MESKLA.BRDOK, STMESKLA.RBR, MESKLA.DATDOK, " +
        "'            ' AS CORG, '            ' AS CVRTR, "+
        "0.000 as KOLUL, 0.000 as KOLIZ," + this.getZCPor("STMESKLA", vrzal) + " STMESKLA.PORAV AS KOLZAD, 0.00 AS KOLRAZ, '            ' as sklul, '            ' as skliz " + "from STMESKLA,MESKLA " + "where STMESKLA.CART=" + cart + " AND STMESKLA.CSKLUL='" + cSkl + "' AND MESKLA.CSKLUL=STMESKLA.CSKLUL " + "AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ  AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD " + "AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 " + "AND MESKLA.DATDOK <= " + newDateZ + mgod + " ORDER BY 6,2,1,4,5"; // bilo
    // 4,5,2,1
        System.out.println("KARTICA qStr: " + qStr);
    return qStr;
  }

  //********************************************************************************//
  //  - - - --- - --- - - - - -- ---- -- - --- --
  // -------------------------------//
  //-------------- S H E M A R A B A T A I Z A V. T R O S K A
  // -------------------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  public String masterZT() {
    String qStr = "select  SHZAVTR.CSHZT AS CSHZT,  SHZAVTR.NSHZT AS NSHZT,  VSHZTR_ZTR.CZT AS CZT, VSHZTR_ZTR.PZT AS PZT, VSHZTR_ZTR.IZT AS IZT, ZAVTR.NZT AS NZT, SHZAVTR.ZTPUK AS ZTPUK, SHZAVTR.ZTIUK AS ZTIUK, VSHZTR_ZTR.RBR AS RBR" + " from SHZAVTR, VSHZTR_ZTR, ZAVTR where " + "SHZAVTR.CSHZT=VSHZTR_ZTR.CSHZT and VSHZTR_ZTR.CZT = ZAVTR.CZT";
    return qStr;
  }

  public String detailZT(String CSHZT) {
    String qStr = "select  SHZAVTR.CSHZT AS CSHZT,  SHZAVTR.NSHZT AS NSHZT,  VSHZTR_ZTR.CZT AS CZT, VSHZTR_ZTR.PZT AS PZT, VSHZTR_ZTR.IZT AS IZT, ZAVTR.NZT AS NZT, SHZAVTR.ZTPUK AS ZTPUK, SHZAVTR.ZTIUK AS ZTIUK, VSHZTR_ZTR.RBR AS RBR" + " from SHZAVTR, VSHZTR_ZTR, ZAVTR where " + "SHZAVTR.CSHZT = '" + CSHZT + "' and SHZAVTR.CSHZT=VSHZTR_ZTR.CSHZT and VSHZTR_ZTR.CZT = ZAVTR.CZT";
    return qStr;
  }

  public String masterRAB() {
    String qStr = "select  RABATI.NRAB AS NRAB, VSHRAB_RAB.CRAB AS CRAB, VSHRAB_RAB.PRAB AS PRAB, SHRAB.CSHRAB AS CSHRAB ,SHRAB.NSHRAB AS NSHRAB, VSHRAB_RAB.RABNARAB AS RABNARAB, RABATI.IRAB AS IRAB, SHRAB.UPRAB AS UPRAB from SHRAB, VSHRAB_RAB, RABATI where " + "SHRAB.CSHRAB= VSHRAB_RAB.CSHRAB and RABATI.CRAB = VSHRAB_RAB.CRAB";
    return qStr;
  }

  public String detailRAB(String CSHRAB) {
    String qStr = "select  RABATI.NRAB AS NRAB, VSHRAB_RAB.CRAB AS CRAB, VSHRAB_RAB.PRAB AS PRAB, VSHRAB_RAB.RBR AS RBR, SHRAB.CSHRAB AS CSHRAB, SHRAB.NSHRAB AS NSHRAB, VSHRAB_RAB.RABNARAB AS RABNARAB, RABATI.IRAB AS IRAB, SHRAB.UPRAB AS UPRAB from SHRAB, VSHRAB_RAB, RABATI where " + "SHRAB.CSHRAB = '" + CSHRAB + "' and SHRAB.CSHRAB= VSHRAB_RAB.CSHRAB and RABATI.CRAB = VSHRAB_RAB.CRAB";

    return qStr;
  }

  public String recMaster(String cSH, char mode) {
    String queryString = "";
    if (mode == 'R') {
      queryString = "select * from vshrab_rab where cshrab = '" + cSH + "' " + "order by rbr";
    } else if (mode == 'Z') {
      queryString = "select * from vshztr_ztr where cshzt = '" + cSH + "' " + "order by rbr";
    }
    return queryString;
  }

  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //---------------------------- I Z V J E Š T A J I
  // -------------------------------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  //-> nerealizirani
  public String getUnReal(String sVrdok, String cSkl, boolean selected, String zavDat) {
    String qStr = "";
    if (selected) {
      qStr = "select DOKI.BRDOK, DOKI.VRDOK, DOKI.CPAR, PARTNERI.NAZPAR, DOKI.CSKL , DOKI.DATDOK, DOKI.UIRAC from DOKI, PARTNERI" + " where DOKI.AKTIV='D' and DOKI.DATDOK <=" + zavDat + " and DOKI.CPAR=PARTNERI.CPAR" + sVrdok + cSkl;
    } else {
      qStr = "select DOKI.BRDOK, DOKI.VRDOK, DOKI.CPAR, PARTNERI.NAZPAR, DOKI.CSKL, DOKI.DATDOSP AS DATDOK, DOKI.UIRAC from DOKI, PARTNERI" + " where DOKI.AKTIV='D' and DOKI.DATDOSP <=" + zavDat + " and DOKI.CPAR=PARTNERI.CPAR" + sVrdok + cSkl;
    }
    return qStr;
  }

  //-> nefakturirani
  public String getUnFuck(String sSkl) {
    String qStr = "select DOKI.BRDOK, DOKI.VRDOK, DOKI.CPAR, PARTNERI.NAZPAR, DOKI.CSKL , DOKI.DATDOK, DOKI.UIRAC from DOKI, PARTNERI" + " where DOKI.AKTIV='D' and DOKI.CPAR=PARTNERI.CPAR and STATIRA='N' " + " AND DOKI.VRDOK = 'OTP'" + sSkl;

    return qStr;
  }
/*  // FUCKED OFF
  //->pregled kolicina
  public String getPregledKol(String dodatak) {
    return getPregledKol(dodatak, hr.restart.util.Util.getUtil().getYear(hr.restart.util.Util.getUtil().getFirstDayOfYear()));
  }

  public String getPregledKol(String dodatak, String god) {
    String qStr = "select artikli.cart, artikli.cart1, artikli.bc, artikli.nazart, stanje.kolrez, stanje.kol, " + " artikli.sigkol, artikli.minkol from artikli, stanje where stanje.god='" + god + "' and artikli.cart = stanje.cart and stanje.cskl = " + dodatak;
    return qStr;
  }
*/
  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //--------------------- P R I M K A _ Z A V T R O Š A K
  // ------------------------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  //  public BigDecimal getPrimka_ZT(String sCSHZT) {
  //    hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  //    String qSql = "select SUM(PZT) as SUMA from VSHZTR_ZTR where CSHZT =
  // '"+sCSHZT+"'";
  //    vl.execSQL(qSql);
  //    vl.RezSet.open();
  //    return vl.RezSet.getBigDecimal("SUMA");
  //  }

  //  public String getVTZavtrInsert(String sCSHZT) {
  //    String qStr = "select LOKK, AKTIV, RBR, CZT, PZT, IZT, ZTNAZT from
  // VSHZTR_ZTR"+
  //                  " where CSHZT = '"+sCSHZT+"'";
  //    return qStr;
  //  }

  //***Poravnanja ovisno o vrsti zalihe
  private String getZCPor(String st, String vrzal) {
    if (vrzal.equals("V"))
      return st + ".vc - " + st + ".svc as zc, ";
    else if (vrzal.equals("M"))
      return st + ".mc - " + st + ".smc as zc, ";
    else
      return "0. as zc, ";
  }

  //********************************************************************************//
  //--------------------------------------------------------------------------------//
  //-------------------------------- C J E N I K
  // -----------------------------------//
  //--------------------------------------------------------------------------------//
  //********************************************************************************//

  public String getCjenikIspis(String cart, String cskl, String param, boolean kolNull, String god, String uvjet) {
    String addCart = "";
    String addKol = "";
    String order = "";
    if (!cart.equals(""))
      addCart = " and artikli.cart =" + cart;
    if (!kolNull)
      addKol = " and stanje.kol>0";
    //    if (param.equals("CART")) order =" order by artikli.cart";
    //    else if(param.equals("CART1")) order =" order by artikli.cart1";
    //    else order =" order by artikli.bc";

    if (param.equals("NAZART"))
      order = " order by artikli." + param + " " + vl.getCollateSQL();
    else
      order = " order by artikli." + param;

    String qStr = "select '" + cskl + "' as cskl, artikli.cart as cart, artikli.cart1 as cart1, artikli.bc as bc, artikli.nazart as nazart, " + "artikli.jm as jm, artikli.cgrart as cgrart, stanje.vc as vc, stanje.mc as mc, stanje.kol as kol " + "from artikli,stanje where artikli.cart = stanje.cart and stanje.god='" + god + "' and stanje.cskl='" + cskl + "'" + addCart + addKol + " " + uvjet + order;

        System.out.println("getCjenikIspis qStr: " + qStr);

    return qStr;
  }

  public String getMarzaIspis(String cart, String cskl, String param, boolean kolNull, String god, String uvjet) {
    //    System.out.println("MArza String...");
    String addCart = "";
    String addKol = "";
    String order = "";
    if (!cart.equals(""))
      addCart = " and artikli.cart =" + cart;
    if (!kolNull)
      addKol = " and stanje.kol>0";

    if (!param.equals("RUC") && !param.equals("MARZA")) {
      if (param.equals("NAZART"))
        order = " order by artikli." + param + " " + vl.getCollateSQL();
      else
        order = " order by artikli." + param;
    } else
      order = " order by artikli.cart";

    //    if (param.equals("CART")) order =" order by artikli.cart";
    //    else if(param.equals("CART1")) order =" order by artikli.cart1";
    //    else order =" order by artikli.bc";

    String qStr = "select '" + cskl + "' as cskl, artikli.cart as cart, artikli.cart1 as cart1, artikli.bc as bc, artikli.nazart as nazart, " + "  stanje.kol as kol, stanje.nc as nc, stanje.vc as vc,(((stanje.vc-stanje.nc)/stanje.nc)*100) as marza, (stanje.vc-stanje.nc) as ruc " + "from artikli,stanje where stanje.nc > 0 and artikli.cart = stanje.cart and stanje.god='" + god + "' and stanje.cskl='" + cskl + "'" + addCart + addKol + " " + uvjet + order;

    System.out.println("getMarzaIspis qStr " + qStr);

    return qStr;
  }

  //  public String getSpecCek(String cskl, String pocDat, String zavDat, String
  // banka, String spec) {
  //    String datumAdd="";
  //    String bankaAdd="";
  //    if(!banka.equals("")) bankaAdd = " and rate.cbanka='"+banka+"'";
  //    if(spec.equals("prim")) datumAdd = " and rate.datdok >="+pocDat+" and
  // rate.datdok <="+zavDat+" order by rate.god, rate.brdok, rate.cbanka,
  // rate.datdok";
  //    else datumAdd = " and rate.datum between "+pocDat+" and "+zavDat+" order by
  // rate.god, rate.brdok, rate.cbanka, rate.datum";
  //
  //    String qStr= "select '"+cskl+"' as cskl, rate.vrdok as vrdok, rate.cnacpl
  // as nacplR, rate.god as god, rate.brdok as brdok, "+
  //                 "rate.broj_cek as sbc, rate.broj_trg as trg, rate.irata as iznos,
  // rate.datum as datnap, rate.datdok as datprim, "+
  //                 "rate.cbanka as cbanka from rate, nacpl where rate.cskl='"+cskl+"' "+
  //                 "and rate.cnacpl=nacpl.cnacpl and nacpl.fl_cek='D'"+bankaAdd+datumAdd;
  //
  //  // System.out.println("getSpecCek qStr " + qStr);
  //
  //    return qStr;
  //  }

  public String getSpecKart(String corg, String cskl, String pocDat, String zavDat, String banka, String spec) {
    String datumAdd = "";
    String bankaAdd = "";
    if (!banka.equals(""))
      bankaAdd = " and rate.cbanka = '" + banka + "'";
    if (spec.equals("prim"))
      datumAdd = " and rate.datdok between " + pocDat + " and " + zavDat + " order by rate.god, rate.brdok, rate.cbanka, rate.datdok";
    else
      datumAdd = " and rate.datum between " + pocDat + " and " + zavDat + " order by rate.god, rate.brdok, rate.cbanka, rate.datum";

    String qStr = "select '" + cskl + "' as cskl, rate.vrdok as vrdok, rate.cnacpl as nacplR, rate.god as god, rate.brdok as brdok, " + "rate.broj_cek as sbc, rate.broj_trg as trg, rate.irata as iznos, rate.datum as datnap, rate.datdok as datprim, " + "rate.cbanka as cbanka from rate, nacpl where rate.cskl in ('" + cskl + "','"+corg+"') " + "and rate.cnacpl=nacpl.cnacpl and nacpl.fl_kartica='D'" + bankaAdd + datumAdd;

    //    System.out.println("SpecKart: " + qStr);

    return qStr;
  }

  public String getIzvNacPlZbirno(String cskl, String pocDat, String zavDat, String nacPl, boolean poRacunu) {
    String addNP = "";
    String dateUvjet = "";
    if (!nacPl.equals(""))
      addNP = " and rate.cnacpl = '" + nacPl + "'";
    if (poRacunu)
      dateUvjet = " and rate.datdok between " + pocDat + " and " + zavDat;
    else
      dateUvjet = " and rate.datum between " + pocDat + " and " + zavDat;

    /*
     * String qStr= "select max('"+cskl+"') as cskl, max(rate.vrdok) as vrdok,
     * max(nacpl.naznacpl) as naznacpl, " + "max(rate.cnacpl) as nacplR,
     * max(rate.god) as god, "+ "max(rate.brdok) as brdok, sum(rate.irata) as
     * iznos, max(rate.datum) as datnap, " + "max(rate.datdok) as datprim,
     * max(rate.brrata) as brrata, " + "max(rate.cbanka) as cbanka "+ "from
     * nacpl,rate where rate.cnacpl = nacpl.cnacpl and rate.cskl='"+cskl+"'
     * "+addNP + dateUvjet+" group by rate.vrdok,rate.god,
     * rate.brdok,rate.cnacpl";
     */
    String qStr = "select max('" + cskl + "') as cskl, max(rate.vrdok) as vrdok, max(nacpl.naznacpl) as naznacpl, max(rate.cnacpl) as nacplR, max(rate.god) as god, " + "max(rate.brdok) as brdok, sum(rate.irata) as iznos, max(rate.datum) as datnap, max(rate.datdok) as datprim, max(rate.brrata) as brrata, max(0) as fl_cek, max(0) as fl_kartica, max(rate.cbanka) as cbanka " + "from nacpl,rate where rate.cnacpl = nacpl.cnacpl and rate.cskl='" + cskl + "' " + addNP + dateUvjet + " group by rate.vrdok,rate.god, rate.brdok,rate.cnacpl";

    //    System.out.println("getIzNacPlZbirno qStr : " + qStr);

    return qStr;
  }

  public String getIzvNacPlPoj(String cskl, String pocDat, String zavDat, String nacPl, boolean poRacunu) {
    String addNP = "";
    String dateUvjet = "";
    if (!nacPl.equals(""))
      addNP = " and rate.cnacpl='" + nacPl + "'";
    if (poRacunu)
      dateUvjet = " and rate.datdok between " + pocDat + " and " + zavDat;
    else
      dateUvjet = " and rate.datum between " + pocDat + " and " + zavDat;

    String qStr = "select '" + cskl + "' as cskl, rate.vrdok as vrdok, nacpl.naznacpl as naznacpl, rate.cnacpl as nacplR, rate.god as god, " + "rate.brdok as brdok, rate.irata as iznos, rate.datum as datnap, rate.datdok as datprim, rate.brrata as brrata, 0 as fl_cek, 0 as fl_kartica, rate.cbanka as cbanka " + "from nacpl,rate where rate.cnacpl = nacpl.cnacpl and rate.cskl='" + cskl + "' " + addNP + dateUvjet;

    System.out.println("getIzNacPlPoj qStr " + qStr);

    return qStr;
  }

  public String getIzArt(String cskl, String cart, String pocDat, String zavDat, String vrDok, String vrArt, String uvjet, String modul, String csklart) {
    String addVrDok = "";
    if (modul.equalsIgnoreCase("ALL")) {
      addVrDok = " and (doki.vrdok in ('ROT','GOT','POD')) ";
    } else if (modul.equalsIgnoreCase("VELE")) {
      addVrDok = " and (doki.vrdok='ROT' or (doki.vrdok='POD' and (doki.param='' or doki.param is null or doki.param like 'P%'))) ";
    } else if (modul.equalsIgnoreCase("MALO")) {
      addVrDok = " and (doki.vrdok='GOT' or (doki.vrdok='POD' and doki.param like 'K%')) ";
    } else if (modul.equalsIgnoreCase("OBRAC")) {
      addVrDok = " and (doki.vrdok in ('RAC','GRN','TER','ODB')) ";
      if (!csklart.equalsIgnoreCase("")) {
        addVrDok += "and stdoki.csklart = '" + csklart + "' ";
      }
    }

    if (!vrDok.equals("") && !vrDok.equals("X")) {
      if (vrDok.equals("POD")) {
        if (modul.equals("VELE"))
          addVrDok = " and doki.vrdok='" + vrDok + "' and (doki.param='' or doki.param is null or doki.param like 'P%') ";
        else if (modul.equals("MALO"))
          addVrDok = " and doki.vrdok='" + vrDok + "' and doki.param like 'K%' ";
      } else {
        addVrDok = " and doki.vrdok='" + vrDok + "' ";
        if (modul.equals("OBRAC")){
          addVrDok += "and stdoki.csklart = '" + csklart + "' ";
        } 
      }
    }
    String addCart = "";
    if (!cart.equals(""))
      addCart = " and stdoki.cart='" + cart + "'";
    String addVrArt = "";
    if (!vrArt.equals("") && !vrArt.equals("X"))
      addVrArt = " and artikli.vrart = '" + vrArt + "' ";

    String qStr = "SELECT max(stdoki.cskl) as cskl, max(stdoki.vrdok) as vrdok, max(stdoki.brdok) as brdok, " + 
    "max(stdoki.rbr) as rbr, max(stdoki.cart) as cart, max(stdoki.cart1) as cart1, max(stdoki.bc) as bc, " + 
    "max(artikli.nazart) as nazart, sum(stdoki.kol) as kol, sum(stdoki.inab) as inab, sum(stdoki.iraz) as izad, " +
    "sum(stdoki.iprodsp) as iprodsp " + 
    "FROM stdoki, doki, artikli WHERE stdoki.cart=artikli.cart and stdoki.cskl=doki.cskl and " +
    "stdoki.vrdok=doki.vrdok and stdoki.brdok=doki.brdok " + "and stdoki.god=doki.god " +
    getsklad(cskl,vrDok) + addVrDok + addCart + addVrArt + 
    " and doki.datdok between " + pocDat + " and " + zavDat + " " + uvjet + " group by stdoki.cart, stdoki.cskl";
    
    System.out.println("getIzArt QSTR: " + qStr);
    return qStr;
  }
  
  private String getsklad(String cskl, String vrdok){
    String knj = hr.restart.zapod.OrgStr.getKNJCORG();
    if (vrdok.equals("POS"))  return  "and doki.cskl='" + knj + "' ";
    String cskling = "";
    if (cskl.equalsIgnoreCase("")){
      cskling = "and doki.cskl in (";
      QueryDataSet sset = ut.getNewQueryDataSet("select cskl from sklad where knjig = '"+knj+"'");
      sset.first();
      do {
        cskling += "'"+sset.getString("CSKL")+"'";
        if (sset.next()){
          cskling += ",";
        } else {
          cskling += ") ";
          break;
        }
      } while (true);
      
    } else {
      cskling = "and doki.cskl='" + cskl + "' ";
    }
    return cskling;
  }

  public String getIzArt2(String cskl, String cart, String pocDat, String zavDat, String vrDok, String vrArt, String uvjet, String modul) {
    String addVrDok = "";

    if (modul.equalsIgnoreCase("VELE")) {
      addVrDok = " and (stdoki.vrdok='ROT' or (stdoki.vrdok='POD' and doki.param not like 'K%')) ";
    } else if (modul.equalsIgnoreCase("MALO")) {
      addVrDok = " and (stdoki.vrdok='GOT' or (stdoki.vrdok='POD' and doki.param like 'K%')) ";
    } else if (modul.equalsIgnoreCase("OBRAC")) {
      addVrDok = " and (stdoki.vrdok in ('RAC','GRN','TER','ODB')) ";
    }

    //    if (modul.equalsIgnoreCase("VELE")){
    //      addVrDok = " and (stdoki.vrdok in ('ROT','POD')) ";
    //    } else if (modul.equalsIgnoreCase("MALO")){
    //      addVrDok = " and (stdoki.vrdok in ('GOT','POD')) ";
    //    } else if (modul.equalsIgnoreCase("OBRAC")){
    //      addVrDok = " and (stdoki.vrdok in ('RAC','GRN','TER','ODB')) ";
    //    }

    if (!vrDok.equals("")) {
      if (vrDok.equals("POD")) {
        if (modul.equals("VELE"))
          addVrDok = " and stdoki.vrdok='" + vrDok + "' and doki.param not like 'K%' ";
        else if (modul.equals("MALO"))
          addVrDok = " and stdoki.vrdok='" + vrDok + "' and doki.param like 'K%' ";
      } else
        addVrDok = " and stdoki.vrdok='" + vrDok + "' ";
    }

    //    if(!vrDok.equals("")) addVrDok = " and stdoki.vrdok='" + vrDok + "' ";
    String addCart = "";
    if (!cart.equals(""))
      addCart = " and stdoki.cart='" + cart + "'";
    String addVrArt = "";
    if (!vrArt.equals(""))
      addVrArt = " and artikli.vrart = '" + vrArt + "' ";

    String qStr = "SELECT max(stdoki.cskl) as cskl, max(stdoki.vrdok) as vrdok, max(stdoki.brdok) as brdok, " + "max(stdoki.rbr) as rbr, max(stdoki.cart) as cart, max(stdoki.cart1) as cart1, max(stdoki.bc) as bc, " + "max(artikli.nazart) as nazart, sum(stdoki.kol) as kol, sum(stdoki.iraz) as iraz, sum(stdoki.uirab) as uirab, sum(stdoki.iprodbp) as iprodbp, " + "sum(stdoki.por1+stdoki.por2+stdoki.por3) as por, sum(stdoki.iprodsp) as iprodsp, sum(stdoki.inab) as inab, sum(stdoki.iprodbp-stdoki.inab) as ukupno " + "FROM stdoki, doki, artikli WHERE stdoki.cart=artikli.cart and stdoki.cskl=doki.cskl and stdoki.vrdok=doki.vrdok and stdoki.brdok=doki.brdok " + "and stdoki.god=doki.god and stdoki.cskl='" + cskl + "' " + addVrDok + addCart + addVrArt + " and doki.datdok between " + pocDat + " and " + zavDat + " " + uvjet + " group by stdoki.cart";
    System.out.println("getIzArt2 QSTR: " + qStr);
    return qStr;
  }

  public String getIzArt3(String cskl, String cart, String pocDat, String zavDat, String vrDok, String vrArt, String uvjet, String modul, String csklart) {
    String addVrDok = "";

    if (modul.equalsIgnoreCase("ALL")) {
      addVrDok = " and (doki.vrdok in ('ROT','GOT','POD')) ";
    } else if (modul.equalsIgnoreCase("VELE")) {
      addVrDok = " and (doki.vrdok='ROT' or (doki.vrdok='POD' and (doki.param='' or doki.param is null or doki.param like 'P%'))) ";
    } else if (modul.equalsIgnoreCase("MALO")) {
      addVrDok = " and (doki.vrdok='GOT' or (doki.vrdok='POD' and doki.param like 'K%')) ";
    } else if (modul.equalsIgnoreCase("OBRAC")) {
      addVrDok = " and (doki.vrdok in ('RAC','GRN','TER','ODB')) ";
      if (!csklart.equalsIgnoreCase("")) {
        addVrDok += "and stdoki.csklart = '" + csklart + "' ";
      }
    }
    

    //    if (modul.equalsIgnoreCase("VELE")){
    //      addVrDok = " and (stdoki.vrdok in ('ROT','POD')) ";
    //    } else if (modul.equalsIgnoreCase("MALO")){
    //      addVrDok = " and (stdoki.vrdok in ('GOT','POD')) ";
    //    } else if (modul.equalsIgnoreCase("OBRAC")){
    //      addVrDok = " and (stdoki.vrdok in ('RAC','GRN','TER','ODB')) ";
    //    }

    if (!vrDok.equals("") && !vrDok.equals("X")) {
      if (vrDok.equals("POD")) {
        if (modul.equals("VELE"))
          addVrDok = " and doki.vrdok='" + vrDok + "' and (doki.param='' or doki.param is null or doki.param like 'P%') ";
        else if (modul.equals("MALO"))
          addVrDok = " and doki.vrdok='" + vrDok + "' and doki.param like 'K%' ";
      } else {
        addVrDok = " and doki.vrdok='" + vrDok + "' ";
        if (modul.equals("OBRAC")) {
          addVrDok += "and stdoki.csklart = '" + csklart + "' ";
        }
      }
    }

    //    if(!vrDok.equals("")) addVrDok = " and stdoki.vrdok='" + vrDok + "' ";
    String addCart = "";
    if (!cart.equals(""))
      addCart = " and stdoki.cart='" + cart + "'";
    String addVrArt = "";
    if (!vrArt.equals("") && !vrArt.equals("X"))
      addVrArt = " and artikli.vrart = '" + vrArt + "' ";

    String qStr = "SELECT " + "max(stdoki.cskl) as cskl, " + "max(stdoki.vrdok) as vrdok, " + 
    "max(stdoki.brdok) as brdok, " + "max(stdoki.cart) as cart, " + "max(stdoki.cart1) as cart1, " + 
    "max(stdoki.bc) as bc, " + "max(artikli.nazart) as nazart, " + "sum(stdoki.kol) as kol, " + 
    "sum(stdoki.iraz) as iraz, " + "sum(stdoki.uirab) as uirab, " + "sum(stdoki.iprodbp) as iprodbp, " + 
    "sum(stdoki.iprodsp - stdoki.iprodbp) as por, " + "sum(stdoki.iprodsp) as iprodsp, " + 
    "sum(stdoki.inab) as inab, " + "sum(stdoki.iprodbp-stdoki.inab) as ukupno " + "FROM " + 
    "stdoki, doki, artikli " + "WHERE stdoki.cart=artikli.cart and " + "stdoki.cskl=doki.cskl " + 
    "and stdoki.vrdok=doki.vrdok " + "and stdoki.brdok=doki.brdok " + "and stdoki.god=doki.god " + 
    /*"and stdoki.cskl='"*/ getsklad(cskl,vrDok) /*+ cskl + "' "*/ + addVrDok + addCart + addVrArt + "and doki.datdok between " + pocDat + 
    " and " + zavDat + " " + uvjet + " " + "group by stdoki.cart, stdoki.cskl";

    System.out.println("getIzArt3 QSTR: " + qStr);
    return qStr;
  }

  public String getIspisCijenaPolica(String cskl, String csklIz, String vrDok, int brDok, String godina) {
    /** @todo ISPIS CIJENA ZA POLICE - broj iz KPR */
    QueryDataSet cartDS = new QueryDataSet();
    String strCart = "";
    String cartIn = "artikli.cart in(";
    String skl = "";
    String cskliz = "";
    if (vrDok.equals("MES") || vrDok.equals("MEU")) {
      skl = "csklul";
      strCart = "select distinct cart as cart, cskliz as cskliz from stmeskla where csklul='" + cskl + "' and cskliz='" + csklIz + "' and brdok=" + brDok + " and vrdok='" + vrDok + "' and god='" + godina + "'";
      cskliz = " '" + csklIz + "' as cskliz, ";
    } else {
      skl = "cskl";
      strCart = "select distinct cart as cart from stdoku where cskl='" + cskl + "' and brdok=" + brDok + " and vrdok='" + vrDok + "' and god='" + godina + "'";
    }
    cartDS.setQuery(new QueryDescriptor(dm.getDatabase1(), strCart));
    cartDS.open();
    cartDS.first();
    int i = 0;
    while (cartDS.inBounds()) {
      if (i < cartDS.getRowCount() - 1)
        cartIn = cartIn + cartDS.getInt("CART") + ",";
      else
        cartIn = cartIn + cartDS.getInt("CART") + ")";
      i++;
      cartDS.next();
    }

    if (cartDS.getRowCount() == 0) {
      //      System.out.println("rowCount je nula");
      return "";
    }
    
    if (cartDS.getRowCount() > 1000 && vrDok.equals("PST")) {
      String qStr = "select cart, cart1, bc, nazart, jm, zc, god, cskl, vrdok, brdok " + 
      "from stdoku where cskl='" + cskl + "' and god='" + godina +
      "' and vrdok='PST' and brdok="+brDok+" and kol!=0 order by cart";
      System.out.println(qStr);
      return qStr;
    }

    String qStr = "select artikli.cart as cart, artikli.cart1 as cart1, artikli.bc as bc, artikli.nazart as nazart, artikli.jm as jm, " + "stanje.zc as ZC, stanje.god as god, '" + cskl + "' as " + skl + ", " + cskliz + " '" + vrDok + "' as vrdok, " + brDok + " as brdok from artikli, stanje where artikli.cart = stanje.cart and stanje.cskl='" + cskl + "' and stanje.god='" + godina + "' and stanje.kol!=0 and " + cartIn + " order by artikli.cart";

    //    System.out.println("qstr\n\n"+qStr);

    return qStr;
  }

  public String getIspisCijenaPolica(String cskl, String nazArt) {
    String carting = "";
    if (!nazArt.equals("")) {
      carting = " AND " + nazArt;
    }

    String qStr = "select artikli.cart as cart, artikli.cart1 as cart1, artikli.bc as bc, artikli.nazart as nazart, " + "stanje.mc as mc, stanje.tkal as tkal from artikli, stanje where artikli.cart = stanje.cart and stanje.cskl='" + cskl + "' " + carting + " order by artikli.cart";
    //    System.out.println("getIspisCijenaPolica("+cskl+","+nazArt+") = " +
    // qStr);
    return qStr;
  }

  //  public String getIspisCijenaPolica(String cskl, String[] nazArt) {
  //    // Za upotrebu u frmCijenePolDva.
  //    String nazArtStr;
  //    if (nazArt.length == 1){
  //      nazArtStr = "= " + nazArt[0];
  //    } else {
  //      nazArtStr = "in ('";
  //      for (int i = 0; i < nazArt.length; i++) {
  //        if (i != nazArt.length-1){
  //          nazArtStr += nazArt[i] + "', '";
  //        } else {
  //          nazArtStr += nazArt[i] + "')";
  //        }
  //      }
  //    }
  //
  //  // System.out.println("inQuery : " + nazArtStr);
  //
  //
  //    String qStr = "select artikli.cart as cart, artikli.cart1 as cart1,
  // artikli.bc as bc, artikli.nazart as naziv, "+
  //                  "stanje.mc as cijena, stanje.tkal as tkal from artikli, stanje where
  // artikli.cart = stanje.cart and stanje.cskl='"+cskl+"' "+
  //                  "AND ARTIKLI.CART " + nazArtStr +" order by artikli.cart";
  //
  //  // System.out.println("qStr : " + qStr);
  //
  //
  //    return qStr;
  //  }

  public String getShemeStavkeDelStr(String kljuc, int i) {
    String qStr = "";
    if (i == 0) {
      qStr = "delete from vshrab_rab where cshrab='" + kljuc + "'";
    } else {
      qStr = "delete from vshztr_ztr where cshzt='" + kljuc + "'";
    }
    return qStr;
  }

  public boolean getCSKL_CORG(String cSklStr, String cOrgStr) {
//    String in = "('";
//    if (cSklStr.equals("") || cSklStr == null)
//      return false;
//    StorageDataSet tds = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(cOrgStr);
//    tds.open();
//    tds.first();
//    while (tds.inBounds()) {
//      if (tds.getRow() < tds.getRowCount() - 1)
//        in += tds.getString("CORG") + "', '";
//      else
//        in += tds.getString("CORG") + "')";
//      tds.next();
//    }

    QueryDataSet qds = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("CSKL ='" + cSklStr + 
        //"' AND CORG IN" + in
        "' AND "+Condition.in("CORG", hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(cOrgStr))
        );
    qds.open();

    if (qds.getRowCount() <= 0)
      return false;
    return true;
  }

  public String hrFormatBigDecimal(BigDecimal izn) {
    if (izn.doubleValue() == 0)
      return "0,00";
    String finalStr = "";
    int idx = 0;
    int radix = 0;
    String temp = "";
    String iznos = izn.toString();
    String iznosInt = iznos.substring(0, iznos.indexOf("."));
    String iznosDec = iznos.substring(iznos.indexOf(".") + 1, iznos.length());
    for (int i = 0; i < iznosInt.length(); i++) {
      temp = iznosInt.substring((iznosInt.length() - (i + 1)), (iznosInt.length() - (i))) + temp;
      idx++;
      if (idx == (3 + radix) && iznosInt.length() > 3) {
        temp = "." + temp;
        idx = 0;
        ++radix;
      }
    }
    finalStr = temp + "," + iznosDec;
    return finalStr;
  }
}