/****license*****************************************************************
**   file: raPrenosVT.java
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
import hr.restart.util.VarStr;
import hr.restart.util.raTransaction;
import hr.restart.util.reports.raStringCache;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raPrenosVT {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private QueryDataSet qdsSRC = new QueryDataSet();
  private QueryDataSet qdsDEST = new QueryDataSet();
  private hr.restart.util.lookupData lkd=  hr.restart.util.lookupData.getlookupData();
  private boolean bgreska=true;

  final PreparedStatement p1 = raTransaction.getPreparedStatement
                              ("DELETE FROM VTprijenos WHERE keysrc = ?");
  final PreparedStatement p2 = raTransaction.getPreparedStatement
                              ("DELETE FROM VTprijenos WHERE keydest = ?");

   final PreparedStatement p3 = raTransaction.getPreparedStatement
                              ("DELETE FROM VTText WHERE ckey = ?");

  public boolean DeleteVTText(String ckey){

    boolean returnValue = true;
    try {
      p3.setString(1,ckey);
      p3.execute();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      returnValue = false;
    }
    return returnValue;
  }

  public raPrenosVT() {}

  public DataSet getVezniDataSetSRC(){
    return qdsSRC;
  }
  public DataSet getVezniDataSetDEST(){
    return qdsDEST;
  }

  public void setGreska(boolean greska){
    bgreska=greska;
  }

  public boolean InsertLink(DataSet ds,String keysrc,String valueKeySrc,
                            String keydest,String valueKeyDest){
//System.out.println(keysrc+" "+valueKeySrc);
//System.out.println(keydest+" "+valueKeyDest);

    boolean returnValue = false;
    ds.open();
    try {
      ds.enableDataSetEvents(false);
      ds.insertRow(true);
      ds.setString(keysrc,valueKeySrc);
      ds.setString(keydest,valueKeyDest);
      raTransaction.saveChanges((QueryDataSet) ds);
      ds.enableDataSetEvents(true);
      returnValue = true;
    }
    catch (Exception ex) {
      if(bgreska) ex.printStackTrace();
      returnValue = false;
    }
    return returnValue;
  }

  public boolean DeleteLink(DataSet ds) {
    boolean returnValue = ds == null;
    if (returnValue) {
      ds.open();
      returnValue = !ds.isEmpty();
      if (returnValue) {
        try {
          ds.enableDataSetEvents(false);
          ds.first();
          ds.deleteAllRows();
          hr.restart.util.raTransaction.saveChanges((QueryDataSet)ds);
          ds.enableDataSetEvents(true);
          returnValue = true;
        }
        catch (Exception ex) {
          if(bgreska) ex.printStackTrace();
          returnValue = false;
        }
      }
    }
    return returnValue;
  }

  public QueryDataSet getOtherDocUpdated(String key) {
    QueryDataSet other = null;
    if (key.indexOf("-RNL-") > 0) {
      other = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT * FROM RN WHERE "+demakeKey(key,"rn"),true);
      System.out.println(other.getQuery().getQueryString());
      for (other.first();other.inBounds();other.next()){
        other.setString("STATUS", "O");
        other.setString("CFAKTURE", "");
      }
    } else {
      other = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT * FROM doki WHERE "+demakeKey(key,"doki"),true);
      for (other.first();other.inBounds();other.next()){
        other.setString("AKTIV", "D");
      }
    }
    return other;
  }

  public boolean DeleteLink(String value){
System.out.println("DeleteLink(String value) value="+value);
    boolean returnValue = true;
    QueryDataSet srcquery = hr.restart.util.Util.getNewQueryDataSet(
            "SELECT * FROM VTprijenos WHERE keysrc = '"+value+"'",true);
    QueryDataSet destquery = hr.restart.util.Util.getNewQueryDataSet(
            "SELECT * FROM VTprijenos WHERE keydest = '"+value+"'",true);
    ArrayList ls = new ArrayList();
    try {
      for (srcquery.first();srcquery.inBounds();srcquery.next())
        ls.add(getOtherDocUpdated(srcquery.getString("keydest")));

      for (destquery.first();destquery.inBounds();destquery.next())
        ls.add(getOtherDocUpdated(destquery.getString("keysrc")));

      for (int i = 0;i<ls.size();i++) {
       raTransaction.saveChanges((QueryDataSet) ls.get(i));
       System.out.println("brojac "+i);
      }

      srcquery.deleteAllRows();
      raTransaction.saveChanges(srcquery);
      destquery.deleteAllRows();
      raTransaction.saveChanges(destquery);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      returnValue = false;
    }
    return returnValue;

  }

  public boolean DeleteLink(String keysrc,String keydest){

    boolean returnValue = true;
    try {
      p1.setString(1,keysrc);
      p1.execute();
      p2.setString(1,keydest);
      p2.execute();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      returnValue = false;
    }
    return returnValue;
  }

  /**
   * @deprecated koristiti makeKey(tablename,cskl,vrdok,god,brdok)
   * */
  public static String makeKey(String cskl,String vrdok,String god,int brdok){
    return cskl.concat("-").concat(vrdok).concat("-").concat(god).concat("-").
                concat(String.valueOf((int) brdok));
  }

  public static String makeKey
      (String tablename,String cskl,String vrdok,String god,int brdok){

     return raControlDocs.UniversalKeyToSqlKey
         (tablename,new String[] {"CSKL","VRDOK","GOD","BRDOK"},
          new String[] {cskl,vrdok,god,String.valueOf(brdok)});
  }

  public static HashMap demakeKey(String key){
    HashMap hm = new HashMap();
    StringTokenizer token = new java.util.StringTokenizer(key,"-");
    hm.put("CSKL",token.nextToken());
    hm.put("VRDOK",token.nextToken());
    hm.put("GOD",token.nextToken());
    hm.put("BRDOK",token.nextToken());
    return hm;
  }

  public static String formatforispis(String key){

    HashMap hm = demakeKey(key);
    return repUtil.getFormatBroj((String)hm.get("CSKL"),(String)hm.get("VRDOK"),(String)hm.get("GOD"),
                                 new Integer((String) hm.get("BRDOK")).intValue());

  }


  public String demakeKey(String key,String tablica){

    StringTokenizer token = new java.util.StringTokenizer(key,"-");

    VarStr varstr= new VarStr("");

    varstr=varstr.append(tablica).append(".cskl='").append(token.nextToken()).append("' and ").
           append(tablica).append(".vrdok='").append(token.nextToken()).append("' and ").
           append(tablica).append(".god='").append(token.nextToken()).append("' and ").
           append(tablica).append(".brdok=").append(token.nextToken());

    return varstr.toString();

  }

  public String[] key2arrey(String key){
    java.util.ArrayList al = new java.util.ArrayList(4);
    char [] keychars = key.toCharArray();
    String [] forreturn;
    al.clear();
    int startidx =0;
    int endidx=0;
    for (int i =0; i<keychars.length;i++){
      if (keychars[i] =='-') {
        endidx=i;
        al.add(key.substring(startidx,endidx));
        startidx = i+1;
      }
    }

    al.add(key.substring(startidx));
    forreturn = new String[al.size()];
    for (int i =0; i<forreturn.length;i++){
      forreturn[i] = (String) al.get(i);
    }
    return forreturn;
  }


  public boolean freeSrcDataSet(DataSet Set4free,String[] field_name){

    qdsSRC.open();
    boolean returnValue = qdsSRC.isEmpty();
    if (returnValue){
      try {
        qdsSRC.first();
        do{
          lkd.raLocate(Set4free, field_name,key2arrey(qdsSRC.getString("KEYSRC")));
          Set4free.setString("ACTIV","D");
          hr.restart.util.raTransaction.saveChanges((QueryDataSet)Set4free);
        } while (qdsSRC.next());
      }
      catch (Exception ex) {
        if(bgreska) ex.printStackTrace();
        returnValue = false;
      }
    }
    return returnValue;
  }
  
  public static String getPrenosText(DataSet ds){
    return getPrenosText(ds, null);
  }

  public static String getPrenosText(DataSet ds, raStringCache cache){
  	String ulaz = getPrenosText(ds, true, cache);
  	String izlaz = getPrenosText(ds, false, cache);
  	VarStr var = new VarStr();  	
  	VarStr vartmp = new VarStr();
  	VarStr var2 = new VarStr();  	
  	if (!ulaz.equalsIgnoreCase("")){
  		var.append(ulaz).chopRight(1);
  		if (!izlaz.equalsIgnoreCase("")){
  		vartmp= new VarStr(izlaz);
  		vartmp = vartmp.chopRight(1);
  		var.append(",").append(vartmp.chopLeft(1));
  		}
  		var.append(")");
  	} else if (!izlaz.equalsIgnoreCase("")){
  		var.append(izlaz);	
  	} else return "";
  	return var.toString();
  }

  
  //******
  public static String getPrenosTextRnlIzd(DataSet ds, raStringCache cache){
  	String izdatnice;
  	if (ds.getString("CRADNAL")!= null){
      String cached = cache.getValue("RNLIZD",ds.getString("CRADNAL"));
      if (cached != null) {
        izdatnice = cached;
      } else {
        String ret = getPrenosTextRnlIzd(ds.getString("CRADNAL"));
        izdatnice = cache.returnValue(ret);
      }
    }
  	else izdatnice = "";
  	
  	return izdatnice;
  	
//  	VarStr var = new VarStr();  	
//  	VarStr vartmp = new VarStr();
//  	if (!izdatnice.equals("")){
//  		var.append(izdatnice).chopRight(1);
//  		if (!izdatnice.equals("")){
//  		vartmp= new VarStr(izdatnice);
//  		vartmp = vartmp.chopRight(1);
//  		var.append(",").append(vartmp.chopLeft(1));
//  		}
//  		var.append(")");
//  	} else return "";
//  	return var.toString();
  }
  
  private static String getPrenosTextRnlIzd(String cradnal){
    QueryDataSet srcquery = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT CSKL, VRDOK, GOD, BRDOK FROM doki WHERE vrdok = 'IZD' and cradnal = '"+cradnal+"'",true);
    
    VarStr var = new VarStr();
    
	for (srcquery.first(); srcquery.inBounds(); srcquery.next()) {
		var = var.append(repUtil.getFormatBroj(srcquery.getString("CSKL"),srcquery.getString("VRDOK"),
		    srcquery.getString("GOD"), srcquery.getInt("BRDOK"))).append(",");
	};

    if (var.length()>0){
      var=new VarStr("(").append(var);
      var=var.chopRight(1);
      var.append(")");
    }

    return var.toString();
  }
  //*******
  
  
  public static String getPrenosText(DataSet ds,boolean ulaz){
    return getPrenosText(ds, ulaz, null);
  }
  
  public static String getPrenosText(DataSet ds,boolean ulaz, raStringCache cache){
 

    if (ds.hasColumn("CSKL")!= null && ds.hasColumn("VRDOK")!= null &&
        ds.hasColumn("BRDOK")!= null && ds.hasColumn("GOD")!= null){
      if (cache != null) {
        String cached = cache.getValue(ulaz ? "raPrenosVT-ULAZ" : "raPrenosVT-IZLAZ",
          ds.getString("CSKL") + ds.getString("VRDOK") + ds.getString("GOD") + ds.getInt("BRDOK"));
        if (cached != null) return cached;
      }
      String ret = getPrenosText(makeKey("doki",ds.getString("CSKL"),ds.getString("VRDOK"),ds.getString("GOD"),ds.getInt("BRDOK")), ulaz);
      return (cache == null ? ret : cache.returnValue(ret));
    }
    return "";
    
  }
  
  public static String getPrenosText(String value, boolean ulaz){
  	QueryDataSet srcquery;
  	if (ulaz) { 
  		srcquery = hr.restart.util.Util.getNewQueryDataSet(
            "SELECT * FROM VTprijenos WHERE KEYDEST = '"+value+"'",true);
  	}
  	else {
  		srcquery = hr.restart.util.Util.getNewQueryDataSet(
  	            "SELECT * FROM VTprijenos WHERE KEYSRC = '"+value+"'",true);
  	}  		
  		
    VarStr var = new VarStr();
    if (ulaz) {
			for (srcquery.first(); srcquery.inBounds(); srcquery.next()) {
				var = var.append(formatforispis(srcquery.getString("keysrc")))
						.append(",");
			}
			;
    } else {
			for (srcquery.first(); srcquery.inBounds(); srcquery.next()) {
				var = var.append(formatforispis(srcquery.getString("keydest")))
						.append(",");
			};

    }

    if (var.length()>0){
      var=new VarStr("(").append(var);
      var=var.chopRight(1);
      var.append(")");
    }

    return var.toString();
  }

}
