package com.example.demo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MoviesServices {

  public List<Movie> getMovies() {
    List<Movie> gcnList = getMoviesFromGcn();

    if (gcnList.isEmpty()) {
      return getMockMovies();
    }

    return gcnList;
  }

  private List<Movie> getMockMovies() {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    Movie movie1 =
        new Movie(
            "Oppenheimer",
            "Oppenheimer",
            List.of("Cillian Murphy", "Emily Blunt", "Robert Downey Jr."),
            List.of("Christopher Nolan"),
            "A dramatization of the life story of J. Robert Oppenheimer.",
            LocalDateTime.now().plusDays(3L).format(fmt),
            "https://www.youtube.com/embed/uYPbbksJxIg");

    Movie movie2 =
        new Movie(
            "Duna: Parte Dois",
            "Dune: Part Two",
            List.of("Timothée Chalamet", "Zendaya", "Rebecca Ferguson"),
            List.of("Denis Villeneuve"),
            "Paul Atreides unites with the Fremen to seek revenge against the conspirators who"
                + " destroyed his family.",
            LocalDateTime.now().plusDays(10L).format(fmt),
            "https://www.youtube.com/embed/Way9Dexny3w");

    return List.of(movie1, movie2);
  }

  private List<Movie> getMoviesFromGcn() {
    Document doc = null;
    String url = "https://www.gnccinemas.com.br/index.php/embreve-gnc-cinemas";
    try {
      log.info("Fetching movies from {}", url);
      Map<String, String> cookies = new HashMap<>();
      cookies.put("dia", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
      cookies.put("cidade_id", "5"); // Joinville
      cookies.put("lgpd", "1");

      doc = Jsoup.connect(url).cookies(cookies).ignoreHttpErrors(true).get();

      Element divPadding30 = doc.selectFirst(".padding30");
      if (divPadding30 == null) {
        log.warn("div.padding30 not found!");
        return List.of();
      }

      List<Movie> movies = new ArrayList<>();

      // walk though the html
      boolean hasNext = true;
      int index = 0;
      while (hasNext) {

        Elements elements = divPadding30.select("div.barravermelha300");
        Elements elementsForValidation = divPadding30.select("div.barravermelha300");

        // validate empty
        if (Objects.isNull(elements) || elements.isEmpty()) {
          log.info("No movies found!");
          hasNext = false;
          break;
        }

        // // validate if it's the last one
        // if (elements.size() == index + 1) {
        //   log.info("No more movies; {} movies were read", index + 1);
        //   hasNext = false;
        //   break;
        // }

        log.info("Movie {}", index + 1);

        // Launch date
        String launchDate = "";
        Element divRedBar300 = elements.get(index);
        if (divRedBar300 != null) {
          launchDate = divRedBar300.text();
        }
        log.info("launchDate={}", launchDate);

        // i18n title - Title in Brazilian Portuguese
        String i18nTitle = "";
        elements = divPadding30.select("strong.txtvermelho");
        if (elements != null && !elements.isEmpty()) {
          Element strongTxtRed = elements.get(index);
          if (strongTxtRed != null) {
            i18nTitle = strongTxtRed.text();
          }
        }
        log.info("i18nTitle={}", i18nTitle);

        // Youtube embed URL
        String youtubePreviewUrl = "";
        elements = divPadding30.select("iframe");
        if (elements != null && !elements.isEmpty()) {
          Element iframe = elements.get(index);
          if (iframe != null) {
            youtubePreviewUrl = iframe.attributes().get("src");
          }
        }
        log.info("youtubePreviewUrl={}", youtubePreviewUrl);

        // originalTitle
        String originalTitle = "";
        elements = divPadding30.select("p.txtvermelho");
        if (elements != null && !elements.isEmpty()) {
          Element pTextRed = elements.get(index);
          if (pTextRed != null) {
            originalTitle = pTextRed.text().replace("Título Original:", "").trim();
          }
        }
        log.info("originalTitle={}", originalTitle);

        // cast & director
        List<String> cast = new ArrayList<>();
        List<String> directors = new ArrayList<>();
        String summary = "";
        elements =
            divPadding30.select(
                "div[style*=float:left][style*=width:295px][style*=padding-right:20px]");
        if (elements != null && !elements.isEmpty()) {
          Element divElement = elements.get(index);
          if (divElement != null) {
            String entireText = divElement.html();
            int castIndex = entireText.indexOf("Elenco:");
            int closingP = -1;
            String castAndDirector = "";
            if (castIndex > -1) {
              closingP = entireText.indexOf("</", castIndex); // \n</p>
              castAndDirector = entireText.substring(castIndex + 7, closingP-6).trim();
              log.info("castAndDirector={}", castAndDirector);

              if (castAndDirector.contains("|")) {
                String[] people = castAndDirector.split("\\|");
                if (people.length > 0) {
                  String[] names = people[0].split(",");
                  cast = Arrays.asList(names).stream().map(String::trim).toList();
                }
                if (people.length > 1) {
                  String[] names = people[1].replace("Diretor:", "").split(",");
                  directors = Arrays.asList(names).stream().map(String::trim).toList();
                }
              }
            }

            // summary
            if (closingP > -1) {
              int nextParagraphIdx = entireText.indexOf("<p>", closingP);
              if (nextParagraphIdx > -1) {
                int closingSummary = entireText.indexOf("</p>", nextParagraphIdx + 3);
                if (closingSummary > -1) {
                  summary = entireText.substring(nextParagraphIdx + 3, closingSummary).trim();
                }
              }
            }
          }
        }
        log.info("cast={}", cast);
        log.info("directors={}", directors);
        log.info("summary={}", summary);

        Movie movie =
            new Movie(
                i18nTitle, originalTitle, cast, directors, summary, launchDate, youtubePreviewUrl);
        movies.add(movie);

        // validate if it's the last one
        if (elementsForValidation.size() == index + 1) {
          log.info("No more movies; {} movies were read", index + 1);
          hasNext = false;
          break;
        }

        index += 1;
        // Next movie
      }

      return movies;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
