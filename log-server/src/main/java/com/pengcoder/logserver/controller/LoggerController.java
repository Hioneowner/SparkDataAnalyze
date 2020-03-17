package com.pengcoder.logserver.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggerController {
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    public static final Logger logger = LoggerFactory.getLogger(LoggerController.class);


    @PostMapping("/log")
    public String dolog(@RequestParam("log") String log) {




//        System.out.println(log);


        JSONObject jsonObject = JSON.parseObject(log);
        jsonObject.put("ts",System.currentTimeMillis());

        logger.info(jsonObject.toJSONString());
        if (jsonObject.get("type").equals("startup")) {
            kafkaTemplate.send(Constant.KAFKA_TOPIC_STARTUP,jsonObject.toJSONString());
        }else {
            kafkaTemplate.send(Constant.KAFKA_TOPIC_EVENT,jsonObject.toJSONString());
        }

        return "success";
    }

}
