package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import server.auxilary.IO;
import server.auxilary.RemoteComms;

import java.io.IOException;

@SpringBootApplication
public class Server
{
    public static void main(String[] args)
    {
        try
        {
            String ip = IO.readAttributeFromConfig("SERVER_IP");
            String port = IO.readAttributeFromConfig("SERVER_PORT");
            String ttl = IO.readAttributeFromConfig("TTL");
            String db_ip = IO.readAttributeFromConfig("DB_IP");
            String db_port = IO.readAttributeFromConfig("DB_PORT");
            String db_name = IO.readAttributeFromConfig("DB_NAME");
            String system_email = IO.readAttributeFromConfig("SYSTEM_EMAIL");

            if(ip!=null && port!=null)
            {
                RemoteComms.host = "http://" + ip + ":" + port;
                IO.log(Server.class.getName(), IO.TAG_INFO, "setting host to: " + RemoteComms.host);
            } else IO.log(Server.class.getName(), IO.TAG_WARN, "attributes SERVER_IP and/or SERVER_PORT are not set in the config file.");

            if(db_ip!=null)
            {
                RemoteComms.DB_IP = db_ip;
                IO.log(Server.class.getName(), IO.TAG_INFO, "setting DB_IP to: " + db_ip);
            } else IO.log(Server.class.getName(), IO.TAG_WARN, "DB_IP attribute is not set in the config file.");

            if(db_port!=null)
            {
                RemoteComms.DB_PORT = Integer.parseInt(db_port);
                IO.log(Server.class.getName(), IO.TAG_INFO, "setting DB_PORT to: " + db_port);
            } else IO.log(Server.class.getName(), IO.TAG_WARN, "DB_PORT attribute is not set in the config file.");

            if(db_name!=null)
            {
                RemoteComms.DB_NAME = db_name;
                IO.log(Server.class.getName(), IO.TAG_INFO, "setting DB_NAME to: " + db_name);
            } else IO.log(Server.class.getName(), IO.TAG_WARN, "DB_NAME attribute is not set in the config file.");

            if(ttl!=null)
            {
                RemoteComms.TTL = Integer.parseInt(ttl);
                IO.log(Server.class.getName(), IO.TAG_INFO, "setting TTL to: " + ttl);
            } else IO.log(Server.class.getName(), IO.TAG_WARN, "TTL attribute is not set in the config file.");

            if(system_email!=null)
            {
                RemoteComms.SYSTEM_EMAIL = system_email;
                IO.log(Server.class.getName(), IO.TAG_INFO, "setting SYSTEM_EMAIL to: " + system_email);
            } else IO.log(Server.class.getName(), IO.TAG_WARN, "SYSTEM_EMAIL attribute is not set in the config file.");

            SpringApplication.run(Server.class, args);
        } catch (IOException e)
        {
            IO.log(Server.class.getName(), IO.TAG_ERROR, e.getMessage());
        }
    }
}