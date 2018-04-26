package com.pivotal;

/**
 * Created by jhuynh on 6/30/17.
 */
public class StopLocatorAndServers {

  public static void main(String[] args) throws Exception {
    GemFireTest test = new GemFireTest();
    test.stopServer("server1");
    System.out.println("stopped server 1");
    test.stopServer("server2");
    System.out.println("stopped server 2");
    test.stopServer("server3");
    System.out.println("stopped server 3");
    test.stopLocator();
  }
}
