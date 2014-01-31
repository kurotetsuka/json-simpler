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
jar_file = jar/json-simpler.jar
version = 
warnings = 
#warnings = -Xlint:deprecation

#includes
include lists.mk
include dependencies.mk

#compilation definitions
$(class_files): bin/%.class : src/%.java
	javac $(cp) $(dest) $(version) $(warnings) $<

#commands
build: $(class_files)

run: test

jar: $(jar_file)
jar-test: jar
	java -jar $(jar_file)
$(jar_file): build manifest.mf
	rm -rf $(jar_file)
	jar cmf manifest.mf $@ -C bin kiwi

#other commands
git-prepare:
	git add -A
	git add -u

#test commands
test: test-jsonadapter

test-jsonadapter: bin/kiwi/json/test/TestJSONAdapter.class
	java $(cp) kiwi.json.test.TestJSONAdapter
