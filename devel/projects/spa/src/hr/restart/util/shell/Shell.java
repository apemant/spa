/****license*****************************************************************
**   file: Shell.java
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
package hr.restart.util.shell;

import java.util.Hashtable;
import java.util.Iterator;

public class Shell extends Thread {

  boolean inShell = true;
  private String prompt;
  private Hashtable responses = new Hashtable();
  private String welcomeMsg = null;
  private ShellEvent shellHelp = new ShellEvent() {
    public boolean eventAction(String inp) {
      Iterator iter = responses.keySet().iterator();
      while (iter.hasNext()) {
        Object key = iter.next();
        Object memb = responses.get(key);
        if (memb instanceof ShellEvent&&!key.equals("")) {
          System.out.println(key);
          System.out.println("  - "+((ShellEvent)memb).getInfo());
        }
      }
      return true;
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "help text";
    }
  };

  private ShellEvent shellExit = new ShellEvent() {
    public boolean eventAction(String inp) {
      if (!exitValidation()) return false;
      inShell = false;
      return true;
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "exits";
    }
  };

  private ShellEvent shellPrompt = new ShellEvent() {
    public boolean eventAction(String inp) {
      setPrompt(inp);
      return true;
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "changes prompt";
    }
  };

  private ShellEvent shellClear = new ShellEvent() {
    public boolean eventAction(String inp) {
      for (int i=0;i<80;i++) System.out.println();
      return true;
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "clear screen";
    }
  };

  private ShellEvent shellMem = new ShellEvent() {
    public boolean eventAction(String inp) {
      System.out.println(printMemUsage());
      return true;
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "prints memory usage";
    }
  };

  private ShellEvent shellGc = new ShellEvent() {
    public boolean eventAction(String inp) {
      System.out.println("Garbage collect engaged ...");
      System.gc();
      System.out.println(printMemUsage());
      return true;
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "Garbage collect";
    }
  };

  public Shell(String nameC) {
    prompt = nameC;
    initInputDefinition();
  }

  private void response(byte[] inp) {
    String input = new String(inp).trim();
    ShellEventRunner shellEv = getDefinedInput(input);
    if (shellEv != null) {
      if (shellEv.inThread())
        shellEv.start();
      else
        shellEv.run();
    }
    else System.out.println("You typed '"+input+"' and that means nothing to me!");
  }

  public void setPrompt(String promptC) {
    prompt = promptC;
  }

  public ShellEventRunner getDefinedInput(String inp) {
    String realKey = null;
    Iterator iter = responses.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next().toString();
      if (inp.startsWith(key)) {
         realKey = key;
         break;
      }
    }
    if (realKey == null) return null;
    Object memb = responses.get(realKey);
    if (!(memb instanceof ShellEvent)) return null;
    ShellEvent shEv = (ShellEvent)memb;
    String shParam = inp.substring(realKey.length(),inp.length()).trim();
    return new ShellEventRunner(shEv,shParam);
  }

  public void defineInput(String inp,ShellEvent event) {
    responses.put(inp,event);
  }

  public void initInputDefinition() {
    responses.clear();
    defineInput("?",shellHelp);
    defineInput("exit",shellExit);
    defineInput("prompt ",shellPrompt);
    defineInput("cls",shellClear);
    defineInput("mem",shellMem);
    defineInput("gc",shellGc);
  }

  public void clearInputDefinition() {
    responses.clear();
  }

  public String getWelcomeMessage() {
    if (welcomeMsg == null) welcomeMsg = "Welcome! Type '?' for help";
    return welcomeMsg;
  }

  public void setWelcomeMessage(String welm) {
    welcomeMsg = welm;
  }

  public boolean exitValidation() {
    return true;
  }

  public void run() {
    byte[] shInput = new byte[255];
    clearBytes(shInput,255);
    shellClear.eventAction(null);
    System.out.println(getWelcomeMessage());
    while (inShell) {
      try {
        System.out.print(prompt+" ");
        int len = System.in.read(shInput);
        if ((new String(shInput).trim().length() > 0) || responses.get("")!=null)
          response(shInput);
        clearBytes(shInput,len);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  public void stopShell() {
    inShell = false;
  }
  void clearBytes(byte[] bs,int len) {
    for (int i=0;i<len;i++) bs[i] = 0x20;
  }

  public static String printMemUsage() {
    Runtime curRt = Runtime.getRuntime();
    String ret = "";
    long ltotal = curRt.totalMemory();
    long lfree = curRt.freeMemory();
    long lused = ltotal - lfree;
    ret = "Memory usage:\n";
    ret = ret+"Total: "+dubToString(ltotal)+"\n";
    ret = ret+"Free:  "+dubToString(lfree)+"\n";
    ret = ret+"Used:  "+dubToString(lused)+"\n";
    return ret;
  }

  static String dubToString(long lng) {
    java.math.BigDecimal bd = java.math.BigDecimal.valueOf(lng,1);
    java.math.BigDecimal bdd = java.math.BigDecimal.valueOf(1048576,1);
    bd = bd.divide(bdd,bd.ROUND_UP);
    return bd.toString()+" MB  ("+lng+" bytes)";
  }

  public static void main(String[] args) {
    new Shell("shell>").start();
  }
}
