package blr.demo.grpctalk.config;

import blr.demo.grpctalk.impl.GrpcIlluminationAsyncClient;
import blr.demo.grpctalk.impl.GrpcIlluminationBlockingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@Slf4j
public class ScheduledConfig {

    @Autowired
    private GrpcIlluminationBlockingClient grpcIlluminationBlockingClient;

    @Autowired
    private GrpcIlluminationAsyncClient grpcIlluminationAsyncClient;

    @Value("${isAsync}")
    private boolean isAsync;


    @Scheduled(fixedDelay = 30000)
    public void runExample() {
        if (isAsync) {
            log.info("ASYNC EXAMPLE");
            grpcIlluminationAsyncClient.doWork();
        } else {
            log.info("SYNC EXAMPLE");
            grpcIlluminationBlockingClient.doWork();
        }
    }

}
