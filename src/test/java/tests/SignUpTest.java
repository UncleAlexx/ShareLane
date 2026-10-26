package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

@Test
public class SignUpTest {

    private static final  Random randomCharacters = new Random();
    private static final Character[] charsForRandom =
            IntStream.concat(IntStream.concat(IntStream.rangeClosed('0','9'), IntStream.rangeClosed('a', 'z')),
            IntStream.rangeClosed('A','Z')).boxed().map(x ->(char)x.intValue()).toArray(Character[]::new);
    private static final Function<Integer, String> getRandomStringFrom = (Integer from) ->
            Arrays.stream(charsForRandom).skip(from).map(x -> charsForRandom[randomCharacters.
            nextInt(from, charsForRandom.length)]).collect(StringBuilder::new,
            StringBuilder::append, (x, y)-> {}).toString();


    public static void TestConfirmationMessageDisplayedAfterSignUpEqualsAccountIsCreated(){

        WebDriver driver = new EdgeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        driver.get("https://sharelane.com/cgi-bin/register.py");

        driver.findElement(By.name("zip_code")).sendKeys(String.valueOf(randomCharacters.
            nextInt(10000, 10001)));
        driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.name("first_name")));

        driver.findElement(By.name("first_name")).sendKeys(getRandomStringFrom.apply(10));
        driver.findElement(By.name("last_name")).sendKeys(getRandomStringFrom.apply(10));
        driver.findElement(By.name("email")).sendKeys(getRandomStringFrom.apply(0) + "@gmail.com");

        var password = getRandomStringFrom.apply(0);

        driver.findElement(By.name("password1")).sendKeys(password);
        driver.findElement(By.name("password2")).sendKeys(password);
        driver.findElement(By.xpath("//input[@type='submit' and @value='Register']")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.className("confirmation_message")));
        Assert.assertTrue(driver.findElement(By.className("confirmation_message")).getText().
            equals("Account is created!"));
    }
}
