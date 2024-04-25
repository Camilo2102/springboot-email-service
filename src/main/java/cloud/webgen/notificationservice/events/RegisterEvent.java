package cloud.webgen.notificationservice.events;

public record RegisterEvent(String userId,  String mail, String subject, String template){
}
