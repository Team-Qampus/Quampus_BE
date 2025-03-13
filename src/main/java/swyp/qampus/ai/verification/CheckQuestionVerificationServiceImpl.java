package swyp.qampus.ai.verification;

import org.springframework.stereotype.Service;

@Service
public class CheckQuestionVerificationServiceImpl implements CheckQuestionVerificationService{

    @Override
    public boolean isValidTotal(String text) {
        return isValidInput(text) &&
                containsMeaningfulWords(text);
    }

    // 입력 길이 검증 (5자 이상)
    private boolean isValidInput(String input){
        return input != null && input.trim().length() > 5;
    }

    // 의미 있는 단어 포함 여부
    private boolean containsMeaningfulWords(String input){
        return input.matches(".*[가-힣a-zA-Z0-9]+.*");
    }

}
