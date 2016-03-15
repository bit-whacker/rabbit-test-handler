package com.rabbit.test.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lacheff.commonutil.rabbitmq.RabbitConnectionManager;
import com.lacheff.commonutil.rabbitmq.RabbitMessageConsumer;
import com.rabbit.test.handler.rabbitmq.RabbitGateManager;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger LOG = LogManager.getRootLogger();
    
    private RabbitConnectionManager rabbitConnectionManager;
    public static void main( String[] args )
    {
        System.out.println( "Initializing Rabbit Request Handler" );
        (new App()).init();
    }
    private void init(){
        rabbitConnectionManager = RabbitGateManager.getInstance().getRabbitConnectionManager();
        try {
            LOG.debug("waiting for five seconds...");
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sleepUntilRabbitConnectionSucceed();
        LOG.info("Rabbit connection is ready now!");
    }
    
    private void sleepUntilRabbitConnectionSucceed() {
        for (;;) {
            RabbitMessageConsumer rConsumer = rabbitConnectionManager
                    .getRabbitMessageConsumer();
            if (rConsumer == null) {
                LOG.info("rConsumer is null. Rabbit connection is not ready yeat");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOG.error("InterruptedException e");
                }
            } else {
                return;
            }
        }

    }
    
}
