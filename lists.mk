#lists
#class files list
class_files = \
	$(base_class_files) \
	$(tutorial_class_files) \
	$(test_class_files) \

#base class files list
base_class_files = \
	bin/kuro/json/JSONAdapter.class \

#test class files list
test_class_files = \
	bin/kuro/json/test/TestCastDerefs.class \
	bin/kuro/json/test/TestCastGets.class \
	bin/kuro/json/test/TestCasts.class \
	bin/kuro/json/test/TestContains.class \
	bin/kuro/json/test/TestDeref.class \
	bin/kuro/json/test/TestGet.class \
	bin/kuro/json/test/TestJSONAdapter.class \
	bin/kuro/json/test/TestSet.class \
	bin/kuro/json/test/TestTypeChecks.class \

#tutorial class files list
tutorial_class_files = \
	bin/kuro/json/tutorial/Tutorial.class \

#source files list
source_files = \
	src/kuro/json/JSONAdapter.java \
	src/kuro/json/test/TestCastGets.java \
	src/kuro/json/test/TestCasts.java \
	src/kuro/json/test/TestContains.java \
	src/kuro/json/test/TestGets.java \
	src/kuro/json/test/TestSets.java \
	src/kuro/json/test/TestTypeChecks.java \
	src/kuro/json/tutorial/Tutorial.java \
