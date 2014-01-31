package kiwi.json;

//standard library imports
import java.util.Vector;

//json-simple imports
import org.json.simple.*;

public class JSONAdapter {
	//private fields
	private Object root;

	//constructors
	public JSONAdapter( Object object){
		if( object == null)
			throw new NullPointerException(
				"JSONAdapter passed a null object");
		if( ( object instanceof JSONObject) || 
				( object instanceof JSONArray))
			root = object;
		else throw new ClassCastException(
			"JSONAdapter passed an object that was neither a json-simple object nor array.");}

	//main get function
	public JSONAdapter get( String tag){
		//setup
		Vector<Token> tokens = getTokens( tag);
		if( tokens == null ) return null;
		Object object = root;

		//for each token
		for( Token token : tokens){
			//try a json object cast
			if( object instanceof JSONObject){
				if( ! token.symbol) return null;
				JSONObject current = (JSONObject) object;
				object = current.get( token.name);}
			//try a json array cast
			else if( object instanceof JSONArray){
				if( token.symbol) return null;
				JSONArray current = (JSONArray) object;
				object = current.get( token.index);}
			//we cant go any deeper, something's gone wrong
			else return null;}
		return new JSONAdapter( object);}


	//utility methods
	public static Vector<Token> getTokens( String tag){
		//setup
		String content = new String( tag);
		Vector<Token> tokens = new Vector<Token>();
		//while there's more to parse
		while( content.length() > 0){
			Token token = null;
			char first = content.charAt( 0);
			String label = null;
			//find type.
			switch( first){
				case '.':{
					//parse the label
					label = new String();
					for( int i = 1; i < content.length(); i++){
						char current = content.charAt( i);
						if( current == '.'){
							//if the next character is an escapable char, escape it
							if( i + 1 >= content.length())
								break;
							char next = content.charAt( i + 1);
							if( next == '.' || next == '['){
								label += next;
								i++;}
							//otherwise we're done parsing the label
							else break;}
						else if( current == '[')
							break;
						else
							label += current;}
					//done parsing this label
					//throw exceptions!
					if( label.isEmpty()) break;
					//we're all good, make token
					token = new Token( label);
					break;}
				case '[':{
					try{
						int closing = content.indexOf( ']');
						if( closing < 1) break;
						label = content.substring( 1, closing);
						token = new Token( Integer.parseInt( label));}
					catch( NumberFormatException exception){}
					break;}
				default:
					break;}
			//throw exceptions!
			if( label == null) break;
			if( token == null) break;
			//remove token from substring
			content = content.substring(
				label.length() + ( token.symbol ? 1 : 2));
			System.out.printf( "%s : %s\n", label, content);
			tokens.add( token);}
		return tokens;}

	//subclasses
	public static class Token {
		public boolean symbol;
		public String name;
		public int index;

		public Token( String name){
			this.symbol = true;
			this.name = name;
			this.index = -1;}
		public Token( int index){
			this.symbol = false;
			this.name = null;
			this.index = index;}
	}
}