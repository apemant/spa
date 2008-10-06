/****license*****************************************************************
**   file: GrammarFactory.java
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
package hr.restart.sisfun;

import java.io.InputStream;
import java.util.StringTokenizer;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class GrammarFactory {

  private GrammarFactory() {
  }

  public static Grammar create(String fname) {
    return create(TextFile.read(fname).check());
  }

  public static Grammar create(InputStream is) {
    return create(TextFile.read(is).check());
  }

  public static Grammar create(TextFile tf) {
    Grammar g = new Grammar("Unnamed");
    String line;
    while ((line = tf.in()) != null) {
      if (line.trim().length() > 0) {
        String rule = new StringTokenizer(line, ":").nextToken().trim();
        g.removeFinal(rule);
        while ((line = tf.in()) != null && line.trim().length() > 0) {
          StringTokenizer st = new StringTokenizer(line);
          String[] elems = new String[st.countTokens()];
          for (int i = 0; st.hasMoreTokens(); g.addFinal(elems[i++] = st.nextToken()));
          g.addRule(rule, elems);
        }
      }
    }
    g.findRoot();
    g.findFinalsFromRules();
    g.findRecursiveRules();
    return g;
  }
}

