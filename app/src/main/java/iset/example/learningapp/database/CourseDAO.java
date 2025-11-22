package iset.example.learningapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import iset.example.learningapp.models.Course;
import iset.example.learningapp.models.CourseSection;
import iset.example.learningapp.models.PdfDocument;
import iset.example.learningapp.models.Professor;
import iset.example.learningapp.models.Video;

public class CourseDAO {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public CourseDAO(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    private void open() {
        db = dbHelper.getWritableDatabase();
    }

    private void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // ==================== COURSES ====================

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        open();

        String query = "SELECT c.*, p." + DatabaseHelper.COL_PROF_NAME + ", p." +
                DatabaseHelper.COL_PROF_SPECIALITY + ", p." + DatabaseHelper.COL_PROF_EXPERIENCE +
                ", p." + DatabaseHelper.COL_PROF_IMAGE_URL + " AS prof_image " +
                "FROM " + DatabaseHelper.TABLE_COURSES + " c " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PROFESSORS + " p ON c." +
                DatabaseHelper.COL_COURSE_PROF_ID + " = p." + DatabaseHelper.COL_PROF_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Course course = cursorToCourse(cursor);
                // Charger les sections pour ce cours
                course.setSections(getSectionsByCourseId(course.getId()));
                // Vérifier si inscrit
                course.setEnrolled(isEnrolled(course.getId()));
                courses.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return courses;
    }

    public Course getCourseById(int courseId) {
        open();
        Course course = null;

        String query = "SELECT c.*, p." + DatabaseHelper.COL_PROF_NAME + ", p." +
                DatabaseHelper.COL_PROF_SPECIALITY + ", p." + DatabaseHelper.COL_PROF_EXPERIENCE +
                ", p." + DatabaseHelper.COL_PROF_IMAGE_URL + " AS prof_image " +
                "FROM " + DatabaseHelper.TABLE_COURSES + " c " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PROFESSORS + " p ON c." +
                DatabaseHelper.COL_COURSE_PROF_ID + " = p." + DatabaseHelper.COL_PROF_ID +
                " WHERE c." + DatabaseHelper.COL_COURSE_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseId)});

        if (cursor.moveToFirst()) {
            course = cursorToCourse(cursor);
            course.setSections(getSectionsByCourseId(courseId));
            course.setEnrolled(isEnrolled(courseId));
        }
        cursor.close();
        close();
        return course;
    }

    private Course cursorToCourse(Cursor cursor) {
        Course course = new Course();
        course.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_ID)));
        course.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_TITLE)));
        course.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_DESCRIPTION)));
        course.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_CATEGORY)));
        course.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_IMAGE_URL)));
        course.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_RATING)));
        course.setEnrolledCount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_ENROLLED_COUNT)));

        // Professeur
        Professor prof = new Professor();
        prof.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_COURSE_PROF_ID)));
        prof.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROF_NAME)));
        prof.setSpeciality(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROF_SPECIALITY)));
        prof.setExperience(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROF_EXPERIENCE)));
        prof.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("prof_image")));
        course.setProfessor(prof);

        return course;
    }

    // ==================== SECTIONS ====================

    public List<CourseSection> getSectionsByCourseId(int courseId) {
        List<CourseSection> sections = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_SECTIONS +
                " WHERE " + DatabaseHelper.COL_SECTION_COURSE_ID + " = ? " +
                "ORDER BY " + DatabaseHelper.COL_SECTION_ORDER;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseId)});

        if (cursor.moveToFirst()) {
            do {
                CourseSection section = new CourseSection();
                int sectionId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SECTION_ID));
                section.setId(sectionId);
                section.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SECTION_TITLE)));
                section.setOrder(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SECTION_ORDER)));

                // Charger le PDF de la section
                section.setPdfDocument(getPdfBySectionId(sectionId));
                // Charger les vidéos de la section
                section.setVideos(getVideosBySectionId(sectionId));

                sections.add(section);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sections;
    }

    // ==================== PDFs ====================

    public PdfDocument getPdfBySectionId(int sectionId) {
        PdfDocument pdf = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PDFS +
                " WHERE " + DatabaseHelper.COL_PDF_SECTION_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sectionId)});

        if (cursor.moveToFirst()) {
            pdf = new PdfDocument();
            pdf.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PDF_ID)));
            pdf.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PDF_TITLE)));
            pdf.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PDF_URL)));
            pdf.setFileSize(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PDF_SIZE)));
            pdf.setDownloaded(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PDF_IS_DOWNLOADED)) == 1);
        }
        cursor.close();
        return pdf;
    }

    // ==================== VIDEOS ====================

    public List<Video> getVideosBySectionId(int sectionId) {
        List<Video> videos = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_VIDEOS +
                " WHERE " + DatabaseHelper.COL_VIDEO_SECTION_ID + " = ? " +
                "ORDER BY " + DatabaseHelper.COL_VIDEO_ORDER;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sectionId)});

        if (cursor.moveToFirst()) {
            do {
                Video video = new Video();
                video.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VIDEO_ID)));
                video.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VIDEO_TITLE)));
                video.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VIDEO_URL)));
                video.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VIDEO_THUMBNAIL)));
                video.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VIDEO_DURATION)));
                video.setOrder(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VIDEO_ORDER)));
                videos.add(video);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return videos;
    }

    // ==================== ENROLLMENTS ====================

    public boolean isEnrolled(int courseId) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_ENROLLMENTS +
                " WHERE " + DatabaseHelper.COL_ENROLL_COURSE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(courseId)});
        boolean enrolled = cursor.getCount() > 0;
        cursor.close();
        return enrolled;
    }

    public boolean enrollCourse(int courseId) {
        open();

        // Vérifier si déjà inscrit
        if (isEnrolled(courseId)) {
            close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ENROLL_COURSE_ID, courseId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        values.put(DatabaseHelper.COL_ENROLL_DATE, sdf.format(new Date()));

        long result = db.insert(DatabaseHelper.TABLE_ENROLLMENTS, null, values);

        // Incrémenter le nombre d'inscrits
        if (result != -1) {
            db.execSQL("UPDATE " + DatabaseHelper.TABLE_COURSES +
                    " SET " + DatabaseHelper.COL_COURSE_ENROLLED_COUNT + " = " +
                    DatabaseHelper.COL_COURSE_ENROLLED_COUNT + " + 1 WHERE " +
                    DatabaseHelper.COL_COURSE_ID + " = " + courseId);
        }

        close();
        return result != -1;
    }

    public boolean unenrollCourse(int courseId) {
        open();
        int result = db.delete(DatabaseHelper.TABLE_ENROLLMENTS,
                DatabaseHelper.COL_ENROLL_COURSE_ID + " = ?",
                new String[]{String.valueOf(courseId)});
        close();
        return result > 0;
    }

    public List<Course> getEnrolledCourses() {
        List<Course> courses = new ArrayList<>();
        open();

        String query = "SELECT c.*, p." + DatabaseHelper.COL_PROF_NAME + ", p." +
                DatabaseHelper.COL_PROF_SPECIALITY + ", p." + DatabaseHelper.COL_PROF_EXPERIENCE +
                ", p." + DatabaseHelper.COL_PROF_IMAGE_URL + " AS prof_image " +
                "FROM " + DatabaseHelper.TABLE_COURSES + " c " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PROFESSORS + " p ON c." +
                DatabaseHelper.COL_COURSE_PROF_ID + " = p." + DatabaseHelper.COL_PROF_ID +
                " INNER JOIN " + DatabaseHelper.TABLE_ENROLLMENTS + " e ON c." +
                DatabaseHelper.COL_COURSE_ID + " = e." + DatabaseHelper.COL_ENROLL_COURSE_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Course course = cursorToCourse(cursor);
                course.setSections(getSectionsByCourseId(course.getId()));
                course.setEnrolled(true);
                courses.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return courses;
    }
}