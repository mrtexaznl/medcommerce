medcommerce
===========

Simple XML-RPC server for implementing an ecommerce service


Run service on same machine where MED wallet is running:

java -jar dist/medcommerce.jar

By default, it should listen on localhost:8080 and wait for JSON-RPC calls;
the service looks for .mediterraneancoin/mediterraneancoin.conf in the user's home for reading username and password.

// this just forwards the getnewaddress method to the wallet...
REQUEST: {"method": "getnewaddress", "params": [], "id": 1}
ANSWER: {"result": "MfVsBVi8UnRpG5aVSWZXtBmCn2jzZTc47R", "error": null, "id": 1}


// this method does the following: 
// start from current highest block and go backwards (for 5 blocks, in this case)
// and look for all transactions with output in the specified MED address;
// return an array of [block number, transaction id, MED value

// with this method, by polling periodically, you can verify if the user has paid the amount due.

REQUEST: {"method": "getlastntransactionsforaddress", "params": ["Mb9hgh4ThWUdSaUucYNqEzTVuBWHq5Q2W1", 5], "id": 1}
ANSWER: [[111074,"f46e8218983ee45fc7052e145652502af69452c4657b5b72f10a36c271d784f7",10.0]]
