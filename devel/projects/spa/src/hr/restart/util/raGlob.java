/****license*****************************************************************
**   file: raGlob.java
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
package hr.restart.util;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * <p>Title: raGlob</p>
 * <p>Description: Klasa koja implementira "wildcard" izraze i ekspanziju.
 * Omogucuje provjeravanje odgovara li neki string odredjenom wildcard
 * izrazu (npr. *arh), te uvid u dijelove wildcard izraza koji odgovaraju
 * dijelovima originalnog stringa i njihovu zamjenu. Primjer
 * upotrebe:</p><pre>
 * raGlob arh = new raGlob("*arh");
 * if (arh.matches("kumulradarh")) {   // true
 *   String[] elems = arh.getLastMatchElements();
 *   System.out.println(VarStr.join(elems, ','));   //  -> kumulrad,arh  niz od 2 elementa.
 *   System.out.println(arh.morphLastMatch("*nik"));  //  kumulradnik, vidi opis metode
 * }</pre>
 * <p>Kreiranjem objekta tipa raGlob, wildcard izraz se parsira u elementarne dijelove,
 * kojih ima 4 tipa:
 * <li> * se podudara sa bilo cime, ukljucujuci i prazan string.</li>
 * <li> ? se podudara sa tocno jednim, bilo kojim, znakom.</li>
 * <li> izraz unutar uglatih zagrada [] podudara se sa tocno jednim znakom,
 * koji je ili naveden unutar zagrada, ili je unutar jednog od raspona navedenih
 * unutar zagrada, npr. [a-dhjv-z] podudara se sa bilo kojim od znakova
 * a,b,c,d,h,j,v,w,x,y,z. Ako je prvi znak unutar zagrada usklicnik (!), onda
 * se izraz podudara sa svim znakovima OSIM onih definiranih nakon usklicnika.</li>
 * <li> svi ostali znakovi i nizovi znakova podudaraju se jedino sa samim sobom,
 * npr. "arh" se podudara jedino sa "arh".</li></p>
 * <p>Nekoliko primjera:
 * <li> "*arh" podudara se sa bilo cime sto zavrsava sa arh, ukljucujuci i string "arh".</li>
 * <li> "*b*" podudara sa sa bilo kojim tekstom koji sadrzi slovo b.</li>
 * <li> "[a-zA-Z]??*" podudara se sa bilo kojim tekstom koji pocinje sa slovom
 * i ima najmanje 3 znaka.</li>
 * <li> "???" podudara se sa bilo kojim tekstom koji ima tocno 3 znaka. </li></p>
 *
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raGlob {
  public static final char MULTI = '*';
  public static final char SINGLE = '?';
  public static final char SELECTION_BEGIN = '[';
  public static final char SELECTION_END = ']';
  public static final char ESCAPE = '\\';
  public static final char RANGE = '-';
  public static final char INVERSE = '!';

  private ArrayList glob = new ArrayList();
  private ArrayList parts = new ArrayList();
  private boolean lastMatch = false;
  private String lastString;

  private raGlob() {
  }

  /**
   * Jedini konstruktor. raGlob objekt enkapsulira odredjeni wildcard
   * izraz. Pozivanjem metoda na rezultirajucem raGlob objektu vrsi se
   * provjera podudarnosti tog wildcard izraza sa odredjenim stringovima itd.<p>
   * @param exp wildcard izraz, npr. "*arh", "radnik?.txt", "[a-z]*".
   */
  public raGlob(String exp) {
    parseGlobExpression(exp, glob);
  }

  /**
   * Vraca niz elementarnih dijelova wildcard izraza, npr. za
   * izraz "*arh" elementi su ["*", "arh"], za "radnik?.txt"
   * elementi su ["radnik", "?", ".txt"]. <p>
   * @return niz elementarnih dijelova wildcard izraza.
   */
  public String[] getGlobElements() {
    String[] ret = new String[glob.size()];
    for (int i = 0; i < glob.size(); i++)
      ret[i] = ((GlobElement) glob.get(i)).toString();
    return ret;
  }

  /**
   * Vraca dijelove zadnjeg stringa testiranog metodom match(),
   * koji se podudaraju redom sa odgovarajucim dijelovima
   * wildcard izraza (koje se moze dobiti metodom getGlobElements()).
   * Npr. ako je wildcard izraz "radnik?.txt", nakon poziva
   * metode match("radnik5.txt"), ova metoda ce vratiti niz
   * ["radnik", "5", ".txt"], koji je odgovara nizu elementarnih
   * dijelova wildcard izraza ["radnik", "?", ".txt"].<p>
   * @return dijelove zadnjeg testiranog stringa, ili null ako
   * taj string nije odgovarao wildcard izrazu ovog raGlob objekta.
   */
  public String[] getLastMatchElements() {
    return lastMatch ? (String[]) parts.toArray(new String[parts.size()]) : null;
  }

  /**
   * Najkompleksnija i najmocnija metoda u ovoj klasi, omogucuje
   * preoblikovanje zadnjeg stringa testiranog metodom match()
   * pomocu zadanog wildcard izraza koji mora na odredjeni
   * nacin biti kompatibilan sa izvornim wildcard izrazom
   * ovog raGlob objekta. Pravila "kompatibilnosti" su dosta
   * slozena, zbog cesto potrebnih nacina konverzije. Objasnjavanje
   * svih specificnosti u detalje uzelo bi previse prostora,
   * zato evo samo nekoliko primjera za ilustraciju:<p>
   * <li> new raGlob("*arh").match("skstavkearh").morphLastMatch("*rad") daje "skstavkerad".</li>
   * <li> new raGlob("*arh").match("skstavkearh").morphLastMatch("x*rad") daje "xskstavkerad".</li>
   * <li> new raGlob("*arh").match("skstavkearh").morphLastMatch("[]rad") daje "rad".</li>
   * <li> new raGlob("*arh").match("skstavkearh").morphLastMatch("*[]") daje "skstavke".</li>
   * <li> new raGlob("*?ba*").match("baobab").morphLastMatch("[]?[][]") daje "o".</li>
   * <li> new raGlob("*?ba*").match("baobab").morphLastMatch("*[]ba[]") daje "baba".</li>
   * <li> new raGlob("*?ba*").match("baobab").morphLastMatch("*[]ba*a") daje "bababa".</li><p>
   * @param rule wildcard izraz kompatibilan sa originalnim wildcard izrazom ovog
   * raGlob objekta, po kojem se preoblikuje string testiran zadnjim pozivom metode match().
   * @return preoblikovani string.
   */
  public String morphLastMatch(String rule) {
    if (!lastMatch) return lastString;
    StringBuffer result = new StringBuffer();
    String esel, gsel;
    ArrayList elems = new ArrayList();
    int i, gi;
    GlobElement re, ge;
    parseGlobExpression(rule, elems);
    for (i = 0, gi = 0; i < elems.size() && gi < glob.size(); i++) {
//      if (gi >= glob.size() || i >= elems.size())
//        throw new IllegalArgumentException("Incompatible glob expressions: number of elements ("+
//          rule+"): "+elems.size()+", ("+getOriginalString()+"): "+glob.size());
      re = (GlobElement) elems.get(i);
      ge = (GlobElement) glob.get(gi);
      if (re.isMulti()) {
        if (!ge.isMulti())
          throw new IllegalArgumentException("Incompatible glob expressions: ("+
              rule+"), ("+getOriginalString()+"), "+re+" <=> "+ge);
        result.append(parts.get(gi++));
      } else if (re.isString()) {
        result.append(re);
        if (ge.isString() || ge.isSingle() || ge.isSelection()) ++gi;
      } else if (re.isSingle()) {
        if (!ge.isSingle() && !ge.isSelection())
          throw new IllegalArgumentException("Incompatible glob expressions: ("+
              rule+"), ("+getOriginalString()+"), "+re+" <=> "+ge);
        result.append(parts.get(gi++));
      } else {
        esel = re.expandSelection();
        if (esel.length() == 0) ++gi;
        else {
          if (!ge.isSelection() || (gsel = ge.expandSelection()).length() != esel.length())
            throw new IllegalArgumentException("Incompatible glob expressions: "+
                rule+"), ("+getOriginalString()+"), "+re+" <=> "+ge);
          result.append(esel.charAt(gsel.indexOf(parts.get(i).toString())));
        }
      }
    }
    if (i < elems.size()) {
      result.append(re = (GlobElement) elems.get(i++));
      if (!re.isString()) i = -1;
    }
    if (gi < glob.size() || i < elems.size())
      throw new IllegalArgumentException("Incompatible glob expressions: number of elements ("+
          rule+"): "+elems.size()+", ("+getOriginalString()+"): "+glob.size());

    return result.toString();
  }
  
  public boolean compatibleWith(String rule)
  {
      try {
          match(getTrivialMatch()).morphLastMatch(rule);
          return true;
      } catch(Exception e) {
          return false;
      }
  }

  /**
   * Pokusava izvrsiti podudaranje zadanog stringa sa
   * originalnim wildcard izrazom ovog raGlob objekta (izrazom zadanim
   * u kontruktoru), dakle razbiti zadani string na jednak broj
   * dijelova koliko ih ima i originalni wildcard izraz, s tim
   * da se svaki dio stringa podudara sa odgovarajucim dijelom
   * wildcard izraza. Metoda vraca this, zbog ulancavanja. Ako
   * je potrebno jedino provjeriti odgovara li string wildcard
   * izrazu, bolje je koristiti metodu matches() koja vraca
   * boolean.<p>
   * @param source string koji se testira na wildcard izraz.
   * @return this.
   */
  public raGlob match(String source) {
    matches(source);
    return this;
  }

  /**
   * Provjerava odgovara li zadani string originalnom wildcard izrazu.<p>
   * @param source string koji se testira.
   * @return true ako je podudaranje moguce.
   */
  public boolean matches(String source) {
    lastString = source;
    parts.clear();
    parts.addAll(glob);
    if (glob.size() == 0)
      return lastMatch = (source.length() == 0);
    int gpos = 0, spos = 0, cont = 0, last = 0, prev = 0;
    if (source.length() == 0) gpos = 1;
    
    while ((prev = spos) < source.length() && gpos < glob.size()) {
      GlobElement ge = (GlobElement) glob.get(gpos++);

      boolean matched = false;
      if (matched = ge.isMulti()) cont = (last = spos) + 1;
      else if (matched = ge.isSingle()) ++spos;
      else if (ge.isSelection()) matched = ge.selectionMatch(source.charAt(spos++));
      else matched = (spos = ge.stringMatch(source, spos)) > 0;

      if (!matched || (gpos == glob.size() && spos < source.length()))
        if (cont == 0) return lastMatch = false;
        else {
          spos = cont++;
          while (!((GlobElement) glob.get(gpos - 1)).isMulti()) --gpos;
        }
      else {
        if (!ge.isMulti() && cont > 0 && ((GlobElement) glob.get(gpos - 2)).isMulti())
          parts.set(gpos - 2, source.substring(last, prev));
        parts.set(gpos - 1, source.substring(prev, spos));
      }
    }
    if (((GlobElement) glob.get(gpos - 1)).isMulti()) {
      spos = source.length();
      parts.set(gpos - 1, source.substring(last, spos));
    }
    while (gpos < glob.size() && ((GlobElement) glob.get(gpos)).isMulti())
      parts.set(gpos++, "");
    return lastMatch = (spos == source.length() && gpos == glob.size());
  }

  private void parseGlobExpression(String exp, ArrayList glob) {
    StringBuffer sum = new StringBuffer(16);
    int i = 0;
    while (i < exp.length()) {
      if (exp.charAt(i) == MULTI || exp.charAt(i) == SINGLE ||
          exp.charAt(i) == SELECTION_BEGIN)
        addString(glob, sum);
      switch (exp.charAt(i)) {
        case MULTI:
          glob.add(new GlobElement(true));
          break;
        case SINGLE:
          glob.add(new GlobElement(false));
          break;
        case SELECTION_BEGIN:
          i = addMulti(glob, exp, i);
          break;
        case ESCAPE:
          if (++i == exp.length())
            throw new IllegalArgumentException("Malformed expression: no escape character ("+exp+")");
        default:
          sum.append(exp.charAt(i));
      }
      ++i;
    }
    addString(glob, sum);
  }

  private int addMulti(ArrayList glob, String exp, int beg) {
    ArrayList sel = new ArrayList();
    char lastChar = 0;
    boolean inverse = false;
    int i = beg;
    while (++i < exp.length() && exp.charAt(i) != SELECTION_END) {
      switch (exp.charAt(i)) {
        case INVERSE:
          if (i != beg + 1)
            throw new IllegalArgumentException("Malformed expression: invalid position of "
                +INVERSE+"("+exp+")");
          else inverse = true;
          break;
        case RANGE:
          if (++i == exp.length())
            throw new IllegalArgumentException("Malformed expression: selection not closed ("+exp+")");
          if (i == beg + 2 || exp.charAt(i) == SELECTION_END || lastChar == 0)
            throw new IllegalArgumentException("Malformed expression: invalid position of "
                +RANGE+"("+exp+")");
          sel.add(new Range(lastChar, exp.charAt(i)));
          lastChar = 0;
          break;
        case ESCAPE:
          if (++i == exp.length())
            throw new IllegalArgumentException("Malformed expression: no escape character ("+exp+")");
        default:
          if (lastChar != 0)
            sel.add(new Range(lastChar));
          lastChar = exp.charAt(i);
      }
    }
    if (i == exp.length())
      throw new IllegalArgumentException("Malformed expression: selection not closed ("+exp+")");
    if (lastChar != 0)
      sel.add(new Range(lastChar));
    glob.add(new GlobElement(sel, inverse));
    return i;
  }

  private void addString(ArrayList glob, StringBuffer str) {
    if (str.length() > 0) {
      glob.add(new GlobElement(str.toString()));
      str.setLength(0);
    }
  }

  /**
   * Vraca originalni wildcard izraz ovog raGlob objekta.<p>
   * @return ditto.
   */
  public String getOriginalString() {
    StringBuffer orig = new StringBuffer();
    for (int i = 0; i < glob.size(); i++)
      orig.append(glob.get(i));
    return orig.toString();
  }
  
  public String getTrivialMatch() {
    StringBuffer tm = new StringBuffer();
    for (int i = 0; i < glob.size(); i++)
      tm.append(((GlobElement) glob.get(i)).getTrivialMatch());
    return tm.toString();
  }

  public String toString() {
    StringBuffer dump = new StringBuffer();
    for (Iterator i = glob.iterator(); i.hasNext(); )
      dump.append((GlobElement) i.next());
    return super.toString().concat(": \n").concat(dump.toString());
  }

  /**
   * Ispisuje debug info za zadnji poziv metode match().
   */
  public void printLastMatch() {
    System.out.print("Expression: '");
    for (int i = 0; i < glob.size(); i++)
      System.out.print(glob.get(i));
    System.out.print("'  String: '");
    for (int i = 0; i < parts.size(); i++)
      System.out.print(parts.get(i));
    System.out.println("'");
    if (lastMatch) {
      System.out.println("Details:");
      for (int i = 0; i < glob.size(); i++)
        System.out.println("'" + glob.get(i) + "' -> '" + parts.get(i) + "'");
    } else
      System.out.println("They don't match.");
  }

  private static void main(String[] args) {
    System.out.println(new raGlob("*?arh").matches("3arh"));
    raGlob p1 = new raGlob("*sdf?*dg*");
    System.out.println(p1);
    p1.matches("xxxsdfdsddgsdfdg");
    p1.printLastMatch();
    System.out.println();
    raGlob p2 = new raGlob("*[ahds]sda*");
    p2.matches("ahdsdasda");
    p2.printLastMatch();
//    System.out.println(p2.morphLastMatch(""));
    System.out.println(new raGlob("*arh").match("skstavkearh").morphLastMatch("*rad"));
    System.out.println(new raGlob("*arh").match("skstavkearh").morphLastMatch("x*rad"));
    System.out.println(new raGlob("*arh").match("skstavkearh").morphLastMatch("[]rad"));
    System.out.println(new raGlob("*arh").match("skstavkearh").morphLastMatch("*[]"));
    System.out.println(new raGlob("*?ba*").match("baobab").morphLastMatch("[]?[][]"));
    System.out.println(new raGlob("*?ba*").match("baobab").morphLastMatch("*[]ba[]"));
    System.out.println(new raGlob("*?ba*").match("baobab").morphLastMatch("*[]ba*a"));
    String sql;

//    System.out.println(raImages.class.getClassLoader().getResource(raImages.getPicResource(raImages.IMGDELALL)));

//    System.out.println(new raGlob("[!bg]*fas!"));
//    System.out.println(new raGlob("[a-z]?*"));
//    System.out.println(new raGlob("[fg-jl-t]"));

  }
}


class Range {
  char fromChar, toChar;
  public Range(char ch) {
    fromChar = toChar = ch;
  }
  public Range(char chFrom, char chTo) {
    fromChar = chFrom;
    toChar = chTo;
  }
  public boolean contains(char ch) {
    return ch >= fromChar && ch <= toChar;
  }
  public String expand() {
    char[] chars = new char[toChar - fromChar + 1];
    for (int i = 0; i < chars.length; i++)
      chars[i] = (char) (fromChar + i);
    return new String(chars);
  }
  public String toString() {
    if (fromChar == toChar)
      return String.valueOf(fromChar);
    else
      return fromChar + "-" + toChar;
  }
}

class GlobElement {
  public static final int MULTI = 0;
  public static final int SINGLE = 1;
  public static final int STRING = 2;
  public static final int SELECTION = 3;
  public static final int INVERSE = 4;
  private int type;
  private String string;
  private String trivialMatch;
  private ArrayList selection;
  public GlobElement(boolean multi) {
    type = multi ? MULTI : SINGLE;
    trivialMatch = ".";
  }
  public GlobElement(String value) {
    type = STRING;
    string = value;
    trivialMatch = value;
  }
  public GlobElement(ArrayList sel, boolean inverse) {
    type = inverse ? INVERSE : SELECTION;
    selection = sel;
    trivialMatch = sel.size() == 0 ? "" : inverse ? "\0" : 
      String.valueOf(((Range) sel.get(0)).fromChar);
  }
  public String toString() {
    if (type == MULTI) return "*";
    else if (type == SINGLE) return "?";
    else if (type == STRING) return string;
    else {
      StringBuffer sel = new StringBuffer(type == INVERSE ? "[!" : "[");
      for (int i = 0; i < selection.size(); i++)
        sel.append(selection.get(i));
      sel.append("]");
      return sel.toString();
    }
  }
  public boolean isMulti() {
    return type == MULTI;
  }
  public boolean isSingle() {
    return type == SINGLE;
  }
  public boolean isSelection() {
    return type == SELECTION || type == INVERSE;
  }
  public boolean isString() {
    return type == STRING;
  }
  public String expandSelection() {
    if (type != SELECTION) return "";
    StringBuffer sel = new StringBuffer();
    for (int i = 0; i < selection.size(); i++)
      sel.append(((Range) selection.get(i)).expand());
    return sel.toString();
  }
  public String getTrivialMatch() {
    return trivialMatch;
  }
  public boolean selectionMatch(char ch) {
    for (int i = 0; i < selection.size(); i++)
      if (((Range) selection.get(i)).contains(ch))
        return type == SELECTION;
    return type == INVERSE;
  }
  public int stringMatch(String src, int beg) {
    return isString() && src.substring(beg).startsWith(string) ? beg + string.length() : -1;
  }
}
