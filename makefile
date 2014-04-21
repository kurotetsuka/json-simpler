#globals
default: build
freshen: clean build
clean:
	rm -rf $(docs_path)
	rm -rf bin/*
	rm -rf pkg/*
	rm -rf jar/*

#variables
version = 1.0.1a1
cp = bin:lib/*
dest = -d bin
docscp = lib/*
docs_path = javadoc
jar_file = jar/json-simpler-$(version).jar
package_file = pkg/json-simpler-$(version).tar.gz
options =
warnings =
#warnings = -Xlint:deprecation

#includes
include lists.mk
include dependencies.mk

#compilation definitions
$(class_files): bin/%.class : src/%.java
	javac -cp $(cp) $(dest) $(warnings) $<

#commands
build: $(class_files)

run: test

$(jar_file): $(class_files) manifest.mf
	rm -rf jar/*
	jar cmf manifest.mf $@ -C bin kiwi
jar: $(jar_file)
jar-test: jar
	java -jar $(jar_file)

$(docs_path): $(source_files)
	rm -rf $(docs_path)
	javadoc -classpath $(docscp) -d $(docs_path) $(source_files)
docs: $(docs_path)
docs-test: docs
	chromium javadoc/index.html &

$(package_file): \
		$(class_files) $(jar_file) $(docs_path) \
		readme.md license.md
	tar -cf $(package_file) $(docs_path) $(jar_file) readme.md license.md
package: $(package_file)
package-test: package
	file-roller $(package_file) &

#other commands
git-prepare:
	git add -A
	git add -u

#test commands
test: test-tutorial

test-jsonadapter: bin/kiwi/json/test/TestJSONAdapter.class
	java -cp $(cp) kiwi.json.test.TestJSONAdapter

test-tutorial: bin/kiwi/json/tutorial/Tutorial.class
	java -cp $(cp) kiwi.json.tutorial.Tutorial
