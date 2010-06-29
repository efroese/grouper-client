/*
 * @author mchyzer
 * $Id: GcFindStems.java,v 1.4 2009-12-19 21:38:27 mchyzer Exp $
 */
package edu.internet2.middleware.grouperClient.api;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.internet2.middleware.grouperClient.util.GrouperClientUtils;
import edu.internet2.middleware.grouperClient.ws.GrouperClientWs;
import edu.internet2.middleware.grouperClient.ws.beans.WsFindStemsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsParam;
import edu.internet2.middleware.grouperClient.ws.beans.WsRestFindStemsRequest;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemQueryFilter;
import edu.internet2.middleware.grouperClient.ws.beans.WsSubjectLookup;


/**
 * class to run find stems
 */
public class GcFindStems {

  /** query filters */
  private WsStemQueryFilter wsStemQueryFilter;

  /**
   * assign a query filter
   * @param theStemQueryFilter
   * @return this for chaining
   */
  public GcFindStems assignStemQueryFilter(WsStemQueryFilter theStemQueryFilter) {
    this.wsStemQueryFilter = theStemQueryFilter;
    return this;
  }
  
  /** client version */
  private String clientVersion;

  /**
   * assign client version
   * @param theClientVersion
   * @return this for chaining
   */
  public GcFindStems assignClientVersion(String theClientVersion) {
    this.clientVersion = theClientVersion;
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
  public GcFindStems addParam(String paramName, String paramValue) {
    this.params.add(new WsParam(paramName, paramValue));
    return this;
  }
  
  /**
   * add a param to the list
   * @param wsParam
   * @return this for chaining
   */
  public GcFindStems addParam(WsParam wsParam) {
    this.params.add(wsParam);
    return this;
  }
  
  /** act as subject if any */
  private WsSubjectLookup actAsSubject;
  /** stem names to query */
  private Set<String> stemNames = new LinkedHashSet<String>();
  /** stem names to query */
  private Set<String> stemUuids = new LinkedHashSet<String>();

  /**
   * assign the act as subject if any
   * @param theActAsSubject
   * @return this for chaining
   */
  public GcFindStems assignActAsSubject(WsSubjectLookup theActAsSubject) {
    this.actAsSubject = theActAsSubject;
    return this;
  }
  
  /**
   * validate this call
   */
  private void validate() {
    if (this.wsStemQueryFilter == null && GrouperClientUtils.length(this.stemUuids) == 0 
        && GrouperClientUtils.length(this.stemNames) == 0) {
      throw new RuntimeException("Need to pass in a stem query filter, or stemNames or stemUuids: " + this);
    }

  }
  
  /**
   * execute the call and return the results.  If there is a problem calling the service, an
   * exception will be thrown
   * 
   * @return the results
   */
  public WsFindStemsResults execute() {
    this.validate();
    WsFindStemsResults wsFindStemsResults = null;
    try {
      //Make the body of the request, in this case with beans and marshaling, but you can make
      //your request document in whatever language or way you want
      WsRestFindStemsRequest findStems = new WsRestFindStemsRequest();

      findStems.setActAsSubjectLookup(this.actAsSubject);

      findStems.setWsStemQueryFilter(this.wsStemQueryFilter);
      
      //add params if there are any
      if (this.params.size() > 0) {
        findStems.setParams(GrouperClientUtils.toArray(this.params, WsParam.class));
      }
      
      List<WsStemLookup> stemLookups = new ArrayList<WsStemLookup>();
      //add names and/or uuids
      for (String stemName : this.stemNames) {
        stemLookups.add(new WsStemLookup(stemName, null));
      }
      for (String stemUuid : this.stemUuids) {
        stemLookups.add(new WsStemLookup(null, stemUuid));
      }
      findStems.setWsStemLookups(GrouperClientUtils.toArray(stemLookups, WsStemLookup.class));

      GrouperClientWs grouperClientWs = new GrouperClientWs();
      
      //kick off the web service
      wsFindStemsResults = (WsFindStemsResults)
        grouperClientWs.executeService("stems", findStems, "findStems", this.clientVersion);
      
      String resultMessage = wsFindStemsResults.getResultMetadata().getResultMessage();
      grouperClientWs.handleFailure(wsFindStemsResults, null, resultMessage);
      
    } catch (Exception e) {
      GrouperClientUtils.convertToRuntimeException(e);
    }
    return wsFindStemsResults;
    
  }

  /**
   * set the stem name
   * @param theStemName
   * @return this for chaining
   */
  public GcFindStems addStemName(String theStemName) {
    this.stemNames.add(theStemName);
    return this;
  }

  /**
   * set the stem uuid
   * @param theStemUuid
   * @return this for chaining
   */
  public GcFindStems addStemUuid(String theStemUuid) {
    this.stemUuids.add(theStemUuid);
    return this;
  }
  
}
