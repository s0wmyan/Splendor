JAVA_SRC_DIR := Game
OUT_DIR := out

# Override like: make run MAIN=game.test.TestBirthday
MAIN ?= game.test.TestBirthday

SOURCES := $(shell find $(JAVA_SRC_DIR) -name "*.java")

.PHONY: build run clean

build:
	mkdir -p $(OUT_DIR)
	javac -d $(OUT_DIR) $(SOURCES)

run: build
	java -cp $(OUT_DIR) $(MAIN)

clean:
	rm -rf $(OUT_DIR)
