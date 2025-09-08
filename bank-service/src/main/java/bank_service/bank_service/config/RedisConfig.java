package bank_service.bank_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Tạo ObjectMapper cho Redis
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        // Serializer mới (không deprecated)
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(mapper);

        // Cấu hình key / value
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
        return new StringRedisTemplate(cf); // StringRedisSerializer cho cả key & value
    }
//
//    @Bean
//    public RedisTemplate<String, Balance> balanceRedisTemplate() {
//        RedisTemplate<String, Balance> template = new RedisTemplate<>();
//        template.setConnectionFactory(this.connectionFactory);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        // Truyền ObjectMapper vào hàm tạo
//        Jackson2JsonRedisSerializer<Balance> serializer = new Jackson2JsonRedisSerializer<>(mapper, Balance.class);
//
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(serializer);
//        template.afterPropertiesSet();
//        return template;
//    }
//    @Bean
//    public RedisTemplate<String, Account> accountRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Account> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        // Chỉ định serializer sẽ xử lý class Account
//        Jackson2JsonRedisSerializer<Account> serializer = new Jackson2JsonRedisSerializer<>(Account.class);
//
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(serializer);
//
//        return template;
//    }
//    private final RedisConnectionFactory connectionFactory;
//
//    private ObjectMapper getObjectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        return mapper;
//    }
//
//    private <T> RedisTemplate<String, T> createTemplate(Class<T> type) {
//        RedisTemplate<String, T> template = new RedisTemplate<>();
//        template.setConnectionFactory(this.connectionFactory);
//
//        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(this.getObjectMapper(), type);
//
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(serializer);
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(serializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }
//
////    @Bean
////    public RedisTemplate<String, String> stringRedisTemplate() {
////        // Vẫn giữ lại StringRedisTemplate cho các trường hợp chỉ lưu chuỗi
////        StringRedisTemplate template = new StringRedisTemplate();
////        template.setConnectionFactory(this.connectionFactory);
////        return template;
////    }
//
//    @Bean
//    public RedisTemplate<String, Balance> balanceRedisTemplate() {
//        return createTemplate(Balance.class);
//    }
//
//    // Ví dụ: Tạo thêm RedisTemplate cho class Account
//    @Bean
//    public RedisTemplate<String, Account> accountRedisTemplate() {
//        return createTemplate(Account.class);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> RedisTemplate() {
//        return createTemplate(Object.class);
//    }

}

