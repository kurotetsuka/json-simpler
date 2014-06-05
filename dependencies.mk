#dependencies

#json package
bin/kuro/json/JSONAdapter.class:

#test package
bin/kuro/json/test/TestGets.class: \
	bin/kuro/json/JSONAdapter.class
bin/kuro/json/test/TestSets.class: \
	bin/kuro/json/JSONAdapter.class

#tutorial package
bin/kuro/json/tutorial/Tutorial.class: \
	bin/kuro/json/JSONAdapter.class
