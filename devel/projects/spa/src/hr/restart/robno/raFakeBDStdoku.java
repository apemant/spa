/****license*****************************************************************
**   file: raFakeBDStdoku.java
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
import hr.restart.util.Aus;

import java.math.BigDecimal;

public class raFakeBDStdoku {


  public BigDecimal kol ;//+
  public BigDecimal dc;//+
  public BigDecimal dc_val;//+
  public BigDecimal idob;
  public BigDecimal idob_val; //+
  public BigDecimal prab; //
  public BigDecimal irab; //
  public BigDecimal pzt; //
  public BigDecimal izt; //
  public BigDecimal nc; //
  public BigDecimal pmar; //
  public BigDecimal mar; //
  public BigDecimal vc; //
  public BigDecimal por1; //
  public BigDecimal por2; //
  public BigDecimal por3; //
  public BigDecimal mc; //
  public BigDecimal inab; //
  public BigDecimal imar; //
  public BigDecimal ibp; //
  public BigDecimal ipor; //
  public BigDecimal isp; //
  public BigDecimal zc; //
  public BigDecimal izad; //
  public BigDecimal kolflh; //
  public BigDecimal skol; //
  public BigDecimal svc; //
  public BigDecimal smc; //
  public BigDecimal diopormar;
  public BigDecimal dioporpor;
  public BigDecimal porav;
  
  public raFakeBDStdoku() {
    Init();
  }
  public void Init(){

    kol        = Aus.zero3;//+
    dc         = Aus.zero2;//+
    dc_val     = Aus.zero2;//+
    idob       = Aus.zero2;
    idob_val   = Aus.zero2; //+
    prab       = Aus.zero2; //
    irab       = Aus.zero2; //
    pzt        = Aus.zero2; //
    izt        = Aus.zero2; //
    nc         = Aus.zero2; //
    pmar       = Aus.zero2; //
    mar        = Aus.zero2; //
    vc         = Aus.zero2; //
    por1       = Aus.zero2; //
    por2       = Aus.zero2; //
    por3       = Aus.zero2; //
    mc         = Aus.zero2; //
    inab       = Aus.zero2; //
    imar       = Aus.zero2; //
    ibp        = Aus.zero2; //
    ipor       = Aus.zero2; //
    isp        = Aus.zero2; //
    zc         = Aus.zero2; //
    izad       = Aus.zero2; //
    kolflh     = Aus.zero3; //
    skol       = Aus.zero3; //
    svc        = Aus.zero2; //
    smc        = Aus.zero2; //
    diopormar  = Aus.zero2;
    dioporpor  = Aus.zero2;
    porav      = Aus.zero2;
  }

}