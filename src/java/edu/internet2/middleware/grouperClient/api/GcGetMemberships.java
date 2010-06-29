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
import edu.internet2.middleware.grouperClient.ws.WsMemberFilter;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetMembershipsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsParam;
import edu.internet2.middleware.grouperClient.ws.beans.WsRestGetMembershipsRequest;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsSubjectLookup;


/**
 * class to run a get memberships web service call
 */
public class GcGetMemberships {

  /** A for all, T or null for enabled only, F for disabled only */
  private String enabled;
  
  /** ws subject lookups to find memberships about */
  private Set<WsSubjectLookup> wsSubjectLookups = new LinkedHashSet<WsSubjectLookup>();
  
  /** either ONE_LEVEL|ALL_IN_SUBTREE */
  private String stemScope = null;
  
  /** stem that limits the memberships either directly inside or in all descendents */
  private WsStemLookup wsStemLookup = null;
  
  /** client version */
  private String clientVersion;

  /**
   * assign client version
   * @param theClientVersion
   * @return this for chaining
   */
  public GcGetMemberships assignClientVersion(String theClientVersion) {
    this.clientVersion = theClientVersion;
    return this;
  }
  
  /**
   * assign stem to limit memberships
   * @param theWsStemLookup
   * @return this for chaining
   */
  public GcGetMemberships assignWsStem(WsStemLookup theWsStemLookup) {
    this.wsStemLookup = theWsStemLookup;
    return this;
  }
  
  /** group names to query */
  private Set<String> groupNames = new LinkedHashSet<String>();
  
  /** group names to query */
  private Set<String> groupUuids = new LinkedHashSet<String>();
  
  /**
   * set the group name
   * @param theGroupName
   * @return this for chaining
   */
  public GcGetMemberships addGroupName(String theGroupName) {
    this.groupNames.add(theGroupName);
    return this;
  }
  
  /**
   * set the subject lookup
   * @param wsSubjectLookup
   * @return this for chaining
   */
  public GcGetMemberships addWsSubjectLookup(WsSubjectLookup wsSubjectLookup) {
    this.wsSubjectLookups.add(wsSubjectLookup);
    return this;
  }
  
  /**
   * set the group uuid
   * @param theGroupUuid
   * @return this for chaining
   */
  public GcGetMemberships addGroupUuid(String theGroupUuid) {
    this.groupUuids.add(theGroupUuid);
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
  public GcGetMemberships addParam(String paramName, String paramValue) {
    this.params.add(new WsParam(paramName, paramValue));
    return this;
  }
  
  /**
   * add a param to the list
   * @param wsParam
   * @return this for chaining
   */
  public GcGetMemberships addParam(WsParam wsParam) {
    this.params.add(wsParam);
    return this;
  }
  
  /** member filter */
  private WsMemberFilter memberFilter;
  
  /**
   * assign the member filter
   * @param theMemberFilter
   * @return this for chaining
   */
  public GcGetMemberships assignMemberFilter(WsMemberFilter theMemberFilter) {
    this.memberFilter = theMemberFilter;
    return this;
  }
  
  /** act as subject if any */
  private WsSubjectLookup actAsSubject;

  /**
   * assign the act as subject if any
   * @param theActAsSubject
   * @return this for chaining
   */
  public GcGetMemberships assignActAsSubject(WsSubjectLookup theActAsSubject) {
    this.actAsSubject = theActAsSubject;
    return this;
  }
  
  /**
   * validate this call
   */
  private void validate() {
    if (GrouperClientUtils.length(this.groupNames) == 0
        && GrouperClientUtils.length(this.groupUuids) == 0
        && GrouperClientUtils.length(this.membershipIds) == 0
        && GrouperClientUtils.length(this.wsSubjectLookups) == 0) {
      throw new RuntimeException("Group name or uuid or subject lookup or membership id is required: " + this);
    }
  }
  
  /** field name to add member */
  private String fieldName;
  
  /** sql like string where percent will be added to the end, this limits the memberships */
  private String scope;
  
  /**
   * assign the field name to the request
   * @param theFieldName
   * @return this for chaining
   */
  public GcGetMemberships assignFieldName(String theFieldName) {
    this.fieldName = theFieldName;
    return this;
  }
  
  /**
   * assign the sql like string which filters the memberships
   * @param theScope
   * @return this for chaining
   */
  public GcGetMemberships assignScope(String theScope) {
    this.scope = theScope;
    return this;
  }
  
  /** if the group detail should be sent back */
  private Boolean includeGroupDetail;
  
  /** if the subject detail should be sent back */
  private Boolean includeSubjectDetail;

  /** subject attribute names to return */
  private Set<String> subjectAttributeNames = new LinkedHashSet<String>();

  /** source ids to limit the results to, or null for all sources */
  private Set<String> sourceIds = null;
  
  /** membership ids search for */
  private Set<String> membershipIds = null;
  
  /**
   * add a source id to filter by (or none for all sources)
   * @param sourceId
   * @return this for chaining
   */
  public GcGetMemberships addSourceId(String sourceId) {
    if (this.sourceIds == null) {
      this.sourceIds = new LinkedHashSet<String>();
    }
    this.sourceIds.add(sourceId);
    return this;
  }
  
  /**
   * add a source id to filter by (or none for all sources)
   * @param sourceId
   * @return this for chaining
   */
  public GcGetMemberships addMembershipId(String sourceId) {
    if (this.membershipIds == null) {
      this.membershipIds = new LinkedHashSet<String>();
    }
    this.membershipIds.add(sourceId);
    return this;
  }
  
  /**
   * 
   * @param subjectAttributeName
   * @return this for chaining
   */
  public GcGetMemberships addSubjectAttributeName(String subjectAttributeName) {
    this.subjectAttributeNames.add(subjectAttributeName);
    return this;
  }
  
  /**
   * assign if the group detail should be included
   * @param theIncludeGroupDetail
   * @return this for chaining
   */
  public GcGetMemberships assignIncludeGroupDetail(Boolean theIncludeGroupDetail) {
    this.includeGroupDetail = theIncludeGroupDetail;
    return this;
  }
  
  /**
   * if should include subject detail
   * @param theIncludeSubjectDetail
   * @return this for chaining
   */
  public GcGetMemberships assignIncludeSubjectDetail(Boolean theIncludeSubjectDetail) {
    this.includeSubjectDetail = theIncludeSubjectDetail;
    return this;
  }
  
  /**
   * execute the call and return the results.  If there is a problem calling the service, an
   * exception will be thrown
   * 
   * @return the results
   */
  public WsGetMembershipsResults execute() {
    this.validate();
    WsGetMembershipsResults wsGetMembershipsResults = null;
    try {
      //Make the body of the request, in this case with beans and marshaling, but you can make
      //your request document in whatever language or way you want
      WsRestGetMembershipsRequest getMemberships = new WsRestGetMembershipsRequest();

      getMemberships.setActAsSubjectLookup(this.actAsSubject);

      getMemberships.setEnabled(this.enabled);
      
      getMemberships.setFieldName(this.fieldName);
      
      List<WsGroupLookup> groupLookups = new ArrayList<WsGroupLookup>();
      //add names and/or uuids
      for (String groupName : this.groupNames) {
        groupLookups.add(new WsGroupLookup(groupName, null));
      }
      for (String groupUuid : this.groupUuids) {
        groupLookups.add(new WsGroupLookup(null, groupUuid));
      }
      
      if (GrouperClientUtils.length(groupLookups) > 0) {
        getMemberships.setWsGroupLookups(GrouperClientUtils.toArray(groupLookups, WsGroupLookup.class));
      }

      if (this.includeGroupDetail != null) {
        getMemberships.setIncludeGroupDetail(this.includeGroupDetail ? "T" : "F");
      }

      if (this.includeSubjectDetail != null) {
        getMemberships.setIncludeSubjectDetail(this.includeSubjectDetail ? "T" : "F");
      }
      
     
      getMemberships.setMemberFilter(this.memberFilter == null ? null : this.memberFilter.name());

      if (GrouperClientUtils.length(this.membershipIds) > 0) {
        getMemberships.setMembershipIds(GrouperClientUtils.toArray(this.membershipIds, String.class));
      }

      //add params if there are any
      if (this.params.size() > 0) {
        getMemberships.setParams(GrouperClientUtils.toArray(this.params, WsParam.class));
      }
      
      getMemberships.setScope(this.scope);
      
      if (GrouperClientUtils.length(this.sourceIds) > 0) {
        getMemberships.setSourceIds(GrouperClientUtils.toArray(this.sourceIds, String.class));
      }
      
      getMemberships.setStemScope(this.stemScope);
      
      if (this.subjectAttributeNames.size() > 0) {
        getMemberships.setSubjectAttributeNames(
            GrouperClientUtils.toArray(this.subjectAttributeNames, String.class));
      }
      
      getMemberships.setWsStemLookup(this.wsStemLookup);
      
      if (GrouperClientUtils.length(this.wsSubjectLookups) > 0) {
        getMemberships.setWsSubjectLookups(GrouperClientUtils.toArray(this.wsSubjectLookups, WsSubjectLookup.class));
      }
      
      GrouperClientWs grouperClientWs = new GrouperClientWs();
      
      //kick off the web service
      wsGetMembershipsResults = (WsGetMembershipsResults)
        grouperClientWs.executeService("memberships", getMemberships, "getMemberships", this.clientVersion);
      
      String resultMessage = wsGetMembershipsResults.getResultMetadata().getResultMessage();
      grouperClientWs.handleFailure(wsGetMembershipsResults, null, resultMessage);
      
    } catch (Exception e) {
      GrouperClientUtils.convertToRuntimeException(e);
    }
    return wsGetMembershipsResults;
    
  }

  /**
   * assign ONE_LEVEL|ALL_IN_SUBTREE to stem scope
   * @param theStemScope
   * @return this for chaining
   */
  public GcGetMemberships assigStemScope(String theStemScope) {
    this.stemScope = theStemScope;
    return this;
  }

  /**
   * assign A for all, T or null for enabled only, F for disabled only
   * @param theEnabled
   * @return this for chaining
   */
  public GcGetMemberships assignEnabled(String theEnabled) {
    this.enabled = theEnabled;
    return this;
  }
  
}
