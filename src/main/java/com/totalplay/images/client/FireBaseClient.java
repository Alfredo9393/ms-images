/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalplay.images.client;

import com.totalplay.images.model.ImagesModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author APerez
 */
@FeignClient(name = "ms-imagesfirebase", 
//        url="http://10.214.9.143:8090"  
        url="http://localhost:8090"
,fallback = FallbackFireBaseClient.class )
public interface FireBaseClient {
        @RequestMapping(
            value = "/storequery", //imagesfirebase
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
        Object imagesfirebase(@RequestBody(required = true) ImagesModel object );
}
