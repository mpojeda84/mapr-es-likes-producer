package com.mpojeda84.mapr;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Properties;
import java.util.Scanner;

public class Application {

    private static Scanner in;

    private static String TOPIC = "/user/mapr/Streams/test:test-1";

    public static void main(String[] argv)throws Exception {

//        String topicName = argv[0];
//        in = new Scanner(System.in);
//        System.out.println("Enter message(type exit to quit)");
//
//        String line = in.nextLine();
//        while(!line.equals("exit")) {
//            //TODO: Make sure to use the ProducerRecord constructor that does not take parition Id
//            ProducerRecord<String, String> rec = new ProducerRecord<String, String>(TOPIC,line);
//            getProducer().send(rec);
//            line = in.nextLine();
//        }
//        in.close();

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
