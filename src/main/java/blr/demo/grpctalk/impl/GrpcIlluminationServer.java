package blr.demo.grpctalk.impl;

import blr.demo.grpctalk.Illumination;
import blr.demo.grpctalk.ManageIlluminationGrpc;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.concurrent.ThreadLocalRandom;

@GrpcService
@Slf4j
public class GrpcIlluminationServer extends ManageIlluminationGrpc.ManageIlluminationImplBase {

    @Override
    public void updateLantern(Illumination.SwitchLantern request, StreamObserver<Illumination.Report> responseObserver) {
        log.info("Received update message");
        responseObserver.onNext(Illumination.Report.newBuilder()
                .setStatus(0)
                .setId(0)
                .build());
        responseObserver.onCompleted();
    }

    @SneakyThrows
    @Override
    public void monitorLantern(Illumination.SwitchLantern request, StreamObserver<Illumination.Report> responseObserver) {
        log.info("Monitoring lantern 10 times");
        for (int i = 0; i < 10; i++) {
            responseObserver.onNext(Illumination.Report.newBuilder()
                    .setStatus(i)
                    .setId(i)
                    .build());
            Thread.sleep(ThreadLocalRandom.current().nextLong(3000));
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Illumination.SwitchLantern> manageLantern(StreamObserver<Illumination.Report> responseObserver) {
        return new StreamObserver<>() {
            int counter = 0;
            @Override
            public void onNext(Illumination.SwitchLantern switchLantern) {
                counter++;
                log.info("Step {}, mode {}", switchLantern.getMode(), counter);
            }
            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getLocalizedMessage());
            }
            @Override
            public void onCompleted() {
                log.info("Whole operation completed");
                responseObserver.onNext(Illumination.Report.newBuilder()
                        .setId(999)
                        .setStatus(counter)
                        .build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<Illumination.SwitchLantern> handleLantern(StreamObserver<Illumination.Report> responseObserver) {
        return new StreamObserver<>() {
            int counter = 0;
            @SneakyThrows
            @Override
            public void onNext(Illumination.SwitchLantern switchLantern) {
                counter++;
                for (int i = 0; i < 10; i++) {
                    responseObserver.onNext(Illumination.Report.newBuilder()
                            .setStatus(i)
                            .setId(i)
                            .build());
                    Thread.sleep(ThreadLocalRandom.current().nextLong(3000));
                }
            }
            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getLocalizedMessage());
            }
            @Override
            public void onCompleted() {
                log.info("Whole operation completed");
                responseObserver.onNext(Illumination.Report.newBuilder()
                        .setId(999)
                        .setStatus(counter)
                        .build());
                responseObserver.onCompleted();
            }
        };
    }
}
