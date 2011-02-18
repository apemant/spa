/****license*****************************************************************
**   file: ReportImporter.java
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
 * Created on Jul 6, 2005
 */
package hr.restart.util;

import hr.restart.baza.Reportext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.borland.dx.sql.dataset.QueryDataSet;



/**
 * @author andrej
 * Intencija je da se automatski importa report u aplikaciju iz strukturiranog file-a.
 * File treba imati dva taga od kojih je prvi definicija sloga u reportext, 
 * a drugi definicija samog reporta.
 */
public class ReportImporter {
  public Document importReport() {
    JFileChooser chooser = new JFileChooser();
    if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
System.out.println("chooser is canceled");
      return null;
    }
    File f = chooser.getSelectedFile();
    try {
      Document doc = importReport(f);
      
      //napravi fileove
      int sqlnum = doc.getElementsByTagName("sql").getLength();
      for (int i = 0; i < sqlnum; i++) {
        String fileName = getTagAttribute(doc,"sql",i,"file");
        String filecontent = getTagValue(doc, "sql", i);
        filecontent = new VarStr(filecontent)
        			.trim()
        			.toString();
        FileHandler.writeConverted(filecontent, fileName, null);
      }
      //napravi dat
      String datcontent = getTagValue(doc, "dat",0);
      //separator
      String sep = Aus.getDumpSeparator();
      String linesep = System.getProperty("line.separator");
      datcontent = new VarStr(datcontent).trim()
									.replaceAll("</sep>",sep) //replace separatora
/*									.replaceAll(sep+linesep,sep+"</LF>") //sacuaj lf-ove koji trebaju bit
									.replaceAll(linesep,"") //makni sve line separatore
									.replaceAll("</LF>",linesep) //vrati koje trebaju
									.replaceLast(sep+linesep,sep) //makni zadnji enter*/
									.toString();
      FileHandler.writeConverted(datcontent,"_imprepext.dat",null);
      
      //napuni tablicu
      QueryDataSet loaded = Reportext.getDataModule().loadData(new File("."),"_imprepext");
      loaded.saveChanges();
      //izvjesti
      VarStr info = new VarStr("Dodavanje dodatnih izvještaja uspjelo! \n Dodani izvještaji: \n");
      for (loaded.first(); loaded.inBounds(); loaded.next()) {
        info = info.append(loaded.getString("NASLOV")+"\n");
      }
      JOptionPane.showMessageDialog(null, info, "INFO", JOptionPane.INFORMATION_MESSAGE);
      return doc;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Neuspješno dodavanje dodatnih izvještaja! Greška: "+e,"GREŠKA!", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
      return null;
    }
    
  }

  /**
   * @param f
   * @throws SAXException
   * @throws IOException
   * @throws FileNotFoundException
   */
  public Document importReport(File f) throws Exception {
    //parse xml
    DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
    //dbfactory.setAttribute("","");
    DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
    Document doc = dbuilder.parse(f);
/*    NodeList nodes = doc.getChildNodes();
    if (nodes!=null) {
      browseNodes(nodes);
    }*/
    return doc;
  }
  /**
   * 
   * @param doc
   * @param tag
   * @param idx
   * @return
   */
  public String getTagValue(Document doc, String tag,int idx) {
    return getTagNode(doc, tag, idx, false).getNodeValue();        
  }
/**
 * 
 * @param doc
 * @param tag
 * @param idx
 * @param attr
 * @return
 */
  public String getTagAttribute(Document doc, String tag,int idx, String attr) {
    NamedNodeMap nnmap = getTagNode(doc, tag, idx, true).getAttributes();
    if (nnmap == null) return null;
    return nnmap.getNamedItem(attr).getNodeValue();
  }

  public Node getTagNode(Document doc, String tag,int idx, boolean rutzitem) {
    ArrayList rets = new ArrayList();
    NodeList rutz = doc.getElementsByTagName(tag);
    if (rutzitem) {
      return rutz.item(idx);
    }
    NodeList nodes = rutz.item(idx).getChildNodes();
    return nodes.item(0);       
  }
  public void exportReports() {
    String xmlfn = JOptionPane.showInputDialog(null,"naziv file-a za export");
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("ODABERI reportext.dat");
    if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
      System.out.println("chooser is canceled");
      return;
    }
    String datPath =chooser.getSelectedFile().getAbsolutePath();

    chooser.setDialogTitle("ODABERI sqlove");
    chooser.setMultiSelectionEnabled(true);
    if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
      System.out.println("chooser is canceled");
      return;
    }
    File[] _ftmp =chooser.getSelectedFiles();
    String[] sqlPaths = new String[_ftmp.length];
    for (int i = 0; i < _ftmp.length; i++) {
      sqlPaths[i] = _ftmp[i].getAbsolutePath();
    }
    exportReports(xmlfn, datPath, sqlPaths);
  }
  public void exportReports(String xmlFileName, String datPath, String[] sqlPaths) {
    try {
      String enc = getEnc();
      String encxml = getEncXml(enc);
      String content = "<?xml version=\"1.0\" encoding=\""+encxml+"\"?>\n<reportext>\n";
      String datcont = FileHandler.readFile(datPath,enc);
      //separator
      String sep = Aus.getDumpSeparator();
      datcont = new VarStr(datcont).replaceAll(sep,"</sep>").toString();
      content = content + "  <dat><![CDATA[\n"+datcont+"\n  ]]></dat>\n";
      for (int i = 0; i < sqlPaths.length; i++) {
        String sqlcont = FileHandler.readFile(sqlPaths[i],enc);
        content = content + "\n  <sql file=\""+new File(sqlPaths[i]).getName()+"\"><![CDATA[\n"+sqlcont+"\n  ]]></sql>\n";
      }
      content = content + "\n</reportext>\n";
      //TextFile.open(xmlFileName,TextFile.WRITE).out(content).close();
      FileHandler.writeConverted(content,xmlFileName, enc);
    } catch (Exception e) {
      // TODO: handle exception
    }
    
  }

  /**
   * @param enc
   * @return
   */
  private String getEncXml(String enc) {
    String encxml = enc;
//  sve za windozere 1250
    encxml = "Windows-1250";
/*    
 if (System.getProperty("os.name").toLowerCase().startsWith("windows") && enc.toLowerCase().startsWith("cp")) {
      encxml = new VarStr(enc).replaceIgnoreCase("Cp", "Windows-").toString();
    }
*/
    return encxml;
  }

  private String getEnc() {
    String enc = System.getProperty("file.encoding");
    return enc;
  }

  public void browseNodes(NodeList nodes) {
    for (int i = 0; i < nodes.getLength(); i++) {
      try {
        System.out.println("=======================================");
        System.out.println("item="+nodes.item(i));
        System.out.println("value="+nodes.item(i).getNodeValue());
        System.out.println("attr="+nodes.item(i).getAttributes());
        System.out.println("name="+nodes.item(i).getNodeName());
        System.out.println("type="+nodes.item(i).getNodeType());
        System.out.println("prefix="+nodes.item(i).getPrefix());
        System.out.println("=======================================");
      } catch (Exception e) {
        System.out.println(e);
      }
      browseNodes(nodes.item(i).getChildNodes());
    }
  }

}
