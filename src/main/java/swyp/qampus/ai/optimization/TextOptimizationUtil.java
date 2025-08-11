package swyp.qampus.ai.optimization;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.springframework.stereotype.Component;

@Component
public class TextOptimizationUtil {
    //자음
    private static final String[] CONSONANTS = {
            "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ",
            "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ",
            "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };

    //모음
    private static final String[] VOWELS = {
            "ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ",
            "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ",
            "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ",
            "ㅠ", "ㅡ", "ㅢ", "ㅣ"
    };

    public String getQuestionText(String request){
        //open-korean-text 정규화
        CharSequence normalized= OpenKoreanTextProcessorJava.normalize(request);
        String normalizedRequest=String.valueOf(normalized);

        normalizedRequest =normalizeQuestionText(normalizedRequest);
        return normalizedRequest;
    }

    /**
     * 제거 메서드 3개 실행 메서드
     * @param input 입력 문자열
     * @return 자음, 모음 삭제 처리 + 공백 및 \n 삭제 처리 + 특수문자 제거 처리 문자열
     */
    private String normalizeQuestionText(String input){
        input=removeKoreanConsonantAndVowels(input);
        input=removeUnnecessarySpecialChars(input);
        input=removeWhitespaceAndLineBreaks(input);

        return input;
    }

    /**
     * 자음 및 모음 삭제 메서드
     * @param input  입력 문자열
     * @return 자음, 모음 삭제 처리된 문자열
     */
    private String removeKoreanConsonantAndVowels(String input){
        for(String consonant:CONSONANTS){
            input=input.replaceAll(consonant,"");
        }

        for (String vowel:VOWELS){
            input=input.replaceAll(vowel,"");
        }

        return input;
    }

    /**
     * 공백 및 \n 삭제 메서드
     * @param input 입력 문자열
     * @return 공백 및 \n 삭제 처리된 문자열
     */
    private String removeWhitespaceAndLineBreaks(String input){
        return input.replaceAll("\\s","");
    }

    /**
     * 특수문자 제거 메서드
     * @param input 입력 문자열
     * @return 특수문자 제거된 문자열
     */
    private String removeUnnecessarySpecialChars(String input){
        return input.replaceAll("[!@#$&.,~]","");
    }


}
