#dependencies

#json package
bin/kuro/json/JSONAdapter.class:

#test package
bin/kuro/json/test/TestJSONAdapter.class: \
	bin/kuro/json/test/TestCastGets.class \
	bin/kuro/json/test/TestCasts.class \
	bin/kuro/json/test/TestContains.class \
	bin/kuro/json/test/TestGets.class \
	bin/kuro/json/test/TestSets.class \
	bin/kuro/json/test/TestTypeChecks.class

bin/kuro/json/test/TestCastGets.class: \
	bin/kuro/json/JSONAdapter.class

bin/kuro/json/test/TestCasts.class: \
	bin/kuro/json/JSONAdapter.class

bin/kuro/json/test/TestContains.class: \
	bin/kuro/json/JSONAdapter.class

bin/kuro/json/test/TestGets.class: \
	bin/kuro/json/JSONAdapter.class

bin/kuro/json/test/TestSets.class: \
	bin/kuro/json/JSONAdapter.class

bin/kuro/json/test/TestTypeChecks.class: \
	bin/kuro/json/JSONAdapter.class

#tutorial package
bin/kuro/json/tutorial/Tutorial.class: \
	bin/kuro/json/JSONAdapter.class
