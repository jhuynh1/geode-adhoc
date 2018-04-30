package com.pivotal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;


/**
 * Created by jhuynh on 6/30/17.
 */
public class ClientPutter {

  public static void main(String[] args) throws Exception {
    GemFireTest test = new GemFireTest();
    test.createClientCache();

    Region region = test.createClientRegion("region", ClientRegionShortcut.PROXY);

    int numPut = 0;
    try {
      for (int i = 0; i < 50; i++) {
        numPut++;
        region.put(i, new SomeObject(i));
        if (i % 100 == 0) {
          System.out.println("numObjects Added" + i);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Total put is: " + numPut);
  }
//
//  private static void doPutAlls(Region region, int startIndex, int endIndex) {
//    System.out.println("Putting entries: " + startIndex + "-" + endIndex);
//    HashMap map = new HashMap();
//    for (int i = startIndex; i < endIndex; i++) {
//      //map.put(i, new Object[]{System.nanoTime(), new long[256]});
//      map.put(i, new SomeObject());
////      map.put(i, "something");
////      map.put(i, new SerializableClass());
////      map.put(i + "B", new ClientPingMessageImpl());
//      map.put(i + "A", new C());
//    }
//    region.putAll(map);
//  }

  public static class SerializableClass implements Serializable {
    public String um = "UM";
  }

  public static class SomeObject implements PdxSerializable {
    public String age;
    public String address;
    public String firstName;
    public String lastName;
    public String zip;
    public String alias;
    public String telephone;

    public SomeObject(int i) {
      age = UUID.randomUUID().toString();
      address = UUID.randomUUID().toString();
      firstName = UUID.randomUUID().toString();
      lastName = UUID.randomUUID().toString();
      zip = UUID.randomUUID().toString();
      alias = UUID.randomUUID().toString();
      telephone = UUID.randomUUID().toString();

    }

    @Override
    public void toData(PdxWriter pdxWriter) {
      pdxWriter.writeString("age", age);
      pdxWriter.writeString("address", address);
      pdxWriter.writeString("firstName", firstName);
      pdxWriter.writeString("lastName", lastName);
      pdxWriter.writeString("zip", zip);
      pdxWriter.writeString("alias", alias);
      pdxWriter.writeString("telephone", telephone);

    }

    @Override
    public void fromData(PdxReader pdxReader) {
      age = pdxReader.readString("age");
      address = pdxReader.readString("address");
      firstName = pdxReader.readString("firstName");
      lastName = pdxReader.readString("lastName");
      zip = pdxReader.readString("zip");
      alias = pdxReader.readString("alias");
      telephone = pdxReader.readString("telephone");
    }
  }
}