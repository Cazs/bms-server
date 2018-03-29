
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.TaskItem;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "task_resources", path = "/task/resources")
public interface TaskItemRepository extends MongoRepository<TaskItem, String>
{
	List<TaskItem> findBy_id(@Param("_id") String _id);
}
