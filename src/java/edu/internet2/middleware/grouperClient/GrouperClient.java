/*
 * @author mchyzer
 * $Id: GrouperClient.java,v 1.29 2009-12-30 04:23:02 mchyzer Exp $
 */
package edu.internet2.middleware.grouperClient;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.internet2.middleware.grouperClient.api.GcAddMember;
import edu.internet2.middleware.grouperClient.api.GcAssignAttributes;
import edu.internet2.middleware.grouperClient.api.GcAssignGrouperPrivileges;
import edu.internet2.middleware.grouperClient.api.GcAssignGrouperPrivilegesLite;
import edu.internet2.middleware.grouperClient.api.GcAssignPermissions;
import edu.internet2.middleware.grouperClient.api.GcDeleteMember;
import edu.internet2.middleware.grouperClient.api.GcFindGroups;
import edu.internet2.middleware.grouperClient.api.GcFindStems;
import edu.internet2.middleware.grouperClient.api.GcGetAttributeAssignments;
import edu.internet2.middleware.grouperClient.api.GcGetGrouperPrivilegesLite;
import edu.internet2.middleware.grouperClient.api.GcGetGroups;
import edu.internet2.middleware.grouperClient.api.GcGetMembers;
import edu.internet2.middleware.grouperClient.api.GcGetMemberships;
import edu.internet2.middleware.grouperClient.api.GcGetPermissionAssignments;
import edu.internet2.middleware.grouperClient.api.GcGetSubjects;
import edu.internet2.middleware.grouperClient.api.GcGroupDelete;
import edu.internet2.middleware.grouperClient.api.GcGroupSave;
import edu.internet2.middleware.grouperClient.api.GcHasMember;
import edu.internet2.middleware.grouperClient.api.GcLdapSearchAttribute;
import edu.internet2.middleware.grouperClient.api.GcMemberChangeSubject;
import edu.internet2.middleware.grouperClient.api.GcStemDelete;
import edu.internet2.middleware.grouperClient.api.GcStemSave;
import edu.internet2.middleware.grouperClient.collections.MultiKey;
import edu.internet2.middleware.grouperClient.commandLine.GcLdapSearchAttributeConfig;
import edu.internet2.middleware.grouperClient.commandLine.GcLdapSearchAttributeConfig.SearchAttributeResultType;
import edu.internet2.middleware.grouperClient.util.GrouperClientCommonUtils;
import edu.internet2.middleware.grouperClient.util.GrouperClientLog;
import edu.internet2.middleware.grouperClient.util.GrouperClientUtils;
import edu.internet2.middleware.grouperClient.ws.GcTransactionType;
import edu.internet2.middleware.grouperClient.ws.GrouperClientWs;
import edu.internet2.middleware.grouperClient.ws.StemScope;
import edu.internet2.middleware.grouperClient.ws.WsMemberFilter;
import edu.internet2.middleware.grouperClient.ws.beans.WsAddMemberResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsAddMemberResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsAssignAttributeResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsAssignAttributesResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsAssignGrouperPrivilegesLiteResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsAssignGrouperPrivilegesResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsAssignGrouperPrivilegesResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsAssignPermissionResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsAssignPermissionsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsAttributeAssign;
import edu.internet2.middleware.grouperClient.ws.beans.WsAttributeAssignLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsAttributeAssignValue;
import edu.internet2.middleware.grouperClient.ws.beans.WsAttributeDef;
import edu.internet2.middleware.grouperClient.ws.beans.WsAttributeDefName;
import edu.internet2.middleware.grouperClient.ws.beans.WsDeleteMemberResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsDeleteMemberResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsFindGroupsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsFindStemsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetAttributeAssignmentsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGrouperPrivilegesLiteResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGroupsResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetGroupsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetMembersResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetMembersResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetMembershipsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetPermissionAssignmentsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGetSubjectsResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroup;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupDeleteResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupDeleteResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupDetail;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupSaveResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupSaveResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsGroupToSave;
import edu.internet2.middleware.grouperClient.ws.beans.WsGrouperPrivilegeResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsHasMemberResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsHasMemberResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsMemberChangeSubjectResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsMemberChangeSubjectResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsMembership;
import edu.internet2.middleware.grouperClient.ws.beans.WsMembershipAnyLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsParam;
import edu.internet2.middleware.grouperClient.ws.beans.WsPermissionAssign;
import edu.internet2.middleware.grouperClient.ws.beans.WsQueryFilter;
import edu.internet2.middleware.grouperClient.ws.beans.WsStem;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemDeleteResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemDeleteResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemLookup;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemQueryFilter;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemSaveResult;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemSaveResults;
import edu.internet2.middleware.grouperClient.ws.beans.WsStemToSave;
import edu.internet2.middleware.grouperClient.ws.beans.WsSubject;
import edu.internet2.middleware.grouperClient.ws.beans.WsSubjectLookup;
import edu.internet2.middleware.grouperClientExt.edu.internet2.middleware.morphString.Crypto;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.logging.Log;


/**
 * main class for grouper client.  note, stdout is for output, stderr is for error messages (or logs)
 */
public class GrouperClient {

  /** timing gate */
  private static long startTime = System.currentTimeMillis();
  
  /**
   * 
   */
  static Log log = GrouperClientUtils.retrieveLog(GrouperClient.class);

  /** ldap operations from config file */
  private static Map<String, GcLdapSearchAttributeConfig> ldapOperations = null;

  /** custom operations from config file */
  private static Map<String, Class<ClientOperation>> customOperations = null;

  /**
   * get custom operation classes configured in the grouper.client.properties
   * @return the map of operations
   */
  @SuppressWarnings({ "unchecked", "cast" })
  private static Map<String, Class<ClientOperation>> customOperations() {
    
    if (customOperations == null) {
      
      customOperations = new LinkedHashMap<String, Class<ClientOperation>>();
      
      int i=0;
      String operationName = null;
      while (true) {
        operationName = null;
        operationName = GrouperClientUtils.propertiesValue("customOperation.name." + i, false);
        if (GrouperClientUtils.isBlank(operationName)) {
          break;
        }
        if (customOperations.containsKey(operationName)) {
          throw new RuntimeException("There is an ldap operation defined twice in grouper.client.properties: '" + operationName + "'");
        }
        try {

          String operationClassName = GrouperClientUtils.propertiesValue("customOperation.class." + i, true);
          Class<ClientOperation> operationClass = (Class<ClientOperation>)GrouperClientUtils.forName(operationClassName);
          customOperations.put(operationName, operationClass);

        } catch (RuntimeException re) {
          throw new RuntimeException("Problem with custom operation: " + operationName, re);
        }
        i++;
      }
    }
    
    return customOperations;
    
  }
  
  /**
   * lazy load the ldap operations
   * @return the ldap operations
   */
  private static Map<String, GcLdapSearchAttributeConfig> ldapOperations() {
    
    //lazy load if null
    if (ldapOperations == null) {
      ldapOperations = new LinkedHashMap<String, GcLdapSearchAttributeConfig>();
      
      int i=0;
      while (true) {
        String operationName = GrouperClientUtils.propertiesValue("ldapSearchAttribute.operationName." + i, false);
        if (GrouperClientUtils.isBlank(operationName)) {
          break;
        }
        if (ldapOperations.containsKey(operationName)) {
          throw new RuntimeException("There is an ldap operation defined twice in grouper.client.properties: '" + operationName + "'");
        }
        
        GcLdapSearchAttributeConfig gcLdapSearchAttributeConfig = new GcLdapSearchAttributeConfig();
        gcLdapSearchAttributeConfig.setOperationName(operationName);
        
        //ldapSearchAttribute.ldapName.2 = ou=groups
        gcLdapSearchAttributeConfig.setLdapName(GrouperClientUtils.propertiesValue("ldapSearchAttribute.ldapName." + i, true));

        {
          //ldapSearchAttribute.matchingAttributes.2 = pennid
          //ldapSearchAttribute.matchingAttributeLabels.2 = pennid
          String[] matchingAttributeLabels = GrouperClientUtils.splitTrim(
              GrouperClientUtils.propertiesValue("ldapSearchAttribute.matchingAttributeLabels." + i, true), ",");
          String[] ldapMatchingAttributes = GrouperClientUtils.splitTrim(
              GrouperClientUtils.propertiesValue("ldapSearchAttribute.matchingAttributes." + i, true), ",");
          
          if (matchingAttributeLabels.length != ldapMatchingAttributes.length) {
            throw new RuntimeException("ldapSearchAttribute #" + i + " operation: " + operationName
                + " should have the same number of matchingAttributeLabels " 
                + matchingAttributeLabels.length + " and matchingAttributes " + ldapMatchingAttributes.length);
          }
              
          for (int j=0;j<matchingAttributeLabels.length;j++) {
            gcLdapSearchAttributeConfig.addMatchingAttribute(matchingAttributeLabels[j], ldapMatchingAttributes[j]);
          }
        }
        
        {
          //ldapSearchAttribute.returningAttributes.2 = pennname
          String[] ldapReturningAttributes = GrouperClientUtils.splitTrim(
              GrouperClientUtils.propertiesValue("ldapSearchAttribute.returningAttributes." + i, true), ",");
          
          for (int j=0;j<ldapReturningAttributes.length;j++) {
            gcLdapSearchAttributeConfig.addReturningAttribute(ldapReturningAttributes[j]);
          }
        }
        

        //ldapSearchAttribute.outputTemplate.0 = pennid: ${pennid}
        gcLdapSearchAttributeConfig.setOutputTemplate(GrouperClientUtils.propertiesValue("ldapSearchAttribute.outputTemplate." + i, true));

        //ldapSearchAttribute.resultType.2 = string
        gcLdapSearchAttributeConfig.setSearchAttributeResultType(GrouperClientUtils.propertiesValue("ldapSearchAttribute.resultType." + i, true));

        
        ldapOperations.put(operationName, gcLdapSearchAttributeConfig);
        i++;
      }
      
    }
    return ldapOperations;
  }
  
  /** should java exit on error? */
  public static boolean exitOnError = true;
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    
    String operation = null;
    try {
      if (GrouperClientUtils.length(args) == 0) {
        usage();
        return;
      }
      
      //map of all command line args
      Map<String, String> argMap = GrouperClientUtils.argMap(args);
      
      Map<String, String> argMapNotUsed = new LinkedHashMap<String, String>(argMap);

      boolean debugMode = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "debug", false, false);
      
      GrouperClientLog.assignDebugToConsole(debugMode);
      
      //init if not already
      GrouperClientUtils.grouperClientProperties();
      
      //see where log file came from
      StringBuilder callingLog = new StringBuilder();
      GrouperClientUtils.propertiesFromResourceName("grouper.client.properties", 
          false, true, GrouperClientCommonUtils.class, callingLog);
      
      //see if the message about where it came from is
      //log.debug(callingLog.toString());
      
      operation = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "operation", true);
      
      //where results should go if file
      String saveResultsToFile = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "saveResultsToFile", false);
      boolean shouldSaveResultsToFile = !GrouperClientUtils.isBlank(saveResultsToFile);
      
      if (shouldSaveResultsToFile) {
        log.debug("Will save results to file: " + GrouperClientUtils.fileCanonicalPath(new File(saveResultsToFile)));
      }
      
      String result = null;
      
      if (customOperations().containsKey(operation)) {
        
        Class<ClientOperation> operationClass = customOperations().get(operation);
        ClientOperation clientOperation = GrouperClientUtils.newInstance(operationClass);
        
        OperationParams operationParams = new OperationParams();
        operationParams.setArgMap(argMap);
        operationParams.setArgMapNotUsed(argMapNotUsed);
        operationParams.setShouldSaveResultsToFile(shouldSaveResultsToFile);
        
        result = clientOperation.operate(operationParams);
        
      } else if (GrouperClientUtils.equals(operation, "encryptPassword")) {
        
        result = encryptText(argMap, argMapNotUsed, shouldSaveResultsToFile);
        
      } else if (ldapOperations().containsKey(operation)) {
        
        result = ldapSearchAttribute(argMap, argMapNotUsed, operation);
      } else if (GrouperClientUtils.equals(operation, "addMemberWs")) {
        result = addMember(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "deleteMemberWs")) {
        result = deleteMember(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "getMembersWs")) {
        result = getMembers(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "getMembershipsWs")) {
        result = getMemberships(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "getAttributeAssignmentsWs")) {
        result = getAttributeAssignments(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "getPermissionAssignmentsWs")) {
        result = getPermissionAssignments(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "assignAttributesWs")) {
        result = assignAttributes(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "assignPermissionsWs")) {
        result = assignPermissions(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "getSubjectsWs")) {
        result = getSubjects(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "hasMemberWs")) {
        result = hasMember(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "getGroupsWs")) {
        result = getGroups(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "groupSaveWs")) {
        result = groupSave(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "stemSaveWs")) {
        result = stemSave(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "groupDeleteWs")) {
        result = groupDelete(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "stemDeleteWs")) {
        result = stemDelete(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "getGrouperPrivilegesLiteWs")) {
        result = getGrouperPrivilegesLite(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "assignGrouperPrivilegesLiteWs")) {
        result = assignGrouperPrivilegesLite(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "assignGrouperPrivilegesWs")) {
        result = assignGrouperPrivileges(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "findGroupsWs")) {
        result = findGroups(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "findStemsWs")) {
        result = findStems(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "memberChangeSubjectWs")) {
        result = memberChangeSubject(argMap, argMapNotUsed);

      } else if (GrouperClientUtils.equals(operation, "sendFile")) {
        result = sendFile(argMap, argMapNotUsed);

      } else {
        System.err.println("Error: invalid operation: '" + operation + "', for usage help, run: java -jar grouperClient.jar" );
        if (exitOnError) {
          System.exit(1);
        }
        throw new RuntimeException("Invalid usage");
      }
      
      //this already has a newline on it
      if (shouldSaveResultsToFile) {
        GrouperClientUtils.saveStringIntoFile(new File(saveResultsToFile), result);
      } else {
        System.out.print(result);
      }

      failOnArgsNotUsed(argMapNotUsed);
      
    } catch (Exception e) {
      System.err.println("Error with grouper client, check the logs: " + e.getMessage());
      log.fatal(e.getMessage(), e);
      if (exitOnError) {
        System.exit(1);
      }
      if (e instanceof RuntimeException) {
        throw (RuntimeException)e;
      }
      throw new RuntimeException(e.getMessage(), e);
    } finally {
      try {
        log.debug("Elapsed time: " + (System.currentTimeMillis() - startTime) + "ms");
      } catch (Exception e) {}
      GrouperClientLog.assignDebugToConsole(false);
    }
    
  }

  /**
   * @param argMapNotUsed
   */
  private static void failOnArgsNotUsed(Map<String, String> argMapNotUsed) {
    if (argMapNotUsed.size() > 0) {
      boolean failOnExtraParams = GrouperClientUtils.propertiesValueBoolean(
          "grouperClient.failOnExtraCommandLineArgs", true, true);
      String error = "Invalid command line arguments: " + argMapNotUsed.keySet();
      if (failOnExtraParams) {
        throw new RuntimeException(error);
      }
      log.error(error);
    }
  }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @param shouldSaveResultsToFile
   * @return result
   */
  private static String encryptText(Map<String, String> argMap,
      Map<String, String> argMapNotUsed,
      boolean shouldSaveResultsToFile) {
    boolean dontMask = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "dontMask", false, false);
    
    String encryptKey = GrouperClientUtils.propertiesValue("encrypt.key", true);
    
    boolean disableExternalFileLookup = GrouperClientUtils.propertiesValueBoolean(
        "encrypt.disableExternalFileLookup", false, true);
    
    //lets lookup if file
    encryptKey = GrouperClientUtils.readFromFileIfFile(encryptKey, disableExternalFileLookup);
    
    //lets get the password from stdin
    String password = GrouperClientUtils.retrievePasswordFromStdin(dontMask, 
        "Type the string to encrypt (note: pasting might echo it back): ");
    
    String encrypted = new Crypto(encryptKey).encrypt(password);
    
    if (shouldSaveResultsToFile) {
      return encrypted;
    }
    return "Encrypted password: " + encrypted;
  }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @param operation
   * @return the output
   */
  public static String ldapSearchAttribute(Map<String, String> argMap,
      Map<String, String> argMapNotUsed, String operation) {
    //ldap operation
    GcLdapSearchAttributeConfig gcLdapSearchAttributeConfig = ldapOperations().get(operation);
    
    GcLdapSearchAttribute gcLdapSearchAttribute = new GcLdapSearchAttribute();
    gcLdapSearchAttribute.assignLdapName(gcLdapSearchAttributeConfig.getLdapName());
    
    //go through the matching attributes and get from command line
    for (String matchingAttributeLabel : gcLdapSearchAttributeConfig.getMatchingAttributes().keySet()) {
      String matchingAttributeValue = GrouperClientUtils.argMapString(argMap, argMapNotUsed, matchingAttributeLabel, true);
      gcLdapSearchAttribute.addMatchingAttribute(
          gcLdapSearchAttributeConfig.getMatchingAttributes().get(matchingAttributeLabel), matchingAttributeValue);
    }
    
    //go through the returning attributes and assign to query
    for (String ldapAttribute : gcLdapSearchAttributeConfig.getReturningAttributes()) {
      gcLdapSearchAttribute.addReturningAttribute(ldapAttribute);
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    failOnArgsNotUsed(argMapNotUsed);
    
    gcLdapSearchAttribute.execute();
    
    SearchAttributeResultType searchAttributeResultType = gcLdapSearchAttributeConfig.getSearchAttributeResultTypeEnum();
    
    log.debug("LDAP search attribute result type: " + searchAttributeResultType);
    
    String outputTemplate = null;
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = gcLdapSearchAttributeConfig.getOutputTemplate();
    }
    
    log.debug("Output template: " + outputTemplate);

    
    String results = searchAttributeResultType.processOutput(gcLdapSearchAttributeConfig, gcLdapSearchAttribute, outputTemplate);
    
    return results;
  }

  /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String addMember(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
      String groupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupName", false);
      String groupUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupUuid", false);

      String fieldName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fieldName", false);
      String txType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "txType", false);
     
      Boolean replaceAllExisting = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "replaceAllExisting");
  
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      
      Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
      Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      GcAddMember gcAddMember = new GcAddMember();        
  
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcAddMember.assignClientVersion(clientVersion);
  
      for (WsParam param : params) {
        gcAddMember.addParam(param);
      }
      
      List<WsSubjectLookup> wsSubjectLookupList = retrieveSubjectsFromArgs(argMap,
          argMapNotUsed, true);
      
      for (WsSubjectLookup wsSubjectLookup : wsSubjectLookupList) {
        gcAddMember.addSubjectLookup(wsSubjectLookup);
      }
      
      gcAddMember.assignGroupName(groupName);
      gcAddMember.assignGroupUuid(groupUuid);
      
      {
        Timestamp disabledTime = GrouperClientUtils.argMapTimestamp(argMap, argMapNotUsed, "disabledTime");
        gcAddMember.assignDisabledTime(disabledTime);
      }

      {
        Timestamp enabledTime = GrouperClientUtils.argMapTimestamp(argMap, argMapNotUsed, "enabledTime");
        gcAddMember.assignEnabledTime(enabledTime);
      }
      
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      
      gcAddMember.assignActAsSubject(actAsSubject);
      
      gcAddMember.assignReplaceAllExisting(replaceAllExisting);
      gcAddMember.assignIncludeGroupDetail(includeGroupDetail);
      gcAddMember.assignIncludeSubjectDetail(includeSubjectDetail);
      
      gcAddMember.assignFieldName(fieldName);
      gcAddMember.assignTxType(GcTransactionType.valueOfIgnoreCase(txType));
  
      for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
        gcAddMember.addSubjectAttributeName(subjectAttribute);
      }
      
      //register that we will use this
      GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      failOnArgsNotUsed(argMapNotUsed);
  
      WsAddMemberResults wsAddMemberResults = gcAddMember.execute();
      
      StringBuilder result = new StringBuilder();
      int index = 0;
      
      Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
      substituteMap.put("wsAddMemberResults", wsAddMemberResults);
      substituteMap.put("grouperClientUtils", new GrouperClientUtils());
      substituteMap.put("wsGroupAssigned", wsAddMemberResults.getWsGroupAssigned());
  
      String outputTemplate = null;
  
      if (argMap.containsKey("outputTemplate")) {
        outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
        outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
      } else {
        outputTemplate = GrouperClientUtils.propertiesValue("webService.addMember.output", true);
      }
      log.debug("Output template: " + outputTemplate + ", available variables: wsAddMemberResults, " +
        "grouperClientUtils, wsGroupAssigned, index, wsAddMemberResult, wsSubject, resultMetadata");

      for (WsAddMemberResult wsAddMemberResult : wsAddMemberResults.getResults()) {
        
        substituteMap.put("index", index);
        substituteMap.put("wsAddMemberResult", wsAddMemberResult);
        substituteMap.put("wsSubject", wsAddMemberResult.getWsSubject());
        substituteMap.put("resultMetadata", wsAddMemberResult.getResultMetadata());

  //          result.append("Index " + index + ": success: " + wsAddMemberResult.getResultMetadata().getSuccess()
  //              + ": code: " + wsAddMemberResult.getResultMetadata().getResultCode() + ": " 
  //              + wsAddMemberResult.getWsSubject().getId() + "\n");
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
      
      return result.toString();
    }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String memberChangeSubject(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
    
    Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
    Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    GcMemberChangeSubject gcMemberChangeSubject = new GcMemberChangeSubject();        
  
    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
    gcMemberChangeSubject.assignClientVersion(clientVersion);
  
    for (WsParam param : params) {
      gcMemberChangeSubject.addParam(param);
    }
    
    Boolean deleteOldMember = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "deleteOldMember");
    if (deleteOldMember != null) {
      gcMemberChangeSubject.assignDeleteOldMember(deleteOldMember);
    }
    
    WsSubjectLookup oldSubjectLookup = retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "old", true);
    WsSubjectLookup newSubjectLookup = retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "new", true);
  
    gcMemberChangeSubject.assignOldSubjectLookup(oldSubjectLookup);
    gcMemberChangeSubject.assignNewSubjectLookup(newSubjectLookup);
    
    
    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    gcMemberChangeSubject.assignActAsSubject(actAsSubject);
    
    gcMemberChangeSubject.assignIncludeSubjectDetail(includeSubjectDetail);
    
    for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
      gcMemberChangeSubject.addSubjectAttributeName(subjectAttribute);
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    failOnArgsNotUsed(argMapNotUsed);
  
    WsMemberChangeSubjectResults wsMemberChangeSubjectResults = gcMemberChangeSubject.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsMemberChangeSubjectResults", wsMemberChangeSubjectResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.memberChangeSubject.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsMemberChangeSubjectResults, " +
      "grouperClientUtils, index, resultMetadata, wsMemberChangeSubjectResult, wsSubjectOld, wsSubjectNew");
  
    //there will only be one result, but loop anyways
    for (WsMemberChangeSubjectResult wsMemberChangeSubjectResult : wsMemberChangeSubjectResults.getResults()) {
      
      substituteMap.put("index", index);
      substituteMap.put("resultMetadata", wsMemberChangeSubjectResult.getResultMetadata());
      substituteMap.put("wsMemberChangeSubjectResult", wsMemberChangeSubjectResult);
      substituteMap.put("wsSubjectOld", wsMemberChangeSubjectResult.getWsSubjectOld());
      substituteMap.put("wsSubjectNew", wsMemberChangeSubjectResult.getWsSubjectNew());
  
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }
    
    return result.toString();
  }

  /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String sendFile(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      
      String fileContents = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fileContents", false);
      
      String theFileName = "[contents on command line]";
      if (GrouperClientUtils.isBlank(fileContents)) {
        String fileName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fileName", true);

        fileContents = GrouperClientUtils.readFileIntoString(new File(fileName));
        
        theFileName = GrouperClientUtils.fileCanonicalPath(new File(fileName));
      }
      
      if (fileContents.startsWith("POST") || fileContents.startsWith("GET")
          || fileContents.startsWith("PUT") || fileContents.startsWith("DELETE")
          || fileContents.startsWith("Connection:")) {
        throw new RuntimeException("The file is detected as containing HTTP headers, it should only contain the payload (e.g. the XML): " + theFileName);
      }
      
      String urlSuffix = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "urlSuffix", true);

      //this is part of the log file if logging output
      String labelForLog = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "labelForLog", false);
      
      labelForLog = GrouperClientUtils.defaultIfBlank(labelForLog, "sendFile");
      
      boolean indentOutput = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "indentOutput", false, true);
      
      String contentType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "contentType", false);
      
      failOnArgsNotUsed(argMapNotUsed);
      
      GrouperClientWs grouperClientWs = new GrouperClientWs();
      
      if (GrouperClientUtils.isNotBlank(contentType)) {
        grouperClientWs.assignContentType(contentType);
      }
      
      try {
        //assume the url suffix is already escaped...
        String results = (String)grouperClientWs.executeService(urlSuffix, fileContents, labelForLog, clientVersion);

        if (indentOutput) {
          results = GrouperClientUtils.indent(results, false);
        }
        
        return results;
        
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    /**
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String getGrouperPrivilegesLite(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
    
    String groupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupName", false);
    String stemName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemName", false);
    String privilegeType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "privilegeType", false);
    String privilegeName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "privilegeName", false);
  
    Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
    
    Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
    Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    GcGetGrouperPrivilegesLite gcGetGrouperPrivilegesLite = new GcGetGrouperPrivilegesLite();        
  
    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
    gcGetGrouperPrivilegesLite.assignClientVersion(clientVersion);
  
    for (WsParam param : params) {
      gcGetGrouperPrivilegesLite.addParam(param);
    }
    
    WsSubjectLookup wsSubjectLookup = retrieveSubjectFromArgs(argMap,
        argMapNotUsed);
    
    gcGetGrouperPrivilegesLite.assignStemName(stemName);
    gcGetGrouperPrivilegesLite.assignPrivilegeType(privilegeType);
    gcGetGrouperPrivilegesLite.assignPrivilegeName(privilegeName);
    
    gcGetGrouperPrivilegesLite.assignSubjectLookup(wsSubjectLookup);
    
    gcGetGrouperPrivilegesLite.assignGroupName(groupName);
    
    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    gcGetGrouperPrivilegesLite.assignActAsSubject(actAsSubject);
    
    gcGetGrouperPrivilegesLite.assignIncludeGroupDetail(includeGroupDetail);
    gcGetGrouperPrivilegesLite.assignIncludeSubjectDetail(includeSubjectDetail);
    
  
    for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
      gcGetGrouperPrivilegesLite.addSubjectAttributeName(subjectAttribute);
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    failOnArgsNotUsed(argMapNotUsed);
  
    WsGetGrouperPrivilegesLiteResult wsGetGrouperPrivilegesLiteResult = gcGetGrouperPrivilegesLite.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsGetGrouperPrivilegesLiteResult", wsGetGrouperPrivilegesLiteResult);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
    substituteMap.put("resultMetadata", wsGetGrouperPrivilegesLiteResult.getResultMetadata());

  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.getGrouperPrivilegesLite.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsGetGrouperPrivilegesLiteResult, " +
      "grouperClientUtils, resultMetadata, index, wsGrouperPrivilegeResult, wsSubject, wsGroup, wsStem, objectType, objectName");
  
    for (WsGrouperPrivilegeResult wsGrouperPrivilegeResult : GrouperClientUtils.nonNull( 
        wsGetGrouperPrivilegesLiteResult.getPrivilegeResults(), WsGrouperPrivilegeResult.class)) {
      
      substituteMap.put("index", index);
      substituteMap.put("wsGrouperPrivilegeResult", wsGrouperPrivilegeResult);
      substituteMap.put("wsSubject", wsGrouperPrivilegeResult.getWsSubject());
      substituteMap.put("wsGroup", wsGrouperPrivilegeResult.getWsGroup());
      substituteMap.put("wsStem", wsGrouperPrivilegeResult.getWsStem());
      substituteMap.put("objectType", wsGrouperPrivilegeResult.getWsStem() == null ? "group" : "stem");
      substituteMap.put("objectName", wsGrouperPrivilegeResult.getWsStem() == null 
          ? wsGrouperPrivilegeResult.getWsGroup().getName() : wsGrouperPrivilegeResult.getWsStem().getName());
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }
    
    return result.toString();
  }

    /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String assignGrouperPrivilegesLite(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
      String groupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupName", false);
      String stemName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemName", false);
      String privilegeType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "privilegeType", false);
      String privilegeName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "privilegeName", true);
     
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      
      boolean allowed = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "allowed", true, true);
      
      Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
      Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      GcAssignGrouperPrivilegesLite gcAssignGrouperPrivilegesLite = new GcAssignGrouperPrivilegesLite();        
  
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcAssignGrouperPrivilegesLite.assignClientVersion(clientVersion);

      for (WsParam param : params) {
        gcAssignGrouperPrivilegesLite.addParam(param);
      }
      
      WsSubjectLookup wsSubjectLookup = retrieveSubjectFromArgs(argMap,
          argMapNotUsed);
      
      gcAssignGrouperPrivilegesLite.assignStemName(stemName);
      gcAssignGrouperPrivilegesLite.assignPrivilegeType(privilegeType);
      gcAssignGrouperPrivilegesLite.assignPrivilegeName(privilegeName);
      
      gcAssignGrouperPrivilegesLite.assignSubjectLookup(wsSubjectLookup);
      
      gcAssignGrouperPrivilegesLite.assignAllowed(allowed);
      
      gcAssignGrouperPrivilegesLite.assignGroupName(groupName);
      
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      
      gcAssignGrouperPrivilegesLite.assignActAsSubject(actAsSubject);
      
      gcAssignGrouperPrivilegesLite.assignIncludeGroupDetail(includeGroupDetail);
      gcAssignGrouperPrivilegesLite.assignIncludeSubjectDetail(includeSubjectDetail);
      
  
      for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
        gcAssignGrouperPrivilegesLite.addSubjectAttributeName(subjectAttribute);
      }
      
      //register that we will use this
      GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      failOnArgsNotUsed(argMapNotUsed);

      WsAssignGrouperPrivilegesLiteResult wsAssignGrouperPrivilegesLiteResult = gcAssignGrouperPrivilegesLite.execute();
      
      StringBuilder result = new StringBuilder();
      
      Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
      substituteMap.put("wsAssignGrouperPrivilegesLiteResult", wsAssignGrouperPrivilegesLiteResult);
      substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
      String outputTemplate = null;
  
      if (argMap.containsKey("outputTemplate")) {
        outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
        outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
      } else {
        outputTemplate = GrouperClientUtils.propertiesValue("webService.assignGrouperPrivilegesLite.output", true);
      }
      log.debug("Output template: " + outputTemplate + ", available variables: wsAssignGrouperPrivilegesLiteResult, " +
        "grouperClientUtils, wsSubject, resultMetadata, wsGroup, wsStem, objectType, objectName");

      substituteMap.put("wsSubject", wsAssignGrouperPrivilegesLiteResult.getWsSubject());
      substituteMap.put("resultMetadata", wsAssignGrouperPrivilegesLiteResult.getResultMetadata());
      substituteMap.put("wsGroup", wsAssignGrouperPrivilegesLiteResult.getWsGroup());
      substituteMap.put("wsStem", wsAssignGrouperPrivilegesLiteResult.getWsStem());
      substituteMap.put("objectType", wsAssignGrouperPrivilegesLiteResult.getWsStem() == null ? "group" : "stem");
      substituteMap.put("objectName", wsAssignGrouperPrivilegesLiteResult.getWsStem() == null 
          ? wsAssignGrouperPrivilegesLiteResult.getWsGroup().getName() : wsAssignGrouperPrivilegesLiteResult.getWsStem().getName());
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      return result.toString();
    }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String groupDelete(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
    
    List<String> groupNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "groupNames", true);
    String txType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "txType", false);
  
    Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
    
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    GcGroupDelete gcGroupDelete = new GcGroupDelete();        
  
    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
    gcGroupDelete.assignClientVersion(clientVersion);
  
    for (WsParam param : params) {
      gcGroupDelete.addParam(param);
    }
  
    for (String groupName : groupNames) {
      WsGroupLookup wsGroupLookup = new WsGroupLookup(groupName, null);
      gcGroupDelete.addGroupLookup(wsGroupLookup);
    }
    
    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    gcGroupDelete.assignActAsSubject(actAsSubject);
    
    gcGroupDelete.assignIncludeGroupDetail(includeGroupDetail);
    
    gcGroupDelete.assignTxType(GcTransactionType.valueOfIgnoreCase(txType));
  
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    failOnArgsNotUsed(argMapNotUsed);

    WsGroupDeleteResults wsGroupDeleteResults = gcGroupDelete.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsGroupDeleteResults", wsGroupDeleteResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.groupDelete.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsGroupDeleteResults, " +
      "grouperClientUtils, index, wsGroupDeleteResult, resultMetadata, wsGroup");

    for (WsGroupDeleteResult wsGroupDeleteResult : wsGroupDeleteResults.getResults()) {
      
      substituteMap.put("index", index);
      substituteMap.put("wsGroupDeleteResult", wsGroupDeleteResult);
      substituteMap.put("resultMetadata", wsGroupDeleteResult.getResultMetadata());
      substituteMap.put("wsGroup", wsGroupDeleteResult.getWsGroup());
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }
    
    return result.toString();
  }

  /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String stemDelete(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
      List<String> stemNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "stemNames", true);
      String txType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "txType", false);
     
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      GcStemDelete gcStemDelete = new GcStemDelete();        
  
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcStemDelete.assignClientVersion(clientVersion);

      for (WsParam param : params) {
        gcStemDelete.addParam(param);
      }

      for (String stemName : stemNames) {
        WsStemLookup wsStemLookup = new WsStemLookup(stemName, null);
        gcStemDelete.addGroupLookup(wsStemLookup);
      }
      
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      
      gcStemDelete.assignActAsSubject(actAsSubject);
      
      gcStemDelete.assignTxType(GcTransactionType.valueOfIgnoreCase(txType));
  
      //register that we will use this
      GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      failOnArgsNotUsed(argMapNotUsed);

      WsStemDeleteResults wsStemDeleteResults = gcStemDelete.execute();
      
      StringBuilder result = new StringBuilder();
      int index = 0;
      
      Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
      substituteMap.put("wsStemDeleteResults", wsStemDeleteResults);
      substituteMap.put("grouperClientUtils", new GrouperClientUtils());

      String outputTemplate = null;
  
      if (argMap.containsKey("outputTemplate")) {
        outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
        outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
      } else {
        outputTemplate = GrouperClientUtils.propertiesValue("webService.stemDelete.output", true);
      }
      
      log.debug("Output template: " + outputTemplate + ", available variables: wsStemDeleteResults, " +
        "grouperClientUtils, resultMetadata, index, wsStemDeleteResult, wsStem");
      
      for (WsStemDeleteResult wsStemDeleteResult : wsStemDeleteResults.getResults()) {

        substituteMap.put("resultMetadata", wsStemDeleteResult.getResultMetadata());
        substituteMap.put("index", index);
        substituteMap.put("wsStemDeleteResult", wsStemDeleteResult);
        substituteMap.put("wsStem", wsStemDeleteResult.getWsStem());

        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
      
      return result.toString();
    }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String groupSave(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
    
    String txType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "txType", false);
     
    Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
    
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    GcGroupSave gcGroupSave = new GcGroupSave();        
  
    for (WsParam param : params) {
      gcGroupSave.addParam(param);
    }
    
    WsGroupToSave wsGroupToSave = new WsGroupToSave();
    gcGroupSave.addGroupToSave(wsGroupToSave);
    WsGroup wsGroup = new WsGroup();
    wsGroupToSave.setWsGroup(wsGroup);
  
    String groupLookupName = GrouperClientUtils.argMapString(argMap, 
        argMapNotUsed, "groupLookupName", false);
    String groupLookupUuid = GrouperClientUtils.argMapString(argMap, 
        argMapNotUsed, "groupLookupUuid", false);
    
    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
    gcGroupSave.assignClientVersion(clientVersion);
    
    String name = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "name", true);
    wsGroup.setName(name);
    
    WsGroupLookup wsGroupLookup = new WsGroupLookup();
    wsGroupToSave.setWsGroupLookup(wsGroupLookup);
    //do the lookup if an edit
    if (!GrouperClientUtils.isBlank(groupLookupName) || !GrouperClientUtils.isBlank(groupLookupUuid)) {
      if (!GrouperClientUtils.isBlank(groupLookupName)) {
        wsGroupLookup.setGroupName(groupLookupName);
      }
      if (!GrouperClientUtils.isBlank(groupLookupUuid)) {
        wsGroupLookup.setUuid(groupLookupUuid);
      }
    } else {
      //just edit the name passed in
      wsGroupLookup.setGroupName(name);
    }
    
    //createParentStemsIfNotExist
    String createParentStemsIfNotExist = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "createParentStemsIfNotExist", false);
    wsGroupToSave.setCreateParentStemsIfNotExist(createParentStemsIfNotExist);
    
    //save mode
    String saveMode = GrouperClientUtils.argMapString(argMap, 
        argMapNotUsed, "saveMode", false);
    if (saveMode != null) {
      wsGroupToSave.setSaveMode(saveMode);
    }
    
    String description = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "description", false);
    if (!GrouperClientUtils.isBlank(description)) {
      wsGroup.setDescription(description);
    }
    
    String displayExtension = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "displayExtension", false);
  
    //just default to the id
    if (GrouperClientUtils.isBlank(displayExtension)) {
      displayExtension = GrouperClientUtils.substringAfterLast(name, ":");
    }
    
    wsGroup.setDisplayExtension(displayExtension);
  
    boolean hasAttribute0 = GrouperClientUtils.isNotBlank(GrouperClientUtils.argMapString(
        argMap, argMapNotUsed, "attributeName0", false));
    boolean hasGroupDetailParamName0 = GrouperClientUtils.isNotBlank(GrouperClientUtils.argMapString(
        argMap, argMapNotUsed, "groupDetailParamName0", false));
    boolean hasCompositeType = GrouperClientUtils.isNotBlank(GrouperClientUtils.argMapString(
        argMap, argMapNotUsed, "compositeType", false));
    boolean hasLeftGroupName = GrouperClientUtils.isNotBlank(GrouperClientUtils.argMapString(
        argMap, argMapNotUsed, "leftGroupName", false));
    boolean hasRightGroupName = GrouperClientUtils.isNotBlank(GrouperClientUtils.argMapString(
        argMap, argMapNotUsed, "rightGroupName", false));
    boolean hasTypeNames = GrouperClientUtils.isNotBlank(GrouperClientUtils.argMapString(
        argMap, argMapNotUsed, "typeNames", false));
    
    if (hasAttribute0 || hasGroupDetailParamName0 || hasCompositeType 
        || hasLeftGroupName || hasRightGroupName || hasTypeNames) {
      
      WsGroupDetail wsGroupDetail = new WsGroupDetail();
      wsGroup.setDetail(wsGroupDetail);
  
      //attributes
      if (hasAttribute0) {
        int i=0;
        List<String> attributeNameList = new ArrayList<String>();
        List<String> attributeValueList = new ArrayList<String>();
        while (true) {
          String attributeName = GrouperClientUtils.argMapString(
              argMap, argMapNotUsed, "attributeName" + i, false);
          if (GrouperClientUtils.isBlank(attributeName)) {
            break;
          }
          String attributeValue = GrouperClientUtils.argMapString(
              argMap, argMapNotUsed, "attributeValue" + i, true);
          attributeNameList.add(attributeName);
          attributeValueList.add(attributeValue);
          i++;
        }
        wsGroupDetail.setAttributeNames(GrouperClientUtils.toArray(attributeNameList, String.class));
        wsGroupDetail.setAttributeValues(GrouperClientUtils.toArray(attributeValueList, String.class));
      }
      
      //params
      if (hasGroupDetailParamName0) {
        int i=0;
        List<WsParam> paramList = new ArrayList<WsParam>();
        while (true) {
          String paramName = GrouperClientUtils.argMapString(
              argMap, argMapNotUsed, "groupDetailParamName" + i, false);
          if (GrouperClientUtils.isBlank(paramName)) {
            break;
          }
          String paramValue = GrouperClientUtils.argMapString(
              argMap, argMapNotUsed, "groupDetailParamValue" + i, true);
          paramList.add(new WsParam(paramName, paramValue));
          i++;
        }
        wsGroupDetail.setParams(GrouperClientUtils.toArray(paramList, WsParam.class));
      }
  
      if (hasCompositeType) {
        wsGroupDetail.setHasComposite("T");
        String compositeType = GrouperClientUtils.argMapString(
            argMap, argMapNotUsed, "compositeType", true);
        wsGroupDetail.setCompositeType(compositeType);
        {
          String leftGroupName = GrouperClientUtils.argMapString(
              argMap, argMapNotUsed, "leftGroupName", true);
          WsGroup leftGroup = new WsGroup();
          leftGroup.setName(leftGroupName);
          wsGroupDetail.setLeftGroup(leftGroup);
        }
        {
          String rightGroupName = GrouperClientUtils.argMapString(
              argMap, argMapNotUsed, "rightGroupName", true);
          WsGroup rightGroup = new WsGroup();
          rightGroup.setName(rightGroupName);
          wsGroupDetail.setRightGroup(rightGroup);
        }
      }
  
      if (hasTypeNames) {
        List<String> typeNamesList = GrouperClientUtils.argMapList(
            argMap, argMapNotUsed, "typeNames", true);
        String[] typeNamesArray = GrouperClientUtils.toArray(typeNamesList, String.class);
        wsGroupDetail.setTypeNames(typeNamesArray);
      }
      
    }
    
    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    gcGroupSave.assignActAsSubject(actAsSubject);
    
    gcGroupSave.assignIncludeGroupDetail(includeGroupDetail);
    
    gcGroupSave.assignTxType(GcTransactionType.valueOfIgnoreCase(txType));
  
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    failOnArgsNotUsed(argMapNotUsed);

    WsGroupSaveResults wsGroupSaveResults = gcGroupSave.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsGroupSaveResults", wsGroupSaveResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());

    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.groupSave.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsGroupSaveResults, " +
      "grouperClientUtils, index, wsGroupSaveResult, resultMetadata");

    //there is one result...  but loop anyways
    for (WsGroupSaveResult wsGroupSaveResult : wsGroupSaveResults.getResults()) {
      
      substituteMap.put("index", index);
      substituteMap.put("wsGroupSaveResult", wsGroupSaveResult);
      substituteMap.put("resultMetadata", wsGroupSaveResult.getResultMetadata());
      wsGroupSaveResult.getWsGroup();
      substituteMap.put("wsGroup", wsGroup);
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }
    
    return result.toString();
  }

  /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String stemSave(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
      String txType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "txType", false);
       
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      GcStemSave gcStemSave = new GcStemSave();        
  
      for (WsParam param : params) {
        gcStemSave.addParam(param);
      }
      
      WsStemToSave wsStemToSave = new WsStemToSave();
      gcStemSave.addStemToSave(wsStemToSave);
      WsStem wsStem = new WsStem();
      wsStemToSave.setWsStem(wsStem);

      String stemLookupName = GrouperClientUtils.argMapString(argMap, 
          argMapNotUsed, "stemLookupName", false);
      String stemLookupUuid = GrouperClientUtils.argMapString(argMap, 
          argMapNotUsed, "stemLookupUuid", false);
      
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcStemSave.assignClientVersion(clientVersion);
      
      String name = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "name", true);
      wsStem.setName(name);
      
      WsStemLookup wsStemLookup = new WsStemLookup();
      wsStemToSave.setWsStemLookup(wsStemLookup);
      //do the lookup if an edit
      if (!GrouperClientUtils.isBlank(stemLookupName) || !GrouperClientUtils.isBlank(stemLookupUuid)) {
        if (!GrouperClientUtils.isBlank(stemLookupName)) {
          wsStemLookup.setStemName(stemLookupName);
        }
        if (!GrouperClientUtils.isBlank(stemLookupUuid)) {
          wsStemLookup.setUuid(stemLookupUuid);
        }
      } else {
        //just edit the name passed in
        wsStemLookup.setStemName(name);
      }
      
      //createParentStemsIfNotExist
      String createParentStemsIfNotExist = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "createParentStemsIfNotExist", false);
      wsStemToSave.setCreateParentStemsIfNotExist(createParentStemsIfNotExist);
      
      //save mode
      String saveMode = GrouperClientUtils.argMapString(argMap, 
          argMapNotUsed, "saveMode", false);
      if (saveMode != null) {
        wsStemToSave.setSaveMode(saveMode);
      }
      
      String description = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "description", false);
      if (!GrouperClientUtils.isBlank(description)) {
        wsStem.setDescription(description);
      }
      
      String displayExtension = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
          "displayExtension", false);

      //just default to the id
      if (GrouperClientUtils.isBlank(displayExtension)) {
        displayExtension = GrouperClientUtils.substringAfterLast(name, ":");
      }
      
      wsStem.setDisplayExtension(displayExtension);

      
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      
      gcStemSave.assignActAsSubject(actAsSubject);
      
      gcStemSave.assignTxType(GcTransactionType.valueOfIgnoreCase(txType));
  
      //register that we will use this
      GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      failOnArgsNotUsed(argMapNotUsed);

      WsStemSaveResults wsStemSaveResults = gcStemSave.execute();
      
      StringBuilder result = new StringBuilder();
      int index = 0;
      
      Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
      substituteMap.put("wsStemSaveResults", wsStemSaveResults);
      substituteMap.put("grouperClientUtils", new GrouperClientUtils());
      
      String outputTemplate = null;
  
      if (argMap.containsKey("outputTemplate")) {
        outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
        outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
      } else {
        outputTemplate = GrouperClientUtils.propertiesValue("webService.stemSave.output", true);
      }
      log.debug("Output template: " + outputTemplate + ", available variables: wsStemSaveResults, " +
        "grouperClientUtils, index, wsStemSaveResult, wsStem, resultMetadata");

      //there is one result...  but loop anyways
      for (WsStemSaveResult wsStemSaveResult : wsStemSaveResults.getResults()) {
        
        substituteMap.put("index", index);
        substituteMap.put("wsStemSaveResult", wsStemSaveResult);
        substituteMap.put("wsStem", wsStemSaveResult.getWsStem());
        substituteMap.put("resultMetadata", wsStemSaveResult.getResultMetadata());
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
      
      return result.toString();
    }

  /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String hasMember(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
      String groupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupName", false);
      String groupUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupUuid", false);
      String fieldName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fieldName", false);
     
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      
      Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
      String memberFilter = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "memberFilter", false);

      Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      GcHasMember gcHasMember = new GcHasMember();        
      
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcHasMember.assignClientVersion(clientVersion);

      for (WsParam param : params) {
        gcHasMember.addParam(param);
      }
      
      List<WsSubjectLookup> wsSubjectLookupList = retrieveSubjectsFromArgs(argMap,
          argMapNotUsed, true);
      
      for (WsSubjectLookup wsSubjectLookup : wsSubjectLookupList) {
        gcHasMember.addSubjectLookup(wsSubjectLookup);
      }
      
      gcHasMember.assignGroupName(groupName);
      gcHasMember.assignGroupUuid(groupUuid);
      
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      
      gcHasMember.assignActAsSubject(actAsSubject);
      
      gcHasMember.assignMemberFilter(WsMemberFilter.valueOfIgnoreCase(memberFilter));

      gcHasMember.assignIncludeGroupDetail(includeGroupDetail);
      gcHasMember.assignIncludeSubjectDetail(includeSubjectDetail);
      
      gcHasMember.assignFieldName(fieldName);

      
      for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
        gcHasMember.addSubjectAttributeName(subjectAttribute);
      }
      
      //register that we will use this
      GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      failOnArgsNotUsed(argMapNotUsed);

      WsHasMemberResults wsHasMemberResults = gcHasMember.execute();
      
      StringBuilder result = new StringBuilder();
      int index = 0;
      
      Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
      substituteMap.put("wsHasMemberResults", wsHasMemberResults);
      substituteMap.put("grouperClientUtils", new GrouperClientUtils());
      substituteMap.put("wsGroup", wsHasMemberResults.getWsGroup());

      String outputTemplate = null;
  
      if (argMap.containsKey("outputTemplate")) {
        outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
        outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
      } else {
        outputTemplate = GrouperClientUtils.propertiesValue("webService.hasMember.output", true);
      }
      log.debug("Output template: " + outputTemplate + ", available variables: wsHasMemberResults, " +
        "grouperClientUtils, index, wsGroup, wsHasMemberResult, wsSubject, resultMetadata, hasMember");

      for (WsHasMemberResult wsHasMemberResult : wsHasMemberResults.getResults()) {
        
        substituteMap.put("index", index);
        substituteMap.put("wsHasMemberResult", wsHasMemberResult);
        substituteMap.put("wsSubject", wsHasMemberResult.getWsSubject());
        String resultCode = wsHasMemberResult.getResultMetadata().getResultCode();
        substituteMap.put("hasMember", GrouperClientUtils.equals("IS_MEMBER", resultCode));
        substituteMap.put("resultMetadata", wsHasMemberResult.getResultMetadata());
//            result.append("Index " + index + ": success: " + wsHasMemberResult.getResultMetadata().getSuccess()
//                + ": code: " + resultCode + ": " 
//                + wsHasMemberResult.getWsSubject().getId() + "\n");
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
      
      return result.toString();
    }

  /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String deleteMember(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
      String groupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupName", false);
      String groupUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupUuid", false);
      String fieldName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fieldName", false);
      String txType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "txType", false);
     
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      
      Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
      Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      GcDeleteMember gcDeleteMember = new GcDeleteMember();        
  
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcDeleteMember.assignClientVersion(clientVersion);
  
      for (WsParam param : params) {
        gcDeleteMember.addParam(param);
      }
      
      List<WsSubjectLookup> wsSubjectLookupList = retrieveSubjectsFromArgs(argMap,
          argMapNotUsed, true);
      
      for (WsSubjectLookup wsSubjectLookup : wsSubjectLookupList) {
        gcDeleteMember.addSubjectLookup(wsSubjectLookup);
      }
      
      gcDeleteMember.assignGroupName(groupName);
      gcDeleteMember.assignGroupUuid(groupUuid);
      
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      
      gcDeleteMember.assignActAsSubject(actAsSubject);
      
      gcDeleteMember.assignIncludeGroupDetail(includeGroupDetail);
      gcDeleteMember.assignIncludeSubjectDetail(includeSubjectDetail);
      
      gcDeleteMember.assignFieldName(fieldName);
      gcDeleteMember.assignTxType(GcTransactionType.valueOfIgnoreCase(txType));
  
      for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
        gcDeleteMember.addSubjectAttributeName(subjectAttribute);
      }
      
      //register that we will use this
      GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      failOnArgsNotUsed(argMapNotUsed);
  
      WsDeleteMemberResults wsDeleteMemberResults = gcDeleteMember.execute();
      
      StringBuilder result = new StringBuilder();
      int index = 0;
      
      Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
      substituteMap.put("wsDeleteMemberResults", wsDeleteMemberResults);
      substituteMap.put("grouperClientUtils", new GrouperClientUtils());
      substituteMap.put("wsGroup", wsDeleteMemberResults.getWsGroup());
      
      String outputTemplate = null;
  
      if (argMap.containsKey("outputTemplate")) {
        outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
        outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
      } else {
        outputTemplate = GrouperClientUtils.propertiesValue("webService.deleteMember.output", true);
      }
      log.debug("Output template: " + outputTemplate + ", available variables: wsDeleteMemberResults, " +
      		"grouperClientUtils, wsGroup, index, wsDeleteMemberResult, wsSubject, resultMetadata");
  
      for (WsDeleteMemberResult wsDeleteMemberResult : wsDeleteMemberResults.getResults()) {
        
        substituteMap.put("index", index);
        substituteMap.put("wsDeleteMemberResult", wsDeleteMemberResult);
        substituteMap.put("wsSubject", wsDeleteMemberResult.getWsSubject());
        substituteMap.put("resultMetadata", wsDeleteMemberResult.getResultMetadata());
        
  //          result.append("Index " + index + ": success: " + wsDeleteMemberResult.getResultMetadata().getSuccess()
  //              + ": code: " + wsDeleteMemberResult.getResultMetadata().getResultCode() + ": " 
  //              + wsDeleteMemberResult.getWsSubject().getId() + "\n");
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
      
      return result.toString();
    }

    /**
   * retrieve a query filter from the args with suffix (if child group)
   * @param argMap
   * @param argMapNotUsed
   * @param suffix if a child of a previous one
   * @param required
   * @return the query filter
   */
  private static WsQueryFilter retrieveQueryFilterFromArgs(Map<String, String> argMap,
      Map<String, String> argMapNotUsed, String suffix, boolean required) {
    
    String queryFilterType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "queryFilterType" + suffix, required);
    
    if (GrouperClientUtils.isBlank(queryFilterType)) {
      return null;
    }
    
    WsQueryFilter result = new WsQueryFilter();
    result.setQueryFilterType(queryFilterType);
    
    //at this point everything is optional
    String groupAttributeName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "groupAttributeName" + suffix, false);
    result.setGroupAttributeName(groupAttributeName);
  
    String groupAttributeValue = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "groupAttributeValue" + suffix, false);
    result.setGroupAttributeValue(groupAttributeValue);
  
    String groupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "groupName" + suffix, false);
    result.setGroupName(groupName);
    
    String groupTypeName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "groupTypeName" + suffix, false);
    result.setGroupTypeName(groupTypeName);
  
    String groupUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "groupUuid" + suffix, false);
    result.setGroupUuid(groupUuid);
  
    WsQueryFilter queryFilter0 = retrieveQueryFilterFromArgs(argMap, 
        argMapNotUsed, suffix + "0", false);
    result.setQueryFilter0(queryFilter0);
  
    WsQueryFilter queryFilter1 = retrieveQueryFilterFromArgs(argMap, 
        argMapNotUsed, suffix + "1", false);
    result.setQueryFilter1(queryFilter1);
    
    String stemName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "stemName" + suffix, false);
    result.setStemName(stemName);
  
    String stemNameScope = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        "stemNameScope" + suffix, false);
    result.setStemNameScope(stemNameScope);
    
    return result;
  }

    /**
     * retrieve a query filter from the args with suffix (if child group)
     * @param argMap
     * @param argMapNotUsed
     * @param suffix if a child of a previous one
     * @param required
     * @return the query filter
     */
    private static WsStemQueryFilter retrieveStemQueryFilterFromArgs(Map<String, String> argMap,
        Map<String, String> argMapNotUsed, String suffix, boolean required) {
      
      String stemQueryFilterType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
          "stemQueryFilterType" + suffix, required);
      
      if (GrouperClientUtils.isBlank(stemQueryFilterType)) {
        return null;
      }
      
      WsStemQueryFilter result = new WsStemQueryFilter();
      result.setStemQueryFilterType(stemQueryFilterType);
      
      //at this point everything is optional
      String stemAttributeName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
          "stemAttributeName" + suffix, false);
      result.setStemAttributeName(stemAttributeName);

      String stemAttributeValue = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
          "stemAttributeValue" + suffix, false);
      result.setStemAttributeValue(stemAttributeValue);

      String stemName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
          "stemName" + suffix, false);
      result.setStemName(stemName);
      
      String parentStemName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
          "parentStemName" + suffix, false);
      result.setParentStemName(parentStemName);

      String parentStemNameScope = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
          "parentStemNameScope" + suffix, false);
      result.setParentStemNameScope(parentStemNameScope);

      String stemUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
          "stemUuid" + suffix, false);
      result.setStemUuid(stemUuid);

      WsStemQueryFilter stemQueryFilter0 = retrieveStemQueryFilterFromArgs(argMap, 
          argMapNotUsed, suffix + "0", false);
      result.setStemQueryFilter0(stemQueryFilter0);

      WsStemQueryFilter stemQueryFilter1 = retrieveStemQueryFilterFromArgs(argMap, 
          argMapNotUsed, suffix + "1", false);
      result.setStemQueryFilter1(stemQueryFilter1);
      
      return result;
    }
    
    /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String findGroups(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
    
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      GcFindGroups gcFindGroups = new GcFindGroups();        
    
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcFindGroups.assignClientVersion(clientVersion);
    
      for (WsParam param : params) {
        gcFindGroups.addParam(param);
      }
      
      List<String> groupNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "groupNames", false);
      List<String> groupUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "groupUuids", false);

      if (GrouperClientUtils.length(groupNames) > 0) {
        for (String groupName: groupNames) {
          gcFindGroups.addGroupName(groupName);
        }
      }
      
      if (GrouperClientUtils.length(groupUuids) > 0) {
        for (String groupUuid: groupUuids) {
          gcFindGroups.addGroupUuid(groupUuid);
        }
      }
      
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      
      gcFindGroups.assignActAsSubject(actAsSubject);
      
      gcFindGroups.assignIncludeGroupDetail(includeGroupDetail);
    
      WsQueryFilter wsQueryFilter = retrieveQueryFilterFromArgs(argMap, argMapNotUsed, "", false);
      gcFindGroups.assignQueryFilter(wsQueryFilter);
      
      //register that we will use this
      GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      
      failOnArgsNotUsed(argMapNotUsed);
    
      WsFindGroupsResults wsFindGroupsResults = gcFindGroups.execute();
      
      StringBuilder result = new StringBuilder();
      int index = 0;
      
      Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
    
      substituteMap.put("wsFindGroupsResults", wsFindGroupsResults);
      substituteMap.put("resultMetadata", wsFindGroupsResults.getResultMetadata());
      substituteMap.put("grouperClientUtils", new GrouperClientUtils());

      String outputTemplate = null;
    
      if (argMap.containsKey("outputTemplate")) {
        outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
        outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
      } else {
        outputTemplate = GrouperClientUtils.propertiesValue("webService.findGroups.output", true);
      }
      log.debug("Output template: " + outputTemplate + ", available variables: wsFindGroupsResults, " +
        "resultMetadata, grouperClientUtils, index, wsGroup");

      for (WsGroup wsGroup : GrouperClientUtils.nonNull(wsFindGroupsResults.getGroupResults(), WsGroup.class)) {
        
        substituteMap.put("index", index);
        substituteMap.put("wsGroup", wsGroup);
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
      
      return result.toString();
    }

    /**
     * @param argMap
     * @param argMapNotUsed
     * @return result
     */
    private static String findStems(Map<String, String> argMap,
        Map<String, String> argMapNotUsed) {
      
     
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      GcFindStems gcFindStems = new GcFindStems();        
  
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcFindStems.assignClientVersion(clientVersion);

      for (WsParam param : params) {
        gcFindStems.addParam(param);
      }
      
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      
      gcFindStems.assignActAsSubject(actAsSubject);
      

      WsStemQueryFilter wsStemQueryFilter = retrieveStemQueryFilterFromArgs(argMap, argMapNotUsed, "", false);
      gcFindStems.assignStemQueryFilter(wsStemQueryFilter);
      
      List<String> stemNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "stemNames", false);
      List<String> stemUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "stemUuids", false);

      if (GrouperClientUtils.length(stemNames) > 0) {
        for (String stemName: stemNames) {
          gcFindStems.addStemName(stemName);
        }
      }
      
      if (GrouperClientUtils.length(stemUuids) > 0) {
        for (String stemUuid: stemUuids) {
          gcFindStems.addStemUuid(stemUuid);
        }
      }

      
      //register that we will use this
      GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
      
      failOnArgsNotUsed(argMapNotUsed);

      WsFindStemsResults wsFindStemsResults = gcFindStems.execute();
      
      StringBuilder result = new StringBuilder();
      int index = 0;
      
      Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
      substituteMap.put("wsFindStemsResults", wsFindStemsResults);
      substituteMap.put("resultMetadata", wsFindStemsResults.getResultMetadata());
      substituteMap.put("grouperClientUtils", new GrouperClientUtils());

      String outputTemplate = null;
  
      if (argMap.containsKey("outputTemplate")) {
        outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
        outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
      } else {
        outputTemplate = GrouperClientUtils.propertiesValue("webService.findStems.output", true);
      }
      log.debug("Output template: " + outputTemplate + ", available variables: wsFindStemsResults, " +
        "resultMetadata, grouperClientUtils, index, wsStem");

      for (WsStem wsStem : GrouperClientUtils.nonNull(wsFindStemsResults.getStemResults(), WsStem.class)) {
        
        substituteMap.put("index", index);
        substituteMap.put("wsStem", wsStem);
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
      
      return result.toString();
    }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String getMembers(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
    List<String> groupNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "groupNames", false);
    List<String> groupUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "groupUuids", false);

    String fieldName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fieldName", false);
  
    String memberFilter = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "memberFilter", false);
  
    Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
    
    Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
    Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
    String sourceIds = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "sourceIds", false);
    
    GcGetMembers gcGetMembers = new GcGetMembers();        
  
    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
    gcGetMembers.assignClientVersion(clientVersion);

    if (GrouperClientUtils.length(groupNames) > 0) {
      for (String groupName: groupNames) {
        gcGetMembers.addGroupName(groupName);
      }
    }
    
    if (GrouperClientUtils.length(groupUuids) > 0) {
      for (String groupUuid: groupUuids) {
        gcGetMembers.addGroupUuid(groupUuid);
      }
    }
    
    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    gcGetMembers.assignActAsSubject(actAsSubject);
    
    gcGetMembers.assignMemberFilter(WsMemberFilter.valueOfIgnoreCase(memberFilter));
  
    gcGetMembers.assignIncludeGroupDetail(includeGroupDetail);
    gcGetMembers.assignIncludeSubjectDetail(includeSubjectDetail);
    
    gcGetMembers.assignFieldName(fieldName);
  
    for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
      gcGetMembers.addSubjectAttributeName(subjectAttribute);
    }
    
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    for (WsParam param : params) {
      gcGetMembers.addParam(param);
    }
    
    //add source ids if there
    if (!GrouperClientUtils.isBlank(sourceIds)) {
      String[] sourceIdsArray = GrouperClientUtils.splitTrim(sourceIds, ",");
      for (String sourceId : sourceIdsArray) {
        gcGetMembers.addSourceId(sourceId);
      }
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    failOnArgsNotUsed(argMapNotUsed);

    WsGetMembersResults wsGetMembersResults = gcGetMembers.execute();
    
    StringBuilder result = new StringBuilder();
    int groupIndex = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsGetMembersResults", wsGetMembersResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());

    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.getMembers.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsGetMembersResults, " +
      "grouperClientUtils, groupIndex, wsGetMembersResult, wsGroup, resultMetadata, subjectIndex, wsSubject");

    for (WsGetMembersResult wsGetMembersResult : GrouperClientUtils.nonNull(wsGetMembersResults.getResults(), WsGetMembersResult.class)) {
      
      substituteMap.put("groupIndex", groupIndex);
      substituteMap.put("wsGetMembersResult", wsGetMembersResult);
      substituteMap.put("wsGroup", wsGetMembersResult.getWsGroup());
      substituteMap.put("resultMetadata", wsGetMembersResult.getResultMetadata());
      int subjectIndex = 0;
      for (WsSubject wsSubject : GrouperClientUtils.nonNull(wsGetMembersResult.getWsSubjects(), WsSubject.class)) {
        
        substituteMap.put("subjectIndex", subjectIndex);
        substituteMap.put("wsSubject", wsSubject);
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        subjectIndex++;
      }
      
      groupIndex++;
    }
    
    return result.toString();
  }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String getGroups(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
   
    String memberFilter = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "memberFilter", false);

    Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
    
    Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");

    Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);

    String scope = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "scope", false);
    
    String stemUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemUuid", false);
    String stemName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemName", false);
    
    WsStemLookup wsStemLookup = null;
    
    if (!GrouperClientUtils.isBlank(stemName) || !GrouperClientUtils.isBlank(stemUuid)) {
      wsStemLookup = new WsStemLookup(stemName, stemUuid);
    }

    String fieldName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fieldName", false);

    String stemScope = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemScope", false);

    String enabled = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "enabled", false);
    
    Integer pageSize = GrouperClientUtils.argMapInteger(argMap, argMapNotUsed, "pageSize", false, null); 
    
    Integer pageNumber = GrouperClientUtils.argMapInteger(argMap, argMapNotUsed, "pageNumber", false, null); 

    String sortString = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "sortString", false);
    
    Boolean ascending = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "ascending");
    
    GcGetGroups gcGetGroups = new GcGetGroups();        

    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
    gcGetGroups.assignClientVersion(clientVersion);

    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    gcGetGroups.assignActAsSubject(actAsSubject);
    
    gcGetGroups.assignMemberFilter(WsMemberFilter.valueOfIgnoreCase(memberFilter));

    gcGetGroups.assignIncludeGroupDetail(includeGroupDetail);
    gcGetGroups.assignIncludeSubjectDetail(includeSubjectDetail);
    
    gcGetGroups.assignScope(scope);
    gcGetGroups.assignWsStemLookup(wsStemLookup);
    if (!GrouperClientUtils.isBlank(stemScope)) {
      StemScope theStemScope = StemScope.valueOfIgnoreCase(stemScope);
      gcGetGroups.assignStemScope(theStemScope);
    }
    
    if (!GrouperClientUtils.isBlank(enabled)) {
      //A
      Boolean enabledBoolean = null;
      if (!GrouperClientUtils.equalsIgnoreCase("A", enabled)) {
        enabledBoolean = GrouperClientUtils.booleanValue(enabled);
      }
      
      gcGetGroups.assignEnabled(enabledBoolean);
    }
    gcGetGroups.assignPageSize(pageSize);
    gcGetGroups.assignPageNumber(pageNumber);
    gcGetGroups.assignSortString(sortString);
    gcGetGroups.assignAscending(ascending);
    
    gcGetGroups.assignFieldName(fieldName);
    
    List<WsSubjectLookup> wsSubjectLookupList = retrieveSubjectsFromArgs(argMap, argMapNotUsed, true);
    
    for (WsSubjectLookup wsSubjectLookup : wsSubjectLookupList) {
      gcGetGroups.addSubjectLookup(wsSubjectLookup);
    }
    
    for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
      gcGetGroups.addSubjectAttributeName(subjectAttribute);
    }
    
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    for (WsParam param : params) {
      gcGetGroups.addParam(param);
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    failOnArgsNotUsed(argMapNotUsed);

    WsGetGroupsResults wsGetGroupsResults = gcGetGroups.execute();
    
    StringBuilder result = new StringBuilder();
    int subjectIndex = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();

    substituteMap.put("wsGetGroupsResults", wsGetGroupsResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());

    String outputTemplate = null;

    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.getGroups.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsGetGroupsResults, " +
      "grouperClientUtils, subjectIndex, wsGetGroupsResult, resultMetadata, wsSubject, groupIndex, wsGroup");

    for (WsGetGroupsResult wsGetGroupsResult : GrouperClientUtils.nonNull(wsGetGroupsResults.getResults(), WsGetGroupsResult.class)) {
      
      substituteMap.put("subjectIndex", subjectIndex);
      substituteMap.put("wsGetGroupsResult", wsGetGroupsResult);
      substituteMap.put("resultMetadata", wsGetGroupsResult.getResultMetadata());
      substituteMap.put("wsSubject", wsGetGroupsResult.getWsSubject());
      
      int groupIndex = 0;
      for (WsGroup wsGroup : GrouperClientUtils.nonNull(wsGetGroupsResult.getWsGroups(), WsGroup.class)) {
        
        substituteMap.put("groupIndex", groupIndex);
        substituteMap.put("wsGroup", wsGroup);
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        groupIndex++;
      }
      
      subjectIndex++;
    }
    
    return result.toString();
  }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @return the lookup
   */
  private static WsSubjectLookup retrieveActAsSubjectFromArgs(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
    return retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "actAs", false);
  }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @param prefix is the prefix of the subject, e.g. actAs for actAsSubjectId
   * @param required 
   * @return the lookup
   */
  private static WsSubjectLookup retrieveSuffixSubjectFromArgs(Map<String, String> argMap,
      Map<String, String> argMapNotUsed, String prefix, boolean required) {
    
    String argMapPrefixSubjectIdKey = prefix + "SubjectId";
    
    //see if we have an alias
    {
      String aliasSubjectId = GrouperClientUtils.propertiesValue(
          "grouperClient.alias.SubjectId", false);
      
      if (!GrouperClientUtils.isBlank(aliasSubjectId)) {
        String aliasKey = prefix + aliasSubjectId;
        boolean containsAliasKey = argMap.containsKey(aliasKey);
        if (argMap.containsKey(argMapPrefixSubjectIdKey) && containsAliasKey) {
          throw new RuntimeException("You cannot pass both arguments " + prefix + "SubjectId and " + aliasKey + ", choose one or the other");
        }
        argMapPrefixSubjectIdKey = containsAliasKey ? aliasKey : argMapPrefixSubjectIdKey;
      }
    }    
    
    String argMapPrefixSubjectIdentifierKey = prefix + "SubjectIdentifier";
    {
      String aliasSubjectIdentifier = GrouperClientUtils.propertiesValue(
          "grouperClient.alias.SubjectIdentifier", false);
      
      if (!GrouperClientUtils.isBlank(aliasSubjectIdentifier)) {
        String aliasKey = prefix + aliasSubjectIdentifier;
        boolean containsAliasKey = argMap.containsKey(aliasKey);
        if (argMap.containsKey(argMapPrefixSubjectIdentifierKey) && containsAliasKey) {
          throw new RuntimeException("You cannot pass both arguments " + prefix + "SubjectIdentifier and " + aliasKey + ", choose one or the other");
        }
        argMapPrefixSubjectIdentifierKey = containsAliasKey ? aliasKey : argMapPrefixSubjectIdentifierKey;
      }
    }
    
    // set the act as id
    String prefixSubjectId = GrouperClientUtils.argMapString(argMap, argMapNotUsed, argMapPrefixSubjectIdKey, false);
    String prefixSubjectIdentifier = GrouperClientUtils.argMapString(argMap, argMapNotUsed, argMapPrefixSubjectIdentifierKey, false);
    String prefixSubjectSource = GrouperClientUtils.argMapString(argMap, argMapNotUsed, prefix + "SubjectSource", false);
    
    if (GrouperClientUtils.isBlank(prefixSubjectId) 
        && GrouperClientUtils.isBlank(prefixSubjectIdentifier)
        && GrouperClientUtils.isBlank(prefixSubjectSource)) {
      if (required) {
        throw new RuntimeException(prefix + "Subject is required");
      }
      return null;
    }
    
    WsSubjectLookup actAsSubject = new WsSubjectLookup(prefixSubjectId, prefixSubjectSource, prefixSubjectIdentifier);
    return actAsSubject;
  }

  /**
   * retrieve params from args
   * @param argMap
   * @param argMapNotUsed
   * @return the list of params or empty list if none
   */
  private static List<WsParam> retrieveParamsFromArgs(
      Map<String, String> argMap, Map<String, String> argMapNotUsed) {

    List<WsParam> params = new ArrayList<WsParam>();
    int index = 0;
    while (true) {

      String argName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "paramName" + index, false);
      if (GrouperClientUtils.isBlank(argName)) {
        break;
      }
      String argValue = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "paramValue" + index, true);
      params.add(new WsParam(argName, argValue));
      index++;
    }
    return params;
  }
  
  /**
   * @param argMap
   * @param argMapNotUsed
   * @param required if they are required
   * @return the subjects
   */
  private static List<WsSubjectLookup> retrieveSubjectsFromArgs(
      Map<String, String> argMap, Map<String, String> argMapNotUsed, boolean required) {
    
    
    String argMapSubjectIdsKey = "subjectIds";
    
    //see if we have an alias
    {
      String aliasSubjectIds = GrouperClientUtils.propertiesValue(
          "grouperClient.alias.subjectIds", false);
      
      if (!GrouperClientUtils.isBlank(aliasSubjectIds)) {
        boolean containsAliasKey = argMap.containsKey(aliasSubjectIds);
        if (argMap.containsKey(argMapSubjectIdsKey) && containsAliasKey) {
          throw new RuntimeException("You cannot pass both arguments subjectIds and " + aliasSubjectIds + ", choose one or the other");
        }
        argMapSubjectIdsKey = containsAliasKey ? aliasSubjectIds : argMapSubjectIdsKey;
      }
    }    

    String argMapSubjectIdentifiersKey = "subjectIdentifiers";
    
    //see if we have an alias
    {
      String aliasSubjectIdentifiers = GrouperClientUtils.propertiesValue(
          "grouperClient.alias.subjectIdentifiers", false);
      
      if (!GrouperClientUtils.isBlank(aliasSubjectIdentifiers)) {
        boolean containsAliasKey = argMap.containsKey(aliasSubjectIdentifiers);
        if (argMap.containsKey(argMapSubjectIdentifiersKey) && containsAliasKey) {
          throw new RuntimeException("You cannot pass both arguments subjectIdentifiers and " + aliasSubjectIdentifiers + ", choose one or the other");
        }
        argMapSubjectIdentifiersKey = containsAliasKey ? aliasSubjectIdentifiers : argMapSubjectIdentifiersKey;
      }
    }    
    
    String argMapSubjectIdsFileKey = "subjectIdsFile";
    
    //see if we have an alias
    {
      String aliasSubjectIds = GrouperClientUtils.propertiesValue(
          "grouperClient.alias.subjectIds", false);
      
      if (!GrouperClientUtils.isBlank(aliasSubjectIds)) {
        String aliasSubjectIdsFile = aliasSubjectIds + "File";
        boolean containsAliasKey = argMap.containsKey(aliasSubjectIdsFile);
        if (argMap.containsKey(argMapSubjectIdsFileKey) && containsAliasKey) {
          throw new RuntimeException("You cannot pass both arguments subjectIdsFile and " 
              + aliasSubjectIdsFile + ", choose one or the other");
        }
        argMapSubjectIdsFileKey = containsAliasKey ? aliasSubjectIdsFile : argMapSubjectIdsFileKey;
      }
    }    

    String argMapSubjectIdentifiersFileKey = "subjectIdentifiersFile";
    
    //see if we have an alias
    {
      String aliasSubjectIdentifiers = GrouperClientUtils.propertiesValue(
          "grouperClient.alias.subjectIdentifiers", false);
      
      if (!GrouperClientUtils.isBlank(aliasSubjectIdentifiers)) {
        String aliasSubjectIdentifiersFile = aliasSubjectIdentifiers + "File";
        boolean containsAliasKey = argMap.containsKey(aliasSubjectIdentifiersFile);
        if (argMap.containsKey(argMapSubjectIdentifiersFileKey) && containsAliasKey) {
          throw new RuntimeException("You cannot pass both arguments subjectIdentifiersFile and " 
              + aliasSubjectIdentifiersFile + ", choose one or the other");
        }
        argMapSubjectIdentifiersFileKey = containsAliasKey ? aliasSubjectIdentifiersFile : argMapSubjectIdentifiersFileKey;
      }
    }    
    
    List<String> subjectIdsList = GrouperClientUtils.argMapList(argMap, argMapNotUsed, argMapSubjectIdsKey, false);
    
    List<String> subjectIdentifiersList = GrouperClientUtils.argMapList(argMap, argMapNotUsed, 
        argMapSubjectIdentifiersKey, false);
    
    List<String> sourceIdsList = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "subjectSources", false);
    
    String defaultSubjectSource = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "defaultSubjectSource", false);
    
    // add two subjects to the group
    int subjectIdLength = GrouperClientUtils.length(subjectIdsList);
    int subjectIdentifierLength = GrouperClientUtils.length(subjectIdentifiersList);
    int sourceIdLength = GrouperClientUtils.length(sourceIdsList);
    
    if (argMap.containsKey(argMapSubjectIdsFileKey)) {
      if (subjectIdLength > 0) {
        throw new RuntimeException("Cant pass in " + argMapSubjectIdsKey + " and " 
            + argMapSubjectIdsFileKey + ", use one or the other");
      }
      subjectIdsList = GrouperClientUtils.argMapFileList(argMap, argMapNotUsed, argMapSubjectIdsFileKey, true);
      subjectIdLength = GrouperClientUtils.length(subjectIdsList);
    }
    
    if (argMap.containsKey(argMapSubjectIdentifiersFileKey)) {
      if (subjectIdentifierLength > 0) {
        throw new RuntimeException("Cant pass in " + argMapSubjectIdentifiersKey 
            + " and " + argMapSubjectIdentifiersFileKey + ", use one or the other");
      }
      subjectIdentifiersList = GrouperClientUtils.argMapFileList(argMap, argMapNotUsed, 
          argMapSubjectIdentifiersFileKey, true);
      subjectIdentifierLength = GrouperClientUtils.length(subjectIdentifiersList);
    }
    
    if (argMap.containsKey("sourceIdsFile")) {
      if (sourceIdLength > 0) {
        throw new RuntimeException("Cant pass in " + argMapSubjectIdsKey + " and sourceIdsFile, use one or the other");
      }
      sourceIdsList = GrouperClientUtils.argMapFileList(argMap, argMapNotUsed, "sourceIdsFile", true);
      sourceIdLength = GrouperClientUtils.length(sourceIdsList);
    }
    
    if (!GrouperClientUtils.isBlank(defaultSubjectSource) && sourceIdLength > 0) {
      throw new RuntimeException("Cant specify a default subject source, and source ids");
    }
    
    if (subjectIdLength == 0 && subjectIdentifierLength == 0) {
      if (!required) {
        return null;
      }
      throw new RuntimeException("Cant pass no subject ids and no subject identifiers!");
    }
    if (subjectIdLength != 0 && subjectIdentifierLength != 0) {
      throw new RuntimeException("Cant pass " + argMapSubjectIdsKey + " and " 
          + argMapSubjectIdentifiersKey + "! (pass one of the other)");
    }
    int subjectsLength = Math.max(subjectIdLength, subjectIdentifierLength);
    
    //if there is one source, that is ok, use it for everything
    if (sourceIdLength == 1 && subjectsLength > 1) {
      
      //replicate it out
      for (int i=1;i<subjectsLength;i++) {
        sourceIdsList.add(sourceIdsList.get(0));
      }
      sourceIdLength = sourceIdsList.size();
    }
    
    if (sourceIdLength > 0 && sourceIdLength != subjectIdLength 
        && sourceIdLength != subjectIdentifierLength) {
      throw new RuntimeException("If subjectSources are passed in, you " +
          "must pass the same number as " + argMapSubjectIdsKey + " or " 
          + argMapSubjectIdentifiersKey + ", or pass one for all.");
    }
    
    List<WsSubjectLookup> wsSubjectLookupList = new ArrayList<WsSubjectLookup>();
    
    for (int i=0;i<subjectsLength;i++) {
      WsSubjectLookup wsSubjectLookup = new WsSubjectLookup();
      if (subjectIdLength > 0) {
        wsSubjectLookup.setSubjectId(subjectIdsList.get(i));
      }
      if (subjectIdentifierLength > 0) {
        wsSubjectLookup.setSubjectIdentifier(subjectIdentifiersList.get(i));
      }
      if (sourceIdLength > 0) {
        wsSubjectLookup.setSubjectSourceId(sourceIdsList.get(i));
      }
      if (!GrouperClientUtils.isBlank(defaultSubjectSource)) {
        wsSubjectLookup.setSubjectSourceId(defaultSubjectSource);
      }
      wsSubjectLookupList.add(wsSubjectLookup);
    }
    return wsSubjectLookupList;
  }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @return the subject
   */
  private static WsSubjectLookup retrieveSubjectFromArgs(
      Map<String, String> argMap, Map<String, String> argMapNotUsed) {
    
    
    String argMapSubjectIdKey = "subjectId";
    
    //see if we have an alias
    {
      String aliasSubjectId = GrouperClientUtils.propertiesValue(
          "grouperClient.alias.subjectId", false);
      
      if (!GrouperClientUtils.isBlank(aliasSubjectId)) {
        boolean containsAliasKey = argMap.containsKey(aliasSubjectId);
        if (argMap.containsKey(argMapSubjectIdKey) && containsAliasKey) {
          throw new RuntimeException("You cannot pass both arguments subjectId and " 
              + aliasSubjectId + ", choose one or the other");
        }
        argMapSubjectIdKey = containsAliasKey ? aliasSubjectId : argMapSubjectIdKey;
      }
    }    

    String argMapSubjectIdentifierKey = "subjectIdentifier";
    
    //see if we have an alias
    {
      String aliasSubjectIdentifier = GrouperClientUtils.propertiesValue(
          "grouperClient.alias.subjectIdentifier", false);
      
      if (!GrouperClientUtils.isBlank(aliasSubjectIdentifier)) {
        boolean containsAliasKey = argMap.containsKey(aliasSubjectIdentifier);
        if (argMap.containsKey(argMapSubjectIdentifierKey) && containsAliasKey) {
          throw new RuntimeException("You cannot pass both arguments subjectIdentifier and " 
              + aliasSubjectIdentifier + ", choose one or the other");
        }
        argMapSubjectIdentifierKey = containsAliasKey ? aliasSubjectIdentifier : argMapSubjectIdentifierKey;
      }
    }    
    
    String subjectId = GrouperClientUtils.argMapString(argMap, argMapNotUsed, argMapSubjectIdKey, false);
    
    String subjectIdentifier = GrouperClientUtils.argMapString(argMap, argMapNotUsed, 
        argMapSubjectIdentifierKey, false);
    
    String sourceId = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "sourceId", false);
    
    // add two subjects to the group
    boolean hasSubjectId = GrouperClientUtils.isNotBlank(subjectId);
    boolean hasSubjectIdentifier = GrouperClientUtils.isNotBlank(subjectIdentifier);
    
    if (hasSubjectId && hasSubjectIdentifier) {
      throw new RuntimeException("Cant pass " + argMapSubjectIdKey 
          + " and " + argMapSubjectIdentifierKey + "! (pass one of the other)");
    }

    if (hasSubjectId || hasSubjectIdentifier) {
      return new WsSubjectLookup(subjectId, sourceId, subjectIdentifier);
    }
    
    return null;
  }

  /**
   * print usage and exit
   */
  public static void usage() {
    //read in the usage file
    String usage = GrouperClientUtils.readResourceIntoString("grouper.client.usage.txt", GrouperClientCommonUtils.class);
    System.err.println(usage);
  }

  /**
   * get memberships
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String getMemberships(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {

    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);


    String fieldName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fieldName", false);

    List<String> groupNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "groupNames", false);
    List<String> groupUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "groupUuids", false);
  
    Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");

    Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");

    String memberFilter = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "memberFilter", false);

    List<String> membershipIds = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "membershipIds", false);

    String scope = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "scope", false);

    String sourceIds = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "sourceIds", false);

    String stemName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemName", false);
    String stemUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemUuid", false);
    
    WsStemLookup wsStemLookup = null;
    if (!GrouperClientUtils.isBlank(stemName) || !GrouperClientUtils.isBlank(stemUuid)) {
      wsStemLookup = new WsStemLookup(stemName, stemUuid);
    }
    
    String stemScope = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemScope", false);
    
    Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);

    List<WsSubjectLookup> wsSubjectLookupList = retrieveSubjectsFromArgs(argMap, argMapNotUsed, false);

    GcGetMemberships gcGetMemberships = new GcGetMemberships();        

    gcGetMemberships.assignActAsSubject(actAsSubject);
    
    gcGetMemberships.assignClientVersion(clientVersion);
    
    {
      String enabled = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "enabled", false);
      gcGetMemberships.assignEnabled(enabled);
    }
    
    gcGetMemberships.assignFieldName(fieldName);
    
    if (GrouperClientUtils.length(groupNames) > 0) {
      for (String groupName: groupNames) {
        gcGetMemberships.addGroupName(groupName);
      }
    }
    
    if (GrouperClientUtils.length(groupUuids) > 0) {
      for (String groupUuid: groupUuids) {
        gcGetMemberships.addGroupUuid(groupUuid);
      }
    }
    
    gcGetMemberships.assignIncludeGroupDetail(includeGroupDetail);
    gcGetMemberships.assignIncludeSubjectDetail(includeSubjectDetail);
    
    gcGetMemberships.assignMemberFilter(WsMemberFilter.valueOfIgnoreCase(memberFilter));
  
    if (GrouperClientUtils.length(membershipIds) > 0) {
      for (String membershipId: membershipIds) {
        gcGetMemberships.addMembershipId(membershipId);
      }
    }
    
    
    for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
      gcGetMemberships.addSubjectAttributeName(subjectAttribute);
    }
    
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    for (WsParam param : params) {
      gcGetMemberships.addParam(param);
    }
    
    //add source ids if there
    if (!GrouperClientUtils.isBlank(sourceIds)) {
      String[] sourceIdsArray = GrouperClientUtils.splitTrim(sourceIds, ",");
      for (String sourceId : sourceIdsArray) {
        gcGetMemberships.addSourceId(sourceId);
      }
    }
    
    gcGetMemberships.assignScope(scope);

    gcGetMemberships.assigStemScope(stemScope);
    
    gcGetMemberships.assignWsStem(wsStemLookup);
    
    if (GrouperClientUtils.length(wsSubjectLookupList) > 0) {
      for (WsSubjectLookup wsSubjectLookup: wsSubjectLookupList) {
        gcGetMemberships.addWsSubjectLookup(wsSubjectLookup);
      }
    }

    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    
    
    failOnArgsNotUsed(argMapNotUsed);
  
    WsGetMembershipsResults wsGetMembershipsResults = gcGetMemberships.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsGetMembershipsResults", wsGetMembershipsResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.getMemberships.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsGetMembershipsResults, " +
      "grouperClientUtils, index, wsMembership");
  
    //lets index the groups by groupId 
    Map<String, WsGroup> groupLookup = new HashMap<String, WsGroup>();
    
    for (WsGroup wsGroup : GrouperClientUtils.nonNull(wsGetMembershipsResults.getWsGroups(), WsGroup.class)) {
      groupLookup.put(wsGroup.getUuid(), wsGroup);
    }
    
    //lets index the subjects by multikey of sourceId and subjectId
    Map<MultiKey, WsSubject> subjectLookup = new HashMap<MultiKey, WsSubject>();
    
    for (WsSubject wsSubject : GrouperClientUtils.nonNull(wsGetMembershipsResults.getWsSubjects(), WsSubject.class)) {
      MultiKey key = new MultiKey(wsSubject.getSourceId(), wsSubject.getId());
      subjectLookup.put(key, wsSubject);
    }
    
    for (WsMembership wsMembership : GrouperClientUtils.nonNull(wsGetMembershipsResults.getWsMemberships(), WsMembership.class)) {
      
      WsGroup wsGroup = groupLookup.get(wsMembership.getGroupId());
      MultiKey subjectKey = new MultiKey(wsMembership.getSubjectSourceId(), wsMembership.getSubjectId());
      WsSubject wsSubject = subjectLookup.get(subjectKey);
      
      substituteMap.put("index", index);
      substituteMap.put("wsMembership", wsMembership);
      substituteMap.put("wsSubject", wsSubject);
      substituteMap.put("wsGroup", wsGroup);
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }
    
    return result.toString();
  }

  /**
   * get subjects
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String getSubjects(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
  
    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
  
    String fieldName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "fieldName", false);
  
    String searchString = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "searchString", false);

    String groupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupName", false);
    String groupUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupUuid", false);
  
    Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
  
    Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
    String memberFilter = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "memberFilter", false);
  
    String sourceIds = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "sourceIds", false);
  
    Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
    List<WsSubjectLookup> wsSubjectLookupList = retrieveSubjectsFromArgs(argMap, argMapNotUsed, false);
  
    GcGetSubjects gcGetSubjects = new GcGetSubjects();        
  
    gcGetSubjects.assignActAsSubject(actAsSubject);

    
    gcGetSubjects.assignClientVersion(clientVersion);
  
    gcGetSubjects.assignFieldName(fieldName);
    
    if (!GrouperClientUtils.isBlank(groupName) || !GrouperClientUtils.isBlank(groupUuid)) {
      WsGroupLookup wsGroupLookup = new WsGroupLookup(groupName, groupUuid);
      gcGetSubjects.assignGroupLookup(wsGroupLookup);
    }
    
    gcGetSubjects.assignIncludeGroupDetail(includeGroupDetail);
    gcGetSubjects.assignIncludeSubjectDetail(includeSubjectDetail);
    
    gcGetSubjects.assignMemberFilter(WsMemberFilter.valueOfIgnoreCase(memberFilter));
  
    for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
      gcGetSubjects.addSubjectAttributeName(subjectAttribute);
    }
    
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    for (WsParam param : params) {
      gcGetSubjects.addParam(param);
    }
    
    //add source ids if there
    if (!GrouperClientUtils.isBlank(sourceIds)) {
      String[] sourceIdsArray = GrouperClientUtils.splitTrim(sourceIds, ",");
      for (String sourceId : sourceIdsArray) {
        gcGetSubjects.addSourceId(sourceId);
      }
    }
    
    gcGetSubjects.assignSearchString(searchString);
    
    if (GrouperClientUtils.length(wsSubjectLookupList) > 0) {
      for (WsSubjectLookup wsSubjectLookup: wsSubjectLookupList) {
        gcGetSubjects.addWsSubjectLookup(wsSubjectLookup);
      }
    }
  
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    
    
    failOnArgsNotUsed(argMapNotUsed);
  
    WsGetSubjectsResults wsGetSubjectsResults = gcGetSubjects.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsGetSubjectsResults", wsGetSubjectsResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.getSubjects.output", true);
    }
    substituteMap.put("wsGroup", wsGetSubjectsResults.getWsGroup());

    log.debug("Output template: " + outputTemplate + ", available variables: wsGetSubjectsResults, " +
      "grouperClientUtils, index, wsSubject, wsGroup, success");

    for (WsSubject wsSubject : GrouperClientUtils.nonNull(wsGetSubjectsResults.getWsSubjects(), WsSubject.class)) {
            
      substituteMap.put("index", index);
      substituteMap.put("wsSubject", wsSubject);

      String success = "F";
      if ("SUCCESS".equalsIgnoreCase(wsSubject.getResultCode())) {
        success = "T";
      }
      substituteMap.put("success", success);
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }
    
    return result.toString();
  }

  /**
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String assignGrouperPrivileges(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
    
    String groupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupName", false);
    String groupUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "groupUuid", false);
    String stemName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemName", false);
    String stemUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "stemUuid", false);
    
    String privilegeType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "privilegeType", false);
    String txType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "txType", false);
    Boolean replaceAllExisting = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "replaceAllExisting");

    List<String> privilegeNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "privilegeNames", true);
  
    Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
    
    boolean allowed = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "allowed", true, true);
    
    Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
  
    Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
    List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
    
    GcAssignGrouperPrivileges gcAssignGrouperPrivileges = new GcAssignGrouperPrivileges();
  
    String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
    gcAssignGrouperPrivileges.assignClientVersion(clientVersion);
  
    for (WsParam param : params) {
      gcAssignGrouperPrivileges.addParam(param);
    }
    
    List<WsSubjectLookup> wsSubjectLookups = retrieveSubjectsFromArgs(argMap,
        argMapNotUsed, false);
    
    if (!GrouperClientUtils.isBlank(stemName) || !GrouperClientUtils.isBlank(stemUuid)) {
      gcAssignGrouperPrivileges.assignStemLookup(new WsStemLookup(stemName, stemUuid));
    }

    if (!GrouperClientUtils.isBlank(groupName) || !GrouperClientUtils.isBlank(groupUuid)) {
      gcAssignGrouperPrivileges.assignGroupLookup(new WsGroupLookup(groupName, groupUuid));
    }

    gcAssignGrouperPrivileges.assignPrivilegeType(privilegeType);
    
    gcAssignGrouperPrivileges.assignTxType(GcTransactionType.valueOfIgnoreCase(txType));

    gcAssignGrouperPrivileges.assignReplaceAllExisting(replaceAllExisting);
    
    for (String privilegeName : privilegeNames) {
      gcAssignGrouperPrivileges.addPrivilegeName(privilegeName);
    }
    
    for (WsSubjectLookup wsSubjectLookup : GrouperClientUtils.nonNull(wsSubjectLookups)) {
      gcAssignGrouperPrivileges.addSubjectLookup(wsSubjectLookup);
    }
    
    gcAssignGrouperPrivileges.assignAllowed(allowed);
    
    WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
    
    gcAssignGrouperPrivileges.assignActAsSubject(actAsSubject);
    
    gcAssignGrouperPrivileges.assignIncludeGroupDetail(includeGroupDetail);
    gcAssignGrouperPrivileges.assignIncludeSubjectDetail(includeSubjectDetail);
    
  
    for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
      gcAssignGrouperPrivileges.addSubjectAttributeName(subjectAttribute);
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    failOnArgsNotUsed(argMapNotUsed);
  
    WsAssignGrouperPrivilegesResults wsAssignGrouperPrivilegesResults = gcAssignGrouperPrivileges.execute();
    
    StringBuilder result = new StringBuilder();
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsAssignGrouperPrivilegesResults", wsAssignGrouperPrivilegesResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.assignGrouperPrivileges.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsAssignGrouperPrivilegesResults, wsAssignGrouperPrivilegesResult, " +
      "grouperClientUtils, wsSubject, resultMetadata, wsGroup, wsStem, objectType, objectName, index");
  
    substituteMap.put("wsGroup", wsAssignGrouperPrivilegesResults.getWsGroup());
    substituteMap.put("wsStem", wsAssignGrouperPrivilegesResults.getWsStem());
    substituteMap.put("objectType", wsAssignGrouperPrivilegesResults.getWsStem() == null ? "group" : "stem");
    substituteMap.put("objectName", wsAssignGrouperPrivilegesResults.getWsStem() == null 
        ? wsAssignGrouperPrivilegesResults.getWsGroup().getName() : wsAssignGrouperPrivilegesResults.getWsStem().getName());

    int index = 0;
    for (WsAssignGrouperPrivilegesResult wsAssignGrouperPrivilegesResult : wsAssignGrouperPrivilegesResults.getResults()) {
      
      substituteMap.put("index", index);
      substituteMap.put("wsAssignGrouperPrivilegesResult", wsAssignGrouperPrivilegesResult);
      substituteMap.put("wsSubject", wsAssignGrouperPrivilegesResult.getWsSubject());
      substituteMap.put("resultMetadata", wsAssignGrouperPrivilegesResult.getResultMetadata());
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }

    return result.toString();
  }

  /**
   * get attributeAssignments
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String getAttributeAssignments(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
  
    GcGetAttributeAssignments gcGetAttributeAssignments = new GcGetAttributeAssignments();        

    for (int i=0;i<10;i++) {
      WsSubjectLookup ownerMembershipAnySubjectLookup = retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "ownerMembershipAny" + i, false);
      String ownerMembershipAnyGroupUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "ownerMembershipAny" + i + "GroupUuid", false);
      String ownerMembershipAnyGroupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "ownerMembershipAny" + i + "GroupName", false);
      if (ownerMembershipAnySubjectLookup != null || !GrouperClientUtils.isBlank(ownerMembershipAnyGroupName) 
          || !GrouperClientUtils.isBlank(ownerMembershipAnyGroupUuid)) {
        WsMembershipAnyLookup wsMembershipAnyLookup = new WsMembershipAnyLookup();
        wsMembershipAnyLookup.setWsGroupLookup(new WsGroupLookup(ownerMembershipAnyGroupName, ownerMembershipAnyGroupUuid));
        wsMembershipAnyLookup.setWsSubjectLookup(ownerMembershipAnySubjectLookup);
        gcGetAttributeAssignments.addOwnerMembershipAnyLookup(wsMembershipAnyLookup);
      }
    }
    
    for (int i=0;i<10;i++) {
      WsSubjectLookup ownerSubjectLookup = retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "owner" + i, false);
      if (ownerSubjectLookup != null) {
        gcGetAttributeAssignments.addOwnerSubjectLookup(ownerSubjectLookup);
      }
    }
    
    {
      Boolean includeAssignmentsOnAssignments = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeAssignmentsOnAssignments");
      gcGetAttributeAssignments.assignIncludeAssignmentsOnAssignments(includeAssignmentsOnAssignments);
    }
    {
      Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
      gcGetAttributeAssignments.assignIncludeSubjectDetail(includeSubjectDetail);
    }
    {
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      gcGetAttributeAssignments.assignIncludeGroupDetail(includeGroupDetail);
    }
    {
      Set<String> attributeAssignUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeAssignUuids", false);
      if (GrouperClientUtils.length(attributeAssignUuids) > 0) {
        for (String attributeAssignUuid : attributeAssignUuids) {
          gcGetAttributeAssignments.addAttributeAssignId(attributeAssignUuid);
        }
      }
    }
    {
      List<String> ownerGroupUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerGroupUuids", false);
      if (GrouperClientUtils.length(ownerGroupUuids) > 0) {
        for (String ownerGroupUuid: ownerGroupUuids) {
          gcGetAttributeAssignments.addOwnerGroupUuid(ownerGroupUuid);
        }
      }
    }
    {
      List<String> ownerGroupNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerGroupNames", false);
      if (GrouperClientUtils.length(ownerGroupNames) > 0) {
        for (String ownerGroupName: ownerGroupNames) {
          gcGetAttributeAssignments.addOwnerGroupName(ownerGroupName);
        }
      }
    }
    {
      List<String> ownerMembershipUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerMembershipUuids", false);
      if (GrouperClientUtils.length(ownerMembershipUuids) > 0) {
        for (String ownerMembershipUuid: ownerMembershipUuids) {
          gcGetAttributeAssignments.addOwnerMembershipId(ownerMembershipUuid);
        }
      }
    }
    {
      List<String> ownerStemUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerStemUuids", false);
      if (GrouperClientUtils.length(ownerStemUuids) > 0) {
        for (String ownerStemUuid: ownerStemUuids) {
          gcGetAttributeAssignments.addOwnerStemUuid(ownerStemUuid);
        }
      }
    }
    {
      List<String> ownerStemNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerStemNames", false);
      if (GrouperClientUtils.length(ownerStemNames) > 0) {
        for (String ownerStemName: ownerStemNames) {
          gcGetAttributeAssignments.addOwnerStemName(ownerStemName);
        }
      }
    }
    {
      String enabled = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "enabled", false);
      gcGetAttributeAssignments.assignEnabled(enabled);
    }

    {
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcGetAttributeAssignments.assignClientVersion(clientVersion);
      
    }
    
    {
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      gcGetAttributeAssignments.assignActAsSubject(actAsSubject);
    }

    {
      String attributeAssignType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "attributeAssignType", true);
      gcGetAttributeAssignments.assignAttributeAssignType(attributeAssignType);
    }
    
    { 
      Set<String> attributeDefNameNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNameNames", false);
      
      if (GrouperClientUtils.length(attributeDefNameNames) > 0) {
        for (String attributeDefNameName : attributeDefNameNames) {
          gcGetAttributeAssignments.addAttributeDefNameName(attributeDefNameName);
        }
      }
    }
    
    {
      Set<String> attributeDefNameUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNameUuids", false);
      if (GrouperClientUtils.length(attributeDefNameUuids) > 0) {
        for (String attributeDefNameUuid : attributeDefNameUuids) {
          gcGetAttributeAssignments.addAttributeDefNameUuid(attributeDefNameUuid);
        }
      }
    }
    
    { 
      Set<String> attributeDefNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNames", false);
      
      if (GrouperClientUtils.length(attributeDefNames) > 0) {
        for (String attributeDefName : attributeDefNames) {
          gcGetAttributeAssignments.addAttributeDefName(attributeDefName);
        }
      }
    }
    
    {
      Set<String> attributeDefUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefUuids", false);
      if (GrouperClientUtils.length(attributeDefUuids) > 0) {
        for (String attributeDefUuid : attributeDefUuids) {
          gcGetAttributeAssignments.addAttributeDefUuid(attributeDefUuid);
        }
      }
    }

    { 
      Set<String> ownerAttributeDefNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "ownerAttributeDefNames", false);
      
      if (GrouperClientUtils.length(ownerAttributeDefNames) > 0) {
        for (String ownerAttributeDefName : ownerAttributeDefNames) {
          gcGetAttributeAssignments.addOwnerAttributeDefName(ownerAttributeDefName);
        }
      }
    }
    
    {
      Set<String> ownerAttributeDefUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "ownerAttributeDefUuids", false);
      if (GrouperClientUtils.length(ownerAttributeDefUuids) > 0) {
        for (String ownerAttributeDefUuid : ownerAttributeDefUuids) {
          gcGetAttributeAssignments.addOwnerAttributeDefUuid(ownerAttributeDefUuid);
        }
      }
    }

    {
      Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
      for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
        gcGetAttributeAssignments.addSubjectAttributeName(subjectAttribute);
      }
    }    
    
    {
      Set<String> actions = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "actions", false);
      
      if (GrouperClientUtils.length(actions) > 0) {
        for (String action : actions) {
          gcGetAttributeAssignments.addAction(action);
        }
      }
    }
    
    
    
    {
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      for (WsParam param : params) {
        gcGetAttributeAssignments.addParam(param);
      }
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    
    
    failOnArgsNotUsed(argMapNotUsed);
  
    WsGetAttributeAssignmentsResults wsAttributeAssignmentsResults = gcGetAttributeAssignments.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsGetAttributeAssignmentsResults", wsAttributeAssignmentsResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.getAttributeAssignments.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsGetAttributeAssignmentsResults, " +
      "grouperClientUtils, index, wsAttributeAssignment");
  
    //########## GROUPS
    //lets index the groups by groupId 
    Map<String, WsGroup> groupLookup = new HashMap<String, WsGroup>();
    
    for (WsGroup wsGroupCurrent : GrouperClientUtils.nonNull(wsAttributeAssignmentsResults.getWsGroups(), WsGroup.class)) {
      groupLookup.put(wsGroupCurrent.getUuid(), wsGroupCurrent);
    }
    
    //########## SUBJECTS
    //lets index the subjects by multikey of sourceId and subjectId
    Map<MultiKey, WsSubject> subjectLookup = new HashMap<MultiKey, WsSubject>();
    
    for (WsSubject wsSubject : GrouperClientUtils.nonNull(wsAttributeAssignmentsResults.getWsSubjects(), WsSubject.class)) {
      MultiKey key = new MultiKey(wsSubject.getSourceId(), wsSubject.getId());
      subjectLookup.put(key, wsSubject);
    }

    //########## STEMS
    //lets index the stems by stemId 
    Map<String, WsStem> stemLookup = new HashMap<String, WsStem>();
    
    for (WsStem wsStemCurrent : GrouperClientUtils.nonNull(wsAttributeAssignmentsResults.getWsStems(), WsStem.class)) {
      stemLookup.put(wsStemCurrent.getUuid(), wsStemCurrent);
    }
    
    //########## ATTRIBUTE DEFS
    //lets index the attributeDefs by attributeDefId 
    Map<String, WsAttributeDef> attributeDefLookup = new HashMap<String, WsAttributeDef>();
    
    for (WsAttributeDef wsAttributeDefCurrent : GrouperClientUtils.nonNull(wsAttributeAssignmentsResults.getWsAttributeDefs(), WsAttributeDef.class)) {
      attributeDefLookup.put(wsAttributeDefCurrent.getUuid(), wsAttributeDefCurrent);
    }
    
    //########## ATTRIBUTE DEF NAMES
    //lets index the attributeDefNames by attributeDefNameId 
    Map<String, WsAttributeDefName> attributeDefNameLookup = new HashMap<String, WsAttributeDefName>();
    
    for (WsAttributeDefName wsAttributeDefNameCurrent : GrouperClientUtils.nonNull(wsAttributeAssignmentsResults.getWsAttributeDefNames(), WsAttributeDefName.class)) {
      attributeDefNameLookup.put(wsAttributeDefNameCurrent.getUuid(), wsAttributeDefNameCurrent);
    }
    
    //########## MEMBERSHIPS
    //lets index the memberships by membership id and membership immediate id (if immediate) 
    Map<String, WsMembership> membershipLookup = new HashMap<String, WsMembership>();
    
    for (WsMembership wsMembershipCurrent : GrouperClientUtils.nonNull(wsAttributeAssignmentsResults.getWsMemberships(), WsMembership.class)) {
      membershipLookup.put(wsMembershipCurrent.getMembershipId(), wsMembershipCurrent);
      if ("immediate".equals(wsMembershipCurrent.getMembershipType())) {
        membershipLookup.put(wsMembershipCurrent.getImmediateMembershipId(), wsMembershipCurrent);
      }
    }
    
    
    
    for (WsAttributeAssign wsAttributeAssign : GrouperClientUtils.nonNull(wsAttributeAssignmentsResults.getWsAttributeAssigns(), WsAttributeAssign.class)) {
      
      WsAttributeDef wsAttributeDef = attributeDefLookup.get(wsAttributeAssign.getAttributeDefId());
      WsAttributeDefName wsAttributeDefName = attributeDefNameLookup.get(wsAttributeAssign.getAttributeDefNameId());
      WsStem wsOwnerStem = stemLookup.get(wsAttributeAssign.getOwnerStemId());
      WsAttributeDef wsOwnerAttributeDef = attributeDefLookup.get(wsAttributeAssign.getOwnerAttributeDefId());
      WsMembership wsOwnerMembership = membershipLookup.get(wsAttributeAssign.getOwnerMembershipId());
      WsGroup wsOwnerGroup = groupLookup.get(wsAttributeAssign.getOwnerGroupId());
      MultiKey ownerSubjectKey = new MultiKey(wsAttributeAssign.getOwnerMemberSourceId(), wsAttributeAssign.getOwnerMemberSubjectId());
      WsSubject wsOwnerMemberSubject = subjectLookup.get(ownerSubjectKey);
      
      String ownerName = null;
      if (GrouperClientUtils.equals("group", wsAttributeAssign.getAttributeAssignType())) {
        ownerName = wsOwnerGroup.getName();
      } else if (GrouperClientUtils.equals("member", wsAttributeAssign.getAttributeAssignType())) {
        ownerName = wsAttributeAssign.getOwnerMemberSourceId() + " - " + wsAttributeAssign.getOwnerMemberSubjectId();
      } else if (GrouperClientUtils.equals("stem", wsAttributeAssign.getAttributeAssignType())) {
        ownerName = wsOwnerStem.getName();
      } else if (GrouperClientUtils.equals("any_mem", wsAttributeAssign.getAttributeAssignType())) {
        ownerName = wsOwnerGroup.getName() + " - " + wsAttributeAssign.getOwnerMemberSourceId() + " - " + wsAttributeAssign.getOwnerMemberSubjectId();
      } else if (GrouperClientUtils.equals("imm_mem", wsAttributeAssign.getAttributeAssignType())) {
        ownerName = wsAttributeAssign.getOwnerMembershipId();
      } else if (GrouperClientUtils.equals("attr_def", wsAttributeAssign.getAttributeAssignType())) {
        ownerName = wsOwnerAttributeDef.getName();
      } else if (wsAttributeAssign.getAttributeAssignType() != null && wsAttributeAssign.getAttributeAssignType().endsWith("_asgn")) {
        ownerName = wsAttributeAssign.getOwnerAttributeAssignId();
      } else {
        throw new RuntimeException("Cant find attribute assign type: " + wsAttributeAssign.getAttributeAssignType());
      }

      String valuesString = "none";
      int valuesLength = GrouperClientUtils.length(wsAttributeAssign.getWsAttributeAssignValues());
      if (valuesLength > 0) {
        StringBuilder valuesResult = new StringBuilder();
        for (int i=0;i<valuesLength;i++) {
          WsAttributeAssignValue wsAttributeAssignValue = wsAttributeAssign.getWsAttributeAssignValues()[i];
          valuesResult.append(wsAttributeAssignValue.getValueSystem());
          if (i<valuesLength-1) {
            valuesResult.append(",");
          }
        }
        valuesString = valuesResult.toString();
      }
      
      substituteMap.put("index", index);
      substituteMap.put("ownerName", ownerName);
      substituteMap.put("valuesString", valuesString);
      substituteMap.put("wsOwnerAttributeDef", wsOwnerAttributeDef);
      substituteMap.put("wsAttributeAssign", wsAttributeAssign);
      substituteMap.put("wsAttributeDef", wsAttributeDef);
      substituteMap.put("wsAttributeDefName", wsAttributeDefName);
      substituteMap.put("wsOwnerMemberSubject", wsOwnerMemberSubject);
      substituteMap.put("wsOwnerMembership", wsOwnerMembership);
      substituteMap.put("wsOwnerGroup", wsOwnerGroup);
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }
    
    return result.toString();
  }

  /**
   * assign attributes
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String assignAttributes(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
  
    GcAssignAttributes gcAssignAttributes = new GcAssignAttributes();        
  
    for (int i=0;i<10;i++) {
      WsSubjectLookup ownerMembershipAnySubjectLookup = retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "ownerMembershipAny" + i, false);
      String ownerMembershipAnyGroupUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "ownerMembershipAny" + i + "GroupUuid", false);
      String ownerMembershipAnyGroupName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "ownerMembershipAny" + i + "GroupName", false);
      if (ownerMembershipAnySubjectLookup != null || !GrouperClientUtils.isBlank(ownerMembershipAnyGroupName) 
          || !GrouperClientUtils.isBlank(ownerMembershipAnyGroupUuid)) {
        WsMembershipAnyLookup wsMembershipAnyLookup = new WsMembershipAnyLookup();
        wsMembershipAnyLookup.setWsGroupLookup(new WsGroupLookup(ownerMembershipAnyGroupName, ownerMembershipAnyGroupUuid));
        wsMembershipAnyLookup.setWsSubjectLookup(ownerMembershipAnySubjectLookup);
        gcAssignAttributes.addOwnerMembershipAnyLookup(wsMembershipAnyLookup);
      }
    }
    
    for (int i=0;i<10;i++) {
      WsSubjectLookup ownerSubjectLookup = retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "owner" + i, false);
      if (ownerSubjectLookup != null) {
        gcAssignAttributes.addOwnerSubjectLookup(ownerSubjectLookup);
      }
    }

    for (int i=0;i<10;i++) {
      String valuesId = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "values" + i + "Id", false);
      String valuesFormatted = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "values" + i + "Formatted", false);
      String valuesSystem = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "values" + i + "System", false);
      if (!GrouperClientUtils.isBlank(valuesId) || !GrouperClientUtils.isBlank(valuesFormatted) 
          || !GrouperClientUtils.isBlank(valuesSystem)) {
        WsAttributeAssignValue wsAttributeAssignValue = new WsAttributeAssignValue();
        wsAttributeAssignValue.setId(valuesId);
        wsAttributeAssignValue.setValueFormatted(valuesFormatted);
        wsAttributeAssignValue.setValueSystem(valuesSystem);
        gcAssignAttributes.addValue(wsAttributeAssignValue);
      }
    }
    
    {
      Set<String> wsOwnerAttributeAssignUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "ownerAttributeAssignUuids", false);
      if (GrouperClientUtils.length(wsOwnerAttributeAssignUuids) > 0) {
        for (String attributeAssignUuid : wsOwnerAttributeAssignUuids) {
          WsAttributeAssignLookup wsAttributeAssignLookup = new WsAttributeAssignLookup();
          wsAttributeAssignLookup.setUuid(attributeAssignUuid);
          gcAssignAttributes.addOwnerAttributeAssignLookup(wsAttributeAssignLookup);
        }
      }
    }

    {
      Timestamp assignmentDisabledTime = GrouperClientUtils.argMapTimestamp(argMap, argMapNotUsed, "assignmentDisabledTime");
      gcAssignAttributes.assignDisabledTime(assignmentDisabledTime);
    }
    
    {
      Timestamp assignmentEnabledTime = GrouperClientUtils.argMapTimestamp(argMap, argMapNotUsed, "assignmentEnabledTime");
      gcAssignAttributes.assignEnabledTime(assignmentEnabledTime);
    }
    
    {
      String assignmentNotes = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "assignmentNotes", false);
      gcAssignAttributes.assignAssignmentNotes(assignmentNotes);
    }
    
    {
      String delegatable = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "delegatable", false);
      gcAssignAttributes.assignDelegatable(delegatable);
    }
    
    {
      String attributeAssignOperation = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "attributeAssignOperation", false);
      gcAssignAttributes.assignAttributeAssignOperation(attributeAssignOperation);
    }
    
    {
      String attributeAssignValueOperation = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "attributeAssignValueOperation", false);
      gcAssignAttributes.assignAttributeAssignValueOperation(attributeAssignValueOperation);
    }
    
    {
      Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
      gcAssignAttributes.assignIncludeSubjectDetail(includeSubjectDetail);
    }
    {
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      gcAssignAttributes.assignIncludeGroupDetail(includeGroupDetail);
    }
    {
      Set<String> attributeAssignUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeAssignUuids", false);
      if (GrouperClientUtils.length(attributeAssignUuids) > 0) {
        for (String attributeAssignUuid : attributeAssignUuids) {
          gcAssignAttributes.addAttributeAssignId(attributeAssignUuid);
        }
      }
    }
    {
      List<String> ownerGroupUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerGroupUuids", false);
      if (GrouperClientUtils.length(ownerGroupUuids) > 0) {
        for (String ownerGroupUuid: ownerGroupUuids) {
          gcAssignAttributes.addOwnerGroupUuid(ownerGroupUuid);
        }
      }
    }
    {
      List<String> ownerGroupNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerGroupNames", false);
      if (GrouperClientUtils.length(ownerGroupNames) > 0) {
        for (String ownerGroupName: ownerGroupNames) {
          gcAssignAttributes.addOwnerGroupName(ownerGroupName);
        }
      }
    }
    {
      List<String> ownerMembershipUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerMembershipUuids", false);
      if (GrouperClientUtils.length(ownerMembershipUuids) > 0) {
        for (String ownerMembershipUuid: ownerMembershipUuids) {
          gcAssignAttributes.addOwnerMembershipId(ownerMembershipUuid);
        }
      }
    }
    {
      List<String> ownerStemUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerStemUuids", false);
      if (GrouperClientUtils.length(ownerStemUuids) > 0) {
        for (String ownerStemUuid: ownerStemUuids) {
          gcAssignAttributes.addOwnerStemUuid(ownerStemUuid);
        }
      }
    }
    {
      List<String> ownerStemNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "ownerStemNames", false);
      if (GrouperClientUtils.length(ownerStemNames) > 0) {
        for (String ownerStemName: ownerStemNames) {
          gcAssignAttributes.addOwnerStemName(ownerStemName);
        }
      }
    }
  
    {
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcAssignAttributes.assignClientVersion(clientVersion);
      
    }
    
    {
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      gcAssignAttributes.assignActAsSubject(actAsSubject);
    }
  
    {
      String attributeAssignType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "attributeAssignType", true);
      gcAssignAttributes.assignAttributeAssignType(attributeAssignType);
    }
    
    { 
      Set<String> attributeDefNameNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNameNames", false);
      
      if (GrouperClientUtils.length(attributeDefNameNames) > 0) {
        for (String attributeDefNameName : attributeDefNameNames) {
          gcAssignAttributes.addAttributeDefNameName(attributeDefNameName);
        }
      }
    }
    
    {
      Set<String> attributeDefNameUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNameUuids", false);
      if (GrouperClientUtils.length(attributeDefNameUuids) > 0) {
        for (String attributeDefNameUuid : attributeDefNameUuids) {
          gcAssignAttributes.addAttributeDefNameUuid(attributeDefNameUuid);
        }
      }
    }
    
    { 
      Set<String> ownerAttributeDefNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "ownerAttributeDefNames", false);
      
      if (GrouperClientUtils.length(ownerAttributeDefNames) > 0) {
        for (String ownerAttributeDefName : ownerAttributeDefNames) {
          gcAssignAttributes.addOwnerAttributeDefName(ownerAttributeDefName);
        }
      }
    }
    
    {
      Set<String> ownerAttributeDefUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "ownerAttributeDefUuids", false);
      if (GrouperClientUtils.length(ownerAttributeDefUuids) > 0) {
        for (String ownerAttributeDefUuid : ownerAttributeDefUuids) {
          gcAssignAttributes.addOwnerAttributeDefUuid(ownerAttributeDefUuid);
        }
      }
    }
  
    {
      Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
      for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
        gcAssignAttributes.addSubjectAttributeName(subjectAttribute);
      }
    }    
    
    {
      Set<String> actions = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "actions", false);
      
      if (GrouperClientUtils.length(actions) > 0) {
        for (String action : actions) {
          gcAssignAttributes.addAction(action);
        }
      }
    }
    
    {
      Set<String> actionsToReplace = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "actionsToReplace", false);
      
      if (GrouperClientUtils.length(actionsToReplace) > 0) {
        for (String actionToReplace : actionsToReplace) {
          gcAssignAttributes.addActionToReplace(actionToReplace);
        }
      }
    }
    
    {
      Set<String> attributeDefTypesToReplace = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefTypesToReplace", false);
      
      if (GrouperClientUtils.length(attributeDefTypesToReplace) > 0) {
        for (String attributeDefTypeToReplace : attributeDefTypesToReplace) {
          gcAssignAttributes.addAttributeDefTypeToReplace(attributeDefTypeToReplace);
        }
      }
    }
    
    {
      Set<String> attributeDefNamesToReplace = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNamesToReplace", false);
      
      if (GrouperClientUtils.length(attributeDefNamesToReplace) > 0) {
        for (String attributeDefNameToReplace : attributeDefNamesToReplace) {
          gcAssignAttributes.addAttributeDefNameToReplace(attributeDefNameToReplace);
        }
      }
    }
    
    {
      Set<String> attributeDefUuidsToReplace = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefUuidsToReplace", false);
      
      if (GrouperClientUtils.length(attributeDefUuidsToReplace) > 0) {
        for (String attributeDefUuidToReplace : attributeDefUuidsToReplace) {
          gcAssignAttributes.addAttributeDefUuidToReplace(attributeDefUuidToReplace);
        }
      }
    }
    
    {
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      for (WsParam param : params) {
        gcAssignAttributes.addParam(param);
      }
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    
    
    failOnArgsNotUsed(argMapNotUsed);
  
    WsAssignAttributesResults wsAssignAttributesResults = gcAssignAttributes.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsAssignAttributesResults", wsAssignAttributesResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.assignAttributes.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsAssignAttributesResults, " +
      "grouperClientUtils, index, wsAttributeAssignment");
  
    //########## GROUPS
    //lets index the groups by groupId 
    Map<String, WsGroup> groupLookup = new HashMap<String, WsGroup>();
    
    for (WsGroup wsGroupCurrent : GrouperClientUtils.nonNull(wsAssignAttributesResults.getWsGroups(), WsGroup.class)) {
      groupLookup.put(wsGroupCurrent.getUuid(), wsGroupCurrent);
    }
    
    //########## SUBJECTS
    //lets index the subjects by multikey of sourceId and subjectId
    Map<MultiKey, WsSubject> subjectLookup = new HashMap<MultiKey, WsSubject>();
    
    for (WsSubject wsSubject : GrouperClientUtils.nonNull(wsAssignAttributesResults.getWsSubjects(), WsSubject.class)) {
      MultiKey key = new MultiKey(wsSubject.getSourceId(), wsSubject.getId());
      subjectLookup.put(key, wsSubject);
    }
  
    //########## STEMS
    //lets index the stems by stemId 
    Map<String, WsStem> stemLookup = new HashMap<String, WsStem>();
    
    for (WsStem wsStemCurrent : GrouperClientUtils.nonNull(wsAssignAttributesResults.getWsStems(), WsStem.class)) {
      stemLookup.put(wsStemCurrent.getUuid(), wsStemCurrent);
    }
    
    //########## ATTRIBUTE DEFS
    //lets index the attributeDefs by attributeDefId 
    Map<String, WsAttributeDef> attributeDefLookup = new HashMap<String, WsAttributeDef>();
    
    for (WsAttributeDef wsAttributeDefCurrent : GrouperClientUtils.nonNull(wsAssignAttributesResults.getWsAttributeDefs(), WsAttributeDef.class)) {
      attributeDefLookup.put(wsAttributeDefCurrent.getUuid(), wsAttributeDefCurrent);
    }
    
    //########## ATTRIBUTE DEF NAMES
    //lets index the attributeDefNames by attributeDefNameId 
    Map<String, WsAttributeDefName> attributeDefNameLookup = new HashMap<String, WsAttributeDefName>();
    
    for (WsAttributeDefName wsAttributeDefNameCurrent : GrouperClientUtils.nonNull(wsAssignAttributesResults.getWsAttributeDefNames(), WsAttributeDefName.class)) {
      attributeDefNameLookup.put(wsAttributeDefNameCurrent.getUuid(), wsAttributeDefNameCurrent);
    }
    
    //########## MEMBERSHIPS
    //lets index the memberships by membership id and membership immediate id (if immediate) 
    Map<String, WsMembership> membershipLookup = new HashMap<String, WsMembership>();
    
    for (WsMembership wsMembershipCurrent : GrouperClientUtils.nonNull(wsAssignAttributesResults.getWsMemberships(), WsMembership.class)) {
      membershipLookup.put(wsMembershipCurrent.getMembershipId(), wsMembershipCurrent);
      if ("immediate".equals(wsMembershipCurrent.getMembershipType())) {
        membershipLookup.put(wsMembershipCurrent.getImmediateMembershipId(), wsMembershipCurrent);
      }
    }
    
    
    for (WsAssignAttributeResult wsAssignAttributeResult : GrouperClientUtils.nonNull(wsAssignAttributesResults.getWsAttributeAssignResults(), WsAssignAttributeResult.class)) {
    
      substituteMap.put("wsAssignAttributeResult", wsAssignAttributeResult);

      for (WsAttributeAssign wsAttributeAssign : GrouperClientUtils.nonNull(wsAssignAttributeResult.getWsAttributeAssigns(), WsAttributeAssign.class)) {
        
        WsAttributeDef wsAttributeDef = attributeDefLookup.get(wsAttributeAssign.getAttributeDefId());
        WsAttributeDefName wsAttributeDefName = attributeDefNameLookup.get(wsAttributeAssign.getAttributeDefNameId());
        WsStem wsOwnerStem = stemLookup.get(wsAttributeAssign.getOwnerStemId());
        WsAttributeDef wsOwnerAttributeDef = attributeDefLookup.get(wsAttributeAssign.getOwnerAttributeDefId());
        WsMembership wsOwnerMembership = membershipLookup.get(wsAttributeAssign.getOwnerMembershipId());
        WsGroup wsOwnerGroup = groupLookup.get(wsAttributeAssign.getOwnerGroupId());
        MultiKey ownerSubjectKey = new MultiKey(wsAttributeAssign.getOwnerMemberSourceId(), wsAttributeAssign.getOwnerMemberSubjectId());
        WsSubject wsOwnerMemberSubject = subjectLookup.get(ownerSubjectKey);
        
        String ownerName = null;
        if (GrouperClientUtils.equals("group", wsAttributeAssign.getAttributeAssignType())) {
          ownerName = wsOwnerGroup.getName();
        } else if (GrouperClientUtils.equals("member", wsAttributeAssign.getAttributeAssignType())) {
          ownerName = wsAttributeAssign.getOwnerMemberSourceId() + " - " + wsAttributeAssign.getOwnerMemberSubjectId();
        } else if (GrouperClientUtils.equals("stem", wsAttributeAssign.getAttributeAssignType())) {
          ownerName = wsOwnerStem.getName();
        } else if (GrouperClientUtils.equals("any_mem", wsAttributeAssign.getAttributeAssignType())) {
          ownerName = wsOwnerGroup.getName() + " - " + wsAttributeAssign.getOwnerMemberSourceId() + " - " + wsAttributeAssign.getOwnerMemberSubjectId();
        } else if (GrouperClientUtils.equals("imm_mem", wsAttributeAssign.getAttributeAssignType())) {
          ownerName = wsAttributeAssign.getOwnerMembershipId();
        } else if (GrouperClientUtils.equals("attr_def", wsAttributeAssign.getAttributeAssignType())) {
          ownerName = wsOwnerAttributeDef.getName();
        } else if (wsAttributeAssign.getAttributeAssignType() != null && wsAttributeAssign.getAttributeAssignType().endsWith("_asgn")) {
          ownerName = wsAttributeAssign.getOwnerAttributeAssignId();
        } else {
          throw new RuntimeException("Cant find attribute assign type: " + wsAttributeAssign.getAttributeAssignType());
        }
    
        String valuesString = "none";
        int valuesLength = GrouperClientUtils.length(wsAttributeAssign.getWsAttributeAssignValues());
        if (valuesLength > 0) {
          StringBuilder valuesResult = new StringBuilder();
          for (int i=0;i<valuesLength;i++) {
            WsAttributeAssignValue wsAttributeAssignValue = wsAttributeAssign.getWsAttributeAssignValues()[i];
            valuesResult.append(wsAttributeAssignValue.getValueSystem());
            if (i<valuesLength-1) {
              valuesResult.append(",");
            }
          }
          valuesString = valuesResult.toString();
        }
        
        substituteMap.put("index", index);
        substituteMap.put("ownerName", ownerName);
        substituteMap.put("valuesString", valuesString);
        substituteMap.put("wsOwnerAttributeDef", wsOwnerAttributeDef);
        substituteMap.put("wsAttributeAssign", wsAttributeAssign);
        substituteMap.put("wsAttributeDef", wsAttributeDef);
        substituteMap.put("wsAttributeDefName", wsAttributeDefName);
        substituteMap.put("wsOwnerMemberSubject", wsOwnerMemberSubject);
        substituteMap.put("wsOwnerMembership", wsOwnerMembership);
        substituteMap.put("wsOwnerGroup", wsOwnerGroup);
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
    }    
    return result.toString();
  }

  /**
   * get permissionAssignments
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String getPermissionAssignments(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
  
    GcGetPermissionAssignments gcGetPermissionAssignments = new GcGetPermissionAssignments();        
  
    for (int i=0;i<10;i++) {
      WsSubjectLookup subjectLookup = retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "subject" + i, false);
      if (subjectLookup != null) {
        gcGetPermissionAssignments.addSubjectLookup(subjectLookup);
      }
    }
    
    {
      Boolean includeAssignmentsOnAssignments = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeAssignmentsOnAssignments");
      gcGetPermissionAssignments.assignIncludeAssignmentsOnAssignments(includeAssignmentsOnAssignments);
    }
    {
      Boolean includeAttributeAssignments = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeAttributeAssignments");
      gcGetPermissionAssignments.assignIncludeAttributeAssignments(includeAttributeAssignments);
    }
    {
      Boolean includeAttributeDefNames = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeAttributeDefNames");
      gcGetPermissionAssignments.assignIncludeAttributeDefNames(includeAttributeDefNames);
    }
    {
      Boolean includePermissionAssignDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includePermissionAssignDetail");
      gcGetPermissionAssignments.assignIncludePermissionAssignDetail(includePermissionAssignDetail);
    }
    {
      Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
      gcGetPermissionAssignments.assignIncludeSubjectDetail(includeSubjectDetail);
    }
    {
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      gcGetPermissionAssignments.assignIncludeGroupDetail(includeGroupDetail);
    }
    {
      List<String> roleUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "roleUuids", false);
      if (GrouperClientUtils.length(roleUuids) > 0) {
        for (String ownerGroupUuid: roleUuids) {
          gcGetPermissionAssignments.addRoleUuid(ownerGroupUuid);
        }
      }
    }
    {
      List<String> roleNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "roleNames", false);
      if (GrouperClientUtils.length(roleNames) > 0) {
        for (String ownerGroupName: roleNames) {
          gcGetPermissionAssignments.addRoleName(ownerGroupName);
        }
      }
    }
    {
      String enabled = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "enabled", false);
      gcGetPermissionAssignments.assignEnabled(enabled);
    }
  
    {
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcGetPermissionAssignments.assignClientVersion(clientVersion);
      
    }
    
    {
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      gcGetPermissionAssignments.assignActAsSubject(actAsSubject);
    }
  
    { 
      Set<String> attributeDefNameNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNameNames", false);
      
      if (GrouperClientUtils.length(attributeDefNameNames) > 0) {
        for (String attributeDefNameName : attributeDefNameNames) {
          gcGetPermissionAssignments.addAttributeDefNameName(attributeDefNameName);
        }
      }
    }
    
    {
      Set<String> attributeDefNameUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNameUuids", false);
      if (GrouperClientUtils.length(attributeDefNameUuids) > 0) {
        for (String attributeDefNameUuid : attributeDefNameUuids) {
          gcGetPermissionAssignments.addAttributeDefNameUuid(attributeDefNameUuid);
        }
      }
    }
    
    { 
      Set<String> attributeDefNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNames", false);
      
      if (GrouperClientUtils.length(attributeDefNames) > 0) {
        for (String attributeDefName : attributeDefNames) {
          gcGetPermissionAssignments.addAttributeDefName(attributeDefName);
        }
      }
    }
    
    {
      Set<String> attributeDefUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefUuids", false);
      if (GrouperClientUtils.length(attributeDefUuids) > 0) {
        for (String attributeDefUuid : attributeDefUuids) {
          gcGetPermissionAssignments.addAttributeDefUuid(attributeDefUuid);
        }
      }
    }
  
    {
      Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
      for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
        gcGetPermissionAssignments.addSubjectAttributeName(subjectAttribute);
      }
    }    
    
    {
      Set<String> actions = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "actions", false);
      
      if (GrouperClientUtils.length(actions) > 0) {
        for (String action : actions) {
          gcGetPermissionAssignments.addAction(action);
        }
      }
    }
    
    
    
    {
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      for (WsParam param : params) {
        gcGetPermissionAssignments.addParam(param);
      }
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    
    
    failOnArgsNotUsed(argMapNotUsed);
  
    WsGetPermissionAssignmentsResults wsPermissionAssignmentsResults = gcGetPermissionAssignments.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsPermissionAssignmentsResults", wsPermissionAssignmentsResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.getPermissionAssignments.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsPermissionAssignmentsResults, " +
      "grouperClientUtils, index, wsAttributeAssignment, wsPermissionAssign, wsAttributeDef, wsAttributeDefName, wsSubject, role");
  
    //########## GROUPS
    //lets index the groups by groupId 
    Map<String, WsGroup> groupLookup = new HashMap<String, WsGroup>();
    
    for (WsGroup wsGroupCurrent : GrouperClientUtils.nonNull(wsPermissionAssignmentsResults.getWsGroups(), WsGroup.class)) {
      groupLookup.put(wsGroupCurrent.getUuid(), wsGroupCurrent);
    }
    
    //########## SUBJECTS
    //lets index the subjects by multikey of sourceId and subjectId
    Map<MultiKey, WsSubject> subjectLookup = new HashMap<MultiKey, WsSubject>();
    
    for (WsSubject wsSubject : GrouperClientUtils.nonNull(wsPermissionAssignmentsResults.getWsSubjects(), WsSubject.class)) {
      MultiKey key = new MultiKey(wsSubject.getSourceId(), wsSubject.getId());
      subjectLookup.put(key, wsSubject);
    }
  
    //########## ATTRIBUTE DEFS
    //lets index the attributeDefs by attributeDefId 
    Map<String, WsAttributeDef> attributeDefLookup = new HashMap<String, WsAttributeDef>();
    
    for (WsAttributeDef wsAttributeDefCurrent : GrouperClientUtils.nonNull(wsPermissionAssignmentsResults.getWsAttributeDefs(), WsAttributeDef.class)) {
      attributeDefLookup.put(wsAttributeDefCurrent.getUuid(), wsAttributeDefCurrent);
    }
    
    //########## ATTRIBUTE DEF NAMES
    //lets index the attributeDefNames by attributeDefNameId 
    Map<String, WsAttributeDefName> attributeDefNameLookup = new HashMap<String, WsAttributeDefName>();
    
    for (WsAttributeDefName wsAttributeDefNameCurrent : GrouperClientUtils.nonNull(wsPermissionAssignmentsResults.getWsAttributeDefNames(), WsAttributeDefName.class)) {
      attributeDefNameLookup.put(wsAttributeDefNameCurrent.getUuid(), wsAttributeDefNameCurrent);
    }    
    
    //######### ATTRIBUTE ASSIGNS
    Map<String, WsAttributeAssign> attributeAssignLookup = new HashMap<String, WsAttributeAssign>();
    
    for (WsAttributeAssign wsAttributeAssignCurrent : GrouperClientUtils.nonNull(wsPermissionAssignmentsResults.getWsAttributeAssigns(), WsAttributeAssign.class)) {
      attributeAssignLookup.put(wsAttributeAssignCurrent.getId(), wsAttributeAssignCurrent);
    }    
    
    for (WsPermissionAssign wsPermissionAssign : GrouperClientUtils.nonNull(wsPermissionAssignmentsResults.getWsPermissionAssigns(), WsPermissionAssign.class)) {
      
      WsAttributeAssign wsAttributeAssign = attributeAssignLookup.get(wsPermissionAssign.getAttributeDefId());
      WsAttributeDef wsAttributeDef = attributeDefLookup.get(wsPermissionAssign.getAttributeDefId());
      WsAttributeDefName wsAttributeDefName = attributeDefNameLookup.get(wsPermissionAssign.getAttributeDefNameId());
      WsGroup role = groupLookup.get(wsPermissionAssign.getRoleId());
      MultiKey subjectKey = new MultiKey(wsPermissionAssign.getSourceId(), wsPermissionAssign.getSubjectId());
      WsSubject wsSubject = subjectLookup.get(subjectKey);
      
      substituteMap.put("index", index);
      substituteMap.put("wsAttributeAssign", wsAttributeAssign);
      substituteMap.put("wsPermissionAssign", wsPermissionAssign);
      substituteMap.put("wsAttributeDef", wsAttributeDef);
      substituteMap.put("wsAttributeDefName", wsAttributeDefName);
      substituteMap.put("wsSubject", wsSubject);
      substituteMap.put("role", role);
      
      String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
      result.append(output);
      
      index++;
    }
    
    return result.toString();
  }

  /**
   * assign permissions
   * @param argMap
   * @param argMapNotUsed
   * @return result
   */
  private static String assignPermissions(Map<String, String> argMap,
      Map<String, String> argMapNotUsed) {
  
    GcAssignPermissions gcAssignPermissions = new GcAssignPermissions();        
  
    for (int i=0;i<10;i++) {
      WsSubjectLookup subjectRoleSubjectLookup = retrieveSuffixSubjectFromArgs(argMap, argMapNotUsed, "subjectRole" + i, false);
      String subjectRoleUuid = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "subjectRole" + i + "RoleUuid", false);
      String subjectRoleName = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "subjectRole" + i + "RoleName", false);
      if (subjectRoleSubjectLookup != null || !GrouperClientUtils.isBlank(subjectRoleName) 
          || !GrouperClientUtils.isBlank(subjectRoleUuid)) {
        WsMembershipAnyLookup subjectRoleLookup = new WsMembershipAnyLookup();
        subjectRoleLookup.setWsGroupLookup(new WsGroupLookup(subjectRoleName, subjectRoleUuid));
        subjectRoleLookup.setWsSubjectLookup(subjectRoleSubjectLookup);
        gcAssignPermissions.addSubjectRoleLookup(subjectRoleLookup);
      }
    }
    
    {
      Timestamp assignmentDisabledTime = GrouperClientUtils.argMapTimestamp(argMap, argMapNotUsed, "assignmentDisabledTime");
      gcAssignPermissions.assignDisabledTime(assignmentDisabledTime);
    }
    
    {
      Timestamp assignmentEnabledTime = GrouperClientUtils.argMapTimestamp(argMap, argMapNotUsed, "assignmentEnabledTime");
      gcAssignPermissions.assignEnabledTime(assignmentEnabledTime);
    }
    
    {
      String assignmentNotes = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "assignmentNotes", false);
      gcAssignPermissions.assignAssignmentNotes(assignmentNotes);
    }
    
    {
      String delegatable = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "delegatable", false);
      gcAssignPermissions.assignDelegatable(delegatable);
    }
    
    {
      String permissionAssignOperation = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "permissionAssignOperation", false);
      gcAssignPermissions.assignPermissionAssignOperation(permissionAssignOperation);
    }
    
    {
      Boolean includeSubjectDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeSubjectDetail");
      gcAssignPermissions.assignIncludeSubjectDetail(includeSubjectDetail);
    }
    {
      Boolean includeGroupDetail = GrouperClientUtils.argMapBoolean(argMap, argMapNotUsed, "includeGroupDetail");
      gcAssignPermissions.assignIncludeGroupDetail(includeGroupDetail);
    }
    {
      Set<String> attributeAssignUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeAssignUuids", false);
      if (GrouperClientUtils.length(attributeAssignUuids) > 0) {
        for (String attributeAssignUuid : attributeAssignUuids) {
          gcAssignPermissions.addAttributeAssignId(attributeAssignUuid);
        }
      }
    }
    {
      List<String> roleUuids = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "roleUuids", false);
      if (GrouperClientUtils.length(roleUuids) > 0) {
        for (String roleUuid: roleUuids) {
          gcAssignPermissions.addRoleUuid(roleUuid);
        }
      }
    }
    {
      List<String> roleNames = GrouperClientUtils.argMapList(argMap, argMapNotUsed, "roleNames", false);
      if (GrouperClientUtils.length(roleNames) > 0) {
        for (String roleName: roleNames) {
          gcAssignPermissions.addRoleName(roleName);
        }
      }
    }
    {
      String clientVersion = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "clientVersion", false);
      gcAssignPermissions.assignClientVersion(clientVersion);
      
    }
    
    {
      WsSubjectLookup actAsSubject = retrieveActAsSubjectFromArgs(argMap, argMapNotUsed);
      gcAssignPermissions.assignActAsSubject(actAsSubject);
    }
  
    {
      String permissionType = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "permissionType", true);
      gcAssignPermissions.assignPermissionType(permissionType);
    }
    
    { 
      Set<String> permissionDefNameNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "permissionDefNameNames", false);
      
      if (GrouperClientUtils.length(permissionDefNameNames) > 0) {
        for (String permissionDefNameName : permissionDefNameNames) {
          gcAssignPermissions.addPermissionDefNameName(permissionDefNameName);
        }
      }
    }
    
    {
      Set<String> permissionDefNameUuids = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "permissionDefNameUuids", false);
      if (GrouperClientUtils.length(permissionDefNameUuids) > 0) {
        for (String permissionDefNameUuid : permissionDefNameUuids) {
          gcAssignPermissions.addPermissionDefNameUuid(permissionDefNameUuid);
        }
      }
    }
    
    {
      Set<String> subjectAttributeNames = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "subjectAttributeNames", false);
  
      for (String subjectAttribute : GrouperClientUtils.nonNull(subjectAttributeNames)) {
        gcAssignPermissions.addSubjectAttributeName(subjectAttribute);
      }
    }    
    
    {
      Set<String> actions = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "actions", false);
      
      if (GrouperClientUtils.length(actions) > 0) {
        for (String action : actions) {
          gcAssignPermissions.addAction(action);
        }
      }
    }
    
    {
      Set<String> actionsToReplace = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "actionsToReplace", false);
      
      if (GrouperClientUtils.length(actionsToReplace) > 0) {
        for (String actionToReplace : actionsToReplace) {
          gcAssignPermissions.addActionToReplace(actionToReplace);
        }
      }
    }
    
    {
      Set<String> attributeDefNamesToReplace = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefNamesToReplace", false);
      
      if (GrouperClientUtils.length(attributeDefNamesToReplace) > 0) {
        for (String attributeDefNameToReplace : attributeDefNamesToReplace) {
          gcAssignPermissions.addAttributeDefNameToReplace(attributeDefNameToReplace);
        }
      }
    }
    
    {
      Set<String> attributeDefUuidsToReplace = GrouperClientUtils.argMapSet(argMap, argMapNotUsed, "attributeDefUuidsToReplace", false);
      
      if (GrouperClientUtils.length(attributeDefUuidsToReplace) > 0) {
        for (String attributeDefUuidToReplace : attributeDefUuidsToReplace) {
          gcAssignPermissions.addAttributeDefUuidToReplace(attributeDefUuidToReplace);
        }
      }
    }

    
    {
      List<WsParam> params = retrieveParamsFromArgs(argMap, argMapNotUsed);
      
      for (WsParam param : params) {
        gcAssignPermissions.addParam(param);
      }
    }
    
    //register that we will use this
    GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", false);
    
    
    failOnArgsNotUsed(argMapNotUsed);
  
    WsAssignPermissionsResults wsAssignPermissionsResults = gcAssignPermissions.execute();
    
    StringBuilder result = new StringBuilder();
    int index = 0;
    
    Map<String, Object> substituteMap = new LinkedHashMap<String, Object>();
  
    substituteMap.put("wsAssignPermissionsResults", wsAssignPermissionsResults);
    substituteMap.put("grouperClientUtils", new GrouperClientUtils());
  
    String outputTemplate = null;
  
    if (argMap.containsKey("outputTemplate")) {
      outputTemplate = GrouperClientUtils.argMapString(argMap, argMapNotUsed, "outputTemplate", true);
      outputTemplate = GrouperClientUtils.substituteCommonVars(outputTemplate);
    } else {
      outputTemplate = GrouperClientUtils.propertiesValue("webService.assignPermissions.output", true);
    }
    log.debug("Output template: " + outputTemplate + ", available variables: wsAssignPermissionsResults, " +
      "grouperClientUtils, index, wsAssignPermissionResult, ownerName, wsAttributeAssign, " +
      "wsAttributeDef, wsAttributeDefName, wsOwnerGroup, permissionType");
  
    //########## GROUPS
    //lets index the groups by groupId 
    Map<String, WsGroup> groupLookup = new HashMap<String, WsGroup>();
    
    for (WsGroup wsGroupCurrent : GrouperClientUtils.nonNull(wsAssignPermissionsResults.getWsGroups(), WsGroup.class)) {
      groupLookup.put(wsGroupCurrent.getUuid(), wsGroupCurrent);
    }
    
    //########## SUBJECTS
    //lets index the subjects by multikey of sourceId and subjectId
    Map<MultiKey, WsSubject> subjectLookup = new HashMap<MultiKey, WsSubject>();
    
    for (WsSubject wsSubject : GrouperClientUtils.nonNull(wsAssignPermissionsResults.getWsSubjects(), WsSubject.class)) {
      MultiKey key = new MultiKey(wsSubject.getSourceId(), wsSubject.getId());
      subjectLookup.put(key, wsSubject);
    }
  
    //########## ATTRIBUTE DEFS
    //lets index the attributeDefs by attributeDefId 
    Map<String, WsAttributeDef> attributeDefLookup = new HashMap<String, WsAttributeDef>();
    
    for (WsAttributeDef wsAttributeDefCurrent : GrouperClientUtils.nonNull(wsAssignPermissionsResults.getWsAttributeDefs(), WsAttributeDef.class)) {
      attributeDefLookup.put(wsAttributeDefCurrent.getUuid(), wsAttributeDefCurrent);
    }
    
    //########## ATTRIBUTE DEF NAMES
    //lets index the attributeDefNames by attributeDefNameId 
    Map<String, WsAttributeDefName> attributeDefNameLookup = new HashMap<String, WsAttributeDefName>();
    
    for (WsAttributeDefName wsAttributeDefNameCurrent : GrouperClientUtils.nonNull(wsAssignPermissionsResults.getWsAttributeDefNames(), WsAttributeDefName.class)) {
      attributeDefNameLookup.put(wsAttributeDefNameCurrent.getUuid(), wsAttributeDefNameCurrent);
    }
    
    for (WsAssignPermissionResult wsAssignPermissionResult : 
        GrouperClientUtils.nonNull(wsAssignPermissionsResults.getWsAssignPermissionResults(), WsAssignPermissionResult.class)) {
    
      substituteMap.put("wsAssignPermissionResult", wsAssignPermissionResult);
  
      for (WsAttributeAssign wsAttributeAssign : GrouperClientUtils.nonNull(wsAssignPermissionResult.getWsAttributeAssigns(), WsAttributeAssign.class)) {
        
        WsAttributeDef wsAttributeDef = attributeDefLookup.get(wsAttributeAssign.getAttributeDefId());
        WsAttributeDefName wsAttributeDefName = attributeDefNameLookup.get(wsAttributeAssign.getAttributeDefNameId());
        WsGroup wsOwnerGroup = groupLookup.get(wsAttributeAssign.getOwnerGroupId());
        
        String ownerName = null;
        String permissionType = null;
        //permissions only have two types of assignments with no values
        if (GrouperClientUtils.equals("group", wsAttributeAssign.getAttributeAssignType())) {
          ownerName = wsOwnerGroup.getName();
          permissionType = "role";
        } else if (GrouperClientUtils.equals("any_mem", wsAttributeAssign.getAttributeAssignType())) {
          ownerName = wsOwnerGroup.getName() + " - " + wsAttributeAssign.getOwnerMemberSourceId() + " - " + wsAttributeAssign.getOwnerMemberSubjectId();
          permissionType = "role_subject";
        } else {
          throw new RuntimeException("Cant find attribute assign type: " + wsAttributeAssign.getAttributeAssignType());
        }
    
        substituteMap.put("permissionType", permissionType);
        substituteMap.put("index", index);
        substituteMap.put("ownerName", ownerName);
        substituteMap.put("wsAttributeAssign", wsAttributeAssign);
        substituteMap.put("wsAttributeDef", wsAttributeDef);
        substituteMap.put("wsAttributeDefName", wsAttributeDefName);
        substituteMap.put("wsOwnerGroup", wsOwnerGroup);
        
        String output = GrouperClientUtils.substituteExpressionLanguage(outputTemplate, substituteMap);
        result.append(output);
        
        index++;
      }
    }    
    return result.toString();
  }

}