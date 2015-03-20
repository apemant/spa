package hr.restart.util.reports;

import java.awt.image.BufferedImage;
import java.io.IOException;


public class Bc {
  public static BufferedImage getImg(Object txt) {
    try {
      net.sourceforge.barbecue.Barcode bc = net.sourceforge.barbecue.BarcodeFactory.createCode128(txt.toString());
      bc.setDrawingText(false);
      return net.sourceforge.barbecue.BarcodeImageHandler.getImage(bc);
    } catch (Exception e) {
      throw new RuntimeException("Invalid barcode text");
    }
  }
  
  public static BufferedImage getPDF417(Object txt) {
    try {
      org.krysalis.barcode4j.impl.pdf417.PDF417Bean bean = new org.krysalis.barcode4j.impl.pdf417.PDF417Bean();
      org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider canvas = 
          new org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider(600, BufferedImage.TYPE_BYTE_BINARY, false, 0);
      bean.setModuleWidth(0.5f);
      
      bean.setBarHeight(25.4f);
      bean.generateBarcode(canvas, txt.toString());
      canvas.finish();
      return canvas.getBufferedImage();
    } catch (IOException e) {
      throw new RuntimeException("Invalid barcode text");
    }
  }
}
