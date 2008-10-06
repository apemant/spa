package example2;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class HelloWorld {
  static Logger log = Logger.getLogger("example2.HelloWorld");

  public static void main(String[] args) {
    BasicConfigurator.configure();
    log.debug("Entering main().");
    System.out.println("Hello World!");
    log.debug("Leaving main().");
  }
}
