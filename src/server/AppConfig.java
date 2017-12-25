package server;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import server.auxilary.RemoteComms;
import server.model.Employee;

import java.net.UnknownHostException;

@Configuration
public class AppConfig
{
    @Bean
    public MongoDbFactory mongoDbFactory()
    {
        return new SimpleMongoDbFactory(new MongoClient(RemoteComms.DB_IP, RemoteComms.DB_PORT), RemoteComms.DB_NAME);
    }

    @Bean
    public MongoOperations mongoOperations() throws UnknownHostException
    {
        return new MongoTemplate(mongoDbFactory());
    }

    /*@Bean
    public Mongo mongo() throws Exception
    {
        return new Mongo("localhost");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception
    {
        return new MongoTemplate(mongo(), "bms-server");
    }*/
}
