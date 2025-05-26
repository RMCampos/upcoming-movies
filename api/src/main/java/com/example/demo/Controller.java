package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/upcoming-movies")
@AllArgsConstructor
public class Controller {

  private final MoviesServices moviesServices;
  
  @GetMapping
  public List<Movie> getMovies() {
    return moviesServices.getMovies();
  }
}
