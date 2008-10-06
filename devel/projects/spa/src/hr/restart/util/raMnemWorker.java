/****license*****************************************************************
**   file: raMnemWorker.java
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
/*
 * raMnemWorker.java
 *
 * Created on 2003. studeni 05, 12:31
 */

package hr.restart.util;
import java.util.HashMap;
/** Abstraktna klasa koja se koristi za mnemonic skalameriju
 * @see raMnemonics
 * @author andrej
 */
public class raMnemWorker {
  private HashMap mnemVars = new HashMap();
  private String id;
  /** Creates a new instance of raMnemWorker
   * @param _id ID toga workera za kasniji dohvat njega i njegovih varijabli
   */
  public raMnemWorker(String _id) {
    id = _id;
  }
  
  HashMap getVars() {
    return mnemVars;
  }
  
  /** Jedinstvena oznaka workera
   * @return toga workera za dohvat njega i njegovih varijabli u raMnemonics
   */  
  public String getID() {
    return id;
  }
  /** Dodaje mnemonic u listu mnemonica od tog workera
   * @param var - mnemonic koji treba dodati
   * @return isti taj mnemonic tako da bi kod izgledao zgodno npr:
   * <pre>
   * raMnemWorker worker = raMnemonics.addWorker(new raMnemWorker("lozhachi"));
   * raMnemVar pero = worker.add(new raMnemVar("pero","Pero Lozach") {
   *    public String getText() {
   *      //bla bla bla
   *    }
   * }
   * </pre>
   */
  public raMnemVar addVar(raMnemVar var) {
    mnemVars.put(var.getName(), var);
    return var;
  }
}
