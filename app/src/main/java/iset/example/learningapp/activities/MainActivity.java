package iset.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import iset.example.learningapp.R;
import iset.example.learningapp.adapters.CoursesAdapter;

import iset.example.learningapp.models.Course;
import iset.example.learningapp.models.CourseSection;
import iset.example.learningapp.models.PdfDocument;
import iset.example.learningapp.models.Professor;
import iset.example.learningapp.models.Video;

public class MainActivity extends AppCompatActivity implements CoursesAdapter.OnCourseClickListener {

    private RecyclerView rvCourses;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private BottomNavigationView bottomNav;

    private CoursesAdapter coursesAdapter;
    private List<Course> coursesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupBottomNavigation();
        loadCourses();
    }

    private void initViews() {
        rvCourses = findViewById(R.id.rvCourses);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupRecyclerView() {
        rvCourses.setLayoutManager(new LinearLayoutManager(this));
        coursesAdapter = new CoursesAdapter(coursesList, this);
        rvCourses.setAdapter(coursesAdapter);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Déjà sur home
                return true;
            } else if (itemId == R.id.nav_my_courses) {
                // TODO: Naviguer vers Mes Cours
                return true;
            } else if (itemId == R.id.nav_profile) {
                // TODO: Naviguer vers Profil
                return true;
            }
            return false;
        });
    }

    private void loadCourses() {
        progressBar.setVisibility(View.VISIBLE);

        // TODO: Remplacer par appel API Retrofit
        // Simuler un délai de chargement
        rvCourses.postDelayed(() -> {
            coursesList.clear();
            coursesList.addAll(getMockCourses());
            coursesAdapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            if (coursesList.isEmpty()) {
                layoutEmpty.setVisibility(View.VISIBLE);
                rvCourses.setVisibility(View.GONE);
            } else {
                layoutEmpty.setVisibility(View.GONE);
                rvCourses.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    @Override
    public void onCourseClick(Course course) {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra(CourseDetailActivity.EXTRA_COURSE_ID, course.getId());
        startActivity(intent);
    }

    // ============== DONNÉES MOCK ==============
    private List<Course> getMockCourses() {
        List<Course> courses = new ArrayList<>();

        // Cours 1
        Professor prof1 = new Professor(1, "Dr. Ahmed Ben Ali",
                "Développement Mobile", 8,
                "https://randomuser.me/api/portraits/men/32.jpg");

        List<CourseSection> sections1 = createMockSections();

        Course course1 = new Course(1, "Développement Android Complet",
                "Apprenez à créer des applications Android professionnelles de A à Z.",
                "Développement Mobile",
                "https://images.unsplash.com/photo-1607252650355-f7fd0460ccdb?w=800",
                sections1, prof1);
        course1.setEnrolledCount(1250);
        course1.setRating(4.7f);
        courses.add(course1);

        // Cours 2
        Professor prof2 = new Professor(2, "Dr. Fatma Trabelsi",
                "Intelligence Artificielle", 10,
                "https://randomuser.me/api/portraits/women/44.jpg");

        Course course2 = new Course(2, "Machine Learning avec Python",
                "Maîtrisez les algorithmes de Machine Learning et créez vos propres modèles.",
                "Intelligence Artificielle",
                "https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=800",
                createMockSections(), prof2);
        course2.setEnrolledCount(890);
        course2.setRating(4.9f);
        courses.add(course2);

        // Cours 3
        Professor prof3 = new Professor(3, "Ing. Mohamed Sassi",
                "Développement Web", 6,
                "https://randomuser.me/api/portraits/men/67.jpg");

        Course course3 = new Course(3, "React.js - De Zéro à Expert",
                "Construisez des applications web modernes avec React.js.",
                "Développement Web",
                "https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=800",
                createMockSections(), prof3);
        course3.setEnrolledCount(2100);
        course3.setRating(4.8f);
        courses.add(course3);

        // Cours 4
        Professor prof4 = new Professor(4, "Dr. Sonia Meddeb",
                "Base de données", 12,
                "https://randomuser.me/api/portraits/women/28.jpg");

        Course course4 = new Course(4, "SQL et NoSQL - Bases de données",
                "Maîtrisez les bases de données relationnelles et NoSQL.",
                "Base de données",
                "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=800",
                createMockSections(), prof4);
        course4.setEnrolledCount(650);
        course4.setRating(4.5f);
        courses.add(course4);

        // Cours 5
        Professor prof5 = new Professor(5, "Ing. Karim Bouaziz",
                "Cybersécurité", 9,
                "https://randomuser.me/api/portraits/men/52.jpg");

        Course course5 = new Course(5, "Cybersécurité - Les Fondamentaux",
                "Protégez vos systèmes et données contre les cyberattaques.",
                "Sécurité Informatique",
                "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=800",
                createMockSections(), prof5);
        course5.setEnrolledCount(430);
        course5.setRating(4.6f);
        courses.add(course5);

        return courses;
    }

    private List<CourseSection> createMockSections() {
        List<CourseSection> sections = new ArrayList<>();

        // Section 1
        PdfDocument pdf1 = new PdfDocument(1, "Introduction au cours.pdf",
                "https://www.w3.org/WAI/WCAG21/Techniques/pdf/img/table-word.pdf", 2500000);
        List<Video> videos1 = Arrays.asList(
                new Video(1, "Bienvenue dans le cours",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "https://i.ytimg.com/vi/YE7VzlLtp-4/maxresdefault.jpg", 180, 1),
                new Video(2, "Présentation des outils",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                        "https://i.ytimg.com/vi/KQ4bN4n0pTQ/maxresdefault.jpg", 720, 2),
                new Video(3, "Premier projet pratique",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                        "https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg", 540, 3)
        );
        sections.add(new CourseSection(1, "Introduction", 1, pdf1, videos1));

        // Section 2
        PdfDocument pdf2 = new PdfDocument(2, "Les fondamentaux.pdf",
                "https://www.africau.edu/images/default/sample.pdf", 3200000);
        List<Video> videos2 = Arrays.asList(
                new Video(4, "Variables et types de données",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                        "https://i.ytimg.com/vi/2vjPBrBU-TM/maxresdefault.jpg", 600, 1),
                new Video(5, "Les fonctions essentielles",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                        "https://i.ytimg.com/vi/3fumBcKC6RE/maxresdefault.jpg", 480, 2)
        );
        sections.add(new CourseSection(2, "Les Fondamentaux", 2, pdf2, videos2));

        // Section 3
        PdfDocument pdf3 = new PdfDocument(3, "Niveau avancé.pdf",
                "https://www.orimi.com/pdf-test.pdf", 4100000);
        List<Video> videos3 = Arrays.asList(
                new Video(6, "Concepts avancés",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                        "https://i.ytimg.com/vi/5qap5aO4i9A/maxresdefault.jpg", 900, 1),
                new Video(7, "Projet final complet",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                        "https://i.ytimg.com/vi/LXb3EKWsInQ/maxresdefault.jpg", 1200, 2)
        );
        sections.add(new CourseSection(3, "Niveau Avancé", 3, pdf3, videos3));

        return sections;
    }
}