package jjk.animetrack.AnimePackage;

import jjk.animetrack.Entity.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AnimeRepository animeRepository;

    @Override
    public void run(String... args) throws Exception {


        // Only add data if the database is empty
        if (animeRepository.count() == 0) {

            // 1. Jujutsu Kaisen
            Anime a1 = new Anime();
            a1.setTitle("Jujutsu Kaisen");
            a1.setGenre("Dark Fantasy");
            a1.setEpisodesWatched(56);
            a1.setTotalEpisodes(60);
            a1.setRating(5); // Set default rating
            a1.setImageUrl("https://p325k7wa.twic.pics/high/jujutsu-kaisen/jujutsu-kaisen-cursed-clash/00-page-setup/JJK-header-mobile2.jpg?twic=v1/resize=760/step=10/quality=80");

            // 2. Dragon Ball Super
            Anime a2 = new Anime();
            a2.setTitle("Dragon Ball Super");
            a2.setGenre("Action");
            a2.setEpisodesWatched(131);
            a2.setTotalEpisodes(131);
            a2.setRating(4); // Set default rating
            a2.setImageUrl("https://p325k7wa.twic.pics/high/dragon-ball/dragon-ball-sparking-zero/00-page-setup/Page-Setup-Revamp/DBSZ_thumbnail.jpg?twic=v1/resize=760/step=10/quality=80");

            animeRepository.save(a1);
            animeRepository.save(a2);

            System.out.println("Database seeded with Anime data, Images, and Ratings!");
        }
    }
}