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
		if( ( object == null) ||
				( object instanceof JSONObject) || 
				( object instanceof JSONArray) || 
				( object instanceof String) || 
				( object instanceof Number) || 
				( object instanceof Boolean))
			root = object;
		else throw new ClassCastException(
			"JSONAdapter passed an object that was not a json-simple type.");}

	//main get function
	public JSONAdapter get( String tag){
		//validation
		if( tag == null)
			throw new NullPointerException();
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
			else throw new java.util.NoSuchElementException(
				String.format( "Could not find element %s", tag));}
		//we're done, return
		return new JSONAdapter( object);}

	//validation functions
	public boolean isBoolean(){
		return root instanceof Boolean;}
	public boolean isDecimal(){
		return root instanceof Double;}
	public boolean isInteger(){
		return root instanceof Long;}
	public boolean isNull(){
		return root == null;}
	public boolean isNumber(){
		return root instanceof Number;}
	public boolean isString(){
		return root instanceof String;}

	public boolean isBoolean( String tag){
		return get( tag).isBoolean();}
	public boolean isDecimal( String tag){
		return get( tag).isDecimal();}
	public boolean isInteger( String tag){
		return get( tag).isInteger();}
	public boolean isNull( String tag){
		return get( tag).isNull();}
	public boolean isNumber( String tag){
		return get( tag).isNumber();}
	public boolean isString( String tag){
		return get( tag).isString();}

	//validation functions
	public boolean getBoolean(){
		if( isBoolean())
			return ( (Boolean) root).booleanValue();
		else throw new ClassCastException(
			String.format(
				"%s cannot be cast to %s",
				root.getClass().getName(),
				Boolean.class.getName()));}
	public double getDouble(){
		if( isDecimal())
			return ( (Double) root).doubleValue();
		else throw new ClassCastException(
			String.format(
				"%s cannot be cast to %s",
				root.getClass().getName(),
				Double.class.getName()));}
	public float getFloat(){
		if( isDecimal())
			return ( (Float) root).floatValue();
		else throw new ClassCastException(
			String.format(
				"%s cannot be cast to %s",
				root.getClass().getName(),
				Float.class.getName()));}
	public int getInteger(){
		if( isInteger())
			return ( (Integer) root).intValue();
		else throw new ClassCastException(
			String.format(
				"%s cannot be cast to %s",
				root.getClass().getName(),
				Integer.class.getName()));}
	public long getLong(){
		if( isInteger())
			return ( (Long) root).longValue();
		else throw new ClassCastException(
			String.format(
				"%s cannot be cast to %s",
				root.getClass().getName(),
				Long.class.getName()));}
	public String getString(){
		if( isString())
			return (String) root;
		else throw new ClassCastException(
			String.format(
				"%s cannot be cast to %s",
				root.getClass().getName(),
				String.class.getName()));}

	public boolean getBoolean( String tag){
		JSONAdapter adapter = get( tag);
		return adapter.getBoolean();}
	public double getDouble( String tag){
		JSONAdapter adapter = get( tag);
		return adapter.getDouble();}
	public float getFloat( String tag){
		JSONAdapter adapter = get( tag);
		return adapter.getFloat();}
	public int getInteger( String tag){
		JSONAdapter adapter = get( tag);
		return adapter.getInteger();}
	public long getLong( String tag){
		JSONAdapter adapter = get( tag);
		return adapter.getLong();}
	public String getString( String tag){
		JSONAdapter adapter = get( tag);
		return adapter.getString();}

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
			//System.out.printf( "%s : %s\n", label, content);
			tokens.add( token);}
		return tokens;}

	//accessor methods
	public String toString(){
		return root == null ?
			"null" :
			root.toString();}

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