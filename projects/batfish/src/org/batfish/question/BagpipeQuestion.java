package org.batfish.question;

public class BagpipeQuestion extends Question {
   public BagpipeQuestion(QuestionParameters parameters) {
      super(QuestionType.BAGPIPE, parameters);
   }

   @Override
   public boolean getDataPlane() {
      return false;
   }

   @Override
   public boolean getDifferential() {
      return false;
   }
}
