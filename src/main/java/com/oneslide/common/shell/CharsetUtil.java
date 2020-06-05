package com.oneslide.common.shell;

import sun.nio.cs.ext.IBM500;

/**
 * charset utils
 *  <pre>
 under windows:
 * <code>cmd /c chcp</code> to get active code page,and reference to:
 *  <a>https://docs.microsoft.com/zh-cn/windows/win32/intl/code-page-identifiers?redirectedfrom=MSDN</a>
 * to check available charset format
 *  there are {@code WindowsCharsetFormat} for multiple options
 *  codePage: active code page
 *  name: pass to String to get locale language format of bytes
 *  description: some description about this format
 * </pre>
 *
 * **/
public class CharsetUtil {
    public static enum WindowsCharsetFormat{
        IBM037(37,"IBM037","IBM EBCDIC US-Canada"),
        IBM437(437,"IBM437","OEM United States"),
        IBM500(500,"IBM500","IBM EBCDIC International"),
        ASMO_708(708,"ASMO-708","Arabic (ASMO 708)"),
        NOT_SUPPORT_208648038(709,"","Arabic (ASMO-449+, BCON V4)"),
        NOT_SUPPORT_360354689(710,"","Arabic - Transparent Arabic"),
        DOS_720(720,"DOS-720","Arabic (Transparent ASMO); Arabic (DOS)"),
        IBM737(737,"ibm737","OEM Greek (formerly 437G); Greek (DOS)"),
        IBM775(775,"ibm775","OEM Baltic; Baltic (DOS)"),
        IBM850(850,"ibm850","OEM Multilingual Latin 1; Western European (DOS)"),
        IBM852(852,"ibm852","OEM Latin 2; Central European (DOS)"),
        IBM855(855,"IBM855","OEM Cyrillic (primarily Russian)"),
        IBM857(857,"ibm857","OEM Turkish; Turkish (DOS)"),
        IBM00858(858,"IBM00858","OEM Multilingual Latin 1 + Euro symbol"),
        IBM860(860,"IBM860","OEM Portuguese; Portuguese (DOS)"),
        IBM861(861,"ibm861","OEM Icelandic; Icelandic (DOS)"),
        DOS_862(862,"DOS-862","OEM Hebrew; Hebrew (DOS)"),
        IBM863(863,"IBM863","OEM French Canadian; French Canadian (DOS)"),
        IBM864(864,"IBM864","OEM Arabic; Arabic (864)"),
        IBM865(865,"IBM865","OEM Nordic; Nordic (DOS)"),
        CP866(866,"cp866","OEM Russian; Cyrillic (DOS)"),
        IBM869(869,"ibm869","OEM Modern Greek; Greek, Modern (DOS)"),
        IBM870(870,"IBM870","IBM EBCDIC Multilingual/ROECE (Latin 2); IBM EBCDIC Multilingual Latin 2"),
        WINDOWS_874(874,"windows-874","ANSI/OEM Thai (ISO 8859-11); Thai (Windows)"),
        CP875(875,"cp875","IBM EBCDIC Greek Modern"),
        SHIFT_JIS(932,"shift_jis","ANSI/OEM Japanese; Japanese (Shift-JIS)"),
        GB2312(936,"gb2312","ANSI/OEM Simplified Chinese (PRC, Singapore); Chinese Simplified (GB2312)"),
        KS_C_5601_1987(949,"ks_c_5601-1987","ANSI/OEM Korean (Unified Hangul Code)"),
        BIG5(950,"big5","ANSI/OEM Traditional Chinese (Taiwan; Hong Kong SAR, PRC); Chinese Traditional (Big5)"),
        IBM1026(1026,"IBM1026","IBM EBCDIC Turkish (Latin 5)"),
        IBM01047(1047,"IBM01047","IBM EBCDIC Latin 1/Open System"),
        IBM01140(1140,"IBM01140","IBM EBCDIC US-Canada (037 + Euro symbol); IBM EBCDIC (US-Canada-Euro)"),
        IBM01141(1141,"IBM01141","IBM EBCDIC Germany (20273 + Euro symbol); IBM EBCDIC (Germany-Euro)"),
        IBM01142(1142,"IBM01142","IBM EBCDIC Denmark-Norway (20277 + Euro symbol); IBM EBCDIC (Denmark-Norway-Euro)"),
        IBM01143(1143,"IBM01143","IBM EBCDIC Finland-Sweden (20278 + Euro symbol); IBM EBCDIC (Finland-Sweden-Euro)"),
        IBM01144(1144,"IBM01144","IBM EBCDIC Italy (20280 + Euro symbol); IBM EBCDIC (Italy-Euro)"),
        IBM01145(1145,"IBM01145","IBM EBCDIC Latin America-Spain (20284 + Euro symbol); IBM EBCDIC (Spain-Euro)"),
        IBM01146(1146,"IBM01146","IBM EBCDIC United Kingdom (20285 + Euro symbol); IBM EBCDIC (UK-Euro)"),
        IBM01147(1147,"IBM01147","IBM EBCDIC France (20297 + Euro symbol); IBM EBCDIC (France-Euro)"),
        IBM01148(1148,"IBM01148","IBM EBCDIC International (500 + Euro symbol); IBM EBCDIC (International-Euro)"),
        IBM01149(1149,"IBM01149","IBM EBCDIC Icelandic (20871 + Euro symbol); IBM EBCDIC (Icelandic-Euro)"),
        UTF_16(1200,"utf-16","Unicode UTF-16, little endian byte order (BMP of ISO 10646); available only to managed applications"),
        UNICODEFFFE(1201,"unicodeFFFE","Unicode UTF-16, big endian byte order; available only to managed applications"),
        WINDOWS_1250(1250,"windows-1250","ANSI Central European; Central European (Windows)"),
        WINDOWS_1251(1251,"windows-1251","ANSI Cyrillic; Cyrillic (Windows)"),
        WINDOWS_1252(1252,"windows-1252","ANSI Latin 1; Western European (Windows)"),
        WINDOWS_1253(1253,"windows-1253","ANSI Greek; Greek (Windows)"),
        WINDOWS_1254(1254,"windows-1254","ANSI Turkish; Turkish (Windows)"),
        WINDOWS_1255(1255,"windows-1255","ANSI Hebrew; Hebrew (Windows)"),
        WINDOWS_1256(1256,"windows-1256","ANSI Arabic; Arabic (Windows)"),
        WINDOWS_1257(1257,"windows-1257","ANSI Baltic; Baltic (Windows)"),
        WINDOWS_1258(1258,"windows-1258","ANSI/OEM Vietnamese; Vietnamese (Windows)"),
        JOHAB(1361,"Johab","Korean (Johab)"),
        MACINTOSH(10000,"macintosh","MAC Roman; Western European (Mac)"),
        X_MAC_JAPANESE(10001,"x-mac-japanese","Japanese (Mac)"),
        X_MAC_CHINESETRAD(10002,"x-mac-chinesetrad","MAC Traditional Chinese (Big5); Chinese Traditional (Mac)"),
        X_MAC_KOREAN(10003,"x-mac-korean","Korean (Mac)"),
        X_MAC_ARABIC(10004,"x-mac-arabic","Arabic (Mac)"),
        X_MAC_HEBREW(10005,"x-mac-hebrew","Hebrew (Mac)"),
        X_MAC_GREEK(10006,"x-mac-greek","Greek (Mac)"),
        X_MAC_CYRILLIC(10007,"x-mac-cyrillic","Cyrillic (Mac)"),
        X_MAC_CHINESESIMP(10008,"x-mac-chinesesimp","MAC Simplified Chinese (GB 2312); Chinese Simplified (Mac)"),
        X_MAC_ROMANIAN(10010,"x-mac-romanian","Romanian (Mac)"),
        X_MAC_UKRAINIAN(10017,"x-mac-ukrainian","Ukrainian (Mac)"),
        X_MAC_THAI(10021,"x-mac-thai","Thai (Mac)"),
        X_MAC_CE(10029,"x-mac-ce","MAC Latin 2; Central European (Mac)"),
        X_MAC_ICELANDIC(10079,"x-mac-icelandic","Icelandic (Mac)"),
        X_MAC_TURKISH(10081,"x-mac-turkish","Turkish (Mac)"),
        X_MAC_CROATIAN(10082,"x-mac-croatian","Croatian (Mac)"),
        UTF_32(12000,"utf-32","Unicode UTF-32, little endian byte order; available only to managed applications"),
        UTF_32BE(12001,"utf-32BE","Unicode UTF-32, big endian byte order; available only to managed applications"),
        X_CHINESE_CNS(20000,"x-Chinese_CNS","CNS Taiwan; Chinese Traditional (CNS)"),
        X_CP20001(20001,"x-cp20001","TCA Taiwan"),
        X_CHINESE_ETEN(20002,"x_Chinese-Eten","Eten Taiwan; Chinese Traditional (Eten)"),
        X_CP20003(20003,"x-cp20003","IBM5550 Taiwan"),
        X_CP20004(20004,"x-cp20004","TeleText Taiwan"),
        X_CP20005(20005,"x-cp20005","Wang Taiwan"),
        X_IA5(20105,"x-IA5","IA5 (IRV International Alphabet No. 5, 7-bit); Western European (IA5)"),
        X_IA5_GERMAN(20106,"x-IA5-German","IA5 German (7-bit)"),
        X_IA5_SWEDISH(20107,"x-IA5-Swedish","IA5 Swedish (7-bit)"),
        X_IA5_NORWEGIAN(20108,"x-IA5-Norwegian","IA5 Norwegian (7-bit)"),
        US_ASCII(20127,"us-ascii","US-ASCII (7-bit)"),
        X_CP20261(20261,"x-cp20261","T.61"),
        X_CP20269(20269,"x-cp20269","ISO 6937 Non-Spacing Accent"),
        IBM273(20273,"IBM273","IBM EBCDIC Germany"),
        IBM277(20277,"IBM277","IBM EBCDIC Denmark-Norway"),
        IBM278(20278,"IBM278","IBM EBCDIC Finland-Sweden"),
        IBM280(20280,"IBM280","IBM EBCDIC Italy"),
        IBM284(20284,"IBM284","IBM EBCDIC Latin America-Spain"),
        IBM285(20285,"IBM285","IBM EBCDIC United Kingdom"),
        IBM290(20290,"IBM290","IBM EBCDIC Japanese Katakana Extended"),
        IBM297(20297,"IBM297","IBM EBCDIC France"),
        IBM420(20420,"IBM420","IBM EBCDIC Arabic"),
        IBM423(20423,"IBM423","IBM EBCDIC Greek"),
        IBM424(20424,"IBM424","IBM EBCDIC Hebrew"),
        X_EBCDIC_KOREANEXTENDED(20833,"x-EBCDIC-KoreanExtended","IBM EBCDIC Korean Extended"),
        IBM_THAI(20838,"IBM-Thai","IBM EBCDIC Thai"),
        KOI8_R(20866,"koi8-r","Russian (KOI8-R); Cyrillic (KOI8-R)"),
        IBM871(20871,"IBM871","IBM EBCDIC Icelandic"),
        IBM880(20880,"IBM880","IBM EBCDIC Cyrillic Russian"),
        IBM905(20905,"IBM905","IBM EBCDIC Turkish"),
        IBM00924(20924,"IBM00924","IBM EBCDIC Latin 1/Open System (1047 + Euro symbol)"),
        EUC_JP(20932,"EUC-JP","Japanese (JIS 0208-1990 and 0212-1990)"),
        X_CP20936(20936,"x-cp20936","Simplified Chinese (GB2312); Chinese Simplified (GB2312-80)"),
        X_CP20949(20949,"x-cp20949","Korean Wansung"),
        CP1025(21025,"cp1025","IBM EBCDIC Cyrillic Serbian-Bulgarian"),
        NOT_SUPPORT_663671673(21027,"","(deprecated)"),
        KOI8_U(21866,"koi8-u","Ukrainian (KOI8-U); Cyrillic (KOI8-U)"),
        ISO_8859_1(28591,"iso-8859-1","ISO 8859-1 Latin 1; Western European (ISO)"),
        ISO_8859_2(28592,"iso-8859-2","ISO 8859-2 Central European; Central European (ISO)"),
        ISO_8859_3(28593,"iso-8859-3","ISO 8859-3 Latin 3"),
        ISO_8859_4(28594,"iso-8859-4","ISO 8859-4 Baltic"),
        ISO_8859_5(28595,"iso-8859-5","ISO 8859-5 Cyrillic"),
        ISO_8859_6(28596,"iso-8859-6","ISO 8859-6 Arabic"),
        ISO_8859_7(28597,"iso-8859-7","ISO 8859-7 Greek"),
        ISO_8859_8(28598,"iso-8859-8","ISO 8859-8 Hebrew; Hebrew (ISO-Visual)"),
        ISO_8859_9(28599,"iso-8859-9","ISO 8859-9 Turkish"),
        ISO_8859_13(28603,"iso-8859-13","ISO 8859-13 Estonian"),
        ISO_8859_15(28605,"iso-8859-15","ISO 8859-15 Latin 9"),
        X_EUROPA(29001,"x-Europa","Europa 3"),
        ISO_8859_8_I(38598,"iso-8859-8-i","ISO 8859-8 Hebrew; Hebrew (ISO-Logical)"),
        ISO_2022_JP(50220,"iso-2022-jp","ISO 2022 Japanese with no halfwidth Katakana; Japanese (JIS)"),
        CSISO2022JP(50221,"csISO2022JP","ISO 2022 Japanese with halfwidth Katakana; Japanese (JIS-Allow 1 byte Kana)"),
        ISO_2022_JP_JIS(50222,"iso-2022-jp","ISO 2022 Japanese JIS X 0201-1989; Japanese (JIS-Allow 1 byte Kana - SO/SI)"),
        ISO_2022_KR(50225,"iso-2022-kr","ISO 2022 Korean"),
        X_CP50227(50227,"x-cp50227","ISO 2022 Simplified Chinese; Chinese Simplified (ISO 2022)"),
        NOT_SUPPORT_1753623188(50229,"","ISO 2022 Traditional Chinese"),
        NOT_SUPPORT_122068714(50930,"","EBCDIC Japanese (Katakana) Extended"),
        NOT_SUPPORT_300801983(50931,"","EBCDIC US-Canada and Japanese"),
        NOT_SUPPORT_1694976397(50933,"","EBCDIC Korean Extended and Korean"),
        NOT_SUPPORT_1568536584(50935,"","EBCDIC Simplified Chinese Extended and Simplified Chinese"),
        NOT_SUPPORT_162660643(50936,"","EBCDIC Simplified Chinese"),
        NOT_SUPPORT_748633355(50937,"","EBCDIC US-Canada and Traditional Chinese"),
        NOT_SUPPORT_2013297185(50939,"","EBCDIC Japanese (Latin) Extended and Japanese"),
        EUC_JP_SIMPLE(51932,"euc-jp","EUC Japanese"),
        EUC_CN(51936,"EUC-CN","EUC Simplified Chinese; Chinese Simplified (EUC)"),
        EUC_KR(51949,"euc-kr","EUC Korean"),
        NOT_SUPPORT_1800479654(51950,"","EUC Traditional Chinese"),
        HZ_GB_2312(52936,"hz-gb-2312","HZ-GB2312 Simplified Chinese; Chinese Simplified (HZ)"),
        GB18030(54936,"GB18030","Windows XP and later: GB18030 Simplified Chinese (4 byte); Chinese Simplified (GB18030)"),
        X_ISCII_DE(57002,"x-iscii-de","ISCII Devanagari"),
        X_ISCII_BE(57003,"x-iscii-be","ISCII Bangla"),
        X_ISCII_TA(57004,"x-iscii-ta","ISCII Tamil"),
        X_ISCII_TE(57005,"x-iscii-te","ISCII Telugu"),
        X_ISCII_AS(57006,"x-iscii-as","ISCII Assamese"),
        X_ISCII_OR(57007,"x-iscii-or","ISCII Odia"),
        X_ISCII_KA(57008,"x-iscii-ka","ISCII Kannada"),
        X_ISCII_MA(57009,"x-iscii-ma","ISCII Malayalam"),
        X_ISCII_GU(57010,"x-iscii-gu","ISCII Gujarati"),
        X_ISCII_PA(57011,"x-iscii-pa","ISCII Punjabi"),
        UTF_7(65000,"utf-7","Unicode (UTF-7)"),
        UTF_8(65001,"utf-8","Unicode (UTF-8)");
        private final String name;
        private final int codePage;
        private final String description;
        WindowsCharsetFormat(int codePage,String name,String description) {
            this.name = name;
            this.codePage=codePage;
            this.description=description;
        }
    }

}
