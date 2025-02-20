package swyp.qampus.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.qampus.image.domain.Image;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
