/****license*****************************************************************
**   file: AutomationScript.java
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
package hr.restart.sisfun;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class AutomationScript {
  ScriptCommand root, current;
  String fileName;
  int lineNumber;

  public AutomationScript(String file) {
    fileName = file;
    root = current = new ScriptCommand("root command", file, 0);
    parse(file);
  }

  private void parse(String file) {
    TextFile tf = null;
    String line;
    lineNumber = 0;
    try {
      tf = TextFile.read(file);
      while ((line = tf.in()) != null && ++lineNumber >= 0)
        parseLine(line);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      tf.close();
    }
  }

  private void parseLine(String line) {
    if (line.trim().startsWith("#") || line.trim().length() == 0) return;
    if (line.trim().equalsIgnoreCase("end")) {
      if (current == root)
        throw new ScriptException("End without begin", current);
      current = current.getParent();
      return;
    }
    ScriptCommand com;
    current.addCommand(com = new ScriptCommand(line, fileName, lineNumber));
    if (com.getCommand().equalsIgnoreCase("begin"))
      current = com;
  }

  private void performCommands() {
    for (int i = 0; i < root.getChildrenCount(); i++) {
      ScriptCommand one = root.getChild(i);
      if (one.getArgument().equalsIgnoreCase("matpodaci")) {
        new ScriptedMatPodaci(one).execute();
      }
      if (one.getArgument().equalsIgnoreCase("masterdetail")) {
        new ScriptedMasterDetail(one).execute();
      }
    }
  }

  public void execute() {
    Borg.install();
    new Thread() {
      public void run() {
        try {
          GlobalEventListener.setBorgThread(Thread.currentThread());
          performCommands();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          GlobalEventListener.setBorgThread(null);
        }
      }
    }.start();
  }
}
