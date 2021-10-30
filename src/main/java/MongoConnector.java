import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.client.*;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.Calendar;


public class MongoConnector {
  private MongoDatabase database;
  private MongoCollection<Document> collection;

  public MongoConnector(String uri, String databaseName, String collectionName) {
    ConnectionString connectionString = new ConnectionString(uri);
    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .build();
    MongoClient mongoClient = MongoClients.create(settings);
    database = mongoClient.getDatabase(databaseName);
    collection = database.getCollection(collectionName);
  }

  public void insertReading(BLEReading reading) {
    Document document = new Document();

    document.put("temperature", reading.getTemp());
    document.put("humidity", reading.getHum());
    document.put("timestamp", addHours(reading.getTimestamp(), 1));

    //    TODO - testar funcionamento do error handling
    try {
      collection.insertOne(document);
    }
    catch (Exception error) {
      error.printStackTrace();
    }

  }

  public void insertDocument(Document doc) {
    collection.insertOne(doc);
  }

  private void printInsertion(BLEReading reading) {
    System.out.println("tempearture: " + reading.getTemp());
    System.out.println("humidity: " + reading.getHum());
    System.out.println("timestamp: " + reading.getTimestamp());
  }

  private static Timestamp addHours(Timestamp ts, int hours) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(ts.getTime());
    cal.add(Calendar.HOUR, hours);
    Timestamp nts = new Timestamp(cal.getTime().getTime());

    return nts;
  }

  public void deleteAllReadings() {
    collection.deleteMany(new Document());
  }

  public void printAllReadings() {
    MongoCursor<Document> cursor = collection.find().iterator();
    while (cursor.hasNext()) {
      System.out.println(cursor.next());
    }
  }

  public MongoCursor<Document> getCollectionAsCursor() {
    return collection.find().iterator();
  }

}
