/****license*****************************************************************
**   file: repPlatjnaLista.java
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

public class repPlatjnaLista implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  frmIspList fil = frmIspList.getInstance();
  DataSet ds = fil.getObracunski();
  DataSet ss = fil.getSumObracunski();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repPlatjnaLista() {
//    fil = frmIspList.getInstance();
//    ds = fil.getObracunski();
//    ss = fil.getSumObracunski();
    ru.setDataSet(ds);
  }

  static int rbr;

  public repPlatjnaLista(int idx) {
    if (idx == 0){
//      System.out.println("fil : " + fil.toString());
      rbr = 0;
    }

    ds.goToRow(idx);
    if (fil.getRepMode() == 'A') ld.raLocate(ss, new String[] {"GODOBR", "MJOBR", "RBROBR"},
                                                 new String[] {String.valueOf(ds.getShort("GODOBR")),
                                                               String.valueOf(ds.getShort("MJOBR")),
                                                               String.valueOf(ds.getShort("RBROBR"))});
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
    }
    int indx=0;
    public Object nextElement() {

      return new repPlatjnaLista(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }

  public void close() {
  }

  public int getRBR(){
    return ++rbr;
  }

  public short getMJOBR(){
    if (fil.getRepMode() == 'O') return (short)0;
    return ds.getShort("MJOBR");
  }

  public String getCRADNIK(){
    return ds.getString("CRADNIK");
  }

  public String getRADNIK(){
    return ds.getString("PREZIME").concat(" ".concat(ds.getString("IME")));
  }

  public String getCRADMJ(){
    return ds.getString("CRADMJ");
  }

  public String getRADNOMJESTO(){
    ld.raLocate(dm.getRadMJ(), new String[] {"CRADMJ"}, new String[] {ds.getString("CRADMJ")});
    return dm.getRadMJ().getString("NAZIVRM");
  }

  public String getCORG(){
    return ds.getString("CORG");
  }

  public String getORGJEDINICA(){
    ld.raLocate(dm.getOrgstruktura(), new String[] {"CORG"}, new String[] {ds.getString("CORG")});
    return dm.getOrgstruktura().getString("NAZIV");
  }

  public BigDecimal getSATI(){
    return ds.getBigDecimal("SATI");
  }

  public BigDecimal getBRUTO(){
    return ds.getBigDecimal("BRUTO");
  }

  public BigDecimal getDOPRINOSI(){
    return ds.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getNETO(){
    return ds.getBigDecimal("NETO");
  }

  public BigDecimal getISKNEOP(){
    return ds.getBigDecimal("ISKNEOP");
  }

  public BigDecimal getPOR1(){
    return ds.getBigDecimal("POR1");
  }

  public BigDecimal getPOR2(){
    return ds.getBigDecimal("POR2");
  }

  public BigDecimal getPOR3(){
    return ds.getBigDecimal("POR3");
  }

  public BigDecimal getPOR4(){
    return ds.getBigDecimal("POR4");
  }

  public BigDecimal getPOR5(){
    return ds.getBigDecimal("POR5");
  }

  public BigDecimal getPORUK(){
    return ds.getBigDecimal("PORUK");
  }

  public BigDecimal getPRIR(){
    return ds.getBigDecimal("PRIR");
  }

  public BigDecimal getNAKNADE(){
    return ds.getBigDecimal("NAKNADE");
  }

  public BigDecimal getKREDA(){
    return ds.getBigDecimal("KREDITI");
  }

  public BigDecimal getNARUKE(){
    return ds.getBigDecimal("NARUKE");
  }

  public BigDecimal getSumSATI(){
    return ss.getBigDecimal("SATI");
  }

  public BigDecimal getSumBRUTO(){
    return ss.getBigDecimal("BRUTO");
  }

  public BigDecimal getSumDOPRINOSI(){
    return ss.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getSumNETO(){
    return ss.getBigDecimal("NETO");
  }

  public BigDecimal getSumISKNEOP(){
    return ss.getBigDecimal("ISKNEOP");
  }

  public BigDecimal getSumPOR1(){
    return ss.getBigDecimal("POR1");
  }

  public BigDecimal getSumPOR2(){
    return ss.getBigDecimal("POR2");
  }

  public BigDecimal getSumPOR3(){
    return ss.getBigDecimal("POR3");
  }

  public BigDecimal getSumPOR4(){
    return ss.getBigDecimal("POR4");
  }

  public BigDecimal getSumPOR5(){
    return ss.getBigDecimal("POR5");
  }

  public BigDecimal getSumPORUK(){
    return ss.getBigDecimal("PORUK");
  }

  public BigDecimal getSumPRIR(){
    return ss.getBigDecimal("PRIR");
  }

  public BigDecimal getSumNAKNADE(){
    return ss.getBigDecimal("NAKNADE");
  }

  public BigDecimal getSumKREDA(){
    return ss.getBigDecimal("KREDITI");
  }

  public BigDecimal getSumNARUKE(){
    return ss.getBigDecimal("NARUKE");
  }

  public String getNASLOV(){
    if (fil.getRepMode() == 'O') return "\n".concat(fil.getObracun().toLowerCase());
    return "\nobra\u010Dun pla\u0107e za " + ds.getShort("MJOBR") +".mjesec " + ds.getShort("GODOBR") +". (rbr." + ds.getShort("RBROBR") +")";
  }

  public int getSORTME(){
    if (fil.getRepMode() == 'A') return ds.getShort("GODOBR")*1000+ds.getShort("MJOBR")*100+ds.getShort("RBROBR");
    return 0;
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
  public String getDatumIsplate(){
    return rdu.dataFormatter(fil.getDatumIspl());
  }
}