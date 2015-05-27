## [json-simpler](http://kurotetsuka.github.io/json-simpler/) :: An extension to [json-simple](https://code.google.com/p/json-simple/)

By: [Kurotetsuka](https://github.com/kurotetsuka)  
License: [LGPLv3.0](license.md) ([details](legal/gnu-lgpl-v3.0.md))

------------------------------------------------------------------------

### Tutorial
Content from `src/kuro/json/tutorial/Tutorial.java`:

```java
// first load your data
String filename = "data/tutorial.json";
File file = new File( filename);
String data = new String();
try {
	BufferedReader reader =
		new BufferedReader(
			new FileReader( file));
	while( reader.ready())
		data += reader.readLine() + '\n';
	reader.close();}
catch( Exception exception){
	exception.printStackTrace();
	return;}

// parse your data to a JSONObject (or JSONArray)
Object root = null;
try {
	root = new JSONParser().parse( data);}
catch( ParseException exception){
	System.out.println( "Your file was invalid!");
	exception.printStackTrace();
	return;}

// give your parsed root object to a JSONAdapter
JSONAdapter adapter = new JSONAdapter( root);

// now you can easily read your data!
String name = adapter.getString( "name");
int age = adapter.getInteger( "age");
boolean likesCake = adapter.getBoolean( "likes-cake");

// you can use dot-dereferencing notation to get sub-objects
float weight_kilos = adapter.derefFloat( "weight.kilos");
// alternatively, you can get sub-adapters for sub-objects
JSONAdapter weight_adapter = adapter.get( "weight");
double weight_pounds = weight_adapter.getDouble( "pounds");

// you can use array indexing notation to get array elements
Date birthday = new Date(
	adapter.derefInteger( "birthday[0]") - 1900,
	adapter.derefInteger( "birthday[1]"),
	adapter.derefInteger( "birthday[2]"));
// just like with sub-objects, you can get adapters for arrays
JSONAdapter deathday_adapter = adapter.get( "deathday");
// then use integers for gets
Date deathday = new Date(
	deathday_adapter.getInteger( 0) - 1900,
	deathday_adapter.getInteger( 1),
	deathday_adapter.getInteger( 2));

// if you want, you can get the raw json-simple objects and cast them yourself
JSONArray friends = adapter.getJSONArray( "friends");
Object friend_object = friends.get( 0);
String friend = null;
if( friend_object instanceof String)
	friend = (String) friend_object;

// there are also contains functions
boolean friendsWithMark =
	adapter.get("friends").containsValue( "Mark");

// there's also null checks, of course
boolean hasGirlfriend = ! adapter.isNull( "girlfriend");
// and type checks
boolean age_isint = adapter.isInteger( "age");

// no such element exceptions are thrown when the indicated element cannot be found
try{
	adapter.deref( "friends[10]");
	System.out.println( "This line won't print");}
catch( NoSuchElementException exception){}

// class cast exceptions are thrown when the element is found, but is of the wrong type
try{
	adapter.getInteger( "name");
	System.out.println( "This line won't print");}
catch( ClassCastException exception){}

// illegal argument exceptions are thrown when a dereference string is incorrectly formatted
try{
	System.out.printf(
		"asdf:%s\n",
		adapter.deref( "[0.asdf]"));
	System.out.println( "This line won't print");}
catch( IllegalArgumentException exception){}

// just to prove that everything worked
System.out.printf( "name: %s\n", name);
System.out.printf( "age: %d years\n", age);
System.out.printf( "likes cake: %s\n",
	likesCake ? "definely man!" : "nope dude");
System.out.printf( "weight: %.2f kilos, %.2f pounds\n",
	weight_kilos, weight_pounds);
System.out.printf( "birthday: %tF\n", birthday);
System.out.printf( "deathday: %tF\n", deathday);
System.out.printf( "first friend: %s\n", friend);
System.out.printf( "friends with mark: %b\n", friendsWithMark);
System.out.printf( "has girlfriend: %s\n",
	hasGirlfriend ? "affirmative" : "negative");}
```

------------------------------------------------------------------------

Github: [github.com/kurotetsuka/json-simpler](https://github.com/kurotetsuka/json-simpler/)  
Drone: [drone.io/github.com/kurotetsuka/json-simpler](https://drone.io/github.com/kurotetsuka/json-simpler)  
Site: [kurotetsuka.github.io/json-simpler](http://kurotetsuka.github.io/json-simpler/)  
Javadoc: [kurotetsuka.github.io/json-simpler/doc](http://kurotetsuka.github.io/json-simpler/doc/)  

Latest Release: [2.0](https://github.com/kurotetsuka/json-simpler/releases/tag/v2.0)  
Latest Beta: [2.1b2](https://github.com/kurotetsuka/json-simpler/releases/tag/v2.1b2)  
Latest Alpha: [2.0a1](https://github.com/kurotetsuka/json-simpler/releases/tag/v2.0a1)  
Build Status: [![Build Status](https://drone.io/github.com/kurotetsuka/json-simpler/status.png)](https://drone.io/github.com/kurotetsuka/json-simpler/latest)

