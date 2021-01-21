# Extract table names from postgres sql query

- put your query in somefile
- change App.java to put the path of the query in that file 

### To compile
````
mvn clean package 
````

### To copy it to a remote location (optional)
````
rsync -zaP target/dependency-jars/ 188.166.204.110:~/code/template
````

### To run it on the remote location
````
java -cp  '/home/sharath.g/code/template/:/home/sharath.g/code/template/*' sha.App
````
