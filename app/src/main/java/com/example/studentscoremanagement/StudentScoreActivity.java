package com.example.studentscoremanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.studentscoremanagement.Adapter.ScoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudentScoreActivity extends AppCompatActivity {
    private RecyclerView recyclerViewScores;
    private Button btnLogout; // Thêm btnLogout
    private DBHelper dbHelper;
    private int maHocSinh;
    private ScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_score);

        recyclerViewScores = findViewById(R.id.recyclerViewScores);
        btnLogout = findViewById(R.id.btnLogout);
        dbHelper = new DBHelper(this);
        maHocSinh = getIntent().getIntExtra("maHocSinh", -1);

        recyclerViewScores.setLayoutManager(new LinearLayoutManager(this));
        loadScores();

        // Sự kiện cho nút Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentScoreActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa stack Activity
                startActivity(intent);
                finish(); // Đóng StudentScoreActivity
            }
        });
    }

    private void loadScores() {
        List<Score> scoreList = new ArrayList<>();
        Cursor cursor = dbHelper.GetData(
                "SELECT " + DBHelper.TB_MONHOC + "." + DBHelper.COL_MONHOC_TENMONHOC + ", " +
                        DBHelper.TB_DIEM + "." + DBHelper.COL_DIEM_DIEM +
                        " FROM " + DBHelper.TB_DIEM +
                        " INNER JOIN " + DBHelper.TB_MONHOC +
                        " ON " + DBHelper.TB_DIEM + "." + DBHelper.COL_DIEM_MAMONHOC + " = " +
                        DBHelper.TB_MONHOC + "." + DBHelper.COL_MONHOC_MAMONHOC +
                        " WHERE " + DBHelper.TB_DIEM + "." + DBHelper.COL_DIEM_MAHOCSINH + " = '" + maHocSinh + "'",
                null
        );

        while (cursor.moveToNext()) {
            String tenMonHoc = cursor.getString(0);
            float diem = cursor.getFloat(1);
            scoreList.add(new Score(tenMonHoc, diem));
        }
        cursor.close();

        scoreAdapter = new ScoreAdapter(scoreList);
        recyclerViewScores.setAdapter(scoreAdapter);
    }
}