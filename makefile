#globals
default: build
freshen: clean build
clean:
	rm -rf $(docs_path)
	rm -rf bin/*
	rm -rf pkg/*
	rm -rf jar/*

#variables
version = 2.0
cp = bin:lib/*
docscp = lib/*:
docs_path = javadoc
options =
#warnings = -Xlint:deprecation
#warnings = -Xlint:unchecked

#includes
include lists.mk
include dependencies.mk
include packaging.mk

#compilation definitions
$(class_files): bin/%.class : src/%.java
	javac -cp $(cp) -d bin $(warnings) $<

#command definitions
build: $(class_files)
build-base: $(base_class_files)

run: test

jar: $(jar_base)
jars: $(jar_files)
docs-test: docs
	chrome javadoc/index.html &
package: $(package_base)
packages: $(package_files)
package-test: package
	file-roller $(package_base) &> /dev/null &

#documentation
$(docs_path): $(source_files)
	rm -rf $(docs_path)
	javadoc -classpath $(docscp) \
		-d $(docs_path) $(source_files)
docs: $(docs_path)

#test commands
test: test-all

test-all: bin/kuro/json/test/TestJSONAdapter.class
	java -cp $(cp) kuro.json.test.TestJSONAdapter

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

test-tutorial: bin/kuro/json/tutorial/Tutorial.class
	java -cp $(cp) kuro.json.tutorial.Tutorial
