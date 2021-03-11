
### Export data

```mongoexport -h localhost:27017 -d library -c GENRES -o GENRES.json
mongoexport -h localhost:27017 -d library -c AUTHORS -o AUTHORS.json
mongoexport -h localhost:27017 -d library -c BOOKS -o BOOKS.json
mongoexport -h localhost:27017 -d library -c COMMENTS -o COMMENTS.json
mongoexport -h localhost:27017 -d library -c db_sequence -o db_sequence.json```