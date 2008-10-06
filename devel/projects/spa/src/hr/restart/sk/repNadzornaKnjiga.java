/****license*****************************************************************
**   file: repNadzornaKnjiga.java
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

import hr.restart.baza.dM;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repNadzornaKnjiga implements raReportData {
  frmNadzornaKnjiga fnk = frmNadzornaKnjiga.getInstance();
  QueryDataSet ds = fnk.getRaQueryDataSet();//new QueryDataSet(); /// <- ovo bi mogla bit pušiona
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();

  public repNadzornaKnjiga() {
    dm.getZpZemlje().open();
    dm.getMjesta().open();
  }

  public raReportData getRow(int idx) {
    ds.goToRow(idx);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds = null;
  }

  public String getKNJIG(){
     return ds.getString("KNJIG");
  }

  public String getNAZKNJIG(){
    DataRow dr = ld.raLookup(dm.getOrgstruktura(), "CORG", ds.getString("KNJIG"));
    if (dr != null) return dr.getString("NAZIV");
    else return "";
  }

  public String getADRKNJIG(){
    DataRow dr = ld.raLookup(dm.getOrgstruktura(), "CORG", ds.getString("KNJIG"));
    if (dr != null) {
      String adresa = "",mj="",pb="",adr="";
      if (!dr.getString("ADRESA").equals("")) {
        adr= dr.getString("ADRESA")+" ";
        if (!dr.getString("HPBROJ").equals("")) {
          pb = dr.getString("HPBROJ")+ " ";
          if (!dr.getString("MJESTO").equals("")) mj = dr.getString("MJESTO");
        } else {
          if (!dr.getString("MJESTO").equals("")) mj = dr.getString("MJESTO");
        }
      } else return "";
      return adresa = adr+"\n"+pb+mj;
    }
    else return "";
  }

  public String getFULLKNJIG(){
    return getNAZKNJIG()+"\n"+getADRKNJIG();
  }

  public String getGOD(){
     return ds.getString("GOD");
  }

  public int getRBR(){
     return ds.getInt("RBR");
  }

  public String getDATUM(){
     return rdu.dataFormatter(ds.getTimestamp("DATUM"));
  }

  public int getBRSTR(){
     return ds.getInt("BRSTR");
  }

  public String getSTRANICABROJ() {
    return getBRSTR() + "-" + getRBR();
  }

  public String getBRISPRAVE(){
     return ds.getString("BRISPRAVE");
  }

  public String getDATDOK(){
     return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  private static DataRow partnerRow;

  public int getCPAR(){
    partnerRow = ld.raLookup(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR")));
    return ds.getInt("CPAR");
  }

  public String getNAZIVPAR(){
    if (partnerRow != null) return  partnerRow.getString("NAZPAR");
    else return "";
  }

  public String getADRESAPAR(){
    if (partnerRow != null) {
      String pbr = "";
      if (!partnerRow.getString("PBR").equals("")) pbr = partnerRow.getString("PBR")+" ";
      return  pbr+partnerRow.getString("ADR");
    }
    else return "";
  }

  public String getMJESTOPAR(){
    if (partnerRow != null){
      if (partnerRow.getInt("CMJESTA") != 0){
        DataRow dr = ld.raLookup(dm.getMjesta(), "CMJESTA", String.valueOf(partnerRow.getInt("CMJESTA")));
        if (dr != null) return dr.getString("NAZMJESTA");
      } else return partnerRow.getString("MJ");
    }
    return "";
  }

  public String getZEMLJAPAR(){

    int cmjesta = partnerRow.getInt("CMJESTA");
    if (!lookupData.getlookupData().raLocate(dm.getAllMjesta(), "CMJESTA", cmjesta+"")) return "Hrvatska";
    String czem = dm.getAllMjesta().getString("CZEM");
    if (!lookupData.getlookupData().raLocate(dm.getAllZpZemlje(), "CZEM", czem)) return "";

    return dm.getAllZpZemlje().getString("NAZIVZEM");

//    if (partnerRow != null){
//      System.out.println("- "+partnerRow.getInt("CMJESTA"));
//      if (partnerRow.getInt("CMJESTA") != 0){
//        DataRow dr = ld.raLookup(dm.getMjesta(), "CMJESTA", String.valueOf(partnerRow.getInt("CMJESTA")));
//        if (dr != null) {
//          DataRow drZem = ld.raLookup(dm.getZpZemlje(), "CZEMLJE", dr.getString("CZEMLJE"));
//          System.out.println(dm.getZpZemlje().getString("NAZIVZEM"));
//          if (drZem != null) return dm.getZpZemlje().getString("NAZIVZEM");
//        }
//      } else return "Hrvatska";
//    }
//    return "";
  }

  private static DataRow partnerPosrRow;

  public int getCPARPOSR(){
    partnerPosrRow = ld.raLookup(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPARPOSR")));
    return ds.getInt("CPARPOSR");
  }

  public String getNAZIVPARPOSR(){
    if (partnerPosrRow != null) return  partnerPosrRow.getString("NAZPAR");
    else return "";
  }

  public String getADRESAPARPOSR(){
    if (partnerPosrRow != null) {
      String pbr = "";
      if (!partnerPosrRow.getString("PBR").equals("")) pbr = partnerPosrRow.getString("PBR")+" ";
      return  pbr+partnerPosrRow.getString("ADR");
    }
    else return "";
  }

  public String getMJESTOPARPOSR(){
    if (partnerPosrRow != null){
      if (partnerPosrRow.getInt("CMJESTA") != 0){
        DataRow dr = ld.raLookup(dm.getMjesta(), "CMJESTA", String.valueOf(partnerPosrRow.getInt("CMJESTA")));
        if (dr != null) return dr.getString("NAZMJESTA");
      } else return partnerPosrRow.getString("MJ");
    }
    return "";
  }

  public String getZEMLJAPARPOSR(){

    int cmjesta = partnerPosrRow.getInt("CMJESTA");
    if (!lookupData.getlookupData().raLocate(dm.getAllMjesta(), "CMJESTA", cmjesta+"")) return "Hrvatska";
    String czem = dm.getAllMjesta().getString("CZEM");
    if (!lookupData.getlookupData().raLocate(dm.getAllZpZemlje(), "CZEM", czem)) return "";

    return dm.getAllZpZemlje().getString("NAZIVZEM");

//    if (partnerPosrRow != null){
//      if (partnerPosrRow.getInt("CMJESTA") != 0){
//        DataRow dr = ld.raLookup(dm.getMjesta(), "CMJESTA", String.valueOf(partnerPosrRow.getInt("CMJESTA")));
//        if (dr != null) {
//          DataRow drZem = ld.raLookup(dm.getZpZemlje(), "CZEMLJE", dr.getString("CZEMLJE"));
//          if (drZem != null) return dm.getZpZemlje().getString("NAZIVZEM");
//        }
//      } else return "Hrvatska";
//    }
//    return "";
  }

  public BigDecimal getVRIJEDNOST(){
     return ds.getBigDecimal("VRIJEDNOST");
  }

  public String getVRIJEDNOSTSTR(){
    String iznos = format(ds,"VRIJEDNOST");
    if (iznos.equals("0,00")) return "";
    return iznos;
  }

  public BigDecimal getPROVIZIJA(){
     return ds.getBigDecimal("PROVIZIJA");
  }

  public String getPROVIZIJASTR(){
    String iznos = format(ds,"PROVIZIJA");
    if (iznos.equals("0,00")) return "";
    return iznos;
  }

  public String getDATUMPL(){
     return rdu.dataFormatter(ds.getTimestamp("DATUMPL"));
  }

  public BigDecimal getIZNOSPL(){
     return ds.getBigDecimal("IZNOSPL");
  }

  public String getCNACPL(){
     return ds.getString("CNACPL");
  }

  public String getNAZNACPL(){
    dm.getNacpl().open();
    DataRow dr = ld.raLookup(dm.getNacpl(), "CNACPL", ds.getString("CNACPL"));
    if (dr != null) return dr.getString("NAZNACPL");
    else return "";
  }

  public String getBROJRJES(){
     return ds.getString("BROJRJES");
  }

  public String getDATUMRJES(){
     return rdu.dataFormatter(ds.getTimestamp("DATUMRJES"));
  }

  public int getSTARIRBR(){
    return ds.getInt("STARIRBR");
  }

  public String getSTARAGOD(){
     return ds.getString("STARAGOD");
  }

  public String getBROJJCD(){
     return ds.getString("BROJJCD");
  }

  public String gatDATUMJCD(){
     return rdu.dataFormatter(ds.getTimestamp("DATUMJCD"));
  }

  public String getZEMPODRIJETLA(){
     return ds.getString("ZEMPODRIJETLA");
  }

  public String getNAZZEMPODRIJETLA(){
    if (!lookupData.getlookupData().raLocate(dm.getAllZpZemlje(), "OZNZEM", ds.getString("ZEMPODRIJETLA"))) return "";

    return dm.getAllZpZemlje().getString("NAZIVZEM");


//    DataRow dr = ld.raLookup(dm.getZpZemlje(), "CZEM", ds.getString("ZEMPODRIJETLA"));
//    if (dr != null) return dr.getString("NAZIVZEM");
//    else return null;
  }

  public String get1(){
    return getRBR() + " , " + getDATUM();
  }

  public String get2(){
    return getBRISPRAVE() + " , " + getDATDOK();
  }

  public String get3(){
    String sepp = " , ";
    if (getNAZIVPAR().equals(""))return "";
    else if (getZEMLJAPAR().equals("")) sepp ="";
    return getNAZIVPAR() + sepp + getZEMLJAPAR();
  }

  public String get5(){
    String sepp = " , ";
    if (getNAZIVPARPOSR().equals(""))return "";
    else if (getZEMLJAPARPOSR().equals("")) sepp ="";
    return getNAZIVPARPOSR() + sepp + getZEMLJAPARPOSR();
  }

  public String get7(){
    String sepp1 = " , ";
    String sepp2 = " , ";
    String iznos = format(ds,"IZNOSPL");
    if (iznos.equals("0,00")) iznos = "";
    if (getDATUMPL().equals("")) sepp1 = "";
    if (iznos.equals("") || getNAZNACPL().equals("")) sepp2 = "";
    return getDATUMPL()+ sepp1 + iznos + sepp2 + getNAZNACPL();
  }

  public String get8(){
    String sepp = " , ";
    if (getBROJRJES().equals("") || getDATUMRJES().equals("")) sepp = "";
    return getBROJRJES() + sepp + getDATUMRJES();
  }

  public String get9(){
    if (getSTARIRBR() == 0) return "";
    return getSTARIRBR() + " , " + getSTARAGOD();
  }

  public String get10(){
    String sepp = " , ";
    if (getBROJJCD().equals("")) sepp = "";
    return getBROJJCD() + sepp + gatDATUMJCD();
  }



  private String format(DataSet set, String colName) {
    com.borland.dx.text.VariantFormatter formater = ds.getColumn("IZNOSPL").getFormatter();
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    set.getVariant(colName,v);
    return formater.format(v);
  }
}