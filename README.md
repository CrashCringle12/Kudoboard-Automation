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
 
Each major should have its own folder within Utils, in each folder should be a names.txt and a randomNames.txt

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
 * file it will find 5 names at random from the randomNames.txt and include theri posts
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
 ******* students.txt ********
 * This should contain a list of each student to be receiving a kudoboard on each line (first and last name on one line)
 * The format is the same as names.txt
