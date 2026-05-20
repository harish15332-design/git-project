package jjk.animetrack.AnimePackage;

import jjk.animetrack.Entity.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findByUserUsername(String username);
    List<Anime> findByUserUsernameAndTitleContainingIgnoreCase(String username, String title);
    List<Anime> findByUserEmail(String email);
    List<Anime> findByUserEmailAndTitleContainingIgnoreCase(String email, String title);
}