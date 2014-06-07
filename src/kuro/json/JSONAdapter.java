package kuro.json;

//standard library imports
import java.util.NoSuchElementException;
import java.util.Vector;

//json-simple imports
import org.json.simple.*;

public class JSONAdapter {
	//private fields
	private Object root;
	private static final boolean debug = false;

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

	//get functions
	public Object get(){
		return root;}
	public JSONAdapter get( int index){
		if( ! this.isJSONArray())
			throw new NoSuchElementException(
				"Root is not an array");
		return new JSONAdapter(((JSONArray) root).get( index));}
	public JSONAdapter get( String tag){
		//validation
		if( tag == null)
			//this is not an error, this is indended behavior.
			return this;

		//setup
		//figure out request
		String request;
		if( tag.length() > 0)
			//preppend a '.', if they probably wanted one
			if( tag.charAt( 0) != '.' &&
					tag.charAt( 0) != '[')
				request = '.' + tag;
			else request = tag;
		else
			//they sent an empty string?!
			return this;
		//debug print statement
		if( debug)
			System.out.printf( "request: [%s]\n", request);

		//get tokens
		Vector<Token> tokens = getTokens( request);
		//getTokens should never return null
		if( tokens == null )
			throw new NullPointerException(
				"getTokens() returned null?! Wtf?!");
		if( tokens.size() == 0)
			return this;

		//start at root of the tree
		Object object = root;
		//for each token
		for( Token token : tokens){
			//debug print statement
			if( debug)
				System.out.printf( "token: [%b, %s]\n",
					token.symbol, token.symbol ? token.name :
						String.valueOf( token.index));

			//try a json object cast
			if( object instanceof JSONObject){
				//check that this step in the request is dereferencing an object
				if( ! token.symbol)
					throw new NoSuchElementException( String.format(
						"Could not find element %s in %d", token.index, object));
				//cast the current object
				JSONObject current = (JSONObject) object;
				//check object contains key
				if( ! current.containsKey( token.name))
					throw new NoSuchElementException( String.format(
						"Could not find element %s in %s", token.name, object));
				//get key
				object = current.get( token.name);}

			//try a json array cast
			else if( object instanceof JSONArray){
				//check that this step in the request is indexing an array
				if( token.symbol)
					throw new NoSuchElementException( String.format(
						"Could not find element %s in %s", token.name, object));
				//cast the current object
				JSONArray current = (JSONArray) object;
				//get indexed object
				try {
					object = current.get( token.index);}
				//catch invalid index
				catch( IndexOutOfBoundsException exception){
					throw new NoSuchElementException( String.format(
						"Could not find index %d in %s", token.index, object));}}

			//all possible casts failed
			//we cant go any deeper, something's gone wrong
			else throw new NoSuchElementException( String.format(
				"Could not find element %s in %s", tag, object));

			if( debug)
				System.out.printf( "result: [%s]", object);}
		//we're done, return
		return new JSONAdapter( object);}

	//set functions
	//array set function
	public Object set( int index, Object object){
		if( ! this.isJSONArray())
			throw new NoSuchElementException(
				"Root is not an array");
		JSONArray array = (JSONArray) root;
		return array.set( index, object);}

	//object set function
	public Object set( String tag, Object object){
		//validation
		if( tag == null)
			//this is not an error, this is indended behavior.
			return this;

		//setup
		//figure out request
		String request;
		if( tag.length() > 0)
			//preppend a '.', if they probably wanted one
			if( tag.charAt( 0) != '.' &&
					tag.charAt( 0) != '[')
				request = '.' + tag;
			else request = tag;
		else
			//they sent an empty string?!
			return this;
		//debug print statement
		if( debug)
			System.out.printf( "request: [%s]\n", request);

		//get tokens
		Vector<Token> tokens = getTokens( request);
		//getTokens should never return null
		if( tokens == null )
			throw new NullPointerException(
				"getTokens() returned null?! Wtf?!");
		if( tokens.size() == 0)
			return this;

		//start at root of the tree
		JSONAdapter adapter = this;
		//for each token
		int i;
		for( i = 0; i < tokens.size() - 1; i++){
			Token token = tokens.get( i);
			//debug print statement
			if( debug)
				System.out.printf( "token: [%b, %s]\n",
					token.symbol, token.symbol ? token.name :
						String.valueOf( token.index));
			if( token.symbol)
				if( ! adapter.isJSONObject())
					throw new NoSuchElement

			else
				if( ! adapter.isJSONArray());}


		return null;}

	//contains functions
	public boolean containsIndex( int index){
		if( this.isJSONArray()){
			JSONArray array = this.getJSONArray();
			return index >= 0 && index < array.size();}
		else return false;}
	public boolean containsKey( String key){
		if( this.isJSONObject()){
			JSONObject object = this.getJSONObject();
			return object.containsKey( key);}
		else return false;}
	public boolean containsValue( Object value){
		if( this.isJSONObject()){
			JSONObject object = this.getJSONObject();
			return object.containsValue( value);}
		else return false;}

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
			int escapes = 0;
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
								escapes++;
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
				label.length() + ( token.symbol ? 1 : 2) + escapes);
			tokens.add( token);}
		return tokens;}

	//accessor methods
	public String toString(){
		return root == null ?
			"null" :
			root.toString();}

	public int size(){
		if( this.isJSONArray())
			return ( (JSONArray) root).size();
		if( this.isJSONObject())
			return ( (JSONObject) root).size();
		else
			throw new ClassCastException(
				String.format( "Cannot get the size of a %s",
					root.getClass().getName()));}

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

		public String toString(){
			return symbol ? name :
				String.valueOf( index);}
	}
}