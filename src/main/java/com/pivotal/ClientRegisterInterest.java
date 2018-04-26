package com.pivotal;

import java.io.InputStreamReader;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionEvent;
import org.apache.geode.cache.client.ClientRegionShortcut;

/**
 * Created by jhuynh on 6/30/17.
 */
public class ClientRegisterInterest {

  public static void main(String[] args) throws Exception {
    GemFireTest test = new GemFireTest();
    test.createClientCache();

    PutTimeListener listener = new PutTimeListener(500000);

    Region region = test.createClientRegion("region", ClientRegionShortcut.PROXY, listener);
    long startRegexTime = System.nanoTime();

    region.registerInterestRegex(".*");
    System.out.println("Register interest took " + (System.nanoTime() - startRegexTime) + " ns");
    while (listener.numOps < 1500000) {
      Thread.sleep(15000);
      System.out.println("current ops count: " + listener.numOps);
    }

    System.out.println("Average across all runs:" + listener.getAverageDelta());
    System.out.println("WCET across all puts: " + listener.getWCET());
    InputStreamReader isr = new InputStreamReader(System.in);
    isr.read();
  }

  private static class PutTimeListener implements CacheListener {
    private long totalDiff = 0;
    private long numOps = 0;
    private final long warmup;
    private long warmupIters = 0;
    private long WCET = 0;

    public PutTimeListener(long warmup) {
      this.warmup = warmup;
    }

    public long getWCET() {
      return WCET;
    }

    public long getAverageDelta() {
      return totalDiff / numOps;
    }

    @Override
    public void afterCreate(final EntryEvent entryEvent) {
      if (warmupIters++ < warmup) {
        return;
      } else {
        long putTime = (Long) ((Object[]) entryEvent.getNewValue())[0];
        long currentTime = System.nanoTime();
        long commTime = currentTime - putTime;

        if (commTime > WCET)
          WCET = commTime;

        totalDiff += commTime;
        numOps++;
      }
    }

    @Override
    public void afterUpdate(final EntryEvent entryEvent) {
      System.out.println("received entry event for:" + entryEvent.getKey());
    }

    @Override
    public void afterInvalidate(final EntryEvent entryEvent) {

    }

    @Override
    public void afterDestroy(final EntryEvent entryEvent) {

    }

    @Override
    public void afterRegionInvalidate(final RegionEvent regionEvent) {

    }

    @Override
    public void afterRegionDestroy(final RegionEvent regionEvent) {

    }

    @Override
    public void afterRegionClear(final RegionEvent regionEvent) {

    }

    @Override
    public void afterRegionCreate(final RegionEvent regionEvent) {

    }

    @Override
    public void afterRegionLive(final RegionEvent regionEvent) {

    }

    @Override
    public void close() {

    }
  };
}
