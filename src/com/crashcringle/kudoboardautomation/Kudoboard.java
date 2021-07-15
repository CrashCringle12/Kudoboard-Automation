/****** Data.txt Format *******
 * This file is used to allow you to quickly login to the kudoboard site
 * automatically and gives the application some important information needed for 
 * it to run
 * Line 1: First Name
 * Line 2: Email
 * Line 3 Password
 * Line 4: The amount of assorted posts to generate not including the mandatory posts
 * Line 5: The major (IST, SRA, HCDD, CYBER, ETI, DS)
 * Example:
 * 
 * Lamar
 * lamar@psu.edu
 * Password123
 * 7
 * Cyber
 * 
 *
 ******* names.txt Format *******
 * This file contains the mandatory names to be included in every kudoboard.
 * These are the mandatory posts required for every kudoboard of this major.
 * For example, at this time every Cyber student kudoboard requires posts from 
 * Steve, Dr Gines, Jeffery Bardzel, Angela Miller, Joanne Peca, and Dr. Giacobe
 * This file should contain a list of names (one per line) written exactly as they
 * are written in the spreadsheet. This includes any and all punctuation. 
 * 
 * The application reads data from the spreadsheet and it's critical these names
 * are written in here verbatim
 * 
 ******* randomNames.txt Format *******
 * This file contains the "bank" of names to be used as the assorted names in each
 * Kudoboard. In addition to the mandatory posts in each board there are also 
 * other posts included from a variety of different students, staff, faculty, and alumni
 * Use this file to list the names of those wish to be included in the "bank" of names
 * the application will choose from when picking random posts to include.
 * 
 * For example, if you have marked that you would like 5 random posts in the data.txt
 * file it will find 5 names at random from the randomNames.txt and include their posts
 * in the kudoboard. The format is the same as names.txt
 * 
 ****** Kudoboard.csv *******
 * The format of the smartsheet at the time of writing had the author names in
 * the second column and Welcome statement in the third column. This is the only
 * requirement for this file.
 * This can be downloaded from the smartsheet online as a csv file. Ensure it is
 * named Kudoboard.csv
 * 
 ******* img folder ********
 * This folder should contain images to be included with each respective kudoboard
 * post. Each image should be named after the name of the person whose kudoboard
 * it represents. For example, Lauren Pearl's picture for her post should be named
 * "Lauren Pearl" the extension is ignored during reading so it is okay if it is a
 * jpg/png/jpeg etc.
 * 
 */
package com.crashcringle.kudoboardautomation;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Lamar Cooley
 */
public class Kudoboard {

    static WebDriver driver;
    static String yourFirstName;
    static String firstName;
    static String lastName;
    int amountOfRandom;
    String major;
    String path; //This is the Folder path containing all of the necessary assets
    Map<String,String> theRecords;
    public Kudoboard() {
        //Allow the user to set the path containing all of the assets.
        path = getFile().getPath();
        login();
        theRecords = readCSV(new File(path+"/Kudoboard.csv"));   
        boolean flag = false;
        List<String> names = promptNames();
        for (String name : names) {
            try {
                driver.get("https://psu-ist.kudoboard.com/boards/given");
                //Allow the user to login
                firstName = name.split(" ")[0];
                lastName = name.split(" ")[1];
                play();
            } catch (Exception e) {
            e.getStackTrace();
            
            }
        }
        driver.quit();
    }
     public List<String> promptNames() {
        boolean flag = false;
        List<String> names = new ArrayList<>();
        Scanner scnr;
        try {
            scnr = new Scanner(new File(path+"/students.txt"));
            while (scnr.hasNext()) {
                String name = scnr.nextLine();
                System.out.println(name);
                names.add(name);      
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Kudoboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        return names;
    }
    public void play() {
        createNewBoard();
        int counter = 0;
        for (String key : theRecords.keySet()) {
            File file = findFile(key);
            addToBoard(theRecords.get(key), file, key);
            counter++;
        } 
    }
    /**
     * This method opens up a dialog allowing for the user to select
     * a directory to be read by this program.
     * 
     * @returns the folder containing the necessary files
     * for kudoboard automation. 
     */
    public static File getFile() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Kudoboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Kudoboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Kudoboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Kudoboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle("Select the folder containing kudo info");

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                return selectedFile;
        }
        else {
            return null;
        }
    }
    /**
     * 
     * @param key Name of the file to locate
     * @return the file with a name matching the key
     */
    public File findFile(String key)
    {
        File folder = new File(path+"/img/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
                String filename = file.getName(); //split filename from it's extension
                if(filename.contains(key)) //matching defined filename
                    return file;
            }
         }
        return new File(path+"/img/default.jpg");
    }
    public List<String> cyberNames() {
        List<String> names = new ArrayList<>();
        Scanner scnr = null;
        try {
            scnr = new Scanner(new File(path+"/"+major+"/names.txt"));
            while (scnr.hasNextLine()) {
                names.add(scnr.nextLine());
            }
            scnr = new Scanner(new File(path+"/"+major+"/randomNames.txt"));
            List<String> ranNames = new ArrayList<>();
            while (scnr.hasNextLine()) {
                    ranNames.add(scnr.nextLine());
            }
            int ranNum = amountOfRandom;
            while (ranNum> 0) {
                int randomIndex = (new Random()).nextInt(ranNames.size());
                names.add(ranNames.get(randomIndex));
                ranNames.remove(randomIndex);
                ranNum--;
            }

        } catch (FileNotFoundException ex) {
           ex.getStackTrace();
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            scnr.close();
        }
        return names;
    }
    public static void Wait(int i) {
        try {
            Thread.sleep(1000 * i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public Map<String,String> readCSV(File file) {
        List<String> names = cyberNames();
        Map<String,String> theRecords = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(file));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                if (names.contains(Arrays.asList(values).get(1)))
                    theRecords.put(Arrays.asList(values).get(1), Arrays.asList(values).get(2));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return theRecords;
    }

    public static void addToBoard(String text, File image, String author) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 35);
            driver.findElement(By.xpath("//a[@class='floating-button']")).click();
            Wait(2);
            driver.findElement(By.xpath("//div[@role='textbox']")).sendKeys(text);
            JavascriptExecutor jse = (JavascriptExecutor)driver;
            jse.executeScript("scroll(0, -800);");
            driver.findElement(By.xpath("//button[contains(text(), 'Add Image')]")).click();
            driver.findElement(By.xpath("//button[contains(text(), 'Upload Image')]")).click();
            Wait(2);
            WebElement uploadFileElement = driver.findElement(By.xpath("//input[@type='file']"));
            uploadFileElement.sendKeys(image.getAbsolutePath());
            jse.executeScript("scroll(0, 800);");
            Wait(1);
            driver.findElement(By.xpath("//button[contains(text(),'Post')]")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'From')]")));
            Wait(2);
            driver.findElement(By.xpath("//span[contains(text(),'From "+yourFirstName+"')]")).click();
            Wait(2);
            WebElement authorBox;
            try {
                authorBox = driver.findElement(By.xpath("//div[@class='content-editable medium-editor-element']"));
            } catch (Exception e) {
                System.out.println("Couldn't click on author box retrying");
                driver.findElement(By.xpath("//span[contains(text(),'From ')]")).click();
                authorBox = driver.findElement(By.xpath("//div[@class='content-editable medium-editor-element']"));
            } 
            authorBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), author);
            authorBox.sendKeys(Keys.TAB);
            authorBox.sendKeys(Keys.ENTER);
            //driver.findElements(By.xpath("//*[@class='kudo']//div[@class='kudo-footer']//i[@class='fa fa-check']")).get(element).click();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        }

    /**
     *
     */
    public static void createNewBoard() {
        
        driver.findElement(By.xpath("//a[contains(text(),'New Board')]")).click();
        Wait(1);
        List<WebElement> textBoxes = driver.findElements(By.xpath("//input[@type='text']"));
        textBoxes.get(0).sendKeys(firstName);
        textBoxes.get(1).sendKeys(lastName);
        textBoxes.get(2).sendKeys("Congrats " + firstName + " from Penn State's College of IST");
        textBoxes.get(2).submit();
        Wait(1);
    }
    
    /**
     * @author Lamar Cooley
     * This method takes the email, password, first name
     * and amount of random posts to generate. These values are 
     * saved and known by the application throughout its runtime.
     * 
     * The primary purpose of this method is to login to the kudoboard website
     * using the input given. Rather than prompting for you to enter this information
     * every single time, you simply put this information in the data.txt file
     * using the format put above.
     */
    public void login() {
        //I've included the chromium webdriver in this application
        //This makes sure your system knows where to find it
        System.setProperty("webdriver.chrome.driver", path+"/chromedriver.exe");
        driver = new ChromeDriver();

        //Navigate to the Kudoboard website.
        driver.get("https://psu-ist.kudoboard.com/boards/given");

        //Get the Email and Password input boxes by their X-path.
        WebElement username = driver.findElement(By.xpath("//input[@type='email']"));
        WebElement password = driver.findElement(By.xpath("//input[@type='password']"));

        //Allow the user to login
        Scanner scnr;
        try {
            scnr = new Scanner(new File(path+"/data.txt"));
            //Prompt the user for their first name, email, and password.
            yourFirstName = scnr.next();
            System.out.println("First Name: " + yourFirstName);
            String email = scnr.next();
            System.out.println("Email: " + email);
            username.sendKeys(email);
            System.out.println("Password: ******");
            password.sendKeys(scnr.next());
         
            password.submit();
            //How many random posts should we generate outside of the Mandatory posts?
            amountOfRandom =scnr.nextInt();
            System.out.println("Random Names: " + amountOfRandom);
            major = scnr.next();
            System.out.println("Major: " + major);
            System.out.println("Note, these should be in your data.txt file separated by spaces");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
