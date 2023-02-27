fun main(args: Array<String>) {

    // Initialize Lexical Analyzer
    val lex = Lex()

    // Read in files to variables for later use
    val jackFile = lex.readLines("src/main/resources/Main.jack")
    val squareFile = lex.readLines("src/main/resources/SquareGame.jack")

    // Remove comments and trim lines from text file for tokenizing
    val jackToTokenize = lex.trimLines(lex.removeComments(jackFile))
    val squareToTokenize = lex.trimLines(lex.removeComments(squareFile))

    // Create
    val jackWords = lex.lineToTerms(jackToTokenize)
    val squareWords = lex.lineToTerms(squareToTokenize)

    // Remove gaps between terms
    val jackT = lex.removeGaps(jackWords)
    val squareT = lex.removeGaps(squareWords)

    // Print the final tokenized terms if necessary
    //lex.printLines(termsJ)
    //lex.printLines(termsS)

    // Write tokenized terms to new file as .xml
    lex.tokenize(jackT, "jackT.xml")
    lex.tokenize(squareT, "squareGameT.xml")

}