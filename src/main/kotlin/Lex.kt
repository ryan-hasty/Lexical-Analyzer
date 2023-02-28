import java.io.File
import java.io.IOException
import java.util.*

class Lex {

    // Declare Private Arrays of necessary keywords and symbols
    private val symbols: Array<String> = arrayOf(",", ";", "<", ">", "(", ")", "{", "}","=", "+", "-", "*", "[", "]", ".")
    private val reserved: Array<String> = arrayOf("class", "field", "int", "method", "boolean", "char", "void", "var", "static"
    , "let", "do", "if", "else", "while", "return", "true", "false", "null", "this", "function", "constructor")


    // Function that takes out the white spaces before text begins and after text ends
    fun trimLines(lineList: ArrayList<String>): ArrayList<String>
    {
        val noLines: ArrayList<String> = ArrayList()

        for (line in lineList){
            noLines.add(line.trim())
        }
        return noLines
    }

    // Function that removes all comments from each line since we ignore comments in the jack language
    fun removeComments(listWithComments: ArrayList<String>): ArrayList<String>
    {
        val noComments: ArrayList<String> = ArrayList()
        for(line in listWithComments){

            // If the line starts with any of the identifying values to be a comment, remove it
            if(line.trim().startsWith("//") or line.trim().startsWith("/*") or line.trim().startsWith("*")) {
                continue
            }
            // If the comment appears int the middle of the line then only add the line-up to the comment
            else if(line.indexOf("//") != -1) {
                val commentIndex = line.indexOf("//")

                noComments.add(line.slice(0 until commentIndex))
            }
            else{
                noComments.add(line)
            }
        }
        return noComments
    }

    // Function that reads the lines of the passed in filepath and returns the items as an arrayList
    fun readLines(filePath: String): ArrayList<String> {

        val myList: ArrayList<String> = ArrayList()

        try {
            val scan = Scanner(File(filePath))
            while (scan.hasNextLine()) {
                myList.add(scan.nextLine())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return myList
    }

    // Helper function that prints all values in the ArrayList
    fun printLines(listToRead: ArrayList<String>){
        for(item in listToRead) {
            println(item)
        }
    }

    // Function that separates the values using spaces and symbols as separators
    fun lineToTerms(listToRead: ArrayList<String>): ArrayList<String>{

        val wordList: ArrayList<String> = ArrayList()
        var tempWord = ""
        var tempWord2 = ""
        var count = 0

        for(line in listToRead) {
            // If line is empty skip it
            if(line.isEmpty()) {
                continue
            }
            else {
                for(character in line) {

                    // If a double quote is seen, it starts the counter for identifying string literals
                    if(character == '"' && count == 0) {
                    count += 1
                    continue
                    }

                    // If the character is not a double quote, keep adding the value to the string
                    else if(character != '"' && count == 1) {
                    tempWord2 += character
                    }

                    // If we have reached the second quote, add the string literal to the array list as a term
                    else if(character == '"' && count == 1) {
                    wordList.add(tempWord2)
                    tempWord2 = ""
                    count = 0
                    continue
                    }

                    else if(!character.isWhitespace()) {

                        // If the character is not a white space, add the character to our temporary string
                        tempWord += character

                        // If the temporary word is in the reserved, add it to the list and then reset the temp word
                        if(tempWord in reserved) {
                            wordList.add(tempWord)
                            tempWord = ""
                        }

                        // If the present character is a symbol then add it to the arrayList only
                        else if(character.toString() in symbols) {
                            tempWord = tempWord.dropLast(1) // drops nth index from the temporary string (the symbol)
                            wordList.add(tempWord) // Add the current word as a term to list
                            wordList.add(character.toString()) // adds the symbol as its own term to list
                            tempWord = ""
                        }
                    }
                    else{
                        // If we made it here, it's an identifier so add it
                        wordList.add(tempWord)
                        tempWord = ""
                    }
                }
            }
        }
        return wordList
    }

    // Function that removes all fo the gapes in the lines
    fun removeGaps(listWithGaps: ArrayList<String>): ArrayList<String>
    {
        val listCopy: ArrayList<String> = ArrayList()

        for(line in listWithGaps) {
            if(line.isNotEmpty()) {
                listCopy.add(line)
            }
            else{
                continue
            }
        }
        return listCopy
    }

    // Function that tokenizes and adds the terms to a xml file
    fun tokenize(termsList: ArrayList<String>, fileName: String){

        File(fileName).bufferedWriter().use { out -> // File writer

            out.write("<tokens>\n")
            for (term in termsList) {
                if (term in symbols) {
                    out.write("<symbol> $term </symbol>\n")
                } else if (term in reserved) {
                    out.write("<keyword> $term </keyword>\n")
                } else if (isNumeric(term)) {
                    out.write("<integerConstant> $term </integerConstant>\n")
                } else if (' ' in term) {
                    out.write("<stringConstant> $term </stringConstant>\n")
                } else {
                    out.write("<identifier> $term </identifier>\n")
                }
            }
            out.write("</tokens>")
        }
    }

    // Helper function that checks if a value is numeric
    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }
}