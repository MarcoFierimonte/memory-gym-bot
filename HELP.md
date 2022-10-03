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


