package kiwi.json.tutorial;

//standard library imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Scanner;

//json-simple imports
import org.json.simple.*;
import org.json.simple.parser.*;

//json-simpler imports
import kiwi.json.JSONAdapter;

public class Tutorial {
	public static void main( String[] args){
		//
		String filename = "data/tutorial.json";
		File file = new File( filename);

		//first load your data
		String data = new String();
		try {
			BufferedReader reader = new BufferedReader(
				new FileReader( file));
			while( reader.ready())
				data += reader.readLine() + '\n';
			reader.close();}
		catch( Exception exception){
			exception.printStackTrace();
			return;}

		//parse your data to a JSONObject (or JSONArray)
		Object root = null;
		try {
			root = new JSONParser().parse( data);}
		catch( ParseException exception){
			System.out.println("Your file was invalid!");
			exception.printStackTrace();
			return;}

		//give your root object to my JSONAdapter
		JSONAdapter adapter = new JSONAdapter( root);

		//now you can easily read your data!
		String name = adapter.getString( ".name");
		int age = adapter.getInteger( ".age");
		boolean likesCake = adapter.getBoolean( ".likes-cake");

		//you can use dot-dereferencing notation to get sub-objects
		float weight_kilos = adapter.getFloat( ".weight.kilos");
		//alternative you can get adapters for sub-objects
		JSONAdapter weight_adapter = adapter.get( ".weight");
		double weight_pounds = weight_adapter.getDouble( ".pounds");

		//you can use array indexing notation to get array elements
		Date birthday = new Date(
			adapter.getInteger( ".birthday[0]"),
			adapter.getInteger( ".birthday[1]"),
			adapter.getInteger( ".birthday[2]"));
		//just like with sub-objects, you can get adapters for arrays
		JSONAdapter deathday_adapter = adapter.get( ".deathday");
		//then use integers for gets
		Date deathday = new Date(
			deathday_adapter.getInteger( 0),
			deathday_adapter.getInteger( 1),
			deathday_adapter.getInteger( 2));}

}