/****license*****************************************************************
**   file: DataPostreceiver.java
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
/*
 * Created on Oct 12, 2005
 */
package hr.restart.util.mail;

/**
 * Interface for running logic after DataReceiver receives and loads data
 * <pre>
 * Sample:
 * 
 * step 1.
 * DataReceiver datareceiver = new DataReceiver();
 * ... (setup of datareceiver)
 * datareceiver.addDataPostReceiver(new MyDataPostreceiver());
 * 
 * step 2.
 * public class MyDataPostreceiver implements DataPostreceiver {
 * 		public boolean run(DataReceiverLoadedData data) {
 * 			QueryDataSet[] dokis = data.getLoadedSetsByModule("doki");
 * 			for (int i = 0; i < dokis.length; i++) {
 * 				doSomethingWithDoki(dokis[]);
 * 			}
 * 		}
 * }
 * </pre>
 * @see hr.restart.util.mail.DataReceiver
 * @see hr.restart.util.mail.DataReceiverLoadedData
 * @author andrej
 */
public interface DataPostreceiver {
  /**
   * Runs after after DataReceiver receives and loads data
   * @param data object of type DataReceiverLoadedData which contains data 
   * @return false if receiving should fail and no other postreceivers should run, othervise true
   */
  public boolean run(DataReceiverLoadedData data);
  
  /**
   * Info to display in info status of DataReceiver eg. 
   * <code>return "snimanje promjena"</code> will result in message "Pokrecem snimanje promjena ..."
   * @return
   */
  public String getShortInfo();
}
