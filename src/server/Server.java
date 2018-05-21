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
        IO.initEnv();
        SpringApplication.run(Server.class, args);
    }
}