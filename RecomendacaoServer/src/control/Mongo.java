/**
 * 
 */
package control;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author Thiago
 *
 */
public class Mongo {

	public static final int BEACON = 1;
	public static final int TAG_NFC = 2;
	public static final int SENSOR = 3;

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private List<MongoCollection<Document>> mongoCollections;

	/**
	 * Construtor da classe
	 * 
	 * @param dbName
	 *            Nome do banco de dados que será manipulado
	 * @param collection
	 *            Coleção que será manipulada.
	 */
	public Mongo(String dbName, List<String> collections) {
		mongoClient = new MongoClient();
		mongoDatabase = mongoClient.getDatabase(dbName);
		mongoCollections = new ArrayList<>();

		for (String collection : collections) {
			mongoCollections.add(mongoDatabase.getCollection(collection));
		}
	}

	/************************************************
	 * MÉTODOS PRIVADOS *
	 ************************************************/
	private void verificaInstancias() {

		if (mongoClient == null || mongoDatabase == null || mongoCollections.size() == 0) {

			String collectionString = "";

			for (MongoCollection<Document> collection : mongoCollections) {
				collectionString.concat("|" + collection);
			}

			throw new NullPointerException("NullPointer\nVerifique se todos os parâmetros foram instanciados corretamente\nCliente: "
								+ mongoClient + "\tBanco:" + mongoDatabase + "\tColeções:" + collectionString);
		}
	}

	/************************************************
	 * MÉTODOS PÚBLICOS *
	 ************************************************/

	/**
	 * 
	 */
	public void fechaConexao() {
		verificaInstancias();
		
		mongoClient.close();
	}

	/**
	 * 
	 * @param documento
	 * @param collection
	 */
	public void insere(Document documento, String collection) {
		verificaInstancias();

		mongoCollections.get(findCollection(mongoCollections, collection)).insertOne(documento);
	}

	/**
	 * 
	 * @param documentos
	 * @param collection
	 */
	public void insere(List<Document> documentos, String collection) {
		verificaInstancias();

		mongoCollections.get(findCollection(mongoCollections, collection)).insertMany(documentos);
	}

	/**
	 * 
	 * @param chave
	 * @param valor
	 * @param collection
	 * @return
	 */
	public List<Document> procura(String chave, Object valor, String collection) {
		verificaInstancias();
		return procura(new Document(chave, valor), collection);
	}

	/**
	 * 
	 * @param documento
	 * @param collection
	 * @return
	 */
	public List<Document> procura(Document documento, String collection) {
		verificaInstancias();

		List<Document> documents = new ArrayList<>();

		FindIterable<Document> iterable = mongoCollections.get(findCollection(mongoCollections, collection)).find(documento);

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				documents.add(document);
			}
		});

		return documents;
	}

	/**
	 * 
	 * @param collection
	 * @return
	 */
	public List<Document> listaRegistros(String collection) {
		verificaInstancias();

		List<Document> documents = new ArrayList<>();

		FindIterable<Document> iterable = mongoCollections.get(findCollection(mongoCollections, collection)).find();

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				documents.add(document);
			}
		});

		return documents;
	}

	/**
	 * 
	 * @param documento
	 * @param collection
	 */
	public void remove(Document documento, String collection) {
		verificaInstancias();
		mongoCollections.get(findCollection(mongoCollections, collection)).deleteOne(documento);
	}

	/**
	 * 
	 * @param collection
	 */
	public void removeTodos(String collection) {
		verificaInstancias();
		mongoCollections.get(findCollection(mongoCollections, collection)).deleteMany(new Document());
	}

	/**
	 * 
	 * @param list
	 * @param collection
	 * @return
	 */
	public int findCollection(List<MongoCollection<Document>> list, String collection) {

		if (list == null || collection == null || collection == "") {
			return -1;
		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getNamespace().getCollectionName().equals(collection)) {
				return i;
			}
		}

		return -1;
	}

	/************************************************
	 * GETTERS E SETTERS *
	 ************************************************/

	/**
	 * @return the mongoClient
	 */
	public MongoClient getMongoClient() {
		return mongoClient;
	}

	/**
	 * @param mongoClient
	 *            the mongoClient to set
	 */
	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	/**
	 * @return the mongoDatabase
	 */
	public MongoDatabase getMongoDatabase() {
		return mongoDatabase;
	}

	/**
	 * @param mongoDatabase
	 *            the mongoDatabase to set
	 */
	public void setMongoDatabase(MongoDatabase mongoDatabase) {
		this.mongoDatabase = mongoDatabase;
	}

	public List<MongoCollection<Document>> getMongoCollections() {
		return mongoCollections;
	}

	public void setMongoCollections(List<MongoCollection<Document>> mongoCollections) {
		this.mongoCollections = mongoCollections;
	}

}
