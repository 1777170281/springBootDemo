package com.tttare.rabbit.rabbitmqOps.consumers;

import com.rabbitmq.client.Channel;
import com.tttare.rabbit.model.Mail;
import com.tttare.rabbit.model.RabbitMQConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ClassName: CommenConsumer <br/>
 * Description: <br/>
 * date: 2019/9/29 20:02<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Component
@Slf4j
public class FanoutConsumer {

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = RabbitMQConstant.FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT,
                    durable = RabbitMQConstant.FALSE_CONSTANT, autoDelete = RabbitMQConstant.TRUE_CONSTANT),
            value = @Queue(value = RabbitMQConstant.FANOUT_QUEUE_ONE, durable = RabbitMQConstant.FALSE_CONSTANT,
                    autoDelete = RabbitMQConstant.TRUE_CONSTANT)
            /*FANOUT 不需要key ,key = RabbitMQConstant.CONFIRM_KEY*/),
            containerFactory = "rabbitListenerContainerFactory")
    public void process1(Mail mail, Channel channel,@Header(name = "amqp_deliveryTag") long deliveryTag,
                        @Header("amqp_redelivered") boolean redelivered, @Headers Map<String, String> head) {
        try {

            channel.basicAck(deliveryTag,true);
            //channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.info("------receive,来自消费者一: " + mail.toString());
            log.info("------header:"+head);
            channel.close();
        } catch (Exception e) {
            log.error("consume confirm error!", e);
            //这一步千万不要忘记，不会会导致消息未确认，消息到达连接的qos之后便不能再接收新消息
            //一般重试肯定的有次数，这里简单的根据是否已经重发过来来决定重发。第二个参数表示是否重新分发
            channel.basicReject(deliveryTag, !redelivered);
            //这个方法我知道的是比上面多一个批量确认的参数
            // channel.basicNack(deliveryTag, false,!redelivered);
        }
    }

    @SneakyThrows
    @RabbitListener(queues=RabbitMQConstant.FANOUT_QUEUE_TWO,
            containerFactory = "rabbitListenerContainerFactory")
    public void process2(Mail mail, Channel channel,@Header(name = "amqp_deliveryTag") long deliveryTag,
                        @Header("amqp_redelivered") boolean redelivered, @Headers Map<String, String> head) {
        try {
            channel.basicAck(deliveryTag,true);
            log.info("------receive,来自消费者二: " + mail.toString());
            log.info("------header:"+head);
            channel.close();
        } catch (Exception e) {
            log.error("consume confirm error!", e);
            channel.basicReject(deliveryTag, !redelivered);
        }
    }
}
