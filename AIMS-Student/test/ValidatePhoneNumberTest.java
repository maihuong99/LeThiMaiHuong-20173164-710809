
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.ParameterizedTest;

//import controller.PlaceOrderController;
import controller.*;

class ValidatePhoneNumberTest {
	//Le Thi Mai Huong 20173164
	private PlaceOrderController placeOrderController;

	@BeforeEach
	void setUp() throws Exception {

		placeOrderController = new PlaceOrderController();
	}
	
	@ParameterizedTest
	@CsvSource({
		"0123456789,true",
		"01234,false",
		"abc345,false",
		"1234567890,false"
	})
	
	public void test(String phone, boolean expected) {
		boolean isValid = placeOrderController.validatePhoneNumber(phone);
		assertEquals(expected, isValid);
	}

}