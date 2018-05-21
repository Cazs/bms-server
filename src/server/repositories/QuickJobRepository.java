
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.QuickJob;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "quickjobs", path = "/quickjobs")
public interface QuickJobRepository extends MongoRepository<QuickJob, String>
{
	List<QuickJob> findBy_id(@Param("_id") String _id);
}
