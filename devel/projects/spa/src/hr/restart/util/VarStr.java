/****license*****************************************************************
**   file: VarStr.java
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
import java.util.List;

/**
 * <p>Title: VarStr</p>
 * <p>Description: Mladji brat klase StringBuffer, implementira skoro svu funkcionalnost
 * StringBuffer-a (osim nekoliko cudnih metoda), te dodaje poprilican broj novih (vrlo
 * potrebnih) metoda. Nije sinkroniziran kao StringBuffer. Mnoge metode su identicne
 * StringBuffer metodama pa za njih nema dokumentacije (sve append() i insert() metode,
 * charAt(), setCharAt(), delete(), length(), capacity() itd.). Metode specificne
 * za VarStr su dokumentirane u (nadam se) dovoljnoj mjeri. </p>
 * <p>Klasu je preporucljivo koristiti gdje god se manipulira sa stringovima koji se
 * mijenjaju na bilo koji nacin. Obicni String je "immutable", kad god se nesto
 * mijenja unutar njega, alocira se novi string. Tipican primjer je konstrukcija
 * oblika String nekiString = nekiString + drugiString. Ovdje se najprije stvara
 * jedan StringBuffer, kopija od nekiString, zatim se njemu dodaje drugiString
 * metodom stringBuffer.append(), i na kraju se od StringBuffer-a stvara ponovo
 * obicni String. Dakle, alociraju se dva dodatna objekta, a dolazi i do pretakanja
 * iz supljeg u prazno na par mjesta. Zato je bolje koristiti VarStr od
 * pocetka i dodavati tekst metodama append().</p>
 * <p>Metode indexOf() i lastIndexOf() su analogne onima iz klase String, pa nisu
 * dokumnetirane. Metode indexOfIgnoreCase() i lastIndexOfIgnoreCase() mislim da
 * nema potrebe objasnjavati :). Neke metode mogle bi se logicki grupirati, sto se
 * ne vidi iz abecednog popisa metoda:</p>
 * <li>mid(int, int), left(int) i right(int) vracaju odredjeni dio teksta unutar
 * ovog VarStr-a u obliku String-a (alociraju novi String objekt).</li>
 * <li>copy(int, int), leftCopy(int) i rightCopy(int) rade istu stvar, ali alociraju
 * novi VarStr umjesto String-a.</li>
 * <li>chop(int, int), leftChop(int) i rightChop(int) odsijecaju odredjeni broj
 * znakova sa lijeve, desne ili obje strane, na ovom VarStr-u (ne alociraju novog).</li>
 * <li>justify(int), leftJustify(int) i rightJustify(int) poravnavaju VarStr na
 * odredjenu duzinu ubacujuci prazne znakove po potrebi (ne alociraju novi VarStr).</li>
 * <li>trim() radi isto sto i String.trim() ali "in place", na samom ovom VarStr-u,
 * bez alociranja novog kao u slucaju String.trim().</li>
 * <li>truncate(int), leftTruncate(int) i rightTruncate(int) skracuje ovaj VarStr
 * na odredjenu duzinu izbacivanjem suvisnih znakova s pocetka ili kraja
 * (ne alociraju novi VarStr).</li>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class VarStr {
  private static int DEFAULT_CAPACITY = 16;

  private char[] value;
  private int count;

  /**
   * Staticka metoda sa postavljanje inicijalnog kapaciteta internog buffera.
   * Inace je 16.<p>
   * @param c inicijalni kapacitet buffera.
   */
  public static void setDefaultCapacity(int c) {
    DEFAULT_CAPACITY = c;
  }

  /**
   * Vraca inicijalni kapacitet internog buffera.<p>
   * @return ditto.
   */

  public static int getDefaultCapacity() {
    return DEFAULT_CAPACITY;
  }

  /**
   * Default konstruktor, stvara prazni VarStr kapaciteta getDefaultCapacity().
   * Kad se kapacitet popuni (metodama append, insert itd.) automatski se
   * povecava za 50 %.
   */
  public VarStr() {
    this(DEFAULT_CAPACITY);
  }

  /**
   * Konstruktor u kojem se eksplicitno zadaje inicijalni kapacitet buffer-a.
   * Ako se zna da ce ovaj VarStr biti jako velik, moze se pomocu ovog
   * konstruktora odmah zauzeti veliki buffer i tako izbjeci pretakanje
   * iz supljeg u prazno kad se buffer popuni.<p>
   * @param cap kapacitet internog buffer-a.
   */
  public VarStr(int cap) {
    value = new char[cap];
  }

  /**
   * Kontruktor koji stvara kopiju String-a u formatu VarStr-a, kapaciteta
   * nesto veceg od duzine tog String-a (veceg tocno za getDefaultCapacity()).<p>
   * @param s izvorni String.
   */
  public VarStr(String s) {
    this(s.length() + DEFAULT_CAPACITY);
    append(s);
  }

  /**
   * Konstruktor koji stvar kopiju VarStr-a, kapaciteta za
   * getDefaultCapacity() veceg od duzine danog VarStr-a (koji,
   * potencijalno, moze imati jos daleko veci kapacitet, recimo).<p>
   * @param orig izvorni VarStr.
   */
  public VarStr(VarStr orig) {
    count = orig.count;
    value = new char[count + DEFAULT_CAPACITY];
    if (count > 0)
      System.arraycopy(orig.value, 0, value, 0, count);
  }

  /**
   * Kontruktor koji stvara kopiju jednog dijela VarStr-a,
   * od start do end ekskluzivno. Baca StringIndexOutOfBoundsException
   * u slucaju pogresnih indeksa.<p>
   * @param orig izvorni VarStr.
   * @param start pocetni indeks.
   * @param end krajnji indeks. Duzina ovog VarStr-a ce biti end - start.
   */
  public VarStr(VarStr orig, int start, int end) {
    if (start < 0)
      throw new StringIndexOutOfBoundsException(start);
    if (end > orig.count)
      throw new StringIndexOutOfBoundsException(end);
    if (start > end)
      throw new StringIndexOutOfBoundsException(end - start);

    count = end - start;
    value = new char[count + DEFAULT_CAPACITY];
    if (count > 0)
      System.arraycopy(orig.value, start, value, 0, count);
  }

  /**
   * Vraca trenutacnu duzinu ovog VarStr-a.<p>
   * @return ditto.
   */
  public int length() {
    return count;
  }

  /**
   * Vraca trenutacni kapacitet internog buffer-a.<p>
   * @return ditto.
   */
  public int capacity() {
    return value.length;
  }

  /**
   * Brise ovaj VarStr (sadrzavat ce prazan string), ali ne mijenja
   * kapacitet.<p>
   * @return this.
   */
  public VarStr clear() {
    count = 0;
    return this;
  }

  /**
   * Brise ovaj VarStr i alocira novi buffer kapaciteta getDefaultCapacity(),
   * sto znaci da se stari moze pocistiti. Analogno StringBuffer.setLength(0).<p>
   * @return this.
   */
  public VarStr reset() {
    value = new char[DEFAULT_CAPACITY];
    count = 0;
    return this;
  }

  /**
   * Vraca true ako je ovaj VarStr sastavljen od samih znamenki (bez predznaka).<p>
   * @return ditto.
   */
  public boolean isDigit() {
    for (int i = 0; i < count; i++)
      if (value[i] < '0' || value[i] > '9')
        return false;
    return true;
  }

  /**
   * Vraca true ako ovaj VarStr predtavlja broj (decimalni ili cjelobrojni).<p>
   * @return ditto.
   */
  public boolean isNumber() {
    try {
      Double.parseDouble(toString());
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Vraca true ako ovaj VarStr predstavlja cijeli broj.<p>
   * @return ditto.
   */
  public boolean isInteger() {
    try {
      Integer.parseInt(toString());
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Poziva Double.parseDouble().<p>
   * @return double vrijednost predstavljenu ovim VarStr-om.
   */
  public double toDouble() {
    return Double.parseDouble(toString());
  }

  /**
   * Poziva Float.parseFloat().<p>
   * @return float vrijednost predstavljenu ovim VarStr-om.
   */
  public float toFloat() {
    return Float.parseFloat(toString());
  }

  /**
   * Poziva Long.parseLong().<p>
   * @return long vrijednost predstavljenu ovim VarStr-om.
   */
  public long toLong() {
    return Long.parseLong(toString());
  }

  /**
   * Poziva Integer.parseInt().<p>
   * @return int vrijednost predstavljenu ovim VarStr-om.
   */
  public int toInt() {
    return Integer.parseInt(toString());
  }

  /**
   * Poziva Short.parseShort().<p>
   * @return short vrijednost predstavljenu ovim VarStr-om.
   */
  public short toShort() {
    return Short.parseShort(toString());
  }

  /**
   * Izbacuje eventualne prazne znakove sa pocetka i kraja ovog VarStr-a.<p>
   * @return this.
   */
  public VarStr trim() {
    int left = 0, right = count;
    char[] val = value;
    while (left < right && val[left] <= ' ') ++left;
    while (left < right && val[right - 1] <= ' ') --right;
    count = right - left;
    if (count > 0)
      System.arraycopy(value, left, value, 0, count);
    return this;
  }
  
  /**
   * <p>Izbacuje sve suvisne prazne znakove iz stringa, ukljucujuci one
   * s pocetka i kraja stringa, kao i sve prazne znakove unutar stringa
   * osim jednog. Rezultat je string u kojem su sve rijeci sacuvane
   * i odvojene, ali najvise sa jednim praznim znakom.</p>
   * @return this
   */
  public VarStr powerTrim() {
    trim();
    
    char[] val = value;
    int idx = count, mark = 0;
    while (--idx > 0)
      if (val[idx] <= ' ' && mark == 0) mark = idx;
      else if (val[idx] > ' ' && mark > 0) {
        replace(idx + 1, mark + 1, " ");
        mark = 0;
      }
     return this;
  }

  public VarStr setLength(int l) {
    if (l < 0) l = 0;
    if (l > value.length) l = value.length;
    count = l;
    return this;
  }

  /**
   * Postavlja veliko pocetno slovo.<p>
   * @return this.
   */
  public VarStr capitalize() {
    if (count > 0)
      value[0] = Character.toUpperCase(value[0]);
    return this;
  }

  /**
   * Skracuje VarStr (s kraja) ako je duzi od odredjenog broja
   * znakova. Ne mijenja nista ako je VarStr kraci ili jednak
   * specificiranoj duzini.<p>
   * @param size duzina na koju se VarStr skracuje.
   * @return this.
   */
  public VarStr truncate(int size) {
    if (size < 0)
      throw new StringIndexOutOfBoundsException();
    if (count > size) count = size;
    return this;
  }

  /**
   * Isto sto i truncate().
   */
  public VarStr rightTruncate(int size) {
    return truncate(size);
  }

  /**
   * Isto sto i truncate().
   */
  public VarStr truncateRight(int size) {
    return truncate(size);
  }

  /**
   * Isto sto i truncate(), samo sto izbacuje suvisne znakove sa
   * pocetka VarStr-a.<p>
   * @param size duzina na koju se VarStr skracuje.
   * @return this.
   */
  public VarStr leftTruncate(int size) {
    if (size < 0)
      throw new StringIndexOutOfBoundsException();
    if (count <= size) return this;
    System.arraycopy(value, count - size, value, 0, size);
    count = size;
    return this;
  }

  /**
   * Isto sto i leftTruncate().
   */
  public VarStr truncateLeft(int size) {
    return leftTruncate(size);
  }


  /**
   * Isto sto i rightJustify().
   */
  public VarStr justify(int length) {
    return paddLeft(length - count, ' ');
  }

  /**
   * Poravnava ovaj VarStr po desnom rubu, dodajuci potreban broj praznih
   * znakova na pocetak VarStr-a.<p>
   * @param length duzina na koju se VarStr poravnava.
   * @return this.
   */
  public VarStr rightJustify(int length) {
    return paddLeft(length - count, ' ');
  }

  /**
   * Poravnava ovaj VarStr po lijevom rubu, dodajuci potreban broj praznih
   * znakova na kraj VarStr-a.<p>
   * @param length duzina na koju se VarStr poravnava.
   * @return this.
   */
  public VarStr leftJustify(int length) {
    return paddRight(length - count, ' ');
  }

  /**
   * Isto sto i paddRight().
   */
  public VarStr padd(int num, char ch) {
    return paddRight(num, ch);
  }

  /**
   * Dodaje odredjeni broj istih znakova na pocetak VarStr-a.<p>
   * @param num broj znakova koji se dodaje.
   * @param ch znak koji se dodaje.
   * @return this.
   */
  public VarStr paddLeft(int num, char ch) {
    if (num <= 0) return this;
    if (count + num > value.length)
      expandCapacity(count + num);
    System.arraycopy(value, 0, value, num, count);
    char[] val = value;
    for (int i = 0; i < num; i++)
      val[i] = ch;
    count += num;
    return this;
  }

  /**
   * Dodaje odredjeni broj istih znakova na kraj VarStr-a.<p>
   * @param num broj znakova koji se dodaje.
   * @param ch znak koji se dodaje.
   * @return this.
   */
  public VarStr paddRight(int num, char ch) {
    if (num <= 0) return this;
    if (count + num > value.length)
      expandCapacity(count + num);
    char[] val = value;
    for (int i = count; i < count + num; i++)
      val[i] = ch;
    count += num;
    return this;
  }

  /**
   * Izbacuje odredjeni broj znakova sa pocetka i kraja ovog VarStr-a.<p>
   * @param left broj znakova za izbaciti sa pocetka VarStr-a.
   * @param right broj znakova za izbaciti sa kraja VarStr-a.
   * @return this.
   */
  public VarStr chop(int left, int right) {
    if (left < 0 || right < 0)
      throw new StringIndexOutOfBoundsException();

    right = count - right;
    if (left >= right) return clear();
    count = right - left;
    if (left > 0)
      System.arraycopy(value, left, value, 0, count);
    return this;
  }

  /**
   * Sinonim za chop(0, int) (ili rightChop(int)).<p>
   */

  public VarStr chop(int right) {
    return chop(0, right);
  }

  /**
   * Sinonim za chop(0, 1) (ili rightChop(1)). Razlog za postojanje
   * ove metode kao i metode chop(int) je u tome sto je izbacivanje
   * znakova sa kraja stringa vrlo jeftina operacija (nema nikakvog
   * kopiranja, samo se smanji brojac slova), a u isto vrijeme vrlo
   * cesto potrebna metoda, npr. kad se dodaju elementi liste u jedan
   * niz odvojen zarezom, na kraju je potrebno zavrsni zarez maknuti.<p>
   * @return this.
   */
  public VarStr chop() {
    return chop(0, 1);
  }

  /**
   * Sinonim za chop(chars, 0).<p>
   * @param chars broj znakova za izbaciti sa pocetka VarStr-a.
   * @return this.
   */
  public VarStr leftChop(int chars) {
    return chop(chars, 0);
  }

  /**
   * Sinonim za chop(0, chars).<p>
   * @param chars broj znakova za izbaciti sa kraja VarStr-a.
   * @return this.
   */
  public VarStr rightChop(int chars) {
    return chop(0, chars);
  }

  /**
   * Isto sto i leftChop().
   */
  public VarStr chopLeft(int chars) {
    return chop(chars, 0);
  }

  /**
   * Isto sto i rightChop().
   */
  public VarStr chopRight(int chars) {
    return chop(0, chars);
  }

  /**
   * Alocira novi VarStr, kopiju ovog pocevsi od start do end.<p>
   * @param start pocetni indeks.
   * @param end krajnji indeks.
   * @return kopiju ovog VarStr-a duzine end - start, pocevsi od start.
   */
  public VarStr copy(int start, int end) {
    return new VarStr(this, start, end);
  }

  /**
   * Alocira novi VarStr sa prvih chars znakova ovog VarStr-a.<p>
   * @param chars broj znakova s lijeve strane koji se kopira.
   * @return kopiju ovog VarStr-a, prvih chars znakova.
   */
  public VarStr leftCopy(int chars) {
    return new VarStr(this, 0, chars);
  }

  /**
   * Alocira novi VarStr sa zadnjih chars znakova ovog VarStr-a.<p>
   * @param chars broj znakova s desne strane koji se kopira.
   * @return kopiju ovog VarStr-a, zadnjih chars znakova.
   */
  public VarStr rightCopy(int chars) {
    return new VarStr(this, count - chars, count);
  }

  /**
   * Isto sto i leftCopy().
   */
  public VarStr copyLeft(int chars) {
    return new VarStr(this, 0, chars);
  }

  /**
   * Isto sto i rightCopy().
   */
  public VarStr copyRight(int chars) {
    return new VarStr(this, count - chars, count);
  }

  public char charAt(int index) {
    if ((index < 0) || (index >= count))
      throw new StringIndexOutOfBoundsException(index);

    return value[index];
  }

  public void setCharAt(int index, char ch) {
    if ((index < 0) || (index >= count))
      throw new StringIndexOutOfBoundsException(index);

    value[index] = ch;
  }

  public void ensureCapacity(int minimumCapacity) {
    if (minimumCapacity > value.length)
      expandCapacity(minimumCapacity);
  }

  private void expandCapacity(int minimumCapacity) {
    int newCapacity = (value.length + 1) * 3 / 2;
    if (newCapacity < 0) {
      newCapacity = Integer.MAX_VALUE;
    } else if (minimumCapacity > newCapacity) {
      newCapacity = minimumCapacity;
    }
    char newValue[] = new char[newCapacity];
    System.arraycopy(value, 0, newValue, 0, count);
    value = newValue;
  }

  public VarStr append(Object obj) {
    if (obj instanceof VarStr)
      return append((VarStr) obj);
    return append(String.valueOf(obj));
  }

  public VarStr append(String str) {
    if (str == null) return this;

    int len = str.length();
    int newcount = count + len;
    if (newcount > value.length)
      expandCapacity(newcount);
    str.getChars(0, len, value, count);
    count = newcount;
    return this;
  }

  public VarStr append(VarStr str) {
    int newcount = count + str.count;
    if (newcount > value.length)
      expandCapacity(newcount);
    System.arraycopy(str.value, 0, value, count, str.count);
    count = newcount;
    return this;
  }

  public VarStr append(char str[]) {
    int len = str.length;
    int newcount = count + len;
    if (newcount > value.length)
      expandCapacity(newcount);
    System.arraycopy(str, 0, value, count, len);
    count = newcount;
    return this;
  }

  public VarStr append(boolean b) {
    return append(String.valueOf(b));
  }

  public VarStr append(char c) {
    int newcount = count + 1;
    if (newcount > value.length)
      expandCapacity(newcount);
    value[count++] = c;
    return this;
  }

  public VarStr append(int i) {
    return append(String.valueOf(i));
  }

  public VarStr append(long l) {
    return append(String.valueOf(l));
  }

  public VarStr append(float f) {
    return append(String.valueOf(f));
  }

  public VarStr append(double d) {
    return append(String.valueOf(d));
  }

  public VarStr delete(int start, int end) {
    if (start < 0)
      throw new StringIndexOutOfBoundsException(start);
    if (end > count)
      end = count;
    if (start > end)
      throw new StringIndexOutOfBoundsException();

    int len = end - start;
    if (len > 0) {
      System.arraycopy(value, start+len, value, start, count-end);
      count -= len;
    }
    return this;
  }

  public VarStr deleteCharAt(int index) {
    if ((index < 0) || (index >= count))
      throw new StringIndexOutOfBoundsException();
    System.arraycopy(value, index+1, value, index, count-index-1);
    count--;
    return this;
  }

  public VarStr replace(int start, int end, String str) {
    if (start < 0)
      throw new StringIndexOutOfBoundsException(start);
    if (end > count)
      end = count;
    if (start > end)
      throw new StringIndexOutOfBoundsException();

    int len = str.length();
    int newCount = count + len - (end - start);
    if (newCount > value.length)
      expandCapacity(newCount);

    System.arraycopy(value, end, value, start + len, count - end);
    str.getChars(0, len, value, start);
    count = newCount;
    return this;
  }

  public VarStr replace(int start, int end, VarStr str) {
    if (start < 0)
      throw new StringIndexOutOfBoundsException(start);
    if (end > count)
      end = count;
    if (start > end)
      throw new StringIndexOutOfBoundsException();

    int newCount = count + str.count - (end - start);
    if (newCount > value.length)
      expandCapacity(newCount);

    System.arraycopy(value, end, value, start + str.count, count - end);
    System.arraycopy(str.value, 0, value, start, str.count);
    count = newCount;
    return this;
  }

  public int indexOf(char ch) {
    return indexOf(ch, 0);
  }

  public int indexOf(char ch, int fromIndex) {
    if (fromIndex >= count) return -1;
    if (fromIndex < 0) fromIndex = 0;
    char[] val = value;
    for (int i = fromIndex; i < count; i++)
      if (val[i] == ch) return i;
    return -1;
  }

  public int indexOf(String str) {
    return indexOf(str, 0);
  }

  private int indexOf(char[] str, int off, int len, int beg) {
    char[] val = value;
    while ((beg = indexOf(str[off], beg) + 1) > 0 && beg + len - 2 < count) {
      for (int i = 1; i <= len; i++)
        if (i == len) return beg - 1;
        else if (val[beg - 1 + i] != str[off + i]) break;
    }
    return -1;
  }

  public int indexOf(String str, int fromIndex) {
    int len = str.length();
    if (len == 0) return -1;
    int capacity = count + len;
    if (capacity > value.length)
      expandCapacity(capacity);
    str.getChars(0, len, value, count);
    return indexOf(value, count, len, fromIndex);
  }

  public int indexOf(VarStr str) {
    return str.count > 0 ? indexOf(str.value, 0, str.count, 0) : -1;
  }

  public int indexOf(VarStr str, int fromIndex) {
    return str.count > 0 ? indexOf(str.value, 0, str.count, fromIndex) : -1;
  }

  public int indexOf(VarStr str, int offset, int length) {
    return str.count > 0 ? indexOf(str.value, offset, length, 0) : -1;
  }

  public int indexOf(VarStr str, int offset, int length, int fromIndex) {
    return str.count > 0 ? indexOf(str.value, offset, length, fromIndex) : -1;
  }

  public int indexOfIgnoreCase(char ch) {
    return indexOfIgnoreCase(ch, 0);
  }

  public int indexOfIgnoreCase(char ch, int fromIndex) {
    if (fromIndex >= count) return -1;
    if (fromIndex < 0) fromIndex = 0;
    char[] val = value;
    ch = Character.toLowerCase(ch);
    for (int i = fromIndex; i < count; i++)
      if (Character.toLowerCase(val[i]) == ch) return i;
    return -1;
  }

  public int indexOfIgnoreCase(String str) {
    return indexOfIgnoreCase(str, 0);
  }

  private int indexOfIgnoreCase(char[] str, int off, int len, int beg) {
    char[] val = value;
    while ((beg = indexOfIgnoreCase(str[off], beg) + 1) > 0 && beg + len - 2 < count) {
      for (int i = 1; i <= len; i++)
        if (i == len) return beg - 1;
        else if (Character.toLowerCase(val[beg - 1 + i]) !=
                 Character.toLowerCase(str[off + i])) break;
    }
    return -1;
  }

  public int indexOfIgnoreCase(String str, int fromIndex) {
    int len = str.length();
    if (len == 0) return -1;
    int capacity = count + len;
    if (capacity > value.length)
      expandCapacity(capacity);
    str.getChars(0, len, value, count);
    return indexOfIgnoreCase(value, count, len, fromIndex);
  }

  public int indexOfIgnoreCase(VarStr str) {
    return str.count > 0 ? indexOfIgnoreCase(str.value, 0, str.count, 0) : -1;
  }

  public int indexOfIgnoreCase(VarStr str, int fromIndex) {
    return str.count > 0 ? indexOfIgnoreCase(str.value, 0, str.count, fromIndex) : -1;
  }


  public int lastIndexOf(char ch) {
    return lastIndexOf(ch, count);
  }

  public int lastIndexOf(char ch, int fromIndex) {
    if (fromIndex > count) fromIndex = count;
    if (fromIndex <= 0) return -1;
    char[] val = value;
    for (int i = fromIndex - 1; i >= 0; i--)
      if (val[i] == ch) return i;
    return -1;
  }

  public int lastIndexOf(String str) {
    return lastIndexOf(str, count);
  }

  private int lastIndexOf(char[] str, int off, int len, int beg) {
    char[] val = value;
    while ((beg = lastIndexOf(str[off + len - 1], beg)) >= 0 && beg - len + 1 >= 0) {
      for (int i = len - 1; i >= 0; i--)
        if (i == 0) return beg - len + 1;
        else if (val[beg - len + i] != str[off + i - 1]) break;
    }
    return -1;
  }

  public int lastIndexOf(String str, int fromIndex) {
    int len = str.length();
    if (len == 0) return -1;
    int capacity = count + len;
    if (capacity > value.length)
      expandCapacity(capacity);
    str.getChars(0, len, value, count);
    return lastIndexOf(value, count, len, fromIndex);
  }

  public int lastIndexOf(VarStr str) {
    return str.count > 0 ? lastIndexOf(str.value, 0, str.count, count) : -1;
  }

  public int lastIndexOf(VarStr str, int fromIndex) {
    return str.count > 0 ? lastIndexOf(str.value, 0, str.count, fromIndex) : -1;
  }

  public int lastIndexOfIgnoreCase(char ch) {
    return lastIndexOfIgnoreCase(ch, count);
  }

  public int lastIndexOfIgnoreCase(char ch, int fromIndex) {
    if (fromIndex > count) fromIndex = count;
    if (fromIndex <= 0) return -1;
    char[] val = value;
    ch = Character.toLowerCase(ch);
    for (int i = fromIndex - 1; i >= 0; i--)
      if (Character.toLowerCase(val[i]) == ch) return i;
    return -1;
  }

  public int lastIndexOfIgnoreCase(String str) {
    return lastIndexOfIgnoreCase(str, count);
  }

  private int lastIndexOfIgnoreCase(char[] str, int off, int len, int beg) {
    char[] val = value;
    while ((beg = lastIndexOfIgnoreCase(str[off + len - 1], beg)) >= 0 && beg - len + 1 >= 0) {
      for (int i = len - 1; i >= 0; i--)
        if (i == 0) return beg - len + 1;
      else if (Character.toLowerCase(val[beg - len + i]) !=
               Character.toLowerCase(str[off + i - 1])) break;
    }
    return -1;
  }

  public int lastIndexOfIgnoreCase(String str, int fromIndex) {
    int len = str.length();
    if (len == 0) return -1;
    int capacity = count + len;
    if (capacity > value.length)
      expandCapacity(capacity);
    str.getChars(0, len, value, count);
    return lastIndexOfIgnoreCase(value, count, len, fromIndex);
  }

  public int lastIndexOfIgnoreCase(VarStr str) {
    return str.count > 0 ? lastIndexOfIgnoreCase(str.value, 0, str.count, count) : -1;
  }

  public int lastIndexOfIgnoreCase(VarStr str, int fromIndex) {
    return str.count > 0 ? lastIndexOfIgnoreCase(str.value, 0, str.count, fromIndex) : -1;
  }

  /**
   * Izbacuje iz ovog VarStr-a sve znakove ch.<p>
   * @param ch znak koji se izbacuje iz VarStr-a.
   * @return this.
   */
  public VarStr remove(char ch) {
    char val[] = value;
    for (int i = count - 1; i >= 0; i--)
      if (val[i] == ch && --count > i)
        System.arraycopy(val, i + 1, val, i, count - i);
    return this;
  }

  /**
   * Radi isto sto i remove(), ali vraca broj izbacenih znakova.
   * @param ch znak koji se izbacuje.
   * @return broj izbacenih znakova.
   */
  public int countRemove(char ch) {
    int removed = 0;
    char val[] = value;
    for (int i = count - 1; i >= 0; i--)
      if (val[i] == ch && ++removed > 0 && --count > i)
        System.arraycopy(val, i + 1, val, i, count - i);
    return removed;
  }

  /**
   * Izbacuje iz ovog VarStr-a sve znakove navedene u stringu.<p>
   * @param chars String sa popisom svih znakova koje se izbacuju.
   * @return this.
   */
  public VarStr removeChars(String chars) {
    if (chars == null || chars.length() == 0) return this;
    char val[] = value;
    for (int i = count - 1; i >= 0; i--) {
      if (chars.indexOf(val[i]) >= 0 && --count > i)
        System.arraycopy(val, i + 1, val, i, count - i);
    }
    return this;
  }

  /**
   * Izbacuje iz ovog VarStr-a sve prazne znakove: razmak, line feed, carriage
   * return i tabulator.<p>
   * @return this.
   */
  public VarStr removeWhitespace() {
    return removeChars(" \n\r\t");
  }

  
  public VarStr replace(char original, char replacement) {
    char[] val = value;
    for (int i = 0; i < count; i++)
      if (val[i] == original)
        val[i] = replacement;
    return this;
  }
  
  /**
   * Zamjenjuje sva pojavljivanja String-a original sa String-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replace(String original, String replacement) {
    int index = 0;
    while ((index = indexOf(original, index)) >= 0) {
      replace(index, index + original.length(), replacement);
      index += replacement.length();
    }
    return this;
  }

  /**
   * Zamjenjuje sva pojavljivanja String-a original sa VarStr-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replace(String original, VarStr replacement) {
    int index = 0;
    while ((index = indexOf(original, index)) >= 0) {
      replace(index, index + original.length(), replacement);
      index += replacement.count;
    }
    return this;
  }

  /**
   * Zamjenjuje prvo pojavljivanje String-a original sa String-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceFirst(String original, String replacement) {
    int index = indexOf(original);
    if (index >= 0)
      replace(index, index + original.length(), replacement);
    return this;
  }

  /**
   * Zamjenjuje prvo pojavljivanje String-a original sa VarStr-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceFirst(String original, VarStr replacement) {
    int index = indexOf(original);
    if (index >= 0)
      replace(index, index + original.length(), replacement);
    return this;
  }

  /**
   * Zamjenjuje zadnje pojavljivanje String-a original sa String-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceLast(String original, String replacement) {
    int index = lastIndexOf(original);
    if (index >= 0)
      replace(index, index + original.length(), replacement);
    return this;
  }

  /**
   * Zamjenjuje zadnje pojavljivanje String-a original sa VarStr-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceLast(String original, VarStr replacement) {
    int index = lastIndexOf(original);
    if (index >= 0)
      replace(index, index + original.length(), replacement);
    return this;
  }

  public VarStr replaceAll(char original, char replacement) {
    return replace(original, replacement);
  }

  /**
   * Zamjenjuje sva pojavljivanja String-a original sa String-om replacement.
   * Operacija je atomizirana; sto znaci da izraz VarStr.replaceAll("da", "dada")
   * nece dovesti do beskonacne petlje, jer se zamjenjuju samo pojavljivanja
   * trazenog niza u ovom VarStr-u prije bilo koje promjene.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceAll(String original, String replacement) {
    return replace(original, replacement);
  }

  /**
   * Zamjenjuje sva pojavljivanja String-a original sa VarStr-om replacement.
   * Vrijede iste napomene kao i kod metode replaceAll(String, String).<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceAll(String original, VarStr replacement) {
    return replace(original, replacement);
  }
  
  public VarStr replaceIgnoreCase(char original, char replacement) {
    char[] val = value;
    original = Character.toLowerCase(original);
    for (int i = 0; i < count; i++)
      if (Character.toLowerCase(val[i]) == original)
        val[i] = replacement;
    return this;
  }

  /**
   * Zamjenjuje prvo pojavljivanje String-a original, bez obzira na velicinu slova,
   * sa String-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceIgnoreCase(String original, String replacement) {
    int index = 0;
    while ((index = indexOfIgnoreCase(original, index)) >= 0) {
      replace(index, index + original.length(), replacement);
      index += replacement.length();
    }
    return this;
  }

  /**
   * Zamjenjuje prvo pojavljivanje String-a original, bez obzira na velicinu slova,
   * sa VarStr-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceIgnoreCase(String original, VarStr replacement) {
    int index = 0;
    while ((index = indexOfIgnoreCase(original, index)) >= 0) {
      replace(index, index + original.length(), replacement);
      index += replacement.count;
    }
    return this;
  }

  /**
   * Isto sto i replaceIgnoreCase() (zbog konzistencije sa replaceLast()).
   */
  public VarStr replaceFirstIgnoreCase(String original, String replacement) {
    int index = indexOfIgnoreCase(original);
    if (index >= 0)
      replace(index, index + original.length(), replacement);
    return this;
  }

  /**
   * Isto sto i replaceIgnoreCase() (zbog konzistencije sa replaceLast()).
   */
  public VarStr replaceFirstIgnoreCase(String original, VarStr replacement) {
    int index = indexOfIgnoreCase(original);
    if (index >= 0)
      replace(index, index + original.length(), replacement);
    return this;
  }

  /**
   * Zamjenjuje zadnje pojavljivanje String-a original, bez obzira na velicinu slova,
   * sa String-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceLastIgnoreCase(String original, String replacement) {
    int index = lastIndexOfIgnoreCase(original);
    if (index >= 0)
      replace(index, index + original.length(), replacement);
    return this;
  }

  /**
   * Zamjenjuje zadnje pojavljivanje String-a original, bez obzira na velicinu slova,
   * sa VarStr-om replacement.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceLastIgnoreCase(String original, VarStr replacement) {
    int index = lastIndexOfIgnoreCase(original);
    if (index >= 0)
      replace(index, index + original.length(), replacement);
    return this;
  }

  public VarStr replaceAllIgnoreCase(char original, char replacement) {
    return replaceIgnoreCase(original, replacement);
  }

  /**
   * Zamjenjuje zadnje pojavljivanje String-a original, bez obzira na velicinu slova,
   * sa String-om replacement.
   * Operacija je atomizirana; sto znaci da izraz VarStr.replaceAll("da", "dada")
   * nece dovesti do beskonacne petlje, jer se zamjenjuju samo pojavljivanja
   * trazenog niza u ovom VarStr-u prije bilo koje promjene.<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceAllIgnoreCase(String original, String replacement) {
    return replaceIgnoreCase(original, replacement);
  }

  /**
   * Zamjenjuje zadnje pojavljivanje String-a original, bez obzira na velicinu slova,
   * sa VarStr-om replacement.
   * Vrijede iste napomene kao i kod metode replaceAll(String, String).<p>
   * @param original trazeni niz znakova.
   * @param replacement zamjenski niz znakova.
   * @return this.
   */
  public VarStr replaceAllIgnoreCase(String original, VarStr replacement) {
    return replaceIgnoreCase(original, replacement);
  }


  /**
   * Sinonim za mid(start, end).
   */
  public String substring(int start, int end) {
    return mid(start, end);
  }

  /**
   * Vraca podniz od zadanog indeksa do kraja.<p>
   * @param start indeks pocetka podniza.
   * @return String, podskup ovog VarStr-a.
   */
  public String from(int start) {
    return mid(start, count);
  }

  /**
   * Vraca novi String, dio ovog VarStr-a od start do end (duzine end - start).<p>
   * @param start pocetni indeks.
   * @param end krajnji indeks.
   * @return String, podskup ovog VarStr-a.
   */
  public String mid(int start, int end) {
    if (start < 0)
      throw new StringIndexOutOfBoundsException(start);
    if (end > count)
      throw new StringIndexOutOfBoundsException(end);
    if (start > end)
      throw new StringIndexOutOfBoundsException(end - start);
    return new String(value, start, end - start);
  }

  /**
   * Vraca novi String koji sadrzi prvih chars znakova ovog VarStr-a.<p>
   * @param chars broj znakova od pocetka ovog VarStr-a.
   * @return rezultirajuci String.
   */
  public String left(int chars) {
    return mid(0, chars);
  }

  /**
   * Vraca novi String koji sadrzi zadnjih chars znakova ovog VarStr-a.<p>
   * @param chars broj znakova od kraja ovog VarStr-a.
   * @return rezultirajuci String.
   */
  public String right(int chars) {
    return mid(count - chars, count);
  }

  public VarStr insert(int offset, Object obj) {
    if (obj instanceof VarStr)
      return insert(offset, (VarStr) obj);
    return insert(offset, String.valueOf(obj));
  }

  public VarStr insert(int offset, String str) {
    if ((offset < 0) || (offset > count))
      throw new StringIndexOutOfBoundsException();

    if (str == null) return this;
    int len = str.length();
    int newcount = count + len;
    if (newcount > value.length)
      expandCapacity(newcount);

    System.arraycopy(value, offset, value, offset + len, count - offset);
    str.getChars(0, len, value, offset);
    count = newcount;
    return this;
  }

  public VarStr insert(int offset, VarStr str) {
    if ((offset < 0) || (offset > count))
      throw new StringIndexOutOfBoundsException();

    int newcount = count + str.count;
    if (newcount > value.length)
      expandCapacity(newcount);

    System.arraycopy(value, offset, value, offset + str.count, count - offset);
    System.arraycopy(str.value, 0, value, offset, str.count);
    count = newcount;
    return this;
  }


  public VarStr insert(int offset, char str[]) {
    if ((offset < 0) || (offset > count))
      throw new StringIndexOutOfBoundsException();

    int len = str.length;
    int newcount = count + len;
    if (newcount > value.length)
      expandCapacity(newcount);

    System.arraycopy(value, offset, value, offset + len, count - offset);
    System.arraycopy(str, 0, value, offset, len);
    count = newcount;
    return this;
  }

  public VarStr insert(int offset, boolean b) {
    return insert(offset, String.valueOf(b));
  }

  public VarStr insert(int offset, char c) {
    if ((offset < 0) || (offset > count))
      throw new StringIndexOutOfBoundsException();

    int newcount = count + 1;
    if (newcount > value.length)
      expandCapacity(newcount);

    System.arraycopy(value, offset, value, offset + 1, count - offset);
    value[offset] = c;
    count = newcount;
    return this;
  }

  public VarStr insert(int offset, int i) {
    return insert(offset, String.valueOf(i));
  }

  public VarStr insert(int offset, long l) {
    return insert(offset, String.valueOf(l));
  }

  public VarStr insert(int offset, float f) {
    return insert(offset, String.valueOf(f));
  }

  public VarStr insert(int offset, double d) {
    return insert(offset, String.valueOf(d));
  }

  public VarStr reverse() {
    int n = count - 1;
    for (int j = (n-1) >> 1; j >= 0; --j) {
      char temp = value[j];
      value[j] = value[n - j];
      value[n - j] = temp;
    }
    return this;
  }

  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene zadanim znakom.<p>
   * @param delimiter znak za rastavljanje.
   * @return niz String-ova.
   */
  public String[] split(char delimiter) {
    int num = 0, from = 0;
    while ((from = indexOf(delimiter, from) + 1) > 0) ++num;

    int beg = 0, end = 0;
    String[] result = new String[num + 1];
    for (int i = 0; i < num; i++, beg = end + 1)
      result[i] = mid(beg, end = indexOf(delimiter, beg));

    result[num] = mid(beg, count);
    return result;
  }
  
  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene praznim znakovima.<p>
   * Dijelovi su automatski trimani.
   * @return niz String-ova.
   */
  public String[] split() {
    List ret = splitAsList();
    return (String[]) ret.toArray(new String[ret.size()]);
  }
  
  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene praznim znakovima.<p>
   * Dijelovi su automatski trimani.
   * @return niz String-ova.
   */
  public List splitAsList() {
    List ret = new ArrayList();
    boolean token = false;
    int beg = 0;
    char val[] = value;
    
    for (int i = 0; i < count; i++) {
      boolean ws = val[i] <= ' ';
      if (ws == token) {
        if (!ws) beg = i;
        else ret.add(mid(beg, i));
        token = !ws;
      }
    }
    if (token) ret.add(from(beg));
    return ret;
  }

  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene zadanim znakom.<p>
   * @param delimiter znak za rastavljanje.
   * @return List popunjen dijelovima ovog VarStr-a.
   */
  public List splitAsList(char delimiter) {
    int beg, end;
    ArrayList list = new ArrayList();

    for (beg = 0; (end = indexOf(delimiter, beg)) >= 0; beg = end + 1)
      list.add(mid(beg, end));

    list.add(mid(beg, count));
    return list;
  }

  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene zadanim stringom.<p>
   * @param delimiter string za rastavljanje.
   * @return niz String-ova.
   */
  public String[] split(String delimiter) {
    if (delimiter.length() == 1)
      return split(delimiter.charAt(0));
    return (String[]) splitAsList(delimiter).toArray(new String[0]);
  }

  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene zadanim stringom.<p>
   * @param delimiter string za rastavljanje.
   * @return List popunjen dijelovima ovog VarStr-a.
   */
  public List splitAsList(String delimiter) {
    if (delimiter.length() == 1)
      return splitAsList(delimiter.charAt(0));

    int beg, end;
    ArrayList list = new ArrayList();

    for (beg = 0; (end = indexOf(delimiter, beg)) >= 0; beg = end + delimiter.length())
      list.add(mid(beg, end));

    list.add(mid(beg, count));
    return list;
  }

  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene zadanim znakom,
   * uz automatsko trimanje dijelova.<p>
   * @param delimiter znak za rastavljanje.
   * @return niz String-ova.
   */
  public String[] splitTrimmed(char delimiter) {
    int num = 0, from = 0;
    while ((from = indexOf(delimiter, from) + 1) > 0) ++num;

    int beg = 0, end = 0;
    String[] result = new String[num + 1];
    for (int i = 0; i < num; i++, beg = end + 1)
      result[i] = mid(beg, end = indexOf(delimiter, beg)).trim();

    result[num] = mid(beg, count).trim();
    return result;
  }

  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene zadanim znakom,
   * uz automatsko trimanje dijelova.<p>
   * @param delimiter znak za rastavljanje.
   * @return List popunjen dijelovima ovog VarStr-a.
   */
  public List splitAsListTrimmed(char delimiter) {
    int beg, end;
    ArrayList list = new ArrayList();

    for (beg = 0; (end = indexOf(delimiter, beg)) >= 0; beg = end + 1)
      list.add(mid(beg, end).trim());

    list.add(mid(beg, count).trim());
    return list;
  }

  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene zadanim znakom,
   * uz automatsko trimanje dijelova.<p>
   * @param delimiter string za rastavljanje.
   * @return niz String-ova.
   */
  public String[] splitTrimmed(String delimiter) {
    if (delimiter.length() == 1)
      return splitTrimmed(delimiter.charAt(0));
    return (String[]) splitAsListTrimmed(delimiter).toArray(new String[0]);
  }

  /**
   * Rastavlja ovaj VarStr na dijelove tipa String odvojene zadanim znakom,
   * uz automatsko trimanje dijelova.<p>
   * @param delimiter string za rastavljanje.
   * @return List popunjen dijelovima ovog VarStr-a.
   */
  public List splitAsListTrimmed(String delimiter) {
    if (delimiter.length() == 1)
      return splitAsList(delimiter.charAt(0));

    int beg, end;
    ArrayList list = new ArrayList();

    for (beg = 0; (end = indexOf(delimiter, beg)) >= 0; beg = end + delimiter.length())
      list.add(mid(beg, end).trim());

    list.add(mid(beg, count).trim());
    return list;
  }


  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Niz stringova koji se spaja.
   * @param delimiter znak kojim su pojedini stringovi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(String[] parts, char delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Niz stringova koji se spaja.
   * @param delimiter string kojim su pojedini stringovi u nizu odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(String[] parts, String delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop(delimiter.length());
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Niz objekata koji se spaja.
   * @param delimiter znak kojim su pojedini objekti odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(Object[] parts, char delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Niz objekata koji se spaja.
   * @param delimiter string kojim su pojedini objekti odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(Object[] parts, String delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop(delimiter.length());
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter znak kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(int[] parts, char delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter string kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(int[] parts, String delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop(delimiter.length());
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter znak kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(short[] parts, char delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter string kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(short[] parts, String delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop(delimiter.length());
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter znak kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(long[] parts, char delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter string kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(long[] parts, String delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop(delimiter.length());
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter znak kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(float[] parts, char delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter string kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(float[] parts, String delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop(delimiter.length());
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter znak kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(double[] parts, char delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Niz koji se spaja.
   * @param delimiter string kojim su pojedini elementi odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(double[] parts, String delimiter) {
    VarStr result = new VarStr();

    for (int i = 0; i < parts.length; i++)
      result.append(parts[i]).append(delimiter);

    return result.chop(delimiter.length());
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Lista objekata koji se spajaju (u String se pretvaraju sa toString()).
   * @param delimiter znak kojim su pojedini objekti odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(List parts, char delimiter) {
    VarStr result = new VarStr();

    for (Iterator i = parts.iterator(); i.hasNext(); result.append(i.next()).append(delimiter));

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Lista objekata koji se spajaju (u String se pretvaraju sa toString()).
   * @param delimiter string kojim su pojedini objekti odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(List parts, String delimiter) {
    VarStr result = new VarStr();

    for (Iterator i = parts.iterator(); i.hasNext(); result.append(i.next()).append(delimiter));

    return result.chop(delimiter.length());
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim znakom.<p>
   * @param parts Iterator objekata koji se spajaju (u String se pretvaraju sa toString()).
   * @param delimiter znak kojim su pojedini objekti odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(Iterator parts, char delimiter) {
    VarStr result = new VarStr();

    while (parts.hasNext()) result.append(parts.next()).append(delimiter);

    return result.chop();
  }

  /**
   * Vraca novi VarStr sastavljen od niza dijelova odvojenih odredjenim stringom.<p>
   * @param parts Iterator objekata koji se spajaju (u String se pretvaraju sa toString()).
   * @param delimiter string kojim su pojedini objekti odvojeni.
   * @return novi VarStr.
   */
  public static VarStr join(Iterator parts, String delimiter) {
    VarStr result = new VarStr();

    while (parts.hasNext()) result.append(parts.next()).append(delimiter);

    return result.chop(delimiter.length());
  }

  /**
   * Trazi broj ponavljanja nekog znaka unutar ovog VarStr-a.<p>
   * @param ch znak koji se trazi.
   * @return broj ponavljanja trazenog znaka.
   */
  public int countOccurences(char ch) {
    int occur = 0;
    char[] val = value;
    for (int i = 0; i < count; i++)
      if (val[i] == ch) ++occur;

    return occur;
  }

  /**
   * Trazi broj ponavljanja nekog stringa unutar ovog VarStr-a.<p>
   * @param str string koji se trazi.
   * @return broj ponavljanja trazenog stringa.
   */
  public int countOccurences(String str) {
    int occur = 0, from = 0;

    while ((from = indexOf(str, from) + str.length()) > 0) ++occur;

    return occur;
  }

  /**
   * Trazi broj ponavljanja nekog znaka unutar ovog VarStr-a, bez obzira na
   * velicinu slova.<p>
   * @param ch znak koji se trazi.
   * @return broj ponavljanja trazenog znaka.
   */
  public int countOccurencesIgnoreCase(char ch) {
    int occur = 0;
    char[] val = value;
    ch = Character.toLowerCase(ch);
    for (int i = 0; i < count; i++)
      if (Character.toLowerCase(val[i]) == ch) ++occur;

    return occur;
  }

  /**
   * Trazi broj ponavljanja nekog stringa unutar ovog VarStr-a, bez obzira na
   * velicinu slova.<p>
   * @param str string koji se trazi.
   * @return broj ponavljanja trazenog stringa.
   */
  public int countOccurencesIgnoreCase(String str) {
    int occur = 0, from = 0;

    while ((from = indexOfIgnoreCase(str, from) + str.length()) > 0) ++occur;

    return occur;
  }

  public boolean equals(Object o) {
    if (o instanceof VarStr && ((VarStr) o).count == count) {
      VarStr v = (VarStr) o;
      char[] val = value;
      for (int i = 0; i < count; i++)
        if (val[i] != v.value[i]) return false;
      return true;
    } 
    return false;
  }

  public String toString() {
    return new String(value, 0, count);
  }

  public static void main(String[] args) {
    VarStr test = new VarStr("Ovo je pokusni tekst");
    System.out.println(test.indexOf(" je "));
    System.out.println(test.indexOf(""));
    System.out.println(test.indexOf("je po"));
    System.out.println(test.indexOf("Ovo"));
    System.out.println(test.indexOf("tekst"));
    System.out.println(test.indexOf("tekst "));
    System.out.println(test.lastIndexOf("s"));
    System.out.println(test.lastIndexOf("t"));
    System.out.println(test.lastIndexOf("vo"));
    System.out.println(test.length());
    test.replace("pokusni", "mali pokusni");

    System.out.println(test);
    test.replace("tekst", "tekstic");
    System.out.println(test);
    test.replaceAll("o", "oo");
    System.out.println(test);
    test.insert(10, "blah");
    System.out.println(test);
    System.out.println(test.left(5));
    System.out.println(test.right(6));
//    test.justify(40);
    System.out.println(test);
    test.leftJustify(40);
    System.out.println("|"+test+"|");
    test.chopLeft(10);
    System.out.println("|"+test+"|");
    System.out.println(test.copyLeft(6));
    System.out.println(new VarStr("ovo;je;jedan;mali;primjer").splitAsList(';'));
    System.out.println(VarStr.join(new String[] {"ovo","je","jedan","primjer"}, ";"));

    System.out.println(VarStr.join(new VarStr("ovo;je;jedan;mali;primjer").split(';'), "."));

    System.out.println(VarStr.join(new int[] {1,2,3,4,5,6,7,8,9,10}, ':'));
    System.out.println(new VarStr("SELECT * FROM nesto WHERE bla bla in ('df','dg','sdfg',").replaceLast("',","')"));

    System.out.println(VarStr.join(new VarStr("Ovo ,  je, mali,pokus   ,   s,tri  manjem, splita ").splitTrimmed(","), '|'));

    VarStr sorter = new VarStr("URA1-24534431G");
    int fn = 0;
    for (int i = 0; i < sorter.length(); i++)
      if (!Character.isDigit(sorter.charAt(i))) fn = i + 1;

    sorter.chopLeft(fn);
    sorter.justify(10).leftTruncate(10).replaceAll(' ','0');
    System.out.println(sorter);

    try {
      VarStr pow = new VarStr(" da   vidimo\nhoce\n \n  li \n ovo raditi   ili mozda nece\n \n");
      System.out.println("|"+pow.powerTrim()+"|");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
