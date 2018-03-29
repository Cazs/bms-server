package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.Task;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "tasks", path = "/tasks")
public interface TaskRepository extends MongoRepository<Task, String>
{
    List<Task> findByCreator(@Param("creator") String creator);
}
