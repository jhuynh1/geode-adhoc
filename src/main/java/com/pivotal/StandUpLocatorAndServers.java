package com.pivotal;

/**
 * Created by jhuynh on 6/30/17.
 */
public class StandUpLocatorAndServers {

  public static void main(String[] args) throws Exception {
    GemFireTest test = new GemFireTest();
    test.startLocator();
    test.startServer("server1", 48888, "--J=-Xmx80m --J=-Xms80m --J=-Dgemfire.luceneReindex=true");
//    test.startServer("server2", 48889, "--J=-Xmx128m --J=-Xms128m");
  }
}
