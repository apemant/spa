/****license*****************************************************************
**   file: raFakeBDStmeskla.java
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

public class raFakeBDStmeskla {

  public BigDecimal kol;
  public BigDecimal nc;
  public BigDecimal vc;
  public BigDecimal mc;
  public BigDecimal zc;
  public BigDecimal zcul;
  public BigDecimal skol;
  public BigDecimal snc;
  public BigDecimal svc;
  public BigDecimal smc;
  public BigDecimal inabiz;
  public BigDecimal inabul;
  public BigDecimal imariz;
  public BigDecimal imarul;
  public BigDecimal pmar;
  public BigDecimal iporiz;
  public BigDecimal iporul;
  public BigDecimal porav;
  public BigDecimal diopormar;
  public BigDecimal dioporpor;
  public BigDecimal zadraziz;
  public BigDecimal zadrazul;
  public BigDecimal reverzpostopor;
  public BigDecimal postopor;

  public raFakeBDStmeskla() {
    Init();
  }

  public void Init(){

    ponistavanjeUlaza();
    ponistavanjeIzlaza();
    kol        = Aus.zero3;
    nc         = Aus.zero2;
    vc         = Aus.zero2;
    mc         = Aus.zero2;
    zc         = Aus.zero2;
    pmar       = Aus.zero2;
    reverzpostopor=Aus.zero2;

  }
  public void ponistavanjeUlaza(){

    skol       = Aus.zero3;
    snc        = Aus.zero2;
    svc        = Aus.zero2;
    smc        = Aus.zero2;
    zcul       = Aus.zero2;
    inabul     = Aus.zero2;
    imarul     = Aus.zero2;
    iporul     = Aus.zero2;
    porav      = Aus.zero2;
    diopormar  = Aus.zero2;
    dioporpor  = Aus.zero2;
    zadrazul   = Aus.zero2;
  }
  public void ponistavanjeIzlaza(){

    inabiz     = Aus.zero2;
    imariz     = Aus.zero2;
    iporiz     = Aus.zero2;
    zadraziz   = Aus.zero2;
  }
}
