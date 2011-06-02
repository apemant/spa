/****license*****************************************************************
**   file: optionsDialog.java
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

import hr.restart.util.mail.ui.MailOptions;
import hr.restart.util.menus.MenuUpdaterUI;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import bsh.ClassPathException;
import bsh.Interpreter;
import bsh.classpath.ClassManagerImpl;
import bsh.util.JConsole;
import bsh.util.NameCompletionTable;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class optionsDialog extends raTabDialog {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.util.aiRes");
  protected JFrame fbsh;
  raPropsViewer pvw;
  
  public optionsDialog() {
    this("");
  }
  public optionsDialog(String title) {
    super(title);
//    this.addFrameTab(new ModeDialog(frame,res.getString("jmiStartFrSysMode"),modal));
    this.addAction(new ActionTask("Parametri aplikacije", raImages.getModuleIcom("vir"),null) {
      public void doAction() {
        startFrame sf = raLLFrames.getRaLLFrames().getMsgStartFrame();
        hr.restart.sisfun.frmParam fappar = 
        (hr.restart.sisfun.frmParam)sf.showFrame("hr.restart.sisfun.frmParam",0,res.getString("jmiStartFrSysApp"),false);
        fappar.psFrmParam.showPreselect(fappar,"Aplikacija");
      }
    });
    this.addAction(new ActionTask("Import izvještaja", raImages.getModuleIcom("sk"),null) {
      public void doAction() {
        new ReportImporter().importReport();
      }
    });    
//    this.addAction(new ActionTask("Sigurnosna kopija", raImages.getModuleIcom("gk"),null) {
//      public void doAction() {
//        hr.restart.start.makeMiniBackup(true);
//      }
//    });
    this.addAction(new ActionTask("Bean Shell", raImages.getModuleIcom("vir"),null) {
      public void doAction() {
        if (fbsh == null) {
	        fbsh = new JFrame() {
	          public void pack() {
	            super.pack();
	            this.setSize(640,480);
	          }
	        };
	        JConsole con = new JConsole();
	    		fbsh.getContentPane().setLayout(new BorderLayout());
	    		fbsh.getContentPane().add(con,BorderLayout.CENTER);
	    		Interpreter interpreter = new Interpreter( con );
	    		NameCompletionTable nct = new NameCompletionTable();
	    		try {
            nct.add(((ClassManagerImpl)ClassManagerImpl.createClassManager(interpreter)).getClassPath());
            con.setNameCompletion(nct);
          } catch (ClassPathException e) {
            e.printStackTrace();
          }
	    		new Thread(interpreter).start();
	    		raLLFrames.getRaLLFrames().getMsgStartFrame().centerFrame(fbsh,0,"Bean Shell");
	    		raLLFrames.getRaLLFrames().getMsgStartFrame().showFrame(fbsh);
        } else {
          fbsh.show();
        }
    		
/*        try {
          if (ip !=null) {
            //ovo sjebe situaciju, a ni Niemeyer ne zna sto bi (vidi src/bsh/commands/desktop.bsh l:23)
            ip.eval("unset(bsh.system.desktop)");
          }
          ip = new Interpreter();
          ip.eval("bsh.system.shutdownOnExit = false");
          ip.eval("desktop()");
        } catch (EvalError e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }*/
      }
    });
    
    this.addFrameTab("Tema izgleda", new raSkinDialog(null,res.getString("jmiStartFrSysLnF"),false), raImages.getModuleIcom("zapod"));
    this.addFrameTab("Slika", new backImgDialog(), raImages.DEFAULTBROWSEICON);
    this.addFrameTab(new UISettingsDialog(), raImages.IMGTLEAF);
    this.addFrameTab(new MailOptions());
    this.addAction("SQL Pilot", raImages.getModuleIcom("rn"), "hr.restart.sisfun.raPilot");
    this.addFrameTab(new raToolMenuPanel(this));
    this.addFrameTab("Log", new dlgFileViewer(Util.getLogFileName(),false,false), raImages.getModuleIcom("blpn"));
    this.addFrameTab(pvw = new raPropsViewer());
    this.addFrameTab("Postavke baze", new Parametri_app(null), raImages.getModuleIcom("gk"));
    this.addFrameTab("Postavke servera", new ServerDialog(), raImages.getModuleIcom("pos"));
    this.addFrameTab("Memorija", new MemDialog(), raImages.getModuleIcom("os"));
    
    this.addAction(new ActionTask("Izvoz podataka", raImages.getImageIcon(raImages.IMGTOPEN),null) {
      public void doAction() {
        raExportData.export();
      }
    });
    
    this.addAction(new ActionTask("Spremanje baze", raImages.getModuleIcom("gk"),null) {
      public void doAction() {
        raDbaseCreator.displayDumpDialog(optionsDialog.this);
      }
    });
    
    this.addFrameTab(new MenuUpdaterUI());
    this.addFrameTab(new hr.restart.util.versions.jpPatchMaker());
    this.setApplyMessageText("Da bi promjena parametara bila potpuna potrebno je restartati aplikaciju.");
    this.taskInit();
    loadAtEnd(raSkinDialog.class);
  }
  
  private ArrayList classesAtEnd = new ArrayList();
  private ArrayList loadersAtEnd = new ArrayList();
  public void loadAtEnd(Class cl) {
    classesAtEnd.add(cl);
  }
  public void show() {
    new Thread() {
      public void run() {
//        raDelayWindow rdw = raDelayWindow.show(optionsDialog.this,"Loading","Inicijalizacija...");
        for (int i=0;i<TabList.size();i++) {
          Object tabComp = TabList.get(i);
          if (tabComp instanceof hr.restart.util.loadFrame) {
            if (classesAtEnd.contains(tabComp.getClass())) {
              loadersAtEnd.add(tabComp);
            } else {
              ((loadFrame)tabComp).reload();
            }
          }
        }
        for (Iterator i = loadersAtEnd.iterator(); i.hasNext(); ) {
          loadFrame item = (loadFrame)i.next();
          item.reload();
        }
        pvw.reloadAll();
//        rdw.close();
      }
    }.start();
    super.show();
  }
  public static void main(String[] args) {
    startFrame.getStartFrame().showFrame("hr.restart.util.optionsDialog",0,"Korisnièki parametri");
    System.exit(0);
  }
}