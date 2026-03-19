# Splendor (Java)

This folder currently contains **two different Java code trees**:

- `Game/` compiles today (packages like `game.model`, `game.action`, `game.test`).
- `src/` does **not** compile yet: it references packages/classes that are not present (`engine.*`, `model.*`, etc.).

## Build + run (working tree: `Game/`)

From the `Splendor/` directory:

```bash
make build
make run
```

Override the main class:

```bash
make run MAIN=game.test.TestBirthday
```

Without `make`:

```bash
mkdir -p out
javac -d out $(find Game -name "*.java")
java -cp out game.test.TestBirthday
```

## Testing (no external deps)

Right now the only runnable “test” is `game.test.TestBirthday` (a simple smoke test via `main`).
If you want more tests without adding Maven/Gradle/JUnit yet, add more classes under `Game/test/`
with a `main` method and run them the same way (or use Java `assert` + `java -ea`).
