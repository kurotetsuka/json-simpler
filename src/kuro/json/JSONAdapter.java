package kuro.json;

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
		//type check
		if( isJSONSimpleType( object))
			root = object;
		//throw error
		else throw new IllegalArgumentException(
			"JSONAdapter passed an object that was not a json-simple type");}

	//get functions
	//raw get
	public Object get(){
		return root;}
	//array get
	public JSONAdapter get( int index){
		//validation
		if( ! this.isJSONArray())
			throw new NoSuchElementException(
				"JSONAdapter root object is not an json array");
		if( ! this.containsIndex( index))
			throw new NoSuchElementException(
				String.format( "JSONAdapter does not contain index: %s", index));
		//get desired object
		Object result = ( (JSONArray) root).get( index);
		//create and return adapter for desired object
		return new JSONAdapter( result);}
	//object get
	public JSONAdapter get( String tag){
		//validation
		if( ! this.isJSONObject())
			throw new NoSuchElementException(
				"JSONAdapter root object is not a json object");
		if( ! this.containsKey( tag))
			throw new NoSuchElementException(
				String.format( "JSONAdapter does not contain key: %s", tag));
		//get desired object
		Object result = ( (JSONObject) root).get( tag);
		//create and return adapter for desired object
		return new JSONAdapter( result);}
 	//multi-level object get
	public JSONAdapter get( String[] tags){
		//validation
		if( ! this.isJSONObject())
			throw new NoSuchElementException(
				"JSONAdapter root object is not a json object");
		if( tags.length == 0)
			throw new IllegalArgumentException(
				"Tags array must not be empty");
		//walk through the object hierarchy
		JSONAdapter adapter = this;
		for( String tag : tags)
			adapter = adapter.get( tag);
		//return adapter for desired object
		return adapter;}

	//set functions
	//array set
	public Object set( int index, Object value){
		//validation
		if( ! this.isJSONArray())
			throw new ClassCastException(
				"JSONAdapter root object is not an array");
		if( ! isJSONSimpleType( value))
			throw new IllegalArgumentException(
				"JSONAdapter passed an object that was not a json-simple type");
		//apply the set
		JSONArray array = this.getJSONArray();
		return array.set( index, value);}

	//object set
	public Object set( String tag, Object value){
		//validation
		if( ! this.isJSONObject())
			throw new ClassCastException(
				"JSONAdapter root object is not a json object");
		if( ! isJSONSimpleType( value))
			throw new IllegalArgumentException(
				"JSONAdapter passed an object that was not a json-simple type");
		//apply the set
		JSONObject object = this.getJSONObject();
		return object.put( tag, value);}
	//multi-level object set
	public Object set( String[] tags, Object value){
		//validation
		if( ! this.isJSONObject())
			throw new ClassCastException(
				"JSONAdapter root object is not a json object");
		if( ! isJSONSimpleType( value))
			throw new IllegalArgumentException(
				"JSONAdapter passed an object that was not a json-simple type");
		if( tags.length == 0)
			throw new IllegalArgumentException(
				"Tags array must not be empty");
		//setup
		int i = 0;
		int last_i = tags.length - 1;
		JSONAdapter adapter = this;
		//find the highest existing object in the desired structure
		for( String tag : tags){
			if( i == last_i) break;
			if( adapter.containsKey( tag)){
				JSONAdapter next = adapter.get( tag);
				if( ! next.isJSONObject())
					break;
				else
					adapter = next;}
			else break;}
		//build the rest of the desired structure
		while( i < last_i){
			String tag = tags[ i];
			adapter.set( tag, new JSONObject());
			adapter = adapter.get( tag);
			i++;}
		//put the desired value in the specified place
		String last_tag = tags[ last_i];
		return adapter.set( last_tag, value);}

	//deref functions
	public JSONAdapter deref( String reference){
		//validation
		if( reference == null)
			throw new NullPointerException();
		if( reference.isEmpty())
			throw new IllegalArgumentException(
				"Reference string must not be empty");
		//tokenize reference string
		Vector<Token> tokens = Token.parse( reference);
		//walk down token path
		JSONAdapter adapter = this;
		for( int i = 0; i < tokens.size(); i++){
			Token token = tokens.get( i);
			try{
				adapter = token.isName() ?
					adapter.get( token.name) :
					adapter.get( token.index);}
			catch( Exception exception){
				throw new NoSuchElementException(
					String.format(
						"Could not dereference %s from this%s",
						token.toString(), Token.toString( tokens, i)));}}
		//return adapter for desired object
		return adapter;}

	//contains functions
	public boolean containsIndex( int index){
		//validate type
		if( this.isJSONArray()){
			JSONArray array = this.getJSONArray();
			return index >= 0 && index < array.size();}
		//we're not an array, so we cannot contain an index
		else return false;}
	public boolean containsKey( String key){
		//validate type
		if( this.isJSONObject()){
			JSONObject object = this.getJSONObject();
			return object.containsKey( key);}
		//we're not an object, so we cannot contain a key
		else return false;}
	public boolean containsValue( Object value){
		//we're an object
		if( this.isJSONObject()){
			JSONObject object = this.getJSONObject();
			return object.containsValue( value);}
		//we're an array
		if( this.isJSONArray()){
			JSONArray array = this.getJSONArray();
			return array.contains( value);}
		//we're not an object nor array, so we cannot contain a vlue
		else return false;}

	//type check functions
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

	//object type check functions
	public boolean isBoolean( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return type check
		return adapter.isBoolean();}
	public boolean isDecimal( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return type check
		return adapter.isDecimal();}
	public boolean isInteger( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return type check
		return adapter.isInteger();}
	public boolean isNull( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return type check
		return adapter.isNull();}
	public boolean isNumber( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return type check
		return adapter.isNumber();}
	public boolean isString( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return type check
		return adapter.isString();}
	public boolean isJSONArray( String tag){
	//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return type check
		return adapter.isJSONArray();}
	public boolean isJSONObject( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return type check
		return adapter.isJSONObject();}

	//array type check functions
	public boolean isBoolean( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return type check
		return adapter.isBoolean();}
	public boolean isDecimal( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return type check
		return adapter.isDecimal();}
	public boolean isInteger( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return type check
		return adapter.isInteger();}
	public boolean isNull( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return type check
		return adapter.isNull();}
	public boolean isNumber( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return type check
		return adapter.isNumber();}
	public boolean isString( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return type check
		return adapter.isString();}
	public boolean isJSONArray( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return type check
		return adapter.isJSONArray();}
	public boolean isJSONObject( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return type check
		return adapter.isJSONObject();}

	//cast functions
	public boolean getBoolean(){
		//validate type
		if( isBoolean())
			return ( (Boolean) root).booleanValue();
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Boolean.class.getName()));}
	public Number getNumber(){
		//validate type
		if( isNumber())
			return (Number) root;
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Number.class.getName()));}
	public double getDouble(){
		//validate type
		if( isDecimal())
			return ( (Double) root).doubleValue();
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Double.class.getName()));}
	public float getFloat(){
		//validate type
		if( isDecimal())
			return ( (Double) root).floatValue();
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Float.class.getName()));}
	public int getInteger(){
		//validate type
		if( isInteger())
			return ( (Long) root).intValue();
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Integer.class.getName()));}
	public long getLong(){
		//validate type
		if( isInteger())
			return ( (Long) root).longValue();
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				Long.class.getName()));}
	public String getString(){
		//validate type
		if( isString())
			return (String) root;
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				String.class.getName()));}
	public JSONArray getJSONArray(){
		//validate type
		if( isJSONArray())
			return (JSONArray) root;
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				JSONArray.class.getName()));}
	public JSONObject getJSONObject(){
		//validate type
		if( isJSONObject())
			return (JSONObject) root;
		//throw error
		else throw new ClassCastException(
			String.format( "%s cannot be cast to %s",
				root == null ? "null" : root.getClass().getName(),
				JSONObject.class.getName()));}
	public Object getObject(){
		return root;}

	//object get cast functions
	public boolean getBoolean( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getBoolean();}
	public Number getNumber( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getNumber();}
	public double getDouble( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getDouble();}
	public float getFloat( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getFloat();}
	public int getInteger( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getInteger();}
	public long getLong( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getLong();}
	public String getString( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getString();}
	public JSONArray getJSONArray( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getJSONArray();}
	public JSONObject getJSONObject( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getJSONObject();}
	public Object getObject( String tag){
		//get adapter for tag
		JSONAdapter adapter = this.get( tag);
		//return cast
		return adapter.getObject();}

	//array get cast functions
	public boolean getBoolean( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getBoolean();}
	public Number getNumber( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getNumber();}
	public double getDouble( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getDouble();}
	public float getFloat( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getFloat();}
	public int getInteger( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getInteger();}
	public long getLong( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getLong();}
	public String getString( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getString();}
	public JSONArray getJSONArray( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getJSONArray();}
	public JSONObject getJSONObject( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getJSONObject();}
	public Object getObject( int index){
		//get adapter for index
		JSONAdapter adapter = this.get( index);
		//return cast
		return adapter.getObject();}

	//object get cast functions
	public boolean derefBoolean( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getBoolean();}
	public Number derefNumber( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getNumber();}
	public double derefDouble( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getDouble();}
	public float derefFloat( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getFloat();}
	public int derefInteger( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getInteger();}
	public long derefLong( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getLong();}
	public String derefString( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getString();}
	public JSONArray derefJSONArray( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getJSONArray();}
	public JSONObject derefJSONObject( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getJSONObject();}
	public Object derefObject( String reference){
		//deref adapter for reference
		JSONAdapter adapter = this.deref( reference);
		//return cast
		return adapter.getObject();}

	//utility functions
	public static boolean isJSONSimpleType( Object object){
		 return (
	 		( object == null) ||
			( object instanceof JSONArray) ||
			( object instanceof JSONObject) ||
			( object instanceof Boolean) ||
			( object instanceof Double) ||
			( object instanceof Long) ||
			( object instanceof String));}

	//accessors
	@Override
	public String toString(){
		return root == null ?
			"null" : root.toString();}

	public int size(){
		//validate type
		if( this.isJSONArray())
			return ( (JSONArray) root).size();
		if( this.isJSONObject())
			return ( (JSONObject) root).size();
		//throw error
		else
			throw new ClassCastException(
				String.format( "Cannot get the size of a %s",
					root.getClass().getName()));}

	//subclasses
	/**
	 * Just a little utility class to make dereferencing easier
	 */
	public static class Token {
		//whether this token is a name
		private boolean symbol;
		//the name of this token
		public String name;
		//the index of this token
		public int index;

		//constructors
		public Token( String name){
			this.symbol = true;
			this.name = name;
			this.index = -1;}
		public Token( int index){
			this.symbol = false;
			this.name = null;
			this.index = index;}

		//Accessors
		public boolean isName(){
			return symbol;}
		public boolean isIndex(){
			return ! symbol;}

		public String toString(){
			return String.format(
				symbol ? ".%s" : "[%d]",
				symbol ? name : index);}

		//utility function
		public static Vector<Token> parse( String reference){
			//setup
			Vector<Token> result = new Vector<Token>();
			char[] chars = reference.toCharArray();
			int begin = 0, end = 0;

			//while we have unparsed characters left
			while( begin < chars.length)
				switch( chars[begin]){

					//we're indexing an array
					case '[':{
						end = reference.indexOf( ']', begin);
						//check that bracket was matched
						if( end < 0)
							throw new IllegalArgumentException(
								String.format(
									"Unmatched bracket in reference string at index %d", begin));
						//get and try to parse index string
						String index_string = reference.substring( begin + 1, end);
						int index;
						try{
							index = Integer.parseInt( index_string);}
						//catch bad index string
						catch( NumberFormatException exception){
							throw new IllegalArgumentException(
								String.format(
									"Invalid number in reference string at index %d",
									begin + 1),
								exception);}
						//assert non-negativity
						if( index < 0)
							throw new IllegalArgumentException(
								String.format(
									"Negative number in reference string at index %d",
									begin + 1));
						//add the token
						result.add( new Token( index));
						//increment begin index and iterate
						begin = end + 1;
						break;}

					//we've got a symbol with a preceding dot
					case '.':{
						begin++;
						if( begin >= chars.length)
							throw new IllegalArgumentException(
								"Orphaned '.' at end of reference string");}

					//we've got a symbol
					default:{
						//find end of current token
						int next_dot = reference.indexOf( '.', begin);
						if( next_dot < 0) next_dot = reference.length();
						int next_bracket = reference.indexOf( '[', begin);
						if( next_bracket < 0) next_bracket = reference.length();
						end = Math.min( next_dot, next_bracket);
						//get token string
						String token_string = reference.substring( begin, end);
						//check for emptiness
						if( token_string.isEmpty())
							throw new IllegalArgumentException(
								String.format(
									"Empty token in reference string at index %d",
									begin - 1));
						//add the token
						result.add( new Token( token_string));
						//increment the begin index and iterate
						begin = end;
						break;}}
			//return
			return result;}

		//string casts
		public static String toString( Vector<Token> tokens){
			String result = new String();
			for( Token token : tokens)
				result += token.toString();
			return result;}
		public static String toString( Vector<Token> tokens, int end){
			String result = new String();
			for( int i = 0; i < end && i < tokens.size(); i++)
				result += tokens.get(i).toString();
			return result;}
	}
}
