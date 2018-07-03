package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.sisfun.EbayException;
import hr.restart.sisfun.EbayUtils;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.XYPanel;
import hr.restart.swing.raInputDialog;
import hr.restart.util.Aus;
import hr.restart.util.raProcess;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.andiv.print.VarStr;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.ebay.sdk.ApiCall;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.VariationsType;
import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.wsdl.parser.MemberSubmissionAddressingWSDLParserExtension;


public class ArteBayImporter {
  
  List images = new ArrayList();
  raInputDialog dlg;
  XYPanel content;
  
  JraComboBox chooser;
  raInputDialog dc;
  
  public ArteBayImporter() {
    dlg = new raInputDialog();
    content = new XYPanel(Aus.createSet("ITEM:30"));
    content.label("eBay item number").text("ITEM", 150).expand();
    
    dc = new raInputDialog() {
      protected boolean checkOk() {
        return chooser.getSelectedIndex() > 0;
      }
    };
  }
  
  public void clearImages() {
    images.clear();
  }
  
  public List getImages() {
    return images;
  }
  
  public void importTo(final frmArtikli art) {
    images.clear();
    if (dlg.show(art.getWindow(), content, "Dohvat artikla s eBaya")) {
      System.out.println("Artikl: " + content.getText("ITEM").getText());
      
      try {
        raProcess.runChild(art.getWindow(), new Runnable() {
          public void run() {
            raProcess.yield(EbayUtils.getInstance().getTradingItem(content.getText("ITEM").getText()));
          }
        });
        if (raProcess.isFailed()) {
          if (raProcess.getLastException() != null) 
            throw new EbayException(raProcess.getLastException().getMessage());
          throw new EbayException("Greška kod pristupa eBay-u!");
        }
        if (raProcess.isInterrupted()) return;
        ItemType item = (ItemType) raProcess.getReturnValue();
        if (item == null)
          throw new EbayException("Greška kod dohvata artikla s eBay-a!");
        
        Map attrs = new HashMap();
        
        VariationsType vars = item.getVariations();
        if (vars != null && vars.getVariationLength() > 1) {
          System.out.println("list length: " + vars.getVariationSpecificsSet().getNameValueListLength());
          NameValueListType vals = vars.getVariationSpecificsSet().getNameValueList(0);
          System.out.println("name: " + vals.getName() + "  values: " + Arrays.asList(vals.getValue()));
          
          String[] options = new String[vals.getValueLength() + 1];
          options[0] = "- Select -";
          System.arraycopy(vals.getValue(), 0, options, 1, vals.getValueLength());
                   
          chooser = new JraComboBox(options);
          XYPanel cont = new XYPanel();
          cont.label(vals.getName()).combo(chooser, 150).expand();
          
          if (!dc.show(art.getWindow(), cont, "Odabir varijante")) return;
          
          art.getRaQueryDataSet().setString("NAZART", limit(item.getTitle() + " (" + chooser.getSelectedItem() + ")", 75));
          importCommon(item, art.getRaQueryDataSet(), attrs);
          attrs.put(vals.getName(), chooser.getSelectedItem());
          //fillAttr(art.getRaQueryDataSet(), vals.getName(), (String) chooser.getSelectedItem());
          
          for (int i = 0; i < vars.getVariationLength(); i++) 
            if (vars.getVariation(i).getVariationSpecifics().getNameValueList(0).getName().equals(chooser.getSelectedItem())) {
              System.out.println("found specifics price");
              art.getRaQueryDataSet().setBigDecimal("MCREF", 
                  new BigDecimal(vars.getVariation(i).getStartPrice().getValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
              break;
            }
          if (vars.getPicturesLength() > 0)
            for (int i = 0; i < vars.getPictures(0).getVariationSpecificPictureSetLength(); i++) {
            if (vars.getPictures(0).getVariationSpecificPictureSet(i).getVariationSpecificValue().equals(chooser.getSelectedItem())) {
              System.out.println("found specifics images");
              for (int p = 0; p < vars.getPictures(0).getVariationSpecificPictureSet(i).getPictureURLLength(); p++)
                images.add(vars.getPictures(0).getVariationSpecificPictureSet(i).getPictureURL(p));
              break;
            }
          }
        } else {
          art.getRaQueryDataSet().setString("NAZART", limit(item.getTitle(), 75));
          importCommon(item, art.getRaQueryDataSet(), attrs);
        }
        art.getRaQueryDataSet().setString("NAZPRI", art.getRaQueryDataSet().getString("NAZART"));
        art.getRaQueryDataSet().setString("NAZLANG", art.getRaQueryDataSet().getString("NAZART"));
        
        if (item.getStorefront() != null) {
          art.addExternalCategory(item.getStorefront().getStoreCategoryID() + "");
          art.addExternalCategory(item.getStorefront().getStoreCategory2ID() + "");
        }
        
        if (item.getPictureDetails() != null) {
          for (int i = 0; i < item.getPictureDetails().getPictureURLLength(); i++)
            images.add(item.getPictureDetails().getPictureURL(i));
        }
        
        fillAttributes(art, attrs);
        
        if (images.size() > 0) {
          raProcess.runChild(art.getWindow(), "Dohvat artikla", "Preuzimanje slika...", new Runnable() {
            public void run() {
              try {
                List files = new ArrayList();
                for (Iterator i = images.iterator(); i.hasNext(); ) {
                  File f = File.createTempFile("img", null);
                  f.deleteOnExit();
                  EbayUtils.getInstance().saveImage(f, (String) i.next());
                  files.add(f);
                }
                images.clear();
                images.addAll(files);
              } catch (IOException e) {
                images.clear();
                raProcess.fail();
              }
              System.out.println("all images: " + images);
            }
          });
          if (raProcess.isFailed())
            throw new EbayException("Greška kod dohvata slika!");
        }
      } catch (EbayException e) {
        images.clear();
        e.printStackTrace();
        JOptionPane.showMessageDialog(art.getWindow(), e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
    }
  }
  
  void importCommon(ItemType it, DataSet ds, Map attrs) {
    for (int i = 0; i < it.getItemSpecifics().getNameValueListLength(); i++) {
      NameValueListType attr = it.getItemSpecifics().getNameValueList(i);
      if (attr.getValueLength() == 0) continue;
      String name = attr.getName().trim();
      String val = VarStr.join(attr.getValue(), ' ').trim().toString();
      
      System.out.println(name + " = " + val);
      //fillAttr(ds, name, val);
      attrs.put(name, val);
      
    }
    ds.setBigDecimal("MCREF", new BigDecimal(it.getSellingStatus().getCurrentPrice().getValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
  }
 
  /*void fillAttr(DataSet ds, String name, String val) {
    if (name.equalsIgnoreCase("Brand")) {
      ds.setString("NAZPROIZ", limit(val, 50));
    } else if (name.equalsIgnoreCase("Color")) {
      ds.setString("BOJA", limit(val, 20));
    } else if (name.equalsIgnoreCase("Model")) {
      ds.setString("MODEL", limit(val, 50));
    }
  }*/
  
  void fillAttr(DataSet ds, String col, String val) {
    if (col.equalsIgnoreCase("ignore")) return;
    
    Column c = ds.hasColumn(col);
    if (c == null) {
      System.out.println("invalid column?! " + col + " = " + val);
      return;
    }
    if (c.getDataType() == Variant.STRING) 
      ds.setString(col, limit(val, c.getPrecision()));
    else if (c.getDataType() == Variant.BIGDECIMAL) 
      ds.setBigDecimal(col, Aus.getDecNumber(val).setScale(c.getScale(), BigDecimal.ROUND_HALF_UP));
    else if (c.getDataType() == Variant.INT)
      ds.setInt(col, Aus.getNumber(val));
    else System.out.println("invalid column?! " + col + " = " + val);
  }
  
  void fillAttributes(frmArtikli art, Map attrs) {
    Map defs = new HashMap();
    String[] captions = {"Ignorirati", "Brand", "Model", "Boja", "Dužina", "Težina", "Ship. težina", "Materijal", "Dimenzije",
          "Rod Power", "No. of pieces", "Line weight", "Hand Retrieve", "Ball bearings", "Max Drag", "Gear Ratio", "Line Capacity"};
    String[] names = {"ignore", "NAZPROIZ", "MODEL", "BOJA", "DUZINA", "TEZINA", "SHTEZ", "MATERIJAL", "DIMENZIJE",
        "SNAGA", "KOMADA", "LTEZ", "RUCKA", "KUGLEZ", "TRENJE", "ZOMJER", "LKAP"};
    
    for (int i = 0; i < captions.length; i++) {
      defs.put(names[i], frmParam.getParam("robno", "ebay.attr." + names[i], "", "Popis eBay atributa za " + captions[i]));
    }
    System.out.println(defs);
    System.out.println(attrs);
    
    Set undef = new HashSet();
    for (Iterator i = attrs.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry e = (Map.Entry) i.next();
      boolean def = false;
      for (int c = 0; c < names.length && !def; c++)
        if (((String) defs.get(names[c])).indexOf((String) e.getKey()) >= 0)
          def = true;
      if (!def) undef.add(e.getKey());
    }
    
    if (undef.size() > 0) {
      String[] undefs = (String[]) undef.toArray(new String[undef.size()]);
      JraComboBox[] unpick = new JraComboBox[undefs.length];
      XYPanel picker = new XYPanel();
      picker.label("eBay atribut").label("Veza u SPA").nl().nl();
      for (int i = 0; i < undefs.length; i++) {
        unpick[i] = new JraComboBox(captions);
        picker.label(undefs[i].length() <= 20 ? undefs[i] : undefs[i].substring(0,18) + "...").combo(unpick[i], 175).nl();
      }
      picker.expand();
      if (dlg.show(art.getWindow(), picker, "Povezivanje atributa")) {
        Map ndef = new HashMap();
        for (int i = 0; i < undefs.length; i++)
          if (unpick[i].getSelectedIndex() >= 0) {
            String name = names[unpick[i].getSelectedIndex()];
            String val = (String) (ndef.containsKey(name) ? ndef.get(name) : defs.get(name));
            if (val == null || val.length() == 0) val = undefs[i];
            else if (val.length() + undefs[i].length() < 495) val = val + "|" + undefs[i];
            ndef.put(name, val);
            defs.put(name, val);
          }
        
        for (Iterator i = ndef.entrySet().iterator(); i.hasNext(); ) {
          Map.Entry e = (Map.Entry) i.next();
          frmParam.setParam("robno",  "ebay.attr." + e.getKey(), (String) e.getValue());
        }
        dM.getSynchronizer().markAsDirty("parametri");
      }
    }
    
    for (Iterator i = attrs.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry e = (Map.Entry) i.next();
      for (int c = 0; c < names.length; c++)
        if (((String) defs.get(names[c])).indexOf((String) e.getKey()) >= 0)
          fillAttr(art.getRaQueryDataSet(), names[c], (String) e.getValue());
    }
  }
  
  String limit(String val, int len) {
    if (val.length() <= len) return val;
    return val.substring(0, len);
  }
}
