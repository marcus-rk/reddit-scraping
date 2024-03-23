package com.example.reddit_scrapping

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

fun main() {
    // Scrape by url and save as MutableList of comment strings:
    val driver = ChromeDriver();
    val url: String = "https://www.reddit.com/r/lonely/comments/1352xec/everytime_i_post_something_here_i_get_swarmed_by/";
    val redditComments: MutableList<String> = getRedditPostComments(driver, url)
    driver.quit()

    // Console.log result:
    for (comment in redditComments.indices) {
        println("$comment: ${redditComments[comment]}\n");
    }

    // TODO: Make comments save in excel
}


// TODO: Current error with selenium and chrome version:
// Unable to find an exact match for CDP version 123,
// returning the closest version; found: 122;
// Please update to a Selenium version that supports CDP version 123
fun getRedditPostComments(driver: ChromeDriver, url: String): MutableList<String> {
    driver.get(url)

    // Get all comment div's
    val commentWebElements: List<WebElement> = driver.findElements(By.id("-post-rtjson-content"));

    // Add every comment to a list
    val comments: MutableList<String> = mutableListOf()
    for (commentWebElement in commentWebElements) {
        // Comment paragraph/paragraphs
        val commentWebChildElements: List<WebElement> = commentWebElement.findElements(By.tagName("p"))

        var comment: String = "";
        for (paragraphElement in commentWebChildElements) {
            comment += paragraphElement.text;
        }
        comments.add(comment)
    }

    return comments
}