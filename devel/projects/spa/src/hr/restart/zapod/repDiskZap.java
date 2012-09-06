/****license*****************************************************************
**   file: repDiskZap.java
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
package hr.restart.zapod;

import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;

import java.util.StringTokenizer;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repDiskZap extends repDisk {
  QueryDataSet qds;
  QueryDataSet cjelineDS = new QueryDataSet();
  hr.restart.baza.dM dm;
  Valid vl;
  public String fileName="";

  public repDiskZap() {
    try {
      String datum  =Valid.getValid().getToday().toString();
      fileName = datum.substring(8,10)+datum.substring(5,7)+datum.substring(2,4)+".ZAP";
      try {
         this.setPrinter(mxPrinter.getDefaultMxPrinter());
         this.getPrinter().setNewline(System.getProperty("line.separator"));
       }
       catch (Exception ex) {
         ex.printStackTrace();
      }
      this.setPrint("MM"+fileName);
      fill();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void fill() throws Exception
  {
    vl = Valid.getValid();
    dm = hr.restart.baza.dM.getDataModule();
    qds = (QueryDataSet)frmVirmani.getInstance().getRaQueryDataSet();
    qds.open();
    qds.getColumn("BRRACNT").setRowId(true);
    qds.setSort(new SortDescriptor(new String[] {"BRRACNT"}));
    qds.first();
    diskzap.open();
    diskzap.insertRow(false);
    diskzap.setString("REDAK", createLabela());
    diskzap.post();
    getCjelineDS(qds.getString("CKEY"));
    cjelineDS.first();
    while(cjelineDS.inBounds())
    {
      diskzap.insertRow(false);
      diskzap.setString("REDAK", insertSlogCjelina());
      insertSlogPojNal(cjelineDS.getString("BRRACNT"));
      cjelineDS.next();
    }
  }
  private String createLabela()
  {
    String datumizv = qds.getTimestamp("DATUMIZV").toString();
    StringBuffer sb = getNullSB();
    String nalogodavatelj ,mjesto,zirorac,DI, identifikator, param, nule;
    nule="000";
    String corg = hr.restart.zapod.OrgStr.getKNJCORG();
    lookupData.getlookupData().raLocate(dm.getOrgstruktura(), new String [] {"CORG"}, new String[]{corg});
    nalogodavatelj = dm.getOrgstruktura().getString("NAZIV");
    mjesto = dm.getOrgstruktura().getString("MJESTO")+"          ";
    mjesto = mjesto.substring(0,10);
    zirorac = zrParser(dm.getOrgstruktura().getString("ZIRO"));
    identifikator=zirorac.substring(0,3);
    param = hr.restart.sisfun.frmParam.getParam("zapod", "znoIzdok");
    DI = zirorac.substring(0,dm.getOrgstruktura().getString("ZIRO").indexOf("-"));
    if(identifikator.equals("301") || identifikator.equals("601"))
    {
      throw new RuntimeException("Neispravan format ziro-racuna '" + zirorac+"'");
    }
    else
    {
      sb.replace(18,18+nalogodavatelj.length(), nalogodavatelj);
      sb.replace(68,68+10/*mjesto.length()*/, mjesto+"          ");
      sb.replace(78,78+datumParser(datumizv, 0).length(),
                 datumParser(datumizv,0));
      sb.replace(84,84+nule.length(),nule);
      sb.replace(97,97+param.length(),param);
//      sb.replace(100,100+DI.length(), DI);
//      sb.replace(107,107+zirorac.length(), zirorac);
      sb.replace(100,100+zirorac.length(), zirorac);
      String datpodn=datumParser(new java.sql.Timestamp(System.currentTimeMillis()).toString(),1);
      sb.replace(241,241+datpodn.length(),datpodn);
//      sb.replace(241,241+datumParser(datumizv,1).length()
//                 ,datumParser(datumizv,1));
      sb.replace(249,250,"0");
    }
    return new String(sb);
  }

  private String datumParser(String datum, int i)
  {
    if(i==0)
    {
      return datum.substring(8,10)+datum.substring(5,7)+datum.substring(2,4);
    }
    else
    {
      return datum.substring(8,10)+datum.substring(5,7)+datum.substring(0,4);
    }
  }
  private String zrParser(String zr)
  {
    String newZR = "";
    StringTokenizer st = new StringTokenizer(zr,"-");
    {
      while(st.hasMoreTokens())
      {
        newZR += st.nextToken();
      }
    }
    return newZR;
  }

  private void getCjelineDS(String ckey)
  {
    String zr="";
    cjelineDS = hr.restart.baza.Virmani.getDataModule().getFilteredDataSet("1=0");
    cjelineDS.open();

    qds.first();
    while(qds.inBounds())
    {
      if(!zr.equals(qds.getString("BRRACNT")))
      {
        zr= qds.getString("BRRACNT");
        cjelineDS.insertRow(false);
        qds.copyTo(cjelineDS);
        cjelineDS.post();
      }
      qds.next();
    }
  }
  private String insertSlogCjelina()
  {
    StringBuffer sb = getNullSB();
    String nalogodavatelj, mjesto, svota,brNal, zirorac, DI, identifikator;
    int z = cjelineDS.getString("NATERET").indexOf("\n");
    String datumizvParsed1 = datumParser(cjelineDS.getTimestamp("DATUMIZV").toString(), 1);
    if(z>0)
      nalogodavatelj = cjelineDS.getString("NATERET").substring(0,cjelineDS.getString("NATERET").indexOf("\n")).trim();
    else
      nalogodavatelj = cjelineDS.getString("NATERET").trim();
    lookupData.getlookupData().raLocate(dm.getOrgstruktura(), new String [] {"NAZIV"}, new String[]{nalogodavatelj});
    mjesto = dm.getOrgstruktura().getString("MJESTO")+"          ";
    mjesto = mjesto.substring(0,10);
    zirorac = zrParser(cjelineDS.getString("BRRACNT"));
    try {
      DI = dm.getOrgstruktura().getString("ZIRO").substring(0,dm.getOrgstruktura().getString("ZIRO").indexOf("-"));
    }
    catch (Exception ex) {
      throw new RuntimeException("Neispravan format ziro-racuna '" + zirorac+"'");
    }

    svota = getSvotaCJ(cjelineDS.getString("BRRACNT"), cjelineDS.getString("CKEY"));
    brNal = getBrNaloga(cjelineDS.getString("BRRACNT"), cjelineDS.getString("CKEY"));
    identifikator=zirorac.substring(0,3);

    if(identifikator.equals("301") || identifikator.equals("601"))
    {
      throw new RuntimeException("Stari format ziro-racuna");
    }
    else
    {
      sb.replace(18,18+nalogodavatelj.length(),nalogodavatelj);

      sb.replace(68,68+10/*mjesto.length()*/,mjesto+"          ");
      sb.replace(78, 78+svota.length(),svota);
      sb.replace(93,93+brNal.length(),brNal);
//      sb.replace(98,98+DI.length(),DI);
//      sb.replace(105,105+zirorac.length(),zirorac);
      sb.replace(98,98+zirorac.length(),zirorac);
      sb.replace(123,123+datumizvParsed1.length()
                 ,datumizvParsed1);
      sb.replace(249,250,"9");
    }
    return new String(sb);
  }

  private void insertSlogPojNal(String zr)
  {

    String primatelj, mjesto, zirorac, pnbz1,pnbz2, pnbo1, pnbo2, svrha, DI,svota, temp;
    zirorac = zrParser(cjelineDS.getString("BRRACUK"));
    StringBuffer sb;
    qds.first();
    while(qds.inBounds())
    {
      if(qds.getString("BRRACNT").equals(zr))
      {
        sb = getNullSB();
        int z = qds.getString("UKORIST").indexOf("\n");
        if(z>0)
          primatelj = qds.getString("UKORIST").substring(0,qds.getString("UKORIST").indexOf("\n")).trim();
        else
          primatelj = qds.getString("UKORIST").trim();
        lookupData.getlookupData().raLocate(dm.getPartneri(), new String [] {"NAZPAR"}, new String[]{primatelj});

        temp = getSvotaPN(qds.getString("BRRACUK"), qds.getShort("RBR"));
        svota = formatString(temp,13);
        pnbz1 = //formatString(qds.getString("PNBZ1"),2);
          qds.getString("PNBZ1");//ako hoce nule nek ih upise u virman
        pnbz2 = qds.getString("PNBZ2");
        pnbo1 = //formatString(qds.getString("PNBO1"),2);
          qds.getString("PNBO1");//ako hoce nule nek ih upise u virman
        pnbo2 = qds.getString("PNBO2");

        svrha = qds.getString("SVRHA").trim();
        if(svrha.length()>36)
          svrha = svrha.substring(0,36).trim();
        mjesto = dm.getPartneri().getString("MJ")+"          ";
        mjesto = mjesto.substring(0,10);
        try {
          DI= qds.getString("BRRACUK").substring(0, qds.getString("BRRACUK").indexOf("-"));
        }
        catch (Exception ex) {
          throw new RuntimeException("Neispravan oblik žiro-ra\u010Duna !");
        }
        zirorac = zrParser(qds.getString("BRRACUK")) ;
        sb.replace(18,18+primatelj.length(),primatelj);
        sb.replace(68,68+10/*mjesto.length()*/,mjesto);
        sb.replace(78,78+pnbz1.length(),pnbz1);
        sb.replace(80,80+pnbz2.length(),pnbz2);
        sb.replace(102,102+svrha.length(),svrha);
        sb.replace(157,157+pnbo1.length(),pnbo1);
        sb.replace(159,159+pnbo2.length(),pnbo2);
        sb.replace(144,144+svota.length(),svota);
//        sb.replace(181,181+DI.length(), DI);
//        sb.replace(188,188+zirorac.length(), zirorac);
        sb.replace(181,181+zirorac.length(), zirorac);
        sb.replace(249,250,"1");
        diskzap.insertRow(false);
        diskzap.setString("REDAK", new String(sb));
        diskzap.post();
      }
      qds.next();
    }
  }

  private StringBuffer getNullSB()
  {
    String dummy = "";
    for (int i = 0; i<250;i++)
    {
      dummy+=" ";
    };
    return new StringBuffer(dummy);
  }

  private String getSvotaCJ(String zr, String ckey)
  {
    String svota, temp;
    String qStr = "select sum(iznos) as iznos from virmani where brracnt='"+zr+"' and ckey = '"+ckey+"'";
    QueryDataSet ds = Util.getNewQueryDataSet(qStr);
    ds.open();
    temp = ds.getBigDecimal("IZNOS").toString();
    svota = temp.substring(0,temp.indexOf("."))+temp.substring(temp.indexOf(".")+1, temp.length());
    return formatString(svota,15);
  }

  private String getSvotaPN(String zr, short rbr)
  {
    String svota, temp;
/*
 //ANDREJ: Ovo je totalna nebuloza i nemrem dokuciti cemu sluzi ovaj maliciozni kod 
    String qStr = "select iznos as iznos from virmani where brracuk='"+zr+"' and rbr="+rbr;
    QueryDataSet ds = Util.getNewQueryDataSet(qStr);
    ds.open();
*/
    temp = qds.getBigDecimal("IZNOS").toString();
    svota = temp.substring(0,temp.indexOf("."))+temp.substring(temp.indexOf(".")+1, temp.length());

    return formatString(svota,13);
  }
  private String getBrNaloga(String zr, String ckey)
  {
    String qStr = "select count(*) as brn from virmani where brracnt='"+zr+"' and ckey ='"+ckey+"'";
    QueryDataSet ds = Util.getNewQueryDataSet(qStr);
    ds.open();
    return formatString(ds.getInt("brn")+"",5);
  }

  private String formatString(String str, int len)
  {
    if(str.length()>len)
      return str.substring(0,len);
    else
      return vl.maskString(str,'0',len);
  }
}
