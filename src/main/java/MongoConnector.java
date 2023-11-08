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

  public void insertReading(BLEReading reading, boolean extraHour) {
    Document document = new Document();

    document.put("temperature", reading.getTemp());
    document.put("humidity", reading.getHum());
    if(extraHour) {
      document.put("timestamp", Utils.addHours(reading.getTimestamp(), 1));
    }
    else {
      document.put("timestamp", reading.getTimestamp());
    }

    //    TODO - testar funcionamento do error handling
    try {
      System.out.println(Utils.getCurrentDateTime() + "Inserting Reading into MongoDB");
      collection.insertOne(document);
    }
    catch (Exception error) {
      error.printStackTrace();
    }
    finally {

    }

  }

//  public void insertDocument(Document doc) {
//    collection.insertOne(doc);
//  }

//  private void printInsertion(BLEReading reading) {
//    System.out.println("tempearture: " + reading.getTemp());
//    System.out.println("humidity: " + reading.getHum());
//    System.out.println("timestamp: " + reading.getTimestamp());
//  }


//  public void deleteAllReadings() {
//    collection.deleteMany(new Document());
//  }

//  public void printAllReadings() {
//    MongoCursor<Document> cursor = collection.find().iterator();
//    while (cursor.hasNext()) {
//      System.out.println(cursor.next());
//    }
//  }

//  public MongoCursor<Document> getCollectionAsCursor() {
//    return collection.find().iterator();
//  }

}
