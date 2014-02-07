package kiwi.json;

//standard library imports
import java.util.NoSuchElementException;
import java.util.Vector;

//json-simple imports
import org.json.simple.*;

public class JSONAdapter {
	//private fields
	private Object root;

	//constructors
	public JSONAdapter( Object object){
		if( ( object == null) ||
				( object instanceof JSONArray) ||
				( object instanceof JSONObject) ||
				( object instanceof Boolean) ||
				( object instanceof Double) ||
				( object instanceof Long) ||
				( object instanceof String))
			root = object;
		else throw new ClassCastException(
			"JSONAdapter passed an object that was not a json-simple type.");}

	//main get function
	public JSONAdapter get( int index){
		if( ! this.isJSONArray())
			throw new NoSuchElementException(
				"Root is not an array");
	return new JSONAdapter(((JSONArray) root).get( index));}
	public JSONAdapter get( String tag){
		//validation
		if( tag == null)
			return this;
		//setup
		String request;
		if( tag.length() > 0)
			//preppend a '.', if they probably wanted one
			if( tag.charAt( 0) != '.' ||
					tag.charAt( 0) != '[')
				request = '.' + tag;
			else request = tag;
		else
			//they sent an empty string?!
			return this;
		Vector<Token> tokens = getTokens( request);
		if( tokens == null )
			throw new NullPointerException(
				"getTokens() returned null?! Wtf?!");
		if( tokens.size() == 0)
			return this;
		Object object = root;

		//for each token
		for( Token token : tokens){
			//try a json object cast
			if( object instanceof JSONObject){
				if( ! token.symbol)
					throw new NoSuchElementException(
						String.format(
							"Could not find element %s in object %s", tag, this));
				JSONObject current = (JSONObject) object;
				object = current.get( token.name);}
			//try a json array cast
			else if( object instanceof JSONArray){
				if( token.symbol)
					throw new NoSuchElementException(
						String.format(
							"Could not find element %s in object %s", tag, this));
				JSONArray current = (JSONArray) object;
				object = current.get( token.index);}
			//we cant go any deeper, something's gone wrong
			else throw new NoSuchElementException(
				String.format(
					"Could not find element %s in object %s", tag, this));}
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
	public boolean isJSONArray(){
		return root instanceof JSONArray;}
	public boolean isJSONObject(){
		return root instanceof JSONObject;}

	//validation dereference functions
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
	public boolean isJSONArray( String tag){
		return get( tag).isJSONArray();}
	public boolean isJSONObject( String tag){
		return get( tag).isJSONObject();}

	//validation indexed functions
	public boolean isBoolean( int index){
		return get( index).isBoolean();}
	public boolean isDecimal( int index){
		return get( index).isDecimal();}
	public boolean isInteger( int index){
		return get( index).isInteger();}
	public boolean isNull( int index){
		return get( index).isNull();}
	public boolean isNumber( int index){
		return get( index).isNumber();}
	public boolean isString( int index){
		return get( index).isString();}
	public boolean isJSONArray( int index){
		return get( index).isJSONArray();}
	public boolean isJSONObject( int index){
		return get( index).isJSONObject();}

	//cast functions
	public boolean getBoolean(){
		if( isBoolean())
			return ( (Boolean) root).booleanValue();
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Boolean.class.getName()));}
	public Number getNumber(){
		if( isNumber())
			return (Number) root;
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Number.class.getName()));}
	public double getDouble(){
		if( isDecimal())
			return ( (Double) root).doubleValue();
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Double.class.getName()));}
	public float getFloat(){
		if( isDecimal())
			return ( (Double) root).floatValue();
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Float.class.getName()));}
	public int getInteger(){
		if( isInteger())
			return ( (Long) root).intValue();
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Integer.class.getName()));}
	public long getLong(){
		if( isInteger())
			return ( (Long) root).longValue();
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Long.class.getName()));}
	public String getString(){
		if( isString())
			return (String) root;
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				String.class.getName()));}
	public JSONArray getJSONArray(){
		if( isJSONArray())
			return (JSONArray) root;
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				JSONArray.class.getName()));}
	public JSONObject getJSONObject(){
		if( isJSONObject())
			return (JSONObject) root;
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				JSONObject.class.getName()));}
	public Object getObject(){
		return root;}

	//get dereference functions
	public boolean getBoolean( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getBoolean();}
	public Number getNumber( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getNumber();}
	public double getDouble( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getDouble();}
	public float getFloat( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getFloat();}
	public int getInteger( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getInteger();}
	public long getLong( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getLong();}
	public String getString( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getString();}
	public JSONArray getJSONArray( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getJSONArray();}
	public JSONObject getJSONObject( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getJSONObject();}
	public Object getObject( String tag){
		JSONAdapter adapter = this.get( tag);
		return adapter.getObject();}

	//get indexed functions
	public boolean getBoolean( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getBoolean();}
	public Number getNumber( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getNumber();}
	public double getDouble( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getDouble();}
	public float getFloat( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getFloat();}
	public int getInteger( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getInteger();}
	public long getLong( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getLong();}
	public String getString( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getString();}
	public JSONArray getJSONArray( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getJSONArray();}
	public JSONObject getJSONObject( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getJSONObject();}
	public Object getObject( int index){
		JSONAdapter adapter = this.get( index);
		return adapter.getObject();}

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