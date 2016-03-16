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
public class App {
    
    private static Logger LOG = LogManager.getRootLogger();
    private RabbitConnectionManager rcm;

    public static void main(String[] args) throws InterruptedException{
        (new App()).initPro();
    }

    public void initPro() throws InterruptedException {
        rcm = RabbitGateManager.getInstance().getRabbitConnectionManager();
        Thread.sleep(3000);
        
        LOG.debug("rabbitConnectionManager is getting ready!");
        sleepUntilRabbitConnectionSucceed();
        
        LOG.debug("rabbitConnectionManager is ready to handle messages :D");
    }

    private void sleepUntilRabbitConnectionSucceed() {
        for (;;) {
            RabbitMessageConsumer rConsumer = rcm.getRabbitMessageConsumer();
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
