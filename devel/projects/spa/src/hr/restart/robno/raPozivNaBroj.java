/****license*****************************************************************
**   file: raPozivNaBroj.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raPozivNaBroj {

  static raPozivNaBroj rPNB;
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.lookupData ld =hr.restart.util.lookupData.getlookupData();
  private hr.restart.util.Valid val =hr.restart.util.Valid.getValid();
  private TypeDoc td = TypeDoc.getTypeDoc();
  
  
  private raPozivNaBroj() {
    frmParam.getParam("robno", "pnbBrWid", "0",
      "Minimalna širina broja dokumenta u pozivu na broj (vodeæe nule)");
  }

  public static raPozivNaBroj getraPozivNaBrojClass(){
    if (rPNB == null) rPNB = new raPozivNaBroj();
    return rPNB;
  }

  public String getPozivNaBrojRestArtType(DataSet ds) {
    String brWid = frmParam.getParam("robno", "pnbBrWid", "0",
        "Minimalna širina broja dokumenta u pozivu na broj (vodeæe nule)");
    int wid = Aus.isDigit(brWid) ? Integer.parseInt(brWid) : 0;
    if (wid > 6) wid = 0;
    
    VarStr forReturn=new VarStr("");
    if (td.isDocFinanc(ds.getString("VRDOK")) || ds.getString("VRDOK").equalsIgnoreCase("IZD") ) {
      if (ld.raLocate(dm.getVrdokum(),new String[]{"VRDOK"},new String[] {ds.getString("VRDOK")})) {
        forReturn.append(dm.getVrdokum().getString("ID"));
//        forReturn.append("-");
        forReturn.append(ds.getString("CSKL"));
        forReturn.append("-");
        forReturn.append(ds.getString("GOD"));
        forReturn.append("-");
        forReturn.append(ds.getInt("BRDOK"));
        if (wid > 1) {
          int zeroes = wid - forReturn.length() + forReturn.lastIndexOf('-') + 1;
          if (zeroes > 0)
            forReturn.insert(forReturn.lastIndexOf('-') + 1, Aus.string(zeroes, '0'));
        }
        return forReturn.toString();
      }
    }
    return "";
  }

  /**
   * Za sve organizacijske jedinice radi poziv na broj formata OJ-xxx gdje je
   * xxx brdok ili OJxxxx ako je xxxx>999 osim za oj koja je zadana u parametru
   * pnb4corg a za njega je uvijek 000xxx (znaèi 6 slovni brojcani string s vodecim nulama)
   *
   * @param ds mora biti instanca doki ili doku
   * @return
   */

  public String getPozivNaBroj(DataSet ds) {
    String specForm = frmParam.getParam("robno", "specForm", "",
      "Custom format broja izlaznog dokumenta na ispisu");
    
    if (specForm != null && specForm.length() > 0 && !specForm.equalsIgnoreCase("pnbz2"))
      return Aus.formatBroj(ds, specForm);

    if (hr.restart.sisfun.frmParam.getParam
        ("robno","pnbRestArt","N","Poziv na broj RestArt formata").equalsIgnoreCase("D")){
        return getPozivNaBrojRestArtType(ds);
    } else {
    int row = -1;
    if (dm.getSklad().isOpen()) row = dm.getSklad().getRow();
    if (td.isDocFinanc(ds.getString("VRDOK"))) {
      if (td.isDocSklad(ds.getString("VRDOK"))) {
        if (ld.raLocate(dm.getSklad(),new String[]{"CSKL"},new String[] {ds.getString("CSKL")})) {
          if (hr.restart.sisfun.frmParam.getParam("robno","pnb4corg","","Poziv na broj bez OJ u sebi").equalsIgnoreCase(
              dm.getSklad().getString("CORG"))){
            return val.maskZeroInteger(new Integer(ds.getInt("BRDOK")),5);
          }
          if (ds.getInt("BRDOK")<1000) {
            return (dm.getSklad().getString("CORG")+"-"+val.maskZeroInteger(new Integer(ds.getInt("BRDOK")),2));
          } else {
            return (dm.getSklad().getString("CORG")+val.maskZeroInteger(new Integer(ds.getInt("BRDOK")),3));
          }
        } else {
          return "";//String.valueOf(ds.getInt("BRDOK"));
        }
      } else {
        if (ds.getInt("BRDOK")<1000) {
          return (ds.getString("CSKL")+"-"+val.maskZeroInteger(new Integer(ds.getInt("BRDOK")),2));
        } else {
          return (ds.getString("CSKL")+val.maskZeroInteger(new Integer(ds.getInt("BRDOK")),3));
        }
      }
    }
    if (row > -1) dm.getSklad().goToRow(row);
    return "";
    }
  }

  private String getZiro(String corg) {

  	
  	
    int row = -1;
   if (dm.getOrgstruktura().isOpen()) row = dm.getOrgstruktura().getRow();
    if (ld.raLocate(dm.getOrgstruktura(),new String[] {"CORG"},new String[] {corg})) {
      String pripad = dm.getOrgstruktura().getString("PRIPADNOST");
      if (!dm.getOrgstruktura().getString("ZIRO").equalsIgnoreCase("")) {
          return dm.getOrgstruktura().getString("ZIRO");
      } else {
        if (ld.raLocate(dm.getOrgstruktura(),new String[] {"CORG"},new String[] {pripad})) {
          return dm.getOrgstruktura().getString("ZIRO");
        } else {
          return "";
        }
      }
    }
    if (row > -1) dm.getOrgstruktura().goToRow(row);
    return "";
  }

  public String getZiroRN(DataSet ds) {
    int row = -1;
    if (dm.getSklad().isOpen()) row = dm.getSklad().getRow();
    if (td.isDocOJ(ds.getString("VRDOK"))){
      return getZiro(ds.getString("CSKL"));
    } else {
       if (ld.raLocate(dm.getSklad(),new String[]{"CSKL"},new String[] {ds.getString("CSKL")})) {
       	   return getZiro(dm.getSklad().getString("CORG"));
       } else {
       	if (row > -1) dm.getSklad().goToRow(row);       	
       	return getZiro(ds.getString("CSKL"));
       }
    }
    
    
//    if (td.isDocFinanc(ds.getString("VRDOK"))) {
//      if (td.isDocSklad(ds.getString("VRDOK"))) {
//         if (ld.raLocate(dm.getSklad(),new String[]{"CSKL"},new String[] {ds.getString("CSKL")})) {
//           return getZiro(dm.getSklad().getString("CORG"));
//         }  else {
//System.out.println("Oðe ima probljem");
//           return "";
//         }
//      }
//      else {
//        return getZiro(ds.getString("CSKL"));
//      }
//    }
    //if (row > -1) dm.getSklad().goToRow(row);
    //return "";
  }
}