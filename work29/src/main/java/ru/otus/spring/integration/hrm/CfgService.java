package ru.otus.spring.integration.hrm;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.SubscribableChannel;
import ru.otus.spring.integration.domain.HrInfo;

@Slf4j
@Configuration
public class CfgService {

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate( 1000 ).maxMessagesPerPoll( 1 ).get();
    }

    @Bean
    public SubscribableChannel zone1Channel() {
        return MessageChannels.publishSubscribe().get();
    }
    @Bean
    public SubscribableChannel zone2Channel() {
        return MessageChannels.publishSubscribe().get();
    }
    @Bean
    public SubscribableChannel zone3Channel() {
        return MessageChannels.publishSubscribe().get();
    }
    @Bean
    public SubscribableChannel zone4Channel() {
        return MessageChannels.publishSubscribe().get();
    }
    @Bean
    public SubscribableChannel zone5Channel() {
        return MessageChannels.publishSubscribe().get();
    }
    @Bean
    public SubscribableChannel direct1Channel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow hrmFlow() {
        return IntegrationFlows.from("hrmChannel")
                .handle( "hrmService", "process" )
                .transform(transformFilter()).handle( "hrmService", "filter" )
                .wireTap("analyseChannel")
                .<HrInfo, Integer> route(HrInfo::getZone,
                        mapping -> mapping
                                .subFlowMapping(1, subflow -> subflow.channel("zone1Channel"))
                                .subFlowMapping(2, subflow -> subflow.channel("zone2Channel"))
                                .subFlowMapping(3, subflow -> subflow.channel("zone3Channel"))
                                .subFlowMapping(4, subflow -> subflow.channel("zone4Channel"))
                                .subFlowMapping(5, subflow -> subflow.channel("zone5Channel"))
                )
                .get();
    }

    @Bean
    public IntegrationFlow hrmFlowBack1() {
        return IntegrationFlows.from("zone1Channel")
                .handle("zonesService", "zone1")
                .channel("backChannel")
                .get();
    }

    @Bean
    public IntegrationFlow hrmFlowBack2() {
        return IntegrationFlows.from("zone2Channel")
                .handle("zonesService", "zone2")
                .channel("backChannel")
                .get();
    }

    @Bean
    public IntegrationFlow hrmFlowBack3() {
        return IntegrationFlows.from("zone3Channel")
                .handle("zonesService", "zone3")
                .channel("backChannel")
                .get();
    }

    @Bean
    public IntegrationFlow hrmFlowBack4() {
        return IntegrationFlows.from("zone4Channel")
                .handle("zonesService", "zone4")
                .channel("backChannel")
                .get();
    }

    @Bean
    public IntegrationFlow hrmFlowBack5() {
        return IntegrationFlows.from("zone5Channel")
                .handle("zonesService", "zone5")
                .channel("backChannel")
                .get();
    }

    @Bean
    public IntegrationFlow hrmFlowAnalyse() {
        return IntegrationFlows.from("analyseChannel")
                .channel("direct1Channel")
                .get();
    }

    @Bean
    public GenericTransformer<HrInfo, HrInfo> transformFilter() {
        return source -> {
            val diff = Math.abs(Math.abs(source.getPrevHr()) - Math.abs(source.getHr()));
            // filter hr noise
            if(diff >= 10) {
                source.setHr(source.getPrevHr());
                source.setPrevHr(null);
            }
            return source;
        };
    }

}

