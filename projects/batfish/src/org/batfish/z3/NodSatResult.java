package org.batfish.z3;

import java.util.Map;

import org.batfish.common.BatfishLogger;
import org.batfish.job.BatfishJobResult;

public class NodSatResult<Key> extends BatfishJobResult<Map<Key, Boolean>> {

   private final Map<Key, Boolean> _results;

   public NodSatResult(long elapsedTime, Throwable failureCause) {
      super(elapsedTime, failureCause);
      _results = null;
   }

   public NodSatResult(Map<Key, Boolean> results, long elapsedTime) {
      super(elapsedTime);
      _results = results;
   }

   @Override
   public void applyTo(Map<Key, Boolean> output, BatfishLogger logger) {
      output.putAll(_results);
   }

   @Override
   public void explainFailure(BatfishLogger logger) {
   }

   @Override
   public String toString() {
      if (_results == null) {
         return "<FAILED>";
      }
      else {
         int numSat = 0;
         int numUnsat = 0;
         for (Boolean result : _results.values()) {
            if (result) {
               numSat++;
            }
            else {
               numUnsat++;
            }
         }
         return "<UNSAT: " + numUnsat + ", SAT: " + numSat + ">";
      }
   }

}
