/****license*****************************************************************
**   file: repUtil.java
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
import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;

public class repUtil {

  private static repUtil rU;
  private hr.restart.util.Valid val=hr.restart.util.Valid.getValid();
  private Variant myVariant = new Variant();
  private lookupData lkd=  hr.restart.util.lookupData.getlookupData();
  private DataSet ds ;
  private Variant[] filter = new Variant[] {new Variant()};
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(true);
  private repUtil() {}

  public static repUtil getrepUtil() {
    if(rU==null)
      rU=new repUtil();
    return rU;
  }
  public void setDataSet(DataSet dst){
    ds=dst;
  }

  public Variant getSomething(String[] colname,DataSet dst,String what){
    String [] colname2 = (String [])colname.clone();
    return getSomething2(colname,colname2,ds, dst, what);
  }
  public Variant getSomething(String[] colname,String[] colname2,DataSet dst,String what){
	return getSomething2(colname,colname2,ds, dst, what);
  }

//  public


  public Variant getSomething2(String[] colname,String[] colname_to_search,
                                DataSet where_to_start,DataSet dst,String what){

    where_to_start.open();
    dst.open();
    for (int i=0; i< colname.length; i++){
      where_to_start.getVariant(colname_to_search[i],filter[i]);
    }
    if (lkd.raLocate(dst,colname,filter)){
      dst.getVariant(what,myVariant);
      return myVariant;
    }
    else
      return new Variant();
  }
  public static int getBrdokSize() {
    try {
      return Integer.parseInt(frmParam.getParam("robno", "brdoksize", "6", "Koliko je velik broj dokumenta na ispisima 6=000001"));
    } catch (Exception e) {
    }
    return 6;
  }
  public static String getFormatBroj(String cskl,String vrdok,String god,int brdok){
    
    return (vrdok.trim()+"-"+cskl.trim()+"/"+god+"-"+
            hr.restart.util.Valid.getValid().maskZeroInteger(new Integer(brdok),getBrdokSize()));
  }
  
  public static String getFormatBroj(DataSet ds) {
    return getFormatBroj(ds.getString("CSKL"), ds.getString("VRDOK"),
        ds.getString("GOD"), ds.getInt("BRDOK"));
  }

  public String getFormatBroj(){
    return getFormatBroj(ds.getString("CSKL"),ds.getString("VRDOK"),
                         ds.getString("GOD"),ds.getInt("BRDOK"));
//    return (ds.getString("VRDOK").trim()+"-"+ds.getString("CSKL").trim()+"/"+ds.getString("GOD")+"-"+
//            val.maskZeroInteger(new Integer(ds.getInt("BRDOK")),6));
  }

  public String getFormatBrojME(){
    st.prn(ds);
    return ds.getString("VRDOK").trim()+"-"+ds.getString("CSKLIZ").trim()+"-"+ds.getString("CSKLUL").trim()+"/"+ds.getString("GOD")+"-"+
      val.maskZeroInteger(new Integer(ds.getInt("BRDOK")),6);
  }
  
  
  public String getFormatPerformaInvoice(){
   return  (ds.getString("CSKL").trim()+"/"+ds.getString("GOD")+"-"+hr.restart.util.Valid.getValid().maskZeroInteger(new Integer(ds.getInt("BRDOK")),6));
  }
}