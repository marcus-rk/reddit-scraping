package com.example.reddit_scrapping

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

fun main() {
    // It’s you, you creeper. Scraping
    val driver = ChromeDriver();
    driver.get("https://www.reddit.com/r/lonely/comments/12fzic8/its_you_you_creeper/")

    // Get all comment div's
    val commentWebElements: List<WebElement> = driver.findElements(By.idName("-post-rtjson-content"));

    // Add every comment to a list
    val comments: MutableList<String> = mutableListOf()
    for (commentWebElement in commentWebElements) {
        var comment: String = "";
        for (paragraph in commentWebElement) {
            comment += paragraph.text;
        }
        comments.add(comment)
    }

    // Console.log result:
    for (comment in comments)
        println(comment)
}