package jjk.animetrack.Entity;

import jakarta.persistence.*;
@Entity
@Table(name = "animes")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private Integer episodesWatched = 0;
    private Integer totalEpisodes = 0;
    private Integer rating = 1;


    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public String getRatingStars() {
        if (this.rating == null) return "Not Rated";
        return "⭐".repeat(Math.max(1, Math.min(this.rating, 5)));
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getEpisodesWatched() { return episodesWatched; }
    public void setEpisodesWatched(Integer episodesWatched) { this.episodesWatched = episodesWatched; }

    public Integer getTotalEpisodes() { return totalEpisodes; }
    public void setTotalEpisodes(Integer totalEpisodes) { this.totalEpisodes = totalEpisodes; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}