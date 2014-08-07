package batfish.ucla;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;

import batfish.grammar.BatfishLexer;
import batfish.grammar.BatfishParser;

import batfish.grammar.cisco.CiscoGrammar;
import batfish.grammar.cisco.CiscoGrammar.Cisco_configurationContext;
import batfish.grammar.cisco.CiscoGrammarCommonLexer;
import batfish.grammar.cisco.controlplane.CiscoControlPlaneExtractor;
import batfish.main.Batfish;
import batfish.main.Settings;

import batfish.representation.Ip;

import batfish.representation.cisco.BgpPeerGroup;
import batfish.representation.cisco.BgpProcess;

import batfish.representation.cisco.CiscoVendorConfiguration;
import batfish.representation.cisco.Interface;
import batfish.representation.cisco.NamedBgpPeerGroup;
import batfish.representation.cisco.PrefixList;
import batfish.util.Util;

public class DeptGenerator {
   public static final String FAKE_INTERFACE_PREFIX = "TenGigabitEthernet200/";
   public static final String FLOW_SINK_INTERFACE_PREFIX = "TenGigabitEthernet100/";
   private Batfish _batfish;
   private Map<String, DeptRouter> _deptRouters;
   private String _separator;
   private Settings _settings;

   private List<Set<DeptRouter>> _subgroups;

   public DeptGenerator(Batfish batfish, Settings settings, String separator) {
      _batfish = batfish;
      _settings = settings;
      _separator = separator;
      _deptRouters = new HashMap<String, DeptRouter>();
      _subgroups = new ArrayList<Set<DeptRouter>>();
   }

   public void createSubgroupTestRigs() {
      for (int i = 0; i < _subgroups.size(); i++) {
         Set<DeptRouter> subgroup = _subgroups.get(i);
         Path testRigPath = Paths.get(_settings.getTestRigPath());
         String testRigName = testRigPath.getFileName().toString();
         String subgroupTestRigName = testRigName + "-subgroup-"
               + String.format("%02d", i);
         Path subgroupTestRigPath = Paths.get(_settings.getTestRigPath(),
               subgroupTestRigName);
         Path subgroupConfigPath = Paths.get(subgroupTestRigPath.toString(),
               "configs");
         try {
            // FileUtils.copyDirectory(configPath.toFile(),
            // subgroupConfigPath.toFile());
            Files.createDirectories(subgroupConfigPath);
            writeDeptRouters(subgroup, subgroupConfigPath.toString());
            writeFlowFile(subgroup, subgroupTestRigPath.toString());
         }
         catch (IOException e) {
            e.printStackTrace();
            quit(1);
         }
      }

   }

   private void error(int logLevel, String text) {
      _batfish.error(logLevel, text);
   }

   public void generateDeptRouters() {
      List<CiscoVendorConfiguration> configs = parseDistributionRouters(_settings
            .getTestRigPath());
      if (configs == null) {
         error(0, "quitting due to parse error\n");
         quit(1);
      }
      for (CiscoVendorConfiguration config : configs) {
         BgpProcess proc = config.getBgpProcess();
         NamedBgpPeerGroup department = proc.getNamedPeerGroups().get("department");
         if (department == null) {
            continue;
         }
         for (Ip neighborName : department.getNeighborAddresses()) {
            if (!proc.getActivatedNeighbors().contains(neighborName)) {
               continue;
            }
            BgpPeerGroup pg = proc.getPeerGroup(neighborName.toString()
                  );
            int remoteAs = pg.getRemoteAS();
            String deptName = "dpt_" + remoteAs;
            DeptRouter deptRouter = _deptRouters.get(deptName);
            if (deptRouter == null) {
               deptRouter = new DeptRouter(deptName, remoteAs);
               _deptRouters.put(deptName, deptRouter);
            }
            DistDeptPeering peering = new DistDeptPeering();
            deptRouter.getPeerings().add(peering);
            long deptIntIpLong = neighborName.asLong();
            String distrIp = null;
            String distrSubnet = null;
            for (Interface i : config.getInterfaces().values()) {
               if (i.getIP() == null) {
                  continue;
               }
               long intIpLong = i.getIP().asLong();
               long intSubLong = i.getSubnetMask().asLong();
               int intSubBits = Util.numSubnetBits(i.getSubnetMask().toString());
               long network_start = intIpLong & intSubLong;
               long network_end = Util.getNetworkEnd(network_start, intSubBits);
               if (deptIntIpLong >= network_start
                     && deptIntIpLong <= network_end) {
                  // we found the distribution interface
                  distrIp = i.getIP().toString();
                  distrSubnet = i.getSubnetMask().toString();
                  break;
               }
            }
            if (distrIp == null) {
               throw new Error("could not find interface for peering");
            }
            peering.setDistIp(distrIp);
            peering.setDistName(config.getHostname());
            peering.setIp(neighborName.toString());
            peering.setSubnet(distrSubnet);
            String distrPrefixListName = pg.getInboundPrefixList();
            if (distrPrefixListName != null) {
               PrefixList distrPrefixList = config.getPrefixLists().get(
                     distrPrefixListName);
               if (distrPrefixList != null) {
                  peering.setPrefixList(distrPrefixList);
               }
            }
         }
      }

      // compute department networks and flow ip equivalence classes
      for (DeptRouter router : _deptRouters.values()) {
         router.computeDeptNetworks();
      }

      // compute subgroups and subgroup networks
      int maxSubgroupSize = _settings.getMaxSubgroupSize();
      Set<DeptRouter> currentSubgroup = new TreeSet<DeptRouter>();
      _subgroups.add(currentSubgroup);
      Map<Ip, Ip> subgroupNetworks = new TreeMap<Ip, Ip>();
      for (DeptRouter router : _deptRouters.values()) {
         if (currentSubgroup.size() == maxSubgroupSize) {
            currentSubgroup = new TreeSet<DeptRouter>();
            _subgroups.add(currentSubgroup);
            subgroupNetworks = new TreeMap<Ip, Ip>();
         }
         currentSubgroup.add(router);
         subgroupNetworks.putAll(router.getDeptNetworks());
         router.setSubgroupNetworks(subgroupNetworks);
      }

   }

   private List<CiscoVendorConfiguration> parseDistributionRouters(
         String testRigPath) {
      List<CiscoVendorConfiguration> configs = new ArrayList<CiscoVendorConfiguration>();
      
      Map<File, String> configurationData = new TreeMap<File, String>();
      File configsPath = new File(testRigPath + _separator + "configs");
      File[] configFilePaths = configsPath.listFiles(new FilenameFilter() {
         public boolean accept(File dir, String name) {
            return name.startsWith("dr");
         }
      });
      for (File file : configFilePaths) {
         print(2, "Reading: \"" + file.toString() + "\"\n");
         String fileText = readFile(file.getAbsoluteFile());
         configurationData.put(file, fileText);
      }
      // Get generated facts from configuration files      


      boolean processingError = false;
      for (File currentFile : configurationData.keySet()) {
         String fileText = configurationData.get(currentFile);
         String currentPath = currentFile.getAbsolutePath();
         if (fileText.length() == 0) {
            continue;
         }
         BatfishParser bParser = null;
         BatfishLexer bLexer = null;
         CiscoVendorConfiguration vc = null;

         print(2, "Parsing: \"" + currentPath + "\"" + "\n");
         org.antlr.v4.runtime.CharStream stream = new org.antlr.v4.runtime.ANTLRInputStream(
               fileText);
         CiscoGrammarCommonLexer lexer4 = new CiscoGrammarCommonLexer(stream);
         bLexer = lexer4;
         org.antlr.v4.runtime.CommonTokenStream tokens4 = new org.antlr.v4.runtime.CommonTokenStream(
               lexer4);
         CiscoGrammar parser4 = new CiscoGrammar(tokens4);
         bParser = parser4;
         parser4.getInterpreter().setPredictionMode(PredictionMode.SLL);
         Cisco_configurationContext tree = parser4.cisco_configuration();
         List<String> parserErrors = bParser.getErrors();
         List<String> lexerErrors = bLexer.getErrors();
         int numErrors = parserErrors.size() + lexerErrors.size();
         if (numErrors > 0) {
            error(0, " ..." + numErrors + " ERROR(S)\n");
            for (String msg : lexerErrors) {
               error(2, "\tlexer: " + msg + "\n");
            }
            for (String msg : parserErrors) {
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
         ParseTreeWalker walker = new ParseTreeWalker();
         CiscoControlPlaneExtractor extractor = new CiscoControlPlaneExtractor(fileText);
         walker.walk(extractor, tree);
         
         vc = (CiscoVendorConfiguration) extractor.getVendorConfiguration();
         configs.add(vc);
      }
      if (processingError) {
         return null;
      }
      else {
         return configs;
      }
   }

   private void print(int logLevel, String text) {
      _batfish.print(logLevel, text);
   }

   private void quit(int code) {
      _batfish.quit(code);
   }

   private String readFile(File file) {
      return _batfish.readFile(file);
   }

   private void writeDeptRouters(Set<DeptRouter> routers, String directory)
         throws IOException {
      for (DeptRouter router : routers) {
         String routerName = router.getName();
         String fileName = routerName + ".conf";
         Path filePath = Paths.get(directory, fileName);
         String routerConfig = router.toConfigString();
         print(2, "Writing: \"" + filePath.toAbsolutePath().toString() + "\"\n");
         FileUtils.writeStringToFile(filePath.toFile(), routerConfig);
      }
   }

   private void writeFlowFile(Set<DeptRouter> routers, String testRigPath)
         throws IOException {
      StringBuilder flowSinks = new StringBuilder();
      flowSinks.append("dc_stub" + "|" + FLOW_SINK_INTERFACE_PREFIX + "0\n");
      flowSinks.append("hpr_stub" + "|" + FLOW_SINK_INTERFACE_PREFIX + "0\n");
      for (DeptRouter router : routers) {
         String routerName = router.getName();
         for (int i = 0; i < router.getNumFlowSinkInterfaces(); i++) {
            String flowSinkInterface = FLOW_SINK_INTERFACE_PREFIX + i;
            flowSinks.append(routerName + "|" + flowSinkInterface + "\n");
         }
      }

      // Remove trailing newline
      int flen = flowSinks.length();
      flowSinks.delete(flen - 1, flen);

      String fileName = "flow_sinks";
      Path filePath = Paths.get(testRigPath, fileName);

      print(2, "Writing: \"" + filePath.toAbsolutePath().toString() + "\"\n");
      FileUtils.writeStringToFile(filePath.toFile(), flowSinks.toString());
   }

}
