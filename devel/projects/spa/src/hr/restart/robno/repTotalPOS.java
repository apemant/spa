/****license*****************************************************************
**   file: repTotalPOS.java
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

import hr.restart.util.reports.mxReport;

public class repTotalPOS extends mxReport {
  java.math.BigDecimal ukupno = _Main.nul;
  java.math.BigDecimal subsum = _Main.nul;
  Redak redak = new Redak();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  String[] detail = {"<#red|41|center#>"};
  public repTotalPOS() {
    this.setDataObject(redak);
    this.setDetail(detail);
  }
  public void makeReport() {

    String stavk = "";
    boolean totale = false;
    boolean zaglavlje = false;

    this.start();

    redak.red = "---------------------------------------";
    this.add();
    redak.red = "T O T A L";
    this.add();
    redak.red = "---------------------------------------";
    this.add();
    if (ispPOS_Total.getispPOS_Total().getRedOdDoDatuma()){
      redak.red = "od "+ispPOS_Total.getispPOS_Total().tds.getTimestamp("pocDatum").toString().substring(0,10)+" do "+ispPOS_Total.getispPOS_Total().tds.getTimestamp("zavDatum").toString().substring(0,10);
    } else {
      redak.red = "od "+String.valueOf(ispPOS_Total.getispPOS_Total().tds.getInt("pocBroj"))+" do "+String.valueOf(ispPOS_Total.getispPOS_Total().tds.getInt("zavBroj"));
    }
    this.add();
    redak.red = "---------------------------------------";
    this.add();
    ispPOS_Total.getispPOS_Total().tempDs.open();
    ispPOS_Total.getispPOS_Total().tempDs.first();
    ukupno = _Main.nul;
    for (int i=0; i<ispPOS_Total.getispPOS_Total().tempDs.rowCount(); i++) {

//      if (!stavk.equals(ispPOS_Total.getispPOS_Total().tempDs.getString("NPL"))){
//        System.out.println("totale - TRUE");
//        totale = true;
//      }

//      System.out.println("DATASET:   " + ispPOS_Total.getispPOS_Total().tempDs);

      if (ispPOS_Total.getispPOS_Total().tempDs.getString("banka").equals("")) {
        stavk = ispPOS_Total.getispPOS_Total().tempDs.getString("NPL");
//        System.out.println("stavk " + stavk);
        if (totale) {
          redak.red = " --------------------------------------";
          this.add();
          redak.red = " Ukupno                          " + subsum;
          this.add();
          redak.red = "---------------------------------------";
          this.add();
//          System.out.println("totale - FALSE");
//          System.out.println("zaglavlje - FALSE");
          totale = false;
          subsum = _Main.nul;
        }
        redak.red=util.getStr(ispPOS_Total.getispPOS_Total().tempDs.getString("NAZIV"), 26, util.STR_LEFT) +  "            ";
        this.add();
        if (ispPOS_Total.getispPOS_Total().tempDs.getString("NPL").equals("G") || ispPOS_Total.getispPOS_Total().tempDs.getString("NPL").equals("V")){
          redak.red = " --------------------------------------";
          this.add();
          redak.red = "Ukupno                   " + util.getStr(ispPOS_Total.getispPOS_Total().tempDs.getBigDecimal("IZNOS").toString(), 13, util.STR_RIGHT);
          this.add();
          redak.red = "---------------------------------------";
          this.add();
        }

        ukupno=ukupno.add(ispPOS_Total.getispPOS_Total().tempDs.getBigDecimal("IZNOS"));

      } else {
        redak.red="  "+util.getStr(ispPOS_Total.getispPOS_Total().tempDs.getString("NAZIV"), 24, util.STR_LEFT)+
                  util.getStr(ispPOS_Total.getispPOS_Total().tempDs.getBigDecimal("IZNOS").toString(), 13, util.STR_RIGHT);
        this.add();
        subsum = subsum.add(ispPOS_Total.getispPOS_Total().tempDs.getBigDecimal("IZNOS"));
        totale = true;

      }
      ispPOS_Total.getispPOS_Total().tempDs.next();
    }

    if (totale) {
      redak.red = " --------------------------------------";
      this.add();
      redak.red = " Ukupno                          " + subsum;
      this.add();
      redak.red = "---------------------------------------";
      this.add();
//      System.out.println("totale - FALSE");
//      System.out.println("zaglavlje - FALSE");
      totale = false;
      subsum = _Main.nul;
    }

    redak.red=util.getStr("Ukupno", 24, util.STR_LEFT)+util.getStr(ukupno.toString(), 15, util.STR_RIGHT);
    this.add();

    if (ispPOS_Total.getispPOS_Total().jCheckBox1.isSelected()) {
      redak.red = "---------------------------------------";
      this.add();
      redak.red="";
      this.add();
      this.add();
      redak.red = " Artikl                        Kolièina";
      this.add();
      redak.red = "---------------------------------------";
      this.add();
      ispPOS_Total.getispPOS_Total().tempAs.open();
      ispPOS_Total.getispPOS_Total().tempAs.first();
      for (int i=0; i<ispPOS_Total.getispPOS_Total().tempAs.rowCount(); i++) {
        System.out.println("first: '"+util.getStr(ispPOS_Total.getispPOS_Total().tempAs.getString(1), 25, util.STR_LEFT)+"'");
        System.out.println("secco: '"+ispPOS_Total.getispPOS_Total().tempAs.getBigDecimal(2)+"'");
        redak.red=util.getStr(ispPOS_Total.getispPOS_Total().tempAs.getString(1), 25, util.STR_LEFT)+
                  util.getStr(ispPOS_Total.getispPOS_Total().tempAs.getBigDecimal(2).toString(), 13, util.STR_RIGHT);
        this.add();
        ispPOS_Total.getispPOS_Total().tempAs.next();
      }
      redak.red = "---------------------------------------";
      this.add();
    }
    this.finish();
  }
  public class Redak {
    String red;
    public String getRed() {
      return red;
    }
  }
}
