package Service;

import org.junit.Test;
import service.UserService;

import static org.junit.Assert.*;

public class UserServiceTest {

    private UserService userService = new UserService();


    @Test
    public void testEncryptionDecryption() throws Exception {

        String original = "mypassword123";

        String encrypted = util.EncryptionUtil.encrypt(original);
        String decrypted = util.EncryptionUtil.decrypt(encrypted);

        assertEquals(original, decrypted);
    }
    @Test
    public void testLoginWithWrongPassword() throws Exception {

        boolean result = userService.login("fake@gmail.com", "wrongpass");

        assertFalse(result);
    }

    @Test
    public void testValidateWrongCode() throws Exception {

        boolean result = userService.validateCode(1, "123456");

        assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testChangeMasterPasswordWrongOldPassword() throws Exception {

        userService.changeMasterPassword(1, "wrongOld", "newPass123");
    }

}
