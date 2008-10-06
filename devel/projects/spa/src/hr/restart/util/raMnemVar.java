/****license*****************************************************************
**   file: raMnemVar.java
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
 * raMnemVar.java
 *
 * Created on 2003. studeni 05, 12:40
 */

package hr.restart.util;

/** Klasa koja reprezentira varijablu ciju vrijednost zamijenjujemo u textovima
 * @see raMnemonics
 * @author andrej
 */
public abstract class raMnemVar {
  private String name;
  private String description;
  /** Konstruira raMnemVar
   * @param _name Ime varijable. bitno da bude unique na nivou raMnemWorkera
   * @param _desc Opis koji se vidi na ekranu za get
   * @see raMnemWorker
   * @see raMnemonics
   */  
  public raMnemVar(String _name, String _desc) {
    name = _name;
    description = _desc;
  }
  
  /** Vraca naziv varijable zadan u konstruktoru
   * @return naziv varijable zadane u konstruktoru
   */  
  public String getName() {
    return name;
  }
  
  /** Vraca opis varijable zadan u konstruktoru
   * @return opis varijable zadan u konstruktoru
   */  
  public String getDescription() {
    return description;
  }
  
  /** Metoda koja u datom trenutku treba vratiti replacement za varijablu u textu
   * @return replacement za varijablu u textu
   * @see raMnemonics.replaceMnemonics
   */  
  public abstract String getText();
  
}
