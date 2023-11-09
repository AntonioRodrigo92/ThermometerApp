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
      System.out.println("### " + Utils.getCurrentDateTime() + "Inserting Reading into MongoDB");
      collection.insertOne(document);
      System.out.println("### " + Utils.getCurrentDateTime() + "Inserted Reading with success");
    }
    catch (Exception error) {
      System.out.println("### " + Utils.getCurrentDateTime() + "ERROR while inserting into MongoDB");
      error.printStackTrace();
    }
    finally {

    }

  }


}
