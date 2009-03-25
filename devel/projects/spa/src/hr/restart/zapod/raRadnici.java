/****license*****************************************************************
**   file: raRadnici.java
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

import hr.restart.baza.Condition;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 *Description of the Class
 *
 *@author   S.G.
 *@created  2003. lipanj 27
 */
public class raRadnici {
  private static String corg;
  private static String knjig;

  private static QueryDataSet radnici;

  protected raRadnici() {}

  /**
   *@return  QueryDataSet sa svim radnicima iz svih organizacijskih jedinica koje su ispod tekuceg knjigovodstva.
   */
  public static QueryDataSet getRadniciFromCurrentKnjig() {
    return getRadniciFromKnjig(OrgStr.getKNJCORG());
  }

  /**
   *@param co  Organizacijska jedinica unutar koje se traze radnici
   *@return   QueryDataSet sa svim radnicima unutar zadane organizacijske jedinice.
   */
  public static QueryDataSet getRadniciFromCorg(String co) {
    if (radnici != null && corg.equals(co)) {
      return radnici;
    }
    corg = co;
    knjig = null;
    radnici = hr.restart.baza.Radnici.getDataModule().getFilteredDataSet("CORG = '" + co + "'");
    return radnici;
  }

  /**
   *@param knj Knjigovodstvo za koji se traze radnici.
   *@return    QueryDataSet sa svim radnicima iz svih organizacijskih jedinica koje su ispod zadanog knjigovodstva.
   */
  public static QueryDataSet getRadniciFromKnjig(String knj) {
    if (radnici != null && corg == null && knj.equals(knjig)) {
      return radnici;
    }
    corg = null;
    knjig = knj;
    //String inQuery = OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndKnjig(knj));
    radnici = hr.restart.baza.Radnici.getDataModule().getFilteredDataSet(
        Condition.in("CORG", OrgStr.getOrgStr().getOrgstrAndKnjig(knj))
        //"CORG in " + inQuery
        );
    return radnici;
  }
}