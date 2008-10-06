/****license*****************************************************************
**   file: StackFrame.java
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class StackFrame {
  private static final String CLASS_NAME = "StackFrame";
  private static final String METHOD_NAME = "getStackFrame";

  private StackFrame parent;
  private String packageName, className, methodName;

  public static StackFrame getStackFrame() {
    StringWriter sw = new StringWriter(1024);
    new Throwable().printStackTrace(new PrintWriter(sw));
    return parseStackTrace(sw.getBuffer());
  }

  public String getPackageName() {
    return packageName;
  }

  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }
  
  public StackFrame getParent() {
    return parent;
  }

  public StackFrame findPackage(String packageName) {
    StackFrame sf = this;
    while (sf != null && !sf.packageName.equals(packageName))
      sf = sf.parent;
    return sf;
  }

  public StackFrame findClass(String className) {
    StackFrame sf = this;
    while (sf != null && !sf.className.equals(className))
      sf = sf.parent;
    return sf;
  }

  private StackFrame(String pack, String cls, String meth) {
    packageName = pack;
    className = cls;
    methodName = meth;
    parent = null;
  }

  private static StackFrame parseStackTrace(StringBuffer sb) {
    StackFrame top = null, last = null, curr;
    StringTokenizer st = new StringTokenizer(sb.toString(), "\n");
    int brace, dot1, dot2;
    while (st.hasMoreTokens()) {
      String token = st.nextToken().trim();
      if (token.startsWith("at ")) {
        brace = token.indexOf('(');
        dot2 = token.lastIndexOf('.', brace);
        dot1 = token.lastIndexOf('.', dot2 - 1);
        curr = new StackFrame(token.substring(3, dot1),
                              token.substring(dot1 + 1, dot2),
                              token.substring(dot2 + 1, brace));
        if (last != null) last = last.parent = curr;
        else if (!curr.className.equals(CLASS_NAME) &&
                 !curr.methodName.equals(METHOD_NAME))
          last = top = curr;
      }
    }
    return top;
  }

  public String toString() {
    return packageName + "." + className + "." + methodName + "()";
  }
  
  public void dump() {
    StackFrame sf = this;
    while (sf != null) {
      System.out.println(sf);
      sf = sf.parent;
    }
  }
}

