/****license*****************************************************************
**   file: commObject.java
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
package hr.restart.util.server;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class commObject implements java.io.Serializable {

  Object invoker;
  String methodToInvoke;
  Class[] parameterTypes;
  Object[] parameters;

  public commObject() {
  }

  public Object invoke() throws Exception {
    return invoker.getClass().getMethod(methodToInvoke,parameterTypes).invoke(invoker,parameters);
  }

  public void setInvoker(Object invokr) {
    invoker = invokr;
  }
  public Object getInvoker() {
    return invoker;
  }

  public void setMethodToInvoke(String meth) {
    methodToInvoke = meth;
  }

  public String getMethodToInvoke() {
    return methodToInvoke;
  }

  public void setParameters(Object[] params) {
    parameters = params;
    if (parameters == null) parameterTypes = null;
    else {
      parameterTypes = new Class[parameters.length];
      for (int i=0;i<parameters.length;i++)
        parameterTypes[i] = parameters[i].getClass();
    }
  }
  public Object[] getParameters() {
    return parameters;
  }
  public Class[] getParameterTypes() {
    return parameterTypes;
  }
  public String toString() {
    String tostr;
    tostr = "\n"+this.getClass().toString()+":\n";
    tostr = tostr + "invoker = "+invoker+"\n";
    tostr = tostr + "method = "+methodToInvoke+"(";
    if (parameters == null) tostr = tostr + "null)\n";
    else {
      for (int i=0;i<parameters.length;i++)
        tostr = tostr + parameters[i]+",";
      tostr = tostr + "\n";
    }
    return tostr;
  }
/* to moze i neki server ili client odradit
  public Object invokeRemote(Socket client) {
    try {
      receive(client);
      return invoke();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
  public static void send(Socket client,commObject cobj) {
    try {
      ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
      output.writeObject(cobj);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static commObject receive(Socket client) {
    try {
      ObjectInputStream input = new ObjectInputStream(client.getInputStream());
      return (commObject)input.readObject();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
*/
}
