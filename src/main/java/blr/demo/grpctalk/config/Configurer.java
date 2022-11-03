package blr.demo.grpctalk.config;

import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class Configurer {

    @Bean
    public GrpcChannelConfigurer executorConfigurer() {
        return (channelBuilder, name) -> {
            if (channelBuilder instanceof NettyChannelBuilder) {
                ((NettyChannelBuilder) channelBuilder)
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


}
