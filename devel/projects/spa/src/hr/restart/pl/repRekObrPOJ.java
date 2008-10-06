/****license*****************************************************************
**   file: repRekObrPOJ.java
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
import hr.restart.robno.sgQuerys;
import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repRekObrPOJ implements sg.com.elixir.reportwriter.datasource.IDataProvider{
  _Main main;
  DataSet ds = frmRekObr.getQdsPOJ();

  QueryDataSet doprinosiNa = frmRekObr.getDoprinosiNaPOJ();
  QueryDataSet doprinosiR = frmRekObr.getDoprinosiIzPOJ();
  lookupData ld = lookupData.getlookupData();
  String [] prefix = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p"};

  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  String[] colname = new String[] {""};
  
  sgQuerys sg = sgQuerys.getSgQuerys();


  public repRekObrPOJ() {
//     ru.setDataSet(ds);
  }

  public repRekObrPOJ(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
    }
    int indx=0;
    public Object nextElement() {
      return new repRekObrPOJ(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }
//******************** M O J I  P O D A C I ******************************
  public String getCOrg()
  {
    return ds.getString("CORG");
  }

  public String getCVro()
  {
    return ds.getString("CVRO");
  }

  public double getBruto()
  {
    return ds.getBigDecimal("BRUTO").doubleValue();
  }

  public double getDoprinosi()
  {
    return ds.getBigDecimal("DOPRINOSI").doubleValue();
  }

  public double getNeto()
  {
    return ds.getBigDecimal("NETO").doubleValue();
  }

  public double getNeop()
  {
    return ds.getBigDecimal("NEOP").doubleValue();
  }

  public double getIskNeop()
  {
    return ds.getBigDecimal("ISKNEOP").doubleValue();
  }

  public double getPorOsn()
  {
    return ds.getBigDecimal("POROSN").doubleValue();
  }

  public double getPor1()
  {
    return ds.getBigDecimal("POR1").doubleValue();
  }

  public double getPor2()
  {
    return ds.getBigDecimal("POR2").doubleValue();
  }

  public double getPor3()
  {
    return ds.getBigDecimal("POR3").doubleValue();
  }

  public double getPor4()
  {
    return ds.getBigDecimal("POR4").doubleValue();
  }

  public double getPor5()
  {
    return ds.getBigDecimal("POR5").doubleValue();
  }

  public double getPorUk()
  {
    return ds.getBigDecimal("PORUK").doubleValue();
  }

  public double getPrir()
  {
   return ds.getBigDecimal("PRIR").doubleValue();
  }

  public double getPorIprir()
  {
   return ds.getBigDecimal("PORIPRIR").doubleValue();
  }

  public double getNeto2()
  {
   return ds.getBigDecimal("NETO2").doubleValue();
  }

  public double getNaknade()
  {
   return ds.getBigDecimal("NAKNADE").doubleValue();
  }

  public double getNetoPK()
  {
   return ds.getBigDecimal("NETOPK").doubleValue();
  }

  public double getKrediti()
  {
   return ds.getBigDecimal("KREDITI").doubleValue();
  }

  public double getNaRuke()
  {
   return ds.getBigDecimal("NARUKE").doubleValue();
  }

  public double getDoprPod()
  {
   return ds.getBigDecimal("DOPRPOD").doubleValue();
  }

  public double getOL31()
  {
   return ds.getBigDecimal("OL31").doubleValue();
  }

  public double getOL32()
  {
   return ds.getBigDecimal("OL32").doubleValue();
  }

  public double getOL33()
  {
   return ds.getBigDecimal("OL33").doubleValue();
  }


  public String getObracunLab()
 {
   String mj = frmRekObr.getFrmRekObr().fieldSet.getShort("MJESECOD")+"";
   String god = frmRekObr.getFrmRekObr().fieldSet.getShort("GODINAOD")+"";
   String rbr = frmRekObr.getFrmRekObr().fieldSet.getShort("RBROD")+"";
   String mjdo = frmRekObr.getFrmRekObr().fieldSet.getShort("MJESECDO")+"";
   String goddo = frmRekObr.getFrmRekObr().fieldSet.getShort("GODINADO")+"";
   String rbrdo = frmRekObr.getFrmRekObr().fieldSet.getShort("RBRDO")+"";


   if(frmRekObr.getFrmRekObr().getRepMode()=='A')
     return ("Obra\u010Dun za "+mj+"-"+god+" do " + mjdo+"-"+goddo+", rbr "+rbr+"-"+rbrdo);
   return ("Obra\u010Dun za "+mj+"-"+god+"/"+rbr);

  }

  public String getNivoLab()
  {
    String nivoLab ="";
    String cOrg = frmRekObr.getFrmRekObr().fieldSet.getString("CORG");
    ru.setDataSet(frmRekObr.getFrmRekObr().fieldSet);
    colname[0] = "CORG";
    nivoLab = ru.getSomething(colname,dm.getOrgstruktura(),"NAZIV").getString();
    return ("na organizacijskom nivou "+cOrg + " "+ nivoLab);

  }

  public String getOJ()
  {
    String nivoLab ="";
    String cOrg = ds.getString("CORG");
    ru.setDataSet(ds);
    colname[0] = "CORG";
    nivoLab = ru.getSomething(colname,dm.getOrgstruktura(),"NAZIV").getString();
    return (nivoLab);

   }

   public String getVroLab()
   {
     String nazVro ="";
     String cVro = ds.getString("CVRO");
     ru.setDataSet(ds);
     colname[0] = "CVRO";
     nazVro = ru.getSomething(colname,dm.getVrodn(),"NAZIVRO").getString();
     return (nazVro);
   }


  public int getBrRad()
  {
//    System.out.println("VRO RADNICI: " + ds.getString("VRO"));
//    System.out.println(frmRekObr.getFrmRekObr().getbrDjelatnikaVRO(ds.getString("VRO")));
//    return frmRekObr.getFrmRekObr().getbrDjelatnikaVRO(ds.getString("VRO"));
    return frmRekObr.getFrmRekObr().getbrDjelatnikaVRO(ds.getString("CVRO"), ds.getString("CORG"));
  }

  public String getDoprinosiNaOpis()
  {
    String opis ="";
    doprinosiNa.first();
    int i = 0;
    while(doprinosiNa.inBounds())
    {
      if(doprinosiNa.getString("CVRO").equals(this.getCVro()) && doprinosiNa.getString("CORG").equals(this.getCOrg()))
      {
        ld.raLocate(dm.getVrsteodb(), new String[]{"CVRODB"}, new String[]{doprinosiNa.getShort("CVRODB")+""});
        if(doprinosiNa.getRow() < doprinosiNa.getRowCount()-1)
          opis += prefix[i]+". "+dm.getVrsteodb().getString("OPISVRODB")+"\n";
        else
          opis += prefix[i]+". "+dm.getVrsteodb().getString("OPISVRODB");
        i++;
      }
      doprinosiNa.next();

    }
    return opis;
  }

  public String getDoprinosiNaIznosi()
  {
    String opis ="";
    doprinosiNa.first();
    while(doprinosiNa.inBounds())
    {
      if(doprinosiNa.getString("CVRO").equals(this.getCVro()) && doprinosiNa.getString("CORG").equals(this.getCOrg()))
      {
        if(doprinosiNa.getRow() < doprinosiNa.getRowCount()-1)
          opis += sg.format(doprinosiNa.getBigDecimal("IZNOS"),2)+"\n";
//          opis += reformatStr(doprinosiNa.getBigDecimal("IZNOS").toString())+"\n";
        else
          opis += sg.format(doprinosiNa.getBigDecimal("IZNOS"),2);
//          opis += reformatStr(doprinosiNa.getBigDecimal("IZNOS").toString());
      }
      doprinosiNa.next();

    }
    return opis;
  }


  public String getDoprinosiIzOpis()
{
  String opis ="";
  doprinosiR.first();

  int i = 0;
  while(doprinosiR.inBounds())
  {
    if(doprinosiR.getString("CVRO").equals(this.getCVro()) && doprinosiR.getString("CORG").equals(this.getCOrg()))
    {
      ld.raLocate(dm.getVrsteodb(), new String[]{"CVRODB"}, new String[]{doprinosiR.getShort("CVRODB")+""});
      if(doprinosiR.getRow() < doprinosiR.getRowCount()-1)
        opis += prefix[i]+". "+dm.getVrsteodb().getString("OPISVRODB")+"\n";
      else
        opis += prefix[i]+". "+dm.getVrsteodb().getString("OPISVRODB");
      i++;
    }
    doprinosiR.next();

  }
  return opis;
}

public String getDoprinosiIzIznosi()
{
  String opis ="";
  doprinosiR.first();
  while(doprinosiR.inBounds())
  {
    if(doprinosiR.getString("CVRO").equals(this.getCVro()) && doprinosiR.getString("CORG").equals(this.getCOrg()))
    {
      if(doprinosiR.getRow() < doprinosiR.getRowCount()-1)
        opis += sg.format(doprinosiR.getBigDecimal("IZNOS"),2)+"\n";
//        opis += reformatStr(doprinosiR.getBigDecimal("IZNOS").toString())+"\n";
      else
        opis += sg.format(doprinosiR.getBigDecimal("IZNOS"),2);
//        opis += reformatStr(doprinosiR.getBigDecimal("IZNOS").toString());
    }
    doprinosiR.next();

  }
  return opis;
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

//  private String reformatStr(String str)
//  {
//
//    int i = str.indexOf(".");
//    VarStr vs = new VarStr(str);
//    vs.replace(".",",");
//
//    while(i>3)
//    {
//      i = i-3;
//      vs.insert(i,".");
//    }
//    return vs.toString();
//  }

}

