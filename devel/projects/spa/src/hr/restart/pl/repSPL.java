/****license*****************************************************************
**   file: repSPL.java
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

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repSPL implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmSPL frSPL = frmSPL.getInstance();
  DataSet ds = frSPL.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repSPL() {
    ru.setDataSet(ds);
  }

  public repSPL(int idx) {
    if (idx == 0){
    }
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {

        return new repSPL(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public double getBROJRADNIKA(){
     return frSPL.getBrojRadnika();
  }

  public BigDecimal getBRUTO0(){
    return ds.getBigDecimal("BRUTO");
  }

  public double getBRUTOMAX10(){
    return frSPL.getBruto10();
  }

  public double getNETOMAX12(){
    return frSPL.getNeto12();
  }

  public double getBRUTO(){
    return frSPL.getBruto0();
  }

  public double getRB4(){
    return ds.getBigDecimal("NETO2").add(ds.getBigDecimal("PORIPRIR").add(ds.getBigDecimal("DOPRINOSI"))).doubleValue();
  }

  public BigDecimal getNETO(){
     return ds.getBigDecimal("NETO");
  }

  public BigDecimal getDOPRINOSI(){
     return ds.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getPORIPRIR(){
     return ds.getBigDecimal("PORIPRIR");
  }

  public BigDecimal getDOPRIPORIPRIR(){
     return ds.getBigDecimal("PORIPRIR").add(ds.getBigDecimal("DOPRINOSI"));
  }

  public BigDecimal getNETO2(){
     return ds.getBigDecimal("NETO2");
  }

  public BigDecimal getBMIN(){
     return ds.getBigDecimal("BMIN");
  }

  public BigDecimal getBMAX(){
     return ds.getBigDecimal("BMAX");
  }

  public BigDecimal getNMIN(){
     return ds.getBigDecimal("NMIN");
  }

  public BigDecimal getNMAX(){
     return ds.getBigDecimal("NMAX");
  }

  public String getDATUMISPISA(){
    return rdu.dataFormatter(frSPL.getDatumIspl());
  }

  public String getUMJESECUGODINE(){
    String mj = String.valueOf(frSPL.getMjObr());
    String gd = String.valueOf(frSPL.getGdObr());
    if (frSPL.getMjObr() < 10) mj = "0".concat(mj);
    return "u mjesecu ".concat(mj.concat(", ".concat(gd.concat(" godine"))));
  }

  public String getNazivMjesto(){
    return frSPL.getKnjNaziv() + "\n" 
          + frSPL.getKnjHpBroj() + " " + frSPL.getKnjMjesto() + "\n"
           + frSPL.getKnjAdresa();
  }

  public int getGodMj(){
    return frSPL.getGodMj();
  }

  public String getZIRO(){
    return frSPL.getKnjZiro();
  }

  public String getMATBROJ(){
    return frSPL.getKnjMatbroj();
  }
  
  public String getOznVl() {
    return frmParam.getParam("pl", "oznVlas"+OrgStr.getKNJCORG(),"1","Oznaka vlasnistva poduzeca "+OrgStr.getKNJCORG());
  }
  
  public String getCopcine() {
    raIniciranje.getInstance().posOrgsPl(hr.restart.zapod.OrgStr.getKNJCORG());
    return dm.getOrgpl().getString("COPCINE");
  }
  public String getSifraDjel() {
    return re.getLogoSifdjel();
  }
  public String getCZup() {
    lookupData.getlookupData().raLocate(dm.getOpcine(), "COPCINE", getCopcine());
    return dm.getOpcine().getShort("CZUP")+"";
  }
  
}