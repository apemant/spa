/****license*****************************************************************
**   file: doki.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class doki extends KreirDrop implements DataModule {

  private static doki inst = new doki();

  QueryDataSet zagPOS = new raDataSet();
  QueryDataSet zagGOT = new raDataSet();
  QueryDataSet zagPON = new raDataSet();
  QueryDataSet zagPONKup = new raDataSet();
  QueryDataSet zagPONOJ = new raDataSet();
  QueryDataSet zagPONPar = new raDataSet();
  QueryDataSet zagPOV = new raDataSet();
  QueryDataSet zagNAR = new raDataSet();
  QueryDataSet zagPRD = new raDataSet();
  QueryDataSet zagPRDkup = new raDataSet();
  QueryDataSet zagRAC = new raDataSet();
  QueryDataSet zagROT = new raDataSet();
  QueryDataSet zagOTP = new raDataSet();
  QueryDataSet zagDOS = new raDataSet();
  QueryDataSet zagIZD = new raDataSet();
  QueryDataSet zagPOD = new raDataSet();
  QueryDataSet zagPODKup = new raDataSet();
  QueryDataSet zagODB = new raDataSet();
  QueryDataSet zagTER = new raDataSet();
  QueryDataSet zagGRN = new raDataSet();
  QueryDataSet zagREV = new raDataSet();
  QueryDataSet zagPRE = new raDataSet();
  QueryDataSet zagNDO = new raDataSet();
  QueryDataSet zagINM = new raDataSet();
  QueryDataSet zagKON = new raDataSet();
  QueryDataSet zagZAH = new raDataSet();
  
  {
  	initClones();
  }

  public static doki getDataModule() {
    return inst;
  }

  private void initClones() {
    createFilteredDataSet(zagPOS, "1=0");
    createFilteredDataSet(zagGOT, "1=0");
    createFilteredDataSet(zagPON, "1=0");
    createFilteredDataSet(zagPONPar, "1=0");
    createFilteredDataSet(zagPONOJ, "1=0");
    createFilteredDataSet(zagPONKup, "1=0");
    createFilteredDataSet(zagPOD, "1=0");
    createFilteredDataSet(zagPODKup, "1=0");
    createFilteredDataSet(zagPOV, "1=0");
    createFilteredDataSet(zagNAR, "1=0");
    createFilteredDataSet(zagPRD, "1=0");
    createFilteredDataSet(zagPRDkup, "1=0");
    createFilteredDataSet(zagRAC, "1=0");
    createFilteredDataSet(zagROT, "1=0");
    createFilteredDataSet(zagOTP, "1=0");
    createFilteredDataSet(zagIZD, "1=0");
    createFilteredDataSet(zagPOD, "1=0");
    createFilteredDataSet(zagTER, "1=0");
    createFilteredDataSet(zagODB, "1=0");
    createFilteredDataSet(zagGRN, "1=0");
    createFilteredDataSet(zagREV, "1=0");
    createFilteredDataSet(zagPRE, "1=0");
    createFilteredDataSet(zagNDO, "1=0");
    createFilteredDataSet(zagINM, "1=0");
    createFilteredDataSet(zagDOS, "1=0");
    createFilteredDataSet(zagKON, "1=0");
    createFilteredDataSet(zagZAH, "1=0");
  }


  public com.borland.dx.sql.dataset.QueryDataSet getZagPos() {
    return zagPOS;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagInm() {
    return zagINM;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagGot() {
    return zagGOT;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPon() {
    return zagPON;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPonKup() {
    return zagPONKup;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPonOJ() {
    return zagPONOJ;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPonPar() {
    return zagPONPar;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPov() {
    return zagPOV;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagNar() {
    return zagNAR;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPrd() {
    return zagPRD;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getZagPrdKup() {
    return zagPRDkup;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRac() {
    return zagRAC;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRot() {
    return zagROT;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagOtp() {
    return zagOTP;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagDos() {
    return zagDOS;
  }
  

  public com.borland.dx.sql.dataset.QueryDataSet getZagIzd() {
    return zagIZD;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagOdb() {
    return zagODB;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagTer() {
    return zagTER;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPod() {
    return zagPOD;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPodKup() {
    return zagPODKup;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getZagGrn() {
    return zagGRN;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRev() {
    return zagREV;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPre() {
    return zagPRE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagNdo() {
    return zagNDO;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagKon() {
	    return zagKON;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagZah() {
     return zagKON;
  }
}
