package com.rabbit.test.handler.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lacheff.commonutil.rabbitmq.RabbitConnectionManager;
import com.lacheff.commonutil.rabbitmq.RabbitGate;
import com.lacheff.commonutil.rabbitmq.RabbitMessageHander;

public class RabbitGateManager {
    private static Logger LOG = LogManager.getRootLogger();
    private static RabbitGateManager INSTANCE = null;
    private RabbitGate rabbitGate;
    private NlpProcessingRequestHandler nlpProcessingRequestHandler;
    private RabbitConnectionManager rabbitConnectionManager;
    
    private static String RABBIT_HOST = "localhost";
    private static String RABBIT_USER = "guest";
    private static String RABBIT_PWD = "guest";
    
    private RabbitGateManager(){
        if(!initializeRabbitGate()){
            System.exit(0);
        }
    }
    
    public static synchronized RabbitGateManager getInstance(){
        if(INSTANCE == null){
           LOG.info("Creating New RabbitSender Instance");
           INSTANCE = new RabbitGateManager();
        } else {
            LOG.info("Returning existing RabbitSender Instance");
        }
        
        return INSTANCE;
    }
    
    public RabbitGate getRabbitGate(){
        return INSTANCE.rabbitGate;
    }
    public RabbitConnectionManager getRabbitConnectionManager(){
        return INSTANCE.rabbitConnectionManager;
    }
    
    private boolean initializeRabbitGate(){
        boolean rabbitCreationSuccess = true;
        try{
            nlpProcessingRequestHandler = new NlpProcessingRequestHandler();
            
            List<RabbitMessageHander> handlers = new ArrayList<RabbitMessageHander>();
            handlers.add(nlpProcessingRequestHandler);
            rabbitConnectionManager = new RabbitConnectionManager(RABBIT_HOST, RABBIT_USER, RABBIT_PWD, handlers);
            rabbitGate = new RabbitGate(rabbitConnectionManager);
            
        }catch(Exception ex){
            rabbitCreationSuccess = false;
            LOG.error("RabbitGate Creation Exception: {}", ex.getMessage());
            ex.printStackTrace();
        }
        
        return rabbitCreationSuccess;
    }
}
