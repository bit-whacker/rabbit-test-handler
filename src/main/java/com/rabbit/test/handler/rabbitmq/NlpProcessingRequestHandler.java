package com.rabbit.test.handler.rabbitmq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lacheff.commonutil.rabbitmq.RabbitGate;
import com.lacheff.commonutil.rabbitmq.RabbitMessageHander;
import com.lacheff.commonutil.rabbitmq.request.NlpProcessingRequest;
import com.lacheff.commonutil.rabbitmq.request.NlpSummarizationRequest;
import com.lacheff.commonutil.rabbitmq.response.NlpProcessingResponse;
import com.lacheff.commonutil.rabbitmq.response.NlpSummarizationResponse;
import com.lacheff.commonutil.rabbitmq.support.ExchangeType;
import com.lacheff.commonutil.rabbitmq.support.RoutingKey;

public class NlpProcessingRequestHandler implements RabbitMessageHander {
    private static Logger LOG = LogManager.getLogger(NlpProcessingRequestHandler.class);



	private RabbitGate rabbitGate = RabbitGateManager.getInstance().getRabbitGate();

	@Override
	public void handle(Object object) {
		LOG.info("Seeing this in the log means that message was send and received");

		if (object instanceof NlpProcessingRequest) {
			handleNlpProcessingRequest((NlpProcessingRequest) object);
		} else if (object instanceof NlpSummarizationRequest){
		    handleNlpSummarizationRequest((NlpSummarizationRequest) object);
		} else {
		    LOG.error("Invalid Request Recieved!!!, {}", object.getClass());
		}

	}

	private void handleNlpProcessingRequest(NlpProcessingRequest nlpProcessingRequest) {
		LOG.info("Got processing request: {}", nlpProcessingRequest.getReviewUUID());
		try {

			NlpProcessingResponse nlpProcessingResponse = new NlpProcessingResponse();
			nlpProcessingResponse.setReviewUUID(new Date() + "");
			LOG.debug("Shipping response after NLP processing: {}", nlpProcessingResponse);
			rabbitGate.send(nlpProcessingResponse);
			
		} catch (Exception ex) {
			LOG.error("Exception at handleNlpProcessingRequest:", ex);
		}
	}
	
	private void handleNlpSummarizationRequest(NlpSummarizationRequest nlpSummarizationRequest){
	    LOG.debug("Sending response after NLP Summarization Request");
	    NlpSummarizationResponse nlpSummarizationResponse = new NlpSummarizationResponse();
	    nlpSummarizationResponse.setRequestUUID(new Date() + "");
	    rabbitGate.send(nlpSummarizationResponse);
	}
	
	@Override
	public List<Class> getHandlingTypes() {
		List<Class> list = new ArrayList<Class>();
		list.add(NlpProcessingRequest.class);
		list.add(NlpSummarizationRequest.class);
		return list;
	}

	@Override
	public ExchangeType getExchangeType() {
		return ExchangeType.NLP_PROCESSING;
	}

	@Override
	public RoutingKey getRoutingKey() {
		return RoutingKey.TO_NLP_PROCESSOR;
	}

}
