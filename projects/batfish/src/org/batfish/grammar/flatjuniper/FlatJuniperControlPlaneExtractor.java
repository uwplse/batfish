package org.batfish.grammar.flatjuniper;

import java.util.Set;
import java.util.TreeSet;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.batfish.grammar.ControlPlaneExtractor;
import org.batfish.main.Warnings;
import org.batfish.representation.VendorConfiguration;
import org.batfish.representation.juniper.JuniperVendorConfiguration;

public class FlatJuniperControlPlaneExtractor implements ControlPlaneExtractor {

   private JuniperVendorConfiguration _configuration;

   private final FlatJuniperCombinedParser _parser;

   private final String _text;

   private final Set<String> _unimplementedFeatures;

   private final Warnings _w;

   public FlatJuniperControlPlaneExtractor(String fileText,
         FlatJuniperCombinedParser combinedParser, Warnings warnings) {
      _text = fileText;
      _unimplementedFeatures = new TreeSet<String>();
      _parser = combinedParser;
      _w = warnings;
   }

   @Override
   public Set<String> getUnimplementedFeatures() {
      return _unimplementedFeatures;
   }

   @Override
   public VendorConfiguration getVendorConfiguration() {
      return _configuration;
   }

   @Override
   public void processParseTree(ParserRuleContext tree) {
      Hierarchy hierarchy = new Hierarchy();
      ParseTreeWalker walker = new ParseTreeWalker();
      DeactivateTreeBuilder dtb = new DeactivateTreeBuilder(hierarchy);
      walker.walk(dtb, tree);
      DeactivateLinePruner dp = new DeactivateLinePruner();
      walker.walk(dp, tree);
      DeactivatedLinePruner dlp = new DeactivatedLinePruner(hierarchy);
      walker.walk(dlp, tree);
      InitialTreeBuilder tb = new InitialTreeBuilder(hierarchy);
      walker.walk(tb, tree);
      GroupTreeBuilder gb = new GroupTreeBuilder(_parser, hierarchy);
      walker.walk(gb, tree);
      ApplyGroupsApplicator hb;
      do {
         hb = new ApplyGroupsApplicator(_parser, hierarchy, _w);
         walker.walk(hb, tree);
      } while (hb.getChanged());
      GroupPruner gp = new GroupPruner();
      walker.walk(gp, tree);
      WildcardApplicator wa = new WildcardApplicator(hierarchy);
      walker.walk(wa, tree);
      WildcardPruner wp = new WildcardPruner();
      walker.walk(wp, tree);
      walker.walk(dlp, tree);
      ApplyPathApplicator ap = new ApplyPathApplicator(hierarchy, _w);
      walker.walk(ap, tree);
      ConfigurationBuilder cb = new ConfigurationBuilder(_parser, _text, _w,
            _unimplementedFeatures);
      walker.walk(cb, tree);
      _configuration = cb.getConfiguration();
   }

}
