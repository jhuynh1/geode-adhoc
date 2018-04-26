package com.pivotal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

/**
 * Created by jhuynh on 6/29/17.
 */
public class GemFireTest {

  private ClientCache cache;

  private static String GFSH_LOCATION = "/Users/jhuynh/Pivotal/gemfire/geode-fork/geode-assembly/build/install/apache-geode/bin/";

  public GemFireTest() {}

  protected void createClientCache() {
    ClientCacheFactory ccf = new ClientCacheFactory();
    ccf.addPoolLocator("10.118.33.173", 10333);
    ccf.setPoolSubscriptionEnabled(true);
//    ccf.setPoolSubscriptionAckInterval(10);
    System.setProperty("gemfire.statistic-archive-file", "client_stats.gfs");
    ccf.set("cache-xml-file", "/Users/jhuynh/Pivotal/geode-jh/src/main/resources/client-cache.xml");
    cache = ccf.create();
  }

  protected Region createClientRegion(String regionName, ClientRegionShortcut regionShortcut) {
    return cache.createClientRegionFactory(regionShortcut).setStatisticsEnabled(true).create(regionName);
  }

  protected Region createClientRegion(String regionName, ClientRegionShortcut regionShortcut, CacheListener listener) {
    return cache.createClientRegionFactory(regionShortcut).setStatisticsEnabled(true).addCacheListener(listener).create(regionName);
  }

  protected  void startLocator() throws Exception {
    executeCommand(GFSH_LOCATION+ "gfsh start locator --name=locator --port=10333");
  }

  protected void startServer(String serverName, int serverPort, String arguments) throws Exception {
    System.out.println("Starting server " + serverName + " with " + GFSH_LOCATION + "gfsh start server " + arguments + " --name=" + serverName + " --locators=10.118.33.173[10333]--server-port=" + serverPort + " --statistic-archive-file=stats.gfs");
//    executeCommand(GFSH_LOCATION + "gfsh start server --name=" + serverName + " --locators=localhost[10333] --server-port=" + serverPort + " --cache-xml-file=./resources/cache.xml --statistic-archive-file=stats.gfs" + arguments);
    executeCommand(GFSH_LOCATION + "gfsh start server --name=" + serverName + " " + arguments + " --locators=10.118.33.173[10333] --server-port=" + serverPort + " --log-level=fine --statistic-archive-file=stats.gfs" + " --cache-xml-file=/Users/jhuynh/Pivotal/geode-jh/src/main/resources/cache.xml ");
  }

  protected void stopServer(String dir) throws Exception {
    executeCommand(GFSH_LOCATION+ "gfsh stop server --dir=" + dir);
  }

  protected void stopLocator() throws Exception {
    executeCommand(GFSH_LOCATION+ "gfsh stop locator --dir=locator");
  }

  protected void executeCommand(String command) throws Exception {
    ProcessBuilder pb = new ProcessBuilder();
    Process pr = pb.command(command.split(" ")).start();
    pr.waitFor();
    InputStream is = pr.getInputStream();
    BufferedReader bis = new BufferedReader(new InputStreamReader(is));
    String line = bis.readLine();
    while (line != null) {
      System.out.println(line);
      line =  bis.readLine();
    }
  }
}
