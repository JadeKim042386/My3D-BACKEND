package joo.project.my3dbackend.service;

public interface EmailServiceInterface {
    /**
     * @param to 수신자
     * @param subject 제목
     * @param text 본문
     */
    void sendEmail(String to, String subject, String text);
}
