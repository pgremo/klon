/*
 * KlonTokenizer.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

package klon;

import java.io.Reader;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.TokenPattern;
import net.percederberg.grammatica.parser.Tokenizer;

/**
 * A character stream tokenizer.
 *
 * @author   Patrick Gremo
 */
public class KlonTokenizer extends Tokenizer {

    /**
     * Creates a new tokenizer for the specified input stream.
     *
     * @param input          the input stream to read
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    public KlonTokenizer(Reader input) throws ParserCreationException {
        super(input, false);
        createPatterns();
    }

    /**
     * Initializes the tokenizer by creating all the token patterns.
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        TokenPattern  pattern;

        pattern = new TokenPattern(KlonConstants.NUMBER,
                                   "NUMBER",
                                   TokenPattern.REGEXP_TYPE,
                                   "((-?[0-9]+)|(-?[0-9]*\\.[0-9]+))(e-?[0-9]+)?|0[xX][0-9a-fA-F]+");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.OPERATOR,
                                   "OPERATOR",
                                   TokenPattern.REGEXP_TYPE,
                                   "[:='~!@$%\\^&*\\-+\\<>|?/.]+|(and|or|super)");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.IDENTIFIER,
                                   "IDENTIFIER",
                                   TokenPattern.REGEXP_TYPE,
                                   "[a-zA-Z][a-zA-Z0-9_.]*");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.STRING,
                                   "STRING",
                                   TokenPattern.REGEXP_TYPE,
                                   "(\"([^\"]|(\\\\\"))*\")");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.TERMINATOR,
                                   "TERMINATOR",
                                   TokenPattern.REGEXP_TYPE,
                                   "[\\r\\n;]");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.SET,
                                   "SET",
                                   TokenPattern.STRING_TYPE,
                                   ":=");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.UPDATE,
                                   "UPDATE",
                                   TokenPattern.STRING_TYPE,
                                   "=");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.COMMA,
                                   "COMMA",
                                   TokenPattern.STRING_TYPE,
                                   ",");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.LPAREN,
                                   "LPAREN",
                                   TokenPattern.STRING_TYPE,
                                   "(");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.RPAREN,
                                   "RPAREN",
                                   TokenPattern.STRING_TYPE,
                                   ")");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.LBRACK,
                                   "LBRACK",
                                   TokenPattern.STRING_TYPE,
                                   "[");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.RBRACK,
                                   "RBRACK",
                                   TokenPattern.STRING_TYPE,
                                   "]");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.LBRACE,
                                   "LBRACE",
                                   TokenPattern.STRING_TYPE,
                                   "{");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.RBRACE,
                                   "RBRACE",
                                   TokenPattern.STRING_TYPE,
                                   "}");
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.WHITE_SPACE,
                                   "WHITE_SPACE",
                                   TokenPattern.REGEXP_TYPE,
                                   "[ \\t]+");
        pattern.setIgnore();
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.POUND_COMMENT,
                                   "POUND_COMMENT",
                                   TokenPattern.REGEXP_TYPE,
                                   "#.*");
        pattern.setIgnore();
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.SLASH_COMMENT,
                                   "SLASH_COMMENT",
                                   TokenPattern.REGEXP_TYPE,
                                   "//.*");
        pattern.setIgnore();
        addPattern(pattern);

        pattern = new TokenPattern(KlonConstants.MULTI_COMMENT,
                                   "MULTI_COMMENT",
                                   TokenPattern.REGEXP_TYPE,
                                   "/\\*([^*]|\\*[^/])*\\*/");
        pattern.setIgnore();
        addPattern(pattern);
    }
}
