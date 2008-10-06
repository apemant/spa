/****license*****************************************************************
**   file: Rule.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class Rule {
  private String name;
  private ArrayList cases;
  private HashSet directFinals;

  // breadth-first flag
  private boolean mark, recursion;

  public Rule(String name) {
    this.name = name;
    cases = new ArrayList();
    directFinals = new HashSet();
  }

  void addDirectFinal(String ch) {
    directFinals.add(ch);
  }

  public boolean startsWithFinal(String ch) {
    return directFinals.contains(ch);
  }

  boolean isMarked() {
    return mark;
  }

  void setMark() {
    mark = true;
  }

  void clearMark() {
    mark = false;
  }

  void setRecursive() {
    recursion = true;
  }

  public boolean isRecursive() {
    return recursion;
  }

  public Case getRecursiveCase() {
    for (int i = 0; i < getCaseCount(); i++)
      if (getCase(i).getElem(0).equals(name))
        return getCase(i);
    return null;
  }

  public String getName() {
    return name;
  }

  public void add(String[] elems) {
    cases.add(new Case(elems));
  }

  public int getCaseCount() {
    return cases.size();
  }

  public Case getCase(int i) {
    return (Case) cases.get(i);
  }

  public void dump() {
    System.out.println(name + ":");
    for (int i = 0; i < cases.size(); i++)
      ((Case) cases.get(i)).dump();
    System.out.println("   Direct finals: " + directFinals);
    System.out.println();
  }

  public class Case {
    private String[] elem;
    private int caseOrd;

    public Case(String[] elems) {
      elem = elems;
      caseOrd = getCaseCount();
    }

    public Rule getRule() {
      return Rule.this;
    }

    public int getOrdinal() {
      return caseOrd;
    }

    public Case next() {
      return getCaseCount() > caseOrd + 1 ? getCase(caseOrd + 1) : null;
    }

    public Case nextCompatible(int upToPosition) {
      if (upToPosition == 0) return next();
      Case c = this;
      while ((c = c.next()) != null)
        if (c.compatibleWith(this, upToPosition))
          return c;
      return null;
    }

    public String getElem(int i) {
      return elem[i];
    }

    public int getElemCount() {
      return elem.length;
    }

    public List asList() {
      return Arrays.asList(elem);
    }

    public boolean compatibleWith(Case c, int upToPosition) {
      if (upToPosition > elem.length || upToPosition > c.elem.length)
        return false;
      for (int i = 0; i < upToPosition; i++)
        if (!elem[i].equals(c.elem[i]))
          return false;
      return true;
    }

    public void dump() {
      System.out.print("\t");
      for (int i = 0; i < elem.length; i++)
        System.out.print(elem[i] + " ");
      System.out.println();
    }
    public void dump(int cursor) {
      System.out.print("  ");
      for (int i = 0; i < elem.length; i++)
        System.out.print((cursor == i ? "^" : "") + elem[i] + " ");
      System.out.println(cursor == elem.length ? "^" : "");
    }
  }
}
