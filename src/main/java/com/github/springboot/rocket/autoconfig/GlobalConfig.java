package com.github.springboot.rocket.autoconfig;

import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.remoting.common.RemotingUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局配置
 * @author chengsp
 * @date 2020/5/29 14:54
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = GlobalConfig.CONFIG_PREFIX)
public class GlobalConfig {
    public static final String CONFIG_PREFIX = "spring.rocket.consumer";
    public static final int DEFAULT_POLL_NAME_SERVER_INTEVAL = 1000 * 30;
    public static final int DEFAULT_HEARTBEAT_BROKER_INTERVAL = 1000 * 30;
    public static final int DEFAULT_PERSIST_CONSUMER_OFFSET_INTERVAL = 1000 * 5;
    private String namesrvAddr = System.getProperty(MixAll.NAMESRV_ADDR_PROPERTY, System.getenv(MixAll.NAMESRV_ADDR_ENV));

    private String clientIP = RemotingUtil.getLocalAddress();

    private String instanceName = System.getProperty("rocketmq.client.name", "DEFAULT");

    private Integer clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();
    /**
     * Pulling topic information interval from the named server
     */
    private Integer pollNameServerInteval = DEFAULT_POLL_NAME_SERVER_INTEVAL;
    /**
     * Heartbeat interval in microseconds with message broker
     */
    private Integer heartbeatBrokerInterval = DEFAULT_HEARTBEAT_BROKER_INTERVAL;
    /**
     * Offset persistent interval for consumer
     */
    private Integer persistConsumerOffsetInterval = DEFAULT_PERSIST_CONSUMER_OFFSET_INTERVAL;
    /**
     * 每个消费者的独立配置
     */
    private Map<String, ConsumerConfig> consumer = new LinkedHashMap<>();
    /**
     * 所有消费者额全局配置,可以被消费者配置覆盖
     */
    @NestedConfigurationProperty
    private DefaultConsumerConfig global;
}
