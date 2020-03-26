
  package com.wifiestastripe.billing.controller;
  
  import javax.websocket.server.PathParam;
  
  import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
  org.springframework.beans.factory.annotation.Autowired; import
  org.springframework.web.bind.annotation.GetMapping; import
  org.springframework.web.bind.annotation.PathVariable; import
  org.springframework.web.bind.annotation.RequestMapping; import
  org.springframework.web.bind.annotation.RestController;
  
  import com.wifiestastripe.billing.service.BpDemoService;
  
  @RestController
  
  @RequestMapping("/demo") public class BpDemoController {
  
  private static Logger logger =
  LoggerFactory.getLogger(BpDemoController.class);
  
  @Autowired BpDemoService bpDemoService;
  
  @GetMapping("/") public void healthcheck() {
  logger.info("Healthcheck of {}: at {}:", this.getClass().getSimpleName(),
  System.currentTimeMillis()); }
  
  @GetMapping("/bill/{id}") public void generateBpBill(@PathVariable("id")
  String bpId) { try { bpDemoService.generateBill(bpId); } catch (Exception e)
  { e.printStackTrace(); } } }
 