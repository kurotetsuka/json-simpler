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
		boolean pass4 = false;
		try{
			test( data1, null);}
		catch( NullPointerException exception){
			pass4 = true;
			System.out.println("Pass - null pointer exception recieved");}
		catch( Exception exception){
			exception.printStackTrace();}
		System.out.println();

		System.out.println( "Test 5");
		boolean pass5 = false;
		System.out.println( new JSONAdapter( null));
		System.out.println();

		System.out.println( "Test 6");
		test( data3, request4);
		System.out.println();

		System.out.println( "Test 7");
		test( data1, request5);
		System.out.println();}

	public static void test( String data, String request){
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
		JSONAdapter result = adapter.get( request);
		System.out.printf( "result: (%s) %s\n",
			result.getObject().getClass().getName(),
			result);

		//try all the casts
		try{
			System.out.printf(
				"result as boolean: %b\n", result.getBoolean());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as double: %f\n", result.getDouble());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as float: %g\n", result.getFloat());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as integer: %d\n", result.getInteger());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as long: %d\n", result.getLong());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as string: %s\n", result.getString());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as json object: %s\n", result.getJSONObject());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as json array: %s\n", result.getJSONArray());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as object: %s\n", result.getObject());}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}

		//try all the cast gets
		try{
			System.out.printf(
				"result as boolean: %b\n",
				adapter.getBoolean( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as double: %f\n",
				adapter.getDouble( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as float: %g\n",
				adapter.getFloat( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as integer: %d\n",
				adapter.getInteger( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as long: %d\n",
				adapter.getLong( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as string: %s\n",
				adapter.getString( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as json object: %s\n",
				adapter.getJSONObject( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as json array: %s\n",
				adapter.getJSONArray( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}
		try{
			System.out.printf(
				"result as object: %s\n",
				adapter.getObject( request));}
		catch( ClassCastException exception){
			System.out.println( exception.getMessage());}}
}