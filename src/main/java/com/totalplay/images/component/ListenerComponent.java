/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalplay.images.component;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;


import com.totalplay.images.service.ImagesService;
import io.grpc.stub.StreamObserver;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.EventReceive;
import io.kubemq.sdk.event.Subscriber;
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
        
    @PostConstruct
    public void init() {                
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setChannel("chanel-images-request");
        subscribeRequest.setClientID("client-images-request");
        subscribeRequest.setSubscribeType(SubscribeType.EventsStore);
        subscribeRequest.setEventsStoreType(EventsStoreType.StartNewOnly);
        try {
            LOG.info("subscriber: chanel-images-request ");
            subscriber.SubscribeToEvents(subscribeRequest, this);
        } catch (ServerAddressNotSuppliedException | SSLException e) {
            LOG.info("Error subscriber: chanel-images-request "+e);
        }
    }
    
    @Override
    public void onNext(EventReceive eventReceive) {
        LOG.info("  *********** Event: chanel-images-request ***********    ");
        try {
            LOG.info("Body: %s {} ", Converter.FromByteArray(eventReceive.getBody()));
            String idcommerce =(String)Converter.FromByteArray(eventReceive.getBody());
            LOG.info("idcommerce idcommerce: "+idcommerce );
            imagesService.getImages(idcommerce);
        } catch (IOException | ClassNotFoundException e) {
            LOG.error("Error EventReceive [chanel-images-request]", e);
        }

    }

    @Override
    public void onError(Throwable thrwbl) {
    }

    @Override
    public void onCompleted() {
    }
    

    
}
