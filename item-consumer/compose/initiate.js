conn = new Mongo();
db = conn.getDB("my_database");
db.person.insertOne({"_id": ObjectId("5ff5cb25df0696fc2bb8c643")},{$set:{name:"David"}})
load("query.js");
