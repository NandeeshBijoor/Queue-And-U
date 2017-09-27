# Queue-And-U-

Build

	Build Tool: Maven
	Version:  3.3.9
	Java: 1.8.x
	Spring Boot version : 1.4.0.RELEASE
	Build Command: mvn clean compile package
	Package Name: QueueAndU-1.0.0-SNAPSHOT.jar

Deploy

	To deploy the jar go to jar file location java -jar QueueAndU-1.0.0-SNAPSHOT.jar
	Runs on port 9090 by default

APIs:

/add

	API to add add work request to queue
	Takes request id and request date as parameters as shown in example

	Method - POST

	Body - 

		{
		"id":"1",
		"date":"2017-09-17T01:14:16-04:00"
		}

		id - id of the work request
		date - request date in yyyy-MM-dd'T'HH:mm:ssXXX

/top
	
	API to get the top id from queue
	
	Method: GET


/list

	API to list all the ids from queue
	
	Method: GET

/remove

	API to remove work request from queue
	Takes request id as parameter as shown in example

	Method - POST

	Body - 

		{
		"id":"1"
		}

		id - id of the work request

/position

	API to check the position of work request in queue
	Takes request id as parameter as shown in example

	Method - POST

	Body - 

		{
		"id":"1"
		}

		id - id of the work request

/meantime

	API to get meantime of all the request in queue
	Takes request current time as parameter as shown in example

	Method - POST

	Body - 

		{
		"date":"2017-09-17T01:14:16-04:00"
		}

		date - request date in yyyy-MM-dd'T'HH:mm:ssXXX

