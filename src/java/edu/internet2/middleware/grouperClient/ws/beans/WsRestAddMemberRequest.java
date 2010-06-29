/*
 * @author mchyzer
 * $Id: WsRestAddMemberRequest.java,v 1.3 2008-12-02 06:21:09 mchyzer Exp $
 */
package edu.internet2.middleware.grouperClient.ws.beans;

/**
 * bean that will be the data from rest request
 */
public class WsRestAddMemberRequest implements WsRequestBean {
  
  /** field */
  private String clientVersion;
  
  /** field */
  private WsGroupLookup wsGroupLookup;
  
  /** subjects to assign to */
  private WsSubjectLookup[] subjectLookups;
  
  /** field */
  private String replaceAllExisting;
  
  /** who to act as if not the connecting user */
  private WsSubjectLookup actAsSubjectLookup;
  
  /** field */
  private String fieldName;
  
  /** field */
  private String txType;
  
  /** field */
  private String includeGroupDetail;
  
  /** field */
  private String includeSubjectDetail;
  
  /** attribute names to return */
  private String[] subjectAttributeNames;

  /** field */
  private WsParam[] params;
  
  
  /**  date this membership will be disabled, yyyy/MM/dd HH:mm:ss.SSS */
  private String disabledTime;
  
  /**  date this membership will be enabled, yyyy/MM/dd HH:mm:ss.SSS */
  private String enabledTime;
  
  /**
   * date this membership will be disabled, yyyy/MM/dd HH:mm:ss.SSS
   * @return disabled time
   */
  public String getDisabledTime() {
    return this.disabledTime;
  }


  /**
   * date this membership will be disabled, yyyy/MM/dd HH:mm:ss.SSS
   * @param disabledTime1
   */
  public void setDisabledTime(String disabledTime1) {
    this.disabledTime = disabledTime1;
  }


  /**
   * date this membership will be enabled, yyyy/MM/dd HH:mm:ss.SSS
   * @return date
   */
  public String getEnabledTime() {
    return this.enabledTime;
  }


  /**
   * date this membership will be enabled, yyyy/MM/dd HH:mm:ss.SSS
   * @param enabledTime1
   */
  public void setEnabledTime(String enabledTime1) {
    this.enabledTime = enabledTime1;
  }


  /**
   * @return the clientVersion
   */
  public String getClientVersion() {
    return this.clientVersion;
  }

  
  /**
   * @param clientVersion1 the clientVersion to set
   */
  public void setClientVersion(String clientVersion1) {
    this.clientVersion = clientVersion1;
  }

  
  /**
   * @return the wsGroupLookup
   */
  public WsGroupLookup getWsGroupLookup() {
    return this.wsGroupLookup;
  }

  
  /**
   * @param wsGroupLookup1 the wsGroupLookup to set
   */
  public void setWsGroupLookup(WsGroupLookup wsGroupLookup1) {
    this.wsGroupLookup = wsGroupLookup1;
  }

  
  /**
   * @return the subjectLookups
   */
  public WsSubjectLookup[] getSubjectLookups() {
    return this.subjectLookups;
  }

  
  /**
   * @param subjectLookups1 the subjectLookups to set
   */
  public void setSubjectLookups(WsSubjectLookup[] subjectLookups1) {
    this.subjectLookups = subjectLookups1;
  }

  
  /**
   * @return the replaceAllExisting
   */
  public String getReplaceAllExisting() {
    return this.replaceAllExisting;
  }

  
  /**
   * @param replaceAllExisting1 the replaceAllExisting to set
   */
  public void setReplaceAllExisting(String replaceAllExisting1) {
    this.replaceAllExisting = replaceAllExisting1;
  }

  
  /**
   * @return the actAsSubjectLookup
   */
  public WsSubjectLookup getActAsSubjectLookup() {
    return this.actAsSubjectLookup;
  }

  
  /**
   * @param actAsSubjectLookup1 the actAsSubjectLookup to set
   */
  public void setActAsSubjectLookup(WsSubjectLookup actAsSubjectLookup1) {
    this.actAsSubjectLookup = actAsSubjectLookup1;
  }

  
  /**
   * @return the fieldName
   */
  public String getFieldName() {
    return this.fieldName;
  }

  
  /**
   * @param fieldName1 the fieldName to set
   */
  public void setFieldName(String fieldName1) {
    this.fieldName = fieldName1;
  }

  
  /**
   * @return the txType
   */
  public String getTxType() {
    return this.txType;
  }

  
  /**
   * @param txType1 the txType to set
   */
  public void setTxType(String txType1) {
    this.txType = txType1;
  }

  
  /**
   * @return the includeGroupDetail
   */
  public String getIncludeGroupDetail() {
    return this.includeGroupDetail;
  }

  
  /**
   * who to act as if not the connecting user
   * @param includeGroupDetail1 the includeGroupDetail to set
   */
  public void setIncludeGroupDetail(String includeGroupDetail1) {
    this.includeGroupDetail = includeGroupDetail1;
  }

  
  /**
   * @return the includeSubjectDetail
   */
  public String getIncludeSubjectDetail() {
    return this.includeSubjectDetail;
  }

  
  /**
   * @param includeSubjectDetail1 the includeSubjectDetail to set
   */
  public void setIncludeSubjectDetail(String includeSubjectDetail1) {
    this.includeSubjectDetail = includeSubjectDetail1;
  }

  
  /**
   * @return the subjectAttributeNames
   */
  public String[] getSubjectAttributeNames() {
    return this.subjectAttributeNames;
  }

  
  /**
   * @param subjectAttributeNames1 the subjectAttributeNames to set
   */
  public void setSubjectAttributeNames(String[] subjectAttributeNames1) {
    this.subjectAttributeNames = subjectAttributeNames1;
  }


  
  /**
   * @return the params
   */
  public WsParam[] getParams() {
    return this.params;
  }


  
  /**
   * @param params1 the params to set
   */
  public void setParams(WsParam[] params1) {
    this.params = params1;
  }

}
