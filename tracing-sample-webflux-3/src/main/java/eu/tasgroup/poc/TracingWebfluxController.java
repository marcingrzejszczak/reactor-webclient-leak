package eu.tasgroup.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class TracingWebfluxController {

	@Autowired
	private WebClient webClient;

	@GetMapping("/hello")
	public Mono<String> hello(){
		log.info("Hello");

//		return webClient.get().uri("https://httpbin.org/get").retrieve().bodyToMono(String.class);
		return Mono.just("HELLO");
	}
}
