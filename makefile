#globals
default: build
freshen: clean build
clean: clean-specials
	rm -rf bin/*
clean-specials:
	rm -rf $(jar_file)

#variables
cp = -cp src:bin:lib/*
dest = -d bin
docscp = -classpath src:bin:lib/*
documentation = -d javadoc
version = 1.0.0b6
jar_file = jar/json-simpler-$(version).jar
options = -g:vars
warnings =
#warnings = -Xlint:deprecation

#includes
include lists.mk
include dependencies.mk

#compilation definitions
$(class_files): bin/%.class : src/%.java
	javac $(cp) $(options) $(dest) $(warnings) $<

#commands
build: $(class_files)

run: test

jar: $(jar_file)
jar-test: jar
	java -jar $(jar_file)
$(jar_file): build manifest.mf
	rm -rf jar/*
	jar cmf manifest.mf $@ -C bin kiwi

javadoc: $(source_files)
	rm -rf javadoc
	javadoc $(docscp) $(documentation) $(source_files)
docs: javadoc
docs-test: docs
	chromium-browser javadoc/index.html

#other commands
git-prepare:
	git add -A
	git add -u

#test commands
test: test-tutorial

test-jsonadapter: bin/kiwi/json/test/TestJSONAdapter.class
	java $(cp) kiwi.json.test.TestJSONAdapter

test-tutorial: bin/kiwi/json/tutorial/Tutorial.class
	java $(cp) kiwi.json.tutorial.Tutorial
