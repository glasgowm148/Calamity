package org.terrier.asp.database;

import com.mongodb.MongoClient;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.terrier.asp.conf.ASPModuleConf;
import org.terrier.asp.conf.ASPReaderConf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import static com.mongodb.client.model.Filters.*;




/**
 * ASP uses stored configurations for modules in a MongoDB instance on an external server.
 * 
 * These configurations will be loaded as needed by ASP.
 * 
 * @author richardm
 *
 */
public class ASPConfigurationDB {

	String host;
	
	public ASPConfigurationDB(String host) {
		this.host = host;
	}
	
	
	public boolean storeModuleConf(ASPModuleConf configuration) {
		
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(host);
			MongoDatabase database = mongoClient.getDatabase("ASPConfiguration");
			MongoCollection<Document> collection = database.getCollection("ASPConfigurationObjects");
			
			ObjectWriter jsonWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

			Document bson = Document.parse(jsonWriter.writeValueAsString(configuration));
			
			Document existingDoc = collection.find(eq("moduleID", configuration.getModuleID())).first();
			if (existingDoc==null) {
				collection.insertOne(bson);
			} else {
				collection.findOneAndReplace(existingDoc, bson);
			}

			mongoClient.close();
			return true;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		} finally {
			mongoClient.close();
		}
		
		
	}
	
	
	public ASPModuleConf getModuleConf(String moduleID) {
		
			MongoClient mongoClient = new MongoClient(host);
			MongoDatabase database = mongoClient.getDatabase("ASPConfiguration");
			MongoCollection<Document> collection = database.getCollection("ASPConfigurationObjects");
			
			
			Document existingDoc = collection.find(eq("moduleID", moduleID)).first();
			
			ASPModuleConf conf = null;
			try {
				if (existingDoc!=null) {
					String json = existingDoc.toJson();
					conf = new ObjectMapper().readValue(json, ASPModuleConf.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			mongoClient.close();
			return conf;

	}
	
public boolean storeReaderConf(ASPReaderConf configuration) {
		
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(host);
			MongoDatabase database = mongoClient.getDatabase("ASPConfiguration");
			MongoCollection<Document> collection = database.getCollection("ASPConfigurationObjects");
			
			ObjectWriter jsonWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

			Document bson = Document.parse(jsonWriter.writeValueAsString(configuration));
			
			Document existingDoc = collection.find(eq("readerID", configuration.getReaderID())).first();
			if (existingDoc==null) {
				collection.insertOne(bson);
			} else {
				collection.findOneAndReplace(existingDoc, bson);
			}

			mongoClient.close();
			return true;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		} finally {
			mongoClient.close();
		}
		
		
	}
	
	
	public ASPReaderConf getReaderConf(String readerID) {
		
			MongoClient mongoClient = new MongoClient(host);
			MongoDatabase database = mongoClient.getDatabase("ASPConfiguration");
			MongoCollection<Document> collection = database.getCollection("ASPConfigurationObjects");
			
			
			Document existingDoc = collection.find(eq("readerID", readerID)).first();
			
			ASPReaderConf conf = null;
			try {
				if (existingDoc!=null) {
					String json = existingDoc.toJson();
					conf = new ObjectMapper().readValue(json, ASPReaderConf.class);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			mongoClient.close();
			return conf;

	}
	
	
	public static void main(String[] args) {
		ASPConfigurationDB confDB = new ASPConfigurationDB("130.209.249.19");
		ASPModuleConf moduleConf = new ASPModuleConf("Z:\\SambaMounts\\tr.kba\\TRECIS\\ASP\\TestConf\\ASPWriter.module.conf");
		
		boolean storedOK = confDB.storeModuleConf(moduleConf);
		System.err.println(storedOK);
		ASPModuleConf readConf = confDB.getModuleConf(moduleConf.getModuleID());
		ObjectWriter jsonWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
		try {
			System.err.println(jsonWriter.writeValueAsString(readConf));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
