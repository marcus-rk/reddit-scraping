package com.example.reddit_scrapping

// Selenium imports:
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

//  Write to excel imports:
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

fun main() {
    // Scrape by url and save as MutableList of comment strings:
    val driver = ChromeDriver();
    val url: String = "https://www.reddit.com/r/lonely/comments/1352xec/everytime_i_post_something_here_i_get_swarmed_by/";
    val redditComments: MutableList<String> = getRedditPostComments(driver, url)
    driver.quit()

    // Print Reddit comments for testing:
    for (comment in redditComments.indices) {
        println("$comment: ${redditComments[comment]}\n");
    }

    // Create excel file and write reddit comments
    val fileName: String = "test"
    createAndWriteToExcelFile(redditComments, fileName)
}


// TODO: Current error with selenium and chrome version:
// Unable to find an exact match for CDP version 123,
// returning the closest version; found: 122;
// Please update to a Selenium version that supports CDP version 123
fun getRedditPostComments(driver: ChromeDriver, url: String): MutableList<String> {
    driver.get(url)

    // Get all comment Divs
    val commentDivWebElements: List<WebElement> = driver.findElements(By.id("-post-rtjson-content"));

    // Add every comment to a list
    val comments: MutableList<String> = mutableListOf()

    for (commentWebElement in commentDivWebElements) {
        // Comment paragraph/paragraphs element
        val commentDivWebChildElements: List<WebElement> = commentWebElement.findElements(By.tagName("p"))

        var comment: String = "";
        for (paragraphElement in commentDivWebChildElements) {
            comment += paragraphElement.text;
        }
        comments.add(comment)
    }

    return comments
}

// Create excel file with Reddit comments
fun createAndWriteToExcelFile(comments: MutableList<String>, fileName: String): Unit {
    val xlWb = XSSFWorkbook()
    val xlWs = xlWb.createSheet()

    // Add one comment for each row
    for (i in comments.indices){
        val row: Row = xlWs.createRow(i);
        row.createCell(0).setCellValue(comments[i]);
    }

    // Create excel file add rows via xlWs
    val outputStream = FileOutputStream("app/src/main/java/com/example/reddit_scrapping/${fileName}.xlsx")
    xlWb.write(outputStream)
    xlWb.close()
}