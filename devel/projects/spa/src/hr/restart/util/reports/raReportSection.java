/****license*****************************************************************
**   file: raReportSection.java
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
package hr.restart.util.reports;

import hr.restart.util.Aus;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import sg.com.elixir.reportwriter.xml.IModel;
import sg.com.elixir.reportwriter.xml.ModelFactory;

/**
 * <p>Title: raReportSection</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: REST-ART</p>
 * Klasa koja extenda raReportElement i sadrži dodatnu funkcionalnost potrebnu za
 * pojedine dijelove reporta (page header, report header itd.).
 * @author ab.f
 * @version 1.0
 */

public class raReportSection extends raReportElement {

  private LinkedList elements = new LinkedList();

  private ArrayList modifiers = new ArrayList();

  /**
   * Default konstruktor (ne koristiti).
   */
  public raReportSection() {
  }

  /**
   * Registrira ReportModifier sa doti\u010Dnim reportom. Prilikom pozivanja metode
   * getReportTemplate(), pozivaju se metode modify() svih ovako registriranih
   * ReportModifiera.
   * @param mod ReportModifier koji radi odre\u0111enu izmjenu.
   */
  public void addReportModifier(ReportModifier mod) {
    modifiers.add(mod);
  }

  /**
   * Izbacuje ReportModifier iz liste registriranih uz ovaj report.
   * @param mod ReportModifier koji se izbacuje.
   */
  public void removeReportModifier(ReportModifier mod) {
    modifiers.remove(mod);
  }

  public void invokeModifiers() {
    for (Iterator i = modifiers.iterator(); i.hasNext(); ((ReportModifier) i.next()).modify());
  }

  public void removeAllModifiers() {
    modifiers.clear();
  }

  public raReportSection defaultAltererSect() {
    return new raReportSection(this, true);
  }

  private raReportSection(raReportSection source, boolean alt) {
    super(source, alt);
    elements.clear();
    if (alt)
      for (Iterator i = source.elements.iterator(); i.hasNext(); )
        elements.add(((raReportElement) i.next()).defaultAlterer());
  }

  /**
   * Konstruktor koji radi shallow kopiju nekog drugog report sectiona.
   * Koristi se za stvaranje view-a (podskupa nekog report sectiona koji
   * sadrži samo neke od elemenata).<p>
   * @param source izvorni report section.
   */
  public raReportSection(raReportSection source) {
    super(source);
    elements.clear();
  }

  /**
   * Konstruktor koji radi wrapper za IModel nekog report sectiona (report header,
   * page header, detail itd.). Kasnije \u0107e biti potrebno pozvati i metodu
   * setDefaults kojim se setiraju default propertiji za sam report section.<p>
   * @param model IModel za koji se radi wrapper.
   */
  public raReportSection(IModel model) {
    super(model);
//    if (model != null) model.removeAllModels();
    elements.clear();
  }
  
  public raReportSection(IModel model, boolean wrap) {
    super(model, wrap);
    elements.clear();
    if (wrap)
      for (int i = 0; i < model.getModelCount(); i++)
        elements.add(new raReportElement(model.getModel(i), true));
  }

  /**
   * Konstruktor koji radi wrapper za IModel nekog report sectiona, i ujedno postavlja
   * njegove default propertije.<p>
   * @param model IModel za koji se radi ovaj wrapper.
   * @param defaults niz stringova koji predstavljaju default propertije.
   */
  public raReportSection(IModel model, String[] defaults) {
    super(model, defaults);
//    if (model != null) model.removeAllModels();
    elements.clear();
  }

  /**
   * Odre\u0111uje vidljivost cijelog sectiona (svih elemenata unutar njega).<p>
   * @param tp true za proziran (nevidljiv) section, false za vidljiv.
   */
  public void setTransparent(boolean tp) {
    Iterator i = elements.iterator();
    while (i.hasNext())
      if (!tp) {
        raReportElement e = (raReportElement) i.next();
        e.restoreDefault(ep.VISIBLE);
      } else ((raReportElement) i.next()).setVisible(false);
  }

  /**
   * Pomi\u010De sve elemente unutar sectiona za odre\u0111enu udaljenost (u decipointima)
   * prema dolje.<p>
   * @param points vertikalni pomak u decipointima.
   */
  public void moveDown(long points) {
    raReportElement e;
    Iterator i = elements.iterator();
    while (i.hasNext()) {
      e = (raReportElement) i.next();
      e.setTop(e.getTop() + points);
    }
  }

  /**
   * Pomi\u010De sve elemente unutar sectiona za odre\u0111enu udaljenost (u centimetrima)
   * prema dolje.<p>
   * @param cms vertikalni pomak u centimetrima.
   */
  public void moveDownCm(double cms) {
    moveDown(Math.round(cms * RATIO));
  }

  /**
   * Pomi\u010De sve elemente unutar sectiona za odre\u0111enu udaljenost (u decipointima)
   * prema gore.<p>
   * @param points vertikalni pomak u decipointima.
   */
  public void moveUp(long points) {
    moveDown(-points);
  }

  /**
   * Pomi\u010De sve elemente unutar sectiona za odre\u0111enu udaljenost (u centimetrima)
   * prema gore.<p>
   * @param cms vertikalni pomak u centimetrima.
   */
  public void moveUpCm(double cms) {
    moveDown(-Math.round(cms * RATIO));
  }

  /**
   * Pomi\u010De sve elemente unutar sectiona za odre\u0111enu udaljenost (u decipointima)
   * prema desno.<p>
   * @param points horizontalni pomak u decipointima.
   */
  public void moveRight(long points) {
    raReportElement e;
    Iterator i = elements.iterator();
    while (i.hasNext()) {
      e = (raReportElement) i.next();
      e.setLeft(e.getLeft() + points);
    }
  }

  /**
   * Pomi\u010De sve elemente unutar sectiona za odre\u0111enu udaljenost (u centimetrima)
   * prema desno.<p>
   * @param cms horizontalni pomak u centimetrima.
   */
  public void moveRightCm(double cms) {
    moveRight(Math.round(cms * RATIO));
  }

  /**
   * Pomi\u010De sve elemente unutar sectiona za odre\u0111enu udaljenost (u decipointima)
   * prema lijevo.<p>
   * @param points horizontalni pomak u decipointima.
   */
  public void moveLeft(long points) {
    moveRight(-points);
  }

  /**
   * Pomi\u010De sve elemente unutar sectiona za odre\u0111enu udaljenost (u centimetrima)
   * prema lijevo.<p>
   * @param cms horizontalni pomak u centimetrima.
   */
  public void moveLeftCm(double cms) {
    moveRight(-Math.round(cms * RATIO));
  }

  /**
   * Izbacuje element iz ovog sectiona, uz proporcionalno širenje ostalih
   * elemenata horizontalno poravnatih s doti\u010Dnim. Ukoliko je neki element u istom
   * sectionu vertikalno poravnat sa zadanim, i on \u0107e se izbaciti (uz širenje ostalih
   * elemenata horizontalno poravnatih s njim).<p>
   * Inverzna operacija ne postoji, jedina mogu\u0107nost je restoreDefaults() na cijelon
   * sectionu.<p>
   * @param be element unutar ovog koji se izbacuje.
   */
  public void deflateElement(raReportElement be) {
    resizeElement(be, (long) 0, null);
  }

  /**
   * Izbacuje element iz ovog sectiona, uz adekvatno širenje zadanog elementa
   * koji mora biti horizontalno poravnat s njim. Tako\u0111er izbacuje elemente vertikalno
   * poravnate sa zadanim elementom.<p>
   * Inverzna operacija ne postoji, jedina mogu\u0107nost je restoreDefaults() na cijelon
   * sectionu.<p>
   * @param be
   * @param target
   */
  public void deflateElement(raReportElement be, raReportElement target) {
    resizeElement(be, (long) 0, target);
  }

  /**
   * Mijenja širinu nekog od elemenata ovog sectiona, uz adekvatnu promjenu
   * širine svih ostalih elemenata poravnatih s istim, tako da ukupna širina
   * ostane nepromijenjena. Ukoliko je neki element horizontalno poravnat s
   * izvornim (ista X koordinata i širina), i njegova \u0107e se širina tako\u0111er
   * promijeniti (uz konzekventno podešavanje eventualnih elemenata u njegovoj
   * liniji itd). Podešavanje širine ostalih elemenata horizontalno poravnatih
   * s izvornim se radi tako da se svaki od tih elemenata suzi ili proširi
   * proporcionalno s njegovom bivšom širinom (tako da se sve širine mijenjaju
   * u istim postocima).<p>
   * @param be element unutar ovog sectiona koji se podešava.
   * @param cmWidth nova širina elementa u centrimetrima.
   */
  public void resizeElementCm(raReportElement be, double cmWidth) {
    resizeElement(be, Math.round(cmWidth * RATIO), null);
  }

  /**
   * Mijenja širinu nekog od elementa na ra\u010Dun (ili u korist) nekog drugog elementa
   * horizontalno poravnatog s njim. Tako\u0111er vrši istu operaciju i za sve ostale elemente
   * vertikalno poravnatih s izvornim.<p>
   * @param be element unutar ovog sectiona koji se podešava.
   * @param cmWidth nova širina elementa u centimetrima.
   * @param target ciljni element.
   */
  public void resizeElementCm(raReportElement be, double cmWidth, raReportElement target) {
    resizeElement(be, Math.round(cmWidth * RATIO), target);
  }

  /**
   * Radi isto što i resizeElementCm.<p>
   * @param be element kojem se mijenja širina.
   * @param newWidth nova širina u decipointima.
   */
  public void resizeElement(raReportElement be, long newWidth) {
    resizeElement(be, newWidth, null);
  }

  /**
   * Radi isto što i resizeElementCm.<p>
   * @param be element kojem se mijenja širina.
   * @param newWidth nova širina u decipointima.
   * @param target ciljni element.
   */
  public void resizeElement(raReportElement be, long newWidth, raReportElement target) {
    newWidth = (newWidth + 10) / 20 * 20;
    if (be.getWidth() == newWidth) return;
    raReportElement e;
    LinkedList all = new LinkedList();
    Iterator i = elements.iterator();
    while (i.hasNext()) {
      e = (raReportElement) i.next();
      if (e.getLeft() == be.getLeft() && e.getWidth() == be.getWidth())
        all.add(e);
    }
    i = all.iterator();
    while (i.hasNext())
      resizeElementImpl((raReportElement) i.next(), newWidth, target);
  }

  private void resizeElementImpl(raReportElement be, long newWidth, raReportElement target) {
    long totalw = -be.getWidth(), newTotalw = -newWidth;
    long tleft = target == null ? -1 : target.getLeft();
    long eleft, bleft = be.getLeft();
    TreeSet line = new TreeSet(new Comparator() {
      public int compare(Object e1, Object e2) {
        return (int) ((raReportElement) e1).getLeft() - (int) ((raReportElement) e2).getLeft();
      }
    });
    raReportElement e = null;
    Iterator i = elements.iterator();
    while (i.hasNext()) {
      e = (raReportElement) i.next();
      if (e.getTop() == be.getTop() && (e.isVisible() || e == be)) {
        line.add(e);
        totalw += e.getWidth();
      }
    }
    long adjust = 1;
    long xdiff = 0;
    long diff = be.getWidth() - newWidth;
    if (line.size() > 1 && newWidth == 0) {
      e = ((raReportElement) line.first()).getLeft() == be.getLeft() ?
          ((raReportElement[]) line.toArray(new raReportElement[line.size()]))[1] :
          (raReportElement) line.headSet(be).last();
      diff = e.getLeft() > be.getLeft() ? e.getLeft() - be.getLeft() :
             be.getRight() - e.getRight();
    }
    i = line.iterator();
    while (i.hasNext()) {
      e = (raReportElement) i.next();
      if (adjust++ == 0) xdiff = be.getLeft() - e.getLeft();
      e.setLeft((eleft = e.getLeft()) + xdiff);
      xdiff -= e.getWidth();
      if (eleft == bleft && adjust != 1)
        e.setWidth(adjust = newWidth);
      else if (target == null)
        e.setWidth(e.getWidth() * (totalw + diff) / totalw);
      else if (eleft == tleft)
        e.setWidth(e.getWidth() + diff);
      xdiff += e.getWidth();
      newTotalw += e.getWidth();
    }
    if (e == be && newWidth == 0)
      e = (raReportElement) line.headSet(be).last();
    e.setWidth(e.getWidth() + totalw + diff - newTotalw);
    be.setVisible(newWidth != 0);
  }

  /**
   * Vra\u0107a podskup ovog report sectiona sa svim elementima koji su barem polovicom
   * širine i visine unutar danog pravokutnika. Na tom podskupu se onda mogu pozivati
   * metode tipa moveDown, moveLeft itd.<p>
   * @param left lijevi rub pravokutnika u decipointima.
   * @param top gornji rub pravokutnika.
   * @param right desni rub pravokutnika.
   * @param bottom donji rub pravokutnika.
   * @return report section sa obuhva\u0107enim elementima.
   */
  public raReportSection getView(long left, long top, long right, long bottom) {
    raReportSection view = new raReportSection(this, defaultMode);
    view.elements.clear();
    Iterator i = elements.iterator();
    while (i.hasNext()) {
      raReportElement e = (raReportElement) i.next();
      if (e.getLeft() + e.getWidth() / 2 <= right && e.getTop() + e.getHeight() / 2 <= bottom &&
          e.getLeft() + e.getWidth() / 2 >= left && e.getTop() + e.getHeight() / 2 >= top)
        view.elements.add(e);
    }
    return view;
  }

  /**
   * Vra\u0107a podskup ovog report sectiona sa svim elementima koji su barem polovicom
   * širine i visine unutar danog pravokutnika. Na tom podskupu se onda mogu pozivati
   * metode tipa moveDown, moveLeft itd.<p>
   * @param left lijevi rub pravokutnika u centimetrima.
   * @param top gornji rub pravokutnika.
   * @param right desni rub pravokutnika.
   * @param bottom donji rub pravokutnika.
   * @return report section sa obuhva\u0107enim elementima.
   */
  public raReportSection getViewCm(double left, double top, double right, double bottom) {
    return getView(Math.round(left * RATIO), Math.round(top * RATIO),
                   Math.round(right * RATIO), Math.round(bottom * RATIO));
  }

  /**
   * Vra\u0107a podskup ovog report sectiona, koji sadrži eksplicitno navedene elemente.<p>
   * @param elems niz elemenata podskupa ovog sectiona.
   * @return report section sa danim elementima.
   */
  public raReportSection getView(raReportElement[] elems) {
    raReportSection view = new raReportSection(this, defaultMode);
    for (int i = 0; i < elems.length; i++)
      view.elements.add((defaultMode ? elems[i].defaultAlterer() : elems[i]));
    return view;
  }

  private void recursivePushDown(raReportElement from, long dy) {
    for (Iterator i = elements.iterator(); i.hasNext(); ) {
      raReportElement e = (raReportElement) i.next();
      if (e.getTop() < from.getTop() && isPathClearVertical(e, from)) {
        recursivePushDown(e, dy);
        e.setTop(e.getTop() + dy);
      }
    }
  }

  public boolean isPathClearVertical(raReportElement from, raReportElement to) {
    Rectangle t = new Rectangle();
    Rectangle r = new Rectangle((int) from.getLeft(), (int) from.getTop(to),
        (int) from.getWidth(), (int) from.getBottom(to) - (int) from.getTop(to));
    for (Iterator i = elements.iterator(); i.hasNext(); ) {
      raReportElement el = (raReportElement) i.next();
      if (!el.equals(from) && !el.equals(to) && r.intersects(el.getBounds(t)))
        return false;
    }
    return true;
  }

  public boolean intersectsOnlyWith(raReportElement el, raReportElement with) {
    boolean intersectsWith = false;
    for (Iterator i = elements.iterator(); i.hasNext(); ) {
      raReportElement e = (raReportElement) i.next();
      if (el.intersects(e)) {
        if (e == with) intersectsWith = true;
        else return false;
      }
    }
    return intersectsWith;
  }

  public boolean wouldIntersectOnlyWithIfMoved(raReportElement el, raReportElement with, long dx, long dy) {
    boolean intersectsWith = false;
    for (Iterator i = elements.iterator(); i.hasNext(); ) {
      raReportElement e = (raReportElement) i.next();
      if (el.wouldIntersectIfMoved(e, dx, dy)) {
        if (e == with) intersectsWith = true;
        else return false;
      }
    }
    return intersectsWith;
  }

  public boolean wouldIntersectWithAnyIfMoved(raReportElement el, long dx, long dy) {
    for (Iterator i = elements.iterator(); i.hasNext(); )
      if (el.wouldIntersectIfMoved((raReportElement) i.next(), dx, dy)) return true;
    return false;
  }

  public boolean intersectsWithAny(raReportElement el) {
    for (Iterator i = elements.iterator(); i.hasNext(); )
      if (el.intersects((raReportElement) i.next())) return true;
    return false;
  }

  public void deleteElementPushDown(raReportSection orig, raReportElement elem) {
    raReportElement nearest = null;
    if (defaultMode) elem = elem.defaultAlterer();
    for (Iterator i = elements.iterator(); i.hasNext(); ) {
      raReportElement e = (raReportElement) i.next();
      if (e.getTop() < elem.getTop() && (nearest == null ||
          e.distance(elem) < nearest.distance(elem)))
        nearest = e;
    }
    long dy = elem.getTop() - nearest.getTop();
    orig.removeModel(elem);
    elements.remove(elem);
    recursivePushDown(elem, dy);
  }

  public void deleteElementsPushDown(raReportSection orig, raReportElement[] elems) {
    for (int i = 0; i < elems.length; i++) {
      try {
        deleteElementPushDown(orig, elems[i]);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Vra\u0107a podskup ovog report sectiona u kojem se nalaze svi elementi odre\u0111eni
   * pravokutnikom kojeg stvaraju sva elementa, gornji lijevi i donji desni.<p>
   * @param upperLeft gornji lijevi element pravokutnika.
   * @param lowerRight donji desni element pravokutnika.
   * @return podskup report sectiona sa obuhva\u0107enim elementima.
   */
  public raReportSection getView(raReportElement upperLeft, raReportElement lowerRight) {
    if (defaultMode) {
      upperLeft = upperLeft.defaultAlterer();
      lowerRight = lowerRight.defaultAlterer();
    }
    return getView(upperLeft.getLeft(), upperLeft.getTop(),
                   lowerRight.getRight(), lowerRight.getBottom());
  }


  public void changeFont(String defaultFont, String newFont, double ratio) {
    raReportElement e;
    boolean hasText = false;
    Iterator i = elements.iterator();
    while (i.hasNext()) {
      e = (raReportElement) i.next();
      if (e.hasProperty(ep.FONT_NAME) && e.getDefault(ep.FONT_NAME).equals(defaultFont))
        hasText = true;
    }
    if (!hasText) return;
    //restoreDefaults();
    setHeightCm(getHeightCm() / ratio);
    i = elements.iterator();
    while (i.hasNext()) {
      e = (raReportElement) i.next();
      if (e.hasProperty(ep.FONT_NAME) && e.getDefault(ep.FONT_NAME).equals(defaultFont)) {
        e.setFont(newFont);
        e.setProperty(ep.HEIGHT, e.getDefault(ep.HEIGHT));
        e.setHeightCm(e.getHeightCm() / ratio);
      }
      e.setTopCm(e.getTopCm() / ratio);
    }
  }

  public void setBackColor(String col) {
    Iterator i = elements.iterator();
    while (i.hasNext())
      ((raReportElement) i.next()).setBackColor(col);
  }

//  public void enlargeElementsVertical(double factor) {
//    raReportElement e;
//    Iterator i = elements.iterator();
//    while (i.hasNext()) {
//      e = (raReportElement) i.next();
//      if (e.hasProperty(ep.HEIGHT)) {
//        e.setHeight(Math.round(e.getHeight() * factor));
//        e.setTop(Math.round(e.getTop() * factor));
//      }
//    }
//    if (this.hasProperty(ep.HEIGHT))
//      this.setHeight(Math.round(this.getHeight() * factor));
//  }

  /**
   * Postavlja default vrijednosti za sve propertije ovog sectiona i svih elemenata
   * unutar njega.
   */
  public void restoreDefaults() {
    super.restoreDefaults();
    Iterator i = elements.iterator();
    while (i.hasNext())
      ((raReportElement) i.next()).restoreDefaults();
  }

  /**
   * Dodaje element u ovaj section \u010Diji se IModel ve\u0107 nalazi u IModelu sectiona
   * (Elixir specifi\u010Dnosti), kao što su 'sorting and grouping' sekcije. Nema
   * potrebe zvati, koristi se jedino unutar automatskog generatora java report
   * klasa.<p>
   * @param model vrsta modela koji se dodaje.
   * @param defaults default propertiji za taj model.
   * @return wrapper za dani model.
   */
  public raReportElement getModel(String model, String[] defaults) {
    raReportElement child = new raReportElement(this.model.getModel(model), defaults);
    elements.add(child);
    updateChildOrder(child.model);
    return child;
  }

  public raReportElement addModel(String model) {
    return addModel(model, null);
  }

  /**
   * Dodaje element tipa TEXT, LABEL, LINE itd. u ovaj report section.<p>
   * @param model vrsta modela koji se dodaje (raElixirProperties.TEXT, LABEL, LINE itd.).
   * @param defaults default propertiji za taj model.
   * @return wrapper za model.
   */
  public raReportElement addModel(String model, String[] defaults) {
    raReportElement child = new raReportElement(ModelFactory.getModel(model), defaults);
    this.model.addModel(child.model.getPropertyValue(raElixirProperties.NAME), child.model);
    elements.add(child);
    updateChildOrder(child.model);
    return child;
  }
  
  private void updateChildOrder(IModel child) {
    String count = this.model.getHiddenPropertyValue(ep.CHILD_COUNTER);
    this.model.removeHiddenProperty(ep.CHILD_COUNTER);
    child.removeHiddenProperty(ep.CHILD_NUMBER);
    if (count == null) {
      this.model.addHiddenProperty(ep.CHILD_COUNTER, "1");
      child.addHiddenProperty(ep.CHILD_NUMBER, "1");
    } else {
      int next = Aus.getNumber(count) + 1;
      this.model.addHiddenProperty(ep.CHILD_COUNTER, Integer.toString(next));
      child.addHiddenProperty(ep.CHILD_NUMBER, Integer.toString(next));
    }
  }

  public raReportElement copyToModify(raReportElement source) {
    return addModel(source.model.getPropertyValue(ep.MODEL_NAME),
                    (String[]) source.getDefaults().clone()).defaultAlterer();
  }

  /**
   * Vra\u0107a element u sectiona. Koristiti samo ako je sigurno koji element
   * je na kojem mjestu.<p>
   * @param model redni broj elementa.
   */

  public raReportElement getModel(int i) {
    return (raReportElement) elements.get(i);
  }
  
  public int getModelCount() {
    return elements.size();
  }

  /**
   * Izbacuje element iz sectiona. Koristiti samo ako je sigurno koji element
   * je na kojem mjestu, jer kad se jedan element izbaci, ostali mijenjaju
   * redne brojeve.<p>
   * @param model redni broj elementa koji se izbacuje.
   */
  public void removeModel(int i) {
    removeModel(getModel(i));
  }

  public void addModel(raReportElement model) {
    this.model.addModel(model.model.getPropertyValue(raElixirProperties.NAME), model.model);
    elements.add(model);
  }

  /**
   * Izbacuje element iz sectiona.<p>
   * @param model element koji se izbacuje.
   */
  public void removeModel(raReportElement model) {
    this.model.removeModel(model.model.getPropertyValue(raElixirProperties.NAME));
    elements.remove(model);
  }

  /**
   * Izbacuje više elemenata iz sectiona.<p>
   * @param models niz elemenata koji se izbacuju.
   */
  public void removeModels(raReportElement[] models) {
    for (int i = 0; i < models.length; i++)
      removeModel(models[i]);
  }

  public void moveAll(raReportSection dest) {
    raReportElement[] all = new raReportElement[elements.size()];
    elements.toArray(all);
    for (int i = 0; i < all.length; i++) {
      dest.addModel(all[i]);
    }
    removeModels(all);
  }
  
  public long getShrinkedHeight() {
    raReportElement[] all = new raReportElement[elements.size()];
    elements.toArray(all);
    long height = 0;
    for (int i = 0; i < all.length; i++) {
      long bottom = all[i].getBottom();
      if (bottom > height) height = bottom;
    }
    return height;
  }
}
