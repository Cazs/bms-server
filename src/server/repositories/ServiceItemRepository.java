
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.ResourceType;
import server.model.ServiceItem;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "service_items", path = "/services/items")
public interface ServiceItemRepository extends MongoRepository<ServiceItem, String>
{
	List<ServiceItem> findBy_id(@Param("_id") String _id);
}
