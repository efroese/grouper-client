/**
 * @author mchyzer
 * $Id$
 */
package edu.internet2.middleware.grouperClient.ws.beans;


/**
 * Bean for rest request to get permissions
 */
public class WsRestGetPermissionAssignmentsRequest implements WsRequestBean {

  /** T or F for if attributeDefName objects should be returned */
  private String includeAttributeDefNames;
  
  /**
   * T or F for if attributeDefName objects should be returned
   * @return the attributeDefName
   */
  public String getIncludeAttributeDefNames() {
    return this.includeAttributeDefNames;
  }

  /**
   * T or F for if attributeDefName objects should be returned
   * @param includeAttributeDefNames1
   */
  public void setIncludeAttributeDefNames(String includeAttributeDefNames1) {
    this.includeAttributeDefNames = includeAttributeDefNames1;
  }

  /** T or F for if the permission details should be returned */
  private String includePermissionAssignDetail;
  
  
  
  /**
   * T or F for if the permission details should be returned
   * @return T or F
   */
  public String getIncludePermissionAssignDetail() {
    return this.includePermissionAssignDetail;
  }

  /**
   * T or F for if the permission details should be returned
   * @param includePermissionAssignDetail1
   */
  public void setIncludePermissionAssignDetail(String includePermissionAssignDetail1) {
    this.includePermissionAssignDetail = includePermissionAssignDetail1;
  }

  /** T or F for it attribute assignments should be returned */
  private String includeAttributeAssignments;
  
  /**
   * T or F for it attribute assignments should be returned
   * @return include attribute assignments
   */
  public String getIncludeAttributeAssignments() {
    return this.includeAttributeAssignments;
  }

  /**
   * T or F for it attribute assignments should be returned
   * @param includeAttributeAssignments1
   */
  public void setIncludeAttributeAssignments(String includeAttributeAssignments1) {
    this.includeAttributeAssignments = includeAttributeAssignments1;
  }

  /** is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000 */
  private String clientVersion;
  
  /**
   * is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
   * @return version
   */
  public String getClientVersion() {
    return this.clientVersion;
  }

  /**
   * is the version of the client.  Must be in GrouperWsVersion, e.g. v1_3_000
   * @param clientVersion1
   */
  public void setClientVersion(String clientVersion1) {
    this.clientVersion = clientVersion1;
  }

  /**
   * find assignments in these attribute defs (optional)
   */
  private WsAttributeDefLookup[] wsAttributeDefLookups;
  
  /**
   * find assignments in these attribute defs (optional)
   * @return defs
   */
  public WsAttributeDefLookup[] getWsAttributeDefLookups() {
    return this.wsAttributeDefLookups;
  }

  /**
   * find assignments in these attribute defs (optional)
   * @param wsAttributeDefLookups1
   */
  public void setWsAttributeDefLookups(WsAttributeDefLookup[] wsAttributeDefLookups1) {
    this.wsAttributeDefLookups = wsAttributeDefLookups1;
  }
  
  /**
   * find assignments in these attribute def names (optional)
   */
  private WsAttributeDefNameLookup[] wsAttributeDefNameLookups;
  
  /**
   *  find assignments in these attribute def names (optional)
   *  @return def name lookups
   */
  public WsAttributeDefNameLookup[] getWsAttributeDefNameLookups() {
    return this.wsAttributeDefNameLookups;
  }

  /**
   * find assignments in these attribute def names (optional)
   * @param wsAttributeDefNameLookups1
   */
  public void setWsAttributeDefNameLookups(
      WsAttributeDefNameLookup[] wsAttributeDefNameLookups1) {
    this.wsAttributeDefNameLookups = wsAttributeDefNameLookups1;
  }
  
  /** are roles to look in */
  private WsGroupLookup[] roleLookups;
  
  /**
   * are roles to look in
   * @return owner group lookups
   */
  public WsGroupLookup[] getRoleLookups() {
    return this.roleLookups;
  }

  /**
   * are roles to look in
   * @param wsOwnerGroupLookups1
   */
  public void setRoleLookups(WsGroupLookup[] wsOwnerGroupLookups1) {
    this.roleLookups = wsOwnerGroupLookups1;
  }

  /** are subjects to look in */
  private WsSubjectLookup[] wsSubjectLookups;
  
  
  
  /**
   * are subjects to look in
   * @return subject
   */
  public WsSubjectLookup[] getWsSubjectLookups() {
    return this.wsSubjectLookups;
  }

  /**
   * are subjects to look in
   * @param wsOwnerSubjectLookups1
   */
  public void setWsSubjectLookups(WsSubjectLookup[] wsOwnerSubjectLookups1) {
    this.wsSubjectLookups = wsOwnerSubjectLookups1;
  }
  
  /**
   * actions to query, or none to query all actions
   */
  private String[] actions; 
  
  /**
   * actions to query, or none to query all actions
   * @return actions
   */
  public String[] getActions() {
    return this.actions;
  }

  /**
   * actions to query, or none to query all actions
   * @param actions1
   */
  public void setActions(String[] actions1) {
    this.actions = actions1;
  }

  /**
   * if this is not querying assignments on assignments directly, but the assignments
   * and assignments on those assignments should be returned, enter true.  default to false.
   */
  private String includeAssignmentsOnAssignments;

  
  
  /**
   * if this is not querying assignments on assignments directly, but the assignments
   * and assignments on those assignments should be returned, enter true.  default to false.
   * @return if include assignments on assignments
   */
  public String getIncludeAssignmentsOnAssignments() {
    return this.includeAssignmentsOnAssignments;
  }

  /**
   * if this is not querying assignments on assignments directly, but the assignments
   * and assignments on those assignments should be returned, enter true.  default to false.
   * @param includeAssignmentsOnAssignments1
   */
  public void setIncludeAssignmentsOnAssignments(String includeAssignmentsOnAssignments1) {
    this.includeAssignmentsOnAssignments = includeAssignmentsOnAssignments1;
  }

  /** if acting as someone else */
  private WsSubjectLookup actAsSubjectLookup;
  
  /**
   * if acting as someone else
   * @return act as subject
   */
  public WsSubjectLookup getActAsSubjectLookup() {
    return this.actAsSubjectLookup;
  }

  /**
   * if acting as someone else
   * @param actAsSubjectLookup1
   */
  public void setActAsSubjectLookup(WsSubjectLookup actAsSubjectLookup1) {
    this.actAsSubjectLookup = actAsSubjectLookup1;
  }

  /**
   * T|F, for if the extended subject information should be
   * returned (anything more than just the id)
   */
  private String includeSubjectDetail;
  
  /**
   * T|F, for if the extended subject information should be
   * returned (anything more than just the id)
   * @return T|F
   */
  public String getIncludeSubjectDetail() {
    return this.includeSubjectDetail;
  }
  
  /**
   * T|F, for if the extended subject information should be
   * returned (anything more than just the id)
   * @param includeSubjectDetail1
   */
  public void setIncludeSubjectDetail(String includeSubjectDetail1) {
    this.includeSubjectDetail = includeSubjectDetail1;
  }

  /** are the additional subject attributes (data) to return. 
   * If blank, whatever is configured in the grouper-ws.properties will be sent
   */
  private String[] subjectAttributeNames;

  
  
  /**
   * are the additional subject attributes (data) to return.
   * If blank, whatever is configured in the grouper-ws.properties will be sent
   * @return subject attribute names
   */
  public String[] getSubjectAttributeNames() {
    return this.subjectAttributeNames;
  }

  /**
   * are the additional subject attributes (data) to return.
   * If blank, whatever is configured in the grouper-ws.properties will be sent
   * @param subjectAttributeNames1
   */
  public void setSubjectAttributeNames(String[] subjectAttributeNames1) {
    this.subjectAttributeNames = subjectAttributeNames1;
  }

  /** T or F as to if the group detail should be returned */
  private String includeGroupDetail;
  
  
  
  /**
   * T or F as to if the group detail should be returned
   * @return T|F
   */
  public String getIncludeGroupDetail() {
    return this.includeGroupDetail;
  }

  /**
   * T or F as to if the group detail should be returned
   * @param includeGroupDetail1
   */
  public void setIncludeGroupDetail(String includeGroupDetail1) {
    this.includeGroupDetail = includeGroupDetail1;
  }

  /** optional: reserved for future use */
  private  WsParam[] params;

  
  
  /**
   * optional: reserved for future use
   * @return params
   */
  public WsParam[] getParams() {
    return this.params;
  }

  /**
   * optional: reserved for future use
   * @param params1
   */
  public void setParams(WsParam[] params1) {
    this.params = params1;
  }

  /** A for all, T or null for enabled only, F for disabled  */
  private String enabled;

  /**
   * A for all, T or null for enabled only, F for disabled 
   * @return enabled
   */
  public String getEnabled() {
    return this.enabled;
  }

  /**
   * A for all, T or null for enabled only, F for disabled 
   * @param enabled1
   */
  public void setEnabled(String enabled1) {
    this.enabled = enabled1;
  }

  


}
