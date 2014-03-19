/****license*****************************************************************
**   file: raCalcPorez.java
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
package hr.restart.pl;

import hr.restart.util.Aus;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class raCalcPorez {
  //util
//  Util ut = Util.getUtil();
  private static Logger log = Logger.getLogger(raCalcPorez.class);
  //ulaz
  private BigDecimal ulaz;
  private BigDecimal[] stope;
  private BigDecimal oldosn;
  private BigDecimal[] oldpor;
  private BigDecimal[] limits;
  private BigDecimal stprir;
  //rezultat
  private BigDecimal[] res_por;
  private BigDecimal[] res_osn;
  private BigDecimal res_prir;
  private BigDecimal izlaz;
  //temp
  private BigDecimal nula = Aus.zero2;
  private BigDecimal jedan = new BigDecimal("1.00");
  public raCalcPorez() {
  }
  
  /**
   * Inicijalizira calculator. Obavezno pozvati prije calc()
   * ili calcBack()
   * @param _ulaz Porezna osnovica za calc(), Neto prije kredita za calcBack
   * @param _stope Stope poreza
   * @param _oldosn ako se zove calc(): setOldOsn(suma_poreznih_osnovica_u_tom_mjesecu);
   * ako se zove calcBack(): setOldOsn(suma_neta_u_tom_mjesecu);
   * @param _oldpor iznosi obracunatih poreza u ranijim isplatama
   * za calc() _oldpor.length() mora biti jednako _stope.length()
   * za calcBack() oldpor[5] = lodbitak jer nemre bit vise od 5 poreza, oldpor[6] = minpl (1500)
   * @param _stprir stopa prireza
   * @param _limits osnovice za poreze npr. 3000,6750,21000
   */  
  public void init(BigDecimal _ulaz, BigDecimal[] _stope,BigDecimal _oldosn, BigDecimal[] _oldpor, BigDecimal _stprir, BigDecimal[] _limits) {
    if (log.isDebugEnabled()) {
      log.debug("ulaz = "+_ulaz);
      debug("stope",_stope);
      log.debug(_oldosn);
      debug("oldpor",_oldpor);
      log.debug(_stprir);
      debug("limits",_limits);
    }
    setUlaz(_ulaz);
    setStope(_stope);
    setOldosn(_oldosn);
    setOldpor(_oldpor);
    setLimits(_limits);
    setStprir(_stprir);
  }


  /**
   * @param objarr
   */
  private void debug(String name, Object[] objarr) {
    log.debug(name+"-----------------------");
    if (objarr==null) {
      log.debug("objarr is null");
      return;
    }
    for (int i = 0; i < objarr.length; i++) {
      log.debug(objarr[i]);
    }
    log.debug("------------------------");
  }

  public void calc() {
    if (!validate()) return;
    BigDecimal porosn = ulaz.add(getOldosn());
    BigDecimal lim = null;
    res_por = new BigDecimal[stope.length];
    res_osn = new BigDecimal[stope.length];
    Arrays.fill(res_por,nula);
    Arrays.fill(res_osn,nula);
    for (int i = 0; i < stope.length; i++) {
      if (i==0) {
        lim = limits[i];
      } else {
        lim = limits[i].add(limits[i-1].negate());
        if (lim.compareTo(nula) < 0) lim = nula;
      }
      if (lim.compareTo(nula) == 0) {
        res_por[i] = porosn.multiply(stope[i]);
        res_osn[i] = porosn;
        break;
      } else if (porosn.compareTo(lim) >= 0) {
        porosn = porosn.add(lim.negate());
        res_por[i] = lim.multiply(stope[i]);
        res_osn[i] = lim;
      } else if (porosn.compareTo(nula) >= 0) {
        res_por[i] = porosn.multiply(stope[i]);
        res_osn[i] = porosn;
        break;
      }
    }
    izlaz = ulaz;
    for (int i = 0; i < res_por.length; i++) {
      res_por[i] = res_por[i].add(oldpor[i].negate());
      res_por[i] = res_por[i].setScale(2,java.math.BigDecimal.ROUND_HALF_UP);
      izlaz = izlaz.add(res_por[i].negate());
    }
    //prirez
    res_prir = ulaz.add(izlaz.negate()).multiply(getStprir());
    izlaz = izlaz.add(getRes_prir().negate());//PAZI!! totalna nebuloza na izlazu
  }
//  sysoutTEST ST = new sysoutTEST(false);
  public void calcBack() {
    /**@todo: Isprobati i uvaliti u raObracunPL 
     * PAZI napuni oldpor[5] i oldpor[6]
     */
    //ulaz = netopk
    //izlaz = porosn
    //oldosn = stari neto-i
    if (!validate()) return;
 //varijable 
    BigDecimal lodbitak = getOldpor()[5]; //lodbitak u oldpor[5] jer nemre bit vise od 5 poreza (
    BigDecimal neto2 = getUlaz(); //ulaz je netopk
    BigDecimal lpostprirez = getStprir();
    
    BigDecimal l15 = getStope()[0];
    BigDecimal l20 = getStope()[1];
    BigDecimal l35 = getStope()[2];
    BigDecimal l45 = getStope()[3];

    BigDecimal lporminpl = getOldpor()[6]; //dm.getParametripl().getBigDecimal("MINPL");
    BigDecimal lporminpl_2 = getLimits()[0]; //dm.getParametripl().getBigDecimal("OSNPOR1");
    BigDecimal lporminpl_5 = getLimits()[1]; //dm.getParametripl().getBigDecimal("OSNPOR2");
    BigDecimal lporminpl_45 = getLimits()[2]; //dm.getParametripl().getBigDecimal("OSNPOR3");
    
// pejstano iz raObracunPL
    BigDecimal losn15 = nula.setScale(8);
    BigDecimal losn25 = nula.setScale(8);
    BigDecimal losn35 = nula.setScale(8);
    BigDecimal losn45 = nula.setScale(8);
    BigDecimal losnovica = nula;
    
    //BigDecimal jedan = Aus.one0.setScale(8);
    BigDecimal neto2_lodbitak = neto2.add(lodbitak.negate()).setScale(8);
    BigDecimal revl15_prirez = jedan.add(
    l15.multiply(jedan.add(lpostprirez)).negate()
    );
    if (neto2_lodbitak.divide(revl15_prirez,BigDecimal.ROUND_HALF_UP).compareTo(lporminpl_2) <= 0) {
      log.debug("if 1");
      if (neto2_lodbitak.divide(revl15_prirez,BigDecimal.ROUND_HALF_UP).compareTo(nula) <= 0) {
        log.debug("if 1.1");
        losnovica = nula;
      } else {
        log.debug("if 1.2");
        losn15 = neto2_lodbitak.divide(revl15_prirez,BigDecimal.ROUND_HALF_UP);
      }
    } else {
      log.debug("el 1");
      if (neto2_lodbitak.add(
      lporminpl_2.multiply(
      l15.add(l20.negate())
      ).multiply(
      jedan.add(lpostprirez)))
      .divide(
      jedan.add(l20.negate()
      .multiply(jedan.add(lpostprirez))),BigDecimal.ROUND_HALF_UP)
      .compareTo(lporminpl_5) <= 0) {
        log.debug("el 1 if 1");
        losnovica =
        neto2.add(lodbitak.negate()).add(
        lporminpl_2
        .multiply(l15.add(l20.negate()))
        .multiply(jedan.add(lpostprirez)))
        .divide(
        jedan.add(
        l20.multiply(jedan.add(lpostprirez)).negate()),BigDecimal.ROUND_HALF_UP);
        
        losn15 = lporminpl_2;
        losn25 = losnovica.add(losn15.negate());
      } else {
        log.debug("---- 35 ili 45% ");
        log.debug("el 1 if 1 el");
////        lporminpl.multiply(l15.multiply(new BigDecimal("2.00")).add(l20.multiply(new BigDecimal("2.50"))).add(l35.multiply(new BigDecimal("4.50")).negate()))
//        losnovica = neto2.add(lodbitak.negate())
//            .add(lporminpl.multiply(l15.multiply(new BigDecimal("1.00")).add(l20.multiply(new BigDecimal("3.00"))).add(l35.multiply(new BigDecimal("5.00")).negate()))
//        .multiply(jedan.add(lpostprirez))
//        ).divide(jedan.add(l35.multiply(jedan.add(lpostprirez)).negate())
//        ,BigDecimal.ROUND_HALF_UP);
        losnovica = neto2.subtract(lodbitak)
            .add(lporminpl.multiply(
                             new BigDecimal("1.00").add(lpostprirez)
                           ).multiply(
                               l15.add(l20.multiply(new BigDecimal("3.00"))).subtract(l35.multiply(new BigDecimal("4.00")))
                             )
                ).divide(new BigDecimal("1.00").subtract(l35).subtract(l35.multiply(lpostprirez)), BigDecimal.ROUND_HALF_UP);
        log.debug("losnovica = "+losnovica);
        losn15=lporminpl_2;
        losn25=lporminpl_5.add(lporminpl_2.negate());
        losn35=losnovica.add(losn15.negate()).add(losn25.negate());
        if (losn35.compareTo(lporminpl_45.add(lporminpl_5.negate())) > 0) {//9.5*1500=14250 HASTA LA ... 45
          log.debug("TRUE "+losn35+".compareTo("+lporminpl_45.add(lporminpl_5.negate())+")>0" );
          losn35 = lporminpl_45.add(lporminpl_5.negate());
          BigDecimal dva_x_l15 = new BigDecimal("2.00").multiply(l15);
//          BigDecimal dvaipol_x_l20 = new BigDecimal("2.50").multiply(l20);
          BigDecimal dvaipol_x_l20 = new BigDecimal("3.00").multiply(l20);
//          BigDecimal devetipol_x_l35 = new BigDecimal("9.50").multiply(l35);
          BigDecimal devetipol_x_l35 = new BigDecimal("9.00").multiply(l35);
          BigDecimal minuscetrnaest_x_l45 = new BigDecimal("14.00").multiply(l45).negate();
          losnovica = neto2_lodbitak.add(lporminpl.multiply(
          dva_x_l15.add(dvaipol_x_l20).add(devetipol_x_l35).add(minuscetrnaest_x_l45)
          ) //lporminpl.multiply(
          .multiply(jedan.add(lpostprirez)))
          .divide(
          jedan.add(l45.multiply(jedan.add(lpostprirez)).negate())
          ,BigDecimal.ROUND_HALF_UP) //lporminpl.multiply.multiply.divide(
          ; //neto2_lodbitak.add(
          log.debug("losnovica za 45 = "+losnovica);
          losn45 = losnovica.add(losn15.negate()).add(losn25.negate()).add(losn35.negate());
        }
      }
    }
		if (log.isDebugEnabled()) {
		  log.debug("neto2(ulaz) = "+neto2);
		  log.debug("stope : "+l15+", "+l20+", "+l35+", "+l45);
		  log.debug("15 = "+losn15);
		  log.debug("25 = "+losn25);
		  log.debug("35 = "+losn35);
		  log.debug("45 = "+losn45);
		}
    BigDecimal _osnpor = losn15.add(losn25).add(losn35).add(losn45).setScale(2,BigDecimal.ROUND_HALF_UP);
    if (_osnpor.compareTo(nula) == 0) {
      izlaz = neto2;
    } else {
      izlaz = _osnpor.add(lodbitak);
    }
    if (log.isDebugEnabled()) {
      log.debug("izlaz = "+izlaz);
    }
  }
  public boolean validate() {
    return (getOldosn()!=null && getOldpor()!=null && getStope()!=null && getUlaz()!=null && getLimits()!=null && getStprir() != null);
  }
  //Geteri i irski seteri
  public BigDecimal getOldosn() {
    return oldosn;
  }
  public BigDecimal[] getOldpor() {
    return oldpor;
  }
  public BigDecimal[] getStope() {
    return stope;
  }
  public BigDecimal getUlaz() {
    return ulaz;
  }
  public BigDecimal[] getLimits() {
    return limits;
  }
  public BigDecimal getStprir() {
    return stprir;
  }

  public BigDecimal[] getRes_por() {
    return res_por;
  }
  public BigDecimal[] getRes_osn() {
    return res_osn;
  }

  public BigDecimal getRes_prir() {
    return res_prir;
  }
  public BigDecimal getIzlaz() {
    return izlaz;
  }

  /**
   * ako se zove calc(): setOldOsn(suma_poreznih_osnovica_u_tom_mjesecu);
   * ako se zove calcBack(): setOldOsn(suma_neta_u_tom_mjesecu);
   */
  public void setOldosn(BigDecimal _oldosn) {
    if (_oldosn == null) _oldosn = Aus.zero2;
    oldosn = _oldosn;
  }
  /**
   * _oldpor.length() mora biti jednako _stope.length()
   */
  public void setOldpor(BigDecimal[] _oldpor) {
    if (_oldpor == null) _oldpor = new BigDecimal[] {Aus.zero2};
    if (getStope() != null) {
      Object[] upar = upari(_oldpor,getStope());
      oldpor = (BigDecimal[])upar[0];
      stope = (BigDecimal[])upar[1];
    } else {
      oldpor = _oldpor;
    }
  }
  public void setStope(BigDecimal[] _stope) {
    if (_stope == null) _stope = new BigDecimal[] {Aus.zero2};
    if (getOldpor() != null) {
      Object[] upar = upari(getOldpor(),_stope);
      stope = (BigDecimal[])upar[0];
      //setOldpor((BigDecimal[])upar[1]);
      oldpor = (BigDecimal[])upar[1];
    } else {
      stope = _stope;
    }
  }

  public void setStprir(BigDecimal _stprir) {
    if (_stprir == null) _stprir = nula;
    _stprir = _stprir.setScale(4,java.math.BigDecimal.ROUND_HALF_UP);
    stprir = _stprir;
  }

  public void setLimits(BigDecimal[] _limits) {
    if (_limits == null) _limits = new BigDecimal[] {Aus.zero2};
    if (getStope() != null) {
      Object[] upar = upari(_limits,getStope());
      limits = (BigDecimal[])upar[0];
      stope = (BigDecimal[])upar[1];
    } else {
      limits = _limits;
    }
  }
  private Object[] upari(BigDecimal[] first, BigDecimal[] sec) {
    if (sec.length > first.length) {
      BigDecimal[] newfirst = new BigDecimal[sec.length];
      Arrays.fill(newfirst,Aus.zero2);
      for (int i=0;i<first.length;i++) newfirst[i] = first[i];
      return new Object[] {newfirst,sec};
    } else if (sec.length < first.length) {
      BigDecimal[] newsec = new BigDecimal[first.length];
      Arrays.fill(newsec,Aus.zero2);
      for (int i=0;i<sec.length;i++) newsec[i] = sec[i];
      return new Object[] {first,newsec};
    }
    return new Object[] {first,sec};
  }
  public void setUlaz(BigDecimal _ulaz) {
    if (_ulaz == null) _ulaz = Aus.zero2;
    ulaz = _ulaz;
  }
  /**
   * Ako je neto1/(1-sumstopa) > maxosn onda je bruto = neto1+maxosn*stopa
   * i tako za sve stope
   * @param neto1 bruto - doprinosi
   * @param stope stope doprinosa u formatu /100 (eg. 0.2)
   * @param maxosn max osnovice :-> maxosn.length = stope.length
   * @return bruto
   */
  public static BigDecimal neto1ToBruto(BigDecimal neto1, BigDecimal[] stope, BigDecimal[] maxosn) {
    /**@todo: prebaciti i implementirati u raCalcPorez */
    //sumastopa
    BigDecimal sumstopa = Aus.zero2;
    for (int i=0; i<stope.length; i++) sumstopa = sumstopa.add(stope[i]);
    //calc bto
    BigDecimal btomax = neto1.divide(new BigDecimal("1.00").add(sumstopa.negate()), BigDecimal.ROUND_HALF_UP);
    BigDecimal bto = neto1;
    boolean used_maxosn = false;
    for (int i=0; i<stope.length; i++) {
      if (log.isDebugEnabled()) {
        log.debug("stopa = "+stope[i]);
        log.debug("maxosn = "+maxosn[i]);
        log.debug("bto = "+bto);
        log.debug("bto.compareTo(maxosn[i]) "+btomax.compareTo(maxosn[i]));
        log.debug("maxosn[i].compareTo(new BigDecimal(\"0.00\")) "+maxosn[i].compareTo(Aus.zero2));        
      }
      if (maxosn[i].compareTo(Aus.zero2) > 0 && btomax.compareTo(maxosn[i]) > 0) {
        //mozda prelazi osnovicu
        //bto = bto.add(neto1.multiply(stope[i]).negate()).add(maxosn[i].multiply(stope[i]));
        bto = bto.add(maxosn[i].multiply(stope[i]));
        if (log.isDebugEnabled()) {
          log.debug("sad bto = "+bto);
        }
        used_maxosn = true;
      } else {
        //b=(n+mxd)/(1-s)
        //bto = bto.add(neto1.multiply(stope[i]).negate())
        //      .divide(new BigDecimal("1.00").add(stope[i].negate()),BigDecimal.ROUND_HALF_UP);
        if (used_maxosn) {
          bto = bto.divide(new BigDecimal("1.00").add(stope[i].negate()),BigDecimal.ROUND_HALF_UP);
        }
        //System.out.println("else bto = "+bto);
      }
    }
    if (!used_maxosn) {
      bto = btomax;
      //System.out.println("nije koristena maximalna osnovica, bto = "+bto);
    }
    return bto.setScale(2,BigDecimal.ROUND_HALF_UP);
  }
  
  public static void main(String[] args) {
    hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
    raCalcPorez cpor = new raCalcPorez();
//    BigDecimal ulaz = new BigDecimal("28000.00");
    BigDecimal ulaz = new BigDecimal("16760.50");
    BigDecimal[] stope = new BigDecimal[] {new BigDecimal("0.15"),new BigDecimal("0.25"),new BigDecimal("0.35"),new BigDecimal("0.45")};
//    BigDecimal[] stope = new BigDecimal[] {new BigDecimal(0.35)};
    BigDecimal[] limits = new BigDecimal[] {new BigDecimal(3000),new BigDecimal(6750),new BigDecimal(21000)};
//    BigDecimal[] limits = null;
    BigDecimal oldosn = new BigDecimal("10000.00");
    BigDecimal[] oldpor = new BigDecimal[] {new BigDecimal(450),new BigDecimal(937.5),new BigDecimal(1137.50),new BigDecimal(0)};
    BigDecimal stprir = new BigDecimal(0.18);
    System.out.println("init...");
    cpor.init(ulaz,stope,null,null,stprir,limits);
//    cpor.init(ulaz,stope,oldosn,oldpor,stprir,limits);

    System.out.println("ulaz");
    ST.prn(cpor.getUlaz());
/*    System.out.println("stope");
    ST.prn(cpor.getStope());
    System.out.println("oldosn");
    ST.prn(cpor.getOldosn());
    System.out.println("oldpor");
    ST.prn(cpor.getOldpor());
    System.out.println("limits");
    ST.prn(cpor.getLimits());
*/
//    cpor.calc();
System.out.println("calcBack");
cpor.calcBack();
    System.out.println("res_por");
    ST.prn(cpor.getRes_por());

    System.out.println("res_prir");
    ST.prn(cpor.getRes_prir());

    System.out.println("izlaz");
    ST.prn(cpor.getIzlaz());
  }
}