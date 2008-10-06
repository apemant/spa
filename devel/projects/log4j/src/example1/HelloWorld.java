package example1;

import org.apache.log4j.Logger;

public class HelloWorld {
  static Logger log = Logger.getLogger("example1.HelloWorld");

  public static void main(String[] args) {
    log.debug("Entering main().");
    System.out.println("Hello World!");
    log.debug("Leaving main().");
  }
}
