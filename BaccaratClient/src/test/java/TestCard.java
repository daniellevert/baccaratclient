import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TestCard {
	Card testCard;
	
	@BeforeEach
	void instantiateCard() {
		testCard = new Card("Spades", 1);
	}
	
	@Test
	void testSuite(){
		assertEquals("Spades", testCard.suite, "Incorrect suit for getSuite");
	}
	
	@Test
	void testValue() {
		assertEquals(1, testCard.value, "Incorrect value for getValue");
	}

}
