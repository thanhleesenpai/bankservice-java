//package payment_service.payment_service.config;
//
//import jakarta.jms.Queue;
//import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
//import org.springframework.jms.support.converter.MessageConverter;
//import org.springframework.jms.support.converter.MessageType;
//
//import java.util.Collections;
//
//@Configuration
//public class JmsConfig {
//    @Bean
//    public Queue queue() {
//        return new ActiveMQQueue("payment-queue");
//    }
//    @Bean
//    public MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//        //converter.setTypeIdPropertyName("_type");
//        converter.setTypeIdMappings(Collections.emptyMap());
//        return converter;
//    }
//}
