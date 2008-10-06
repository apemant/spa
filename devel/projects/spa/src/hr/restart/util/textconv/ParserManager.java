/****license*****************************************************************
**   file: ParserManager.java
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
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.textconv;

import java.io.File;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParserManager {
	
	private static Logger log = Logger.getLogger(ParserManager.class);
  /**
   * returns FileParser created by spring
   * @param context xml file with lines and columns defined - argument for org.springframework.context.support.ClassPathXmlApplicationContext
   * @param parserBeanID id of fileparser defined in context
   * @return parser
   */
  public static FileParser getFileParser(String context, String parserBeanID) {
    BeanFactory factory = new ClassPathXmlApplicationContext(context);
    FileParser parser = (FileParser)factory.getBean(parserBeanID);
    return parser;
  }
  /**
   * Parses given file with specified parser
   * @param parser 
   * @param file
   * @return map of lines parsed from given file where key is line number and value is ILine
   */
  public static TreeMap getParsedLines(FileParser parser, File file) {
    parser.setFile(file);
    parser.parse();
    return parser.getLinesparsed();
  }
  /**
   * Parses given file with parser defined in context file with parserBeanID
   * @param context xml file with lines and columns defined - argument for org.springframework.context.support.ClassPathXmlApplicationContext
   * @param parserBeanID id of fileparser defined in context
   * @param file file to parse
   * @return map of lines parsed from given file where key is line number and value is ILine
   */
  public static TreeMap getParsedLines(String context, String parserBeanID, File file) {
  	FileParser parser = getFileParser(context, parserBeanID);
    return getParsedLines(parser, file);
  }
  
  /**
   * Converts lines using converter
   * @see ILineConverter
   * @see ILine
   * @param lines
   * @param converter
   */
  public static boolean convertLines(TreeMap lines, ILineConverter converter) {
  	converter.begin();
  	try {
	  	for (Iterator iter = lines.keySet().iterator(); iter.hasNext();) {
	  		Object key = iter.next();
	  		ILine line = (ILine)lines.get(key);
	  		converter.convert(line);
	  	}
	  	converter.end();
	  	return true;
	} catch (Exception e) {
		converter.abort();
		log.fatal("conversion failed ",e);
		return false;
	}
  }
  /**
   * Converts lines using converter defined in context wirh converterBeanID
   * @param lines
   * @param context
   * @param converterBeanID
   * @return
   */
  public static boolean convertLines(TreeMap lines, String context, String converterBeanID) {
  	BeanFactory factory = new ClassPathXmlApplicationContext(context);
  	ILineConverter converter = (ILineConverter)factory.getBean(converterBeanID);
  	return convertLines(lines, converter);
  }
  /**
   * Parses given file with parser defined in context file with parserBeanID and converts resulting lines with converter
   * defined in same context and converterBeanID
   * @param context
   * @param parserBeanID
   * @param converterBeanID
   * @param file
   * @return
   */
  public static boolean parseAndConvertLines(String context, String parserBeanID, String converterBeanID, File file) {
    BeanFactory factory = new ClassPathXmlApplicationContext(context);
  	ILineConverter converter = (ILineConverter)factory.getBean(converterBeanID);
  	FileParser parser = (FileParser)factory.getBean(parserBeanID);
  	return convertLines(getParsedLines(parser, file),converter);
  }
//TEST
  public static void main(String[] args) {
/*    BeanFactory factory = new ClassPathXmlApplicationContext("texconv_test.xml");
    FileParser parser = (FileParser)factory.getBean("testFP");
    parser.setFile(new File("textconv_test.txt"));
    parser.parse();
    TreeMap parsed = parser.getLinesparsed();*/
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(System.getProperty("user.dir"));
    if (chooser.showOpenDialog(null) != javax.swing.JFileChooser.APPROVE_OPTION) {
      System.exit(0);
    }
    File file = chooser.getSelectedFile();
    TreeMap parsed = getParsedLines(args[0],args[1],file);
    for (Iterator iter = parsed.keySet().iterator(); iter.hasNext();) {
      Object key = iter.next();
      System.out.println("key = "+key);
      System.out.println("value = "+parsed.get(key));
      System.out.println("class = "+parsed.get(key).getClass());
    }
  }
}
