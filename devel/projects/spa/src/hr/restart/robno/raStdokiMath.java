/****license*****************************************************************
**   file: raStdokiMath.java
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
import java.math.BigDecimal;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raStdokiMath {

  private hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private String cskl = "";
  private String god = "";
  private String vrstazalihe="";
  private String vrdok = "";
  private String cshrab = "";
  private String cshzt = "";
  private BigDecimal postoporeza_1 ;
  private boolean pornapor1 = false ;
  private BigDecimal postoporeza_2 ;
  private boolean pornapor2 = false ;
  private BigDecimal postoporeza_3 ;
  private boolean pornapor3 = false ;
  private BigDecimal uprab;
  private BigDecimal upzt;
  private BigDecimal izzt;
  private QueryDataSet Stavka;
  private allStanje AST = new allStanje();
  public raStdokiMath() {
  }
  /**
   * tu treba inicializirati vrstu zalihe i vrstu dokumenta i potrebito je inicirati samo
   * jednom kod višestrukog dodavanja, rabate (shemu), zavisne troskove (shemu)
   * @param cskl
   * @param vrdok
   * @param god
   * @param cshrab šema rabata
   * @param uprab
   * @param cshzt šema zavisnih troškova
   * @param upzt
   * @return int = 0 ako je dobro, -1 ako nema skladista, -2 ako je greska u vrdoku
   */
  public int initMathCommon(String cskl,String vrdok,String god,
                            String cshrab, BigDecimal uprab,
                            String cshzt , BigDecimal upzt){

    if (!(lD.raLocate(dm.getSklad(),
             new String[]{"CSKL"},
             new String[]{cskl}))) return -1;
    vrstazalihe= dm.getSklad().getString("VRZAL");
    this.cskl = cskl;
    this.god = god;
    this.vrdok = vrdok; // vidjeti ce mo sto ce mo poslije s tim
    this.cshrab = cshrab;
    this.uprab = uprab;
    this.cshzt = cshzt;
    this.upzt = upzt;
    if  (lD.raLocate(dm.getShrab(),
             new String[]{"CSHRAB"},
             new String[]{cshrab})) {
          this.uprab = dm.getShrab().getBigDecimal("UPRAB");
    }
    if  (lD.raLocate(dm.getShzavtr(),
             new String[]{"CSHZT"},
             new String[]{cshrab})) {
          this.upzt = dm.getShrab().getBigDecimal("ZTPUK");
          this.izzt = dm.getShrab().getBigDecimal("ZTIUK");   // iznos koji se dodaje na svaku stavku ????
    }
    return 0;
  }
  /**
   * ovdje treba inicirati za vrijednosti artikla i to na\u0107i porezne stope i cijene
   * u ovisnosti o partneru, i ostalom
   * @param stavka
   * @param cpar
   * @param cart
   * @param kolicina
   */
  public void initMathCart(QueryDataSet stavka,int cart,int cpar,BigDecimal kolicina){
    this.Stavka = stavka;
    if (lD.raLocate(dm.getArtikli(),
                     new String[]{"CART"},
                     new String[]{String.valueOf(cart)}))
    {
      Stavka.setInt("CART",cart);
      Stavka.setString("CART1",dm.getArtikli().getString("CART1"));
      Stavka.setString("BC",dm.getArtikli().getString("BC"));
      Stavka.setString("NAZART",dm.getArtikli().getString("NAZART"));
      Stavka.setString("JM",dm.getArtikli().getString("JM"));
      /* ovo je setiranje cijena u ovisnosti ako nema kop_arta i */

      Stavka.setBigDecimal("FC",dm.getArtikli().getBigDecimal("VC"));
      Stavka.setBigDecimal("FVC",dm.getArtikli().getBigDecimal("VC"));
      Stavka.setBigDecimal("FMC",dm.getArtikli().getBigDecimal("MC"));

    }
    if (lD.raLocate(dm.getPorezi(),
                     new String[]{"CPOR"},
                     new String[]{dm.getArtikli().getString("CPOR")}))
    {
        postoporeza_1 = dm.getPorezi().getBigDecimal("POR1");
        pornapor1 = dm.getPorezi().getString("PORNAPOR1").equals("D");
        postoporeza_2 = dm.getPorezi().getBigDecimal("POR2");
        pornapor2 = dm.getPorezi().getString("PORNAPOR2").equals("D");
        postoporeza_3 = dm.getPorezi().getBigDecimal("POR3");
        pornapor3 = dm.getPorezi().getString("PORNAPOR3").equals("D");
    }

    if (!dm.getArtikli().getString("VRART").equals("U")){
       AST.findStanjeUnconditional(god,cskl,cart);
       if (!AST.gettrenSTANJE().isEmpty())
        {
          Stavka.setBigDecimal("FC",AST.gettrenSTANJE().getBigDecimal("VC"));
          Stavka.setBigDecimal("FVC",AST.gettrenSTANJE().getBigDecimal("VC"));
          Stavka.setBigDecimal("FMC",AST.gettrenSTANJE().getBigDecimal("MC"));
          Stavka.setBigDecimal("NC",AST.gettrenSTANJE().getBigDecimal("NC"));
          Stavka.setBigDecimal("VC",AST.gettrenSTANJE().getBigDecimal("VC"));
          Stavka.setBigDecimal("MC",AST.gettrenSTANJE().getBigDecimal("MC"));
          Stavka.setBigDecimal("ZC",AST.gettrenSTANJE().getBigDecimal("ZC"));
        }
     }
    /**
     * kad se objedini cijenik i kup art ovaj dio koda se mijenja
     */
    if (lD.raLocate(dm.getCjenik(),new String[] {"CPAR","CART"},
        new String[] {String.valueOf(cpar),String.valueOf(cart)}))
    {
      Stavka.setBigDecimal("FC",dm.getCjenik().getBigDecimal("VC"));
      Stavka.setBigDecimal("FVC",dm.getCjenik().getBigDecimal("VC"));
      Stavka.setBigDecimal("FMC",dm.getCjenik().getBigDecimal("MC"));
    }
    /* findaj rabat iz kuparta */
    if (cshrab.equals("")) {
      if (lD.raLocate(dm.getKup_art(),new String[] {"CPAR","CART"},
        new String[] {String.valueOf(cpar),String.valueOf(cart)}))
      {
        Stavka.setBigDecimal("UPRAB",dm.getKup_art().getBigDecimal("PRAB"));
        cshrab = "";
        uprab = dm.getKup_art().getBigDecimal("PRAB");
      }
    }
    Stavka.setBigDecimal("KOL",kolicina);
  }
  /**
   * Kalkulacija skladišnog dijela stdokija
   */
  public void calcMathSklad(){
//    stavka.inab = stavka.kol * stavka.nc;
    Stavka.setBigDecimal("INAB",
            Stavka.getBigDecimal("KOL").multiply(Stavka.getBigDecimal("NC")));
//    stavka.ibp  = stavka.vc * stavka.kol;
    Stavka.setBigDecimal("IBP",
            Stavka.getBigDecimal("KOL").multiply(Stavka.getBigDecimal("VC")));
//    stavka.isp  = stavka.mc * stavka.kol;
    Stavka.setBigDecimal("ISP",
            Stavka.getBigDecimal("KOL").multiply(Stavka.getBigDecimal("MC")));
//    stavka.iraz = stavka.zc * stavka.kol;
    Stavka.setBigDecimal("IRAZ",
            Stavka.getBigDecimal("KOL").multiply(Stavka.getBigDecimal("ZC")));
//    stavka.imar = stavka.ibp - stavka.inab;
    Stavka.setBigDecimal("IMAR",
            Stavka.getBigDecimal("IBP").subtract(Stavka.getBigDecimal("INAB")));
//    stavka.ipor = stavka.isp - stavka.ibp;
    Stavka.setBigDecimal("IPOR",
            Stavka.getBigDecimal("ISP").subtract(Stavka.getBigDecimal("IBP")));
  }
  /**
   * Kalkulacija financijskog dijela stdokija
   */
  public void calcMathFinanc(){
    if (Stavka.getBigDecimal("KOL").doubleValue()!=0) {
      BigDecimal tmpBD ;
      Stavka.setBigDecimal("UPRAB",uprab);
      Stavka.setBigDecimal("UPZT",upzt);
      // ineto = kol * fc
      Stavka.setBigDecimal("INETO",
             Stavka.getBigDecimal("KOL").multiply(Stavka.getBigDecimal("FC")));
      // uirab = ineto*uprab /100 ....zaokruženo na 2 decimale
      Stavka.setBigDecimal("UIRAB",
             (Stavka.getBigDecimal("INETO").multiply(Stavka.getBigDecimal("UPRAB"))).
             divide(BigDecimal.valueOf((long) 100),2,BigDecimal.ROUND_HALF_UP));
      // uizt = (ineto-uirab)*upzt / 100 ....zaokruženo na 2 decimale
      tmpBD=Stavka.getBigDecimal("INETO").subtract(Stavka.getBigDecimal("UIRAB"));
      Stavka.setBigDecimal("UIZT",
             (tmpBD.multiply(Stavka.getBigDecimal("UPZT"))).
             divide(BigDecimal.valueOf((long) 100),2,BigDecimal.ROUND_HALF_UP));
      // iprodbp = ineto - uirab + uizt
      Stavka.setBigDecimal("IPRODBP",tmpBD.add(Stavka.getBigDecimal("UIZT")));
      // fvc = iprodbp / kol
      Stavka.setBigDecimal("FVC",
             Stavka.getBigDecimal("IPRODBP").
             divide(Stavka.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));


      tmpBD=Stavka.getBigDecimal("IPRODBP");

      Stavka.setBigDecimal("POR1",
             (Stavka.getBigDecimal("IPRODBP").multiply(postoporeza_1)).
             divide(BigDecimal.valueOf((long) 100),2,BigDecimal.ROUND_HALF_UP));
      if (pornapor2) tmpBD=Stavka.getBigDecimal("IPRODBP").
                           add(Stavka.getBigDecimal("POR1"));
      Stavka.setBigDecimal("POR2",
             (tmpBD.multiply(postoporeza_2)).
             divide(BigDecimal.valueOf((long) 100),2,BigDecimal.ROUND_HALF_UP));
      if (pornapor2) tmpBD=Stavka.getBigDecimal("IPRODBP").
                           add(Stavka.getBigDecimal("POR1").
                           add(Stavka.getBigDecimal("POR2")));
      Stavka.setBigDecimal("POR3",
             (tmpBD.multiply(postoporeza_3)).
             divide(BigDecimal.valueOf((long) 100),2,BigDecimal.ROUND_HALF_UP));

      tmpBD=Stavka.getBigDecimal("IPRODBP").add(Stavka.getBigDecimal("POR1"));
      tmpBD=tmpBD.add(Stavka.getBigDecimal("POR2"));
      tmpBD=tmpBD.add(Stavka.getBigDecimal("POR3"));
      Stavka.setBigDecimal("IPRODSP",tmpBD);
      // fmc = iprodsp / kol
      Stavka.setBigDecimal("FMC",
             Stavka.getBigDecimal("IPRODSP").
             divide(Stavka.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
    }
  }
}