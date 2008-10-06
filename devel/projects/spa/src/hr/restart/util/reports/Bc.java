package hr.restart.util.reports;

import java.awt.image.BufferedImage;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;


public class Bc {
  public static BufferedImage getImg(Object txt) {
    try {
      Barcode bc = BarcodeFactory.createCode128(txt.toString());
      bc.setDrawingText(false);
      return BarcodeImageHandler.getImage(bc);
    } catch (Exception e) {
      throw new RuntimeException("Invalid barcode text");
    }
  }
}
