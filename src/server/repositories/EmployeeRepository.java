
package server.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import server.model.Employee;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "employees", path = "/employees")
public interface EmployeeRepository extends MongoRepository<Employee, String>
{
	List<Employee> findByLastname(@Param("lastname") String lastname);
}
