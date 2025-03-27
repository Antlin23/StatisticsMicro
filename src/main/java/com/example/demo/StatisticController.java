package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statistics")
public class StatisticController {

    private StatisticRepository statisticRepository;

    private final WebClient webClient;

    public StatisticController(WebClient.Builder webClientBuilder, StatisticRepository statisticRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
        this.statisticRepository = statisticRepository;
    }

    @GetMapping("/{id}")
    public List<Review> getMovieRating(@PathVariable("id") Long movieId) {
        List<Review> allReviews = webClient.get()
                .uri("/reviews") // fetch all reviews
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();

        //If no reviews is found
        if (allReviews == null || allReviews.isEmpty()) {
            System.out.println("No reviews found.");
            return Collections.emptyList();
        }

        // Filter by movieId
        List<Review> filteredReviews = allReviews.stream()
                .filter(review -> review.getMovieId().equals(movieId))
                .collect(Collectors.toList());

        if(filteredReviews.isEmpty()){
            System.out.println("No reviews for that movie found.");
            return Collections.emptyList();
        }

        double rate = 0;
        for (int i = 0; i < filteredReviews.size(); i++){

            rate += filteredReviews.get(i).getRating();

        }

        double averageRate = rate / filteredReviews.size();

        System.out.println(averageRate);

        return filteredReviews;
    }

//Get all reviews by movieId
  /*  @GetMapping("/{id}")
    public List<Review> getMovieReviews(@PathVariable("id") Long movieId) {
        List<Review> allReviews = webClient.get()
                .uri("/reviews") // fetch all reviews
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();

        //If no reviews is found
        if (allReviews == null || allReviews.isEmpty()) {
            System.out.println("No reviews found.");
            return Collections.emptyList();
        }

        // Filter by movieId
        List<Review> filteredReviews = allReviews.stream()
                .filter(review -> review.getMovieId().equals(movieId))
                .collect(Collectors.toList());

        if(filteredReviews.isEmpty()){
            System.out.println("No reviews for that movie found.");
            return Collections.emptyList();
        }

        return filteredReviews;
    }

   */
}