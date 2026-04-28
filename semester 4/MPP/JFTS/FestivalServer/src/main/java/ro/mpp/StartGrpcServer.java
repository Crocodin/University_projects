package ro.mpp;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ro.mpp.service.FestivalServiceGrpcImpl;

public class StartGrpcServer {
    public static void main(String[] args) throws Exception {
        // Same Spring XML wires up festivalService just like StartJsonServer
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-server.xml");
        ro.mpp.observer.IFestivalService festivalService = context.getBean("festivalCSService", ro.mpp.observer.IFestivalService.class);

        Server server = ServerBuilder
                .forPort(5555)
                .addService(new FestivalServiceGrpcImpl(festivalService))
                .build()
                .start();

        System.out.println("gRPC Server started on port 5555");
        server.awaitTermination();
    }
}