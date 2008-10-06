/****license*****************************************************************
**   file: raFakeBDStanje.java
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

public class raFakeBDStanje {

  public String sVrSklad ="";
  public BigDecimal kolps;
  public BigDecimal kolul;
  public BigDecimal koliz;
  public BigDecimal kolrez;
  public BigDecimal nabps;
  public BigDecimal marps;
  public BigDecimal porps;
  public BigDecimal vps;
  public BigDecimal nabul;
  public BigDecimal marul;
  public BigDecimal porul;
  public BigDecimal vul;
  public BigDecimal nabiz;
  public BigDecimal mariz;
  public BigDecimal poriz;
  public BigDecimal viz;
  public BigDecimal kol;
  public BigDecimal zc;
  public BigDecimal vri;
  public BigDecimal nc;
  public BigDecimal vc;
  public BigDecimal mc;
///// zlu ne trebalo
  public BigDecimal nabuk;
  public BigDecimal maruk;
  public BigDecimal poruk;
  public BigDecimal porav;
  public BigDecimal diopormar;
  public BigDecimal dioporpor;
  public BigDecimal inv_posto_por;
  public BigDecimal kolsklad;
  public BigDecimal kolskladul;
  public BigDecimal kolskladiz;
  
  public boolean AKTIV = false ;

  public raFakeBDStanje() {
    Init();
  }

  public void Init(){

    kolps    = Aus.zero3;
    kolul    = Aus.zero3;
    koliz    = Aus.zero3;
    kolrez   = Aus.zero3;
    nabps    = Aus.zero2;
    marps    = Aus.zero2;
    porps    = Aus.zero2;
    vps      = Aus.zero2;
    nabul    = Aus.zero2;
    marul    = Aus.zero2;
    porul    = Aus.zero2;
    vul      = Aus.zero2;
    nabiz    = Aus.zero2;
    mariz    = Aus.zero2;
    poriz    = Aus.zero2;
    viz      = Aus.zero2;
    kol      = Aus.zero3;
    zc       = Aus.zero2;
    vri      = Aus.zero2;
    nc       = Aus.zero2;
    vc       = Aus.zero2;
    mc       = Aus.zero2;
    nabuk    = Aus.zero2;
    maruk    = Aus.zero2;
    poruk    = Aus.zero2;
    porav    = Aus.zero2;
    diopormar = Aus.zero2;
    dioporpor = Aus.zero2;
    inv_posto_por = Aus.zero2;
    kolsklad    = Aus.zero3;
    kolskladul    = Aus.zero3;
    kolskladiz     = Aus.zero3;
  }
}