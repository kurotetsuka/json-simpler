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
				"Root is not an json array");
		return new JSONAdapter(
			( (JSONArray) root).get( index));}
	public JSONAdapter get( String tag){
		if( ! this.isJSONObject())
			throw new NoSuchElementException(
				"Root is not a json object");
		return new JSONAdapter(
			( (JSONObject) root).get( tag));}
	public JSONAdapter get( String[] tags){
		if( ! this.isJSONObject())
			throw new NoSuchElementException(
				"Root is not a json object");
		JSONAdapter adapter = this;
		for( String tag : tags)
			adapter = adapter.get( tag);
		return adapter;}

	//set functions
	//array set function
	public Object set( int index, Object value){
		if( ! this.isJSONArray())
			throw new NoSuchElementException(
				"Root is not an array");
		JSONArray array = this.getJSONArray();
		return array.set( index, value);}

	//object set function
	public Object set( String tag, Object value){
		if( ! this.isJSONObject())
			throw new NoSuchElementException(
				"Root is not a json object");
		JSONObject object = this.getJSONObject();
		return null;}
	public Object set( String[] tags, Object value){
		if( ! this.isJSONObject())
			throw new NoSuchElementException(
				"Root is not a json object");
		int i = 0;
		int last_i = tags.length - 1;
		JSONAdapter adapter = this;
		for( String tag : tags){
			if( i == last_i) break;
			if( adapter.containsKey( tag)){
				JSONAdapter next = adapter.get( tag);
				if( ! next.isJSONObject())
					break;
				else
					adapter = next;}
			else break;}

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