
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.SafetyDocument;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "safety_documents", path = "/documents/safety")
public interface SafetyDocumentsRepository extends MongoRepository<SafetyDocument, String>
{
	List<SafetyDocument> findBy_id(@Param("_id") String _id);
}
