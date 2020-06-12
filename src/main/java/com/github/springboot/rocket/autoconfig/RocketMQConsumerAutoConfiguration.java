package com.github.springboot.rocket.autoconfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * 自动配置消费者
 * @author chengsp
 * @date 2020/5/29 16:16
 */
@Configurable
@Slf4j
@EnableConfigurationProperties(GlobalConfig.class)
@ConditionalOnBean({MessageListener.class})
public class RocketMQConsumerAutoConfiguration implements InitializingBean {
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private List<MessageListener> messageListeners;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<MessageListener> messageListenerSet = new HashSet<>(this.messageListeners);
        DefaultConsumerConfig global = globalConfig.getGlobal();
        if (global == null) {
            global = new DefaultConsumerConfig();
            globalConfig.setGlobal(global);
        }
        global.setDefaultValueIfNull();
        Map<String, ConsumerConfig> consumerConfigMap = globalConfig.getConsumer();
        for (Map.Entry<String, ConsumerConfig> entry : consumerConfigMap.entrySet()) {
            ConsumerConfig consumerConfig = entry.getValue();
            consumerConfig.mergeDefault(global);
            String consumerGroup = consumerConfig.getConsumerGroup();
            String namesrvAddr = globalConfig.getNamesrvAddr();
            String topics = consumerConfig.getTopics();
            if (StringUtils.isEmpty(consumerGroup)) {
                throw new RuntimeException("consumerGroup 不能为空");
            }
            if (StringUtils.isEmpty(namesrvAddr)) {
                throw new RuntimeException("namesrvAddr 不能为空");
            }
            if (StringUtils.isEmpty(topics)) {
                throw new RuntimeException("topics 不能为空");
            }

            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
            consumer.setNamesrvAddr(namesrvAddr);
            Object o = applicationContext.getBean(consumerConfig.getMessageListener());
            if (!messageListenerSet.contains(o)) {
                throw new RuntimeException("不存在名称为+" + consumerConfig.getMessageListener() + "的bean," +
                        "请在配置中为该消费者指定一个正确的MessageListener名称");
            }
            if (o instanceof MessageListenerConcurrently) {
                consumer.registerMessageListener(((MessageListenerConcurrently) o));
            } else if (o instanceof MessageListenerOrderly) {
                consumer.registerMessageListener(((MessageListenerOrderly) o));
            } else {
                throw new RuntimeException("未知的MessageListener:" + o.getClass().getSimpleName() + "");
            }
            consumer.setConsumeThreadMax(consumerConfig.getConsumeThreadMax());
            consumer.setConsumeThreadMin(consumerConfig.getConsumeThreadMin());
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            consumer.setConsumeMessageBatchMaxSize(consumerConfig.getConsumeMessageBatchMaxSize());
            if (DefaultConsumerConfig.DEFAULT_MESSAGE_MODEL.equals(consumerConfig.getMessageModel())) {
                consumer.setMessageModel(MessageModel.CLUSTERING);
            } else if (ConsumerConfig.MESSAGE_MODEL_BROADCASTING.equals(consumerConfig.getMessageModel())) {
                consumer.setMessageModel(MessageModel.BROADCASTING);
            } else {
                throw new RuntimeException("不存在这样的消费模式:" + consumerConfig.getMessageModel() + ",消费模式只能配置broadcast" +
                        "或者cluster");
            }
            String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0], topicTag[1]);
            }
            consumer.setPersistConsumerOffsetInterval(globalConfig.getPersistConsumerOffsetInterval());
            consumer.setPostSubscriptionWhenPull(consumerConfig.getPostSubscriptionWhenPull());
            consumer.setPullBatchSize(consumerConfig.getPullBatchSize());
            consumer.setPullInterval(consumerConfig.getPullInterval());
            consumer.setPullThresholdForQueue(consumerConfig.getPullThresholdForQueue());
            consumer.setUnitMode(consumerConfig.getUnitMode());
            consumer.setAdjustThreadPoolNumsThreshold(consumerConfig.getAdjustThreadPoolNumsThreshold());
            consumer.setClientCallbackExecutorThreads(globalConfig.getClientCallbackExecutorThreads());
            consumer.setConsumeConcurrentlyMaxSpan(consumerConfig.getConsumeConcurrentlyMaxSpan());
            consumer.setHeartbeatBrokerInterval(globalConfig.getHeartbeatBrokerInterval());
            consumer.setPollNameServerInteval(globalConfig.getPollNameServerInteval());
            String consumeFromWhere = consumerConfig.getConsumeFromWhere();
            if (consumeFromWhere != null) {
                if (!CONSUME_FROM_WHERE_MAP.containsKey(consumeFromWhere)) {
                    throw new RuntimeException("不存在这样的consumeFromWhere,该值必须为consume_from_last_offset," +
                            "consume_from_last_offset_and_from_min_when_boot_first," +
                            "consume_from_min_offset,consume_from_max_offset,consume_from_first_offset" +
                            "consume_from_timestamp其一");
                }
                consumer.setConsumeFromWhere(CONSUME_FROM_WHERE_MAP.get(consumeFromWhere));
            }
            consumer.start();
            log.info("{}-consumerConfig:{}", entry.getKey(), JSON.toJSONString(consumerConfig));
            log.info("MQ start success namesrvAddr:{},group:{},topics:{}", globalConfig.getNamesrvAddr(),
                    consumerGroup, topics);
        }
    }

    private static final Map<String, ConsumeFromWhere> CONSUME_FROM_WHERE_MAP = new HashMap<>();

    static {
        CONSUME_FROM_WHERE_MAP.put("consume_from_last_offset", ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        CONSUME_FROM_WHERE_MAP.put("consume_from_last_offset_and_from_min_when_boot_first", ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET_AND_FROM_MIN_WHEN_BOOT_FIRST);
        CONSUME_FROM_WHERE_MAP.put("consume_from_min_offset", ConsumeFromWhere.CONSUME_FROM_MIN_OFFSET);
        CONSUME_FROM_WHERE_MAP.put("consume_from_max_offset", ConsumeFromWhere.CONSUME_FROM_MAX_OFFSET);
        CONSUME_FROM_WHERE_MAP.put("consume_from_first_offset", ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        CONSUME_FROM_WHERE_MAP.put("consume_from_timestamp", ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
    }
}
