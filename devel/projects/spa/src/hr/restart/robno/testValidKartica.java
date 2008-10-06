/****license*****************************************************************
**   file: testValidKartica.java
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

import hr.restart.baza.dM;
import hr.restart.util.Aus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;


public class testValidKartica {

  private dM dm = dM.getDataModule();
  private String vrskl;
  private String godina;
  private String cskl;
  private StorageDataSet kartica;
  private Integer cart;
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

//  private testValidKartica(){
//    initKartica();
//  }
  public int getCart() {
    return cart.intValue();
  }
  public String getCskl() {
    return cskl;
  }
  public String getGodina() {
    return godina;
  }
  public void setCart(int cart) {
    this.cart = new Integer(cart);
  }
  public void setCskl(String cskl) {
    this.cskl = cskl;
    vrskl = hr.restart.util.Util.getNewQueryDataSet("SELECT VRZAL FROM SKLAD WHERE CSKL='"+cskl+"'").getString("VRZAL");
  }
  public void setGodina(String godina) {
    this.godina = godina;
  }

  public void eraseKartica(){
    kartica = null;
  }
  public void initKartica(){
    Column ckey = dm.getDoki().getColumn("OPIS").cloneColumn();
    ckey.setColumnName("CKEY");
    ckey.setCaption("CKEY");
    Column cdatdok = dm.getDoki().getColumn("DATDOK").cloneColumn();
    Column ckol = dm.getStdoki().getColumn("KOL").cloneColumn();
    Column cvri = dm.getStdoki().getColumn("NC").cloneColumn();
    cvri.setColumnName("VRI");
    cvri.setCaption("Iznos");

    Column cnc = dm.getStdoki().getColumn("NC").cloneColumn();
    Column cncisp = dm.getStdoki().getColumn("NC").cloneColumn();
    cncisp.setColumnName("NC_isp");
    cncisp.setCaption("Ispravan NC");
    Column cvc = dm.getStdoki().getColumn("VC").cloneColumn();
    Column cvcisp = dm.getStdoki().getColumn("VC").cloneColumn();
    cvcisp.setColumnName("VC_isp");
    cvcisp.setCaption("Ispravan VC");
    Column cmc = dm.getStdoki().getColumn("MC").cloneColumn();
    Column cmcisp = dm.getStdoki().getColumn("MC").cloneColumn();
    cmcisp.setColumnName("MC_isp");
    cmcisp.setCaption("Ispravan MC");
    Column czc = dm.getStdoki().getColumn("ZC").cloneColumn();
    Column czcisp = dm.getStdoki().getColumn("ZC").cloneColumn();
    czcisp.setColumnName("ZC_isp");
    czcisp.setCaption("Ispravan ZC");
    Column ckolsaldo = dm.getStdoki().getColumn("KOL").cloneColumn();
    ckolsaldo.setColumnName("KOL_saldo");
    ckolsaldo.setCaption("Kol saldo");
    Column cvrisaldo = dm.getStdoki().getColumn("NC").cloneColumn();
    cvrisaldo.setColumnName("VRI_saldo");
    cvrisaldo.setCaption("Vri saldo");
    Column copis = dm.getDoki().getColumn("OPIS").cloneColumn();
    Column ccskl = dm.getDoki().getColumn("CSKL").cloneColumn();
    Column cvrdok  = dm.getDoki().getColumn("VRDOK").cloneColumn();
    Column cgod  = dm.getDoki().getColumn("GOD").cloneColumn();
    Column cbrdok  = dm.getDoki().getColumn("BRDOK").cloneColumn();
    kartica = new StorageDataSet();
    kartica.setColumns(new Column[]{ckey,cdatdok,ckol,ckolsaldo,cvri,cvrisaldo,cnc,cncisp,cvc,cvcisp,
        cmc,cmcisp,czc,czcisp,copis,ccskl,cvrdok,cgod,cbrdok});
  }

  public void clear(){
    godina = null;
    cskl = null;
    kartica = null;
    cart = null;
  }

  public void test_Cijena_u_Kartici() throws Exception {
    if (godina==null || cskl ==null || cart == null) {
      throw new Exception("Nisu setirani svi parametri provjeri pozive "+
                          "setCart(int cart),setCskl(String cskl),setGodina(String godina)");
    }
    kartica= null;
    initKartica();
    kartica.open();


    String ulazi = "SELECT (stdoku.cskl||'-'||stdoku.vrdok||'-'||stdoku.god||'-'||stdoku.brdok||'-'||stdoku.rbr) as ckey,"+
                   " doku.datdok,stdoku.* FROM Doku,Stdoku "+
                   "WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok "+
                   "AND doku.god = stdoku.god AND doku.brdok = stdoku.brdok "+
                   "AND god='"+getGodina()+"' and cskl='"+getCskl()+"' AND cart="+getCart();

    String izlazi = "SELECT (stdoki.cskl||'-'||stdoki.vrdok||'-'||stdoki.god||'-'||stdoki.brdok||'-'||stdoki.rbr) as ckey,"+
                    "doki.datdok,stdoki.* FROM Doki,Stdoki "+
                   "WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok "+
                   "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok "+
                   "AND god='"+getGodina()+"' and cskl='"+getCskl()+"' AND cart="+getCart();

    String meskla = "SELECT (stmeskla.cskliz||'-'||stmeskla.csklul||'-'||stmeskla.vrdok||'-'||stmeskla.god||'-'||stmeskla.brdok||'-'||stmeskla.rbr) as ckey, "+
                    "meskla.datdok,stmeskla.* FROM meskla,Stmeskla "+
                    "WHERE meskla.cskliz = stmeskla.cskliz AND meskla.csklul = stmeskla.csklul "+
                    "AND meskla.vrdok = stmeskla.vrdok AND meskla.god = stmeskla.god "+
                    "AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok "+
                    "AND god='"+getGodina()+"' and (csklul='"+getCskl()+"' or cskliz='"+getCskl()+"') "+
                    "AND cart="+getCart();

    fillKartica(hr.restart.util.Util.getNewQueryDataSet(ulazi,true));
    fillKartica(hr.restart.util.Util.getNewQueryDataSet(izlazi,true));
    fillKarticaMes(hr.restart.util.Util.getNewQueryDataSet(meskla,true));

    if (getKartica().getRowCount()==0) {
      kartica = null;
      return;
    }

    kartica.setSort(new SortDescriptor(new String[]{"DATDOK"}));
    Analza();

  }

  private void fillKartica(DataSet setzaubaciti){
//ST.prn(setzaubaciti);

    for (setzaubaciti.first();setzaubaciti.inBounds();setzaubaciti.next()){
      kartica.insertRow(true);
      copyDS2DS(setzaubaciti,kartica);
      try {
        kartica.setBigDecimal("VRI",setzaubaciti.getBigDecimal("IZAD"));
      }
      catch (Exception ex) {
//ex.printStackTrace();
        kartica.setBigDecimal("VRI",setzaubaciti.getBigDecimal("IRAZ"));
      }
    }
  }

  private void fillKarticaMes(DataSet setzaubaciti){
//ST.prn(setzaubaciti);
    for (setzaubaciti.first();setzaubaciti.inBounds();setzaubaciti.next()){

      if (setzaubaciti.getString("CSKLUL").equalsIgnoreCase(getCskl())) {
        if (setzaubaciti.getString("VRDOK").equalsIgnoreCase("MES") ||
            setzaubaciti.getString("VRDOK").equalsIgnoreCase("MEU")) {
      kartica.insertRow(true);
      kartica.setString("CKEY",setzaubaciti.getString("CKEY"));
      kartica.setString("CSKL",getCskl());
      kartica.setString("VRDOK",setzaubaciti.getString("VRDOK"));
      kartica.setString("GOD",getGodina());
      kartica.setInt("BRDOK",setzaubaciti.getInt("BRDOK"));
      kartica.setTimestamp("DATDOK",setzaubaciti.getTimestamp("DATDOK"));
      kartica.setBigDecimal("KOL",setzaubaciti.getBigDecimal("KOL"));
      kartica.setBigDecimal("NC",setzaubaciti.getBigDecimal("NC"));
      kartica.setBigDecimal("VC",setzaubaciti.getBigDecimal("VC"));
      kartica.setBigDecimal("MC",setzaubaciti.getBigDecimal("MC"));
      kartica.setBigDecimal("ZC",setzaubaciti.getBigDecimal("ZCUL"));
      kartica.setBigDecimal("VRI",setzaubaciti.getBigDecimal("ZADRAZUL"));
        }
      } else {
        if (setzaubaciti.getString("VRDOK").equalsIgnoreCase("MES") ||
            setzaubaciti.getString("VRDOK").equalsIgnoreCase("MEI")) {
          kartica.insertRow(true);
          kartica.setString("CKEY",setzaubaciti.getString("CKEY"));
          kartica.setString("CSKL",getCskl());
          kartica.setString("VRDOK",setzaubaciti.getString("VRDOK"));
          kartica.setString("GOD",getGodina());
          kartica.setInt("BRDOK",setzaubaciti.getInt("BRDOK"));
          kartica.setTimestamp("DATDOK",setzaubaciti.getTimestamp("DATDOK"));
          kartica.setBigDecimal("KOL",setzaubaciti.getBigDecimal("KOL"));
          kartica.setBigDecimal("NC",setzaubaciti.getBigDecimal("NC"));
          kartica.setBigDecimal("VC",setzaubaciti.getBigDecimal("VC"));
          kartica.setBigDecimal("MC",setzaubaciti.getBigDecimal("MC"));
          kartica.setBigDecimal("ZC",setzaubaciti.getBigDecimal("ZC"));
          kartica.setBigDecimal("VRI",setzaubaciti.getBigDecimal("ZADRAZIZ"));
        }
      }
    }
  }

  public void copyDS2DS(DataSet source,DataSet dest) {

    HashMap hm = new HashMap();
    for (int i=0;i<source.getColumns().length;i++){
      hm.put(source.getColumn(i).getColumnName(),source.getColumn(i).getColumnName());
    }

    ArrayList al = new ArrayList();
    for (int i= 0;i<dest.getColumns().length;i++) {
      if (hm.containsKey(dest.getColumn(i).getColumnName())) {
        al.add(dest.getColumn(i).getColumnName());
      }
    }
    String[] polja_za_kopiranje = new String[al.size()];

    for (int i= 0;i<polja_za_kopiranje.length;i++) {
      polja_za_kopiranje[i] = (String) al.get(i);
    }
    dM.copyColumns(source,dest,polja_za_kopiranje);
    hm=null;
    al=null;
    polja_za_kopiranje=null;
  }
  public StorageDataSet getKartica() {
    return kartica;
  }

  public void Analza(){

    TypeDoc TD = TypeDoc.getTypeDoc();
    BigDecimal tmp_kol = Aus.zero2;
    BigDecimal tmp_vri = Aus.zero2;
    BigDecimal pret_nc = Aus.zero2;
    BigDecimal pros_nc = Aus.zero2;
    BigDecimal pret_vc = Aus.zero2;
    BigDecimal pret_mc = Aus.zero2;
    BigDecimal pret_zc = Aus.zero2;

    kartica.first();
    if (!TD.isDocUlaz(kartica.getString("VRDOK"))) {
       kartica.setString("OPIS",kartica.getString("OPIS")+"Prvi dokument izlazni!");
    } else {
      pret_nc = kartica.getBigDecimal("NC");
      pret_vc = kartica.getBigDecimal("VC");
      pret_mc = kartica.getBigDecimal("MC");
      pret_zc = kartica.getBigDecimal("ZC");
    }

 
    for (kartica.first();kartica.inBounds();kartica.next()){
    
      // pocetno stanje mora biti prvo i iznosi su uvijek toèni !!!
      // ako i nije vc*1.22 = mc zanemarujemo to	
    	
    	if (kartica.getString("VRDOK").equalsIgnoreCase("PST")){
    		if (kartica.getBigDecimal("KOL").doubleValue()!=0){
    	          kartica.setBigDecimal("NC_isp",
    	          		kartica.getBigDecimal("INAB").divide(
    	          				kartica.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
    	          kartica.setBigDecimal("VC_isp",
    	          		kartica.getBigDecimal("IBP").divide(
    	          				kartica.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
    	          kartica.setBigDecimal("MC_isp",
    	          		kartica.getBigDecimal("IBP").divide(
    	          				kartica.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
    		} else {
  	          kartica.setBigDecimal("NC_isp",kartica.getBigDecimal("NC"));
	          kartica.setBigDecimal("VC_isp",kartica.getBigDecimal("VC"));
	          kartica.setBigDecimal("MC_isp",kartica.getBigDecimal("MC"));
    		}
    	      pret_nc = kartica.getBigDecimal("NC");
    	      pret_vc = kartica.getBigDecimal("VC");
     	      pret_mc = kartica.getBigDecimal("MC");
  
     	      if ("N".equalsIgnoreCase(vrskl)){
                pret_zc = kartica.getBigDecimal("NC");
                kartica.setBigDecimal("ZC_isp",kartica.getBigDecimal("NC"));
              } else if ("V".equalsIgnoreCase(vrskl)){
                pret_zc = kartica.getBigDecimal("VC");
                kartica.setBigDecimal("ZC_isp",kartica.getBigDecimal("VC"));
              } else if ("M".equalsIgnoreCase(vrskl)){
                pret_zc = kartica.getBigDecimal("MC");
                kartica.setBigDecimal("ZC_isp",kartica.getBigDecimal("MC"));
              }
      	continue;
      }	
    	
      if (TD.isDocDiraZalihu(kartica.getString("VRDOK")) &&
          TD.isDocUlaz(kartica.getString("VRDOK"))){
          tmp_kol = tmp_kol.add(kartica.getBigDecimal("KOL"));
          tmp_vri = tmp_vri.add(kartica.getBigDecimal("VRI"));
          kartica.setBigDecimal("KOL_saldo",tmp_kol);
          kartica.setBigDecimal("VRI_saldo",tmp_vri);
          pret_nc = kartica.getBigDecimal("NC");
          pret_vc = kartica.getBigDecimal("VC");
          pret_mc = kartica.getBigDecimal("MC");
          pros_nc = tmp_vri.divide(tmp_kol,2,BigDecimal.ROUND_HALF_UP);
          if ("N".equalsIgnoreCase(vrskl)){
            pret_zc = pros_nc;
            kartica.setBigDecimal("ZC_isp",kartica.getBigDecimal("NC"));
          } else if ("V".equalsIgnoreCase(vrskl)){
            pret_zc = kartica.getBigDecimal("VC");
            kartica.setBigDecimal("ZC_isp",kartica.getBigDecimal("VC"));
            if (kartica.getBigDecimal("ZC").compareTo(pret_zc)!=0){
              setOpis("Neispravan ZC");
            }
          } else if ("M".equalsIgnoreCase(vrskl)){
            pret_zc = kartica.getBigDecimal("MC");
            kartica.setBigDecimal("ZC_isp",kartica.getBigDecimal("MC"));
            if (kartica.getBigDecimal("ZC").compareTo(pret_zc)!=0){
              setOpis("Neispravan ZC");
            }
          }
          kartica.setBigDecimal("NC_isp",pret_nc);
          kartica.setBigDecimal("VC_isp",pret_vc);
          kartica.setBigDecimal("MC_isp",pret_mc);
     } else if (TD.isDocDiraZalihu(kartica.getString("VRDOK")) &&
          !TD.isDocUlaz(kartica.getString("VRDOK"))){
         tmp_kol = tmp_kol.subtract(kartica.getBigDecimal("KOL"));
         tmp_vri = tmp_vri.subtract(kartica.getBigDecimal("VRI"));
         kartica.setBigDecimal("KOL_saldo",tmp_kol);
         kartica.setBigDecimal("VRI_saldo",tmp_vri);
//         kartica.setBigDecimal("NC_isp",pret_zc);
         kartica.setBigDecimal("NC_isp",pros_nc);
         kartica.setBigDecimal("VC_isp",pret_vc);
         kartica.setBigDecimal("MC_isp",pret_mc);
         kartica.setBigDecimal("ZC_isp",pret_zc);
         if (kartica.getBigDecimal("NC").compareTo(pros_nc)!=0) {
           setOpis("Neispravan NC");
         }
         if (kartica.getBigDecimal("VC").compareTo(pret_vc)!=0) {
           setOpis("Neispravan VC");
         }
         if (kartica.getBigDecimal("MC").compareTo(pret_mc)!=0) {
           setOpis("Neispravan MC");
         }
         if (kartica.getBigDecimal("ZC").compareTo(pret_zc)!=0) {
           setOpis("Neispravan ZC");
         }
      }
      testMinusKolIVri();
    }
  }
  private void setOpis(String text){
      kartica.setString("OPIS",kartica.getString("OPIS")+";"+text);
  }

  public void testMinusKolIVri(){

    if (kartica.getBigDecimal("KOL_saldo").floatValue()<0){
      kartica.setString("OPIS",kartica.getString("OPIS")+";KOL_SALDO < 0");
    }
    if (kartica.getBigDecimal("VRI_saldo").floatValue()<0){
      kartica.setString("OPIS",kartica.getString("OPIS")+";VRI_SALDO < 0");
    }
    if (kartica.getBigDecimal("KOL_saldo").floatValue()==0 &&
        kartica.getBigDecimal("VRI_saldo").floatValue()>0){
      kartica.setString("OPIS",kartica.getString("OPIS")+";KOL_saldo= 0 i VRI_saldo < 0");
    }
    if (kartica.getBigDecimal("KOL_saldo").floatValue()>0 &&
        kartica.getBigDecimal("VRI_saldo").floatValue()==0){
      kartica.setString("OPIS",kartica.getString("OPIS")+";KOL_saldo>0 i VRI_saldo = 0");
    }
 }
}