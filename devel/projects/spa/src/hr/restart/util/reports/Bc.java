package hr.restart.util.reports;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.krysalis.barcode4j.impl.pdf417.PDF417Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

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
  
  public static BufferedImage getPDF417(Object txt) {
    try {
      PDF417Bean bean = new PDF417Bean();
      BitmapCanvasProvider canvas = new BitmapCanvasProvider(600, BufferedImage.TYPE_BYTE_BINARY, false, 0);
      bean.setModuleWidth(0.5f);
      
      bean.setBarHeight(UnitConv.in2mm(1.0f));
      bean.generateBarcode(canvas, txt.toString());
      canvas.finish();
      return canvas.getBufferedImage();
    } catch (IOException e) {
      throw new RuntimeException("Invalid barcode text");
    }
  }
}
