/****license*****************************************************************
**   file: raReportElement.java
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

import java.awt.Rectangle;

import sg.com.elixir.reportwriter.xml.IModel;

/**
 * <p>Title: raReportElement</p>
 * <p>Description: Wrapper za IModel pojedina\u010Dnog elementa reporta.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: REST-ART</p>
 * Klasa služi kao wrapper za IModel nekog elementa reporta (labele, teksta, linije itd).
 * Propertiji elementa se ovako mogu mijenjati intuitivnim metodama tipa
 * setLeft, setTop, setVisible itd.
 * @author ab.f
 * @version 1.0
 */

public class raReportElement {
  public static final double RATIO = 567;
  protected raElixirProperties ep = raElixirPropertiesInstance.get();
  protected raElixirPropertyValues ev = raElixirPropertyValuesInstance.get();
  protected IModel model;
  private String[] properties;
  private String[] defaults;
  private String[] propertyDefaults;

  protected boolean defaultMode = false;

  /**
   * Default konstruktor. Bolje ne koristiti.
   */
  public raReportElement() {
    model = null;
  }

  protected raReportElement(raReportElement source, boolean alt) {
    this(source.model, source.defaults);
    defaultMode = alt;
  }

  public raReportElement defaultAlterer() {
    return new raReportElement(this, true);
  }

  /**
   * Konstruktor koji stvara shallow kopiju nekog drugog elementa.
   * Koristi se za stvaranje view-a nekog report sectiona (page header, section header itd).<p>
   * @param source izvorni report element.
   */
  public raReportElement(raReportElement source) {
    this(source.model, source.defaults);
  }

  /**
   * Konstruktor koji stvara report element od odgovaraju\u0107eg IModel-a (Elixirovog
   * report element interface-a). Ovako kreiran report element ne\u0107e imati
   * postavljene vrijednosti propertija, pa ga ne bi trebalo koristiti prije
   * poziva metode setDefaults(String[] defaults).<p>
   * @param model IModel report elementa.
   */
  public raReportElement(IModel model) {
    this(model, null);
  }
  
  public raReportElement(IModel model, boolean wrap) {
    this(model, null);
    if (wrap) {
      for (int i = 0; i < defaults.length; i++)
        setDefault(properties[i], getProperty(properties[i]));
    }
  }

  /**
   * Konstruktor koji stvara report element od odgovaraju\u0107eg IModel-a, uz
   * odre\u0111ene vrijednosti propertija.<p>
   * @param model IModel report elementa.
   * @param defaults niz default vrijednosti za propertije.
   */
  public raReportElement(IModel model, String[] defaults) {
    this.model = model;
    this.properties = ep.MODEL_PROPS[getModelIndex()];
    this.propertyDefaults = ev.MODEL_DEFS[getModelIndex()];
    if (defaults != null)
      this.defaults = defaults;
    else {
      this.defaults = new String[properties.length];
      for (int i = 0; i < this.defaults.length; i++)
        this.defaults[i] = "";
    }
  }

  /**
   * Postavlja default propertije za ovaj element, u slucaju da je element kreiran
   * sa konstruktorom raReportElement(IModel).<p>
   * @param defaults popis default vrijednosti za propertije elementa.
   */
  public void setDefaults(String[] defaults) {
    this.defaults = defaults;
  }

  /**
   * vraca default propertije ovog elementa.<p>
   * @return popis default vrijednosti za propertije elementa.
   */
  public String[] getDefaults() {
    return defaults;
  }

  /**
   * Postavlja default vrijednost jednog propertija. Rijetko je potrebno rucno
   * prckati po default propertijima, jer se oni generiraju automatski iz .template
   * fajlova.<p>
   * Zapravo, zakljucio sam da i nije tako rijetko, pa sam dodao hrpu metoda
   * za lakse prckanje po defaultima.<p>
   * @param property property \u010Dija se default vrijednost postavlja.
   * @param value vrijednost propertija.
   */
  public void setDefault(String property, String value) {
    for (int i = 0; i < properties.length; i++)
      if (property.equals(properties[i])) {
        defaults[i] = value;
//        restoreDefault(property);
      }
  }

  /**
   * Getter za default vrijednost propertija.<p>
   * @param property property \u010Dija se default vrijednost traži
   * @return default vrijednost propertija.
   */
  public String getDefault(String property) {
    for (int i = 0; i < properties.length; i++)
      if (property.equals(properties[i]))
        return (defaults[i] == "" ? propertyDefaults[i] : defaults[i]);
    return "";
  }

  private void restoreDefault(int i) {
    if (!defaults[i].equals(""))
      setProperty(properties[i], defaults[i]);
    else
      setProperty(properties[i], propertyDefaults[i]);
  }

  /**
   * Postavlja default vrijednosti svih propertija ovog elementa (prije eventualnih
   * izmjena putem ReportModifiera).
   */
  public void restoreDefaults() {
    if (defaults != null)
      for (int i = 0; i < properties.length; i++)
        restoreDefault(i);
  }

  /**
   * Postavlja default vrijednost odre\u0111enog propertija (raElixirPropertier.XXX).<p>
   * @param property property kojem se postavlja default vrijednost.
   */
  public void restoreDefault(String property) {
    if (defaults != null)
      for (int i = 0; i < properties.length; i++)
        if (property.equals(properties[i]))
          restoreDefault(i);
  }

  /**
   * Odre\u0111uje vidljivost ovog report elementa.<p>
   * @param visible true za vidljivi element, false za nevidljivi.
   */
  public void setVisible(boolean visible) {
    setProperty(ep.VISIBLE, visible ? ev.YES : ev.NO);
  }

  /**
   * @return true ako je element vidljiv, false ina\u010De.
   */
  public boolean isVisible() {
    return getProperty(ep.VISIBLE).equals(ev.YES);
  }

  /**
   * Postavlja Control Source property za Text elemente.<p>
   * @param source control source za tekst komponente.
   */
  public void setControlSource(String source) {
    setProperty(ep.CONTROL_SOURCE, source);
  }

  /**
   * @return Control Source ovog Text elementa.
   */
  public String getControlSource() {
    return getProperty(ep.CONTROL_SOURCE);
  }

  /**
   * Postavlja Caption za Label elemente.<p>
   * @param caption tekst labele.
   */
  public void setCaption(String caption) {
    setProperty(ep.CAPTION, caption);
  }

  /**
   * @return text labele.
   */
  public String getCaption() {
    return getProperty(ep.CAPTION);
  }

  public void setPicture(String pic) {
    setProperty(ep.PICTURE, pic);
  }

  public void setSizeMode(String sm) {
    setProperty(ep.SIZE_MODE, sm);
  }

  public long distance(raReportElement other) {
    long dx = getLeft() + getWidth() / 2 - other.getLeft() - other.getWidth() / 2;
    long dy = getTop() + getHeight() / 2 - other.getTop() - other.getHeight() / 2;
    return (dx * dx + dy * dy);
  }

  public boolean intersects(raReportElement other) {
    return (getLeft() + getWidth() > other.getLeft() &&
            getTop() + getHeight() > other.getTop() &&
            getLeft() < other.getLeft() + other.getWidth() &&
            getTop() < other.getTop() + other.getHeight());
  }

  public boolean wouldIntersectIfMoved(raReportElement other, long dx, long dy) {
    return (getLeft() + getWidth() + dx > other.getLeft() &&
            getTop() + getHeight() + dy > other.getTop() &&
            getLeft() + dx < other.getLeft() + other.getWidth() &&
            getTop() + dy < other.getTop() + other.getHeight());
  }

  public Rectangle getBounds() {
    return new Rectangle((int) getLeft(), (int) getTop(), (int) getWidth(), (int) getHeight());
  }

  public Rectangle getBounds(Rectangle r) {
    r.setBounds((int) getLeft(), (int) getTop(), (int) getWidth(), (int) getHeight());
    return r;
  }

  /**
   * Postavlja širinu elementa u decipointima (1/1440 in\u010Da).
   * Interno se zaokružuje na najbližu dvadeseticu, jer Elixir ne zna
   * preciznije.<p>
   * @param width širina elementa.
   */
  public void setWidth(long width) {
    setProperty(ep.WIDTH, "" + ((width + 10) / 20 * 20));
  }
  
  public void alterWidth(long width) {
    setProperty(ep.WIDTH, "" + (getWidth() + (width / 20) * 20));
  }
  
  /**
   * Postavlja širinu elementa u centimetrima.<p>
   * @param width širina elementa u centimetrima.
   */
  public void setWidthCm(double width) {
    setWidth(Math.round(width * RATIO));
  }

  /**
   * @return širinu elementa u decipointima.
   */
  public long getWidth() {
    return Math.round(Double.parseDouble(getProperty(ep.WIDTH)));
  }

  /**
   * @return širinu elementa u centimetrima.
   */
  public double getWidthCm() {
    return Double.parseDouble(getProperty(ep.WIDTH)) / RATIO;
  }

  /**
   * Postavlja visinu elementa u decipointima.<p>
   * @param height visina elementa.
   */
  public void setHeight(long height) {
    setProperty(ep.HEIGHT, "" + ((height + 10) / 20 * 20));
  }

  /**
   * Postavlja visinu elementa u centimetrima.<p>
   * @param height visina elementa u centimetrima.
   */
  public void setHeightCm(double height) {
    setHeight(Math.round(height * RATIO));
  }

  /**
   * @return visinu elementa u decimpointima.
   */
  public long getHeight() {
    return Math.round(Double.parseDouble(getProperty(ep.HEIGHT)));
  }

  /**
   * @return visinu elementa u centimetrima.
   */
  public double getHeightCm() {
    return Double.parseDouble(getProperty(ep.HEIGHT)) / RATIO;
  }

  /**
   * Postavlja Y koordinatu u decipointima.<p>
   * @param top udaljenost od gornjeg ruba.
   */
  public void setTop(long top) {
    setProperty(ep.TOP, "" + ((top + 10) / 20 * 20));
  }
  
  public void moveVert(long top) {
    setProperty(ep.TOP, "" + (getTop() + (top / 20) * 20));
  }

  /**
   * Postavlja Y koordinatu u centimetrima.<p>
   * @param top udaljenost od gornjeg ruba u centimetrima.
   */
  public void setTopCm(double top) {
    setTop(Math.round(top * RATIO));
  }


  /**
   * @return Y koorinatu u decipointima.
   */
  public long getTop() {
    return Math.round(Double.parseDouble(getProperty(ep.TOP)));
  }

  public long getBottom() {
    return getTop() + getHeight();
  }

  public long getTop(raReportElement other) {
    return Math.min(getTop(), other.getTop());
  }

  public long getBottom(raReportElement other) {
    return Math.max(getBottom(), other.getBottom());
  }

  /**
   * @return Y koordinatu u centimetrima.
   */
  public double getTopCm() {
    return Double.parseDouble(getProperty(ep.TOP)) / RATIO;
  }

  /**
   * Postavlja X koordinatu u decipointima.<p>
   * @param left udaljenost od lijevog ruba.
   */
  public void setLeft(long left) {
    setProperty(ep.LEFT, "" + ((left + 10) / 20 * 20));
  }

  public void moveHor(long left) {
    setProperty(ep.LEFT, "" + (getLeft() + (left / 20) * 20));
  }
  
  /**
   * Postavlja X koordinatu u centimetrima.<p>
   * @param left udaljenost od lijevog ruba u centimetrima.
   */
  public void setLeftCm(double left) {
    setLeft(Math.round(left * RATIO));
  }

  /**
   * @return X koordinatu u decipointima.
   */
  public long getLeft() {
    return Math.round(Double.parseDouble(getProperty(ep.LEFT)));
  }

  public long getRight() {
    return getLeft() + getWidth();
  }

  public long getLeft(raReportElement other) {
    return Math.min(getLeft(), other.getLeft());
  }

  public long getRight(raReportElement other) {
    return Math.max(getRight(), other.getRight());
  }

  /**
   * @return X koordinatu u centimetrima.
   */
  public double getLeftCm() {
    return Double.parseDouble(getProperty(ep.LEFT)) / RATIO;
  }

  public void setFont(String fontName) {
    setProperty(ep.FONT_NAME, fontName);
  }

  /**
   * Postavlja boju teksta ovog elementa. Mogu se koristiti konstante
   * raElixirPropertyValue.XXX, ili broj\u010Dane vrijednosti u Stringu.<p>
   * @param color boja teksta.
   */
  public void setFontColor(String color) {
    setProperty(ep.FONT_COLOR, color);
  }

  public void setFontSize(int size) {
    setProperty(ep.FONT_SIZE, String.valueOf(size));
  }

  public void setFontItalic(boolean yesno) {
    setProperty(ep.ITALIC, yesno ? ev.YES : ev.NO);
  }

  public void setFontBold(boolean yesno) {
    setProperty(ep.FONT_WEIGHT, yesno ? ev.BOLD : ev.PLAIN);
  }

  public void setTextAlign(String align) {
    setProperty(ep.ALIGN, align);
  }

  public void setAlignment(String align) {
    setProperty(ep.ALIGNMENT, align);
  }

  /**
   * Postavlja boju pozadine elementa.<p>
   * @param color boja pozadine.
   */
  public void setBackColor(String color) {
    setProperty(ep.BACK_COLOR, color);
  }

  /**
   * Postavlja na\u010Din ispisa (raElixirProperyValues.BOLD ili NORMAL).<p>
   * @param weight na\u010Din ispisa.
   */
//  public void setFontWeight(String weight) {
//    setProperty(ep.FONT_WEIGHT, weight);
//  }

  /**
   * Vra\u0107a true ako ovaj element ima odre\u0111eni property.<p>
   * @param property traženi property.
   * @return true ako ovaj element ima doti\u010Dni property.
   */
  public boolean hasProperty(String property) {
    for (int i = 0; i < properties.length; i++)
      if (property.equals(properties[i]))
        return true;

    return false;
  }
  
  public boolean isText() {
    return model.getPropertyValue(ep.MODEL_NAME).equals(ep.TEXT);
  }
  
  public boolean isLabel() {
    return model.getPropertyValue(ep.MODEL_NAME).equals(ep.LABEL);
  }

  /**
   * Vra\u0107a vrijednost nekog propertija ovog elementa. Baca exception ako
   * element nema traženi property.<p>
   * @param property property \u010Dija se vrijednost traži.
   * @return vrijednost traženog propertija.
   */
  public String getProperty(String property) {
    if (!this.hasProperty(property))
      throw new RuntimeException("Model '" + model.getPropertyValue(ep.MODEL_NAME) +
          "' doesn't have property '" + property + "'");
    return (!defaultMode ? model.getPropertyValue(property) : getDefault(property));
  }

  /**
   * Postavlja vrijednost nekog propertija. Baca exception ako element nema
   * doti\u010Dni property.<p>
   * @param property property \u010Dija se vrijednost setira.
   * @param value vrijednost propertija.
   */
  public void setProperty(String property, String value) {
    if (!this.hasProperty(property))
      throw new RuntimeException("Model '" + model.getPropertyValue(ep.MODEL_NAME) +
          "' doesn't have property '" + property + "'");
    try {
      if (!defaultMode) model.setPropertyValue(property, value);
      else setDefault(property, value);
    } catch (Exception e) {
      throw new RuntimeException("Duplicate exception for model '" +
         model.getPropertyValue(ep.MODEL_NAME) +
         " (" + property + " = " + value + ")");
    }
  }

  private int getModelIndex() {
    String m = model.getPropertyValue(ep.MODEL_NAME);
    for (int i = 0; i < ep.MODELS.length; i++)
      if (m.equals(ep.MODELS[i]))
        return i;

    throw new RuntimeException("Unknown model '" + m +
          "' (" + model.getPropertyValue(ep.NAME) + ")");
  }

  public boolean equals(Object o) {
    if (o instanceof raReportElement) {
      raReportElement other = (raReportElement) o;
      return defaults == other.defaults;
    } else return false;
  }

/*  public String toString() {
    String main = "";
    if (hasProperty(ep.CONTROL_SOURCE))
      main = " (SOURCE=" + getDefault(ep.CONTROL_SOURCE) + ")";
    else if (hasProperty(ep.CAPTION))
      main = " (CAPTION=" + getDefault(ep.CAPTION) + ")";
    return (defaultMode ? "*" : "") + "raReportElement" + main + ": " +
        getDefault(ep.LEFT) + " " + getDefault(ep.TOP) + " " +
        getDefault(ep.WIDTH) + " " + getDefault(ep.HEIGHT);
  }*/

  public String toString() {
    String main = "";
    if (hasProperty(ep.CONTROL_SOURCE))
      main = " (SOURCE=" + getProperty(ep.CONTROL_SOURCE) + ")";
    else if (hasProperty(ep.CAPTION))
      main = " (CAPTION=" + getProperty(ep.CAPTION) + ")";
    return (defaultMode ? "*" : "") + "raReportElement" + main + ": " +
        getProperty(ep.LEFT) + " " + getProperty(ep.TOP) + " " +
        getProperty(ep.WIDTH) + " " + getProperty(ep.HEIGHT);
  }
}
