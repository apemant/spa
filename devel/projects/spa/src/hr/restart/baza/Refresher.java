/****license*****************************************************************
**   file: Refresher.java
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
package hr.restart.baza;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataModule;


public class Refresher extends KreirDrop implements DataModule {

  private static Refresher inst = new Refresher();

  Timer t = new Timer();
  TimerTask refresh = new TimerTask() {
    public void run() {
    	if (dm.getDatabase1().getAutoCommit() &&
    	  System.currentTimeMillis() >= lastRefresh + delay)
    		SwingUtilities.invokeLater(new Runnable() {
    		  public void run() {
    		    if (dm.getDatabase1().getAutoCommit() &&
    		        System.currentTimeMillis() >= lastRefresh + delay) {
    		      System.out.println("refreshing after " +
    		          (System.currentTimeMillis() - lastRefresh) + " ms");
    		      lastRefresh = System.currentTimeMillis();
    		      data.refresh();
    		    }
              }
            });
    }
  };
  
  int delay;
  static long lastRefresh;



  public static Refresher getDataModule() {
    return inst;
  }
  
  public static void postpone() {
    lastRefresh = System.currentTimeMillis();
  }


  public void begin(int refreshRate) {
    delay = refreshRate;
    lastRefresh = System.currentTimeMillis();
    t.schedule(refresh, refreshRate, refreshRate);
  }

  public void stop() {
    t.cancel();
  }
}
