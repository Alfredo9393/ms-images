/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalplay.images.client;

import com.totalplay.images.model.ImagesModel;
import org.springframework.stereotype.Component;

/**
 *
 * @author APerez
 */
@Component
public class FallbackFireBaseClient implements FireBaseClient{

    @Override
    public Object imagesfirebase(ImagesModel object) {
        System.out.println("Fallback FallbackFireBaseClient");
        return new Object();
        
    }
    
}
