/****license*****************************************************************
**   file: repStanjeKonto.java
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
package hr.restart.gk;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;

import com.borland.dx.dataset.DataSet;

public class repStanjeKonto implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmStanjeKonto fsk = frmStanjeKonto.getFrmStanjeKonto();
  DataSet ds = fsk.getRepQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static int rb;

  public repStanjeKonto() {
    ru.setDataSet(ds);
  }

  public repStanjeKonto(int idx) {
    if(idx==0){
      rb = 0;
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

        return new repStanjeKonto(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getFIRSTCOL() {
    if (fsk.getZbirno()) {
      return ds.getString("MM").trim();
    }
    return ds.getString("MJ").trim();
  }

  public String getNazFirstCol() {
    if (fsk.getZbirno()) {
      return "";
    }
    return "Mjesec";
  }
  public double getPsIP(){
    return fsk.getPsIP().doubleValue();
  }
  public double getPsID(){
    return fsk.getPsID().doubleValue();
  }
  public double getPsSALDO(){
    return fsk.getPsSaldo().doubleValue();
  }
  public double getSUMIP(){
    return fsk.getSUMIP().doubleValue();
  }
  public double getSUMID(){
    return fsk.getSUMID().doubleValue();
  }
  public double getSUMSALDO(){
    return fsk.getSUMSALDO().doubleValue();
  }
  public double getSveukupnoSUMIP(){
    return getPsIP() + getSUMIP();
  }
  public double getSveukupnoSUMID(){
    return getPsID() + getSUMID();
  }
  public double getSveukupnoSUMSALDO(){
    return getPsSALDO() + getSUMSALDO();
  }
  public int getRBR(){
    return ds.getInt("RBR");
  }
  public double getID() {
    return ds.getBigDecimal("ID").doubleValue();
  }
  public double getIP() {
    return ds.getBigDecimal("IP").doubleValue();
  }
  public double getSALDO() {
    return ds.getBigDecimal("SALDO").doubleValue();
  }
  public String getCORG() {
    return fsk.getCORG();
  }
  public String getNAZORG() {
    return fsk.getNAZORG();
  }
  public String getCKON() {
    return fsk.getCKON();
  }
  public String getNAZKON() {
    return fsk.getNAZKON();
  }
  public String getDoMjGod() {
    return "Stanje do " + fsk.getMJ() + " mjeseca " + fsk.getGOD() + " godine.";
  }
  public String getFirstLine(){
    return re.getFirstLine();
  }
  public String getSecondLine(){
    return re.getSecondLine();
  }
  public String getThirdLine(){
    return re.getThirdLine();
  }
  public String getLogoMjesto(){
    return re.getLogoMjesto();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}