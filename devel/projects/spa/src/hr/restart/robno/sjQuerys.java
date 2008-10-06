/****license*****************************************************************
**   file: sjQuerys.java
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

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class sjQuerys {

  public sjQuerys() {
  }
  public static String getMaxRbr4Rate(String cskl, String vrdok, String god, int brdok) {
    String cVrati="select max(rbr) from rate where cskl='"+cskl+"' and vrdok='"+vrdok+"' and god='"+god+"' and brdok="+brdok;
//    System.out.println("SQL: "+cVrati);
    return cVrati;
  }
  public static String getNaplac(String cskl, String vrdok, String god, int brdok) {
    String cVrati="select sum(irata) from rate where cskl='"+cskl+"' and vrdok='"+vrdok+"' and god='"+god+"' and brdok="+brdok;
//    System.out.println("SQL: "+cVrati);
    return cVrati;
  }

}