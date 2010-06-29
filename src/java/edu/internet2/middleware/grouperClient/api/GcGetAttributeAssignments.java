/*
 * @author mchyzer
 * $Id: GcGetMemberships.java,v 1.1 2009-12-19 21:38:27 mchyzer Exp $
 */
package edu.internet2.middleware.grouperClient.api;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.internet2.middleware.grouperClient.util.GrouperClientUtils;
import edu.internet2.middleware.grouperClient.ws.GrouperClientWs;
import edu.internet2.middleware.grouperClient.ws.beans.WsAttributeAssignLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsAttributeDefLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsAttributeDefNameLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetAttributeAssignmentsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsMembershipAnyLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsMembershipLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsParam;
import edu.internet2.middleware.grouperClient.ws.beans.WsRestGetAttributeAssignmentsRequest;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsSubjectLookup;


/**
 * class to run a get attribute assignments web service call
 */
public class GcGetAttributeAssignments {

  /** A for all, T or null for enabled only, F for disabled only */
  private String enabled;
  
  /** ws subject lookups to find memberships about */
  private Set<WsSubjectLookup> ownerSubjectLookups = new LinkedHashSet<WsSubjectLookup>();
  
  /** client version */
  private String clientVersion;

  /**
   * Type of owner, from enum AttributeAssignType, e.g.
   * group, member, stem, any_mem, imm_mem, attr_def, group_asgn, mem_asgn, 
   * stem_asgn, any_mem_asgn, imm_mem_asgn, attr_def_asgn  
   */
  private String attributeAssignType;
  
  /**
   * Type of owner, from enum AttributeAssignType, e.g.
   * group, member, stem, any_mem, imm_mem, attr_def, group_asgn, mem_asgn, 
   * stem_asgn, any_mem_asgn, imm_mem_asgn, attr_def_asgn  
   * @param theAttributeAssignType
   * @return this for chaining
   */
  public GcGetAttributeAssignments assignAttributeAssignType(String theAttributeAssignType) {
    this.attributeAssignType = theAttributeAssignType;
    return this;
  }
  
//  * @param wsAttributeDefLookups find assignments in these attribute defs (optional)
//  * @param wsAttributeDefNameLookups find assignments in these attribute def names (optional)

  
  

  /**
   * if this is not querying assignments on assignments directly, but the assignments
   * and assignments on those assignments should be returned, enter true.  default to false.   
   */
  private Boolean includeAssignmentsOnAssignments;
  
  /**
   * 
   * @param theIncludeAssignmentsOnAssignments
   * @return this for chaining
   */
  public GcGetAttributeAssignments assignIncludeAssignmentsOnAssignments(Boolean theIncludeAssignmentsOnAssignments) {
    this.includeAssignmentsOnAssignments = theIncludeAssignmentsOnAssignments;
    return this;
  }
  
  /** to query, or none to query all actions */
  private Set<String> actions = new LinkedHashSet<String>();
  
  /**
   * 
   * @param action
   * @return this for chaining
   */
  public GcGetAttributeAssignments addAction(String action) {
    this.actions.add(action);
    return this;
  }
  
  /**
   * assign client version
   * @param theClientVersion
   * @return this for chaining
   */
  public GcGetAttributeAssignments assignClientVersion(String theClientVersion) {
    this.clientVersion = theClientVersion;
    return this;
  }
  
  /** group names to query */
  private Set<String> ownerGroupNames = new LinkedHashSet<String>();
  
  /** group uuids to query */
  private Set<String> ownerGroupUuids = new LinkedHashSet<String>();
  
  /**
   * set the group name
   * @param theGroupName
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerGroupName(String theGroupName) {
    this.ownerGroupNames.add(theGroupName);
    return this;
  }
  
  /**
   * set the subject lookup
   * @param wsSubjectLookup
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerSubjectLookup(WsSubjectLookup wsSubjectLookup) {
    this.ownerSubjectLookups.add(wsSubjectLookup);
    return this;
  }
  
  /**
   * set the group uuid
   * @param theGroupUuid
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerGroupUuid(String theGroupUuid) {
    this.ownerGroupUuids.add(theGroupUuid);
    return this;
  }
  
  /** params */
  private List<WsParam> params = new ArrayList<WsParam>();

  /**
   * add a param to the list
   * @param paramName
   * @param paramValue
   * @return this for chaining
   */
  public GcGetAttributeAssignments addParam(String paramName, String paramValue) {
    this.params.add(new WsParam(paramName, paramValue));
    return this;
  }
  
  /**
   * add a param to the list
   * @param wsParam
   * @return this for chaining
   */
  public GcGetAttributeAssignments addParam(WsParam wsParam) {
    this.params.add(wsParam);
    return this;
  }
  
  /** act as subject if any */
  private WsSubjectLookup actAsSubject;

  /**
   * assign the act as subject if any
   * @param theActAsSubject
   * @return this for chaining
   */
  public GcGetAttributeAssignments assignActAsSubject(WsSubjectLookup theActAsSubject) {
    this.actAsSubject = theActAsSubject;
    return this;
  }
  
  /**
   * validate this call
   */
  private void validate() {
    if (GrouperClientUtils.isBlank(this.attributeAssignType)) {
      throw new RuntimeException("attributeAssignType is required: " + this);
    }
  }
  
  /** if the group detail should be sent back */
  private Boolean includeGroupDetail;
  
  /** if the subject detail should be sent back */
  private Boolean includeSubjectDetail;

  /** subject attribute names to return */
  private Set<String> subjectAttributeNames = new LinkedHashSet<String>();

  /** stem names to query */
  private Set<String> ownerStemNames = new LinkedHashSet<String>();

  /** stem uuids to query */
  private Set<String> ownerStemUuids = new LinkedHashSet<String>();

  /** attribute def names to query */
  private Set<String> ownerAttributeDefNames = new LinkedHashSet<String>();

  /** attribute def uuids to query */
  private Set<String> ownerAttributeDefUuids = new LinkedHashSet<String>();

  /** owner membership any lookup */
  private Set<WsMembershipAnyLookup> ownerMembershipAnyLookups = new LinkedHashSet<WsMembershipAnyLookup>();
  
  /** owner membership lookup */
  private Set<WsMembershipLookup> ownerMembershipLookups = new LinkedHashSet<WsMembershipLookup>();
  
  /** owner membership lookup */
  private Set<WsAttributeAssignLookup> attributeAssignLookups = new LinkedHashSet<WsAttributeAssignLookup>();

  /** attributeDef names to query */
  private Set<String> attributeDefNames = new LinkedHashSet<String>();

  /** attributeDef uuids to query */
  private Set<String> attributeDefUuids = new LinkedHashSet<String>();

  /** attributeDefName names to query */
  private Set<String> attributeDefNameNames = new LinkedHashSet<String>();

  /** attributeDefName uuids to query */
  private Set<String> attributeDefNameUuids = new LinkedHashSet<String>();
  
  
  
  /**
   * add a membership any lookup
   * @param wsMembershipAnyLookup
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerMembershipAnyLookup(WsMembershipAnyLookup wsMembershipAnyLookup) {
    this.ownerMembershipAnyLookups.add(wsMembershipAnyLookup);
    return this;
  }
  
  /**
   * add a membership id lookup for owner
   * @param membershipId id (uuid or immediate)
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerMembershipId(String membershipId) {
    WsMembershipLookup wsMembershipLookup = new WsMembershipLookup();
    wsMembershipLookup.setUuid(membershipId);
    this.ownerMembershipLookups.add(wsMembershipLookup);
    return this;
  }
  
  /**
   * add a attribute assign id lookup
   * @param attributeAssignId id
   * @return this for chaining
   */
  public GcGetAttributeAssignments addAttributeAssignId(String attributeAssignId) {
    WsAttributeAssignLookup wsAttributeAssignLookup = new WsAttributeAssignLookup();
    wsAttributeAssignLookup.setUuid(attributeAssignId);
    this.attributeAssignLookups.add(wsAttributeAssignLookup);
    return this;
  }
  
  /**
   * 
   * @param subjectAttributeName
   * @return this for chaining
   */
  public GcGetAttributeAssignments addSubjectAttributeName(String subjectAttributeName) {
    this.subjectAttributeNames.add(subjectAttributeName);
    return this;
  }
  
  /**
   * assign if the group detail should be included
   * @param theIncludeGroupDetail
   * @return this for chaining
   */
  public GcGetAttributeAssignments assignIncludeGroupDetail(Boolean theIncludeGroupDetail) {
    this.includeGroupDetail = theIncludeGroupDetail;
    return this;
  }
  
  /**
   * if should include subject detail
   * @param theIncludeSubjectDetail
   * @return this for chaining
   */
  public GcGetAttributeAssignments assignIncludeSubjectDetail(Boolean theIncludeSubjectDetail) {
    this.includeSubjectDetail = theIncludeSubjectDetail;
    return this;
  }
  
  /**
   * execute the call and return the results.  If there is a problem calling the service, an
   * exception will be thrown
   * 
   * @return the results
   */
  public WsGetAttributeAssignmentsResults execute() {
    this.validate();
    WsGetAttributeAssignmentsResults wsGetAttributeAssignmentsResults = null;
    try {
      //Make the body of the request, in this case with beans and marshaling, but you can make
      //your request document in whatever language or way you want
      WsRestGetAttributeAssignmentsRequest getAttributeAssignments = new WsRestGetAttributeAssignmentsRequest();

      getAttributeAssignments.setActAsSubjectLookup(this.actAsSubject);

      getAttributeAssignments.setEnabled(this.enabled);
      
      //########### ATTRIBUTE DEFS
      List<WsAttributeDefLookup> attributeDefLookups = new ArrayList<WsAttributeDefLookup>();
      //add names and/or uuids
      for (String attributeDefName : this.attributeDefNames) {
        attributeDefLookups.add(new WsAttributeDefLookup(attributeDefName, null));
      }
      for (String attributeDefUuid : this.attributeDefUuids) {
        attributeDefLookups.add(new WsAttributeDefLookup(null, attributeDefUuid));
      }
      if (GrouperClientUtils.length(attributeDefLookups) > 0) {
        getAttributeAssignments.setWsAttributeDefLookups(GrouperClientUtils.toArray(attributeDefLookups, WsAttributeDefLookup.class));
      }

      //########### ATTRIBUTE DEF NAMES
      List<WsAttributeDefNameLookup> attributeDefNameLookups = new ArrayList<WsAttributeDefNameLookup>();
      //add names and/or uuids
      for (String attributeDefNameName : this.attributeDefNameNames) {
        attributeDefNameLookups.add(new WsAttributeDefNameLookup(attributeDefNameName, null));
      }
      for (String attributeDefNameUuid : this.attributeDefNameUuids) {
        attributeDefNameLookups.add(new WsAttributeDefNameLookup(null, attributeDefNameUuid));
      }
      if (GrouperClientUtils.length(attributeDefNameLookups) > 0) {
        getAttributeAssignments.setWsAttributeDefNameLookups(GrouperClientUtils.toArray(attributeDefNameLookups, WsAttributeDefNameLookup.class));
      }

      //########### GROUPS
      List<WsGroupLookup> ownerGroupLookups = new ArrayList<WsGroupLookup>();
      //add names and/or uuids
      for (String ownerGroupName : this.ownerGroupNames) {
        ownerGroupLookups.add(new WsGroupLookup(ownerGroupName, null));
      }
      for (String ownerGroupUuid : this.ownerGroupUuids) {
        ownerGroupLookups.add(new WsGroupLookup(null, ownerGroupUuid));
      }
      if (GrouperClientUtils.length(ownerGroupLookups) > 0) {
        getAttributeAssignments.setWsOwnerGroupLookups(GrouperClientUtils.toArray(ownerGroupLookups, WsGroupLookup.class));
      }

      //############# STEMS
      List<WsStemLookup> ownerStemLookups = new ArrayList<WsStemLookup>();
      //add names and/or uuids
      for (String ownerStemName : this.ownerStemNames) {
        ownerStemLookups.add(new WsStemLookup(ownerStemName, null));
      }
      for (String ownerStemUuid : this.ownerStemUuids) {
        ownerStemLookups.add(new WsStemLookup(null, ownerStemUuid));
      }
      if (GrouperClientUtils.length(ownerStemLookups) > 0) {
        getAttributeAssignments.setWsOwnerStemLookups(GrouperClientUtils.toArray(ownerStemLookups, WsStemLookup.class));
      }

      //############# SUBJECTS
      if (GrouperClientUtils.length(this.ownerSubjectLookups) > 0) {
        getAttributeAssignments.setWsOwnerSubjectLookups(GrouperClientUtils.toArray(this.ownerSubjectLookups, WsSubjectLookup.class));
      }
      
      //############# MEMBERSHIP ANY LOOKUPS
      if (GrouperClientUtils.length(this.ownerMembershipAnyLookups) > 0) {
        getAttributeAssignments.setWsOwnerMembershipAnyLookups(GrouperClientUtils.toArray(this.ownerMembershipAnyLookups, WsMembershipAnyLookup.class));
      }
      
      //############# MEMBERSHIPS
      if (GrouperClientUtils.length(this.ownerMembershipLookups) > 0) {
        getAttributeAssignments.setWsOwnerMembershipLookups(GrouperClientUtils.toArray(this.ownerMembershipLookups, WsMembershipLookup.class));
      }
      
      //############# ATTRIBUTE DEFS
      List<WsAttributeDefLookup> ownerAttributeDefLookups = new ArrayList<WsAttributeDefLookup>();
      //add names and/or uuids
      for (String ownerAttributeDefName : this.ownerAttributeDefNames) {
        ownerAttributeDefLookups.add(new WsAttributeDefLookup(ownerAttributeDefName, null));
      }
      for (String ownerAttributeDefUuid : this.ownerAttributeDefUuids) {
        ownerAttributeDefLookups.add(new WsAttributeDefLookup(null, ownerAttributeDefUuid));
      }
      if (GrouperClientUtils.length(ownerAttributeDefLookups) > 0) {
        getAttributeAssignments.setWsOwnerAttributeDefLookups(GrouperClientUtils.toArray(ownerAttributeDefLookups, WsAttributeDefLookup.class));
      }
      
      if (this.includeAssignmentsOnAssignments != null) {
        getAttributeAssignments.setIncludeAssignmentsOnAssignments(this.includeAssignmentsOnAssignments ? "T" : "F");
        
      }
      
      if (this.includeGroupDetail != null) {
        getAttributeAssignments.setIncludeGroupDetail(this.includeGroupDetail ? "T" : "F");
      }

      if (this.includeSubjectDetail != null) {
        getAttributeAssignments.setIncludeSubjectDetail(this.includeSubjectDetail ? "T" : "F");
      }
      
      if (GrouperClientUtils.length(this.attributeAssignLookups) > 0) {
        getAttributeAssignments.setWsAttributeAssignLookups(GrouperClientUtils.toArray(
            this.attributeAssignLookups, WsAttributeAssignLookup.class));
      }

      
      getAttributeAssignments.setAttributeAssignType(this.attributeAssignType);
      
      //add params if there are any
      if (this.params.size() > 0) {
        getAttributeAssignments.setParams(GrouperClientUtils.toArray(this.params, WsParam.class));
      }
      
      if (this.subjectAttributeNames.size() > 0) {
        getAttributeAssignments.setSubjectAttributeNames(
            GrouperClientUtils.toArray(this.subjectAttributeNames, String.class));
      }
      
      if (GrouperClientUtils.length(this.actions) > 0) {
        getAttributeAssignments.setActions(GrouperClientUtils.toArray(this.actions, String.class));
      }
      
      GrouperClientWs grouperClientWs = new GrouperClientWs();
      
      //kick off the web service
      wsGetAttributeAssignmentsResults = (WsGetAttributeAssignmentsResults)
        grouperClientWs.executeService("attributeAssignments", getAttributeAssignments, "getAttributeAssignments", this.clientVersion);
      
      String resultMessage = wsGetAttributeAssignmentsResults.getResultMetadata().getResultMessage();
      grouperClientWs.handleFailure(wsGetAttributeAssignmentsResults, null, resultMessage);
      
    } catch (Exception e) {
      GrouperClientUtils.convertToRuntimeException(e);
    }
    return wsGetAttributeAssignmentsResults;
    
  }

  /**
   * assign A for all, T or null for enabled only, F for disabled only
   * @param theEnabled
   * @return this for chaining
   */
  public GcGetAttributeAssignments assignEnabled(String theEnabled) {
    this.enabled = theEnabled;
    return this;
  }

  /**
   * set the stem name
   * @param theStemName
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerStemName(String theStemName) {
    this.ownerStemNames.add(theStemName);
    return this;
  }

  /**
   * set the stem uuid
   * @param theStemUuid
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerStemUuid(String theStemUuid) {
    this.ownerStemUuids.add(theStemUuid);
    return this;
  }

  /**
   * set the attribute def name
   * @param theAttributeDefName
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerAttributeDefName(String theAttributeDefName) {
    this.ownerAttributeDefNames.add(theAttributeDefName);
    return this;
  }

  /**
   * set the attribute def uuid
   * @param theAttributeDefUuid
   * @return this for chaining
   */
  public GcGetAttributeAssignments addOwnerAttributeDefUuid(String theAttributeDefUuid) {
    this.ownerAttributeDefUuids.add(theAttributeDefUuid);
    return this;
  }

  /**
   * set the attributedef name
   * @param theAttributeDefName
   * @return this for chaining
   */
  public GcGetAttributeAssignments addAttributeDefName(String theAttributeDefName) {
    this.attributeDefNames.add(theAttributeDefName);
    return this;
  }

  /**
   * set the attributedef uuid
   * @param theAttributeDefUuid
   * @return this for chaining
   */
  public GcGetAttributeAssignments addAttributeDefUuid(String theAttributeDefUuid) {
    this.attributeDefUuids.add(theAttributeDefUuid);
    return this;
  }

  /**
   * set the attributeDefName name
   * @param theAttributeDefNameName
   * @return this for chaining
   */
  public GcGetAttributeAssignments addAttributeDefNameName(String theAttributeDefNameName) {
    this.attributeDefNameNames.add(theAttributeDefNameName);
    return this;
  }

  /**
   * set the attributeDefName uuid
   * @param theAttributeDefNameUuid
   * @return this for chaining
   */
  public GcGetAttributeAssignments addAttributeDefNameUuid(String theAttributeDefNameUuid) {
    this.attributeDefNameUuids.add(theAttributeDefNameUuid);
    return this;
  }
  
}
