package io.grasscutter.data.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.grasscutter.data.DataInterface;
import io.grasscutter.data.DataSerializable;
import io.grasscutter.utils.constants.DataConstants;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.interfaces.Serializable;
import java.util.Map;
import org.bson.Document;

/* A data interface for MongoDB. */
public final class MongoDBInterface implements DataInterface {
    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;

    @Override
    public void connect() {
        // Get the database properties.
        var properties = Properties.DATABASE();

        // Create a connection URI.
        var connectionUri = properties.overrideUri;
        if (connectionUri.isEmpty())
            connectionUri =
                    "mongodb://"
                            + properties.username
                            + ":"
                            + properties.password
                            + "@"
                            + properties.host
                            + ":"
                            + properties.port;

        // Connect to the database.
        this.mongoClient = MongoClients.create(connectionUri);
        this.mongoDatabase = this.mongoClient.getDatabase(properties.database);
    }

    @Override
    public void disconnect() {
        // Disconnect from the database.
        this.mongoClient.close();
    }

    @Override
    public void initialize() {
        // Create collections for all data serializable classes.
        for (var classType : DataConstants.SERIALIZABLE_DATA) {
            var collectionName = classType.getAnnotation(DataSerializable.class).table();
            this.mongoDatabase.createCollection(collectionName);
        }
    }

    @Override
    public void save() {}

    @Override
    public void save(Object object) {
        if (!DataInterface.isSerializable(object))
            throw new IllegalArgumentException("Object is not data serializable.");

        // Serialize the object.
        Serializable serializable = (Serializable) object;
        var serialized = serializable.serialize();

        // Create the data entry.
        var document = new Document();
        document.putAll(serialized);

        // Get the collection.
        var collectionName = DataInterface.getSerializeTable(object);
        var collection = this.mongoDatabase.getCollection(collectionName);
        // Get the ID name & value.
        var id = DataInterface.getSerializeId(object);

        // Check if the object already exists.
        var filter = new Document(id.a(), id.b());
        if (collection.countDocuments(filter) == 0) {
            // Insert the object.
            collection.insertOne(document);
        } else {
            // Update the object.
            collection.replaceOne(filter, document);
        }
    }

    @Override
    public <T> T get(Class<T> fetch, Map<String, Object> lookupArgs) {
        if (!DataInterface.isSerializable(fetch))
            throw new IllegalArgumentException("Class is not data serializable.");

        // Get the collection.
        var collectionName = DataInterface.getSerializeTable(fetch);
        var collection = this.mongoDatabase.getCollection(collectionName);

        // Convert lookup arguments to a document.
        var filter = new Document();
        filter.putAll(lookupArgs);

        // Perform lookup.
        var document = collection.find(filter).first();
        if (document == null) return null;

        try {
            // De-serialize the object.
            Serializable serializable = (Serializable) fetch.getConstructor().newInstance();
            serializable.deserialize(document);
            return fetch.cast(serializable);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public void purge(Object object) {}
}
