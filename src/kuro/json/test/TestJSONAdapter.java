package kuro.json.test;

//junit imports
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.Suite;

/**
 * Test suite for kuro.json.JSONAdapter
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestCastGets.class,
	TestCasts.class,
	TestContains.class,
	TestDeref.class,
	TestGets.class,
	TestSets.class,
	TestTypeChecks.class})
public class TestJSONAdapter {
	public static void main( String[] args){
		JUnitCore.runClasses( TestJSONAdapter.class);}	
}