package edu.internet2.middleware.grouperClient.ws.beans;

/**
 * returned from the group find query
 * 
 * @author mchyzer
 * 
 */
public class WsFindGroupsResults implements WsResponseBean, ResultMetadataHolder {

  /**
   * has 0 to many groups that match the query
   */
  private WsGroup[] groupResults;

  /**
   * metadata about the result
   */
  private WsResultMeta resultMetadata = new WsResultMeta();

  /**
   * metadata about the result
   */
  private WsResponseMeta responseMetadata = new WsResponseMeta();

  /**
   * has 0 to many groups that match the query by example
   * 
   * @return the groupResults
   */
  public WsGroup[] getGroupResults() {
    return this.groupResults;
  }

  /**
   * basic results to the query
   * @param groupResults1 the groupResults to set
   */
  public void setGroupResults(WsGroup[] groupResults1) {
    this.groupResults = groupResults1;
  }

  /**
   * @return the resultMetadata
   */
  public WsResultMeta getResultMetadata() {
    return this.resultMetadata;
  }

  /**
   * @see edu.internet2.middleware.grouper.ws.rest.WsResponseBean#getResponseMetadata()
   * @return the response metadata
   */
  public WsResponseMeta getResponseMetadata() {
    return this.responseMetadata;
  }

  /**
   * @param responseMetadata1 the responseMetadata to set
   */
  public void setResponseMetadata(WsResponseMeta responseMetadata1) {
    this.responseMetadata = responseMetadata1;
  }

  
  /**
   * @param resultMetadata1 the resultMetadata to set
   */
  public void setResultMetadata(WsResultMeta resultMetadata1) {
    this.resultMetadata = resultMetadata1;
  }
}
