/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalplay.images.service;
import com.google.gson.Gson;
import com.totalplay.images.client.FireBaseClient;
import com.totalplay.images.model.ImagesModel;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.Event;
import io.kubemq.sdk.tools.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 *
 * @author APerez
 */
@Service
public class ImagesServiceImpl implements ImagesService{

    @Autowired
    FireBaseClient fireBaseClient;
        
    @Override
    public Object getImages(String idCommerce) {
        
        Object object = null;
        try{
            
            ImagesModel imagesModel = new ImagesModel();
            imagesModel.setIdcommerce(idCommerce);            
            System.out.println("invoke http://IP:8090/imagesfirebase  idCommerce:"+idCommerce);
            object =fireBaseClient.imagesfirebase(imagesModel);
            System.out.println("Response invoke http://IP:8090/imagesfirebase "+objectToJson(object));

             publishResultImages(object);
        } catch(Exception ex){
            System.out.println("invoke http://IP:8090/imagesfirebase: "+ex);
        }
        return object;
    }

    @Override
    public void publishResultImages(Object object) {
        String channelName = "chanel-images-response", clientID = "client-images-response", kubeMQAddress = "kubemq-cluster-grpc:50000";
        io.kubemq.sdk.event.Channel chan = new io.kubemq.sdk.event.Channel(channelName, clientID, false, kubeMQAddress);
        Event event = new Event();
        try {
            event.setBody(Converter.ToByteArray(object));
        } catch (IOException e) {
            System.out.println("Error publish Message");
            System.out.println(e);
        }

        try {
            System.out.println("publish Message in chanel-images");
            chan.SendEvent(event);
        } catch (SSLException | ServerAddressNotSuppliedException e) {
            System.out.println("Error publish Message");
            System.out.println(e);
        }
    }
    
    private static String objectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
    

}
