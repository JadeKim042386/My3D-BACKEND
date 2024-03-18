package joo.project.my3dbackend.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class PasswordGenerator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%&*()_+-=[]|,./?><";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateRandomPassword() {
        int minLength = 8, maxLength = 20;
        int length = RANDOM.nextInt((maxLength - minLength) + 1) + minLength;
        ArrayList<Character> passwordCharacters = new ArrayList<>();
        // 영문자, 숫자, 특수문자를 각각 2개씩 추가 (최소 길이를 가지는 비밀번호를 기본으로 생성)
        for (int i = 0; i < 2; i++) {
            passwordCharacters.add(CHAR_LOWER.charAt(RANDOM.nextInt(CHAR_LOWER.length())));
            passwordCharacters.add(CHAR_UPPER.charAt(RANDOM.nextInt(CHAR_UPPER.length())));
            passwordCharacters.add(NUMBER.charAt(RANDOM.nextInt(NUMBER.length())));
            passwordCharacters.add(SPECIAL_CHAR.charAt(RANDOM.nextInt(SPECIAL_CHAR.length())));
        }

        // 나머지 글자를 무작위로 추가
        String allCharacter = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHAR;
        for (int i = 0; i < length - minLength; i++) {
            passwordCharacters.add(allCharacter.charAt(RANDOM.nextInt(allCharacter.length())));
        }
        // 무작위로 섞음
        Collections.shuffle(passwordCharacters, RANDOM);

        // 문자열로 변환
        StringBuilder password = new StringBuilder(length);
        for (char passwordCharacter : passwordCharacters) {
            password.append(passwordCharacter);
        }

        return password.toString();
    }
}
