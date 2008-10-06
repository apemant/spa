/****license*****************************************************************
**   file: repPopListIM.java
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

import hr.restart.robno._Main;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repPopListIM implements sg.com.elixir.reportwriter.datasource.IDataProvider{
  _Main main;
  DataSet ds = frmBankSpec.getInstance().getSpecBankDS(new String[] {"CISPLMJ","CORG","PREZIME","IME"});
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  String[] colname = new String[] {""};
  int rowCount =0;
  lookupData ld = lookupData.getlookupData();


  public String getlab0()
  {
    String s = frmBankSpec.getInstance().IMOj;
//    if(s.equals("zbirno"))
//      return "po isplatnim mjestima - "+s;
    return "po isplatnim mjestima\n"+s;
  }

  public repPopListIM() {
     ru.setDataSet(ds);
  }

  public repPopListIM(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
      rowCount= ds.getRowCount();
    }
    int indx=0;
    public Object nextElement() {
      return new repPopListIM(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }
//******************** M O J I  P O D A C I ******************************

  public String getImePrez()
  {
    return ds.getString("PREZIME")+" "+ds.getString("IME");
  }

  public String getBrRac()
  {
    return ds.getString("BROJTEK");
  }

  public String getJMBG()
  {
    return ds.getString("JMBG");
  }

  public double getIznos()
  {
    return ds.getBigDecimal("NARUKE").doubleValue();
  }

  public short getIsplMj()
  {
    return ds.getShort("ISPLMJ");
  }

  public String getSifraRad()
  {
    return ds.getString("CRADNIK");
  }

  public String getSumLabela()
  {
    return "S V E U K U P N O";
  }

  public String getUkDjelLabela()
  {
    return " Sveukupno djelatnika";
  }

  public int getDummy()
  {
    return 1;
  }


  public int getUkBrDjel()
   {
     return ds.getRowCount();
  }
  public double getSuma()
  {
    return frmBankSpec.getInstance().suma;
  }


  public String getlabela()
  {
      return frmBankSpec.getInstance().labela;
  }
  public int getRowNum()
 {
   return rowCount;
  }

  public String getIMNaziv()
  {
    String cIM = getIsplMj()+"";
    ld.raLocate(dm.getIsplMJ(), new String[]{"CISPLMJ"}, new String[]{cIM});
    return dm.getIsplMJ().getString("NAZIV");
  }

//*****************************************************************
  public void close() {
  }
  public String getFirstLine(){
    return rpm.getFirstLine();
  }
  public String getSecondLine(){
    return rpm.getSecondLine();
  }
  public String getThirdLine(){
    return rpm.getThirdLine();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(frmRekObr.getFrmRekObr().getDatumIspl());
  }



}

