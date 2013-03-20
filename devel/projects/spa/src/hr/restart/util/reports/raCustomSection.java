/****license*****************************************************************
**   file: raCustomSection.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;

import sg.com.elixir.reportwriter.xml.IModel;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raCustomSection implements raElixirProperties, raElixirPropertyValues {

  public static boolean outputHeaderDefined(String corg) {
    return lookupData.getlookupData().raLocate(dM.getDataModule().getLogodat(),
        new String[] {"CORG", "VRSTA", "VRDOK"}, new String[] {corg, "H", "I"});
  }

  public static boolean outputHeaderDefined(String corg, String vrdok) {
    return lookupData.getlookupData().raLocate(dM.getDataModule().getLogodat(),
        new String[] {"CORG", "VRSTA", "VRDOK"}, new String[] {corg, "H", vrdok});
  }

  public static boolean outputFooterDefined(String corg) {
    return lookupData.getlookupData().raLocate(dM.getDataModule().getLogodat(),
        new String[] {"CORG", "VRSTA", "VRDOK"}, new String[] {corg, "F", "I"});
  }

  public static boolean outputFooterDefined(String corg, String vrdok) {
    return lookupData.getlookupData().raLocate(dM.getDataModule().getLogodat(),
        new String[] {"CORG", "VRSTA", "VRDOK"}, new String[] {corg, "F", vrdok});
  }

  public static boolean customDefinedFor(String corg, String vrdok, String vrsec) {
    return lookupData.getlookupData().raLocate(dM.getDataModule().getLogodat(),
        new String[] {"CORG", "VRSTA", "VRDOK", "VRSEC"},
        new String[] {corg, "C", vrdok, vrsec});
  }

  public static boolean anythingDefinedFor(String corg, String vrsta, String vrdok, String vrsec) {
    if (vrsta.equals("H"))
      return outputHeaderDefined(corg) || outputHeaderDefined(corg, vrdok);
    else if (vrsta.equals("F"))
      return outputFooterDefined(corg) || outputFooterDefined(corg, vrdok);
    else return customDefinedFor(corg, vrdok, vrsec);
  }

//  public static boolean innerHeaderDefined(String corg) {
//    return lookupData.getlookupData().raLocate(dM.getDataModule().getDiorep(),
//        new String[] {"CORG", "VRSEC"}, new String[] {corg, "HUD"});
//  }
//
//  public static boolean innerFooterDefined(String corg) {
//    return lookupData.getlookupData().raLocate(dM.getDataModule().getDiorep(),
//        new String[] {"CORG", "VRSEC"}, new String[] {corg, "FUD"});
//  }
//
//  public static boolean footerDefinedFor(String corg, String vrdok) {
//    return lookupData.getlookupData().raLocate(dM.getDataModule().getDiorep(),
//        new String[] {"CORG", "VRSEC", "VRDOK"}, new String[] {corg, "FUD", vrdok});
//  }

/*  public static boolean definedFor(String vrdok, String vrsta) {
    return lookupData.getlookupData().raLocate(dM.getDataModule().getLogodat(),
        new String[] {"VRDOK", "VRSTA"}, new String[] {vrdok, vrsta}) ||
           lookupData.getlookupData().raLocate(dM.getDataModule().getLogodat(),
        new String[] {"VRDOK", "VRSTA"}, new String[] {"", vrsta});
  }

  public static raCustomSection create(raReportTemplate owner, String vrdok, String vrsta) {
    DataSet sect;
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getLogodat(),
        new String[] {"VRDOK", "VRSTA"}, new String[] {vrdok, vrsta}))
      sect = Logodat.getDataModule().getTempSet(Condition.equal("VRDOK", vrdok).
        and(Condition.equal("VRSTA", vrsta)));
    else sect = Logodat.getDataModule().getTempSet(Condition.equal("VRDOK", "").
        and(Condition.equal("VRSTA", vrsta)));
    if (vrsta.equalsIgnoreCase("H"))
      return new raCustomSection(owner.template.getModel(raElixirProperties.PAGE_HEADER), sect);
    else
      return new raCustomSection(owner.template.getModel(raElixirProperties.PAGE_FOOTER), sect);
  }

  private raCustomSection(IModel model, DataSet sect) {
    super(model);
    model.removeAllModels();

    sect.open();
    System.out.println(sect);
    for (sect.first(); sect.inBounds(); sect.next()) {
      if (sect.getShort("RBR") == 0) {
        this.setDefault(ep.HEIGHT, String.valueOf(sect.getInt("VPOS") * 20));
      } else {
        if (sect.getString("TIP").equalsIgnoreCase("T")) {
          raReportElement label = addModel(ep.LABEL, null);
          label.setDefault(ep.LEFT, String.valueOf(sect.getInt("HPOS") * 20));
          label.setDefault(ep.TOP, String.valueOf(sect.getInt("VPOS") * 20));
          label.setDefault(ep.WIDTH, String.valueOf(sect.getInt("SIRINA") * 20));
          label.setDefault(ep.HEIGHT, String.valueOf(sect.getInt("VISINA") * 20));
          label.setDefault(ep.CAPTION, sect.getString("TEKST"));
          String[] font = new VarStr(sect.getString("FONT")).split(';');
          label.setDefault(ep.FONT_NAME, font[0]);
          label.setDefault(ep.FONT_SIZE, String.valueOf(Math.round(Float.parseFloat(font[1]))));
          if (font.length > 2 && font[2].equalsIgnoreCase("B"))
            label.setDefault(ep.FONT_WEIGHT, ev.BOLD);
          if (font.length > 3 && font[3].equalsIgnoreCase("I"))
            label.setDefault(ep.ITALIC, ev.YES);
        }
      }
    }
  }*/

  private static void setGrayShade(IModel model, String prop, int shade) {
    try {
      int col = (255 << 24) | (shade << 16) | (shade << 8) | shade;
      model.setPropertyValue(prop, String.valueOf(col));
    } catch (Exception e) {}
  }

  private static void replaceGrayShade(IModel model, String orig, int shade) {
    if (NORMAL.equals(model.getPropertyValue(BACK_STYLE)) &&
        orig.equals(model.getPropertyValue(BACK_COLOR)))
      setGrayShade(model, BACK_COLOR, shade);

    if (SOLID.equals(model.getPropertyValue(BORDER_STYLE)) &&
        orig.equals(model.getPropertyValue(BORDER_COLOR)))
      setGrayShade(model, BORDER_COLOR, shade);

    Enumeration models = model.getModels();
    while (models.hasMoreElements())
      replaceGrayShade((IModel) models.nextElement(), orig, shade);
  }

  public static void replaceLightGray(IModel template, int shade) {
    replaceGrayShade(template, LIGHT_GRAY, shade);
  }

  public static void replaceGray(IModel template, int shade) {
    replaceGrayShade(template, GRAY, shade);
  }

  public static void replaceDarkGray(IModel template, int shade) {
    replaceGrayShade(template, DARK_GRAY, shade);
  }

  public static void ensureVisibility(IModel template, String targ) {
    boolean header;
    if ((header = targ.startsWith(SECTION_HEADER)) || targ.startsWith(SECTION_FOOTER)) {
      String num = targ.substring(targ.length() - 1);
//      System.out.println(targ);
//      System.out.println(num);
      try {
        IModel sect = template.getModel(SECTIONS).getModel(SECTION + num);
        sect.setPropertyValue(header ? GROUP_HEADER : GROUP_FOOTER, YES);
        template.getModel(targ).setPropertyValue(CAPTION, sect.getPropertyValue(FIELD));
        template.getModel(targ).setPropertyValue(VISIBLE, YES);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void ensureSignature(raReportSection pf) {
    if (frmParam.getParam("sisfun", "disableSig", "N", "Iskljuèiti Rest-artov sig " +
            "na svim ispisima (D,N)").equalsIgnoreCase("D")) return;
    pf.setVisible(true);
    if (pf.getHeight() < 160) pf.setHeight(160);
    raReportElement sig = pf.addModel(LABEL);
    sig.setCaption("(c) REST@RT Sustav poslovnih aplikacija (SPA)");
    sig.setFont("Tahoma");
    sig.setFontColor(GRAY);
//    sig.setFontBold(true);
    sig.setFontSize(5);
    sig.setTop(pf.getHeight() - 160);
    sig.setLeft(0);
    sig.setWidth(5000);
    sig.setHeight(160);
  }
  
  public static void ensureSignature(IModel footer) {
    ensureSignature(new raReportSection(footer));
  }
  
  public static void addFisk(raReportSection ps) {
    long max = 0;
    
    for (int i = ps.model.getModelCount() - 1; i >= 0; i--)
      if (Aus.getNumber(ps.model.getModel(i).getPropertyValue(TOP)) +
          Aus.getNumber(ps.model.getModel(i).getPropertyValue(HEIGHT)) > max)
        max = Aus.getNumber(ps.model.getModel(i).getPropertyValue(TOP)) +
        Aus.getNumber(ps.model.getModel(i).getPropertyValue(HEIGHT));
        
/*
    for (int i = 0; i < ps.getModelCount(); i++) {
      System.out.println(ps.getModel(i));
      if (ps.getModel(i).isVisible() && ps.getModel(i).getTop() + ps.getModel(i).getHeight() > max)
        max = ps.getModel(i).getTop() + ps.getModel(i).getHeight();
    } */
    
    System.out.println("addFisk " + max);
    
    raReportElement fisk = ps.addModel(TEXT);
    fisk.setControlSource("FISKRED");
    fisk.setFont("Lucida Bright");
    fisk.setFontSize(7);
    fisk.setTop(max + 40);
    fisk.setHeight(320);
    fisk.setLeft(0);
    fisk.setWidth(10480);
    fisk.setProperty(WRAP, YES);
    fisk.setProperty(GROW, YES);
    fisk.setProperty(SHRINK, YES);
    
    String nf = frmParam.getParam("sisfun", "globalFont", "", "Ime fonta koji zamjenjuje Lucida Bright");
    if (nf != null && nf.length() > 0)
      fisk.setFont(nf);

    ps.setHeight(max + 360);
    
    //Aus.dumpModel(ps.model, 2);
  }
  
  public static void addFisk(Class source, IModel sect) {
    Method[] meth = source.getMethods();
    //Aus.dumpModel(sect, 0);
    for (int i = 0; i < meth.length; i++)
      if (meth[i].getName().equals("getFISKRED"))
        addFisk(new raReportSection(sect));
  }  

  private static String getTextElement(String text, Class source) {
    String[] gets = source == null ? new String[0] : findGetters(source);
    boolean needText = text.indexOf("$PAGE") >= 0 || text.indexOf("$PAGES") >= 0;
    for (int i = 0; i < gets.length && !needText; i++)
      if (text.indexOf(gets[i]) >= 0) needText = true;
    if (!needText) return text;

    VarStr vt = new VarStr(text);
    vt.replaceAll("$PAGES", "\" (pages) \"");
    vt.replaceAll("$PAGE", "\" (page) \"");
    for (int i = 0; i < gets.length; i++)
      vt.replaceAll(gets[i], "\" [".concat(gets[i].substring(1)).concat("] \""));
    vt.insert(0, "=(string-append \"").append("\")");
    return vt.toString();
  }
  
  public static void globalChangeFont(IModel template) {
    String nf = frmParam.getParam("sisfun", "globalFont", "", "Ime fonta koji zamjenjuje Lucida Bright");
    if (nf != null && nf.length() > 0) {
      double ratio;
      String pr = frmParam.getParam("sisfun", "globalRatio", "1.0", "Faktor rastezanja reporta zbog promjene fonta");
      if (pr == null || pr.length() == 0)
        ratio = Aus.getFontHeightRatio(raElixirPropertyValues.LUCIDA_BRIGHT, nf);
      else ratio = Aus.getDecNumber(pr).doubleValue();
      for (int i = 0; i < template.getModelCount(); i++)
        changeFont(template.getModel(i), nf, ratio);
    }
  }
  
  private static void changeFont(IModel model, String nf, double ratio) {
    try {
      String type = model.getPropertyValue(MODEL_NAME);
      System.out.println("chg: "+type);
      if (type.equals(DETAIL) || 
          type.equals(SECTION_HEADER) || type.equals(SECTION_FOOTER) ||
          type.equals(PAGE_HEADER) || type.equals(PAGE_FOOTER) ||
          type.equals(REPORT_HEADER) || type.equals(REPORT_FOOTER)) {
        raReportSection sec = new raReportSection(model, true);
        sec.changeFont(LUCIDA_BRIGHT, nf, ratio);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void moveSection(IModel source, IModel destination) {
    raReportSection src = new raReportSection(source, true);
    raReportSection dest = new raReportSection(destination, true);
    long srcHeight = src.getHeight();
    long destHeight = dest.getHeight();
    if (src.getProperty(SHRINK).equals(YES))
      srcHeight = src.getShrinkedHeight();
    if (srcHeight > 0) {
      dest.setHeight(destHeight + srcHeight);
      dest.moveDown(srcHeight);
      src.moveAll(dest);
      src.setHeight(0);      
    }
  }
  
  public static void modify(IModel model, DataSet sect, Class source, boolean first) {
    raReportSection original = new raReportSection(model);
    long height = original.getHeight(), curr;

    for (sect.first(); sect.inBounds(); sect.next()) {
      if (sect.getShort("RBR") == 0) {
        if (first) {
          if (!sect.getString("VRSTA").equalsIgnoreCase("C") ||
              !original.isVisible()) height = 0;
          else if (original.getProperty(SHRINK).equals(YES)) {
            height = 0;
            for (int i = model.getModelCount() - 1; i >= 0; i--)
              if ((curr = Aus.getNumber(model.getModel(i).getPropertyValue(TOP)) +
                  Aus.getNumber(model.getModel(i).getPropertyValue(HEIGHT)) + 40) > height)
                height = curr;
            original.setProperty(SHRINK, NO);
          }
          sect.setInt("VPOS", (int) height);
        } else height = sect.getInt("VPOS");
        for (int i = model.getModelCount() - 1; i >= 0; i--)
          if (Aus.getNumber(model.getModel(i).getPropertyValue(TOP)) >= height)
            model.removeModel(model.getModel(i).getPropertyValue(NAME));
        original.setHeight(height + (long) sect.getInt("VISINA") * 20);
        if (sect.getString("VRSTA").equalsIgnoreCase("F"))
          ensureSignature(original);
      } else {
        if (sect.getString("TIP").equalsIgnoreCase("T")) {
          raReportElement el;
          String text = getTextElement(sect.getString("TEKST"), source);
          if (text == null) continue;
          if (text.startsWith("=(")) {
            el = original.addModel(TEXT, null);
            el.setControlSource(text);
            el.setProperty(GROW, YES);
            el.setProperty(SHRINK, NO);
            el.setProperty(WRAP, YES);
          } else {
            el = original.addModel(LABEL, null);
            el.setCaption(text);
          }
          el.setLeft((long) sect.getInt("HPOS") * 20);
          el.setTop((long) sect.getInt("VPOS") * 20 + height);
          el.setWidth((long) sect.getInt("SIRINA") * 20 + 40);
          el.setHeight((long) sect.getInt("VISINA") * 20 + 40);          

          String[] font = new VarStr(sect.getString("FONT")).split(';');
          el.setFont(font[0]);
          el.setFontSize(Math.round(Float.parseFloat(font[1])));
          el.setFontBold(font.length > 2 && font[2].equalsIgnoreCase("B"));
          el.setFontItalic(font.length > 3 && font[3].equalsIgnoreCase("I"));
          if (sect.getString("ALIGN").equalsIgnoreCase("L"))
            el.setTextAlign(raElixirPropertyValues.LEFT);
          else if (sect.getString("ALIGN").equalsIgnoreCase("R"))
            el.setTextAlign(raElixirPropertyValues.RIGHT);
          else el.setTextAlign(raElixirPropertyValues.CENTER);
        } else if (sect.getString("TIP").equalsIgnoreCase("P")) {
          raReportElement pic = original.addModel(IMAGE, null);
          pic.setLeft((long) sect.getInt("HPOS") * 20);
          pic.setTop((long) sect.getInt("VPOS") * 20 + height);
          pic.setWidth((long) sect.getInt("SIRINA") * 20);
          pic.setHeight((long) sect.getInt("VISINA") * 20);
          String pict = findPicture(sect.getString("TEKST"));
          System.out.println(pict);
          pic.setPicture(pict);
          pic.setAlignment(CENTER);
          pic.setSizeMode(sect.getString("ALIGN").equalsIgnoreCase("Z") ? STRETCH : CLIP);
        } else if (sect.getString("TIP").equalsIgnoreCase("H")) {
          raReportElement line = original.addModel(LINE, null);
          line.setLeft((long) sect.getInt("HPOS") * 20);
          line.setTop((long) sect.getInt("VPOS") * 20 + 40 + height);
          line.setWidth((long) sect.getInt("SIRINA") * 20);
          line.setHeight((long) 0);
          line.setProperty(BORDER_WIDTH, String.valueOf(sect.getInt("VISINA")));
        } else if (sect.getString("TIP").equalsIgnoreCase("V")) {
          raReportElement line = original.addModel(LINE, null);
          line.setLeft((long) sect.getInt("HPOS") * 20 + 40);
          line.setTop((long) sect.getInt("VPOS") * 20 + height);
          line.setWidth((long) 0);
          line.setHeight((long) sect.getInt("VISINA") * 20);
          line.setProperty(BORDER_WIDTH, String.valueOf(sect.getInt("SIRINA")));
        }
      }
    }
  }

  private static String[] findGetters(Class source) {
    ArrayList getters = new ArrayList();
    HashSet objMethods = new HashSet(Arrays.asList(Object.class.getMethods()));
    objMethods.addAll(Arrays.asList(raReportData.class.getMethods()));
    Method[] meth = source.getMethods();
    for (int i = 0; i < meth.length; i++)
      if (raElixirDatasource.valids.contains(meth[i].getReturnType().getName()) 
          && meth[i].getParameterTypes().length == 0 && !objMethods.contains(meth[i])) {
        String n = meth[i].getName();
        String f = n.startsWith("get") ? n.substring(3) : n;
        getters.add("$".concat(f));
      }
    return (String[]) getters.toArray(new String[getters.size()]);
  }

  private static String findPicture(String src) {
    try {
      File f = Aus.findFileAnywhere(src);
      if (f != null) return f.toURL().toString();
      java.net.URL url = new java.net.URL(src);
      if (url.getProtocol().equalsIgnoreCase("http")) return url.toString();
      return "";
    } catch (Exception e) {
      return "";
    }
  }
}

