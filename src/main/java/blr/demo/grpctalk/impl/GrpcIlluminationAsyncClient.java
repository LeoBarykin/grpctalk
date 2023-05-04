package blr.demo.grpctalk.impl;

import blr.demo.grpctalk.ManageIlluminationGrpc;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

import static blr.demo.grpctalk.Illumination.*;

@Slf4j
@Component
public class GrpcIlluminationAsyncClient {

    @GrpcClient("iService")
    private ManageIlluminationGrpc.ManageIlluminationStub asyncStub;

    @SneakyThrows
    public void doWork() {
        var responseObserver = new StreamObserver<Report>(){

            @Override
            public void onNext(Report value) {
                log.info("Got report from server with status {}", value.getStatus());
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getLocalizedMessage());
            }

            @Override
            public void onCompleted() {
                log.info("END OF STREAM");
            }
        };

        var requestObserver = asyncStub.manageLantern(responseObserver);

        for (int i = 0; i < 16; i++) {
            log.info("Step: {}", i);
            requestObserver.onNext(SwitchLantern.newBuilder()
                            .addColour("GREEN")
                            .setFrequency(i*i/10)
                            .setId(999)
                            .setMode(i % 2 == 0
                                    ? Mode.BRIGHT
                                    : Mode.DIMMED)
                    .build());
            Thread.sleep(ThreadLocalRandom.current().nextLong(3000L));
        }

        requestObserver.onCompleted();

    }

}
