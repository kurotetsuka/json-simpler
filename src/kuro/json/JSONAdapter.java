package kuro.json;

//standard library imports
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

//json-simple imports
import org.json.simple.*;

public class JSONAdapter {
	//private fields
	private Object root;

	//constructors
	/**
	 * Create a new adapter to a json-simple type. These valid types include the following classes and any of their extentions: JSONArray, JSONObject, Boolean, Double, Long, String, null.
	 * 
	 * @param object The desired root object of the adapter. Must be a valid json-simple type.
	 * @throws IllegalArgumentException if the passed object was not a valid json-simple type.
	 */
	public JSONAdapter( Object root){
		//type check
		if( isJSONSimpleType( root))
			this.root = root;
		//throw error
		else throw new IllegalArgumentException(
			"JSONAdapter passed an object that was not a json-simple type");}

	//get functions
	//raw get
	/**
	 * Get the root object of this adapter.
	 * @return the root object of this adapter
	 */
	public Object get(){
		return root;}

	//array get
	/**
	 * Get an element from this adapter's root object. The root object must be a json-simple array.
	 * 
	 * @param index the index of the desired element
	 * @return the element at the desired index
	 * @throws NoSuchElementException if the root object does not contain the desired element ( this may be due to the root object not being a json array )
	 */
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
	/**
	 * Get a field from this adapter's root object. The root object must be a json-simple object.
	 * 
	 * @param tag the field key of the desired field
	 * @return the value of desired field
	 * @throws NoSuchElementException if the root object does not contain the desired field ( this may be due to the root object not being a json object )
	 */
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
 	/**
 	 * Get a nested field from this adapter's root object. The root object and every child along the path must be json-simple objects.
 	 *
 	 * This function is simply a shortcut for doing multiple gets in one call. Example: <code>adapter.get("foo").get("bar")</code> is congruent to <code>adapter.get( new String[]{"foo", "bar"})</code>.
 	 * 
 	 * @param tags the path to the desired object
 	 * @return an adapter for the object at the desired path
	 * @throws IllegalArgumentException if the tags array is empty or null
	 * @throws NoSuchElementException if the desired object cannot be found
 	 */
	public JSONAdapter get( String[] tags){
		//validation
		if( ! this.isJSONObject())
			throw new NoSuchElementException(
				"JSONAdapter root object is not a json object");
		if( tags == null || tags.length == 0)
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
	/**
	 * Change an element of this adapter's root object. The root object must be a json-simple array.
	 * 
	 * @param index the index of the element that should be changed
	 * @param value the new desired value of the element
	 * @return the previous value of the changed element
	 */
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
	/**
	 * Change a field of this adapter's root object. The root object must be a json-simple object.
	 * 
	 * @param tag the key of the field that should be changed
	 * @param value the new desired value of the field
	 * @return the previous value of the changed field, or null if the field did not previously exist ( null can also be the previous value of the field )
	 */
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
	/**
	 * Change a nested field of this adapter's root object. The root object and every child along the path must be json-simple objects.
	 * 
	 * @param tags the path to the field that should be changed
	 * @param value the new desired value of the field
	 * @return the previous value of the changed field, or null if the field did not previously exist ( null can also be the previous value of the field )
	 */
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
	/**
	 * Dereference an object, javascript-style.
	 * 
	 * Examples:
	 * <ul>
	 * <li><code>adapter.deref(".subfield[0]")</code> or <code>adapter.deref("subfield[0]")</code> would be equivalent to <code>adapter.get("subfield").get(0)</code></li>
	 * <li>"[0].asdf.foo[1]"</li>
	 * <li>"[0][1][2]"</li>
	 * </ul>
	 * Check out the tutorial for more examples.
	 * 
	 * @param reference a javascript-style dereference string for the desired object
	 * @return the object pointed to by the given reference, if it exists.
	 * @throws NullPointerException if the given reference string is null
	 * @throws IllegalArgumentException if the given reference string cannot be parsed
	 * @throws NoSuchElementException if no object can be found at the given reference
	 */
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
	/**
	 * Check whether the given index is valid for this adapter's root object.
	 * @param index the index to check
	 * @return true if this adapter's root object is an array and can contain the given index, false otherwise
	 */
	public boolean containsIndex( int index){
		//validate type
		if( this.isJSONArray()){
			JSONArray array = this.getJSONArray();
			return index >= 0 && index < array.size();}
		//we're not an array, so we cannot contain an index
		else return false;}

	/**
	 * Check whether the given key is valid for this adapter's root object.
	 * @param key the key to check
	 * @return true if this adapter's root object is a json object and contains the given key, false otherwise
	 */
	public boolean containsKey( String key){
		//validate type
		if( this.isJSONObject()){
			JSONObject object = this.getJSONObject();
			return object.containsKey( key);}
		//we're not an object, so we cannot contain a key
		else return false;}

	/**
	 * Check whether the given value is mapped to by this adapter's root object.
	 * @param value the value to check for
	 * @return true if this adapter's root object is a json object or array and contains the given value, false otherwise
	 */
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
	/**
	 * Check if this adapter's root object is a boolean object.
	 * @return true if this adapter's root object is a boolean object.
	 */
	public boolean isBoolean(){
		return root instanceof Boolean;}
	/**
	 * Check if this adapter's root object is a float or double object.
	 * @return true if this adapter's root object is a float or double.
	 */
	public boolean isDecimal(){
		return root instanceof Double;}
	/**
	 * Check if this adapter's root object is an integer object.
	 * @return true if this adapter's root object is an integer.
	 */
	public boolean isInteger(){
		return root instanceof Long;}
	/**
	 * Check if this adapter's root object is null.
	 * @return true if this adapter's root object is null.
	 */
	public boolean isNull(){
		return root == null;}
	/**
	 * Check if this adapter's root object is a integer or long object.
	 * @return true if this adapter's root object is a integer or long.
	 */
	public boolean isNumber(){
		return root instanceof Number;}
	/**
	 * Check if this adapter's root object is a string object.
	 * @return true if this adapter's root object is a string.
	 */
	public boolean isString(){
		return root instanceof String;}
	/**
	 * Check if this adapter's root object is a json array object.
	 * @return true if this adapter's root object is a json array.
	 */
	public boolean isJSONArray(){
		return root instanceof JSONArray;}
	/**
	 * Check if this adapter's root object is a json object.
	 * @return true if this adapter's root object is a json object.
	 */
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
	/**
	 * Check if the given object is a json simple type
	 * @param object the object to check
	 * @return true if the given object is a json simple type, false otherwise
	 */
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
	/**
	 * Return a string representation of this adapter's root object.
	 * @return a string representation of this adapter's root object
	 */
	public String toString(){
		return root == null ?
			"null" : root.toString();}

	/**
	 * Get the size of this adapter's root object. The root object must be a json array or object type.
	 * @return if the root object is an array, the size of the array, if it is an object, the number of entries in it's map.
	 */
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
		/** whether this token is a name */
		private boolean symbol;
		/** the name of this token */
		public String name;
		/** the index of this token */
		public int index;

		//constructors
		/**
		 * Create a new token from the given name
		 * @param name the name of the token
		 */
		public Token( String name){
			this.symbol = true;
			this.name = name;
			this.index = -1;}
		/**
		 * Create a new token from the given index
		 * @param index the index of the token
		 */
		public Token( int index){
			this.symbol = false;
			this.name = null;
			this.index = index;}

		//Accessors
		/**
		 * Check if this token is a name
		 * @return true, if this token is a name, false otherwise
		 */
		public boolean isName(){
			return symbol;}
		/**
		 * Check if this token is an index
		 * @return true, if this token is an index, false otherwise
		 */
		public boolean isIndex(){
			return ! symbol;}

		//utility function
		/**
		 * Parse a string into a list of tokens
		 * @param reference a reference string to parse
		 * @return a vector of tokens that was parsed form the given reference string
		 */
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
		/**
		 * Create a string representation of this token.
		 * @return a string representation of this token
		 */
		public String toString(){
			return String.format(
				symbol ? ".%s" : "[%d]",
				symbol ? name : index);}

		/**
		 * Create a string representation of a token path.
		 * @param tokens the token path to be stringified
		 * @return a string representation of the given token path
		 */
		public static String toString( List<Token> tokens){
			String result = new String();
			for( Token token : tokens)
				result += token.toString();
			return result;}

		/**
		 * Create a string representation of the subset of a token path.
		 * @param tokens the token path to be stringified
		 * @param end the exclusive end index of the token path in the list
		 * @return a string representation of the given token path
		 */
		public static String toString( List<Token> tokens, int end){
			String result = new String();
			for( int i = 0; i < end && i < tokens.size(); i++)
				result += tokens.get(i).toString();
			return result;}
	}
}
