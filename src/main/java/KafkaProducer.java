/**
 * Created by yinmuyang on 16/7/13.
 */
import java.util.*;
import java.util.concurrent.TimeUnit;

import kafka.message.Message;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.javaapi.producer.Producer;
import kafka.serializer.StringEncoder;

public class KafkaProducer extends Thread{

    private String topic;

    public KafkaProducer(String topic){
        super();
        this.topic = topic;
    }

    @Override
    public void run() {
        Producer producer = createProducer();
        int i=0;
        while(true){
            i++;
            String  string = "hello"+i;
//            KeyedMessage[K, V](val topic: String, val key: K, val partKey: Any, val message: V)
            producer.send(new KeyedMessage<String, String>(topic,string,"------"+i));
//            if(i==100){
//                break;
//            }
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    private Producer createProducer() {
        Properties properties = new Properties();
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("partitioner.class","kafka.producer.DefaultPartitioner");
        properties.put("metadata.broker.list", "192.168.100.70:9092,192.168.100.71:9092,192.168.100.72:9092");
        properties.put("zk.connect","192.168.100.63:2181,192.168.100.64:2181,192.168.100.65:2181");
        return new Producer<String, String>(new ProducerConfig(properties));
    }


    public static void main(String[] args) {
        new KafkaProducer("test").start();
    }

    public static void test(String[] args) {
        KafkaProducer ps = new KafkaProducer("");

        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        KeyedMessage<Integer,String> data = new KeyedMessage<Integer, String>("test-topic","xxxxxxxxxxxxx");
//        ProducerData<String, String> data = new ProducerData<String, String>("test-topic", "test-message2");
//        producer.send(data);
        producer.close();
    }
}
