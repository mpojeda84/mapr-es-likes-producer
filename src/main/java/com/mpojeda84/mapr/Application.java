package com.mpojeda84.mapr;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Properties;
import java.util.Scanner;

public class Application {

    private static Scanner in;

    private static String TOPIC = "/user/mapr/Streams/test2:topic-2";

    public static void main(String[] argv)throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        KafkaProducer<String, String> producer = getProducer();


        for (int i = 0; i < 1000; i++) {
            Simulator.createNewUser();
        }

        int times = 5000;
        while (times != 0) {
            Simulator.moveUsers();
            for (int i = 0; i < 20; i++) {
                Like like = Simulator.getLike();
                ProducerRecord<String, String> rec = new ProducerRecord<>(TOPIC,objectMapper.writeValueAsString(like));
                producer.send(rec);
            }
            Thread.sleep(5000);
            times --;
        }


        producer.close();
    }

    private static KafkaProducer<String, String> getProducer() {
        Properties props = new Properties();
        props.setProperty("batch.size", "16384");
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("block.on.buffer.full", "true");

        return new KafkaProducer<>(props);
    }
}
