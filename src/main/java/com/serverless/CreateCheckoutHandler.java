package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;

import com.serverless.dal.Checkout;

public class CreateCheckoutHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

      try {
          //Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
          //String productId = pathParameters.get("id");
    	  
          // get the 'body' from input
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

          // create the Checkout object for post
          Checkout checkout = new Checkout();
          //transaction.setId(body.get("id").asText());
          checkout.setTransacao(body.get("transacao").asText());
          checkout.setValor((float) body.get("valor").asDouble());
          checkout.save(checkout);

          // send the response back
      		return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(checkout)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();

      } catch (Exception ex) {
          logger.error("Error in saving checkout: " + ex);

          // send the error response back
    			Response responseBody = new Response("Error in saving checkout: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
    					.setObjectBody(responseBody)
    					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
    					.build();
      }
	}
}
