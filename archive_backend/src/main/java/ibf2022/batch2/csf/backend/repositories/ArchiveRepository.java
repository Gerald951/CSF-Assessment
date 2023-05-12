package ibf2022.batch2.csf.backend.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ArchiveRepository {

	@Autowired
	private MongoTemplate template;

	//TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public Boolean recordBundle(String bundleId, String date, String title, String name, String comments, String URL) {
		
		Document toInsert = new Document();
		toInsert.append("bundleId", bundleId).append("date", date).append("title", title).append("name", name)
				.append("cooments", comments).append("urls", URL);
		
		Document inserted = template.insert(toInsert, "image");

		if (inserted !=null) {
			return true;
		} else {
			return false;
		}
	}

	//TODO: Task 5

	// db.image.find({ bundleId : "eb356beb"})
	public String getBundleByBundleId(String bundleId) {
		Query query = Query.query(Criteria.where("bundleId").is(bundleId));

		Document result = template.findOne(query, Document.class, "image");

		if (result == null) {
			return null;
		} else {
			return result.toJson();
		}

		
	}

	//TODO: Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public Object getBundles(/* any number of parameters here */) {
		return null;
	}


}
