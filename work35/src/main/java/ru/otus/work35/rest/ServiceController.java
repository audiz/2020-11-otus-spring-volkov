package ru.otus.work35.rest;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.cache.Cache;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.annotation.Retry;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import ru.otus.work35.domain.Summary;
import ru.otus.work35.repositories.AuthorRepository;
import ru.otus.work35.repositories.BookRepository;
import ru.otus.work35.repositories.CommentRepository;
import ru.otus.work35.repositories.GenreRepository;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
@RestController
public class ServiceController {
    private static final String SERVICE_CONTROLLER = "ServiceController";

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    private static javax.cache.Cache<String, Summary> cacheInstance;
    private static Cache<String, Summary> cacheContext;
    private static boolean isFirstFailed = false;

    static {
        cacheInstance = Caching.getCache("cacheSummary", String.class, Summary.class);
        if (cacheInstance == null) {
            final CachingProvider cachingProvider = Caching.getCachingProvider();
            final CacheManager mgr = cachingProvider.getCacheManager();
            MutableConfiguration<String, Summary> config = new MutableConfiguration<>();
            config.setTypes(String.class, Summary.class);
            config.setStoreByValue(true);
            config.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
            cacheInstance = mgr.createCache("cacheSummary", config);
        }
        cacheContext = Cache.of(cacheInstance);
    }

    @CircuitBreaker(name = SERVICE_CONTROLLER, fallbackMethod = "getSummaryFallback")
    @Bulkhead(name = SERVICE_CONTROLLER)
    @Retry(name = SERVICE_CONTROLLER)
    @GetMapping("/api/summary")
    public EntityModel<Summary> summary() {
        if(!isFirstFailed || new Random().nextInt(3) == 2) {
            isFirstFailed = true;
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
        }

        cacheInstance.clear();
        CheckedFunction1<String, Summary> cachedFunction = Decorators
                .ofCheckedSupplier(() -> getSummaryCacheable())
                .withCache(cacheContext)
                .decorate();

        Summary summary = Try.of(() -> cachedFunction.apply("cacheKey")).get();
        return EntityModel.of(summary);
    }

    public Summary getSummaryCacheable() {
        val summary = new Summary();
        summary.setAuthors(authorRepository.count());
        summary.setBooks(bookRepository.count());
        summary.setGenres(genreRepository.count());
        summary.setComments(commentRepository.count());
        return summary;
    }

    private EntityModel<Summary> getSummaryFallback(Exception ex) {

        CheckedFunction1<String, Summary> cachedFunction = Decorators
                .ofCheckedSupplier(() -> {
                    val summary = new Summary();
                    summary.setAuthors(0);
                    summary.setBooks(0);
                    summary.setGenres(0);
                    summary.setComments(0);
                    return summary;
                })
                .withCache(cacheContext)
                .decorate();

        Summary summary = Try.of(() -> cachedFunction.apply("cacheKey")).get();
        return EntityModel.of(summary);
    }
}
