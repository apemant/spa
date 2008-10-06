/****license*****************************************************************
**   file: ScriptCommand.java
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

import java.util.ArrayList;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class ScriptCommand {
  private int lineNumber;
  private ScriptCommand parent;
  private String command, argument, script;
  private ArrayList children;

  public ScriptCommand(String line, String script, int lineNumber) {
    this.script = script;
    this.lineNumber = lineNumber;
    parent = null;
    String t = line.trim();
    int spc = t.indexOf(' ');
    if (spc <= 0)
      throw new ScriptException("Missing command", this);
    command = t.substring(0, spc);
    argument = t.substring(spc + 1);
    if (argument.startsWith("'") && argument.endsWith("'"))
      argument = argument.substring(1, argument.length() - 1);
  }

  public String getScriptPath() {
    return script.substring(0, script.lastIndexOf(java.io.File.separatorChar) + 1);
  }

  public String getScript() {
    return script;
  }

  public String getScriptName() {
    return script.substring(script.lastIndexOf(java.io.File.separatorChar) + 1);
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getCommand() {
    return command;
  }

  public String getArgument() {
    return argument;
  }

  public void addCommand(ScriptCommand com) {
    if (children == null) children = new ArrayList();
    children.add(com);
    com.parent = this;
  }

  public ScriptCommand getParent() {
    return parent;
  }

  public ScriptCommand getChild(int i) {
    return (ScriptCommand) children.get(i);
  }

  public ScriptCommand getChild(String comm) {
    return getChild(comm, false);
  }

  public ScriptCommand getChild(String comm, boolean recursive) {
    for (int i = 0; i < getChildrenCount(); i++) {
      if (getChild(i).command.equalsIgnoreCase(comm)) return getChild(i);
      if (recursive) {
        ScriptCommand grand = getChild(i).getChild(comm, true);
        if (grand != null) return grand;
      }
    }
    return null;
  }

  public boolean hasChild(String comm) {
    return getChild(comm, false) != null;
  }

  public String getArgumentFor(String comm) {
    return getArgumentFor(comm, false);
  }

  public String getArgumentFor(String comm, boolean recursive) {
    ScriptCommand ch = getChild(comm, recursive);
    return ch == null ? null : ch.getArgument();
  }

  public String getOuterArgumentFor(String comm) {
    String direct = getArgumentFor(comm);
    if (direct != null) return direct;
    if (parent == null) return null;
    else return parent.getOuterArgumentFor(comm);
  }

  public int getChildrenCount() {
    return children == null ? 0 : children.size();
  }
}
