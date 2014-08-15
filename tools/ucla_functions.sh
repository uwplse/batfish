#!/usr/bin/env bash
batfish_generate_commit() {

   batfish_expect_args 2 $# || return 1
   if [ -z "$BATFISH_CONFIRM" ]; then
      local BATFISH_CONFIRM=true
   fi
   local UCLA_GIT_ROOT=$1 
   local COMMIT=$2
   local OLD_PWD=$PWD
   local TEST_RIG=$PWD/test-$COMMIT
   local CHANGEME_TEST_RIG=$TEST_RIG/test-$COMMIT-subgroup-00
   local UCLA_CONFIGS=$OLD_PWD/ucla_configs-$COMMIT
   local REACH_PATH=$OLD_PWD/z3-reach-${COMMIT}.smt2
   local INCONS_PATH=$OLD_PWD/inconsistent-$COMMIT
   local DUMP_DIR=$OLD_PWD/facts-$COMMIT
   local PREFIX=$COMMIT
   local TMP_GIT_ROOT=$PWD/tmpgit-$COMMIT
   
   echo "Copy to tmp git"
   cp -rf $UCLA_GIT_ROOT $TMP_GIT_ROOT

   echo  "Generate test rig from commit"
   $BATFISH_CONFIRM && { batfish_extract_commit $COMMIT $TMP_GIT_ROOT $UCLA_CONFIGS || return 1 ; }
   
   echo "Produce a UCLA test rig with generated most general peers"
   $BATFISH_CONFIRM && { batfish_mkucla $UCLA_CONFIGS $TEST_RIG || return 1 ; }

   echo "Remove tmp git"
   rm -rf $TMP_GIT_ROOT
   
}
export -f batfish_generate_commit

batfish_generate_all(){
   batfish_expect_args 2 $# || return 1
   local UCLA_GIT_ROOT=$1
   local COMMITS=$2
   local OLD_PWD=$PWD
   cat $COMMITS | while read line
   do
	COMMIT=$line
        TEST_RIG=$PWD/test-$COMMIT
        CHANGEME_TEST_RIG=$TEST_RIG/test-$COMMIT-subgroup-00
	ORI_TEST_TIG=$PWD/ucla_configs-$COMMIT
        UCLA_CONFIGS=$OLD_PWD/ucla_configs-$COMMIT
	batfish_generate_commit $UCLA_GIT_ROOT $COMMIT || return 1 ;
	rm -rf $ORI_TEST_TIG 
   done
}
export -f batfish_generate_all

batfish_analyze_history(){
   batfish_expect_args 3 $# || return 1
   local UCLA_GIT_ROOT=$1
   local COMMITS=$2
   local OLD_PWD=$PWD
   local PREV_COMMIT=$(head $COMMITS -n 1)
   local ORI_TEST_RIG1=$PWD/ucla_configs-$PREV_COMMIT
   local TEST_RIG1=$PWD/test-$PREV_COMMIT
   local PREFIX=$3
   local TMP_RESULT=$PWD/$PREFIX-tmpres
   local CMP_RESULT=$PWD/$PREFIX-cmpres
   batfish_extract_ucla_commit $UCLA_GIT_ROOT $PREV_COMMIT || return 1 ;
   rm -rf $ORI_TEST_RIG1 
   sed -n '2,$p' $COMMITS | while read line
   do
	SEC_COMMIT=$line
    TEST_RIG2=$PWD/test-$SEC_COMMIT
    ORI_TEST_RIG2=$PWD/ucla_configs-$SEC_COMMIT
	batfish_generate_commit $UCLA_GIT_ROOT $SEC_COMMIT || return 1 ;
   	rm -rf $ORI_TEST_RIG2 
	batfish -commits $TEST_RIG1 $TEST_RIG2 || return 1 ;
	TEST_RIG1=$TEST_TIG2
	rm -rf $PWD/test-$PREV_COMMIT
	PREV_COMMIT=$SEC_COMMIT
   done
   sed -n '/Compare/,/End Compare/p' $TMP_RESULT > $CMP_RESULT 
}
export -f batfish_analyze_history

batfish_analyze_self(){
   batfish_expect_args 2 $# || return 1
   local UCLA_GIT_ROOT=$1
   local COMMITS=$2
   local OLD_PWD=$PWD
   rm -rf tmpres_self
   rm -rf cmpres_self
   mkdir tmpres_self
   mkdir cmpres_self
   cat $COMMITS | parallel -j6 batfish_analyze_self_helper {} $UCLA_GIT_ROOT\;
   if [ "${PIPESTATUS[0]}" -ne 0 -o "${PIPESTATUS[1]}" -ne 0 ]; then
      return 1
   fi
}
export -f batfish_analyze_self

batfish_analyze_self_helper(){
   batfish_expect_args 2 $# || return 1
   local SEC_COMMIT=$1
   local UCLA_GIT_ROOT=$2
   local TMP_RESULT=$PWD/tmpres_self/tmpres_self-$SEC_COMMIT
   local TMP_RESULT_ERR=$PWD/tmpres_self/tmpres_self-err-$SEC_COMMIT
   local CMP_RESULT=$PWD/cmpres_self/cmpres_self-$SEC_COMMIT
   local FAIL_COMMIT=$PWD/fail_commits
   {
   TEST_RIG2=$PWD/test-$SEC_COMMIT
   ORI_TEST_RIG2=$PWD/ucla_configs-$SEC_COMMIT
   TODELETE_TEST_RIG2=$PWD/test-$SEC_COMMIT
   batfish_extract_ucla_commit $UCLA_GIT_ROOT $SEC_COMMIT || return 1 ;
   rm -rf $ORI_TEST_RIG2 
   batfish -ee -commits $TEST_RIG2 $TEST_RIG2 || local FAILED=1
   rm -rf $TODELETE_TEST_RIG2
   if [ -n "$FAILED" ];	then
	echo $SEC_COMMIT >> $FAIL_COMMIT
	return 1 ;
   fi
   } > $TMP_RESULT 2>$TMP_RESULT_ERR
   sed -n '/Compare/,/End Compare/p' $TMP_RESULT > $CMP_RESULT 
   if [ "${PIPESTATUS[0]}" -ne 0 -o "${PIPESTATUS[1]}" -ne 0 ]; then
      return 1
   fi
}
export -f batfish_analyze_self_helper

batfish_analyze_commit() {
   batfish_expect_args 2 $# || return 1
   if [ -z "$BATFISH_CONFIRM" ]; then
      local BATFISH_CONFIRM=true
   fi
   local UCLA_GIT_ROOT=$1 
   local COMMIT=$2
   local OLD_PWD=$PWD
   local TF_DIR=$OLD_PWD/tf_ucla_backbone-$COMMIT
   local TEST_RIG=$PWD/test-$COMMIT
   local CHANGEME_TEST_RIG=$TEST_RIG/test-$COMMIT-subgroup-00
   local UCLA_CONFIGS=$OLD_PWD/ucla_configs-$COMMIT
   local REACH_PATH=$OLD_PWD/z3-reach-${COMMIT}.smt2
   local INCONS_PATH=$OLD_PWD/inconsistent-$COMMIT
   local DUMP_DIR=$OLD_PWD/facts-$COMMIT
   local PREFIX=$COMMIT

   echo  "Generate test rig from commit"
   $BATFISH_CONFIRM && { batfish_extract_commit $COMMIT $UCLA_GIT_ROOT $UCLA_CONFIGS || return 1 ; }
   
   echo "Produce a UCLA test rig with generated most general peers"
   $BATFISH_CONFIRM && { batfish_mkucla $UCLA_CONFIGS $TEST_RIG || return 1 ; }
   
   echo "Run batfish analyze"
   { batfish_analyze $CHANGEME_TEST_RIG $PREFIX || return 1 ; }
}
export -f batfish_analyze_commit

batfish_extract_commit() {
   date | tr -d '\n'
   echo  ": START: Generate test rig from commit"
   batfish_expect_args 3 $# || return 1
   local COMMIT=$1
   local UCLA_GIT_ROOT=$2
   local UCLA_CONFIGS=$3
   local OLD_PWD=$PWD
   mkdir -p $UCLA_CONFIGS
   cd $UCLA_GIT_ROOT
   git --work-tree=$UCLA_CONFIGS checkout $COMMIT -- . || return 1
   cd $OLD_PWD
   date | tr -d '\n'
   echo  ": END: Generate test rig from commit"
}
export -f batfish_extract_commit

batfish_mkucla() {
   date | tr -d '\n'
   echo ": START: Produce a UCLA test rig with generated most general peers"
   batfish_expect_args 2 $# || return 1
   local UCLA_CONFIGS=$1
   local TEST_RIG=$2
   local DC_STUB=$BATFISH_TEST_RIG_PATH/dc_stub.cfg
   local HPR_STUB=$BATFISH_TEST_RIG_PATH/hpr_stub.cfg
   local OLD_PWD=$PWD
   mkdir -p $TEST_RIG/configs
   cd $TEST_RIG/configs
   find $UCLA_CONFIGS -type f -not -wholename '*\.git*' -and -not -name 'router.db' -and -not -name '*lab\.ucla\.net*' -and -not -name 'sw*' -and -not -name 'ra*' -and -not -name 'sr*' -and -not -name 'br*' -and -not -name 'vg*' -and -not -name 'vr*' -and -not -name 'rtr*' -and -not -name 'border*' -exec cp -a {} . \;
   rm $(grep -l '!RANCID.*aruba.*' *)
   rm $(grep -l '!RANCID.*cat5.*' *)
   #rm -f $(grep -l '!RANCID.*alcatel.*' *)
   #rm -f $(grep -l '!RANCID.*cisco-nx.*' *)
   cp -a $DC_STUB .
   cp -a $HPR_STUB .
   $BATFISH -testrig $TEST_RIG -dr
   find $TEST_RIG  -type d -name '*subgroup*' -exec cp -a * {}/configs/ \;
   #rm -r $TEST_RIG/configs
   cd $OLD_PWD
   date | tr -d '\n'
   echo ": END: Produce a UCLA test rig with generated most general peers"
}
export -f batfish_mkucla

batfish_mktestrig_ucla() {
   date | tr -d '\n'
   echo ": START: Extract a UCLA test rig from a commit"
   batfish_expect_args 2 $# || return 1
   local UCLA_CONFIGS=$1
   local TEST_RIG=$2
   local OLD_PWD=$PWD
   local CONFIG_DIR=$TEST_RIG/configs
   mkdir -p $CONFIG_DIR
   cd $CONFIG_DIR
   cp $UCLA_CONFIGS/{border,core,distribution}/configs/* .
   rm -f $(grep -l '!RANCID-CONTENT-TYPE: aruba' *)
   rm -f $(grep -l '!RANCID-CONTENT-TYPE: cat5' *)
   #rm -f $(grep -l '!RANCID.*alcatel.*' *)
   #rm -f $(grep -l '!RANCID.*cisco-nx.*' *)
   rm -rf $UCLA_CONFIGS
   cd $OLD_PWD
   date | tr -d '\n'
   echo ": END: Extract a UCLA test rig from a commit"
}
export -f batfish_mktestrig_ucla

batfish_compile_commits() {
   date | tr -d '\n'
   echo ": START: Compute the fixed point of the control plane"
   batfish_expect_args 3 $# || return 1
   local COMMIT=$1
   local TEST_RIG=$2
   local DUMP_DIR=$3
   $BATFISH -testrig $TEST_RIG -compile -facts -guess -ee -dumpcp -dumpdir $DUMP_DIR || return 1
   date | tr -d '\n'
   echo ": END: Compute the fixed point of the control plane"
}
export -f batfish_compile

batfish_extract_ucla_commits() {
   batfish_expect_args 2 $# || return 1
   local UCLA_GIT_ROOT=$1
   local COMMITS=$2
   cat $COMMITS | while read COMMIT; do
      batfish_extract_ucla_commit $UCLA_GIT_ROOT $COMMIT
   done
}
export -f batfish_extract_ucla_commits

batfish_extract_ucla_commit() {
   batfish_expect_args 2 $# || return 1
   if [ -z "$BATFISH_CONFIRM" ]; then
      local BATFISH_CONFIRM=true
   fi
   date | tr -d '\n'
   echo ": START: Parse commit"
   local UCLA_GIT_ROOT=$1
   local COMMIT=$2
   local OLD_PWD=$PWD
   local TEST_RIG=$PWD/test-$COMMIT
   local UCLA_CONFIGS=$OLD_PWD/ucla_configs-$COMMIT

   if [ ! -e $TEST_RIG ]; then
      echo  "Generate test rig from commit"
      $BATFISH_CONFIRM && { batfish_extract_commit $COMMIT $UCLA_GIT_ROOT $UCLA_CONFIGS || return 1 ; }
   
      echo "Extract a UCLA test rig from a commit"
      $BATFISH_CONFIRM && { batfish_mktestrig_ucla $UCLA_CONFIGS $TEST_RIG || return 1 ; }
   fi
   date | tr -d '\n'
   echo ": END: Extract commit"
}
export -f batfish_extract_ucla_commit

batfish_parse_commits() {
   batfish_expect_args 1 $# || return 1
   local COMMITS=$1
   cat $COMMITS | parallel batfish_parse_commit {} \;
}
export -f batfish_parse_commits

batfish_parse_commit() {
   batfish_expect_args 1 $# || return 1
   if [ -z "$BATFISH_CONFIRM" ]; then
      local BATFISH_CONFIRM=true
   fi
   date | tr -d '\n'
   echo ": START: Parse commit"
   local COMMIT=$1
   local OLD_PWD=$PWD
   local TEST_RIG=$PWD/test-$COMMIT
   local SVPATH=$PWD/sv-$COMMIT
   
   if [ ! -e "$SVPATH" ]; then
      echo "Parse test rig: $TEST_RIG"
      $BATFISH_CONFIRM && { batfish -log 1 -testrig $TEST_RIG -sv -svpath $SVPATH -ee || return 1 ; }
   fi
   date | tr -d '\n'
   echo ": END: Parse commit"
}
export -f batfish_parse_commit

