/****license*****************************************************************
**   file: stdoki.java
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
package hr.restart.baza;

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

public class stdoki extends KreirDrop implements DataModule {

  private static stdoki inst = new stdoki();
  
  QueryDataSet stGOT = new QueryDataSet();
  QueryDataSet stPON = new QueryDataSet();
  QueryDataSet stPONKup = new QueryDataSet();
  QueryDataSet stPONOJ = new QueryDataSet();
  QueryDataSet stPOV = new QueryDataSet();
  QueryDataSet stNAR = new QueryDataSet();
  QueryDataSet stPRD = new QueryDataSet();
  QueryDataSet stPRDkup = new QueryDataSet();
  QueryDataSet stRAC = new QueryDataSet();
  QueryDataSet stROT = new QueryDataSet();
  QueryDataSet stOTP = new QueryDataSet();
  QueryDataSet stDOS = new QueryDataSet();
  QueryDataSet stIZD = new QueryDataSet();
  QueryDataSet stPOS = new QueryDataSet();
  QueryDataSet stTER = new QueryDataSet();
  QueryDataSet stODB = new QueryDataSet();
  QueryDataSet stPOD = new QueryDataSet();
  QueryDataSet stPODKup = new QueryDataSet();
  QueryDataSet stRNLp = new QueryDataSet();
  QueryDataSet stRNLs = new QueryDataSet();
  QueryDataSet stGRN = new QueryDataSet();
  QueryDataSet stREV = new QueryDataSet();
  QueryDataSet stPRE = new QueryDataSet();
  QueryDataSet stNDO = new QueryDataSet();
  QueryDataSet stINM = new QueryDataSet();
  QueryDataSet stKON = new QueryDataSet();
  QueryDataSet stZAH = new QueryDataSet();

  {
  	initClones();
  }
  
  public static stdoki getDataModule() {
    return inst;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getStGot() {
    return stGOT;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPon() {
    return stPON;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPonKup() {
    return stPONKup;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPonOJ() {
    return stPONOJ;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPov() {
    return stPOV;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStNar() {
    return stNAR;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPrd() {
    return stPRD;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStPrdKup() {
    return stPRDkup;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRac() {
    return stRAC;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRot() {
    return stROT;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStOtp() {
    return stOTP;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStDos() {
    return stDOS;
  }  

  public com.borland.dx.sql.dataset.QueryDataSet getStIzd() {
    return stIZD;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPos() {
    return stPOS;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPod() {
    return stPOD;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStPodKup() {
    return stPODKup;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getStTer() {
    return stTER;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStOdb() {
    return stODB;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRnlPro() {
    return stRNLp;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRnlSer() {
    return stRNLs;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStGrn() {
    return stGRN;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRev() {
    return stREV;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPre() {
    return stPRE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStNdo() {
    return stNDO;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStInm() {
    return stINM;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStKon() {
    return stKON;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStZah() {
    return stZAH;
  }

  
  protected void modifyColumn(Column c) {
    if (c.getColumnName().equals("FC") || c.getColumnName().equals("FVC") ||
        c.getColumnName().equals("FMC") || c.getColumnName().equals("FMCPRP")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "cijenaDec", 
          "2", "Broj decimala za cijenu na izlazu (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
    if (c.getColumnName().equals("NC") || c.getColumnName().equals("ZC")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "skladDec", 
          "2", "Broj decimala za skladišne cijene (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
  }

  private void initClones() {
    createFilteredDataSet(stGOT, "1=0");
    createFilteredDataSet(stPON, "1=0");
    createFilteredDataSet(stPONKup, "1=0");
    createFilteredDataSet(stPONOJ, "1=0");
    createFilteredDataSet(stNAR, "1=0");
    createFilteredDataSet(stPRD, "1=0");
    createFilteredDataSet(stPRDkup, "1=0");
    createFilteredDataSet(stRAC, "1=0");
    createFilteredDataSet(stROT, "1=0");
    createFilteredDataSet(stOTP, "1=0");
    createFilteredDataSet(stIZD, "1=0");
    createFilteredDataSet(stPOS, "1=0");
    createFilteredDataSet(stPOD, "1=0");
    createFilteredDataSet(stPODKup, "1=0");
    createFilteredDataSet(stTER, "1=0");
    createFilteredDataSet(stODB, "1=0");
    createFilteredDataSet(stRNLp, "1=0");
    createFilteredDataSet(stRNLs, "1=0");
    createFilteredDataSet(stGRN, "1=0");
    createFilteredDataSet(stREV, "1=0");
    createFilteredDataSet(stPRE, "1=0");
    createFilteredDataSet(stNDO, "1=0");
    createFilteredDataSet(stPOV, "1=0");
    createFilteredDataSet(stINM, "1=0");
    createFilteredDataSet(stDOS, "1=0");
    createFilteredDataSet(stKON, "1=0");
    createFilteredDataSet(stZAH, "1=0");
  }
}
