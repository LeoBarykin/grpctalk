package blr.demo.grpctalk.impl;

import blr.demo.grpctalk.Illumination;
import blr.demo.grpctalk.ManageIlluminationGrpc;
import io.grpc.Deadline;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GrpcIlluminationBlockingClient {

    @GrpcClient("iService")
    private ManageIlluminationGrpc.ManageIlluminationBlockingStub blockingStub;

    public void doWork() {
        for (int i = 0; i < 35; i++) {

            Illumination.Report report =
                    blockingStub
                            .withDeadline(Deadline.after(5, TimeUnit.SECONDS))
                            .updateLantern(Illumination.SwitchLantern.newBuilder().build());
            log.info("Id of the lantern = {}", report.getId());

        }
//        Iterator<Illumination.Report> reportIterator =
//                blockingStub.monitorLantern(Illumination.SwitchLantern.newBuilder().build());
//        reportIterator.forEachRemaining(l -> log.info("Current status = {}", l.getStatus()));

    }

}
