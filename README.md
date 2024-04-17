# Key-Value-Database
<<<<<<< HEAD
=======
This is a Spring web application, implementing a { key , value } pair database.

## Running the database
A pre-build jar is kept at <code>gui\file-db\keydb.jar</code> in the repository.

1. Run the exe program to generate the cofiguration files.
   <code>gui\write-configuration.exe</code>

   This will create the below 3 files
   * pid file <code>gui\java.pid</code>
   * log file <code>gui\file-db\engine.log</code>
   * config file <code>gui\file-db\config.txt</code>

2. Run <code>gui\startDB.bat</code> batch to start the application.

3. Run <code>gui\stopDB.bat</code> batch to stop the application.

4. The default database created will be <code>storageDB</code> to access this
   open <code>gui\ui\index.html</code>

## Accessing the database through Postman
Below HTTP end-points have been defined to access the database

1. <code>GET http:/localhost:3040/{database-name}/getKeys</code>
   will get a list of all the keys in the database.

2. <code>POST http:/localhost:3040/{database-name}/getKey</code>
   Request body <code>{"key":"key1","value",""}</code>
   will get the value of the key.

3. <code>POST http:/localhost:3040/{database-name}/setKey</code>
   Request body <code>{"key":"key1","value","value1"}</code>
   will save the key-value pair.
   
4. <code>DELETE http:/localhost:3040/{database-name}/deleteKey</code>
   Request body <code>{"key":"key1","value",""}</code>
   will delete the key-value pair.

## Storage Mechanism

1. Hash-map index corrosponds to the file with the same name as the index.

2. Each pair is stored in a space of [1024] characters by default and the block sequence 
   number is the offset of that block.

3. Below image shows how the internal hash-map corrosponds to the index file in a database

>>>>>>> a274fbe (Fix basic features)
![alt text](https://github.com/shivam0rawat0/Key-Value-Database/blob/main/gui/db-strategy.png?raw=true)
