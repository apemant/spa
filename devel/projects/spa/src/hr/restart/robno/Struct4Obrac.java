/****license*****************************************************************
**   file: Struct4Obrac.java
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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Struct4Obrac {

    int cart = 0;
    BigDecimal kol = Aus.zero3;
    BigDecimal vri = Aus.zero2;
    BigDecimal kolsum = Aus.zero3;
    BigDecimal vrisum =  Aus.zero2;
    BigDecimal kolsumIN = Aus.zero3;
    BigDecimal vrisumIN =  Aus.zero2;
    BigDecimal kolsumOUT = Aus.zero3;
    BigDecimal vrisumOUT =  Aus.zero2;
    BigDecimal zc = new BigDecimal("0.0000");
    BigDecimal nc = new BigDecimal("0.0000");
    BigDecimal vc = new BigDecimal("0.0000");
    BigDecimal mc = new BigDecimal("0.0000");
    boolean forObrac = false;
    String status = "";
    boolean greska = false;

    public Struct4Obrac(int cart,BigDecimal kol, BigDecimal vri,
                        BigDecimal nc, BigDecimal vc, BigDecimal mc,boolean forObrac){
      this.kol = this.kol.add(kol);
      this.vri = this.vri.add(vri);
      this.cart = cart;
      kolsum = kol;
      vrisum = vri;
      if (forObrac) {
        kolsumOUT = kol;
        vrisumOUT = vri;
      } else {
        kolsumIN = kol;
        vrisumIN = vri;
      }
      this.forObrac = forObrac;
      if (kolsum.doubleValue()!=0) {
        zc = vrisum.divide(kolsum,4,BigDecimal.ROUND_HALF_UP);
      }
      if (!forObrac){
        this.nc = nc;
        this.vc = vc;
        this.mc = mc;
      }
    }
    private void sumasumarum(){
      kolsum = kolsumIN.subtract(kolsumOUT);
      vrisum = vrisumIN.subtract(vrisumOUT);
    }
    public void addduplicate(Struct4Obrac s4o){
      kolsumOUT =  kolsumOUT.add(s4o.kolsumOUT);
      vrisumOUT =  vrisumOUT.add(s4o.vrisumOUT);
      kolsumIN  =  kolsumIN.add(s4o.kolsumIN);
      vrisumIN  =  vrisumIN.add(s4o.vrisumIN);
      nc = s4o.nc;
      vc = s4o.vc;
      mc = s4o.mc;
      sumasumarum();
    }

    public void add(Struct4Obrac s4o){
      if (cart == s4o.cart) {
        if (forObrac) {
          kolsumIN = s4o.kolsumIN;
          vrisumIN = s4o.vrisumIN;
          kolsumOUT = kolsumOUT.add(s4o.kolsumOUT);
          vrisumOUT = vrisumOUT.add(s4o.vrisumOUT);
          nc = s4o.nc;
          vc = s4o.vc;
          mc = s4o.mc;
          zc = s4o.zc;
          vri = kol.multiply(zc);
        }
        else {
          kolsumIN = kolsumIN.add(s4o.kolsumIN);
          vrisumIN = vrisumIN.add(s4o.vrisumIN);
          kolsumOUT = s4o.kolsumOUT;
          vrisumOUT = s4o.vrisumOUT;
          if (kolsumIN.doubleValue()!=0) {
            zc = vrisumIN.divide(kolsumIN,4,BigDecimal.ROUND_HALF_UP);
          }
        }
        sumasumarum();
      }
      else {
//System.out.println("nije ušao u petlju this.cart="+cart+ " -- s4o.cart = "+ s4o.cart);
      }
    }

    public String toString() {

      manipString mp = manipString.getmanipString();

      return (mp.getString(String.valueOf(cart))+" -o- "+
              mp.getString(kol.toString(),true)+" -o- "+
              mp.getString(vri.toString(),true)+" -o- "+
              mp.getString(kolsumIN.toString(),true)+" -o- "+
              mp.getString(vrisumIN.toString(),true)+" -o- "+
              mp.getString(kolsumOUT.toString(),true)+" -o- "+
              mp.getString(vrisumOUT.toString(),true)+" -o- "+
              mp.getString(kolsum.toString(),true)+" -o- "+
              mp.getString(vrisum.toString(),true)+" -o- "+
              mp.getString(zc.toString(),true)+ " -0- "+
              mp.getString(String.valueOf(forObrac)));
    }
  }



