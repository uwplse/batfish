package org.batfish.question;

public class BagpipeQuestion extends Question {
   private String text;

   public BagpipeQuestion(QuestionParameters parameters, String text) {
      super(QuestionType.BAGPIPE, parameters);
      this.text = text;
   }

   public String getText() {
     return text;
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
