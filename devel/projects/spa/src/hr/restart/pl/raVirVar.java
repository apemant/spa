/****license*****************************************************************
**   file: raVirVar.java
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

/** Zamjenjeno sa hr.restart.util.raMnemVar
 * @deprecated zamjenjeno sa hr.restart.util.raMnemVar
 */
public abstract class raVirVar {

  private String var;
  private String opis = "";

  /** Zamjenjeno sa hr.restart.util.raMnemVar
   * @deprecated zamjenjeno sa hr.restart.util.raMnemVar
   * @param _var
   * @param _opis
   */  
  public raVirVar(String _var, String _opis) {
    setVar(_var);
    setOpis(_opis);
  }

  /** Zamjenjeno sa hr.restart.util.raMnemVar
   * @param _var
   * @deprecated zamjenjeno sa hr.restart.util.raMnemVar
   */  
  public raVirVar(String _var) {
    this(_var,null);
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public String getOpis() {
    return opis;
  }

  public void setOpis(String _opis) {
    if (_opis!=null) opis = _opis;
  }

  public abstract String getReplaceStr();

}