package hr.restart.util.reports;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import sg.com.elixir.reportwriter.datasource.IDataProvider;
import sg.com.elixir.reportwriter.datasource.IDataSourceInfor;
import sg.com.elixir.reportwriter.xml.IModel;


public class JasperElixirData implements JRDataSource {  
  private raElixirProperties ep = raElixirPropertiesInstance.get();
  
  private IDataProvider provider;
  private Enumeration data;
  private raReportData rdata;
  private JRDataSource jdata;
  private Set usedGetters;
  private Map types;
  private Map rowValues;
  private Map getters;
  private Map params;
  private HashMap[] sortedTable;
  
  String[] sortCols;
  int tableRow = 0;

  public JasperElixirData(String dsource) {
    try {
      data = null;
      Object cl = Class.forName(dsource).newInstance();
      if (cl instanceof raReportData) {
        rdata = (raReportData) cl;
        jdata = null;
        buildTypeMap();
      } else
        jdata = (JRDataSource) cl;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
  
  public JasperElixirData(IDataSourceInfor ds, IDataProvider prov) {
    provider = prov;
    data = provider.getData();
    rdata = null;
    jdata = null;
    buildTypeMap(ds.getModel().getModel("Dataschema"));
  }
  
  public Map getTypeMap() {
    return types;
  }
  
  public boolean isJasperSource() {
    return jdata != null;
  }
  
  public JRDataSource getSource() {
    return isJasperSource() ? jdata : this;
  }
  
  private void buildTypeMap() {
    types = new HashMap();
    getters = new HashMap();
    params = new HashMap();
    
    Method[] meths = rdata.getClass().getMethods();
    for (int i = 0; i < meths.length; i++)
      if ((meths[i].getModifiers() & Modifier.STATIC) == 0 &&
          (meths[i].getModifiers() & Modifier.PUBLIC) != 0 &&
          valids.containsKey(meths[i].getReturnType().getName())) {
        String name = meths[i].getName();
        if (name.startsWith("get")) name = name.substring(3);
        getters.put(name, meths[i]);
        types.put(name, valids.get(meths[i].getReturnType().toString()));
      }
  }

  private void buildTypeMap(IModel dsm) {
    types = new HashMap();
    getters = new HashMap();
    usedGetters = new HashSet();
    params = new HashMap();
    
    Map methods = new HashMap();
    try {
      Class dc = Class.forName(dsm.getPropertyValue("Object Class"));
      Method[] meths = dc.getMethods();
      for (int i = 0; i < meths.length; i++)
        methods.put(meths[i].getName(), meths[i]);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < dsm.getModelCount(); i++) {
      IModel f = dsm.getModel(i);
      String name = f.getPropertyValue("Field Name");
      String type = f.getPropertyValue("Return Type");
      String meth = f.getPropertyValue("Access Method");
      types.put(name, valids.get(type));
      getters.put(name, methods.get(meth));
      if (f.getModelCount() == 1) {
        IModel param = f.getModel("Parameter0");
        if (param.getPropertyValue("Parameter Type").equals("int"))
          params.put(name, new Object[] {Integer.valueOf(param.getPropertyValue("Parameter Value"))});
      }
    }
  }
  
  public void removeUnusedGetters() {
    getters.keySet().retainAll(usedGetters);
  }
  
  public void setUsedGetter(String name) {
    usedGetters.add(name);
  }
  
  public boolean isGetter(String name) {
    return getters.containsKey(name);
  }

  public void buildTable() {
    List table = new ArrayList();
    
    for (int i = 0; i < rdata.getRowCount(); i++) {
      buildValueMap(rdata.getRow(i));
      table.add(new HashMap(rowValues));
    }
    
    sortedTable = (HashMap[]) table.toArray(new HashMap[table.size()]);
  }
  
  public void buildTable(IModel template) {
    List sortFields = new ArrayList();
    IModel sects = template.getModel(ep.SECTIONS);
    for (int i = 0; i < sects.getModelCount(); i++) {
      IModel sect = sects.getModel(ep.SECTION + i);
      String fname = sect.getPropertyValue(ep.FIELD);
      if (fname.trim().length() == 0 || !getters.containsKey(fname)) break;
      sortFields.add(fname.trim());
    }
    if (sortFields.size() == 0) return;
    
    sortCols = (String[]) sortFields.toArray(new String[sortFields.size()]);
    List table = new ArrayList();
    while (data.hasMoreElements()) {
      buildValueMap(data.nextElement());
      table.add(new HashMap(rowValues));
    }
    sortedTable = (HashMap[]) table.toArray(new HashMap[table.size()]);
  }
  
  public void sortTable() {
    if (sortedTable == null) return;
    Arrays.sort(sortedTable, new Comparator() {
      public int compare(Object o1, Object o2) {
        HashMap h1 = (HashMap) o1;
        HashMap h2 = (HashMap) o2;
        for (int i = 0; i < sortCols.length; i++) {
          int c = ((Comparable) h1.get(sortCols[i])).compareTo(h2.get(sortCols[i]));
          if (c != 0) return c;
        }
        return 0;
      }
    });
  }
  
  private void buildValueMap(Object obj) {
    if (rowValues == null) rowValues = new HashMap();
    rowValues.clear();
    for (Iterator i = getters.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry me = (Map.Entry) i.next();
      try {
        if (me.getValue() == null) rowValues.put(me.getKey(), null);
        else rowValues.put(me.getKey(), ((Method) me.getValue()).
            invoke(obj, (Object[]) params.get(me.getKey())));
      } catch (Exception e) {
        e.printStackTrace();
        rowValues.put(me.getKey(), null);
        me.setValue(null);
      }
    }
  }
  
  public boolean next() throws JRException {
    if (sortedTable != null) {
      if (tableRow >= sortedTable.length) {
        if (rdata != null) rdata.close();
        else provider.close();
        return false;
      }
      rowValues.clear();
      rowValues.putAll(sortedTable[tableRow++]);
      return true;
    }
    
    if (!data.hasMoreElements()) {
      provider.close();
      return false;
    }
    buildValueMap(data.nextElement());
    return true;
  }

  public Object getFieldValue(JRField f) throws JRException {
    return rowValues.get(f.getName());
  }
  
  private static final String[] validReturns = {
    "int", "short", "long", "double", "float",
    "java.lang.String", "java.sql.Date",
    "java.sql.Timestamp", "java.math.BigDecimal", "java.lang.Boolean"
  };
  
  private static final String[][] validTypes = {
    {"int", "java.lang.Integer"},
    {"short", "java.lang.Short"},
    {"long", "java.lang.Long"},
    {"double", "java.lang.Double"},
    {"float", "java.lang.Float"},
    {"java.lang.String", "java.lang.String"},
    {"java.sql.Date", "java.sql.Date"},
    {"java.sql.Timestamp", "java.sql.Timestamp"},
    {"java.math.BigDecimal", "java.math.BigDecimal"},
    {"java.lang.Boolean", "java.lang.Boolean"}
  };
  
  private static final Map valids = new HashMap();
  static {
    for (int i = 0; i < validTypes.length; i++)
      valids.put(validTypes[i][0], validTypes[i][1]);
  }
}
