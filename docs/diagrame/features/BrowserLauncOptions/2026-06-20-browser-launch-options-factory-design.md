# Browser Launch Options Factory — Design

## Context

The crawler needs to produce Playwright `BrowserType.LaunchOptions` configurations for Chromium, varying by where the browser process runs:

- **Docker**: running inside a Docker container.
- **Non-Docker**: running directly on a host, outside Docker.

This factory is scoped to browser-level launch configuration only. Anti-detection/stealth concerns (user-agent, viewport, locale, timezone, etc.) live on `Browser.NewContextOptions`, which is a context-level concern, not a launch-level one — out of scope for this design entirely.

## Scope

- Stack: Java, Spring, Maven, Playwright.
- Browser engine: Chromium only.
- Only `BrowserType.LaunchOptions` is produced — no context options, no stealth.
- No per-call override object — each environment is a fixed, pre-configured preset.
- No shared parameterized class or enum-driven branching. Docker vs non-Docker is encoded by which concrete class a caller injects, not by a constructor argument or runtime dispatch — there are exactly two fixed environments and no indication of more arriving, so a parameter that always resolves to one of two hardcoded branches is just indirection around picking a class.

## Components

### `LaunchOptionsDirector`

Shared interface, kept for a uniform contract and testability:

```java
public interface LaunchOptionsDirector {
    BrowserType.LaunchOptions create();
}
```

### `DockerLaunchOptionsDirector`

```java
@Component
public class DockerLaunchOptionsDirector implements LaunchOptionsDirector {
    public BrowserType.LaunchOptions create() {
        // headless = true, args = ["--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu"],
        // executablePath = path to container-installed Chromium
    }
}
```

### `NonDockerLaunchOptionsDirector`

```java
@Component
public class NonDockerLaunchOptionsDirector implements LaunchOptionsDirector {
    public BrowserType.LaunchOptions create() {
        // headless = false, devtools = true, no executablePath override (uses Playwright's bundled browser)
    }
}
```

Each `create()` must construct a new `BrowserType.LaunchOptions` instance on every call — never return or mutate a cached/shared instance — since each Director bean is a long-lived Spring singleton and multiple callers must not interfere with each other's config objects.

Callers inject the concrete Director they need directly:

```java
@Service
public class CrawlerService {
    private final LaunchOptionsDirector director;

    public CrawlerService(@Qualifier("dockerLaunchOptionsDirector") LaunchOptionsDirector director) {
        this.director = director;
    }

    void launch() {
        BrowserType.LaunchOptions options = director.create();
        // playwright.chromium().launch(options) ...
    }
}
```

## Data Flow

```
Caller holds a specific LaunchOptionsDirector bean (chosen by concrete type at injection time)
  → director.create()
  → return fresh BrowserType.LaunchOptions
```

No parameters are passed per call, and no enum, config class, or runtime branching exists — the environment is fixed by which class was written and injected.

## Error Handling

- There is no "missing environment" failure mode: each environment is its own concrete class. Referencing one that doesn't exist is a compile-time error, not a runtime one.
- The only invariant to enforce is that `create()` never returns a cached/shared instance — covered by tests below.

## Testing

- One unit test per Director (`DockerLaunchOptionsDirector`, `NonDockerLaunchOptionsDirector`) asserting the exact resulting `BrowserType.LaunchOptions` fields (headless, args, executablePath as applicable).
- One test per Director asserting that calling `create()` twice returns two distinct `BrowserType.LaunchOptions` objects, confirming no shared mutable state.
