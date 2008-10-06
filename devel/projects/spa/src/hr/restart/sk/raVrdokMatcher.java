/****license*****************************************************************
**   file: raVrdokMatcher.java
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
package hr.restart.sk;

import java.math.BigDecimal;

import com.borland.dx.dataset.ReadRow;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raVrdokMatcher {
  private String vrdok;
  private BigDecimal id, ip;
  private boolean idp, ipp, idn, ipn, urn, irn, upl, ipl, okk, okd, any;
  private static raVrdokMatcher shared = new raVrdokMatcher();

  public raVrdokMatcher() {
  }

  public raVrdokMatcher(ReadRow stavka) {
    setStavka(stavka);
  }

  public raVrdokMatcher setStavka(ReadRow stavka) {
    try {
      vrdok = stavka.getString("VRDOK");
      id = stavka.getBigDecimal("ID");
      ip = stavka.getBigDecimal("IP");
      idn = id.signum() < 0;
      ipn = ip.signum() < 0;
      idp = id.signum() > 0;
      ipp = ip.signum() > 0;
      urn = irn = ipl = upl = okk = okd = false;
      any = urn = "URN".equalsIgnoreCase(vrdok);
      any = any || (irn = "IRN".equalsIgnoreCase(vrdok));
      any = any || (ipl = "IPL".equalsIgnoreCase(vrdok));
      any = any || (upl = "UPL".equalsIgnoreCase(vrdok));
      any = any || (okk = "OKK".equalsIgnoreCase(vrdok));
      any = any || (okd = "OKD".equalsIgnoreCase(vrdok));
//      if (!any) throw new IllegalArgumentException("Invalid vrdok "+vrdok);
    } catch (Exception e) {}
    return this;
  }

  public boolean isRacun() {
    return urn || irn || (okd && ipp) || (okk && idp);
  }

  public boolean isUplata() {
    return upl || ipl || (okd && idp) || (okk && ipp);
  }

  public boolean isKob() {
    return (okd || okk) && (ipn || idn);
  }

  public boolean isKup() {
    return irn || upl || okk;
  }

  public boolean isDob() {
    return urn || ipl || okd;
  }

  public boolean isRacunTip() {
    return urn || irn || (okd && (ipp || ipn)) || (okk && (idp || idn));
  }

  public boolean isUplataTip() {
    return upl || ipl || (okd && (idp || idn)) || (okk && (ipp || ipn));
  }

  public boolean isID() {
    return idp || idn;
  }

  public boolean isIP() {
    return ipp || ipn;
  }

  public static boolean isRacun(ReadRow stavka) {
    return shared.setStavka(stavka).isRacun();
  }

  public static boolean isUplata(ReadRow stavka) {
    return shared.setStavka(stavka).isUplata();
  }

  public static boolean isKob(ReadRow stavka) {
    return shared.setStavka(stavka).isKob();
  }

  public static boolean isKup(ReadRow stavka) {
    return shared.setStavka(stavka).isKup();
  }

  public static boolean isDob(ReadRow stavka) {
    return shared.setStavka(stavka).isDob();
  }

  public static boolean isRacunTip(ReadRow stavka) {
    return shared.setStavka(stavka).isRacunTip();
  }

  public static boolean isUplataTip(ReadRow stavka) {
    return shared.setStavka(stavka).isUplataTip();
  }

/*
  private boolean matchRU(raVrdokMatcher other) {
    return (urn && other.ipl) || (irn && other.upl);
  }

  private boolean matchRK(raVrdokMatcher other) {
    return (urn && other.okd && (other.ipn || other.idp)) ||
           (irn && other.okk && (other.idn || other.ipp));
  }

  private boolean matchUK(raVrdokMatcher other) {
    return (upl && other.okk && (other.ipn || other.idp)) ||
           (ipl && other.okd && (other.idn || other.ipp));
  }

  public boolean matches(raVrdokMatcher other) {
    return matchRU(other) || other.matchRU(this) ||
           matchRK(other) || other.matchRK(this) ||
           matchUK(other) || other.matchUK(this);
  }
  */

  public boolean matches(raVrdokMatcher other) {
    return isKup() == other.isKup() &&
           isRacunSide() != other.isRacunSide();
  }

  public boolean matches(ReadRow stavka) {
    return matches(shared.setStavka(stavka));
  }

  public String getMatchString() {
    if (urn || (okd && (idn || ipp)))
      return "(VRDOK='IPL' OR (VRDOK='OKD' AND (IP < 0 OR ID > 0)))";
    else if (irn || (okk && (ipn || idp)))
      return "(VRDOK='UPL' OR (VRDOK='OKK' AND (ID < 0 OR IP > 0)))";
    else if (upl || (okk && (idn || ipp)))
      return "(VRDOK='IRN' OR (VRDOK='OKK' AND (IP < 0 OR ID > 0)))";
    else if (ipl || (okd && (ipn || idp)))
      return "(VRDOK='URN' OR (VRDOK='OKD' AND (ID < 0 OR IP > 0)))";
    else return "VRDOK=''";
  }

  public static String getMatchString(ReadRow stavka) {
    return shared.setStavka(stavka).getMatchString();
  }

  public boolean isRacunSide() {
    return isRacun() || (okk && ipn || okd && idn);
  }

  public String getMatchSide() {
    return isRacunSide() ? "cracuna" : "cuplate";
  }

  public static String getMatchSide(ReadRow stavka) {
    return shared.setStavka(stavka).getMatchSide();
  }

  public String getVrdok() {
    return vrdok;
  }

  public String getOtherSide() {
    return isRacunSide() ? "cuplate" : "cracuna";
  }

  public static String getOtherSide(ReadRow stavka) {
    return shared.setStavka(stavka).getOtherSide();
  }
}
