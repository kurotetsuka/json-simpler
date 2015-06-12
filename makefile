# globals
default: build
freshen: clean build
clean:
	rm -rf $(docs_path)
	rm -rf bin/*
	rm -rf pkg/*
	rm -rf jar/*

# variables
version = 2.1.1a2
cp = bin:lib/*
docscp = lib/*:
docs_path = javadoc
java_version = -source 1.6 -target 1.6
options =
#warnings = -Xlint:deprecation
#warnings = -Xlint:unchecked

# includes
include lists.mk
include deps.mk
include pkg.mk

# compilation definitions
$(class_files): bin/%.class : src/%.java
	javac -cp $(cp) -d bin $(java_version) $(warnings) $<

# command definitions
build: $(class_files)
build-base: $(base_class_files)

run: test

ci:
	make-ci build $(source_files)

jar: $(jar_base)
jars: $(jar_files)
docs-test: docs
	chrome javadoc/index.html &
package: $(package_base)
packages: $(package_files)
package-test: package
	file-roller $(package_base) &> /dev/null &

# documentation
docs: $(docs_path)
$(docs_path): $(source_files)
	rm -rf $(docs_path)
	javadoc -classpath $(docscp) \
		-d $(docs_path) $(source_files)

# test commands
test: test-tutorial

test-all: \
	test-tutorial \
	test-castgets \
	test-casts \
	test-contains \
	test-gets \
	test-sets \
	test-typechecks

test-tutorial: bin/kuro/json/tutorial/Tutorial.class
	java -cp $(cp) kuro.json.tutorial.Tutorial

test-castgets: bin/kuro/json/test/TestCastGets.class
	java -cp $(cp) kuro.json.test.TestCastGets
test-casts: bin/kuro/json/test/TestCasts.class
	java -cp $(cp) kuro.json.test.TestCasts
test-contains: bin/kuro/json/test/TestContains.class
	java -cp $(cp) kuro.json.test.TestContains
test-gets: bin/kuro/json/test/TestGets.class
	java -cp $(cp) kuro.json.test.TestGets
test-sets: bin/kuro/json/test/TestSets.class
	java -cp $(cp) kuro.json.test.TestSets
test-typechecks: bin/kuro/json/test/TestTypeChecks.class
	java -cp $(cp) kuro.json.test.TestTypeChecks
