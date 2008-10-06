/****license*****************************************************************
**   file: raMiscTests.java
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

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raMiscTests {
  private static raMiscTests rMT;
  public raMiscTests() {}

  public static raMiscTests getRaMiscTest(){
    if (rMT == null) rMT = new raMiscTests();
    return rMT;
  }

  public QueryDataSet getSkladSet(String cskl){

    return  hr.restart.util.Util.getNewQueryDataSet(
         "SELECT * FROM Sklad WHERE cskl = '"+cskl+"'",true);

  }

  public boolean isRadUDvijeGodine(String cskl){
    return getSkladSet(cskl).getString("STATRAD").equalsIgnoreCase("D");
  }

  public String getGodinaRad(String cskl) {
      return getSkladSet(cskl).getString("GODINA");
  }


  public boolean isSkladMaterijalno(String cskl){
    return getSkladSet(cskl).getString("TIPSKL").equalsIgnoreCase("M");
  }

  public boolean isSkladMatAndObrac(String cskl){
      return (getSkladSet(cskl).getString("TIPSKL").equalsIgnoreCase("M")
              && !getSkladSet(cskl).getString("NACOBR").equalsIgnoreCase("P"));
  }

}