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
    // Initializing Selenium ChromeDriver
    val driver = ChromeDriver()

    // URLs of Reddit posts to scrape
    val url1: String = "https://old.reddit.com/r/lonely/comments/1bhb5fr/im_ugly_thats_why_im_lonely/"
    val url2: String = "https://old.reddit.com/r/lonely/comments/1494egl/no_romance_for_ugly_gals/"
    val url3: String = "https://old.reddit.com/r/lonely/comments/1352xec/everytime_i_post_something_here_i_get_swarmed_by/"
    val url4: String = "https://old.reddit.com/r/lonely/comments/12fzic8/its_you_you_creeper/"

    // Scraping Reddit comments from each post
    val redditComments1: MutableList<String> = getRedditPostComments(driver, url1, "siteTable_t3_1bhb5fr")
    val redditComments2: MutableList<String> = getRedditPostComments(driver, url2, "siteTable_t3_1494egl")
    val redditComments3: MutableList<String> = getRedditPostComments(driver, url3, "siteTable_t3_1352xec")
    val redditComments4: MutableList<String> = getRedditPostComments(driver, url4, "siteTable_t3_12fzic8")

    // Create and write Reddit comments to Excel files
    createAndWriteToExcelFile(redditComments1, "im_ugly_thats_why_im_lonely")
    createAndWriteToExcelFile(redditComments2, "no_romance_for_ugly_gals")
    createAndWriteToExcelFile(redditComments3, "everytime_i_post_something_here_i_get_swarmed_by")
    createAndWriteToExcelFile(redditComments4, "its_you_you_creeper")

    // Close Selenium ChromeDriver
    driver.quit()
}

// Function to scrape comments from a Reddit post using Selenium
fun getRedditPostComments(driver: ChromeDriver, url: String, siteTableID: String): MutableList<String> {
    // Navigate to the Reddit post URL
    driver.get(url)

    // Locate the comment section in the HTML
    val commentSection: WebElement? = driver.findElement(By.id(siteTableID))

    // Extract all comment elements within the comment section
    val commentElements: List<WebElement> = commentSection!!.findElements(By.className("md"))

    // Extract comments from HTML elements and add to redditComments list
    val redditComments: MutableList<String> = mutableListOf()
    for (element in commentElements) {
        val paragraphs = element.findElements(By.tagName("p"))
        var commentText: String = ""

        // Make text from each paragraph element into a single string, adding newline if multiple paragraphs
        for (paragraph in paragraphs) {
            commentText += paragraph.text
            if (paragraphs.size > 1) {
                commentText += "\n"
            }
        }

        if (commentText.isNotEmpty())
            redditComments.add(commentText)
    }

    return redditComments
}


// Function to create an Excel file and write Reddit comments to it
fun createAndWriteToExcelFile(comments: MutableList<String>, fileName: String) {
    val xlWb = XSSFWorkbook()
    val xlWs = xlWb.createSheet()

    // Add one comment for each row
    for (i in comments.indices){
        val row: Row = xlWs.createRow(i)
        row.createCell(0).setCellValue(comments[i])
    }

    // Write comments to Excel file
    val outputStream = FileOutputStream("app/src/main/java/com/example/reddit_scrapping/${fileName}.xlsx")
    xlWb.write(outputStream)
    xlWb.close()
}
