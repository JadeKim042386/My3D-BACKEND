package joo.project.my3dbackend.fixture;

import joo.project.my3dbackend.domain.Address;
import joo.project.my3dbackend.domain.UserAccount;
import joo.project.my3dbackend.domain.constants.UserRole;

public class Fixture {

    public static UserAccount createUserAccount(
            String email, String password, String nickname, String phone, Address address, UserRole userRole) {
        return UserAccount.of(email, password, nickname, phone, address, userRole);
    }

    public static UserAccount createUserAccount() {
        return createUserAccount(
                "testUser@gmail.com", "pw", "testUser", "01012341234", Address.of("12345", "street", "detail"), UserRole.USER);
    }
}
