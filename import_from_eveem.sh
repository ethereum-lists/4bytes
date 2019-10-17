#!/usr/bin/env sh

echo "downloading"

wget http://eveem.org/static/supplement.zip

echo "unzipping"
unzip supplement.zip

echo "reading from DB"
echo "select folded_name from functions" | sqlite3 supplement.db > tmp_eveem
./gradlew importFromEveem

echo "cleanup"
rm supplement.zip
rm supplement.db
rm tmp_eveem

