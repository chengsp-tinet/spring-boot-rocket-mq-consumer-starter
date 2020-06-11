package com.github.springboot.rocket.autoconfig;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者独立配置
 * @author chengsp
 * @date 2020/5/29 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Accessors(chain = true)
public class ConsumerConfig extends DefaultConsumerConfig {
    public static final String MESSAGE_MODEL_BROADCASTING = "broadcast";
    /**
     * Do the same thing for the same Group, the application must be set,and
     * guarantee Globally unique
     */
    private String consumerGroup;
    /**
     * topics
     */
    private String topics;

    /**
     * Message listener
     */
    private String messageListener;

    /**
     * 将默认的配置合并到当前配置中
     *
     * @param defaultConfig
     */
    public void mergeDefault(DefaultConsumerConfig defaultConfig) {
        if (defaultConfig == null) {
            return;
        }
        if (getConsumeThreadMin() == null) {
            setConsumeThreadMin(defaultConfig.getConsumeThreadMin());
        }
        if (getConsumeThreadMax() == null) {
            setConsumeThreadMax(defaultConfig.getConsumeThreadMax());
        }
        if (getAdjustThreadPoolNumsThreshold() == null) {
            setAdjustThreadPoolNumsThreshold(defaultConfig.getAdjustThreadPoolNumsThreshold());
        }
        if (getConsumeConcurrentlyMaxSpan() == null) {
            setConsumeConcurrentlyMaxSpan(defaultConfig.getConsumeConcurrentlyMaxSpan());
        }
        if (getPullThresholdForQueue() == null) {
            setPullThresholdForQueue(defaultConfig.getPullThresholdForQueue());
        }
        if (getPullInterval() == null) {
            setPullInterval(defaultConfig.getPullInterval());
        }
        if (getConsumeMessageBatchMaxSize() == null) {
            setConsumeMessageBatchMaxSize(defaultConfig.getConsumeMessageBatchMaxSize());
        }
        if (getPullBatchSize() == null) {
            setPullBatchSize(defaultConfig.getPullBatchSize());
        }
        if (getPostSubscriptionWhenPull() == null) {
            setPostSubscriptionWhenPull(defaultConfig.getPostSubscriptionWhenPull());
        }
        if (getUnitMode() == null) {
            setUnitMode((defaultConfig.getUnitMode()));
        }
        if (getConsumeFromWhere() == null) {
            setConsumeFromWhere(defaultConfig.getConsumeFromWhere());
        }
        if (getMessageModel() == null) {
            setMessageModel(defaultConfig.getMessageModel());
        }
    }
}
