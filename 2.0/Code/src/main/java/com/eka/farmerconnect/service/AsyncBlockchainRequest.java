package com.eka.farmerconnect.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//import com.eka.blockchain.util.BlockchainConfig;
//import com.eka.blockchain.util.BlockchainFactory;

@Service
public class AsyncBlockchainRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncBlockchainRequest.class);
	private static final int nThreads = 3; // no. of threads in the pool
	private static final int timeout = 0; // connection time out in milliseconds
	//private static BlockchainConfig blockchainConfig = BlockchainFactory.getBlockchainConfig();

	public void createSmartContract(String requestJsonData) {
		URIBuilder urlBuilder = new URIBuilder();//;.setScheme(blockchainConfig.getHostType())
				//.setHost(blockchainConfig.getHost()).setPath(blockchainConfig.getPath());
		// URIBuilder urlBuilder = new
		// URIBuilder().setScheme("http").setHost("127.0.0.1:8090").setPath("/contract/create");

		URI uri = null;
		try {
			uri = urlBuilder.build();
		} catch (URISyntaxException use) {
			use.printStackTrace();
		}

		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
		Async async = Async.newInstance().use(executorService);

		final Request request = Request.Post(uri).bodyString(requestJsonData, ContentType.APPLICATION_JSON)
				.connectTimeout(timeout);

		async.execute(request, new FutureCallback<Content>() {
			public void failed(final Exception e) {
				LOGGER.info("Async Request failed: " + request);
			}

			public void completed(final Content content) {
				LOGGER.info("Async Request completed: " + request);
				LOGGER.info("Async Request content: " + content.asString());
			}

			public void cancelled() {
				LOGGER.info("Async Request cancelled: ");

			}
		});

		LOGGER.info("Async Request submitted: ");

	}

}