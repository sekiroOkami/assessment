package com.kbtg.bootcamp.posttest.lottery;

import com.kbtg.bootcamp.posttest.admin.AdminRequest;
import com.kbtg.bootcamp.posttest.exception.DuplicateLotteryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class LotteryRepositoryTest {
    @Autowired
    private LotteryRepository lotteryRepository;

    @Autowired
    private LotteryService lotteryService;


    @Test
    @DisplayName("EXP01 task: check Duplicate")
    void whenDuplicateTicket_thenThrowDataIntegrityViolationException() {
        getRandom6digitsString();

        // Arrange
        String ticket = "111111";


        // Act & Assert
        DuplicateLotteryException exception = assertThrows(DuplicateLotteryException.class, () -> {
            Lottery lottery2 = new Lottery(ticket, 20.0, 200L); // Duplicate ticket value
            if (lotteryService.isLotteryExistsByTicketNumber(ticket)) {
                throw new DuplicateLotteryException("duplicate ticket");
            }
        });

    }


    @Test
    @DisplayName("EXP01 task: check notExits lottery")
    public void whenTicketDoesNotExist_thenReturnFalse() {
        String randomString = getRandom6digitsString();

        // Arrange
        String nonExistingTicket = randomString;

        // Act
        boolean exists = lotteryService.isLotteryExistsByTicketNumber(nonExistingTicket);

        // Assert
        assertThat(exists).isFalse();
    }

    private static String getRandom6digitsString() {
        int min = 100000;
        int max = 999999;
        int randomNumber = (int) (Math.random() * (max - min + 1) + min);
        String randomString = String.valueOf(randomNumber);
        return randomString;
    }

    @Test
    @DisplayName("EXP01 task: check return response body ")
    void shouldReturnBodyWhenCreateNewLottery() { // lottery does n't exist
        String randomString = getRandom6digitsString();
        // arrange
        AdminRequest request = new AdminRequest(randomString, 10.0, 10L);

        // act
        ResponseEntity<?> lottery = lotteryService.createLottery(request);

        // Assert
        assertThat(lottery.getBody()).isInstanceOf(LotteryResponse.class);
    }

}