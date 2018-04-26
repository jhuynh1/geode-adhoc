package com.pivotal;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.query.Index;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.internal.index.HashIndex;
import org.apache.geode.cache.util.ObjectSizer;

import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.stream.IntStream;

/**
 * Created by jhuynh on 6/30/17.
 */
public class SizeTest {

  Cache cache = new CacheFactory().create();

  public static void main(String[] args) throws Exception {
      SizeTest test = new SizeTest();
      test.main();
  }

  private void main() throws Exception {

    Region region = cache.createRegionFactory(RegionShortcut.REPLICATE).create("TestRegion");
    QueryService qs = cache.getQueryService();
    Index index = qs.createHashIndex("MyIndex", "ID", "/TestRegion");
    System.out.println(ObjectSizer.DEFAULT.sizeof(index));

    IntStream.range(0, 10000).forEach(i -> region.put(i, new SimpleObject(i)));

    System.out.println("HashINdex:" + ((HashIndex)index).getStatistics().getNumberOfValues());
    InputStreamReader isr = new InputStreamReader(System.in);
    isr.read();
  }

  private class SimpleObject implements Serializable {
    public int ID;

    public SimpleObject() {

    }
    public SimpleObject (int ID) {
      this.ID = ID;
    }
  }
}

