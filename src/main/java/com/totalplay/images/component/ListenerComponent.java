/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalplay.images.component;

import com.google.gson.Gson;
import com.totalplay.images.service.ImagesService;
import io.grpc.stub.StreamObserver;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.EventReceive;
import io.kubemq.sdk.event.Subscriber;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.Transaction;
import io.kubemq.sdk.queue.TransactionMessagesResponse;
import io.kubemq.sdk.subscription.EventsStoreType;
import io.kubemq.sdk.subscription.SubscribeRequest;
import io.kubemq.sdk.subscription.SubscribeType;
import io.kubemq.sdk.tools.Converter;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;


/**
 *
 * @author APerez
 */
@Component
public class ListenerComponent implements StreamObserver<EventReceive> {
    private static final Logger LOG = LoggerFactory.getLogger(ListenerComponent.class);

    private Subscriber subscriber;
    
    @Autowired
    private ImagesService imagesService;
    private Queue queue;
    private TaskExecutor taskExecutor;
    
    public ListenerComponent(Subscriber subscriber, ImagesService imagesService, TaskExecutor taskExecutor, Queue queue) {
        this.subscriber = subscriber;
        this.imagesService = imagesService;
        this.taskExecutor = taskExecutor;
        this.queue = queue;
    }
    
    @PostConstruct
    public void init() {                
//        SubscribeRequest subscribeRequest = new SubscribeRequest();
//        subscribeRequest.setChannel("chanel-images-request");
//        subscribeRequest.setClientID("client-images-request");
//        subscribeRequest.setSubscribeType(SubscribeType.EventsStore);
//        subscribeRequest.setEventsStoreType(EventsStoreType.StartNewOnly);
//        try {
//            LOG.info("subscriber: chanel-images-request "+objectToJson(subscribeRequest));
//            subscriber.SubscribeToEvents(subscribeRequest, this);
//        } catch (ServerAddressNotSuppliedException | SSLException e) {
//            LOG.info("Error subscriber: chanel-images-request ");
//            System.out.println(e);
//        }
    }
    
    @Override
    public void onNext(EventReceive eventReceive) {
//        LOG.info("  *********** Event: [chanel-images-request] ***********    ");
//        try {
//            LOG.info("Body: %s {} ", Converter.FromByteArray(eventReceive.getBody()));
//            String idcommerce =(String)Converter.FromByteArray(eventReceive.getBody());
//            LOG.info("idcommerce idcommerce: "+idcommerce );
//            imagesService.getImages(idcommerce);
//        } catch (IOException | ClassNotFoundException e) {
//            LOG.error("Error EventReceive [chanel-images-request]", e);
//        }

    }
    
    @PostConstruct
	public void listen() {
		taskExecutor.execute(() -> {
			while (true) {
			    try {
                    Transaction transaction = queue.CreateTransaction();
                    TransactionMessagesResponse response = transaction.Receive(10, 10);
                    if (response.getMessage().getBody().length > 0) {
                        String idcommerce = (String) Converter.FromByteArray(response.getMessage().getBody());
                        LOG.info("Processed: {}", idcommerce);
                        imagesService.getImages(idcommerce, response.getMessage().getMetadata());
                        transaction.AckMessage();
//                        Event event = new Event();
//                        event.setEventId(response.getMessage().getMessageID());
//                        event.setBody(Converter.ToByteArray(order));
//						LOGGER.info("Sending event: id={}", event.getEventId());
//                        channel.SendEvent(event);

                            //transaction.RejectMessage();
                    }
//                    Thread.sleep(10000);
                } catch (Exception e) {
					LOG.error("Error", e);
                }
			}
		});

	}

    @Override
    public void onError(Throwable thrwbl) {
    }

    @Override
    public void onCompleted() {
    }
    
    private static String objectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
    
}
