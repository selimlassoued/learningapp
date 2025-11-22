package iset.example.learningapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "learning_app.db";
    private static final int DATABASE_VERSION = 1;

    // Table Professors
    public static final String TABLE_PROFESSORS = "professors";
    public static final String COL_PROF_ID = "id";
    public static final String COL_PROF_NAME = "name";
    public static final String COL_PROF_SPECIALITY = "speciality";
    public static final String COL_PROF_EXPERIENCE = "experience";
    public static final String COL_PROF_IMAGE_URL = "image_url";

    // Table Courses
    public static final String TABLE_COURSES = "courses";
    public static final String COL_COURSE_ID = "id";
    public static final String COL_COURSE_TITLE = "title";
    public static final String COL_COURSE_DESCRIPTION = "description";
    public static final String COL_COURSE_CATEGORY = "category";
    public static final String COL_COURSE_IMAGE_URL = "image_url";
    public static final String COL_COURSE_PROF_ID = "professor_id";
    public static final String COL_COURSE_RATING = "rating";
    public static final String COL_COURSE_ENROLLED_COUNT = "enrolled_count";

    // Table Sections
    public static final String TABLE_SECTIONS = "sections";
    public static final String COL_SECTION_ID = "id";
    public static final String COL_SECTION_TITLE = "title";
    public static final String COL_SECTION_ORDER = "section_order";
    public static final String COL_SECTION_COURSE_ID = "course_id";

    // Table PDFs
    public static final String TABLE_PDFS = "pdfs";
    public static final String COL_PDF_ID = "id";
    public static final String COL_PDF_TITLE = "title";
    public static final String COL_PDF_URL = "url";
    public static final String COL_PDF_SIZE = "file_size";
    public static final String COL_PDF_SECTION_ID = "section_id";
    public static final String COL_PDF_IS_DOWNLOADED = "is_downloaded";

    // Table Videos
    public static final String TABLE_VIDEOS = "videos";
    public static final String COL_VIDEO_ID = "id";
    public static final String COL_VIDEO_TITLE = "title";
    public static final String COL_VIDEO_URL = "url";
    public static final String COL_VIDEO_THUMBNAIL = "thumbnail_url";
    public static final String COL_VIDEO_DURATION = "duration";
    public static final String COL_VIDEO_ORDER = "video_order";
    public static final String COL_VIDEO_SECTION_ID = "section_id";

    // Table Enrollments (inscriptions)
    public static final String TABLE_ENROLLMENTS = "enrollments";
    public static final String COL_ENROLL_ID = "id";
    public static final String COL_ENROLL_COURSE_ID = "course_id";
    public static final String COL_ENROLL_DATE = "enrollment_date";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer table Professors
        String createProfessors = "CREATE TABLE " + TABLE_PROFESSORS + " (" +
                COL_PROF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROF_NAME + " TEXT NOT NULL, " +
                COL_PROF_SPECIALITY + " TEXT, " +
                COL_PROF_EXPERIENCE + " INTEGER, " +
                COL_PROF_IMAGE_URL + " TEXT)";
        db.execSQL(createProfessors);

        // Créer table Courses
        String createCourses = "CREATE TABLE " + TABLE_COURSES + " (" +
                COL_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COURSE_TITLE + " TEXT NOT NULL, " +
                COL_COURSE_DESCRIPTION + " TEXT, " +
                COL_COURSE_CATEGORY + " TEXT, " +
                COL_COURSE_IMAGE_URL + " TEXT, " +
                COL_COURSE_PROF_ID + " INTEGER, " +
                COL_COURSE_RATING + " REAL DEFAULT 0, " +
                COL_COURSE_ENROLLED_COUNT + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_COURSE_PROF_ID + ") REFERENCES " +
                TABLE_PROFESSORS + "(" + COL_PROF_ID + "))";
        db.execSQL(createCourses);

        // Créer table Sections
        String createSections = "CREATE TABLE " + TABLE_SECTIONS + " (" +
                COL_SECTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SECTION_TITLE + " TEXT NOT NULL, " +
                COL_SECTION_ORDER + " INTEGER, " +
                COL_SECTION_COURSE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_SECTION_COURSE_ID + ") REFERENCES " +
                TABLE_COURSES + "(" + COL_COURSE_ID + "))";
        db.execSQL(createSections);

        // Créer table PDFs
        String createPdfs = "CREATE TABLE " + TABLE_PDFS + " (" +
                COL_PDF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PDF_TITLE + " TEXT NOT NULL, " +
                COL_PDF_URL + " TEXT, " +
                COL_PDF_SIZE + " INTEGER, " +
                COL_PDF_SECTION_ID + " INTEGER, " +
                COL_PDF_IS_DOWNLOADED + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_PDF_SECTION_ID + ") REFERENCES " +
                TABLE_SECTIONS + "(" + COL_SECTION_ID + "))";
        db.execSQL(createPdfs);

        // Créer table Videos
        String createVideos = "CREATE TABLE " + TABLE_VIDEOS + " (" +
                COL_VIDEO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_VIDEO_TITLE + " TEXT NOT NULL, " +
                COL_VIDEO_URL + " TEXT, " +
                COL_VIDEO_THUMBNAIL + " TEXT, " +
                COL_VIDEO_DURATION + " INTEGER, " +
                COL_VIDEO_ORDER + " INTEGER, " +
                COL_VIDEO_SECTION_ID + " INTEGER, " +
                "FOREIGN KEY(" + COL_VIDEO_SECTION_ID + ") REFERENCES " +
                TABLE_SECTIONS + "(" + COL_SECTION_ID + "))";
        db.execSQL(createVideos);

        // Créer table Enrollments
        String createEnrollments = "CREATE TABLE " + TABLE_ENROLLMENTS + " (" +
                COL_ENROLL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ENROLL_COURSE_ID + " INTEGER, " +
                COL_ENROLL_DATE + " TEXT, " +
                "FOREIGN KEY(" + COL_ENROLL_COURSE_ID + ") REFERENCES " +
                TABLE_COURSES + "(" + COL_COURSE_ID + "))";
        db.execSQL(createEnrollments);

        // Insérer les données de test
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PDFS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSORS);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Insérer les professeurs
        db.execSQL("INSERT INTO " + TABLE_PROFESSORS + " VALUES (1, 'Dr. Ahmed Ben Ali', 'Développement Mobile', 8, 'https://randomuser.me/api/portraits/men/32.jpg')");
        db.execSQL("INSERT INTO " + TABLE_PROFESSORS + " VALUES (2, 'Dr. Fatma Trabelsi', 'Intelligence Artificielle', 10, 'https://randomuser.me/api/portraits/women/44.jpg')");
        db.execSQL("INSERT INTO " + TABLE_PROFESSORS + " VALUES (3, 'Ing. Mohamed Sassi', 'Développement Web', 6, 'https://randomuser.me/api/portraits/men/67.jpg')");

        // Insérer les cours
        db.execSQL("INSERT INTO " + TABLE_COURSES + " VALUES (1, 'Développement Android Complet', 'Ce cours complet vous guidera à travers toutes les étapes du développement Android.', 'Développement Mobile', 'https://images.unsplash.com/photo-1607252650355-f7fd0460ccdb?w=800', 1, 4.7, 1250)");
        db.execSQL("INSERT INTO " + TABLE_COURSES + " VALUES (2, 'Machine Learning avec Python', 'Maîtrisez les algorithmes de Machine Learning.', 'Intelligence Artificielle', 'https://images.unsplash.com/photo-1555949963-aa79dcee981c?w=800', 2, 4.9, 890)");
        db.execSQL("INSERT INTO " + TABLE_COURSES + " VALUES (3, 'React.js - De Zéro à Expert', 'Construisez des applications web modernes avec React.js.', 'Développement Web', 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=800', 3, 4.8, 2100)");

        // Insérer les sections pour le cours 1
        db.execSQL("INSERT INTO " + TABLE_SECTIONS + " VALUES (1, 'Introduction', 1, 1)");
        db.execSQL("INSERT INTO " + TABLE_SECTIONS + " VALUES (2, 'Fondamentaux Java', 2, 1)");
        db.execSQL("INSERT INTO " + TABLE_SECTIONS + " VALUES (3, 'Interfaces utilisateur', 3, 1)");

        // Insérer les sections pour le cours 2
        db.execSQL("INSERT INTO " + TABLE_SECTIONS + " VALUES (4, 'Introduction au ML', 1, 2)");
        db.execSQL("INSERT INTO " + TABLE_SECTIONS + " VALUES (5, 'Algorithmes supervisés', 2, 2)");

        // Insérer les sections pour le cours 3
        db.execSQL("INSERT INTO " + TABLE_SECTIONS + " VALUES (6, 'Bases de React', 1, 3)");
        db.execSQL("INSERT INTO " + TABLE_SECTIONS + " VALUES (7, 'Hooks et State', 2, 3)");

        // Insérer les PDFs
        db.execSQL("INSERT INTO " + TABLE_PDFS + " VALUES (1, 'Introduction Android.pdf', 'https://www.w3.org/WAI/WCAG21/Techniques/pdf/img/table-word.pdf', 2500000, 1, 0)");
        db.execSQL("INSERT INTO " + TABLE_PDFS + " VALUES (2, 'Java Fondamentaux.pdf', 'https://www.africau.edu/images/default/sample.pdf', 3200000, 2, 0)");
        db.execSQL("INSERT INTO " + TABLE_PDFS + " VALUES (3, 'UI Android.pdf', 'https://www.orimi.com/pdf-test.pdf', 4100000, 3, 0)");

        // Insérer les vidéos
        db.execSQL("INSERT INTO " + TABLE_VIDEOS + " VALUES (1, 'Bienvenue dans le cours', 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4', 'https://i.ytimg.com/vi/YE7VzlLtp-4/maxresdefault.jpg', 180, 1, 1)");
        db.execSQL("INSERT INTO " + TABLE_VIDEOS + " VALUES (2, 'Installation Android Studio', 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4', 'https://i.ytimg.com/vi/KQ4bN4n0pTQ/maxresdefault.jpg', 720, 2, 1)");
        db.execSQL("INSERT INTO " + TABLE_VIDEOS + " VALUES (3, 'Premier projet', 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4', 'https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg', 540, 3, 1)");
        db.execSQL("INSERT INTO " + TABLE_VIDEOS + " VALUES (4, 'Variables Java', 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4', 'https://i.ytimg.com/vi/2vjPBrBU-TM/maxresdefault.jpg', 600, 1, 2)");
        db.execSQL("INSERT INTO " + TABLE_VIDEOS + " VALUES (5, 'Classes et Objets', 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4', 'https://i.ytimg.com/vi/3fumBcKC6RE/maxresdefault.jpg', 480, 2, 2)");
        db.execSQL("INSERT INTO " + TABLE_VIDEOS + " VALUES (6, 'Layouts XML', 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4', 'https://i.ytimg.com/vi/5qap5aO4i9A/maxresdefault.jpg', 900, 1, 3)");
        db.execSQL("INSERT INTO " + TABLE_VIDEOS + " VALUES (7, 'RecyclerView', 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4', 'https://i.ytimg.com/vi/LXb3EKWsInQ/maxresdefault.jpg', 1200, 2, 3)");
    }
}