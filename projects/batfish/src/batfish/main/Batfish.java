package batfish.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.logicblox.bloxweb.client.ServiceClientException;
import com.logicblox.connect.Workspace.Relation;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import batfish.grammar.BatfishCombinedParser;
import batfish.grammar.ConfigurationLexer;
import batfish.grammar.ConfigurationParser;
import batfish.grammar.ParseTreePrettyPrinter;
import batfish.grammar.TopologyLexer;
import batfish.grammar.TopologyParser;
import batfish.grammar.cisco.CiscoCombinedParser;
import batfish.grammar.cisco.controlplane.CiscoControlPlaneExtractor;
import batfish.grammar.juniper.FlatJuniperGrammarLexer;
import batfish.grammar.juniper.FlatJuniperGrammarParser;
import batfish.grammar.juniper.JuniperConfiguration;
import batfish.grammar.juniper.JuniperGrammarLexer;
import batfish.grammar.juniper.JuniperGrammarParser;
import batfish.grammar.topology.BatfishTopologyLexer;
import batfish.grammar.topology.BatfishTopologyParser;
import batfish.grammar.topology.GNS3TopologyLexer;
import batfish.grammar.topology.GNS3TopologyParser;
import batfish.grammar.z3.ConstraintsLexer;
import batfish.grammar.z3.ConstraintsParser;
import batfish.grammar.z3.QueryResultLexer;
import batfish.grammar.z3.QueryResultParser;
import batfish.grammar.z3.Result;
import batfish.grammar.semantics.SemanticsLexer;
import batfish.grammar.semantics.SemanticsParser;
import batfish.logic.LogicResourceLocator;
import batfish.logicblox.ConfigurationFactExtractor;
import batfish.logicblox.Facts;
import batfish.logicblox.LBInitializationException;
import batfish.logicblox.LBValueType;
import batfish.logicblox.LogicBloxFrontend;
import batfish.logicblox.PredicateInfo;
import batfish.logicblox.ProjectFile;
import batfish.logicblox.QueryException;
import batfish.logicblox.TopologyFactExtractor;
import batfish.representation.Configuration;
import batfish.representation.Edge;
import batfish.representation.Ip;
import batfish.representation.Protocol;
import batfish.representation.Topology;
import batfish.representation.VendorConfiguration;
import batfish.representation.VendorConversionException;
import batfish.representation.cisco.BgpProcess;
import batfish.representation.cisco.BgpRedistributionPolicy;
import batfish.representation.cisco.CiscoConfiguration;
import batfish.representation.cisco.ExpandedCommunityList;
import batfish.representation.cisco.ExtendedAccessList;
import batfish.representation.cisco.Interface;
import batfish.representation.cisco.IpAsPathAccessList;
import batfish.representation.cisco.OspfProcess;
import batfish.representation.cisco.PrefixList;
import batfish.representation.cisco.RouteMap;
import batfish.representation.cisco.RouteMapClause;
import batfish.representation.cisco.StandardAccessList;
import batfish.representation.cisco.StandardCommunityList;
import batfish.representation.cisco.StaticRoute;
import batfish.representation.juniper.BGPProcess;
import batfish.representation.juniper.JuniperVendorConfiguration;
import batfish.representation.juniper.OSPFProcess;
import batfish.representation.juniper.PolicyStatement;
import batfish.representation.juniper.RouteFilter;
import batfish.ucla.DeptGenerator;
import batfish.util.UrlZipExplorer;
import batfish.util.StringFilter;
import batfish.util.Util;
import batfish.z3.Concretizer;
import batfish.z3.FibRow;
import batfish.z3.Synthesizer;

public class Batfish {
   private static final String BASIC_FACTS_BLOCKNAME = "BaseFacts";
   private static final String EDGES_FILENAME = "edges";
   // private static final String FLOW_SINK_FILENAME = "flow_sinks";
   private static final String FIB_PREDICATE_NAME = "FibNetworkForward";
   private static final String FIBS_FILENAME = "fibs";
   private static final String SEPARATOR = System.getProperty("file.separator");
   private static final String STATIC_FACT_BLOCK_PREFIX = "libbatfish:";
   private static final String TOPOLOGY_FILENAME = "topology.net";
   private static final String TOPOLOGY_PREDICATE_NAME = "LanAdjacent";

   private static void initControlPlaneFactBins(
         Map<String, StringBuilder> factBins) {
      initFactBins(Facts.CONTROL_PLANE_FACT_COLUMN_HEADERS, factBins);
   }

   private static void initFactBins(Map<String, String> columnHeaderMap,
         Map<String, StringBuilder> factBins) {
      for (String factPredicate : columnHeaderMap.keySet()) {
         String columnHeaders = columnHeaderMap.get(factPredicate);
         String initialText = columnHeaders + "\n";
         factBins.put(factPredicate, new StringBuilder(initialText));
      }

   }

   private static void initTrafficFactBins(Map<String, StringBuilder> factBins) {
      initFactBins(Facts.TRAFFIC_FACT_COLUMN_HEADERS, factBins);
   }

   private List<LogicBloxFrontend> _lbFrontends;

   private PredicateInfo _predicateInfo;
   private Settings _settings;

   private long _timerCount;

   private File _tmpLogicDir;

   public Batfish(Settings settings) {
      _settings = settings;
      _lbFrontends = new ArrayList<LogicBloxFrontend>();
      _tmpLogicDir = null;
   }

   private void addProject(LogicBloxFrontend lbFrontend) {
      print(0, "\n*** ADDING PROJECT ***\n");
      resetTimer();
      String settingsLogicDir = _settings.getLogicDir();
      File logicDir;
      if (settingsLogicDir != null) {
         logicDir = new ProjectFile(settingsLogicDir);
      }
      else {
         logicDir = retrieveLogicDir().getAbsoluteFile();
      }
      String result = lbFrontend.addProject(logicDir, "");
      cleanupLogicDir();
      if (result != null) {
         error(0, result + "\n");
         quit(1);
      }
      print(1, "SUCCESS\n");
      printElapsedTime();
   }

   private void addStaticFacts(LogicBloxFrontend lbFrontend,
         List<String> blockNames) {
      print(0, "\n*** ADDING STATIC FACTS ***\n");
      resetTimer();
      for (String blockName : blockNames) {
         print(1, "Adding " + blockName + "...");
         String output = lbFrontend.execNamedBlock(STATIC_FACT_BLOCK_PREFIX
               + blockName);
         if (output == null) {
            print(1, "OK\n");
         }
         else {
            error(0, output + "\n");
            quit(1);
         }
      }
      print(1, "SUCCESS\n");
      printElapsedTime();
   }

   private void anonymizeConfigurations() {
      // TODO Auto-generated method stub

   }

   private void cleanupLogicDir() {
      if (_tmpLogicDir != null) {
         try {
            FileUtils.deleteDirectory(_tmpLogicDir);
         }
         catch (IOException e) {
            e.printStackTrace();
         }
         _tmpLogicDir = null;
      }
   }

   private void computeDataPlane(LogicBloxFrontend lbFrontend) {
      print(0, "\n*** COMPUTING DATA PLANE STRUCTURES ***\n");
      resetTimer();

      lbFrontend.initEntityTable();
      print(1, "Retrieving topology information from LogicBlox..");
      Set<Edge> topologyEdges = getTopologyEdges(lbFrontend);
      print(1, "OK\n");

      String fibQualifiedName = _predicateInfo.getPredicateNames().get(
            FIB_PREDICATE_NAME);
      print(1, "Retrieving FIB information from LogicBlox..");
      Relation fib = lbFrontend.queryPredicate(fibQualifiedName);
      print(1, "OK\n");
      print(1, "Caclulating forwarding rules..");
      Map<String, TreeSet<FibRow>> fibs = getRouteForwardingRules(fib,
            lbFrontend);
      print(1, "OK\n");

      Path fibsPath = Paths.get(_settings.getDataPlaneDir(), FIBS_FILENAME);
      Path edgesPath = Paths.get(_settings.getDataPlaneDir(), EDGES_FILENAME);

      print(1, "Serializing fibs..");
      serializeObject(fibs, fibsPath.toFile());
      print(1, "OK\n");

      print(1, "Serializing toplogy edges..");
      serializeObject(topologyEdges, edgesPath.toFile());
      print(1, "OK\n");

      printElapsedTime();
   }

   private void concretize() {
      File queryOutputFile = new File(_settings.getConcretizerInputFilePath());
      String queryOutputStr = null;
      try {
         queryOutputStr = FileUtils.readFileToString(queryOutputFile);
      }
      catch (IOException e1) {
         e1.printStackTrace();
         quit(1);
      }
      ANTLRStringStream stream = new ANTLRStringStream(queryOutputStr);
      QueryResultLexer lexer = new QueryResultLexer(stream);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      QueryResultParser parser = new QueryResultParser(tokens);
      List<Result> results = null;
      try {
         results = parser.results();
      }
      catch (Exception e) {
         error(0, " ...ERROR\n");
         e.printStackTrace();
      }
      List<String> parserErrors = parser.getErrors();
      List<String> lexerErrors = lexer.getErrors();
      int numErrors = parserErrors.size() + lexerErrors.size();
      if (numErrors > 0) {
         error(0, " ..." + numErrors + " ERROR(S)\n");
         for (String msg : lexer.getErrors()) {
            error(2, "\tlexer: " + msg + "\n");
         }
         for (String msg : parser.getErrors()) {
            error(2, "\tparser: " + msg + "\n");
         }
         quit(1);
      }
      File outputFile = new File(_settings.getConcretizerOutputFilePath());
      outputFile.delete();
      for (Result result : results) {
         if (result == null) {
            try {
               FileUtils.write(outputFile, "unsat\n", true);
            }
            catch (IOException e) {
               e.printStackTrace();
               quit(1);
            }
            quit(0);
         }
      }
      Concretizer concretizer = new Concretizer(results,
            Synthesizer.getStdArgs());
      List<String> concretizerOutputs = concretizer.concretize();
      for (String co : concretizerOutputs) {
         co += "\n\n";
         try {
            FileUtils.write(outputFile, co, true);
         }
         catch (IOException e) {
            e.printStackTrace();
            quit(1);
         }
      }
   }

   private LogicBloxFrontend connect() {
      boolean assumedToExist = !_settings.createWorkspace();
      String workspaceMaster = _settings.getWorkspaceName();
      LogicBloxFrontend lbFrontend = null;
      try {
         lbFrontend = initFrontend(assumedToExist, workspaceMaster);
      }
      catch (LBInitializationException e) {
         error(0, ExceptionUtils.getStackTrace(e));
         quit(1);
      }
      return lbFrontend;
   }

   public Map<String, Configuration> deserializeConfigurations(
         String serializedConfigPath) {
      print(1,
            "\n*** DESERIALIZING VENDOR-INDEPENDENT CONFIGURATION STRUCTURES ***\n");
      resetTimer();
      Map<String, Configuration> configurations = new TreeMap<String, Configuration>();
      File dir = new File(serializedConfigPath);
      File[] serializedConfigs = dir.listFiles();
      for (File serializedConfig : serializedConfigs) {
         String name = serializedConfig.getName();
         print(2, "Reading config: \"" + serializedConfig + "\"");
         Object object = deserializeObject(serializedConfig);
         Configuration c = (Configuration) object;
         configurations.put(name, c);
         print(2, "...OK\n");
      }
      printElapsedTime();
      return configurations;
   }

   private Map<String, TreeSet<FibRow>> deserializeFibs(File fibsFile) {
      new HashMap<String, TreeSet<FibRow>>();
      Map<String, TreeSet<FibRow>> fibs = new HashMap<String, TreeSet<FibRow>>();
      Object fibsObj = deserializeObject(fibsFile);
      Map<?, ?> fibsObjCast = (Map<?, ?>) fibsObj;
      for (Object key : fibsObjCast.keySet()) {
         String keyString = (String) key;
         Object value = fibsObjCast.get(key);
         TreeSet<?> setCast = (TreeSet<?>) value;
         TreeSet<FibRow> set = new TreeSet<FibRow>();
         for (Object setObj : setCast) {
            FibRow row = (FibRow) setObj;
            set.add(row);
         }
         fibs.put(keyString, set);
      }
      return fibs;
   }

   private Object deserializeObject(File inputFile) {
      XStream xstream = new XStream(new DomDriver("UTF-8"));
      FileInputStream fis;
      Object o = null;
      try {
         fis = new FileInputStream(inputFile);
         ObjectInputStream ois = xstream.createObjectInputStream(fis);
         o = ois.readObject();
         ois.close();
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
         quit(1);
      }
      catch (IOException e) {
         e.printStackTrace();
         quit(1);
      }
      catch (ClassNotFoundException e) {
         e.printStackTrace();
         quit(1);
      }
      return o;
   }

   private Set<Edge> deserializeTopologyEdges(File edgesFile) {
      Set<Edge> edges = new HashSet<Edge>();
      Object edgesObj = deserializeObject(edgesFile);
      Set<?> edgesObjCast = (Set<?>) edgesObj;
      for (Object edgeObj : edgesObjCast) {
         Edge edge = (Edge) edgeObj;
         edges.add(edge);
      }
      return edges;
   }

   public Map<String, VendorConfiguration> deserializeVendorConfigurations(
         String serializedVendorConfigPath) {
      print(1, "\n*** DESERIALIZING VENDOR CONFIGURATION STRUCTURES ***\n");
      resetTimer();
      Map<String, VendorConfiguration> vendorConfigurations = new TreeMap<String, VendorConfiguration>();
      File dir = new File(serializedVendorConfigPath);
      File[] serializedConfigs = dir.listFiles();
      for (File serializedConfig : serializedConfigs) {
         String name = serializedConfig.getName();
         print(2, "Reading vendor config: \"" + serializedConfig + "\"");
         Object object = deserializeObject(serializedConfig);
         VendorConfiguration vc = (VendorConfiguration) object;
         vendorConfigurations.put(name, vc);
         print(2, "...OK\n");
      }
      printElapsedTime();
      return vendorConfigurations;
   }

   private void dumpFacts(Map<String, StringBuilder> factBins) {
      print(0, "\n*** DUMPING FACTS ***\n");
      resetTimer();
      Path factsDir = Paths.get(_settings.getDumpFactsDir());
      try {
         Files.createDirectories(factsDir);
         for (String factsFilename : factBins.keySet()) {
            String facts = factBins.get(factsFilename).toString();
            Path factsFilePath = factsDir.resolve(factsFilename);
            print(1, "Writing: \"" + factsFilePath.toAbsolutePath().toString()
                  + "\"\n");
            FileUtils.write(factsFilePath.toFile(), facts);
         }
      }
      catch (IOException e) {
         e.printStackTrace();
         quit(1);
      }
      printElapsedTime();
   }

   public void error(int logLevel, String text) {
      if (_settings.getLogLevel() >= logLevel) {
         System.err.print(text);
         System.err.flush();
      }
   }

   private Map<String, String> extractPredicateSemantics(
         Map<String, String> logicFiles) {
      Map<String, String> predicateSemantics = new HashMap<String, String>();
      for (String absolutePath : logicFiles.keySet()) {
         String currentRules = logicFiles.get(absolutePath);
         ANTLRStringStream in = new ANTLRStringStream(currentRules);
         SemanticsLexer lexer = new SemanticsLexer(in);
         CommonTokenStream tokens = new CommonTokenStream(lexer);
         SemanticsParser parser = new SemanticsParser(tokens);
         print(2, "Parsing: \"" + absolutePath + "\"");
         try {
            predicateSemantics.putAll(parser.predicate_semantics());
         }
         catch (RecognitionException e) {
            print(2, " ...ERROR\n");
            e.printStackTrace();
            return null;
         }
         int numErrors = parser.getErrors().size() + lexer.getErrors().size();
         if (numErrors > 0) {
            error(0, " ..." + numErrors + " ERROR(S)\n");
            for (String msg : lexer.getErrors()) {
               error(2, "\tlexer: " + msg + "\n");
            }
            for (String msg : parser.getErrors()) {
               error(2, "\tparser: " + msg + "\n");
            }
            return null;
         }
         print(2, " ...OK\n");

      }
      return predicateSemantics;
   }

   private void genZ3(Map<String, Configuration> configurations) {
      print(0, "\n*** GENERATING Z3 LOGIC ***\n");
      resetTimer();

      Path fibsPath = Paths.get(_settings.getDataPlaneDir(), FIBS_FILENAME);
      Path edgesPath = Paths.get(_settings.getDataPlaneDir(), EDGES_FILENAME);

      print(1, "Deserializing fibs..");
      Map<String, TreeSet<FibRow>> fibs = deserializeFibs(fibsPath.toFile());
      print(1, "OK\n");

      print(1, "Deserializing toplogy edges..");
      Set<Edge> topologyEdges = deserializeTopologyEdges(edgesPath.toFile());
      print(1, "OK\n");

      print(1, "Synthesizing Z3 logic..");
      Synthesizer s = new Synthesizer(configurations, fibs, topologyEdges,
            _settings.getSimplify());
      try {
         s.synthesize(_settings.getZ3File());
      }
      catch (IOException e) {
         error(1, "ERROR\n");
         e.printStackTrace();
         quit(1);
      }
      print(1, "OK\n");
      printElapsedTime();

   }

   public void getCompare() {
      Map<String, VendorConfiguration> firstConfigurations = getVenderConfigurations(_settings
            .getCommits().get(0));
      if (firstConfigurations == null) {
         quit(1);
      }
      Map<String, VendorConfiguration> secondConfigurations = getVenderConfigurations(_settings
            .getCommits().get(1));
      if (secondConfigurations == null) {
         quit(1);
      }

      boolean changed = false;

      print(1, "*** Compare ***\n");
      print(1, "Compare between " + _settings.getCommits().get(0) + ":"
            + _settings.getCommits().get(1) + "\n");
      for (Entry<String, VendorConfiguration> e : firstConfigurations
            .entrySet()) {
         if (secondConfigurations.containsKey(e.getKey())) {
            if (firstConfigurations.get(e.getKey()).getVendor().equals("cisco")) {
               if (compareCiscoConfigurations(
                     (CiscoConfiguration) firstConfigurations.get(e.getKey()),
                     (CiscoConfiguration) secondConfigurations.get(e.getKey())))
                  changed = true;
            }
            else if (firstConfigurations.get(e.getKey()).getVendor()
                  .equals("cisco")) {
               if (compareJuniperConfigurations(
                     (JuniperConfiguration) firstConfigurations.get(e.getKey()),
                     (JuniperConfiguration) secondConfigurations.get(e.getKey())))
                  changed = true;
            }
         }
         else {
            print(1, "REMOVED NODE:"
                  + firstConfigurations.get(e.getKey()).getHostname() + "\n\n");
         }
      }

      Set<String> tmpSet = secondConfigurations.keySet();
      tmpSet.removeAll(firstConfigurations.keySet());
      for (String hostname : tmpSet) {
         print(1, "ADDED NODE:" + hostname + "\n\n");
      }

      if (changed) {
         print(1, "FINAL:CHANGED\n");
      }
      else {
         print(1, "FINAL:UNCHANGED\n");
      }
      print(1, "*** End Compare***\n\n");

   }

   private boolean compareJuniperConfigurations(
         JuniperConfiguration juniperConfiguration,
         JuniperConfiguration juniperConfiguration2) {
      boolean nodeChanged = false;
      boolean changed = compareJuniperVendorConfigurations(
            juniperConfiguration.getConfiguration(),
            juniperConfiguration2.getConfiguration());
      if (changed) {
         print(1,
               "_configuration(CONFIGS) CHANGED at "
                     + juniperConfiguration.getRouterID() + "\n");
         nodeChanged = true;
      }

      changed = (juniperConfiguration.getAsNum() != juniperConfiguration2
            .getAsNum());
      if (changed) {
         print(1, "_asNum(ASNS) CHANGED at " + juniperConfiguration.getRouterID()
               + "\n");
         nodeChanged = true;
      }

      changed = !Util.equalOrNull(juniperConfiguration.getRouterID(),
            juniperConfiguration2.getRouterID());
      if (changed) {
         print(1, "_routerID(RID) CHANGED at " + juniperConfiguration.getRouterID()
               + "\n");
         nodeChanged = true;
      }

      changed = !Util.sameRepresentationMaps(
            juniperConfiguration.getInterfaceAddressMap(),
            juniperConfiguration2.getInterfaceAddressMap());
      if (changed) {
         print(1,
               "_interfaceAddressMap(IAM) CHANGED at "
                     + juniperConfiguration.getRouterID() + "\n");
         nodeChanged = true;
      }
      if (nodeChanged) {
         print(1, "NODE " +juniperConfiguration.getRouterID() + " CHANGED\n\n");
      }

      return nodeChanged;

   }

   private boolean compareJuniperVendorConfigurations(
         JuniperVendorConfiguration configuration,
         JuniperVendorConfiguration configuration2) {
      boolean nodeChanged = false;

      boolean changed = (configuration.getAsNum() != configuration2.getAsNum());
      if (changed) {
         print(1, "_asNum CHANGED at " + configuration.getHostname() + "\n");
         nodeChanged = true;
      }

      changed = !Util.sameRepresentationMaps(
            configuration.getAsPathAccessLists(),
            configuration2.getAsPathAccessLists());
      if (changed) {
         print(1,
               "_asPathAccessLists CHANGED at " + configuration.getHostname()
                     + "\n");
         nodeChanged = true;
      }

      changed = compareBGPProcesses(configuration.getBGPProcesses(),
            configuration2.getBGPProcesses());
      if (changed) {
         print(1, "_bgpProcess CHANGED at " + configuration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = compareJuniperExpandedCommunityLists(
            configuration.getExpandedCommunityLists(),
            configuration2.getExpandedCommunityLists());
      if (changed) {
         print(1,
               "_expandedCommunityLists CHANGED at "
                     + configuration.getHostname() + "\n");
         nodeChanged = true;
      }

      changed = compareJuniperExtendedAccessLists(
            configuration.getExtendedAccessLists(),
            configuration2.getExtendedAccessLists());
      if (changed) {
         print(1,
               "_extendedAccessLists CHANGED at " + configuration.getHostname()
                     + "\n");
         nodeChanged = true;
      }

      changed = !Util.sameRepresentationLists(
            configuration.getGenerateRoutes(),
            configuration2.getGenerateRoutes());
      if (changed) {
         print(1, "_generateRoutes CHANGED at " + configuration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = (configuration.getHostname() != configuration2.getHostname());
      if (changed) {
         print(1, "_hostname CHANGED at " + configuration.getHostname() + "\n");
         nodeChanged = true;
      }

      changed = compareJuniperInterfaces(configuration.getInterfaces(),
            configuration2.getInterfaces());
      if (changed) {
         print(1, "_interfaces CHANGED at " + configuration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = compareOspfProcesses(configuration.getOSPFProcesses(),
            configuration2.getOSPFProcesses());
      if (changed) {
         print(1, "_ospfProcess CHANGED at " + configuration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = comparePolicyStatements(configuration.getPolicyStatements(),
            configuration2.getPolicyStatements());
      if (changed) {
         print(1, "_policyStatements CHANGED at " + configuration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = compareRouteFilters(configuration.getRouteFilter(),
            configuration2.getRouteFilter());
      if (changed) {
         print(1, "_routeFilters CHANGED at " + configuration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = !Util.sameRepresentationLists(configuration.getStaticRoutes(),
            configuration2.getStaticRoutes());
      if (changed) {
         print(1, "_staticRoutes CHANGED at " + configuration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      if (nodeChanged) {
         print(1, configuration.getHostname() + " CHANGED\n\n");
      }
      return nodeChanged;
   }

   private boolean compareRouteFilters(Map<String, RouteFilter> routeFilter,
         Map<String, RouteFilter> routeFilter2) {
      if (routeFilter == null && routeFilter2 == null)
         return false;
      boolean res = false;
      if (routeFilter == null && routeFilter2 != null) {
         for (Entry<String, RouteFilter> e : routeFilter2.entrySet()) {
            print(1, "ADDED route filter:" + e.getKey() + "\n");
         }
         return true;
      }
      if (routeFilter2 == null && routeFilter != null) {
         for (Entry<String, RouteFilter> e : routeFilter.entrySet()) {
            print(1, "REMOVED route filter:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, RouteFilter> e : routeFilter.entrySet()) {
         if (routeFilter2.containsKey(e.getKey())) {
            boolean listChange = false;
            RouteFilter list1 = routeFilter.get(e.getKey());
            RouteFilter list2 = routeFilter2.get(e.getKey());
            if (!Util.equalOrNull(list1.getName(), list2.getName())) {
               print(1, "_name, ");
               listChange = true;
            }
            if (!Util.sameRepresentationLists(list1.getLines(),
                  list2.getLines())) {
               print(1, "_lines, ");
               listChange = true;
            }

            if (listChange) {
               print(1, "CHANGED at route filter with key " + e.getKey() + "\n");
               res = true;
            }
         }
         else {
            print(1, "REMOVED route filter:" + e.getKey() + "\n");
            res = true;
         }
      }

      Set<String> tmpSet = routeFilter2.keySet();
      tmpSet.removeAll(routeFilter.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED route filter:" + key + "\n");
      }
      return res;
   }

   private boolean comparePolicyStatements(
         Map<String, PolicyStatement> policyStatements,
         Map<String, PolicyStatement> policyStatements2) {
      if (policyStatements == null && policyStatements2 == null)
         return false;
      boolean res = false;
      if (policyStatements == null && policyStatements2 != null) {
         for (Entry<String, PolicyStatement> e : policyStatements2.entrySet()) {
            print(1, "ADDED policy statement:" + e.getKey() + "\n");
         }
         return true;
      }
      if (policyStatements2 == null && policyStatements != null) {
         for (Entry<String, PolicyStatement> e : policyStatements.entrySet()) {
            print(1, "REMOVED policy statement:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, PolicyStatement> e : policyStatements.entrySet()) {
         if (policyStatements2.containsKey(e.getKey())) {
            boolean listChange = false;
            PolicyStatement list1 = policyStatements.get(e.getKey());
            PolicyStatement list2 = policyStatements2.get(e.getKey());
            if (!Util.equalOrNull(list1.getMapName(), list2.getMapName())) {
               print(1, "_mapName, ");
               listChange = true;
            }
            if (!Util.sameRepresentationMaps(list1.getClauseMap(),
                  list2.getClauseMap())) {
               print(1, "_clauses, ");
               listChange = true;
            }

            if (listChange) {
               print(1, "CHANGED at policy statement with key " + e.getKey()
                     + "\n");
               res = true;
            }
         }
         else {
            print(1, "REMOVED policy statement:" + e.getKey() + "\n");
            res = true;
         }
      }

      Set<String> tmpSet = policyStatements2.keySet();
      tmpSet.removeAll(policyStatements.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED policy statement:" + key + "\n");
      }
      return res;
   }

   private boolean compareOspfProcesses(List<OSPFProcess> ospfProcesses,
         List<OSPFProcess> ospfProcesses2) {
      return Util.sameRepresentationLists(ospfProcesses, ospfProcesses2);
   }

   private boolean compareJuniperInterfaces(
         List<batfish.representation.juniper.Interface> interfaces,
         List<batfish.representation.juniper.Interface> interfaces2) {
      return Util.sameRepresentationLists(interfaces, interfaces2);
   }

   private boolean compareJuniperExtendedAccessLists(
         Map<String, batfish.representation.juniper.ExtendedAccessList> extendedAccessLists,
         Map<String, batfish.representation.juniper.ExtendedAccessList> extendedAccessLists2) {
      if (extendedAccessLists == null && extendedAccessLists2 == null)
         return false;
      boolean res = false;
      if (extendedAccessLists == null && extendedAccessLists2 != null) {
         for (Entry<String, batfish.representation.juniper.ExtendedAccessList> e : extendedAccessLists2
               .entrySet()) {
            print(1, "ADDED extended access list:" + e.getKey() + "\n");
         }
         return true;
      }
      if (extendedAccessLists2 == null && extendedAccessLists != null) {
         for (Entry<String, batfish.representation.juniper.ExtendedAccessList> e : extendedAccessLists
               .entrySet()) {
            print(1, "REMOVED extended access list:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, batfish.representation.juniper.ExtendedAccessList> e : extendedAccessLists
            .entrySet()) {
         if (extendedAccessLists2.containsKey(e.getKey())) {
            boolean listChange = false;
            batfish.representation.juniper.ExtendedAccessList list1 = extendedAccessLists
                  .get(e.getKey());
            batfish.representation.juniper.ExtendedAccessList list2 = extendedAccessLists2
                  .get(e.getKey());
            if (!Util.equalOrNull(list1.getId(), list2.getId())) {
               print(1, "_id, ");
               listChange = true;
            }

            if (!Util.sameRepresentationLists(list1.getLines(),
                  list2.getLines())) {
               print(1, "_lines, ");
               listChange = true;
            }

            if (!Util.sameRepresentationLists(list1.getTerms(),
                  list2.getTerms())) {
               print(1, "_terms, ");
               listChange = true;
            }

            if (listChange) {
               print(1,
                     "CHANGED at extended access list with key " + e.getKey()
                           + "\n");
               res = true;
            }
         }
         else {
            print(1, "REMOVED extended access list:" + e.getKey() + "\n");
            res = true;
         }
      }

      Set<String> tmpSet = extendedAccessLists2.keySet();
      tmpSet.removeAll(extendedAccessLists.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED extended access list:" + key + "\n");
      }
      return res;
   }

   private boolean compareJuniperExpandedCommunityLists(
         Map<String, batfish.representation.juniper.ExpandedCommunityList> expandedCommunityLists,
         Map<String, batfish.representation.juniper.ExpandedCommunityList> expandedCommunityLists2) {
      if (expandedCommunityLists == null && expandedCommunityLists2 == null)
         return false;
      boolean res = false;
      if (expandedCommunityLists == null && expandedCommunityLists2 != null) {
         for (Entry<String, batfish.representation.juniper.ExpandedCommunityList> e : expandedCommunityLists2
               .entrySet()) {
            print(1, "ADDED expanded community list:" + e.getKey() + "\n");
         }
         return true;
      }
      if (expandedCommunityLists2 == null && expandedCommunityLists != null) {
         for (Entry<String, batfish.representation.juniper.ExpandedCommunityList> e : expandedCommunityLists
               .entrySet()) {
            print(1, "REMOVED expanded community list:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, batfish.representation.juniper.ExpandedCommunityList> e : expandedCommunityLists
            .entrySet()) {
         if (expandedCommunityLists2.containsKey(e.getKey())) {
            boolean listChange = false;
            batfish.representation.juniper.ExpandedCommunityList list1 = expandedCommunityLists
                  .get(e.getKey());
            batfish.representation.juniper.ExpandedCommunityList list2 = expandedCommunityLists2
                  .get(e.getKey());
            if (!Util.equalOrNull(list1.getName(), list2.getName())) {
               print(1, "_name, ");
               listChange = true;
            }
            if (!Util.sameRepresentationLists(list1.getLines(),
                  list2.getLines())) {
               print(1, "_lines, ");
               listChange = true;
            }

            if (listChange) {
               print(1,
                     "CHANGED at expanded community list with key "
                           + e.getKey() + "\n");
               res = true;
            }
         }
         else {
            print(1, "REMOVED expanded community list:" + e.getKey() + "\n");
            res = true;
         }
      }

      Set<String> tmpSet = expandedCommunityLists2.keySet();
      tmpSet.removeAll(expandedCommunityLists.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED expanded community list:" + key + "\n");
      }
      return res;
   }

   private boolean compareBGPProcesses(List<BGPProcess> bgpProcesses,
         List<BGPProcess> bgpProcesses2) {
      return Util.sameRepresentationLists(bgpProcesses, bgpProcesses2);
   }

   public boolean compareCiscoConfigurations(
         CiscoConfiguration ciscoConfiguration,
         CiscoConfiguration ciscoConfiguration2) {

      boolean nodeChanged = false;
      boolean changed = compareIPAsPathAccessLists(
            ciscoConfiguration.getAsPathAccessLists(),
            ciscoConfiguration2.getAsPathAccessLists());
      if (changed) {
         print(1,
               "_asPathAccessLists(ASAL) CHANGED at "
                     + ciscoConfiguration.getHostname() + "\n");
         nodeChanged = true;
      }

      changed = compareBGPProcess(ciscoConfiguration.getBgpProcess(),
            ciscoConfiguration2.getBgpProcess());
      if (changed) {
         print(1, "_bgpProcess(BGP) CHANGED at " + ciscoConfiguration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = compareExpandedCommunityLists(
            ciscoConfiguration.getExpandedCommunityLists(),
            ciscoConfiguration2.getExpandedCommunityLists());
      if (changed) {
         print(1,
               "_expandedCommunityLists(ECL) CHANGED at "
                     + ciscoConfiguration.getHostname() + "\n");
         nodeChanged = true;
      }

      changed = compareExtendedAccessLists(
            ciscoConfiguration.getExtendedAcls(),
            ciscoConfiguration2.getExtendedAcls());
      if (changed) {
         print(1,
               "_extendedAccessLists(EAL) CHANGED at "
                     + ciscoConfiguration.getHostname() + "\n");
         nodeChanged = true;
      }

      changed = compareInterfaces(ciscoConfiguration.getInterfaces(),
            ciscoConfiguration2.getInterfaces());
      if (changed) {
         print(1, "_interfaces(IS) CHANGED at " + ciscoConfiguration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = compareOspfProcess(ciscoConfiguration.getOspfProcess(),
            ciscoConfiguration2.getOspfProcess());
      if (changed) {
         print(1, "_ospfProcess(OSPF) CHANGED at " + ciscoConfiguration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = comparePrefixLists(ciscoConfiguration.getPrefixLists(),
            ciscoConfiguration2.getPrefixLists());
      if (changed) {
         print(1, "_prefixLists(PXLIST) CHANGED at " + ciscoConfiguration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = compareRouteMap(ciscoConfiguration.getRouteMaps(),
            ciscoConfiguration2.getRouteMaps());
      if (changed) {
         print(1, "_routeMaps(RMAP) CHANGED at " + ciscoConfiguration.getHostname()
               + "\n");
         nodeChanged = true;
      }

      changed = compareStandardAccessLists(
            ciscoConfiguration.getStandardAcls(),
            ciscoConfiguration2.getStandardAcls());
      if (changed) {
         print(1,
               "_standardAccessLists(SAL) CHANGED at "
                     + ciscoConfiguration.getHostname() + "\n");
         nodeChanged = true;
      }

      changed = compareStandardCommunityLists(
            ciscoConfiguration.getStandardCommunityLists(),
            ciscoConfiguration2.getStandardCommunityLists());
      if (changed) {
         print(1,
               "_standardCommunityLists(SCL) CHANGED at "
                     + ciscoConfiguration.getHostname() + "\n");
         nodeChanged = true;
      }

      changed = compareStaticRoutes(ciscoConfiguration.getStaticRoutes(),
            ciscoConfiguration2.getStaticRoutes());
      if (changed) {
         print(1,
               "_staticRoutes(SR) CHANGED at " + ciscoConfiguration.getHostname()
                     + "\n");
         nodeChanged = true;
      }

      if (nodeChanged) {
         print(1, "NODE " + ciscoConfiguration.getHostname() + " CHANGED\n\n");
      }

      return nodeChanged;
   }

   private boolean compareStaticRoutes(Map<String, StaticRoute> staticRoutes,
         Map<String, StaticRoute> staticRoutes2) {
      boolean res = false;
      if (staticRoutes == null && staticRoutes2 == null)
         return false;

      if (staticRoutes == null && staticRoutes2 != null) {
         for (Entry<String, StaticRoute> e : staticRoutes2.entrySet()) {
            print(1, "ADDED static route:" + e.getKey() + "\n");
         }
         return true;
      }
      if (staticRoutes2 == null && staticRoutes != null) {
         for (Entry<String, StaticRoute> e : staticRoutes.entrySet()) {
            print(1, "REMOVED static route:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, StaticRoute> e : staticRoutes.entrySet()) {
         if (staticRoutes2.containsKey(e.getKey())) {
            boolean routeChange = false;
            StaticRoute route1 = staticRoutes.get(e.getKey());
            StaticRoute route2 = staticRoutes2.get(e.getKey());
            if (route1.getDistance() != route2.getDistance()) {
               routeChange = true;
               print(1, "_distance, ");
            }
            if (!Util.equalOrNull(route1.getMask(), route2.getMask())) {
               routeChange = true;
               print(1, "_mask, ");
            }
            if (!Util.equalOrNull(route1.getNextHopInterface(),
                  route2.getNextHopInterface())) {
               routeChange = true;
               print(1, "_nextHopInterface, ");
            }
            if (!Util.equalOrNull(route1.getNextHopIp(), route2.getNextHopIp())) {
               routeChange = true;
               print(1, "_nextHopIp, ");
            }
            if (route1.getPermanent() != route2.getPermanent()) {
               routeChange = true;
               print(1, "_permanent, ");
            }
            if (!Util.equalOrNull(route1.getPrefix(), route2.getPrefix())) {
               routeChange = true;
               print(1, "_prefix, ");
            }
            if (!Util.equalOrNull(route1.getTag(), route2.getTag())) {
               routeChange = true;
               print(1, "_tag, ");
            }
            if (!Util.equalOrNull(route1.getTrack(), route2.getTrack())) {
               routeChange = true;
               print(1, "_track, ");
            }
            if (routeChange) {
               print(1, "CHANGED at static route with key " + e.getKey() + "\n");
               res = true;
            }
         }
         else {
            print(1, "REMOVED static route:" + e.getKey() + "\n");
            res = true;
         }
      }

      Set<String> tmpSet = staticRoutes2.keySet();
      tmpSet.removeAll(staticRoutes.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED static route:" + key + "\n");
      }
      return res;
   }

   private boolean compareStandardCommunityLists(
         Map<String, StandardCommunityList> standardCommunityLists,
         Map<String, StandardCommunityList> standardCommunityLists2) {
      if (standardCommunityLists == null && standardCommunityLists2 == null)
         return false;
      boolean res = false;
      if (standardCommunityLists == null && standardCommunityLists2 != null) {
         for (Entry<String, StandardCommunityList> e : standardCommunityLists2
               .entrySet()) {
            print(1, "ADDED standard community list:" + e.getKey() + "\n");
         }
         return true;
      }
      if (standardCommunityLists2 == null && standardCommunityLists != null) {
         for (Entry<String, StandardCommunityList> e : standardCommunityLists
               .entrySet()) {
            print(1, "REMOVED standard community list:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, StandardCommunityList> e : standardCommunityLists
            .entrySet()) {
         if (standardCommunityLists2.containsKey(e.getKey())) {
            boolean listChange = false;
            StandardCommunityList list1 = standardCommunityLists
                  .get(e.getKey());
            StandardCommunityList list2 = standardCommunityLists2.get(e
                  .getKey());
            if (!Util.equalOrNull(list1.getName(), list2.getName())) {
               print(1, "_name, ");
               listChange = true;
            }
            if (!Util.sameRepresentationLists(list1.getLines(),
                  list2.getLines())) {
               print(1, "_lines, ");
               listChange = true;
            }

            if (listChange) {
               print(1,
                     "CHANGED at standard community list with key "
                           + e.getKey() + "\n");
               res = true;
            }
         }
         else {
            print(1, "REMOVED standard community list:" + e.getKey() + "\n");
            res = true;
         }
      }

      Set<String> tmpSet = standardCommunityLists2.keySet();
      tmpSet.removeAll(standardCommunityLists.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED standard community list:" + key + "\n");
      }
      return res;
   }

   private boolean compareStandardAccessLists(
         Map<String, StandardAccessList> standardAcls,
         Map<String, StandardAccessList> standardAcls2) {
      if (standardAcls == null && standardAcls2 == null)
         return false;
      boolean res = false;
      if (standardAcls == null && standardAcls2 != null) {
         for (Entry<String, StandardAccessList> e : standardAcls2.entrySet()) {
            print(1, "ADDED standard access list:" + e.getKey() + "\n");
         }
         return true;
      }
      if (standardAcls2 == null && standardAcls != null) {
         for (Entry<String, StandardAccessList> e : standardAcls.entrySet()) {
            print(1, "REMOVED standard access list:" + e.getKey() + "\n");
         }
         return true;
      }
      for (Entry<String, StandardAccessList> e : standardAcls.entrySet()) {
         if (standardAcls2.containsKey(e.getKey())) {
            boolean listChanged = false;
            StandardAccessList list1 = e.getValue();
            StandardAccessList list2 = standardAcls2.get(e.getKey());
            if (!Util.equalOrNull(list1.getId(), list2.getId())) {
               listChanged = true;
               print(1, "_id, ");
            }
            if (!Util.sameRepresentationLists(list1.getLines(),
                  list2.getLines())) {
               listChanged = true;
               print(1, "_lines, ");
            }
            if (listChanged) {
               res = true;
               print(1,
                     "CHANGED at standard access list with key " + e.getKey()
                           + "\n");
            }
         }
         else {
            res = true;
            print(1, "REMOVED standard access list with key " + e.getKey()
                  + "\n");
         }
      }

      Set<String> tmpSet = standardAcls2.keySet();
      tmpSet.removeAll(standardAcls.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED standard access list with key " + key + "\n");
      }

      return res;
   }

   private boolean compareRouteMap(Map<String, RouteMap> routeMaps,
         Map<String, RouteMap> routeMaps2) {
      if (routeMaps == null & routeMaps2 == null)
         return false;
      boolean res = false;
      if (routeMaps == null && routeMaps2 != null) {
         for (Entry<String, RouteMap> e : routeMaps2.entrySet()) {
            print(1, "ADDED route map:" + e.getKey() + "\n");
         }
         return true;
      }
      if (routeMaps2 == null && routeMaps != null) {
         for (Entry<String, RouteMap> e : routeMaps.entrySet()) {
            print(1, "REMOVED route map:" + e.getKey() + "\n");
         }
         return true;
      }
      for (Entry<String, RouteMap> e : routeMaps.entrySet()) {
         if (routeMaps2.containsKey(e.getKey())) {
            boolean mapChanged = false;
            RouteMap map1 = e.getValue();
            RouteMap map2 = routeMaps2.get(e.getKey());
            if (compareRouteMapClauses(map1.getClauses(), map2.getClauses())) {
               mapChanged = true;
               print(1, "_clauses, ");
            }
            if (map1.getIgnore() != map2.getIgnore()) {
               mapChanged = true;
               print(1, "_ignore, ");
            }
            if (!Util.equalOrNull(map1.getMapName(), map2.getMapName())) {
               mapChanged = true;
               print(1, "_mapName, ");
            }
            if (mapChanged) {
               res = true;
               print(1, "CHANGED at route map with key " + e.getKey() + "\n");
            }
         }
         else {
            res = true;
            print(1, "REMOVED route map with key " + e.getKey() + "\n");
         }
      }

      Set<String> tmpSet = routeMaps2.keySet();
      tmpSet.removeAll(routeMaps.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED route map with key " + key + "\n");
      }

      return res;
   }

   private boolean compareRouteMapClauses(Map<Integer, RouteMapClause> clauses,
         Map<Integer, RouteMapClause> clauses2) {
      if (clauses == null && clauses2 == null)
         return false;
      boolean res = false;
      if (clauses == null && clauses2 != null) {
         for (Entry<Integer, RouteMapClause> e : clauses2.entrySet()) {
            print(1, "ADDED route map clause:" + e.getKey() + "\n");
         }
         return true;
      }
      if (clauses2 == null && clauses != null) {
         for (Entry<Integer, RouteMapClause> e : clauses.entrySet()) {
            print(1, "REMOVED route map clause:" + e.getKey() + "\n");
         }
         return true;
      }
      for (Entry<Integer, RouteMapClause> e : clauses.entrySet()) {
         if (clauses2.containsKey(e.getKey())) {
            boolean clauseChanged = false;
            RouteMapClause clause1 = e.getValue();
            RouteMapClause clause2 = clauses2.get(e.getKey());
            if (!Util.equalOrNull(clause1.getAction(), clause2.getAction())) {
               clauseChanged = true;
               print(1, "_action, ");
            }
            if (clause1.getIgnore() != clause2.getIgnore()) {
               clauseChanged = true;
               print(1, "_ignore, ");
            }
            if (!Util.equalOrNull(clause1.getMapName(), clause2.getMapName())) {
               clauseChanged = true;
               print(1, "_mapName, ");
            }
            if (!Util.sameRepresentationLists(clause1.getMatchList(),
                  clause2.getMatchList())) {
               clauseChanged = true;
               print(1, "_matchList, ");
            }
            if (clause1.getSeqNum() != clause2.getSeqNum()) {
               clauseChanged = true;
               print(1, "_seqNum, ");
            }
            if (!Util.sameRepresentationLists(clause1.getSetList(),
                  clause2.getSetList())) {
               clauseChanged = true;
               print(1, "_setList, ");
            }
            if (clauseChanged) {
               res = true;
               print(1, "CHANGED at route map clause with key " + e.getKey()
                     + "\n");
            }
         }
         else {
            res = true;
            print(1, "REMOVED route map clause with key " + e.getKey() + "\n");
         }
      }

      Set<Integer> tmpSet = clauses2.keySet();
      tmpSet.removeAll(clauses.keySet());
      for (Integer key : tmpSet) {
         res = true;
         print(1, "ADDED route map clause with key " + key + "\n");
      }

      return res;
   }

   private boolean comparePrefixLists(Map<String, PrefixList> prefixLists,
         Map<String, PrefixList> prefixLists2) {
      if (prefixLists == null && prefixLists2 == null)
         return false;
      boolean res = false;
      if (prefixLists == null && prefixLists2 != null) {
         for (Entry<String, PrefixList> e : prefixLists2.entrySet()) {
            print(1, "ADDED  prefix list:" + e.getKey() + "\n");
         }
         return true;
      }
      if (prefixLists2 == null && prefixLists != null) {
         for (Entry<String, PrefixList> e : prefixLists.entrySet()) {
            print(1, "REMOVED  prefix list:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, PrefixList> e : prefixLists.entrySet()) {
         if (prefixLists2.containsKey(e.getKey())) {
            boolean plistChanged = false;
            PrefixList plist1 = e.getValue();
            PrefixList plist2 = prefixLists2.get(e.getKey());
            if (!Util.equalOrNull(plist1.getName(), plist2.getName())) {
               plistChanged = true;
               print(1, "_name, ");
            }
            if (!Util.sameRepresentationLists(plist1.getLines(),
                  plist2.getLines())) {
               plistChanged = true;
               print(1, "_lines, ");
            }
            if (plistChanged) {
               res = true;
               print(1, "CHANGED at prefix list with key " + e.getKey() + "\n");
            }
         }
         else {
            res = true;
            print(1, "REMOVED prefix list with key " + e.getKey() + "\n");
         }
      }

      Set<String> tmpSet = prefixLists2.keySet();
      tmpSet.removeAll(prefixLists.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED prefix list with key " + key + "\n");
      }

      return res;
   }

   private boolean compareOspfProcess(OspfProcess ospfProcess,
         OspfProcess ospfProcess2) {
      if (ospfProcess == null && ospfProcess2 == null)
         return false;
      if (ospfProcess2 == null && ospfProcess != null) {
         return true;
      }
      if (ospfProcess == null && ospfProcess2 != null) {
         return true;
      }
      boolean res = false;
      if (ospfProcess.getDefaultInformationMetric() != ospfProcess2
            .getDefaultInformationMetric()) {
         res = true;
         print(1, "_defaultInformationMetric, ");
      }
      if (!Util.equalOrNull(ospfProcess.getDefaultInformationMetricType(),
            ospfProcess2.getDefaultInformationMetricType())) {
         res = true;
         print(1, "_defaultInformationMetricType, ");
      }
      if (ospfProcess.getDefaultInformationOriginate() != ospfProcess2
            .getDefaultInformationOriginate()) {
         res = true;
         print(1, "_defaultInformationOriginate, ");
      }

      if (ospfProcess.getDefaultInformationOriginateAlways() != ospfProcess2
            .getDefaultInformationOriginateAlways()) {
         res = true;
         print(1, "_defaultInformationOriginateAlways, ");
      }

      if (!Util.equalOrNull(ospfProcess.getDefaultInformationOriginateMap(),
            ospfProcess2.getDefaultInformationOriginateMap())) {
         res = true;
         print(1, "_defaultInformationOriginateMap, ");
      }

      if (!Util.sameRepresentationSets(ospfProcess.getInterfaceBlacklist(),
            ospfProcess2.getInterfaceBlacklist())) {
         res = true;
         print(1, "_interfaceBlacklist, ");
      }

      if (!Util.sameRepresentationSets(ospfProcess.getInterfaceWhitelist(),
            ospfProcess2.getInterfaceWhitelist())) {
         res = true;
         print(1, "_interfaceWhitelist, ");
      }

      if (!Util.sameRepresentationSets(ospfProcess.getNetworks(),
            ospfProcess2.getNetworks())) {
         res = true;
         print(1, "_networks, ");
      }

      if (!Util.sameRepresentationMaps(ospfProcess.getNssas(),
            ospfProcess2.getNssas())) {
         res = true;
         print(1, "_nssas, ");
      }

      if (ospfProcess.getPid() != ospfProcess2.getPid()) {
         res = true;
         print(1, "_pid, ");
      }

      if (!Util.sameRepresentationMaps(ospfProcess.getRedistributionPolicies(),
            ospfProcess2.getRedistributionPolicies())) {
         res = true;
         print(1, "_redistributionPolicies, ");
      }

      if (ospfProcess.getReferenceBandwidth() != ospfProcess2
            .getReferenceBandwidth()) {
         res = true;
         print(1, "_referenceBandwidth, ");
      }

      if (!Util.equalOrNull(ospfProcess.getRouterId(),
            ospfProcess2.getRouterId())) {
         res = true;
         print(1, "_routerId, ");
      }

      if (!Util.sameRepresentationSets(ospfProcess.getWildcardNetworks(),
            ospfProcess2.getWildcardNetworks())) {
         res = true;
         print(1, "_wildcardNetworks, ");
      }

      if (res) {
         print(1, "CHANGED at OspfProcess.\n");
      }
      return res;
   }

   private boolean compareInterfaces(Map<String, Interface> interfaces,
         Map<String, Interface> interfaces2) {
      if (interfaces == null && interfaces2 == null)
         return false;
      boolean res = false;
      if (interfaces == null && interfaces2 != null) {
         for (Entry<String, Interface> e : interfaces2.entrySet()) {
            print(1, "ADDED  interface:" + e.getKey() + "\n");
         }
         return true;
      }
      if (interfaces2 == null && interfaces != null) {
         for (Entry<String, Interface> e : interfaces.entrySet()) {
            print(1, "REMOVED  interface:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, Interface> e : interfaces.entrySet()) {
         if (interfaces2.containsKey(e.getKey())) {
            boolean interfaceChanged = false;
            Interface interface1 = e.getValue();
            Interface interface2 = interfaces2.get(e.getKey());

            if (interface1.getAccessVlan() != interface2.getAccessVlan()) {
               interfaceChanged = true;
               print(1, "_accessVlan, ");
            }

            if (interface1.getActive() != interface2.getActive()) {
               interfaceChanged = true;
               print(1, "_active, ");
            }

            if (!Util.sameRepresentationLists(interface1.getAllowedVlans(),
                  interface2.getAllowedVlans())) {
               interfaceChanged = true;
               print(1, "_allowedVlans, ");
            }

            if (!Util.equalOrNull(interface1.getArea(), interface2.getArea())) {
               interfaceChanged = true;
               print(1, "_area, ");
            }

            if (!Util.equalOrNull(interface1.getBandwidth(),
                  interface2.getBandwidth())) {
               interfaceChanged = true;
               print(1, "_bandwidth, ");
            }

            if (!Util.equalOrNull(interface1.getIncomingFilter(),
                  interface2.getIncomingFilter())) {
               interfaceChanged = true;
               print(1, "_incomingFilter, ");
            }

            if (!Util.equalOrNull(interface1.getIP(), interface2.getIP())) {
               interfaceChanged = true;
               print(1, "_ip, ");
            }

            if (!Util.equalOrNull(interface1.getName(), interface2.getName())) {
               interfaceChanged = true;
               print(1, "_name, ");
            }

            if (interface1.getNativeVlan() != interface2.getNativeVlan()) {
               interfaceChanged = true;
               print(1, "_nativeVlan, ");
            }

            if (!Util.equalOrNull(interface1.getOspfCost(),
                  interface2.getOspfCost())) {
               interfaceChanged = true;
               print(1, "_ospfCost, ");
            }

            if (interface1.getOspfDeadInterval() != interface2
                  .getOspfDeadInterval()) {
               interfaceChanged = true;
               print(1, "_ospfDeadInterval, ");
            }

            if (interface1.getOspfHelloMultiplier() != interface2
                  .getOspfHelloMultiplier()) {
               interfaceChanged = true;
               print(1, "_ospfHelloMultiplier, ");
            }

            if (!Util.equalOrNull(interface1.getOutgoingFilter(),
                  interface2.getOutgoingFilter())) {
               interfaceChanged = true;
               print(1, "_outgoingFilter, ");
            }

            if (!Util.sameRepresentationMaps(interface1.getSecondaryIps(),
                  interface2.getSecondaryIps())) {
               interfaceChanged = true;
               print(1, "_secondaryIps, ");
            }

            if (!Util.equalOrNull(interface1.getSubnetMask(),
                  interface2.getSubnetMask())) {
               interfaceChanged = true;
               print(1, "_subnet, ");
            }

            if (!Util.equalOrNull(interface1.getSwitchportMode(),
                  interface2.getSwitchportMode())) {
               interfaceChanged = true;
               print(1, "_switchportMode, ");
            }

            if (!Util.equalOrNull(interface1.getSwitchportTrunkEncapsulation(),
                  interface2.getSwitchportTrunkEncapsulation())) {
               interfaceChanged = true;
               print(1, "_switchportTrunkEncapsulation, ");
            }

            if (interfaceChanged) {
               res = true;
               print(1, "CHANGED at interface with key " + e.getKey() + "\n");
            }

         }
         else {
            res = true;
            print(1, "REMOVED interface with key " + e.getKey() + "\n");
         }
      }

      Set<String> tmpSet = interfaces2.keySet();
      tmpSet.removeAll(interfaces.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED interface with key " + key + "\n");
      }

      return res;
   }

   private boolean compareExtendedAccessLists(
         Map<String, ExtendedAccessList> extendedAcls,
         Map<String, ExtendedAccessList> extendedAcls2) {
      if (extendedAcls == null && extendedAcls2 == null)
         return false;
      boolean res = false;
      if (extendedAcls == null && extendedAcls2 != null) {
         for (Entry<String, ExtendedAccessList> e : extendedAcls2.entrySet()) {
            print(1, "ADDED  extended access list:" + e.getKey() + "\n");
         }
         return true;
      }
      if (extendedAcls2 == null && extendedAcls != null) {
         for (Entry<String, ExtendedAccessList> e : extendedAcls.entrySet()) {
            print(1, "REMOVED  extended access list:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, ExtendedAccessList> e : extendedAcls.entrySet()) {
         if (extendedAcls2.containsKey(e.getKey())) {
            boolean elistChanged = false;
            ExtendedAccessList elist1 = e.getValue();
            ExtendedAccessList elist2 = extendedAcls2.get(e.getKey());

            if (!Util.equalOrNull(elist1.getId(), elist2.getId())) {
               elistChanged = true;
               print(1, "_id, ");
            }

            if (!Util.sameRepresentationLists(elist1.getLines(),
                  elist2.getLines())) {
               elistChanged = true;
               print(1, "_lines, ");
            }

            if (elistChanged) {
               res = true;
               print(1,
                     "CHANGED at extended access list with key " + e.getKey()
                           + "\n");
            }
         }
         else {
            res = true;
            print(1, "REMOVED extended access list with key " + e.getKey()
                  + "\n");
         }
      }

      Set<String> tmpSet = extendedAcls2.keySet();
      tmpSet.removeAll(extendedAcls.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED extended access list with key " + key + "\n");
      }

      return res;
   }

   private boolean compareExpandedCommunityLists(
         Map<String, ExpandedCommunityList> map,
         Map<String, ExpandedCommunityList> map2) {
      if (map == null && map2 == null)
         return false;
      boolean res = false;
      if (map == null && map2 != null) {
         for (Entry<String, ExpandedCommunityList> e : map2.entrySet()) {
            print(1, "ADDED expanded community list:" + e.getKey() + "\n");
         }
         return true;
      }
      if (map2 == null && map != null) {
         for (Entry<String, ExpandedCommunityList> e : map.entrySet()) {
            print(1, "REMOVED expanded community list:" + e.getKey() + "\n");
         }
         return true;
      }

      for (Entry<String, ExpandedCommunityList> e : map.entrySet()) {
         if (map2.containsKey(e.getKey())) {
            boolean elistChanged = false;
            ExpandedCommunityList elist1 = e.getValue();
            ExpandedCommunityList elist2 = map.get(e.getKey());

            if (!Util.equalOrNull(elist1.getName(), elist2.getName())) {
               elistChanged = true;
               print(1, "_id, ");
            }

            if (!Util.equalOrNull(elist1.getLines(), elist2.getLines())) {
               elistChanged = true;
               print(1, "_lines, ");
            }

            if (elistChanged) {
               res = true;
               print(1,
                     "CHANGED at expanded community list with key "
                           + e.getKey() + "\n");
            }
         }
         else {
            res = true;
            print(1, "REMOVED expanded community list with key " + e.getKey()
                  + "\n");
         }
      }

      Set<String> tmpSet = map2.keySet();
      tmpSet.removeAll(map.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED expanded community list with key " + key + "\n");
      }

      return res;
   }

   private boolean compareBGPProcess(BgpProcess bgpProcess,
         BgpProcess bgpProcess2) {
      if (bgpProcess == null && bgpProcess2 == null)
         return false;
      if (bgpProcess2 == null && bgpProcess != null) {
         return true;
      }
      if (bgpProcess == null && bgpProcess2 != null) {
         return true;
      }

      boolean res = false;
      if (!Util.equalOrNull(bgpProcess.getActivatedNeighbors(),
            bgpProcess2.getActivatedNeighbors())) {
         res = true;
         print(1, "_activatedNeighbors, ");
      }
      if (!Util.equalOrNull(bgpProcess.getAggregateNetworks(),
            bgpProcess2.getAggregateNetworks())) {
         res = true;
         print(1, "_aggregateNetworks, ");
      }
      if (!Util.sameRepresentationMaps(bgpProcess.getAllPeerGroups(),
            bgpProcess2.getAllPeerGroups())) {
         res = true;
         print(1, "_allPeerGroups, ");
      }

      if (!Util.equalOrNull(bgpProcess.getClusterId(),
            bgpProcess2.getClusterId())) {
         res = true;
         print(1, "_clusterId, ");
      }

      if (bgpProcess.getDefaultMetric() != bgpProcess2.getDefaultMetric()) {
         res = true;
         print(1, "_defaultMetric, ");
      }

      if (bgpProcess.getDefaultNeighborActivate() != bgpProcess2
            .getDefaultNeighborActivate()) {
         res = true;
         print(1, "_defaultNeighborActivate, ");
      }

      if (!Util.sameRepresentationMaps(bgpProcess.getIpPeerGroups(),
            bgpProcess2.getIpPeerGroups())) {
         res = true;
         print(1, "_ipPeerGroups, ");
      }

      if (!Util.sameRepresentationMaps(bgpProcess.getNamedPeerGroups(),
            bgpProcess2.getNamedPeerGroups())) {
         res = true;
         print(1, "_namedPeerGroups, ");
      }

      if (!Util
            .equalOrNull(bgpProcess.getNetworks(), bgpProcess2.getNetworks())) {
         res = true;
         print(1, "_networks, ");
      }

      if (bgpProcess.getPid() != bgpProcess2.getPid()) {
         res = true;
         print(1, "_pid, ");
      }

      if (compareRedistributionPolicies(bgpProcess.getRedistributionPolicies(),
            bgpProcess2.getRedistributionPolicies())) {
         res = true;
         print(1, "_redistributionPolicies, ");
      }

      if (!Util
            .equalOrNull(bgpProcess.getRouterId(), bgpProcess2.getRouterId())) {
         res = true;
         print(1, "_routerId, ");
      }

      if (!Util.equalOrNull(bgpProcess.getShutdownNeighbors(),
            bgpProcess2.getShutdownNeighbors())) {
         res = true;
         print(1, "_shutdownNeighbors, ");
      }

      if (res) {
         print(1, "CHANGED at BGPProcess.\n");
      }
      return res;
   }

   private boolean compareRedistributionPolicies(
         Map<Protocol, BgpRedistributionPolicy> redistributionPolicies,
         Map<Protocol, BgpRedistributionPolicy> redistributionPolicies2) {
      if (redistributionPolicies == null && redistributionPolicies2 == null)
         return false;
      boolean res = false;
      if (redistributionPolicies == null && redistributionPolicies2 != null) {
         for (Entry<Protocol, BgpRedistributionPolicy> e : redistributionPolicies2
               .entrySet()) {
            print(1, "ADDED  BgpRedistributionPolicy:" + e.getKey() + "\n");
         }
         return true;
      }
      if (redistributionPolicies2 == null && redistributionPolicies != null) {
         for (Entry<Protocol, BgpRedistributionPolicy> e : redistributionPolicies
               .entrySet()) {
            print(1, "REMOVED  BgpRedistributionPolicy:" + e.getKey() + "\n");
         }
         return true;
      }
      for (Entry<Protocol, BgpRedistributionPolicy> e : redistributionPolicies
            .entrySet()) {
         if (redistributionPolicies2.containsKey(e.getKey())) {
            boolean listChanged = false;
            BgpRedistributionPolicy policy = e.getValue();
            BgpRedistributionPolicy policy2 = redistributionPolicies2.get(e
                  .getKey());
            if (!Util.equalOrNull(policy.getMap(), policy2.getMap())) {
               listChanged = true;
               print(1, "_map, ");
            }
            if (!Util.equalOrNull(policy.getMetric(), policy2.getMetric())) {
               listChanged = true;
               print(1, "_metric, ");
            }
            if (listChanged) {
               res = true;
               print(1,
                     "CHANGED at BgpRedistributionPolicy with key "
                           + e.getKey() + "\n");
            }
         }
         else {
            res = true;
            print(1, "REMOVED BgpRedistributionPolicy with key " + e.getKey()
                  + "\n");
         }
      }

      Set<Protocol> tmpSet = redistributionPolicies2.keySet();
      tmpSet.removeAll(redistributionPolicies.keySet());
      for (Protocol key : tmpSet) {
         res = true;
         print(1, "ADDED BgpRedistributionPolicy with key " + key + "\n");
      }

      return res;
   }

   private boolean compareIPAsPathAccessLists(
         Map<String, IpAsPathAccessList> asPathAccessLists,
         Map<String, IpAsPathAccessList> asPathAccessLists2) {
      if (asPathAccessLists == null && asPathAccessLists2 == null)
         return false;
      boolean res = false;
      if (asPathAccessLists == null && asPathAccessLists2 != null) {
         for (Entry<String, IpAsPathAccessList> e : asPathAccessLists2
               .entrySet()) {
            print(1, "ADDED  IpAsPathAccessList:" + e.getKey() + "\n");
         }
         return true;
      }
      if (asPathAccessLists2 == null && asPathAccessLists != null) {
         for (Entry<String, IpAsPathAccessList> e : asPathAccessLists
               .entrySet()) {
            print(1, "REMOVED  IpAsPathAccessList:" + e.getKey() + "\n");
         }
         return true;
      }
      for (Entry<String, IpAsPathAccessList> e : asPathAccessLists.entrySet()) {
         if (asPathAccessLists2.containsKey(e.getKey())) {
            boolean listChanged = false;
            IpAsPathAccessList list1 = e.getValue();
            IpAsPathAccessList list2 = asPathAccessLists2.get(e.getKey());
            if (!Util.equalOrNull(list1.getName(), list2.getName())) {
               listChanged = true;
               print(1, "_name, ");
            }
            if (!Util.sameRepresentationLists(list1.getLines(),
                  list2.getLines())) {
               listChanged = true;
               print(1, "_lines, ");
            }
            if (listChanged) {
               res = true;
               print(1, "CHANGED at IpAsPathAccessList with key " + e.getKey()
                     + "\n");
            }
         }
         else {
            res = true;
            print(1, "REMOVED IpAsPathAccessList with key " + e.getKey() + "\n");
         }
      }

      Set<String> tmpSet = asPathAccessLists2.keySet();
      tmpSet.removeAll(asPathAccessLists.keySet());
      for (String key : tmpSet) {
         res = true;
         print(1, "ADDED IpAsPathAccessList with key " + key + "\n");
      }

      return res;

   }

   public Map<String, Configuration> getConfigurations(
         String serializedVendorConfigPath) {
      Map<String, VendorConfiguration> vendorConfigurations = deserializeVendorConfigurations(serializedVendorConfigPath);
      Map<String, Configuration> configurations = parseConfigurations(vendorConfigurations);
      return configurations;
   }

   public Map<String, VendorConfiguration> getVenderConfigurations(
         String testRigPath) {
      Map<File, String> configurationData = readConfigurationFiles(testRigPath);
      Map<String, VendorConfiguration> vendorConfigurations = parseVendorConfigurations(configurationData);
      return vendorConfigurations;
   }

   public void getDiff() {
      // Map<File, String> configurationData1 = readConfigurationFiles(_settings
      // .getTestRigPath());
      // Map<File, String> configurationData2 = readConfigurationFiles(_settings
      // .getSecondTestRigPath());
      //
      // List<Configuration> firstConfigurations =
      // parseConfigFiles(configurationData1);
      // if (firstConfigurations == null) {
      // quit(1);
      // }
      // List<Configuration> secondConfigurations =
      // parseConfigFiles(configurationData2);
      // if (secondConfigurations == null) {
      // quit(1);
      // }
      // if (firstConfigurations.size() != secondConfigurations.size()) {
      // System.out.println("Size MISMATCH");
      // quit(1);
      // }
      // Collections.sort(firstConfigurations);
      // Collections.sort(secondConfigurations);
      // boolean finalRes = true;
      // for (int i = 0; i < firstConfigurations.size(); i++) {
      // boolean res = (firstConfigurations.get(i).sameParseTree(
      // secondConfigurations.get(i), firstConfigurations.get(i)
      // .getName() + " MISMATCH"));
      // if (res == false) {
      // finalRes = false;
      // }
      // }
      // if (finalRes == true) {
      // System.out.println("MATCH");
      // }
   }

   private double getElapsedTime(long beforeTime) {
      long difference = System.currentTimeMillis() - beforeTime;
      double seconds = difference / 1000d;
      return seconds;
   }

   private List<String> getHelpPredicates(Map<String, String> predicateSemantics) {
      Set<String> helpPredicateSet = new LinkedHashSet<String>();
      _settings.getHelpPredicates();
      if (_settings.getHelpPredicates() == null) {
         helpPredicateSet.addAll(predicateSemantics.keySet());
      }
      else {
         helpPredicateSet.addAll(_settings.getHelpPredicates());
      }
      List<String> helpPredicates = new ArrayList<String>();
      helpPredicates.addAll(helpPredicateSet);
      Collections.sort(helpPredicates);
      return helpPredicates;
   }

   public PredicateInfo getPredicateInfo(Map<String, String> logicFiles) {
      // Get predicate semantics from rules file
      print(1, "\n*** PARSING PREDICATE SEMANTICS ***\n");
      resetTimer();
      Map<String, String> predicateSemantics = extractPredicateSemantics(logicFiles);
      if (predicateSemantics == null) {
         quit(1);
      }
      PredicateInfo predicateInfo = new PredicateInfo(predicateSemantics);
      printElapsedTime();
      return predicateInfo;
   }

   private Map<String, TreeSet<FibRow>> getRouteForwardingRules(
         Relation installedRoutes, LogicBloxFrontend lbFrontend) {
      Map<String, TreeSet<FibRow>> fibs = new HashMap<String, TreeSet<FibRow>>();
      List<String> nameList = new ArrayList<String>();
      lbFrontend.fillColumn(LBValueType.ENTITY_REF_STRING, nameList,
            installedRoutes.getColumns().get(0));
      List<String> networkList = new ArrayList<String>();
      lbFrontend.fillColumn(LBValueType.ENTITY_INDEX_NETWORK, networkList,
            installedRoutes.getColumns().get(1));
      List<String> interfaces = new ArrayList<String>();
      lbFrontend.fillColumn(LBValueType.ENTITY_REF_STRING, interfaces,
            installedRoutes.getColumns().get(2));

      String currentHostname = "";
      Map<String, Integer> startIndices = new HashMap<String, Integer>();
      Map<String, Integer> endIndices = new HashMap<String, Integer>();
      for (int i = 0; i < nameList.size(); i++) {
         String currentRowHostname = nameList.get(i);
         if (!currentHostname.equals(currentRowHostname)) {
            if (i > 0) {
               endIndices.put(currentHostname, i - 1);
            }
            currentHostname = currentRowHostname;
            startIndices.put(currentHostname, i);
         }
      }
      endIndices.put(currentHostname, nameList.size() - 1);
      for (String hostname : startIndices.keySet()) {
         TreeSet<FibRow> fibRows = new TreeSet<FibRow>();
         fibs.put(hostname, fibRows);
         int startIndex = startIndices.get(hostname);
         int endIndex = endIndices.get(hostname);
         for (int i = startIndex; i <= endIndex; i++) {
            String networkString = networkList.get(i);
            Ip networkAddress = new Ip(
                  Util.getIpFromIpSubnetPair(networkString));
            int prefixLength = Util
                  .getPrefixLengthFromIpSubnetPair(networkString);
            String iface = interfaces.get(i);
            fibRows.add(new FibRow(networkAddress, prefixLength, iface));
         }
      }
      return fibs;
   }

   private Map<String, String> getSemanticsFiles() {
      final Map<String, String> semanticsFiles = new HashMap<String, String>();
      File logicDirFile = retrieveLogicDir();
      FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
         @Override
         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
               throws IOException {
            String pathString = file.toString();
            if (pathString.endsWith(".semantics")) {
               String contents = FileUtils.readFileToString(file.toFile());
               semanticsFiles.put(pathString, contents);
            }
            return super.visitFile(file, attrs);
         }
      };

      try {
         Files.walkFileTree(Paths.get(logicDirFile.getAbsolutePath()), visitor);
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      cleanupLogicDir();
      return semanticsFiles;
   }

   public Set<Edge> getTopologyEdges(LogicBloxFrontend lbFrontend) {
      Set<Edge> edges = new HashSet<Edge>();
      String qualifiedName = _predicateInfo.getPredicateNames().get(
            TOPOLOGY_PREDICATE_NAME);
      Relation topologyRelation = lbFrontend.queryPredicate(qualifiedName);
      List<String> fromRouters = new ArrayList<String>();
      lbFrontend.fillColumn(LBValueType.ENTITY_REF_STRING, fromRouters,
            topologyRelation.getColumns().get(0));
      List<String> fromInterfaces = new ArrayList<String>();
      lbFrontend.fillColumn(LBValueType.ENTITY_REF_STRING, fromInterfaces,
            topologyRelation.getColumns().get(1));
      List<String> toRouters = new ArrayList<String>();
      lbFrontend.fillColumn(LBValueType.ENTITY_REF_STRING, toRouters,
            topologyRelation.getColumns().get(2));
      List<String> toInterfaces = new ArrayList<String>();
      lbFrontend.fillColumn(LBValueType.ENTITY_REF_STRING, toInterfaces,
            topologyRelation.getColumns().get(3));
      for (int i = 0; i < fromRouters.size(); i++) {
         if (Util.isLoopback(fromInterfaces.get(i))
               || Util.isLoopback(toInterfaces.get(i))) {
            continue;
         }
         Edge newEdge = new Edge(fromRouters.get(i), fromInterfaces.get(i),
               toRouters.get(i), toInterfaces.get(i));
         edges.add(newEdge);
      }
      return edges;
   }

   public LogicBloxFrontend initFrontend(boolean assumedToExist,
         String workspace) throws LBInitializationException {
      print(1, "\n*** STARTING CONNECTBLOX SESSION ***\n");
      resetTimer();
      LogicBloxFrontend lbFrontend = new LogicBloxFrontend(
            _settings.getConnectBloxHost(), _settings.getConnectBloxPort(),
            _settings.getSshPort(), workspace, assumedToExist);
      lbFrontend.initialize();
      if (!lbFrontend.connected()) {
         error(0,
               "Error connecting to ConnectBlox service. Please make sure service is running and try again.\n\n");
         quit(1);
      }
      print(1, "SUCCESS\n");
      printElapsedTime();
      _lbFrontends.add(lbFrontend);
      return lbFrontend;

   }

   private Map<String, Configuration> parseConfigurations(
         Map<String, VendorConfiguration> vendorConfigurations) {
      boolean processingError = false;
      Map<String, Configuration> configurations = new TreeMap<String, Configuration>();
      print(1,
            "\n*** CONVERTING VENDOR CONFIGURATIONS TO INDEPENDENT FORMAT ***\n");
      resetTimer();
      for (String name : vendorConfigurations.keySet()) {
         print(2, "Processing: \"" + name + "\"");
         VendorConfiguration vc = vendorConfigurations.get(name);
         try {
            Configuration config = vc.toVendorIndependentConfiguration();
            configurations.put(name, config);
         }
         catch (VendorConversionException e) {
            error(0, "...CONVERSION ERROR\n");
            error(0, ExceptionUtils.getStackTrace(e));
            if (_settings.exitOnParseError()) {
               return null;
            }
            else {
               processingError = true;
               continue;
            }
         }

         List<String> conversionWarnings = vc.getConversionWarnings();
         int numWarnings = conversionWarnings.size();
         if (numWarnings > 0) {
            print(2, "..." + numWarnings + " WARNING(S)\n");
            for (String warning : conversionWarnings) {
               print(2, "\tconverter: " + warning + "\n");
            }
         }
         else {
            print(2, " ...OK\n");
         }
      }
      if (processingError) {
         return null;
      }
      else {
         printElapsedTime();
         return configurations;
      }
   }

   private void parseFlowsFromConstraints(StringBuilder sw) {
      // Path nodesPath = Paths.get(_settings.getFlowPath(), NODES_FILENAME);
      // String nodesText = readFile(nodesPath.toFile());
      // String[] nodes = nodesText.split("\n");
      Path flowConstraintsDir = Paths.get(_settings.getFlowPath());
      File[] constraintsFiles = flowConstraintsDir.toFile().listFiles(
            new FilenameFilter() {
               @Override
               public boolean accept(File dir, String filename) {
                  return filename.matches(".*constraints.*.smt2.out");
               }
            });
      for (File constraintsFile : constraintsFiles) {
         String flowConstraintsText = readFile(constraintsFile);
         ANTLRStringStream s = new ANTLRStringStream(flowConstraintsText);
         ConstraintsLexer lexer = new ConstraintsLexer(s);
         CommonTokenStream tokens = new CommonTokenStream(lexer);
         ConstraintsParser parser = new ConstraintsParser(tokens);
         Map<String, Long> constraints = null;
         print(2, "Parsing: \"" + constraintsFile.getAbsolutePath() + "\"");
         try {
            constraints = parser.constraints();
         }
         catch (RecognitionException e) {
            e.printStackTrace();
            quit(1);
         }
         List<String> parserErrors = parser.getErrors();
         List<String> lexerErrors = lexer.getErrors();
         int numErrors = parserErrors.size() + lexerErrors.size();
         if (numErrors > 0) {
            error(0, " ..." + numErrors + " ERROR(S)\n");
            for (String msg : lexer.getErrors()) {
               error(2, "\tlexer: " + msg + "\n");
            }
            for (String msg : parser.getErrors()) {
               error(2, "\tparser: " + msg + "\n");
            }
            quit(1);
         }
         print(2, " ...OK\n");
         if (constraints == null) {
            continue;
         }
         long src_ip = 0;
         long dst_ip = 0;
         long src_port = 0;
         long dst_port = 0;
         long protocol = 0;
         for (String varName : constraints.keySet()) {
            Long value = constraints.get(varName);
            switch (varName) {
            case Synthesizer.SRC_IP_VAR:
               src_ip = value;
               break;

            case Synthesizer.DST_IP_VAR:
               dst_ip = value;
               break;

            case Synthesizer.SRC_PORT_VAR:
               src_port = value;
               break;

            case Synthesizer.DST_PORT_VAR:
               dst_port = value;
               break;

            case Synthesizer.IP_PROTOCOL_VAR:
               protocol = value;
               break;

            default:
               throw new Error("invalid variable name");
            }
         }
         String node = constraintsFile.getName().replaceFirst(
               ".*-([^-]*).smt2.out", "$1");
         String line = node + "|" + src_ip + "|" + dst_ip + "|" + src_port
               + "|" + dst_port + "|" + protocol + "\n";
         sw.append(line);
      }
   }

   private void parseTopology(String testRigPath, String topologyFileText,
         Map<String, StringBuilder> factBins) {
      TopologyParser parser = null;
      TopologyLexer lexer = null;
      Topology topology = null;
      ANTLRStringStream in = new ANTLRStringStream(topologyFileText);
      CommonTokenStream tokens;
      File topologyPath = new File(testRigPath + SEPARATOR + "topology.net");
      print(2, "Parsing: \"" + topologyPath.getAbsolutePath() + "\"");
      if (topologyFileText.startsWith("autostart")) {
         lexer = new GNS3TopologyLexer(in);
         tokens = new CommonTokenStream(lexer);
         parser = new GNS3TopologyParser(tokens);
      }
      else if (topologyFileText.startsWith("CONFIGPARSER_TOPOLOGY")) {
         lexer = new BatfishTopologyLexer(in);
         tokens = new CommonTokenStream(lexer);
         parser = new BatfishTopologyParser(tokens);
      }
      else if (topologyFileText.equals("")) {
         error(1, "...WARNING: empty topology\n");
         return;
      }
      else {
         error(0, "...ERROR\n");
         throw new Error("Topology format error");
      }
      try {
         topology = parser.topology();
      }
      catch (Exception e) {
         error(0, " ...ERROR\n");
         e.printStackTrace();
      }
      List<String> parserErrors = parser.getErrors();
      List<String> lexerErrors = lexer.getErrors();
      int numErrors = parserErrors.size() + lexerErrors.size();
      if (numErrors > 0) {
         error(0, " ..." + numErrors + " ERROR(S)\n");
         for (String msg : lexer.getErrors()) {
            error(2, "\tlexer: " + msg + "\n");
         }
         for (String msg : parser.getErrors()) {
            error(2, "\tparser: " + msg + "\n");
         }
         quit(1);
      }
      TopologyFactExtractor tfe = new TopologyFactExtractor(topology);
      tfe.writeFacts(factBins);
      print(2, " ...OK\n");
   }

   private Map<String, VendorConfiguration> parseVendorConfigurations(
         Map<File, String> configurationData) {
      print(1, "\n*** PARSING VENDOR CONFIGURATION FILES ***\n");
      resetTimer();
      Map<String, VendorConfiguration> vendorConfigurations = new TreeMap<String, VendorConfiguration>();

      boolean processingError = false;
      for (File currentFile : configurationData.keySet()) {
         String fileText = configurationData.get(currentFile);
         String currentPath = currentFile.getAbsolutePath();
         ConfigurationParser parser = null;
         ConfigurationLexer lexer = null;
         VendorConfiguration vc = null;
         ANTLRStringStream in = new ANTLRStringStream(fileText);
         CommonTokenStream tokens;
         if (fileText.length() == 0) {
            continue;
         }
         CiscoControlPlaneExtractor extractor = null;
         boolean antlr4 = false;
         if (fileText.charAt(0) == '!') {
            // antlr 4 stuff
            print(1, "Parsing: \"" + currentPath + "\"");
            antlr4 = true;
            BatfishCombinedParser combinedParser = new CiscoCombinedParser(
                  fileText);
            ParserRuleContext tree = combinedParser.parse();
            List<String> errors = combinedParser.getErrors();
            int numErrors = errors.size();
            if (numErrors > 0) {
               error(1, " ..." + numErrors + " ERROR(S)\n");
               for (int i = 0; i < numErrors; i++) {
                  String prefix = "ERROR " + (i + 1) + ": ";
                  String msg = errors.get(i);
                  String prefixedMsg = Util.applyPrefix(prefix, msg);
                  error(1, prefixedMsg + "\n");
               }
               if (_settings.exitOnParseError()) {
                  return null;
               }
               else {
                  processingError = true;
                  continue;
               }
            }
            else if (!_settings.printParseTree()) {
               print(1, "...OK\n");
            }
            else {
               print(0, "...OK, PRINTING PARSE TREE:\n");
               print(0,
                     ParseTreePrettyPrinter.print(tree,
                           combinedParser.getParser())
                           + "\n\n");
            }
            extractor = new CiscoControlPlaneExtractor(fileText,
                  combinedParser.getParser());
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(extractor, tree);
            for (String warning : extractor.getWarnings()) {
               error(2, warning);
            }
            vc = extractor.getVendorConfiguration();
            assert Boolean.TRUE;
         }
         else if ((fileText.indexOf("set version") >= 0)
               && ((fileText.indexOf("set version") == 0) || (fileText
                     .charAt(fileText.indexOf("set version") - 1) == '\n'))) {
            lexer = new FlatJuniperGrammarLexer(in);
            tokens = new CommonTokenStream(lexer);
            parser = new FlatJuniperGrammarParser(tokens);
         }
         else if (fileText.charAt(0) == '#') {
            lexer = new JuniperGrammarLexer(in);
            tokens = new CommonTokenStream(lexer);
            parser = new JuniperGrammarParser(tokens);
         }
         else {
            continue;
         }
         if (!antlr4) {
            print(2, "Parsing: \"" + currentPath + "\"");
            try {
               vc = parser.parse_configuration();
            }
            catch (Exception e) {
               error(0, " ...ERROR\n");
               e.printStackTrace();
            }
            List<String> parserErrors = parser.getErrors();
            List<String> lexerErrors = lexer.getErrors();
            int numErrors = parserErrors.size() + lexerErrors.size();
            if (numErrors > 0) {
               error(0, " ..." + numErrors + " ERROR(S)\n");
               for (String msg : lexer.getErrors()) {
                  error(2, "\tlexer: " + msg + "\n");
               }
               for (String msg : parser.getErrors()) {
                  error(2, "\tparser: " + msg + "\n");
               }
               if (_settings.exitOnParseError()) {
                  return null;
               }
               else {
                  processingError = true;
                  continue;
               }
            }
            else {
               print(2, "...OK\n");
            }
         }

         // at this point we should have a VendorConfiguration vc
         vendorConfigurations.put(vc.getHostname(), vc);
      }
      if (processingError) {
         return null;
      }
      else {
         printElapsedTime();
         return vendorConfigurations;
      }
   }

   private void populateConfigurationFactBins(
         Collection<Configuration> configurations,
         Map<String, StringBuilder> factBins) {
      print(1, "\n*** EXTRACTING LOGICBLOX FACTS FROM CONFIGURATIONS ***\n");
      resetTimer();
      Set<Long> communities = new LinkedHashSet<Long>();
      for (Configuration c : configurations) {
         communities.addAll(c.getCommunities());
      }
      for (Configuration c : configurations) {
         ConfigurationFactExtractor cfe = new ConfigurationFactExtractor(c,
               communities, factBins);
         cfe.writeFacts();
      }
      printElapsedTime();
   }

   private void postFacts(LogicBloxFrontend lbFrontend,
         Map<String, StringBuilder> factBins) {
      print(1, "\n*** POSTING FACTS TO BLOXWEB SERVICES ***\n");
      resetTimer();
      String ret = lbFrontend.startBloxWebServices();
      if (ret != null) {
         error(0, ret + "\n");
         quit(1);
      }
      try {
         lbFrontend.postFacts(factBins);
      }
      catch (ServiceClientException e) {
         e.printStackTrace();
         quit(1);
      }
      ret = lbFrontend.stopBloxWebServices();
      if (ret != null) {
         error(0, ret + "\n");
         quit(1);
      }
      print(1, "SUCCESS\n");
      printElapsedTime();
   }

   public void print(int logLevel, String text) {
      if (_settings.getLogLevel() >= logLevel) {
         System.out.print(text);
         System.out.flush();
      }
   }

   private void printAllPredicateSemantics(
         Map<String, String> predicateSemantics) {
      // Get predicate semantics from rules file
      print(1, "\n*** PRINTING PREDICATE SEMANTICS ***\n");
      List<String> helpPredicates = getHelpPredicates(predicateSemantics);
      for (String predicate : helpPredicates) {
         printPredicateSemantics(predicate);
         print(0, "\n");
      }
   }

   private void printElapsedTime() {
      double seconds = getElapsedTime(_timerCount);
      print(1, "Time taken for this task: " + seconds + " seconds\n");
   }

   private void printPredicate(LogicBloxFrontend lbFrontend,
         String predicateName) {
      List<String> output;
      printPredicateSemantics(predicateName);
      String qualifiedName = _predicateInfo.getPredicateNames().get(
            predicateName);
      if (qualifiedName == null) { // predicate not found
         error(0, "ERROR: No information for predicate: " + predicateName
               + "\n");
         return;
      }
      Relation relation = lbFrontend.queryPredicate(qualifiedName);
      try {
         output = lbFrontend.getPredicate(_predicateInfo, relation,
               predicateName);
         for (String match : output) {
            print(0, match);
         }
      }
      catch (QueryException q) {
         error(0, q.getMessage() + "\n");
      }
   }

   private void printPredicateCount(LogicBloxFrontend lbFrontend,
         String predicateName) {
      int numRows = lbFrontend.queryPredicate(predicateName).getColumns()
            .get(0).size();
      String output = "|" + predicateName + "| = " + numRows + "\n";
      print(0, output);
   }

   public void printPredicateCounts(LogicBloxFrontend lbFrontend,
         Set<String> predicateNames) {
      // Print predicate(s) here
      print(0, "\n*** SUBMITTING QUERY(IES) ***\n");
      resetTimer();
      for (String predicateName : predicateNames) {
         printPredicateCount(lbFrontend, predicateName);
         // print(0, "\n");
      }
      printElapsedTime();
   }

   public void printPredicates(LogicBloxFrontend lbFrontend,
         Set<String> predicateNames) {
      // Print predicate(s) here
      print(0, "\n*** SUBMITTING QUERY(IES) ***\n");
      resetTimer();
      for (String predicateName : predicateNames) {
         printPredicate(lbFrontend, predicateName);
         print(0, "\n");
      }
      printElapsedTime();
   }

   private void printPredicateSemantics(String predicateName) {
      String semantics = _predicateInfo.getPredicateSemantics(predicateName);
      if (semantics == null) {
         semantics = "<missing>";
      }
      print(0, "Predicate: " + predicateName + "\n");
      print(0, "Semantics: " + semantics + "\n");
   }

   public void quit(int exitCode) {
      for (LogicBloxFrontend lbFrontend : _lbFrontends) {
         // Close backend threads
         if (lbFrontend != null && lbFrontend.connected()) {
            lbFrontend.close();
         }
      }
      System.exit(exitCode);
   }

   private Map<File, String> readConfigurationFiles(String testRigPath) {
      print(1, "\n*** READING CONFIGURATION FILES ***\n");
      resetTimer();
      Map<File, String> configurationData = new TreeMap<File, String>();
      File configsPath = Paths.get(testRigPath, "configs").toFile();
      File[] configFilePaths = configsPath.listFiles(new FilenameFilter() {
         @Override
         public boolean accept(File dir, String name) {
            return !name.startsWith(".");
         }
      });
      for (File file : configFilePaths) {
         print(2, "Reading: \"" + file.toString() + "\"\n");
         String fileText = readFile(file.getAbsoluteFile());
         configurationData.put(file, fileText);
      }
      printElapsedTime();
      return configurationData;
   }

   public String readFile(File file) {
      String text = null;
      try {
         text = FileUtils.readFileToString(file);
      }
      catch (IOException e) {
         e.printStackTrace();
         quit(1);
      }
      return text;
   }

   private void resetTimer() {
      _timerCount = System.currentTimeMillis();
   }

   private File retrieveLogicDir() {
      File logicDirFile = null;
      final String locatorFilename = LogicResourceLocator.class.getSimpleName()
            + ".class";
      URL logicSourceURL = LogicResourceLocator.class.getProtectionDomain()
            .getCodeSource().getLocation();
      String logicSourceString = logicSourceURL.toString();
      UrlZipExplorer zip = null;
      StringFilter lbFilter = new StringFilter() {
         @Override
         public boolean accept(String filename) {
            return filename.endsWith(".lbb") || filename.endsWith(".lbp")
                  || filename.endsWith(".semantics")
                  || filename.endsWith(locatorFilename);
         }
      };
      if (logicSourceString.startsWith("onejar:")) {
         FileVisitor<Path> visitor = null;
         try {
            zip = new UrlZipExplorer(logicSourceURL);
            Path destinationDir = Files.createTempDirectory("lbtmpproject");
            File destinationDirAsFile = destinationDir.toFile();
            zip.extractFiles(lbFilter, destinationDirAsFile);
            visitor = new SimpleFileVisitor<Path>() {
               private String _projectDirectory;

               @Override
               public String toString() {
                  return _projectDirectory;
               }

               @Override
               public FileVisitResult visitFile(Path aFile,
                     BasicFileAttributes aAttrs) throws IOException {
                  if (aFile.endsWith(locatorFilename)) {
                     _projectDirectory = aFile.getParent().toString();
                     return FileVisitResult.TERMINATE;
                  }
                  return FileVisitResult.CONTINUE;
               }
            };
            Files.walkFileTree(destinationDir, visitor);
            _tmpLogicDir = destinationDirAsFile;
         }
         catch (IOException e) {
            e.printStackTrace();
            quit(1);
         }
         String fileString = visitor.toString();
         return new File(fileString);
      }
      else {
         String logicPackageResourceName = LogicResourceLocator.class
               .getPackage().getName().replace('.', SEPARATOR.charAt(0));
         try {
            logicDirFile = new File(LogicResourceLocator.class.getClassLoader()
                  .getResource(logicPackageResourceName).toURI());
         }
         catch (URISyntaxException e) {
            e.printStackTrace();
            quit(1);
         }
         return logicDirFile;
      }
   }

   private void revert(LogicBloxFrontend lbFrontend) {
      print(1, "\n*** REVERTING WORKSPACE ***\n");
      String workspaceName = new File(_settings.getTestRigPath()).getName();
      String branchName = _settings.getBranchName();
      print(2, "Reverting workspace: \"" + workspaceName + "\" to branch: \""
            + branchName + "\n");
      String errorResult = lbFrontend.revertDatabase(branchName);
      if (errorResult != null) {
         error(0, errorResult + "\n");
         quit(1);
      }
   }

   public void run() {
      if (_settings.redirectStdErr()) {
         System.setErr(System.out);
      }

      if (_settings.getZ3()) {
         Map<String, Configuration> configurations = deserializeConfigurations(_settings
               .getSerializeIndependentPath());
         genZ3(configurations);
         quit(0);
      }

      if (_settings.getAnonymize()) {
         anonymizeConfigurations();
         quit(0);
      }

      if (_settings.getSerializeVendor()) {
         String testRigPath = _settings.getTestRigPath();
         String outputPath = _settings.getSerializeVendorPath();
         serializeVendorConfigs(testRigPath, outputPath);
         quit(0);
      }

      if (_settings.getSerializeIndependent()) {
         String inputPath = _settings.getSerializeVendorPath();
         String outputPath = _settings.getSerializeIndependentPath();
         serializeIndependentConfigs(inputPath, outputPath);
         quit(0);
      }

      if (_settings.getDiff()) {
         getDiff();
         quit(0);
      }

      if (_settings.getDr()) {
         DeptGenerator gen = new DeptGenerator(this, _settings, SEPARATOR);
         gen.generateDeptRouters();
         gen.createSubgroupTestRigs();
         quit(0);
      }

      if (_settings.getCompareCommits()) {
         getCompare();
         quit(0);
      }

      if (_settings.getConcretize()) {
         concretize();
         quit(0);
      }

      if (_settings.getQuery() || _settings.getPrintSemantics()
            || _settings.getDataPlane()) {
         Map<String, String> logicFiles = getSemanticsFiles();
         _predicateInfo = getPredicateInfo(logicFiles);
         // Print predicate semantics and quit if requested
         if (_settings.getPrintSemantics()) {
            printAllPredicateSemantics(_predicateInfo.getPredicateSemantics());
            quit(0);
         }
      }

      Map<String, StringBuilder> cpFactBins = null;
      if (_settings.getFacts() || _settings.getDumpControlPlaneFacts()) {
         cpFactBins = new LinkedHashMap<String, StringBuilder>();
         initControlPlaneFactBins(cpFactBins);
         writeTopologyFacts(_settings.getTestRigPath(), cpFactBins);
         writeConfigurationFacts(_settings.getSerializeIndependentPath(),
               cpFactBins);
         if (_settings.getDumpControlPlaneFacts()) {
            dumpFacts(cpFactBins);
         }
         if (!(_settings.getFacts() || _settings.createWorkspace())) {
            quit(0);
         }
      }

      // Start frontend
      LogicBloxFrontend lbFrontend = null;
      if (_settings.createWorkspace() || _settings.getFacts()
            || _settings.getQuery() || _settings.getDataPlane()
            || _settings.getFlows() || _settings.revert()) {
         lbFrontend = connect();
      }

      if (_settings.revert()) {
         revert(lbFrontend);
         quit(0);
      }

      // Create new workspace (will overwrite existing) if requested
      if (_settings.createWorkspace()) {
         addProject(lbFrontend);
         if (!_settings.getFacts()) {
            quit(0);
         }
      }

      // Post facts if requested
      if (_settings.getFacts()) {
         addStaticFacts(lbFrontend,
               Collections.singletonList(BASIC_FACTS_BLOCKNAME));
         postFacts(lbFrontend, cpFactBins);
         quit(0);
      }

      if (_settings.getQuery()) {
         lbFrontend.initEntityTable();
         Map<String, String> allPredicateNames = _predicateInfo
               .getPredicateNames();
         Set<String> predicateNames = new TreeSet<String>();
         if (_settings.getQueryAll()) {
            predicateNames.addAll(allPredicateNames.keySet());
         }
         else {
            predicateNames.addAll(_settings.getPredicates());
         }
         if (_settings.getCountsOnly()) {
            printPredicateCounts(lbFrontend, predicateNames);
         }
         else {
            printPredicates(lbFrontend, predicateNames);
         }
         quit(0);
      }

      if (_settings.getDataPlane()) {
         computeDataPlane(lbFrontend);
         quit(0);
      }

      Map<String, StringBuilder> trafficFactBins = null;
      if (_settings.getFlows() || _settings.getDumpTrafficFacts()) {
         trafficFactBins = new LinkedHashMap<String, StringBuilder>();
         initTrafficFactBins(trafficFactBins);
         writeTrafficFacts(trafficFactBins);
         if (_settings.getDumpTrafficFacts()) {
            dumpFacts(trafficFactBins);
         }
         if (_settings.getFlows()) {
            postFacts(lbFrontend, trafficFactBins);
            quit(0);
         }
      }

      error(0, "No task performed! Run with -help flag to see usage\n");
      quit(1);
   }

   private void serializeIndependentConfigs(String vendorConfigPath,
         String outputPath) {
      Map<String, Configuration> configurations = getConfigurations(vendorConfigPath);
      print(1,
            "\n*** SERIALIZING VENDOR-INDEPENDENT CONFIGURATION STRUCTURES ***\n");
      resetTimer();
      for (String name : configurations.keySet()) {
         Configuration c = configurations.get(name);
         Path currentOutputPath = Paths.get(outputPath, name);
         print(2,
               "Serializing: \"" + name + "\" ==> \""
                     + currentOutputPath.toString() + "\"");
         serializeObject(c, currentOutputPath.toFile());
         print(2, " ...OK\n");
      }
      printElapsedTime();
   }

   private void serializeObject(Object object, File outputFile) {
      XStream xstream = new XStream(new DomDriver("UTF-8"));
      FileOutputStream fos;
      try {
         fos = new FileOutputStream(outputFile);
         ObjectOutputStream oos = xstream.createObjectOutputStream(fos);
         oos.writeObject(object);
         oos.close();
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
         quit(1);
      }
      catch (IOException e) {
         e.printStackTrace();
         quit(1);
      }
   }

   private void serializeVendorConfigs(String testRigPath, String outputPath) {
      Map<File, String> configurationData = readConfigurationFiles(testRigPath);
      Map<String, VendorConfiguration> vendorConfigurations = parseVendorConfigurations(configurationData);
      if (vendorConfigurations == null) {
         error(0, "Exiting due to parser errors\n");
         quit(1);
      }
      print(1, "\n*** SERIALIZING VENDOR CONFIGURATION STRUCTURES ***\n");
      resetTimer();
      new File(outputPath).mkdirs();
      for (String name : vendorConfigurations.keySet()) {
         VendorConfiguration vc = vendorConfigurations.get(name);
         Path currentOutputPath = Paths.get(outputPath, name);
         print(2,
               "Serializing: \"" + name + "\" ==> \""
                     + currentOutputPath.toString() + "\"");
         serializeObject(vc, currentOutputPath.toFile());
         print(2, " ...OK\n");
      }
      printElapsedTime();
   }

   public void writeConfigurationFacts(String serializedConfigPath,
         Map<String, StringBuilder> factBins) {
      Map<String, Configuration> configurations = deserializeConfigurations(serializedConfigPath);
      populateConfigurationFactBins(configurations.values(), factBins);
   }

   public void writeTopologyFacts(String testRigPath,
         Map<String, StringBuilder> factBins) {
      Path topologyFilePath = Paths.get(testRigPath, TOPOLOGY_FILENAME);
      // Get generated facts from topology file
      String topologyFileText = null;
      boolean guess = false;
      print(1, "*** PARSING TOPOLOGY ***\n");
      resetTimer();
      try {
         topologyFileText = FileUtils.readFileToString(topologyFilePath
               .toFile());
      }
      catch (FileNotFoundException e) {
         // tell logicblox to guess adjacencies based on interface subnetworks
         print(1, "*** (GUESSING TOPOLOGY IN ABSENCE OF EXPLICIT FILE) ***\n");
         StringBuilder wGuessTopology = factBins.get("GuessTopology");
         wGuessTopology.append("1\n");
         guess = true;
      }
      catch (IOException e) {
         e.printStackTrace();
         quit(1);
      }
      if (!guess) {
         parseTopology(testRigPath, topologyFileText, factBins);
      }
      printElapsedTime();
      /*
       * // flow sinks Path flowSinkPath = Paths.get(_settings.getTestRigPath(),
       * FLOW_SINK_FILENAME);
       * 
       * StringBuilder wSetFlowSinkInterface = factBins
       * .get("SetFlowSinkInterface"); if (Files.exists(flowSinkPath)) { try {
       * String flowSinkInterface = FileUtils.readFileToString(flowSinkPath
       * .toFile()); wSetFlowSinkInterface.append(flowSinkInterface); } catch
       * (IOException e) { e.printStackTrace(); quit(1); } }
       */
   }

   private void writeTrafficFacts(Map<String, StringBuilder> factBins) {
      StringBuilder wSetFlowOriginate = factBins.get("SetFlowOriginate");
      parseFlowsFromConstraints(wSetFlowOriginate);
   }
}
