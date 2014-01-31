package kiwi.json.test;

//local imports
import kiwi.json.*;

//json-simple imports
import org.json.simple.*;
import org.json.simple.parser.*;

public class TestJSONAdapter {
	public static void main( String[] args){
		String data1 = "{ \"asdf\":[ 0, 1, 2, { \"fdsa\":\"hello\"}]}";
		String data2 = null;
		String data3 = "[0, 1, 2]";
		String request1 = ".asdf[3].fdsa";
		String request2 = "..";
		String request3 = ".";
		String request4 = "[0]";
		String request5 = "";

		System.out.println( "Test 1");
		test( data1, request1);
		System.out.println();

		System.out.println( "Test 2");
		test( data1, request2);
		System.out.println();

		System.out.println( "Test 3");
		test( data1, request3);
		System.out.println();

		System.out.println( "Test 4");
		test( data1, null);
		System.out.println();

		System.out.println( "Test 5");
		boolean pass7 = false;
		try{
			new JSONAdapter( null);}
		catch( NullPointerException exception){
			pass7 = true;
			System.out.println("Pass - null pointer exception recieved");}
		catch( Exception exception){
			exception.printStackTrace();}
		System.out.println();

		System.out.println( "Test 6");
		test( data3, request4);
		System.out.println();

		System.out.println( "Test 6");
		test( data1, request5);
		System.out.println();}

	public static void test( String data, String request){
		try{
			//load root object
			Object root = null;
			try {
				root = new JSONParser().parse( data);}
			catch( ParseException exception){
				System.out.println( "Parsing JSON file failed.");}

			//load adapter
			JSONAdapter adapter = new JSONAdapter( root);

			//get request
			System.out.printf( "data: %s\n", data);
			System.out.printf( "request: %s\n", request);
			System.out.printf( "result: %s\n", adapter.get( request));}
		catch( Exception exception){
			exception.printStackTrace();}}
}