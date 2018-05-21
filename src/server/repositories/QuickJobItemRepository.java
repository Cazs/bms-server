
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.QuickJobItem;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "quickjob_resources", path = "/quickjobs/resources")
public interface QuickJobItemRepository extends MongoRepository<QuickJobItem, String>
{
	List<QuickJobItem> findBy_id(@Param("_id") String _id);
}
