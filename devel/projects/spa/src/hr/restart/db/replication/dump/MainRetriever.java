/****license*****************************************************************
**   file: MainRetriever.java
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
 * Created on Dec 7, 2004
 *
 */
package hr.restart.db.replication.dump;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author dgradecak
 */
public class MainRetriever {

    
	public static void main(String[] args) {
					    
	    
	    try {
            System.out.println("(MAIN) started");
            
                
                    final BeanFactory beanFactory = new ClassPathXmlApplicationContext("hr/restart/db/replication/dump/retreiver.xml");
                    
                    Retreiver e = (Retreiver)beanFactory.getBean("Retriever");
                    boolean go = true;
                    while(go){
                        go = e.execute();
                    }
                                

             System.out.println("Finished");

        } catch (Throwable e) {
            e.printStackTrace();
        }
	    
	}
}
