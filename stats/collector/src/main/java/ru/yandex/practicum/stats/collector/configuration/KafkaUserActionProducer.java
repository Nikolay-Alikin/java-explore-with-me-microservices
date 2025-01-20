package ru.yandex.practicum.stats.collector.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
@ToString
public class KafkaUserActionProducer {

    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final KafkaConfig config;

    public KafkaUserActionProducer(KafkaConfig config) {
        this.config = config;
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getValueSerializer());
        this.producer = new KafkaProducer<>(props);
    }

    public void sendRecord(ProducerRecord<String, SpecificRecordBase> record) {
        try {
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println(
                            "Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());
                }
            });
            producer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
