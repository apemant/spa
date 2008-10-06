/****license*****************************************************************
**   file: pokusable.java
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
package hr.restart.baza;
import java.util.ArrayList;

public class pokusable {

  public ArrayList anagram(String word) {
    ArrayList anagrams = new ArrayList();

    for (int i = 0; i < word.length(); i++) {
      ArrayList anag1 = anagram(word.substring(0, i) + word.substring(i + 1, word.length()));
      for (int e = 0; e < anag1.size(); e++)
        anag1.set(e, word.substring(i, i + 1) + anag1.get(e));
      anagrams.addAll(anag1);
    }
    if (anagrams.size() == 0)
      anagrams.add("");
    return anagrams;
  }
}
