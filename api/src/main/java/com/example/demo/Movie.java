package com.example.demo;

import java.util.List;

public record Movie(
    String i18nTitle,            // ok
    String originalTitle,        // ok
    List<String> cast,           // ok
    List<String> directors,      // ok
    String summary,
    String launchDate,           // ok
    String youtubePreviewUrl) {} // ok
