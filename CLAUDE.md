# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

MPopUp is a small Android **dialog/popup library** distributed via JitPack as
`com.github.mafmudin:MPopUp:<git-tag>`. The repo is a two-module Gradle project: the
`:mpopup` library and an `:app` demo that exercises it.

The README is in Indonesian and is the user-facing API documentation — keep it in sync
when the public API of `MPopUp.java` changes.

Toolchain (modernized to match the `progress-sg` sibling project):
- Gradle **8.13**, Android Gradle Plugin **8.13.0**, plugins DSL.
- Kotlin plugin (`org.jetbrains.kotlin.android` **2.2.0**) applied to both modules — the
  source is still **Java**, but Kotlin files compile out of the box.
- `compileSdk` **36**, `minSdk` **21**, `targetSdk` **36** (app).
- **AndroidX** (`android.useAndroidX=true`, `android.nonTransitiveRClass=true`),
  **viewBinding** enabled, builds on **JDK 17** (`sourceCompatibility`/`targetCompatibility`/`jvmTarget` = 17).

(Historical note: originally a legacy stack — Gradle 4.4 / AGP 3.1.4 / jcenter / SDK 28 /
minSdk 15 / `android.support` / JDK 8. Don't reintroduce the old toolchain.)

CLI builds need `local.properties` with `sdk.dir=...` (Android Studio generates it; not
committed). JitPack supplies it via `jitpack.yml`.

## Modules

- **`:mpopup`** (`com.android.library`) — the published artifact. Entire public API is one
  class: `mpopup/src/main/java/udinsi/dev/owntoast/MPopUp.java`.
- **`:app`** (`com.android.application`) — demo app. `MainActivity` shows both supported
  usage patterns and is the de-facto integration test.

Both modules share the **same namespace** `udinsi.dev.owntoast`; `R` resolves per-module
(see the nonTransitiveRClass landmine below).

## Commands

Builds on **JDK 17** (set `JAVA_HOME` to a JDK 17 if the default differs).

```bash
./gradlew :app:assembleDebug             # build demo app
./gradlew :mpopup:assembleRelease        # build the library .aar
./gradlew :mpopup:publishToMavenLocal    # what JitPack runs (needs -PVERSION=<tag>)
./gradlew lint                           # Android lint
./gradlew test                           # JVM unit tests (all modules)
./gradlew connectedAndroidTest           # instrumented tests (needs device/emulator)

# single unit test class:
./gradlew :app:testDebugUnitTest --tests "udinsi.dev.owntoast.ExampleUnitTest"
```

Note: the only test files (`ExampleUnitTest`, `ExampleInstrumentedTest`) are unmodified
template stubs — there is no real test coverage yet.

## API design (the whole library)

`MPopUp` offers two mutually-exclusive usage styles, both wrapping an
`androidx.appcompat.app.AlertDialog` whose window background is made transparent:

1. **Fluent builder** — `new MPopUp().in(ctx).setTitle(...).setMessage(...)....show()`.
   Renders the bundled `mpopup/.../res/layout/popup_dialog.xml` (a `CardView` containing
   a title `TextView`, message `TextView`, and OK/Cancel `Button`s). Every setter returns
   `this` for chaining.
2. **Custom layout** — `customLayout(layoutResId, ctx)` inflates an arbitrary layout, shows
   it as a dialog, and **returns the inflated `View`** so the caller wires up its own
   widgets and calls `dismiss()`.

Button callbacks go through the `MPopUp.OnClick` interface via `setOnOkClickListener` /
`setOnCancleClickListener` (note the misspelling "Cancle" — it is part of the public API,
including the default negative-button label and `setOnCancleClickListener`; renaming is a
breaking change).

## Releasing (JitPack + GitHub Actions)

Publishing is **tag-driven**: JitPack builds the library from a pushed git tag and serves it
as `com.github.mafmudin:MPopUp:<tag>`. The `:mpopup` module applies `maven-publish`
(`singleVariant('release')` + sources jar) and takes its version from the `-PVERSION` Gradle
property (set in root `build.gradle`), falling back to `0.0.1-SNAPSHOT` for local builds — so
the pushed git tag drives the published version. `artifactId` is pinned to `MPopUp` and
`group` to `com.github.mafmudin`. `jitpack.yml` pins `openjdk17` and runs
`:mpopup:publishToMavenLocal`. `_config.yml` (`jekyll-theme-hacker`) only themes the GitHub
Pages site, not the build.

Versions are **bare semver** (e.g. `1.0.0`, no `v` prefix). The legacy `v1` tag predates this
scheme and is left untouched (so it does not match the workflow's `[0-9]+.[0-9]+.[0-9]+`
trigger). `.github/workflows/release.yml` automates a release two ways: (1) `workflow_dispatch`
with a `patch`/`minor`/`major` bump (derives the next tag from the latest semver tag, pushes
it), or (2) pushing a bare-semver tag manually. It builds & validates the publication on
JDK 17, creates a GitHub Release with the `.aar` attached, and warms up JitPack. Because the
latest matching tag is currently none, the **first** dispatch from `0.0.0` yields `0.0.1` on a
`patch` bump — pick `major` (→ `1.0.0`) or push `1.0.0` manually for the first release.

## Landmines

- **NPE in the builder path**: `show()` registers click listeners that call
  `listener.onClick()` / `cancleListener.onClick()` unconditionally, but both default to
  `null`. A consumer who calls `show()` without `setOnOkClickListener` /
  `setOnCancleClickListener` will crash on button tap. The demo avoids this by setting the
  listeners (it does so *after* `show()`, which is fine since they only fire on tap).
- **`setBgColor` expects a resolved color int**, not a resource id — it feeds
  `llBackground.setBackgroundColor(...)` directly. The demo passes `R.color.primary` (a
  resource id), which is technically the wrong type; preserve existing behavior unless
  explicitly fixing it.
- **`nonTransitiveRClass=true` → each module owns its code-referenced resources**: the
  `:mpopup` library is self-contained. The `:app` demo keeps its **own** copies of the
  `white`/`primary` colors (in `app/.../values/colors.xml`) and the `button_primary_round*`
  drawables, because `MainActivity` references them in **code** (`R.color.white`,
  `R.drawable.button_primary_round`) and the app's `R` only contains app-local resources.
  XML references (`@color/...`) still resolve cross-module via resource merge; **code**
  references do not. If you move/remove these app-local copies, the demo stops compiling.
- **Two `popup_dialog.xml` copies**: one in `:mpopup` (used by the builder `show()` path via
  the library's `R`) and one in `:app` (used by the `customLayout()` demo). Edit both if you
  change the layout. Because both modules share the namespace `udinsi.dev.owntoast` **and**
  both have a layout named `popup_dialog.xml`, viewBinding would generate the same
  `udinsi.dev.owntoast.databinding.PopupDialogBinding` class in each module → a duplicate-class
  failure at `mergeDex`. Both roots therefore carry `tools:viewBindingIgnore="true"` (no code
  uses the binding — inflation is manual via `findViewById`/`customLayout`). Keep that
  attribute on both, or give the modules distinct namespaces, if you touch these layouts.
  Note also: the `:app` module declares `androidx.cardview:cardview` directly — `:mpopup`
  exposes cardview only via `implementation` (runtime-only), so the app's compiled
  `popup_dialog.xml` would not resolve `CardView` without its own dependency.
- **Package vs. name**: everything lives in namespace/package `udinsi.dev.owntoast`
  ("OwnToast"), unrelated to "MPopUp". This is historical — don't assume the package name
  describes the module.
- **`color.xml` vs `colors.xml`** in `:mpopup`: `@color/primary` is in `color.xml`
  (singular), `@color/white` in `colors.xml` (plural) — both are real and referenced by
  drawables/layouts.
