/****license*****************************************************************
**   file: SyntaxTree.java
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
import java.util.ArrayList;
import java.util.HashSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class SyntaxTree {
  Tokenizer tok;
  Grammar gram;
  HashSet identifiers;
  HashSet literals;
  Node root, current;
  Rule currentRule;
  Rule.Case currentCase;
  int currentLine, currentPosition;

  public SyntaxTree(Grammar g) {
    this(g, new DefaultTokenizer());
  }

  public SyntaxTree(Grammar g, Tokenizer t) {
    gram = g;
    tok = t;
    identifiers = new HashSet();
    literals = new HashSet();
    currentRule = gram.getRootRule();
    currentCase = currentRule.getCase(0);
    currentPosition = 0;
    root = current = new Node(currentCase);
  }

  public void clear() {
    root = null;
    currentLine = 0;
  }

  private boolean actualCompatible(Rule.Case c, int pos, Node n) {
//    if (n.parent.children.size() == pos)
    return true;
  }

  private Rule.Case traceFinal(Token t) {
    Rule.Case c = traceFinalFromCase(currentCase, convertToken(t), currentPosition);
    if (c != null || currentPosition == 0) return c;
    for (c = currentCase.next(); c != null; c = c.next())
      if (!c.compatibleWith(currentCase, currentPosition) &&
          actualCompatible(c, currentPosition, current))
        return c;
    return null;
  }

  private Rule.Case traceFinalFromCase(Rule.Case src, String ch, int upToPosition) {
    do {
      if (src.getElemCount() > upToPosition) {
        String e = src.getElem(upToPosition);
        if (gram.isFinal(e) && e.equals(ch) ||
            gram.tasRule(e) && gram.getRule(e).startsWithFinal(ch))
          break;
      }
    } while ((src = src.nextCompatible(upToPosition)) != null);
    return src;
  }

  private void createSubtree(Token t) {
    String name = currentCase.getElem(currentPosition);
    Object elem = gram.isRule(name) ? (Object) gram.getRule(name).getCase(0) : (Object) t;
    if (currentPosition++ == 0) {
      current.elem = currentCase;
      current = current.addChild(new Node(elem));
    } else
      current = current.parent.addChild(new Node(elem));
    if (!gram.isRule(name)) return;
    gram.tasRule(name);
    currentCase = traceFinalFromCase((Rule.Case) elem, convertToken(t), currentPosition = 0);
    createSubtree(t);
  }

  private boolean insertRecursion(Rule.Case c, Token t) {
    Node recur = new Node(current.parent.elem);
    current.parent.elem = c;
    recur.addChild(current.parent.setChild(0, recur));
    current = recur;
    currentCase = c;
    currentPosition = 1;
    return searchForwards(t);
  }

  private boolean searchBackwards(Token t) {
    System.out.print("searchBackwards: ");
    currentCase.dump(currentPosition);
    Rule.Case c = currentCase;

    // find a completed production case
    while (c != null && c.getElemCount() > currentPosition)
      c = c.nextCompatible(currentPosition);

    if (c != null) {  // found
      // check for recursive productions
      if (c.getRule().isRecursive()) {
        Rule.Case r = traceFinalFromCase(c.getRule().getRecursiveCase(), convertToken(t), 1);
        if (r != null) return insertRecursion(r, t);
      }
      if (current.parent.parent == null) return false;
      current = current.parent;
      current.elem = c;
      current.resolve();
      currentCase = (Rule.Case) current.parent.elem;
      currentPosition = current.position + 1;
      if (searchForwards(t)) return true;
      else return searchBackwards(t);
    } else
      System.out.println("Not at the end??");
    return false;
  }

  private String convertToken(Token t) {
    if (t.isLiteral()) return Grammar.LITERAL;
    if (t.isIdent() && !gram.isFinal(t.getValue())) return Grammar.IDENTIFIER;
    return t.getValue();
  }

  private boolean searchForwards(Token t) {
    System.out.print("searchForwards:  ");
    currentCase.dump(currentPosition);
    Rule.Case c = traceFinalFromCase(currentCase, convertToken(t), currentPosition);

    if (c != null) {
      currentCase = c;
      gram.clearMarks();
      createSubtree(t);
    } else System.out.println("  - not found |"+convertToken(t)+"|");
    return c != null;
  }

  public void parse(String line) {
    Token t;
    ++currentLine;
    tok.newLine(line);
    while ((t = tok.next()) != null)
      if (!searchForwards(t))
        searchBackwards(t);
  }

  public void parse(TextFile tf) {
    String line;
    current = root;
    while ((line = tf.in()) != null)
      parse(line);
  }

  public void parse(InputStream is) {
    parse(TextFile.read(is).check());
  }

  public void dump() {
    root.dump();
  }

  public class Node {
    private Node parent;
    private ArrayList children;
    private int position;

    private Object elem;

    boolean resolved;

    private Node(Object elem) {
      this.elem = elem;
      resolved = false;
      if (elem instanceof Rule.Case)
        children = new ArrayList();
    }

    private Node addChild(Node child) {
      child.parent = this;
      child.position = children.size();
      children.add(child);
      return child;
    }

    private Node setChild(int i, Node newChild) {
      Node oldChild = (Node) children.get(i);
      oldChild.parent = null;
      newChild.parent = this;
      newChild.position = i;
      children.set(i, newChild);
      return oldChild;
    }

    private void resolve() {
      resolved = true;
      if (children != null)
        for (int i = 0; i < children.size(); i++)
          if (!((Node) children.get(i)).resolved)
            ((Node) children.get(i)).resolve();
    }

    public void dump() {
      dump(0);
    }

    private void dump(int tab) {
      for (int i = 0; i < tab; i++) System.out.print(" ");
      if (elem instanceof Token)
        System.out.println(elem + "("+position+")");
      else System.out.println(((Rule.Case) elem).getRule().getName() + "(" + position + "): "+
                              ((Rule.Case) elem).getOrdinal());
      if (children != null)
        for (int i = 0; i < children.size(); i++)
          ((Node) children.get(i)).dump(tab + 2);
    }
  }
}
