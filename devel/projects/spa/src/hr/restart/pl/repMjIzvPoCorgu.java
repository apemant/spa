/****license*****************************************************************
**   file: repMjIzvPoCorgu.java
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
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repMjIzvPoCorgu implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  frmMjIzv frmi = frmMjIzv.getFrmMjIzv();
  DataSet ds = frmi.getRepPoCorg();
  DataSet ss = frmi.getSumPoCorg();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repMjIzvPoCorgu() {
//    frmi = frmMjIzv.getFrmMjIzv();
//    ds = frmi.getRepPoCorg();
//    ss = frmi.getSumPoCorg();
//    System.out.println("frmi : " + frmi);
//    System.out.println("MjIzv - RIPORT");
    ru.setDataSet(ds);
  }

  public repMjIzvPoCorgu(int idx) {
    if (idx == 0){}
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
    }
    int indx=0;
    public Object nextElement() {

      return new repMjIzvPoCorgu(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }

  public void close() {
  }

  public double getFONDSATIMJ(){
    if (frmi.getRepMode() == 'O'){
      return frmi.getFondSatiRada().doubleValue();
    }
    return frmi.getFondSatiRada(ds.getShort("GODOBR"), ds.getShort("MJOBR")).doubleValue();
  }

  public int getBRZAP(){
    int brzap;
    if (frmi.getRepMode() == 'O'){
      brzap = (ds.getBigDecimal("SATI").divide(frmi.getFondSatiRada(),0)).intValue();
    } else {
      brzap = ds.getBigDecimal("SATI").intValue() / frmi.getSumFondSati();
    }
    return brzap;
  }

  public BigDecimal getSATIPRV(){
    return ds.getBigDecimal("SATI");
  }

  public String getCORG(){
    return ds.getString("CORG");
  }

  public String getNAZORG(){
    dm.getOrgstruktura().open();
    lookupData.getlookupData().raLocate(dm.getOrgstruktura(), new String[] {"CORG"}, new String[] {ds.getString("CORG")});
    return dm.getOrgstruktura().getString("NAZIV");
  }

  public short getGODOBR(){
    if(frmi.getRepMode() == 'O')
      return (short)0;
    return ds.getShort("GODOBR");
  }

  public short getMJOBR(){
    if(frmi.getRepMode() == 'O')
      return (short)0;
    return ds.getShort("MJOBR");
  }

  public BigDecimal getNETO(){
    return ds.getBigDecimal("NETO");
  }

  public BigDecimal getPORIPRIR(){
    return ds.getBigDecimal("PORIPRIR");
  }

  public BigDecimal getDOPIZPL(){
    return ds.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getBRUTO(){
    return ds.getBigDecimal("BRUTO");
  }

  public BigDecimal getDOPNAPL(){
    return ds.getBigDecimal("DOPRPOD");
  }

  public double getZADPLAC(){
    double zadpl = (ds.getBigDecimal("BRUTO").add(ds.getBigDecimal("DOPRPOD"))).doubleValue();
    return zadpl;
  }

  public BigDecimal getNAKNADE(){
    return ds.getBigDecimal("NAKNADE");
  }

  public double getZADRAC(){
    double zadpl = (ds.getBigDecimal("BRUTO").add(ds.getBigDecimal("DOPRPOD").add(ds.getBigDecimal("NAKNADE")))).doubleValue();
    return zadpl;
  }

  public int getsumRadnika(){
    int brrad;
    if(frmi.getRepMode() == 'A'){
      brrad = ss.getBigDecimal("SATI").intValue() / frmi.getSumFondSati();
    } else {
      brrad = ss.getInt("BRRAD");
    }
    return brrad;
  }

  public BigDecimal getsumSati(){
     return ss.getBigDecimal("SATI");
  }

  public BigDecimal getsumNeto(){
     return ss.getBigDecimal("NETO");
  }

  public BigDecimal getsumPorPri(){
    return ss.getBigDecimal("PORIPRIR");
  }

  public BigDecimal getsumDopIzPl(){
    return ss.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getsumBruto(){
    return ss.getBigDecimal("BRUTO");
  }

  public BigDecimal getsumDoprPod(){
    return ss.getBigDecimal("DOPRPOD");
  }

  public BigDecimal getsumZadPl(){
    return ss.getBigDecimal("ZADPL");
  }

  public BigDecimal getsumNaknade(){
    return ss.getBigDecimal("NAKNADE");
  }

  public BigDecimal getsumZadRac(){
    return ss.getBigDecimal("ZADRC");
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

  public double getFondSati(){
    return frmi.getFondSatiRada().doubleValue();
  }

  public String getDatumIsplate(){
    if (frmi.getRepMode() == 'O')
    return rdu.dataFormatter(frmi.getDatumIspl());
    return "";
  }

  public String getDatumIspisa(){
    return rdu.dataFormatter(vl.getToday());
  }

  public String getPODNASLOV(){
    if (frmi.getRepMode() == 'O')
      return "za ".concat(ut.getMonth(frmi.getDatumIspl())).concat(
             ". mjesec ".concat(ut.getYear(frmi.getDatumIspl()))).concat(". godine");
    return "za period od " + frmi.getMjesecOd() + ". mjeseca " + frmi.getGodOd() + ". godine\n" +
           "do " + frmi.getMjesecDo() + ". mjeseca " + frmi.getGodDo() + ". godine";
  }
}