package com.github.springboot.rocket.autoconfig;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消费者默认配置
 * @author chengsp
 * @date 2020/6/8 17:00
 */
@Data
@Accessors(chain = true)
public class DefaultConsumerConfig {
    public static final int DEFAULT_CONSUME_THREAD_MIN = 20;
    public static final int DEFAULT_CONSUME_THREAD_MAX = 64;
    public static final long DEFAULT_ADJUST_THREAD_POOL_NUMS_THRESHOLD = 100000L;
    public static final int DEFAULT_CONSUME_CONCURRENTLY_MAX_SPAN = 2000;
    public static final int DEFAULT_PULL_THRESHOLD_FOR_QUEUE = 1000;
    public static final long DEFAULT_PULL_INTERVAL = 0L;
    public static final int DEFAULT_CONSUME_MESSAGE_BATCH_MAX_SIZE = 1;
    public static final int DEFAULT_PULL_BATCH_SIZE = 32;
    public static final boolean DEFAULT_POST_SUBSCRIPTION_WHEN_PULL = false;
    public static final boolean DEFAULT_UNIT_MODE = false;
    public static final String DEFAULT_CONSUME_FROM_WHERE = "consume_from_last_offset";

    public static final String DEFAULT_MESSAGE_MODEL = "cluster";
    /**
     * Minimum consumer thread number
     */
    private Integer consumeThreadMin;
    /**
     * Max consumer thread number
     */
    private Integer consumeThreadMax;

    /**
     * Threshold for dynamic adjustment of the number of thread pool
     */
    private Long adjustThreadPoolNumsThreshold;

    /**
     * Concurrently max span offset.it has no effect on sequential consumption
     */
    private Integer consumeConcurrentlyMaxSpan;
    /**
     * Flow control threshold
     */
    private Integer pullThresholdForQueue;
    /**
     * Message pull Interval
     */
    private Long pullInterval;
    /**
     * Batch consumption size
     */
    private Integer consumeMessageBatchMaxSize;
    /**
     * Batch pull size
     */
    private Integer pullBatchSize;

    /**
     * Whether update subscription relationship when every pull
     */
    private Boolean postSubscriptionWhenPull;

    /**
     * Whether the unit of subscription group
     */
    private Boolean unitMode;

    /**
     * Consumption offset
     */
    private String consumeFromWhere ;

    /**
     * Consumption pattern,default is clustering
     */
    private String messageModel;

    /**
     * 如果spring注入后的字段为空,则给该字段赋默认值
     */
    public void setDefaultValueIfNull(){
        if (getConsumeThreadMin() == null) {
            setConsumeThreadMin(DEFAULT_CONSUME_THREAD_MIN);
        }
        if (getConsumeThreadMax() == null) {
            setConsumeThreadMax(DEFAULT_CONSUME_THREAD_MAX);
        }
        if (getAdjustThreadPoolNumsThreshold() == null) {
            setAdjustThreadPoolNumsThreshold(DEFAULT_ADJUST_THREAD_POOL_NUMS_THRESHOLD);
        }
        if (getConsumeConcurrentlyMaxSpan() == null) {
            setConsumeConcurrentlyMaxSpan(DEFAULT_CONSUME_CONCURRENTLY_MAX_SPAN);
        }
        if (getPullThresholdForQueue() == null) {
            setPullThresholdForQueue(DEFAULT_PULL_THRESHOLD_FOR_QUEUE);
        }
        if (getPullInterval() == null) {
            setPullInterval(DEFAULT_PULL_INTERVAL);
        }
        if (getConsumeMessageBatchMaxSize() == null) {
            setConsumeMessageBatchMaxSize(DEFAULT_CONSUME_MESSAGE_BATCH_MAX_SIZE);
        }
        if (getPullBatchSize() == null) {
            setPullBatchSize(DEFAULT_PULL_BATCH_SIZE);
        }
        if (getPostSubscriptionWhenPull() == null) {
            setPostSubscriptionWhenPull(DEFAULT_POST_SUBSCRIPTION_WHEN_PULL);
        }
        if (getUnitMode() == null) {
            setUnitMode((DEFAULT_UNIT_MODE));
        }
        if (getConsumeFromWhere() == null) {
            setConsumeFromWhere(DEFAULT_CONSUME_FROM_WHERE);
        }
        if (getMessageModel() == null) {
            setMessageModel(DEFAULT_MESSAGE_MODEL);
        }
    }
}
