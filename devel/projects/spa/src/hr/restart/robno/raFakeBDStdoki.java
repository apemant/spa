/****license*****************************************************************
**   file: raFakeBDStdoki.java
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
import java.math.BigDecimal;

public class raFakeBDStdoki {

/// otpremnice-izdatnice-ra\u010Duni
  public BigDecimal kol;
  public BigDecimal uprab;
  public BigDecimal uirab;
  public BigDecimal upzt;
  public BigDecimal uizt;
  public BigDecimal fc;
  public BigDecimal ineto;
  public BigDecimal fvc;
  public BigDecimal iprodbp;
  public BigDecimal por1;
  public BigDecimal por2;
  public BigDecimal por3;
  public BigDecimal fmc;
  public BigDecimal fmcprp;
  public BigDecimal iprodsp;
  public BigDecimal nc;
  public BigDecimal inab;
  public BigDecimal imar;
  public BigDecimal vc;
  public BigDecimal ibp;
  public BigDecimal ipor;
  public BigDecimal mc;
  public BigDecimal isp;
  public BigDecimal zc;
  public BigDecimal iraz;
  public BigDecimal ppor1;
  public BigDecimal ppor2;
  public BigDecimal ppor3;
  public BigDecimal uipor;
  public BigDecimal uppor;

  ///// tmpPolja racuni-otpremnice

//  public BigDecimal jirab;
//  public BigDecimal jizt;
//  public BigDecimal pospor1;
//  public BigDecimal pospor2;
//  public BigDecimal pospor3;
//  public BigDecimal por1do3;


  public BigDecimal kolzal;


  ///// razno
  public String rezkol;

  public raFakeBDStdoki() {
    Init();
  }

  public void Init() {
    rezkol = "neinicijaliziran";
    resetKol();
    resetSklad();
    resetFinanc();
    resetDodPrikaz();
  }

  public void resetKol() {
    kol        = BigDecimal.valueOf(0,4);

  }
  /**
   * resetira na nulu skladišni dio izlaznog dokumenta
   */
  public void resetSklad(){

    nc         = BigDecimal.valueOf(0,2);
    inab       = BigDecimal.valueOf(0,2);
    imar       = BigDecimal.valueOf(0,2);
    vc         = BigDecimal.valueOf(0,2);
    ibp        = BigDecimal.valueOf(0,2);
    ipor       = BigDecimal.valueOf(0,2);
    mc         = BigDecimal.valueOf(0,2);
    isp        = BigDecimal.valueOf(0,2);
    zc         = BigDecimal.valueOf(0,2);
    iraz       = BigDecimal.valueOf(0,2);

  }
  /**
   * resetira na nulu financijski dio izlaznog dokumenta
   */
  public void resetFinanc(){

    uprab      = BigDecimal.valueOf(0,2);
    uirab      = BigDecimal.valueOf(0,2);
    upzt       = BigDecimal.valueOf(0,2);
    uizt       = BigDecimal.valueOf(0,2);
    fc         = BigDecimal.valueOf(0,2);
    ineto      = BigDecimal.valueOf(0,2);
    fvc        = BigDecimal.valueOf(0,2);
    iprodbp    = BigDecimal.valueOf(0,2);
    por1       = BigDecimal.valueOf(0,2);
    por2       = BigDecimal.valueOf(0,2);
    por3       = BigDecimal.valueOf(0,2);
    fmc        = BigDecimal.valueOf(0,2);
    fmcprp     = BigDecimal.valueOf(0,2);
    iprodsp    = BigDecimal.valueOf(0,2);
    ppor1      = BigDecimal.valueOf(0,2);
    ppor2      = BigDecimal.valueOf(0,2);
    ppor3      = BigDecimal.valueOf(0,2);
    uppor      = BigDecimal.valueOf(0,2);
    uipor      = BigDecimal.valueOf(0,2);

  }
  /**
   * resetira dodatne dijelove potrebne za prikaz na ekran
   */

  public void resetDodPrikaz(){
/*
    jirab      = BigDecimal.valueOf(0,2);
    jizt       = BigDecimal.valueOf(0,2);
    pospor1    = BigDecimal.valueOf(0,2);
    pospor2    = BigDecimal.valueOf(0,2);
    pospor3    = BigDecimal.valueOf(0,2);
    por1do3    = BigDecimal.valueOf(0,2);
*/
    kolzal     = BigDecimal.valueOf(0,4);
  }
  
  public String toString() {
    // e ovo sam u excelu isprogramirao :)
    return "raFakeBDStdoki:: \n "+
    "   kol =" +    kol +"\n" +
    "   uprab   =" +    uprab   +"\n" +
    "   uirab   =" +    uirab   +"\n" +
    "   upzt    =" +    upzt    +"\n" +
    "   uizt    =" +    uizt    +"\n" +
    "   fc  =" +    fc  +"\n" +
    "   ineto   =" +    ineto   +"\n" +
    "   fvc =" +    fvc +"\n" +
    "   iprodbp =" +    iprodbp +"\n" +
    "   por1    =" +    por1    +"\n" +
    "   por2    =" +    por2    +"\n" +
    "   por3    =" +    por3    +"\n" +
    "   fmc =" +    fmc +"\n" +
    "   fmcprp  =" +    fmcprp  +"\n" +
    "   iprodsp =" +    iprodsp +"\n" +
    "   nc  =" +    nc  +"\n" +
    "   inab    =" +    inab    +"\n" +
    "   imar    =" +    imar    +"\n" +
    "   vc  =" +    vc  +"\n" +
    "   ibp =" +    ibp +"\n" +
    "   ipor    =" +    ipor    +"\n" +
    "   mc  =" +    mc  +"\n" +
    "   isp =" +    isp +"\n" +
    "   zc  =" +    zc  +"\n" +
    "   iraz    =" +    iraz    +"\n" +
    "   ppor1   =" +    ppor1   +"\n" +
    "   ppor2   =" +    ppor2   +"\n" +
    "   ppor3   =" +    ppor3   +"\n" +
    "   uipor   =" +    uipor   +"\n" +
    "   uppor   =" +    uppor   +"\n";
  }
}