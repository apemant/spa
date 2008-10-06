/****license*****************************************************************
**   file: dsPrinter.java
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

public class dsPrinter {

  protected dsPrinter() {
  }
  public static void print(com.borland.dx.dataset.DataSet ds) {
    ds.open();
  System.out.println("dsPrinter: ");
  System.out.println("ds = "+ds);
  System.out.println("ds.getColumnCount() = "+ds.getColumnCount());
  System.out.println("ds.getRowCount() = "+ds.getRowCount());
    com.borland.dx.dataset.Variant v1 = new com.borland.dx.dataset.Variant();
    String[] columns = ds.getColumnNames(ds.getColumnCount());
    ds.first();
    for (int i=0;i<columns.length;i++) {
      System.out.print(columns[i]);
    }
    System.out.println();
    do {
      for (int i=0;i<columns.length;i++) {
        ds.getVariant(columns[i],v1);
        System.out.print(v1);
      }
      System.out.println();
    } while (ds.next());
  }
}