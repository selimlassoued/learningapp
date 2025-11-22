package iset.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import iset.example.learningapp.R;
import iset.example.learningapp.adapters.CourseSectionsAdapter;
import iset.example.learningapp.database.CourseDAO;
import iset.example.learningapp.models.Course;
import iset.example.learningapp.models.CourseSection;
import iset.example.learningapp.models.PdfDocument;
import iset.example.learningapp.models.Professor;
import iset.example.learningapp.models.Video;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity
        implements CourseSectionsAdapter.OnSectionInteractionListener {

    public static final String EXTRA_COURSE_ID = "course_id";

    private ImageView ivCourseImage;
    private TextView tvCourseTitle;
    private TextView tvCourseCategory;
    private TextView tvSectionsCount;
    private TextView tvVideosCount;
    private TextView tvDuration;
    private ShapeableImageView ivProfessorImage;
    private TextView tvProfessorName;
    private TextView tvProfessorSpeciality;
    private TextView tvProfessorExperience;
    private TextView tvCourseDescription;
    private RecyclerView rvSections;
    private MaterialButton btnEnroll;
    private Toolbar toolbar;

    private Course course;
    private CourseSectionsAdapter sectionsAdapter;
    private CourseDAO courseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Initialiser le DAO
        courseDAO = new CourseDAO(this);

        initViews();
        setupToolbar();

        // Récupérer l'ID du cours depuis l'intent
        int courseId = getIntent().getIntExtra(EXTRA_COURSE_ID, -1);

        // Charger les données du cours depuis SQLite
        loadCourseData(courseId);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivCourseImage = findViewById(R.id.ivCourseImage);
        tvCourseTitle = findViewById(R.id.tvCourseTitle);
        tvCourseCategory = findViewById(R.id.tvCourseCategory);
        tvSectionsCount = findViewById(R.id.tvSectionsCount);
        tvVideosCount = findViewById(R.id.tvVideosCount);
        tvDuration = findViewById(R.id.tvDuration);
        ivProfessorImage = findViewById(R.id.ivProfessorImage);
        tvProfessorName = findViewById(R.id.tvProfessorName);
        tvProfessorSpeciality = findViewById(R.id.tvProfessorSpeciality);
        tvProfessorExperience = findViewById(R.id.tvProfessorExperience);
        tvCourseDescription = findViewById(R.id.tvCourseDescription);
        rvSections = findViewById(R.id.rvSections);
        btnEnroll = findViewById(R.id.btnEnroll);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadCourseData(int courseId) {
        // Charger le cours depuis SQLite
        course = courseDAO.getCourseById(courseId);

        if (course != null) {
            displayCourseData();
        } else {
            Toast.makeText(this, "Cours non trouvé", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayCourseData() {
        // Image du cours
        Glide.with(this)
                .load(course.getImageUrl())
                .centerCrop()
                .into(ivCourseImage);

        // Infos principales
        tvCourseTitle.setText(course.getTitle());
        tvCourseCategory.setText(course.getCategory());
        tvCourseDescription.setText(course.getDescription());

        // Stats
        tvSectionsCount.setText(course.getTotalSections() + " sections");
        tvVideosCount.setText(course.getTotalVideos() + " vidéos");
        tvDuration.setText(course.getTotalDurationFormatted());

        // Professeur
        Professor prof = course.getProfessor();
        if (prof != null) {
            tvProfessorName.setText(prof.getName());
            tvProfessorSpeciality.setText(prof.getSpeciality());
            tvProfessorExperience.setText(prof.getExperience() + " ans d'expérience");

            Glide.with(this)
                    .load(prof.getImageUrl())
                    .circleCrop()
                    .placeholder(R.drawable.ic_person_placeholder)
                    .into(ivProfessorImage);
        }

        // Sections
        setupSectionsRecyclerView();

        // Bouton d'inscription
        updateEnrollButton();
    }

    private void setupSectionsRecyclerView() {
        rvSections.setLayoutManager(new LinearLayoutManager(this));
        rvSections.setNestedScrollingEnabled(false);
        sectionsAdapter = new CourseSectionsAdapter(course.getSections(), this);
        rvSections.setAdapter(sectionsAdapter);
    }

    private void updateEnrollButton() {
        if (course.isEnrolled()) {
            btnEnroll.setText("Continuer le cours");
            btnEnroll.setOnClickListener(v -> openCourseContent());
        } else {
            btnEnroll.setText("S'inscrire au cours");
            btnEnroll.setOnClickListener(v -> enrollToCourse());
        }
    }

    private void enrollToCourse() {
        // Inscrire au cours via SQLite
        boolean success = courseDAO.enrollCourse(course.getId());
        if (success) {
            course.setEnrolled(true);
            updateEnrollButton();
            Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vous êtes déjà inscrit", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCourseContent() {
        // Ouvrir la première vidéo du premier section
        if (course.getSections() != null && !course.getSections().isEmpty()) {
            CourseSection firstSection = course.getSections().get(0);
            if (firstSection.getVideos() != null && !firstSection.getVideos().isEmpty()) {
                Video firstVideo = firstSection.getVideos().get(0);
                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL, firstVideo.getUrl());
                intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_TITLE, firstVideo.getTitle());
                startActivity(intent);
            } else if (firstSection.getPdfDocument() != null) {
                // Si pas de vidéo, ouvrir le PDF
                PdfDocument pdf = firstSection.getPdfDocument();
                Intent intent = new Intent(this, PdfViewerActivity.class);
                intent.putExtra(PdfViewerActivity.EXTRA_PDF_URL, pdf.getUrl());
                intent.putExtra(PdfViewerActivity.EXTRA_PDF_TITLE, pdf.getTitle());
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Aucun contenu disponible", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPdfDownloadClick(PdfDocument pdf) {
        // Ouvrir le PDF dans PdfViewerActivity
        Intent intent = new Intent(this, PdfViewerActivity.class);
        intent.putExtra(PdfViewerActivity.EXTRA_PDF_URL, pdf.getUrl());
        intent.putExtra(PdfViewerActivity.EXTRA_PDF_TITLE, pdf.getTitle());
        startActivity(intent);
    }

    @Override
    public void onVideoClick(Video video) {
        // Ouvrir la vidéo dans VideoPlayerActivity
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_URL, video.getUrl());
        intent.putExtra(VideoPlayerActivity.EXTRA_VIDEO_TITLE, video.getTitle());
        startActivity(intent);
    }

    // ============== DONNÉES MOCK ==============
    private Course getMockCourse() {
        Professor prof = new Professor(
                1,
                "Dr. Ahmed Ben Ali",
                "Développement Mobile",
                8,
                "https://randomuser.me/api/portraits/men/32.jpg"
        );

        List<CourseSection> sections = new ArrayList<>();

        // Section 1
        PdfDocument pdf1 = new PdfDocument(1, "Introduction au développement Android.pdf",
                "https://www.w3.org/WAI/WCAG21/Techniques/pdf/img/table-word.pdf", 2500000);
        List<Video> videos1 = Arrays.asList(
                new Video(1, "Bienvenue dans le cours",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "https://i.ytimg.com/vi/YE7VzlLtp-4/maxresdefault.jpg", 180, 1),
                new Video(2, "Installation d'Android Studio",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                        "https://i.ytimg.com/vi/KQ4bN4n0pTQ/maxresdefault.jpg", 720, 2),
                new Video(3, "Premier projet Android",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                        "https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg", 540, 3)
        );
        sections.add(new CourseSection(1, "Introduction", 1, pdf1, videos1));

        // Section 2
        PdfDocument pdf2 = new PdfDocument(2, "Les bases du langage Java.pdf",
                "https://www.africau.edu/images/default/sample.pdf", 3200000);
        List<Video> videos2 = Arrays.asList(
                new Video(4, "Variables et types de données",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                        "https://i.ytimg.com/vi/2vjPBrBU-TM/maxresdefault.jpg", 600, 1),
                new Video(5, "Structures de contrôle",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                        "https://i.ytimg.com/vi/3fumBcKC6RE/maxresdefault.jpg", 480, 2),
                new Video(6, "Les classes et objets",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                        "https://i.ytimg.com/vi/5qap5aO4i9A/maxresdefault.jpg", 900, 3),
                new Video(7, "L'héritage en Java",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                        "https://i.ytimg.com/vi/LXb3EKWsInQ/maxresdefault.jpg", 660, 4)
        );
        sections.add(new CourseSection(2, "Fondamentaux Java", 2, pdf2, videos2));

        // Section 3
        PdfDocument pdf3 = new PdfDocument(3, "Interface utilisateur Android.pdf",
                "https://www.orimi.com/pdf-test.pdf", 4100000);
        List<Video> videos3 = Arrays.asList(
                new Video(8, "Les layouts XML",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                        "https://i.ytimg.com/vi/eRsGyueVLvQ/maxresdefault.jpg", 720, 1),
                new Video(9, "Les composants UI",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
                        "https://i.ytimg.com/vi/4Tr0otuiQuU/maxresdefault.jpg", 540, 2),
                new Video(10, "RecyclerView en détail",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                        "https://i.ytimg.com/vi/mhDJNfV7hjk/maxresdefault.jpg", 1200, 3)
        );
        sections.add(new CourseSection(3, "Interfaces utilisateur", 3, pdf3, videos3));

        Course course = new Course(
                1,
                "Développement Android Complet",
                "Ce cours complet vous guidera à travers toutes les étapes du développement Android, " +
                        "de l'installation de l'environnement jusqu'à la publication de votre application sur le Play Store. " +
                        "Vous apprendrez les concepts fondamentaux, les bonnes pratiques et les techniques avancées.",
                "Développement Mobile",
                "https://images.unsplash.com/photo-1607252650355-f7fd0460ccdb?w=800",
                sections,
                prof
        );
        course.setEnrolledCount(1250);
        course.setRating(4.7f);

        return course;
    }
}