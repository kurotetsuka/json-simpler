package kiwi.json.test;

//standard libray imports
import java.util.NoSuchElementException;

//json-simple imports
import org.json.simple.*;
import org.json.simple.parser.*;

//local imports
import kiwi.json.*;

public class TestJSONAdapter {
	public static void main( String[] args){
		System.out.println();
		//datum
		String data0 = "{ \"asdf\":[ 0, 1, 2, { \"fdsa\":\"hello\"}]}";
		String data1 = "[0, 1, 2]";
		String data2 = "{ \"a.d\":10}";
		String data3 = null;
		//requests
		String request0 = ".asdf[3].fdsa";
		String request1 = "..";
		String request2 = ".";
		String request3 = "[0]";
		String request4 = "";
		String request5 = "a..d";
		String request6 = null;
		//expected
		Object expected00 = null;
		Object expected01 = null;
		Object expected02 = null;
		Object expected03 = null;
		Object expected04 = null;
		Object expected10 = null;
		Object expected11 = null;
		Object expected12 = null;
		Object expected13 = null;
		Object expected14 = null;
		Object expected20 = null;
		Object expected21 = null;
		Object expected22 = null;
		Object expected23 = null;
		Object expected24 = null;
		//arrays
		String[] datum = new String[]{
			data0, data1, data2, data3};
		String[] requests = new String[]{
			request0, request1, request2, request3, request4, request5,
			request6};
		Object[][] expected = {
			{ expected00, expected01, expected02, expected03, expected04},
			{ expected10, expected11, expected12, expected13, expected14},
			{ expected20, expected21, expected22, expected23, expected24}};

		//do the actual tests
		for( int datum_i = 0; datum_i < datum.length; datum_i ++)
			for( int requests_i = 0; requests_i < requests.length; requests_i ++)
				try{
					test(
						datum[ datum_i], requests[ requests_i],
						//expected[ datum_i][ requests_i],
						datum_i, requests_i);}
				catch( Exception exception){
					exception.printStackTrace();}

		//done
		System.out.println( "Done all tests.");}

	public static Object test(
			String data, String request,
			int datum_i, int request_i){
		//load root object
		Object root = null;
		try {
			root = new JSONParser().parse( data);}
		catch( ParseException exception){
			System.out.println( "Parsing JSON file failed.");}
		catch( NullPointerException exception){}

		//load adapter
		JSONAdapter adapter = new JSONAdapter( root);

		//get request
		System.out.printf( "data [%d] : %s\n", datum_i, data);
		System.out.printf( "request [%d] : %s\n", request_i, request);
		JSONAdapter result = null;
		Object result_object = null;
		try{
			result = adapter.get( request);
			result_object = result.getObject();}
		catch( NoSuchElementException exception){
			System.out.println( exception.getMessage());}
		Class result_class = null;
		String result_string = null;
		if( result_object == null)
			System.out.println( "result: null");
		else {
			result_class = result_object.getClass();
			result_string = result.toString();
			System.out.printf( "result: (%s) %s\n",
				result_class.getName(), result);}

		//try all the casts
		//boolean cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				boolean result_boolean = result.getBoolean();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				boolean result_boolean = adapter.getBoolean( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, Boolean.class,
					cast_success,  castGet_success);}
		//number cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				Number result_number = result.getNumber();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				Number result_number = adapter.getNumber( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, Number.class,
					cast_success,  castGet_success);}
		//double cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				double result_double = result.getDouble();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				double result_double = adapter.getDouble( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, Double.class,
					cast_success,  castGet_success);}
		//float cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				float result_float = result.getFloat();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				float result_float = adapter.getFloat( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, Float.class,
					cast_success,  castGet_success);}
		//integer cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				int result_int = result.getInteger();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				int result_int = adapter.getInteger( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, Integer.class,
					cast_success,  castGet_success);}
		//long cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				long result_long = result.getLong();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				long result_long = adapter.getLong( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, Long.class,
					cast_success,  castGet_success);}
		//string cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				String result_String = result.getString();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				String result_String = adapter.getString( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, String.class,
					cast_success,  castGet_success);}
		//json array cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				JSONArray result_jsonArray = result.getJSONArray();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				JSONArray result_jsonArray = adapter.getJSONArray( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, JSONArray.class,
					cast_success,  castGet_success);}
		//json object cast
		{
			boolean cast_success = false;
			boolean castGet_success = false;
			try{
				JSONObject result_jsonObject = result.getJSONObject();
				cast_success = true;}
			catch( ClassCastException exception){}
			catch( NullPointerException exception){
				if( result != null)
					exception.printStackTrace();}
			try{
				JSONObject result_jsonObject = adapter.getJSONObject( request);
				castGet_success = true;}
			catch( ClassCastException exception){}
			catch( NoSuchElementException exception){
				System.out.println( exception.getMessage());}
			if( result != null)
				failMessage(
					result_string, result_class, JSONObject.class,
					cast_success,  castGet_success);}

		//done
		System.out.println();
		return result_object;}

	private static void failMessage(
			String result_string, Class<?> result_class, Class<?> cast_class,
			boolean cast_success, boolean castGet_success){
		boolean class_match =
			result_class != null && cast_class != null ?
				cast_class.isAssignableFrom( result_class) :
				false;
		//check the cast
		if( class_match ^ cast_success)
			System.out.printf(
				"[Fail] Cast of %s to %s %s\n",
				result_string, cast_class.getName(),
				//determine fail type
				cast_success ?
					"succeeded when it should have failed." :
					"failed when it should have succeeded.");
		//check the cast get
		if( class_match ^ castGet_success)
			System.out.printf(
				"[Fail] Cast get of %s to %s %s\n",
				result_string, cast_class.getName(),
				//determine fail type
				castGet_success ?
					"succeeded when it should have failed." :
					"failed when it should have succeeded.");}
}