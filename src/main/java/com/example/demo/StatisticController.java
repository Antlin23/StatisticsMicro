package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statistics")
public class StatisticController {

    private StatisticRepository statisticRepository;

    private final WebClient webClient;
    private final WebClient webClientMovie;


    public StatisticController(WebClient.Builder webClientBuilder, WebClient.Builder webClientMovieBuilder, StatisticRepository statisticRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
        this.webClientMovie = webClientMovieBuilder.baseUrl("http://localhost:8083").build();
        this.statisticRepository = statisticRepository;
    }

    @GetMapping("/{id}")
    public Double getMovieRating(@PathVariable("id") Long movieId) {
        List<Review> allReviews = webClient.get()
                .uri("/reviews") // fetch all reviews
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();

        //If no reviews is found
        if (allReviews == null || allReviews.isEmpty()) {
            System.out.println("No reviews found.");
            return null;
        }

        // Filter by movieId
        List<Review> filteredReviews = allReviews.stream()
                .filter(review -> review.getMovieId().equals(movieId))
                .collect(Collectors.toList());

        if(filteredReviews.isEmpty()){
            System.out.println("No reviews for that movie found.");
            return null;
        }

        double rate = 0;
        for (int i = 0; i < filteredReviews.size(); i++){
            rate += filteredReviews.get(i).getRating();
        }

        double averageRate = rate / filteredReviews.size();

        System.out.println(averageRate);

        return averageRate;
    }

    @GetMapping("/top")
    public Movie getTopRatedMovie() {
        List<Review> allReviews = webClient.get()
                .uri("/reviews") // fetch all reviews
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();

        //If no reviews is found
        if (allReviews == null || allReviews.isEmpty()) {
            System.out.println("No reviews found.");
            return null;
        }

        List<Movie> allMovies = webClientMovie.get()
                .uri("/movies") // fetch all reviews
                .retrieve()
                .bodyToFlux(Movie.class)
                .collectList()
                .block();

        if(allMovies.size() < 1){
            return null;
        }

        Movie topRatedMovie = new Movie();

        for(int i = 0; i < allMovies.size(); i++){
            System.out.println(allMovies.get(i).getTitle());
            if (topRatedMovie.getTitle() == null || topRatedMovie.getTitle().isEmpty()) {
                topRatedMovie = allMovies.get(i);
            } else if (getMovieRating(allMovies.get(i).getId()) != null) {
                if(getMovieRating(allMovies.get(i).getId()) > getMovieRating(topRatedMovie.getId())){
                    topRatedMovie = allMovies.get(i);
                }
            }
        }
        return topRatedMovie;
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