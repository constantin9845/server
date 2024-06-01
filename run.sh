if [ "$#" == 1 ]
then
  	# Run Server
	java -cp bin Server $1	
else
	# Run Client
	java -cp bin Client $1 $2
fi