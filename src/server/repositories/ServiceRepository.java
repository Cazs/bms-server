
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.Service;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "services", path = "/services")
public interface ServiceRepository extends MongoRepository<Service, String>
{
	List<Service> findBy_id(@Param("_id") String _id);
}
