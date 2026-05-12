package ro.mpp;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ro.mpp.service.FestivalServiceGrpcImpl;

@SpringBootApplication(scanBasePackages = "ro.mpp")
public class StartGrpcServer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StartGrpcServer.class, args);
    }

    @Bean
    public Server grpcServer(ro.mpp.observer.IFestivalService festivalService) throws Exception {
        Server server = ServerBuilder
                .forPort(5555)
                .addService(new FestivalServiceGrpcImpl(festivalService))
                .build()
                .start();
        System.out.println("gRPC Server started on port 5555");
        return server;
    }
}