package jjk.animetrack.AnimePackage;

import jjk.animetrack.Entity.Anime;
import jjk.animetrack.Entity.User;
import jjk.animetrack.UserPackages.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class AnimeController {

    private final AnimeService animeService;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "uploads/";

    public AnimeController(AnimeService animeService, UserRepository userRepository) {
        this.animeService = animeService;
        this.userRepository = userRepository;
    }

    @GetMapping("/watchlist")
    public String showWatchlist(Model model, 
                                @RequestParam(value = "search", required = false) String search, 
                                @RequestParam(value = "filter", required = false) String filter,
                                Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Anime> userList = (search != null && !search.isEmpty())
                ? animeService.searchUserAnimeByEmail(email, search)
                : animeService.findAnimeByEmail(email);

        // Seed defaults if list is empty and no search is performed
        if (userList.isEmpty() && (search == null || search.isEmpty())) {
            seedDefaultAnime(user);
            userList = animeService.findAnimeByEmail(email);
        }

        long completedCount = userList.stream()
                .filter(a -> a.getEpisodesWatched() != null && a.getTotalEpisodes() != null
                        && a.getEpisodesWatched().equals(a.getTotalEpisodes()) && a.getTotalEpisodes() > 0)
                .count();

        // Apply ongoing filter if requested
        if ("ongoing".equals(filter)) {
            userList = userList.stream()
                    .filter(a -> a.getEpisodesWatched() < a.getTotalEpisodes())
                    .toList();
        }

        model.addAttribute("animeList", userList);
        model.addAttribute("totalCount", userList.size());
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("anime", new Anime());
        model.addAttribute("search", search);
        model.addAttribute("filter", filter);
        return "watchlist";
    }

    private void seedDefaultAnime(User user) {
        Anime a1 = new Anime();
        a1.setTitle("Jujutsu Kaisen");
        a1.setGenre("Dark Fantasy");
        a1.setEpisodesWatched(24);
        a1.setTotalEpisodes(24);
        a1.setRating(5);
        a1.setImageUrl("https://p325k7wa.twic.pics/high/jujutsu-kaisen/jujutsu-kaisen-cursed-clash/00-page-setup/JJK-header-mobile2.jpg?twic=v1/resize=760/step=10/quality=80");
        a1.setUser(user);

        Anime a2 = new Anime();
        a2.setTitle("Dragon Ball Super");
        a2.setGenre("Action");
        a2.setEpisodesWatched(131);
        a2.setTotalEpisodes(131);
        a2.setRating(4);
        a2.setImageUrl("https://p325k7wa.twic.pics/high/dragon-ball/dragon-ball-sparking-zero/00-page-setup/Page-Setup-Revamp/DBSZ_thumbnail.jpg?twic=v1/resize=760/step=10/quality=80");
        a2.setUser(user);

        animeService.saveAnime(a1);
        animeService.saveAnime(a2);
    }

    @PostMapping("/save")
    public String saveAnime(@ModelAttribute Anime anime, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, Principal principal) throws IOException {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        anime.setUser(user);
        if (anime.getEpisodesWatched() == null) {
            anime.setEpisodesWatched(0);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            handleImageUpload(anime, imageFile);
        }

        animeService.saveAnime(anime);
        return "redirect:/watchlist";
    }


    @PostMapping("/anime/increment/{id}")
    public String increment(@PathVariable Long id, Principal principal) {
        Anime anime = animeService.getById(id);
        if (anime != null && anime.getUser().getEmail().equals(principal.getName())) {
            if (anime.getEpisodesWatched() < anime.getTotalEpisodes()) {
                anime.setEpisodesWatched(anime.getEpisodesWatched() + 1);
                animeService.saveAnime(anime);
            }
        }
        return "redirect:/watchlist";
    }

    @PostMapping("/anime/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        Anime anime = animeService.getById(id);
        if (anime != null && anime.getUser().getEmail().equals(principal.getName())) {
            animeService.deleteById(id);
        }
        return "redirect:/watchlist";
    }

    @PostMapping("/anime/update")
    public String updateAnime(@ModelAttribute Anime updatedAnime, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, Principal principal) throws IOException {
        Anime existingAnime = animeService.getById(updatedAnime.getId());

        if (existingAnime != null && existingAnime.getUser().getEmail().equals(principal.getName())) {
            existingAnime.setTitle(updatedAnime.getTitle());
            existingAnime.setGenre(updatedAnime.getGenre());
            existingAnime.setEpisodesWatched(updatedAnime.getEpisodesWatched());
            existingAnime.setTotalEpisodes(updatedAnime.getTotalEpisodes());
            existingAnime.setRating(updatedAnime.getRating());
            
            if (imageFile != null && !imageFile.isEmpty()) {
                handleImageUpload(existingAnime, imageFile);
            } else if (updatedAnime.getImageUrl() != null && !updatedAnime.getImageUrl().isEmpty()) {
                existingAnime.setImageUrl(updatedAnime.getImageUrl());
            }

            animeService.saveAnime(existingAnime);
        }
        return "redirect:/watchlist";
    }

    private void handleImageUpload(Anime anime, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath);
            anime.setImageUrl("/uploads/" + fileName);
        }
    }
}
