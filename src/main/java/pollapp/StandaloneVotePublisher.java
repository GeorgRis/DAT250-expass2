package pollapp;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import pollapp.events.VoteEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Properties;

public class StandaloneVotePublisher {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ObjectMapper mapper = new ObjectMapper();

        // Create a test vote event
        VoteEvent voteEvent = new VoteEvent("test-poll-id", "choice-1", "anonymous-voter", "VOTE_CAST");
        String jsonEvent = mapper.writeValueAsString(voteEvent);

        ProducerRecord<String, String> record = new ProducerRecord<>("poll-test-poll-id", "choice-1", jsonEvent);
        producer.send(record);

        System.out.println("Vote event published!");
        producer.close();
    }
}