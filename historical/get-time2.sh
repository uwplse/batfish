#!/usr/bin/env bash
SEQ=$1
ALLCOMMITS=$2
ALLDATES=$3
PREFIX=$4
CMPRESPWD=$5
OUTPUT=$PWD/$PREFIX-time2
PREV_COMMIT=$(head $SEQ -n 1)
FIRST_DATE=$(sed -n -e "$(grep -n $PREV_COMMIT $ALLCOMMITS | cut -d':' -f 1)p" $ALLDATES)
S0=$(date -d "${FIRST_DATE:8:24}" +%s)
{
sed -n '2,$p' $SEQ | while read line
do
  SEC_COMMIT=$line
  SECOND_DATE=$(sed -n -e "$(grep -n $SEC_COMMIT $ALLCOMMITS | cut -d':' -f 1)p" $ALLDATES)
  RES_FILE=$CMPRESPWD/history-cmpres-$PREV_COMMIT-$SEC_COMMIT.simplified
  if grep -q $PREFIX "$RES_FILE"; then
    DIFF=0
    S2=$(date -d "${SECOND_DATE:8:24}" +%s)
    let "DIFF=S2-S0" 
    echo $DIFF
  fi
  PREV_COMMIT=$SEC_COMMIT
done 
} > $OUTPUT
