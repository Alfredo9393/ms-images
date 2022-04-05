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
import io.kubemq.sdk.event.Channel;
import io.kubemq.sdk.event.Event;
import io.kubemq.sdk.queue.Message;
import io.kubemq.sdk.queue.Queue;
import io.kubemq.sdk.queue.SendMessageResult;
import io.kubemq.sdk.tools.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
/**
 *
 * @author APerez
 */
@Service
public class ImagesServiceImpl implements ImagesService{

    @Autowired
    FireBaseClient fireBaseClient;
        
    private Channel channel;
       
    public ImagesServiceImpl(Channel channel) {
        this.channel = channel;
    }
        
    @Override
    public Object getImages(String idCommerce, String metadata) {
        
        Object object = null;
        try{
            
            ImagesModel imagesModel = new ImagesModel();
            imagesModel.setIdcommerce(idCommerce);            
            System.out.println("invoke http://IP:8090/imagesfirebase  idCommerce:"+idCommerce);
            object =fireBaseClient.imagesfirebase(imagesModel);
            System.out.println("Response invoke http://IP:8090/imagesfirebase "+objectToJson(object));

         
        } catch(Exception ex){
            System.out.println("invoke http://IP:8090/imagesfirebase: "+ex);
        }
        
        publishResultImages(object, metadata);
           
        return object;
    }

    @Override
    public void publishResultImages(Object object, String metadata) {
//        Event event = new Event();
//        event.setEventId(getid());
//
//        try {
//            
//            System.out.println("set body: [chanel-images-response]");
//            event.setBody(Converter.ToByteArray(object));
//        } catch (IOException e) {
//            System.out.println("Error publish [chanel-images-response]");
//            System.out.println(e);
//        }
//
//        try {
//            System.out.println("publish Message in [chanel-images-response]");
//            channel.SendEvent(event); 
//        } catch (SSLException | ServerAddressNotSuppliedException e) {
//            System.out.println("Error publish [chanel-images-response]");
//            System.out.println(e);
//        }
    	
    	try {
    		Queue queue = new Queue("chanel-images-response", "chanel-images-response", "localhost:50000");
        	//System.out.println("Sending: {}", idCommerce);
    		Message message = new Message();
        	message.setBody(Converter.ToByteArray(object));
        	message.setMetadata(metadata);
//        	LOG.info("MessageID: %d, Body: %s", message.getMessageID(),
//        	          Converter.FromByteArray(message.getMessage().getBody()));
            final SendMessageResult result = queue.SendQueueMessage(message);

        } catch (ServerAddressNotSuppliedException | IOException e) {

        }
    }
    
    
    
    private static String objectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
    
    private static String getid() {
        int min = 10;  
        int max = 900;    
        Integer res1 = (int) (Math.random()*(max-min+1)+min);   
        String res = res1.toString();
        return res;
    }

}
