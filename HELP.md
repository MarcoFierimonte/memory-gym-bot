# USE MONGODB IMPORT FILE EXAMPLE

.\mongoimport.exe --uri "mongodb+srv://<user>:<pwd>@cluster0.n7aqfhp.mongodb.net" --db memory_gym_bot --collection init_dataset --jsonArray --drop --file ./initDataset.json


# MONGO COMMANDS UTILS:

- Add a new column to collection with default value:

`db.getSiblingDB("memory_gym_bot").getCollection("dictionary").updateMany({}, {$set: {"frequency": 0}})`

- Example of pipeline operations with 1-->match,2-->find max of field,3-->get only 3 documents from previous results

`db.getSiblingDB("memory_gym_bot").getCollection("dictionary").aggregate([
{'$match':{'chatId': 69501949 }},
{ "$group":
{
"_id": null,
"max": { "$max": "$frequency" }
}},
{$sample:{size: 3 }}
])`

# POSTMAN COLLECTION FOR TESTING
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/16175024-eb355b57-e409-402e-b328-1cdfab803eb9?action=collection%2Ffork&collection-url=entityId%3D16175024-eb355b57-e409-402e-b328-1cdfab803eb9%26entityType%3Dcollection%26workspaceId%3D6423365e-5880-4765-8190-40a7a97d32a5#?env%5Blocal_telegram_bot%5D=W3sia2V5IjoiYmFzZVVybCIsInZhbHVlIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZW5hYmxlZCI6dHJ1ZSwidHlwZSI6ImRlZmF1bHQifV0=)
