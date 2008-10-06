/****license*****************************************************************
**   file: MainExtracter.java
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
package hr.restart.db.replication.dump;

import hr.restart.db.replication.dump.Extracter;

import java.sql.SQLException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/*
 * Created on Oct 19, 2004
 *
 */

/**
 * @author dgradecak
 */
public class MainExtracter {

    
	public static void main(String[] args) {
					    
	    
	    try {
            System.out.println("(MAIN) started");
            
                
                    final BeanFactory beanFactory = new ClassPathXmlApplicationContext("hr/restart/db/replication/dump/dump.xml");
                    
                    Extracter e = (Extracter)beanFactory.getBean("Extracter");
                    e.extract();
                                

             System.out.println("Finished");
        } catch (BeansException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	    
	}
}
