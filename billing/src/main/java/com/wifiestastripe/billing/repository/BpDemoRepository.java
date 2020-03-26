
  package com.wifiestastripe.billing.repository;
  
  import java.util.Collections; import java.util.List; import
  java.util.stream.Collectors;
  
  import javax.annotation.PostConstruct;
  
  import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
  org.springframework.stereotype.Repository;
  
  import com.couchbase.client.core.lang.backport.java.util.Objects; import
  com.couchbase.client.java.Bucket; import com.couchbase.client.java.Cluster;
  import com.couchbase.client.java.CouchbaseCluster; import
  com.couchbase.client.java.document.JsonDocument; import
  com.couchbase.client.java.document.json.JsonObject; import
  com.couchbase.client.java.query.N1qlQuery; import
  com.couchbase.client.java.query.N1qlQueryResult; import
  com.couchbase.client.java.query.N1qlQueryRow;
  
  @Repository public class BpDemoRepository {
  
  private static final String couchBaseNode = "172.31.49.112"; private static
  final String bucketName = "places"; private static final String
  bucketPassword = "f1estA17!WIFI17";
  
  
  private Cluster cluster; private Bucket bucket;
  
  private final Logger logger =
  LoggerFactory.getLogger(BpDemoRepository.class);
  
  public enum Arguments { bpId, pid, placeIds, couponStatus }
  
  @PostConstruct public void init() { cluster =
  CouchbaseCluster.create(couchBaseNode); bucket =
  cluster.openBucket(bucketName, bucketPassword); }
  
  public String getBusinessProfile(String bpId) { String bpKey =
  String.format("bp:%s", bpId); logger.info("bpKey : {}", bpKey);
  
  JsonDocument bpDoc = bucket.get(bpKey); logger.info("bpDoc: {}", bpDoc);
  
  return bpDoc == null ? null : bpDoc.toString(); }
  
  private static final String GET_ALL_BRANCHES_BY_BP = new StringBuilder(1024)
  .append("SELECT pbrnch.pid \n") .append("FROM places pbrnch \n")
  .append("WHERE pbrnch.tp = 'pbrnch' \n")
  .append("AND pbrnch.bpid = $bpId \n") .toString();
  
  public List<String> getBpBranches(String bpId) { JsonObject placeHolderValues
  = JsonObject.create().put(Arguments.bpId.name(), bpId); N1qlQuery query =
  N1qlQuery.parameterized(GET_ALL_BRANCHES_BY_BP, placeHolderValues);
  
  N1qlQueryResult results = bucket.query(query);
  if(!results.errors().isEmpty()) { logger.error(GET_ALL_BRANCHES_BY_BP);
  return Collections.emptyList(); }
  
  List<N1qlQueryRow> allRows = results.allRows(); List<String> placeIds =
  allRows.stream().map( row -> row.value().getString(Arguments.pid.name())
  ).collect(Collectors.toList());
  
  logger.info("placeIds : {}", placeIds); return placeIds; }
  
  private static final String GET_COUPONS_BY_PLACES = new StringBuilder(1024)
  .append("SELECT coupon \n") .append("FROM places coupon \n")
  .append("WHERE coupon.tp = 'coup' \n")
  .append("AND coupon.pid IN $placeIds \n")
  .append("AND coupon.status = $couponStatus") .toString();
  
  public List<String> getBranchCoupons(List<String> bpBranches) { JsonObject
  placeHolderValues = JsonObject.create().put(Arguments.placeIds.name(),
  bpBranches) .put(Arguments.couponStatus.name(), "vali");
  
  N1qlQuery query = N1qlQuery.parameterized(GET_COUPONS_BY_PLACES,
  placeHolderValues); N1qlQueryResult result = bucket.query(query);
  logger.info("GET_COUPONS_BY_PLACES : {}", GET_COUPONS_BY_PLACES); if
  (!result.errors().isEmpty()) { logger.error(GET_COUPONS_BY_PLACES); return
  Collections.emptyList(); }
  
  List<N1qlQueryRow> allRows = result.allRows(); List<String> couponsAsString =
  allRows.stream().map(row ->
  row.value().toString()).collect(Collectors.toList());
  logger.info("Coupons : {}", couponsAsString); return couponsAsString; }
  
  public String getStripeCustomerId(String bpId) { String bpStripeCustomerKey =
  String.format("bp:%s:stripe:cus:usd", bpId); JsonDocument doc =
  bucket.get(bpStripeCustomerKey);
  logger.info("Stripe Customer : {}",doc.content().toString()); if(doc != null)
  { return doc.content().getString("cusid"); } return null; } }
 