#packages
#vars
jar_base = jar/json-simpler-$(version).jar
jar_docs = jar/json-simpler-$(version)-docs.jar
jar_tests = jar/json-simpler-$(version)-tests.jar
jar_all = jar/json-simpler-$(version)-all.jar
package_base = pkg/json-simpler-$(version).tar.gz
package_all = pkg/json-simpler-$(version)-all.tar.gz

#jar files
jar_files = \
	$(jar_base) \
	$(jar_docs) \
	$(jar_all) \

$(jar_base): $(base_class_files)
	jar cf $@ -C bin kuro/json/

$(jar_docs): $(docs_path)
	jar cf $@ $(docs_path)

$(jar_all): $(class_files)
	jar cfe $@ \
		kuro.json.test.TestJSONAdapter \
		-C bin kuro

#packages
package_files = \
	$(package_base) \
	$(package_all) \

$(package_base): \
		$(jar_base) legal/gnu-lgpl-v3.0.md license.md
	tar -cf $@ \
		$(jar_base) legal/gnu-lgpl-v3.0.md license.md
$(package_all): \
		$(docs_path) $(jar_files) lib/json-simple-*.jar \
		data legal/gnu-lgpl-v3.0.md license.md readme.md \
		$(source_files)
	tar -cf $@ \
		$(docs_path) $(jar_files) lib/json-simple-*.jar \
		data legal/gnu-lgpl-v3.0.md license.md readme.md \
		$(source_files)