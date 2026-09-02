package br.com.andrewdesigner.web;

import br.com.andrewdesigner.github.GithubService;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class PortfolioResource {
    @Inject @Location("portfolio.html")
    Template portfolio;

    @Inject @Location("fragments/github-repos.html")
    Template githubRepos;

    @Inject
    GithubService github;

    @GET
    public TemplateInstance pagina() {
        return portfolio.instance();
    }

    @GET
    @Path("fragments/github")
    public TemplateInstance repositorios() {
        var repositorios = github.destaques();
        return githubRepos.data("repos", repositorios)
                .data("temRepos", !repositorios.isEmpty())
                .data("usuario", github.usuario());
    }
}
