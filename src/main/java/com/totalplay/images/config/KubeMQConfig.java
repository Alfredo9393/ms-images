/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.totalplay.images.config;

import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import io.kubemq.sdk.event.Channel;
import io.kubemq.sdk.event.Subscriber;
import io.kubemq.sdk.queue.Queue;
import javax.net.ssl.SSLException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author APerez
 */
@Configuration
@ConfigurationProperties("kubemq")
public class KubeMQConfig {
    private String address;

    @Bean
    public Subscriber subscriber() {
        return new Subscriber(address);
    }

    @Bean
    public Channel channel() {
        return new Channel("chanel-images-response", "client-images-response", true, address);
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        System.out.println("addres: "+address);
        this.address = address;
    }
}