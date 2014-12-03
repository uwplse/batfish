package batfish.grammar.cisco.controlplane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import batfish.grammar.cisco.*;
import batfish.grammar.cisco.CiscoGrammar.Arp_access_list_stanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Arp_al_substanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Mac_access_list_stanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Mac_access_list_substanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Neighbor_filter_list_rb_stanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Neighbor_nexus_af_stanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Neighbor_nexus_af_stanza_tailContext;
import batfish.grammar.cisco.CiscoGrammar.Neighbor_nexus_remote_as_stanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Neighbor_nexus_update_source_stanzaContext;
import batfish.grammar.cisco.CiscoGrammar.Nexus_access_list_statisticsContext;
import batfish.grammar.cisco.CiscoGrammar.Template_peer_inheritContext;

enum stanza_type{IFACE, ACL, ROUTEMAP, ROUTER};
class stanza{
	public stanza_type type;
	public String name;
	List<reference_to> references;
	public stanza(stanza_type t){
		type = t;
		name = null;
		references = new ArrayList<reference_to>();
	}
	public stanza(stanza_type t, String n){
		type = t;
		name = n;
	}
	public void AddReference(stanza_type type, String name){
		references.add(new reference_to(type, name));
	}
	@Override
	public int hashCode(){
		return type.hashCode()+name.hashCode();
	}
	@Override
	public boolean equals(Object obj){
		if( obj  instanceof stanza){
			stanza stanza_obj = (stanza) obj;
			return stanza_obj.type == this.type && stanza_obj.name.equals(this.name);
		}
		return false;
	}
	@Override
	public String toString(){
		return "stanza:"+name+"("+type.name()+")";
	}
}
class reference_to{
	public stanza_type type;
	public String name;
	public reference_to(stanza_type t, String n){
		type = t;
		name = n;
	}
}
public class CiscoControlPlaneComplexity implements CiscoGrammarListener{
	Set<stanza> stanzas = new HashSet<stanza>();
	stanza current = null;
	
	public Integer getComplexit(){
		int totalReferences=0;
		for(stanza s: stanzas){
			List<reference_to> references = s.references;
			for(reference_to to: references){
				stanza dst = new stanza(to.type, to.name);
				if(!stanzas.contains(dst)){
					System.out.println(s+" references to a non-existing stanza: "+dst);
				}
				else{
					totalReferences++;
				}
			}
		}
		
		return totalReferences;
	}
	
	private void enterStanza(stanza_type t){
		if(current !=null){
			System.out.println("enter a new stanza without exiting the previous, please check. "+current);
		}
		current = new stanza(t);
	}
	private void exitStanza(String name){
		if(current == null){
			System.out.println("exit a null stanza, please check.");
		}
		current.name = name;
		if(stanzas.contains(current)){
			System.out.println("duplicated stanzas, please check: "+current);
		}
		else{
			stanzas.add(current);
		}
		current = null;
	}
	private void AddReference(stanza_type type, String name) {
		current.AddReference(type, name);		
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_ospf_dead_interval_if_stanza(@NotNull CiscoGrammar.Ip_ospf_dead_interval_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_ospf_dead_interval_if_stanza(@NotNull CiscoGrammar.Ip_ospf_dead_interval_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRouter_id_ro_stanza(@NotNull CiscoGrammar.Router_id_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRouter_id_ro_stanza(@NotNull CiscoGrammar.Router_id_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPassive_interface_default_ro_stanza(@NotNull CiscoGrammar.Passive_interface_default_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPassive_interface_default_ro_stanza(@NotNull CiscoGrammar.Passive_interface_default_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMaximum_paths_ro_stanza(@NotNull CiscoGrammar.Maximum_paths_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMaximum_paths_ro_stanza(@NotNull CiscoGrammar.Maximum_paths_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLog_adjacency_changes_ipv6_ro_stanza(@NotNull CiscoGrammar.Log_adjacency_changes_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLog_adjacency_changes_ipv6_ro_stanza(@NotNull CiscoGrammar.Log_adjacency_changes_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDefault_information_ipv6_ro_stanza(@NotNull CiscoGrammar.Default_information_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDefault_information_ipv6_ro_stanza(@NotNull CiscoGrammar.Default_information_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_ipv6_ro_stanza(@NotNull CiscoGrammar.Null_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_ipv6_ro_stanza(@NotNull CiscoGrammar.Null_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_route_reflector_client_af_stanza(@NotNull CiscoGrammar.Neighbor_route_reflector_client_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_route_reflector_client_af_stanza(@NotNull CiscoGrammar.Neighbor_route_reflector_client_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_description_rb_stanza(@NotNull CiscoGrammar.Neighbor_description_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_description_rb_stanza(@NotNull CiscoGrammar.Neighbor_description_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRouter_bgp_stanza_tail(@NotNull CiscoGrammar.Router_bgp_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRouter_bgp_stanza_tail(@NotNull CiscoGrammar.Router_bgp_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_peer_group_assignment_af_stanza(@NotNull CiscoGrammar.Neighbor_peer_group_assignment_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_peer_group_assignment_af_stanza(@NotNull CiscoGrammar.Neighbor_peer_group_assignment_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_description_af_stanza(@NotNull CiscoGrammar.Neighbor_description_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_description_af_stanza(@NotNull CiscoGrammar.Neighbor_description_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAccess_list_action(@NotNull CiscoGrammar.Access_list_actionContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAccess_list_action(@NotNull CiscoGrammar.Access_list_actionContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_default_originate_tail_bgp(@NotNull CiscoGrammar.Neighbor_default_originate_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_default_originate_tail_bgp(@NotNull CiscoGrammar.Neighbor_default_originate_tail_bgpContext ctx) { 
		if(ctx.map!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.map.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_peer_group_creation_rb_stanza(@NotNull CiscoGrammar.Neighbor_peer_group_creation_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_peer_group_creation_rb_stanza(@NotNull CiscoGrammar.Neighbor_peer_group_creation_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNetwork6_rb_stanza(@NotNull CiscoGrammar.Network6_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNetwork6_rb_stanza(@NotNull CiscoGrammar.Network6_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterHsrp_stanza_tail(@NotNull CiscoGrammar.Hsrp_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitHsrp_stanza_tail(@NotNull CiscoGrammar.Hsrp_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_community_list_standard_numbered_stanza(@NotNull CiscoGrammar.Ip_community_list_standard_numbered_stanzaContext ctx) {	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_community_list_standard_numbered_stanza(@NotNull CiscoGrammar.Ip_community_list_standard_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_if_stanza(@NotNull CiscoGrammar.Null_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_if_stanza(@NotNull CiscoGrammar.Null_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMatch_community_list_rm_stanza(@NotNull CiscoGrammar.Match_community_list_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMatch_community_list_rm_stanza(@NotNull CiscoGrammar.Match_community_list_rm_stanzaContext ctx) {
		for(Token name: ctx.name_list){
			AddReference(stanza_type.ACL, name.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNetwork6_tail_bgp(@NotNull CiscoGrammar.Network6_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNetwork6_tail_bgp(@NotNull CiscoGrammar.Network6_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStanza(@NotNull CiscoGrammar.StanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStanza(@NotNull CiscoGrammar.StanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAddress_family_rb_stanza_tail(@NotNull CiscoGrammar.Address_family_rb_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAddress_family_rb_stanza_tail(@NotNull CiscoGrammar.Address_family_rb_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_community_none_rm_stanza(@NotNull CiscoGrammar.Set_community_none_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_community_none_rm_stanza(@NotNull CiscoGrammar.Set_community_none_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAggregate_address_rb_stanza(@NotNull CiscoGrammar.Aggregate_address_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAggregate_address_rb_stanza(@NotNull CiscoGrammar.Aggregate_address_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_next_hop_rm_stanza(@NotNull CiscoGrammar.Set_next_hop_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_next_hop_rm_stanza(@NotNull CiscoGrammar.Set_next_hop_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMatch_tag_rm_stanza(@NotNull CiscoGrammar.Match_tag_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMatch_tag_rm_stanza(@NotNull CiscoGrammar.Match_tag_rm_stanzaContext ctx) {
		System.out.println("Try to match a tag, but I did not see where the tags are defined");
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_metric_rm_stanza(@NotNull CiscoGrammar.Set_metric_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_metric_rm_stanza(@NotNull CiscoGrammar.Set_metric_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_shutdown_rb_stanza(@NotNull CiscoGrammar.Neighbor_shutdown_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_shutdown_rb_stanza(@NotNull CiscoGrammar.Neighbor_shutdown_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNetwork6_af_stanza(@NotNull CiscoGrammar.Network6_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNetwork6_af_stanza(@NotNull CiscoGrammar.Network6_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_community_list_standard_named_stanza(@NotNull CiscoGrammar.Ip_community_list_standard_named_stanzaContext ctx) {	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_community_list_standard_named_stanza(@NotNull CiscoGrammar.Ip_community_list_standard_named_stanzaContext ctx) { 
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAppletalk_access_list_null_tail(@NotNull CiscoGrammar.Appletalk_access_list_null_tailContext ctx) {	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAppletalk_access_list_null_tail(@NotNull CiscoGrammar.Appletalk_access_list_null_tailContext ctx) {
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_as_path_access_list_tail(@NotNull CiscoGrammar.Ip_as_path_access_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_as_path_access_list_tail(@NotNull CiscoGrammar.Ip_as_path_access_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDefault_metric_af_stanza(@NotNull CiscoGrammar.Default_metric_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDefault_metric_af_stanza(@NotNull CiscoGrammar.Default_metric_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNetwork_ro_stanza(@NotNull CiscoGrammar.Network_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNetwork_ro_stanza(@NotNull CiscoGrammar.Network_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterInterface_name(@NotNull CiscoGrammar.Interface_nameContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitInterface_name(@NotNull CiscoGrammar.Interface_nameContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_trunk_encapsulation(@NotNull CiscoGrammar.Switchport_trunk_encapsulationContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_trunk_encapsulation(@NotNull CiscoGrammar.Switchport_trunk_encapsulationContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterProtocol_type_code_access_list_null_tail(@NotNull CiscoGrammar.Protocol_type_code_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitProtocol_type_code_access_list_null_tail(@NotNull CiscoGrammar.Protocol_type_code_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_community_list_expanded_numbered_stanza(@NotNull CiscoGrammar.Ip_community_list_expanded_numbered_stanzaContext ctx) { 
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_community_list_expanded_numbered_stanza(@NotNull CiscoGrammar.Ip_community_list_expanded_numbered_stanzaContext ctx) {
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterShutdown_if_stanza(@NotNull CiscoGrammar.Shutdown_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitShutdown_if_stanza(@NotNull CiscoGrammar.Shutdown_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMatch_ip_prefix_list_rm_stanza(@NotNull CiscoGrammar.Match_ip_prefix_list_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMatch_ip_prefix_list_rm_stanza(@NotNull CiscoGrammar.Match_ip_prefix_list_rm_stanzaContext ctx) {
		for(Token name: ctx.name_list){
			AddReference(stanza_type.ACL, name.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_aggregate_af_stanza(@NotNull CiscoGrammar.Redistribute_aggregate_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_aggregate_af_stanza(@NotNull CiscoGrammar.Redistribute_aggregate_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterHsrp_stanza(@NotNull CiscoGrammar.Hsrp_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitHsrp_stanza(@NotNull CiscoGrammar.Hsrp_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRouter_id_bgp_rb_stanza(@NotNull CiscoGrammar.Router_id_bgp_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRouter_id_bgp_rb_stanza(@NotNull CiscoGrammar.Router_id_bgp_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_community_list_standard_stanza(@NotNull CiscoGrammar.Ip_community_list_standard_stanzaContext ctx) { 
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_community_list_standard_stanza(@NotNull CiscoGrammar.Ip_community_list_standard_stanzaContext ctx) {
		if(ctx.named!=null){
			exitStanza(ctx.named.name.getText());
		}
		else{
			exitStanza(ctx.numbered.name.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIf_stanza(@NotNull CiscoGrammar.If_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIf_stanza(@NotNull CiscoGrammar.If_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_ro_stanza(@NotNull CiscoGrammar.Null_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_ro_stanza(@NotNull CiscoGrammar.Null_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_ipv6_rm_stanza(@NotNull CiscoGrammar.Set_ipv6_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_ipv6_rm_stanza(@NotNull CiscoGrammar.Set_ipv6_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_community_list_expanded_named_stanza(@NotNull CiscoGrammar.Ip_community_list_expanded_named_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_community_list_expanded_named_stanza(@NotNull CiscoGrammar.Ip_community_list_expanded_named_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_nexus_shutdown_stanza(@NotNull CiscoGrammar.Neighbor_nexus_shutdown_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_nexus_shutdown_stanza(@NotNull CiscoGrammar.Neighbor_nexus_shutdown_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_connected_tail_bgp(@NotNull CiscoGrammar.Redistribute_connected_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_connected_tail_bgp(@NotNull CiscoGrammar.Redistribute_connected_tail_bgpContext ctx) {
		if(ctx.map!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.map.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRoute_map_stanza(@NotNull CiscoGrammar.Route_map_stanzaContext ctx) {
		enterStanza(stanza_type.ROUTEMAP);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRoute_map_stanza(@NotNull CiscoGrammar.Route_map_stanzaContext ctx) {
		exitStanza(ctx.named.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMatch_as_path_access_list_rm_stanza(@NotNull CiscoGrammar.Match_as_path_access_list_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMatch_as_path_access_list_rm_stanza(@NotNull CiscoGrammar.Match_as_path_access_list_rm_stanzaContext ctx) {
		for(Token name : ctx.name_list){
			AddReference(stanza_type.ACL, name.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAddress_family_rb_stanza(@NotNull CiscoGrammar.Address_family_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAddress_family_rb_stanza(@NotNull CiscoGrammar.Address_family_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRouter_rip_stanza(@NotNull CiscoGrammar.Router_rip_stanzaContext ctx) { 
		System.out.println("There is a rip stanza, I ignore it.");
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRouter_rip_stanza(@NotNull CiscoGrammar.Router_rip_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRo_stanza(@NotNull CiscoGrammar.Ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRo_stanza(@NotNull CiscoGrammar.Ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_community_rm_stanza(@NotNull CiscoGrammar.Set_community_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_community_rm_stanza(@NotNull CiscoGrammar.Set_community_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_static_tail_bgp(@NotNull CiscoGrammar.Redistribute_static_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_static_tail_bgp(@NotNull CiscoGrammar.Redistribute_static_tail_bgpContext ctx) {
		if(ctx.map!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.map.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBanner_stanza(@NotNull CiscoGrammar.Banner_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBanner_stanza(@NotNull CiscoGrammar.Banner_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_comm_list_delete_rm_stanza(@NotNull CiscoGrammar.Set_comm_list_delete_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_comm_list_delete_rm_stanza(@NotNull CiscoGrammar.Set_comm_list_delete_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_trunk_native_if_stanza(@NotNull CiscoGrammar.Switchport_trunk_native_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_trunk_native_if_stanza(@NotNull CiscoGrammar.Switchport_trunk_native_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRoute_map_tail(@NotNull CiscoGrammar.Route_map_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRoute_map_tail(@NotNull CiscoGrammar.Route_map_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_nexus_vrf_rb_substanza(@NotNull CiscoGrammar.Neighbor_nexus_vrf_rb_substanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_nexus_vrf_rb_substanza(@NotNull CiscoGrammar.Neighbor_nexus_vrf_rb_substanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_address_secondary_if_stanza(@NotNull CiscoGrammar.Ip_address_secondary_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_address_secondary_if_stanza(@NotNull CiscoGrammar.Ip_address_secondary_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_route_map_af_stanza(@NotNull CiscoGrammar.Neighbor_route_map_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_route_map_af_stanza(@NotNull CiscoGrammar.Neighbor_route_map_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMatch_length_rm_stanza(@NotNull CiscoGrammar.Match_length_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMatch_length_rm_stanza(@NotNull CiscoGrammar.Match_length_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_connected_rb_stanza(@NotNull CiscoGrammar.Redistribute_connected_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_connected_rb_stanza(@NotNull CiscoGrammar.Redistribute_connected_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_standalone_ro_stanza(@NotNull CiscoGrammar.Null_standalone_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_standalone_ro_stanza(@NotNull CiscoGrammar.Null_standalone_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_prefix_list_stanza(@NotNull CiscoGrammar.Ip_prefix_list_stanzaContext ctx) { 
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_prefix_list_stanza(@NotNull CiscoGrammar.Ip_prefix_list_stanzaContext ctx) {
		exitStanza(ctx.named.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_prefix_list_rb_stanza(@NotNull CiscoGrammar.Neighbor_prefix_list_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_prefix_list_rb_stanza(@NotNull CiscoGrammar.Neighbor_prefix_list_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStandard_access_list_numbered_stanza(@NotNull CiscoGrammar.Standard_access_list_numbered_stanzaContext ctx) {	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStandard_access_list_numbered_stanza(@NotNull CiscoGrammar.Standard_access_list_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_route_map_tail_bgp(@NotNull CiscoGrammar.Neighbor_route_map_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_route_map_tail_bgp(@NotNull CiscoGrammar.Neighbor_route_map_tail_bgpContext ctx) {
		AddReference(stanza_type.ROUTEMAP, ctx.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_rm_stanza(@NotNull CiscoGrammar.Set_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_rm_stanza(@NotNull CiscoGrammar.Set_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_rip_ro_stanza(@NotNull CiscoGrammar.Redistribute_rip_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_rip_ro_stanza(@NotNull CiscoGrammar.Redistribute_rip_ro_stanzaContext ctx) {
		System.out.println("there is a reference to rip, check it");
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAggregate_address_tail_bgp(@NotNull CiscoGrammar.Aggregate_address_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAggregate_address_tail_bgp(@NotNull CiscoGrammar.Aggregate_address_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIpv6_router_ospf_stanza(@NotNull CiscoGrammar.Ipv6_router_ospf_stanzaContext ctx) { 
		enterStanza(stanza_type.ROUTER);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIpv6_router_ospf_stanza(@NotNull CiscoGrammar.Ipv6_router_ospf_stanzaContext ctx) {
		exitStanza("ospf_"+ctx.procnum.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_aggregate_tail_bgp(@NotNull CiscoGrammar.Redistribute_aggregate_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_aggregate_tail_bgp(@NotNull CiscoGrammar.Redistribute_aggregate_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_trunk_allowed_if_stanza(@NotNull CiscoGrammar.Switchport_trunk_allowed_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_trunk_allowed_if_stanza(@NotNull CiscoGrammar.Switchport_trunk_allowed_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_mode_dynamic_desirable_stanza(@NotNull CiscoGrammar.Switchport_mode_dynamic_desirable_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_mode_dynamic_desirable_stanza(@NotNull CiscoGrammar.Switchport_mode_dynamic_desirable_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExtended_access_list_stanza(@NotNull CiscoGrammar.Extended_access_list_stanzaContext ctx) {
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExtended_access_list_stanza(@NotNull CiscoGrammar.Extended_access_list_stanzaContext ctx) { 
		if(ctx.named!=null){
			exitStanza(ctx.named.name.getText());
		}
		else{
			exitStanza(ctx.numbered.name.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_nexus_stanza(@NotNull CiscoGrammar.Neighbor_nexus_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_nexus_stanza(@NotNull CiscoGrammar.Neighbor_nexus_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRb_stanza(@NotNull CiscoGrammar.Rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRb_stanza(@NotNull CiscoGrammar.Rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTemplate_peer_stanza(@NotNull CiscoGrammar.Template_peer_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTemplate_peer_stanza(@NotNull CiscoGrammar.Template_peer_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMatch_ip_access_list_rm_stanza(@NotNull CiscoGrammar.Match_ip_access_list_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMatch_ip_access_list_rm_stanza(@NotNull CiscoGrammar.Match_ip_access_list_rm_stanzaContext ctx) {
		for(Token name: ctx.name_list){
			AddReference(stanza_type.ACL, name.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDefault_information_ro_stanza(@NotNull CiscoGrammar.Default_information_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDefault_information_ro_stanza(@NotNull CiscoGrammar.Default_information_ro_stanzaContext ctx) {
		if(ctx.map!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.map.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_description_tail_bgp(@NotNull CiscoGrammar.Neighbor_description_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_description_tail_bgp(@NotNull CiscoGrammar.Neighbor_description_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_community_list_standard_tail(@NotNull CiscoGrammar.Ip_community_list_standard_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_community_list_standard_tail(@NotNull CiscoGrammar.Ip_community_list_standard_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_remote_as_rb_stanza(@NotNull CiscoGrammar.Neighbor_remote_as_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_remote_as_rb_stanza(@NotNull CiscoGrammar.Neighbor_remote_as_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_address_stanza(@NotNull CiscoGrammar.Ip_address_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_address_stanza(@NotNull CiscoGrammar.Ip_address_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_address_if_stanza(@NotNull CiscoGrammar.Ip_address_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_address_if_stanza(@NotNull CiscoGrammar.Ip_address_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPreempt_stanza(@NotNull CiscoGrammar.Preempt_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPreempt_stanza(@NotNull CiscoGrammar.Preempt_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_default_originate_rb_stanza(@NotNull CiscoGrammar.Neighbor_default_originate_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_default_originate_rb_stanza(@NotNull CiscoGrammar.Neighbor_default_originate_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNexus_access_list_stanza(@NotNull CiscoGrammar.Nexus_access_list_stanzaContext ctx) {
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNexus_access_list_stanza(@NotNull CiscoGrammar.Nexus_access_list_stanzaContext ctx) {
		exitStanza( ctx.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_stanza(@NotNull CiscoGrammar.Null_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_stanza(@NotNull CiscoGrammar.Null_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNo_neighbor_activate_af_stanza(@NotNull CiscoGrammar.No_neighbor_activate_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNo_neighbor_activate_af_stanza(@NotNull CiscoGrammar.No_neighbor_activate_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAddress_family_vrf_stanza(@NotNull CiscoGrammar.Address_family_vrf_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAddress_family_vrf_stanza(@NotNull CiscoGrammar.Address_family_vrf_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_as_path_prepend_rm_stanza(@NotNull CiscoGrammar.Set_as_path_prepend_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_as_path_prepend_rm_stanza(@NotNull CiscoGrammar.Set_as_path_prepend_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_next_hop_self_tail_bgp(@NotNull CiscoGrammar.Neighbor_next_hop_self_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_next_hop_self_tail_bgp(@NotNull CiscoGrammar.Neighbor_next_hop_self_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_mode_access_stanza(@NotNull CiscoGrammar.Switchport_mode_access_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_mode_access_stanza(@NotNull CiscoGrammar.Switchport_mode_access_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_send_community_rb_stanza(@NotNull CiscoGrammar.Neighbor_send_community_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_send_community_rb_stanza(@NotNull CiscoGrammar.Neighbor_send_community_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_standalone_af_stanza(@NotNull CiscoGrammar.Null_standalone_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_standalone_af_stanza(@NotNull CiscoGrammar.Null_standalone_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPassive_interface_ipv6_ro_stanza(@NotNull CiscoGrammar.Passive_interface_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPassive_interface_ipv6_ro_stanza(@NotNull CiscoGrammar.Passive_interface_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTemplate_peer_stanza_tail(@NotNull CiscoGrammar.Template_peer_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTemplate_peer_stanza_tail(@NotNull CiscoGrammar.Template_peer_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRr_stanza(@NotNull CiscoGrammar.Rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRr_stanza(@NotNull CiscoGrammar.Rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_ospf_tail_bgp(@NotNull CiscoGrammar.Redistribute_ospf_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_ospf_tail_bgp(@NotNull CiscoGrammar.Redistribute_ospf_tail_bgpContext ctx) {
		AddReference(stanza_type.ROUTER, "ospf_"+ctx.procnum.getText());
		if(ctx.map!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.map.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_default_gateway_stanza(@NotNull CiscoGrammar.Ip_default_gateway_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_default_gateway_stanza(@NotNull CiscoGrammar.Ip_default_gateway_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_policy_if_stanza(@NotNull CiscoGrammar.Ip_policy_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_policy_if_stanza(@NotNull CiscoGrammar.Ip_policy_if_stanzaContext ctx) {
		AddReference(stanza_type.ROUTEMAP, ctx.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIpx_sap_access_list_stanza(@NotNull CiscoGrammar.Ipx_sap_access_list_stanzaContext ctx) {
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIpx_sap_access_list_stanza(@NotNull CiscoGrammar.Ipx_sap_access_list_stanzaContext ctx) {
		exitStanza(ctx.numbered.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_static_ro_stanza(@NotNull CiscoGrammar.Redistribute_static_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_static_ro_stanza(@NotNull CiscoGrammar.Redistribute_static_ro_stanzaContext ctx) {
		if(ctx.map!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.map.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_next_hop_self_af_stanza(@NotNull CiscoGrammar.Neighbor_next_hop_self_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_next_hop_self_af_stanza(@NotNull CiscoGrammar.Neighbor_next_hop_self_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_next_hop_self_rb_stanza(@NotNull CiscoGrammar.Neighbor_next_hop_self_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_next_hop_self_rb_stanza(@NotNull CiscoGrammar.Neighbor_next_hop_self_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNetwork_rr_stanza(@NotNull CiscoGrammar.Network_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNetwork_rr_stanza(@NotNull CiscoGrammar.Network_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVrf_context_stanza(@NotNull CiscoGrammar.Vrf_context_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVrf_context_stanza(@NotNull CiscoGrammar.Vrf_context_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_remove_private_as_af_stanza(@NotNull CiscoGrammar.Neighbor_remove_private_as_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_remove_private_as_af_stanza(@NotNull CiscoGrammar.Neighbor_remove_private_as_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDefault_metric_rb_stanza(@NotNull CiscoGrammar.Default_metric_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDefault_metric_rb_stanza(@NotNull CiscoGrammar.Default_metric_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIpx_sap_access_list_null_tail(@NotNull CiscoGrammar.Ipx_sap_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIpx_sap_access_list_null_tail(@NotNull CiscoGrammar.Ipx_sap_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_as_path_access_list_stanza(@NotNull CiscoGrammar.Ip_as_path_access_list_stanzaContext ctx) { 
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_as_path_access_list_stanza(@NotNull CiscoGrammar.Ip_as_path_access_list_stanzaContext ctx) {
		exitStanza(ctx.numbered.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_block_stanza(@NotNull CiscoGrammar.Null_block_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_block_stanza(@NotNull CiscoGrammar.Null_block_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterProtocol(@NotNull CiscoGrammar.ProtocolContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitProtocol(@NotNull CiscoGrammar.ProtocolContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterArea_ipv6_ro_stanza(@NotNull CiscoGrammar.Area_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitArea_ipv6_ro_stanza(@NotNull CiscoGrammar.Area_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCommunity(@NotNull CiscoGrammar.CommunityContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCommunity(@NotNull CiscoGrammar.CommunityContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_rm_stanza(@NotNull CiscoGrammar.Null_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_rm_stanza(@NotNull CiscoGrammar.Null_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_nexus_tail(@NotNull CiscoGrammar.Neighbor_nexus_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_nexus_tail(@NotNull CiscoGrammar.Neighbor_nexus_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_ip_df_rm_stanza(@NotNull CiscoGrammar.Set_ip_df_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_ip_df_rm_stanza(@NotNull CiscoGrammar.Set_ip_df_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_remote_as_af_stanza(@NotNull CiscoGrammar.Neighbor_remote_as_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_remote_as_af_stanza(@NotNull CiscoGrammar.Neighbor_remote_as_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIpv6_ro_stanza(@NotNull CiscoGrammar.Ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIpv6_ro_stanza(@NotNull CiscoGrammar.Ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStandard_access_list_tail(@NotNull CiscoGrammar.Standard_access_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStandard_access_list_tail(@NotNull CiscoGrammar.Standard_access_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVrf_rb_substanza(@NotNull CiscoGrammar.Vrf_rb_substanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVrf_rb_substanza(@NotNull CiscoGrammar.Vrf_rb_substanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_static_af_stanza(@NotNull CiscoGrammar.Redistribute_static_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_static_af_stanza(@NotNull CiscoGrammar.Redistribute_static_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNetwork_rb_stanza(@NotNull CiscoGrammar.Network_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNetwork_rb_stanza(@NotNull CiscoGrammar.Network_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStandard_access_list_stanza(@NotNull CiscoGrammar.Standard_access_list_stanzaContext ctx) {
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStandard_access_list_stanza(@NotNull CiscoGrammar.Standard_access_list_stanzaContext ctx) {
		if(ctx.named!=null){
			exitStanza(ctx.named.name.getText());
		}
		else{
			exitStanza(ctx.numbered.name.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_route_map_rb_stanza(@NotNull CiscoGrammar.Neighbor_route_map_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_route_map_rb_stanza(@NotNull CiscoGrammar.Neighbor_route_map_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMatch_ipv6_rm_stanza(@NotNull CiscoGrammar.Match_ipv6_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMatch_ipv6_rm_stanza(@NotNull CiscoGrammar.Match_ipv6_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_remote_as_tail_bgp(@NotNull CiscoGrammar.Neighbor_remote_as_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_remote_as_tail_bgp(@NotNull CiscoGrammar.Neighbor_remote_as_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_ebgp_multihop_af_stanza(@NotNull CiscoGrammar.Neighbor_ebgp_multihop_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_ebgp_multihop_af_stanza(@NotNull CiscoGrammar.Neighbor_ebgp_multihop_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAuto_summary_af_stanza(@NotNull CiscoGrammar.Auto_summary_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAuto_summary_af_stanza(@NotNull CiscoGrammar.Auto_summary_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRouter_ospf_stanza(@NotNull CiscoGrammar.Router_ospf_stanzaContext ctx) {
		enterStanza(stanza_type.ROUTER);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRouter_ospf_stanza(@NotNull CiscoGrammar.Router_ospf_stanzaContext ctx) { 
		exitStanza("ospf_"+ctx.procnum.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDescription_line(@NotNull CiscoGrammar.Description_lineContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDescription_line(@NotNull CiscoGrammar.Description_lineContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_shutdown_af_stanza(@NotNull CiscoGrammar.Neighbor_shutdown_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_shutdown_af_stanza(@NotNull CiscoGrammar.Neighbor_shutdown_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPriority_stanza(@NotNull CiscoGrammar.Priority_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPriority_stanza(@NotNull CiscoGrammar.Priority_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCertificate_stanza(@NotNull CiscoGrammar.Certificate_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCertificate_stanza(@NotNull CiscoGrammar.Certificate_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_peer_group_assignment_tail_bgp(@NotNull CiscoGrammar.Neighbor_peer_group_assignment_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_peer_group_assignment_tail_bgp(@NotNull CiscoGrammar.Neighbor_peer_group_assignment_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAggregate_address_af_stanza(@NotNull CiscoGrammar.Aggregate_address_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAggregate_address_af_stanza(@NotNull CiscoGrammar.Aggregate_address_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAccess_list_ip_range(@NotNull CiscoGrammar.Access_list_ip_rangeContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAccess_list_ip_range(@NotNull CiscoGrammar.Access_list_ip_rangeContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_default_originate_af_stanza(@NotNull CiscoGrammar.Neighbor_default_originate_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_default_originate_af_stanza(@NotNull CiscoGrammar.Neighbor_default_originate_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_rr_stanza(@NotNull CiscoGrammar.Redistribute_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_rr_stanza(@NotNull CiscoGrammar.Redistribute_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_mode_trunk_stanza(@NotNull CiscoGrammar.Switchport_mode_trunk_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_mode_trunk_stanza(@NotNull CiscoGrammar.Switchport_mode_trunk_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_bgp_ro_stanza(@NotNull CiscoGrammar.Redistribute_bgp_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_bgp_ro_stanza(@NotNull CiscoGrammar.Redistribute_bgp_ro_stanzaContext ctx) {
		if(ctx.map!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.map.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_access_if_stanza(@NotNull CiscoGrammar.Switchport_access_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_access_if_stanza(@NotNull CiscoGrammar.Switchport_access_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPassive_interface_rr_stanza(@NotNull CiscoGrammar.Passive_interface_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPassive_interface_rr_stanza(@NotNull CiscoGrammar.Passive_interface_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDescription_if_stanza(@NotNull CiscoGrammar.Description_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDescription_if_stanza(@NotNull CiscoGrammar.Description_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_local_preference_rm_stanza(@NotNull CiscoGrammar.Set_local_preference_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_local_preference_rm_stanza(@NotNull CiscoGrammar.Set_local_preference_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_block_substanza(@NotNull CiscoGrammar.Null_block_substanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_block_substanza(@NotNull CiscoGrammar.Null_block_substanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCluster_id_bgp_rb_stanza(@NotNull CiscoGrammar.Cluster_id_bgp_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCluster_id_bgp_rb_stanza(@NotNull CiscoGrammar.Cluster_id_bgp_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_mode_dynamic_auto_stanza(@NotNull CiscoGrammar.Switchport_mode_dynamic_auto_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_mode_dynamic_auto_stanza(@NotNull CiscoGrammar.Switchport_mode_dynamic_auto_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStandard_access_list_named_stanza(@NotNull CiscoGrammar.Standard_access_list_named_stanzaContext ctx) {	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStandard_access_list_named_stanza(@NotNull CiscoGrammar.Standard_access_list_named_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRouter_id_ipv6_ro_stanza(@NotNull CiscoGrammar.Router_id_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRouter_id_ipv6_ro_stanza(@NotNull CiscoGrammar.Router_id_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVrf_rb_stanza(@NotNull CiscoGrammar.Vrf_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVrf_rb_stanza(@NotNull CiscoGrammar.Vrf_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPassive_interface_ro_stanza(@NotNull CiscoGrammar.Passive_interface_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPassive_interface_ro_stanza(@NotNull CiscoGrammar.Passive_interface_ro_stanzaContext ctx) {
		AddReference(stanza_type.IFACE, ctx.i.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterHostname_stanza(@NotNull CiscoGrammar.Hostname_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitHostname_stanza(@NotNull CiscoGrammar.Hostname_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAf_stanza(@NotNull CiscoGrammar.Af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAf_stanza(@NotNull CiscoGrammar.Af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_send_community_af_stanza(@NotNull CiscoGrammar.Neighbor_send_community_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_send_community_af_stanza(@NotNull CiscoGrammar.Neighbor_send_community_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_community_list_expanded_stanza(@NotNull CiscoGrammar.Ip_community_list_expanded_stanzaContext ctx) {
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_community_list_expanded_stanza(@NotNull CiscoGrammar.Ip_community_list_expanded_stanzaContext ctx) {
		if(ctx.named!=null){
			exitStanza(ctx.named.name.getText());
		}
		else{
			exitStanza(ctx.numbered.name.getText());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRoute_map_tail_tail(@NotNull CiscoGrammar.Route_map_tail_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRoute_map_tail_tail(@NotNull CiscoGrammar.Route_map_tail_tailContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIpx_sap_access_list_numbered_stanza(@NotNull CiscoGrammar.Ipx_sap_access_list_numbered_stanzaContext ctx) {}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIpx_sap_access_list_numbered_stanza(@NotNull CiscoGrammar.Ipx_sap_access_list_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_ipv6_ro_stanza(@NotNull CiscoGrammar.Redistribute_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_ipv6_ro_stanza(@NotNull CiscoGrammar.Redistribute_ipv6_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_distribute_list_rb_stanza(@NotNull CiscoGrammar.Neighbor_distribute_list_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_distribute_list_rb_stanza(@NotNull CiscoGrammar.Neighbor_distribute_list_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRouter_bgp_stanza(@NotNull CiscoGrammar.Router_bgp_stanzaContext ctx) {
		enterStanza(stanza_type.ROUTER);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRouter_bgp_stanza(@NotNull CiscoGrammar.Router_bgp_stanzaContext ctx) { 
		exitStanza("bgp_"+ctx.procnum.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_distribute_list_af_stanza(@NotNull CiscoGrammar.Neighbor_distribute_list_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_distribute_list_af_stanza(@NotNull CiscoGrammar.Neighbor_distribute_list_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_static_rb_stanza(@NotNull CiscoGrammar.Redistribute_static_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_static_rb_stanza(@NotNull CiscoGrammar.Redistribute_static_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPort(@NotNull CiscoGrammar.PortContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPort(@NotNull CiscoGrammar.PortContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_interface_rm_stanza(@NotNull CiscoGrammar.Set_interface_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_interface_rm_stanza(@NotNull CiscoGrammar.Set_interface_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterInterface_stanza_tail(@NotNull CiscoGrammar.Interface_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitInterface_stanza_tail(@NotNull CiscoGrammar.Interface_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRange(@NotNull CiscoGrammar.RangeContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRange(@NotNull CiscoGrammar.RangeContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_as_path_numbered_stanza(@NotNull CiscoGrammar.Ip_as_path_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_as_path_numbered_stanza(@NotNull CiscoGrammar.Ip_as_path_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_peer_group_creation_tail_bgp(@NotNull CiscoGrammar.Neighbor_peer_group_creation_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_peer_group_creation_tail_bgp(@NotNull CiscoGrammar.Neighbor_peer_group_creation_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExtended_access_list_null_tail(@NotNull CiscoGrammar.Extended_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExtended_access_list_null_tail(@NotNull CiscoGrammar.Extended_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNo_ip_address_if_stanza(@NotNull CiscoGrammar.No_ip_address_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNo_ip_address_if_stanza(@NotNull CiscoGrammar.No_ip_address_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitchport_trunk_encapsulation_if_stanza(@NotNull CiscoGrammar.Switchport_trunk_encapsulation_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitchport_trunk_encapsulation_if_stanza(@NotNull CiscoGrammar.Switchport_trunk_encapsulation_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_origin_rm_stanza(@NotNull CiscoGrammar.Set_origin_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_origin_rm_stanza(@NotNull CiscoGrammar.Set_origin_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_ospf_rb_stanza(@NotNull CiscoGrammar.Redistribute_ospf_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_ospf_rb_stanza(@NotNull CiscoGrammar.Redistribute_ospf_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_extcomm_list_rm_stanza(@NotNull CiscoGrammar.Set_extcomm_list_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_extcomm_list_rm_stanza(@NotNull CiscoGrammar.Set_extcomm_list_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_route_stanza(@NotNull CiscoGrammar.Ip_route_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_route_stanza(@NotNull CiscoGrammar.Ip_route_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_activate_af_stanza(@NotNull CiscoGrammar.Neighbor_activate_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_activate_af_stanza(@NotNull CiscoGrammar.Neighbor_activate_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStandard_access_list_null_tail(@NotNull CiscoGrammar.Standard_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStandard_access_list_null_tail(@NotNull CiscoGrammar.Standard_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAf_vrf_rb_substanza(@NotNull CiscoGrammar.Af_vrf_rb_substanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAf_vrf_rb_substanza(@NotNull CiscoGrammar.Af_vrf_rb_substanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_ebgp_multihop_tail_bgp(@NotNull CiscoGrammar.Neighbor_ebgp_multihop_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_ebgp_multihop_tail_bgp(@NotNull CiscoGrammar.Neighbor_ebgp_multihop_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMatch_rm_stanza(@NotNull CiscoGrammar.Match_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMatch_rm_stanza(@NotNull CiscoGrammar.Match_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_template_peer_stanza(@NotNull CiscoGrammar.Null_template_peer_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_template_peer_stanza(@NotNull CiscoGrammar.Null_template_peer_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_rb_stanza(@NotNull CiscoGrammar.Null_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_rb_stanza(@NotNull CiscoGrammar.Null_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_prefix_list_null_tail(@NotNull CiscoGrammar.Ip_prefix_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_prefix_list_null_tail(@NotNull CiscoGrammar.Ip_prefix_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterProtocol_type_code_access_list_numbered_stanza(@NotNull CiscoGrammar.Protocol_type_code_access_list_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitProtocol_type_code_access_list_numbered_stanza(@NotNull CiscoGrammar.Protocol_type_code_access_list_numbered_stanzaContext ctx) {}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_update_source_rb_stanza(@NotNull CiscoGrammar.Neighbor_update_source_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_update_source_rb_stanza(@NotNull CiscoGrammar.Neighbor_update_source_rb_stanzaContext ctx) {
		AddReference(stanza_type.IFACE, ctx.source.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_prefix_list_tail(@NotNull CiscoGrammar.Ip_prefix_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_prefix_list_tail(@NotNull CiscoGrammar.Ip_prefix_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCisco_configuration(@NotNull CiscoGrammar.Cisco_configurationContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCisco_configuration(@NotNull CiscoGrammar.Cisco_configurationContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_ebgp_multihop_rb_stanza(@NotNull CiscoGrammar.Neighbor_ebgp_multihop_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_ebgp_multihop_rb_stanza(@NotNull CiscoGrammar.Neighbor_ebgp_multihop_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTemplate_peer_remote_as(@NotNull CiscoGrammar.Template_peer_remote_asContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTemplate_peer_remote_as(@NotNull CiscoGrammar.Template_peer_remote_asContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMacro_stanza(@NotNull CiscoGrammar.Macro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMacro_stanza(@NotNull CiscoGrammar.Macro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_standalone_if_stanza(@NotNull CiscoGrammar.Null_standalone_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_standalone_if_stanza(@NotNull CiscoGrammar.Null_standalone_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRouter_ospf_stanza_tail(@NotNull CiscoGrammar.Router_ospf_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRouter_ospf_stanza_tail(@NotNull CiscoGrammar.Router_ospf_stanza_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExact_match(@NotNull CiscoGrammar.Exact_matchContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExact_match(@NotNull CiscoGrammar.Exact_matchContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSubrange(@NotNull CiscoGrammar.SubrangeContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSubrange(@NotNull CiscoGrammar.SubrangeContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPort_specifier(@NotNull CiscoGrammar.Port_specifierContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPort_specifier(@NotNull CiscoGrammar.Port_specifierContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_mpls_label_rm_stanza(@NotNull CiscoGrammar.Set_mpls_label_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_mpls_label_rm_stanza(@NotNull CiscoGrammar.Set_mpls_label_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_connected_ro_stanza(@NotNull CiscoGrammar.Redistribute_connected_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_connected_ro_stanza(@NotNull CiscoGrammar.Redistribute_connected_ro_stanzaContext ctx) {
		if(ctx.map!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.map.getText());
		}		
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_distribute_list_tail_bgp(@NotNull CiscoGrammar.Neighbor_distribute_list_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_distribute_list_tail_bgp(@NotNull CiscoGrammar.Neighbor_distribute_list_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_shutdown_tail_bgp(@NotNull CiscoGrammar.Neighbor_shutdown_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_shutdown_tail_bgp(@NotNull CiscoGrammar.Neighbor_shutdown_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_metric_type_rm_stanza(@NotNull CiscoGrammar.Set_metric_type_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_metric_type_rm_stanza(@NotNull CiscoGrammar.Set_metric_type_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterInterface_stanza(@NotNull CiscoGrammar.Interface_stanzaContext ctx) {
		enterStanza(stanza_type.IFACE);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitInterface_stanza(@NotNull CiscoGrammar.Interface_stanzaContext ctx) { 
		exitStanza(ctx.iname.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_send_community_tail_bgp(@NotNull CiscoGrammar.Neighbor_send_community_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_send_community_tail_bgp(@NotNull CiscoGrammar.Neighbor_send_community_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_standalone_rb_stanza(@NotNull CiscoGrammar.Null_standalone_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_standalone_rb_stanza(@NotNull CiscoGrammar.Null_standalone_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExtended_access_list_named_stanza(@NotNull CiscoGrammar.Extended_access_list_named_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExtended_access_list_named_stanza(@NotNull CiscoGrammar.Extended_access_list_named_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAppletalk_access_list_numbered_stanza(@NotNull CiscoGrammar.Appletalk_access_list_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAppletalk_access_list_numbered_stanza(@NotNull CiscoGrammar.Appletalk_access_list_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_ospf_dead_interval_minimal_if_stanza(@NotNull CiscoGrammar.Ip_ospf_dead_interval_minimal_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_ospf_dead_interval_minimal_if_stanza(@NotNull CiscoGrammar.Ip_ospf_dead_interval_minimal_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_filter_list_af_stanza(@NotNull CiscoGrammar.Neighbor_filter_list_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_filter_list_af_stanza(@NotNull CiscoGrammar.Neighbor_filter_list_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTemplate_peer_update_source(@NotNull CiscoGrammar.Template_peer_update_sourceContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTemplate_peer_update_source(@NotNull CiscoGrammar.Template_peer_update_sourceContext ctx) {
		AddReference(stanza_type.IFACE, ctx.source.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_ospf_cost_if_stanza(@NotNull CiscoGrammar.Ip_ospf_cost_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_ospf_cost_if_stanza(@NotNull CiscoGrammar.Ip_ospf_cost_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDistance_rr_stanza(@NotNull CiscoGrammar.Distance_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDistance_rr_stanza(@NotNull CiscoGrammar.Distance_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_access_group_if_stanza(@NotNull CiscoGrammar.Ip_access_group_if_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_access_group_if_stanza(@NotNull CiscoGrammar.Ip_access_group_if_stanzaContext ctx) {
		AddReference(stanza_type.ACL, ctx.name.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_af_stanza(@NotNull CiscoGrammar.Null_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_af_stanza(@NotNull CiscoGrammar.Null_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExtended_access_list_tail(@NotNull CiscoGrammar.Extended_access_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExtended_access_list_tail(@NotNull CiscoGrammar.Extended_access_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterHsrpc_stanza(@NotNull CiscoGrammar.Hsrpc_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitHsrpc_stanza(@NotNull CiscoGrammar.Hsrpc_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_aggregate_rb_stanza(@NotNull CiscoGrammar.Redistribute_aggregate_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_aggregate_rb_stanza(@NotNull CiscoGrammar.Redistribute_aggregate_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_nexus_null_tail(@NotNull CiscoGrammar.Neighbor_nexus_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_nexus_null_tail(@NotNull CiscoGrammar.Neighbor_nexus_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterArea_nssa_ro_stanza(@NotNull CiscoGrammar.Area_nssa_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitArea_nssa_ro_stanza(@NotNull CiscoGrammar.Area_nssa_ro_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_community_list_expanded_tail(@NotNull CiscoGrammar.Ip_community_list_expanded_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_community_list_expanded_tail(@NotNull CiscoGrammar.Ip_community_list_expanded_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_peer_group_creation_af_stanza(@NotNull CiscoGrammar.Neighbor_peer_group_creation_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_peer_group_creation_af_stanza(@NotNull CiscoGrammar.Neighbor_peer_group_creation_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_connected_af_stanza(@NotNull CiscoGrammar.Redistribute_connected_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_connected_af_stanza(@NotNull CiscoGrammar.Redistribute_connected_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_filter_list_tail_bgp(@NotNull CiscoGrammar.Neighbor_filter_list_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_filter_list_tail_bgp(@NotNull CiscoGrammar.Neighbor_filter_list_tail_bgpContext ctx) { 
		AddReference(stanza_type.ACL, ctx.num.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterProtocol_type_code_access_list_stanza(@NotNull CiscoGrammar.Protocol_type_code_access_list_stanzaContext ctx) {
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitProtocol_type_code_access_list_stanza(@NotNull CiscoGrammar.Protocol_type_code_access_list_stanzaContext ctx) { 
		exitStanza(ctx.numbered.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNexus_access_list_tail(@NotNull CiscoGrammar.Nexus_access_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNexus_access_list_tail(@NotNull CiscoGrammar.Nexus_access_list_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRm_stanza(@NotNull CiscoGrammar.Rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRm_stanza(@NotNull CiscoGrammar.Rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRoute_map_named_stanza(@NotNull CiscoGrammar.Route_map_named_stanzaContext ctx) {}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRoute_map_named_stanza(@NotNull CiscoGrammar.Route_map_named_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDefault_metric_tail_bgp(@NotNull CiscoGrammar.Default_metric_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDefault_metric_tail_bgp(@NotNull CiscoGrammar.Default_metric_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExtended_access_list_numbered_stanza(@NotNull CiscoGrammar.Extended_access_list_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExtended_access_list_numbered_stanza(@NotNull CiscoGrammar.Extended_access_list_numbered_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterExtended_access_list_additional_feature(@NotNull CiscoGrammar.Extended_access_list_additional_featureContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitExtended_access_list_additional_feature(@NotNull CiscoGrammar.Extended_access_list_additional_featureContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_prefix_list_tail_bgp(@NotNull CiscoGrammar.Neighbor_prefix_list_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_prefix_list_tail_bgp(@NotNull CiscoGrammar.Neighbor_prefix_list_tail_bgpContext ctx) {
		AddReference(stanza_type.ACL, ctx.list_name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAppletalk_access_list_stanza(@NotNull CiscoGrammar.Appletalk_access_list_stanzaContext ctx) {
		enterStanza(stanza_type.ACL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAppletalk_access_list_stanza(@NotNull CiscoGrammar.Appletalk_access_list_stanzaContext ctx) { 
		exitStanza(ctx.numbered.name.getText());
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_peer_group_assignment_rb_stanza(@NotNull CiscoGrammar.Neighbor_peer_group_assignment_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_peer_group_assignment_rb_stanza(@NotNull CiscoGrammar.Neighbor_peer_group_assignment_rb_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRedistribute_ospf_af_stanza(@NotNull CiscoGrammar.Redistribute_ospf_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRedistribute_ospf_af_stanza(@NotNull CiscoGrammar.Redistribute_ospf_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDistribute_list_rr_stanza(@NotNull CiscoGrammar.Distribute_list_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDistribute_list_rr_stanza(@NotNull CiscoGrammar.Distribute_list_rr_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_template_peer_standalone_stanza(@NotNull CiscoGrammar.Null_template_peer_standalone_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_template_peer_standalone_stanza(@NotNull CiscoGrammar.Null_template_peer_standalone_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIp_prefix_list_named_stanza(@NotNull CiscoGrammar.Ip_prefix_list_named_stanzaContext ctx) {	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIp_prefix_list_named_stanza(@NotNull CiscoGrammar.Ip_prefix_list_named_stanzaContext ctx) { 	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_nexus_inherit_stanza(@NotNull CiscoGrammar.Neighbor_nexus_inherit_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_nexus_inherit_stanza(@NotNull CiscoGrammar.Neighbor_nexus_inherit_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNexus_access_list_null_tail(@NotNull CiscoGrammar.Nexus_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNexus_access_list_null_tail(@NotNull CiscoGrammar.Nexus_access_list_null_tailContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSwitching_mode_stanza(@NotNull CiscoGrammar.Switching_mode_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSwitching_mode_stanza(@NotNull CiscoGrammar.Switching_mode_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNeighbor_prefix_list_af_stanza(@NotNull CiscoGrammar.Neighbor_prefix_list_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNeighbor_prefix_list_af_stanza(@NotNull CiscoGrammar.Neighbor_prefix_list_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNull_standalone_stanza(@NotNull CiscoGrammar.Null_standalone_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNull_standalone_stanza(@NotNull CiscoGrammar.Null_standalone_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_weight_rm_stanza(@NotNull CiscoGrammar.Set_weight_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_weight_rm_stanza(@NotNull CiscoGrammar.Set_weight_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNetwork_af_stanza(@NotNull CiscoGrammar.Network_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNetwork_af_stanza(@NotNull CiscoGrammar.Network_af_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSet_community_additive_rm_stanza(@NotNull CiscoGrammar.Set_community_additive_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSet_community_additive_rm_stanza(@NotNull CiscoGrammar.Set_community_additive_rm_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVrf_stanza(@NotNull CiscoGrammar.Vrf_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVrf_stanza(@NotNull CiscoGrammar.Vrf_stanzaContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNetwork_tail_bgp(@NotNull CiscoGrammar.Network_tail_bgpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNetwork_tail_bgp(@NotNull CiscoGrammar.Network_tail_bgpContext ctx) {
		if(ctx.mapname!=null){
			AddReference(stanza_type.ROUTEMAP, ctx.mapname.getText());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(@NotNull ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(@NotNull ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(@NotNull TerminalNode node) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(@NotNull ErrorNode node) { }
	


	@Override
	public void enterNeighbor_nexus_remote_as_stanza(
			Neighbor_nexus_remote_as_stanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitNeighbor_nexus_remote_as_stanza(
			Neighbor_nexus_remote_as_stanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enterArp_access_list_stanza(Arp_access_list_stanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitArp_access_list_stanza(Arp_access_list_stanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enterNeighbor_nexus_af_stanza_tail(
			Neighbor_nexus_af_stanza_tailContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitNeighbor_nexus_af_stanza_tail(
			Neighbor_nexus_af_stanza_tailContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enterNeighbor_nexus_af_stanza(
			Neighbor_nexus_af_stanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitNeighbor_nexus_af_stanza(Neighbor_nexus_af_stanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enterMac_access_list_stanza(Mac_access_list_stanzaContext ctx) {
		current = new stanza(stanza_type.ACL);
		
	}
	@Override
	public void exitMac_access_list_stanza(Mac_access_list_stanzaContext ctx) {
		exitStanza(ctx.name.getText());
	}
	@Override
	public void enterNeighbor_filter_list_rb_stanza(
			Neighbor_filter_list_rb_stanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitNeighbor_filter_list_rb_stanza(
			Neighbor_filter_list_rb_stanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enterNexus_access_list_statistics(
			Nexus_access_list_statisticsContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitNexus_access_list_statistics(
			Nexus_access_list_statisticsContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTemplate_peer_inherit(Template_peer_inheritContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitTemplate_peer_inherit(Template_peer_inheritContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enterNeighbor_nexus_update_source_stanza(Neighbor_nexus_update_source_stanzaContext ctx) {
		
		
	}
	@Override
	public void exitNeighbor_nexus_update_source_stanza(Neighbor_nexus_update_source_stanzaContext ctx) {
		AddReference(stanza_type.IFACE, ctx.source.getText());
		
	}
	@Override
	public void enterArp_al_substanza(Arp_al_substanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exitArp_al_substanza(Arp_al_substanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterMac_access_list_substanza(
			Mac_access_list_substanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitMac_access_list_substanza(
			Mac_access_list_substanzaContext ctx) {
		// TODO Auto-generated method stub
		
	}
}
