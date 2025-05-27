package br.dev.ricardocampos;

import java.util.List;

public record Movie(
    String i18nTitle,
    String originalTitle,
    List<String> cast,
    List<String> directors,
    String summary,
    String launchDate,
    String youtubePreviewUrl) {}
