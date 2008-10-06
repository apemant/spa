/****license*****************************************************************
**   file: raFakeStavke.java
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

public class raFakeStavke {

/// otpremnice-izdatnice-ra\u010Duni
  public double kol        = 0;
  public double uprab      = 0;
  public double uirab      = 0;
  public double upzt       = 0;
  public double uizt       = 0;
  public double fc         = 0;
  public double ineto      = 0;
  public double fvc        = 0;
  public double iprodbp    = 0;
  public double por1       = 0;
  public double por2       = 0;
  public double por3       = 0;
  public double fmc        = 0;
  public double iprodsp    = 0;
  public double nc         = 0;
  public double inab       = 0;
  public double imar       = 0;
  public double vc         = 0;
  public double ibp        = 0;
  public double ipor       = 0;
  public double mc         = 0;
  public double isp        = 0;
  public double zc         = 0;
  public double iraz       = 0;
  ///// tmpPolja racuni-otpremnice
  public double jirab      = 0;
  public double jizt       = 0;
  public double pospor1    = 0;
  public double pospor2    = 0;
  public double pospor3    = 0;
  public double por1do3    = 0;
  public double ppor1      = 0;
  public double ppor2      = 0;
  public double ppor3      = 0;
  public double kolzal     = 0;
  ///// razno
  public String rezkol;

  public raFakeStavke() {}
  public void Init() {
    rezkol = "neinicijaliziran";
    resetKol();
    resetSklad();
    resetFinanc();
    resetDodPrikaz();
  }

  public void resetKol() {
      kol        = 0;
  }
  /**
   * resetira na nulu skladišni dio izlaznog dokumenta
   */
  public void resetSklad(){
    nc         = 0;
    inab       = 0;
    imar       = 0;
    vc         = 0;
    ibp        = 0;
    ipor       = 0;
    mc         = 0;
    isp        = 0;
    zc         = 0;
    iraz       = 0;
  }
  /**
   * resetira na nulu financijski dio izlaznog dokumenta
   */
  public void resetFinanc(){

    uprab      = 0;
    uirab      = 0;
    upzt       = 0;
    uizt       = 0;
    fc         = 0;
    ineto      = 0;
    fvc        = 0;
    iprodbp    = 0;
    por1       = 0;
    por2       = 0;
    por3       = 0;
    fmc        = 0;
    iprodsp    = 0;

  }
  /**
   * resetira dodatne dijelove potrebne za prikaz na ekran
   */

  public void resetDodPrikaz(){
    jirab      = 0;
    jizt       = 0;
    pospor1    = 0;
    pospor2    = 0;
    pospor3    = 0;
    por1do3    = 0;
    ppor1      = 0;
    ppor2      = 0;
    ppor3      = 0;
    kolzal     = 0;
  }
}