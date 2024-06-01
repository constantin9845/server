if [ $# -eq 1 ]
then
  	# Run Server
  	javac -d bin src/*.java
	java -cp bin Server "$1"	
else
	# Run Client
	javac -d bin src/*.java
	java -cp bin Client "$1" "$2"
fi