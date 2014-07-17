package hr.restart.util.reports;

import hr.restart.util.Aus;
import hr.restart.util.VarStr;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGraphicElement;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRTextElement;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import sg.com.elixir.reportwriter.xml.IModel;


public class ElixirToJasperConverter {
  private raElixirProperties ep = raElixirPropertiesInstance.get();
  private raElixirPropertyValues ev = raElixirPropertyValuesInstance.get();
  
  private IModel model;
  private JasperElixirData data;
  JasperDesign jas;
  JRDesignGroup currGroup = null;
  boolean isHeader = false;
  
  public ElixirToJasperConverter(IModel model, JasperElixirData data) {
    this.model = model;
    this.data = data;
  }
  
  public static void adjustReport(JasperDesign jas) {
    adjustBand(jas.getTitle());
    adjustBand(jas.getColumnHeader());
    adjustBand(jas.getDetail());
    adjustBand(jas.getColumnFooter());
    adjustBand(jas.getSummary());
    JRGroup[] grs = jas.getGroups();
    for (int i = 0; i < grs.length; i++) {
      adjustBand(grs[i].getGroupHeader());
      adjustBand(grs[i].getGroupFooter());
    }
  }
  
  private static void adjustBand(JRBand band) {
    if (band == null) return;
    JRElement[] els = band.getElements();
    for (int i = 0; i < els.length; i++)
      if (els[i] instanceof JRDesignTextField)
        adjustTextElement((JRDesignTextField) els[i]);
  }
  
  private static void adjustTextElement(JRDesignTextField tf) {
    if (tf.getHeight() <= tf.getFontSize() * 2 && 
        !tf.isStretchWithOverflow()) tf.setWrapAllowed(false);
  }
    
  
  public JasperDesign getJasperDesign() {
    jas = new JasperDesign();
    try {
      createDesign();
      //System.out.println(JRXmlWriter.writeReport(jas, "Cp1250"));
    } catch (JRException e) {
      e.printStackTrace();
      jas = null;
    }
    return jas;
  }
  
  private void createDesign() throws JRException {
    jas.setName("design");
    createFields();
    createLayout();
    createGroups();
    createDetail();
    createPageEdges();
    createReportEdges();
  }
  
  private void createDetail() throws JRException {
    JRDesignBand detail = new JRDesignBand();
    fillBand(detail, model.getModel(ep.DETAIL));
    jas.setDetail(detail);
  }

  private void createReportEdges() throws JRException {
    JRDesignBand title = new JRDesignBand();
    IModel mh = model.getModel(ep.REPORT_HEADER);
    fillBand(title, mh);
    jas.setTitle(title);
    if (ev.AFTER.equals(mh.getPropertyValue(ep.FORCE_NEW)))
      jas.setTitleNewPage(true);
    
    JRDesignBand summ = new JRDesignBand();
    fillBand(summ, model.getModel(ep.REPORT_FOOTER));
    jas.setSummary(summ);
  }
 
  private void createGroups() throws JRException {
    isHeader = false;
    IModel sects = model.getModel(ep.SECTIONS);
    for (int i = 0; i < sects.getModelCount(); i++) {
      IModel sect = sects.getModel(ep.SECTION + i);
      boolean gh = ev.YES.equals(sect.getPropertyValue(ep.GROUP_HEADER));
      boolean gf = ev.YES.equals(sect.getPropertyValue(ep.GROUP_FOOTER));
      if (!gh && !gf) continue;
    
      JRDesignGroup gr = currGroup = new JRDesignGroup();
      gr.setName(sect.getPropertyValue(ep.NAME));
      gr.setExpression(createExpression(sect.getPropertyValue(ep.FIELD)));
      if (gh) {
        IModel mh = model.getModel(ep.SECTION_HEADER + i);
        JRDesignBand bh = new JRDesignBand();
        isHeader = true;
        fillBand(bh, mh);
        isHeader = false;
        gr.setReprintHeaderOnEachPage(ev.YES.equals(mh.getPropertyValue(ep.REPEAT)));
        if (ev.BEFORE.equals(mh.getPropertyValue(ep.FORCE_NEW)))
          gr.setStartNewPage(true);
        gr.setGroupHeader(bh);
      } else gr.setGroupHeader(createEmptyBand());
      if (gf) {
        IModel mf = model.getModel(ep.SECTION_FOOTER + i);
        JRDesignBand bf = new JRDesignBand();
        isHeader = false;
        fillBand(bf, mf);
        if (ev.AFTER.equals(mf.getPropertyValue(ep.FORCE_NEW))) {
          JRGroup tgr = gr;
          JRGroup[] pg = jas.getGroups();
          for (int pgi = pg.length - 1; pgi >= 0; pgi--)
            if (pg[pgi].getExpression().getText().equals(gr.getExpression().getText()))
              tgr = pg[pgi];
          tgr.setStartNewPage(true);
        }
        gr.setGroupFooter(bf);
      } else gr.setGroupFooter(createEmptyBand());
      if (i == 0 && gr.isStartNewPage()) gr.setResetPageNumber(true);
      jas.addGroup(gr);
    }
    isHeader = false;
  }

  private void createPageEdges() throws JRException {
    JRDesignBand ph = new JRDesignBand();
    fillBand(ph, model.getModel(ep.PAGE_HEADER));
    jas.setPageHeader(ph);
    
    JRDesignBand pf = new JRDesignBand();
    fillBand(pf, model.getModel(ep.PAGE_FOOTER));
    jas.setPageFooter(pf);
    jas.setLastPageFooter(pf);
  }
  
  private void fillBand(JRDesignBand band, IModel m) throws JRException {
    if (ev.NO.equals(m.getPropertyValue(ep.VISIBLE))) {
      band.setHeight(0);
      return;
    }
    
    boolean grow = false;
    boolean shrink = false;
    if (!m.getPropertyValue(ep.MODEL_NAME).startsWith("Page")) {
      grow = ev.YES.equals(m.getPropertyValue(ep.GROW));
      shrink = ev.YES.equals(m.getPropertyValue(ep.SHRINK));
    }
    band.setHeight(getCoord(m, ep.HEIGHT));
    band.setSplitAllowed(ev.NO.equals(m.getPropertyValue(ep.KEEP_TOGETHER)));
    int maxy = 0;
    IModel[] mods = getSortedModels(m);
    for (int i = 0; i < mods.length; i++) {
      if (ev.NO.equals(mods[i].getPropertyValue(ep.VISIBLE))) continue;
      JRDesignElement e = createElement(mods[i]);
      if (isPageCounter(mods[i])) {
        JRDesignTextField te1 = (JRDesignTextField) createElement(mods[i]);
        JRDesignTextField te2 = (JRDesignTextField) e;
        if (isForeignCounter(mods[i])) {
          te1.setExpression(createExpression("_fpage1"));
          te2.setExpression(createExpression("_fpage2"));
        } else {
          te1.setExpression(createExpression("_page1"));
          te2.setExpression(createExpression("_page2"));
        }
        
        if (jas.getGroupsList().size() == 0)
          te2.setEvaluationTime(JRExpression.EVALUATION_TIME_REPORT);
        else {
          JRDesignGroup gr = (JRDesignGroup) jas.getGroupsList().get(0);
          if (!gr.isResetPageNumber())
            te2.setEvaluationTime(JRExpression.EVALUATION_TIME_REPORT);
          else {
            te2.setEvaluationTime(JRExpression.EVALUATION_TIME_GROUP);
            te2.setEvaluationGroup(gr);
          }
        }

        te1.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_RIGHT);
        te1.setWidth(te1.getWidth() - 40);
        te1.setRightPadding(0);
        te2.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
        te2.setX(te2.getX() + te2.getWidth() - 40);
        te2.setWidth(40);
        te1.setLeftPadding(0);
        
        if (grow || te1.getY() + te1.getHeight() <= band.getHeight())
          band.addElement(te1);
        if (te1.getY() + te1.getHeight() > maxy)
          maxy = te1.getY() + te1.getHeight();
      } else e.setPositionType(JRElement.POSITION_TYPE_FLOAT);
      
      if (!grow && e.getY() < band.getHeight() && 
          e.getY() + e.getHeight() > band.getHeight())
        e.setHeight(band.getHeight() - e.getY());
      if (grow || e.getY() + e.getHeight() <= band.getHeight())
        band.addElement(e);
      if (e.getY() + e.getHeight() > maxy)
        maxy = e.getY() + e.getHeight();
    }
    if (grow && maxy > band.getHeight()) band.setHeight(maxy);
    if (shrink && maxy < band.getHeight()) band.setHeight(maxy);
  }
  
  private boolean isPageCounter(IModel m) {
    if (!m.getPropertyValue(ep.MODEL_NAME).equals(ep.TEXT)) return false;
    String cs = m.getPropertyValue(ep.CONTROL_SOURCE);
    return cs.startsWith("=(") && cs.indexOf("string-append") > 0 && cs.indexOf("(page)") > 0;
  }
  
  private boolean isForeignCounter(IModel m) {
    if (!m.getPropertyValue(ep.MODEL_NAME).equals(ep.TEXT)) return false;
    String cs = m.getPropertyValue(ep.CONTROL_SOURCE);
    return cs.startsWith("=(") && cs.indexOf("string-append") > 0 && cs.indexOf("\"Page") > 0;
  }

  private JRDesignElement createElement(IModel m) throws JRException {
    String type = m.getPropertyValue(ep.MODEL_NAME);
    if (ep.TEXT.equals(type)) return createTextElement(m);
    else if (ep.LABEL.equals(type)) return createLabelElement(m);
    else if (ep.LINE.equals(type)) return createLineElement(m);
    else if (ep.RECTANGLE.equals(type)) return createRectangleElement(m);
    else if (ep.IMAGE.equals(type)) return createImageElement(m);
    else throw new RuntimeException("Unsupported element type: " + type);
  }
  
  private JRDesignElement createBarcodeElement(IModel m, String getter) {
    JRDesignImage img = new JRDesignImage(null);
    setBounds(img, m);
    img.setLazy(false);
    img.setScaleImage(JRImage.SCALE_IMAGE_FILL_FRAME);
    img.setMode(JRDesignElement.MODE_OPAQUE);
    JRDesignExpression exp = new JRDesignExpression();
    exp.setValueClassName("java.awt.Image");

    exp.addTextChunk("hr.restart.util.reports.Bc.getImg(");
    if (data.isGetter(getter)) {
      exp.addFieldChunk(getter);
      data.setUsedGetter(getter);
    } else exp.addTextChunk('"' + getter + '"');
    exp.addTextChunk(")");
    img.setExpression(exp);
    /*img.setEvaluationTime(JRExpression.EVALUATION_TIME_GROUP);
    img.setEvaluationGroup(currGroup);*/
    return img;
  }
  
  private JRDesignElement createImageElement(IModel m) {
    JRDesignImage img = new JRDesignImage(null);
    setBounds(img, m);
    img.setLazy(false);
    img.setScaleImage(m.getPropertyValue(ep.SIZE_MODE).equals(ev.STRETCH)
        ? JRImage.SCALE_IMAGE_FILL_FRAME : JRImage.SCALE_IMAGE_CLIP);
    img.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
    img.setHorizontalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
    img.setMode(JRDesignElement.MODE_OPAQUE);
    
    JRDesignExpression exp = new JRDesignExpression();
    exp.setValueClassName("java.lang.String");
    try {
      URL ui = new URL(m.getPropertyValue(ep.PICTURE));
      ui.openConnection();
      exp.addTextChunk("\""+m.getPropertyValue(ep.PICTURE)+"\"");
    } catch (Exception ex) {
      ex.printStackTrace();
      exp.addTextChunk("");
    }
    System.out.println(m.getPropertyValue(ep.PICTURE));
    img.setExpression(exp);
    return img;
  }
  
  private byte getPen(IModel m) {
    String wid = m.getPropertyValue(ep.BORDER_WIDTH);
    if (wid.startsWith("1"))
      return JRGraphicElement.PEN_THIN;
    if (wid.startsWith("2"))
      return JRGraphicElement.PEN_1_POINT;
    if (wid.startsWith("3"))
      return JRGraphicElement.PEN_2_POINT;
    return JRGraphicElement.PEN_4_POINT;
  }

  private JRDesignElement createRectangleElement(IModel m) {
    JRDesignRectangle rect = new JRDesignRectangle();
    setBounds(rect, m);

    rect.setPen(getPen(m));
    rect.setForecolor(parseSimpleColor(m.getPropertyValue(ep.BORDER_COLOR)));
    rect.setBackcolor(parseSimpleColor(m.getPropertyValue(ep.BACK_COLOR)));
    rect.setMode(ev.TRANSPARENT.equals(m.getPropertyValue(ep.BACK_STYLE)) 
        ? JRDesignElement.MODE_TRANSPARENT : JRDesignElement.MODE_OPAQUE);
    //rect.setFill(JRGraphicElement.FILL_SOLID)
    return rect;
  }

  private JRDesignElement createLineElement(IModel m) {
    JRDesignLine line = new JRDesignLine();
    setBounds(line, m);
    
    line.setPen(getPen(m));
    line.setForecolor(parseSimpleColor(m.getPropertyValue(ep.BORDER_COLOR)));
    return line;
  }

  private JRDesignElement createLabelElement(IModel m) {
    String cap = m.getPropertyValue(ep.CAPTION);
    if (cap != null && cap.startsWith("#") && cap.endsWith("#") && cap.length() > 2)
      return createBarcodeElement(m, cap.substring(1, cap.length() - 1));
      
    JRDesignStaticText lab = new JRDesignStaticText();
    lab.setText(cap);
    setBounds(lab, m);
    setFont(lab, m);
    ensureLabelMinimumHeight(lab);
    setBox(lab, m);
    return lab;
  }
  
  private void ensureLabelMinimumHeight(JRDesignStaticText e) {
    if (e.getText().trim().length() == 0) return;
    
    LineMetrics lm = new Font(e.getFontName(), e.isBold() ? Font.BOLD : Font.PLAIN, 
        e.getFontSize()).getLineMetrics("", new FontRenderContext(null, false, false));
    int lines = new VarStr(e.getText()).countOccurences('\n') + 1;
    int height = e.getHeight();
    int needed = ((int) (lm.getHeight() + 0.9)) * lines + 1;
    if (height < needed)
      e.setHeight(needed);
  }
  
  private void setBox(JRTextElement e, IModel m) {
    if (!(ev.TRANSPARENT.equals(m.getPropertyValue(ep.BACK_STYLE)))) {
      e.setMode(JRDesignElement.MODE_OPAQUE);
      e.setBackcolor(parseSimpleColor(m.getPropertyValue(ep.BACK_COLOR)));
    }
    int padding = 2;
    if (!(ev.TRANSPARENT.equals(m.getPropertyValue(ep.BORDER_STYLE)))) {
      e.setBorderColor(parseSimpleColor(m.getPropertyValue(ep.BORDER_COLOR)));
      e.setBorder(getPen(m));
      padding = 4;
    }
    String align = m.getPropertyValue(ep.ALIGN);
    if (ev.LEFT.equals(align)) {
      e.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
      e.setLeftPadding(padding);
    } else if (ev.RIGHT.equals(align)) {
      e.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_RIGHT);
      e.setRightPadding(padding);
    } else if (ev.CENTER.equals(align)) 
      e.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
    e.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
    e.setTopPadding(1);
  }

  private void setFont(JRTextElement e, IModel m) {
    e.setFontName(m.getPropertyValue(ep.FONT_NAME));
    e.setFontSize(getModelInt(m, ep.FONT_SIZE));
    e.setForecolor(parseSimpleColor(m.getPropertyValue(ep.FONT_COLOR)));
    e.setBold(ev.BOLD.equals(m.getPropertyValue(ep.FONT_WEIGHT)));
    //e.setPdfEmbedded(true);
    e.setPdfEncoding("Cp1250");
    //if (e.isBold())
    //  e.setPdfFontName("Helvetica-Bold");
    //else e.setPdfFontName("Helvetica");
  }

  private void setBounds(JRDesignElement e, IModel m) {
    e.setX(getCoord(m, ep.LEFT));
    e.setY(getCoord(m, ep.TOP));
    e.setWidth(getCoord(m, ep.WIDTH));
    e.setHeight(getCoord(m, ep.HEIGHT));
  }
  
  private void setFormat(JRDesignTextField e, IModel m) {
    String cn = e.getExpression().getValueClassName();
    if (cn == null) {
      System.out.println("Invalid model: " + m.getPropertyValue(ep.CONTROL_SOURCE));
      return;
    }
    if (cn.equals("java.math.BigDecimal") ||
        cn.equals("java.lang.Double") || 
        cn.equals("java.lang.Float")) {
      String[] fp = new VarStr(m.getPropertyValue(ep.FORMAT)).split('|');
      if (fp.length == 9 && fp[0].equals("Number")) {
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setGroupingUsed(fp[6].equals("true"));
        nf.setMinimumFractionDigits(Aus.getNumber(fp[4]));
        nf.setMaximumFractionDigits(Aus.getNumber(fp[5]));
        e.setPattern(nf.toPattern());
      } else if (fp.length == 3 && fp[0].equals("Number") && fp[1].equals("true")) {
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setGroupingUsed(true);
        int dig = fp[2].endsWith("000") ? 3 : 2;
        nf.setMinimumFractionDigits(dig);
        nf.setMaximumFractionDigits(dig);
        e.setPattern(nf.toPattern());
      }
    } else if (cn.equals("java.util.Timestamp")) {
      
    }
  }

  private JRDesignElement createTextElement(IModel m) throws JRException {
    String cs = m.getPropertyValue(ep.CONTROL_SOURCE);
    if (cs != null && cs.startsWith("EANCODE")) 
      return createBarcodeElement(m, cs);
    
    JRDesignTextField tf = new JRDesignTextField();
    if (cs.indexOf("UIU")>=0) {
      JRDesignExpression pw = new JRDesignExpression();
      pw.setText("$F{UIUprint}");
      pw.setValueClass(Boolean.class);
      tf.setPrintWhenExpression(pw);
      data.setUsedGetter("UIUprint");
    }
    tf.setBlankWhenNull(true);
    tf.setWrapAllowed(!ev.NO.equals(m.getPropertyValue(ep.GROW)));
    tf.setStretchWithOverflow(tf.isWrapAllowed() ||
        ev.YES.equals(m.getPropertyValue(ep.WRAP)));
    if (m.getPropertyValue(ep.RUNNING_SUM).equals(ev.OVER_GROUP) &&
        data.getTypeMap().containsKey(cs)) {
      JRDesignVariable var = new JRDesignVariable();
      var.setName("rsum_" + currGroup.getName() + "_" + cs);
      var.setValueClassName((String) data.getTypeMap().get(cs));
      var.setExpression(createExpression(cs));
      var.setCalculation(JRVariable.CALCULATION_SUM);
      var.setResetType(JRVariable.RESET_TYPE_GROUP);
      var.setResetGroup(currGroup);
      var.setSystemDefined(false);
      jas.addVariable(var);
      
      JRDesignExpression je = new JRDesignExpression();
      je.addVariableChunk(var.getName());
      je.setValueClassName(var.getValueClassName());
      tf.setExpression(je);
    } else 
      tf.setExpression(createExpression(cs));

    // hack za sume na headerima
    if (isHeader && cs.startsWith("=") && cs.indexOf("dsum") > 0) {
      System.out.println("setting eval time to group "+currGroup.getName());
      tf.setEvaluationTime(JRExpression.EVALUATION_TIME_GROUP);
      tf.setEvaluationGroup(currGroup);
    }

    setFormat(tf, m);
    setBounds(tf, m);
    setFont(tf, m);
    setBox(tf, m);
    if (tf.getHeight() > tf.getFontSize() * 2 && 
        tf.getHorizontalAlignment() != JRAlignment.HORIZONTAL_ALIGN_CENTER) {
      tf.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_TOP);
    }
    if (tf.getHeight() > tf.getFontSize() * 2 &&
        ev.YES.equals(m.getPropertyValue(ep.WRAP))) {
      if (!tf.isWrapAllowed())
        tf.setStretchWithOverflow(false);
      tf.setWrapAllowed(true);
    }
    if (tf.getHeight() <= tf.getFontSize()+2)
      tf.setStretchWithOverflow(true);
    System.out.println(tf.getExpression().getText() + " = " + tf.getHeight() + ", " + tf.getFontSize());
      
    return tf;
  }

  private JRExpression createExpression(String exp) throws JRException {
    JRDesignExpression je = new JRDesignExpression();
    if (exp.startsWith("=")) {
      System.out.println("Parsing: "+exp);
      Expression ex = new Expression(exp.substring(1));
      je.setText(ex.convert());
      je.setValueClassName(ex.getType());
      System.out.println("Expression: " + je.getText());
      System.out.println("Type: " + je.getValueClassName());
    } else if (exp.equals("_page1")) {
      je.setText("\"Stranica \" + $V{PAGE_NUMBER}");
      je.setValueClassName("java.lang.String");
    } else if (exp.equals("_page2")) {
      je.setText("\" od \" + $V{PAGE_NUMBER}");
      je.setValueClassName("java.lang.String");
    } else if (exp.equals("_fpage1")) {
      je.setText("\"Page \" + $V{PAGE_NUMBER}");
      je.setValueClassName("java.lang.String");
    } else if (exp.equals("_fpage2")) {
      je.setText("\" of \" + $V{PAGE_NUMBER}");
      je.setValueClassName("java.lang.String");
    } else if (data.getTypeMap().containsKey(exp)) {
      je.addFieldChunk(exp);
      je.setValueClassName((String) data.getTypeMap().get(exp));
      data.setUsedGetter(exp);
    } else {
      je.addTextChunk("\"\"");
      je.setValueClassName("java.lang.String");
      System.out.println("Invalid field: "+exp);
    }
    return je;
  }

  private Field[] cols = Color.class.getFields();
  private Color parseSimpleColor(String col) {
    if (Aus.isNumber(col)) return new Color(Aus.getNumber(col));
    try {
      String uc = col.replace(' ', '_');
      String nc = new VarStr(col).removeWhitespace().toString();
      for (int i = 0; i < cols.length; i++)
        if (cols[i].getName().equalsIgnoreCase(uc) ||
            cols[i].getName().equalsIgnoreCase(nc)) 
          return (Color) cols[i].get(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Invalid color: " + col);
  }

  private void createLayout() throws JRException {
    jas.setColumnHeader(createEmptyBand());
    jas.setColumnFooter(createEmptyBand());
    
    IModel pageSetup = model.getModel(ep.PAGE_SETUP);
    int columns = getModelInt(pageSetup, "Number of Columns");
    jas.setColumnCount(columns > 1 ? columns : 1);

    jas.setOrientation(pageSetup.getPropertyValue(ep.ORIENTATION).equals(ev.PORTRAIT)
        ? JRReport.ORIENTATION_PORTRAIT : JRReport.ORIENTATION_LANDSCAPE);
    jas.setPageWidth(getCoord(pageSetup, 
        jas.getOrientation() == JRReport.ORIENTATION_PORTRAIT ? ep.WIDTH : ep.HEIGHT));
    jas.setPageHeight(getCoord(pageSetup, 
        jas.getOrientation() == JRReport.ORIENTATION_PORTRAIT ? ep.HEIGHT : ep.WIDTH));
    jas.setTopMargin(getCoord(pageSetup, ep.TOP));
    jas.setBottomMargin(getCoord(pageSetup, ep.BOTTOM));
    jas.setLeftMargin(getCoord(pageSetup, ep.LEFT));
    jas.setRightMargin(getCoord(pageSetup, ep.RIGHT));
    jas.setColumnWidth(jas.getPageWidth() - jas.getLeftMargin() - jas.getRightMargin());
    if (columns > 1) {
      jas.setColumnSpacing(getCoord(pageSetup, "Column Spacing"));
      jas.setColumnWidth((jas.getColumnWidth() - 
          jas.getColumnSpacing() * (columns - 1)) / columns);
    }
  }

  private void createFields() throws JRException {
    Map fields = data.getTypeMap();
    
    for (Iterator i = fields.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry me = (Map.Entry) i.next();
      JRDesignField field = new JRDesignField();
      field.setName((String) me.getKey());
      field.setDescription(field.getName());
      field.setValueClassName((String) me.getValue());
      
      jas.addField(field);
    }
  }
  
  private JRBand createEmptyBand() {
    JRDesignBand empty = new JRDesignBand();
    empty.setHeight(0);
    return empty;
  }

  private int getModelInt(IModel m, String prop) {
    return Integer.parseInt(m.getPropertyValue(prop));
  }
  
  private int getCoord(IModel m, String prop) {
    return (int) Math.round(Double.parseDouble(m.getPropertyValue(prop)) / 20);
  }
  
  private IModel[] getSortedModels(IModel model) {
    IModel[] ms = new IModel[model.getModelCount()];
    for (int i = 0; i < ms.length; i++)
      ms[i] = model.getModel(i);
    
    if (model.getHiddenPropertyValue(ep.CHILD_COUNTER) != null)
      Arrays.sort(ms, new Comparator() {
        public int compare(Object o1, Object o2) {
          String n1 = ((IModel) o1).getHiddenPropertyValue(ep.CHILD_NUMBER); 
          String n2 = ((IModel) o2).getHiddenPropertyValue(ep.CHILD_NUMBER);
          if (n1 == null && n2 == null) return 0;
          if (n1 == null) return -1;
          if (n2 == null) return 1;
          return Aus.getNumber(n1) - Aus.getNumber(n2);
        }
      });
    return ms;
  }
  
  class Expression {
    String orig;
    String command;
    String type;
    String conv;
    List args;
    public Expression(String expr) throws JRException {
      orig = expr;
      args = new ArrayList();
      conv = null;
      parse();
    }
    public boolean isBoolean() throws JRException {
      conv = convert();
      return type.equals("boolean");
    }
    public boolean isString() throws JRException {
      conv = convert();
      return type.equals("java.lang.String");
    }
    public boolean isDec() throws JRException {
      conv = convert();
      return type.equals("java.lang.Double");
    }
    public boolean isInt() throws JRException {
      conv = convert();
      return type.equals("java.lang.Integer");
    }
    public void addArg(Expression exp) {
      if (args == null) args = new ArrayList();
      args.add(exp);
    }
    public Expression getArg(int i) {
      return (Expression) args.get(i);
    }
    private void parse() throws JRException {
      String expr = orig.trim();
      while (expr.startsWith("(")) {
        if (!expr.endsWith(")"))
          throw new JRException("Unmatched (");
        expr = expr.substring(1, expr.length() - 1).trim();
      }
      if (expr.startsWith("\"")) {
        if (!expr.endsWith("\""))
          throw new JRException("Unmatched \"");
        command = new VarStr(expr).replace("\n", "\\n").toString();
        return;
      }
      if (expr.startsWith("[")) {
        if (!expr.endsWith("]"))
          throw new JRException("Unmatched ]");
        command = expr;
        return;
      }
      int i = 0;
      while (i < expr.length() && expr.charAt(i) > ' ' && 
          expr.charAt(i) != '(' && expr.charAt(i) != '\"' && expr.charAt(i) != '[') ++i;
      command = expr.substring(0, i);
      while (i < expr.length() && expr.charAt(i) <= ' ') ++i;
      while (i < expr.length()) {
        int beg = i;
        char ch = expr.charAt(i++);
        if (ch == '\"') {
          while (i < expr.length() && (expr.charAt(i) != '\"' || expr.charAt(i - 1) == '\\')) ++i;
        } else if (ch == '[') {
          while (i < expr.length() && expr.charAt(i) != ']') ++i;
        } else if (ch == '(') {
          int depth = 1;
          while (i < expr.length()) {
            if (expr.charAt(i) == '(') ++depth;
            if (expr.charAt(i) == ')' && --depth == 0) break;
            ++i;
          }
        } else {
          while (i < expr.length() && expr.charAt(i) > ' ') ++i;
        }
        if (i < expr.length()) ++i;
        addArg(new Expression(expr.substring(beg, i)));
        while (i < expr.length() && expr.charAt(i) <= ' ') ++i;
      }
    }
        
    public String getType() {
      return type;
    }
    
    private void enforceArgs(int num) throws JRException {
      if (args.size() != num)
        throw new JRException("Invalid number of arguments " +
                "(" + num + "expected)");
    }
    
    private void enforceArgs(int min, int max) throws JRException {
      if (args.size() < min || args.size() > max)
        throw new JRException("Invalid number of arguments " +
                "(" + min + " to " + max + " expected)");
    }
    
    public String convert() throws JRException {
      if (conv != null) return conv;
      if (command.equals("dsum") || command.equals("total")) { 
        enforceArgs(1);
        String field = new VarStr(getArg(0).command).split("\"")[1];
        if (!data.getTypeMap().containsKey(field)) {
          type = "java.lang.Double";
          return "new Double(0)";
        }

        String vname = "sum_" + currGroup.getName() + "_" + field;
        if (command.equals("total"))
          vname = "total_" + field;
        JRDesignVariable var = null;
        if (jas.getVariablesMap().containsKey(vname))
          var = (JRDesignVariable) jas.getVariablesMap().get(vname);
        if (var == null) {
          var = new JRDesignVariable();
          var.setName(vname);
          var.setValueClassName((String) data.getTypeMap().get(field));
          var.setExpression(createExpression(field));
          var.setCalculation(JRVariable.CALCULATION_SUM);
          var.setResetType(JRVariable.RESET_TYPE_GROUP);
          if (command.equals("total"))
            var.setResetType(JRVariable.RESET_TYPE_REPORT);
          var.setResetGroup(currGroup);
          var.setSystemDefined(false);
          jas.addVariable(var); 
        }
        type = var.getValueClassName();
        return "$V{" + vname + "}";
      } else if (command.startsWith("\"")) {
        type = "java.lang.String";
        return command;
      } else if (command.equals("string-append")) {
        enforceArgs(1, 20);
        type = "java.lang.String";
        VarStr buf = new VarStr();
        for (int i = 0; i < args.size(); i++)
          buf.append(getArg(i).convert()).append('+');
        return buf.chop().toString();
      } else if (command.equals("+") || command.equals("-") || 
          command.equals(">") || command.equals("<") || command.equals("=")) {
        enforceArgs(2);
        Expression ex1 = getArg(0);
        Expression ex2 = getArg(1);
        if (ex1.isString() || ex2.isString())
          throw new JRException("Invalid arguments (string for arithmetic)");
        if (command.equals(">") || command.equals("<") || command.equals("=")) {
          type = "boolean";
          if (command.equals("=")) command = "==";
          return "(" + ex1.convert() + ".doubleValue()" + command + 
            ex2.convert() + ".doubleValue())";
        }
        if (ex1.isInt() && ex2.isInt()) {
          type = "java.lang.Integer";
          return "new Integer(" + ex1.convert() + ".intValue()" + command + 
            ex2.convert() + ".intValue())";
        } 
        type = "java.lang.Double";
        return "new Double(" + ex1.convert() + ".doubleValue()" + command + 
                  ex2.convert() + ".doubleValue())";
      } else if (command.startsWith("[")) {
        String field = command.substring(1, command.length() - 1);
        if (!data.getTypeMap().containsKey(field)) {
          type = "java.lang.Double";
          return "new Double(0)";
        }
        data.setUsedGetter(field);
        type = (String) data.getTypeMap().get(field);
        return "$F{" + field + "}";
      } else if (command.equals("or") || command.equals("and")) {
        enforceArgs(2);
        Expression ex1 = getArg(0);
        Expression ex2 = getArg(1);
        if (!ex1.isBoolean() || !ex2.isBoolean())
          throw new JRException("Boolean expression expected");
        type = "boolean";
        command = command.equals("or") ? " || " : " && ";
        return "(" + ex1.convert() + command + ex2.convert() + ")";
      } else if (command.equals("if")) {
        enforceArgs(2, 3);
        if (!getArg(0).isBoolean())
          throw new JRException("Boolean expression expected");
        String cond = "(" + getArg(0).convert() + " ? ";
        if (args.size() == 2) {
          if (getArg(1).isString()) {
            type = "java.lang.String";
            return cond + getArg(1).convert() + ".toString() : \"\")";
          }
          if (getArg(1).isInt()) {
            type = "java.lang.Integer";
            return cond + getArg(1).convert() +" : Integer.valueOf(0))";
          }
          if (getArg(1).isDec()) {
            type = "java.lang.Double";
            return cond + getArg(1).convert() + " : Double.valueOf(0))";
          }
        }
         if (getArg(1).isString() || getArg(2).isString()) {
           type = "java.lang.String";
           return cond + getArg(1).convert() + ".toString() : "+
                 getArg(2).convert() + ".toString())";
         }
         if (getArg(1).isInt() || getArg(2).isInt()) {
           type = "java.lang.Integer";
           return cond + getArg(1).convert() + " : "+ getArg(2).convert() + ")";
         }
         type = "java.lang.Double";
         return cond + getArg(1).convert() + " : "+ getArg(2).convert() + ")";
      } else if (Aus.isDecNumber(command)) {
        if (Aus.isDigit(command)) {
          type = "java.lang.Integer";
          return "new Integer(" + command + ")";
        }
        type = "java.lang.Double";
        return "new Double(" + command + ")";
      } else if (command.equals("create-var-cache") || command.equals("destroy-var-cache")) {
        type = "java.lang.String";
        return "\"\"";
      } else if (command.startsWith("page")) {
        type = "java.lang.String";
        return "\"\"";
      } else if (command.equals("get-var") || command.equals("row-no")) {
        type = "java.lang.Integer";
        if (currGroup == null) return "$V{REPORT_COUNT}";
        return "$V{"+currGroup.getName()+"_COUNT}";
      } else if (command.equals("null")) {
        type = "null";
        conv = "null";
        return "null";
      } else 
        throw new JRException("Invalid expression command: " + command);
    }
  }
}
