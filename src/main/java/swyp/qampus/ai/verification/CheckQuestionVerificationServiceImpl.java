package swyp.qampus.ai.verification;

import org.springframework.stereotype.Service;

@Service
public class CheckQuestionVerificationServiceImpl implements CheckQuestionVerificationService{

    @Override
    public boolean isValidTotal(String text) {
        return isValidInput(text) &&
                containsMeaningfulWords(text) &&
                !isRandomKeyboardInput(text);
    }

    // 입력 길이 검증 (5자 이상)
    private boolean isValidInput(String input){
        return input != null && input.trim().length() > 5;
    }

    // 의미 있는 단어 포함 여부
    private boolean containsMeaningfulWords(String input){
        return input.matches(".*[가-힣a-zA-Z0-9]+.*");
    }

    // 의미 없는 랜덤 키보드 입력 감지
    private boolean isRandomKeyboardInput(String input){
        // 의미 없는 반복 문자열 (예: ㅁㄴㅇㄹ, asdf, qwert 등) 체크
        String lowerInput = input.toLowerCase();
        return lowerInput.matches(".*(.)\\1{2,}.*") || // 같은 문자 반복
                lowerInput.matches(".*[ㄱ-ㅎㅏ-ㅣ]{3,}.*") || // 자음/모음만 반복
                lowerInput.matches(".*(asdf|qwer|zxcv|1234).*"); // 흔한 키보드 패턴
    }

}
