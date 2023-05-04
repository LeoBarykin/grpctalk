package blr.demo.grpctalk.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class Configurer {

    @Bean
    public GrpcChannelConfigurer executorConfigurer() {
        return (channelBuilder, name) -> {
            if (channelBuilder instanceof NettyChannelBuilder) {
                ((NettyChannelBuilder) channelBuilder)
                        //.defaultServiceConfig(getRetryingServiceConfig())
                        //.defaultServiceConfig(getHedgingServiceConfig())
                        .enableRetry()
                        .executor(Executors.newFixedThreadPool(100));
            }
        };
    }

    @GrpcGlobalClientInterceptor
    public ClientInterceptor noopClientInterceptor() {
        return new ClientInterceptor() {
            @Override
            public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                                       CallOptions callOptions, Channel channel) {
                return channel.newCall(methodDescriptor, callOptions);
            }
        };
    }

    private Map<String, ?> getRetryingServiceConfig() {
        return new Gson()
                .fromJson(new JsonReader(new InputStreamReader(Objects.requireNonNull(
                        this.getClass().getResourceAsStream("retrying.json")), UTF_8)), Map.class);
    }

    private Map<String, ?> getHedgingServiceConfig() {
        return new Gson()
                .fromJson(new JsonReader(new InputStreamReader(Objects.requireNonNull(
                        this.getClass().getResourceAsStream("hedging.json")), UTF_8)), Map.class);
    }

}
