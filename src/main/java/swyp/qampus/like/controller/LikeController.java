package swyp.qampus.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.qampus.common.ResponseDto;
import swyp.qampus.like.service.LikeService;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    @PostMapping
    public ResponseEntity<?> insertLike(@RequestParam("answer")Long answerId, @RequestHeader("Authorization")String token){
        likeService.insert(token,answerId);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"좋아요"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLike(@RequestParam("answer")Long answerId,@RequestHeader("Authorization")String token){
        likeService.delete(token,answerId);
        return ResponseEntity.ok().body(ResponseDto.of(true,200,"좋아요 취소 성공"));
    }
}
