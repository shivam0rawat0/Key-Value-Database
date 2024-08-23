docker build --build-arg DB_CONFIG=db-active.txt -t keydb .
docker build --build-arg DB_CONFIG=db-raid.txt -t keydb-raid .
docker build --build-arg DB_CONFIG=db-raid1.txt -t keydb-raid1 .