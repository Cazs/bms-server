
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.QuoteItem;
import server.model.QuoteService;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "quote_services", path = "/quotes/services")
public interface QuoteServiceRepository extends MongoRepository<QuoteService, String>
{
	List<QuoteService> findBy_id(@Param("_id") String _id);
}
