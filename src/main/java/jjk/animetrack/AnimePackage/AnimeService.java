package jjk.animetrack.AnimePackage;

import org.springframework.transaction.annotation.Transactional;
import jjk.animetrack.Entity.Anime;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnimeService {
    private final AnimeRepository repository;

    // Use constructor injection only (no @Autowired needed here)
    public AnimeService(AnimeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Anime> findAnimeByEmail(String email) {
        return repository.findByUserEmail(email);
    }

    public List<Anime> searchUserAnimeByEmail(String email, String title) {
        return repository.findByUserEmailAndTitleContainingIgnoreCase(email, title);
    }

    @Transactional(readOnly = true)
    public List<Anime> findAnimeByUsername(String username) {
        return repository.findByUserUsername(username);
    }

    public List<Anime> searchUserAnime(String username, String title) {
        return repository.findByUserUsernameAndTitleContainingIgnoreCase(username, title);
    }

    public void saveAnime(Anime anime) {
        repository.save(anime);
    }

    public Anime getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}