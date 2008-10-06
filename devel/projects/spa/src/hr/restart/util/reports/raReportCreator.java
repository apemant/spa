/****license*****************************************************************
**   file: raReportCreator.java
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

import hr.restart.util.VarStr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import sg.com.elixir.reportwriter.xml.IModel;
import sg.com.elixir.reportwriter.xml.ModelFactory;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raReportCreator implements raElixirProperties {
  private LinkedList output = new LinkedList();
  private LinkedList properties = new LinkedList();
  private LinkedList creator = new LinkedList();
  private LinkedList methods = new LinkedList();
  private HashSet names = new HashSet();
  private IModel rt;
  private int textCount, labelCount, lineCount, rectangleCount;
  private String className;
  private String path = "";
  private String pack = "";
  private String sect = "";

  private static IModel[] defaults = new IModel[MODELS.length];

  static {
    setDefaults();
  }

  private static void setDefaults() {
    for (int i = 0; i < defaults.length; i++)
      defaults[i] = null;

    defaults[0] = ModelFactory.getModel(REPORT_TEMPLATE);
    ModelFactory.setCurrentReport(defaults[0]);
    Enumeration models = defaults[0].getModels();
    IModel m;
    while (models.hasMoreElements()) {
      m = (IModel) models.nextElement();
      defaults[getModelIndex(m)] = m;
    }
    m = defaults[0].getModel(SECTIONS).getModel(SECTION + 0);

    for (int i = 0; i < defaults.length; i++)
      if (defaults[i] == null)
        defaults[i] = ModelFactory.getModel(MODELS[i]);
  }

  private raReportCreator() {
  }

  public raReportCreator(IModel m, String pack, String path, String section) {
    this.pack = pack;
    this.path = path;
    this.sect = section;
    rt = m;
    createOutput();
    writeOutput();
  }

  public raReportCreator(IModel m, String pack) {
    this(m, pack, "", "");
  }

  public raReportCreator(IModel m) {
    this(m, "", "", "");
  }

  public void writeOutput() {
    String fn = path.equals("") ? className + ".java" : path;
    BufferedWriter outf;
    try {
      outf = new BufferedWriter(new FileWriter(fn));
    } catch (Exception ex) {
      System.err.println("error opening file");
      return;
    }
    try {
      Iterator i = output.iterator();
      while (i.hasNext()) {
         outf.write((String) i.next());
         outf.newLine();
      }
    } catch (Exception ex) {
      System.err.println("error writing file");
    }
    try {
      outf.close();
    } catch (Exception ex) {}
    System.out.println(fn + " written.");
  }

  private void createImports() {
    if (!sect.equals(""))
      output.add("package hr.restart.util.reports;");
    else if (pack.equals(""))
      output.add("package hr.restart; /**@todo: ime pekidža */");
    else
      output.add("package hr.restart." + pack + ";");
    if (sect.equals("")) {
      output.add("");
      output.add("import sg.com.elixir.*;");
      output.add("import sg.com.elixir.reportwriter.xml.*;");
      output.add("");
      output.add("import hr.restart.util.reports.*;");
    }
    output.add("");
  }

  private void createClassHeader() {
    if (path.equals(""))
      className = rt.getPropertyValue(RECORD_SOURCE).substring(3) + "OrigTemplate";
    else if (path.lastIndexOf('\\') > 0)
      className = path.substring(path.lastIndexOf('\\') + 1, path.lastIndexOf('.'));
    else
      className = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
    if (sect.equals("")) {
      output.add("public abstract class " + className + " extends raReportTemplate {");
      output.add("  public raElixirProperties ep = raElixirPropertiesInstance.get();");
    } else
      output.add("public class " + className + " extends raReportSection {");
    output.add("");
  }

  private void createFields() {
    output.addAll(properties);
  }

  private void createMethods() {
    output.add("");
    if (sect.equals("")) {
      output.add("  public " + className + "() {");
      output.add("    createReportStructure();");
      output.add("    setReportProperties();");
      output.add("  }");
    }
    output.addAll(methods);
    if (sect.equals(""))
      output.addAll(creator);
    else {
      output.add("");
      output.add("  private void modifyThis() {");
      output.add("  }");
    }
  }

  private void createFooter() {
    output.add("}");
  }

  private void createOutput() {
    output.clear();
    properties.clear();
    creator.clear();
    methods.clear();
    names.clear();
    textCount = labelCount = lineCount = rectangleCount = 0;
    createImports();
    createClassHeader();
    parseTemplate();
    createFields();
    createMethods();
    createFooter();
  }

/*  private void replaceString(StringBuffer s, String orig, String chg) {
    int offset = 0;
    while ((offset = s.toString().indexOf(orig, offset)) != -1) {
      s.replace(offset, offset + orig.length(), chg);
      offset += chg.length();
    }
  }*/

  private static int getModelIndex(IModel m) {
    for (int i = 0; i < MODELS.length; i++)
      if (m.getPropertyValue(MODEL_NAME).equals(MODELS[i]))
        return i;

    throw new RuntimeException("Unknown model");
  }

  private String[] getPropertyNames(IModel m) {
    return MODEL_PROPS[getModelIndex(m)];
  }

  private String getModelName(IModel m) {
    return MODEL_NAMES[getModelIndex(m)];
  }

  private String noDuplicate(String name) {
    String checked = name;
    if (!names.add(name))
      for (int i = 1; !names.add(checked = name + i); i++);
    return checked;
  }

  private String getWord(String word) {
    VarStr out = new VarStr(16);

    for (int i = 0; i < word.length(); i++) {
      char c = word.charAt(i);
      if (c == '\u010C' || c == '\u0106') out.append('C');
      if (c == '\u0110') out.append("Dj");
      if (c == 'Š') out.append('S');
      if (c == 'Ž') out.append('Z');
      if (c == '\u010D' || c == '\u0107') out.append('c');
      if (c == '\u0111') out.append("dj");
      if (c == 'š') out.append('s');
      if (c == 'ž') out.append('z');

      if (((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z')
           || (c >= 'A' && c <= 'Z') || c == '_')) out.append(c);
    }
    return out.toString();
  }

  private String findLabelName(IModel m) {
    if (m.getPropertyValue(NAME).startsWith(LABEL)) {
      String name = "", caption = m.getPropertyValue(CAPTION);
      StringTokenizer st = new StringTokenizer(caption);
      int count = st.countTokens();
      for (int i = 0; i < count && (i < 4 || name.length() < 16); i++)
        name = name + (i > 0 ? "_" : "") + getWord(st.nextToken().trim());
      if (name.equals(""))
        return LABEL + ++labelCount;
      else return LABEL + name;
    } else return m.getPropertyValue(NAME);
  }

  private String findTextName(IModel m) {
    if (m.getPropertyValue(NAME).startsWith(TEXT)) {
      String source = m.getPropertyValue(CONTROL_SOURCE);
      if (!source.trim().startsWith("="))
        return TEXT + getWord(source.trim());
      else return TEXT + ++textCount;
    } else return m.getPropertyValue(NAME);
  }

  private String findLineName(IModel m) {
    if (m.getPropertyValue(NAME).startsWith(LINE))
      return LINE + ++lineCount;
    else return m.getPropertyValue(NAME);
  }

  private String findRectangleName(IModel m) {
    if (m.getPropertyValue(NAME).startsWith(RECTANGLE))
      return RECTANGLE + ++rectangleCount;
    else return m.getPropertyValue(NAME);
  }

  private String findImageName(IModel m) {
    if (m.getPropertyValue(NAME).startsWith(IMAGE))
      return IMAGE + ++rectangleCount;
    else return m.getPropertyValue(NAME);
  }

  private String createModelName(IModel m) {
    VarStr varName = new VarStr(16);
    String type = m.getPropertyValue(MODEL_NAME);
    boolean primitive;
    if ((primitive = type.equals(TEXT)))
      varName.append(noDuplicate(findTextName(m)));
    else if ((primitive = type.equals(LABEL)))
      varName.append(noDuplicate(findLabelName(m)));
    else if ((primitive = type.equals(LINE)))
      varName.append(noDuplicate(findLineName(m)));
    else if ((primitive = type.equals(RECTANGLE)))
      varName.append(noDuplicate(findRectangleName(m)));
    else if ((primitive = type.equals(IMAGE)))
      varName.append(noDuplicate(findImageName(m)));
    else {
      varName.append(m.getPropertyValue(NAME));
      primitive = type.equals(SECTION);
    }
    varName.replaceAll(" ", "");
    if (primitive)
      properties.add("  public raReportElement " + varName + ";");
    else
      properties.add("  public raReportSection " + varName + ";");

    return varName.toString();
  }

  private String createPropertyTable(IModel m) {
    String name = createModelName(m);
    String defval, val;
    String[] props = getPropertyNames(m);
    VarStr line = new VarStr(256);
    VarStr value = new VarStr(64);

    if (m.getPropertyValue(NAME).equals(sect)) name = "this";
    line.append("  private String[] ").append(name).append("Props = new String[] {");

    for (int i = 0; i < props.length; i++) {
      value.clear();
      defval = defaults[getModelIndex(m)].getPropertyValue(props[i]);
      val = m.getPropertyValue(props[i]);

      if (props[i].equals(TOP) || props[i].equals(LEFT) ||
          props[i].equals(WIDTH) || props[i].equals(HEIGHT))
        val = String.valueOf(((long) (Double.parseDouble(val) / 20)) * 20);

      if (!val.equals(defval)) {
        value.append(val);
        value.replaceAll("\\", "\\\\").replaceAll("\n", "\\n").replaceAll("\"", "\\\"");
        value.replaceAll("\u0110", "\\u0110").replaceAll("\u0111", "\\u0111");
        value.replaceAll("\u0106", "\\u0106").replaceAll("\u0107", "\\u0107");
        value.replaceAll("\u010C", "\\u010C").replaceAll("\u010D", "\\u010D");
      }
      if (line.length() + value.length() + 3 > 100 && line.length() > 5) {
        properties.add(line.toString());
        line.clear().append("     ");
      }
      line.append("\"").append(value).append("\", ");
    }
    line.chop(2).append("};");
    properties.add(line.toString());
    return name;
  }

  private LinkedList createSections() {
    IModel section;
    IModel sections = rt.getModel(SECTIONS);
    LinkedList h = new LinkedList();
    LinkedList f = new LinkedList();
    String sname = null, secname;

    h.addLast(REPORT_HEADER);
    h.addLast("ep.REPORT_HEADER");
    h.addLast(PAGE_HEADER);
    h.addLast("ep.PAGE_HEADER");
    f.addFirst("ep.REPORT_FOOTER");
    f.addFirst(REPORT_FOOTER);
    f.addFirst("ep.PAGE_FOOTER");
    f.addFirst(PAGE_FOOTER);

    for (int i = 0; i < 10; i++) {
      section = sections.getModel(SECTION + i);
      if (section != null && !section.getPropertyValue(FIELD).trim().equals("")) {
        if (sname == null) {
          sname = createModelName(sections);
          methods.add("");
          methods.add("  public raReportSection createSections() {");
          methods.add("    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));");
          methods.add("");
          creator.add("    " + sname + " = addSection(createSections());");
        }
        secname = createPropertyTable(section);
        methods.add("    " + secname + " = sect.getModel(ep.SECTION + " +
                  i + ", " + secname + "Props);");
        if (section.getPropertyValue(GROUP_HEADER).trim().equals("Yes")) {
          h.addLast(SECTION_HEADER + i);
          h.addLast("ep.SECTION_HEADER + " + i);
        }
        if (section.getPropertyValue(GROUP_FOOTER).trim().equals("Yes")) {
          f.addFirst("ep.SECTION_FOOTER + " + i);
          f.addFirst(SECTION_FOOTER + i);
        }
      }
    }
    methods.add("    return sect;");
    methods.add("  }");
    h.addLast(DETAIL);
    h.addLast("ep.DETAIL");
    h.addAll(f);
    return h;
  }

  private void createSection(String sname, String ssname) {
    IModel comp;
    IModel m = rt.getModel(sname);
    String name;
    String cname = createPropertyTable(m);
    if (sect.equals("")) {
      methods.add("");
      methods.add("  public raReportSection create" + cname + "() {");
      methods.add("    raReportSection sect = new raReportSection(template.getModel(" +
                  ssname + "), " + cname + "Props);");
      methods.add("");
      creator.add("    " + cname + " = addSection(create" + cname + "());");
    } else {
      properties.removeFirst();
      for (int i = 0; i < 10; i++)
        if (sname.endsWith("" + i))
          ssname = ssname + " + " + i;
      methods.add("  public " + className + "(raReportTemplate owner) {");
      methods.add("    super(owner.template.getModel(raElixirProperties." + ssname + "));");
      methods.add("    this.setDefaults(thisProps);");
      methods.add("");
      methods.add("    addElements();");
      methods.add("");
      methods.add("    addReportModifier(new ReportModifier() {");
      methods.add("      public void modify() {");
      methods.add("        modifyThis();");
      methods.add("      }");
      methods.add("    });");
      methods.add("  }");
      methods.add("");
      methods.add("  private void addElements() {");
    }
    IModel[] models = new IModel[m.getModelCount()];
    Enumeration comps = m.getModels();
    for (int i = 0; i < models.length; i++)
      models[i] = (IModel) comps.nextElement();
    Arrays.sort(models, new Comparator() {
      public int compare(Object o1, Object o2) {
        if ((o1 instanceof IModel) && (o2 instanceof IModel))
          return (int) (Float.parseFloat(((IModel) o1).getPropertyValue(TOP)) -
          Float.parseFloat(((IModel) o2).getPropertyValue(TOP)));
        else return 0;
      }
    });

//    while (comps.hasMoreElements()) {
//      comp = (IModel) comps.nextElement();
    for (int i = 0; i < models.length; i++) {
      comp = models[i];
      name = createPropertyTable(comp);
      if (sect.equals(""))
        methods.add("    " + name + " = sect.addModel(ep." + getModelName(comp) + ", "
                           + name + "Props);");
      else
        methods.add("    " + name + " = addModel(ep." + getModelName(comp) + ", "
                           + name + "Props);");
    }
    if (sect.equals("")) {
      if (m.getModelCount() > 0) methods.add("    return sect;");
      else {
        while (!((String) properties.getLast()).trim().startsWith("public"))
          properties.removeLast();

        while (!((String) methods.getLast()).trim().startsWith("public"))
          methods.removeLast();
        methods.removeLast();
        methods.add("  public abstract raReportSection create" + cname + "();");
        return;
      }
    }
    methods.add("  }");
  }

  private void parseTemplate() {
    if (sect.equals("")) {
      LinkedList sections;
      String cname = createPropertyTable(rt);
      creator.add("");
      creator.add("  public void createReportStructure() {");
      creator.add("    template = ModelFactory.getModel(ep.REPORT_TEMPLATE);");
      creator.add("    ModelFactory.setCurrentReport(template);");
      creator.add("");
      creator.add("    " + cname + " = addSection(new raReportSection(template, " + cname + "Props));");
      creator.add("");

      cname = createPropertyTable(rt.getModel(PAGE_SETUP));
      creator.add("    " + cname + " = addSection(new raReportSection(template.getModel(ep.PAGE_SETUP), "
                         + cname + "Props));");

      sections = createSections();
      creator.add("");

  /*
      ReportHeader = addSection(createReportHeader());
      PageHeader = addSection(createPageHeader()); */

      Iterator i = sections.iterator();
      while (i.hasNext())
        createSection((String) i.next(), (String) i.next());

      creator.add("  }");
    } else
      createSection(sect, getModelName(defaults[0].getModel(sect)));
  }
}
