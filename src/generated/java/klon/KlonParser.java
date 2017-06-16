/*
 * KlonParser.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

package klon;

import java.io.Reader;

import net.percederberg.grammatica.parser.Analyzer;
import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ProductionPattern;
import net.percederberg.grammatica.parser.ProductionPatternAlternative;
import net.percederberg.grammatica.parser.RecursiveDescentParser;

/**
 * A token stream parser.
 *
 * @author   Patrick Gremo
 */
public class KlonParser extends RecursiveDescentParser {

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_1 = 3001;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_2 = 3002;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_3 = 3003;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_4 = 3004;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_5 = 3005;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_6 = 3006;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_7 = 3007;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_8 = 3008;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_9 = 3009;

    /**
     * Creates a new parser.
     *
     * @param in             the input stream to read from
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public KlonParser(Reader in) throws ParserCreationException {
        super(new KlonTokenizer(in));
        createPatterns();
    }

    /**
     * Creates a new parser.
     *
     * @param in             the input stream to read from
     * @param analyzer       the analyzer to use while parsing
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public KlonParser(Reader in, Analyzer analyzer)
        throws ParserCreationException {

        super(new KlonTokenizer(in), analyzer);
        createPatterns();
    }

    /**
     * Initializes the parser by creating all the production patterns.
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        ProductionPattern             pattern;
        ProductionPatternAlternative  alt;

        pattern = new ProductionPattern(KlonConstants.MESSAGE_CHAIN,
                                        "MessageChain");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_1, 1, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(KlonConstants.MESSAGE,
                                        "Message");
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.STANDARD_MESSAGE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.SLOT_OPERATION, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(KlonConstants.STANDARD_MESSAGE,
                                        "StandardMessage");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_2, 1, 1);
        alt.addProduction(KlonConstants.ATTACHED, 0, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(KlonConstants.SLOT_OPERATION,
                                        "SlotOperation");
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.IDENTIFIER, 1, 1);
        alt.addProduction(SUBPRODUCTION_3, 1, 1);
        alt.addProduction(KlonConstants.ATTACHED, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(KlonConstants.GROUP,
                                        "Group");
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.LPAREN, 1, 1);
        alt.addProduction(SUBPRODUCTION_5, 0, 1);
        alt.addToken(KlonConstants.RPAREN, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.LBRACE, 1, 1);
        alt.addProduction(SUBPRODUCTION_7, 0, 1);
        alt.addToken(KlonConstants.RBRACE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.LBRACK, 1, 1);
        alt.addProduction(SUBPRODUCTION_9, 0, 1);
        alt.addToken(KlonConstants.RBRACK, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(KlonConstants.ATTACHED,
                                        "Attached");
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.MESSAGE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(KlonConstants.SYMBOL,
                                        "Symbol");
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.IDENTIFIER, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.OPERATOR, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.STRING, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.NUMBER, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_1,
                                        "Subproduction1");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.MESSAGE, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.TERMINATOR, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_2,
                                        "Subproduction2");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.GROUP, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.SYMBOL, 1, 1);
        alt.addProduction(KlonConstants.GROUP, 0, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_3,
                                        "Subproduction3");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.SET, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.UPDATE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_4,
                                        "Subproduction4");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.COMMA, 1, 1);
        alt.addProduction(KlonConstants.MESSAGE_CHAIN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_5,
                                        "Subproduction5");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.MESSAGE_CHAIN, 1, 1);
        alt.addProduction(SUBPRODUCTION_4, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_6,
                                        "Subproduction6");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.COMMA, 1, 1);
        alt.addProduction(KlonConstants.MESSAGE_CHAIN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_7,
                                        "Subproduction7");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.MESSAGE_CHAIN, 1, 1);
        alt.addProduction(SUBPRODUCTION_6, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_8,
                                        "Subproduction8");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(KlonConstants.COMMA, 1, 1);
        alt.addProduction(KlonConstants.MESSAGE_CHAIN, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_9,
                                        "Subproduction9");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addProduction(KlonConstants.MESSAGE_CHAIN, 1, 1);
        alt.addProduction(SUBPRODUCTION_8, 0, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);
    }
}
