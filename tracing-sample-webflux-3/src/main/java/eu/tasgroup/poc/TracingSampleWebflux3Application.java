package eu.tasgroup.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectReader;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.NettyPipeline;
import reactor.netty.http.client.HttpClient;
import reactor.util.context.Context;

@SpringBootApplication
public class TracingSampleWebflux3Application {

	private static final Logger log = LoggerFactory.getLogger(TracingSampleWebflux3Application.class);

	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();

		SpringApplication.run(TracingSampleWebflux3Application.class, args);
	}

	@Autowired ObservationRegistry observationRegistry;

	@PostConstruct
	void setup() {
		ObservationThreadLocalAccessor.getInstance().setObservationRegistry(observationRegistry);
	}

//	@Bean
	ApplicationRunner applicationRunner(WebClient webClient, ObservationRegistry observationRegistry) {
		return (args) -> {
			log.info("Start!");
			Observation server = Observation.start("http.server.requests", observationRegistry);
			server.observe(() -> {
				Observation webClientObs = Observation.createNotStarted("http.client.requests", observationRegistry).start();
				try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(Context.of(ObservationThreadLocalAccessor.KEY, server))) {
					Mono.just("hello")
							.subscribeOn(Schedulers.single())
							.doOnNext(s -> log.info("[Client] After context write"))
							.contextWrite(context -> context.put(ObservationThreadLocalAccessor.KEY, webClientObs))
							.doOnNext(s -> log.info("[Server] After context capture"))
							.contextCapture()
							.block();
				}
			});
		};
	}

	// THIS REPLICATES THE ISSUE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	@Bean
	ApplicationRunner applicationRunner2(WebClient webClient, ObservationRegistry observationRegistry) {
		return (args) -> {
			log.info("Start!");
			Observation server = Observation.start("http.server.requests", observationRegistry);
			server.observe(() -> {
				log.info("In scope");
				webClient.get().uri("https://httpbin.org/get").retrieve().bodyToMono(String.class)
						.contextCapture().block();
			});
		};
	}

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		HttpClient client = HttpClient.create()
				.compress(true)
				.wiretap(true)
				.doOnChannelInit((obs, ch, addr) ->
						ch.pipeline()
								.addFirst(TracingChannelInboundHandler.NAME, TracingChannelInboundHandler.INSTANCE)
								.addLast(TracingChannelOutboundHandler.NAME, TracingChannelOutboundHandler.INSTANCE));
		return builder.clientConnector(new ReactorClientHttpConnector(client))
				.build();
	}

	@Bean
	public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
		NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
		webServerFactory.addServerCustomizers(httpServer ->
				httpServer.wiretap(true).doOnChannelInit((obs, ch, addr) ->
						ch.pipeline()
								.addFirst(TracingChannelInboundHandler.NAME, TracingChannelInboundHandler.INSTANCE)
								.addLast(TracingChannelOutboundHandler.NAME, TracingChannelOutboundHandler.INSTANCE)));
		return webServerFactory;
	}
}
