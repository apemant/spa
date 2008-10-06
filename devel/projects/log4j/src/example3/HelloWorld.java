package example3;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class HelloWorld {
  static Logger log = Logger.getLogger("example3.HelloWorld");

  public static void main(String[] args) {
    PropertyConfigurator.configure(ClassLoader.getSystemResource("log.properties"));
    log.debug("Entering main().");
    System.out.println("Hello World!");
    log.debug("Leaving main().");
  }
}
