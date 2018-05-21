
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.QuoteItemExtraCost;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "quote_extra_costs", path = "/quotes/resources/extra_costs")
public interface QuoteItemExtraCostRepository extends MongoRepository<QuoteItemExtraCost, String>
{
	List<QuoteItemExtraCost> findBy_id(@Param("_id") String _id);
}
