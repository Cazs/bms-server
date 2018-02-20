package server;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import server.auxilary.RemoteComms;
import server.model.*;

import java.net.UnknownHostException;

@Configuration
public class AppConfig  extends RepositoryRestConfigurerAdapter
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

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config)
    {
        config.exposeIdsFor(Employee.class);
        config.exposeIdsFor(Client.class);
        config.exposeIdsFor(Supplier.class);
        config.exposeIdsFor(Asset.class);
        config.exposeIdsFor(Resource.class);
        config.exposeIdsFor(ResourceType.class);
        config.exposeIdsFor(Requisition.class);
        config.exposeIdsFor(Quote.class);
        config.exposeIdsFor(QuoteItem.class);
        config.exposeIdsFor(QuoteService.class);
        config.exposeIdsFor(Job.class);
        config.exposeIdsFor(JobEmployee.class);
        config.exposeIdsFor(Invoice.class);
        config.exposeIdsFor(Revenue.class);
        config.exposeIdsFor(Expense.class);
        config.exposeIdsFor(PurchaseOrder.class);
        config.exposeIdsFor(PurchaseOrderResource.class);
        config.exposeIdsFor(PurchaseOrderAsset.class);
        config.exposeIdsFor(Leave.class);
        config.exposeIdsFor(Overtime.class);
        config.exposeIdsFor(Service.class);
        config.exposeIdsFor(ServiceItem.class);
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
