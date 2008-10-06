/****license*****************************************************************
**   file: Grammar.java
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class Grammar {
  public static final String IDENTIFIER = "identifier";
  public static final String LITERAL = "literal";
  public static final String STRING_LITERAL = "string-literal";
  public static final String INTEGER_LITERAL = "integer-literal";
  public static final String DECIMAL_LITERAL = "decimal-literal";
//  public static String
  private Rule root;
  private HashMap rules;
  private HashSet chars;
  private HashSet words;
  private String name;

  public Grammar(String name) {
    this.name = name;
    rules = new HashMap();
    chars = new HashSet();
  }

  public Rule getRootRule() {
    return root;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HashMap getRules() {
    return rules;
  }

  public HashSet getChars() {
    return chars;
  }

  public HashSet getWords() {
    return words;
  }

  public Rule addRule(String name, String[] elems) {
    Rule r = (Rule) rules.get(name);
    if (r == null)
      rules.put(name, r = new Rule(name));
    r.add(elems);
    return r;
  }

  public Rule getRule(String name) {
    return (Rule) rules.get(name);
  }

  public void addFinal(String name) {
    if (!rules.containsKey(name)) {
      chars.add(name);
    }
  }

  public boolean tasRule(String name) {
    Rule r = (Rule) rules.get(name);
    if (r == null || r.isMarked()) return false;
    r.setMark();
    return true;
  }

  public boolean isRule(String name) {
    return rules.containsKey(name);
  }

  public boolean isFinal(String name) {
    return chars.contains(name);
  }

  public void removeFinal(String name) {
    chars.remove(name);
//    words.remove(name);
  }

//  void clearMarks(Rule r) {
//    r.clearMark();
//    for (int c = 0; c < r.getCaseCount(); c++)
//      for (int e = 0; e < r.getCase(c).getElemCount(); e++) {
//        String elem = r.getCase(c).getElem(e);
//        if (isRule(elem) && getRule(elem).isMarked())
//          clearMarks(getRule(elem));
//      }
//  }

  void clearMarks() {
    for (Iterator i = rules.values().iterator(); i.hasNext(); )
      ((Rule) i.next()).clearMark();
  }

  void findFinalsFromRule(Rule orig, Rule r) {
    r.setMark();
    for (int i = 0; i < r.getCaseCount(); i++)
      if (tasRule(r.getCase(i).getElem(0)))
        findFinalsFromRule(orig, getRule(r.getCase(i).getElem(0)));
      else orig.addDirectFinal(r.getCase(i).getElem(0));
  }

  void findFinalsFromRule(Rule r) {
    findFinalsFromRule(r, r);
    clearMarks();
  }

  void findFinalsFromRules() {
    for (Iterator i = rules.values().iterator(); i.hasNext(); )
      findFinalsFromRule((Rule) i.next());
  }

  void checkRuleRecursive(Rule r) {
    for (int c = 0; c < r.getCaseCount(); c++)
      for (int i = 0; i < r.getCase(c).getElemCount(); i++) {
        String elem = r.getCase(c).getElem(i);
        if (r.getName().equals(elem)) {
          r.setRecursive();
          return;
        }
      }
  }

  void findRecursiveRules() {
    for (Iterator i = rules.values().iterator(); i.hasNext(); )
      checkRuleRecursive((Rule) i.next());
  }

  void findRoot() {
    HashSet leaves = new HashSet(rules.keySet());
    for (Iterator i = rules.values().iterator(); i.hasNext(); ) {
      Rule r = (Rule) i.next();
      for (int c = 0; c < r.getCaseCount(); c++)
        leaves.removeAll(r.getCase(c).asList());
    }
    for (Iterator i = leaves.iterator(); i.hasNext(); System.out.println(i.next()));
    if (leaves.size() == 0)
      throw new GrammarException("Missing root production");
    if (leaves.size() > 1)
      throw new GrammarException("Multiple root productions");
    root = getRule((String) leaves.iterator().next());
  }

  public void dump() {
    System.out.print("Finals:");
    for (Iterator i = chars.iterator(); i.hasNext(); System.out.print(" " + i.next()));
    System.out.println();
    System.out.println("Productions (root = " + root.getName() + "):");
    for (Iterator i = rules.values().iterator(); i.hasNext(); ((Rule) i.next()).dump());
  }
}
