/****license*****************************************************************
**   file: repNalogSK.java
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
/*
 * Created on Feb 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.gk;

import hr.restart.baza.Skstavke;
import hr.restart.db.raVariant;
import hr.restart.sisfun.frmParam;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.util.StringTokenizer;

import com.borland.dx.dataset.DataSet;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class repNalogSK extends repNalog implements raReportData {
  DataSet brdokCache, pp;
  lookupData ld = lookupData.getlookupData();
  
  
  public String getOpis() {
    return super.getOpis()+getOpisSK(super.ds);
  }
  
  

  public repNalogSK() {
    super();
    String cnaloga = jpBrojNaloga.getCNaloga(ds);
    brdokCache = Skstavke.getDataModule().getTempSet("CGKSTAVKE BROJDOK", 
        "cgkstavke like '"+cnaloga + "%'");
    brdokCache.open();
    
    pp = dm.getPartneri();
  }
  /**
   * @param set
   * @return
   */
  private String getOpisSK(DataSet set) {
    if (set.hasColumn("CPAR")==null) return "";
    int cpar = ds.getInt("CPAR");
    if (cpar == 0 || cpar == -1) return "";

    if (!ld.raLocate(pp,"CPAR",cpar+"")) return "";
    boolean op = !super.getOpis().trim().equals("");
    String brojdok = (op ? "\n" : "");
    if (set.hasColumn("BROJDOK")!=null) {
      brojdok = (op ? ", " : "") + set.getString("BROJDOK") + "\n";
    } else {
      String cgkstavke = jpBrojNaloga.getCNaloga(ds)+"-"+ds.getInt("RBS");
      if (ld.raLocate(brdokCache, "CGKSTAVKE", cgkstavke))
        brojdok = (op ? ", " : "") + brdokCache.getString("BROJDOK") + "\n";
    }
    String par = "";
    StringTokenizer elementipar = new StringTokenizer(
        frmParam.getParam("gk","colppnatem","NAZPAR","Koje kolone partnera se vide na ispisu temeljnice s partnerom (comma delimited)")
        ,",");
    while (elementipar.hasMoreTokens()) {
      String cn = elementipar.nextToken();
      if (pp.hasColumn(cn)!=null) {
        par = par + raVariant.getDataSetValue(pp,cn).toString()+" "; 
      }
    }
    return brojdok+par;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.reports.raReportData#getRow(int)
   */
  /*public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.reports.raReportData#getRowCount()
   */
  /*public int getRowCount() {
    return ds.getRowCount();
  }*/
}
