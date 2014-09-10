SEQ=$1
ALLCOMMITS=$2
ALLDATES=$3
PROPERTIES=$4
CMPRESPWD=$5
cat $PROPERTIES | while read line
do
	./get-time.sh $SEQ $ALLCOMMITS $ALLDATES $line $CMPRESPWD
	./get-time2.sh $SEQ $ALLCOMMITS $ALLDATES $line $CMPRESPWD
	./histo-time.py $line-time
	./histo-time2.py $line-time2
done
