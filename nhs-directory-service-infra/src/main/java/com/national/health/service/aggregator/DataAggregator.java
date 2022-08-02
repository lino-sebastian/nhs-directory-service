package com.national.health.service.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;

/**
 * DataAggregator class implementing Camel Aggregation strategy
 */
public class DataAggregator implements AggregationStrategy {

    /**
     * Method processing two camel Exchanges for aggregating split data read from CSV to one exchange
     *
     * @param oldExchange :   Camel oldExchange
     * @param newExchange :   Camel newExchange
     * @return {@link Exchange}
     */
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Message newExchangeMessage = newExchange.getIn();
        Object newExchangeMessageBody = newExchangeMessage.getBody();
        ArrayList<Object> exchangeDataList;

        if (oldExchange == null) {
            exchangeDataList = new ArrayList<>();
            exchangeDataList.add(newExchangeMessageBody);
            newExchangeMessage.setBody(exchangeDataList);
            return newExchange;
        } else {
            Message oldExchangeMessage = oldExchange.getIn();
            exchangeDataList = oldExchangeMessage.getBody(ArrayList.class);
            exchangeDataList.add(newExchangeMessageBody);
            return oldExchange;
        }
    }
}
