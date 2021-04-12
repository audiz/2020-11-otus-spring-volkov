package ru.otus.work23.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.apache.commons.io.IOUtils;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@ChangeLog(order = "001")
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "a.volkov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "dataInsert", author = "a.volkov", runAlways = true)
    public void insertData(MongoDatabase db) {
        importCollection(db, "GENRES");
        importCollection(db, "AUTHORS");
        importCollection(db, "BOOKS");
        importCollection(db, "COMMENTS");
        importCollection(db, "USERS");
        importCollection(db, "db_sequence");
    }

    private void importCollection(MongoDatabase db, String collection) {
        MongoCollection<Document> mongoCollection = db.getCollection(collection);
        InputStream inputStream = getClass().getResourceAsStream("/data/" + collection + ".json");
        try {
            List<String> jsons = IOUtils.readLines(inputStream, "UTF-8");
            inputStream.close();
            mongoCollection.insertMany(
                    jsons.stream().map(Document::parse).collect(Collectors.toList())
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
