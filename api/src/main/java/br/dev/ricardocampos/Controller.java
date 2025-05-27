package br.dev.ricardocampos;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
