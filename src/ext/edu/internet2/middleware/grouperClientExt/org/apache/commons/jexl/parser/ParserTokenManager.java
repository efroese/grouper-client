/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Generated By:JJTree&JavaCC: Do not edit this line. ParserTokenManager.java */
package edu.internet2.middleware.grouperClientExt.org.apache.commons.jexl.parser;

public class ParserTokenManager implements ParserConstants {
    public java.io.PrintStream debugStream = System.out;

    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch (pos) {
            case 0:
                if ((active0 & 0x3ef285550a4800L) != 0L) {
                    jjmatchedKind = 58;
                    return 5;
                }
                return -1;
            case 1:
                if ((active0 & 0x1cf28000084800L) != 0L) {
                    jjmatchedKind = 58;
                    jjmatchedPos = 1;
                    return 5;
                }
                if ((active0 & 0x22000555020000L) != 0L)
                    return 5;
                return -1;
            case 2:
                if ((active0 & 0x1ce00000004800L) != 0L) {
                    jjmatchedKind = 58;
                    jjmatchedPos = 2;
                    return 5;
                }
                if ((active0 & 0x128000080000L) != 0L)
                    return 5;
                return -1;
            case 3:
                if ((active0 & 0x18800000000800L) != 0L) {
                    jjmatchedKind = 58;
                    jjmatchedPos = 3;
                    return 5;
                }
                if ((active0 & 0x4600000004000L) != 0L)
                    return 5;
                return -1;
            case 4:
                if ((active0 & 0x10000000000000L) != 0L) {
                    jjmatchedKind = 58;
                    jjmatchedPos = 4;
                    return 5;
                }
                if ((active0 & 0x8800000000800L) != 0L)
                    return 5;
                return -1;
            case 5:
                if ((active0 & 0x10000000000000L) != 0L) {
                    jjmatchedKind = 58;
                    jjmatchedPos = 5;
                    return 5;
                }
                return -1;
            default:
                return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private final int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_0() {
        switch (curChar) {
            case 33:
                jjmatchedKind = 43;
                return jjMoveStringLiteralDfa1_0(0x2000000L);
            case 37:
                return jjStopAtPos(0, 40);
            case 38:
                jjmatchedKind = 22;
                return jjMoveStringLiteralDfa1_0(0x40000L);
            case 40:
                return jjStopAtPos(0, 12);
            case 41:
                return jjStopAtPos(0, 13);
            case 42:
                return jjStopAtPos(0, 37);
            case 43:
                return jjStopAtPos(0, 35);
            case 44:
                return jjStopAtPos(0, 54);
            case 45:
                return jjStopAtPos(0, 36);
            case 46:
                return jjStopAtPos(0, 57);
            case 47:
                return jjStopAtPos(0, 38);
            case 59:
                return jjStopAtPos(0, 48);
            case 60:
                jjmatchedKind = 27;
                return jjMoveStringLiteralDfa1_0(0x80000000L);
            case 61:
                jjmatchedKind = 15;
                return jjMoveStringLiteralDfa1_0(0x800000L);
            case 62:
                jjmatchedKind = 29;
                return jjMoveStringLiteralDfa1_0(0x200000000L);
            case 91:
                return jjStopAtPos(0, 55);
            case 93:
                return jjStopAtPos(0, 56);
            case 94:
                return jjStopAtPos(0, 21);
            case 97:
                return jjMoveStringLiteralDfa1_0(0x80000L);
            case 100:
                return jjMoveStringLiteralDfa1_0(0x8000000000L);
            case 101:
                return jjMoveStringLiteralDfa1_0(0x4000001000800L);
            case 102:
                return jjMoveStringLiteralDfa1_0(0x10800000000000L);
            case 103:
                return jjMoveStringLiteralDfa1_0(0x440000000L);
            case 105:
                return jjMoveStringLiteralDfa1_0(0x22000000000000L);
            case 108:
                return jjMoveStringLiteralDfa1_0(0x110000000L);
            case 109:
                return jjMoveStringLiteralDfa1_0(0x20000000000L);
            case 110:
                return jjMoveStringLiteralDfa1_0(0x300004000000L);
            case 111:
                return jjMoveStringLiteralDfa1_0(0x20000L);
            case 115:
                return jjMoveStringLiteralDfa1_0(0x4000L);
            case 116:
                return jjMoveStringLiteralDfa1_0(0x400000000000L);
            case 119:
                return jjMoveStringLiteralDfa1_0(0x8000000000000L);
            case 123:
                return jjStopAtPos(0, 9);
            case 124:
                jjmatchedKind = 20;
                return jjMoveStringLiteralDfa1_0(0x10000L);
            case 125:
                return jjStopAtPos(0, 10);
            case 126:
                return jjStopAtPos(0, 42);
            default:
                return jjMoveNfa_0(3, 0);
        }
    }

    private final int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
        switch (curChar) {
            case 38:
                if ((active0 & 0x40000L) != 0L)
                    return jjStopAtPos(1, 18);
                break;
            case 61:
                if ((active0 & 0x800000L) != 0L)
                    return jjStopAtPos(1, 23);
                else if ((active0 & 0x2000000L) != 0L)
                    return jjStopAtPos(1, 25);
                else if ((active0 & 0x80000000L) != 0L)
                    return jjStopAtPos(1, 31);
                else if ((active0 & 0x200000000L) != 0L)
                    return jjStopAtPos(1, 33);
                break;
            case 97:
                return jjMoveStringLiteralDfa2_0(active0, 0x800000000000L);
            case 101:
                if ((active0 & 0x4000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 26, 5);
                else if ((active0 & 0x100000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 32, 5);
                else if ((active0 & 0x400000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 34, 5);
                break;
            case 102:
                if ((active0 & 0x2000000000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 49, 5);
                break;
            case 104:
                return jjMoveStringLiteralDfa2_0(active0, 0x8000000000000L);
            case 105:
                return jjMoveStringLiteralDfa2_0(active0, 0x8000004000L);
            case 108:
                return jjMoveStringLiteralDfa2_0(active0, 0x4000000000000L);
            case 109:
                return jjMoveStringLiteralDfa2_0(active0, 0x800L);
            case 110:
                if ((active0 & 0x20000000000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 53, 5);
                return jjMoveStringLiteralDfa2_0(active0, 0x80000L);
            case 111:
                return jjMoveStringLiteralDfa2_0(active0, 0x10120000000000L);
            case 113:
                if ((active0 & 0x1000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 24, 5);
                break;
            case 114:
                if ((active0 & 0x20000L) != 0L)
                    return jjStartNfaWithStates_0(1, 17, 5);
                return jjMoveStringLiteralDfa2_0(active0, 0x400000000000L);
            case 116:
                if ((active0 & 0x10000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 28, 5);
                else if ((active0 & 0x40000000L) != 0L)
                    return jjStartNfaWithStates_0(1, 30, 5);
                break;
            case 117:
                return jjMoveStringLiteralDfa2_0(active0, 0x200000000000L);
            case 124:
                if ((active0 & 0x10000L) != 0L)
                    return jjStopAtPos(1, 16);
                break;
            default:
                break;
        }
        return jjStartNfa_0(0, active0);
    }

    private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(0, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(1, active0);
            return 2;
        }
        switch (curChar) {
            case 100:
                if ((active0 & 0x80000L) != 0L)
                    return jjStartNfaWithStates_0(2, 19, 5);
                else if ((active0 & 0x20000000000L) != 0L)
                    return jjStartNfaWithStates_0(2, 41, 5);
                break;
            case 105:
                return jjMoveStringLiteralDfa3_0(active0, 0x8000000000000L);
            case 108:
                return jjMoveStringLiteralDfa3_0(active0, 0xa00000000000L);
            case 112:
                return jjMoveStringLiteralDfa3_0(active0, 0x800L);
            case 114:
                return jjMoveStringLiteralDfa3_0(active0, 0x10000000000000L);
            case 115:
                return jjMoveStringLiteralDfa3_0(active0, 0x4000000000000L);
            case 116:
                if ((active0 & 0x100000000000L) != 0L)
                    return jjStartNfaWithStates_0(2, 44, 5);
                break;
            case 117:
                return jjMoveStringLiteralDfa3_0(active0, 0x400000000000L);
            case 118:
                if ((active0 & 0x8000000000L) != 0L)
                    return jjStartNfaWithStates_0(2, 39, 5);
                break;
            case 122:
                return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
            default:
                break;
        }
        return jjStartNfa_0(1, active0);
    }

    private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(1, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(2, active0);
            return 3;
        }
        switch (curChar) {
            case 101:
                if ((active0 & 0x4000L) != 0L)
                    return jjStartNfaWithStates_0(3, 14, 5);
                else if ((active0 & 0x400000000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 46, 5);
                else if ((active0 & 0x4000000000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 50, 5);
                return jjMoveStringLiteralDfa4_0(active0, 0x10000000000000L);
            case 108:
                if ((active0 & 0x200000000000L) != 0L)
                    return jjStartNfaWithStates_0(3, 45, 5);
                return jjMoveStringLiteralDfa4_0(active0, 0x8000000000000L);
            case 115:
                return jjMoveStringLiteralDfa4_0(active0, 0x800000000000L);
            case 116:
                return jjMoveStringLiteralDfa4_0(active0, 0x800L);
            default:
                break;
        }
        return jjStartNfa_0(2, active0);
    }

    private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(2, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(3, active0);
            return 4;
        }
        switch (curChar) {
            case 97:
                return jjMoveStringLiteralDfa5_0(active0, 0x10000000000000L);
            case 101:
                if ((active0 & 0x800000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 47, 5);
                else if ((active0 & 0x8000000000000L) != 0L)
                    return jjStartNfaWithStates_0(4, 51, 5);
                break;
            case 121:
                if ((active0 & 0x800L) != 0L)
                    return jjStartNfaWithStates_0(4, 11, 5);
                break;
            default:
                break;
        }
        return jjStartNfa_0(3, active0);
    }

    private final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(3, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(4, active0);
            return 5;
        }
        switch (curChar) {
            case 99:
                return jjMoveStringLiteralDfa6_0(active0, 0x10000000000000L);
            default:
                break;
        }
        return jjStartNfa_0(4, active0);
    }

    private final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L)
            return jjStartNfa_0(4, old0);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(5, active0);
            return 6;
        }
        switch (curChar) {
            case 104:
                if ((active0 & 0x10000000000000L) != 0L)
                    return jjStartNfaWithStates_0(6, 52, 5);
                break;
            default:
                break;
        }
        return jjStartNfa_0(5, active0);
    }

    private final void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private final void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private final void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private final void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }

    private final void jjCheckNAddStates(int start) {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    private final int jjMoveNfa_0(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 17;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (;;) {
            if (++jjround == 0x7fffffff)
                ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch (jjstateSet[--i]) {
                        case 3:
                            if ((0x3ff000000000000L & l) != 0L) {
                                if (kind > 7)
                                    kind = 7;
                                jjCheckNAddStates(0, 2);
                            } else if (curChar == 39)
                                jjCheckNAddTwoStates(10, 11);
                            else if (curChar == 34)
                                jjCheckNAddTwoStates(7, 8);
                            else if (curChar == 36) {
                                if (kind > 58)
                                    kind = 58;
                                jjCheckNAdd(5);
                            } else if (curChar == 35)
                                jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        case 0:
                            if (curChar == 35)
                                jjCheckNAddTwoStates(1, 2);
                            break;
                        case 1:
                            if ((0xfffffffbffffdbffL & l) != 0L)
                                jjCheckNAddTwoStates(1, 2);
                            break;
                        case 2:
                            if ((0x2400L & l) != 0L)
                                kind = 1;
                            break;
                        case 4:
                            if (curChar != 36)
                                break;
                            if (kind > 58)
                                kind = 58;
                            jjCheckNAdd(5);
                            break;
                        case 5:
                            if ((0x3ff001000000000L & l) == 0L)
                                break;
                            if (kind > 58)
                                kind = 58;
                            jjCheckNAdd(5);
                            break;
                        case 6:
                            if (curChar == 34)
                                jjCheckNAddTwoStates(7, 8);
                            break;
                        case 7:
                            if ((0xfffffffbffffdbffL & l) != 0L)
                                jjCheckNAddTwoStates(7, 8);
                            break;
                        case 8:
                            if (curChar == 34 && kind > 61)
                                kind = 61;
                            break;
                        case 9:
                            if (curChar == 39)
                                jjCheckNAddTwoStates(10, 11);
                            break;
                        case 10:
                            if ((0xffffff7fffffdbffL & l) != 0L)
                                jjCheckNAddTwoStates(10, 11);
                            break;
                        case 11:
                            if (curChar == 39 && kind > 61)
                                kind = 61;
                            break;
                        case 12:
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 7)
                                kind = 7;
                            jjCheckNAddStates(0, 2);
                            break;
                        case 13:
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 7)
                                kind = 7;
                            jjCheckNAdd(13);
                            break;
                        case 14:
                            if ((0x3ff000000000000L & l) != 0L)
                                jjCheckNAddTwoStates(14, 15);
                            break;
                        case 15:
                            if (curChar == 46)
                                jjCheckNAdd(16);
                            break;
                        case 16:
                            if ((0x3ff000000000000L & l) == 0L)
                                break;
                            if (kind > 8)
                                kind = 8;
                            jjCheckNAdd(16);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                MatchLoop: do {
                    switch (jjstateSet[--i]) {
                        case 3:
                        case 5:
                            if ((0x7fffffe87fffffeL & l) == 0L)
                                break;
                            if (kind > 58)
                                kind = 58;
                            jjCheckNAdd(5);
                            break;
                        case 1:
                            jjAddStates(3, 4);
                            break;
                        case 7:
                            jjAddStates(5, 6);
                            break;
                        case 10:
                            jjAddStates(7, 8);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else {
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do {
                    switch (jjstateSet[--i]) {
                        case 1:
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjAddStates(3, 4);
                            break;
                        case 7:
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjAddStates(5, 6);
                            break;
                        case 10:
                            if ((jjbitVec0[i2] & l2) != 0L)
                                jjAddStates(7, 8);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 17 - (jjnewStateCnt = startsAt)))
                return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    static final int[] jjnextStates = { 13, 14, 15, 1, 2, 7, 8, 10, 11, };

    public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, null, null, "\173",
        "\175", "\145\155\160\164\171", "\50", "\51", "\163\151\172\145", "\75", "\174\174", "\157\162", "\46\46",
        "\141\156\144", "\174", "\136", "\46", "\75\75", "\145\161", "\41\75", "\156\145", "\74", "\154\164", "\76",
        "\147\164", "\74\75", "\154\145", "\76\75", "\147\145", "\53", "\55", "\52", "\57", "\144\151\166", "\45",
        "\155\157\144", "\176", "\41", "\156\157\164", "\156\165\154\154", "\164\162\165\145", "\146\141\154\163\145",
        "\73", "\151\146", "\145\154\163\145", "\167\150\151\154\145", "\146\157\162\145\141\143\150", "\151\156",
        "\54", "\133", "\135", "\56", null, null, null, null, };

    public static final String[] lexStateNames = { "DEFAULT", };

    static final long[] jjtoToken = { 0x27ffffffffffff81L, };

    static final long[] jjtoSkip = { 0x7eL, };

    private SimpleCharStream input_stream;

    private final int[] jjrounds = new int[17];

    private final int[] jjstateSet = new int[34];

    protected char curChar;

    public ParserTokenManager(SimpleCharStream stream) {
        if (SimpleCharStream.staticFlag)
            throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    public ParserTokenManager(SimpleCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 17; i-- > 0;)
            jjrounds[i] = 0x80000000;
    }

    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0)
            throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.",
                TokenMgrError.INVALID_LEXICAL_STATE);
        else
            curLexState = lexState;
    }

    private final Token jjFillToken() {
        Token t = Token.newToken(jjmatchedKind);
        t.kind = jjmatchedKind;
        String im = jjstrLiteralImages[jjmatchedKind];
        t.image = (im == null) ? input_stream.GetImage() : im;
        t.beginLine = input_stream.getBeginLine();
        t.beginColumn = input_stream.getBeginColumn();
        t.endLine = input_stream.getEndLine();
        t.endColumn = input_stream.getEndColumn();
        return t;
    }

    int curLexState = 0;

    int defaultLexState = 0;

    int jjnewStateCnt;

    int jjround;

    int jjmatchedPos;

    int jjmatchedKind;

    public final Token getNextToken() {
        int kind;
        Token specialToken = null;
        Token matchedToken;
        int curPos = 0;

        EOFLoop: for (;;) {
            try {
                curChar = input_stream.BeginToken();
            } catch (java.io.IOException e) {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                return matchedToken;
            }

            try {
                input_stream.backup(0);
                while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L)
                    curChar = input_stream.BeginToken();
            } catch (java.io.IOException e1) {
                continue EOFLoop;
            }
            jjmatchedKind = 0x7fffffff;
            jjmatchedPos = 0;
            curPos = jjMoveStringLiteralDfa0_0();
            if (jjmatchedKind != 0x7fffffff) {
                if (jjmatchedPos + 1 < curPos)
                    input_stream.backup(curPos - jjmatchedPos - 1);
                if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                    matchedToken = jjFillToken();
                    return matchedToken;
                } else {
                    continue EOFLoop;
                }
            }
            int error_line = input_stream.getEndLine();
            int error_column = input_stream.getEndColumn();
            String error_after = null;
            boolean EOFSeen = false;
            try {
                input_stream.readChar();
                input_stream.backup(1);
            } catch (java.io.IOException e1) {
                EOFSeen = true;
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
                if (curChar == '\n' || curChar == '\r') {
                    error_line++;
                    error_column = 0;
                } else
                    error_column++;
            }
            if (!EOFSeen) {
                input_stream.backup(1);
                error_after = curPos <= 1 ? "" : input_stream.GetImage();
            }
            throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar,
                TokenMgrError.LEXICAL_ERROR);
        }
    }

}
