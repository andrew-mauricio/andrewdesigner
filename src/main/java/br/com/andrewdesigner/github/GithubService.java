package br.com.andrewdesigner.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class GithubService {
    private static final Set<String> DESTAQUES = Set.of(
            "Conan-Api", "Conan-Api-SDK", "Conan-Shop", "Conan-Unlock-Engrams");

    private final ObjectMapper mapper;
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(4))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    @ConfigProperty(name = "portfolio.github.user")
    String usuario;

    @ConfigProperty(name = "portfolio.github.cache-minutes", defaultValue = "10")
    long cacheMinutes;

    private volatile List<GithubRepository> cache = List.of();
    private volatile Instant cacheAte = Instant.EPOCH;

    public GithubService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<GithubRepository> destaques() {
        if (Instant.now().isBefore(cacheAte) && !cache.isEmpty()) {
            return cache;
        }
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/users/" + usuario + "/repos?per_page=100&sort=updated"))
                    .timeout(Duration.ofSeconds(6))
                    .header("Accept", "application/vnd.github+json")
                    .header("User-Agent", "andrewdesigner.com.br")
                    .GET()
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                var repositorios = mapper.readValue(response.body(), new TypeReference<List<GithubRepository>>() {});
                cache = DESTAQUES.stream()
                        .map(nome -> repositorios.stream().filter(repo -> nome.equals(repo.name())).findFirst().orElse(null))
                        .filter(java.util.Objects::nonNull)
                        .toList();
                cacheAte = Instant.now().plus(Duration.ofMinutes(cacheMinutes));
            }
        } catch (Exception erro) {
            Log.warnf("GitHub indisponível; mantendo fallback: %s", erro.getMessage());
        }
        return cache;
    }

    public String usuario() {
        return usuario;
    }
}
