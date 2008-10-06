/****license*****************************************************************
**   file: test.java
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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class test {

  public test() {
System.out.println("getin'");
    raCalcPrimanja rcp = raCalcPrimanja.getRaCalcPrimanja();
System.out.println("parsin' ");
    rcp.parseValues("<radnicipl.brutuk>*(<primanjaobr.sati>/<orgpl.satimj>)*<primanjaobr.koef>/100.00");
    System.exit(0);
  }
  public static void main(String[] args) {
    test test1 = new test();
  }
}