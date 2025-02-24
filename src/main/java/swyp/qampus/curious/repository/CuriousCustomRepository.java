package swyp.qampus.curious.repository;

import swyp.qampus.curious.domain.Curious;

import java.util.Optional;

public interface CuriousCustomRepository {
    Optional<Curious>findCuriousByQuestionAndUser(Long questionId,Long userId);
}
