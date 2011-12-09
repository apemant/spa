/****license*****************************************************************
**   file: raElixirDatasource.java
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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import sg.com.elixir.reportwriter.datasource.DataSchemaModel;
import sg.com.elixir.reportwriter.datasource.DataSourceManager;
import sg.com.elixir.reportwriter.datasource.IDataProvider;
import sg.com.elixir.reportwriter.datasource.IDataSourceInfor;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raElixirDatasource {
  public static final String OBJECT_DATA = "Object Data Source";
  public static final String DATASCHEMA = "Dataschema";

  private static final String[] validReturns = {
    "int", "short", "long", "double", "float",
    "java.lang.String", "java.sql.Date",
    "java.sql.Timestamp", "java.math.BigDecimal", "java.lang.Boolean"
  };
  public static final HashSet valids = new HashSet(Arrays.asList(validReturns));

  private raElixirDatasource() {
  }
  
  public static void benchProvider(Object o) {
    HashSet objMethods = new HashSet(Arrays.asList(Object.class.getMethods()));
    objMethods.addAll(Arrays.asList(raReportData.class.getMethods()));
    Method[] meth = o.getClass().getMethods();
    for (int i = 0; i < meth.length; i++)
      if (valids.contains(meth[i].getReturnType().getName()) && !objMethods.contains(meth[i])) {
        try {
          long now = System.currentTimeMillis();
          meth[i].invoke(o, null);
          now = System.currentTimeMillis() - now;
          System.out.println(meth[i].getName() + " - " + now);          
        } catch (Exception e) {
          e.printStackTrace();
        }
      }    
  }
  
//rd.getProviderName(), rd.getProvider());
  public static void build(raReportDescriptor rd) throws Exception {
    String name = rd.getProviderName();
    DataSourceManager dsm = DataSourceManager.current();
    if (rd.isExtended()) raElixirDataProvider.getInstance().setDataClass(rd.getDataSource());
    dsm.addDataProvider(name, rd.isExtended() ? raElixirDataProvider.getInstance() :
                        (IDataProvider) rd.getProvider());
    if (dsm.userDSNNameExist(name)) return;
    IDataSourceInfor dsi = dsm.getSystemDSN(OBJECT_DATA).cloneDataSource();
    Class c = !rd.isExtended() ? rd.getProvider().getClass() :
              raElixirDataProvider.getInstance().getReportData().getClass();
    dsi.setName(name);
    DataSchemaModel m = new DataSchemaModel(dsi.getModel().getModel(DATASCHEMA));
    m.setObjectClassName(c.getName());
//    System.out.println("Adding datasource "+name+" for class "+c.getName());
    HashSet objMethods = new HashSet(Arrays.asList(Object.class.getMethods()));
    objMethods.addAll(Arrays.asList(raReportData.class.getMethods()));
    Method[] meth = c.getMethods();
    String[] empty = new String[0];
    for (int i = 0; i < meth.length; i++)
      if (valids.contains(meth[i].getReturnType().getName()) && !objMethods.contains(meth[i])) {
        String n = meth[i].getName();
        String f = n.startsWith("get") ? n.substring(3) : n;
        m.addDetail(f, n, empty, empty, meth[i].getReturnType().getName());
//        System.out.println("Adding field "+f+", method "+meth[i].getReturnType().getName()+" "+n+"()");
      }
//      hr.restart.util.Aus.dumpModel(dsi.getModel(), 0);
    dsm.addUserDSN(dsi);
  }

  public static void buildDynamic() throws Exception {
    String STR = "java.lang.String";
    DataSourceManager dsm = DataSourceManager.current();
    IDataSourceInfor dsi = dsm.getSystemDSN(OBJECT_DATA).cloneDataSource();
    dsi.setName(raReportDescriptor.DYNAMIC_NAME);
    DataSchemaModel m = new DataSchemaModel(dsi.getModel().getModel(DATASCHEMA));
    m.setObjectClassName(raReportDescriptor.DYNAMIC_CLASS_DS);
    String[] gets = new String[] {"Group", "GroupCaption", "GroupValue",
      "HeaderValue", "DataValue", "SumValue", "SummaryHead", "SummaryData"};
    int[] params = new int[] {10, 10, 10, 16, 16, 16, 8, 8};

    String[] empty = new String[0];
    m.addDetail("Title", "getTitle", empty, empty, STR);
    m.addDetail("SubTitle", "getSubTitle", empty, empty, STR);
    m.addDetail("Logo", "getLogo", empty, empty, "int");
    m.addDetail("FirstLine", "getFirstLine", empty, empty, STR);
    m.addDetail("SecondLine", "getSecondLine", empty, empty, STR);
    m.addDetail("ThirdLine", "getThirdLine", empty, empty, STR);
    m.addDetail("DatumIsp", "getDatumIsp", empty, empty, STR);
    for (int g = 0; g < gets.length; g++)
      for (int i = 0; i < params[g]; i++)
        m.addDetail(gets[g] + i, "get" + gets[g], new String[] {"int"},
                    new String[] {String.valueOf(i)}, STR);
    for (int i = 0; i < 16; i++)
      m.addDetail("DataNum" + i, "getDataNum", new String[] {"int"},
                  new String[] {String.valueOf(i)}, "double");
    dsm.addUserDSN(dsi);
    dsm.addDataProvider(raReportDescriptor.DYNAMIC_NAME, repDynamicProvider.getInstance());
  }
}
