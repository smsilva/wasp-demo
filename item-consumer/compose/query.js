cursor = db.person.find({_id: ObjectId("5ff5cb25df0696fc2bb8c643")});
while ( cursor.hasNext() ) {
  printjson( cursor.next() );
}
