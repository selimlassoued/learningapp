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
import java.util.List;

import iset.example.learningapp.R;
import iset.example.learningapp.adapters.CoursesAdapter;
import iset.example.learningapp.database.CourseDAO;
import iset.example.learningapp.models.Course;

public class MainActivity extends AppCompatActivity implements CoursesAdapter.OnCourseClickListener {

    private RecyclerView rvCourses;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private BottomNavigationView bottomNav;

    private CoursesAdapter coursesAdapter;
    private List<Course> coursesList = new ArrayList<>();
    private CourseDAO courseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser le DAO
        courseDAO = new CourseDAO(this);

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

        // Charger les cours depuis SQLite
        rvCourses.postDelayed(() -> {
            coursesList.clear();
            coursesList.addAll(courseDAO.getAllCourses());
            coursesAdapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            if (coursesList.isEmpty()) {
                layoutEmpty.setVisibility(View.VISIBLE);
                rvCourses.setVisibility(View.GONE);
            } else {
                layoutEmpty.setVisibility(View.GONE);
                rvCourses.setVisibility(View.VISIBLE);
            }
        }, 300);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recharger les cours quand on revient sur l'activité
        loadCourses();
    }

    @Override
    public void onCourseClick(Course course) {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra(CourseDetailActivity.EXTRA_COURSE_ID, course.getId());
        startActivity(intent);
    }
}