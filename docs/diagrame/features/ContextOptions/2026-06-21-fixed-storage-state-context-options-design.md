# Fixed-account storage state for ContextOptionsFactory

## Problem

The per-site `ContextOptionsFactory` implementations (`OlxContextOptionsFactory`,
`AutovitContextOptionsFactory`, `EmagContextOptionsFactory`) currently return plain,
anonymous `Browser.NewContextOptions`. There is no way for a browser context created
for a given site to start already logged in.

Crawling logged in serves two equally important goals: access to data only visible to
an authenticated account, and reduced anti-bot friction compared to anonymous traffic.
Each site uses a single fixed account (no per-request/per-user selection).

## Chosen approach

Reuse a previously-captured Playwright storage state (cookies/localStorage) file, one
fixed file per site, loaded into every new browser context for that site. Capturing or
refreshing the storage state file is a separate, manual/offline concern and is out of
scope here — these factories only consume an existing file.

Two other approaches were considered and rejected for this iteration:
- A long-lived `launchPersistentContext` kept warm in-process: more operationally
  complex (concurrency, disk growth, restart handling) with no clear benefit over
  cookie-based storage state, since the anti-bot signal sites check is primarily the
  session cookie, not context lifetime.
- Automated login performed by the worker itself: rejected because login pages are
  typically more heavily guarded by anti-bot/CAPTCHA than regular browsing, and
  repeated automated logins risk getting the fixed account flagged — directly
  undermining the anti-bot goal.

## Configuration

One storage-state file path per site, configured in `application.properties`:

```
browser-worker.context.olx.storage-state-path=./storage-state/olx.json
browser-worker.context.autovit.storage-state-path=./storage-state/autovit.json
browser-worker.context.emag.storage-state-path=./storage-state/emag.json
```

## Interface

Unchanged — no token or other parameter is introduced:

```java
public interface ContextOptionsFactory {
    Browser.NewContextOptions create();
}
```

## Shared logic: `StorageStateContextOptionsConfigurer`

A new `@Component` in the `contextoptions` package holds the logic common to all three
factories, to avoid duplicating it three times:

```java
Browser.NewContextOptions applyStorageState(Browser.NewContextOptions options, String storageStatePath)
```

Behavior:
- If `storageStatePath` is blank, log a warning (site) and return `options` unchanged
  (anonymous context).
- Otherwise resolve `Path.of(storageStatePath)`. If the file exists, call
  `options.setStorageStatePath(path)`.
- If the resolved file does not exist, log a warning (site/path) and return `options`
  unchanged. This is a deliberate fallback so the worker keeps functioning even when no
  session has been captured yet, rather than failing outright.

No path-traversal handling is needed: the path comes entirely from configuration, never
from request input.

## Per-site factories

Each factory is constructor-injected (per project convention: constructor injection
only, no field injection) with the shared configurer and its own `@Value`-bound file
path:

```java
@Component
@Qualifier("olx")
public class OlxContextOptionsFactory implements ContextOptionsFactory {
    private final StorageStateContextOptionsConfigurer configurer;
    private final String storageStatePath;

    public OlxContextOptionsFactory(
            StorageStateContextOptionsConfigurer configurer,
            @Value("${browser-worker.context.olx.storage-state-path}") String storageStatePath) {
        this.configurer = configurer;
        this.storageStatePath = storageStatePath;
    }

    @Override
    public Browser.NewContextOptions create() {
        return configurer.applyStorageState(new Browser.NewContextOptions(), storageStatePath);
    }
}
```

`AutovitContextOptionsFactory` and `EmagContextOptionsFactory` follow the identical
shape, each with their own `@Qualifier` and property key.

## Testing

- `StorageStateContextOptionsConfigurerTest`: plain JUnit + AssertJ, using `@TempDir`.
  - Existing file at the configured path → `storageStatePath` is set on the returned
    options.
  - Blank path → options unchanged, no exception.
  - Valid-looking path with no matching file → options unchanged, no exception.
- One test per site factory verifying `create()` delegates to the configurer with that
  site's configured path (consistent with the existing plain-unit-test style used for
  `LaunchOptionsFactory` implementations — no Spring context loaded).

## Out of scope

- Capturing/refreshing storage state files (manual login flow, separate tooling).
- Runtime detection of an expired session after navigation (e.g. redirect to a login
  page); only the missing-file case at load time is handled.
- Wiring these `ContextOptionsFactory` beans into `BrowserController` (they are not
  currently used there; this change only prepares the factories themselves).
- Multi-user/per-token session selection.

## Supersedes

This replaces `2026-06-21-context-options-storage-state-design.md`, which introduced a
per-token directory lookup (`create(String token)`) for multi-user sessions. That design
is superseded by this fixed single-account-per-site approach, since each site uses one
fixed account rather than per-request user selection.
