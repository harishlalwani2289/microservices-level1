package com.harish.moviecatalogservice;

import com.harish.moviecatalogservice.model.CatalogItem;
import com.harish.moviecatalogservice.model.Movie;
import com.harish.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userID}")
    public List<CatalogItem> getCatalog(@PathVariable("userID") String userID) {
        // get All rated movie IDs
        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsData/users/" + userID,
                UserRating.class);

        return userRating.getUserRatings().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

            return new CatalogItem(movie.getName(), "Test", rating.getRating());
        }).collect(Collectors.toList());
    }
}


/*Movie movie = webClientBuilder.build()
        .get()
        .uri("http://localhost:8082/movies/" + rating.getMovieId())
        .retrieve()
        .bodyToMono(Movie.class)
        .block();
*/
