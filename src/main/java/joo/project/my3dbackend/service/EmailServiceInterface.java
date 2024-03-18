package joo.project.my3dbackend.service;

public interface EmailServiceInterface {
    /**
     * 비동기 이메일 전송
     * @param to 수신자
     * @param subject 제목
     * @param text 본문
     */
    void sendAsyncEmail(String to, String subject, String text);

    /**
     * 임시 비밀번호 전송
     * @param toEmail 수신자
     * @param subject 제목
     * @param password 비밀번호
     */
    void sendRandomPassword(String toEmail, String subject, String password);

    /**
     * 이메일이 정상적으로 전송되었는지 확인
     * 이메일 형식은 유효하지만 존재하지 않을 경우 반송되기 때문에 INBOX를 확인하여 반송된 메일이 있는지 확인합니다.
     * 반송된 메일이 존재할 경우 false를 반환하고 삭제하며, 없으면 true를 반환합니다.
     */
    boolean isCompleteSentEmail(String email);
}
